/**
 * Copyright (C) 2011 The Android Open Source Project
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
 * Abstract base class for TTS engine implementations. The following methods
 * need to be implemented:
 * <ul>
 * <li>{@link #onIsLanguageAvailable}</li>
 * <li>{@link #onLoadLanguage}</li>
 * <li>{@link #onGetLanguage}</li>
 * <li>{@link #onSynthesizeText}</li>
 * <li>{@link #onStop}</li>
 * </ul>
 * The first three deal primarily with language management, and are used to
 * query the engine for it's support for a given language and indicate to it
 * that requests in a given language are imminent.
 *
 * {@link #onSynthesizeText} is central to the engine implementation. The
 * implementation should synthesize text as per the request parameters and
 * return synthesized data via the supplied callback. This class and its helpers
 * will then consume that data, which might mean queuing it for playback or writing
 * it to a file or similar. All calls to this method will be on a single thread,
 * which will be different from the main thread of the service. Synthesis must be
 * synchronous which means the engine must NOT hold on to the callback or call any
 * methods on it after the method returns.
 *
 * {@link #onStop} tells the engine that it should stop
 * all ongoing synthesis, if any. Any pending data from the current synthesis
 * will be discarded.
 *
 * {@link #onGetLanguage} is not required as of JELLYBEAN_MR2 (API 18) and later, it is only
 * called on earlier versions of Android.
 *
 * API Level 20 adds support for Voice objects. Voices are an abstraction that allow the TTS
 * service to expose multiple backends for a single locale. Each one of them can have a different
 * features set. In order to fully take advantage of voices, an engine should implement
 * the following methods:
 * <ul>
 * <li>{@link #onGetVoices()}</li>
 * <li>{@link #onIsValidVoiceName(String)}</li>
 * <li>{@link #onLoadVoice(String)}</li>
 * <li>{@link #onGetDefaultVoiceNameFor(String, String, String)}</li>
 * </ul>
 * The first three methods are siblings of the {@link #onGetLanguage},
 * {@link #onIsLanguageAvailable} and {@link #onLoadLanguage} methods. The last one,
 * {@link #onGetDefaultVoiceNameFor(String, String, String)} is a link between locale and voice
 * based methods. Since API level 21 {@link TextToSpeech#setLanguage} is implemented by
 * calling {@link TextToSpeech#setVoice} with the voice returned by
 * {@link #onGetDefaultVoiceNameFor(String, String, String)}.
 *
 * If the client uses a voice instead of a locale, {@link SynthesisRequest} will contain the
 * requested voice name.
 *
 * The default implementations of Voice-related methods implement them using the
 * pre-existing locale-based implementation.
 */
public abstract class TextToSpeechService extends android.app.Service {
    private static final boolean DBG = false;

    private static final java.lang.String TAG = "TextToSpeechService";

    private static final java.lang.String SYNTH_THREAD_NAME = "SynthThread";

    private android.speech.tts.TextToSpeechService.SynthHandler mSynthHandler;

    // A thread and it's associated handler for playing back any audio
    // associated with this TTS engine. Will handle all requests except synthesis
    // to file requests, which occur on the synthesis thread.
    @android.annotation.NonNull
    private android.speech.tts.AudioPlaybackHandler mAudioPlaybackHandler;

    private android.speech.tts.TtsEngines mEngineHelper;

    private android.speech.tts.TextToSpeechService.CallbackMap mCallbacks;

    private java.lang.String mPackageName;

    private final java.lang.Object mVoicesInfoLock = new java.lang.Object();

    @java.lang.Override
    public void onCreate() {
        if (android.speech.tts.TextToSpeechService.DBG)
            android.util.Log.d(android.speech.tts.TextToSpeechService.TAG, "onCreate()");

        super.onCreate();
        android.speech.tts.TextToSpeechService.SynthThread synthThread = new android.speech.tts.TextToSpeechService.SynthThread();
        synthThread.start();
        mSynthHandler = new android.speech.tts.TextToSpeechService.SynthHandler(synthThread.getLooper());
        mAudioPlaybackHandler = new android.speech.tts.AudioPlaybackHandler();
        mAudioPlaybackHandler.start();
        mEngineHelper = new android.speech.tts.TtsEngines(this);
        mCallbacks = new android.speech.tts.TextToSpeechService.CallbackMap();
        mPackageName = getApplicationInfo().packageName;
        java.lang.String[] defaultLocale = getSettingsLocale();
        // Load default language
        onLoadLanguage(defaultLocale[0], defaultLocale[1], defaultLocale[2]);
    }

    @java.lang.Override
    public void onDestroy() {
        if (android.speech.tts.TextToSpeechService.DBG)
            android.util.Log.d(android.speech.tts.TextToSpeechService.TAG, "onDestroy()");

        // Tell the synthesizer to stop
        mSynthHandler.quit();
        // Tell the audio playback thread to stop.
        mAudioPlaybackHandler.quit();
        // Unregister all callbacks.
        mCallbacks.kill();
        super.onDestroy();
    }

    /**
     * Checks whether the engine supports a given language.
     *
     * Can be called on multiple threads.
     *
     * Its return values HAVE to be consistent with onLoadLanguage.
     *
     * @param lang
     * 		ISO-3 language code.
     * @param country
     * 		ISO-3 country code. May be empty or null.
     * @param variant
     * 		Language variant. May be empty or null.
     * @return Code indicating the support status for the locale.
    One of {@link TextToSpeech#LANG_AVAILABLE},
    {@link TextToSpeech#LANG_COUNTRY_AVAILABLE},
    {@link TextToSpeech#LANG_COUNTRY_VAR_AVAILABLE},
    {@link TextToSpeech#LANG_MISSING_DATA}
    {@link TextToSpeech#LANG_NOT_SUPPORTED}.
     */
    protected abstract int onIsLanguageAvailable(java.lang.String lang, java.lang.String country, java.lang.String variant);

    /**
     * Returns the language, country and variant currently being used by the TTS engine.
     *
     * This method will be called only on Android 4.2 and before (API <= 17). In later versions
     * this method is not called by the Android TTS framework.
     *
     * Can be called on multiple threads.
     *
     * @return A 3-element array, containing language (ISO 3-letter code),
    country (ISO 3-letter code) and variant used by the engine.
    The country and variant may be {@code ""}. If country is empty, then variant must
    be empty too.
     * @see Locale#getISO3Language()
     * @see Locale#getISO3Country()
     * @see Locale#getVariant()
     */
    protected abstract java.lang.String[] onGetLanguage();

