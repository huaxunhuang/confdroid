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


class AudioPlaybackQueueItem extends android.speech.tts.PlaybackQueueItem {
    private static final java.lang.String TAG = "TTS.AudioQueueItem";

    private final android.content.Context mContext;

    private final android.net.Uri mUri;

    private final android.speech.tts.TextToSpeechService.AudioOutputParams mAudioParams;

    private final android.os.ConditionVariable mDone;

    private android.media.MediaPlayer mPlayer;

    private volatile boolean mFinished;

    AudioPlaybackQueueItem(android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher dispatcher, java.lang.Object callerIdentity, android.content.Context context, android.net.Uri uri, android.speech.tts.TextToSpeechService.AudioOutputParams audioParams) {
        super(dispatcher, callerIdentity);
        mContext = context;
        mUri = uri;
        mAudioParams = audioParams;
        mDone = new android.os.ConditionVariable();
        mPlayer = null;
        mFinished = false;
    }

    @java.lang.Override
    public void run() {
        final android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher dispatcher = getDispatcher();
        dispatcher.dispatchOnStart();
        int sessionId = mAudioParams.mSessionId;
        mPlayer = android.media.MediaPlayer.create(mContext, mUri, null, mAudioParams.mAudioAttributes, sessionId > 0 ? sessionId : android.media.AudioManager.AUDIO_SESSION_ID_GENERATE);
        if (mPlayer == null) {
            dispatcher.dispatchOnError(android.speech.tts.TextToSpeech.ERROR_OUTPUT);
            return;
        }
        try {
            mPlayer.setOnErrorListener(new android.media.MediaPlayer.OnErrorListener() {
                @java.lang.Override
                public boolean onError(android.media.MediaPlayer mp, int what, int extra) {
                    android.util.Log.w(android.speech.tts.AudioPlaybackQueueItem.TAG, (("Audio playback error: " + what) + ", ") + extra);
                    mDone.open();
                    return true;
                }
            });
            mPlayer.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
                @java.lang.Override
                public void onCompletion(android.media.MediaPlayer mp) {
                    mFinished = true;
                    mDone.open();
                }
            });
            android.speech.tts.AudioPlaybackQueueItem.setupVolume(mPlayer, mAudioParams.mVolume, mAudioParams.mPan);
            mPlayer.start();
            mDone.block();
            finish();
        } catch (java.lang.IllegalArgumentException ex) {
            android.util.Log.w(android.speech.tts.AudioPlaybackQueueItem.TAG, "MediaPlayer failed", ex);
            mDone.open();
        }
        if (mFinished) {
            dispatcher.dispatchOnSuccess();
        } else {
            dispatcher.dispatchOnStop();
        }
    }

    private static void setupVolume(android.media.MediaPlayer player, float volume, float pan) {
        final float vol = android.speech.tts.AudioPlaybackQueueItem.clip(volume, 0.0F, 1.0F);
        final float panning = android.speech.tts.AudioPlaybackQueueItem.clip(pan, -1.0F, 1.0F);
        float volLeft = vol;
        float volRight = vol;
        if (panning > 0.0F) {
            volLeft *= 1.0F - panning;
        } else
            if (panning < 0.0F) {
                volRight *= 1.0F + panning;
            }

        player.setVolume(volLeft, volRight);
    }

    private static final float clip(float value, float min, float max) {
        return value < min ? min : value < max ? value : max;
    }

    private void finish() {
        try {
            mPlayer.stop();
        } catch (java.lang.IllegalStateException ex) {
            // Do nothing, the player is already stopped
        }
        mPlayer.release();
    }

    @java.lang.Override
    void stop(int errorCode) {
        mDone.open();
    }
}

