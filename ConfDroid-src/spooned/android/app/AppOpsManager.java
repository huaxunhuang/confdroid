/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.app;


/**
 * API for interacting with "application operation" tracking.
 *
 * <p>This API is not generally intended for third party application developers; most
 * features are only available to system applications.  Obtain an instance of it through
 * {@link Context#getSystemService(String) Context.getSystemService} with
 * {@link Context#APP_OPS_SERVICE Context.APP_OPS_SERVICE}.</p>
 */
public class AppOpsManager {
    /**
     * <p>App ops allows callers to:</p>
     *
     * <ul>
     * <li> Note when operations are happening, and find out if they are allowed for the current
     * caller.</li>
     * <li> Disallow specific apps from doing specific operations.</li>
     * <li> Collect all of the current information about operations that have been executed or
     * are not being allowed.</li>
     * <li> Monitor for changes in whether an operation is allowed.</li>
     * </ul>
     *
     * <p>Each operation is identified by a single integer; these integers are a fixed set of
     * operations, enumerated by the OP_* constants.
     *
     * <p></p>When checking operations, the result is a "mode" integer indicating the current
     * setting for the operation under that caller: MODE_ALLOWED, MODE_IGNORED (don't execute
     * the operation but fake its behavior enough so that the caller doesn't crash),
     * MODE_ERRORED (throw a SecurityException back to the caller; the normal operation calls
     * will do this for you).
     */
    final android.content.Context mContext;

    final com.android.internal.app.IAppOpsService mService;

    final android.util.ArrayMap<android.app.AppOpsManager.OnOpChangedListener, com.android.internal.app.IAppOpsCallback> mModeWatchers = new android.util.ArrayMap<android.app.AppOpsManager.OnOpChangedListener, com.android.internal.app.IAppOpsCallback>();

    static android.os.IBinder sToken;

    /**
     * Result from {@link #checkOp}, {@link #noteOp}, {@link #startOp}: the given caller is
     * allowed to perform the given operation.
     */
    public static final int MODE_ALLOWED = 0;

    /**
     * Result from {@link #checkOp}, {@link #noteOp}, {@link #startOp}: the given caller is
     * not allowed to perform the given operation, and this attempt should
     * <em>silently fail</em> (it should not cause the app to crash).
     */
    public static final int MODE_IGNORED = 1;

    /**
     * Result from {@link #checkOpNoThrow}, {@link #noteOpNoThrow}, {@link #startOpNoThrow}: the
     * given caller is not allowed to perform the given operation, and this attempt should
     * cause it to have a fatal error, typically a {@link SecurityException}.
     */
    public static final int MODE_ERRORED = 2;

    /**
     * Result from {@link #checkOp}, {@link #noteOp}, {@link #startOp}: the given caller should
     * use its default security check.  This mode is not normally used; it should only be used
     * with appop permissions, and callers must explicitly check for it and deal with it.
     */
    public static final int MODE_DEFAULT = 3;

    // when adding one of these:
    // - increment _NUM_OP
    // - add rows to sOpToSwitch, sOpToString, sOpNames, sOpToPerms, sOpDefault
    // - add descriptive strings to Settings/res/values/arrays.xml
    // - add the op to the appropriate template in AppOpsState.OpsTemplate (settings app)
    /**
     *
     *
     * @unknown No operation specified.
     */
    public static final int OP_NONE = -1;

    /**
     *
     *
     * @unknown Access to coarse location information.
     */
    public static final int OP_COARSE_LOCATION = 0;

    /**
     *
     *
     * @unknown Access to fine location information.
     */
    public static final int OP_FINE_LOCATION = 1;

    /**
     *
     *
     * @unknown Causing GPS to run.
     */
    public static final int OP_GPS = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_VIBRATE = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_READ_CONTACTS = 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_WRITE_CONTACTS = 5;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_READ_CALL_LOG = 6;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_WRITE_CALL_LOG = 7;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_READ_CALENDAR = 8;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_WRITE_CALENDAR = 9;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_WIFI_SCAN = 10;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_POST_NOTIFICATION = 11;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_NEIGHBORING_CELLS = 12;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_CALL_PHONE = 13;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_READ_SMS = 14;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_WRITE_SMS = 15;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_RECEIVE_SMS = 16;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_RECEIVE_EMERGECY_SMS = 17;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_RECEIVE_MMS = 18;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_RECEIVE_WAP_PUSH = 19;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_SEND_SMS = 20;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_READ_ICC_SMS = 21;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_WRITE_ICC_SMS = 22;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_WRITE_SETTINGS = 23;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_SYSTEM_ALERT_WINDOW = 24;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_ACCESS_NOTIFICATIONS = 25;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_CAMERA = 26;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_RECORD_AUDIO = 27;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_PLAY_AUDIO = 28;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_READ_CLIPBOARD = 29;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_WRITE_CLIPBOARD = 30;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_TAKE_MEDIA_BUTTONS = 31;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_TAKE_AUDIO_FOCUS = 32;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_AUDIO_MASTER_VOLUME = 33;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_AUDIO_VOICE_VOLUME = 34;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_AUDIO_RING_VOLUME = 35;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_AUDIO_MEDIA_VOLUME = 36;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_AUDIO_ALARM_VOLUME = 37;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_AUDIO_NOTIFICATION_VOLUME = 38;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_AUDIO_BLUETOOTH_VOLUME = 39;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_WAKE_LOCK = 40;

    /**
     *
     *
     * @unknown Continually monitoring location data.
     */
    public static final int OP_MONITOR_LOCATION = 41;

    /**
     *
     *
     * @unknown Continually monitoring location data with a relatively high power request.
     */
    public static final int OP_MONITOR_HIGH_POWER_LOCATION = 42;

    /**
     *
     *
     * @unknown Retrieve current usage stats via {@link UsageStatsManager}.
     */
    public static final int OP_GET_USAGE_STATS = 43;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_MUTE_MICROPHONE = 44;

    /**
     *
     *
     * @unknown 
     */
    public static final int OP_TOAST_WINDOW = 45;

    /**
     *
     *
     * @unknown Capture the device's display contents and/or audio
     */
    public static final int OP_PROJECT_MEDIA = 46;

    /**
     *
     *
     * @unknown Activate a VPN connection without user intervention.
     */
    public static final int OP_ACTIVATE_VPN = 47;

    /**
     *
     *
     * @unknown Access the WallpaperManagerAPI to write wallpapers.
     */
    public static final int OP_WRITE_WALLPAPER = 48;

    /**
     *
     *
     * @unknown Received the assist structure from an app.
     */
    public static final int OP_ASSIST_STRUCTURE = 49;

    /**
     *
     *
     * @unknown Received a screenshot from assist.
     */
    public static final int OP_ASSIST_SCREENSHOT = 50;

    /**
     *
     *
     * @unknown Read the phone state.
     */
    public static final int OP_READ_PHONE_STATE = 51;

    /**
     *
     *
     * @unknown Add voicemail messages to the voicemail content provider.
     */
    public static final int OP_ADD_VOICEMAIL = 52;

    /**
     *
     *
     * @unknown Access APIs for SIP calling over VOIP or WiFi.
     */
    public static final int OP_USE_SIP = 53;

    /**
     *
     *
     * @unknown Intercept outgoing calls.
     */
    public static final int OP_PROCESS_OUTGOING_CALLS = 54;

    /**
     *
     *
     * @unknown User the fingerprint API.
     */
    public static final int OP_USE_FINGERPRINT = 55;

    /**
     *
     *
     * @unknown Access to body sensors such as heart rate, etc.
     */
    public static final int OP_BODY_SENSORS = 56;

    /**
     *
     *
     * @unknown Read previously received cell broadcast messages.
     */
    public static final int OP_READ_CELL_BROADCASTS = 57;

    /**
     *
     *
     * @unknown Inject mock location into the system.
     */
    public static final int OP_MOCK_LOCATION = 58;

    /**
     *
     *
     * @unknown Read external storage.
     */
    public static final int OP_READ_EXTERNAL_STORAGE = 59;

    /**
     *
     *
     * @unknown Write external storage.
     */
    public static final int OP_WRITE_EXTERNAL_STORAGE = 60;

    /**
     *
     *
     * @unknown Turned on the screen.
     */
    public static final int OP_TURN_SCREEN_ON = 61;

    /**
     *
     *
     * @unknown Get device accounts.
     */
    public static final int OP_GET_ACCOUNTS = 62;

    /**
     *
     *
     * @unknown Control whether an application is allowed to run in the background.
     */
    public static final int OP_RUN_IN_BACKGROUND = 63;

    /**
     *
     *
     * @unknown 
     */
    public static final int _NUM_OP = 64;

