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
 * Advertise data packet container for Bluetooth LE advertising. This represents the data to be
 * advertised as well as the scan response data for active scans.
 * <p>
 * Use {@link AdvertiseData.Builder} to create an instance of {@link AdvertiseData} to be
 * advertised.
 *
 * @see BluetoothLeAdvertiser
 * @see ScanRecord
 */
public final class AdvertiseData implements android.os.Parcelable {
    @android.annotation.Nullable
    private final java.util.List<android.os.ParcelUuid> mServiceUuids;

    private final android.util.SparseArray<byte[]> mManufacturerSpecificData;

    private final java.util.Map<android.os.ParcelUuid, byte[]> mServiceData;

    private final boolean mIncludeTxPowerLevel;

    private final boolean mIncludeDeviceName;

    private AdvertiseData(java.util.List<android.os.ParcelUuid> serviceUuids, android.util.SparseArray<byte[]> manufacturerData, java.util.Map<android.os.ParcelUuid, byte[]> serviceData, boolean includeTxPowerLevel, boolean includeDeviceName) {
        mServiceUuids = serviceUuids;
        mManufacturerSpecificData = manufacturerData;
        mServiceData = serviceData;
        mIncludeTxPowerLevel = includeTxPowerLevel;
        mIncludeDeviceName = includeDeviceName;
    }

    /**
     * Returns a list of service UUIDs within the advertisement that are used to identify the
     * Bluetooth GATT services.
     */
    public java.util.List<android.os.ParcelUuid> getServiceUuids() {
        return mServiceUuids;
    }

    /**
     * Returns an array of manufacturer Id and the corresponding manufacturer specific data. The
     * manufacturer id is a non-negative number assigned by Bluetooth SIG.
     */
    public android.util.SparseArray<byte[]> getManufacturerSpecificData() {
        return mManufacturerSpecificData;
    }

    /**
     * Returns a map of 16-bit UUID and its corresponding service data.
     */
    public java.util.Map<android.os.ParcelUuid, byte[]> getServiceData() {
        return mServiceData;
    }

    /**
     * Whether the transmission power level will be included in the advertisement packet.
     */
    public boolean getIncludeTxPowerLevel() {
        return mIncludeTxPowerLevel;
    }

