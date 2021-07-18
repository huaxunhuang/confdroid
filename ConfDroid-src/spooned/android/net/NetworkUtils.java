/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.net;


/**
 * Native methods for managing network interfaces.
 *
 * {@hide }
 */
public class NetworkUtils {
    private static final java.lang.String TAG = "NetworkUtils";

    /**
     * Attaches a socket filter that accepts DHCP packets to the given socket.
     */
    public static native void attachDhcpFilter(java.io.FileDescriptor fd) throws java.net.SocketException;

    /**
     * Attaches a socket filter that accepts ICMPv6 router advertisements to the given socket.
     *
     * @param fd
     * 		the socket's {@link FileDescriptor}.
     * @param packetType
     * 		the hardware address type, one of ARPHRD_*.
     */
    public static native void attachRaFilter(java.io.FileDescriptor fd, int packetType) throws java.net.SocketException;

    /**
     * Configures a socket for receiving ICMPv6 router solicitations and sending advertisements.
     *
     * @param fd
     * 		the socket's {@link FileDescriptor}.
     * @param ifIndex
     * 		the interface index.
     */
    public static native void setupRaSocket(java.io.FileDescriptor fd, int ifIndex) throws java.net.SocketException;

    /**
     * Binds the current process to the network designated by {@code netId}.  All sockets created
     * in the future (and not explicitly bound via a bound {@link SocketFactory} (see
     * {@link Network#getSocketFactory}) will be bound to this network.  Note that if this
     * {@code Network} ever disconnects all sockets created in this way will cease to work.  This
     * is by design so an application doesn't accidentally use sockets it thinks are still bound to
     * a particular {@code Network}.  Passing NETID_UNSET clears the binding.
     */
    public static native boolean bindProcessToNetwork(int netId);

    /**
     * Return the netId last passed to {@link #bindProcessToNetwork}, or NETID_UNSET if
     * {@link #unbindProcessToNetwork} has been called since {@link #bindProcessToNetwork}.
     */
    public static native int getBoundNetworkForProcess();

    /**
     * Binds host resolutions performed by this process to the network designated by {@code netId}.
     * {@link #bindProcessToNetwork} takes precedence over this setting.  Passing NETID_UNSET clears
     * the binding.
     *
     * @deprecated This is strictly for legacy usage to support startUsingNetworkFeature().
     */
    public static native boolean bindProcessToNetworkForHostResolution(int netId);

    /**
     * Explicitly binds {@code socketfd} to the network designated by {@code netId}.  This
     * overrides any binding via {@link #bindProcessToNetwork}.
     *
     * @return 0 on success or negative errno on failure.
     */
    public static native int bindSocketToNetwork(int socketfd, int netId);

    /**
     * Protect {@code fd} from VPN connections.  After protecting, data sent through
     * this socket will go directly to the underlying network, so its traffic will not be
     * forwarded through the VPN.
     */
    public static boolean protectFromVpn(java.io.FileDescriptor fd) {
        return android.net.NetworkUtils.protectFromVpn(fd.getInt$());
    }

    /**
     * Protect {@code socketfd} from VPN connections.  After protecting, data sent through
     * this socket will go directly to the underlying network, so its traffic will not be
     * forwarded through the VPN.
     */
    public static native boolean protectFromVpn(int socketfd);

    /**
     * Determine if {@code uid} can access network designated by {@code netId}.
     *
     * @return {@code true} if {@code uid} can access network, {@code false} otherwise.
     */
    public static native boolean queryUserAccess(int uid, int netId);

    /**
     * Convert a IPv4 address from an integer to an InetAddress.
     *
     * @param hostAddress
     * 		an int corresponding to the IPv4 address in network byte order
     */
    public static java.net.InetAddress intToInetAddress(int hostAddress) {
        byte[] addressBytes = new byte[]{ ((byte) (0xff & hostAddress)), ((byte) (0xff & (hostAddress >> 8))), ((byte) (0xff & (hostAddress >> 16))), ((byte) (0xff & (hostAddress >> 24))) };
        try {
            return java.net.InetAddress.getByAddress(addressBytes);
        } catch (java.net.UnknownHostException e) {
            throw new java.lang.AssertionError();
        }
    }

    /**
     * Convert a IPv4 address from an InetAddress to an integer
     *
     * @param inetAddr
     * 		is an InetAddress corresponding to the IPv4 address
     * @return the IP address as an integer in network byte order
     */
    public static int inetAddressToInt(java.net.Inet4Address inetAddr) throws java.lang.IllegalArgumentException {
        byte[] addr = inetAddr.getAddress();
        return ((((addr[3] & 0xff) << 24) | ((addr[2] & 0xff) << 16)) | ((addr[1] & 0xff) << 8)) | (addr[0] & 0xff);
    }

