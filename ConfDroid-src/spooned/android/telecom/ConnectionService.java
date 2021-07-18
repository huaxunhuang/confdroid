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
package android.telecom;


/**
 * An abstract service that should be implemented by any apps which can make phone calls (VoIP or
 * otherwise) and want those calls to be integrated into the built-in phone app.
 * Once implemented, the {@code ConnectionService} needs two additional steps before it will be
 * integrated into the phone app:
 * <p>
 * 1. <i>Registration in AndroidManifest.xml</i>
 * <br/>
 * <pre>
 * &lt;service android:name="com.example.package.MyConnectionService"
 *    android:label="@string/some_label_for_my_connection_service"
 *    android:permission="android.permission.BIND_TELECOM_CONNECTION_SERVICE"&gt;
 *  &lt;intent-filter&gt;
 *   &lt;action android:name="android.telecom.ConnectionService" /&gt;
 *  &lt;/intent-filter&gt;
 * &lt;/service&gt;
 * </pre>
 * <p>
 * 2. <i> Registration of {@link PhoneAccount} with {@link TelecomManager}.</i>
 * <br/>
 * See {@link PhoneAccount} and {@link TelecomManager#registerPhoneAccount} for more information.
 * <p>
 * Once registered and enabled by the user in the phone app settings, telecom will bind to a
 * {@code ConnectionService} implementation when it wants that {@code ConnectionService} to place
 * a call or the service has indicated that is has an incoming call through
 * {@link TelecomManager#addNewIncomingCall}. The {@code ConnectionService} can then expect a call
 * to {@link #onCreateIncomingConnection} or {@link #onCreateOutgoingConnection} wherein it
 * should provide a new instance of a {@link Connection} object.  It is through this
 * {@link Connection} object that telecom receives state updates and the {@code ConnectionService}
 * receives call-commands such as answer, reject, hold and disconnect.
 * <p>
 * When there are no more live calls, telecom will unbind from the {@code ConnectionService}.
 */
