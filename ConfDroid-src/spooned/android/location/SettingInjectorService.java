/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.location;


/**
 * Dynamically specifies the enabled status of a preference injected into
 * the list of app settings displayed by the system settings app
 * <p/>
 * For use only by apps that are included in the system image, for preferences that affect multiple
 * apps. Location settings that apply only to one app should be shown within that app,
 * rather than in the system settings.
 * <p/>
 * To add a preference to the list, a subclass of {@link SettingInjectorService} must be declared in
 * the manifest as so:
 *
 * <pre>
 *     &lt;service android:name="com.example.android.injector.MyInjectorService" &gt;
 *         &lt;intent-filter&gt;
 *             &lt;action android:name="android.location.SettingInjectorService" /&gt;
 *         &lt;/intent-filter&gt;
 *
 *         &lt;meta-data
 *             android:name="android.location.SettingInjectorService"
 *             android:resource="@xml/my_injected_location_setting" /&gt;
 *     &lt;/service&gt;
 * </pre>
 * The resource file specifies the static data for the setting:
 * <pre>
 *     &lt;injected-location-setting xmlns:android="http://schemas.android.com/apk/res/android"
 *         android:title="@string/injected_setting_title"
 *         android:icon="@drawable/ic_acme_corp"
 *         android:settingsActivity="com.example.android.injector.MySettingActivity"
 *     /&gt;
 * </pre>
 * Here:
 * <ul>
 *     <li>title: The {@link android.preference.Preference#getTitle()} value. The title should make
 *     it clear which apps are affected by the setting, typically by including the name of the
 *     developer. For example, "Acme Corp. ads preferences." </li>
 *
 *     <li>icon: The {@link android.preference.Preference#getIcon()} value. Typically this will be a
 *     generic icon for the developer rather than the icon for an individual app.</li>
 *
 *     <li>settingsActivity: the activity which is launched to allow the user to modify the setting
 *     value.  The activity must be in the same package as the subclass of
 *     {@link SettingInjectorService}. The activity should use your own branding to help emphasize
 *     to the user that it is not part of the system settings.</li>
 * </ul>
 *
 * To ensure a good user experience, your {@link android.app.Application#onCreate()},
 * and {@link #onGetEnabled()} methods must all be fast. If either is slow,
 * it can delay the display of settings values for other apps as well. Note further that these
 * methods are called on your app's UI thread.
 * <p/>
 * For compactness, only one copy of a given setting should be injected. If each account has a
 * distinct value for the setting, then only {@code settingsActivity} should display the value for
 * each account.
 */
public abstract class SettingInjectorService extends android.app.Service {
    private static final java.lang.String TAG = "SettingInjectorService";

    /**
     * Intent action that must be declared in the manifest for the subclass. Used to start the
     * service to read the dynamic status for the setting.
     */
    public static final java.lang.String ACTION_SERVICE_INTENT = "android.location.SettingInjectorService";

    /**
     * Name of the meta-data tag used to specify the resource file that includes the settings
     * attributes.
     */
    public static final java.lang.String META_DATA_NAME = "android.location.SettingInjectorService";

    /**
     * Name of the XML tag that includes the attributes for the setting.
     */
    public static final java.lang.String ATTRIBUTES_NAME = "injected-location-setting";

    /**
     * Intent action a client should broadcast when the value of one of its injected settings has
     * changed, so that the setting can be updated in the UI.
     */
    public static final java.lang.String ACTION_INJECTED_SETTING_CHANGED = "android.location.InjectedSettingChanged";

    /**
     * Name of the bundle key for the string specifying whether the setting is currently enabled.
     *
     * @unknown 
     */
    public static final java.lang.String ENABLED_KEY = "enabled";

    /**
     * Name of the intent key used to specify the messenger
     *
     * @unknown 
     */
    public static final java.lang.String MESSENGER_KEY = "messenger";

    private final java.lang.String mName;

    /**
     * Constructor.
     *
     * @param name
     * 		used to identify your subclass in log messages
     */
    public SettingInjectorService(java.lang.String name) {
        mName = name;
    }

    @java.lang.Override
    public final android.os.IBinder onBind(android.content.Intent intent) {
        return null;
    }

    @java.lang.Override
    public final void onStart(android.content.Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @java.lang.Override
    public final int onStartCommand(android.content.Intent intent, int flags, int startId) {
        onHandleIntent(intent);
        stopSelf(startId);
        return android.app.Service.START_NOT_STICKY;
    }

    private void onHandleIntent(android.content.Intent intent) {
        boolean enabled;
        try {
            enabled = onGetEnabled();
        } catch (java.lang.RuntimeException e) {
            // Exception. Send status anyway, so that settings injector can immediately start
            // loading the status of the next setting.
            sendStatus(intent, true);
            throw e;
        }
        sendStatus(intent, enabled);
    }

    /**
     * Send the enabled values back to the caller via the messenger encoded in the
     * intent.
     */
    private void sendStatus(android.content.Intent intent, boolean enabled) {
        android.os.Message message = android.os.Message.obtain();
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putBoolean(android.location.SettingInjectorService.ENABLED_KEY, enabled);
        message.setData(bundle);
        if (android.util.Log.isLoggable(android.location.SettingInjectorService.TAG, android.util.Log.DEBUG)) {
            android.util.Log.d(android.location.SettingInjectorService.TAG, (((((mName + ": received ") + intent) + ", enabled=") + enabled) + ", sending message: ") + message);
        }
        android.os.Messenger messenger = intent.getParcelableExtra(android.location.SettingInjectorService.MESSENGER_KEY);
        try {
            messenger.send(message);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.location.SettingInjectorService.TAG, mName + ": sending dynamic status failed", e);
        }
    }

    /**
     * This method is no longer called, because status values are no longer shown for any injected
     * setting.
     *
     * @return ignored
     * @deprecated not called any more
     */
    @java.lang.Deprecated
    protected abstract java.lang.String onGetSummary();

    /**
     * Returns the {@link android.preference.Preference#isEnabled()} value. Should not perform
     * unpredictably-long operations such as network access--see the running-time comments in the
     * class-level javadoc.
     * <p/>
     * Note that to prevent churn in the settings list, there is no support for dynamically choosing
     * to hide a setting. Instead you should have this method return false, which will disable the
     * setting and its link to your setting activity. One reason why you might choose to do this is
     * if {@link android.provider.Settings.Secure#LOCATION_MODE} is {@link android.provider.Settings.Secure#LOCATION_MODE_OFF}.
     * <p/>
     * It is possible that the user may click on the setting before this method returns, so your
     * settings activity must handle the case where it is invoked even though the setting is
     * disabled. The simplest approach may be to simply call {@link android.app.Activity#finish()}
     * when disabled.
     *
     * @return the {@link android.preference.Preference#isEnabled()} value
     */
    protected abstract boolean onGetEnabled();
}

