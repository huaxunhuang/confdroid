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
 * Provides APIs for SIP tasks, such as initiating SIP connections, and provides access to related
 * SIP services. This class is the starting point for any SIP actions. You can acquire an instance
 * of it with {@link #newInstance newInstance()}.</p>
 * <p>The APIs in this class allows you to:</p>
 * <ul>
 * <li>Create a {@link SipSession} to get ready for making calls or listen for incoming calls. See
 * {@link #createSipSession createSipSession()} and {@link #getSessionFor getSessionFor()}.</li>
 * <li>Initiate and receive generic SIP calls or audio-only SIP calls. Generic SIP calls may
 * be video, audio, or other, and are initiated with {@link #open open()}. Audio-only SIP calls
 * should be handled with a {@link SipAudioCall}, which you can acquire with {@link #makeAudioCall makeAudioCall()} and {@link #takeAudioCall takeAudioCall()}.</li>
 * <li>Register and unregister with a SIP service provider, with
 *      {@link #register register()} and {@link #unregister unregister()}.</li>
 * <li>Verify session connectivity, with {@link #isOpened isOpened()} and
 *      {@link #isRegistered isRegistered()}.</li>
 * </ul>
 * <p class="note"><strong>Note:</strong> Not all Android-powered devices support VOIP calls using
 * SIP. You should always call {@link android.net.sip.SipManager#isVoipSupported
 * isVoipSupported()} to verify that the device supports VOIP calling and {@link android.net.sip.SipManager#isApiSupported isApiSupported()} to verify that the device supports
 * the SIP APIs. Your application must also request the {@link android.Manifest.permission#INTERNET} and {@link android.Manifest.permission#USE_SIP}
 * permissions.</p>
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about using SIP, read the
 * <a href="{@docRoot }guide/topics/network/sip.html">Session Initiation Protocol</a>
 * developer guide.</p>
 * </div>
 */
public class SipManager {
    /**
     * The result code to be sent back with the incoming call
     * {@link PendingIntent}.
     *
     * @see #open(SipProfile, PendingIntent, SipRegistrationListener)
     */
    public static final int INCOMING_CALL_RESULT_CODE = 101;

    /**
     * Key to retrieve the call ID from an incoming call intent.
     *
     * @see #open(SipProfile, PendingIntent, SipRegistrationListener)
     */
    public static final java.lang.String EXTRA_CALL_ID = "android:sipCallID";

    /**
     * Key to retrieve the offered session description from an incoming call
     * intent.
     *
     * @see #open(SipProfile, PendingIntent, SipRegistrationListener)
     */
    public static final java.lang.String EXTRA_OFFER_SD = "android:sipOfferSD";

    /**
     * Action to broadcast when SipService is up.
     * Internal use only.
     *
     * @unknown 
     */
    public static final java.lang.String ACTION_SIP_SERVICE_UP = "android.net.sip.SIP_SERVICE_UP";

    /**
     * Action string for the incoming call intent for the Phone app.
     * Internal use only.
     *
     * @unknown 
     */
    public static final java.lang.String ACTION_SIP_INCOMING_CALL = "com.android.phone.SIP_INCOMING_CALL";

    /**
     * Action string for the add-phone intent.
     * Internal use only.
     *
     * @unknown 
     */
    public static final java.lang.String ACTION_SIP_ADD_PHONE = "com.android.phone.SIP_ADD_PHONE";

    /**
     * Action string for the remove-phone intent.
     * Internal use only.
     *
     * @unknown 
     */
    public static final java.lang.String ACTION_SIP_REMOVE_PHONE = "com.android.phone.SIP_REMOVE_PHONE";

    /**
     * Action string for the SIP call option configuration changed intent.
     * This is used to communicate  change to the SIP call option, triggering re-registration of
     * the SIP phone accounts.
     * Internal use only.
     *
     * @unknown 
     */
    public static final java.lang.String ACTION_SIP_CALL_OPTION_CHANGED = "com.android.phone.SIP_CALL_OPTION_CHANGED";

    /**
     * Part of the ACTION_SIP_ADD_PHONE and ACTION_SIP_REMOVE_PHONE intents.
     * Internal use only.
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_LOCAL_URI = "android:localSipUri";

    private static final java.lang.String TAG = "SipManager";

    private android.net.sip.ISipService mSipService;

    private android.content.Context mContext;

    /**
     * Creates a manager instance. Returns null if SIP API is not supported.
     *
     * @param context
     * 		application context for creating the manager object
     * @return the manager instance or null if SIP API is not supported
     */
    public static android.net.sip.SipManager newInstance(android.content.Context context) {
        return android.net.sip.SipManager.isApiSupported(context) ? new android.net.sip.SipManager(context) : null;
    }

    /**
     * Returns true if the SIP API is supported by the system.
     */
    public static boolean isApiSupported(android.content.Context context) {
        return context.getPackageManager().hasSystemFeature(android.content.pm.PackageManager.FEATURE_SIP);
    }

    /**
     * Returns true if the system supports SIP-based VOIP API.
     */
    public static boolean isVoipSupported(android.content.Context context) {
        return context.getPackageManager().hasSystemFeature(android.content.pm.PackageManager.FEATURE_SIP_VOIP) && android.net.sip.SipManager.isApiSupported(context);
    }

    /**
     * Returns true if SIP is only available on WIFI.
     */
    public static boolean isSipWifiOnly(android.content.Context context) {
        return context.getResources().getBoolean(com.android.internal.R.bool.config_sip_wifi_only);
    }

    private SipManager(android.content.Context context) {
        mContext = context;
        createSipService();
    }

    private void createSipService() {
        if (mSipService == null) {
            android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.SIP_SERVICE);
            mSipService = ISipService.Stub.asInterface(b);
        }
    }

    private void checkSipServiceConnection() throws android.net.sip.SipException {
        createSipService();
        if (mSipService == null) {
            throw new android.net.sip.SipException("SipService is dead and is restarting...", new java.lang.Exception());
        }
    }

    /**
     * Opens the profile for making generic SIP calls. The caller may make subsequent calls
     * through {@link #makeAudioCall}. If one also wants to receive calls on the
     * profile, use
     * {@link #open(SipProfile, PendingIntent, SipRegistrationListener)}
     * instead.
     *
     * @param localProfile
     * 		the SIP profile to make calls from
     * @throws SipException
     * 		if the profile contains incorrect settings or
     * 		calling the SIP service results in an error
     */
    public void open(android.net.sip.SipProfile localProfile) throws android.net.sip.SipException {
        try {
            checkSipServiceConnection();
            mSipService.open(localProfile, mContext.getOpPackageName());
        } catch (android.os.RemoteException e) {
            throw new android.net.sip.SipException("open()", e);
        }
    }

    /**
     * Opens the profile for making calls and/or receiving generic SIP calls. The caller may
     * make subsequent calls through {@link #makeAudioCall}. If the
     * auto-registration option is enabled in the profile, the SIP service
     * will register the profile to the corresponding SIP provider periodically
     * in order to receive calls from the provider. When the SIP service
     * receives a new call, it will send out an intent with the provided action
     * string. The intent contains a call ID extra and an offer session
     * description string extra. Use {@link #getCallId} and
     * {@link #getOfferSessionDescription} to retrieve those extras.
     *
     * @param localProfile
     * 		the SIP profile to receive incoming calls for
     * @param incomingCallPendingIntent
     * 		When an incoming call is received, the
     * 		SIP service will call
     * 		{@link PendingIntent#send(Context, int, Intent)} to send back the
     * 		intent to the caller with {@link #INCOMING_CALL_RESULT_CODE} as the
     * 		result code and the intent to fill in the call ID and session
     * 		description information. It cannot be null.
     * @param listener
     * 		to listen to registration events; can be null
     * @see #getCallId
     * @see #getOfferSessionDescription
     * @see #takeAudioCall
     * @throws NullPointerException
     * 		if {@code incomingCallPendingIntent} is null
     * @throws SipException
     * 		if the profile contains incorrect settings or
     * 		calling the SIP service results in an error
     * @see #isIncomingCallIntent
     * @see #getCallId
     * @see #getOfferSessionDescription
     */
    public void open(android.net.sip.SipProfile localProfile, android.app.PendingIntent incomingCallPendingIntent, android.net.sip.SipRegistrationListener listener) throws android.net.sip.SipException {
        if (incomingCallPendingIntent == null) {
            throw new java.lang.NullPointerException("incomingCallPendingIntent cannot be null");
        }
        try {
            checkSipServiceConnection();
            mSipService.open3(localProfile, incomingCallPendingIntent, android.net.sip.SipManager.createRelay(listener, localProfile.getUriString()), mContext.getOpPackageName());
        } catch (android.os.RemoteException e) {
            throw new android.net.sip.SipException("open()", e);
        }
    }

    /**
     * Sets the listener to listen to registration events. No effect if the
     * profile has not been opened to receive calls (see
     * {@link #open(SipProfile, PendingIntent, SipRegistrationListener)}).
     *
     * @param localProfileUri
     * 		the URI of the profile
     * @param listener
     * 		to listen to registration events; can be null
     * @throws SipException
     * 		if calling the SIP service results in an error
     */
    public void setRegistrationListener(java.lang.String localProfileUri, android.net.sip.SipRegistrationListener listener) throws android.net.sip.SipException {
        try {
            checkSipServiceConnection();
            mSipService.setRegistrationListener(localProfileUri, android.net.sip.SipManager.createRelay(listener, localProfileUri), mContext.getOpPackageName());
        } catch (android.os.RemoteException e) {
            throw new android.net.sip.SipException("setRegistrationListener()", e);
        }
    }

    /**
     * Closes the specified profile to not make/receive calls. All the resources
     * that were allocated to the profile are also released.
     *
     * @param localProfileUri
     * 		the URI of the profile to close
     * @throws SipException
     * 		if calling the SIP service results in an error
     */
    public void close(java.lang.String localProfileUri) throws android.net.sip.SipException {
        try {
            checkSipServiceConnection();
            mSipService.close(localProfileUri, mContext.getOpPackageName());
        } catch (android.os.RemoteException e) {
            throw new android.net.sip.SipException("close()", e);
        }
    }

    /**
     * Checks if the specified profile is opened in the SIP service for
     * making and/or receiving calls.
     *
     * @param localProfileUri
     * 		the URI of the profile in question
     * @return true if the profile is enabled to receive calls
     * @throws SipException
     * 		if calling the SIP service results in an error
     */
    public boolean isOpened(java.lang.String localProfileUri) throws android.net.sip.SipException {
        try {
            checkSipServiceConnection();
            return mSipService.isOpened(localProfileUri, mContext.getOpPackageName());
        } catch (android.os.RemoteException e) {
            throw new android.net.sip.SipException("isOpened()", e);
        }
    }

    /**
     * Checks if the SIP service has successfully registered the profile to the
     * SIP provider (specified in the profile) for receiving calls. Returning
     * true from this method also implies the profile is opened
     * ({@link #isOpened}).
     *
     * @param localProfileUri
     * 		the URI of the profile in question
     * @return true if the profile is registered to the SIP provider; false if
    the profile has not been opened in the SIP service or the SIP
    service has not yet successfully registered the profile to the SIP
    provider
     * @throws SipException
     * 		if calling the SIP service results in an error
     */
    public boolean isRegistered(java.lang.String localProfileUri) throws android.net.sip.SipException {
        try {
            checkSipServiceConnection();
            return mSipService.isRegistered(localProfileUri, mContext.getOpPackageName());
        } catch (android.os.RemoteException e) {
            throw new android.net.sip.SipException("isRegistered()", e);
        }
    }

    /**
     * Creates a {@link SipAudioCall} to make a call. The attempt will be timed
     * out if the call is not established within {@code timeout} seconds and
     * {@link SipAudioCall.Listener#onError onError(SipAudioCall, SipErrorCode.TIME_OUT, String)}
     * will be called.
     *
     * @param localProfile
     * 		the SIP profile to make the call from
     * @param peerProfile
     * 		the SIP profile to make the call to
     * @param listener
     * 		to listen to the call events from {@link SipAudioCall};
     * 		can be null
     * @param timeout
     * 		the timeout value in seconds. Default value (defined by
     * 		SIP protocol) is used if {@code timeout} is zero or negative.
     * @return a {@link SipAudioCall} object
     * @throws SipException
     * 		if calling the SIP service results in an error or
     * 		VOIP API is not supported by the device
     * @see SipAudioCall.Listener#onError
     * @see #isVoipSupported
     */
    public android.net.sip.SipAudioCall makeAudioCall(android.net.sip.SipProfile localProfile, android.net.sip.SipProfile peerProfile, android.net.sip.SipAudioCall.Listener listener, int timeout) throws android.net.sip.SipException {
        if (!android.net.sip.SipManager.isVoipSupported(mContext)) {
            throw new android.net.sip.SipException("VOIP API is not supported");
        }
        android.net.sip.SipAudioCall call = new android.net.sip.SipAudioCall(mContext, localProfile);
        call.setListener(listener);
        android.net.sip.SipSession s = createSipSession(localProfile, null);
        call.makeCall(peerProfile, s, timeout);
        return call;
    }

    /**
     * Creates a {@link SipAudioCall} to make an audio call. The attempt will be
     * timed out if the call is not established within {@code timeout} seconds
     * and
     * {@link SipAudioCall.Listener#onError onError(SipAudioCall, SipErrorCode.TIME_OUT, String)}
     * will be called.
     *
     * @param localProfileUri
     * 		URI of the SIP profile to make the call from
     * @param peerProfileUri
     * 		URI of the SIP profile to make the call to
     * @param listener
     * 		to listen to the call events from {@link SipAudioCall};
     * 		can be null
     * @param timeout
     * 		the timeout value in seconds. Default value (defined by
     * 		SIP protocol) is used if {@code timeout} is zero or negative.
     * @return a {@link SipAudioCall} object
     * @throws SipException
     * 		if calling the SIP service results in an error or
     * 		VOIP API is not supported by the device
     * @see SipAudioCall.Listener#onError
     * @see #isVoipSupported
     */
    public android.net.sip.SipAudioCall makeAudioCall(java.lang.String localProfileUri, java.lang.String peerProfileUri, android.net.sip.SipAudioCall.Listener listener, int timeout) throws android.net.sip.SipException {
        if (!android.net.sip.SipManager.isVoipSupported(mContext)) {
            throw new android.net.sip.SipException("VOIP API is not supported");
        }
        try {
            return makeAudioCall(new android.net.sip.SipProfile.Builder(localProfileUri).build(), new android.net.sip.SipProfile.Builder(peerProfileUri).build(), listener, timeout);
        } catch (java.text.ParseException e) {
            throw new android.net.sip.SipException("build SipProfile", e);
        }
    }

    /**
     * Creates a {@link SipAudioCall} to take an incoming call. Before the call
     * is returned, the listener will receive a
     * {@link SipAudioCall.Listener#onRinging}
     * callback.
     *
     * @param incomingCallIntent
     * 		the incoming call broadcast intent
     * @param listener
     * 		to listen to the call events from {@link SipAudioCall};
     * 		can be null
     * @return a {@link SipAudioCall} object
     * @throws SipException
     * 		if calling the SIP service results in an error
     */
    public android.net.sip.SipAudioCall takeAudioCall(android.content.Intent incomingCallIntent, android.net.sip.SipAudioCall.Listener listener) throws android.net.sip.SipException {
        if (incomingCallIntent == null) {
            throw new android.net.sip.SipException("Cannot retrieve session with null intent");
        }
        java.lang.String callId = android.net.sip.SipManager.getCallId(incomingCallIntent);
        if (callId == null) {
            throw new android.net.sip.SipException("Call ID missing in incoming call intent");
        }
        java.lang.String offerSd = android.net.sip.SipManager.getOfferSessionDescription(incomingCallIntent);
        if (offerSd == null) {
            throw new android.net.sip.SipException("Session description missing in incoming " + "call intent");
        }
        try {
            checkSipServiceConnection();
            android.net.sip.ISipSession session = mSipService.getPendingSession(callId, mContext.getOpPackageName());
            if (session == null) {
                throw new android.net.sip.SipException("No pending session for the call");
            }
            android.net.sip.SipAudioCall call = new android.net.sip.SipAudioCall(mContext, session.getLocalProfile());
            call.attachCall(new android.net.sip.SipSession(session), offerSd);
            call.setListener(listener);
            return call;
        } catch (java.lang.Throwable t) {
            throw new android.net.sip.SipException("takeAudioCall()", t);
        }
    }

    /**
     * Checks if the intent is an incoming call broadcast intent.
     *
     * @param intent
     * 		the intent in question
     * @return true if the intent is an incoming call broadcast intent
     */
    public static boolean isIncomingCallIntent(android.content.Intent intent) {
        if (intent == null)
            return false;

        java.lang.String callId = android.net.sip.SipManager.getCallId(intent);
        java.lang.String offerSd = android.net.sip.SipManager.getOfferSessionDescription(intent);
        return (callId != null) && (offerSd != null);
    }

    /**
     * Gets the call ID from the specified incoming call broadcast intent.
     *
     * @param incomingCallIntent
     * 		the incoming call broadcast intent
     * @return the call ID or null if the intent does not contain it
     */
    public static java.lang.String getCallId(android.content.Intent incomingCallIntent) {
        return incomingCallIntent.getStringExtra(android.net.sip.SipManager.EXTRA_CALL_ID);
    }

    /**
     * Gets the offer session description from the specified incoming call
     * broadcast intent.
     *
     * @param incomingCallIntent
     * 		the incoming call broadcast intent
     * @return the offer session description or null if the intent does not
    have it
     */
    public static java.lang.String getOfferSessionDescription(android.content.Intent incomingCallIntent) {
        return incomingCallIntent.getStringExtra(android.net.sip.SipManager.EXTRA_OFFER_SD);
    }

    /**
     * Creates an incoming call broadcast intent.
     *
     * @param callId
     * 		the call ID of the incoming call
     * @param sessionDescription
     * 		the session description of the incoming call
     * @return the incoming call intent
     * @unknown 
     */
    public static android.content.Intent createIncomingCallBroadcast(java.lang.String callId, java.lang.String sessionDescription) {
        android.content.Intent intent = new android.content.Intent();
        intent.putExtra(android.net.sip.SipManager.EXTRA_CALL_ID, callId);
        intent.putExtra(android.net.sip.SipManager.EXTRA_OFFER_SD, sessionDescription);
        return intent;
    }

    /**
     * Manually registers the profile to the corresponding SIP provider for
     * receiving calls.
     * {@link #open(SipProfile, PendingIntent, SipRegistrationListener)} is
     * still needed to be called at least once in order for the SIP service to
     * notify the caller with the {@link android.app.PendingIntent} when an incoming call is
     * received.
     *
     * @param localProfile
     * 		the SIP profile to register with
     * @param expiryTime
     * 		registration expiration time (in seconds)
     * @param listener
     * 		to listen to the registration events
     * @throws SipException
     * 		if calling the SIP service results in an error
     */
    public void register(android.net.sip.SipProfile localProfile, int expiryTime, android.net.sip.SipRegistrationListener listener) throws android.net.sip.SipException {
        try {
            checkSipServiceConnection();
            android.net.sip.ISipSession session = mSipService.createSession(localProfile, android.net.sip.SipManager.createRelay(listener, localProfile.getUriString()), mContext.getOpPackageName());
            if (session == null) {
                throw new android.net.sip.SipException("SipService.createSession() returns null");
            }
            session.register(expiryTime);
        } catch (android.os.RemoteException e) {
            throw new android.net.sip.SipException("register()", e);
        }
    }

    /**
     * Manually unregisters the profile from the corresponding SIP provider for
     * stop receiving further calls. This may interference with the auto
     * registration process in the SIP service if the auto-registration option
     * in the profile is enabled.
     *
     * @param localProfile
     * 		the SIP profile to register with
     * @param listener
     * 		to listen to the registration events
     * @throws SipException
     * 		if calling the SIP service results in an error
     */
    public void unregister(android.net.sip.SipProfile localProfile, android.net.sip.SipRegistrationListener listener) throws android.net.sip.SipException {
        try {
            checkSipServiceConnection();
            android.net.sip.ISipSession session = mSipService.createSession(localProfile, android.net.sip.SipManager.createRelay(listener, localProfile.getUriString()), mContext.getOpPackageName());
            if (session == null) {
                throw new android.net.sip.SipException("SipService.createSession() returns null");
            }
            session.unregister();
        } catch (android.os.RemoteException e) {
            throw new android.net.sip.SipException("unregister()", e);
        }
    }

    /**
     * Gets the {@link SipSession} that handles the incoming call. For audio
     * calls, consider to use {@link SipAudioCall} to handle the incoming call.
     * See {@link #takeAudioCall}. Note that the method may be called only once
     * for the same intent. For subsequent calls on the same intent, the method
     * returns null.
     *
     * @param incomingCallIntent
     * 		the incoming call broadcast intent
     * @return the session object that handles the incoming call
     */
    public android.net.sip.SipSession getSessionFor(android.content.Intent incomingCallIntent) throws android.net.sip.SipException {
        try {
            checkSipServiceConnection();
            java.lang.String callId = android.net.sip.SipManager.getCallId(incomingCallIntent);
            android.net.sip.ISipSession s = mSipService.getPendingSession(callId, mContext.getOpPackageName());
            return s == null ? null : new android.net.sip.SipSession(s);
        } catch (android.os.RemoteException e) {
            throw new android.net.sip.SipException("getSessionFor()", e);
        }
    }

    private static android.net.sip.ISipSessionListener createRelay(android.net.sip.SipRegistrationListener listener, java.lang.String uri) {
        return listener == null ? null : new android.net.sip.SipManager.ListenerRelay(listener, uri);
    }

    /**
     * Creates a {@link SipSession} with the specified profile. Use other
     * methods, if applicable, instead of interacting with {@link SipSession}
     * directly.
     *
     * @param localProfile
     * 		the SIP profile the session is associated with
     * @param listener
     * 		to listen to SIP session events
     */
    public android.net.sip.SipSession createSipSession(android.net.sip.SipProfile localProfile, android.net.sip.SipSession.Listener listener) throws android.net.sip.SipException {
        try {
            checkSipServiceConnection();
            android.net.sip.ISipSession s = mSipService.createSession(localProfile, null, mContext.getOpPackageName());
            if (s == null) {
                throw new android.net.sip.SipException("Failed to create SipSession; network unavailable?");
            }
            return new android.net.sip.SipSession(s, listener);
        } catch (android.os.RemoteException e) {
            throw new android.net.sip.SipException("createSipSession()", e);
        }
    }

    /**
     * Gets the list of profiles hosted by the SIP service. The user information
     * (username, password and display name) are crossed out.
     *
     * @unknown 
     */
    public android.net.sip.SipProfile[] getListOfProfiles() throws android.net.sip.SipException {
        try {
            checkSipServiceConnection();
            return mSipService.getListOfProfiles(mContext.getOpPackageName());
        } catch (android.os.RemoteException e) {
            return new android.net.sip.SipProfile[0];
        }
    }

    private static class ListenerRelay extends android.net.sip.SipSessionAdapter {
        private android.net.sip.SipRegistrationListener mListener;

        private java.lang.String mUri;

        // listener must not be null
        public ListenerRelay(android.net.sip.SipRegistrationListener listener, java.lang.String uri) {
            mListener = listener;
            mUri = uri;
        }

        private java.lang.String getUri(android.net.sip.ISipSession session) {
            try {
                return session == null ? mUri : session.getLocalProfile().getUriString();
            } catch (java.lang.Throwable e) {
                // SipService died? SIP stack died?
                android.telephony.Rlog.e(android.net.sip.SipManager.TAG, "getUri(): ", e);
                return null;
            }
        }

        @java.lang.Override
        public void onRegistering(android.net.sip.ISipSession session) {
            mListener.onRegistering(getUri(session));
        }

        @java.lang.Override
        public void onRegistrationDone(android.net.sip.ISipSession session, int duration) {
            long expiryTime = duration;
            if (duration > 0)
                expiryTime += java.lang.System.currentTimeMillis();

            mListener.onRegistrationDone(getUri(session), expiryTime);
        }

        @java.lang.Override
        public void onRegistrationFailed(android.net.sip.ISipSession session, int errorCode, java.lang.String message) {
            mListener.onRegistrationFailed(getUri(session), errorCode, message);
        }

        @java.lang.Override
        public void onRegistrationTimeout(android.net.sip.ISipSession session) {
            mListener.onRegistrationFailed(getUri(session), android.net.sip.SipErrorCode.TIME_OUT, "registration timed out");
        }
    }
}

