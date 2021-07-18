/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.net.sip;


/**
 * Represents a SIP session that is associated with a SIP dialog or a standalone
 * transaction not within a dialog.
 * <p>You can get a {@link SipSession} from {@link SipManager} with {@link SipManager#createSipSession createSipSession()} (when initiating calls) or {@link SipManager#getSessionFor getSessionFor()} (when receiving calls).</p>
 */
public final class SipSession {
    private static final java.lang.String TAG = "SipSession";

    /**
     * Defines SIP session states, such as "registering", "outgoing call", and "in call".
     */
    public static class State {
        /**
         * When session is ready to initiate a call or transaction.
         */
        public static final int READY_TO_CALL = 0;

        /**
         * When the registration request is sent out.
         */
        public static final int REGISTERING = 1;

        /**
         * When the unregistration request is sent out.
         */
        public static final int DEREGISTERING = 2;

        /**
         * When an INVITE request is received.
         */
        public static final int INCOMING_CALL = 3;

        /**
         * When an OK response is sent for the INVITE request received.
         */
        public static final int INCOMING_CALL_ANSWERING = 4;

        /**
         * When an INVITE request is sent.
         */
        public static final int OUTGOING_CALL = 5;

        /**
         * When a RINGING response is received for the INVITE request sent.
         */
        public static final int OUTGOING_CALL_RING_BACK = 6;

        /**
         * When a CANCEL request is sent for the INVITE request sent.
         */
        public static final int OUTGOING_CALL_CANCELING = 7;

        /**
         * When a call is established.
         */
        public static final int IN_CALL = 8;

        /**
         * When an OPTIONS request is sent.
         */
        public static final int PINGING = 9;

        /**
         * When ending a call. @hide
         */
        public static final int ENDING_CALL = 10;

        /**
         * Not defined.
         */
        public static final int NOT_DEFINED = 101;

        /**
         * Converts the state to string.
         */
        public static java.lang.String toString(int state) {
            switch (state) {
                case android.net.sip.SipSession.State.READY_TO_CALL :
                    return "READY_TO_CALL";
                case android.net.sip.SipSession.State.REGISTERING :
                    return "REGISTERING";
                case android.net.sip.SipSession.State.DEREGISTERING :
                    return "DEREGISTERING";
                case android.net.sip.SipSession.State.INCOMING_CALL :
                    return "INCOMING_CALL";
                case android.net.sip.SipSession.State.INCOMING_CALL_ANSWERING :
                    return "INCOMING_CALL_ANSWERING";
                case android.net.sip.SipSession.State.OUTGOING_CALL :
                    return "OUTGOING_CALL";
                case android.net.sip.SipSession.State.OUTGOING_CALL_RING_BACK :
                    return "OUTGOING_CALL_RING_BACK";
                case android.net.sip.SipSession.State.OUTGOING_CALL_CANCELING :
                    return "OUTGOING_CALL_CANCELING";
                case android.net.sip.SipSession.State.IN_CALL :
                    return "IN_CALL";
                case android.net.sip.SipSession.State.PINGING :
                    return "PINGING";
                default :
                    return "NOT_DEFINED";
            }
        }

        private State() {
        }
    }

    /**
     * Listener for events relating to a SIP session, such as when a session is being registered
     * ("on registering") or a call is outgoing ("on calling").
     * <p>Many of these events are also received by {@link SipAudioCall.Listener}.</p>
     */
    public static class Listener {
        /**
         * Called when an INVITE request is sent to initiate a new call.
         *
         * @param session
         * 		the session object that carries out the transaction
         */
        public void onCalling(android.net.sip.SipSession session) {
        }

        /**
         * Called when an INVITE request is received.
         *
         * @param session
         * 		the session object that carries out the transaction
         * @param caller
         * 		the SIP profile of the caller
         * @param sessionDescription
         * 		the caller's session description
         */
        public void onRinging(android.net.sip.SipSession session, android.net.sip.SipProfile caller, java.lang.String sessionDescription) {
        }

        /**
         * Called when a RINGING response is received for the INVITE request sent
         *
         * @param session
         * 		the session object that carries out the transaction
         */
        public void onRingingBack(android.net.sip.SipSession session) {
        }

