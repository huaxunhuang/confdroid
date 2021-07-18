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


class SilencePlaybackQueueItem extends android.speech.tts.PlaybackQueueItem {
    private final android.os.ConditionVariable mCondVar = new android.os.ConditionVariable();

    private final long mSilenceDurationMs;

    SilencePlaybackQueueItem(android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher dispatcher, java.lang.Object callerIdentity, long silenceDurationMs) {
        super(dispatcher, callerIdentity);
        mSilenceDurationMs = silenceDurationMs;
    }

    @java.lang.Override
    public void run() {
        getDispatcher().dispatchOnStart();
        boolean wasStopped = false;
        if (mSilenceDurationMs > 0) {
            wasStopped = mCondVar.block(mSilenceDurationMs);
        }
        if (wasStopped) {
            getDispatcher().dispatchOnStop();
        } else {
            getDispatcher().dispatchOnSuccess();
        }
    }

    @java.lang.Override
    void stop(int errorCode) {
        mCondVar.open();
    }
}

