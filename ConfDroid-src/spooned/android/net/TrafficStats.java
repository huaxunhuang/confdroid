/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * Class that provides network traffic statistics. These statistics include
 * bytes transmitted and received and network packets transmitted and received,
 * over all interfaces, over the mobile interface, and on a per-UID basis.
 * <p>
 * These statistics may not be available on all platforms. If the statistics are
 * not supported by this device, {@link #UNSUPPORTED} will be returned.
 * <p>
 * Note that the statistics returned by this class reset and start from zero
 * after every reboot. To access more robust historical network statistics data,
 * use {@link NetworkStatsManager} instead.
 */
public class TrafficStats {
    /**
     * The return value to indicate that the device does not support the statistic.
     */
    public static final int UNSUPPORTED = -1;

    /**
     *
     *
     * @unknown 
     */
    public static final long KB_IN_BYTES = 1024;

    /**
     *
     *
     * @unknown 
     */
    public static final long MB_IN_BYTES = android.net.TrafficStats.KB_IN_BYTES * 1024;

    /**
     *
     *
     * @unknown 
     */
    public static final long GB_IN_BYTES = android.net.TrafficStats.MB_IN_BYTES * 1024;

    /**
     *
     *
     * @unknown 
     */
    public static final long TB_IN_BYTES = android.net.TrafficStats.GB_IN_BYTES * 1024;

    /**
     *
     *
     * @unknown 
     */
    public static final long PB_IN_BYTES = android.net.TrafficStats.TB_IN_BYTES * 1024;

    /**
     * Special UID value used when collecting {@link NetworkStatsHistory} for
     * removed applications.
     *
     * @unknown 
     */
    public static final int UID_REMOVED = -4;

    /**
     * Special UID value used when collecting {@link NetworkStatsHistory} for
     * tethering traffic.
     *
     * @unknown 
     */
    public static final int UID_TETHERING = -5;

    /**
     * Default tag value for {@link DownloadManager} traffic.
     *
     * @unknown 
     */
    public static final int TAG_SYSTEM_DOWNLOAD = 0xffffff01;

    /**
     * Default tag value for {@link MediaPlayer} traffic.
     *
     * @unknown 
     */
    public static final int TAG_SYSTEM_MEDIA = 0xffffff02;

    /**
     * Default tag value for {@link BackupManager} backup traffic; that is,
     * traffic from the device to the storage backend.
     *
     * @unknown 
     */
    public static final int TAG_SYSTEM_BACKUP = 0xffffff03;

    /**
     * Default tag value for {@link BackupManager} restore traffic; that is,
     * app data retrieved from the storage backend at install time.
     *
     * @unknown 
     */
    public static final int TAG_SYSTEM_RESTORE = 0xffffff04;

    private static android.net.INetworkStatsService sStatsService;

    private static synchronized android.net.INetworkStatsService getStatsService() {
        if (android.net.TrafficStats.sStatsService == null) {
            android.net.TrafficStats.sStatsService = INetworkStatsService.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.NETWORK_STATS_SERVICE));
        }
        return android.net.TrafficStats.sStatsService;
    }

    /**
     * Snapshot of {@link NetworkStats} when the currently active profiling
     * session started, or {@code null} if no session active.
     *
     * @see #startDataProfiling(Context)
     * @see #stopDataProfiling(Context)
     */
    private static android.net.NetworkStats sActiveProfilingStart;

    private static java.lang.Object sProfilingLock = new java.lang.Object();

    /**
     * Set active tag to use when accounting {@link Socket} traffic originating
     * from the current thread. Only one active tag per thread is supported.
     * <p>
     * Changes only take effect during subsequent calls to
     * {@link #tagSocket(Socket)}.
     * <p>
     * Tags between {@code 0xFFFFFF00} and {@code 0xFFFFFFFF} are reserved and
     * used internally by system services like {@link DownloadManager} when
     * performing traffic on behalf of an application.
     *
     * @see #clearThreadStatsTag()
     */
    public static void setThreadStatsTag(int tag) {
        com.android.server.NetworkManagementSocketTagger.setThreadSocketStatsTag(tag);
    }

    /**
     * Set active tag to use when accounting {@link Socket} traffic originating
     * from the current thread. The tag used internally is well-defined to
     * distinguish all backup-related traffic.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static void setThreadStatsTagBackup() {
        android.net.TrafficStats.setThreadStatsTag(android.net.TrafficStats.TAG_SYSTEM_BACKUP);
    }

    /**
     * Set active tag to use when accounting {@link Socket} traffic originating
     * from the current thread. The tag used internally is well-defined to
     * distinguish all restore-related traffic.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static void setThreadStatsTagRestore() {
        android.net.TrafficStats.setThreadStatsTag(android.net.TrafficStats.TAG_SYSTEM_RESTORE);
    }

    /**
     * Get the active tag used when accounting {@link Socket} traffic originating
     * from the current thread. Only one active tag per thread is supported.
     * {@link #tagSocket(Socket)}.
     *
     * @see #setThreadStatsTag(int)
     */
    public static int getThreadStatsTag() {
        return com.android.server.NetworkManagementSocketTagger.getThreadSocketStatsTag();
    }

    /**
     * Clear any active tag set to account {@link Socket} traffic originating
     * from the current thread.
     *
     * @see #setThreadStatsTag(int)
     */
    public static void clearThreadStatsTag() {
        com.android.server.NetworkManagementSocketTagger.setThreadSocketStatsTag(-1);
    }

    /**
     * Set specific UID to use when accounting {@link Socket} traffic
     * originating from the current thread. Designed for use when performing an
     * operation on behalf of another application.
     * <p>
     * Changes only take effect during subsequent calls to
     * {@link #tagSocket(Socket)}.
     * <p>
     * To take effect, caller must hold
     * {@link android.Manifest.permission#UPDATE_DEVICE_STATS} permission.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static void setThreadStatsUid(int uid) {
        com.android.server.NetworkManagementSocketTagger.setThreadSocketStatsUid(uid);
    }

    /**
     * Clear any active UID set to account {@link Socket} traffic originating
     * from the current thread.
     *
     * @see #setThreadStatsUid(int)
     * @unknown 
     */
    @android.annotation.SystemApi
    public static void clearThreadStatsUid() {
        com.android.server.NetworkManagementSocketTagger.setThreadSocketStatsUid(-1);
    }

    /**
     * Tag the given {@link Socket} with any statistics parameters active for
     * the current thread. Subsequent calls always replace any existing
     * parameters. When finished, call {@link #untagSocket(Socket)} to remove
     * statistics parameters.
     *
     * @see #setThreadStatsTag(int)
     */
    public static void tagSocket(java.net.Socket socket) throws java.net.SocketException {
        dalvik.system.SocketTagger.get().tag(socket);
    }

    /**
     * Remove any statistics parameters from the given {@link Socket}.
     */
    public static void untagSocket(java.net.Socket socket) throws java.net.SocketException {
        dalvik.system.SocketTagger.get().untag(socket);
    }

    /**
     * Tag the given {@link DatagramSocket} with any statistics parameters
     * active for the current thread. Subsequent calls always replace any
     * existing parameters. When finished, call
     * {@link #untagDatagramSocket(DatagramSocket)} to remove statistics
     * parameters.
     *
     * @see #setThreadStatsTag(int)
     */
    public static void tagDatagramSocket(java.net.DatagramSocket socket) throws java.net.SocketException {
        dalvik.system.SocketTagger.get().tag(socket);
    }

    /**
     * Remove any statistics parameters from the given {@link DatagramSocket}.
     */
    public static void untagDatagramSocket(java.net.DatagramSocket socket) throws java.net.SocketException {
        dalvik.system.SocketTagger.get().untag(socket);
    }

    /**
     * Start profiling data usage for current UID. Only one profiling session
     * can be active at a time.
     *
     * @unknown 
     */
    public static void startDataProfiling(android.content.Context context) {
        synchronized(android.net.TrafficStats.sProfilingLock) {
            if (android.net.TrafficStats.sActiveProfilingStart != null) {
                throw new java.lang.IllegalStateException("already profiling data");
            }
            // take snapshot in time; we calculate delta later
            android.net.TrafficStats.sActiveProfilingStart = android.net.TrafficStats.getDataLayerSnapshotForUid(context);
        }
    }

    /**
     * Stop profiling data usage for current UID.
     *
     * @return Detailed {@link NetworkStats} of data that occurred since last
    {@link #startDataProfiling(Context)} call.
     * @unknown 
     */
    public static android.net.NetworkStats stopDataProfiling(android.content.Context context) {
        synchronized(android.net.TrafficStats.sProfilingLock) {
            if (android.net.TrafficStats.sActiveProfilingStart == null) {
                throw new java.lang.IllegalStateException("not profiling data");
            }
            // subtract starting values and return delta
            final android.net.NetworkStats profilingStop = android.net.TrafficStats.getDataLayerSnapshotForUid(context);
            final android.net.NetworkStats profilingDelta = android.net.NetworkStats.subtract(profilingStop, android.net.TrafficStats.sActiveProfilingStart, null, null);
            android.net.TrafficStats.sActiveProfilingStart = null;
            return profilingDelta;
        }
    }

    /**
     * Increment count of network operations performed under the accounting tag
     * currently active on the calling thread. This can be used to derive
     * bytes-per-operation.
     *
     * @param operationCount
     * 		Number of operations to increment count by.
     */
    public static void incrementOperationCount(int operationCount) {
        final int tag = android.net.TrafficStats.getThreadStatsTag();
        android.net.TrafficStats.incrementOperationCount(tag, operationCount);
    }

    /**
     * Increment count of network operations performed under the given
     * accounting tag. This can be used to derive bytes-per-operation.
     *
     * @param tag
     * 		Accounting tag used in {@link #setThreadStatsTag(int)}.
     * @param operationCount
     * 		Number of operations to increment count by.
     */
    public static void incrementOperationCount(int tag, int operationCount) {
        final int uid = android.os.Process.myUid();
        try {
            android.net.TrafficStats.getStatsService().incrementOperationCount(uid, tag, operationCount);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public static void closeQuietly(android.net.INetworkStatsSession session) {
        // TODO: move to NetworkStatsService once it exists
        if (session != null) {
            try {
                session.close();
            } catch (java.lang.RuntimeException rethrown) {
                throw rethrown;
            } catch (java.lang.Exception ignored) {
            }
        }
    }

    /**
     * Return number of packets transmitted across mobile networks since device
     * boot. Counts packets across all mobile network interfaces, and always
     * increases monotonically since device boot. Statistics are measured at the
     * network layer, so they include both TCP and UDP usage.
     * <p>
     * Before {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2}, this may
     * return {@link #UNSUPPORTED} on devices where statistics aren't available.
     */
    public static long getMobileTxPackets() {
        long total = 0;
        for (java.lang.String iface : android.net.TrafficStats.getMobileIfaces()) {
            total += android.net.TrafficStats.getTxPackets(iface);
        }
        return total;
    }

    /**
     * Return number of packets received across mobile networks since device
     * boot. Counts packets across all mobile network interfaces, and always
     * increases monotonically since device boot. Statistics are measured at the
     * network layer, so they include both TCP and UDP usage.
     * <p>
     * Before {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2}, this may
     * return {@link #UNSUPPORTED} on devices where statistics aren't available.
     */
    public static long getMobileRxPackets() {
        long total = 0;
        for (java.lang.String iface : android.net.TrafficStats.getMobileIfaces()) {
            total += android.net.TrafficStats.getRxPackets(iface);
        }
        return total;
    }

    /**
     * Return number of bytes transmitted across mobile networks since device
     * boot. Counts packets across all mobile network interfaces, and always
     * increases monotonically since device boot. Statistics are measured at the
     * network layer, so they include both TCP and UDP usage.
     * <p>
     * Before {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2}, this may
     * return {@link #UNSUPPORTED} on devices where statistics aren't available.
     */
    public static long getMobileTxBytes() {
        long total = 0;
        for (java.lang.String iface : android.net.TrafficStats.getMobileIfaces()) {
            total += android.net.TrafficStats.getTxBytes(iface);
        }
        return total;
    }

    /**
     * Return number of bytes received across mobile networks since device boot.
     * Counts packets across all mobile network interfaces, and always increases
     * monotonically since device boot. Statistics are measured at the network
     * layer, so they include both TCP and UDP usage.
     * <p>
     * Before {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2}, this may
     * return {@link #UNSUPPORTED} on devices where statistics aren't available.
     */
    public static long getMobileRxBytes() {
        long total = 0;
        for (java.lang.String iface : android.net.TrafficStats.getMobileIfaces()) {
            total += android.net.TrafficStats.getRxBytes(iface);
        }
        return total;
    }

    /**
     * {@hide }
     */
    public static long getMobileTcpRxPackets() {
        long total = 0;
        for (java.lang.String iface : android.net.TrafficStats.getMobileIfaces()) {
            final long stat = android.net.TrafficStats.nativeGetIfaceStat(iface, android.net.TrafficStats.TYPE_TCP_RX_PACKETS);
            if (stat != android.net.TrafficStats.UNSUPPORTED) {
                total += stat;
            }
        }
        return total;
    }

    /**
     * {@hide }
     */
    public static long getMobileTcpTxPackets() {
        long total = 0;
        for (java.lang.String iface : android.net.TrafficStats.getMobileIfaces()) {
            final long stat = android.net.TrafficStats.nativeGetIfaceStat(iface, android.net.TrafficStats.TYPE_TCP_TX_PACKETS);
            if (stat != android.net.TrafficStats.UNSUPPORTED) {
                total += stat;
            }
        }
        return total;
    }

    /**
     * {@hide }
     */
    public static long getTxPackets(java.lang.String iface) {
        return android.net.TrafficStats.nativeGetIfaceStat(iface, android.net.TrafficStats.TYPE_TX_PACKETS);
    }

    /**
     * {@hide }
     */
    public static long getRxPackets(java.lang.String iface) {
        return android.net.TrafficStats.nativeGetIfaceStat(iface, android.net.TrafficStats.TYPE_RX_PACKETS);
    }

    /**
     * {@hide }
     */
    public static long getTxBytes(java.lang.String iface) {
        return android.net.TrafficStats.nativeGetIfaceStat(iface, android.net.TrafficStats.TYPE_TX_BYTES);
    }

    /**
     * {@hide }
     */
    public static long getRxBytes(java.lang.String iface) {
        return android.net.TrafficStats.nativeGetIfaceStat(iface, android.net.TrafficStats.TYPE_RX_BYTES);
    }

    /**
     * Return number of packets transmitted since device boot. Counts packets
     * across all network interfaces, and always increases monotonically since
     * device boot. Statistics are measured at the network layer, so they
     * include both TCP and UDP usage.
     * <p>
     * Before {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2}, this may
     * return {@link #UNSUPPORTED} on devices where statistics aren't available.
     */
    public static long getTotalTxPackets() {
        return android.net.TrafficStats.nativeGetTotalStat(android.net.TrafficStats.TYPE_TX_PACKETS);
    }

    /**
     * Return number of packets received since device boot. Counts packets
     * across all network interfaces, and always increases monotonically since
     * device boot. Statistics are measured at the network layer, so they
     * include both TCP and UDP usage.
     * <p>
     * Before {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2}, this may
     * return {@link #UNSUPPORTED} on devices where statistics aren't available.
     */
    public static long getTotalRxPackets() {
        return android.net.TrafficStats.nativeGetTotalStat(android.net.TrafficStats.TYPE_RX_PACKETS);
    }

    /**
     * Return number of bytes transmitted since device boot. Counts packets
     * across all network interfaces, and always increases monotonically since
     * device boot. Statistics are measured at the network layer, so they
     * include both TCP and UDP usage.
     * <p>
     * Before {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2}, this may
     * return {@link #UNSUPPORTED} on devices where statistics aren't available.
     */
    public static long getTotalTxBytes() {
        return android.net.TrafficStats.nativeGetTotalStat(android.net.TrafficStats.TYPE_TX_BYTES);
    }

    /**
     * Return number of bytes received since device boot. Counts packets across
     * all network interfaces, and always increases monotonically since device
     * boot. Statistics are measured at the network layer, so they include both
     * TCP and UDP usage.
     * <p>
     * Before {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2}, this may
     * return {@link #UNSUPPORTED} on devices where statistics aren't available.
     */
    public static long getTotalRxBytes() {
        return android.net.TrafficStats.nativeGetTotalStat(android.net.TrafficStats.TYPE_RX_BYTES);
    }

    /**
     * Return number of bytes transmitted by the given UID since device boot.
     * Counts packets across all network interfaces, and always increases
     * monotonically since device boot. Statistics are measured at the network
     * layer, so they include both TCP and UDP usage.
     * <p>
     * Before {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2}, this may
     * return {@link #UNSUPPORTED} on devices where statistics aren't available.
     * <p>
     * Starting in {@link android.os.Build.VERSION_CODES#N} this will only
     * report traffic statistics for the calling UID. It will return
     * {@link #UNSUPPORTED} for all other UIDs for privacy reasons. To access
     * historical network statistics belonging to other UIDs, use
     * {@link NetworkStatsManager}.
     *
     * @see android.os.Process#myUid()
     * @see android.content.pm.ApplicationInfo#uid
     */
    public static long getUidTxBytes(int uid) {
        // This isn't actually enforcing any security; it just returns the
        // unsupported value. The real filtering is done at the kernel level.
        final int callingUid = android.os.Process.myUid();
        if ((callingUid == android.os.Process.SYSTEM_UID) || (callingUid == uid)) {
            return android.net.TrafficStats.nativeGetUidStat(uid, android.net.TrafficStats.TYPE_TX_BYTES);
        } else {
            return android.net.TrafficStats.UNSUPPORTED;
        }
    }

    /**
     * Return number of bytes received by the given UID since device boot.
     * Counts packets across all network interfaces, and always increases
     * monotonically since device boot. Statistics are measured at the network
     * layer, so they include both TCP and UDP usage.
     * <p>
     * Before {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2}, this may return
     * {@link #UNSUPPORTED} on devices where statistics aren't available.
     * <p>
     * Starting in {@link android.os.Build.VERSION_CODES#N} this will only
     * report traffic statistics for the calling UID. It will return
     * {@link #UNSUPPORTED} for all other UIDs for privacy reasons. To access
     * historical network statistics belonging to other UIDs, use
     * {@link NetworkStatsManager}.
     *
     * @see android.os.Process#myUid()
     * @see android.content.pm.ApplicationInfo#uid
     */
    public static long getUidRxBytes(int uid) {
        // This isn't actually enforcing any security; it just returns the
        // unsupported value. The real filtering is done at the kernel level.
        final int callingUid = android.os.Process.myUid();
        if ((callingUid == android.os.Process.SYSTEM_UID) || (callingUid == uid)) {
            return android.net.TrafficStats.nativeGetUidStat(uid, android.net.TrafficStats.TYPE_RX_BYTES);
        } else {
            return android.net.TrafficStats.UNSUPPORTED;
        }
    }

    /**
     * Return number of packets transmitted by the given UID since device boot.
     * Counts packets across all network interfaces, and always increases
     * monotonically since device boot. Statistics are measured at the network
     * layer, so they include both TCP and UDP usage.
     * <p>
     * Before {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2}, this may return
     * {@link #UNSUPPORTED} on devices where statistics aren't available.
     * <p>
     * Starting in {@link android.os.Build.VERSION_CODES#N} this will only
     * report traffic statistics for the calling UID. It will return
     * {@link #UNSUPPORTED} for all other UIDs for privacy reasons. To access
     * historical network statistics belonging to other UIDs, use
     * {@link NetworkStatsManager}.
     *
     * @see android.os.Process#myUid()
     * @see android.content.pm.ApplicationInfo#uid
     */
    public static long getUidTxPackets(int uid) {
        // This isn't actually enforcing any security; it just returns the
        // unsupported value. The real filtering is done at the kernel level.
        final int callingUid = android.os.Process.myUid();
        if ((callingUid == android.os.Process.SYSTEM_UID) || (callingUid == uid)) {
            return android.net.TrafficStats.nativeGetUidStat(uid, android.net.TrafficStats.TYPE_TX_PACKETS);
        } else {
            return android.net.TrafficStats.UNSUPPORTED;
        }
    }

    /**
     * Return number of packets received by the given UID since device boot.
     * Counts packets across all network interfaces, and always increases
     * monotonically since device boot. Statistics are measured at the network
     * layer, so they include both TCP and UDP usage.
     * <p>
     * Before {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2}, this may return
     * {@link #UNSUPPORTED} on devices where statistics aren't available.
     * <p>
     * Starting in {@link android.os.Build.VERSION_CODES#N} this will only
     * report traffic statistics for the calling UID. It will return
     * {@link #UNSUPPORTED} for all other UIDs for privacy reasons. To access
     * historical network statistics belonging to other UIDs, use
     * {@link NetworkStatsManager}.
     *
     * @see android.os.Process#myUid()
     * @see android.content.pm.ApplicationInfo#uid
     */
    public static long getUidRxPackets(int uid) {
        // This isn't actually enforcing any security; it just returns the
        // unsupported value. The real filtering is done at the kernel level.
        final int callingUid = android.os.Process.myUid();
        if ((callingUid == android.os.Process.SYSTEM_UID) || (callingUid == uid)) {
            return android.net.TrafficStats.nativeGetUidStat(uid, android.net.TrafficStats.TYPE_RX_PACKETS);
        } else {
            return android.net.TrafficStats.UNSUPPORTED;
        }
    }

    /**
     *
     *
     * @deprecated Starting in {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2},
    transport layer statistics are no longer available, and will
    always return {@link #UNSUPPORTED}.
     * @see #getUidTxBytes(int)
     */
    @java.lang.Deprecated
    public static long getUidTcpTxBytes(int uid) {
        return android.net.TrafficStats.UNSUPPORTED;
    }

    /**
     *
     *
     * @deprecated Starting in {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2},
    transport layer statistics are no longer available, and will
    always return {@link #UNSUPPORTED}.
     * @see #getUidRxBytes(int)
     */
    @java.lang.Deprecated
    public static long getUidTcpRxBytes(int uid) {
        return android.net.TrafficStats.UNSUPPORTED;
    }

    /**
     *
     *
     * @deprecated Starting in {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2},
    transport layer statistics are no longer available, and will
    always return {@link #UNSUPPORTED}.
     * @see #getUidTxBytes(int)
     */
    @java.lang.Deprecated
    public static long getUidUdpTxBytes(int uid) {
        return android.net.TrafficStats.UNSUPPORTED;
    }

    /**
     *
     *
     * @deprecated Starting in {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2},
    transport layer statistics are no longer available, and will
    always return {@link #UNSUPPORTED}.
     * @see #getUidRxBytes(int)
     */
    @java.lang.Deprecated
    public static long getUidUdpRxBytes(int uid) {
        return android.net.TrafficStats.UNSUPPORTED;
    }

    /**
     *
     *
     * @deprecated Starting in {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2},
    transport layer statistics are no longer available, and will
    always return {@link #UNSUPPORTED}.
     * @see #getUidTxPackets(int)
     */
    @java.lang.Deprecated
    public static long getUidTcpTxSegments(int uid) {
        return android.net.TrafficStats.UNSUPPORTED;
    }

    /**
     *
     *
     * @deprecated Starting in {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2},
    transport layer statistics are no longer available, and will
    always return {@link #UNSUPPORTED}.
     * @see #getUidRxPackets(int)
     */
    @java.lang.Deprecated
    public static long getUidTcpRxSegments(int uid) {
        return android.net.TrafficStats.UNSUPPORTED;
    }

    /**
     *
     *
     * @deprecated Starting in {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2},
    transport layer statistics are no longer available, and will
    always return {@link #UNSUPPORTED}.
     * @see #getUidTxPackets(int)
     */
    @java.lang.Deprecated
    public static long getUidUdpTxPackets(int uid) {
        return android.net.TrafficStats.UNSUPPORTED;
    }

    /**
     *
     *
     * @deprecated Starting in {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2},
    transport layer statistics are no longer available, and will
    always return {@link #UNSUPPORTED}.
     * @see #getUidRxPackets(int)
     */
    @java.lang.Deprecated
    public static long getUidUdpRxPackets(int uid) {
        return android.net.TrafficStats.UNSUPPORTED;
    }

    /**
     * Return detailed {@link NetworkStats} for the current UID. Requires no
     * special permission.
     */
    private static android.net.NetworkStats getDataLayerSnapshotForUid(android.content.Context context) {
        // TODO: take snapshot locally, since proc file is now visible
        final int uid = android.os.Process.myUid();
        try {
            return android.net.TrafficStats.getStatsService().getDataLayerSnapshotForUid(uid);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Return set of any ifaces associated with mobile networks since boot.
     * Interfaces are never removed from this list, so counters should always be
     * monotonic.
     */
    private static java.lang.String[] getMobileIfaces() {
        try {
            return android.net.TrafficStats.getStatsService().getMobileIfaces();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    // NOTE: keep these in sync with android_net_TrafficStats.cpp
    private static final int TYPE_RX_BYTES = 0;

    private static final int TYPE_RX_PACKETS = 1;

    private static final int TYPE_TX_BYTES = 2;

    private static final int TYPE_TX_PACKETS = 3;

    private static final int TYPE_TCP_RX_PACKETS = 4;

    private static final int TYPE_TCP_TX_PACKETS = 5;

    private static native long nativeGetTotalStat(int type);

    private static native long nativeGetIfaceStat(java.lang.String iface, int type);

    private static native long nativeGetUidStat(int uid, int type);
}

