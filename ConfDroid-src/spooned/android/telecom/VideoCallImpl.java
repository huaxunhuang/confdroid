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
 * limitations under the License
 */
package android.telecom;


/**
 * Implementation of a Video Call, which allows InCallUi to communicate commands to the underlying
 * {@link Connection.VideoProvider}, and direct callbacks from the
 * {@link Connection.VideoProvider} to the appropriate {@link VideoCall.Listener}.
 *
 * {@hide }
 */
public class VideoCallImpl extends android.telecom.InCallService.VideoCall {
    private final com.android.internal.telecom.IVideoProvider mVideoProvider;

    private final android.telecom.VideoCallImpl.VideoCallListenerBinder mBinder;

    private android.telecom.InCallService.VideoCall.Callback mCallback;

    private int mVideoQuality = android.telecom.VideoProfile.QUALITY_UNKNOWN;

    private int mVideoState = android.telecom.VideoProfile.STATE_AUDIO_ONLY;

    private android.os.IBinder.DeathRecipient mDeathRecipient = new android.os.IBinder.DeathRecipient() {
        @java.lang.Override
        public void binderDied() {
            mVideoProvider.asBinder().unlinkToDeath(this, 0);
        }
    };

    /**
     * IVideoCallback stub implementation.
     */
    private final class VideoCallListenerBinder extends com.android.internal.telecom.IVideoCallback.Stub {
        @java.lang.Override
        public void receiveSessionModifyRequest(android.telecom.VideoProfile videoProfile) {
            if (mHandler == null) {
                return;
            }
            mHandler.obtainMessage(android.telecom.VideoCallImpl.MessageHandler.MSG_RECEIVE_SESSION_MODIFY_REQUEST, videoProfile).sendToTarget();
        }

        @java.lang.Override
        public void receiveSessionModifyResponse(int status, android.telecom.VideoProfile requestProfile, android.telecom.VideoProfile responseProfile) {
            if (mHandler == null) {
                return;
            }
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = status;
            args.arg2 = requestProfile;
            args.arg3 = responseProfile;
            mHandler.obtainMessage(android.telecom.VideoCallImpl.MessageHandler.MSG_RECEIVE_SESSION_MODIFY_RESPONSE, args).sendToTarget();
        }

        @java.lang.Override
        public void handleCallSessionEvent(int event) {
            if (mHandler == null) {
                return;
            }
            mHandler.obtainMessage(android.telecom.VideoCallImpl.MessageHandler.MSG_HANDLE_CALL_SESSION_EVENT, event).sendToTarget();
        }

        @java.lang.Override
        public void changePeerDimensions(int width, int height) {
            if (mHandler == null) {
                return;
            }
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = width;
            args.arg2 = height;
            mHandler.obtainMessage(android.telecom.VideoCallImpl.MessageHandler.MSG_CHANGE_PEER_DIMENSIONS, args).sendToTarget();
        }

        @java.lang.Override
        public void changeVideoQuality(int videoQuality) {
            if (mHandler == null) {
                return;
            }
            mHandler.obtainMessage(android.telecom.VideoCallImpl.MessageHandler.MSG_CHANGE_VIDEO_QUALITY, videoQuality, 0).sendToTarget();
        }

        @java.lang.Override
        public void changeCallDataUsage(long dataUsage) {
            if (mHandler == null) {
                return;
            }
            mHandler.obtainMessage(android.telecom.VideoCallImpl.MessageHandler.MSG_CHANGE_CALL_DATA_USAGE, dataUsage).sendToTarget();
        }

        @java.lang.Override
        public void changeCameraCapabilities(android.telecom.VideoProfile.CameraCapabilities cameraCapabilities) {
            if (mHandler == null) {
                return;
            }
            mHandler.obtainMessage(android.telecom.VideoCallImpl.MessageHandler.MSG_CHANGE_CAMERA_CAPABILITIES, cameraCapabilities).sendToTarget();
        }
    }

    /**
     * Default handler used to consolidate binder method calls onto a single thread.
     */
    private final class MessageHandler extends android.os.Handler {
        private static final int MSG_RECEIVE_SESSION_MODIFY_REQUEST = 1;

        private static final int MSG_RECEIVE_SESSION_MODIFY_RESPONSE = 2;

        private static final int MSG_HANDLE_CALL_SESSION_EVENT = 3;

        private static final int MSG_CHANGE_PEER_DIMENSIONS = 4;

        private static final int MSG_CHANGE_CALL_DATA_USAGE = 5;

        private static final int MSG_CHANGE_CAMERA_CAPABILITIES = 6;

        private static final int MSG_CHANGE_VIDEO_QUALITY = 7;