    /**
     * Access to coarse location information.
     */
    public static final java.lang.String OPSTR_COARSE_LOCATION = "android:coarse_location";

    /**
     * Access to fine location information.
     */
    public static final java.lang.String OPSTR_FINE_LOCATION = "android:fine_location";

    /**
     * Continually monitoring location data.
     */
    public static final java.lang.String OPSTR_MONITOR_LOCATION = "android:monitor_location";

    /**
     * Continually monitoring location data with a relatively high power request.
     */
    public static final java.lang.String OPSTR_MONITOR_HIGH_POWER_LOCATION = "android:monitor_location_high_power";

    /**
     * Access to {@link android.app.usage.UsageStatsManager}.
     */
    public static final java.lang.String OPSTR_GET_USAGE_STATS = "android:get_usage_stats";

    /**
     * Activate a VPN connection without user intervention. @hide
     */
    @android.annotation.SystemApi
    public static final java.lang.String OPSTR_ACTIVATE_VPN = "android:activate_vpn";

    /**
     * Allows an application to read the user's contacts data.
     */
    public static final java.lang.String OPSTR_READ_CONTACTS = "android:read_contacts";

    /**
     * Allows an application to write to the user's contacts data.
     */
    public static final java.lang.String OPSTR_WRITE_CONTACTS = "android:write_contacts";

    /**
     * Allows an application to read the user's call log.
     */
    public static final java.lang.String OPSTR_READ_CALL_LOG = "android:read_call_log";

    /**
     * Allows an application to write to the user's call log.
     */
    public static final java.lang.String OPSTR_WRITE_CALL_LOG = "android:write_call_log";

    /**
     * Allows an application to read the user's calendar data.
     */
    public static final java.lang.String OPSTR_READ_CALENDAR = "android:read_calendar";

    /**
     * Allows an application to write to the user's calendar data.
     */
    public static final java.lang.String OPSTR_WRITE_CALENDAR = "android:write_calendar";

    /**
     * Allows an application to initiate a phone call.
     */
    public static final java.lang.String OPSTR_CALL_PHONE = "android:call_phone";

    /**
     * Allows an application to read SMS messages.
     */
    public static final java.lang.String OPSTR_READ_SMS = "android:read_sms";

    /**
     * Allows an application to receive SMS messages.
     */
    public static final java.lang.String OPSTR_RECEIVE_SMS = "android:receive_sms";

    /**
     * Allows an application to receive MMS messages.
     */
    public static final java.lang.String OPSTR_RECEIVE_MMS = "android:receive_mms";

    /**
     * Allows an application to receive WAP push messages.
     */
    public static final java.lang.String OPSTR_RECEIVE_WAP_PUSH = "android:receive_wap_push";

    /**
     * Allows an application to send SMS messages.
     */
    public static final java.lang.String OPSTR_SEND_SMS = "android:send_sms";

    /**
     * Required to be able to access the camera device.
     */
    public static final java.lang.String OPSTR_CAMERA = "android:camera";

    /**
     * Required to be able to access the microphone device.
     */
    public static final java.lang.String OPSTR_RECORD_AUDIO = "android:record_audio";

    /**
     * Required to access phone state related information.
     */
    public static final java.lang.String OPSTR_READ_PHONE_STATE = "android:read_phone_state";

    /**
     * Required to access phone state related information.
     */
    public static final java.lang.String OPSTR_ADD_VOICEMAIL = "android:add_voicemail";

    /**
     * Access APIs for SIP calling over VOIP or WiFi
     */
    public static final java.lang.String OPSTR_USE_SIP = "android:use_sip";

    /**
     * Use the fingerprint API.
     */
    public static final java.lang.String OPSTR_USE_FINGERPRINT = "android:use_fingerprint";

    /**
     * Access to body sensors such as heart rate, etc.
     */
    public static final java.lang.String OPSTR_BODY_SENSORS = "android:body_sensors";

    /**
     * Read previously received cell broadcast messages.
     */
    public static final java.lang.String OPSTR_READ_CELL_BROADCASTS = "android:read_cell_broadcasts";

    /**
     * Inject mock location into the system.
     */
    public static final java.lang.String OPSTR_MOCK_LOCATION = "android:mock_location";

    /**
     * Read external storage.
     */
    public static final java.lang.String OPSTR_READ_EXTERNAL_STORAGE = "android:read_external_storage";

    /**
     * Write external storage.
     */
    public static final java.lang.String OPSTR_WRITE_EXTERNAL_STORAGE = "android:write_external_storage";

    /**
     * Required to draw on top of other apps.
     */
    public static final java.lang.String OPSTR_SYSTEM_ALERT_WINDOW = "android:system_alert_window";

    /**
     * Required to write/modify/update system settingss.
     */
    public static final java.lang.String OPSTR_WRITE_SETTINGS = "android:write_settings";

    /**
     *
     *
     * @unknown Get device accounts.
     */
    public static final java.lang.String OPSTR_GET_ACCOUNTS = "android:get_accounts";

    private static final int[] RUNTIME_PERMISSIONS_OPS = new int[]{ // Contacts
    android.app.AppOpsManager.OP_READ_CONTACTS, android.app.AppOpsManager.OP_WRITE_CONTACTS, android.app.AppOpsManager.OP_GET_ACCOUNTS, // Calendar
    android.app.AppOpsManager.OP_READ_CALENDAR, android.app.AppOpsManager.OP_WRITE_CALENDAR, // SMS
    android.app.AppOpsManager.OP_SEND_SMS, android.app.AppOpsManager.OP_RECEIVE_SMS, android.app.AppOpsManager.OP_READ_SMS, android.app.AppOpsManager.OP_RECEIVE_WAP_PUSH, android.app.AppOpsManager.OP_RECEIVE_MMS, android.app.AppOpsManager.OP_READ_CELL_BROADCASTS, // Storage
    android.app.AppOpsManager.OP_READ_EXTERNAL_STORAGE, android.app.AppOpsManager.OP_WRITE_EXTERNAL_STORAGE, // Location
    android.app.AppOpsManager.OP_COARSE_LOCATION, android.app.AppOpsManager.OP_FINE_LOCATION, // Phone
    android.app.AppOpsManager.OP_READ_PHONE_STATE, android.app.AppOpsManager.OP_CALL_PHONE, android.app.AppOpsManager.OP_READ_CALL_LOG, android.app.AppOpsManager.OP_WRITE_CALL_LOG, android.app.AppOpsManager.OP_ADD_VOICEMAIL, android.app.AppOpsManager.OP_USE_SIP, android.app.AppOpsManager.OP_PROCESS_OUTGOING_CALLS, // Microphone
    android.app.AppOpsManager.OP_RECORD_AUDIO, // Camera
    android.app.AppOpsManager.OP_CAMERA, // Body sensors
    android.app.AppOpsManager.OP_BODY_SENSORS };