public abstract class ConnectionService extends android.app.Service {
    /**
     * The {@link Intent} that must be declared as handled by the service.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.SERVICE_ACTION)
    public static final java.lang.String SERVICE_INTERFACE = "android.telecom.ConnectionService";

    // Flag controlling whether PII is emitted into the logs
    private static final boolean PII_DEBUG = android.telecom.Log.isLoggable(android.util.Log.DEBUG);

    private static final int MSG_ADD_CONNECTION_SERVICE_ADAPTER = 1;

    private static final int MSG_CREATE_CONNECTION = 2;

    private static final int MSG_ABORT = 3;

    private static final int MSG_ANSWER = 4;

    private static final int MSG_REJECT = 5;

    private static final int MSG_DISCONNECT = 6;

    private static final int MSG_HOLD = 7;

    private static final int MSG_UNHOLD = 8;

    private static final int MSG_ON_CALL_AUDIO_STATE_CHANGED = 9;

    private static final int MSG_PLAY_DTMF_TONE = 10;

    private static final int MSG_STOP_DTMF_TONE = 11;

    private static final int MSG_CONFERENCE = 12;

    private static final int MSG_SPLIT_FROM_CONFERENCE = 13;

    private static final int MSG_ON_POST_DIAL_CONTINUE = 14;

    private static final int MSG_REMOVE_CONNECTION_SERVICE_ADAPTER = 16;

    private static final int MSG_ANSWER_VIDEO = 17;

    private static final int MSG_MERGE_CONFERENCE = 18;

    private static final int MSG_SWAP_CONFERENCE = 19;

    private static final int MSG_REJECT_WITH_MESSAGE = 20;

    private static final int MSG_SILENCE = 21;

    private static final int MSG_PULL_EXTERNAL_CALL = 22;

    private static final int MSG_SEND_CALL_EVENT = 23;

    private static final int MSG_ON_EXTRAS_CHANGED = 24;

    private static android.telecom.Connection sNullConnection;

    private final java.util.Map<java.lang.String, android.telecom.Connection> mConnectionById = new java.util.concurrent.ConcurrentHashMap<>();

    private final java.util.Map<android.telecom.Connection, java.lang.String> mIdByConnection = new java.util.concurrent.ConcurrentHashMap<>();

    private final java.util.Map<java.lang.String, android.telecom.Conference> mConferenceById = new java.util.concurrent.ConcurrentHashMap<>();

    private final java.util.Map<android.telecom.Conference, java.lang.String> mIdByConference = new java.util.concurrent.ConcurrentHashMap<>();

    private final android.telecom.RemoteConnectionManager mRemoteConnectionManager = new android.telecom.RemoteConnectionManager(this);

    private final java.util.List<java.lang.Runnable> mPreInitializationConnectionRequests = new java.util.ArrayList<>();

    private final android.telecom.ConnectionServiceAdapter mAdapter = new android.telecom.ConnectionServiceAdapter();

    private boolean mAreAccountsInitialized = false;

    private android.telecom.Conference sNullConference;

    private java.lang.Object mIdSyncRoot = new java.lang.Object();

    private int mId = 0;

    private final android.os.IBinder mBinder = new com.android.internal.telecom.IConnectionService.Stub() {
        @java.lang.Override
        public void addConnectionServiceAdapter(com.android.internal.telecom.IConnectionServiceAdapter adapter) {
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_ADD_CONNECTION_SERVICE_ADAPTER, adapter).sendToTarget();
        }

        public void removeConnectionServiceAdapter(com.android.internal.telecom.IConnectionServiceAdapter adapter) {
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_REMOVE_CONNECTION_SERVICE_ADAPTER, adapter).sendToTarget();
        }

        @java.lang.Override
        public void createConnection(android.telecom.PhoneAccountHandle connectionManagerPhoneAccount, java.lang.String id, android.telecom.ConnectionRequest request, boolean isIncoming, boolean isUnknown) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = connectionManagerPhoneAccount;
            args.arg2 = id;
            args.arg3 = request;
            args.argi1 = (isIncoming) ? 1 : 0;
            args.argi2 = (isUnknown) ? 1 : 0;
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_CREATE_CONNECTION, args).sendToTarget();
        }

        @java.lang.Override
        public void abort(java.lang.String callId) {
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_ABORT, callId).sendToTarget();
        }

        @java.lang.Override
        public void answerVideo(java.lang.String callId, int videoState) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = callId;
            args.argi1 = videoState;
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_ANSWER_VIDEO, args).sendToTarget();
        }

        @java.lang.Override
        public void answer(java.lang.String callId) {
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_ANSWER, callId).sendToTarget();
        }

        @java.lang.Override
        public void reject(java.lang.String callId) {
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_REJECT, callId).sendToTarget();
        }

        @java.lang.Override
        public void rejectWithMessage(java.lang.String callId, java.lang.String message) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = callId;
            args.arg2 = message;
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_REJECT_WITH_MESSAGE, args).sendToTarget();
        }

        @java.lang.Override
        public void silence(java.lang.String callId) {
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_SILENCE, callId).sendToTarget();
        }

        @java.lang.Override
        public void disconnect(java.lang.String callId) {
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_DISCONNECT, callId).sendToTarget();
        }

        @java.lang.Override
        public void hold(java.lang.String callId) {
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_HOLD, callId).sendToTarget();
        }

        @java.lang.Override
        public void unhold(java.lang.String callId) {
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_UNHOLD, callId).sendToTarget();
        }

        @java.lang.Override
        public void onCallAudioStateChanged(java.lang.String callId, android.telecom.CallAudioState callAudioState) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = callId;
            args.arg2 = callAudioState;
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_ON_CALL_AUDIO_STATE_CHANGED, args).sendToTarget();
        }

        @java.lang.Override
        public void playDtmfTone(java.lang.String callId, char digit) {
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_PLAY_DTMF_TONE, digit, 0, callId).sendToTarget();
        }

        @java.lang.Override
        public void stopDtmfTone(java.lang.String callId) {
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_STOP_DTMF_TONE, callId).sendToTarget();
        }

        @java.lang.Override
        public void conference(java.lang.String callId1, java.lang.String callId2) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = callId1;
            args.arg2 = callId2;
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_CONFERENCE, args).sendToTarget();
        }

        @java.lang.Override
        public void splitFromConference(java.lang.String callId) {
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_SPLIT_FROM_CONFERENCE, callId).sendToTarget();
        }

        @java.lang.Override
        public void mergeConference(java.lang.String callId) {
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_MERGE_CONFERENCE, callId).sendToTarget();
        }

        @java.lang.Override
        public void swapConference(java.lang.String callId) {
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_SWAP_CONFERENCE, callId).sendToTarget();
        }

        @java.lang.Override
        public void onPostDialContinue(java.lang.String callId, boolean proceed) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = callId;
            args.argi1 = (proceed) ? 1 : 0;
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_ON_POST_DIAL_CONTINUE, args).sendToTarget();
        }

        @java.lang.Override
        public void pullExternalCall(java.lang.String callId) {
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_PULL_EXTERNAL_CALL, callId).sendToTarget();
        }

        @java.lang.Override
        public void sendCallEvent(java.lang.String callId, java.lang.String event, android.os.Bundle extras) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = callId;
            args.arg2 = event;
            args.arg3 = extras;
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_SEND_CALL_EVENT, args).sendToTarget();
        }

        @java.lang.Override
        public void onExtrasChanged(java.lang.String callId, android.os.Bundle extras) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = callId;
            args.arg2 = extras;
            mHandler.obtainMessage(android.telecom.ConnectionService.MSG_ON_EXTRAS_CHANGED, args).sendToTarget();
        }
    };

    private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper()) {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.telecom.ConnectionService.MSG_ADD_CONNECTION_SERVICE_ADAPTER :
                    mAdapter.addAdapter(((com.android.internal.telecom.IConnectionServiceAdapter) (msg.obj)));
                    onAdapterAttached();
                    break;
                case android.telecom.ConnectionService.MSG_REMOVE_CONNECTION_SERVICE_ADAPTER :
                    mAdapter.removeAdapter(((com.android.internal.telecom.IConnectionServiceAdapter) (msg.obj)));
                    break;
                case android.telecom.ConnectionService.MSG_CREATE_CONNECTION :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            final android.telecom.PhoneAccountHandle connectionManagerPhoneAccount = ((android.telecom.PhoneAccountHandle) (args.arg1));
                            final java.lang.String id = ((java.lang.String) (args.arg2));
                            final android.telecom.ConnectionRequest request = ((android.telecom.ConnectionRequest) (args.arg3));
                            final boolean isIncoming = args.argi1 == 1;
                            final boolean isUnknown = args.argi2 == 1;
                            if (!mAreAccountsInitialized) {
                                android.telecom.Log.d(this, "Enqueueing pre-init request %s", id);
                                mPreInitializationConnectionRequests.add(new java.lang.Runnable() {
                                    @java.lang.Override
                                    public void run() {
                                        createConnection(connectionManagerPhoneAccount, id, request, isIncoming, isUnknown);
                                    }
                                });
                            } else {
                                createConnection(connectionManagerPhoneAccount, id, request, isIncoming, isUnknown);
                            }
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionService.MSG_ABORT :
                    abort(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionService.MSG_ANSWER :
                    answer(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionService.MSG_ANSWER_VIDEO :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            java.lang.String callId = ((java.lang.String) (args.arg1));
                            int videoState = args.argi1;
                            answerVideo(callId, videoState);
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionService.MSG_REJECT :
                    reject(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionService.MSG_REJECT_WITH_MESSAGE :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            reject(((java.lang.String) (args.arg1)), ((java.lang.String) (args.arg2)));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionService.MSG_DISCONNECT :
                    disconnect(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionService.MSG_SILENCE :
                    silence(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionService.MSG_HOLD :
                    hold(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionService.MSG_UNHOLD :
                    unhold(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionService.MSG_ON_CALL_AUDIO_STATE_CHANGED :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            java.lang.String callId = ((java.lang.String) (args.arg1));
                            android.telecom.CallAudioState audioState = ((android.telecom.CallAudioState) (args.arg2));
                            onCallAudioStateChanged(callId, new android.telecom.CallAudioState(audioState));
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionService.MSG_PLAY_DTMF_TONE :
                    playDtmfTone(((java.lang.String) (msg.obj)), ((char) (msg.arg1)));
                    break;
                case android.telecom.ConnectionService.MSG_STOP_DTMF_TONE :
                    stopDtmfTone(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionService.MSG_CONFERENCE :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            java.lang.String callId1 = ((java.lang.String) (args.arg1));
                            java.lang.String callId2 = ((java.lang.String) (args.arg2));
                            conference(callId1, callId2);
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionService.MSG_SPLIT_FROM_CONFERENCE :
                    splitFromConference(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionService.MSG_MERGE_CONFERENCE :
                    mergeConference(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionService.MSG_SWAP_CONFERENCE :
                    swapConference(((java.lang.String) (msg.obj)));
                    break;
                case android.telecom.ConnectionService.MSG_ON_POST_DIAL_CONTINUE :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            java.lang.String callId = ((java.lang.String) (args.arg1));
                            boolean proceed = args.argi1 == 1;
                            onPostDialContinue(callId, proceed);
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionService.MSG_PULL_EXTERNAL_CALL :
                    {
                        pullExternalCall(((java.lang.String) (msg.obj)));
                        break;
                    }
                case android.telecom.ConnectionService.MSG_SEND_CALL_EVENT :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            java.lang.String callId = ((java.lang.String) (args.arg1));
                            java.lang.String event = ((java.lang.String) (args.arg2));
                            android.os.Bundle extras = ((android.os.Bundle) (args.arg3));
                            sendCallEvent(callId, event, extras);
                        } finally {
                            args.recycle();
                        }
                        break;
                    }
                case android.telecom.ConnectionService.MSG_ON_EXTRAS_CHANGED :
                    {
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        try {
                            java.lang.String callId = ((java.lang.String) (args.arg1));
                            android.os.Bundle extras = ((android.os.Bundle) (args.arg2));
                            handleExtrasChanged(callId, extras);
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

    private final android.telecom.Conference.Listener mConferenceListener = new android.telecom.Conference.Listener() {
        @java.lang.Override
        public void onStateChanged(android.telecom.Conference conference, int oldState, int newState) {
            java.lang.String id = mIdByConference.get(conference);
            switch (newState) {
                case android.telecom.Connection.STATE_ACTIVE :
                    mAdapter.setActive(id);
                    break;
                case android.telecom.Connection.STATE_HOLDING :
                    mAdapter.setOnHold(id);
                    break;
                case android.telecom.Connection.STATE_DISCONNECTED :
                    // handled by onDisconnected
                    break;
            }
        }

        @java.lang.Override
        public void onDisconnected(android.telecom.Conference conference, android.telecom.DisconnectCause disconnectCause) {
            java.lang.String id = mIdByConference.get(conference);
            mAdapter.setDisconnected(id, disconnectCause);
        }

        @java.lang.Override
        public void onConnectionAdded(android.telecom.Conference conference, android.telecom.Connection connection) {
        }

        @java.lang.Override
        public void onConnectionRemoved(android.telecom.Conference conference, android.telecom.Connection connection) {
        }

        @java.lang.Override
        public void onConferenceableConnectionsChanged(android.telecom.Conference conference, java.util.List<android.telecom.Connection> conferenceableConnections) {
            mAdapter.setConferenceableConnections(mIdByConference.get(conference), createConnectionIdList(conferenceableConnections));
        }

        @java.lang.Override
        public void onDestroyed(android.telecom.Conference conference) {
            removeConference(conference);
        }

        @java.lang.Override
        public void onConnectionCapabilitiesChanged(android.telecom.Conference conference, int connectionCapabilities) {
            java.lang.String id = mIdByConference.get(conference);
            android.telecom.Log.d(this, "call capabilities: conference: %s", android.telecom.Connection.capabilitiesToString(connectionCapabilities));
            mAdapter.setConnectionCapabilities(id, connectionCapabilities);
        }

        @java.lang.Override
        public void onConnectionPropertiesChanged(android.telecom.Conference conference, int connectionProperties) {
            java.lang.String id = mIdByConference.get(conference);
            android.telecom.Log.d(this, "call capabilities: conference: %s", android.telecom.Connection.propertiesToString(connectionProperties));
            mAdapter.setConnectionProperties(id, connectionProperties);
        }

        @java.lang.Override
        public void onVideoStateChanged(android.telecom.Conference c, int videoState) {
            java.lang.String id = mIdByConference.get(c);
            android.telecom.Log.d(this, "onVideoStateChanged set video state %d", videoState);
            mAdapter.setVideoState(id, videoState);
        }

        @java.lang.Override
        public void onVideoProviderChanged(android.telecom.Conference c, android.telecom.Connection.VideoProvider videoProvider) {
            java.lang.String id = mIdByConference.get(c);
            android.telecom.Log.d(this, "onVideoProviderChanged: Connection: %s, VideoProvider: %s", c, videoProvider);
            mAdapter.setVideoProvider(id, videoProvider);
        }

        @java.lang.Override
        public void onStatusHintsChanged(android.telecom.Conference conference, android.telecom.StatusHints statusHints) {
            java.lang.String id = mIdByConference.get(conference);
            if (id != null) {
                mAdapter.setStatusHints(id, statusHints);
            }
        }

        @java.lang.Override
        public void onExtrasChanged(android.telecom.Conference c, android.os.Bundle extras) {
            java.lang.String id = mIdByConference.get(c);
            if (id != null) {
                mAdapter.putExtras(id, extras);
            }
        }

        @java.lang.Override
        public void onExtrasRemoved(android.telecom.Conference c, java.util.List<java.lang.String> keys) {
            java.lang.String id = mIdByConference.get(c);
            if (id != null) {
                mAdapter.removeExtras(id, keys);
            }
        }
    };

    private final android.telecom.Connection.Listener mConnectionListener = new android.telecom.Connection.Listener() {
        @java.lang.Override
        public void onStateChanged(android.telecom.Connection c, int state) {
            java.lang.String id = mIdByConnection.get(c);
            android.telecom.Log.d(this, "Adapter set state %s %s", id, android.telecom.Connection.stateToString(state));
            switch (state) {
                case android.telecom.Connection.STATE_ACTIVE :
                    mAdapter.setActive(id);
                    break;
                case android.telecom.Connection.STATE_DIALING :
                    mAdapter.setDialing(id);
                    break;
                case android.telecom.Connection.STATE_PULLING_CALL :
                    mAdapter.setPulling(id);
                    break;
                case android.telecom.Connection.STATE_DISCONNECTED :
                    // Handled in onDisconnected()
                    break;
                case android.telecom.Connection.STATE_HOLDING :
                    mAdapter.setOnHold(id);
                    break;
                case android.telecom.Connection.STATE_NEW :
                    // Nothing to tell Telecom
                    break;
                case android.telecom.Connection.STATE_RINGING :
                    mAdapter.setRinging(id);
                    break;
            }
        }

        @java.lang.Override
        public void onDisconnected(android.telecom.Connection c, android.telecom.DisconnectCause disconnectCause) {
            java.lang.String id = mIdByConnection.get(c);
            android.telecom.Log.d(this, "Adapter set disconnected %s", disconnectCause);
            mAdapter.setDisconnected(id, disconnectCause);
        }

        @java.lang.Override
        public void onVideoStateChanged(android.telecom.Connection c, int videoState) {
            java.lang.String id = mIdByConnection.get(c);
            android.telecom.Log.d(this, "Adapter set video state %d", videoState);
            mAdapter.setVideoState(id, videoState);
        }

        @java.lang.Override
        public void onAddressChanged(android.telecom.Connection c, android.net.Uri address, int presentation) {
            java.lang.String id = mIdByConnection.get(c);
            mAdapter.setAddress(id, address, presentation);
        }

        @java.lang.Override
        public void onCallerDisplayNameChanged(android.telecom.Connection c, java.lang.String callerDisplayName, int presentation) {
            java.lang.String id = mIdByConnection.get(c);
            mAdapter.setCallerDisplayName(id, callerDisplayName, presentation);
        }

        @java.lang.Override
        public void onDestroyed(android.telecom.Connection c) {
            removeConnection(c);
        }

        @java.lang.Override
        public void onPostDialWait(android.telecom.Connection c, java.lang.String remaining) {
            java.lang.String id = mIdByConnection.get(c);
            android.telecom.Log.d(this, "Adapter onPostDialWait %s, %s", c, remaining);
            mAdapter.onPostDialWait(id, remaining);
        }

        @java.lang.Override
        public void onPostDialChar(android.telecom.Connection c, char nextChar) {
            java.lang.String id = mIdByConnection.get(c);
            android.telecom.Log.d(this, "Adapter onPostDialChar %s, %s", c, nextChar);
            mAdapter.onPostDialChar(id, nextChar);
        }

        @java.lang.Override
        public void onRingbackRequested(android.telecom.Connection c, boolean ringback) {
            java.lang.String id = mIdByConnection.get(c);
            android.telecom.Log.d(this, "Adapter onRingback %b", ringback);
            mAdapter.setRingbackRequested(id, ringback);
        }

        @java.lang.Override
        public void onConnectionCapabilitiesChanged(android.telecom.Connection c, int capabilities) {
            java.lang.String id = mIdByConnection.get(c);
            android.telecom.Log.d(this, "capabilities: parcelableconnection: %s", android.telecom.Connection.capabilitiesToString(capabilities));
            mAdapter.setConnectionCapabilities(id, capabilities);
        }

        @java.lang.Override
        public void onConnectionPropertiesChanged(android.telecom.Connection c, int properties) {
            java.lang.String id = mIdByConnection.get(c);
            android.telecom.Log.d(this, "properties: parcelableconnection: %s", android.telecom.Connection.propertiesToString(properties));
            mAdapter.setConnectionProperties(id, properties);
        }

        @java.lang.Override
        public void onVideoProviderChanged(android.telecom.Connection c, android.telecom.Connection.VideoProvider videoProvider) {
            java.lang.String id = mIdByConnection.get(c);
            android.telecom.Log.d(this, "onVideoProviderChanged: Connection: %s, VideoProvider: %s", c, videoProvider);
            mAdapter.setVideoProvider(id, videoProvider);
        }

        @java.lang.Override
        public void onAudioModeIsVoipChanged(android.telecom.Connection c, boolean isVoip) {
            java.lang.String id = mIdByConnection.get(c);
            mAdapter.setIsVoipAudioMode(id, isVoip);
        }

        @java.lang.Override
        public void onStatusHintsChanged(android.telecom.Connection c, android.telecom.StatusHints statusHints) {
            java.lang.String id = mIdByConnection.get(c);
            mAdapter.setStatusHints(id, statusHints);
        }

        @java.lang.Override
        public void onConferenceablesChanged(android.telecom.Connection connection, java.util.List<android.telecom.Conferenceable> conferenceables) {
            mAdapter.setConferenceableConnections(mIdByConnection.get(connection), createIdList(conferenceables));
        }

        @java.lang.Override
        public void onConferenceChanged(android.telecom.Connection connection, android.telecom.Conference conference) {
            java.lang.String id = mIdByConnection.get(connection);
            if (id != null) {
                java.lang.String conferenceId = null;
                if (conference != null) {
                    conferenceId = mIdByConference.get(conference);
                }
                mAdapter.setIsConferenced(id, conferenceId);
            }
        }

        @java.lang.Override
        public void onConferenceMergeFailed(android.telecom.Connection connection) {
            java.lang.String id = mIdByConnection.get(connection);
            if (id != null) {
                mAdapter.onConferenceMergeFailed(id);
            }
        }

        @java.lang.Override
        public void onExtrasChanged(android.telecom.Connection c, android.os.Bundle extras) {
            java.lang.String id = mIdByConnection.get(c);
            if (id != null) {
                mAdapter.putExtras(id, extras);
            }
        }

        public void onExtrasRemoved(android.telecom.Connection c, java.util.List<java.lang.String> keys) {
            java.lang.String id = mIdByConnection.get(c);
            if (id != null) {
                mAdapter.removeExtras(id, keys);
            }
        }

        @java.lang.Override
        public void onConnectionEvent(android.telecom.Connection connection, java.lang.String event, android.os.Bundle extras) {
            java.lang.String id = mIdByConnection.get(connection);
            if (id != null) {
                mAdapter.onConnectionEvent(id, event, extras);
            }
        }
    };

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public final android.os.IBinder onBind(android.content.Intent intent) {
        return mBinder;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public boolean onUnbind(android.content.Intent intent) {
        endAllConnections();
        return super.onUnbind(intent);
    }

    /**
     * This can be used by telecom to either create a new outgoing call or attach to an existing
     * incoming call. In either case, telecom will cycle through a set of services and call
     * createConnection util a connection service cancels the process or completes it successfully.
     */
    private void createConnection(final android.telecom.PhoneAccountHandle callManagerAccount, final java.lang.String callId, final android.telecom.ConnectionRequest request, boolean isIncoming, boolean isUnknown) {
        android.telecom.Log.d(this, "createConnection, callManagerAccount: %s, callId: %s, request: %s, " + "isIncoming: %b, isUnknown: %b", callManagerAccount, callId, request, isIncoming, isUnknown);
        android.telecom.Connection connection = (isUnknown) ? onCreateUnknownConnection(callManagerAccount, request) : isIncoming ? onCreateIncomingConnection(callManagerAccount, request) : onCreateOutgoingConnection(callManagerAccount, request);
        android.telecom.Log.d(this, "createConnection, connection: %s", connection);
        if (connection == null) {
            connection = android.telecom.Connection.createFailedConnection(new android.telecom.DisconnectCause(android.telecom.DisconnectCause.ERROR));
        }
        connection.setTelecomCallId(callId);
        if (connection.getState() != android.telecom.Connection.STATE_DISCONNECTED) {
            addConnection(callId, connection);
        }
        android.net.Uri address = connection.getAddress();
        java.lang.String number = (address == null) ? "null" : address.getSchemeSpecificPart();
        android.telecom.Log.v(this, "createConnection, number: %s, state: %s, capabilities: %s, properties: %s", android.telecom.Connection.toLogSafePhoneNumber(number), android.telecom.Connection.stateToString(connection.getState()), android.telecom.Connection.capabilitiesToString(connection.getConnectionCapabilities()), android.telecom.Connection.propertiesToString(connection.getConnectionProperties()));
        android.telecom.Log.d(this, "createConnection, calling handleCreateConnectionSuccessful %s", callId);
        mAdapter.handleCreateConnectionComplete(callId, request, new android.telecom.ParcelableConnection(request.getAccountHandle(), connection.getState(), connection.getConnectionCapabilities(), connection.getConnectionProperties(), connection.getAddress(), connection.getAddressPresentation(), connection.getCallerDisplayName(), connection.getCallerDisplayNamePresentation(), connection.getVideoProvider() == null ? null : connection.getVideoProvider().getInterface(), connection.getVideoState(), connection.isRingbackRequested(), connection.getAudioModeIsVoip(), connection.getConnectTimeMillis(), connection.getStatusHints(), connection.getDisconnectCause(), createIdList(connection.getConferenceables()), connection.getExtras()));
        if (isUnknown) {
            triggerConferenceRecalculate();
        }
    }

