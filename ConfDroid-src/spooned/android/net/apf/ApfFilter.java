/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.net.apf;


/**
 * For networks that support packet filtering via APF programs, {@code ApfFilter}
 * listens for IPv6 ICMPv6 router advertisements (RAs) and generates APF programs to
 * filter out redundant duplicate ones.
 *
 * Threading model:
 * A collection of RAs we've received is kept in mRas. Generating APF programs uses mRas to
 * know what RAs to filter for, thus generating APF programs is dependent on mRas.
 * mRas can be accessed by multiple threads:
 * - ReceiveThread, which listens for RAs and adds them to mRas, and generates APF programs.
 * - callers of:
 *    - setMulticastFilter(), which can cause an APF program to be generated.
 *    - dump(), which dumps mRas among other things.
 *    - shutdown(), which clears mRas.
 * So access to mRas is synchronized.
 *
 * @unknown 
 */
public class ApfFilter {
    // Enums describing the outcome of receiving an RA packet.
    // APF program updated for expiry
    private static enum ProcessRaResult {

        MATCH,
        // Received RA matched a known RA
        DROPPED,
        // Received RA ignored due to MAX_RAS
        PARSE_ERROR,
        // Received RA could not be parsed
        ZERO_LIFETIME,
        // Received RA had 0 lifetime
        UPDATE_NEW_RA,
        // APF program updated for new RA
        UPDATE_EXPIRY;}

    // Thread to listen for RAs.
    @com.android.internal.annotations.VisibleForTesting
    class ReceiveThread extends java.lang.Thread {
        private final byte[] mPacket = new byte[1514];

        private final java.io.FileDescriptor mSocket;

        private volatile boolean mStopped;

        // Starting time of the RA receiver thread.
        private final long mStart = android.os.SystemClock.elapsedRealtime();

        private int mReceivedRas;// Number of received RAs


        private int mMatchingRas;// Number of received RAs matching a known RA


        private int mDroppedRas;// Number of received RAs ignored due to the MAX_RAS limit


        private int mParseErrors;// Number of received RAs that could not be parsed


        private int mZeroLifetimeRas;// Number of received RAs with a 0 lifetime


        private int mProgramUpdates;// Number of APF program updates triggered by receiving a RA


        public ReceiveThread(java.io.FileDescriptor socket) {
            mSocket = socket;
        }

        public void halt() {
            mStopped = true;
            try {
                // Interrupts the read() call the thread is blocked in.
                libcore.io.IoBridge.closeAndSignalBlockedThreads(mSocket);
            } catch (java.io.IOException ignored) {
            }
        }

        @java.lang.Override
        public void run() {
            log("begin monitoring");
            while (!mStopped) {
                try {
                    int length = android.system.Os.read(mSocket, mPacket, 0, mPacket.length);
                    updateStats(processRa(mPacket, length));
                } catch (java.io.IOException | android.system.ErrnoException e) {
                    if (!mStopped) {
                        android.util.Log.e(android.net.apf.ApfFilter.TAG, "Read error", e);
                    }
                }
            } 
            logStats();
        }

        private void updateStats(android.net.apf.ApfFilter.ProcessRaResult result) {
            mReceivedRas++;
            switch (result) {
                case MATCH :
                    mMatchingRas++;
                    return;
                case DROPPED :
                    mDroppedRas++;
                    return;
                case PARSE_ERROR :
                    mParseErrors++;
                    return;
                case ZERO_LIFETIME :
                    mZeroLifetimeRas++;
                    return;
                case UPDATE_EXPIRY :
                    mMatchingRas++;
                    mProgramUpdates++;
                    return;
                case UPDATE_NEW_RA :
                    mProgramUpdates++;
                    return;
            }
        }

        private void logStats() {
            long durationMs = android.os.SystemClock.elapsedRealtime() - mStart;
            int maxSize = mApfCapabilities.maximumApfProgramSize;
            mMetricsLog.log(new android.net.metrics.ApfStats(durationMs, mReceivedRas, mMatchingRas, mDroppedRas, mZeroLifetimeRas, mParseErrors, mProgramUpdates, maxSize));
        }
    }

    private static final java.lang.String TAG = "ApfFilter";

    private static final boolean DBG = true;

    private static final boolean VDBG = false;

    private static final int ETH_HEADER_LEN = 14;

    private static final int ETH_DEST_ADDR_OFFSET = 0;

    private static final int ETH_ETHERTYPE_OFFSET = 12;

    private static final byte[] ETH_BROADCAST_MAC_ADDRESS = new byte[]{ ((byte) (0xff)), ((byte) (0xff)), ((byte) (0xff)), ((byte) (0xff)), ((byte) (0xff)), ((byte) (0xff)) };

    // TODO: Make these offsets relative to end of link-layer header; don't include ETH_HEADER_LEN.
    private static final int IPV4_FRAGMENT_OFFSET_OFFSET = android.net.apf.ApfFilter.ETH_HEADER_LEN + 6;

    // Endianness is not an issue for this constant because the APF interpreter always operates in
    // network byte order.
    private static final int IPV4_FRAGMENT_OFFSET_MASK = 0x1fff;

    private static final int IPV4_PROTOCOL_OFFSET = android.net.apf.ApfFilter.ETH_HEADER_LEN + 9;

    private static final int IPV4_DEST_ADDR_OFFSET = android.net.apf.ApfFilter.ETH_HEADER_LEN + 16;

    private static final int IPV4_ANY_HOST_ADDRESS = 0;

    private static final int IPV4_BROADCAST_ADDRESS = -1;// 255.255.255.255


    private static final int IPV6_NEXT_HEADER_OFFSET = android.net.apf.ApfFilter.ETH_HEADER_LEN + 6;

    private static final int IPV6_SRC_ADDR_OFFSET = android.net.apf.ApfFilter.ETH_HEADER_LEN + 8;

    private static final int IPV6_DEST_ADDR_OFFSET = android.net.apf.ApfFilter.ETH_HEADER_LEN + 24;

    private static final int IPV6_HEADER_LEN = 40;

    // The IPv6 all nodes address ff02::1
    private static final byte[] IPV6_ALL_NODES_ADDRESS = new byte[]{ ((byte) (0xff)), 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 };

    private static final int ICMP6_TYPE_OFFSET = android.net.apf.ApfFilter.ETH_HEADER_LEN + android.net.apf.ApfFilter.IPV6_HEADER_LEN;

    private static final int ICMP6_NEIGHBOR_ANNOUNCEMENT = 136;

    private static final int ICMP6_ROUTER_ADVERTISEMENT = 134;

    // NOTE: this must be added to the IPv4 header length in IPV4_HEADER_SIZE_MEMORY_SLOT
    private static final int UDP_DESTINATION_PORT_OFFSET = android.net.apf.ApfFilter.ETH_HEADER_LEN + 2;

    private static final int UDP_HEADER_LEN = 8;

    private static final int DHCP_CLIENT_PORT = 68;

    // NOTE: this must be added to the IPv4 header length in IPV4_HEADER_SIZE_MEMORY_SLOT
    private static final int DHCP_CLIENT_MAC_OFFSET = (android.net.apf.ApfFilter.ETH_HEADER_LEN + android.net.apf.ApfFilter.UDP_HEADER_LEN) + 28;

