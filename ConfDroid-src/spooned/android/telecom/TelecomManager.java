/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.telecom;


/**
 * Provides access to information about active calls and registration/call-management functionality.
 * Apps can use methods in this class to determine the current call state.
 * <p>
 * Apps do not instantiate this class directly; instead, they retrieve a reference to an instance
 * through {@link Context#getSystemService Context.getSystemService(Context.TELECOM_SERVICE)}.
 * <p>
 * Note that access to some telecom information is permission-protected. Your app cannot access the
 * protected information or gain access to protected functionality unless it has the appropriate
 * permissions declared in its manifest file. Where permissions apply, they are noted in the method
 * descriptions.
 */
public class TelecomManager {
    /**
     * Activity action: Starts the UI for handing an incoming call. This intent starts the in-call
     * UI by notifying the Telecom system that an incoming call exists for a specific call service
     * (see {@link android.telecom.ConnectionService}). Telecom reads the Intent extras to find
     * and bind to the appropriate {@link android.telecom.ConnectionService} which Telecom will
     * ultimately use to control and get information about the call.
     * <p>
     * Input: get*Extra field {@link #EXTRA_PHONE_ACCOUNT_HANDLE} contains the component name of the
     * {@link android.telecom.ConnectionService} that Telecom should bind to. Telecom will then
     * ask the connection service for more information about the call prior to showing any UI.
     */
    public static final java.lang.String ACTION_INCOMING_CALL = "android.telecom.action.INCOMING_CALL";

    /**
     * Similar to {@link #ACTION_INCOMING_CALL}, but is used only by Telephony to add a new
     * sim-initiated MO call for carrier testing.
     *
     * @unknown 
     */
    public static final java.lang.String ACTION_NEW_UNKNOWN_CALL = "android.telecom.action.NEW_UNKNOWN_CALL";

    /**
     * An {@link android.content.Intent} action sent by the telecom framework to start a
     * configuration dialog for a registered {@link PhoneAccount}. There is no default dialog
     * and each app that registers a {@link PhoneAccount} should provide one if desired.
     * <p>
     * A user can access the list of enabled {@link android.telecom.PhoneAccount}s through the Phone
     * app's settings menu. For each entry, the settings app will add a click action. When
     * triggered, the click-action will start this intent along with the extra
     * {@link #EXTRA_PHONE_ACCOUNT_HANDLE} to indicate the {@link PhoneAccount} to configure. If the
     * {@link PhoneAccount} package does not register an {@link android.app.Activity} for this
     * intent, then it will not be sent.
     */
    public static final java.lang.String ACTION_CONFIGURE_PHONE_ACCOUNT = "android.telecom.action.CONFIGURE_PHONE_ACCOUNT";

    /**
     * The {@link android.content.Intent} action used to show the call accessibility settings page.
     */
    public static final java.lang.String ACTION_SHOW_CALL_ACCESSIBILITY_SETTINGS = "android.telecom.action.SHOW_CALL_ACCESSIBILITY_SETTINGS";

    /**
     * The {@link android.content.Intent} action used to show the call settings page.
     */
    public static final java.lang.String ACTION_SHOW_CALL_SETTINGS = "android.telecom.action.SHOW_CALL_SETTINGS";

    /**
     * The {@link android.content.Intent} action used to show the respond via SMS settings page.
     */
    public static final java.lang.String ACTION_SHOW_RESPOND_VIA_SMS_SETTINGS = "android.telecom.action.SHOW_RESPOND_VIA_SMS_SETTINGS";

    /**
     * The {@link android.content.Intent} action used to show the settings page used to configure
     * {@link PhoneAccount} preferences.
     */
    public static final java.lang.String ACTION_CHANGE_PHONE_ACCOUNTS = "android.telecom.action.CHANGE_PHONE_ACCOUNTS";

    /**
     * The {@link android.content.Intent} action used indicate that a new phone account was
     * just registered.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final java.lang.String ACTION_PHONE_ACCOUNT_REGISTERED = "android.telecom.action.PHONE_ACCOUNT_REGISTERED";

    /**
     * The {@link android.content.Intent} action used indicate that a phone account was
     * just unregistered.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final java.lang.String ACTION_PHONE_ACCOUNT_UNREGISTERED = "android.telecom.action.PHONE_ACCOUNT_UNREGISTERED";

    /**
     * Activity action: Shows a dialog asking the user whether or not they want to replace the
     * current default Dialer with the one specified in
     * {@link #EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME}.
     *
     * Usage example:
     * <pre>
     * Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
     * intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
     *         getActivity().getPackageName());
     * startActivity(intent);
     * </pre>
     */
    public static final java.lang.String ACTION_CHANGE_DEFAULT_DIALER = "android.telecom.action.CHANGE_DEFAULT_DIALER";

    /**
     * Broadcast intent action indicating that the current default dialer has changed.
     * The string extra {@link #EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME} will contain the
     * name of the package that the default dialer was changed to.
     *
     * @see #EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME
     */
    public static final java.lang.String ACTION_DEFAULT_DIALER_CHANGED = "android.telecom.action.DEFAULT_DIALER_CHANGED";

    /**
     * Extra value used to provide the package name for {@link #ACTION_CHANGE_DEFAULT_DIALER}.
     */
    public static final java.lang.String EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME = "android.telecom.extra.CHANGE_DEFAULT_DIALER_PACKAGE_NAME";

    /**
     * Optional extra for {@link android.content.Intent#ACTION_CALL} containing a boolean that
     * determines whether the speakerphone should be automatically turned on for an outgoing call.
     */
    public static final java.lang.String EXTRA_START_CALL_WITH_SPEAKERPHONE = "android.telecom.extra.START_CALL_WITH_SPEAKERPHONE";

    /**
     * Optional extra for {@link android.content.Intent#ACTION_CALL} containing an integer that
     * determines the desired video state for an outgoing call.
     * Valid options:
     * {@link VideoProfile#STATE_AUDIO_ONLY},
     * {@link VideoProfile#STATE_BIDIRECTIONAL},
     * {@link VideoProfile#STATE_RX_ENABLED},
     * {@link VideoProfile#STATE_TX_ENABLED}.
     */
    public static final java.lang.String EXTRA_START_CALL_WITH_VIDEO_STATE = "android.telecom.extra.START_CALL_WITH_VIDEO_STATE";

