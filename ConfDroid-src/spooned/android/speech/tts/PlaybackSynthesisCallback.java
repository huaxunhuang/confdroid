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
 * Speech synthesis request that plays the audio as it is received.
 */
class PlaybackSynthesisCallback extends android.speech.tts.AbstractSynthesisCallback {
    private static final java.lang.String TAG = "PlaybackSynthesisRequest";

    private static final boolean DBG = false;

    private static final int MIN_AUDIO_BUFFER_SIZE = 8192;

    private final android.speech.tts.TextToSpeechService.AudioOutputParams mAudioParams;

    /**
     * Guards {@link #mAudioTrackHandler}, {@link #mItem} and {@link #mStopped}.
     */
    private final java.lang.Object mStateLock = new java.lang.Object();

    // Handler associated with a thread that plays back audio requests.
    private final android.speech.tts.AudioPlaybackHandler mAudioTrackHandler;

    // A request "token", which will be non null after start() has been called.
    private android.speech.tts.SynthesisPlaybackQueueItem mItem = null;

    private volatile boolean mDone = false;

    /**
     * Status code of synthesis
     */
    protected int mStatusCode;

    private final android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher mDispatcher;

    private final java.lang.Object mCallerIdentity;

    private final android.speech.tts.AbstractEventLogger mLogger;

    PlaybackSynthesisCallback(@android.annotation.NonNull
    android.speech.tts.TextToSpeechService.AudioOutputParams audioParams, @android.annotation.NonNull
    android.speech.tts.AudioPlaybackHandler audioTrackHandler, @android.annotation.NonNull
    android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher dispatcher, @android.annotation.NonNull
    java.lang.Object callerIdentity, @android.annotation.NonNull
    android.speech.tts.AbstractEventLogger logger, boolean clientIsUsingV2) {
        super(clientIsUsingV2);
        mAudioParams = audioParams;
        mAudioTrackHandler = audioTrackHandler;
        mDispatcher = dispatcher;
        mCallerIdentity = callerIdentity;
        mLogger = logger;
        mStatusCode = android.speech.tts.TextToSpeech.SUCCESS;
    }

    @java.lang.Override
    void stop() {
        if (android.speech.tts.PlaybackSynthesisCallback.DBG)
            android.util.Log.d(android.speech.tts.PlaybackSynthesisCallback.TAG, "stop()");

        android.speech.tts.SynthesisPlaybackQueueItem item;
        synchronized(mStateLock) {
            if (mDone) {
                return;
            }
            if (mStatusCode == android.speech.tts.TextToSpeech.STOPPED) {
                android.util.Log.w(android.speech.tts.PlaybackSynthesisCallback.TAG, "stop() called twice");
                return;
            }
            item = mItem;
            mStatusCode = android.speech.tts.TextToSpeech.STOPPED;
        }
        if (item != null) {
            // This might result in the synthesis thread being woken up, at which
            // point it will write an additional buffer to the item - but we
            // won't worry about that because the audio playback queue will be cleared
            // soon after (see SynthHandler#stop(String).
            item.stop(android.speech.tts.TextToSpeech.STOPPED);
        } else {
            // This happens when stop() or error() were called before start() was.
            // In all other cases, mAudioTrackHandler.stop() will
            // result in onSynthesisDone being called, and we will
            // write data there.
            mLogger.onCompleted(android.speech.tts.TextToSpeech.STOPPED);
            mDispatcher.dispatchOnStop();
        }
    }

    @java.lang.Override
    public int getMaxBufferSize() {
        // The AudioTrack buffer will be at least MIN_AUDIO_BUFFER_SIZE, so that should always be
        // a safe buffer size to pass in.
        return android.speech.tts.PlaybackSynthesisCallback.MIN_AUDIO_BUFFER_SIZE;
    }

    @java.lang.Override
    public boolean hasStarted() {
        synchronized(mStateLock) {
            return mItem != null;
        }
    }

    @java.lang.Override
    public boolean hasFinished() {
        synchronized(mStateLock) {
            return mDone;
        }
    }

    @java.lang.Override
    public int start(int sampleRateInHz, int audioFormat, int channelCount) {
        if (android.speech.tts.PlaybackSynthesisCallback.DBG)
            android.util.Log.d(android.speech.tts.PlaybackSynthesisCallback.TAG, ((((("start(" + sampleRateInHz) + ",") + audioFormat) + ",") + channelCount) + ")");

        if (((audioFormat != android.media.AudioFormat.ENCODING_PCM_8BIT) && (audioFormat != android.media.AudioFormat.ENCODING_PCM_16BIT)) && (audioFormat != android.media.AudioFormat.ENCODING_PCM_FLOAT)) {
            android.util.Log.w(android.speech.tts.PlaybackSynthesisCallback.TAG, ((("Audio format encoding " + audioFormat) + " not supported. Please use one ") + "of AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT or ") + "AudioFormat.ENCODING_PCM_FLOAT");
        }
        mDispatcher.dispatchOnBeginSynthesis(sampleRateInHz, audioFormat, channelCount);
        int channelConfig = android.speech.tts.BlockingAudioTrack.getChannelConfig(channelCount);
        synchronized(mStateLock) {
            if (channelConfig == 0) {
                android.util.Log.e(android.speech.tts.PlaybackSynthesisCallback.TAG, "Unsupported number of channels :" + channelCount);
                mStatusCode = android.speech.tts.TextToSpeech.ERROR_OUTPUT;
                return android.speech.tts.TextToSpeech.ERROR;
            }
            if (mStatusCode == android.speech.tts.TextToSpeech.STOPPED) {
                if (android.speech.tts.PlaybackSynthesisCallback.DBG)
                    android.util.Log.d(android.speech.tts.PlaybackSynthesisCallback.TAG, "stop() called before start(), returning.");

                return errorCodeOnStop();
            }
            if (mStatusCode != android.speech.tts.TextToSpeech.SUCCESS) {
                if (android.speech.tts.PlaybackSynthesisCallback.DBG)
                    android.util.Log.d(android.speech.tts.PlaybackSynthesisCallback.TAG, "Error was raised");

                return android.speech.tts.TextToSpeech.ERROR;
            }
            if (mItem != null) {
                android.util.Log.e(android.speech.tts.PlaybackSynthesisCallback.TAG, "Start called twice");
                return android.speech.tts.TextToSpeech.ERROR;
            }
            android.speech.tts.SynthesisPlaybackQueueItem item = new android.speech.tts.SynthesisPlaybackQueueItem(mAudioParams, sampleRateInHz, audioFormat, channelCount, mDispatcher, mCallerIdentity, mLogger);
            mAudioTrackHandler.enqueue(item);
            mItem = item;
        }
        return android.speech.tts.TextToSpeech.SUCCESS;
    }

