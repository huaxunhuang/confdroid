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
 * The Settings provider contains global system-level device preferences.
 */
public final class Settings {
    // Intent actions for Settings
    /**
     * Activity Action: Show system settings.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_SETTINGS = "android.settings.SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of APNs.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_APN_SETTINGS = "android.settings.APN_SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of current location
     * sources.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_LOCATION_SOURCE_SETTINGS = "android.settings.LOCATION_SOURCE_SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of users.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_USER_SETTINGS = "android.settings.USER_SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of wireless controls
     * such as Wi-Fi, Bluetooth and Mobile networks.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_WIRELESS_SETTINGS = "android.settings.WIRELESS_SETTINGS";

    /**
     * Activity Action: Show tether provisioning activity.
     *
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: {@link ConnectivityManager.EXTRA_TETHER_TYPE} should be included to specify which type
     * of tethering should be checked. {@link ConnectivityManager.EXTRA_PROVISION_CALLBACK} should
     * contain a {@link ResultReceiver} which will be called back with a tether result code.
     * <p>
     * Output: The result of the provisioning check.
     * {@link ConnectivityManager.TETHER_ERROR_NO_ERROR} if successful,
     * {@link ConnectivityManager.TETHER_ERROR_PROVISION_FAILED} for failure.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_TETHER_PROVISIONING = "android.settings.TETHER_PROVISIONING_UI";

    /**
     * Activity Action: Show settings to allow entering/exiting airplane mode.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_AIRPLANE_MODE_SETTINGS = "android.settings.AIRPLANE_MODE_SETTINGS";

    /**
     * Activity Action: Modify Airplane mode settings using a voice command.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you safeguard against this.
     * <p>
     * This intent MUST be started using
     * {@link android.service.voice.VoiceInteractionSession#startVoiceActivity
     * startVoiceActivity}.
     * <p>
     * Note: The activity implementing this intent MUST verify that
     * {@link android.app.Activity#isVoiceInteraction isVoiceInteraction} returns true before
     * modifying the setting.
     * <p>
     * Input: To tell which state airplane mode should be set to, add the
     * {@link #EXTRA_AIRPLANE_MODE_ENABLED} extra to this Intent with the state specified.
     * If the extra is not included, no changes will be made.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_VOICE_CONTROL_AIRPLANE_MODE = "android.settings.VOICE_CONTROL_AIRPLANE_MODE";

    /**
     * Activity Action: Show settings for accessibility modules.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_ACCESSIBILITY_SETTINGS = "android.settings.ACCESSIBILITY_SETTINGS";

    /**
     * Activity Action: Show settings to control access to usage information.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_USAGE_ACCESS_SETTINGS = "android.settings.USAGE_ACCESS_SETTINGS";

    /**
     * Activity Category: Show application settings related to usage access.
     * <p>
     * An activity that provides a user interface for adjusting usage access related
     * preferences for its containing application. Optional but recommended for apps that
     * use {@link android.Manifest.permission#PACKAGE_USAGE_STATS}.
     * <p>
     * The activity may define meta-data to describe what usage access is
     * used for within their app with {@link #METADATA_USAGE_ACCESS_REASON}, which
     * will be displayed in Settings.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.INTENT_CATEGORY)
    public static final java.lang.String INTENT_CATEGORY_USAGE_ACCESS_CONFIG = "android.intent.category.USAGE_ACCESS_CONFIG";

    /**
     * Metadata key: Reason for needing usage access.
     * <p>
     * A key for metadata attached to an activity that receives action
     * {@link #INTENT_CATEGORY_USAGE_ACCESS_CONFIG}, shown to the
     * user as description of how the app uses usage access.
     * <p>
     */
    public static final java.lang.String METADATA_USAGE_ACCESS_REASON = "android.settings.metadata.USAGE_ACCESS_REASON";

    /**
     * Activity Action: Show settings to allow configuration of security and
     * location privacy.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_SECURITY_SETTINGS = "android.settings.SECURITY_SETTINGS";

    /**
     * Activity Action: Show trusted credentials settings, opening to the user tab,
     * to allow management of installed credentials.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_TRUSTED_CREDENTIALS_USER = "com.android.settings.TRUSTED_CREDENTIALS_USER";

    /**
     * Activity Action: Show dialog explaining that an installed CA cert may enable
     * monitoring of encrypted network traffic.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this. Add {@link #EXTRA_NUMBER_OF_CERTIFICATES} extra to indicate the
     * number of certificates.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_MONITORING_CERT_INFO = "com.android.settings.MONITORING_CERT_INFO";

    /**
     * Activity Action: Show settings to allow configuration of privacy options.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_PRIVACY_SETTINGS = "android.settings.PRIVACY_SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of VPN.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_VPN_SETTINGS = "android.settings.VPN_SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of Wi-Fi.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_WIFI_SETTINGS = "android.settings.WIFI_SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of a static IP
     * address for Wi-Fi.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you safeguard
     * against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_WIFI_IP_SETTINGS = "android.settings.WIFI_IP_SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of Bluetooth.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_BLUETOOTH_SETTINGS = "android.settings.BLUETOOTH_SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of cast endpoints.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_CAST_SETTINGS = "android.settings.CAST_SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of date and time.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_DATE_SETTINGS = "android.settings.DATE_SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of sound and volume.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_SOUND_SETTINGS = "android.settings.SOUND_SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of display.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_DISPLAY_SETTINGS = "android.settings.DISPLAY_SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of Night display.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_NIGHT_DISPLAY_SETTINGS = "android.settings.NIGHT_DISPLAY_SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of locale.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_LOCALE_SETTINGS = "android.settings.LOCALE_SETTINGS";

    /**
     * Activity Action: Show settings to configure input methods, in particular
     * allowing the user to enable input methods.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_VOICE_INPUT_SETTINGS = "android.settings.VOICE_INPUT_SETTINGS";

    /**
     * Activity Action: Show settings to configure input methods, in particular
     * allowing the user to enable input methods.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_INPUT_METHOD_SETTINGS = "android.settings.INPUT_METHOD_SETTINGS";

    /**
     * Activity Action: Show settings to enable/disable input method subtypes.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * To tell which input method's subtypes are displayed in the settings, add
     * {@link #EXTRA_INPUT_METHOD_ID} extra to this Intent with the input method id.
     * If there is no extra in this Intent, subtypes from all installed input methods
     * will be displayed in the settings.
     *
     * @see android.view.inputmethod.InputMethodInfo#getId
    <p>
    Input: Nothing.
    <p>
    Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_INPUT_METHOD_SUBTYPE_SETTINGS = "android.settings.INPUT_METHOD_SUBTYPE_SETTINGS";

    /**
     * Activity Action: Show a dialog to select input method.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_SHOW_INPUT_METHOD_PICKER = "android.settings.SHOW_INPUT_METHOD_PICKER";

    /**
     * Activity Action: Show settings to manage the user input dictionary.
     * <p>
     * Starting with {@link android.os.Build.VERSION_CODES#KITKAT},
     * it is guaranteed there will always be an appropriate implementation for this Intent action.
     * In prior releases of the platform this was optional, so ensure you safeguard against it.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_USER_DICTIONARY_SETTINGS = "android.settings.USER_DICTIONARY_SETTINGS";

    /**
     * Activity Action: Show settings to configure the hardware keyboard.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_HARD_KEYBOARD_SETTINGS = "android.settings.HARD_KEYBOARD_SETTINGS";

    /**
     * Activity Action: Adds a word to the user dictionary.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: An extra with key <code>word</code> that contains the word
     * that should be added to the dictionary.
     * <p>
     * Output: Nothing.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_USER_DICTIONARY_INSERT = "com.android.settings.USER_DICTIONARY_INSERT";

    /**
     * Activity Action: Show settings to allow configuration of application-related settings.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_APPLICATION_SETTINGS = "android.settings.APPLICATION_SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of application
     * development-related settings.  As of
     * {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR1} this action is
     * a required part of the platform.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_APPLICATION_DEVELOPMENT_SETTINGS = "android.settings.APPLICATION_DEVELOPMENT_SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of quick launch shortcuts.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_QUICK_LAUNCH_SETTINGS = "android.settings.QUICK_LAUNCH_SETTINGS";

    /**
     * Activity Action: Show settings to manage installed applications.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_MANAGE_APPLICATIONS_SETTINGS = "android.settings.MANAGE_APPLICATIONS_SETTINGS";

    /**
     * Activity Action: Show settings to manage all applications.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS = "android.settings.MANAGE_ALL_APPLICATIONS_SETTINGS";

    /**
     * Activity Action: Show screen for controlling which apps can draw on top of other apps.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Optionally, the Intent's data URI can specify the application package name to
     * directly invoke the management GUI specific to the package name. For example
     * "package:com.my.app".
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_MANAGE_OVERLAY_PERMISSION = "android.settings.action.MANAGE_OVERLAY_PERMISSION";

    /**
     * Activity Action: Show screen for controlling which apps are allowed to write/modify
     * system settings.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Optionally, the Intent's data URI can specify the application package name to
     * directly invoke the management GUI specific to the package name. For example
     * "package:com.my.app".
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_MANAGE_WRITE_SETTINGS = "android.settings.action.MANAGE_WRITE_SETTINGS";

    /**
     * Activity Action: Show screen of details about a particular application.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: The Intent's data URI specifies the application package name
     * to be shown, with the "package" scheme.  That is "package:com.my.app".
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_APPLICATION_DETAILS_SETTINGS = "android.settings.APPLICATION_DETAILS_SETTINGS";

    /**
     * Activity Action: Show screen for controlling which apps can ignore battery optimizations.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     * <p>
     * You can use {@link android.os.PowerManager#isIgnoringBatteryOptimizations
     * PowerManager.isIgnoringBatteryOptimizations()} to determine if an application is
     * already ignoring optimizations.  You can use
     * {@link #ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS} to ask the user to put you
     * on this list.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS = "android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS";

    /**
     * Activity Action: Ask the user to allow an app to ignore battery optimizations (that is,
     * put them on the whitelist of apps shown by
     * {@link #ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS}).  For an app to use this, it also
     * must hold the {@link android.Manifest.permission#REQUEST_IGNORE_BATTERY_OPTIMIZATIONS}
     * permission.
     * <p><b>Note:</b> most applications should <em>not</em> use this; there are many facilities
     * provided by the platform for applications to operate correctly in the various power
     * saving modes.  This is only for unusual applications that need to deeply control their own
     * execution, at the potential expense of the user's battery life.  Note that these applications
     * greatly run the risk of showing to the user as high power consumers on their device.</p>
     * <p>
     * Input: The Intent's data URI must specify the application package name
     * to be shown, with the "package" scheme.  That is "package:com.my.app".
     * <p>
     * Output: Nothing.
     * <p>
     * You can use {@link android.os.PowerManager#isIgnoringBatteryOptimizations
     * PowerManager.isIgnoringBatteryOptimizations()} to determine if an application is
     * already ignoring optimizations.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS = "android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS";

    /**
     * Activity Action: Show screen for controlling background data
     * restrictions for a particular application.
     * <p>
     * Input: Intent's data URI set with an application name, using the
     * "package" schema (like "package:com.my.app").
     *
     * <p>
     * Output: Nothing.
     * <p>
     * Applications can also use {@link android.net.ConnectivityManager#getRestrictBackgroundStatus
     * ConnectivityManager#getRestrictBackgroundStatus()} to determine the
     * status of the background data restrictions for them.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS = "android.settings.IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS";

    /**
     *
     *
     * @unknown Activity Action: Show the "app ops" settings screen.
    <p>
    Input: Nothing.
    <p>
    Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_APP_OPS_SETTINGS = "android.settings.APP_OPS_SETTINGS";

    /**
     * Activity Action: Show settings for system update functionality.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_SYSTEM_UPDATE_SETTINGS = "android.settings.SYSTEM_UPDATE_SETTINGS";

    /**
     * Activity Action: Show settings to allow configuration of sync settings.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * The account types available to add via the add account button may be restricted by adding an
     * {@link #EXTRA_AUTHORITIES} extra to this Intent with one or more syncable content provider's
     * authorities. Only account types which can sync with that content provider will be offered to
     * the user.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_SYNC_SETTINGS = "android.settings.SYNC_SETTINGS";

    /**
     * Activity Action: Show add account screen for creating a new account.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * The account types available to add may be restricted by adding an {@link #EXTRA_AUTHORITIES}
     * extra to the Intent with one or more syncable content provider's authorities.  Only account
     * types which can sync with that content provider will be offered to the user.
     * <p>
     * Account types can also be filtered by adding an {@link #EXTRA_ACCOUNT_TYPES} extra to the
     * Intent with one or more account types.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_ADD_ACCOUNT = "android.settings.ADD_ACCOUNT_SETTINGS";

    /**
     * Activity Action: Show settings for selecting the network operator.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_NETWORK_OPERATOR_SETTINGS = "android.settings.NETWORK_OPERATOR_SETTINGS";

    /**
     * Activity Action: Show settings for selection of 2G/3G.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_DATA_ROAMING_SETTINGS = "android.settings.DATA_ROAMING_SETTINGS";

    /**
     * Activity Action: Show settings for internal storage.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_INTERNAL_STORAGE_SETTINGS = "android.settings.INTERNAL_STORAGE_SETTINGS";

    /**
     * Activity Action: Show settings for memory card storage.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_MEMORY_CARD_SETTINGS = "android.settings.MEMORY_CARD_SETTINGS";

    /**
     * Activity Action: Show settings for global search.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_SEARCH_SETTINGS = "android.search.action.SEARCH_SETTINGS";

    /**
     * Activity Action: Show general device information settings (serial
     * number, software version, phone number, etc.).
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_DEVICE_INFO_SETTINGS = "android.settings.DEVICE_INFO_SETTINGS";

    /**
     * Activity Action: Show NFC settings.
     * <p>
     * This shows UI that allows NFC to be turned on or off.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing
     *
     * @see android.nfc.NfcAdapter#isEnabled()
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_NFC_SETTINGS = "android.settings.NFC_SETTINGS";

    /**
     * Activity Action: Show NFC Sharing settings.
     * <p>
     * This shows UI that allows NDEF Push (Android Beam) to be turned on or
     * off.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing
     *
     * @see android.nfc.NfcAdapter#isNdefPushEnabled()
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_NFCSHARING_SETTINGS = "android.settings.NFCSHARING_SETTINGS";

    /**
     * Activity Action: Show NFC Tap & Pay settings
     * <p>
     * This shows UI that allows the user to configure Tap&Pay
     * settings.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_NFC_PAYMENT_SETTINGS = "android.settings.NFC_PAYMENT_SETTINGS";

    /**
     * Activity Action: Show Daydream settings.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     *
     * @see android.service.dreams.DreamService
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_DREAM_SETTINGS = "android.settings.DREAM_SETTINGS";

    /**
     * Activity Action: Show Notification listener settings.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     *
     * @see android.service.notification.NotificationListenerService
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    /**
     * Activity Action: Show Do Not Disturb access settings.
     * <p>
     * Users can grant and deny access to Do Not Disturb configuration from here.
     * See {@link android.app.NotificationManager#isNotificationPolicyAccessGranted()} for more
     * details.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS = "android.settings.NOTIFICATION_POLICY_ACCESS_SETTINGS";

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_CONDITION_PROVIDER_SETTINGS = "android.settings.ACTION_CONDITION_PROVIDER_SETTINGS";

    /**
     * Activity Action: Show settings for video captioning.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you safeguard
     * against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_CAPTIONING_SETTINGS = "android.settings.CAPTIONING_SETTINGS";

    /**
     * Activity Action: Show the top level print settings.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_PRINT_SETTINGS = "android.settings.ACTION_PRINT_SETTINGS";

    /**
     * Activity Action: Show Zen Mode configuration settings.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_ZEN_MODE_SETTINGS = "android.settings.ZEN_MODE_SETTINGS";

    /**
     * Activity Action: Show Zen Mode priority configuration settings.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_ZEN_MODE_PRIORITY_SETTINGS = "android.settings.ZEN_MODE_PRIORITY_SETTINGS";

    /**
     * Activity Action: Show Zen Mode automation configuration settings.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_ZEN_MODE_AUTOMATION_SETTINGS = "android.settings.ZEN_MODE_AUTOMATION_SETTINGS";

    /**
     * Activity Action: Modify do not disturb mode settings.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you safeguard against this.
     * <p>
     * This intent MUST be started using
     * {@link android.service.voice.VoiceInteractionSession#startVoiceActivity
     * startVoiceActivity}.
     * <p>
     * Note: The Activity implementing this intent MUST verify that
     * {@link android.app.Activity#isVoiceInteraction isVoiceInteraction}.
     * returns true before modifying the setting.
     * <p>
     * Input: The optional {@link #EXTRA_DO_NOT_DISTURB_MODE_MINUTES} extra can be used to indicate
     * how long the user wishes to avoid interruptions for. The optional
     * {@link #EXTRA_DO_NOT_DISTURB_MODE_ENABLED} extra can be to indicate if the user is
     * enabling or disabling do not disturb mode. If either extra is not included, the
     * user maybe asked to provide the value.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_VOICE_CONTROL_DO_NOT_DISTURB_MODE = "android.settings.VOICE_CONTROL_DO_NOT_DISTURB_MODE";

    /**
     * Activity Action: Show Zen Mode schedule rule configuration settings.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_ZEN_MODE_SCHEDULE_RULE_SETTINGS = "android.settings.ZEN_MODE_SCHEDULE_RULE_SETTINGS";

    /**
     * Activity Action: Show Zen Mode event rule configuration settings.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_ZEN_MODE_EVENT_RULE_SETTINGS = "android.settings.ZEN_MODE_EVENT_RULE_SETTINGS";

    /**
     * Activity Action: Show Zen Mode external rule configuration settings.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_ZEN_MODE_EXTERNAL_RULE_SETTINGS = "android.settings.ZEN_MODE_EXTERNAL_RULE_SETTINGS";

    /**
     * Activity Action: Show the regulatory information screen for the device.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you safeguard
     * against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_SHOW_REGULATORY_INFO = "android.settings.SHOW_REGULATORY_INFO";

    /**
     * Activity Action: Show Device Name Settings.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you safeguard
     * against this.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String DEVICE_NAME_SETTINGS = "android.settings.DEVICE_NAME";

    /**
     * Activity Action: Show pairing settings.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you safeguard
     * against this.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_PAIRING_SETTINGS = "android.settings.PAIRING_SETTINGS";

    /**
     * Activity Action: Show battery saver settings.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you safeguard
     * against this.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_BATTERY_SAVER_SETTINGS = "android.settings.BATTERY_SAVER_SETTINGS";

    /**
     * Activity Action: Modify Battery Saver mode setting using a voice command.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you safeguard against this.
     * <p>
     * This intent MUST be started using
     * {@link android.service.voice.VoiceInteractionSession#startVoiceActivity
     * startVoiceActivity}.
     * <p>
     * Note: The activity implementing this intent MUST verify that
     * {@link android.app.Activity#isVoiceInteraction isVoiceInteraction} returns true before
     * modifying the setting.
     * <p>
     * Input: To tell which state batter saver mode should be set to, add the
     * {@link #EXTRA_BATTERY_SAVER_MODE_ENABLED} extra to this Intent with the state specified.
     * If the extra is not included, no changes will be made.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_VOICE_CONTROL_BATTERY_SAVER_MODE = "android.settings.VOICE_CONTROL_BATTERY_SAVER_MODE";

    /**
     * Activity Action: Show Home selection settings. If there are multiple activities
     * that can satisfy the {@link Intent#CATEGORY_HOME} intent, this screen allows you
     * to pick your preferred activity.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_HOME_SETTINGS = "android.settings.HOME_SETTINGS";

    /**
     * Activity Action: Show Default apps settings.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_MANAGE_DEFAULT_APPS_SETTINGS = "android.settings.MANAGE_DEFAULT_APPS_SETTINGS";

    /**
     * Activity Action: Show notification settings.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_NOTIFICATION_SETTINGS = "android.settings.NOTIFICATION_SETTINGS";

    /**
     * Activity Action: Show notification settings for a single app.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_APP_NOTIFICATION_SETTINGS = "android.settings.APP_NOTIFICATION_SETTINGS";

    /**
     * Activity Action: Show notification redaction settings.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_APP_NOTIFICATION_REDACTION = "android.settings.ACTION_APP_NOTIFICATION_REDACTION";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_APP_UID = "app_uid";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_APP_PACKAGE = "app_package";

    /**
     * Activity Action: Show a dialog with disabled by policy message.
     * <p> If an user action is disabled by policy, this dialog can be triggered to let
     * the user know about this.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_SHOW_ADMIN_SUPPORT_DETAILS = "android.settings.SHOW_ADMIN_SUPPORT_DETAILS";

    /**
     * Activity Action: Show a dialog for remote bugreport flow.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_SHOW_REMOTE_BUGREPORT_DIALOG = "android.settings.SHOW_REMOTE_BUGREPORT_DIALOG";

    /**
     * Activity Action: Show VR listener settings.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     *
     * @see android.service.vr.VrListenerService
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_VR_LISTENER_SETTINGS = "android.settings.VR_LISTENER_SETTINGS";

    /**
     * Activity Action: Show Storage Manager settings.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     *
     * @unknown 
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_STORAGE_MANAGER_SETTINGS = "android.settings.STORAGE_MANAGER_SETTINGS";

    /**
     * Activity Action: Allows user to select current webview implementation.
     * <p>
     * Input: Nothing.
     * <p>
     * Output: Nothing.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.ACTIVITY_INTENT_ACTION)
    public static final java.lang.String ACTION_WEBVIEW_SETTINGS = "android.settings.WEBVIEW_SETTINGS";

    // End of Intent actions for Settings
    /**
     *
     *
     * @unknown - Private call() method on SettingsProvider to read from 'system' table.
     */
    public static final java.lang.String CALL_METHOD_GET_SYSTEM = "GET_system";

    /**
     *
     *
     * @unknown - Private call() method on SettingsProvider to read from 'secure' table.
     */
    public static final java.lang.String CALL_METHOD_GET_SECURE = "GET_secure";

    /**
     *
     *
     * @unknown - Private call() method on SettingsProvider to read from 'global' table.
     */
    public static final java.lang.String CALL_METHOD_GET_GLOBAL = "GET_global";

    /**
     *
     *
     * @unknown - Specifies that the caller of the fast-path call()-based flow tracks
    the settings generation in order to cache values locally. If this key is
    mapped to a <code>null</code> string extra in the request bundle, the response
    bundle will contain the same key mapped to a parcelable extra which would be
    an {@link android.util.MemoryIntArray}. The response will also contain an
    integer mapped to the {@link #CALL_METHOD_GENERATION_INDEX_KEY} which is the
    index in the array clients should use to lookup the generation. For efficiency
    the caller should request the generation tracking memory array only if it
    doesn't already have it.
     * @see #CALL_METHOD_GENERATION_INDEX_KEY
     */
    public static final java.lang.String CALL_METHOD_TRACK_GENERATION_KEY = "_track_generation";

    /**
     *
     *
     * @unknown Key with the location in the {@link android.util.MemoryIntArray} where
    to look up the generation id of the backing table. The value is an integer.
     * @see #CALL_METHOD_TRACK_GENERATION_KEY
     */
    public static final java.lang.String CALL_METHOD_GENERATION_INDEX_KEY = "_generation_index";

    /**
     *
     *
     * @unknown Key with the settings table generation. The value is an integer.
     * @see #CALL_METHOD_TRACK_GENERATION_KEY
     */
    public static final java.lang.String CALL_METHOD_GENERATION_KEY = "_generation";

    /**
     *
     *
     * @unknown - User handle argument extra to the fast-path call()-based requests
     */
    public static final java.lang.String CALL_METHOD_USER_KEY = "_user";

    /**
     *
     *
     * @unknown - Private call() method to write to 'system' table
     */
    public static final java.lang.String CALL_METHOD_PUT_SYSTEM = "PUT_system";

    /**
     *
     *
     * @unknown - Private call() method to write to 'secure' table
     */
    public static final java.lang.String CALL_METHOD_PUT_SECURE = "PUT_secure";

    /**
     *
     *
     * @unknown - Private call() method to write to 'global' table
     */
    public static final java.lang.String CALL_METHOD_PUT_GLOBAL = "PUT_global";

    /**
     * Activity Extra: Limit available options in launched activity based on the given authority.
     * <p>
     * This can be passed as an extra field in an Activity Intent with one or more syncable content
     * provider's authorities as a String[]. This field is used by some intents to alter the
     * behavior of the called activity.
     * <p>
     * Example: The {@link #ACTION_ADD_ACCOUNT} intent restricts the account types available based
     * on the authority given.
     */
    public static final java.lang.String EXTRA_AUTHORITIES = "authorities";

    /**
     * Activity Extra: Limit available options in launched activity based on the given account
     * types.
     * <p>
     * This can be passed as an extra field in an Activity Intent with one or more account types
     * as a String[]. This field is used by some intents to alter the behavior of the called
     * activity.
     * <p>
     * Example: The {@link #ACTION_ADD_ACCOUNT} intent restricts the account types to the specified
     * list.
     */
    public static final java.lang.String EXTRA_ACCOUNT_TYPES = "account_types";

    public static final java.lang.String EXTRA_INPUT_METHOD_ID = "input_method_id";

    /**
     * Activity Extra: The device identifier to act upon.
     * <p>
     * This can be passed as an extra field in an Activity Intent with a single
     * InputDeviceIdentifier. This field is used by some activities to jump straight into the
     * settings for the given device.
     * <p>
     * Example: The {@link #ACTION_INPUT_METHOD_SETTINGS} intent opens the keyboard layout
     * dialog for the given device.
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_INPUT_DEVICE_IDENTIFIER = "input_device_identifier";

    /**
     * Activity Extra: Enable or disable Airplane Mode.
     * <p>
     * This can be passed as an extra field to the {@link #ACTION_VOICE_CONTROL_AIRPLANE_MODE}
     * intent as a boolean to indicate if it should be enabled.
     */
    public static final java.lang.String EXTRA_AIRPLANE_MODE_ENABLED = "airplane_mode_enabled";

    /**
     * Activity Extra: Enable or disable Battery saver mode.
     * <p>
     * This can be passed as an extra field to the {@link #ACTION_VOICE_CONTROL_BATTERY_SAVER_MODE}
     * intent as a boolean to indicate if it should be enabled.
     */
    public static final java.lang.String EXTRA_BATTERY_SAVER_MODE_ENABLED = "android.settings.extra.battery_saver_mode_enabled";

    /**
     * Activity Extra: Enable or disable Do Not Disturb mode.
     * <p>
     * This can be passed as an extra field to the {@link #ACTION_VOICE_CONTROL_DO_NOT_DISTURB_MODE}
     * intent as a boolean to indicate if it should be enabled.
     */
    public static final java.lang.String EXTRA_DO_NOT_DISTURB_MODE_ENABLED = "android.settings.extra.do_not_disturb_mode_enabled";

    /**
     * Activity Extra: How many minutes to enable do not disturb mode for.
     * <p>
     * This can be passed as an extra field to the {@link #ACTION_VOICE_CONTROL_DO_NOT_DISTURB_MODE}
     * intent to indicate how long do not disturb mode should be enabled for.
     */
    public static final java.lang.String EXTRA_DO_NOT_DISTURB_MODE_MINUTES = "android.settings.extra.do_not_disturb_mode_minutes";

    /**
     * Activity Extra: Number of certificates
     * <p>
     * This can be passed as an extra field to the {@link #ACTION_MONITORING_CERT_INFO}
     * intent to indicate the number of certificates
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_NUMBER_OF_CERTIFICATES = "android.settings.extra.number_of_certificates";

    private static final java.lang.String JID_RESOURCE_PREFIX = "android";

    public static final java.lang.String AUTHORITY = "settings";

    private static final java.lang.String TAG = "Settings";

    private static final boolean LOCAL_LOGV = false;

    // Lock ensures that when enabling/disabling the master location switch, we don't end up
    // with a partial enable/disable state in multi-threaded situations.
    private static final java.lang.Object mLocationSettingsLock = new java.lang.Object();

    public static class SettingNotFoundException extends android.util.AndroidException {
        public SettingNotFoundException(java.lang.String msg) {
            super(msg);
        }
    }

    /**
     * Common base for tables of name/value settings.
     */
    public static class NameValueTable implements android.provider.BaseColumns {
        public static final java.lang.String NAME = "name";

        public static final java.lang.String VALUE = "value";

        protected static boolean putString(android.content.ContentResolver resolver, android.net.Uri uri, java.lang.String name, java.lang.String value) {
            // The database will take care of replacing duplicates.
            try {
                android.content.ContentValues values = new android.content.ContentValues();
                values.put(android.provider.Settings.NameValueTable.NAME, name);
                values.put(android.provider.Settings.NameValueTable.VALUE, value);
                resolver.insert(uri, values);
                return true;
            } catch (android.database.SQLException e) {
                android.util.Log.w(android.provider.Settings.TAG, (("Can't set key " + name) + " in ") + uri, e);
                return false;
            }
        }

        public static android.net.Uri getUriFor(android.net.Uri uri, java.lang.String name) {
            return android.net.Uri.withAppendedPath(uri, name);
        }
    }

    private static final class GenerationTracker {
        private final android.util.MemoryIntArray mArray;

        private final java.lang.Runnable mErrorHandler;

        private final int mIndex;

        private int mCurrentGeneration;

        public GenerationTracker(@android.annotation.NonNull
        android.util.MemoryIntArray array, int index, int generation, java.lang.Runnable errorHandler) {
            mArray = array;
            mIndex = index;
            mErrorHandler = errorHandler;
            mCurrentGeneration = generation;
        }

        public boolean isGenerationChanged() {
            final int currentGeneration = readCurrentGeneration();
            if (currentGeneration >= 0) {
                if (currentGeneration == mCurrentGeneration) {
                    return false;
                }
                mCurrentGeneration = currentGeneration;
            }
            return true;
        }

        private int readCurrentGeneration() {
            try {
                return mArray.get(mIndex);
            } catch (java.io.IOException e) {
                android.util.Log.e(android.provider.Settings.TAG, "Error getting current generation", e);
                if (mErrorHandler != null) {
                    mErrorHandler.run();
                }
            }
            return -1;
        }

        public void destroy() {
            try {
                mArray.close();
            } catch (java.io.IOException e) {
                android.util.Log.e(android.provider.Settings.TAG, "Error closing backing array", e);
                if (mErrorHandler != null) {
                    mErrorHandler.run();
                }
            }
        }
    }

    // Thread-safe.
    private static class NameValueCache {
        private static final boolean DEBUG = false;

        private final android.net.Uri mUri;

        private static final java.lang.String[] SELECT_VALUE = new java.lang.String[]{ android.provider.Settings.NameValueTable.VALUE };

        private static final java.lang.String NAME_EQ_PLACEHOLDER = "name=?";

        // Must synchronize on 'this' to access mValues and mValuesVersion.
        private final java.util.HashMap<java.lang.String, java.lang.String> mValues = new java.util.HashMap<java.lang.String, java.lang.String>();

        // Initially null; set lazily and held forever.  Synchronized on 'this'.
        private android.content.IContentProvider mContentProvider = null;

        // The method we'll call (or null, to not use) on the provider
        // for the fast path of retrieving settings.
        private final java.lang.String mCallGetCommand;

        private final java.lang.String mCallSetCommand;

        @com.android.internal.annotations.GuardedBy("this")
        private android.provider.Settings.GenerationTracker mGenerationTracker;

        public NameValueCache(android.net.Uri uri, java.lang.String getCommand, java.lang.String setCommand) {
            mUri = uri;
            mCallGetCommand = getCommand;
            mCallSetCommand = setCommand;
        }

        private android.content.IContentProvider lazyGetProvider(android.content.ContentResolver cr) {
            android.content.IContentProvider cp = null;
            synchronized(this) {
                cp = mContentProvider;
                if (cp == null) {
                    cp = mContentProvider = cr.acquireProvider(mUri.getAuthority());
                }
            }
            return cp;
        }

        public boolean putStringForUser(android.content.ContentResolver cr, java.lang.String name, java.lang.String value, final int userHandle) {
            try {
                android.os.Bundle arg = new android.os.Bundle();
                arg.putString(android.provider.Settings.NameValueTable.VALUE, value);
                arg.putInt(android.provider.Settings.CALL_METHOD_USER_KEY, userHandle);
                android.content.IContentProvider cp = lazyGetProvider(cr);
                cp.call(cr.getPackageName(), mCallSetCommand, name, arg);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.provider.Settings.TAG, (("Can't set key " + name) + " in ") + mUri, e);
                return false;
            }
            return true;
        }

        public java.lang.String getStringForUser(android.content.ContentResolver cr, java.lang.String name, final int userHandle) {
            final boolean isSelf = userHandle == android.os.UserHandle.myUserId();
            if (isSelf) {
                synchronized(this) {
                    if (mGenerationTracker != null) {
                        if (mGenerationTracker.isGenerationChanged()) {
                            if (android.provider.Settings.NameValueCache.DEBUG) {
                                android.util.Log.i(android.provider.Settings.TAG, (((("Generation changed for type:" + mUri.getPath()) + " in package:") + cr.getPackageName()) + " and user:") + userHandle);
                            }
                            mValues.clear();
                        } else
                            if (mValues.containsKey(name)) {
                                return mValues.get(name);
                            }

                    }
                }
            } else {
                if (android.provider.Settings.LOCAL_LOGV)
                    android.util.Log.v(android.provider.Settings.TAG, ((("get setting for user " + userHandle) + " by user ") + android.os.UserHandle.myUserId()) + " so skipping cache");

            }
            android.content.IContentProvider cp = lazyGetProvider(cr);
            // Try the fast path first, not using query().  If this
            // fails (alternate Settings provider that doesn't support
            // this interface?) then we fall back to the query/table
            // interface.
            if (mCallGetCommand != null) {
                try {
                    android.os.Bundle args = null;
                    if (!isSelf) {
                        args = new android.os.Bundle();
                        args.putInt(android.provider.Settings.CALL_METHOD_USER_KEY, userHandle);
                    }
                    boolean needsGenerationTracker = false;
                    synchronized(this) {
                        if (isSelf && (mGenerationTracker == null)) {
                            needsGenerationTracker = true;
                            if (args == null) {
                                args = new android.os.Bundle();
                            }
                            args.putString(android.provider.Settings.CALL_METHOD_TRACK_GENERATION_KEY, null);
                            if (android.provider.Settings.NameValueCache.DEBUG) {
                                android.util.Log.i(android.provider.Settings.TAG, (((("Requested generation tracker for type: " + mUri.getPath()) + " in package:") + cr.getPackageName()) + " and user:") + userHandle);
                            }
                        }
                    }
                    android.os.Bundle b = cp.call(cr.getPackageName(), mCallGetCommand, name, args);
                    if (b != null) {
                        java.lang.String value = b.getString(android.provider.Settings.NameValueTable.VALUE);
                        // Don't update our cache for reads of other users' data
                        if (isSelf) {
                            synchronized(this) {
                                if (needsGenerationTracker) {
                                    android.util.MemoryIntArray array = b.getParcelable(android.provider.Settings.CALL_METHOD_TRACK_GENERATION_KEY);
                                    final int index = b.getInt(android.provider.Settings.CALL_METHOD_GENERATION_INDEX_KEY, -1);
                                    if ((array != null) && (index >= 0)) {
                                        final int generation = b.getInt(android.provider.Settings.CALL_METHOD_GENERATION_KEY, 0);
                                        if (android.provider.Settings.NameValueCache.DEBUG) {
                                            android.util.Log.i(android.provider.Settings.TAG, (((((("Received generation tracker for type:" + mUri.getPath()) + " in package:") + cr.getPackageName()) + " and user:") + userHandle) + " with index:") + index);
                                        }
                                        mGenerationTracker = new android.provider.Settings.GenerationTracker(array, index, generation, () -> {
                                            synchronized(this) {
                                                android.util.Log.e(android.provider.Settings.TAG, "Error accessing generation" + " tracker - removing");
                                                if (mGenerationTracker != null) {
                                                    android.provider.Settings.GenerationTracker generationTracker = mGenerationTracker;
                                                    mGenerationTracker = null;
                                                    generationTracker.destroy();
                                                    mValues.clear();
                                                }
                                            }
                                        });
                                    }
                                }
                                mValues.put(name, value);
                            }
                        } else {
                            if (android.provider.Settings.LOCAL_LOGV)
                                android.util.Log.i(android.provider.Settings.TAG, ((("call-query of user " + userHandle) + " by ") + android.os.UserHandle.myUserId()) + " so not updating cache");

                        }
                        return value;
                    }
                    // If the response Bundle is null, we fall through
                    // to the query interface below.
                } catch (android.os.RemoteException e) {
                    // Not supported by the remote side?  Fall through
                    // to query().
                }
            }
            android.database.Cursor c = null;
            try {
                c = cp.query(cr.getPackageName(), mUri, android.provider.Settings.NameValueCache.SELECT_VALUE, android.provider.Settings.NameValueCache.NAME_EQ_PLACEHOLDER, new java.lang.String[]{ name }, null, null);
                if (c == null) {
                    android.util.Log.w(android.provider.Settings.TAG, (("Can't get key " + name) + " from ") + mUri);
                    return null;
                }
                java.lang.String value = (c.moveToNext()) ? c.getString(0) : null;
                synchronized(this) {
                    mValues.put(name, value);
                }
                if (android.provider.Settings.LOCAL_LOGV) {
                    android.util.Log.v(android.provider.Settings.TAG, (((("cache miss [" + mUri.getLastPathSegment()) + "]: ") + name) + " = ") + (value == null ? "(null)" : value));
                }
                return value;
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.provider.Settings.TAG, (("Can't get key " + name) + " from ") + mUri, e);
                return null;// Return null, but don't cache it.

            } finally {
                if (c != null)
                    c.close();

            }
        }
    }

    /**
     * Checks if the specified context can draw on top of other apps. As of API
     * level 23, an app cannot draw on top of other apps unless it declares the
     * {@link android.Manifest.permission#SYSTEM_ALERT_WINDOW} permission in its
     * manifest, <em>and</em> the user specifically grants the app this
     * capability. To prompt the user to grant this approval, the app must send an
     * intent with the action
     * {@link android.provider.Settings#ACTION_MANAGE_OVERLAY_PERMISSION}, which
     * causes the system to display a permission management screen.
     *
     * @param context
     * 		App context.
     * @return true if the specified context can draw on top of other apps, false otherwise
     */
    public static boolean canDrawOverlays(android.content.Context context) {
        return android.provider.Settings.isCallingPackageAllowedToDrawOverlays(context, android.os.Process.myUid(), context.getOpPackageName(), false);
    }

    /**
     * System settings, containing miscellaneous system preferences.  This
     * table holds simple name/value pairs.  There are convenience
     * functions for accessing individual settings entries.
     */
    public static final class System extends android.provider.Settings.NameValueTable {
        private static final float DEFAULT_FONT_SCALE = 1.0F;

        /**
         *
         *
         * @unknown 
         */
        public static interface Validator {
            public boolean validate(java.lang.String value);
        }

        /**
         * The content:// style URL for this table
         */
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse(("content://" + android.provider.Settings.AUTHORITY) + "/system");

        private static final android.provider.Settings.NameValueCache sNameValueCache = new android.provider.Settings.NameValueCache(android.provider.Settings.System.CONTENT_URI, android.provider.Settings.CALL_METHOD_GET_SYSTEM, android.provider.Settings.CALL_METHOD_PUT_SYSTEM);

        private static final java.util.HashSet<java.lang.String> MOVED_TO_SECURE;

        static {
            MOVED_TO_SECURE = new java.util.HashSet<java.lang.String>(30);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.ANDROID_ID);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.HTTP_PROXY);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.LOCK_BIOMETRIC_WEAK_FLAGS);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.LOCK_PATTERN_ENABLED);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.LOCK_PATTERN_VISIBLE);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.LOCK_PATTERN_TACTILE_FEEDBACK_ENABLED);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.LOGGING_ID);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.PARENTAL_CONTROL_ENABLED);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.PARENTAL_CONTROL_LAST_UPDATE);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.PARENTAL_CONTROL_REDIRECT_URL);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.SETTINGS_CLASSNAME);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.USE_GOOGLE_MAIL);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.WIFI_NUM_OPEN_NETWORKS_KEPT);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.WIFI_ON);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.WIFI_WATCHDOG_ACCEPTABLE_PACKET_LOSS_PERCENTAGE);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.WIFI_WATCHDOG_AP_COUNT);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.WIFI_WATCHDOG_BACKGROUND_CHECK_DELAY_MS);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.WIFI_WATCHDOG_BACKGROUND_CHECK_ENABLED);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.WIFI_WATCHDOG_BACKGROUND_CHECK_TIMEOUT_MS);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.WIFI_WATCHDOG_INITIAL_IGNORED_PING_COUNT);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.WIFI_WATCHDOG_MAX_AP_CHECKS);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.WIFI_WATCHDOG_ON);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.WIFI_WATCHDOG_PING_COUNT);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.WIFI_WATCHDOG_PING_DELAY_MS);
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.WIFI_WATCHDOG_PING_TIMEOUT_MS);
            // At one time in System, then Global, but now back in Secure
            android.provider.Settings.System.MOVED_TO_SECURE.add(android.provider.Settings.Secure.INSTALL_NON_MARKET_APPS);
        }

        private static final java.util.HashSet<java.lang.String> MOVED_TO_GLOBAL;

        private static final java.util.HashSet<java.lang.String> MOVED_TO_SECURE_THEN_GLOBAL;

        static {
            MOVED_TO_GLOBAL = new java.util.HashSet<java.lang.String>();
            MOVED_TO_SECURE_THEN_GLOBAL = new java.util.HashSet<java.lang.String>();
            // these were originally in system but migrated to secure in the past,
            // so are duplicated in the Secure.* namespace
            android.provider.Settings.System.MOVED_TO_SECURE_THEN_GLOBAL.add(android.provider.Settings.Global.ADB_ENABLED);
            android.provider.Settings.System.MOVED_TO_SECURE_THEN_GLOBAL.add(android.provider.Settings.Global.BLUETOOTH_ON);
            android.provider.Settings.System.MOVED_TO_SECURE_THEN_GLOBAL.add(android.provider.Settings.Global.DATA_ROAMING);
            android.provider.Settings.System.MOVED_TO_SECURE_THEN_GLOBAL.add(android.provider.Settings.Global.DEVICE_PROVISIONED);
            android.provider.Settings.System.MOVED_TO_SECURE_THEN_GLOBAL.add(android.provider.Settings.Global.USB_MASS_STORAGE_ENABLED);
            android.provider.Settings.System.MOVED_TO_SECURE_THEN_GLOBAL.add(android.provider.Settings.Global.HTTP_PROXY);
            // these are moving directly from system to global
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.AIRPLANE_MODE_ON);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.AIRPLANE_MODE_RADIOS);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.AIRPLANE_MODE_TOGGLEABLE_RADIOS);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.AUTO_TIME);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.AUTO_TIME_ZONE);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.CAR_DOCK_SOUND);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.CAR_UNDOCK_SOUND);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DESK_DOCK_SOUND);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DESK_UNDOCK_SOUND);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DOCK_SOUNDS_ENABLED);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.LOCK_SOUND);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.UNLOCK_SOUND);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.LOW_BATTERY_SOUND);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.POWER_SOUNDS_ENABLED);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.STAY_ON_WHILE_PLUGGED_IN);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_SLEEP_POLICY);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.MODE_RINGER);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WINDOW_ANIMATION_SCALE);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.TRANSITION_ANIMATION_SCALE);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.ANIMATOR_DURATION_SCALE);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.FANCY_IME_ANIMATIONS);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.COMPATIBILITY_MODE);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.EMERGENCY_TONE);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.CALL_AUTO_RETRY);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DEBUG_APP);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WAIT_FOR_DEBUGGER);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.ALWAYS_FINISH_ACTIVITIES);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.TZINFO_UPDATE_CONTENT_URL);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.TZINFO_UPDATE_METADATA_URL);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.SELINUX_UPDATE_CONTENT_URL);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.SELINUX_UPDATE_METADATA_URL);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.SMS_SHORT_CODES_UPDATE_CONTENT_URL);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.SMS_SHORT_CODES_UPDATE_METADATA_URL);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.CERT_PIN_UPDATE_CONTENT_URL);
            android.provider.Settings.System.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.CERT_PIN_UPDATE_METADATA_URL);
        }

        private static final android.provider.Settings.System.Validator sBooleanValidator = new android.provider.Settings.System.DiscreteValueValidator(new java.lang.String[]{ "0", "1" });

        private static final android.provider.Settings.System.Validator sNonNegativeIntegerValidator = new android.provider.Settings.System.Validator() {
            @java.lang.Override
            public boolean validate(java.lang.String value) {
                try {
                    return java.lang.Integer.parseInt(value) >= 0;
                } catch (java.lang.NumberFormatException e) {
                    return false;
                }
            }
        };

        private static final android.provider.Settings.System.Validator sUriValidator = new android.provider.Settings.System.Validator() {
            @java.lang.Override
            public boolean validate(java.lang.String value) {
                try {
                    android.net.Uri.decode(value);
                    return true;
                } catch (java.lang.IllegalArgumentException e) {
                    return false;
                }
            }
        };

        private static final android.provider.Settings.System.Validator sLenientIpAddressValidator = new android.provider.Settings.System.Validator() {
            private static final int MAX_IPV6_LENGTH = 45;

            @java.lang.Override
            public boolean validate(java.lang.String value) {
                return value.length() <= MAX_IPV6_LENGTH;
            }
        };

        /**
         *
         *
         * @unknown 
         */
        public static void getMovedToGlobalSettings(java.util.Set<java.lang.String> outKeySet) {
            outKeySet.addAll(android.provider.Settings.System.MOVED_TO_GLOBAL);
            outKeySet.addAll(android.provider.Settings.System.MOVED_TO_SECURE_THEN_GLOBAL);
        }

        /**
         *
         *
         * @unknown 
         */
        public static void getMovedToSecureSettings(java.util.Set<java.lang.String> outKeySet) {
            outKeySet.addAll(android.provider.Settings.System.MOVED_TO_SECURE);
        }

        /**
         *
         *
         * @unknown 
         */
        public static void getNonLegacyMovedKeys(java.util.HashSet<java.lang.String> outKeySet) {
            outKeySet.addAll(android.provider.Settings.System.MOVED_TO_GLOBAL);
        }

        /**
         * Look up a name in the database.
         *
         * @param resolver
         * 		to access the database with
         * @param name
         * 		to look up in the table
         * @return the corresponding value, or null if not present
         */
        public static java.lang.String getString(android.content.ContentResolver resolver, java.lang.String name) {
            return android.provider.Settings.System.getStringForUser(resolver, name, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static java.lang.String getStringForUser(android.content.ContentResolver resolver, java.lang.String name, int userHandle) {
            if (android.provider.Settings.System.MOVED_TO_SECURE.contains(name)) {
                android.util.Log.w(android.provider.Settings.TAG, (("Setting " + name) + " has moved from android.provider.Settings.System") + " to android.provider.Settings.Secure, returning read-only value.");
                return android.provider.Settings.Secure.getStringForUser(resolver, name, userHandle);
            }
            if (android.provider.Settings.System.MOVED_TO_GLOBAL.contains(name) || android.provider.Settings.System.MOVED_TO_SECURE_THEN_GLOBAL.contains(name)) {
                android.util.Log.w(android.provider.Settings.TAG, (("Setting " + name) + " has moved from android.provider.Settings.System") + " to android.provider.Settings.Global, returning read-only value.");
                return android.provider.Settings.Global.getStringForUser(resolver, name, userHandle);
            }
            return android.provider.Settings.System.sNameValueCache.getStringForUser(resolver, name, userHandle);
        }

        /**
         * Store a name/value pair into the database.
         *
         * @param resolver
         * 		to access the database with
         * @param name
         * 		to store
         * @param value
         * 		to associate with the name
         * @return true if the value was set, false on database errors
         */
        public static boolean putString(android.content.ContentResolver resolver, java.lang.String name, java.lang.String value) {
            return android.provider.Settings.System.putStringForUser(resolver, name, value, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static boolean putStringForUser(android.content.ContentResolver resolver, java.lang.String name, java.lang.String value, int userHandle) {
            if (android.provider.Settings.System.MOVED_TO_SECURE.contains(name)) {
                android.util.Log.w(android.provider.Settings.TAG, (("Setting " + name) + " has moved from android.provider.Settings.System") + " to android.provider.Settings.Secure, value is unchanged.");
                return false;
            }
            if (android.provider.Settings.System.MOVED_TO_GLOBAL.contains(name) || android.provider.Settings.System.MOVED_TO_SECURE_THEN_GLOBAL.contains(name)) {
                android.util.Log.w(android.provider.Settings.TAG, (("Setting " + name) + " has moved from android.provider.Settings.System") + " to android.provider.Settings.Global, value is unchanged.");
                return false;
            }
            return android.provider.Settings.System.sNameValueCache.putStringForUser(resolver, name, value, userHandle);
        }

        /**
         * Construct the content URI for a particular name/value pair,
         * useful for monitoring changes with a ContentObserver.
         *
         * @param name
         * 		to look up in the table
         * @return the corresponding content URI, or null if not present
         */
        public static android.net.Uri getUriFor(java.lang.String name) {
            if (android.provider.Settings.System.MOVED_TO_SECURE.contains(name)) {
                android.util.Log.w(android.provider.Settings.TAG, (("Setting " + name) + " has moved from android.provider.Settings.System") + " to android.provider.Settings.Secure, returning Secure URI.");
                return android.provider.Settings.Secure.getUriFor(android.provider.Settings.Secure.CONTENT_URI, name);
            }
            if (android.provider.Settings.System.MOVED_TO_GLOBAL.contains(name) || android.provider.Settings.System.MOVED_TO_SECURE_THEN_GLOBAL.contains(name)) {
                android.util.Log.w(android.provider.Settings.TAG, (("Setting " + name) + " has moved from android.provider.Settings.System") + " to android.provider.Settings.Global, returning read-only global URI.");
                return android.provider.Settings.Global.getUriFor(android.provider.Settings.Global.CONTENT_URI, name);
            }
            return android.provider.Settings.NameValueTable.getUriFor(android.provider.Settings.System.CONTENT_URI, name);
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as an integer.  Note that internally setting values are always
         * stored as strings; this function converts the string to an integer
         * for you.  The default value will be returned if the setting is
         * not defined or not an integer.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @param def
         * 		Value to return if the setting is not defined.
         * @return The setting's current value, or 'def' if it is not defined
        or not a valid integer.
         */
        public static int getInt(android.content.ContentResolver cr, java.lang.String name, int def) {
            return android.provider.Settings.System.getIntForUser(cr, name, def, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static int getIntForUser(android.content.ContentResolver cr, java.lang.String name, int def, int userHandle) {
            java.lang.String v = android.provider.Settings.System.getStringForUser(cr, name, userHandle);
            try {
                return v != null ? java.lang.Integer.parseInt(v) : def;
            } catch (java.lang.NumberFormatException e) {
                return def;
            }
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as an integer.  Note that internally setting values are always
         * stored as strings; this function converts the string to an integer
         * for you.
         * <p>
         * This version does not take a default value.  If the setting has not
         * been set, or the string value is not a number,
         * it throws {@link SettingNotFoundException}.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @throws SettingNotFoundException
         * 		Thrown if a setting by the given
         * 		name can't be found or the setting value is not an integer.
         * @return The setting's current value.
         */
        public static int getInt(android.content.ContentResolver cr, java.lang.String name) throws android.provider.Settings.SettingNotFoundException {
            return android.provider.Settings.System.getIntForUser(cr, name, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static int getIntForUser(android.content.ContentResolver cr, java.lang.String name, int userHandle) throws android.provider.Settings.SettingNotFoundException {
            java.lang.String v = android.provider.Settings.System.getStringForUser(cr, name, userHandle);
            try {
                return java.lang.Integer.parseInt(v);
            } catch (java.lang.NumberFormatException e) {
                throw new android.provider.Settings.SettingNotFoundException(name);
            }
        }

        /**
         * Convenience function for updating a single settings value as an
         * integer. This will either create a new entry in the table if the
         * given name does not exist, or modify the value of the existing row
         * with that name.  Note that internally setting values are always
         * stored as strings, so this function converts the given value to a
         * string before storing it.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to modify.
         * @param value
         * 		The new value for the setting.
         * @return true if the value was set, false on database errors
         */
        public static boolean putInt(android.content.ContentResolver cr, java.lang.String name, int value) {
            return android.provider.Settings.System.putIntForUser(cr, name, value, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static boolean putIntForUser(android.content.ContentResolver cr, java.lang.String name, int value, int userHandle) {
            return android.provider.Settings.System.putStringForUser(cr, name, java.lang.Integer.toString(value), userHandle);
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as a {@code long}.  Note that internally setting values are always
         * stored as strings; this function converts the string to a {@code long}
         * for you.  The default value will be returned if the setting is
         * not defined or not a {@code long}.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @param def
         * 		Value to return if the setting is not defined.
         * @return The setting's current value, or 'def' if it is not defined
        or not a valid {@code long}.
         */
        public static long getLong(android.content.ContentResolver cr, java.lang.String name, long def) {
            return android.provider.Settings.System.getLongForUser(cr, name, def, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static long getLongForUser(android.content.ContentResolver cr, java.lang.String name, long def, int userHandle) {
            java.lang.String valString = android.provider.Settings.System.getStringForUser(cr, name, userHandle);
            long value;
            try {
                value = (valString != null) ? java.lang.Long.parseLong(valString) : def;
            } catch (java.lang.NumberFormatException e) {
                value = def;
            }
            return value;
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as a {@code long}.  Note that internally setting values are always
         * stored as strings; this function converts the string to a {@code long}
         * for you.
         * <p>
         * This version does not take a default value.  If the setting has not
         * been set, or the string value is not a number,
         * it throws {@link SettingNotFoundException}.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @return The setting's current value.
         * @throws SettingNotFoundException
         * 		Thrown if a setting by the given
         * 		name can't be found or the setting value is not an integer.
         */
        public static long getLong(android.content.ContentResolver cr, java.lang.String name) throws android.provider.Settings.SettingNotFoundException {
            return android.provider.Settings.System.getLongForUser(cr, name, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static long getLongForUser(android.content.ContentResolver cr, java.lang.String name, int userHandle) throws android.provider.Settings.SettingNotFoundException {
            java.lang.String valString = android.provider.Settings.System.getStringForUser(cr, name, userHandle);
            try {
                return java.lang.Long.parseLong(valString);
            } catch (java.lang.NumberFormatException e) {
                throw new android.provider.Settings.SettingNotFoundException(name);
            }
        }

        /**
         * Convenience function for updating a single settings value as a long
         * integer. This will either create a new entry in the table if the
         * given name does not exist, or modify the value of the existing row
         * with that name.  Note that internally setting values are always
         * stored as strings, so this function converts the given value to a
         * string before storing it.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to modify.
         * @param value
         * 		The new value for the setting.
         * @return true if the value was set, false on database errors
         */
        public static boolean putLong(android.content.ContentResolver cr, java.lang.String name, long value) {
            return android.provider.Settings.System.putLongForUser(cr, name, value, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static boolean putLongForUser(android.content.ContentResolver cr, java.lang.String name, long value, int userHandle) {
            return android.provider.Settings.System.putStringForUser(cr, name, java.lang.Long.toString(value), userHandle);
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as a floating point number.  Note that internally setting values are
         * always stored as strings; this function converts the string to an
         * float for you. The default value will be returned if the setting
         * is not defined or not a valid float.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @param def
         * 		Value to return if the setting is not defined.
         * @return The setting's current value, or 'def' if it is not defined
        or not a valid float.
         */
        public static float getFloat(android.content.ContentResolver cr, java.lang.String name, float def) {
            return android.provider.Settings.System.getFloatForUser(cr, name, def, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static float getFloatForUser(android.content.ContentResolver cr, java.lang.String name, float def, int userHandle) {
            java.lang.String v = android.provider.Settings.System.getStringForUser(cr, name, userHandle);
            try {
                return v != null ? java.lang.Float.parseFloat(v) : def;
            } catch (java.lang.NumberFormatException e) {
                return def;
            }
        }

        /**
         * Convenience function for retrieving a single system settings value
         * as a float.  Note that internally setting values are always
         * stored as strings; this function converts the string to a float
         * for you.
         * <p>
         * This version does not take a default value.  If the setting has not
         * been set, or the string value is not a number,
         * it throws {@link SettingNotFoundException}.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @throws SettingNotFoundException
         * 		Thrown if a setting by the given
         * 		name can't be found or the setting value is not a float.
         * @return The setting's current value.
         */
        public static float getFloat(android.content.ContentResolver cr, java.lang.String name) throws android.provider.Settings.SettingNotFoundException {
            return android.provider.Settings.System.getFloatForUser(cr, name, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static float getFloatForUser(android.content.ContentResolver cr, java.lang.String name, int userHandle) throws android.provider.Settings.SettingNotFoundException {
            java.lang.String v = android.provider.Settings.System.getStringForUser(cr, name, userHandle);
            if (v == null) {
                throw new android.provider.Settings.SettingNotFoundException(name);
            }
            try {
                return java.lang.Float.parseFloat(v);
            } catch (java.lang.NumberFormatException e) {
                throw new android.provider.Settings.SettingNotFoundException(name);
            }
        }

        /**
         * Convenience function for updating a single settings value as a
         * floating point number. This will either create a new entry in the
         * table if the given name does not exist, or modify the value of the
         * existing row with that name.  Note that internally setting values
         * are always stored as strings, so this function converts the given
         * value to a string before storing it.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to modify.
         * @param value
         * 		The new value for the setting.
         * @return true if the value was set, false on database errors
         */
        public static boolean putFloat(android.content.ContentResolver cr, java.lang.String name, float value) {
            return android.provider.Settings.System.putFloatForUser(cr, name, value, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static boolean putFloatForUser(android.content.ContentResolver cr, java.lang.String name, float value, int userHandle) {
            return android.provider.Settings.System.putStringForUser(cr, name, java.lang.Float.toString(value), userHandle);
        }

        /**
         * Convenience function to read all of the current
         * configuration-related settings into a
         * {@link Configuration} object.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param outConfig
         * 		Where to place the configuration settings.
         */
        public static void getConfiguration(android.content.ContentResolver cr, android.content.res.Configuration outConfig) {
            /* updateSettingsIfEmpty */
            android.provider.Settings.System.adjustConfigurationForUser(cr, outConfig, android.os.UserHandle.myUserId(), false);
        }

        /**
         *
         *
         * @unknown 
         */
        public static void adjustConfigurationForUser(android.content.ContentResolver cr, android.content.res.Configuration outConfig, int userHandle, boolean updateSettingsIfEmpty) {
            outConfig.fontScale = android.provider.Settings.System.getFloatForUser(cr, android.provider.Settings.System.FONT_SCALE, android.provider.Settings.System.DEFAULT_FONT_SCALE, userHandle);
            if (outConfig.fontScale < 0) {
                outConfig.fontScale = android.provider.Settings.System.DEFAULT_FONT_SCALE;
            }
            final java.lang.String localeValue = android.provider.Settings.System.getStringForUser(cr, android.provider.Settings.System.SYSTEM_LOCALES, userHandle);
            if (localeValue != null) {
                outConfig.setLocales(android.os.LocaleList.forLanguageTags(localeValue));
            } else {
                // Do not update configuration with emtpy settings since we need to take over the
                // locale list of previous user if the settings value is empty. This happens when a
                // new user is created.
                if (updateSettingsIfEmpty) {
                    // Make current configuration persistent. This is necessary the first time a
                    // user log in. At the first login, the configuration settings are empty, so we
                    // need to store the adjusted configuration as the initial settings.
                    android.provider.Settings.System.putStringForUser(cr, android.provider.Settings.System.SYSTEM_LOCALES, outConfig.getLocales().toLanguageTags(), userHandle);
                }
            }
        }

        /**
         *
         *
         * @unknown Erase the fields in the Configuration that should be applied
        by the settings.
         */
        public static void clearConfiguration(android.content.res.Configuration inoutConfig) {
            inoutConfig.fontScale = 0;
            if ((!inoutConfig.userSetLocale) && (!inoutConfig.getLocales().isEmpty())) {
                inoutConfig.clearLocales();
            }
        }

        /**
         * Convenience function to write a batch of configuration-related
         * settings from a {@link Configuration} object.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param config
         * 		The settings to write.
         * @return true if the values were set, false on database errors
         */
        public static boolean putConfiguration(android.content.ContentResolver cr, android.content.res.Configuration config) {
            return android.provider.Settings.System.putConfigurationForUser(cr, config, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static boolean putConfigurationForUser(android.content.ContentResolver cr, android.content.res.Configuration config, int userHandle) {
            return android.provider.Settings.System.putFloatForUser(cr, android.provider.Settings.System.FONT_SCALE, config.fontScale, userHandle) && android.provider.Settings.System.putStringForUser(cr, android.provider.Settings.System.SYSTEM_LOCALES, config.getLocales().toLanguageTags(), userHandle);
        }

        /**
         *
         *
         * @unknown 
         */
        public static boolean hasInterestingConfigurationChanges(int changes) {
            return ((changes & android.content.pm.ActivityInfo.CONFIG_FONT_SCALE) != 0) || ((changes & android.content.pm.ActivityInfo.CONFIG_LOCALE) != 0);
        }

        /**
         *
         *
         * @deprecated - Do not use
         */
        @java.lang.Deprecated
        public static boolean getShowGTalkServiceStatus(android.content.ContentResolver cr) {
            return android.provider.Settings.System.getShowGTalkServiceStatusForUser(cr, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         * @deprecated - Do not use
         */
        public static boolean getShowGTalkServiceStatusForUser(android.content.ContentResolver cr, int userHandle) {
            return android.provider.Settings.System.getIntForUser(cr, android.provider.Settings.System.SHOW_GTALK_SERVICE_STATUS, 0, userHandle) != 0;
        }

        /**
         *
         *
         * @deprecated - Do not use
         */
        @java.lang.Deprecated
        public static void setShowGTalkServiceStatus(android.content.ContentResolver cr, boolean flag) {
            android.provider.Settings.System.setShowGTalkServiceStatusForUser(cr, flag, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         * @deprecated - Do not use
         */
        @java.lang.Deprecated
        public static void setShowGTalkServiceStatusForUser(android.content.ContentResolver cr, boolean flag, int userHandle) {
            android.provider.Settings.System.putIntForUser(cr, android.provider.Settings.System.SHOW_GTALK_SERVICE_STATUS, flag ? 1 : 0, userHandle);
        }

        private static final class DiscreteValueValidator implements android.provider.Settings.System.Validator {
            private final java.lang.String[] mValues;

            public DiscreteValueValidator(java.lang.String[] values) {
                mValues = values;
            }

            @java.lang.Override
            public boolean validate(java.lang.String value) {
                return com.android.internal.util.ArrayUtils.contains(mValues, value);
            }
        }

        private static final class InclusiveIntegerRangeValidator implements android.provider.Settings.System.Validator {
            private final int mMin;

            private final int mMax;

            public InclusiveIntegerRangeValidator(int min, int max) {
                mMin = min;
                mMax = max;
            }

            @java.lang.Override
            public boolean validate(java.lang.String value) {
                try {
                    final int intValue = java.lang.Integer.parseInt(value);
                    return (intValue >= mMin) && (intValue <= mMax);
                } catch (java.lang.NumberFormatException e) {
                    return false;
                }
            }
        }

        private static final class InclusiveFloatRangeValidator implements android.provider.Settings.System.Validator {
            private final float mMin;

            private final float mMax;

            public InclusiveFloatRangeValidator(float min, float max) {
                mMin = min;
                mMax = max;
            }

            @java.lang.Override
            public boolean validate(java.lang.String value) {
                try {
                    final float floatValue = java.lang.Float.parseFloat(value);
                    return (floatValue >= mMin) && (floatValue <= mMax);
                } catch (java.lang.NumberFormatException e) {
                    return false;
                }
            }
        }

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#STAY_ON_WHILE_PLUGGED_IN} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String STAY_ON_WHILE_PLUGGED_IN = android.provider.Settings.Global.STAY_ON_WHILE_PLUGGED_IN;

        /**
         * What happens when the user presses the end call button if they're not
         * on a call.<br/>
         * <b>Values:</b><br/>
         * 0 - The end button does nothing.<br/>
         * 1 - The end button goes to the home screen.<br/>
         * 2 - The end button puts the device to sleep and locks the keyguard.<br/>
         * 3 - The end button goes to the home screen.  If the user is already on the
         * home screen, it puts the device to sleep.
         */
        public static final java.lang.String END_BUTTON_BEHAVIOR = "end_button_behavior";

        private static final android.provider.Settings.System.Validator END_BUTTON_BEHAVIOR_VALIDATOR = new android.provider.Settings.System.InclusiveIntegerRangeValidator(0, 3);

        /**
         * END_BUTTON_BEHAVIOR value for "go home".
         *
         * @unknown 
         */
        public static final int END_BUTTON_BEHAVIOR_HOME = 0x1;

        /**
         * END_BUTTON_BEHAVIOR value for "go to sleep".
         *
         * @unknown 
         */
        public static final int END_BUTTON_BEHAVIOR_SLEEP = 0x2;

        /**
         * END_BUTTON_BEHAVIOR default value.
         *
         * @unknown 
         */
        public static final int END_BUTTON_BEHAVIOR_DEFAULT = android.provider.Settings.System.END_BUTTON_BEHAVIOR_SLEEP;

        /**
         * Is advanced settings mode turned on. 0 == no, 1 == yes
         *
         * @unknown 
         */
        public static final java.lang.String ADVANCED_SETTINGS = "advanced_settings";

        private static final android.provider.Settings.System.Validator ADVANCED_SETTINGS_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * ADVANCED_SETTINGS default value.
         *
         * @unknown 
         */
        public static final int ADVANCED_SETTINGS_DEFAULT = 0;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#AIRPLANE_MODE_ON} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String AIRPLANE_MODE_ON = android.provider.Settings.Global.AIRPLANE_MODE_ON;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#RADIO_BLUETOOTH} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String RADIO_BLUETOOTH = android.provider.Settings.Global.RADIO_BLUETOOTH;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#RADIO_WIFI} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String RADIO_WIFI = android.provider.Settings.Global.RADIO_WIFI;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#RADIO_WIMAX} instead
        {@hide }
         */
        @java.lang.Deprecated
        public static final java.lang.String RADIO_WIMAX = android.provider.Settings.Global.RADIO_WIMAX;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#RADIO_CELL} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String RADIO_CELL = android.provider.Settings.Global.RADIO_CELL;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#RADIO_NFC} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String RADIO_NFC = android.provider.Settings.Global.RADIO_NFC;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#AIRPLANE_MODE_RADIOS} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String AIRPLANE_MODE_RADIOS = android.provider.Settings.Global.AIRPLANE_MODE_RADIOS;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#AIRPLANE_MODE_TOGGLEABLE_RADIOS} instead

        {@hide }
         */
        @java.lang.Deprecated
        public static final java.lang.String AIRPLANE_MODE_TOGGLEABLE_RADIOS = android.provider.Settings.Global.AIRPLANE_MODE_TOGGLEABLE_RADIOS;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#WIFI_SLEEP_POLICY} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_SLEEP_POLICY = android.provider.Settings.Global.WIFI_SLEEP_POLICY;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#WIFI_SLEEP_POLICY_DEFAULT} instead
         */
        @java.lang.Deprecated
        public static final int WIFI_SLEEP_POLICY_DEFAULT = android.provider.Settings.Global.WIFI_SLEEP_POLICY_DEFAULT;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#WIFI_SLEEP_POLICY_NEVER_WHILE_PLUGGED} instead
         */
        @java.lang.Deprecated
        public static final int WIFI_SLEEP_POLICY_NEVER_WHILE_PLUGGED = android.provider.Settings.Global.WIFI_SLEEP_POLICY_NEVER_WHILE_PLUGGED;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#WIFI_SLEEP_POLICY_NEVER} instead
         */
        @java.lang.Deprecated
        public static final int WIFI_SLEEP_POLICY_NEVER = android.provider.Settings.Global.WIFI_SLEEP_POLICY_NEVER;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#MODE_RINGER} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String MODE_RINGER = android.provider.Settings.Global.MODE_RINGER;

        /**
         * Whether to use static IP and other static network attributes.
         * <p>
         * Set to 1 for true and 0 for false.
         *
         * @deprecated Use {@link WifiManager} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_USE_STATIC_IP = "wifi_use_static_ip";

        private static final android.provider.Settings.System.Validator WIFI_USE_STATIC_IP_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * The static IP address.
         * <p>
         * Example: "192.168.1.51"
         *
         * @deprecated Use {@link WifiManager} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_STATIC_IP = "wifi_static_ip";

        private static final android.provider.Settings.System.Validator WIFI_STATIC_IP_VALIDATOR = android.provider.Settings.System.sLenientIpAddressValidator;

        /**
         * If using static IP, the gateway's IP address.
         * <p>
         * Example: "192.168.1.1"
         *
         * @deprecated Use {@link WifiManager} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_STATIC_GATEWAY = "wifi_static_gateway";

        private static final android.provider.Settings.System.Validator WIFI_STATIC_GATEWAY_VALIDATOR = android.provider.Settings.System.sLenientIpAddressValidator;

        /**
         * If using static IP, the net mask.
         * <p>
         * Example: "255.255.255.0"
         *
         * @deprecated Use {@link WifiManager} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_STATIC_NETMASK = "wifi_static_netmask";

        private static final android.provider.Settings.System.Validator WIFI_STATIC_NETMASK_VALIDATOR = android.provider.Settings.System.sLenientIpAddressValidator;

        /**
         * If using static IP, the primary DNS's IP address.
         * <p>
         * Example: "192.168.1.1"
         *
         * @deprecated Use {@link WifiManager} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_STATIC_DNS1 = "wifi_static_dns1";

        private static final android.provider.Settings.System.Validator WIFI_STATIC_DNS1_VALIDATOR = android.provider.Settings.System.sLenientIpAddressValidator;

        /**
         * If using static IP, the secondary DNS's IP address.
         * <p>
         * Example: "192.168.1.2"
         *
         * @deprecated Use {@link WifiManager} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_STATIC_DNS2 = "wifi_static_dns2";

        private static final android.provider.Settings.System.Validator WIFI_STATIC_DNS2_VALIDATOR = android.provider.Settings.System.sLenientIpAddressValidator;

        /**
         * Determines whether remote devices may discover and/or connect to
         * this device.
         * <P>Type: INT</P>
         * 2 -- discoverable and connectable
         * 1 -- connectable but not discoverable
         * 0 -- neither connectable nor discoverable
         */
        public static final java.lang.String BLUETOOTH_DISCOVERABILITY = "bluetooth_discoverability";

        private static final android.provider.Settings.System.Validator BLUETOOTH_DISCOVERABILITY_VALIDATOR = new android.provider.Settings.System.InclusiveIntegerRangeValidator(0, 2);

        /**
         * Bluetooth discoverability timeout.  If this value is nonzero, then
         * Bluetooth becomes discoverable for a certain number of seconds,
         * after which is becomes simply connectable.  The value is in seconds.
         */
        public static final java.lang.String BLUETOOTH_DISCOVERABILITY_TIMEOUT = "bluetooth_discoverability_timeout";

        private static final android.provider.Settings.System.Validator BLUETOOTH_DISCOVERABILITY_TIMEOUT_VALIDATOR = android.provider.Settings.System.sNonNegativeIntegerValidator;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Secure#LOCK_PATTERN_ENABLED}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String LOCK_PATTERN_ENABLED = android.provider.Settings.Secure.LOCK_PATTERN_ENABLED;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Secure#LOCK_PATTERN_VISIBLE}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String LOCK_PATTERN_VISIBLE = "lock_pattern_visible_pattern";

        /**
         *
         *
         * @deprecated Use
        {@link android.provider.Settings.Secure#LOCK_PATTERN_TACTILE_FEEDBACK_ENABLED}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String LOCK_PATTERN_TACTILE_FEEDBACK_ENABLED = "lock_pattern_tactile_feedback_enabled";

        /**
         * A formatted string of the next alarm that is set, or the empty string
         * if there is no alarm set.
         *
         * @deprecated Use {@link android.app.AlarmManager#getNextAlarmClock()}.
         */
        @java.lang.Deprecated
        public static final java.lang.String NEXT_ALARM_FORMATTED = "next_alarm_formatted";

        private static final android.provider.Settings.System.Validator NEXT_ALARM_FORMATTED_VALIDATOR = new android.provider.Settings.System.Validator() {
            private static final int MAX_LENGTH = 1000;

            @java.lang.Override
            public boolean validate(java.lang.String value) {
                // TODO: No idea what the correct format is.
                return (value == null) || (value.length() < MAX_LENGTH);
            }
        };

        /**
         * Scaling factor for fonts, float.
         */
        public static final java.lang.String FONT_SCALE = "font_scale";

        private static final android.provider.Settings.System.Validator FONT_SCALE_VALIDATOR = new android.provider.Settings.System.Validator() {
            @java.lang.Override
            public boolean validate(java.lang.String value) {
                try {
                    return java.lang.Float.parseFloat(value) >= 0;
                } catch (java.lang.NumberFormatException e) {
                    return false;
                }
            }
        };

        /**
         * The serialized system locale value.
         *
         * Do not use this value directory.
         * To get system locale, use {@link LocaleList#getDefault} instead.
         * To update system locale, use {@link com.android.internal.app.LocalePicker#updateLocales}
         * instead.
         *
         * @unknown 
         */
        public static final java.lang.String SYSTEM_LOCALES = "system_locales";

        /**
         * Name of an application package to be debugged.
         *
         * @deprecated Use {@link Global#DEBUG_APP} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String DEBUG_APP = android.provider.Settings.Global.DEBUG_APP;

        /**
         * If 1, when launching DEBUG_APP it will wait for the debugger before
         * starting user code.  If 0, it will run normally.
         *
         * @deprecated Use {@link Global#WAIT_FOR_DEBUGGER} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WAIT_FOR_DEBUGGER = android.provider.Settings.Global.WAIT_FOR_DEBUGGER;

        /**
         * Whether or not to dim the screen. 0=no  1=yes
         *
         * @deprecated This setting is no longer used.
         */
        @java.lang.Deprecated
        public static final java.lang.String DIM_SCREEN = "dim_screen";

        private static final android.provider.Settings.System.Validator DIM_SCREEN_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * The amount of time in milliseconds before the device goes to sleep or begins
         * to dream after a period of inactivity.  This value is also known as the
         * user activity timeout period since the screen isn't necessarily turned off
         * when it expires.
         */
        public static final java.lang.String SCREEN_OFF_TIMEOUT = "screen_off_timeout";

        private static final android.provider.Settings.System.Validator SCREEN_OFF_TIMEOUT_VALIDATOR = android.provider.Settings.System.sNonNegativeIntegerValidator;

        /**
         * The screen backlight brightness between 0 and 255.
         */
        public static final java.lang.String SCREEN_BRIGHTNESS = "screen_brightness";

        private static final android.provider.Settings.System.Validator SCREEN_BRIGHTNESS_VALIDATOR = new android.provider.Settings.System.InclusiveIntegerRangeValidator(0, 255);

        /**
         * Control whether to enable automatic brightness mode.
         */
        public static final java.lang.String SCREEN_BRIGHTNESS_MODE = "screen_brightness_mode";

        private static final android.provider.Settings.System.Validator SCREEN_BRIGHTNESS_MODE_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Adjustment to auto-brightness to make it generally more (>0.0 <1.0)
         * or less (<0.0 >-1.0) bright.
         *
         * @unknown 
         */
        public static final java.lang.String SCREEN_AUTO_BRIGHTNESS_ADJ = "screen_auto_brightness_adj";

        private static final android.provider.Settings.System.Validator SCREEN_AUTO_BRIGHTNESS_ADJ_VALIDATOR = new android.provider.Settings.System.InclusiveFloatRangeValidator(-1, 1);

        /**
         * SCREEN_BRIGHTNESS_MODE value for manual mode.
         */
        public static final int SCREEN_BRIGHTNESS_MODE_MANUAL = 0;

        /**
         * SCREEN_BRIGHTNESS_MODE value for automatic mode.
         */
        public static final int SCREEN_BRIGHTNESS_MODE_AUTOMATIC = 1;

        /**
         * Control whether the process CPU usage meter should be shown.
         *
         * @deprecated This functionality is no longer available as of
        {@link android.os.Build.VERSION_CODES#N_MR1}.
         */
        @java.lang.Deprecated
        public static final java.lang.String SHOW_PROCESSES = android.provider.Settings.Global.SHOW_PROCESSES;

        /**
         * If 1, the activity manager will aggressively finish activities and
         * processes as soon as they are no longer needed.  If 0, the normal
         * extended lifetime is used.
         *
         * @deprecated Use {@link Global#ALWAYS_FINISH_ACTIVITIES} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String ALWAYS_FINISH_ACTIVITIES = android.provider.Settings.Global.ALWAYS_FINISH_ACTIVITIES;

        /**
         * Determines which streams are affected by ringer mode changes. The
         * stream type's bit should be set to 1 if it should be muted when going
         * into an inaudible ringer mode.
         */
        public static final java.lang.String MODE_RINGER_STREAMS_AFFECTED = "mode_ringer_streams_affected";

        private static final android.provider.Settings.System.Validator MODE_RINGER_STREAMS_AFFECTED_VALIDATOR = android.provider.Settings.System.sNonNegativeIntegerValidator;

        /**
         * Determines which streams are affected by mute. The
         * stream type's bit should be set to 1 if it should be muted when a mute request
         * is received.
         */
        public static final java.lang.String MUTE_STREAMS_AFFECTED = "mute_streams_affected";

        private static final android.provider.Settings.System.Validator MUTE_STREAMS_AFFECTED_VALIDATOR = android.provider.Settings.System.sNonNegativeIntegerValidator;

        /**
         * Whether vibrate is on for different events. This is used internally,
         * changing this value will not change the vibrate. See AudioManager.
         */
        public static final java.lang.String VIBRATE_ON = "vibrate_on";

        private static final android.provider.Settings.System.Validator VIBRATE_ON_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * If 1, redirects the system vibrator to all currently attached input devices
         * that support vibration.  If there are no such input devices, then the system
         * vibrator is used instead.
         * If 0, does not register the system vibrator.
         *
         * This setting is mainly intended to provide a compatibility mechanism for
         * applications that only know about the system vibrator and do not use the
         * input device vibrator API.
         *
         * @unknown 
         */
        public static final java.lang.String VIBRATE_INPUT_DEVICES = "vibrate_input_devices";

        private static final android.provider.Settings.System.Validator VIBRATE_INPUT_DEVICES_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Ringer volume. This is used internally, changing this value will not
         * change the volume. See AudioManager.
         *
         * @unknown Not used by anything since API 2.
         */
        public static final java.lang.String VOLUME_RING = "volume_ring";

        /**
         * System/notifications volume. This is used internally, changing this
         * value will not change the volume. See AudioManager.
         *
         * @unknown Not used by anything since API 2.
         */
        public static final java.lang.String VOLUME_SYSTEM = "volume_system";

        /**
         * Voice call volume. This is used internally, changing this value will
         * not change the volume. See AudioManager.
         *
         * @unknown Not used by anything since API 2.
         */
        public static final java.lang.String VOLUME_VOICE = "volume_voice";

        /**
         * Music/media/gaming volume. This is used internally, changing this
         * value will not change the volume. See AudioManager.
         *
         * @unknown Not used by anything since API 2.
         */
        public static final java.lang.String VOLUME_MUSIC = "volume_music";

        /**
         * Alarm volume. This is used internally, changing this
         * value will not change the volume. See AudioManager.
         *
         * @unknown Not used by anything since API 2.
         */
        public static final java.lang.String VOLUME_ALARM = "volume_alarm";

        /**
         * Notification volume. This is used internally, changing this
         * value will not change the volume. See AudioManager.
         *
         * @unknown Not used by anything since API 2.
         */
        public static final java.lang.String VOLUME_NOTIFICATION = "volume_notification";

        /**
         * Bluetooth Headset volume. This is used internally, changing this value will
         * not change the volume. See AudioManager.
         *
         * @unknown Not used by anything since API 2.
         */
        public static final java.lang.String VOLUME_BLUETOOTH_SCO = "volume_bluetooth_sco";

        /**
         * Master volume (float in the range 0.0f to 1.0f).
         *
         * @unknown 
         */
        public static final java.lang.String VOLUME_MASTER = "volume_master";

        /**
         * Master mono (int 1 = mono, 0 = normal).
         *
         * @unknown 
         */
        public static final java.lang.String MASTER_MONO = "master_mono";

        private static final android.provider.Settings.System.Validator MASTER_MONO_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Whether the notifications should use the ring volume (value of 1) or
         * a separate notification volume (value of 0). In most cases, users
         * will have this enabled so the notification and ringer volumes will be
         * the same. However, power users can disable this and use the separate
         * notification volume control.
         * <p>
         * Note: This is a one-off setting that will be removed in the future
         * when there is profile support. For this reason, it is kept hidden
         * from the public APIs.
         *
         * @unknown 
         * @deprecated 
         */
        @java.lang.Deprecated
        public static final java.lang.String NOTIFICATIONS_USE_RING_VOLUME = "notifications_use_ring_volume";

        private static final android.provider.Settings.System.Validator NOTIFICATIONS_USE_RING_VOLUME_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Whether silent mode should allow vibration feedback. This is used
         * internally in AudioService and the Sound settings activity to
         * coordinate decoupling of vibrate and silent modes. This setting
         * will likely be removed in a future release with support for
         * audio/vibe feedback profiles.
         *
         * Not used anymore. On devices with vibrator, the user explicitly selects
         * silent or vibrate mode.
         * Kept for use by legacy database upgrade code in DatabaseHelper.
         *
         * @unknown 
         */
        public static final java.lang.String VIBRATE_IN_SILENT = "vibrate_in_silent";

        private static final android.provider.Settings.System.Validator VIBRATE_IN_SILENT_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * The mapping of stream type (integer) to its setting.
         *
         * @unknown Not used by anything since API 2.
         */
        public static final java.lang.String[] VOLUME_SETTINGS = new java.lang.String[]{ android.provider.Settings.System.VOLUME_VOICE, android.provider.Settings.System.VOLUME_SYSTEM, android.provider.Settings.System.VOLUME_RING, android.provider.Settings.System.VOLUME_MUSIC, android.provider.Settings.System.VOLUME_ALARM, android.provider.Settings.System.VOLUME_NOTIFICATION, android.provider.Settings.System.VOLUME_BLUETOOTH_SCO };

        /**
         * Appended to various volume related settings to record the previous
         * values before they the settings were affected by a silent/vibrate
         * ringer mode change.
         *
         * @unknown Not used by anything since API 2.
         */
        public static final java.lang.String APPEND_FOR_LAST_AUDIBLE = "_last_audible";

        /**
         * Persistent store for the system-wide default ringtone URI.
         * <p>
         * If you need to play the default ringtone at any given time, it is recommended
         * you give {@link #DEFAULT_RINGTONE_URI} to the media player.  It will resolve
         * to the set default ringtone at the time of playing.
         *
         * @see #DEFAULT_RINGTONE_URI
         */
        public static final java.lang.String RINGTONE = "ringtone";

        private static final android.provider.Settings.System.Validator RINGTONE_VALIDATOR = android.provider.Settings.System.sUriValidator;

        /**
         * A {@link Uri} that will point to the current default ringtone at any
         * given time.
         * <p>
         * If the current default ringtone is in the DRM provider and the caller
         * does not have permission, the exception will be a
         * FileNotFoundException.
         */
        public static final android.net.Uri DEFAULT_RINGTONE_URI = android.provider.Settings.System.getUriFor(android.provider.Settings.System.RINGTONE);

        /**
         * {@hide }
         */
        public static final java.lang.String RINGTONE_CACHE = "ringtone_cache";

        /**
         * {@hide }
         */
        public static final android.net.Uri RINGTONE_CACHE_URI = android.provider.Settings.System.getUriFor(android.provider.Settings.System.RINGTONE_CACHE);

        /**
         * Persistent store for the system-wide default notification sound.
         *
         * @see #RINGTONE
         * @see #DEFAULT_NOTIFICATION_URI
         */
        public static final java.lang.String NOTIFICATION_SOUND = "notification_sound";

        private static final android.provider.Settings.System.Validator NOTIFICATION_SOUND_VALIDATOR = android.provider.Settings.System.sUriValidator;

        /**
         * A {@link Uri} that will point to the current default notification
         * sound at any given time.
         *
         * @see #DEFAULT_RINGTONE_URI
         */
        public static final android.net.Uri DEFAULT_NOTIFICATION_URI = android.provider.Settings.System.getUriFor(android.provider.Settings.System.NOTIFICATION_SOUND);

        /**
         * {@hide }
         */
        public static final java.lang.String NOTIFICATION_SOUND_CACHE = "notification_sound_cache";

        /**
         * {@hide }
         */
        public static final android.net.Uri NOTIFICATION_SOUND_CACHE_URI = android.provider.Settings.System.getUriFor(android.provider.Settings.System.NOTIFICATION_SOUND_CACHE);

        /**
         * Persistent store for the system-wide default alarm alert.
         *
         * @see #RINGTONE
         * @see #DEFAULT_ALARM_ALERT_URI
         */
        public static final java.lang.String ALARM_ALERT = "alarm_alert";

        private static final android.provider.Settings.System.Validator ALARM_ALERT_VALIDATOR = android.provider.Settings.System.sUriValidator;

        /**
         * A {@link Uri} that will point to the current default alarm alert at
         * any given time.
         *
         * @see #DEFAULT_ALARM_ALERT_URI
         */
        public static final android.net.Uri DEFAULT_ALARM_ALERT_URI = android.provider.Settings.System.getUriFor(android.provider.Settings.System.ALARM_ALERT);

        /**
         * {@hide }
         */
        public static final java.lang.String ALARM_ALERT_CACHE = "alarm_alert_cache";

        /**
         * {@hide }
         */
        public static final android.net.Uri ALARM_ALERT_CACHE_URI = android.provider.Settings.System.getUriFor(android.provider.Settings.System.ALARM_ALERT_CACHE);

        /**
         * Persistent store for the system default media button event receiver.
         *
         * @unknown 
         */
        public static final java.lang.String MEDIA_BUTTON_RECEIVER = "media_button_receiver";

        private static final android.provider.Settings.System.Validator MEDIA_BUTTON_RECEIVER_VALIDATOR = new android.provider.Settings.System.Validator() {
            @java.lang.Override
            public boolean validate(java.lang.String value) {
                try {
                    android.content.ComponentName.unflattenFromString(value);
                    return true;
                } catch (java.lang.NullPointerException e) {
                    return false;
                }
            }
        };

        /**
         * Setting to enable Auto Replace (AutoText) in text editors. 1 = On, 0 = Off
         */
        public static final java.lang.String TEXT_AUTO_REPLACE = "auto_replace";

        private static final android.provider.Settings.System.Validator TEXT_AUTO_REPLACE_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Setting to enable Auto Caps in text editors. 1 = On, 0 = Off
         */
        public static final java.lang.String TEXT_AUTO_CAPS = "auto_caps";

        private static final android.provider.Settings.System.Validator TEXT_AUTO_CAPS_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Setting to enable Auto Punctuate in text editors. 1 = On, 0 = Off. This
         * feature converts two spaces to a "." and space.
         */
        public static final java.lang.String TEXT_AUTO_PUNCTUATE = "auto_punctuate";

        private static final android.provider.Settings.System.Validator TEXT_AUTO_PUNCTUATE_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Setting to showing password characters in text editors. 1 = On, 0 = Off
         */
        public static final java.lang.String TEXT_SHOW_PASSWORD = "show_password";

        private static final android.provider.Settings.System.Validator TEXT_SHOW_PASSWORD_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        public static final java.lang.String SHOW_GTALK_SERVICE_STATUS = "SHOW_GTALK_SERVICE_STATUS";

        private static final android.provider.Settings.System.Validator SHOW_GTALK_SERVICE_STATUS_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Name of activity to use for wallpaper on the home screen.
         *
         * @deprecated Use {@link WallpaperManager} instead.
         */
        @java.lang.Deprecated
        public static final java.lang.String WALLPAPER_ACTIVITY = "wallpaper_activity";

        private static final android.provider.Settings.System.Validator WALLPAPER_ACTIVITY_VALIDATOR = new android.provider.Settings.System.Validator() {
            private static final int MAX_LENGTH = 1000;

            @java.lang.Override
            public boolean validate(java.lang.String value) {
                if ((value != null) && (value.length() > MAX_LENGTH)) {
                    return false;
                }
                return android.content.ComponentName.unflattenFromString(value) != null;
            }
        };

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#AUTO_TIME}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String AUTO_TIME = android.provider.Settings.Global.AUTO_TIME;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#AUTO_TIME_ZONE}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String AUTO_TIME_ZONE = android.provider.Settings.Global.AUTO_TIME_ZONE;

        /**
         * Display times as 12 or 24 hours
         *   12
         *   24
         */
        public static final java.lang.String TIME_12_24 = "time_12_24";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator TIME_12_24_VALIDATOR = new android.provider.Settings.System.DiscreteValueValidator(new java.lang.String[]{ "12", "24" });

        /**
         * Date format string
         *   mm/dd/yyyy
         *   dd/mm/yyyy
         *   yyyy/mm/dd
         */
        public static final java.lang.String DATE_FORMAT = "date_format";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator DATE_FORMAT_VALIDATOR = new android.provider.Settings.System.Validator() {
            @java.lang.Override
            public boolean validate(java.lang.String value) {
                try {
                    new java.text.SimpleDateFormat(value);
                    return true;
                } catch (java.lang.IllegalArgumentException e) {
                    return false;
                }
            }
        };

        /**
         * Whether the setup wizard has been run before (on first boot), or if
         * it still needs to be run.
         *
         * nonzero = it has been run in the past
         * 0 = it has not been run in the past
         */
        public static final java.lang.String SETUP_WIZARD_HAS_RUN = "setup_wizard_has_run";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator SETUP_WIZARD_HAS_RUN_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Scaling factor for normal window animations. Setting to 0 will disable window
         * animations.
         *
         * @deprecated Use {@link Global#WINDOW_ANIMATION_SCALE} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WINDOW_ANIMATION_SCALE = android.provider.Settings.Global.WINDOW_ANIMATION_SCALE;

        /**
         * Scaling factor for activity transition animations. Setting to 0 will disable window
         * animations.
         *
         * @deprecated Use {@link Global#TRANSITION_ANIMATION_SCALE} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String TRANSITION_ANIMATION_SCALE = android.provider.Settings.Global.TRANSITION_ANIMATION_SCALE;

        /**
         * Scaling factor for Animator-based animations. This affects both the start delay and
         * duration of all such animations. Setting to 0 will cause animations to end immediately.
         * The default value is 1.
         *
         * @deprecated Use {@link Global#ANIMATOR_DURATION_SCALE} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String ANIMATOR_DURATION_SCALE = android.provider.Settings.Global.ANIMATOR_DURATION_SCALE;

        /**
         * Control whether the accelerometer will be used to change screen
         * orientation.  If 0, it will not be used unless explicitly requested
         * by the application; if 1, it will be used by default unless explicitly
         * disabled by the application.
         */
        public static final java.lang.String ACCELEROMETER_ROTATION = "accelerometer_rotation";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator ACCELEROMETER_ROTATION_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Default screen rotation when no other policy applies.
         * When {@link #ACCELEROMETER_ROTATION} is zero and no on-screen Activity expresses a
         * preference, this rotation value will be used. Must be one of the
         * {@link android.view.Surface#ROTATION_0 Surface rotation constants}.
         *
         * @see android.view.Display#getRotation
         */
        public static final java.lang.String USER_ROTATION = "user_rotation";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator USER_ROTATION_VALIDATOR = new android.provider.Settings.System.InclusiveIntegerRangeValidator(0, 3);

        /**
         * Control whether the rotation lock toggle in the System UI should be hidden.
         * Typically this is done for accessibility purposes to make it harder for
         * the user to accidentally toggle the rotation lock while the display rotation
         * has been locked for accessibility.
         *
         * If 0, then rotation lock toggle is not hidden for accessibility (although it may be
         * unavailable for other reasons).  If 1, then the rotation lock toggle is hidden.
         *
         * @unknown 
         */
        public static final java.lang.String HIDE_ROTATION_LOCK_TOGGLE_FOR_ACCESSIBILITY = "hide_rotation_lock_toggle_for_accessibility";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator HIDE_ROTATION_LOCK_TOGGLE_FOR_ACCESSIBILITY_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Whether the phone vibrates when it is ringing due to an incoming call. This will
         * be used by Phone and Setting apps; it shouldn't affect other apps.
         * The value is boolean (1 or 0).
         *
         * Note: this is not same as "vibrate on ring", which had been available until ICS.
         * It was about AudioManager's setting and thus affected all the applications which
         * relied on the setting, while this is purely about the vibration setting for incoming
         * calls.
         */
        public static final java.lang.String VIBRATE_WHEN_RINGING = "vibrate_when_ringing";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator VIBRATE_WHEN_RINGING_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Whether the audible DTMF tones are played by the dialer when dialing. The value is
         * boolean (1 or 0).
         */
        public static final java.lang.String DTMF_TONE_WHEN_DIALING = "dtmf_tone";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator DTMF_TONE_WHEN_DIALING_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * CDMA only settings
         * DTMF tone type played by the dialer when dialing.
         *                 0 = Normal
         *                 1 = Long
         */
        public static final java.lang.String DTMF_TONE_TYPE_WHEN_DIALING = "dtmf_tone_type";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator DTMF_TONE_TYPE_WHEN_DIALING_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Whether the hearing aid is enabled. The value is
         * boolean (1 or 0).
         *
         * @unknown 
         */
        public static final java.lang.String HEARING_AID = "hearing_aid";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator HEARING_AID_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * CDMA only settings
         * TTY Mode
         * 0 = OFF
         * 1 = FULL
         * 2 = VCO
         * 3 = HCO
         *
         * @unknown 
         */
        public static final java.lang.String TTY_MODE = "tty_mode";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator TTY_MODE_VALIDATOR = new android.provider.Settings.System.InclusiveIntegerRangeValidator(0, 3);

        /**
         * Whether the sounds effects (key clicks, lid open ...) are enabled. The value is
         * boolean (1 or 0).
         */
        public static final java.lang.String SOUND_EFFECTS_ENABLED = "sound_effects_enabled";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator SOUND_EFFECTS_ENABLED_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Whether the haptic feedback (long presses, ...) are enabled. The value is
         * boolean (1 or 0).
         */
        public static final java.lang.String HAPTIC_FEEDBACK_ENABLED = "haptic_feedback_enabled";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator HAPTIC_FEEDBACK_ENABLED_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         *
         *
         * @deprecated Each application that shows web suggestions should have its own
        setting for this.
         */
        @java.lang.Deprecated
        public static final java.lang.String SHOW_WEB_SUGGESTIONS = "show_web_suggestions";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator SHOW_WEB_SUGGESTIONS_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Whether the notification LED should repeatedly flash when a notification is
         * pending. The value is boolean (1 or 0).
         *
         * @unknown 
         */
        public static final java.lang.String NOTIFICATION_LIGHT_PULSE = "notification_light_pulse";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator NOTIFICATION_LIGHT_PULSE_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Show pointer location on screen?
         * 0 = no
         * 1 = yes
         *
         * @unknown 
         */
        public static final java.lang.String POINTER_LOCATION = "pointer_location";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator POINTER_LOCATION_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Show touch positions on screen?
         * 0 = no
         * 1 = yes
         *
         * @unknown 
         */
        public static final java.lang.String SHOW_TOUCHES = "show_touches";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator SHOW_TOUCHES_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Log raw orientation data from
         * {@link com.android.server.policy.WindowOrientationListener} for use with the
         * orientationplot.py tool.
         * 0 = no
         * 1 = yes
         *
         * @unknown 
         */
        public static final java.lang.String WINDOW_ORIENTATION_LISTENER_LOG = "window_orientation_listener_log";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator WINDOW_ORIENTATION_LISTENER_LOG_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#POWER_SOUNDS_ENABLED}
        instead
         * @unknown 
         */
        @java.lang.Deprecated
        public static final java.lang.String POWER_SOUNDS_ENABLED = android.provider.Settings.Global.POWER_SOUNDS_ENABLED;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#DOCK_SOUNDS_ENABLED}
        instead
         * @unknown 
         */
        @java.lang.Deprecated
        public static final java.lang.String DOCK_SOUNDS_ENABLED = android.provider.Settings.Global.DOCK_SOUNDS_ENABLED;

        /**
         * Whether to play sounds when the keyguard is shown and dismissed.
         *
         * @unknown 
         */
        public static final java.lang.String LOCKSCREEN_SOUNDS_ENABLED = "lockscreen_sounds_enabled";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator LOCKSCREEN_SOUNDS_ENABLED_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Whether the lockscreen should be completely disabled.
         *
         * @unknown 
         */
        public static final java.lang.String LOCKSCREEN_DISABLED = "lockscreen.disabled";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator LOCKSCREEN_DISABLED_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#LOW_BATTERY_SOUND}
        instead
         * @unknown 
         */
        @java.lang.Deprecated
        public static final java.lang.String LOW_BATTERY_SOUND = android.provider.Settings.Global.LOW_BATTERY_SOUND;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#DESK_DOCK_SOUND}
        instead
         * @unknown 
         */
        @java.lang.Deprecated
        public static final java.lang.String DESK_DOCK_SOUND = android.provider.Settings.Global.DESK_DOCK_SOUND;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#DESK_UNDOCK_SOUND}
        instead
         * @unknown 
         */
        @java.lang.Deprecated
        public static final java.lang.String DESK_UNDOCK_SOUND = android.provider.Settings.Global.DESK_UNDOCK_SOUND;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#CAR_DOCK_SOUND}
        instead
         * @unknown 
         */
        @java.lang.Deprecated
        public static final java.lang.String CAR_DOCK_SOUND = android.provider.Settings.Global.CAR_DOCK_SOUND;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#CAR_UNDOCK_SOUND}
        instead
         * @unknown 
         */
        @java.lang.Deprecated
        public static final java.lang.String CAR_UNDOCK_SOUND = android.provider.Settings.Global.CAR_UNDOCK_SOUND;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#LOCK_SOUND}
        instead
         * @unknown 
         */
        @java.lang.Deprecated
        public static final java.lang.String LOCK_SOUND = android.provider.Settings.Global.LOCK_SOUND;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#UNLOCK_SOUND}
        instead
         * @unknown 
         */
        @java.lang.Deprecated
        public static final java.lang.String UNLOCK_SOUND = android.provider.Settings.Global.UNLOCK_SOUND;

        /**
         * Receive incoming SIP calls?
         * 0 = no
         * 1 = yes
         *
         * @unknown 
         */
        public static final java.lang.String SIP_RECEIVE_CALLS = "sip_receive_calls";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator SIP_RECEIVE_CALLS_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Call Preference String.
         * "SIP_ALWAYS" : Always use SIP with network access
         * "SIP_ADDRESS_ONLY" : Only if destination is a SIP address
         *
         * @unknown 
         */
        public static final java.lang.String SIP_CALL_OPTIONS = "sip_call_options";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator SIP_CALL_OPTIONS_VALIDATOR = new android.provider.Settings.System.DiscreteValueValidator(new java.lang.String[]{ "SIP_ALWAYS", "SIP_ADDRESS_ONLY" });

        /**
         * One of the sip call options: Always use SIP with network access.
         *
         * @unknown 
         */
        public static final java.lang.String SIP_ALWAYS = "SIP_ALWAYS";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator SIP_ALWAYS_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * One of the sip call options: Only if destination is a SIP address.
         *
         * @unknown 
         */
        public static final java.lang.String SIP_ADDRESS_ONLY = "SIP_ADDRESS_ONLY";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator SIP_ADDRESS_ONLY_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         *
         *
         * @deprecated Use SIP_ALWAYS or SIP_ADDRESS_ONLY instead.  Formerly used to indicate that
        the user should be prompted each time a call is made whether it should be placed using
        SIP.  The {@link com.android.providers.settings.DatabaseHelper} replaces this with
        SIP_ADDRESS_ONLY.
         * @unknown 
         */
        @java.lang.Deprecated
        public static final java.lang.String SIP_ASK_ME_EACH_TIME = "SIP_ASK_ME_EACH_TIME";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator SIP_ASK_ME_EACH_TIME_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * Pointer speed setting.
         * This is an integer value in a range between -7 and +7, so there are 15 possible values.
         *   -7 = slowest
         *    0 = default speed
         *   +7 = fastest
         *
         * @unknown 
         */
        public static final java.lang.String POINTER_SPEED = "pointer_speed";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator POINTER_SPEED_VALIDATOR = new android.provider.Settings.System.InclusiveFloatRangeValidator(-7, 7);

        /**
         * Whether lock-to-app will be triggered by long-press on recents.
         *
         * @unknown 
         */
        public static final java.lang.String LOCK_TO_APP_ENABLED = "lock_to_app_enabled";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator LOCK_TO_APP_ENABLED_VALIDATOR = android.provider.Settings.System.sBooleanValidator;

        /**
         * I am the lolrus.
         * <p>
         * Nonzero values indicate that the user has a bukkit.
         * Backward-compatible with <code>PrefGetPreference(prefAllowEasterEggs)</code>.
         *
         * @unknown 
         */
        public static final java.lang.String EGG_MODE = "egg_mode";

        /**
         *
         *
         * @unknown 
         */
        public static final android.provider.Settings.System.Validator EGG_MODE_VALIDATOR = new android.provider.Settings.System.Validator() {
            @java.lang.Override
            public boolean validate(java.lang.String value) {
                try {
                    return java.lang.Long.parseLong(value) >= 0;
                } catch (java.lang.NumberFormatException e) {
                    return false;
                }
            }
        };

        /**
         * IMPORTANT: If you add a new public settings you also have to add it to
         * PUBLIC_SETTINGS below. If the new setting is hidden you have to add
         * it to PRIVATE_SETTINGS below. Also add a validator that can validate
         * the setting value. See an example above.
         */
        /**
         * Settings to backup. This is here so that it's in the same place as the settings
         * keys and easy to update.
         *
         * NOTE: Settings are backed up and restored in the order they appear
         *       in this array. If you have one setting depending on another,
         *       make sure that they are ordered appropriately.
         *
         * @unknown 
         */
        public static final java.lang.String[] SETTINGS_TO_BACKUP = new java.lang.String[]{ android.provider.Settings.System.STAY_ON_WHILE_PLUGGED_IN// moved to global
        , android.provider.Settings.System.WIFI_USE_STATIC_IP, android.provider.Settings.System.WIFI_STATIC_IP, android.provider.Settings.System.WIFI_STATIC_GATEWAY, android.provider.Settings.System.WIFI_STATIC_NETMASK, android.provider.Settings.System.WIFI_STATIC_DNS1, android.provider.Settings.System.WIFI_STATIC_DNS2, android.provider.Settings.System.BLUETOOTH_DISCOVERABILITY, android.provider.Settings.System.BLUETOOTH_DISCOVERABILITY_TIMEOUT, android.provider.Settings.System.FONT_SCALE, android.provider.Settings.System.DIM_SCREEN, android.provider.Settings.System.SCREEN_OFF_TIMEOUT, android.provider.Settings.System.SCREEN_BRIGHTNESS, android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE, android.provider.Settings.System.SCREEN_AUTO_BRIGHTNESS_ADJ, android.provider.Settings.System.VIBRATE_INPUT_DEVICES, android.provider.Settings.System.MODE_RINGER_STREAMS_AFFECTED, android.provider.Settings.System.TEXT_AUTO_REPLACE, android.provider.Settings.System.TEXT_AUTO_CAPS, android.provider.Settings.System.TEXT_AUTO_PUNCTUATE, android.provider.Settings.System.TEXT_SHOW_PASSWORD, android.provider.Settings.System.AUTO_TIME, // moved to global
        android.provider.Settings.System.AUTO_TIME_ZONE, // moved to global
        android.provider.Settings.System.TIME_12_24, android.provider.Settings.System.DATE_FORMAT, android.provider.Settings.System.DTMF_TONE_WHEN_DIALING, android.provider.Settings.System.DTMF_TONE_TYPE_WHEN_DIALING, android.provider.Settings.System.HEARING_AID, android.provider.Settings.System.TTY_MODE, android.provider.Settings.System.MASTER_MONO, android.provider.Settings.System.SOUND_EFFECTS_ENABLED, android.provider.Settings.System.HAPTIC_FEEDBACK_ENABLED, android.provider.Settings.System.POWER_SOUNDS_ENABLED// moved to global
        , android.provider.Settings.System.DOCK_SOUNDS_ENABLED// moved to global
        , android.provider.Settings.System.LOCKSCREEN_SOUNDS_ENABLED, android.provider.Settings.System.SHOW_WEB_SUGGESTIONS, android.provider.Settings.System.SIP_CALL_OPTIONS, android.provider.Settings.System.SIP_RECEIVE_CALLS, android.provider.Settings.System.POINTER_SPEED, android.provider.Settings.System.VIBRATE_WHEN_RINGING, android.provider.Settings.System.RINGTONE, android.provider.Settings.System.LOCK_TO_APP_ENABLED, android.provider.Settings.System.NOTIFICATION_SOUND, android.provider.Settings.System.ACCELEROMETER_ROTATION };

        /**
         * These are all public system settings
         *
         * @unknown 
         */
        public static final java.util.Set<java.lang.String> PUBLIC_SETTINGS = new android.util.ArraySet<>();

        static {
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.END_BUTTON_BEHAVIOR);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.WIFI_USE_STATIC_IP);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.WIFI_STATIC_IP);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.WIFI_STATIC_GATEWAY);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.WIFI_STATIC_NETMASK);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.WIFI_STATIC_DNS1);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.WIFI_STATIC_DNS2);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.BLUETOOTH_DISCOVERABILITY);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.BLUETOOTH_DISCOVERABILITY_TIMEOUT);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.NEXT_ALARM_FORMATTED);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.FONT_SCALE);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.DIM_SCREEN);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.SCREEN_OFF_TIMEOUT);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.SCREEN_BRIGHTNESS);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.MODE_RINGER_STREAMS_AFFECTED);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.MUTE_STREAMS_AFFECTED);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.VIBRATE_ON);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.VOLUME_RING);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.VOLUME_SYSTEM);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.VOLUME_VOICE);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.VOLUME_MUSIC);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.VOLUME_ALARM);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.VOLUME_NOTIFICATION);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.VOLUME_BLUETOOTH_SCO);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.RINGTONE);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.NOTIFICATION_SOUND);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.ALARM_ALERT);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.TEXT_AUTO_REPLACE);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.TEXT_AUTO_CAPS);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.TEXT_AUTO_PUNCTUATE);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.TEXT_SHOW_PASSWORD);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.SHOW_GTALK_SERVICE_STATUS);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.WALLPAPER_ACTIVITY);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.TIME_12_24);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.DATE_FORMAT);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.SETUP_WIZARD_HAS_RUN);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.ACCELEROMETER_ROTATION);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.USER_ROTATION);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.DTMF_TONE_WHEN_DIALING);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.SOUND_EFFECTS_ENABLED);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.HAPTIC_FEEDBACK_ENABLED);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.SHOW_WEB_SUGGESTIONS);
            android.provider.Settings.System.PUBLIC_SETTINGS.add(android.provider.Settings.System.VIBRATE_WHEN_RINGING);
        }

        /**
         * These are all hidden system settings.
         *
         * @unknown 
         */
        public static final java.util.Set<java.lang.String> PRIVATE_SETTINGS = new android.util.ArraySet<>();

        static {
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.WIFI_USE_STATIC_IP);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.END_BUTTON_BEHAVIOR);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.ADVANCED_SETTINGS);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.SCREEN_AUTO_BRIGHTNESS_ADJ);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.VIBRATE_INPUT_DEVICES);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.VOLUME_MASTER);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.MASTER_MONO);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.NOTIFICATIONS_USE_RING_VOLUME);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.VIBRATE_IN_SILENT);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.MEDIA_BUTTON_RECEIVER);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.HIDE_ROTATION_LOCK_TOGGLE_FOR_ACCESSIBILITY);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.DTMF_TONE_TYPE_WHEN_DIALING);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.HEARING_AID);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.TTY_MODE);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.NOTIFICATION_LIGHT_PULSE);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.POINTER_LOCATION);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.SHOW_TOUCHES);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.WINDOW_ORIENTATION_LISTENER_LOG);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.POWER_SOUNDS_ENABLED);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.DOCK_SOUNDS_ENABLED);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.LOCKSCREEN_SOUNDS_ENABLED);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.LOCKSCREEN_DISABLED);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.LOW_BATTERY_SOUND);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.DESK_DOCK_SOUND);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.DESK_UNDOCK_SOUND);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.CAR_DOCK_SOUND);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.CAR_UNDOCK_SOUND);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.LOCK_SOUND);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.UNLOCK_SOUND);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.SIP_RECEIVE_CALLS);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.SIP_CALL_OPTIONS);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.SIP_ALWAYS);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.SIP_ADDRESS_ONLY);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.SIP_ASK_ME_EACH_TIME);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.POINTER_SPEED);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.LOCK_TO_APP_ENABLED);
            android.provider.Settings.System.PRIVATE_SETTINGS.add(android.provider.Settings.System.EGG_MODE);
        }

        /**
         * These are all public system settings
         *
         * @unknown 
         */
        public static final java.util.Map<java.lang.String, android.provider.Settings.System.Validator> VALIDATORS = new android.util.ArrayMap<>();

        static {
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.END_BUTTON_BEHAVIOR, android.provider.Settings.System.END_BUTTON_BEHAVIOR_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.WIFI_USE_STATIC_IP, android.provider.Settings.System.WIFI_USE_STATIC_IP_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.BLUETOOTH_DISCOVERABILITY, android.provider.Settings.System.BLUETOOTH_DISCOVERABILITY_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.BLUETOOTH_DISCOVERABILITY_TIMEOUT, android.provider.Settings.System.BLUETOOTH_DISCOVERABILITY_TIMEOUT_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.NEXT_ALARM_FORMATTED, android.provider.Settings.System.NEXT_ALARM_FORMATTED_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.FONT_SCALE, android.provider.Settings.System.FONT_SCALE_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.DIM_SCREEN, android.provider.Settings.System.DIM_SCREEN_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.SCREEN_OFF_TIMEOUT, android.provider.Settings.System.SCREEN_OFF_TIMEOUT_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.SCREEN_BRIGHTNESS, android.provider.Settings.System.SCREEN_BRIGHTNESS_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE, android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.MODE_RINGER_STREAMS_AFFECTED, android.provider.Settings.System.MODE_RINGER_STREAMS_AFFECTED_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.MUTE_STREAMS_AFFECTED, android.provider.Settings.System.MUTE_STREAMS_AFFECTED_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.VIBRATE_ON, android.provider.Settings.System.VIBRATE_ON_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.RINGTONE, android.provider.Settings.System.RINGTONE_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.NOTIFICATION_SOUND, android.provider.Settings.System.NOTIFICATION_SOUND_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.ALARM_ALERT, android.provider.Settings.System.ALARM_ALERT_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.TEXT_AUTO_REPLACE, android.provider.Settings.System.TEXT_AUTO_REPLACE_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.TEXT_AUTO_CAPS, android.provider.Settings.System.TEXT_AUTO_CAPS_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.TEXT_AUTO_PUNCTUATE, android.provider.Settings.System.TEXT_AUTO_PUNCTUATE_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.TEXT_SHOW_PASSWORD, android.provider.Settings.System.TEXT_SHOW_PASSWORD_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.SHOW_GTALK_SERVICE_STATUS, android.provider.Settings.System.SHOW_GTALK_SERVICE_STATUS_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.WALLPAPER_ACTIVITY, android.provider.Settings.System.WALLPAPER_ACTIVITY_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.TIME_12_24, android.provider.Settings.System.TIME_12_24_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.DATE_FORMAT, android.provider.Settings.System.DATE_FORMAT_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.SETUP_WIZARD_HAS_RUN, android.provider.Settings.System.SETUP_WIZARD_HAS_RUN_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.ACCELEROMETER_ROTATION, android.provider.Settings.System.ACCELEROMETER_ROTATION_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.USER_ROTATION, android.provider.Settings.System.USER_ROTATION_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.DTMF_TONE_WHEN_DIALING, android.provider.Settings.System.DTMF_TONE_WHEN_DIALING_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.SOUND_EFFECTS_ENABLED, android.provider.Settings.System.SOUND_EFFECTS_ENABLED_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.HAPTIC_FEEDBACK_ENABLED, android.provider.Settings.System.HAPTIC_FEEDBACK_ENABLED_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.SHOW_WEB_SUGGESTIONS, android.provider.Settings.System.SHOW_WEB_SUGGESTIONS_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.WIFI_USE_STATIC_IP, android.provider.Settings.System.WIFI_USE_STATIC_IP_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.END_BUTTON_BEHAVIOR, android.provider.Settings.System.END_BUTTON_BEHAVIOR_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.ADVANCED_SETTINGS, android.provider.Settings.System.ADVANCED_SETTINGS_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.SCREEN_AUTO_BRIGHTNESS_ADJ, android.provider.Settings.System.SCREEN_AUTO_BRIGHTNESS_ADJ_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.VIBRATE_INPUT_DEVICES, android.provider.Settings.System.VIBRATE_INPUT_DEVICES_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.MASTER_MONO, android.provider.Settings.System.MASTER_MONO_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.NOTIFICATIONS_USE_RING_VOLUME, android.provider.Settings.System.NOTIFICATIONS_USE_RING_VOLUME_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.VIBRATE_IN_SILENT, android.provider.Settings.System.VIBRATE_IN_SILENT_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.MEDIA_BUTTON_RECEIVER, android.provider.Settings.System.MEDIA_BUTTON_RECEIVER_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.HIDE_ROTATION_LOCK_TOGGLE_FOR_ACCESSIBILITY, android.provider.Settings.System.HIDE_ROTATION_LOCK_TOGGLE_FOR_ACCESSIBILITY_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.VIBRATE_WHEN_RINGING, android.provider.Settings.System.VIBRATE_WHEN_RINGING_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.DTMF_TONE_TYPE_WHEN_DIALING, android.provider.Settings.System.DTMF_TONE_TYPE_WHEN_DIALING_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.HEARING_AID, android.provider.Settings.System.HEARING_AID_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.TTY_MODE, android.provider.Settings.System.TTY_MODE_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.NOTIFICATION_LIGHT_PULSE, android.provider.Settings.System.NOTIFICATION_LIGHT_PULSE_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.POINTER_LOCATION, android.provider.Settings.System.POINTER_LOCATION_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.SHOW_TOUCHES, android.provider.Settings.System.SHOW_TOUCHES_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.WINDOW_ORIENTATION_LISTENER_LOG, android.provider.Settings.System.WINDOW_ORIENTATION_LISTENER_LOG_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.LOCKSCREEN_SOUNDS_ENABLED, android.provider.Settings.System.LOCKSCREEN_SOUNDS_ENABLED_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.LOCKSCREEN_DISABLED, android.provider.Settings.System.LOCKSCREEN_DISABLED_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.SIP_RECEIVE_CALLS, android.provider.Settings.System.SIP_RECEIVE_CALLS_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.SIP_CALL_OPTIONS, android.provider.Settings.System.SIP_CALL_OPTIONS_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.SIP_ALWAYS, android.provider.Settings.System.SIP_ALWAYS_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.SIP_ADDRESS_ONLY, android.provider.Settings.System.SIP_ADDRESS_ONLY_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.SIP_ASK_ME_EACH_TIME, android.provider.Settings.System.SIP_ASK_ME_EACH_TIME_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.POINTER_SPEED, android.provider.Settings.System.POINTER_SPEED_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.LOCK_TO_APP_ENABLED, android.provider.Settings.System.LOCK_TO_APP_ENABLED_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.EGG_MODE, android.provider.Settings.System.EGG_MODE_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.WIFI_STATIC_IP, android.provider.Settings.System.WIFI_STATIC_IP_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.WIFI_STATIC_GATEWAY, android.provider.Settings.System.WIFI_STATIC_GATEWAY_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.WIFI_STATIC_NETMASK, android.provider.Settings.System.WIFI_STATIC_NETMASK_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.WIFI_STATIC_DNS1, android.provider.Settings.System.WIFI_STATIC_DNS1_VALIDATOR);
            android.provider.Settings.System.VALIDATORS.put(android.provider.Settings.System.WIFI_STATIC_DNS2, android.provider.Settings.System.WIFI_STATIC_DNS2_VALIDATOR);
        }

        /**
         * These entries are considered common between the personal and the managed profile,
         * since the managed profile doesn't get to change them.
         */
        private static final java.util.Set<java.lang.String> CLONE_TO_MANAGED_PROFILE = new android.util.ArraySet<>();

        static {
            android.provider.Settings.System.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.System.DATE_FORMAT);
            android.provider.Settings.System.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.System.HAPTIC_FEEDBACK_ENABLED);
            android.provider.Settings.System.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.System.SOUND_EFFECTS_ENABLED);
            android.provider.Settings.System.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.System.TEXT_SHOW_PASSWORD);
            android.provider.Settings.System.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.System.TIME_12_24);
        }

        /**
         *
         *
         * @unknown 
         */
        public static void getCloneToManagedProfileSettings(java.util.Set<java.lang.String> outKeySet) {
            outKeySet.addAll(android.provider.Settings.System.CLONE_TO_MANAGED_PROFILE);
        }

        /**
         * When to use Wi-Fi calling
         *
         * @see android.telephony.TelephonyManager.WifiCallingChoices
         * @unknown 
         */
        public static final java.lang.String WHEN_TO_MAKE_WIFI_CALLS = "when_to_make_wifi_calls";

        // Settings moved to Settings.Secure
        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#ADB_ENABLED}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String ADB_ENABLED = android.provider.Settings.Global.ADB_ENABLED;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Secure#ANDROID_ID} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String ANDROID_ID = android.provider.Settings.Secure.ANDROID_ID;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#BLUETOOTH_ON} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String BLUETOOTH_ON = android.provider.Settings.Global.BLUETOOTH_ON;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#DATA_ROAMING} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String DATA_ROAMING = android.provider.Settings.Global.DATA_ROAMING;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#DEVICE_PROVISIONED} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String DEVICE_PROVISIONED = android.provider.Settings.Global.DEVICE_PROVISIONED;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#HTTP_PROXY} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String HTTP_PROXY = android.provider.Settings.Global.HTTP_PROXY;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Secure#INSTALL_NON_MARKET_APPS} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String INSTALL_NON_MARKET_APPS = android.provider.Settings.Secure.INSTALL_NON_MARKET_APPS;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Secure#LOCATION_PROVIDERS_ALLOWED}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String LOCATION_PROVIDERS_ALLOWED = android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Secure#LOGGING_ID} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String LOGGING_ID = android.provider.Settings.Secure.LOGGING_ID;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#NETWORK_PREFERENCE} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String NETWORK_PREFERENCE = android.provider.Settings.Global.NETWORK_PREFERENCE;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Secure#PARENTAL_CONTROL_ENABLED}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String PARENTAL_CONTROL_ENABLED = android.provider.Settings.Secure.PARENTAL_CONTROL_ENABLED;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Secure#PARENTAL_CONTROL_LAST_UPDATE}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String PARENTAL_CONTROL_LAST_UPDATE = android.provider.Settings.Secure.PARENTAL_CONTROL_LAST_UPDATE;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Secure#PARENTAL_CONTROL_REDIRECT_URL}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String PARENTAL_CONTROL_REDIRECT_URL = android.provider.Settings.Secure.PARENTAL_CONTROL_REDIRECT_URL;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Secure#SETTINGS_CLASSNAME} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String SETTINGS_CLASSNAME = android.provider.Settings.Secure.SETTINGS_CLASSNAME;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#USB_MASS_STORAGE_ENABLED} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String USB_MASS_STORAGE_ENABLED = android.provider.Settings.Global.USB_MASS_STORAGE_ENABLED;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#USE_GOOGLE_MAIL} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String USE_GOOGLE_MAIL = android.provider.Settings.Global.USE_GOOGLE_MAIL;

        /**
         *
         *
         * @deprecated Use
        {@link android.provider.Settings.Global#WIFI_MAX_DHCP_RETRY_COUNT} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_MAX_DHCP_RETRY_COUNT = android.provider.Settings.Global.WIFI_MAX_DHCP_RETRY_COUNT;

        /**
         *
         *
         * @deprecated Use
        {@link android.provider.Settings.Global#WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS = android.provider.Settings.Global.WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS;

        /**
         *
         *
         * @deprecated Use
        {@link android.provider.Settings.Global#WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON = android.provider.Settings.Global.WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON;

        /**
         *
         *
         * @deprecated Use
        {@link android.provider.Settings.Global#WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY = android.provider.Settings.Global.WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#WIFI_NUM_OPEN_NETWORKS_KEPT}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_NUM_OPEN_NETWORKS_KEPT = android.provider.Settings.Global.WIFI_NUM_OPEN_NETWORKS_KEPT;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#WIFI_ON} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_ON = android.provider.Settings.Global.WIFI_ON;

        /**
         *
         *
         * @deprecated Use
        {@link android.provider.Settings.Secure#WIFI_WATCHDOG_ACCEPTABLE_PACKET_LOSS_PERCENTAGE}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_ACCEPTABLE_PACKET_LOSS_PERCENTAGE = android.provider.Settings.Secure.WIFI_WATCHDOG_ACCEPTABLE_PACKET_LOSS_PERCENTAGE;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Secure#WIFI_WATCHDOG_AP_COUNT} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_AP_COUNT = android.provider.Settings.Secure.WIFI_WATCHDOG_AP_COUNT;

        /**
         *
         *
         * @deprecated Use
        {@link android.provider.Settings.Secure#WIFI_WATCHDOG_BACKGROUND_CHECK_DELAY_MS} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_BACKGROUND_CHECK_DELAY_MS = android.provider.Settings.Secure.WIFI_WATCHDOG_BACKGROUND_CHECK_DELAY_MS;

        /**
         *
         *
         * @deprecated Use
        {@link android.provider.Settings.Secure#WIFI_WATCHDOG_BACKGROUND_CHECK_ENABLED} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_BACKGROUND_CHECK_ENABLED = android.provider.Settings.Secure.WIFI_WATCHDOG_BACKGROUND_CHECK_ENABLED;

        /**
         *
         *
         * @deprecated Use
        {@link android.provider.Settings.Secure#WIFI_WATCHDOG_BACKGROUND_CHECK_TIMEOUT_MS}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_BACKGROUND_CHECK_TIMEOUT_MS = android.provider.Settings.Secure.WIFI_WATCHDOG_BACKGROUND_CHECK_TIMEOUT_MS;

        /**
         *
         *
         * @deprecated Use
        {@link android.provider.Settings.Secure#WIFI_WATCHDOG_INITIAL_IGNORED_PING_COUNT} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_INITIAL_IGNORED_PING_COUNT = android.provider.Settings.Secure.WIFI_WATCHDOG_INITIAL_IGNORED_PING_COUNT;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Secure#WIFI_WATCHDOG_MAX_AP_CHECKS}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_MAX_AP_CHECKS = android.provider.Settings.Secure.WIFI_WATCHDOG_MAX_AP_CHECKS;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#WIFI_WATCHDOG_ON} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_ON = android.provider.Settings.Global.WIFI_WATCHDOG_ON;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Secure#WIFI_WATCHDOG_PING_COUNT} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_PING_COUNT = android.provider.Settings.Secure.WIFI_WATCHDOG_PING_COUNT;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Secure#WIFI_WATCHDOG_PING_DELAY_MS}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_PING_DELAY_MS = android.provider.Settings.Secure.WIFI_WATCHDOG_PING_DELAY_MS;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Secure#WIFI_WATCHDOG_PING_TIMEOUT_MS}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_PING_TIMEOUT_MS = android.provider.Settings.Secure.WIFI_WATCHDOG_PING_TIMEOUT_MS;

        /**
         * Checks if the specified app can modify system settings. As of API
         * level 23, an app cannot modify system settings unless it declares the
         * {@link android.Manifest.permission#WRITE_SETTINGS}
         * permission in its manifest, <em>and</em> the user specifically grants
         * the app this capability. To prompt the user to grant this approval,
         * the app must send an intent with the action {@link android.provider.Settings#ACTION_MANAGE_WRITE_SETTINGS}, which causes
         * the system to display a permission management screen.
         *
         * @param context
         * 		App context.
         * @return true if the calling app can write to system settings, false otherwise
         */
        public static boolean canWrite(android.content.Context context) {
            return android.provider.Settings.isCallingPackageAllowedToWriteSettings(context, android.os.Process.myUid(), context.getOpPackageName(), false);
        }
    }

    /**
     * Secure system settings, containing system preferences that applications
     * can read but are not allowed to write.  These are for preferences that
     * the user must explicitly modify through the system UI or specialized
     * APIs for those values, not modified directly by applications.
     */
    public static final class Secure extends android.provider.Settings.NameValueTable {
        /**
         * The content:// style URL for this table
         */
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse(("content://" + android.provider.Settings.AUTHORITY) + "/secure");

        // Populated lazily, guarded by class object:
        private static final android.provider.Settings.NameValueCache sNameValueCache = new android.provider.Settings.NameValueCache(android.provider.Settings.Secure.CONTENT_URI, android.provider.Settings.CALL_METHOD_GET_SECURE, android.provider.Settings.CALL_METHOD_PUT_SECURE);

        private static com.android.internal.widget.ILockSettings sLockSettings = null;

        private static boolean sIsSystemProcess;

        private static final java.util.HashSet<java.lang.String> MOVED_TO_LOCK_SETTINGS;

        private static final java.util.HashSet<java.lang.String> MOVED_TO_GLOBAL;

        static {
            MOVED_TO_LOCK_SETTINGS = new java.util.HashSet<java.lang.String>(3);
            android.provider.Settings.Secure.MOVED_TO_LOCK_SETTINGS.add(android.provider.Settings.Secure.LOCK_PATTERN_ENABLED);
            android.provider.Settings.Secure.MOVED_TO_LOCK_SETTINGS.add(android.provider.Settings.Secure.LOCK_PATTERN_VISIBLE);
            android.provider.Settings.Secure.MOVED_TO_LOCK_SETTINGS.add(android.provider.Settings.Secure.LOCK_PATTERN_TACTILE_FEEDBACK_ENABLED);
            MOVED_TO_GLOBAL = new java.util.HashSet<java.lang.String>();
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.ADB_ENABLED);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.ASSISTED_GPS_ENABLED);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.BLUETOOTH_ON);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.BUGREPORT_IN_POWER_MENU);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.CDMA_CELL_BROADCAST_SMS);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.CDMA_ROAMING_MODE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.CDMA_SUBSCRIPTION_MODE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DATA_ACTIVITY_TIMEOUT_MOBILE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DATA_ACTIVITY_TIMEOUT_WIFI);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DATA_ROAMING);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DEVELOPMENT_SETTINGS_ENABLED);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DEVICE_PROVISIONED);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DISPLAY_SIZE_FORCED);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DOWNLOAD_MAX_BYTES_OVER_MOBILE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DOWNLOAD_RECOMMENDED_MAX_BYTES_OVER_MOBILE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.MOBILE_DATA);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_DEV_BUCKET_DURATION);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_DEV_DELETE_AGE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_DEV_PERSIST_BYTES);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_DEV_ROTATE_AGE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_ENABLED);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_GLOBAL_ALERT_BYTES);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_POLL_INTERVAL);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_SAMPLE_ENABLED);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_TIME_CACHE_MAX_AGE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_UID_BUCKET_DURATION);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_UID_DELETE_AGE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_UID_PERSIST_BYTES);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_UID_ROTATE_AGE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_UID_TAG_BUCKET_DURATION);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_UID_TAG_DELETE_AGE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_UID_TAG_PERSIST_BYTES);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETSTATS_UID_TAG_ROTATE_AGE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NETWORK_PREFERENCE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NITZ_UPDATE_DIFF);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NITZ_UPDATE_SPACING);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NTP_SERVER);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NTP_TIMEOUT);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.PDP_WATCHDOG_ERROR_POLL_COUNT);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.PDP_WATCHDOG_LONG_POLL_INTERVAL_MS);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.PDP_WATCHDOG_MAX_PDP_RESET_FAIL_COUNT);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.PDP_WATCHDOG_POLL_INTERVAL_MS);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.PDP_WATCHDOG_TRIGGER_PACKET_COUNT);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.SAMPLING_PROFILER_MS);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.SETUP_PREPAID_DATA_SERVICE_URL);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.SETUP_PREPAID_DETECTION_REDIR_HOST);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.SETUP_PREPAID_DETECTION_TARGET_URL);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.TETHER_DUN_APN);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.TETHER_DUN_REQUIRED);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.TETHER_SUPPORTED);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.USB_MASS_STORAGE_ENABLED);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.USE_GOOGLE_MAIL);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_COUNTRY_CODE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_FRAMEWORK_SCAN_INTERVAL_MS);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_FREQUENCY_BAND);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_IDLE_MS);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_MAX_DHCP_RETRY_COUNT);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_NUM_OPEN_NETWORKS_KEPT);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_ON);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_P2P_DEVICE_NAME);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_SAVED_STATE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_SUPPLICANT_SCAN_INTERVAL_MS);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_SUSPEND_OPTIMIZATIONS_ENABLED);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_VERBOSE_LOGGING_ENABLED);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_ENHANCED_AUTO_JOIN);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_NETWORK_SHOW_RSSI);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_WATCHDOG_ON);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIFI_WATCHDOG_POOR_NETWORK_TEST_ENABLED);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WIMAX_NETWORKS_AVAILABLE_NOTIFICATION_ON);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.PACKAGE_VERIFIER_ENABLE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.PACKAGE_VERIFIER_TIMEOUT);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.PACKAGE_VERIFIER_DEFAULT_RESPONSE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DATA_STALL_ALARM_NON_AGGRESSIVE_DELAY_IN_MS);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DATA_STALL_ALARM_AGGRESSIVE_DELAY_IN_MS);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.GPRS_REGISTER_CHECK_PERIOD_MS);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WTF_IS_FATAL);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.BATTERY_DISCHARGE_DURATION_THRESHOLD);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.BATTERY_DISCHARGE_THRESHOLD);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.SEND_ACTION_APP_ERROR);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DROPBOX_AGE_SECONDS);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DROPBOX_MAX_FILES);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DROPBOX_QUOTA_KB);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DROPBOX_QUOTA_PERCENT);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DROPBOX_RESERVE_PERCENT);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DROPBOX_TAG_PREFIX);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.ERROR_LOGCAT_PREFIX);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.SYS_FREE_STORAGE_LOG_INTERVAL);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DISK_FREE_CHANGE_REPORTING_THRESHOLD);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.SYS_STORAGE_THRESHOLD_PERCENTAGE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.SYS_STORAGE_THRESHOLD_MAX_BYTES);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.SYS_STORAGE_FULL_THRESHOLD_BYTES);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.SYNC_MAX_RETRY_DELAY_IN_SECONDS);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.CONNECTIVITY_CHANGE_DELAY);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.CAPTIVE_PORTAL_DETECTION_ENABLED);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.CAPTIVE_PORTAL_SERVER);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.NSD_ON);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.SET_INSTALL_LOCATION);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DEFAULT_INSTALL_LOCATION);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.INET_CONDITION_DEBOUNCE_UP_DELAY);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.INET_CONDITION_DEBOUNCE_DOWN_DELAY);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.READ_EXTERNAL_STORAGE_ENFORCED_DEFAULT);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.HTTP_PROXY);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.GLOBAL_HTTP_PROXY_HOST);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.GLOBAL_HTTP_PROXY_PORT);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.GLOBAL_HTTP_PROXY_EXCLUSION_LIST);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.SET_GLOBAL_HTTP_PROXY);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.DEFAULT_DNS_SERVER);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.PREFERRED_NETWORK_MODE);
            android.provider.Settings.Secure.MOVED_TO_GLOBAL.add(android.provider.Settings.Global.WEBVIEW_DATA_REDUCTION_PROXY_KEY);
        }

        /**
         *
         *
         * @unknown 
         */
        public static void getMovedToGlobalSettings(java.util.Set<java.lang.String> outKeySet) {
            outKeySet.addAll(android.provider.Settings.Secure.MOVED_TO_GLOBAL);
        }

        /**
         * Look up a name in the database.
         *
         * @param resolver
         * 		to access the database with
         * @param name
         * 		to look up in the table
         * @return the corresponding value, or null if not present
         */
        public static java.lang.String getString(android.content.ContentResolver resolver, java.lang.String name) {
            return android.provider.Settings.Secure.getStringForUser(resolver, name, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static java.lang.String getStringForUser(android.content.ContentResolver resolver, java.lang.String name, int userHandle) {
            if (android.provider.Settings.Secure.MOVED_TO_GLOBAL.contains(name)) {
                android.util.Log.w(android.provider.Settings.TAG, (("Setting " + name) + " has moved from android.provider.Settings.Secure") + " to android.provider.Settings.Global.");
                return android.provider.Settings.Global.getStringForUser(resolver, name, userHandle);
            }
            if (android.provider.Settings.Secure.MOVED_TO_LOCK_SETTINGS.contains(name)) {
                synchronized(android.provider.Settings.Secure.class) {
                    if (android.provider.Settings.Secure.sLockSettings == null) {
                        android.provider.Settings.Secure.sLockSettings = ILockSettings.Stub.asInterface(((android.os.IBinder) (android.os.ServiceManager.getService("lock_settings"))));
                        android.provider.Settings.Secure.sIsSystemProcess = android.os.Process.myUid() == android.os.Process.SYSTEM_UID;
                    }
                }
                if ((android.provider.Settings.Secure.sLockSettings != null) && (!android.provider.Settings.Secure.sIsSystemProcess)) {
                    // No context; use the ActivityThread's context as an approximation for
                    // determining the target API level.
                    android.app.Application application = android.app.ActivityThread.currentApplication();
                    boolean isPreMnc = ((application != null) && (application.getApplicationInfo() != null)) && (application.getApplicationInfo().targetSdkVersion <= android.os.Build.VERSION_CODES.LOLLIPOP_MR1);
                    if (isPreMnc) {
                        try {
                            return android.provider.Settings.Secure.sLockSettings.getString(name, "0", userHandle);
                        } catch (android.os.RemoteException re) {
                            // Fall through
                        }
                    } else {
                        throw new java.lang.SecurityException((("Settings.Secure." + name) + " is deprecated and no longer accessible.") + " See API documentation for potential replacements.");
                    }
                }
            }
            return android.provider.Settings.Secure.sNameValueCache.getStringForUser(resolver, name, userHandle);
        }

        /**
         * Store a name/value pair into the database.
         *
         * @param resolver
         * 		to access the database with
         * @param name
         * 		to store
         * @param value
         * 		to associate with the name
         * @return true if the value was set, false on database errors
         */
        public static boolean putString(android.content.ContentResolver resolver, java.lang.String name, java.lang.String value) {
            return android.provider.Settings.Secure.putStringForUser(resolver, name, value, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static boolean putStringForUser(android.content.ContentResolver resolver, java.lang.String name, java.lang.String value, int userHandle) {
            if (android.provider.Settings.Secure.LOCATION_MODE.equals(name)) {
                // HACK ALERT: temporary hack to work around b/10491283.
                // TODO: once b/10491283 fixed, remove this hack
                return android.provider.Settings.Secure.setLocationModeForUser(resolver, java.lang.Integer.parseInt(value), userHandle);
            }
            if (android.provider.Settings.Secure.MOVED_TO_GLOBAL.contains(name)) {
                android.util.Log.w(android.provider.Settings.TAG, (("Setting " + name) + " has moved from android.provider.Settings.System") + " to android.provider.Settings.Global");
                return android.provider.Settings.Global.putStringForUser(resolver, name, value, userHandle);
            }
            return android.provider.Settings.Secure.sNameValueCache.putStringForUser(resolver, name, value, userHandle);
        }

        /**
         * Construct the content URI for a particular name/value pair,
         * useful for monitoring changes with a ContentObserver.
         *
         * @param name
         * 		to look up in the table
         * @return the corresponding content URI, or null if not present
         */
        public static android.net.Uri getUriFor(java.lang.String name) {
            if (android.provider.Settings.Secure.MOVED_TO_GLOBAL.contains(name)) {
                android.util.Log.w(android.provider.Settings.TAG, (("Setting " + name) + " has moved from android.provider.Settings.Secure") + " to android.provider.Settings.Global, returning global URI.");
                return android.provider.Settings.Global.getUriFor(android.provider.Settings.Global.CONTENT_URI, name);
            }
            return android.provider.Settings.NameValueTable.getUriFor(android.provider.Settings.Secure.CONTENT_URI, name);
        }

        /**
         * Convenience function for retrieving a single secure settings value
         * as an integer.  Note that internally setting values are always
         * stored as strings; this function converts the string to an integer
         * for you.  The default value will be returned if the setting is
         * not defined or not an integer.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @param def
         * 		Value to return if the setting is not defined.
         * @return The setting's current value, or 'def' if it is not defined
        or not a valid integer.
         */
        public static int getInt(android.content.ContentResolver cr, java.lang.String name, int def) {
            return android.provider.Settings.Secure.getIntForUser(cr, name, def, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static int getIntForUser(android.content.ContentResolver cr, java.lang.String name, int def, int userHandle) {
            if (android.provider.Settings.Secure.LOCATION_MODE.equals(name)) {
                // HACK ALERT: temporary hack to work around b/10491283.
                // TODO: once b/10491283 fixed, remove this hack
                return android.provider.Settings.Secure.getLocationModeForUser(cr, userHandle);
            }
            java.lang.String v = android.provider.Settings.Secure.getStringForUser(cr, name, userHandle);
            try {
                return v != null ? java.lang.Integer.parseInt(v) : def;
            } catch (java.lang.NumberFormatException e) {
                return def;
            }
        }

        /**
         * Convenience function for retrieving a single secure settings value
         * as an integer.  Note that internally setting values are always
         * stored as strings; this function converts the string to an integer
         * for you.
         * <p>
         * This version does not take a default value.  If the setting has not
         * been set, or the string value is not a number,
         * it throws {@link SettingNotFoundException}.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @throws SettingNotFoundException
         * 		Thrown if a setting by the given
         * 		name can't be found or the setting value is not an integer.
         * @return The setting's current value.
         */
        public static int getInt(android.content.ContentResolver cr, java.lang.String name) throws android.provider.Settings.SettingNotFoundException {
            return android.provider.Settings.Secure.getIntForUser(cr, name, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static int getIntForUser(android.content.ContentResolver cr, java.lang.String name, int userHandle) throws android.provider.Settings.SettingNotFoundException {
            if (android.provider.Settings.Secure.LOCATION_MODE.equals(name)) {
                // HACK ALERT: temporary hack to work around b/10491283.
                // TODO: once b/10491283 fixed, remove this hack
                return android.provider.Settings.Secure.getLocationModeForUser(cr, userHandle);
            }
            java.lang.String v = android.provider.Settings.Secure.getStringForUser(cr, name, userHandle);
            try {
                return java.lang.Integer.parseInt(v);
            } catch (java.lang.NumberFormatException e) {
                throw new android.provider.Settings.SettingNotFoundException(name);
            }
        }

        /**
         * Convenience function for updating a single settings value as an
         * integer. This will either create a new entry in the table if the
         * given name does not exist, or modify the value of the existing row
         * with that name.  Note that internally setting values are always
         * stored as strings, so this function converts the given value to a
         * string before storing it.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to modify.
         * @param value
         * 		The new value for the setting.
         * @return true if the value was set, false on database errors
         */
        public static boolean putInt(android.content.ContentResolver cr, java.lang.String name, int value) {
            return android.provider.Settings.Secure.putIntForUser(cr, name, value, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static boolean putIntForUser(android.content.ContentResolver cr, java.lang.String name, int value, int userHandle) {
            return android.provider.Settings.Secure.putStringForUser(cr, name, java.lang.Integer.toString(value), userHandle);
        }

        /**
         * Convenience function for retrieving a single secure settings value
         * as a {@code long}.  Note that internally setting values are always
         * stored as strings; this function converts the string to a {@code long}
         * for you.  The default value will be returned if the setting is
         * not defined or not a {@code long}.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @param def
         * 		Value to return if the setting is not defined.
         * @return The setting's current value, or 'def' if it is not defined
        or not a valid {@code long}.
         */
        public static long getLong(android.content.ContentResolver cr, java.lang.String name, long def) {
            return android.provider.Settings.Secure.getLongForUser(cr, name, def, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static long getLongForUser(android.content.ContentResolver cr, java.lang.String name, long def, int userHandle) {
            java.lang.String valString = android.provider.Settings.Secure.getStringForUser(cr, name, userHandle);
            long value;
            try {
                value = (valString != null) ? java.lang.Long.parseLong(valString) : def;
            } catch (java.lang.NumberFormatException e) {
                value = def;
            }
            return value;
        }

        /**
         * Convenience function for retrieving a single secure settings value
         * as a {@code long}.  Note that internally setting values are always
         * stored as strings; this function converts the string to a {@code long}
         * for you.
         * <p>
         * This version does not take a default value.  If the setting has not
         * been set, or the string value is not a number,
         * it throws {@link SettingNotFoundException}.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @return The setting's current value.
         * @throws SettingNotFoundException
         * 		Thrown if a setting by the given
         * 		name can't be found or the setting value is not an integer.
         */
        public static long getLong(android.content.ContentResolver cr, java.lang.String name) throws android.provider.Settings.SettingNotFoundException {
            return android.provider.Settings.Secure.getLongForUser(cr, name, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static long getLongForUser(android.content.ContentResolver cr, java.lang.String name, int userHandle) throws android.provider.Settings.SettingNotFoundException {
            java.lang.String valString = android.provider.Settings.Secure.getStringForUser(cr, name, userHandle);
            try {
                return java.lang.Long.parseLong(valString);
            } catch (java.lang.NumberFormatException e) {
                throw new android.provider.Settings.SettingNotFoundException(name);
            }
        }

        /**
         * Convenience function for updating a secure settings value as a long
         * integer. This will either create a new entry in the table if the
         * given name does not exist, or modify the value of the existing row
         * with that name.  Note that internally setting values are always
         * stored as strings, so this function converts the given value to a
         * string before storing it.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to modify.
         * @param value
         * 		The new value for the setting.
         * @return true if the value was set, false on database errors
         */
        public static boolean putLong(android.content.ContentResolver cr, java.lang.String name, long value) {
            return android.provider.Settings.Secure.putLongForUser(cr, name, value, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static boolean putLongForUser(android.content.ContentResolver cr, java.lang.String name, long value, int userHandle) {
            return android.provider.Settings.Secure.putStringForUser(cr, name, java.lang.Long.toString(value), userHandle);
        }

        /**
         * Convenience function for retrieving a single secure settings value
         * as a floating point number.  Note that internally setting values are
         * always stored as strings; this function converts the string to an
         * float for you. The default value will be returned if the setting
         * is not defined or not a valid float.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @param def
         * 		Value to return if the setting is not defined.
         * @return The setting's current value, or 'def' if it is not defined
        or not a valid float.
         */
        public static float getFloat(android.content.ContentResolver cr, java.lang.String name, float def) {
            return android.provider.Settings.Secure.getFloatForUser(cr, name, def, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static float getFloatForUser(android.content.ContentResolver cr, java.lang.String name, float def, int userHandle) {
            java.lang.String v = android.provider.Settings.Secure.getStringForUser(cr, name, userHandle);
            try {
                return v != null ? java.lang.Float.parseFloat(v) : def;
            } catch (java.lang.NumberFormatException e) {
                return def;
            }
        }

        /**
         * Convenience function for retrieving a single secure settings value
         * as a float.  Note that internally setting values are always
         * stored as strings; this function converts the string to a float
         * for you.
         * <p>
         * This version does not take a default value.  If the setting has not
         * been set, or the string value is not a number,
         * it throws {@link SettingNotFoundException}.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @throws SettingNotFoundException
         * 		Thrown if a setting by the given
         * 		name can't be found or the setting value is not a float.
         * @return The setting's current value.
         */
        public static float getFloat(android.content.ContentResolver cr, java.lang.String name) throws android.provider.Settings.SettingNotFoundException {
            return android.provider.Settings.Secure.getFloatForUser(cr, name, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static float getFloatForUser(android.content.ContentResolver cr, java.lang.String name, int userHandle) throws android.provider.Settings.SettingNotFoundException {
            java.lang.String v = android.provider.Settings.Secure.getStringForUser(cr, name, userHandle);
            if (v == null) {
                throw new android.provider.Settings.SettingNotFoundException(name);
            }
            try {
                return java.lang.Float.parseFloat(v);
            } catch (java.lang.NumberFormatException e) {
                throw new android.provider.Settings.SettingNotFoundException(name);
            }
        }

        /**
         * Convenience function for updating a single settings value as a
         * floating point number. This will either create a new entry in the
         * table if the given name does not exist, or modify the value of the
         * existing row with that name.  Note that internally setting values
         * are always stored as strings, so this function converts the given
         * value to a string before storing it.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to modify.
         * @param value
         * 		The new value for the setting.
         * @return true if the value was set, false on database errors
         */
        public static boolean putFloat(android.content.ContentResolver cr, java.lang.String name, float value) {
            return android.provider.Settings.Secure.putFloatForUser(cr, name, value, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static boolean putFloatForUser(android.content.ContentResolver cr, java.lang.String name, float value, int userHandle) {
            return android.provider.Settings.Secure.putStringForUser(cr, name, java.lang.Float.toString(value), userHandle);
        }

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#DEVELOPMENT_SETTINGS_ENABLED}
        instead
         */
        @java.lang.Deprecated
        public static final java.lang.String DEVELOPMENT_SETTINGS_ENABLED = android.provider.Settings.Global.DEVELOPMENT_SETTINGS_ENABLED;

        /**
         * When the user has enable the option to have a "bug report" command
         * in the power menu.
         *
         * @deprecated Use {@link android.provider.Settings.Global#BUGREPORT_IN_POWER_MENU} instead
         * @unknown 
         */
        @java.lang.Deprecated
        public static final java.lang.String BUGREPORT_IN_POWER_MENU = "bugreport_in_power_menu";

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#ADB_ENABLED} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String ADB_ENABLED = android.provider.Settings.Global.ADB_ENABLED;

        /**
         * Setting to allow mock locations and location provider status to be injected into the
         * LocationManager service for testing purposes during application development.  These
         * locations and status values  override actual location and status information generated
         * by network, gps, or other location providers.
         *
         * @deprecated This settings is not used anymore.
         */
        @java.lang.Deprecated
        public static final java.lang.String ALLOW_MOCK_LOCATION = "mock_location";

        /**
         * A 64-bit number (as a hex string) that is randomly
         * generated when the user first sets up the device and should remain
         * constant for the lifetime of the user's device. The value may
         * change if a factory reset is performed on the device.
         * <p class="note"><strong>Note:</strong> When a device has <a
         * href="{@docRoot }about/versions/android-4.2.html#MultipleUsers">multiple users</a>
         * (available on certain devices running Android 4.2 or higher), each user appears as a
         * completely separate device, so the {@code ANDROID_ID} value is unique to each
         * user.</p>
         */
        public static final java.lang.String ANDROID_ID = "android_id";

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#BLUETOOTH_ON} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String BLUETOOTH_ON = android.provider.Settings.Global.BLUETOOTH_ON;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#DATA_ROAMING} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String DATA_ROAMING = android.provider.Settings.Global.DATA_ROAMING;

        /**
         * Setting to record the input method used by default, holding the ID
         * of the desired method.
         */
        public static final java.lang.String DEFAULT_INPUT_METHOD = "default_input_method";

        /**
         * Setting to record the input method subtype used by default, holding the ID
         * of the desired method.
         */
        public static final java.lang.String SELECTED_INPUT_METHOD_SUBTYPE = "selected_input_method_subtype";

        /**
         * Setting to record the history of input method subtype, holding the pair of ID of IME
         * and its last used subtype.
         *
         * @unknown 
         */
        public static final java.lang.String INPUT_METHODS_SUBTYPE_HISTORY = "input_methods_subtype_history";

        /**
         * Setting to record the visibility of input method selector
         */
        public static final java.lang.String INPUT_METHOD_SELECTOR_VISIBILITY = "input_method_selector_visibility";

        /**
         * The currently selected voice interaction service flattened ComponentName.
         *
         * @unknown 
         */
        @android.annotation.TestApi
        public static final java.lang.String VOICE_INTERACTION_SERVICE = "voice_interaction_service";

        /**
         * bluetooth HCI snoop log configuration
         *
         * @unknown 
         */
        public static final java.lang.String BLUETOOTH_HCI_LOG = "bluetooth_hci_log";

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#DEVICE_PROVISIONED} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String DEVICE_PROVISIONED = android.provider.Settings.Global.DEVICE_PROVISIONED;

        /**
         * Whether the current user has been set up via setup wizard (0 = false, 1 = true)
         *
         * @unknown 
         */
        public static final java.lang.String USER_SETUP_COMPLETE = "user_setup_complete";

        /**
         * Prefix for category name that marks whether a suggested action from that category was
         * completed.
         *
         * @unknown 
         */
        public static final java.lang.String COMPLETED_CATEGORY_PREFIX = "suggested.completed_category.";

        /**
         * List of input methods that are currently enabled.  This is a string
         * containing the IDs of all enabled input methods, each ID separated
         * by ':'.
         */
        public static final java.lang.String ENABLED_INPUT_METHODS = "enabled_input_methods";

        /**
         * List of system input methods that are currently disabled.  This is a string
         * containing the IDs of all disabled input methods, each ID separated
         * by ':'.
         *
         * @unknown 
         */
        public static final java.lang.String DISABLED_SYSTEM_INPUT_METHODS = "disabled_system_input_methods";

        /**
         * Whether to show the IME when a hard keyboard is connected. This is a boolean that
         * determines if the IME should be shown when a hard keyboard is attached.
         *
         * @unknown 
         */
        public static final java.lang.String SHOW_IME_WITH_HARD_KEYBOARD = "show_ime_with_hard_keyboard";

        /**
         * Host name and port for global http proxy. Uses ':' seperator for
         * between host and port.
         *
         * @deprecated Use {@link Global#HTTP_PROXY}
         */
        @java.lang.Deprecated
        public static final java.lang.String HTTP_PROXY = android.provider.Settings.Global.HTTP_PROXY;

        /**
         * Package designated as always-on VPN provider.
         *
         * @unknown 
         */
        public static final java.lang.String ALWAYS_ON_VPN_APP = "always_on_vpn_app";

        /**
         * Whether to block networking outside of VPN connections while always-on is set.
         *
         * @see #ALWAYS_ON_VPN_APP
         * @unknown 
         */
        public static final java.lang.String ALWAYS_ON_VPN_LOCKDOWN = "always_on_vpn_lockdown";

        /**
         * Whether applications can be installed for this user via the system's
         * {@link Intent#ACTION_INSTALL_PACKAGE} mechanism.
         *
         * <p>1 = permit app installation via the system package installer intent
         * <p>0 = do not allow use of the package installer
         */
        public static final java.lang.String INSTALL_NON_MARKET_APPS = "install_non_market_apps";

        /**
         * Comma-separated list of location providers that activities may access. Do not rely on
         * this value being present in settings.db or on ContentObserver notifications on the
         * corresponding Uri.
         *
         * @deprecated use {@link #LOCATION_MODE} and
        {@link LocationManager#MODE_CHANGED_ACTION} (or
        {@link LocationManager#PROVIDERS_CHANGED_ACTION})
         */
        @java.lang.Deprecated
        public static final java.lang.String LOCATION_PROVIDERS_ALLOWED = "location_providers_allowed";

        /**
         * The degree of location access enabled by the user.
         * <p>
         * When used with {@link #putInt(ContentResolver, String, int)}, must be one of {@link #LOCATION_MODE_HIGH_ACCURACY}, {@link #LOCATION_MODE_SENSORS_ONLY}, {@link #LOCATION_MODE_BATTERY_SAVING}, or {@link #LOCATION_MODE_OFF}. When used with {@link #getInt(ContentResolver, String)}, the caller must gracefully handle additional location
         * modes that might be added in the future.
         * <p>
         * Note: do not rely on this value being present in settings.db or on ContentObserver
         * notifications for the corresponding Uri. Use {@link LocationManager#MODE_CHANGED_ACTION}
         * to receive changes in this value.
         */
        public static final java.lang.String LOCATION_MODE = "location_mode";

        /**
         * Stores the previous location mode when {@link #LOCATION_MODE} is set to
         * {@link #LOCATION_MODE_OFF}
         *
         * @unknown 
         */
        public static final java.lang.String LOCATION_PREVIOUS_MODE = "location_previous_mode";

        /**
         * Sets all location providers to the previous states before location was turned off.
         *
         * @unknown 
         */
        public static final int LOCATION_MODE_PREVIOUS = -1;

        /**
         * Location access disabled.
         */
        public static final int LOCATION_MODE_OFF = 0;

        /**
         * Network Location Provider disabled, but GPS and other sensors enabled.
         */
        public static final int LOCATION_MODE_SENSORS_ONLY = 1;

        /**
         * Reduced power usage, such as limiting the number of GPS updates per hour. Requests
         * with {@link android.location.Criteria#POWER_HIGH} may be downgraded to
         * {@link android.location.Criteria#POWER_MEDIUM}.
         */
        public static final int LOCATION_MODE_BATTERY_SAVING = 2;

        /**
         * Best-effort location computation allowed.
         */
        public static final int LOCATION_MODE_HIGH_ACCURACY = 3;

        /**
         * A flag containing settings used for biometric weak
         *
         * @unknown 
         */
        @java.lang.Deprecated
        public static final java.lang.String LOCK_BIOMETRIC_WEAK_FLAGS = "lock_biometric_weak_flags";

        /**
         * Whether lock-to-app will lock the keyguard when exiting.
         *
         * @unknown 
         */
        public static final java.lang.String LOCK_TO_APP_EXIT_LOCKED = "lock_to_app_exit_locked";

        /**
         * Whether autolock is enabled (0 = false, 1 = true)
         *
         * @deprecated Use {@link android.app.KeyguardManager} to determine the state and security
        level of the keyguard. Accessing this setting from an app that is targeting
        {@link VERSION_CODES#M} or later throws a {@code SecurityException}.
         */
        @java.lang.Deprecated
        public static final java.lang.String LOCK_PATTERN_ENABLED = "lock_pattern_autolock";

        /**
         * Whether lock pattern is visible as user enters (0 = false, 1 = true)
         *
         * @deprecated Accessing this setting from an app that is targeting
        {@link VERSION_CODES#M} or later throws a {@code SecurityException}.
         */
        @java.lang.Deprecated
        public static final java.lang.String LOCK_PATTERN_VISIBLE = "lock_pattern_visible_pattern";

        /**
         * Whether lock pattern will vibrate as user enters (0 = false, 1 =
         * true)
         *
         * @deprecated Starting in {@link VERSION_CODES#JELLY_BEAN_MR1} the
        lockscreen uses
        {@link Settings.System#HAPTIC_FEEDBACK_ENABLED}.
        Accessing this setting from an app that is targeting
        {@link VERSION_CODES#M} or later throws a {@code SecurityException}.
         */
        @java.lang.Deprecated
        public static final java.lang.String LOCK_PATTERN_TACTILE_FEEDBACK_ENABLED = "lock_pattern_tactile_feedback_enabled";

        /**
         * This preference allows the device to be locked given time after screen goes off,
         * subject to current DeviceAdmin policy limits.
         *
         * @unknown 
         */
        public static final java.lang.String LOCK_SCREEN_LOCK_AFTER_TIMEOUT = "lock_screen_lock_after_timeout";

        /**
         * This preference contains the string that shows for owner info on LockScreen.
         *
         * @unknown 
         * @deprecated 
         */
        public static final java.lang.String LOCK_SCREEN_OWNER_INFO = "lock_screen_owner_info";

        /**
         * Ids of the user-selected appwidgets on the lockscreen (comma-delimited).
         *
         * @unknown 
         */
        @java.lang.Deprecated
        public static final java.lang.String LOCK_SCREEN_APPWIDGET_IDS = "lock_screen_appwidget_ids";

        /**
         * Id of the appwidget shown on the lock screen when appwidgets are disabled.
         *
         * @unknown 
         */
        @java.lang.Deprecated
        public static final java.lang.String LOCK_SCREEN_FALLBACK_APPWIDGET_ID = "lock_screen_fallback_appwidget_id";

        /**
         * Index of the lockscreen appwidget to restore, -1 if none.
         *
         * @unknown 
         */
        @java.lang.Deprecated
        public static final java.lang.String LOCK_SCREEN_STICKY_APPWIDGET = "lock_screen_sticky_appwidget";

        /**
         * This preference enables showing the owner info on LockScreen.
         *
         * @unknown 
         * @deprecated 
         */
        public static final java.lang.String LOCK_SCREEN_OWNER_INFO_ENABLED = "lock_screen_owner_info_enabled";

        /**
         * When set by a user, allows notifications to be shown atop a securely locked screen
         * in their full "private" form (same as when the device is unlocked).
         *
         * @unknown 
         */
        public static final java.lang.String LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS = "lock_screen_allow_private_notifications";

        /**
         * When set by a user, allows notification remote input atop a securely locked screen
         * without having to unlock
         *
         * @unknown 
         */
        public static final java.lang.String LOCK_SCREEN_ALLOW_REMOTE_INPUT = "lock_screen_allow_remote_input";

        /**
         * Set by the system to track if the user needs to see the call to action for
         * the lockscreen notification policy.
         *
         * @unknown 
         */
        public static final java.lang.String SHOW_NOTE_ABOUT_NOTIFICATION_HIDING = "show_note_about_notification_hiding";

        /**
         * Set to 1 by the system after trust agents have been initialized.
         *
         * @unknown 
         */
        public static final java.lang.String TRUST_AGENTS_INITIALIZED = "trust_agents_initialized";

        /**
         * The Logging ID (a unique 64-bit value) as a hex string.
         * Used as a pseudonymous identifier for logging.
         *
         * @deprecated This identifier is poorly initialized and has
        many collisions.  It should not be used.
         */
        @java.lang.Deprecated
        public static final java.lang.String LOGGING_ID = "logging_id";

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#NETWORK_PREFERENCE} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String NETWORK_PREFERENCE = android.provider.Settings.Global.NETWORK_PREFERENCE;

        /**
         * No longer supported.
         */
        public static final java.lang.String PARENTAL_CONTROL_ENABLED = "parental_control_enabled";

        /**
         * No longer supported.
         */
        public static final java.lang.String PARENTAL_CONTROL_LAST_UPDATE = "parental_control_last_update";

        /**
         * No longer supported.
         */
        public static final java.lang.String PARENTAL_CONTROL_REDIRECT_URL = "parental_control_redirect_url";

        /**
         * Settings classname to launch when Settings is clicked from All
         * Applications.  Needed because of user testing between the old
         * and new Settings apps.
         */
        // TODO: 881807
        public static final java.lang.String SETTINGS_CLASSNAME = "settings_classname";

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#USB_MASS_STORAGE_ENABLED} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String USB_MASS_STORAGE_ENABLED = android.provider.Settings.Global.USB_MASS_STORAGE_ENABLED;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#USE_GOOGLE_MAIL} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String USE_GOOGLE_MAIL = android.provider.Settings.Global.USE_GOOGLE_MAIL;

        /**
         * If accessibility is enabled.
         */
        public static final java.lang.String ACCESSIBILITY_ENABLED = "accessibility_enabled";

        /**
         * If touch exploration is enabled.
         */
        public static final java.lang.String TOUCH_EXPLORATION_ENABLED = "touch_exploration_enabled";

        /**
         * List of the enabled accessibility providers.
         */
        public static final java.lang.String ENABLED_ACCESSIBILITY_SERVICES = "enabled_accessibility_services";

        /**
         * List of the accessibility services to which the user has granted
         * permission to put the device into touch exploration mode.
         *
         * @unknown 
         */
        public static final java.lang.String TOUCH_EXPLORATION_GRANTED_ACCESSIBILITY_SERVICES = "touch_exploration_granted_accessibility_services";

        /**
         * Whether to speak passwords while in accessibility mode.
         */
        public static final java.lang.String ACCESSIBILITY_SPEAK_PASSWORD = "speak_password";

        /**
         * Whether to draw text with high contrast while in accessibility mode.
         *
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_HIGH_TEXT_CONTRAST_ENABLED = "high_text_contrast_enabled";

        /**
         * If injection of accessibility enhancing JavaScript screen-reader
         * is enabled.
         * <p>
         *   Note: The JavaScript based screen-reader is served by the
         *   Google infrastructure and enable users with disabilities to
         *   efficiently navigate in and explore web content.
         * </p>
         * <p>
         *   This property represents a boolean value.
         * </p>
         *
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_SCRIPT_INJECTION = "accessibility_script_injection";

        /**
         * The URL for the injected JavaScript based screen-reader used
         * for providing accessibility of content in WebView.
         * <p>
         *   Note: The JavaScript based screen-reader is served by the
         *   Google infrastructure and enable users with disabilities to
         *   efficiently navigate in and explore web content.
         * </p>
         * <p>
         *   This property represents a string value.
         * </p>
         *
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_SCREEN_READER_URL = "accessibility_script_injection_url";

        /**
         * Key bindings for navigation in built-in accessibility support for web content.
         * <p>
         *   Note: These key bindings are for the built-in accessibility navigation for
         *   web content which is used as a fall back solution if JavaScript in a WebView
         *   is not enabled or the user has not opted-in script injection from Google.
         * </p>
         * <p>
         *   The bindings are separated by semi-colon. A binding is a mapping from
         *   a key to a sequence of actions (for more details look at
         *   android.webkit.AccessibilityInjector). A key is represented as the hexademical
         *   string representation of an integer obtained from a meta state (optional) shifted
         *   sixteen times left and bitwise ored with a key code. An action is represented
         *   as a hexademical string representation of an integer where the first two digits
         *   are navigation action index, the second, the third, and the fourth digit pairs
         *   represent the action arguments. The separate actions in a binding are colon
         *   separated. The key and the action sequence it maps to are separated by equals.
         * </p>
         * <p>
         *   For example, the binding below maps the DPAD right button to traverse the
         *   current navigation axis once without firing an accessibility event and to
         *   perform the same traversal again but to fire an event:
         *   <code>
         *     0x16=0x01000100:0x01000101;
         *   </code>
         * </p>
         * <p>
         *   The goal of this binding is to enable dynamic rebinding of keys to
         *   navigation actions for web content without requiring a framework change.
         * </p>
         * <p>
         *   This property represents a string value.
         * </p>
         *
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_WEB_CONTENT_KEY_BINDINGS = "accessibility_web_content_key_bindings";

        /**
         * Setting that specifies whether the display magnification is enabled.
         * Display magnifications allows the user to zoom in the display content
         * and is targeted to low vision users. The current magnification scale
         * is controlled by {@link #ACCESSIBILITY_DISPLAY_MAGNIFICATION_SCALE}.
         *
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_DISPLAY_MAGNIFICATION_ENABLED = "accessibility_display_magnification_enabled";

        /**
         * Setting that specifies what the display magnification scale is.
         * Display magnifications allows the user to zoom in the display
         * content and is targeted to low vision users. Whether a display
         * magnification is performed is controlled by
         * {@link #ACCESSIBILITY_DISPLAY_MAGNIFICATION_ENABLED}
         *
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_DISPLAY_MAGNIFICATION_SCALE = "accessibility_display_magnification_scale";

        /**
         * Setting that specifies whether the display magnification should be
         * automatically updated. If this fearture is enabled the system will
         * exit magnification mode or pan the viewport when a context change
         * occurs. For example, on staring a new activity or rotating the screen,
         * the system may zoom out so the user can see the new context he is in.
         * Another example is on showing a window that is not visible in the
         * magnified viewport the system may pan the viewport to make the window
         * the has popped up so the user knows that the context has changed.
         * Whether a screen magnification is performed is controlled by
         * {@link #ACCESSIBILITY_DISPLAY_MAGNIFICATION_ENABLED}
         *
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_DISPLAY_MAGNIFICATION_AUTO_UPDATE = "accessibility_display_magnification_auto_update";

        /**
         * Setting that specifies what mode the soft keyboard is in (default or hidden). Can be
         * modified from an AccessibilityService using the SoftKeyboardController.
         *
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_SOFT_KEYBOARD_MODE = "accessibility_soft_keyboard_mode";

        /**
         * Default soft keyboard behavior.
         *
         * @unknown 
         */
        public static final int SHOW_MODE_AUTO = 0;

        /**
         * Soft keyboard is never shown.
         *
         * @unknown 
         */
        public static final int SHOW_MODE_HIDDEN = 1;

        /**
         * Setting that specifies whether timed text (captions) should be
         * displayed in video content. Text display properties are controlled by
         * the following settings:
         * <ul>
         * <li>{@link #ACCESSIBILITY_CAPTIONING_LOCALE}
         * <li>{@link #ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR}
         * <li>{@link #ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR}
         * <li>{@link #ACCESSIBILITY_CAPTIONING_EDGE_COLOR}
         * <li>{@link #ACCESSIBILITY_CAPTIONING_EDGE_TYPE}
         * <li>{@link #ACCESSIBILITY_CAPTIONING_TYPEFACE}
         * <li>{@link #ACCESSIBILITY_CAPTIONING_FONT_SCALE}
         * </ul>
         *
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_CAPTIONING_ENABLED = "accessibility_captioning_enabled";

        /**
         * Setting that specifies the language for captions as a locale string,
         * e.g. en_US.
         *
         * @see java.util.Locale#toString
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_CAPTIONING_LOCALE = "accessibility_captioning_locale";

        /**
         * Integer property that specifies the preset style for captions, one
         * of:
         * <ul>
         * <li>{@link android.view.accessibility.CaptioningManager.CaptionStyle#PRESET_CUSTOM}
         * <li>a valid index of {@link android.view.accessibility.CaptioningManager.CaptionStyle#PRESETS}
         * </ul>
         *
         * @see java.util.Locale#toString
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_CAPTIONING_PRESET = "accessibility_captioning_preset";

        /**
         * Integer property that specifes the background color for captions as a
         * packed 32-bit color.
         *
         * @see android.graphics.Color#argb
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR = "accessibility_captioning_background_color";

        /**
         * Integer property that specifes the foreground color for captions as a
         * packed 32-bit color.
         *
         * @see android.graphics.Color#argb
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR = "accessibility_captioning_foreground_color";

        /**
         * Integer property that specifes the edge type for captions, one of:
         * <ul>
         * <li>{@link android.view.accessibility.CaptioningManager.CaptionStyle#EDGE_TYPE_NONE}
         * <li>{@link android.view.accessibility.CaptioningManager.CaptionStyle#EDGE_TYPE_OUTLINE}
         * <li>{@link android.view.accessibility.CaptioningManager.CaptionStyle#EDGE_TYPE_DROP_SHADOW}
         * </ul>
         *
         * @see #ACCESSIBILITY_CAPTIONING_EDGE_COLOR
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_CAPTIONING_EDGE_TYPE = "accessibility_captioning_edge_type";

        /**
         * Integer property that specifes the edge color for captions as a
         * packed 32-bit color.
         *
         * @see #ACCESSIBILITY_CAPTIONING_EDGE_TYPE
         * @see android.graphics.Color#argb
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_CAPTIONING_EDGE_COLOR = "accessibility_captioning_edge_color";

        /**
         * Integer property that specifes the window color for captions as a
         * packed 32-bit color.
         *
         * @see android.graphics.Color#argb
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_CAPTIONING_WINDOW_COLOR = "accessibility_captioning_window_color";

        /**
         * String property that specifies the typeface for captions, one of:
         * <ul>
         * <li>DEFAULT
         * <li>MONOSPACE
         * <li>SANS_SERIF
         * <li>SERIF
         * </ul>
         *
         * @see android.graphics.Typeface
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_CAPTIONING_TYPEFACE = "accessibility_captioning_typeface";

        /**
         * Floating point property that specifies font scaling for captions.
         *
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_CAPTIONING_FONT_SCALE = "accessibility_captioning_font_scale";

        /**
         * Setting that specifies whether display color inversion is enabled.
         */
        public static final java.lang.String ACCESSIBILITY_DISPLAY_INVERSION_ENABLED = "accessibility_display_inversion_enabled";

        /**
         * Setting that specifies whether display color space adjustment is
         * enabled.
         *
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED = "accessibility_display_daltonizer_enabled";

        /**
         * Integer property that specifies the type of color space adjustment to
         * perform. Valid values are defined in AccessibilityManager.
         *
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_DISPLAY_DALTONIZER = "accessibility_display_daltonizer";

        /**
         * Setting that specifies whether automatic click when the mouse pointer stops moving is
         * enabled.
         *
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_AUTOCLICK_ENABLED = "accessibility_autoclick_enabled";

        /**
         * Integer setting specifying amount of time in ms the mouse pointer has to stay still
         * before performing click when {@link #ACCESSIBILITY_AUTOCLICK_ENABLED} is set.
         *
         * @see #ACCESSIBILITY_AUTOCLICK_ENABLED
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_AUTOCLICK_DELAY = "accessibility_autoclick_delay";

        /**
         * Whether or not larger size icons are used for the pointer of mouse/trackpad for
         * accessibility.
         * (0 = false, 1 = true)
         *
         * @unknown 
         */
        public static final java.lang.String ACCESSIBILITY_LARGE_POINTER_ICON = "accessibility_large_pointer_icon";

        /**
         * The timeout for considering a press to be a long press in milliseconds.
         *
         * @unknown 
         */
        public static final java.lang.String LONG_PRESS_TIMEOUT = "long_press_timeout";

        /**
         * List of the enabled print services.
         *
         * N and beyond uses {@link #DISABLED_PRINT_SERVICES}. But this might be used in an upgrade
         * from pre-N.
         *
         * @unknown 
         */
        public static final java.lang.String ENABLED_PRINT_SERVICES = "enabled_print_services";

        /**
         * List of the disabled print services.
         *
         * @unknown 
         */
        public static final java.lang.String DISABLED_PRINT_SERVICES = "disabled_print_services";

        /**
         * The saved value for WindowManagerService.setForcedDisplayDensity()
         * formatted as a single integer representing DPI. If unset, then use
         * the real display density.
         *
         * @unknown 
         */
        public static final java.lang.String DISPLAY_DENSITY_FORCED = "display_density_forced";

        /**
         * Setting to always use the default text-to-speech settings regardless
         * of the application settings.
         * 1 = override application settings,
         * 0 = use application settings (if specified).
         *
         * @deprecated The value of this setting is no longer respected by
        the framework text to speech APIs as of the Ice Cream Sandwich release.
         */
        @java.lang.Deprecated
        public static final java.lang.String TTS_USE_DEFAULTS = "tts_use_defaults";

        /**
         * Default text-to-speech engine speech rate. 100 = 1x
         */
        public static final java.lang.String TTS_DEFAULT_RATE = "tts_default_rate";

        /**
         * Default text-to-speech engine pitch. 100 = 1x
         */
        public static final java.lang.String TTS_DEFAULT_PITCH = "tts_default_pitch";

        /**
         * Default text-to-speech engine.
         */
        public static final java.lang.String TTS_DEFAULT_SYNTH = "tts_default_synth";

        /**
         * Default text-to-speech language.
         *
         * @deprecated this setting is no longer in use, as of the Ice Cream
        Sandwich release. Apps should never need to read this setting directly,
        instead can query the TextToSpeech framework classes for the default
        locale. {@link TextToSpeech#getLanguage()}.
         */
        @java.lang.Deprecated
        public static final java.lang.String TTS_DEFAULT_LANG = "tts_default_lang";

        /**
         * Default text-to-speech country.
         *
         * @deprecated this setting is no longer in use, as of the Ice Cream
        Sandwich release. Apps should never need to read this setting directly,
        instead can query the TextToSpeech framework classes for the default
        locale. {@link TextToSpeech#getLanguage()}.
         */
        @java.lang.Deprecated
        public static final java.lang.String TTS_DEFAULT_COUNTRY = "tts_default_country";

        /**
         * Default text-to-speech locale variant.
         *
         * @deprecated this setting is no longer in use, as of the Ice Cream
        Sandwich release. Apps should never need to read this setting directly,
        instead can query the TextToSpeech framework classes for the
        locale that is in use {@link TextToSpeech#getLanguage()}.
         */
        @java.lang.Deprecated
        public static final java.lang.String TTS_DEFAULT_VARIANT = "tts_default_variant";

        /**
         * Stores the default tts locales on a per engine basis. Stored as
         * a comma seperated list of values, each value being of the form
         * {@code engine_name:locale} for example,
         * {@code com.foo.ttsengine:eng-USA,com.bar.ttsengine:esp-ESP}. This
         * supersedes {@link #TTS_DEFAULT_LANG}, {@link #TTS_DEFAULT_COUNTRY} and
         * {@link #TTS_DEFAULT_VARIANT}. Apps should never need to read this
         * setting directly, and can query the TextToSpeech framework classes
         * for the locale that is in use.
         *
         * @unknown 
         */
        public static final java.lang.String TTS_DEFAULT_LOCALE = "tts_default_locale";

        /**
         * Space delimited list of plugin packages that are enabled.
         */
        public static final java.lang.String TTS_ENABLED_PLUGINS = "tts_enabled_plugins";

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON}
        instead.
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON = android.provider.Settings.Global.WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY}
        instead.
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY = android.provider.Settings.Global.WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#WIFI_NUM_OPEN_NETWORKS_KEPT}
        instead.
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_NUM_OPEN_NETWORKS_KEPT = android.provider.Settings.Global.WIFI_NUM_OPEN_NETWORKS_KEPT;

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#WIFI_ON}
        instead.
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_ON = android.provider.Settings.Global.WIFI_ON;

        /**
         * The acceptable packet loss percentage (range 0 - 100) before trying
         * another AP on the same network.
         *
         * @deprecated This setting is not used.
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_ACCEPTABLE_PACKET_LOSS_PERCENTAGE = "wifi_watchdog_acceptable_packet_loss_percentage";

        /**
         * The number of access points required for a network in order for the
         * watchdog to monitor it.
         *
         * @deprecated This setting is not used.
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_AP_COUNT = "wifi_watchdog_ap_count";

        /**
         * The delay between background checks.
         *
         * @deprecated This setting is not used.
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_BACKGROUND_CHECK_DELAY_MS = "wifi_watchdog_background_check_delay_ms";

        /**
         * Whether the Wi-Fi watchdog is enabled for background checking even
         * after it thinks the user has connected to a good access point.
         *
         * @deprecated This setting is not used.
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_BACKGROUND_CHECK_ENABLED = "wifi_watchdog_background_check_enabled";

        /**
         * The timeout for a background ping
         *
         * @deprecated This setting is not used.
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_BACKGROUND_CHECK_TIMEOUT_MS = "wifi_watchdog_background_check_timeout_ms";

        /**
         * The number of initial pings to perform that *may* be ignored if they
         * fail. Again, if these fail, they will *not* be used in packet loss
         * calculation. For example, one network always seemed to time out for
         * the first couple pings, so this is set to 3 by default.
         *
         * @deprecated This setting is not used.
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_INITIAL_IGNORED_PING_COUNT = "wifi_watchdog_initial_ignored_ping_count";

        /**
         * The maximum number of access points (per network) to attempt to test.
         * If this number is reached, the watchdog will no longer monitor the
         * initial connection state for the network. This is a safeguard for
         * networks containing multiple APs whose DNS does not respond to pings.
         *
         * @deprecated This setting is not used.
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_MAX_AP_CHECKS = "wifi_watchdog_max_ap_checks";

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Global#WIFI_WATCHDOG_ON} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_ON = "wifi_watchdog_on";

        /**
         * A comma-separated list of SSIDs for which the Wi-Fi watchdog should be enabled.
         *
         * @deprecated This setting is not used.
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_WATCH_LIST = "wifi_watchdog_watch_list";

        /**
         * The number of pings to test if an access point is a good connection.
         *
         * @deprecated This setting is not used.
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_PING_COUNT = "wifi_watchdog_ping_count";

        /**
         * The delay between pings.
         *
         * @deprecated This setting is not used.
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_PING_DELAY_MS = "wifi_watchdog_ping_delay_ms";

        /**
         * The timeout per ping.
         *
         * @deprecated This setting is not used.
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_WATCHDOG_PING_TIMEOUT_MS = "wifi_watchdog_ping_timeout_ms";

        /**
         *
         *
         * @deprecated Use
        {@link android.provider.Settings.Global#WIFI_MAX_DHCP_RETRY_COUNT} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_MAX_DHCP_RETRY_COUNT = android.provider.Settings.Global.WIFI_MAX_DHCP_RETRY_COUNT;

        /**
         *
         *
         * @deprecated Use
        {@link android.provider.Settings.Global#WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS = android.provider.Settings.Global.WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS;

        /**
         * The number of milliseconds to hold on to a PendingIntent based request. This delay gives
         * the receivers of the PendingIntent an opportunity to make a new network request before
         * the Network satisfying the request is potentially removed.
         *
         * @unknown 
         */
        public static final java.lang.String CONNECTIVITY_RELEASE_PENDING_INTENT_DELAY_MS = "connectivity_release_pending_intent_delay_ms";

        /**
         * Whether background data usage is allowed.
         *
         * @deprecated As of {@link VERSION_CODES#ICE_CREAM_SANDWICH},
        availability of background data depends on several
        combined factors. When background data is unavailable,
        {@link ConnectivityManager#getActiveNetworkInfo()} will
        now appear disconnected.
         */
        @java.lang.Deprecated
        public static final java.lang.String BACKGROUND_DATA = "background_data";

        /**
         * Origins for which browsers should allow geolocation by default.
         * The value is a space-separated list of origins.
         */
        public static final java.lang.String ALLOWED_GEOLOCATION_ORIGINS = "allowed_geolocation_origins";

        /**
         * The preferred TTY mode     0 = TTy Off, CDMA default
         *                            1 = TTY Full
         *                            2 = TTY HCO
         *                            3 = TTY VCO
         *
         * @unknown 
         */
        public static final java.lang.String PREFERRED_TTY_MODE = "preferred_tty_mode";

        /**
         * Whether the enhanced voice privacy mode is enabled.
         * 0 = normal voice privacy
         * 1 = enhanced voice privacy
         *
         * @unknown 
         */
        public static final java.lang.String ENHANCED_VOICE_PRIVACY_ENABLED = "enhanced_voice_privacy_enabled";

        /**
         * Whether the TTY mode mode is enabled.
         * 0 = disabled
         * 1 = enabled
         *
         * @unknown 
         */
        public static final java.lang.String TTY_MODE_ENABLED = "tty_mode_enabled";

        /**
         * Controls whether settings backup is enabled.
         * Type: int ( 0 = disabled, 1 = enabled )
         *
         * @unknown 
         */
        public static final java.lang.String BACKUP_ENABLED = "backup_enabled";

        /**
         * Controls whether application data is automatically restored from backup
         * at install time.
         * Type: int ( 0 = disabled, 1 = enabled )
         *
         * @unknown 
         */
        public static final java.lang.String BACKUP_AUTO_RESTORE = "backup_auto_restore";

        /**
         * Indicates whether settings backup has been fully provisioned.
         * Type: int ( 0 = unprovisioned, 1 = fully provisioned )
         *
         * @unknown 
         */
        public static final java.lang.String BACKUP_PROVISIONED = "backup_provisioned";

        /**
         * Component of the transport to use for backup/restore.
         *
         * @unknown 
         */
        public static final java.lang.String BACKUP_TRANSPORT = "backup_transport";

        /**
         * Version for which the setup wizard was last shown.  Bumped for
         * each release when there is new setup information to show.
         *
         * @unknown 
         */
        public static final java.lang.String LAST_SETUP_SHOWN = "last_setup_shown";

        /**
         * The interval in milliseconds after which Wi-Fi is considered idle.
         * When idle, it is possible for the device to be switched from Wi-Fi to
         * the mobile data network.
         *
         * @unknown 
         * @deprecated Use {@link android.provider.Settings.Global#WIFI_IDLE_MS}
        instead.
         */
        @java.lang.Deprecated
        public static final java.lang.String WIFI_IDLE_MS = android.provider.Settings.Global.WIFI_IDLE_MS;

        /**
         * The global search provider chosen by the user (if multiple global
         * search providers are installed). This will be the provider returned
         * by {@link SearchManager#getGlobalSearchActivity()} if it's still
         * installed. This setting is stored as a flattened component name as
         * per {@link ComponentName#flattenToString()}.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_GLOBAL_SEARCH_ACTIVITY = "search_global_search_activity";

        /**
         * The number of promoted sources in GlobalSearch.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_NUM_PROMOTED_SOURCES = "search_num_promoted_sources";

        /**
         * The maximum number of suggestions returned by GlobalSearch.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_MAX_RESULTS_TO_DISPLAY = "search_max_results_to_display";

        /**
         * The number of suggestions GlobalSearch will ask each non-web search source for.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_MAX_RESULTS_PER_SOURCE = "search_max_results_per_source";

        /**
         * The number of suggestions the GlobalSearch will ask the web search source for.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_WEB_RESULTS_OVERRIDE_LIMIT = "search_web_results_override_limit";

        /**
         * The number of milliseconds that GlobalSearch will wait for suggestions from
         * promoted sources before continuing with all other sources.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_PROMOTED_SOURCE_DEADLINE_MILLIS = "search_promoted_source_deadline_millis";

        /**
         * The number of milliseconds before GlobalSearch aborts search suggesiton queries.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_SOURCE_TIMEOUT_MILLIS = "search_source_timeout_millis";

        /**
         * The maximum number of milliseconds that GlobalSearch shows the previous results
         * after receiving a new query.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_PREFILL_MILLIS = "search_prefill_millis";

        /**
         * The maximum age of log data used for shortcuts in GlobalSearch.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_MAX_STAT_AGE_MILLIS = "search_max_stat_age_millis";

        /**
         * The maximum age of log data used for source ranking in GlobalSearch.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_MAX_SOURCE_EVENT_AGE_MILLIS = "search_max_source_event_age_millis";

        /**
         * The minimum number of impressions needed to rank a source in GlobalSearch.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_MIN_IMPRESSIONS_FOR_SOURCE_RANKING = "search_min_impressions_for_source_ranking";

        /**
         * The minimum number of clicks needed to rank a source in GlobalSearch.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_MIN_CLICKS_FOR_SOURCE_RANKING = "search_min_clicks_for_source_ranking";

        /**
         * The maximum number of shortcuts shown by GlobalSearch.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_MAX_SHORTCUTS_RETURNED = "search_max_shortcuts_returned";

        /**
         * The size of the core thread pool for suggestion queries in GlobalSearch.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_QUERY_THREAD_CORE_POOL_SIZE = "search_query_thread_core_pool_size";

        /**
         * The maximum size of the thread pool for suggestion queries in GlobalSearch.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_QUERY_THREAD_MAX_POOL_SIZE = "search_query_thread_max_pool_size";

        /**
         * The size of the core thread pool for shortcut refreshing in GlobalSearch.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_SHORTCUT_REFRESH_CORE_POOL_SIZE = "search_shortcut_refresh_core_pool_size";

        /**
         * The maximum size of the thread pool for shortcut refreshing in GlobalSearch.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_SHORTCUT_REFRESH_MAX_POOL_SIZE = "search_shortcut_refresh_max_pool_size";

        /**
         * The maximun time that excess threads in the GlobalSeach thread pools will
         * wait before terminating.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_THREAD_KEEPALIVE_SECONDS = "search_thread_keepalive_seconds";

        /**
         * The maximum number of concurrent suggestion queries to each source.
         *
         * @unknown 
         */
        public static final java.lang.String SEARCH_PER_SOURCE_CONCURRENT_QUERY_LIMIT = "search_per_source_concurrent_query_limit";

        /**
         * Whether or not alert sounds are played on MountService events. (0 = false, 1 = true)
         *
         * @unknown 
         */
        public static final java.lang.String MOUNT_PLAY_NOTIFICATION_SND = "mount_play_not_snd";

        /**
         * Whether or not UMS auto-starts on UMS host detection. (0 = false, 1 = true)
         *
         * @unknown 
         */
        public static final java.lang.String MOUNT_UMS_AUTOSTART = "mount_ums_autostart";

        /**
         * Whether or not a notification is displayed on UMS host detection. (0 = false, 1 = true)
         *
         * @unknown 
         */
        public static final java.lang.String MOUNT_UMS_PROMPT = "mount_ums_prompt";

        /**
         * Whether or not a notification is displayed while UMS is enabled. (0 = false, 1 = true)
         *
         * @unknown 
         */
        public static final java.lang.String MOUNT_UMS_NOTIFY_ENABLED = "mount_ums_notify_enabled";

        /**
         * If nonzero, ANRs in invisible background processes bring up a dialog.
         * Otherwise, the process will be silently killed.
         *
         * Also prevents ANRs and crash dialogs from being suppressed.
         *
         * @unknown 
         */
        public static final java.lang.String ANR_SHOW_BACKGROUND = "anr_show_background";

        /**
         * The {@link ComponentName} string of the service to be used as the voice recognition
         * service.
         *
         * @unknown 
         */
        public static final java.lang.String VOICE_RECOGNITION_SERVICE = "voice_recognition_service";

        /**
         * Stores whether an user has consented to have apps verified through PAM.
         * The value is boolean (1 or 0).
         *
         * @unknown 
         */
        public static final java.lang.String PACKAGE_VERIFIER_USER_CONSENT = "package_verifier_user_consent";

        /**
         * The {@link ComponentName} string of the selected spell checker service which is
         * one of the services managed by the text service manager.
         *
         * @unknown 
         */
        public static final java.lang.String SELECTED_SPELL_CHECKER = "selected_spell_checker";

        /**
         * The {@link ComponentName} string of the selected subtype of the selected spell checker
         * service which is one of the services managed by the text service manager.
         *
         * @unknown 
         */
        public static final java.lang.String SELECTED_SPELL_CHECKER_SUBTYPE = "selected_spell_checker_subtype";

        /**
         * The {@link ComponentName} string whether spell checker is enabled or not.
         *
         * @unknown 
         */
        public static final java.lang.String SPELL_CHECKER_ENABLED = "spell_checker_enabled";

        /**
         * What happens when the user presses the Power button while in-call
         * and the screen is on.<br/>
         * <b>Values:</b><br/>
         * 1 - The Power button turns off the screen and locks the device. (Default behavior)<br/>
         * 2 - The Power button hangs up the current call.<br/>
         *
         * @unknown 
         */
        public static final java.lang.String INCALL_POWER_BUTTON_BEHAVIOR = "incall_power_button_behavior";

        /**
         * INCALL_POWER_BUTTON_BEHAVIOR value for "turn off screen".
         *
         * @unknown 
         */
        public static final int INCALL_POWER_BUTTON_BEHAVIOR_SCREEN_OFF = 0x1;

        /**
         * INCALL_POWER_BUTTON_BEHAVIOR value for "hang up".
         *
         * @unknown 
         */
        public static final int INCALL_POWER_BUTTON_BEHAVIOR_HANGUP = 0x2;

        /**
         * INCALL_POWER_BUTTON_BEHAVIOR default value.
         *
         * @unknown 
         */
        public static final int INCALL_POWER_BUTTON_BEHAVIOR_DEFAULT = android.provider.Settings.Secure.INCALL_POWER_BUTTON_BEHAVIOR_SCREEN_OFF;

        /**
         * Whether the device should wake when the wake gesture sensor detects motion.
         *
         * @unknown 
         */
        public static final java.lang.String WAKE_GESTURE_ENABLED = "wake_gesture_enabled";

        /**
         * Whether the device should doze if configured.
         *
         * @unknown 
         */
        public static final java.lang.String DOZE_ENABLED = "doze_enabled";

        /**
         * Whether the device should pulse on pick up gesture.
         *
         * @unknown 
         */
        public static final java.lang.String DOZE_PULSE_ON_PICK_UP = "doze_pulse_on_pick_up";

        /**
         * Whether the device should pulse on double tap gesture.
         *
         * @unknown 
         */
        public static final java.lang.String DOZE_PULSE_ON_DOUBLE_TAP = "doze_pulse_on_double_tap";

        /**
         * The current night mode that has been selected by the user.  Owned
         * and controlled by UiModeManagerService.  Constants are as per
         * UiModeManager.
         *
         * @unknown 
         */
        public static final java.lang.String UI_NIGHT_MODE = "ui_night_mode";

        /**
         * Whether screensavers are enabled.
         *
         * @unknown 
         */
        public static final java.lang.String SCREENSAVER_ENABLED = "screensaver_enabled";

        /**
         * The user's chosen screensaver components.
         *
         * These will be launched by the PhoneWindowManager after a timeout when not on
         * battery, or upon dock insertion (if SCREENSAVER_ACTIVATE_ON_DOCK is set to 1).
         *
         * @unknown 
         */
        public static final java.lang.String SCREENSAVER_COMPONENTS = "screensaver_components";

        /**
         * If screensavers are enabled, whether the screensaver should be automatically launched
         * when the device is inserted into a (desk) dock.
         *
         * @unknown 
         */
        public static final java.lang.String SCREENSAVER_ACTIVATE_ON_DOCK = "screensaver_activate_on_dock";

        /**
         * If screensavers are enabled, whether the screensaver should be automatically launched
         * when the screen times out when not on battery.
         *
         * @unknown 
         */
        public static final java.lang.String SCREENSAVER_ACTIVATE_ON_SLEEP = "screensaver_activate_on_sleep";

        /**
         * If screensavers are enabled, the default screensaver component.
         *
         * @unknown 
         */
        public static final java.lang.String SCREENSAVER_DEFAULT_COMPONENT = "screensaver_default_component";

        /**
         * The default NFC payment component
         *
         * @unknown 
         */
        public static final java.lang.String NFC_PAYMENT_DEFAULT_COMPONENT = "nfc_payment_default_component";

        /**
         * Whether NFC payment is handled by the foreground application or a default.
         *
         * @unknown 
         */
        public static final java.lang.String NFC_PAYMENT_FOREGROUND = "nfc_payment_foreground";

        /**
         * Specifies the package name currently configured to be the primary sms application
         *
         * @unknown 
         */
        public static final java.lang.String SMS_DEFAULT_APPLICATION = "sms_default_application";

        /**
         * Specifies the package name currently configured to be the default dialer application
         *
         * @unknown 
         */
        public static final java.lang.String DIALER_DEFAULT_APPLICATION = "dialer_default_application";

        /**
         * Specifies the package name currently configured to be the emergency assistance application
         *
         * @see android.telephony.TelephonyManager#ACTION_EMERGENCY_ASSISTANCE
         * @unknown 
         */
        public static final java.lang.String EMERGENCY_ASSISTANCE_APPLICATION = "emergency_assistance_application";

        /**
         * Specifies whether the current app context on scren (assist data) will be sent to the
         * assist application (active voice interaction service).
         *
         * @unknown 
         */
        public static final java.lang.String ASSIST_STRUCTURE_ENABLED = "assist_structure_enabled";

        /**
         * Specifies whether a screenshot of the screen contents will be sent to the assist
         * application (active voice interaction service).
         *
         * @unknown 
         */
        public static final java.lang.String ASSIST_SCREENSHOT_ENABLED = "assist_screenshot_enabled";

        /**
         * Specifies whether the screen will show an animation if screen contents are sent to the
         * assist application (active voice interaction service).
         *
         * Note that the disclosure will be forced for third-party assistants or if the device
         * does not support disabling it.
         *
         * @unknown 
         */
        public static final java.lang.String ASSIST_DISCLOSURE_ENABLED = "assist_disclosure_enabled";

        /**
         * Names of the service components that the current user has explicitly allowed to
         * see all of the user's notifications, separated by ':'.
         *
         * @unknown 
         */
        public static final java.lang.String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

        /**
         * Names of the packages that the current user has explicitly allowed to
         * manage notification policy configuration, separated by ':'.
         *
         * @unknown 
         */
        @android.annotation.TestApi
        public static final java.lang.String ENABLED_NOTIFICATION_POLICY_ACCESS_PACKAGES = "enabled_notification_policy_access_packages";

        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String BAR_SERVICE_COMPONENT = "bar_service_component";

        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String VOLUME_CONTROLLER_SERVICE_COMPONENT = "volume_controller_service_component";

        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String IMMERSIVE_MODE_CONFIRMATIONS = "immersive_mode_confirmations";

        /**
         * This is the query URI for finding a print service to install.
         *
         * @unknown 
         */
        public static final java.lang.String PRINT_SERVICE_SEARCH_URI = "print_service_search_uri";

        /**
         * This is the query URI for finding a NFC payment service to install.
         *
         * @unknown 
         */
        public static final java.lang.String PAYMENT_SERVICE_SEARCH_URI = "payment_service_search_uri";

        /**
         * If enabled, apps should try to skip any introductory hints on first launch. This might
         * apply to users that are already familiar with the environment or temporary users.
         * <p>
         * Type : int (0 to show hints, 1 to skip showing hints)
         */
        public static final java.lang.String SKIP_FIRST_USE_HINTS = "skip_first_use_hints";

        /**
         * Persisted playback time after a user confirmation of an unsafe volume level.
         *
         * @unknown 
         */
        public static final java.lang.String UNSAFE_VOLUME_MUSIC_ACTIVE_MS = "unsafe_volume_music_active_ms";

        /**
         * This preference enables notification display on the lockscreen.
         *
         * @unknown 
         */
        public static final java.lang.String LOCK_SCREEN_SHOW_NOTIFICATIONS = "lock_screen_show_notifications";

        /**
         * List of TV inputs that are currently hidden. This is a string
         * containing the IDs of all hidden TV inputs. Each ID is encoded by
         * {@link android.net.Uri#encode(String)} and separated by ':'.
         *
         * @unknown 
         */
        public static final java.lang.String TV_INPUT_HIDDEN_INPUTS = "tv_input_hidden_inputs";

        /**
         * List of custom TV input labels. This is a string containing <TV input id, custom name>
         * pairs. TV input id and custom name are encoded by {@link android.net.Uri#encode(String)}
         * and separated by ','. Each pair is separated by ':'.
         *
         * @unknown 
         */
        public static final java.lang.String TV_INPUT_CUSTOM_LABELS = "tv_input_custom_labels";

        /**
         * Whether automatic routing of system audio to USB audio peripheral is disabled.
         * The value is boolean (1 or 0), where 1 means automatic routing is disabled,
         * and 0 means automatic routing is enabled.
         *
         * @unknown 
         */
        public static final java.lang.String USB_AUDIO_AUTOMATIC_ROUTING_DISABLED = "usb_audio_automatic_routing_disabled";

        /**
         * The timeout in milliseconds before the device fully goes to sleep after
         * a period of inactivity.  This value sets an upper bound on how long the device
         * will stay awake or dreaming without user activity.  It should generally
         * be longer than {@link Settings.System#SCREEN_OFF_TIMEOUT} as otherwise the device
         * will sleep before it ever has a chance to dream.
         * <p>
         * Use -1 to disable this timeout.
         * </p>
         *
         * @unknown 
         */
        public static final java.lang.String SLEEP_TIMEOUT = "sleep_timeout";

        /**
         * Controls whether double tap to wake is enabled.
         *
         * @unknown 
         */
        public static final java.lang.String DOUBLE_TAP_TO_WAKE = "double_tap_to_wake";

        /**
         * The current assistant component. It could be a voice interaction service,
         * or an activity that handles ACTION_ASSIST, or empty which means using the default
         * handling.
         *
         * @unknown 
         */
        public static final java.lang.String ASSISTANT = "assistant";

        /**
         * Whether the camera launch gesture should be disabled.
         *
         * @unknown 
         */
        public static final java.lang.String CAMERA_GESTURE_DISABLED = "camera_gesture_disabled";

        /**
         * Whether the camera launch gesture to double tap the power button when the screen is off
         * should be disabled.
         *
         * @unknown 
         */
        public static final java.lang.String CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED = "camera_double_tap_power_gesture_disabled";

        /**
         * Whether the camera double twist gesture to flip between front and back mode should be
         * enabled.
         *
         * @unknown 
         */
        public static final java.lang.String CAMERA_DOUBLE_TWIST_TO_FLIP_ENABLED = "camera_double_twist_to_flip_enabled";

        /**
         * Control whether Night display is currently activated.
         *
         * @unknown 
         */
        public static final java.lang.String NIGHT_DISPLAY_ACTIVATED = "night_display_activated";

        /**
         * Control whether Night display will automatically activate/deactivate.
         *
         * @unknown 
         */
        public static final java.lang.String NIGHT_DISPLAY_AUTO_MODE = "night_display_auto_mode";

        /**
         * Custom time when Night display is scheduled to activate.
         * Represented as milliseconds from midnight (e.g. 79200000 == 10pm).
         *
         * @unknown 
         */
        public static final java.lang.String NIGHT_DISPLAY_CUSTOM_START_TIME = "night_display_custom_start_time";

        /**
         * Custom time when Night display is scheduled to deactivate.
         * Represented as milliseconds from midnight (e.g. 21600000 == 6am).
         *
         * @unknown 
         */
        public static final java.lang.String NIGHT_DISPLAY_CUSTOM_END_TIME = "night_display_custom_end_time";

        /**
         * Whether brightness should automatically adjust based on twilight state.
         *
         * @unknown 
         */
        public static final java.lang.String BRIGHTNESS_USE_TWILIGHT = "brightness_use_twilight";

        /**
         * Names of the service components that the current user has explicitly allowed to
         * be a VR mode listener, separated by ':'.
         *
         * @unknown 
         */
        public static final java.lang.String ENABLED_VR_LISTENERS = "enabled_vr_listeners";

        /**
         * Behavior of the display while in VR mode.
         *
         * One of {@link #VR_DISPLAY_MODE_LOW_PERSISTENCE} or {@link #VR_DISPLAY_MODE_OFF}.
         *
         * @unknown 
         */
        public static final java.lang.String VR_DISPLAY_MODE = "vr_display_mode";

        /**
         * Lower the display persistence while the system is in VR mode.
         *
         * @see PackageManager#FEATURE_VR_MODE_HIGH_PERFORMANCE
         * @unknown 
         */
        public static final int VR_DISPLAY_MODE_LOW_PERSISTENCE = 0;

        /**
         * Do not alter the display persistence while the system is in VR mode.
         *
         * @see PackageManager#FEATURE_VR_MODE_HIGH_PERFORMANCE
         * @unknown 
         */
        public static final int VR_DISPLAY_MODE_OFF = 1;

        /**
         * Whether CarrierAppUtils#disableCarrierAppsUntilPrivileged has been executed at least
         * once.
         *
         * <p>This is used to ensure that we only take one pass which will disable apps that are not
         * privileged (if any). From then on, we only want to enable apps (when a matching SIM is
         * inserted), to avoid disabling an app that the user might actively be using.
         *
         * <p>Will be set to 1 once executed.
         *
         * @unknown 
         */
        public static final java.lang.String CARRIER_APPS_HANDLED = "carrier_apps_handled";

        /**
         * Whether parent user can access remote contact in managed profile.
         *
         * @unknown 
         */
        public static final java.lang.String MANAGED_PROFILE_CONTACT_REMOTE_SEARCH = "managed_profile_contact_remote_search";

        /**
         * Whether or not the automatic storage manager is enabled and should run on the device.
         *
         * @unknown 
         */
        public static final java.lang.String AUTOMATIC_STORAGE_MANAGER_ENABLED = "automatic_storage_manager_enabled";

        /**
         * How many days of information for the automatic storage manager to retain on the device.
         *
         * @unknown 
         */
        public static final java.lang.String AUTOMATIC_STORAGE_MANAGER_DAYS_TO_RETAIN = "automatic_storage_manager_days_to_retain";

        /**
         * Default number of days of information for the automatic storage manager to retain.
         *
         * @unknown 
         */
        public static final int AUTOMATIC_STORAGE_MANAGER_DAYS_TO_RETAIN_DEFAULT = 90;

        /**
         * How many bytes the automatic storage manager has cleared out.
         *
         * @unknown 
         */
        public static final java.lang.String AUTOMATIC_STORAGE_MANAGER_BYTES_CLEARED = "automatic_storage_manager_bytes_cleared";

        /**
         * Last run time for the automatic storage manager.
         *
         * @unknown 
         */
        public static final java.lang.String AUTOMATIC_STORAGE_MANAGER_LAST_RUN = "automatic_storage_manager_last_run";

        /**
         * Whether SystemUI navigation keys is enabled.
         *
         * @unknown 
         */
        public static final java.lang.String SYSTEM_NAVIGATION_KEYS_ENABLED = "system_navigation_keys_enabled";

        /**
         * Holds comma separated list of ordering of QS tiles.
         *
         * @unknown 
         */
        public static final java.lang.String QS_TILES = "sysui_qs_tiles";

        /**
         * Whether preloaded APKs have been installed for the user.
         *
         * @unknown 
         */
        public static final java.lang.String DEMO_USER_SETUP_COMPLETE = "demo_user_setup_complete";

        /**
         * Specifies whether the web action API is enabled.
         *
         * @unknown 
         */
        public static final java.lang.String WEB_ACTION_ENABLED = "web_action_enabled";

        /**
         * This are the settings to be backed up.
         *
         * NOTE: Settings are backed up and restored in the order they appear
         *       in this array. If you have one setting depending on another,
         *       make sure that they are ordered appropriately.
         *
         * @unknown 
         */
        public static final java.lang.String[] SETTINGS_TO_BACKUP = new java.lang.String[]{ android.provider.Settings.Secure.BUGREPORT_IN_POWER_MENU, // moved to global
        android.provider.Settings.Secure.ALLOW_MOCK_LOCATION, android.provider.Settings.Secure.PARENTAL_CONTROL_ENABLED, android.provider.Settings.Secure.PARENTAL_CONTROL_REDIRECT_URL, android.provider.Settings.Secure.USB_MASS_STORAGE_ENABLED, // moved to global
        android.provider.Settings.Secure.ACCESSIBILITY_DISPLAY_INVERSION_ENABLED, android.provider.Settings.Secure.ACCESSIBILITY_DISPLAY_DALTONIZER, android.provider.Settings.Secure.ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED, android.provider.Settings.Secure.ACCESSIBILITY_DISPLAY_MAGNIFICATION_ENABLED, android.provider.Settings.Secure.ACCESSIBILITY_DISPLAY_MAGNIFICATION_SCALE, android.provider.Settings.Secure.ACCESSIBILITY_DISPLAY_MAGNIFICATION_AUTO_UPDATE, android.provider.Settings.Secure.ACCESSIBILITY_SCRIPT_INJECTION, android.provider.Settings.Secure.ACCESSIBILITY_WEB_CONTENT_KEY_BINDINGS, android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, android.provider.Settings.Secure.ENABLED_NOTIFICATION_LISTENERS, android.provider.Settings.Secure.ENABLED_VR_LISTENERS, android.provider.Settings.Secure.ENABLED_INPUT_METHODS, android.provider.Settings.Secure.TOUCH_EXPLORATION_GRANTED_ACCESSIBILITY_SERVICES, android.provider.Settings.Secure.TOUCH_EXPLORATION_ENABLED, android.provider.Settings.Secure.ACCESSIBILITY_ENABLED, android.provider.Settings.Secure.ACCESSIBILITY_SPEAK_PASSWORD, android.provider.Settings.Secure.ACCESSIBILITY_HIGH_TEXT_CONTRAST_ENABLED, android.provider.Settings.Secure.ACCESSIBILITY_CAPTIONING_PRESET, android.provider.Settings.Secure.ACCESSIBILITY_CAPTIONING_ENABLED, android.provider.Settings.Secure.ACCESSIBILITY_CAPTIONING_LOCALE, android.provider.Settings.Secure.ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR, android.provider.Settings.Secure.ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR, android.provider.Settings.Secure.ACCESSIBILITY_CAPTIONING_EDGE_TYPE, android.provider.Settings.Secure.ACCESSIBILITY_CAPTIONING_EDGE_COLOR, android.provider.Settings.Secure.ACCESSIBILITY_CAPTIONING_TYPEFACE, android.provider.Settings.Secure.ACCESSIBILITY_CAPTIONING_FONT_SCALE, android.provider.Settings.Secure.ACCESSIBILITY_CAPTIONING_WINDOW_COLOR, android.provider.Settings.Secure.TTS_USE_DEFAULTS, android.provider.Settings.Secure.TTS_DEFAULT_RATE, android.provider.Settings.Secure.TTS_DEFAULT_PITCH, android.provider.Settings.Secure.TTS_DEFAULT_SYNTH, android.provider.Settings.Secure.TTS_DEFAULT_LANG, android.provider.Settings.Secure.TTS_DEFAULT_COUNTRY, android.provider.Settings.Secure.TTS_ENABLED_PLUGINS, android.provider.Settings.Secure.TTS_DEFAULT_LOCALE, android.provider.Settings.Secure.SHOW_IME_WITH_HARD_KEYBOARD, android.provider.Settings.Secure.WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON// moved to global
        , android.provider.Settings.Secure.WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY, // moved to global
        android.provider.Settings.Secure.WIFI_NUM_OPEN_NETWORKS_KEPT, // moved to global
        android.provider.Settings.Secure.SELECTED_SPELL_CHECKER, android.provider.Settings.Secure.SELECTED_SPELL_CHECKER_SUBTYPE, android.provider.Settings.Secure.SPELL_CHECKER_ENABLED, android.provider.Settings.Secure.MOUNT_PLAY_NOTIFICATION_SND, android.provider.Settings.Secure.MOUNT_UMS_AUTOSTART, android.provider.Settings.Secure.MOUNT_UMS_PROMPT, android.provider.Settings.Secure.MOUNT_UMS_NOTIFY_ENABLED, android.provider.Settings.Secure.SLEEP_TIMEOUT, android.provider.Settings.Secure.DOUBLE_TAP_TO_WAKE, android.provider.Settings.Secure.WAKE_GESTURE_ENABLED, android.provider.Settings.Secure.LONG_PRESS_TIMEOUT, android.provider.Settings.Secure.CAMERA_GESTURE_DISABLED, android.provider.Settings.Secure.ACCESSIBILITY_AUTOCLICK_ENABLED, android.provider.Settings.Secure.ACCESSIBILITY_AUTOCLICK_DELAY, android.provider.Settings.Secure.ACCESSIBILITY_LARGE_POINTER_ICON, android.provider.Settings.Secure.PREFERRED_TTY_MODE, android.provider.Settings.Secure.ENHANCED_VOICE_PRIVACY_ENABLED, android.provider.Settings.Secure.TTY_MODE_ENABLED, android.provider.Settings.Secure.INCALL_POWER_BUTTON_BEHAVIOR, android.provider.Settings.Secure.NIGHT_DISPLAY_CUSTOM_START_TIME, android.provider.Settings.Secure.NIGHT_DISPLAY_CUSTOM_END_TIME, android.provider.Settings.Secure.NIGHT_DISPLAY_AUTO_MODE, android.provider.Settings.Secure.NIGHT_DISPLAY_ACTIVATED, android.provider.Settings.Secure.CAMERA_DOUBLE_TWIST_TO_FLIP_ENABLED, android.provider.Settings.Secure.CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED, android.provider.Settings.Secure.SYSTEM_NAVIGATION_KEYS_ENABLED, android.provider.Settings.Secure.QS_TILES, android.provider.Settings.Secure.DOZE_ENABLED, android.provider.Settings.Secure.DOZE_PULSE_ON_PICK_UP, android.provider.Settings.Secure.DOZE_PULSE_ON_DOUBLE_TAP };

        /**
         * These entries are considered common between the personal and the managed profile,
         * since the managed profile doesn't get to change them.
         */
        private static final java.util.Set<java.lang.String> CLONE_TO_MANAGED_PROFILE = new android.util.ArraySet<>();

        static {
            android.provider.Settings.Secure.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            android.provider.Settings.Secure.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.Secure.ALLOW_MOCK_LOCATION);
            android.provider.Settings.Secure.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.Secure.ALLOWED_GEOLOCATION_ORIGINS);
            android.provider.Settings.Secure.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.Secure.DEFAULT_INPUT_METHOD);
            android.provider.Settings.Secure.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            android.provider.Settings.Secure.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.Secure.ENABLED_INPUT_METHODS);
            android.provider.Settings.Secure.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.Secure.LOCATION_MODE);
            android.provider.Settings.Secure.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.Secure.LOCATION_PREVIOUS_MODE);
            android.provider.Settings.Secure.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            android.provider.Settings.Secure.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.Secure.SELECTED_INPUT_METHOD_SUBTYPE);
            android.provider.Settings.Secure.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.Secure.SELECTED_SPELL_CHECKER);
            android.provider.Settings.Secure.CLONE_TO_MANAGED_PROFILE.add(android.provider.Settings.Secure.SELECTED_SPELL_CHECKER_SUBTYPE);
        }

        /**
         *
         *
         * @unknown 
         */
        public static void getCloneToManagedProfileSettings(java.util.Set<java.lang.String> outKeySet) {
            outKeySet.addAll(android.provider.Settings.Secure.CLONE_TO_MANAGED_PROFILE);
        }

        /**
         * Helper method for determining if a location provider is enabled.
         *
         * @param cr
         * 		the content resolver to use
         * @param provider
         * 		the location provider to query
         * @return true if the provider is enabled
         * @deprecated use {@link #LOCATION_MODE} or
        {@link LocationManager#isProviderEnabled(String)}
         */
        @java.lang.Deprecated
        public static final boolean isLocationProviderEnabled(android.content.ContentResolver cr, java.lang.String provider) {
            return android.provider.Settings.Secure.isLocationProviderEnabledForUser(cr, provider, android.os.UserHandle.myUserId());
        }

        /**
         * Helper method for determining if a location provider is enabled.
         *
         * @param cr
         * 		the content resolver to use
         * @param provider
         * 		the location provider to query
         * @param userId
         * 		the userId to query
         * @return true if the provider is enabled
         * @deprecated use {@link #LOCATION_MODE} or
        {@link LocationManager#isProviderEnabled(String)}
         * @unknown 
         */
        @java.lang.Deprecated
        public static final boolean isLocationProviderEnabledForUser(android.content.ContentResolver cr, java.lang.String provider, int userId) {
            java.lang.String allowedProviders = android.provider.Settings.Secure.getStringForUser(cr, android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED, userId);
            return android.text.TextUtils.delimitedStringContains(allowedProviders, ',', provider);
        }

        /**
         * Thread-safe method for enabling or disabling a single location provider.
         *
         * @param cr
         * 		the content resolver to use
         * @param provider
         * 		the location provider to enable or disable
         * @param enabled
         * 		true if the provider should be enabled
         * @deprecated use {@link #putInt(ContentResolver, String, int)} and {@link #LOCATION_MODE}
         */
        @java.lang.Deprecated
        public static final void setLocationProviderEnabled(android.content.ContentResolver cr, java.lang.String provider, boolean enabled) {
            android.provider.Settings.Secure.setLocationProviderEnabledForUser(cr, provider, enabled, android.os.UserHandle.myUserId());
        }

        /**
         * Thread-safe method for enabling or disabling a single location provider.
         *
         * @param cr
         * 		the content resolver to use
         * @param provider
         * 		the location provider to enable or disable
         * @param enabled
         * 		true if the provider should be enabled
         * @param userId
         * 		the userId for which to enable/disable providers
         * @return true if the value was set, false on database errors
         * @deprecated use {@link #putIntForUser(ContentResolver, String, int, int)} and
        {@link #LOCATION_MODE}
         * @unknown 
         */
        @java.lang.Deprecated
        public static final boolean setLocationProviderEnabledForUser(android.content.ContentResolver cr, java.lang.String provider, boolean enabled, int userId) {
            synchronized(android.provider.Settings.mLocationSettingsLock) {
                // to ensure thread safety, we write the provider name with a '+' or '-'
                // and let the SettingsProvider handle it rather than reading and modifying
                // the list of enabled providers.
                if (enabled) {
                    provider = "+" + provider;
                } else {
                    provider = "-" + provider;
                }
                return android.provider.Settings.Secure.putStringForUser(cr, android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED, provider, userId);
            }
        }

        /**
         * Saves the current location mode into {@link #LOCATION_PREVIOUS_MODE}.
         */
        private static final boolean saveLocationModeForUser(android.content.ContentResolver cr, int userId) {
            final int mode = android.provider.Settings.Secure.getLocationModeForUser(cr, userId);
            return android.provider.Settings.Secure.putIntForUser(cr, android.provider.Settings.Secure.LOCATION_PREVIOUS_MODE, mode, userId);
        }

        /**
         * Restores the current location mode from {@link #LOCATION_PREVIOUS_MODE}.
         */
        private static final boolean restoreLocationModeForUser(android.content.ContentResolver cr, int userId) {
            int mode = android.provider.Settings.Secure.getIntForUser(cr, android.provider.Settings.Secure.LOCATION_PREVIOUS_MODE, android.provider.Settings.Secure.LOCATION_MODE_HIGH_ACCURACY, userId);
            // Make sure that the previous mode is never "off". Otherwise the user won't be able to
            // turn on location any longer.
            if (mode == android.provider.Settings.Secure.LOCATION_MODE_OFF) {
                mode = android.provider.Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;
            }
            return android.provider.Settings.Secure.setLocationModeForUser(cr, mode, userId);
        }

        /**
         * Thread-safe method for setting the location mode to one of
         * {@link #LOCATION_MODE_HIGH_ACCURACY}, {@link #LOCATION_MODE_SENSORS_ONLY},
         * {@link #LOCATION_MODE_BATTERY_SAVING}, or {@link #LOCATION_MODE_OFF}.
         *
         * @param cr
         * 		the content resolver to use
         * @param mode
         * 		such as {@link #LOCATION_MODE_HIGH_ACCURACY}
         * @param userId
         * 		the userId for which to change mode
         * @return true if the value was set, false on database errors
         * @throws IllegalArgumentException
         * 		if mode is not one of the supported values
         */
        private static final boolean setLocationModeForUser(android.content.ContentResolver cr, int mode, int userId) {
            synchronized(android.provider.Settings.mLocationSettingsLock) {
                boolean gps = false;
                boolean network = false;
                switch (mode) {
                    case android.provider.Settings.Secure.LOCATION_MODE_PREVIOUS :
                        // Retrieve the actual mode and set to that mode.
                        return android.provider.Settings.Secure.restoreLocationModeForUser(cr, userId);
                    case android.provider.Settings.Secure.LOCATION_MODE_OFF :
                        android.provider.Settings.Secure.saveLocationModeForUser(cr, userId);
                        break;
                    case android.provider.Settings.Secure.LOCATION_MODE_SENSORS_ONLY :
                        gps = true;
                        break;
                    case android.provider.Settings.Secure.LOCATION_MODE_BATTERY_SAVING :
                        network = true;
                        break;
                    case android.provider.Settings.Secure.LOCATION_MODE_HIGH_ACCURACY :
                        gps = true;
                        network = true;
                        break;
                    default :
                        throw new java.lang.IllegalArgumentException("Invalid location mode: " + mode);
                }
                // Note it's important that we set the NLP mode first. The Google implementation
                // of NLP clears its NLP consent setting any time it receives a
                // LocationManager.PROVIDERS_CHANGED_ACTION broadcast and NLP is disabled. Also,
                // it shows an NLP consent dialog any time it receives the broadcast, NLP is
                // enabled, and the NLP consent is not set. If 1) we were to enable GPS first,
                // 2) a setup wizard has its own NLP consent UI that sets the NLP consent setting,
                // and 3) the receiver happened to complete before we enabled NLP, then the Google
                // NLP would detect the attempt to enable NLP and show a redundant NLP consent
                // dialog. Then the people who wrote the setup wizard would be sad.
                boolean nlpSuccess = android.provider.Settings.Secure.setLocationProviderEnabledForUser(cr, android.location.LocationManager.NETWORK_PROVIDER, network, userId);
                boolean gpsSuccess = android.provider.Settings.Secure.setLocationProviderEnabledForUser(cr, android.location.LocationManager.GPS_PROVIDER, gps, userId);
                return gpsSuccess && nlpSuccess;
            }
        }

        /**
         * Thread-safe method for reading the location mode, returns one of
         * {@link #LOCATION_MODE_HIGH_ACCURACY}, {@link #LOCATION_MODE_SENSORS_ONLY},
         * {@link #LOCATION_MODE_BATTERY_SAVING}, or {@link #LOCATION_MODE_OFF}.
         *
         * @param cr
         * 		the content resolver to use
         * @param userId
         * 		the userId for which to read the mode
         * @return the location mode
         */
        private static final int getLocationModeForUser(android.content.ContentResolver cr, int userId) {
            synchronized(android.provider.Settings.mLocationSettingsLock) {
                boolean gpsEnabled = android.provider.Settings.Secure.isLocationProviderEnabledForUser(cr, android.location.LocationManager.GPS_PROVIDER, userId);
                boolean networkEnabled = android.provider.Settings.Secure.isLocationProviderEnabledForUser(cr, android.location.LocationManager.NETWORK_PROVIDER, userId);
                if (gpsEnabled && networkEnabled) {
                    return android.provider.Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;
                } else
                    if (gpsEnabled) {
                        return android.provider.Settings.Secure.LOCATION_MODE_SENSORS_ONLY;
                    } else
                        if (networkEnabled) {
                            return android.provider.Settings.Secure.LOCATION_MODE_BATTERY_SAVING;
                        } else {
                            return android.provider.Settings.Secure.LOCATION_MODE_OFF;
                        }


            }
        }
    }

    /**
     * Global system settings, containing preferences that always apply identically
     * to all defined users.  Applications can read these but are not allowed to write;
     * like the "Secure" settings, these are for preferences that the user must
     * explicitly modify through the system UI or specialized APIs for those values.
     */
    public static final class Global extends android.provider.Settings.NameValueTable {
        /**
         * The content:// style URL for global secure settings items.  Not public.
         */
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse(("content://" + android.provider.Settings.AUTHORITY) + "/global");

        /**
         * Whether users are allowed to add more users or guest from lockscreen.
         * <p>
         * Type: int
         *
         * @unknown 
         */
        public static final java.lang.String ADD_USERS_WHEN_LOCKED = "add_users_when_locked";

        /**
         * Setting whether the global gesture for enabling accessibility is enabled.
         * If this gesture is enabled the user will be able to perfrom it to enable
         * the accessibility state without visiting the settings app.
         *
         * @unknown 
         */
        public static final java.lang.String ENABLE_ACCESSIBILITY_GLOBAL_GESTURE_ENABLED = "enable_accessibility_global_gesture_enabled";

        /**
         * Whether Airplane Mode is on.
         */
        public static final java.lang.String AIRPLANE_MODE_ON = "airplane_mode_on";

        /**
         * Whether Theater Mode is on.
         * {@hide }
         */
        @android.annotation.SystemApi
        public static final java.lang.String THEATER_MODE_ON = "theater_mode_on";

        /**
         * Constant for use in AIRPLANE_MODE_RADIOS to specify Bluetooth radio.
         */
        public static final java.lang.String RADIO_BLUETOOTH = "bluetooth";

        /**
         * Constant for use in AIRPLANE_MODE_RADIOS to specify Wi-Fi radio.
         */
        public static final java.lang.String RADIO_WIFI = "wifi";

        /**
         * {@hide }
         */
        public static final java.lang.String RADIO_WIMAX = "wimax";

        /**
         * Constant for use in AIRPLANE_MODE_RADIOS to specify Cellular radio.
         */
        public static final java.lang.String RADIO_CELL = "cell";

        /**
         * Constant for use in AIRPLANE_MODE_RADIOS to specify NFC radio.
         */
        public static final java.lang.String RADIO_NFC = "nfc";

        /**
         * A comma separated list of radios that need to be disabled when airplane mode
         * is on. This overrides WIFI_ON and BLUETOOTH_ON, if Wi-Fi and bluetooth are
         * included in the comma separated list.
         */
        public static final java.lang.String AIRPLANE_MODE_RADIOS = "airplane_mode_radios";

        /**
         * A comma separated list of radios that should to be disabled when airplane mode
         * is on, but can be manually reenabled by the user.  For example, if RADIO_WIFI is
         * added to both AIRPLANE_MODE_RADIOS and AIRPLANE_MODE_TOGGLEABLE_RADIOS, then Wifi
         * will be turned off when entering airplane mode, but the user will be able to reenable
         * Wifi in the Settings app.
         *
         * {@hide }
         */
        public static final java.lang.String AIRPLANE_MODE_TOGGLEABLE_RADIOS = "airplane_mode_toggleable_radios";

        /**
         * A Long representing a bitmap of profiles that should be disabled when bluetooth starts.
         * See {@link android.bluetooth.BluetoothProfile}.
         * {@hide }
         */
        public static final java.lang.String BLUETOOTH_DISABLED_PROFILES = "bluetooth_disabled_profiles";

        /**
         * A semi-colon separated list of Bluetooth interoperability workarounds.
         * Each entry is a partial Bluetooth device address string and an integer representing
         * the feature to be disabled, separated by a comma. The integer must correspond
         * to a interoperability feature as defined in "interop.h" in /system/bt.
         * <p>
         * Example: <br/>
         *   "00:11:22,0;01:02:03:04,2"
         *
         * @unknown 
         */
        public static final java.lang.String BLUETOOTH_INTEROPERABILITY_LIST = "bluetooth_interoperability_list";

        /**
         * The policy for deciding when Wi-Fi should go to sleep (which will in
         * turn switch to using the mobile data as an Internet connection).
         * <p>
         * Set to one of {@link #WIFI_SLEEP_POLICY_DEFAULT},
         * {@link #WIFI_SLEEP_POLICY_NEVER_WHILE_PLUGGED}, or
         * {@link #WIFI_SLEEP_POLICY_NEVER}.
         */
        public static final java.lang.String WIFI_SLEEP_POLICY = "wifi_sleep_policy";

        /**
         * Value for {@link #WIFI_SLEEP_POLICY} to use the default Wi-Fi sleep
         * policy, which is to sleep shortly after the turning off
         * according to the {@link #STAY_ON_WHILE_PLUGGED_IN} setting.
         */
        public static final int WIFI_SLEEP_POLICY_DEFAULT = 0;

        /**
         * Value for {@link #WIFI_SLEEP_POLICY} to use the default policy when
         * the device is on battery, and never go to sleep when the device is
         * plugged in.
         */
        public static final int WIFI_SLEEP_POLICY_NEVER_WHILE_PLUGGED = 1;

        /**
         * Value for {@link #WIFI_SLEEP_POLICY} to never go to sleep.
         */
        public static final int WIFI_SLEEP_POLICY_NEVER = 2;

        /**
         * Value to specify if the user prefers the date, time and time zone
         * to be automatically fetched from the network (NITZ). 1=yes, 0=no
         */
        public static final java.lang.String AUTO_TIME = "auto_time";

        /**
         * Value to specify if the user prefers the time zone
         * to be automatically fetched from the network (NITZ). 1=yes, 0=no
         */
        public static final java.lang.String AUTO_TIME_ZONE = "auto_time_zone";

        /**
         * URI for the car dock "in" event sound.
         *
         * @unknown 
         */
        public static final java.lang.String CAR_DOCK_SOUND = "car_dock_sound";

        /**
         * URI for the car dock "out" event sound.
         *
         * @unknown 
         */
        public static final java.lang.String CAR_UNDOCK_SOUND = "car_undock_sound";

        /**
         * URI for the desk dock "in" event sound.
         *
         * @unknown 
         */
        public static final java.lang.String DESK_DOCK_SOUND = "desk_dock_sound";

        /**
         * URI for the desk dock "out" event sound.
         *
         * @unknown 
         */
        public static final java.lang.String DESK_UNDOCK_SOUND = "desk_undock_sound";

        /**
         * Whether to play a sound for dock events.
         *
         * @unknown 
         */
        public static final java.lang.String DOCK_SOUNDS_ENABLED = "dock_sounds_enabled";

        /**
         * URI for the "device locked" (keyguard shown) sound.
         *
         * @unknown 
         */
        public static final java.lang.String LOCK_SOUND = "lock_sound";

        /**
         * URI for the "device unlocked" sound.
         *
         * @unknown 
         */
        public static final java.lang.String UNLOCK_SOUND = "unlock_sound";

        /**
         * URI for the "device is trusted" sound, which is played when the device enters the trusted
         * state without unlocking.
         *
         * @unknown 
         */
        public static final java.lang.String TRUSTED_SOUND = "trusted_sound";

        /**
         * URI for the low battery sound file.
         *
         * @unknown 
         */
        public static final java.lang.String LOW_BATTERY_SOUND = "low_battery_sound";

        /**
         * Whether to play a sound for low-battery alerts.
         *
         * @unknown 
         */
        public static final java.lang.String POWER_SOUNDS_ENABLED = "power_sounds_enabled";

        /**
         * URI for the "wireless charging started" sound.
         *
         * @unknown 
         */
        public static final java.lang.String WIRELESS_CHARGING_STARTED_SOUND = "wireless_charging_started_sound";

        /**
         * Whether to play a sound for charging events.
         *
         * @unknown 
         */
        public static final java.lang.String CHARGING_SOUNDS_ENABLED = "charging_sounds_enabled";

        /**
         * Whether we keep the device on while the device is plugged in.
         * Supported values are:
         * <ul>
         * <li>{@code 0} to never stay on while plugged in</li>
         * <li>{@link BatteryManager#BATTERY_PLUGGED_AC} to stay on for AC charger</li>
         * <li>{@link BatteryManager#BATTERY_PLUGGED_USB} to stay on for USB charger</li>
         * <li>{@link BatteryManager#BATTERY_PLUGGED_WIRELESS} to stay on for wireless charger</li>
         * </ul>
         * These values can be OR-ed together.
         */
        public static final java.lang.String STAY_ON_WHILE_PLUGGED_IN = "stay_on_while_plugged_in";

        /**
         * When the user has enable the option to have a "bug report" command
         * in the power menu.
         *
         * @unknown 
         */
        public static final java.lang.String BUGREPORT_IN_POWER_MENU = "bugreport_in_power_menu";

        /**
         * Whether ADB is enabled.
         */
        public static final java.lang.String ADB_ENABLED = "adb_enabled";

        /**
         * Whether Views are allowed to save their attribute data.
         *
         * @unknown 
         */
        public static final java.lang.String DEBUG_VIEW_ATTRIBUTES = "debug_view_attributes";

        /**
         * Whether assisted GPS should be enabled or not.
         *
         * @unknown 
         */
        public static final java.lang.String ASSISTED_GPS_ENABLED = "assisted_gps_enabled";

        /**
         * Whether bluetooth is enabled/disabled
         * 0=disabled. 1=enabled.
         */
        public static final java.lang.String BLUETOOTH_ON = "bluetooth_on";

        /**
         * CDMA Cell Broadcast SMS
         *                            0 = CDMA Cell Broadcast SMS disabled
         *                            1 = CDMA Cell Broadcast SMS enabled
         *
         * @unknown 
         */
        public static final java.lang.String CDMA_CELL_BROADCAST_SMS = "cdma_cell_broadcast_sms";

        /**
         * The CDMA roaming mode 0 = Home Networks, CDMA default
         *                       1 = Roaming on Affiliated networks
         *                       2 = Roaming on any networks
         *
         * @unknown 
         */
        public static final java.lang.String CDMA_ROAMING_MODE = "roaming_settings";

        /**
         * The CDMA subscription mode 0 = RUIM/SIM (default)
         *                                1 = NV
         *
         * @unknown 
         */
        public static final java.lang.String CDMA_SUBSCRIPTION_MODE = "subscription_mode";

        /**
         * Inactivity timeout to track mobile data activity.
         *
         * If set to a positive integer, it indicates the inactivity timeout value in seconds to
         * infer the data activity of mobile network. After a period of no activity on mobile
         * networks with length specified by the timeout, an {@code ACTION_DATA_ACTIVITY_CHANGE}
         * intent is fired to indicate a transition of network status from "active" to "idle". Any
         * subsequent activity on mobile networks triggers the firing of {@code ACTION_DATA_ACTIVITY_CHANGE} intent indicating transition from "idle" to "active".
         *
         * Network activity refers to transmitting or receiving data on the network interfaces.
         *
         * Tracking is disabled if set to zero or negative value.
         *
         * @unknown 
         */
        public static final java.lang.String DATA_ACTIVITY_TIMEOUT_MOBILE = "data_activity_timeout_mobile";

        /**
         * Timeout to tracking Wifi data activity. Same as {@code DATA_ACTIVITY_TIMEOUT_MOBILE}
         * but for Wifi network.
         *
         * @unknown 
         */
        public static final java.lang.String DATA_ACTIVITY_TIMEOUT_WIFI = "data_activity_timeout_wifi";

        /**
         * Whether or not data roaming is enabled. (0 = false, 1 = true)
         */
        public static final java.lang.String DATA_ROAMING = "data_roaming";

        /**
         * The value passed to a Mobile DataConnection via bringUp which defines the
         * number of retries to preform when setting up the initial connection. The default
         * value defined in DataConnectionTrackerBase#DEFAULT_MDC_INITIAL_RETRY is currently 1.
         *
         * @unknown 
         */
        public static final java.lang.String MDC_INITIAL_MAX_RETRY = "mdc_initial_max_retry";

        /**
         * Whether any package can be on external storage. When this is true, any
         * package, regardless of manifest values, is a candidate for installing
         * or moving onto external storage. (0 = false, 1 = true)
         *
         * @unknown 
         */
        public static final java.lang.String FORCE_ALLOW_ON_EXTERNAL = "force_allow_on_external";

        /**
         * Whether any activity can be resized. When this is true, any
         * activity, regardless of manifest values, can be resized for multi-window.
         * (0 = false, 1 = true)
         *
         * @unknown 
         */
        public static final java.lang.String DEVELOPMENT_FORCE_RESIZABLE_ACTIVITIES = "force_resizable_activities";

        /**
         * Whether to enable experimental freeform support for windows.
         *
         * @unknown 
         */
        public static final java.lang.String DEVELOPMENT_ENABLE_FREEFORM_WINDOWS_SUPPORT = "enable_freeform_support";

        /**
         * Whether user has enabled development settings.
         */
        public static final java.lang.String DEVELOPMENT_SETTINGS_ENABLED = "development_settings_enabled";

        /**
         * Whether the device has been provisioned (0 = false, 1 = true).
         * <p>On a multiuser device with a separate system user, the screen may be locked
         * as soon as this is set to true and further activities cannot be launched on the
         * system user unless they are marked to show over keyguard.
         */
        public static final java.lang.String DEVICE_PROVISIONED = "device_provisioned";

        /**
         * Whether mobile data should be allowed while the device is being provisioned.
         * This allows the provisioning process to turn off mobile data before the user
         * has an opportunity to set things up, preventing other processes from burning
         * precious bytes before wifi is setup.
         * (0 = false, 1 = true)
         *
         * @unknown 
         */
        public static final java.lang.String DEVICE_PROVISIONING_MOBILE_DATA_ENABLED = "device_provisioning_mobile_data";

        /**
         * The saved value for WindowManagerService.setForcedDisplaySize().
         * Two integers separated by a comma.  If unset, then use the real display size.
         *
         * @unknown 
         */
        public static final java.lang.String DISPLAY_SIZE_FORCED = "display_size_forced";

        /**
         * The saved value for WindowManagerService.setForcedDisplayScalingMode().
         * 0 or unset if scaling is automatic, 1 if scaling is disabled.
         *
         * @unknown 
         */
        public static final java.lang.String DISPLAY_SCALING_FORCE = "display_scaling_force";

        /**
         * The maximum size, in bytes, of a download that the download manager will transfer over
         * a non-wifi connection.
         *
         * @unknown 
         */
        public static final java.lang.String DOWNLOAD_MAX_BYTES_OVER_MOBILE = "download_manager_max_bytes_over_mobile";

        /**
         * The recommended maximum size, in bytes, of a download that the download manager should
         * transfer over a non-wifi connection. Over this size, the use will be warned, but will
         * have the option to start the download over the mobile connection anyway.
         *
         * @unknown 
         */
        public static final java.lang.String DOWNLOAD_RECOMMENDED_MAX_BYTES_OVER_MOBILE = "download_manager_recommended_max_bytes_over_mobile";

        /**
         *
         *
         * @deprecated Use {@link android.provider.Settings.Secure#INSTALL_NON_MARKET_APPS} instead
         */
        @java.lang.Deprecated
        public static final java.lang.String INSTALL_NON_MARKET_APPS = android.provider.Settings.Secure.INSTALL_NON_MARKET_APPS;

        /**
         * Whether HDMI control shall be enabled. If disabled, no CEC/MHL command will be
         * sent or processed. (0 = false, 1 = true)
         *
         * @unknown 
         */
        public static final java.lang.String HDMI_CONTROL_ENABLED = "hdmi_control_enabled";

        /**
         * Whether HDMI system audio is enabled. If enabled, TV internal speaker is muted,
         * and the output is redirected to AV Receiver connected via
         * {@Global#HDMI_SYSTEM_AUDIO_OUTPUT }.
         *
         * @unknown 
         */
        public static final java.lang.String HDMI_SYSTEM_AUDIO_ENABLED = "hdmi_system_audio_enabled";

        /**
         * Whether TV will automatically turn on upon reception of the CEC command
         * &lt;Text View On&gt; or &lt;Image View On&gt;. (0 = false, 1 = true)
         *
         * @unknown 
         */
        public static final java.lang.String HDMI_CONTROL_AUTO_WAKEUP_ENABLED = "hdmi_control_auto_wakeup_enabled";

        /**
         * Whether TV will also turn off other CEC devices when it goes to standby mode.
         * (0 = false, 1 = true)
         *
         * @unknown 
         */
        public static final java.lang.String HDMI_CONTROL_AUTO_DEVICE_OFF_ENABLED = "hdmi_control_auto_device_off_enabled";

        /**
         * Whether TV will switch to MHL port when a mobile device is plugged in.
         * (0 = false, 1 = true)
         *
         * @unknown 
         */
        public static final java.lang.String MHL_INPUT_SWITCHING_ENABLED = "mhl_input_switching_enabled";

        /**
         * Whether TV will charge the mobile device connected at MHL port. (0 = false, 1 = true)
         *
         * @unknown 
         */
        public static final java.lang.String MHL_POWER_CHARGE_ENABLED = "mhl_power_charge_enabled";

        /**
         * Whether mobile data connections are allowed by the user.  See
         * ConnectivityManager for more info.
         *
         * @unknown 
         */
        public static final java.lang.String MOBILE_DATA = "mobile_data";

        /**
         * Whether the mobile data connection should remain active even when higher
         * priority networks like WiFi are active, to help make network switching faster.
         *
         * See ConnectivityService for more info.
         *
         * (0 = disabled, 1 = enabled)
         *
         * @unknown 
         */
        public static final java.lang.String MOBILE_DATA_ALWAYS_ON = "mobile_data_always_on";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_ENABLED = "netstats_enabled";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_POLL_INTERVAL = "netstats_poll_interval";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_TIME_CACHE_MAX_AGE = "netstats_time_cache_max_age";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_GLOBAL_ALERT_BYTES = "netstats_global_alert_bytes";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_SAMPLE_ENABLED = "netstats_sample_enabled";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_DEV_BUCKET_DURATION = "netstats_dev_bucket_duration";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_DEV_PERSIST_BYTES = "netstats_dev_persist_bytes";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_DEV_ROTATE_AGE = "netstats_dev_rotate_age";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_DEV_DELETE_AGE = "netstats_dev_delete_age";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_UID_BUCKET_DURATION = "netstats_uid_bucket_duration";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_UID_PERSIST_BYTES = "netstats_uid_persist_bytes";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_UID_ROTATE_AGE = "netstats_uid_rotate_age";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_UID_DELETE_AGE = "netstats_uid_delete_age";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_UID_TAG_BUCKET_DURATION = "netstats_uid_tag_bucket_duration";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_UID_TAG_PERSIST_BYTES = "netstats_uid_tag_persist_bytes";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_UID_TAG_ROTATE_AGE = "netstats_uid_tag_rotate_age";

        /**
         * {@hide }
         */
        public static final java.lang.String NETSTATS_UID_TAG_DELETE_AGE = "netstats_uid_tag_delete_age";

        /**
         * User preference for which network(s) should be used. Only the
         * connectivity service should touch this.
         */
        public static final java.lang.String NETWORK_PREFERENCE = "network_preference";

        /**
         * Which package name to use for network scoring. If null, or if the package is not a valid
         * scorer app, external network scores will neither be requested nor accepted.
         *
         * @unknown 
         */
        public static final java.lang.String NETWORK_SCORER_APP = "network_scorer_app";

        /**
         * If the NITZ_UPDATE_DIFF time is exceeded then an automatic adjustment
         * to SystemClock will be allowed even if NITZ_UPDATE_SPACING has not been
         * exceeded.
         *
         * @unknown 
         */
        public static final java.lang.String NITZ_UPDATE_DIFF = "nitz_update_diff";

        /**
         * The length of time in milli-seconds that automatic small adjustments to
         * SystemClock are ignored if NITZ_UPDATE_DIFF is not exceeded.
         *
         * @unknown 
         */
        public static final java.lang.String NITZ_UPDATE_SPACING = "nitz_update_spacing";

        /**
         * Preferred NTP server. {@hide }
         */
        public static final java.lang.String NTP_SERVER = "ntp_server";

        /**
         * Timeout in milliseconds to wait for NTP server. {@hide }
         */
        public static final java.lang.String NTP_TIMEOUT = "ntp_timeout";

        /**
         * {@hide }
         */
        public static final java.lang.String STORAGE_BENCHMARK_INTERVAL = "storage_benchmark_interval";

        /**
         * Sample validity in seconds to configure for the system DNS resolver.
         * {@hide }
         */
        public static final java.lang.String DNS_RESOLVER_SAMPLE_VALIDITY_SECONDS = "dns_resolver_sample_validity_seconds";

        /**
         * Success threshold in percent for use with the system DNS resolver.
         * {@hide }
         */
        public static final java.lang.String DNS_RESOLVER_SUCCESS_THRESHOLD_PERCENT = "dns_resolver_success_threshold_percent";

        /**
         * Minimum number of samples needed for statistics to be considered meaningful in the
         * system DNS resolver.
         * {@hide }
         */
        public static final java.lang.String DNS_RESOLVER_MIN_SAMPLES = "dns_resolver_min_samples";

        /**
         * Maximum number taken into account for statistics purposes in the system DNS resolver.
         * {@hide }
         */
        public static final java.lang.String DNS_RESOLVER_MAX_SAMPLES = "dns_resolver_max_samples";

        /**
         * Whether to disable the automatic scheduling of system updates.
         * 1 = system updates won't be automatically scheduled (will always
         * present notification instead).
         * 0 = system updates will be automatically scheduled. (default)
         *
         * @unknown 
         */
        @android.annotation.SystemApi
        public static final java.lang.String OTA_DISABLE_AUTOMATIC_UPDATE = "ota_disable_automatic_update";

        /**
         * Whether the package manager should send package verification broadcasts for verifiers to
         * review apps prior to installation.
         * 1 = request apps to be verified prior to installation, if a verifier exists.
         * 0 = do not verify apps before installation
         *
         * @unknown 
         */
        public static final java.lang.String PACKAGE_VERIFIER_ENABLE = "package_verifier_enable";

        /**
         * Timeout for package verification.
         *
         * @unknown 
         */
        public static final java.lang.String PACKAGE_VERIFIER_TIMEOUT = "verifier_timeout";

        /**
         * Default response code for package verification.
         *
         * @unknown 
         */
        public static final java.lang.String PACKAGE_VERIFIER_DEFAULT_RESPONSE = "verifier_default_response";

        /**
         * Show package verification setting in the Settings app.
         * 1 = show (default)
         * 0 = hide
         *
         * @unknown 
         */
        public static final java.lang.String PACKAGE_VERIFIER_SETTING_VISIBLE = "verifier_setting_visible";

        /**
         * Run package verification on apps installed through ADB/ADT/USB
         * 1 = perform package verification on ADB installs (default)
         * 0 = bypass package verification on ADB installs
         *
         * @unknown 
         */
        public static final java.lang.String PACKAGE_VERIFIER_INCLUDE_ADB = "verifier_verify_adb_installs";

        /**
         * Time since last fstrim (milliseconds) after which we force one to happen
         * during device startup.  If unset, the default is 3 days.
         *
         * @unknown 
         */
        public static final java.lang.String FSTRIM_MANDATORY_INTERVAL = "fstrim_mandatory_interval";

        /**
         * The interval in milliseconds at which to check packet counts on the
         * mobile data interface when screen is on, to detect possible data
         * connection problems.
         *
         * @unknown 
         */
        public static final java.lang.String PDP_WATCHDOG_POLL_INTERVAL_MS = "pdp_watchdog_poll_interval_ms";

        /**
         * The interval in milliseconds at which to check packet counts on the
         * mobile data interface when screen is off, to detect possible data
         * connection problems.
         *
         * @unknown 
         */
        public static final java.lang.String PDP_WATCHDOG_LONG_POLL_INTERVAL_MS = "pdp_watchdog_long_poll_interval_ms";

        /**
         * The interval in milliseconds at which to check packet counts on the
         * mobile data interface after {@link #PDP_WATCHDOG_TRIGGER_PACKET_COUNT}
         * outgoing packets has been reached without incoming packets.
         *
         * @unknown 
         */
        public static final java.lang.String PDP_WATCHDOG_ERROR_POLL_INTERVAL_MS = "pdp_watchdog_error_poll_interval_ms";

        /**
         * The number of outgoing packets sent without seeing an incoming packet
         * that triggers a countdown (of {@link #PDP_WATCHDOG_ERROR_POLL_COUNT}
         * device is logged to the event log
         *
         * @unknown 
         */
        public static final java.lang.String PDP_WATCHDOG_TRIGGER_PACKET_COUNT = "pdp_watchdog_trigger_packet_count";

        /**
         * The number of polls to perform (at {@link #PDP_WATCHDOG_ERROR_POLL_INTERVAL_MS})
         * after hitting {@link #PDP_WATCHDOG_TRIGGER_PACKET_COUNT} before
         * attempting data connection recovery.
         *
         * @unknown 
         */
        public static final java.lang.String PDP_WATCHDOG_ERROR_POLL_COUNT = "pdp_watchdog_error_poll_count";

        /**
         * The number of failed PDP reset attempts before moving to something more
         * drastic: re-registering to the network.
         *
         * @unknown 
         */
        public static final java.lang.String PDP_WATCHDOG_MAX_PDP_RESET_FAIL_COUNT = "pdp_watchdog_max_pdp_reset_fail_count";

        /**
         * A positive value indicates how often the SamplingProfiler
         * should take snapshots. Zero value means SamplingProfiler
         * is disabled.
         *
         * @unknown 
         */
        public static final java.lang.String SAMPLING_PROFILER_MS = "sampling_profiler_ms";

        /**
         * URL to open browser on to allow user to manage a prepay account
         *
         * @unknown 
         */
        public static final java.lang.String SETUP_PREPAID_DATA_SERVICE_URL = "setup_prepaid_data_service_url";

        /**
         * URL to attempt a GET on to see if this is a prepay device
         *
         * @unknown 
         */
        public static final java.lang.String SETUP_PREPAID_DETECTION_TARGET_URL = "setup_prepaid_detection_target_url";

        /**
         * Host to check for a redirect to after an attempt to GET
         * SETUP_PREPAID_DETECTION_TARGET_URL. (If we redirected there,
         * this is a prepaid device with zero balance.)
         *
         * @unknown 
         */
        public static final java.lang.String SETUP_PREPAID_DETECTION_REDIR_HOST = "setup_prepaid_detection_redir_host";

        /**
         * The interval in milliseconds at which to check the number of SMS sent out without asking
         * for use permit, to limit the un-authorized SMS usage.
         *
         * @unknown 
         */
        public static final java.lang.String SMS_OUTGOING_CHECK_INTERVAL_MS = "sms_outgoing_check_interval_ms";

        /**
         * The number of outgoing SMS sent without asking for user permit (of {@link #SMS_OUTGOING_CHECK_INTERVAL_MS}
         *
         * @unknown 
         */
        public static final java.lang.String SMS_OUTGOING_CHECK_MAX_COUNT = "sms_outgoing_check_max_count";

        /**
         * Used to disable SMS short code confirmation - defaults to true.
         * True indcates we will do the check, etc.  Set to false to disable.
         *
         * @see com.android.internal.telephony.SmsUsageMonitor
         * @unknown 
         */
        public static final java.lang.String SMS_SHORT_CODE_CONFIRMATION = "sms_short_code_confirmation";

        /**
         * Used to select which country we use to determine premium sms codes.
         * One of com.android.internal.telephony.SMSDispatcher.PREMIUM_RULE_USE_SIM,
         * com.android.internal.telephony.SMSDispatcher.PREMIUM_RULE_USE_NETWORK,
         * or com.android.internal.telephony.SMSDispatcher.PREMIUM_RULE_USE_BOTH.
         *
         * @unknown 
         */
        public static final java.lang.String SMS_SHORT_CODE_RULE = "sms_short_code_rule";

        /**
         * Used to select TCP's default initial receiver window size in segments - defaults to a build config value
         *
         * @unknown 
         */
        public static final java.lang.String TCP_DEFAULT_INIT_RWND = "tcp_default_init_rwnd";

        /**
         * Used to disable Tethering on a device - defaults to true
         *
         * @unknown 
         */
        public static final java.lang.String TETHER_SUPPORTED = "tether_supported";

        /**
         * Used to require DUN APN on the device or not - defaults to a build config value
         * which defaults to false
         *
         * @unknown 
         */
        public static final java.lang.String TETHER_DUN_REQUIRED = "tether_dun_required";

        /**
         * Used to hold a gservices-provisioned apn value for DUN.  If set, or the
         * corresponding build config values are set it will override the APN DB
         * values.
         * Consists of a comma seperated list of strings:
         * "name,apn,proxy,port,username,password,server,mmsc,mmsproxy,mmsport,mcc,mnc,auth,type"
         * note that empty fields can be ommitted: "name,apn,,,,,,,,,310,260,,DUN"
         *
         * @unknown 
         */
        public static final java.lang.String TETHER_DUN_APN = "tether_dun_apn";

        /**
         * List of carrier apps which are whitelisted to prompt the user for install when
         * a sim card with matching uicc carrier privilege rules is inserted.
         *
         * The value is "package1;package2;..."
         *
         * @unknown 
         */
        public static final java.lang.String CARRIER_APP_WHITELIST = "carrier_app_whitelist";

        /**
         * USB Mass Storage Enabled
         */
        public static final java.lang.String USB_MASS_STORAGE_ENABLED = "usb_mass_storage_enabled";

        /**
         * If this setting is set (to anything), then all references
         * to Gmail on the device must change to Google Mail.
         */
        public static final java.lang.String USE_GOOGLE_MAIL = "use_google_mail";

        /**
         * Webview Data reduction proxy key.
         *
         * @unknown 
         */
        public static final java.lang.String WEBVIEW_DATA_REDUCTION_PROXY_KEY = "webview_data_reduction_proxy_key";

        /**
         * Whether or not the WebView fallback mechanism should be enabled.
         * 0=disabled, 1=enabled.
         *
         * @unknown 
         */
        public static final java.lang.String WEBVIEW_FALLBACK_LOGIC_ENABLED = "webview_fallback_logic_enabled";

        /**
         * Name of the package used as WebView provider (if unset the provider is instead determined
         * by the system).
         *
         * @unknown 
         */
        public static final java.lang.String WEBVIEW_PROVIDER = "webview_provider";

        /**
         * Developer setting to enable WebView multiprocess rendering.
         *
         * @unknown 
         */
        @android.annotation.SystemApi
        public static final java.lang.String WEBVIEW_MULTIPROCESS = "webview_multiprocess";

        /**
         * The maximum number of notifications shown in 24 hours when switching networks.
         *
         * @unknown 
         */
        public static final java.lang.String NETWORK_SWITCH_NOTIFICATION_DAILY_LIMIT = "network_switch_notification_daily_limit";

        /**
         * The minimum time in milliseconds between notifications when switching networks.
         *
         * @unknown 
         */
        public static final java.lang.String NETWORK_SWITCH_NOTIFICATION_RATE_LIMIT_MILLIS = "network_switch_notification_rate_limit_millis";

        /**
         * Whether to automatically switch away from wifi networks that lose Internet access.
         * Only meaningful if config_networkAvoidBadWifi is set to 0, otherwise the system always
         * avoids such networks. Valid values are:
         *
         * 0: Don't avoid bad wifi, don't prompt the user. Get stuck on bad wifi like it's 2013.
         * null: Ask the user whether to switch away from bad wifi.
         * 1: Avoid bad wifi.
         *
         * @unknown 
         */
        public static final java.lang.String NETWORK_AVOID_BAD_WIFI = "network_avoid_bad_wifi";

        /**
         * Whether Wifi display is enabled/disabled
         * 0=disabled. 1=enabled.
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_DISPLAY_ON = "wifi_display_on";

        /**
         * Whether Wifi display certification mode is enabled/disabled
         * 0=disabled. 1=enabled.
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_DISPLAY_CERTIFICATION_ON = "wifi_display_certification_on";

        /**
         * WPS Configuration method used by Wifi display, this setting only
         * takes effect when WIFI_DISPLAY_CERTIFICATION_ON is 1 (enabled).
         *
         * Possible values are:
         *
         * WpsInfo.INVALID: use default WPS method chosen by framework
         * WpsInfo.PBC    : use Push button
         * WpsInfo.KEYPAD : use Keypad
         * WpsInfo.DISPLAY: use Display
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_DISPLAY_WPS_CONFIG = "wifi_display_wps_config";

        /**
         * Whether to notify the user of open networks.
         * <p>
         * If not connected and the scan results have an open network, we will
         * put this notification up. If we attempt to connect to a network or
         * the open network(s) disappear, we remove the notification. When we
         * show the notification, we will not show it again for
         * {@link android.provider.Settings.Secure#WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY} time.
         */
        public static final java.lang.String WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON = "wifi_networks_available_notification_on";

        /**
         * {@hide }
         */
        public static final java.lang.String WIMAX_NETWORKS_AVAILABLE_NOTIFICATION_ON = "wimax_networks_available_notification_on";

        /**
         * Delay (in seconds) before repeating the Wi-Fi networks available notification.
         * Connecting to a network will reset the timer.
         */
        public static final java.lang.String WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY = "wifi_networks_available_repeat_delay";

        /**
         * 802.11 country code in ISO 3166 format
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_COUNTRY_CODE = "wifi_country_code";

        /**
         * The interval in milliseconds to issue wake up scans when wifi needs
         * to connect. This is necessary to connect to an access point when
         * device is on the move and the screen is off.
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_FRAMEWORK_SCAN_INTERVAL_MS = "wifi_framework_scan_interval_ms";

        /**
         * The interval in milliseconds after which Wi-Fi is considered idle.
         * When idle, it is possible for the device to be switched from Wi-Fi to
         * the mobile data network.
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_IDLE_MS = "wifi_idle_ms";

        /**
         * When the number of open networks exceeds this number, the
         * least-recently-used excess networks will be removed.
         */
        public static final java.lang.String WIFI_NUM_OPEN_NETWORKS_KEPT = "wifi_num_open_networks_kept";

        /**
         * Whether the Wi-Fi should be on.  Only the Wi-Fi service should touch this.
         */
        public static final java.lang.String WIFI_ON = "wifi_on";

        /**
         * Setting to allow scans to be enabled even wifi is turned off for connectivity.
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_SCAN_ALWAYS_AVAILABLE = "wifi_scan_always_enabled";

        /**
         * Settings to allow BLE scans to be enabled even when Bluetooth is turned off for
         * connectivity.
         *
         * @unknown 
         */
        public static final java.lang.String BLE_SCAN_ALWAYS_AVAILABLE = "ble_scan_always_enabled";

        /**
         * Used to save the Wifi_ON state prior to tethering.
         * This state will be checked to restore Wifi after
         * the user turns off tethering.
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_SAVED_STATE = "wifi_saved_state";

        /**
         * The interval in milliseconds to scan as used by the wifi supplicant
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_SUPPLICANT_SCAN_INTERVAL_MS = "wifi_supplicant_scan_interval_ms";

        /**
         * whether frameworks handles wifi auto-join
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_ENHANCED_AUTO_JOIN = "wifi_enhanced_auto_join";

        /**
         * whether settings show RSSI
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_NETWORK_SHOW_RSSI = "wifi_network_show_rssi";

        /**
         * The interval in milliseconds to scan at supplicant when p2p is connected
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_SCAN_INTERVAL_WHEN_P2P_CONNECTED_MS = "wifi_scan_interval_p2p_connected_ms";

        /**
         * Whether the Wi-Fi watchdog is enabled.
         */
        public static final java.lang.String WIFI_WATCHDOG_ON = "wifi_watchdog_on";

        /**
         * Setting to turn off poor network avoidance on Wi-Fi. Feature is enabled by default and
         * the setting needs to be set to 0 to disable it.
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_WATCHDOG_POOR_NETWORK_TEST_ENABLED = "wifi_watchdog_poor_network_test_enabled";

        /**
         * Setting to turn on suspend optimizations at screen off on Wi-Fi. Enabled by default and
         * needs to be set to 0 to disable it.
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_SUSPEND_OPTIMIZATIONS_ENABLED = "wifi_suspend_optimizations_enabled";

        /**
         * Setting to enable verbose logging in Wi-Fi; disabled by default, and setting to 1
         * will enable it. In the future, additional values may be supported.
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_VERBOSE_LOGGING_ENABLED = "wifi_verbose_logging_enabled";

        /**
         * The maximum number of times we will retry a connection to an access
         * point for which we have failed in acquiring an IP address from DHCP.
         * A value of N means that we will make N+1 connection attempts in all.
         */
        public static final java.lang.String WIFI_MAX_DHCP_RETRY_COUNT = "wifi_max_dhcp_retry_count";

        /**
         * Maximum amount of time in milliseconds to hold a wakelock while waiting for mobile
         * data connectivity to be established after a disconnect from Wi-Fi.
         */
        public static final java.lang.String WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS = "wifi_mobile_data_transition_wakelock_timeout_ms";

        /**
         * This setting controls whether WiFi configurations created by a Device Owner app
         * should be locked down (that is, be editable or removable only by the Device Owner App,
         * not even by Settings app).
         * This setting takes integer values. Non-zero values mean DO created configurations
         * are locked down. Value of zero means they are not. Default value in the absence of
         * actual value to this setting is 0.
         */
        public static final java.lang.String WIFI_DEVICE_OWNER_CONFIGS_LOCKDOWN = "wifi_device_owner_configs_lockdown";

        /**
         * The operational wifi frequency band
         * Set to one of {@link WifiManager#WIFI_FREQUENCY_BAND_AUTO},
         * {@link WifiManager#WIFI_FREQUENCY_BAND_5GHZ} or
         * {@link WifiManager#WIFI_FREQUENCY_BAND_2GHZ}
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_FREQUENCY_BAND = "wifi_frequency_band";

        /**
         * The Wi-Fi peer-to-peer device name
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_P2P_DEVICE_NAME = "wifi_p2p_device_name";

        /**
         * The min time between wifi disable and wifi enable
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_REENABLE_DELAY_MS = "wifi_reenable_delay";

        /**
         * Timeout for ephemeral networks when all known BSSIDs go out of range. We will disconnect
         * from an ephemeral network if there is no BSSID for that network with a non-null score that
         * has been seen in this time period.
         *
         * If this is less than or equal to zero, we use a more conservative behavior and only check
         * for a non-null score from the currently connected or target BSSID.
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_EPHEMERAL_OUT_OF_RANGE_TIMEOUT_MS = "wifi_ephemeral_out_of_range_timeout_ms";

        /**
         * The number of milliseconds to delay when checking for data stalls during
         * non-aggressive detection. (screen is turned off.)
         *
         * @unknown 
         */
        public static final java.lang.String DATA_STALL_ALARM_NON_AGGRESSIVE_DELAY_IN_MS = "data_stall_alarm_non_aggressive_delay_in_ms";

        /**
         * The number of milliseconds to delay when checking for data stalls during
         * aggressive detection. (screen on or suspected data stall)
         *
         * @unknown 
         */
        public static final java.lang.String DATA_STALL_ALARM_AGGRESSIVE_DELAY_IN_MS = "data_stall_alarm_aggressive_delay_in_ms";

        /**
         * The number of milliseconds to allow the provisioning apn to remain active
         *
         * @unknown 
         */
        public static final java.lang.String PROVISIONING_APN_ALARM_DELAY_IN_MS = "provisioning_apn_alarm_delay_in_ms";

        /**
         * The interval in milliseconds at which to check gprs registration
         * after the first registration mismatch of gprs and voice service,
         * to detect possible data network registration problems.
         *
         * @unknown 
         */
        public static final java.lang.String GPRS_REGISTER_CHECK_PERIOD_MS = "gprs_register_check_period_ms";

        /**
         * Nonzero causes Log.wtf() to crash.
         *
         * @unknown 
         */
        public static final java.lang.String WTF_IS_FATAL = "wtf_is_fatal";

        /**
         * Ringer mode. This is used internally, changing this value will not
         * change the ringer mode. See AudioManager.
         */
        public static final java.lang.String MODE_RINGER = "mode_ringer";

        /**
         * Overlay display devices setting.
         * The associated value is a specially formatted string that describes the
         * size and density of simulated secondary display devices.
         * <p>
         * Format: {width}x{height}/{dpi};...
         * </p><p>
         * Example:
         * <ul>
         * <li><code>1280x720/213</code>: make one overlay that is 1280x720 at 213dpi.</li>
         * <li><code>1920x1080/320;1280x720/213</code>: make two overlays, the first
         * at 1080p and the second at 720p.</li>
         * <li>If the value is empty, then no overlay display devices are created.</li>
         * </ul></p>
         *
         * @unknown 
         */
        public static final java.lang.String OVERLAY_DISPLAY_DEVICES = "overlay_display_devices";

        /**
         * Threshold values for the duration and level of a discharge cycle,
         * under which we log discharge cycle info.
         *
         * @unknown 
         */
        public static final java.lang.String BATTERY_DISCHARGE_DURATION_THRESHOLD = "battery_discharge_duration_threshold";

        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String BATTERY_DISCHARGE_THRESHOLD = "battery_discharge_threshold";

        /**
         * Flag for allowing ActivityManagerService to send ACTION_APP_ERROR
         * intents on application crashes and ANRs. If this is disabled, the
         * crash/ANR dialog will never display the "Report" button.
         * <p>
         * Type: int (0 = disallow, 1 = allow)
         *
         * @unknown 
         */
        public static final java.lang.String SEND_ACTION_APP_ERROR = "send_action_app_error";

        /**
         * Maximum age of entries kept by {@link DropBoxManager}.
         *
         * @unknown 
         */
        public static final java.lang.String DROPBOX_AGE_SECONDS = "dropbox_age_seconds";

        /**
         * Maximum number of entry files which {@link DropBoxManager} will keep
         * around.
         *
         * @unknown 
         */
        public static final java.lang.String DROPBOX_MAX_FILES = "dropbox_max_files";

        /**
         * Maximum amount of disk space used by {@link DropBoxManager} no matter
         * what.
         *
         * @unknown 
         */
        public static final java.lang.String DROPBOX_QUOTA_KB = "dropbox_quota_kb";

        /**
         * Percent of free disk (excluding reserve) which {@link DropBoxManager}
         * will use.
         *
         * @unknown 
         */
        public static final java.lang.String DROPBOX_QUOTA_PERCENT = "dropbox_quota_percent";

        /**
         * Percent of total disk which {@link DropBoxManager} will never dip
         * into.
         *
         * @unknown 
         */
        public static final java.lang.String DROPBOX_RESERVE_PERCENT = "dropbox_reserve_percent";

        /**
         * Prefix for per-tag dropbox disable/enable settings.
         *
         * @unknown 
         */
        public static final java.lang.String DROPBOX_TAG_PREFIX = "dropbox:";

        /**
         * Lines of logcat to include with system crash/ANR/etc. reports, as a
         * prefix of the dropbox tag of the report type. For example,
         * "logcat_for_system_server_anr" controls the lines of logcat captured
         * with system server ANR reports. 0 to disable.
         *
         * @unknown 
         */
        public static final java.lang.String ERROR_LOGCAT_PREFIX = "logcat_for_";

        /**
         * The interval in minutes after which the amount of free storage left
         * on the device is logged to the event log
         *
         * @unknown 
         */
        public static final java.lang.String SYS_FREE_STORAGE_LOG_INTERVAL = "sys_free_storage_log_interval";

        /**
         * Threshold for the amount of change in disk free space required to
         * report the amount of free space. Used to prevent spamming the logs
         * when the disk free space isn't changing frequently.
         *
         * @unknown 
         */
        public static final java.lang.String DISK_FREE_CHANGE_REPORTING_THRESHOLD = "disk_free_change_reporting_threshold";

        /**
         * Minimum percentage of free storage on the device that is used to
         * determine if the device is running low on storage. The default is 10.
         * <p>
         * Say this value is set to 10, the device is considered running low on
         * storage if 90% or more of the device storage is filled up.
         *
         * @unknown 
         */
        public static final java.lang.String SYS_STORAGE_THRESHOLD_PERCENTAGE = "sys_storage_threshold_percentage";

        /**
         * Maximum byte size of the low storage threshold. This is to ensure
         * that {@link #SYS_STORAGE_THRESHOLD_PERCENTAGE} does not result in an
         * overly large threshold for large storage devices. Currently this must
         * be less than 2GB. This default is 500MB.
         *
         * @unknown 
         */
        public static final java.lang.String SYS_STORAGE_THRESHOLD_MAX_BYTES = "sys_storage_threshold_max_bytes";

        /**
         * Minimum bytes of free storage on the device before the data partition
         * is considered full. By default, 1 MB is reserved to avoid system-wide
         * SQLite disk full exceptions.
         *
         * @unknown 
         */
        public static final java.lang.String SYS_STORAGE_FULL_THRESHOLD_BYTES = "sys_storage_full_threshold_bytes";

        /**
         * The maximum reconnect delay for short network outages or when the
         * network is suspended due to phone use.
         *
         * @unknown 
         */
        public static final java.lang.String SYNC_MAX_RETRY_DELAY_IN_SECONDS = "sync_max_retry_delay_in_seconds";

        /**
         * The number of milliseconds to delay before sending out
         * {@link ConnectivityManager#CONNECTIVITY_ACTION} broadcasts. Ignored.
         *
         * @unknown 
         */
        public static final java.lang.String CONNECTIVITY_CHANGE_DELAY = "connectivity_change_delay";

        /**
         * Network sampling interval, in seconds. We'll generate link information
         * about bytes/packets sent and error rates based on data sampled in this interval
         *
         * @unknown 
         */
        public static final java.lang.String CONNECTIVITY_SAMPLING_INTERVAL_IN_SECONDS = "connectivity_sampling_interval_in_seconds";

        /**
         * The series of successively longer delays used in retrying to download PAC file.
         * Last delay is used between successful PAC downloads.
         *
         * @unknown 
         */
        public static final java.lang.String PAC_CHANGE_DELAY = "pac_change_delay";

        /**
         * Setting to turn off captive portal detection. Feature is enabled by
         * default and the setting needs to be set to 0 to disable it.
         *
         * @unknown 
         */
        public static final java.lang.String CAPTIVE_PORTAL_DETECTION_ENABLED = "captive_portal_detection_enabled";

        /**
         * The server used for captive portal detection upon a new conection. A
         * 204 response code from the server is used for validation.
         * TODO: remove this deprecated symbol.
         *
         * @unknown 
         */
        public static final java.lang.String CAPTIVE_PORTAL_SERVER = "captive_portal_server";

        /**
         * The URL used for HTTPS captive portal detection upon a new connection.
         * A 204 response code from the server is used for validation.
         *
         * @unknown 
         */
        public static final java.lang.String CAPTIVE_PORTAL_HTTPS_URL = "captive_portal_https_url";

        /**
         * The URL used for HTTP captive portal detection upon a new connection.
         * A 204 response code from the server is used for validation.
         *
         * @unknown 
         */
        public static final java.lang.String CAPTIVE_PORTAL_HTTP_URL = "captive_portal_http_url";

        /**
         * The URL used for fallback HTTP captive portal detection when previous HTTP
         * and HTTPS captive portal detection attemps did not return a conclusive answer.
         *
         * @unknown 
         */
        public static final java.lang.String CAPTIVE_PORTAL_FALLBACK_URL = "captive_portal_fallback_url";

        /**
         * Whether to use HTTPS for network validation. This is enabled by default and the setting
         * needs to be set to 0 to disable it. This setting is a misnomer because captive portals
         * don't actually use HTTPS, but it's consistent with the other settings.
         *
         * @unknown 
         */
        public static final java.lang.String CAPTIVE_PORTAL_USE_HTTPS = "captive_portal_use_https";

        /**
         * Which User-Agent string to use in the header of the captive portal detection probes.
         * The User-Agent field is unset when this setting has no value (HttpUrlConnection default).
         *
         * @unknown 
         */
        public static final java.lang.String CAPTIVE_PORTAL_USER_AGENT = "captive_portal_user_agent";

        /**
         * Whether network service discovery is enabled.
         *
         * @unknown 
         */
        public static final java.lang.String NSD_ON = "nsd_on";

        /**
         * Let user pick default install location.
         *
         * @unknown 
         */
        public static final java.lang.String SET_INSTALL_LOCATION = "set_install_location";

        /**
         * Default install location value.
         * 0 = auto, let system decide
         * 1 = internal
         * 2 = sdcard
         *
         * @unknown 
         */
        public static final java.lang.String DEFAULT_INSTALL_LOCATION = "default_install_location";

        /**
         * ms during which to consume extra events related to Inet connection
         * condition after a transtion to fully-connected
         *
         * @unknown 
         */
        public static final java.lang.String INET_CONDITION_DEBOUNCE_UP_DELAY = "inet_condition_debounce_up_delay";

        /**
         * ms during which to consume extra events related to Inet connection
         * condtion after a transtion to partly-connected
         *
         * @unknown 
         */
        public static final java.lang.String INET_CONDITION_DEBOUNCE_DOWN_DELAY = "inet_condition_debounce_down_delay";

        /**
         * {@hide }
         */
        public static final java.lang.String READ_EXTERNAL_STORAGE_ENFORCED_DEFAULT = "read_external_storage_enforced_default";

        /**
         * Host name and port for global http proxy. Uses ':' seperator for
         * between host and port.
         */
        public static final java.lang.String HTTP_PROXY = "http_proxy";

        /**
         * Host name for global http proxy. Set via ConnectivityManager.
         *
         * @unknown 
         */
        public static final java.lang.String GLOBAL_HTTP_PROXY_HOST = "global_http_proxy_host";

        /**
         * Integer host port for global http proxy. Set via ConnectivityManager.
         *
         * @unknown 
         */
        public static final java.lang.String GLOBAL_HTTP_PROXY_PORT = "global_http_proxy_port";

        /**
         * Exclusion list for global proxy. This string contains a list of
         * comma-separated domains where the global proxy does not apply.
         * Domains should be listed in a comma- separated list. Example of
         * acceptable formats: ".domain1.com,my.domain2.com" Use
         * ConnectivityManager to set/get.
         *
         * @unknown 
         */
        public static final java.lang.String GLOBAL_HTTP_PROXY_EXCLUSION_LIST = "global_http_proxy_exclusion_list";

        /**
         * The location PAC File for the proxy.
         *
         * @unknown 
         */
        public static final java.lang.String GLOBAL_HTTP_PROXY_PAC = "global_proxy_pac_url";

        /**
         * Enables the UI setting to allow the user to specify the global HTTP
         * proxy and associated exclusion list.
         *
         * @unknown 
         */
        public static final java.lang.String SET_GLOBAL_HTTP_PROXY = "set_global_http_proxy";

        /**
         * Setting for default DNS in case nobody suggests one
         *
         * @unknown 
         */
        public static final java.lang.String DEFAULT_DNS_SERVER = "default_dns_server";

        /**
         * {@hide }
         */
        public static final java.lang.String BLUETOOTH_HEADSET_PRIORITY_PREFIX = "bluetooth_headset_priority_";

        /**
         * {@hide }
         */
        public static final java.lang.String BLUETOOTH_A2DP_SINK_PRIORITY_PREFIX = "bluetooth_a2dp_sink_priority_";

        /**
         * {@hide }
         */
        public static final java.lang.String BLUETOOTH_A2DP_SRC_PRIORITY_PREFIX = "bluetooth_a2dp_src_priority_";

        /**
         * {@hide }
         */
        public static final java.lang.String BLUETOOTH_INPUT_DEVICE_PRIORITY_PREFIX = "bluetooth_input_device_priority_";

        /**
         * {@hide }
         */
        public static final java.lang.String BLUETOOTH_MAP_PRIORITY_PREFIX = "bluetooth_map_priority_";

        /**
         * {@hide }
         */
        public static final java.lang.String BLUETOOTH_PBAP_CLIENT_PRIORITY_PREFIX = "bluetooth_pbap_client_priority_";

        /**
         * {@hide }
         */
        public static final java.lang.String BLUETOOTH_SAP_PRIORITY_PREFIX = "bluetooth_sap_priority_";

        /**
         * Device Idle (Doze) specific settings.
         * This is encoded as a key=value list, separated by commas. Ex:
         *
         * "inactive_timeout=60000,sensing_timeout=400000"
         *
         * The following keys are supported:
         *
         * <pre>
         * inactive_to                      (long)
         * sensing_to                       (long)
         * motion_inactive_to               (long)
         * idle_after_inactive_to           (long)
         * idle_pending_to                  (long)
         * max_idle_pending_to              (long)
         * idle_pending_factor              (float)
         * idle_to                          (long)
         * max_idle_to                      (long)
         * idle_factor                      (float)
         * min_time_to_alarm                (long)
         * max_temp_app_whitelist_duration  (long)
         * notification_whitelist_duration  (long)
         * </pre>
         *
         * <p>
         * Type: string
         *
         * @unknown 
         * @see com.android.server.DeviceIdleController.Constants
         */
        public static final java.lang.String DEVICE_IDLE_CONSTANTS = "device_idle_constants";

        /**
         * Device Idle (Doze) specific settings for watches. See {@code #DEVICE_IDLE_CONSTANTS}
         *
         * <p>
         * Type: string
         *
         * @unknown 
         * @see com.android.server.DeviceIdleController.Constants
         */
        public static final java.lang.String DEVICE_IDLE_CONSTANTS_WATCH = "device_idle_constants_watch";

        /**
         * App standby (app idle) specific settings.
         * This is encoded as a key=value list, separated by commas. Ex:
         *
         * "idle_duration=5000,parole_interval=4500"
         *
         * The following keys are supported:
         *
         * <pre>
         * idle_duration2       (long)
         * wallclock_threshold  (long)
         * parole_interval      (long)
         * parole_duration      (long)
         *
         * idle_duration        (long) // This is deprecated and used to circumvent b/26355386.
         * </pre>
         *
         * <p>
         * Type: string
         *
         * @unknown 
         * @see com.android.server.usage.UsageStatsService.SettingsObserver
         */
        public static final java.lang.String APP_IDLE_CONSTANTS = "app_idle_constants";

        /**
         * Alarm manager specific settings.
         * This is encoded as a key=value list, separated by commas. Ex:
         *
         * "min_futurity=5000,allow_while_idle_short_time=4500"
         *
         * The following keys are supported:
         *
         * <pre>
         * min_futurity                         (long)
         * min_interval                         (long)
         * allow_while_idle_short_time          (long)
         * allow_while_idle_long_time           (long)
         * allow_while_idle_whitelist_duration  (long)
         * </pre>
         *
         * <p>
         * Type: string
         *
         * @unknown 
         * @see com.android.server.AlarmManagerService.Constants
         */
        public static final java.lang.String ALARM_MANAGER_CONSTANTS = "alarm_manager_constants";

        /**
         * Job scheduler specific settings.
         * This is encoded as a key=value list, separated by commas. Ex:
         *
         * "min_ready_jobs_count=2,moderate_use_factor=.5"
         *
         * The following keys are supported:
         *
         * <pre>
         * min_idle_count                       (int)
         * min_charging_count                   (int)
         * min_connectivity_count               (int)
         * min_content_count                    (int)
         * min_ready_jobs_count                 (int)
         * heavy_use_factor                     (float)
         * moderate_use_factor                  (float)
         * fg_job_count                         (int)
         * bg_normal_job_count                  (int)
         * bg_moderate_job_count                (int)
         * bg_low_job_count                     (int)
         * bg_critical_job_count                (int)
         * </pre>
         *
         * <p>
         * Type: string
         *
         * @unknown 
         * @see com.android.server.job.JobSchedulerService.Constants
         */
        public static final java.lang.String JOB_SCHEDULER_CONSTANTS = "job_scheduler_constants";

        /**
         * ShortcutManager specific settings.
         * This is encoded as a key=value list, separated by commas. Ex:
         *
         * "reset_interval_sec=86400,max_updates_per_interval=1"
         *
         * The following keys are supported:
         *
         * <pre>
         * reset_interval_sec              (long)
         * max_updates_per_interval        (int)
         * max_icon_dimension_dp           (int, DP)
         * max_icon_dimension_dp_lowram    (int, DP)
         * max_shortcuts                   (int)
         * icon_quality                    (int, 0-100)
         * icon_format                     (String)
         * </pre>
         *
         * <p>
         * Type: string
         *
         * @unknown 
         * @see com.android.server.pm.ShortcutService.ConfigConstants
         */
        public static final java.lang.String SHORTCUT_MANAGER_CONSTANTS = "shortcut_manager_constants";

        /**
         * Get the key that retrieves a bluetooth headset's priority.
         *
         * @unknown 
         */
        public static final java.lang.String getBluetoothHeadsetPriorityKey(java.lang.String address) {
            return android.provider.Settings.Global.BLUETOOTH_HEADSET_PRIORITY_PREFIX + address.toUpperCase(java.util.Locale.ROOT);
        }

        /**
         * Get the key that retrieves a bluetooth a2dp sink's priority.
         *
         * @unknown 
         */
        public static final java.lang.String getBluetoothA2dpSinkPriorityKey(java.lang.String address) {
            return android.provider.Settings.Global.BLUETOOTH_A2DP_SINK_PRIORITY_PREFIX + address.toUpperCase(java.util.Locale.ROOT);
        }

        /**
         * Get the key that retrieves a bluetooth a2dp src's priority.
         *
         * @unknown 
         */
        public static final java.lang.String getBluetoothA2dpSrcPriorityKey(java.lang.String address) {
            return android.provider.Settings.Global.BLUETOOTH_A2DP_SRC_PRIORITY_PREFIX + address.toUpperCase(java.util.Locale.ROOT);
        }

        /**
         * Get the key that retrieves a bluetooth Input Device's priority.
         *
         * @unknown 
         */
        public static final java.lang.String getBluetoothInputDevicePriorityKey(java.lang.String address) {
            return android.provider.Settings.Global.BLUETOOTH_INPUT_DEVICE_PRIORITY_PREFIX + address.toUpperCase(java.util.Locale.ROOT);
        }

        /**
         * Get the key that retrieves a bluetooth map priority.
         *
         * @unknown 
         */
        public static final java.lang.String getBluetoothMapPriorityKey(java.lang.String address) {
            return android.provider.Settings.Global.BLUETOOTH_MAP_PRIORITY_PREFIX + address.toUpperCase(java.util.Locale.ROOT);
        }

        /**
         * Get the key that retrieves a bluetooth pbap client priority.
         *
         * @unknown 
         */
        public static final java.lang.String getBluetoothPbapClientPriorityKey(java.lang.String address) {
            return android.provider.Settings.Global.BLUETOOTH_PBAP_CLIENT_PRIORITY_PREFIX + address.toUpperCase(java.util.Locale.ROOT);
        }

        /**
         * Get the key that retrieves a bluetooth map priority.
         *
         * @unknown 
         */
        public static final java.lang.String getBluetoothSapPriorityKey(java.lang.String address) {
            return android.provider.Settings.Global.BLUETOOTH_SAP_PRIORITY_PREFIX + address.toUpperCase(java.util.Locale.ROOT);
        }

        /**
         * Scaling factor for normal window animations. Setting to 0 will
         * disable window animations.
         */
        public static final java.lang.String WINDOW_ANIMATION_SCALE = "window_animation_scale";

        /**
         * Scaling factor for activity transition animations. Setting to 0 will
         * disable window animations.
         */
        public static final java.lang.String TRANSITION_ANIMATION_SCALE = "transition_animation_scale";

        /**
         * Scaling factor for Animator-based animations. This affects both the
         * start delay and duration of all such animations. Setting to 0 will
         * cause animations to end immediately. The default value is 1.
         */
        public static final java.lang.String ANIMATOR_DURATION_SCALE = "animator_duration_scale";

        /**
         * Scaling factor for normal window animations. Setting to 0 will
         * disable window animations.
         *
         * @unknown 
         */
        public static final java.lang.String FANCY_IME_ANIMATIONS = "fancy_ime_animations";

        /**
         * If 0, the compatibility mode is off for all applications.
         * If 1, older applications run under compatibility mode.
         * TODO: remove this settings before code freeze (bug/1907571)
         *
         * @unknown 
         */
        public static final java.lang.String COMPATIBILITY_MODE = "compatibility_mode";

        /**
         * CDMA only settings
         * Emergency Tone  0 = Off
         *                 1 = Alert
         *                 2 = Vibrate
         *
         * @unknown 
         */
        public static final java.lang.String EMERGENCY_TONE = "emergency_tone";

        /**
         * CDMA only settings
         * Whether the auto retry is enabled. The value is
         * boolean (1 or 0).
         *
         * @unknown 
         */
        public static final java.lang.String CALL_AUTO_RETRY = "call_auto_retry";

        /**
         * A setting that can be read whether the emergency affordance is currently needed.
         * The value is a boolean (1 or 0).
         *
         * @unknown 
         */
        public static final java.lang.String EMERGENCY_AFFORDANCE_NEEDED = "emergency_affordance_needed";

        /**
         * See RIL_PreferredNetworkType in ril.h
         *
         * @unknown 
         */
        public static final java.lang.String PREFERRED_NETWORK_MODE = "preferred_network_mode";

        /**
         * Name of an application package to be debugged.
         */
        public static final java.lang.String DEBUG_APP = "debug_app";

        /**
         * If 1, when launching DEBUG_APP it will wait for the debugger before
         * starting user code.  If 0, it will run normally.
         */
        public static final java.lang.String WAIT_FOR_DEBUGGER = "wait_for_debugger";

        /**
         * Control whether the process CPU usage meter should be shown.
         *
         * @deprecated This functionality is no longer available as of
        {@link android.os.Build.VERSION_CODES#N_MR1}.
         */
        @java.lang.Deprecated
        public static final java.lang.String SHOW_PROCESSES = "show_processes";

        /**
         * If 1 low power mode is enabled.
         *
         * @unknown 
         */
        public static final java.lang.String LOW_POWER_MODE = "low_power";

        /**
         * Battery level [1-99] at which low power mode automatically turns on.
         * If 0, it will not automatically turn on.
         *
         * @unknown 
         */
        public static final java.lang.String LOW_POWER_MODE_TRIGGER_LEVEL = "low_power_trigger_level";

        /**
         * If not 0, the activity manager will aggressively finish activities and
         * processes as soon as they are no longer needed.  If 0, the normal
         * extended lifetime is used.
         */
        public static final java.lang.String ALWAYS_FINISH_ACTIVITIES = "always_finish_activities";

        /**
         *
         *
         * @unknown If not 0, the activity manager will implement a looser version of background
        check that is more compatible with existing apps.
         */
        public static final java.lang.String LENIENT_BACKGROUND_CHECK = "lenient_background_check";

        /**
         * Use Dock audio output for media:
         *      0 = disabled
         *      1 = enabled
         *
         * @unknown 
         */
        public static final java.lang.String DOCK_AUDIO_MEDIA_ENABLED = "dock_audio_media_enabled";

        /**
         * The surround sound formats AC3, DTS or IEC61937 are
         * available for use if they are detected.
         * This is the default mode.
         *
         * Note that AUTO is equivalent to ALWAYS for Android TVs and other
         * devices that have an S/PDIF output. This is because S/PDIF
         * is unidirectional and the TV cannot know if a decoder is
         * connected. So it assumes they are always available.
         *
         * @unknown 
         */
        public static final int ENCODED_SURROUND_OUTPUT_AUTO = 0;

        /**
         * AC3, DTS or IEC61937 are NEVER available, even if they
         * are detected by the hardware. Those formats will not be
         * reported.
         *
         * An example use case would be an AVR reports that it is capable of
         * surround sound decoding but is broken. If NEVER is chosen
         * then apps must use PCM output instead of encoded output.
         *
         * @unknown 
         */
        public static final int ENCODED_SURROUND_OUTPUT_NEVER = 1;

        /**
         * AC3, DTS or IEC61937 are ALWAYS available, even if they
         * are not detected by the hardware. Those formats will be
         * reported as part of the HDMI output capability. Applications
         * are then free to use either PCM or encoded output.
         *
         * An example use case would be a when TV was connected over
         * TOS-link to an AVR. But the TV could not see it because TOS-link
         * is unidirectional.
         *
         * @unknown 
         */
        public static final int ENCODED_SURROUND_OUTPUT_ALWAYS = 2;

        /**
         * Set to ENCODED_SURROUND_OUTPUT_AUTO,
         * ENCODED_SURROUND_OUTPUT_NEVER or
         * ENCODED_SURROUND_OUTPUT_ALWAYS
         *
         * @unknown 
         */
        public static final java.lang.String ENCODED_SURROUND_OUTPUT = "encoded_surround_output";

        /**
         * Persisted safe headphone volume management state by AudioService
         *
         * @unknown 
         */
        public static final java.lang.String AUDIO_SAFE_VOLUME_STATE = "audio_safe_volume_state";

        /**
         * URL for tzinfo (time zone) updates
         *
         * @unknown 
         */
        public static final java.lang.String TZINFO_UPDATE_CONTENT_URL = "tzinfo_content_url";

        /**
         * URL for tzinfo (time zone) update metadata
         *
         * @unknown 
         */
        public static final java.lang.String TZINFO_UPDATE_METADATA_URL = "tzinfo_metadata_url";

        /**
         * URL for selinux (mandatory access control) updates
         *
         * @unknown 
         */
        public static final java.lang.String SELINUX_UPDATE_CONTENT_URL = "selinux_content_url";

        /**
         * URL for selinux (mandatory access control) update metadata
         *
         * @unknown 
         */
        public static final java.lang.String SELINUX_UPDATE_METADATA_URL = "selinux_metadata_url";

        /**
         * URL for sms short code updates
         *
         * @unknown 
         */
        public static final java.lang.String SMS_SHORT_CODES_UPDATE_CONTENT_URL = "sms_short_codes_content_url";

        /**
         * URL for sms short code update metadata
         *
         * @unknown 
         */
        public static final java.lang.String SMS_SHORT_CODES_UPDATE_METADATA_URL = "sms_short_codes_metadata_url";

        /**
         * URL for apn_db updates
         *
         * @unknown 
         */
        public static final java.lang.String APN_DB_UPDATE_CONTENT_URL = "apn_db_content_url";

        /**
         * URL for apn_db update metadata
         *
         * @unknown 
         */
        public static final java.lang.String APN_DB_UPDATE_METADATA_URL = "apn_db_metadata_url";

        /**
         * URL for cert pinlist updates
         *
         * @unknown 
         */
        public static final java.lang.String CERT_PIN_UPDATE_CONTENT_URL = "cert_pin_content_url";

        /**
         * URL for cert pinlist updates
         *
         * @unknown 
         */
        public static final java.lang.String CERT_PIN_UPDATE_METADATA_URL = "cert_pin_metadata_url";

        /**
         * URL for intent firewall updates
         *
         * @unknown 
         */
        public static final java.lang.String INTENT_FIREWALL_UPDATE_CONTENT_URL = "intent_firewall_content_url";

        /**
         * URL for intent firewall update metadata
         *
         * @unknown 
         */
        public static final java.lang.String INTENT_FIREWALL_UPDATE_METADATA_URL = "intent_firewall_metadata_url";

        /**
         * SELinux enforcement status. If 0, permissive; if 1, enforcing.
         *
         * @unknown 
         */
        public static final java.lang.String SELINUX_STATUS = "selinux_status";

        /**
         * Developer setting to force RTL layout.
         *
         * @unknown 
         */
        public static final java.lang.String DEVELOPMENT_FORCE_RTL = "debug.force_rtl";

        /**
         * Milliseconds after screen-off after which low battery sounds will be silenced.
         *
         * If zero, battery sounds will always play.
         * Defaults to @integer/def_low_battery_sound_timeout in SettingsProvider.
         *
         * @unknown 
         */
        public static final java.lang.String LOW_BATTERY_SOUND_TIMEOUT = "low_battery_sound_timeout";

        /**
         * Milliseconds to wait before bouncing Wi-Fi after settings is restored. Note that after
         * the caller is done with this, they should call {@link ContentResolver#delete} to
         * clean up any value that they may have written.
         *
         * @unknown 
         */
        public static final java.lang.String WIFI_BOUNCE_DELAY_OVERRIDE_MS = "wifi_bounce_delay_override_ms";

        /**
         * Defines global runtime overrides to window policy.
         *
         * See {@link com.android.server.policy.PolicyControl} for value format.
         *
         * @unknown 
         */
        public static final java.lang.String POLICY_CONTROL = "policy_control";

        /**
         * Defines global zen mode.  ZEN_MODE_OFF, ZEN_MODE_IMPORTANT_INTERRUPTIONS,
         * or ZEN_MODE_NO_INTERRUPTIONS.
         *
         * @unknown 
         */
        public static final java.lang.String ZEN_MODE = "zen_mode";

        /**
         *
         *
         * @unknown 
         */
        public static final int ZEN_MODE_OFF = 0;

        /**
         *
         *
         * @unknown 
         */
        public static final int ZEN_MODE_IMPORTANT_INTERRUPTIONS = 1;

        /**
         *
         *
         * @unknown 
         */
        public static final int ZEN_MODE_NO_INTERRUPTIONS = 2;

        /**
         *
         *
         * @unknown 
         */
        public static final int ZEN_MODE_ALARMS = 3;

        /**
         *
         *
         * @unknown 
         */
        public static java.lang.String zenModeToString(int mode) {
            if (mode == android.provider.Settings.Global.ZEN_MODE_IMPORTANT_INTERRUPTIONS)
                return "ZEN_MODE_IMPORTANT_INTERRUPTIONS";

            if (mode == android.provider.Settings.Global.ZEN_MODE_ALARMS)
                return "ZEN_MODE_ALARMS";

            if (mode == android.provider.Settings.Global.ZEN_MODE_NO_INTERRUPTIONS)
                return "ZEN_MODE_NO_INTERRUPTIONS";

            return "ZEN_MODE_OFF";
        }

        /**
         *
         *
         * @unknown 
         */
        public static boolean isValidZenMode(int value) {
            switch (value) {
                case android.provider.Settings.Global.ZEN_MODE_OFF :
                case android.provider.Settings.Global.ZEN_MODE_IMPORTANT_INTERRUPTIONS :
                case android.provider.Settings.Global.ZEN_MODE_ALARMS :
                case android.provider.Settings.Global.ZEN_MODE_NO_INTERRUPTIONS :
                    return true;
                default :
                    return false;
            }
        }

        /**
         * Value of the ringer before entering zen mode.
         *
         * @unknown 
         */
        public static final java.lang.String ZEN_MODE_RINGER_LEVEL = "zen_mode_ringer_level";

        /**
         * Opaque value, changes when persisted zen mode configuration changes.
         *
         * @unknown 
         */
        public static final java.lang.String ZEN_MODE_CONFIG_ETAG = "zen_mode_config_etag";

        /**
         * Defines global heads up toggle.  One of HEADS_UP_OFF, HEADS_UP_ON.
         *
         * @unknown 
         */
        public static final java.lang.String HEADS_UP_NOTIFICATIONS_ENABLED = "heads_up_notifications_enabled";

        /**
         *
         *
         * @unknown 
         */
        public static final int HEADS_UP_OFF = 0;

        /**
         *
         *
         * @unknown 
         */
        public static final int HEADS_UP_ON = 1;

        /**
         * The name of the device
         */
        public static final java.lang.String DEVICE_NAME = "device_name";

        /**
         * Whether the NetworkScoringService has been first initialized.
         * <p>
         * Type: int (0 for false, 1 for true)
         *
         * @unknown 
         */
        public static final java.lang.String NETWORK_SCORING_PROVISIONED = "network_scoring_provisioned";

        /**
         * Whether the user wants to be prompted for password to decrypt the device on boot.
         * This only matters if the storage is encrypted.
         * <p>
         * Type: int (0 for false, 1 for true)
         *
         * @unknown 
         */
        public static final java.lang.String REQUIRE_PASSWORD_TO_DECRYPT = "require_password_to_decrypt";

        /**
         * Whether the Volte is enabled
         * <p>
         * Type: int (0 for false, 1 for true)
         *
         * @unknown 
         */
        public static final java.lang.String ENHANCED_4G_MODE_ENABLED = "volte_vt_enabled";

        /**
         * Whether VT (Video Telephony over IMS) is enabled
         * <p>
         * Type: int (0 for false, 1 for true)
         *
         * @unknown 
         */
        public static final java.lang.String VT_IMS_ENABLED = "vt_ims_enabled";

        /**
         * Whether WFC is enabled
         * <p>
         * Type: int (0 for false, 1 for true)
         *
         * @unknown 
         */
        public static final java.lang.String WFC_IMS_ENABLED = "wfc_ims_enabled";

        /**
         * WFC mode on home/non-roaming network.
         * <p>
         * Type: int - 2=Wi-Fi preferred, 1=Cellular preferred, 0=Wi-Fi only
         *
         * @unknown 
         */
        public static final java.lang.String WFC_IMS_MODE = "wfc_ims_mode";

        /**
         * WFC mode on roaming network.
         * <p>
         * Type: int - see {@link WFC_IMS_MODE} for values
         *
         * @unknown 
         */
        public static final java.lang.String WFC_IMS_ROAMING_MODE = "wfc_ims_roaming_mode";

        /**
         * Whether WFC roaming is enabled
         * <p>
         * Type: int (0 for false, 1 for true)
         *
         * @unknown 
         */
        public static final java.lang.String WFC_IMS_ROAMING_ENABLED = "wfc_ims_roaming_enabled";

        /**
         * Whether user can enable/disable LTE as a preferred network. A carrier might control
         * this via gservices, OMA-DM, carrier app, etc.
         * <p>
         * Type: int (0 for false, 1 for true)
         *
         * @unknown 
         */
        public static final java.lang.String LTE_SERVICE_FORCED = "lte_service_forced";

        /**
         * Ephemeral app cookie max size in bytes.
         * <p>
         * Type: int
         *
         * @unknown 
         */
        public static final java.lang.String EPHEMERAL_COOKIE_MAX_SIZE_BYTES = "ephemeral_cookie_max_size_bytes";

        /**
         * Toggle to enable/disable the entire ephemeral feature. By default, ephemeral is
         * enabled. Set to zero to disable.
         * <p>
         * Type: int (0 for false, 1 for true)
         *
         * @unknown 
         */
        public static final java.lang.String ENABLE_EPHEMERAL_FEATURE = "enable_ephemeral_feature";

        /**
         * A mask applied to the ephemeral hash to generate the hash prefix.
         * <p>
         * Type: int
         *
         * @unknown 
         */
        public static final java.lang.String EPHEMERAL_HASH_PREFIX_MASK = "ephemeral_hash_prefix_mask";

        /**
         * Number of hash prefixes to send during ephemeral resolution.
         * <p>
         * Type: int
         *
         * @unknown 
         */
        public static final java.lang.String EPHEMERAL_HASH_PREFIX_COUNT = "ephemeral_hash_prefix_count";

        /**
         * The duration for caching uninstalled ephemeral apps.
         * <p>
         * Type: long
         *
         * @unknown 
         */
        public static final java.lang.String UNINSTALLED_EPHEMERAL_APP_CACHE_DURATION_MILLIS = "uninstalled_ephemeral_app_cache_duration_millis";

        /**
         * Allows switching users when system user is locked.
         * <p>
         * Type: int
         *
         * @unknown 
         */
        public static final java.lang.String ALLOW_USER_SWITCHING_WHEN_SYSTEM_USER_LOCKED = "allow_user_switching_when_system_user_locked";

        /**
         * Boot count since the device starts running APK level 24.
         * <p>
         * Type: int
         */
        public static final java.lang.String BOOT_COUNT = "boot_count";

        /**
         * Whether the safe boot is disallowed.
         *
         * <p>This setting should have the identical value as the corresponding user restriction.
         * The purpose of the setting is to make the restriction available in early boot stages
         * before the user restrictions are loaded.
         *
         * @unknown 
         */
        public static final java.lang.String SAFE_BOOT_DISALLOWED = "safe_boot_disallowed";

        /**
         * Whether this device is currently in retail demo mode. If true, device
         * usage is severely limited.
         * <p>
         * Type: int (0 for false, 1 for true)
         *
         * @unknown 
         */
        public static final java.lang.String DEVICE_DEMO_MODE = "device_demo_mode";

        /**
         * Retail mode specific settings. This is encoded as a key=value list, separated by commas.
         * Ex: "user_inactivity_timeout_ms=30000,warning_dialog_timeout_ms=10000". The following
         * keys are supported:
         *
         * <pre>
         * user_inactivity_timeout_ms  (long)
         * warning_dialog_timeout_ms   (long)
         * </pre>
         * <p>
         * Type: string
         *
         * @unknown 
         */
        public static final java.lang.String RETAIL_DEMO_MODE_CONSTANTS = "retail_demo_mode_constants";

        /**
         * The reason for the settings database being downgraded. This is only for
         * troubleshooting purposes and its value should not be interpreted in any way.
         *
         * Type: string
         *
         * @unknown 
         */
        public static final java.lang.String DATABASE_DOWNGRADE_REASON = "database_downgrade_reason";

        /**
         * Settings to backup. This is here so that it's in the same place as the settings
         * keys and easy to update.
         *
         * These keys may be mentioned in the SETTINGS_TO_BACKUP arrays in System
         * and Secure as well.  This is because those tables drive both backup and
         * restore, and restore needs to properly whitelist keys that used to live
         * in those namespaces.  The keys will only actually be backed up / restored
         * if they are also mentioned in this table (Global.SETTINGS_TO_BACKUP).
         *
         * NOTE: Settings are backed up and restored in the order they appear
         *       in this array. If you have one setting depending on another,
         *       make sure that they are ordered appropriately.
         *
         * @unknown 
         */
        public static final java.lang.String[] SETTINGS_TO_BACKUP = new java.lang.String[]{ android.provider.Settings.Global.BUGREPORT_IN_POWER_MENU, android.provider.Settings.Global.STAY_ON_WHILE_PLUGGED_IN, android.provider.Settings.Global.AUTO_TIME, android.provider.Settings.Global.AUTO_TIME_ZONE, android.provider.Settings.Global.POWER_SOUNDS_ENABLED, android.provider.Settings.Global.DOCK_SOUNDS_ENABLED, android.provider.Settings.Global.CHARGING_SOUNDS_ENABLED, android.provider.Settings.Global.USB_MASS_STORAGE_ENABLED, android.provider.Settings.Global.ENABLE_ACCESSIBILITY_GLOBAL_GESTURE_ENABLED, android.provider.Settings.Global.WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON, android.provider.Settings.Global.WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY, android.provider.Settings.Global.WIFI_WATCHDOG_POOR_NETWORK_TEST_ENABLED, android.provider.Settings.Global.WIFI_NUM_OPEN_NETWORKS_KEPT, android.provider.Settings.Global.EMERGENCY_TONE, android.provider.Settings.Global.CALL_AUTO_RETRY, android.provider.Settings.Global.DOCK_AUDIO_MEDIA_ENABLED, android.provider.Settings.Global.ENCODED_SURROUND_OUTPUT, android.provider.Settings.Global.LOW_POWER_MODE_TRIGGER_LEVEL };

        // Populated lazily, guarded by class object:
        private static android.provider.Settings.NameValueCache sNameValueCache = new android.provider.Settings.NameValueCache(android.provider.Settings.Global.CONTENT_URI, android.provider.Settings.CALL_METHOD_GET_GLOBAL, android.provider.Settings.CALL_METHOD_PUT_GLOBAL);

        // Certain settings have been moved from global to the per-user secure namespace
        private static final java.util.HashSet<java.lang.String> MOVED_TO_SECURE;

        static {
            MOVED_TO_SECURE = new java.util.HashSet<java.lang.String>(1);
            android.provider.Settings.Global.MOVED_TO_SECURE.add(android.provider.Settings.Global.INSTALL_NON_MARKET_APPS);
        }

        /**
         *
         *
         * @unknown 
         */
        public static void getMovedToSecureSettings(java.util.Set<java.lang.String> outKeySet) {
            outKeySet.addAll(android.provider.Settings.Global.MOVED_TO_SECURE);
        }

        /**
         * Look up a name in the database.
         *
         * @param resolver
         * 		to access the database with
         * @param name
         * 		to look up in the table
         * @return the corresponding value, or null if not present
         */
        public static java.lang.String getString(android.content.ContentResolver resolver, java.lang.String name) {
            return android.provider.Settings.Global.getStringForUser(resolver, name, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static java.lang.String getStringForUser(android.content.ContentResolver resolver, java.lang.String name, int userHandle) {
            if (android.provider.Settings.Global.MOVED_TO_SECURE.contains(name)) {
                android.util.Log.w(android.provider.Settings.TAG, (("Setting " + name) + " has moved from android.provider.Settings.Global") + " to android.provider.Settings.Secure, returning read-only value.");
                return android.provider.Settings.Secure.getStringForUser(resolver, name, userHandle);
            }
            return android.provider.Settings.Global.sNameValueCache.getStringForUser(resolver, name, userHandle);
        }

        /**
         * Store a name/value pair into the database.
         *
         * @param resolver
         * 		to access the database with
         * @param name
         * 		to store
         * @param value
         * 		to associate with the name
         * @return true if the value was set, false on database errors
         */
        public static boolean putString(android.content.ContentResolver resolver, java.lang.String name, java.lang.String value) {
            return android.provider.Settings.Global.putStringForUser(resolver, name, value, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public static boolean putStringForUser(android.content.ContentResolver resolver, java.lang.String name, java.lang.String value, int userHandle) {
            if (android.provider.Settings.LOCAL_LOGV) {
                android.util.Log.v(android.provider.Settings.TAG, (((("Global.putString(name=" + name) + ", value=") + value) + " for ") + userHandle);
            }
            // Global and Secure have the same access policy so we can forward writes
            if (android.provider.Settings.Global.MOVED_TO_SECURE.contains(name)) {
                android.util.Log.w(android.provider.Settings.TAG, (("Setting " + name) + " has moved from android.provider.Settings.Global") + " to android.provider.Settings.Secure, value is unchanged.");
                return android.provider.Settings.Secure.putStringForUser(resolver, name, value, userHandle);
            }
            return android.provider.Settings.Global.sNameValueCache.putStringForUser(resolver, name, value, userHandle);
        }

        /**
         * Construct the content URI for a particular name/value pair,
         * useful for monitoring changes with a ContentObserver.
         *
         * @param name
         * 		to look up in the table
         * @return the corresponding content URI, or null if not present
         */
        public static android.net.Uri getUriFor(java.lang.String name) {
            return android.provider.Settings.NameValueTable.getUriFor(android.provider.Settings.Global.CONTENT_URI, name);
        }

        /**
         * Convenience function for retrieving a single secure settings value
         * as an integer.  Note that internally setting values are always
         * stored as strings; this function converts the string to an integer
         * for you.  The default value will be returned if the setting is
         * not defined or not an integer.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @param def
         * 		Value to return if the setting is not defined.
         * @return The setting's current value, or 'def' if it is not defined
        or not a valid integer.
         */
        public static int getInt(android.content.ContentResolver cr, java.lang.String name, int def) {
            java.lang.String v = android.provider.Settings.Global.getString(cr, name);
            try {
                return v != null ? java.lang.Integer.parseInt(v) : def;
            } catch (java.lang.NumberFormatException e) {
                return def;
            }
        }

        /**
         * Convenience function for retrieving a single secure settings value
         * as an integer.  Note that internally setting values are always
         * stored as strings; this function converts the string to an integer
         * for you.
         * <p>
         * This version does not take a default value.  If the setting has not
         * been set, or the string value is not a number,
         * it throws {@link SettingNotFoundException}.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @throws SettingNotFoundException
         * 		Thrown if a setting by the given
         * 		name can't be found or the setting value is not an integer.
         * @return The setting's current value.
         */
        public static int getInt(android.content.ContentResolver cr, java.lang.String name) throws android.provider.Settings.SettingNotFoundException {
            java.lang.String v = android.provider.Settings.Global.getString(cr, name);
            try {
                return java.lang.Integer.parseInt(v);
            } catch (java.lang.NumberFormatException e) {
                throw new android.provider.Settings.SettingNotFoundException(name);
            }
        }

        /**
         * Convenience function for updating a single settings value as an
         * integer. This will either create a new entry in the table if the
         * given name does not exist, or modify the value of the existing row
         * with that name.  Note that internally setting values are always
         * stored as strings, so this function converts the given value to a
         * string before storing it.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to modify.
         * @param value
         * 		The new value for the setting.
         * @return true if the value was set, false on database errors
         */
        public static boolean putInt(android.content.ContentResolver cr, java.lang.String name, int value) {
            return android.provider.Settings.Global.putString(cr, name, java.lang.Integer.toString(value));
        }

        /**
         * Convenience function for retrieving a single secure settings value
         * as a {@code long}.  Note that internally setting values are always
         * stored as strings; this function converts the string to a {@code long}
         * for you.  The default value will be returned if the setting is
         * not defined or not a {@code long}.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @param def
         * 		Value to return if the setting is not defined.
         * @return The setting's current value, or 'def' if it is not defined
        or not a valid {@code long}.
         */
        public static long getLong(android.content.ContentResolver cr, java.lang.String name, long def) {
            java.lang.String valString = android.provider.Settings.Global.getString(cr, name);
            long value;
            try {
                value = (valString != null) ? java.lang.Long.parseLong(valString) : def;
            } catch (java.lang.NumberFormatException e) {
                value = def;
            }
            return value;
        }

        /**
         * Convenience function for retrieving a single secure settings value
         * as a {@code long}.  Note that internally setting values are always
         * stored as strings; this function converts the string to a {@code long}
         * for you.
         * <p>
         * This version does not take a default value.  If the setting has not
         * been set, or the string value is not a number,
         * it throws {@link SettingNotFoundException}.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @return The setting's current value.
         * @throws SettingNotFoundException
         * 		Thrown if a setting by the given
         * 		name can't be found or the setting value is not an integer.
         */
        public static long getLong(android.content.ContentResolver cr, java.lang.String name) throws android.provider.Settings.SettingNotFoundException {
            java.lang.String valString = android.provider.Settings.Global.getString(cr, name);
            try {
                return java.lang.Long.parseLong(valString);
            } catch (java.lang.NumberFormatException e) {
                throw new android.provider.Settings.SettingNotFoundException(name);
            }
        }

        /**
         * Convenience function for updating a secure settings value as a long
         * integer. This will either create a new entry in the table if the
         * given name does not exist, or modify the value of the existing row
         * with that name.  Note that internally setting values are always
         * stored as strings, so this function converts the given value to a
         * string before storing it.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to modify.
         * @param value
         * 		The new value for the setting.
         * @return true if the value was set, false on database errors
         */
        public static boolean putLong(android.content.ContentResolver cr, java.lang.String name, long value) {
            return android.provider.Settings.Global.putString(cr, name, java.lang.Long.toString(value));
        }

        /**
         * Convenience function for retrieving a single secure settings value
         * as a floating point number.  Note that internally setting values are
         * always stored as strings; this function converts the string to an
         * float for you. The default value will be returned if the setting
         * is not defined or not a valid float.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @param def
         * 		Value to return if the setting is not defined.
         * @return The setting's current value, or 'def' if it is not defined
        or not a valid float.
         */
        public static float getFloat(android.content.ContentResolver cr, java.lang.String name, float def) {
            java.lang.String v = android.provider.Settings.Global.getString(cr, name);
            try {
                return v != null ? java.lang.Float.parseFloat(v) : def;
            } catch (java.lang.NumberFormatException e) {
                return def;
            }
        }

        /**
         * Convenience function for retrieving a single secure settings value
         * as a float.  Note that internally setting values are always
         * stored as strings; this function converts the string to a float
         * for you.
         * <p>
         * This version does not take a default value.  If the setting has not
         * been set, or the string value is not a number,
         * it throws {@link SettingNotFoundException}.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to retrieve.
         * @throws SettingNotFoundException
         * 		Thrown if a setting by the given
         * 		name can't be found or the setting value is not a float.
         * @return The setting's current value.
         */
        public static float getFloat(android.content.ContentResolver cr, java.lang.String name) throws android.provider.Settings.SettingNotFoundException {
            java.lang.String v = android.provider.Settings.Global.getString(cr, name);
            if (v == null) {
                throw new android.provider.Settings.SettingNotFoundException(name);
            }
            try {
                return java.lang.Float.parseFloat(v);
            } catch (java.lang.NumberFormatException e) {
                throw new android.provider.Settings.SettingNotFoundException(name);
            }
        }

        /**
         * Convenience function for updating a single settings value as a
         * floating point number. This will either create a new entry in the
         * table if the given name does not exist, or modify the value of the
         * existing row with that name.  Note that internally setting values
         * are always stored as strings, so this function converts the given
         * value to a string before storing it.
         *
         * @param cr
         * 		The ContentResolver to access.
         * @param name
         * 		The name of the setting to modify.
         * @param value
         * 		The new value for the setting.
         * @return true if the value was set, false on database errors
         */
        public static boolean putFloat(android.content.ContentResolver cr, java.lang.String name, float value) {
            return android.provider.Settings.Global.putString(cr, name, java.lang.Float.toString(value));
        }

        /**
         * Subscription to be used for voice call on a multi sim device. The supported values
         * are 0 = SUB1, 1 = SUB2 and etc.
         *
         * @unknown 
         */
        public static final java.lang.String MULTI_SIM_VOICE_CALL_SUBSCRIPTION = "multi_sim_voice_call";

        /**
         * Used to provide option to user to select subscription during dial.
         * The supported values are 0 = disable or 1 = enable prompt.
         *
         * @unknown 
         */
        public static final java.lang.String MULTI_SIM_VOICE_PROMPT = "multi_sim_voice_prompt";

        /**
         * Subscription to be used for data call on a multi sim device. The supported values
         * are 0 = SUB1, 1 = SUB2 and etc.
         *
         * @unknown 
         */
        public static final java.lang.String MULTI_SIM_DATA_CALL_SUBSCRIPTION = "multi_sim_data_call";

        /**
         * Subscription to be used for SMS on a multi sim device. The supported values
         * are 0 = SUB1, 1 = SUB2 and etc.
         *
         * @unknown 
         */
        public static final java.lang.String MULTI_SIM_SMS_SUBSCRIPTION = "multi_sim_sms";

        /**
         * Used to provide option to user to select subscription during send SMS.
         * The value 1 - enable, 0 - disable
         *
         * @unknown 
         */
        public static final java.lang.String MULTI_SIM_SMS_PROMPT = "multi_sim_sms_prompt";

        /**
         * User preferred subscriptions setting.
         * This holds the details of the user selected subscription from the card and
         * the activation status. Each settings string have the coma separated values
         * iccId,appType,appId,activationStatus,3gppIndex,3gpp2Index
         *
         * @unknown 
         */
        public static final java.lang.String[] MULTI_SIM_USER_PREFERRED_SUBS = new java.lang.String[]{ "user_preferred_sub1", "user_preferred_sub2", "user_preferred_sub3" };

        /**
         * Whether to enable new contacts aggregator or not.
         * The value 1 - enable, 0 - disable
         *
         * @unknown 
         */
        public static final java.lang.String NEW_CONTACT_AGGREGATOR = "new_contact_aggregator";

        /**
         * Whether to enable contacts metadata syncing or not
         * The value 1 - enable, 0 - disable
         *
         * @unknown 
         */
        @java.lang.Deprecated
        public static final java.lang.String CONTACT_METADATA_SYNC = "contact_metadata_sync";

        /**
         * Whether to enable contacts metadata syncing or not
         * The value 1 - enable, 0 - disable
         */
        public static final java.lang.String CONTACT_METADATA_SYNC_ENABLED = "contact_metadata_sync_enabled";

        /**
         * Whether to enable cellular on boot.
         * The value 1 - enable, 0 - disable
         *
         * @unknown 
         */
        public static final java.lang.String ENABLE_CELLULAR_ON_BOOT = "enable_cellular_on_boot";

        /**
         * The maximum allowed notification enqueue rate in Hertz.
         *
         * Should be a float, and includes both posts and updates.
         *
         * @unknown 
         */
        public static final java.lang.String MAX_NOTIFICATION_ENQUEUE_RATE = "max_notification_enqueue_rate";

        /**
         * Whether cell is enabled/disabled
         *
         * @unknown 
         */
        public static final java.lang.String CELL_ON = "cell_on";
    }

    /**
     * User-defined bookmarks and shortcuts.  The target of each bookmark is an
     * Intent URL, allowing it to be either a web page or a particular
     * application activity.
     *
     * @unknown 
     */
    public static final class Bookmarks implements android.provider.BaseColumns {
        private static final java.lang.String TAG = "Bookmarks";

        /**
         * The content:// style URL for this table
         */
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse(("content://" + android.provider.Settings.AUTHORITY) + "/bookmarks");

        /**
         * The row ID.
         * <p>Type: INTEGER</p>
         */
        public static final java.lang.String ID = "_id";

        /**
         * Descriptive name of the bookmark that can be displayed to the user.
         * If this is empty, the title should be resolved at display time (use
         * {@link #getTitle(Context, Cursor)} any time you want to display the
         * title of a bookmark.)
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final java.lang.String TITLE = "title";

        /**
         * Arbitrary string (displayed to the user) that allows bookmarks to be
         * organized into categories.  There are some special names for
         * standard folders, which all start with '@'.  The label displayed for
         * the folder changes with the locale (via {@link #getLabelForFolder}) but
         * the folder name does not change so you can consistently query for
         * the folder regardless of the current locale.
         *
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String FOLDER = "folder";

        /**
         * The Intent URL of the bookmark, describing what it points to.  This
         * value is given to {@link android.content.Intent#getIntent} to create
         * an Intent that can be launched.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String INTENT = "intent";

        /**
         * Optional shortcut character associated with this bookmark.
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String SHORTCUT = "shortcut";

        /**
         * The order in which the bookmark should be displayed
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String ORDERING = "ordering";

        private static final java.lang.String[] sIntentProjection = new java.lang.String[]{ android.provider.Settings.Bookmarks.INTENT };

        private static final java.lang.String[] sShortcutProjection = new java.lang.String[]{ android.provider.Settings.Bookmarks.ID, android.provider.Settings.Bookmarks.SHORTCUT };

        private static final java.lang.String sShortcutSelection = android.provider.Settings.Bookmarks.SHORTCUT + "=?";

        /**
         * Convenience function to retrieve the bookmarked Intent for a
         * particular shortcut key.
         *
         * @param cr
         * 		The ContentResolver to query.
         * @param shortcut
         * 		The shortcut key.
         * @return Intent The bookmarked URL, or null if there is no bookmark
        matching the given shortcut.
         */
        public static android.content.Intent getIntentForShortcut(android.content.ContentResolver cr, char shortcut) {
            android.content.Intent intent = null;
            android.database.Cursor c = cr.query(android.provider.Settings.Bookmarks.CONTENT_URI, android.provider.Settings.Bookmarks.sIntentProjection, android.provider.Settings.Bookmarks.sShortcutSelection, new java.lang.String[]{ java.lang.String.valueOf(((int) (shortcut))) }, android.provider.Settings.Bookmarks.ORDERING);
            // Keep trying until we find a valid shortcut
            try {
                while ((intent == null) && c.moveToNext()) {
                    try {
                        java.lang.String intentURI = c.getString(c.getColumnIndexOrThrow(android.provider.Settings.Bookmarks.INTENT));
                        intent = android.content.Intent.parseUri(intentURI, 0);
                    } catch (java.net.URISyntaxException e) {
                        // The stored URL is bad...  ignore it.
                    } catch (java.lang.IllegalArgumentException e) {
                        // Column not found
                        android.util.Log.w(android.provider.Settings.Bookmarks.TAG, "Intent column not found", e);
                    }
                } 
            } finally {
                if (c != null)
                    c.close();

            }
            return intent;
        }

        /**
         * Add a new bookmark to the system.
         *
         * @param cr
         * 		The ContentResolver to query.
         * @param intent
         * 		The desired target of the bookmark.
         * @param title
         * 		Bookmark title that is shown to the user; null if none
         * 		or it should be resolved to the intent's title.
         * @param folder
         * 		Folder in which to place the bookmark; null if none.
         * @param shortcut
         * 		Shortcut that will invoke the bookmark; 0 if none. If
         * 		this is non-zero and there is an existing bookmark entry
         * 		with this same shortcut, then that existing shortcut is
         * 		cleared (the bookmark is not removed).
         * @return The unique content URL for the new bookmark entry.
         */
        public static android.net.Uri add(android.content.ContentResolver cr, android.content.Intent intent, java.lang.String title, java.lang.String folder, char shortcut, int ordering) {
            // If a shortcut is supplied, and it is already defined for
            // another bookmark, then remove the old definition.
            if (shortcut != 0) {
                cr.delete(android.provider.Settings.Bookmarks.CONTENT_URI, android.provider.Settings.Bookmarks.sShortcutSelection, new java.lang.String[]{ java.lang.String.valueOf(((int) (shortcut))) });
            }
            android.content.ContentValues values = new android.content.ContentValues();
            if (title != null)
                values.put(android.provider.Settings.Bookmarks.TITLE, title);

            if (folder != null)
                values.put(android.provider.Settings.Bookmarks.FOLDER, folder);

            values.put(android.provider.Settings.Bookmarks.INTENT, intent.toUri(0));
            if (shortcut != 0)
                values.put(android.provider.Settings.Bookmarks.SHORTCUT, ((int) (shortcut)));

            values.put(android.provider.Settings.Bookmarks.ORDERING, ordering);
            return cr.insert(android.provider.Settings.Bookmarks.CONTENT_URI, values);
        }

        /**
         * Return the folder name as it should be displayed to the user.  This
         * takes care of localizing special folders.
         *
         * @param r
         * 		Resources object for current locale; only need access to
         * 		system resources.
         * @param folder
         * 		The value found in the {@link #FOLDER} column.
         * @return CharSequence The label for this folder that should be shown
        to the user.
         */
        public static java.lang.CharSequence getLabelForFolder(android.content.res.Resources r, java.lang.String folder) {
            return folder;
        }

        /**
         * Return the title as it should be displayed to the user. This takes
         * care of localizing bookmarks that point to activities.
         *
         * @param context
         * 		A context.
         * @param cursor
         * 		A cursor pointing to the row whose title should be
         * 		returned. The cursor must contain at least the {@link #TITLE}
         * 		and {@link #INTENT} columns.
         * @return A title that is localized and can be displayed to the user,
        or the empty string if one could not be found.
         */
        public static java.lang.CharSequence getTitle(android.content.Context context, android.database.Cursor cursor) {
            int titleColumn = cursor.getColumnIndex(android.provider.Settings.Bookmarks.TITLE);
            int intentColumn = cursor.getColumnIndex(android.provider.Settings.Bookmarks.INTENT);
            if ((titleColumn == (-1)) || (intentColumn == (-1))) {
                throw new java.lang.IllegalArgumentException("The cursor must contain the TITLE and INTENT columns.");
            }
            java.lang.String title = cursor.getString(titleColumn);
            if (!android.text.TextUtils.isEmpty(title)) {
                return title;
            }
            java.lang.String intentUri = cursor.getString(intentColumn);
            if (android.text.TextUtils.isEmpty(intentUri)) {
                return "";
            }
            android.content.Intent intent;
            try {
                intent = android.content.Intent.parseUri(intentUri, 0);
            } catch (java.net.URISyntaxException e) {
                return "";
            }
            android.content.pm.PackageManager packageManager = context.getPackageManager();
            android.content.pm.ResolveInfo info = packageManager.resolveActivity(intent, 0);
            return info != null ? info.loadLabel(packageManager) : "";
        }
    }

    /**
     * Returns the device ID that we should use when connecting to the mobile gtalk server.
     * This is a string like "android-0x1242", where the hex string is the Android ID obtained
     * from the GoogleLoginService.
     *
     * @param androidId
     * 		The Android ID for this device.
     * @return The device ID that should be used when connecting to the mobile gtalk server.
     * @unknown 
     */
    public static java.lang.String getGTalkDeviceId(long androidId) {
        return "android-" + java.lang.Long.toHexString(androidId);
    }

    private static final java.lang.String[] PM_WRITE_SETTINGS = new java.lang.String[]{ android.Manifest.permission.WRITE_SETTINGS };

    private static final java.lang.String[] PM_CHANGE_NETWORK_STATE = new java.lang.String[]{ android.Manifest.permission.CHANGE_NETWORK_STATE, android.Manifest.permission.WRITE_SETTINGS };

    private static final java.lang.String[] PM_SYSTEM_ALERT_WINDOW = new java.lang.String[]{ android.Manifest.permission.SYSTEM_ALERT_WINDOW };

    /**
     * Performs a strict and comprehensive check of whether a calling package is allowed to
     * write/modify system settings, as the condition differs for pre-M, M+, and
     * privileged/preinstalled apps. If the provided uid does not match the
     * callingPackage, a negative result will be returned.
     *
     * @unknown 
     */
    public static boolean isCallingPackageAllowedToWriteSettings(android.content.Context context, int uid, java.lang.String callingPackage, boolean throwException) {
        return android.provider.Settings.isCallingPackageAllowedToPerformAppOpsProtectedOperation(context, uid, callingPackage, throwException, android.app.AppOpsManager.OP_WRITE_SETTINGS, android.provider.Settings.PM_WRITE_SETTINGS, false);
    }

    /**
     * Performs a strict and comprehensive check of whether a calling package is allowed to
     * write/modify system settings, as the condition differs for pre-M, M+, and
     * privileged/preinstalled apps. If the provided uid does not match the
     * callingPackage, a negative result will be returned. The caller is expected to have
     * the WRITE_SETTINGS permission declared.
     *
     * Note: if the check is successful, the operation of this app will be updated to the
     * current time.
     *
     * @unknown 
     */
    public static boolean checkAndNoteWriteSettingsOperation(android.content.Context context, int uid, java.lang.String callingPackage, boolean throwException) {
        return android.provider.Settings.isCallingPackageAllowedToPerformAppOpsProtectedOperation(context, uid, callingPackage, throwException, android.app.AppOpsManager.OP_WRITE_SETTINGS, android.provider.Settings.PM_WRITE_SETTINGS, true);
    }

    /**
     * Performs a strict and comprehensive check of whether a calling package is allowed to
     * change the state of network, as the condition differs for pre-M, M+, and
     * privileged/preinstalled apps. The caller is expected to have either the
     * CHANGE_NETWORK_STATE or the WRITE_SETTINGS permission declared. Either of these
     * permissions allow changing network state; WRITE_SETTINGS is a runtime permission and
     * can be revoked, but (except in M, excluding M MRs), CHANGE_NETWORK_STATE is a normal
     * permission and cannot be revoked. See http://b/23597341
     *
     * Note: if the check succeeds because the application holds WRITE_SETTINGS, the operation
     * of this app will be updated to the current time.
     *
     * @unknown 
     */
    public static boolean checkAndNoteChangeNetworkStateOperation(android.content.Context context, int uid, java.lang.String callingPackage, boolean throwException) {
        if (context.checkCallingOrSelfPermission(android.Manifest.permission.CHANGE_NETWORK_STATE) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return android.provider.Settings.isCallingPackageAllowedToPerformAppOpsProtectedOperation(context, uid, callingPackage, throwException, android.app.AppOpsManager.OP_WRITE_SETTINGS, android.provider.Settings.PM_CHANGE_NETWORK_STATE, true);
    }

    /**
     * Performs a strict and comprehensive check of whether a calling package is allowed to
     * draw on top of other apps, as the conditions differs for pre-M, M+, and
     * privileged/preinstalled apps. If the provided uid does not match the callingPackage,
     * a negative result will be returned.
     *
     * @unknown 
     */
    public static boolean isCallingPackageAllowedToDrawOverlays(android.content.Context context, int uid, java.lang.String callingPackage, boolean throwException) {
        return android.provider.Settings.isCallingPackageAllowedToPerformAppOpsProtectedOperation(context, uid, callingPackage, throwException, android.app.AppOpsManager.OP_SYSTEM_ALERT_WINDOW, android.provider.Settings.PM_SYSTEM_ALERT_WINDOW, false);
    }

    /**
     * Performs a strict and comprehensive check of whether a calling package is allowed to
     * draw on top of other apps, as the conditions differs for pre-M, M+, and
     * privileged/preinstalled apps. If the provided uid does not match the callingPackage,
     * a negative result will be returned.
     *
     * Note: if the check is successful, the operation of this app will be updated to the
     * current time.
     *
     * @unknown 
     */
    public static boolean checkAndNoteDrawOverlaysOperation(android.content.Context context, int uid, java.lang.String callingPackage, boolean throwException) {
        return android.provider.Settings.isCallingPackageAllowedToPerformAppOpsProtectedOperation(context, uid, callingPackage, throwException, android.app.AppOpsManager.OP_SYSTEM_ALERT_WINDOW, android.provider.Settings.PM_SYSTEM_ALERT_WINDOW, true);
    }

    /**
     * Helper method to perform a general and comprehensive check of whether an operation that is
     * protected by appops can be performed by a caller or not. e.g. OP_SYSTEM_ALERT_WINDOW and
     * OP_WRITE_SETTINGS
     *
     * @unknown 
     */
    public static boolean isCallingPackageAllowedToPerformAppOpsProtectedOperation(android.content.Context context, int uid, java.lang.String callingPackage, boolean throwException, int appOpsOpCode, java.lang.String[] permissions, boolean makeNote) {
        if (callingPackage == null) {
            return false;
        }
        android.app.AppOpsManager appOpsMgr = ((android.app.AppOpsManager) (context.getSystemService(android.content.Context.APP_OPS_SERVICE)));
        int mode = android.app.AppOpsManager.MODE_DEFAULT;
        if (makeNote) {
            mode = appOpsMgr.noteOpNoThrow(appOpsOpCode, uid, callingPackage);
        } else {
            mode = appOpsMgr.checkOpNoThrow(appOpsOpCode, uid, callingPackage);
        }
        switch (mode) {
            case android.app.AppOpsManager.MODE_ALLOWED :
                return true;
            case android.app.AppOpsManager.MODE_DEFAULT :
                // this is the default operating mode after an app's installation
                // In this case we will check all associated static permission to see
                // if it is granted during install time.
                for (java.lang.String permission : permissions) {
                    if (context.checkCallingOrSelfPermission(permission) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        // if either of the permissions are granted, we will allow it
                        return true;
                    }
                }
            default :
                // this is for all other cases trickled down here...
                if (!throwException) {
                    return false;
                }
        }
        // prepare string to throw SecurityException
        java.lang.StringBuilder exceptionMessage = new java.lang.StringBuilder();
        exceptionMessage.append(callingPackage);
        exceptionMessage.append(" was not granted ");
        if (permissions.length > 1) {
            exceptionMessage.append(" either of these permissions: ");
        } else {
            exceptionMessage.append(" this permission: ");
        }
        for (int i = 0; i < permissions.length; i++) {
            exceptionMessage.append(permissions[i]);
            exceptionMessage.append(i == (permissions.length - 1) ? "." : ", ");
        }
        throw new java.lang.SecurityException(exceptionMessage.toString());
    }

    /**
     * Retrieves a correponding package name for a given uid. It will query all
     * packages that are associated with the given uid, but it will return only
     * the zeroth result.
     * Note: If package could not be found, a null is returned.
     *
     * @unknown 
     */
    public static java.lang.String getPackageNameForUid(android.content.Context context, int uid) {
        java.lang.String[] packages = context.getPackageManager().getPackagesForUid(uid);
        if (packages == null) {
            return null;
        }
        return packages[0];
    }
}

