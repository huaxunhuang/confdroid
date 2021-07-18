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
 * Remote connection service which other connection services can use to place calls on their behalf.
 *
 * @unknown 
 */
final class RemoteConnectionService {
    // Note: Casting null to avoid ambiguous constructor reference.
    private static final android.telecom.RemoteConnection NULL_CONNECTION = new android.telecom.RemoteConnection("NULL", null, ((android.telecom.ConnectionRequest) (null)));

    private static final android.telecom.RemoteConference NULL_CONFERENCE = new android.telecom.RemoteConference("NULL", null);

    private final com.android.internal.telecom.IConnectionServiceAdapter mServantDelegate = new com.android.internal.telecom.IConnectionServiceAdapter() {
        @java.lang.Override
        public void handleCreateConnectionComplete(java.lang.String id, android.telecom.ConnectionRequest request, android.telecom.ParcelableConnection parcel) {
            android.telecom.RemoteConnection connection = findConnectionForAction(id, "handleCreateConnectionSuccessful");
            if ((connection != android.telecom.RemoteConnectionService.NULL_CONNECTION) && mPendingConnections.contains(connection)) {
                mPendingConnections.remove(connection);
                // Unconditionally initialize the connection ...
                connection.setConnectionCapabilities(parcel.getConnectionCapabilities());
                connection.setConnectionProperties(parcel.getConnectionProperties());
                if ((parcel.getHandle() != null) || (parcel.getState() != android.telecom.Connection.STATE_DISCONNECTED)) {
                    connection.setAddress(parcel.getHandle(), parcel.getHandlePresentation());
                }
                if ((parcel.getCallerDisplayName() != null) || (parcel.getState() != android.telecom.Connection.STATE_DISCONNECTED)) {
                    connection.setCallerDisplayName(parcel.getCallerDisplayName(), parcel.getCallerDisplayNamePresentation());
                }
                // Set state after handle so that the client can identify the connection.
                if (parcel.getState() == android.telecom.Connection.STATE_DISCONNECTED) {
                    connection.setDisconnected(parcel.getDisconnectCause());
                } else {
                    connection.setState(parcel.getState());
                }
                java.util.List<android.telecom.RemoteConnection> conferenceable = new java.util.ArrayList<>();
                for (java.lang.String confId : parcel.getConferenceableConnectionIds()) {
                    if (mConnectionById.containsKey(confId)) {
                        conferenceable.add(mConnectionById.get(confId));
                    }
                }
                connection.setConferenceableConnections(conferenceable);
                connection.setVideoState(parcel.getVideoState());
                if (connection.getState() == android.telecom.Connection.STATE_DISCONNECTED) {
                    // ... then, if it was created in a disconnected state, that indicates
                    // failure on the providing end, so immediately mark it destroyed
                    connection.setDestroyed();
                }
            }
        }

        @java.lang.Override
        public void setActive(java.lang.String callId) {
            if (mConnectionById.containsKey(callId)) {
                findConnectionForAction(callId, "setActive").setState(android.telecom.Connection.STATE_ACTIVE);
            } else {
                findConferenceForAction(callId, "setActive").setState(android.telecom.Connection.STATE_ACTIVE);
            }
        }

        @java.lang.Override
        public void setRinging(java.lang.String callId) {
            findConnectionForAction(callId, "setRinging").setState(android.telecom.Connection.STATE_RINGING);
        }

        @java.lang.Override
        public void setDialing(java.lang.String callId) {
            findConnectionForAction(callId, "setDialing").setState(android.telecom.Connection.STATE_DIALING);
        }

        @java.lang.Override
        public void setPulling(java.lang.String callId) {
            findConnectionForAction(callId, "setPulling").setState(android.telecom.Connection.STATE_PULLING_CALL);
        }

        @java.lang.Override
        public void setDisconnected(java.lang.String callId, android.telecom.DisconnectCause disconnectCause) {
            if (mConnectionById.containsKey(callId)) {
                findConnectionForAction(callId, "setDisconnected").setDisconnected(disconnectCause);
            } else {
                findConferenceForAction(callId, "setDisconnected").setDisconnected(disconnectCause);
            }
        }

        @java.lang.Override
        public void setOnHold(java.lang.String callId) {
            if (mConnectionById.containsKey(callId)) {
                findConnectionForAction(callId, "setOnHold").setState(android.telecom.Connection.STATE_HOLDING);
            } else {
                findConferenceForAction(callId, "setOnHold").setState(android.telecom.Connection.STATE_HOLDING);
            }
        }

        @java.lang.Override
        public void setRingbackRequested(java.lang.String callId, boolean ringing) {
            findConnectionForAction(callId, "setRingbackRequested").setRingbackRequested(ringing);
        }

        @java.lang.Override
        public void setConnectionCapabilities(java.lang.String callId, int connectionCapabilities) {
            if (mConnectionById.containsKey(callId)) {
                findConnectionForAction(callId, "setConnectionCapabilities").setConnectionCapabilities(connectionCapabilities);
            } else {
                findConferenceForAction(callId, "setConnectionCapabilities").setConnectionCapabilities(connectionCapabilities);
            }
        }

        @java.lang.Override
        public void setConnectionProperties(java.lang.String callId, int connectionProperties) {
            if (mConnectionById.containsKey(callId)) {
                findConnectionForAction(callId, "setConnectionProperties").setConnectionProperties(connectionProperties);
            } else {
                findConferenceForAction(callId, "setConnectionProperties").setConnectionProperties(connectionProperties);
            }
        }

        @java.lang.Override
        public void setIsConferenced(java.lang.String callId, java.lang.String conferenceCallId) {
            // Note: callId should not be null; conferenceCallId may be null
            android.telecom.RemoteConnection connection = findConnectionForAction(callId, "setIsConferenced");
            if (connection != android.telecom.RemoteConnectionService.NULL_CONNECTION) {
                if (conferenceCallId == null) {
                    // 'connection' is being split from its conference
                    if (connection.getConference() != null) {
                        connection.getConference().removeConnection(connection);
                    }
                } else {
                    android.telecom.RemoteConference conference = findConferenceForAction(conferenceCallId, "setIsConferenced");
                    if (conference != android.telecom.RemoteConnectionService.NULL_CONFERENCE) {
                        conference.addConnection(connection);
                    }
                }
            }
        }

        @java.lang.Override
        public void setConferenceMergeFailed(java.lang.String callId) {
            // Nothing to do here.
            // The event has already been handled and there is no state to update
            // in the underlying connection or conference objects
        }

        @java.lang.Override
        public void addConferenceCall(final java.lang.String callId, android.telecom.ParcelableConference parcel) {
            android.telecom.RemoteConference conference = new android.telecom.RemoteConference(callId, mOutgoingConnectionServiceRpc);
            for (java.lang.String id : parcel.getConnectionIds()) {
                android.telecom.RemoteConnection c = mConnectionById.get(id);
                if (c != null) {
                    conference.addConnection(c);
                }
            }
            if (conference.getConnections().size() == 0) {
                // A conference was created, but none of its connections are ones that have been
                // created by, and therefore being tracked by, this remote connection service. It
                // is of no interest to us.
                android.telecom.Log.d(this, "addConferenceCall - skipping");
                return;
            }
            conference.setState(parcel.getState());
            conference.setConnectionCapabilities(parcel.getConnectionCapabilities());
            conference.setConnectionProperties(parcel.getConnectionProperties());
            conference.putExtras(parcel.getExtras());
            mConferenceById.put(callId, conference);
            // Stash the original connection ID as it exists in the source ConnectionService.
            // Telecom will use this to avoid adding duplicates later.
            // See comments on Connection.EXTRA_ORIGINAL_CONNECTION_ID for more information.
            android.os.Bundle newExtras = new android.os.Bundle();
            newExtras.putString(android.telecom.Connection.EXTRA_ORIGINAL_CONNECTION_ID, callId);
            conference.putExtras(newExtras);
            conference.registerCallback(new android.telecom.RemoteConference.Callback() {
                @java.lang.Override
                public void onDestroyed(android.telecom.RemoteConference c) {
                    mConferenceById.remove(callId);
                    maybeDisconnectAdapter();
                }
            });
            mOurConnectionServiceImpl.addRemoteConference(conference);
        }

        @java.lang.Override
        public void removeCall(java.lang.String callId) {
            if (mConnectionById.containsKey(callId)) {
                findConnectionForAction(callId, "removeCall").setDestroyed();
            } else {
                findConferenceForAction(callId, "removeCall").setDestroyed();
            }
        }

        @java.lang.Override
        public void onPostDialWait(java.lang.String callId, java.lang.String remaining) {
            findConnectionForAction(callId, "onPostDialWait").setPostDialWait(remaining);
        }

        @java.lang.Override
        public void onPostDialChar(java.lang.String callId, char nextChar) {
            findConnectionForAction(callId, "onPostDialChar").onPostDialChar(nextChar);
        }

        @java.lang.Override
        public void queryRemoteConnectionServices(com.android.internal.telecom.RemoteServiceCallback callback) {
            // Not supported from remote connection service.
        }

        @java.lang.Override
        public void setVideoProvider(java.lang.String callId, com.android.internal.telecom.IVideoProvider videoProvider) {
            android.telecom.RemoteConnection.VideoProvider remoteVideoProvider = null;
            if (videoProvider != null) {
                remoteVideoProvider = new android.telecom.RemoteConnection.VideoProvider(videoProvider);
            }
            findConnectionForAction(callId, "setVideoProvider").setVideoProvider(remoteVideoProvider);
        }

        @java.lang.Override
        public void setVideoState(java.lang.String callId, int videoState) {
            findConnectionForAction(callId, "setVideoState").setVideoState(videoState);
        }

        @java.lang.Override
        public void setIsVoipAudioMode(java.lang.String callId, boolean isVoip) {
            findConnectionForAction(callId, "setIsVoipAudioMode").setIsVoipAudioMode(isVoip);
        }

        @java.lang.Override
        public void setStatusHints(java.lang.String callId, android.telecom.StatusHints statusHints) {
            findConnectionForAction(callId, "setStatusHints").setStatusHints(statusHints);
        }

        @java.lang.Override
        public void setAddress(java.lang.String callId, android.net.Uri address, int presentation) {
            findConnectionForAction(callId, "setAddress").setAddress(address, presentation);
        }

        @java.lang.Override
        public void setCallerDisplayName(java.lang.String callId, java.lang.String callerDisplayName, int presentation) {
            findConnectionForAction(callId, "setCallerDisplayName").setCallerDisplayName(callerDisplayName, presentation);
        }

        @java.lang.Override
        public android.os.IBinder asBinder() {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public final void setConferenceableConnections(java.lang.String callId, java.util.List<java.lang.String> conferenceableConnectionIds) {
            java.util.List<android.telecom.RemoteConnection> conferenceable = new java.util.ArrayList<>();
            for (java.lang.String id : conferenceableConnectionIds) {
                if (mConnectionById.containsKey(id)) {
                    conferenceable.add(mConnectionById.get(id));
                }
            }
            if (hasConnection(callId)) {
                findConnectionForAction(callId, "setConferenceableConnections").setConferenceableConnections(conferenceable);
            } else {
                findConferenceForAction(callId, "setConferenceableConnections").setConferenceableConnections(conferenceable);
            }
        }

        @java.lang.Override
        public void addExistingConnection(final java.lang.String callId, android.telecom.ParcelableConnection connection) {
            android.telecom.RemoteConnection remoteConnection = new android.telecom.RemoteConnection(callId, mOutgoingConnectionServiceRpc, connection);
            mConnectionById.put(callId, remoteConnection);
            remoteConnection.registerCallback(new android.telecom.RemoteConnection.Callback() {
                @java.lang.Override
                public void onDestroyed(android.telecom.RemoteConnection connection) {
                    mConnectionById.remove(callId);
                    maybeDisconnectAdapter();
                }
            });
            mOurConnectionServiceImpl.addRemoteExistingConnection(remoteConnection);
        }

        @java.lang.Override
        public void putExtras(java.lang.String callId, android.os.Bundle extras) {
            if (hasConnection(callId)) {
                findConnectionForAction(callId, "putExtras").putExtras(extras);
            } else {
                findConferenceForAction(callId, "putExtras").putExtras(extras);
            }
        }

        @java.lang.Override
        public void removeExtras(java.lang.String callId, java.util.List<java.lang.String> keys) {
            if (hasConnection(callId)) {
                findConnectionForAction(callId, "removeExtra").removeExtras(keys);
            } else {
                findConferenceForAction(callId, "removeExtra").removeExtras(keys);
            }
        }

        @java.lang.Override
        public void onConnectionEvent(java.lang.String callId, java.lang.String event, android.os.Bundle extras) {
            if (mConnectionById.containsKey(callId)) {
                findConnectionForAction(callId, "onConnectionEvent").onConnectionEvent(event, extras);
            }
        }
    };

    private final android.telecom.ConnectionServiceAdapterServant mServant = new android.telecom.ConnectionServiceAdapterServant(mServantDelegate);

    private final android.os.IBinder.DeathRecipient mDeathRecipient = new android.os.IBinder.DeathRecipient() {
        @java.lang.Override
        public void binderDied() {
            for (android.telecom.RemoteConnection c : mConnectionById.values()) {
                c.setDestroyed();
            }
            for (android.telecom.RemoteConference c : mConferenceById.values()) {
                c.setDestroyed();
            }
            mConnectionById.clear();
            mConferenceById.clear();
            mPendingConnections.clear();
            mOutgoingConnectionServiceRpc.asBinder().unlinkToDeath(mDeathRecipient, 0);
        }
    };

    private final com.android.internal.telecom.IConnectionService mOutgoingConnectionServiceRpc;

    private final android.telecom.ConnectionService mOurConnectionServiceImpl;

    private final java.util.Map<java.lang.String, android.telecom.RemoteConnection> mConnectionById = new java.util.HashMap<>();

    private final java.util.Map<java.lang.String, android.telecom.RemoteConference> mConferenceById = new java.util.HashMap<>();

    private final java.util.Set<android.telecom.RemoteConnection> mPendingConnections = new java.util.HashSet<>();

    RemoteConnectionService(com.android.internal.telecom.IConnectionService outgoingConnectionServiceRpc, android.telecom.ConnectionService ourConnectionServiceImpl) throws android.os.RemoteException {
        mOutgoingConnectionServiceRpc = outgoingConnectionServiceRpc;
        mOutgoingConnectionServiceRpc.asBinder().linkToDeath(mDeathRecipient, 0);
        mOurConnectionServiceImpl = ourConnectionServiceImpl;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ("[RemoteCS - " + toString()) + "]";
    }

    final android.telecom.RemoteConnection createRemoteConnection(android.telecom.PhoneAccountHandle connectionManagerPhoneAccount, android.telecom.ConnectionRequest request, boolean isIncoming) {
        final java.lang.String id = java.util.UUID.randomUUID().toString();
        final android.telecom.ConnectionRequest newRequest = new android.telecom.ConnectionRequest(request.getAccountHandle(), request.getAddress(), request.getExtras(), request.getVideoState());
        try {
            if (mConnectionById.isEmpty()) {
                mOutgoingConnectionServiceRpc.addConnectionServiceAdapter(mServant.getStub());
            }
            android.telecom.RemoteConnection connection = new android.telecom.RemoteConnection(id, mOutgoingConnectionServiceRpc, newRequest);
            mPendingConnections.add(connection);
            mConnectionById.put(id, connection);
            /* isUnknownCall */
            mOutgoingConnectionServiceRpc.createConnection(connectionManagerPhoneAccount, id, newRequest, isIncoming, false);
            connection.registerCallback(new android.telecom.RemoteConnection.Callback() {
                @java.lang.Override
                public void onDestroyed(android.telecom.RemoteConnection connection) {
                    mConnectionById.remove(id);
                    maybeDisconnectAdapter();
                }
            });
            return connection;
        } catch (android.os.RemoteException e) {
            return android.telecom.RemoteConnection.failure(new android.telecom.DisconnectCause(android.telecom.DisconnectCause.ERROR, e.toString()));
        }
    }

    private boolean hasConnection(java.lang.String callId) {
        return mConnectionById.containsKey(callId);
    }

    private android.telecom.RemoteConnection findConnectionForAction(java.lang.String callId, java.lang.String action) {
        if (mConnectionById.containsKey(callId)) {
            return mConnectionById.get(callId);
        }
        android.telecom.Log.w(this, "%s - Cannot find Connection %s", action, callId);
        return android.telecom.RemoteConnectionService.NULL_CONNECTION;
    }

    private android.telecom.RemoteConference findConferenceForAction(java.lang.String callId, java.lang.String action) {
        if (mConferenceById.containsKey(callId)) {
            return mConferenceById.get(callId);
        }
        android.telecom.Log.w(this, "%s - Cannot find Conference %s", action, callId);
        return android.telecom.RemoteConnectionService.NULL_CONFERENCE;
    }

    private void maybeDisconnectAdapter() {
        if (mConnectionById.isEmpty() && mConferenceById.isEmpty()) {
            try {
                mOutgoingConnectionServiceRpc.removeConnectionServiceAdapter(mServant.getStub());
            } catch (android.os.RemoteException e) {
            }
        }
    }
}

