/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.bluetooth.le;


/**
 * This class provides methods to perform scan related operations for Bluetooth LE devices. An
 * application can scan for a particular type of Bluetooth LE devices using {@link ScanFilter}. It
 * can also request different types of callbacks for delivering the result.
 * <p>
 * Use {@link BluetoothAdapter#getBluetoothLeScanner()} to get an instance of
 * {@link BluetoothLeScanner}.
 * <p>
 * <b>Note:</b> Most of the scan methods here require
 * {@link android.Manifest.permission#BLUETOOTH_ADMIN} permission.
 *
 * @see ScanFilter
 */
public final class BluetoothLeScanner {
    private static final java.lang.String TAG = "BluetoothLeScanner";

    private static final boolean DBG = true;

    private static final boolean VDBG = false;

    private final android.bluetooth.IBluetoothManager mBluetoothManager;

    private final android.os.Handler mHandler;

    private android.bluetooth.BluetoothAdapter mBluetoothAdapter;

    private final java.util.Map<android.bluetooth.le.ScanCallback, android.bluetooth.le.BluetoothLeScanner.BleScanCallbackWrapper> mLeScanClients;

    /**
     * Use {@link BluetoothAdapter#getBluetoothLeScanner()} instead.
     *
     * @param bluetoothManager
     * 		BluetoothManager that conducts overall Bluetooth Management.
     * @unknown 
     */
    public BluetoothLeScanner(android.bluetooth.IBluetoothManager bluetoothManager) {
        mBluetoothManager = bluetoothManager;
        mBluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        mLeScanClients = new java.util.HashMap<android.bluetooth.le.ScanCallback, android.bluetooth.le.BluetoothLeScanner.BleScanCallbackWrapper>();
    }

    /**
     * Start Bluetooth LE scan with default parameters and no filters. The scan results will be
     * delivered through {@code callback}.
     * <p>
     * Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN} permission.
     * An app must hold
     * {@link android.Manifest.permission#ACCESS_COARSE_LOCATION ACCESS_COARSE_LOCATION} or
     * {@link android.Manifest.permission#ACCESS_FINE_LOCATION ACCESS_FINE_LOCATION} permission
     * in order to get results.
     *
     * @param callback
     * 		Callback used to deliver scan results.
     * @throws IllegalArgumentException
     * 		If {@code callback} is null.
     */
    @android.annotation.RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void startScan(final android.bluetooth.le.ScanCallback callback) {
        startScan(null, new android.bluetooth.le.ScanSettings.Builder().build(), callback);
    }

    /**
     * Start Bluetooth LE scan. The scan results will be delivered through {@code callback}.
     * <p>
     * Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN} permission.
     * An app must hold
     * {@link android.Manifest.permission#ACCESS_COARSE_LOCATION ACCESS_COARSE_LOCATION} or
     * {@link android.Manifest.permission#ACCESS_FINE_LOCATION ACCESS_FINE_LOCATION} permission
     * in order to get results.
     *
     * @param filters
     * 		{@link ScanFilter}s for finding exact BLE devices.
     * @param settings
     * 		Settings for the scan.
     * @param callback
     * 		Callback used to deliver scan results.
     * @throws IllegalArgumentException
     * 		If {@code settings} or {@code callback} is null.
     */
    @android.annotation.RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void startScan(java.util.List<android.bluetooth.le.ScanFilter> filters, android.bluetooth.le.ScanSettings settings, final android.bluetooth.le.ScanCallback callback) {
        startScan(filters, settings, null, callback, null);
    }

