/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.media.audiofx;


/**
 * A sound generated within a room travels in many directions. The listener first hears the direct
 * sound from the source itself. Later, he or she hears discrete echoes caused by sound bouncing off
 * nearby walls, the ceiling and the floor. As sound waves arrive after undergoing more and more
 * reflections, individual reflections become indistinguishable and the listener hears continuous
 * reverberation that decays over time.
 * Reverb is vital for modeling a listener's environment. It can be used in music applications
 * to simulate music being played back in various environments, or in games to immerse the
 * listener within the game's environment.
 * The EnvironmentalReverb class allows an application to control each reverb engine property in a
 * global reverb environment and is more suitable for games. For basic control, more suitable for
 * music applications, it is recommended to use the
 * {@link android.media.audiofx.PresetReverb} class.
 * <p>An application creates a EnvironmentalReverb object to instantiate and control a reverb engine
 * in the audio framework.
 * <p>The methods, parameter types and units exposed by the EnvironmentalReverb implementation are
 * directly mapping those defined by the OpenSL ES 1.0.1 Specification
 * (http://www.khronos.org/opensles/) for the SLEnvironmentalReverbItf interface.
 * Please refer to this specification for more details.
 * <p>The EnvironmentalReverb is an output mix auxiliary effect and should be created on
 * Audio session 0. In order for a MediaPlayer or AudioTrack to be fed into this effect,
 * they must be explicitely attached to it and a send level must be specified. Use the effect ID
 * returned by getId() method to designate this particular effect when attaching it to the
 * MediaPlayer or AudioTrack.
 * <p>Creating a reverb on the output mix (audio session 0) requires permission
 * {@link android.Manifest.permission#MODIFY_AUDIO_SETTINGS}
 * <p>See {@link android.media.audiofx.AudioEffect} class for more details on controlling
 * audio effects.
 */
public class EnvironmentalReverb extends android.media.audiofx.AudioEffect {
    private static final java.lang.String TAG = "EnvironmentalReverb";

    // These constants must be synchronized with those in
    // frameworks/base/include/media/EffectEnvironmentalReverbApi.h
    /**
     * Room level. Parameter ID for OnParameterChangeListener
     */
    public static final int PARAM_ROOM_LEVEL = 0;

    /**
     * Room HF level. Parameter ID for OnParameterChangeListener
     */
    public static final int PARAM_ROOM_HF_LEVEL = 1;

    /**
     * Decay time. Parameter ID for OnParameterChangeListener
     */
    public static final int PARAM_DECAY_TIME = 2;

    /**
     * Decay HF ratio. Parameter ID for
     * {@link android.media.audiofx.EnvironmentalReverb.OnParameterChangeListener}
     */
    public static final int PARAM_DECAY_HF_RATIO = 3;

    /**
     * Early reflections level. Parameter ID for OnParameterChangeListener
     */
    public static final int PARAM_REFLECTIONS_LEVEL = 4;

    /**
     * Early reflections delay. Parameter ID for OnParameterChangeListener
     */
    public static final int PARAM_REFLECTIONS_DELAY = 5;

    /**
     * Reverb level. Parameter ID for OnParameterChangeListener
     */
    public static final int PARAM_REVERB_LEVEL = 6;

    /**
     * Reverb delay. Parameter ID for OnParameterChangeListener
     */
    public static final int PARAM_REVERB_DELAY = 7;

    /**
     * Diffusion. Parameter ID for OnParameterChangeListener
     */
    public static final int PARAM_DIFFUSION = 8;

    /**
     * Density. Parameter ID for OnParameterChangeListener
     */
    public static final int PARAM_DENSITY = 9;

    // used by setProperties()/getProperties
    private static final int PARAM_PROPERTIES = 10;

    /**
     * Registered listener for parameter changes
     */
    private android.media.audiofx.EnvironmentalReverb.OnParameterChangeListener mParamListener = null;

    /**
     * Listener used internally to to receive raw parameter change event from AudioEffect super
     * class
     */
    private android.media.audiofx.EnvironmentalReverb.BaseParameterListener mBaseParamListener = null;

    /**
     * Lock for access to mParamListener
     */
    private final java.lang.Object mParamListenerLock = new java.lang.Object();

