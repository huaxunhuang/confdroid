/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.os;


/**
 * Used for configuring and controlling the status of the android common time service.
 *
 * @unknown 
 */
public class CommonTimeConfig {
    /**
     * Successful operation.
     */
    public static final int SUCCESS = 0;

    /**
     * Unspecified error.
     */
    public static final int ERROR = -1;

    /**
     * Operation failed due to bad parameter value.
     */
    public static final int ERROR_BAD_VALUE = -4;

    /**
     * Operation failed due to dead remote object.
     */
    public static final int ERROR_DEAD_OBJECT = -7;

    /**
     * Sentinel value returned by {@link #getMasterElectionGroupId()} when an error occurs trying to
     * fetch the master election group.
     */
    public static final long INVALID_GROUP_ID = -1;

    /**
     * Name of the underlying native binder service
     */
    public static final java.lang.String SERVICE_NAME = "common_time.config";

    /**
     * Class constructor.
     *
     * @throws android.os.RemoteException
     * 		
     */
    public CommonTimeConfig() throws android.os.RemoteException {
        mRemote = android.os.ServiceManager.getService(android.os.CommonTimeConfig.SERVICE_NAME);
        if (null == mRemote)
            throw new android.os.RemoteException();

        mInterfaceDesc = mRemote.getInterfaceDescriptor();
        mUtils = new android.os.CommonTimeUtils(mRemote, mInterfaceDesc);
        mRemote.linkToDeath(mDeathHandler, 0);
    }

    /**
     * Handy class factory method.
     */
    public static android.os.CommonTimeConfig create() {
        android.os.CommonTimeConfig retVal;
        try {
            retVal = new android.os.CommonTimeConfig();
        } catch (android.os.RemoteException e) {
            retVal = null;
        }
        return retVal;
    }

    /**
     * Release all native resources held by this {@link android.os.CommonTimeConfig} instance.  Once
     * resources have been released, the {@link android.os.CommonTimeConfig} instance is
     * disconnected from the native service and will throw a {@link android.os.RemoteException} if
     * any of its methods are called.  Clients should always call release on their client instances
     * before releasing their last Java reference to the instance.  Failure to do this will cause
     * non-deterministic native resource reclamation and may cause the common time service to remain
     * active on the network for longer than it should.
     */
    public void release() {
        if (null != mRemote) {
            try {
                mRemote.unlinkToDeath(mDeathHandler, 0);
            } catch (java.util.NoSuchElementException e) {
            }
            mRemote = null;
        }
        mUtils = null;
    }

    /**
     * Gets the current priority of the common time service used in the master election protocol.
     *
     * @return an 8 bit value indicating the priority of this common time service relative to other
    common time services operating in the same domain.
     * @throws android.os.RemoteException
     * 		
     */
    public byte getMasterElectionPriority() throws android.os.RemoteException {
        throwOnDeadServer();
        return ((byte) (mUtils.transactGetInt(android.os.CommonTimeConfig.METHOD_GET_MASTER_ELECTION_PRIORITY, -1)));
    }

    /**
     * Sets the current priority of the common time service used in the master election protocol.
     *
     * @param priority
     * 		priority of the common time service used in the master election protocol.
     * 		Lower numbers are lower priority.
     * @return {@link #SUCCESS} in case of success,
    {@link #ERROR} or {@link #ERROR_DEAD_OBJECT} in case of failure.
     */
    public int setMasterElectionPriority(byte priority) {
        if (checkDeadServer())
            return android.os.CommonTimeConfig.ERROR_DEAD_OBJECT;

        return mUtils.transactSetInt(android.os.CommonTimeConfig.METHOD_SET_MASTER_ELECTION_PRIORITY, priority);
    }

    /**
     * Gets the IP endpoint used by the time service to participate in the master election protocol.
     *
     * @return an InetSocketAddress containing the IP address and UDP port being used by the
    system's common time service to participate in the master election protocol.
     * @throws android.os.RemoteException
     * 		
     */
    public java.net.InetSocketAddress getMasterElectionEndpoint() throws android.os.RemoteException {
        throwOnDeadServer();
        return mUtils.transactGetSockaddr(android.os.CommonTimeConfig.METHOD_GET_MASTER_ELECTION_ENDPOINT);
    }

    /**
     * Sets the IP endpoint used by the common time service to participate in the master election
     * protocol.
     *
     * @param ep
     * 		The IP address and UDP port to be used by the common time service to participate in
     * 		the master election protocol.  The supplied IP address must be either the broadcast or
     * 		multicast address, unicast addresses are considered to be illegal values.
     * @return {@link #SUCCESS} in case of success,
    {@link #ERROR}, {@link #ERROR_BAD_VALUE} or {@link #ERROR_DEAD_OBJECT} in case of failure.
     */
    public int setMasterElectionEndpoint(java.net.InetSocketAddress ep) {
        if (checkDeadServer())
            return android.os.CommonTimeConfig.ERROR_DEAD_OBJECT;

        return mUtils.transactSetSockaddr(android.os.CommonTimeConfig.METHOD_SET_MASTER_ELECTION_ENDPOINT, ep);
    }

