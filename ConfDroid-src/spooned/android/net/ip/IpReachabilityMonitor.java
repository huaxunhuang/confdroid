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
package android.net.ip;


/**
 * IpReachabilityMonitor.
 *
 * Monitors on-link IP reachability and notifies callers whenever any on-link
 * addresses of interest appear to have become unresponsive.
 *
 * This code does not concern itself with "why" a neighbour might have become
 * unreachable. Instead, it primarily reacts to the kernel's notion of IP
 * reachability for each of the neighbours we know to be critically important
 * to normal network connectivity. As such, it is often "just the messenger":
 * the neighbours about which it warns are already deemed by the kernel to have
 * become unreachable.
 *
 *
 * How it works:
 *
 *   1. The "on-link neighbours of interest" found in a given LinkProperties
 *      instance are added to a "watch list" via #updateLinkProperties().
 *      This usually means all default gateways and any on-link DNS servers.
 *
 *   2. We listen continuously for netlink neighbour messages (RTM_NEWNEIGH,
 *      RTM_DELNEIGH), watching only for neighbours in the watch list.
 *
 *        - A neighbour going into NUD_REACHABLE, NUD_STALE, NUD_DELAY, and
 *          even NUD_PROBE is perfectly normal; we merely record the new state.
 *
 *        - A neighbour's entry may be deleted (RTM_DELNEIGH), for example due
 *          to garbage collection.  This is not necessarily of immediate
 *          concern; we record the neighbour as moving to NUD_NONE.
 *
 *        - A neighbour transitioning to NUD_FAILED (for any reason) is
 *          critically important and is handled as described below in #4.
 *
 *   3. All on-link neighbours in the watch list can be forcibly "probed" by
 *      calling #probeAll(). This should be called whenever it is important to
 *      verify that critical neighbours on the link are still reachable, e.g.
 *      when roaming between BSSIDs.
 *
 *        - The kernel will send unicast ARP requests for IPv4 neighbours and
 *          unicast NS packets for IPv6 neighbours.  The expected replies will
 *          likely be unicast.
 *
 *        - The forced probing is done holding a wakelock. The kernel may,
 *          however, initiate probing of a neighbor on its own, i.e. whenever
 *          a neighbour has expired from NUD_DELAY.
 *
 *        - The kernel sends:
 *
 *              /proc/sys/net/ipv{4,6}/neigh/<ifname>/ucast_solicit
 *
 *          number of probes (usually 3) every:
 *
 *              /proc/sys/net/ipv{4,6}/neigh/<ifname>/retrans_time_ms
 *
 *          number of milliseconds (usually 1000ms). This normally results in
 *          3 unicast packets, 1 per second.
 *
 *        - If no response is received to any of the probe packets, the kernel
 *          marks the neighbour as being in state NUD_FAILED, and the listening
 *          process in #2 will learn of it.
 *
 *   4. We call the supplied Callback#notifyLost() function if the loss of a
 *      neighbour in NUD_FAILED would cause IPv4 or IPv6 configuration to
 *      become incomplete (a loss of provisioning).
 *
 *        - For example, losing all our IPv4 on-link DNS servers (or losing
 *          our only IPv6 default gateway) constitutes a loss of IPv4 (IPv6)
 *          provisioning; Callback#notifyLost() would be called.
 *
 *        - Since it can be non-trivial to reacquire certain IP provisioning
 *          state it may be best for the link to disconnect completely and
 *          reconnect afresh.
 *
 * @unknown 
 */
public class IpReachabilityMonitor {
    private static final java.lang.String TAG = "IpReachabilityMonitor";

    private static final boolean DBG = false;

    private static final boolean VDBG = false;

    public interface Callback {
        // This callback function must execute as quickly as possible as it is
        // run on the same thread that listens to kernel neighbor updates.
        // 
        // TODO: refactor to something like notifyProvisioningLost(String msg).
        public void notifyLost(java.net.InetAddress ip, java.lang.String logMsg);
    }

    private final java.lang.Object mLock = new java.lang.Object();

    private final android.os.PowerManager.WakeLock mWakeLock;

    private final java.lang.String mInterfaceName;

    private final int mInterfaceIndex;

    private final android.net.ip.IpReachabilityMonitor.Callback mCallback;

