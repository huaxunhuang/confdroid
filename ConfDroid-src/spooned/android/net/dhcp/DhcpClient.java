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
package android.net.dhcp;


/**
 * A DHCPv4 client.
 *
 * Written to behave similarly to the DhcpStateMachine + dhcpcd 5.5.6 combination used in Android
 * 5.1 and below, as configured on Nexus 6. The interface is the same as DhcpStateMachine.
 *
 * TODO:
 *
 * - Exponential backoff when receiving NAKs (not specified by the RFC, but current behaviour).
 * - Support persisting lease state and support INIT-REBOOT. Android 5.1 does this, but it does not
 *   do so correctly: instead of requesting the lease last obtained on a particular network (e.g., a
 *   given SSID), it requests the last-leased IP address on the same interface, causing a delay if
 *   the server NAKs or a timeout if it doesn't.
 *
 * Known differences from current behaviour:
 *
 * - Does not request the "static routes" option.
 * - Does not support BOOTP servers. DHCP has been around since 1993, should be everywhere now.
 * - Requests the "broadcast" option, but does nothing with it.
 * - Rejects invalid subnet masks such as 255.255.255.1 (current code treats that as 255.255.255.0).
 *
 * @unknown 
 */
public class DhcpClient extends com.android.internal.util.StateMachine {
    private static final java.lang.String TAG = "DhcpClient";

    private static final boolean DBG = true;

    private static final boolean STATE_DBG = false;

    private static final boolean MSG_DBG = false;

    private static final boolean PACKET_DBG = false;

    // Timers and timeouts.
    private static final int SECONDS = 1000;

    private static final int FIRST_TIMEOUT_MS = 2 * android.net.dhcp.DhcpClient.SECONDS;

    private static final int MAX_TIMEOUT_MS = 128 * android.net.dhcp.DhcpClient.SECONDS;

    // This is not strictly needed, since the client is asynchronous and implements exponential
    // backoff. It's maintained for backwards compatibility with the previous DHCP code, which was
    // a blocking operation with a 30-second timeout. We pick 36 seconds so we can send packets at
    // t=0, t=2, t=6, t=14, t=30, allowing for 10% jitter.
    private static final int DHCP_TIMEOUT_MS = 36 * android.net.dhcp.DhcpClient.SECONDS;

    private static final int PUBLIC_BASE = com.android.internal.util.Protocol.BASE_DHCP;

    /* Commands from controller to start/stop DHCP */
    public static final int CMD_START_DHCP = android.net.dhcp.DhcpClient.PUBLIC_BASE + 1;

    public static final int CMD_STOP_DHCP = android.net.dhcp.DhcpClient.PUBLIC_BASE + 2;

    /* Notification from DHCP state machine prior to DHCP discovery/renewal */
    public static final int CMD_PRE_DHCP_ACTION = android.net.dhcp.DhcpClient.PUBLIC_BASE + 3;

    /* Notification from DHCP state machine post DHCP discovery/renewal. Indicates
    success/failure
     */
    public static final int CMD_POST_DHCP_ACTION = android.net.dhcp.DhcpClient.PUBLIC_BASE + 4;

    /* Notification from DHCP state machine before quitting */
    public static final int CMD_ON_QUIT = android.net.dhcp.DhcpClient.PUBLIC_BASE + 5;

    /* Command from controller to indicate DHCP discovery/renewal can continue
    after pre DHCP action is complete
     */
    public static final int CMD_PRE_DHCP_ACTION_COMPLETE = android.net.dhcp.DhcpClient.PUBLIC_BASE + 6;

    /* Command and event notification to/from IpManager requesting the setting
    (or clearing) of an IPv4 LinkAddress.
     */
    public static final int CMD_CLEAR_LINKADDRESS = android.net.dhcp.DhcpClient.PUBLIC_BASE + 7;

    public static final int CMD_CONFIGURE_LINKADDRESS = android.net.dhcp.DhcpClient.PUBLIC_BASE + 8;

    public static final int EVENT_LINKADDRESS_CONFIGURED = android.net.dhcp.DhcpClient.PUBLIC_BASE + 9;

    /* Message.arg1 arguments to CMD_POST_DHCP_ACTION notification */
    public static final int DHCP_SUCCESS = 1;

    public static final int DHCP_FAILURE = 2;

    // Internal messages.
    private static final int PRIVATE_BASE = com.android.internal.util.Protocol.BASE_DHCP + 100;

    private static final int CMD_KICK = android.net.dhcp.DhcpClient.PRIVATE_BASE + 1;

    private static final int CMD_RECEIVED_PACKET = android.net.dhcp.DhcpClient.PRIVATE_BASE + 2;

    private static final int CMD_TIMEOUT = android.net.dhcp.DhcpClient.PRIVATE_BASE + 3;

    private static final int CMD_RENEW_DHCP = android.net.dhcp.DhcpClient.PRIVATE_BASE + 4;

    private static final int CMD_REBIND_DHCP = android.net.dhcp.DhcpClient.PRIVATE_BASE + 5;

    private static final int CMD_EXPIRE_DHCP = android.net.dhcp.DhcpClient.PRIVATE_BASE + 6;

    // For message logging.
    private static final java.lang.Class[] sMessageClasses = new java.lang.Class[]{ android.net.dhcp.DhcpClient.class };

    private static final android.util.SparseArray<java.lang.String> sMessageNames = com.android.internal.util.MessageUtils.findMessageNames(android.net.dhcp.DhcpClient.sMessageClasses);