    /**
     * Notifies the engine that it should load a speech synthesis language. There is no guarantee
     * that this method is always called before the language is used for synthesis. It is merely
     * a hint to the engine that it will probably get some synthesis requests for this language
     * at some point in the future.
     *
     * Can be called on multiple threads.
     * In <= Android 4.2 (<= API 17) can be called on main and service binder threads.
     * In > Android 4.2 (> API 17) can be called on main and synthesis threads.
     *
     * @param lang
     * 		ISO-3 language code.
     * @param country
     * 		ISO-3 country code. May be empty or null.
     * @param variant
     * 		Language variant. May be empty or null.
     * @return Code indicating the support status for the locale.
    One of {@link TextToSpeech#LANG_AVAILABLE},
    {@link TextToSpeech#LANG_COUNTRY_AVAILABLE},
    {@link TextToSpeech#LANG_COUNTRY_VAR_AVAILABLE},
    {@link TextToSpeech#LANG_MISSING_DATA}
    {@link TextToSpeech#LANG_NOT_SUPPORTED}.
     */
    protected abstract int onLoadLanguage(java.lang.String lang, java.lang.String country, java.lang.String variant);

    /**
     * Notifies the service that it should stop any in-progress speech synthesis.
     * This method can be called even if no speech synthesis is currently in progress.
     *
     * Can be called on multiple threads, but not on the synthesis thread.
     */
    protected abstract void onStop();

    /**
     * Tells the service to synthesize speech from the given text. This method
     * should block until the synthesis is finished. Used for requests from V1
     * clients ({@link android.speech.tts.TextToSpeech}). Called on the synthesis
     * thread.
     *
     * @param request
     * 		The synthesis request.
     * @param callback
     * 		The callback that the engine must use to make data
     * 		available for playback or for writing to a file.
     */
    protected abstract void onSynthesizeText(android.speech.tts.SynthesisRequest request, android.speech.tts.SynthesisCallback callback);

    /**
     * Queries the service for a set of features supported for a given language.
     *
     * Can be called on multiple threads.
     *
     * @param lang
     * 		ISO-3 language code.
     * @param country
     * 		ISO-3 country code. May be empty or null.
     * @param variant
     * 		Language variant. May be empty or null.
     * @return A list of features supported for the given language.
     */
    protected java.util.Set<java.lang.String> onGetFeaturesForLanguage(java.lang.String lang, java.lang.String country, java.lang.String variant) {
        return new java.util.HashSet<java.lang.String>();
    }

    private int getExpectedLanguageAvailableStatus(java.util.Locale locale) {
        int expectedStatus = android.speech.tts.TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE;
        if (locale.getVariant().isEmpty()) {
            if (locale.getCountry().isEmpty()) {
                expectedStatus = android.speech.tts.TextToSpeech.LANG_AVAILABLE;
            } else {
                expectedStatus = android.speech.tts.TextToSpeech.LANG_COUNTRY_AVAILABLE;
            }
        }
        return expectedStatus;
    }

    /**
     * Queries the service for a set of supported voices.
     *
     * Can be called on multiple threads.
     *
     * The default implementation tries to enumerate all available locales, pass them to
     * {@link #onIsLanguageAvailable(String, String, String)} and create Voice instances (using
     * the locale's BCP-47 language tag as the voice name) for the ones that are supported.
     * Note, that this implementation is suitable only for engines that don't have multiple voices
     * for a single locale. Also, this implementation won't work with Locales not listed in the
     * set returned by the {@link Locale#getAvailableLocales()} method.
     *
     * @return A list of voices supported.
     */
    public java.util.List<android.speech.tts.Voice> onGetVoices() {
        // Enumerate all locales and check if they are available
        java.util.ArrayList<android.speech.tts.Voice> voices = new java.util.ArrayList<android.speech.tts.Voice>();
        for (java.util.Locale locale : java.util.Locale.getAvailableLocales()) {
            int expectedStatus = getExpectedLanguageAvailableStatus(locale);
            try {
                int localeStatus = onIsLanguageAvailable(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant());
                if (localeStatus != expectedStatus) {
                    continue;
                }
            } catch (java.util.MissingResourceException e) {
                // Ignore locale without iso 3 codes
                continue;
            }
            java.util.Set<java.lang.String> features = onGetFeaturesForLanguage(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant());
            java.lang.String voiceName = onGetDefaultVoiceNameFor(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant());
            voices.add(new android.speech.tts.Voice(voiceName, locale, android.speech.tts.Voice.QUALITY_NORMAL, android.speech.tts.Voice.LATENCY_NORMAL, false, features));
        }
        return voices;
    }

    /**
     * Return a name of the default voice for a given locale.
     *
     * This method provides a mapping between locales and available voices. This method is
     * used in {@link TextToSpeech#setLanguage}, which calls this method and then calls
     * {@link TextToSpeech#setVoice} with the voice returned by this method.
     *
     * Also, it's used by {@link TextToSpeech#getDefaultVoice()} to find a default voice for
     * the default locale.
     *
     * @param lang
     * 		ISO-3 language code.
     * @param country
     * 		ISO-3 country code. May be empty or null.
     * @param variant
     * 		Language variant. May be empty or null.
     * @return A name of the default voice for a given locale.
     */
    public java.lang.String onGetDefaultVoiceNameFor(java.lang.String lang, java.lang.String country, java.lang.String variant) {
        int localeStatus = onIsLanguageAvailable(lang, country, variant);
        java.util.Locale iso3Locale = null;
        switch (localeStatus) {
            case android.speech.tts.TextToSpeech.LANG_AVAILABLE :
                iso3Locale = new java.util.Locale(lang);
                break;
            case android.speech.tts.TextToSpeech.LANG_COUNTRY_AVAILABLE :
                iso3Locale = new java.util.Locale(lang, country);
                break;
            case android.speech.tts.TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE :
                iso3Locale = new java.util.Locale(lang, country, variant);
                break;
            default :
                return null;
        }
        java.util.Locale properLocale = android.speech.tts.TtsEngines.normalizeTTSLocale(iso3Locale);
        java.lang.String voiceName = properLocale.toLanguageTag();
        if (onIsValidVoiceName(voiceName) == android.speech.tts.TextToSpeech.SUCCESS) {
            return voiceName;
        } else {
            return null;
        }
    }

    /**
     * Notifies the engine that it should load a speech synthesis voice. There is no guarantee
     * that this method is always called before the voice is used for synthesis. It is merely
     * a hint to the engine that it will probably get some synthesis requests for this voice
     * at some point in the future.
     *
     * Will be called only on synthesis thread.
     *
     * The default implementation creates a Locale from the voice name (by interpreting the name as
     * a BCP-47 tag for the locale), and passes it to
     * {@link #onLoadLanguage(String, String, String)}.
     *
     * @param voiceName
     * 		Name of the voice.
     * @return {@link TextToSpeech#ERROR} or {@link TextToSpeech#SUCCESS}.
     */
    public int onLoadVoice(java.lang.String voiceName) {
        java.util.Locale locale = java.util.Locale.forLanguageTag(voiceName);
        if (locale == null) {
            return android.speech.tts.TextToSpeech.ERROR;
        }
        int expectedStatus = getExpectedLanguageAvailableStatus(locale);
        try {
            int localeStatus = onIsLanguageAvailable(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant());
            if (localeStatus != expectedStatus) {
                return android.speech.tts.TextToSpeech.ERROR;
            }
            onLoadLanguage(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant());
            return android.speech.tts.TextToSpeech.SUCCESS;
        } catch (java.util.MissingResourceException e) {
            return android.speech.tts.TextToSpeech.ERROR;
        }
    }

