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
 * Criteria for filtering result from Bluetooth LE scans. A {@link ScanFilter} allows clients to
 * restrict scan results to only those that are of interest to them.
 * <p>
 * Current filtering on the following fields are supported:
 * <li>Service UUIDs which identify the bluetooth gatt services running on the device.
 * <li>Name of remote Bluetooth LE device.
 * <li>Mac address of the remote device.
 * <li>Service data which is the data associated with a service.
 * <li>Manufacturer specific data which is the data associated with a particular manufacturer.
 *
 * @see ScanResult
 * @see BluetoothLeScanner
 */
public final class ScanFilter implements android.os.Parcelable {
    @android.annotation.Nullable
    private final java.lang.String mDeviceName;

    @android.annotation.Nullable
    private final java.lang.String mDeviceAddress;

    @android.annotation.Nullable
    private final android.os.ParcelUuid mServiceUuid;

    @android.annotation.Nullable
    private final android.os.ParcelUuid mServiceUuidMask;

    @android.annotation.Nullable
    private final android.os.ParcelUuid mServiceDataUuid;

    @android.annotation.Nullable
    private final byte[] mServiceData;

    @android.annotation.Nullable
    private final byte[] mServiceDataMask;

    private final int mManufacturerId;

    @android.annotation.Nullable
    private final byte[] mManufacturerData;

    @android.annotation.Nullable
    private final byte[] mManufacturerDataMask;

    private static final android.bluetooth.le.ScanFilter EMPTY = new android.bluetooth.le.ScanFilter.Builder().build();