    /**
     * Gets the current group ID used by the common time service in the master election protocol.
     *
     * @return The 64-bit group ID of the common time service.
     * @throws android.os.RemoteException
     * 		
     */
    public long getMasterElectionGroupId() throws android.os.RemoteException {
        throwOnDeadServer();
        return mUtils.transactGetLong(android.os.CommonTimeConfig.METHOD_GET_MASTER_ELECTION_GROUP_ID, android.os.CommonTimeConfig.INVALID_GROUP_ID);
    }

    /**
     * Sets the current group ID used by the common time service in the master election protocol.
     *
     * @param id
     * 		The 64-bit group ID of the common time service.
     * @return {@link #SUCCESS} in case of success,
    {@link #ERROR}, {@link #ERROR_BAD_VALUE} or {@link #ERROR_DEAD_OBJECT} in case of failure.
     */
    public int setMasterElectionGroupId(long id) {
        if (checkDeadServer())
            return android.os.CommonTimeConfig.ERROR_DEAD_OBJECT;

        return mUtils.transactSetLong(android.os.CommonTimeConfig.METHOD_SET_MASTER_ELECTION_GROUP_ID, id);
    }

    /**
     * Gets the name of the network interface which the common time service attempts to bind to.
     *
     * @return a string with the network interface name which the common time service is bound to,
    or null if the service is currently unbound.  Examples of interface names are things like
    "eth0", or "wlan0".
     * @throws android.os.RemoteException
     * 		
     */
    public java.lang.String getInterfaceBinding() throws android.os.RemoteException {
        throwOnDeadServer();
        java.lang.String ifaceName = mUtils.transactGetString(android.os.CommonTimeConfig.METHOD_GET_INTERFACE_BINDING, null);
        if ((null != ifaceName) && (0 == ifaceName.length()))
            return null;

        return ifaceName;
    }

    /**
     * Sets the name of the network interface which the common time service should attempt to bind
     * to.
     *
     * @param ifaceName
     * 		The name of the network interface ("eth0", "wlan0", etc...) wich the common
     * 		time service should attempt to bind to, or null to force the common time service to unbind
     * 		from the network and run in networkless mode.
     * @return {@link #SUCCESS} in case of success,
    {@link #ERROR}, {@link #ERROR_BAD_VALUE} or {@link #ERROR_DEAD_OBJECT} in case of failure.
     */
    public int setNetworkBinding(java.lang.String ifaceName) {
        if (checkDeadServer())
            return android.os.CommonTimeConfig.ERROR_DEAD_OBJECT;

        return mUtils.transactSetString(android.os.CommonTimeConfig.METHOD_SET_INTERFACE_BINDING, null == ifaceName ? "" : ifaceName);
    }

    /**
     * Gets the amount of time the common time service will wait between master announcements when
     * it is the timeline master.
     *
     * @return The time (in milliseconds) between master announcements.
     * @throws android.os.RemoteException
     * 		
     */
    public int getMasterAnnounceInterval() throws android.os.RemoteException {
        throwOnDeadServer();
        return mUtils.transactGetInt(android.os.CommonTimeConfig.METHOD_GET_MASTER_ANNOUNCE_INTERVAL, -1);
    }

    /**
     * Sets the amount of time the common time service will wait between master announcements when
     * it is the timeline master.
     *
     * @param interval
     * 		The time (in milliseconds) between master announcements.
     * @return {@link #SUCCESS} in case of success,
    {@link #ERROR}, {@link #ERROR_BAD_VALUE} or {@link #ERROR_DEAD_OBJECT} in case of failure.
     */
    public int setMasterAnnounceInterval(int interval) {
        if (checkDeadServer())
            return android.os.CommonTimeConfig.ERROR_DEAD_OBJECT;

        return mUtils.transactSetInt(android.os.CommonTimeConfig.METHOD_SET_MASTER_ANNOUNCE_INTERVAL, interval);
    }

    /**
     * Gets the amount of time the common time service will wait between time synchronization
     * requests when it is the client of another common time service on the network.
     *
     * @return The time (in milliseconds) between time sync requests.
     * @throws android.os.RemoteException
     * 		
     */
    public int getClientSyncInterval() throws android.os.RemoteException {
        throwOnDeadServer();
        return mUtils.transactGetInt(android.os.CommonTimeConfig.METHOD_GET_CLIENT_SYNC_INTERVAL, -1);
    }

