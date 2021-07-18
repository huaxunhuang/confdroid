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
package android.bluetooth;


/**
 * This class provides the APIs to control the Bluetooth PBAP Client Profile.
 *
 * @unknown 
 */
public final class BluetoothPbapClient implements android.bluetooth.BluetoothProfile {
    private static final java.lang.String TAG = "BluetoothPbapClient";

    private static final boolean DBG = false;

    private static final boolean VDBG = false;

    public static final java.lang.String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.pbap.profile.action.CONNECTION_STATE_CHANGED";

    private android.bluetooth.IBluetoothPbapClient mService;

    private final android.content.Context mContext;

    private android.bluetooth.BluetoothProfile.ServiceListener mServiceListener;

    private android.bluetooth.BluetoothAdapter mAdapter;

    /**
     * There was an error trying to obtain the state
     */
    public static final int STATE_ERROR = -1;

    public static final int RESULT_FAILURE = 0;

    public static final int RESULT_SUCCESS = 1;

    /**
     * Connection canceled before completion.
     */
    public static final int RESULT_CANCELED = 2;

    private final android.bluetooth.IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new android.bluetooth.IBluetoothStateChangeCallback.Stub() {
        public void onBluetoothStateChange(boolean up) {
            if (android.bluetooth.BluetoothPbapClient.DBG) {
                android.util.Log.d(android.bluetooth.BluetoothPbapClient.TAG, "onBluetoothStateChange: PBAP CLIENT up=" + up);
            }
            if (!up) {
                if (android.bluetooth.BluetoothPbapClient.VDBG) {
                    android.util.Log.d(android.bluetooth.BluetoothPbapClient.TAG, "Unbinding service...");
                }
                synchronized(mConnection) {
                    try {
                        mService = null;
                        mContext.unbindService(mConnection);
                    } catch (java.lang.Exception re) {
                        android.util.Log.e(android.bluetooth.BluetoothPbapClient.TAG, "", re);
                    }
                }
            } else {
                synchronized(mConnection) {
                    try {
                        if (mService == null) {
                            if (android.bluetooth.BluetoothPbapClient.VDBG) {
                                android.util.Log.d(android.bluetooth.BluetoothPbapClient.TAG, "Binding service...");
                            }
                            doBind();
                        }
                    } catch (java.lang.Exception re) {
                        android.util.Log.e(android.bluetooth.BluetoothPbapClient.TAG, "", re);
                    }
                }
            }
        }
    };