    /**
     * This maps each operation to the operation that serves as the
     * switch to determine whether it is allowed.  Generally this is
     * a 1:1 mapping, but for some things (like location) that have
     * multiple low-level operations being tracked that should be
     * presented to the user as one switch then this can be used to
     * make them all controlled by the same single operation.
     */
    private static int[] sOpToSwitch = new int[]{ android.app.AppOpsManager.OP_COARSE_LOCATION, android.app.AppOpsManager.OP_COARSE_LOCATION, android.app.AppOpsManager.OP_COARSE_LOCATION, android.app.AppOpsManager.OP_VIBRATE, android.app.AppOpsManager.OP_READ_CONTACTS, android.app.AppOpsManager.OP_WRITE_CONTACTS, android.app.AppOpsManager.OP_READ_CALL_LOG, android.app.AppOpsManager.OP_WRITE_CALL_LOG, android.app.AppOpsManager.OP_READ_CALENDAR, android.app.AppOpsManager.OP_WRITE_CALENDAR, android.app.AppOpsManager.OP_COARSE_LOCATION, android.app.AppOpsManager.OP_POST_NOTIFICATION, android.app.AppOpsManager.OP_COARSE_LOCATION, android.app.AppOpsManager.OP_CALL_PHONE, android.app.AppOpsManager.OP_READ_SMS, android.app.AppOpsManager.OP_WRITE_SMS, android.app.AppOpsManager.OP_RECEIVE_SMS, android.app.AppOpsManager.OP_RECEIVE_SMS, android.app.AppOpsManager.OP_RECEIVE_SMS, android.app.AppOpsManager.OP_RECEIVE_SMS, android.app.AppOpsManager.OP_SEND_SMS, android.app.AppOpsManager.OP_READ_SMS, android.app.AppOpsManager.OP_WRITE_SMS, android.app.AppOpsManager.OP_WRITE_SETTINGS, android.app.AppOpsManager.OP_SYSTEM_ALERT_WINDOW, android.app.AppOpsManager.OP_ACCESS_NOTIFICATIONS, android.app.AppOpsManager.OP_CAMERA, android.app.AppOpsManager.OP_RECORD_AUDIO, android.app.AppOpsManager.OP_PLAY_AUDIO, android.app.AppOpsManager.OP_READ_CLIPBOARD, android.app.AppOpsManager.OP_WRITE_CLIPBOARD, android.app.AppOpsManager.OP_TAKE_MEDIA_BUTTONS, android.app.AppOpsManager.OP_TAKE_AUDIO_FOCUS, android.app.AppOpsManager.OP_AUDIO_MASTER_VOLUME, android.app.AppOpsManager.OP_AUDIO_VOICE_VOLUME, android.app.AppOpsManager.OP_AUDIO_RING_VOLUME, android.app.AppOpsManager.OP_AUDIO_MEDIA_VOLUME, android.app.AppOpsManager.OP_AUDIO_ALARM_VOLUME, android.app.AppOpsManager.OP_AUDIO_NOTIFICATION_VOLUME, android.app.AppOpsManager.OP_AUDIO_BLUETOOTH_VOLUME, android.app.AppOpsManager.OP_WAKE_LOCK, android.app.AppOpsManager.OP_COARSE_LOCATION, android.app.AppOpsManager.OP_COARSE_LOCATION, android.app.AppOpsManager.OP_GET_USAGE_STATS, android.app.AppOpsManager.OP_MUTE_MICROPHONE, android.app.AppOpsManager.OP_TOAST_WINDOW, android.app.AppOpsManager.OP_PROJECT_MEDIA, android.app.AppOpsManager.OP_ACTIVATE_VPN, android.app.AppOpsManager.OP_WRITE_WALLPAPER, android.app.AppOpsManager.OP_ASSIST_STRUCTURE, android.app.AppOpsManager.OP_ASSIST_SCREENSHOT, android.app.AppOpsManager.OP_READ_PHONE_STATE, android.app.AppOpsManager.OP_ADD_VOICEMAIL, android.app.AppOpsManager.OP_USE_SIP, android.app.AppOpsManager.OP_PROCESS_OUTGOING_CALLS, android.app.AppOpsManager.OP_USE_FINGERPRINT, android.app.AppOpsManager.OP_BODY_SENSORS, android.app.AppOpsManager.OP_READ_CELL_BROADCASTS, android.app.AppOpsManager.OP_MOCK_LOCATION, android.app.AppOpsManager.OP_READ_EXTERNAL_STORAGE, android.app.AppOpsManager.OP_WRITE_EXTERNAL_STORAGE, android.app.AppOpsManager.OP_TURN_SCREEN_ON, android.app.AppOpsManager.OP_GET_ACCOUNTS, android.app.AppOpsManager.OP_RUN_IN_BACKGROUND };

    /**
     * This maps each operation to the public string constant for it.
     * If it doesn't have a public string constant, it maps to null.
     */
    private static java.lang.String[] sOpToString = new java.lang.String[]{ android.app.AppOpsManager.OPSTR_COARSE_LOCATION, android.app.AppOpsManager.OPSTR_FINE_LOCATION, null, null, android.app.AppOpsManager.OPSTR_READ_CONTACTS, android.app.AppOpsManager.OPSTR_WRITE_CONTACTS, android.app.AppOpsManager.OPSTR_READ_CALL_LOG, android.app.AppOpsManager.OPSTR_WRITE_CALL_LOG, android.app.AppOpsManager.OPSTR_READ_CALENDAR, android.app.AppOpsManager.OPSTR_WRITE_CALENDAR, null, null, null, android.app.AppOpsManager.OPSTR_CALL_PHONE, android.app.AppOpsManager.OPSTR_READ_SMS, null, android.app.AppOpsManager.OPSTR_RECEIVE_SMS, null, android.app.AppOpsManager.OPSTR_RECEIVE_MMS, android.app.AppOpsManager.OPSTR_RECEIVE_WAP_PUSH, android.app.AppOpsManager.OPSTR_SEND_SMS, null, null, android.app.AppOpsManager.OPSTR_WRITE_SETTINGS, android.app.AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW, null, android.app.AppOpsManager.OPSTR_CAMERA, android.app.AppOpsManager.OPSTR_RECORD_AUDIO, null, null, null, null, null, null, null, null, null, null, null, null, null, android.app.AppOpsManager.OPSTR_MONITOR_LOCATION, android.app.AppOpsManager.OPSTR_MONITOR_HIGH_POWER_LOCATION, android.app.AppOpsManager.OPSTR_GET_USAGE_STATS, null, null, null, android.app.AppOpsManager.OPSTR_ACTIVATE_VPN, null, null, null, android.app.AppOpsManager.OPSTR_READ_PHONE_STATE, android.app.AppOpsManager.OPSTR_ADD_VOICEMAIL, android.app.AppOpsManager.OPSTR_USE_SIP, null, android.app.AppOpsManager.OPSTR_USE_FINGERPRINT, android.app.AppOpsManager.OPSTR_BODY_SENSORS, android.app.AppOpsManager.OPSTR_READ_CELL_BROADCASTS, android.app.AppOpsManager.OPSTR_MOCK_LOCATION, android.app.AppOpsManager.OPSTR_READ_EXTERNAL_STORAGE, android.app.AppOpsManager.OPSTR_WRITE_EXTERNAL_STORAGE, null, android.app.AppOpsManager.OPSTR_GET_ACCOUNTS, null };

    /**
     * This provides a simple name for each operation to be used
     * in debug output.
     */
    private static java.lang.String[] sOpNames = new java.lang.String[]{ "COARSE_LOCATION", "FINE_LOCATION", "GPS", "VIBRATE", "READ_CONTACTS", "WRITE_CONTACTS", "READ_CALL_LOG", "WRITE_CALL_LOG", "READ_CALENDAR", "WRITE_CALENDAR", "WIFI_SCAN", "POST_NOTIFICATION", "NEIGHBORING_CELLS", "CALL_PHONE", "READ_SMS", "WRITE_SMS", "RECEIVE_SMS", "RECEIVE_EMERGECY_SMS", "RECEIVE_MMS", "RECEIVE_WAP_PUSH", "SEND_SMS", "READ_ICC_SMS", "WRITE_ICC_SMS", "WRITE_SETTINGS", "SYSTEM_ALERT_WINDOW", "ACCESS_NOTIFICATIONS", "CAMERA", "RECORD_AUDIO", "PLAY_AUDIO", "READ_CLIPBOARD", "WRITE_CLIPBOARD", "TAKE_MEDIA_BUTTONS", "TAKE_AUDIO_FOCUS", "AUDIO_MASTER_VOLUME", "AUDIO_VOICE_VOLUME", "AUDIO_RING_VOLUME", "AUDIO_MEDIA_VOLUME", "AUDIO_ALARM_VOLUME", "AUDIO_NOTIFICATION_VOLUME", "AUDIO_BLUETOOTH_VOLUME", "WAKE_LOCK", "MONITOR_LOCATION", "MONITOR_HIGH_POWER_LOCATION", "GET_USAGE_STATS", "MUTE_MICROPHONE", "TOAST_WINDOW", "PROJECT_MEDIA", "ACTIVATE_VPN", "WRITE_WALLPAPER", "ASSIST_STRUCTURE", "ASSIST_SCREENSHOT", "OP_READ_PHONE_STATE", "ADD_VOICEMAIL", "USE_SIP", "PROCESS_OUTGOING_CALLS", "USE_FINGERPRINT", "BODY_SENSORS", "READ_CELL_BROADCASTS", "MOCK_LOCATION", "READ_EXTERNAL_STORAGE", "WRITE_EXTERNAL_STORAGE", "TURN_ON_SCREEN", "GET_ACCOUNTS", "RUN_IN_BACKGROUND" };

