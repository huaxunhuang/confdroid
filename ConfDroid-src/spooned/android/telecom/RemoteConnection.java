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
 * A connection provided to a {@link ConnectionService} by another {@code ConnectionService}
 * running in a different process.
 *
 * @see ConnectionService#createRemoteOutgoingConnection(PhoneAccountHandle, ConnectionRequest)
 * @see ConnectionService#createRemoteIncomingConnection(PhoneAccountHandle, ConnectionRequest)
 */
public final class RemoteConnection {
    /**
     * Callback base class for {@link RemoteConnection}.
     */
    public static abstract class Callback {
        /**
         * Invoked when the state of this {@code RemoteConnection} has changed. See
         * {@link #getState()}.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param state
         * 		The new state of the {@code RemoteConnection}.
         */
        public void onStateChanged(android.telecom.RemoteConnection connection, int state) {
        }

        /**
         * Invoked when this {@code RemoteConnection} is disconnected.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param disconnectCause
         * 		The ({@see DisconnectCause}) associated with this failed
         * 		connection.
         */
        public void onDisconnected(android.telecom.RemoteConnection connection, android.telecom.DisconnectCause disconnectCause) {
        }

        /**
         * Invoked when this {@code RemoteConnection} is requesting ringback. See
         * {@link #isRingbackRequested()}.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param ringback
         * 		Whether the {@code RemoteConnection} is requesting ringback.
         */
        public void onRingbackRequested(android.telecom.RemoteConnection connection, boolean ringback) {
        }

        /**
         * Indicates that the call capabilities of this {@code RemoteConnection} have changed.
         * See {@link #getConnectionCapabilities()}.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param connectionCapabilities
         * 		The new capabilities of the {@code RemoteConnection}.
         */
        public void onConnectionCapabilitiesChanged(android.telecom.RemoteConnection connection, int connectionCapabilities) {
        }

        /**
         * Indicates that the call properties of this {@code RemoteConnection} have changed.
         * See {@link #getConnectionProperties()}.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param connectionProperties
         * 		The new properties of the {@code RemoteConnection}.
         */
        public void onConnectionPropertiesChanged(android.telecom.RemoteConnection connection, int connectionProperties) {
        }

        /**
         * Invoked when the post-dial sequence in the outgoing {@code Connection} has reached a
         * pause character. This causes the post-dial signals to stop pending user confirmation. An
         * implementation should present this choice to the user and invoke
         * {@link RemoteConnection#postDialContinue(boolean)} when the user makes the choice.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param remainingPostDialSequence
         * 		The post-dial characters that remain to be sent.
         */
        public void onPostDialWait(android.telecom.RemoteConnection connection, java.lang.String remainingPostDialSequence) {
        }

        /**
         * Invoked when the post-dial sequence in the outgoing {@code Connection} has processed
         * a character.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param nextChar
         * 		The character being processed.
         */
        public void onPostDialChar(android.telecom.RemoteConnection connection, char nextChar) {
        }

        /**
         * Indicates that the VOIP audio status of this {@code RemoteConnection} has changed.
         * See {@link #isVoipAudioMode()}.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param isVoip
         * 		Whether the new audio state of the {@code RemoteConnection} is VOIP.
         */
        public void onVoipAudioChanged(android.telecom.RemoteConnection connection, boolean isVoip) {
        }

        /**
         * Indicates that the status hints of this {@code RemoteConnection} have changed. See
         * {@link #getStatusHints()} ()}.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param statusHints
         * 		The new status hints of the {@code RemoteConnection}.
         */
        public void onStatusHintsChanged(android.telecom.RemoteConnection connection, android.telecom.StatusHints statusHints) {
        }

        /**
         * Indicates that the address (e.g., phone number) of this {@code RemoteConnection} has
         * changed. See {@link #getAddress()} and {@link #getAddressPresentation()}.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param address
         * 		The new address of the {@code RemoteConnection}.
         * @param presentation
         * 		The presentation requirements for the address.
         * 		See {@link TelecomManager} for valid values.
         */
        public void onAddressChanged(android.telecom.RemoteConnection connection, android.net.Uri address, int presentation) {
        }

        /**
         * Indicates that the caller display name of this {@code RemoteConnection} has changed.
         * See {@link #getCallerDisplayName()} and {@link #getCallerDisplayNamePresentation()}.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param callerDisplayName
         * 		The new caller display name of the {@code RemoteConnection}.
         * @param presentation
         * 		The presentation requirements for the handle.
         * 		See {@link TelecomManager} for valid values.
         */
        public void onCallerDisplayNameChanged(android.telecom.RemoteConnection connection, java.lang.String callerDisplayName, int presentation) {
        }

        /**
         * Indicates that the video state of this {@code RemoteConnection} has changed.
         * See {@link #getVideoState()}.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param videoState
         * 		The new video state of the {@code RemoteConnection}.
         */
        public void onVideoStateChanged(android.telecom.RemoteConnection connection, int videoState) {
        }

        /**
         * Indicates that this {@code RemoteConnection} has been destroyed. No further requests
         * should be made to the {@code RemoteConnection}, and references to it should be cleared.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         */
        public void onDestroyed(android.telecom.RemoteConnection connection) {
        }

        /**
         * Indicates that the {@code RemoteConnection}s with which this {@code RemoteConnection}
         * may be asked to create a conference has changed.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param conferenceableConnections
         * 		The {@code RemoteConnection}s with which this
         * 		{@code RemoteConnection} may be asked to create a conference.
         */
        public void onConferenceableConnectionsChanged(android.telecom.RemoteConnection connection, java.util.List<android.telecom.RemoteConnection> conferenceableConnections) {
        }