    private void abort(java.lang.String callId) {
        android.telecom.Log.d(this, "abort %s", callId);
        findConnectionForAction(callId, "abort").onAbort();
    }

    private void answerVideo(java.lang.String callId, int videoState) {
        android.telecom.Log.d(this, "answerVideo %s", callId);
        findConnectionForAction(callId, "answer").onAnswer(videoState);
    }

    private void answer(java.lang.String callId) {
        android.telecom.Log.d(this, "answer %s", callId);
        findConnectionForAction(callId, "answer").onAnswer();
    }

    private void reject(java.lang.String callId) {
        android.telecom.Log.d(this, "reject %s", callId);
        findConnectionForAction(callId, "reject").onReject();
    }

    private void reject(java.lang.String callId, java.lang.String rejectWithMessage) {
        android.telecom.Log.d(this, "reject %s with message", callId);
        findConnectionForAction(callId, "reject").onReject(rejectWithMessage);
    }

    private void silence(java.lang.String callId) {
        android.telecom.Log.d(this, "silence %s", callId);
        findConnectionForAction(callId, "silence").onSilence();
    }

    private void disconnect(java.lang.String callId) {
        android.telecom.Log.d(this, "disconnect %s", callId);
        if (mConnectionById.containsKey(callId)) {
            findConnectionForAction(callId, "disconnect").onDisconnect();
        } else {
            findConferenceForAction(callId, "disconnect").onDisconnect();
        }
    }