    private final android.net.util.AvoidBadWifiTracker mAvoidBadWifiTracker;

    private final android.net.ip.IpReachabilityMonitor.NetlinkSocketObserver mNetlinkSocketObserver;

    private final java.lang.Thread mObserverThread;

    private final android.net.metrics.IpConnectivityLog mMetricsLog = new android.net.metrics.IpConnectivityLog();

    @com.android.internal.annotations.GuardedBy("mLock")
    private android.net.LinkProperties mLinkProperties = new android.net.LinkProperties();

    // TODO: consider a map to a private NeighborState class holding more
    // information than a single NUD state entry.
    @com.android.internal.annotations.GuardedBy("mLock")
    private java.util.Map<java.net.InetAddress, java.lang.Short> mIpWatchList = new java.util.HashMap<>();

    @com.android.internal.annotations.GuardedBy("mLock")
    private int mIpWatchListVersion;

    @com.android.internal.annotations.GuardedBy("mLock")
    private boolean mRunning;

    // Time in milliseconds of the last forced probe request.
    private volatile long mLastProbeTimeMs;

    /**
     * Make the kernel perform neighbor reachability detection (IPv4 ARP or IPv6 ND)
     * for the given IP address on the specified interface index.
     *
     * @return 0 if the request was successfully passed to the kernel; otherwise return
    a non-zero error code.
     */
    private static int probeNeighbor(int ifIndex, java.net.InetAddress ip) {
        final java.lang.String msgSnippet = (("probing ip=" + ip.getHostAddress()) + "%") + ifIndex;
        if (android.net.ip.IpReachabilityMonitor.DBG) {
            android.util.Log.d(android.net.ip.IpReachabilityMonitor.TAG, msgSnippet);
        }
        final byte[] msg = android.net.netlink.RtNetlinkNeighborMessage.newNewNeighborMessage(1, ip, android.net.netlink.StructNdMsg.NUD_PROBE, ifIndex, null);
        int errno = -android.system.OsConstants.EPROTO;
        try (android.net.netlink.NetlinkSocket nlSocket = new android.net.netlink.NetlinkSocket(android.system.OsConstants.NETLINK_ROUTE)) {
            final long IO_TIMEOUT = 300L;
            nlSocket.connectToKernel();
            nlSocket.sendMessage(msg, 0, msg.length, IO_TIMEOUT);
            final java.nio.ByteBuffer bytes = nlSocket.recvMessage(IO_TIMEOUT);
            // recvMessage() guaranteed to not return null if it did not throw.
            final android.net.netlink.NetlinkMessage response = android.net.netlink.NetlinkMessage.parse(bytes);
            if (((response != null) && (response instanceof android.net.netlink.NetlinkErrorMessage)) && (((android.net.netlink.NetlinkErrorMessage) (response)).getNlMsgError() != null)) {
                errno = ((android.net.netlink.NetlinkErrorMessage) (response)).getNlMsgError().error;
                if (errno != 0) {
                    // TODO: consider ignoring EINVAL (-22), which appears to be
                    // normal when probing a neighbor for which the kernel does
                    // not already have / no longer has a link layer address.
                    android.util.Log.e(android.net.ip.IpReachabilityMonitor.TAG, (("Error " + msgSnippet) + ", errmsg=") + response.toString());
                }
            } else {
                java.lang.String errmsg;
                if (response == null) {
                    bytes.position(0);
                    errmsg = "raw bytes: " + android.net.netlink.NetlinkConstants.hexify(bytes);
                } else {
                    errmsg = response.toString();
                }
                android.util.Log.e(android.net.ip.IpReachabilityMonitor.TAG, (("Error " + msgSnippet) + ", errmsg=") + errmsg);
            }
        } catch (android.system.ErrnoException e) {
            android.util.Log.e(android.net.ip.IpReachabilityMonitor.TAG, "Error " + msgSnippet, e);
            errno = -e.errno;
        } catch (java.io.InterruptedIOException e) {
            android.util.Log.e(android.net.ip.IpReachabilityMonitor.TAG, "Error " + msgSnippet, e);
            errno = -android.system.OsConstants.ETIMEDOUT;
        } catch (java.net.SocketException e) {
            android.util.Log.e(android.net.ip.IpReachabilityMonitor.TAG, "Error " + msgSnippet, e);
            errno = -android.system.OsConstants.EIO;
        }
        return errno;
    }