    // DHCP parameters that we request.
    /* package */
    static final byte[] REQUESTED_PARAMS = new byte[]{ android.net.dhcp.DhcpPacket.DHCP_SUBNET_MASK, android.net.dhcp.DhcpPacket.DHCP_ROUTER, android.net.dhcp.DhcpPacket.DHCP_DNS_SERVER, android.net.dhcp.DhcpPacket.DHCP_DOMAIN_NAME, android.net.dhcp.DhcpPacket.DHCP_MTU, android.net.dhcp.DhcpPacket.DHCP_BROADCAST_ADDRESS// TODO: currently ignored.
    , android.net.dhcp.DhcpPacket.DHCP_LEASE_TIME, android.net.dhcp.DhcpPacket.DHCP_RENEWAL_TIME, android.net.dhcp.DhcpPacket.DHCP_REBINDING_TIME, android.net.dhcp.DhcpPacket.DHCP_VENDOR_INFO };

    // DHCP flag that means "yes, we support unicast."
    private static final boolean DO_UNICAST = false;

    // System services / libraries we use.
    private final android.content.Context mContext;

    private final java.util.Random mRandom;

    private final android.net.metrics.IpConnectivityLog mMetricsLog = new android.net.metrics.IpConnectivityLog();

    // Sockets.
    // - We use a packet socket to receive, because servers send us packets bound for IP addresses
    // which we have not yet configured, and the kernel protocol stack drops these.
    // - We use a UDP socket to send, so the kernel handles ARP and routing for us (DHCP servers can
    // be off-link as well as on-link).
    private java.io.FileDescriptor mPacketSock;

    private java.io.FileDescriptor mUdpSock;

    private android.net.dhcp.DhcpClient.ReceiveThread mReceiveThread;

    // State variables.
    private final com.android.internal.util.StateMachine mController;

    private final com.android.internal.util.WakeupMessage mKickAlarm;

    private final com.android.internal.util.WakeupMessage mTimeoutAlarm;

    private final com.android.internal.util.WakeupMessage mRenewAlarm;

    private final com.android.internal.util.WakeupMessage mRebindAlarm;

    private final com.android.internal.util.WakeupMessage mExpiryAlarm;

    private final java.lang.String mIfaceName;

    private boolean mRegisteredForPreDhcpNotification;

    private java.net.NetworkInterface mIface;

    private byte[] mHwAddr;

    private android.system.PacketSocketAddress mInterfaceBroadcastAddr;

    private int mTransactionId;

    private long mTransactionStartMillis;

    private android.net.DhcpResults mDhcpLease;

    private long mDhcpLeaseExpiry;

    private android.net.DhcpResults mOffer;

    // Milliseconds SystemClock timestamps used to record transition times to DhcpBoundState.
    private long mLastInitEnterTime;

    private long mLastBoundExitTime;

    // States.
    private com.android.internal.util.State mStoppedState = new android.net.dhcp.DhcpClient.StoppedState();

    private com.android.internal.util.State mDhcpState = new android.net.dhcp.DhcpClient.DhcpState();

    private com.android.internal.util.State mDhcpInitState = new android.net.dhcp.DhcpClient.DhcpInitState();

    private com.android.internal.util.State mDhcpSelectingState = new android.net.dhcp.DhcpClient.DhcpSelectingState();

    private com.android.internal.util.State mDhcpRequestingState = new android.net.dhcp.DhcpClient.DhcpRequestingState();

    private com.android.internal.util.State mDhcpHaveLeaseState = new android.net.dhcp.DhcpClient.DhcpHaveLeaseState();

    private com.android.internal.util.State mConfiguringInterfaceState = new android.net.dhcp.DhcpClient.ConfiguringInterfaceState();

    private com.android.internal.util.State mDhcpBoundState = new android.net.dhcp.DhcpClient.DhcpBoundState();

    private com.android.internal.util.State mDhcpRenewingState = new android.net.dhcp.DhcpClient.DhcpRenewingState();

    private com.android.internal.util.State mDhcpRebindingState = new android.net.dhcp.DhcpClient.DhcpRebindingState();

    private com.android.internal.util.State mDhcpInitRebootState = new android.net.dhcp.DhcpClient.DhcpInitRebootState();

    private com.android.internal.util.State mDhcpRebootingState = new android.net.dhcp.DhcpClient.DhcpRebootingState();

    private com.android.internal.util.State mWaitBeforeStartState = new android.net.dhcp.DhcpClient.WaitBeforeStartState(mDhcpInitState);

    private com.android.internal.util.State mWaitBeforeRenewalState = new android.net.dhcp.DhcpClient.WaitBeforeRenewalState(mDhcpRenewingState);

    private com.android.internal.util.WakeupMessage makeWakeupMessage(java.lang.String cmdName, int cmd) {
        cmdName = (((android.net.dhcp.DhcpClient.class.getSimpleName() + ".") + mIfaceName) + ".") + cmdName;
        return new com.android.internal.util.WakeupMessage(mContext, getHandler(), cmdName, cmd);
    }