    /**
     * The extra used with an {@link android.content.Intent#ACTION_CALL} and
     * {@link android.content.Intent#ACTION_DIAL} {@code Intent} to specify a
     * {@link PhoneAccountHandle} to use when making the call.
     * <p class="note">
     * Retrieve with {@link android.content.Intent#getParcelableExtra(String)}.
     */
    public static final java.lang.String EXTRA_PHONE_ACCOUNT_HANDLE = "android.telecom.extra.PHONE_ACCOUNT_HANDLE";

    /**
     * Optional extra for {@link android.content.Intent#ACTION_CALL} containing a string call
     * subject which will be associated with an outgoing call.  Should only be specified if the
     * {@link PhoneAccount} supports the capability {@link PhoneAccount#CAPABILITY_CALL_SUBJECT}.
     */
    public static final java.lang.String EXTRA_CALL_SUBJECT = "android.telecom.extra.CALL_SUBJECT";

    /**
     * The extra used by a {@link ConnectionService} to provide the handle of the caller that
     * has initiated a new incoming call.
     */
    public static final java.lang.String EXTRA_INCOMING_CALL_ADDRESS = "android.telecom.extra.INCOMING_CALL_ADDRESS";

    /**
     * Optional extra for {@link #ACTION_INCOMING_CALL} containing a {@link Bundle} which contains
     * metadata about the call. This {@link Bundle} will be returned to the
     * {@link ConnectionService}.
     */
    public static final java.lang.String EXTRA_INCOMING_CALL_EXTRAS = "android.telecom.extra.INCOMING_CALL_EXTRAS";

    /**
     * Optional extra for {@link android.content.Intent#ACTION_CALL} and
     * {@link android.content.Intent#ACTION_DIAL} {@code Intent} containing a {@link Bundle}
     * which contains metadata about the call. This {@link Bundle} will be saved into
     * {@code Call.Details} and passed to the {@link ConnectionService} when placing the call.
     */
    public static final java.lang.String EXTRA_OUTGOING_CALL_EXTRAS = "android.telecom.extra.OUTGOING_CALL_EXTRAS";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_UNKNOWN_CALL_HANDLE = "android.telecom.extra.UNKNOWN_CALL_HANDLE";

    /**
     * Optional extra for incoming and outgoing calls containing a long which specifies the time the
     * call was created. This value is in milliseconds since boot.
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_CALL_CREATED_TIME_MILLIS = "android.telecom.extra.CALL_CREATED_TIME_MILLIS";

    /**
     * Optional extra for incoming and outgoing calls containing a long which specifies the time
     * telecom began routing the call. This value is in milliseconds since boot.
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_CALL_TELECOM_ROUTING_START_TIME_MILLIS = "android.telecom.extra.CALL_TELECOM_ROUTING_START_TIME_MILLIS";

    /**
     * Optional extra for incoming and outgoing calls containing a long which specifies the time
     * telecom finished routing the call. This value is in milliseconds since boot.
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_CALL_TELECOM_ROUTING_END_TIME_MILLIS = "android.telecom.extra.CALL_TELECOM_ROUTING_END_TIME_MILLIS";

    /**
     * Optional extra for {@link android.telephony.TelephonyManager#ACTION_PHONE_STATE_CHANGED}
     * containing the disconnect code.
     */
    public static final java.lang.String EXTRA_CALL_DISCONNECT_CAUSE = "android.telecom.extra.CALL_DISCONNECT_CAUSE";

    /**
     * Optional extra for {@link android.telephony.TelephonyManager#ACTION_PHONE_STATE_CHANGED}
     * containing the disconnect message.
     */
    public static final java.lang.String EXTRA_CALL_DISCONNECT_MESSAGE = "android.telecom.extra.CALL_DISCONNECT_MESSAGE";

    /**
     * Optional extra for {@link android.telephony.TelephonyManager#ACTION_PHONE_STATE_CHANGED}
     * containing the component name of the associated connection service.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final java.lang.String EXTRA_CONNECTION_SERVICE = "android.telecom.extra.CONNECTION_SERVICE";

    /**
     * Optional extra for communicating the call technology used by a
     * {@link com.android.internal.telephony.Connection} to Telecom
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_CALL_TECHNOLOGY_TYPE = "android.telecom.extra.CALL_TECHNOLOGY_TYPE";

    /**
     * An optional {@link android.content.Intent#ACTION_CALL} intent extra denoting the
     * package name of the app specifying an alternative gateway for the call.
     * The value is a string.
     *
     * (The following comment corresponds to the all GATEWAY_* extras)
     * An app which sends the {@link android.content.Intent#ACTION_CALL} intent can specify an
     * alternative address to dial which is different from the one specified and displayed to
     * the user. This alternative address is referred to as the gateway address.
     */
    public static final java.lang.String GATEWAY_PROVIDER_PACKAGE = "android.telecom.extra.GATEWAY_PROVIDER_PACKAGE";

    /**
     * An optional {@link android.content.Intent#ACTION_CALL} intent extra corresponding to the
     * original address to dial for the call. This is used when an alternative gateway address is
     * provided to recall the original address.
     * The value is a {@link android.net.Uri}.
     *
     * (See {@link #GATEWAY_PROVIDER_PACKAGE} for details)
     */
    public static final java.lang.String GATEWAY_ORIGINAL_ADDRESS = "android.telecom.extra.GATEWAY_ORIGINAL_ADDRESS";

    /**
     * The number which the party on the other side of the line will see (and use to return the
     * call).
     * <p>
     * {@link ConnectionService}s which interact with {@link RemoteConnection}s should only populate
     * this if the {@link android.telephony.TelephonyManager#getLine1Number()} value, as that is the
     * user's expected caller ID.
     */
    public static final java.lang.String EXTRA_CALL_BACK_NUMBER = "android.telecom.extra.CALL_BACK_NUMBER";