    /**
     * This optionally maps a permission to an operation.  If there
     * is no permission associated with an operation, it is null.
     */
    private static java.lang.String[] sOpPerms = new java.lang.String[]{ android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, null, android.Manifest.permission.VIBRATE, android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_CONTACTS, android.Manifest.permission.READ_CALL_LOG, android.Manifest.permission.WRITE_CALL_LOG, android.Manifest.permission.READ_CALENDAR, android.Manifest.permission.WRITE_CALENDAR, android.Manifest.permission.ACCESS_WIFI_STATE, null// no permission required for notifications
    , null// neighboring cells shares the coarse location perm
    , android.Manifest.permission.CALL_PHONE, android.Manifest.permission.READ_SMS, null// no permission required for writing sms
    , android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.RECEIVE_EMERGENCY_BROADCAST, android.Manifest.permission.RECEIVE_MMS, android.Manifest.permission.RECEIVE_WAP_PUSH, android.Manifest.permission.SEND_SMS, android.Manifest.permission.READ_SMS, null// no permission required for writing icc sms
    , android.Manifest.permission.WRITE_SETTINGS, android.Manifest.permission.SYSTEM_ALERT_WINDOW, android.Manifest.permission.ACCESS_NOTIFICATIONS, android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO, null// no permission for playing audio
    , null// no permission for reading clipboard
    , null// no permission for writing clipboard
    , null// no permission for taking media buttons
    , null// no permission for taking audio focus
    , null// no permission for changing master volume
    , null// no permission for changing voice volume
    , null// no permission for changing ring volume
    , null// no permission for changing media volume
    , null// no permission for changing alarm volume
    , null// no permission for changing notification volume
    , null// no permission for changing bluetooth volume
    , android.Manifest.permission.WAKE_LOCK, null// no permission for generic location monitoring
    , null// no permission for high power location monitoring
    , android.Manifest.permission.PACKAGE_USAGE_STATS, null// no permission for muting/unmuting microphone
    , null// no permission for displaying toasts
    , null// no permission for projecting media
    , null// no permission for activating vpn
    , null// no permission for supporting wallpaper
    , null// no permission for receiving assist structure
    , null// no permission for receiving assist screenshot
    , Manifest.permission.READ_PHONE_STATE, Manifest.permission.ADD_VOICEMAIL, Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.USE_FINGERPRINT, Manifest.permission.BODY_SENSORS, Manifest.permission.READ_CELL_BROADCASTS, null, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, null// no permission for turning the screen on
    , Manifest.permission.GET_ACCOUNTS, null// no permission for running in background
     };

    /**
     * Specifies whether an Op should be restricted by a user restriction.
     * Each Op should be filled with a restriction string from UserManager or
     * null to specify it is not affected by any user restriction.
     */
    private static java.lang.String[] sOpRestrictions = new java.lang.String[]{ android.os.UserManager.DISALLOW_SHARE_LOCATION// COARSE_LOCATION
    , android.os.UserManager.DISALLOW_SHARE_LOCATION// FINE_LOCATION
    , android.os.UserManager.DISALLOW_SHARE_LOCATION// GPS
    , null// VIBRATE
    , null// READ_CONTACTS
    , null// WRITE_CONTACTS
    , android.os.UserManager.DISALLOW_OUTGOING_CALLS// READ_CALL_LOG
    , android.os.UserManager.DISALLOW_OUTGOING_CALLS// WRITE_CALL_LOG
    , null// READ_CALENDAR
    , null// WRITE_CALENDAR
    , android.os.UserManager.DISALLOW_SHARE_LOCATION// WIFI_SCAN
    , null// POST_NOTIFICATION
    , null// NEIGHBORING_CELLS
    , null// CALL_PHONE
    , android.os.UserManager.DISALLOW_SMS// READ_SMS
    , android.os.UserManager.DISALLOW_SMS// WRITE_SMS
    , android.os.UserManager.DISALLOW_SMS// RECEIVE_SMS
    , null// RECEIVE_EMERGENCY_SMS
    , android.os.UserManager.DISALLOW_SMS// RECEIVE_MMS
    , null// RECEIVE_WAP_PUSH
    , android.os.UserManager.DISALLOW_SMS// SEND_SMS
    , android.os.UserManager.DISALLOW_SMS// READ_ICC_SMS
    , android.os.UserManager.DISALLOW_SMS// WRITE_ICC_SMS
    , null// WRITE_SETTINGS
    , android.os.UserManager.DISALLOW_CREATE_WINDOWS// SYSTEM_ALERT_WINDOW
    , null// ACCESS_NOTIFICATIONS
    , android.os.UserManager.DISALLOW_CAMERA// CAMERA
    , android.os.UserManager.DISALLOW_RECORD_AUDIO// RECORD_AUDIO
    , null// PLAY_AUDIO
    , null// READ_CLIPBOARD
    , null// WRITE_CLIPBOARD
    , null// TAKE_MEDIA_BUTTONS
    , null// TAKE_AUDIO_FOCUS
    , android.os.UserManager.DISALLOW_ADJUST_VOLUME// AUDIO_MASTER_VOLUME
    , android.os.UserManager.DISALLOW_ADJUST_VOLUME// AUDIO_VOICE_VOLUME
    , android.os.UserManager.DISALLOW_ADJUST_VOLUME// AUDIO_RING_VOLUME
    , android.os.UserManager.DISALLOW_ADJUST_VOLUME// AUDIO_MEDIA_VOLUME
    , android.os.UserManager.DISALLOW_ADJUST_VOLUME// AUDIO_ALARM_VOLUME
    , android.os.UserManager.DISALLOW_ADJUST_VOLUME// AUDIO_NOTIFICATION_VOLUME
    , android.os.UserManager.DISALLOW_ADJUST_VOLUME// AUDIO_BLUETOOTH_VOLUME
    , null// WAKE_LOCK
    , android.os.UserManager.DISALLOW_SHARE_LOCATION// MONITOR_LOCATION
    , android.os.UserManager.DISALLOW_SHARE_LOCATION// MONITOR_HIGH_POWER_LOCATION
    , null// GET_USAGE_STATS
    , android.os.UserManager.DISALLOW_UNMUTE_MICROPHONE// MUTE_MICROPHONE
    , android.os.UserManager.DISALLOW_CREATE_WINDOWS// TOAST_WINDOW
    , null// PROJECT_MEDIA
    , null// ACTIVATE_VPN
    , android.os.UserManager.DISALLOW_WALLPAPER// WRITE_WALLPAPER
    , null// ASSIST_STRUCTURE
    , null// ASSIST_SCREENSHOT
    , null// READ_PHONE_STATE
    , null// ADD_VOICEMAIL
    , null// USE_SIP
    , null// PROCESS_OUTGOING_CALLS
    , null// USE_FINGERPRINT
    , null// BODY_SENSORS
    , null// READ_CELL_BROADCASTS
    , null// MOCK_LOCATION
    , null// READ_EXTERNAL_STORAGE
    , null// WRITE_EXTERNAL_STORAGE
    , null// TURN_ON_SCREEN
    , null// GET_ACCOUNTS
    , null// RUN_IN_BACKGROUND
     };

    /**
     * This specifies whether each option should allow the system
     * (and system ui) to bypass the user restriction when active.
     */
    private static boolean[] sOpAllowSystemRestrictionBypass = new boolean[]{ true// COARSE_LOCATION
    , true// FINE_LOCATION
    , false// GPS
    , false// VIBRATE
    , false// READ_CONTACTS
    , false// WRITE_CONTACTS
    , false// READ_CALL_LOG
    , false// WRITE_CALL_LOG
    , false// READ_CALENDAR
    , false// WRITE_CALENDAR
    , true// WIFI_SCAN
    , false// POST_NOTIFICATION
    , false// NEIGHBORING_CELLS
    , false// CALL_PHONE
    , false// READ_SMS
    , false// WRITE_SMS
    , false// RECEIVE_SMS
    , false// RECEIVE_EMERGECY_SMS
    , false// RECEIVE_MMS
    , false// RECEIVE_WAP_PUSH
    , false// SEND_SMS
    , false// READ_ICC_SMS
    , false// WRITE_ICC_SMS
    , false// WRITE_SETTINGS
    , true// SYSTEM_ALERT_WINDOW
    , false// ACCESS_NOTIFICATIONS
    , false// CAMERA
    , false// RECORD_AUDIO
    , false// PLAY_AUDIO
    , false// READ_CLIPBOARD
    , false// WRITE_CLIPBOARD
    , false// TAKE_MEDIA_BUTTONS
    , false// TAKE_AUDIO_FOCUS
    , false// AUDIO_MASTER_VOLUME
    , false// AUDIO_VOICE_VOLUME
    , false// AUDIO_RING_VOLUME
    , false// AUDIO_MEDIA_VOLUME
    , false// AUDIO_ALARM_VOLUME
    , false// AUDIO_NOTIFICATION_VOLUME
    , false// AUDIO_BLUETOOTH_VOLUME
    , false// WAKE_LOCK
    , false// MONITOR_LOCATION
    , false// MONITOR_HIGH_POWER_LOCATION
    , false// GET_USAGE_STATS
    , false// MUTE_MICROPHONE
    , true// TOAST_WINDOW
    , false// PROJECT_MEDIA
    , false// ACTIVATE_VPN
    , false// WALLPAPER
    , false// ASSIST_STRUCTURE
    , false// ASSIST_SCREENSHOT
    , false// READ_PHONE_STATE
    , false// ADD_VOICEMAIL
    , false// USE_SIP
    , false// PROCESS_OUTGOING_CALLS
    , false// USE_FINGERPRINT
    , false// BODY_SENSORS
    , false// READ_CELL_BROADCASTS
    , false// MOCK_LOCATION
    , false// READ_EXTERNAL_STORAGE
    , false// WRITE_EXTERNAL_STORAGE
    , false// TURN_ON_SCREEN
    , false// GET_ACCOUNTS
    , false// RUN_IN_BACKGROUND
     };

