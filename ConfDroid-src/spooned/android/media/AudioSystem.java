/**
 * Copyright (C) 2006 The Android Open Source Project
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


/* IF YOU CHANGE ANY OF THE CONSTANTS IN THIS FILE, DO NOT FORGET
TO UPDATE THE CORRESPONDING NATIVE GLUE AND AudioManager.java.
THANK YOU FOR YOUR COOPERATION.
 */
/**
 *
 *
 * @unknown 
 */
public class AudioSystem {
    private static final java.lang.String TAG = "AudioSystem";

    /* These values must be kept in sync with system/audio.h */
    /* If these are modified, please also update Settings.System.VOLUME_SETTINGS
    and attrs.xml and AudioManager.java.
     */
    /* The default audio stream */
    public static final int STREAM_DEFAULT = -1;

    /* The audio stream for phone calls */
    public static final int STREAM_VOICE_CALL = 0;

    /* The audio stream for system sounds */
    public static final int STREAM_SYSTEM = 1;

    /* The audio stream for the phone ring and message alerts */
    public static final int STREAM_RING = 2;

    /* The audio stream for music playback */
    public static final int STREAM_MUSIC = 3;

    /* The audio stream for alarms */
    public static final int STREAM_ALARM = 4;

    /* The audio stream for notifications */
    public static final int STREAM_NOTIFICATION = 5;

    /* @hide The audio stream for phone calls when connected on bluetooth */
    public static final int STREAM_BLUETOOTH_SCO = 6;

    /* @hide The audio stream for enforced system sounds in certain countries (e.g camera in Japan) */
    public static final int STREAM_SYSTEM_ENFORCED = 7;

    /* @hide The audio stream for DTMF tones */
    public static final int STREAM_DTMF = 8;

    /* @hide The audio stream for text to speech (TTS) */
    public static final int STREAM_TTS = 9;

    /**
     *
     *
     * @deprecated Use {@link #numStreamTypes() instead}
     */
    public static final int NUM_STREAMS = 5;

    // Expose only the getter method publicly so we can change it in the future
    private static final int NUM_STREAM_TYPES = 10;

    public static final int getNumStreamTypes() {
        return android.media.AudioSystem.NUM_STREAM_TYPES;
    }

    public static final java.lang.String[] STREAM_NAMES = new java.lang.String[]{ "STREAM_VOICE_CALL", "STREAM_SYSTEM", "STREAM_RING", "STREAM_MUSIC", "STREAM_ALARM", "STREAM_NOTIFICATION", "STREAM_BLUETOOTH_SCO", "STREAM_SYSTEM_ENFORCED", "STREAM_DTMF", "STREAM_TTS" };

    /* Sets the microphone mute on or off.

    @param on set <var>true</var> to mute the microphone;
              <var>false</var> to turn mute off
    @return command completion status see AUDIO_STATUS_OK, see AUDIO_STATUS_ERROR
     */
    public static native int muteMicrophone(boolean on);

    /* Checks whether the microphone mute is on or off.

    @return true if microphone is muted, false if it's not
     */
    public static native boolean isMicrophoneMuted();

    /* modes for setPhoneState, must match AudioSystem.h audio_mode */
    public static final int MODE_INVALID = -2;

    public static final int MODE_CURRENT = -1;

    public static final int MODE_NORMAL = 0;

    public static final int MODE_RINGTONE = 1;

    public static final int MODE_IN_CALL = 2;

    public static final int MODE_IN_COMMUNICATION = 3;

    public static final int NUM_MODES = 4;

    /* Routing bits for the former setRouting/getRouting API */
    /**
     *
     *
     * @deprecated 
     */
    @java.lang.Deprecated
    public static final int ROUTE_EARPIECE = 1 << 0;

    /**
     *
     *
     * @deprecated 
     */
    @java.lang.Deprecated
    public static final int ROUTE_SPEAKER = 1 << 1;

    /**
     *
     *
     * @deprecated use {@link #ROUTE_BLUETOOTH_SCO}
     */
    @java.lang.Deprecated
    public static final int ROUTE_BLUETOOTH = 1 << 2;

    /**
     *
     *
     * @deprecated 
     */
    @java.lang.Deprecated
    public static final int ROUTE_BLUETOOTH_SCO = 1 << 2;

    /**
     *
     *
     * @deprecated 
     */
    @java.lang.Deprecated
    public static final int ROUTE_HEADSET = 1 << 3;

    /**
     *
     *
     * @deprecated 
     */
    @java.lang.Deprecated
    public static final int ROUTE_BLUETOOTH_A2DP = 1 << 4;

    /**
     *
     *
     * @deprecated 
     */
    @java.lang.Deprecated
    public static final int ROUTE_ALL = 0xffffffff;

    // Keep in sync with system/media/audio/include/system/audio.h
    public static final int AUDIO_SESSION_ALLOCATE = 0;

    /* Checks whether the specified stream type is active.

    return true if any track playing on this stream is active.
     */
    public static native boolean isStreamActive(int stream, int inPastMs);

    /* Checks whether the specified stream type is active on a remotely connected device. The notion
    of what constitutes a remote device is enforced by the audio policy manager of the platform.

    return true if any track playing on this stream is active on a remote device.
     */
    public static native boolean isStreamActiveRemotely(int stream, int inPastMs);

    /* Checks whether the specified audio source is active.

    return true if any recorder using this source is currently recording
     */
    public static native boolean isSourceActive(int source);

    /* Returns a new unused audio session ID */
    public static native int newAudioSessionId();