    private static final int ARP_HEADER_OFFSET = android.net.apf.ApfFilter.ETH_HEADER_LEN;

    private static final int ARP_OPCODE_OFFSET = android.net.apf.ApfFilter.ARP_HEADER_OFFSET + 6;

    private static final short ARP_OPCODE_REQUEST = 1;

    private static final short ARP_OPCODE_REPLY = 2;

    private static final byte[] ARP_IPV4_HEADER = new byte[]{ 0, 1// Hardware type: Ethernet (1)
    , 8, 0// Protocol type: IP (0x0800)
    , 6// Hardware size: 6
    , 4// Protocol size: 4
     };

    private static final int ARP_TARGET_IP_ADDRESS_OFFSET = android.net.apf.ApfFilter.ETH_HEADER_LEN + 24;

    private final android.net.apf.ApfCapabilities mApfCapabilities;

    private final android.net.ip.IpManager.Callback mIpManagerCallback;

    private final java.net.NetworkInterface mNetworkInterface;

    private final android.net.metrics.IpConnectivityLog mMetricsLog;

    @com.android.internal.annotations.VisibleForTesting
    byte[] mHardwareAddress;

    @com.android.internal.annotations.VisibleForTesting
    android.net.apf.ApfFilter.ReceiveThread mReceiveThread;

    @com.android.internal.annotations.GuardedBy("this")
    private long mUniqueCounter;

    @com.android.internal.annotations.GuardedBy("this")
    private boolean mMulticastFilter;

    // Our IPv4 address, if we have just one, otherwise null.
    @com.android.internal.annotations.GuardedBy("this")
    private byte[] mIPv4Address;

    // The subnet prefix length of our IPv4 network. Only valid if mIPv4Address is not null.
    @com.android.internal.annotations.GuardedBy("this")
    private int mIPv4PrefixLength;

    @com.android.internal.annotations.VisibleForTesting
    ApfFilter(android.net.apf.ApfCapabilities apfCapabilities, java.net.NetworkInterface networkInterface, android.net.ip.IpManager.Callback ipManagerCallback, boolean multicastFilter, android.net.metrics.IpConnectivityLog log) {
        mApfCapabilities = apfCapabilities;
        mIpManagerCallback = ipManagerCallback;
        mNetworkInterface = networkInterface;
        mMulticastFilter = multicastFilter;
        mMetricsLog = log;
        maybeStartFilter();
    }

    private void log(java.lang.String s) {
        android.util.Log.d(android.net.apf.ApfFilter.TAG, (("(" + mNetworkInterface.getName()) + "): ") + s);
    }

    @com.android.internal.annotations.GuardedBy("this")
    private long getUniqueNumberLocked() {
        return mUniqueCounter++;
    }

    /**
     * Attempt to start listening for RAs and, if RAs are received, generating and installing
     * filters to ignore useless RAs.
     */
    @com.android.internal.annotations.VisibleForTesting
    void maybeStartFilter() {
        java.io.FileDescriptor socket;
        try {
            mHardwareAddress = mNetworkInterface.getHardwareAddress();
            synchronized(this) {
                // Install basic filters
                installNewProgramLocked();
            }
            socket = android.system.Os.socket(android.system.OsConstants.AF_PACKET, android.system.OsConstants.SOCK_RAW, android.system.OsConstants.ETH_P_IPV6);
            android.system.PacketSocketAddress addr = new android.system.PacketSocketAddress(((short) (android.system.OsConstants.ETH_P_IPV6)), mNetworkInterface.getIndex());
            android.system.Os.bind(socket, addr);
            android.net.NetworkUtils.attachRaFilter(socket, mApfCapabilities.apfPacketFormat);
        } catch (java.net.SocketException | android.system.ErrnoException e) {
            android.util.Log.e(android.net.apf.ApfFilter.TAG, "Error starting filter", e);
            return;
        }
        mReceiveThread = new android.net.apf.ApfFilter.ReceiveThread(socket);
        mReceiveThread.start();
    }

    // Returns seconds since Unix Epoch.
    // TODO: use SystemClock.elapsedRealtime() instead
    private static long curTime() {
        return java.lang.System.currentTimeMillis() / android.text.format.DateUtils.SECOND_IN_MILLIS;
    }

    // A class to hold information about an RA.
    private class Ra {
        // From RFC4861:
        private static final int ICMP6_RA_HEADER_LEN = 16;

        private static final int ICMP6_RA_CHECKSUM_OFFSET = (android.net.apf.ApfFilter.ETH_HEADER_LEN + android.net.apf.ApfFilter.IPV6_HEADER_LEN) + 2;

        private static final int ICMP6_RA_CHECKSUM_LEN = 2;

        private static final int ICMP6_RA_OPTION_OFFSET = (android.net.apf.ApfFilter.ETH_HEADER_LEN + android.net.apf.ApfFilter.IPV6_HEADER_LEN) + android.net.apf.ApfFilter.Ra.ICMP6_RA_HEADER_LEN;

        private static final int ICMP6_RA_ROUTER_LIFETIME_OFFSET = (android.net.apf.ApfFilter.ETH_HEADER_LEN + android.net.apf.ApfFilter.IPV6_HEADER_LEN) + 6;

        private static final int ICMP6_RA_ROUTER_LIFETIME_LEN = 2;

        // Prefix information option.
        private static final int ICMP6_PREFIX_OPTION_TYPE = 3;

        private static final int ICMP6_PREFIX_OPTION_LEN = 32;

        private static final int ICMP6_PREFIX_OPTION_VALID_LIFETIME_OFFSET = 4;

        private static final int ICMP6_PREFIX_OPTION_VALID_LIFETIME_LEN = 4;

        private static final int ICMP6_PREFIX_OPTION_PREFERRED_LIFETIME_OFFSET = 8;

        private static final int ICMP6_PREFIX_OPTION_PREFERRED_LIFETIME_LEN = 4;

        // From RFC6106: Recursive DNS Server option
        private static final int ICMP6_RDNSS_OPTION_TYPE = 25;

        // From RFC6106: DNS Search List option
        private static final int ICMP6_DNSSL_OPTION_TYPE = 31;

        // From RFC4191: Route Information option
        private static final int ICMP6_ROUTE_INFO_OPTION_TYPE = 24;

        // Above three options all have the same format:
        private static final int ICMP6_4_BYTE_LIFETIME_OFFSET = 4;

        private static final int ICMP6_4_BYTE_LIFETIME_LEN = 4;

        // Note: mPacket's position() cannot be assumed to be reset.
        private final java.nio.ByteBuffer mPacket;

        // List of binary ranges that include the whole packet except the lifetimes.
        // Pairs consist of offset and length.
        private final java.util.ArrayList<android.util.Pair<java.lang.Integer, java.lang.Integer>> mNonLifetimes = new java.util.ArrayList<android.util.Pair<java.lang.Integer, java.lang.Integer>>();

        // Minimum lifetime in packet
        long mMinLifetime;

        // When the packet was last captured, in seconds since Unix Epoch
        long mLastSeen;

        // For debugging only. Offsets into the packet where PIOs are.
        private final java.util.ArrayList<java.lang.Integer> mPrefixOptionOffsets = new java.util.ArrayList<>();

