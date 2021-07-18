/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.provider;


/**
 * The CallLog provider contains information about placed and received calls.
 */
public class CallLog {
    private static final java.lang.String LOG_TAG = "CallLog";

    private static final boolean VERBOSE_LOG = false;// DON'T SUBMIT WITH TRUE.


    public static final java.lang.String AUTHORITY = "call_log";

    /**
     * The content:// style URL for this provider
     */
    public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://" + android.provider.CallLog.AUTHORITY);

    /**
     * The "shadow" provider stores calllog when the real calllog provider is encrypted.  The
     * real provider will alter copy from it when it starts, and remove the entries in the shadow.
     *
     * <p>See the comment in {@link Calls#addCall} for the details.
     *
     * @unknown 
     */
    public static final java.lang.String SHADOW_AUTHORITY = "call_log_shadow";

    /**
     * Contains the recent calls.
     */
    public static class Calls implements android.provider.BaseColumns {
        /**
         * The content:// style URL for this table
         */
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://call_log/calls");

        /**
         *
         *
         * @unknown 
         */
        public static final android.net.Uri SHADOW_CONTENT_URI = android.net.Uri.parse("content://call_log_shadow/calls");

        /**
         * The content:// style URL for filtering this table on phone numbers
         */
        public static final android.net.Uri CONTENT_FILTER_URI = android.net.Uri.parse("content://call_log/calls/filter");

        /**
         * Query parameter used to limit the number of call logs returned.
         * <p>
         * TYPE: integer
         */
        public static final java.lang.String LIMIT_PARAM_KEY = "limit";

        /**
         * Query parameter used to specify the starting record to return.
         * <p>
         * TYPE: integer
         */
        public static final java.lang.String OFFSET_PARAM_KEY = "offset";

        /**
         * An optional URI parameter which instructs the provider to allow the operation to be
         * applied to voicemail records as well.
         * <p>
         * TYPE: Boolean
         * <p>
         * Using this parameter with a value of {@code true} will result in a security error if the
         * calling package does not have appropriate permissions to access voicemails.
         *
         * @unknown 
         */
        public static final java.lang.String ALLOW_VOICEMAILS_PARAM_KEY = "allow_voicemails";

        /**
         * An optional extra used with {@link #CONTENT_TYPE Calls.CONTENT_TYPE} and
         * {@link Intent#ACTION_VIEW} to specify that the presented list of calls should be
         * filtered for a particular call type.
         *
         * Applications implementing a call log UI should check for this extra, and display a
         * filtered list of calls based on the specified call type. If not applicable within the
         * application's UI, it should be silently ignored.
         *
         * <p>
         * The following example brings up the call log, showing only missed calls.
         * <pre>
         * Intent intent = new Intent(Intent.ACTION_VIEW);
         * intent.setType(CallLog.Calls.CONTENT_TYPE);
         * intent.putExtra(CallLog.Calls.EXTRA_CALL_TYPE_FILTER, CallLog.Calls.MISSED_TYPE);
         * startActivity(intent);
         * </pre>
         * </p>
         */
        public static final java.lang.String EXTRA_CALL_TYPE_FILTER = "android.provider.extra.CALL_TYPE_FILTER";

        /**
         * Content uri used to access call log entries, including voicemail records. You must have
         * the READ_CALL_LOG and WRITE_CALL_LOG permissions to read and write to the call log, as
         * well as READ_VOICEMAIL and WRITE_VOICEMAIL permissions to read and write voicemails.
         */
        public static final android.net.Uri CONTENT_URI_WITH_VOICEMAIL = android.provider.CallLog.Calls.CONTENT_URI.buildUpon().appendQueryParameter(android.provider.CallLog.Calls.ALLOW_VOICEMAILS_PARAM_KEY, "true").build();

        /**
         * The default sort order for this table
         */
        public static final java.lang.String DEFAULT_SORT_ORDER = "date DESC";

        /**
         * The MIME type of {@link #CONTENT_URI} and {@link #CONTENT_FILTER_URI}
         * providing a directory of calls.
         */
        public static final java.lang.String CONTENT_TYPE = "vnd.android.cursor.dir/calls";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
         * call.
         */
        public static final java.lang.String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/calls";

