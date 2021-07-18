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
package android.media.audiopolicy;


/**
 *
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class AudioMix {
    private android.media.audiopolicy.AudioMixingRule mRule;

    private android.media.AudioFormat mFormat;

    private int mRouteFlags;

    private int mMixType = android.media.audiopolicy.AudioMix.MIX_TYPE_INVALID;

    // written by AudioPolicy
    int mMixState = android.media.audiopolicy.AudioMix.MIX_STATE_DISABLED;

    int mCallbackFlags;

    java.lang.String mDeviceAddress;

    // initialized in constructor, read by AudioPolicyConfig
    final int mDeviceSystemType;// an AudioSystem.DEVICE_* value, not AudioDeviceInfo.TYPE_*


    /**
     * All parameters are guaranteed valid through the Builder.
     */
    private AudioMix(android.media.audiopolicy.AudioMixingRule rule, android.media.AudioFormat format, int routeFlags, int callbackFlags, int deviceType, java.lang.String deviceAddress) {
        mRule = rule;
        mFormat = format;
        mRouteFlags = routeFlags;
        mMixType = rule.getTargetMixType();
        mCallbackFlags = callbackFlags;
        mDeviceSystemType = deviceType;
        mDeviceAddress = (deviceAddress == null) ? new java.lang.String("") : deviceAddress;
    }

    // CALLBACK_FLAG_* values: keep in sync with AudioMix::kCbFlag* values defined
    // in frameworks/av/include/media/AudioPolicy.h
    /**
     *
     *
     * @unknown 
     */
    public static final int CALLBACK_FLAG_NOTIFY_ACTIVITY = 0x1;

    // when adding new MIX_FLAG_* flags, add them to this mask of authorized masks:
    private static final int CALLBACK_FLAGS_ALL = android.media.audiopolicy.AudioMix.CALLBACK_FLAG_NOTIFY_ACTIVITY;

    // ROUTE_FLAG_* values: keep in sync with MIX_ROUTE_FLAG_* values defined
    // in frameworks/av/include/media/AudioPolicy.h
    /**
     * An audio mix behavior where the output of the mix is sent to the original destination of
     * the audio signal, i.e. an output device for an output mix, or a recording for an input mix.
     */
    @android.annotation.SystemApi
    public static final int ROUTE_FLAG_RENDER = 0x1;

    /**
     * An audio mix behavior where the output of the mix is rerouted back to the framework and
     * is accessible for injection or capture through the {@link AudioTrack} and {@link AudioRecord}
     * APIs.
     */
    @android.annotation.SystemApi
    public static final int ROUTE_FLAG_LOOP_BACK = 0x1 << 1;

    private static final int ROUTE_FLAG_SUPPORTED = android.media.audiopolicy.AudioMix.ROUTE_FLAG_RENDER | android.media.audiopolicy.AudioMix.ROUTE_FLAG_LOOP_BACK;

    // MIX_TYPE_* values to keep in sync with frameworks/av/include/media/AudioPolicy.h
    /**
     *
     *
     * @unknown Invalid mix type, default value.
     */
    public static final int MIX_TYPE_INVALID = -1;

    /**
     *
     *
     * @unknown Mix type indicating playback streams are mixed.
     */
    public static final int MIX_TYPE_PLAYERS = 0;

    /**
     *
     *
     * @unknown Mix type indicating recording streams are mixed.
     */
    public static final int MIX_TYPE_RECORDERS = 1;

    // MIX_STATE_* values to keep in sync with frameworks/av/include/media/AudioPolicy.h
    /**
     *
     *
     * @unknown State of a mix before its policy is enabled.
     */
    @android.annotation.SystemApi
    public static final int MIX_STATE_DISABLED = -1;

    /**
     *
     *
     * @unknown State of a mix when there is no audio to mix.
     */
    @android.annotation.SystemApi
    public static final int MIX_STATE_IDLE = 0;

    /**
     *
     *
     * @unknown State of a mix that is actively mixing audio.
     */
    @android.annotation.SystemApi
    public static final int MIX_STATE_MIXING = 1;

    /**
     *
     *
     * @unknown The current mixing state.
     * @return one of {@link #MIX_STATE_DISABLED}, {@link #MIX_STATE_IDLE},
    {@link #MIX_STATE_MIXING}.
     */
    @android.annotation.SystemApi
    public int getMixState() {
        return mMixState;
    }

    int getRouteFlags() {
        return mRouteFlags;
    }

    android.media.AudioFormat getFormat() {
        return mFormat;
    }

    android.media.audiopolicy.AudioMixingRule getRule() {
        return mRule;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getMixType() {
        return mMixType;
    }

    void setRegistration(java.lang.String regId) {
        mDeviceAddress = regId;
    }

    /**
     *
     *
     * @unknown 
     */
    public java.lang.String getRegistration() {
        return mDeviceAddress;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mRouteFlags, mRule, mMixType, mFormat);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = true, value = { android.media.audiopolicy.AudioMix.ROUTE_FLAG_RENDER, android.media.audiopolicy.AudioMix.ROUTE_FLAG_LOOP_BACK })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface RouteFlags {}

    /**
     * Builder class for {@link AudioMix} objects
     */
    @android.annotation.SystemApi
    public static class Builder {
        private android.media.audiopolicy.AudioMixingRule mRule = null;

        private android.media.AudioFormat mFormat = null;

        private int mRouteFlags = 0;

        private int mCallbackFlags = 0;

        // an AudioSystem.DEVICE_* value, not AudioDeviceInfo.TYPE_*
        private int mDeviceSystemType = android.media.AudioSystem.DEVICE_NONE;

        private java.lang.String mDeviceAddress = null;

        /**
         *
         *
         * @unknown Only used by AudioPolicyConfig, not a public API.
         */
        Builder() {
        }

        /**
         * Construct an instance for the given {@link AudioMixingRule}.
         *
         * @param rule
         * 		a non-null {@link AudioMixingRule} instance.
         * @throws IllegalArgumentException
         * 		
         */
        @android.annotation.SystemApi
        public Builder(android.media.audiopolicy.AudioMixingRule rule) throws java.lang.IllegalArgumentException {
            if (rule == null) {
                throw new java.lang.IllegalArgumentException("Illegal null AudioMixingRule argument");
            }
            mRule = rule;
        }

        /**
         *
         *
         * @unknown Only used by AudioPolicyConfig, not a public API.
         * @param rule
         * 		
         * @return the same Builder instance.
         * @throws IllegalArgumentException
         * 		
         */
        android.media.audiopolicy.AudioMix.Builder setMixingRule(android.media.audiopolicy.AudioMixingRule rule) throws java.lang.IllegalArgumentException {
            if (rule == null) {
                throw new java.lang.IllegalArgumentException("Illegal null AudioMixingRule argument");
            }
            mRule = rule;
            return this;
        }

        /**
         *
         *
         * @unknown Only used by AudioPolicyConfig, not a public API.
         * @param callbackFlags
         * 		which callbacks are called from native
         * @return the same Builder instance.
         * @throws IllegalArgumentException
         * 		
         */
        android.media.audiopolicy.AudioMix.Builder setCallbackFlags(int flags) throws java.lang.IllegalArgumentException {
            if ((flags != 0) && ((flags & android.media.audiopolicy.AudioMix.CALLBACK_FLAGS_ALL) == 0)) {
                throw new java.lang.IllegalArgumentException("Illegal callback flags 0x" + java.lang.Integer.toHexString(flags).toUpperCase());
            }
            mCallbackFlags = flags;
            return this;
        }

        /**
         *
         *
         * @unknown Only used by AudioPolicyConfig, not a public API.
         * @param deviceType
         * 		an AudioSystem.DEVICE_* value, not AudioDeviceInfo.TYPE_*
         * @param address
         * 		
         * @return the same Builder instance.
         */
        android.media.audiopolicy.AudioMix.Builder setDevice(int deviceType, java.lang.String address) {
            mDeviceSystemType = deviceType;
            mDeviceAddress = address;
            return this;
        }

        /**
         * Sets the {@link AudioFormat} for the mix.
         *
         * @param format
         * 		a non-null {@link AudioFormat} instance.
         * @return the same Builder instance.
         * @throws IllegalArgumentException
         * 		
         */
        @android.annotation.SystemApi
        public android.media.audiopolicy.AudioMix.Builder setFormat(android.media.AudioFormat format) throws java.lang.IllegalArgumentException {
            if (format == null) {
                throw new java.lang.IllegalArgumentException("Illegal null AudioFormat argument");
            }
            mFormat = format;
            return this;
        }

        /**
         * Sets the routing behavior for the mix. If not set, routing behavior will default to
         * {@link AudioMix#ROUTE_FLAG_LOOP_BACK}.
         *
         * @param routeFlags
         * 		one of {@link AudioMix#ROUTE_FLAG_LOOP_BACK},
         * 		{@link AudioMix#ROUTE_FLAG_RENDER}
         * @return the same Builder instance.
         * @throws IllegalArgumentException
         * 		
         */
        @android.annotation.SystemApi
        public android.media.audiopolicy.AudioMix.Builder setRouteFlags(@android.media.audiopolicy.AudioMix.RouteFlags
        int routeFlags) throws java.lang.IllegalArgumentException {
            if (routeFlags == 0) {
                throw new java.lang.IllegalArgumentException("Illegal empty route flags");
            }
            if ((routeFlags & android.media.audiopolicy.AudioMix.ROUTE_FLAG_SUPPORTED) == 0) {
                throw new java.lang.IllegalArgumentException(("Invalid route flags 0x" + java.lang.Integer.toHexString(routeFlags)) + "when configuring an AudioMix");
            }
            if ((routeFlags & (~android.media.audiopolicy.AudioMix.ROUTE_FLAG_SUPPORTED)) != 0) {
                throw new java.lang.IllegalArgumentException(("Unknown route flags 0x" + java.lang.Integer.toHexString(routeFlags)) + "when configuring an AudioMix");
            }
            mRouteFlags = routeFlags;
            return this;
        }

        /**
         * Sets the audio device used for playback. Cannot be used in the context of an audio
         * policy used to inject audio to be recorded, or in a mix whose route flags doesn't
         * specify {@link AudioMix#ROUTE_FLAG_RENDER}.
         *
         * @param device
         * 		a non-null AudioDeviceInfo describing the audio device to play the output
         * 		of this mix.
         * @return the same Builder instance
         * @throws IllegalArgumentException
         * 		
         */
        @android.annotation.SystemApi
        public android.media.audiopolicy.AudioMix.Builder setDevice(@android.annotation.NonNull
        android.media.AudioDeviceInfo device) throws java.lang.IllegalArgumentException {
            if (device == null) {
                throw new java.lang.IllegalArgumentException("Illegal null AudioDeviceInfo argument");
            }
            if (!device.isSink()) {
                throw new java.lang.IllegalArgumentException("Unsupported device type on mix, not a sink");
            }
            mDeviceSystemType = android.media.AudioDeviceInfo.convertDeviceTypeToInternalDevice(device.getType());
            mDeviceAddress = device.getAddress();
            return this;
        }

        /**
         * Combines all of the settings and return a new {@link AudioMix} object.
         *
         * @return a new {@link AudioMix} object
         * @throws IllegalArgumentException
         * 		if no {@link AudioMixingRule} has been set.
         */
        @android.annotation.SystemApi
        public android.media.audiopolicy.AudioMix build() throws java.lang.IllegalArgumentException {
            if (mRule == null) {
                throw new java.lang.IllegalArgumentException("Illegal null AudioMixingRule");
            }
            if (mRouteFlags == 0) {
                // no route flags set, use default as described in Builder.setRouteFlags(int)
                mRouteFlags = android.media.audiopolicy.AudioMix.ROUTE_FLAG_LOOP_BACK;
            }
            // can't do loop back AND render at same time in this implementation
            if (mRouteFlags == (android.media.audiopolicy.AudioMix.ROUTE_FLAG_RENDER | android.media.audiopolicy.AudioMix.ROUTE_FLAG_LOOP_BACK)) {
                throw new java.lang.IllegalArgumentException("Unsupported route behavior combination 0x" + java.lang.Integer.toHexString(mRouteFlags));
            }
            if (mFormat == null) {
                // FIXME Can we eliminate this?  Will AudioMix work with an unspecified sample rate?
                int rate = android.media.AudioSystem.getPrimaryOutputSamplingRate();
                if (rate <= 0) {
                    rate = 44100;
                }
                mFormat = new android.media.AudioFormat.Builder().setSampleRate(rate).build();
            }
            if (((mDeviceSystemType != android.media.AudioSystem.DEVICE_NONE) && (mDeviceSystemType != android.media.AudioSystem.DEVICE_OUT_REMOTE_SUBMIX)) && (mDeviceSystemType != android.media.AudioSystem.DEVICE_IN_REMOTE_SUBMIX)) {
                if ((mRouteFlags & android.media.audiopolicy.AudioMix.ROUTE_FLAG_RENDER) == 0) {
                    throw new java.lang.IllegalArgumentException("Can't have audio device without flag ROUTE_FLAG_RENDER");
                }
                if (mRule.getTargetMixType() != android.media.audiopolicy.AudioMix.MIX_TYPE_PLAYERS) {
                    throw new java.lang.IllegalArgumentException("Unsupported device on non-playback mix");
                }
            } else {
                if ((mRouteFlags & android.media.audiopolicy.AudioMix.ROUTE_FLAG_RENDER) == android.media.audiopolicy.AudioMix.ROUTE_FLAG_RENDER) {
                    throw new java.lang.IllegalArgumentException("Can't have flag ROUTE_FLAG_RENDER without an audio device");
                }
                if ((mRouteFlags & android.media.audiopolicy.AudioMix.ROUTE_FLAG_SUPPORTED) == android.media.audiopolicy.AudioMix.ROUTE_FLAG_LOOP_BACK) {
                    if (mRule.getTargetMixType() == android.media.audiopolicy.AudioMix.MIX_TYPE_PLAYERS) {
                        mDeviceSystemType = android.media.AudioSystem.DEVICE_OUT_REMOTE_SUBMIX;
                    } else
                        if (mRule.getTargetMixType() == android.media.audiopolicy.AudioMix.MIX_TYPE_RECORDERS) {
                            mDeviceSystemType = android.media.AudioSystem.DEVICE_IN_REMOTE_SUBMIX;
                        } else {
                            throw new java.lang.IllegalArgumentException("Unknown mixing rule type");
                        }

                }
            }
            return new android.media.audiopolicy.AudioMix(mRule, mFormat, mRouteFlags, mCallbackFlags, mDeviceSystemType, mDeviceAddress);
        }
    }
}

