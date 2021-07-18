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
package android.net.ip;


/**
 * Basic IPv6 Router Advertisement Daemon.
 *
 * TODO:
 *
 *     - Rewrite using Handler (and friends) so that AlarmManager can deliver
 *       "kick" messages when it's time to send a multicast RA.
 *
 * @unknown 
 */
public class RouterAdvertisementDaemon {
    private static final java.lang.String TAG = android.net.ip.RouterAdvertisementDaemon.class.getSimpleName();

    private static final byte ICMPV6_ND_ROUTER_SOLICIT = android.net.ip.RouterAdvertisementDaemon.asByte(133);

    private static final byte ICMPV6_ND_ROUTER_ADVERT = android.net.ip.RouterAdvertisementDaemon.asByte(134);

    private static final int IPV6_MIN_MTU = 1280;

    private static final int MIN_RA_HEADER_SIZE = 16;

    // Summary of various timers and lifetimes.
    private static final int MIN_RTR_ADV_INTERVAL_SEC = 300;

    private static final int MAX_RTR_ADV_INTERVAL_SEC = 600;

    // In general, router, prefix, and DNS lifetimes are all advised to be
    // greater than or equal to 3 * MAX_RTR_ADV_INTERVAL.  Here, we double
    // that to allow for multicast packet loss.
    // 
    // This MAX_RTR_ADV_INTERVAL_SEC and DEFAULT_LIFETIME are also consistent
    // with the https://tools.ietf.org/html/rfc7772#section-4 discussion of
    // "approximately 7 RAs per hour".
    private static final int DEFAULT_LIFETIME = 6 * android.net.ip.RouterAdvertisementDaemon.MAX_RTR_ADV_INTERVAL_SEC;

    // From https://tools.ietf.org/html/rfc4861#section-10 .
    private static final int MIN_DELAY_BETWEEN_RAS_SEC = 3;

    // Both initial and final RAs, but also for changes in RA contents.
    // From https://tools.ietf.org/html/rfc4861#section-10 .
    private static final int MAX_URGENT_RTR_ADVERTISEMENTS = 5;

    private static final int DAY_IN_SECONDS = 86400;

    private static final byte[] ALL_NODES = new byte[]{ ((byte) (0xff)), 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 };

    private final java.lang.String mIfName;

    private final int mIfIndex;

    private final byte[] mHwAddr;

    private final java.net.InetSocketAddress mAllNodes;

    // This lock is to protect the RA from being updated while being
    // transmitted on another thread  (multicast or unicast).
    // 
    // TODO: This should be handled with a more RCU-like approach.
    private final java.lang.Object mLock = new java.lang.Object();

    @com.android.internal.annotations.GuardedBy("mLock")
    private final byte[] mRA = new byte[android.net.ip.RouterAdvertisementDaemon.IPV6_MIN_MTU];

    @com.android.internal.annotations.GuardedBy("mLock")
    private int mRaLength;

    @com.android.internal.annotations.GuardedBy("mLock")
    private final android.net.ip.RouterAdvertisementDaemon.DeprecatedInfoTracker mDeprecatedInfoTracker;

    @com.android.internal.annotations.GuardedBy("mLock")
    private android.net.ip.RouterAdvertisementDaemon.RaParams mRaParams;

    private volatile java.io.FileDescriptor mSocket;

    private volatile android.net.ip.RouterAdvertisementDaemon.MulticastTransmitter mMulticastTransmitter;

    private volatile android.net.ip.RouterAdvertisementDaemon.UnicastResponder mUnicastResponder;

    public static class RaParams {
        public boolean hasDefaultRoute;

        public int mtu;

        public java.util.HashSet<android.net.IpPrefix> prefixes;

        public java.util.HashSet<java.net.Inet6Address> dnses;

        public RaParams() {
            hasDefaultRoute = false;
            mtu = android.net.ip.RouterAdvertisementDaemon.IPV6_MIN_MTU;
            prefixes = new java.util.HashSet<android.net.IpPrefix>();
            dnses = new java.util.HashSet<java.net.Inet6Address>();
        }