        /**
         * The type of the call (incoming, outgoing or missed).
         * <P>Type: INTEGER (int)</P>
         *
         * <p>
         * Allowed values:
         * <ul>
         * <li>{@link #INCOMING_TYPE}</li>
         * <li>{@link #OUTGOING_TYPE}</li>
         * <li>{@link #MISSED_TYPE}</li>
         * <li>{@link #VOICEMAIL_TYPE}</li>
         * <li>{@link #REJECTED_TYPE}</li>
         * <li>{@link #BLOCKED_TYPE}</li>
         * <li>{@link #ANSWERED_EXTERNALLY_TYPE}</li>
         * </ul>
         * </p>
         */
        public static final java.lang.String TYPE = "type";

        /**
         * Call log type for incoming calls.
         */
        public static final int INCOMING_TYPE = 1;

        /**
         * Call log type for outgoing calls.
         */
        public static final int OUTGOING_TYPE = 2;

        /**
         * Call log type for missed calls.
         */
        public static final int MISSED_TYPE = 3;

        /**
         * Call log type for voicemails.
         */
        public static final int VOICEMAIL_TYPE = 4;

        /**
         * Call log type for calls rejected by direct user action.
         */
        public static final int REJECTED_TYPE = 5;

        /**
         * Call log type for calls blocked automatically.
         */
        public static final int BLOCKED_TYPE = 6;

        /**
         * Call log type for a call which was answered on another device.  Used in situations where
         * a call rings on multiple devices simultaneously and it ended up being answered on a
         * device other than the current one.
         */
        public static final int ANSWERED_EXTERNALLY_TYPE = 7;

        /**
         * Bit-mask describing features of the call (e.g. video).
         *
         * <P>Type: INTEGER (int)</P>
         */
        public static final java.lang.String FEATURES = "features";

        /**
         * Call had video.
         */
        public static final int FEATURES_VIDEO = 0x1;

        /**
         * Call was pulled externally.
         */
        public static final int FEATURES_PULLED_EXTERNALLY = 0x2;

        /**
         * The phone number as the user entered it.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String NUMBER = "number";

        /**
         * The number presenting rules set by the network.
         *
         * <p>
         * Allowed values:
         * <ul>
         * <li>{@link #PRESENTATION_ALLOWED}</li>
         * <li>{@link #PRESENTATION_RESTRICTED}</li>
         * <li>{@link #PRESENTATION_UNKNOWN}</li>
         * <li>{@link #PRESENTATION_PAYPHONE}</li>
         * </ul>
         * </p>
         *
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String NUMBER_PRESENTATION = "presentation";

        /**
         * Number is allowed to display for caller id.
         */
        public static final int PRESENTATION_ALLOWED = 1;

        /**
         * Number is blocked by user.
         */
        public static final int PRESENTATION_RESTRICTED = 2;

        /**
         * Number is not specified or unknown by network.
         */
        public static final int PRESENTATION_UNKNOWN = 3;

        /**
         * Number is a pay phone.
         */
        public static final int PRESENTATION_PAYPHONE = 4;

        /**
         * The ISO 3166-1 two letters country code of the country where the
         * user received or made the call.
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final java.lang.String COUNTRY_ISO = "countryiso";

        /**
         * The date the call occured, in milliseconds since the epoch
         * <P>Type: INTEGER (long)</P>
         */
        public static final java.lang.String DATE = "date";

        /**
         * The duration of the call in seconds
         * <P>Type: INTEGER (long)</P>
         */
        public static final java.lang.String DURATION = "duration";

        /**
         * The data usage of the call in bytes.
         * <P>Type: INTEGER (long)</P>
         */
        public static final java.lang.String DATA_USAGE = "data_usage";

        /**
         * Whether or not the call has been acknowledged
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final java.lang.String NEW = "new";

        /**
         * The cached name associated with the phone number, if it exists.
         *
         * <p>This value is typically filled in by the dialer app for the caching purpose,
         * so it's not guaranteed to be present, and may not be current if the contact
         * information associated with this number has changed.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CACHED_NAME = "name";

        /**
         * The cached number type (Home, Work, etc) associated with the
         * phone number, if it exists.
         *
         * <p>This value is typically filled in by the dialer app for the caching purpose,
         * so it's not guaranteed to be present, and may not be current if the contact
         * information associated with this number has changed.
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String CACHED_NUMBER_TYPE = "numbertype";

        /**
         * The cached number label, for a custom number type, associated with the
         * phone number, if it exists.
         *
         * <p>This value is typically filled in by the dialer app for the caching purpose,
         * so it's not guaranteed to be present, and may not be current if the contact
         * information associated with this number has changed.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CACHED_NUMBER_LABEL = "numberlabel";

        /**
         * URI of the voicemail entry. Populated only for {@link #VOICEMAIL_TYPE}.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String VOICEMAIL_URI = "voicemail_uri";

        /**
         * Transcription of the call or voicemail entry. This will only be populated for call log
         * entries of type {@link #VOICEMAIL_TYPE} that have valid transcriptions.
         */
        public static final java.lang.String TRANSCRIPTION = "transcription";

