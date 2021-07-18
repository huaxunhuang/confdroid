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
 * Bass boost is an audio effect to boost or amplify low frequencies of the sound. It is comparable
 * to a simple equalizer but limited to one band amplification in the low frequency range.
 * <p>An application creates a BassBoost object to instantiate and control a bass boost engine in
 * the audio framework.
 * <p>The methods, parameter types and units exposed by the BassBoost implementation are directly
 * mapping those defined by the OpenSL ES 1.0.1 Specification (http://www.khronos.org/opensles/)
 * for the SLBassBoostItf interface. Please refer to this specification for more details.
 * <p>To attach the BassBoost to a particular AudioTrack or MediaPlayer, specify the audio session
 * ID of this AudioTrack or MediaPlayer when constructing the BassBoost.
 * <p>NOTE: attaching a BassBoost to the global audio output mix by use of session 0 is deprecated.
 * <p>See {@link android.media.MediaPlayer#getAudioSessionId()} for details on audio sessions.
 * <p>See {@link android.media.audiofx.AudioEffect} class for more details on
 * controlling audio effects.
 */
public class BassBoost extends android.media.audiofx.AudioEffect {
    private static final java.lang.String TAG = "BassBoost";

    // These constants must be synchronized with those in
    // frameworks/base/include/media/EffectBassBoostApi.h
    /**
     * Is strength parameter supported by bass boost engine. Parameter ID for getParameter().
     */
    public static final int PARAM_STRENGTH_SUPPORTED = 0;

    /**
     * Bass boost effect strength. Parameter ID for
     * {@link android.media.audiofx.BassBoost.OnParameterChangeListener}
     */
    public static final int PARAM_STRENGTH = 1;

    /**
     * Indicates if strength parameter is supported by the bass boost engine
     */
    private boolean mStrengthSupported = false;

    /**
     * Registered listener for parameter changes.
     */
    private android.media.audiofx.BassBoost.OnParameterChangeListener mParamListener = null;

    /**
     * Listener used internally to to receive raw parameter change event from AudioEffect super class
     */
    private android.media.audiofx.BassBoost.BaseParameterListener mBaseParamListener = null;

    /**
     * Lock for access to mParamListener
     */
    private final java.lang.Object mParamListenerLock = new java.lang.Object();

    /**
     * Class constructor.
     *
     * @param priority
     * 		the priority level requested by the application for controlling the BassBoost
     * 		engine. As the same engine can be shared by several applications, this parameter indicates
     * 		how much the requesting application needs control of effect parameters. The normal priority
     * 		is 0, above normal is a positive number, below normal a negative number.
     * @param audioSession
     * 		system wide unique audio session identifier. The BassBoost will be
     * 		attached to the MediaPlayer or AudioTrack in the same audio session.
     * @throws java.lang.IllegalStateException
     * 		
     * @throws java.lang.IllegalArgumentException
     * 		
     * @throws java.lang.UnsupportedOperationException
     * 		
     * @throws java.lang.RuntimeException
     * 		
     */
    public BassBoost(int priority, int audioSession) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.RuntimeException, java.lang.UnsupportedOperationException {
        super(android.media.audiofx.AudioEffect.EFFECT_TYPE_BASS_BOOST, android.media.audiofx.AudioEffect.EFFECT_TYPE_NULL, priority, audioSession);
        if (audioSession == 0) {
            android.util.Log.w(android.media.audiofx.BassBoost.TAG, "WARNING: attaching a BassBoost to global output mix is deprecated!");
        }
        int[] value = new int[1];
        checkStatus(getParameter(android.media.audiofx.BassBoost.PARAM_STRENGTH_SUPPORTED, value));
        mStrengthSupported = value[0] != 0;
    }

    /**
     * Indicates whether setting strength is supported. If this method returns false, only one
     * strength is supported and the setStrength() method always rounds to that value.
     *
     * @return true is strength parameter is supported, false otherwise
     */
    public boolean getStrengthSupported() {
        return mStrengthSupported;
    }

    /**
     * Sets the strength of the bass boost effect. If the implementation does not support per mille
     * accuracy for setting the strength, it is allowed to round the given strength to the nearest
     * supported value. You can use the {@link #getRoundedStrength()} method to query the
     * (possibly rounded) value that was actually set.
     *
     * @param strength
     * 		strength of the effect. The valid range for strength strength is [0, 1000],
     * 		where 0 per mille designates the mildest effect and 1000 per mille designates the strongest.
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public void setStrength(short strength) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        checkStatus(setParameter(android.media.audiofx.BassBoost.PARAM_STRENGTH, strength));
    }

    /**
     * Gets the current strength of the effect.
     *
     * @return the strength of the effect. The valid range for strength is [0, 1000], where 0 per
    mille designates the mildest effect and 1000 per mille the strongest
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public short getRoundedStrength() throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        short[] value = new short[1];
        checkStatus(getParameter(android.media.audiofx.BassBoost.PARAM_STRENGTH, value));
        return value[0];
    }

    /**
     * The OnParameterChangeListener interface defines a method called by the BassBoost when a
     * parameter value has changed.
     */
    public interface OnParameterChangeListener {
        /**
         * Method called when a parameter value has changed. The method is called only if the
         * parameter was changed by another application having the control of the same
         * BassBoost engine.
         *
         * @param effect
         * 		the BassBoost on which the interface is registered.
         * @param status
         * 		status of the set parameter operation.
         * @param param
         * 		ID of the modified parameter. See {@link #PARAM_STRENGTH} ...
         * @param value
         * 		the new parameter value.
         */
        void onParameterChange(android.media.audiofx.BassBoost effect, int status, int param, short value);
    }

    /**
     * Listener used internally to receive unformatted parameter change events from AudioEffect
     * super class.
     */
    private class BaseParameterListener implements android.media.audiofx.AudioEffect.OnParameterChangeListener {
        private BaseParameterListener() {
        }

        public void onParameterChange(android.media.audiofx.AudioEffect effect, int status, byte[] param, byte[] value) {
            android.media.audiofx.BassBoost.OnParameterChangeListener l = null;
            synchronized(mParamListenerLock) {
                if (mParamListener != null) {
                    l = mParamListener;
                }
            }
            if (l != null) {
                int p = -1;
                short v = -1;
                if (param.length == 4) {
                    p = android.media.audiofx.AudioEffect.byteArrayToInt(param, 0);
                }
                if (value.length == 2) {
                    v = android.media.audiofx.AudioEffect.byteArrayToShort(value, 0);
                }
                if ((p != (-1)) && (v != (-1))) {
                    l.onParameterChange(android.media.audiofx.BassBoost.this, status, p, v);
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
    public void setParameterListener(android.media.audiofx.BassBoost.OnParameterChangeListener listener) {
        synchronized(mParamListenerLock) {
            if (mParamListener == null) {
                mParamListener = listener;
                mBaseParamListener = new android.media.audiofx.BassBoost.BaseParameterListener();
                super.setParameterListener(mBaseParamListener);
            }
        }
    }

    /**
     * The Settings class regroups all bass boost parameters. It is used in
     * conjuntion with getProperties() and setProperties() methods to backup and restore
     * all parameters in a single call.
     */
    public static class Settings {
        public short strength;

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
            if (st.countTokens() != 3) {
                throw new java.lang.IllegalArgumentException("settings: " + settings);
            }
            java.lang.String key = st.nextToken();
            if (!key.equals("BassBoost")) {
                throw new java.lang.IllegalArgumentException("invalid settings for BassBoost: " + key);
            }
            try {
                key = st.nextToken();
                if (!key.equals("strength")) {
                    throw new java.lang.IllegalArgumentException("invalid key name: " + key);
                }
                strength = java.lang.Short.parseShort(st.nextToken());
            } catch (java.lang.NumberFormatException nfe) {
                throw new java.lang.IllegalArgumentException("invalid value for key: " + key);
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.String str = new java.lang.String(("BassBoost" + ";strength=") + java.lang.Short.toString(strength));
            return str;
        }
    }

    /**
     * Gets the bass boost properties. This method is useful when a snapshot of current
     * bass boost settings must be saved by the application.
     *
     * @return a BassBoost.Settings object containing all current parameters values
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public android.media.audiofx.BassBoost.Settings getProperties() throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        android.media.audiofx.BassBoost.Settings settings = new android.media.audiofx.BassBoost.Settings();
        short[] value = new short[1];
        checkStatus(getParameter(android.media.audiofx.BassBoost.PARAM_STRENGTH, value));
        settings.strength = value[0];
        return settings;
    }

    /**
     * Sets the bass boost properties. This method is useful when bass boost settings have to
     * be applied from a previous backup.
     *
     * @param settings
     * 		a BassBoost.Settings object containing the properties to apply
     * @throws IllegalStateException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws UnsupportedOperationException
     * 		
     */
    public void setProperties(android.media.audiofx.BassBoost.Settings settings) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.UnsupportedOperationException {
        checkStatus(setParameter(android.media.audiofx.BassBoost.PARAM_STRENGTH, settings.strength));
    }
}