        // For debugging only. Offsets into the packet where RDNSS options are.
        private final java.util.ArrayList<java.lang.Integer> mRdnssOptionOffsets = new java.util.ArrayList<>();

        // For debugging only. How many times this RA was seen.
        int seenCount = 0;

        // For debugging only. Returns the hex representation of the last matching packet.
        java.lang.String getLastMatchingPacket() {
            return /* lowercase */
            com.android.internal.util.HexDump.toHexString(mPacket.array(), 0, mPacket.capacity(), false);
        }

        // For debugging only. Returns the string representation of the IPv6 address starting at
        // position pos in the packet.
        private java.lang.String IPv6AddresstoString(int pos) {
            try {
                byte[] array = mPacket.array();
                // Can't just call copyOfRange() and see if it throws, because if it reads past the
                // end it pads with zeros instead of throwing.
                if (((pos < 0) || ((pos + 16) > array.length)) || ((pos + 16) < pos)) {
                    return "???";
                }
                byte[] addressBytes = java.util.Arrays.copyOfRange(array, pos, pos + 16);
                java.net.InetAddress address = ((java.net.Inet6Address) (java.net.InetAddress.getByAddress(addressBytes)));
                return address.getHostAddress();
            } catch (java.lang.UnsupportedOperationException e) {
                // array() failed. Cannot happen, mPacket is array-backed and read-write.
                return "???";
            } catch (java.lang.ClassCastException | java.net.UnknownHostException e) {
                // Cannot happen.
                return "???";
            }
        }

        // Can't be static because it's in a non-static inner class.
        // TODO: Make this static once RA is its own class.
        private void prefixOptionToString(java.lang.StringBuffer sb, int offset) {
            java.lang.String prefix = IPv6AddresstoString(offset + 16);
            int length = android.net.apf.ApfFilter.uint8(mPacket.get(offset + 2));
            long valid = mPacket.getInt(offset + 4);
            long preferred = mPacket.getInt(offset + 8);
            sb.append(java.lang.String.format("%s/%d %ds/%ds ", prefix, length, valid, preferred));
        }

        private void rdnssOptionToString(java.lang.StringBuffer sb, int offset) {
            int optLen = android.net.apf.ApfFilter.uint8(mPacket.get(offset + 1)) * 8;
            if (optLen < 24)
                return;
            // Malformed or empty.

            long lifetime = android.net.apf.ApfFilter.uint32(mPacket.getInt(offset + 4));
            int numServers = (optLen - 8) / 16;
            sb.append("DNS ").append(lifetime).append("s");
            for (int server = 0; server < numServers; server++) {
                sb.append(" ").append(IPv6AddresstoString((offset + 8) + (16 * server)));
            }
        }

        public java.lang.String toString() {
            try {
                java.lang.StringBuffer sb = new java.lang.StringBuffer();
                sb.append(java.lang.String.format("RA %s -> %s %ds ", IPv6AddresstoString(android.net.apf.ApfFilter.IPV6_SRC_ADDR_OFFSET), IPv6AddresstoString(android.net.apf.ApfFilter.IPV6_DEST_ADDR_OFFSET), android.net.apf.ApfFilter.uint16(mPacket.getShort(android.net.apf.ApfFilter.Ra.ICMP6_RA_ROUTER_LIFETIME_OFFSET))));
                for (int i : mPrefixOptionOffsets) {
                    prefixOptionToString(sb, i);
                }
                for (int i : mRdnssOptionOffsets) {
                    rdnssOptionToString(sb, i);
                }
                return sb.toString();
            } catch (java.nio.BufferUnderflowException | java.lang.IndexOutOfBoundsException e) {
                return "<Malformed RA>";
            }
        }

        /**
         * Add a binary range of the packet that does not include a lifetime to mNonLifetimes.
         * Assumes mPacket.position() is as far as we've parsed the packet.
         *
         * @param lastNonLifetimeStart
         * 		offset within packet of where the last binary range of
         * 		data not including a lifetime.
         * @param lifetimeOffset
         * 		offset from mPacket.position() to the next lifetime data.
         * @param lifetimeLength
         * 		length of the next lifetime data.
         * @return offset within packet of where the next binary range of data not including
        a lifetime. This can be passed into the next invocation of this function
        via {@code lastNonLifetimeStart}.
         */
        private int addNonLifetime(int lastNonLifetimeStart, int lifetimeOffset, int lifetimeLength) {
            lifetimeOffset += mPacket.position();
            mNonLifetimes.add(new android.util.Pair<java.lang.Integer, java.lang.Integer>(lastNonLifetimeStart, lifetimeOffset - lastNonLifetimeStart));
            return lifetimeOffset + lifetimeLength;
        }

        private int addNonLifetimeU32(int lastNonLifetimeStart) {
            return addNonLifetime(lastNonLifetimeStart, android.net.apf.ApfFilter.Ra.ICMP6_4_BYTE_LIFETIME_OFFSET, android.net.apf.ApfFilter.Ra.ICMP6_4_BYTE_LIFETIME_LEN);
        }

