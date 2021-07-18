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
package android.media.audiopolicy;


/**
 *
 *
 * @unknown AudioPolicy provides access to the management of audio routing and audio focus.
 */
@android.annotation.SystemApi
public class AudioPolicy {
    private static final java.lang.String TAG = "AudioPolicy";

    private static final boolean DEBUG = false;

    private final java.lang.Object mLock = new java.lang.Object();

    /**
     * The status of an audio policy that is valid but cannot be used because it is not registered.
     */
    @android.annotation.SystemApi
    public static final int POLICY_STATUS_UNREGISTERED = 1;

    /**
     * The status of an audio policy that is valid, successfully registered and thus active.
     */
    @android.annotation.SystemApi
    public static final int POLICY_STATUS_REGISTERED = 2;

    private int mStatus;

    private java.lang.String mRegistrationId;

    private android.media.audiopolicy.AudioPolicy.AudioPolicyStatusListener mStatusListener;

    /**
     * The behavior of a policy with regards to audio focus where it relies on the application
     * to do the ducking, the is the legacy and default behavior.
     */
    @android.annotation.SystemApi
    public static final int FOCUS_POLICY_DUCKING_IN_APP = 0;

    public static final int FOCUS_POLICY_DUCKING_DEFAULT = android.media.audiopolicy.AudioPolicy.FOCUS_POLICY_DUCKING_IN_APP;

    /**
     * The behavior of a policy with regards to audio focus where it handles ducking instead
     * of the application losing focus and being signaled it can duck (as communicated by
     * {@link android.media.AudioManager#AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK}).
     * <br>Can only be used after having set a listener with
     * {@link AudioPolicy#setAudioPolicyFocusListener(AudioPolicyFocusListener)}.
     */
    @android.annotation.SystemApi
    public static final int FOCUS_POLICY_DUCKING_IN_POLICY = 1;

    private android.media.audiopolicy.AudioPolicy.AudioPolicyFocusListener mFocusListener;

    private android.content.Context mContext;

    private android.media.audiopolicy.AudioPolicyConfig mConfig;