    private void hold(java.lang.String callId) {
        android.telecom.Log.d(this, "hold %s", callId);
        if (mConnectionById.containsKey(callId)) {
            findConnectionForAction(callId, "hold").onHold();
        } else {
            findConferenceForAction(callId, "hold").onHold();
        }
    }

    private void unhold(java.lang.String callId) {
        android.telecom.Log.d(this, "unhold %s", callId);
        if (mConnectionById.containsKey(callId)) {
            findConnectionForAction(callId, "unhold").onUnhold();
        } else {
            findConferenceForAction(callId, "unhold").onUnhold();
        }
    }

    private void onCallAudioStateChanged(java.lang.String callId, android.telecom.CallAudioState callAudioState) {
        android.telecom.Log.d(this, "onAudioStateChanged %s %s", callId, callAudioState);
        if (mConnectionById.containsKey(callId)) {
            findConnectionForAction(callId, "onCallAudioStateChanged").setCallAudioState(callAudioState);
        } else {
            findConferenceForAction(callId, "onCallAudioStateChanged").setCallAudioState(callAudioState);
        }
    }

    private void playDtmfTone(java.lang.String callId, char digit) {
        android.telecom.Log.d(this, "playDtmfTone %s %c", callId, digit);
        if (mConnectionById.containsKey(callId)) {
            findConnectionForAction(callId, "playDtmfTone").onPlayDtmfTone(digit);
        } else {
            findConferenceForAction(callId, "playDtmfTone").onPlayDtmfTone(digit);
        }
    }