        // Note that this parses RA and may throw IllegalArgumentException (from
        // Buffer.position(int) or due to an invalid-length option) or IndexOutOfBoundsException
        // (from ByteBuffer.get(int) ) if parsing encounters something non-compliant with
        // specifications.
        Ra(byte[] packet, int length) {
            mPacket = java.nio.ByteBuffer.wrap(java.util.Arrays.copyOf(packet, length));
            mLastSeen = android.net.apf.ApfFilter.curTime();
            // Sanity check packet in case a packet arrives before we attach RA filter
            // to our packet socket. b/29586253
            if (((android.net.apf.ApfFilter.getUint16(mPacket, android.net.apf.ApfFilter.ETH_ETHERTYPE_OFFSET) != android.system.OsConstants.ETH_P_IPV6) || (android.net.apf.ApfFilter.uint8(mPacket.get(android.net.apf.ApfFilter.IPV6_NEXT_HEADER_OFFSET)) != android.system.OsConstants.IPPROTO_ICMPV6)) || (android.net.apf.ApfFilter.uint8(mPacket.get(android.net.apf.ApfFilter.ICMP6_TYPE_OFFSET)) != android.net.apf.ApfFilter.ICMP6_ROUTER_ADVERTISEMENT)) {
                throw new java.lang.IllegalArgumentException("Not an ICMP6 router advertisement");
            }
            android.net.metrics.RaEvent.Builder builder = new android.net.metrics.RaEvent.Builder();
            // Ignore the checksum.
            int lastNonLifetimeStart = addNonLifetime(0, android.net.apf.ApfFilter.Ra.ICMP6_RA_CHECKSUM_OFFSET, android.net.apf.ApfFilter.Ra.ICMP6_RA_CHECKSUM_LEN);
            // Parse router lifetime
            lastNonLifetimeStart = addNonLifetime(lastNonLifetimeStart, android.net.apf.ApfFilter.Ra.ICMP6_RA_ROUTER_LIFETIME_OFFSET, android.net.apf.ApfFilter.Ra.ICMP6_RA_ROUTER_LIFETIME_LEN);
            builder.updateRouterLifetime(android.net.apf.ApfFilter.getUint16(mPacket, android.net.apf.ApfFilter.Ra.ICMP6_RA_ROUTER_LIFETIME_OFFSET));
            // Ensures that the RA is not truncated.
            mPacket.position(android.net.apf.ApfFilter.Ra.ICMP6_RA_OPTION_OFFSET);
            while (mPacket.hasRemaining()) {
                final int position = mPacket.position();
                final int optionType = android.net.apf.ApfFilter.uint8(mPacket.get(position));
                final int optionLength = android.net.apf.ApfFilter.uint8(mPacket.get(position + 1)) * 8;
                long lifetime;
                switch (optionType) {
                    case android.net.apf.ApfFilter.Ra.ICMP6_PREFIX_OPTION_TYPE :
                        // Parse valid lifetime
                        lastNonLifetimeStart = addNonLifetime(lastNonLifetimeStart, android.net.apf.ApfFilter.Ra.ICMP6_PREFIX_OPTION_VALID_LIFETIME_OFFSET, android.net.apf.ApfFilter.Ra.ICMP6_PREFIX_OPTION_VALID_LIFETIME_LEN);
                        lifetime = android.net.apf.ApfFilter.getUint32(mPacket, position + android.net.apf.ApfFilter.Ra.ICMP6_PREFIX_OPTION_VALID_LIFETIME_OFFSET);
                        builder.updatePrefixValidLifetime(lifetime);
                        // Parse preferred lifetime
                        lastNonLifetimeStart = addNonLifetime(lastNonLifetimeStart, android.net.apf.ApfFilter.Ra.ICMP6_PREFIX_OPTION_PREFERRED_LIFETIME_OFFSET, android.net.apf.ApfFilter.Ra.ICMP6_PREFIX_OPTION_PREFERRED_LIFETIME_LEN);
                        lifetime = android.net.apf.ApfFilter.getUint32(mPacket, position + android.net.apf.ApfFilter.Ra.ICMP6_PREFIX_OPTION_PREFERRED_LIFETIME_OFFSET);
                        builder.updatePrefixPreferredLifetime(lifetime);
                        mPrefixOptionOffsets.add(position);
                        break;
                        // These three options have the same lifetime offset and size, and
                        // are processed with the same specialized addNonLifetimeU32:
                    case android.net.apf.ApfFilter.Ra.ICMP6_RDNSS_OPTION_TYPE :
                        mRdnssOptionOffsets.add(position);
                        lastNonLifetimeStart = addNonLifetimeU32(lastNonLifetimeStart);
                        lifetime = android.net.apf.ApfFilter.getUint32(mPacket, position + android.net.apf.ApfFilter.Ra.ICMP6_4_BYTE_LIFETIME_OFFSET);
                        builder.updateRdnssLifetime(lifetime);
                        break;
                    case android.net.apf.ApfFilter.Ra.ICMP6_ROUTE_INFO_OPTION_TYPE :
                        lastNonLifetimeStart = addNonLifetimeU32(lastNonLifetimeStart);
                        lifetime = android.net.apf.ApfFilter.getUint32(mPacket, position + android.net.apf.ApfFilter.Ra.ICMP6_4_BYTE_LIFETIME_OFFSET);
                        builder.updateRouteInfoLifetime(lifetime);
                        break;
                    case android.net.apf.ApfFilter.Ra.ICMP6_DNSSL_OPTION_TYPE :
                        lastNonLifetimeStart = addNonLifetimeU32(lastNonLifetimeStart);
                        lifetime = android.net.apf.ApfFilter.getUint32(mPacket, position + android.net.apf.ApfFilter.Ra.ICMP6_4_BYTE_LIFETIME_OFFSET);
                        builder.updateDnsslLifetime(lifetime);
                        break;
                    default :
                        // RFC4861 section 4.2 dictates we ignore unknown options for fowards
                        // compatibility.
                        break;
                }
                if (optionLength <= 0) {
                    throw new java.lang.IllegalArgumentException(java.lang.String.format("Invalid option length opt=%d len=%d", optionType, optionLength));
                }
                mPacket.position(position + optionLength);
            } 
            // Mark non-lifetime bytes since last lifetime.
            addNonLifetime(lastNonLifetimeStart, 0, 0);
            mMinLifetime = minLifetime(packet, length);
            mMetricsLog.log(builder.build());
        }

        // Ignoring lifetimes (which may change) does {@code packet} match this RA?
        boolean matches(byte[] packet, int length) {
            if (length != mPacket.capacity())
                return false;

            byte[] referencePacket = mPacket.array();
            for (android.util.Pair<java.lang.Integer, java.lang.Integer> nonLifetime : mNonLifetimes) {
                for (int i = nonLifetime.first; i < (nonLifetime.first + nonLifetime.second); i++) {
                    if (packet[i] != referencePacket[i])
                        return false;

                }
            }
            return true;
        }

        // What is the minimum of all lifetimes within {@code packet} in seconds?
        // Precondition: matches(packet, length) already returned true.
        long minLifetime(byte[] packet, int length) {
            long minLifetime = java.lang.Long.MAX_VALUE;
            // Wrap packet in ByteBuffer so we can read big-endian values easily
            java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.wrap(packet);
            for (int i = 0; (i + 1) < mNonLifetimes.size(); i++) {
                int offset = mNonLifetimes.get(i).first + mNonLifetimes.get(i).second;
                // The checksum is in mNonLifetimes, but it's not a lifetime.
                if (offset == android.net.apf.ApfFilter.Ra.ICMP6_RA_CHECKSUM_OFFSET) {
                    continue;
                }
                final int lifetimeLength = mNonLifetimes.get(i + 1).first - offset;
                final long optionLifetime;
                switch (lifetimeLength) {
                    case 2 :
                        optionLifetime = android.net.apf.ApfFilter.uint16(byteBuffer.getShort(offset));
                        break;
                    case 4 :
                        optionLifetime = android.net.apf.ApfFilter.uint32(byteBuffer.getInt(offset));
                        break;
                    default :
                        throw new java.lang.IllegalStateException("bogus lifetime size " + lifetimeLength);
                }
                minLifetime = java.lang.Math.min(minLifetime, optionLifetime);
            }
            return minLifetime;
        }

        // How many seconds does this RA's have to live, taking into account the fact
        // that we might have seen it a while ago.
        long currentLifetime() {
            return mMinLifetime - (android.net.apf.ApfFilter.curTime() - mLastSeen);
        }

        boolean isExpired() {
            // TODO: We may want to handle 0 lifetime RAs differently, if they are common. We'll
            // have to calculte the filter lifetime specially as a fraction of 0 is still 0.
            return currentLifetime() <= 0;
        }