        public RaParams(android.net.ip.RouterAdvertisementDaemon.RaParams other) {
            hasDefaultRoute = other.hasDefaultRoute;
            mtu = other.mtu;
            prefixes = ((java.util.HashSet) (other.prefixes.clone()));
            dnses = ((java.util.HashSet) (other.dnses.clone()));
        }

        // Returns the subset of RA parameters that become deprecated when
        // moving from announcing oldRa to announcing newRa.
        // 
        // Currently only tracks differences in |prefixes| and |dnses|.
        public static android.net.ip.RouterAdvertisementDaemon.RaParams getDeprecatedRaParams(android.net.ip.RouterAdvertisementDaemon.RaParams oldRa, android.net.ip.RouterAdvertisementDaemon.RaParams newRa) {
            android.net.ip.RouterAdvertisementDaemon.RaParams newlyDeprecated = new android.net.ip.RouterAdvertisementDaemon.RaParams();
            if (oldRa != null) {
                for (android.net.IpPrefix ipp : oldRa.prefixes) {
                    if ((newRa == null) || (!newRa.prefixes.contains(ipp))) {
                        newlyDeprecated.prefixes.add(ipp);
                    }
                }
                for (java.net.Inet6Address dns : oldRa.dnses) {
                    if ((newRa == null) || (!newRa.dnses.contains(dns))) {
                        newlyDeprecated.dnses.add(dns);
                    }
                }
            }
            return newlyDeprecated;
        }
    }

    private static class DeprecatedInfoTracker {
        private final java.util.HashMap<android.net.IpPrefix, java.lang.Integer> mPrefixes = new java.util.HashMap<>();

        private final java.util.HashMap<java.net.Inet6Address, java.lang.Integer> mDnses = new java.util.HashMap<>();

        java.util.Set<android.net.IpPrefix> getPrefixes() {
            return mPrefixes.keySet();
        }

        void putPrefixes(java.util.Set<android.net.IpPrefix> prefixes) {
            for (android.net.IpPrefix ipp : prefixes) {
                mPrefixes.put(ipp, android.net.ip.RouterAdvertisementDaemon.MAX_URGENT_RTR_ADVERTISEMENTS);
            }
        }

        void removePrefixes(java.util.Set<android.net.IpPrefix> prefixes) {
            for (android.net.IpPrefix ipp : prefixes) {
                mPrefixes.remove(ipp);
            }
        }

        java.util.Set<java.net.Inet6Address> getDnses() {
            return mDnses.keySet();
        }

        void putDnses(java.util.Set<java.net.Inet6Address> dnses) {
            for (java.net.Inet6Address dns : dnses) {
                mDnses.put(dns, android.net.ip.RouterAdvertisementDaemon.MAX_URGENT_RTR_ADVERTISEMENTS);
            }
        }

        void removeDnses(java.util.Set<java.net.Inet6Address> dnses) {
            for (java.net.Inet6Address dns : dnses) {
                mDnses.remove(dns);
            }
        }

        boolean isEmpty() {
            return mPrefixes.isEmpty() && mDnses.isEmpty();
        }

        private boolean decrementCounters() {
            boolean removed = decrementCounter(mPrefixes);
            removed |= decrementCounter(mDnses);
            return removed;
        }

        private <T> boolean decrementCounter(java.util.HashMap<T, java.lang.Integer> map) {
            boolean removed = false;
            for (java.util.Iterator<java.util.Map.Entry<T, java.lang.Integer>> it = map.entrySet().iterator(); it.hasNext();) {
                java.util.Map.Entry<T, java.lang.Integer> kv = it.next();
                if (kv.getValue() == 0) {
                    it.remove();
                    removed = true;
                } else {
                    kv.setValue(kv.getValue() - 1);
                }
            }
            return removed;
        }
    }