    /**
     * Checks whether the engine supports a voice with a given name.
     *
     * Can be called on multiple threads.
     *
     * The default implementation treats the voice name as a language tag, creating a Locale from
     * the voice name, and passes it to {@link #onIsLanguageAvailable(String, String, String)}.
     *
     * @param voiceName
     * 		Name of the voice.
     * @return {@link TextToSpeech#ERROR} or {@link TextToSpeech#SUCCESS}.
     */
    public int onIsValidVoiceName(java.lang.String voiceName) {
        java.util.Locale locale = java.util.Locale.forLanguageTag(voiceName);
        if (locale == null) {
            return android.speech.tts.TextToSpeech.ERROR;
        }
        int expectedStatus = getExpectedLanguageAvailableStatus(locale);
        try {
            int localeStatus = onIsLanguageAvailable(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant());
            if (localeStatus != expectedStatus) {
                return android.speech.tts.TextToSpeech.ERROR;
            }
            return android.speech.tts.TextToSpeech.SUCCESS;
        } catch (java.util.MissingResourceException e) {
            return android.speech.tts.TextToSpeech.ERROR;
        }
    }

    private int getDefaultSpeechRate() {
        return getSecureSettingInt(android.provider.Settings.Secure.TTS_DEFAULT_RATE, android.speech.tts.TextToSpeech.Engine.DEFAULT_RATE);
    }

    private java.lang.String[] getSettingsLocale() {
        final java.util.Locale locale = mEngineHelper.getLocalePrefForEngine(mPackageName);
        return android.speech.tts.TtsEngines.toOldLocaleStringFormat(locale);
    }

    private int getSecureSettingInt(java.lang.String name, int defaultValue) {
        return android.provider.Settings.Secure.getInt(getContentResolver(), name, defaultValue);
    }

    /**
     * Synthesizer thread. This thread is used to run {@link SynthHandler}.
     */
    private class SynthThread extends android.os.HandlerThread implements android.os.MessageQueue.IdleHandler {
        private boolean mFirstIdle = true;

        public SynthThread() {
            super(android.speech.tts.TextToSpeechService.SYNTH_THREAD_NAME, android.os.Process.THREAD_PRIORITY_DEFAULT);
        }

        @java.lang.Override
        protected void onLooperPrepared() {
            getLooper().getQueue().addIdleHandler(this);
        }

        @java.lang.Override
        public boolean queueIdle() {
            if (mFirstIdle) {
                mFirstIdle = false;
            } else {
                broadcastTtsQueueProcessingCompleted();
            }
            return true;
        }

        private void broadcastTtsQueueProcessingCompleted() {
            android.content.Intent i = new android.content.Intent(android.speech.tts.TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
            if (android.speech.tts.TextToSpeechService.DBG)
                android.util.Log.d(android.speech.tts.TextToSpeechService.TAG, "Broadcasting: " + i);

            sendBroadcast(i);
        }
    }

    private class SynthHandler extends android.os.Handler {
        private android.speech.tts.TextToSpeechService.SpeechItem mCurrentSpeechItem = null;

        // When a message with QUEUE_FLUSH arrives we add the caller identity to the List and when a
        // message with QUEUE_DESTROY arrives we increment mFlushAll. Then a message is added to the
        // handler queue that removes the caller identify from the list and decrements the mFlushAll
        // counter. This is so that when a message is processed and the caller identity is in the
        // list or mFlushAll is not zero, we know that the message should be flushed.
        // It's important that mFlushedObjects is a List and not a Set, and that mFlushAll is an
        // int and not a bool. This is because when multiple messages arrive with QUEUE_FLUSH or
        // QUEUE_DESTROY, we want to keep flushing messages until we arrive at the last QUEUE_FLUSH
        // or QUEUE_DESTROY message.
        private java.util.List<java.lang.Object> mFlushedObjects = new java.util.ArrayList<>();

        private int mFlushAll = 0;

        public SynthHandler(android.os.Looper looper) {
            super(looper);
        }

        private void startFlushingSpeechItems(java.lang.Object callerIdentity) {
            synchronized(mFlushedObjects) {
                if (callerIdentity == null) {
                    mFlushAll += 1;
                } else {
                    mFlushedObjects.add(callerIdentity);
                }
            }
        }

        private void endFlushingSpeechItems(java.lang.Object callerIdentity) {
            synchronized(mFlushedObjects) {
                if (callerIdentity == null) {
                    mFlushAll -= 1;
                } else {
                    mFlushedObjects.remove(callerIdentity);
                }
            }
        }

        private boolean isFlushed(android.speech.tts.TextToSpeechService.SpeechItem speechItem) {
            synchronized(mFlushedObjects) {
                return (mFlushAll > 0) || mFlushedObjects.contains(speechItem.getCallerIdentity());
            }
        }

        private synchronized android.speech.tts.TextToSpeechService.SpeechItem getCurrentSpeechItem() {
            return mCurrentSpeechItem;
        }

        private synchronized android.speech.tts.TextToSpeechService.SpeechItem setCurrentSpeechItem(android.speech.tts.TextToSpeechService.SpeechItem speechItem) {
            android.speech.tts.TextToSpeechService.SpeechItem old = mCurrentSpeechItem;
            mCurrentSpeechItem = speechItem;
            return old;
        }

        private synchronized android.speech.tts.TextToSpeechService.SpeechItem maybeRemoveCurrentSpeechItem(java.lang.Object callerIdentity) {
            if ((mCurrentSpeechItem != null) && (mCurrentSpeechItem.getCallerIdentity() == callerIdentity)) {
                android.speech.tts.TextToSpeechService.SpeechItem current = mCurrentSpeechItem;
                mCurrentSpeechItem = null;
                return current;
            }
            return null;
        }

        public boolean isSpeaking() {
            return getCurrentSpeechItem() != null;
        }

        public void quit() {
            // Don't process any more speech items
            getLooper().quit();
            // Stop the current speech item
            android.speech.tts.TextToSpeechService.SpeechItem current = setCurrentSpeechItem(null);
            if (current != null) {
                current.stop();
            }
            // The AudioPlaybackHandler will be destroyed by the caller.
        }

