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
 * IpManager
 *
 * This class provides the interface to IP-layer provisioning and maintenance
 * functionality that can be used by transport layers like Wi-Fi, Ethernet,
 * et cetera.
 *
 * [ Lifetime ]
 * IpManager is designed to be instantiated as soon as the interface name is
 * known and can be as long-lived as the class containing it (i.e. declaring
 * it "private final" is okay).
 *
 * @unknown 
 */
public class IpManager extends com.android.internal.util.StateMachine {
    private static final boolean DBG = false;

    private static final boolean VDBG = false;

    // For message logging.
    private static final java.lang.Class[] sMessageClasses = new java.lang.Class[]{ android.net.ip.IpManager.class, android.net.dhcp.DhcpClient.class };

    private static final android.util.SparseArray<java.lang.String> sWhatToString = com.android.internal.util.MessageUtils.findMessageNames(android.net.ip.IpManager.sMessageClasses);

    /**
     * Callbacks for handling IpManager events.
     */
    public static class Callback {
        // In order to receive onPreDhcpAction(), call #withPreDhcpAction()
        // when constructing a ProvisioningConfiguration.
        // 
        // Implementations of onPreDhcpAction() must call
        // IpManager#completedPreDhcpAction() to indicate that DHCP is clear
        // to proceed.
        public void onPreDhcpAction() {
        }

        public void onPostDhcpAction() {
        }

        // This is purely advisory and not an indication of provisioning
        // success or failure.  This is only here for callers that want to
        // expose DHCPv4 results to other APIs (e.g., WifiInfo#setInetAddress).
        // DHCPv4 or static IPv4 configuration failure or success can be
        // determined by whether or not the passed-in DhcpResults object is
        // null or not.
        public void onNewDhcpResults(android.net.DhcpResults dhcpResults) {
        }

        public void onProvisioningSuccess(android.net.LinkProperties newLp) {
        }

        public void onProvisioningFailure(android.net.LinkProperties newLp) {
        }

        // Invoked on LinkProperties changes.
        public void onLinkPropertiesChange(android.net.LinkProperties newLp) {
        }

        // Called when the internal IpReachabilityMonitor (if enabled) has
        // detected the loss of a critical number of required neighbors.
        public void onReachabilityLost(java.lang.String logMsg) {
        }

        // Called when the IpManager state machine terminates.
        public void onQuit() {
        }

        // Install an APF program to filter incoming packets.
        public void installPacketFilter(byte[] filter) {
        }

        // If multicast filtering cannot be accomplished with APF, this function will be called to
        // actuate multicast filtering using another means.
        public void setFallbackMulticastFilter(boolean enabled) {
        }

        // Enabled/disable Neighbor Discover offload functionality. This is
        // called, for example, whenever 464xlat is being started or stopped.
        public void setNeighborDiscoveryOffload(boolean enable) {
        }
    }

    public static class WaitForProvisioningCallback extends android.net.ip.IpManager.Callback {
        private android.net.LinkProperties mCallbackLinkProperties;

        public android.net.LinkProperties waitForProvisioning() {
            synchronized(this) {
                try {
                    wait();
                } catch (java.lang.InterruptedException e) {
                }
                return mCallbackLinkProperties;
            }
        }

        @java.lang.Override
        public void onProvisioningSuccess(android.net.LinkProperties newLp) {
            synchronized(this) {
                mCallbackLinkProperties = newLp;
                notify();
            }
        }

        @java.lang.Override
        public void onProvisioningFailure(android.net.LinkProperties newLp) {
            synchronized(this) {
                mCallbackLinkProperties = null;
                notify();
            }
        }
    }

    // Use a wrapper class to log in order to ensure complete and detailed
    // logging. This method is lighter weight than annotations/reflection
    // and has the following benefits:
    // 
    // - No invoked method can be forgotten.
    // Any new method added to IpManager.Callback must be overridden
    // here or it will never be called.
    // 
    // - No invoking call site can be forgotten.
    // Centralized logging in this way means call sites don't need to
    // remember to log, and therefore no call site can be forgotten.
    // 
    // - No variation in log format among call sites.
    // Encourages logging of any available arguments, and all call sites
    // are necessarily logged identically.
    // 
    // TODO: Find an lighter weight approach.
    private class LoggingCallbackWrapper extends android.net.ip.IpManager.Callback {
        private static final java.lang.String PREFIX = "INVOKE ";

        private android.net.ip.IpManager.Callback mCallback;

        public LoggingCallbackWrapper(android.net.ip.IpManager.Callback callback) {
            mCallback = callback;
        }

        private void log(java.lang.String msg) {
            mLocalLog.log(android.net.ip.IpManager.LoggingCallbackWrapper.PREFIX + msg);
        }

        @java.lang.Override
        public void onPreDhcpAction() {
            mCallback.onPreDhcpAction();
            log("onPreDhcpAction()");
        }

        @java.lang.Override
        public void onPostDhcpAction() {
            mCallback.onPostDhcpAction();
            log("onPostDhcpAction()");
        }

        @java.lang.Override
        public void onNewDhcpResults(android.net.DhcpResults dhcpResults) {
            mCallback.onNewDhcpResults(dhcpResults);
            log(("onNewDhcpResults({" + dhcpResults) + "})");
        }

        @java.lang.Override
        public void onProvisioningSuccess(android.net.LinkProperties newLp) {
            mCallback.onProvisioningSuccess(newLp);
            log(("onProvisioningSuccess({" + newLp) + "})");
        }

        @java.lang.Override
        public void onProvisioningFailure(android.net.LinkProperties newLp) {
            mCallback.onProvisioningFailure(newLp);
            log(("onProvisioningFailure({" + newLp) + "})");
        }

        @java.lang.Override
        public void onLinkPropertiesChange(android.net.LinkProperties newLp) {
            mCallback.onLinkPropertiesChange(newLp);
            log(("onLinkPropertiesChange({" + newLp) + "})");
        }

        @java.lang.Override
        public void onReachabilityLost(java.lang.String logMsg) {
            mCallback.onReachabilityLost(logMsg);
            log(("onReachabilityLost(" + logMsg) + ")");
        }

        @java.lang.Override
        public void onQuit() {
            mCallback.onQuit();
            log("onQuit()");
        }

        @java.lang.Override
        public void installPacketFilter(byte[] filter) {
            mCallback.installPacketFilter(filter);
            log(("installPacketFilter(byte[" + filter.length) + "])");
        }

        @java.lang.Override
        public void setFallbackMulticastFilter(boolean enabled) {
            mCallback.setFallbackMulticastFilter(enabled);
            log(("setFallbackMulticastFilter(" + enabled) + ")");
        }