    /**
     * A boolean meta-data value indicating whether an {@link InCallService} implements an
     * in-call user interface. Dialer implementations (see {@link #getDefaultDialerPackage()}) which
     * would also like to replace the in-call interface should set this meta-data to {@code true} in
     * the manifest registration of their {@link InCallService}.
     */
    public static final java.lang.String METADATA_IN_CALL_SERVICE_UI = "android.telecom.IN_CALL_SERVICE_UI";

    /**
     * A boolean meta-data value indicating whether an {@link InCallService} implements an
     * in-call user interface to be used while the device is in car-mode (see
     * {@link android.content.res.Configuration.UI_MODE_TYPE_CAR}).
     *
     * @unknown 
     */
    public static final java.lang.String METADATA_IN_CALL_SERVICE_CAR_MODE_UI = "android.telecom.IN_CALL_SERVICE_CAR_MODE_UI";

    /**
     * A boolean meta-data value indicating whether an {@link InCallService} implements ringing.
     * Dialer implementations (see {@link #getDefaultDialerPackage()}) which would also like to
     * override the system provided ringing should set this meta-data to {@code true} in the
     * manifest registration of their {@link InCallService}.
     */
    public static final java.lang.String METADATA_IN_CALL_SERVICE_RINGING = "android.telecom.IN_CALL_SERVICE_RINGING";

    /**
     * A boolean meta-data value indicating whether an {@link InCallService} wants to be informed of
     * calls which have the {@link Call.Details#PROPERTY_IS_EXTERNAL_CALL} property.  An external
     * call is one which a {@link ConnectionService} knows about, but is not connected to directly.
     * Dialer implementations (see {@link #getDefaultDialerPackage()}) which would like to be
     * informed of external calls should set this meta-data to {@code true} in the manifest
     * registration of their {@link InCallService}.  By default, the {@link InCallService} will NOT
     * be informed of external calls.
     */
    public static final java.lang.String METADATA_INCLUDE_EXTERNAL_CALLS = "android.telecom.INCLUDE_EXTERNAL_CALLS";

    /**
     * The dual tone multi-frequency signaling character sent to indicate the dialing system should
     * pause for a predefined period.
     */
    public static final char DTMF_CHARACTER_PAUSE = ',';

    /**
     * The dual-tone multi-frequency signaling character sent to indicate the dialing system should
     * wait for user confirmation before proceeding.
     */
    public static final char DTMF_CHARACTER_WAIT = ';';

    /**
     * TTY (teletypewriter) mode is off.
     *
     * @unknown 
     */
    public static final int TTY_MODE_OFF = 0;

    /**
     * TTY (teletypewriter) mode is on. The speaker is off and the microphone is muted. The user
     * will communicate with the remote party by sending and receiving text messages.
     *
     * @unknown 
     */
    public static final int TTY_MODE_FULL = 1;

    /**
     * TTY (teletypewriter) mode is in hearing carryover mode (HCO). The microphone is muted but the
     * speaker is on. The user will communicate with the remote party by sending text messages and
     * hearing an audible reply.
     *
     * @unknown 
     */
    public static final int TTY_MODE_HCO = 2;

    /**
     * TTY (teletypewriter) mode is in voice carryover mode (VCO). The speaker is off but the
     * microphone is still on. User will communicate with the remote party by speaking and receiving
     * text message replies.
     *
     * @unknown 
     */
    public static final int TTY_MODE_VCO = 3;

    /**
     * Broadcast intent action indicating that the current TTY mode has changed. An intent extra
     * provides this state as an int.
     *
     * @see #EXTRA_CURRENT_TTY_MODE
     * @unknown 
     */
    public static final java.lang.String ACTION_CURRENT_TTY_MODE_CHANGED = "android.telecom.action.CURRENT_TTY_MODE_CHANGED";

    /**
     * The lookup key for an int that indicates the current TTY mode.
     * Valid modes are:
     * - {@link #TTY_MODE_OFF}
     * - {@link #TTY_MODE_FULL}
     * - {@link #TTY_MODE_HCO}
     * - {@link #TTY_MODE_VCO}
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_CURRENT_TTY_MODE = "android.telecom.intent.extra.CURRENT_TTY_MODE";

    /**
     * Broadcast intent action indicating that the TTY preferred operating mode has changed. An
     * intent extra provides the new mode as an int.
     *
     * @see #EXTRA_TTY_PREFERRED_MODE
     * @unknown 
     */
    public static final java.lang.String ACTION_TTY_PREFERRED_MODE_CHANGED = "android.telecom.action.TTY_PREFERRED_MODE_CHANGED";

    /**
     * The lookup key for an int that indicates preferred TTY mode. Valid modes are: -
     * {@link #TTY_MODE_OFF} - {@link #TTY_MODE_FULL} - {@link #TTY_MODE_HCO} -
     * {@link #TTY_MODE_VCO}
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_TTY_PREFERRED_MODE = "android.telecom.intent.extra.TTY_PREFERRED";

    /**
     * Broadcast intent action for letting custom component know to show the missed call
     * notification. If no custom component exists then this is sent to the default dialer which
     * should post a missed-call notification.
     */
    public static final java.lang.String ACTION_SHOW_MISSED_CALLS_NOTIFICATION = "android.telecom.action.SHOW_MISSED_CALLS_NOTIFICATION";

    /**
     * The number of calls associated with the notification. If the number is zero then the missed
     * call notification should be dismissed.
     */
    public static final java.lang.String EXTRA_NOTIFICATION_COUNT = "android.telecom.extra.NOTIFICATION_COUNT";

    /**
     * The number associated with the missed calls. This number is only relevant
     * when EXTRA_NOTIFICATION_COUNT is 1.
     */
    public static final java.lang.String EXTRA_NOTIFICATION_PHONE_NUMBER = "android.telecom.extra.NOTIFICATION_PHONE_NUMBER";