    public IpReachabilityMonitor(android.content.Context context, java.lang.String ifName, android.net.ip.IpReachabilityMonitor.Callback callback) {
        this(context, ifName, callback, null);
    }

    public IpReachabilityMonitor(android.content.Context context, java.lang.String ifName, android.net.ip.IpReachabilityMonitor.Callback callback, android.net.util.AvoidBadWifiTracker tracker) throws java.lang.IllegalArgumentException {
        mInterfaceName = ifName;
        int ifIndex = -1;
        try {
            java.net.NetworkInterface netIf = java.net.NetworkInterface.getByName(ifName);
            mInterfaceIndex = netIf.getIndex();
        } catch (java.net.SocketException | java.lang.NullPointerException e) {
            throw new java.lang.IllegalArgumentException(("invalid interface '" + ifName) + "': ", e);
        }
        mWakeLock = ((android.os.PowerManager) (context.getSystemService(android.content.Context.POWER_SERVICE))).newWakeLock(android.os.PowerManager.PARTIAL_WAKE_LOCK, (android.net.ip.IpReachabilityMonitor.TAG + ".") + mInterfaceName);
        mCallback = callback;
        mAvoidBadWifiTracker = tracker;
        mNetlinkSocketObserver = new android.net.ip.IpReachabilityMonitor.NetlinkSocketObserver();
        mObserverThread = new java.lang.Thread(mNetlinkSocketObserver);
        mObserverThread.start();
    }

    public void stop() {
        synchronized(mLock) {
            mRunning = false;
        }
        clearLinkProperties();
        mNetlinkSocketObserver.clearNetlinkSocket();
    }

    // TODO: add a public dump() method that can be called during a bug report.
    private java.lang.String describeWatchList() {
        final java.lang.String delimiter = ", ";
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        synchronized(mLock) {
            sb.append(((("iface{" + mInterfaceName) + "/") + mInterfaceIndex) + "}, ");
            sb.append(("v{" + mIpWatchListVersion) + "}, ");
            sb.append("ntable=[");
            boolean firstTime = true;
            for (java.util.Map.Entry<java.net.InetAddress, java.lang.Short> entry : mIpWatchList.entrySet()) {
                if (firstTime) {
                    firstTime = false;
                } else {
                    sb.append(delimiter);
                }
                sb.append((entry.getKey().getHostAddress() + "/") + android.net.netlink.StructNdMsg.stringForNudState(entry.getValue()));
            }
            sb.append("]");
        }
        return sb.toString();
    }

    private boolean isWatching(java.net.InetAddress ip) {
        synchronized(mLock) {
            return mRunning && mIpWatchList.containsKey(ip);
        }
    }

    private boolean stillRunning() {
        synchronized(mLock) {
            return mRunning;
        }
    }

    private static boolean isOnLink(java.util.List<android.net.RouteInfo> routes, java.net.InetAddress ip) {
        for (android.net.RouteInfo route : routes) {
            if ((!route.hasGateway()) && route.matches(ip)) {
                return true;
            }
        }
        return false;
    }

    private short getNeighborStateLocked(java.net.InetAddress ip) {
        if (mIpWatchList.containsKey(ip)) {
            return mIpWatchList.get(ip);
        }
        return android.net.netlink.StructNdMsg.NUD_NONE;
    }

