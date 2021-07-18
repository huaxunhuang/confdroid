/**
 * Copyright (C) 2011 The Android Open Source Project
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
 * This class provides the public APIs to control the Bluetooth Input
 * Device Profile.
 *
 * <p>BluetoothInputDevice is a proxy object for controlling the Bluetooth
 * Service via IPC. Use {@link BluetoothAdapter#getProfileProxy} to get
 * the BluetoothInputDevice proxy object.
 *
 * <p>Each method is protected with its appropriate permission.
 *
 * @unknown 
 */
public final class BluetoothInputDevice implements android.bluetooth.BluetoothProfile {
    private static final java.lang.String TAG = "BluetoothInputDevice";

    private static final boolean DBG = true;

    private static final boolean VDBG = false;

    /**
     * Intent used to broadcast the change in connection state of the Input
     * Device profile.
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
    public static final java.lang.String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.input.profile.action.CONNECTION_STATE_CHANGED";

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String ACTION_PROTOCOL_MODE_CHANGED = "android.bluetooth.input.profile.action.PROTOCOL_MODE_CHANGED";

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String ACTION_HANDSHAKE = "android.bluetooth.input.profile.action.HANDSHAKE";

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String ACTION_REPORT = "android.bluetooth.input.profile.action.REPORT";

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String ACTION_VIRTUAL_UNPLUG_STATUS = "android.bluetooth.input.profile.action.VIRTUAL_UNPLUG_STATUS";

    /**
     * Return codes for the connect and disconnect Bluez / Dbus calls.
     *
     * @unknown 
     */
    public static final int INPUT_DISCONNECT_FAILED_NOT_CONNECTED = 5000;

    /**
     *
     *
     * @unknown 
     */
    public static final int INPUT_CONNECT_FAILED_ALREADY_CONNECTED = 5001;

    /**
     *
     *
     * @unknown 
     */
    public static final int INPUT_CONNECT_FAILED_ATTEMPT_FAILED = 5002;

    /**
     *
     *
     * @unknown 
     */
    public static final int INPUT_OPERATION_GENERIC_FAILURE = 5003;

    /**
     *
     *
     * @unknown 
     */
    public static final int INPUT_OPERATION_SUCCESS = 5004;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROTOCOL_REPORT_MODE = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROTOCOL_BOOT_MODE = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROTOCOL_UNSUPPORTED_MODE = 255;

    /* int reportType, int reportType, int bufferSize */
    /**
     *
     *
     * @unknown 
     */
    public static final byte REPORT_TYPE_INPUT = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final byte REPORT_TYPE_OUTPUT = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final byte REPORT_TYPE_FEATURE = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int VIRTUAL_UNPLUG_STATUS_SUCCESS = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int VIRTUAL_UNPLUG_STATUS_FAIL = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_PROTOCOL_MODE = "android.bluetooth.BluetoothInputDevice.extra.PROTOCOL_MODE";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_REPORT_TYPE = "android.bluetooth.BluetoothInputDevice.extra.REPORT_TYPE";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_REPORT_ID = "android.bluetooth.BluetoothInputDevice.extra.REPORT_ID";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_REPORT_BUFFER_SIZE = "android.bluetooth.BluetoothInputDevice.extra.REPORT_BUFFER_SIZE";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_REPORT = "android.bluetooth.BluetoothInputDevice.extra.REPORT";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_STATUS = "android.bluetooth.BluetoothInputDevice.extra.STATUS";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_VIRTUAL_UNPLUG_STATUS = "android.bluetooth.BluetoothInputDevice.extra.VIRTUAL_UNPLUG_STATUS";

    private android.content.Context mContext;

    private android.bluetooth.BluetoothProfile.ServiceListener mServiceListener;

    private android.bluetooth.BluetoothAdapter mAdapter;

    private android.bluetooth.IBluetoothInputDevice mService;