    /**
     * Sets the amount of time the common time service will wait between time synchronization
     * requests when it is the client of another common time service on the network.
     *
     * @param interval
     * 		The time (in milliseconds) between time sync requests.
     * @return {@link #SUCCESS} in case of success,
    {@link #ERROR}, {@link #ERROR_BAD_VALUE} or {@link #ERROR_DEAD_OBJECT} in case of failure.
     */
    public int setClientSyncInterval(int interval) {
        if (checkDeadServer())
            return android.os.CommonTimeConfig.ERROR_DEAD_OBJECT;

        return mUtils.transactSetInt(android.os.CommonTimeConfig.METHOD_SET_CLIENT_SYNC_INTERVAL, interval);
    }

    /**
     * Gets the panic threshold for the estimated error level of the common time service.  When the
     * common time service's estimated error rises above this level, the service will panic and
     * reset, causing a discontinuity in the currently synchronized timeline.
     *
     * @return The threshold (in microseconds) past which the common time service will panic.
     * @throws android.os.RemoteException
     * 		
     */
    public int getPanicThreshold() throws android.os.RemoteException {
        throwOnDeadServer();
        return mUtils.transactGetInt(android.os.CommonTimeConfig.METHOD_GET_PANIC_THRESHOLD, -1);
    }

    /**
     * Sets the panic threshold for the estimated error level of the common time service.  When the
     * common time service's estimated error rises above this level, the service will panic and
     * reset, causing a discontinuity in the currently synchronized timeline.
     *
     * @param threshold
     * 		The threshold (in microseconds) past which the common time service will
     * 		panic.
     * @return {@link #SUCCESS} in case of success,
    {@link #ERROR}, {@link #ERROR_BAD_VALUE} or {@link #ERROR_DEAD_OBJECT} in case of failure.
     */
    public int setPanicThreshold(int threshold) {
        if (checkDeadServer())
            return android.os.CommonTimeConfig.ERROR_DEAD_OBJECT;

        return mUtils.transactSetInt(android.os.CommonTimeConfig.METHOD_SET_PANIC_THRESHOLD, threshold);
    }

    /**
     * Gets the current state of the common time service's auto disable flag.
     *
     * @return The current state of the common time service's auto disable flag.
     * @throws android.os.RemoteException
     * 		
     */
    public boolean getAutoDisable() throws android.os.RemoteException {
        throwOnDeadServer();
        return 1 == mUtils.transactGetInt(android.os.CommonTimeConfig.METHOD_GET_AUTO_DISABLE, 1);
    }

    /**
     * Sets the current state of the common time service's auto disable flag.  When the time
     * service's auto disable flag is set, it will automatically cease all network activity when
     * it has no active local clients, resuming activity the next time the service has interested
     * local clients.  When the auto disabled flag is cleared, the common time service will continue
     * to participate the time synchronization group even when it has no active local clients.
     *
     * @param autoDisable
     * 		The desired state of the common time service's auto disable flag.
     * @return {@link #SUCCESS} in case of success,
    {@link #ERROR} or {@link #ERROR_DEAD_OBJECT} in case of failure.
     */
    public int setAutoDisable(boolean autoDisable) {
        if (checkDeadServer())
            return android.os.CommonTimeConfig.ERROR_DEAD_OBJECT;

        return mUtils.transactSetInt(android.os.CommonTimeConfig.METHOD_SET_AUTO_DISABLE, autoDisable ? 1 : 0);
    }

