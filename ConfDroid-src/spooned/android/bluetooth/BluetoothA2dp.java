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
package android.bluetooth;


/**
 * This class provides the public APIs to control the Bluetooth A2DP
 * profile.
 *
 * <p>BluetoothA2dp is a proxy object for controlling the Bluetooth A2DP
 * Service via IPC. Use {@link BluetoothAdapter#getProfileProxy} to get
 * the BluetoothA2dp proxy object.
 *
 * <p> Android only supports one connected Bluetooth A2dp device at a time.
 * Each method is protected with its appropriate permission.
 */
public final class BluetoothA2dp implements android.bluetooth.BluetoothProfile {
    private static final java.lang.String TAG = "BluetoothA2dp";

    private static final boolean DBG = true;

    private static final boolean VDBG = false;

    /**
     * Intent used to broadcast the change in connection state of the A2DP
     * profile.
     *
     * <p>This intent will have 3 extras:
     * <ul>
     *   <li> {@link #EXTRA_STATE} - The current state of the profile. </li>
     *   <li> {@link #EXTRA_PREVIOUS_STATE}- The previous state of the profile.</li>
     *   <li> {@link BluetoothDevice#EXTRA_DEVICE} - The remote device. </li>
     * </ul>
     *
     * <p>{@link #EXTRA_STATE} or {@link #EXTRA_PREVIOUS_STATE} can be any of
     * {@link #STATE_DISCONNECTED}, {@link #STATE_CONNECTING},
     * {@link #STATE_CONNECTED}, {@link #STATE_DISCONNECTING}.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission to
     * receive.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED";

    /**
     * Intent used to broadcast the change in the Playing state of the A2DP
     * profile.
     *
     * <p>This intent will have 3 extras:
     * <ul>
     *   <li> {@link #EXTRA_STATE} - The current state of the profile. </li>
     *   <li> {@link #EXTRA_PREVIOUS_STATE}- The previous state of the profile. </li>
     *   <li> {@link BluetoothDevice#EXTRA_DEVICE} - The remote device. </li>
     * </ul>
     *
     * <p>{@link #EXTRA_STATE} or {@link #EXTRA_PREVIOUS_STATE} can be any of
     * {@link #STATE_PLAYING}, {@link #STATE_NOT_PLAYING},
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission to
     * receive.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String ACTION_PLAYING_STATE_CHANGED = "android.bluetooth.a2dp.profile.action.PLAYING_STATE_CHANGED";

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String ACTION_AVRCP_CONNECTION_STATE_CHANGED = "android.bluetooth.a2dp.profile.action.AVRCP_CONNECTION_STATE_CHANGED";

    /**
     * A2DP sink device is streaming music. This state can be one of
     * {@link #EXTRA_STATE} or {@link #EXTRA_PREVIOUS_STATE} of
     * {@link #ACTION_PLAYING_STATE_CHANGED} intent.
     */
    public static final int STATE_PLAYING = 10;

    /**
     * A2DP sink device is NOT streaming music. This state can be one of
     * {@link #EXTRA_STATE} or {@link #EXTRA_PREVIOUS_STATE} of
     * {@link #ACTION_PLAYING_STATE_CHANGED} intent.
     */
    public static final int STATE_NOT_PLAYING = 11;

    private android.content.Context mContext;

    private android.bluetooth.BluetoothProfile.ServiceListener mServiceListener;

    private final java.util.concurrent.locks.ReentrantReadWriteLock mServiceLock = new java.util.concurrent.locks.ReentrantReadWriteLock();

    @com.android.internal.annotations.GuardedBy("mServiceLock")
    private android.bluetooth.IBluetoothA2dp mService;

    private android.bluetooth.BluetoothAdapter mAdapter;

