/**
 * Copyright (C) 2013 The Android Open Source Project
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
 * High level manager used to obtain an instance of an {@link BluetoothAdapter}
 * and to conduct overall Bluetooth Management.
 * <p>
 * Use {@link android.content.Context#getSystemService(java.lang.String)}
 * with {@link Context#BLUETOOTH_SERVICE} to create an {@link BluetoothManager},
 * then call {@link #getAdapter} to obtain the {@link BluetoothAdapter}.
 * <p>
 * Alternately, you can just call the static helper
 * {@link BluetoothAdapter#getDefaultAdapter()}.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>
 * For more information about using BLUETOOTH, read the <a href=
 * "{@docRoot }guide/topics/connectivity/bluetooth.html">Bluetooth</a> developer
 * guide.
 * </p>
 * </div>
 *
 * @see Context#getSystemService
 * @see BluetoothAdapter#getDefaultAdapter()
 */
public final class BluetoothManager {
    private static final java.lang.String TAG = "BluetoothManager";

    private static final boolean DBG = true;

    private static final boolean VDBG = true;

    private final android.bluetooth.BluetoothAdapter mAdapter;

    /**
     *
     *
     * @unknown 
     */
    public BluetoothManager(android.content.Context context) {
        context = context.getApplicationContext();
        if (context == null) {
            throw new java.lang.IllegalArgumentException("context not associated with any application (using a mock context?)");
        }
        // Legacy api - getDefaultAdapter does not take in the context
        mAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * Get the default BLUETOOTH Adapter for this device.
     *
     * @return the default BLUETOOTH Adapter
     */
    public android.bluetooth.BluetoothAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * Get the current connection state of the profile to the remote device.
     *
     * <p>This is not specific to any application configuration but represents
     * the connection state of the local Bluetooth adapter for certain profile.
     * This can be used by applications like status bar which would just like
     * to know the state of Bluetooth.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param device
     * 		Remote bluetooth device.
     * @param profile
     * 		GATT or GATT_SERVER
     * @return State of the profile connection. One of
    {@link BluetoothProfile#STATE_CONNECTED}, {@link BluetoothProfile#STATE_CONNECTING},
    {@link BluetoothProfile#STATE_DISCONNECTED},
    {@link BluetoothProfile#STATE_DISCONNECTING}
     */
    @android.annotation.RequiresPermission(Manifest.permission.BLUETOOTH)
    public int getConnectionState(android.bluetooth.BluetoothDevice device, int profile) {
        if (android.bluetooth.BluetoothManager.DBG)
            android.util.Log.d(android.bluetooth.BluetoothManager.TAG, "getConnectionState()");

        java.util.List<android.bluetooth.BluetoothDevice> connectedDevices = getConnectedDevices(profile);
        for (android.bluetooth.BluetoothDevice connectedDevice : connectedDevices) {
            if (device.equals(connectedDevice)) {
                return android.bluetooth.BluetoothProfile.STATE_CONNECTED;
            }
        }
        return android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
    }

    /**
     * Get connected devices for the specified profile.
     *
     * <p> Return the set of devices which are in state {@link BluetoothProfile#STATE_CONNECTED}
     *
     * <p>This is not specific to any application configuration but represents
     * the connection state of Bluetooth for this profile.
     * This can be used by applications like status bar which would just like
     * to know the state of Bluetooth.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param profile
     * 		GATT or GATT_SERVER
     * @return List of devices. The list will be empty on error.
     */
    @android.annotation.RequiresPermission(Manifest.permission.BLUETOOTH)
    public java.util.List<android.bluetooth.BluetoothDevice> getConnectedDevices(int profile) {
        if (android.bluetooth.BluetoothManager.DBG)
            android.util.Log.d(android.bluetooth.BluetoothManager.TAG, "getConnectedDevices");

        if ((profile != android.bluetooth.BluetoothProfile.GATT) && (profile != android.bluetooth.BluetoothProfile.GATT_SERVER)) {
            throw new java.lang.IllegalArgumentException("Profile not supported: " + profile);
        }
        java.util.List<android.bluetooth.BluetoothDevice> connectedDevices = new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
        try {
            android.bluetooth.IBluetoothManager managerService = mAdapter.getBluetoothManager();
            android.bluetooth.IBluetoothGatt iGatt = managerService.getBluetoothGatt();
            if (iGatt == null)
                return connectedDevices;

            connectedDevices = iGatt.getDevicesMatchingConnectionStates(new int[]{ android.bluetooth.BluetoothProfile.STATE_CONNECTED });
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothManager.TAG, "", e);
        }
        return connectedDevices;
    }

    /**
     * Get a list of devices that match any of the given connection
     * states.
     *
     * <p> If none of the devices match any of the given states,
     * an empty list will be returned.
     *
     * <p>This is not specific to any application configuration but represents
     * the connection state of the local Bluetooth adapter for this profile.
     * This can be used by applications like status bar which would just like
     * to know the state of the local adapter.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param profile
     * 		GATT or GATT_SERVER
     * @param states
     * 		Array of states. States can be one of
     * 		{@link BluetoothProfile#STATE_CONNECTED}, {@link BluetoothProfile#STATE_CONNECTING},
     * 		{@link BluetoothProfile#STATE_DISCONNECTED},
     * 		{@link BluetoothProfile#STATE_DISCONNECTING},
     * @return List of devices. The list will be empty on error.
     */
    @android.annotation.RequiresPermission(Manifest.permission.BLUETOOTH)
    public java.util.List<android.bluetooth.BluetoothDevice> getDevicesMatchingConnectionStates(int profile, int[] states) {
        if (android.bluetooth.BluetoothManager.DBG)
            android.util.Log.d(android.bluetooth.BluetoothManager.TAG, "getDevicesMatchingConnectionStates");

        if ((profile != android.bluetooth.BluetoothProfile.GATT) && (profile != android.bluetooth.BluetoothProfile.GATT_SERVER)) {
            throw new java.lang.IllegalArgumentException("Profile not supported: " + profile);
        }
        java.util.List<android.bluetooth.BluetoothDevice> devices = new java.util.ArrayList<android.bluetooth.BluetoothDevice>();
        try {
            android.bluetooth.IBluetoothManager managerService = mAdapter.getBluetoothManager();
            android.bluetooth.IBluetoothGatt iGatt = managerService.getBluetoothGatt();
            if (iGatt == null)
                return devices;

            devices = iGatt.getDevicesMatchingConnectionStates(states);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothManager.TAG, "", e);
        }
        return devices;
    }

    /**
     * Open a GATT Server
     * The callback is used to deliver results to Caller, such as connection status as well
     * as the results of any other GATT server operations.
     * The method returns a BluetoothGattServer instance. You can use BluetoothGattServer
     * to conduct GATT server operations.
     *
     * @param context
     * 		App context
     * @param callback
     * 		GATT server callback handler that will receive asynchronous callbacks.
     * @return BluetoothGattServer instance
     */
    public android.bluetooth.BluetoothGattServer openGattServer(android.content.Context context, android.bluetooth.BluetoothGattServerCallback callback) {
        return openGattServer(context, callback, android.bluetooth.BluetoothDevice.TRANSPORT_AUTO);
    }

    /**
     * Open a GATT Server
     * The callback is used to deliver results to Caller, such as connection status as well
     * as the results of any other GATT server operations.
     * The method returns a BluetoothGattServer instance. You can use BluetoothGattServer
     * to conduct GATT server operations.
     *
     * @param context
     * 		App context
     * @param callback
     * 		GATT server callback handler that will receive asynchronous callbacks.
     * @param transport
     * 		preferred transport for GATT connections to remote dual-mode devices
     * 		{@link BluetoothDevice#TRANSPORT_AUTO} or
     * 		{@link BluetoothDevice#TRANSPORT_BREDR} or {@link BluetoothDevice#TRANSPORT_LE}
     * @return BluetoothGattServer instance
     * @unknown 
     */
    public android.bluetooth.BluetoothGattServer openGattServer(android.content.Context context, android.bluetooth.BluetoothGattServerCallback callback, int transport) {
        if ((context == null) || (callback == null)) {
            throw new java.lang.IllegalArgumentException((("null parameter: " + context) + " ") + callback);
        }
        // TODO(Bluetooth) check whether platform support BLE
        // Do the check here or in GattServer?
        try {
            android.bluetooth.IBluetoothManager managerService = mAdapter.getBluetoothManager();
            android.bluetooth.IBluetoothGatt iGatt = managerService.getBluetoothGatt();
            if (iGatt == null) {
                android.util.Log.e(android.bluetooth.BluetoothManager.TAG, "Fail to get GATT Server connection");
                return null;
            }
            android.bluetooth.BluetoothGattServer mGattServer = new android.bluetooth.BluetoothGattServer(iGatt, transport);
            java.lang.Boolean regStatus = mGattServer.registerCallback(callback);
            return regStatus ? mGattServer : null;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothManager.TAG, "", e);
            return null;
        }
    }
}