    public void updateLinkProperties(android.net.LinkProperties lp) {
        if (!mInterfaceName.equals(lp.getInterfaceName())) {
            // TODO: figure out whether / how to cope with interface changes.
            android.util.Log.wtf(android.net.ip.IpReachabilityMonitor.TAG, (("requested LinkProperties interface '" + lp.getInterfaceName()) + "' does not match: ") + mInterfaceName);
            return;
        }
        synchronized(mLock) {
            mLinkProperties = new android.net.LinkProperties(lp);
            java.util.Map<java.net.InetAddress, java.lang.Short> newIpWatchList = new java.util.HashMap<>();
            final java.util.List<android.net.RouteInfo> routes = mLinkProperties.getRoutes();
            for (android.net.RouteInfo route : routes) {
                if (route.hasGateway()) {
                    java.net.InetAddress gw = route.getGateway();
                    if (android.net.ip.IpReachabilityMonitor.isOnLink(routes, gw)) {
                        newIpWatchList.put(gw, getNeighborStateLocked(gw));
                    }
                }
            }
            for (java.net.InetAddress nameserver : lp.getDnsServers()) {
                if (android.net.ip.IpReachabilityMonitor.isOnLink(routes, nameserver)) {
                    newIpWatchList.put(nameserver, getNeighborStateLocked(nameserver));
                }
            }
            mIpWatchList = newIpWatchList;
            mIpWatchListVersion++;
        }
        if (android.net.ip.IpReachabilityMonitor.DBG) {
            android.util.Log.d(android.net.ip.IpReachabilityMonitor.TAG, "watch: " + describeWatchList());
        }
    }

    public void clearLinkProperties() {
        synchronized(mLock) {
            mLinkProperties.clear();
            mIpWatchList.clear();
            mIpWatchListVersion++;
        }
        if (android.net.ip.IpReachabilityMonitor.DBG) {
            android.util.Log.d(android.net.ip.IpReachabilityMonitor.TAG, "clear: " + describeWatchList());
        }
    }

    private void handleNeighborLost(java.lang.String msg) {
        java.net.InetAddress ip = null;
        final android.net.LinkProperties.ProvisioningChange delta;
        synchronized(mLock) {
            android.net.LinkProperties whatIfLp = new android.net.LinkProperties(mLinkProperties);
            for (java.util.Map.Entry<java.net.InetAddress, java.lang.Short> entry : mIpWatchList.entrySet()) {
                if (entry.getValue() != android.net.netlink.StructNdMsg.NUD_FAILED) {
                    continue;
                }
                ip = entry.getKey();
                for (android.net.RouteInfo route : mLinkProperties.getRoutes()) {
                    if (ip.equals(route.getGateway())) {
                        whatIfLp.removeRoute(route);
                    }
                }
                if (avoidingBadLinks() || (!(ip instanceof java.net.Inet6Address))) {
                    // We should do this unconditionally, but alas we cannot: b/31827713.
                    whatIfLp.removeDnsServer(ip);
                }
            }
            delta = android.net.LinkProperties.compareProvisioning(mLinkProperties, whatIfLp);
        }
        if (delta == android.net.LinkProperties.ProvisioningChange.LOST_PROVISIONING) {
            final java.lang.String logMsg = "FAILURE: LOST_PROVISIONING, " + msg;
            android.util.Log.w(android.net.ip.IpReachabilityMonitor.TAG, logMsg);
            if (mCallback != null) {
                // TODO: remove |ip| when the callback signature no longer has
                // an InetAddress argument.
                mCallback.notifyLost(ip, logMsg);
            }
        }
        logNudFailed(delta);
    }

    private boolean avoidingBadLinks() {
        return mAvoidBadWifiTracker != null ? mAvoidBadWifiTracker.currentValue() : true;
    }

    public void probeAll() {
        java.util.Set<java.net.InetAddress> ipProbeList = new java.util.HashSet<java.net.InetAddress>();
        synchronized(mLock) {
            ipProbeList.addAll(mIpWatchList.keySet());
        }
        if ((!ipProbeList.isEmpty()) && stillRunning()) {
            // Keep the CPU awake long enough to allow all ARP/ND
            // probes a reasonable chance at success. See b/23197666.
            // 
            // The wakelock we use is (by default) refcounted, and this version
            // of acquire(timeout) queues a release message to keep acquisitions
            // and releases balanced.
            mWakeLock.acquire(android.net.ip.IpReachabilityMonitor.getProbeWakeLockDuration());
        }
        for (java.net.InetAddress target : ipProbeList) {
            if (!stillRunning()) {
                break;
            }
            final int returnValue = android.net.ip.IpReachabilityMonitor.probeNeighbor(mInterfaceIndex, target);
            logEvent(android.net.metrics.IpReachabilityEvent.PROBE, returnValue);
        }
        mLastProbeTimeMs = android.os.SystemClock.elapsedRealtime();
    }