    private void stopDtmfTone(java.lang.String callId) {
        android.telecom.Log.d(this, "stopDtmfTone %s", callId);
        if (mConnectionById.containsKey(callId)) {
            findConnectionForAction(callId, "stopDtmfTone").onStopDtmfTone();
        } else {
            findConferenceForAction(callId, "stopDtmfTone").onStopDtmfTone();
        }
    }

    private void conference(java.lang.String callId1, java.lang.String callId2) {
        android.telecom.Log.d(this, "conference %s, %s", callId1, callId2);
        // Attempt to get second connection or conference.
        android.telecom.Connection connection2 = findConnectionForAction(callId2, "conference");
        android.telecom.Conference conference2 = getNullConference();
        if (connection2 == android.telecom.ConnectionService.getNullConnection()) {
            conference2 = findConferenceForAction(callId2, "conference");
            if (conference2 == getNullConference()) {
                android.telecom.Log.w(this, "Connection2 or Conference2 missing in conference request %s.", callId2);
                return;
            }
        }
        // Attempt to get first connection or conference and perform merge.
        android.telecom.Connection connection1 = findConnectionForAction(callId1, "conference");
        if (connection1 == android.telecom.ConnectionService.getNullConnection()) {
            android.telecom.Conference conference1 = findConferenceForAction(callId1, "addConnection");
            if (conference1 == getNullConference()) {
                android.telecom.Log.w(this, "Connection1 or Conference1 missing in conference request %s.", callId1);
            } else {
                // Call 1 is a conference.
                if (connection2 != android.telecom.ConnectionService.getNullConnection()) {
                    // Call 2 is a connection so merge via call 1 (conference).
                    conference1.onMerge(connection2);
                } else {
                    // Call 2 is ALSO a conference; this should never happen.
                    android.telecom.Log.wtf(this, "There can only be one conference and an attempt was made to " + "merge two conferences.");
                    return;
                }
            }
        } else {
            // Call 1 is a connection.
            if (conference2 != getNullConference()) {
                // Call 2 is a conference, so merge via call 2.
                conference2.onMerge(connection1);
            } else {
                // Call 2 is a connection, so merge together.
                onConference(connection1, connection2);
            }
        }
    }

    private void splitFromConference(java.lang.String callId) {
        android.telecom.Log.d(this, "splitFromConference(%s)", callId);
        android.telecom.Connection connection = findConnectionForAction(callId, "splitFromConference");
        if (connection == android.telecom.ConnectionService.getNullConnection()) {
            android.telecom.Log.w(this, "Connection missing in conference request %s.", callId);
            return;
        }
        android.telecom.Conference conference = connection.getConference();
        if (conference != null) {
            conference.onSeparate(connection);
        }
    }

    private void mergeConference(java.lang.String callId) {
        android.telecom.Log.d(this, "mergeConference(%s)", callId);
        android.telecom.Conference conference = findConferenceForAction(callId, "mergeConference");
        if (conference != null) {
            conference.onMerge();
        }
    }

    private void swapConference(java.lang.String callId) {
        android.telecom.Log.d(this, "swapConference(%s)", callId);
        android.telecom.Conference conference = findConferenceForAction(callId, "swapConference");
        if (conference != null) {
            conference.onSwap();
        }
    }

    /**
     * Notifies a {@link Connection} of a request to pull an external call.
     *
     * See {@link Call#pullExternalCall()}.
     *
     * @param callId
     * 		The ID of the call to pull.
     */
    private void pullExternalCall(java.lang.String callId) {
        android.telecom.Log.d(this, "pullExternalCall(%s)", callId);
        android.telecom.Connection connection = findConnectionForAction(callId, "pullExternalCall");
        if (connection != null) {
            connection.onPullExternalCall();
        }
    }

