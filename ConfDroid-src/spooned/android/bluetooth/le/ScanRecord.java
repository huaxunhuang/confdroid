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
package android.bluetooth.le;


/**
 * Represents a scan record from Bluetooth LE scan.
 */
public final class ScanRecord {
    private static final java.lang.String TAG = "ScanRecord";

    // The following data type values are assigned by Bluetooth SIG.
    // For more details refer to Bluetooth 4.1 specification, Volume 3, Part C, Section 18.
    private static final int DATA_TYPE_FLAGS = 0x1;

    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL = 0x2;

    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE = 0x3;

    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL = 0x4;

    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE = 0x5;

    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL = 0x6;

    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE = 0x7;

    private static final int DATA_TYPE_LOCAL_NAME_SHORT = 0x8;

    private static final int DATA_TYPE_LOCAL_NAME_COMPLETE = 0x9;

    private static final int DATA_TYPE_TX_POWER_LEVEL = 0xa;

    private static final int DATA_TYPE_SERVICE_DATA = 0x16;

    private static final int DATA_TYPE_MANUFACTURER_SPECIFIC_DATA = 0xff;

    // Flags of the advertising data.
    private final int mAdvertiseFlags;

    @android.annotation.Nullable
    private final java.util.List<android.os.ParcelUuid> mServiceUuids;

    private final android.util.SparseArray<byte[]> mManufacturerSpecificData;

    private final java.util.Map<android.os.ParcelUuid, byte[]> mServiceData;

    // Transmission power level(in dB).
    private final int mTxPowerLevel;

    // Local name of the Bluetooth LE device.
    private final java.lang.String mDeviceName;

    // Raw bytes of scan record.
    private final byte[] mBytes;

    /**
     * Returns the advertising flags indicating the discoverable mode and capability of the device.
     * Returns -1 if the flag field is not set.
     */
    public int getAdvertiseFlags() {
        return mAdvertiseFlags;
    }

    /**
     * Returns a list of service UUIDs within the advertisement that are used to identify the
     * bluetooth GATT services.
     */
    public java.util.List<android.os.ParcelUuid> getServiceUuids() {
        return mServiceUuids;
    }

    /**
     * Returns a sparse array of manufacturer identifier and its corresponding manufacturer specific
     * data.
     */
    public android.util.SparseArray<byte[]> getManufacturerSpecificData() {
        return mManufacturerSpecificData;
    }

    /**
     * Returns the manufacturer specific data associated with the manufacturer id. Returns
     * {@code null} if the {@code manufacturerId} is not found.
     */
    @android.annotation.Nullable
    public byte[] getManufacturerSpecificData(int manufacturerId) {
        return mManufacturerSpecificData.get(manufacturerId);
    }

    /**
     * Returns a map of service UUID and its corresponding service data.
     */
    public java.util.Map<android.os.ParcelUuid, byte[]> getServiceData() {
        return mServiceData;
    }

    /**
     * Returns the service data byte array associated with the {@code serviceUuid}. Returns
     * {@code null} if the {@code serviceDataUuid} is not found.
     */
    @android.annotation.Nullable
    public byte[] getServiceData(android.os.ParcelUuid serviceDataUuid) {
        if (serviceDataUuid == null) {
            return null;
        }
        return mServiceData.get(serviceDataUuid);
    }

    /**
     * Returns the transmission power level of the packet in dBm. Returns {@link Integer#MIN_VALUE}
     * if the field is not set. This value can be used to calculate the path loss of a received
     * packet using the following equation:
     * <p>
     * <code>pathloss = txPowerLevel - rssi</code>
     */
    public int getTxPowerLevel() {
        return mTxPowerLevel;
    }

    /**
     * Returns the local name of the BLE device. The is a UTF-8 encoded string.
     */
    @android.annotation.Nullable
    public java.lang.String getDeviceName() {
        return mDeviceName;
    }

    /**
     * Returns raw bytes of scan record.
     */
    public byte[] getBytes() {
        return mBytes;
    }

    private ScanRecord(java.util.List<android.os.ParcelUuid> serviceUuids, android.util.SparseArray<byte[]> manufacturerData, java.util.Map<android.os.ParcelUuid, byte[]> serviceData, int advertiseFlags, int txPowerLevel, java.lang.String localName, byte[] bytes) {
        mServiceUuids = serviceUuids;
        mManufacturerSpecificData = manufacturerData;
        mServiceData = serviceData;
        mDeviceName = localName;
        mAdvertiseFlags = advertiseFlags;
        mTxPowerLevel = txPowerLevel;
        mBytes = bytes;
    }