    private static long getProbeWakeLockDuration() {
        // Ideally, this would be computed by examining the values of:
        // 
        // /proc/sys/net/ipv[46]/neigh/<ifname>/ucast_solicit
        // 
        // and:
        // 
        // /proc/sys/net/ipv[46]/neigh/<ifname>/retrans_time_ms
        // 
        // For now, just make some assumptions.
        final long numUnicastProbes = 3;
        final long retransTimeMs = 1000;
        final long gracePeriodMs = 500;
        return (numUnicastProbes * retransTimeMs) + gracePeriodMs;
    }

    private void logEvent(int probeType, int errorCode) {
        int eventType = probeType | (errorCode & 0xff);
        mMetricsLog.log(new android.net.metrics.IpReachabilityEvent(mInterfaceName, eventType));
    }

    private void logNudFailed(android.net.LinkProperties.ProvisioningChange delta) {
        long duration = android.os.SystemClock.elapsedRealtime() - mLastProbeTimeMs;
        boolean isFromProbe = duration < android.net.ip.IpReachabilityMonitor.getProbeWakeLockDuration();
        boolean isProvisioningLost = delta == android.net.LinkProperties.ProvisioningChange.LOST_PROVISIONING;
        int eventType = android.net.metrics.IpReachabilityEvent.nudFailureEventType(isFromProbe, isProvisioningLost);
        mMetricsLog.log(new android.net.metrics.IpReachabilityEvent(mInterfaceName, eventType));
    }

    // TODO: simplify the number of objects by making this extend Thread.
    private final class NetlinkSocketObserver implements java.lang.Runnable {
        private android.net.netlink.NetlinkSocket mSocket;

        @java.lang.Override
        public void run() {
            if (android.net.ip.IpReachabilityMonitor.VDBG) {
                android.util.Log.d(android.net.ip.IpReachabilityMonitor.TAG, "Starting observing thread.");
            }
            synchronized(mLock) {
                mRunning = true;
            }
            try {
                setupNetlinkSocket();
            } catch (android.system.ErrnoException | java.net.SocketException e) {
                android.util.Log.e(android.net.ip.IpReachabilityMonitor.TAG, "Failed to suitably initialize a netlink socket", e);
                synchronized(mLock) {
                    mRunning = false;
                }
            }
            java.nio.ByteBuffer byteBuffer;
            while (stillRunning()) {
                try {
                    byteBuffer = recvKernelReply();
                } catch (android.system.ErrnoException e) {
                    if (stillRunning()) {
                        android.util.Log.w(android.net.ip.IpReachabilityMonitor.TAG, "ErrnoException: ", e);
                    }
                    break;
                }
                final long whenMs = android.os.SystemClock.elapsedRealtime();
                if (byteBuffer == null) {
                    continue;
                }
                parseNetlinkMessageBuffer(byteBuffer, whenMs);
            } 
            clearNetlinkSocket();
            synchronized(mLock) {
                mRunning = false;
            }
            if (android.net.ip.IpReachabilityMonitor.VDBG) {
                android.util.Log.d(android.net.ip.IpReachabilityMonitor.TAG, "Finishing observing thread.");
            }
        }

        private void clearNetlinkSocket() {
            if (mSocket != null) {
                mSocket.close();
            }
        }

        // TODO: Refactor the main loop to recreate the socket upon recoverable errors.
        private void setupNetlinkSocket() throws android.system.ErrnoException, java.net.SocketException {
            clearNetlinkSocket();
            mSocket = new android.net.netlink.NetlinkSocket(android.system.OsConstants.NETLINK_ROUTE);
            final android.system.NetlinkSocketAddress listenAddr = new android.system.NetlinkSocketAddress(0, android.system.OsConstants.RTMGRP_NEIGH);
            mSocket.bind(listenAddr);
            if (android.net.ip.IpReachabilityMonitor.VDBG) {
                final android.system.NetlinkSocketAddress nlAddr = mSocket.getLocalAddress();
                android.util.Log.d(android.net.ip.IpReachabilityMonitor.TAG, ((("bound to sockaddr_nl{" + ((long) (nlAddr.getPortId() & 0xffffffff))) + ", ") + nlAddr.getGroupsMask()) + "}");
            }
        }

