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
 * This class provides the APIs to control the Bluetooth Pan
 * Profile.
 *
 * <p>BluetoothPan is a proxy object for controlling the Bluetooth
 * Service via IPC. Use {@link BluetoothAdapter#getProfileProxy} to get
 * the BluetoothPan proxy object.
 *
 * <p>Each method is protected with its appropriate permission.
 *
 * @unknown 
 */
public final class BluetoothPan implements android.bluetooth.BluetoothProfile {
    private static final java.lang.String TAG = "BluetoothPan";

    private static final boolean DBG = true;

    private static final boolean VDBG = false;

    /**
     * Intent used to broadcast the change in connection state of the Pan
     * profile.
     *
     * <p>This intent will have 4 extras:
     * <ul>
     *   <li> {@link #EXTRA_STATE} - The current state of the profile. </li>
     *   <li> {@link #EXTRA_PREVIOUS_STATE}- The previous state of the profile.</li>
     *   <li> {@link BluetoothDevice#EXTRA_DEVICE} - The remote device. </li>
     *   <li> {@link #EXTRA_LOCAL_ROLE} - Which local role the remote device is
     *   bound to. </li>
     * </ul>
     *
     * <p>{@link #EXTRA_STATE} or {@link #EXTRA_PREVIOUS_STATE} can be any of
     * {@link #STATE_DISCONNECTED}, {@link #STATE_CONNECTING},
     * {@link #STATE_CONNECTED}, {@link #STATE_DISCONNECTING}.
     *
     * <p> {@link #EXTRA_LOCAL_ROLE} can be one of {@link #LOCAL_NAP_ROLE} or
     * {@link #LOCAL_PANU_ROLE}
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission to
     * receive.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.pan.profile.action.CONNECTION_STATE_CHANGED";

    /**
     * Extra for {@link #ACTION_CONNECTION_STATE_CHANGED} intent
     * The local role of the PAN profile that the remote device is bound to.
     * It can be one of {@link #LOCAL_NAP_ROLE} or {@link #LOCAL_PANU_ROLE}.
     */
    public static final java.lang.String EXTRA_LOCAL_ROLE = "android.bluetooth.pan.extra.LOCAL_ROLE";

    public static final int PAN_ROLE_NONE = 0;

    /**
     * The local device is acting as a Network Access Point.
     */
    public static final int LOCAL_NAP_ROLE = 1;

    public static final int REMOTE_NAP_ROLE = 1;

    /**
     * The local device is acting as a PAN User.
     */
    public static final int LOCAL_PANU_ROLE = 2;

    public static final int REMOTE_PANU_ROLE = 2;

    /**
     * Return codes for the connect and disconnect Bluez / Dbus calls.
     *
     * @unknown 
     */
    public static final int PAN_DISCONNECT_FAILED_NOT_CONNECTED = 1000;

    /**
     *
     *
     * @unknown 
     */
    public static final int PAN_CONNECT_FAILED_ALREADY_CONNECTED = 1001;

    /**
     *
     *
     * @unknown 
     */
    public static final int PAN_CONNECT_FAILED_ATTEMPT_FAILED = 1002;

    /**
     *
     *
     * @unknown 
     */
    public static final int PAN_OPERATION_GENERIC_FAILURE = 1003;

    /**
     *
     *
     * @unknown 
     */
    public static final int PAN_OPERATION_SUCCESS = 1004;

    private android.content.Context mContext;

    private android.bluetooth.BluetoothProfile.ServiceListener mServiceListener;

    private android.bluetooth.BluetoothAdapter mAdapter;

    private android.bluetooth.IBluetoothPan mPanService;

    /**
     * Create a BluetoothPan proxy object for interacting with the local
     * Bluetooth Service which handles the Pan profile
     */
    /* package */
    BluetoothPan(android.content.Context context, android.bluetooth.BluetoothProfile.ServiceListener l) {
        mContext = context;
        mServiceListener = l;
        mAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        try {
            mAdapter.getBluetoothManager().registerStateChangeCallback(mStateChangeCallback);
        } catch (android.os.RemoteException re) {
            android.util.Log.w(android.bluetooth.BluetoothPan.TAG, "Unable to register BluetoothStateChangeCallback", re);
        }
        if (android.bluetooth.BluetoothPan.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothPan.TAG, "BluetoothPan() call bindService");

        doBind();
    }

    boolean doBind() {
        android.content.Intent intent = new android.content.Intent(android.bluetooth.IBluetoothPan.class.getName());
        android.content.ComponentName comp = intent.resolveSystemService(mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if ((comp == null) || (!mContext.bindServiceAsUser(intent, mConnection, 0, android.os.Process.myUserHandle()))) {
            android.util.Log.e(android.bluetooth.BluetoothPan.TAG, "Could not bind to Bluetooth Pan Service with " + intent);
            return false;
        }
        return true;
    }

    /* package */
    void close() {
        if (android.bluetooth.BluetoothPan.VDBG)
            android.bluetooth.BluetoothPan.log("close()");

        android.bluetooth.IBluetoothManager mgr = mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(mStateChangeCallback);
            } catch (android.os.RemoteException re) {
                android.util.Log.w(android.bluetooth.BluetoothPan.TAG, "Unable to unregister BluetoothStateChangeCallback", re);
            }
        }
        synchronized(mConnection) {
            if (mPanService != null) {
                try {
                    mPanService = null;
                    mContext.unbindService(mConnection);
                } catch (java.lang.Exception re) {
                    android.util.Log.e(android.bluetooth.BluetoothPan.TAG, "", re);
                }
            }
        }
        mServiceListener = null;
    }

    protected void finalize() {
        close();
    }

    private final android.bluetooth.IBluetoothStateChangeCallback mStateChangeCallback = new android.bluetooth.IBluetoothStateChangeCallback.Stub() {
        @java.lang.Override
        public void onBluetoothStateChange(boolean on) {
            // Handle enable request to bind again.
            android.util.Log.d(android.bluetooth.BluetoothPan.TAG, "onBluetoothStateChange on: " + on);
            if (on) {
                try {
                    if (mPanService == null) {
                        if (android.bluetooth.BluetoothPan.VDBG)
                            android.util.Log.d(android.bluetooth.BluetoothPan.TAG, "onBluetoothStateChange calling doBind()");

                        doBind();
                    }
                } catch (java.lang.IllegalStateException e) {
                    android.util.Log.e(android.bluetooth.BluetoothPan.TAG, "onBluetoothStateChange: could not bind to PAN service: ", e);
                } catch (java.lang.SecurityException e) {
                    android.util.Log.e(android.bluetooth.BluetoothPan.TAG, "onBluetoothStateChange: could not bind to PAN service: ", e);
                }
            } else {
                if (android.bluetooth.BluetoothPan.VDBG)
                    android.util.Log.d(android.bluetooth.BluetoothPan.TAG, "Unbinding service...");

                synchronized(mConnection) {
                    try {
                        mPanService = null;
                        mContext.unbindService(mConnection);
                    } catch (java.lang.Exception re) {
                        android.util.Log.e(android.bluetooth.BluetoothPan.TAG, "", re);
                    }
                }
            }
        }
    };

    /**
     * Initiate connection to a profile of the remote bluetooth device.
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
        if (android.bluetooth.BluetoothPan.DBG)
            android.bluetooth.BluetoothPan.log(("connect(" + device) + ")");

        if (((mPanService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mPanService.connect(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPan.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mPanService == null)
            android.util.Log.w(android.bluetooth.BluetoothPan.TAG, "Proxy not attached to service");

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
        if (android.bluetooth.BluetoothPan.DBG)
            android.bluetooth.BluetoothPan.log(("disconnect(" + device) + ")");

        if (((mPanService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mPanService.disconnect(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPan.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mPanService == null)
            android.util.Log.w(android.bluetooth.BluetoothPan.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * {@inheritDoc }
     */
    public java.util.List<android.bluetooth.BluetoothDevice> getConnectedDevices() {
        if (android.bluetooth.BluetoothPan.VDBG)
            android.bluetooth.BluetoothPan.log("getConnectedDevices()");

        if ((mPanService != null) && isEnabled()) {
            try {
                return mPanService.getConnectedDevices();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPan.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
            }
        }
        if (mPanService == null)
            android.util.Log.w(android.bluetooth.BluetoothPan.TAG, "Proxy not attached to service");

        return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
    }

    /**
     * {@inheritDoc }
     */
    public java.util.List<android.bluetooth.BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        if (android.bluetooth.BluetoothPan.VDBG)
            android.bluetooth.BluetoothPan.log("getDevicesMatchingStates()");

        if ((mPanService != null) && isEnabled()) {
            try {
                return mPanService.getDevicesMatchingConnectionStates(states);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPan.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
            }
        }
        if (mPanService == null)
            android.util.Log.w(android.bluetooth.BluetoothPan.TAG, "Proxy not attached to service");

        return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
    }

    /**
     * {@inheritDoc }
     */
    public int getConnectionState(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothPan.VDBG)
            android.bluetooth.BluetoothPan.log(("getState(" + device) + ")");

        if (((mPanService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mPanService.getConnectionState(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPan.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
            }
        }
        if (mPanService == null)
            android.util.Log.w(android.bluetooth.BluetoothPan.TAG, "Proxy not attached to service");

        return android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
    }

    public void setBluetoothTethering(boolean value) {
        if (android.bluetooth.BluetoothPan.DBG)
            android.bluetooth.BluetoothPan.log(("setBluetoothTethering(" + value) + ")");

        if ((mPanService != null) && isEnabled()) {
            try {
                mPanService.setBluetoothTethering(value);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPan.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
    }

    public boolean isTetheringOn() {
        if (android.bluetooth.BluetoothPan.VDBG)
            android.bluetooth.BluetoothPan.log("isTetheringOn()");

        if ((mPanService != null) && isEnabled()) {
            try {
                return mPanService.isTetheringOn();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPan.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
            }
        }
        return false;
    }

    private final android.content.ServiceConnection mConnection = new android.content.ServiceConnection() {
        public void onServiceConnected(android.content.ComponentName className, android.os.IBinder service) {
            if (android.bluetooth.BluetoothPan.DBG)
                android.util.Log.d(android.bluetooth.BluetoothPan.TAG, "BluetoothPAN Proxy object connected");

            mPanService = IBluetoothPan.Stub.asInterface(service);
            if (mServiceListener != null) {
                mServiceListener.onServiceConnected(android.bluetooth.BluetoothProfile.PAN, android.bluetooth.BluetoothPan.this);
            }
        }

        public void onServiceDisconnected(android.content.ComponentName className) {
            if (android.bluetooth.BluetoothPan.DBG)
                android.util.Log.d(android.bluetooth.BluetoothPan.TAG, "BluetoothPAN Proxy object disconnected");

            mPanService = null;
            if (mServiceListener != null) {
                mServiceListener.onServiceDisconnected(android.bluetooth.BluetoothProfile.PAN);
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
        android.util.Log.d(android.bluetooth.BluetoothPan.TAG, msg);
    }
}