    private DhcpClient(android.content.Context context, com.android.internal.util.StateMachine controller, java.lang.String iface) {
        super(android.net.dhcp.DhcpClient.TAG);
        mContext = context;
        mController = controller;
        mIfaceName = iface;
        addState(mStoppedState);
        addState(mDhcpState);
        addState(mDhcpInitState, mDhcpState);
        addState(mWaitBeforeStartState, mDhcpState);
        addState(mDhcpSelectingState, mDhcpState);
        addState(mDhcpRequestingState, mDhcpState);
        addState(mDhcpHaveLeaseState, mDhcpState);
        addState(mConfiguringInterfaceState, mDhcpHaveLeaseState);
        addState(mDhcpBoundState, mDhcpHaveLeaseState);
        addState(mWaitBeforeRenewalState, mDhcpHaveLeaseState);
        addState(mDhcpRenewingState, mDhcpHaveLeaseState);
        addState(mDhcpRebindingState, mDhcpHaveLeaseState);
        addState(mDhcpInitRebootState, mDhcpState);
        addState(mDhcpRebootingState, mDhcpState);
        setInitialState(mStoppedState);
        mRandom = new java.util.Random();
        // Used to schedule packet retransmissions.
        mKickAlarm = makeWakeupMessage("KICK", android.net.dhcp.DhcpClient.CMD_KICK);
        // Used to time out PacketRetransmittingStates.
        mTimeoutAlarm = makeWakeupMessage("TIMEOUT", android.net.dhcp.DhcpClient.CMD_TIMEOUT);
        // Used to schedule DHCP reacquisition.
        mRenewAlarm = makeWakeupMessage("RENEW", android.net.dhcp.DhcpClient.CMD_RENEW_DHCP);
        mRebindAlarm = makeWakeupMessage("REBIND", android.net.dhcp.DhcpClient.CMD_REBIND_DHCP);
        mExpiryAlarm = makeWakeupMessage("EXPIRY", android.net.dhcp.DhcpClient.CMD_EXPIRE_DHCP);
    }

    public void registerForPreDhcpNotification() {
        mRegisteredForPreDhcpNotification = true;
    }

    public static android.net.dhcp.DhcpClient makeDhcpClient(android.content.Context context, com.android.internal.util.StateMachine controller, java.lang.String intf) {
        android.net.dhcp.DhcpClient client = new android.net.dhcp.DhcpClient(context, controller, intf);
        client.start();
        return client;
    }

    private boolean initInterface() {
        try {
            mIface = java.net.NetworkInterface.getByName(mIfaceName);
            mHwAddr = mIface.getHardwareAddress();
            mInterfaceBroadcastAddr = new android.system.PacketSocketAddress(mIface.getIndex(), android.net.dhcp.DhcpPacket.ETHER_BROADCAST);
            return true;
        } catch (java.net.SocketException | java.lang.NullPointerException e) {
            android.util.Log.e(android.net.dhcp.DhcpClient.TAG, "Can't determine ifindex or MAC address for " + mIfaceName, e);
            return false;
        }
    }

    private void startNewTransaction() {
        mTransactionId = mRandom.nextInt();
        mTransactionStartMillis = android.os.SystemClock.elapsedRealtime();
    }

    private boolean initSockets() {
        return initPacketSocket() && initUdpSocket();
    }

    private boolean initPacketSocket() {
        try {
            mPacketSock = android.system.Os.socket(android.system.OsConstants.AF_PACKET, android.system.OsConstants.SOCK_RAW, android.system.OsConstants.ETH_P_IP);
            android.system.PacketSocketAddress addr = new android.system.PacketSocketAddress(((short) (android.system.OsConstants.ETH_P_IP)), mIface.getIndex());
            android.system.Os.bind(mPacketSock, addr);
            android.net.NetworkUtils.attachDhcpFilter(mPacketSock);
        } catch (java.net.SocketException | android.system.ErrnoException e) {
            android.util.Log.e(android.net.dhcp.DhcpClient.TAG, "Error creating packet socket", e);
            return false;
        }
        return true;
    }

    private boolean initUdpSocket() {
        try {
            mUdpSock = android.system.Os.socket(android.system.OsConstants.AF_INET, android.system.OsConstants.SOCK_DGRAM, android.system.OsConstants.IPPROTO_UDP);
            android.system.Os.setsockoptInt(mUdpSock, android.system.OsConstants.SOL_SOCKET, android.system.OsConstants.SO_REUSEADDR, 1);
            android.system.Os.setsockoptIfreq(mUdpSock, android.system.OsConstants.SOL_SOCKET, android.system.OsConstants.SO_BINDTODEVICE, mIfaceName);
            android.system.Os.setsockoptInt(mUdpSock, android.system.OsConstants.SOL_SOCKET, android.system.OsConstants.SO_BROADCAST, 1);
            android.system.Os.setsockoptInt(mUdpSock, android.system.OsConstants.SOL_SOCKET, android.system.OsConstants.SO_RCVBUF, 0);
            android.system.Os.bind(mUdpSock, ANY, android.net.dhcp.DhcpPacket.DHCP_CLIENT);
            android.net.NetworkUtils.protectFromVpn(mUdpSock);
        } catch (java.net.SocketException | android.system.ErrnoException e) {
            android.util.Log.e(android.net.dhcp.DhcpClient.TAG, "Error creating UDP socket", e);
            return false;
        }
        return true;
    }

    private boolean connectUdpSock(java.net.Inet4Address to) {
        try {
            android.system.Os.connect(mUdpSock, to, android.net.dhcp.DhcpPacket.DHCP_SERVER);
            return true;
        } catch (java.net.SocketException | android.system.ErrnoException e) {
            android.util.Log.e(android.net.dhcp.DhcpClient.TAG, "Error connecting UDP socket", e);
            return false;
        }
    }

    private static void closeQuietly(java.io.FileDescriptor fd) {
        try {
            libcore.io.IoBridge.closeAndSignalBlockedThreads(fd);
        } catch (java.io.IOException ignored) {
        }
    }

    private void closeSockets() {
        android.net.dhcp.DhcpClient.closeQuietly(mUdpSock);
        android.net.dhcp.DhcpClient.closeQuietly(mPacketSock);
    }

    class ReceiveThread extends java.lang.Thread {
        private final byte[] mPacket = new byte[android.net.dhcp.DhcpPacket.MAX_LENGTH];

        private volatile boolean mStopped = false;

