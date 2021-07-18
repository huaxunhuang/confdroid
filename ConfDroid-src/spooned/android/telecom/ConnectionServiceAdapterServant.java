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
 * A component that provides an RPC servant implementation of {@link IConnectionServiceAdapter},
 * posting incoming messages on the main thread on a client-supplied delegate object.
 *
 * TODO: Generate this and similar classes using a compiler starting from AIDL interfaces.
 *
 * @unknown 
 */
final class ConnectionServiceAdapterServant {
    private static final int MSG_HANDLE_CREATE_CONNECTION_COMPLETE = 1;

    private static final int MSG_SET_ACTIVE = 2;

    private static final int MSG_SET_RINGING = 3;

    private static final int MSG_SET_DIALING = 4;

    private static final int MSG_SET_DISCONNECTED = 5;

    private static final int MSG_SET_ON_HOLD = 6;

    private static final int MSG_SET_RINGBACK_REQUESTED = 7;

    private static final int MSG_SET_CONNECTION_CAPABILITIES = 8;

    private static final int MSG_SET_IS_CONFERENCED = 9;

    private static final int MSG_ADD_CONFERENCE_CALL = 10;

    private static final int MSG_REMOVE_CALL = 11;

    private static final int MSG_ON_POST_DIAL_WAIT = 12;

    private static final int MSG_QUERY_REMOTE_CALL_SERVICES = 13;

    private static final int MSG_SET_VIDEO_STATE = 14;

    private static final int MSG_SET_VIDEO_CALL_PROVIDER = 15;

    private static final int MSG_SET_IS_VOIP_AUDIO_MODE = 16;

    private static final int MSG_SET_STATUS_HINTS = 17;

    private static final int MSG_SET_ADDRESS = 18;

    private static final int MSG_SET_CALLER_DISPLAY_NAME = 19;

    private static final int MSG_SET_CONFERENCEABLE_CONNECTIONS = 20;

    private static final int MSG_ADD_EXISTING_CONNECTION = 21;

    private static final int MSG_ON_POST_DIAL_CHAR = 22;

    private static final int MSG_SET_CONFERENCE_MERGE_FAILED = 23;

    private static final int MSG_PUT_EXTRAS = 24;

    private static final int MSG_REMOVE_EXTRAS = 25;

    private static final int MSG_ON_CONNECTION_EVENT = 26;

    private static final int MSG_SET_CONNECTION_PROPERTIES = 27;

    private static final int MSG_SET_PULLING = 28;

