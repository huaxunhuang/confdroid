/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * Class for managing the default dialer application that will receive incoming calls, and be
 * allowed to make emergency outgoing calls.
 *
 * @unknown 
 */
public class DefaultDialerManager {
    private static final java.lang.String TAG = "DefaultDialerManager";

    /**
     * Sets the specified package name as the default dialer application for the current user.
     * The caller of this method needs to have permission to write to secure settings and
     * manage users on the device.
     *
     * @return {@code true} if the default dialer application was successfully changed,
    {@code false} otherwise.
     * @unknown 
     */
    public static boolean setDefaultDialerApplication(android.content.Context context, java.lang.String packageName) {
        return android.telecom.DefaultDialerManager.setDefaultDialerApplication(context, packageName, android.app.ActivityManager.getCurrentUser());
    }

    /**
     * Sets the specified package name as the default dialer application for the specified user.
     * The caller of this method needs to have permission to write to secure settings and
     * manage users on the device.
     *
     * @return {@code true} if the default dialer application was successfully changed,
    {@code false} otherwise.
     * @unknown 
     */
    public static boolean setDefaultDialerApplication(android.content.Context context, java.lang.String packageName, int user) {
        // Get old package name
        java.lang.String oldPackageName = android.provider.Settings.Secure.getStringForUser(context.getContentResolver(), android.provider.Settings.Secure.DIALER_DEFAULT_APPLICATION, user);
        if (((packageName != null) && (oldPackageName != null)) && packageName.equals(oldPackageName)) {
            // No change
            return false;
        }
        // Only make the change if the new package belongs to a valid phone application
        java.util.List<java.lang.String> packageNames = android.telecom.DefaultDialerManager.getInstalledDialerApplications(context);
        if (packageNames.contains(packageName)) {
            // Update the secure setting.
            android.provider.Settings.Secure.putStringForUser(context.getContentResolver(), android.provider.Settings.Secure.DIALER_DEFAULT_APPLICATION, packageName, user);
            return true;
        }
        return false;
    }

    /**
     * Returns the installed dialer application for the current user that will be used to receive
     * incoming calls, and is allowed to make emergency calls.
     *
     * The application will be returned in order of preference:
     * 1) User selected phone application (if still installed)
     * 2) Pre-installed system dialer (if not disabled)
     * 3) Null
     *
     * The caller of this method needs to have permission to manage users on the device.
     *
     * @unknown 
     */
    public static java.lang.String getDefaultDialerApplication(android.content.Context context) {
        return android.telecom.DefaultDialerManager.getDefaultDialerApplication(context, context.getUserId());
    }

    /**
     * Returns the installed dialer application for the specified user that will be used to receive
     * incoming calls, and is allowed to make emergency calls.
     *
     * The application will be returned in order of preference:
     * 1) User selected phone application (if still installed)
     * 2) Pre-installed system dialer (if not disabled)
     * 3) Null
     *
     * The caller of this method needs to have permission to manage users on the device.
     *
     * @unknown 
     */
    public static java.lang.String getDefaultDialerApplication(android.content.Context context, int user) {
        java.lang.String defaultPackageName = android.provider.Settings.Secure.getStringForUser(context.getContentResolver(), android.provider.Settings.Secure.DIALER_DEFAULT_APPLICATION, user);
        final java.util.List<java.lang.String> packageNames = android.telecom.DefaultDialerManager.getInstalledDialerApplications(context);
        // Verify that the default dialer has not been disabled or uninstalled.
        if (packageNames.contains(defaultPackageName)) {
            return defaultPackageName;
        }
        // No user-set dialer found, fallback to system dialer
        java.lang.String systemDialerPackageName = android.telecom.DefaultDialerManager.getTelecomManager(context).getSystemDialerPackage();
        if (android.text.TextUtils.isEmpty(systemDialerPackageName)) {
            // No system dialer configured at build time
            return null;
        }
        if (packageNames.contains(systemDialerPackageName)) {
            return systemDialerPackageName;
        } else {
            return null;
        }
    }

    /**
     * Returns a list of installed and available dialer applications.
     *
     * In order to appear in the list, a dialer application must implement an intent-filter with
     * the DIAL intent for the following schemes:
     *
     * 1) Empty scheme
     * 2) tel Uri scheme
     *
     * @unknown 
     */
    public static java.util.List<java.lang.String> getInstalledDialerApplications(android.content.Context context, int userId) {
        android.content.pm.PackageManager packageManager = context.getPackageManager();
        // Get the list of apps registered for the DIAL intent with empty scheme
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_DIAL);
        java.util.List<android.content.pm.ResolveInfo> resolveInfoList = packageManager.queryIntentActivitiesAsUser(intent, 0, userId);
        java.util.List<java.lang.String> packageNames = new java.util.ArrayList<>();
        for (android.content.pm.ResolveInfo resolveInfo : resolveInfoList) {
            final android.content.pm.ActivityInfo activityInfo = resolveInfo.activityInfo;
            if ((activityInfo != null) && (!packageNames.contains(activityInfo.packageName))) {
                packageNames.add(activityInfo.packageName);
            }
        }
        final android.content.Intent dialIntentWithTelScheme = new android.content.Intent(android.content.Intent.ACTION_DIAL);
        dialIntentWithTelScheme.setData(android.net.Uri.fromParts(android.telecom.PhoneAccount.SCHEME_TEL, "", null));
        return android.telecom.DefaultDialerManager.filterByIntent(context, packageNames, dialIntentWithTelScheme);
    }

    public static java.util.List<java.lang.String> getInstalledDialerApplications(android.content.Context context) {
        return android.telecom.DefaultDialerManager.getInstalledDialerApplications(context, android.os.Process.myUserHandle().getIdentifier());
    }

    /**
     * Determines if the package name belongs to the user-selected default dialer or the preloaded
     * system dialer, and thus should be allowed to perform certain privileged operations.
     *
     * @param context
     * 		A valid context.
     * @param packageName
     * 		of the package to check for.
     * @return {@code true} if the provided package name corresponds to the user-selected default
    dialer or the preloaded system dialer, {@code false} otherwise.
     * @unknown 
     */
    public static boolean isDefaultOrSystemDialer(android.content.Context context, java.lang.String packageName) {
        if (android.text.TextUtils.isEmpty(packageName)) {
            return false;
        }
        final android.telecom.TelecomManager tm = android.telecom.DefaultDialerManager.getTelecomManager(context);
        return packageName.equals(tm.getDefaultDialerPackage()) || packageName.equals(tm.getSystemDialerPackage());
    }

    /**
     * Filter a given list of package names for those packages that contain an activity that has
     * an intent filter for a given intent.
     *
     * @param context
     * 		A valid context
     * @param packageNames
     * 		List of package names to filter.
     * @return The filtered list.
     */
    private static java.util.List<java.lang.String> filterByIntent(android.content.Context context, java.util.List<java.lang.String> packageNames, android.content.Intent intent) {
        if ((packageNames == null) || packageNames.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        final java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        final java.util.List<android.content.pm.ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, 0);
        final int length = resolveInfoList.size();
        for (int i = 0; i < length; i++) {
            final android.content.pm.ActivityInfo info = resolveInfoList.get(i).activityInfo;
            if (((info != null) && packageNames.contains(info.packageName)) && (!result.contains(info.packageName))) {
                result.add(info.packageName);
            }
        }
        return result;
    }

    private static android.telecom.TelecomManager getTelecomManager(android.content.Context context) {
        return ((android.telecom.TelecomManager) (context.getSystemService(android.content.Context.TELECOM_SERVICE)));
    }
}

