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
package android.media;


/**
 * Class to provide information about the audio devices.
 */
public final class AudioDeviceInfo {
    /**
     * A device type associated with an unknown or uninitialized device.
     */
    public static final int TYPE_UNKNOWN = 0;

    /**
     * A device type describing the attached earphone speaker.
     */
    public static final int TYPE_BUILTIN_EARPIECE = 1;

    /**
     * A device type describing the speaker system (i.e. a mono speaker or stereo speakers) built
     * in a device.
     */
    public static final int TYPE_BUILTIN_SPEAKER = 2;

    /**
     * A device type describing a headset, which is the combination of a headphones and microphone.
     */
    public static final int TYPE_WIRED_HEADSET = 3;

    /**
     * A device type describing a pair of wired headphones.
     */
    public static final int TYPE_WIRED_HEADPHONES = 4;

    /**
     * A device type describing an analog line-level connection.
     */
    public static final int TYPE_LINE_ANALOG = 5;

    /**
     * A device type describing a digital line connection (e.g. SPDIF).
     */
    public static final int TYPE_LINE_DIGITAL = 6;

    /**
     * A device type describing a Bluetooth device typically used for telephony.
     */
    public static final int TYPE_BLUETOOTH_SCO = 7;

    /**
     * A device type describing a Bluetooth device supporting the A2DP profile.
     */
    public static final int TYPE_BLUETOOTH_A2DP = 8;

    /**
     * A device type describing an HDMI connection .
     */
    public static final int TYPE_HDMI = 9;

    /**
     * A device type describing the Audio Return Channel of an HDMI connection.
     */
    public static final int TYPE_HDMI_ARC = 10;

    /**
     * A device type describing a USB audio device.
     */
    public static final int TYPE_USB_DEVICE = 11;

    /**
     * A device type describing a USB audio device in accessory mode.
     */
    public static final int TYPE_USB_ACCESSORY = 12;

    /**
     * A device type describing the audio device associated with a dock.
     */
    public static final int TYPE_DOCK = 13;

    /**
     * A device type associated with the transmission of audio signals over FM.
     */
    public static final int TYPE_FM = 14;

    /**
     * A device type describing the microphone(s) built in a device.
     */
    public static final int TYPE_BUILTIN_MIC = 15;

    /**
     * A device type for accessing the audio content transmitted over FM.
     */
    public static final int TYPE_FM_TUNER = 16;

    /**
     * A device type for accessing the audio content transmitted over the TV tuner system.
     */
    public static final int TYPE_TV_TUNER = 17;

    /**
     * A device type describing the transmission of audio signals over the telephony network.
     */
    public static final int TYPE_TELEPHONY = 18;

    /**
     * A device type describing the auxiliary line-level connectors.
     */
    public static final int TYPE_AUX_LINE = 19;

    /**
     * A device type connected over IP.
     */
    public static final int TYPE_IP = 20;

    /**
     * A type-agnostic device used for communication with external audio systems
     */
    public static final int TYPE_BUS = 21;

    private final android.media.AudioDevicePort mPort;

    AudioDeviceInfo(android.media.AudioDevicePort port) {
        mPort = port;
    }

    /**
     *
     *
     * @return The internal device ID.
     */
    public int getId() {
        return mPort.handle().id();
    }

    /**
     *
     *
     * @return The human-readable name of the audio device.
     */
    public java.lang.CharSequence getProductName() {
        java.lang.String portName = mPort.name();
        return portName.length() != 0 ? portName : android.os.Build.MODEL;
    }

    /**
     *
     *
     * @unknown 
     * @return The "address" string of the device. This generally contains device-specific
    parameters.
     */
    public java.lang.String getAddress() {
        return mPort.address();
    }

    /**
     *
     *
     * @return true if the audio device is a source for audio data (e.e an input).
     */
    public boolean isSource() {
        return mPort.role() == android.media.AudioPort.ROLE_SOURCE;
    }

    /**
     *
     *
     * @return true if the audio device is a sink for audio data (i.e. an output).
     */
    public boolean isSink() {
        return mPort.role() == android.media.AudioPort.ROLE_SINK;
    }

    /**
     *
     *
     * @return An array of sample rates supported by the audio device.

    Note: an empty array indicates that the device supports arbitrary rates.
     */
    @android.annotation.NonNull
    public int[] getSampleRates() {
        return mPort.samplingRates();
    }

    /**
     *
     *
     * @return An array of channel position masks (e.g. {@link AudioFormat#CHANNEL_IN_STEREO},
    {@link AudioFormat#CHANNEL_OUT_7POINT1}) for which this audio device can be configured.
     * @see AudioFormat

    Note: an empty array indicates that the device supports arbitrary channel masks.
     */
    @android.annotation.NonNull
    public int[] getChannelMasks() {
        return mPort.channelMasks();
    }

