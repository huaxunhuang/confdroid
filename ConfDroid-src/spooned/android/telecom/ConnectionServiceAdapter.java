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
 * Provides methods for IConnectionService implementations to interact with the system phone app.
 *
 * @unknown 
 */
final class ConnectionServiceAdapter implements android.os.IBinder.DeathRecipient {
    /**
     * ConcurrentHashMap constructor params: 8 is initial table size, 0.9f is
     * load factor before resizing, 1 means we only expect a single thread to
     * access the map so make only a single shard
     */
    private final java.util.Set<com.android.internal.telecom.IConnectionServiceAdapter> mAdapters = java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<com.android.internal.telecom.IConnectionServiceAdapter, java.lang.Boolean>(8, 0.9F, 1));

    ConnectionServiceAdapter() {
    }

    void addAdapter(com.android.internal.telecom.IConnectionServiceAdapter adapter) {
        for (com.android.internal.telecom.IConnectionServiceAdapter it : mAdapters) {
            if (it.asBinder() == adapter.asBinder()) {
                android.telecom.Log.w(this, "Ignoring duplicate adapter addition.");
                return;
            }
        }
        if (mAdapters.add(adapter)) {
            try {
                adapter.asBinder().linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
                mAdapters.remove(adapter);
            }
        }
    }

    void removeAdapter(com.android.internal.telecom.IConnectionServiceAdapter adapter) {
        if (adapter != null) {
            for (com.android.internal.telecom.IConnectionServiceAdapter it : mAdapters) {
                if ((it.asBinder() == adapter.asBinder()) && mAdapters.remove(it)) {
                    adapter.asBinder().unlinkToDeath(this, 0);
                    break;
                }
            }
        }
    }

    /**
     * ${inheritDoc}
     */
    @java.lang.Override
    public void binderDied() {
        java.util.Iterator<com.android.internal.telecom.IConnectionServiceAdapter> it = mAdapters.iterator();
        while (it.hasNext()) {
            com.android.internal.telecom.IConnectionServiceAdapter adapter = it.next();
            if (!adapter.asBinder().isBinderAlive()) {
                it.remove();
                adapter.asBinder().unlinkToDeath(this, 0);
            }
        } 
    }

    void handleCreateConnectionComplete(java.lang.String id, android.telecom.ConnectionRequest request, android.telecom.ParcelableConnection connection) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.handleCreateConnectionComplete(id, request, connection);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /**
     * Sets a call's state to active (e.g., an ongoing call where two parties can actively
     * communicate).
     *
     * @param callId
     * 		The unique ID of the call whose state is changing to active.
     */
    void setActive(java.lang.String callId) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setActive(callId);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /**
     * Sets a call's state to ringing (e.g., an inbound ringing call).
     *
     * @param callId
     * 		The unique ID of the call whose state is changing to ringing.
     */
    void setRinging(java.lang.String callId) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setRinging(callId);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /**
     * Sets a call's state to dialing (e.g., dialing an outbound call).
     *
     * @param callId
     * 		The unique ID of the call whose state is changing to dialing.
     */
    void setDialing(java.lang.String callId) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setDialing(callId);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /**
     * Sets a call's state to pulling (e.g. a call with {@link Connection#PROPERTY_IS_EXTERNAL_CALL}
     * is being pulled to the local device.
     *
     * @param callId
     * 		The unique ID of the call whose state is changing to dialing.
     */
    void setPulling(java.lang.String callId) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setPulling(callId);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /**
     * Sets a call's state to disconnected.
     *
     * @param callId
     * 		The unique ID of the call whose state is changing to disconnected.
     * @param disconnectCause
     * 		The reason for the disconnection, as described by
     * 		{@link android.telecomm.DisconnectCause}.
     */
    void setDisconnected(java.lang.String callId, android.telecom.DisconnectCause disconnectCause) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setDisconnected(callId, disconnectCause);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /**
     * Sets a call's state to be on hold.
     *
     * @param callId
     * 		- The unique ID of the call whose state is changing to be on hold.
     */
    void setOnHold(java.lang.String callId) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setOnHold(callId);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /**
     * Asks Telecom to start or stop a ringback tone for a call.
     *
     * @param callId
     * 		The unique ID of the call whose ringback is being changed.
     * @param ringback
     * 		Whether Telecom should start playing a ringback tone.
     */
    void setRingbackRequested(java.lang.String callId, boolean ringback) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setRingbackRequested(callId, ringback);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    void setConnectionCapabilities(java.lang.String callId, int capabilities) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setConnectionCapabilities(callId, capabilities);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    void setConnectionProperties(java.lang.String callId, int properties) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setConnectionProperties(callId, properties);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    /**
     * Indicates whether or not the specified call is currently conferenced into the specified
     * conference call.
     *
     * @param callId
     * 		The unique ID of the call being conferenced.
     * @param conferenceCallId
     * 		The unique ID of the conference call. Null if call is not
     * 		conferenced.
     */
    void setIsConferenced(java.lang.String callId, java.lang.String conferenceCallId) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                android.telecom.Log.d(this, "sending connection %s with conference %s", callId, conferenceCallId);
                adapter.setIsConferenced(callId, conferenceCallId);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    /**
     * Indicates that the merge request on this call has failed.
     *
     * @param callId
     * 		The unique ID of the call being conferenced.
     */
    void onConferenceMergeFailed(java.lang.String callId) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                android.telecom.Log.d(this, "merge failed for call %s", callId);
                adapter.setConferenceMergeFailed(callId);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    /**
     * Indicates that the call no longer exists. Can be used with either a call or a conference
     * call.
     *
     * @param callId
     * 		The unique ID of the call.
     */
    void removeCall(java.lang.String callId) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.removeCall(callId);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    void onPostDialWait(java.lang.String callId, java.lang.String remaining) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.onPostDialWait(callId, remaining);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    void onPostDialChar(java.lang.String callId, char nextChar) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.onPostDialChar(callId, nextChar);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    /**
     * Indicates that a new conference call has been created.
     *
     * @param callId
     * 		The unique ID of the conference call.
     */
    void addConferenceCall(java.lang.String callId, android.telecom.ParcelableConference parcelableConference) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.addConferenceCall(callId, parcelableConference);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    /**
     * Retrieves a list of remote connection services usable to place calls.
     */
    void queryRemoteConnectionServices(com.android.internal.telecom.RemoteServiceCallback callback) {
        // Only supported when there is only one adapter.
        if (mAdapters.size() == 1) {
            try {
                queryRemoteConnectionServices(callback);
            } catch (android.os.RemoteException e) {
                android.telecom.Log.e(this, e, "Exception trying to query for remote CSs");
            }
        }
    }

    /**
     * Sets the call video provider for a call.
     *
     * @param callId
     * 		The unique ID of the call to set with the given call video provider.
     * @param videoProvider
     * 		The call video provider instance to set on the call.
     */
    void setVideoProvider(java.lang.String callId, android.telecom.Connection.VideoProvider videoProvider) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setVideoProvider(callId, videoProvider == null ? null : videoProvider.getInterface());
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /**
     * Requests that the framework use VOIP audio mode for this connection.
     *
     * @param callId
     * 		The unique ID of the call to set with the given call video provider.
     * @param isVoip
     * 		True if the audio mode is VOIP.
     */
    void setIsVoipAudioMode(java.lang.String callId, boolean isVoip) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setIsVoipAudioMode(callId, isVoip);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    void setStatusHints(java.lang.String callId, android.telecom.StatusHints statusHints) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setStatusHints(callId, statusHints);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    void setAddress(java.lang.String callId, android.net.Uri address, int presentation) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setAddress(callId, address, presentation);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    void setCallerDisplayName(java.lang.String callId, java.lang.String callerDisplayName, int presentation) {
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setCallerDisplayName(callId, callerDisplayName, presentation);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /**
     * Sets the video state associated with a call.
     *
     * Valid values: {@link VideoProfile#STATE_BIDIRECTIONAL},
     * {@link VideoProfile#STATE_AUDIO_ONLY},
     * {@link VideoProfile#STATE_TX_ENABLED},
     * {@link VideoProfile#STATE_RX_ENABLED}.
     *
     * @param callId
     * 		The unique ID of the call to set the video state for.
     * @param videoState
     * 		The video state.
     */
    void setVideoState(java.lang.String callId, int videoState) {
        android.telecom.Log.v(this, "setVideoState: %d", videoState);
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setVideoState(callId, videoState);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    void setConferenceableConnections(java.lang.String callId, java.util.List<java.lang.String> conferenceableCallIds) {
        android.telecom.Log.v(this, "setConferenceableConnections: %s, %s", callId, conferenceableCallIds);
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.setConferenceableConnections(callId, conferenceableCallIds);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    /**
     * Informs telecom of an existing connection which was added by the {@link ConnectionService}.
     *
     * @param callId
     * 		The unique ID of the call being added.
     * @param connection
     * 		The connection.
     */
    void addExistingConnection(java.lang.String callId, android.telecom.ParcelableConnection connection) {
        android.telecom.Log.v(this, "addExistingConnection: %s", callId);
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.addExistingConnection(callId, connection);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    /**
     * Adds some extras associated with a {@code Connection}.
     *
     * @param callId
     * 		The unique ID of the call.
     * @param extras
     * 		The extras to add.
     */
    void putExtras(java.lang.String callId, android.os.Bundle extras) {
        android.telecom.Log.v(this, "putExtras: %s", callId);
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.putExtras(callId, extras);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    /**
     * Adds an extra associated with a {@code Connection}.
     *
     * @param callId
     * 		The unique ID of the call.
     * @param key
     * 		The extra key.
     * @param value
     * 		The extra value.
     */
    void putExtra(java.lang.String callId, java.lang.String key, boolean value) {
        android.telecom.Log.v(this, "putExtra: %s %s=%b", callId, key, value);
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                android.os.Bundle bundle = new android.os.Bundle();
                bundle.putBoolean(key, value);
                adapter.putExtras(callId, bundle);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    /**
     * Adds an extra associated with a {@code Connection}.
     *
     * @param callId
     * 		The unique ID of the call.
     * @param key
     * 		The extra key.
     * @param value
     * 		The extra value.
     */
    void putExtra(java.lang.String callId, java.lang.String key, int value) {
        android.telecom.Log.v(this, "putExtra: %s %s=%d", callId, key, value);
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                android.os.Bundle bundle = new android.os.Bundle();
                bundle.putInt(key, value);
                adapter.putExtras(callId, bundle);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    /**
     * Adds an extra associated with a {@code Connection}.
     *
     * @param callId
     * 		The unique ID of the call.
     * @param key
     * 		The extra key.
     * @param value
     * 		The extra value.
     */
    void putExtra(java.lang.String callId, java.lang.String key, java.lang.String value) {
        android.telecom.Log.v(this, "putExtra: %s %s=%s", callId, key, value);
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                android.os.Bundle bundle = new android.os.Bundle();
                bundle.putString(key, value);
                adapter.putExtras(callId, bundle);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    /**
     * Removes extras associated with a {@code Connection}.
     *
     * @param callId
     * 		The unique ID of the call.
     * @param keys
     * 		The extra keys to remove.
     */
    void removeExtras(java.lang.String callId, java.util.List<java.lang.String> keys) {
        android.telecom.Log.v(this, "removeExtras: %s %s", callId, keys);
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.removeExtras(callId, keys);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }

    /**
     * Informs Telecom of a connection level event.
     *
     * @param callId
     * 		The unique ID of the call.
     * @param event
     * 		The event.
     * @param extras
     * 		Extras associated with the event.
     */
    void onConnectionEvent(java.lang.String callId, java.lang.String event, android.os.Bundle extras) {
        android.telecom.Log.v(this, "onConnectionEvent: %s", event);
        for (com.android.internal.telecom.IConnectionServiceAdapter adapter : mAdapters) {
            try {
                adapter.onConnectionEvent(callId, event, extras);
            } catch (android.os.RemoteException ignored) {
            }
        }
    }
}