        // Append a filter for this RA to {@code gen}. Jump to DROP_LABEL if it should be dropped.
        // Jump to the next filter if packet doesn't match this RA.
        @com.android.internal.annotations.GuardedBy("ApfFilter.this")
        long generateFilterLocked(android.net.apf.ApfGenerator gen) throws android.net.apf.ApfGenerator.IllegalInstructionException {
            java.lang.String nextFilterLabel = "Ra" + getUniqueNumberLocked();
            // Skip if packet is not the right size
            gen.addLoadFromMemory(android.net.apf.ApfGenerator.Register.R0, gen.PACKET_SIZE_MEMORY_SLOT);
            gen.addJumpIfR0NotEquals(mPacket.capacity(), nextFilterLabel);
            int filterLifetime = ((int) (currentLifetime() / android.net.apf.ApfFilter.FRACTION_OF_LIFETIME_TO_FILTER));
            // Skip filter if expired
            gen.addLoadFromMemory(android.net.apf.ApfGenerator.Register.R0, gen.FILTER_AGE_MEMORY_SLOT);
            gen.addJumpIfR0GreaterThan(filterLifetime, nextFilterLabel);
            for (int i = 0; i < mNonLifetimes.size(); i++) {
                // Generate code to match the packet bytes
                android.util.Pair<java.lang.Integer, java.lang.Integer> nonLifetime = mNonLifetimes.get(i);
                // Don't generate JNEBS instruction for 0 bytes as it always fails the
                // ASSERT_FORWARD_IN_PROGRAM(pc + cmp_imm - 1) check where cmp_imm is
                // the number of bytes to compare. nonLifetime is zero between the
                // valid and preferred lifetimes in the prefix option.
                if (nonLifetime.second != 0) {
                    gen.addLoadImmediate(android.net.apf.ApfGenerator.Register.R0, nonLifetime.first);
                    gen.addJumpIfBytesNotEqual(android.net.apf.ApfGenerator.Register.R0, java.util.Arrays.copyOfRange(mPacket.array(), nonLifetime.first, nonLifetime.first + nonLifetime.second), nextFilterLabel);
                }
                // Generate code to test the lifetimes haven't gone down too far
                if ((i + 1) < mNonLifetimes.size()) {
                    android.util.Pair<java.lang.Integer, java.lang.Integer> nextNonLifetime = mNonLifetimes.get(i + 1);
                    int offset = nonLifetime.first + nonLifetime.second;
                    // Skip the checksum.
                    if (offset == android.net.apf.ApfFilter.Ra.ICMP6_RA_CHECKSUM_OFFSET) {
                        continue;
                    }
                    int length = nextNonLifetime.first - offset;
                    switch (length) {
                        case 4 :
                            gen.addLoad32(android.net.apf.ApfGenerator.Register.R0, offset);
                            break;
                        case 2 :
                            gen.addLoad16(android.net.apf.ApfGenerator.Register.R0, offset);
                            break;
                        default :
                            throw new java.lang.IllegalStateException("bogus lifetime size " + length);
                    }
                    gen.addJumpIfR0LessThan(filterLifetime, nextFilterLabel);
                }
            }
            gen.addJump(gen.DROP_LABEL);
            gen.defineLabel(nextFilterLabel);
            return filterLifetime;
        }
    }

    // Maximum number of RAs to filter for.
    private static final int MAX_RAS = 10;

    @com.android.internal.annotations.GuardedBy("this")
    private java.util.ArrayList<android.net.apf.ApfFilter.Ra> mRas = new java.util.ArrayList<android.net.apf.ApfFilter.Ra>();

    // There is always some marginal benefit to updating the installed APF program when an RA is
    // seen because we can extend the program's lifetime slightly, but there is some cost to
    // updating the program, so don't bother unless the program is going to expire soon. This
    // constant defines "soon" in seconds.
    private static final long MAX_PROGRAM_LIFETIME_WORTH_REFRESHING = 30;

    // We don't want to filter an RA for it's whole lifetime as it'll be expired by the time we ever
    // see a refresh.  Using half the lifetime might be a good idea except for the fact that
    // packets may be dropped, so let's use 6.
    private static final int FRACTION_OF_LIFETIME_TO_FILTER = 6;

    // When did we last install a filter program? In seconds since Unix Epoch.
    @com.android.internal.annotations.GuardedBy("this")
    private long mLastTimeInstalledProgram;

    // How long should the last installed filter program live for? In seconds.
    @com.android.internal.annotations.GuardedBy("this")
    private long mLastInstalledProgramMinLifetime;

    // For debugging only. The last program installed.
    @com.android.internal.annotations.GuardedBy("this")
    private byte[] mLastInstalledProgram;

    // For debugging only. How many times the program was updated since we started.
    @com.android.internal.annotations.GuardedBy("this")
    private int mNumProgramUpdates;

    /**
     * Generate filter code to process ARP packets. Execution of this code ends in either the
     * DROP_LABEL or PASS_LABEL and does not fall off the end.
     * Preconditions:
     *  - Packet being filtered is ARP
     */
    @com.android.internal.annotations.GuardedBy("this")
    private void generateArpFilterLocked(android.net.apf.ApfGenerator gen) throws android.net.apf.ApfGenerator.IllegalInstructionException {
        // Here's a basic summary of what the ARP filter program does:
        // 
        // if not ARP IPv4
        // pass
        // if not ARP IPv4 reply or request
        // pass
        // if unicast ARP reply
        // pass
        // if interface has no IPv4 address
        // if target ip is 0.0.0.0
        // drop
        // else
        // if target ip is not the interface ip
        // drop
        // pass
        final java.lang.String checkTargetIPv4 = "checkTargetIPv4";
        // Pass if not ARP IPv4.
        gen.addLoadImmediate(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.ARP_HEADER_OFFSET);
        gen.addJumpIfBytesNotEqual(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.ARP_IPV4_HEADER, gen.PASS_LABEL);
        // Pass if unknown ARP opcode.
        gen.addLoad16(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.ARP_OPCODE_OFFSET);
        gen.addJumpIfR0Equals(android.net.apf.ApfFilter.ARP_OPCODE_REQUEST, checkTargetIPv4);// Skip to unicast check

        gen.addJumpIfR0NotEquals(android.net.apf.ApfFilter.ARP_OPCODE_REPLY, gen.PASS_LABEL);
        // Pass if unicast reply.
        gen.addLoadImmediate(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.ETH_DEST_ADDR_OFFSET);
        gen.addJumpIfBytesNotEqual(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.ETH_BROADCAST_MAC_ADDRESS, gen.PASS_LABEL);
        // Either a unicast request, a unicast reply, or a broadcast reply.
        gen.defineLabel(checkTargetIPv4);
        if (mIPv4Address == null) {
            // When there is no IPv4 address, drop GARP replies (b/29404209).
            gen.addLoad32(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.ARP_TARGET_IP_ADDRESS_OFFSET);
            gen.addJumpIfR0Equals(android.net.apf.ApfFilter.IPV4_ANY_HOST_ADDRESS, gen.DROP_LABEL);
        } else {
            // When there is an IPv4 address, drop unicast/broadcast requests
            // and broadcast replies with a different target IPv4 address.
            gen.addLoadImmediate(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.ARP_TARGET_IP_ADDRESS_OFFSET);
            gen.addJumpIfBytesNotEqual(android.net.apf.ApfGenerator.Register.R0, mIPv4Address, gen.DROP_LABEL);
        }
        gen.addJump(gen.PASS_LABEL);
    }