    @java.lang.Override
    public int audioAvailable(byte[] buffer, int offset, int length) {
        if (android.speech.tts.PlaybackSynthesisCallback.DBG)
            android.util.Log.d(android.speech.tts.PlaybackSynthesisCallback.TAG, ((((("audioAvailable(byte[" + buffer.length) + "],") + offset) + ",") + length) + ")");

        if ((length > getMaxBufferSize()) || (length <= 0)) {
            throw new java.lang.IllegalArgumentException(("buffer is too large or of zero length (" + (+length)) + " bytes)");
        }
        android.speech.tts.SynthesisPlaybackQueueItem item = null;
        synchronized(mStateLock) {
            if (mItem == null) {
                mStatusCode = android.speech.tts.TextToSpeech.ERROR_OUTPUT;
                return android.speech.tts.TextToSpeech.ERROR;
            }
            if (mStatusCode != android.speech.tts.TextToSpeech.SUCCESS) {
                if (android.speech.tts.PlaybackSynthesisCallback.DBG)
                    android.util.Log.d(android.speech.tts.PlaybackSynthesisCallback.TAG, "Error was raised");

                return android.speech.tts.TextToSpeech.ERROR;
            }
            if (mStatusCode == android.speech.tts.TextToSpeech.STOPPED) {
                return errorCodeOnStop();
            }
            item = mItem;
        }
        // Sigh, another copy.
        final byte[] bufferCopy = new byte[length];
        java.lang.System.arraycopy(buffer, offset, bufferCopy, 0, length);
        mDispatcher.dispatchOnAudioAvailable(bufferCopy);
        // Might block on mItem.this, if there are too many buffers waiting to
        // be consumed.
        try {
            item.put(bufferCopy);
        } catch (java.lang.InterruptedException ie) {
            synchronized(mStateLock) {
                mStatusCode = android.speech.tts.TextToSpeech.ERROR_OUTPUT;
                return android.speech.tts.TextToSpeech.ERROR;
            }
        }
        mLogger.onEngineDataReceived();
        return android.speech.tts.TextToSpeech.SUCCESS;
    }

    @java.lang.Override
    public int done() {
        if (android.speech.tts.PlaybackSynthesisCallback.DBG)
            android.util.Log.d(android.speech.tts.PlaybackSynthesisCallback.TAG, "done()");

        int statusCode = 0;
        android.speech.tts.SynthesisPlaybackQueueItem item = null;
        synchronized(mStateLock) {
            if (mDone) {
                android.util.Log.w(android.speech.tts.PlaybackSynthesisCallback.TAG, "Duplicate call to done()");
                // Not an error that would prevent synthesis. Hence no
                // setStatusCode
                return android.speech.tts.TextToSpeech.ERROR;
            }
            if (mStatusCode == android.speech.tts.TextToSpeech.STOPPED) {
                if (android.speech.tts.PlaybackSynthesisCallback.DBG)
                    android.util.Log.d(android.speech.tts.PlaybackSynthesisCallback.TAG, "Request has been aborted.");

                return errorCodeOnStop();
            }
            mDone = true;
            if (mItem == null) {
                // .done() was called before .start. Treat it as successful synthesis
                // for a client, despite service bad implementation.
                android.util.Log.w(android.speech.tts.PlaybackSynthesisCallback.TAG, "done() was called before start() call");
                if (mStatusCode == android.speech.tts.TextToSpeech.SUCCESS) {
                    mDispatcher.dispatchOnSuccess();
                } else {
                    mDispatcher.dispatchOnError(mStatusCode);
                }
                mLogger.onEngineComplete();
                return android.speech.tts.TextToSpeech.ERROR;
            }
            item = mItem;
            statusCode = mStatusCode;
        }
        // Signal done or error to item
        if (statusCode == android.speech.tts.TextToSpeech.SUCCESS) {
            item.done();
        } else {
            item.stop(statusCode);
        }
        mLogger.onEngineComplete();
        return android.speech.tts.TextToSpeech.SUCCESS;
    }

    @java.lang.Override
    public void error() {
        error(android.speech.tts.TextToSpeech.ERROR_SYNTHESIS);
    }

    @java.lang.Override
    public void error(int errorCode) {
        if (android.speech.tts.PlaybackSynthesisCallback.DBG)
            android.util.Log.d(android.speech.tts.PlaybackSynthesisCallback.TAG, "error() [will call stop]");

        synchronized(mStateLock) {
            if (mDone) {
                return;
            }
            mStatusCode = errorCode;
        }
    }
}

