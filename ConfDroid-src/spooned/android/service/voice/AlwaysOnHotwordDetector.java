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
package android.service.voice;


/**
 * A class that lets a VoiceInteractionService implementation interact with
 * always-on keyphrase detection APIs.
 */
public class AlwaysOnHotwordDetector {
    // ---- States of Keyphrase availability. Return codes for onAvailabilityChanged() ----//
    /**
     * Indicates that this hotword detector is no longer valid for any recognition
     * and should not be used anymore.
     */
    private static final int STATE_INVALID = -3;

    /**
     * Indicates that recognition for the given keyphrase is not available on the system
     * because of the hardware configuration.
     * No further interaction should be performed with the detector that returns this availability.
     */
    public static final int STATE_HARDWARE_UNAVAILABLE = -2;

    /**
     * Indicates that recognition for the given keyphrase is not supported.
     * No further interaction should be performed with the detector that returns this availability.
     */
    public static final int STATE_KEYPHRASE_UNSUPPORTED = -1;

    /**
     * Indicates that the given keyphrase is not enrolled.
     * The caller may choose to begin an enrollment flow for the keyphrase.
     */
    public static final int STATE_KEYPHRASE_UNENROLLED = 1;

    /**
     * Indicates that the given keyphrase is currently enrolled and it's possible to start
     * recognition for it.
     */
    public static final int STATE_KEYPHRASE_ENROLLED = 2;

    /**
     * Indicates that the detector isn't ready currently.
     */
    private static final int STATE_NOT_READY = 0;

