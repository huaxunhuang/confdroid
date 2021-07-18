package android.support.v4.speech.tts;


/**
 * Helper class for TTS functionality introduced in ICS MR1
 */
class TextToSpeechICSMR1 {
    /**
     * Call {@link TextToSpeech#getFeatures} if available.
     *
     * @return {@link TextToSpeech#getFeatures} or null on older devices.
     */
    static java.util.Set<java.lang.String> getFeatures(android.speech.tts.TextToSpeech tts, java.util.Locale locale) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            return tts.getFeatures(locale);
        }
        return null;
    }

    public static final java.lang.String KEY_FEATURE_EMBEDDED_SYNTHESIS = "embeddedTts";

    public static final java.lang.String KEY_FEATURE_NETWORK_SYNTHESIS = "networkTts";

    static interface UtteranceProgressListenerICSMR1 {
        void onDone(java.lang.String utteranceId);

        void onError(java.lang.String utteranceId);

        void onStart(java.lang.String utteranceId);
    }

    /**
     * Call {@link TextToSpeech#setOnUtteranceProgressListener} if ICS-MR1 or newer.
     *
     * On pre ICS-MR1 devices,{@link TextToSpeech#setOnUtteranceCompletedListener} is
     * used to emulate its behavior - at the end of synthesis we call
     * {@link UtteranceProgressListenerICSMR1#onStart(String)} and
     * {@link UtteranceProgressListenerICSMR1#onDone(String)} one after the other.
     * Errors can't be detected.
     */
    static void setUtteranceProgressListener(android.speech.tts.TextToSpeech tts, final android.support.v4.speech.tts.TextToSpeechICSMR1.UtteranceProgressListenerICSMR1 listener) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            tts.setOnUtteranceProgressListener(new android.speech.tts.UtteranceProgressListener() {
                @java.lang.Override
                public void onStart(java.lang.String utteranceId) {
                    listener.onStart(utteranceId);
                }

                @java.lang.Override
                public void onError(java.lang.String utteranceId) {
                    listener.onError(utteranceId);
                }

                @java.lang.Override
                public void onDone(java.lang.String utteranceId) {
                    listener.onDone(utteranceId);
                }
            });
        } else {
            tts.setOnUtteranceCompletedListener(new android.speech.tts.TextToSpeech.OnUtteranceCompletedListener() {
                @java.lang.Override
                public void onUtteranceCompleted(java.lang.String utteranceId) {
                    // Emulate onStart. Clients are expecting it will happen.
                    listener.onStart(utteranceId);
                    listener.onDone(utteranceId);
                }
            });
        }
    }
}

