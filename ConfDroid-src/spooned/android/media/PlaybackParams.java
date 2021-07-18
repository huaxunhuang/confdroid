/**
 * Copyright 2015 The Android Open Source Project
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
 * Structure for common playback params.
 *
 * Used by {@link AudioTrack} {@link AudioTrack#getPlaybackParams()} and
 * {@link AudioTrack#setPlaybackParams(PlaybackParams)}
 * to control playback behavior.
 * <p> <strong>audio fallback mode:</strong>
 * select out-of-range parameter handling.
 * <ul>
 * <li> {@link PlaybackParams#AUDIO_FALLBACK_MODE_DEFAULT}:
 *   System will determine best handling. </li>
 * <li> {@link PlaybackParams#AUDIO_FALLBACK_MODE_MUTE}:
 *   Play silence for params normally out of range.</li>
 * <li> {@link PlaybackParams#AUDIO_FALLBACK_MODE_FAIL}:
 *   Return {@link java.lang.IllegalArgumentException} from
 *   <code>AudioTrack.setPlaybackParams(PlaybackParams)</code>.</li>
 * </ul>
 * <p> <strong>pitch:</strong> increases or decreases the tonal frequency of the audio content.
 * It is expressed as a multiplicative factor, where normal pitch is 1.0f.
 * <p> <strong>speed:</strong> increases or decreases the time to
 * play back a set of audio or video frames.
 * It is expressed as a multiplicative factor, where normal speed is 1.0f.
 * <p> Different combinations of speed and pitch may be used for audio playback;
 * some common ones:
 * <ul>
 * <li> <em>Pitch equals 1.0f.</em> Speed change will be done with pitch preserved,
 * often called <em>timestretching</em>.</li>
 * <li> <em>Pitch equals speed.</em> Speed change will be done by <em>resampling</em>,
 * similar to {@link AudioTrack#setPlaybackRate(int)}.</li>
 * </ul>
 */