        public void halt() {
            mStopped = true;
            closeSockets();// Interrupts the read() call the thread is blocked in.

        }

        @java.lang.Override
        public void run() {
            if (android.net.dhcp.DhcpClient.DBG)
                android.util.Log.d(android.net.dhcp.DhcpClient.TAG, "Receive thread started");

            while (!mStopped) {
                int length = 0;// Or compiler can't tell it's initialized if a parse error occurs.

                try {
                    length = android.system.Os.read(mPacketSock, mPacket, 0, mPacket.length);
                    android.net.dhcp.DhcpPacket packet = null;
                    packet = android.net.dhcp.DhcpPacket.decodeFullPacket(mPacket, length, android.net.dhcp.DhcpPacket.ENCAP_L2);
                    if (android.net.dhcp.DhcpClient.DBG)
                        android.util.Log.d(android.net.dhcp.DhcpClient.TAG, "Received packet: " + packet);

                    sendMessage(android.net.dhcp.DhcpClient.CMD_RECEIVED_PACKET, packet);
                } catch (java.io.IOException | android.system.ErrnoException e) {
                    if (!mStopped) {
                        android.util.Log.e(android.net.dhcp.DhcpClient.TAG, "Read error", e);
                        logError(android.net.metrics.DhcpErrorEvent.RECEIVE_ERROR);
                    }
                } catch (android.net.dhcp.DhcpPacket.ParseException e) {
                    android.util.Log.e(android.net.dhcp.DhcpClient.TAG, "Can't parse packet: " + e.getMessage());
                    if (android.net.dhcp.DhcpClient.PACKET_DBG) {
                        android.util.Log.d(android.net.dhcp.DhcpClient.TAG, com.android.internal.util.HexDump.dumpHexString(mPacket, 0, length));
                    }
                    logError(e.errorCode);
                }
            } 
            if (android.net.dhcp.DhcpClient.DBG)
                android.util.Log.d(android.net.dhcp.DhcpClient.TAG, "Receive thread stopped");

        }
    }

    private short getSecs() {
        return ((short) ((android.os.SystemClock.elapsedRealtime() - mTransactionStartMillis) / 1000));
    }

    private boolean transmitPacket(java.nio.ByteBuffer buf, java.lang.String description, int encap, java.net.Inet4Address to) {
        try {
            if (encap == android.net.dhcp.DhcpPacket.ENCAP_L2) {
                if (android.net.dhcp.DhcpClient.DBG)
                    android.util.Log.d(android.net.dhcp.DhcpClient.TAG, "Broadcasting " + description);

                android.system.Os.sendto(mPacketSock, buf.array(), 0, buf.limit(), 0, mInterfaceBroadcastAddr);
            } else
                if ((encap == android.net.dhcp.DhcpPacket.ENCAP_BOOTP) && to.equals(android.net.dhcp.DhcpPacket.INADDR_BROADCAST)) {
                    if (android.net.dhcp.DhcpClient.DBG)
                        android.util.Log.d(android.net.dhcp.DhcpClient.TAG, "Broadcasting " + description);

                    // We only send L3-encapped broadcasts in DhcpRebindingState,
                    // where we have an IP address and an unconnected UDP socket.
                    // 
                    // N.B.: We only need this codepath because DhcpRequestPacket
                    // hardcodes the source IP address to 0.0.0.0. We could reuse
                    // the packet socket if this ever changes.
                    android.system.Os.sendto(mUdpSock, buf, 0, to, android.net.dhcp.DhcpPacket.DHCP_SERVER);
                } else {
                    // It's safe to call getpeername here, because we only send unicast packets if we
                    // have an IP address, and we connect the UDP socket in DhcpBoundState#enter.
                    if (android.net.dhcp.DhcpClient.DBG)
                        android.util.Log.d(android.net.dhcp.DhcpClient.TAG, java.lang.String.format("Unicasting %s to %s", description, android.system.Os.getpeername(mUdpSock)));

                    android.system.Os.write(mUdpSock, buf);
                }

        } catch (android.system.ErrnoException | java.io.IOException e) {
            android.util.Log.e(android.net.dhcp.DhcpClient.TAG, "Can't send packet: ", e);
            return false;
        }
        return true;
    }

    private boolean sendDiscoverPacket() {
        java.nio.ByteBuffer packet = android.net.dhcp.DhcpPacket.buildDiscoverPacket(android.net.dhcp.DhcpPacket.ENCAP_L2, mTransactionId, getSecs(), mHwAddr, android.net.dhcp.DhcpClient.DO_UNICAST, android.net.dhcp.DhcpClient.REQUESTED_PARAMS);
        return transmitPacket(packet, "DHCPDISCOVER", android.net.dhcp.DhcpPacket.ENCAP_L2, android.net.dhcp.DhcpPacket.INADDR_BROADCAST);
    }

    private boolean sendRequestPacket(java.net.Inet4Address clientAddress, java.net.Inet4Address requestedAddress, java.net.Inet4Address serverAddress, java.net.Inet4Address to) {
        // TODO: should we use the transaction ID from the server?
        final int encap = (android.net.dhcp.DhcpPacket.INADDR_ANY.equals(clientAddress)) ? android.net.dhcp.DhcpPacket.ENCAP_L2 : android.net.dhcp.DhcpPacket.ENCAP_BOOTP;
        java.nio.ByteBuffer packet = android.net.dhcp.DhcpPacket.buildRequestPacket(encap, mTransactionId, getSecs(), clientAddress, android.net.dhcp.DhcpClient.DO_UNICAST, mHwAddr, requestedAddress, serverAddress, android.net.dhcp.DhcpClient.REQUESTED_PARAMS, null);
        java.lang.String serverStr = (serverAddress != null) ? serverAddress.getHostAddress() : null;
        java.lang.String description = (((("DHCPREQUEST ciaddr=" + clientAddress.getHostAddress()) + " request=") + requestedAddress.getHostAddress()) + " serverid=") + serverStr;
        return transmitPacket(packet, description, encap, to);
    }