    /**
     *
     *
     * @unknown 
     */
    public android.media.audiopolicy.AudioPolicyConfig getConfig() {
        return mConfig;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean hasFocusListener() {
        return mFocusListener != null;
    }

    /**
     * The parameter is guaranteed non-null through the Builder
     */
    private AudioPolicy(android.media.audiopolicy.AudioPolicyConfig config, android.content.Context context, android.os.Looper looper, android.media.audiopolicy.AudioPolicy.AudioPolicyFocusListener fl, android.media.audiopolicy.AudioPolicy.AudioPolicyStatusListener sl) {
        mConfig = config;
        mStatus = android.media.audiopolicy.AudioPolicy.POLICY_STATUS_UNREGISTERED;
        mContext = context;
        if (looper == null) {
            looper = android.os.Looper.getMainLooper();
        }
        if (looper != null) {
            mEventHandler = new android.media.audiopolicy.AudioPolicy.EventHandler(this, looper);
        } else {
            mEventHandler = null;
            android.util.Log.e(android.media.audiopolicy.AudioPolicy.TAG, "No event handler due to looper without a thread");
        }
        mFocusListener = fl;
        mStatusListener = sl;
    }

    /**
     * Builder class for {@link AudioPolicy} objects
     */
    @android.annotation.SystemApi
    public static class Builder {
        private java.util.ArrayList<android.media.audiopolicy.AudioMix> mMixes;

        private android.content.Context mContext;

        private android.os.Looper mLooper;

        private android.media.audiopolicy.AudioPolicy.AudioPolicyFocusListener mFocusListener;

        private android.media.audiopolicy.AudioPolicy.AudioPolicyStatusListener mStatusListener;

        /**
         * Constructs a new Builder with no audio mixes.
         *
         * @param context
         * 		the context for the policy
         */
        @android.annotation.SystemApi
        public Builder(android.content.Context context) {
            mMixes = new java.util.ArrayList<android.media.audiopolicy.AudioMix>();
            mContext = context;
        }

        /**
         * Add an {@link AudioMix} to be part of the audio policy being built.
         *
         * @param mix
         * 		a non-null {@link AudioMix} to be part of the audio policy.
         * @return the same Builder instance.
         * @throws IllegalArgumentException
         * 		
         */
        @android.annotation.SystemApi
        public android.media.audiopolicy.AudioPolicy.Builder addMix(@android.annotation.NonNull
        android.media.audiopolicy.AudioMix mix) throws java.lang.IllegalArgumentException {
            if (mix == null) {
                throw new java.lang.IllegalArgumentException("Illegal null AudioMix argument");
            }
            mMixes.add(mix);
            return this;
        }

        /**
         * Sets the {@link Looper} on which to run the event loop.
         *
         * @param looper
         * 		a non-null specific Looper.
         * @return the same Builder instance.
         * @throws IllegalArgumentException
         * 		
         */
        @android.annotation.SystemApi
        public android.media.audiopolicy.AudioPolicy.Builder setLooper(@android.annotation.NonNull
        android.os.Looper looper) throws java.lang.IllegalArgumentException {
            if (looper == null) {
                throw new java.lang.IllegalArgumentException("Illegal null Looper argument");
            }
            mLooper = looper;
            return this;
        }

        /**
         * Sets the audio focus listener for the policy.
         *
         * @param l
         * 		a {@link AudioPolicy.AudioPolicyFocusListener}
         */
        @android.annotation.SystemApi
        public void setAudioPolicyFocusListener(android.media.audiopolicy.AudioPolicy.AudioPolicyFocusListener l) {
            mFocusListener = l;
        }

        /**
         * Sets the audio policy status listener.
         *
         * @param l
         * 		a {@link AudioPolicy.AudioPolicyStatusListener}
         */
        @android.annotation.SystemApi
        public void setAudioPolicyStatusListener(android.media.audiopolicy.AudioPolicy.AudioPolicyStatusListener l) {
            mStatusListener = l;
        }

        @android.annotation.SystemApi
        public android.media.audiopolicy.AudioPolicy build() {
            if (mStatusListener != null) {
                // the AudioPolicy status listener includes updates on each mix activity state
                for (android.media.audiopolicy.AudioMix mix : mMixes) {
                    mix.mCallbackFlags |= android.media.audiopolicy.AudioMix.CALLBACK_FLAG_NOTIFY_ACTIVITY;
                }
            }
            return new android.media.audiopolicy.AudioPolicy(new android.media.audiopolicy.AudioPolicyConfig(mMixes), mContext, mLooper, mFocusListener, mStatusListener);
        }
    }

    public void setRegistration(java.lang.String regId) {
        synchronized(mLock) {
            mRegistrationId = regId;
            mConfig.setRegistration(regId);
            if (regId != null) {
                mStatus = android.media.audiopolicy.AudioPolicy.POLICY_STATUS_REGISTERED;
            } else {
                mStatus = android.media.audiopolicy.AudioPolicy.POLICY_STATUS_UNREGISTERED;
            }
        }
        sendMsg(android.media.audiopolicy.AudioPolicy.MSG_POLICY_STATUS_CHANGE);
    }

    private boolean policyReadyToUse() {
        synchronized(mLock) {
            if (mStatus != android.media.audiopolicy.AudioPolicy.POLICY_STATUS_REGISTERED) {
                android.util.Log.e(android.media.audiopolicy.AudioPolicy.TAG, "Cannot use unregistered AudioPolicy");
                return false;
            }
            if (mContext == null) {
                android.util.Log.e(android.media.audiopolicy.AudioPolicy.TAG, "Cannot use AudioPolicy without context");
                return false;
            }
            if (mRegistrationId == null) {
                android.util.Log.e(android.media.audiopolicy.AudioPolicy.TAG, "Cannot use unregistered AudioPolicy");
                return false;
            }
        }
        if (!(android.content.pm.PackageManager.PERMISSION_GRANTED == mContext.checkCallingOrSelfPermission(android.Manifest.permission.MODIFY_AUDIO_ROUTING))) {
            android.util.Slog.w(android.media.audiopolicy.AudioPolicy.TAG, ((("Cannot use AudioPolicy for pid " + android.os.Binder.getCallingPid()) + " / uid ") + android.os.Binder.getCallingUid()) + ", needs MODIFY_AUDIO_ROUTING");
            return false;
        }
        return true;
    }

    private void checkMixReadyToUse(android.media.audiopolicy.AudioMix mix, boolean forTrack) throws java.lang.IllegalArgumentException {
        if (mix == null) {
            java.lang.String msg = (forTrack) ? "Invalid null AudioMix for AudioTrack creation" : "Invalid null AudioMix for AudioRecord creation";
            throw new java.lang.IllegalArgumentException(msg);
        }
        if (!mConfig.mMixes.contains(mix)) {
            throw new java.lang.IllegalArgumentException("Invalid mix: not part of this policy");
        }
        if ((mix.getRouteFlags() & android.media.audiopolicy.AudioMix.ROUTE_FLAG_LOOP_BACK) != android.media.audiopolicy.AudioMix.ROUTE_FLAG_LOOP_BACK) {
            throw new java.lang.IllegalArgumentException("Invalid AudioMix: not defined for loop back");
        }
        if (forTrack && (mix.getMixType() != android.media.audiopolicy.AudioMix.MIX_TYPE_RECORDERS)) {
            throw new java.lang.IllegalArgumentException("Invalid AudioMix: not defined for being a recording source");
        }
        if ((!forTrack) && (mix.getMixType() != android.media.audiopolicy.AudioMix.MIX_TYPE_PLAYERS)) {
            throw new java.lang.IllegalArgumentException("Invalid AudioMix: not defined for capturing playback");
        }
    }

    /**
     * Returns the current behavior for audio focus-related ducking.
     *
     * @return {@link #FOCUS_POLICY_DUCKING_IN_APP} or {@link #FOCUS_POLICY_DUCKING_IN_POLICY}
     */
    @android.annotation.SystemApi
    public int getFocusDuckingBehavior() {
        return mConfig.mDuckingPolicy;
    }

    // Note on implementation: not part of the Builder as there can be only one registered policy
    // that handles ducking but there can be multiple policies
    /**
     * Sets the behavior for audio focus-related ducking.
     * There must be a focus listener if this policy is to handle ducking.
     *
     * @param behavior
     * 		{@link #FOCUS_POLICY_DUCKING_IN_APP} or
     * 		{@link #FOCUS_POLICY_DUCKING_IN_POLICY}
     * @return {@link AudioManager#SUCCESS} or {@link AudioManager#ERROR} (for instance if there
    is already an audio policy that handles ducking).
     * @throws IllegalArgumentException
     * 		
     * @throws IllegalStateException
     * 		
     */
    @android.annotation.SystemApi
    public int setFocusDuckingBehavior(int behavior) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException {
        if ((behavior != android.media.audiopolicy.AudioPolicy.FOCUS_POLICY_DUCKING_IN_APP) && (behavior != android.media.audiopolicy.AudioPolicy.FOCUS_POLICY_DUCKING_IN_POLICY)) {
            throw new java.lang.IllegalArgumentException("Invalid ducking behavior " + behavior);
        }
        synchronized(mLock) {
            if (mStatus != android.media.audiopolicy.AudioPolicy.POLICY_STATUS_REGISTERED) {
                throw new java.lang.IllegalStateException("Cannot change ducking behavior for unregistered policy");
            }
            if ((behavior == android.media.audiopolicy.AudioPolicy.FOCUS_POLICY_DUCKING_IN_POLICY) && (mFocusListener == null)) {
                // there must be a focus listener if the policy handles ducking
                throw new java.lang.IllegalStateException("Cannot handle ducking without an audio focus listener");
            }
            android.media.IAudioService service = android.media.audiopolicy.AudioPolicy.getService();
            try {
                final int status = /* duckingBehavior */
                service.setFocusPropertiesForPolicy(behavior, this.cb());
                if (status == android.media.AudioManager.SUCCESS) {
                    mConfig.mDuckingPolicy = behavior;
                }
                return status;
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.media.audiopolicy.AudioPolicy.TAG, "Dead object in setFocusPropertiesForPolicy for behavior", e);
                return android.media.AudioManager.ERROR;
            }
        }
    }