    /**
     * Class constructor.
     *
     * @param priority
     * 		the priority level requested by the application for controlling the
     * 		EnvironmentalReverb engine. As the same engine can be shared by several applications, this
     * 		parameter indicates how much the requesting application needs control of effect parameters.
     * 		The normal priority is 0, above normal is a positive number, below normal a negative number.
     * @param audioSession
     * 		system wide unique audio session identifier. If audioSession
     * 		is not 0, the EnvironmentalReverb will be attached to the MediaPlayer or AudioTrack in the
     * 		same audio session. Otherwise, the EnvironmentalReverb will apply to the output mix.
     * 		As the EnvironmentalReverb is an auxiliary effect it is recommended to instantiate it on
     * 		audio session 0 and to attach it to the MediaPLayer auxiliary output.
     * @throws java.lang.IllegalArgumentException
     * 		
     * @throws java.lang.UnsupportedOperationException
     * 		
     * @throws java.lang.RuntimeException
     * 		
     */
    public EnvironmentalReverb(int priority, int audioSession) throws java.lang.IllegalArgumentException, java.lang.RuntimeException, java.lang.UnsupportedOperationException {
        super(android.media.audiofx.AudioEffect.EFFECT_TYPE_ENV_REVERB, android.media.audiofx.AudioEffect.EFFECT_TYPE_NULL, priority, audioSession);
    }

