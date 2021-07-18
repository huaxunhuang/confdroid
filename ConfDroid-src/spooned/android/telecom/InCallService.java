/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.telecom;


/**
 * This service is implemented by any app that wishes to provide the user-interface for managing
 * phone calls. Telecom binds to this service while there exists a live (active or incoming) call,
 * and uses it to notify the in-call app of any live and recently disconnected calls. An app must
 * first be set as the default phone app (See {@link TelecomManager#getDefaultDialerPackage()})
 * before the telecom service will bind to its {@code InCallService} implementation.
 * <p>
 * Below is an example manifest registration for an {@code InCallService}. The meta-data
 * ({@link TelecomManager#METADATA_IN_CALL_SERVICE_UI}) indicates that this particular
 * {@code InCallService} implementation intends to replace the built-in in-call UI.
 * <pre>
 * {@code <service android:name="your.package.YourInCallServiceImplementation"
 *          android:permission="android.permission.BIND_INCALL_SERVICE">
 *      <meta-data android:name="android.telecom.IN_CALL_SERVICE_UI" android:value="true" />
 *      <intent-filter>
 *          <action android:name="android.telecom.InCallService"/>
 *      </intent-filter>
 * </service>}
 * </pre>
 */
public abstract class InCallService extends android.app.Service {
    /**
     * The {@link Intent} that must be declared as handled by the service.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.SERVICE_ACTION)
    public static final java.lang.String SERVICE_INTERFACE = "android.telecom.InCallService";

    private static final int MSG_SET_IN_CALL_ADAPTER = 1;

    private static final int MSG_ADD_CALL = 2;

    private static final int MSG_UPDATE_CALL = 3;

    private static final int MSG_SET_POST_DIAL_WAIT = 4;

    private static final int MSG_ON_CALL_AUDIO_STATE_CHANGED = 5;

    private static final int MSG_BRING_TO_FOREGROUND = 6;

    private static final int MSG_ON_CAN_ADD_CALL_CHANGED = 7;

    private static final int MSG_SILENCE_RINGER = 8;

    private static final int MSG_ON_CONNECTION_EVENT = 9;

    /**
     * Default Handler used to consolidate binder method calls onto a single thread.
     */
    private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper()) {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            if ((mPhone == null) && (msg.what != android.telecom.InCallService.MSG_SET_IN_CALL_ADAPTER)) {
                return;
            }
            switch (msg.what) {
                case android.telecom.InCallService.MSG_SET_IN_CALL_ADAPTER :
                    mPhone = new android.telecom.Phone(new android.telecom.InCallAdapter(((com.android.internal.telecom.IInCallAdapter) (msg.obj))));
                    mPhone.addListener(mPhoneListener);
                    onPhoneCreated(mPhone);
                    break;
                case android.telecom.InCallService.MSG_ADD_CALL :
                    mPhone.internalAddCall(((android.telecom.ParcelableCall) (msg.obj)));
                    break;
                case android.telecom.InCallService.MSG_UPDATE_CALL :
                    mPhone.internalUpdateCall(((android.telecom.ParcelableCall) (msg.obj)));
                    break;
                case android.telecom.InCallService.MSG_SET_POST_DIAL_WAIT :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            java.lang.String callId = ((java.lang.String) (args.arg1));
                            java.lang.String remaining = ((java.lang.String) (args.arg2));
                            mPhone.internalSetPostDialWait(callId, remaining);
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.InCallService.MSG_ON_CALL_AUDIO_STATE_CHANGED :
                    mPhone.internalCallAudioStateChanged(((android.telecom.CallAudioState) (msg.obj)));
                    break;
                case android.telecom.InCallService.MSG_BRING_TO_FOREGROUND :
                    mPhone.internalBringToForeground(msg.arg1 == 1);
                    break;
                case android.telecom.InCallService.MSG_ON_CAN_ADD_CALL_CHANGED :
                    mPhone.internalSetCanAddCall(msg.arg1 == 1);
                    break;
                case android.telecom.InCallService.MSG_SILENCE_RINGER :
                    mPhone.internalSilenceRinger();
                    break;
                case android.telecom.InCallService.MSG_ON_CONNECTION_EVENT :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            java.lang.String callId = ((java.lang.String) (args.arg1));
                            java.lang.String event = ((java.lang.String) (args.arg2));
                            android.os.Bundle extras = ((android.os.Bundle) (args.arg3));
                            mPhone.internalOnConnectionEvent(callId, event, extras);
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                default :
                    break;
            }
        }
    };

    /**
     * Manages the binder calls so that the implementor does not need to deal with it.
     */
    private final class InCallServiceBinder extends com.android.internal.telecom.IInCallService.Stub {
        @java.lang.Override
        public void setInCallAdapter(com.android.internal.telecom.IInCallAdapter inCallAdapter) {
            mHandler.obtainMessage(android.telecom.InCallService.MSG_SET_IN_CALL_ADAPTER, inCallAdapter).sendToTarget();
        }

        @java.lang.Override
        public void addCall(android.telecom.ParcelableCall call) {
            mHandler.obtainMessage(android.telecom.InCallService.MSG_ADD_CALL, call).sendToTarget();
        }

        @java.lang.Override
        public void updateCall(android.telecom.ParcelableCall call) {
            mHandler.obtainMessage(android.telecom.InCallService.MSG_UPDATE_CALL, call).sendToTarget();
        }

        @java.lang.Override
        public void setPostDial(java.lang.String callId, java.lang.String remaining) {
            // TODO: Unused
        }

        @java.lang.Override
        public void setPostDialWait(java.lang.String callId, java.lang.String remaining) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = callId;
            args.arg2 = remaining;
            mHandler.obtainMessage(android.telecom.InCallService.MSG_SET_POST_DIAL_WAIT, args).sendToTarget();
        }

        @java.lang.Override
        public void onCallAudioStateChanged(android.telecom.CallAudioState callAudioState) {
            mHandler.obtainMessage(android.telecom.InCallService.MSG_ON_CALL_AUDIO_STATE_CHANGED, callAudioState).sendToTarget();
        }

        @java.lang.Override
        public void bringToForeground(boolean showDialpad) {
            mHandler.obtainMessage(android.telecom.InCallService.MSG_BRING_TO_FOREGROUND, showDialpad ? 1 : 0, 0).sendToTarget();
        }

        @java.lang.Override
        public void onCanAddCallChanged(boolean canAddCall) {
            mHandler.obtainMessage(android.telecom.InCallService.MSG_ON_CAN_ADD_CALL_CHANGED, canAddCall ? 1 : 0, 0).sendToTarget();
        }

        @java.lang.Override
        public void silenceRinger() {
            mHandler.obtainMessage(android.telecom.InCallService.MSG_SILENCE_RINGER).sendToTarget();
        }

        @java.lang.Override
        public void onConnectionEvent(java.lang.String callId, java.lang.String event, android.os.Bundle extras) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = callId;
            args.arg2 = event;
            args.arg3 = extras;
            mHandler.obtainMessage(android.telecom.InCallService.MSG_ON_CONNECTION_EVENT, args).sendToTarget();
        }
    }

    private android.telecom.Phone.Listener mPhoneListener = new android.telecom.Phone.Listener() {
        /**
         * ${inheritDoc}
         */
        @java.lang.Override
        public void onAudioStateChanged(android.telecom.Phone phone, android.telecom.AudioState audioState) {
            android.telecom.InCallService.this.onAudioStateChanged(audioState);
        }

        public void onCallAudioStateChanged(android.telecom.Phone phone, android.telecom.CallAudioState callAudioState) {
            android.telecom.InCallService.this.onCallAudioStateChanged(callAudioState);
        }

        /**
         * ${inheritDoc}
         */
        @java.lang.Override
        public void onBringToForeground(android.telecom.Phone phone, boolean showDialpad) {
            android.telecom.InCallService.this.onBringToForeground(showDialpad);
        }

        /**
         * ${inheritDoc}
         */
        @java.lang.Override
        public void onCallAdded(android.telecom.Phone phone, android.telecom.Call call) {
            android.telecom.InCallService.this.onCallAdded(call);
        }

        /**
         * ${inheritDoc}
         */
        @java.lang.Override
        public void onCallRemoved(android.telecom.Phone phone, android.telecom.Call call) {
            android.telecom.InCallService.this.onCallRemoved(call);
        }

        /**
         * ${inheritDoc}
         */
        @java.lang.Override
        public void onCanAddCallChanged(android.telecom.Phone phone, boolean canAddCall) {
            android.telecom.InCallService.this.onCanAddCallChanged(canAddCall);
        }

        /**
         * ${inheritDoc}
         */
        @java.lang.Override
        public void onSilenceRinger(android.telecom.Phone phone) {
            android.telecom.InCallService.this.onSilenceRinger();
        }
    };

    private android.telecom.Phone mPhone;

    public InCallService() {
    }

    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        return new android.telecom.InCallService.InCallServiceBinder();
    }

    @java.lang.Override
    public boolean onUnbind(android.content.Intent intent) {
        if (mPhone != null) {
            android.telecom.Phone oldPhone = mPhone;
            mPhone = null;
            oldPhone.destroy();
            // destroy sets all the calls to disconnected if any live ones still exist. Therefore,
            // it is important to remove the Listener *after* the call to destroy so that
            // InCallService.on* callbacks are appropriately called.
            oldPhone.removeListener(mPhoneListener);
            onPhoneDestroyed(oldPhone);
        }
        return false;
    }

    /**
     * Obtain the {@code Phone} associated with this {@code InCallService}.
     *
     * @return The {@code Phone} object associated with this {@code InCallService}, or {@code null}
    if the {@code InCallService} is not in a state where it has an associated
    {@code Phone}.
     * @unknown 
     * @deprecated Use direct methods on InCallService instead of {@link Phone}.
     */
    @android.annotation.SystemApi
    @java.lang.Deprecated
    public android.telecom.Phone getPhone() {
        return mPhone;
    }

    /**
     * Obtains the current list of {@code Call}s to be displayed by this in-call service.
     *
     * @return A list of the relevant {@code Call}s.
     */
    public final java.util.List<android.telecom.Call> getCalls() {
        return mPhone == null ? java.util.Collections.<android.telecom.Call>emptyList() : mPhone.getCalls();
    }

    /**
     * Returns if the device can support additional calls.
     *
     * @return Whether the phone supports adding more calls.
     */
    public final boolean canAddCall() {
        return mPhone == null ? false : mPhone.canAddCall();
    }

    /**
     * Obtains the current phone call audio state.
     *
     * @return An object encapsulating the audio state. Returns null if the service is not
    fully initialized.
     * @deprecated Use {@link #getCallAudioState()} instead.
     * @unknown 
     */
    @java.lang.Deprecated
    public final android.telecom.AudioState getAudioState() {
        return mPhone == null ? null : mPhone.getAudioState();
    }

    /**
     * Obtains the current phone call audio state.
     *
     * @return An object encapsulating the audio state. Returns null if the service is not
    fully initialized.
     */
    public final android.telecom.CallAudioState getCallAudioState() {
        return mPhone == null ? null : mPhone.getCallAudioState();
    }

    /**
     * Sets the microphone mute state. When this request is honored, there will be change to
     * the {@link #getCallAudioState()}.
     *
     * @param state
     * 		{@code true} if the microphone should be muted; {@code false} otherwise.
     */
    public final void setMuted(boolean state) {
        if (mPhone != null) {
            mPhone.setMuted(state);
        }
    }

    /**
     * Sets the audio route (speaker, bluetooth, etc...).  When this request is honored, there will
     * be change to the {@link #getCallAudioState()}.
     *
     * @param route
     * 		The audio route to use.
     */
    public final void setAudioRoute(int route) {
        if (mPhone != null) {
            mPhone.setAudioRoute(route);
        }
    }

    /**
     * Invoked when the {@code Phone} has been created. This is a signal to the in-call experience
     * to start displaying in-call information to the user. Each instance of {@code InCallService}
     * will have only one {@code Phone}, and this method will be called exactly once in the lifetime
     * of the {@code InCallService}.
     *
     * @param phone
     * 		The {@code Phone} object associated with this {@code InCallService}.
     * @unknown 
     * @deprecated Use direct methods on InCallService instead of {@link Phone}.
     */
    @android.annotation.SystemApi
    @java.lang.Deprecated
    public void onPhoneCreated(android.telecom.Phone phone) {
    }

    /**
     * Invoked when a {@code Phone} has been destroyed. This is a signal to the in-call experience
     * to stop displaying in-call information to the user. This method will be called exactly once
     * in the lifetime of the {@code InCallService}, and it will always be called after a previous
     * call to {@link #onPhoneCreated(Phone)}.
     *
     * @param phone
     * 		The {@code Phone} object associated with this {@code InCallService}.
     * @unknown 
     * @deprecated Use direct methods on InCallService instead of {@link Phone}.
     */
    @android.annotation.SystemApi
    @java.lang.Deprecated
    public void onPhoneDestroyed(android.telecom.Phone phone) {
    }

    /**
     * Called when the audio state changes.
     *
     * @param audioState
     * 		The new {@link AudioState}.
     * @deprecated Use {@link #onCallAudioStateChanged(CallAudioState) instead}.
     * @unknown 
     */
    @java.lang.Deprecated
    public void onAudioStateChanged(android.telecom.AudioState audioState) {
    }

    /**
     * Called when the audio state changes.
     *
     * @param audioState
     * 		The new {@link CallAudioState}.
     */
    public void onCallAudioStateChanged(android.telecom.CallAudioState audioState) {
    }

    /**
     * Called to bring the in-call screen to the foreground. The in-call experience should
     * respond immediately by coming to the foreground to inform the user of the state of
     * ongoing {@code Call}s.
     *
     * @param showDialpad
     * 		If true, put up the dialpad when the screen is shown.
     */
    public void onBringToForeground(boolean showDialpad) {
    }

    /**
     * Called when a {@code Call} has been added to this in-call session. The in-call user
     * experience should add necessary state listeners to the specified {@code Call} and
     * immediately start to show the user information about the existence
     * and nature of this {@code Call}. Subsequent invocations of {@link #getCalls()} will
     * include this {@code Call}.
     *
     * @param call
     * 		A newly added {@code Call}.
     */
    public void onCallAdded(android.telecom.Call call) {
    }

    /**
     * Called when a {@code Call} has been removed from this in-call session. The in-call user
     * experience should remove any state listeners from the specified {@code Call} and
     * immediately stop displaying any information about this {@code Call}.
     * Subsequent invocations of {@link #getCalls()} will no longer include this {@code Call}.
     *
     * @param call
     * 		A newly removed {@code Call}.
     */
    public void onCallRemoved(android.telecom.Call call) {
    }

    /**
     * Called when the ability to add more calls changes.  If the phone cannot
     * support more calls then {@code canAddCall} is set to {@code false}.  If it can, then it
     * is set to {@code true}. This can be used to control the visibility of UI to add more calls.
     *
     * @param canAddCall
     * 		Indicates whether an additional call can be added.
     */
    public void onCanAddCallChanged(boolean canAddCall) {
    }

    /**
     * Called to silence the ringer if a ringing call exists.
     */
    public void onSilenceRinger() {
    }

    /**
     * Unused; to handle connection events issued by a {@link ConnectionService}, implement the
     * {@link android.telecom.Call.Callback#onConnectionEvent(Call, String, Bundle)} callback.
     * <p>
     * See {@link Connection#sendConnectionEvent(String, Bundle)}.
     *
     * @param call
     * 		The call the event is associated with.
     * @param event
     * 		The event.
     * @param extras
     * 		Any associated extras.
     */
    public void onConnectionEvent(android.telecom.Call call, java.lang.String event, android.os.Bundle extras) {
    }

    /**
     * Used to issue commands to the {@link Connection.VideoProvider} associated with a
     * {@link Call}.
     */
    public static abstract class VideoCall {
        /**
         *
         *
         * @unknown 
         */
        public abstract void destroy();

        /**
         * Registers a callback to receive commands and state changes for video calls.
         *
         * @param callback
         * 		The video call callback.
         */
        public abstract void registerCallback(android.telecom.InCallService.VideoCall.Callback callback);

        /**
         * Registers a callback to receive commands and state changes for video calls.
         *
         * @param callback
         * 		The video call callback.
         * @param handler
         * 		A handler which commands and status changes will be delivered to.
         */
        public abstract void registerCallback(android.telecom.InCallService.VideoCall.Callback callback, android.os.Handler handler);

        /**
         * Clears the video call callback set via {@link #registerCallback}.
         *
         * @param callback
         * 		The video call callback to clear.
         */
        public abstract void unregisterCallback(android.telecom.InCallService.VideoCall.Callback callback);

        /**
         * Sets the camera to be used for the outgoing video.
         * <p>
         * Handled by {@link Connection.VideoProvider#onSetCamera(String)}.
         *
         * @param cameraId
         * 		The id of the camera (use ids as reported by
         * 		{@link CameraManager#getCameraIdList()}).
         */
        public abstract void setCamera(java.lang.String cameraId);

        /**
         * Sets the surface to be used for displaying a preview of what the user's camera is
         * currently capturing.  When video transmission is enabled, this is the video signal which
         * is sent to the remote device.
         * <p>
         * Handled by {@link Connection.VideoProvider#onSetPreviewSurface(Surface)}.
         *
         * @param surface
         * 		The {@link Surface}.
         */
        public abstract void setPreviewSurface(android.view.Surface surface);

        /**
         * Sets the surface to be used for displaying the video received from the remote device.
         * <p>
         * Handled by {@link Connection.VideoProvider#onSetDisplaySurface(Surface)}.
         *
         * @param surface
         * 		The {@link Surface}.
         */
        public abstract void setDisplaySurface(android.view.Surface surface);

        /**
         * Sets the device orientation, in degrees.  Assumes that a standard portrait orientation of
         * the device is 0 degrees.
         * <p>
         * Handled by {@link Connection.VideoProvider#onSetDeviceOrientation(int)}.
         *
         * @param rotation
         * 		The device orientation, in degrees.
         */
        public abstract void setDeviceOrientation(int rotation);

        /**
         * Sets camera zoom ratio.
         * <p>
         * Handled by {@link Connection.VideoProvider#onSetZoom(float)}.
         *
         * @param value
         * 		The camera zoom ratio.
         */
        public abstract void setZoom(float value);

        /**
         * Issues a request to modify the properties of the current video session.
         * <p>
         * Example scenarios include: requesting an audio-only call to be upgraded to a
         * bi-directional video call, turning on or off the user's camera, sending a pause signal
         * when the {@link InCallService} is no longer the foreground application.
         * <p>
         * Handled by
         * {@link Connection.VideoProvider#onSendSessionModifyRequest(VideoProfile, VideoProfile)}.
         *
         * @param requestProfile
         * 		The requested call video properties.
         */
        public abstract void sendSessionModifyRequest(android.telecom.VideoProfile requestProfile);

        /**
         * Provides a response to a request to change the current call video session
         * properties.  This should be called in response to a request the {@link InCallService} has
         * received via {@link VideoCall.Callback#onSessionModifyRequestReceived}.
         * <p>
         * Handled by
         * {@link Connection.VideoProvider#onSendSessionModifyResponse(VideoProfile)}.
         *
         * @param responseProfile
         * 		The response call video properties.
         */
        public abstract void sendSessionModifyResponse(android.telecom.VideoProfile responseProfile);

        /**
         * Issues a request to the {@link Connection.VideoProvider} to retrieve the capabilities
         * of the current camera.  The current camera is selected using
         * {@link VideoCall#setCamera(String)}.
         * <p>
         * Camera capabilities are reported to the caller via
         * {@link VideoCall.Callback#onCameraCapabilitiesChanged(VideoProfile.CameraCapabilities)}.
         * <p>
         * Handled by {@link Connection.VideoProvider#onRequestCameraCapabilities()}.
         */
        public abstract void requestCameraCapabilities();

        /**
         * Issues a request to the {@link Connection.VideoProvider} to retrieve the cumulative data
         * usage for the video component of the current call (in bytes).  Data usage is reported
         * to the caller via {@link VideoCall.Callback#onCallDataUsageChanged}.
         * <p>
         * Handled by {@link Connection.VideoProvider#onRequestConnectionDataUsage()}.
         */
        public abstract void requestCallDataUsage();

        /**
         * Provides the {@link Connection.VideoProvider} with the {@link Uri} of an image to be
         * displayed to the peer device when the video signal is paused.
         * <p>
         * Handled by {@link Connection.VideoProvider#onSetPauseImage(Uri)}.
         *
         * @param uri
         * 		URI of image to display.
         */
        public abstract void setPauseImage(android.net.Uri uri);

        /**
         * The {@link InCallService} extends this class to provide a means of receiving callbacks
         * from the {@link Connection.VideoProvider}.
         * <p>
         * When the {@link InCallService} receives the
         * {@link Call.Callback#onVideoCallChanged(Call, VideoCall)} callback, it should create an
         * instance its {@link VideoCall.Callback} implementation and set it on the
         * {@link VideoCall} using {@link VideoCall#registerCallback(Callback)}.
         */
        public static abstract class Callback {
            /**
             * Called when the {@link Connection.VideoProvider} receives a session modification
             * request from the peer device.
             * <p>
             * The {@link InCallService} may potentially prompt the user to confirm whether they
             * wish to accept the request, or decide to automatically accept the request.  In either
             * case the {@link InCallService} should call
             * {@link VideoCall#sendSessionModifyResponse(VideoProfile)} to indicate the video
             * profile agreed upon.
             * <p>
             * Callback originates from
             * {@link Connection.VideoProvider#receiveSessionModifyRequest(VideoProfile)}.
             *
             * @param videoProfile
             * 		The requested video profile.
             */
            public abstract void onSessionModifyRequestReceived(android.telecom.VideoProfile videoProfile);

            /**
             * Called when the {@link Connection.VideoProvider} receives a response to a session
             * modification request previously sent to the peer device.
             * <p>
             * The new video state should not be considered active by the {@link InCallService}
             * until the {@link Call} video state changes (the
             * {@link Call.Callback#onDetailsChanged(Call, Call.Details)} callback is triggered
             * when the video state changes).
             * <p>
             * Callback originates from
             * {@link Connection.VideoProvider#receiveSessionModifyResponse(int, VideoProfile,
             *      VideoProfile)}.
             *
             * @param status
             * 		Status of the session modify request.  Valid values are
             * 		{@link Connection.VideoProvider#SESSION_MODIFY_REQUEST_SUCCESS},
             * 		{@link Connection.VideoProvider#SESSION_MODIFY_REQUEST_FAIL},
             * 		{@link Connection.VideoProvider#SESSION_MODIFY_REQUEST_INVALID},
             * 		{@link Connection.VideoProvider#SESSION_MODIFY_REQUEST_TIMED_OUT},
             * 		{@link Connection.VideoProvider#SESSION_MODIFY_REQUEST_REJECTED_BY_REMOTE}.
             * @param requestedProfile
             * 		The original request which was sent to the peer device.
             * @param responseProfile
             * 		The actual profile changes made by the peer device.
             */
            public abstract void onSessionModifyResponseReceived(int status, android.telecom.VideoProfile requestedProfile, android.telecom.VideoProfile responseProfile);

            /**
             * Handles events related to the current video session which the {@link InCallService}
             * may wish to handle. These are separate from requested changes to the session due to
             * the underlying protocol or connection.
             * <p>
             * Callback originates from
             * {@link Connection.VideoProvider#handleCallSessionEvent(int)}.
             *
             * @param event
             * 		The event.  Valid values are:
             * 		{@link Connection.VideoProvider#SESSION_EVENT_RX_PAUSE},
             * 		{@link Connection.VideoProvider#SESSION_EVENT_RX_RESUME},
             * 		{@link Connection.VideoProvider#SESSION_EVENT_TX_START},
             * 		{@link Connection.VideoProvider#SESSION_EVENT_TX_STOP},
             * 		{@link Connection.VideoProvider#SESSION_EVENT_CAMERA_FAILURE},
             * 		{@link Connection.VideoProvider#SESSION_EVENT_CAMERA_READY}.
             */
            public abstract void onCallSessionEvent(int event);

            /**
             * Handles a change to the video dimensions from the peer device. This could happen if,
             * for example, the peer changes orientation of their device, or switches cameras.
             * <p>
             * Callback originates from
             * {@link Connection.VideoProvider#changePeerDimensions(int, int)}.
             *
             * @param width
             * 		The updated peer video width.
             * @param height
             * 		The updated peer video height.
             */
            public abstract void onPeerDimensionsChanged(int width, int height);

            /**
             * Handles a change to the video quality.
             * <p>
             * Callback originates from {@link Connection.VideoProvider#changeVideoQuality(int)}.
             *
             * @param videoQuality
             * 		The updated peer video quality.  Valid values:
             * 		{@link VideoProfile#QUALITY_HIGH},
             * 		{@link VideoProfile#QUALITY_MEDIUM},
             * 		{@link VideoProfile#QUALITY_LOW},
             * 		{@link VideoProfile#QUALITY_DEFAULT}.
             */
            public abstract void onVideoQualityChanged(int videoQuality);

            /**
             * Handles an update to the total data used for the current video session.
             * <p>
             * Used by the {@link Connection.VideoProvider} in response to
             * {@link VideoCall#requestCallDataUsage()}.  May also be called periodically by the
             * {@link Connection.VideoProvider}.
             * <p>
             * Callback originates from {@link Connection.VideoProvider#setCallDataUsage(long)}.
             *
             * @param dataUsage
             * 		The updated data usage (in bytes).
             */
            public abstract void onCallDataUsageChanged(long dataUsage);

            /**
             * Handles a change in the capabilities of the currently selected camera.
             * <p>
             * Used by the {@link Connection.VideoProvider} in response to
             * {@link VideoCall#requestCameraCapabilities()}.  The {@link Connection.VideoProvider}
             * may also report the camera capabilities after a call to
             * {@link VideoCall#setCamera(String)}.
             * <p>
             * Callback originates from
             * {@link Connection.VideoProvider#changeCameraCapabilities(
             *      VideoProfile.CameraCapabilities)}.
             *
             * @param cameraCapabilities
             * 		The changed camera capabilities.
             */
            public abstract void onCameraCapabilitiesChanged(android.telecom.VideoProfile.CameraCapabilities cameraCapabilities);
        }
    }
}