        /**
         * Adds a speech item to the queue.
         *
         * Called on a service binder thread.
         */
        public int enqueueSpeechItem(int queueMode, final android.speech.tts.TextToSpeechService.SpeechItem speechItem) {
            android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher utterenceProgress = null;
            if (speechItem instanceof android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher) {
                utterenceProgress = ((android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher) (speechItem));
            }
            if (!speechItem.isValid()) {
                if (utterenceProgress != null) {
                    utterenceProgress.dispatchOnError(android.speech.tts.TextToSpeech.ERROR_INVALID_REQUEST);
                }
                return android.speech.tts.TextToSpeech.ERROR;
            }
            if (queueMode == android.speech.tts.TextToSpeech.QUEUE_FLUSH) {
                stopForApp(speechItem.getCallerIdentity());
            } else
                if (queueMode == android.speech.tts.TextToSpeech.QUEUE_DESTROY) {
                    stopAll();
                }

            java.lang.Runnable runnable = new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    if (isFlushed(speechItem)) {
                        speechItem.stop();
                    } else {
                        setCurrentSpeechItem(speechItem);
                        speechItem.play();
                        setCurrentSpeechItem(null);
                    }
                }
            };
            android.os.Message msg = android.os.Message.obtain(this, runnable);
            // The obj is used to remove all callbacks from the given app in
            // stopForApp(String).
            // 
            // Note that this string is interned, so the == comparison works.
            msg.obj = speechItem.getCallerIdentity();
            if (sendMessage(msg)) {
                return android.speech.tts.TextToSpeech.SUCCESS;
            } else {
                android.util.Log.w(android.speech.tts.TextToSpeechService.TAG, "SynthThread has quit");
                if (utterenceProgress != null) {
                    utterenceProgress.dispatchOnError(android.speech.tts.TextToSpeech.ERROR_SERVICE);
                }
                return android.speech.tts.TextToSpeech.ERROR;
            }
        }