    /**
     * The intent to clear missed calls.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final java.lang.String EXTRA_CLEAR_MISSED_CALLS_INTENT = "android.telecom.extra.CLEAR_MISSED_CALLS_INTENT";

    /**
     * The intent to call back a missed call.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final java.lang.String EXTRA_CALL_BACK_INTENT = "android.telecom.extra.CALL_BACK_INTENT";

    /**
     * The following 4 constants define how properties such as phone numbers and names are
     * displayed to the user.
     */
    /**
     * Indicates that the address or number of a call is allowed to be displayed for caller ID.
     */
    public static final int PRESENTATION_ALLOWED = 1;

    /**
     * Indicates that the address or number of a call is blocked by the other party.
     */
    public static final int PRESENTATION_RESTRICTED = 2;

    /**
     * Indicates that the address or number of a call is not specified or known by the carrier.
     */
    public static final int PRESENTATION_UNKNOWN = 3;

    /**
     * Indicates that the address or number of a call belongs to a pay phone.
     */
    public static final int PRESENTATION_PAYPHONE = 4;

    private static final java.lang.String TAG = "TelecomManager";

    private final android.content.Context mContext;

    private final com.android.internal.telecom.ITelecomService mTelecomServiceOverride;

    /**
     *
     *
     * @unknown 
     */
    public static android.telecom.TelecomManager from(android.content.Context context) {
        return ((android.telecom.TelecomManager) (context.getSystemService(android.content.Context.TELECOM_SERVICE)));
    }

    /**
     *
     *
     * @unknown 
     */
    public TelecomManager(android.content.Context context) {
        this(context, null);
    }

    /**
     *
     *
     * @unknown 
     */
    public TelecomManager(android.content.Context context, com.android.internal.telecom.ITelecomService telecomServiceImpl) {
        android.content.Context appContext = context.getApplicationContext();
        if (appContext != null) {
            mContext = appContext;
        } else {
            mContext = context;
        }
        mTelecomServiceOverride = telecomServiceImpl;
        android.telecom.Log.initMd5Sum();
    }