    /**
     * Convert a network prefix length to an IPv4 netmask integer
     *
     * @param prefixLength
     * 		
     * @return the IPv4 netmask as an integer in network byte order
     */
    public static int prefixLengthToNetmaskInt(int prefixLength) throws java.lang.IllegalArgumentException {
        if ((prefixLength < 0) || (prefixLength > 32)) {
            throw new java.lang.IllegalArgumentException("Invalid prefix length (0 <= prefix <= 32)");
        }
        int value = 0xffffffff << (32 - prefixLength);
        return java.lang.Integer.reverseBytes(value);
    }

    /**
     * Convert a IPv4 netmask integer to a prefix length
     *
     * @param netmask
     * 		as an integer in network byte order
     * @return the network prefix length
     */
    public static int netmaskIntToPrefixLength(int netmask) {
        return java.lang.Integer.bitCount(netmask);
    }

    /**
     * Convert an IPv4 netmask to a prefix length, checking that the netmask is contiguous.
     *
     * @param netmask
     * 		as a {@code Inet4Address}.
     * @return the network prefix length
     * @throws IllegalArgumentException
     * 		the specified netmask was not contiguous.
     * @unknown 
     */
    public static int netmaskToPrefixLength(java.net.Inet4Address netmask) {
        // inetAddressToInt returns an int in *network* byte order.
        int i = java.lang.Integer.reverseBytes(android.net.NetworkUtils.inetAddressToInt(netmask));
        int prefixLength = java.lang.Integer.bitCount(i);
        int trailingZeros = java.lang.Integer.numberOfTrailingZeros(i);
        if (trailingZeros != (32 - prefixLength)) {
            throw new java.lang.IllegalArgumentException("Non-contiguous netmask: " + java.lang.Integer.toHexString(i));
        }
        return prefixLength;
    }

    /**
     * Create an InetAddress from a string where the string must be a standard
     * representation of a V4 or V6 address.  Avoids doing a DNS lookup on failure
     * but it will throw an IllegalArgumentException in that case.
     *
     * @param addrString
     * 		
     * @return the InetAddress
     * @unknown 
     */
    public static java.net.InetAddress numericToInetAddress(java.lang.String addrString) throws java.lang.IllegalArgumentException {
        return java.net.InetAddress.parseNumericAddress(addrString);
    }

    /**
     * Writes an InetAddress to a parcel. The address may be null. This is likely faster than
     * calling writeSerializable.
     */
    protected static void parcelInetAddress(android.os.Parcel parcel, java.net.InetAddress address, int flags) {
        byte[] addressArray = (address != null) ? address.getAddress() : null;
        parcel.writeByteArray(addressArray);
    }

    /**
     * Reads an InetAddress from a parcel. Returns null if the address that was written was null
     * or if the data is invalid.
     */
    protected static java.net.InetAddress unparcelInetAddress(android.os.Parcel in) {
        byte[] addressArray = in.createByteArray();
        if (addressArray == null) {
            return null;
        }
        try {
            return java.net.InetAddress.getByAddress(addressArray);
        } catch (java.net.UnknownHostException e) {
            return null;
        }
    }

    /**
     * Masks a raw IP address byte array with the specified prefix length.
     */
    public static void maskRawAddress(byte[] array, int prefixLength) {
        if ((prefixLength < 0) || (prefixLength > (array.length * 8))) {
            throw new java.lang.RuntimeException((("IP address with " + array.length) + " bytes has invalid prefix length ") + prefixLength);
        }
        int offset = prefixLength / 8;
        int remainder = prefixLength % 8;
        byte mask = ((byte) (0xff << (8 - remainder)));
        if (offset < array.length)
            array[offset] = ((byte) (array[offset] & mask));

        offset++;
        for (; offset < array.length; offset++) {
            array[offset] = 0;
        }
    }

    /**
     * Get InetAddress masked with prefixLength.  Will never return null.
     *
     * @param address
     * 		the IP address to mask with
     * @param prefixLength
     * 		the prefixLength used to mask the IP
     */
    public static java.net.InetAddress getNetworkPart(java.net.InetAddress address, int prefixLength) {
        byte[] array = address.getAddress();
        android.net.NetworkUtils.maskRawAddress(array, prefixLength);
        java.net.InetAddress netPart = null;
        try {
            netPart = java.net.InetAddress.getByAddress(array);
        } catch (java.net.UnknownHostException e) {
            throw new java.lang.RuntimeException("getNetworkPart error - " + e.toString());
        }
        return netPart;
    }