        /**
         * Stops all speech output and removes any utterances still in the queue for
         * the calling app.
         *
         * Called on a service binder thread.
         */
        public int stopForApp(final java.lang.Object callerIdentity) {
            if (callerIdentity == null) {
                return android.speech.tts.TextToSpeech.ERROR;
            }
            // Flush pending messages from callerIdentity
            startFlushingSpeechItems(callerIdentity);
            // This stops writing data to the file / or publishing
            // items to the audio playback handler.
            // 
            // Note that the current speech item must be removed only if it
            // belongs to the callingApp, else the item will be "orphaned" and
            // not stopped correctly if a stop request comes along for the item
            // from the app it belongs to.
            android.speech.tts.TextToSpeechService.SpeechItem current = maybeRemoveCurrentSpeechItem(callerIdentity);
            if (current != null) {
                current.stop();
            }
            // Remove any enqueued audio too.
            mAudioPlaybackHandler.stopForApp(callerIdentity);
            // Stop flushing pending messages
            java.lang.Runnable runnable = new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    endFlushingSpeechItems(callerIdentity);
                }
            };
            sendMessage(android.os.Message.obtain(this, runnable));
            return android.speech.tts.TextToSpeech.SUCCESS;
        }

        public int stopAll() {
            // Order to flush pending messages
            startFlushingSpeechItems(null);
            // Stop the current speech item unconditionally .
            android.speech.tts.TextToSpeechService.SpeechItem current = setCurrentSpeechItem(null);
            if (current != null) {
                current.stop();
            }
            // Remove all pending playback as well.
            mAudioPlaybackHandler.stop();
            // Message to stop flushing pending messages
            java.lang.Runnable runnable = new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    endFlushingSpeechItems(null);
                }
            };
            sendMessage(android.os.Message.obtain(this, runnable));
            return android.speech.tts.TextToSpeech.SUCCESS;
        }
    }

    interface UtteranceProgressDispatcher {
        public void dispatchOnStop();

        public void dispatchOnSuccess();

        public void dispatchOnStart();

        public void dispatchOnError(int errorCode);

        public void dispatchOnBeginSynthesis(int sampleRateInHz, int audioFormat, int channelCount);

        public void dispatchOnAudioAvailable(byte[] audio);
    }

    /**
     * Set of parameters affecting audio output.
     */
    static class AudioOutputParams {
        /**
         * Audio session identifier. May be used to associate audio playback with one of the
         * {@link android.media.audiofx.AudioEffect} objects. If not specified by client,
         * it should be equal to {@link AudioManager#AUDIO_SESSION_ID_GENERATE}.
         */
        public final int mSessionId;

        /**
         * Volume, in the range [0.0f, 1.0f]. The default value is
         * {@link TextToSpeech.Engine#DEFAULT_VOLUME} (1.0f).
         */
        public final float mVolume;

        /**
         * Left/right position of the audio, in the range [-1.0f, 1.0f].
         * The default value is {@link TextToSpeech.Engine#DEFAULT_PAN} (0.0f).
         */
        public final float mPan;

        /**
         * Audio attributes, set by {@link TextToSpeech#setAudioAttributes}
         * or created from the value of {@link TextToSpeech.Engine#KEY_PARAM_STREAM}.
         */
        public final android.media.AudioAttributes mAudioAttributes;

        /**
         * Create AudioOutputParams with default values
         */
        AudioOutputParams() {
            mSessionId = android.media.AudioManager.AUDIO_SESSION_ID_GENERATE;
            mVolume = android.speech.tts.TextToSpeech.Engine.DEFAULT_VOLUME;
            mPan = android.speech.tts.TextToSpeech.Engine.DEFAULT_PAN;
            mAudioAttributes = null;
        }

        AudioOutputParams(int sessionId, float volume, float pan, android.media.AudioAttributes audioAttributes) {
            mSessionId = sessionId;
            mVolume = volume;
            mPan = pan;
            mAudioAttributes = audioAttributes;
        }

        /**
         * Create AudioOutputParams from A {@link SynthesisRequest#getParams()} bundle
         */
        static android.speech.tts.TextToSpeechService.AudioOutputParams createFromV1ParamsBundle(android.os.Bundle paramsBundle, boolean isSpeech) {
            if (paramsBundle == null) {
                return new android.speech.tts.TextToSpeechService.AudioOutputParams();
            }
            android.media.AudioAttributes audioAttributes = ((android.media.AudioAttributes) (paramsBundle.getParcelable(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_AUDIO_ATTRIBUTES)));
            if (audioAttributes == null) {
                int streamType = paramsBundle.getInt(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_STREAM, android.speech.tts.TextToSpeech.Engine.DEFAULT_STREAM);
                audioAttributes = new android.media.AudioAttributes.Builder().setLegacyStreamType(streamType).setContentType(isSpeech ? android.media.AudioAttributes.CONTENT_TYPE_SPEECH : android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            }
            return new android.speech.tts.TextToSpeechService.AudioOutputParams(paramsBundle.getInt(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_SESSION_ID, android.media.AudioManager.AUDIO_SESSION_ID_GENERATE), paramsBundle.getFloat(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_VOLUME, android.speech.tts.TextToSpeech.Engine.DEFAULT_VOLUME), paramsBundle.getFloat(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_PAN, android.speech.tts.TextToSpeech.Engine.DEFAULT_PAN), audioAttributes);
        }
    }

    /**
     * An item in the synth thread queue.
     */
    private abstract class SpeechItem {
        private final java.lang.Object mCallerIdentity;

        private final int mCallerUid;

        private final int mCallerPid;

        private boolean mStarted = false;

        private boolean mStopped = false;

        public SpeechItem(java.lang.Object caller, int callerUid, int callerPid) {
            mCallerIdentity = caller;
            mCallerUid = callerUid;
            mCallerPid = callerPid;
        }

        public java.lang.Object getCallerIdentity() {
            return mCallerIdentity;
        }

        public int getCallerUid() {
            return mCallerUid;
        }

        public int getCallerPid() {
            return mCallerPid;
        }

        /**
         * Checker whether the item is valid. If this method returns false, the item should not
         * be played.
         */
        public abstract boolean isValid();

        /**
         * Plays the speech item. Blocks until playback is finished.
         * Must not be called more than once.
         *
         * Only called on the synthesis thread.
         */
        public void play() {
            synchronized(this) {
                if (mStarted) {
                    throw new java.lang.IllegalStateException("play() called twice");
                }
                mStarted = true;
            }
            playImpl();
        }

        protected abstract void playImpl();

        /**
         * Stops the speech item.
         * Must not be called more than once.
         *
         * Can be called on multiple threads,  but not on the synthesis thread.
         */
        public void stop() {
            synchronized(this) {
                if (mStopped) {
                    throw new java.lang.IllegalStateException("stop() called twice");
                }
                mStopped = true;
            }
            stopImpl();
        }

        protected abstract void stopImpl();

        protected synchronized boolean isStopped() {
            return mStopped;
        }

        protected synchronized boolean isStarted() {
            return mStarted;
        }
    }

    /**
     * An item in the synth thread queue that process utterance (and call back to client about
     * progress).
     */
    private abstract class UtteranceSpeechItem extends android.speech.tts.TextToSpeechService.SpeechItem implements android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher {
        public UtteranceSpeechItem(java.lang.Object caller, int callerUid, int callerPid) {
            super(caller, callerUid, callerPid);
        }

        @java.lang.Override
        public void dispatchOnSuccess() {
            final java.lang.String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                mCallbacks.dispatchOnSuccess(getCallerIdentity(), utteranceId);
            }
        }

        @java.lang.Override
        public void dispatchOnStop() {
            final java.lang.String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                mCallbacks.dispatchOnStop(getCallerIdentity(), utteranceId, isStarted());
            }
        }

        @java.lang.Override
        public void dispatchOnStart() {
            final java.lang.String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                mCallbacks.dispatchOnStart(getCallerIdentity(), utteranceId);
            }
        }

        @java.lang.Override
        public void dispatchOnError(int errorCode) {
            final java.lang.String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                mCallbacks.dispatchOnError(getCallerIdentity(), utteranceId, errorCode);
            }
        }

        @java.lang.Override
        public void dispatchOnBeginSynthesis(int sampleRateInHz, int audioFormat, int channelCount) {
            final java.lang.String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                mCallbacks.dispatchOnBeginSynthesis(getCallerIdentity(), utteranceId, sampleRateInHz, audioFormat, channelCount);
            }
        }

        @java.lang.Override
        public void dispatchOnAudioAvailable(byte[] audio) {
            final java.lang.String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                mCallbacks.dispatchOnAudioAvailable(getCallerIdentity(), utteranceId, audio);
            }
        }

        public abstract java.lang.String getUtteranceId();

        java.lang.String getStringParam(android.os.Bundle params, java.lang.String key, java.lang.String defaultValue) {
            return params == null ? defaultValue : params.getString(key, defaultValue);
        }

        int getIntParam(android.os.Bundle params, java.lang.String key, int defaultValue) {
            return params == null ? defaultValue : params.getInt(key, defaultValue);
        }

        float getFloatParam(android.os.Bundle params, java.lang.String key, float defaultValue) {
            return params == null ? defaultValue : params.getFloat(key, defaultValue);
        }
    }

    /**
     * UtteranceSpeechItem for V1 API speech items. V1 API speech items keep
     * synthesis parameters in a single Bundle passed as parameter. This class
     * allow subclasses to access them conveniently.
     */
    private abstract class SpeechItemV1 extends android.speech.tts.TextToSpeechService.UtteranceSpeechItem {
        protected final android.os.Bundle mParams;

        protected final java.lang.String mUtteranceId;

        SpeechItemV1(java.lang.Object callerIdentity, int callerUid, int callerPid, android.os.Bundle params, java.lang.String utteranceId) {
            super(callerIdentity, callerUid, callerPid);
            mParams = params;
            mUtteranceId = utteranceId;
        }

        boolean hasLanguage() {
            return !android.text.TextUtils.isEmpty(getStringParam(mParams, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_LANGUAGE, null));
        }

        int getSpeechRate() {
            return getIntParam(mParams, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_RATE, getDefaultSpeechRate());
        }

        int getPitch() {
            return getIntParam(mParams, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_PITCH, android.speech.tts.TextToSpeech.Engine.DEFAULT_PITCH);
        }

        @java.lang.Override
        public java.lang.String getUtteranceId() {
            return mUtteranceId;
        }

        android.speech.tts.TextToSpeechService.AudioOutputParams getAudioParams() {
            return android.speech.tts.TextToSpeechService.AudioOutputParams.createFromV1ParamsBundle(mParams, true);
        }
    }

    class SynthesisSpeechItemV1 extends android.speech.tts.TextToSpeechService.SpeechItemV1 {
        // Never null.
        private final java.lang.CharSequence mText;

        private final android.speech.tts.SynthesisRequest mSynthesisRequest;

        private final java.lang.String[] mDefaultLocale;

        // Non null after synthesis has started, and all accesses
        // guarded by 'this'.
        private android.speech.tts.AbstractSynthesisCallback mSynthesisCallback;

        private final android.speech.tts.EventLoggerV1 mEventLogger;

        private final int mCallerUid;

        public SynthesisSpeechItemV1(java.lang.Object callerIdentity, int callerUid, int callerPid, android.os.Bundle params, java.lang.String utteranceId, java.lang.CharSequence text) {
            super(callerIdentity, callerUid, callerPid, params, utteranceId);
            mText = text;
            mCallerUid = callerUid;
            mSynthesisRequest = new android.speech.tts.SynthesisRequest(mText, mParams);
            mDefaultLocale = getSettingsLocale();
            setRequestParams(mSynthesisRequest);
            mEventLogger = new android.speech.tts.EventLoggerV1(mSynthesisRequest, callerUid, callerPid, mPackageName);
        }

        public java.lang.CharSequence getText() {
            return mText;
        }

        @java.lang.Override
        public boolean isValid() {
            if (mText == null) {
                android.util.Log.e(android.speech.tts.TextToSpeechService.TAG, "null synthesis text");
                return false;
            }
            if (mText.length() >= android.speech.tts.TextToSpeech.getMaxSpeechInputLength()) {
                android.util.Log.w(android.speech.tts.TextToSpeechService.TAG, ("Text too long: " + mText.length()) + " chars");
                return false;
            }
            return true;
        }

        @java.lang.Override
        protected void playImpl() {
            android.speech.tts.AbstractSynthesisCallback synthesisCallback;
            mEventLogger.onRequestProcessingStart();
            synchronized(this) {
                // stop() might have been called before we enter this
                // synchronized block.
                if (isStopped()) {
                    return;
                }
                mSynthesisCallback = createSynthesisCallback();
                synthesisCallback = mSynthesisCallback;
            }
            android.speech.tts.TextToSpeechService.this.onSynthesizeText(mSynthesisRequest, synthesisCallback);
            // Fix for case where client called .start() & .error(), but did not called .done()
            if (synthesisCallback.hasStarted() && (!synthesisCallback.hasFinished())) {
                synthesisCallback.done();
            }
        }

        protected android.speech.tts.AbstractSynthesisCallback createSynthesisCallback() {
            return new android.speech.tts.PlaybackSynthesisCallback(getAudioParams(), mAudioPlaybackHandler, this, getCallerIdentity(), mEventLogger, false);
        }

        private void setRequestParams(android.speech.tts.SynthesisRequest request) {
            java.lang.String voiceName = getVoiceName();
            request.setLanguage(getLanguage(), getCountry(), getVariant());
            if (!android.text.TextUtils.isEmpty(voiceName)) {
                request.setVoiceName(getVoiceName());
            }
            request.setSpeechRate(getSpeechRate());
            request.setCallerUid(mCallerUid);
            request.setPitch(getPitch());
        }

        @java.lang.Override
        protected void stopImpl() {
            android.speech.tts.AbstractSynthesisCallback synthesisCallback;
            synchronized(this) {
                synthesisCallback = mSynthesisCallback;
            }
            if (synthesisCallback != null) {
                // If the synthesis callback is null, it implies that we haven't
                // entered the synchronized(this) block in playImpl which in
                // turn implies that synthesis would not have started.
                synthesisCallback.stop();
                android.speech.tts.TextToSpeechService.this.onStop();
            } else {
                dispatchOnStop();
            }
        }

        private java.lang.String getCountry() {
            if (!hasLanguage())
                return mDefaultLocale[1];

            return getStringParam(mParams, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_COUNTRY, "");
        }

        private java.lang.String getVariant() {
            if (!hasLanguage())
                return mDefaultLocale[2];

            return getStringParam(mParams, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_VARIANT, "");
        }

        public java.lang.String getLanguage() {
            return getStringParam(mParams, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_LANGUAGE, mDefaultLocale[0]);
        }

        public java.lang.String getVoiceName() {
            return getStringParam(mParams, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_VOICE_NAME, "");
        }
    }

    private class SynthesisToFileOutputStreamSpeechItemV1 extends android.speech.tts.TextToSpeechService.SynthesisSpeechItemV1 {
        private final java.io.FileOutputStream mFileOutputStream;

        public SynthesisToFileOutputStreamSpeechItemV1(java.lang.Object callerIdentity, int callerUid, int callerPid, android.os.Bundle params, java.lang.String utteranceId, java.lang.CharSequence text, java.io.FileOutputStream fileOutputStream) {
            super(callerIdentity, callerUid, callerPid, params, utteranceId, text);
            mFileOutputStream = fileOutputStream;
        }

        @java.lang.Override
        protected android.speech.tts.AbstractSynthesisCallback createSynthesisCallback() {
            return new android.speech.tts.FileSynthesisCallback(mFileOutputStream.getChannel(), this, false);
        }

        @java.lang.Override
        protected void playImpl() {
            dispatchOnStart();
            super.playImpl();
            try {
                mFileOutputStream.close();
            } catch (java.io.IOException e) {
                android.util.Log.w(android.speech.tts.TextToSpeechService.TAG, "Failed to close output file", e);
            }
        }
    }

    private class AudioSpeechItemV1 extends android.speech.tts.TextToSpeechService.SpeechItemV1 {
        private final android.speech.tts.AudioPlaybackQueueItem mItem;

        public AudioSpeechItemV1(java.lang.Object callerIdentity, int callerUid, int callerPid, android.os.Bundle params, java.lang.String utteranceId, android.net.Uri uri) {
            super(callerIdentity, callerUid, callerPid, params, utteranceId);
            mItem = new android.speech.tts.AudioPlaybackQueueItem(this, getCallerIdentity(), android.speech.tts.TextToSpeechService.this, uri, getAudioParams());
        }

        @java.lang.Override
        public boolean isValid() {
            return true;
        }

        @java.lang.Override
        protected void playImpl() {
            mAudioPlaybackHandler.enqueue(mItem);
        }

        @java.lang.Override
        protected void stopImpl() {
            // Do nothing.
        }

        @java.lang.Override
        public java.lang.String getUtteranceId() {
            return getStringParam(mParams, android.speech.tts.TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, null);
        }

        @java.lang.Override
        android.speech.tts.TextToSpeechService.AudioOutputParams getAudioParams() {
            return android.speech.tts.TextToSpeechService.AudioOutputParams.createFromV1ParamsBundle(mParams, false);
        }
    }

    private class SilenceSpeechItem extends android.speech.tts.TextToSpeechService.UtteranceSpeechItem {
        private final long mDuration;

        private final java.lang.String mUtteranceId;

        public SilenceSpeechItem(java.lang.Object callerIdentity, int callerUid, int callerPid, java.lang.String utteranceId, long duration) {
            super(callerIdentity, callerUid, callerPid);
            mUtteranceId = utteranceId;
            mDuration = duration;
        }

        @java.lang.Override
        public boolean isValid() {
            return true;
        }

        @java.lang.Override
        protected void playImpl() {
            mAudioPlaybackHandler.enqueue(new android.speech.tts.SilencePlaybackQueueItem(this, getCallerIdentity(), mDuration));
        }

        @java.lang.Override
        protected void stopImpl() {
        }

        @java.lang.Override
        public java.lang.String getUtteranceId() {
            return mUtteranceId;
        }
    }

    /**
     * Call {@link TextToSpeechService#onLoadLanguage} on synth thread.
     */
    private class LoadLanguageItem extends android.speech.tts.TextToSpeechService.SpeechItem {
        private final java.lang.String mLanguage;

        private final java.lang.String mCountry;

        private final java.lang.String mVariant;

        public LoadLanguageItem(java.lang.Object callerIdentity, int callerUid, int callerPid, java.lang.String language, java.lang.String country, java.lang.String variant) {
            super(callerIdentity, callerUid, callerPid);
            mLanguage = language;
            mCountry = country;
            mVariant = variant;
        }

        @java.lang.Override
        public boolean isValid() {
            return true;
        }

        @java.lang.Override
        protected void playImpl() {
            android.speech.tts.TextToSpeechService.this.onLoadLanguage(mLanguage, mCountry, mVariant);
        }

        @java.lang.Override
        protected void stopImpl() {
            // No-op
        }
    }

    /**
     * Call {@link TextToSpeechService#onLoadLanguage} on synth thread.
     */
    private class LoadVoiceItem extends android.speech.tts.TextToSpeechService.SpeechItem {
        private final java.lang.String mVoiceName;

        public LoadVoiceItem(java.lang.Object callerIdentity, int callerUid, int callerPid, java.lang.String voiceName) {
            super(callerIdentity, callerUid, callerPid);
            mVoiceName = voiceName;
        }

        @java.lang.Override
        public boolean isValid() {
            return true;
        }

        @java.lang.Override
        protected void playImpl() {
            android.speech.tts.TextToSpeechService.this.onLoadVoice(mVoiceName);
        }

        @java.lang.Override
        protected void stopImpl() {
            // No-op
        }
    }

    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        if (android.speech.tts.TextToSpeech.Engine.INTENT_ACTION_TTS_SERVICE.equals(intent.getAction())) {
            return mBinder;
        }
        return null;
    }

    /**
     * Binder returned from {@code #onBind(Intent)}. The methods in this class can be
     * called called from several different threads.
     */
    // NOTE: All calls that are passed in a calling app are interned so that
    // they can be used as message objects (which are tested for equality using ==).
    private final ITextToSpeechService.Stub mBinder = new android.speech.tts.ITextToSpeechService.Stub() {
        @java.lang.Override
        public int speak(android.os.IBinder caller, java.lang.CharSequence text, int queueMode, android.os.Bundle params, java.lang.String utteranceId) {
            if (!checkNonNull(caller, text, params)) {
                return android.speech.tts.TextToSpeech.ERROR;
            }
            android.speech.tts.TextToSpeechService.SpeechItem item = new android.speech.tts.TextToSpeechService.SynthesisSpeechItemV1(caller, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), params, utteranceId, text);
            return mSynthHandler.enqueueSpeechItem(queueMode, item);
        }

        @java.lang.Override
        public int synthesizeToFileDescriptor(android.os.IBinder caller, java.lang.CharSequence text, android.os.ParcelFileDescriptor fileDescriptor, android.os.Bundle params, java.lang.String utteranceId) {
            if (!checkNonNull(caller, text, fileDescriptor, params)) {
                return android.speech.tts.TextToSpeech.ERROR;
            }
            // In test env, ParcelFileDescriptor instance may be EXACTLY the same
            // one that is used by client. And it will be closed by a client, thus
            // preventing us from writing anything to it.
            final android.os.ParcelFileDescriptor sameFileDescriptor = android.os.ParcelFileDescriptor.adoptFd(fileDescriptor.detachFd());
            android.speech.tts.TextToSpeechService.SpeechItem item = new android.speech.tts.TextToSpeechService.SynthesisToFileOutputStreamSpeechItemV1(caller, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), params, utteranceId, text, new android.os.ParcelFileDescriptor.AutoCloseOutputStream(sameFileDescriptor));
            return mSynthHandler.enqueueSpeechItem(android.speech.tts.TextToSpeech.QUEUE_ADD, item);
        }

        @java.lang.Override
        public int playAudio(android.os.IBinder caller, android.net.Uri audioUri, int queueMode, android.os.Bundle params, java.lang.String utteranceId) {
            if (!checkNonNull(caller, audioUri, params)) {
                return android.speech.tts.TextToSpeech.ERROR;
            }
            android.speech.tts.TextToSpeechService.SpeechItem item = new android.speech.tts.TextToSpeechService.AudioSpeechItemV1(caller, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), params, utteranceId, audioUri);
            return mSynthHandler.enqueueSpeechItem(queueMode, item);
        }

        @java.lang.Override
        public int playSilence(android.os.IBinder caller, long duration, int queueMode, java.lang.String utteranceId) {
            if (!checkNonNull(caller)) {
                return android.speech.tts.TextToSpeech.ERROR;
            }
            android.speech.tts.TextToSpeechService.SpeechItem item = new android.speech.tts.TextToSpeechService.SilenceSpeechItem(caller, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), utteranceId, duration);
            return mSynthHandler.enqueueSpeechItem(queueMode, item);
        }

        @java.lang.Override
        public boolean isSpeaking() {
            return mSynthHandler.isSpeaking() || mAudioPlaybackHandler.isSpeaking();
        }

        @java.lang.Override
        public int stop(android.os.IBinder caller) {
            if (!checkNonNull(caller)) {
                return android.speech.tts.TextToSpeech.ERROR;
            }
            return mSynthHandler.stopForApp(caller);
        }

        @java.lang.Override
        public java.lang.String[] getLanguage() {
            return onGetLanguage();
        }

        @java.lang.Override
        public java.lang.String[] getClientDefaultLanguage() {
            return getSettingsLocale();
        }

        /* If defaults are enforced, then no language is "available" except
        perhaps the default language selected by the user.
         */
        @java.lang.Override
        public int isLanguageAvailable(java.lang.String lang, java.lang.String country, java.lang.String variant) {
            if (!checkNonNull(lang)) {
                return android.speech.tts.TextToSpeech.ERROR;
            }
            return onIsLanguageAvailable(lang, country, variant);
        }

        @java.lang.Override
        public java.lang.String[] getFeaturesForLanguage(java.lang.String lang, java.lang.String country, java.lang.String variant) {
            java.util.Set<java.lang.String> features = onGetFeaturesForLanguage(lang, country, variant);
            java.lang.String[] featuresArray = null;
            if (features != null) {
                featuresArray = new java.lang.String[features.size()];
                features.toArray(featuresArray);
            } else {
                featuresArray = new java.lang.String[0];
            }
            return featuresArray;
        }

        /* There is no point loading a non default language if defaults
        are enforced.
         */
        @java.lang.Override
        public int loadLanguage(android.os.IBinder caller, java.lang.String lang, java.lang.String country, java.lang.String variant) {
            if (!checkNonNull(lang)) {
                return android.speech.tts.TextToSpeech.ERROR;
            }
            int retVal = onIsLanguageAvailable(lang, country, variant);
            if (((retVal == android.speech.tts.TextToSpeech.LANG_AVAILABLE) || (retVal == android.speech.tts.TextToSpeech.LANG_COUNTRY_AVAILABLE)) || (retVal == android.speech.tts.TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE)) {
                android.speech.tts.TextToSpeechService.SpeechItem item = new android.speech.tts.TextToSpeechService.LoadLanguageItem(caller, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), lang, country, variant);
                if (mSynthHandler.enqueueSpeechItem(android.speech.tts.TextToSpeech.QUEUE_ADD, item) != android.speech.tts.TextToSpeech.SUCCESS) {
                    return android.speech.tts.TextToSpeech.ERROR;
                }
            }
            return retVal;
        }

        @java.lang.Override
        public java.util.List<android.speech.tts.Voice> getVoices() {
            return onGetVoices();
        }

        @java.lang.Override
        public int loadVoice(android.os.IBinder caller, java.lang.String voiceName) {
            if (!checkNonNull(voiceName)) {
                return android.speech.tts.TextToSpeech.ERROR;
            }
            int retVal = onIsValidVoiceName(voiceName);
            if (retVal == android.speech.tts.TextToSpeech.SUCCESS) {
                android.speech.tts.TextToSpeechService.SpeechItem item = new android.speech.tts.TextToSpeechService.LoadVoiceItem(caller, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), voiceName);
                if (mSynthHandler.enqueueSpeechItem(android.speech.tts.TextToSpeech.QUEUE_ADD, item) != android.speech.tts.TextToSpeech.SUCCESS) {
                    return android.speech.tts.TextToSpeech.ERROR;
                }
            }
            return retVal;
        }

        public java.lang.String getDefaultVoiceNameFor(java.lang.String lang, java.lang.String country, java.lang.String variant) {
            if (!checkNonNull(lang)) {
                return null;
            }
            int retVal = onIsLanguageAvailable(lang, country, variant);
            if (((retVal == android.speech.tts.TextToSpeech.LANG_AVAILABLE) || (retVal == android.speech.tts.TextToSpeech.LANG_COUNTRY_AVAILABLE)) || (retVal == android.speech.tts.TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE)) {
                return onGetDefaultVoiceNameFor(lang, country, variant);
            } else {
                return null;
            }
        }

        @java.lang.Override
        public void setCallback(android.os.IBinder caller, android.speech.tts.ITextToSpeechCallback cb) {
            // Note that passing in a null callback is a valid use case.
            if (!checkNonNull(caller)) {
                return;
            }
            mCallbacks.setCallback(caller, cb);
        }

        private java.lang.String intern(java.lang.String in) {
            // The input parameter will be non null.
            return in.intern();
        }

        private boolean checkNonNull(java.lang.Object... args) {
            for (java.lang.Object o : args) {
                if (o == null)
                    return false;

            }
            return true;
        }
    };

    private class CallbackMap extends android.os.RemoteCallbackList<android.speech.tts.ITextToSpeechCallback> {
        private final java.util.HashMap<android.os.IBinder, android.speech.tts.ITextToSpeechCallback> mCallerToCallback = new java.util.HashMap<android.os.IBinder, android.speech.tts.ITextToSpeechCallback>();

        public void setCallback(android.os.IBinder caller, android.speech.tts.ITextToSpeechCallback cb) {
            synchronized(mCallerToCallback) {
                android.speech.tts.ITextToSpeechCallback old;
                if (cb != null) {
                    register(cb, caller);
                    old = mCallerToCallback.put(caller, cb);
                } else {
                    old = mCallerToCallback.remove(caller);
                }
                if ((old != null) && (old != cb)) {
                    unregister(old);
                }
            }
        }

        public void dispatchOnStop(java.lang.Object callerIdentity, java.lang.String utteranceId, boolean started) {
            android.speech.tts.ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb == null)
                return;

            try {
                cb.onStop(utteranceId, started);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.speech.tts.TextToSpeechService.TAG, "Callback onStop failed: " + e);
            }
        }

        public void dispatchOnSuccess(java.lang.Object callerIdentity, java.lang.String utteranceId) {
            android.speech.tts.ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb == null)
                return;

            try {
                cb.onSuccess(utteranceId);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.speech.tts.TextToSpeechService.TAG, "Callback onDone failed: " + e);
            }
        }

        public void dispatchOnStart(java.lang.Object callerIdentity, java.lang.String utteranceId) {
            android.speech.tts.ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb == null)
                return;

            try {
                cb.onStart(utteranceId);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.speech.tts.TextToSpeechService.TAG, "Callback onStart failed: " + e);
            }
        }

        public void dispatchOnError(java.lang.Object callerIdentity, java.lang.String utteranceId, int errorCode) {
            android.speech.tts.ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb == null)
                return;

            try {
                cb.onError(utteranceId, errorCode);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.speech.tts.TextToSpeechService.TAG, "Callback onError failed: " + e);
            }
        }

        public void dispatchOnBeginSynthesis(java.lang.Object callerIdentity, java.lang.String utteranceId, int sampleRateInHz, int audioFormat, int channelCount) {
            android.speech.tts.ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb == null)
                return;

            try {
                cb.onBeginSynthesis(utteranceId, sampleRateInHz, audioFormat, channelCount);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.speech.tts.TextToSpeechService.TAG, "Callback dispatchOnBeginSynthesis(String, int, int, int) failed: " + e);
            }
        }

        public void dispatchOnAudioAvailable(java.lang.Object callerIdentity, java.lang.String utteranceId, byte[] buffer) {
            android.speech.tts.ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb == null)
                return;

            try {
                cb.onAudioAvailable(utteranceId, buffer);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.speech.tts.TextToSpeechService.TAG, "Callback dispatchOnAudioAvailable(String, byte[]) failed: " + e);
            }
        }

        @java.lang.Override
        public void onCallbackDied(android.speech.tts.ITextToSpeechCallback callback, java.lang.Object cookie) {
            android.os.IBinder caller = ((android.os.IBinder) (cookie));
            synchronized(mCallerToCallback) {
                mCallerToCallback.remove(caller);
            }
            // mSynthHandler.stopForApp(caller);
        }

        @java.lang.Override
        public void kill() {
            synchronized(mCallerToCallback) {
                mCallerToCallback.clear();
                super.kill();
            }
        }

        private android.speech.tts.ITextToSpeechCallback getCallbackFor(java.lang.Object caller) {
            android.speech.tts.ITextToSpeechCallback cb;
            android.os.IBinder asBinder = ((android.os.IBinder) (caller));
            synchronized(mCallerToCallback) {
                cb = mCallerToCallback.get(asBinder);
            }
            return cb;
        }
    }
}