    private final android.bluetooth.IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new android.bluetooth.IBluetoothStateChangeCallback.Stub() {
        public void onBluetoothStateChange(boolean up) {
            if (android.bluetooth.BluetoothA2dp.DBG)
                android.util.Log.d(android.bluetooth.BluetoothA2dp.TAG, "onBluetoothStateChange: up=" + up);

            if (!up) {
                if (android.bluetooth.BluetoothA2dp.VDBG)
                    android.util.Log.d(android.bluetooth.BluetoothA2dp.TAG, "Unbinding service...");

                try {
                    mServiceLock.writeLock().lock();
                    mService = null;
                    mContext.unbindService(mConnection);
                } catch (java.lang.Exception re) {
                    android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "", re);
                } finally {
                    mServiceLock.writeLock().unlock();
                }
            } else {
                try {
                    mServiceLock.readLock().lock();
                    if (mService == null) {
                        if (android.bluetooth.BluetoothA2dp.VDBG)
                            android.util.Log.d(android.bluetooth.BluetoothA2dp.TAG, "Binding service...");

                        doBind();
                    }
                } catch (java.lang.Exception re) {
                    android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "", re);
                } finally {
                    mServiceLock.readLock().unlock();
                }
            }
        }
    };

    /**
     * Create a BluetoothA2dp proxy object for interacting with the local
     * Bluetooth A2DP service.
     */
    /* package */
    BluetoothA2dp(android.content.Context context, android.bluetooth.BluetoothProfile.ServiceListener l) {
        mContext = context;
        mServiceListener = l;
        mAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        android.bluetooth.IBluetoothManager mgr = mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(mBluetoothStateChangeCallback);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "", e);
            }
        }
        doBind();
    }

    boolean doBind() {
        android.content.Intent intent = new android.content.Intent(android.bluetooth.IBluetoothA2dp.class.getName());
        android.content.ComponentName comp = intent.resolveSystemService(mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if ((comp == null) || (!mContext.bindServiceAsUser(intent, mConnection, 0, android.os.Process.myUserHandle()))) {
            android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "Could not bind to Bluetooth A2DP Service with " + intent);
            return false;
        }
        return true;
    }

    /* package */
    void close() {
        mServiceListener = null;
        android.bluetooth.IBluetoothManager mgr = mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(mBluetoothStateChangeCallback);
            } catch (java.lang.Exception e) {
                android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "", e);
            }
        }
        try {
            mServiceLock.writeLock().lock();
            if (mService != null) {
                mService = null;
                mContext.unbindService(mConnection);
            }
        } catch (java.lang.Exception re) {
            android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "", re);
        } finally {
            mServiceLock.writeLock().unlock();
        }
    }

    public void finalize() {
        // The empty finalize needs to be kept or the
        // cts signature tests would fail.
    }

    /**
     * Initiate connection to a profile of the remote bluetooth device.
     *
     * <p> Currently, the system supports only 1 connection to the
     * A2DP profile. The API will automatically disconnect connected
     * devices before connecting.
     *
     * <p> This API returns false in scenarios like the profile on the
     * device is already connected or Bluetooth is not turned on.
     * When this API returns true, it is guaranteed that
     * connection state intent for the profile will be broadcasted with
     * the state. Users can get the connection state of the profile
     * from this intent.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN}
     * permission.
     *
     * @param device
     * 		Remote Bluetooth Device
     * @return false on immediate error,
    true otherwise
     * @unknown 
     */
    public boolean connect(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothA2dp.DBG)
            android.bluetooth.BluetoothA2dp.log(("connect(" + device) + ")");

        try {
            mServiceLock.readLock().lock();
            if (((mService != null) && isEnabled()) && isValidDevice(device)) {
                return mService.connect(device);
            }
            if (mService == null)
                android.util.Log.w(android.bluetooth.BluetoothA2dp.TAG, "Proxy not attached to service");

            return false;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
            return false;
        } finally {
            mServiceLock.readLock().unlock();
        }
    }

    /**
     * Initiate disconnection from a profile
     *
     * <p> This API will return false in scenarios like the profile on the
     * Bluetooth device is not in connected state etc. When this API returns,
     * true, it is guaranteed that the connection state change
     * intent will be broadcasted with the state. Users can get the
     * disconnection state of the profile from this intent.
     *
     * <p> If the disconnection is initiated by a remote device, the state
     * will transition from {@link #STATE_CONNECTED} to
     * {@link #STATE_DISCONNECTED}. If the disconnect is initiated by the
     * host (local) device the state will transition from
     * {@link #STATE_CONNECTED} to state {@link #STATE_DISCONNECTING} to
     * state {@link #STATE_DISCONNECTED}. The transition to
     * {@link #STATE_DISCONNECTING} can be used to distinguish between the
     * two scenarios.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN}
     * permission.
     *
     * @param device
     * 		Remote Bluetooth Device
     * @return false on immediate error,
    true otherwise
     * @unknown 
     */
    public boolean disconnect(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothA2dp.DBG)
            android.bluetooth.BluetoothA2dp.log(("disconnect(" + device) + ")");

        try {
            mServiceLock.readLock().lock();
            if (((mService != null) && isEnabled()) && isValidDevice(device)) {
                return mService.disconnect(device);
            }
            if (mService == null)
                android.util.Log.w(android.bluetooth.BluetoothA2dp.TAG, "Proxy not attached to service");

            return false;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
            return false;
        } finally {
            mServiceLock.readLock().unlock();
        }
    }

    /**
     * {@inheritDoc }
     */
    public java.util.List<android.bluetooth.BluetoothDevice> getConnectedDevices() {
        if (android.bluetooth.BluetoothA2dp.VDBG)
            android.bluetooth.BluetoothA2dp.log("getConnectedDevices()");

        try {
            mServiceLock.readLock().lock();
            if ((mService != null) && isEnabled()) {
                return mService.getConnectedDevices();
            }
            if (mService == null)
                android.util.Log.w(android.bluetooth.BluetoothA2dp.TAG, "Proxy not attached to service");

            return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
            return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
        } finally {
            mServiceLock.readLock().unlock();
        }
    }

    /**
     * {@inheritDoc }
     */
    public java.util.List<android.bluetooth.BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        if (android.bluetooth.BluetoothA2dp.VDBG)
            android.bluetooth.BluetoothA2dp.log("getDevicesMatchingStates()");

        try {
            mServiceLock.readLock().lock();
            if ((mService != null) && isEnabled()) {
                return mService.getDevicesMatchingConnectionStates(states);
            }
            if (mService == null)
                android.util.Log.w(android.bluetooth.BluetoothA2dp.TAG, "Proxy not attached to service");

            return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
            return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
        } finally {
            mServiceLock.readLock().unlock();
        }
    }

    /**
     * {@inheritDoc }
     */
    public int getConnectionState(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothA2dp.VDBG)
            android.bluetooth.BluetoothA2dp.log(("getState(" + device) + ")");

        try {
            mServiceLock.readLock().lock();
            if (((mService != null) && isEnabled()) && isValidDevice(device)) {
                return mService.getConnectionState(device);
            }
            if (mService == null)
                android.util.Log.w(android.bluetooth.BluetoothA2dp.TAG, "Proxy not attached to service");

            return android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
            return android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
        } finally {
            mServiceLock.readLock().unlock();
        }
    }

    /**
     * Set priority of the profile
     *
     * <p> The device should already be paired.
     *  Priority can be one of {@link #PRIORITY_ON} orgetBluetoothManager
     * {@link #PRIORITY_OFF},
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN}
     * permission.
     *
     * @param device
     * 		Paired bluetooth device
     * @param priority
     * 		
     * @return true if priority is set, false on error
     * @unknown 
     */
    public boolean setPriority(android.bluetooth.BluetoothDevice device, int priority) {
        if (android.bluetooth.BluetoothA2dp.DBG)
            android.bluetooth.BluetoothA2dp.log(((("setPriority(" + device) + ", ") + priority) + ")");

        try {
            mServiceLock.readLock().lock();
            if (((mService != null) && isEnabled()) && isValidDevice(device)) {
                if ((priority != android.bluetooth.BluetoothProfile.PRIORITY_OFF) && (priority != android.bluetooth.BluetoothProfile.PRIORITY_ON)) {
                    return false;
                }
                return mService.setPriority(device, priority);
            }
            if (mService == null)
                android.util.Log.w(android.bluetooth.BluetoothA2dp.TAG, "Proxy not attached to service");

            return false;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
            return false;
        } finally {
            mServiceLock.readLock().unlock();
        }
    }

    /**
     * Get the priority of the profile.
     *
     * <p> The priority can be any of:
     * {@link #PRIORITY_AUTO_CONNECT}, {@link #PRIORITY_OFF},
     * {@link #PRIORITY_ON}, {@link #PRIORITY_UNDEFINED}
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param device
     * 		Bluetooth device
     * @return priority of the device
     * @unknown 
     */
    @android.annotation.RequiresPermission(Manifest.permission.BLUETOOTH)
    public int getPriority(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothA2dp.VDBG)
            android.bluetooth.BluetoothA2dp.log(("getPriority(" + device) + ")");

        try {
            mServiceLock.readLock().lock();
            if (((mService != null) && isEnabled()) && isValidDevice(device)) {
                return mService.getPriority(device);
            }
            if (mService == null)
                android.util.Log.w(android.bluetooth.BluetoothA2dp.TAG, "Proxy not attached to service");

            return android.bluetooth.BluetoothProfile.PRIORITY_OFF;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
            return android.bluetooth.BluetoothProfile.PRIORITY_OFF;
        } finally {
            mServiceLock.readLock().unlock();
        }
    }

    /**
     * Checks if Avrcp device supports the absolute volume feature.
     *
     * @return true if device supports absolute volume
     * @unknown 
     */
    public boolean isAvrcpAbsoluteVolumeSupported() {
        if (android.bluetooth.BluetoothA2dp.DBG)
            android.util.Log.d(android.bluetooth.BluetoothA2dp.TAG, "isAvrcpAbsoluteVolumeSupported");

        try {
            mServiceLock.readLock().lock();
            if ((mService != null) && isEnabled()) {
                return mService.isAvrcpAbsoluteVolumeSupported();
            }
            if (mService == null)
                android.util.Log.w(android.bluetooth.BluetoothA2dp.TAG, "Proxy not attached to service");

            return false;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "Error talking to BT service in isAvrcpAbsoluteVolumeSupported()", e);
            return false;
        } finally {
            mServiceLock.readLock().unlock();
        }
    }

    /**
     * Tells remote device to adjust volume. Only if absolute volume is
     * supported. Uses the following values:
     * <ul>
     * <li>{@link AudioManager#ADJUST_LOWER}</li>
     * <li>{@link AudioManager#ADJUST_RAISE}</li>
     * <li>{@link AudioManager#ADJUST_MUTE}</li>
     * <li>{@link AudioManager#ADJUST_UNMUTE}</li>
     * </ul>
     *
     * @param direction
     * 		One of the supported adjust values.
     * @unknown 
     */
    public void adjustAvrcpAbsoluteVolume(int direction) {
        if (android.bluetooth.BluetoothA2dp.DBG)
            android.util.Log.d(android.bluetooth.BluetoothA2dp.TAG, "adjustAvrcpAbsoluteVolume");

        try {
            mServiceLock.readLock().lock();
            if ((mService != null) && isEnabled()) {
                mService.adjustAvrcpAbsoluteVolume(direction);
            }
            if (mService == null)
                android.util.Log.w(android.bluetooth.BluetoothA2dp.TAG, "Proxy not attached to service");

        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "Error talking to BT service in adjustAvrcpAbsoluteVolume()", e);
        } finally {
            mServiceLock.readLock().unlock();
        }
    }

    /**
     * Tells remote device to set an absolute volume. Only if absolute volume is supported
     *
     * @param volume
     * 		Absolute volume to be set on AVRCP side
     * @unknown 
     */
    public void setAvrcpAbsoluteVolume(int volume) {
        if (android.bluetooth.BluetoothA2dp.DBG)
            android.util.Log.d(android.bluetooth.BluetoothA2dp.TAG, "setAvrcpAbsoluteVolume");

        try {
            mServiceLock.readLock().lock();
            if ((mService != null) && isEnabled()) {
                mService.setAvrcpAbsoluteVolume(volume);
            }
            if (mService == null)
                android.util.Log.w(android.bluetooth.BluetoothA2dp.TAG, "Proxy not attached to service");

        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "Error talking to BT service in setAvrcpAbsoluteVolume()", e);
        } finally {
            mServiceLock.readLock().unlock();
        }
    }

    /**
     * Check if A2DP profile is streaming music.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param device
     * 		BluetoothDevice device
     */
    public boolean isA2dpPlaying(android.bluetooth.BluetoothDevice device) {
        try {
            mServiceLock.readLock().lock();
            if (((mService != null) && isEnabled()) && isValidDevice(device)) {
                return mService.isA2dpPlaying(device);
            }
            if (mService == null)
                android.util.Log.w(android.bluetooth.BluetoothA2dp.TAG, "Proxy not attached to service");

            return false;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothA2dp.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
            return false;
        } finally {
            mServiceLock.readLock().unlock();
        }
    }

    /**
     * This function checks if the remote device is an AVCRP
     * target and thus whether we should send volume keys
     * changes or not.
     *
     * @unknown 
     */
    public boolean shouldSendVolumeKeys(android.bluetooth.BluetoothDevice device) {
        if (isEnabled() && isValidDevice(device)) {
            android.os.ParcelUuid[] uuids = device.getUuids();
            if (uuids == null)
                return false;

            for (android.os.ParcelUuid uuid : uuids) {
                if (android.bluetooth.BluetoothUuid.isAvrcpTarget(uuid)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Helper for converting a state to a string.
     *
     * For debug use only - strings are not internationalized.
     *
     * @unknown 
     */
    public static java.lang.String stateToString(int state) {
        switch (state) {
            case android.bluetooth.BluetoothProfile.STATE_DISCONNECTED :
                return "disconnected";
            case android.bluetooth.BluetoothProfile.STATE_CONNECTING :
                return "connecting";
            case android.bluetooth.BluetoothProfile.STATE_CONNECTED :
                return "connected";
            case android.bluetooth.BluetoothProfile.STATE_DISCONNECTING :
                return "disconnecting";
            case android.bluetooth.BluetoothA2dp.STATE_PLAYING :
                return "playing";
            case android.bluetooth.BluetoothA2dp.STATE_NOT_PLAYING :
                return "not playing";
            default :
                return ("<unknown state " + state) + ">";
        }
    }

    private final android.content.ServiceConnection mConnection = new android.content.ServiceConnection() {
        public void onServiceConnected(android.content.ComponentName className, android.os.IBinder service) {
            if (android.bluetooth.BluetoothA2dp.DBG)
                android.util.Log.d(android.bluetooth.BluetoothA2dp.TAG, "Proxy object connected");

            try {
                mServiceLock.writeLock().lock();
                mService = IBluetoothA2dp.Stub.asInterface(service);
            } finally {
                mServiceLock.writeLock().unlock();
            }
            if (mServiceListener != null) {
                mServiceListener.onServiceConnected(android.bluetooth.BluetoothProfile.A2DP, android.bluetooth.BluetoothA2dp.this);
            }
        }

        public void onServiceDisconnected(android.content.ComponentName className) {
            if (android.bluetooth.BluetoothA2dp.DBG)
                android.util.Log.d(android.bluetooth.BluetoothA2dp.TAG, "Proxy object disconnected");

            try {
                mServiceLock.writeLock().lock();
                mService = null;
            } finally {
                mServiceLock.writeLock().unlock();
            }
            if (mServiceListener != null) {
                mServiceListener.onServiceDisconnected(android.bluetooth.BluetoothProfile.A2DP);
            }
        }
    };

    private boolean isEnabled() {
        if (mAdapter.getState() == android.bluetooth.BluetoothAdapter.STATE_ON)
            return true;

        return false;
    }

    private boolean isValidDevice(android.bluetooth.BluetoothDevice device) {
        if (device == null)
            return false;

        if (android.bluetooth.BluetoothAdapter.checkBluetoothAddress(device.getAddress()))
            return true;

        return false;
    }

    private static void log(java.lang.String msg) {
        android.util.Log.d(android.bluetooth.BluetoothA2dp.TAG, msg);
    }
}

