/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.hardware.usb;


/**
 * Describes the status of a USB port.
 * <p>
 * This object is immutable.
 * </p>
 *
 * @unknown 
 */
public final class UsbPortStatus implements android.os.Parcelable {
    private final int mCurrentMode;

    private final int mCurrentPowerRole;

    private final int mCurrentDataRole;

    private final int mSupportedRoleCombinations;

    /**
     *
     *
     * @unknown 
     */
    public UsbPortStatus(int currentMode, int currentPowerRole, int currentDataRole, int supportedRoleCombinations) {
        mCurrentMode = currentMode;
        mCurrentPowerRole = currentPowerRole;
        mCurrentDataRole = currentDataRole;
        mSupportedRoleCombinations = supportedRoleCombinations;
    }

    /**
     * Returns true if there is anything connected to the port.
     *
     * @return True if there is anything connected to the port.
     */
    public boolean isConnected() {
        return mCurrentMode != 0;
    }

    /**
     * Gets the current mode of the port.
     *
     * @return The current mode: {@link UsbPort#MODE_DFP}, {@link UsbPort#MODE_UFP},
    or 0 if nothing is connected.
     */
    public int getCurrentMode() {
        return mCurrentMode;
    }

    /**
     * Gets the current power role of the port.
     *
     * @return The current power role: {@link UsbPort#POWER_ROLE_SOURCE},
    {@link UsbPort#POWER_ROLE_SINK}, or 0 if nothing is connected.
     */
    public int getCurrentPowerRole() {
        return mCurrentPowerRole;
    }

    /**
     * Gets the current data role of the port.
     *
     * @return The current data role: {@link UsbPort#DATA_ROLE_HOST},
    {@link UsbPort#DATA_ROLE_DEVICE}, or 0 if nothing is connected.
     */
    public int getCurrentDataRole() {
        return mCurrentDataRole;
    }

    /**
     * Returns true if the specified power and data role combination is supported
     * given what is currently connected to the port.
     *
     * @param powerRole
     * 		The power role to check: {@link UsbPort#POWER_ROLE_SOURCE}
     * 		or {@link UsbPort#POWER_ROLE_SINK}, or 0 if no power role.
     * @param dataRole
     * 		The data role to check: either {@link UsbPort#DATA_ROLE_HOST}
     * 		or {@link UsbPort#DATA_ROLE_DEVICE}, or 0 if no data role.
     */
    public boolean isRoleCombinationSupported(int powerRole, int dataRole) {
        return (mSupportedRoleCombinations & android.hardware.usb.UsbPort.combineRolesAsBit(powerRole, dataRole)) != 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getSupportedRoleCombinations() {
        return mSupportedRoleCombinations;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((("UsbPortStatus{connected=" + isConnected()) + ", currentMode=") + android.hardware.usb.UsbPort.modeToString(mCurrentMode)) + ", currentPowerRole=") + android.hardware.usb.UsbPort.powerRoleToString(mCurrentPowerRole)) + ", currentDataRole=") + android.hardware.usb.UsbPort.dataRoleToString(mCurrentDataRole)) + ", supportedRoleCombinations=") + android.hardware.usb.UsbPort.roleCombinationsToString(mSupportedRoleCombinations)) + "}";
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mCurrentMode);
        dest.writeInt(mCurrentPowerRole);
        dest.writeInt(mCurrentDataRole);
        dest.writeInt(mSupportedRoleCombinations);
    }

    public static final android.os.Parcelable.Creator<android.hardware.usb.UsbPortStatus> CREATOR = new android.os.Parcelable.Creator<android.hardware.usb.UsbPortStatus>() {
        @java.lang.Override
        public android.hardware.usb.UsbPortStatus createFromParcel(android.os.Parcel in) {
            int currentMode = in.readInt();
            int currentPowerRole = in.readInt();
            int currentDataRole = in.readInt();
            int supportedRoleCombinations = in.readInt();
            return new android.hardware.usb.UsbPortStatus(currentMode, currentPowerRole, currentDataRole, supportedRoleCombinations);
        }

        @java.lang.Override
        public android.hardware.usb.UsbPortStatus[] newArray(int size) {
            return new android.hardware.usb.UsbPortStatus[size];
        }
    };
}