        /**
         * Whether this item has been read or otherwise consumed by the user.
         * <p>
         * Unlike the {@link #NEW} field, which requires the user to have acknowledged the
         * existence of the entry, this implies the user has interacted with the entry.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final java.lang.String IS_READ = "is_read";

        /**
         * A geocoded location for the number associated with this call.
         * <p>
         * The string represents a city, state, or country associated with the number.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String GEOCODED_LOCATION = "geocoded_location";

        /**
         * The cached URI to look up the contact associated with the phone number, if it exists.
         *
         * <p>This value is typically filled in by the dialer app for the caching purpose,
         * so it's not guaranteed to be present, and may not be current if the contact
         * information associated with this number has changed.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CACHED_LOOKUP_URI = "lookup_uri";

        /**
         * The cached phone number of the contact which matches this entry, if it exists.
         *
         * <p>This value is typically filled in by the dialer app for the caching purpose,
         * so it's not guaranteed to be present, and may not be current if the contact
         * information associated with this number has changed.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CACHED_MATCHED_NUMBER = "matched_number";

        /**
         * The cached normalized(E164) version of the phone number, if it exists.
         *
         * <p>This value is typically filled in by the dialer app for the caching purpose,
         * so it's not guaranteed to be present, and may not be current if the contact
         * information associated with this number has changed.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CACHED_NORMALIZED_NUMBER = "normalized_number";

        /**
         * The cached photo id of the picture associated with the phone number, if it exists.
         *
         * <p>This value is typically filled in by the dialer app for the caching purpose,
         * so it's not guaranteed to be present, and may not be current if the contact
         * information associated with this number has changed.
         * <P>Type: INTEGER (long)</P>
         */
        public static final java.lang.String CACHED_PHOTO_ID = "photo_id";

        /**
         * The cached photo URI of the picture associated with the phone number, if it exists.
         *
         * <p>This value is typically filled in by the dialer app for the caching purpose,
         * so it's not guaranteed to be present, and may not be current if the contact
         * information associated with this number has changed.
         * <P>Type: TEXT (URI)</P>
         */
        public static final java.lang.String CACHED_PHOTO_URI = "photo_uri";

        /**
         * The cached phone number, formatted with formatting rules based on the country the
         * user was in when the call was made or received.
         *
         * <p>This value is typically filled in by the dialer app for the caching purpose,
         * so it's not guaranteed to be present, and may not be current if the contact
         * information associated with this number has changed.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String CACHED_FORMATTED_NUMBER = "formatted_number";

        // Note: PHONE_ACCOUNT_* constant values are "subscription_*" due to a historic naming
        // that was encoded into call log databases.
        /**
         * The component name of the account used to place or receive the call; in string form.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String PHONE_ACCOUNT_COMPONENT_NAME = "subscription_component_name";

        /**
         * The identifier for the account used to place or receive the call.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String PHONE_ACCOUNT_ID = "subscription_id";

        /**
         * The address associated with the account used to place or receive the call; in string
         * form. For SIM-based calls, this is the user's own phone number.
         * <P>Type: TEXT</P>
         *
         * @unknown 
         */
        public static final java.lang.String PHONE_ACCOUNT_ADDRESS = "phone_account_address";

        /**
         * Indicates that the entry will be hidden from all queries until the associated
         * {@link android.telecom.PhoneAccount} is registered with the system.
         * <P>Type: INTEGER</P>
         *
         * @unknown 
         */
        public static final java.lang.String PHONE_ACCOUNT_HIDDEN = "phone_account_hidden";

        /**
         * The subscription ID used to place this call.  This is no longer used and has been
         * replaced with PHONE_ACCOUNT_COMPONENT_NAME/PHONE_ACCOUNT_ID.
         * For ContactsProvider internal use only.
         * <P>Type: INTEGER</P>
         *
         * @unknown 
         * @unknown 
         */
        public static final java.lang.String SUB_ID = "sub_id";

