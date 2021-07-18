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
 * A conference provided to a {@link ConnectionService} by another {@code ConnectionService} through
 * {@link ConnectionService#conferenceRemoteConnections}. Once created, a {@code RemoteConference}
 * can be used to control the conference call or monitor changes through
 * {@link RemoteConnection.Callback}.
 *
 * @see ConnectionService#onRemoteConferenceAdded
 */
public final class RemoteConference {
    /**
     * Callback base class for {@link RemoteConference}.
     */
    public static abstract class Callback {
        /**
         * Invoked when the state of this {@code RemoteConferece} has changed. See
         * {@link #getState()}.
         *
         * @param conference
         * 		The {@code RemoteConference} invoking this method.
         * @param oldState
         * 		The previous state of the {@code RemoteConference}.
         * @param newState
         * 		The new state of the {@code RemoteConference}.
         */
        public void onStateChanged(android.telecom.RemoteConference conference, int oldState, int newState) {
        }

        /**
         * Invoked when this {@code RemoteConference} is disconnected.
         *
         * @param conference
         * 		The {@code RemoteConference} invoking this method.
         * @param disconnectCause
         * 		The ({@see DisconnectCause}) associated with this failed
         * 		conference.
         */
        public void onDisconnected(android.telecom.RemoteConference conference, android.telecom.DisconnectCause disconnectCause) {
        }

        /**
         * Invoked when a {@link RemoteConnection} is added to the conference call.
         *
         * @param conference
         * 		The {@code RemoteConference} invoking this method.
         * @param connection
         * 		The {@link RemoteConnection} being added.
         */
        public void onConnectionAdded(android.telecom.RemoteConference conference, android.telecom.RemoteConnection connection) {
        }

        /**
         * Invoked when a {@link RemoteConnection} is removed from the conference call.
         *
         * @param conference
         * 		The {@code RemoteConference} invoking this method.
         * @param connection
         * 		The {@link RemoteConnection} being removed.
         */
        public void onConnectionRemoved(android.telecom.RemoteConference conference, android.telecom.RemoteConnection connection) {
        }

        /**
         * Indicates that the call capabilities of this {@code RemoteConference} have changed.
         * See {@link #getConnectionCapabilities()}.
         *
         * @param conference
         * 		The {@code RemoteConference} invoking this method.
         * @param connectionCapabilities
         * 		The new capabilities of the {@code RemoteConference}.
         */
        public void onConnectionCapabilitiesChanged(android.telecom.RemoteConference conference, int connectionCapabilities) {
        }

        /**
         * Indicates that the call properties of this {@code RemoteConference} have changed.
         * See {@link #getConnectionProperties()}.
         *
         * @param conference
         * 		The {@code RemoteConference} invoking this method.
         * @param connectionProperties
         * 		The new properties of the {@code RemoteConference}.
         */
        public void onConnectionPropertiesChanged(android.telecom.RemoteConference conference, int connectionProperties) {
        }

        /**
         * Invoked when the set of {@link RemoteConnection}s which can be added to this conference
         * call have changed.
         *
         * @param conference
         * 		The {@code RemoteConference} invoking this method.
         * @param conferenceableConnections
         * 		The list of conferenceable {@link RemoteConnection}s.
         */
        public void onConferenceableConnectionsChanged(android.telecom.RemoteConference conference, java.util.List<android.telecom.RemoteConnection> conferenceableConnections) {
        }

        /**
         * Indicates that this {@code RemoteConference} has been destroyed. No further requests
         * should be made to the {@code RemoteConference}, and references to it should be cleared.
         *
         * @param conference
         * 		The {@code RemoteConference} invoking this method.
         */
        public void onDestroyed(android.telecom.RemoteConference conference) {
        }

        /**
         * Handles changes to the {@code RemoteConference} extras.
         *
         * @param conference
         * 		The {@code RemoteConference} invoking this method.
         * @param extras
         * 		The extras containing other information associated with the conference.
         */
        public void onExtrasChanged(android.telecom.RemoteConference conference, @android.annotation.Nullable
        android.os.Bundle extras) {
        }
    }

    private final java.lang.String mId;

    private final com.android.internal.telecom.IConnectionService mConnectionService;

    private final java.util.Set<android.telecom.CallbackRecord<android.telecom.RemoteConference.Callback>> mCallbackRecords = new java.util.concurrent.CopyOnWriteArraySet<>();

    private final java.util.List<android.telecom.RemoteConnection> mChildConnections = new java.util.concurrent.CopyOnWriteArrayList<>();

    private final java.util.List<android.telecom.RemoteConnection> mUnmodifiableChildConnections = java.util.Collections.unmodifiableList(mChildConnections);

    private final java.util.List<android.telecom.RemoteConnection> mConferenceableConnections = new java.util.ArrayList<>();

    private final java.util.List<android.telecom.RemoteConnection> mUnmodifiableConferenceableConnections = java.util.Collections.unmodifiableList(mConferenceableConnections);

    private int mState = android.telecom.Connection.STATE_NEW;

    private android.telecom.DisconnectCause mDisconnectCause;

    private int mConnectionCapabilities;

    private int mConnectionProperties;

    private android.os.Bundle mExtras;

    /**
     *
     *
     * @unknown 
     */
    RemoteConference(java.lang.String id, com.android.internal.telecom.IConnectionService connectionService) {
        mId = id;
        mConnectionService = connectionService;
    }

    /**
     *
     *
     * @unknown 
     */
    java.lang.String getId() {
        return mId;
    }

    /**
     *
     *
     * @unknown 
     */
    void setDestroyed() {
        for (android.telecom.RemoteConnection connection : mChildConnections) {
            connection.setConference(null);
        }
        for (android.telecom.CallbackRecord<android.telecom.RemoteConference.Callback> record : mCallbackRecords) {
            final android.telecom.RemoteConference conference = this;
            final android.telecom.RemoteConference.Callback callback = record.getCallback();
            record.getHandler().post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    callback.onDestroyed(conference);
                }
            });
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void setState(final int newState) {
        if (((newState != android.telecom.Connection.STATE_ACTIVE) && (newState != android.telecom.Connection.STATE_HOLDING)) && (newState != android.telecom.Connection.STATE_DISCONNECTED)) {
            android.telecom.Log.w(this, "Unsupported state transition for Conference call.", android.telecom.Connection.stateToString(newState));
            return;
        }
        if (mState != newState) {
            final int oldState = mState;
            mState = newState;
            for (android.telecom.CallbackRecord<android.telecom.RemoteConference.Callback> record : mCallbackRecords) {
                final android.telecom.RemoteConference conference = this;
                final android.telecom.RemoteConference.Callback callback = record.getCallback();
                record.getHandler().post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        callback.onStateChanged(conference, oldState, newState);
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
    void addConnection(final android.telecom.RemoteConnection connection) {
        if (!mChildConnections.contains(connection)) {
            mChildConnections.add(connection);
            connection.setConference(this);
            for (android.telecom.CallbackRecord<android.telecom.RemoteConference.Callback> record : mCallbackRecords) {
                final android.telecom.RemoteConference conference = this;
                final android.telecom.RemoteConference.Callback callback = record.getCallback();
                record.getHandler().post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        callback.onConnectionAdded(conference, connection);
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
    void removeConnection(final android.telecom.RemoteConnection connection) {
        if (mChildConnections.contains(connection)) {
            mChildConnections.remove(connection);
            connection.setConference(null);
            for (android.telecom.CallbackRecord<android.telecom.RemoteConference.Callback> record : mCallbackRecords) {
                final android.telecom.RemoteConference conference = this;
                final android.telecom.RemoteConference.Callback callback = record.getCallback();
                record.getHandler().post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        callback.onConnectionRemoved(conference, connection);
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
        if (mConnectionCapabilities != connectionCapabilities) {
            mConnectionCapabilities = connectionCapabilities;
            for (android.telecom.CallbackRecord<android.telecom.RemoteConference.Callback> record : mCallbackRecords) {
                final android.telecom.RemoteConference conference = this;
                final android.telecom.RemoteConference.Callback callback = record.getCallback();
                record.getHandler().post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        callback.onConnectionCapabilitiesChanged(conference, mConnectionCapabilities);
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
    void setConnectionProperties(final int connectionProperties) {
        if (mConnectionProperties != connectionProperties) {
            mConnectionProperties = connectionProperties;
            for (android.telecom.CallbackRecord<android.telecom.RemoteConference.Callback> record : mCallbackRecords) {
                final android.telecom.RemoteConference conference = this;
                final android.telecom.RemoteConference.Callback callback = record.getCallback();
                record.getHandler().post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        callback.onConnectionPropertiesChanged(conference, mConnectionProperties);
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
    void setConferenceableConnections(java.util.List<android.telecom.RemoteConnection> conferenceableConnections) {
        mConferenceableConnections.clear();
        mConferenceableConnections.addAll(conferenceableConnections);
        for (android.telecom.CallbackRecord<android.telecom.RemoteConference.Callback> record : mCallbackRecords) {
            final android.telecom.RemoteConference conference = this;
            final android.telecom.RemoteConference.Callback callback = record.getCallback();
            record.getHandler().post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    callback.onConferenceableConnectionsChanged(conference, mUnmodifiableConferenceableConnections);
                }
            });
        }
    }

    /**
     *
     *
     * @unknown 
     */
    void setDisconnected(final android.telecom.DisconnectCause disconnectCause) {
        if (mState != android.telecom.Connection.STATE_DISCONNECTED) {
            mDisconnectCause = disconnectCause;
            setState(android.telecom.Connection.STATE_DISCONNECTED);
            for (android.telecom.CallbackRecord<android.telecom.RemoteConference.Callback> record : mCallbackRecords) {
                final android.telecom.RemoteConference conference = this;
                final android.telecom.RemoteConference.Callback callback = record.getCallback();
                record.getHandler().post(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        callback.onDisconnected(conference, disconnectCause);
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
        for (android.telecom.CallbackRecord<android.telecom.RemoteConference.Callback> record : mCallbackRecords) {
            final android.telecom.RemoteConference conference = this;
            final android.telecom.RemoteConference.Callback callback = record.getCallback();
            record.getHandler().post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    callback.onExtrasChanged(conference, mExtras);
                }
            });
        }
    }

    /**
     * Returns the list of {@link RemoteConnection}s contained in this conference.
     *
     * @return A list of child connections.
     */
    public final java.util.List<android.telecom.RemoteConnection> getConnections() {
        return mUnmodifiableChildConnections;
    }

    /**
     * Gets the state of the conference call. See {@link Connection} for valid values.
     *
     * @return A constant representing the state the conference call is currently in.
     */
    public final int getState() {
        return mState;
    }

    /**
     * Returns the capabilities of the conference. See {@code CAPABILITY_*} constants in class
     * {@link Connection} for valid values.
     *
     * @return A bitmask of the capabilities of the conference call.
     */
    public final int getConnectionCapabilities() {
        return mConnectionCapabilities;
    }

    /**
     * Returns the properties of the conference. See {@code PROPERTY_*} constants in class
     * {@link Connection} for valid values.
     *
     * @return A bitmask of the properties of the conference call.
     */
    public final int getConnectionProperties() {
        return mConnectionProperties;
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
     * Disconnects the conference call as well as the child {@link RemoteConnection}s.
     */
    public void disconnect() {
        try {
            mConnectionService.disconnect(mId);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Removes the specified {@link RemoteConnection} from the conference. This causes the
     * {@link RemoteConnection} to become a standalone connection. This is a no-op if the
     * {@link RemoteConnection} does not belong to this conference.
     *
     * @param connection
     * 		The remote-connection to remove.
     */
    public void separate(android.telecom.RemoteConnection connection) {
        if (mChildConnections.contains(connection)) {
            try {
                mConnectionService.splitFromConference(connection.getId());
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /**
     * Merges all {@link RemoteConnection}s of this conference into a single call. This should be
     * invoked only if the conference contains the capability
     * {@link Connection#CAPABILITY_MERGE_CONFERENCE}, otherwise it is a no-op. The presence of said
     * capability indicates that the connections of this conference, despite being part of the
     * same conference object, are yet to have their audio streams merged; this is a common pattern
     * for CDMA conference calls, but the capability is not used for GSM and SIP conference calls.
     * Invoking this method will cause the unmerged child connections to merge their audio
     * streams.
     */
    public void merge() {
        try {
            mConnectionService.mergeConference(mId);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Swaps the active audio stream between the conference's child {@link RemoteConnection}s.
     * This should be invoked only if the conference contains the capability
     * {@link Connection#CAPABILITY_SWAP_CONFERENCE}, otherwise it is a no-op. This is only used by
     * {@link ConnectionService}s that create conferences for connections that do not yet have
     * their audio streams merged; this is a common pattern for CDMA conference calls, but the
     * capability is not used for GSM and SIP conference calls. Invoking this method will change the
     * active audio stream to a different child connection.
     */
    public void swap() {
        try {
            mConnectionService.swapConference(mId);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Puts the conference on hold.
     */
    public void hold() {
        try {
            mConnectionService.hold(mId);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Unholds the conference call.
     */
    public void unhold() {
        try {
            mConnectionService.unhold(mId);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Returns the {@link DisconnectCause} for the conference if it is in the state
     * {@link Connection#STATE_DISCONNECTED}. If the conference is not disconnected, this will
     * return null.
     *
     * @return The disconnect cause.
     */
    public android.telecom.DisconnectCause getDisconnectCause() {
        return mDisconnectCause;
    }

    /**
     * Requests that the conference start playing the specified DTMF tone.
     *
     * @param digit
     * 		The digit for which to play a DTMF tone.
     */
    public void playDtmfTone(char digit) {
        try {
            mConnectionService.playDtmfTone(mId, digit);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Stops the most recent request to play a DTMF tone.
     *
     * @see #playDtmfTone
     */
    public void stopDtmfTone() {
        try {
            mConnectionService.stopDtmfTone(mId);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Request to change the conference's audio routing to the specified state. The specified state
     * can include audio routing (Bluetooth, Speaker, etc) and muting state.
     *
     * @see android.telecom.AudioState
     * @deprecated Use {@link #setCallAudioState(CallAudioState)} instead.
     * @unknown 
     */
    @android.annotation.SystemApi
    @java.lang.Deprecated
    public void setAudioState(android.telecom.AudioState state) {
        setCallAudioState(new android.telecom.CallAudioState(state));
    }

    /**
     * Request to change the conference's audio routing to the specified state. The specified state
     * can include audio routing (Bluetooth, Speaker, etc) and muting state.
     */
    public void setCallAudioState(android.telecom.CallAudioState state) {
        try {
            mConnectionService.onCallAudioStateChanged(mId, state);
        } catch (android.os.RemoteException e) {
        }
    }

    /**
     * Returns a list of independent connections that can me merged with this conference.
     *
     * @return A list of conferenceable connections.
     */
    public java.util.List<android.telecom.RemoteConnection> getConferenceableConnections() {
        return mUnmodifiableConferenceableConnections;
    }

    /**
     * Register a callback through which to receive state updates for this conference.
     *
     * @param callback
     * 		The callback to notify of state changes.
     */
    public final void registerCallback(android.telecom.RemoteConference.Callback callback) {
        registerCallback(callback, new android.os.Handler());
    }

    /**
     * Registers a callback through which to receive state updates for this conference.
     * Callbacks will be notified using the specified handler, if provided.
     *
     * @param callback
     * 		The callback to notify of state changes.
     * @param handler
     * 		The handler on which to execute the callbacks.
     */
    public final void registerCallback(android.telecom.RemoteConference.Callback callback, android.os.Handler handler) {
        unregisterCallback(callback);
        if ((callback != null) && (handler != null)) {
            mCallbackRecords.add(new android.telecom.CallbackRecord(callback, handler));
        }
    }

    /**
     * Unregisters a previously registered callback.
     *
     * @see #registerCallback
     * @param callback
     * 		The callback to unregister.
     */
    public final void unregisterCallback(android.telecom.RemoteConference.Callback callback) {
        if (callback != null) {
            for (android.telecom.CallbackRecord<android.telecom.RemoteConference.Callback> record : mCallbackRecords) {
                if (record.getCallback() == callback) {
                    mCallbackRecords.remove(record);
                    break;
                }
            }
        }
    }
}