    private void scheduleLeaseTimers() {
        if (mDhcpLeaseExpiry == 0) {
            android.util.Log.d(android.net.dhcp.DhcpClient.TAG, "Infinite lease, no timer scheduling needed");
            return;
        }
        final long now = android.os.SystemClock.elapsedRealtime();
        // TODO: consider getting the renew and rebind timers from T1 and T2.
        // See also:
        // https://tools.ietf.org/html/rfc2131#section-4.4.5
        // https://tools.ietf.org/html/rfc1533#section-9.9
        // https://tools.ietf.org/html/rfc1533#section-9.10
        final long remainingDelay = mDhcpLeaseExpiry - now;
        final long renewDelay = remainingDelay / 2;
        final long rebindDelay = (remainingDelay * 7) / 8;
        mRenewAlarm.schedule(now + renewDelay);
        mRebindAlarm.schedule(now + rebindDelay);
        mExpiryAlarm.schedule(now + remainingDelay);
        android.util.Log.d(android.net.dhcp.DhcpClient.TAG, ("Scheduling renewal in " + (renewDelay / 1000)) + "s");
        android.util.Log.d(android.net.dhcp.DhcpClient.TAG, ("Scheduling rebind in " + (rebindDelay / 1000)) + "s");
        android.util.Log.d(android.net.dhcp.DhcpClient.TAG, ("Scheduling expiry in " + (remainingDelay / 1000)) + "s");
    }

    private void notifySuccess() {
        mController.sendMessage(android.net.dhcp.DhcpClient.CMD_POST_DHCP_ACTION, android.net.dhcp.DhcpClient.DHCP_SUCCESS, 0, new android.net.DhcpResults(mDhcpLease));
    }

    private void notifyFailure() {
        mController.sendMessage(android.net.dhcp.DhcpClient.CMD_POST_DHCP_ACTION, android.net.dhcp.DhcpClient.DHCP_FAILURE, 0, null);
    }

    private void acceptDhcpResults(android.net.DhcpResults results, java.lang.String msg) {
        mDhcpLease = results;
        mOffer = null;
        android.util.Log.d(android.net.dhcp.DhcpClient.TAG, (msg + " lease: ") + mDhcpLease);
        notifySuccess();
    }

    private void clearDhcpState() {
        mDhcpLease = null;
        mDhcpLeaseExpiry = 0;
        mOffer = null;
    }

    /**
     * Quit the DhcpStateMachine.
     *
     * @unknown 
     */
    public void doQuit() {
        android.util.Log.d(android.net.dhcp.DhcpClient.TAG, "doQuit");
        quit();
    }

    @java.lang.Override
    protected void onQuitting() {
        android.util.Log.d(android.net.dhcp.DhcpClient.TAG, "onQuitting");
        mController.sendMessage(android.net.dhcp.DhcpClient.CMD_ON_QUIT);
    }

    abstract class LoggingState extends com.android.internal.util.State {
        private long mEnterTimeMs;

        @java.lang.Override
        public void enter() {
            if (android.net.dhcp.DhcpClient.STATE_DBG)
                android.util.Log.d(android.net.dhcp.DhcpClient.TAG, "Entering state " + getName());

            mEnterTimeMs = android.os.SystemClock.elapsedRealtime();
        }

        @java.lang.Override
        public void exit() {
            long durationMs = android.os.SystemClock.elapsedRealtime() - mEnterTimeMs;
            logState(getName(), ((int) (durationMs)));
        }

        private java.lang.String messageName(int what) {
            return android.net.dhcp.DhcpClient.sMessageNames.get(what, java.lang.Integer.toString(what));
        }

        private java.lang.String messageToString(android.os.Message message) {
            long now = android.os.SystemClock.uptimeMillis();
            java.lang.StringBuilder b = new java.lang.StringBuilder(" ");
            android.util.TimeUtils.formatDuration(message.getWhen() - now, b);
            b.append(" ").append(messageName(message.what)).append(" ").append(message.arg1).append(" ").append(message.arg2).append(" ").append(message.obj);
            return b.toString();
        }

        @java.lang.Override
        public boolean processMessage(android.os.Message message) {
            if (android.net.dhcp.DhcpClient.MSG_DBG) {
                android.util.Log.d(android.net.dhcp.DhcpClient.TAG, getName() + messageToString(message));
            }
            return NOT_HANDLED;
        }

        @java.lang.Override
        public java.lang.String getName() {
            // All DhcpClient's states are inner classes with a well defined name.
            // Use getSimpleName() and avoid super's getName() creating new String instances.
            return getClass().getSimpleName();
        }
    }

    // Sends CMD_PRE_DHCP_ACTION to the controller, waits for the controller to respond with
    // CMD_PRE_DHCP_ACTION_COMPLETE, and then transitions to mOtherState.
    abstract class WaitBeforeOtherState extends android.net.dhcp.DhcpClient.LoggingState {
        protected com.android.internal.util.State mOtherState;

        @java.lang.Override
        public void enter() {
            super.enter();
            mController.sendMessage(android.net.dhcp.DhcpClient.CMD_PRE_DHCP_ACTION);
        }