        /**
         * The post-dial portion of a dialed number, including any digits dialed after a
         * {@link TelecomManager#DTMF_CHARACTER_PAUSE} or a {@link TelecomManager#DTMF_CHARACTER_WAIT} and these characters themselves.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String POST_DIAL_DIGITS = "post_dial_digits";

        /**
         * For an incoming call, the secondary line number the call was received via.
         * When a SIM card has multiple phone numbers associated with it, the via number indicates
         * which of the numbers associated with the SIM was called.
         */
        public static final java.lang.String VIA_NUMBER = "via_number";

        /**
         * Indicates that the entry will be copied from primary user to other users.
         * <P>Type: INTEGER</P>
         *
         * @unknown 
         */
        public static final java.lang.String ADD_FOR_ALL_USERS = "add_for_all_users";

        /**
         * The date the row is last inserted, updated, or marked as deleted, in milliseconds
         * since the epoch. Read only.
         * <P>Type: INTEGER (long)</P>
         */
        public static final java.lang.String LAST_MODIFIED = "last_modified";

        /**
         * If a successful call is made that is longer than this duration, update the phone number
         * in the ContactsProvider with the normalized version of the number, based on the user's
         * current country code.
         */
        private static final int MIN_DURATION_FOR_NORMALIZED_NUMBER_UPDATE_MS = 1000 * 10;

        /**
         * Adds a call to the call log.
         *
         * @param ci
         * 		the CallerInfo object to get the target contact from.  Can be null
         * 		if the contact is unknown.
         * @param context
         * 		the context used to get the ContentResolver
         * @param number
         * 		the phone number to be added to the calls db
         * @param presentation
         * 		enum value from PhoneConstants.PRESENTATION_xxx, which
         * 		is set by the network and denotes the number presenting rules for
         * 		"allowed", "payphone", "restricted" or "unknown"
         * @param callType
         * 		enumerated values for "incoming", "outgoing", or "missed"
         * @param features
         * 		features of the call (e.g. Video).
         * @param accountHandle
         * 		The accountHandle object identifying the provider of the call
         * @param start
         * 		time stamp for the call in milliseconds
         * @param duration
         * 		call duration in seconds
         * @param dataUsage
         * 		data usage for the call in bytes, null if data usage was not tracked for
         * 		the call.
         * @unknown The URI of the call log entry belonging to the user that made or received this
        call.
        {@hide }
         */
        public static android.net.Uri addCall(com.android.internal.telephony.CallerInfo ci, android.content.Context context, java.lang.String number, int presentation, int callType, int features, android.telecom.PhoneAccountHandle accountHandle, long start, int duration, java.lang.Long dataUsage) {
            return /* postDialDigits = */
            /* viaNumber = */
            /* addForAllUsers = */
            /* userToBeInsertedTo = */
            /* is_read = */
            android.provider.CallLog.Calls.addCall(ci, context, number, "", "", presentation, callType, features, accountHandle, start, duration, dataUsage, false, null, false);
        }

        /**
         * Adds a call to the call log.
         *
         * @param ci
         * 		the CallerInfo object to get the target contact from.  Can be null
         * 		if the contact is unknown.
         * @param context
         * 		the context used to get the ContentResolver
         * @param number
         * 		the phone number to be added to the calls db
         * @param viaNumber
         * 		the secondary number that the incoming call received with. If the
         * 		call was received with the SIM assigned number, then this field must be ''.
         * @param presentation
         * 		enum value from PhoneConstants.PRESENTATION_xxx, which
         * 		is set by the network and denotes the number presenting rules for
         * 		"allowed", "payphone", "restricted" or "unknown"
         * @param callType
         * 		enumerated values for "incoming", "outgoing", or "missed"
         * @param features
         * 		features of the call (e.g. Video).
         * @param accountHandle
         * 		The accountHandle object identifying the provider of the call
         * @param start
         * 		time stamp for the call in milliseconds
         * @param duration
         * 		call duration in seconds
         * @param dataUsage
         * 		data usage for the call in bytes, null if data usage was not tracked for
         * 		the call.
         * @param addForAllUsers
         * 		If true, the call is added to the call log of all currently
         * 		running users. The caller must have the MANAGE_USERS permission if this is true.
         * @param userToBeInsertedTo
         * 		{@link UserHandle} of user that the call is going to be
         * 		inserted to. null if it is inserted to the current user. The
         * 		value is ignored if @{link addForAllUsers} is true.
         * @unknown The URI of the call log entry belonging to the user that made or received this
        call.
        {@hide }
         */
        public static android.net.Uri addCall(com.android.internal.telephony.CallerInfo ci, android.content.Context context, java.lang.String number, java.lang.String postDialDigits, java.lang.String viaNumber, int presentation, int callType, int features, android.telecom.PhoneAccountHandle accountHandle, long start, int duration, java.lang.Long dataUsage, boolean addForAllUsers, android.os.UserHandle userToBeInsertedTo) {
            return /* is_read = */
            android.provider.CallLog.Calls.addCall(ci, context, number, postDialDigits, viaNumber, presentation, callType, features, accountHandle, start, duration, dataUsage, addForAllUsers, userToBeInsertedTo, false);
        }