        /**
         * Indicates that the {@code VideoProvider} associated with this {@code RemoteConnection}
         * has changed.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param videoProvider
         * 		The new {@code VideoProvider} associated with this
         * 		{@code RemoteConnection}.
         */
        public void onVideoProviderChanged(android.telecom.RemoteConnection connection, android.telecom.RemoteConnection.VideoProvider videoProvider) {
        }

        /**
         * Indicates that the {@code RemoteConference} that this {@code RemoteConnection} is a part
         * of has changed.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param conference
         * 		The {@code RemoteConference} of which this {@code RemoteConnection} is
         * 		a part, which may be {@code null}.
         */
        public void onConferenceChanged(android.telecom.RemoteConnection connection, android.telecom.RemoteConference conference) {
        }

        /**
         * Handles changes to the {@code RemoteConnection} extras.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param extras
         * 		The extras containing other information associated with the connection.
         */
        public void onExtrasChanged(android.telecom.RemoteConnection connection, @android.annotation.Nullable
        android.os.Bundle extras) {
        }

        /**
         * Handles a connection event propagated to this {@link RemoteConnection}.
         * <p>
         * Connection events originate from {@link Connection#sendConnectionEvent(String, Bundle)}.
         *
         * @param connection
         * 		The {@code RemoteConnection} invoking this method.
         * @param event
         * 		The connection event.
         * @param extras
         * 		Extras associated with the event.
         */
        public void onConnectionEvent(android.telecom.RemoteConnection connection, java.lang.String event, android.os.Bundle extras) {
        }
    }

    /**
     * {@link RemoteConnection.VideoProvider} associated with a {@link RemoteConnection}.  Used to
     * receive video related events and control the video associated with a
     * {@link RemoteConnection}.
     *
     * @see Connection.VideoProvider
     */
    public static class VideoProvider {
        /**
         * Callback class used by the {@link RemoteConnection.VideoProvider} to relay events from
         * the {@link Connection.VideoProvider}.
         */
        public static abstract class Callback {
            /**
             * Reports a session modification request received from the
             * {@link Connection.VideoProvider} associated with a {@link RemoteConnection}.
             *
             * @param videoProvider
             * 		The {@link RemoteConnection.VideoProvider} invoking this method.
             * @param videoProfile
             * 		The requested video call profile.
             * @see InCallService.VideoCall.Callback#onSessionModifyRequestReceived(VideoProfile)
             * @see Connection.VideoProvider#receiveSessionModifyRequest(VideoProfile)
             */
            public void onSessionModifyRequestReceived(android.telecom.RemoteConnection.VideoProvider videoProvider, android.telecom.VideoProfile videoProfile) {
            }

            /**
             * Reports a session modification response received from the
             * {@link Connection.VideoProvider} associated with a {@link RemoteConnection}.
             *
             * @param videoProvider
             * 		The {@link RemoteConnection.VideoProvider} invoking this method.
             * @param status
             * 		Status of the session modify request.
             * @param requestedProfile
             * 		The original request which was sent to the peer device.
             * @param responseProfile
             * 		The actual profile changes made by the peer device.
             * @see InCallService.VideoCall.Callback#onSessionModifyResponseReceived(int,
            VideoProfile, VideoProfile)
             * @see Connection.VideoProvider#receiveSessionModifyResponse(int, VideoProfile,
            VideoProfile)
             */
            public void onSessionModifyResponseReceived(android.telecom.RemoteConnection.VideoProvider videoProvider, int status, android.telecom.VideoProfile requestedProfile, android.telecom.VideoProfile responseProfile) {
            }

            /**
             * Reports a call session event received from the {@link Connection.VideoProvider}
             * associated with a {@link RemoteConnection}.
             *
             * @param videoProvider
             * 		The {@link RemoteConnection.VideoProvider} invoking this method.
             * @param event
             * 		The event.
             * @see InCallService.VideoCall.Callback#onCallSessionEvent(int)
             * @see Connection.VideoProvider#handleCallSessionEvent(int)
             */
            public void onCallSessionEvent(android.telecom.RemoteConnection.VideoProvider videoProvider, int event) {
            }

            /**
             * Reports a change in the peer video dimensions received from the
             * {@link Connection.VideoProvider} associated with a {@link RemoteConnection}.
             *
             * @param videoProvider
             * 		The {@link RemoteConnection.VideoProvider} invoking this method.
             * @param width
             * 		The updated peer video width.
             * @param height
             * 		The updated peer video height.
             * @see InCallService.VideoCall.Callback#onPeerDimensionsChanged(int, int)
             * @see Connection.VideoProvider#changePeerDimensions(int, int)
             */
            public void onPeerDimensionsChanged(android.telecom.RemoteConnection.VideoProvider videoProvider, int width, int height) {
            }

            /**
             * Reports a change in the data usage (in bytes) received from the
             * {@link Connection.VideoProvider} associated with a {@link RemoteConnection}.
             *
             * @param videoProvider
             * 		The {@link RemoteConnection.VideoProvider} invoking this method.
             * @param dataUsage
             * 		The updated data usage (in bytes).
             * @see InCallService.VideoCall.Callback#onCallDataUsageChanged(long)
             * @see Connection.VideoProvider#setCallDataUsage(long)
             */
            public void onCallDataUsageChanged(android.telecom.RemoteConnection.VideoProvider videoProvider, long dataUsage) {
            }