    /* Sets a group generic audio configuration parameters. The use of these parameters
    are platform dependent, see libaudio

    param keyValuePairs  list of parameters key value pairs in the form:
       key1=value1;key2=value2;...
     */
    public static native int setParameters(java.lang.String keyValuePairs);

    /* Gets a group generic audio configuration parameters. The use of these parameters
    are platform dependent, see libaudio

    param keys  list of parameters
    return value: list of parameters key value pairs in the form:
       key1=value1;key2=value2;...
     */
    public static native java.lang.String getParameters(java.lang.String keys);

    // These match the enum AudioError in frameworks/base/core/jni/android_media_AudioSystem.cpp
    /* Command sucessful or Media server restarted. see ErrorCallback */
    public static final int AUDIO_STATUS_OK = 0;

    /* Command failed or unspecified audio error.  see ErrorCallback */
    public static final int AUDIO_STATUS_ERROR = 1;

    /* Media server died. see ErrorCallback */
    public static final int AUDIO_STATUS_SERVER_DIED = 100;

    private static android.media.AudioSystem.ErrorCallback mErrorCallback;

    /* Handles the audio error callback. */
    public interface ErrorCallback {
        /* Callback for audio server errors.
        param error   error code:
        - AUDIO_STATUS_OK
        - AUDIO_STATUS_SERVER_DIED
        - AUDIO_STATUS_ERROR
         */
        void onError(int error);
    }

    /* Registers a callback to be invoked when an error occurs.
    @param cb the callback to run
     */
    public static void setErrorCallback(android.media.AudioSystem.ErrorCallback cb) {
        synchronized(android.media.AudioSystem.class) {
            android.media.AudioSystem.mErrorCallback = cb;
            if (cb != null) {
                cb.onError(android.media.AudioSystem.checkAudioFlinger());
            }
        }
    }

    private static void errorCallbackFromNative(int error) {
        android.media.AudioSystem.ErrorCallback errorCallback = null;
        synchronized(android.media.AudioSystem.class) {
            if (android.media.AudioSystem.mErrorCallback != null) {
                errorCallback = android.media.AudioSystem.mErrorCallback;
            }
        }
        if (errorCallback != null) {
            errorCallback.onError(error);
        }
    }

    /**
     * Handles events from the audio policy manager about dynamic audio policies
     *
     * @see android.media.audiopolicy.AudioPolicy
     */
    public interface DynamicPolicyCallback {
        void onDynamicPolicyMixStateUpdate(java.lang.String regId, int state);
    }

    // keep in sync with include/media/AudioPolicy.h
    private static final int DYNAMIC_POLICY_EVENT_MIX_STATE_UPDATE = 0;

    private static android.media.AudioSystem.DynamicPolicyCallback sDynPolicyCallback;

    public static void setDynamicPolicyCallback(android.media.AudioSystem.DynamicPolicyCallback cb) {
        synchronized(android.media.AudioSystem.class) {
            android.media.AudioSystem.sDynPolicyCallback = cb;
            android.media.AudioSystem.native_register_dynamic_policy_callback();
        }
    }

    private static void dynamicPolicyCallbackFromNative(int event, java.lang.String regId, int val) {
        android.media.AudioSystem.DynamicPolicyCallback cb = null;
        synchronized(android.media.AudioSystem.class) {
            if (android.media.AudioSystem.sDynPolicyCallback != null) {
                cb = android.media.AudioSystem.sDynPolicyCallback;
            }
        }
        if (cb != null) {
            switch (event) {
                case android.media.AudioSystem.DYNAMIC_POLICY_EVENT_MIX_STATE_UPDATE :
                    cb.onDynamicPolicyMixStateUpdate(regId, val);
                    break;
                default :
                    android.util.Log.e(android.media.AudioSystem.TAG, "dynamicPolicyCallbackFromNative: unknown event " + event);
            }
        }
    }

    /**
     * Handles events from the audio policy manager about recording events
     *
     * @see android.media.AudioManager.AudioRecordingCallback
     */
    public interface AudioRecordingCallback {
        /**
         * Callback for recording activity notifications events
         *
         * @param event
         * 		
         * @param session
         * 		
         * @param source
         * 		
         * @param recordingFormat
         * 		an array of ints containing respectively the client and device
         * 		recording configurations (2*3 ints), followed by the patch handle:
         * 		index 0: client format
         * 		1: client channel mask
         * 		2: client sample rate
         * 		3: device format
         * 		4: device channel mask
         * 		5: device sample rate
         * 		6: patch handle
         */
        void onRecordingConfigurationChanged(int event, int session, int source, int[] recordingFormat);
    }

    private static android.media.AudioSystem.AudioRecordingCallback sRecordingCallback;

    public static void setRecordingCallback(android.media.AudioSystem.AudioRecordingCallback cb) {
        synchronized(android.media.AudioSystem.class) {
            android.media.AudioSystem.sRecordingCallback = cb;
            android.media.AudioSystem.native_register_recording_callback();
        }
    }

    /**
     * Callback from native for recording configuration updates.
     *
     * @param event
     * 		
     * @param session
     * 		
     * @param source
     * 		
     * @param recordingFormat
     * 		see
     * 		{@link AudioRecordingCallback#onRecordingConfigurationChanged(int, int, int, int[])} for
     * 		the description of the record format.
     */
    private static void recordingCallbackFromNative(int event, int session, int source, int[] recordingFormat) {
        android.media.AudioSystem.AudioRecordingCallback cb = null;
        synchronized(android.media.AudioSystem.class) {
            cb = android.media.AudioSystem.sRecordingCallback;
        }
        if (cb != null) {
            cb.onRecordingConfigurationChanged(event, session, source, recordingFormat);
        }
    }

