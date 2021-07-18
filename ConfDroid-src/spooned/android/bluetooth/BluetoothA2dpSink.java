/**
 * Copyright (C) 2014 The Android Open Source Project
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
 * This class provides the public APIs to control the Bluetooth A2DP Sink
 * profile.
 *
 * <p>BluetoothA2dpSink is a proxy object for controlling the Bluetooth A2DP Sink
 * Service via IPC. Use {@link BluetoothAdapter#getProfileProxy} to get
 * the BluetoothA2dpSink proxy object.
 *
 * @unknown 
 */
public final class BluetoothA2dpSink implements android.bluetooth.BluetoothProfile {
    private static final java.lang.String TAG = "BluetoothA2dpSink";

    private static final boolean DBG = true;

    private static final boolean VDBG = false;

    /**
     * Intent used to broadcast the change in connection state of the A2DP Sink
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
    public static final java.lang.String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.a2dp-sink.profile.action.CONNECTION_STATE_CHANGED";

    /**
     * Intent used to broadcast the change in the Playing state of the A2DP Sink
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
    public static final java.lang.String ACTION_PLAYING_STATE_CHANGED = "android.bluetooth.a2dp-sink.profile.action.PLAYING_STATE_CHANGED";

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

    /**
     * Intent used to broadcast the change in the Playing state of the A2DP Sink
     * profile.
     *
     * <p>This intent will have 3 extras:
     * <ul>
     *   <li> {@link #EXTRA_AUDIO_CONFIG} - The audio configuration for the remote device. </li>
     *   <li> {@link BluetoothDevice#EXTRA_DEVICE} - The remote device. </li>
     * </ul>
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission to
     * receive.
     */
    public static final java.lang.String ACTION_AUDIO_CONFIG_CHANGED = "android.bluetooth.a2dp-sink.profile.action.AUDIO_CONFIG_CHANGED";

    /**
     * Extra for the {@link #ACTION_AUDIO_CONFIG_CHANGED} intent.
     *
     * This extra represents the current audio configuration of the A2DP source device.
     * {@see BluetoothAudioConfig}
     */
    public static final java.lang.String EXTRA_AUDIO_CONFIG = "android.bluetooth.a2dp-sink.profile.extra.AUDIO_CONFIG";

    private android.content.Context mContext;

    private android.bluetooth.BluetoothProfile.ServiceListener mServiceListener;

    private android.bluetooth.IBluetoothA2dpSink mService;

    private android.bluetooth.BluetoothAdapter mAdapter;

    private final android.bluetooth.IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new android.bluetooth.IBluetoothStateChangeCallback.Stub() {
        public void onBluetoothStateChange(boolean up) {
            if (android.bluetooth.BluetoothA2dpSink.DBG)
                android.util.Log.d(android.bluetooth.BluetoothA2dpSink.TAG, "onBluetoothStateChange: up=" + up);

            if (!up) {
                if (android.bluetooth.BluetoothA2dpSink.VDBG)
                    android.util.Log.d(android.bluetooth.BluetoothA2dpSink.TAG, "Unbinding service...");

                synchronized(mConnection) {
                    try {
                        mService = null;
                        mContext.unbindService(mConnection);
                    } catch (java.lang.Exception re) {
                        android.util.Log.e(android.bluetooth.BluetoothA2dpSink.TAG, "", re);
                    }
                }
            } else {
                synchronized(mConnection) {
                    try {
                        if (mService == null) {
                            if (android.bluetooth.BluetoothA2dpSink.VDBG)
                                android.util.Log.d(android.bluetooth.BluetoothA2dpSink.TAG, "Binding service...");

                            doBind();
                        }
                    } catch (java.lang.Exception re) {
                        android.util.Log.e(android.bluetooth.BluetoothA2dpSink.TAG, "", re);
                    }
                }
            }
        }
    };

