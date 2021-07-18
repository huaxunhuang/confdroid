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
 * R* limitations under the License.
 */
package android.telecom;


/**
 * A component that provides an RPC servant implementation of {@link IVideoCallback},
 * posting incoming messages on the main thread on a client-supplied delegate object.
 *
 * TODO: Generate this and similar classes using a compiler starting from AIDL interfaces.
 *
 * @unknown 
 */
final class VideoCallbackServant {
    private static final int MSG_RECEIVE_SESSION_MODIFY_REQUEST = 0;

    private static final int MSG_RECEIVE_SESSION_MODIFY_RESPONSE = 1;

    private static final int MSG_HANDLE_CALL_SESSION_EVENT = 2;

    private static final int MSG_CHANGE_PEER_DIMENSIONS = 3;

    private static final int MSG_CHANGE_CALL_DATA_USAGE = 4;

    private static final int MSG_CHANGE_CAMERA_CAPABILITIES = 5;

    private static final int MSG_CHANGE_VIDEO_QUALITY = 6;

    private final com.android.internal.telecom.IVideoCallback mDelegate;

    private final android.os.Handler mHandler = new android.os.Handler() {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            try {
                internalHandleMessage(msg);
            } catch (android.os.RemoteException e) {
            }
        }

        // Internal method defined to centralize handling of RemoteException
        private void internalHandleMessage(android.os.Message msg) throws android.os.RemoteException {
            switch (msg.what) {
                case android.telecom.VideoCallbackServant.MSG_RECEIVE_SESSION_MODIFY_REQUEST :
                    {
                        mDelegate.receiveSessionModifyRequest(((android.telecom.VideoProfile) (msg.obj)));
                        break;
                    }
                case android.telecom.VideoCallbackServant.MSG_RECEIVE_SESSION_MODIFY_RESPONSE :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.receiveSessionModifyResponse(args.argi1, ((android.telecom.VideoProfile) (args.arg1)), ((android.telecom.VideoProfile) (args.arg2)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.VideoCallbackServant.MSG_HANDLE_CALL_SESSION_EVENT :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.handleCallSessionEvent(args.argi1);
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.VideoCallbackServant.MSG_CHANGE_PEER_DIMENSIONS :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.changePeerDimensions(args.argi1, args.argi2);
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.VideoCallbackServant.MSG_CHANGE_CALL_DATA_USAGE :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.changeCallDataUsage(((long) (args.arg1)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.VideoCallbackServant.MSG_CHANGE_CAMERA_CAPABILITIES :
                    {
                        mDelegate.changeCameraCapabilities(((android.telecom.VideoProfile.CameraCapabilities) (msg.obj)));
                        break;
                    }
                case android.telecom.VideoCallbackServant.MSG_CHANGE_VIDEO_QUALITY :
                    {
                        mDelegate.changeVideoQuality(msg.arg1);
                        break;
                    }
            }
        }
    };

    private final com.android.internal.telecom.IVideoCallback mStub = new com.android.internal.telecom.IVideoCallback.Stub() {
        @java.lang.Override
        public void receiveSessionModifyRequest(android.telecom.VideoProfile videoProfile) throws android.os.RemoteException {
            mHandler.obtainMessage(android.telecom.VideoCallbackServant.MSG_RECEIVE_SESSION_MODIFY_REQUEST, videoProfile).sendToTarget();
        }

        @java.lang.Override
        public void receiveSessionModifyResponse(int status, android.telecom.VideoProfile requestedProfile, android.telecom.VideoProfile responseProfile) throws android.os.RemoteException {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.argi1 = status;
            args.arg1 = requestedProfile;
            args.arg2 = responseProfile;
            mHandler.obtainMessage(android.telecom.VideoCallbackServant.MSG_RECEIVE_SESSION_MODIFY_RESPONSE, args).sendToTarget();
        }

        @java.lang.Override
        public void handleCallSessionEvent(int event) throws android.os.RemoteException {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.argi1 = event;
            mHandler.obtainMessage(android.telecom.VideoCallbackServant.MSG_HANDLE_CALL_SESSION_EVENT, args).sendToTarget();
        }

        @java.lang.Override
        public void changePeerDimensions(int width, int height) throws android.os.RemoteException {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.argi1 = width;
            args.argi2 = height;
            mHandler.obtainMessage(android.telecom.VideoCallbackServant.MSG_CHANGE_PEER_DIMENSIONS, args).sendToTarget();
        }

        @java.lang.Override
        public void changeCallDataUsage(long dataUsage) throws android.os.RemoteException {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = dataUsage;
            mHandler.obtainMessage(android.telecom.VideoCallbackServant.MSG_CHANGE_CALL_DATA_USAGE, args).sendToTarget();
        }

        @java.lang.Override
        public void changeCameraCapabilities(android.telecom.VideoProfile.CameraCapabilities cameraCapabilities) throws android.os.RemoteException {
            mHandler.obtainMessage(android.telecom.VideoCallbackServant.MSG_CHANGE_CAMERA_CAPABILITIES, cameraCapabilities).sendToTarget();
        }

        @java.lang.Override
        public void changeVideoQuality(int videoQuality) throws android.os.RemoteException {
            mHandler.obtainMessage(android.telecom.VideoCallbackServant.MSG_CHANGE_VIDEO_QUALITY, videoQuality, 0).sendToTarget();
        }
    };

    public VideoCallbackServant(com.android.internal.telecom.IVideoCallback delegate) {
        mDelegate = delegate;
    }

    public com.android.internal.telecom.IVideoCallback getStub() {
        return mStub;
    }
}