    /* Error codes used by public APIs (AudioTrack, AudioRecord, AudioManager ...)
    Must be kept in sync with frameworks/base/core/jni/android_media_AudioErrors.h
     */
    public static final int SUCCESS = 0;

    public static final int ERROR = -1;

    public static final int BAD_VALUE = -2;

    public static final int INVALID_OPERATION = -3;

    public static final int PERMISSION_DENIED = -4;

    public static final int NO_INIT = -5;

    public static final int DEAD_OBJECT = -6;

    public static final int WOULD_BLOCK = -7;

    /* AudioPolicyService methods */
    // 
    // audio device definitions: must be kept in sync with values in system/core/audio.h
    // 
    public static final int DEVICE_NONE = 0x0;

    // reserved bits
    public static final int DEVICE_BIT_IN = 0x80000000;

    public static final int DEVICE_BIT_DEFAULT = 0x40000000;

    // output devices, be sure to update AudioManager.java also
    public static final int DEVICE_OUT_EARPIECE = 0x1;

    public static final int DEVICE_OUT_SPEAKER = 0x2;

    public static final int DEVICE_OUT_WIRED_HEADSET = 0x4;

    public static final int DEVICE_OUT_WIRED_HEADPHONE = 0x8;

    public static final int DEVICE_OUT_BLUETOOTH_SCO = 0x10;

    public static final int DEVICE_OUT_BLUETOOTH_SCO_HEADSET = 0x20;

    public static final int DEVICE_OUT_BLUETOOTH_SCO_CARKIT = 0x40;

    public static final int DEVICE_OUT_BLUETOOTH_A2DP = 0x80;

    public static final int DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES = 0x100;

    public static final int DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER = 0x200;

    public static final int DEVICE_OUT_AUX_DIGITAL = 0x400;

    public static final int DEVICE_OUT_HDMI = android.media.AudioSystem.DEVICE_OUT_AUX_DIGITAL;

    public static final int DEVICE_OUT_ANLG_DOCK_HEADSET = 0x800;

    public static final int DEVICE_OUT_DGTL_DOCK_HEADSET = 0x1000;

    public static final int DEVICE_OUT_USB_ACCESSORY = 0x2000;

    public static final int DEVICE_OUT_USB_DEVICE = 0x4000;

    public static final int DEVICE_OUT_REMOTE_SUBMIX = 0x8000;

    public static final int DEVICE_OUT_TELEPHONY_TX = 0x10000;

    public static final int DEVICE_OUT_LINE = 0x20000;

    public static final int DEVICE_OUT_HDMI_ARC = 0x40000;

    public static final int DEVICE_OUT_SPDIF = 0x80000;

    public static final int DEVICE_OUT_FM = 0x100000;

    public static final int DEVICE_OUT_AUX_LINE = 0x200000;

    public static final int DEVICE_OUT_SPEAKER_SAFE = 0x400000;

    public static final int DEVICE_OUT_IP = 0x800000;

    public static final int DEVICE_OUT_BUS = 0x1000000;

    public static final int DEVICE_OUT_DEFAULT = android.media.AudioSystem.DEVICE_BIT_DEFAULT;

    public static final int DEVICE_OUT_ALL = ((((((((((((((((((((((((android.media.AudioSystem.DEVICE_OUT_EARPIECE | android.media.AudioSystem.DEVICE_OUT_SPEAKER) | android.media.AudioSystem.DEVICE_OUT_WIRED_HEADSET) | android.media.AudioSystem.DEVICE_OUT_WIRED_HEADPHONE) | android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_SCO) | android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_SCO_HEADSET) | android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_SCO_CARKIT) | android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_A2DP) | android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES) | android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER) | android.media.AudioSystem.DEVICE_OUT_HDMI) | android.media.AudioSystem.DEVICE_OUT_ANLG_DOCK_HEADSET) | android.media.AudioSystem.DEVICE_OUT_DGTL_DOCK_HEADSET) | android.media.AudioSystem.DEVICE_OUT_USB_ACCESSORY) | android.media.AudioSystem.DEVICE_OUT_USB_DEVICE) | android.media.AudioSystem.DEVICE_OUT_REMOTE_SUBMIX) | android.media.AudioSystem.DEVICE_OUT_TELEPHONY_TX) | android.media.AudioSystem.DEVICE_OUT_LINE) | android.media.AudioSystem.DEVICE_OUT_HDMI_ARC) | android.media.AudioSystem.DEVICE_OUT_SPDIF) | android.media.AudioSystem.DEVICE_OUT_FM) | android.media.AudioSystem.DEVICE_OUT_AUX_LINE) | android.media.AudioSystem.DEVICE_OUT_SPEAKER_SAFE) | android.media.AudioSystem.DEVICE_OUT_IP) | android.media.AudioSystem.DEVICE_OUT_BUS) | android.media.AudioSystem.DEVICE_OUT_DEFAULT;

    public static final int DEVICE_OUT_ALL_A2DP = (android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_A2DP | android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES) | android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER;

    public static final int DEVICE_OUT_ALL_SCO = (android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_SCO | android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_SCO_HEADSET) | android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_SCO_CARKIT;

    public static final int DEVICE_OUT_ALL_USB = android.media.AudioSystem.DEVICE_OUT_USB_ACCESSORY | android.media.AudioSystem.DEVICE_OUT_USB_DEVICE;