    private final android.bluetooth.IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new android.bluetooth.IBluetoothStateChangeCallback.Stub() {
        public void onBluetoothStateChange(boolean up) {
            if (android.bluetooth.BluetoothInputDevice.DBG)
                android.util.Log.d(android.bluetooth.BluetoothInputDevice.TAG, "onBluetoothStateChange: up=" + up);

            if (!up) {
                if (android.bluetooth.BluetoothInputDevice.VDBG)
                    android.util.Log.d(android.bluetooth.BluetoothInputDevice.TAG, "Unbinding service...");

                synchronized(mConnection) {
                    try {
                        mService = null;
                        mContext.unbindService(mConnection);
                    } catch (java.lang.Exception re) {
                        android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "", re);
                    }
                }
            } else {
                synchronized(mConnection) {
                    try {
                        if (mService == null) {
                            if (android.bluetooth.BluetoothInputDevice.VDBG)
                                android.util.Log.d(android.bluetooth.BluetoothInputDevice.TAG, "Binding service...");

                            doBind();
                        }
                    } catch (java.lang.Exception re) {
                        android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "", re);
                    }
                }
            }
        }
    };

    /**
     * Create a BluetoothInputDevice proxy object for interacting with the local
     * Bluetooth Service which handles the InputDevice profile
     */
    /* package */
    BluetoothInputDevice(android.content.Context context, android.bluetooth.BluetoothProfile.ServiceListener l) {
        mContext = context;
        mServiceListener = l;
        mAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        android.bluetooth.IBluetoothManager mgr = mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(mBluetoothStateChangeCallback);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "", e);
            }
        }
        doBind();
    }

    boolean doBind() {
        android.content.Intent intent = new android.content.Intent(android.bluetooth.IBluetoothInputDevice.class.getName());
        android.content.ComponentName comp = intent.resolveSystemService(mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if ((comp == null) || (!mContext.bindServiceAsUser(intent, mConnection, 0, android.os.Process.myUserHandle()))) {
            android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "Could not bind to Bluetooth HID Service with " + intent);
            return false;
        }
        return true;
    }

    /* package */
    void close() {
        if (android.bluetooth.BluetoothInputDevice.VDBG)
            android.bluetooth.BluetoothInputDevice.log("close()");

        android.bluetooth.IBluetoothManager mgr = mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(mBluetoothStateChangeCallback);
            } catch (java.lang.Exception e) {
                android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "", e);
            }
        }
        synchronized(mConnection) {
            if (mService != null) {
                try {
                    mService = null;
                    mContext.unbindService(mConnection);
                } catch (java.lang.Exception re) {
                    android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "", re);
                }
            }
        }
        mServiceListener = null;
    }

    /**
     * Initiate connection to a profile of the remote bluetooth device.
     *
     * <p> The system supports connection to multiple input devices.
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
        if (android.bluetooth.BluetoothInputDevice.DBG)
            android.bluetooth.BluetoothInputDevice.log(("connect(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.connect(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothInputDevice.TAG, "Proxy not attached to service");

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
        if (android.bluetooth.BluetoothInputDevice.DBG)
            android.bluetooth.BluetoothInputDevice.log(("disconnect(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.disconnect(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothInputDevice.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * {@inheritDoc }
     */
    public java.util.List<android.bluetooth.BluetoothDevice> getConnectedDevices() {
        if (android.bluetooth.BluetoothInputDevice.VDBG)
            android.bluetooth.BluetoothInputDevice.log("getConnectedDevices()");

        if ((mService != null) && isEnabled()) {
            try {
                return mService.getConnectedDevices();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothInputDevice.TAG, "Proxy not attached to service");

        return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
    }

    /**
     * {@inheritDoc }
     */
    public java.util.List<android.bluetooth.BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        if (android.bluetooth.BluetoothInputDevice.VDBG)
            android.bluetooth.BluetoothInputDevice.log("getDevicesMatchingStates()");

        if ((mService != null) && isEnabled()) {
            try {
                return mService.getDevicesMatchingConnectionStates(states);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothInputDevice.TAG, "Proxy not attached to service");

        return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
    }

    /**
     * {@inheritDoc }
     */
    public int getConnectionState(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothInputDevice.VDBG)
            android.bluetooth.BluetoothInputDevice.log(("getState(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.getConnectionState(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothInputDevice.TAG, "Proxy not attached to service");

        return android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
    }

    /**
     * Set priority of the profile
     *
     * <p> The device should already be paired.
     *  Priority can be one of {@link #PRIORITY_ON} or
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
        if (android.bluetooth.BluetoothInputDevice.DBG)
            android.bluetooth.BluetoothInputDevice.log(((("setPriority(" + device) + ", ") + priority) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            if ((priority != android.bluetooth.BluetoothProfile.PRIORITY_OFF) && (priority != android.bluetooth.BluetoothProfile.PRIORITY_ON)) {
                return false;
            }
            try {
                return mService.setPriority(device, priority);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothInputDevice.TAG, "Proxy not attached to service");

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
        if (android.bluetooth.BluetoothInputDevice.VDBG)
            android.bluetooth.BluetoothInputDevice.log(("getPriority(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.getPriority(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return android.bluetooth.BluetoothProfile.PRIORITY_OFF;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothInputDevice.TAG, "Proxy not attached to service");

        return android.bluetooth.BluetoothProfile.PRIORITY_OFF;
    }

    private final android.content.ServiceConnection mConnection = new android.content.ServiceConnection() {
        public void onServiceConnected(android.content.ComponentName className, android.os.IBinder service) {
            if (android.bluetooth.BluetoothInputDevice.DBG)
                android.util.Log.d(android.bluetooth.BluetoothInputDevice.TAG, "Proxy object connected");

            mService = IBluetoothInputDevice.Stub.asInterface(service);
            if (mServiceListener != null) {
                mServiceListener.onServiceConnected(android.bluetooth.BluetoothProfile.INPUT_DEVICE, android.bluetooth.BluetoothInputDevice.this);
            }
        }

        public void onServiceDisconnected(android.content.ComponentName className) {
            if (android.bluetooth.BluetoothInputDevice.DBG)
                android.util.Log.d(android.bluetooth.BluetoothInputDevice.TAG, "Proxy object disconnected");

            mService = null;
            if (mServiceListener != null) {
                mServiceListener.onServiceDisconnected(android.bluetooth.BluetoothProfile.INPUT_DEVICE);
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

    /**
     * Initiate virtual unplug for a HID input device.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN} permission.
     *
     * @param device
     * 		Remote Bluetooth Device
     * @return false on immediate error,
    true otherwise
     * @unknown 
     */
    public boolean virtualUnplug(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothInputDevice.DBG)
            android.bluetooth.BluetoothInputDevice.log(("virtualUnplug(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.virtualUnplug(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothInputDevice.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Send Get_Protocol_Mode command to the connected HID input device.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN} permission.
     *
     * @param device
     * 		Remote Bluetooth Device
     * @return false on immediate error,
    true otherwise
     * @unknown 
     */
    public boolean getProtocolMode(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothInputDevice.VDBG)
            android.bluetooth.BluetoothInputDevice.log(("getProtocolMode(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.getProtocolMode(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothInputDevice.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Send Set_Protocol_Mode command to the connected HID input device.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN} permission.
     *
     * @param device
     * 		Remote Bluetooth Device
     * @return false on immediate error,
    true otherwise
     * @unknown 
     */
    public boolean setProtocolMode(android.bluetooth.BluetoothDevice device, int protocolMode) {
        if (android.bluetooth.BluetoothInputDevice.DBG)
            android.bluetooth.BluetoothInputDevice.log(("setProtocolMode(" + device) + ")");

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.setProtocolMode(device, protocolMode);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothInputDevice.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Send Get_Report command to the connected HID input device.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN} permission.
     *
     * @param device
     * 		Remote Bluetooth Device
     * @param reportType
     * 		Report type
     * @param reportId
     * 		Report ID
     * @param bufferSize
     * 		Report receiving buffer size
     * @return false on immediate error,
    true otherwise
     * @unknown 
     */
    public boolean getReport(android.bluetooth.BluetoothDevice device, byte reportType, byte reportId, int bufferSize) {
        if (android.bluetooth.BluetoothInputDevice.VDBG)
            android.bluetooth.BluetoothInputDevice.log((((((("getReport(" + device) + "), reportType=") + reportType) + " reportId=") + reportId) + "bufferSize=") + bufferSize);

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.getReport(device, reportType, reportId, bufferSize);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothInputDevice.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Send Set_Report command to the connected HID input device.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN} permission.
     *
     * @param device
     * 		Remote Bluetooth Device
     * @param reportType
     * 		Report type
     * @param report
     * 		Report receiving buffer size
     * @return false on immediate error,
    true otherwise
     * @unknown 
     */
    public boolean setReport(android.bluetooth.BluetoothDevice device, byte reportType, java.lang.String report) {
        if (android.bluetooth.BluetoothInputDevice.VDBG)
            android.bluetooth.BluetoothInputDevice.log((((("setReport(" + device) + "), reportType=") + reportType) + " report=") + report);

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.setReport(device, reportType, report);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothInputDevice.TAG, "Proxy not attached to service");

        return false;
    }

    /**
     * Send Send_Data command to the connected HID input device.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN} permission.
     *
     * @param device
     * 		Remote Bluetooth Device
     * @param report
     * 		Report to send
     * @return false on immediate error,
    true otherwise
     * @unknown 
     */
    public boolean sendData(android.bluetooth.BluetoothDevice device, java.lang.String report) {
        if (android.bluetooth.BluetoothInputDevice.DBG)
            android.bluetooth.BluetoothInputDevice.log((("sendData(" + device) + "), report=") + report);

        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.sendData(device, report);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothInputDevice.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return false;
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothInputDevice.TAG, "Proxy not attached to service");

        return false;
    }

    private static void log(java.lang.String msg) {
        android.util.Log.d(android.bluetooth.BluetoothInputDevice.TAG, msg);
    }
}