        @java.lang.Override
        public void setNeighborDiscoveryOffload(boolean enable) {
            mCallback.setNeighborDiscoveryOffload(enable);
            log(("setNeighborDiscoveryOffload(" + enable) + ")");
        }
    }

    /**
     * This class encapsulates parameters to be passed to
     * IpManager#startProvisioning(). A defensive copy is made by IpManager
     * and the values specified herein are in force until IpManager#stop()
     * is called.
     *
     * Example use:
     *
     *     final ProvisioningConfiguration config =
     *             mIpManager.buildProvisioningConfiguration()
     *                     .withPreDhcpAction()
     *                     .withProvisioningTimeoutMs(36 * 1000)
     *                     .build();
     *     mIpManager.startProvisioning(config);
     *     ...
     *     mIpManager.stop();
     *
     * The specified provisioning configuration will only be active until
     * IpManager#stop() is called. Future calls to IpManager#startProvisioning()
     * must specify the configuration again.
     */
    public static class ProvisioningConfiguration {
        // TODO: Delete this default timeout once those callers that care are
        // fixed to pass in their preferred timeout.
        // 
        // We pick 36 seconds so we can send DHCP requests at
        // 
        // t=0, t=2, t=6, t=14, t=30
        // 
        // allowing for 10% jitter.
        private static final int DEFAULT_TIMEOUT_MS = 36 * 1000;

        public static class Builder {
            private android.net.ip.IpManager.ProvisioningConfiguration mConfig = new android.net.ip.IpManager.ProvisioningConfiguration();

            public android.net.ip.IpManager.ProvisioningConfiguration.Builder withoutIPv4() {
                mConfig.mEnableIPv4 = false;
                return this;
            }

            public android.net.ip.IpManager.ProvisioningConfiguration.Builder withoutIPv6() {
                mConfig.mEnableIPv6 = false;
                return this;
            }

            public android.net.ip.IpManager.ProvisioningConfiguration.Builder withoutIpReachabilityMonitor() {
                mConfig.mUsingIpReachabilityMonitor = false;
                return this;
            }

            public android.net.ip.IpManager.ProvisioningConfiguration.Builder withPreDhcpAction() {
                mConfig.mRequestedPreDhcpActionMs = android.net.ip.IpManager.ProvisioningConfiguration.DEFAULT_TIMEOUT_MS;
                return this;
            }

            public android.net.ip.IpManager.ProvisioningConfiguration.Builder withPreDhcpAction(int dhcpActionTimeoutMs) {
                mConfig.mRequestedPreDhcpActionMs = dhcpActionTimeoutMs;
                return this;
            }

            public android.net.ip.IpManager.ProvisioningConfiguration.Builder withStaticConfiguration(android.net.StaticIpConfiguration staticConfig) {
                mConfig.mStaticIpConfig = staticConfig;
                return this;
            }

            public android.net.ip.IpManager.ProvisioningConfiguration.Builder withApfCapabilities(android.net.apf.ApfCapabilities apfCapabilities) {
                mConfig.mApfCapabilities = apfCapabilities;
                return this;
            }

            public android.net.ip.IpManager.ProvisioningConfiguration.Builder withProvisioningTimeoutMs(int timeoutMs) {
                mConfig.mProvisioningTimeoutMs = timeoutMs;
                return this;
            }

            public android.net.ip.IpManager.ProvisioningConfiguration build() {
                return new android.net.ip.IpManager.ProvisioningConfiguration(mConfig);
            }
        }

        /* package */
        boolean mEnableIPv4 = true;

        /* package */
        boolean mEnableIPv6 = true;

        /* package */
        boolean mUsingIpReachabilityMonitor = true;

        /* package */
        int mRequestedPreDhcpActionMs;

        /* package */
        android.net.StaticIpConfiguration mStaticIpConfig;

        /* package */
        android.net.apf.ApfCapabilities mApfCapabilities;

        /* package */
        int mProvisioningTimeoutMs = android.net.ip.IpManager.ProvisioningConfiguration.DEFAULT_TIMEOUT_MS;

        public ProvisioningConfiguration() {
        }

        public ProvisioningConfiguration(android.net.ip.IpManager.ProvisioningConfiguration other) {
            mEnableIPv4 = other.mEnableIPv4;
            mEnableIPv6 = other.mEnableIPv6;
            mUsingIpReachabilityMonitor = other.mUsingIpReachabilityMonitor;
            mRequestedPreDhcpActionMs = other.mRequestedPreDhcpActionMs;
            mStaticIpConfig = other.mStaticIpConfig;
            mApfCapabilities = other.mApfCapabilities;
            mProvisioningTimeoutMs = other.mProvisioningTimeoutMs;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return new java.util.StringJoiner(", ", getClass().getSimpleName() + "{", "}").add("mEnableIPv4: " + mEnableIPv4).add("mEnableIPv6: " + mEnableIPv6).add("mUsingIpReachabilityMonitor: " + mUsingIpReachabilityMonitor).add("mRequestedPreDhcpActionMs: " + mRequestedPreDhcpActionMs).add("mStaticIpConfig: " + mStaticIpConfig).add("mApfCapabilities: " + mApfCapabilities).add("mProvisioningTimeoutMs: " + mProvisioningTimeoutMs).toString();
        }
    }

    public static final java.lang.String DUMP_ARG = "ipmanager";

    public static final java.lang.String DUMP_ARG_CONFIRM = "confirm";

    private static final int CMD_STOP = 1;

    private static final int CMD_START = 2;

    private static final int CMD_CONFIRM = 3;

    private static final int EVENT_PRE_DHCP_ACTION_COMPLETE = 4;

    // Sent by NetlinkTracker to communicate netlink events.
    private static final int EVENT_NETLINK_LINKPROPERTIES_CHANGED = 5;

    private static final int CMD_UPDATE_TCP_BUFFER_SIZES = 6;

    private static final int CMD_UPDATE_HTTP_PROXY = 7;

    private static final int CMD_SET_MULTICAST_FILTER = 8;

    private static final int EVENT_PROVISIONING_TIMEOUT = 9;

    private static final int EVENT_DHCPACTION_TIMEOUT = 10;

    private static final int MAX_LOG_RECORDS = 500;

    private static final boolean NO_CALLBACKS = false;

    private static final boolean SEND_CALLBACKS = true;

    // This must match the interface prefix in clatd.c.
    // TODO: Revert this hack once IpManager and Nat464Xlat work in concert.
    private static final java.lang.String CLAT_PREFIX = "v4-";

    private final com.android.internal.util.State mStoppedState = new android.net.ip.IpManager.StoppedState();

