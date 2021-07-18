/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.hardware.display;


/**
 * Describes the properties of a Wifi display.
 * <p>
 * This object is immutable.
 * </p>
 *
 * @unknown 
 */
public final class WifiDisplay implements android.os.Parcelable {
    private final java.lang.String mDeviceAddress;

    private final java.lang.String mDeviceName;

    private final java.lang.String mDeviceAlias;

    private final boolean mIsAvailable;

    private final boolean mCanConnect;

    private final boolean mIsRemembered;

    public static final android.hardware.display.WifiDisplay[] EMPTY_ARRAY = new android.hardware.display.WifiDisplay[0];

    public static final android.os.Parcelable.Creator<android.hardware.display.WifiDisplay> CREATOR = new android.os.Parcelable.Creator<android.hardware.display.WifiDisplay>() {
        public android.hardware.display.WifiDisplay createFromParcel(android.os.Parcel in) {
            java.lang.String deviceAddress = in.readString();
            java.lang.String deviceName = in.readString();
            java.lang.String deviceAlias = in.readString();
            boolean isAvailable = in.readInt() != 0;
            boolean canConnect = in.readInt() != 0;
            boolean isRemembered = in.readInt() != 0;
            return new android.hardware.display.WifiDisplay(deviceAddress, deviceName, deviceAlias, isAvailable, canConnect, isRemembered);
        }

        public android.hardware.display.WifiDisplay[] newArray(int size) {
            return size == 0 ? android.hardware.display.WifiDisplay.EMPTY_ARRAY : new android.hardware.display.WifiDisplay[size];
        }
    };

    public WifiDisplay(java.lang.String deviceAddress, java.lang.String deviceName, java.lang.String deviceAlias, boolean available, boolean canConnect, boolean remembered) {
        if (deviceAddress == null) {
            throw new java.lang.IllegalArgumentException("deviceAddress must not be null");
        }
        if (deviceName == null) {
            throw new java.lang.IllegalArgumentException("deviceName must not be null");
        }
        mDeviceAddress = deviceAddress;
        mDeviceName = deviceName;
        mDeviceAlias = deviceAlias;
        mIsAvailable = available;
        mCanConnect = canConnect;
        mIsRemembered = remembered;
    }

    /**
     * Gets the MAC address of the Wifi display device.
     */
    public java.lang.String getDeviceAddress() {
        return mDeviceAddress;
    }

    /**
     * Gets the name of the Wifi display device.
     */
    public java.lang.String getDeviceName() {
        return mDeviceName;
    }

    /**
     * Gets the user-specified alias of the Wifi display device, or null if none.
     * <p>
     * The alias should be used in the UI whenever available.  It is the value
     * provided by the user when renaming the device.
     * </p>
     */
    public java.lang.String getDeviceAlias() {
        return mDeviceAlias;
    }

    /**
     * Returns true if device is available, false otherwise.
     */
    public boolean isAvailable() {
        return mIsAvailable;
    }

    /**
     * Returns true if device can be connected to (not in use), false otherwise.
     */
    public boolean canConnect() {
        return mCanConnect;
    }

    /**
     * Returns true if device has been remembered, false otherwise.
     */
    public boolean isRemembered() {
        return mIsRemembered;
    }

    /**
     * Gets the name to show in the UI.
     * Uses the device alias if available, otherwise uses the device name.
     */
    public java.lang.String getFriendlyDisplayName() {
        return mDeviceAlias != null ? mDeviceAlias : mDeviceName;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        return (o instanceof android.hardware.display.WifiDisplay) && equals(((android.hardware.display.WifiDisplay) (o)));
    }

    /**
     * Returns true if the two displays have the same identity (address, name and alias).
     * This method does not compare the current status of the displays.
     */
    public boolean equals(android.hardware.display.WifiDisplay other) {
        return (((other != null) && mDeviceAddress.equals(other.mDeviceAddress)) && mDeviceName.equals(other.mDeviceName)) && libcore.util.Objects.equal(mDeviceAlias, other.mDeviceAlias);
    }

    /**
     * Returns true if the other display is not null and has the same address as this one.
     * Can be used to perform identity comparisons on displays ignoring properties
     * that might change during a connection such as the name or alias.
     */
    public boolean hasSameAddress(android.hardware.display.WifiDisplay other) {
        return (other != null) && mDeviceAddress.equals(other.mDeviceAddress);
    }

    @java.lang.Override
    public int hashCode() {
        // The address on its own should be sufficiently unique for hashing purposes.
        return mDeviceAddress.hashCode();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(mDeviceAddress);
        dest.writeString(mDeviceName);
        dest.writeString(mDeviceAlias);
        dest.writeInt(mIsAvailable ? 1 : 0);
        dest.writeInt(mCanConnect ? 1 : 0);
        dest.writeInt(mIsRemembered ? 1 : 0);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    // For debugging purposes only.
    @java.lang.Override
    public java.lang.String toString() {
        java.lang.String result = ((mDeviceName + " (") + mDeviceAddress) + ")";
        if (mDeviceAlias != null) {
            result += ", alias " + mDeviceAlias;
        }
        result += ((((", isAvailable " + mIsAvailable) + ", canConnect ") + mCanConnect) + ", isRemembered ") + mIsRemembered;
        return result;
    }
}

