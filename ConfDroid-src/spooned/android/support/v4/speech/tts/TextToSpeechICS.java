package android.support.v4.speech.tts;


/**
 * Helper class for TTS functionality introduced in ICS
 */
class TextToSpeechICS {
    private static final java.lang.String TAG = "android.support.v4.speech.tts";

    static android.speech.tts.TextToSpeech construct(android.content.Context context, android.speech.tts.TextToSpeech.OnInitListener onInitListener, java.lang.String engineName) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (engineName == null) {
                return new android.speech.tts.TextToSpeech(context, onInitListener);
            } else {
                android.util.Log.w(android.support.v4.speech.tts.TextToSpeechICS.TAG, "Can't specify tts engine on this device");
                return new android.speech.tts.TextToSpeech(context, onInitListener);
            }
        } else {
            return new android.speech.tts.TextToSpeech(context, onInitListener, engineName);
        }
    }
}