    /**
     * This specifies the default mode for each operation.
     */
    private static int[] sOpDefaultMode = new int[]{ android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_IGNORED// OP_WRITE_SMS
    , android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_DEFAULT// OP_WRITE_SETTINGS
    , android.app.AppOpsManager.MODE_DEFAULT// OP_SYSTEM_ALERT_WINDOW
    , android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_DEFAULT// OP_GET_USAGE_STATS
    , android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_IGNORED// OP_PROJECT_MEDIA
    , android.app.AppOpsManager.MODE_IGNORED// OP_ACTIVATE_VPN
    , android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ERRORED// OP_MOCK_LOCATION
    , android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED// OP_TURN_ON_SCREEN
    , android.app.AppOpsManager.MODE_ALLOWED, android.app.AppOpsManager.MODE_ALLOWED// OP_RUN_IN_BACKGROUND
     };

    /**
     * This specifies whether each option is allowed to be reset
     * when resetting all app preferences.  Disable reset for
     * app ops that are under strong control of some part of the
     * system (such as OP_WRITE_SMS, which should be allowed only
     * for whichever app is selected as the current SMS app).
     */
    private static boolean[] sOpDisableReset = new boolean[]{ false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true// OP_WRITE_SMS
    , false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };

    /**
     * Mapping from an app op name to the app op code.
     */
    private static java.util.HashMap<java.lang.String, java.lang.Integer> sOpStrToOp = new java.util.HashMap<>();

    /**
     * Mapping from a permission to the corresponding app op.
     */
    private static java.util.HashMap<java.lang.String, java.lang.Integer> sRuntimePermToOp = new java.util.HashMap<>();

    static {
        if (android.app.AppOpsManager.sOpToSwitch.length != android.app.AppOpsManager._NUM_OP) {
            throw new java.lang.IllegalStateException((("sOpToSwitch length " + android.app.AppOpsManager.sOpToSwitch.length) + " should be ") + android.app.AppOpsManager._NUM_OP);
        }
        if (android.app.AppOpsManager.sOpToString.length != android.app.AppOpsManager._NUM_OP) {
            throw new java.lang.IllegalStateException((("sOpToString length " + android.app.AppOpsManager.sOpToString.length) + " should be ") + android.app.AppOpsManager._NUM_OP);
        }
        if (android.app.AppOpsManager.sOpNames.length != android.app.AppOpsManager._NUM_OP) {
            throw new java.lang.IllegalStateException((("sOpNames length " + android.app.AppOpsManager.sOpNames.length) + " should be ") + android.app.AppOpsManager._NUM_OP);
        }
        if (android.app.AppOpsManager.sOpPerms.length != android.app.AppOpsManager._NUM_OP) {
            throw new java.lang.IllegalStateException((("sOpPerms length " + android.app.AppOpsManager.sOpPerms.length) + " should be ") + android.app.AppOpsManager._NUM_OP);
        }
        if (android.app.AppOpsManager.sOpDefaultMode.length != android.app.AppOpsManager._NUM_OP) {
            throw new java.lang.IllegalStateException((("sOpDefaultMode length " + android.app.AppOpsManager.sOpDefaultMode.length) + " should be ") + android.app.AppOpsManager._NUM_OP);
        }
        if (android.app.AppOpsManager.sOpDisableReset.length != android.app.AppOpsManager._NUM_OP) {
            throw new java.lang.IllegalStateException((("sOpDisableReset length " + android.app.AppOpsManager.sOpDisableReset.length) + " should be ") + android.app.AppOpsManager._NUM_OP);
        }
        if (android.app.AppOpsManager.sOpRestrictions.length != android.app.AppOpsManager._NUM_OP) {
            throw new java.lang.IllegalStateException((("sOpRestrictions length " + android.app.AppOpsManager.sOpRestrictions.length) + " should be ") + android.app.AppOpsManager._NUM_OP);
        }
        if (android.app.AppOpsManager.sOpAllowSystemRestrictionBypass.length != android.app.AppOpsManager._NUM_OP) {
            throw new java.lang.IllegalStateException((("sOpAllowSYstemRestrictionsBypass length " + android.app.AppOpsManager.sOpRestrictions.length) + " should be ") + android.app.AppOpsManager._NUM_OP);
        }
        for (int i = 0; i < android.app.AppOpsManager._NUM_OP; i++) {
            if (android.app.AppOpsManager.sOpToString[i] != null) {
                android.app.AppOpsManager.sOpStrToOp.put(android.app.AppOpsManager.sOpToString[i], i);
            }
        }
        for (int op : android.app.AppOpsManager.RUNTIME_PERMISSIONS_OPS) {
            if (android.app.AppOpsManager.sOpPerms[op] != null) {
                android.app.AppOpsManager.sRuntimePermToOp.put(android.app.AppOpsManager.sOpPerms[op], op);
            }
        }
    }

    /**
     * Retrieve the op switch that controls the given operation.
     *
     * @unknown 
     */
    public static int opToSwitch(int op) {
        return android.app.AppOpsManager.sOpToSwitch[op];
    }

    /**
     * Retrieve a non-localized name for the operation, for debugging output.
     *
     * @unknown 
     */
    public static java.lang.String opToName(int op) {
        if (op == android.app.AppOpsManager.OP_NONE)
            return "NONE";

        return op < android.app.AppOpsManager.sOpNames.length ? android.app.AppOpsManager.sOpNames[op] : ("Unknown(" + op) + ")";
    }

    /**
     *
     *
     * @unknown 
     */
    public static int strDebugOpToOp(java.lang.String op) {
        for (int i = 0; i < android.app.AppOpsManager.sOpNames.length; i++) {
            if (android.app.AppOpsManager.sOpNames[i].equals(op)) {
                return i;
            }
        }
        throw new java.lang.IllegalArgumentException("Unknown operation string: " + op);
    }

    /**
     * Retrieve the permission associated with an operation, or null if there is not one.
     *
     * @unknown 
     */
    public static java.lang.String opToPermission(int op) {
        return android.app.AppOpsManager.sOpPerms[op];
    }

    /**
     * Retrieve the user restriction associated with an operation, or null if there is not one.
     *
     * @unknown 
     */
    public static java.lang.String opToRestriction(int op) {
        return android.app.AppOpsManager.sOpRestrictions[op];
    }

    /**
     * Retrieve the app op code for a permission, or null if there is not one.
     * This API is intended to be used for mapping runtime permissions to the
     * corresponding app op.
     *
     * @unknown 
     */
    public static int permissionToOpCode(java.lang.String permission) {
        java.lang.Integer boxedOpCode = android.app.AppOpsManager.sRuntimePermToOp.get(permission);
        return boxedOpCode != null ? boxedOpCode : android.app.AppOpsManager.OP_NONE;
    }

    /**
     * Retrieve whether the op allows the system (and system ui) to
     * bypass the user restriction.
     *
     * @unknown 
     */
    public static boolean opAllowSystemBypassRestriction(int op) {
        return android.app.AppOpsManager.sOpAllowSystemRestrictionBypass[op];
    }

    /**
     * Retrieve the default mode for the operation.
     *
     * @unknown 
     */
    public static int opToDefaultMode(int op) {
        return android.app.AppOpsManager.sOpDefaultMode[op];
    }

    /**
     * Retrieve whether the op allows itself to be reset.
     *
     * @unknown 
     */
    public static boolean opAllowsReset(int op) {
        return !android.app.AppOpsManager.sOpDisableReset[op];
    }

    /**
     * Class holding all of the operation information associated with an app.
     *
     * @unknown 
     */
    public static class PackageOps implements android.os.Parcelable {
        private final java.lang.String mPackageName;

        private final int mUid;

        private final java.util.List<android.app.AppOpsManager.OpEntry> mEntries;

        public PackageOps(java.lang.String packageName, int uid, java.util.List<android.app.AppOpsManager.OpEntry> entries) {
            mPackageName = packageName;
            mUid = uid;
            mEntries = entries;
        }

        public java.lang.String getPackageName() {
            return mPackageName;
        }

        public int getUid() {
            return mUid;
        }