        @java.lang.Override
        public boolean processMessage(android.os.Message message) {
            super.processMessage(message);
            switch (message.what) {
                case android.net.dhcp.DhcpClient.CMD_PRE_DHCP_ACTION_COMPLETE :
                    transitionTo(mOtherState);
                    return HANDLED;
                default :
                    return NOT_HANDLED;
            }
        }
    }

    class StoppedState extends com.android.internal.util.State {
        @java.lang.Override
        public boolean processMessage(android.os.Message message) {
            switch (message.what) {
                case android.net.dhcp.DhcpClient.CMD_START_DHCP :
                    if (mRegisteredForPreDhcpNotification) {
                        transitionTo(mWaitBeforeStartState);
                    } else {
                        transitionTo(mDhcpInitState);
                    }
                    return HANDLED;
                default :
                    return NOT_HANDLED;
            }
        }
    }

    class WaitBeforeStartState extends android.net.dhcp.DhcpClient.WaitBeforeOtherState {
        public WaitBeforeStartState(com.android.internal.util.State otherState) {
            super();
            mOtherState = otherState;
        }
    }

    class WaitBeforeRenewalState extends android.net.dhcp.DhcpClient.WaitBeforeOtherState {
        public WaitBeforeRenewalState(com.android.internal.util.State otherState) {
            super();
            mOtherState = otherState;
        }
    }

    class DhcpState extends com.android.internal.util.State {
        @java.lang.Override
        public void enter() {
            clearDhcpState();
            if (initInterface() && initSockets()) {
                mReceiveThread = new android.net.dhcp.DhcpClient.ReceiveThread();
                mReceiveThread.start();
            } else {
                notifyFailure();
                transitionTo(mStoppedState);
            }
        }

        @java.lang.Override
        public void exit() {
            if (mReceiveThread != null) {
                mReceiveThread.halt();// Also closes sockets.

                mReceiveThread = null;
            }
            clearDhcpState();
        }

        @java.lang.Override
        public boolean processMessage(android.os.Message message) {
            processMessage(message);
            switch (message.what) {
                case android.net.dhcp.DhcpClient.CMD_STOP_DHCP :
                    transitionTo(mStoppedState);
                    return HANDLED;
                default :
                    return NOT_HANDLED;
            }
        }
    }

    public boolean isValidPacket(android.net.dhcp.DhcpPacket packet) {
        // TODO: check checksum.
        int xid = packet.getTransactionId();
        if (xid != mTransactionId) {
            android.util.Log.d(android.net.dhcp.DhcpClient.TAG, (("Unexpected transaction ID " + xid) + ", expected ") + mTransactionId);
            return false;
        }
        if (!java.util.Arrays.equals(packet.getClientMac(), mHwAddr)) {
            android.util.Log.d(android.net.dhcp.DhcpClient.TAG, (("MAC addr mismatch: got " + com.android.internal.util.HexDump.toHexString(packet.getClientMac())) + ", expected ") + com.android.internal.util.HexDump.toHexString(packet.getClientMac()));
            return false;
        }
        return true;
    }

    public void setDhcpLeaseExpiry(android.net.dhcp.DhcpPacket packet) {
        long leaseTimeMillis = packet.getLeaseTimeMillis();
        mDhcpLeaseExpiry = (leaseTimeMillis > 0) ? android.os.SystemClock.elapsedRealtime() + leaseTimeMillis : 0;
    }

    /**
     * Retransmits packets using jittered exponential backoff with an optional timeout. Packet
     * transmission is triggered by CMD_KICK, which is sent by an AlarmManager alarm. If a subclass
     * sets mTimeout to a positive value, then timeout() is called by an AlarmManager alarm mTimeout
     * milliseconds after entering the state. Kicks and timeouts are cancelled when leaving the
     * state.
     *
     * Concrete subclasses must implement sendPacket, which is called when the alarm fires and a
     * packet needs to be transmitted, and receivePacket, which is triggered by CMD_RECEIVED_PACKET
     * sent by the receive thread. They may also set mTimeout and implement timeout.
     */
    abstract class PacketRetransmittingState extends android.net.dhcp.DhcpClient.LoggingState {
        private int mTimer;

        protected int mTimeout = 0;

        @java.lang.Override
        public void enter() {
            super.enter();
            initTimer();
            maybeInitTimeout();
            sendMessage(android.net.dhcp.DhcpClient.CMD_KICK);
        }

        @java.lang.Override
        public boolean processMessage(android.os.Message message) {
            super.processMessage(message);
            switch (message.what) {
                case android.net.dhcp.DhcpClient.CMD_KICK :
                    sendPacket();
                    scheduleKick();
                    return HANDLED;
                case android.net.dhcp.DhcpClient.CMD_RECEIVED_PACKET :
                    receivePacket(((android.net.dhcp.DhcpPacket) (message.obj)));
                    return HANDLED;
                case android.net.dhcp.DhcpClient.CMD_TIMEOUT :
                    timeout();
                    return HANDLED;
                default :
                    return NOT_HANDLED;
            }
        }

        @java.lang.Override
        public void exit() {
            super.exit();
            mKickAlarm.cancel();
            mTimeoutAlarm.cancel();
        }

        protected abstract boolean sendPacket();

        protected abstract void receivePacket(android.net.dhcp.DhcpPacket packet);

        protected void timeout() {
        }

        protected void initTimer() {
            mTimer = android.net.dhcp.DhcpClient.FIRST_TIMEOUT_MS;
        }

        protected int jitterTimer(int baseTimer) {
            int maxJitter = baseTimer / 10;
            int jitter = mRandom.nextInt(2 * maxJitter) - maxJitter;
            return baseTimer + jitter;
        }