    /**
     * Start Bluetooth LE scan. Same as {@link #startScan(ScanCallback)} but allows the caller to
     * specify on behalf of which application(s) the work is being done.
     *
     * @param workSource
     * 		{@link WorkSource} identifying the application(s) for which to blame for
     * 		the scan.
     * @param callback
     * 		Callback used to deliver scan results.
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.RequiresPermission(allOf = { Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.UPDATE_DEVICE_STATS })
    public void startScanFromSource(final android.os.WorkSource workSource, final android.bluetooth.le.ScanCallback callback) {
        startScanFromSource(null, new android.bluetooth.le.ScanSettings.Builder().build(), workSource, callback);
    }

    /**
     * Start Bluetooth LE scan. Same as {@link #startScan(List, ScanSettings, ScanCallback)} but
     * allows the caller to specify on behalf of which application(s) the work is being done.
     *
     * @param filters
     * 		{@link ScanFilter}s for finding exact BLE devices.
     * @param settings
     * 		Settings for the scan.
     * @param workSource
     * 		{@link WorkSource} identifying the application(s) for which to blame for
     * 		the scan.
     * @param callback
     * 		Callback used to deliver scan results.
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.RequiresPermission(allOf = { Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.UPDATE_DEVICE_STATS })
    public void startScanFromSource(java.util.List<android.bluetooth.le.ScanFilter> filters, android.bluetooth.le.ScanSettings settings, final android.os.WorkSource workSource, final android.bluetooth.le.ScanCallback callback) {
        startScan(filters, settings, workSource, callback, null);
    }

    private void startScan(java.util.List<android.bluetooth.le.ScanFilter> filters, android.bluetooth.le.ScanSettings settings, final android.os.WorkSource workSource, final android.bluetooth.le.ScanCallback callback, java.util.List<java.util.List<android.bluetooth.le.ResultStorageDescriptor>> resultStorages) {
        android.bluetooth.le.BluetoothLeUtils.checkAdapterStateOn(mBluetoothAdapter);
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback is null");
        }
        if (settings == null) {
            throw new java.lang.IllegalArgumentException("settings is null");
        }
        synchronized(mLeScanClients) {
            if (mLeScanClients.containsKey(callback)) {
                postCallbackError(callback, android.bluetooth.le.ScanCallback.SCAN_FAILED_ALREADY_STARTED);
                return;
            }
            android.bluetooth.IBluetoothGatt gatt;
            try {
                gatt = mBluetoothManager.getBluetoothGatt();
            } catch (android.os.RemoteException e) {
                gatt = null;
            }
            if (gatt == null) {
                postCallbackError(callback, android.bluetooth.le.ScanCallback.SCAN_FAILED_INTERNAL_ERROR);
                return;
            }
            if (!isSettingsConfigAllowedForScan(settings)) {
                postCallbackError(callback, android.bluetooth.le.ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED);
                return;
            }
            if (!isHardwareResourcesAvailableForScan(settings)) {
                postCallbackError(callback, android.bluetooth.le.ScanCallback.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES);
                return;
            }
            if (!isSettingsAndFilterComboAllowed(settings, filters)) {
                postCallbackError(callback, android.bluetooth.le.ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED);
                return;
            }
            android.bluetooth.le.BluetoothLeScanner.BleScanCallbackWrapper wrapper = new android.bluetooth.le.BluetoothLeScanner.BleScanCallbackWrapper(gatt, filters, settings, workSource, callback, resultStorages);
            wrapper.startRegisteration();
        }
    }

    /**
     * Stops an ongoing Bluetooth LE scan.
     * <p>
     * Requires {@link android.Manifest.permission#BLUETOOTH_ADMIN} permission.
     *
     * @param callback
     * 		
     */
    @android.annotation.RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void stopScan(android.bluetooth.le.ScanCallback callback) {
        android.bluetooth.le.BluetoothLeUtils.checkAdapterStateOn(mBluetoothAdapter);
        synchronized(mLeScanClients) {
            android.bluetooth.le.BluetoothLeScanner.BleScanCallbackWrapper wrapper = mLeScanClients.remove(callback);
            if (wrapper == null) {
                if (android.bluetooth.le.BluetoothLeScanner.DBG)
                    android.util.Log.d(android.bluetooth.le.BluetoothLeScanner.TAG, "could not find callback wrapper");

                return;
            }
            wrapper.stopLeScan();
        }
    }