    /**
     * Return the {@link PhoneAccount} which will be used to place outgoing calls to addresses with
     * the specified {@code uriScheme}. This {@link PhoneAccount} will always be a member of the
     * list which is returned from invoking {@link #getCallCapablePhoneAccounts()}. The specific
     * account returned depends on the following priorities:
     * <ul>
     * <li> If the user-selected default {@link PhoneAccount} supports the specified scheme, it will
     * be returned.
     * </li>
     * <li> If there exists only one {@link PhoneAccount} that supports the specified scheme, it
     * will be returned.
     * </li>
     * </ul>
     * <p>
     * If no {@link PhoneAccount} fits the criteria above, this method will return {@code null}.
     *
     * Requires permission: {@link android.Manifest.permission#READ_PHONE_STATE}
     *
     * @param uriScheme
     * 		The URI scheme.
     * @return The {@link PhoneAccountHandle} corresponding to the account to be used.
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public android.telecom.PhoneAccountHandle getDefaultOutgoingPhoneAccount(java.lang.String uriScheme) {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getDefaultOutgoingPhoneAccount(uriScheme, mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#getDefaultOutgoingPhoneAccount", e);
        }
        return null;
    }

    /**
     * Return the {@link PhoneAccount} which is the user-chosen default for making outgoing phone
     * calls. This {@code PhoneAccount} will always be a member of the list which is returned from
     * calling {@link #getCallCapablePhoneAccounts()}
     * <p>
     * Apps must be prepared for this method to return {@code null}, indicating that there currently
     * exists no user-chosen default {@code PhoneAccount}.
     *
     * @return The user outgoing phone account selected by the user.
     * @unknown 
     */
    public android.telecom.PhoneAccountHandle getUserSelectedOutgoingPhoneAccount() {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getUserSelectedOutgoingPhoneAccount();
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#getUserSelectedOutgoingPhoneAccount", e);
        }
        return null;
    }

    /**
     * Sets the user-chosen default for making outgoing phone calls.
     *
     * @unknown 
     */
    public void setUserSelectedOutgoingPhoneAccount(android.telecom.PhoneAccountHandle accountHandle) {
        try {
            if (isServiceConnected()) {
                getTelecomService().setUserSelectedOutgoingPhoneAccount(accountHandle);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#setUserSelectedOutgoingPhoneAccount");
        }
    }

    /**
     * Returns the current SIM call manager. Apps must be prepared for this method to return
     * {@code null}, indicating that there currently exists no user-chosen default
     * {@code PhoneAccount}.
     *
     * @return The phone account handle of the current sim call manager.
     */
    public android.telecom.PhoneAccountHandle getSimCallManager() {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getSimCallManager();
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#getSimCallManager");
        }
        return null;
    }

    /**
     * Returns the current SIM call manager for the specified user. Apps must be prepared for this
     * method to return {@code null}, indicating that there currently exists no user-chosen default
     * {@code PhoneAccount}.
     *
     * @return The phone account handle of the current sim call manager.
     * @unknown 
     */
    public android.telecom.PhoneAccountHandle getSimCallManager(int userId) {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getSimCallManagerForUser(userId);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#getSimCallManagerForUser");
        }
        return null;
    }

    /**
     * Returns the current connection manager. Apps must be prepared for this method to return
     * {@code null}, indicating that there currently exists no user-chosen default
     * {@code PhoneAccount}.
     *
     * @return The phone account handle of the current connection manager.
     * @unknown 
     */
    @android.annotation.SystemApi
    public android.telecom.PhoneAccountHandle getConnectionManager() {
        return getSimCallManager();
    }

    /**
     * Returns a list of the {@link PhoneAccountHandle}s which can be used to make and receive phone
     * calls which support the specified URI scheme.
     * <P>
     * For example, invoking with {@code "tel"} will find all {@link PhoneAccountHandle}s which
     * support telephone calls (e.g. URIs such as {@code tel:555-555-1212}).  Invoking with
     * {@code "sip"} will find all {@link PhoneAccountHandle}s which support SIP calls (e.g. URIs
     * such as {@code sip:example@sipexample.com}).
     *
     * @param uriScheme
     * 		The URI scheme.
     * @return A list of {@code PhoneAccountHandle} objects supporting the URI scheme.
     * @unknown 
     */
    @android.annotation.SystemApi
    public java.util.List<android.telecom.PhoneAccountHandle> getPhoneAccountsSupportingScheme(java.lang.String uriScheme) {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getPhoneAccountsSupportingScheme(uriScheme, mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#getPhoneAccountsSupportingScheme", e);
        }
        return new java.util.ArrayList<>();
    }

    /**
     * Returns a list of {@link PhoneAccountHandle}s which can be used to make and receive phone
     * calls. The returned list includes only those accounts which have been explicitly enabled
     * by the user.
     *
     * Requires permission: {@link android.Manifest.permission#READ_PHONE_STATE}
     *
     * @see #EXTRA_PHONE_ACCOUNT_HANDLE
     * @return A list of {@code PhoneAccountHandle} objects.
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public java.util.List<android.telecom.PhoneAccountHandle> getCallCapablePhoneAccounts() {
        return getCallCapablePhoneAccounts(false);
    }

    /**
     * Returns a list of {@link PhoneAccountHandle}s including those which have not been enabled
     * by the user.
     *
     * @return A list of {@code PhoneAccountHandle} objects.
     * @unknown 
     */
    public java.util.List<android.telecom.PhoneAccountHandle> getCallCapablePhoneAccounts(boolean includeDisabledAccounts) {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getCallCapablePhoneAccounts(includeDisabledAccounts, mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, ("Error calling ITelecomService#getCallCapablePhoneAccounts(" + includeDisabledAccounts) + ")", e);
        }
        return new java.util.ArrayList<>();
    }

    /**
     * Returns a list of all {@link PhoneAccount}s registered for the calling package.
     *
     * @return A list of {@code PhoneAccountHandle} objects.
     * @unknown 
     */
    @android.annotation.SystemApi
    public java.util.List<android.telecom.PhoneAccountHandle> getPhoneAccountsForPackage() {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getPhoneAccountsForPackage(mContext.getPackageName());
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#getPhoneAccountsForPackage", e);
        }
        return null;
    }

    /**
     * Return the {@link PhoneAccount} for a specified {@link PhoneAccountHandle}. Object includes
     * resources which can be used in a user interface.
     *
     * @param account
     * 		The {@link PhoneAccountHandle}.
     * @return The {@link PhoneAccount} object.
     */
    public android.telecom.PhoneAccount getPhoneAccount(android.telecom.PhoneAccountHandle account) {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getPhoneAccount(account);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#getPhoneAccount", e);
        }
        return null;
    }

    /**
     * Returns a count of all {@link PhoneAccount}s.
     *
     * @return The count of {@link PhoneAccount}s.
     * @unknown 
     */
    @android.annotation.SystemApi
    public int getAllPhoneAccountsCount() {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getAllPhoneAccountsCount();
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#getAllPhoneAccountsCount", e);
        }
        return 0;
    }

    /**
     * Returns a list of all {@link PhoneAccount}s.
     *
     * @return All {@link PhoneAccount}s.
     * @unknown 
     */
    @android.annotation.SystemApi
    public java.util.List<android.telecom.PhoneAccount> getAllPhoneAccounts() {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getAllPhoneAccounts();
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#getAllPhoneAccounts", e);
        }
        return java.util.Collections.EMPTY_LIST;
    }

    /**
     * Returns a list of all {@link PhoneAccountHandle}s.
     *
     * @return All {@link PhoneAccountHandle}s.
     * @unknown 
     */
    @android.annotation.SystemApi
    public java.util.List<android.telecom.PhoneAccountHandle> getAllPhoneAccountHandles() {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getAllPhoneAccountHandles();
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#getAllPhoneAccountHandles", e);
        }
        return java.util.Collections.EMPTY_LIST;
    }

    /**
     * Register a {@link PhoneAccount} for use by the system that will be stored in Device Encrypted
     * storage. When registering {@link PhoneAccount}s, existing registrations will be overwritten
     * if the {@link PhoneAccountHandle} matches that of a {@link PhoneAccount} which is already
     * registered. Once registered, the {@link PhoneAccount} is listed to the user as an option
     * when placing calls. The user may still need to enable the {@link PhoneAccount} within
     * the phone app settings before the account is usable.
     * <p>
     * A {@link SecurityException} will be thrown if an app tries to register a
     * {@link PhoneAccountHandle} where the package name specified within
     * {@link PhoneAccountHandle#getComponentName()} does not match the package name of the app.
     *
     * @param account
     * 		The complete {@link PhoneAccount}.
     */
    public void registerPhoneAccount(android.telecom.PhoneAccount account) {
        try {
            if (isServiceConnected()) {
                getTelecomService().registerPhoneAccount(account);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#registerPhoneAccount", e);
        }
    }

    /**
     * Remove a {@link PhoneAccount} registration from the system.
     *
     * @param accountHandle
     * 		A {@link PhoneAccountHandle} for the {@link PhoneAccount} to unregister.
     */
    public void unregisterPhoneAccount(android.telecom.PhoneAccountHandle accountHandle) {
        try {
            if (isServiceConnected()) {
                getTelecomService().unregisterPhoneAccount(accountHandle);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#unregisterPhoneAccount", e);
        }
    }

    /**
     * Remove all Accounts that belong to the calling package from the system.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public void clearPhoneAccounts() {
        clearAccounts();
    }

    /**
     * Remove all Accounts that belong to the calling package from the system.
     *
     * @deprecated Use {@link #clearPhoneAccounts()} instead.
     * @unknown 
     */
    @android.annotation.SystemApi
    public void clearAccounts() {
        try {
            if (isServiceConnected()) {
                getTelecomService().clearAccounts(mContext.getPackageName());
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#clearAccounts", e);
        }
    }

    /**
     * Remove all Accounts that belong to the specified package from the system.
     *
     * @unknown 
     */
    public void clearAccountsForPackage(java.lang.String packageName) {
        try {
            if (isServiceConnected() && (!android.text.TextUtils.isEmpty(packageName))) {
                getTelecomService().clearAccounts(packageName);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#clearAccountsForPackage", e);
        }
    }

    /**
     *
     *
     * @deprecated - Use {@link TelecomManager#getDefaultDialerPackage} to directly access
    the default dialer's package name instead.
     * @unknown 
     */
    @android.annotation.SystemApi
    public android.content.ComponentName getDefaultPhoneApp() {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getDefaultPhoneApp();
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "RemoteException attempting to get the default phone app.", e);
        }
        return null;
    }

    /**
     * Used to determine the currently selected default dialer package.
     *
     * @return package name for the default dialer package or null if no package has been
    selected as the default dialer.
     */
    public java.lang.String getDefaultDialerPackage() {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getDefaultDialerPackage();
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "RemoteException attempting to get the default dialer package name.", e);
        }
        return null;
    }

    /**
     * Used to set the default dialer package.
     *
     * @param packageName
     * 		to set the default dialer to..
     * @unknown {@code true} if the default dialer was successfully changed, {@code false} if
    the specified package does not correspond to an installed dialer, or is already
    the default dialer.

    Requires permission: {@link android.Manifest.permission#MODIFY_PHONE_STATE}
    Requires permission: {@link android.Manifest.permission#WRITE_SECURE_SETTINGS}
     * @unknown 
     */
    public boolean setDefaultDialer(java.lang.String packageName) {
        try {
            if (isServiceConnected()) {
                return getTelecomService().setDefaultDialer(packageName);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "RemoteException attempting to set the default dialer.", e);
        }
        return false;
    }

    /**
     * Used to determine the dialer package that is preloaded on the system partition.
     *
     * @return package name for the system dialer package or null if no system dialer is preloaded.
     * @unknown 
     */
    public java.lang.String getSystemDialerPackage() {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getSystemDialerPackage();
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "RemoteException attempting to get the system dialer package name.", e);
        }
        return null;
    }

    /**
     * Return whether a given phone number is the configured voicemail number for a
     * particular phone account.
     *
     * Requires permission: {@link android.Manifest.permission#READ_PHONE_STATE}
     *
     * @param accountHandle
     * 		The handle for the account to check the voicemail number against
     * @param number
     * 		The number to look up.
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public boolean isVoiceMailNumber(android.telecom.PhoneAccountHandle accountHandle, java.lang.String number) {
        try {
            if (isServiceConnected()) {
                return getTelecomService().isVoiceMailNumber(accountHandle, number, mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "RemoteException calling ITelecomService#isVoiceMailNumber.", e);
        }
        return false;
    }

    /**
     * Return the voicemail number for a given phone account.
     *
     * Requires permission: {@link android.Manifest.permission#READ_PHONE_STATE}
     *
     * @param accountHandle
     * 		The handle for the phone account.
     * @return The voicemail number for the phone account, and {@code null} if one has not been
    configured.
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public java.lang.String getVoiceMailNumber(android.telecom.PhoneAccountHandle accountHandle) {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getVoiceMailNumber(accountHandle, mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "RemoteException calling ITelecomService#hasVoiceMailNumber.", e);
        }
        return null;
    }

    /**
     * Return the line 1 phone number for given phone account.
     *
     * Requires permission: {@link android.Manifest.permission#READ_PHONE_STATE}
     *
     * @param accountHandle
     * 		The handle for the account retrieve a number for.
     * @return A string representation of the line 1 phone number.
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public java.lang.String getLine1Number(android.telecom.PhoneAccountHandle accountHandle) {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getLine1Number(accountHandle, mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "RemoteException calling ITelecomService#getLine1Number.", e);
        }
        return null;
    }

    /**
     * Returns whether there is an ongoing phone call (can be in dialing, ringing, active or holding
     * states).
     * <p>
     * Requires permission: {@link android.Manifest.permission#READ_PHONE_STATE}
     * </p>
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public boolean isInCall() {
        try {
            if (isServiceConnected()) {
                return getTelecomService().isInCall(mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "RemoteException calling isInCall().", e);
        }
        return false;
    }

    /**
     * Returns one of the following constants that represents the current state of Telecom:
     *
     * {@link TelephonyManager#CALL_STATE_RINGING}
     * {@link TelephonyManager#CALL_STATE_OFFHOOK}
     * {@link TelephonyManager#CALL_STATE_IDLE}
     *
     * Note that this API does not require the
     * {@link android.Manifest.permission#READ_PHONE_STATE} permission. This is intentional, to
     * preserve the behavior of {@link TelephonyManager#getCallState()}, which also did not require
     * the permission.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public int getCallState() {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getCallState();
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.d(android.telecom.TelecomManager.TAG, "RemoteException calling getCallState().", e);
        }
        return android.telephony.TelephonyManager.CALL_STATE_IDLE;
    }

    /**
     * Returns whether there currently exists is a ringing incoming-call.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public boolean isRinging() {
        try {
            if (isServiceConnected()) {
                return getTelecomService().isRinging(mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "RemoteException attempting to get ringing state of phone app.", e);
        }
        return false;
    }

    /**
     * Ends an ongoing call.
     * TODO: L-release - need to convert all invocations of ITelecomService#endCall to use this
     * method (clockwork & gearhead).
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public boolean endCall() {
        try {
            if (isServiceConnected()) {
                return getTelecomService().endCall();
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#endCall", e);
        }
        return false;
    }

    /**
     * If there is a ringing incoming call, this method accepts the call on behalf of the user.
     * TODO: L-release - need to convert all invocation of ITelecmmService#answerRingingCall to use
     * this method (clockwork & gearhead).
     * If the incoming call is a video call, the call will be answered with the same video state as
     * the incoming call requests.  This means, for example, that an incoming call requesting
     * {@link VideoProfile#STATE_BIDIRECTIONAL} will be answered, accepting that state.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public void acceptRingingCall() {
        try {
            if (isServiceConnected()) {
                getTelecomService().acceptRingingCall();
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#acceptRingingCall", e);
        }
    }

    /**
     * If there is a ringing incoming call, this method accepts the call on behalf of the user,
     * with the specified video state.
     *
     * @param videoState
     * 		The desired video state to answer the call with.
     * @unknown 
     */
    @android.annotation.SystemApi
    public void acceptRingingCall(int videoState) {
        try {
            if (isServiceConnected()) {
                getTelecomService().acceptRingingCallWithVideoState(videoState);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#acceptRingingCallWithVideoState", e);
        }
    }

    /**
     * Silences the ringer if a ringing call exists.
     *
     * Requires permission: {@link android.Manifest.permission#MODIFY_PHONE_STATE}
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.MODIFY_PHONE_STATE)
    public void silenceRinger() {
        try {
            if (isServiceConnected()) {
                getTelecomService().silenceRinger(mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#silenceRinger", e);
        }
    }

    /**
     * Returns whether TTY is supported on this device.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public boolean isTtySupported() {
        try {
            if (isServiceConnected()) {
                return getTelecomService().isTtySupported(mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "RemoteException attempting to get TTY supported state.", e);
        }
        return false;
    }

    /**
     * Returns the current TTY mode of the device. For TTY to be on the user must enable it in
     * settings and have a wired headset plugged in.
     * Valid modes are:
     * - {@link TelecomManager#TTY_MODE_OFF}
     * - {@link TelecomManager#TTY_MODE_FULL}
     * - {@link TelecomManager#TTY_MODE_HCO}
     * - {@link TelecomManager#TTY_MODE_VCO}
     *
     * @unknown 
     */
    public int getCurrentTtyMode() {
        try {
            if (isServiceConnected()) {
                return getTelecomService().getCurrentTtyMode(mContext.getOpPackageName());
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "RemoteException attempting to get the current TTY mode.", e);
        }
        return android.telecom.TelecomManager.TTY_MODE_OFF;
    }

    /**
     * Registers a new incoming call. A {@link ConnectionService} should invoke this method when it
     * has an incoming call. The specified {@link PhoneAccountHandle} must have been registered
     * with {@link #registerPhoneAccount} and the user must have enabled the corresponding
     * {@link PhoneAccount}. This can be checked using {@link #getPhoneAccount}. Once invoked, this
     * method will cause the system to bind to the {@link ConnectionService} associated with the
     * {@link PhoneAccountHandle} and request additional information about the call
     * (See {@link ConnectionService#onCreateIncomingConnection}) before starting the incoming
     * call UI.
     * <p>
     * A {@link SecurityException} will be thrown if either the {@link PhoneAccountHandle} does not
     * correspond to a registered {@link PhoneAccount} or the associated {@link PhoneAccount} is not
     * currently enabled by the user.
     *
     * @param phoneAccount
     * 		A {@link PhoneAccountHandle} registered with
     * 		{@link #registerPhoneAccount}.
     * @param extras
     * 		A bundle that will be passed through to
     * 		{@link ConnectionService#onCreateIncomingConnection}.
     */
    public void addNewIncomingCall(android.telecom.PhoneAccountHandle phoneAccount, android.os.Bundle extras) {
        try {
            if (isServiceConnected()) {
                getTelecomService().addNewIncomingCall(phoneAccount, extras == null ? new android.os.Bundle() : extras);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "RemoteException adding a new incoming call: " + phoneAccount, e);
        }
    }

    /**
     * Registers a new unknown call with Telecom. This can only be called by the system Telephony
     * service. This is invoked when Telephony detects a new unknown connection that was neither
     * a new incoming call, nor an user-initiated outgoing call.
     *
     * @param phoneAccount
     * 		A {@link PhoneAccountHandle} registered with
     * 		{@link #registerPhoneAccount}.
     * @param extras
     * 		A bundle that will be passed through to
     * 		{@link ConnectionService#onCreateIncomingConnection}.
     * @unknown 
     */
    @android.annotation.SystemApi
    public void addNewUnknownCall(android.telecom.PhoneAccountHandle phoneAccount, android.os.Bundle extras) {
        try {
            if (isServiceConnected()) {
                getTelecomService().addNewUnknownCall(phoneAccount, extras == null ? new android.os.Bundle() : extras);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.telecom.TelecomManager.TAG, "RemoteException adding a new unknown call: " + phoneAccount, e);
        }
    }

    /**
     * Processes the specified dial string as an MMI code.
     * MMI codes are any sequence of characters entered into the dialpad that contain a "*" or "#".
     * Some of these sequences launch special behavior through handled by Telephony.
     * This method uses the default subscription.
     * <p>
     * Requires that the method-caller be set as the system dialer app.
     * </p>
     *
     * Requires permission: {@link android.Manifest.permission#MODIFY_PHONE_STATE}
     *
     * @param dialString
     * 		The digits to dial.
     * @return True if the digits were processed as an MMI code, false otherwise.
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.MODIFY_PHONE_STATE)
    public boolean handleMmi(java.lang.String dialString) {
        com.android.internal.telecom.ITelecomService service = getTelecomService();
        if (service != null) {
            try {
                return service.handlePinMmi(dialString, mContext.getOpPackageName());
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#handlePinMmi", e);
            }
        }
        return false;
    }

    /**
     * Processes the specified dial string as an MMI code.
     * MMI codes are any sequence of characters entered into the dialpad that contain a "*" or "#".
     * Some of these sequences launch special behavior through handled by Telephony.
     * <p>
     * Requires that the method-caller be set as the system dialer app.
     * </p>
     *
     * Requires permission: {@link android.Manifest.permission#MODIFY_PHONE_STATE}
     *
     * @param accountHandle
     * 		The handle for the account the MMI code should apply to.
     * @param dialString
     * 		The digits to dial.
     * @return True if the digits were processed as an MMI code, false otherwise.
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.MODIFY_PHONE_STATE)
    public boolean handleMmi(java.lang.String dialString, android.telecom.PhoneAccountHandle accountHandle) {
        com.android.internal.telecom.ITelecomService service = getTelecomService();
        if (service != null) {
            try {
                return service.handlePinMmiForPhoneAccount(accountHandle, dialString, mContext.getOpPackageName());
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#handlePinMmi", e);
            }
        }
        return false;
    }

    /**
     * Requires permission: {@link android.Manifest.permission#MODIFY_PHONE_STATE}
     *
     * @param accountHandle
     * 		The handle for the account to derive an adn query URI for or
     * 		{@code null} to return a URI which will use the default account.
     * @return The URI (with the content:// scheme) specific to the specified {@link PhoneAccount}
    for the the content retrieve.
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.MODIFY_PHONE_STATE)
    public android.net.Uri getAdnUriForPhoneAccount(android.telecom.PhoneAccountHandle accountHandle) {
        com.android.internal.telecom.ITelecomService service = getTelecomService();
        if ((service != null) && (accountHandle != null)) {
            try {
                return service.getAdnUriForPhoneAccount(accountHandle, mContext.getOpPackageName());
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#getAdnUriForPhoneAccount", e);
            }
        }
        return android.net.Uri.parse("content://icc/adn");
    }

    /**
     * Removes the missed-call notification if one is present.
     * <p>
     * Requires that the method-caller be set as the system dialer app.
     * </p>
     *
     * Requires permission: {@link android.Manifest.permission#MODIFY_PHONE_STATE}
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.MODIFY_PHONE_STATE)
    public void cancelMissedCallsNotification() {
        com.android.internal.telecom.ITelecomService service = getTelecomService();
        if (service != null) {
            try {
                service.cancelMissedCallsNotification(mContext.getOpPackageName());
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#cancelMissedCallsNotification", e);
            }
        }
    }

    /**
     * Brings the in-call screen to the foreground if there is an ongoing call. If there is
     * currently no ongoing call, then this method does nothing.
     * <p>
     * Requires that the method-caller be set as the system dialer app or have the
     * {@link android.Manifest.permission#READ_PHONE_STATE} permission.
     * </p>
     *
     * @param showDialpad
     * 		Brings up the in-call dialpad as part of showing the in-call screen.
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public void showInCallScreen(boolean showDialpad) {
        com.android.internal.telecom.ITelecomService service = getTelecomService();
        if (service != null) {
            try {
                service.showInCallScreen(showDialpad, mContext.getOpPackageName());
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#showCallScreen", e);
            }
        }
    }

    /**
     * Places a new outgoing call to the provided address using the system telecom service with
     * the specified extras.
     *
     * This method is equivalent to placing an outgoing call using {@link Intent#ACTION_CALL},
     * except that the outgoing call will always be sent via the system telecom service. If
     * method-caller is either the user selected default dialer app or preloaded system dialer
     * app, then emergency calls will also be allowed.
     *
     * Requires permission: {@link android.Manifest.permission#CALL_PHONE}
     *
     * Usage example:
     * <pre>
     * Uri uri = Uri.fromParts("tel", "12345", null);
     * Bundle extras = new Bundle();
     * extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, true);
     * telecomManager.placeCall(uri, extras);
     * </pre>
     *
     * The following keys are supported in the supplied extras.
     * <ul>
     *   <li>{@link #EXTRA_OUTGOING_CALL_EXTRAS}</li>
     *   <li>{@link #EXTRA_PHONE_ACCOUNT_HANDLE}</li>
     *   <li>{@link #EXTRA_START_CALL_WITH_SPEAKERPHONE}</li>
     *   <li>{@link #EXTRA_START_CALL_WITH_VIDEO_STATE}</li>
     * </ul>
     *
     * @param address
     * 		The address to make the call to.
     * @param extras
     * 		Bundle of extras to use with the call.
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.CALL_PHONE)
    public void placeCall(android.net.Uri address, android.os.Bundle extras) {
        com.android.internal.telecom.ITelecomService service = getTelecomService();
        if (service != null) {
            if (address == null) {
                android.util.Log.w(android.telecom.TelecomManager.TAG, "Cannot place call to empty address.");
            }
            try {
                service.placeCall(address, extras == null ? new android.os.Bundle() : extras, mContext.getOpPackageName());
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#placeCall", e);
            }
        }
    }

    /**
     * Enables and disables specified phone account.
     *
     * @param handle
     * 		Handle to the phone account.
     * @param isEnabled
     * 		Enable state of the phone account.
     * @unknown 
     */
    @android.annotation.SystemApi
    public void enablePhoneAccount(android.telecom.PhoneAccountHandle handle, boolean isEnabled) {
        com.android.internal.telecom.ITelecomService service = getTelecomService();
        if (service != null) {
            try {
                service.enablePhoneAccount(handle, isEnabled);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.telecom.TelecomManager.TAG, "Error enablePhoneAbbount", e);
            }
        }
    }

    /**
     * Dumps telecom analytics for uploading.
     *
     * @return 
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.RequiresPermission(Manifest.permission.DUMP)
    public android.telecom.TelecomAnalytics dumpAnalytics() {
        com.android.internal.telecom.ITelecomService service = getTelecomService();
        android.telecom.TelecomAnalytics result = null;
        if (service != null) {
            try {
                result = service.dumpCallAnalytics();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.telecom.TelecomManager.TAG, "Error dumping call analytics", e);
            }
        }
        return result;
    }

    /**
     * Creates the {@link Intent} which can be used with {@link Context#startActivity(Intent)} to
     * launch the activity to manage blocked numbers.
     * <p> The activity will display the UI to manage blocked numbers only if
     * {@link android.provider.BlockedNumberContract#canCurrentUserBlockNumbers(Context)} returns
     * {@code true} for the current user.
     */
    public android.content.Intent createManageBlockedNumbersIntent() {
        com.android.internal.telecom.ITelecomService service = getTelecomService();
        android.content.Intent result = null;
        if (service != null) {
            try {
                result = service.createManageBlockedNumbersIntent();
            } catch (android.os.RemoteException e) {
                android.util.Log.e(android.telecom.TelecomManager.TAG, "Error calling ITelecomService#createManageBlockedNumbersIntent", e);
            }
        }
        return result;
    }

    private com.android.internal.telecom.ITelecomService getTelecomService() {
        if (mTelecomServiceOverride != null) {
            return mTelecomServiceOverride;
        }
        return ITelecomService.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.TELECOM_SERVICE));
    }

    private boolean isServiceConnected() {
        boolean isConnected = getTelecomService() != null;
        if (!isConnected) {
            android.util.Log.w(android.telecom.TelecomManager.TAG, "Telecom Service not found.");
        }
        return isConnected;
    }
}

