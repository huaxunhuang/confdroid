/**
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.net.util;


/**
 *
 *
 * @unknown 
 */
public class IpUtils {
    /**
     * Converts a signed short value to an unsigned int value.  Needed
     * because Java does not have unsigned types.
     */
    private static int intAbs(short v) {
        return v & 0xffff;
    }

    /**
     * Performs an IP checksum (used in IP header and across UDP
     * payload) on the specified portion of a ByteBuffer.  The seed
     * allows the checksum to commence with a specified value.
     */
    private static int checksum(java.nio.ByteBuffer buf, int seed, int start, int end) {
        int sum = seed;
        final int bufPosition = buf.position();
        // set position of original ByteBuffer, so that the ShortBuffer
        // will be correctly initialized
        buf.position(start);
        java.nio.ShortBuffer shortBuf = buf.asShortBuffer();
        // re-set ByteBuffer position
        buf.position(bufPosition);
        final int numShorts = (end - start) / 2;
        for (int i = 0; i < numShorts; i++) {
            sum += android.net.util.IpUtils.intAbs(shortBuf.get(i));
        }
        start += numShorts * 2;
        // see if a singleton byte remains
        if (end != start) {
            short b = buf.get(start);
            // make it unsigned
            if (b < 0) {
                b += 256;
            }
            sum += b * 256;
        }
        sum = ((sum >> 16) & 0xffff) + (sum & 0xffff);
        sum = (sum + ((sum >> 16) & 0xffff)) & 0xffff;
        int negated = ~sum;
        return android.net.util.IpUtils.intAbs(((short) (negated)));
    }

    private static int pseudoChecksumIPv4(java.nio.ByteBuffer buf, int headerOffset, int protocol, int transportLen) {
        int partial = protocol + transportLen;
        partial += android.net.util.IpUtils.intAbs(buf.getShort(headerOffset + 12));
        partial += android.net.util.IpUtils.intAbs(buf.getShort(headerOffset + 14));
        partial += android.net.util.IpUtils.intAbs(buf.getShort(headerOffset + 16));
        partial += android.net.util.IpUtils.intAbs(buf.getShort(headerOffset + 18));
        return partial;
    }

    private static int pseudoChecksumIPv6(java.nio.ByteBuffer buf, int headerOffset, int protocol, int transportLen) {
        int partial = protocol + transportLen;
        for (int offset = 8; offset < 40; offset += 2) {
            partial += android.net.util.IpUtils.intAbs(buf.getShort(headerOffset + offset));
        }
        return partial;
    }

    private static byte ipversion(java.nio.ByteBuffer buf, int headerOffset) {
        return ((byte) ((buf.get(headerOffset) & ((byte) (0xf0))) >> 4));
    }

    public static short ipChecksum(java.nio.ByteBuffer buf, int headerOffset) {
        byte ihl = ((byte) (buf.get(headerOffset) & 0xf));
        return ((short) (android.net.util.IpUtils.checksum(buf, 0, headerOffset, headerOffset + (ihl * 4))));
    }

    private static short transportChecksum(java.nio.ByteBuffer buf, int protocol, int ipOffset, int transportOffset, int transportLen) {
        if (transportLen < 0) {
            throw new java.lang.IllegalArgumentException("Transport length < 0: " + transportLen);
        }
        int sum;
        byte ver = android.net.util.IpUtils.ipversion(buf, ipOffset);
        if (ver == 4) {
            sum = android.net.util.IpUtils.pseudoChecksumIPv4(buf, ipOffset, protocol, transportLen);
        } else
            if (ver == 6) {
                sum = android.net.util.IpUtils.pseudoChecksumIPv6(buf, ipOffset, protocol, transportLen);
            } else {
                throw new java.lang.UnsupportedOperationException("Checksum must be IPv4 or IPv6");
            }

        sum = android.net.util.IpUtils.checksum(buf, sum, transportOffset, transportOffset + transportLen);
        if ((protocol == android.system.OsConstants.IPPROTO_UDP) && (sum == 0)) {
            sum = ((short) (0xffff));
        }
        return ((short) (sum));
    }

    public static short udpChecksum(java.nio.ByteBuffer buf, int ipOffset, int transportOffset) {
        int transportLen = android.net.util.IpUtils.intAbs(buf.getShort(transportOffset + 4));
        return android.net.util.IpUtils.transportChecksum(buf, android.system.OsConstants.IPPROTO_UDP, ipOffset, transportOffset, transportLen);
    }

    public static short tcpChecksum(java.nio.ByteBuffer buf, int ipOffset, int transportOffset, int transportLen) {
        return android.net.util.IpUtils.transportChecksum(buf, android.system.OsConstants.IPPROTO_TCP, ipOffset, transportOffset, transportLen);
    }

    public static java.lang.String addressAndPortToString(java.net.InetAddress address, int port) {
        return java.lang.String.format(address instanceof java.net.Inet6Address ? "[%s]:%d" : "%s:%d", address.getHostAddress(), port);
    }

    public static boolean isValidUdpOrTcpPort(int port) {
        return (port > 0) && (port < 65536);
    }
}