        protected void scheduleKick() {
            long now = android.os.SystemClock.elapsedRealtime();
            long timeout = jitterTimer(mTimer);
            long alarmTime = now + timeout;
            mKickAlarm.schedule(alarmTime);
            mTimer *= 2;
            if (mTimer > android.net.dhcp.DhcpClient.MAX_TIMEOUT_MS) {
                mTimer = android.net.dhcp.DhcpClient.MAX_TIMEOUT_MS;
            }
        }

        protected void maybeInitTimeout() {
            if (mTimeout > 0) {
                long alarmTime = android.os.SystemClock.elapsedRealtime() + mTimeout;
                mTimeoutAlarm.schedule(alarmTime);
            }
        }
    }

    class DhcpInitState extends android.net.dhcp.DhcpClient.PacketRetransmittingState {
        public DhcpInitState() {
            super();
        }

        @java.lang.Override
        public void enter() {
            super.enter();
            startNewTransaction();
            mLastInitEnterTime = android.os.SystemClock.elapsedRealtime();
        }

        protected boolean sendPacket() {
            return sendDiscoverPacket();
        }

        protected void receivePacket(android.net.dhcp.DhcpPacket packet) {
            if (!isValidPacket(packet))
                return;

            if (!(packet instanceof android.net.dhcp.DhcpOfferPacket))
                return;

            mOffer = packet.toDhcpResults();
            if (mOffer != null) {
                android.util.Log.d(android.net.dhcp.DhcpClient.TAG, "Got pending lease: " + mOffer);
                transitionTo(mDhcpRequestingState);
            }
        }
    }

    // Not implemented. We request the first offer we receive.
    class DhcpSelectingState extends android.net.dhcp.DhcpClient.LoggingState {}

    class DhcpRequestingState extends android.net.dhcp.DhcpClient.PacketRetransmittingState {
        public DhcpRequestingState() {
            mTimeout = android.net.dhcp.DhcpClient.DHCP_TIMEOUT_MS / 2;
        }

        protected boolean sendPacket() {
            return // ciaddr
            // DHCP_REQUESTED_IP
            // DHCP_SERVER_IDENTIFIER
            sendRequestPacket(android.net.dhcp.DhcpPacket.INADDR_ANY, ((java.net.Inet4Address) (mOffer.ipAddress.getAddress())), ((java.net.Inet4Address) (mOffer.serverAddress)), android.net.dhcp.DhcpPacket.INADDR_BROADCAST);// packet destination address

        }

        protected void receivePacket(android.net.dhcp.DhcpPacket packet) {
            if (!isValidPacket(packet))
                return;

            if (packet instanceof android.net.dhcp.DhcpAckPacket) {
                android.net.DhcpResults results = packet.toDhcpResults();
                if (results != null) {
                    setDhcpLeaseExpiry(packet);
                    acceptDhcpResults(results, "Confirmed");
                    transitionTo(mConfiguringInterfaceState);
                }
            } else
                if (packet instanceof android.net.dhcp.DhcpNakPacket) {
                    // TODO: Wait a while before returning into INIT state.
                    android.util.Log.d(android.net.dhcp.DhcpClient.TAG, "Received NAK, returning to INIT");
                    mOffer = null;
                    transitionTo(mDhcpInitState);
                }

        }

        @java.lang.Override
        protected void timeout() {
            // After sending REQUESTs unsuccessfully for a while, go back to init.
            transitionTo(mDhcpInitState);
        }
    }

    class DhcpHaveLeaseState extends com.android.internal.util.State {
        @java.lang.Override
        public boolean processMessage(android.os.Message message) {
            switch (message.what) {
                case android.net.dhcp.DhcpClient.CMD_EXPIRE_DHCP :
                    android.util.Log.d(android.net.dhcp.DhcpClient.TAG, "Lease expired!");
                    notifyFailure();
                    transitionTo(mDhcpInitState);
                    return HANDLED;
                default :
                    return NOT_HANDLED;
            }
        }

        @java.lang.Override
        public void exit() {
            // Clear any extant alarms.
            mRenewAlarm.cancel();
            mRebindAlarm.cancel();
            mExpiryAlarm.cancel();
            clearDhcpState();
            // Tell IpManager to clear the IPv4 address. There is no need to
            // wait for confirmation since any subsequent packets are sent from
            // INADDR_ANY anyway (DISCOVER, REQUEST).
            mController.sendMessage(android.net.dhcp.DhcpClient.CMD_CLEAR_LINKADDRESS);
        }
    }

    class ConfiguringInterfaceState extends android.net.dhcp.DhcpClient.LoggingState {
        @java.lang.Override
        public void enter() {
            super.enter();
            mController.sendMessage(android.net.dhcp.DhcpClient.CMD_CONFIGURE_LINKADDRESS, mDhcpLease.ipAddress);
        }

        @java.lang.Override
        public boolean processMessage(android.os.Message message) {
            super.processMessage(message);
            switch (message.what) {
                case android.net.dhcp.DhcpClient.EVENT_LINKADDRESS_CONFIGURED :
                    transitionTo(mDhcpBoundState);
                    return HANDLED;
                default :
                    return NOT_HANDLED;
            }
        }
    }

    class DhcpBoundState extends android.net.dhcp.DhcpClient.LoggingState {
        @java.lang.Override
        public void enter() {
            super.enter();
            if ((mDhcpLease.serverAddress != null) && (!connectUdpSock(mDhcpLease.serverAddress))) {
                // There's likely no point in going into DhcpInitState here, we'll probably
                // just repeat the transaction, get the same IP address as before, and fail.
                // 
                // NOTE: It is observed that connectUdpSock() basically never fails, due to
                // SO_BINDTODEVICE. Examining the local socket address shows it will happily
                // return an IPv4 address from another interface, or even return "0.0.0.0".
                // 
                // TODO: Consider deleting this check, following testing on several kernels.
                notifyFailure();
                transitionTo(mStoppedState);
            }
            scheduleLeaseTimers();
            logTimeToBoundState();
        }