    /**
     * Sets the master volume level of the environmental reverb effect.
     *
     * @param room
     * 		room level in millibels. The valid range is [-9000, 0].
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public void setRoomLevel(short room) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = android.media.audiofx.AudioEffect.shortToByteArray(room);
        checkStatus(setParameter(android.media.audiofx.EnvironmentalReverb.PARAM_ROOM_LEVEL, param));
    }

    /**
     * Gets the master volume level of the environmental reverb effect.
     *
     * @return the room level in millibels.
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public short getRoomLevel() throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = new byte[2];
        checkStatus(getParameter(android.media.audiofx.EnvironmentalReverb.PARAM_ROOM_LEVEL, param));
        return android.media.audiofx.AudioEffect.byteArrayToShort(param);
    }

    /**
     * Sets the volume level at 5 kHz relative to the volume level at low frequencies of the
     * overall reverb effect.
     * <p>This controls a low-pass filter that will reduce the level of the high-frequency.
     *
     * @param roomHF
     * 		high frequency attenuation level in millibels. The valid range is [-9000, 0].
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public void setRoomHFLevel(short roomHF) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = android.media.audiofx.AudioEffect.shortToByteArray(roomHF);
        checkStatus(setParameter(android.media.audiofx.EnvironmentalReverb.PARAM_ROOM_HF_LEVEL, param));
    }

    /**
     * Gets the room HF level.
     *
     * @return the room HF level in millibels.
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public short getRoomHFLevel() throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = new byte[2];
        checkStatus(getParameter(android.media.audiofx.EnvironmentalReverb.PARAM_ROOM_HF_LEVEL, param));
        return android.media.audiofx.AudioEffect.byteArrayToShort(param);
    }

    /**
     * Sets the time taken for the level of reverberation to decay by 60 dB.
     *
     * @param decayTime
     * 		decay time in milliseconds. The valid range is [100, 20000].
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public void setDecayTime(int decayTime) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = android.media.audiofx.AudioEffect.intToByteArray(decayTime);
        checkStatus(setParameter(android.media.audiofx.EnvironmentalReverb.PARAM_DECAY_TIME, param));
    }

    /**
     * Gets the decay time.
     *
     * @return the decay time in milliseconds.
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public int getDecayTime() throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = new byte[4];
        checkStatus(getParameter(android.media.audiofx.EnvironmentalReverb.PARAM_DECAY_TIME, param));
        return android.media.audiofx.AudioEffect.byteArrayToInt(param);
    }

    /**
     * Sets the ratio of high frequency decay time (at 5 kHz) relative to the decay time at low
     * frequencies.
     *
     * @param decayHFRatio
     * 		high frequency decay ratio using a permille scale. The valid range is
     * 		[100, 2000]. A ratio of 1000 indicates that all frequencies decay at the same rate.
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public void setDecayHFRatio(short decayHFRatio) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = android.media.audiofx.AudioEffect.shortToByteArray(decayHFRatio);
        checkStatus(setParameter(android.media.audiofx.EnvironmentalReverb.PARAM_DECAY_HF_RATIO, param));
    }

    /**
     * Gets the ratio of high frequency decay time (at 5 kHz) relative to low frequencies.
     *
     * @return the decay HF ration. See {@link #setDecayHFRatio(short)} for units.
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public short getDecayHFRatio() throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = new byte[2];
        checkStatus(getParameter(android.media.audiofx.EnvironmentalReverb.PARAM_DECAY_HF_RATIO, param));
        return android.media.audiofx.AudioEffect.byteArrayToShort(param);
    }

    /**
     * Sets the volume level of the early reflections.
     * <p>This level is combined with the overall room level
     * (set using {@link #setRoomLevel(short)}).
     *
     * @param reflectionsLevel
     * 		reflection level in millibels. The valid range is [-9000, 1000].
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public void setReflectionsLevel(short reflectionsLevel) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = android.media.audiofx.AudioEffect.shortToByteArray(reflectionsLevel);
        checkStatus(setParameter(android.media.audiofx.EnvironmentalReverb.PARAM_REFLECTIONS_LEVEL, param));
    }

    /**
     * Gets the volume level of the early reflections.
     *
     * @return the early reflections level in millibels.
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public short getReflectionsLevel() throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = new byte[2];
        checkStatus(getParameter(android.media.audiofx.EnvironmentalReverb.PARAM_REFLECTIONS_LEVEL, param));
        return android.media.audiofx.AudioEffect.byteArrayToShort(param);
    }

    /**
     * Sets the delay time for the early reflections.
     * <p>This method sets the time between when the direct path is heard and when the first
     * reflection is heard.
     *
     * @param reflectionsDelay
     * 		reflections delay in milliseconds. The valid range is [0, 300].
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public void setReflectionsDelay(int reflectionsDelay) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = android.media.audiofx.AudioEffect.intToByteArray(reflectionsDelay);
        checkStatus(setParameter(android.media.audiofx.EnvironmentalReverb.PARAM_REFLECTIONS_DELAY, param));
    }

    /**
     * Gets the reflections delay.
     *
     * @return the early reflections delay in milliseconds.
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public int getReflectionsDelay() throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = new byte[4];
        checkStatus(getParameter(android.media.audiofx.EnvironmentalReverb.PARAM_REFLECTIONS_DELAY, param));
        return android.media.audiofx.AudioEffect.byteArrayToInt(param);
    }

    /**
     * Sets the volume level of the late reverberation.
     * <p>This level is combined with the overall room level (set using {@link #setRoomLevel(short)}).
     *
     * @param reverbLevel
     * 		reverb level in millibels. The valid range is [-9000, 2000].
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public void setReverbLevel(short reverbLevel) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = android.media.audiofx.AudioEffect.shortToByteArray(reverbLevel);
        checkStatus(setParameter(android.media.audiofx.EnvironmentalReverb.PARAM_REVERB_LEVEL, param));
    }

    /**
     * Gets the reverb level.
     *
     * @return the reverb level in millibels.
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public short getReverbLevel() throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = new byte[2];
        checkStatus(getParameter(android.media.audiofx.EnvironmentalReverb.PARAM_REVERB_LEVEL, param));
        return android.media.audiofx.AudioEffect.byteArrayToShort(param);
    }

    /**
     * Sets the time between the first reflection and the reverberation.
     *
     * @param reverbDelay
     * 		reverb delay in milliseconds. The valid range is [0, 100].
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public void setReverbDelay(int reverbDelay) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = android.media.audiofx.AudioEffect.intToByteArray(reverbDelay);
        checkStatus(setParameter(android.media.audiofx.EnvironmentalReverb.PARAM_REVERB_DELAY, param));
    }

    /**
     * Gets the reverb delay.
     *
     * @return the reverb delay in milliseconds.
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public int getReverbDelay() throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = new byte[4];
        checkStatus(getParameter(android.media.audiofx.EnvironmentalReverb.PARAM_REVERB_DELAY, param));
        return android.media.audiofx.AudioEffect.byteArrayToInt(param);
    }

    /**
     * Sets the echo density in the late reverberation decay.
     * <p>The scale should approximately map linearly to the perceived change in reverberation.
     *
     * @param diffusion
     * 		diffusion specified using a permille scale. The diffusion valid range is
     * 		[0, 1000]. A value of 1000 o/oo indicates a smooth reverberation decay.
     * 		Values below this level give a more <i>grainy</i> character.
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public void setDiffusion(short diffusion) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = android.media.audiofx.AudioEffect.shortToByteArray(diffusion);
        checkStatus(setParameter(android.media.audiofx.EnvironmentalReverb.PARAM_DIFFUSION, param));
    }

    /**
     * Gets diffusion level.
     *
     * @return the diffusion level. See {@link #setDiffusion(short)} for units.
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public short getDiffusion() throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = new byte[2];
        checkStatus(getParameter(android.media.audiofx.EnvironmentalReverb.PARAM_DIFFUSION, param));
        return android.media.audiofx.AudioEffect.byteArrayToShort(param);
    }

    /**
     * Controls the modal density of the late reverberation decay.
     * <p> The scale should approximately map linearly to the perceived change in reverberation.
     * A lower density creates a hollow sound that is useful for simulating small reverberation
     * spaces such as bathrooms.
     *
     * @param density
     * 		density specified using a permille scale. The valid range is [0, 1000].
     * 		A value of 1000 o/oo indicates a natural sounding reverberation. Values below this level
     * 		produce a more colored effect.
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public void setDensity(short density) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = android.media.audiofx.AudioEffect.shortToByteArray(density);
        checkStatus(setParameter(android.media.audiofx.EnvironmentalReverb.PARAM_DENSITY, param));
    }

    /**
     * Gets the density level.
     *
     * @return the density level. See {@link #setDiffusion(short)} for units.
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public short getDensity() throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = new byte[2];
        checkStatus(getParameter(android.media.audiofx.EnvironmentalReverb.PARAM_DENSITY, param));
        return android.media.audiofx.AudioEffect.byteArrayToShort(param);
    }

    /**
     * The OnParameterChangeListener interface defines a method called by the EnvironmentalReverb
     * when a parameter value has changed.
     */
    public interface OnParameterChangeListener {
        /**
         * Method called when a parameter value has changed. The method is called only if the
         * parameter was changed by another application having the control of the same
         * EnvironmentalReverb engine.
         *
         * @param effect
         * 		the EnvironmentalReverb on which the interface is registered.
         * @param status
         * 		status of the set parameter operation.
         * @param param
         * 		ID of the modified parameter. See {@link #PARAM_ROOM_LEVEL} ...
         * @param value
         * 		the new parameter value.
         */
        void onParameterChange(android.media.audiofx.EnvironmentalReverb effect, int status, int param, int value);
    }