        public java.util.List<android.app.AppOpsManager.OpEntry> getOps() {
            return mEntries;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(mPackageName);
            dest.writeInt(mUid);
            dest.writeInt(mEntries.size());
            for (int i = 0; i < mEntries.size(); i++) {
                mEntries.get(i).writeToParcel(dest, flags);
            }
        }

        PackageOps(android.os.Parcel source) {
            mPackageName = source.readString();
            mUid = source.readInt();
            mEntries = new java.util.ArrayList<android.app.AppOpsManager.OpEntry>();
            final int N = source.readInt();
            for (int i = 0; i < N; i++) {
                mEntries.add(android.app.AppOpsManager.OpEntry.CREATOR.createFromParcel(source));
            }
        }

        public static final android.os.Parcelable.Creator<android.app.AppOpsManager.PackageOps> CREATOR = new android.os.Parcelable.Creator<android.app.AppOpsManager.PackageOps>() {
            @java.lang.Override
            public android.app.AppOpsManager.PackageOps createFromParcel(android.os.Parcel source) {
                return new android.app.AppOpsManager.PackageOps(source);
            }

            @java.lang.Override
            public android.app.AppOpsManager.PackageOps[] newArray(int size) {
                return new android.app.AppOpsManager.PackageOps[size];
            }
        };
    }

    /**
     * Class holding the information about one unique operation of an application.
     *
     * @unknown 
     */
    public static class OpEntry implements android.os.Parcelable {
        private final int mOp;

        private final int mMode;

        private final long mTime;

        private final long mRejectTime;

        private final int mDuration;

        private final int mProxyUid;

        private final java.lang.String mProxyPackageName;

        public OpEntry(int op, int mode, long time, long rejectTime, int duration, int proxyUid, java.lang.String proxyPackage) {
            mOp = op;
            mMode = mode;
            mTime = time;
            mRejectTime = rejectTime;
            mDuration = duration;
            mProxyUid = proxyUid;
            mProxyPackageName = proxyPackage;
        }

        public int getOp() {
            return mOp;
        }

        public int getMode() {
            return mMode;
        }

        public long getTime() {
            return mTime;
        }

        public long getRejectTime() {
            return mRejectTime;
        }

        public boolean isRunning() {
            return mDuration == (-1);
        }

        public int getDuration() {
            return mDuration == (-1) ? ((int) (java.lang.System.currentTimeMillis() - mTime)) : mDuration;
        }

        public int getProxyUid() {
            return mProxyUid;
        }

        public java.lang.String getProxyPackageName() {
            return mProxyPackageName;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(mOp);
            dest.writeInt(mMode);
            dest.writeLong(mTime);
            dest.writeLong(mRejectTime);
            dest.writeInt(mDuration);
            dest.writeInt(mProxyUid);
            dest.writeString(mProxyPackageName);
        }

        OpEntry(android.os.Parcel source) {
            mOp = source.readInt();
            mMode = source.readInt();
            mTime = source.readLong();
            mRejectTime = source.readLong();
            mDuration = source.readInt();
            mProxyUid = source.readInt();
            mProxyPackageName = source.readString();
        }

        public static final android.os.Parcelable.Creator<android.app.AppOpsManager.OpEntry> CREATOR = new android.os.Parcelable.Creator<android.app.AppOpsManager.OpEntry>() {
            @java.lang.Override
            public android.app.AppOpsManager.OpEntry createFromParcel(android.os.Parcel source) {
                return new android.app.AppOpsManager.OpEntry(source);
            }

            @java.lang.Override
            public android.app.AppOpsManager.OpEntry[] newArray(int size) {
                return new android.app.AppOpsManager.OpEntry[size];
            }
        };
    }

    /**
     * Callback for notification of changes to operation state.
     */
    public interface OnOpChangedListener {
        public void onOpChanged(java.lang.String op, java.lang.String packageName);
    }

    /**
     * Callback for notification of changes to operation state.
     * This allows you to see the raw op codes instead of strings.
     *
     * @unknown 
     */
    public static class OnOpChangedInternalListener implements android.app.AppOpsManager.OnOpChangedListener {
        public void onOpChanged(java.lang.String op, java.lang.String packageName) {
        }

        public void onOpChanged(int op, java.lang.String packageName) {
        }
    }

    AppOpsManager(android.content.Context context, com.android.internal.app.IAppOpsService service) {
        mContext = context;
        mService = service;
    }