    /**
     * Whether the device name will be included in the advertisement packet.
     */
    public boolean getIncludeDeviceName() {
        return mIncludeDeviceName;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mServiceUuids, mManufacturerSpecificData, mServiceData, mIncludeDeviceName, mIncludeTxPowerLevel);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        android.bluetooth.le.AdvertiseData other = ((android.bluetooth.le.AdvertiseData) (obj));
        return (((java.util.Objects.equals(mServiceUuids, other.mServiceUuids) && android.bluetooth.le.BluetoothLeUtils.equals(mManufacturerSpecificData, other.mManufacturerSpecificData)) && android.bluetooth.le.BluetoothLeUtils.equals(mServiceData, other.mServiceData)) && (mIncludeDeviceName == other.mIncludeDeviceName)) && (mIncludeTxPowerLevel == other.mIncludeTxPowerLevel);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((("AdvertiseData [mServiceUuids=" + mServiceUuids) + ", mManufacturerSpecificData=") + android.bluetooth.le.BluetoothLeUtils.toString(mManufacturerSpecificData)) + ", mServiceData=") + android.bluetooth.le.BluetoothLeUtils.toString(mServiceData)) + ", mIncludeTxPowerLevel=") + mIncludeTxPowerLevel) + ", mIncludeDeviceName=") + mIncludeDeviceName) + "]";
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeList(mServiceUuids);
        // mManufacturerSpecificData could not be null.
        dest.writeInt(mManufacturerSpecificData.size());
        for (int i = 0; i < mManufacturerSpecificData.size(); ++i) {
            dest.writeInt(mManufacturerSpecificData.keyAt(i));
            byte[] data = mManufacturerSpecificData.valueAt(i);
            if (data == null) {
                dest.writeInt(0);
            } else {
                dest.writeInt(1);
                dest.writeInt(data.length);
                dest.writeByteArray(data);
            }
        }
        dest.writeInt(mServiceData.size());
        for (android.os.ParcelUuid uuid : mServiceData.keySet()) {
            dest.writeParcelable(uuid, flags);
            byte[] data = mServiceData.get(uuid);
            if (data == null) {
                dest.writeInt(0);
            } else {
                dest.writeInt(1);
                dest.writeInt(data.length);
                dest.writeByteArray(data);
            }
        }
        dest.writeByte(((byte) (getIncludeTxPowerLevel() ? 1 : 0)));
        dest.writeByte(((byte) (getIncludeDeviceName() ? 1 : 0)));
    }

    public static final android.os.Parcelable.Creator<android.bluetooth.le.AdvertiseData> CREATOR = new android.os.Parcelable.Creator<android.bluetooth.le.AdvertiseData>() {
        @java.lang.Override
        public android.bluetooth.le.AdvertiseData[] newArray(int size) {
            return new android.bluetooth.le.AdvertiseData[size];
        }

        @java.lang.Override
        public android.bluetooth.le.AdvertiseData createFromParcel(android.os.Parcel in) {
            android.bluetooth.le.AdvertiseData.Builder builder = new android.bluetooth.le.AdvertiseData.Builder();
            @java.lang.SuppressWarnings("unchecked")
            java.util.List<android.os.ParcelUuid> uuids = in.readArrayList(android.os.ParcelUuid.class.getClassLoader());
            if (uuids != null) {
                for (android.os.ParcelUuid uuid : uuids) {
                    builder.addServiceUuid(uuid);
                }
            }
            int manufacturerSize = in.readInt();
            for (int i = 0; i < manufacturerSize; ++i) {
                int manufacturerId = in.readInt();
                if (in.readInt() == 1) {
                    int manufacturerDataLength = in.readInt();
                    byte[] manufacturerData = new byte[manufacturerDataLength];
                    in.readByteArray(manufacturerData);
                    builder.addManufacturerData(manufacturerId, manufacturerData);
                }
            }
            int serviceDataSize = in.readInt();
            for (int i = 0; i < serviceDataSize; ++i) {
                android.os.ParcelUuid serviceDataUuid = in.readParcelable(android.os.ParcelUuid.class.getClassLoader());
                if (in.readInt() == 1) {
                    int serviceDataLength = in.readInt();
                    byte[] serviceData = new byte[serviceDataLength];
                    in.readByteArray(serviceData);
                    builder.addServiceData(serviceDataUuid, serviceData);
                }
            }
            builder.setIncludeTxPowerLevel(in.readByte() == 1);
            builder.setIncludeDeviceName(in.readByte() == 1);
            return builder.build();
        }
    };

    /**
     * Builder for {@link AdvertiseData}.
     */
    public static final class Builder {
        @android.annotation.Nullable
        private java.util.List<android.os.ParcelUuid> mServiceUuids = new java.util.ArrayList<android.os.ParcelUuid>();

        private android.util.SparseArray<byte[]> mManufacturerSpecificData = new android.util.SparseArray<byte[]>();

        private java.util.Map<android.os.ParcelUuid, byte[]> mServiceData = new android.util.ArrayMap<android.os.ParcelUuid, byte[]>();

        private boolean mIncludeTxPowerLevel;

        private boolean mIncludeDeviceName;

        /**
         * Add a service UUID to advertise data.
         *
         * @param serviceUuid
         * 		A service UUID to be advertised.
         * @throws IllegalArgumentException
         * 		If the {@code serviceUuids} are null.
         */
        public android.bluetooth.le.AdvertiseData.Builder addServiceUuid(android.os.ParcelUuid serviceUuid) {
            if (serviceUuid == null) {
                throw new java.lang.IllegalArgumentException("serivceUuids are null");
            }
            mServiceUuids.add(serviceUuid);
            return this;
        }

        /**
         * Add service data to advertise data.
         *
         * @param serviceDataUuid
         * 		16-bit UUID of the service the data is associated with
         * @param serviceData
         * 		Service data
         * @throws IllegalArgumentException
         * 		If the {@code serviceDataUuid} or {@code serviceData} is
         * 		empty.
         */
        public android.bluetooth.le.AdvertiseData.Builder addServiceData(android.os.ParcelUuid serviceDataUuid, byte[] serviceData) {
            if ((serviceDataUuid == null) || (serviceData == null)) {
                throw new java.lang.IllegalArgumentException("serviceDataUuid or serviceDataUuid is null");
            }
            mServiceData.put(serviceDataUuid, serviceData);
            return this;
        }

        /**
         * Add manufacturer specific data.
         * <p>
         * Please refer to the Bluetooth Assigned Numbers document provided by the <a
         * href="https://www.bluetooth.org">Bluetooth SIG</a> for a list of existing company
         * identifiers.
         *
         * @param manufacturerId
         * 		Manufacturer ID assigned by Bluetooth SIG.
         * @param manufacturerSpecificData
         * 		Manufacturer specific data
         * @throws IllegalArgumentException
         * 		If the {@code manufacturerId} is negative or
         * 		{@code manufacturerSpecificData} is null.
         */
        public android.bluetooth.le.AdvertiseData.Builder addManufacturerData(int manufacturerId, byte[] manufacturerSpecificData) {
            if (manufacturerId < 0) {
                throw new java.lang.IllegalArgumentException("invalid manufacturerId - " + manufacturerId);
            }
            if (manufacturerSpecificData == null) {
                throw new java.lang.IllegalArgumentException("manufacturerSpecificData is null");
            }
            mManufacturerSpecificData.put(manufacturerId, manufacturerSpecificData);
            return this;
        }

        /**
         * Whether the transmission power level should be included in the advertise packet. Tx power
         * level field takes 3 bytes in advertise packet.
         */
        public android.bluetooth.le.AdvertiseData.Builder setIncludeTxPowerLevel(boolean includeTxPowerLevel) {
            mIncludeTxPowerLevel = includeTxPowerLevel;
            return this;
        }

        /**
         * Set whether the device name should be included in advertise packet.
         */
        public android.bluetooth.le.AdvertiseData.Builder setIncludeDeviceName(boolean includeDeviceName) {
            mIncludeDeviceName = includeDeviceName;
            return this;
        }

        /**
         * Build the {@link AdvertiseData}.
         */
        public android.bluetooth.le.AdvertiseData build() {
            return new android.bluetooth.le.AdvertiseData(mServiceUuids, mManufacturerSpecificData, mServiceData, mIncludeTxPowerLevel, mIncludeDeviceName);
        }
    }
}