    /**
     * Listener used internally to receive unformatted parameter change events from AudioEffect
     * super class.
     */
    private class BaseParameterListener implements android.media.audiofx.AudioEffect.OnParameterChangeListener {
        private BaseParameterListener() {
        }

        public void onParameterChange(android.media.audiofx.AudioEffect effect, int status, byte[] param, byte[] value) {
            android.media.audiofx.EnvironmentalReverb.OnParameterChangeListener l = null;
            synchronized(mParamListenerLock) {
                if (mParamListener != null) {
                    l = mParamListener;
                }
            }
            if (l != null) {
                int p = -1;
                int v = -1;
                if (param.length == 4) {
                    p = android.media.audiofx.AudioEffect.byteArrayToInt(param, 0);
                }
                if (value.length == 2) {
                    v = ((int) (android.media.audiofx.AudioEffect.byteArrayToShort(value, 0)));
                } else
                    if (value.length == 4) {
                        v = android.media.audiofx.AudioEffect.byteArrayToInt(value, 0);
                    }

                if ((p != (-1)) && (v != (-1))) {
                    l.onParameterChange(android.media.audiofx.EnvironmentalReverb.this, status, p, v);
                }
            }
        }
    }

    /**
     * Registers an OnParameterChangeListener interface.
     *
     * @param listener
     * 		OnParameterChangeListener interface registered
     */
    public void setParameterListener(android.media.audiofx.EnvironmentalReverb.OnParameterChangeListener listener) {
        synchronized(mParamListenerLock) {
            if (mParamListener == null) {
                mParamListener = listener;
                mBaseParamListener = new android.media.audiofx.EnvironmentalReverb.BaseParameterListener();
                super.setParameterListener(mBaseParamListener);
            }
        }
    }