    public RouterAdvertisementDaemon(java.lang.String ifname, int ifindex, byte[] hwaddr) {
        mIfName = ifname;
        mIfIndex = ifindex;
        mHwAddr = hwaddr;
        mAllNodes = new java.net.InetSocketAddress(android.net.ip.RouterAdvertisementDaemon.getAllNodesForScopeId(mIfIndex), 0);
        mDeprecatedInfoTracker = new android.net.ip.RouterAdvertisementDaemon.DeprecatedInfoTracker();
    }

    public void buildNewRa(android.net.ip.RouterAdvertisementDaemon.RaParams deprecatedParams, android.net.ip.RouterAdvertisementDaemon.RaParams newParams) {
        synchronized(mLock) {
            if (deprecatedParams != null) {
                mDeprecatedInfoTracker.putPrefixes(deprecatedParams.prefixes);
                mDeprecatedInfoTracker.putDnses(deprecatedParams.dnses);
            }
            if (newParams != null) {
                // Process information that is no longer deprecated.
                mDeprecatedInfoTracker.removePrefixes(newParams.prefixes);
                mDeprecatedInfoTracker.removeDnses(newParams.dnses);
            }
            mRaParams = newParams;
            assembleRaLocked();
        }
        maybeNotifyMulticastTransmitter();
    }

    public boolean start() {
        if (!createSocket()) {
            return false;
        }
        mMulticastTransmitter = new android.net.ip.RouterAdvertisementDaemon.MulticastTransmitter();
        mMulticastTransmitter.start();
        mUnicastResponder = new android.net.ip.RouterAdvertisementDaemon.UnicastResponder();
        mUnicastResponder.start();
        return true;
    }

    public void stop() {
        closeSocket();
        mMulticastTransmitter = null;
        mUnicastResponder = null;
    }

    private void assembleRaLocked() {
        final java.nio.ByteBuffer ra = java.nio.ByteBuffer.wrap(mRA);
        ra.order(java.nio.ByteOrder.BIG_ENDIAN);
        boolean shouldSendRA = false;
        try {
            android.net.ip.RouterAdvertisementDaemon.putHeader(ra, (mRaParams != null) && mRaParams.hasDefaultRoute);
            android.net.ip.RouterAdvertisementDaemon.putSlla(ra, mHwAddr);
            mRaLength = ra.position();
            // https://tools.ietf.org/html/rfc5175#section-4 says:
            // 
            // "MUST NOT be added to a Router Advertisement message
            // if no flags in the option are set."
            // 
            // putExpandedFlagsOption(ra);
            if (mRaParams != null) {
                android.net.ip.RouterAdvertisementDaemon.putMtu(ra, mRaParams.mtu);
                mRaLength = ra.position();
                for (android.net.IpPrefix ipp : mRaParams.prefixes) {
                    android.net.ip.RouterAdvertisementDaemon.putPio(ra, ipp, android.net.ip.RouterAdvertisementDaemon.DEFAULT_LIFETIME, android.net.ip.RouterAdvertisementDaemon.DEFAULT_LIFETIME);
                    mRaLength = ra.position();
                    shouldSendRA = true;
                }
                if (mRaParams.dnses.size() > 0) {
                    android.net.ip.RouterAdvertisementDaemon.putRdnss(ra, mRaParams.dnses, android.net.ip.RouterAdvertisementDaemon.DEFAULT_LIFETIME);
                    mRaLength = ra.position();
                    shouldSendRA = true;
                }
            }
            for (android.net.IpPrefix ipp : mDeprecatedInfoTracker.getPrefixes()) {
                android.net.ip.RouterAdvertisementDaemon.putPio(ra, ipp, 0, 0);
                mRaLength = ra.position();
                shouldSendRA = true;
            }
            final java.util.Set<java.net.Inet6Address> deprecatedDnses = mDeprecatedInfoTracker.getDnses();
            if (!deprecatedDnses.isEmpty()) {
                android.net.ip.RouterAdvertisementDaemon.putRdnss(ra, deprecatedDnses, 0);
                mRaLength = ra.position();
                shouldSendRA = true;
            }
        } catch (java.nio.BufferOverflowException e) {
            // The packet up to mRaLength  is valid, since it has been updated
            // progressively as the RA was built. Log an error, and continue
            // on as best as possible.
            android.util.Log.e(android.net.ip.RouterAdvertisementDaemon.TAG, "Could not construct new RA: " + e);
        }
        // We have nothing worth announcing; indicate as much to maybeSendRA().
        if (!shouldSendRA) {
            mRaLength = 0;
        }
    }