    /**
     * Create a BluetoothA2dp proxy object for interacting with the local
     * Bluetooth A2DP service.
     */
    /* package */
    BluetoothA2dpSink(android.content.Context context, android.bluetooth.BluetoothProfile.ServiceListener l) {
        mContext = context;
        mServiceListener = l;
        mAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        android.bluetooth.IBluetoothManager mgr = mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(mBluetoothStateChangeCallback);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothA2dpSink.TAG, "", e);
            }
        }
        doBind();
    }

    boolean doBind() {
        android.content.Intent intent = new android.content.Intent(android.bluetooth.IBluetoothA2dpSink.class.getName());
        android.content.ComponentName comp = intent.resolveSystemService(mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if ((comp == null) || (!mContext.bindServiceAsUser(intent, mConnection, 0, android.os.Process.myUserHandle()))) {
            android.util.Log.e(android.bluetooth.BluetoothA2dpSink.TAG, "Could not bind to Bluetooth A2DP Service with " + intent);
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
                android.util.Log.e(android.bluetooth.BluetoothA2dpSink.TAG, "", e);
            }
        }
        synchronized(mConnection) {
            if (mService != null) {
                try {
                    mService = null;
                    mContext.unbindService(mConnection);
                } catch (java.lang.Exception re) {
                    android.util.Log.e(android.bluetooth.BluetoothA2dpSink.TAG, "", re);
                }
            }
        }
    }

    public void finalize() {
        close();
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
        if (android.bluetooth.BluetoothA2dpSink.DBG)
            android.bluetooth.BluetoothA2dpSink.log(("connect(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.connect(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothA2dpSink.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothA2dpSink.TAG, "Proxy not attached to service");

        return false;
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
        if (android.bluetooth.BluetoothA2dpSink.DBG)
            android.bluetooth.BluetoothA2dpSink.log(("disconnect(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.disconnect(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothA2dpSink.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothA2dpSink.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * {@inheritDoc }
     */
    public java.util.List<android.bluetooth.BluetoothDevice> getConnectedDevices() {
        if (android.bluetooth.BluetoothA2dpSink.VDBG)
            android.bluetooth.BluetoothA2dpSink.log("getConnectedDevices()");

        if ((mService != null) && isEnabled()) {
            try {
                return mService.getConnectedDevices();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothA2dpSink.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothA2dpSink.TAG, "Proxy not attached to service");

        return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
    }

    /**
     * {@inheritDoc }
     */
    public java.util.List<android.bluetooth.BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        if (android.bluetooth.BluetoothA2dpSink.VDBG)
            android.bluetooth.BluetoothA2dpSink.log("getDevicesMatchingStates()");

        if ((mService != null) && isEnabled()) {
            try {
                return mService.getDevicesMatchingConnectionStates(states);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothA2dpSink.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothA2dpSink.TAG, "Proxy not attached to service");

        return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
    }

    /**
     * {@inheritDoc }
     */
    public int getConnectionState(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothA2dpSink.VDBG)
            android.bluetooth.BluetoothA2dpSink.log(("getState(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.getConnectionState(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothA2dpSink.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothA2dpSink.TAG, "Proxy not attached to service");

        return android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
    }

    /**
     * Get the current audio configuration for the A2DP source device,
     * or null if the device has no audio configuration
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param device
     * 		Remote bluetooth device.
     * @return audio configuration for the device, or null

    {@see BluetoothAudioConfig}
     */
    public android.bluetooth.BluetoothAudioConfig getAudioConfig(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothA2dpSink.VDBG)
            android.bluetooth.BluetoothA2dpSink.log(("getAudioConfig(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.getAudioConfig(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothA2dpSink.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return null;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothA2dpSink.TAG, "Proxy not attached to service");

        return null;
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
        if (android.bluetooth.BluetoothA2dpSink.DBG)
            android.bluetooth.BluetoothA2dpSink.log(((("setPriority(" + device) + ", ") + priority) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            if ((priority != android.bluetooth.BluetoothProfile.PRIORITY_OFF) && (priority != android.bluetooth.BluetoothProfile.PRIORITY_ON)) {
                return false;
            }
            try {
                return mService.setPriority(device, priority);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothA2dpSink.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothA2dpSink.TAG, "Proxy not attached to service");

        return false;
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
    public int getPriority(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothA2dpSink.VDBG)
            android.bluetooth.BluetoothA2dpSink.log(("getPriority(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.getPriority(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothA2dpSink.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return android.bluetooth.BluetoothProfile.PRIORITY_OFF;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothA2dpSink.TAG, "Proxy not attached to service");

        return android.bluetooth.BluetoothProfile.PRIORITY_OFF;
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
        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.isA2dpPlaying(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothA2dpSink.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothA2dpSink.TAG, "Proxy not attached to service");

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
            case android.bluetooth.BluetoothA2dpSink.STATE_PLAYING :
                return "playing";
            case android.bluetooth.BluetoothA2dpSink.STATE_NOT_PLAYING :
                return "not playing";
            default :
                return ("<unknown state " + state) + ">";
        }
    }

    private final android.content.ServiceConnection mConnection = new android.content.ServiceConnection() {
        public void onServiceConnected(android.content.ComponentName className, android.os.IBinder service) {
            if (android.bluetooth.BluetoothA2dpSink.DBG)
                android.util.Log.d(android.bluetooth.BluetoothA2dpSink.TAG, "Proxy object connected");

            mService = IBluetoothA2dpSink.Stub.asInterface(service);
            if (mServiceListener != null) {
                mServiceListener.onServiceConnected(android.bluetooth.BluetoothProfile.A2DP_SINK, android.bluetooth.BluetoothA2dpSink.this);
            }
        }

        public void onServiceDisconnected(android.content.ComponentName className) {
            if (android.bluetooth.BluetoothA2dpSink.DBG)
                android.util.Log.d(android.bluetooth.BluetoothA2dpSink.TAG, "Proxy object disconnected");

            mService = null;
            if (mServiceListener != null) {
                mServiceListener.onServiceDisconnected(android.bluetooth.BluetoothProfile.A2DP_SINK);
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
        android.util.Log.d(android.bluetooth.BluetoothA2dpSink.TAG, msg);
    }
}