    /**
     * The Settings class regroups all environmental reverb parameters. It is used in
     * conjuntion with getProperties() and setProperties() methods to backup and restore
     * all parameters in a single call.
     */
    public static class Settings {
        public short roomLevel;

        public short roomHFLevel;

        public int decayTime;

        public short decayHFRatio;

        public short reflectionsLevel;

        public int reflectionsDelay;

        public short reverbLevel;

        public int reverbDelay;

        public short diffusion;

        public short density;

        public Settings() {
        }

        /**
         * Settings class constructor from a key=value; pairs formatted string. The string is
         * typically returned by Settings.toString() method.
         *
         * @throws IllegalArgumentException
         * 		if the string is not correctly formatted.
         */
        public Settings(java.lang.String settings) {
            java.util.StringTokenizer st = new java.util.StringTokenizer(settings, "=;");
            int tokens = st.countTokens();
            if (st.countTokens() != 21) {
                throw new java.lang.IllegalArgumentException("settings: " + settings);
            }
            java.lang.String key = st.nextToken();
            if (!key.equals("EnvironmentalReverb")) {
                throw new java.lang.IllegalArgumentException("invalid settings for EnvironmentalReverb: " + key);
            }
            try {
                key = st.nextToken();
                if (!key.equals("roomLevel")) {
                    throw new java.lang.IllegalArgumentException("invalid key name: " + key);
                }
                roomLevel = java.lang.Short.parseShort(st.nextToken());
                key = st.nextToken();
                if (!key.equals("roomHFLevel")) {
                    throw new java.lang.IllegalArgumentException("invalid key name: " + key);
                }
                roomHFLevel = java.lang.Short.parseShort(st.nextToken());
                key = st.nextToken();
                if (!key.equals("decayTime")) {
                    throw new java.lang.IllegalArgumentException("invalid key name: " + key);
                }
                decayTime = java.lang.Integer.parseInt(st.nextToken());
                key = st.nextToken();
                if (!key.equals("decayHFRatio")) {
                    throw new java.lang.IllegalArgumentException("invalid key name: " + key);
                }
                decayHFRatio = java.lang.Short.parseShort(st.nextToken());
                key = st.nextToken();
                if (!key.equals("reflectionsLevel")) {
                    throw new java.lang.IllegalArgumentException("invalid key name: " + key);
                }
                reflectionsLevel = java.lang.Short.parseShort(st.nextToken());
                key = st.nextToken();
                if (!key.equals("reflectionsDelay")) {
                    throw new java.lang.IllegalArgumentException("invalid key name: " + key);
                }
                reflectionsDelay = java.lang.Integer.parseInt(st.nextToken());
                key = st.nextToken();
                if (!key.equals("reverbLevel")) {
                    throw new java.lang.IllegalArgumentException("invalid key name: " + key);
                }
                reverbLevel = java.lang.Short.parseShort(st.nextToken());
                key = st.nextToken();
                if (!key.equals("reverbDelay")) {
                    throw new java.lang.IllegalArgumentException("invalid key name: " + key);
                }
                reverbDelay = java.lang.Integer.parseInt(st.nextToken());
                key = st.nextToken();
                if (!key.equals("diffusion")) {
                    throw new java.lang.IllegalArgumentException("invalid key name: " + key);
                }
                diffusion = java.lang.Short.parseShort(st.nextToken());
                key = st.nextToken();
                if (!key.equals("density")) {
                    throw new java.lang.IllegalArgumentException("invalid key name: " + key);
                }
                density = java.lang.Short.parseShort(st.nextToken());
            } catch (java.lang.NumberFormatException nfe) {
                throw new java.lang.IllegalArgumentException("invalid value for key: " + key);
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            return new java.lang.String(((((((((((((((((((("EnvironmentalReverb" + ";roomLevel=") + java.lang.Short.toString(roomLevel)) + ";roomHFLevel=") + java.lang.Short.toString(roomHFLevel)) + ";decayTime=") + java.lang.Integer.toString(decayTime)) + ";decayHFRatio=") + java.lang.Short.toString(decayHFRatio)) + ";reflectionsLevel=") + java.lang.Short.toString(reflectionsLevel)) + ";reflectionsDelay=") + java.lang.Integer.toString(reflectionsDelay)) + ";reverbLevel=") + java.lang.Short.toString(reverbLevel)) + ";reverbDelay=") + java.lang.Integer.toString(reverbDelay)) + ";diffusion=") + java.lang.Short.toString(diffusion)) + ";density=") + java.lang.Short.toString(density));
        }
    }