    /**
     * Retrieve current operation state for all applications.
     *
     * @param ops
     * 		The set of operations you are interested in, or null if you want all of them.
     * @unknown 
     */
    public java.util.List<android.app.AppOpsManager.PackageOps> getPackagesForOps(int[] ops) {
        try {
            return mService.getPackagesForOps(ops);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Retrieve current operation state for one application.
     *
     * @param uid
     * 		The uid of the application of interest.
     * @param packageName
     * 		The name of the application of interest.
     * @param ops
     * 		The set of operations you are interested in, or null if you want all of them.
     * @unknown 
     */
    public java.util.List<android.app.AppOpsManager.PackageOps> getOpsForPackage(int uid, java.lang.String packageName, int[] ops) {
        try {
            return mService.getOpsForPackage(uid, packageName, ops);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Sets given app op in the specified mode for app ops in the UID.
     * This applies to all apps currently in the UID or installed in
     * this UID in the future.
     *
     * @param code
     * 		The app op.
     * @param uid
     * 		The UID for which to set the app.
     * @param mode
     * 		The app op mode to set.
     * @unknown 
     */
    public void setUidMode(int code, int uid, int mode) {
        try {
            mService.setUidMode(code, uid, mode);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Sets given app op in the specified mode for app ops in the UID.
     * This applies to all apps currently in the UID or installed in
     * this UID in the future.
     *
     * @param appOp
     * 		The app op.
     * @param uid
     * 		The UID for which to set the app.
     * @param mode
     * 		The app op mode to set.
     * @unknown 
     */
    @android.annotation.SystemApi
    public void setUidMode(java.lang.String appOp, int uid, int mode) {
        try {
            mService.setUidMode(android.app.AppOpsManager.strOpToOp(appOp), uid, mode);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setUserRestriction(int code, boolean restricted, android.os.IBinder token) {
        /* exceptionPackages */
        setUserRestriction(code, restricted, token, null);
    }

    /**
     *
     *
     * @unknown 
     */
    public void setUserRestriction(int code, boolean restricted, android.os.IBinder token, java.lang.String[] exceptionPackages) {
        setUserRestrictionForUser(code, restricted, token, exceptionPackages, mContext.getUserId());
    }

    /**
     *
     *
     * @unknown 
     */
    public void setUserRestrictionForUser(int code, boolean restricted, android.os.IBinder token, java.lang.String[] exceptionPackages, int userId) {
        try {
            mService.setUserRestriction(code, restricted, token, userId, exceptionPackages);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setMode(int code, int uid, java.lang.String packageName, int mode) {
        try {
            mService.setMode(code, uid, packageName, mode);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Set a non-persisted restriction on an audio operation at a stream-level.
     * Restrictions are temporary additional constraints imposed on top of the persisted rules
     * defined by {@link #setMode}.
     *
     * @param code
     * 		The operation to restrict.
     * @param usage
     * 		The {@link android.media.AudioAttributes} usage value.
     * @param mode
     * 		The restriction mode (MODE_IGNORED,MODE_ERRORED) or MODE_ALLOWED to unrestrict.
     * @param exceptionPackages
     * 		Optional list of packages to exclude from the restriction.
     * @unknown 
     */
    public void setRestriction(int code, @android.media.AudioAttributes.AttributeUsage
    int usage, int mode, java.lang.String[] exceptionPackages) {
        try {
            final int uid = android.os.Binder.getCallingUid();
            mService.setAudioRestriction(code, usage, uid, mode, exceptionPackages);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void resetAllModes() {
        try {
            mService.resetAllModes(android.os.UserHandle.myUserId(), null);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Gets the app op name associated with a given permission.
     * The app op name is one of the public constants defined
     * in this class such as {@link #OPSTR_COARSE_LOCATION}.
     * This API is intended to be used for mapping runtime
     * permissions to the corresponding app op.
     *
     * @param permission
     * 		The permission.
     * @return The app op associated with the permission or null.
     */
    public static java.lang.String permissionToOp(java.lang.String permission) {
        final java.lang.Integer opCode = android.app.AppOpsManager.sRuntimePermToOp.get(permission);
        if (opCode == null) {
            return null;
        }
        return android.app.AppOpsManager.sOpToString[opCode];
    }

    /**
     * Monitor for changes to the operating mode for the given op in the given app package.
     *
     * @param op
     * 		The operation to monitor, one of OPSTR_*.
     * @param packageName
     * 		The name of the application to monitor.
     * @param callback
     * 		Where to report changes.
     */
    public void startWatchingMode(java.lang.String op, java.lang.String packageName, final android.app.AppOpsManager.OnOpChangedListener callback) {
        startWatchingMode(android.app.AppOpsManager.strOpToOp(op), packageName, callback);
    }

    /**
     * Monitor for changes to the operating mode for the given op in the given app package.
     *
     * @param op
     * 		The operation to monitor, one of OP_*.
     * @param packageName
     * 		The name of the application to monitor.
     * @param callback
     * 		Where to report changes.
     * @unknown 
     */
    public void startWatchingMode(int op, java.lang.String packageName, final android.app.AppOpsManager.OnOpChangedListener callback) {
        synchronized(mModeWatchers) {
            com.android.internal.app.IAppOpsCallback cb = mModeWatchers.get(callback);
            if (cb == null) {
                cb = new com.android.internal.app.IAppOpsCallback.Stub() {
                    public void opChanged(int op, int uid, java.lang.String packageName) {
                        if (callback instanceof android.app.AppOpsManager.OnOpChangedInternalListener) {
                            ((android.app.AppOpsManager.OnOpChangedInternalListener) (callback)).onOpChanged(op, packageName);
                        }
                        if (android.app.AppOpsManager.sOpToString[op] != null) {
                            callback.onOpChanged(android.app.AppOpsManager.sOpToString[op], packageName);
                        }
                    }
                };
                mModeWatchers.put(callback, cb);
            }
            try {
                mService.startWatchingMode(op, packageName, cb);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    /**
     * Stop monitoring that was previously started with {@link #startWatchingMode}.  All
     * monitoring associated with this callback will be removed.
     */
    public void stopWatchingMode(android.app.AppOpsManager.OnOpChangedListener callback) {
        synchronized(mModeWatchers) {
            com.android.internal.app.IAppOpsCallback cb = mModeWatchers.get(callback);
            if (cb != null) {
                try {
                    mService.stopWatchingMode(cb);
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    private java.lang.String buildSecurityExceptionMsg(int op, int uid, java.lang.String packageName) {
        return (((packageName + " from uid ") + uid) + " not allowed to perform ") + android.app.AppOpsManager.sOpNames[op];
    }

    /**
     * {@hide }
     */
    public static int strOpToOp(java.lang.String op) {
        java.lang.Integer val = android.app.AppOpsManager.sOpStrToOp.get(op);
        if (val == null) {
            throw new java.lang.IllegalArgumentException("Unknown operation string: " + op);
        }
        return val;
    }

    /**
     * Do a quick check for whether an application might be able to perform an operation.
     * This is <em>not</em> a security check; you must use {@link #noteOp(String, int, String)}
     * or {@link #startOp(String, int, String)} for your actual security checks, which also
     * ensure that the given uid and package name are consistent.  This function can just be
     * used for a quick check to see if an operation has been disabled for the application,
     * as an early reject of some work.  This does not modify the time stamp or other data
     * about the operation.
     *
     * @param op
     * 		The operation to check.  One of the OPSTR_* constants.
     * @param uid
     * 		The user id of the application attempting to perform the operation.
     * @param packageName
     * 		The name of the application attempting to perform the operation.
     * @return Returns {@link #MODE_ALLOWED} if the operation is allowed, or
    {@link #MODE_IGNORED} if it is not allowed and should be silently ignored (without
    causing the app to crash).
     * @throws SecurityException
     * 		If the app has been configured to crash on this op.
     */
    public int checkOp(java.lang.String op, int uid, java.lang.String packageName) {
        return checkOp(android.app.AppOpsManager.strOpToOp(op), uid, packageName);
    }

    /**
     * Like {@link #checkOp} but instead of throwing a {@link SecurityException} it
     * returns {@link #MODE_ERRORED}.
     */
    public int checkOpNoThrow(java.lang.String op, int uid, java.lang.String packageName) {
        return checkOpNoThrow(android.app.AppOpsManager.strOpToOp(op), uid, packageName);
    }

    /**
     * Make note of an application performing an operation.  Note that you must pass
     * in both the uid and name of the application to be checked; this function will verify
     * that these two match, and if not, return {@link #MODE_IGNORED}.  If this call
     * succeeds, the last execution time of the operation for this app will be updated to
     * the current time.
     *
     * @param op
     * 		The operation to note.  One of the OPSTR_* constants.
     * @param uid
     * 		The user id of the application attempting to perform the operation.
     * @param packageName
     * 		The name of the application attempting to perform the operation.
     * @return Returns {@link #MODE_ALLOWED} if the operation is allowed, or
    {@link #MODE_IGNORED} if it is not allowed and should be silently ignored (without
    causing the app to crash).
     * @throws SecurityException
     * 		If the app has been configured to crash on this op.
     */
    public int noteOp(java.lang.String op, int uid, java.lang.String packageName) {
        return noteOp(android.app.AppOpsManager.strOpToOp(op), uid, packageName);
    }

    /**
     * Like {@link #noteOp} but instead of throwing a {@link SecurityException} it
     * returns {@link #MODE_ERRORED}.
     */
    public int noteOpNoThrow(java.lang.String op, int uid, java.lang.String packageName) {
        return noteOpNoThrow(android.app.AppOpsManager.strOpToOp(op), uid, packageName);
    }

    /**
     * Make note of an application performing an operation on behalf of another
     * application when handling an IPC. Note that you must pass the package name
     * of the application that is being proxied while its UID will be inferred from
     * the IPC state; this function will verify that the calling uid and proxied
     * package name match, and if not, return {@link #MODE_IGNORED}. If this call
     * succeeds, the last execution time of the operation for the proxied app and
     * your app will be updated to the current time.
     *
     * @param op
     * 		The operation to note.  One of the OPSTR_* constants.
     * @param proxiedPackageName
     * 		The name of the application calling into the proxy application.
     * @return Returns {@link #MODE_ALLOWED} if the operation is allowed, or
    {@link #MODE_IGNORED} if it is not allowed and should be silently ignored (without
    causing the app to crash).
     * @throws SecurityException
     * 		If the app has been configured to crash on this op.
     */
    public int noteProxyOp(java.lang.String op, java.lang.String proxiedPackageName) {
        return noteProxyOp(android.app.AppOpsManager.strOpToOp(op), proxiedPackageName);
    }

    /**
     * Like {@link #noteProxyOp(String, String)} but instead
     * of throwing a {@link SecurityException} it returns {@link #MODE_ERRORED}.
     */
    public int noteProxyOpNoThrow(java.lang.String op, java.lang.String proxiedPackageName) {
        return noteProxyOpNoThrow(android.app.AppOpsManager.strOpToOp(op), proxiedPackageName);
    }

    /**
     * Report that an application has started executing a long-running operation.  Note that you
     * must pass in both the uid and name of the application to be checked; this function will
     * verify that these two match, and if not, return {@link #MODE_IGNORED}.  If this call
     * succeeds, the last execution time of the operation for this app will be updated to
     * the current time and the operation will be marked as "running".  In this case you must
     * later call {@link #finishOp(String, int, String)} to report when the application is no
     * longer performing the operation.
     *
     * @param op
     * 		The operation to start.  One of the OPSTR_* constants.
     * @param uid
     * 		The user id of the application attempting to perform the operation.
     * @param packageName
     * 		The name of the application attempting to perform the operation.
     * @return Returns {@link #MODE_ALLOWED} if the operation is allowed, or
    {@link #MODE_IGNORED} if it is not allowed and should be silently ignored (without
    causing the app to crash).
     * @throws SecurityException
     * 		If the app has been configured to crash on this op.
     */
    public int startOp(java.lang.String op, int uid, java.lang.String packageName) {
        return startOp(android.app.AppOpsManager.strOpToOp(op), uid, packageName);
    }

    /**
     * Like {@link #startOp} but instead of throwing a {@link SecurityException} it
     * returns {@link #MODE_ERRORED}.
     */
    public int startOpNoThrow(java.lang.String op, int uid, java.lang.String packageName) {
        return startOpNoThrow(android.app.AppOpsManager.strOpToOp(op), uid, packageName);
    }

    /**
     * Report that an application is no longer performing an operation that had previously
     * been started with {@link #startOp(String, int, String)}.  There is no validation of input
     * or result; the parameters supplied here must be the exact same ones previously passed
     * in when starting the operation.
     */
    public void finishOp(java.lang.String op, int uid, java.lang.String packageName) {
        finishOp(android.app.AppOpsManager.strOpToOp(op), uid, packageName);
    }

    /**
     * Do a quick check for whether an application might be able to perform an operation.
     * This is <em>not</em> a security check; you must use {@link #noteOp(int, int, String)}
     * or {@link #startOp(int, int, String)} for your actual security checks, which also
     * ensure that the given uid and package name are consistent.  This function can just be
     * used for a quick check to see if an operation has been disabled for the application,
     * as an early reject of some work.  This does not modify the time stamp or other data
     * about the operation.
     *
     * @param op
     * 		The operation to check.  One of the OP_* constants.
     * @param uid
     * 		The user id of the application attempting to perform the operation.
     * @param packageName
     * 		The name of the application attempting to perform the operation.
     * @return Returns {@link #MODE_ALLOWED} if the operation is allowed, or
    {@link #MODE_IGNORED} if it is not allowed and should be silently ignored (without
    causing the app to crash).
     * @throws SecurityException
     * 		If the app has been configured to crash on this op.
     * @unknown 
     */
    public int checkOp(int op, int uid, java.lang.String packageName) {
        try {
            int mode = mService.checkOperation(op, uid, packageName);
            if (mode == android.app.AppOpsManager.MODE_ERRORED) {
                throw new java.lang.SecurityException(buildSecurityExceptionMsg(op, uid, packageName));
            }
            return mode;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Like {@link #checkOp} but instead of throwing a {@link SecurityException} it
     * returns {@link #MODE_ERRORED}.
     *
     * @unknown 
     */
    public int checkOpNoThrow(int op, int uid, java.lang.String packageName) {
        try {
            return mService.checkOperation(op, uid, packageName);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Do a quick check to validate if a package name belongs to a UID.
     *
     * @throws SecurityException
     * 		if the package name doesn't belong to the given
     * 		UID, or if ownership cannot be verified.
     */
    public void checkPackage(int uid, java.lang.String packageName) {
        try {
            if (mService.checkPackage(uid, packageName) != android.app.AppOpsManager.MODE_ALLOWED) {
                throw new java.lang.SecurityException((("Package " + packageName) + " does not belong to ") + uid);
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Like {@link #checkOp} but at a stream-level for audio operations.
     *
     * @unknown 
     */
    public int checkAudioOp(int op, int stream, int uid, java.lang.String packageName) {
        try {
            final int mode = mService.checkAudioOperation(op, stream, uid, packageName);
            if (mode == android.app.AppOpsManager.MODE_ERRORED) {
                throw new java.lang.SecurityException(buildSecurityExceptionMsg(op, uid, packageName));
            }
            return mode;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Like {@link #checkAudioOp} but instead of throwing a {@link SecurityException} it
     * returns {@link #MODE_ERRORED}.
     *
     * @unknown 
     */
    public int checkAudioOpNoThrow(int op, int stream, int uid, java.lang.String packageName) {
        try {
            return mService.checkAudioOperation(op, stream, uid, packageName);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Make note of an application performing an operation.  Note that you must pass
     * in both the uid and name of the application to be checked; this function will verify
     * that these two match, and if not, return {@link #MODE_IGNORED}.  If this call
     * succeeds, the last execution time of the operation for this app will be updated to
     * the current time.
     *
     * @param op
     * 		The operation to note.  One of the OP_* constants.
     * @param uid
     * 		The user id of the application attempting to perform the operation.
     * @param packageName
     * 		The name of the application attempting to perform the operation.
     * @return Returns {@link #MODE_ALLOWED} if the operation is allowed, or
    {@link #MODE_IGNORED} if it is not allowed and should be silently ignored (without
    causing the app to crash).
     * @throws SecurityException
     * 		If the app has been configured to crash on this op.
     * @unknown 
     */
    public int noteOp(int op, int uid, java.lang.String packageName) {
        try {
            int mode = mService.noteOperation(op, uid, packageName);
            if (mode == android.app.AppOpsManager.MODE_ERRORED) {
                throw new java.lang.SecurityException(buildSecurityExceptionMsg(op, uid, packageName));
            }
            return mode;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Make note of an application performing an operation on behalf of another
     * application when handling an IPC. Note that you must pass the package name
     * of the application that is being proxied while its UID will be inferred from
     * the IPC state; this function will verify that the calling uid and proxied
     * package name match, and if not, return {@link #MODE_IGNORED}. If this call
     * succeeds, the last execution time of the operation for the proxied app and
     * your app will be updated to the current time.
     *
     * @param op
     * 		The operation to note. One of the OPSTR_* constants.
     * @param proxiedPackageName
     * 		The name of the application calling into the proxy application.
     * @return Returns {@link #MODE_ALLOWED} if the operation is allowed, or
    {@link #MODE_IGNORED} if it is not allowed and should be silently ignored (without
    causing the app to crash).
     * @throws SecurityException
     * 		If the proxy or proxied app has been configured to
     * 		crash on this op.
     * @unknown 
     */
    public int noteProxyOp(int op, java.lang.String proxiedPackageName) {
        int mode = noteProxyOpNoThrow(op, proxiedPackageName);
        if (mode == android.app.AppOpsManager.MODE_ERRORED) {
            throw new java.lang.SecurityException((((((((("Proxy package " + mContext.getOpPackageName()) + " from uid ") + android.os.Process.myUid()) + " or calling package ") + proxiedPackageName) + " from uid ") + android.os.Binder.getCallingUid()) + " not allowed to perform ") + android.app.AppOpsManager.sOpNames[op]);
        }
        return mode;
    }

    /**
     * Like {@link #noteProxyOp(int, String)} but instead
     * of throwing a {@link SecurityException} it returns {@link #MODE_ERRORED}.
     *
     * @unknown 
     */
    public int noteProxyOpNoThrow(int op, java.lang.String proxiedPackageName) {
        try {
            return mService.noteProxyOperation(op, mContext.getOpPackageName(), android.os.Binder.getCallingUid(), proxiedPackageName);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Like {@link #noteOp} but instead of throwing a {@link SecurityException} it
     * returns {@link #MODE_ERRORED}.
     *
     * @unknown 
     */
    public int noteOpNoThrow(int op, int uid, java.lang.String packageName) {
        try {
            return mService.noteOperation(op, uid, packageName);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public int noteOp(int op) {
        return noteOp(op, android.os.Process.myUid(), mContext.getOpPackageName());
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.os.IBinder getToken(com.android.internal.app.IAppOpsService service) {
        synchronized(android.app.AppOpsManager.class) {
            if (android.app.AppOpsManager.sToken != null) {
                return android.app.AppOpsManager.sToken;
            }
            try {
                android.app.AppOpsManager.sToken = service.getToken(new android.os.Binder());
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
            return android.app.AppOpsManager.sToken;
        }
    }

    /**
     * Report that an application has started executing a long-running operation.  Note that you
     * must pass in both the uid and name of the application to be checked; this function will
     * verify that these two match, and if not, return {@link #MODE_IGNORED}.  If this call
     * succeeds, the last execution time of the operation for this app will be updated to
     * the current time and the operation will be marked as "running".  In this case you must
     * later call {@link #finishOp(int, int, String)} to report when the application is no
     * longer performing the operation.
     *
     * @param op
     * 		The operation to start.  One of the OP_* constants.
     * @param uid
     * 		The user id of the application attempting to perform the operation.
     * @param packageName
     * 		The name of the application attempting to perform the operation.
     * @return Returns {@link #MODE_ALLOWED} if the operation is allowed, or
    {@link #MODE_IGNORED} if it is not allowed and should be silently ignored (without
    causing the app to crash).
     * @throws SecurityException
     * 		If the app has been configured to crash on this op.
     * @unknown 
     */
    public int startOp(int op, int uid, java.lang.String packageName) {
        try {
            int mode = mService.startOperation(android.app.AppOpsManager.getToken(mService), op, uid, packageName);
            if (mode == android.app.AppOpsManager.MODE_ERRORED) {
                throw new java.lang.SecurityException(buildSecurityExceptionMsg(op, uid, packageName));
            }
            return mode;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Like {@link #startOp} but instead of throwing a {@link SecurityException} it
     * returns {@link #MODE_ERRORED}.
     *
     * @unknown 
     */
    public int startOpNoThrow(int op, int uid, java.lang.String packageName) {
        try {
            return mService.startOperation(android.app.AppOpsManager.getToken(mService), op, uid, packageName);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public int startOp(int op) {
        return startOp(op, android.os.Process.myUid(), mContext.getOpPackageName());
    }

    /**
     * Report that an application is no longer performing an operation that had previously
     * been started with {@link #startOp(int, int, String)}.  There is no validation of input
     * or result; the parameters supplied here must be the exact same ones previously passed
     * in when starting the operation.
     *
     * @unknown 
     */
    public void finishOp(int op, int uid, java.lang.String packageName) {
        try {
            mService.finishOperation(android.app.AppOpsManager.getToken(mService), op, uid, packageName);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void finishOp(int op) {
        finishOp(op, android.os.Process.myUid(), mContext.getOpPackageName());
    }
}