    /**
     * Flush pending batch scan results stored in Bluetooth controller. This will return Bluetooth
     * LE scan results batched on bluetooth controller. Returns immediately, batch scan results data
     * will be delivered through the {@code callback}.
     *
     * @param callback
     * 		Callback of the Bluetooth LE Scan, it has to be the same instance as the one
     * 		used to start scan.
     */
    public void flushPendingScanResults(android.bluetooth.le.ScanCallback callback) {
        android.bluetooth.le.BluetoothLeUtils.checkAdapterStateOn(mBluetoothAdapter);
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback cannot be null!");
        }
        synchronized(mLeScanClients) {
            android.bluetooth.le.BluetoothLeScanner.BleScanCallbackWrapper wrapper = mLeScanClients.get(callback);
            if (wrapper == null) {
                return;
            }
            wrapper.flushPendingBatchResults();
        }
    }

    /**
     * Start truncated scan.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public void startTruncatedScan(java.util.List<android.bluetooth.le.TruncatedFilter> truncatedFilters, android.bluetooth.le.ScanSettings settings, final android.bluetooth.le.ScanCallback callback) {
        int filterSize = truncatedFilters.size();
        java.util.List<android.bluetooth.le.ScanFilter> scanFilters = new java.util.ArrayList<android.bluetooth.le.ScanFilter>(filterSize);
        java.util.List<java.util.List<android.bluetooth.le.ResultStorageDescriptor>> scanStorages = new java.util.ArrayList<java.util.List<android.bluetooth.le.ResultStorageDescriptor>>(filterSize);
        for (android.bluetooth.le.TruncatedFilter filter : truncatedFilters) {
            scanFilters.add(filter.getFilter());
            scanStorages.add(filter.getStorageDescriptors());
        }
        startScan(scanFilters, settings, null, callback, scanStorages);
    }

    /**
     * Cleans up scan clients. Should be called when bluetooth is down.
     *
     * @unknown 
     */
    public void cleanup() {
        mLeScanClients.clear();
    }

    /**
     * Bluetooth GATT interface callbacks
     */
    private class BleScanCallbackWrapper extends android.bluetooth.BluetoothGattCallbackWrapper {
        private static final int REGISTRATION_CALLBACK_TIMEOUT_MILLIS = 2000;

        private final android.bluetooth.le.ScanCallback mScanCallback;

        private final java.util.List<android.bluetooth.le.ScanFilter> mFilters;

        private final android.os.WorkSource mWorkSource;

        private android.bluetooth.le.ScanSettings mSettings;

        private android.bluetooth.IBluetoothGatt mBluetoothGatt;

        private java.util.List<java.util.List<android.bluetooth.le.ResultStorageDescriptor>> mResultStorages;

        // mLeHandle 0: not registered
        // -1: scan stopped or registration failed
        // > 0: registered and scan started
        private int mClientIf;

        public BleScanCallbackWrapper(android.bluetooth.IBluetoothGatt bluetoothGatt, java.util.List<android.bluetooth.le.ScanFilter> filters, android.bluetooth.le.ScanSettings settings, android.os.WorkSource workSource, android.bluetooth.le.ScanCallback scanCallback, java.util.List<java.util.List<android.bluetooth.le.ResultStorageDescriptor>> resultStorages) {
            mBluetoothGatt = bluetoothGatt;
            mFilters = filters;
            mSettings = settings;
            mWorkSource = workSource;
            mScanCallback = scanCallback;
            mClientIf = 0;
            mResultStorages = resultStorages;
        }

        public void startRegisteration() {
            synchronized(this) {
                // Scan stopped.
                if (mClientIf == (-1))
                    return;

                try {
                    java.util.UUID uuid = java.util.UUID.randomUUID();
                    mBluetoothGatt.registerClient(new android.os.ParcelUuid(uuid), this);
                    wait(android.bluetooth.le.BluetoothLeScanner.BleScanCallbackWrapper.REGISTRATION_CALLBACK_TIMEOUT_MILLIS);
                } catch (java.lang.InterruptedException | android.os.RemoteException e) {
                    android.util.Log.e(android.bluetooth.le.BluetoothLeScanner.TAG, "application registeration exception", e);
                    postCallbackError(mScanCallback, android.bluetooth.le.ScanCallback.SCAN_FAILED_INTERNAL_ERROR);
                }
                if (mClientIf > 0) {
                    mLeScanClients.put(mScanCallback, this);
                } else {
                    // Registration timed out or got exception, reset clientIf to -1 so no
                    // subsequent operations can proceed.
                    if (mClientIf == 0)
                        mClientIf = -1;

                    postCallbackError(mScanCallback, android.bluetooth.le.ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED);
                }
            }
        }

        public void stopLeScan() {
            synchronized(this) {
                if (mClientIf <= 0) {
                    android.util.Log.e(android.bluetooth.le.BluetoothLeScanner.TAG, "Error state, mLeHandle: " + mClientIf);
                    return;
                }
                try {
                    mBluetoothGatt.stopScan(mClientIf, false);
                    mBluetoothGatt.unregisterClient(mClientIf);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.bluetooth.le.BluetoothLeScanner.TAG, "Failed to stop scan and unregister", e);
                }
                mClientIf = -1;
            }
        }

        void flushPendingBatchResults() {
            synchronized(this) {
                if (mClientIf <= 0) {
                    android.util.Log.e(android.bluetooth.le.BluetoothLeScanner.TAG, "Error state, mLeHandle: " + mClientIf);
                    return;
                }
                try {
                    mBluetoothGatt.flushPendingBatchResults(mClientIf, false);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(android.bluetooth.le.BluetoothLeScanner.TAG, "Failed to get pending scan results", e);
                }
            }
        }

        /**
         * Application interface registered - app is ready to go
         */
        @java.lang.Override
        public void onClientRegistered(int status, int clientIf) {
            android.util.Log.d(android.bluetooth.le.BluetoothLeScanner.TAG, (((("onClientRegistered() - status=" + status) + " clientIf=") + clientIf) + " mClientIf=") + mClientIf);
            synchronized(this) {
                if (status == android.bluetooth.BluetoothGatt.GATT_SUCCESS) {
                    try {
                        if (mClientIf == (-1)) {
                            // Registration succeeds after timeout, unregister client.
                            mBluetoothGatt.unregisterClient(clientIf);
                        } else {
                            mClientIf = clientIf;
                            mBluetoothGatt.startScan(mClientIf, false, mSettings, mFilters, mWorkSource, mResultStorages, android.app.ActivityThread.currentOpPackageName());
                        }
                    } catch (android.os.RemoteException e) {
                        android.util.Log.e(android.bluetooth.le.BluetoothLeScanner.TAG, "fail to start le scan: " + e);
                        mClientIf = -1;
                    }
                } else {
                    // registration failed
                    mClientIf = -1;
                }
                notifyAll();
            }
        }

        /**
         * Callback reporting an LE scan result.
         *
         * @unknown 
         */
        @java.lang.Override
        public void onScanResult(final android.bluetooth.le.ScanResult scanResult) {
            if (android.bluetooth.le.BluetoothLeScanner.VDBG)
                android.util.Log.d(android.bluetooth.le.BluetoothLeScanner.TAG, "onScanResult() - " + scanResult.toString());

            // Check null in case the scan has been stopped
            synchronized(this) {
                if (mClientIf <= 0)
                    return;

            }
            android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
            handler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mScanCallback.onScanResult(android.bluetooth.le.ScanSettings.CALLBACK_TYPE_ALL_MATCHES, scanResult);
                }
            });
        }

        @java.lang.Override
        public void onBatchScanResults(final java.util.List<android.bluetooth.le.ScanResult> results) {
            android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
            handler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    mScanCallback.onBatchScanResults(results);
                }
            });
        }

        @java.lang.Override
        public void onFoundOrLost(final boolean onFound, final android.bluetooth.le.ScanResult scanResult) {
            if (android.bluetooth.le.BluetoothLeScanner.VDBG) {
                android.util.Log.d(android.bluetooth.le.BluetoothLeScanner.TAG, (("onFoundOrLost() - onFound = " + onFound) + " ") + scanResult.toString());
            }
            // Check null in case the scan has been stopped
            synchronized(this) {
                if (mClientIf <= 0)
                    return;

            }
            android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
            handler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    if (onFound) {
                        mScanCallback.onScanResult(android.bluetooth.le.ScanSettings.CALLBACK_TYPE_FIRST_MATCH, scanResult);
                    } else {
                        mScanCallback.onScanResult(android.bluetooth.le.ScanSettings.CALLBACK_TYPE_MATCH_LOST, scanResult);
                    }
                }
            });
        }

        @java.lang.Override
        public void onScanManagerErrorCallback(final int errorCode) {
            if (android.bluetooth.le.BluetoothLeScanner.VDBG) {
                android.util.Log.d(android.bluetooth.le.BluetoothLeScanner.TAG, "onScanManagerErrorCallback() - errorCode = " + errorCode);
            }
            synchronized(this) {
                if (mClientIf <= 0)
                    return;

            }
            postCallbackError(mScanCallback, errorCode);
        }
    }

    private void postCallbackError(final android.bluetooth.le.ScanCallback callback, final int errorCode) {
        mHandler.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                callback.onScanFailed(errorCode);
            }
        });
    }

    private boolean isSettingsConfigAllowedForScan(android.bluetooth.le.ScanSettings settings) {
        if (mBluetoothAdapter.isOffloadedFilteringSupported()) {
            return true;
        }
        final int callbackType = settings.getCallbackType();
        // Only support regular scan if no offloaded filter support.
        if ((callbackType == android.bluetooth.le.ScanSettings.CALLBACK_TYPE_ALL_MATCHES) && (settings.getReportDelayMillis() == 0)) {
            return true;
        }
        return false;
    }

    private boolean isSettingsAndFilterComboAllowed(android.bluetooth.le.ScanSettings settings, java.util.List<android.bluetooth.le.ScanFilter> filterList) {
        final int callbackType = settings.getCallbackType();
        // If onlost/onfound is requested, a non-empty filter is expected
        if ((callbackType & (android.bluetooth.le.ScanSettings.CALLBACK_TYPE_FIRST_MATCH | android.bluetooth.le.ScanSettings.CALLBACK_TYPE_MATCH_LOST)) != 0) {
            if (filterList == null) {
                return false;
            }
            for (android.bluetooth.le.ScanFilter filter : filterList) {
                if (filter.isAllFieldsEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isHardwareResourcesAvailableForScan(android.bluetooth.le.ScanSettings settings) {
        final int callbackType = settings.getCallbackType();
        if (((callbackType & android.bluetooth.le.ScanSettings.CALLBACK_TYPE_FIRST_MATCH) != 0) || ((callbackType & android.bluetooth.le.ScanSettings.CALLBACK_TYPE_MATCH_LOST) != 0)) {
            // For onlost/onfound, we required hw support be available
            return mBluetoothAdapter.isOffloadedFilteringSupported() && mBluetoothAdapter.isHardwareTrackingFiltersAvailable();
        }
        return true;
    }
}