        /**
         * Called when the session is established.
         *
         * @param session
         * 		the session object that is associated with the dialog
         * @param sessionDescription
         * 		the peer's session description
         */
        public void onCallEstablished(android.net.sip.SipSession session, java.lang.String sessionDescription) {
        }

        /**
         * Called when the session is terminated.
         *
         * @param session
         * 		the session object that is associated with the dialog
         */
        public void onCallEnded(android.net.sip.SipSession session) {
        }

        /**
         * Called when the peer is busy during session initialization.
         *
         * @param session
         * 		the session object that carries out the transaction
         */
        public void onCallBusy(android.net.sip.SipSession session) {
        }

        /**
         * Called when the call is being transferred to a new one.
         *
         * @unknown 
         * @param newSession
         * 		the new session that the call will be transferred to
         * @param sessionDescription
         * 		the new peer's session description
         */
        public void onCallTransferring(android.net.sip.SipSession newSession, java.lang.String sessionDescription) {
        }

        /**
         * Called when an error occurs during session initialization and
         * termination.
         *
         * @param session
         * 		the session object that carries out the transaction
         * @param errorCode
         * 		error code defined in {@link SipErrorCode}
         * @param errorMessage
         * 		error message
         */
        public void onError(android.net.sip.SipSession session, int errorCode, java.lang.String errorMessage) {
        }

        /**
         * Called when an error occurs during session modification negotiation.
         *
         * @param session
         * 		the session object that carries out the transaction
         * @param errorCode
         * 		error code defined in {@link SipErrorCode}
         * @param errorMessage
         * 		error message
         */
        public void onCallChangeFailed(android.net.sip.SipSession session, int errorCode, java.lang.String errorMessage) {
        }

        /**
         * Called when a registration request is sent.
         *
         * @param session
         * 		the session object that carries out the transaction
         */
        public void onRegistering(android.net.sip.SipSession session) {
        }

        /**
         * Called when registration is successfully done.
         *
         * @param session
         * 		the session object that carries out the transaction
         * @param duration
         * 		duration in second before the registration expires
         */
        public void onRegistrationDone(android.net.sip.SipSession session, int duration) {
        }

        /**
         * Called when the registration fails.
         *
         * @param session
         * 		the session object that carries out the transaction
         * @param errorCode
         * 		error code defined in {@link SipErrorCode}
         * @param errorMessage
         * 		error message
         */
        public void onRegistrationFailed(android.net.sip.SipSession session, int errorCode, java.lang.String errorMessage) {
        }

        /**
         * Called when the registration gets timed out.
         *
         * @param session
         * 		the session object that carries out the transaction
         */
        public void onRegistrationTimeout(android.net.sip.SipSession session) {
        }
    }

    private final android.net.sip.ISipSession mSession;

    private android.net.sip.SipSession.Listener mListener;

    SipSession(android.net.sip.ISipSession realSession) {
        mSession = realSession;
        if (realSession != null) {
            try {
                realSession.setListener(createListener());
            } catch (android.os.RemoteException e) {
                loge("SipSession.setListener:", e);
            }
        }
    }

    SipSession(android.net.sip.ISipSession realSession, android.net.sip.SipSession.Listener listener) {
        this(realSession);
        setListener(listener);
    }

    /**
     * Gets the IP address of the local host on which this SIP session runs.
     *
     * @return the IP address of the local host
     */
    public java.lang.String getLocalIp() {
        try {
            return mSession.getLocalIp();
        } catch (android.os.RemoteException e) {
            loge("getLocalIp:", e);
            return "127.0.0.1";
        }
    }

    /**
     * Gets the SIP profile that this session is associated with.
     *
     * @return the SIP profile that this session is associated with
     */
    public android.net.sip.SipProfile getLocalProfile() {
        try {
            return mSession.getLocalProfile();
        } catch (android.os.RemoteException e) {
            loge("getLocalProfile:", e);
            return null;
        }
    }

    /**
     * Gets the SIP profile that this session is connected to. Only available
     * when the session is associated with a SIP dialog.
     *
     * @return the SIP profile that this session is connected to
     */
    public android.net.sip.SipProfile getPeerProfile() {
        try {
            return mSession.getPeerProfile();
        } catch (android.os.RemoteException e) {
            loge("getPeerProfile:", e);
            return null;
        }
    }

