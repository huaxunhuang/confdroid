/**
 * Copyright 2011 Google Inc. All Rights Reserved.
 */
package android.speech.tts;


abstract class PlaybackQueueItem implements java.lang.Runnable {
    private final android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher mDispatcher;

    private final java.lang.Object mCallerIdentity;

    PlaybackQueueItem(android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher dispatcher, java.lang.Object callerIdentity) {
        mDispatcher = dispatcher;
        mCallerIdentity = callerIdentity;
    }

    java.lang.Object getCallerIdentity() {
        return mCallerIdentity;
    }

    protected android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher getDispatcher() {
        return mDispatcher;
    }

    @java.lang.Override
    public abstract void run();

    /**
     * Stop the playback.
     *
     * @param errorCode
     * 		Cause of the stop. Can be either one of the error codes from
     * 		{@link android.speech.tts.TextToSpeech} or
     * 		{@link android.speech.tts.TextToSpeech#STOPPED}
     * 		if stopped on a client request.
     */
    abstract void stop(int errorCode);
}