        @java.lang.Override
        public void exit() {
            super.exit();
            mLastBoundExitTime = android.os.SystemClock.elapsedRealtime();
        }

        @java.lang.Override
        public boolean processMessage(android.os.Message message) {
            super.processMessage(message);
            switch (message.what) {
                case android.net.dhcp.DhcpClient.CMD_RENEW_DHCP :
                    if (mRegisteredForPreDhcpNotification) {
                        transitionTo(mWaitBeforeRenewalState);
                    } else {
                        transitionTo(mDhcpRenewingState);
                    }
                    return HANDLED;
                default :
                    return NOT_HANDLED;
            }
        }

        private void logTimeToBoundState() {
            long now = android.os.SystemClock.elapsedRealtime();
            if (mLastBoundExitTime > mLastInitEnterTime) {
                logState(android.net.metrics.DhcpClientEvent.RENEWING_BOUND, ((int) (now - mLastBoundExitTime)));
            } else {
                logState(android.net.metrics.DhcpClientEvent.INITIAL_BOUND, ((int) (now - mLastInitEnterTime)));
            }
        }
    }

    abstract class DhcpReacquiringState extends android.net.dhcp.DhcpClient.PacketRetransmittingState {
        protected java.lang.String mLeaseMsg;

        @java.lang.Override
        public void enter() {
            super.enter();
            startNewTransaction();
        }

        protected abstract java.net.Inet4Address packetDestination();

        protected boolean sendPacket() {
            return // ciaddr
            // DHCP_REQUESTED_IP
            // DHCP_SERVER_IDENTIFIER
            sendRequestPacket(((java.net.Inet4Address) (mDhcpLease.ipAddress.getAddress())), android.net.dhcp.DhcpPacket.INADDR_ANY, null, packetDestination());// packet destination address

        }

        protected void receivePacket(android.net.dhcp.DhcpPacket packet) {
            if (!isValidPacket(packet))
                return;

            if (packet instanceof android.net.dhcp.DhcpAckPacket) {
                final android.net.DhcpResults results = packet.toDhcpResults();
                if (results != null) {
                    if (!mDhcpLease.ipAddress.equals(results.ipAddress)) {
                        android.util.Log.d(android.net.dhcp.DhcpClient.TAG, "Renewed lease not for our current IP address!");
                        notifyFailure();
                        transitionTo(mDhcpInitState);
                    }
                    setDhcpLeaseExpiry(packet);
                    // Updating our notion of DhcpResults here only causes the
                    // DNS servers and routes to be updated in LinkProperties
                    // in IpManager and by any overridden relevant handlers of
                    // the registered IpManager.Callback.  IP address changes
                    // are not supported here.
                    acceptDhcpResults(results, mLeaseMsg);
                    transitionTo(mDhcpBoundState);
                }
            } else
                if (packet instanceof android.net.dhcp.DhcpNakPacket) {
                    android.util.Log.d(android.net.dhcp.DhcpClient.TAG, "Received NAK, returning to INIT");
                    notifyFailure();
                    transitionTo(mDhcpInitState);
                }

        }
    }

    class DhcpRenewingState extends android.net.dhcp.DhcpClient.DhcpReacquiringState {
        public DhcpRenewingState() {
            mLeaseMsg = "Renewed";
        }

        @java.lang.Override
        public boolean processMessage(android.os.Message message) {
            if (super.processMessage(message) == HANDLED) {
                return HANDLED;
            }
            switch (message.what) {
                case android.net.dhcp.DhcpClient.CMD_REBIND_DHCP :
                    transitionTo(mDhcpRebindingState);
                    return HANDLED;
                default :
                    return NOT_HANDLED;
            }
        }

        @java.lang.Override
        protected java.net.Inet4Address packetDestination() {
            // Not specifying a SERVER_IDENTIFIER option is a violation of RFC 2131, but...
            // http://b/25343517 . Try to make things work anyway by using broadcast renews.
            return mDhcpLease.serverAddress != null ? mDhcpLease.serverAddress : android.net.dhcp.DhcpPacket.INADDR_BROADCAST;
        }
    }

    class DhcpRebindingState extends android.net.dhcp.DhcpClient.DhcpReacquiringState {
        public DhcpRebindingState() {
            mLeaseMsg = "Rebound";
        }

        @java.lang.Override
        public void enter() {
            super.enter();
            // We need to broadcast and possibly reconnect the socket to a
            // completely different server.
            android.net.dhcp.DhcpClient.closeQuietly(mUdpSock);
            if (!initUdpSocket()) {
                android.util.Log.e(android.net.dhcp.DhcpClient.TAG, "Failed to recreate UDP socket");
                transitionTo(mDhcpInitState);
            }
        }

        @java.lang.Override
        protected java.net.Inet4Address packetDestination() {
            return android.net.dhcp.DhcpPacket.INADDR_BROADCAST;
        }
    }

    class DhcpInitRebootState extends android.net.dhcp.DhcpClient.LoggingState {}

    class DhcpRebootingState extends android.net.dhcp.DhcpClient.LoggingState {}

    private void logError(int errorCode) {
        mMetricsLog.log(new android.net.metrics.DhcpErrorEvent(mIfaceName, errorCode));
    }

    private void logState(java.lang.String name, int durationMs) {
        mMetricsLog.log(new android.net.metrics.DhcpClientEvent(mIfaceName, name, durationMs));
    }
}