    /**
     *
     *
     * @return An array of channel index masks for which this audio device can be configured.
     * @see AudioFormat

    Note: an empty array indicates that the device supports arbitrary channel index masks.
     */
    @android.annotation.NonNull
    public int[] getChannelIndexMasks() {
        return mPort.channelIndexMasks();
    }

    /**
     *
     *
     * @return An array of channel counts (1, 2, 4, ...) for which this audio device
    can be configured.

    Note: an empty array indicates that the device supports arbitrary channel counts.
     */
    @android.annotation.NonNull
    public int[] getChannelCounts() {
        java.util.TreeSet<java.lang.Integer> countSet = new java.util.TreeSet<java.lang.Integer>();
        // Channel Masks
        for (int mask : getChannelMasks()) {
            countSet.add(isSink() ? android.media.AudioFormat.channelCountFromOutChannelMask(mask) : android.media.AudioFormat.channelCountFromInChannelMask(mask));
        }
        // Index Masks
        for (int index_mask : getChannelIndexMasks()) {
            countSet.add(java.lang.Integer.bitCount(index_mask));
        }
        int[] counts = new int[countSet.size()];
        int index = 0;
        for (int count : countSet) {
            counts[index++] = count;
        }
        return counts;
    }

    /**
     *
     *
     * @return An array of audio encodings (e.g. {@link AudioFormat#ENCODING_PCM_16BIT},
    {@link AudioFormat#ENCODING_PCM_FLOAT}) supported by the audio device.
    <code>ENCODING_PCM_FLOAT</code> indicates the device supports more
    than 16 bits of integer precision.  As there is no AudioFormat constant
    specifically defined for 24-bit PCM, the value <code>ENCODING_PCM_FLOAT</code>
    indicates that {@link AudioTrack} or {@link AudioRecord} can preserve at least 24 bits of
    integer precision to that device.
     * @see AudioFormat

    Note: an empty array indicates that the device supports arbitrary encodings.
     */
    @android.annotation.NonNull
    public int[] getEncodings() {
        return android.media.AudioFormat.filterPublicFormats(mPort.formats());
    }

    /**
     *
     *
     * @return The device type identifier of the audio device (i.e. TYPE_BUILTIN_SPEAKER).
     */
    public int getType() {
        return android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.get(mPort.type(), android.media.AudioDeviceInfo.TYPE_UNKNOWN);
    }

    /**
     *
     *
     * @unknown 
     */
    public static int convertDeviceTypeToInternalDevice(int deviceType) {
        return android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.get(deviceType, android.media.AudioSystem.DEVICE_NONE);
    }

    /**
     *
     *
     * @unknown 
     */
    public static int convertInternalDeviceToDeviceType(int intDevice) {
        return android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.get(intDevice, android.media.AudioDeviceInfo.TYPE_UNKNOWN);
    }

    private static final android.util.SparseIntArray INT_TO_EXT_DEVICE_MAPPING;

    private static final android.util.SparseIntArray EXT_TO_INT_DEVICE_MAPPING;

