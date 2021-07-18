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
package android.media;


/**
 * The AudioRecord class manages the audio resources for Java applications
 * to record audio from the audio input hardware of the platform. This is
 * achieved by "pulling" (reading) the data from the AudioRecord object. The
 * application is responsible for polling the AudioRecord object in time using one of
 * the following three methods:  {@link #read(byte[],int, int)}, {@link #read(short[], int, int)}
 * or {@link #read(ByteBuffer, int)}. The choice of which method to use will be based
 * on the audio data storage format that is the most convenient for the user of AudioRecord.
 * <p>Upon creation, an AudioRecord object initializes its associated audio buffer that it will
 * fill with the new audio data. The size of this buffer, specified during the construction,
 * determines how long an AudioRecord can record before "over-running" data that has not
 * been read yet. Data should be read from the audio hardware in chunks of sizes inferior to
 * the total recording buffer size.
 */
public class AudioRecord implements android.media.AudioRouting {
    // ---------------------------------------------------------
    // Constants
    // --------------------
    /**
     * indicates AudioRecord state is not successfully initialized.
     */
    public static final int STATE_UNINITIALIZED = 0;

    /**
     * indicates AudioRecord state is ready to be used
     */
    public static final int STATE_INITIALIZED = 1;

    /**
     * indicates AudioRecord recording state is not recording
     */
    public static final int RECORDSTATE_STOPPED = 1;// matches SL_RECORDSTATE_STOPPED


    /**
     * indicates AudioRecord recording state is recording
     */
    public static final int RECORDSTATE_RECORDING = 3;// matches SL_RECORDSTATE_RECORDING


    /**
     * Denotes a successful operation.
     */
    public static final int SUCCESS = android.media.AudioSystem.SUCCESS;

    /**
     * Denotes a generic operation failure.
     */
    public static final int ERROR = android.media.AudioSystem.ERROR;

    /**
     * Denotes a failure due to the use of an invalid value.
     */
    public static final int ERROR_BAD_VALUE = android.media.AudioSystem.BAD_VALUE;

    /**
     * Denotes a failure due to the improper use of a method.
     */
    public static final int ERROR_INVALID_OPERATION = android.media.AudioSystem.INVALID_OPERATION;

    /**
     * An error code indicating that the object reporting it is no longer valid and needs to
     * be recreated.
     */
    public static final int ERROR_DEAD_OBJECT = android.media.AudioSystem.DEAD_OBJECT;

    // Error codes:
    // to keep in sync with frameworks/base/core/jni/android_media_AudioRecord.cpp
    private static final int AUDIORECORD_ERROR_SETUP_ZEROFRAMECOUNT = -16;

    private static final int AUDIORECORD_ERROR_SETUP_INVALIDCHANNELMASK = -17;

    private static final int AUDIORECORD_ERROR_SETUP_INVALIDFORMAT = -18;

    private static final int AUDIORECORD_ERROR_SETUP_INVALIDSOURCE = -19;

    private static final int AUDIORECORD_ERROR_SETUP_NATIVEINITFAILED = -20;

    // Events:
    // to keep in sync with frameworks/av/include/media/AudioRecord.h
    /**
     * Event id denotes when record head has reached a previously set marker.
     */
    private static final int NATIVE_EVENT_MARKER = 2;

    /**
     * Event id denotes when previously set update period has elapsed during recording.
     */
    private static final int NATIVE_EVENT_NEW_POS = 3;