    public static final int DEVICE_OUT_ALL_HDMI_SYSTEM_AUDIO = (android.media.AudioSystem.DEVICE_OUT_AUX_LINE | android.media.AudioSystem.DEVICE_OUT_HDMI_ARC) | android.media.AudioSystem.DEVICE_OUT_SPDIF;

    public static final int DEVICE_ALL_HDMI_SYSTEM_AUDIO_AND_SPEAKER = android.media.AudioSystem.DEVICE_OUT_ALL_HDMI_SYSTEM_AUDIO | android.media.AudioSystem.DEVICE_OUT_SPEAKER;

    // input devices
    public static final int DEVICE_IN_COMMUNICATION = android.media.AudioSystem.DEVICE_BIT_IN | 0x1;

    public static final int DEVICE_IN_AMBIENT = android.media.AudioSystem.DEVICE_BIT_IN | 0x2;

    public static final int DEVICE_IN_BUILTIN_MIC = android.media.AudioSystem.DEVICE_BIT_IN | 0x4;

    public static final int DEVICE_IN_BLUETOOTH_SCO_HEADSET = android.media.AudioSystem.DEVICE_BIT_IN | 0x8;

    public static final int DEVICE_IN_WIRED_HEADSET = android.media.AudioSystem.DEVICE_BIT_IN | 0x10;

    public static final int DEVICE_IN_AUX_DIGITAL = android.media.AudioSystem.DEVICE_BIT_IN | 0x20;

    public static final int DEVICE_IN_HDMI = android.media.AudioSystem.DEVICE_IN_AUX_DIGITAL;

    public static final int DEVICE_IN_VOICE_CALL = android.media.AudioSystem.DEVICE_BIT_IN | 0x40;

    public static final int DEVICE_IN_TELEPHONY_RX = android.media.AudioSystem.DEVICE_IN_VOICE_CALL;

    public static final int DEVICE_IN_BACK_MIC = android.media.AudioSystem.DEVICE_BIT_IN | 0x80;

    public static final int DEVICE_IN_REMOTE_SUBMIX = android.media.AudioSystem.DEVICE_BIT_IN | 0x100;

    public static final int DEVICE_IN_ANLG_DOCK_HEADSET = android.media.AudioSystem.DEVICE_BIT_IN | 0x200;

    public static final int DEVICE_IN_DGTL_DOCK_HEADSET = android.media.AudioSystem.DEVICE_BIT_IN | 0x400;

    public static final int DEVICE_IN_USB_ACCESSORY = android.media.AudioSystem.DEVICE_BIT_IN | 0x800;

    public static final int DEVICE_IN_USB_DEVICE = android.media.AudioSystem.DEVICE_BIT_IN | 0x1000;

    public static final int DEVICE_IN_FM_TUNER = android.media.AudioSystem.DEVICE_BIT_IN | 0x2000;

    public static final int DEVICE_IN_TV_TUNER = android.media.AudioSystem.DEVICE_BIT_IN | 0x4000;

    public static final int DEVICE_IN_LINE = android.media.AudioSystem.DEVICE_BIT_IN | 0x8000;

    public static final int DEVICE_IN_SPDIF = android.media.AudioSystem.DEVICE_BIT_IN | 0x10000;

    public static final int DEVICE_IN_BLUETOOTH_A2DP = android.media.AudioSystem.DEVICE_BIT_IN | 0x20000;

    public static final int DEVICE_IN_LOOPBACK = android.media.AudioSystem.DEVICE_BIT_IN | 0x40000;

    public static final int DEVICE_IN_IP = android.media.AudioSystem.DEVICE_BIT_IN | 0x80000;

    public static final int DEVICE_IN_BUS = android.media.AudioSystem.DEVICE_BIT_IN | 0x100000;

    public static final int DEVICE_IN_DEFAULT = android.media.AudioSystem.DEVICE_BIT_IN | android.media.AudioSystem.DEVICE_BIT_DEFAULT;

    public static final int DEVICE_IN_ALL = ((((((((((((((((((((android.media.AudioSystem.DEVICE_IN_COMMUNICATION | android.media.AudioSystem.DEVICE_IN_AMBIENT) | android.media.AudioSystem.DEVICE_IN_BUILTIN_MIC) | android.media.AudioSystem.DEVICE_IN_BLUETOOTH_SCO_HEADSET) | android.media.AudioSystem.DEVICE_IN_WIRED_HEADSET) | android.media.AudioSystem.DEVICE_IN_HDMI) | android.media.AudioSystem.DEVICE_IN_TELEPHONY_RX) | android.media.AudioSystem.DEVICE_IN_BACK_MIC) | android.media.AudioSystem.DEVICE_IN_REMOTE_SUBMIX) | android.media.AudioSystem.DEVICE_IN_ANLG_DOCK_HEADSET) | android.media.AudioSystem.DEVICE_IN_DGTL_DOCK_HEADSET) | android.media.AudioSystem.DEVICE_IN_USB_ACCESSORY) | android.media.AudioSystem.DEVICE_IN_USB_DEVICE) | android.media.AudioSystem.DEVICE_IN_FM_TUNER) | android.media.AudioSystem.DEVICE_IN_TV_TUNER) | android.media.AudioSystem.DEVICE_IN_LINE) | android.media.AudioSystem.DEVICE_IN_SPDIF) | android.media.AudioSystem.DEVICE_IN_BLUETOOTH_A2DP) | android.media.AudioSystem.DEVICE_IN_LOOPBACK) | android.media.AudioSystem.DEVICE_IN_IP) | android.media.AudioSystem.DEVICE_IN_BUS) | android.media.AudioSystem.DEVICE_IN_DEFAULT;

