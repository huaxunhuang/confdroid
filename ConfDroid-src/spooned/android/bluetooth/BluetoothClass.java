/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * Represents a Bluetooth class, which describes general characteristics
 * and capabilities of a device. For example, a Bluetooth class will
 * specify the general device type such as a phone, a computer, or
 * headset, and whether it's capable of services such as audio or telephony.
 *
 * <p>Every Bluetooth class is composed of zero or more service classes, and
 * exactly one device class. The device class is further broken down into major
 * and minor device class components.
 *
 * <p>{@link BluetoothClass} is useful as a hint to roughly describe a device
 * (for example to show an icon in the UI), but does not reliably describe which
 * Bluetooth profiles or services are actually supported by a device. Accurate
 * service discovery is done through SDP requests, which are automatically
 * performed when creating an RFCOMM socket with {@link BluetoothDevice#createRfcommSocketToServiceRecord} and {@link BluetoothAdapter#listenUsingRfcommWithServiceRecord}</p>
 *
 * <p>Use {@link BluetoothDevice#getBluetoothClass} to retrieve the class for
 * a remote device.
 *
 * <!--
 * The Bluetooth class is a 32 bit field. The format of these bits is defined at
 * http://www.bluetooth.org/Technical/AssignedNumbers/baseband.htm
 * (login required). This class contains that 32 bit field, and provides
 * constants and methods to determine which Service Class(es) and Device Class
 * are encoded in that field.
 * -->
 */
public final class BluetoothClass implements android.os.Parcelable {
    /**
     * Legacy error value. Applications should use null instead.
     *
     * @unknown 
     */
    public static final int ERROR = 0xff000000;

    private final int mClass;

    /**
     *
     *
     * @unknown 
     */
    public BluetoothClass(int classInt) {
        mClass = classInt;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o instanceof android.bluetooth.BluetoothClass) {
            return mClass == ((android.bluetooth.BluetoothClass) (o)).mClass;
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        return mClass;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.Integer.toHexString(mClass);
    }

    public int describeContents() {
        return 0;
    }

    public static final android.os.Parcelable.Creator<android.bluetooth.BluetoothClass> CREATOR = new android.os.Parcelable.Creator<android.bluetooth.BluetoothClass>() {
        public android.bluetooth.BluetoothClass createFromParcel(android.os.Parcel in) {
            return new android.bluetooth.BluetoothClass(in.readInt());
        }

        public android.bluetooth.BluetoothClass[] newArray(int size) {
            return new android.bluetooth.BluetoothClass[size];
        }
    };

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(mClass);
    }

    /**
     * Defines all service class constants.
     * <p>Each {@link BluetoothClass} encodes zero or more service classes.
     */
    public static final class Service {
        private static final int BITMASK = 0xffe000;

        public static final int LIMITED_DISCOVERABILITY = 0x2000;

        public static final int POSITIONING = 0x10000;

        public static final int NETWORKING = 0x20000;

        public static final int RENDER = 0x40000;

        public static final int CAPTURE = 0x80000;

        public static final int OBJECT_TRANSFER = 0x100000;

        public static final int AUDIO = 0x200000;

        public static final int TELEPHONY = 0x400000;

        public static final int INFORMATION = 0x800000;
    }

    /**
     * Return true if the specified service class is supported by this
     * {@link BluetoothClass}.
     * <p>Valid service classes are the public constants in
     * {@link BluetoothClass.Service}. For example, {@link BluetoothClass.Service#AUDIO}.
     *
     * @param service
     * 		valid service class
     * @return true if the service class is supported
     */
    public boolean hasService(int service) {
        return ((mClass & android.bluetooth.BluetoothClass.Service.BITMASK) & service) != 0;
    }

    /**
     * Defines all device class constants.
     * <p>Each {@link BluetoothClass} encodes exactly one device class, with
     * major and minor components.
     * <p>The constants in {@link BluetoothClass.Device} represent a combination of major and minor
     * device components (the complete device class). The constants in {@link BluetoothClass.Device.Major} represent only major device classes.
     * <p>See {@link BluetoothClass.Service} for service class constants.
     */
    public static class Device {
        private static final int BITMASK = 0x1ffc;

        /**
         * Defines all major device class constants.
         * <p>See {@link BluetoothClass.Device} for minor classes.
         */
        public static class Major {
            private static final int BITMASK = 0x1f00;

            public static final int MISC = 0x0;

            public static final int COMPUTER = 0x100;

            public static final int PHONE = 0x200;

            public static final int NETWORKING = 0x300;

            public static final int AUDIO_VIDEO = 0x400;

            public static final int PERIPHERAL = 0x500;

            public static final int IMAGING = 0x600;

            public static final int WEARABLE = 0x700;

            public static final int TOY = 0x800;

            public static final int HEALTH = 0x900;

            public static final int UNCATEGORIZED = 0x1f00;
        }

        // Devices in the COMPUTER major class
        public static final int COMPUTER_UNCATEGORIZED = 0x100;

        public static final int COMPUTER_DESKTOP = 0x104;

        public static final int COMPUTER_SERVER = 0x108;

        public static final int COMPUTER_LAPTOP = 0x10c;

        public static final int COMPUTER_HANDHELD_PC_PDA = 0x110;

        public static final int COMPUTER_PALM_SIZE_PC_PDA = 0x114;

        public static final int COMPUTER_WEARABLE = 0x118;

        // Devices in the PHONE major class
        public static final int PHONE_UNCATEGORIZED = 0x200;

        public static final int PHONE_CELLULAR = 0x204;

        public static final int PHONE_CORDLESS = 0x208;

        public static final int PHONE_SMART = 0x20c;

        public static final int PHONE_MODEM_OR_GATEWAY = 0x210;

        public static final int PHONE_ISDN = 0x214;

        // Minor classes for the AUDIO_VIDEO major class
        public static final int AUDIO_VIDEO_UNCATEGORIZED = 0x400;

        public static final int AUDIO_VIDEO_WEARABLE_HEADSET = 0x404;

        public static final int AUDIO_VIDEO_HANDSFREE = 0x408;

        // public static final int AUDIO_VIDEO_RESERVED              = 0x040C;
        public static final int AUDIO_VIDEO_MICROPHONE = 0x410;

        public static final int AUDIO_VIDEO_LOUDSPEAKER = 0x414;

        public static final int AUDIO_VIDEO_HEADPHONES = 0x418;

        public static final int AUDIO_VIDEO_PORTABLE_AUDIO = 0x41c;

        public static final int AUDIO_VIDEO_CAR_AUDIO = 0x420;

        public static final int AUDIO_VIDEO_SET_TOP_BOX = 0x424;

        public static final int AUDIO_VIDEO_HIFI_AUDIO = 0x428;

        public static final int AUDIO_VIDEO_VCR = 0x42c;

        public static final int AUDIO_VIDEO_VIDEO_CAMERA = 0x430;

        public static final int AUDIO_VIDEO_CAMCORDER = 0x434;

        public static final int AUDIO_VIDEO_VIDEO_MONITOR = 0x438;

        public static final int AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER = 0x43c;

        public static final int AUDIO_VIDEO_VIDEO_CONFERENCING = 0x440;

        // public static final int AUDIO_VIDEO_RESERVED              = 0x0444;
        public static final int AUDIO_VIDEO_VIDEO_GAMING_TOY = 0x448;

        // Devices in the WEARABLE major class
        public static final int WEARABLE_UNCATEGORIZED = 0x700;

        public static final int WEARABLE_WRIST_WATCH = 0x704;

        public static final int WEARABLE_PAGER = 0x708;

        public static final int WEARABLE_JACKET = 0x70c;

        public static final int WEARABLE_HELMET = 0x710;

        public static final int WEARABLE_GLASSES = 0x714;

        // Devices in the TOY major class
        public static final int TOY_UNCATEGORIZED = 0x800;

        public static final int TOY_ROBOT = 0x804;

        public static final int TOY_VEHICLE = 0x808;

        public static final int TOY_DOLL_ACTION_FIGURE = 0x80c;

        public static final int TOY_CONTROLLER = 0x810;

        public static final int TOY_GAME = 0x814;

        // Devices in the HEALTH major class
        public static final int HEALTH_UNCATEGORIZED = 0x900;

        public static final int HEALTH_BLOOD_PRESSURE = 0x904;

        public static final int HEALTH_THERMOMETER = 0x908;

        public static final int HEALTH_WEIGHING = 0x90c;

        public static final int HEALTH_GLUCOSE = 0x910;

        public static final int HEALTH_PULSE_OXIMETER = 0x914;

        public static final int HEALTH_PULSE_RATE = 0x918;

        public static final int HEALTH_DATA_DISPLAY = 0x91c;

        // Devices in PERIPHERAL major class
        /**
         *
         *
         * @unknown 
         */
        public static final int PERIPHERAL_NON_KEYBOARD_NON_POINTING = 0x500;

        /**
         *
         *
         * @unknown 
         */
        public static final int PERIPHERAL_KEYBOARD = 0x540;

        /**
         *
         *
         * @unknown 
         */
        public static final int PERIPHERAL_POINTING = 0x580;

        /**
         *
         *
         * @unknown 
         */
        public static final int PERIPHERAL_KEYBOARD_POINTING = 0x5c0;
    }

    /**
     * Return the major device class component of this {@link BluetoothClass}.
     * <p>Values returned from this function can be compared with the
     * public constants in {@link BluetoothClass.Device.Major} to determine
     * which major class is encoded in this Bluetooth class.
     *
     * @return major device class component
     */
    public int getMajorDeviceClass() {
        return mClass & android.bluetooth.BluetoothClass.Device.Major.BITMASK;
    }

    /**
     * Return the (major and minor) device class component of this
     * {@link BluetoothClass}.
     * <p>Values returned from this function can be compared with the
     * public constants in {@link BluetoothClass.Device} to determine which
     * device class is encoded in this Bluetooth class.
     *
     * @return device class component
     */
    public int getDeviceClass() {
        return mClass & android.bluetooth.BluetoothClass.Device.BITMASK;
    }

    /**
     *
     *
     * @unknown 
     */
    public static final int PROFILE_HEADSET = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROFILE_A2DP = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROFILE_OPP = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROFILE_HID = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROFILE_PANU = 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROFILE_NAP = 5;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROFILE_A2DP_SINK = 6;

    /**
     * Check class bits for possible bluetooth profile support.
     * This is a simple heuristic that tries to guess if a device with the
     * given class bits might support specified profile. It is not accurate for all
     * devices. It tries to err on the side of false positives.
     *
     * @param profile
     * 		The profile to be checked
     * @return True if this device might support specified profile.
     * @unknown 
     */
    public boolean doesClassMatch(int profile) {
        if (profile == android.bluetooth.BluetoothClass.PROFILE_A2DP) {
            if (hasService(android.bluetooth.BluetoothClass.Service.RENDER)) {
                return true;
            }
            // By the A2DP spec, sinks must indicate the RENDER service.
            // However we found some that do not (Chordette). So lets also
            // match on some other class bits.
            switch (getDeviceClass()) {
                case android.bluetooth.BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO :
                case android.bluetooth.BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES :
                case android.bluetooth.BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER :
                case android.bluetooth.BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO :
                    return true;
                default :
                    return false;
            }
        } else
            if (profile == android.bluetooth.BluetoothClass.PROFILE_A2DP_SINK) {
                if (hasService(android.bluetooth.BluetoothClass.Service.CAPTURE)) {
                    return true;
                }
                // By the A2DP spec, srcs must indicate the CAPTURE service.
                // However if some device that do not, we try to
                // match on some other class bits.
                switch (getDeviceClass()) {
                    case android.bluetooth.BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO :
                    case android.bluetooth.BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX :
                    case android.bluetooth.BluetoothClass.Device.AUDIO_VIDEO_VCR :
                        return true;
                    default :
                        return false;
                }
            } else
                if (profile == android.bluetooth.BluetoothClass.PROFILE_HEADSET) {
                    // The render service class is required by the spec for HFP, so is a
                    // pretty good signal
                    if (hasService(android.bluetooth.BluetoothClass.Service.RENDER)) {
                        return true;
                    }
                    // Just in case they forgot the render service class
                    switch (getDeviceClass()) {
                        case android.bluetooth.BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE :
                        case android.bluetooth.BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET :
                        case android.bluetooth.BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO :
                            return true;
                        default :
                            return false;
                    }
                } else
                    if (profile == android.bluetooth.BluetoothClass.PROFILE_OPP) {
                        if (hasService(android.bluetooth.BluetoothClass.Service.OBJECT_TRANSFER)) {
                            return true;
                        }
                        switch (getDeviceClass()) {
                            case android.bluetooth.BluetoothClass.Device.COMPUTER_UNCATEGORIZED :
                            case android.bluetooth.BluetoothClass.Device.COMPUTER_DESKTOP :
                            case android.bluetooth.BluetoothClass.Device.COMPUTER_SERVER :
                            case android.bluetooth.BluetoothClass.Device.COMPUTER_LAPTOP :
                            case android.bluetooth.BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA :
                            case android.bluetooth.BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA :
                            case android.bluetooth.BluetoothClass.Device.COMPUTER_WEARABLE :
                            case android.bluetooth.BluetoothClass.Device.PHONE_UNCATEGORIZED :
                            case android.bluetooth.BluetoothClass.Device.PHONE_CELLULAR :
                            case android.bluetooth.BluetoothClass.Device.PHONE_CORDLESS :
                            case android.bluetooth.BluetoothClass.Device.PHONE_SMART :
                            case android.bluetooth.BluetoothClass.Device.PHONE_MODEM_OR_GATEWAY :
                            case android.bluetooth.BluetoothClass.Device.PHONE_ISDN :
                                return true;
                            default :
                                return false;
                        }
                    } else
                        if (profile == android.bluetooth.BluetoothClass.PROFILE_HID) {
                            return (getDeviceClass() & android.bluetooth.BluetoothClass.Device.Major.PERIPHERAL) == android.bluetooth.BluetoothClass.Device.Major.PERIPHERAL;
                        } else
                            if ((profile == android.bluetooth.BluetoothClass.PROFILE_PANU) || (profile == android.bluetooth.BluetoothClass.PROFILE_NAP)) {
                                // No good way to distinguish between the two, based on class bits.
                                if (hasService(android.bluetooth.BluetoothClass.Service.NETWORKING)) {
                                    return true;
                                }
                                return (getDeviceClass() & android.bluetooth.BluetoothClass.Device.Major.NETWORKING) == android.bluetooth.BluetoothClass.Device.Major.NETWORKING;
                            } else {
                                return false;
                            }





    }
}

