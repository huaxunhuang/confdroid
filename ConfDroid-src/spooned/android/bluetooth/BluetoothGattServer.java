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
 * Public API for the Bluetooth GATT Profile server role.
 *
 * <p>This class provides Bluetooth GATT server role functionality,
 * allowing applications to create Bluetooth Smart services and
 * characteristics.
 *
 * <p>BluetoothGattServer is a proxy object for controlling the Bluetooth Service
 * via IPC.  Use {@link BluetoothManager#openGattServer} to get an instance
 * of this class.
 */
public final class BluetoothGattServer implements android.bluetooth.BluetoothProfile {
    private static final java.lang.String TAG = "BluetoothGattServer";

    private static final boolean DBG = true;

    private static final boolean VDBG = false;

    private android.bluetooth.BluetoothAdapter mAdapter;

    private android.bluetooth.IBluetoothGatt mService;

    private android.bluetooth.BluetoothGattServerCallback mCallback;

    private java.lang.Object mServerIfLock = new java.lang.Object();

    private int mServerIf;

    private int mTransport;

    private java.util.List<android.bluetooth.BluetoothGattService> mServices;

    private static final int CALLBACK_REG_TIMEOUT = 10000;

    /**
     * Bluetooth GATT interface callbacks
     */
    private final android.bluetooth.IBluetoothGattServerCallback mBluetoothGattServerCallback = new android.bluetooth.IBluetoothGattServerCallback.Stub() {
        /**
         * Application interface registered - app is ready to go
         *
         * @unknown 
         */
        public void onServerRegistered(int status, int serverIf) {
            if (android.bluetooth.BluetoothGattServer.DBG)
                android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, (("onServerRegistered() - status=" + status) + " serverIf=") + serverIf);

            synchronized(mServerIfLock) {
                if (mCallback != null) {
                    mServerIf = serverIf;
                    mServerIfLock.notify();
                } else {
                    // registration timeout
                    android.util.Log.e(android.bluetooth.BluetoothGattServer.TAG, "onServerRegistered: mCallback is null");
                }
            }
        }

        /**
         * Callback reporting an LE scan result.
         *
         * @unknown 
         */
        public void onScanResult(java.lang.String address, int rssi, byte[] advData) {
            if (android.bluetooth.BluetoothGattServer.VDBG)
                android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, (("onScanResult() - Device=" + address) + " RSSI=") + rssi);

            // no op
        }

        /**
         * Server connection state changed
         *
         * @unknown 
         */
        public void onServerConnectionState(int status, int serverIf, boolean connected, java.lang.String address) {
            if (android.bluetooth.BluetoothGattServer.DBG)
                android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, (((("onServerConnectionState() - status=" + status) + " serverIf=") + serverIf) + " device=") + address);

            try {
                mCallback.onConnectionStateChange(mAdapter.getRemoteDevice(address), status, connected ? android.bluetooth.BluetoothProfile.STATE_CONNECTED : android.bluetooth.BluetoothProfile.STATE_DISCONNECTED);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
            }
        }

        /**
         * Service has been added
         *
         * @unknown 
         */
        public void onServiceAdded(int status, int srvcType, int srvcInstId, android.os.ParcelUuid srvcId) {
            java.util.UUID srvcUuid = srvcId.getUuid();
            if (android.bluetooth.BluetoothGattServer.DBG)
                android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, (("onServiceAdded() - service=" + srvcUuid) + "status=") + status);

            android.bluetooth.BluetoothGattService service = getService(srvcUuid, srvcInstId, srvcType);
            if (service == null)
                return;

            try {
                mCallback.onServiceAdded(((int) (status)), service);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
            }
        }

        /**
         * Remote client characteristic read request.
         *
         * @unknown 
         */
        public void onCharacteristicReadRequest(java.lang.String address, int transId, int offset, boolean isLong, int srvcType, int srvcInstId, android.os.ParcelUuid srvcId, int charInstId, android.os.ParcelUuid charId) {
            java.util.UUID srvcUuid = srvcId.getUuid();
            java.util.UUID charUuid = charId.getUuid();
            if (android.bluetooth.BluetoothGattServer.VDBG)
                android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, ((("onCharacteristicReadRequest() - " + "service=") + srvcUuid) + ", characteristic=") + charUuid);

            android.bluetooth.BluetoothDevice device = mAdapter.getRemoteDevice(address);
            android.bluetooth.BluetoothGattService service = getService(srvcUuid, srvcInstId, srvcType);
            if (service == null)
                return;

            android.bluetooth.BluetoothGattCharacteristic characteristic = service.getCharacteristic(charUuid);
            if (characteristic == null)
                return;

            try {
                mCallback.onCharacteristicReadRequest(device, transId, offset, characteristic);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
            }
        }

        /**
         * Remote client descriptor read request.
         *
         * @unknown 
         */
        public void onDescriptorReadRequest(java.lang.String address, int transId, int offset, boolean isLong, int srvcType, int srvcInstId, android.os.ParcelUuid srvcId, int charInstId, android.os.ParcelUuid charId, android.os.ParcelUuid descrId) {
            java.util.UUID srvcUuid = srvcId.getUuid();
            java.util.UUID charUuid = charId.getUuid();
            java.util.UUID descrUuid = descrId.getUuid();
            if (android.bluetooth.BluetoothGattServer.VDBG)
                android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, ((((("onCharacteristicReadRequest() - " + "service=") + srvcUuid) + ", characteristic=") + charUuid) + "descriptor=") + descrUuid);

            android.bluetooth.BluetoothDevice device = mAdapter.getRemoteDevice(address);
            android.bluetooth.BluetoothGattService service = getService(srvcUuid, srvcInstId, srvcType);
            if (service == null)
                return;

            android.bluetooth.BluetoothGattCharacteristic characteristic = service.getCharacteristic(charUuid);
            if (characteristic == null)
                return;

            android.bluetooth.BluetoothGattDescriptor descriptor = characteristic.getDescriptor(descrUuid);
            if (descriptor == null)
                return;

            try {
                mCallback.onDescriptorReadRequest(device, transId, offset, descriptor);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
            }
        }

        /**
         * Remote client characteristic write request.
         *
         * @unknown 
         */
        public void onCharacteristicWriteRequest(java.lang.String address, int transId, int offset, int length, boolean isPrep, boolean needRsp, int srvcType, int srvcInstId, android.os.ParcelUuid srvcId, int charInstId, android.os.ParcelUuid charId, byte[] value) {
            java.util.UUID srvcUuid = srvcId.getUuid();
            java.util.UUID charUuid = charId.getUuid();
            if (android.bluetooth.BluetoothGattServer.VDBG)
                android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, ((("onCharacteristicWriteRequest() - " + "service=") + srvcUuid) + ", characteristic=") + charUuid);

            android.bluetooth.BluetoothDevice device = mAdapter.getRemoteDevice(address);
            android.bluetooth.BluetoothGattService service = getService(srvcUuid, srvcInstId, srvcType);
            if (service == null)
                return;

            android.bluetooth.BluetoothGattCharacteristic characteristic = service.getCharacteristic(charUuid);
            if (characteristic == null)
                return;

            try {
                mCallback.onCharacteristicWriteRequest(device, transId, characteristic, isPrep, needRsp, offset, value);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
            }
        }

        /**
         * Remote client descriptor write request.
         *
         * @unknown 
         */
        public void onDescriptorWriteRequest(java.lang.String address, int transId, int offset, int length, boolean isPrep, boolean needRsp, int srvcType, int srvcInstId, android.os.ParcelUuid srvcId, int charInstId, android.os.ParcelUuid charId, android.os.ParcelUuid descrId, byte[] value) {
            java.util.UUID srvcUuid = srvcId.getUuid();
            java.util.UUID charUuid = charId.getUuid();
            java.util.UUID descrUuid = descrId.getUuid();
            if (android.bluetooth.BluetoothGattServer.VDBG)
                android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, ((((("onDescriptorWriteRequest() - " + "service=") + srvcUuid) + ", characteristic=") + charUuid) + "descriptor=") + descrUuid);

            android.bluetooth.BluetoothDevice device = mAdapter.getRemoteDevice(address);
            android.bluetooth.BluetoothGattService service = getService(srvcUuid, srvcInstId, srvcType);
            if (service == null)
                return;

            android.bluetooth.BluetoothGattCharacteristic characteristic = service.getCharacteristic(charUuid);
            if (characteristic == null)
                return;

            android.bluetooth.BluetoothGattDescriptor descriptor = characteristic.getDescriptor(descrUuid);
            if (descriptor == null)
                return;

            try {
                mCallback.onDescriptorWriteRequest(device, transId, descriptor, isPrep, needRsp, offset, value);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
            }
        }

        /**
         * Execute pending writes.
         *
         * @unknown 
         */
        public void onExecuteWrite(java.lang.String address, int transId, boolean execWrite) {
            if (android.bluetooth.BluetoothGattServer.DBG)
                android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, ((((("onExecuteWrite() - " + "device=") + address) + ", transId=") + transId) + "execWrite=") + execWrite);

            android.bluetooth.BluetoothDevice device = mAdapter.getRemoteDevice(address);
            if (device == null)
                return;

            try {
                mCallback.onExecuteWrite(device, transId, execWrite);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
            }
        }

        /**
         * A notification/indication has been sent.
         *
         * @unknown 
         */
        public void onNotificationSent(java.lang.String address, int status) {
            if (android.bluetooth.BluetoothGattServer.VDBG)
                android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, ((("onNotificationSent() - " + "device=") + address) + ", status=") + status);

            android.bluetooth.BluetoothDevice device = mAdapter.getRemoteDevice(address);
            if (device == null)
                return;

            try {
                mCallback.onNotificationSent(device, status);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGattServer.TAG, "Unhandled exception: " + ex);
            }
        }

        /**
         * The MTU for a connection has changed
         *
         * @unknown 
         */
        public void onMtuChanged(java.lang.String address, int mtu) {
            if (android.bluetooth.BluetoothGattServer.DBG)
                android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, ((("onMtuChanged() - " + "device=") + address) + ", mtu=") + mtu);

            android.bluetooth.BluetoothDevice device = mAdapter.getRemoteDevice(address);
            if (device == null)
                return;

            try {
                mCallback.onMtuChanged(device, mtu);
            } catch (java.lang.Exception ex) {
                android.util.Log.w(android.bluetooth.BluetoothGattServer.TAG, "Unhandled exception: " + ex);
            }
        }
    };

    /**
     * Create a BluetoothGattServer proxy object.
     */
    /* package */
    BluetoothGattServer(android.bluetooth.IBluetoothGatt iGatt, int transport) {
        mService = iGatt;
        mAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        mCallback = null;
        mServerIf = 0;
        mTransport = transport;
        mServices = new java.util.ArrayList<android.bluetooth.BluetoothGattService>();
    }

    /**
     * Close this GATT server instance.
     *
     * Application should call this method as early as possible after it is done with
     * this GATT server.
     */
    public void close() {
        if (android.bluetooth.BluetoothGattServer.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, "close()");

        unregisterCallback();
    }

    /**
     * Register an application callback to start using GattServer.
     *
     * <p>This is an asynchronous call. The callback is used to notify
     * success or failure if the function returns true.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param callback
     * 		GATT callback handler that will receive asynchronous
     * 		callbacks.
     * @return true, the callback will be called to notify success or failure,
    false on immediate error
     */
    /* package */
    boolean registerCallback(android.bluetooth.BluetoothGattServerCallback callback) {
        if (android.bluetooth.BluetoothGattServer.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, "registerCallback()");

        if (mService == null) {
            android.util.Log.e(android.bluetooth.BluetoothGattServer.TAG, "GATT service not available");
            return false;
        }
        java.util.UUID uuid = java.util.UUID.randomUUID();
        if (android.bluetooth.BluetoothGattServer.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, "registerCallback() - UUID=" + uuid);

        synchronized(mServerIfLock) {
            if (mCallback != null) {
                android.util.Log.e(android.bluetooth.BluetoothGattServer.TAG, "App can register callback only once");
                return false;
            }
            mCallback = callback;
            try {
                mService.registerServer(new android.os.ParcelUuid(uuid), mBluetoothGattServerCallback);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.bluetooth.BluetoothGattServer.TAG, "", e);
                mCallback = null;
                return false;
            }
            try {
                mServerIfLock.wait(android.bluetooth.BluetoothGattServer.CALLBACK_REG_TIMEOUT);
            } catch (java.lang.InterruptedException e) {
                android.util.Log.e(android.bluetooth.BluetoothGattServer.TAG, "" + e);
                mCallback = null;
            }
            if (mServerIf == 0) {
                mCallback = null;
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * Unregister the current application and callbacks.
     */
    private void unregisterCallback() {
        if (android.bluetooth.BluetoothGattServer.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, "unregisterCallback() - mServerIf=" + mServerIf);

        if ((mService == null) || (mServerIf == 0))
            return;

        try {
            mCallback = null;
            mService.unregisterServer(mServerIf);
            mServerIf = 0;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGattServer.TAG, "", e);
        }
    }

    /**
     * Returns a service by UUID, instance and type.
     *
     * @unknown 
     */
    /* package */
    android.bluetooth.BluetoothGattService getService(java.util.UUID uuid, int instanceId, int type) {
        for (android.bluetooth.BluetoothGattService svc : mServices) {
            if (((svc.getType() == type) && (svc.getInstanceId() == instanceId)) && svc.getUuid().equals(uuid)) {
                return svc;
            }
        }
        return null;
    }

    /**
     * Initiate a connection to a Bluetooth GATT capable device.
     *
     * <p>The connection may not be established right away, but will be
     * completed when the remote device is available. A
     * {@link BluetoothGattServerCallback#onConnectionStateChange} callback will be
     * invoked when the connection state changes as a result of this function.
     *
     * <p>The autoConnect paramter determines whether to actively connect to
     * the remote device, or rather passively scan and finalize the connection
     * when the remote device is in range/available. Generally, the first ever
     * connection to a device should be direct (autoConnect set to false) and
     * subsequent connections to known devices should be invoked with the
     * autoConnect parameter set to true.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param autoConnect
     * 		Whether to directly connect to the remote device (false)
     * 		or to automatically connect as soon as the remote
     * 		device becomes available (true).
     * @return true, if the connection attempt was initiated successfully
     */
    public boolean connect(android.bluetooth.BluetoothDevice device, boolean autoConnect) {
        if (android.bluetooth.BluetoothGattServer.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, (("connect() - device: " + device.getAddress()) + ", auto: ") + autoConnect);

        if ((mService == null) || (mServerIf == 0))
            return false;

        try {
            mService.serverConnect(mServerIf, device.getAddress(), autoConnect ? false : true, mTransport);// autoConnect is inverse of "isDirect"

        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGattServer.TAG, "", e);
            return false;
        }
        return true;
    }

    /**
     * Disconnects an established connection, or cancels a connection attempt
     * currently in progress.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param device
     * 		Remote device
     */
    public void cancelConnection(android.bluetooth.BluetoothDevice device) {
        if (android.bluetooth.BluetoothGattServer.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, "cancelConnection() - device: " + device.getAddress());

        if ((mService == null) || (mServerIf == 0))
            return;

        try {
            mService.serverDisconnect(mServerIf, device.getAddress());
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGattServer.TAG, "", e);
        }
    }

    /**
     * Send a response to a read or write request to a remote device.
     *
     * <p>This function must be invoked in when a remote read/write request
     * is received by one of these callback methods:
     *
     * <ul>
     *      <li>{@link BluetoothGattServerCallback#onCharacteristicReadRequest}
     *      <li>{@link BluetoothGattServerCallback#onCharacteristicWriteRequest}
     *      <li>{@link BluetoothGattServerCallback#onDescriptorReadRequest}
     *      <li>{@link BluetoothGattServerCallback#onDescriptorWriteRequest}
     * </ul>
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param device
     * 		The remote device to send this response to
     * @param requestId
     * 		The ID of the request that was received with the callback
     * @param status
     * 		The status of the request to be sent to the remote devices
     * @param offset
     * 		Value offset for partial read/write response
     * @param value
     * 		The value of the attribute that was read/written (optional)
     */
    public boolean sendResponse(android.bluetooth.BluetoothDevice device, int requestId, int status, int offset, byte[] value) {
        if (android.bluetooth.BluetoothGattServer.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, "sendResponse() - device: " + device.getAddress());

        if ((mService == null) || (mServerIf == 0))
            return false;

        try {
            mService.sendResponse(mServerIf, device.getAddress(), requestId, status, offset, value);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGattServer.TAG, "", e);
            return false;
        }
        return true;
    }

    /**
     * Send a notification or indication that a local characteristic has been
     * updated.
     *
     * <p>A notification or indication is sent to the remote device to signal
     * that the characteristic has been updated. This function should be invoked
     * for every client that requests notifications/indications by writing
     * to the "Client Configuration" descriptor for the given characteristic.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param device
     * 		The remote device to receive the notification/indication
     * @param characteristic
     * 		The local characteristic that has been updated
     * @param confirm
     * 		true to request confirmation from the client (indication),
     * 		false to send a notification
     * @throws IllegalArgumentException
     * 		
     * @return true, if the notification has been triggered successfully
     */
    public boolean notifyCharacteristicChanged(android.bluetooth.BluetoothDevice device, android.bluetooth.BluetoothGattCharacteristic characteristic, boolean confirm) {
        if (android.bluetooth.BluetoothGattServer.VDBG)
            android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, "notifyCharacteristicChanged() - device: " + device.getAddress());

        if ((mService == null) || (mServerIf == 0))
            return false;

        android.bluetooth.BluetoothGattService service = characteristic.getService();
        if (service == null)
            return false;

        if (characteristic.getValue() == null) {
            throw new java.lang.IllegalArgumentException("Chracteristic value is empty. Use " + "BluetoothGattCharacteristic#setvalue to update");
        }
        try {
            mService.sendNotification(mServerIf, device.getAddress(), service.getType(), service.getInstanceId(), new android.os.ParcelUuid(service.getUuid()), characteristic.getInstanceId(), new android.os.ParcelUuid(characteristic.getUuid()), confirm, characteristic.getValue());
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGattServer.TAG, "", e);
            return false;
        }
        return true;
    }

    /**
     * Add a service to the list of services to be hosted.
     *
     * <p>Once a service has been addded to the the list, the service and its
     * included characteristics will be provided by the local device.
     *
     * <p>If the local device has already exposed services when this function
     * is called, a service update notification will be sent to all clients.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param service
     * 		Service to be added to the list of services provided
     * 		by this device.
     * @return true, if the service has been added successfully
     */
    public boolean addService(android.bluetooth.BluetoothGattService service) {
        if (android.bluetooth.BluetoothGattServer.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, "addService() - service: " + service.getUuid());

        if ((mService == null) || (mServerIf == 0))
            return false;

        mServices.add(service);
        try {
            mService.beginServiceDeclaration(mServerIf, service.getType(), service.getInstanceId(), service.getHandles(), new android.os.ParcelUuid(service.getUuid()), service.isAdvertisePreferred());
            java.util.List<android.bluetooth.BluetoothGattService> includedServices = service.getIncludedServices();
            for (android.bluetooth.BluetoothGattService includedService : includedServices) {
                mService.addIncludedService(mServerIf, includedService.getType(), includedService.getInstanceId(), new android.os.ParcelUuid(includedService.getUuid()));
            }
            java.util.List<android.bluetooth.BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            for (android.bluetooth.BluetoothGattCharacteristic characteristic : characteristics) {
                int permission = ((characteristic.getKeySize() - 7) << 12) + characteristic.getPermissions();
                mService.addCharacteristic(mServerIf, new android.os.ParcelUuid(characteristic.getUuid()), characteristic.getProperties(), permission);
                java.util.List<android.bluetooth.BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                for (android.bluetooth.BluetoothGattDescriptor descriptor : descriptors) {
                    permission = ((characteristic.getKeySize() - 7) << 12) + descriptor.getPermissions();
                    mService.addDescriptor(mServerIf, new android.os.ParcelUuid(descriptor.getUuid()), permission);
                }
            }
            mService.endServiceDeclaration(mServerIf);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGattServer.TAG, "", e);
            return false;
        }
        return true;
    }

    /**
     * Removes a service from the list of services to be provided.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param service
     * 		Service to be removed.
     * @return true, if the service has been removed
     */
    public boolean removeService(android.bluetooth.BluetoothGattService service) {
        if (android.bluetooth.BluetoothGattServer.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, "removeService() - service: " + service.getUuid());

        if ((mService == null) || (mServerIf == 0))
            return false;

        android.bluetooth.BluetoothGattService intService = getService(service.getUuid(), service.getInstanceId(), service.getType());
        if (intService == null)
            return false;

        try {
            mService.removeService(mServerIf, service.getType(), service.getInstanceId(), new android.os.ParcelUuid(service.getUuid()));
            mServices.remove(intService);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGattServer.TAG, "", e);
            return false;
        }
        return true;
    }

    /**
     * Remove all services from the list of provided services.
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     */
    public void clearServices() {
        if (android.bluetooth.BluetoothGattServer.DBG)
            android.util.Log.d(android.bluetooth.BluetoothGattServer.TAG, "clearServices()");

        if ((mService == null) || (mServerIf == 0))
            return;

        try {
            mService.clearServices(mServerIf);
            mServices.clear();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bluetooth.BluetoothGattServer.TAG, "", e);
        }
    }

    /**
     * Returns a list of GATT services offered by this device.
     *
     * <p>An application must call {@link #addService} to add a serice to the
     * list of services offered by this device.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @return List of services. Returns an empty list
    if no services have been added yet.
     */
    public java.util.List<android.bluetooth.BluetoothGattService> getServices() {
        return mServices;
    }

    /**
     * Returns a {@link BluetoothGattService} from the list of services offered
     * by this device.
     *
     * <p>If multiple instances of the same service (as identified by UUID)
     * exist, the first instance of the service is returned.
     *
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param uuid
     * 		UUID of the requested service
     * @return BluetoothGattService if supported, or null if the requested
    service is not offered by this device.
     */
    public android.bluetooth.BluetoothGattService getService(java.util.UUID uuid) {
        for (android.bluetooth.BluetoothGattService service : mServices) {
            if (service.getUuid().equals(uuid)) {
                return service;
            }
        }
        return null;
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

