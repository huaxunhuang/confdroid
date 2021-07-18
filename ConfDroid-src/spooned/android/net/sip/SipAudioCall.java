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
 * Handles an Internet audio call over SIP. You can instantiate this class with {@link SipManager},
 * using {@link SipManager#makeAudioCall makeAudioCall()} and  {@link SipManager#takeAudioCall
 * takeAudioCall()}.
 *
 * <p class="note"><strong>Note:</strong> Using this class require the
 *   {@link android.Manifest.permission#INTERNET} and
 *   {@link android.Manifest.permission#USE_SIP} permissions. In addition, {@link #startAudio} requires the
 *   {@link android.Manifest.permission#RECORD_AUDIO},
 *   {@link android.Manifest.permission#ACCESS_WIFI_STATE}, and
 *   {@link android.Manifest.permission#WAKE_LOCK} permissions; and {@link #setSpeakerMode
 *   setSpeakerMode()} requires the
 *   {@link android.Manifest.permission#MODIFY_AUDIO_SETTINGS} permission.</p>
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about using SIP, read the
 * <a href="{@docRoot }guide/topics/network/sip.html">Session Initiation Protocol</a>
 * developer guide.</p>
 * </div>
 */
public class SipAudioCall {
    private static final java.lang.String LOG_TAG = android.net.sip.SipAudioCall.class.getSimpleName();

    private static final boolean DBG = true;

    private static final boolean RELEASE_SOCKET = true;

    private static final boolean DONT_RELEASE_SOCKET = false;

    private static final int SESSION_TIMEOUT = 5;// in seconds


    private static final int TRANSFER_TIMEOUT = 15;// in seconds


    /**
     * Listener for events relating to a SIP call, such as when a call is being
     * recieved ("on ringing") or a call is outgoing ("on calling").
     * <p>Many of these events are also received by {@link SipSession.Listener}.</p>
     */
    public static class Listener {
        /**
         * Called when the call object is ready to make another call.
         * The default implementation calls {@link #onChanged}.
         *
         * @param call
         * 		the call object that is ready to make another call
         */
        public void onReadyToCall(android.net.sip.SipAudioCall call) {
            onChanged(call);
        }

        /**
         * Called when a request is sent out to initiate a new call.
         * The default implementation calls {@link #onChanged}.
         *
         * @param call
         * 		the call object that carries out the audio call
         */
        public void onCalling(android.net.sip.SipAudioCall call) {
            onChanged(call);
        }

        /**
         * Called when a new call comes in.
         * The default implementation calls {@link #onChanged}.
         *
         * @param call
         * 		the call object that carries out the audio call
         * @param caller
         * 		the SIP profile of the caller
         */
        public void onRinging(android.net.sip.SipAudioCall call, android.net.sip.SipProfile caller) {
            onChanged(call);
        }

        /**
         * Called when a RINGING response is received for the INVITE request
         * sent. The default implementation calls {@link #onChanged}.
         *
         * @param call
         * 		the call object that carries out the audio call
         */
        public void onRingingBack(android.net.sip.SipAudioCall call) {
            onChanged(call);
        }

        /**
         * Called when the session is established.
         * The default implementation calls {@link #onChanged}.
         *
         * @param call
         * 		the call object that carries out the audio call
         */
        public void onCallEstablished(android.net.sip.SipAudioCall call) {
            onChanged(call);
        }

        /**
         * Called when the session is terminated.
         * The default implementation calls {@link #onChanged}.
         *
         * @param call
         * 		the call object that carries out the audio call
         */
        public void onCallEnded(android.net.sip.SipAudioCall call) {
            onChanged(call);
        }

        /**
         * Called when the peer is busy during session initialization.
         * The default implementation calls {@link #onChanged}.
         *
         * @param call
         * 		the call object that carries out the audio call
         */
        public void onCallBusy(android.net.sip.SipAudioCall call) {
            onChanged(call);
        }

        /**
         * Called when the call is on hold.
         * The default implementation calls {@link #onChanged}.
         *
         * @param call
         * 		the call object that carries out the audio call
         */
        public void onCallHeld(android.net.sip.SipAudioCall call) {
            onChanged(call);
        }

        /**
         * Called when an error occurs. The default implementation is no op.
         *
         * @param call
         * 		the call object that carries out the audio call
         * @param errorCode
         * 		error code of this error
         * @param errorMessage
         * 		error message
         * @see SipErrorCode
         */
        public void onError(android.net.sip.SipAudioCall call, int errorCode, java.lang.String errorMessage) {
            // no-op
        }

        /**
         * Called when an event occurs and the corresponding callback is not
         * overridden. The default implementation is no op. Error events are
         * not re-directed to this callback and are handled in {@link #onError}.
         */
        public void onChanged(android.net.sip.SipAudioCall call) {
            // no-op
        }
    }

    private android.content.Context mContext;

    private android.net.sip.SipProfile mLocalProfile;

    private android.net.sip.SipAudioCall.Listener mListener;

    private android.net.sip.SipSession mSipSession;

    private android.net.sip.SipSession mTransferringSession;

    private long mSessionId = java.lang.System.currentTimeMillis();

    private java.lang.String mPeerSd;

    private android.net.rtp.AudioStream mAudioStream;

    private android.net.rtp.AudioGroup mAudioGroup;

    private boolean mInCall = false;

    private boolean mMuted = false;

    private boolean mHold = false;

    private android.net.wifi.WifiManager mWm;

    private android.net.wifi.WifiManager.WifiLock mWifiHighPerfLock;

    private int mErrorCode = android.net.sip.SipErrorCode.NO_ERROR;

    private java.lang.String mErrorMessage;

    /**
     * Creates a call object with the local SIP profile.
     *
     * @param context
     * 		the context for accessing system services such as
     * 		ringtone, audio, WIFI etc
     */
    public SipAudioCall(android.content.Context context, android.net.sip.SipProfile localProfile) {
        mContext = context;
        mLocalProfile = localProfile;
        mWm = ((android.net.wifi.WifiManager) (context.getSystemService(android.content.Context.WIFI_SERVICE)));
    }

    /**
     * Sets the listener to listen to the audio call events. The method calls
     * {@link #setListener setListener(listener, false)}.
     *
     * @param listener
     * 		to listen to the audio call events of this object
     * @see #setListener(Listener, boolean)
     */
    public void setListener(android.net.sip.SipAudioCall.Listener listener) {
        setListener(listener, false);
    }

    /**
     * Sets the listener to listen to the audio call events. A
     * {@link SipAudioCall} can only hold one listener at a time. Subsequent
     * calls to this method override the previous listener.
     *
     * @param listener
     * 		to listen to the audio call events of this object
     * @param callbackImmediately
     * 		set to true if the caller wants to be called
     * 		back immediately on the current state
     */
    public void setListener(android.net.sip.SipAudioCall.Listener listener, boolean callbackImmediately) {
        mListener = listener;
        try {
            if ((listener == null) || (!callbackImmediately)) {
                // do nothing
            } else
                if (mErrorCode != android.net.sip.SipErrorCode.NO_ERROR) {
                    listener.onError(this, mErrorCode, mErrorMessage);
                } else
                    if (mInCall) {
                        if (mHold) {
                            listener.onCallHeld(this);
                        } else {
                            listener.onCallEstablished(this);
                        }
                    } else {
                        int state = getState();
                        switch (state) {
                            case android.net.sip.SipSession.State.READY_TO_CALL :
                                listener.onReadyToCall(this);
                                break;
                            case android.net.sip.SipSession.State.INCOMING_CALL :
                                listener.onRinging(this, getPeerProfile());
                                break;
                            case android.net.sip.SipSession.State.OUTGOING_CALL :
                                listener.onCalling(this);
                                break;
                            case android.net.sip.SipSession.State.OUTGOING_CALL_RING_BACK :
                                listener.onRingingBack(this);
                                break;
                        }
                    }


        } catch (java.lang.Throwable t) {
            loge("setListener()", t);
        }
    }

    /**
     * Checks if the call is established.
     *
     * @return true if the call is established
     */
    public boolean isInCall() {
        synchronized(this) {
            return mInCall;
        }
    }

    /**
     * Checks if the call is on hold.
     *
     * @return true if the call is on hold
     */
    public boolean isOnHold() {
        synchronized(this) {
            return mHold;
        }
    }

    /**
     * Closes this object. This object is not usable after being closed.
     */
    public void close() {
        close(true);
    }

    private synchronized void close(boolean closeRtp) {
        if (closeRtp)
            stopCall(android.net.sip.SipAudioCall.RELEASE_SOCKET);

        mInCall = false;
        mHold = false;
        mSessionId = java.lang.System.currentTimeMillis();
        mErrorCode = android.net.sip.SipErrorCode.NO_ERROR;
        mErrorMessage = null;
        if (mSipSession != null) {
            mSipSession.setListener(null);
            mSipSession = null;
        }
    }

    /**
     * Gets the local SIP profile.
     *
     * @return the local SIP profile
     */
    public android.net.sip.SipProfile getLocalProfile() {
        synchronized(this) {
            return mLocalProfile;
        }
    }

    /**
     * Gets the peer's SIP profile.
     *
     * @return the peer's SIP profile
     */
    public android.net.sip.SipProfile getPeerProfile() {
        synchronized(this) {
            return mSipSession == null ? null : mSipSession.getPeerProfile();
        }
    }

    /**
     * Gets the state of the {@link SipSession} that carries this call.
     * The value returned must be one of the states in {@link SipSession.State}.
     *
     * @return the session state
     */
    public int getState() {
        synchronized(this) {
            if (mSipSession == null)
                return android.net.sip.SipSession.State.READY_TO_CALL;

            return mSipSession.getState();
        }
    }

    /**
     * Gets the {@link SipSession} that carries this call.
     *
     * @return the session object that carries this call
     * @unknown 
     */
    public android.net.sip.SipSession getSipSession() {
        synchronized(this) {
            return mSipSession;
        }
    }

    private synchronized void transferToNewSession() {
        if (mTransferringSession == null)
            return;

        android.net.sip.SipSession origin = mSipSession;
        mSipSession = mTransferringSession;
        mTransferringSession = null;
        // stop the replaced call.
        if (mAudioStream != null) {
            mAudioStream.join(null);
        } else {
            try {
                mAudioStream = new android.net.rtp.AudioStream(java.net.InetAddress.getByName(getLocalIp()));
            } catch (java.lang.Throwable t) {
                loge("transferToNewSession():", t);
            }
        }
        if (origin != null)
            origin.endCall();

        startAudio();
    }

    private android.net.sip.SipSession.Listener createListener() {
        return new android.net.sip.SipSession.Listener() {
            @java.lang.Override
            public void onCalling(android.net.sip.SipSession session) {
                if (android.net.sip.SipAudioCall.DBG)
                    log("onCalling: session=" + session);

                android.net.sip.SipAudioCall.Listener listener = mListener;
                if (listener != null) {
                    try {
                        listener.onCalling(android.net.sip.SipAudioCall.this);
                    } catch (java.lang.Throwable t) {
                        loge("onCalling():", t);
                    }
                }
            }

            @java.lang.Override
            public void onRingingBack(android.net.sip.SipSession session) {
                if (android.net.sip.SipAudioCall.DBG)
                    log("onRingingBackk: " + session);

                android.net.sip.SipAudioCall.Listener listener = mListener;
                if (listener != null) {
                    try {
                        listener.onRingingBack(android.net.sip.SipAudioCall.this);
                    } catch (java.lang.Throwable t) {
                        loge("onRingingBack():", t);
                    }
                }
            }

            @java.lang.Override
            public void onRinging(android.net.sip.SipSession session, android.net.sip.SipProfile peerProfile, java.lang.String sessionDescription) {
                // this callback is triggered only for reinvite.
                synchronized(android.net.sip.SipAudioCall.this) {
                    if (((mSipSession == null) || (!mInCall)) || (!session.getCallId().equals(mSipSession.getCallId()))) {
                        // should not happen
                        session.endCall();
                        return;
                    }
                    // session changing request
                    try {
                        java.lang.String answer = createAnswer(sessionDescription).encode();
                        mSipSession.answerCall(answer, android.net.sip.SipAudioCall.SESSION_TIMEOUT);
                    } catch (java.lang.Throwable e) {
                        loge("onRinging():", e);
                        session.endCall();
                    }
                }
            }

            @java.lang.Override
            public void onCallEstablished(android.net.sip.SipSession session, java.lang.String sessionDescription) {
                mPeerSd = sessionDescription;
                if (android.net.sip.SipAudioCall.DBG)
                    log("onCallEstablished(): " + mPeerSd);

                // TODO: how to notify the UI that the remote party is changed
                if ((mTransferringSession != null) && (session == mTransferringSession)) {
                    transferToNewSession();
                    return;
                }
                android.net.sip.SipAudioCall.Listener listener = mListener;
                if (listener != null) {
                    try {
                        if (mHold) {
                            listener.onCallHeld(android.net.sip.SipAudioCall.this);
                        } else {
                            listener.onCallEstablished(android.net.sip.SipAudioCall.this);
                        }
                    } catch (java.lang.Throwable t) {
                        loge("onCallEstablished(): ", t);
                    }
                }
            }

            @java.lang.Override
            public void onCallEnded(android.net.sip.SipSession session) {
                if (android.net.sip.SipAudioCall.DBG)
                    log((("onCallEnded: " + session) + " mSipSession:") + mSipSession);

                // reset the trasnferring session if it is the one.
                if (session == mTransferringSession) {
                    mTransferringSession = null;
                    return;
                }
                // or ignore the event if the original session is being
                // transferred to the new one.
                if ((mTransferringSession != null) || (session != mSipSession))
                    return;

                android.net.sip.SipAudioCall.Listener listener = mListener;
                if (listener != null) {
                    try {
                        listener.onCallEnded(android.net.sip.SipAudioCall.this);
                    } catch (java.lang.Throwable t) {
                        loge("onCallEnded(): ", t);
                    }
                }
                close();
            }

            @java.lang.Override
            public void onCallBusy(android.net.sip.SipSession session) {
                if (android.net.sip.SipAudioCall.DBG)
                    log("onCallBusy: " + session);

                android.net.sip.SipAudioCall.Listener listener = mListener;
                if (listener != null) {
                    try {
                        listener.onCallBusy(android.net.sip.SipAudioCall.this);
                    } catch (java.lang.Throwable t) {
                        loge("onCallBusy(): ", t);
                    }
                }
                close(false);
            }

            @java.lang.Override
            public void onCallChangeFailed(android.net.sip.SipSession session, int errorCode, java.lang.String message) {
                if (android.net.sip.SipAudioCall.DBG)
                    log("onCallChangedFailed: " + message);

                mErrorCode = errorCode;
                mErrorMessage = message;
                android.net.sip.SipAudioCall.Listener listener = mListener;
                if (listener != null) {
                    try {
                        listener.onError(android.net.sip.SipAudioCall.this, mErrorCode, message);
                    } catch (java.lang.Throwable t) {
                        loge("onCallBusy():", t);
                    }
                }
            }

            @java.lang.Override
            public void onError(android.net.sip.SipSession session, int errorCode, java.lang.String message) {
                android.net.sip.SipAudioCall.this.onError(errorCode, message);
            }

            @java.lang.Override
            public void onRegistering(android.net.sip.SipSession session) {
                // irrelevant
            }

            @java.lang.Override
            public void onRegistrationTimeout(android.net.sip.SipSession session) {
                // irrelevant
            }

            @java.lang.Override
            public void onRegistrationFailed(android.net.sip.SipSession session, int errorCode, java.lang.String message) {
                // irrelevant
            }

            @java.lang.Override
            public void onRegistrationDone(android.net.sip.SipSession session, int duration) {
                // irrelevant
            }

            @java.lang.Override
            public void onCallTransferring(android.net.sip.SipSession newSession, java.lang.String sessionDescription) {
                if (android.net.sip.SipAudioCall.DBG)
                    log((("onCallTransferring: mSipSession=" + mSipSession) + " newSession=") + newSession);

                mTransferringSession = newSession;
                try {
                    if (sessionDescription == null) {
                        newSession.makeCall(newSession.getPeerProfile(), createOffer().encode(), android.net.sip.SipAudioCall.TRANSFER_TIMEOUT);
                    } else {
                        java.lang.String answer = createAnswer(sessionDescription).encode();
                        newSession.answerCall(answer, android.net.sip.SipAudioCall.SESSION_TIMEOUT);
                    }
                } catch (java.lang.Throwable e) {
                    loge("onCallTransferring()", e);
                    newSession.endCall();
                }
            }
        };
    }

    private void onError(int errorCode, java.lang.String message) {
        if (android.net.sip.SipAudioCall.DBG)
            log((("onError: " + android.net.sip.SipErrorCode.toString(errorCode)) + ": ") + message);

        mErrorCode = errorCode;
        mErrorMessage = message;
        android.net.sip.SipAudioCall.Listener listener = mListener;
        if (listener != null) {
            try {
                listener.onError(this, errorCode, message);
            } catch (java.lang.Throwable t) {
                loge("onError():", t);
            }
        }
        synchronized(this) {
            if ((errorCode == android.net.sip.SipErrorCode.DATA_CONNECTION_LOST) || (!isInCall())) {
                close(true);
            }
        }
    }

    /**
     * Attaches an incoming call to this call object.
     *
     * @param session
     * 		the session that receives the incoming call
     * @param sessionDescription
     * 		the session description of the incoming call
     * @throws SipException
     * 		if the SIP service fails to attach this object to
     * 		the session or VOIP API is not supported by the device
     * @see SipManager#isVoipSupported
     */
    public void attachCall(android.net.sip.SipSession session, java.lang.String sessionDescription) throws android.net.sip.SipException {
        if (!android.net.sip.SipManager.isVoipSupported(mContext)) {
            throw new android.net.sip.SipException("VOIP API is not supported");
        }
        synchronized(this) {
            mSipSession = session;
            mPeerSd = sessionDescription;
            if (android.net.sip.SipAudioCall.DBG)
                log("attachCall(): " + mPeerSd);

            try {
                session.setListener(createListener());
            } catch (java.lang.Throwable e) {
                loge("attachCall()", e);
                throwSipException(e);
            }
        }
    }

    /**
     * Initiates an audio call to the specified profile. The attempt will be
     * timed out if the call is not established within {@code timeout} seconds
     * and {@link Listener#onError onError(SipAudioCall, SipErrorCode.TIME_OUT, String)}
     * will be called.
     *
     * @param peerProfile
     * 		the SIP profile to make the call to
     * @param sipSession
     * 		the {@link SipSession} for carrying out the call
     * @param timeout
     * 		the timeout value in seconds. Default value (defined by
     * 		SIP protocol) is used if {@code timeout} is zero or negative.
     * @see Listener#onError
     * @throws SipException
     * 		if the SIP service fails to create a session for the
     * 		call or VOIP API is not supported by the device
     * @see SipManager#isVoipSupported
     */
    public void makeCall(android.net.sip.SipProfile peerProfile, android.net.sip.SipSession sipSession, int timeout) throws android.net.sip.SipException {
        if (android.net.sip.SipAudioCall.DBG)
            log((((("makeCall: " + peerProfile) + " session=") + sipSession) + " timeout=") + timeout);

        if (!android.net.sip.SipManager.isVoipSupported(mContext)) {
            throw new android.net.sip.SipException("VOIP API is not supported");
        }
        synchronized(this) {
            mSipSession = sipSession;
            try {
                mAudioStream = new android.net.rtp.AudioStream(java.net.InetAddress.getByName(getLocalIp()));
                sipSession.setListener(createListener());
                sipSession.makeCall(peerProfile, createOffer().encode(), timeout);
            } catch (java.io.IOException e) {
                loge("makeCall:", e);
                throw new android.net.sip.SipException("makeCall()", e);
            }
        }
    }

    /**
     * Ends a call.
     *
     * @throws SipException
     * 		if the SIP service fails to end the call
     */
    public void endCall() throws android.net.sip.SipException {
        if (android.net.sip.SipAudioCall.DBG)
            log("endCall: mSipSession" + mSipSession);

        synchronized(this) {
            stopCall(android.net.sip.SipAudioCall.RELEASE_SOCKET);
            mInCall = false;
            // perform the above local ops first and then network op
            if (mSipSession != null)
                mSipSession.endCall();

        }
    }

    /**
     * Puts a call on hold.  When succeeds, {@link Listener#onCallHeld} is
     * called. The attempt will be timed out if the call is not established
     * within {@code timeout} seconds and
     * {@link Listener#onError onError(SipAudioCall, SipErrorCode.TIME_OUT, String)}
     * will be called.
     *
     * @param timeout
     * 		the timeout value in seconds. Default value (defined by
     * 		SIP protocol) is used if {@code timeout} is zero or negative.
     * @see Listener#onError
     * @throws SipException
     * 		if the SIP service fails to hold the call
     */
    public void holdCall(int timeout) throws android.net.sip.SipException {
        if (android.net.sip.SipAudioCall.DBG)
            log((("holdCall: mSipSession" + mSipSession) + " timeout=") + timeout);

        synchronized(this) {
            if (mHold)
                return;

            if (mSipSession == null) {
                loge("holdCall:");
                throw new android.net.sip.SipException("Not in a call to hold call");
            }
            mSipSession.changeCall(createHoldOffer().encode(), timeout);
            mHold = true;
            setAudioGroupMode();
        }
    }

    /**
     * Answers a call. The attempt will be timed out if the call is not
     * established within {@code timeout} seconds and
     * {@link Listener#onError onError(SipAudioCall, SipErrorCode.TIME_OUT, String)}
     * will be called.
     *
     * @param timeout
     * 		the timeout value in seconds. Default value (defined by
     * 		SIP protocol) is used if {@code timeout} is zero or negative.
     * @see Listener#onError
     * @throws SipException
     * 		if the SIP service fails to answer the call
     */
    public void answerCall(int timeout) throws android.net.sip.SipException {
        if (android.net.sip.SipAudioCall.DBG)
            log((("answerCall: mSipSession" + mSipSession) + " timeout=") + timeout);

        synchronized(this) {
            if (mSipSession == null) {
                throw new android.net.sip.SipException("No call to answer");
            }
            try {
                mAudioStream = new android.net.rtp.AudioStream(java.net.InetAddress.getByName(getLocalIp()));
                mSipSession.answerCall(createAnswer(mPeerSd).encode(), timeout);
            } catch (java.io.IOException e) {
                loge("answerCall:", e);
                throw new android.net.sip.SipException("answerCall()", e);
            }
        }
    }

    /**
     * Continues a call that's on hold. When succeeds,
     * {@link Listener#onCallEstablished} is called. The attempt will be timed
     * out if the call is not established within {@code timeout} seconds and
     * {@link Listener#onError onError(SipAudioCall, SipErrorCode.TIME_OUT, String)}
     * will be called.
     *
     * @param timeout
     * 		the timeout value in seconds. Default value (defined by
     * 		SIP protocol) is used if {@code timeout} is zero or negative.
     * @see Listener#onError
     * @throws SipException
     * 		if the SIP service fails to unhold the call
     */
    public void continueCall(int timeout) throws android.net.sip.SipException {
        if (android.net.sip.SipAudioCall.DBG)
            log((("continueCall: mSipSession" + mSipSession) + " timeout=") + timeout);

        synchronized(this) {
            if (!mHold)
                return;

            mSipSession.changeCall(createContinueOffer().encode(), timeout);
            mHold = false;
            setAudioGroupMode();
        }
    }

    private android.net.sip.SimpleSessionDescription createOffer() {
        android.net.sip.SimpleSessionDescription offer = new android.net.sip.SimpleSessionDescription(mSessionId, getLocalIp());
        android.net.rtp.AudioCodec[] codecs = android.net.rtp.AudioCodec.getCodecs();
        android.net.sip.SimpleSessionDescription.Media media = offer.newMedia("audio", mAudioStream.getLocalPort(), 1, "RTP/AVP");
        for (android.net.rtp.AudioCodec codec : android.net.rtp.AudioCodec.getCodecs()) {
            media.setRtpPayload(codec.type, codec.rtpmap, codec.fmtp);
        }
        media.setRtpPayload(127, "telephone-event/8000", "0-15");
        if (android.net.sip.SipAudioCall.DBG)
            log("createOffer: offer=" + offer);

        return offer;
    }

    private android.net.sip.SimpleSessionDescription createAnswer(java.lang.String offerSd) {
        if (android.text.TextUtils.isEmpty(offerSd))
            return createOffer();

        android.net.sip.SimpleSessionDescription offer = new android.net.sip.SimpleSessionDescription(offerSd);
        android.net.sip.SimpleSessionDescription answer = new android.net.sip.SimpleSessionDescription(mSessionId, getLocalIp());
        android.net.rtp.AudioCodec codec = null;
        for (android.net.sip.SimpleSessionDescription.Media media : offer.getMedia()) {
            if ((((codec == null) && (media.getPort() > 0)) && "audio".equals(media.getType())) && "RTP/AVP".equals(media.getProtocol())) {
                // Find the first audio codec we supported.
                for (int type : media.getRtpPayloadTypes()) {
                    codec = android.net.rtp.AudioCodec.getCodec(type, media.getRtpmap(type), media.getFmtp(type));
                    if (codec != null) {
                        break;
                    }
                }
                if (codec != null) {
                    android.net.sip.SimpleSessionDescription.Media reply = answer.newMedia("audio", mAudioStream.getLocalPort(), 1, "RTP/AVP");
                    reply.setRtpPayload(codec.type, codec.rtpmap, codec.fmtp);
                    // Check if DTMF is supported in the same media.
                    for (int type : media.getRtpPayloadTypes()) {
                        java.lang.String rtpmap = media.getRtpmap(type);
                        if (((type != codec.type) && (rtpmap != null)) && rtpmap.startsWith("telephone-event")) {
                            reply.setRtpPayload(type, rtpmap, media.getFmtp(type));
                        }
                    }
                    // Handle recvonly and sendonly.
                    if (media.getAttribute("recvonly") != null) {
                        answer.setAttribute("sendonly", "");
                    } else
                        if (media.getAttribute("sendonly") != null) {
                            answer.setAttribute("recvonly", "");
                        } else
                            if (offer.getAttribute("recvonly") != null) {
                                answer.setAttribute("sendonly", "");
                            } else
                                if (offer.getAttribute("sendonly") != null) {
                                    answer.setAttribute("recvonly", "");
                                }



                    continue;
                }
            }
            // Reject the media.
            android.net.sip.SimpleSessionDescription.Media reply = answer.newMedia(media.getType(), 0, 1, media.getProtocol());
            for (java.lang.String format : media.getFormats()) {
                reply.setFormat(format, null);
            }
        }
        if (codec == null) {
            loge("createAnswer: no suitable codes");
            throw new java.lang.IllegalStateException("Reject SDP: no suitable codecs");
        }
        if (android.net.sip.SipAudioCall.DBG)
            log("createAnswer: answer=" + answer);

        return answer;
    }

    private android.net.sip.SimpleSessionDescription createHoldOffer() {
        android.net.sip.SimpleSessionDescription offer = createContinueOffer();
        offer.setAttribute("sendonly", "");
        if (android.net.sip.SipAudioCall.DBG)
            log("createHoldOffer: offer=" + offer);

        return offer;
    }

    private android.net.sip.SimpleSessionDescription createContinueOffer() {
        if (android.net.sip.SipAudioCall.DBG)
            log("createContinueOffer");

        android.net.sip.SimpleSessionDescription offer = new android.net.sip.SimpleSessionDescription(mSessionId, getLocalIp());
        android.net.sip.SimpleSessionDescription.Media media = offer.newMedia("audio", mAudioStream.getLocalPort(), 1, "RTP/AVP");
        android.net.rtp.AudioCodec codec = mAudioStream.getCodec();
        media.setRtpPayload(codec.type, codec.rtpmap, codec.fmtp);
        int dtmfType = mAudioStream.getDtmfType();
        if (dtmfType != (-1)) {
            media.setRtpPayload(dtmfType, "telephone-event/8000", "0-15");
        }
        return offer;
    }

    private void grabWifiHighPerfLock() {
        if (mWifiHighPerfLock == null) {
            if (android.net.sip.SipAudioCall.DBG)
                log("grabWifiHighPerfLock:");

            mWifiHighPerfLock = ((android.net.wifi.WifiManager) (mContext.getSystemService(android.content.Context.WIFI_SERVICE))).createWifiLock(android.net.wifi.WifiManager.WIFI_MODE_FULL_HIGH_PERF, android.net.sip.SipAudioCall.LOG_TAG);
            mWifiHighPerfLock.acquire();
        }
    }

    private void releaseWifiHighPerfLock() {
        if (mWifiHighPerfLock != null) {
            if (android.net.sip.SipAudioCall.DBG)
                log("releaseWifiHighPerfLock:");

            mWifiHighPerfLock.release();
            mWifiHighPerfLock = null;
        }
    }

    private boolean isWifiOn() {
        return mWm.getConnectionInfo().getBSSID() == null ? false : true;
    }

    /**
     * Toggles mute.
     */
    public void toggleMute() {
        synchronized(this) {
            mMuted = !mMuted;
            setAudioGroupMode();
        }
    }

    /**
     * Checks if the call is muted.
     *
     * @return true if the call is muted
     */
    public boolean isMuted() {
        synchronized(this) {
            return mMuted;
        }
    }

    /**
     * Puts the device to speaker mode.
     * <p class="note"><strong>Note:</strong> Requires the
     *   {@link android.Manifest.permission#MODIFY_AUDIO_SETTINGS} permission.</p>
     *
     * @param speakerMode
     * 		set true to enable speaker mode; false to disable
     */
    public void setSpeakerMode(boolean speakerMode) {
        synchronized(this) {
            ((android.media.AudioManager) (mContext.getSystemService(android.content.Context.AUDIO_SERVICE))).setSpeakerphoneOn(speakerMode);
            setAudioGroupMode();
        }
    }

    private boolean isSpeakerOn() {
        return ((android.media.AudioManager) (mContext.getSystemService(android.content.Context.AUDIO_SERVICE))).isSpeakerphoneOn();
    }

    /**
     * Sends a DTMF code. According to <a href="http://tools.ietf.org/html/rfc2833">RFC 2883</a>,
     * event 0--9 maps to decimal
     * value 0--9, '*' to 10, '#' to 11, event 'A'--'D' to 12--15, and event
     * flash to 16. Currently, event flash is not supported.
     *
     * @param code
     * 		the DTMF code to send. Value 0 to 15 (inclusive) are valid
     * 		inputs.
     */
    public void sendDtmf(int code) {
        sendDtmf(code, null);
    }

    /**
     * Sends a DTMF code. According to <a href="http://tools.ietf.org/html/rfc2833">RFC 2883</a>,
     * event 0--9 maps to decimal
     * value 0--9, '*' to 10, '#' to 11, event 'A'--'D' to 12--15, and event
     * flash to 16. Currently, event flash is not supported.
     *
     * @param code
     * 		the DTMF code to send. Value 0 to 15 (inclusive) are valid
     * 		inputs.
     * @param result
     * 		the result message to send when done
     */
    public void sendDtmf(int code, android.os.Message result) {
        synchronized(this) {
            android.net.rtp.AudioGroup audioGroup = getAudioGroup();
            if (((audioGroup != null) && (mSipSession != null)) && (android.net.sip.SipSession.State.IN_CALL == getState())) {
                if (android.net.sip.SipAudioCall.DBG)
                    log((("sendDtmf: code=" + code) + " result=") + result);

                audioGroup.sendDtmf(code);
            }
            if (result != null)
                result.sendToTarget();

        }
    }

    /**
     * Gets the {@link AudioStream} object used in this call. The object
     * represents the RTP stream that carries the audio data to and from the
     * peer. The object may not be created before the call is established. And
     * it is undefined after the call ends or the {@link #close} method is
     * called.
     *
     * @return the {@link AudioStream} object or null if the RTP stream has not
    yet been set up
     * @unknown 
     */
    public android.net.rtp.AudioStream getAudioStream() {
        synchronized(this) {
            return mAudioStream;
        }
    }

    /**
     * Gets the {@link AudioGroup} object which the {@link AudioStream} object
     * joins. The group object may not exist before the call is established.
     * Also, the {@code AudioStream} may change its group during a call (e.g.,
     * after the call is held/un-held). Finally, the {@code AudioGroup} object
     * returned by this method is undefined after the call ends or the
     * {@link #close} method is called. If a group object is set by
     * {@link #setAudioGroup(AudioGroup)}, then this method returns that object.
     *
     * @return the {@link AudioGroup} object or null if the RTP stream has not
    yet been set up
     * @see #getAudioStream
     * @unknown 
     */
    public android.net.rtp.AudioGroup getAudioGroup() {
        synchronized(this) {
            if (mAudioGroup != null)
                return mAudioGroup;

            return mAudioStream == null ? null : mAudioStream.getGroup();
        }
    }

    /**
     * Sets the {@link AudioGroup} object which the {@link AudioStream} object
     * joins. If {@code audioGroup} is null, then the {@code AudioGroup} object
     * will be dynamically created when needed. Note that the mode of the
     * {@code AudioGroup} is not changed according to the audio settings (i.e.,
     * hold, mute, speaker phone) of this object. This is mainly used to merge
     * multiple {@code SipAudioCall} objects to form a conference call. The
     * settings of the first object (that merges others) override others'.
     *
     * @see #getAudioStream
     * @unknown 
     */
    public void setAudioGroup(android.net.rtp.AudioGroup group) {
        synchronized(this) {
            if (android.net.sip.SipAudioCall.DBG)
                log("setAudioGroup: group=" + group);

            if ((mAudioStream != null) && (mAudioStream.getGroup() != null)) {
                mAudioStream.join(group);
            }
            mAudioGroup = group;
        }
    }

    /**
     * Starts the audio for the established call. This method should be called
     * after {@link Listener#onCallEstablished} is called.
     * <p class="note"><strong>Note:</strong> Requires the
     *   {@link android.Manifest.permission#RECORD_AUDIO},
     *   {@link android.Manifest.permission#ACCESS_WIFI_STATE} and
     *   {@link android.Manifest.permission#WAKE_LOCK} permissions.</p>
     */
    public void startAudio() {
        try {
            startAudioInternal();
        } catch (java.net.UnknownHostException e) {
            onError(android.net.sip.SipErrorCode.PEER_NOT_REACHABLE, e.getMessage());
        } catch (java.lang.Throwable e) {
            onError(android.net.sip.SipErrorCode.CLIENT_ERROR, e.getMessage());
        }
    }

    private synchronized void startAudioInternal() throws java.net.UnknownHostException {
        if (android.net.sip.SipAudioCall.DBG)
            loge("startAudioInternal: mPeerSd=" + mPeerSd);

        if (mPeerSd == null) {
            throw new java.lang.IllegalStateException("mPeerSd = null");
        }
        stopCall(android.net.sip.SipAudioCall.DONT_RELEASE_SOCKET);
        mInCall = true;
        // Run exact the same logic in createAnswer() to setup mAudioStream.
        android.net.sip.SimpleSessionDescription offer = new android.net.sip.SimpleSessionDescription(mPeerSd);
        android.net.rtp.AudioStream stream = mAudioStream;
        android.net.rtp.AudioCodec codec = null;
        for (android.net.sip.SimpleSessionDescription.Media media : offer.getMedia()) {
            if ((((codec == null) && (media.getPort() > 0)) && "audio".equals(media.getType())) && "RTP/AVP".equals(media.getProtocol())) {
                // Find the first audio codec we supported.
                for (int type : media.getRtpPayloadTypes()) {
                    codec = android.net.rtp.AudioCodec.getCodec(type, media.getRtpmap(type), media.getFmtp(type));
                    if (codec != null) {
                        break;
                    }
                }
                if (codec != null) {
                    // Associate with the remote host.
                    java.lang.String address = media.getAddress();
                    if (address == null) {
                        address = offer.getAddress();
                    }
                    stream.associate(java.net.InetAddress.getByName(address), media.getPort());
                    stream.setDtmfType(-1);
                    stream.setCodec(codec);
                    // Check if DTMF is supported in the same media.
                    for (int type : media.getRtpPayloadTypes()) {
                        java.lang.String rtpmap = media.getRtpmap(type);
                        if (((type != codec.type) && (rtpmap != null)) && rtpmap.startsWith("telephone-event")) {
                            stream.setDtmfType(type);
                        }
                    }
                    // Handle recvonly and sendonly.
                    if (mHold) {
                        stream.setMode(android.net.rtp.RtpStream.MODE_NORMAL);
                    } else
                        if (media.getAttribute("recvonly") != null) {
                            stream.setMode(android.net.rtp.RtpStream.MODE_SEND_ONLY);
                        } else
                            if (media.getAttribute("sendonly") != null) {
                                stream.setMode(android.net.rtp.RtpStream.MODE_RECEIVE_ONLY);
                            } else
                                if (offer.getAttribute("recvonly") != null) {
                                    stream.setMode(android.net.rtp.RtpStream.MODE_SEND_ONLY);
                                } else
                                    if (offer.getAttribute("sendonly") != null) {
                                        stream.setMode(android.net.rtp.RtpStream.MODE_RECEIVE_ONLY);
                                    } else {
                                        stream.setMode(android.net.rtp.RtpStream.MODE_NORMAL);
                                    }




                    break;
                }
            }
        }
        if (codec == null) {
            throw new java.lang.IllegalStateException("Reject SDP: no suitable codecs");
        }
        if (isWifiOn())
            grabWifiHighPerfLock();

        // AudioGroup logic:
        android.net.rtp.AudioGroup audioGroup = getAudioGroup();
        if (mHold) {
            // don't create an AudioGroup here; doing so will fail if
            // there's another AudioGroup out there that's active
        } else {
            if (audioGroup == null)
                audioGroup = new android.net.rtp.AudioGroup();

            stream.join(audioGroup);
        }
        setAudioGroupMode();
    }

    // set audio group mode based on current audio configuration
    private void setAudioGroupMode() {
        android.net.rtp.AudioGroup audioGroup = getAudioGroup();
        if (android.net.sip.SipAudioCall.DBG)
            log("setAudioGroupMode: audioGroup=" + audioGroup);

        if (audioGroup != null) {
            if (mHold) {
                audioGroup.setMode(android.net.rtp.AudioGroup.MODE_ON_HOLD);
            } else
                if (mMuted) {
                    audioGroup.setMode(android.net.rtp.AudioGroup.MODE_MUTED);
                } else
                    if (isSpeakerOn()) {
                        audioGroup.setMode(android.net.rtp.AudioGroup.MODE_ECHO_SUPPRESSION);
                    } else {
                        audioGroup.setMode(android.net.rtp.AudioGroup.MODE_NORMAL);
                    }


        }
    }

    private void stopCall(boolean releaseSocket) {
        if (android.net.sip.SipAudioCall.DBG)
            log("stopCall: releaseSocket=" + releaseSocket);

        releaseWifiHighPerfLock();
        if (mAudioStream != null) {
            mAudioStream.join(null);
            if (releaseSocket) {
                mAudioStream.release();
                mAudioStream = null;
            }
        }
    }

    private java.lang.String getLocalIp() {
        return mSipSession.getLocalIp();
    }

    private void throwSipException(java.lang.Throwable throwable) throws android.net.sip.SipException {
        if (throwable instanceof android.net.sip.SipException) {
            throw ((android.net.sip.SipException) (throwable));
        } else {
            throw new android.net.sip.SipException("", throwable);
        }
    }

    private void log(java.lang.String s) {
        android.telephony.Rlog.d(android.net.sip.SipAudioCall.LOG_TAG, s);
    }

    private void loge(java.lang.String s) {
        android.telephony.Rlog.e(android.net.sip.SipAudioCall.LOG_TAG, s);
    }

    private void loge(java.lang.String s, java.lang.Throwable t) {
        android.telephony.Rlog.e(android.net.sip.SipAudioCall.LOG_TAG, s, t);
    }
}

