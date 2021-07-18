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
 * Represents a physical USB port and describes its characteristics.
 * <p>
 * This object is immutable.
 * </p>
 *
 * @unknown 
 */
public final class UsbPort implements android.os.Parcelable {
    private final java.lang.String mId;

    private final int mSupportedModes;

    /**
     * Mode bit: This USB port can act as a downstream facing port (host).
     * <p>
     * Implies that the port supports the {@link #POWER_ROLE_SOURCE} and {@link #DATA_ROLE_HOST}
     * combination of roles (and possibly others as well).
     * </p>
     */
    public static final int MODE_DFP = 1 << 0;

    /**
     * Mode bit: This USB port can act as an upstream facing port (device).
     * <p>
     * Implies that the port supports the {@link #POWER_ROLE_SINK} and {@link #DATA_ROLE_DEVICE}
     * combination of roles (and possibly others as well).
     * </p>
     */
    public static final int MODE_UFP = 1 << 1;

    /**
     * Mode bit: This USB port can act either as an downstream facing port (host) or as
     * an upstream facing port (device).
     * <p>
     * Implies that the port supports the {@link #POWER_ROLE_SOURCE} and {@link #DATA_ROLE_HOST}
     * combination of roles and the {@link #POWER_ROLE_SINK} and {@link #DATA_ROLE_DEVICE}
     * combination of roles (and possibly others as well).
     * </p>
     */
    public static final int MODE_DUAL = android.hardware.usb.UsbPort.MODE_DFP | android.hardware.usb.UsbPort.MODE_UFP;

    /**
     * Power role: This USB port can act as a source (provide power).
     */
    public static final int POWER_ROLE_SOURCE = 1;

    /**
     * Power role: This USB port can act as a sink (receive power).
     */
    public static final int POWER_ROLE_SINK = 2;

    /**
     * Data role: This USB port can act as a host (access data services).
     */
    public static final int DATA_ROLE_HOST = 1;

    /**
     * Data role: This USB port can act as a device (offer data services).
     */
    public static final int DATA_ROLE_DEVICE = 2;

    private static final int NUM_DATA_ROLES = 3;

    /**
     *
     *
     * @unknown 
     */
    public UsbPort(java.lang.String id, int supportedModes) {
        mId = id;
        mSupportedModes = supportedModes;
    }

    /**
     * Gets the unique id of the port.
     *
     * @return The unique id of the port; not intended for display.
     */
    public java.lang.String getId() {
        return mId;
    }

    /**
     * Gets the supported modes of the port.
     * <p>
     * The actual mode of the port may vary depending on what is plugged into it.
     * </p>
     *
     * @return The supported modes: one of {@link #MODE_DFP}, {@link #MODE_UFP}, or
    {@link #MODE_DUAL}.
     */
    public int getSupportedModes() {
        return mSupportedModes;
    }

    /**
     * Combines one power and one data role together into a unique value with
     * exactly one bit set.  This can be used to efficiently determine whether
     * a combination of roles is supported by testing whether that bit is present
     * in a bit-field.
     *
     * @param powerRole
     * 		The desired power role: {@link UsbPort#POWER_ROLE_SOURCE}
     * 		or {@link UsbPort#POWER_ROLE_SINK}, or 0 if no power role.
     * @param dataRole
     * 		The desired data role: {@link UsbPort#DATA_ROLE_HOST}
     * 		or {@link UsbPort#DATA_ROLE_DEVICE}, or 0 if no data role.
     * @unknown 
     */
    public static int combineRolesAsBit(int powerRole, int dataRole) {
        android.hardware.usb.UsbPort.checkRoles(powerRole, dataRole);
        final int index = (powerRole * android.hardware.usb.UsbPort.NUM_DATA_ROLES) + dataRole;
        return 1 << index;
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String modeToString(int mode) {
        switch (mode) {
            case 0 :
                return "none";
            case android.hardware.usb.UsbPort.MODE_DFP :
                return "dfp";
            case android.hardware.usb.UsbPort.MODE_UFP :
                return "ufp";
            case android.hardware.usb.UsbPort.MODE_DUAL :
                return "dual";
            default :
                return java.lang.Integer.toString(mode);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String powerRoleToString(int role) {
        switch (role) {
            case 0 :
                return "no-power";
            case android.hardware.usb.UsbPort.POWER_ROLE_SOURCE :
                return "source";
            case android.hardware.usb.UsbPort.POWER_ROLE_SINK :
                return "sink";
            default :
                return java.lang.Integer.toString(role);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String dataRoleToString(int role) {
        switch (role) {
            case 0 :
                return "no-data";
            case android.hardware.usb.UsbPort.DATA_ROLE_HOST :
                return "host";
            case android.hardware.usb.UsbPort.DATA_ROLE_DEVICE :
                return "device";
            default :
                return java.lang.Integer.toString(role);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String roleCombinationsToString(int combo) {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        result.append("[");
        boolean first = true;
        while (combo != 0) {
            final int index = java.lang.Integer.numberOfTrailingZeros(combo);
            combo &= ~(1 << index);
            final int powerRole = index / android.hardware.usb.UsbPort.NUM_DATA_ROLES;
            final int dataRole = index % android.hardware.usb.UsbPort.NUM_DATA_ROLES;
            if (first) {
                first = false;
            } else {
                result.append(", ");
            }
            result.append(android.hardware.usb.UsbPort.powerRoleToString(powerRole));
            result.append(':');
            result.append(android.hardware.usb.UsbPort.dataRoleToString(dataRole));
        } 
        result.append("]");
        return result.toString();
    }

    /**
     *
     *
     * @unknown 
     */
    public static void checkRoles(int powerRole, int dataRole) {
        com.android.internal.util.Preconditions.checkArgumentInRange(powerRole, 0, android.hardware.usb.UsbPort.POWER_ROLE_SINK, "powerRole");
        com.android.internal.util.Preconditions.checkArgumentInRange(dataRole, 0, android.hardware.usb.UsbPort.DATA_ROLE_DEVICE, "dataRole");
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("UsbPort{id=" + mId) + ", supportedModes=") + android.hardware.usb.UsbPort.modeToString(mSupportedModes)) + "}";
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeInt(mSupportedModes);
    }

    public static final android.os.Parcelable.Creator<android.hardware.usb.UsbPort> CREATOR = new android.os.Parcelable.Creator<android.hardware.usb.UsbPort>() {
        @java.lang.Override
        public android.hardware.usb.UsbPort createFromParcel(android.os.Parcel in) {
            java.lang.String id = in.readString();
            int supportedModes = in.readInt();
            return new android.hardware.usb.UsbPort(id, supportedModes);
        }

        @java.lang.Override
        public android.hardware.usb.UsbPort[] newArray(int size) {
            return new android.hardware.usb.UsbPort[size];
        }
    };
}