        private java.nio.ByteBuffer recvKernelReply() throws android.system.ErrnoException {
            try {
                return mSocket.recvMessage(0);
            } catch (java.io.InterruptedIOException e) {
                // Interruption or other error, e.g. another thread closed our file descriptor.
            } catch (android.system.ErrnoException e) {
                if (e.errno != android.system.OsConstants.EAGAIN) {
                    throw e;
                }
            }
            return null;
        }

        private void parseNetlinkMessageBuffer(java.nio.ByteBuffer byteBuffer, long whenMs) {
            while (byteBuffer.remaining() > 0) {
                final int position = byteBuffer.position();
                final android.net.netlink.NetlinkMessage nlMsg = android.net.netlink.NetlinkMessage.parse(byteBuffer);
                if ((nlMsg == null) || (nlMsg.getHeader() == null)) {
                    byteBuffer.position(position);
                    android.util.Log.e(android.net.ip.IpReachabilityMonitor.TAG, "unparsable netlink msg: " + android.net.netlink.NetlinkConstants.hexify(byteBuffer));
                    break;
                }
                final int srcPortId = nlMsg.getHeader().nlmsg_pid;
                if (srcPortId != 0) {
                    android.util.Log.e(android.net.ip.IpReachabilityMonitor.TAG, "non-kernel source portId: " + ((long) (srcPortId & 0xffffffff)));
                    break;
                }
                if (nlMsg instanceof android.net.netlink.NetlinkErrorMessage) {
                    android.util.Log.e(android.net.ip.IpReachabilityMonitor.TAG, "netlink error: " + nlMsg);
                    continue;
                } else
                    if (!(nlMsg instanceof android.net.netlink.RtNetlinkNeighborMessage)) {
                        if (android.net.ip.IpReachabilityMonitor.DBG) {
                            android.util.Log.d(android.net.ip.IpReachabilityMonitor.TAG, "non-rtnetlink neighbor msg: " + nlMsg);
                        }
                        continue;
                    }

                evaluateRtNetlinkNeighborMessage(((android.net.netlink.RtNetlinkNeighborMessage) (nlMsg)), whenMs);
            } 
        }

        private void evaluateRtNetlinkNeighborMessage(android.net.netlink.RtNetlinkNeighborMessage neighMsg, long whenMs) {
            final android.net.netlink.StructNdMsg ndMsg = neighMsg.getNdHeader();
            if ((ndMsg == null) || (ndMsg.ndm_ifindex != mInterfaceIndex)) {
                return;
            }
            final java.net.InetAddress destination = neighMsg.getDestination();
            if (!isWatching(destination)) {
                return;
            }
            final short msgType = neighMsg.getHeader().nlmsg_type;
            final short nudState = ndMsg.ndm_state;
            final java.lang.String eventMsg = ((((((((((("NeighborEvent{" + "elapsedMs=") + whenMs) + ", ") + destination.getHostAddress()) + ", ") + "[") + android.net.netlink.NetlinkConstants.hexify(neighMsg.getLinkLayerAddress())) + "], ") + android.net.netlink.NetlinkConstants.stringForNlMsgType(msgType)) + ", ") + android.net.netlink.StructNdMsg.stringForNudState(nudState)) + "}";
            if (android.net.ip.IpReachabilityMonitor.VDBG) {
                android.util.Log.d(android.net.ip.IpReachabilityMonitor.TAG, neighMsg.toString());
            } else
                if (android.net.ip.IpReachabilityMonitor.DBG) {
                    android.util.Log.d(android.net.ip.IpReachabilityMonitor.TAG, eventMsg);
                }

            synchronized(mLock) {
                if (mIpWatchList.containsKey(destination)) {
                    final short value = (msgType == android.net.netlink.NetlinkConstants.RTM_DELNEIGH) ? android.net.netlink.StructNdMsg.NUD_NONE : nudState;
                    mIpWatchList.put(destination, value);
                }
            }
            if (nudState == android.net.netlink.StructNdMsg.NUD_FAILED) {
                android.util.Log.w(android.net.ip.IpReachabilityMonitor.TAG, "ALERT: " + eventMsg);
                handleNeighborLost(eventMsg);
            }
        }
    }
}

