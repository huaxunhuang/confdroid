/**
 * Copyright (C) 2013 The Android Open Source Project
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
 * Writes data about a given speech synthesis request for V1 API to the event
 * logs. The data that is logged includes the calling app, length of the
 * utterance, speech rate / pitch, the latency, and overall time taken.
 */
class EventLoggerV1 extends android.speech.tts.AbstractEventLogger {
    private final android.speech.tts.SynthesisRequest mRequest;

    EventLoggerV1(android.speech.tts.SynthesisRequest request, int callerUid, int callerPid, java.lang.String serviceApp) {
        super(callerUid, callerPid, serviceApp);
        mRequest = request;
    }

    @java.lang.Override
    protected void logFailure(int statusCode) {
        // We don't report stopped syntheses because their overall
        // total time spent will be inaccurate (will not correlate with
        // the length of the utterance).
        if (statusCode != android.speech.tts.TextToSpeech.STOPPED) {
            android.speech.tts.EventLogTags.writeTtsSpeakFailure(mServiceApp, mCallerUid, mCallerPid, getUtteranceLength(), getLocaleString(), mRequest.getSpeechRate(), mRequest.getPitch());
        }
    }

    @java.lang.Override
    protected void logSuccess(long audioLatency, long engineLatency, long engineTotal) {
        android.speech.tts.EventLogTags.writeTtsSpeakSuccess(mServiceApp, mCallerUid, mCallerPid, getUtteranceLength(), getLocaleString(), mRequest.getSpeechRate(), mRequest.getPitch(), engineLatency, engineTotal, audioLatency);
    }

    /**
     *
     *
     * @return the length of the utterance for the given synthesis, 0
    if the utterance was {@code null}.
     */
    private int getUtteranceLength() {
        final java.lang.String utterance = mRequest.getText();
        return utterance == null ? 0 : utterance.length();
    }

    /**
     * Returns a formatted locale string from the synthesis params of the
     * form lang-country-variant.
     */
    private java.lang.String getLocaleString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(mRequest.getLanguage());
        if (!android.text.TextUtils.isEmpty(mRequest.getCountry())) {
            sb.append('-');
            sb.append(mRequest.getCountry());
            if (!android.text.TextUtils.isEmpty(mRequest.getVariant())) {
                sb.append('-');
                sb.append(mRequest.getVariant());
            }
        }
        return sb.toString();
    }
}