    /**
     * Notifies a {@link Connection} of a call event.
     *
     * See {@link Call#sendCallEvent(String, Bundle)}.
     *
     * @param callId
     * 		The ID of the call receiving the event.
     * @param event
     * 		The event.
     * @param extras
     * 		Extras associated with the event.
     */
    private void sendCallEvent(java.lang.String callId, java.lang.String event, android.os.Bundle extras) {
        android.telecom.Log.d(this, "sendCallEvent(%s, %s)", callId, event);
        android.telecom.Connection connection = findConnectionForAction(callId, "sendCallEvent");
        if (connection != null) {
            connection.onCallEvent(event, extras);
        }
    }

    /**
     * Notifies a {@link Connection} or {@link Conference} of a change to the extras from Telecom.
     * <p>
     * These extra changes can originate from Telecom itself, or from an {@link InCallService} via
     * the {@link android.telecom.Call#putExtra(String, boolean)},
     * {@link android.telecom.Call#putExtra(String, int)},
     * {@link android.telecom.Call#putExtra(String, String)},
     * {@link Call#removeExtras(List)}.
     *
     * @param callId
     * 		The ID of the call receiving the event.
     * @param extras
     * 		The new extras bundle.
     */
    private void handleExtrasChanged(java.lang.String callId, android.os.Bundle extras) {
        android.telecom.Log.d(this, "handleExtrasChanged(%s, %s)", callId, extras);
        if (mConnectionById.containsKey(callId)) {
            findConnectionForAction(callId, "handleExtrasChanged").handleExtrasChanged(extras);
        } else
            if (mConferenceById.containsKey(callId)) {
                findConferenceForAction(callId, "handleExtrasChanged").handleExtrasChanged(extras);
            }

    }

    private void onPostDialContinue(java.lang.String callId, boolean proceed) {
        android.telecom.Log.d(this, "onPostDialContinue(%s)", callId);
        findConnectionForAction(callId, "stopDtmfTone").onPostDialContinue(proceed);
    }

    private void onAdapterAttached() {
        if (mAreAccountsInitialized) {
            // No need to query again if we already did it.
            return;
        }
        mAdapter.queryRemoteConnectionServices(new com.android.internal.telecom.RemoteServiceCallback.Stub() {
            @java.lang.Override
            public void onResult(final java.util.List<android.content.ComponentName> componentNames, final java.util.List<android.os.IBinder> services) {
                mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        for (int i = 0; (i < componentNames.size()) && (i < services.size()); i++) {
                            mRemoteConnectionManager.addConnectionService(componentNames.get(i), IConnectionService.Stub.asInterface(services.get(i)));
                        }
                        onAccountsInitialized();
                        android.telecom.Log.d(this, "remote connection services found: " + services);
                    }
                });
            }