    private ScanFilter(java.lang.String name, java.lang.String deviceAddress, android.os.ParcelUuid uuid, android.os.ParcelUuid uuidMask, android.os.ParcelUuid serviceDataUuid, byte[] serviceData, byte[] serviceDataMask, int manufacturerId, byte[] manufacturerData, byte[] manufacturerDataMask) {
        mDeviceName = name;
        mServiceUuid = uuid;
        mServiceUuidMask = uuidMask;
        mDeviceAddress = deviceAddress;
        mServiceDataUuid = serviceDataUuid;
        mServiceData = serviceData;
        mServiceDataMask = serviceDataMask;
        mManufacturerId = manufacturerId;
        mManufacturerData = manufacturerData;
        mManufacturerDataMask = manufacturerDataMask;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mDeviceName == null ? 0 : 1);
        if (mDeviceName != null) {
            dest.writeString(mDeviceName);
        }
        dest.writeInt(mDeviceAddress == null ? 0 : 1);
        if (mDeviceAddress != null) {
            dest.writeString(mDeviceAddress);
        }
        dest.writeInt(mServiceUuid == null ? 0 : 1);
        if (mServiceUuid != null) {
            dest.writeParcelable(mServiceUuid, flags);
            dest.writeInt(mServiceUuidMask == null ? 0 : 1);
            if (mServiceUuidMask != null) {
                dest.writeParcelable(mServiceUuidMask, flags);
            }
        }
        dest.writeInt(mServiceDataUuid == null ? 0 : 1);
        if (mServiceDataUuid != null) {
            dest.writeParcelable(mServiceDataUuid, flags);
            dest.writeInt(mServiceData == null ? 0 : 1);
            if (mServiceData != null) {
                dest.writeInt(mServiceData.length);
                dest.writeByteArray(mServiceData);
                dest.writeInt(mServiceDataMask == null ? 0 : 1);
                if (mServiceDataMask != null) {
                    dest.writeInt(mServiceDataMask.length);
                    dest.writeByteArray(mServiceDataMask);
                }
            }
        }
        dest.writeInt(mManufacturerId);
        dest.writeInt(mManufacturerData == null ? 0 : 1);
        if (mManufacturerData != null) {
            dest.writeInt(mManufacturerData.length);
            dest.writeByteArray(mManufacturerData);
            dest.writeInt(mManufacturerDataMask == null ? 0 : 1);
            if (mManufacturerDataMask != null) {
                dest.writeInt(mManufacturerDataMask.length);
                dest.writeByteArray(mManufacturerDataMask);
            }
        }
    }

    /**
     * A {@link android.os.Parcelable.Creator} to create {@link ScanFilter} from parcel.
     */
    public static final android.os.Parcelable.Creator<android.bluetooth.le.ScanFilter> CREATOR = new android.os.Parcelable.Creator<android.bluetooth.le.ScanFilter>() {
        @java.lang.Override
        public android.bluetooth.le.ScanFilter[] newArray(int size) {
            return new android.bluetooth.le.ScanFilter[size];
        }

        @java.lang.Override
        public android.bluetooth.le.ScanFilter createFromParcel(android.os.Parcel in) {
            android.bluetooth.le.ScanFilter.Builder builder = new android.bluetooth.le.ScanFilter.Builder();
            if (in.readInt() == 1) {
                builder.setDeviceName(in.readString());
            }
            if (in.readInt() == 1) {
                builder.setDeviceAddress(in.readString());
            }
            if (in.readInt() == 1) {
                android.os.ParcelUuid uuid = in.readParcelable(android.os.ParcelUuid.class.getClassLoader());
                builder.setServiceUuid(uuid);
                if (in.readInt() == 1) {
                    android.os.ParcelUuid uuidMask = in.readParcelable(android.os.ParcelUuid.class.getClassLoader());
                    builder.setServiceUuid(uuid, uuidMask);
                }
            }
            if (in.readInt() == 1) {
                android.os.ParcelUuid servcieDataUuid = in.readParcelable(android.os.ParcelUuid.class.getClassLoader());
                if (in.readInt() == 1) {
                    int serviceDataLength = in.readInt();
                    byte[] serviceData = new byte[serviceDataLength];
                    in.readByteArray(serviceData);
                    if (in.readInt() == 0) {
                        builder.setServiceData(servcieDataUuid, serviceData);
                    } else {
                        int serviceDataMaskLength = in.readInt();
                        byte[] serviceDataMask = new byte[serviceDataMaskLength];
                        in.readByteArray(serviceDataMask);
                        builder.setServiceData(servcieDataUuid, serviceData, serviceDataMask);
                    }
                }
            }
            int manufacturerId = in.readInt();
            if (in.readInt() == 1) {
                int manufacturerDataLength = in.readInt();
                byte[] manufacturerData = new byte[manufacturerDataLength];
                in.readByteArray(manufacturerData);
                if (in.readInt() == 0) {
                    builder.setManufacturerData(manufacturerId, manufacturerData);
                } else {
                    int manufacturerDataMaskLength = in.readInt();
                    byte[] manufacturerDataMask = new byte[manufacturerDataMaskLength];
                    in.readByteArray(manufacturerDataMask);
                    builder.setManufacturerData(manufacturerId, manufacturerData, manufacturerDataMask);
                }
            }
            return builder.build();
        }
    };

    /**
     * Returns the filter set the device name field of Bluetooth advertisement data.
     */
    @android.annotation.Nullable
    public java.lang.String getDeviceName() {
        return mDeviceName;
    }

    /**
     * Returns the filter set on the service uuid.
     */
    @android.annotation.Nullable
    public android.os.ParcelUuid getServiceUuid() {
        return mServiceUuid;
    }

    @android.annotation.Nullable
    public android.os.ParcelUuid getServiceUuidMask() {
        return mServiceUuidMask;
    }

    @android.annotation.Nullable
    public java.lang.String getDeviceAddress() {
        return mDeviceAddress;
    }

    @android.annotation.Nullable
    public byte[] getServiceData() {
        return mServiceData;
    }

    @android.annotation.Nullable
    public byte[] getServiceDataMask() {
        return mServiceDataMask;
    }

    @android.annotation.Nullable
    public android.os.ParcelUuid getServiceDataUuid() {
        return mServiceDataUuid;
    }

    /**
     * Returns the manufacturer id. -1 if the manufacturer filter is not set.
     */
    public int getManufacturerId() {
        return mManufacturerId;
    }

    @android.annotation.Nullable
    public byte[] getManufacturerData() {
        return mManufacturerData;
    }

    @android.annotation.Nullable
    public byte[] getManufacturerDataMask() {
        return mManufacturerDataMask;
    }

    /**
     * Check if the scan filter matches a {@code scanResult}. A scan result is considered as a match
     * if it matches all the field filters.
     */
    public boolean matches(android.bluetooth.le.ScanResult scanResult) {
        if (scanResult == null) {
            return false;
        }
        android.bluetooth.BluetoothDevice device = scanResult.getDevice();
        // Device match.
        if ((mDeviceAddress != null) && ((device == null) || (!mDeviceAddress.equals(device.getAddress())))) {
            return false;
        }
        android.bluetooth.le.ScanRecord scanRecord = scanResult.getScanRecord();
        // Scan record is null but there exist filters on it.
        if ((scanRecord == null) && ((((mDeviceName != null) || (mServiceUuid != null)) || (mManufacturerData != null)) || (mServiceData != null))) {
            return false;
        }
        // Local name match.
        if ((mDeviceName != null) && (!mDeviceName.equals(scanRecord.getDeviceName()))) {
            return false;
        }
        // UUID match.
        if ((mServiceUuid != null) && (!matchesServiceUuids(mServiceUuid, mServiceUuidMask, scanRecord.getServiceUuids()))) {
            return false;
        }
        // Service data match
        if (mServiceDataUuid != null) {
            if (!matchesPartialData(mServiceData, mServiceDataMask, scanRecord.getServiceData(mServiceDataUuid))) {
                return false;
            }
        }
        // Manufacturer data match.
        if (mManufacturerId >= 0) {
            if (!matchesPartialData(mManufacturerData, mManufacturerDataMask, scanRecord.getManufacturerSpecificData(mManufacturerId))) {
                return false;
            }
        }
        // All filters match.
        return true;
    }

    // Check if the uuid pattern is contained in a list of parcel uuids.
    private boolean matchesServiceUuids(android.os.ParcelUuid uuid, android.os.ParcelUuid parcelUuidMask, java.util.List<android.os.ParcelUuid> uuids) {
        if (uuid == null) {
            return true;
        }
        if (uuids == null) {
            return false;
        }
        for (android.os.ParcelUuid parcelUuid : uuids) {
            java.util.UUID uuidMask = (parcelUuidMask == null) ? null : parcelUuidMask.getUuid();
            if (matchesServiceUuid(uuid.getUuid(), uuidMask, parcelUuid.getUuid())) {
                return true;
            }
        }
        return false;
    }

    // Check if the uuid pattern matches the particular service uuid.
    private boolean matchesServiceUuid(java.util.UUID uuid, java.util.UUID mask, java.util.UUID data) {
        if (mask == null) {
            return uuid.equals(data);
        }
        if ((uuid.getLeastSignificantBits() & mask.getLeastSignificantBits()) != (data.getLeastSignificantBits() & mask.getLeastSignificantBits())) {
            return false;
        }
        return (uuid.getMostSignificantBits() & mask.getMostSignificantBits()) == (data.getMostSignificantBits() & mask.getMostSignificantBits());
    }

    // Check whether the data pattern matches the parsed data.
    private boolean matchesPartialData(byte[] data, byte[] dataMask, byte[] parsedData) {
        if ((parsedData == null) || (parsedData.length < data.length)) {
            return false;
        }
        if (dataMask == null) {
            for (int i = 0; i < data.length; ++i) {
                if (parsedData[i] != data[i]) {
                    return false;
                }
            }
            return true;
        }
        for (int i = 0; i < data.length; ++i) {
            if ((dataMask[i] & parsedData[i]) != (dataMask[i] & data[i])) {
                return false;
            }
        }
        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((((((((((("BluetoothLeScanFilter [mDeviceName=" + mDeviceName) + ", mDeviceAddress=") + mDeviceAddress) + ", mUuid=") + mServiceUuid) + ", mUuidMask=") + mServiceUuidMask) + ", mServiceDataUuid=") + java.util.Objects.toString(mServiceDataUuid)) + ", mServiceData=") + java.util.Arrays.toString(mServiceData)) + ", mServiceDataMask=") + java.util.Arrays.toString(mServiceDataMask)) + ", mManufacturerId=") + mManufacturerId) + ", mManufacturerData=") + java.util.Arrays.toString(mManufacturerData)) + ", mManufacturerDataMask=") + java.util.Arrays.toString(mManufacturerDataMask)) + "]";
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mDeviceName, mDeviceAddress, mManufacturerId, java.util.Arrays.hashCode(mManufacturerData), java.util.Arrays.hashCode(mManufacturerDataMask), mServiceDataUuid, java.util.Arrays.hashCode(mServiceData), java.util.Arrays.hashCode(mServiceDataMask), mServiceUuid, mServiceUuidMask);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        android.bluetooth.le.ScanFilter other = ((android.bluetooth.le.ScanFilter) (obj));
        return ((((((((java.util.Objects.equals(mDeviceName, other.mDeviceName) && java.util.Objects.equals(mDeviceAddress, other.mDeviceAddress)) && (mManufacturerId == other.mManufacturerId)) && java.util.Objects.deepEquals(mManufacturerData, other.mManufacturerData)) && java.util.Objects.deepEquals(mManufacturerDataMask, other.mManufacturerDataMask)) && java.util.Objects.equals(mServiceDataUuid, other.mServiceDataUuid)) && java.util.Objects.deepEquals(mServiceData, other.mServiceData)) && java.util.Objects.deepEquals(mServiceDataMask, other.mServiceDataMask)) && java.util.Objects.equals(mServiceUuid, other.mServiceUuid)) && java.util.Objects.equals(mServiceUuidMask, other.mServiceUuidMask);
    }

    /**
     * Checks if the scanfilter is empty
     *
     * @unknown 
     */
    public boolean isAllFieldsEmpty() {
        return android.bluetooth.le.ScanFilter.EMPTY.equals(this);
    }

    /**
     * Builder class for {@link ScanFilter}.
     */
    public static final class Builder {
        private java.lang.String mDeviceName;

        private java.lang.String mDeviceAddress;

        private android.os.ParcelUuid mServiceUuid;

        private android.os.ParcelUuid mUuidMask;

        private android.os.ParcelUuid mServiceDataUuid;

        private byte[] mServiceData;

        private byte[] mServiceDataMask;

        private int mManufacturerId = -1;

        private byte[] mManufacturerData;

        private byte[] mManufacturerDataMask;

        /**
         * Set filter on device name.
         */
        public android.bluetooth.le.ScanFilter.Builder setDeviceName(java.lang.String deviceName) {
            mDeviceName = deviceName;
            return this;
        }

        /**
         * Set filter on device address.
         *
         * @param deviceAddress
         * 		The device Bluetooth address for the filter. It needs to be in the
         * 		format of "01:02:03:AB:CD:EF". The device address can be validated using
         * 		{@link BluetoothAdapter#checkBluetoothAddress}.
         * @throws IllegalArgumentException
         * 		If the {@code deviceAddress} is invalid.
         */
        public android.bluetooth.le.ScanFilter.Builder setDeviceAddress(java.lang.String deviceAddress) {
            if ((deviceAddress != null) && (!android.bluetooth.BluetoothAdapter.checkBluetoothAddress(deviceAddress))) {
                throw new java.lang.IllegalArgumentException("invalid device address " + deviceAddress);
            }
            mDeviceAddress = deviceAddress;
            return this;
        }

        /**
         * Set filter on service uuid.
         */
        public android.bluetooth.le.ScanFilter.Builder setServiceUuid(android.os.ParcelUuid serviceUuid) {
            mServiceUuid = serviceUuid;
            mUuidMask = null;// clear uuid mask

            return this;
        }

        /**
         * Set filter on partial service uuid. The {@code uuidMask} is the bit mask for the
         * {@code serviceUuid}. Set any bit in the mask to 1 to indicate a match is needed for the
         * bit in {@code serviceUuid}, and 0 to ignore that bit.
         *
         * @throws IllegalArgumentException
         * 		If {@code serviceUuid} is {@code null} but
         * 		{@code uuidMask} is not {@code null}.
         */
        public android.bluetooth.le.ScanFilter.Builder setServiceUuid(android.os.ParcelUuid serviceUuid, android.os.ParcelUuid uuidMask) {
            if ((mUuidMask != null) && (mServiceUuid == null)) {
                throw new java.lang.IllegalArgumentException("uuid is null while uuidMask is not null!");
            }
            mServiceUuid = serviceUuid;
            mUuidMask = uuidMask;
            return this;
        }

        /**
         * Set filtering on service data.
         *
         * @throws IllegalArgumentException
         * 		If {@code serviceDataUuid} is null.
         */
        public android.bluetooth.le.ScanFilter.Builder setServiceData(android.os.ParcelUuid serviceDataUuid, byte[] serviceData) {
            if (serviceDataUuid == null) {
                throw new java.lang.IllegalArgumentException("serviceDataUuid is null");
            }
            mServiceDataUuid = serviceDataUuid;
            mServiceData = serviceData;
            mServiceDataMask = null;// clear service data mask

            return this;
        }

        /**
         * Set partial filter on service data. For any bit in the mask, set it to 1 if it needs to
         * match the one in service data, otherwise set it to 0 to ignore that bit.
         * <p>
         * The {@code serviceDataMask} must have the same length of the {@code serviceData}.
         *
         * @throws IllegalArgumentException
         * 		If {@code serviceDataUuid} is null or
         * 		{@code serviceDataMask} is {@code null} while {@code serviceData} is not or
         * 		{@code serviceDataMask} and {@code serviceData} has different length.
         */
        public android.bluetooth.le.ScanFilter.Builder setServiceData(android.os.ParcelUuid serviceDataUuid, byte[] serviceData, byte[] serviceDataMask) {
            if (serviceDataUuid == null) {
                throw new java.lang.IllegalArgumentException("serviceDataUuid is null");
            }
            if (mServiceDataMask != null) {
                if (mServiceData == null) {
                    throw new java.lang.IllegalArgumentException("serviceData is null while serviceDataMask is not null");
                }
                // Since the mServiceDataMask is a bit mask for mServiceData, the lengths of the two
                // byte array need to be the same.
                if (mServiceData.length != mServiceDataMask.length) {
                    throw new java.lang.IllegalArgumentException("size mismatch for service data and service data mask");
                }
            }
            mServiceDataUuid = serviceDataUuid;
            mServiceData = serviceData;
            mServiceDataMask = serviceDataMask;
            return this;
        }

        /**
         * Set filter on on manufacturerData. A negative manufacturerId is considered as invalid id.
         * <p>
         * Note the first two bytes of the {@code manufacturerData} is the manufacturerId.
         *
         * @throws IllegalArgumentException
         * 		If the {@code manufacturerId} is invalid.
         */
        public android.bluetooth.le.ScanFilter.Builder setManufacturerData(int manufacturerId, byte[] manufacturerData) {
            if ((manufacturerData != null) && (manufacturerId < 0)) {
                throw new java.lang.IllegalArgumentException("invalid manufacture id");
            }
            mManufacturerId = manufacturerId;
            mManufacturerData = manufacturerData;
            mManufacturerDataMask = null;// clear manufacturer data mask

            return this;
        }

        /**
         * Set filter on partial manufacture data. For any bit in the mask, set it the 1 if it needs
         * to match the one in manufacturer data, otherwise set it to 0.
         * <p>
         * The {@code manufacturerDataMask} must have the same length of {@code manufacturerData}.
         *
         * @throws IllegalArgumentException
         * 		If the {@code manufacturerId} is invalid, or
         * 		{@code manufacturerData} is null while {@code manufacturerDataMask} is not,
         * 		or {@code manufacturerData} and {@code manufacturerDataMask} have different
         * 		length.
         */
        public android.bluetooth.le.ScanFilter.Builder setManufacturerData(int manufacturerId, byte[] manufacturerData, byte[] manufacturerDataMask) {
            if ((manufacturerData != null) && (manufacturerId < 0)) {
                throw new java.lang.IllegalArgumentException("invalid manufacture id");
            }
            if (mManufacturerDataMask != null) {
                if (mManufacturerData == null) {
                    throw new java.lang.IllegalArgumentException("manufacturerData is null while manufacturerDataMask is not null");
                }
                // Since the mManufacturerDataMask is a bit mask for mManufacturerData, the lengths
                // of the two byte array need to be the same.
                if (mManufacturerData.length != mManufacturerDataMask.length) {
                    throw new java.lang.IllegalArgumentException("size mismatch for manufacturerData and manufacturerDataMask");
                }
            }
            mManufacturerId = manufacturerId;
            mManufacturerData = manufacturerData;
            mManufacturerDataMask = manufacturerDataMask;
            return this;
        }

        /**
         * Build {@link ScanFilter}.
         *
         * @throws IllegalArgumentException
         * 		If the filter cannot be built.
         */
        public android.bluetooth.le.ScanFilter build() {
            return new android.bluetooth.le.ScanFilter(mDeviceName, mDeviceAddress, mServiceUuid, mUuidMask, mServiceDataUuid, mServiceData, mServiceDataMask, mManufacturerId, mManufacturerData, mManufacturerDataMask);
        }
    }
}