    private final com.android.internal.util.State mStoppingState = new android.net.ip.IpManager.StoppingState();

    private final com.android.internal.util.State mStartedState = new android.net.ip.IpManager.StartedState();

    private final com.android.internal.util.State mRunningState = new android.net.ip.IpManager.RunningState();

    private final java.lang.String mTag;

    private final android.content.Context mContext;

    private final java.lang.String mInterfaceName;

    private final java.lang.String mClatInterfaceName;

    @com.android.internal.annotations.VisibleForTesting
    protected final android.net.ip.IpManager.Callback mCallback;

    private final android.os.INetworkManagementService mNwService;

    private final com.android.server.net.NetlinkTracker mNetlinkTracker;

    private final com.android.internal.util.WakeupMessage mProvisioningTimeoutAlarm;

    private final com.android.internal.util.WakeupMessage mDhcpActionTimeoutAlarm;

    private final android.net.util.AvoidBadWifiTracker mAvoidBadWifiTracker;

    private final android.util.LocalLog mLocalLog;

    private final android.net.ip.IpManager.MessageHandlingLogger mMsgStateLogger;

    private final android.net.metrics.IpConnectivityLog mMetricsLog = new android.net.metrics.IpConnectivityLog();

    private java.net.NetworkInterface mNetworkInterface;

    /**
     * Non-final member variables accessed only from within our StateMachine.
     */
    private android.net.LinkProperties mLinkProperties;

    private android.net.ip.IpManager.ProvisioningConfiguration mConfiguration;

    private android.net.ip.IpReachabilityMonitor mIpReachabilityMonitor;

    private android.net.dhcp.DhcpClient mDhcpClient;

    private android.net.DhcpResults mDhcpResults;

    private java.lang.String mTcpBufferSizes;

    private android.net.ProxyInfo mHttpProxy;

    private android.net.apf.ApfFilter mApfFilter;

    private boolean mMulticastFiltering;

    private long mStartTimeMillis;