    static {
        INT_TO_EXT_DEVICE_MAPPING = new android.util.SparseIntArray();
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_EARPIECE, android.media.AudioDeviceInfo.TYPE_BUILTIN_EARPIECE);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_SPEAKER, android.media.AudioDeviceInfo.TYPE_BUILTIN_SPEAKER);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_WIRED_HEADSET, android.media.AudioDeviceInfo.TYPE_WIRED_HEADSET);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_WIRED_HEADPHONE, android.media.AudioDeviceInfo.TYPE_WIRED_HEADPHONES);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_SCO, android.media.AudioDeviceInfo.TYPE_BLUETOOTH_SCO);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_SCO_HEADSET, android.media.AudioDeviceInfo.TYPE_BLUETOOTH_SCO);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_SCO_CARKIT, android.media.AudioDeviceInfo.TYPE_BLUETOOTH_SCO);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_A2DP, android.media.AudioDeviceInfo.TYPE_BLUETOOTH_A2DP);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES, android.media.AudioDeviceInfo.TYPE_BLUETOOTH_A2DP);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER, android.media.AudioDeviceInfo.TYPE_BLUETOOTH_A2DP);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_HDMI, android.media.AudioDeviceInfo.TYPE_HDMI);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_ANLG_DOCK_HEADSET, android.media.AudioDeviceInfo.TYPE_DOCK);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_DGTL_DOCK_HEADSET, android.media.AudioDeviceInfo.TYPE_DOCK);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_USB_ACCESSORY, android.media.AudioDeviceInfo.TYPE_USB_ACCESSORY);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_USB_DEVICE, android.media.AudioDeviceInfo.TYPE_USB_DEVICE);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_TELEPHONY_TX, android.media.AudioDeviceInfo.TYPE_TELEPHONY);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_LINE, android.media.AudioDeviceInfo.TYPE_LINE_ANALOG);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_HDMI_ARC, android.media.AudioDeviceInfo.TYPE_HDMI_ARC);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_SPDIF, android.media.AudioDeviceInfo.TYPE_LINE_DIGITAL);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_FM, android.media.AudioDeviceInfo.TYPE_FM);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_AUX_LINE, android.media.AudioDeviceInfo.TYPE_AUX_LINE);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_IP, android.media.AudioDeviceInfo.TYPE_IP);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_OUT_BUS, android.media.AudioDeviceInfo.TYPE_BUS);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_BUILTIN_MIC, android.media.AudioDeviceInfo.TYPE_BUILTIN_MIC);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_BLUETOOTH_SCO_HEADSET, android.media.AudioDeviceInfo.TYPE_BLUETOOTH_SCO);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_WIRED_HEADSET, android.media.AudioDeviceInfo.TYPE_WIRED_HEADSET);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_HDMI, android.media.AudioDeviceInfo.TYPE_HDMI);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_TELEPHONY_RX, android.media.AudioDeviceInfo.TYPE_TELEPHONY);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_BACK_MIC, android.media.AudioDeviceInfo.TYPE_BUILTIN_MIC);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_ANLG_DOCK_HEADSET, android.media.AudioDeviceInfo.TYPE_DOCK);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_DGTL_DOCK_HEADSET, android.media.AudioDeviceInfo.TYPE_DOCK);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_USB_ACCESSORY, android.media.AudioDeviceInfo.TYPE_USB_ACCESSORY);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_USB_DEVICE, android.media.AudioDeviceInfo.TYPE_USB_DEVICE);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_FM_TUNER, android.media.AudioDeviceInfo.TYPE_FM_TUNER);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_TV_TUNER, android.media.AudioDeviceInfo.TYPE_TV_TUNER);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_LINE, android.media.AudioDeviceInfo.TYPE_LINE_ANALOG);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_SPDIF, android.media.AudioDeviceInfo.TYPE_LINE_DIGITAL);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_BLUETOOTH_A2DP, android.media.AudioDeviceInfo.TYPE_BLUETOOTH_A2DP);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_IP, android.media.AudioDeviceInfo.TYPE_IP);
        android.media.AudioDeviceInfo.INT_TO_EXT_DEVICE_MAPPING.put(android.media.AudioSystem.DEVICE_IN_BUS, android.media.AudioDeviceInfo.TYPE_BUS);
        // not covered here, legacy
        // AudioSystem.DEVICE_OUT_REMOTE_SUBMIX
        // AudioSystem.DEVICE_IN_REMOTE_SUBMIX
        // privileges mapping to output device
        EXT_TO_INT_DEVICE_MAPPING = new android.util.SparseIntArray();
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_BUILTIN_EARPIECE, android.media.AudioSystem.DEVICE_OUT_EARPIECE);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_BUILTIN_SPEAKER, android.media.AudioSystem.DEVICE_OUT_SPEAKER);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_WIRED_HEADSET, android.media.AudioSystem.DEVICE_OUT_WIRED_HEADSET);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_WIRED_HEADPHONES, android.media.AudioSystem.DEVICE_OUT_WIRED_HEADPHONE);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_LINE_ANALOG, android.media.AudioSystem.DEVICE_OUT_LINE);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_LINE_DIGITAL, android.media.AudioSystem.DEVICE_OUT_SPDIF);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_BLUETOOTH_SCO, android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_SCO);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_BLUETOOTH_A2DP, android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_A2DP);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_HDMI, android.media.AudioSystem.DEVICE_OUT_HDMI);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_HDMI_ARC, android.media.AudioSystem.DEVICE_OUT_HDMI_ARC);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_USB_DEVICE, android.media.AudioSystem.DEVICE_OUT_USB_DEVICE);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_USB_ACCESSORY, android.media.AudioSystem.DEVICE_OUT_USB_ACCESSORY);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_DOCK, android.media.AudioSystem.DEVICE_OUT_ANLG_DOCK_HEADSET);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_FM, android.media.AudioSystem.DEVICE_OUT_FM);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_BUILTIN_MIC, android.media.AudioSystem.DEVICE_IN_BUILTIN_MIC);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_FM_TUNER, android.media.AudioSystem.DEVICE_IN_FM_TUNER);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_TV_TUNER, android.media.AudioSystem.DEVICE_IN_TV_TUNER);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_TELEPHONY, android.media.AudioSystem.DEVICE_OUT_TELEPHONY_TX);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_AUX_LINE, android.media.AudioSystem.DEVICE_OUT_AUX_LINE);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_IP, android.media.AudioSystem.DEVICE_OUT_IP);
        android.media.AudioDeviceInfo.EXT_TO_INT_DEVICE_MAPPING.put(android.media.AudioDeviceInfo.TYPE_BUS, android.media.AudioSystem.DEVICE_OUT_BUS);
    }
}