            /**
             * Reports a change in the capabilities of the current camera, received from the
             * {@link Connection.VideoProvider} associated with a {@link RemoteConnection}.
             *
             * @param videoProvider
             * 		The {@link RemoteConnection.VideoProvider} invoking this method.
             * @param cameraCapabilities
             * 		The changed camera capabilities.
             * @see InCallService.VideoCall.Callback#onCameraCapabilitiesChanged(
            VideoProfile.CameraCapabilities)
             * @see Connection.VideoProvider#changeCameraCapabilities(
            VideoProfile.CameraCapabilities)
             */
            public void onCameraCapabilitiesChanged(android.telecom.RemoteConnection.VideoProvider videoProvider, android.telecom.VideoProfile.CameraCapabilities cameraCapabilities) {
            }

            /**
             * Reports a change in the video quality received from the
             * {@link Connection.VideoProvider} associated with a {@link RemoteConnection}.
             *
             * @param videoProvider
             * 		The {@link RemoteConnection.VideoProvider} invoking this method.
             * @param videoQuality
             * 		The updated peer video quality.
             * @see InCallService.VideoCall.Callback#onVideoQualityChanged(int)
             * @see Connection.VideoProvider#changeVideoQuality(int)
             */
            public void onVideoQualityChanged(android.telecom.RemoteConnection.VideoProvider videoProvider, int videoQuality) {
            }
        }

        private final com.android.internal.telecom.IVideoCallback mVideoCallbackDelegate = new com.android.internal.telecom.IVideoCallback() {
            @java.lang.Override
            public void receiveSessionModifyRequest(android.telecom.VideoProfile videoProfile) {
                for (android.telecom.RemoteConnection.VideoProvider.Callback l : mCallbacks) {
                    l.onSessionModifyRequestReceived(android.telecom.RemoteConnection.VideoProvider.this, videoProfile);
                }
            }

            @java.lang.Override
            public void receiveSessionModifyResponse(int status, android.telecom.VideoProfile requestedProfile, android.telecom.VideoProfile responseProfile) {
                for (android.telecom.RemoteConnection.VideoProvider.Callback l : mCallbacks) {
                    l.onSessionModifyResponseReceived(android.telecom.RemoteConnection.VideoProvider.this, status, requestedProfile, responseProfile);
                }
            }

            @java.lang.Override
            public void handleCallSessionEvent(int event) {
                for (android.telecom.RemoteConnection.VideoProvider.Callback l : mCallbacks) {
                    l.onCallSessionEvent(android.telecom.RemoteConnection.VideoProvider.this, event);
                }
            }

            @java.lang.Override
            public void changePeerDimensions(int width, int height) {
                for (android.telecom.RemoteConnection.VideoProvider.Callback l : mCallbacks) {
                    l.onPeerDimensionsChanged(android.telecom.RemoteConnection.VideoProvider.this, width, height);
                }
            }

            @java.lang.Override
            public void changeCallDataUsage(long dataUsage) {
                for (android.telecom.RemoteConnection.VideoProvider.Callback l : mCallbacks) {
                    l.onCallDataUsageChanged(android.telecom.RemoteConnection.VideoProvider.this, dataUsage);
                }
            }

            @java.lang.Override
            public void changeCameraCapabilities(android.telecom.VideoProfile.CameraCapabilities cameraCapabilities) {
                for (android.telecom.RemoteConnection.VideoProvider.Callback l : mCallbacks) {
                    l.onCameraCapabilitiesChanged(android.telecom.RemoteConnection.VideoProvider.this, cameraCapabilities);
                }
            }

            @java.lang.Override
            public void changeVideoQuality(int videoQuality) {
                for (android.telecom.RemoteConnection.VideoProvider.Callback l : mCallbacks) {
                    l.onVideoQualityChanged(android.telecom.RemoteConnection.VideoProvider.this, videoQuality);
                }
            }

            @java.lang.Override
            public android.os.IBinder asBinder() {
                return null;
            }
        };

        private final android.telecom.VideoCallbackServant mVideoCallbackServant = new android.telecom.VideoCallbackServant(mVideoCallbackDelegate);

        private final com.android.internal.telecom.IVideoProvider mVideoProviderBinder;

        /**
         * ConcurrentHashMap constructor params: 8 is initial table size, 0.9f is
         * load factor before resizing, 1 means we only expect a single thread to
         * access the map so make only a single shard
         */
        private final java.util.Set<android.telecom.RemoteConnection.VideoProvider.Callback> mCallbacks = java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<android.telecom.RemoteConnection.VideoProvider.Callback, java.lang.Boolean>(8, 0.9F, 1));

        VideoProvider(com.android.internal.telecom.IVideoProvider videoProviderBinder) {
            mVideoProviderBinder = videoProviderBinder;
            try {
                mVideoProviderBinder.addVideoCallback(mVideoCallbackServant.getStub().asBinder());
            } catch (android.os.RemoteException e) {
            }
        }

        /**
         * Registers a callback to receive commands and state changes for video calls.
         *
         * @param l
         * 		The video call callback.
         */
        public void registerCallback(android.telecom.RemoteConnection.VideoProvider.Callback l) {
            mCallbacks.add(l);
        }

        /**
         * Clears the video call callback set via {@link #registerCallback}.
         *
         * @param l
         * 		The video call callback to clear.
         */
        public void unregisterCallback(android.telecom.RemoteConnection.VideoProvider.Callback l) {
            mCallbacks.remove(l);
        }

        /**
         * Sets the camera to be used for the outgoing video for the
         * {@link RemoteConnection.VideoProvider}.
         *
         * @param cameraId
         * 		The id of the camera (use ids as reported by
         * 		{@link CameraManager#getCameraIdList()}).
         * @see Connection.VideoProvider#onSetCamera(String)
         */
        public void setCamera(java.lang.String cameraId) {
            try {
                mVideoProviderBinder.setCamera(cameraId);
            } catch (android.os.RemoteException e) {
            }
        }