    // Keep this in sync with sizeof(s_reverb_settings) defined in
    // frameworks/base/include/media/EffectEnvironmentalReverbApi.h
    private static int PROPERTY_SIZE = 26;

    /**
     * Gets the environmental reverb properties. This method is useful when a snapshot of current
     * reverb settings must be saved by the application.
     *
     * @return an EnvironmentalReverb.Settings object containing all current parameters values
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public android.media.audiofx.EnvironmentalReverb.Settings getProperties() throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = new byte[android.media.audiofx.EnvironmentalReverb.PROPERTY_SIZE];
        checkStatus(getParameter(android.media.audiofx.EnvironmentalReverb.PARAM_PROPERTIES, param));
        android.media.audiofx.EnvironmentalReverb.Settings settings = new android.media.audiofx.EnvironmentalReverb.Settings();
        settings.roomLevel = android.media.audiofx.AudioEffect.byteArrayToShort(param, 0);
        settings.roomHFLevel = android.media.audiofx.AudioEffect.byteArrayToShort(param, 2);
        settings.decayTime = android.media.audiofx.AudioEffect.byteArrayToInt(param, 4);
        settings.decayHFRatio = android.media.audiofx.AudioEffect.byteArrayToShort(param, 8);
        settings.reflectionsLevel = android.media.audiofx.AudioEffect.byteArrayToShort(param, 10);
        settings.reflectionsDelay = android.media.audiofx.AudioEffect.byteArrayToInt(param, 12);
        settings.reverbLevel = android.media.audiofx.AudioEffect.byteArrayToShort(param, 16);
        settings.reverbDelay = android.media.audiofx.AudioEffect.byteArrayToInt(param, 18);
        settings.diffusion = android.media.audiofx.AudioEffect.byteArrayToShort(param, 22);
        settings.density = android.media.audiofx.AudioEffect.byteArrayToShort(param, 24);
        return settings;
    }

    /**
     * Sets the environmental reverb properties. This method is useful when reverb settings have to
     * be applied from a previous backup.
     *
     * @param settings
     * 		a EnvironmentalReverb.Settings object containing the properties to apply
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public void setProperties(android.media.audiofx.EnvironmentalReverb.Settings settings) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        byte[] param = android.media.audiofx.AudioEffect.concatArrays(android.media.audiofx.AudioEffect.shortToByteArray(settings.roomLevel), android.media.audiofx.AudioEffect.shortToByteArray(settings.roomHFLevel), android.media.audiofx.AudioEffect.intToByteArray(settings.decayTime), android.media.audiofx.AudioEffect.shortToByteArray(settings.decayHFRatio), android.media.audiofx.AudioEffect.shortToByteArray(settings.reflectionsLevel), android.media.audiofx.AudioEffect.intToByteArray(settings.reflectionsDelay), android.media.audiofx.AudioEffect.shortToByteArray(settings.reverbLevel), android.media.audiofx.AudioEffect.intToByteArray(settings.reverbDelay), android.media.audiofx.AudioEffect.shortToByteArray(settings.diffusion), android.media.audiofx.AudioEffect.shortToByteArray(settings.density));
        checkStatus(setParameter(android.media.audiofx.EnvironmentalReverb.PARAM_PROPERTIES, param));
    }
}

