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
 * Represents a Bluetooth GATT Included Service
 *
 * @unknown 
 */
public class BluetoothGattIncludedService implements android.os.Parcelable {
    /**
     * The UUID of this service.
     */
    protected java.util.UUID mUuid;

    /**
     * Instance ID for this service.
     */
    protected int mInstanceId;

    /**
     * Service type (Primary/Secondary).
     */
    protected int mServiceType;

    /**
     * Create a new BluetoothGattIncludedService
     */
    public BluetoothGattIncludedService(java.util.UUID uuid, int instanceId, int serviceType) {
        mUuid = uuid;
        mInstanceId = instanceId;
        mServiceType = serviceType;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeParcelable(new android.os.ParcelUuid(mUuid), 0);
        out.writeInt(mInstanceId);
        out.writeInt(mServiceType);
    }

    public static final android.os.Parcelable.Creator<android.bluetooth.BluetoothGattIncludedService> CREATOR = new android.os.Parcelable.Creator<android.bluetooth.BluetoothGattIncludedService>() {
        public android.bluetooth.BluetoothGattIncludedService createFromParcel(android.os.Parcel in) {
            return new android.bluetooth.BluetoothGattIncludedService(in);
        }

        public android.bluetooth.BluetoothGattIncludedService[] newArray(int size) {
            return new android.bluetooth.BluetoothGattIncludedService[size];
        }
    };

    private BluetoothGattIncludedService(android.os.Parcel in) {
        mUuid = ((android.os.ParcelUuid) (in.readParcelable(null))).getUuid();
        mInstanceId = in.readInt();
        mServiceType = in.readInt();
    }

    /**
     * Returns the UUID of this service
     *
     * @return UUID of this service
     */
    public java.util.UUID getUuid() {
        return mUuid;
    }

    /**
     * Returns the instance ID for this service
     *
     * <p>If a remote device offers multiple services with the same UUID
     * (ex. multiple battery services for different batteries), the instance
     * ID is used to distuinguish services.
     *
     * @return Instance ID of this service
     */
    public int getInstanceId() {
        return mInstanceId;
    }

    /**
     * Get the type of this service (primary/secondary)
     */
    public int getType() {
        return mServiceType;
    }
}