        public MessageHandler(android.os.Looper looper) {
            super(looper);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            if (android.telecom.VideoCallImpl.this.mCallback == null) {
                return;
            }
            com.android.internal.os.SomeArgs args;
            switch (msg.what) {
                case android.telecom.VideoCallImpl.MessageHandler.MSG_RECEIVE_SESSION_MODIFY_REQUEST :
                    android.telecom.VideoCallImpl.this.mCallback.onSessionModifyRequestReceived(((android.telecom.VideoProfile) (msg.obj)));
                    break;
                case android.telecom.VideoCallImpl.MessageHandler.MSG_RECEIVE_SESSION_MODIFY_RESPONSE :
                    args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    try {
                        int status = ((int) (args.arg1));
                        android.telecom.VideoProfile requestProfile = ((android.telecom.VideoProfile) (args.arg2));
                        android.telecom.VideoProfile responseProfile = ((android.telecom.VideoProfile) (args.arg3));
                        android.telecom.VideoCallImpl.this.mCallback.onSessionModifyResponseReceived(status, requestProfile, responseProfile);
                    } finally {
                        args.recycle();
                    }
                    break;
                case android.telecom.VideoCallImpl.MessageHandler.MSG_HANDLE_CALL_SESSION_EVENT :
                    android.telecom.VideoCallImpl.this.mCallback.onCallSessionEvent(((int) (msg.obj)));
                    break;
                case android.telecom.VideoCallImpl.MessageHandler.MSG_CHANGE_PEER_DIMENSIONS :
                    args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    try {
                        int width = ((int) (args.arg1));
                        int height = ((int) (args.arg2));
                        android.telecom.VideoCallImpl.this.mCallback.onPeerDimensionsChanged(width, height);
                    } finally {
                        args.recycle();
                    }
                    break;
                case android.telecom.VideoCallImpl.MessageHandler.MSG_CHANGE_CALL_DATA_USAGE :
                    android.telecom.VideoCallImpl.this.mCallback.onCallDataUsageChanged(((long) (msg.obj)));
                    break;
                case android.telecom.VideoCallImpl.MessageHandler.MSG_CHANGE_CAMERA_CAPABILITIES :
                    android.telecom.VideoCallImpl.this.mCallback.onCameraCapabilitiesChanged(((android.telecom.VideoProfile.CameraCapabilities) (msg.obj)));
                    break;
                case android.telecom.VideoCallImpl.MessageHandler.MSG_CHANGE_VIDEO_QUALITY :
                    mVideoQuality = msg.arg1;
                    android.telecom.VideoCallImpl.this.mCallback.onVideoQualityChanged(msg.arg1);
                    break;
                default :
                    break;
            }
        }
    }

    private android.os.Handler mHandler;

    VideoCallImpl(com.android.internal.telecom.IVideoProvider videoProvider) throws android.os.RemoteException {
        mVideoProvider = videoProvider;
        mVideoProvider.asBinder().linkToDeath(mDeathRecipient, 0);
        mBinder = new android.telecom.VideoCallImpl.VideoCallListenerBinder();
        mVideoProvider.addVideoCallback(mBinder);
    }

    public void destroy() {
        unregisterCallback(mCallback);
    }

    /**
     * {@inheritDoc }
     */
    public void registerCallback(android.telecom.InCallService.VideoCall.Callback callback) {
        registerCallback(callback, null);
    }

    /**
     * {@inheritDoc }
     */
    public void registerCallback(android.telecom.InCallService.VideoCall.Callback callback, android.os.Handler handler) {
        mCallback = callback;
        if (handler == null) {
            mHandler = new android.telecom.VideoCallImpl.MessageHandler(android.os.Looper.getMainLooper());
        } else {
            mHandler = new android.telecom.VideoCallImpl.MessageHandler(handler.getLooper());
        }
    }

    /**
     * {@inheritDoc }
     */
    public void unregisterCallback(android.telecom.InCallService.VideoCall.Callback callback) {
        if (callback != mCallback) {
            return;
        }
        mCallback = null;
        try {
            mVideoProvider.removeVideoCallback(mBinder);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * {@inheritDoc }
     */
    public void setCamera(java.lang.String cameraId) {
        try {
            mVideoProvider.setCamera(cameraId);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * {@inheritDoc }
     */
    public void setPreviewSurface(android.view.Surface surface) {
        try {
            mVideoProvider.setPreviewSurface(surface);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * {@inheritDoc }
     */
    public void setDisplaySurface(android.view.Surface surface) {
        try {
            mVideoProvider.setDisplaySurface(surface);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * {@inheritDoc }
     */
    public void setDeviceOrientation(int rotation) {
        try {
            mVideoProvider.setDeviceOrientation(rotation);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * {@inheritDoc }
     */
    public void setZoom(float value) {
        try {
            mVideoProvider.setZoom(value);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Sends a session modification request to the video provider.
     * <p>
     * The {@link InCallService} will create the {@code requestProfile} based on the current
     * video state (i.e. {@link Call.Details#getVideoState()}).  It is, however, possible that the
     * video state maintained by the {@link InCallService} could get out of sync with what is known
     * by the {@link android.telecom.Connection.VideoProvider}.  To remove ambiguity, the
     * {@link VideoCallImpl} passes along the pre-modify video profile to the {@code VideoProvider}
     * to ensure it has full context of the requested change.
     *
     * @param requestProfile
     * 		The requested video profile.
     */
    public void sendSessionModifyRequest(android.telecom.VideoProfile requestProfile) {
        try {
            android.telecom.VideoProfile originalProfile = new android.telecom.VideoProfile(mVideoState, mVideoQuality);
            mVideoProvider.sendSessionModifyRequest(originalProfile, requestProfile);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * {@inheritDoc }
     */
    public void sendSessionModifyResponse(android.telecom.VideoProfile responseProfile) {
        try {
            mVideoProvider.sendSessionModifyResponse(responseProfile);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * {@inheritDoc }
     */
    public void requestCameraCapabilities() {
        try {
            mVideoProvider.requestCameraCapabilities();
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * {@inheritDoc }
     */
    public void requestCallDataUsage() {
        try {
            mVideoProvider.requestCallDataUsage();
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * {@inheritDoc }
     */
    public void setPauseImage(android.net.Uri uri) {
        try {
            mVideoProvider.setPauseImage(uri);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Sets the video state for the current video call.
     *
     * @param videoState
     * 		the new video state.
     */
    public void setVideoState(int videoState) {
        mVideoState = videoState;
    }
}

