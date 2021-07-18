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
 * Structure for common A/V sync params.
 *
 * Used by {@link MediaSync} {link MediaSync#getSyncParams()} and
 * {link MediaSync#setSyncParams(SyncParams)}
 * to control A/V sync behavior.
 * <p> <strong>audio adjust mode:</strong>
 * select handling of audio track when changing playback speed due to sync.
 * <ul>
 * <li> {@link SyncParams#AUDIO_ADJUST_MODE_DEFAULT}:
 *   System will determine best handling. </li>
 * <li> {@link SyncParams#AUDIO_ADJUST_MODE_STRETCH}:
 *   Change the speed of audio playback without altering its pitch.</li>
 * <li> {@link SyncParams#AUDIO_ADJUST_MODE_RESAMPLE}:
 *   Change the speed of audio playback by resampling the audio.</li>
 * </ul>
 * <p> <strong>sync source:</strong> select
 * clock source for sync.
 * <ul>
 * <li> {@link SyncParams#SYNC_SOURCE_DEFAULT}:
 *   System will determine best selection.</li>
 * <li> {@link SyncParams#SYNC_SOURCE_SYSTEM_CLOCK}:
 *   Use system clock for sync source.</li>
 * <li> {@link SyncParams#SYNC_SOURCE_AUDIO}:
 *   Use audio track for sync source.</li>
 * <li> {@link SyncParams#SYNC_SOURCE_VSYNC}:
 *   Syncronize media to vsync.</li>
 * </ul>
 * <p> <strong>tolerance:</strong> specifies the amount of allowed playback rate
 * change to keep media in sync with the sync source. The handling of this depends
 * on the sync source, but must not be negative, and must be less than one.
 * <p> <strong>frameRate:</strong> initial hint for video frame rate. Used when
 * sync source is vsync. Negative values can be used to clear a previous hint.
 */
public final class SyncParams {
    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.media.SyncParams.SYNC_SOURCE_DEFAULT, android.media.SyncParams.SYNC_SOURCE_SYSTEM_CLOCK, android.media.SyncParams.SYNC_SOURCE_AUDIO, android.media.SyncParams.SYNC_SOURCE_VSYNC })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface SyncSource {}

    /**
     * Use the default sync source (default). If media has video, the sync renders to a
     * surface that directly renders to a display, and tolerance is non zero (e.g. not
     * less than 0.001) vsync source is used for clock source.  Otherwise, if media has
     * audio, audio track is used. Finally, if media has no audio, system clock is used.
     */
    public static final int SYNC_SOURCE_DEFAULT = 0;

    /**
     * Use system monotonic clock for sync source.
     *
     * @see System#nanoTime
     */
    public static final int SYNC_SOURCE_SYSTEM_CLOCK = 1;

    /**
     * Use audio track for sync source. This requires audio data and an audio track.
     *
     * @see AudioTrack#getTimeStamp
     */
    public static final int SYNC_SOURCE_AUDIO = 2;

    /**
     * Use vsync as the sync source. This requires video data and an output surface that
     * directly renders to the display, e.g. {@link android.view.SurfaceView}
     * <p>
     * This mode allows smoother playback experience by adjusting the playback speed
     * to match the vsync rate, e.g. playing 30fps content on a 59.94Hz display.
     * When using this mode, the tolerance should be set to greater than 0 (e.g. at least
     * 1/1000), so that the playback speed can actually be adjusted.
     * <p>
     * This mode can also be used to play 25fps content on a 60Hz display using
     * a 2:3 pulldown (basically playing the content at 24fps), which results on
     * better playback experience on most devices. In this case the tolerance should be
     * at least (1/24).
     *
     * @see android.view.Choreographer.FrameCallback#doFrame
     * @see android.view.Display#getAppVsyncOffsetNanos
     */
    public static final int SYNC_SOURCE_VSYNC = 3;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.media.SyncParams.AUDIO_ADJUST_MODE_DEFAULT, android.media.SyncParams.AUDIO_ADJUST_MODE_STRETCH, android.media.SyncParams.AUDIO_ADJUST_MODE_RESAMPLE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AudioAdjustMode {}

    /**
     * System will determine best handling of audio for playback rate
     * adjustments.
     * <p>
     * Used by default. This will make audio play faster or slower as required
     * by the sync source without changing its pitch; however, system may fall
     * back to some other method (e.g. change the pitch, or mute the audio) if
     * time stretching is no longer supported for the playback rate.
     */
    public static final int AUDIO_ADJUST_MODE_DEFAULT = 0;

    /**
     * Time stretch audio when playback rate must be adjusted.
     * <p>
     * This will make audio play faster or slower as required by the sync source
     * without changing its pitch, as long as it is supported for the playback
     * rate.
     *
     * @see MediaSync#PLAYBACK_RATE_AUDIO_MODE_STRETCH
     * @see MediaPlayer#PLAYBACK_RATE_AUDIO_MODE_STRETCH
     */
    public static final int AUDIO_ADJUST_MODE_STRETCH = 1;

    /**
     * Resample audio when playback rate must be adjusted.
     * <p>
     * This will make audio play faster or slower as required by the sync source
     * by changing its pitch (making it lower to play slower, and higher to play
     * faster.)
     *
     * @see MediaSync#PLAYBACK_RATE_AUDIO_MODE_RESAMPLE
     * @see MediaPlayer#PLAYBACK_RATE_AUDIO_MODE_RESAMPLE
     */
    public static final int AUDIO_ADJUST_MODE_RESAMPLE = 2;

    // flags to indicate which params are actually set
    private static final int SET_SYNC_SOURCE = 1 << 0;

    private static final int SET_AUDIO_ADJUST_MODE = 1 << 1;

    private static final int SET_TOLERANCE = 1 << 2;

    private static final int SET_FRAME_RATE = 1 << 3;

    private int mSet = 0;

    // params
    private int mAudioAdjustMode = android.media.SyncParams.AUDIO_ADJUST_MODE_DEFAULT;

    private int mSyncSource = android.media.SyncParams.SYNC_SOURCE_DEFAULT;

    private float mTolerance = 0.0F;

    private float mFrameRate = 0.0F;

    /**
     * Allows defaults to be returned for properties not set.
     * Otherwise a {@link java.lang.IllegalArgumentException} exception
     * is raised when getting those properties
     * which have defaults but have never been set.
     *
     * @return this <code>SyncParams</code> instance.
     */
    public android.media.SyncParams allowDefaults() {
        mSet |= (android.media.SyncParams.SET_SYNC_SOURCE | android.media.SyncParams.SET_AUDIO_ADJUST_MODE) | android.media.SyncParams.SET_TOLERANCE;
        return this;
    }

    /**
     * Sets the audio adjust mode.
     *
     * @param audioAdjustMode
     * 		
     * @return this <code>SyncParams</code> instance.
     */
    public android.media.SyncParams setAudioAdjustMode(@android.media.SyncParams.AudioAdjustMode
    int audioAdjustMode) {
        mAudioAdjustMode = audioAdjustMode;
        mSet |= android.media.SyncParams.SET_AUDIO_ADJUST_MODE;
        return this;
    }

    /**
     * Retrieves the audio adjust mode.
     *
     * @return audio adjust mode
     * @throws IllegalStateException
     * 		if the audio adjust mode is not set.
     */
    @android.media.SyncParams.AudioAdjustMode
    public int getAudioAdjustMode() {
        if ((mSet & android.media.SyncParams.SET_AUDIO_ADJUST_MODE) == 0) {
            throw new java.lang.IllegalStateException("audio adjust mode not set");
        }
        return mAudioAdjustMode;
    }

    /**
     * Sets the sync source.
     *
     * @param syncSource
     * 		
     * @return this <code>SyncParams</code> instance.
     */
    public android.media.SyncParams setSyncSource(@android.media.SyncParams.SyncSource
    int syncSource) {
        mSyncSource = syncSource;
        mSet |= android.media.SyncParams.SET_SYNC_SOURCE;
        return this;
    }

    /**
     * Retrieves the sync source.
     *
     * @return sync source
     * @throws IllegalStateException
     * 		if the sync source is not set.
     */
    @android.media.SyncParams.SyncSource
    public int getSyncSource() {
        if ((mSet & android.media.SyncParams.SET_SYNC_SOURCE) == 0) {
            throw new java.lang.IllegalStateException("sync source not set");
        }
        return mSyncSource;
    }

    /**
     * Sets the tolerance. The default tolerance is platform specific, but is never more than 1/24.
     *
     * @param tolerance
     * 		A non-negative number representing
     * 		the maximum deviation of the playback rate from the playback rate
     * 		set. ({@code abs(actual_rate - set_rate) / set_rate})
     * @return this <code>SyncParams</code> instance.
     * @throws InvalidArgumentException
     * 		if the tolerance is negative, or not less than one
     */
    public android.media.SyncParams setTolerance(float tolerance) {
        if ((tolerance < 0.0F) || (tolerance >= 1.0F)) {
            throw new java.lang.IllegalArgumentException("tolerance must be less than one and non-negative");
        }
        mTolerance = tolerance;
        mSet |= android.media.SyncParams.SET_TOLERANCE;
        return this;
    }

    /**
     * Retrieves the tolerance factor.
     *
     * @return tolerance factor. A non-negative number representing
    the maximum deviation of the playback rate from the playback rate
    set. ({@code abs(actual_rate - set_rate) / set_rate})
     * @throws IllegalStateException
     * 		if tolerance is not set.
     */
    public float getTolerance() {
        if ((mSet & android.media.SyncParams.SET_TOLERANCE) == 0) {
            throw new java.lang.IllegalStateException("tolerance not set");
        }
        return mTolerance;
    }

    /**
     * Sets the video frame rate hint to be used. By default the frame rate is unspecified.
     *
     * @param frameRate
     * 		A non-negative number used as an initial hint on
     * 		the video frame rate to be used when using vsync as the sync source. A negative
     * 		number is used to clear a previous hint.
     * @return this <code>SyncParams</code> instance.
     */
    public android.media.SyncParams setFrameRate(float frameRate) {
        mFrameRate = frameRate;
        mSet |= android.media.SyncParams.SET_FRAME_RATE;
        return this;
    }

    /**
     * Retrieves the video frame rate hint.
     *
     * @return frame rate factor. A non-negative number representing
    the maximum deviation of the playback rate from the playback rate
    set. ({@code abs(actual_rate - set_rate) / set_rate}), or a negative
    number representing the desire to clear a previous hint using these params.
     * @throws IllegalStateException
     * 		if frame rate is not set.
     */
    public float getFrameRate() {
        if ((mSet & android.media.SyncParams.SET_FRAME_RATE) == 0) {
            throw new java.lang.IllegalStateException("frame rate not set");
        }
        return mFrameRate;
    }
}

