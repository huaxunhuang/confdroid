/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.media.soundtrigger;


/**
 * A class that allows interaction with the actual sound trigger detection on the system.
 * Sound trigger detection refers to a detectors that match generic sound patterns that are
 * not voice-based. The voice-based recognition models should utilize the {@link VoiceInteractionService} instead. Access to this class is protected by a permission
 * granted only to system or privileged apps.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class SoundTriggerDetector {
    private static final boolean DBG = false;

    private static final java.lang.String TAG = "SoundTriggerDetector";

    private static final int MSG_AVAILABILITY_CHANGED = 1;

    private static final int MSG_SOUND_TRIGGER_DETECTED = 2;

    private static final int MSG_DETECTION_ERROR = 3;

    private static final int MSG_DETECTION_PAUSE = 4;

    private static final int MSG_DETECTION_RESUME = 5;

    private final java.lang.Object mLock = new java.lang.Object();

    private final com.android.internal.app.ISoundTriggerService mSoundTriggerService;

    private final java.util.UUID mSoundModelId;

    private final android.media.soundtrigger.SoundTriggerDetector.Callback mCallback;

    private final android.os.Handler mHandler;

    private final android.media.soundtrigger.SoundTriggerDetector.RecognitionCallback mRecognitionCallback;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef(flag = true, value = { android.media.soundtrigger.SoundTriggerDetector.RECOGNITION_FLAG_NONE, android.media.soundtrigger.SoundTriggerDetector.RECOGNITION_FLAG_CAPTURE_TRIGGER_AUDIO, android.media.soundtrigger.SoundTriggerDetector.RECOGNITION_FLAG_ALLOW_MULTIPLE_TRIGGERS })
    public @interface RecognitionFlags {}

    /**
     * Empty flag for {@link #startRecognition(int)}.
     *
     * @unknown 
     */
    public static final int RECOGNITION_FLAG_NONE = 0;

    /**
     * Recognition flag for {@link #startRecognition(int)} that indicates
     * whether the trigger audio for hotword needs to be captured.
     */
    public static final int RECOGNITION_FLAG_CAPTURE_TRIGGER_AUDIO = 0x1;

    /**
     * Recognition flag for {@link #startRecognition(int)} that indicates
     * whether the recognition should keep going on even after the
     * model triggers.
     * If this flag is specified, it's possible to get multiple
     * triggers after a call to {@link #startRecognition(int)}, if the model
     * triggers multiple times.
     * When this isn't specified, the default behavior is to stop recognition once the
     * trigger happenss, till the caller starts recognition again.
     */
    public static final int RECOGNITION_FLAG_ALLOW_MULTIPLE_TRIGGERS = 0x2;

    /**
     * Additional payload for {@link Callback#onDetected}.
     */
    public static class EventPayload {
        private final boolean mTriggerAvailable;

        // Indicates if {@code captureSession} can be used to continue capturing more audio
        // from the DSP hardware.
        private final boolean mCaptureAvailable;

        // The session to use when attempting to capture more audio from the DSP hardware.
        private final int mCaptureSession;

        private final android.media.AudioFormat mAudioFormat;

        // Raw data associated with the event.
        // This is the audio that triggered the keyphrase if {@code isTriggerAudio} is true.
        private final byte[] mData;

        private EventPayload(boolean triggerAvailable, boolean captureAvailable, android.media.AudioFormat audioFormat, int captureSession, byte[] data) {
            mTriggerAvailable = triggerAvailable;
            mCaptureAvailable = captureAvailable;
            mCaptureSession = captureSession;
            mAudioFormat = audioFormat;
            mData = data;
        }

        /**
         * Gets the format of the audio obtained using {@link #getTriggerAudio()}.
         * May be null if there's no audio present.
         */
        @android.annotation.Nullable
        public android.media.AudioFormat getCaptureAudioFormat() {
            return mAudioFormat;
        }

        /**
         * Gets the raw audio that triggered the keyphrase.
         * This may be null if the trigger audio isn't available.
         * If non-null, the format of the audio can be obtained by calling
         * {@link #getCaptureAudioFormat()}.
         *
         * @see AlwaysOnHotwordDetector#RECOGNITION_FLAG_CAPTURE_TRIGGER_AUDIO
         */
        @android.annotation.Nullable
        public byte[] getTriggerAudio() {
            if (mTriggerAvailable) {
                return mData;
            } else {
                return null;
            }
        }

        /**
         * Gets the session ID to start a capture from the DSP.
         * This may be null if streaming capture isn't possible.
         * If non-null, the format of the audio that can be captured can be
         * obtained using {@link #getCaptureAudioFormat()}.
         *
         * TODO: Candidate for Public API when the API to start capture with a session ID
         * is made public.
         *
         * TODO: Add this to {@link #getCaptureAudioFormat()}:
         * "Gets the format of the audio obtained using {@link #getTriggerAudio()}
         * or {@link #getCaptureSession()}. May be null if no audio can be obtained
         * for either the trigger or a streaming session."
         *
         * TODO: Should this return a known invalid value instead?
         *
         * @unknown 
         */
        @android.annotation.Nullable
        public java.lang.Integer getCaptureSession() {
            if (mCaptureAvailable) {
                return mCaptureSession;
            } else {
                return null;
            }
        }
    }

    public static abstract class Callback {
        /**
         * Called when the availability of the sound model changes.
         */
        public abstract void onAvailabilityChanged(int status);

        /**
         * Called when the sound model has triggered (such as when it matched a
         * given sound pattern).
         */
        public abstract void onDetected(@android.annotation.NonNull
        android.media.soundtrigger.SoundTriggerDetector.EventPayload eventPayload);

        /**
         * Called when the detection fails due to an error.
         */
        public abstract void onError();

        /**
         * Called when the recognition is paused temporarily for some reason.
         * This is an informational callback, and the clients shouldn't be doing anything here
         * except showing an indication on their UI if they have to.
         */
        public abstract void onRecognitionPaused();

        /**
         * Called when the recognition is resumed after it was temporarily paused.
         * This is an informational callback, and the clients shouldn't be doing anything here
         * except showing an indication on their UI if they have to.
         */
        public abstract void onRecognitionResumed();
    }

    /**
     * This class should be constructed by the {@link SoundTriggerManager}.
     *
     * @unknown 
     */
    SoundTriggerDetector(com.android.internal.app.ISoundTriggerService soundTriggerService, java.util.UUID soundModelId, @android.annotation.NonNull
    android.media.soundtrigger.SoundTriggerDetector.Callback callback, @android.annotation.Nullable
    android.os.Handler handler) {
        mSoundTriggerService = soundTriggerService;
        mSoundModelId = soundModelId;
        mCallback = callback;
        if (handler == null) {
            mHandler = new android.media.soundtrigger.SoundTriggerDetector.MyHandler();
        } else {
            mHandler = new android.media.soundtrigger.SoundTriggerDetector.MyHandler(handler.getLooper());
        }
        mRecognitionCallback = new android.media.soundtrigger.SoundTriggerDetector.RecognitionCallback();
    }

    /**
     * Starts recognition on the associated sound model. Result is indicated via the
     * {@link Callback}.
     *
     * @return Indicates whether the call succeeded or not.
     */
    public boolean startRecognition(@android.media.soundtrigger.SoundTriggerDetector.RecognitionFlags
    int recognitionFlags) {
        if (android.media.soundtrigger.SoundTriggerDetector.DBG) {
            android.util.Slog.d(android.media.soundtrigger.SoundTriggerDetector.TAG, "startRecognition()");
        }
        boolean captureTriggerAudio = (recognitionFlags & android.media.soundtrigger.SoundTriggerDetector.RECOGNITION_FLAG_CAPTURE_TRIGGER_AUDIO) != 0;
        boolean allowMultipleTriggers = (recognitionFlags & android.media.soundtrigger.SoundTriggerDetector.RECOGNITION_FLAG_ALLOW_MULTIPLE_TRIGGERS) != 0;
        int status = android.hardware.soundtrigger.SoundTrigger.STATUS_OK;
        try {
            status = mSoundTriggerService.startRecognition(new android.os.ParcelUuid(mSoundModelId), mRecognitionCallback, new android.hardware.soundtrigger.SoundTrigger.RecognitionConfig(captureTriggerAudio, allowMultipleTriggers, null, null));
        } catch (android.os.RemoteException e) {
            return false;
        }
        return status == android.hardware.soundtrigger.SoundTrigger.STATUS_OK;
    }

    /**
     * Stops recognition for the associated model.
     */
    public boolean stopRecognition() {
        int status = android.hardware.soundtrigger.SoundTrigger.STATUS_OK;
        try {
            status = mSoundTriggerService.stopRecognition(new android.os.ParcelUuid(mSoundModelId), mRecognitionCallback);
        } catch (android.os.RemoteException e) {
            return false;
        }
        return status == android.hardware.soundtrigger.SoundTrigger.STATUS_OK;
    }

    /**
     *
     *
     * @unknown 
     */
    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        synchronized(mLock) {
            // TODO: Dump useful debug information.
        }
    }

    /**
     * Callback that handles events from the lower sound trigger layer.
     *
     * Note that these callbacks will be called synchronously from the SoundTriggerService
     * layer and thus should do minimal work (such as sending a message on a handler to do
     * the real work).
     *
     * @unknown 
     */
    private class RecognitionCallback extends android.hardware.soundtrigger.IRecognitionStatusCallback.Stub {
        /**
         *
         *
         * @unknown 
         */
        @java.lang.Override
        public void onGenericSoundTriggerDetected(android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent event) {
            android.util.Slog.d(android.media.soundtrigger.SoundTriggerDetector.TAG, "onGenericSoundTriggerDetected()" + event);
            android.os.Message.obtain(mHandler, android.media.soundtrigger.SoundTriggerDetector.MSG_SOUND_TRIGGER_DETECTED, new android.media.soundtrigger.SoundTriggerDetector.EventPayload(event.triggerInData, event.captureAvailable, event.captureFormat, event.captureSession, event.data)).sendToTarget();
        }

        @java.lang.Override
        public void onKeyphraseDetected(android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent event) {
            android.util.Slog.e(android.media.soundtrigger.SoundTriggerDetector.TAG, "Ignoring onKeyphraseDetected() called for " + event);
        }

        /**
         *
         *
         * @unknown 
         */
        @java.lang.Override
        public void onError(int status) {
            android.util.Slog.d(android.media.soundtrigger.SoundTriggerDetector.TAG, "onError()" + status);
            mHandler.sendEmptyMessage(android.media.soundtrigger.SoundTriggerDetector.MSG_DETECTION_ERROR);
        }

        /**
         *
         *
         * @unknown 
         */
        @java.lang.Override
        public void onRecognitionPaused() {
            android.util.Slog.d(android.media.soundtrigger.SoundTriggerDetector.TAG, "onRecognitionPaused()");
            mHandler.sendEmptyMessage(android.media.soundtrigger.SoundTriggerDetector.MSG_DETECTION_PAUSE);
        }

        /**
         *
         *
         * @unknown 
         */
        @java.lang.Override
        public void onRecognitionResumed() {
            android.util.Slog.d(android.media.soundtrigger.SoundTriggerDetector.TAG, "onRecognitionResumed()");
            mHandler.sendEmptyMessage(android.media.soundtrigger.SoundTriggerDetector.MSG_DETECTION_RESUME);
        }
    }

    private class MyHandler extends android.os.Handler {
        MyHandler() {
            super();
        }

        MyHandler(android.os.Looper looper) {
            super(looper);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            if (android.media.soundtrigger.SoundTriggerDetector.this.mCallback == null) {
                android.util.Slog.w(android.media.soundtrigger.SoundTriggerDetector.TAG, ("Received message: " + msg.what) + " for NULL callback.");
                return;
            }
            switch (msg.what) {
                case android.media.soundtrigger.SoundTriggerDetector.MSG_SOUND_TRIGGER_DETECTED :
                    android.media.soundtrigger.SoundTriggerDetector.this.mCallback.onDetected(((android.media.soundtrigger.SoundTriggerDetector.EventPayload) (msg.obj)));
                    break;
                case android.media.soundtrigger.SoundTriggerDetector.MSG_DETECTION_ERROR :
                    android.media.soundtrigger.SoundTriggerDetector.this.mCallback.onError();
                    break;
                case android.media.soundtrigger.SoundTriggerDetector.MSG_DETECTION_PAUSE :
                    android.media.soundtrigger.SoundTriggerDetector.this.mCallback.onRecognitionPaused();
                    break;
                case android.media.soundtrigger.SoundTriggerDetector.MSG_DETECTION_RESUME :
                    android.media.soundtrigger.SoundTriggerDetector.this.mCallback.onRecognitionResumed();
                    break;
                default :
                    super.handleMessage(msg);
            }
        }
    }
}