    private void maybeNotifyMulticastTransmitter() {
        final android.net.ip.RouterAdvertisementDaemon.MulticastTransmitter m = mMulticastTransmitter;
        if (m != null) {
            m.hup();
        }
    }

    private static java.net.Inet6Address getAllNodesForScopeId(int scopeId) {
        try {
            return java.net.Inet6Address.getByAddress("ff02::1", android.net.ip.RouterAdvertisementDaemon.ALL_NODES, scopeId);
        } catch (java.net.UnknownHostException uhe) {
            android.util.Log.wtf(android.net.ip.RouterAdvertisementDaemon.TAG, "Failed to construct ff02::1 InetAddress: " + uhe);
            return null;
        }
    }

    private static byte asByte(int value) {
        return ((byte) (value));
    }

    private static short asShort(int value) {
        return ((short) (value));
    }

    private static void putHeader(java.nio.ByteBuffer ra, boolean hasDefaultRoute) {
        /**
         * Router Advertisement Message Format
         *
         * 0                   1                   2                   3
         * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |     Type      |     Code      |          Checksum             |
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * | Cur Hop Limit |M|O|H|Prf|P|R|R|       Router Lifetime         |
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |                         Reachable Time                        |
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |                          Retrans Timer                        |
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |   Options ...
         * +-+-+-+-+-+-+-+-+-+-+-+-
         */
        final byte DEFAULT_HOPLIMIT = 64;
        // RFC 4191 "high" preference, iff. advertising a default route.
        ra.put(android.net.ip.RouterAdvertisementDaemon.ICMPV6_ND_ROUTER_ADVERT).put(android.net.ip.RouterAdvertisementDaemon.asByte(0)).putShort(android.net.ip.RouterAdvertisementDaemon.asShort(0)).put(DEFAULT_HOPLIMIT).put(hasDefaultRoute ? android.net.ip.RouterAdvertisementDaemon.asByte(0x8) : android.net.ip.RouterAdvertisementDaemon.asByte(0)).putShort(hasDefaultRoute ? android.net.ip.RouterAdvertisementDaemon.asShort(android.net.ip.RouterAdvertisementDaemon.DEFAULT_LIFETIME) : android.net.ip.RouterAdvertisementDaemon.asShort(0)).putInt(0).putInt(0);
    }

    private static void putSlla(java.nio.ByteBuffer ra, byte[] slla) {
        /**
         * Source/Target Link-layer Address
         *
         * 0                   1                   2                   3
         * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |     Type      |    Length     |    Link-Layer Address ...
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         */
        if ((slla == null) || (slla.length != 6)) {
            // Only IEEE 802.3 6-byte addresses are supported.
            return;
        }
        final byte ND_OPTION_SLLA = 1;
        final byte SLLA_NUM_8OCTETS = 1;
        ra.put(ND_OPTION_SLLA).put(SLLA_NUM_8OCTETS).put(slla);
    }

    private static void putExpandedFlagsOption(java.nio.ByteBuffer ra) {
        /**
         * Router Advertisement Expanded Flags Option
         *
         * 0                   1                   2                   3
         * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |     Type      |    Length     |         Bit fields available ..
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * ... for assignment                                              |
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         */
        final byte ND_OPTION_EFO = 26;
        final byte EFO_NUM_8OCTETS = 1;
        ra.put(ND_OPTION_EFO).put(EFO_NUM_8OCTETS).putShort(android.net.ip.RouterAdvertisementDaemon.asShort(0)).putInt(0);
    }