        /**
         * Sets the surface to be used for displaying a preview of what the user's camera is
         * currently capturing for the {@link RemoteConnection.VideoProvider}.
         *
         * @param surface
         * 		The {@link Surface}.
         * @see Connection.VideoProvider#onSetPreviewSurface(Surface)
         */
        public void setPreviewSurface(android.view.Surface surface) {
            try {
                mVideoProviderBinder.setPreviewSurface(surface);
            } catch (android.os.RemoteException e) {
            }
        }

        /**
         * Sets the surface to be used for displaying the video received from the remote device for
         * the {@link RemoteConnection.VideoProvider}.
         *
         * @param surface
         * 		The {@link Surface}.
         * @see Connection.VideoProvider#onSetDisplaySurface(Surface)
         */
        public void setDisplaySurface(android.view.Surface surface) {
            try {
                mVideoProviderBinder.setDisplaySurface(surface);
            } catch (android.os.RemoteException e) {
            }
        }

        /**
         * Sets the device orientation, in degrees, for the {@link RemoteConnection.VideoProvider}.
         * Assumes that a standard portrait orientation of the device is 0 degrees.
         *
         * @param rotation
         * 		The device orientation, in degrees.
         * @see Connection.VideoProvider#onSetDeviceOrientation(int)
         */
        public void setDeviceOrientation(int rotation) {
            try {
                mVideoProviderBinder.setDeviceOrientation(rotation);
            } catch (android.os.RemoteException e) {
            }
        }

        /**
         * Sets camera zoom ratio for the {@link RemoteConnection.VideoProvider}.
         *
         * @param value
         * 		The camera zoom ratio.
         * @see Connection.VideoProvider#onSetZoom(float)
         */
        public void setZoom(float value) {
            try {
                mVideoProviderBinder.setZoom(value);
            } catch (android.os.RemoteException e) {
            }
        }

        /**
         * Issues a request to modify the properties of the current video session for the
         * {@link RemoteConnection.VideoProvider}.
         *
         * @param fromProfile
         * 		The video profile prior to the request.
         * @param toProfile
         * 		The video profile with the requested changes made.
         * @see Connection.VideoProvider#onSendSessionModifyRequest(VideoProfile, VideoProfile)
         */
        public void sendSessionModifyRequest(android.telecom.VideoProfile fromProfile, android.telecom.VideoProfile toProfile) {
            try {
                mVideoProviderBinder.sendSessionModifyRequest(fromProfile, toProfile);
            } catch (android.os.RemoteException e) {
            }
        }

        /**
         * Provides a response to a request to change the current call video session
         * properties for the {@link RemoteConnection.VideoProvider}.
         *
         * @param responseProfile
         * 		The response call video properties.
         * @see Connection.VideoProvider#onSendSessionModifyResponse(VideoProfile)
         */
        public void sendSessionModifyResponse(android.telecom.VideoProfile responseProfile) {
            try {
                mVideoProviderBinder.sendSessionModifyResponse(responseProfile);
            } catch (android.os.RemoteException e) {
            }
        }

        /**
         * Issues a request to retrieve the capabilities of the current camera for the
         * {@link RemoteConnection.VideoProvider}.
         *
         * @see Connection.VideoProvider#onRequestCameraCapabilities()
         */
        public void requestCameraCapabilities() {
            try {
                mVideoProviderBinder.requestCameraCapabilities();
            } catch (android.os.RemoteException e) {
            }
        }

        /**
         * Issues a request to retrieve the data usage (in bytes) of the video portion of the
         * {@link RemoteConnection} for the {@link RemoteConnection.VideoProvider}.
         *
         * @see Connection.VideoProvider#onRequestConnectionDataUsage()
         */
        public void requestCallDataUsage() {
            try {
                mVideoProviderBinder.requestCallDataUsage();
            } catch (android.os.RemoteException e) {
            }
        }