    public IpManager(android.content.Context context, java.lang.String ifName, android.net.ip.IpManager.Callback callback) throws java.lang.IllegalArgumentException {
        this(context, ifName, callback, INetworkManagementService.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.NETWORKMANAGEMENT_SERVICE)));
    }

    /**
     * An expanded constructor, useful for dependency injection.
     */
    public IpManager(android.content.Context context, java.lang.String ifName, android.net.ip.IpManager.Callback callback, android.os.INetworkManagementService nwService) throws java.lang.IllegalArgumentException {
        super((android.net.ip.IpManager.class.getSimpleName() + ".") + ifName);
        mTag = getName();
        mContext = context;
        mInterfaceName = ifName;
        mClatInterfaceName = android.net.ip.IpManager.CLAT_PREFIX + ifName;
        mCallback = new android.net.ip.IpManager.LoggingCallbackWrapper(callback);
        mNwService = nwService;
        mNetlinkTracker = new com.android.server.net.NetlinkTracker(mInterfaceName, new com.android.server.net.NetlinkTracker.Callback() {
            @java.lang.Override
            public void update() {
                sendMessage(android.net.ip.IpManager.EVENT_NETLINK_LINKPROPERTIES_CHANGED);
            }
        }) {
            @java.lang.Override
            public void interfaceAdded(java.lang.String iface) {
                interfaceAdded(iface);
                if (mClatInterfaceName.equals(iface)) {
                    mCallback.setNeighborDiscoveryOffload(false);
                }
            }

            @java.lang.Override
            public void interfaceRemoved(java.lang.String iface) {
                interfaceRemoved(iface);
                if (mClatInterfaceName.equals(iface)) {
                    // TODO: consider sending a message to the IpManager main
                    // StateMachine thread, in case "NDO enabled" state becomes
                    // tied to more things that 464xlat operation.
                    mCallback.setNeighborDiscoveryOffload(true);
                }
            }
        };
        try {
            mNwService.registerObserver(mNetlinkTracker);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(mTag, "Couldn't register NetlinkTracker: " + e.toString());
        }
        mAvoidBadWifiTracker = new android.net.util.AvoidBadWifiTracker(mContext, getHandler());
        resetLinkProperties();
        mProvisioningTimeoutAlarm = new com.android.internal.util.WakeupMessage(mContext, getHandler(), mTag + ".EVENT_PROVISIONING_TIMEOUT", android.net.ip.IpManager.EVENT_PROVISIONING_TIMEOUT);
        mDhcpActionTimeoutAlarm = new com.android.internal.util.WakeupMessage(mContext, getHandler(), mTag + ".EVENT_DHCPACTION_TIMEOUT", android.net.ip.IpManager.EVENT_DHCPACTION_TIMEOUT);
        // Super simple StateMachine.
        addState(mStoppedState);
        addState(mStartedState);
        addState(mRunningState, mStartedState);
        addState(mStoppingState);
        setInitialState(mStoppedState);
        mLocalLog = new android.util.LocalLog(android.net.ip.IpManager.MAX_LOG_RECORDS);
        mMsgStateLogger = new android.net.ip.IpManager.MessageHandlingLogger();
        super.start();
    }

    @java.lang.Override
    protected void onQuitting() {
        mCallback.onQuit();
    }

    // Shut down this IpManager instance altogether.
    public void shutdown() {
        stop();
        quit();
    }

    public static android.net.ip.IpManager.ProvisioningConfiguration.Builder buildProvisioningConfiguration() {
        return new android.net.ip.IpManager.ProvisioningConfiguration.Builder();
    }

    public void startProvisioning(android.net.ip.IpManager.ProvisioningConfiguration req) {
        getNetworkInterface();
        mCallback.setNeighborDiscoveryOffload(true);
        sendMessage(android.net.ip.IpManager.CMD_START, new android.net.ip.IpManager.ProvisioningConfiguration(req));
    }

    // TODO: Delete this.
    public void startProvisioning(android.net.StaticIpConfiguration staticIpConfig) {
        startProvisioning(android.net.ip.IpManager.buildProvisioningConfiguration().withStaticConfiguration(staticIpConfig).build());
    }

    public void startProvisioning() {
        startProvisioning(new android.net.ip.IpManager.ProvisioningConfiguration());
    }

    public void stop() {
        sendMessage(android.net.ip.IpManager.CMD_STOP);
    }

    public void confirmConfiguration() {
        sendMessage(android.net.ip.IpManager.CMD_CONFIRM);
    }

    public void completedPreDhcpAction() {
        sendMessage(android.net.ip.IpManager.EVENT_PRE_DHCP_ACTION_COMPLETE);
    }

    /**
     * Set the TCP buffer sizes to use.
     *
     * This may be called, repeatedly, at any time before or after a call to
     * #startProvisioning(). The setting is cleared upon calling #stop().
     */
    public void setTcpBufferSizes(java.lang.String tcpBufferSizes) {
        sendMessage(android.net.ip.IpManager.CMD_UPDATE_TCP_BUFFER_SIZES, tcpBufferSizes);
    }

    /**
     * Set the HTTP Proxy configuration to use.
     *
     * This may be called, repeatedly, at any time before or after a call to
     * #startProvisioning(). The setting is cleared upon calling #stop().
     */
    public void setHttpProxy(android.net.ProxyInfo proxyInfo) {
        sendMessage(android.net.ip.IpManager.CMD_UPDATE_HTTP_PROXY, proxyInfo);
    }

    /**
     * Enable or disable the multicast filter.  Attempts to use APF to accomplish the filtering,
     * if not, Callback.setFallbackMulticastFilter() is called.
     */
    public void setMulticastFilter(boolean enabled) {
        sendMessage(android.net.ip.IpManager.CMD_SET_MULTICAST_FILTER, enabled);
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        if ((args.length > 0) && android.net.ip.IpManager.DUMP_ARG_CONFIRM.equals(args[0])) {
            // Execute confirmConfiguration() and take no further action.
            confirmConfiguration();
            return;
        }
        com.android.internal.util.IndentingPrintWriter pw = new com.android.internal.util.IndentingPrintWriter(writer, "  ");
        pw.println("APF dump:");
        pw.increaseIndent();
        // Thread-unsafe access to mApfFilter but just used for debugging.
        android.net.apf.ApfFilter apfFilter = mApfFilter;
        if (apfFilter != null) {
            apfFilter.dump(pw);
        } else {
            pw.println("No apf support");
        }
        pw.decreaseIndent();
        pw.println();
        pw.println(mTag + " StateMachine dump:");
        pw.increaseIndent();
        mLocalLog.readOnlyLocalLog().dump(fd, pw, args);
        pw.decreaseIndent();
    }

    /**
     * Internals.
     */
    @java.lang.Override
    protected java.lang.String getWhatToString(int what) {
        return android.net.ip.IpManager.sWhatToString.get(what, "UNKNOWN: " + java.lang.Integer.toString(what));
    }

    @java.lang.Override
    protected java.lang.String getLogRecString(android.os.Message msg) {
        final java.lang.String logLine = java.lang.String.format("%s/%d %d %d %s [%s]", mInterfaceName, mNetworkInterface == null ? -1 : mNetworkInterface.getIndex(), msg.arg1, msg.arg2, java.util.Objects.toString(msg.obj), mMsgStateLogger);
        final java.lang.String richerLogLine = (getWhatToString(msg.what) + " ") + logLine;
        mLocalLog.log(richerLogLine);
        if (android.net.ip.IpManager.VDBG) {
            android.util.Log.d(mTag, richerLogLine);
        }
        mMsgStateLogger.reset();
        return logLine;
    }

    @java.lang.Override
    protected boolean recordLogRec(android.os.Message msg) {
        // Don't log EVENT_NETLINK_LINKPROPERTIES_CHANGED. They can be noisy,
        // and we already log any LinkProperties change that results in an
        // invocation of IpManager.Callback#onLinkPropertiesChange().
        final boolean shouldLog = msg.what != android.net.ip.IpManager.EVENT_NETLINK_LINKPROPERTIES_CHANGED;
        if (!shouldLog) {
            mMsgStateLogger.reset();
        }
        return shouldLog;
    }

    private void getNetworkInterface() {
        try {
            mNetworkInterface = java.net.NetworkInterface.getByName(mInterfaceName);
        } catch (java.net.SocketException | java.lang.NullPointerException e) {
            // TODO: throw new IllegalStateException.
            android.util.Log.e(mTag, "ALERT: Failed to get interface object: ", e);
        }
    }

    // This needs to be called with care to ensure that our LinkProperties
    // are in sync with the actual LinkProperties of the interface. For example,
    // we should only call this if we know for sure that there are no IP addresses
    // assigned to the interface, etc.
    private void resetLinkProperties() {
        mNetlinkTracker.clearLinkProperties();
        mConfiguration = null;
        mDhcpResults = null;
        mTcpBufferSizes = "";
        mHttpProxy = null;
        mLinkProperties = new android.net.LinkProperties();
        mLinkProperties.setInterfaceName(mInterfaceName);
    }

    private void recordMetric(final int type) {
        if (mStartTimeMillis <= 0) {
            android.util.Log.wtf(mTag, "Start time undefined!");
        }
        final long duration = android.os.SystemClock.elapsedRealtime() - mStartTimeMillis;
        mMetricsLog.log(new android.net.metrics.IpManagerEvent(mInterfaceName, type, duration));
    }

    // For now: use WifiStateMachine's historical notion of provisioned.
    private static boolean isProvisioned(android.net.LinkProperties lp) {
        // For historical reasons, we should connect even if all we have is
        // an IPv4 address and nothing else.
        return lp.isProvisioned() || lp.hasIPv4Address();
    }

    // TODO: Investigate folding all this into the existing static function
    // LinkProperties.compareProvisioning() or some other single function that
    // takes two LinkProperties objects and returns a ProvisioningChange
    // object that is a correct and complete assessment of what changed, taking
    // account of the asymmetries described in the comments in this function.
    // Then switch to using it everywhere (IpReachabilityMonitor, etc.).
    private android.net.LinkProperties.ProvisioningChange compareProvisioning(android.net.LinkProperties oldLp, android.net.LinkProperties newLp) {
        android.net.LinkProperties.ProvisioningChange delta;
        final boolean wasProvisioned = android.net.ip.IpManager.isProvisioned(oldLp);
        final boolean isProvisioned = android.net.ip.IpManager.isProvisioned(newLp);
        if ((!wasProvisioned) && isProvisioned) {
            delta = android.net.LinkProperties.ProvisioningChange.GAINED_PROVISIONING;
        } else
            if (wasProvisioned && isProvisioned) {
                delta = android.net.LinkProperties.ProvisioningChange.STILL_PROVISIONED;
            } else
                if ((!wasProvisioned) && (!isProvisioned)) {
                    delta = android.net.LinkProperties.ProvisioningChange.STILL_NOT_PROVISIONED;
                } else {
                    // (wasProvisioned && !isProvisioned)
                    // 
                    // Note that this is true even if we lose a configuration element
                    // (e.g., a default gateway) that would not be required to advance
                    // into provisioned state. This is intended: if we have a default
                    // router and we lose it, that's a sure sign of a problem, but if
                    // we connect to a network with no IPv4 DNS servers, we consider
                    // that to be a network without DNS servers and connect anyway.
                    // 
                    // See the comment below.
                    delta = android.net.LinkProperties.ProvisioningChange.LOST_PROVISIONING;
                }


        final boolean lostIPv6 = oldLp.isIPv6Provisioned() && (!newLp.isIPv6Provisioned());
        final boolean lostIPv4Address = oldLp.hasIPv4Address() && (!newLp.hasIPv4Address());
        final boolean lostIPv6Router = oldLp.hasIPv6DefaultRoute() && (!newLp.hasIPv6DefaultRoute());
        // If bad wifi avoidance is disabled, then ignore IPv6 loss of
        // provisioning. Otherwise, when a hotspot that loses Internet
        // access sends out a 0-lifetime RA to its clients, the clients
        // will disconnect and then reconnect, avoiding the bad hotspot,
        // instead of getting stuck on the bad hotspot. http://b/31827713 .
        // 
        // This is incorrect because if the hotspot then regains Internet
        // access with a different prefix, TCP connections on the
        // deprecated addresses will remain stuck.
        // 
        // Note that we can still be disconnected by IpReachabilityMonitor
        // if the IPv6 default gateway (but not the IPv6 DNS servers; see
        // accompanying code in IpReachabilityMonitor) is unreachable.
        final boolean ignoreIPv6ProvisioningLoss = !mAvoidBadWifiTracker.currentValue();
        // Additionally:
        // 
        // Partial configurations (e.g., only an IPv4 address with no DNS
        // servers and no default route) are accepted as long as DHCPv4
        // succeeds. On such a network, isProvisioned() will always return
        // false, because the configuration is not complete, but we want to
        // connect anyway. It might be a disconnected network such as a
        // Chromecast or a wireless printer, for example.
        // 
        // Because on such a network isProvisioned() will always return false,
        // delta will never be LOST_PROVISIONING. So check for loss of
        // provisioning here too.
        if (lostIPv4Address || (lostIPv6 && (!ignoreIPv6ProvisioningLoss))) {
            delta = android.net.LinkProperties.ProvisioningChange.LOST_PROVISIONING;
        }
        // Additionally:
        // 
        // If the previous link properties had a global IPv6 address and an
        // IPv6 default route then also consider the loss of that default route
        // to be a loss of provisioning. See b/27962810.
        if (oldLp.hasGlobalIPv6Address() && (lostIPv6Router && (!ignoreIPv6ProvisioningLoss))) {
            delta = android.net.LinkProperties.ProvisioningChange.LOST_PROVISIONING;
        }
        return delta;
    }

    private void dispatchCallback(android.net.LinkProperties.ProvisioningChange delta, android.net.LinkProperties newLp) {
        switch (delta) {
            case GAINED_PROVISIONING :
                if (android.net.ip.IpManager.VDBG) {
                    android.util.Log.d(mTag, "onProvisioningSuccess()");
                }
                recordMetric(android.net.metrics.IpManagerEvent.PROVISIONING_OK);
                mCallback.onProvisioningSuccess(newLp);
                break;
            case LOST_PROVISIONING :
                if (android.net.ip.IpManager.VDBG) {
                    android.util.Log.d(mTag, "onProvisioningFailure()");
                }
                recordMetric(android.net.metrics.IpManagerEvent.PROVISIONING_FAIL);
                mCallback.onProvisioningFailure(newLp);
                break;
            default :
                if (android.net.ip.IpManager.VDBG) {
                    android.util.Log.d(mTag, "onLinkPropertiesChange()");
                }
                mCallback.onLinkPropertiesChange(newLp);
                break;
        }
    }

    // Updates all IpManager-related state concerned with LinkProperties.
    // Returns a ProvisioningChange for possibly notifying other interested
    // parties that are not fronted by IpManager.
    private android.net.LinkProperties.ProvisioningChange setLinkProperties(android.net.LinkProperties newLp) {
        if (mApfFilter != null) {
            mApfFilter.setLinkProperties(newLp);
        }
        if (mIpReachabilityMonitor != null) {
            mIpReachabilityMonitor.updateLinkProperties(newLp);
        }
        android.net.LinkProperties.ProvisioningChange delta = compareProvisioning(mLinkProperties, newLp);
        mLinkProperties = new android.net.LinkProperties(newLp);
        if (delta == android.net.LinkProperties.ProvisioningChange.GAINED_PROVISIONING) {
            // TODO: Add a proper ProvisionedState and cancel the alarm in
            // its enter() method.
            mProvisioningTimeoutAlarm.cancel();
        }
        return delta;
    }

    private boolean linkPropertiesUnchanged(android.net.LinkProperties newLp) {
        return java.util.Objects.equals(newLp, mLinkProperties);
    }

    private android.net.LinkProperties assembleLinkProperties() {
        // [1] Create a new LinkProperties object to populate.
        android.net.LinkProperties newLp = new android.net.LinkProperties();
        newLp.setInterfaceName(mInterfaceName);
        // [2] Pull in data from netlink:
        // - IPv4 addresses
        // - IPv6 addresses
        // - IPv6 routes
        // - IPv6 DNS servers
        // 
        // N.B.: this is fundamentally race-prone and should be fixed by
        // changing NetlinkTracker from a hybrid edge/level model to an
        // edge-only model, or by giving IpManager its own netlink socket(s)
        // so as to track all required information directly.
        android.net.LinkProperties netlinkLinkProperties = mNetlinkTracker.getLinkProperties();
        newLp.setLinkAddresses(netlinkLinkProperties.getLinkAddresses());
        for (android.net.RouteInfo route : netlinkLinkProperties.getRoutes()) {
            newLp.addRoute(route);
        }
        for (java.net.InetAddress dns : netlinkLinkProperties.getDnsServers()) {
            // Only add likely reachable DNS servers.
            // TODO: investigate deleting this.
            if (newLp.isReachable(dns)) {
                newLp.addDnsServer(dns);
            }
        }
        // [3] Add in data from DHCPv4, if available.
        // 
        // mDhcpResults is never shared with any other owner so we don't have
        // to worry about concurrent modification.
        if (mDhcpResults != null) {
            for (android.net.RouteInfo route : mDhcpResults.getRoutes(mInterfaceName)) {
                newLp.addRoute(route);
            }
            for (java.net.InetAddress dns : mDhcpResults.dnsServers) {
                // Only add likely reachable DNS servers.
                // TODO: investigate deleting this.
                if (newLp.isReachable(dns)) {
                    newLp.addDnsServer(dns);
                }
            }
            newLp.setDomains(mDhcpResults.domains);
            if (mDhcpResults.mtu != 0) {
                newLp.setMtu(mDhcpResults.mtu);
            }
        }
        // [4] Add in TCP buffer sizes and HTTP Proxy config, if available.
        if (!android.text.TextUtils.isEmpty(mTcpBufferSizes)) {
            newLp.setTcpBufferSizes(mTcpBufferSizes);
        }
        if (mHttpProxy != null) {
            newLp.setHttpProxy(mHttpProxy);
        }
        if (android.net.ip.IpManager.VDBG) {
            android.util.Log.d(mTag, ("newLp{" + newLp) + "}");
        }
        return newLp;
    }

    // Returns false if we have lost provisioning, true otherwise.
    private boolean handleLinkPropertiesUpdate(boolean sendCallbacks) {
        final android.net.LinkProperties newLp = assembleLinkProperties();
        if (linkPropertiesUnchanged(newLp)) {
            return true;
        }
        final android.net.LinkProperties.ProvisioningChange delta = setLinkProperties(newLp);
        if (sendCallbacks) {
            dispatchCallback(delta, newLp);
        }
        return delta != android.net.LinkProperties.ProvisioningChange.LOST_PROVISIONING;
    }

    private boolean setIPv4Address(android.net.LinkAddress address) {
        final android.net.InterfaceConfiguration ifcg = new android.net.InterfaceConfiguration();
        ifcg.setLinkAddress(address);
        try {
            mNwService.setInterfaceConfig(mInterfaceName, ifcg);
            if (android.net.ip.IpManager.VDBG)
                android.util.Log.d(mTag, "IPv4 configuration succeeded");

        } catch (java.lang.IllegalStateException | android.os.RemoteException e) {
            android.util.Log.e(mTag, "IPv4 configuration failed: ", e);
            return false;
        }
        return true;
    }

    private void clearIPv4Address() {
        try {
            final android.net.InterfaceConfiguration ifcg = new android.net.InterfaceConfiguration();
            ifcg.setLinkAddress(new android.net.LinkAddress("0.0.0.0/0"));
            mNwService.setInterfaceConfig(mInterfaceName, ifcg);
        } catch (java.lang.IllegalStateException | android.os.RemoteException e) {
            android.util.Log.e(mTag, "ALERT: Failed to clear IPv4 address on interface " + mInterfaceName, e);
        }
    }

    private void handleIPv4Success(android.net.DhcpResults dhcpResults) {
        mDhcpResults = new android.net.DhcpResults(dhcpResults);
        final android.net.LinkProperties newLp = assembleLinkProperties();
        final android.net.LinkProperties.ProvisioningChange delta = setLinkProperties(newLp);
        if (android.net.ip.IpManager.VDBG) {
            android.util.Log.d(mTag, ("onNewDhcpResults(" + java.util.Objects.toString(dhcpResults)) + ")");
        }
        mCallback.onNewDhcpResults(dhcpResults);
        dispatchCallback(delta, newLp);
    }

    private void handleIPv4Failure() {
        // TODO: Investigate deleting this clearIPv4Address() call.
        // 
        // DhcpClient will send us CMD_CLEAR_LINKADDRESS in all circumstances
        // that could trigger a call to this function. If we missed handling
        // that message in StartedState for some reason we would still clear
        // any addresses upon entry to StoppedState.
        clearIPv4Address();
        mDhcpResults = null;
        if (android.net.ip.IpManager.VDBG) {
            android.util.Log.d(mTag, "onNewDhcpResults(null)");
        }
        mCallback.onNewDhcpResults(null);
        handleProvisioningFailure();
    }

    private void handleProvisioningFailure() {
        final android.net.LinkProperties newLp = assembleLinkProperties();
        android.net.LinkProperties.ProvisioningChange delta = setLinkProperties(newLp);
        // If we've gotten here and we're still not provisioned treat that as
        // a total loss of provisioning.
        // 
        // Either (a) static IP configuration failed or (b) DHCPv4 failed AND
        // there was no usable IPv6 obtained before a non-zero provisioning
        // timeout expired.
        // 
        // Regardless: GAME OVER.
        if (delta == android.net.LinkProperties.ProvisioningChange.STILL_NOT_PROVISIONED) {
            delta = android.net.LinkProperties.ProvisioningChange.LOST_PROVISIONING;
        }
        dispatchCallback(delta, newLp);
        if (delta == android.net.LinkProperties.ProvisioningChange.LOST_PROVISIONING) {
            transitionTo(mStoppingState);
        }
    }

    private boolean startIPv4() {
        // If we have a StaticIpConfiguration attempt to apply it and
        // handle the result accordingly.
        if (mConfiguration.mStaticIpConfig != null) {
            if (setIPv4Address(mConfiguration.mStaticIpConfig.ipAddress)) {
                handleIPv4Success(new android.net.DhcpResults(mConfiguration.mStaticIpConfig));
            } else {
                if (android.net.ip.IpManager.VDBG) {
                    android.util.Log.d(mTag, "onProvisioningFailure()");
                }
                recordMetric(android.net.metrics.IpManagerEvent.PROVISIONING_FAIL);
                mCallback.onProvisioningFailure(new android.net.LinkProperties(mLinkProperties));
                return false;
            }
        } else {
            // Start DHCPv4.
            mDhcpClient = android.net.dhcp.DhcpClient.makeDhcpClient(mContext, this, mInterfaceName);
            mDhcpClient.registerForPreDhcpNotification();
            mDhcpClient.sendMessage(android.net.dhcp.DhcpClient.CMD_START_DHCP);
        }
        return true;
    }

    private boolean startIPv6() {
        // Set privacy extensions.
        try {
            mNwService.setInterfaceIpv6PrivacyExtensions(mInterfaceName, true);
            mNwService.enableIpv6(mInterfaceName);
        } catch (android.os.RemoteException re) {
            android.util.Log.e(mTag, "Unable to change interface settings: " + re);
            return false;
        } catch (java.lang.IllegalStateException ie) {
            android.util.Log.e(mTag, "Unable to change interface settings: " + ie);
            return false;
        }
        return true;
    }

    private void stopAllIP() {
        // We don't need to worry about routes, just addresses, because:
        // - disableIpv6() will clear autoconf IPv6 routes as well, and
        // - we don't get IPv4 routes from netlink
        // so we neither react to nor need to wait for changes in either.
        try {
            mNwService.disableIpv6(mInterfaceName);
        } catch (java.lang.Exception e) {
            android.util.Log.e(mTag, "Failed to disable IPv6" + e);
        }
        try {
            mNwService.clearInterfaceAddresses(mInterfaceName);
        } catch (java.lang.Exception e) {
            android.util.Log.e(mTag, "Failed to clear addresses " + e);
        }
    }

    class StoppedState extends com.android.internal.util.State {
        @java.lang.Override
        public void enter() {
            stopAllIP();
            resetLinkProperties();
            if (mStartTimeMillis > 0) {
                recordMetric(android.net.metrics.IpManagerEvent.COMPLETE_LIFECYCLE);
                mStartTimeMillis = 0;
            }
        }

        @java.lang.Override
        public boolean processMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.net.ip.IpManager.CMD_STOP :
                    break;
                case android.net.ip.IpManager.CMD_START :
                    mConfiguration = ((android.net.ip.IpManager.ProvisioningConfiguration) (msg.obj));
                    transitionTo(mStartedState);
                    break;
                case android.net.ip.IpManager.EVENT_NETLINK_LINKPROPERTIES_CHANGED :
                    handleLinkPropertiesUpdate(android.net.ip.IpManager.NO_CALLBACKS);
                    break;
                case android.net.ip.IpManager.CMD_UPDATE_TCP_BUFFER_SIZES :
                    mTcpBufferSizes = ((java.lang.String) (msg.obj));
                    handleLinkPropertiesUpdate(android.net.ip.IpManager.NO_CALLBACKS);
                    break;
                case android.net.ip.IpManager.CMD_UPDATE_HTTP_PROXY :
                    mHttpProxy = ((android.net.ProxyInfo) (msg.obj));
                    handleLinkPropertiesUpdate(android.net.ip.IpManager.NO_CALLBACKS);
                    break;
                case android.net.ip.IpManager.CMD_SET_MULTICAST_FILTER :
                    mMulticastFiltering = ((boolean) (msg.obj));
                    break;
                case android.net.dhcp.DhcpClient.CMD_ON_QUIT :
                    // Everything is already stopped.
                    android.util.Log.e(mTag, "Unexpected CMD_ON_QUIT (already stopped).");
                    break;
                default :
                    return NOT_HANDLED;
            }
            mMsgStateLogger.handled(this, getCurrentState());
            return HANDLED;
        }
    }

    class StoppingState extends com.android.internal.util.State {
        @java.lang.Override
        public void enter() {
            if (mDhcpClient == null) {
                // There's no DHCPv4 for which to wait; proceed to stopped.
                transitionTo(mStoppedState);
            }
        }

        @java.lang.Override
        public boolean processMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.net.ip.IpManager.CMD_STOP :
                    break;
                case android.net.dhcp.DhcpClient.CMD_CLEAR_LINKADDRESS :
                    clearIPv4Address();
                    break;
                case android.net.dhcp.DhcpClient.CMD_ON_QUIT :
                    mDhcpClient = null;
                    transitionTo(mStoppedState);
                    break;
                default :
                    deferMessage(msg);
            }
            mMsgStateLogger.handled(this, getCurrentState());
            return HANDLED;
        }
    }

    class StartedState extends com.android.internal.util.State {
        @java.lang.Override
        public void enter() {
            mStartTimeMillis = android.os.SystemClock.elapsedRealtime();
            if (mConfiguration.mProvisioningTimeoutMs > 0) {
                final long alarmTime = android.os.SystemClock.elapsedRealtime() + mConfiguration.mProvisioningTimeoutMs;
                mProvisioningTimeoutAlarm.schedule(alarmTime);
            }
            if (readyToProceed()) {
                transitionTo(mRunningState);
            } else {
                // Clear all IPv4 and IPv6 before proceeding to RunningState.
                // Clean up any leftover state from an abnormal exit from
                // tethering or during an IpManager restart.
                stopAllIP();
            }
        }

        @java.lang.Override
        public void exit() {
            mProvisioningTimeoutAlarm.cancel();
        }

        @java.lang.Override
        public boolean processMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.net.ip.IpManager.CMD_STOP :
                    transitionTo(mStoppingState);
                    break;
                case android.net.ip.IpManager.EVENT_NETLINK_LINKPROPERTIES_CHANGED :
                    handleLinkPropertiesUpdate(android.net.ip.IpManager.NO_CALLBACKS);
                    if (readyToProceed()) {
                        transitionTo(mRunningState);
                    }
                    break;
                case android.net.ip.IpManager.EVENT_PROVISIONING_TIMEOUT :
                    handleProvisioningFailure();
                    break;
                default :
                    // It's safe to process messages out of order because the
                    // only message that can both
                    // a) be received at this time and
                    // b) affect provisioning state
                    // is EVENT_NETLINK_LINKPROPERTIES_CHANGED (handled above).
                    deferMessage(msg);
            }
            mMsgStateLogger.handled(this, getCurrentState());
            return HANDLED;
        }

        boolean readyToProceed() {
            return (!mLinkProperties.hasIPv4Address()) && (!mLinkProperties.hasGlobalIPv6Address());
        }
    }

    class RunningState extends com.android.internal.util.State {
        private boolean mDhcpActionInFlight;

        @java.lang.Override
        public void enter() {
            mApfFilter = android.net.apf.ApfFilter.maybeCreate(mConfiguration.mApfCapabilities, mNetworkInterface, mCallback, mMulticastFiltering);
            // TODO: investigate the effects of any multicast filtering racing/interfering with the
            // rest of this IP configuration startup.
            if (mApfFilter == null) {
                mCallback.setFallbackMulticastFilter(mMulticastFiltering);
            }
            if (mConfiguration.mEnableIPv6) {
                // TODO: Consider transitionTo(mStoppingState) if this fails.
                startIPv6();
            }
            if (mConfiguration.mEnableIPv4) {
                if (!startIPv4()) {
                    transitionTo(mStoppingState);
                    return;
                }
            }
            if (mConfiguration.mUsingIpReachabilityMonitor) {
                mIpReachabilityMonitor = new android.net.ip.IpReachabilityMonitor(mContext, mInterfaceName, new android.net.ip.IpReachabilityMonitor.Callback() {
                    @java.lang.Override
                    public void notifyLost(java.net.InetAddress ip, java.lang.String logMsg) {
                        mCallback.onReachabilityLost(logMsg);
                    }
                }, mAvoidBadWifiTracker);
            }
        }

        @java.lang.Override
        public void exit() {
            stopDhcpAction();
            if (mIpReachabilityMonitor != null) {
                mIpReachabilityMonitor.stop();
                mIpReachabilityMonitor = null;
            }
            if (mDhcpClient != null) {
                mDhcpClient.sendMessage(android.net.dhcp.DhcpClient.CMD_STOP_DHCP);
                mDhcpClient.doQuit();
            }
            if (mApfFilter != null) {
                mApfFilter.shutdown();
                mApfFilter = null;
            }
            resetLinkProperties();
        }

        private void ensureDhcpAction() {
            if (!mDhcpActionInFlight) {
                mCallback.onPreDhcpAction();
                mDhcpActionInFlight = true;
                final long alarmTime = android.os.SystemClock.elapsedRealtime() + mConfiguration.mRequestedPreDhcpActionMs;
                mDhcpActionTimeoutAlarm.schedule(alarmTime);
            }
        }

        private void stopDhcpAction() {
            mDhcpActionTimeoutAlarm.cancel();
            if (mDhcpActionInFlight) {
                mCallback.onPostDhcpAction();
                mDhcpActionInFlight = false;
            }
        }

        @java.lang.Override
        public boolean processMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.net.ip.IpManager.CMD_STOP :
                    transitionTo(mStoppingState);
                    break;
                case android.net.ip.IpManager.CMD_START :
                    android.util.Log.e(mTag, "ALERT: START received in StartedState. Please fix caller.");
                    break;
                case android.net.ip.IpManager.CMD_CONFIRM :
                    // TODO: Possibly introduce a second type of confirmation
                    // that both probes (a) on-link neighbors and (b) does
                    // a DHCPv4 RENEW.  We used to do this on Wi-Fi framework
                    // roams.
                    if (mIpReachabilityMonitor != null) {
                        mIpReachabilityMonitor.probeAll();
                    }
                    break;
                case android.net.ip.IpManager.EVENT_PRE_DHCP_ACTION_COMPLETE :
                    // It's possible to reach here if, for example, someone
                    // calls completedPreDhcpAction() after provisioning with
                    // a static IP configuration.
                    if (mDhcpClient != null) {
                        mDhcpClient.sendMessage(android.net.dhcp.DhcpClient.CMD_PRE_DHCP_ACTION_COMPLETE);
                    }
                    break;
                case android.net.ip.IpManager.EVENT_NETLINK_LINKPROPERTIES_CHANGED :
                    if (!handleLinkPropertiesUpdate(android.net.ip.IpManager.SEND_CALLBACKS)) {
                        transitionTo(mStoppingState);
                    }
                    break;
                case android.net.ip.IpManager.CMD_UPDATE_TCP_BUFFER_SIZES :
                    mTcpBufferSizes = ((java.lang.String) (msg.obj));
                    // This cannot possibly change provisioning state.
                    handleLinkPropertiesUpdate(android.net.ip.IpManager.SEND_CALLBACKS);
                    break;
                case android.net.ip.IpManager.CMD_UPDATE_HTTP_PROXY :
                    mHttpProxy = ((android.net.ProxyInfo) (msg.obj));
                    // This cannot possibly change provisioning state.
                    handleLinkPropertiesUpdate(android.net.ip.IpManager.SEND_CALLBACKS);
                    break;
                case android.net.ip.IpManager.CMD_SET_MULTICAST_FILTER :
                    {
                        mMulticastFiltering = ((boolean) (msg.obj));
                        if (mApfFilter != null) {
                            mApfFilter.setMulticastFilter(mMulticastFiltering);
                        } else {
                            mCallback.setFallbackMulticastFilter(mMulticastFiltering);
                        }
                        break;
                    }
                case android.net.ip.IpManager.EVENT_DHCPACTION_TIMEOUT :
                    stopDhcpAction();
                    break;
                case android.net.dhcp.DhcpClient.CMD_PRE_DHCP_ACTION :
                    if (mConfiguration.mRequestedPreDhcpActionMs > 0) {
                        ensureDhcpAction();
                    } else {
                        sendMessage(android.net.ip.IpManager.EVENT_PRE_DHCP_ACTION_COMPLETE);
                    }
                    break;
                case android.net.dhcp.DhcpClient.CMD_CLEAR_LINKADDRESS :
                    clearIPv4Address();
                    break;
                case android.net.dhcp.DhcpClient.CMD_CONFIGURE_LINKADDRESS :
                    {
                        final android.net.LinkAddress ipAddress = ((android.net.LinkAddress) (msg.obj));
                        if (setIPv4Address(ipAddress)) {
                            mDhcpClient.sendMessage(android.net.dhcp.DhcpClient.EVENT_LINKADDRESS_CONFIGURED);
                        } else {
                            android.util.Log.e(mTag, "Failed to set IPv4 address!");
                            dispatchCallback(android.net.LinkProperties.ProvisioningChange.LOST_PROVISIONING, new android.net.LinkProperties(mLinkProperties));
                            transitionTo(mStoppingState);
                        }
                        break;
                    }
                    // This message is only received when:
                    // 
                    // a) initial address acquisition succeeds,
                    // b) renew succeeds or is NAK'd,
                    // c) rebind succeeds or is NAK'd, or
                    // c) the lease expires,
                    // 
                    // but never when initial address acquisition fails. The latter
                    // condition is now governed by the provisioning timeout.
                case android.net.dhcp.DhcpClient.CMD_POST_DHCP_ACTION :
                    stopDhcpAction();
                    switch (msg.arg1) {
                        case android.net.dhcp.DhcpClient.DHCP_SUCCESS :
                            handleIPv4Success(((android.net.DhcpResults) (msg.obj)));
                            break;
                        case android.net.dhcp.DhcpClient.DHCP_FAILURE :
                            handleIPv4Failure();
                            break;
                        default :
                            android.util.Log.e(mTag, "Unknown CMD_POST_DHCP_ACTION status:" + msg.arg1);
                    }
                    break;
                case android.net.dhcp.DhcpClient.CMD_ON_QUIT :
                    // DHCPv4 quit early for some reason.
                    android.util.Log.e(mTag, "Unexpected CMD_ON_QUIT.");
                    mDhcpClient = null;
                    break;
                default :
                    return NOT_HANDLED;
            }
            mMsgStateLogger.handled(this, getCurrentState());
            return HANDLED;
        }
    }

    private static class MessageHandlingLogger {
        public java.lang.String processedInState;

        public java.lang.String receivedInState;

        public void reset() {
            processedInState = null;
            receivedInState = null;
        }

        public void handled(com.android.internal.util.State processedIn, com.android.internal.util.IState receivedIn) {
            processedInState = processedIn.getClass().getSimpleName();
            receivedInState = receivedIn.getName();
        }

        public java.lang.String toString() {
            return java.lang.String.format("rcvd_in=%s, proc_in=%s", receivedInState, processedInState);
        }
    }
}

