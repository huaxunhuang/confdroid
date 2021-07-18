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
 * Public API for the Bluetooth GATT Profile.
 *
 * <p>This class provides Bluetooth GATT functionality to enable communication
 * with Bluetooth Smart or Smart Ready devices.
 *
 * <p>To connect to a remote peripheral device, create a {@link BluetoothGattCallback}
 * and call {@link BluetoothDevice#connectGatt} to get a instance of this class.
 * GATT capable devices can be discovered using the Bluetooth device discovery or BLE
 * scan process.
 */
public final class BluetoothGatt implements android.bluetooth.BluetoothProfile {
    private static final java.lang.String TAG = "BluetoothGatt";

    private static final boolean DBG = true;

    private static final boolean VDBG = false;

    private android.bluetooth.IBluetoothGatt mService;

    private android.bluetooth.BluetoothGattCallback mCallback;

    private int mClientIf;

    private boolean mAuthRetry = false;

    private android.bluetooth.BluetoothDevice mDevice;

    private boolean mAutoConnect;

    private int mConnState;

    private final java.lang.Object mStateLock = new java.lang.Object();

    private java.lang.Boolean mDeviceBusy = false;

    private int mTransport;

    private static final int CONN_STATE_IDLE = 0;

    private static final int CONN_STATE_CONNECTING = 1;

    private static final int CONN_STATE_CONNECTED = 2;

    private static final int CONN_STATE_DISCONNECTING = 3;

    private static final int CONN_STATE_CLOSED = 4;

    private java.util.List<android.bluetooth.BluetoothGattService> mServices;

    /**
     * A GATT operation completed successfully
     */
    public static final int GATT_SUCCESS = 0;

    /**
     * GATT read operation is not permitted
     */
    public static final int GATT_READ_NOT_PERMITTED = 0x2;

    /**
     * GATT write operation is not permitted
     */
    public static final int GATT_WRITE_NOT_PERMITTED = 0x3;

    /**
     * Insufficient authentication for a given operation
     */
    public static final int GATT_INSUFFICIENT_AUTHENTICATION = 0x5;

    /**
     * The given request is not supported
     */
    public static final int GATT_REQUEST_NOT_SUPPORTED = 0x6;

    /**
     * Insufficient encryption for a given operation
     */
    public static final int GATT_INSUFFICIENT_ENCRYPTION = 0xf;

    /**
     * A read or write operation was requested with an invalid offset
     */
    public static final int GATT_INVALID_OFFSET = 0x7;

    /**
     * A write operation exceeds the maximum length of the attribute
     */
    public static final int GATT_INVALID_ATTRIBUTE_LENGTH = 0xd;

    /**
     * A remote device connection is congested.
     */
    public static final int GATT_CONNECTION_CONGESTED = 0x8f;

    /**
     * A GATT operation failed, errors other than the above
     */
    public static final int GATT_FAILURE = 0x101;

    /**
     * Connection paramter update - Use the connection paramters recommended by the
     * Bluetooth SIG. This is the default value if no connection parameter update
     * is requested.
     */
    public static final int CONNECTION_PRIORITY_BALANCED = 0;

    /**
     * Connection paramter update - Request a high priority, low latency connection.
     * An application should only request high priority connection paramters to transfer
     * large amounts of data over LE quickly. Once the transfer is complete, the application
     * should request {@link BluetoothGatt#CONNECTION_PRIORITY_BALANCED} connectoin parameters
     * to reduce energy use.
     */
    public static final int CONNECTION_PRIORITY_HIGH = 1;

    /**
     * Connection paramter update - Request low power, reduced data rate connection parameters.
     */
    public static final int CONNECTION_PRIORITY_LOW_POWER = 2;

    /**
     * No authentication required.
     *
     * @unknown 
     */
    /* package */
    static final int AUTHENTICATION_NONE = 0;

    /**
     * Authentication requested; no man-in-the-middle protection required.
     *
     * @unknown 
     */
    /* package */
    static final int AUTHENTICATION_NO_MITM = 1;

    /**
     * Authentication with man-in-the-middle protection requested.
     *
     * @unknown 
     */
    /* package */
    static final int AUTHENTICATION_MITM = 2;

    /**
     * Bluetooth GATT callbacks. Overrides the default BluetoothGattCallback implementation.
     */
    private final android.bluetooth.IBluetoothGattCallback mBluetoothGattCallback = new android.bluetooth.BluetoothGattCallbackWrapper() {
        /**
         * Application interface registered - app is ready to go
         *
         * @unknown 
         */
        public void onClientRegistered(int status, int clientIf) {
            if (android.bluetooth.BluetoothGatt.DBG)
                android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, (("onClientRegistered() - status=" + status) + " clientIf=") + clientIf);

            if (android.bluetooth.BluetoothGatt.VDBG) {
                synchronized(mStateLock) {
                    if (mConnState != android.bluetooth.BluetoothGatt.CONN_STATE_CONNECTING) {
                        android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "Bad connection state: " + mConnState);
                    }
                }
            }
            mClientIf = clientIf;
            if (status != android.bluetooth.BluetoothGatt.GATT_SUCCESS) {
                mCallback.onConnectionStateChange(android.bluetooth.BluetoothGatt.this, android.bluetooth.BluetoothGatt.GATT_FAILURE, android.bluetooth.BluetoothProfile.STATE_DISCONNECTED);
                synchronized(mStateLock) {
                    mConnState = android.bluetooth.BluetoothGatt.CONN_STATE_IDLE;
                }
                return;
            }
            try {
                mService.clientConnect(mClientIf, mDevice.getAddress(), !mAutoConnect, mTransport);// autoConnect is inverse of "isDirect"

            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
            }
        }

        /**
         * Client connection state changed
         *
         * @unknown 
         */
        public void onClientConnectionState(int status, int clientIf, boolean connected, java.lang.String address) {
            if (android.bluetooth.BluetoothGatt.DBG)
                android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, (((("onClientConnectionState() - status=" + status) + " clientIf=") + clientIf) + " device=") + address);

            if (!address.equals(mDevice.getAddress())) {
                return;
            }
            int profileState = (connected) ? android.bluetooth.BluetoothProfile.STATE_CONNECTED : android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
            try {
                mCallback.onConnectionStateChange(android.bluetooth.BluetoothGatt.this, status, profileState);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGatt.TAG, "Unhandled exception in callback", ex);
            }
            synchronized(mStateLock) {
                if (connected) {
                    mConnState = android.bluetooth.BluetoothGatt.CONN_STATE_CONNECTED;
                } else {
                    mConnState = android.bluetooth.BluetoothGatt.CONN_STATE_IDLE;
                }
            }
            synchronized(mDeviceBusy) {
                mDeviceBusy = false;
            }
        }

        /**
         * Remote search has been completed.
         * The internal object structure should now reflect the state
         * of the remote device database. Let the application know that
         * we are done at this point.
         *
         * @unknown 
         */
        public void onSearchComplete(java.lang.String address, java.util.List<android.bluetooth.BluetoothGattService> services, int status) {
            if (android.bluetooth.BluetoothGatt.DBG)
                android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, (("onSearchComplete() = Device=" + address) + " Status=") + status);

            if (!address.equals(mDevice.getAddress())) {
                return;
            }
            for (android.bluetooth.BluetoothGattService s : services) {
                // services we receive don't have device set properly.
                s.setDevice(mDevice);
            }
            mServices.addAll(services);
            // Fix references to included services, as they doesn't point to right objects.
            for (android.bluetooth.BluetoothGattService fixedService : mServices) {
                java.util.ArrayList<android.bluetooth.BluetoothGattService> includedServices = new java.util.ArrayList(fixedService.getIncludedServices());
                fixedService.getIncludedServices().clear();
                for (android.bluetooth.BluetoothGattService brokenRef : includedServices) {
                    android.bluetooth.BluetoothGattService includedService = getService(mDevice, brokenRef.getUuid(), brokenRef.getInstanceId(), brokenRef.getType());
                    if (includedService != null) {
                        fixedService.addIncludedService(includedService);
                    } else {
                        android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "Broken GATT database: can't find included service.");
                    }
                }
            }
            try {
                mCallback.onServicesDiscovered(android.bluetooth.BluetoothGatt.this, status);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGatt.TAG, "Unhandled exception in callback", ex);
            }
        }

        /**
         * Remote characteristic has been read.
         * Updates the internal value.
         *
         * @unknown 
         */
        public void onCharacteristicRead(java.lang.String address, int status, int handle, byte[] value) {
            if (android.bluetooth.BluetoothGatt.VDBG)
                android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, (((("onCharacteristicRead() - Device=" + address) + " handle=") + handle) + " Status=") + status);

            android.util.Log.w(android.bluetooth.BluetoothGatt.TAG, (((("onCharacteristicRead() - Device=" + address) + " handle=") + handle) + " Status=") + status);
            if (!address.equals(mDevice.getAddress())) {
                return;
            }
            synchronized(mDeviceBusy) {
                mDeviceBusy = false;
            }
            if (((status == android.bluetooth.BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION) || (status == android.bluetooth.BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION)) && (mAuthRetry == false)) {
                try {
                    mAuthRetry = true;
                    mService.readCharacteristic(mClientIf, address, handle, android.bluetooth.BluetoothGatt.AUTHENTICATION_MITM);
                    return;
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
                }
            }
            mAuthRetry = false;
            android.bluetooth.BluetoothGattCharacteristic characteristic = getCharacteristicById(mDevice, handle);
            if (characteristic == null) {
                android.util.Log.w(android.bluetooth.BluetoothGatt.TAG, "onCharacteristicRead() failed to find characteristic!");
                return;
            }
            if (status == 0)
                characteristic.setValue(value);

            try {
                mCallback.onCharacteristicRead(android.bluetooth.BluetoothGatt.this, characteristic, status);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGatt.TAG, "Unhandled exception in callback", ex);
            }
        }

        /**
         * Characteristic has been written to the remote device.
         * Let the app know how we did...
         *
         * @unknown 
         */
        public void onCharacteristicWrite(java.lang.String address, int status, int handle) {
            if (android.bluetooth.BluetoothGatt.VDBG)
                android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, (((("onCharacteristicWrite() - Device=" + address) + " handle=") + handle) + " Status=") + status);

            if (!address.equals(mDevice.getAddress())) {
                return;
            }
            synchronized(mDeviceBusy) {
                mDeviceBusy = false;
            }
            android.bluetooth.BluetoothGattCharacteristic characteristic = getCharacteristicById(mDevice, handle);
            if (characteristic == null)
                return;

            if (((status == android.bluetooth.BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION) || (status == android.bluetooth.BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION)) && (mAuthRetry == false)) {
                try {
                    mAuthRetry = true;
                    mService.writeCharacteristic(mClientIf, address, handle, characteristic.getWriteType(), android.bluetooth.BluetoothGatt.AUTHENTICATION_MITM, characteristic.getValue());
                    return;
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
                }
            }
            mAuthRetry = false;
            try {
                mCallback.onCharacteristicWrite(android.bluetooth.BluetoothGatt.this, characteristic, status);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGatt.TAG, "Unhandled exception in callback", ex);
            }
        }

        /**
         * Remote characteristic has been updated.
         * Updates the internal value.
         *
         * @unknown 
         */
        public void onNotify(java.lang.String address, int handle, byte[] value) {
            if (android.bluetooth.BluetoothGatt.VDBG)
                android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, (("onNotify() - Device=" + address) + " handle=") + handle);

            if (!address.equals(mDevice.getAddress())) {
                return;
            }
            android.bluetooth.BluetoothGattCharacteristic characteristic = getCharacteristicById(mDevice, handle);
            if (characteristic == null)
                return;

            characteristic.setValue(value);
            try {
                mCallback.onCharacteristicChanged(android.bluetooth.BluetoothGatt.this, characteristic);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGatt.TAG, "Unhandled exception in callback", ex);
            }
        }

        /**
         * Descriptor has been read.
         *
         * @unknown 
         */
        public void onDescriptorRead(java.lang.String address, int status, int handle, byte[] value) {
            if (android.bluetooth.BluetoothGatt.VDBG)
                android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, (("onDescriptorRead() - Device=" + address) + " handle=") + handle);

            if (!address.equals(mDevice.getAddress())) {
                return;
            }
            synchronized(mDeviceBusy) {
                mDeviceBusy = false;
            }
            android.bluetooth.BluetoothGattDescriptor descriptor = getDescriptorById(mDevice, handle);
            if (descriptor == null)
                return;

            if (status == 0)
                descriptor.setValue(value);

            if (((status == android.bluetooth.BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION) || (status == android.bluetooth.BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION)) && (mAuthRetry == false)) {
                try {
                    mAuthRetry = true;
                    mService.readDescriptor(mClientIf, address, handle, android.bluetooth.BluetoothGatt.AUTHENTICATION_MITM);
                    return;
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
                }
            }
            mAuthRetry = true;
            try {
                mCallback.onDescriptorRead(android.bluetooth.BluetoothGatt.this, descriptor, status);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGatt.TAG, "Unhandled exception in callback", ex);
            }
        }

        /**
         * Descriptor write operation complete.
         *
         * @unknown 
         */
        public void onDescriptorWrite(java.lang.String address, int status, int handle) {
            if (android.bluetooth.BluetoothGatt.VDBG)
                android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, (("onDescriptorWrite() - Device=" + address) + " handle=") + handle);

            if (!address.equals(mDevice.getAddress())) {
                return;
            }
            synchronized(mDeviceBusy) {
                mDeviceBusy = false;
            }
            android.bluetooth.BluetoothGattDescriptor descriptor = getDescriptorById(mDevice, handle);
            if (descriptor == null)
                return;

            if (((status == android.bluetooth.BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION) || (status == android.bluetooth.BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION)) && (mAuthRetry == false)) {
                try {
                    mAuthRetry = true;
                    mService.writeDescriptor(mClientIf, address, handle, android.bluetooth.BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT, android.bluetooth.BluetoothGatt.AUTHENTICATION_MITM, descriptor.getValue());
                    return;
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
                }
            }
            mAuthRetry = false;
            try {
                mCallback.onDescriptorWrite(android.bluetooth.BluetoothGatt.this, descriptor, status);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGatt.TAG, "Unhandled exception in callback", ex);
            }
        }

        /**
         * Prepared write transaction completed (or aborted)
         *
         * @unknown 
         */
        public void onExecuteWrite(java.lang.String address, int status) {
            if (android.bluetooth.BluetoothGatt.VDBG)
                android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, (("onExecuteWrite() - Device=" + address) + " status=") + status);

            if (!address.equals(mDevice.getAddress())) {
                return;
            }
            synchronized(mDeviceBusy) {
                mDeviceBusy = false;
            }
            try {
                mCallback.onReliableWriteCompleted(android.bluetooth.BluetoothGatt.this, status);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGatt.TAG, "Unhandled exception in callback", ex);
            }
        }

        /**
         * Remote device RSSI has been read
         *
         * @unknown 
         */
        public void onReadRemoteRssi(java.lang.String address, int rssi, int status) {
            if (android.bluetooth.BluetoothGatt.VDBG)
                android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, (((("onReadRemoteRssi() - Device=" + address) + " rssi=") + rssi) + " status=") + status);

            if (!address.equals(mDevice.getAddress())) {
                return;
            }
            try {
                mCallback.onReadRemoteRssi(android.bluetooth.BluetoothGatt.this, rssi, status);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGatt.TAG, "Unhandled exception in callback", ex);
            }
        }

        /**
         * Callback invoked when the MTU for a given connection changes
         *
         * @unknown 
         */
        public void onConfigureMTU(java.lang.String address, int mtu, int status) {
            if (android.bluetooth.BluetoothGatt.DBG)
                android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, (((("onConfigureMTU() - Device=" + address) + " mtu=") + mtu) + " status=") + status);

            if (!address.equals(mDevice.getAddress())) {
                return;
            }
            try {
                mCallback.onMtuChanged(android.bluetooth.BluetoothGatt.this, mtu, status);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGatt.TAG, "Unhandled exception in callback", ex);
            }
        }
    };

    /* package */
    BluetoothGatt(android.bluetooth.IBluetoothGatt iGatt, android.bluetooth.BluetoothDevice device, int transport) {
        mService = iGatt;
        mDevice = device;
        mTransport = transport;
        mServices = new java.util.ArrayList<android.bluetooth.BluetoothGattService>();
        mConnState = android.bluetooth.BluetoothGatt.CONN_STATE_IDLE;
    }

    /**
     * Close this Bluetooth GATT client.
     *
     * Application should call this method as early as possible after it is done with
     * this GATT client.
     */
    public void close() {
        if (android.bluetooth.BluetoothGatt.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, "close()");

        unregisterApp();
        mConnState = android.bluetooth.BluetoothGatt.CONN_STATE_CLOSED;
    }

    /**
     * Returns a service by UUID, instance and type.
     *
     * @unknown 
     */
    /* package */
    android.bluetooth.BluetoothGattService getService(android.bluetooth.BluetoothDevice device, java.util.UUID uuid, int instanceId, int type) {
        for (android.bluetooth.BluetoothGattService svc : mServices) {
            if (((svc.getDevice().equals(device) && (svc.getType() == type)) && (svc.getInstanceId() == instanceId)) && svc.getUuid().equals(uuid)) {
                return svc;
            }
        }
        return null;
    }

    /**
     * Returns a characteristic with id equal to instanceId.
     *
     * @unknown 
     */
    /* package */
    android.bluetooth.BluetoothGattCharacteristic getCharacteristicById(android.bluetooth.BluetoothDevice device, int instanceId) {
        for (android.bluetooth.BluetoothGattService svc : mServices) {
            for (android.bluetooth.BluetoothGattCharacteristic charac : svc.getCharacteristics()) {
                if (charac.getInstanceId() == instanceId)
                    return charac;

            }
        }
        return null;
    }

    /**
     * Returns a descriptor with id equal to instanceId.
     *
     * @unknown 
     */
    /* package */
    android.bluetooth.BluetoothGattDescriptor getDescriptorById(android.bluetooth.BluetoothDevice device, int instanceId) {
        for (android.bluetooth.BluetoothGattService svc : mServices) {
            for (android.bluetooth.BluetoothGattCharacteristic charac : svc.getCharacteristics()) {
                for (android.bluetooth.BluetoothGattDescriptor desc : charac.getDescriptors()) {
                    if (desc.getInstanceId() == instanceId)
                        return desc;

                }
            }
        }
        return null;
    }

    /**
     * Register an application callback to start using GATT.
     *
     * <p>This is an asynchronous call. The callback {@link BluetoothGattCallback#onAppRegistered}
     * is used to notify success or failure if the function returns true.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param callback
     * 		GATT callback handler that will receive asynchronous callbacks.
     * @return If true, the callback will be called to notify success or failure,
    false on immediate error
     */
    private boolean registerApp(android.bluetooth.BluetoothGattCallback callback) {
        if (android.bluetooth.BluetoothGatt.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, "registerApp()");

        if (mService == null)
            return false;

        mCallback = callback;
        java.util.UUID uuid = java.util.UUID.randomUUID();
        if (android.bluetooth.BluetoothGatt.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, "registerApp() - UUID=" + uuid);

        try {
            mService.registerClient(new android.os.ParcelUuid(uuid), mBluetoothGattCallback);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
            return false;
        }
        return true;
    }

    /**
     * Unregister the current application and callbacks.
     */
    private void unregisterApp() {
        if (android.bluetooth.BluetoothGatt.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, "unregisterApp() - mClientIf=" + mClientIf);

        if ((mService == null) || (mClientIf == 0))
            return;

        try {
            mCallback = null;
            mService.unregisterClient(mClientIf);
            mClientIf = 0;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
        }
    }

    /**
     * Initiate a connection to a Bluetooth GATT capable device.
     *
     * <p>The connection may not be established right away, but will be
     * completed when the remote device is available. A
     * {@link BluetoothGattCallback#onConnectionStateChange} callback will be
     * invoked when the connection state changes as a result of this function.
     *
     * <p>The autoConnect parameter determines whether to actively connect to
     * the remote device, or rather passively scan and finalize the connection
     * when the remote device is in range/available. Generally, the first ever
     * connection to a device should be direct (autoConnect set to false) and
     * subsequent connections to known devices should be invoked with the
     * autoConnect parameter set to true.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param device
     * 		Remote device to connect to
     * @param autoConnect
     * 		Whether to directly connect to the remote device (false)
     * 		or to automatically connect as soon as the remote
     * 		device becomes available (true).
     * @return true, if the connection attempt was initiated successfully
     */
    /* package */
    boolean connect(java.lang.Boolean autoConnect, android.bluetooth.BluetoothGattCallback callback) {
        if (android.bluetooth.BluetoothGatt.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, (("connect() - device: " + mDevice.getAddress()) + ", auto: ") + autoConnect);

        synchronized(mStateLock) {
            if (mConnState != android.bluetooth.BluetoothGatt.CONN_STATE_IDLE) {
                throw new java.lang.IllegalStateException("Not idle");
            }
            mConnState = android.bluetooth.BluetoothGatt.CONN_STATE_CONNECTING;
        }
        mAutoConnect = autoConnect;
        if (!registerApp(callback)) {
            synchronized(mStateLock) {
                mConnState = android.bluetooth.BluetoothGatt.CONN_STATE_IDLE;
            }
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "Failed to register callback");
            return false;
        }
        // The connection will continue in the onClientRegistered callback
        return true;
    }

    /**
     * Disconnects an established connection, or cancels a connection attempt
     * currently in progress.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     */
    public void disconnect() {
        if (android.bluetooth.BluetoothGatt.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, "cancelOpen() - device: " + mDevice.getAddress());

        if ((mService == null) || (mClientIf == 0))
            return;

        try {
            mService.clientDisconnect(mClientIf, mDevice.getAddress());
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
        }
    }

    /**
     * Connect back to remote device.
     *
     * <p>This method is used to re-connect to a remote device after the
     * connection has been dropped. If the device is not in range, the
     * re-connection will be triggered once the device is back in range.
     *
     * @return true, if the connection attempt was initiated successfully
     */
    public boolean connect() {
        try {
            mService.clientConnect(mClientIf, mDevice.getAddress(), false, mTransport);// autoConnect is inverse of "isDirect"

            return true;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
            return false;
        }
    }

    /**
     * Return the remote bluetooth device this GATT client targets to
     *
     * @return remote bluetooth device
     */
    public android.bluetooth.BluetoothDevice getDevice() {
        return mDevice;
    }

    /**
     * Discovers services offered by a remote device as well as their
     * characteristics and descriptors.
     *
     * <p>This is an asynchronous operation. Once service discovery is completed,
     * the {@link BluetoothGattCallback#onServicesDiscovered} callback is
     * triggered. If the discovery was successful, the remote services can be
     * retrieved using the {@link #getServices} function.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @return true, if the remote service discovery has been started
     */
    public boolean discoverServices() {
        if (android.bluetooth.BluetoothGatt.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, "discoverServices() - device: " + mDevice.getAddress());

        if ((mService == null) || (mClientIf == 0))
            return false;

        mServices.clear();
        try {
            mService.discoverServices(mClientIf, mDevice.getAddress());
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
            return false;
        }
        return true;
    }

    /**
     * Returns a list of GATT services offered by the remote device.
     *
     * <p>This function requires that service discovery has been completed
     * for the given device.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @return List of services on the remote device. Returns an empty list
    if service discovery has not yet been performed.
     */
    public java.util.List<android.bluetooth.BluetoothGattService> getServices() {
        java.util.List<android.bluetooth.BluetoothGattService> result = new java.util.ArrayList<android.bluetooth.BluetoothGattService>();
        for (android.bluetooth.BluetoothGattService service : mServices) {
            if (service.getDevice().equals(mDevice)) {
                result.add(service);
            }
        }
        return result;
    }

    /**
     * Returns a {@link BluetoothGattService}, if the requested UUID is
     * supported by the remote device.
     *
     * <p>This function requires that service discovery has been completed
     * for the given device.
     *
     * <p>If multiple instances of the same service (as identified by UUID)
     * exist, the first instance of the service is returned.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param uuid
     * 		UUID of the requested service
     * @return BluetoothGattService if supported, or null if the requested
    service is not offered by the remote device.
     */
    public android.bluetooth.BluetoothGattService getService(java.util.UUID uuid) {
        for (android.bluetooth.BluetoothGattService service : mServices) {
            if (service.getDevice().equals(mDevice) && service.getUuid().equals(uuid)) {
                return service;
            }
        }
        return null;
    }

    /**
     * Reads the requested characteristic from the associated remote device.
     *
     * <p>This is an asynchronous operation. The result of the read operation
     * is reported by the {@link BluetoothGattCallback#onCharacteristicRead}
     * callback.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param characteristic
     * 		Characteristic to read from the remote device
     * @return true, if the read operation was initiated successfully
     */
    public boolean readCharacteristic(android.bluetooth.BluetoothGattCharacteristic characteristic) {
        if ((characteristic.getProperties() & android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ) == 0)
            return false;

        if (android.bluetooth.BluetoothGatt.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, "readCharacteristic() - uuid: " + characteristic.getUuid());

        if ((mService == null) || (mClientIf == 0))
            return false;

        android.bluetooth.BluetoothGattService service = characteristic.getService();
        if (service == null)
            return false;

        android.bluetooth.BluetoothDevice device = service.getDevice();
        if (device == null)
            return false;

        synchronized(mDeviceBusy) {
            if (mDeviceBusy)
                return false;

            mDeviceBusy = true;
        }
        try {
            mService.readCharacteristic(mClientIf, device.getAddress(), characteristic.getInstanceId(), android.bluetooth.BluetoothGatt.AUTHENTICATION_NONE);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
            mDeviceBusy = false;
            return false;
        }
        return true;
    }

    /**
     * Writes a given characteristic and its values to the associated remote device.
     *
     * <p>Once the write operation has been completed, the
     * {@link BluetoothGattCallback#onCharacteristicWrite} callback is invoked,
     * reporting the result of the operation.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param characteristic
     * 		Characteristic to write on the remote device
     * @return true, if the write operation was initiated successfully
     */
    public boolean writeCharacteristic(android.bluetooth.BluetoothGattCharacteristic characteristic) {
        if (((characteristic.getProperties() & android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE) == 0) && ((characteristic.getProperties() & android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) == 0))
            return false;

        if (android.bluetooth.BluetoothGatt.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, "writeCharacteristic() - uuid: " + characteristic.getUuid());

        if (((mService == null) || (mClientIf == 0)) || (characteristic.getValue() == null))
            return false;

        android.bluetooth.BluetoothGattService service = characteristic.getService();
        if (service == null)
            return false;

        android.bluetooth.BluetoothDevice device = service.getDevice();
        if (device == null)
            return false;

        synchronized(mDeviceBusy) {
            if (mDeviceBusy)
                return false;

            mDeviceBusy = true;
        }
        try {
            mService.writeCharacteristic(mClientIf, device.getAddress(), characteristic.getInstanceId(), characteristic.getWriteType(), android.bluetooth.BluetoothGatt.AUTHENTICATION_NONE, characteristic.getValue());
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
            mDeviceBusy = false;
            return false;
        }
        return true;
    }

    /**
     * Reads the value for a given descriptor from the associated remote device.
     *
     * <p>Once the read operation has been completed, the
     * {@link BluetoothGattCallback#onDescriptorRead} callback is
     * triggered, signaling the result of the operation.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param descriptor
     * 		Descriptor value to read from the remote device
     * @return true, if the read operation was initiated successfully
     */
    public boolean readDescriptor(android.bluetooth.BluetoothGattDescriptor descriptor) {
        if (android.bluetooth.BluetoothGatt.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, "readDescriptor() - uuid: " + descriptor.getUuid());

        if ((mService == null) || (mClientIf == 0))
            return false;

        android.bluetooth.BluetoothGattCharacteristic characteristic = descriptor.getCharacteristic();
        if (characteristic == null)
            return false;

        android.bluetooth.BluetoothGattService service = characteristic.getService();
        if (service == null)
            return false;

        android.bluetooth.BluetoothDevice device = service.getDevice();
        if (device == null)
            return false;

        synchronized(mDeviceBusy) {
            if (mDeviceBusy)
                return false;

            mDeviceBusy = true;
        }
        try {
            mService.readDescriptor(mClientIf, device.getAddress(), descriptor.getInstanceId(), android.bluetooth.BluetoothGatt.AUTHENTICATION_NONE);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
            mDeviceBusy = false;
            return false;
        }
        return true;
    }

    /**
     * Write the value of a given descriptor to the associated remote device.
     *
     * <p>A {@link BluetoothGattCallback#onDescriptorWrite} callback is
     * triggered to report the result of the write operation.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param descriptor
     * 		Descriptor to write to the associated remote device
     * @return true, if the write operation was initiated successfully
     */
    public boolean writeDescriptor(android.bluetooth.BluetoothGattDescriptor descriptor) {
        if (android.bluetooth.BluetoothGatt.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, "writeDescriptor() - uuid: " + descriptor.getUuid());

        if (((mService == null) || (mClientIf == 0)) || (descriptor.getValue() == null))
            return false;

        android.bluetooth.BluetoothGattCharacteristic characteristic = descriptor.getCharacteristic();
        if (characteristic == null)
            return false;

        android.bluetooth.BluetoothGattService service = characteristic.getService();
        if (service == null)
            return false;

        android.bluetooth.BluetoothDevice device = service.getDevice();
        if (device == null)
            return false;

        synchronized(mDeviceBusy) {
            if (mDeviceBusy)
                return false;

            mDeviceBusy = true;
        }
        try {
            mService.writeDescriptor(mClientIf, device.getAddress(), descriptor.getInstanceId(), android.bluetooth.BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT, android.bluetooth.BluetoothGatt.AUTHENTICATION_NONE, descriptor.getValue());
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
            mDeviceBusy = false;
            return false;
        }
        return true;
    }

    /**
     * Initiates a reliable write transaction for a given remote device.
     *
     * <p>Once a reliable write transaction has been initiated, all calls
     * to {@link #writeCharacteristic} are sent to the remote device for
     * verification and queued up for atomic execution. The application will
     * receive an {@link BluetoothGattCallback#onCharacteristicWrite} callback
     * in response to every {@link #writeCharacteristic} call and is responsible
     * for verifying if the value has been transmitted accurately.
     *
     * <p>After all characteristics have been queued up and verified,
     * {@link #executeReliableWrite} will execute all writes. If a characteristic
     * was not written correctly, calling {@link #abortReliableWrite} will
     * cancel the current transaction without commiting any values on the
     * remote device.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @return true, if the reliable write transaction has been initiated
     */
    public boolean beginReliableWrite() {
        if (android.bluetooth.BluetoothGatt.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, "beginReliableWrite() - device: " + mDevice.getAddress());

        if ((mService == null) || (mClientIf == 0))
            return false;

        try {
            mService.beginReliableWrite(mClientIf, mDevice.getAddress());
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
            return false;
        }
        return true;
    }

    /**
     * Executes a reliable write transaction for a given remote device.
     *
     * <p>This function will commit all queued up characteristic write
     * operations for a given remote device.
     *
     * <p>A {@link BluetoothGattCallback#onReliableWriteCompleted} callback is
     * invoked to indicate whether the transaction has been executed correctly.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @return true, if the request to execute the transaction has been sent
     */
    public boolean executeReliableWrite() {
        if (android.bluetooth.BluetoothGatt.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, "executeReliableWrite() - device: " + mDevice.getAddress());

        if ((mService == null) || (mClientIf == 0))
            return false;

        synchronized(mDeviceBusy) {
            if (mDeviceBusy)
                return false;

            mDeviceBusy = true;
        }
        try {
            mService.endReliableWrite(mClientIf, mDevice.getAddress(), true);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
            mDeviceBusy = false;
            return false;
        }
        return true;
    }

    /**
     * Cancels a reliable write transaction for a given device.
     *
     * <p>Calling this function will discard all queued characteristic write
     * operations for a given remote device.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     */
    public void abortReliableWrite() {
        if (android.bluetooth.BluetoothGatt.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, "abortReliableWrite() - device: " + mDevice.getAddress());

        if ((mService == null) || (mClientIf == 0))
            return;

        try {
            mService.endReliableWrite(mClientIf, mDevice.getAddress(), false);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
        }
    }

    /**
     *
     *
     * @deprecated Use {@link #abortReliableWrite()}
     */
    public void abortReliableWrite(android.bluetooth.BluetoothDevice mDevice) {
        abortReliableWrite();
    }

    /**
     * Enable or disable notifications/indications for a given characteristic.
     *
     * <p>Once notifications are enabled for a characteristic, a
     * {@link BluetoothGattCallback#onCharacteristicChanged} callback will be
     * triggered if the remote device indicates that the given characteristic
     * has changed.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param characteristic
     * 		The characteristic for which to enable notifications
     * @param enable
     * 		Set to true to enable notifications/indications
     * @return true, if the requested notification status was set successfully
     */
    public boolean setCharacteristicNotification(android.bluetooth.BluetoothGattCharacteristic characteristic, boolean enable) {
        if (android.bluetooth.BluetoothGatt.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, (("setCharacteristicNotification() - uuid: " + characteristic.getUuid()) + " enable: ") + enable);

        if ((mService == null) || (mClientIf == 0))
            return false;

        android.bluetooth.BluetoothGattService service = characteristic.getService();
        if (service == null)
            return false;

        android.bluetooth.BluetoothDevice device = service.getDevice();
        if (device == null)
            return false;

        try {
            mService.registerForNotification(mClientIf, device.getAddress(), characteristic.getInstanceId(), enable);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
            return false;
        }
        return true;
    }

    /**
     * Clears the internal cache and forces a refresh of the services from the
     * remote device.
     *
     * @unknown 
     */
    public boolean refresh() {
        if (android.bluetooth.BluetoothGatt.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, "refresh() - device: " + mDevice.getAddress());

        if ((mService == null) || (mClientIf == 0))
            return false;

        try {
            mService.refreshDevice(mClientIf, mDevice.getAddress());
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
            return false;
        }
        return true;
    }

    /**
     * Read the RSSI for a connected remote device.
     *
     * <p>The {@link BluetoothGattCallback#onReadRemoteRssi} callback will be
     * invoked when the RSSI value has been read.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @return true, if the RSSI value has been requested successfully
     */
    public boolean readRemoteRssi() {
        if (android.bluetooth.BluetoothGatt.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, "readRssi() - device: " + mDevice.getAddress());

        if ((mService == null) || (mClientIf == 0))
            return false;

        try {
            mService.readRemoteRssi(mClientIf, mDevice.getAddress());
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
            return false;
        }
        return true;
    }

    /**
     * Request an MTU size used for a given connection.
     *
     * <p>When performing a write request operation (write without response),
     * the data sent is truncated to the MTU size. This function may be used
     * to request a larger MTU size to be able to send more data at once.
     *
     * <p>A {@link BluetoothGattCallback#onMtuChanged} callback will indicate
     * whether this operation was successful.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @return true, if the new MTU value has been requested successfully
     */
    public boolean requestMtu(int mtu) {
        if (android.bluetooth.BluetoothGatt.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, (("configureMTU() - device: " + mDevice.getAddress()) + " mtu: ") + mtu);

        if ((mService == null) || (mClientIf == 0))
            return false;

        try {
            mService.configureMTU(mClientIf, mDevice.getAddress(), mtu);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
            return false;
        }
        return true;
    }

    /**
     * Request a connection parameter update.
     *
     * <p>This function will send a connection parameter update request to the
     * remote device.
     *
     * @param connectionPriority
     * 		Request a specific connection priority. Must be one of
     * 		{@link BluetoothGatt#CONNECTION_PRIORITY_BALANCED},
     * 		{@link BluetoothGatt#CONNECTION_PRIORITY_HIGH}
     * 		or {@link BluetoothGatt#CONNECTION_PRIORITY_LOW_POWER}.
     * @throws IllegalArgumentException
     * 		If the parameters are outside of their
     * 		specified range.
     */
    public boolean requestConnectionPriority(int connectionPriority) {
        if ((connectionPriority < android.bluetooth.BluetoothGatt.CONNECTION_PRIORITY_BALANCED) || (connectionPriority > android.bluetooth.BluetoothGatt.CONNECTION_PRIORITY_LOW_POWER)) {
            throw new java.lang.IllegalArgumentException("connectionPriority not within valid range");
        }
        if (android.bluetooth.BluetoothGatt.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGatt.TAG, "requestConnectionPriority() - params: " + connectionPriority);

        if ((mService == null) || (mClientIf == 0))
            return false;

        try {
            mService.connectionParameterUpdate(mClientIf, mDevice.getAddress(), connectionPriority);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGatt.TAG, "", e);
            return false;
        }
        return true;
    }

    /**
     * Not supported - please use {@link BluetoothManager#getConnectedDevices(int)}
     * with {@link BluetoothProfile#GATT} as argument
     *
     * @throws UnsupportedOperationException
     * 		
     */
    @java.lang.Override
    public int getConnectionState(android.bluetooth.BluetoothDevice device) {
        throw new java.lang.UnsupportedOperationException("Use BluetoothManager#getConnectionState instead.");
    }

    /**
     * Not supported - please use {@link BluetoothManager#getConnectedDevices(int)}
     * with {@link BluetoothProfile#GATT} as argument
     *
     * @throws UnsupportedOperationException
     * 		
     */
    @java.lang.Override
    public java.util.List<android.bluetooth.BluetoothDevice> getConnectedDevices() {
        throw new java.lang.UnsupportedOperationException("Use BluetoothManager#getConnectedDevices instead.");
    }

    /**
     * Not supported - please use
     * {@link BluetoothManager#getDevicesMatchingConnectionStates(int, int[])}
     * with {@link BluetoothProfile#GATT} as first argument
     *
     * @throws UnsupportedOperationException
     * 		
     */
    @java.lang.Override
    public java.util.List<android.bluetooth.BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        throw new java.lang.UnsupportedOperationException("Use BluetoothManager#getDevicesMatchingConnectionStates instead.");
    }
}