        /**
         * Sets the {@link Uri} of an image to be displayed to the peer device when the video signal
         * is paused, for the {@link RemoteConnection.VideoProvider}.
         *
         * @see Connection.VideoProvider#onSetPauseImage(Uri)
         */
        public void setPauseImage(android.net.Uri uri) {
            try {
                mVideoProviderBinder.setPauseImage(uri);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    private com.android.internal.telecom.IConnectionService mConnectionService;

    private final java.lang.String mConnectionId;

    /**
     * ConcurrentHashMap constructor params: 8 is initial table size, 0.9f is
     * load factor before resizing, 1 means we only expect a single thread to
     * access the map so make only a single shard
     */
    private final java.util.Set<android.telecom.RemoteConnection.CallbackRecord> mCallbackRecords = java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<android.telecom.RemoteConnection.CallbackRecord, java.lang.Boolean>(8, 0.9F, 1));

    private final java.util.List<android.telecom.RemoteConnection> mConferenceableConnections = new java.util.ArrayList<>();

    private final java.util.List<android.telecom.RemoteConnection> mUnmodifiableconferenceableConnections = java.util.Collections.unmodifiableList(mConferenceableConnections);

    private int mState = android.telecom.Connection.STATE_NEW;

    private android.telecom.DisconnectCause mDisconnectCause;

    private boolean mRingbackRequested;

    private boolean mConnected;

    private int mConnectionCapabilities;

    private int mConnectionProperties;

    private int mVideoState;

    private android.telecom.RemoteConnection.VideoProvider mVideoProvider;

    private boolean mIsVoipAudioMode;

    private android.telecom.StatusHints mStatusHints;

    private android.net.Uri mAddress;

    private int mAddressPresentation;

    private java.lang.String mCallerDisplayName;

    private int mCallerDisplayNamePresentation;

    private android.telecom.RemoteConference mConference;

    private android.os.Bundle mExtras;

    /**
     *
     *
     * @unknown 
     */
    RemoteConnection(java.lang.String id, com.android.internal.telecom.IConnectionService connectionService, android.telecom.ConnectionRequest request) {
        mConnectionId = id;
        mConnectionService = connectionService;
        mConnected = true;
        mState = android.telecom.Connection.STATE_INITIALIZING;
    }

    /**
     *
     *
     * @unknown 
     */
    RemoteConnection(java.lang.String callId, com.android.internal.telecom.IConnectionService connectionService, android.telecom.ParcelableConnection connection) {
        mConnectionId = callId;
        mConnectionService = connectionService;
        mConnected = true;
        mState = connection.getState();
        mDisconnectCause = connection.getDisconnectCause();
        mRingbackRequested = connection.isRingbackRequested();
        mConnectionCapabilities = connection.getConnectionCapabilities();
        mConnectionProperties = connection.getConnectionProperties();
        mVideoState = connection.getVideoState();
        com.android.internal.telecom.IVideoProvider videoProvider = connection.getVideoProvider();
        if (videoProvider != null) {
            mVideoProvider = new android.telecom.RemoteConnection.VideoProvider(videoProvider);
        } else {
            mVideoProvider = null;
        }
        mIsVoipAudioMode = connection.getIsVoipAudioMode();
        mStatusHints = connection.getStatusHints();
        mAddress = connection.getHandle();
        mAddressPresentation = connection.getHandlePresentation();
        mCallerDisplayName = connection.getCallerDisplayName();
        mCallerDisplayNamePresentation = connection.getCallerDisplayNamePresentation();
        mConference = null;
        putExtras(connection.getExtras());
        // Stash the original connection ID as it exists in the source ConnectionService.
        // Telecom will use this to avoid adding duplicates later.
        // See comments on Connection.EXTRA_ORIGINAL_CONNECTION_ID for more information.
        android.os.Bundle newExtras = new android.os.Bundle();
        newExtras.putString(android.telecom.Connection.EXTRA_ORIGINAL_CONNECTION_ID, callId);
        putExtras(newExtras);
    }

    /**
     * Create a RemoteConnection which is used for failed connections. Note that using it for any
     * "real" purpose will almost certainly fail. Callers should note the failure and act
     * accordingly (moving on to another RemoteConnection, for example)
     *
     * @param disconnectCause
     * 		The reason for the failed connection.
     * @unknown 
     */
    RemoteConnection(android.telecom.DisconnectCause disconnectCause) {
        mConnectionId = "NULL";
        mConnected = false;
        mState = android.telecom.Connection.STATE_DISCONNECTED;
        mDisconnectCause = disconnectCause;
    }

    /**
     * Adds a callback to this {@code RemoteConnection}.
     *
     * @param callback
     * 		A {@code Callback}.
     */
    public void registerCallback(android.telecom.RemoteConnection.Callback callback) {
        registerCallback(callback, new android.os.Handler());
    }

    /**
     * Adds a callback to this {@code RemoteConnection}.
     *
     * @param callback
     * 		A {@code Callback}.
     * @param handler
     * 		A {@code Handler} which command and status changes will be delivered to.
     */
    public void registerCallback(android.telecom.RemoteConnection.Callback callback, android.os.Handler handler) {
        unregisterCallback(callback);
        if ((callback != null) && (handler != null)) {
            mCallbackRecords.add(new android.telecom.RemoteConnection.CallbackRecord(callback, handler));
        }
    }

    /**
     * Removes a callback from this {@code RemoteConnection}.
     *
     * @param callback
     * 		A {@code Callback}.
     */
    public void unregisterCallback(android.telecom.RemoteConnection.Callback callback) {
        if (callback != null) {
            for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
                if (record.getCallback() == callback) {
                    mCallbackRecords.remove(record);
                    break;
                }
            }
        }
    }

    /**
     * Obtains the state of this {@code RemoteConnection}.
     *
     * @return A state value, chosen from the {@code STATE_*} constants.
     */
    public int getState() {
        return mState;
    }

    /**
     * Obtains the reason why this {@code RemoteConnection} may have been disconnected.
     *
     * @return For a {@link Connection#STATE_DISCONNECTED} {@code RemoteConnection}, the
    disconnect cause expressed as a code chosen from among those declared in
    {@link DisconnectCause}.
     */
    public android.telecom.DisconnectCause getDisconnectCause() {
        return mDisconnectCause;
    }

    /**
     * Obtains the capabilities of this {@code RemoteConnection}.
     *
     * @return A bitmask of the capabilities of the {@code RemoteConnection}, as defined in
    the {@code CAPABILITY_*} constants in class {@link Connection}.
     */
    public int getConnectionCapabilities() {
        return mConnectionCapabilities;
    }

    /**
     * Obtains the properties of this {@code RemoteConnection}.
     *
     * @return A bitmask of the properties of the {@code RemoteConnection}, as defined in the
    {@code PROPERTY_*} constants in class {@link Connection}.
     */
    public int getConnectionProperties() {
        return mConnectionProperties;
    }

    /**
     * Determines if the audio mode of this {@code RemoteConnection} is VOIP.
     *
     * @return {@code true} if the {@code RemoteConnection}'s current audio mode is VOIP.
     */
    public boolean isVoipAudioMode() {
        return mIsVoipAudioMode;
    }

    /**
     * Obtains status hints pertaining to this {@code RemoteConnection}.
     *
     * @return The current {@link StatusHints} of this {@code RemoteConnection},
    or {@code null} if none have been set.
     */
    public android.telecom.StatusHints getStatusHints() {
        return mStatusHints;
    }

    /**
     * Obtains the address of this {@code RemoteConnection}.
     *
     * @return The address (e.g., phone number) to which the {@code RemoteConnection}
    is currently connected.
     */
    public android.net.Uri getAddress() {
        return mAddress;
    }