    /**
     * Create an {@link AudioRecord} instance that is associated with the given {@link AudioMix}.
     * Audio buffers recorded through the created instance will contain the mix of the audio
     * streams that fed the given mixer.
     *
     * @param mix
     * 		a non-null {@link AudioMix} instance whose routing flags was defined with
     * 		{@link AudioMix#ROUTE_FLAG_LOOP_BACK}, previously added to this policy.
     * @return a new {@link AudioRecord} instance whose data format is the one defined in the
    {@link AudioMix}, or null if this policy was not successfully registered
    with {@link AudioManager#registerAudioPolicy(AudioPolicy)}.
     * @throws IllegalArgumentException
     * 		
     */
    @android.annotation.SystemApi
    public android.media.AudioRecord createAudioRecordSink(android.media.audiopolicy.AudioMix mix) throws java.lang.IllegalArgumentException {
        if (!policyReadyToUse()) {
            android.util.Log.e(android.media.audiopolicy.AudioPolicy.TAG, "Cannot create AudioRecord sink for AudioMix");
            return null;
        }
        /* not for an AudioTrack */
        checkMixReadyToUse(mix, false);
        // create an AudioFormat from the mix format compatible with recording, as the mix
        // was defined for playback
        android.media.AudioFormat mixFormat = new android.media.AudioFormat.Builder(mix.getFormat()).setChannelMask(android.media.AudioFormat.inChannelMaskFromOutChannelMask(mix.getFormat().getChannelMask())).build();
        // create the AudioRecord, configured for loop back, using the same format as the mix
        android.media.AudioRecord ar = new android.media.AudioRecord(new android.media.AudioAttributes.Builder().setInternalCapturePreset(android.media.MediaRecorder.AudioSource.REMOTE_SUBMIX).addTag(android.media.audiopolicy.AudioPolicy.addressForTag(mix)).build(), mixFormat, // using stereo for buffer size to avoid the current poor support for masks
        android.media.AudioRecord.getMinBufferSize(mix.getFormat().getSampleRate(), android.media.AudioFormat.CHANNEL_IN_STEREO, mix.getFormat().getEncoding()), android.media.AudioManager.AUDIO_SESSION_ID_GENERATE);
        return ar;
    }