    /**
     * Gets the session state. The value returned must be one of the states in
     * {@link State}.
     *
     * @return the session state
     */
    public int getState() {
        try {
            return mSession.getState();
        } catch (android.os.RemoteException e) {
            loge("getState:", e);
            return android.net.sip.SipSession.State.NOT_DEFINED;
        }
    }

    /**
     * Checks if the session is in a call.
     *
     * @return true if the session is in a call
     */
    public boolean isInCall() {
        try {
            return mSession.isInCall();
        } catch (android.os.RemoteException e) {
            loge("isInCall:", e);
            return false;
        }
    }

    /**
     * Gets the call ID of the session.
     *
     * @return the call ID
     */
    public java.lang.String getCallId() {
        try {
            return mSession.getCallId();
        } catch (android.os.RemoteException e) {
            loge("getCallId:", e);
            return null;
        }
    }

    /**
     * Sets the listener to listen to the session events. A {@code SipSession}
     * can only hold one listener at a time. Subsequent calls to this method
     * override the previous listener.
     *
     * @param listener
     * 		to listen to the session events of this object
     */
    public void setListener(android.net.sip.SipSession.Listener listener) {
        mListener = listener;
    }

    /**
     * Performs registration to the server specified by the associated local
     * profile. The session listener is called back upon success or failure of
     * registration. The method is only valid to call when the session state is
     * in {@link State#READY_TO_CALL}.
     *
     * @param duration
     * 		duration in second before the registration expires
     * @see Listener
     */
    public void register(int duration) {
        try {
            mSession.register(duration);
        } catch (android.os.RemoteException e) {
            loge("register:", e);
        }
    }

    /**
     * Performs unregistration to the server specified by the associated local
     * profile. Unregistration is technically the same as registration with zero
     * expiration duration. The session listener is called back upon success or
     * failure of unregistration. The method is only valid to call when the
     * session state is in {@link State#READY_TO_CALL}.
     *
     * @see Listener
     */
    public void unregister() {
        try {
            mSession.unregister();
        } catch (android.os.RemoteException e) {
            loge("unregister:", e);
        }
    }

    /**
     * Initiates a call to the specified profile. The session listener is called
     * back upon defined session events. The method is only valid to call when
     * the session state is in {@link State#READY_TO_CALL}.
     *
     * @param callee
     * 		the SIP profile to make the call to
     * @param sessionDescription
     * 		the session description of this call
     * @param timeout
     * 		the session will be timed out if the call is not
     * 		established within {@code timeout} seconds. Default value (defined
     * 		by SIP protocol) is used if {@code timeout} is zero or negative.
     * @see Listener
     */
    public void makeCall(android.net.sip.SipProfile callee, java.lang.String sessionDescription, int timeout) {
        try {
            mSession.makeCall(callee, sessionDescription, timeout);
        } catch (android.os.RemoteException e) {
            loge("makeCall:", e);
        }
    }

    /**
     * Answers an incoming call with the specified session description. The
     * method is only valid to call when the session state is in
     * {@link State#INCOMING_CALL}.
     *
     * @param sessionDescription
     * 		the session description to answer this call
     * @param timeout
     * 		the session will be timed out if the call is not
     * 		established within {@code timeout} seconds. Default value (defined
     * 		by SIP protocol) is used if {@code timeout} is zero or negative.
     */
    public void answerCall(java.lang.String sessionDescription, int timeout) {
        try {
            mSession.answerCall(sessionDescription, timeout);
        } catch (android.os.RemoteException e) {
            loge("answerCall:", e);
        }
    }

    /**
     * Ends an established call, terminates an outgoing call or rejects an
     * incoming call. The method is only valid to call when the session state is
     * in {@link State#IN_CALL},
     * {@link State#INCOMING_CALL},
     * {@link State#OUTGOING_CALL} or
     * {@link State#OUTGOING_CALL_RING_BACK}.
     */
    public void endCall() {
        try {
            mSession.endCall();
        } catch (android.os.RemoteException e) {
            loge("endCall:", e);
        }
    }