    /**
     * Obtains the presentation requirements for the address of this {@code RemoteConnection}.
     *
     * @return The presentation requirements for the address. See
    {@link TelecomManager} for valid values.
     */
    public int getAddressPresentation() {
        return mAddressPresentation;
    }

    /**
     * Obtains the display name for this {@code RemoteConnection}'s caller.
     *
     * @return The display name for the caller.
     */
    public java.lang.CharSequence getCallerDisplayName() {
        return mCallerDisplayName;
    }

    /**
     * Obtains the presentation requirements for this {@code RemoteConnection}'s
     * caller's display name.
     *
     * @return The presentation requirements for the caller display name. See
    {@link TelecomManager} for valid values.
     */
    public int getCallerDisplayNamePresentation() {
        return mCallerDisplayNamePresentation;
    }

    /**
     * Obtains the video state of this {@code RemoteConnection}.
     *
     * @return The video state of the {@code RemoteConnection}. See {@link VideoProfile}.
     */
    public int getVideoState() {
        return mVideoState;
    }

    /**
     * Obtains the video provider of this {@code RemoteConnection}.
     *
     * @return The video provider associated with this {@code RemoteConnection}.
     */
    public final android.telecom.RemoteConnection.VideoProvider getVideoProvider() {
        return mVideoProvider;
    }

    /**
     * Obtain the extras associated with this {@code RemoteConnection}.
     *
     * @return The extras for this connection.
     */
    public final android.os.Bundle getExtras() {
        return mExtras;
    }

    /**
     * Determines whether this {@code RemoteConnection} is requesting ringback.
     *
     * @return Whether the {@code RemoteConnection} is requesting that the framework play a
    ringback tone on its behalf.
     */
    public boolean isRingbackRequested() {
        return mRingbackRequested;
    }

    /**
     * Instructs this {@code RemoteConnection} to abort.
     */
    public void abort() {
        try {
            if (mConnected) {
                mConnectionService.abort(mConnectionId);
            }
        } catch (android.os.RemoteException ignored) {
        }
    }

    /**
     * Instructs this {@link Connection#STATE_RINGING} {@code RemoteConnection} to answer.
     */
    public void answer() {
        try {
            if (mConnected) {
                mConnectionService.answer(mConnectionId);
            }
        } catch (android.os.RemoteException ignored) {
        }
    }

    /**
     * Instructs this {@link Connection#STATE_RINGING} {@code RemoteConnection} to answer.
     *
     * @param videoState
     * 		The video state in which to answer the call.
     * @unknown 
     */
    public void answer(int videoState) {
        try {
            if (mConnected) {
                mConnectionService.answerVideo(mConnectionId, videoState);
            }
        } catch (android.os.RemoteException ignored) {
        }
    }

    /**
     * Instructs this {@link Connection#STATE_RINGING} {@code RemoteConnection} to reject.
     */
    public void reject() {
        try {
            if (mConnected) {
                mConnectionService.reject(mConnectionId);
            }
        } catch (android.os.RemoteException ignored) {
        }
    }

    /**
     * Instructs this {@code RemoteConnection} to go on hold.
     */
    public void hold() {
        try {
            if (mConnected) {
                mConnectionService.hold(mConnectionId);
            }
        } catch (android.os.RemoteException ignored) {
        }
    }

    /**
     * Instructs this {@link Connection#STATE_HOLDING} call to release from hold.
     */
    public void unhold() {
        try {
            if (mConnected) {
                mConnectionService.unhold(mConnectionId);
            }
        } catch (android.os.RemoteException ignored) {
        }
    }

    /**
     * Instructs this {@code RemoteConnection} to disconnect.
     */
    public void disconnect() {
        try {
            if (mConnected) {
                mConnectionService.disconnect(mConnectionId);
            }
        } catch (android.os.RemoteException ignored) {
        }
    }

    /**
     * Instructs this {@code RemoteConnection} to play a dual-tone multi-frequency signaling
     * (DTMF) tone.
     *
     * Any other currently playing DTMF tone in the specified call is immediately stopped.
     *
     * @param digit
     * 		A character representing the DTMF digit for which to play the tone. This
     * 		value must be one of {@code '0'} through {@code '9'}, {@code '*'} or {@code '#'}.
     */
    public void playDtmfTone(char digit) {
        try {
            if (mConnected) {
                mConnectionService.playDtmfTone(mConnectionId, digit);
            }
        } catch (android.os.RemoteException ignored) {
        }
    }

    /**
     * Instructs this {@code RemoteConnection} to stop any dual-tone multi-frequency signaling
     * (DTMF) tone currently playing.
     *
     * DTMF tones are played by calling {@link #playDtmfTone(char)}. If no DTMF tone is
     * currently playing, this method will do nothing.
     */
    public void stopDtmfTone() {
        try {
            if (mConnected) {
                mConnectionService.stopDtmfTone(mConnectionId);
            }
        } catch (android.os.RemoteException ignored) {
        }
    }

