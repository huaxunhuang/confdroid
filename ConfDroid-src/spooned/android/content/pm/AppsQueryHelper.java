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
 * limitations under the License
 */
package android.content.pm;


/**
 * Helper class for querying installed applications using multiple criteria.
 *
 * @unknown 
 */
public class AppsQueryHelper {
    /**
     * Return apps without launcher icon
     */
    public static int GET_NON_LAUNCHABLE_APPS = 1;

    /**
     * Return apps with {@link Manifest.permission#INTERACT_ACROSS_USERS} permission
     */
    public static int GET_APPS_WITH_INTERACT_ACROSS_USERS_PERM = 1 << 1;

    /**
     * Return all input methods available for the current user.
     */
    public static int GET_IMES = 1 << 2;

    /**
     * Return all apps that are flagged as required for the system user.
     */
    public static int GET_REQUIRED_FOR_SYSTEM_USER = 1 << 3;

    private final android.content.pm.IPackageManager mPackageManager;

    private java.util.List<android.content.pm.ApplicationInfo> mAllApps;

    public AppsQueryHelper(android.content.pm.IPackageManager packageManager) {
        mPackageManager = packageManager;
    }

    public AppsQueryHelper() {
        this(android.app.AppGlobals.getPackageManager());
    }

    /**
     * Return a List of all packages that satisfy a specified criteria.
     *
     * @param flags
     * 		search flags. Use any combination of {@link #GET_NON_LAUNCHABLE_APPS},
     * 		{@link #GET_APPS_WITH_INTERACT_ACROSS_USERS_PERM} or {@link #GET_IMES}.
     * @param systemAppsOnly
     * 		if true, only system apps will be returned
     * @param user
     * 		user, whose apps are queried
     */
    public java.util.List<java.lang.String> queryApps(int flags, boolean systemAppsOnly, android.os.UserHandle user) {
        boolean nonLaunchableApps = (flags & android.content.pm.AppsQueryHelper.GET_NON_LAUNCHABLE_APPS) > 0;
        boolean interactAcrossUsers = (flags & android.content.pm.AppsQueryHelper.GET_APPS_WITH_INTERACT_ACROSS_USERS_PERM) > 0;
        boolean imes = (flags & android.content.pm.AppsQueryHelper.GET_IMES) > 0;
        boolean requiredForSystemUser = (flags & android.content.pm.AppsQueryHelper.GET_REQUIRED_FOR_SYSTEM_USER) > 0;
        if (mAllApps == null) {
            mAllApps = getAllApps(user.getIdentifier());
        }
        java.util.List<java.lang.String> result = new java.util.ArrayList<>();
        if (flags == 0) {
            final int allAppsSize = mAllApps.size();
            for (int i = 0; i < allAppsSize; i++) {
                final android.content.pm.ApplicationInfo appInfo = mAllApps.get(i);
                if (systemAppsOnly && (!appInfo.isSystemApp())) {
                    continue;
                }
                result.add(appInfo.packageName);
            }
            return result;
        }
        if (nonLaunchableApps) {
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_MAIN).addCategory(android.content.Intent.CATEGORY_LAUNCHER);
            final java.util.List<android.content.pm.ResolveInfo> resolveInfos = queryIntentActivitiesAsUser(intent, user.getIdentifier());
            android.util.ArraySet<java.lang.String> appsWithLaunchers = new android.util.ArraySet();
            final int resolveInfosSize = resolveInfos.size();
            for (int i = 0; i < resolveInfosSize; i++) {
                appsWithLaunchers.add(resolveInfos.get(i).activityInfo.packageName);
            }
            final int allAppsSize = mAllApps.size();
            for (int i = 0; i < allAppsSize; i++) {
                final android.content.pm.ApplicationInfo appInfo = mAllApps.get(i);
                if (systemAppsOnly && (!appInfo.isSystemApp())) {
                    continue;
                }
                final java.lang.String packageName = appInfo.packageName;
                if (!appsWithLaunchers.contains(packageName)) {
                    result.add(packageName);
                }
            }
        }
        if (interactAcrossUsers) {
            final java.util.List<android.content.pm.PackageInfo> packagesHoldingPermissions = getPackagesHoldingPermission(Manifest.permission.INTERACT_ACROSS_USERS, user.getIdentifier());
            final int packagesHoldingPermissionsSize = packagesHoldingPermissions.size();
            for (int i = 0; i < packagesHoldingPermissionsSize; i++) {
                android.content.pm.PackageInfo packageInfo = packagesHoldingPermissions.get(i);
                if (systemAppsOnly && (!packageInfo.applicationInfo.isSystemApp())) {
                    continue;
                }
                if (!result.contains(packageInfo.packageName)) {
                    result.add(packageInfo.packageName);
                }
            }
        }
        if (imes) {
            final java.util.List<android.content.pm.ResolveInfo> resolveInfos = queryIntentServicesAsUser(new android.content.Intent(android.view.inputmethod.InputMethod.SERVICE_INTERFACE), user.getIdentifier());
            final int resolveInfosSize = resolveInfos.size();
            for (int i = 0; i < resolveInfosSize; i++) {
                android.content.pm.ServiceInfo serviceInfo = resolveInfos.get(i).serviceInfo;
                if (systemAppsOnly && (!serviceInfo.applicationInfo.isSystemApp())) {
                    continue;
                }
                if (!result.contains(serviceInfo.packageName)) {
                    result.add(serviceInfo.packageName);
                }
            }
        }
        if (requiredForSystemUser) {
            final int allAppsSize = mAllApps.size();
            for (int i = 0; i < allAppsSize; i++) {
                final android.content.pm.ApplicationInfo appInfo = mAllApps.get(i);
                if (systemAppsOnly && (!appInfo.isSystemApp())) {
                    continue;
                }
                if (appInfo.isRequiredForSystemUser()) {
                    result.add(appInfo.packageName);
                }
            }
        }
        return result;
    }

    @com.android.internal.annotations.VisibleForTesting
    @java.lang.SuppressWarnings("unchecked")
    protected java.util.List<android.content.pm.ApplicationInfo> getAllApps(int userId) {
        try {
            return mPackageManager.getInstalledApplications(android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES | android.content.pm.PackageManager.MATCH_DISABLED_COMPONENTS, userId).getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @com.android.internal.annotations.VisibleForTesting
    protected java.util.List<android.content.pm.ResolveInfo> queryIntentActivitiesAsUser(android.content.Intent intent, int userId) {
        try {
            return mPackageManager.queryIntentActivities(intent, null, ((android.content.pm.PackageManager.MATCH_DISABLED_COMPONENTS | android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES) | android.content.pm.PackageManager.MATCH_DIRECT_BOOT_AWARE) | android.content.pm.PackageManager.MATCH_DIRECT_BOOT_UNAWARE, userId).getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @com.android.internal.annotations.VisibleForTesting
    protected java.util.List<android.content.pm.ResolveInfo> queryIntentServicesAsUser(android.content.Intent intent, int userId) {
        try {
            return mPackageManager.queryIntentServices(intent, null, ((android.content.pm.PackageManager.GET_META_DATA | android.content.pm.PackageManager.MATCH_DISABLED_UNTIL_USED_COMPONENTS) | android.content.pm.PackageManager.MATCH_DIRECT_BOOT_AWARE) | android.content.pm.PackageManager.MATCH_DIRECT_BOOT_UNAWARE, userId).getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @com.android.internal.annotations.VisibleForTesting
    @java.lang.SuppressWarnings("unchecked")
    protected java.util.List<android.content.pm.PackageInfo> getPackagesHoldingPermission(java.lang.String perm, int userId) {
        try {
            return mPackageManager.getPackagesHoldingPermissions(new java.lang.String[]{ perm }, 0, userId).getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}

