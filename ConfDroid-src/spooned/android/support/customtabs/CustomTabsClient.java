/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.customtabs;


/**
 * Class to communicate with a {@link CustomTabsService} and create
 * {@link CustomTabsSession} from it.
 */
public class CustomTabsClient {
    private final android.support.customtabs.ICustomTabsService mService;

    private final android.content.ComponentName mServiceComponentName;

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    CustomTabsClient(android.support.customtabs.ICustomTabsService service, android.content.ComponentName componentName) {
        mService = service;
        mServiceComponentName = componentName;
    }

    /**
     * Bind to a {@link CustomTabsService} using the given package name and
     * {@link ServiceConnection}.
     *
     * @param context
     * 		{@link Context} to use while calling
     * 		{@link Context#bindService(Intent, ServiceConnection, int)}
     * @param packageName
     * 		Package name to set on the {@link Intent} for binding.
     * @param connection
     * 		{@link CustomTabsServiceConnection} to use when binding. This will
     * 		return a {@link CustomTabsClient} on
     * 		{@link CustomTabsServiceConnection
     * 		#onCustomTabsServiceConnected(ComponentName, CustomTabsClient)}
     * @return Whether the binding was successful.
     */
    public static boolean bindCustomTabsService(android.content.Context context, java.lang.String packageName, android.support.customtabs.CustomTabsServiceConnection connection) {
        android.content.Intent intent = new android.content.Intent(android.support.customtabs.CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION);
        if (!android.text.TextUtils.isEmpty(packageName))
            intent.setPackage(packageName);

        return context.bindService(intent, connection, android.content.Context.BIND_AUTO_CREATE | android.content.Context.BIND_WAIVE_PRIORITY);
    }

    /**
     * Returns the preferred package to use for Custom Tabs, preferring the default VIEW handler.
     *
     * @see {@link #getPackageName(Context, List<String>, boolean)}.
     */
    public static java.lang.String getPackageName(android.content.Context context, @android.support.annotation.Nullable
    java.util.List<java.lang.String> packages) {
        return android.support.customtabs.CustomTabsClient.getPackageName(context, packages, false);
    }

    /**
     * Returns the preferred package to use for Custom Tabs.
     *
     * The preferred package name is the default VIEW intent handler as long as it supports Custom
     * Tabs. To modify this preferred behavior, set <code>ignoreDefault</code> to true and give a
     * non empty list of package names in <code>packages</code>.
     *
     * @param context
     * 		{@link Context} to use for querying the packages.
     * @param packages
     * 		Ordered list of packages to test for Custom Tabs support, in
     * 		decreasing order of priority.
     * @param ignoreDefault
     * 		If set, the default VIEW handler won't get priority over other browsers.
     * @return The preferred package name for handling Custom Tabs, or <code>null</code>.
     */
    public static java.lang.String getPackageName(android.content.Context context, @android.support.annotation.Nullable
    java.util.List<java.lang.String> packages, boolean ignoreDefault) {
        android.content.pm.PackageManager pm = context.getPackageManager();
        java.util.List<java.lang.String> packageNames = (packages == null) ? new java.util.ArrayList<java.lang.String>() : packages;
        android.content.Intent activityIntent = new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("http://"));
        if (!ignoreDefault) {
            android.content.pm.ResolveInfo defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0);
            if (defaultViewHandlerInfo != null) {
                java.lang.String packageName = defaultViewHandlerInfo.activityInfo.packageName;
                packageNames = new java.util.ArrayList<java.lang.String>(packageNames.size() + 1);
                packageNames.add(packageName);
                if (packages != null)
                    packageNames.addAll(packages);

            }
        }
        android.content.Intent serviceIntent = new android.content.Intent(android.support.customtabs.CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION);
        for (java.lang.String packageName : packageNames) {
            serviceIntent.setPackage(packageName);
            if (pm.resolveService(serviceIntent, 0) != null)
                return packageName;

        }
        return null;
    }

    /**
     * Connects to the Custom Tabs warmup service, and initializes the browser.
     *
     * This convenience method connects to the service, and immediately warms up the Custom Tabs
     * implementation. Since service connection is asynchronous, the return code is not the return
     * code of warmup.
     * This call is optional, and clients are encouraged to connect to the service, call
     * <code>warmup()</code> and create a session. In this case, calling this method is not
     * necessary.
     *
     * @param context
     * 		{@link Context} to use to connect to the remote service.
     * @param packageName
     * 		Package name of the target implementation.
     * @return Whether the binding was successful.
     */
    public static boolean connectAndInitialize(android.content.Context context, java.lang.String packageName) {
        if (packageName == null)
            return false;

        final android.content.Context applicationContext = context.getApplicationContext();
        android.support.customtabs.CustomTabsServiceConnection connection = new android.support.customtabs.CustomTabsServiceConnection() {
            @java.lang.Override
            public final void onCustomTabsServiceConnected(android.content.ComponentName name, android.support.customtabs.CustomTabsClient client) {
                client.warmup(0);
                // Unbinding immediately makes the target process "Empty", provided that it is
                // not used by anyone else, and doesn't contain any Activity. This makes it
                // likely to get killed, but is preferable to keeping the connection around.
                applicationContext.unbindService(this);
            }

            @java.lang.Override
            public final void onServiceDisconnected(android.content.ComponentName componentName) {
            }
        };
        try {
            return android.support.customtabs.CustomTabsClient.bindCustomTabsService(applicationContext, packageName, connection);
        } catch (java.lang.SecurityException e) {
            return false;
        }
    }

    /**
     * Warm up the browser process.
     *
     * Allows the browser application to pre-initialize itself in the background. Significantly
     * speeds up URL opening in the browser. This is asynchronous and can be called several times.
     *
     * @param flags
     * 		Reserved for future use.
     * @return Whether the warmup was successful.
     */
    public boolean warmup(long flags) {
        try {
            return mService.warmup(flags);
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    /**
     * Creates a new session through an ICustomTabsService with the optional callback. This session
     * can be used to associate any related communication through the service with an intent and
     * then later with a Custom Tab. The client can then send later service calls or intents to
     * through same session-intent-Custom Tab association.
     *
     * @param callback
     * 		The callback through which the client will receive updates about the created
     * 		session. Can be null.
     * @return The session object that was created as a result of the transaction. The client can
    use this to relay {@link CustomTabsSession#mayLaunchUrl(Uri, Bundle, List)} calls.
    Null on error.
     */
    public android.support.customtabs.CustomTabsSession newSession(final android.support.customtabs.CustomTabsCallback callback) {
        android.support.customtabs.ICustomTabsCallback.Stub wrapper = new android.support.customtabs.ICustomTabsCallback.Stub() {
            @java.lang.Override
            public void onNavigationEvent(int navigationEvent, android.os.Bundle extras) {
                if (callback != null)
                    callback.onNavigationEvent(navigationEvent, extras);

            }

            @java.lang.Override
            public void extraCallback(java.lang.String callbackName, android.os.Bundle args) throws android.os.RemoteException {
                if (callback != null)
                    callback.extraCallback(callbackName, args);

            }
        };
        try {
            if (!mService.newSession(wrapper))
                return null;

        } catch (android.os.RemoteException e) {
            return null;
        }
        return new android.support.customtabs.CustomTabsSession(mService, wrapper, mServiceComponentName);
    }

    public android.os.Bundle extraCommand(java.lang.String commandName, android.os.Bundle args) {
        try {
            return mService.extraCommand(commandName, args);
        } catch (android.os.RemoteException e) {
            return null;
        }
    }
}