    public static final int DEVICE_IN_ALL_SCO = android.media.AudioSystem.DEVICE_IN_BLUETOOTH_SCO_HEADSET;

    public static final int DEVICE_IN_ALL_USB = android.media.AudioSystem.DEVICE_IN_USB_ACCESSORY | android.media.AudioSystem.DEVICE_IN_USB_DEVICE;

    // device states, must match AudioSystem::device_connection_state
    public static final int DEVICE_STATE_UNAVAILABLE = 0;

    public static final int DEVICE_STATE_AVAILABLE = 1;

    private static final int NUM_DEVICE_STATES = 1;

    public static final java.lang.String DEVICE_OUT_EARPIECE_NAME = "earpiece";

    public static final java.lang.String DEVICE_OUT_SPEAKER_NAME = "speaker";

    public static final java.lang.String DEVICE_OUT_WIRED_HEADSET_NAME = "headset";

    public static final java.lang.String DEVICE_OUT_WIRED_HEADPHONE_NAME = "headphone";

    public static final java.lang.String DEVICE_OUT_BLUETOOTH_SCO_NAME = "bt_sco";

    public static final java.lang.String DEVICE_OUT_BLUETOOTH_SCO_HEADSET_NAME = "bt_sco_hs";

    public static final java.lang.String DEVICE_OUT_BLUETOOTH_SCO_CARKIT_NAME = "bt_sco_carkit";

    public static final java.lang.String DEVICE_OUT_BLUETOOTH_A2DP_NAME = "bt_a2dp";

    public static final java.lang.String DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES_NAME = "bt_a2dp_hp";

    public static final java.lang.String DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER_NAME = "bt_a2dp_spk";

    public static final java.lang.String DEVICE_OUT_AUX_DIGITAL_NAME = "aux_digital";

    public static final java.lang.String DEVICE_OUT_HDMI_NAME = "hdmi";

    public static final java.lang.String DEVICE_OUT_ANLG_DOCK_HEADSET_NAME = "analog_dock";

    public static final java.lang.String DEVICE_OUT_DGTL_DOCK_HEADSET_NAME = "digital_dock";

    public static final java.lang.String DEVICE_OUT_USB_ACCESSORY_NAME = "usb_accessory";

    public static final java.lang.String DEVICE_OUT_USB_DEVICE_NAME = "usb_device";

    public static final java.lang.String DEVICE_OUT_REMOTE_SUBMIX_NAME = "remote_submix";

    public static final java.lang.String DEVICE_OUT_TELEPHONY_TX_NAME = "telephony_tx";

    public static final java.lang.String DEVICE_OUT_LINE_NAME = "line";

    public static final java.lang.String DEVICE_OUT_HDMI_ARC_NAME = "hmdi_arc";

    public static final java.lang.String DEVICE_OUT_SPDIF_NAME = "spdif";

    public static final java.lang.String DEVICE_OUT_FM_NAME = "fm_transmitter";

    public static final java.lang.String DEVICE_OUT_AUX_LINE_NAME = "aux_line";

    public static final java.lang.String DEVICE_OUT_SPEAKER_SAFE_NAME = "speaker_safe";

    public static final java.lang.String DEVICE_OUT_IP_NAME = "ip";

    public static final java.lang.String DEVICE_OUT_BUS_NAME = "bus";

    public static final java.lang.String DEVICE_IN_COMMUNICATION_NAME = "communication";

    public static final java.lang.String DEVICE_IN_AMBIENT_NAME = "ambient";

    public static final java.lang.String DEVICE_IN_BUILTIN_MIC_NAME = "mic";

    public static final java.lang.String DEVICE_IN_BLUETOOTH_SCO_HEADSET_NAME = "bt_sco_hs";

    public static final java.lang.String DEVICE_IN_WIRED_HEADSET_NAME = "headset";

    public static final java.lang.String DEVICE_IN_AUX_DIGITAL_NAME = "aux_digital";

    public static final java.lang.String DEVICE_IN_TELEPHONY_RX_NAME = "telephony_rx";

    public static final java.lang.String DEVICE_IN_BACK_MIC_NAME = "back_mic";

    public static final java.lang.String DEVICE_IN_REMOTE_SUBMIX_NAME = "remote_submix";

    public static final java.lang.String DEVICE_IN_ANLG_DOCK_HEADSET_NAME = "analog_dock";

    public static final java.lang.String DEVICE_IN_DGTL_DOCK_HEADSET_NAME = "digital_dock";

    public static final java.lang.String DEVICE_IN_USB_ACCESSORY_NAME = "usb_accessory";

    public static final java.lang.String DEVICE_IN_USB_DEVICE_NAME = "usb_device";

    public static final java.lang.String DEVICE_IN_FM_TUNER_NAME = "fm_tuner";

    public static final java.lang.String DEVICE_IN_TV_TUNER_NAME = "tv_tuner";

    public static final java.lang.String DEVICE_IN_LINE_NAME = "line";

    public static final java.lang.String DEVICE_IN_SPDIF_NAME = "spdif";

    public static final java.lang.String DEVICE_IN_BLUETOOTH_A2DP_NAME = "bt_a2dp";

    public static final java.lang.String DEVICE_IN_LOOPBACK_NAME = "loopback";

    public static final java.lang.String DEVICE_IN_IP_NAME = "ip";

    public static final java.lang.String DEVICE_IN_BUS_NAME = "bus";

