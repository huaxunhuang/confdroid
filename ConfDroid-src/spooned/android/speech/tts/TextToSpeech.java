/**
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.speech.tts;


/**
 * Synthesizes speech from text for immediate playback or to create a sound file.
 * <p>A TextToSpeech instance can only be used to synthesize text once it has completed its
 * initialization. Implement the {@link TextToSpeech.OnInitListener} to be
 * notified of the completion of the initialization.<br>
 * When you are done using the TextToSpeech instance, call the {@link #shutdown()} method
 * to release the native resources used by the TextToSpeech engine.
 */
public class TextToSpeech {
    private static final java.lang.String TAG = "TextToSpeech";

    /**
     * Denotes a successful operation.
     */
    public static final int SUCCESS = 0;

    /**
     * Denotes a generic operation failure.
     */
    public static final int ERROR = -1;

    /**
     * Denotes a stop requested by a client. It's used only on the service side of the API,
     * client should never expect to see this result code.
     */
    public static final int STOPPED = -2;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.speech.tts.TextToSpeech.ERROR_SYNTHESIS, android.speech.tts.TextToSpeech.ERROR_SERVICE, android.speech.tts.TextToSpeech.ERROR_OUTPUT, android.speech.tts.TextToSpeech.ERROR_NETWORK, android.speech.tts.TextToSpeech.ERROR_NETWORK_TIMEOUT, android.speech.tts.TextToSpeech.ERROR_INVALID_REQUEST, android.speech.tts.TextToSpeech.ERROR_NOT_INSTALLED_YET })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Error {}

    /**
     * Denotes a failure of a TTS engine to synthesize the given input.
     */
    public static final int ERROR_SYNTHESIS = -3;

    /**
     * Denotes a failure of a TTS service.
     */
    public static final int ERROR_SERVICE = -4;

    /**
     * Denotes a failure related to the output (audio device or a file).
     */
    public static final int ERROR_OUTPUT = -5;

    /**
     * Denotes a failure caused by a network connectivity problems.
     */
    public static final int ERROR_NETWORK = -6;

    /**
     * Denotes a failure caused by network timeout.
     */
    public static final int ERROR_NETWORK_TIMEOUT = -7;

    /**
     * Denotes a failure caused by an invalid request.
     */
    public static final int ERROR_INVALID_REQUEST = -8;

    /**
     * Denotes a failure caused by an unfinished download of the voice data.
     *
     * @see Engine#KEY_FEATURE_NOT_INSTALLED
     */
    public static final int ERROR_NOT_INSTALLED_YET = -9;

    /**
     * Queue mode where all entries in the playback queue (media to be played
     * and text to be synthesized) are dropped and replaced by the new entry.
     * Queues are flushed with respect to a given calling app. Entries in the queue
     * from other callees are not discarded.
     */
    public static final int QUEUE_FLUSH = 0;

    /**
     * Queue mode where the new entry is added at the end of the playback queue.
     */
    public static final int QUEUE_ADD = 1;

    /**
     * Queue mode where the entire playback queue is purged. This is different
     * from {@link #QUEUE_FLUSH} in that all entries are purged, not just entries
     * from a given caller.
     *
     * @unknown 
     */
    static final int QUEUE_DESTROY = 2;

    /**
     * Denotes the language is available exactly as specified by the locale.
     */
    public static final int LANG_COUNTRY_VAR_AVAILABLE = 2;

    /**
     * Denotes the language is available for the language and country specified
     * by the locale, but not the variant.
     */
    public static final int LANG_COUNTRY_AVAILABLE = 1;

    /**
     * Denotes the language is available for the language by the locale,
     * but not the country and variant.
     */
    public static final int LANG_AVAILABLE = 0;

    /**
     * Denotes the language data is missing.
     */
    public static final int LANG_MISSING_DATA = -1;

    /**
     * Denotes the language is not supported.
     */
    public static final int LANG_NOT_SUPPORTED = -2;

    /**
     * Broadcast Action: The TextToSpeech synthesizer has completed processing
     * of all the text in the speech queue.
     *
     * Note that this notifies callers when the <b>engine</b> has finished has
     * processing text data. Audio playback might not have completed (or even started)
     * at this point. If you wish to be notified when this happens, see
     * {@link OnUtteranceCompletedListener}.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String ACTION_TTS_QUEUE_PROCESSING_COMPLETED = "android.speech.tts.TTS_QUEUE_PROCESSING_COMPLETED";

    /**
     * Interface definition of a callback to be invoked indicating the completion of the
     * TextToSpeech engine initialization.
     */
    public interface OnInitListener {
        /**
         * Called to signal the completion of the TextToSpeech engine initialization.
         *
         * @param status
         * 		{@link TextToSpeech#SUCCESS} or {@link TextToSpeech#ERROR}.
         */
        public void onInit(int status);
    }

    /**
     * Listener that will be called when the TTS service has
     * completed synthesizing an utterance. This is only called if the utterance
     * has an utterance ID (see {@link TextToSpeech.Engine#KEY_PARAM_UTTERANCE_ID}).
     *
     * @deprecated Use {@link UtteranceProgressListener} instead.
     */
    @java.lang.Deprecated
    public interface OnUtteranceCompletedListener {
        /**
         * Called when an utterance has been synthesized.
         *
         * @param utteranceId
         * 		the identifier of the utterance.
         */
        public void onUtteranceCompleted(java.lang.String utteranceId);
    }

    /**
     * Constants and parameter names for controlling text-to-speech. These include:
     *
     * <ul>
     *     <li>
     *         Intents to ask engine to install data or check its data and
     *         extras for a TTS engine's check data activity.
     *     </li>
     *     <li>
     *         Keys for the parameters passed with speak commands, e.g.
     *         {@link Engine#KEY_PARAM_UTTERANCE_ID}, {@link Engine#KEY_PARAM_STREAM}.
     *     </li>
     *     <li>
     *         A list of feature strings that engines might support, e.g
     *         {@link Engine#KEY_FEATURE_NETWORK_SYNTHESIS}. These values may be passed in to
     *         {@link TextToSpeech#speak} and {@link TextToSpeech#synthesizeToFile} to modify
     *         engine behaviour. The engine can be queried for the set of features it supports
     *         through {@link TextToSpeech#getFeatures(java.util.Locale)}.
     *     </li>
     * </ul>
     */
    public class Engine {
        /**
         * Default speech rate.
         *
         * @unknown 
         */
        public static final int DEFAULT_RATE = 100;

        /**
         * Default pitch.
         *
         * @unknown 
         */
        public static final int DEFAULT_PITCH = 100;

        /**
         * Default volume.
         *
         * @unknown 
         */
        public static final float DEFAULT_VOLUME = 1.0F;

        /**
         * Default pan (centered).
         *
         * @unknown 
         */
        public static final float DEFAULT_PAN = 0.0F;

        /**
         * Default value for {@link Settings.Secure#TTS_USE_DEFAULTS}.
         *
         * @unknown 
         */
        public static final int USE_DEFAULTS = 0;// false


        /**
         * Package name of the default TTS engine.
         *
         * @unknown 
         * @deprecated No longer in use, the default engine is determined by
        the sort order defined in {@link TtsEngines}. Note that
        this doesn't "break" anything because there is no guarantee that
        the engine specified below is installed on a given build, let
        alone be the default.
         */
        @java.lang.Deprecated
        public static final java.lang.String DEFAULT_ENGINE = "com.svox.pico";

        /**
         * Default audio stream used when playing synthesized speech.
         */
        public static final int DEFAULT_STREAM = android.media.AudioManager.STREAM_MUSIC;

        /**
         * Indicates success when checking the installation status of the resources used by the
         * TextToSpeech engine with the {@link #ACTION_CHECK_TTS_DATA} intent.
         */
        public static final int CHECK_VOICE_DATA_PASS = 1;

        /**
         * Indicates failure when checking the installation status of the resources used by the
         * TextToSpeech engine with the {@link #ACTION_CHECK_TTS_DATA} intent.
         */
        public static final int CHECK_VOICE_DATA_FAIL = 0;

        /**
         * Indicates erroneous data when checking the installation status of the resources used by
         * the TextToSpeech engine with the {@link #ACTION_CHECK_TTS_DATA} intent.
         *
         * @deprecated Use CHECK_VOICE_DATA_FAIL instead.
         */
        @java.lang.Deprecated
        public static final int CHECK_VOICE_DATA_BAD_DATA = -1;

        /**
         * Indicates missing resources when checking the installation status of the resources used
         * by the TextToSpeech engine with the {@link #ACTION_CHECK_TTS_DATA} intent.
         *
         * @deprecated Use CHECK_VOICE_DATA_FAIL instead.
         */
        @java.lang.Deprecated
        public static final int CHECK_VOICE_DATA_MISSING_DATA = -2;

        /**
         * Indicates missing storage volume when checking the installation status of the resources
         * used by the TextToSpeech engine with the {@link #ACTION_CHECK_TTS_DATA} intent.
         *
         * @deprecated Use CHECK_VOICE_DATA_FAIL instead.
         */
        @java.lang.Deprecated
        public static final int CHECK_VOICE_DATA_MISSING_VOLUME = -3;

        /**
         * Intent for starting a TTS service. Services that handle this intent must
         * extend {@link TextToSpeechService}. Normal applications should not use this intent
         * directly, instead they should talk to the TTS service using the the methods in this
         * class.
         */
        @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.SERVICE_ACTION)
        public static final java.lang.String INTENT_ACTION_TTS_SERVICE = "android.intent.action.TTS_SERVICE";

        /**
         * Name under which a text to speech engine publishes information about itself.
         * This meta-data should reference an XML resource containing a
         * <code>&lt;{@link android.R.styleable#TextToSpeechEngine tts-engine}&gt;</code>
         * tag.
         */
        public static final java.lang.String SERVICE_META_DATA = "android.speech.tts";

        // intents to ask engine to install data or check its data
        /**
         * Activity Action: Triggers the platform TextToSpeech engine to
         * start the activity that installs the resource files on the device
         * that are required for TTS to be operational. Since the installation
         * of the data can be interrupted or declined by the user, the application
         * shouldn't expect successful installation upon return from that intent,
         * and if need be, should check installation status with
         * {@link #ACTION_CHECK_TTS_DATA}.
         */
        @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
        public static final java.lang.String ACTION_INSTALL_TTS_DATA = "android.speech.tts.engine.INSTALL_TTS_DATA";

        /**
         * Broadcast Action: broadcast to signal the change in the list of available
         * languages or/and their features.
         */
        @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
        public static final java.lang.String ACTION_TTS_DATA_INSTALLED = "android.speech.tts.engine.TTS_DATA_INSTALLED";

        /**
         * Activity Action: Starts the activity from the platform TextToSpeech
         * engine to verify the proper installation and availability of the
         * resource files on the system. Upon completion, the activity will
         * return one of the following codes:
         * {@link #CHECK_VOICE_DATA_PASS},
         * {@link #CHECK_VOICE_DATA_FAIL},
         * <p> Moreover, the data received in the activity result will contain the following
         * fields:
         * <ul>
         *   <li>{@link #EXTRA_AVAILABLE_VOICES} which contains an ArrayList<String> of all the
         *   available voices. The format of each voice is: lang-COUNTRY-variant where COUNTRY and
         *   variant are optional (ie, "eng" or "eng-USA" or "eng-USA-FEMALE").</li>
         *   <li>{@link #EXTRA_UNAVAILABLE_VOICES} which contains an ArrayList<String> of all the
         *   unavailable voices (ones that user can install). The format of each voice is:
         *   lang-COUNTRY-variant where COUNTRY and variant are optional (ie, "eng" or
         *   "eng-USA" or "eng-USA-FEMALE").</li>
         * </ul>
         */
        @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
        public static final java.lang.String ACTION_CHECK_TTS_DATA = "android.speech.tts.engine.CHECK_TTS_DATA";

        /**
         * Activity intent for getting some sample text to use for demonstrating TTS. Specific
         * locale have to be requested by passing following extra parameters:
         * <ul>
         *   <li>language</li>
         *   <li>country</li>
         *   <li>variant</li>
         * </ul>
         *
         * Upon completion, the activity result may contain the following fields:
         * <ul>
         *   <li>{@link #EXTRA_SAMPLE_TEXT} which contains an String with sample text.</li>
         * </ul>
         */
        @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
        public static final java.lang.String ACTION_GET_SAMPLE_TEXT = "android.speech.tts.engine.GET_SAMPLE_TEXT";

        /**
         * Extra information received with the {@link #ACTION_GET_SAMPLE_TEXT} intent result where
         * the TextToSpeech engine returns an String with sample text for requested voice
         */
        public static final java.lang.String EXTRA_SAMPLE_TEXT = "sampleText";

        // extras for a TTS engine's check data activity
        /**
         * Extra information received with the {@link #ACTION_CHECK_TTS_DATA} intent result where
         * the TextToSpeech engine returns an ArrayList<String> of all the available voices.
         * The format of each voice is: lang-COUNTRY-variant where COUNTRY and variant are
         * optional (ie, "eng" or "eng-USA" or "eng-USA-FEMALE").
         */
        public static final java.lang.String EXTRA_AVAILABLE_VOICES = "availableVoices";

        /**
         * Extra information received with the {@link #ACTION_CHECK_TTS_DATA} intent result where
         * the TextToSpeech engine returns an ArrayList<String> of all the unavailable voices.
         * The format of each voice is: lang-COUNTRY-variant where COUNTRY and variant are
         * optional (ie, "eng" or "eng-USA" or "eng-USA-FEMALE").
         */
        public static final java.lang.String EXTRA_UNAVAILABLE_VOICES = "unavailableVoices";

        /**
         * Extra information received with the {@link #ACTION_CHECK_TTS_DATA} intent result where
         * the TextToSpeech engine specifies the path to its resources.
         *
         * It may be used by language packages to find out where to put their data.
         *
         * @deprecated TTS engine implementation detail, this information has no use for
        text-to-speech API client.
         */
        @java.lang.Deprecated
        public static final java.lang.String EXTRA_VOICE_DATA_ROOT_DIRECTORY = "dataRoot";

        /**
         * Extra information received with the {@link #ACTION_CHECK_TTS_DATA} intent result where
         * the TextToSpeech engine specifies the file names of its resources under the
         * resource path.
         *
         * @deprecated TTS engine implementation detail, this information has no use for
        text-to-speech API client.
         */
        @java.lang.Deprecated
        public static final java.lang.String EXTRA_VOICE_DATA_FILES = "dataFiles";

        /**
         * Extra information received with the {@link #ACTION_CHECK_TTS_DATA} intent result where
         * the TextToSpeech engine specifies the locale associated with each resource file.
         *
         * @deprecated TTS engine implementation detail, this information has no use for
        text-to-speech API client.
         */
        @java.lang.Deprecated
        public static final java.lang.String EXTRA_VOICE_DATA_FILES_INFO = "dataFilesInfo";

        /**
         * Extra information sent with the {@link #ACTION_CHECK_TTS_DATA} intent where the
         * caller indicates to the TextToSpeech engine which specific sets of voice data to
         * check for by sending an ArrayList<String> of the voices that are of interest.
         * The format of each voice is: lang-COUNTRY-variant where COUNTRY and variant are
         * optional (ie, "eng" or "eng-USA" or "eng-USA-FEMALE").
         *
         * @deprecated Redundant functionality, checking for existence of specific sets of voice
        data can be done on client side.
         */
        @java.lang.Deprecated
        public static final java.lang.String EXTRA_CHECK_VOICE_DATA_FOR = "checkVoiceDataFor";

        // extras for a TTS engine's data installation
        /**
         * Extra information received with the {@link #ACTION_TTS_DATA_INSTALLED} intent result.
         * It indicates whether the data files for the synthesis engine were successfully
         * installed. The installation was initiated with the  {@link #ACTION_INSTALL_TTS_DATA}
         * intent. The possible values for this extra are
         * {@link TextToSpeech#SUCCESS} and {@link TextToSpeech#ERROR}.
         *
         * @deprecated No longer in use. If client ise interested in information about what
        changed, is should send ACTION_CHECK_TTS_DATA intent to discover available voices.
         */
        @java.lang.Deprecated
        public static final java.lang.String EXTRA_TTS_DATA_INSTALLED = "dataInstalled";

        // keys for the parameters passed with speak commands. Hidden keys are used internally
        // to maintain engine state for each TextToSpeech instance.
        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String KEY_PARAM_RATE = "rate";

        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String KEY_PARAM_VOICE_NAME = "voiceName";

        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String KEY_PARAM_LANGUAGE = "language";

        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String KEY_PARAM_COUNTRY = "country";

        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String KEY_PARAM_VARIANT = "variant";

        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String KEY_PARAM_ENGINE = "engine";

        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String KEY_PARAM_PITCH = "pitch";

        /**
         * Parameter key to specify the audio stream type to be used when speaking text
         * or playing back a file. The value should be one of the STREAM_ constants
         * defined in {@link AudioManager}.
         *
         * @see TextToSpeech#speak(String, int, HashMap)
         * @see TextToSpeech#playEarcon(String, int, HashMap)
         */
        public static final java.lang.String KEY_PARAM_STREAM = "streamType";

        /**
         * Parameter key to specify the audio attributes to be used when
         * speaking text or playing back a file. The value should be set
         * using {@link TextToSpeech#setAudioAttributes(AudioAttributes)}.
         *
         * @see TextToSpeech#speak(String, int, HashMap)
         * @see TextToSpeech#playEarcon(String, int, HashMap)
         * @unknown 
         */
        public static final java.lang.String KEY_PARAM_AUDIO_ATTRIBUTES = "audioAttributes";

        /**
         * Parameter key to identify an utterance in the
         * {@link TextToSpeech.OnUtteranceCompletedListener} after text has been
         * spoken, a file has been played back or a silence duration has elapsed.
         *
         * @see TextToSpeech#speak(String, int, HashMap)
         * @see TextToSpeech#playEarcon(String, int, HashMap)
         * @see TextToSpeech#synthesizeToFile(String, HashMap, String)
         */
        public static final java.lang.String KEY_PARAM_UTTERANCE_ID = "utteranceId";

        /**
         * Parameter key to specify the speech volume relative to the current stream type
         * volume used when speaking text. Volume is specified as a float ranging from 0 to 1
         * where 0 is silence, and 1 is the maximum volume (the default behavior).
         *
         * @see TextToSpeech#speak(String, int, HashMap)
         * @see TextToSpeech#playEarcon(String, int, HashMap)
         */
        public static final java.lang.String KEY_PARAM_VOLUME = "volume";

        /**
         * Parameter key to specify how the speech is panned from left to right when speaking text.
         * Pan is specified as a float ranging from -1 to +1 where -1 maps to a hard-left pan,
         * 0 to center (the default behavior), and +1 to hard-right.
         *
         * @see TextToSpeech#speak(String, int, HashMap)
         * @see TextToSpeech#playEarcon(String, int, HashMap)
         */
        public static final java.lang.String KEY_PARAM_PAN = "pan";

        /**
         * Feature key for network synthesis. See {@link TextToSpeech#getFeatures(Locale)}
         * for a description of how feature keys work. If set (and supported by the engine
         * as per {@link TextToSpeech#getFeatures(Locale)}, the engine must
         * use network based synthesis.
         *
         * @see TextToSpeech#speak(String, int, java.util.HashMap)
         * @see TextToSpeech#synthesizeToFile(String, java.util.HashMap, String)
         * @see TextToSpeech#getFeatures(java.util.Locale)
         * @deprecated Starting from API level 21, to select network synthesis, call
        {@link TextToSpeech#getVoices()}, find a suitable network voice
        ({@link Voice#isNetworkConnectionRequired()}) and pass it
        to {@link TextToSpeech#setVoice(Voice)}.
         */
        @java.lang.Deprecated
        public static final java.lang.String KEY_FEATURE_NETWORK_SYNTHESIS = "networkTts";

        /**
         * Feature key for embedded synthesis. See {@link TextToSpeech#getFeatures(Locale)}
         * for a description of how feature keys work. If set and supported by the engine
         * as per {@link TextToSpeech#getFeatures(Locale)}, the engine must synthesize
         * text on-device (without making network requests).
         *
         * @see TextToSpeech#speak(String, int, java.util.HashMap)
         * @see TextToSpeech#synthesizeToFile(String, java.util.HashMap, String)
         * @see TextToSpeech#getFeatures(java.util.Locale)
         * @deprecated Starting from API level 21, to select embedded synthesis, call
        ({@link TextToSpeech#getVoices()}, find a suitable embedded voice
        ({@link Voice#isNetworkConnectionRequired()}) and pass it
        to {@link TextToSpeech#setVoice(Voice)}).
         */
        @java.lang.Deprecated
        public static final java.lang.String KEY_FEATURE_EMBEDDED_SYNTHESIS = "embeddedTts";

        /**
         * Parameter key to specify an audio session identifier (obtained from
         * {@link AudioManager#generateAudioSessionId()}) that will be used by the request audio
         * output. It can be used to associate one of the {@link android.media.audiofx.AudioEffect}
         * objects with the synthesis (or earcon) output.
         *
         * @see TextToSpeech#speak(String, int, HashMap)
         * @see TextToSpeech#playEarcon(String, int, HashMap)
         */
        public static final java.lang.String KEY_PARAM_SESSION_ID = "sessionId";

        /**
         * Feature key that indicates that the voice may need to download additional data to be fully
         * functional. The download will be triggered by calling
         * {@link TextToSpeech#setVoice(Voice)} or {@link TextToSpeech#setLanguage(Locale)}.
         * Until download is complete, each synthesis request will either report
         * {@link TextToSpeech#ERROR_NOT_INSTALLED_YET} error, or use a different voice to synthesize
         * the request. This feature should NOT be used as a key of a request parameter.
         *
         * @see TextToSpeech#getFeatures(java.util.Locale)
         * @see Voice#getFeatures()
         */
        public static final java.lang.String KEY_FEATURE_NOT_INSTALLED = "notInstalled";

        /**
         * Feature key that indicate that a network timeout can be set for the request. If set and
         * supported as per {@link TextToSpeech#getFeatures(Locale)} or {@link Voice#getFeatures()},
         * it can be used as request parameter to set the maximum allowed time for a single
         * request attempt, in milliseconds, before synthesis fails. When used as a key of
         * a request parameter, its value should be a string with an integer value.
         *
         * @see TextToSpeech#getFeatures(java.util.Locale)
         * @see Voice#getFeatures()
         */
        public static final java.lang.String KEY_FEATURE_NETWORK_TIMEOUT_MS = "networkTimeoutMs";

        /**
         * Feature key that indicates that network request retries count can be set for the request.
         * If set and supported as per {@link TextToSpeech#getFeatures(Locale)} or
         * {@link Voice#getFeatures()}, it can be used as a request parameter to set the
         * number of network request retries that are attempted in case of failure. When used as
         * a key of a request parameter, its value should be a string with an integer value.
         *
         * @see TextToSpeech#getFeatures(java.util.Locale)
         * @see Voice#getFeatures()
         */
        public static final java.lang.String KEY_FEATURE_NETWORK_RETRIES_COUNT = "networkRetriesCount";
    }

    private final android.content.Context mContext;

    private android.speech.tts.TextToSpeech.Connection mConnectingServiceConnection;

    private android.speech.tts.TextToSpeech.Connection mServiceConnection;

    private android.speech.tts.TextToSpeech.OnInitListener mInitListener;

    // Written from an unspecified application thread, read from
    // a binder thread.
    @android.annotation.Nullable
    private volatile android.speech.tts.UtteranceProgressListener mUtteranceProgressListener;

    private final java.lang.Object mStartLock = new java.lang.Object();

    private java.lang.String mRequestedEngine;

    // Whether to initialize this TTS object with the default engine,
    // if the requested engine is not available. Valid only if mRequestedEngine
    // is not null. Used only for testing, though potentially useful API wise
    // too.
    private final boolean mUseFallback;

    private final java.util.Map<java.lang.String, android.net.Uri> mEarcons;

    private final java.util.Map<java.lang.CharSequence, android.net.Uri> mUtterances;

    private final android.os.Bundle mParams = new android.os.Bundle();

    private final android.speech.tts.TtsEngines mEnginesHelper;

    private volatile java.lang.String mCurrentEngine = null;

    /**
     * The constructor for the TextToSpeech class, using the default TTS engine.
     * This will also initialize the associated TextToSpeech engine if it isn't already running.
     *
     * @param context
     * 		The context this instance is running in.
     * @param listener
     * 		The {@link TextToSpeech.OnInitListener} that will be called when the
     * 		TextToSpeech engine has initialized. In a case of a failure the listener
     * 		may be called immediately, before TextToSpeech instance is fully constructed.
     */
    public TextToSpeech(android.content.Context context, android.speech.tts.TextToSpeech.OnInitListener listener) {
        this(context, listener, null);
    }

    /**
     * The constructor for the TextToSpeech class, using the given TTS engine.
     * This will also initialize the associated TextToSpeech engine if it isn't already running.
     *
     * @param context
     * 		The context this instance is running in.
     * @param listener
     * 		The {@link TextToSpeech.OnInitListener} that will be called when the
     * 		TextToSpeech engine has initialized. In a case of a failure the listener
     * 		may be called immediately, before TextToSpeech instance is fully constructed.
     * @param engine
     * 		Package name of the TTS engine to use.
     */
    public TextToSpeech(android.content.Context context, android.speech.tts.TextToSpeech.OnInitListener listener, java.lang.String engine) {
        this(context, listener, engine, null, true);
    }

    /**
     * Used by the framework to instantiate TextToSpeech objects with a supplied
     * package name, instead of using {@link android.content.Context#getPackageName()}
     *
     * @unknown 
     */
    public TextToSpeech(android.content.Context context, android.speech.tts.TextToSpeech.OnInitListener listener, java.lang.String engine, java.lang.String packageName, boolean useFallback) {
        mContext = context;
        mInitListener = listener;
        mRequestedEngine = engine;
        mUseFallback = useFallback;
        mEarcons = new java.util.HashMap<java.lang.String, android.net.Uri>();
        mUtterances = new java.util.HashMap<java.lang.CharSequence, android.net.Uri>();
        mUtteranceProgressListener = null;
        mEnginesHelper = new android.speech.tts.TtsEngines(mContext);
        initTts();
    }

    private <R> R runActionNoReconnect(android.speech.tts.TextToSpeech.Action<R> action, R errorResult, java.lang.String method, boolean onlyEstablishedConnection) {
        return runAction(action, errorResult, method, false, onlyEstablishedConnection);
    }

    private <R> R runAction(android.speech.tts.TextToSpeech.Action<R> action, R errorResult, java.lang.String method) {
        return runAction(action, errorResult, method, true, true);
    }

    private <R> R runAction(android.speech.tts.TextToSpeech.Action<R> action, R errorResult, java.lang.String method, boolean reconnect, boolean onlyEstablishedConnection) {
        synchronized(mStartLock) {
            if (mServiceConnection == null) {
                android.util.Log.w(android.speech.tts.TextToSpeech.TAG, method + " failed: not bound to TTS engine");
                return errorResult;
            }
            return mServiceConnection.runAction(action, errorResult, method, reconnect, onlyEstablishedConnection);
        }
    }

    private int initTts() {
        // Step 1: Try connecting to the engine that was requested.
        if (mRequestedEngine != null) {
            if (mEnginesHelper.isEngineInstalled(mRequestedEngine)) {
                if (connectToEngine(mRequestedEngine)) {
                    mCurrentEngine = mRequestedEngine;
                    return android.speech.tts.TextToSpeech.SUCCESS;
                } else
                    if (!mUseFallback) {
                        mCurrentEngine = null;
                        dispatchOnInit(android.speech.tts.TextToSpeech.ERROR);
                        return android.speech.tts.TextToSpeech.ERROR;
                    }

            } else
                if (!mUseFallback) {
                    android.util.Log.i(android.speech.tts.TextToSpeech.TAG, "Requested engine not installed: " + mRequestedEngine);
                    mCurrentEngine = null;
                    dispatchOnInit(android.speech.tts.TextToSpeech.ERROR);
                    return android.speech.tts.TextToSpeech.ERROR;
                }

        }
        // Step 2: Try connecting to the user's default engine.
        final java.lang.String defaultEngine = getDefaultEngine();
        if ((defaultEngine != null) && (!defaultEngine.equals(mRequestedEngine))) {
            if (connectToEngine(defaultEngine)) {
                mCurrentEngine = defaultEngine;
                return android.speech.tts.TextToSpeech.SUCCESS;
            }
        }
        // Step 3: Try connecting to the highest ranked engine in the
        // system.
        final java.lang.String highestRanked = mEnginesHelper.getHighestRankedEngineName();
        if (((highestRanked != null) && (!highestRanked.equals(mRequestedEngine))) && (!highestRanked.equals(defaultEngine))) {
            if (connectToEngine(highestRanked)) {
                mCurrentEngine = highestRanked;
                return android.speech.tts.TextToSpeech.SUCCESS;
            }
        }
        // NOTE: The API currently does not allow the caller to query whether
        // they are actually connected to any engine. This might fail for various
        // reasons like if the user disables all her TTS engines.
        mCurrentEngine = null;
        dispatchOnInit(android.speech.tts.TextToSpeech.ERROR);
        return android.speech.tts.TextToSpeech.ERROR;
    }

    private boolean connectToEngine(java.lang.String engine) {
        android.speech.tts.TextToSpeech.Connection connection = new android.speech.tts.TextToSpeech.Connection();
        android.content.Intent intent = new android.content.Intent(android.speech.tts.TextToSpeech.Engine.INTENT_ACTION_TTS_SERVICE);
        intent.setPackage(engine);
        boolean bound = mContext.bindService(intent, connection, android.content.Context.BIND_AUTO_CREATE);
        if (!bound) {
            android.util.Log.e(android.speech.tts.TextToSpeech.TAG, "Failed to bind to " + engine);
            return false;
        } else {
            android.util.Log.i(android.speech.tts.TextToSpeech.TAG, "Sucessfully bound to " + engine);
            mConnectingServiceConnection = connection;
            return true;
        }
    }

    private void dispatchOnInit(int result) {
        synchronized(mStartLock) {
            if (mInitListener != null) {
                mInitListener.onInit(result);
                mInitListener = null;
            }
        }
    }

    private android.os.IBinder getCallerIdentity() {
        return mServiceConnection.getCallerIdentity();
    }

    /**
     * Releases the resources used by the TextToSpeech engine.
     * It is good practice for instance to call this method in the onDestroy() method of an Activity
     * so the TextToSpeech engine can be cleanly stopped.
     */
    public void shutdown() {
        // Special case, we are asked to shutdown connection that did finalize its connection.
        synchronized(mStartLock) {
            if (mConnectingServiceConnection != null) {
                mContext.unbindService(mConnectingServiceConnection);
                mConnectingServiceConnection = null;
                return;
            }
        }
        // Post connection case
        runActionNoReconnect(new android.speech.tts.TextToSpeech.Action<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException {
                service.setCallback(getCallerIdentity(), null);
                service.stop(getCallerIdentity());
                mServiceConnection.disconnect();
                // Context#unbindService does not result in a call to
                // ServiceConnection#onServiceDisconnected. As a result, the
                // service ends up being destroyed (if there are no other open
                // connections to it) but the process lives on and the
                // ServiceConnection continues to refer to the destroyed service.
                // 
                // This leads to tons of log spam about SynthThread being dead.
                mServiceConnection = null;
                mCurrentEngine = null;
                return null;
            }
        }, null, "shutdown", false);
    }

    /**
     * Adds a mapping between a string of text and a sound resource in a
     * package. After a call to this method, subsequent calls to
     * {@link #speak(String, int, HashMap)} will play the specified sound resource
     * if it is available, or synthesize the text it is missing.
     *
     * @param text
     * 		The string of text. Example: <code>"south_south_east"</code>
     * @param packagename
     * 		Pass the packagename of the application that contains the
     * 		resource. If the resource is in your own application (this is
     * 		the most common case), then put the packagename of your
     * 		application here.<br/>
     * 		Example: <b>"com.google.marvin.compass"</b><br/>
     * 		The packagename can be found in the AndroidManifest.xml of
     * 		your application.
     * 		<p>
     * 		<code>&lt;manifest xmlns:android=&quot;...&quot;
     * 		package=&quot;<b>com.google.marvin.compass</b>&quot;&gt;</code>
     * 		</p>
     * @param resourceId
     * 		Example: <code>R.raw.south_south_east</code>
     * @return Code indicating success or failure. See {@link #ERROR} and {@link #SUCCESS}.
     */
    public int addSpeech(java.lang.String text, java.lang.String packagename, @android.annotation.RawRes
    int resourceId) {
        synchronized(mStartLock) {
            mUtterances.put(text, makeResourceUri(packagename, resourceId));
            return android.speech.tts.TextToSpeech.SUCCESS;
        }
    }

    /**
     * Adds a mapping between a CharSequence (may be spanned with TtsSpans) of text
     * and a sound resource in a package. After a call to this method, subsequent calls to
     * {@link #speak(String, int, HashMap)} will play the specified sound resource
     * if it is available, or synthesize the text it is missing.
     *
     * @param text
     * 		The string of text. Example: <code>"south_south_east"</code>
     * @param packagename
     * 		Pass the packagename of the application that contains the
     * 		resource. If the resource is in your own application (this is
     * 		the most common case), then put the packagename of your
     * 		application here.<br/>
     * 		Example: <b>"com.google.marvin.compass"</b><br/>
     * 		The packagename can be found in the AndroidManifest.xml of
     * 		your application.
     * 		<p>
     * 		<code>&lt;manifest xmlns:android=&quot;...&quot;
     * 		package=&quot;<b>com.google.marvin.compass</b>&quot;&gt;</code>
     * 		</p>
     * @param resourceId
     * 		Example: <code>R.raw.south_south_east</code>
     * @return Code indicating success or failure. See {@link #ERROR} and {@link #SUCCESS}.
     */
    public int addSpeech(java.lang.CharSequence text, java.lang.String packagename, @android.annotation.RawRes
    int resourceId) {
        synchronized(mStartLock) {
            mUtterances.put(text, makeResourceUri(packagename, resourceId));
            return android.speech.tts.TextToSpeech.SUCCESS;
        }
    }

    /**
     * Adds a mapping between a string of text and a sound file. Using this, it
     * is possible to add custom pronounciations for a string of text.
     * After a call to this method, subsequent calls to {@link #speak(String, int, HashMap)}
     * will play the specified sound resource if it is available, or synthesize the text it is
     * missing.
     *
     * @param text
     * 		The string of text. Example: <code>"south_south_east"</code>
     * @param filename
     * 		The full path to the sound file (for example:
     * 		"/sdcard/mysounds/hello.wav")
     * @return Code indicating success or failure. See {@link #ERROR} and {@link #SUCCESS}.
     */
    public int addSpeech(java.lang.String text, java.lang.String filename) {
        synchronized(mStartLock) {
            mUtterances.put(text, android.net.Uri.parse(filename));
            return android.speech.tts.TextToSpeech.SUCCESS;
        }
    }

    /**
     * Adds a mapping between a CharSequence (may be spanned with TtsSpans and a sound file.
     * Using this, it is possible to add custom pronounciations for a string of text.
     * After a call to this method, subsequent calls to {@link #speak(String, int, HashMap)}
     * will play the specified sound resource if it is available, or synthesize the text it is
     * missing.
     *
     * @param text
     * 		The string of text. Example: <code>"south_south_east"</code>
     * @param file
     * 		File object pointing to the sound file.
     * @return Code indicating success or failure. See {@link #ERROR} and {@link #SUCCESS}.
     */
    public int addSpeech(java.lang.CharSequence text, java.io.File file) {
        synchronized(mStartLock) {
            mUtterances.put(text, android.net.Uri.fromFile(file));
            return android.speech.tts.TextToSpeech.SUCCESS;
        }
    }

    /**
     * Adds a mapping between a string of text and a sound resource in a
     * package. Use this to add custom earcons.
     *
     * @see #playEarcon(String, int, HashMap)
     * @param earcon
     * 		The name of the earcon.
     * 		Example: <code>"[tick]"</code><br/>
     * @param packagename
     * 		the package name of the application that contains the
     * 		resource. This can for instance be the package name of your own application.
     * 		Example: <b>"com.google.marvin.compass"</b><br/>
     * 		The package name can be found in the AndroidManifest.xml of
     * 		the application containing the resource.
     * 		<p>
     * 		<code>&lt;manifest xmlns:android=&quot;...&quot;
     * 		package=&quot;<b>com.google.marvin.compass</b>&quot;&gt;</code>
     * 		</p>
     * @param resourceId
     * 		Example: <code>R.raw.tick_snd</code>
     * @return Code indicating success or failure. See {@link #ERROR} and {@link #SUCCESS}.
     */
    public int addEarcon(java.lang.String earcon, java.lang.String packagename, @android.annotation.RawRes
    int resourceId) {
        synchronized(mStartLock) {
            mEarcons.put(earcon, makeResourceUri(packagename, resourceId));
            return android.speech.tts.TextToSpeech.SUCCESS;
        }
    }

    /**
     * Adds a mapping between a string of text and a sound file.
     * Use this to add custom earcons.
     *
     * @see #playEarcon(String, int, HashMap)
     * @param earcon
     * 		The name of the earcon.
     * 		Example: <code>"[tick]"</code>
     * @param filename
     * 		The full path to the sound file (for example:
     * 		"/sdcard/mysounds/tick.wav")
     * @return Code indicating success or failure. See {@link #ERROR} and {@link #SUCCESS}.
     * @deprecated As of API level 21, replaced by
    {@link #addEarcon(String, File)}.
     */
    @java.lang.Deprecated
    public int addEarcon(java.lang.String earcon, java.lang.String filename) {
        synchronized(mStartLock) {
            mEarcons.put(earcon, android.net.Uri.parse(filename));
            return android.speech.tts.TextToSpeech.SUCCESS;
        }
    }

    /**
     * Adds a mapping between a string of text and a sound file.
     * Use this to add custom earcons.
     *
     * @see #playEarcon(String, int, HashMap)
     * @param earcon
     * 		The name of the earcon.
     * 		Example: <code>"[tick]"</code>
     * @param file
     * 		File object pointing to the sound file.
     * @return Code indicating success or failure. See {@link #ERROR} and {@link #SUCCESS}.
     */
    public int addEarcon(java.lang.String earcon, java.io.File file) {
        synchronized(mStartLock) {
            mEarcons.put(earcon, android.net.Uri.fromFile(file));
            return android.speech.tts.TextToSpeech.SUCCESS;
        }
    }

    private android.net.Uri makeResourceUri(java.lang.String packageName, int resourceId) {
        return new android.net.Uri.Builder().scheme(android.content.ContentResolver.SCHEME_ANDROID_RESOURCE).encodedAuthority(packageName).appendEncodedPath(java.lang.String.valueOf(resourceId)).build();
    }

    /**
     * Speaks the text using the specified queuing strategy and speech parameters, the text may
     * be spanned with TtsSpans.
     * This method is asynchronous, i.e. the method just adds the request to the queue of TTS
     * requests and then returns. The synthesis might not have finished (or even started!) at the
     * time when this method returns. In order to reliably detect errors during synthesis,
     * we recommend setting an utterance progress listener (see
     * {@link #setOnUtteranceProgressListener}) and using the
     * {@link Engine#KEY_PARAM_UTTERANCE_ID} parameter.
     *
     * @param text
     * 		The string of text to be spoken. No longer than
     * 		{@link #getMaxSpeechInputLength()} characters.
     * @param queueMode
     * 		The queuing strategy to use, {@link #QUEUE_ADD} or {@link #QUEUE_FLUSH}.
     * @param params
     * 		Parameters for the request. Can be null.
     * 		Supported parameter names:
     * 		{@link Engine#KEY_PARAM_STREAM},
     * 		{@link Engine#KEY_PARAM_VOLUME},
     * 		{@link Engine#KEY_PARAM_PAN}.
     * 		Engine specific parameters may be passed in but the parameter keys
     * 		must be prefixed by the name of the engine they are intended for. For example
     * 		the keys "com.svox.pico_foo" and "com.svox.pico:bar" will be passed to the
     * 		engine named "com.svox.pico" if it is being used.
     * @param utteranceId
     * 		An unique identifier for this request.
     * @return {@link #ERROR} or {@link #SUCCESS} of <b>queuing</b> the speak operation.
     */
    public int speak(final java.lang.CharSequence text, final int queueMode, final android.os.Bundle params, final java.lang.String utteranceId) {
        return runAction(new android.speech.tts.TextToSpeech.Action<java.lang.Integer>() {
            @java.lang.Override
            public java.lang.Integer run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException {
                android.net.Uri utteranceUri = mUtterances.get(text);
                if (utteranceUri != null) {
                    return service.playAudio(getCallerIdentity(), utteranceUri, queueMode, getParams(params), utteranceId);
                } else {
                    return service.speak(getCallerIdentity(), text, queueMode, getParams(params), utteranceId);
                }
            }
        }, android.speech.tts.TextToSpeech.ERROR, "speak");
    }

    /**
     * Speaks the string using the specified queuing strategy and speech parameters.
     * This method is asynchronous, i.e. the method just adds the request to the queue of TTS
     * requests and then returns. The synthesis might not have finished (or even started!) at the
     * time when this method returns. In order to reliably detect errors during synthesis,
     * we recommend setting an utterance progress listener (see
     * {@link #setOnUtteranceProgressListener}) and using the
     * {@link Engine#KEY_PARAM_UTTERANCE_ID} parameter.
     *
     * @param text
     * 		The string of text to be spoken. No longer than
     * 		{@link #getMaxSpeechInputLength()} characters.
     * @param queueMode
     * 		The queuing strategy to use, {@link #QUEUE_ADD} or {@link #QUEUE_FLUSH}.
     * @param params
     * 		Parameters for the request. Can be null.
     * 		Supported parameter names:
     * 		{@link Engine#KEY_PARAM_STREAM},
     * 		{@link Engine#KEY_PARAM_UTTERANCE_ID},
     * 		{@link Engine#KEY_PARAM_VOLUME},
     * 		{@link Engine#KEY_PARAM_PAN}.
     * 		Engine specific parameters may be passed in but the parameter keys
     * 		must be prefixed by the name of the engine they are intended for. For example
     * 		the keys "com.svox.pico_foo" and "com.svox.pico:bar" will be passed to the
     * 		engine named "com.svox.pico" if it is being used.
     * @return {@link #ERROR} or {@link #SUCCESS} of <b>queuing</b> the speak operation.
     * @deprecated As of API level 21, replaced by
    {@link #speak(CharSequence, int, Bundle, String)}.
     */
    @java.lang.Deprecated
    public int speak(final java.lang.String text, final int queueMode, final java.util.HashMap<java.lang.String, java.lang.String> params) {
        return speak(text, queueMode, convertParamsHashMaptoBundle(params), params == null ? null : params.get(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID));
    }

    /**
     * Plays the earcon using the specified queueing mode and parameters.
     * The earcon must already have been added with {@link #addEarcon(String, String)} or
     * {@link #addEarcon(String, String, int)}.
     * This method is asynchronous, i.e. the method just adds the request to the queue of TTS
     * requests and then returns. The synthesis might not have finished (or even started!) at the
     * time when this method returns. In order to reliably detect errors during synthesis,
     * we recommend setting an utterance progress listener (see
     * {@link #setOnUtteranceProgressListener}) and using the
     * {@link Engine#KEY_PARAM_UTTERANCE_ID} parameter.
     *
     * @param earcon
     * 		The earcon that should be played
     * @param queueMode
     * 		{@link #QUEUE_ADD} or {@link #QUEUE_FLUSH}.
     * @param params
     * 		Parameters for the request. Can be null.
     * 		Supported parameter names:
     * 		{@link Engine#KEY_PARAM_STREAM},
     * 		Engine specific parameters may be passed in but the parameter keys
     * 		must be prefixed by the name of the engine they are intended for. For example
     * 		the keys "com.svox.pico_foo" and "com.svox.pico:bar" will be passed to the
     * 		engine named "com.svox.pico" if it is being used.
     * @return {@link #ERROR} or {@link #SUCCESS} of <b>queuing</b> the playEarcon operation.
     */
    public int playEarcon(final java.lang.String earcon, final int queueMode, final android.os.Bundle params, final java.lang.String utteranceId) {
        return runAction(new android.speech.tts.TextToSpeech.Action<java.lang.Integer>() {
            @java.lang.Override
            public java.lang.Integer run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException {
                android.net.Uri earconUri = mEarcons.get(earcon);
                if (earconUri == null) {
                    return android.speech.tts.TextToSpeech.ERROR;
                }
                return service.playAudio(getCallerIdentity(), earconUri, queueMode, getParams(params), utteranceId);
            }
        }, android.speech.tts.TextToSpeech.ERROR, "playEarcon");
    }

    /**
     * Plays the earcon using the specified queueing mode and parameters.
     * The earcon must already have been added with {@link #addEarcon(String, String)} or
     * {@link #addEarcon(String, String, int)}.
     * This method is asynchronous, i.e. the method just adds the request to the queue of TTS
     * requests and then returns. The synthesis might not have finished (or even started!) at the
     * time when this method returns. In order to reliably detect errors during synthesis,
     * we recommend setting an utterance progress listener (see
     * {@link #setOnUtteranceProgressListener}) and using the
     * {@link Engine#KEY_PARAM_UTTERANCE_ID} parameter.
     *
     * @param earcon
     * 		The earcon that should be played
     * @param queueMode
     * 		{@link #QUEUE_ADD} or {@link #QUEUE_FLUSH}.
     * @param params
     * 		Parameters for the request. Can be null.
     * 		Supported parameter names:
     * 		{@link Engine#KEY_PARAM_STREAM},
     * 		{@link Engine#KEY_PARAM_UTTERANCE_ID}.
     * 		Engine specific parameters may be passed in but the parameter keys
     * 		must be prefixed by the name of the engine they are intended for. For example
     * 		the keys "com.svox.pico_foo" and "com.svox.pico:bar" will be passed to the
     * 		engine named "com.svox.pico" if it is being used.
     * @return {@link #ERROR} or {@link #SUCCESS} of <b>queuing</b> the playEarcon operation.
     * @deprecated As of API level 21, replaced by
    {@link #playEarcon(String, int, Bundle, String)}.
     */
    @java.lang.Deprecated
    public int playEarcon(final java.lang.String earcon, final int queueMode, final java.util.HashMap<java.lang.String, java.lang.String> params) {
        return playEarcon(earcon, queueMode, convertParamsHashMaptoBundle(params), params == null ? null : params.get(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID));
    }

    /**
     * Plays silence for the specified amount of time using the specified
     * queue mode.
     * This method is asynchronous, i.e. the method just adds the request to the queue of TTS
     * requests and then returns. The synthesis might not have finished (or even started!) at the
     * time when this method returns. In order to reliably detect errors during synthesis,
     * we recommend setting an utterance progress listener (see
     * {@link #setOnUtteranceProgressListener}) and using the
     * {@link Engine#KEY_PARAM_UTTERANCE_ID} parameter.
     *
     * @param durationInMs
     * 		The duration of the silence.
     * @param queueMode
     * 		{@link #QUEUE_ADD} or {@link #QUEUE_FLUSH}.
     * @param utteranceId
     * 		An unique identifier for this request.
     * @return {@link #ERROR} or {@link #SUCCESS} of <b>queuing</b> the playSilentUtterance operation.
     */
    public int playSilentUtterance(final long durationInMs, final int queueMode, final java.lang.String utteranceId) {
        return runAction(new android.speech.tts.TextToSpeech.Action<java.lang.Integer>() {
            @java.lang.Override
            public java.lang.Integer run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException {
                return service.playSilence(getCallerIdentity(), durationInMs, queueMode, utteranceId);
            }
        }, android.speech.tts.TextToSpeech.ERROR, "playSilentUtterance");
    }

    /**
     * Plays silence for the specified amount of time using the specified
     * queue mode.
     * This method is asynchronous, i.e. the method just adds the request to the queue of TTS
     * requests and then returns. The synthesis might not have finished (or even started!) at the
     * time when this method returns. In order to reliably detect errors during synthesis,
     * we recommend setting an utterance progress listener (see
     * {@link #setOnUtteranceProgressListener}) and using the
     * {@link Engine#KEY_PARAM_UTTERANCE_ID} parameter.
     *
     * @param durationInMs
     * 		The duration of the silence.
     * @param queueMode
     * 		{@link #QUEUE_ADD} or {@link #QUEUE_FLUSH}.
     * @param params
     * 		Parameters for the request. Can be null.
     * 		Supported parameter names:
     * 		{@link Engine#KEY_PARAM_UTTERANCE_ID}.
     * 		Engine specific parameters may be passed in but the parameter keys
     * 		must be prefixed by the name of the engine they are intended for. For example
     * 		the keys "com.svox.pico_foo" and "com.svox.pico:bar" will be passed to the
     * 		engine named "com.svox.pico" if it is being used.
     * @return {@link #ERROR} or {@link #SUCCESS} of <b>queuing</b> the playSilence operation.
     * @deprecated As of API level 21, replaced by
    {@link #playSilentUtterance(long, int, String)}.
     */
    @java.lang.Deprecated
    public int playSilence(final long durationInMs, final int queueMode, final java.util.HashMap<java.lang.String, java.lang.String> params) {
        return playSilentUtterance(durationInMs, queueMode, params == null ? null : params.get(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID));
    }

    /**
     * Queries the engine for the set of features it supports for a given locale.
     * Features can either be framework defined, e.g.
     * {@link TextToSpeech.Engine#KEY_FEATURE_NETWORK_SYNTHESIS} or engine specific.
     * Engine specific keys must be prefixed by the name of the engine they
     * are intended for. These keys can be used as parameters to
     * {@link TextToSpeech#speak(String, int, java.util.HashMap)} and
     * {@link TextToSpeech#synthesizeToFile(String, java.util.HashMap, String)}.
     *
     * Features values are strings and their values must meet restrictions described in their
     * documentation.
     *
     * @param locale
     * 		The locale to query features for.
     * @return Set instance. May return {@code null} on error.
     * @deprecated As of API level 21, please use voices. In order to query features of the voice,
    call {@link #getVoices()} to retrieve the list of available voices and
    {@link Voice#getFeatures()} to retrieve the set of features.
     */
    @java.lang.Deprecated
    public java.util.Set<java.lang.String> getFeatures(final java.util.Locale locale) {
        return runAction(new android.speech.tts.TextToSpeech.Action<java.util.Set<java.lang.String>>() {
            @java.lang.Override
            public java.util.Set<java.lang.String> run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException {
                java.lang.String[] features = null;
                try {
                    features = service.getFeaturesForLanguage(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant());
                } catch (java.util.MissingResourceException e) {
                    android.util.Log.w(android.speech.tts.TextToSpeech.TAG, ("Couldn't retrieve 3 letter ISO 639-2/T language and/or ISO 3166 " + "country code for locale: ") + locale, e);
                    return null;
                }
                if (features != null) {
                    final java.util.Set<java.lang.String> featureSet = new java.util.HashSet<java.lang.String>();
                    java.util.Collections.addAll(featureSet, features);
                    return featureSet;
                }
                return null;
            }
        }, null, "getFeatures");
    }

    /**
     * Checks whether the TTS engine is busy speaking. Note that a speech item is
     * considered complete once it's audio data has been sent to the audio mixer, or
     * written to a file. There might be a finite lag between this point, and when
     * the audio hardware completes playback.
     *
     * @return {@code true} if the TTS engine is speaking.
     */
    public boolean isSpeaking() {
        return runAction(new android.speech.tts.TextToSpeech.Action<java.lang.Boolean>() {
            @java.lang.Override
            public java.lang.Boolean run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException {
                return service.isSpeaking();
            }
        }, false, "isSpeaking");
    }

    /**
     * Interrupts the current utterance (whether played or rendered to file) and discards other
     * utterances in the queue.
     *
     * @return {@link #ERROR} or {@link #SUCCESS}.
     */
    public int stop() {
        return runAction(new android.speech.tts.TextToSpeech.Action<java.lang.Integer>() {
            @java.lang.Override
            public java.lang.Integer run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException {
                return service.stop(getCallerIdentity());
            }
        }, android.speech.tts.TextToSpeech.ERROR, "stop");
    }

    /**
     * Sets the speech rate.
     *
     * This has no effect on any pre-recorded speech.
     *
     * @param speechRate
     * 		Speech rate. {@code 1.0} is the normal speech rate,
     * 		lower values slow down the speech ({@code 0.5} is half the normal speech rate),
     * 		greater values accelerate it ({@code 2.0} is twice the normal speech rate).
     * @return {@link #ERROR} or {@link #SUCCESS}.
     */
    public int setSpeechRate(float speechRate) {
        if (speechRate > 0.0F) {
            int intRate = ((int) (speechRate * 100));
            if (intRate > 0) {
                synchronized(mStartLock) {
                    mParams.putInt(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_RATE, intRate);
                }
                return android.speech.tts.TextToSpeech.SUCCESS;
            }
        }
        return android.speech.tts.TextToSpeech.ERROR;
    }

    /**
     * Sets the speech pitch for the TextToSpeech engine.
     *
     * This has no effect on any pre-recorded speech.
     *
     * @param pitch
     * 		Speech pitch. {@code 1.0} is the normal pitch,
     * 		lower values lower the tone of the synthesized voice,
     * 		greater values increase it.
     * @return {@link #ERROR} or {@link #SUCCESS}.
     */
    public int setPitch(float pitch) {
        if (pitch > 0.0F) {
            int intPitch = ((int) (pitch * 100));
            if (intPitch > 0) {
                synchronized(mStartLock) {
                    mParams.putInt(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_PITCH, intPitch);
                }
                return android.speech.tts.TextToSpeech.SUCCESS;
            }
        }
        return android.speech.tts.TextToSpeech.ERROR;
    }

    /**
     * Sets the audio attributes to be used when speaking text or playing
     * back a file.
     *
     * @param audioAttributes
     * 		Valid AudioAttributes instance.
     * @return {@link #ERROR} or {@link #SUCCESS}.
     */
    public int setAudioAttributes(android.media.AudioAttributes audioAttributes) {
        if (audioAttributes != null) {
            synchronized(mStartLock) {
                mParams.putParcelable(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_AUDIO_ATTRIBUTES, audioAttributes);
            }
            return android.speech.tts.TextToSpeech.SUCCESS;
        }
        return android.speech.tts.TextToSpeech.ERROR;
    }

    /**
     *
     *
     * @return the engine currently in use by this TextToSpeech instance.
     * @unknown 
     */
    public java.lang.String getCurrentEngine() {
        return mCurrentEngine;
    }

    /**
     * Returns a Locale instance describing the language currently being used as the default
     * Text-to-speech language.
     *
     * The locale object returned by this method is NOT a valid one. It has identical form to the
     * one in {@link #getLanguage()}. Please refer to {@link #getLanguage()} for more information.
     *
     * @return language, country (if any) and variant (if any) used by the client stored in a
    Locale instance, or {@code null} on error.
     * @deprecated As of API level 21, use <code>getDefaultVoice().getLocale()</code> ({@link #getDefaultVoice()})
     */
    @java.lang.Deprecated
    public java.util.Locale getDefaultLanguage() {
        return runAction(new android.speech.tts.TextToSpeech.Action<java.util.Locale>() {
            @java.lang.Override
            public java.util.Locale run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException {
                java.lang.String[] defaultLanguage = service.getClientDefaultLanguage();
                return new java.util.Locale(defaultLanguage[0], defaultLanguage[1], defaultLanguage[2]);
            }
        }, null, "getDefaultLanguage");
    }

    /**
     * Sets the text-to-speech language.
     * The TTS engine will try to use the closest match to the specified
     * language as represented by the Locale, but there is no guarantee that the exact same Locale
     * will be used. Use {@link #isLanguageAvailable(Locale)} to check the level of support
     * before choosing the language to use for the next utterances.
     *
     * This method sets the current voice to the default one for the given Locale;
     * {@link #getVoice()} can be used to retrieve it.
     *
     * @param loc
     * 		The locale describing the language to be used.
     * @return Code indicating the support status for the locale. See {@link #LANG_AVAILABLE},
    {@link #LANG_COUNTRY_AVAILABLE}, {@link #LANG_COUNTRY_VAR_AVAILABLE},
    {@link #LANG_MISSING_DATA} and {@link #LANG_NOT_SUPPORTED}.
     */
    public int setLanguage(final java.util.Locale loc) {
        return runAction(new android.speech.tts.TextToSpeech.Action<java.lang.Integer>() {
            @java.lang.Override
            public java.lang.Integer run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException {
                if (loc == null) {
                    return android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED;
                }
                java.lang.String language = null;
                java.lang.String country = null;
                try {
                    language = loc.getISO3Language();
                } catch (java.util.MissingResourceException e) {
                    android.util.Log.w(android.speech.tts.TextToSpeech.TAG, "Couldn't retrieve ISO 639-2/T language code for locale: " + loc, e);
                    return android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED;
                }
                try {
                    country = loc.getISO3Country();
                } catch (java.util.MissingResourceException e) {
                    android.util.Log.w(android.speech.tts.TextToSpeech.TAG, "Couldn't retrieve ISO 3166 country code for locale: " + loc, e);
                    return android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED;
                }
                java.lang.String variant = loc.getVariant();
                // As of API level 21, setLanguage is implemented using setVoice.
                // (which, in the default implementation, will call loadLanguage on the service
                // interface).
                // Sanitize locale using isLanguageAvailable.
                int result = service.isLanguageAvailable(language, country, variant);
                if (result >= android.speech.tts.TextToSpeech.LANG_AVAILABLE) {
                    // Get the default voice for the locale.
                    java.lang.String voiceName = service.getDefaultVoiceNameFor(language, country, variant);
                    if (android.text.TextUtils.isEmpty(voiceName)) {
                        android.util.Log.w(android.speech.tts.TextToSpeech.TAG, (((("Couldn't find the default voice for " + language) + "-") + country) + "-") + variant);
                        return android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED;
                    }
                    // Load it.
                    if (service.loadVoice(getCallerIdentity(), voiceName) == android.speech.tts.TextToSpeech.ERROR) {
                        android.util.Log.w(android.speech.tts.TextToSpeech.TAG, ((((((("The service claimed " + language) + "-") + country) + "-") + variant) + " was available with voice name ") + voiceName) + " but loadVoice returned ERROR");
                        return android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED;
                    }
                    // Set the language/country/variant of the voice, so #getLanguage will return
                    // the currently set voice locale when called.
                    android.speech.tts.Voice voice = getVoice(service, voiceName);
                    if (voice == null) {
                        android.util.Log.w(android.speech.tts.TextToSpeech.TAG, ((((((("getDefaultVoiceNameFor returned " + voiceName) + " for locale ") + language) + "-") + country) + "-") + variant) + " but getVoice returns null");
                        return android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED;
                    }
                    java.lang.String voiceLanguage = "";
                    try {
                        voiceLanguage = voice.getLocale().getISO3Language();
                    } catch (java.util.MissingResourceException e) {
                        android.util.Log.w(android.speech.tts.TextToSpeech.TAG, "Couldn't retrieve ISO 639-2/T language code for locale: " + voice.getLocale(), e);
                    }
                    java.lang.String voiceCountry = "";
                    try {
                        voiceCountry = voice.getLocale().getISO3Country();
                    } catch (java.util.MissingResourceException e) {
                        android.util.Log.w(android.speech.tts.TextToSpeech.TAG, "Couldn't retrieve ISO 3166 country code for locale: " + voice.getLocale(), e);
                    }
                    mParams.putString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_VOICE_NAME, voiceName);
                    mParams.putString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_LANGUAGE, voiceLanguage);
                    mParams.putString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_COUNTRY, voiceCountry);
                    mParams.putString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_VARIANT, voice.getLocale().getVariant());
                }
                return result;
            }
        }, android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED, "setLanguage");
    }

    /**
     * Returns a Locale instance describing the language currently being used for synthesis
     * requests sent to the TextToSpeech engine.
     *
     * In Android 4.2 and before (API <= 17) this function returns the language that is currently
     * being used by the TTS engine. That is the last language set by this or any other
     * client by a {@link TextToSpeech#setLanguage} call to the same engine.
     *
     * In Android versions after 4.2 this function returns the language that is currently being
     * used for the synthesis requests sent from this client. That is the last language set
     * by a {@link TextToSpeech#setLanguage} call on this instance.
     *
     * If a voice is set (by {@link #setVoice(Voice)}), getLanguage will return the language of
     * the currently set voice.
     *
     * Please note that the Locale object returned by this method is NOT a valid Locale object. Its
     * language field contains a three-letter ISO 639-2/T code (where a proper Locale would use
     * a two-letter ISO 639-1 code), and the country field contains a three-letter ISO 3166 country
     * code (where a proper Locale would use a two-letter ISO 3166-1 code).
     *
     * @return language, country (if any) and variant (if any) used by the client stored in a
    Locale instance, or {@code null} on error.
     * @deprecated As of API level 21, please use <code>getVoice().getLocale()</code>
    ({@link #getVoice()}).
     */
    @java.lang.Deprecated
    public java.util.Locale getLanguage() {
        return runAction(new android.speech.tts.TextToSpeech.Action<java.util.Locale>() {
            @java.lang.Override
            public java.util.Locale run(android.speech.tts.ITextToSpeechService service) {
                /* No service call, but we're accessing mParams, hence need for
                wrapping it as an Action instance
                 */
                java.lang.String lang = mParams.getString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_LANGUAGE, "");
                java.lang.String country = mParams.getString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_COUNTRY, "");
                java.lang.String variant = mParams.getString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_VARIANT, "");
                return new java.util.Locale(lang, country, variant);
            }
        }, null, "getLanguage");
    }

    /**
     * Query the engine about the set of available languages.
     */
    public java.util.Set<java.util.Locale> getAvailableLanguages() {
        return runAction(new android.speech.tts.TextToSpeech.Action<java.util.Set<java.util.Locale>>() {
            @java.lang.Override
            public java.util.Set<java.util.Locale> run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException {
                java.util.List<android.speech.tts.Voice> voices = service.getVoices();
                if (voices == null) {
                    return new java.util.HashSet<java.util.Locale>();
                }
                java.util.HashSet<java.util.Locale> locales = new java.util.HashSet<java.util.Locale>();
                for (android.speech.tts.Voice voice : voices) {
                    locales.add(voice.getLocale());
                }
                return locales;
            }
        }, null, "getAvailableLanguages");
    }

    /**
     * Query the engine about the set of available voices.
     *
     * Each TTS Engine can expose multiple voices for each locale, each with a different set of
     * features.
     *
     * @see #setVoice(Voice)
     * @see Voice
     */
    public java.util.Set<android.speech.tts.Voice> getVoices() {
        return runAction(new android.speech.tts.TextToSpeech.Action<java.util.Set<android.speech.tts.Voice>>() {
            @java.lang.Override
            public java.util.Set<android.speech.tts.Voice> run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException {
                java.util.List<android.speech.tts.Voice> voices = service.getVoices();
                return voices != null ? new java.util.HashSet<android.speech.tts.Voice>(voices) : new java.util.HashSet<android.speech.tts.Voice>();
            }
        }, null, "getVoices");
    }

    /**
     * Sets the text-to-speech voice.
     *
     * @param voice
     * 		One of objects returned by {@link #getVoices()}.
     * @return {@link #ERROR} or {@link #SUCCESS}.
     * @see #getVoices
     * @see Voice
     */
    public int setVoice(final android.speech.tts.Voice voice) {
        return runAction(new android.speech.tts.TextToSpeech.Action<java.lang.Integer>() {
            @java.lang.Override
            public java.lang.Integer run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException {
                int result = service.loadVoice(getCallerIdentity(), voice.getName());
                if (result == android.speech.tts.TextToSpeech.SUCCESS) {
                    mParams.putString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_VOICE_NAME, voice.getName());
                    // Set the language/country/variant, so #getLanguage will return the voice
                    // locale when called.
                    java.lang.String language = "";
                    try {
                        language = voice.getLocale().getISO3Language();
                    } catch (java.util.MissingResourceException e) {
                        android.util.Log.w(android.speech.tts.TextToSpeech.TAG, "Couldn't retrieve ISO 639-2/T language code for locale: " + voice.getLocale(), e);
                    }
                    java.lang.String country = "";
                    try {
                        country = voice.getLocale().getISO3Country();
                    } catch (java.util.MissingResourceException e) {
                        android.util.Log.w(android.speech.tts.TextToSpeech.TAG, "Couldn't retrieve ISO 3166 country code for locale: " + voice.getLocale(), e);
                    }
                    mParams.putString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_LANGUAGE, language);
                    mParams.putString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_COUNTRY, country);
                    mParams.putString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_VARIANT, voice.getLocale().getVariant());
                }
                return result;
            }
        }, android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED, "setVoice");
    }

    /**
     * Returns a Voice instance describing the voice currently being used for synthesis
     * requests sent to the TextToSpeech engine.
     *
     * @return Voice instance used by the client, or {@code null} if not set or on error.
     * @see #getVoices
     * @see #setVoice
     * @see Voice
     */
    public android.speech.tts.Voice getVoice() {
        return runAction(new android.speech.tts.TextToSpeech.Action<android.speech.tts.Voice>() {
            @java.lang.Override
            public android.speech.tts.Voice run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException {
                java.lang.String voiceName = mParams.getString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_VOICE_NAME, "");
                if (android.text.TextUtils.isEmpty(voiceName)) {
                    return null;
                }
                return getVoice(service, voiceName);
            }
        }, null, "getVoice");
    }

    /**
     * Returns a Voice instance of the voice with the given voice name.
     *
     * @return Voice instance with the given voice name, or {@code null} if not set or on error.
     * @see Voice
     */
    private android.speech.tts.Voice getVoice(android.speech.tts.ITextToSpeechService service, java.lang.String voiceName) throws android.os.RemoteException {
        java.util.List<android.speech.tts.Voice> voices = service.getVoices();
        if (voices == null) {
            android.util.Log.w(android.speech.tts.TextToSpeech.TAG, "getVoices returned null");
            return null;
        }
        for (android.speech.tts.Voice voice : voices) {
            if (voice.getName().equals(voiceName)) {
                return voice;
            }
        }
        android.util.Log.w(android.speech.tts.TextToSpeech.TAG, ("Could not find voice " + voiceName) + " in voice list");
        return null;
    }

    /**
     * Returns a Voice instance that's the default voice for the default Text-to-speech language.
     *
     * @return The default voice instance for the default language, or {@code null} if not set or
    on error.
     */
    public android.speech.tts.Voice getDefaultVoice() {
        return runAction(new android.speech.tts.TextToSpeech.Action<android.speech.tts.Voice>() {
            @java.lang.Override
            public android.speech.tts.Voice run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException {
                java.lang.String[] defaultLanguage = service.getClientDefaultLanguage();
                if ((defaultLanguage == null) || (defaultLanguage.length == 0)) {
                    android.util.Log.e(android.speech.tts.TextToSpeech.TAG, "service.getClientDefaultLanguage() returned empty array");
                    return null;
                }
                java.lang.String language = defaultLanguage[0];
                java.lang.String country = (defaultLanguage.length > 1) ? defaultLanguage[1] : "";
                java.lang.String variant = (defaultLanguage.length > 2) ? defaultLanguage[2] : "";
                // Sanitize the locale using isLanguageAvailable.
                int result = service.isLanguageAvailable(language, country, variant);
                if (result < android.speech.tts.TextToSpeech.LANG_AVAILABLE) {
                    // The default language is not supported.
                    return null;
                }
                // Get the default voice name
                java.lang.String voiceName = service.getDefaultVoiceNameFor(language, country, variant);
                if (android.text.TextUtils.isEmpty(voiceName)) {
                    return null;
                }
                // Find it
                java.util.List<android.speech.tts.Voice> voices = service.getVoices();
                if (voices == null) {
                    return null;
                }
                for (android.speech.tts.Voice voice : voices) {
                    if (voice.getName().equals(voiceName)) {
                        return voice;
                    }
                }
                return null;
            }
        }, null, "getDefaultVoice");
    }

    /**
     * Checks if the specified language as represented by the Locale is available and supported.
     *
     * @param loc
     * 		The Locale describing the language to be used.
     * @return Code indicating the support status for the locale. See {@link #LANG_AVAILABLE},
    {@link #LANG_COUNTRY_AVAILABLE}, {@link #LANG_COUNTRY_VAR_AVAILABLE},
    {@link #LANG_MISSING_DATA} and {@link #LANG_NOT_SUPPORTED}.
     */
    public int isLanguageAvailable(final java.util.Locale loc) {
        return runAction(new android.speech.tts.TextToSpeech.Action<java.lang.Integer>() {
            @java.lang.Override
            public java.lang.Integer run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException {
                java.lang.String language = null;
                java.lang.String country = null;
                try {
                    language = loc.getISO3Language();
                } catch (java.util.MissingResourceException e) {
                    android.util.Log.w(android.speech.tts.TextToSpeech.TAG, "Couldn't retrieve ISO 639-2/T language code for locale: " + loc, e);
                    return android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED;
                }
                try {
                    country = loc.getISO3Country();
                } catch (java.util.MissingResourceException e) {
                    android.util.Log.w(android.speech.tts.TextToSpeech.TAG, "Couldn't retrieve ISO 3166 country code for locale: " + loc, e);
                    return android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED;
                }
                return service.isLanguageAvailable(language, country, loc.getVariant());
            }
        }, android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED, "isLanguageAvailable");
    }

    /**
     * Synthesizes the given text to a file using the specified parameters.
     * This method is asynchronous, i.e. the method just adds the request to the queue of TTS
     * requests and then returns. The synthesis might not have finished (or even started!) at the
     * time when this method returns. In order to reliably detect errors during synthesis,
     * we recommend setting an utterance progress listener (see
     * {@link #setOnUtteranceProgressListener}).
     *
     * @param text
     * 		The text that should be synthesized. No longer than
     * 		{@link #getMaxSpeechInputLength()} characters.
     * @param params
     * 		Parameters for the request. Can be null.
     * 		Engine specific parameters may be passed in but the parameter keys
     * 		must be prefixed by the name of the engine they are intended for. For example
     * 		the keys "com.svox.pico_foo" and "com.svox.pico:bar" will be passed to the
     * 		engine named "com.svox.pico" if it is being used.
     * @param file
     * 		File to write the generated audio data to.
     * @param utteranceId
     * 		An unique identifier for this request.
     * @return {@link #ERROR} or {@link #SUCCESS} of <b>queuing</b> the synthesizeToFile operation.
     */
    public int synthesizeToFile(final java.lang.CharSequence text, final android.os.Bundle params, final java.io.File file, final java.lang.String utteranceId) {
        return runAction(new android.speech.tts.TextToSpeech.Action<java.lang.Integer>() {
            @java.lang.Override
            public java.lang.Integer run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException {
                android.os.ParcelFileDescriptor fileDescriptor;
                int returnValue;
                try {
                    if (file.exists() && (!file.canWrite())) {
                        android.util.Log.e(android.speech.tts.TextToSpeech.TAG, "Can't write to " + file);
                        return android.speech.tts.TextToSpeech.ERROR;
                    }
                    fileDescriptor = android.os.ParcelFileDescriptor.open(file, (android.os.ParcelFileDescriptor.MODE_WRITE_ONLY | android.os.ParcelFileDescriptor.MODE_CREATE) | android.os.ParcelFileDescriptor.MODE_TRUNCATE);
                    returnValue = service.synthesizeToFileDescriptor(getCallerIdentity(), text, fileDescriptor, getParams(params), utteranceId);
                    fileDescriptor.close();
                    return returnValue;
                } catch (java.io.FileNotFoundException e) {
                    android.util.Log.e(android.speech.tts.TextToSpeech.TAG, ("Opening file " + file) + " failed", e);
                    return android.speech.tts.TextToSpeech.ERROR;
                } catch (java.io.IOException e) {
                    android.util.Log.e(android.speech.tts.TextToSpeech.TAG, ("Closing file " + file) + " failed", e);
                    return android.speech.tts.TextToSpeech.ERROR;
                }
            }
        }, android.speech.tts.TextToSpeech.ERROR, "synthesizeToFile");
    }

    /**
     * Synthesizes the given text to a file using the specified parameters.
     * This method is asynchronous, i.e. the method just adds the request to the queue of TTS
     * requests and then returns. The synthesis might not have finished (or even started!) at the
     * time when this method returns. In order to reliably detect errors during synthesis,
     * we recommend setting an utterance progress listener (see
     * {@link #setOnUtteranceProgressListener}) and using the
     * {@link Engine#KEY_PARAM_UTTERANCE_ID} parameter.
     *
     * @param text
     * 		The text that should be synthesized. No longer than
     * 		{@link #getMaxSpeechInputLength()} characters.
     * @param params
     * 		Parameters for the request. Can be null.
     * 		Supported parameter names:
     * 		{@link Engine#KEY_PARAM_UTTERANCE_ID}.
     * 		Engine specific parameters may be passed in but the parameter keys
     * 		must be prefixed by the name of the engine they are intended for. For example
     * 		the keys "com.svox.pico_foo" and "com.svox.pico:bar" will be passed to the
     * 		engine named "com.svox.pico" if it is being used.
     * @param filename
     * 		Absolute file filename to write the generated audio data to.It should be
     * 		something like "/sdcard/myappsounds/mysound.wav".
     * @return {@link #ERROR} or {@link #SUCCESS} of <b>queuing</b> the synthesizeToFile operation.
     * @deprecated As of API level 21, replaced by
    {@link #synthesizeToFile(CharSequence, Bundle, File, String)}.
     */
    @java.lang.Deprecated
    public int synthesizeToFile(final java.lang.String text, final java.util.HashMap<java.lang.String, java.lang.String> params, final java.lang.String filename) {
        return synthesizeToFile(text, convertParamsHashMaptoBundle(params), new java.io.File(filename), params.get(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID));
    }

    private android.os.Bundle convertParamsHashMaptoBundle(java.util.HashMap<java.lang.String, java.lang.String> params) {
        if ((params != null) && (!params.isEmpty())) {
            android.os.Bundle bundle = new android.os.Bundle();
            copyIntParam(bundle, params, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_STREAM);
            copyIntParam(bundle, params, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_SESSION_ID);
            copyStringParam(bundle, params, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
            copyFloatParam(bundle, params, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_VOLUME);
            copyFloatParam(bundle, params, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_PAN);
            // Copy feature strings defined by the framework.
            copyStringParam(bundle, params, android.speech.tts.TextToSpeech.Engine.KEY_FEATURE_NETWORK_SYNTHESIS);
            copyStringParam(bundle, params, android.speech.tts.TextToSpeech.Engine.KEY_FEATURE_EMBEDDED_SYNTHESIS);
            copyIntParam(bundle, params, android.speech.tts.TextToSpeech.Engine.KEY_FEATURE_NETWORK_TIMEOUT_MS);
            copyIntParam(bundle, params, android.speech.tts.TextToSpeech.Engine.KEY_FEATURE_NETWORK_RETRIES_COUNT);
            // Copy over all parameters that start with the name of the
            // engine that we are currently connected to. The engine is
            // free to interpret them as it chooses.
            if (!android.text.TextUtils.isEmpty(mCurrentEngine)) {
                for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : params.entrySet()) {
                    final java.lang.String key = entry.getKey();
                    if ((key != null) && key.startsWith(mCurrentEngine)) {
                        bundle.putString(key, entry.getValue());
                    }
                }
            }
            return bundle;
        }
        return null;
    }

    private android.os.Bundle getParams(android.os.Bundle params) {
        if ((params != null) && (!params.isEmpty())) {
            android.os.Bundle bundle = new android.os.Bundle(mParams);
            bundle.putAll(params);
            android.speech.tts.TextToSpeech.verifyIntegerBundleParam(bundle, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_STREAM);
            android.speech.tts.TextToSpeech.verifyIntegerBundleParam(bundle, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_SESSION_ID);
            android.speech.tts.TextToSpeech.verifyStringBundleParam(bundle, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
            android.speech.tts.TextToSpeech.verifyFloatBundleParam(bundle, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_VOLUME);
            android.speech.tts.TextToSpeech.verifyFloatBundleParam(bundle, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_PAN);
            // Copy feature strings defined by the framework.
            android.speech.tts.TextToSpeech.verifyBooleanBundleParam(bundle, android.speech.tts.TextToSpeech.Engine.KEY_FEATURE_NETWORK_SYNTHESIS);
            android.speech.tts.TextToSpeech.verifyBooleanBundleParam(bundle, android.speech.tts.TextToSpeech.Engine.KEY_FEATURE_EMBEDDED_SYNTHESIS);
            android.speech.tts.TextToSpeech.verifyIntegerBundleParam(bundle, android.speech.tts.TextToSpeech.Engine.KEY_FEATURE_NETWORK_TIMEOUT_MS);
            android.speech.tts.TextToSpeech.verifyIntegerBundleParam(bundle, android.speech.tts.TextToSpeech.Engine.KEY_FEATURE_NETWORK_RETRIES_COUNT);
            return bundle;
        } else {
            return mParams;
        }
    }

    private static boolean verifyIntegerBundleParam(android.os.Bundle bundle, java.lang.String key) {
        if (bundle.containsKey(key)) {
            if (!((bundle.get(key) instanceof java.lang.Integer) || (bundle.get(key) instanceof java.lang.Long))) {
                bundle.remove(key);
                android.util.Log.w(android.speech.tts.TextToSpeech.TAG, (("Synthesis request paramter " + key) + " containst value ") + " with invalid type. Should be an Integer or a Long");
                return false;
            }
        }
        return true;
    }

    private static boolean verifyStringBundleParam(android.os.Bundle bundle, java.lang.String key) {
        if (bundle.containsKey(key)) {
            if (!(bundle.get(key) instanceof java.lang.String)) {
                bundle.remove(key);
                android.util.Log.w(android.speech.tts.TextToSpeech.TAG, (("Synthesis request paramter " + key) + " containst value ") + " with invalid type. Should be a String");
                return false;
            }
        }
        return true;
    }

    private static boolean verifyBooleanBundleParam(android.os.Bundle bundle, java.lang.String key) {
        if (bundle.containsKey(key)) {
            if (!((bundle.get(key) instanceof java.lang.Boolean) || (bundle.get(key) instanceof java.lang.String))) {
                bundle.remove(key);
                android.util.Log.w(android.speech.tts.TextToSpeech.TAG, (("Synthesis request paramter " + key) + " containst value ") + " with invalid type. Should be a Boolean or String");
                return false;
            }
        }
        return true;
    }

    private static boolean verifyFloatBundleParam(android.os.Bundle bundle, java.lang.String key) {
        if (bundle.containsKey(key)) {
            if (!((bundle.get(key) instanceof java.lang.Float) || (bundle.get(key) instanceof java.lang.Double))) {
                bundle.remove(key);
                android.util.Log.w(android.speech.tts.TextToSpeech.TAG, (("Synthesis request paramter " + key) + " containst value ") + " with invalid type. Should be a Float or a Double");
                return false;
            }
        }
        return true;
    }

    private void copyStringParam(android.os.Bundle bundle, java.util.HashMap<java.lang.String, java.lang.String> params, java.lang.String key) {
        java.lang.String value = params.get(key);
        if (value != null) {
            bundle.putString(key, value);
        }
    }

    private void copyIntParam(android.os.Bundle bundle, java.util.HashMap<java.lang.String, java.lang.String> params, java.lang.String key) {
        java.lang.String valueString = params.get(key);
        if (!android.text.TextUtils.isEmpty(valueString)) {
            try {
                int value = java.lang.Integer.parseInt(valueString);
                bundle.putInt(key, value);
            } catch (java.lang.NumberFormatException ex) {
                // don't set the value in the bundle
            }
        }
    }

    private void copyFloatParam(android.os.Bundle bundle, java.util.HashMap<java.lang.String, java.lang.String> params, java.lang.String key) {
        java.lang.String valueString = params.get(key);
        if (!android.text.TextUtils.isEmpty(valueString)) {
            try {
                float value = java.lang.Float.parseFloat(valueString);
                bundle.putFloat(key, value);
            } catch (java.lang.NumberFormatException ex) {
                // don't set the value in the bundle
            }
        }
    }

    /**
     * Sets the listener that will be notified when synthesis of an utterance completes.
     *
     * @param listener
     * 		The listener to use.
     * @return {@link #ERROR} or {@link #SUCCESS}.
     * @deprecated Use {@link #setOnUtteranceProgressListener(UtteranceProgressListener)}
    instead.
     */
    @java.lang.Deprecated
    public int setOnUtteranceCompletedListener(final android.speech.tts.TextToSpeech.OnUtteranceCompletedListener listener) {
        mUtteranceProgressListener = android.speech.tts.UtteranceProgressListener.from(listener);
        return android.speech.tts.TextToSpeech.SUCCESS;
    }

    /**
     * Sets the listener that will be notified of various events related to the
     * synthesis of a given utterance.
     *
     * See {@link UtteranceProgressListener} and
     * {@link TextToSpeech.Engine#KEY_PARAM_UTTERANCE_ID}.
     *
     * @param listener
     * 		the listener to use.
     * @return {@link #ERROR} or {@link #SUCCESS}
     */
    public int setOnUtteranceProgressListener(android.speech.tts.UtteranceProgressListener listener) {
        mUtteranceProgressListener = listener;
        return android.speech.tts.TextToSpeech.SUCCESS;
    }

    /**
     * Sets the TTS engine to use.
     *
     * @deprecated This doesn't inform callers when the TTS engine has been
    initialized. {@link #TextToSpeech(Context, OnInitListener, String)}
    can be used with the appropriate engine name. Also, there is no
    guarantee that the engine specified will be loaded. If it isn't
    installed or disabled, the user / system wide defaults will apply.
     * @param enginePackageName
     * 		The package name for the synthesis engine (e.g. "com.svox.pico")
     * @return {@link #ERROR} or {@link #SUCCESS}.
     */
    @java.lang.Deprecated
    public int setEngineByPackageName(java.lang.String enginePackageName) {
        mRequestedEngine = enginePackageName;
        return initTts();
    }

    /**
     * Gets the package name of the default speech synthesis engine.
     *
     * @return Package name of the TTS engine that the user has chosen
    as their default.
     */
    public java.lang.String getDefaultEngine() {
        return mEnginesHelper.getDefaultEngine();
    }

    /**
     * Checks whether the user's settings should override settings requested
     * by the calling application. As of the Ice cream sandwich release,
     * user settings never forcibly override the app's settings.
     */
    @java.lang.Deprecated
    public boolean areDefaultsEnforced() {
        return false;
    }

    /**
     * Gets a list of all installed TTS engines.
     *
     * @return A list of engine info objects. The list can be empty, but never {@code null}.
     */
    public java.util.List<android.speech.tts.TextToSpeech.EngineInfo> getEngines() {
        return mEnginesHelper.getEngines();
    }

    private class Connection implements android.content.ServiceConnection {
        private android.speech.tts.ITextToSpeechService mService;

        private android.speech.tts.TextToSpeech.Connection.SetupConnectionAsyncTask mOnSetupConnectionAsyncTask;

        private boolean mEstablished;

        private final ITextToSpeechCallback.Stub mCallback = new android.speech.tts.ITextToSpeechCallback.Stub() {
            public void onStop(java.lang.String utteranceId, boolean isStarted) throws android.os.RemoteException {
                android.speech.tts.UtteranceProgressListener listener = mUtteranceProgressListener;
                if (listener != null) {
                    listener.onStop(utteranceId, isStarted);
                }
            }

            @java.lang.Override
            public void onSuccess(java.lang.String utteranceId) {
                android.speech.tts.UtteranceProgressListener listener = mUtteranceProgressListener;
                if (listener != null) {
                    listener.onDone(utteranceId);
                }
            }

            @java.lang.Override
            public void onError(java.lang.String utteranceId, int errorCode) {
                android.speech.tts.UtteranceProgressListener listener = mUtteranceProgressListener;
                if (listener != null) {
                    listener.onError(utteranceId);
                }
            }

            @java.lang.Override
            public void onStart(java.lang.String utteranceId) {
                android.speech.tts.UtteranceProgressListener listener = mUtteranceProgressListener;
                if (listener != null) {
                    listener.onStart(utteranceId);
                }
            }

            @java.lang.Override
            public void onBeginSynthesis(java.lang.String utteranceId, int sampleRateInHz, int audioFormat, int channelCount) {
                android.speech.tts.UtteranceProgressListener listener = mUtteranceProgressListener;
                if (listener != null) {
                    listener.onBeginSynthesis(utteranceId, sampleRateInHz, audioFormat, channelCount);
                }
            }

            @java.lang.Override
            public void onAudioAvailable(java.lang.String utteranceId, byte[] audio) {
                android.speech.tts.UtteranceProgressListener listener = mUtteranceProgressListener;
                if (listener != null) {
                    listener.onAudioAvailable(utteranceId, audio);
                }
            }
        };

        private class SetupConnectionAsyncTask extends android.os.AsyncTask<java.lang.Void, java.lang.Void, java.lang.Integer> {
            private final android.content.ComponentName mName;

            public SetupConnectionAsyncTask(android.content.ComponentName name) {
                mName = name;
            }

            @java.lang.Override
            protected java.lang.Integer doInBackground(java.lang.Void... params) {
                synchronized(mStartLock) {
                    if (isCancelled()) {
                        return null;
                    }
                    try {
                        mService.setCallback(getCallerIdentity(), mCallback);
                        if (mParams.getString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_LANGUAGE) == null) {
                            java.lang.String[] defaultLanguage = mService.getClientDefaultLanguage();
                            mParams.putString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_LANGUAGE, defaultLanguage[0]);
                            mParams.putString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_COUNTRY, defaultLanguage[1]);
                            mParams.putString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_VARIANT, defaultLanguage[2]);
                            // Get the default voice for the locale.
                            java.lang.String defaultVoiceName = mService.getDefaultVoiceNameFor(defaultLanguage[0], defaultLanguage[1], defaultLanguage[2]);
                            mParams.putString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_VOICE_NAME, defaultVoiceName);
                        }
                        android.util.Log.i(android.speech.tts.TextToSpeech.TAG, "Set up connection to " + mName);
                        return android.speech.tts.TextToSpeech.SUCCESS;
                    } catch (android.os.RemoteException re) {
                        android.util.Log.e(android.speech.tts.TextToSpeech.TAG, "Error connecting to service, setCallback() failed");
                        return android.speech.tts.TextToSpeech.ERROR;
                    }
                }
            }

            @java.lang.Override
            protected void onPostExecute(java.lang.Integer result) {
                synchronized(mStartLock) {
                    if (mOnSetupConnectionAsyncTask == this) {
                        mOnSetupConnectionAsyncTask = null;
                    }
                    mEstablished = true;
                    dispatchOnInit(result);
                }
            }
        }

        @java.lang.Override
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            synchronized(mStartLock) {
                mConnectingServiceConnection = null;
                android.util.Log.i(android.speech.tts.TextToSpeech.TAG, "Connected to " + name);
                if (mOnSetupConnectionAsyncTask != null) {
                    mOnSetupConnectionAsyncTask.cancel(false);
                }
                mService = ITextToSpeechService.Stub.asInterface(service);
                mServiceConnection = this;
                mEstablished = false;
                mOnSetupConnectionAsyncTask = new android.speech.tts.TextToSpeech.Connection.SetupConnectionAsyncTask(name);
                mOnSetupConnectionAsyncTask.execute();
            }
        }

        public android.os.IBinder getCallerIdentity() {
            return mCallback;
        }

        /**
         * Clear connection related fields and cancel mOnServiceConnectedAsyncTask if set.
         *
         * @return true if we cancel mOnSetupConnectionAsyncTask in progress.
         */
        private boolean clearServiceConnection() {
            synchronized(mStartLock) {
                boolean result = false;
                if (mOnSetupConnectionAsyncTask != null) {
                    result = mOnSetupConnectionAsyncTask.cancel(false);
                    mOnSetupConnectionAsyncTask = null;
                }
                mService = null;
                // If this is the active connection, clear it
                if (mServiceConnection == this) {
                    mServiceConnection = null;
                }
                return result;
            }
        }

        @java.lang.Override
        public void onServiceDisconnected(android.content.ComponentName name) {
            android.util.Log.i(android.speech.tts.TextToSpeech.TAG, "Asked to disconnect from " + name);
            if (clearServiceConnection()) {
                /* We need to protect against a rare case where engine
                dies just after successful connection - and we process onServiceDisconnected
                before OnServiceConnectedAsyncTask.onPostExecute. onServiceDisconnected cancels
                OnServiceConnectedAsyncTask.onPostExecute and we don't call dispatchOnInit
                with ERROR as argument.
                 */
                dispatchOnInit(android.speech.tts.TextToSpeech.ERROR);
            }
        }

        public void disconnect() {
            mContext.unbindService(this);
            clearServiceConnection();
        }

        public boolean isEstablished() {
            return (mService != null) && mEstablished;
        }

        public <R> R runAction(android.speech.tts.TextToSpeech.Action<R> action, R errorResult, java.lang.String method, boolean reconnect, boolean onlyEstablishedConnection) {
            synchronized(mStartLock) {
                try {
                    if (mService == null) {
                        android.util.Log.w(android.speech.tts.TextToSpeech.TAG, method + " failed: not connected to TTS engine");
                        return errorResult;
                    }
                    if (onlyEstablishedConnection && (!isEstablished())) {
                        android.util.Log.w(android.speech.tts.TextToSpeech.TAG, method + " failed: TTS engine connection not fully set up");
                        return errorResult;
                    }
                    return action.run(mService);
                } catch (android.os.RemoteException ex) {
                    android.util.Log.e(android.speech.tts.TextToSpeech.TAG, method + " failed", ex);
                    if (reconnect) {
                        disconnect();
                        initTts();
                    }
                    return errorResult;
                }
            }
        }
    }

    private interface Action<R> {
        R run(android.speech.tts.ITextToSpeechService service) throws android.os.RemoteException;
    }

    /**
     * Information about an installed text-to-speech engine.
     *
     * @see TextToSpeech#getEngines
     */
    public static class EngineInfo {
        /**
         * Engine package name..
         */
        public java.lang.String name;

        /**
         * Localized label for the engine.
         */
        public java.lang.String label;

        /**
         * Icon for the engine.
         */
        public int icon;

        /**
         * Whether this engine is a part of the system
         * image.
         *
         * @unknown 
         */
        public boolean system;

        /**
         * The priority the engine declares for the the intent filter
         * {@code android.intent.action.TTS_SERVICE}
         *
         * @unknown 
         */
        public int priority;

        @java.lang.Override
        public java.lang.String toString() {
            return ("EngineInfo{name=" + name) + "}";
        }
    }

    /**
     * Limit of length of input string passed to speak and synthesizeToFile.
     *
     * @see #speak
     * @see #synthesizeToFile
     */
    public static int getMaxSpeechInputLength() {
        return 4000;
    }
}

