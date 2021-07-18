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
 * A class to encapsulate a collection of attributes describing information about an audio
 * stream.
 * <p><code>AudioAttributes</code> supersede the notion of stream types (see for instance
 * {@link AudioManager#STREAM_MUSIC} or {@link AudioManager#STREAM_ALARM}) for defining the
 * behavior of audio playback. Attributes allow an application to specify more information than is
 * conveyed in a stream type by allowing the application to define:
 * <ul>
 * <li>usage: "why" you are playing a sound, what is this sound used for. This is achieved with
 *     the "usage" information. Examples of usage are {@link #USAGE_MEDIA} and {@link #USAGE_ALARM}.
 *     These two examples are the closest to stream types, but more detailed use cases are
 *     available. Usage information is more expressive than a stream type, and allows certain
 *     platforms or routing policies to use this information for more refined volume or routing
 *     decisions. Usage is the most important information to supply in <code>AudioAttributes</code>
 *     and it is recommended to build any instance with this information supplied, see
 *     {@link AudioAttributes.Builder} for exceptions.</li>
 * <li>content type: "what" you are playing. The content type expresses the general category of
 *     the content. This information is optional. But in case it is known (for instance
 *     {@link #CONTENT_TYPE_MOVIE} for a movie streaming service or {@link #CONTENT_TYPE_MUSIC} for
 *     a music playback application) this information might be used by the audio framework to
 *     selectively configure some audio post-processing blocks.</li>
 * <li>flags: "how" is playback to be affected, see the flag definitions for the specific playback
 *     behaviors they control. </li>
 * </ul>
 * <p><code>AudioAttributes</code> are used for example in one of the {@link AudioTrack}
 * constructors (see {@link AudioTrack#AudioTrack(AudioAttributes, AudioFormat, int, int, int)}),
 * to configure a {@link MediaPlayer}
 * (see {@link MediaPlayer#setAudioAttributes(AudioAttributes)} or a
 * {@link android.app.Notification} (see {@link android.app.Notification#audioAttributes}). An
 * <code>AudioAttributes</code> instance is built through its builder,
 * {@link AudioAttributes.Builder}.
 */
public final class AudioAttributes implements android.os.Parcelable {
    private static final java.lang.String TAG = "AudioAttributes";

    /**
     * Content type value to use when the content type is unknown, or other than the ones defined.
     */
    public static final int CONTENT_TYPE_UNKNOWN = 0;

    /**
     * Content type value to use when the content type is speech.
     */
    public static final int CONTENT_TYPE_SPEECH = 1;

    /**
     * Content type value to use when the content type is music.
     */
    public static final int CONTENT_TYPE_MUSIC = 2;

    /**
     * Content type value to use when the content type is a soundtrack, typically accompanying
     * a movie or TV program.
     */
    public static final int CONTENT_TYPE_MOVIE = 3;

    /**
     * Content type value to use when the content type is a sound used to accompany a user
     * action, such as a beep or sound effect expressing a key click, or event, such as the
     * type of a sound for a bonus being received in a game. These sounds are mostly synthesized
     * or short Foley sounds.
     */
    public static final int CONTENT_TYPE_SONIFICATION = 4;

    /**
     * Usage value to use when the usage is unknown.
     */
    public static final int USAGE_UNKNOWN = 0;

    /**
     * Usage value to use when the usage is media, such as music, or movie
     * soundtracks.
     */
    public static final int USAGE_MEDIA = 1;

    /**
     * Usage value to use when the usage is voice communications, such as telephony
     * or VoIP.
     */
    public static final int USAGE_VOICE_COMMUNICATION = 2;

    /**
     * Usage value to use when the usage is in-call signalling, such as with
     * a "busy" beep, or DTMF tones.
     */
    public static final int USAGE_VOICE_COMMUNICATION_SIGNALLING = 3;

    /**
     * Usage value to use when the usage is an alarm (e.g. wake-up alarm).
     */
    public static final int USAGE_ALARM = 4;

    /**
     * Usage value to use when the usage is notification. See other
     * notification usages for more specialized uses.
     */
    public static final int USAGE_NOTIFICATION = 5;

    /**
     * Usage value to use when the usage is telephony ringtone.
     */
    public static final int USAGE_NOTIFICATION_RINGTONE = 6;

    /**
     * Usage value to use when the usage is a request to enter/end a
     * communication, such as a VoIP communication or video-conference.
     */
    public static final int USAGE_NOTIFICATION_COMMUNICATION_REQUEST = 7;

    /**
     * Usage value to use when the usage is notification for an "instant"
     * communication such as a chat, or SMS.
     */
    public static final int USAGE_NOTIFICATION_COMMUNICATION_INSTANT = 8;

    /**
     * Usage value to use when the usage is notification for a
     * non-immediate type of communication such as e-mail.
     */
    public static final int USAGE_NOTIFICATION_COMMUNICATION_DELAYED = 9;

    /**
     * Usage value to use when the usage is to attract the user's attention,
     * such as a reminder or low battery warning.
     */
    public static final int USAGE_NOTIFICATION_EVENT = 10;

    /**
     * Usage value to use when the usage is for accessibility, such as with
     * a screen reader.
     */
    public static final int USAGE_ASSISTANCE_ACCESSIBILITY = 11;

    /**
     * Usage value to use when the usage is driving or navigation directions.
     */
    public static final int USAGE_ASSISTANCE_NAVIGATION_GUIDANCE = 12;

    /**
     * Usage value to use when the usage is sonification, such as  with user
     * interface sounds.
     */
    public static final int USAGE_ASSISTANCE_SONIFICATION = 13;

    /**
     * Usage value to use when the usage is for game audio.
     */
    public static final int USAGE_GAME = 14;

    /**
     *
     *
     * @unknown Usage value to use when feeding audio to the platform and replacing "traditional" audio
    source, such as audio capture devices.
     */
    public static final int USAGE_VIRTUAL_SOURCE = 15;

    /**
     * IMPORTANT: when adding new usage types, add them to SDK_USAGES and update SUPPRESSIBLE_USAGES
     *            if applicable.
     */
    /**
     *
     *
     * @unknown Denotes a usage for notifications that do not expect immediate intervention from the user,
    will be muted when the Zen mode disables notifications
     * @see #SUPPRESSIBLE_USAGES
     */
    public static final int SUPPRESSIBLE_NOTIFICATION = 1;

    /**
     *
     *
     * @unknown Denotes a usage for notifications that do expect immediate intervention from the user,
    will be muted when the Zen mode disables calls
     * @see #SUPPRESSIBLE_USAGES
     */
    public static final int SUPPRESSIBLE_CALL = 2;

    /**
     *
     *
     * @unknown Array of all usage types for calls and notifications to assign the suppression behavior,
    used by the Zen mode restrictions.
     * @see com.android.server.notification.ZenModeHelper
     */
    public static final android.util.SparseIntArray SUPPRESSIBLE_USAGES;

    static {
        SUPPRESSIBLE_USAGES = new android.util.SparseIntArray();
        android.media.AudioAttributes.SUPPRESSIBLE_USAGES.put(android.media.AudioAttributes.USAGE_NOTIFICATION, android.media.AudioAttributes.SUPPRESSIBLE_NOTIFICATION);
        android.media.AudioAttributes.SUPPRESSIBLE_USAGES.put(android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE, android.media.AudioAttributes.SUPPRESSIBLE_CALL);
        android.media.AudioAttributes.SUPPRESSIBLE_USAGES.put(android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_REQUEST, android.media.AudioAttributes.SUPPRESSIBLE_CALL);
        android.media.AudioAttributes.SUPPRESSIBLE_USAGES.put(android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT, android.media.AudioAttributes.SUPPRESSIBLE_NOTIFICATION);
        android.media.AudioAttributes.SUPPRESSIBLE_USAGES.put(android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_DELAYED, android.media.AudioAttributes.SUPPRESSIBLE_NOTIFICATION);
        android.media.AudioAttributes.SUPPRESSIBLE_USAGES.put(android.media.AudioAttributes.USAGE_NOTIFICATION_EVENT, android.media.AudioAttributes.SUPPRESSIBLE_NOTIFICATION);
    }

    /**
     *
     *
     * @unknown Array of all usage types exposed in the SDK that applications can use.
     */
    public static final int[] SDK_USAGES = new int[]{ android.media.AudioAttributes.USAGE_UNKNOWN, android.media.AudioAttributes.USAGE_MEDIA, android.media.AudioAttributes.USAGE_VOICE_COMMUNICATION, android.media.AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING, android.media.AudioAttributes.USAGE_ALARM, android.media.AudioAttributes.USAGE_NOTIFICATION, android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE, android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_REQUEST, android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT, android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_DELAYED, android.media.AudioAttributes.USAGE_NOTIFICATION_EVENT, android.media.AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY, android.media.AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE, android.media.AudioAttributes.USAGE_ASSISTANCE_SONIFICATION, android.media.AudioAttributes.USAGE_GAME };

    /**
     * Flag defining a behavior where the audibility of the sound will be ensured by the system.
     */
    public static final int FLAG_AUDIBILITY_ENFORCED = 0x1 << 0;

    /**
     *
     *
     * @unknown Flag defining a behavior where the playback of the sound is ensured without
    degradation only when going to a secure sink.
     */
    // FIXME not guaranteed yet
    // TODO  add in FLAG_ALL_PUBLIC when supported and in public API
    public static final int FLAG_SECURE = 0x1 << 1;

    /**
     *
     *
     * @unknown Flag to enable when the stream is associated with SCO usage.
    Internal use only for dealing with legacy STREAM_BLUETOOTH_SCO
     */
    public static final int FLAG_SCO = 0x1 << 2;

    /**
     *
     *
     * @unknown Flag defining a behavior where the system ensures that the playback of the sound will
    be compatible with its use as a broadcast for surrounding people and/or devices.
    Ensures audibility with no or minimal post-processing applied.
     */
    @android.annotation.SystemApi
    public static final int FLAG_BEACON = 0x1 << 3;

    /**
     * Flag requesting the use of an output stream supporting hardware A/V synchronization.
     */
    public static final int FLAG_HW_AV_SYNC = 0x1 << 4;

    /**
     *
     *
     * @unknown Flag requesting capture from the source used for hardware hotword detection.
    To be used with capture preset MediaRecorder.AudioSource.HOTWORD or
    MediaRecorder.AudioSource.VOICE_RECOGNITION.
     */
    @android.annotation.SystemApi
    public static final int FLAG_HW_HOTWORD = 0x1 << 5;

    /**
     *
     *
     * @unknown Flag requesting audible playback even under limited interruptions.
     */
    @android.annotation.SystemApi
    public static final int FLAG_BYPASS_INTERRUPTION_POLICY = 0x1 << 6;

    /**
     *
     *
     * @unknown Flag requesting audible playback even when the underlying stream is muted.
     */
    @android.annotation.SystemApi
    public static final int FLAG_BYPASS_MUTE = 0x1 << 7;

    /**
     * Flag requesting a low latency path when creating an AudioTrack.
     * When using this flag, the sample rate must match the native sample rate
     * of the device. Effects processing is also unavailable.
     *
     * Note that if this flag is used without specifying a bufferSizeInBytes then the
     * AudioTrack's actual buffer size may be too small. It is recommended that a fairly
     * large buffer should be specified when the AudioTrack is created.
     * Then the actual size can be reduced by calling
     * {@link AudioTrack#setBufferSizeInFrames(int)}. The buffer size can be optimized
     * by lowering it after each write() call until the audio glitches, which is detected by calling
     * {@link AudioTrack#getUnderrunCount()}. Then the buffer size can be increased
     * until there are no glitches.
     * This tuning step should be done while playing silence.
     * This technique provides a compromise between latency and glitch rate.
     */
    public static final int FLAG_LOW_LATENCY = 0x1 << 8;

    private static final int FLAG_ALL = (((((((android.media.AudioAttributes.FLAG_AUDIBILITY_ENFORCED | android.media.AudioAttributes.FLAG_SECURE) | android.media.AudioAttributes.FLAG_SCO) | android.media.AudioAttributes.FLAG_BEACON) | android.media.AudioAttributes.FLAG_HW_AV_SYNC) | android.media.AudioAttributes.FLAG_HW_HOTWORD) | android.media.AudioAttributes.FLAG_BYPASS_INTERRUPTION_POLICY) | android.media.AudioAttributes.FLAG_BYPASS_MUTE) | android.media.AudioAttributes.FLAG_LOW_LATENCY;

    private static final int FLAG_ALL_PUBLIC = (android.media.AudioAttributes.FLAG_AUDIBILITY_ENFORCED | android.media.AudioAttributes.FLAG_HW_AV_SYNC) | android.media.AudioAttributes.FLAG_LOW_LATENCY;

    private int mUsage = android.media.AudioAttributes.USAGE_UNKNOWN;

    private int mContentType = android.media.AudioAttributes.CONTENT_TYPE_UNKNOWN;

    private int mSource = android.media.MediaRecorder.AudioSource.AUDIO_SOURCE_INVALID;

    private int mFlags = 0x0;

    private java.util.HashSet<java.lang.String> mTags;

    private java.lang.String mFormattedTags;

    private android.os.Bundle mBundle;// lazy-initialized, may be null


    private AudioAttributes() {
    }

    /**
     * Return the content type.
     *
     * @return one of the values that can be set in {@link Builder#setContentType(int)}
     */
    public int getContentType() {
        return mContentType;
    }

    /**
     * Return the usage.
     *
     * @return one of the values that can be set in {@link Builder#setUsage(int)}
     */
    public int getUsage() {
        return mUsage;
    }

    /**
     *
     *
     * @unknown Return the capture preset.
     * @return one of the values that can be set in {@link Builder#setCapturePreset(int)} or a
    negative value if none has been set.
     */
    @android.annotation.SystemApi
    public int getCapturePreset() {
        return mSource;
    }

    /**
     * Return the flags.
     *
     * @return a combined mask of all flags
     */
    public int getFlags() {
        // only return the flags that are public
        return mFlags & android.media.AudioAttributes.FLAG_ALL_PUBLIC;
    }

    /**
     *
     *
     * @unknown Return all the flags, even the non-public ones.
    Internal use only
     * @return a combined mask of all flags
     */
    @android.annotation.SystemApi
    public int getAllFlags() {
        return mFlags & android.media.AudioAttributes.FLAG_ALL;
    }

    /**
     *
     *
     * @unknown Return the Bundle of data.
     * @return a copy of the Bundle for this instance, may be null.
     */
    @android.annotation.SystemApi
    public android.os.Bundle getBundle() {
        if (mBundle == null) {
            return mBundle;
        } else {
            return new android.os.Bundle(mBundle);
        }
    }

    /**
     *
     *
     * @unknown Return the set of tags.
     * @return a read-only set of all tags stored as strings.
     */
    public java.util.Set<java.lang.String> getTags() {
        return java.util.Collections.unmodifiableSet(mTags);
    }

    /**
     * Builder class for {@link AudioAttributes} objects.
     * <p> Here is an example where <code>Builder</code> is used to define the
     * {@link AudioAttributes} to be used by a new <code>AudioTrack</code> instance:
     *
     * <pre class="prettyprint">
     * AudioTrack myTrack = new AudioTrack(
     *         new AudioAttributes.Builder()
     *             .setUsage(AudioAttributes.USAGE_MEDIA)
     *             .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
     *             .build(),
     *         myFormat, myBuffSize, AudioTrack.MODE_STREAM, mySession);
     * </pre>
     *
     * <p>By default all types of information (usage, content type, flags) conveyed by an
     * <code>AudioAttributes</code> instance are set to "unknown". Unknown information will be
     * interpreted as a default value that is dependent on the context of use, for instance a
     * {@link MediaPlayer} will use a default usage of {@link AudioAttributes#USAGE_MEDIA}.
     */
    public static class Builder {
        private int mUsage = android.media.AudioAttributes.USAGE_UNKNOWN;

        private int mContentType = android.media.AudioAttributes.CONTENT_TYPE_UNKNOWN;

        private int mSource = android.media.MediaRecorder.AudioSource.AUDIO_SOURCE_INVALID;

        private int mFlags = 0x0;

        private java.util.HashSet<java.lang.String> mTags = new java.util.HashSet<java.lang.String>();

        private android.os.Bundle mBundle;

        /**
         * Constructs a new Builder with the defaults.
         * By default, usage and content type are respectively {@link AudioAttributes#USAGE_UNKNOWN}
         * and {@link AudioAttributes#CONTENT_TYPE_UNKNOWN}, and flags are 0. It is recommended to
         * configure the usage (with {@link #setUsage(int)}) or deriving attributes from a legacy
         * stream type (with {@link #setLegacyStreamType(int)}) before calling {@link #build()}
         * to override any default playback behavior in terms of routing and volume management.
         */
        public Builder() {
        }

        /**
         * Constructs a new Builder from a given AudioAttributes
         *
         * @param aa
         * 		the AudioAttributes object whose data will be reused in the new Builder.
         */
        // for cloning of mTags
        @java.lang.SuppressWarnings("unchecked")
        public Builder(android.media.AudioAttributes aa) {
            mUsage = aa.mUsage;
            mContentType = aa.mContentType;
            mFlags = aa.mFlags;
            mTags = ((java.util.HashSet<java.lang.String>) (aa.mTags.clone()));
        }

        /**
         * Combines all of the attributes that have been set and return a new
         * {@link AudioAttributes} object.
         *
         * @return a new {@link AudioAttributes} object
         */
        // for cloning of mTags
        @java.lang.SuppressWarnings("unchecked")
        public android.media.AudioAttributes build() {
            android.media.AudioAttributes aa = new android.media.AudioAttributes();
            aa.mContentType = mContentType;
            aa.mUsage = mUsage;
            aa.mSource = mSource;
            aa.mFlags = mFlags;
            aa.mTags = ((java.util.HashSet<java.lang.String>) (mTags.clone()));
            aa.mFormattedTags = android.text.TextUtils.join(";", mTags);
            if (mBundle != null) {
                aa.mBundle = new android.os.Bundle(mBundle);
            }
            return aa;
        }

        /**
         * Sets the attribute describing what is the intended use of the the audio signal,
         * such as alarm or ringtone.
         *
         * @param usage
         * 		one of {@link AudioAttributes#USAGE_UNKNOWN},
         * 		{@link AudioAttributes#USAGE_MEDIA},
         * 		{@link AudioAttributes#USAGE_VOICE_COMMUNICATION},
         * 		{@link AudioAttributes#USAGE_VOICE_COMMUNICATION_SIGNALLING},
         * 		{@link AudioAttributes#USAGE_ALARM}, {@link AudioAttributes#USAGE_NOTIFICATION},
         * 		{@link AudioAttributes#USAGE_NOTIFICATION_RINGTONE},
         * 		{@link AudioAttributes#USAGE_NOTIFICATION_COMMUNICATION_REQUEST},
         * 		{@link AudioAttributes#USAGE_NOTIFICATION_COMMUNICATION_INSTANT},
         * 		{@link AudioAttributes#USAGE_NOTIFICATION_COMMUNICATION_DELAYED},
         * 		{@link AudioAttributes#USAGE_NOTIFICATION_EVENT},
         * 		{@link AudioAttributes#USAGE_ASSISTANCE_ACCESSIBILITY},
         * 		{@link AudioAttributes#USAGE_ASSISTANCE_NAVIGATION_GUIDANCE},
         * 		{@link AudioAttributes#USAGE_ASSISTANCE_SONIFICATION},
         * 		{@link AudioAttributes#USAGE_GAME}.
         * @return the same Builder instance.
         */
        public android.media.AudioAttributes.Builder setUsage(@android.media.AudioAttributes.AttributeUsage
        int usage) {
            switch (usage) {
                case android.media.AudioAttributes.USAGE_UNKNOWN :
                case android.media.AudioAttributes.USAGE_MEDIA :
                case android.media.AudioAttributes.USAGE_VOICE_COMMUNICATION :
                case android.media.AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING :
                case android.media.AudioAttributes.USAGE_ALARM :
                case android.media.AudioAttributes.USAGE_NOTIFICATION :
                case android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE :
                case android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_REQUEST :
                case android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT :
                case android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_DELAYED :
                case android.media.AudioAttributes.USAGE_NOTIFICATION_EVENT :
                case android.media.AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY :
                case android.media.AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE :
                case android.media.AudioAttributes.USAGE_ASSISTANCE_SONIFICATION :
                case android.media.AudioAttributes.USAGE_GAME :
                case android.media.AudioAttributes.USAGE_VIRTUAL_SOURCE :
                    mUsage = usage;
                    break;
                default :
                    mUsage = android.media.AudioAttributes.USAGE_UNKNOWN;
            }
            return this;
        }

        /**
         * Sets the attribute describing the content type of the audio signal, such as speech,
         * or music.
         *
         * @param contentType
         * 		the content type values, one of
         * 		{@link AudioAttributes#CONTENT_TYPE_MOVIE},
         * 		{@link AudioAttributes#CONTENT_TYPE_MUSIC},
         * 		{@link AudioAttributes#CONTENT_TYPE_SONIFICATION},
         * 		{@link AudioAttributes#CONTENT_TYPE_SPEECH},
         * 		{@link AudioAttributes#CONTENT_TYPE_UNKNOWN}.
         * @return the same Builder instance.
         */
        public android.media.AudioAttributes.Builder setContentType(@android.media.AudioAttributes.AttributeContentType
        int contentType) {
            switch (contentType) {
                case android.media.AudioAttributes.CONTENT_TYPE_UNKNOWN :
                case android.media.AudioAttributes.CONTENT_TYPE_MOVIE :
                case android.media.AudioAttributes.CONTENT_TYPE_MUSIC :
                case android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION :
                case android.media.AudioAttributes.CONTENT_TYPE_SPEECH :
                    mContentType = contentType;
                    break;
                default :
                    mUsage = android.media.AudioAttributes.CONTENT_TYPE_UNKNOWN;
            }
            return this;
        }

        /**
         * Sets the combination of flags.
         *
         * @param flags
         * 		a combination of {@link AudioAttributes#FLAG_AUDIBILITY_ENFORCED},
         * 		{@link AudioAttributes#FLAG_HW_AV_SYNC}.
         * @return the same Builder instance.
         */
        public android.media.AudioAttributes.Builder setFlags(int flags) {
            flags &= android.media.AudioAttributes.FLAG_ALL;
            mFlags |= flags;
            return this;
        }

        /**
         *
         *
         * @unknown Adds a Bundle of data
         * @param bundle
         * 		a non-null Bundle
         * @return the same builder instance
         */
        @android.annotation.SystemApi
        public android.media.AudioAttributes.Builder addBundle(@android.annotation.NonNull
        android.os.Bundle bundle) {
            if (bundle == null) {
                throw new java.lang.IllegalArgumentException("Illegal null bundle");
            }
            if (mBundle == null) {
                mBundle = new android.os.Bundle(bundle);
            } else {
                mBundle.putAll(bundle);
            }
            return this;
        }

        /**
         *
         *
         * @unknown Add a custom tag stored as a string
         * @param tag
         * 		
         * @return the same Builder instance.
         */
        public android.media.AudioAttributes.Builder addTag(java.lang.String tag) {
            mTags.add(tag);
            return this;
        }

        /**
         * Sets attributes as inferred from the legacy stream types.
         * Use this method when building an {@link AudioAttributes} instance to initialize some of
         * the attributes by information derived from a legacy stream type.
         *
         * @param streamType
         * 		one of {@link AudioManager#STREAM_VOICE_CALL},
         * 		{@link AudioManager#STREAM_SYSTEM}, {@link AudioManager#STREAM_RING},
         * 		{@link AudioManager#STREAM_MUSIC}, {@link AudioManager#STREAM_ALARM},
         * 		or {@link AudioManager#STREAM_NOTIFICATION}.
         * @return the same Builder instance.
         */
        public android.media.AudioAttributes.Builder setLegacyStreamType(int streamType) {
            return setInternalLegacyStreamType(streamType);
        }

        /**
         *
         *
         * @unknown For internal framework use only, enables building from hidden stream types.
         * @param streamType
         * 		
         * @return the same Builder instance.
         */
        public android.media.AudioAttributes.Builder setInternalLegacyStreamType(int streamType) {
            switch (streamType) {
                case android.media.AudioSystem.STREAM_VOICE_CALL :
                    mContentType = android.media.AudioAttributes.CONTENT_TYPE_SPEECH;
                    break;
                case android.media.AudioSystem.STREAM_SYSTEM_ENFORCED :
                    mFlags |= android.media.AudioAttributes.FLAG_AUDIBILITY_ENFORCED;
                    // intended fall through, attributes in common with STREAM_SYSTEM
                case android.media.AudioSystem.STREAM_SYSTEM :
                    mContentType = android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION;
                    break;
                case android.media.AudioSystem.STREAM_RING :
                    mContentType = android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION;
                    break;
                case android.media.AudioSystem.STREAM_MUSIC :
                    mContentType = android.media.AudioAttributes.CONTENT_TYPE_MUSIC;
                    break;
                case android.media.AudioSystem.STREAM_ALARM :
                    mContentType = android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION;
                    break;
                case android.media.AudioSystem.STREAM_NOTIFICATION :
                    mContentType = android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION;
                    break;
                case android.media.AudioSystem.STREAM_BLUETOOTH_SCO :
                    mContentType = android.media.AudioAttributes.CONTENT_TYPE_SPEECH;
                    mFlags |= android.media.AudioAttributes.FLAG_SCO;
                    break;
                case android.media.AudioSystem.STREAM_DTMF :
                    mContentType = android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION;
                    break;
                case android.media.AudioSystem.STREAM_TTS :
                    mContentType = android.media.AudioAttributes.CONTENT_TYPE_SPEECH;
                    break;
                default :
                    android.util.Log.e(android.media.AudioAttributes.TAG, ("Invalid stream type " + streamType) + " for AudioAttributes");
            }
            mUsage = android.media.AudioAttributes.usageForLegacyStreamType(streamType);
            return this;
        }

        /**
         *
         *
         * @unknown Sets the capture preset.
        Use this audio attributes configuration method when building an {@link AudioRecord}
        instance with {@link AudioRecord#AudioRecord(AudioAttributes, AudioFormat, int)}.
         * @param preset
         * 		one of {@link MediaRecorder.AudioSource#DEFAULT},
         * 		{@link MediaRecorder.AudioSource#MIC}, {@link MediaRecorder.AudioSource#CAMCORDER},
         * 		{@link MediaRecorder.AudioSource#VOICE_RECOGNITION},
         * 		{@link MediaRecorder.AudioSource#VOICE_COMMUNICATION} or
         * 		{@link MediaRecorder.AudioSource#UNPROCESSED}
         * @return the same Builder instance.
         */
        @android.annotation.SystemApi
        public android.media.AudioAttributes.Builder setCapturePreset(int preset) {
            switch (preset) {
                case android.media.MediaRecorder.AudioSource.DEFAULT :
                case android.media.MediaRecorder.AudioSource.MIC :
                case android.media.MediaRecorder.AudioSource.CAMCORDER :
                case android.media.MediaRecorder.AudioSource.VOICE_RECOGNITION :
                case android.media.MediaRecorder.AudioSource.VOICE_COMMUNICATION :
                case android.media.MediaRecorder.AudioSource.UNPROCESSED :
                    mSource = preset;
                    break;
                default :
                    android.util.Log.e(android.media.AudioAttributes.TAG, ("Invalid capture preset " + preset) + " for AudioAttributes");
            }
            return this;
        }

        /**
         *
         *
         * @unknown Same as {@link #setCapturePreset(int)} but authorizes the use of HOTWORD,
        REMOTE_SUBMIX and RADIO_TUNER.
         * @param preset
         * 		
         * @return the same Builder instance.
         */
        @android.annotation.SystemApi
        public android.media.AudioAttributes.Builder setInternalCapturePreset(int preset) {
            if (((preset == android.media.MediaRecorder.AudioSource.HOTWORD) || (preset == android.media.MediaRecorder.AudioSource.REMOTE_SUBMIX)) || (preset == android.media.MediaRecorder.AudioSource.RADIO_TUNER)) {
                mSource = preset;
            } else {
                setCapturePreset(preset);
            }
            return this;
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     *
     *
     * @unknown Used to indicate that when parcelling, the tags should be parcelled through the flattened
    formatted string, not through the array of strings.
    Keep in sync with frameworks/av/media/libmediaplayerservice/MediaPlayerService.cpp
    see definition of kAudioAttributesMarshallTagFlattenTags
     */
    public static final int FLATTEN_TAGS = 0x1;

    private static final int ATTR_PARCEL_IS_NULL_BUNDLE = -1977;

    private static final int ATTR_PARCEL_IS_VALID_BUNDLE = 1980;

    /**
     * When adding tags for writeToParcel(Parcel, int), add them in the list of flags (| NEW_FLAG)
     */
    private static final int ALL_PARCEL_FLAGS = android.media.AudioAttributes.FLATTEN_TAGS;

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mUsage);
        dest.writeInt(mContentType);
        dest.writeInt(mSource);
        dest.writeInt(mFlags);
        dest.writeInt(flags & android.media.AudioAttributes.ALL_PARCEL_FLAGS);
        if ((flags & android.media.AudioAttributes.FLATTEN_TAGS) == 0) {
            java.lang.String[] tagsArray = new java.lang.String[mTags.size()];
            mTags.toArray(tagsArray);
            dest.writeStringArray(tagsArray);
        } else
            if ((flags & android.media.AudioAttributes.FLATTEN_TAGS) == android.media.AudioAttributes.FLATTEN_TAGS) {
                dest.writeString(mFormattedTags);
            }

        if (mBundle == null) {
            dest.writeInt(android.media.AudioAttributes.ATTR_PARCEL_IS_NULL_BUNDLE);
        } else {
            dest.writeInt(android.media.AudioAttributes.ATTR_PARCEL_IS_VALID_BUNDLE);
            dest.writeBundle(mBundle);
        }
    }

    private AudioAttributes(android.os.Parcel in) {
        mUsage = in.readInt();
        mContentType = in.readInt();
        mSource = in.readInt();
        mFlags = in.readInt();
        boolean hasFlattenedTags = (in.readInt() & android.media.AudioAttributes.FLATTEN_TAGS) == android.media.AudioAttributes.FLATTEN_TAGS;
        mTags = new java.util.HashSet<java.lang.String>();
        if (hasFlattenedTags) {
            mFormattedTags = new java.lang.String(in.readString());
            mTags.add(mFormattedTags);
        } else {
            java.lang.String[] tagsArray = in.readStringArray();
            for (int i = tagsArray.length - 1; i >= 0; i--) {
                mTags.add(tagsArray[i]);
            }
            mFormattedTags = android.text.TextUtils.join(";", mTags);
        }
        switch (in.readInt()) {
            case android.media.AudioAttributes.ATTR_PARCEL_IS_NULL_BUNDLE :
                mBundle = null;
                break;
            case android.media.AudioAttributes.ATTR_PARCEL_IS_VALID_BUNDLE :
                mBundle = new android.os.Bundle(in.readBundle());
                break;
            default :
                android.util.Log.e(android.media.AudioAttributes.TAG, "Illegal value unmarshalling AudioAttributes, can't initialize bundle");
        }
    }

    public static final android.os.Parcelable.Creator<android.media.AudioAttributes> CREATOR = new android.os.Parcelable.Creator<android.media.AudioAttributes>() {
        /**
         * Rebuilds an AudioAttributes previously stored with writeToParcel().
         *
         * @param p
         * 		Parcel object to read the AudioAttributes from
         * @return a new AudioAttributes created from the data in the parcel
         */
        public android.media.AudioAttributes createFromParcel(android.os.Parcel p) {
            return new android.media.AudioAttributes(p);
        }

        public android.media.AudioAttributes[] newArray(int size) {
            return new android.media.AudioAttributes[size];
        }
    };

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        android.media.AudioAttributes that = ((android.media.AudioAttributes) (o));
        return ((((mContentType == that.mContentType) && (mFlags == that.mFlags)) && (mSource == that.mSource)) && (mUsage == that.mUsage)) && // mFormattedTags is never null due to assignment in Builder or unmarshalling
        mFormattedTags.equals(that.mFormattedTags);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mContentType, mFlags, mSource, mUsage, mFormattedTags, mBundle);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return new java.lang.String(((((((((("AudioAttributes:" + " usage=") + mUsage) + " content=") + mContentType) + " flags=0x") + java.lang.Integer.toHexString(mFlags).toUpperCase()) + " tags=") + mFormattedTags) + " bundle=") + (mBundle == null ? "null" : mBundle.toString()));
    }

    /**
     *
     *
     * @unknown 
     */
    public java.lang.String usageToString() {
        return android.media.AudioAttributes.usageToString(mUsage);
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String usageToString(int usage) {
        switch (usage) {
            case android.media.AudioAttributes.USAGE_UNKNOWN :
                return new java.lang.String("USAGE_UNKNOWN");
            case android.media.AudioAttributes.USAGE_MEDIA :
                return new java.lang.String("USAGE_MEDIA");
            case android.media.AudioAttributes.USAGE_VOICE_COMMUNICATION :
                return new java.lang.String("USAGE_VOICE_COMMUNICATION");
            case android.media.AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING :
                return new java.lang.String("USAGE_VOICE_COMMUNICATION_SIGNALLING");
            case android.media.AudioAttributes.USAGE_ALARM :
                return new java.lang.String("USAGE_ALARM");
            case android.media.AudioAttributes.USAGE_NOTIFICATION :
                return new java.lang.String("USAGE_NOTIFICATION");
            case android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE :
                return new java.lang.String("USAGE_NOTIFICATION_RINGTONE");
            case android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_REQUEST :
                return new java.lang.String("USAGE_NOTIFICATION_COMMUNICATION_REQUEST");
            case android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT :
                return new java.lang.String("USAGE_NOTIFICATION_COMMUNICATION_INSTANT");
            case android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_DELAYED :
                return new java.lang.String("USAGE_NOTIFICATION_COMMUNICATION_DELAYED");
            case android.media.AudioAttributes.USAGE_NOTIFICATION_EVENT :
                return new java.lang.String("USAGE_NOTIFICATION_EVENT");
            case android.media.AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY :
                return new java.lang.String("USAGE_ASSISTANCE_ACCESSIBILITY");
            case android.media.AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE :
                return new java.lang.String("USAGE_ASSISTANCE_NAVIGATION_GUIDANCE");
            case android.media.AudioAttributes.USAGE_ASSISTANCE_SONIFICATION :
                return new java.lang.String("USAGE_ASSISTANCE_SONIFICATION");
            case android.media.AudioAttributes.USAGE_GAME :
                return new java.lang.String("USAGE_GAME");
            default :
                return new java.lang.String("unknown usage " + usage);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static int usageForLegacyStreamType(int streamType) {
        switch (streamType) {
            case android.media.AudioSystem.STREAM_VOICE_CALL :
                return android.media.AudioAttributes.USAGE_VOICE_COMMUNICATION;
            case android.media.AudioSystem.STREAM_SYSTEM_ENFORCED :
            case android.media.AudioSystem.STREAM_SYSTEM :
                return android.media.AudioAttributes.USAGE_ASSISTANCE_SONIFICATION;
            case android.media.AudioSystem.STREAM_RING :
                return android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE;
            case android.media.AudioSystem.STREAM_MUSIC :
                return android.media.AudioAttributes.USAGE_MEDIA;
            case android.media.AudioSystem.STREAM_ALARM :
                return android.media.AudioAttributes.USAGE_ALARM;
            case android.media.AudioSystem.STREAM_NOTIFICATION :
                return android.media.AudioAttributes.USAGE_NOTIFICATION;
            case android.media.AudioSystem.STREAM_BLUETOOTH_SCO :
                return android.media.AudioAttributes.USAGE_VOICE_COMMUNICATION;
            case android.media.AudioSystem.STREAM_DTMF :
                return android.media.AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING;
            case android.media.AudioSystem.STREAM_TTS :
                return android.media.AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY;
            default :
                return android.media.AudioAttributes.USAGE_UNKNOWN;
        }
    }

    /**
     *
     *
     * @unknown CANDIDATE FOR PUBLIC (or at least SYSTEM) API
    Returns the stream type matching the given attributes for volume control.
    Use this method to derive the stream type needed to configure the volume
    control slider in an {@link Activity} with {@link Activity#setVolumeControlStream(int)}.
    <BR>Do not use this method to set the stream type on an audio player object
    (e.g. {@link AudioTrack}, {@link MediaPlayer}), use <code>AudioAttributes</code> instead.
     * @param aa
     * 		non-null AudioAttributes.
     * @return a valid stream type for <code>Activity</code> or stream volume control that matches
    the attributes, or {@link AudioManager#USE_DEFAULT_STREAM_TYPE} if there isn't a direct
    match. Note that <code>USE_DEFAULT_STREAM_TYPE</code> is not a valid value
    for {@link AudioManager#setStreamVolume(int, int, int)}.
     */
    public static int getVolumeControlStream(@android.annotation.NonNull
    android.media.AudioAttributes aa) {
        if (aa == null) {
            throw new java.lang.IllegalArgumentException("Invalid null audio attributes");
        }
        return /* fromGetVolumeControlStream */
        android.media.AudioAttributes.toVolumeStreamType(true, aa);
    }

    /**
     *
     *
     * @unknown Only use to get which stream type should be used for volume control, NOT for audio playback
    (all audio playback APIs are supposed to take AudioAttributes as input parameters)
     * @param aa
     * 		non-null AudioAttributes.
     * @return a valid stream type for volume control that matches the attributes.
     */
    public static int toLegacyStreamType(@android.annotation.NonNull
    android.media.AudioAttributes aa) {
        return /* fromGetVolumeControlStream */
        android.media.AudioAttributes.toVolumeStreamType(false, aa);
    }

    private static int toVolumeStreamType(boolean fromGetVolumeControlStream, android.media.AudioAttributes aa) {
        // flags to stream type mapping
        if ((aa.getFlags() & android.media.AudioAttributes.FLAG_AUDIBILITY_ENFORCED) == android.media.AudioAttributes.FLAG_AUDIBILITY_ENFORCED) {
            return fromGetVolumeControlStream ? android.media.AudioSystem.STREAM_SYSTEM : android.media.AudioSystem.STREAM_SYSTEM_ENFORCED;
        }
        if ((aa.getFlags() & android.media.AudioAttributes.FLAG_SCO) == android.media.AudioAttributes.FLAG_SCO) {
            return fromGetVolumeControlStream ? android.media.AudioSystem.STREAM_VOICE_CALL : android.media.AudioSystem.STREAM_BLUETOOTH_SCO;
        }
        // usage to stream type mapping
        switch (aa.getUsage()) {
            case android.media.AudioAttributes.USAGE_MEDIA :
            case android.media.AudioAttributes.USAGE_GAME :
            case android.media.AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY :
            case android.media.AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE :
                return android.media.AudioSystem.STREAM_MUSIC;
            case android.media.AudioAttributes.USAGE_ASSISTANCE_SONIFICATION :
                return android.media.AudioSystem.STREAM_SYSTEM;
            case android.media.AudioAttributes.USAGE_VOICE_COMMUNICATION :
                return android.media.AudioSystem.STREAM_VOICE_CALL;
            case android.media.AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING :
                return fromGetVolumeControlStream ? android.media.AudioSystem.STREAM_VOICE_CALL : android.media.AudioSystem.STREAM_DTMF;
            case android.media.AudioAttributes.USAGE_ALARM :
                return android.media.AudioSystem.STREAM_ALARM;
            case android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE :
                return android.media.AudioSystem.STREAM_RING;
            case android.media.AudioAttributes.USAGE_NOTIFICATION :
            case android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_REQUEST :
            case android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT :
            case android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_DELAYED :
            case android.media.AudioAttributes.USAGE_NOTIFICATION_EVENT :
                return android.media.AudioSystem.STREAM_NOTIFICATION;
            case android.media.AudioAttributes.USAGE_UNKNOWN :
                return fromGetVolumeControlStream ? android.media.AudioManager.USE_DEFAULT_STREAM_TYPE : android.media.AudioSystem.STREAM_MUSIC;
            default :
                if (fromGetVolumeControlStream) {
                    throw new java.lang.IllegalArgumentException(("Unknown usage value " + aa.getUsage()) + " in audio attributes");
                } else {
                    return android.media.AudioSystem.STREAM_MUSIC;
                }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.media.AudioAttributes.USAGE_UNKNOWN, android.media.AudioAttributes.USAGE_MEDIA, android.media.AudioAttributes.USAGE_VOICE_COMMUNICATION, android.media.AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING, android.media.AudioAttributes.USAGE_ALARM, android.media.AudioAttributes.USAGE_NOTIFICATION, android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE, android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_REQUEST, android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT, android.media.AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_DELAYED, android.media.AudioAttributes.USAGE_NOTIFICATION_EVENT, android.media.AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY, android.media.AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE, android.media.AudioAttributes.USAGE_ASSISTANCE_SONIFICATION, android.media.AudioAttributes.USAGE_GAME })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AttributeUsage {}

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.media.AudioAttributes.CONTENT_TYPE_UNKNOWN, android.media.AudioAttributes.CONTENT_TYPE_SPEECH, android.media.AudioAttributes.CONTENT_TYPE_MUSIC, android.media.AudioAttributes.CONTENT_TYPE_MOVIE, android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AttributeContentType {}
}