    private final com.android.internal.telecom.IConnectionServiceAdapter mDelegate;

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
                case android.telecom.ConnectionServiceAdapterServant.MSG_HANDLE_CREATE_CONNECTION_COMPLETE :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.handleCreateConnectionComplete(((java.lang.String) (args.arg1)), ((android.telecom.ConnectionRequest) (args.arg2)), ((android.telecom.ParcelableConnection) (args.arg3)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_ACTIVE :
                    mDelegate.setActive(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_RINGING :
                    mDelegate.setRinging(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_DIALING :
                    mDelegate.setDialing(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_PULLING :
                    mDelegate.setPulling(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_DISCONNECTED :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.setDisconnected(((java.lang.String) (args.arg1)), ((android.telecom.DisconnectCause) (args.arg2)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_ON_HOLD :
                    mDelegate.setOnHold(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_RINGBACK_REQUESTED :
                    mDelegate.setRingbackRequested(((java.lang.String) (msg.obj)), msg.arg1 == 1);
                    break;
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_CONNECTION_CAPABILITIES :
                    mDelegate.setConnectionCapabilities(((java.lang.String) (msg.obj)), msg.arg1);
                    break;
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_CONNECTION_PROPERTIES :
                    mDelegate.setConnectionProperties(((java.lang.String) (msg.obj)), msg.arg1);
                    break;
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_IS_CONFERENCED :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.setIsConferenced(((java.lang.String) (args.arg1)), ((java.lang.String) (args.arg2)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionServiceAdapterServant.MSG_ADD_CONFERENCE_CALL :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.addConferenceCall(((java.lang.String) (args.arg1)), ((android.telecom.ParcelableConference) (args.arg2)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionServiceAdapterServant.MSG_REMOVE_CALL :
                    mDelegate.removeCall(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionServiceAdapterServant.MSG_ON_POST_DIAL_WAIT :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.onPostDialWait(((java.lang.String) (args.arg1)), ((java.lang.String) (args.arg2)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionServiceAdapterServant.MSG_ON_POST_DIAL_CHAR :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.onPostDialChar(((java.lang.String) (args.arg1)), ((char) (args.argi1)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionServiceAdapterServant.MSG_QUERY_REMOTE_CALL_SERVICES :
                    mDelegate.queryRemoteConnectionServices(((com.android.internal.telecom.RemoteServiceCallback) (msg.obj)));
                    break;
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_VIDEO_STATE :
                    mDelegate.setVideoState(((java.lang.String) (msg.obj)), msg.arg1);
                    break;
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_VIDEO_CALL_PROVIDER :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.setVideoProvider(((java.lang.String) (args.arg1)), ((com.android.internal.telecom.IVideoProvider) (args.arg2)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_IS_VOIP_AUDIO_MODE :
                    mDelegate.setIsVoipAudioMode(((java.lang.String) (msg.obj)), msg.arg1 == 1);
                    break;
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_STATUS_HINTS :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.setStatusHints(((java.lang.String) (args.arg1)), ((android.telecom.StatusHints) (args.arg2)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_ADDRESS :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.setAddress(((java.lang.String) (args.arg1)), ((android.net.Uri) (args.arg2)), args.argi1);
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_CALLER_DISPLAY_NAME :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.setCallerDisplayName(((java.lang.String) (args.arg1)), ((java.lang.String) (args.arg2)), args.argi1);
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_CONFERENCEABLE_CONNECTIONS :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.setConferenceableConnections(((java.lang.String) (args.arg1)), ((java.util.List<java.lang.String>) (args.arg2)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionServiceAdapterServant.MSG_ADD_EXISTING_CONNECTION :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.addExistingConnection(((java.lang.String) (args.arg1)), ((android.telecom.ParcelableConnection) (args.arg2)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionServiceAdapterServant.MSG_SET_CONFERENCE_MERGE_FAILED :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.setConferenceMergeFailed(((java.lang.String) (args.arg1)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionServiceAdapterServant.MSG_PUT_EXTRAS :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.putExtras(((java.lang.String) (args.arg1)), ((android.os.Bundle) (args.arg2)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionServiceAdapterServant.MSG_REMOVE_EXTRAS :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.removeExtras(((java.lang.String) (args.arg1)), ((java.util.List<java.lang.String>) (args.arg2)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionServiceAdapterServant.MSG_ON_CONNECTION_EVENT :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            mDelegate.onConnectionEvent(((java.lang.String) (args.arg1)), ((java.lang.String) (args.arg2)), ((android.os.Bundle) (args.arg3)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
            }
        }
    };

    private final com.android.internal.telecom.IConnectionServiceAdapter mStub = new com.android.internal.telecom.IConnectionServiceAdapter.Stub() {
        @java.lang.Override
        public void handleCreateConnectionComplete(java.lang.String id, android.telecom.ConnectionRequest request, android.telecom.ParcelableConnection connection) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = id;
            args.arg2 = request;
            args.arg3 = connection;
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_HANDLE_CREATE_CONNECTION_COMPLETE, args).sendToTarget();
        }

        @java.lang.Override
        public void setActive(java.lang.String connectionId) {
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_ACTIVE, connectionId).sendToTarget();
        }

        @java.lang.Override
        public void setRinging(java.lang.String connectionId) {
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_RINGING, connectionId).sendToTarget();
        }

        @java.lang.Override
        public void setDialing(java.lang.String connectionId) {
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_DIALING, connectionId).sendToTarget();
        }

        @java.lang.Override
        public void setPulling(java.lang.String connectionId) {
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_PULLING, connectionId).sendToTarget();
        }

        @java.lang.Override
        public void setDisconnected(java.lang.String connectionId, android.telecom.DisconnectCause disconnectCause) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = disconnectCause;
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_DISCONNECTED, args).sendToTarget();
        }

        @java.lang.Override
        public void setOnHold(java.lang.String connectionId) {
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_ON_HOLD, connectionId).sendToTarget();
        }

        @java.lang.Override
        public void setRingbackRequested(java.lang.String connectionId, boolean ringback) {
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_RINGBACK_REQUESTED, ringback ? 1 : 0, 0, connectionId).sendToTarget();
        }

        @java.lang.Override
        public void setConnectionCapabilities(java.lang.String connectionId, int connectionCapabilities) {
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_CONNECTION_CAPABILITIES, connectionCapabilities, 0, connectionId).sendToTarget();
        }

        @java.lang.Override
        public void setConnectionProperties(java.lang.String connectionId, int connectionProperties) {
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_CONNECTION_PROPERTIES, connectionProperties, 0, connectionId).sendToTarget();
        }

        @java.lang.Override
        public void setConferenceMergeFailed(java.lang.String callId) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = callId;
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_CONFERENCE_MERGE_FAILED, args).sendToTarget();
        }

        @java.lang.Override
        public void setIsConferenced(java.lang.String callId, java.lang.String conferenceCallId) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = callId;
            args.arg2 = conferenceCallId;
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_IS_CONFERENCED, args).sendToTarget();
        }

        @java.lang.Override
        public void addConferenceCall(java.lang.String callId, android.telecom.ParcelableConference parcelableConference) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = callId;
            args.arg2 = parcelableConference;
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_ADD_CONFERENCE_CALL, args).sendToTarget();
        }

        @java.lang.Override
        public void removeCall(java.lang.String connectionId) {
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_REMOVE_CALL, connectionId).sendToTarget();
        }

        @java.lang.Override
        public void onPostDialWait(java.lang.String connectionId, java.lang.String remainingDigits) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = remainingDigits;
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_ON_POST_DIAL_WAIT, args).sendToTarget();
        }

        @java.lang.Override
        public void onPostDialChar(java.lang.String connectionId, char nextChar) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = connectionId;
            args.argi1 = nextChar;
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_ON_POST_DIAL_CHAR, args).sendToTarget();
        }

        @java.lang.Override
        public void queryRemoteConnectionServices(com.android.internal.telecom.RemoteServiceCallback callback) {
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_QUERY_REMOTE_CALL_SERVICES, callback).sendToTarget();
        }

        @java.lang.Override
        public void setVideoState(java.lang.String connectionId, int videoState) {
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_VIDEO_STATE, videoState, 0, connectionId).sendToTarget();
        }

        @java.lang.Override
        public void setVideoProvider(java.lang.String connectionId, com.android.internal.telecom.IVideoProvider videoProvider) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = videoProvider;
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_VIDEO_CALL_PROVIDER, args).sendToTarget();
        }

        @java.lang.Override
        public final void setIsVoipAudioMode(java.lang.String connectionId, boolean isVoip) {
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_IS_VOIP_AUDIO_MODE, isVoip ? 1 : 0, 0, connectionId).sendToTarget();
        }

        @java.lang.Override
        public final void setStatusHints(java.lang.String connectionId, android.telecom.StatusHints statusHints) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = statusHints;
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_STATUS_HINTS, args).sendToTarget();
        }

        @java.lang.Override
        public final void setAddress(java.lang.String connectionId, android.net.Uri address, int presentation) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = address;
            args.argi1 = presentation;
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_ADDRESS, args).sendToTarget();
        }

