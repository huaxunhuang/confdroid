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
 * Public API for Bluetooth Health Profile.
 *
 * <p>BluetoothHealth is a proxy object for controlling the Bluetooth
 * Service via IPC.
 *
 * <p> How to connect to a health device which is acting in the source role.
 *  <li> Use {@link BluetoothAdapter#getProfileProxy} to get
 *  the BluetoothHealth proxy object. </li>
 *  <li> Create an {@link BluetoothHealth} callback and call
 *  {@link #registerSinkAppConfiguration} to register an application
 *  configuration </li>
 *  <li> Pair with the remote device. This currently needs to be done manually
 *  from Bluetooth Settings </li>
 *  <li> Connect to a health device using {@link #connectChannelToSource}. Some
 *  devices will connect the channel automatically. The {@link BluetoothHealth}
 *  callback will inform the application of channel state change. </li>
 *  <li> Use the file descriptor provided with a connected channel to read and
 *  write data to the health channel. </li>
 *  <li> The received data needs to be interpreted using a health manager which
 *  implements the IEEE 11073-xxxxx specifications.
 *  <li> When done, close the health channel by calling {@link #disconnectChannel}
 *  and unregister the application configuration calling
 *  {@link #unregisterAppConfiguration}
 */
public final class BluetoothHealth implements android.bluetooth.BluetoothProfile {
    private static final java.lang.String TAG = "BluetoothHealth";

    private static final boolean DBG = true;

    private static final boolean VDBG = false;

    /**
     * Health Profile Source Role - the health device.
     */
    public static final int SOURCE_ROLE = 1 << 0;

    /**
     * Health Profile Sink Role the device talking to the health device.
     */
    public static final int SINK_ROLE = 1 << 1;

    /**
     * Health Profile - Channel Type used - Reliable
     */
    public static final int CHANNEL_TYPE_RELIABLE = 10;

    /**
     * Health Profile - Channel Type used - Streaming
     */
    public static final int CHANNEL_TYPE_STREAMING = 11;

    /**
     *
     *
     * @unknown 
     */
    public static final int CHANNEL_TYPE_ANY = 12;

    /**
     *
     *
     * @unknown 
     */
    public static final int HEALTH_OPERATION_SUCCESS = 6000;

    /**
     *
     *
     * @unknown 
     */
    public static final int HEALTH_OPERATION_ERROR = 6001;

    /**
     *
     *
     * @unknown 
     */
    public static final int HEALTH_OPERATION_INVALID_ARGS = 6002;

    /**
     *
     *
     * @unknown 
     */
    public static final int HEALTH_OPERATION_GENERIC_FAILURE = 6003;

    /**
     *
     *
     * @unknown 
     */
    public static final int HEALTH_OPERATION_NOT_FOUND = 6004;

    /**
     *
     *
     * @unknown 
     */
    public static final int HEALTH_OPERATION_NOT_ALLOWED = 6005;

    private final android.bluetooth.IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new android.bluetooth.IBluetoothStateChangeCallback.Stub() {
        public void onBluetoothStateChange(boolean up) {
            if (android.bluetooth.BluetoothHealth.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHealth.TAG, "onBluetoothStateChange: up=" + up);

            if (!up) {
                if (android.bluetooth.BluetoothHealth.VDBG)
                    android.util.Log.d(android.bluetooth.BluetoothHealth.TAG, "Unbinding service...");

                synchronized(mConnection) {
                    try {
                        mService = null;
                        mContext.unbindService(mConnection);
                    } catch (java.lang.Exception re) {
                        android.util.Log.e(android.bluetooth.BluetoothHealth.TAG, "", re);
                    }
                }
            } else {
                synchronized(mConnection) {
                    try {
                        if (mService == null) {
                            if (android.bluetooth.BluetoothHealth.VDBG)
                                android.util.Log.d(android.bluetooth.BluetoothHealth.TAG, "Binding service...");

                            doBind();
                        }
                    } catch (java.lang.Exception re) {
                        android.util.Log.e(android.bluetooth.BluetoothHealth.TAG, "", re);
                    }
                }
            }
        }
    };

    /**
     * Register an application configuration that acts as a Health SINK.
     * This is the configuration that will be used to communicate with health devices
     * which will act as the {@link #SOURCE_ROLE}. This is an asynchronous call and so
     * the callback is used to notify success or failure if the function returns true.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param name
     * 		The friendly name associated with the application or configuration.
     * @param dataType
     * 		The dataType of the Source role of Health Profile to which
     * 		the sink wants to connect to.
     * @param callback
     * 		A callback to indicate success or failure of the registration and
     * 		all operations done on this application configuration.
     * @return If true, callback will be called.
     */
    public boolean registerSinkAppConfiguration(java.lang.String name, int dataType, android.bluetooth.BluetoothHealthCallback callback) {
        if ((!isEnabled()) || (name == null))
            return false;

        if (android.bluetooth.BluetoothHealth.VDBG)
            android.bluetooth.BluetoothHealth.log(((("registerSinkApplication(" + name) + ":") + dataType) + ")");

        return registerAppConfiguration(name, dataType, android.bluetooth.BluetoothHealth.SINK_ROLE, android.bluetooth.BluetoothHealth.CHANNEL_TYPE_ANY, callback);
    }

    /**
     * Register an application configuration that acts as a Health SINK or in a Health
     * SOURCE role.This is an asynchronous call and so
     * the callback is used to notify success or failure if the function returns true.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param name
     * 		The friendly name associated with the application or configuration.
     * @param dataType
     * 		The dataType of the Source role of Health Profile.
     * @param channelType
     * 		The channel type. Will be one of
     * 		{@link #CHANNEL_TYPE_RELIABLE}  or
     * 		{@link #CHANNEL_TYPE_STREAMING}
     * @param callback
     * 		- A callback to indicate success or failure.
     * @return If true, callback will be called.
     * @unknown 
     */
    public boolean registerAppConfiguration(java.lang.String name, int dataType, int role, int channelType, android.bluetooth.BluetoothHealthCallback callback) {
        boolean result = false;
        if ((!isEnabled()) || (!checkAppParam(name, role, channelType, callback)))
            return result;

        if (android.bluetooth.BluetoothHealth.VDBG)
            android.bluetooth.BluetoothHealth.log(((("registerApplication(" + name) + ":") + dataType) + ")");

        android.bluetooth.BluetoothHealth.BluetoothHealthCallbackWrapper wrapper = new android.bluetooth.BluetoothHealth.BluetoothHealthCallbackWrapper(callback);
        android.bluetooth.BluetoothHealthAppConfiguration config = new android.bluetooth.BluetoothHealthAppConfiguration(name, dataType, role, channelType);
        if (mService != null) {
            try {
                result = mService.registerAppConfiguration(config, wrapper);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHealth.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothHealth.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothHealth.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHealth.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return result;
    }

    /**
     * Unregister an application configuration that has been registered using
     * {@link #registerSinkAppConfiguration}
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param config
     * 		The health app configuration
     * @return Success or failure.
     */
    public boolean unregisterAppConfiguration(android.bluetooth.BluetoothHealthAppConfiguration config) {
        boolean result = false;
        if (((mService != null) && isEnabled()) && (config != null)) {
            try {
                result = mService.unregisterAppConfiguration(config);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHealth.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothHealth.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothHealth.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHealth.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return result;
    }

    /**
     * Connect to a health device which has the {@link #SOURCE_ROLE}.
     * This is an asynchronous call. If this function returns true, the callback
     * associated with the application configuration will be called.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param device
     * 		The remote Bluetooth device.
     * @param config
     * 		The application configuration which has been registered using
     * 		{@link #registerSinkAppConfiguration(String, int, BluetoothHealthCallback)}
     * @return If true, the callback associated with the application config will be called.
     */
    public boolean connectChannelToSource(android.bluetooth.BluetoothDevice device, android.bluetooth.BluetoothHealthAppConfiguration config) {
        if ((((mService != null) && isEnabled()) && isValidDevice(device)) && (config != null)) {
            try {
                return mService.connectChannelToSource(device, config);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHealth.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothHealth.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothHealth.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHealth.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return false;
    }

    /**
     * Connect to a health device which has the {@link #SINK_ROLE}.
     * This is an asynchronous call. If this function returns true, the callback
     * associated with the application configuration will be called.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param device
     * 		The remote Bluetooth device.
     * @param config
     * 		The application configuration which has been registered using
     * 		{@link #registerSinkAppConfiguration(String, int, BluetoothHealthCallback)}
     * @return If true, the callback associated with the application config will be called.
     * @unknown 
     */
    public boolean connectChannelToSink(android.bluetooth.BluetoothDevice device, android.bluetooth.BluetoothHealthAppConfiguration config, int channelType) {
        if ((((mService != null) && isEnabled()) && isValidDevice(device)) && (config != null)) {
            try {
                return mService.connectChannelToSink(device, config, channelType);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHealth.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothHealth.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothHealth.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHealth.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return false;
    }

    /**
     * Disconnect a connected health channel.
     * This is an asynchronous call. If this function returns true, the callback
     * associated with the application configuration will be called.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param device
     * 		The remote Bluetooth device.
     * @param config
     * 		The application configuration which has been registered using
     * 		{@link #registerSinkAppConfiguration(String, int, BluetoothHealthCallback)}
     * @param channelId
     * 		The channel id associated with the channel
     * @return If true, the callback associated with the application config will be called.
     */
    public boolean disconnectChannel(android.bluetooth.BluetoothDevice device, android.bluetooth.BluetoothHealthAppConfiguration config, int channelId) {
        if ((((mService != null) && isEnabled()) && isValidDevice(device)) && (config != null)) {
            try {
                return mService.disconnectChannel(device, config, channelId);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHealth.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothHealth.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothHealth.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHealth.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return false;
    }

    /**
     * Get the file descriptor of the main channel associated with the remote device
     * and application configuration.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * <p> Its the responsibility of the caller to close the ParcelFileDescriptor
     * when done.
     *
     * @param device
     * 		The remote Bluetooth health device
     * @param config
     * 		The application configuration
     * @return null on failure, ParcelFileDescriptor on success.
     */
    public android.os.ParcelFileDescriptor getMainChannelFd(android.bluetooth.BluetoothDevice device, android.bluetooth.BluetoothHealthAppConfiguration config) {
        if ((((mService != null) && isEnabled()) && isValidDevice(device)) && (config != null)) {
            try {
                return mService.getMainChannelFd(device, config);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHealth.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothHealth.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothHealth.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHealth.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return null;
    }

    /**
     * Get the current connection state of the profile.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * This is not specific to any application configuration but represents the connection
     * state of the local Bluetooth adapter with the remote device. This can be used
     * by applications like status bar which would just like to know the state of the
     * local adapter.
     *
     * @param device
     * 		Remote bluetooth device.
     * @return State of the profile connection. One of
    {@link #STATE_CONNECTED}, {@link #STATE_CONNECTING},
    {@link #STATE_DISCONNECTED}, {@link #STATE_DISCONNECTING}
     */
    @java.lang.Override
    public int getConnectionState(android.bluetooth.BluetoothDevice device) {
        if (((mService != null) && isEnabled()) && isValidDevice(device)) {
            try {
                return mService.getHealthDeviceConnectionState(device);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHealth.TAG, e.toString());
            }
        } else {
            android.util.Log.w(android.bluetooth.BluetoothHealth.TAG, "Proxy not attached to service");
            if (android.bluetooth.BluetoothHealth.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHealth.TAG, android.util.Log.getStackTraceString(new java.lang.Throwable()));

        }
        return android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
    }

    /**
     * Get connected devices for the health profile.
     *
     * <p> Return the set of devices which are in state {@link #STATE_CONNECTED}
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * This is not specific to any application configuration but represents the connection
     * state of the local Bluetooth adapter for this profile. This can be used
     * by applications like status bar which would just like to know the state of the
     * local adapter.
     *
     * @return List of devices. The list will be empty on error.
     */
    @java.lang.Override
    public java.util.List<android.bluetooth.BluetoothDevice> getConnectedDevices() {
        if ((mService != null) && isEnabled()) {
            try {
                return mService.getConnectedHealthDevices();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHealth.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHealth.TAG, "Proxy not attached to service");

        return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
    }

    /**
     * Get a list of devices that match any of the given connection
     * states.
     *
     * <p> If none of the devices match any of the given states,
     * an empty list will be returned.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     * This is not specific to any application configuration but represents the connection
     * state of the local Bluetooth adapter for this profile. This can be used
     * by applications like status bar which would just like to know the state of the
     * local adapter.
     *
     * @param states
     * 		Array of states. States can be one of
     * 		{@link #STATE_CONNECTED}, {@link #STATE_CONNECTING},
     * 		{@link #STATE_DISCONNECTED}, {@link #STATE_DISCONNECTING},
     * @return List of devices. The list will be empty on error.
     */
    @java.lang.Override
    public java.util.List<android.bluetooth.BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        if ((mService != null) && isEnabled()) {
            try {
                return mService.getHealthDevicesMatchingConnectionStates(states);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHealth.TAG, "Stack:" + android.util.Log.getStackTraceString(new java.lang.Throwable()));
                return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
            }
        }
        if (mService == null)
            android.util.Log.w(android.bluetooth.BluetoothHealth.TAG, "Proxy not attached to service");

        return new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
    }

    private static class BluetoothHealthCallbackWrapper extends android.bluetooth.IBluetoothHealthCallback.Stub {
        private android.bluetooth.BluetoothHealthCallback mCallback;

        public BluetoothHealthCallbackWrapper(android.bluetooth.BluetoothHealthCallback callback) {
            mCallback = callback;
        }

        @java.lang.Override
        public void onHealthAppConfigurationStatusChange(android.bluetooth.BluetoothHealthAppConfiguration config, int status) {
            mCallback.onHealthAppConfigurationStatusChange(config, status);
        }

        @java.lang.Override
        public void onHealthChannelStateChange(android.bluetooth.BluetoothHealthAppConfiguration config, android.bluetooth.BluetoothDevice device, int prevState, int newState, android.os.ParcelFileDescriptor fd, int channelId) {
            mCallback.onHealthChannelStateChange(config, device, prevState, newState, fd, channelId);
        }
    }

    /**
     * Health Channel Connection State - Disconnected
     */
    public static final int STATE_CHANNEL_DISCONNECTED = 0;

    /**
     * Health Channel Connection State - Connecting
     */
    public static final int STATE_CHANNEL_CONNECTING = 1;

    /**
     * Health Channel Connection State - Connected
     */
    public static final int STATE_CHANNEL_CONNECTED = 2;

    /**
     * Health Channel Connection State - Disconnecting
     */
    public static final int STATE_CHANNEL_DISCONNECTING = 3;

    /**
     * Health App Configuration registration success
     */
    public static final int APP_CONFIG_REGISTRATION_SUCCESS = 0;

    /**
     * Health App Configuration registration failure
     */
    public static final int APP_CONFIG_REGISTRATION_FAILURE = 1;

    /**
     * Health App Configuration un-registration success
     */
    public static final int APP_CONFIG_UNREGISTRATION_SUCCESS = 2;

    /**
     * Health App Configuration un-registration failure
     */
    public static final int APP_CONFIG_UNREGISTRATION_FAILURE = 3;

    private android.content.Context mContext;

    private android.bluetooth.BluetoothProfile.ServiceListener mServiceListener;

    private android.bluetooth.IBluetoothHealth mService;

    android.bluetooth.BluetoothAdapter mAdapter;

    /**
     * Create a BluetoothHealth proxy object.
     */
    /* package */
    BluetoothHealth(android.content.Context context, android.bluetooth.BluetoothProfile.ServiceListener l) {
        mContext = context;
        mServiceListener = l;
        mAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        android.bluetooth.IBluetoothManager mgr = mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(mBluetoothStateChangeCallback);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothHealth.TAG, "", e);
            }
        }
        doBind();
    }

    boolean doBind() {
        android.content.Intent intent = new android.content.Intent(android.bluetooth.IBluetoothHealth.class.getName());
        android.content.ComponentName comp = intent.resolveSystemService(mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if ((comp == null) || (!mContext.bindServiceAsUser(intent, mConnection, 0, android.os.Process.myUserHandle()))) {
            android.util.Log.e(android.bluetooth.BluetoothHealth.TAG, "Could not bind to Bluetooth Health Service with " + intent);
            return false;
        }
        return true;
    }

    /* package */
    void close() {
        if (android.bluetooth.BluetoothHealth.VDBG)
            android.bluetooth.BluetoothHealth.log("close()");

        android.bluetooth.IBluetoothManager mgr = mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(mBluetoothStateChangeCallback);
            } catch (java.lang.Exception e) {
                android.util.Log.e(android.bluetooth.BluetoothHealth.TAG, "", e);
            }
        }
        synchronized(mConnection) {
            if (mService != null) {
                try {
                    mService = null;
                    mContext.unbindService(mConnection);
                } catch (java.lang.Exception re) {
                    android.util.Log.e(android.bluetooth.BluetoothHealth.TAG, "", re);
                }
            }
        }
        mServiceListener = null;
    }

    private final android.content.ServiceConnection mConnection = new android.content.ServiceConnection() {
        public void onServiceConnected(android.content.ComponentName className, android.os.IBinder service) {
            if (android.bluetooth.BluetoothHealth.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHealth.TAG, "Proxy object connected");

            mService = IBluetoothHealth.Stub.asInterface(service);
            if (mServiceListener != null) {
                mServiceListener.onServiceConnected(android.bluetooth.BluetoothProfile.HEALTH, android.bluetooth.BluetoothHealth.this);
            }
        }

        public void onServiceDisconnected(android.content.ComponentName className) {
            if (android.bluetooth.BluetoothHealth.DBG)
                android.util.Log.d(android.bluetooth.BluetoothHealth.TAG, "Proxy object disconnected");

            mService = null;
            if (mServiceListener != null) {
                mServiceListener.onServiceDisconnected(android.bluetooth.BluetoothProfile.HEALTH);
            }
        }
    };

    private boolean isEnabled() {
        android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if ((adapter != null) && (adapter.getState() == android.bluetooth.BluetoothAdapter.STATE_ON))
            return true;

        android.bluetooth.BluetoothHealth.log("Bluetooth is Not enabled");
        return false;
    }

    private boolean isValidDevice(android.bluetooth.BluetoothDevice device) {
        if (device == null)
            return false;

        if (android.bluetooth.BluetoothAdapter.checkBluetoothAddress(device.getAddress()))
            return true;

        return false;
    }

    private boolean checkAppParam(java.lang.String name, int role, int channelType, android.bluetooth.BluetoothHealthCallback callback) {
        if ((((name == null) || ((role != android.bluetooth.BluetoothHealth.SOURCE_ROLE) && (role != android.bluetooth.BluetoothHealth.SINK_ROLE))) || (((channelType != android.bluetooth.BluetoothHealth.CHANNEL_TYPE_RELIABLE) && (channelType != android.bluetooth.BluetoothHealth.CHANNEL_TYPE_STREAMING)) && (channelType != android.bluetooth.BluetoothHealth.CHANNEL_TYPE_ANY))) || (callback == null)) {
            return false;
        }
        if ((role == android.bluetooth.BluetoothHealth.SOURCE_ROLE) && (channelType == android.bluetooth.BluetoothHealth.CHANNEL_TYPE_ANY))
            return false;

        return true;
    }

    private static void log(java.lang.String msg) {
        android.util.Log.d(android.bluetooth.BluetoothHealth.TAG, msg);
    }
}