    /**
     * At startup, the time service enters the initial state and remains there until it is given a
     * network interface to bind to.  Common time will be unavailable to clients of the common time
     * service until the service joins a network (even an empty network).  Devices may use the
     * {@link #forceNetworklessMasterMode()} method to force a time service in the INITIAL state
     * with no network configuration to assume MASTER status for a brand new timeline in order to
     * allow clients of the common time service to operate, even though the device is isolated and
     * not on any network.  When a networkless master does join a network, it will defer to any
     * masters already on the network, or continue to maintain the timeline it made up during its
     * networkless state if no other masters are detected.  Attempting to force a client into master
     * mode while it is actively bound to a network will fail with the status code {@link #ERROR}
     *
     * @return {@link #SUCCESS} in case of success,
    {@link #ERROR} or {@link #ERROR_DEAD_OBJECT} in case of failure.
     */
    public int forceNetworklessMasterMode() {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(mInterfaceDesc);
            mRemote.transact(android.os.CommonTimeConfig.METHOD_FORCE_NETWORKLESS_MASTER_MODE, data, reply, 0);
            return reply.readInt();
        } catch (android.os.RemoteException e) {
            return android.os.CommonTimeConfig.ERROR_DEAD_OBJECT;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    /**
     * The OnServerDiedListener interface defines a method called by the
     * {@link android.os.CommonTimeConfig} instance to indicate that the connection to the native
     * media server has been broken and that the {@link android.os.CommonTimeConfig} instance will
     * need to be released and re-created.  The client application can implement this interface and
     * register the listener with the {@link #setServerDiedListener(OnServerDiedListener)} method.
     */
    public interface OnServerDiedListener {
        /**
         * Method called when the native common time service has died.  <p>If the native common time
         * service encounters a fatal error and needs to restart, the binder connection from the
         * {@link android.os.CommonTimeConfig} instance to the common time service will be broken.
         */
        void onServerDied();
    }

    /**
     * Registers an OnServerDiedListener interface.
     * <p>Call this method with a null listener to stop receiving server death notifications.
     */
    public void setServerDiedListener(android.os.CommonTimeConfig.OnServerDiedListener listener) {
        synchronized(mListenerLock) {
            mServerDiedListener = listener;
        }
    }

    protected void finalize() throws java.lang.Throwable {
        release();
    }

    private boolean checkDeadServer() {
        return (null == mRemote) || (null == mUtils);
    }

    private void throwOnDeadServer() throws android.os.RemoteException {
        if (checkDeadServer())
            throw new android.os.RemoteException();

    }

    private final java.lang.Object mListenerLock = new java.lang.Object();

    private android.os.CommonTimeConfig.OnServerDiedListener mServerDiedListener = null;

    private android.os.IBinder mRemote = null;

    private java.lang.String mInterfaceDesc = "";

    private android.os.CommonTimeUtils mUtils;

    private android.os.IBinder.DeathRecipient mDeathHandler = new android.os.IBinder.DeathRecipient() {
        public void binderDied() {
            synchronized(mListenerLock) {
                if (null != mServerDiedListener)
                    mServerDiedListener.onServerDied();

            }
        }
    };

    private static final int METHOD_GET_MASTER_ELECTION_PRIORITY = android.os.IBinder.FIRST_CALL_TRANSACTION;

    private static final int METHOD_SET_MASTER_ELECTION_PRIORITY = android.os.CommonTimeConfig.METHOD_GET_MASTER_ELECTION_PRIORITY + 1;

    private static final int METHOD_GET_MASTER_ELECTION_ENDPOINT = android.os.CommonTimeConfig.METHOD_SET_MASTER_ELECTION_PRIORITY + 1;

    private static final int METHOD_SET_MASTER_ELECTION_ENDPOINT = android.os.CommonTimeConfig.METHOD_GET_MASTER_ELECTION_ENDPOINT + 1;

    private static final int METHOD_GET_MASTER_ELECTION_GROUP_ID = android.os.CommonTimeConfig.METHOD_SET_MASTER_ELECTION_ENDPOINT + 1;

    private static final int METHOD_SET_MASTER_ELECTION_GROUP_ID = android.os.CommonTimeConfig.METHOD_GET_MASTER_ELECTION_GROUP_ID + 1;

    private static final int METHOD_GET_INTERFACE_BINDING = android.os.CommonTimeConfig.METHOD_SET_MASTER_ELECTION_GROUP_ID + 1;

    private static final int METHOD_SET_INTERFACE_BINDING = android.os.CommonTimeConfig.METHOD_GET_INTERFACE_BINDING + 1;

    private static final int METHOD_GET_MASTER_ANNOUNCE_INTERVAL = android.os.CommonTimeConfig.METHOD_SET_INTERFACE_BINDING + 1;

    private static final int METHOD_SET_MASTER_ANNOUNCE_INTERVAL = android.os.CommonTimeConfig.METHOD_GET_MASTER_ANNOUNCE_INTERVAL + 1;

    private static final int METHOD_GET_CLIENT_SYNC_INTERVAL = android.os.CommonTimeConfig.METHOD_SET_MASTER_ANNOUNCE_INTERVAL + 1;

    private static final int METHOD_SET_CLIENT_SYNC_INTERVAL = android.os.CommonTimeConfig.METHOD_GET_CLIENT_SYNC_INTERVAL + 1;

    private static final int METHOD_GET_PANIC_THRESHOLD = android.os.CommonTimeConfig.METHOD_SET_CLIENT_SYNC_INTERVAL + 1;

    private static final int METHOD_SET_PANIC_THRESHOLD = android.os.CommonTimeConfig.METHOD_GET_PANIC_THRESHOLD + 1;

    private static final int METHOD_GET_AUTO_DISABLE = android.os.CommonTimeConfig.METHOD_SET_PANIC_THRESHOLD + 1;

    private static final int METHOD_SET_AUTO_DISABLE = android.os.CommonTimeConfig.METHOD_GET_AUTO_DISABLE + 1;

    private static final int METHOD_FORCE_NETWORKLESS_MASTER_MODE = android.os.CommonTimeConfig.METHOD_SET_AUTO_DISABLE + 1;
}