        @java.lang.Override
        public final void setCallerDisplayName(java.lang.String connectionId, java.lang.String callerDisplayName, int presentation) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = callerDisplayName;
            args.argi1 = presentation;
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_CALLER_DISPLAY_NAME, args).sendToTarget();
        }

        @java.lang.Override
        public final void setConferenceableConnections(java.lang.String connectionId, java.util.List<java.lang.String> conferenceableConnectionIds) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = conferenceableConnectionIds;
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_SET_CONFERENCEABLE_CONNECTIONS, args).sendToTarget();
        }

        @java.lang.Override
        public final void addExistingConnection(java.lang.String connectionId, android.telecom.ParcelableConnection connection) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = connection;
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_ADD_EXISTING_CONNECTION, args).sendToTarget();
        }

        @java.lang.Override
        public final void putExtras(java.lang.String connectionId, android.os.Bundle extras) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = extras;
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_PUT_EXTRAS, args).sendToTarget();
        }

        @java.lang.Override
        public final void removeExtras(java.lang.String connectionId, java.util.List<java.lang.String> keys) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = keys;
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_REMOVE_EXTRAS, args).sendToTarget();
        }

        @java.lang.Override
        public final void onConnectionEvent(java.lang.String connectionId, java.lang.String event, android.os.Bundle extras) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = event;
            args.arg3 = extras;
            mHandler.obtainMessage(android.telecom.ConnectionServiceAdapterServant.MSG_ON_CONNECTION_EVENT, args).sendToTarget();
        }
    };

    public ConnectionServiceAdapterServant(com.android.internal.telecom.IConnectionServiceAdapter delegate) {
        mDelegate = delegate;
    }

    public com.android.internal.telecom.IConnectionServiceAdapter getStub() {
        return mStub;
    }
}