    private static void putMtu(java.nio.ByteBuffer ra, int mtu) {
        /**
         * MTU
         *
         * 0                   1                   2                   3
         * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |     Type      |    Length     |           Reserved            |
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |                              MTU                              |
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         */
        final byte ND_OPTION_MTU = 5;
        final byte MTU_NUM_8OCTETS = 1;
        ra.put(ND_OPTION_MTU).put(MTU_NUM_8OCTETS).putShort(android.net.ip.RouterAdvertisementDaemon.asShort(0)).putInt(mtu < android.net.ip.RouterAdvertisementDaemon.IPV6_MIN_MTU ? android.net.ip.RouterAdvertisementDaemon.IPV6_MIN_MTU : mtu);
    }

    private static void putPio(java.nio.ByteBuffer ra, android.net.IpPrefix ipp, int validTime, int preferredTime) {
        /**
         * Prefix Information
         *
         * 0                   1                   2                   3
         * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |     Type      |    Length     | Prefix Length |L|A| Reserved1 |
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |                         Valid Lifetime                        |
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |                       Preferred Lifetime                      |
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |                           Reserved2                           |
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |                                                               |
         * +                                                               +
         * |                                                               |
         * +                            Prefix                             +
         * |                                                               |
         * +                                                               +
         * |                                                               |
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         */
        final int prefixLength = ipp.getPrefixLength();
        if (prefixLength != 64) {
            return;
        }
        final byte ND_OPTION_PIO = 3;
        final byte PIO_NUM_8OCTETS = 4;
        if (validTime < 0)
            validTime = 0;

        if (preferredTime < 0)
            preferredTime = 0;

        if (preferredTime > validTime)
            preferredTime = validTime;

        final byte[] addr = ipp.getAddress().getAddress();
        /* L & A set */
        ra.put(ND_OPTION_PIO).put(PIO_NUM_8OCTETS).put(android.net.ip.RouterAdvertisementDaemon.asByte(prefixLength)).put(android.net.ip.RouterAdvertisementDaemon.asByte(0xc0)).putInt(validTime).putInt(preferredTime).putInt(0).put(addr);
    }

    private static void putRio(java.nio.ByteBuffer ra, android.net.IpPrefix ipp) {
        /**
         * Route Information Option
         *
         * 0                   1                   2                   3
         * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |     Type      |    Length     | Prefix Length |Resvd|Prf|Resvd|
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |                        Route Lifetime                         |
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |                   Prefix (Variable Length)                    |
         * .                                                               .
         * .                                                               .
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         */
        final int prefixLength = ipp.getPrefixLength();
        if (prefixLength > 64) {
            return;
        }
        final byte ND_OPTION_RIO = 24;
        final byte RIO_NUM_8OCTETS = android.net.ip.RouterAdvertisementDaemon.asByte(prefixLength == 0 ? 1 : prefixLength <= 8 ? 2 : 3);
        final byte[] addr = ipp.getAddress().getAddress();
        ra.put(ND_OPTION_RIO).put(RIO_NUM_8OCTETS).put(android.net.ip.RouterAdvertisementDaemon.asByte(prefixLength)).put(android.net.ip.RouterAdvertisementDaemon.asByte(0x18)).putInt(android.net.ip.RouterAdvertisementDaemon.DEFAULT_LIFETIME);
        // Rely upon an IpPrefix's address being properly zeroed.
        if (prefixLength > 0) {
            ra.put(addr, 0, prefixLength <= 64 ? 8 : 16);
        }
    }