            @java.lang.Override
            public void onError() {
                mHandler.post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        mAreAccountsInitialized = true;
                    }
                });
            }
        });
    }

    /**
     * Ask some other {@code ConnectionService} to create a {@code RemoteConnection} given an
     * incoming request. This is used by {@code ConnectionService}s that are registered with
     * {@link PhoneAccount#CAPABILITY_CONNECTION_MANAGER} and want to be able to manage
     * SIM-based incoming calls.
     *
     * @param connectionManagerPhoneAccount
     * 		See description at
     * 		{@link #onCreateOutgoingConnection(PhoneAccountHandle, ConnectionRequest)}.
     * @param request
     * 		Details about the incoming call.
     * @return The {@code Connection} object to satisfy this call, or {@code null} to
    not handle the call.
     */
    public final android.telecom.RemoteConnection createRemoteIncomingConnection(android.telecom.PhoneAccountHandle connectionManagerPhoneAccount, android.telecom.ConnectionRequest request) {
        return mRemoteConnectionManager.createRemoteConnection(connectionManagerPhoneAccount, request, true);
    }

    /**
     * Ask some other {@code ConnectionService} to create a {@code RemoteConnection} given an
     * outgoing request. This is used by {@code ConnectionService}s that are registered with
     * {@link PhoneAccount#CAPABILITY_CONNECTION_MANAGER} and want to be able to use the
     * SIM-based {@code ConnectionService} to place its outgoing calls.
     *
     * @param connectionManagerPhoneAccount
     * 		See description at
     * 		{@link #onCreateOutgoingConnection(PhoneAccountHandle, ConnectionRequest)}.
     * @param request
     * 		Details about the incoming call.
     * @return The {@code Connection} object to satisfy this call, or {@code null} to
    not handle the call.
     */
    public final android.telecom.RemoteConnection createRemoteOutgoingConnection(android.telecom.PhoneAccountHandle connectionManagerPhoneAccount, android.telecom.ConnectionRequest request) {
        return mRemoteConnectionManager.createRemoteConnection(connectionManagerPhoneAccount, request, false);
    }

    /**
     * Indicates to the relevant {@code RemoteConnectionService} that the specified
     * {@link RemoteConnection}s should be merged into a conference call.
     * <p>
     * If the conference request is successful, the method {@link #onRemoteConferenceAdded} will
     * be invoked.
     *
     * @param remoteConnection1
     * 		The first of the remote connections to conference.
     * @param remoteConnection2
     * 		The second of the remote connections to conference.
     */
    public final void conferenceRemoteConnections(android.telecom.RemoteConnection remoteConnection1, android.telecom.RemoteConnection remoteConnection2) {
        mRemoteConnectionManager.conferenceRemoteConnections(remoteConnection1, remoteConnection2);
    }

    /**
     * Adds a new conference call. When a conference call is created either as a result of an
     * explicit request via {@link #onConference} or otherwise, the connection service should supply
     * an instance of {@link Conference} by invoking this method. A conference call provided by this
     * method will persist until {@link Conference#destroy} is invoked on the conference instance.
     *
     * @param conference
     * 		The new conference object.
     */
    public final void addConference(android.telecom.Conference conference) {
        android.telecom.Log.d(this, "addConference: conference=%s", conference);
        java.lang.String id = addConferenceInternal(conference);
        if (id != null) {
            java.util.List<java.lang.String> connectionIds = new java.util.ArrayList<>(2);
            for (android.telecom.Connection connection : conference.getConnections()) {
                if (mIdByConnection.containsKey(connection)) {
                    connectionIds.add(mIdByConnection.get(connection));
                }
            }
            conference.setTelecomCallId(id);
            android.telecom.ParcelableConference parcelableConference = new android.telecom.ParcelableConference(conference.getPhoneAccountHandle(), conference.getState(), conference.getConnectionCapabilities(), conference.getConnectionProperties(), connectionIds, conference.getVideoProvider() == null ? null : conference.getVideoProvider().getInterface(), conference.getVideoState(), conference.getConnectTimeMillis(), conference.getStatusHints(), conference.getExtras());
            mAdapter.addConferenceCall(id, parcelableConference);
            mAdapter.setVideoProvider(id, conference.getVideoProvider());
            mAdapter.setVideoState(id, conference.getVideoState());
            // Go through any child calls and set the parent.
            for (android.telecom.Connection connection : conference.getConnections()) {
                java.lang.String connectionId = mIdByConnection.get(connection);
                if (connectionId != null) {
                    mAdapter.setIsConferenced(connectionId, id);
                }
            }
        }
    }

    /**
     * Adds a connection created by the {@link ConnectionService} and informs telecom of the new
     * connection.
     *
     * @param phoneAccountHandle
     * 		The phone account handle for the connection.
     * @param connection
     * 		The connection to add.
     */
    public final void addExistingConnection(android.telecom.PhoneAccountHandle phoneAccountHandle, android.telecom.Connection connection) {
        java.lang.String id = addExistingConnectionInternal(phoneAccountHandle, connection);
        if (id != null) {
            java.util.List<java.lang.String> emptyList = new java.util.ArrayList<>(0);
            android.telecom.ParcelableConnection parcelableConnection = new android.telecom.ParcelableConnection(phoneAccountHandle, connection.getState(), connection.getConnectionCapabilities(), connection.getConnectionProperties(), connection.getAddress(), connection.getAddressPresentation(), connection.getCallerDisplayName(), connection.getCallerDisplayNamePresentation(), connection.getVideoProvider() == null ? null : connection.getVideoProvider().getInterface(), connection.getVideoState(), connection.isRingbackRequested(), connection.getAudioModeIsVoip(), connection.getConnectTimeMillis(), connection.getStatusHints(), connection.getDisconnectCause(), emptyList, connection.getExtras());
            mAdapter.addExistingConnection(id, parcelableConnection);
        }
    }

    /**
     * Returns all the active {@code Connection}s for which this {@code ConnectionService}
     * has taken responsibility.
     *
     * @return A collection of {@code Connection}s created by this {@code ConnectionService}.
     */
    public final java.util.Collection<android.telecom.Connection> getAllConnections() {
        return mConnectionById.values();
    }

    /**
     * Returns all the active {@code Conference}s for which this {@code ConnectionService}
     * has taken responsibility.
     *
     * @return A collection of {@code Conference}s created by this {@code ConnectionService}.
     */
    public final java.util.Collection<android.telecom.Conference> getAllConferences() {
        return mConferenceById.values();
    }

    /**
     * Create a {@code Connection} given an incoming request. This is used to attach to existing
     * incoming calls.
     *
     * @param connectionManagerPhoneAccount
     * 		See description at
     * 		{@link #onCreateOutgoingConnection(PhoneAccountHandle, ConnectionRequest)}.
     * @param request
     * 		Details about the incoming call.
     * @return The {@code Connection} object to satisfy this call, or {@code null} to
    not handle the call.
     */
    public android.telecom.Connection onCreateIncomingConnection(android.telecom.PhoneAccountHandle connectionManagerPhoneAccount, android.telecom.ConnectionRequest request) {
        return null;
    }

    /**
     * Trigger recalculate functinality for conference calls. This is used when a Telephony
     * Connection is part of a conference controller but is not yet added to Connection
     * Service and hence cannot be added to the conference call.
     *
     * @unknown 
     */
    public void triggerConferenceRecalculate() {
    }

    /**
     * Create a {@code Connection} given an outgoing request. This is used to initiate new
     * outgoing calls.
     *
     * @param connectionManagerPhoneAccount
     * 		The connection manager account to use for managing
     * 		this call.
     * 		<p>
     * 		If this parameter is not {@code null}, it means that this {@code ConnectionService}
     * 		has registered one or more {@code PhoneAccount}s having
     * 		{@link PhoneAccount#CAPABILITY_CONNECTION_MANAGER}. This parameter will contain
     * 		one of these {@code PhoneAccount}s, while the {@code request} will contain another
     * 		(usually but not always distinct) {@code PhoneAccount} to be used for actually
     * 		making the connection.
     * 		<p>
     * 		If this parameter is {@code null}, it means that this {@code ConnectionService} is
     * 		being asked to make a direct connection. The
     * 		{@link ConnectionRequest#getAccountHandle()} of parameter {@code request} will be
     * 		a {@code PhoneAccount} registered by this {@code ConnectionService} to use for
     * 		making the connection.
     * @param request
     * 		Details about the outgoing call.
     * @return The {@code Connection} object to satisfy this call, or the result of an invocation
    of {@link Connection#createFailedConnection(DisconnectCause)} to not handle the call.
     */
    public android.telecom.Connection onCreateOutgoingConnection(android.telecom.PhoneAccountHandle connectionManagerPhoneAccount, android.telecom.ConnectionRequest request) {
        return null;
    }

    /**
     * Create a {@code Connection} for a new unknown call. An unknown call is a call originating
     * from the ConnectionService that was neither a user-initiated outgoing call, nor an incoming
     * call created using
     * {@code TelecomManager#addNewIncomingCall(PhoneAccountHandle, android.os.Bundle)}.
     *
     * @param connectionManagerPhoneAccount
     * 		
     * @param request
     * 		
     * @return 
     * @unknown 
     */
    public android.telecom.Connection onCreateUnknownConnection(android.telecom.PhoneAccountHandle connectionManagerPhoneAccount, android.telecom.ConnectionRequest request) {
        return null;
    }

    /**
     * Conference two specified connections. Invoked when the user has made a request to merge the
     * specified connections into a conference call. In response, the connection service should
     * create an instance of {@link Conference} and pass it into {@link #addConference}.
     *
     * @param connection1
     * 		A connection to merge into a conference call.
     * @param connection2
     * 		A connection to merge into a conference call.
     */
    public void onConference(android.telecom.Connection connection1, android.telecom.Connection connection2) {
    }

    /**
     * Indicates that a remote conference has been created for existing {@link RemoteConnection}s.
     * When this method is invoked, this {@link ConnectionService} should create its own
     * representation of the conference call and send it to telecom using {@link #addConference}.
     * <p>
     * This is only relevant to {@link ConnectionService}s which are registered with
     * {@link PhoneAccount#CAPABILITY_CONNECTION_MANAGER}.
     *
     * @param conference
     * 		The remote conference call.
     */
    public void onRemoteConferenceAdded(android.telecom.RemoteConference conference) {
    }

    /**
     * Called when an existing connection is added remotely.
     *
     * @param connection
     * 		The existing connection which was added.
     */
    public void onRemoteExistingConnectionAdded(android.telecom.RemoteConnection connection) {
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean containsConference(android.telecom.Conference conference) {
        return mIdByConference.containsKey(conference);
    }

    /**
     * {@hide }
     */
    void addRemoteConference(android.telecom.RemoteConference remoteConference) {
        onRemoteConferenceAdded(remoteConference);
    }

    /**
     * {@hide }
     */
    void addRemoteExistingConnection(android.telecom.RemoteConnection remoteConnection) {
        onRemoteExistingConnectionAdded(remoteConnection);
    }

    private void onAccountsInitialized() {
        mAreAccountsInitialized = true;
        for (java.lang.Runnable r : mPreInitializationConnectionRequests) {
            r.run();
        }
        mPreInitializationConnectionRequests.clear();
    }

    /**
     * Adds an existing connection to the list of connections, identified by a new call ID unique
     * to this connection service.
     *
     * @param connection
     * 		The connection.
     * @return The ID of the connection (e.g. the call-id).
     */
    private java.lang.String addExistingConnectionInternal(android.telecom.PhoneAccountHandle handle, android.telecom.Connection connection) {
        java.lang.String id;
        if ((connection.getExtras() != null) && connection.getExtras().containsKey(android.telecom.Connection.EXTRA_ORIGINAL_CONNECTION_ID)) {
            id = connection.getExtras().getString(android.telecom.Connection.EXTRA_ORIGINAL_CONNECTION_ID);
            android.telecom.Log.d(this, "addExistingConnectionInternal - conn %s reusing original id %s", connection.getTelecomCallId(), id);
        } else
            if (handle == null) {
                // If no phone account handle was provided, we cannot be sure the call ID is unique,
                // so just use a random UUID.
                id = java.util.UUID.randomUUID().toString();
            } else {
                // Phone account handle was provided, so use the ConnectionService class name as a
                // prefix for a unique incremental call ID.
                id = (handle.getComponentName().getClassName() + "@") + getNextCallId();
            }

        addConnection(id, connection);
        return id;
    }

    private void addConnection(java.lang.String callId, android.telecom.Connection connection) {
        connection.setTelecomCallId(callId);
        mConnectionById.put(callId, connection);
        mIdByConnection.put(connection, callId);
        connection.addConnectionListener(mConnectionListener);
        connection.setConnectionService(this);
    }

    /**
     * {@hide }
     */
    protected void removeConnection(android.telecom.Connection connection) {
        connection.unsetConnectionService(this);
        connection.removeConnectionListener(mConnectionListener);
        java.lang.String id = mIdByConnection.get(connection);
        if (id != null) {
            mConnectionById.remove(id);
            mIdByConnection.remove(connection);
            mAdapter.removeCall(id);
        }
    }

    private java.lang.String addConferenceInternal(android.telecom.Conference conference) {
        java.lang.String originalId = null;
        if ((conference.getExtras() != null) && conference.getExtras().containsKey(android.telecom.Connection.EXTRA_ORIGINAL_CONNECTION_ID)) {
            originalId = conference.getExtras().getString(android.telecom.Connection.EXTRA_ORIGINAL_CONNECTION_ID);
            android.telecom.Log.d(this, "addConferenceInternal: conf %s reusing original id %s", conference.getTelecomCallId(), originalId);
        }
        if (mIdByConference.containsKey(conference)) {
            android.telecom.Log.w(this, "Re-adding an existing conference: %s.", conference);
        } else
            if (conference != null) {
                // Conferences do not (yet) have a PhoneAccountHandle associated with them, so we
                // cannot determine a ConnectionService class name to associate with the ID, so use
                // a unique UUID (for now).
                java.lang.String id = (originalId == null) ? java.util.UUID.randomUUID().toString() : originalId;
                mConferenceById.put(id, conference);
                mIdByConference.put(conference, id);
                conference.addListener(mConferenceListener);
                return id;
            }

        return null;
    }

    private void removeConference(android.telecom.Conference conference) {
        if (mIdByConference.containsKey(conference)) {
            conference.removeListener(mConferenceListener);
            java.lang.String id = mIdByConference.get(conference);
            mConferenceById.remove(id);
            mIdByConference.remove(conference);
            mAdapter.removeCall(id);
        }
    }

    private android.telecom.Connection findConnectionForAction(java.lang.String callId, java.lang.String action) {
        if (mConnectionById.containsKey(callId)) {
            return mConnectionById.get(callId);
        }
        android.telecom.Log.w(this, "%s - Cannot find Connection %s", action, callId);
        return android.telecom.ConnectionService.getNullConnection();
    }

    static synchronized android.telecom.Connection getNullConnection() {
        if (android.telecom.ConnectionService.sNullConnection == null) {
            android.telecom.ConnectionService.sNullConnection = new android.telecom.Connection() {};
        }
        return android.telecom.ConnectionService.sNullConnection;
    }

    private android.telecom.Conference findConferenceForAction(java.lang.String conferenceId, java.lang.String action) {
        if (mConferenceById.containsKey(conferenceId)) {
            return mConferenceById.get(conferenceId);
        }
        android.telecom.Log.w(this, "%s - Cannot find conference %s", action, conferenceId);
        return getNullConference();
    }

    private java.util.List<java.lang.String> createConnectionIdList(java.util.List<android.telecom.Connection> connections) {
        java.util.List<java.lang.String> ids = new java.util.ArrayList<>();
        for (android.telecom.Connection c : connections) {
            if (mIdByConnection.containsKey(c)) {
                ids.add(mIdByConnection.get(c));
            }
        }
        java.util.Collections.sort(ids);
        return ids;
    }

    /**
     * Builds a list of {@link Connection} and {@link Conference} IDs based on the list of
     * {@link Conferenceable}s passed in.
     *
     * @param conferenceables
     * 		The {@link Conferenceable} connections and conferences.
     * @return List of string conference and call Ids.
     */
    private java.util.List<java.lang.String> createIdList(java.util.List<android.telecom.Conferenceable> conferenceables) {
        java.util.List<java.lang.String> ids = new java.util.ArrayList<>();
        for (android.telecom.Conferenceable c : conferenceables) {
            // Only allow Connection and Conference conferenceables.
            if (c instanceof android.telecom.Connection) {
                android.telecom.Connection connection = ((android.telecom.Connection) (c));
                if (mIdByConnection.containsKey(connection)) {
                    ids.add(mIdByConnection.get(connection));
                }
            } else
                if (c instanceof android.telecom.Conference) {
                    android.telecom.Conference conference = ((android.telecom.Conference) (c));
                    if (mIdByConference.containsKey(conference)) {
                        ids.add(mIdByConference.get(conference));
                    }
                }

        }
        java.util.Collections.sort(ids);
        return ids;
    }

    private android.telecom.Conference getNullConference() {
        if (sNullConference == null) {
            sNullConference = new android.telecom.Conference(null) {};
        }
        return sNullConference;
    }

    private void endAllConnections() {
        // Unbound from telecomm.  We should end all connections and conferences.
        for (android.telecom.Connection connection : mIdByConnection.keySet()) {
            // only operate on top-level calls. Conference calls will be removed on their own.
            if (connection.getConference() == null) {
                connection.onDisconnect();
            }
        }
        for (android.telecom.Conference conference : mIdByConference.keySet()) {
            conference.onDisconnect();
        }
    }

    /**
     * Retrieves the next call ID as maintainted by the connection service.
     *
     * @return The call ID.
     */
    private int getNextCallId() {
        synchronized(mIdSyncRoot) {
            return ++mId;
        }
    }
}