    /**
     * Changes the session description during a call. The method is only valid
     * to call when the session state is in {@link State#IN_CALL}.
     *
     * @param sessionDescription
     * 		the new session description
     * @param timeout
     * 		the session will be timed out if the call is not
     * 		established within {@code timeout} seconds. Default value (defined
     * 		by SIP protocol) is used if {@code timeout} is zero or negative.
     */
    public void changeCall(java.lang.String sessionDescription, int timeout) {
        try {
            mSession.changeCall(sessionDescription, timeout);
        } catch (android.os.RemoteException e) {
            loge("changeCall:", e);
        }
    }

    android.net.sip.ISipSession getRealSession() {
        return mSession;
    }

    private android.net.sip.ISipSessionListener createListener() {
        return new android.net.sip.ISipSessionListener.Stub() {
            @java.lang.Override
            public void onCalling(android.net.sip.ISipSession session) {
                if (mListener != null) {
                    mListener.onCalling(android.net.sip.SipSession.this);
                }
            }

            @java.lang.Override
            public void onRinging(android.net.sip.ISipSession session, android.net.sip.SipProfile caller, java.lang.String sessionDescription) {
                if (mListener != null) {
                    mListener.onRinging(android.net.sip.SipSession.this, caller, sessionDescription);
                }
            }

            @java.lang.Override
            public void onRingingBack(android.net.sip.ISipSession session) {
                if (mListener != null) {
                    mListener.onRingingBack(android.net.sip.SipSession.this);
                }
            }

            @java.lang.Override
            public void onCallEstablished(android.net.sip.ISipSession session, java.lang.String sessionDescription) {
                if (mListener != null) {
                    mListener.onCallEstablished(android.net.sip.SipSession.this, sessionDescription);
                }
            }

            @java.lang.Override
            public void onCallEnded(android.net.sip.ISipSession session) {
                if (mListener != null) {
                    mListener.onCallEnded(android.net.sip.SipSession.this);
                }
            }

            @java.lang.Override
            public void onCallBusy(android.net.sip.ISipSession session) {
                if (mListener != null) {
                    mListener.onCallBusy(android.net.sip.SipSession.this);
                }
            }

            @java.lang.Override
            public void onCallTransferring(android.net.sip.ISipSession session, java.lang.String sessionDescription) {
                if (mListener != null) {
                    mListener.onCallTransferring(new android.net.sip.SipSession(session, android.net.sip.SipSession.this.mListener), sessionDescription);
                }
            }

            @java.lang.Override
            public void onCallChangeFailed(android.net.sip.ISipSession session, int errorCode, java.lang.String message) {
                if (mListener != null) {
                    mListener.onCallChangeFailed(android.net.sip.SipSession.this, errorCode, message);
                }
            }

            @java.lang.Override
            public void onError(android.net.sip.ISipSession session, int errorCode, java.lang.String message) {
                if (mListener != null) {
                    mListener.onError(android.net.sip.SipSession.this, errorCode, message);
                }
            }

            @java.lang.Override
            public void onRegistering(android.net.sip.ISipSession session) {
                if (mListener != null) {
                    mListener.onRegistering(android.net.sip.SipSession.this);
                }
            }

            @java.lang.Override
            public void onRegistrationDone(android.net.sip.ISipSession session, int duration) {
                if (mListener != null) {
                    mListener.onRegistrationDone(android.net.sip.SipSession.this, duration);
                }
            }

            @java.lang.Override
            public void onRegistrationFailed(android.net.sip.ISipSession session, int errorCode, java.lang.String message) {
                if (mListener != null) {
                    mListener.onRegistrationFailed(android.net.sip.SipSession.this, errorCode, message);
                }
            }

            @java.lang.Override
            public void onRegistrationTimeout(android.net.sip.ISipSession session) {
                if (mListener != null) {
                    mListener.onRegistrationTimeout(android.net.sip.SipSession.this);
                }
            }
        };
    }

    private void loge(java.lang.String s, java.lang.Throwable t) {
        android.telephony.Rlog.e(android.net.sip.SipSession.TAG, s, t);
    }
}