    private static void putRdnss(java.nio.ByteBuffer ra, java.util.Set<java.net.Inet6Address> dnses, int lifetime) {
        /**
         * Recursive DNS Server (RDNSS) Option
         *
         * 0                   1                   2                   3
         * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |     Type      |     Length    |           Reserved            |
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |                           Lifetime                            |
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * |                                                               |
         * :            Addresses of IPv6 Recursive DNS Servers            :
         * |                                                               |
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         */
        final byte ND_OPTION_RDNSS = 25;
        final byte RDNSS_NUM_8OCTETS = android.net.ip.RouterAdvertisementDaemon.asByte((dnses.size() * 2) + 1);
        ra.put(ND_OPTION_RDNSS).put(RDNSS_NUM_8OCTETS).putShort(android.net.ip.RouterAdvertisementDaemon.asShort(0)).putInt(lifetime);
        for (java.net.Inet6Address dns : dnses) {
            // NOTE: If the full of list DNS servers doesn't fit in the packet,
            // this code will cause a buffer overflow and the RA won't include
            // this instance of the option at all.
            // 
            // TODO: Consider looking at ra.remaining() to determine how many
            // DNS servers will fit, and adding only those.
            ra.put(dns.getAddress());
        }
    }

    private boolean createSocket() {
        final int SEND_TIMEOUT_MS = 300;
        try {
            mSocket = android.system.Os.socket(android.system.OsConstants.AF_INET6, android.system.OsConstants.SOCK_RAW, android.system.OsConstants.IPPROTO_ICMPV6);
            // Setting SNDTIMEO is purely for defensive purposes.
            android.system.Os.setsockoptTimeval(mSocket, android.system.OsConstants.SOL_SOCKET, android.system.OsConstants.SO_SNDTIMEO, android.system.StructTimeval.fromMillis(SEND_TIMEOUT_MS));
            android.system.Os.setsockoptIfreq(mSocket, android.system.OsConstants.SOL_SOCKET, android.system.OsConstants.SO_BINDTODEVICE, mIfName);
            android.net.NetworkUtils.protectFromVpn(mSocket);
            android.net.NetworkUtils.setupRaSocket(mSocket, mIfIndex);
        } catch (android.system.ErrnoException | java.io.IOException e) {
            android.util.Log.e(android.net.ip.RouterAdvertisementDaemon.TAG, "Failed to create RA daemon socket: " + e);
            return false;
        }
        return true;
    }

    private void closeSocket() {
        if (mSocket != null) {
            try {
                libcore.io.IoBridge.closeAndSignalBlockedThreads(mSocket);
            } catch (java.io.IOException ignored) {
            }
        }
        mSocket = null;
    }

    private boolean isSocketValid() {
        final java.io.FileDescriptor s = mSocket;
        return (s != null) && s.valid();
    }

    private boolean isSuitableDestination(java.net.InetSocketAddress dest) {
        if (mAllNodes.equals(dest)) {
            return true;
        }
        final java.net.InetAddress destip = dest.getAddress();
        return ((destip instanceof java.net.Inet6Address) && destip.isLinkLocalAddress()) && (((java.net.Inet6Address) (destip)).getScopeId() == mIfIndex);
    }

    private void maybeSendRA(java.net.InetSocketAddress dest) {
        if ((dest == null) || (!isSuitableDestination(dest))) {
            dest = mAllNodes;
        }
        try {
            synchronized(mLock) {
                if (mRaLength < android.net.ip.RouterAdvertisementDaemon.MIN_RA_HEADER_SIZE) {
                    // No actual RA to send.
                    return;
                }
                android.system.Os.sendto(mSocket, mRA, 0, mRaLength, 0, dest);
            }
            android.util.Log.d(android.net.ip.RouterAdvertisementDaemon.TAG, "RA sendto " + dest.getAddress().getHostAddress());
        } catch (android.system.ErrnoException | java.net.SocketException e) {
            if (isSocketValid()) {
                android.util.Log.e(android.net.ip.RouterAdvertisementDaemon.TAG, "sendto error: " + e);
            }
        }
    }