    /**
     * Create an {@link AudioTrack} instance that is associated with the given {@link AudioMix}.
     * Audio buffers played through the created instance will be sent to the given mix
     * to be recorded through the recording APIs.
     *
     * @param mix
     * 		a non-null {@link AudioMix} instance whose routing flags was defined with
     * 		{@link AudioMix#ROUTE_FLAG_LOOP_BACK}, previously added to this policy.
     * @return a new {@link AudioTrack} instance whose data format is the one defined in the
    {@link AudioMix}, or null if this policy was not successfully registered
    with {@link AudioManager#registerAudioPolicy(AudioPolicy)}.
     * @throws IllegalArgumentException
     * 		
     */
    @android.annotation.SystemApi
    public android.media.AudioTrack createAudioTrackSource(android.media.audiopolicy.AudioMix mix) throws java.lang.IllegalArgumentException {
        if (!policyReadyToUse()) {
            android.util.Log.e(android.media.audiopolicy.AudioPolicy.TAG, "Cannot create AudioTrack source for AudioMix");
            return null;
        }
        /* for an AudioTrack */
        checkMixReadyToUse(mix, true);
        // create the AudioTrack, configured for loop back, using the same format as the mix
        android.media.AudioTrack at = new android.media.AudioTrack(new android.media.AudioAttributes.Builder().setUsage(android.media.AudioAttributes.USAGE_VIRTUAL_SOURCE).addTag(android.media.audiopolicy.AudioPolicy.addressForTag(mix)).build(), mix.getFormat(), android.media.AudioTrack.getMinBufferSize(mix.getFormat().getSampleRate(), mix.getFormat().getChannelMask(), mix.getFormat().getEncoding()), android.media.AudioTrack.MODE_STREAM, android.media.AudioManager.AUDIO_SESSION_ID_GENERATE);
        return at;
    }

    @android.annotation.SystemApi
    public int getStatus() {
        return mStatus;
    }

    @android.annotation.SystemApi
    public static abstract class AudioPolicyStatusListener {
        public void onStatusChange() {
        }

        public void onMixStateUpdate(android.media.audiopolicy.AudioMix mix) {
        }
    }

    @android.annotation.SystemApi
    public static abstract class AudioPolicyFocusListener {
        public void onAudioFocusGrant(android.media.AudioFocusInfo afi, int requestResult) {
        }

        public void onAudioFocusLoss(android.media.AudioFocusInfo afi, boolean wasNotified) {
        }
    }

    private void onPolicyStatusChange() {
        android.media.audiopolicy.AudioPolicy.AudioPolicyStatusListener l;
        synchronized(mLock) {
            if (mStatusListener == null) {
                return;
            }
            l = mStatusListener;
        }
        l.onStatusChange();
    }

    // ==================================================
    // Callback interface
    /**
     *
     *
     * @unknown 
     */
    public android.media.audiopolicy.IAudioPolicyCallback cb() {
        return mPolicyCb;
    }