    /**
     * Returns the implicit netmask of an IPv4 address, as was the custom before 1993.
     */
    public static int getImplicitNetmask(java.net.Inet4Address address) {
        int firstByte = address.getAddress()[0] & 0xff;// Convert to an unsigned value.

        if (firstByte < 128) {
            return 8;
        } else
            if (firstByte < 192) {
                return 16;
            } else
                if (firstByte < 224) {
                    return 24;
                } else {
                    return 32;// Will likely not end well for other reasons.

                }


    }

    /**
     * Utility method to parse strings such as "192.0.2.5/24" or "2001:db8::cafe:d00d/64".
     *
     * @unknown 
     */
    public static android.util.Pair<java.net.InetAddress, java.lang.Integer> parseIpAndMask(java.lang.String ipAndMaskString) {
        java.net.InetAddress address = null;
        int prefixLength = -1;
        try {
            java.lang.String[] pieces = ipAndMaskString.split("/", 2);
            prefixLength = java.lang.Integer.parseInt(pieces[1]);
            address = java.net.InetAddress.parseNumericAddress(pieces[0]);
        } catch (java.lang.NullPointerException e) {
            // Null string.
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            // No prefix length.
        } catch (java.lang.NumberFormatException e) {
            // Non-numeric prefix.
        } catch (java.lang.IllegalArgumentException e) {
            // Invalid IP address.
        }
        if ((address == null) || (prefixLength == (-1))) {
            throw new java.lang.IllegalArgumentException("Invalid IP address and mask " + ipAndMaskString);
        }
        return new android.util.Pair<java.net.InetAddress, java.lang.Integer>(address, prefixLength);
    }

    /**
     * Check if IP address type is consistent between two InetAddress.
     *
     * @return true if both are the same type.  False otherwise.
     */
    public static boolean addressTypeMatches(java.net.InetAddress left, java.net.InetAddress right) {
        return ((left instanceof java.net.Inet4Address) && (right instanceof java.net.Inet4Address)) || ((left instanceof java.net.Inet6Address) && (right instanceof java.net.Inet6Address));
    }

    /**
     * Convert a 32 char hex string into a Inet6Address.
     * throws a runtime exception if the string isn't 32 chars, isn't hex or can't be
     * made into an Inet6Address
     *
     * @param addrHexString
     * 		a 32 character hex string representing an IPv6 addr
     * @return addr an InetAddress representation for the string
     */
    public static java.net.InetAddress hexToInet6Address(java.lang.String addrHexString) throws java.lang.IllegalArgumentException {
        try {
            return android.net.NetworkUtils.numericToInetAddress(java.lang.String.format(java.util.Locale.US, "%s:%s:%s:%s:%s:%s:%s:%s", addrHexString.substring(0, 4), addrHexString.substring(4, 8), addrHexString.substring(8, 12), addrHexString.substring(12, 16), addrHexString.substring(16, 20), addrHexString.substring(20, 24), addrHexString.substring(24, 28), addrHexString.substring(28, 32)));
        } catch (java.lang.Exception e) {
            android.util.Log.e("NetworkUtils", (("error in hexToInet6Address(" + addrHexString) + "): ") + e);
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    /**
     * Create a string array of host addresses from a collection of InetAddresses
     *
     * @param addrs
     * 		a Collection of InetAddresses
     * @return an array of Strings containing their host addresses
     */
    public static java.lang.String[] makeStrings(java.util.Collection<java.net.InetAddress> addrs) {
        java.lang.String[] result = new java.lang.String[addrs.size()];
        int i = 0;
        for (java.net.InetAddress addr : addrs) {
            result[i++] = addr.getHostAddress();
        }
        return result;
    }

    /**
     * Trim leading zeros from IPv4 address strings
     * Our base libraries will interpret that as octel..
     * Must leave non v4 addresses and host names alone.
     * For example, 192.168.000.010 -> 192.168.0.10
     * TODO - fix base libraries and remove this function
     *
     * @param addr
     * 		a string representing an ip addr
     * @return a string propertly trimmed
     */
    public static java.lang.String trimV4AddrZeros(java.lang.String addr) {
        if (addr == null)
            return null;

        java.lang.String[] octets = addr.split("\\.");
        if (octets.length != 4)
            return addr;

        java.lang.StringBuilder builder = new java.lang.StringBuilder(16);
        java.lang.String result = null;
        for (int i = 0; i < 4; i++) {
            try {
                if (octets[i].length() > 3)
                    return addr;

                builder.append(java.lang.Integer.parseInt(octets[i]));
            } catch (java.lang.NumberFormatException e) {
                return addr;
            }
            if (i < 3)
                builder.append('.');

        }
        result = builder.toString();
        return result;
    }
}