    /**
     * Create a BluetoothPbapClient proxy object.
     */
    BluetoothPbapClient(android.content.Context context, android.bluetooth.BluetoothProfile.ServiceListener l) {
        if (android.bluetooth.BluetoothPbapClient.DBG) {
            android.util.Log.d(android.bluetooth.BluetoothPbapClient.TAG, "Create BluetoothPbapClient proxy object");
        }
        mContext = context;
        mServiceListener = l;
        mAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        android.bluetooth.IBluetoothManager mgr = mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(mBluetoothStateChangeCallback);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPbapClient.TAG, "", e);
            }
        }
        doBind();
    }

    private boolean doBind() {
        android.content.Intent intent = new android.content.Intent(android.bluetooth.IBluetoothPbapClient.class.getName());
        android.content.ComponentName comp = intent.resolveSystemService(mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if ((comp == null) || (!mContext.bindServiceAsUser(intent, mConnection, 0, android.os.Process.myUserHandle()))) {
            android.util.Log.e(android.bluetooth.BluetoothPbapClient.TAG, "Could not bind to Bluetooth PBAP Client Service with " + intent);
            return false;
        }
        return true;
    }

    protected void finalize() throws java.lang.Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    /**
     * Close the connection to the backing service.
     * Other public functions of BluetoothPbapClient will return default error
     * results once close() has been called. Multiple invocations of close()
     * are ok.
     */
    public synchronized void close() {
        android.bluetooth.IBluetoothManager mgr = mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(mBluetoothStateChangeCallback);
            } catch (java.lang.Exception e) {
                android.util.Log.e(android.bluetooth.BluetoothPbapClient.TAG, "", e);
            }
        }
        synchronized(mConnection) {
            if (mService != null) {
                try {
                    mService = null;
                    mContext.unbindService(mConnection);
                } catch (java.lang.Exception re) {
                    android.util.Log.e(android.bluetooth.BluetoothPbapClient.TAG, "", re);
                }
            }
        }
        mServiceListener = null;
    }

    /**
     * Initiate connection.
     * Upon successful connection to remote PBAP server the Client will
     * attempt to automatically download the users phonebook and call log.
     *
     * @param device
     * 		a remote device we want connect to
     * @return <code>true</code> if command has been issued successfully;
    <code>false</code> otherwise;
     */
    public boolean connect(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothPbapClient.DBG) {
            android.bluetooth.BluetoothPbapClient.log(("connect(" + device) + ") for PBAP Client.");
        }
        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.connect(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPbapClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null) {
            android.util.Log.w(android.bluetooth.BluetoothPbapClient.TAG, "Proxy not attached to service");
        }
        return false;
    }

    /**
     * Initiate disconnect.
     *
     * @param device
     * 		Remote Bluetooth Device
     * @return false on error,
    true otherwise
     */
    public boolean disconnect(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothPbapClient.DBG) {
            android.bluetooth.BluetoothPbapClient.log((("disconnect(" + device) + ")") + new java.lang.Exception());
        }
        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                mService.disconnect(device);
                return true;
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPbapClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null) {
            android.util.Log.w(android.bluetooth.BluetoothPbapClient.TAG, "Proxy not attached to service");
        }
        return false;
    }

    /**
     * Get the list of connected devices.
     * Currently at most one.
     *
     * @return list of connected devices
     */
    @java.lang.Override
    public java.util.List<android.bluetooth.BluetoothDevice> getConnectedDevices() {
        if (android.bluetooth.BluetoothPbapClient.DBG) {
            android.bluetooth.BluetoothPbapClient.log("getConnectedDevices()");
        }
        if ((mService != null) && isEnabled()) {
            try {
                return mService.getConnectedDevices();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPbapClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
            }
        }
        if (mService == null) {
            android.util.Log.w(android.bluetooth.BluetoothPbapClient.TAG, "Proxy not attached to service");
        }
        return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
    }

    /**
     * Get the list of devices matching specified states. Currently at most one.
     *
     * @return list of matching devices
     */
    @java.lang.Override
    public java.util.List<android.bluetooth.BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        if (android.bluetooth.BluetoothPbapClient.DBG) {
            android.bluetooth.BluetoothPbapClient.log("getDevicesMatchingStates()");
        }
        if ((mService != null) && isEnabled()) {
            try {
                return mService.getDevicesMatchingConnectionStates(states);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPbapClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
            }
        }
        if (mService == null) {
            android.util.Log.w(android.bluetooth.BluetoothPbapClient.TAG, "Proxy not attached to service");
        }
        return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
    }

    /**
     * Get connection state of device
     *
     * @return device connection state
     */
    @java.lang.Override
    public int getConnectionState(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothPbapClient.DBG) {
            android.bluetooth.BluetoothPbapClient.log(("getConnectionState(" + device) + ")");
        }
        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.getConnectionState(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPbapClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
            }
        }
        if (mService == null) {
            android.util.Log.w(android.bluetooth.BluetoothPbapClient.TAG, "Proxy not attached to service");
        }
        return android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
    }

    private final android.content.ServiceConnection mConnection = new android.content.ServiceConnection() {
        public void onServiceConnected(android.content.ComponentName className, android.os.IBinder service) {
            if (android.bluetooth.BluetoothPbapClient.DBG) {
                android.bluetooth.BluetoothPbapClient.log("Proxy object connected");
            }
            mService = IBluetoothPbapClient.Stub.asInterface(service);
            if (mServiceListener != null) {
                mServiceListener.onServiceConnected(android.bluetooth.BluetoothProfile.PBAP_CLIENT, android.bluetooth.BluetoothPbapClient.this);
            }
        }

        public void onServiceDisconnected(android.content.ComponentName className) {
            if (android.bluetooth.BluetoothPbapClient.DBG) {
                android.bluetooth.BluetoothPbapClient.log("Proxy object disconnected");
            }
            mService = null;
            if (mServiceListener != null) {
                mServiceListener.onServiceDisconnected(android.bluetooth.BluetoothProfile.PBAP_CLIENT);
            }
        }
    };

    private static void log(java.lang.String msg) {
        android.util.Log.d(android.bluetooth.BluetoothPbapClient.TAG, msg);
    }

    private boolean isEnabled() {
        android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if ((adapter != null) && (adapter.getState() == android.bluetooth.BluetoothAdapter.STATE_ON)) {
            return true;
        }
        android.bluetooth.BluetoothPbapClient.log("Bluetooth is Not enabled");
        return false;
    }

    private boolean isValidDevice(android.bluetooth.BluetoothDevice device) {
        if (device == null) {
            return false;
        }
        if (android.bluetooth.BluetoothAdapter.checkBluetoothAddress(device.getAddress())) {
            return true;
        }
        return false;
    }

    /**
     * Set priority of the profile
     *
     * <p> The device should already be paired.
     *  Priority can be one of {@link #PRIORITY_ON} or
     * {@link #PRIORITY_OFF},
     *
     * @param device
     * 		Paired bluetooth device
     * @param priority
     * 		
     * @return true if priority is set, false on error
     */
    public boolean setPriority(android.bluetooth.BluetoothDevice device, int priority) {
        if (android.bluetooth.BluetoothPbapClient.DBG) {
            android.bluetooth.BluetoothPbapClient.log(((("setPriority(" + device) + ", ") + priority) + ")");
        }
        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            if ((priority != android.bluetooth.BluetoothProfile.PRIORITY_OFF) && (priority != android.bluetooth.BluetoothProfile.PRIORITY_ON)) {
                return false;
            }
            try {
                return mService.setPriority(device, priority);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPbapClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null) {
            android.util.Log.w(android.bluetooth.BluetoothPbapClient.TAG, "Proxy not attached to service");
        }
        return false;
    }

    /**
     * Get the priority of the profile.
     *
     * <p> The priority can be any of:
     * {@link #PRIORITY_AUTO_CONNECT}, {@link #PRIORITY_OFF},
     * {@link #PRIORITY_ON}, {@link #PRIORITY_UNDEFINED}
     *
     * @param device
     * 		Bluetooth device
     * @return priority of the device
     */
    public int getPriority(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothPbapClient.VDBG) {
            android.bluetooth.BluetoothPbapClient.log(("getPriority(" + device) + ")");
        }
        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.getPriority(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothPbapClient.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return android.bluetooth.BluetoothProfile.PRIORITY_OFF;
            }
        }
        if (mService == null) {
            android.util.Log.w(android.bluetooth.BluetoothPbapClient.TAG, "Proxy not attached to service");
        }
        return android.bluetooth.BluetoothProfile.PRIORITY_OFF;
    }
}