    private final class UnicastResponder extends java.lang.Thread {
        private final java.net.InetSocketAddress solicitor = new java.net.InetSocketAddress();

        // The recycled buffer for receiving Router Solicitations from clients.
        // If the RS is larger than IPV6_MIN_MTU the packets are truncated.
        // This is fine since currently only byte 0 is examined anyway.
        private final byte[] mSolication = new byte[android.net.ip.RouterAdvertisementDaemon.IPV6_MIN_MTU];

        @java.lang.Override
        public void run() {
            while (isSocketValid()) {
                try {
                    // Blocking receive.
                    final int rval = android.system.Os.recvfrom(mSocket, mSolication, 0, mSolication.length, 0, solicitor);
                    // Do the least possible amount of validation.
                    if ((rval < 1) || (mSolication[0] != android.net.ip.RouterAdvertisementDaemon.ICMPV6_ND_ROUTER_SOLICIT)) {
                        continue;
                    }
                } catch (android.system.ErrnoException | java.net.SocketException e) {
                    if (isSocketValid()) {
                        android.util.Log.e(android.net.ip.RouterAdvertisementDaemon.TAG, "recvfrom error: " + e);
                    }
                    continue;
                }
                maybeSendRA(solicitor);
            } 
        }
    }

    // TODO: Consider moving this to run on a provided Looper as a Handler,
    // with WakeupMessage-style messages providing the timer driven input.
    private final class MulticastTransmitter extends java.lang.Thread {
        private final java.util.Random mRandom = new java.util.Random();

        private final java.util.concurrent.atomic.AtomicInteger mUrgentAnnouncements = new java.util.concurrent.atomic.AtomicInteger(0);

        @java.lang.Override
        public void run() {
            while (isSocketValid()) {
                try {
                    java.lang.Thread.sleep(getNextMulticastTransmitDelayMs());
                } catch (java.lang.InterruptedException ignored) {
                    // Stop sleeping, immediately send an RA, and continue.
                }
                maybeSendRA(mAllNodes);
                synchronized(mLock) {
                    if (mDeprecatedInfoTracker.decrementCounters()) {
                        // At least one deprecated PIO has been removed;
                        // reassemble the RA.
                        assembleRaLocked();
                    }
                }
            } 
        }

        public void hup() {
            // Set to one fewer that the desired number, because as soon as
            // the thread interrupt is processed we immediately send an RA
            // and mUrgentAnnouncements is not examined until the subsequent
            // sleep interval computation (i.e. this way we send 3 and not 4).
            mUrgentAnnouncements.set(android.net.ip.RouterAdvertisementDaemon.MAX_URGENT_RTR_ADVERTISEMENTS - 1);
            interrupt();
        }

        private int getNextMulticastTransmitDelaySec() {
            boolean deprecationInProgress = false;
            synchronized(mLock) {
                if (mRaLength < android.net.ip.RouterAdvertisementDaemon.MIN_RA_HEADER_SIZE) {
                    // No actual RA to send; just sleep for 1 day.
                    return android.net.ip.RouterAdvertisementDaemon.DAY_IN_SECONDS;
                }
                deprecationInProgress = !mDeprecatedInfoTracker.isEmpty();
            }
            final int urgentPending = mUrgentAnnouncements.getAndDecrement();
            if ((urgentPending > 0) || deprecationInProgress) {
                return android.net.ip.RouterAdvertisementDaemon.MIN_DELAY_BETWEEN_RAS_SEC;
            }
            return android.net.ip.RouterAdvertisementDaemon.MIN_RTR_ADV_INTERVAL_SEC + mRandom.nextInt(android.net.ip.RouterAdvertisementDaemon.MAX_RTR_ADV_INTERVAL_SEC - android.net.ip.RouterAdvertisementDaemon.MIN_RTR_ADV_INTERVAL_SEC);
        }

        private long getNextMulticastTransmitDelayMs() {
            return 1000 * ((long) (getNextMulticastTransmitDelaySec()));
        }
    }
}