    /**
     * Instructs this {@code RemoteConnection} to continue playing a post-dial DTMF string.
     *
     * A post-dial DTMF string is a string of digits following the first instance of either
     * {@link TelecomManager#DTMF_CHARACTER_WAIT} or {@link TelecomManager#DTMF_CHARACTER_PAUSE}.
     * These digits are immediately sent as DTMF tones to the recipient as soon as the
     * connection is made.
     *
     * If the DTMF string contains a {@link TelecomManager#DTMF_CHARACTER_PAUSE} symbol, this
     * {@code RemoteConnection} will temporarily pause playing the tones for a pre-defined period
     * of time.
     *
     * If the DTMF string contains a {@link TelecomManager#DTMF_CHARACTER_WAIT} symbol, this
     * {@code RemoteConnection} will pause playing the tones and notify callbacks via
     * {@link Callback#onPostDialWait(RemoteConnection, String)}. At this point, the in-call app
     * should display to the user an indication of this state and an affordance to continue
     * the postdial sequence. When the user decides to continue the postdial sequence, the in-call
     * app should invoke the {@link #postDialContinue(boolean)} method.
     *
     * @param proceed
     * 		Whether or not to continue with the post-dial sequence.
     */
    public void postDialContinue(boolean proceed) {
        try {
            if (mConnected) {
                mConnectionService.onPostDialContinue(mConnectionId, proceed);
            }
        } catch (android.os.RemoteException ignored) {
        }
    }

    /**
     * Instructs this {@link RemoteConnection} to pull itself to the local device.
     * <p>
     * See {@link Call#pullExternalCall()} for more information.
     */
    public void pullExternalCall() {
        try {
            if (mConnected) {
                mConnectionService.pullExternalCall(mConnectionId);
            }
        } catch (android.os.RemoteException ignored) {
        }
    }

    /**
     * Set the audio state of this {@code RemoteConnection}.
     *
     * @param state
     * 		The audio state of this {@code RemoteConnection}.
     * @unknown 
     * @deprecated Use {@link #setCallAudioState(CallAudioState) instead.
     */
    @android.annotation.SystemApi
    @java.lang.Deprecated
    public void setAudioState(android.telecom.AudioState state) {
        setCallAudioState(new android.telecom.CallAudioState(state));
    }

    /**
     * Set the audio state of this {@code RemoteConnection}.
     *
     * @param state
     * 		The audio state of this {@code RemoteConnection}.
     */
    public void setCallAudioState(android.telecom.CallAudioState state) {
        try {
            if (mConnected) {
                mConnectionService.onCallAudioStateChanged(mConnectionId, state);
            }
        } catch (android.os.RemoteException ignored) {
        }
    }

    /**
     * Obtain the {@code RemoteConnection}s with which this {@code RemoteConnection} may be
     * successfully asked to create a conference with.
     *
     * @return The {@code RemoteConnection}s with which this {@code RemoteConnection} may be
    merged into a {@link RemoteConference}.
     */
    public java.util.List<android.telecom.RemoteConnection> getConferenceableConnections() {
        return mUnmodifiableconferenceableConnections;
    }

    /**
     * Obtain the {@code RemoteConference} that this {@code RemoteConnection} may be a part
     * of, or {@code null} if there is no such {@code RemoteConference}.
     *
     * @return A {@code RemoteConference} or {@code null};
     */
    public android.telecom.RemoteConference getConference() {
        return mConference;
    }

    /**
     * {@hide }
     */
    java.lang.String getId() {
        return mConnectionId;
    }

    /**
     * {@hide }
     */
    com.android.internal.telecom.IConnectionService getConnectionService() {
        return mConnectionService;
    }