    private final android.media.audiopolicy.IAudioPolicyCallback mPolicyCb = new android.media.audiopolicy.IAudioPolicyCallback.Stub() {
        public void notifyAudioFocusGrant(android.media.AudioFocusInfo afi, int requestResult) {
            sendMsg(android.media.audiopolicy.AudioPolicy.MSG_FOCUS_GRANT, afi, requestResult);
            if (android.media.audiopolicy.AudioPolicy.DEBUG) {
                android.util.Log.v(android.media.audiopolicy.AudioPolicy.TAG, (((("notifyAudioFocusGrant: pack=" + afi.getPackageName()) + " client=") + afi.getClientId()) + "reqRes=") + requestResult);
            }
        }

        public void notifyAudioFocusLoss(android.media.AudioFocusInfo afi, boolean wasNotified) {
            sendMsg(android.media.audiopolicy.AudioPolicy.MSG_FOCUS_LOSS, afi, wasNotified ? 1 : 0);
            if (android.media.audiopolicy.AudioPolicy.DEBUG) {
                android.util.Log.v(android.media.audiopolicy.AudioPolicy.TAG, (((("notifyAudioFocusLoss: pack=" + afi.getPackageName()) + " client=") + afi.getClientId()) + "wasNotified=") + wasNotified);
            }
        }

        public void notifyMixStateUpdate(java.lang.String regId, int state) {
            for (android.media.audiopolicy.AudioMix mix : mConfig.getMixes()) {
                if (mix.getRegistration().equals(regId)) {
                    mix.mMixState = state;
                    /* ignored */
                    sendMsg(android.media.audiopolicy.AudioPolicy.MSG_MIX_STATE_UPDATE, mix, 0);
                    if (android.media.audiopolicy.AudioPolicy.DEBUG) {
                        android.util.Log.v(android.media.audiopolicy.AudioPolicy.TAG, (("notifyMixStateUpdate: regId=" + regId) + " state=") + state);
                    }
                }
            }
        }
    };

    // ==================================================
    // Event handling
    private final android.media.audiopolicy.AudioPolicy.EventHandler mEventHandler;

    private static final int MSG_POLICY_STATUS_CHANGE = 0;

    private static final int MSG_FOCUS_GRANT = 1;

    private static final int MSG_FOCUS_LOSS = 2;

    private static final int MSG_MIX_STATE_UPDATE = 3;

    private class EventHandler extends android.os.Handler {
        public EventHandler(android.media.audiopolicy.AudioPolicy ap, android.os.Looper looper) {
            super(looper);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.media.audiopolicy.AudioPolicy.MSG_POLICY_STATUS_CHANGE :
                    onPolicyStatusChange();
                    break;
                case android.media.audiopolicy.AudioPolicy.MSG_FOCUS_GRANT :
                    if (mFocusListener != null) {
                        mFocusListener.onAudioFocusGrant(((android.media.AudioFocusInfo) (msg.obj)), msg.arg1);
                    }
                    break;
                case android.media.audiopolicy.AudioPolicy.MSG_FOCUS_LOSS :
                    if (mFocusListener != null) {
                        mFocusListener.onAudioFocusLoss(((android.media.AudioFocusInfo) (msg.obj)), msg.arg1 != 0);
                    }
                    break;
                case android.media.audiopolicy.AudioPolicy.MSG_MIX_STATE_UPDATE :
                    if (mStatusListener != null) {
                        mStatusListener.onMixStateUpdate(((android.media.audiopolicy.AudioMix) (msg.obj)));
                    }
                    break;
                default :
                    android.util.Log.e(android.media.audiopolicy.AudioPolicy.TAG, "Unknown event " + msg.what);
            }
        }
    }

    // ==========================================================
    // Utils
    private static java.lang.String addressForTag(android.media.audiopolicy.AudioMix mix) {
        return "addr=" + mix.getRegistration();
    }

    private void sendMsg(int msg) {
        if (mEventHandler != null) {
            mEventHandler.sendEmptyMessage(msg);
        }
    }

    private void sendMsg(int msg, java.lang.Object obj, int i) {
        if (mEventHandler != null) {
            mEventHandler.sendMessage(/* arg1 */
            /* arg2, ignored */
            mEventHandler.obtainMessage(msg, i, 0, obj));
        }
    }

    private static android.media.IAudioService sService;

    private static android.media.IAudioService getService() {
        if (android.media.audiopolicy.AudioPolicy.sService != null) {
            return android.media.audiopolicy.AudioPolicy.sService;
        }
        android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.AUDIO_SERVICE);
        android.media.audiopolicy.AudioPolicy.sService = IAudioService.Stub.asInterface(b);
        return android.media.audiopolicy.AudioPolicy.sService;
    }

    public java.lang.String toLogFriendlyString() {
        java.lang.String textDump = new java.lang.String("android.media.audiopolicy.AudioPolicy:\n");
        textDump += "config=" + mConfig.toLogFriendlyString();
        return textDump;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.media.audiopolicy.AudioPolicy.POLICY_STATUS_REGISTERED, android.media.audiopolicy.AudioPolicy.POLICY_STATUS_UNREGISTERED })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface PolicyStatus {}
}