    /**
     * Parse scan record bytes to {@link ScanRecord}.
     * <p>
     * The format is defined in Bluetooth 4.1 specification, Volume 3, Part C, Section 11 and 18.
     * <p>
     * All numerical multi-byte entities and values shall use little-endian <strong>byte</strong>
     * order.
     *
     * @param scanRecord
     * 		The scan record of Bluetooth LE advertisement and/or scan response.
     * @unknown 
     */
    public static android.bluetooth.le.ScanRecord parseFromBytes(byte[] scanRecord) {
        if (scanRecord == null) {
            return null;
        }
        int currentPos = 0;
        int advertiseFlag = -1;
        java.util.List<android.os.ParcelUuid> serviceUuids = new java.util.ArrayList<android.os.ParcelUuid>();
        java.lang.String localName = null;
        int txPowerLevel = java.lang.Integer.MIN_VALUE;
        android.util.SparseArray<byte[]> manufacturerData = new android.util.SparseArray<byte[]>();
        java.util.Map<android.os.ParcelUuid, byte[]> serviceData = new android.util.ArrayMap<android.os.ParcelUuid, byte[]>();
        try {
            while (currentPos < scanRecord.length) {
                // length is unsigned int.
                int length = scanRecord[currentPos++] & 0xff;
                if (length == 0) {
                    break;
                }
                // Note the length includes the length of the field type itself.
                int dataLength = length - 1;
                // fieldType is unsigned int.
                int fieldType = scanRecord[currentPos++] & 0xff;
                switch (fieldType) {
                    case android.bluetooth.le.ScanRecord.DATA_TYPE_FLAGS :
                        advertiseFlag = scanRecord[currentPos] & 0xff;
                        break;
                    case android.bluetooth.le.ScanRecord.DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL :
                    case android.bluetooth.le.ScanRecord.DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE :
                        android.bluetooth.le.ScanRecord.parseServiceUuid(scanRecord, currentPos, dataLength, android.bluetooth.BluetoothUuid.UUID_BYTES_16_BIT, serviceUuids);
                        break;
                    case android.bluetooth.le.ScanRecord.DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL :
                    case android.bluetooth.le.ScanRecord.DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE :
                        android.bluetooth.le.ScanRecord.parseServiceUuid(scanRecord, currentPos, dataLength, android.bluetooth.BluetoothUuid.UUID_BYTES_32_BIT, serviceUuids);
                        break;
                    case android.bluetooth.le.ScanRecord.DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL :
                    case android.bluetooth.le.ScanRecord.DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE :
                        android.bluetooth.le.ScanRecord.parseServiceUuid(scanRecord, currentPos, dataLength, android.bluetooth.BluetoothUuid.UUID_BYTES_128_BIT, serviceUuids);
                        break;
                    case android.bluetooth.le.ScanRecord.DATA_TYPE_LOCAL_NAME_SHORT :
                    case android.bluetooth.le.ScanRecord.DATA_TYPE_LOCAL_NAME_COMPLETE :
                        localName = new java.lang.String(android.bluetooth.le.ScanRecord.extractBytes(scanRecord, currentPos, dataLength));
                        break;
                    case android.bluetooth.le.ScanRecord.DATA_TYPE_TX_POWER_LEVEL :
                        txPowerLevel = scanRecord[currentPos];
                        break;
                    case android.bluetooth.le.ScanRecord.DATA_TYPE_SERVICE_DATA :
                        // The first two bytes of the service data are service data UUID in little
                        // endian. The rest bytes are service data.
                        int serviceUuidLength = android.bluetooth.BluetoothUuid.UUID_BYTES_16_BIT;
                        byte[] serviceDataUuidBytes = android.bluetooth.le.ScanRecord.extractBytes(scanRecord, currentPos, serviceUuidLength);
                        android.os.ParcelUuid serviceDataUuid = android.bluetooth.BluetoothUuid.parseUuidFrom(serviceDataUuidBytes);
                        byte[] serviceDataArray = android.bluetooth.le.ScanRecord.extractBytes(scanRecord, currentPos + serviceUuidLength, dataLength - serviceUuidLength);
                        serviceData.put(serviceDataUuid, serviceDataArray);
                        break;
                    case android.bluetooth.le.ScanRecord.DATA_TYPE_MANUFACTURER_SPECIFIC_DATA :
                        // The first two bytes of the manufacturer specific data are
                        // manufacturer ids in little endian.
                        int manufacturerId = ((scanRecord[currentPos + 1] & 0xff) << 8) + (scanRecord[currentPos] & 0xff);
                        byte[] manufacturerDataBytes = android.bluetooth.le.ScanRecord.extractBytes(scanRecord, currentPos + 2, dataLength - 2);
                        manufacturerData.put(manufacturerId, manufacturerDataBytes);
                        break;
                    default :
                        // Just ignore, we don't handle such data type.
                        break;
                }
                currentPos += dataLength;
            } 
            if (serviceUuids.isEmpty()) {
                serviceUuids = null;
            }
            return new android.bluetooth.le.ScanRecord(serviceUuids, manufacturerData, serviceData, advertiseFlag, txPowerLevel, localName, scanRecord);
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.bluetooth.le.ScanRecord.TAG, "unable to parse scan record: " + java.util.Arrays.toString(scanRecord));
            // As the record is invalid, ignore all the parsed results for this packet
            // and return an empty record with raw scanRecord bytes in results
            return new android.bluetooth.le.ScanRecord(null, null, null, -1, java.lang.Integer.MIN_VALUE, null, scanRecord);
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((("ScanRecord [mAdvertiseFlags=" + mAdvertiseFlags) + ", mServiceUuids=") + mServiceUuids) + ", mManufacturerSpecificData=") + android.bluetooth.le.BluetoothLeUtils.toString(mManufacturerSpecificData)) + ", mServiceData=") + android.bluetooth.le.BluetoothLeUtils.toString(mServiceData)) + ", mTxPowerLevel=") + mTxPowerLevel) + ", mDeviceName=") + mDeviceName) + "]";
    }

    // Parse service UUIDs.
    private static int parseServiceUuid(byte[] scanRecord, int currentPos, int dataLength, int uuidLength, java.util.List<android.os.ParcelUuid> serviceUuids) {
        while (dataLength > 0) {
            byte[] uuidBytes = android.bluetooth.le.ScanRecord.extractBytes(scanRecord, currentPos, uuidLength);
            serviceUuids.add(android.bluetooth.BluetoothUuid.parseUuidFrom(uuidBytes));
            dataLength -= uuidLength;
            currentPos += uuidLength;
        } 
        return currentPos;
    }

    // Helper method to extract bytes from byte array.
    private static byte[] extractBytes(byte[] scanRecord, int start, int length) {
        byte[] bytes = new byte[length];
        java.lang.System.arraycopy(scanRecord, start, bytes, 0, length);
        return bytes;
    }
}