    // Keyphrase management actions. Used in getManageIntent() ----//
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.service.voice.AlwaysOnHotwordDetector.MANAGE_ACTION_ENROLL, android.service.voice.AlwaysOnHotwordDetector.MANAGE_ACTION_RE_ENROLL, android.service.voice.AlwaysOnHotwordDetector.MANAGE_ACTION_UN_ENROLL })
    private @interface ManageActions {}

    /**
     * Indicates that we need to enroll.
     *
     * @unknown 
     */
    public static final int MANAGE_ACTION_ENROLL = 0;

    /**
     * Indicates that we need to re-enroll.
     *
     * @unknown 
     */
    public static final int MANAGE_ACTION_RE_ENROLL = 1;

    /**
     * Indicates that we need to un-enroll.
     *
     * @unknown 
     */
    public static final int MANAGE_ACTION_UN_ENROLL = 2;

    // -- Flags for startRecognition    ----//
    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef(flag = true, value = { android.service.voice.AlwaysOnHotwordDetector.RECOGNITION_FLAG_NONE, android.service.voice.AlwaysOnHotwordDetector.RECOGNITION_FLAG_CAPTURE_TRIGGER_AUDIO, android.service.voice.AlwaysOnHotwordDetector.RECOGNITION_FLAG_ALLOW_MULTIPLE_TRIGGERS })
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
     * whether the recognition should keep going on even after the keyphrase triggers.
     * If this flag is specified, it's possible to get multiple triggers after a
     * call to {@link #startRecognition(int)} if the user speaks the keyphrase multiple times.
     * When this isn't specified, the default behavior is to stop recognition once the
     * keyphrase is spoken, till the caller starts recognition again.
     */
    public static final int RECOGNITION_FLAG_ALLOW_MULTIPLE_TRIGGERS = 0x2;

    // ---- Recognition mode flags. Return codes for getSupportedRecognitionModes() ----//
    // Must be kept in sync with the related attribute defined as searchKeyphraseRecognitionFlags.
    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef(flag = true, value = { android.service.voice.AlwaysOnHotwordDetector.RECOGNITION_MODE_VOICE_TRIGGER, android.service.voice.AlwaysOnHotwordDetector.RECOGNITION_MODE_USER_IDENTIFICATION })
    public @interface RecognitionModes {}

    /**
     * Simple recognition of the key phrase.
     * Returned by {@link #getSupportedRecognitionModes()}
     */
    public static final int RECOGNITION_MODE_VOICE_TRIGGER = android.hardware.soundtrigger.SoundTrigger.RECOGNITION_MODE_VOICE_TRIGGER;

    /**
     * User identification performed with the keyphrase recognition.
     * Returned by {@link #getSupportedRecognitionModes()}
     */
    public static final int RECOGNITION_MODE_USER_IDENTIFICATION = android.hardware.soundtrigger.SoundTrigger.RECOGNITION_MODE_USER_IDENTIFICATION;

    static final java.lang.String TAG = "AlwaysOnHotwordDetector";

    static final boolean DBG = false;

    private static final int STATUS_ERROR = android.hardware.soundtrigger.SoundTrigger.STATUS_ERROR;

    private static final int STATUS_OK = android.hardware.soundtrigger.SoundTrigger.STATUS_OK;

    private static final int MSG_AVAILABILITY_CHANGED = 1;

    private static final int MSG_HOTWORD_DETECTED = 2;

    private static final int MSG_DETECTION_ERROR = 3;

    private static final int MSG_DETECTION_PAUSE = 4;

    private static final int MSG_DETECTION_RESUME = 5;

    private final java.lang.String mText;

    private final java.util.Locale mLocale;

    /**
     * The metadata of the Keyphrase, derived from the enrollment application.
     * This may be null if this keyphrase isn't supported by the enrollment application.
     */
    private final android.hardware.soundtrigger.KeyphraseMetadata mKeyphraseMetadata;

    private final android.hardware.soundtrigger.KeyphraseEnrollmentInfo mKeyphraseEnrollmentInfo;

    private final android.service.voice.IVoiceInteractionService mVoiceInteractionService;

    private final com.android.internal.app.IVoiceInteractionManagerService mModelManagementService;

    private final android.service.voice.AlwaysOnHotwordDetector.SoundTriggerListener mInternalCallback;

    private final android.service.voice.AlwaysOnHotwordDetector.Callback mExternalCallback;

    private final java.lang.Object mLock = new java.lang.Object();

    private final android.os.Handler mHandler;

    private int mAvailability = android.service.voice.AlwaysOnHotwordDetector.STATE_NOT_READY;

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

    /**
     * Callbacks for always-on hotword detection.
     */
    public static abstract class Callback {
        /**
         * Called when the hotword availability changes.
         * This indicates a change in the availability of recognition for the given keyphrase.
         * It's called at least once with the initial availability.<p/>
         *
         * Availability implies whether the hardware on this system is capable of listening for
         * the given keyphrase or not. <p/>
         *
         * @see AlwaysOnHotwordDetector#STATE_HARDWARE_UNAVAILABLE
         * @see AlwaysOnHotwordDetector#STATE_KEYPHRASE_UNSUPPORTED
         * @see AlwaysOnHotwordDetector#STATE_KEYPHRASE_UNENROLLED
         * @see AlwaysOnHotwordDetector#STATE_KEYPHRASE_ENROLLED
         */
        public abstract void onAvailabilityChanged(int status);

        /**
         * Called when the keyphrase is spoken.
         * This implicitly stops listening for the keyphrase once it's detected.
         * Clients should start a recognition again once they are done handling this
         * detection.
         *
         * @param eventPayload
         * 		Payload data for the detection event.
         * 		This may contain the trigger audio, if requested when calling
         * 		{@link AlwaysOnHotwordDetector#startRecognition(int)}.
         */
        public abstract void onDetected(@android.annotation.NonNull
        android.service.voice.AlwaysOnHotwordDetector.EventPayload eventPayload);

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
     *
     *
     * @param text
     * 		The keyphrase text to get the detector for.
     * @param locale
     * 		The java locale for the detector.
     * @param callback
     * 		A non-null Callback for receiving the recognition events.
     * @param voiceInteractionService
     * 		The current voice interaction service.
     * @param modelManagementService
     * 		A service that allows management of sound models.
     * @unknown 
     */
    public AlwaysOnHotwordDetector(java.lang.String text, java.util.Locale locale, android.service.voice.AlwaysOnHotwordDetector.Callback callback, android.hardware.soundtrigger.KeyphraseEnrollmentInfo keyphraseEnrollmentInfo, android.service.voice.IVoiceInteractionService voiceInteractionService, com.android.internal.app.IVoiceInteractionManagerService modelManagementService) {
        mText = text;
        mLocale = locale;
        mKeyphraseEnrollmentInfo = keyphraseEnrollmentInfo;
        mKeyphraseMetadata = mKeyphraseEnrollmentInfo.getKeyphraseMetadata(text, locale);
        mExternalCallback = callback;
        mHandler = new android.service.voice.AlwaysOnHotwordDetector.MyHandler();
        mInternalCallback = new android.service.voice.AlwaysOnHotwordDetector.SoundTriggerListener(mHandler);
        mVoiceInteractionService = voiceInteractionService;
        mModelManagementService = modelManagementService;
        new android.service.voice.AlwaysOnHotwordDetector.RefreshAvailabiltyTask().execute();
    }

    /**
     * Gets the recognition modes supported by the associated keyphrase.
     *
     * @see #RECOGNITION_MODE_USER_IDENTIFICATION
     * @see #RECOGNITION_MODE_VOICE_TRIGGER
     * @throws UnsupportedOperationException
     * 		if the keyphrase itself isn't supported.
     * 		Callers should only call this method after a supported state callback on
     * 		{@link Callback#onAvailabilityChanged(int)} to avoid this exception.
     * @throws IllegalStateException
     * 		if the detector is in an invalid state.
     * 		This may happen if another detector has been instantiated or the
     * 		{@link VoiceInteractionService} hosting this detector has been shut down.
     */
    @android.service.voice.AlwaysOnHotwordDetector.RecognitionModes
    public int getSupportedRecognitionModes() {
        if (android.service.voice.AlwaysOnHotwordDetector.DBG)
            android.util.Slog.d(android.service.voice.AlwaysOnHotwordDetector.TAG, "getSupportedRecognitionModes()");

        synchronized(mLock) {
            return getSupportedRecognitionModesLocked();
        }
    }

    private int getSupportedRecognitionModesLocked() {
        if (mAvailability == android.service.voice.AlwaysOnHotwordDetector.STATE_INVALID) {
            throw new java.lang.IllegalStateException("getSupportedRecognitionModes called on an invalid detector");
        }
        // This method only makes sense if we can actually support a recognition.
        if ((mAvailability != android.service.voice.AlwaysOnHotwordDetector.STATE_KEYPHRASE_ENROLLED) && (mAvailability != android.service.voice.AlwaysOnHotwordDetector.STATE_KEYPHRASE_UNENROLLED)) {
            throw new java.lang.UnsupportedOperationException("Getting supported recognition modes for the keyphrase is not supported");
        }
        return mKeyphraseMetadata.recognitionModeFlags;
    }

    /**
     * Starts recognition for the associated keyphrase.
     *
     * @see #RECOGNITION_FLAG_CAPTURE_TRIGGER_AUDIO
     * @see #RECOGNITION_FLAG_ALLOW_MULTIPLE_TRIGGERS
     * @param recognitionFlags
     * 		The flags to control the recognition properties.
     * @return Indicates whether the call succeeded or not.
     * @throws UnsupportedOperationException
     * 		if the recognition isn't supported.
     * 		Callers should only call this method after a supported state callback on
     * 		{@link Callback#onAvailabilityChanged(int)} to avoid this exception.
     * @throws IllegalStateException
     * 		if the detector is in an invalid state.
     * 		This may happen if another detector has been instantiated or the
     * 		{@link VoiceInteractionService} hosting this detector has been shut down.
     */
    public boolean startRecognition(@android.service.voice.AlwaysOnHotwordDetector.RecognitionFlags
    int recognitionFlags) {
        if (android.service.voice.AlwaysOnHotwordDetector.DBG)
            android.util.Slog.d(android.service.voice.AlwaysOnHotwordDetector.TAG, ("startRecognition(" + recognitionFlags) + ")");

        synchronized(mLock) {
            if (mAvailability == android.service.voice.AlwaysOnHotwordDetector.STATE_INVALID) {
                throw new java.lang.IllegalStateException("startRecognition called on an invalid detector");
            }
            // Check if we can start/stop a recognition.
            if (mAvailability != android.service.voice.AlwaysOnHotwordDetector.STATE_KEYPHRASE_ENROLLED) {
                throw new java.lang.UnsupportedOperationException("Recognition for the given keyphrase is not supported");
            }
            return startRecognitionLocked(recognitionFlags) == android.service.voice.AlwaysOnHotwordDetector.STATUS_OK;
        }
    }

    /**
     * Stops recognition for the associated keyphrase.
     *
     * @return Indicates whether the call succeeded or not.
     * @throws UnsupportedOperationException
     * 		if the recognition isn't supported.
     * 		Callers should only call this method after a supported state callback on
     * 		{@link Callback#onAvailabilityChanged(int)} to avoid this exception.
     * @throws IllegalStateException
     * 		if the detector is in an invalid state.
     * 		This may happen if another detector has been instantiated or the
     * 		{@link VoiceInteractionService} hosting this detector has been shut down.
     */
    public boolean stopRecognition() {
        if (android.service.voice.AlwaysOnHotwordDetector.DBG)
            android.util.Slog.d(android.service.voice.AlwaysOnHotwordDetector.TAG, "stopRecognition()");

        synchronized(mLock) {
            if (mAvailability == android.service.voice.AlwaysOnHotwordDetector.STATE_INVALID) {
                throw new java.lang.IllegalStateException("stopRecognition called on an invalid detector");
            }
            // Check if we can start/stop a recognition.
            if (mAvailability != android.service.voice.AlwaysOnHotwordDetector.STATE_KEYPHRASE_ENROLLED) {
                throw new java.lang.UnsupportedOperationException("Recognition for the given keyphrase is not supported");
            }
            return stopRecognitionLocked() == android.service.voice.AlwaysOnHotwordDetector.STATUS_OK;
        }
    }

    /**
     * Creates an intent to start the enrollment for the associated keyphrase.
     * This intent must be invoked using {@link Activity#startActivityForResult(Intent, int)}.
     * Starting re-enrollment is only valid if the keyphrase is un-enrolled,
     * i.e. {@link #STATE_KEYPHRASE_UNENROLLED},
     * otherwise {@link #createReEnrollIntent()} should be preferred.
     *
     * @return An {@link Intent} to start enrollment for the given keyphrase.
     * @throws UnsupportedOperationException
     * 		if managing they keyphrase isn't supported.
     * 		Callers should only call this method after a supported state callback on
     * 		{@link Callback#onAvailabilityChanged(int)} to avoid this exception.
     * @throws IllegalStateException
     * 		if the detector is in an invalid state.
     * 		This may happen if another detector has been instantiated or the
     * 		{@link VoiceInteractionService} hosting this detector has been shut down.
     */
    public android.content.Intent createEnrollIntent() {
        if (android.service.voice.AlwaysOnHotwordDetector.DBG)
            android.util.Slog.d(android.service.voice.AlwaysOnHotwordDetector.TAG, "createEnrollIntent");

        synchronized(mLock) {
            return getManageIntentLocked(android.service.voice.AlwaysOnHotwordDetector.MANAGE_ACTION_ENROLL);
        }
    }

    /**
     * Creates an intent to start the un-enrollment for the associated keyphrase.
     * This intent must be invoked using {@link Activity#startActivityForResult(Intent, int)}.
     * Starting re-enrollment is only valid if the keyphrase is already enrolled,
     * i.e. {@link #STATE_KEYPHRASE_ENROLLED}, otherwise invoking this may result in an error.
     *
     * @return An {@link Intent} to start un-enrollment for the given keyphrase.
     * @throws UnsupportedOperationException
     * 		if managing they keyphrase isn't supported.
     * 		Callers should only call this method after a supported state callback on
     * 		{@link Callback#onAvailabilityChanged(int)} to avoid this exception.
     * @throws IllegalStateException
     * 		if the detector is in an invalid state.
     * 		This may happen if another detector has been instantiated or the
     * 		{@link VoiceInteractionService} hosting this detector has been shut down.
     */
    public android.content.Intent createUnEnrollIntent() {
        if (android.service.voice.AlwaysOnHotwordDetector.DBG)
            android.util.Slog.d(android.service.voice.AlwaysOnHotwordDetector.TAG, "createUnEnrollIntent");

        synchronized(mLock) {
            return getManageIntentLocked(android.service.voice.AlwaysOnHotwordDetector.MANAGE_ACTION_UN_ENROLL);
        }
    }

    /**
     * Creates an intent to start the re-enrollment for the associated keyphrase.
     * This intent must be invoked using {@link Activity#startActivityForResult(Intent, int)}.
     * Starting re-enrollment is only valid if the keyphrase is already enrolled,
     * i.e. {@link #STATE_KEYPHRASE_ENROLLED}, otherwise invoking this may result in an error.
     *
     * @return An {@link Intent} to start re-enrollment for the given keyphrase.
     * @throws UnsupportedOperationException
     * 		if managing they keyphrase isn't supported.
     * 		Callers should only call this method after a supported state callback on
     * 		{@link Callback#onAvailabilityChanged(int)} to avoid this exception.
     * @throws IllegalStateException
     * 		if the detector is in an invalid state.
     * 		This may happen if another detector has been instantiated or the
     * 		{@link VoiceInteractionService} hosting this detector has been shut down.
     */
    public android.content.Intent createReEnrollIntent() {
        if (android.service.voice.AlwaysOnHotwordDetector.DBG)
            android.util.Slog.d(android.service.voice.AlwaysOnHotwordDetector.TAG, "createReEnrollIntent");

        synchronized(mLock) {
            return getManageIntentLocked(android.service.voice.AlwaysOnHotwordDetector.MANAGE_ACTION_RE_ENROLL);
        }
    }

    private android.content.Intent getManageIntentLocked(int action) {
        if (mAvailability == android.service.voice.AlwaysOnHotwordDetector.STATE_INVALID) {
            throw new java.lang.IllegalStateException("getManageIntent called on an invalid detector");
        }
        // This method only makes sense if we can actually support a recognition.
        if ((mAvailability != android.service.voice.AlwaysOnHotwordDetector.STATE_KEYPHRASE_ENROLLED) && (mAvailability != android.service.voice.AlwaysOnHotwordDetector.STATE_KEYPHRASE_UNENROLLED)) {
            throw new java.lang.UnsupportedOperationException("Managing the given keyphrase is not supported");
        }
        return mKeyphraseEnrollmentInfo.getManageKeyphraseIntent(action, mText, mLocale);
    }

    /**
     * Invalidates this hotword detector so that any future calls to this result
     * in an IllegalStateException.
     *
     * @unknown 
     */
    void invalidate() {
        synchronized(mLock) {
            mAvailability = android.service.voice.AlwaysOnHotwordDetector.STATE_INVALID;
            notifyStateChangedLocked();
        }
    }

    /**
     * Reloads the sound models from the service.
     *
     * @unknown 
     */
    void onSoundModelsChanged() {
        synchronized(mLock) {
            if (((mAvailability == android.service.voice.AlwaysOnHotwordDetector.STATE_INVALID) || (mAvailability == android.service.voice.AlwaysOnHotwordDetector.STATE_HARDWARE_UNAVAILABLE)) || (mAvailability == android.service.voice.AlwaysOnHotwordDetector.STATE_KEYPHRASE_UNSUPPORTED)) {
                android.util.Slog.w(android.service.voice.AlwaysOnHotwordDetector.TAG, "Received onSoundModelsChanged for an unsupported keyphrase/config");
                return;
            }
            // Stop the recognition before proceeding.
            // This is done because we want to stop the recognition on an older model if it changed
            // or was deleted.
            // The availability change callback should ensure that the client starts recognition
            // again if needed.
            stopRecognitionLocked();
            // Execute a refresh availability task - which should then notify of a change.
            new android.service.voice.AlwaysOnHotwordDetector.RefreshAvailabiltyTask().execute();
        }
    }

    private int startRecognitionLocked(int recognitionFlags) {
        android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra[] recognitionExtra = new android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra[1];
        // TODO: Do we need to do something about the confidence level here?
        recognitionExtra[0] = new android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra(mKeyphraseMetadata.id, mKeyphraseMetadata.recognitionModeFlags, 0, new android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel[0]);
        boolean captureTriggerAudio = (recognitionFlags & android.service.voice.AlwaysOnHotwordDetector.RECOGNITION_FLAG_CAPTURE_TRIGGER_AUDIO) != 0;
        boolean allowMultipleTriggers = (recognitionFlags & android.service.voice.AlwaysOnHotwordDetector.RECOGNITION_FLAG_ALLOW_MULTIPLE_TRIGGERS) != 0;
        int code = android.service.voice.AlwaysOnHotwordDetector.STATUS_ERROR;
        try {
            code = mModelManagementService.startRecognition(mVoiceInteractionService, mKeyphraseMetadata.id, mLocale.toLanguageTag(), mInternalCallback, /* additional data */
            new android.hardware.soundtrigger.SoundTrigger.RecognitionConfig(captureTriggerAudio, allowMultipleTriggers, recognitionExtra, null));
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(android.service.voice.AlwaysOnHotwordDetector.TAG, "RemoteException in startRecognition!", e);
        }
        if (code != android.service.voice.AlwaysOnHotwordDetector.STATUS_OK) {
            android.util.Slog.w(android.service.voice.AlwaysOnHotwordDetector.TAG, "startRecognition() failed with error code " + code);
        }
        return code;
    }

    private int stopRecognitionLocked() {
        int code = android.service.voice.AlwaysOnHotwordDetector.STATUS_ERROR;
        try {
            code = mModelManagementService.stopRecognition(mVoiceInteractionService, mKeyphraseMetadata.id, mInternalCallback);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(android.service.voice.AlwaysOnHotwordDetector.TAG, "RemoteException in stopRecognition!", e);
        }
        if (code != android.service.voice.AlwaysOnHotwordDetector.STATUS_OK) {
            android.util.Slog.w(android.service.voice.AlwaysOnHotwordDetector.TAG, "stopRecognition() failed with error code " + code);
        }
        return code;
    }

    private void notifyStateChangedLocked() {
        android.os.Message message = android.os.Message.obtain(mHandler, android.service.voice.AlwaysOnHotwordDetector.MSG_AVAILABILITY_CHANGED);
        message.arg1 = mAvailability;
        message.sendToTarget();
    }

    /**
     *
     *
     * @unknown 
     */
    static final class SoundTriggerListener extends android.hardware.soundtrigger.IRecognitionStatusCallback.Stub {
        private final android.os.Handler mHandler;

        public SoundTriggerListener(android.os.Handler handler) {
            mHandler = handler;
        }

        @java.lang.Override
        public void onKeyphraseDetected(android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent event) {
            if (android.service.voice.AlwaysOnHotwordDetector.DBG) {
                android.util.Slog.d(android.service.voice.AlwaysOnHotwordDetector.TAG, ("onDetected(" + event) + ")");
            } else {
                android.util.Slog.i(android.service.voice.AlwaysOnHotwordDetector.TAG, "onDetected");
            }
            android.os.Message.obtain(mHandler, android.service.voice.AlwaysOnHotwordDetector.MSG_HOTWORD_DETECTED, new android.service.voice.AlwaysOnHotwordDetector.EventPayload(event.triggerInData, event.captureAvailable, event.captureFormat, event.captureSession, event.data)).sendToTarget();
        }

        @java.lang.Override
        public void onGenericSoundTriggerDetected(android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent event) {
            android.util.Slog.w(android.service.voice.AlwaysOnHotwordDetector.TAG, "Generic sound trigger event detected at AOHD: " + event);
        }

        @java.lang.Override
        public void onError(int status) {
            android.util.Slog.i(android.service.voice.AlwaysOnHotwordDetector.TAG, "onError: " + status);
            mHandler.sendEmptyMessage(android.service.voice.AlwaysOnHotwordDetector.MSG_DETECTION_ERROR);
        }

        @java.lang.Override
        public void onRecognitionPaused() {
            android.util.Slog.i(android.service.voice.AlwaysOnHotwordDetector.TAG, "onRecognitionPaused");
            mHandler.sendEmptyMessage(android.service.voice.AlwaysOnHotwordDetector.MSG_DETECTION_PAUSE);
        }

        @java.lang.Override
        public void onRecognitionResumed() {
            android.util.Slog.i(android.service.voice.AlwaysOnHotwordDetector.TAG, "onRecognitionResumed");
            mHandler.sendEmptyMessage(android.service.voice.AlwaysOnHotwordDetector.MSG_DETECTION_RESUME);
        }
    }

    class MyHandler extends android.os.Handler {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            synchronized(mLock) {
                if (mAvailability == android.service.voice.AlwaysOnHotwordDetector.STATE_INVALID) {
                    android.util.Slog.w(android.service.voice.AlwaysOnHotwordDetector.TAG, ("Received message: " + msg.what) + " for an invalid detector");
                    return;
                }
            }
            switch (msg.what) {
                case android.service.voice.AlwaysOnHotwordDetector.MSG_AVAILABILITY_CHANGED :
                    mExternalCallback.onAvailabilityChanged(msg.arg1);
                    break;
                case android.service.voice.AlwaysOnHotwordDetector.MSG_HOTWORD_DETECTED :
                    mExternalCallback.onDetected(((android.service.voice.AlwaysOnHotwordDetector.EventPayload) (msg.obj)));
                    break;
                case android.service.voice.AlwaysOnHotwordDetector.MSG_DETECTION_ERROR :
                    mExternalCallback.onError();
                    break;
                case android.service.voice.AlwaysOnHotwordDetector.MSG_DETECTION_PAUSE :
                    mExternalCallback.onRecognitionPaused();
                    break;
                case android.service.voice.AlwaysOnHotwordDetector.MSG_DETECTION_RESUME :
                    mExternalCallback.onRecognitionResumed();
                    break;
                default :
                    super.handleMessage(msg);
            }
        }
    }

    class RefreshAvailabiltyTask extends android.os.AsyncTask<java.lang.Void, java.lang.Void, java.lang.Void> {
        @java.lang.Override
        public java.lang.Void doInBackground(java.lang.Void... params) {
            int availability = internalGetInitialAvailability();
            boolean enrolled = false;
            // Fetch the sound model if the availability is one of the supported ones.
            if (((availability == android.service.voice.AlwaysOnHotwordDetector.STATE_NOT_READY) || (availability == android.service.voice.AlwaysOnHotwordDetector.STATE_KEYPHRASE_UNENROLLED)) || (availability == android.service.voice.AlwaysOnHotwordDetector.STATE_KEYPHRASE_ENROLLED)) {
                enrolled = internalGetIsEnrolled(mKeyphraseMetadata.id, mLocale);
                if (!enrolled) {
                    availability = android.service.voice.AlwaysOnHotwordDetector.STATE_KEYPHRASE_UNENROLLED;
                } else {
                    availability = android.service.voice.AlwaysOnHotwordDetector.STATE_KEYPHRASE_ENROLLED;
                }
            }
            synchronized(mLock) {
                if (android.service.voice.AlwaysOnHotwordDetector.DBG) {
                    android.util.Slog.d(android.service.voice.AlwaysOnHotwordDetector.TAG, (("Hotword availability changed from " + mAvailability) + " -> ") + availability);
                }
                mAvailability = availability;
                notifyStateChangedLocked();
            }
            return null;
        }

        /**
         *
         *
         * @return The initial availability without checking the enrollment status.
         */
        private int internalGetInitialAvailability() {
            synchronized(mLock) {
                // This detector has already been invalidated.
                if (mAvailability == android.service.voice.AlwaysOnHotwordDetector.STATE_INVALID) {
                    return android.service.voice.AlwaysOnHotwordDetector.STATE_INVALID;
                }
            }
            android.hardware.soundtrigger.SoundTrigger.ModuleProperties dspModuleProperties = null;
            try {
                dspModuleProperties = mModelManagementService.getDspModuleProperties(mVoiceInteractionService);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(android.service.voice.AlwaysOnHotwordDetector.TAG, "RemoteException in getDspProperties!", e);
            }
            // No DSP available
            if (dspModuleProperties == null) {
                return android.service.voice.AlwaysOnHotwordDetector.STATE_HARDWARE_UNAVAILABLE;
            }
            // No enrollment application supports this keyphrase/locale
            if (mKeyphraseMetadata == null) {
                return android.service.voice.AlwaysOnHotwordDetector.STATE_KEYPHRASE_UNSUPPORTED;
            }
            return android.service.voice.AlwaysOnHotwordDetector.STATE_NOT_READY;
        }

        /**
         *
         *
         * @return The corresponding {@link KeyphraseSoundModel} or null if none is found.
         */
        private boolean internalGetIsEnrolled(int keyphraseId, java.util.Locale locale) {
            try {
                return mModelManagementService.isEnrolledForKeyphrase(mVoiceInteractionService, keyphraseId, locale.toLanguageTag());
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(android.service.voice.AlwaysOnHotwordDetector.TAG, "RemoteException in listRegisteredKeyphraseSoundModels!", e);
            }
            return false;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        synchronized(mLock) {
            pw.print(prefix);
            pw.print("Text=");
            pw.println(mText);
            pw.print(prefix);
            pw.print("Locale=");
            pw.println(mLocale);
            pw.print(prefix);
            pw.print("Availability=");
            pw.println(mAvailability);
            pw.print(prefix);
            pw.print("KeyphraseMetadata=");
            pw.println(mKeyphraseMetadata);
            pw.print(prefix);
            pw.print("EnrollmentInfo=");
            pw.println(mKeyphraseEnrollmentInfo);
        }
    }
}