        /**
         * Adds a call to the call log.
         *
         * @param ci
         * 		the CallerInfo object to get the target contact from.  Can be null
         * 		if the contact is unknown.
         * @param context
         * 		the context used to get the ContentResolver
         * @param number
         * 		the phone number to be added to the calls db
         * @param postDialDigits
         * 		the post-dial digits that were dialed after the number,
         * 		if it was outgoing. Otherwise it is ''.
         * @param viaNumber
         * 		the secondary number that the incoming call received with. If the
         * 		call was received with the SIM assigned number, then this field must be ''.
         * @param presentation
         * 		enum value from PhoneConstants.PRESENTATION_xxx, which
         * 		is set by the network and denotes the number presenting rules for
         * 		"allowed", "payphone", "restricted" or "unknown"
         * @param callType
         * 		enumerated values for "incoming", "outgoing", or "missed"
         * @param features
         * 		features of the call (e.g. Video).
         * @param accountHandle
         * 		The accountHandle object identifying the provider of the call
         * @param start
         * 		time stamp for the call in milliseconds
         * @param duration
         * 		call duration in seconds
         * @param dataUsage
         * 		data usage for the call in bytes, null if data usage was not tracked for
         * 		the call.
         * @param addForAllUsers
         * 		If true, the call is added to the call log of all currently
         * 		running users. The caller must have the MANAGE_USERS permission if this is true.
         * @param userToBeInsertedTo
         * 		{@link UserHandle} of user that the call is going to be
         * 		inserted to. null if it is inserted to the current user. The
         * 		value is ignored if @{link addForAllUsers} is true.
         * @param is_read
         * 		Flag to show if the missed call log has been read by the user or not.
         * 		Used for call log restore of missed calls.
         * @unknown The URI of the call log entry belonging to the user that made or received this
        call.  This could be of the shadow provider.  Do not return it to non-system apps,
        as they don't have permissions.
        {@hide }
         */
        public static android.net.Uri addCall(com.android.internal.telephony.CallerInfo ci, android.content.Context context, java.lang.String number, java.lang.String postDialDigits, java.lang.String viaNumber, int presentation, int callType, int features, android.telecom.PhoneAccountHandle accountHandle, long start, int duration, java.lang.Long dataUsage, boolean addForAllUsers, android.os.UserHandle userToBeInsertedTo, boolean is_read) {
            if (android.provider.CallLog.VERBOSE_LOG) {
                android.util.Log.v(android.provider.CallLog.LOG_TAG, java.lang.String.format("Add call: number=%s, user=%s, for all=%s", number, userToBeInsertedTo, addForAllUsers));
            }
            final android.content.ContentResolver resolver = context.getContentResolver();
            int numberPresentation = android.provider.CallLog.Calls.PRESENTATION_ALLOWED;
            android.telecom.TelecomManager tm = null;
            try {
                tm = android.telecom.TelecomManager.from(context);
            } catch (java.lang.UnsupportedOperationException e) {
            }
            java.lang.String accountAddress = null;
            if ((tm != null) && (accountHandle != null)) {
                android.telecom.PhoneAccount account = tm.getPhoneAccount(accountHandle);
                if (account != null) {
                    android.net.Uri address = account.getSubscriptionAddress();
                    if (address != null) {
                        accountAddress = address.getSchemeSpecificPart();
                    }
                }
            }
            // Remap network specified number presentation types
            // PhoneConstants.PRESENTATION_xxx to calllog number presentation types
            // Calls.PRESENTATION_xxx, in order to insulate the persistent calllog
            // from any future radio changes.
            // If the number field is empty set the presentation type to Unknown.
            if (presentation == com.android.internal.telephony.PhoneConstants.PRESENTATION_RESTRICTED) {
                numberPresentation = android.provider.CallLog.Calls.PRESENTATION_RESTRICTED;
            } else
                if (presentation == com.android.internal.telephony.PhoneConstants.PRESENTATION_PAYPHONE) {
                    numberPresentation = android.provider.CallLog.Calls.PRESENTATION_PAYPHONE;
                } else
                    if (android.text.TextUtils.isEmpty(number) || (presentation == com.android.internal.telephony.PhoneConstants.PRESENTATION_UNKNOWN)) {
                        numberPresentation = android.provider.CallLog.Calls.PRESENTATION_UNKNOWN;
                    }


            if (numberPresentation != android.provider.CallLog.Calls.PRESENTATION_ALLOWED) {
                number = "";
                if (ci != null) {
                    ci.name = "";
                }
            }
            // accountHandle information
            java.lang.String accountComponentString = null;
            java.lang.String accountId = null;
            if (accountHandle != null) {
                accountComponentString = accountHandle.getComponentName().flattenToString();
                accountId = accountHandle.getId();
            }
            android.content.ContentValues values = new android.content.ContentValues(6);
            values.put(android.provider.CallLog.Calls.NUMBER, number);
            values.put(android.provider.CallLog.Calls.POST_DIAL_DIGITS, postDialDigits);
            values.put(android.provider.CallLog.Calls.VIA_NUMBER, viaNumber);
            values.put(android.provider.CallLog.Calls.NUMBER_PRESENTATION, java.lang.Integer.valueOf(numberPresentation));
            values.put(android.provider.CallLog.Calls.TYPE, java.lang.Integer.valueOf(callType));
            values.put(android.provider.CallLog.Calls.FEATURES, features);
            values.put(android.provider.CallLog.Calls.DATE, java.lang.Long.valueOf(start));
            values.put(android.provider.CallLog.Calls.DURATION, java.lang.Long.valueOf(duration));
            if (dataUsage != null) {
                values.put(android.provider.CallLog.Calls.DATA_USAGE, dataUsage);
            }
            values.put(android.provider.CallLog.Calls.PHONE_ACCOUNT_COMPONENT_NAME, accountComponentString);
            values.put(android.provider.CallLog.Calls.PHONE_ACCOUNT_ID, accountId);
            values.put(android.provider.CallLog.Calls.PHONE_ACCOUNT_ADDRESS, accountAddress);
            values.put(android.provider.CallLog.Calls.NEW, java.lang.Integer.valueOf(1));
            values.put(android.provider.CallLog.Calls.ADD_FOR_ALL_USERS, addForAllUsers ? 1 : 0);
            if (callType == android.provider.CallLog.Calls.MISSED_TYPE) {
                values.put(android.provider.CallLog.Calls.IS_READ, java.lang.Integer.valueOf(is_read ? 1 : 0));
            }
            if ((ci != null) && (ci.contactIdOrZero > 0)) {
                // Update usage information for the number associated with the contact ID.
                // We need to use both the number and the ID for obtaining a data ID since other
                // contacts may have the same number.
                final android.database.Cursor cursor;
                // We should prefer normalized one (probably coming from
                // Phone.NORMALIZED_NUMBER column) first. If it isn't available try others.
                if (ci.normalizedNumber != null) {
                    final java.lang.String normalizedPhoneNumber = ci.normalizedNumber;
                    cursor = resolver.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new java.lang.String[]{ android.provider.ContactsContract.CommonDataKinds.Phone._ID }, ((android.provider.ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =? AND ") + android.provider.ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER) + " =?", new java.lang.String[]{ java.lang.String.valueOf(ci.contactIdOrZero), normalizedPhoneNumber }, null);
                } else {
                    final java.lang.String phoneNumber = (ci.phoneNumber != null) ? ci.phoneNumber : number;
                    cursor = resolver.query(android.net.Uri.withAppendedPath(android.provider.ContactsContract.CommonDataKinds.Callable.CONTENT_FILTER_URI, android.net.Uri.encode(phoneNumber)), new java.lang.String[]{ android.provider.ContactsContract.CommonDataKinds.Phone._ID }, android.provider.ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?", new java.lang.String[]{ java.lang.String.valueOf(ci.contactIdOrZero) }, null);
                }
                if (cursor != null) {
                    try {
                        if ((cursor.getCount() > 0) && cursor.moveToFirst()) {
                            final java.lang.String dataId = cursor.getString(0);
                            android.provider.CallLog.Calls.updateDataUsageStatForData(resolver, dataId);
                            if (((duration >= android.provider.CallLog.Calls.MIN_DURATION_FOR_NORMALIZED_NUMBER_UPDATE_MS) && (callType == android.provider.CallLog.Calls.OUTGOING_TYPE)) && android.text.TextUtils.isEmpty(ci.normalizedNumber)) {
                                android.provider.CallLog.Calls.updateNormalizedNumber(context, resolver, dataId, number);
                            }
                        }
                    } finally {
                        cursor.close();
                    }
                }
            }
            /* Writing the calllog works in the following way:
            - All user entries
            - if user-0 is encrypted, insert to user-0's shadow only.
            (other users should also be encrypted, so nothing to do for other users.)
            - if user-0 is decrypted, insert to user-0's real provider, as well as
            all other users that are running and decrypted and should have calllog.

            - Single user entry.
            - If the target user is encryted, insert to its shadow.
            - Otherwise insert to its real provider.

            When the (real) calllog provider starts, it copies entries that it missed from
            elsewhere.
            - When user-0's (real) provider starts, it copies from user-0's shadow, and clears
            the shadow.

            - When other users (real) providers start, unless it shouldn't have calllog entries,
            - Copy from the user's shadow, and clears the shadow.
            - Copy from user-0's entries that are FOR_ALL_USERS = 1.  (and don't clear it.)
             */
            android.net.Uri result = null;
            final android.os.UserManager userManager = context.getSystemService(android.os.UserManager.class);
            final int currentUserId = userManager.getUserHandle();
            if (addForAllUsers) {
                // First, insert to the system user.
                final android.net.Uri uriForSystem = android.provider.CallLog.Calls.addEntryAndRemoveExpiredEntries(context, userManager, android.os.UserHandle.SYSTEM, values);
                if ((uriForSystem == null) || android.provider.CallLog.SHADOW_AUTHORITY.equals(uriForSystem.getAuthority())) {
                    // This means the system user is still encrypted and the entry has inserted
                    // into the shadow.  This means other users are still all encrypted.
                    // Nothing further to do; just return null.
                    return null;
                }
                if (android.os.UserHandle.USER_SYSTEM == currentUserId) {
                    result = uriForSystem;
                }
                // Otherwise, insert to all other users that are running and unlocked.
                final java.util.List<android.content.pm.UserInfo> users = userManager.getUsers(true);
                final int count = users.size();
                for (int i = 0; i < count; i++) {
                    final android.content.pm.UserInfo userInfo = users.get(i);
                    final android.os.UserHandle userHandle = userInfo.getUserHandle();
                    final int userId = userHandle.getIdentifier();
                    if (userHandle.isSystem()) {
                        // Already written.
                        continue;
                    }
                    if (!android.provider.CallLog.Calls.shouldHaveSharedCallLogEntries(context, userManager, userId)) {
                        // Shouldn't have calllog entries.
                        continue;
                    }
                    // For other users, we write only when they're running *and* decrypted.
                    // Other providers will copy from the system user's real provider, when they
                    // start.
                    if (userManager.isUserRunning(userHandle) && userManager.isUserUnlocked(userHandle)) {
                        final android.net.Uri uri = android.provider.CallLog.Calls.addEntryAndRemoveExpiredEntries(context, userManager, userHandle, values);
                        if (userId == currentUserId) {
                            result = uri;
                        }
                    }
                }
            } else {
                // Single-user entry. Just write to that user, assuming it's running.  If the
                // user is encrypted, we write to the shadow calllog.
                final android.os.UserHandle targetUserHandle = (userToBeInsertedTo != null) ? userToBeInsertedTo : android.os.UserHandle.of(currentUserId);
                result = android.provider.CallLog.Calls.addEntryAndRemoveExpiredEntries(context, userManager, targetUserHandle, values);
            }
            return result;
        }

        /**
         *
         *
         * @unknown 
         */
        public static boolean shouldHaveSharedCallLogEntries(android.content.Context context, android.os.UserManager userManager, int userId) {
            if (userManager.hasUserRestriction(android.os.UserManager.DISALLOW_OUTGOING_CALLS, android.os.UserHandle.of(userId))) {
                return false;
            }
            final android.content.pm.UserInfo userInfo = userManager.getUserInfo(userId);
            return (userInfo != null) && (!userInfo.isManagedProfile());
        }

        /**
         * Query the call log database for the last dialed number.
         *
         * @param context
         * 		Used to get the content resolver.
         * @return The last phone number dialed (outgoing) or an empty
        string if none exist yet.
         */
        public static java.lang.String getLastOutgoingCall(android.content.Context context) {
            final android.content.ContentResolver resolver = context.getContentResolver();
            android.database.Cursor c = null;
            try {
                c = resolver.query(android.provider.CallLog.Calls.CONTENT_URI, new java.lang.String[]{ android.provider.CallLog.Calls.NUMBER }, (android.provider.CallLog.Calls.TYPE + " = ") + android.provider.CallLog.Calls.OUTGOING_TYPE, null, android.provider.CallLog.Calls.DEFAULT_SORT_ORDER + " LIMIT 1");
                if ((c == null) || (!c.moveToFirst())) {
                    return "";
                }
                return c.getString(0);
            } finally {
                if (c != null)
                    c.close();

            }
        }

        private static android.net.Uri addEntryAndRemoveExpiredEntries(android.content.Context context, android.os.UserManager userManager, android.os.UserHandle user, android.content.ContentValues values) {
            final android.content.ContentResolver resolver = context.getContentResolver();
            // Since we're doing this operation on behalf of an app, we only
            // want to use the actual "unlocked" state.
            final android.net.Uri uri = android.content.ContentProvider.maybeAddUserId(userManager.isUserUnlocked(user) ? android.provider.CallLog.Calls.CONTENT_URI : android.provider.CallLog.Calls.SHADOW_CONTENT_URI, user.getIdentifier());
            if (android.provider.CallLog.VERBOSE_LOG) {
                android.util.Log.v(android.provider.CallLog.LOG_TAG, java.lang.String.format("Inserting to %s", uri));
            }
            try {
                final android.net.Uri result = resolver.insert(uri, values);
                resolver.delete(uri, (("_id IN " + "(SELECT _id FROM calls ORDER BY ") + android.provider.CallLog.Calls.DEFAULT_SORT_ORDER) + " LIMIT -1 OFFSET 500)", null);
                return result;
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Log.w(android.provider.CallLog.LOG_TAG, "Failed to insert calllog", e);
                // Even though we make sure the target user is running and decrypted before calling
                // this method, there's a chance that the user just got shut down, in which case
                // we'll still get "IllegalArgumentException: Unknown URL content://call_log/calls".
                return null;
            }
        }

        private static void updateDataUsageStatForData(android.content.ContentResolver resolver, java.lang.String dataId) {
            final android.net.Uri feedbackUri = android.provider.ContactsContract.DataUsageFeedback.FEEDBACK_URI.buildUpon().appendPath(dataId).appendQueryParameter(android.provider.ContactsContract.DataUsageFeedback.USAGE_TYPE, android.provider.ContactsContract.DataUsageFeedback.USAGE_TYPE_CALL).build();
            resolver.update(feedbackUri, new android.content.ContentValues(), null, null);
        }

        /* Update the normalized phone number for the given dataId in the ContactsProvider, based
        on the user's current country.
         */
        private static void updateNormalizedNumber(android.content.Context context, android.content.ContentResolver resolver, java.lang.String dataId, java.lang.String number) {
            if (android.text.TextUtils.isEmpty(number) || android.text.TextUtils.isEmpty(dataId)) {
                return;
            }
            final java.lang.String countryIso = android.provider.CallLog.Calls.getCurrentCountryIso(context);
            if (android.text.TextUtils.isEmpty(countryIso)) {
                return;
            }
            final java.lang.String normalizedNumber = android.telephony.PhoneNumberUtils.formatNumberToE164(number, android.provider.CallLog.Calls.getCurrentCountryIso(context));
            if (android.text.TextUtils.isEmpty(normalizedNumber)) {
                return;
            }
            final android.content.ContentValues values = new android.content.ContentValues();
            values.put(android.provider.ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER, normalizedNumber);
            resolver.update(android.provider.ContactsContract.Data.CONTENT_URI, values, android.provider.ContactsContract.Data._ID + "=?", new java.lang.String[]{ dataId });
        }

        private static java.lang.String getCurrentCountryIso(android.content.Context context) {
            java.lang.String countryIso = null;
            final android.location.CountryDetector detector = ((android.location.CountryDetector) (context.getSystemService(android.content.Context.COUNTRY_DETECTOR)));
            if (detector != null) {
                final android.location.Country country = detector.detectCountry();
                if (country != null) {
                    countryIso = country.getCountryIso();
                }
            }
            return countryIso;
        }
    }
}