    /**
     *
     *
     * @unknown 
     */
    void setState(final int state) {
        if (mState != state) {
            mState = state;
            for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
                final android.telecom.RemoteConnection connection = this;
                final android.telecom.RemoteConnection.Callback callback = record.getCallback();
                record.getHandler().post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        callback.onStateChanged(connection, state);
                    }
                });
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void setDisconnected(final android.telecom.DisconnectCause disconnectCause) {
        if (mState != android.telecom.Connection.STATE_DISCONNECTED) {
            mState = android.telecom.Connection.STATE_DISCONNECTED;
            mDisconnectCause = disconnectCause;
            for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
                final android.telecom.RemoteConnection connection = this;
                final android.telecom.RemoteConnection.Callback callback = record.getCallback();
                record.getHandler().post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        callback.onDisconnected(connection, disconnectCause);
                    }
                });
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void setRingbackRequested(final boolean ringback) {
        if (mRingbackRequested != ringback) {
            mRingbackRequested = ringback;
            for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
                final android.telecom.RemoteConnection connection = this;
                final android.telecom.RemoteConnection.Callback callback = record.getCallback();
                record.getHandler().post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        callback.onRingbackRequested(connection, ringback);
                    }
                });
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void setConnectionCapabilities(final int connectionCapabilities) {
        mConnectionCapabilities = connectionCapabilities;
        for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
            final android.telecom.RemoteConnection connection = this;
            final android.telecom.RemoteConnection.Callback callback = record.getCallback();
            record.getHandler().post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    callback.onConnectionCapabilitiesChanged(connection, connectionCapabilities);
                }
            });
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void setConnectionProperties(final int connectionProperties) {
        mConnectionProperties = connectionProperties;
        for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
            final android.telecom.RemoteConnection connection = this;
            final android.telecom.RemoteConnection.Callback callback = record.getCallback();
            record.getHandler().post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    callback.onConnectionPropertiesChanged(connection, connectionProperties);
                }
            });
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void setDestroyed() {
        if (!mCallbackRecords.isEmpty()) {
            // Make sure that the callbacks are notified that the call is destroyed first.
            if (mState != android.telecom.Connection.STATE_DISCONNECTED) {
                setDisconnected(new android.telecom.DisconnectCause(android.telecom.DisconnectCause.ERROR, "Connection destroyed."));
            }
            for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
                final android.telecom.RemoteConnection connection = this;
                final android.telecom.RemoteConnection.Callback callback = record.getCallback();
                record.getHandler().post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        callback.onDestroyed(connection);
                    }
                });
            }
            mCallbackRecords.clear();
            mConnected = false;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void setPostDialWait(final java.lang.String remainingDigits) {
        for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
            final android.telecom.RemoteConnection connection = this;
            final android.telecom.RemoteConnection.Callback callback = record.getCallback();
            record.getHandler().post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    callback.onPostDialWait(connection, remainingDigits);
                }
            });
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void onPostDialChar(final char nextChar) {
        for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
            final android.telecom.RemoteConnection connection = this;
            final android.telecom.RemoteConnection.Callback callback = record.getCallback();
            record.getHandler().post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    callback.onPostDialChar(connection, nextChar);
                }
            });
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void setVideoState(final int videoState) {
        mVideoState = videoState;
        for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
            final android.telecom.RemoteConnection connection = this;
            final android.telecom.RemoteConnection.Callback callback = record.getCallback();
            record.getHandler().post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    callback.onVideoStateChanged(connection, videoState);
                }
            });
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void setVideoProvider(final android.telecom.RemoteConnection.VideoProvider videoProvider) {
        mVideoProvider = videoProvider;
        for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
            final android.telecom.RemoteConnection connection = this;
            final android.telecom.RemoteConnection.Callback callback = record.getCallback();
            record.getHandler().post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    callback.onVideoProviderChanged(connection, videoProvider);
                }
            });
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void setIsVoipAudioMode(final boolean isVoip) {
        mIsVoipAudioMode = isVoip;
        for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
            final android.telecom.RemoteConnection connection = this;
            final android.telecom.RemoteConnection.Callback callback = record.getCallback();
            record.getHandler().post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    callback.onVoipAudioChanged(connection, isVoip);
                }
            });
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void setStatusHints(final android.telecom.StatusHints statusHints) {
        mStatusHints = statusHints;
        for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
            final android.telecom.RemoteConnection connection = this;
            final android.telecom.RemoteConnection.Callback callback = record.getCallback();
            record.getHandler().post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    callback.onStatusHintsChanged(connection, statusHints);
                }
            });
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void setAddress(final android.net.Uri address, final int presentation) {
        mAddress = address;
        mAddressPresentation = presentation;
        for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
            final android.telecom.RemoteConnection connection = this;
            final android.telecom.RemoteConnection.Callback callback = record.getCallback();
            record.getHandler().post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    callback.onAddressChanged(connection, address, presentation);
                }
            });
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void setCallerDisplayName(final java.lang.String callerDisplayName, final int presentation) {
        mCallerDisplayName = callerDisplayName;
        mCallerDisplayNamePresentation = presentation;
        for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
            final android.telecom.RemoteConnection connection = this;
            final android.telecom.RemoteConnection.Callback callback = record.getCallback();
            record.getHandler().post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    callback.onCallerDisplayNameChanged(connection, callerDisplayName, presentation);
                }
            });
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void setConferenceableConnections(final java.util.List<android.telecom.RemoteConnection> conferenceableConnections) {
        mConferenceableConnections.clear();
        mConferenceableConnections.addAll(conferenceableConnections);
        for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
            final android.telecom.RemoteConnection connection = this;
            final android.telecom.RemoteConnection.Callback callback = record.getCallback();
            record.getHandler().post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    callback.onConferenceableConnectionsChanged(connection, mUnmodifiableconferenceableConnections);
                }
            });
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void setConference(final android.telecom.RemoteConference conference) {
        if (mConference != conference) {
            mConference = conference;
            for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
                final android.telecom.RemoteConnection connection = this;
                final android.telecom.RemoteConnection.Callback callback = record.getCallback();
                record.getHandler().post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        callback.onConferenceChanged(connection, conference);
                    }
                });
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void putExtras(final android.os.Bundle extras) {
        if (extras == null) {
            return;
        }
        if (mExtras == null) {
            mExtras = new android.os.Bundle();
        }
        mExtras.putAll(extras);
        notifyExtrasChanged();
    }

    /**
     *
     *
     * @unknown 
     */
    void removeExtras(java.util.List<java.lang.String> keys) {
        if (((mExtras == null) || (keys == null)) || keys.isEmpty()) {
            return;
        }
        for (java.lang.String key : keys) {
            mExtras.remove(key);
        }
        notifyExtrasChanged();
    }

    private void notifyExtrasChanged() {
        for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
            final android.telecom.RemoteConnection connection = this;
            final android.telecom.RemoteConnection.Callback callback = record.getCallback();
            record.getHandler().post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    callback.onExtrasChanged(connection, mExtras);
                }
            });
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void onConnectionEvent(final java.lang.String event, final android.os.Bundle extras) {
        for (android.telecom.RemoteConnection.CallbackRecord record : mCallbackRecords) {
            final android.telecom.RemoteConnection connection = this;
            final android.telecom.RemoteConnection.Callback callback = record.getCallback();
            record.getHandler().post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    callback.onConnectionEvent(connection, event, extras);
                }
            });
        }
    }

    /**
     * Create a RemoteConnection represents a failure, and which will be in
     * {@link Connection#STATE_DISCONNECTED}. Attempting to use it for anything will almost
     * certainly result in bad things happening. Do not do this.
     *
     * @return a failed {@link RemoteConnection}
     * @unknown 
     */
    public static android.telecom.RemoteConnection failure(android.telecom.DisconnectCause disconnectCause) {
        return new android.telecom.RemoteConnection(disconnectCause);
    }

    private static final class CallbackRecord extends android.telecom.RemoteConnection.Callback {
        private final android.telecom.RemoteConnection.Callback mCallback;

        private final android.os.Handler mHandler;

        public CallbackRecord(android.telecom.RemoteConnection.Callback callback, android.os.Handler handler) {
            mCallback = callback;
            mHandler = handler;
        }

        public android.telecom.RemoteConnection.Callback getCallback() {
            return mCallback;
        }

        public android.os.Handler getHandler() {
            return mHandler;
        }
    }
}