    public static java.lang.String getOutputDeviceName(int device) {
        switch (device) {
            case android.media.AudioSystem.DEVICE_OUT_EARPIECE :
                return android.media.AudioSystem.DEVICE_OUT_EARPIECE_NAME;
            case android.media.AudioSystem.DEVICE_OUT_SPEAKER :
                return android.media.AudioSystem.DEVICE_OUT_SPEAKER_NAME;
            case android.media.AudioSystem.DEVICE_OUT_WIRED_HEADSET :
                return android.media.AudioSystem.DEVICE_OUT_WIRED_HEADSET_NAME;
            case android.media.AudioSystem.DEVICE_OUT_WIRED_HEADPHONE :
                return android.media.AudioSystem.DEVICE_OUT_WIRED_HEADPHONE_NAME;
            case android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_SCO :
                return android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_SCO_NAME;
            case android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_SCO_HEADSET :
                return android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_SCO_HEADSET_NAME;
            case android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_SCO_CARKIT :
                return android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_SCO_CARKIT_NAME;
            case android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_A2DP :
                return android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_A2DP_NAME;
            case android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES :
                return android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES_NAME;
            case android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER :
                return android.media.AudioSystem.DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER_NAME;
            case android.media.AudioSystem.DEVICE_OUT_HDMI :
                return android.media.AudioSystem.DEVICE_OUT_HDMI_NAME;
            case android.media.AudioSystem.DEVICE_OUT_ANLG_DOCK_HEADSET :
                return android.media.AudioSystem.DEVICE_OUT_ANLG_DOCK_HEADSET_NAME;
            case android.media.AudioSystem.DEVICE_OUT_DGTL_DOCK_HEADSET :
                return android.media.AudioSystem.DEVICE_OUT_DGTL_DOCK_HEADSET_NAME;
            case android.media.AudioSystem.DEVICE_OUT_USB_ACCESSORY :
                return android.media.AudioSystem.DEVICE_OUT_USB_ACCESSORY_NAME;
            case android.media.AudioSystem.DEVICE_OUT_USB_DEVICE :
                return android.media.AudioSystem.DEVICE_OUT_USB_DEVICE_NAME;
            case android.media.AudioSystem.DEVICE_OUT_REMOTE_SUBMIX :
                return android.media.AudioSystem.DEVICE_OUT_REMOTE_SUBMIX_NAME;
            case android.media.AudioSystem.DEVICE_OUT_TELEPHONY_TX :
                return android.media.AudioSystem.DEVICE_OUT_TELEPHONY_TX_NAME;
            case android.media.AudioSystem.DEVICE_OUT_LINE :
                return android.media.AudioSystem.DEVICE_OUT_LINE_NAME;
            case android.media.AudioSystem.DEVICE_OUT_HDMI_ARC :
                return android.media.AudioSystem.DEVICE_OUT_HDMI_ARC_NAME;
            case android.media.AudioSystem.DEVICE_OUT_SPDIF :
                return android.media.AudioSystem.DEVICE_OUT_SPDIF_NAME;
            case android.media.AudioSystem.DEVICE_OUT_FM :
                return android.media.AudioSystem.DEVICE_OUT_FM_NAME;
            case android.media.AudioSystem.DEVICE_OUT_AUX_LINE :
                return android.media.AudioSystem.DEVICE_OUT_AUX_LINE_NAME;
            case android.media.AudioSystem.DEVICE_OUT_SPEAKER_SAFE :
                return android.media.AudioSystem.DEVICE_OUT_SPEAKER_SAFE_NAME;
            case android.media.AudioSystem.DEVICE_OUT_IP :
                return android.media.AudioSystem.DEVICE_OUT_IP_NAME;
            case android.media.AudioSystem.DEVICE_OUT_BUS :
                return android.media.AudioSystem.DEVICE_OUT_BUS_NAME;
            case android.media.AudioSystem.DEVICE_OUT_DEFAULT :
            default :
                return java.lang.Integer.toString(device);
        }
    }