    private static final java.lang.String TAG = "android.media.AudioRecord";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String SUBMIX_FIXED_VOLUME = "fixedVolume";

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.media.AudioRecord.READ_BLOCKING, android.media.AudioRecord.READ_NON_BLOCKING })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ReadMode {}

    /**
     * The read mode indicating the read operation will block until all data
     * requested has been read.
     */
    public static final int READ_BLOCKING = 0;

    /**
     * The read mode indicating the read operation will return immediately after
     * reading as much audio data as possible without blocking.
     */
    public static final int READ_NON_BLOCKING = 1;

    // ---------------------------------------------------------
    // Used exclusively by native code
    // --------------------
    /**
     * Accessed by native methods: provides access to C++ AudioRecord object
     */
    @java.lang.SuppressWarnings("unused")
    private long mNativeRecorderInJavaObj;

    /**
     * Accessed by native methods: provides access to the callback data.
     */
    @java.lang.SuppressWarnings("unused")
    private long mNativeCallbackCookie;

    /**
     * Accessed by native methods: provides access to the JNIDeviceCallback instance.
     */
    @java.lang.SuppressWarnings("unused")
    private long mNativeDeviceCallback;

    // ---------------------------------------------------------
    // Member variables
    // --------------------
    /**
     * The audio data sampling rate in Hz.
     * Never {@link AudioFormat#SAMPLE_RATE_UNSPECIFIED}.
     */
    private int mSampleRate;// initialized by all constructors via audioParamCheck()


    /**
     * The number of input audio channels (1 is mono, 2 is stereo)
     */
    private int mChannelCount;

    /**
     * The audio channel position mask
     */
    private int mChannelMask;

    /**
     * The audio channel index mask
     */
    private int mChannelIndexMask;

    /**
     * The encoding of the audio samples.
     *
     * @see AudioFormat#ENCODING_PCM_8BIT
     * @see AudioFormat#ENCODING_PCM_16BIT
     * @see AudioFormat#ENCODING_PCM_FLOAT
     */
    private int mAudioFormat;

    /**
     * Where the audio data is recorded from.
     */
    private int mRecordSource;

    /**
     * Indicates the state of the AudioRecord instance.
     */
    private int mState = android.media.AudioRecord.STATE_UNINITIALIZED;

    /**
     * Indicates the recording state of the AudioRecord instance.
     */
    private int mRecordingState = android.media.AudioRecord.RECORDSTATE_STOPPED;

    /**
     * Lock to make sure mRecordingState updates are reflecting the actual state of the object.
     */
    private final java.lang.Object mRecordingStateLock = new java.lang.Object();

    /**
     * The listener the AudioRecord notifies when the record position reaches a marker
     * or for periodic updates during the progression of the record head.
     *
     * @see #setRecordPositionUpdateListener(OnRecordPositionUpdateListener)
     * @see #setRecordPositionUpdateListener(OnRecordPositionUpdateListener, Handler)
     */
    private android.media.AudioRecord.OnRecordPositionUpdateListener mPositionListener = null;

    /**
     * Lock to protect position listener updates against event notifications
     */
    private final java.lang.Object mPositionListenerLock = new java.lang.Object();

    /**
     * Handler for marker events coming from the native code
     */
    private android.media.AudioRecord.NativeEventHandler mEventHandler = null;

    /**
     * Looper associated with the thread that creates the AudioRecord instance
     */
    private android.os.Looper mInitializationLooper = null;

    /**
     * Size of the native audio buffer.
     */
    private int mNativeBufferSizeInBytes = 0;

    /**
     * Audio session ID
     */
    private int mSessionId = android.media.AudioManager.AUDIO_SESSION_ID_GENERATE;

    /**
     * AudioAttributes
     */
    private android.media.AudioAttributes mAudioAttributes;

    private boolean mIsSubmixFullVolume = false;

    // ---------------------------------------------------------
    // Constructor, Finalize
    // --------------------
    /**
     * Class constructor.
     * Though some invalid parameters will result in an {@link IllegalArgumentException} exception,
     * other errors do not.  Thus you should call {@link #getState()} immediately after construction
     * to confirm that the object is usable.
     *
     * @param audioSource
     * 		the recording source.
     * 		See {@link MediaRecorder.AudioSource} for the recording source definitions.
     * @param sampleRateInHz
     * 		the sample rate expressed in Hertz. 44100Hz is currently the only
     * 		rate that is guaranteed to work on all devices, but other rates such as 22050,
     * 		16000, and 11025 may work on some devices.
     * 		{@link AudioFormat#SAMPLE_RATE_UNSPECIFIED} means to use a route-dependent value
     * 		which is usually the sample rate of the source.
     * 		{@link #getSampleRate()} can be used to retrieve the actual sample rate chosen.
     * @param channelConfig
     * 		describes the configuration of the audio channels.
     * 		See {@link AudioFormat#CHANNEL_IN_MONO} and
     * 		{@link AudioFormat#CHANNEL_IN_STEREO}.  {@link AudioFormat#CHANNEL_IN_MONO} is guaranteed
     * 		to work on all devices.
     * @param audioFormat
     * 		the format in which the audio data is to be returned.
     * 		See {@link AudioFormat#ENCODING_PCM_8BIT}, {@link AudioFormat#ENCODING_PCM_16BIT},
     * 		and {@link AudioFormat#ENCODING_PCM_FLOAT}.
     * @param bufferSizeInBytes
     * 		the total size (in bytes) of the buffer where audio data is written
     * 		to during the recording. New audio data can be read from this buffer in smaller chunks
     * 		than this size. See {@link #getMinBufferSize(int, int, int)} to determine the minimum
     * 		required buffer size for the successful creation of an AudioRecord instance. Using values
     * 		smaller than getMinBufferSize() will result in an initialization failure.
     * @throws java.lang.IllegalArgumentException
     * 		
     */
    public AudioRecord(int audioSource, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes) throws java.lang.IllegalArgumentException {
        this(new android.media.AudioAttributes.Builder().setInternalCapturePreset(audioSource).build(), new android.media.AudioFormat.Builder().setChannelMask(/* allow legacy configurations */
        android.media.AudioRecord.getChannelMaskFromLegacyConfig(channelConfig, true)).setEncoding(audioFormat).setSampleRate(sampleRateInHz).build(), bufferSizeInBytes, android.media.AudioManager.AUDIO_SESSION_ID_GENERATE);
    }

    /**
     *
     *
     * @unknown Class constructor with {@link AudioAttributes} and {@link AudioFormat}.
     * @param attributes
     * 		a non-null {@link AudioAttributes} instance. Use
     * 		{@link AudioAttributes.Builder#setAudioSource(int)} for configuring the audio
     * 		source for this instance.
     * @param format
     * 		a non-null {@link AudioFormat} instance describing the format of the data
     * 		that will be recorded through this AudioRecord. See {@link AudioFormat.Builder} for
     * 		configuring the audio format parameters such as encoding, channel mask and sample rate.
     * @param bufferSizeInBytes
     * 		the total size (in bytes) of the buffer where audio data is written
     * 		to during the recording. New audio data can be read from this buffer in smaller chunks
     * 		than this size. See {@link #getMinBufferSize(int, int, int)} to determine the minimum
     * 		required buffer size for the successful creation of an AudioRecord instance. Using values
     * 		smaller than getMinBufferSize() will result in an initialization failure.
     * @param sessionId
     * 		ID of audio session the AudioRecord must be attached to, or
     * 		{@link AudioManager#AUDIO_SESSION_ID_GENERATE} if the session isn't known at construction
     * 		time. See also {@link AudioManager#generateAudioSessionId()} to obtain a session ID before
     * 		construction.
     * @throws IllegalArgumentException
     * 		
     */
    @android.annotation.SystemApi
    public AudioRecord(android.media.AudioAttributes attributes, android.media.AudioFormat format, int bufferSizeInBytes, int sessionId) throws java.lang.IllegalArgumentException {
        mRecordingState = android.media.AudioRecord.RECORDSTATE_STOPPED;
        if (attributes == null) {
            throw new java.lang.IllegalArgumentException("Illegal null AudioAttributes");
        }
        if (format == null) {
            throw new java.lang.IllegalArgumentException("Illegal null AudioFormat");
        }
        // remember which looper is associated with the AudioRecord instanciation
        if ((mInitializationLooper = android.os.Looper.myLooper()) == null) {
            mInitializationLooper = android.os.Looper.getMainLooper();
        }
        // is this AudioRecord using REMOTE_SUBMIX at full volume?
        if (attributes.getCapturePreset() == android.media.MediaRecorder.AudioSource.REMOTE_SUBMIX) {
            final android.media.AudioAttributes.Builder filteredAttr = new android.media.AudioAttributes.Builder();
            final java.util.Iterator<java.lang.String> tagsIter = attributes.getTags().iterator();
            while (tagsIter.hasNext()) {
                final java.lang.String tag = tagsIter.next();
                if (tag.equalsIgnoreCase(android.media.AudioRecord.SUBMIX_FIXED_VOLUME)) {
                    mIsSubmixFullVolume = true;
                    android.util.Log.v(android.media.AudioRecord.TAG, "Will record from REMOTE_SUBMIX at full fixed volume");
                } else {
                    // SUBMIX_FIXED_VOLUME: is not to be propagated to the native layers
                    filteredAttr.addTag(tag);
                }
            } 
            filteredAttr.setInternalCapturePreset(attributes.getCapturePreset());
            mAudioAttributes = filteredAttr.build();
        } else {
            mAudioAttributes = attributes;
        }
        int rate = format.getSampleRate();
        if (rate == android.media.AudioFormat.SAMPLE_RATE_UNSPECIFIED) {
            rate = 0;
        }
        int encoding = android.media.AudioFormat.ENCODING_DEFAULT;
        if ((format.getPropertySetMask() & android.media.AudioFormat.AUDIO_FORMAT_HAS_PROPERTY_ENCODING) != 0) {
            encoding = format.getEncoding();
        }
        audioParamCheck(attributes.getCapturePreset(), rate, encoding);
        if ((format.getPropertySetMask() & android.media.AudioFormat.AUDIO_FORMAT_HAS_PROPERTY_CHANNEL_INDEX_MASK) != 0) {
            mChannelIndexMask = format.getChannelIndexMask();
            mChannelCount = format.getChannelCount();
        }
        if ((format.getPropertySetMask() & android.media.AudioFormat.AUDIO_FORMAT_HAS_PROPERTY_CHANNEL_MASK) != 0) {
            mChannelMask = android.media.AudioRecord.getChannelMaskFromLegacyConfig(format.getChannelMask(), false);
            mChannelCount = format.getChannelCount();
        } else
            if (mChannelIndexMask == 0) {
                mChannelMask = android.media.AudioRecord.getChannelMaskFromLegacyConfig(android.media.AudioFormat.CHANNEL_IN_DEFAULT, false);
                mChannelCount = android.media.AudioFormat.channelCountFromInChannelMask(mChannelMask);
            }

        audioBuffSizeCheck(bufferSizeInBytes);
        int[] sampleRate = new int[]{ mSampleRate };
        int[] session = new int[1];
        session[0] = sessionId;
        // TODO: update native initialization when information about hardware init failure
        // due to capture device already open is available.
        int initResult = /* nativeRecordInJavaObj */
        native_setup(new java.lang.ref.WeakReference<android.media.AudioRecord>(this), mAudioAttributes, sampleRate, mChannelMask, mChannelIndexMask, mAudioFormat, mNativeBufferSizeInBytes, session, android.app.ActivityThread.currentOpPackageName(), 0);
        if (initResult != android.media.AudioRecord.SUCCESS) {
            android.media.AudioRecord.loge(("Error code " + initResult) + " when initializing native AudioRecord object.");
            return;// with mState == STATE_UNINITIALIZED

        }
        mSampleRate = sampleRate[0];
        mSessionId = session[0];
        mState = android.media.AudioRecord.STATE_INITIALIZED;
    }

    /**
     * A constructor which explicitly connects a Native (C++) AudioRecord. For use by
     * the AudioRecordRoutingProxy subclass.
     *
     * @param nativeRecordInJavaObj
     * 		A C/C++ pointer to a native AudioRecord
     * 		(associated with an OpenSL ES recorder). Note: the caller must ensure a correct
     * 		value here as no error checking is or can be done.
     */
    /* package */
    AudioRecord(long nativeRecordInJavaObj) {
        mNativeRecorderInJavaObj = 0;
        mNativeCallbackCookie = 0;
        mNativeDeviceCallback = 0;
        // other initialization...
        if (nativeRecordInJavaObj != 0) {
            deferred_connect(nativeRecordInJavaObj);
        } else {
            mState = android.media.AudioRecord.STATE_UNINITIALIZED;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    /* package */
    void deferred_connect(long nativeRecordInJavaObj) {
        if (mState != android.media.AudioRecord.STATE_INITIALIZED) {
            int[] session = new int[]{ 0 };
            int[] rates = new int[]{ 0 };
            // TODO: update native initialization when information about hardware init failure
            // due to capture device already open is available.
            // Note that for this native_setup, we are providing an already created/initialized
            // *Native* AudioRecord, so the attributes parameters to native_setup() are ignored.
            int initResult = /* mAudioAttributes */
            /* mSampleRates */
            /* mChannelMask */
            /* mChannelIndexMask */
            /* mAudioFormat */
            /* mNativeBufferSizeInBytes */
            native_setup(new java.lang.ref.WeakReference<android.media.AudioRecord>(this), null, rates, 0, 0, 0, 0, session, android.app.ActivityThread.currentOpPackageName(), nativeRecordInJavaObj);
            if (initResult != android.media.AudioRecord.SUCCESS) {
                android.media.AudioRecord.loge(("Error code " + initResult) + " when initializing native AudioRecord object.");
                return;// with mState == STATE_UNINITIALIZED

            }
            mSessionId = session[0];
            mState = android.media.AudioRecord.STATE_INITIALIZED;
        }
    }

    /**
     * Builder class for {@link AudioRecord} objects.
     * Use this class to configure and create an <code>AudioRecord</code> instance. By setting the
     * recording source and audio format parameters, you indicate which of
     * those vary from the default behavior on the device.
     * <p> Here is an example where <code>Builder</code> is used to specify all {@link AudioFormat}
     * parameters, to be used by a new <code>AudioRecord</code> instance:
     *
     * <pre class="prettyprint">
     * AudioRecord recorder = new AudioRecord.Builder()
     *         .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
     *         .setAudioFormat(new AudioFormat.Builder()
     *                 .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
     *                 .setSampleRate(32000)
     *                 .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
     *                 .build())
     *         .setBufferSize(2*minBuffSize)
     *         .build();
     * </pre>
     * <p>
     * If the audio source is not set with {@link #setAudioSource(int)},
     * {@link MediaRecorder.AudioSource#DEFAULT} is used.
     * <br>If the audio format is not specified or is incomplete, its channel configuration will be
     * {@link AudioFormat#CHANNEL_IN_MONO}, and the encoding will be
     * {@link AudioFormat#ENCODING_PCM_16BIT}.
     * The sample rate will depend on the device actually selected for capture and can be queried
     * with {@link #getSampleRate()} method.
     * <br>If the buffer size is not specified with {@link #setBufferSizeInBytes(int)},
     * the minimum buffer size for the source is used.
     */
    public static class Builder {
        private android.media.AudioAttributes mAttributes;

        private android.media.AudioFormat mFormat;

        private int mBufferSizeInBytes;

        private int mSessionId = android.media.AudioManager.AUDIO_SESSION_ID_GENERATE;

        /**
         * Constructs a new Builder with the default values as described above.
         */
        public Builder() {
        }

        /**
         *
         *
         * @param source
         * 		the audio source.
         * 		See {@link MediaRecorder.AudioSource} for the supported audio source definitions.
         * @return the same Builder instance.
         * @throws IllegalArgumentException
         * 		
         */
        public android.media.AudioRecord.Builder setAudioSource(int source) throws java.lang.IllegalArgumentException {
            if ((source < android.media.MediaRecorder.AudioSource.DEFAULT) || (source > android.media.MediaRecorder.getAudioSourceMax())) {
                throw new java.lang.IllegalArgumentException("Invalid audio source " + source);
            }
            mAttributes = new android.media.AudioAttributes.Builder().setInternalCapturePreset(source).build();
            return this;
        }

        /**
         *
         *
         * @unknown To be only used by system components. Allows specifying non-public capture presets
         * @param attributes
         * 		a non-null {@link AudioAttributes} instance that contains the capture
         * 		preset to be used.
         * @return the same Builder instance.
         * @throws IllegalArgumentException
         * 		
         */
        @android.annotation.SystemApi
        public android.media.AudioRecord.Builder setAudioAttributes(@android.annotation.NonNull
        android.media.AudioAttributes attributes) throws java.lang.IllegalArgumentException {
            if (attributes == null) {
                throw new java.lang.IllegalArgumentException("Illegal null AudioAttributes argument");
            }
            if (attributes.getCapturePreset() == android.media.MediaRecorder.AudioSource.AUDIO_SOURCE_INVALID) {
                throw new java.lang.IllegalArgumentException("No valid capture preset in AudioAttributes argument");
            }
            // keep reference, we only copy the data when building
            mAttributes = attributes;
            return this;
        }

        /**
         * Sets the format of the audio data to be captured.
         *
         * @param format
         * 		a non-null {@link AudioFormat} instance
         * @return the same Builder instance.
         * @throws IllegalArgumentException
         * 		
         */
        public android.media.AudioRecord.Builder setAudioFormat(@android.annotation.NonNull
        android.media.AudioFormat format) throws java.lang.IllegalArgumentException {
            if (format == null) {
                throw new java.lang.IllegalArgumentException("Illegal null AudioFormat argument");
            }
            // keep reference, we only copy the data when building
            mFormat = format;
            return this;
        }

        /**
         * Sets the total size (in bytes) of the buffer where audio data is written
         * during the recording. New audio data can be read from this buffer in smaller chunks
         * than this size. See {@link #getMinBufferSize(int, int, int)} to determine the minimum
         * required buffer size for the successful creation of an AudioRecord instance.
         * Since bufferSizeInBytes may be internally increased to accommodate the source
         * requirements, use {@link #getBufferSizeInFrames()} to determine the actual buffer size
         * in frames.
         *
         * @param bufferSizeInBytes
         * 		a value strictly greater than 0
         * @return the same Builder instance.
         * @throws IllegalArgumentException
         * 		
         */
        public android.media.AudioRecord.Builder setBufferSizeInBytes(int bufferSizeInBytes) throws java.lang.IllegalArgumentException {
            if (bufferSizeInBytes <= 0) {
                throw new java.lang.IllegalArgumentException("Invalid buffer size " + bufferSizeInBytes);
            }
            mBufferSizeInBytes = bufferSizeInBytes;
            return this;
        }

        /**
         *
         *
         * @unknown To be only used by system components.
         * @param sessionId
         * 		ID of audio session the AudioRecord must be attached to, or
         * 		{@link AudioManager#AUDIO_SESSION_ID_GENERATE} if the session isn't known at
         * 		construction time.
         * @return the same Builder instance.
         * @throws IllegalArgumentException
         * 		
         */
        @android.annotation.SystemApi
        public android.media.AudioRecord.Builder setSessionId(int sessionId) throws java.lang.IllegalArgumentException {
            if (sessionId < 0) {
                throw new java.lang.IllegalArgumentException("Invalid session ID " + sessionId);
            }
            mSessionId = sessionId;
            return this;
        }

        /**
         *
         *
         * @return a new {@link AudioRecord} instance successfully initialized with all
        the parameters set on this <code>Builder</code>.
         * @throws UnsupportedOperationException
         * 		if the parameters set on the <code>Builder</code>
         * 		were incompatible, or if they are not supported by the device,
         * 		or if the device was not available.
         */
        public android.media.AudioRecord build() throws java.lang.UnsupportedOperationException {
            if (mFormat == null) {
                mFormat = new android.media.AudioFormat.Builder().setEncoding(android.media.AudioFormat.ENCODING_PCM_16BIT).setChannelMask(android.media.AudioFormat.CHANNEL_IN_MONO).build();
            } else {
                if (mFormat.getEncoding() == android.media.AudioFormat.ENCODING_INVALID) {
                    mFormat = new android.media.AudioFormat.Builder(mFormat).setEncoding(android.media.AudioFormat.ENCODING_PCM_16BIT).build();
                }
                if ((mFormat.getChannelMask() == android.media.AudioFormat.CHANNEL_INVALID) && (mFormat.getChannelIndexMask() == android.media.AudioFormat.CHANNEL_INVALID)) {
                    mFormat = new android.media.AudioFormat.Builder(mFormat).setChannelMask(android.media.AudioFormat.CHANNEL_IN_MONO).build();
                }
            }
            if (mAttributes == null) {
                mAttributes = new android.media.AudioAttributes.Builder().setInternalCapturePreset(android.media.MediaRecorder.AudioSource.DEFAULT).build();
            }
            try {
                // If the buffer size is not specified,
                // use a single frame for the buffer size and let the
                // native code figure out the minimum buffer size.
                if (mBufferSizeInBytes == 0) {
                    mBufferSizeInBytes = mFormat.getChannelCount() * mFormat.getBytesPerSample(mFormat.getEncoding());
                }
                final android.media.AudioRecord record = new android.media.AudioRecord(mAttributes, mFormat, mBufferSizeInBytes, mSessionId);
                if (record.getState() == android.media.AudioRecord.STATE_UNINITIALIZED) {
                    // release is not necessary
                    throw new java.lang.UnsupportedOperationException("Cannot create AudioRecord");
                }
                return record;
            } catch (java.lang.IllegalArgumentException e) {
                throw new java.lang.UnsupportedOperationException(e.getMessage());
            }
        }
    }

    // Convenience method for the constructor's parameter checks.
    // This, getChannelMaskFromLegacyConfig and audioBuffSizeCheck are where constructor
    // IllegalArgumentException-s are thrown
    private static int getChannelMaskFromLegacyConfig(int inChannelConfig, boolean allowLegacyConfig) {
        int mask;
        switch (inChannelConfig) {
            case android.media.AudioFormat.CHANNEL_IN_DEFAULT :
                // AudioFormat.CHANNEL_CONFIGURATION_DEFAULT
            case android.media.AudioFormat.CHANNEL_IN_MONO :
            case android.media.AudioFormat.CHANNEL_CONFIGURATION_MONO :
                mask = android.media.AudioFormat.CHANNEL_IN_MONO;
                break;
            case android.media.AudioFormat.CHANNEL_IN_STEREO :
            case android.media.AudioFormat.CHANNEL_CONFIGURATION_STEREO :
                mask = android.media.AudioFormat.CHANNEL_IN_STEREO;
                break;
            case android.media.AudioFormat.CHANNEL_IN_FRONT | android.media.AudioFormat.CHANNEL_IN_BACK :
                mask = inChannelConfig;
                break;
            default :
                throw new java.lang.IllegalArgumentException("Unsupported channel configuration.");
        }
        if ((!allowLegacyConfig) && ((inChannelConfig == android.media.AudioFormat.CHANNEL_CONFIGURATION_MONO) || (inChannelConfig == android.media.AudioFormat.CHANNEL_CONFIGURATION_STEREO))) {
            // only happens with the constructor that uses AudioAttributes and AudioFormat
            throw new java.lang.IllegalArgumentException("Unsupported deprecated configuration.");
        }
        return mask;
    }

    // postconditions:
    // mRecordSource is valid
    // mAudioFormat is valid
    // mSampleRate is valid
    private void audioParamCheck(int audioSource, int sampleRateInHz, int audioFormat) throws java.lang.IllegalArgumentException {
        // --------------
        // audio source
        if ((audioSource < android.media.MediaRecorder.AudioSource.DEFAULT) || (((audioSource > android.media.MediaRecorder.getAudioSourceMax()) && (audioSource != android.media.MediaRecorder.AudioSource.RADIO_TUNER)) && (audioSource != android.media.MediaRecorder.AudioSource.HOTWORD))) {
            throw new java.lang.IllegalArgumentException("Invalid audio source.");
        }
        mRecordSource = audioSource;
        // --------------
        // sample rate
        if (((sampleRateInHz < android.media.AudioFormat.SAMPLE_RATE_HZ_MIN) || (sampleRateInHz > android.media.AudioFormat.SAMPLE_RATE_HZ_MAX)) && (sampleRateInHz != android.media.AudioFormat.SAMPLE_RATE_UNSPECIFIED)) {
            throw new java.lang.IllegalArgumentException(sampleRateInHz + "Hz is not a supported sample rate.");
        }
        mSampleRate = sampleRateInHz;
        // --------------
        // audio format
        switch (audioFormat) {
            case android.media.AudioFormat.ENCODING_DEFAULT :
                mAudioFormat = android.media.AudioFormat.ENCODING_PCM_16BIT;
                break;
            case android.media.AudioFormat.ENCODING_PCM_FLOAT :
            case android.media.AudioFormat.ENCODING_PCM_16BIT :
            case android.media.AudioFormat.ENCODING_PCM_8BIT :
                mAudioFormat = audioFormat;
                break;
            default :
                throw new java.lang.IllegalArgumentException("Unsupported sample encoding." + " Should be ENCODING_PCM_8BIT, ENCODING_PCM_16BIT, or ENCODING_PCM_FLOAT.");
        }
    }

    // Convenience method for the contructor's audio buffer size check.
    // preconditions:
    // mChannelCount is valid
    // mAudioFormat is AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT,
    // or AudioFormat.ENCODING_PCM_FLOAT
    // postcondition:
    // mNativeBufferSizeInBytes is valid (multiple of frame size, positive)
    private void audioBuffSizeCheck(int audioBufferSize) throws java.lang.IllegalArgumentException {
        // NB: this section is only valid with PCM data.
        // To update when supporting compressed formats
        int frameSizeInBytes = mChannelCount * android.media.AudioFormat.getBytesPerSample(mAudioFormat);
        if (((audioBufferSize % frameSizeInBytes) != 0) || (audioBufferSize < 1)) {
            throw new java.lang.IllegalArgumentException("Invalid audio buffer size.");
        }
        mNativeBufferSizeInBytes = audioBufferSize;
    }

    /**
     * Releases the native AudioRecord resources.
     * The object can no longer be used and the reference should be set to null
     * after a call to release()
     */
    public void release() {
        try {
            stop();
        } catch (java.lang.IllegalStateException ise) {
            // don't raise an exception, we're releasing the resources.
        }
        native_release();
        mState = android.media.AudioRecord.STATE_UNINITIALIZED;
    }

    @java.lang.Override
    protected void finalize() {
        // will cause stop() to be called, and if appropriate, will handle fixed volume recording
        release();
    }

    // --------------------------------------------------------------------------
    // Getters
    // --------------------
    /**
     * Returns the configured audio sink sample rate in Hz.
     * The sink sample rate never changes after construction.
     * If the constructor had a specific sample rate, then the sink sample rate is that value.
     * If the constructor had {@link AudioFormat#SAMPLE_RATE_UNSPECIFIED},
     * then the sink sample rate is a route-dependent default value based on the source [sic].
     */
    public int getSampleRate() {
        return mSampleRate;
    }

    /**
     * Returns the audio recording source.
     *
     * @see MediaRecorder.AudioSource
     */
    public int getAudioSource() {
        return mRecordSource;
    }

    /**
     * Returns the configured audio data encoding. See {@link AudioFormat#ENCODING_PCM_8BIT},
     * {@link AudioFormat#ENCODING_PCM_16BIT}, and {@link AudioFormat#ENCODING_PCM_FLOAT}.
     */
    public int getAudioFormat() {
        return mAudioFormat;
    }

    /**
     * Returns the configured channel position mask.
     * <p> See {@link AudioFormat#CHANNEL_IN_MONO}
     * and {@link AudioFormat#CHANNEL_IN_STEREO}.
     * This method may return {@link AudioFormat#CHANNEL_INVALID} if
     * a channel index mask is used.
     * Consider {@link #getFormat()} instead, to obtain an {@link AudioFormat},
     * which contains both the channel position mask and the channel index mask.
     */
    public int getChannelConfiguration() {
        return mChannelMask;
    }

    /**
     * Returns the configured <code>AudioRecord</code> format.
     *
     * @return an {@link AudioFormat} containing the
    <code>AudioRecord</code> parameters at the time of configuration.
     */
    @android.annotation.NonNull
    public android.media.AudioFormat getFormat() {
        android.media.AudioFormat.Builder builder = new android.media.AudioFormat.Builder().setSampleRate(mSampleRate).setEncoding(mAudioFormat);
        if (mChannelMask != android.media.AudioFormat.CHANNEL_INVALID) {
            builder.setChannelMask(mChannelMask);
        }
        /* 0 */
        if (mChannelIndexMask != android.media.AudioFormat.CHANNEL_INVALID) {
            builder.setChannelIndexMask(mChannelIndexMask);
        }
        return builder.build();
    }

    /**
     * Returns the configured number of channels.
     */
    public int getChannelCount() {
        return mChannelCount;
    }

    /**
     * Returns the state of the AudioRecord instance. This is useful after the
     * AudioRecord instance has been created to check if it was initialized
     * properly. This ensures that the appropriate hardware resources have been
     * acquired.
     *
     * @see AudioRecord#STATE_INITIALIZED
     * @see AudioRecord#STATE_UNINITIALIZED
     */
    public int getState() {
        return mState;
    }

    /**
     * Returns the recording state of the AudioRecord instance.
     *
     * @see AudioRecord#RECORDSTATE_STOPPED
     * @see AudioRecord#RECORDSTATE_RECORDING
     */
    public int getRecordingState() {
        synchronized(mRecordingStateLock) {
            return mRecordingState;
        }
    }

    /**
     * Returns the frame count of the native <code>AudioRecord</code> buffer.
     *  This is greater than or equal to the bufferSizeInBytes converted to frame units
     *  specified in the <code>AudioRecord</code> constructor or Builder.
     *  The native frame count may be enlarged to accommodate the requirements of the
     *  source on creation or if the <code>AudioRecord</code>
     *  is subsequently rerouted.
     *
     * @return current size in frames of the <code>AudioRecord</code> buffer.
     * @throws IllegalStateException
     * 		
     */
    public int getBufferSizeInFrames() {
        return native_get_buffer_size_in_frames();
    }

    /**
     * Returns the notification marker position expressed in frames.
     */
    public int getNotificationMarkerPosition() {
        return native_get_marker_pos();
    }

    /**
     * Returns the notification update period expressed in frames.
     */
    public int getPositionNotificationPeriod() {
        return native_get_pos_update_period();
    }

    /**
     * Poll for an {@link AudioTimestamp} on demand.
     * <p>
     * The AudioTimestamp reflects the frame delivery information at
     * the earliest point available in the capture pipeline.
     * <p>
     * Calling {@link #startRecording()} following a {@link #stop()} will reset
     * the frame count to 0.
     *
     * @param outTimestamp
     * 		a caller provided non-null AudioTimestamp instance,
     * 		which is updated with the AudioRecord frame delivery information upon success.
     * @param timebase
     * 		one of
     * 		{@link AudioTimestamp#TIMEBASE_BOOTTIME AudioTimestamp.TIMEBASE_BOOTTIME} or
     * 		{@link AudioTimestamp#TIMEBASE_MONOTONIC AudioTimestamp.TIMEBASE_MONOTONIC},
     * 		used to select the clock for the AudioTimestamp time.
     * @return {@link #SUCCESS} if a timestamp is available,
    or {@link #ERROR_INVALID_OPERATION} if a timestamp not available.
     */
    public int getTimestamp(@android.annotation.NonNull
    android.media.AudioTimestamp outTimestamp, @android.media.AudioTimestamp.Timebase
    int timebase) {
        if ((outTimestamp == null) || ((timebase != android.media.AudioTimestamp.TIMEBASE_BOOTTIME) && (timebase != android.media.AudioTimestamp.TIMEBASE_MONOTONIC))) {
            throw new java.lang.IllegalArgumentException();
        }
        return native_get_timestamp(outTimestamp, timebase);
    }

    /**
     * Returns the minimum buffer size required for the successful creation of an AudioRecord
     * object, in byte units.
     * Note that this size doesn't guarantee a smooth recording under load, and higher values
     * should be chosen according to the expected frequency at which the AudioRecord instance
     * will be polled for new data.
     * See {@link #AudioRecord(int, int, int, int, int)} for more information on valid
     * configuration values.
     *
     * @param sampleRateInHz
     * 		the sample rate expressed in Hertz.
     * 		{@link AudioFormat#SAMPLE_RATE_UNSPECIFIED} is not permitted.
     * @param channelConfig
     * 		describes the configuration of the audio channels.
     * 		See {@link AudioFormat#CHANNEL_IN_MONO} and
     * 		{@link AudioFormat#CHANNEL_IN_STEREO}
     * @param audioFormat
     * 		the format in which the audio data is represented.
     * 		See {@link AudioFormat#ENCODING_PCM_16BIT}.
     * @return {@link #ERROR_BAD_VALUE} if the recording parameters are not supported by the
    hardware, or an invalid parameter was passed,
    or {@link #ERROR} if the implementation was unable to query the hardware for its
    input properties,
    or the minimum buffer size expressed in bytes.
     * @see #AudioRecord(int, int, int, int, int)
     */
    public static int getMinBufferSize(int sampleRateInHz, int channelConfig, int audioFormat) {
        int channelCount = 0;
        switch (channelConfig) {
            case android.media.AudioFormat.CHANNEL_IN_DEFAULT :
                // AudioFormat.CHANNEL_CONFIGURATION_DEFAULT
            case android.media.AudioFormat.CHANNEL_IN_MONO :
            case android.media.AudioFormat.CHANNEL_CONFIGURATION_MONO :
                channelCount = 1;
                break;
            case android.media.AudioFormat.CHANNEL_IN_STEREO :
            case android.media.AudioFormat.CHANNEL_CONFIGURATION_STEREO :
            case android.media.AudioFormat.CHANNEL_IN_FRONT | android.media.AudioFormat.CHANNEL_IN_BACK :
                channelCount = 2;
                break;
            case android.media.AudioFormat.CHANNEL_INVALID :
            default :
                android.media.AudioRecord.loge("getMinBufferSize(): Invalid channel configuration.");
                return android.media.AudioRecord.ERROR_BAD_VALUE;
        }
        int size = android.media.AudioRecord.native_get_min_buff_size(sampleRateInHz, channelCount, audioFormat);
        if (size == 0) {
            return android.media.AudioRecord.ERROR_BAD_VALUE;
        } else
            if (size == (-1)) {
                return android.media.AudioRecord.ERROR;
            } else {
                return size;
            }

    }

    /**
     * Returns the audio session ID.
     *
     * @return the ID of the audio session this AudioRecord belongs to.
     */
    public int getAudioSessionId() {
        return mSessionId;
    }

    // ---------------------------------------------------------
    // Transport control methods
    // --------------------
    /**
     * Starts recording from the AudioRecord instance.
     *
     * @throws IllegalStateException
     * 		
     */
    public void startRecording() throws java.lang.IllegalStateException {
        if (mState != android.media.AudioRecord.STATE_INITIALIZED) {
            throw new java.lang.IllegalStateException("startRecording() called on an " + "uninitialized AudioRecord.");
        }
        // start recording
        synchronized(mRecordingStateLock) {
            if (native_start(android.media.MediaSyncEvent.SYNC_EVENT_NONE, 0) == android.media.AudioRecord.SUCCESS) {
                handleFullVolumeRec(true);
                mRecordingState = android.media.AudioRecord.RECORDSTATE_RECORDING;
            }
        }
    }

    /**
     * Starts recording from the AudioRecord instance when the specified synchronization event
     * occurs on the specified audio session.
     *
     * @throws IllegalStateException
     * 		
     * @param syncEvent
     * 		event that triggers the capture.
     * @see MediaSyncEvent
     */
    public void startRecording(android.media.MediaSyncEvent syncEvent) throws java.lang.IllegalStateException {
        if (mState != android.media.AudioRecord.STATE_INITIALIZED) {
            throw new java.lang.IllegalStateException("startRecording() called on an " + "uninitialized AudioRecord.");
        }
        // start recording
        synchronized(mRecordingStateLock) {
            if (native_start(syncEvent.getType(), syncEvent.getAudioSessionId()) == android.media.AudioRecord.SUCCESS) {
                handleFullVolumeRec(true);
                mRecordingState = android.media.AudioRecord.RECORDSTATE_RECORDING;
            }
        }
    }

    /**
     * Stops recording.
     *
     * @throws IllegalStateException
     * 		
     */
    public void stop() throws java.lang.IllegalStateException {
        if (mState != android.media.AudioRecord.STATE_INITIALIZED) {
            throw new java.lang.IllegalStateException("stop() called on an uninitialized AudioRecord.");
        }
        // stop recording
        synchronized(mRecordingStateLock) {
            handleFullVolumeRec(false);
            native_stop();
            mRecordingState = android.media.AudioRecord.RECORDSTATE_STOPPED;
        }
    }

    private final android.os.IBinder mICallBack = new android.os.Binder();

    private void handleFullVolumeRec(boolean starting) {
        if (!mIsSubmixFullVolume) {
            return;
        }
        final android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.AUDIO_SERVICE);
        final android.media.IAudioService ias = IAudioService.Stub.asInterface(b);
        try {
            ias.forceRemoteSubmixFullVolume(starting, mICallBack);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.media.AudioRecord.TAG, "Error talking to AudioService when handling full submix volume", e);
        }
    }

    // ---------------------------------------------------------
    // Audio data supply
    // --------------------
    /**
     * Reads audio data from the audio hardware for recording into a byte array.
     * The format specified in the AudioRecord constructor should be
     * {@link AudioFormat#ENCODING_PCM_8BIT} to correspond to the data in the array.
     *
     * @param audioData
     * 		the array to which the recorded audio data is written.
     * @param offsetInBytes
     * 		index in audioData from which the data is written expressed in bytes.
     * @param sizeInBytes
     * 		the number of requested bytes.
     * @return zero or the positive number of bytes that were read, or one of the following
    error codes. The number of bytes will not exceed sizeInBytes.
    <ul>
    <li>{@link #ERROR_INVALID_OPERATION} if the object isn't properly initialized</li>
    <li>{@link #ERROR_BAD_VALUE} if the parameters don't resolve to valid data and indexes</li>
    <li>{@link #ERROR_DEAD_OBJECT} if the object is not valid anymore and
    needs to be recreated. The dead object error code is not returned if some data was
    successfully transferred. In this case, the error is returned at the next read()</li>
    <li>{@link #ERROR} in case of other error</li>
    </ul>
     */
    public int read(@android.annotation.NonNull
    byte[] audioData, int offsetInBytes, int sizeInBytes) {
        return read(audioData, offsetInBytes, sizeInBytes, android.media.AudioRecord.READ_BLOCKING);
    }

    /**
     * Reads audio data from the audio hardware for recording into a byte array.
     * The format specified in the AudioRecord constructor should be
     * {@link AudioFormat#ENCODING_PCM_8BIT} to correspond to the data in the array.
     * The format can be {@link AudioFormat#ENCODING_PCM_16BIT}, but this is deprecated.
     *
     * @param audioData
     * 		the array to which the recorded audio data is written.
     * @param offsetInBytes
     * 		index in audioData to which the data is written expressed in bytes.
     * 		Must not be negative, or cause the data access to go out of bounds of the array.
     * @param sizeInBytes
     * 		the number of requested bytes.
     * 		Must not be negative, or cause the data access to go out of bounds of the array.
     * @param readMode
     * 		one of {@link #READ_BLOCKING}, {@link #READ_NON_BLOCKING}.
     * 		<br>With {@link #READ_BLOCKING}, the read will block until all the requested data
     * 		is read.
     * 		<br>With {@link #READ_NON_BLOCKING}, the read will return immediately after
     * 		reading as much audio data as possible without blocking.
     * @return zero or the positive number of bytes that were read, or one of the following
    error codes. The number of bytes will be a multiple of the frame size in bytes
    not to exceed sizeInBytes.
    <ul>
    <li>{@link #ERROR_INVALID_OPERATION} if the object isn't properly initialized</li>
    <li>{@link #ERROR_BAD_VALUE} if the parameters don't resolve to valid data and indexes</li>
    <li>{@link #ERROR_DEAD_OBJECT} if the object is not valid anymore and
    needs to be recreated. The dead object error code is not returned if some data was
    successfully transferred. In this case, the error is returned at the next read()</li>
    <li>{@link #ERROR} in case of other error</li>
    </ul>
     */
    public int read(@android.annotation.NonNull
    byte[] audioData, int offsetInBytes, int sizeInBytes, @android.media.AudioRecord.ReadMode
    int readMode) {
        if ((mState != android.media.AudioRecord.STATE_INITIALIZED) || (mAudioFormat == android.media.AudioFormat.ENCODING_PCM_FLOAT)) {
            return android.media.AudioRecord.ERROR_INVALID_OPERATION;
        }
        if ((readMode != android.media.AudioRecord.READ_BLOCKING) && (readMode != android.media.AudioRecord.READ_NON_BLOCKING)) {
            android.util.Log.e(android.media.AudioRecord.TAG, "AudioRecord.read() called with invalid blocking mode");
            return android.media.AudioRecord.ERROR_BAD_VALUE;
        }
        if (((((audioData == null) || (offsetInBytes < 0)) || (sizeInBytes < 0)) || ((offsetInBytes + sizeInBytes) < 0))// detect integer overflow
         || ((offsetInBytes + sizeInBytes) > audioData.length)) {
            return android.media.AudioRecord.ERROR_BAD_VALUE;
        }
        return native_read_in_byte_array(audioData, offsetInBytes, sizeInBytes, readMode == android.media.AudioRecord.READ_BLOCKING);
    }

    /**
     * Reads audio data from the audio hardware for recording into a short array.
     * The format specified in the AudioRecord constructor should be
     * {@link AudioFormat#ENCODING_PCM_16BIT} to correspond to the data in the array.
     *
     * @param audioData
     * 		the array to which the recorded audio data is written.
     * @param offsetInShorts
     * 		index in audioData to which the data is written expressed in shorts.
     * 		Must not be negative, or cause the data access to go out of bounds of the array.
     * @param sizeInShorts
     * 		the number of requested shorts.
     * 		Must not be negative, or cause the data access to go out of bounds of the array.
     * @return zero or the positive number of shorts that were read, or one of the following
    error codes. The number of shorts will be a multiple of the channel count not to exceed
    sizeInShorts.
    <ul>
    <li>{@link #ERROR_INVALID_OPERATION} if the object isn't properly initialized</li>
    <li>{@link #ERROR_BAD_VALUE} if the parameters don't resolve to valid data and indexes</li>
    <li>{@link #ERROR_DEAD_OBJECT} if the object is not valid anymore and
    needs to be recreated. The dead object error code is not returned if some data was
    successfully transferred. In this case, the error is returned at the next read()</li>
    <li>{@link #ERROR} in case of other error</li>
    </ul>
     */
    public int read(@android.annotation.NonNull
    short[] audioData, int offsetInShorts, int sizeInShorts) {
        return read(audioData, offsetInShorts, sizeInShorts, android.media.AudioRecord.READ_BLOCKING);
    }

    /**
     * Reads audio data from the audio hardware for recording into a short array.
     * The format specified in the AudioRecord constructor should be
     * {@link AudioFormat#ENCODING_PCM_16BIT} to correspond to the data in the array.
     *
     * @param audioData
     * 		the array to which the recorded audio data is written.
     * @param offsetInShorts
     * 		index in audioData from which the data is written expressed in shorts.
     * 		Must not be negative, or cause the data access to go out of bounds of the array.
     * @param sizeInShorts
     * 		the number of requested shorts.
     * 		Must not be negative, or cause the data access to go out of bounds of the array.
     * @param readMode
     * 		one of {@link #READ_BLOCKING}, {@link #READ_NON_BLOCKING}.
     * 		<br>With {@link #READ_BLOCKING}, the read will block until all the requested data
     * 		is read.
     * 		<br>With {@link #READ_NON_BLOCKING}, the read will return immediately after
     * 		reading as much audio data as possible without blocking.
     * @return zero or the positive number of shorts that were read, or one of the following
    error codes. The number of shorts will be a multiple of the channel count not to exceed
    sizeInShorts.
    <ul>
    <li>{@link #ERROR_INVALID_OPERATION} if the object isn't properly initialized</li>
    <li>{@link #ERROR_BAD_VALUE} if the parameters don't resolve to valid data and indexes</li>
    <li>{@link #ERROR_DEAD_OBJECT} if the object is not valid anymore and
    needs to be recreated. The dead object error code is not returned if some data was
    successfully transferred. In this case, the error is returned at the next read()</li>
    <li>{@link #ERROR} in case of other error</li>
    </ul>
     */
    public int read(@android.annotation.NonNull
    short[] audioData, int offsetInShorts, int sizeInShorts, @android.media.AudioRecord.ReadMode
    int readMode) {
        if ((mState != android.media.AudioRecord.STATE_INITIALIZED) || (mAudioFormat == android.media.AudioFormat.ENCODING_PCM_FLOAT)) {
            return android.media.AudioRecord.ERROR_INVALID_OPERATION;
        }
        if ((readMode != android.media.AudioRecord.READ_BLOCKING) && (readMode != android.media.AudioRecord.READ_NON_BLOCKING)) {
            android.util.Log.e(android.media.AudioRecord.TAG, "AudioRecord.read() called with invalid blocking mode");
            return android.media.AudioRecord.ERROR_BAD_VALUE;
        }
        if (((((audioData == null) || (offsetInShorts < 0)) || (sizeInShorts < 0)) || ((offsetInShorts + sizeInShorts) < 0))// detect integer overflow
         || ((offsetInShorts + sizeInShorts) > audioData.length)) {
            return android.media.AudioRecord.ERROR_BAD_VALUE;
        }
        return native_read_in_short_array(audioData, offsetInShorts, sizeInShorts, readMode == android.media.AudioRecord.READ_BLOCKING);
    }

    /**
     * Reads audio data from the audio hardware for recording into a float array.
     * The format specified in the AudioRecord constructor should be
     * {@link AudioFormat#ENCODING_PCM_FLOAT} to correspond to the data in the array.
     *
     * @param audioData
     * 		the array to which the recorded audio data is written.
     * @param offsetInFloats
     * 		index in audioData from which the data is written.
     * 		Must not be negative, or cause the data access to go out of bounds of the array.
     * @param sizeInFloats
     * 		the number of requested floats.
     * 		Must not be negative, or cause the data access to go out of bounds of the array.
     * @param readMode
     * 		one of {@link #READ_BLOCKING}, {@link #READ_NON_BLOCKING}.
     * 		<br>With {@link #READ_BLOCKING}, the read will block until all the requested data
     * 		is read.
     * 		<br>With {@link #READ_NON_BLOCKING}, the read will return immediately after
     * 		reading as much audio data as possible without blocking.
     * @return zero or the positive number of floats that were read, or one of the following
    error codes. The number of floats will be a multiple of the channel count not to exceed
    sizeInFloats.
    <ul>
    <li>{@link #ERROR_INVALID_OPERATION} if the object isn't properly initialized</li>
    <li>{@link #ERROR_BAD_VALUE} if the parameters don't resolve to valid data and indexes</li>
    <li>{@link #ERROR_DEAD_OBJECT} if the object is not valid anymore and
    needs to be recreated. The dead object error code is not returned if some data was
    successfully transferred. In this case, the error is returned at the next read()</li>
    <li>{@link #ERROR} in case of other error</li>
    </ul>
     */
    public int read(@android.annotation.NonNull
    float[] audioData, int offsetInFloats, int sizeInFloats, @android.media.AudioRecord.ReadMode
    int readMode) {
        if (mState == android.media.AudioRecord.STATE_UNINITIALIZED) {
            android.util.Log.e(android.media.AudioRecord.TAG, "AudioRecord.read() called in invalid state STATE_UNINITIALIZED");
            return android.media.AudioRecord.ERROR_INVALID_OPERATION;
        }
        if (mAudioFormat != android.media.AudioFormat.ENCODING_PCM_FLOAT) {
            android.util.Log.e(android.media.AudioRecord.TAG, "AudioRecord.read(float[] ...) requires format ENCODING_PCM_FLOAT");
            return android.media.AudioRecord.ERROR_INVALID_OPERATION;
        }
        if ((readMode != android.media.AudioRecord.READ_BLOCKING) && (readMode != android.media.AudioRecord.READ_NON_BLOCKING)) {
            android.util.Log.e(android.media.AudioRecord.TAG, "AudioRecord.read() called with invalid blocking mode");
            return android.media.AudioRecord.ERROR_BAD_VALUE;
        }
        if (((((audioData == null) || (offsetInFloats < 0)) || (sizeInFloats < 0)) || ((offsetInFloats + sizeInFloats) < 0))// detect integer overflow
         || ((offsetInFloats + sizeInFloats) > audioData.length)) {
            return android.media.AudioRecord.ERROR_BAD_VALUE;
        }
        return native_read_in_float_array(audioData, offsetInFloats, sizeInFloats, readMode == android.media.AudioRecord.READ_BLOCKING);
    }

    /**
     * Reads audio data from the audio hardware for recording into a direct buffer. If this buffer
     * is not a direct buffer, this method will always return 0.
     * Note that the value returned by {@link java.nio.Buffer#position()} on this buffer is
     * unchanged after a call to this method.
     * The representation of the data in the buffer will depend on the format specified in
     * the AudioRecord constructor, and will be native endian.
     *
     * @param audioBuffer
     * 		the direct buffer to which the recorded audio data is written.
     * 		Data is written to audioBuffer.position().
     * @param sizeInBytes
     * 		the number of requested bytes. It is recommended but not enforced
     * 		that the number of bytes requested be a multiple of the frame size (sample size in
     * 		bytes multiplied by the channel count).
     * @return zero or the positive number of bytes that were read, or one of the following
    error codes. The number of bytes will not exceed sizeInBytes and will be truncated to be
    a multiple of the frame size.
    <ul>
    <li>{@link #ERROR_INVALID_OPERATION} if the object isn't properly initialized</li>
    <li>{@link #ERROR_BAD_VALUE} if the parameters don't resolve to valid data and indexes</li>
    <li>{@link #ERROR_DEAD_OBJECT} if the object is not valid anymore and
    needs to be recreated. The dead object error code is not returned if some data was
    successfully transferred. In this case, the error is returned at the next read()</li>
    <li>{@link #ERROR} in case of other error</li>
    </ul>
     */
    public int read(@android.annotation.NonNull
    java.nio.ByteBuffer audioBuffer, int sizeInBytes) {
        return read(audioBuffer, sizeInBytes, android.media.AudioRecord.READ_BLOCKING);
    }

    /**
     * Reads audio data from the audio hardware for recording into a direct buffer. If this buffer
     * is not a direct buffer, this method will always return 0.
     * Note that the value returned by {@link java.nio.Buffer#position()} on this buffer is
     * unchanged after a call to this method.
     * The representation of the data in the buffer will depend on the format specified in
     * the AudioRecord constructor, and will be native endian.
     *
     * @param audioBuffer
     * 		the direct buffer to which the recorded audio data is written.
     * 		Data is written to audioBuffer.position().
     * @param sizeInBytes
     * 		the number of requested bytes. It is recommended but not enforced
     * 		that the number of bytes requested be a multiple of the frame size (sample size in
     * 		bytes multiplied by the channel count).
     * @param readMode
     * 		one of {@link #READ_BLOCKING}, {@link #READ_NON_BLOCKING}.
     * 		<br>With {@link #READ_BLOCKING}, the read will block until all the requested data
     * 		is read.
     * 		<br>With {@link #READ_NON_BLOCKING}, the read will return immediately after
     * 		reading as much audio data as possible without blocking.
     * @return zero or the positive number of bytes that were read, or one of the following
    error codes. The number of bytes will not exceed sizeInBytes and will be truncated to be
    a multiple of the frame size.
    <ul>
    <li>{@link #ERROR_INVALID_OPERATION} if the object isn't properly initialized</li>
    <li>{@link #ERROR_BAD_VALUE} if the parameters don't resolve to valid data and indexes</li>
    <li>{@link #ERROR_DEAD_OBJECT} if the object is not valid anymore and
    needs to be recreated. The dead object error code is not returned if some data was
    successfully transferred. In this case, the error is returned at the next read()</li>
    <li>{@link #ERROR} in case of other error</li>
    </ul>
     */
    public int read(@android.annotation.NonNull
    java.nio.ByteBuffer audioBuffer, int sizeInBytes, @android.media.AudioRecord.ReadMode
    int readMode) {
        if (mState != android.media.AudioRecord.STATE_INITIALIZED) {
            return android.media.AudioRecord.ERROR_INVALID_OPERATION;
        }
        if ((readMode != android.media.AudioRecord.READ_BLOCKING) && (readMode != android.media.AudioRecord.READ_NON_BLOCKING)) {
            android.util.Log.e(android.media.AudioRecord.TAG, "AudioRecord.read() called with invalid blocking mode");
            return android.media.AudioRecord.ERROR_BAD_VALUE;
        }
        if ((audioBuffer == null) || (sizeInBytes < 0)) {
            return android.media.AudioRecord.ERROR_BAD_VALUE;
        }
        return native_read_in_direct_buffer(audioBuffer, sizeInBytes, readMode == android.media.AudioRecord.READ_BLOCKING);
    }

    // --------------------------------------------------------------------------
    // Initialization / configuration
    // --------------------
    /**
     * Sets the listener the AudioRecord notifies when a previously set marker is reached or
     * for each periodic record head position update.
     *
     * @param listener
     * 		
     */
    public void setRecordPositionUpdateListener(android.media.AudioRecord.OnRecordPositionUpdateListener listener) {
        setRecordPositionUpdateListener(listener, null);
    }

    /**
     * Sets the listener the AudioRecord notifies when a previously set marker is reached or
     * for each periodic record head position update.
     * Use this method to receive AudioRecord events in the Handler associated with another
     * thread than the one in which you created the AudioRecord instance.
     *
     * @param listener
     * 		
     * @param handler
     * 		the Handler that will receive the event notification messages.
     */
    public void setRecordPositionUpdateListener(android.media.AudioRecord.OnRecordPositionUpdateListener listener, android.os.Handler handler) {
        synchronized(mPositionListenerLock) {
            mPositionListener = listener;
            if (listener != null) {
                if (handler != null) {
                    mEventHandler = new android.media.AudioRecord.NativeEventHandler(this, handler.getLooper());
                } else {
                    // no given handler, use the looper the AudioRecord was created in
                    mEventHandler = new android.media.AudioRecord.NativeEventHandler(this, mInitializationLooper);
                }
            } else {
                mEventHandler = null;
            }
        }
    }

    /**
     * Sets the marker position at which the listener is called, if set with
     * {@link #setRecordPositionUpdateListener(OnRecordPositionUpdateListener)} or
     * {@link #setRecordPositionUpdateListener(OnRecordPositionUpdateListener, Handler)}.
     *
     * @param markerInFrames
     * 		marker position expressed in frames
     * @return error code or success, see {@link #SUCCESS}, {@link #ERROR_BAD_VALUE},
    {@link #ERROR_INVALID_OPERATION}
     */
    public int setNotificationMarkerPosition(int markerInFrames) {
        if (mState == android.media.AudioRecord.STATE_UNINITIALIZED) {
            return android.media.AudioRecord.ERROR_INVALID_OPERATION;
        }
        return native_set_marker_pos(markerInFrames);
    }

    /**
     * Returns an {@link AudioDeviceInfo} identifying the current routing of this AudioRecord.
     * Note: The query is only valid if the AudioRecord is currently recording. If it is not,
     * <code>getRoutedDevice()</code> will return null.
     */
    @java.lang.Override
    public android.media.AudioDeviceInfo getRoutedDevice() {
        int deviceId = native_getRoutedDeviceId();
        if (deviceId == 0) {
            return null;
        }
        android.media.AudioDeviceInfo[] devices = android.media.AudioManager.getDevicesStatic(android.media.AudioManager.GET_DEVICES_INPUTS);
        for (int i = 0; i < devices.length; i++) {
            if (devices[i].getId() == deviceId) {
                return devices[i];
            }
        }
        return null;
    }

    /* Call BEFORE adding a routing callback handler. */
    private void testEnableNativeRoutingCallbacksLocked() {
        if (mRoutingChangeListeners.size() == 0) {
            native_enableDeviceCallback();
        }
    }

    /* Call AFTER removing a routing callback handler. */
    private void testDisableNativeRoutingCallbacksLocked() {
        if (mRoutingChangeListeners.size() == 0) {
            native_disableDeviceCallback();
        }
    }

    // --------------------------------------------------------------------------
    // (Re)Routing Info
    // --------------------
    /**
     * The list of AudioRouting.OnRoutingChangedListener interfaces added (with
     * {@link AudioRecord#addOnRoutingChangedListener} by an app to receive
     * (re)routing notifications.
     */
    @com.android.internal.annotations.GuardedBy("mRoutingChangeListeners")
    private android.util.ArrayMap<android.media.AudioRouting.OnRoutingChangedListener, android.media.AudioRecord.NativeRoutingEventHandlerDelegate> mRoutingChangeListeners = new android.util.ArrayMap<>();

    /**
     * Adds an {@link AudioRouting.OnRoutingChangedListener} to receive notifications of
     * routing changes on this AudioRecord.
     *
     * @param listener
     * 		The {@link AudioRouting.OnRoutingChangedListener} interface to receive
     * 		notifications of rerouting events.
     * @param handler
     * 		Specifies the {@link Handler} object for the thread on which to execute
     * 		the callback. If <code>null</code>, the {@link Handler} associated with the main
     * 		{@link Looper} will be used.
     */
    @java.lang.Override
    public void addOnRoutingChangedListener(android.media.AudioRouting.OnRoutingChangedListener listener, android.os.Handler handler) {
        synchronized(mRoutingChangeListeners) {
            if ((listener != null) && (!mRoutingChangeListeners.containsKey(listener))) {
                testEnableNativeRoutingCallbacksLocked();
                mRoutingChangeListeners.put(listener, new android.media.AudioRecord.NativeRoutingEventHandlerDelegate(this, listener, handler != null ? handler : new android.os.Handler(mInitializationLooper)));
            }
        }
    }

    /**
     * Removes an {@link AudioRouting.OnRoutingChangedListener} which has been previously added
     * to receive rerouting notifications.
     *
     * @param listener
     * 		The previously added {@link AudioRouting.OnRoutingChangedListener} interface
     * 		to remove.
     */
    @java.lang.Override
    public void removeOnRoutingChangedListener(android.media.AudioRouting.OnRoutingChangedListener listener) {
        synchronized(mRoutingChangeListeners) {
            if (mRoutingChangeListeners.containsKey(listener)) {
                mRoutingChangeListeners.remove(listener);
                testDisableNativeRoutingCallbacksLocked();
            }
        }
    }

    // --------------------------------------------------------------------------
    // (Re)Routing Info
    // --------------------
    /**
     * Defines the interface by which applications can receive notifications of
     * routing changes for the associated {@link AudioRecord}.
     *
     * @deprecated users should switch to the general purpose
    {@link AudioRouting.OnRoutingChangedListener} class instead.
     */
    @java.lang.Deprecated
    public interface OnRoutingChangedListener extends android.media.AudioRouting.OnRoutingChangedListener {
        /**
         * Called when the routing of an AudioRecord changes from either and
         * explicit or policy rerouting. Use {@link #getRoutedDevice()} to
         * retrieve the newly routed-from device.
         */
        public void onRoutingChanged(android.media.AudioRecord audioRecord);

        @java.lang.Override
        public default void onRoutingChanged(android.media.AudioRouting router) {
            if (router instanceof android.media.AudioRecord) {
                onRoutingChanged(((android.media.AudioRecord) (router)));
            }
        }
    }

    /**
     * Adds an {@link OnRoutingChangedListener} to receive notifications of routing changes
     * on this AudioRecord.
     *
     * @param listener
     * 		The {@link OnRoutingChangedListener} interface to receive notifications
     * 		of rerouting events.
     * @param handler
     * 		Specifies the {@link Handler} object for the thread on which to execute
     * 		the callback. If <code>null</code>, the {@link Handler} associated with the main
     * 		{@link Looper} will be used.
     * @deprecated users should switch to the general purpose
    {@link AudioRouting.OnRoutingChangedListener} class instead.
     */
    @java.lang.Deprecated
    public void addOnRoutingChangedListener(android.media.AudioRecord.OnRoutingChangedListener listener, android.os.Handler handler) {
        addOnRoutingChangedListener(((android.media.AudioRouting.OnRoutingChangedListener) (listener)), handler);
    }

    /**
     * Removes an {@link OnRoutingChangedListener} which has been previously added
     * to receive rerouting notifications.
     *
     * @param listener
     * 		The previously added {@link OnRoutingChangedListener} interface to remove.
     * @deprecated users should switch to the general purpose
    {@link AudioRouting.OnRoutingChangedListener} class instead.
     */
    @java.lang.Deprecated
    public void removeOnRoutingChangedListener(android.media.AudioRecord.OnRoutingChangedListener listener) {
        removeOnRoutingChangedListener(((android.media.AudioRouting.OnRoutingChangedListener) (listener)));
    }

    /**
     * Helper class to handle the forwarding of native events to the appropriate listener
     * (potentially) handled in a different thread
     */
    private class NativeRoutingEventHandlerDelegate {
        private final android.os.Handler mHandler;

        NativeRoutingEventHandlerDelegate(final android.media.AudioRecord record, final android.media.AudioRouting.OnRoutingChangedListener listener, android.os.Handler handler) {
            // find the looper for our new event handler
            android.os.Looper looper;
            if (handler != null) {
                looper = handler.getLooper();
            } else {
                // no given handler, use the looper the AudioRecord was created in
                looper = mInitializationLooper;
            }
            // construct the event handler with this looper
            if (looper != null) {
                // implement the event handler delegate
                mHandler = new android.os.Handler(looper) {
                    @java.lang.Override
                    public void handleMessage(android.os.Message msg) {
                        if (record == null) {
                            return;
                        }
                        switch (msg.what) {
                            case android.media.AudioSystem.NATIVE_EVENT_ROUTING_CHANGE :
                                if (listener != null) {
                                    listener.onRoutingChanged(record);
                                }
                                break;
                            default :
                                android.media.AudioRecord.loge("Unknown native event type: " + msg.what);
                                break;
                        }
                    }
                };
            } else {
                mHandler = null;
            }
        }

        android.os.Handler getHandler() {
            return mHandler;
        }
    }

    /**
     * Sends device list change notification to all listeners.
     */
    private void broadcastRoutingChange() {
        android.media.AudioManager.resetAudioPortGeneration();
        synchronized(mRoutingChangeListeners) {
            for (android.media.AudioRecord.NativeRoutingEventHandlerDelegate delegate : mRoutingChangeListeners.values()) {
                android.os.Handler handler = delegate.getHandler();
                if (handler != null) {
                    handler.sendEmptyMessage(android.media.AudioSystem.NATIVE_EVENT_ROUTING_CHANGE);
                }
            }
        }
    }

    /**
     * Sets the period at which the listener is called, if set with
     * {@link #setRecordPositionUpdateListener(OnRecordPositionUpdateListener)} or
     * {@link #setRecordPositionUpdateListener(OnRecordPositionUpdateListener, Handler)}.
     * It is possible for notifications to be lost if the period is too small.
     *
     * @param periodInFrames
     * 		update period expressed in frames
     * @return error code or success, see {@link #SUCCESS}, {@link #ERROR_INVALID_OPERATION}
     */
    public int setPositionNotificationPeriod(int periodInFrames) {
        if (mState == android.media.AudioRecord.STATE_UNINITIALIZED) {
            return android.media.AudioRecord.ERROR_INVALID_OPERATION;
        }
        return native_set_pos_update_period(periodInFrames);
    }

    // --------------------------------------------------------------------------
    // Explicit Routing
    // --------------------
    private android.media.AudioDeviceInfo mPreferredDevice = null;

    /**
     * Specifies an audio device (via an {@link AudioDeviceInfo} object) to route
     * the input to this AudioRecord.
     *
     * @param deviceInfo
     * 		The {@link AudioDeviceInfo} specifying the audio source.
     * 		If deviceInfo is null, default routing is restored.
     * @return true if successful, false if the specified {@link AudioDeviceInfo} is non-null and
    does not correspond to a valid audio input device.
     */
    @java.lang.Override
    public boolean setPreferredDevice(android.media.AudioDeviceInfo deviceInfo) {
        // Do some validation....
        if ((deviceInfo != null) && (!deviceInfo.isSource())) {
            return false;
        }
        int preferredDeviceId = (deviceInfo != null) ? deviceInfo.getId() : 0;
        boolean status = native_setInputDevice(preferredDeviceId);
        if (status == true) {
            synchronized(this) {
                mPreferredDevice = deviceInfo;
            }
        }
        return status;
    }

    /**
     * Returns the selected input specified by {@link #setPreferredDevice}. Note that this
     * is not guarenteed to correspond to the actual device being used for recording.
     */
    @java.lang.Override
    public android.media.AudioDeviceInfo getPreferredDevice() {
        synchronized(this) {
            return mPreferredDevice;
        }
    }

    // ---------------------------------------------------------
    // Interface definitions
    // --------------------
    /**
     * Interface definition for a callback to be invoked when an AudioRecord has
     * reached a notification marker set by {@link AudioRecord#setNotificationMarkerPosition(int)}
     * or for periodic updates on the progress of the record head, as set by
     * {@link AudioRecord#setPositionNotificationPeriod(int)}.
     */
    public interface OnRecordPositionUpdateListener {
        /**
         * Called on the listener to notify it that the previously set marker has been reached
         * by the recording head.
         */
        void onMarkerReached(android.media.AudioRecord recorder);

        /**
         * Called on the listener to periodically notify it that the record head has reached
         * a multiple of the notification period.
         */
        void onPeriodicNotification(android.media.AudioRecord recorder);
    }

    // ---------------------------------------------------------
    // Inner classes
    // --------------------
    /**
     * Helper class to handle the forwarding of native events to the appropriate listener
     * (potentially) handled in a different thread
     */
    private class NativeEventHandler extends android.os.Handler {
        private final android.media.AudioRecord mAudioRecord;

        NativeEventHandler(android.media.AudioRecord recorder, android.os.Looper looper) {
            super(looper);
            mAudioRecord = recorder;
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            android.media.AudioRecord.OnRecordPositionUpdateListener listener = null;
            synchronized(mPositionListenerLock) {
                listener = mAudioRecord.mPositionListener;
            }
            switch (msg.what) {
                case android.media.AudioRecord.NATIVE_EVENT_MARKER :
                    if (listener != null) {
                        listener.onMarkerReached(mAudioRecord);
                    }
                    break;
                case android.media.AudioRecord.NATIVE_EVENT_NEW_POS :
                    if (listener != null) {
                        listener.onPeriodicNotification(mAudioRecord);
                    }
                    break;
                default :
                    android.media.AudioRecord.loge("Unknown native event type: " + msg.what);
                    break;
            }
        }
    }

    // ---------------------------------------------------------
    // Java methods called from the native side
    // --------------------
    @java.lang.SuppressWarnings("unused")
    private static void postEventFromNative(java.lang.Object audiorecord_ref, int what, int arg1, int arg2, java.lang.Object obj) {
        // logd("Event posted from the native side: event="+ what + " args="+ arg1+" "+arg2);
        android.media.AudioRecord recorder = ((android.media.AudioRecord) (((java.lang.ref.WeakReference) (audiorecord_ref)).get()));
        if (recorder == null) {
            return;
        }
        if (what == android.media.AudioSystem.NATIVE_EVENT_ROUTING_CHANGE) {
            recorder.broadcastRoutingChange();
            return;
        }
        if (recorder.mEventHandler != null) {
            android.os.Message m = recorder.mEventHandler.obtainMessage(what, arg1, arg2, obj);
            recorder.mEventHandler.sendMessage(m);
        }
    }

    // ---------------------------------------------------------
    // Native methods called from the Java side
    // --------------------
    private final native int native_setup(java.lang.Object audiorecord_this, java.lang.Object attributes, /* AudioAttributes */
    int[] sampleRate, int channelMask, int channelIndexMask, int audioFormat, int buffSizeInBytes, int[] sessionId, java.lang.String opPackageName, long nativeRecordInJavaObj);

    // TODO remove: implementation calls directly into implementation of native_release()
    private final native void native_finalize();

    /**
     *
     *
     * @unknown 
     */
    public final native void native_release();

    private final native int native_start(int syncEvent, int sessionId);

    private final native void native_stop();

    private final native int native_read_in_byte_array(byte[] audioData, int offsetInBytes, int sizeInBytes, boolean isBlocking);

    private final native int native_read_in_short_array(short[] audioData, int offsetInShorts, int sizeInShorts, boolean isBlocking);

    private final native int native_read_in_float_array(float[] audioData, int offsetInFloats, int sizeInFloats, boolean isBlocking);

    private final native int native_read_in_direct_buffer(java.lang.Object jBuffer, int sizeInBytes, boolean isBlocking);

    private final native int native_get_buffer_size_in_frames();

    private final native int native_set_marker_pos(int marker);

    private final native int native_get_marker_pos();

    private final native int native_set_pos_update_period(int updatePeriod);

    private final native int native_get_pos_update_period();

    private static final native int native_get_min_buff_size(int sampleRateInHz, int channelCount, int audioFormat);

    private final native boolean native_setInputDevice(int deviceId);

    private final native int native_getRoutedDeviceId();

    private final native void native_enableDeviceCallback();

    private final native void native_disableDeviceCallback();

    private final native int native_get_timestamp(@android.annotation.NonNull
    android.media.AudioTimestamp outTimestamp, @android.media.AudioTimestamp.Timebase
    int timebase);

    // ---------------------------------------------------------
    // Utility methods
    // ------------------
    private static void logd(java.lang.String msg) {
        android.util.Log.d(android.media.AudioRecord.TAG, msg);
    }

    private static void loge(java.lang.String msg) {
        android.util.Log.e(android.media.AudioRecord.TAG, msg);
    }
}