    /**
     * Generate filter code to process IPv4 packets. Execution of this code ends in either the
     * DROP_LABEL or PASS_LABEL and does not fall off the end.
     * Preconditions:
     *  - Packet being filtered is IPv4
     */
    @com.android.internal.annotations.GuardedBy("this")
    private void generateIPv4FilterLocked(android.net.apf.ApfGenerator gen) throws android.net.apf.ApfGenerator.IllegalInstructionException {
        // Here's a basic summary of what the IPv4 filter program does:
        // 
        // if filtering multicast (i.e. multicast lock not held):
        // if it's DHCP destined to our MAC:
        // pass
        // if it's L2 broadcast:
        // drop
        // if it's IPv4 multicast:
        // drop
        // if it's IPv4 broadcast:
        // drop
        // pass
        if (mMulticastFilter) {
            final java.lang.String skipDhcpv4Filter = "skip_dhcp_v4_filter";
            // Pass DHCP addressed to us.
            // Check it's UDP.
            gen.addLoad8(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.IPV4_PROTOCOL_OFFSET);
            gen.addJumpIfR0NotEquals(android.system.OsConstants.IPPROTO_UDP, skipDhcpv4Filter);
            // Check it's not a fragment. This matches the BPF filter installed by the DHCP client.
            gen.addLoad16(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.IPV4_FRAGMENT_OFFSET_OFFSET);
            gen.addJumpIfR0AnyBitsSet(android.net.apf.ApfFilter.IPV4_FRAGMENT_OFFSET_MASK, skipDhcpv4Filter);
            // Check it's addressed to DHCP client port.
            gen.addLoadFromMemory(android.net.apf.ApfGenerator.Register.R1, gen.IPV4_HEADER_SIZE_MEMORY_SLOT);
            gen.addLoad16Indexed(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.UDP_DESTINATION_PORT_OFFSET);
            gen.addJumpIfR0NotEquals(android.net.apf.ApfFilter.DHCP_CLIENT_PORT, skipDhcpv4Filter);
            // Check it's DHCP to our MAC address.
            gen.addLoadImmediate(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.DHCP_CLIENT_MAC_OFFSET);
            // NOTE: Relies on R1 containing IPv4 header offset.
            gen.addAddR1();
            gen.addJumpIfBytesNotEqual(android.net.apf.ApfGenerator.Register.R0, mHardwareAddress, skipDhcpv4Filter);
            gen.addJump(gen.PASS_LABEL);
            // Drop all multicasts/broadcasts.
            gen.defineLabel(skipDhcpv4Filter);
            // If IPv4 destination address is in multicast range, drop.
            gen.addLoad8(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.IPV4_DEST_ADDR_OFFSET);
            gen.addAnd(0xf0);
            gen.addJumpIfR0Equals(0xe0, gen.DROP_LABEL);
            // If IPv4 broadcast packet, drop regardless of L2 (b/30231088).
            gen.addLoad32(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.IPV4_DEST_ADDR_OFFSET);
            gen.addJumpIfR0Equals(android.net.apf.ApfFilter.IPV4_BROADCAST_ADDRESS, gen.DROP_LABEL);
            if ((mIPv4Address != null) && (mIPv4PrefixLength < 31)) {
                int broadcastAddr = android.net.apf.ApfFilter.ipv4BroadcastAddress(mIPv4Address, mIPv4PrefixLength);
                gen.addJumpIfR0Equals(broadcastAddr, gen.DROP_LABEL);
            }
            // If L2 broadcast packet, drop.
            gen.addLoadImmediate(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.ETH_DEST_ADDR_OFFSET);
            gen.addJumpIfBytesNotEqual(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.ETH_BROADCAST_MAC_ADDRESS, gen.PASS_LABEL);
            gen.addJump(gen.DROP_LABEL);
        }
        // Otherwise, pass
        gen.addJump(gen.PASS_LABEL);
    }

    /**
     * Generate filter code to process IPv6 packets. Execution of this code ends in either the
     * DROP_LABEL or PASS_LABEL, or falls off the end for ICMPv6 packets.
     * Preconditions:
     *  - Packet being filtered is IPv6
     */
    @com.android.internal.annotations.GuardedBy("this")
    private void generateIPv6FilterLocked(android.net.apf.ApfGenerator gen) throws android.net.apf.ApfGenerator.IllegalInstructionException {
        // Here's a basic summary of what the IPv6 filter program does:
        // 
        // if it's not ICMPv6:
        // if it's multicast and we're dropping multicast:
        // drop
        // pass
        // if it's ICMPv6 NA to ff02::1:
        // drop
        gen.addLoad8(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.IPV6_NEXT_HEADER_OFFSET);
        // Drop multicast if the multicast filter is enabled.
        if (mMulticastFilter) {
            // Don't touch ICMPv6 multicast here, we deal with it in more detail later.
            java.lang.String skipIpv6MulticastFilterLabel = "skipIPv6MulticastFilter";
            gen.addJumpIfR0Equals(android.system.OsConstants.IPPROTO_ICMPV6, skipIpv6MulticastFilterLabel);
            // Drop all other packets sent to ff00::/8.
            gen.addLoad8(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.IPV6_DEST_ADDR_OFFSET);
            gen.addJumpIfR0Equals(0xff, gen.DROP_LABEL);
            // Not multicast and not ICMPv6. Pass.
            gen.addJump(gen.PASS_LABEL);
            gen.defineLabel(skipIpv6MulticastFilterLabel);
        } else {
            // If not ICMPv6, pass.
            gen.addJumpIfR0NotEquals(android.system.OsConstants.IPPROTO_ICMPV6, gen.PASS_LABEL);
        }
        // Add unsolicited multicast neighbor announcements filter
        java.lang.String skipUnsolicitedMulticastNALabel = "skipUnsolicitedMulticastNA";
        // If not neighbor announcements, skip unsolicited multicast NA filter
        gen.addLoad8(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.ICMP6_TYPE_OFFSET);
        gen.addJumpIfR0NotEquals(android.net.apf.ApfFilter.ICMP6_NEIGHBOR_ANNOUNCEMENT, skipUnsolicitedMulticastNALabel);
        // If to ff02::1, drop
        // TODO: Drop only if they don't contain the address of on-link neighbours.
        gen.addLoadImmediate(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.IPV6_DEST_ADDR_OFFSET);
        gen.addJumpIfBytesNotEqual(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.IPV6_ALL_NODES_ADDRESS, skipUnsolicitedMulticastNALabel);
        gen.addJump(gen.DROP_LABEL);
        gen.defineLabel(skipUnsolicitedMulticastNALabel);
    }

