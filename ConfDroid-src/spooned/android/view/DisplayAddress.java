/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.view;


/**
 * Display identifier that is stable across reboots.
 *
 * @unknown 
 */
public abstract class DisplayAddress implements android.os.Parcelable {
    /**
     * Creates an address for a physical display given its stable ID.
     *
     * A physical display ID is stable if the display can be identified using EDID information.
     *
     * @param physicalDisplayId
     * 		A physical display ID.
     * @return The {@link Physical} address.
     * @see SurfaceControl#getPhysicalDisplayIds
     */
    @android.annotation.NonNull
    public static android.view.DisplayAddress.Physical fromPhysicalDisplayId(long physicalDisplayId) {
        return new android.view.DisplayAddress.Physical(physicalDisplayId);
    }

    /**
     * Creates an address for a network display given its MAC address.
     *
     * @param macAddress
     * 		A MAC address in colon notation.
     * @return The {@link Network} address.
     */
    @android.annotation.NonNull
    public static android.view.DisplayAddress.Network fromMacAddress(java.lang.String macAddress) {
        return new android.view.DisplayAddress.Network(macAddress);
    }

    /**
     * Address for a physically connected display.
     *
     * A {@link Physical} address is represented by a 64-bit identifier combining the port and model
     * of a display. The port, located in the least significant byte, uniquely identifies a physical
     * connector on the device for display output like eDP or HDMI. The model, located in the upper
     * bits, uniquely identifies a display model across manufacturers by encoding EDID information.
     * While the port is always stable, the model may not be available if EDID identification is not
     * supported by the platform, in which case the address is not unique.
     */
    public static final class Physical extends android.view.DisplayAddress {
        private static final long UNKNOWN_MODEL = 0;

        private static final int MODEL_SHIFT = 8;

        private static final int PORT_MASK = 0xff;

        private final long mPhysicalDisplayId;

        /**
         * Physical port to which the display is connected.
         */
        public byte getPort() {
            return ((byte) (mPhysicalDisplayId));
        }

        /**
         * Model identifier unique across manufacturers.
         *
         * @return The model ID, or {@code null} if the model cannot be identified.
         */
        @android.annotation.Nullable
        public java.lang.Long getModel() {
            final long model = mPhysicalDisplayId >>> android.view.DisplayAddress.Physical.MODEL_SHIFT;
            return model == android.view.DisplayAddress.Physical.UNKNOWN_MODEL ? null : model;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object other) {
            return (other instanceof android.view.DisplayAddress.Physical) && (mPhysicalDisplayId == ((android.view.DisplayAddress.Physical) (other)).mPhysicalDisplayId);
        }

        @java.lang.Override
        public java.lang.String toString() {
            final java.lang.StringBuilder builder = new java.lang.StringBuilder("{").append("port=").append(getPort() & android.view.DisplayAddress.Physical.PORT_MASK);
            final java.lang.Long model = getModel();
            if (model != null) {
                builder.append(", model=0x").append(java.lang.Long.toHexString(model));
            }
            return builder.append("}").toString();
        }

        @java.lang.Override
        public int hashCode() {
            return java.lang.Long.hashCode(mPhysicalDisplayId);
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            out.writeLong(mPhysicalDisplayId);
        }

        private Physical(long physicalDisplayId) {
            mPhysicalDisplayId = physicalDisplayId;
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.view.DisplayAddress.Physical> CREATOR = new android.os.Parcelable.Creator<android.view.DisplayAddress.Physical>() {
            @java.lang.Override
            public android.view.Physical createFromParcel(android.os.Parcel in) {
                return new android.view.Physical(in.readLong());
            }

            @java.lang.Override
            public android.view.Physical[] newArray(int size) {
                return new android.view.Physical[size];
            }
        };
    }

    /**
     * Address for a network-connected display.
     */
    public static final class Network extends android.view.DisplayAddress {
        private final java.lang.String mMacAddress;

        @java.lang.Override
        public boolean equals(java.lang.Object other) {
            return (other instanceof android.view.DisplayAddress.Network) && mMacAddress.equals(((android.view.DisplayAddress.Network) (other)).mMacAddress);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return mMacAddress;
        }

        @java.lang.Override
        public int hashCode() {
            return mMacAddress.hashCode();
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            out.writeString(mMacAddress);
        }

        private Network(java.lang.String macAddress) {
            mMacAddress = macAddress;
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.view.DisplayAddress.Network> CREATOR = new android.os.Parcelable.Creator<android.view.DisplayAddress.Network>() {
            @java.lang.Override
            public android.view.Network createFromParcel(android.os.Parcel in) {
                return new android.view.Network(in.readString());
            }

            @java.lang.Override
            public android.view.Network[] newArray(int size) {
                return new android.view.Network[size];
            }
        };
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }
}