public final class PlaybackParams implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.media.PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT, android.media.PlaybackParams.AUDIO_FALLBACK_MODE_MUTE, android.media.PlaybackParams.AUDIO_FALLBACK_MODE_FAIL })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AudioFallbackMode {}

    public static final int AUDIO_FALLBACK_MODE_DEFAULT = 0;

    public static final int AUDIO_FALLBACK_MODE_MUTE = 1;

    public static final int AUDIO_FALLBACK_MODE_FAIL = 2;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.media.PlaybackParams.AUDIO_STRETCH_MODE_DEFAULT, android.media.PlaybackParams.AUDIO_STRETCH_MODE_VOICE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AudioStretchMode {}

    /**
     *
     *
     * @unknown 
     */
    public static final int AUDIO_STRETCH_MODE_DEFAULT = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int AUDIO_STRETCH_MODE_VOICE = 1;

    // flags to indicate which params are actually set
    private static final int SET_SPEED = 1 << 0;

    private static final int SET_PITCH = 1 << 1;

    private static final int SET_AUDIO_FALLBACK_MODE = 1 << 2;

    private static final int SET_AUDIO_STRETCH_MODE = 1 << 3;

    private int mSet = 0;

    // params
    private int mAudioFallbackMode = android.media.PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT;

    private int mAudioStretchMode = android.media.PlaybackParams.AUDIO_STRETCH_MODE_DEFAULT;

    private float mPitch = 1.0F;

    private float mSpeed = 1.0F;

    public PlaybackParams() {
    }

    private PlaybackParams(android.os.Parcel in) {
        mSet = in.readInt();
        mAudioFallbackMode = in.readInt();
        mAudioStretchMode = in.readInt();
        mPitch = in.readFloat();
        if (mPitch < 0.0F) {
            mPitch = 0.0F;
        }
        mSpeed = in.readFloat();
    }

    /**
     * Allows defaults to be returned for properties not set.
     * Otherwise a {@link java.lang.IllegalArgumentException} exception
     * is raised when getting those properties
     * which have defaults but have never been set.
     *
     * @return this <code>PlaybackParams</code> instance.
     */
    public android.media.PlaybackParams allowDefaults() {
        mSet |= ((android.media.PlaybackParams.SET_AUDIO_FALLBACK_MODE | android.media.PlaybackParams.SET_AUDIO_STRETCH_MODE) | android.media.PlaybackParams.SET_PITCH) | android.media.PlaybackParams.SET_SPEED;
        return this;
    }

    /**
     * Sets the audio fallback mode.
     *
     * @param audioFallbackMode
     * 		
     * @return this <code>PlaybackParams</code> instance.
     */
    public android.media.PlaybackParams setAudioFallbackMode(@android.media.PlaybackParams.AudioFallbackMode
    int audioFallbackMode) {
        mAudioFallbackMode = audioFallbackMode;
        mSet |= android.media.PlaybackParams.SET_AUDIO_FALLBACK_MODE;
        return this;
    }

    /**
     * Retrieves the audio fallback mode.
     *
     * @return audio fallback mode
     * @throws IllegalStateException
     * 		if the audio fallback mode is not set.
     */
    @android.media.PlaybackParams.AudioFallbackMode
    public int getAudioFallbackMode() {
        if ((mSet & android.media.PlaybackParams.SET_AUDIO_FALLBACK_MODE) == 0) {
            throw new java.lang.IllegalStateException("audio fallback mode not set");
        }
        return mAudioFallbackMode;
    }

    /**
     *
     *
     * @unknown Sets the audio stretch mode.
     * @param audioStretchMode
     * 		
     * @return this <code>PlaybackParams</code> instance.
     */
    public android.media.PlaybackParams setAudioStretchMode(@android.media.PlaybackParams.AudioStretchMode
    int audioStretchMode) {
        mAudioStretchMode = audioStretchMode;
        mSet |= android.media.PlaybackParams.SET_AUDIO_STRETCH_MODE;
        return this;
    }

    /**
     *
     *
     * @unknown Retrieves the audio stretch mode.
     * @return audio stretch mode
     * @throws IllegalStateException
     * 		if the audio stretch mode is not set.
     */
    @android.media.PlaybackParams.AudioStretchMode
    public int getAudioStretchMode() {
        if ((mSet & android.media.PlaybackParams.SET_AUDIO_STRETCH_MODE) == 0) {
            throw new java.lang.IllegalStateException("audio stretch mode not set");
        }
        return mAudioStretchMode;
    }

    /**
     * Sets the pitch factor.
     *
     * @param pitch
     * 		
     * @return this <code>PlaybackParams</code> instance.
     * @throws InvalidArgumentException
     * 		if the pitch is negative
     */
    public android.media.PlaybackParams setPitch(float pitch) {
        if (pitch < 0.0F) {
            throw new java.lang.IllegalArgumentException("pitch must not be negative");
        }
        mPitch = pitch;
        mSet |= android.media.PlaybackParams.SET_PITCH;
        return this;
    }

    /**
     * Retrieves the pitch factor.
     *
     * @return pitch
     * @throws IllegalStateException
     * 		if pitch is not set.
     */
    public float getPitch() {
        if ((mSet & android.media.PlaybackParams.SET_PITCH) == 0) {
            throw new java.lang.IllegalStateException("pitch not set");
        }
        return mPitch;
    }

    /**
     * Sets the speed factor.
     *
     * @param speed
     * 		
     * @return this <code>PlaybackParams</code> instance.
     */
    public android.media.PlaybackParams setSpeed(float speed) {
        mSpeed = speed;
        mSet |= android.media.PlaybackParams.SET_SPEED;
        return this;
    }

    /**
     * Retrieves the speed factor.
     *
     * @return speed
     * @throws IllegalStateException
     * 		if speed is not set.
     */
    public float getSpeed() {
        if ((mSet & android.media.PlaybackParams.SET_SPEED) == 0) {
            throw new java.lang.IllegalStateException("speed not set");
        }
        return mSpeed;
    }

    public static final android.os.Parcelable.Creator<android.media.PlaybackParams> CREATOR = new android.os.Parcelable.Creator<android.media.PlaybackParams>() {
        @java.lang.Override
        public android.media.PlaybackParams createFromParcel(android.os.Parcel in) {
            return new android.media.PlaybackParams(in);
        }

        @java.lang.Override
        public android.media.PlaybackParams[] newArray(int size) {
            return new android.media.PlaybackParams[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mSet);
        dest.writeInt(mAudioFallbackMode);
        dest.writeInt(mAudioStretchMode);
        dest.writeFloat(mPitch);
        dest.writeFloat(mSpeed);
    }
}