    /**
     * Begin generating an APF program to:
     * <ul>
     * <li>Drop ARP requests not for us, if mIPv4Address is set,
     * <li>Drop IPv4 broadcast packets, except DHCP destined to our MAC,
     * <li>Drop IPv4 multicast packets, if mMulticastFilter,
     * <li>Pass all other IPv4 packets,
     * <li>Drop all broadcast non-IP non-ARP packets.
     * <li>Pass all non-ICMPv6 IPv6 packets,
     * <li>Pass all non-IPv4 and non-IPv6 packets,
     * <li>Drop IPv6 ICMPv6 NAs to ff02::1.
     * <li>Let execution continue off the end of the program for IPv6 ICMPv6 packets. This allows
     *     insertion of RA filters here, or if there aren't any, just passes the packets.
     * </ul>
     */
    @com.android.internal.annotations.GuardedBy("this")
    private android.net.apf.ApfGenerator beginProgramLocked() throws android.net.apf.ApfGenerator.IllegalInstructionException {
        android.net.apf.ApfGenerator gen = new android.net.apf.ApfGenerator();
        // This is guaranteed to return true because of the check in maybeCreate.
        gen.setApfVersion(mApfCapabilities.apfVersionSupported);
        // Here's a basic summary of what the initial program does:
        // 
        // if it's ARP:
        // insert ARP filter to drop or pass these appropriately
        // if it's IPv4:
        // insert IPv4 filter to drop or pass these appropriately
        // if it's not IPv6:
        // if it's broadcast:
        // drop
        // pass
        // insert IPv6 filter to drop, pass, or fall off the end for ICMPv6 packets
        // Add ARP filters:
        java.lang.String skipArpFiltersLabel = "skipArpFilters";
        gen.addLoad16(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.ETH_ETHERTYPE_OFFSET);
        gen.addJumpIfR0NotEquals(android.system.OsConstants.ETH_P_ARP, skipArpFiltersLabel);
        generateArpFilterLocked(gen);
        gen.defineLabel(skipArpFiltersLabel);
        // Add IPv4 filters:
        java.lang.String skipIPv4FiltersLabel = "skipIPv4Filters";
        // NOTE: Relies on R0 containing ethertype. This is safe because if we got here, we did not
        // execute the ARP filter, since that filter does not fall through, but either drops or
        // passes.
        gen.addJumpIfR0NotEquals(android.system.OsConstants.ETH_P_IP, skipIPv4FiltersLabel);
        generateIPv4FilterLocked(gen);
        gen.defineLabel(skipIPv4FiltersLabel);
        // Check for IPv6:
        // NOTE: Relies on R0 containing ethertype. This is safe because if we got here, we did not
        // execute the ARP or IPv4 filters, since those filters do not fall through, but either
        // drop or pass.
        java.lang.String ipv6FilterLabel = "IPv6Filters";
        gen.addJumpIfR0Equals(android.system.OsConstants.ETH_P_IPV6, ipv6FilterLabel);
        // Drop non-IP non-ARP broadcasts, pass the rest
        gen.addLoadImmediate(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.ETH_DEST_ADDR_OFFSET);
        gen.addJumpIfBytesNotEqual(android.net.apf.ApfGenerator.Register.R0, android.net.apf.ApfFilter.ETH_BROADCAST_MAC_ADDRESS, gen.PASS_LABEL);
        gen.addJump(gen.DROP_LABEL);
        // Add IPv6 filters:
        gen.defineLabel(ipv6FilterLabel);
        generateIPv6FilterLocked(gen);
        return gen;
    }

    /**
     * Generate and install a new filter program.
     */
    @com.android.internal.annotations.GuardedBy("this")
    @com.android.internal.annotations.VisibleForTesting
    void installNewProgramLocked() {
        purgeExpiredRasLocked();
        java.util.ArrayList<android.net.apf.ApfFilter.Ra> rasToFilter = new java.util.ArrayList<>();
        final byte[] program;
        long programMinLifetime = java.lang.Long.MAX_VALUE;
        try {
            // Step 1: Determine how many RA filters we can fit in the program.
            android.net.apf.ApfGenerator gen = beginProgramLocked();
            for (android.net.apf.ApfFilter.Ra ra : mRas) {
                ra.generateFilterLocked(gen);
                // Stop if we get too big.
                if (gen.programLengthOverEstimate() > mApfCapabilities.maximumApfProgramSize)
                    break;

                rasToFilter.add(ra);
            }
            // Step 2: Actually generate the program
            gen = beginProgramLocked();
            for (android.net.apf.ApfFilter.Ra ra : rasToFilter) {
                programMinLifetime = java.lang.Math.min(programMinLifetime, ra.generateFilterLocked(gen));
            }
            // Execution will reach the end of the program if no filters match, which will pass the
            // packet to the AP.
            program = gen.generate();
        } catch (android.net.apf.ApfGenerator.IllegalInstructionException e) {
            android.util.Log.e(android.net.apf.ApfFilter.TAG, "Program failed to generate: ", e);
            return;
        }
        mLastTimeInstalledProgram = android.net.apf.ApfFilter.curTime();
        mLastInstalledProgramMinLifetime = programMinLifetime;
        mLastInstalledProgram = program;
        mNumProgramUpdates++;
        if (android.net.apf.ApfFilter.VDBG) {
            hexDump("Installing filter: ", program, program.length);
        }
        mIpManagerCallback.installPacketFilter(program);
        int flags = android.net.metrics.ApfProgramEvent.flagsFor(mIPv4Address != null, mMulticastFilter);
        mMetricsLog.log(new android.net.metrics.ApfProgramEvent(programMinLifetime, rasToFilter.size(), mRas.size(), program.length, flags));
    }

    /**
     * Returns {@code true} if a new program should be installed because the current one dies soon.
     */
    private boolean shouldInstallnewProgram() {
        long expiry = mLastTimeInstalledProgram + mLastInstalledProgramMinLifetime;
        return expiry < (android.net.apf.ApfFilter.curTime() + android.net.apf.ApfFilter.MAX_PROGRAM_LIFETIME_WORTH_REFRESHING);
    }

    private void hexDump(java.lang.String msg, byte[] packet, int length) {
        log(msg + /* lowercase */
        com.android.internal.util.HexDump.toHexString(packet, 0, length, false));
    }

    @com.android.internal.annotations.GuardedBy("this")
    private void purgeExpiredRasLocked() {
        for (int i = 0; i < mRas.size();) {
            if (mRas.get(i).isExpired()) {
                log("Expiring " + mRas.get(i));
                mRas.remove(i);
            } else {
                i++;
            }
        }
    }

