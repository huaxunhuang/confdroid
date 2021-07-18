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
 * Represents a Bluetooth GATT Service
 *
 * <p> Gatt Service contains a collection of {@link BluetoothGattCharacteristic},
 * as well as referenced services.
 */
public class BluetoothGattService implements android.os.Parcelable {
    /**
     * Primary service
     */
    public static final int SERVICE_TYPE_PRIMARY = 0;

    /**
     * Secondary service (included by primary services)
     */
    public static final int SERVICE_TYPE_SECONDARY = 1;

    /**
     * The remote device his service is associated with.
     * This applies to client applications only.
     *
     * @unknown 
     */
    protected android.bluetooth.BluetoothDevice mDevice;

    /**
     * The UUID of this service.
     *
     * @unknown 
     */
    protected java.util.UUID mUuid;

    /**
     * Instance ID for this service.
     *
     * @unknown 
     */
    protected int mInstanceId;

    /**
     * Handle counter override (for conformance testing).
     *
     * @unknown 
     */
    protected int mHandles = 0;

    /**
     * Service type (Primary/Secondary).
     *
     * @unknown 
     */
    protected int mServiceType;

    /**
     * List of characteristics included in this service.
     */
    protected java.util.List<android.bluetooth.BluetoothGattCharacteristic> mCharacteristics;

    /**
     * List of included services for this service.
     */
    protected java.util.List<android.bluetooth.BluetoothGattService> mIncludedServices;

    /**
     * Whether the service uuid should be advertised.
     */
    private boolean mAdvertisePreferred;

    /**
     * Create a new BluetoothGattService.
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param uuid
     * 		The UUID for this service
     * @param serviceType
     * 		The type of this service,
     * 		{@link BluetoothGattService#SERVICE_TYPE_PRIMARY} or
     * 		{@link BluetoothGattService#SERVICE_TYPE_SECONDARY}
     */
    public BluetoothGattService(java.util.UUID uuid, int serviceType) {
        mDevice = null;
        mUuid = uuid;
        mInstanceId = 0;
        mServiceType = serviceType;
        mCharacteristics = new java.util.ArrayList<android.bluetooth.BluetoothGattCharacteristic>();
        mIncludedServices = new java.util.ArrayList<android.bluetooth.BluetoothGattService>();
    }

    /**
     * Create a new BluetoothGattService
     *
     * @unknown 
     */
    /* package */
    BluetoothGattService(android.bluetooth.BluetoothDevice device, java.util.UUID uuid, int instanceId, int serviceType) {
        mDevice = device;
        mUuid = uuid;
        mInstanceId = instanceId;
        mServiceType = serviceType;
        mCharacteristics = new java.util.ArrayList<android.bluetooth.BluetoothGattCharacteristic>();
        mIncludedServices = new java.util.ArrayList<android.bluetooth.BluetoothGattService>();
    }

    /**
     * Create a new BluetoothGattService
     *
     * @unknown 
     */
    public BluetoothGattService(java.util.UUID uuid, int instanceId, int serviceType) {
        mDevice = null;
        mUuid = uuid;
        mInstanceId = instanceId;
        mServiceType = serviceType;
        mCharacteristics = new java.util.ArrayList<android.bluetooth.BluetoothGattCharacteristic>();
        mIncludedServices = new java.util.ArrayList<android.bluetooth.BluetoothGattService>();
    }