    public static java.lang.String getInputDeviceName(int device) {
        switch (device) {
            case android.media.AudioSystem.DEVICE_IN_COMMUNICATION :
                return android.media.AudioSystem.DEVICE_IN_COMMUNICATION_NAME;
            case android.media.AudioSystem.DEVICE_IN_AMBIENT :
                return android.media.AudioSystem.DEVICE_IN_AMBIENT_NAME;
            case android.media.AudioSystem.DEVICE_IN_BUILTIN_MIC :
                return android.media.AudioSystem.DEVICE_IN_BUILTIN_MIC_NAME;
            case android.media.AudioSystem.DEVICE_IN_BLUETOOTH_SCO_HEADSET :
                return android.media.AudioSystem.DEVICE_IN_BLUETOOTH_SCO_HEADSET_NAME;
            case android.media.AudioSystem.DEVICE_IN_WIRED_HEADSET :
                return android.media.AudioSystem.DEVICE_IN_WIRED_HEADSET_NAME;
            case android.media.AudioSystem.DEVICE_IN_AUX_DIGITAL :
                return android.media.AudioSystem.DEVICE_IN_AUX_DIGITAL_NAME;
            case android.media.AudioSystem.DEVICE_IN_TELEPHONY_RX :
                return android.media.AudioSystem.DEVICE_IN_TELEPHONY_RX_NAME;
            case android.media.AudioSystem.DEVICE_IN_BACK_MIC :
                return android.media.AudioSystem.DEVICE_IN_BACK_MIC_NAME;
            case android.media.AudioSystem.DEVICE_IN_REMOTE_SUBMIX :
                return android.media.AudioSystem.DEVICE_IN_REMOTE_SUBMIX_NAME;
            case android.media.AudioSystem.DEVICE_IN_ANLG_DOCK_HEADSET :
                return android.media.AudioSystem.DEVICE_IN_ANLG_DOCK_HEADSET_NAME;
            case android.media.AudioSystem.DEVICE_IN_DGTL_DOCK_HEADSET :
                return android.media.AudioSystem.DEVICE_IN_DGTL_DOCK_HEADSET_NAME;
            case android.media.AudioSystem.DEVICE_IN_USB_ACCESSORY :
                return android.media.AudioSystem.DEVICE_IN_USB_ACCESSORY_NAME;
            case android.media.AudioSystem.DEVICE_IN_USB_DEVICE :
                return android.media.AudioSystem.DEVICE_IN_USB_DEVICE_NAME;
            case android.media.AudioSystem.DEVICE_IN_FM_TUNER :
                return android.media.AudioSystem.DEVICE_IN_FM_TUNER_NAME;
            case android.media.AudioSystem.DEVICE_IN_TV_TUNER :
                return android.media.AudioSystem.DEVICE_IN_TV_TUNER_NAME;
            case android.media.AudioSystem.DEVICE_IN_LINE :
                return android.media.AudioSystem.DEVICE_IN_LINE_NAME;
            case android.media.AudioSystem.DEVICE_IN_SPDIF :
                return android.media.AudioSystem.DEVICE_IN_SPDIF_NAME;
            case android.media.AudioSystem.DEVICE_IN_BLUETOOTH_A2DP :
                return android.media.AudioSystem.DEVICE_IN_BLUETOOTH_A2DP_NAME;
            case android.media.AudioSystem.DEVICE_IN_LOOPBACK :
                return android.media.AudioSystem.DEVICE_IN_LOOPBACK_NAME;
            case android.media.AudioSystem.DEVICE_IN_IP :
                return android.media.AudioSystem.DEVICE_IN_IP_NAME;
            case android.media.AudioSystem.DEVICE_IN_BUS :
                return android.media.AudioSystem.DEVICE_IN_BUS_NAME;
            case android.media.AudioSystem.DEVICE_IN_DEFAULT :
            default :
                return java.lang.Integer.toString(device);
        }
    }

    // phone state, match audio_mode???
    public static final int PHONE_STATE_OFFCALL = 0;

    public static final int PHONE_STATE_RINGING = 1;

    public static final int PHONE_STATE_INCALL = 2;

    // device categories config for setForceUse, must match audio_policy_forced_cfg_t
    public static final int FORCE_NONE = 0;

    public static final int FORCE_SPEAKER = 1;

    public static final int FORCE_HEADPHONES = 2;

    public static final int FORCE_BT_SCO = 3;

    public static final int FORCE_BT_A2DP = 4;

    public static final int FORCE_WIRED_ACCESSORY = 5;

    public static final int FORCE_BT_CAR_DOCK = 6;

    public static final int FORCE_BT_DESK_DOCK = 7;

    public static final int FORCE_ANALOG_DOCK = 8;

    public static final int FORCE_DIGITAL_DOCK = 9;

    public static final int FORCE_NO_BT_A2DP = 10;

    public static final int FORCE_SYSTEM_ENFORCED = 11;

    public static final int FORCE_HDMI_SYSTEM_AUDIO_ENFORCED = 12;

    public static final int FORCE_ENCODED_SURROUND_NEVER = 13;

    public static final int FORCE_ENCODED_SURROUND_ALWAYS = 14;

    public static final int NUM_FORCE_CONFIG = 15;

    public static final int FORCE_DEFAULT = android.media.AudioSystem.FORCE_NONE;

    // usage for setForceUse, must match audio_policy_force_use_t
    public static final int FOR_COMMUNICATION = 0;

    public static final int FOR_MEDIA = 1;

    public static final int FOR_RECORD = 2;

    public static final int FOR_DOCK = 3;

    public static final int FOR_SYSTEM = 4;

    public static final int FOR_HDMI_SYSTEM_AUDIO = 5;

    public static final int FOR_ENCODED_SURROUND = 6;

    private static final int NUM_FORCE_USE = 7;

    // usage for AudioRecord.startRecordingSync(), must match AudioSystem::sync_event_t
    public static final int SYNC_EVENT_NONE = 0;

    public static final int SYNC_EVENT_PRESENTATION_COMPLETE = 1;

    /**
     *
     *
     * @return command completion status, one of {@link #AUDIO_STATUS_OK},
    {@link #AUDIO_STATUS_ERROR} or {@link #AUDIO_STATUS_SERVER_DIED}
     */
    public static native int setDeviceConnectionState(int device, int state, java.lang.String device_address, java.lang.String device_name);

    public static native int getDeviceConnectionState(int device, java.lang.String device_address);

    public static native int setPhoneState(int state);

    public static native int setForceUse(int usage, int config);

    public static native int getForceUse(int usage);

    public static native int initStreamVolume(int stream, int indexMin, int indexMax);

    public static native int setStreamVolumeIndex(int stream, int index, int device);

    public static native int getStreamVolumeIndex(int stream, int device);

    public static native int setMasterVolume(float value);

    public static native float getMasterVolume();

    public static native int setMasterMute(boolean mute);

    public static native boolean getMasterMute();

    public static native int getDevicesForStream(int stream);

    /**
     *
     *
     * @unknown returns true if master mono is enabled.
     */
    public static native boolean getMasterMono();