    /**
     * Process an RA packet, updating the list of known RAs and installing a new APF program
     * if the current APF program should be updated.
     *
     * @return a ProcessRaResult enum describing what action was performed.
     */
    private synchronized android.net.apf.ApfFilter.ProcessRaResult processRa(byte[] packet, int length) {
        if (android.net.apf.ApfFilter.VDBG)
            hexDump("Read packet = ", packet, length);

        // Have we seen this RA before?
        for (int i = 0; i < mRas.size(); i++) {
            android.net.apf.ApfFilter.Ra ra = mRas.get(i);
            if (ra.matches(packet, length)) {
                if (android.net.apf.ApfFilter.VDBG)
                    log("matched RA " + ra);

                // Update lifetimes.
                ra.mLastSeen = android.net.apf.ApfFilter.curTime();
                ra.mMinLifetime = ra.minLifetime(packet, length);
                ra.seenCount++;
                // Keep mRas in LRU order so as to prioritize generating filters for recently seen
                // RAs. LRU prioritizes this because RA filters are generated in order from mRas
                // until the filter program exceeds the maximum filter program size allowed by the
                // chipset, so RAs appearing earlier in mRas are more likely to make it into the
                // filter program.
                // TODO: consider sorting the RAs in order of increasing expiry time as well.
                // Swap to front of array.
                mRas.add(0, mRas.remove(i));
                // If the current program doesn't expire for a while, don't update.
                if (shouldInstallnewProgram()) {
                    installNewProgramLocked();
                    return android.net.apf.ApfFilter.ProcessRaResult.UPDATE_EXPIRY;
                }
                return android.net.apf.ApfFilter.ProcessRaResult.MATCH;
            }
        }
        purgeExpiredRasLocked();
        // TODO: figure out how to proceed when we've received more then MAX_RAS RAs.
        if (mRas.size() >= android.net.apf.ApfFilter.MAX_RAS) {
            return android.net.apf.ApfFilter.ProcessRaResult.DROPPED;
        }
        final android.net.apf.ApfFilter.Ra ra;
        try {
            ra = new android.net.apf.ApfFilter.Ra(packet, length);
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.net.apf.ApfFilter.TAG, "Error parsing RA: " + e);
            return android.net.apf.ApfFilter.ProcessRaResult.PARSE_ERROR;
        }
        // Ignore 0 lifetime RAs.
        if (ra.isExpired()) {
            return android.net.apf.ApfFilter.ProcessRaResult.ZERO_LIFETIME;
        }
        log("Adding " + ra);
        mRas.add(ra);
        installNewProgramLocked();
        return android.net.apf.ApfFilter.ProcessRaResult.UPDATE_NEW_RA;
    }

    /**
     * Create an {@link ApfFilter} if {@code apfCapabilities} indicates support for packet
     * filtering using APF programs.
     */
    public static android.net.apf.ApfFilter maybeCreate(android.net.apf.ApfCapabilities apfCapabilities, java.net.NetworkInterface networkInterface, android.net.ip.IpManager.Callback ipManagerCallback, boolean multicastFilter) {
        if ((apfCapabilities == null) || (networkInterface == null))
            return null;

        if (apfCapabilities.apfVersionSupported == 0)
            return null;

        if (apfCapabilities.maximumApfProgramSize < 512) {
            android.util.Log.e(android.net.apf.ApfFilter.TAG, "Unacceptably small APF limit: " + apfCapabilities.maximumApfProgramSize);
            return null;
        }
        // For now only support generating programs for Ethernet frames. If this restriction is
        // lifted:
        // 1. the program generator will need its offsets adjusted.
        // 2. the packet filter attached to our packet socket will need its offset adjusted.
        if (apfCapabilities.apfPacketFormat != android.system.OsConstants.ARPHRD_ETHER)
            return null;

        if (!new android.net.apf.ApfGenerator().setApfVersion(apfCapabilities.apfVersionSupported)) {
            android.util.Log.e(android.net.apf.ApfFilter.TAG, "Unsupported APF version: " + apfCapabilities.apfVersionSupported);
            return null;
        }
        return new android.net.apf.ApfFilter(apfCapabilities, networkInterface, ipManagerCallback, multicastFilter, new android.net.metrics.IpConnectivityLog());
    }

    public synchronized void shutdown() {
        if (mReceiveThread != null) {
            log("shutting down");
            mReceiveThread.halt();// Also closes socket.

            mReceiveThread = null;
        }
        mRas.clear();
    }

    public synchronized void setMulticastFilter(boolean enabled) {
        if (mMulticastFilter != enabled) {
            mMulticastFilter = enabled;
            installNewProgramLocked();
        }
    }

    /**
     * Find the single IPv4 LinkAddress if there is one, otherwise return null.
     */
    private static android.net.LinkAddress findIPv4LinkAddress(android.net.LinkProperties lp) {
        android.net.LinkAddress ipv4Address = null;
        for (android.net.LinkAddress address : lp.getLinkAddresses()) {
            if (!(address.getAddress() instanceof java.net.Inet4Address)) {
                continue;
            }
            if ((ipv4Address != null) && (!ipv4Address.isSameAddressAs(address))) {
                // More than one IPv4 address, abort.
                return null;
            }
            ipv4Address = address;
        }
        return ipv4Address;
    }

    public synchronized void setLinkProperties(android.net.LinkProperties lp) {
        // NOTE: Do not keep a copy of LinkProperties as it would further duplicate state.
        final android.net.LinkAddress ipv4Address = android.net.apf.ApfFilter.findIPv4LinkAddress(lp);
        final byte[] addr = (ipv4Address != null) ? ipv4Address.getAddress().getAddress() : null;
        final int prefix = (ipv4Address != null) ? ipv4Address.getPrefixLength() : 0;
        if ((prefix == mIPv4PrefixLength) && java.util.Arrays.equals(addr, mIPv4Address)) {
            return;
        }
        mIPv4Address = addr;
        mIPv4PrefixLength = prefix;
        installNewProgramLocked();
    }

    public synchronized void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println("Capabilities: " + mApfCapabilities);
        pw.println("Receive thread: " + (mReceiveThread != null ? "RUNNING" : "STOPPED"));
        pw.println("Multicast: " + (mMulticastFilter ? "DROP" : "ALLOW"));
        try {
            pw.println("IPv4 address: " + java.net.InetAddress.getByAddress(mIPv4Address).getHostAddress());
        } catch (java.net.UnknownHostException | java.lang.NullPointerException e) {
        }
        if (mLastTimeInstalledProgram == 0) {
            pw.println("No program installed.");
            return;
        }
        pw.println("Program updates: " + mNumProgramUpdates);
        pw.println(java.lang.String.format("Last program length %d, installed %ds ago, lifetime %ds", mLastInstalledProgram.length, android.net.apf.ApfFilter.curTime() - mLastTimeInstalledProgram, mLastInstalledProgramMinLifetime));
        pw.println("RA filters:");
        pw.increaseIndent();
        for (android.net.apf.ApfFilter.Ra ra : mRas) {
            pw.println(ra);
            pw.increaseIndent();
            pw.println(java.lang.String.format("Seen: %d, last %ds ago", ra.seenCount, android.net.apf.ApfFilter.curTime() - ra.mLastSeen));
            if (android.net.apf.ApfFilter.DBG) {
                pw.println("Last match:");
                pw.increaseIndent();
                pw.println(ra.getLastMatchingPacket());
                pw.decreaseIndent();
            }
            pw.decreaseIndent();
        }
        pw.decreaseIndent();
        if (android.net.apf.ApfFilter.DBG) {
            pw.println("Last program:");
            pw.increaseIndent();
            pw.println(/* lowercase */
            com.android.internal.util.HexDump.toHexString(mLastInstalledProgram, false));
            pw.decreaseIndent();
        }
    }

    private static int uint8(byte b) {
        return b & 0xff;
    }

    private static int uint16(short s) {
        return s & 0xffff;
    }

    private static long uint32(int i) {
        return i & 0xffffffffL;
    }

    private static long getUint16(java.nio.ByteBuffer buffer, int position) {
        return android.net.apf.ApfFilter.uint16(buffer.getShort(position));
    }

    private static long getUint32(java.nio.ByteBuffer buffer, int position) {
        return android.net.apf.ApfFilter.uint32(buffer.getInt(position));
    }

    // TODO: move to android.net.NetworkUtils
    @com.android.internal.annotations.VisibleForTesting
    public static int ipv4BroadcastAddress(byte[] addrBytes, int prefixLength) {
        return android.net.apf.ApfFilter.bytesToInt(addrBytes) | ((int) (android.net.apf.ApfFilter.uint32(-1) >>> prefixLength));
    }

    @com.android.internal.annotations.VisibleForTesting
    public static int bytesToInt(byte[] addrBytes) {
        return (((android.net.apf.ApfFilter.uint8(addrBytes[0]) << 24) + (android.net.apf.ApfFilter.uint8(addrBytes[1]) << 16)) + (android.net.apf.ApfFilter.uint8(addrBytes[2]) << 8)) + android.net.apf.ApfFilter.uint8(addrBytes[3]);
    }
}