    /**
     *
     *
     * @unknown 
     */
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeParcelable(new android.os.ParcelUuid(mUuid), 0);
        out.writeInt(mInstanceId);
        out.writeInt(mServiceType);
        out.writeTypedList(mCharacteristics);
        java.util.ArrayList<android.bluetooth.BluetoothGattIncludedService> includedServices = new java.util.ArrayList<android.bluetooth.BluetoothGattIncludedService>(mIncludedServices.size());
        for (android.bluetooth.BluetoothGattService s : mIncludedServices) {
            includedServices.add(new android.bluetooth.BluetoothGattIncludedService(s.getUuid(), s.getInstanceId(), s.getType()));
        }
        out.writeTypedList(includedServices);
    }

    public static final android.os.Parcelable.Creator<android.bluetooth.BluetoothGattService> CREATOR = new android.os.Parcelable.Creator<android.bluetooth.BluetoothGattService>() {
        public android.bluetooth.BluetoothGattService createFromParcel(android.os.Parcel in) {
            return new android.bluetooth.BluetoothGattService(in);
        }

        public android.bluetooth.BluetoothGattService[] newArray(int size) {
            return new android.bluetooth.BluetoothGattService[size];
        }
    };

    private BluetoothGattService(android.os.Parcel in) {
        mUuid = ((android.os.ParcelUuid) (in.readParcelable(null))).getUuid();
        mInstanceId = in.readInt();
        mServiceType = in.readInt();
        mCharacteristics = new java.util.ArrayList<android.bluetooth.BluetoothGattCharacteristic>();
        java.util.ArrayList<android.bluetooth.BluetoothGattCharacteristic> chrcs = in.createTypedArrayList(android.bluetooth.BluetoothGattCharacteristic.CREATOR);
        if (chrcs != null) {
            for (android.bluetooth.BluetoothGattCharacteristic chrc : chrcs) {
                chrc.setService(this);
                mCharacteristics.add(chrc);
            }
        }
        mIncludedServices = new java.util.ArrayList<android.bluetooth.BluetoothGattService>();
        java.util.ArrayList<android.bluetooth.BluetoothGattIncludedService> inclSvcs = in.createTypedArrayList(android.bluetooth.BluetoothGattIncludedService.CREATOR);
        if (chrcs != null) {
            for (android.bluetooth.BluetoothGattIncludedService isvc : inclSvcs) {
                mIncludedServices.add(new android.bluetooth.BluetoothGattService(null, isvc.getUuid(), isvc.getInstanceId(), isvc.getType()));
            }
        }
    }

    /**
     * Returns the device associated with this service.
     *
     * @unknown 
     */
    /* package */
    android.bluetooth.BluetoothDevice getDevice() {
        return mDevice;
    }

    /**
     * Returns the device associated with this service.
     *
     * @unknown 
     */
    /* package */
    void setDevice(android.bluetooth.BluetoothDevice device) {
        this.mDevice = device;
    }

    /**
     * Add an included service to this service.
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param service
     * 		The service to be added
     * @return true, if the included service was added to the service
     */
    public boolean addService(android.bluetooth.BluetoothGattService service) {
        mIncludedServices.add(service);
        return true;
    }

    /**
     * Add a characteristic to this service.
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param characteristic
     * 		The characteristics to be added
     * @return true, if the characteristic was added to the service
     */
    public boolean addCharacteristic(android.bluetooth.BluetoothGattCharacteristic characteristic) {
        mCharacteristics.add(characteristic);
        characteristic.setService(this);
        return true;
    }

    /**
     * Get characteristic by UUID and instanceId.
     *
     * @unknown 
     */
    /* package */
    android.bluetooth.BluetoothGattCharacteristic getCharacteristic(java.util.UUID uuid, int instanceId) {
        for (android.bluetooth.BluetoothGattCharacteristic characteristic : mCharacteristics) {
            if (uuid.equals(characteristic.getUuid()) && (characteristic.getInstanceId() == instanceId))
                return characteristic;

        }
        return null;
    }

    /**
     * Force the instance ID.
     * This is needed for conformance testing only.
     *
     * @unknown 
     */
    public void setInstanceId(int instanceId) {
        mInstanceId = instanceId;
    }

    /**
     * Get the handle count override (conformance testing.
     *
     * @unknown 
     */
    /* package */
    int getHandles() {
        return mHandles;
    }

    /**
     * Force the number of handles to reserve for this service.
     * This is needed for conformance testing only.
     *
     * @unknown 
     */
    public void setHandles(int handles) {
        mHandles = handles;
    }

    /**
     * Add an included service to the internal map.
     *
     * @unknown 
     */
    public void addIncludedService(android.bluetooth.BluetoothGattService includedService) {
        mIncludedServices.add(includedService);
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

    /**
     * Get the list of included GATT services for this service.
     *
     * @return List of included services or empty list if no included services
    were discovered.
     */
    public java.util.List<android.bluetooth.BluetoothGattService> getIncludedServices() {
        return mIncludedServices;
    }

    /**
     * Returns a list of characteristics included in this service.
     *
     * @return Characteristics included in this service
     */
    public java.util.List<android.bluetooth.BluetoothGattCharacteristic> getCharacteristics() {
        return mCharacteristics;
    }

    /**
     * Returns a characteristic with a given UUID out of the list of
     * characteristics offered by this service.
     *
     * <p>This is a convenience function to allow access to a given characteristic
     * without enumerating over the list returned by {@link #getCharacteristics}
     * manually.
     *
     * <p>If a remote service offers multiple characteristics with the same
     * UUID, the first instance of a characteristic with the given UUID
     * is returned.
     *
     * @return GATT characteristic object or null if no characteristic with the
    given UUID was found.
     */
    public android.bluetooth.BluetoothGattCharacteristic getCharacteristic(java.util.UUID uuid) {
        for (android.bluetooth.BluetoothGattCharacteristic characteristic : mCharacteristics) {
            if (uuid.equals(characteristic.getUuid()))
                return characteristic;

        }
        return null;
    }

    /**
     * Returns whether the uuid of the service should be advertised.
     *
     * @unknown 
     */
    public boolean isAdvertisePreferred() {
        return mAdvertisePreferred;
    }

    /**
     * Set whether the service uuid should be advertised.
     *
     * @unknown 
     */
    public void setAdvertisePreferred(boolean advertisePreferred) {
        this.mAdvertisePreferred = advertisePreferred;
    }
}