    /**
     *
     *
     * @unknown enables or disables the master mono mode.
     */
    public static native int setMasterMono(boolean mono);

    // helpers for android.media.AudioManager.getProperty(), see description there for meaning
    public static native int getPrimaryOutputSamplingRate();

    public static native int getPrimaryOutputFrameCount();

    public static native int getOutputLatency(int stream);

    public static native int setLowRamDevice(boolean isLowRamDevice);

    public static native int checkAudioFlinger();

    public static native int listAudioPorts(java.util.ArrayList<android.media.AudioPort> ports, int[] generation);

    public static native int createAudioPatch(android.media.AudioPatch[] patch, android.media.AudioPortConfig[] sources, android.media.AudioPortConfig[] sinks);

    public static native int releaseAudioPatch(android.media.AudioPatch patch);

    public static native int listAudioPatches(java.util.ArrayList<android.media.AudioPatch> patches, int[] generation);

    public static native int setAudioPortConfig(android.media.AudioPortConfig config);

    // declare this instance as having a dynamic policy callback handler
    private static final native void native_register_dynamic_policy_callback();

    // declare this instance as having a recording configuration update callback handler
    private static final native void native_register_recording_callback();

    // must be kept in sync with value in include/system/audio.h
    public static final int AUDIO_HW_SYNC_INVALID = 0;

    public static native int getAudioHwSyncForSession(int sessionId);

    public static native int registerPolicyMixes(java.util.ArrayList<android.media.audiopolicy.AudioMix> mixes, boolean register);

    public static native int systemReady();

    // Items shared with audio service
    /**
     * The delay before playing a sound. This small period exists so the user
     * can press another key (non-volume keys, too) to have it NOT be audible.
     * <p>
     * PhoneWindow will implement this part.
     */
    public static final int PLAY_SOUND_DELAY = 300;

    /**
     * Constant to identify a focus stack entry that is used to hold the focus while the phone
     * is ringing or during a call. Used by com.android.internal.telephony.CallManager when
     * entering and exiting calls.
     */
    public static final java.lang.String IN_VOICE_COMM_FOCUS_ID = "AudioFocus_For_Phone_Ring_And_Calls";

    /**
     *
     *
     * @see AudioManager#setVibrateSetting(int, int)
     */
    public static int getValueForVibrateSetting(int existingValue, int vibrateType, int vibrateSetting) {
        // First clear the existing setting. Each vibrate type has two bits in
        // the value. Note '3' is '11' in binary.
        existingValue &= ~(3 << (vibrateType * 2));
        // Set into the old value
        existingValue |= (vibrateSetting & 3) << (vibrateType * 2);
        return existingValue;
    }

    public static int getDefaultStreamVolume(int streamType) {
        return android.media.AudioSystem.DEFAULT_STREAM_VOLUME[streamType];
    }

    public static int[] DEFAULT_STREAM_VOLUME = new int[]{ 4// STREAM_VOICE_CALL
    , 7// STREAM_SYSTEM
    , 5// STREAM_RING
    , 11// STREAM_MUSIC
    , 6// STREAM_ALARM
    , 5// STREAM_NOTIFICATION
    , 7// STREAM_BLUETOOTH_SCO
    , 7// STREAM_SYSTEM_ENFORCED
    , 11// STREAM_DTMF
    , 11// STREAM_TTS
     };

    public static java.lang.String streamToString(int stream) {
        if ((stream >= 0) && (stream < android.media.AudioSystem.STREAM_NAMES.length))
            return android.media.AudioSystem.STREAM_NAMES[stream];

        if (stream == android.media.AudioManager.USE_DEFAULT_STREAM_TYPE)
            return "USE_DEFAULT_STREAM_TYPE";

        return "UNKNOWN_STREAM_" + stream;
    }

    /**
     * The platform has no specific capabilities
     */
    public static final int PLATFORM_DEFAULT = 0;

    /**
     * The platform is voice call capable (a phone)
     */
    public static final int PLATFORM_VOICE = 1;

    /**
     * The platform is a television or a set-top box
     */
    public static final int PLATFORM_TELEVISION = 2;

    /**
     * Return the platform type that this is running on. One of:
     * <ul>
     * <li>{@link #PLATFORM_VOICE}</li>
     * <li>{@link #PLATFORM_TELEVISION}</li>
     * <li>{@link #PLATFORM_DEFAULT}</li>
     * </ul>
     */
    public static int getPlatformType(android.content.Context context) {
        if (context.getResources().getBoolean(com.android.internal.R.bool.config_voice_capable)) {
            return android.media.AudioSystem.PLATFORM_VOICE;
        } else
            if (context.getPackageManager().hasSystemFeature(android.content.pm.PackageManager.FEATURE_LEANBACK)) {
                return android.media.AudioSystem.PLATFORM_TELEVISION;
            } else {
                return android.media.AudioSystem.PLATFORM_DEFAULT;
            }

    }

    public static final int DEFAULT_MUTE_STREAMS_AFFECTED = (((1 << android.media.AudioSystem.STREAM_MUSIC) | (1 << android.media.AudioSystem.STREAM_RING)) | (1 << android.media.AudioSystem.STREAM_NOTIFICATION)) | (1 << android.media.AudioSystem.STREAM_SYSTEM);

    /**
     * Event posted by AudioTrack and AudioRecord JNI (JNIDeviceCallback) when routing changes.
     * Keep in sync with core/jni/android_media_DeviceCallback.h.
     */
    static final int NATIVE_EVENT_ROUTING_CHANGE = 1000;
}

