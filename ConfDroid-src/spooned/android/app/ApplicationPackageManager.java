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
package android.app;


/**
 *
 *
 * @unknown 
 */
public class ApplicationPackageManager extends android.content.pm.PackageManager {
    private static final java.lang.String TAG = "ApplicationPackageManager";

    private static final boolean DEBUG_ICONS = false;

    private static final int DEFAULT_EPHEMERAL_COOKIE_MAX_SIZE_BYTES = 16384;// 16KB


    // Default flags to use with PackageManager when no flags are given.
    private static final int sDefaultFlags = android.content.pm.PackageManager.GET_SHARED_LIBRARY_FILES;

    private final java.lang.Object mLock = new java.lang.Object();

    @com.android.internal.annotations.GuardedBy("mLock")
    private android.os.UserManager mUserManager;

    @com.android.internal.annotations.GuardedBy("mLock")
    private android.content.pm.PackageInstaller mInstaller;

    @com.android.internal.annotations.GuardedBy("mDelegates")
    private final java.util.ArrayList<android.app.ApplicationPackageManager.MoveCallbackDelegate> mDelegates = new java.util.ArrayList<>();

    @com.android.internal.annotations.GuardedBy("mLock")
    private java.lang.String mPermissionsControllerPackageName;

    android.os.UserManager getUserManager() {
        synchronized(mLock) {
            if (mUserManager == null) {
                mUserManager = android.os.UserManager.get(mContext);
            }
            return mUserManager;
        }
    }

    @java.lang.Override
    public android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        return getPackageInfoAsUser(packageName, flags, mContext.getUserId());
    }

    @java.lang.Override
    public android.content.pm.PackageInfo getPackageInfoAsUser(java.lang.String packageName, int flags, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.PackageInfo pi = mPM.getPackageInfo(packageName, flags, userId);
            if (pi != null) {
                return pi;
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(packageName);
    }

    @java.lang.Override
    public java.lang.String[] currentToCanonicalPackageNames(java.lang.String[] names) {
        try {
            return mPM.currentToCanonicalPackageNames(names);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public java.lang.String[] canonicalToCurrentPackageNames(java.lang.String[] names) {
        try {
            return mPM.canonicalToCurrentPackageNames(names);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public android.content.Intent getLaunchIntentForPackage(java.lang.String packageName) {
        // First see if the package has an INFO activity; the existence of
        // such an activity is implied to be the desired front-door for the
        // overall package (such as if it has multiple launcher entries).
        android.content.Intent intentToResolve = new android.content.Intent(android.content.Intent.ACTION_MAIN);
        intentToResolve.addCategory(android.content.Intent.CATEGORY_INFO);
        intentToResolve.setPackage(packageName);
        java.util.List<android.content.pm.ResolveInfo> ris = queryIntentActivities(intentToResolve, 0);
        // Otherwise, try to find a main launcher activity.
        if ((ris == null) || (ris.size() <= 0)) {
            // reuse the intent instance
            intentToResolve.removeCategory(android.content.Intent.CATEGORY_INFO);
            intentToResolve.addCategory(android.content.Intent.CATEGORY_LAUNCHER);
            intentToResolve.setPackage(packageName);
            ris = queryIntentActivities(intentToResolve, 0);
        }
        if ((ris == null) || (ris.size() <= 0)) {
            return null;
        }
        android.content.Intent intent = new android.content.Intent(intentToResolve);
        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(ris.get(0).activityInfo.packageName, ris.get(0).activityInfo.name);
        return intent;
    }

    @java.lang.Override
    public android.content.Intent getLeanbackLaunchIntentForPackage(java.lang.String packageName) {
        // Try to find a main leanback_launcher activity.
        android.content.Intent intentToResolve = new android.content.Intent(android.content.Intent.ACTION_MAIN);
        intentToResolve.addCategory(android.content.Intent.CATEGORY_LEANBACK_LAUNCHER);
        intentToResolve.setPackage(packageName);
        java.util.List<android.content.pm.ResolveInfo> ris = queryIntentActivities(intentToResolve, 0);
        if ((ris == null) || (ris.size() <= 0)) {
            return null;
        }
        android.content.Intent intent = new android.content.Intent(intentToResolve);
        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(ris.get(0).activityInfo.packageName, ris.get(0).activityInfo.name);
        return intent;
    }

    @java.lang.Override
    public int[] getPackageGids(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        return getPackageGids(packageName, 0);
    }

    @java.lang.Override
    public int[] getPackageGids(java.lang.String packageName, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            int[] gids = mPM.getPackageGids(packageName, flags, mContext.getUserId());
            if (gids != null) {
                return gids;
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(packageName);
    }

    @java.lang.Override
    public int getPackageUid(java.lang.String packageName, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        return getPackageUidAsUser(packageName, flags, mContext.getUserId());
    }

    @java.lang.Override
    public int getPackageUidAsUser(java.lang.String packageName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        return getPackageUidAsUser(packageName, 0, userId);
    }

    @java.lang.Override
    public int getPackageUidAsUser(java.lang.String packageName, int flags, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            int uid = mPM.getPackageUid(packageName, flags, userId);
            if (uid >= 0) {
                return uid;
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(packageName);
    }

    @java.lang.Override
    public android.content.pm.PermissionInfo getPermissionInfo(java.lang.String name, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.PermissionInfo pi = mPM.getPermissionInfo(name, flags);
            if (pi != null) {
                return pi;
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(name);
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public java.util.List<android.content.pm.PermissionInfo> queryPermissionsByGroup(java.lang.String group, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.ParceledListSlice<android.content.pm.PermissionInfo> parceledList = mPM.queryPermissionsByGroup(group, flags);
            if (parceledList != null) {
                java.util.List<android.content.pm.PermissionInfo> pi = parceledList.getList();
                if (pi != null) {
                    return pi;
                }
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(group);
    }

    @java.lang.Override
    public android.content.pm.PermissionGroupInfo getPermissionGroupInfo(java.lang.String name, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.PermissionGroupInfo pgi = mPM.getPermissionGroupInfo(name, flags);
            if (pgi != null) {
                return pgi;
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(name);
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public java.util.List<android.content.pm.PermissionGroupInfo> getAllPermissionGroups(int flags) {
        try {
            android.content.pm.ParceledListSlice<android.content.pm.PermissionGroupInfo> parceledList = mPM.getAllPermissionGroups(flags);
            if (parceledList == null) {
                return java.util.Collections.emptyList();
            }
            return parceledList.getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String packageName, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        return getApplicationInfoAsUser(packageName, flags, mContext.getUserId());
    }

    @java.lang.Override
    public android.content.pm.ApplicationInfo getApplicationInfoAsUser(java.lang.String packageName, int flags, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.ApplicationInfo ai = mPM.getApplicationInfo(packageName, flags, userId);
            if (ai != null) {
                // This is a temporary hack. Callers must use
                // createPackageContext(packageName).getApplicationInfo() to
                // get the right paths.
                return android.app.ApplicationPackageManager.maybeAdjustApplicationInfo(ai);
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(packageName);
    }

    private static android.content.pm.ApplicationInfo maybeAdjustApplicationInfo(android.content.pm.ApplicationInfo info) {
        // If we're dealing with a multi-arch application that has both
        // 32 and 64 bit shared libraries, we might need to choose the secondary
        // depending on what the current runtime's instruction set is.
        if ((info.primaryCpuAbi != null) && (info.secondaryCpuAbi != null)) {
            final java.lang.String runtimeIsa = dalvik.system.VMRuntime.getRuntime().vmInstructionSet();
            // Get the instruction set that the libraries of secondary Abi is supported.
            // In presence of a native bridge this might be different than the one secondary Abi used.
            java.lang.String secondaryIsa = dalvik.system.VMRuntime.getInstructionSet(info.secondaryCpuAbi);
            final java.lang.String secondaryDexCodeIsa = android.os.SystemProperties.get("ro.dalvik.vm.isa." + secondaryIsa);
            secondaryIsa = (secondaryDexCodeIsa.isEmpty()) ? secondaryIsa : secondaryDexCodeIsa;
            // If the runtimeIsa is the same as the primary isa, then we do nothing.
            // Everything will be set up correctly because info.nativeLibraryDir will
            // correspond to the right ISA.
            if (runtimeIsa.equals(secondaryIsa)) {
                android.content.pm.ApplicationInfo modified = new android.content.pm.ApplicationInfo(info);
                modified.nativeLibraryDir = info.secondaryNativeLibraryDir;
                return modified;
            }
        }
        return info;
    }

    @java.lang.Override
    public android.content.pm.ActivityInfo getActivityInfo(android.content.ComponentName className, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.ActivityInfo ai = mPM.getActivityInfo(className, flags, mContext.getUserId());
            if (ai != null) {
                return ai;
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(className.toString());
    }

    @java.lang.Override
    public android.content.pm.ActivityInfo getReceiverInfo(android.content.ComponentName className, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.ActivityInfo ai = mPM.getReceiverInfo(className, flags, mContext.getUserId());
            if (ai != null) {
                return ai;
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(className.toString());
    }

    @java.lang.Override
    public android.content.pm.ServiceInfo getServiceInfo(android.content.ComponentName className, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.ServiceInfo si = mPM.getServiceInfo(className, flags, mContext.getUserId());
            if (si != null) {
                return si;
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(className.toString());
    }

    @java.lang.Override
    public android.content.pm.ProviderInfo getProviderInfo(android.content.ComponentName className, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.ProviderInfo pi = mPM.getProviderInfo(className, flags, mContext.getUserId());
            if (pi != null) {
                return pi;
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(className.toString());
    }

    @java.lang.Override
    public java.lang.String[] getSystemSharedLibraryNames() {
        try {
            return mPM.getSystemSharedLibraryNames();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.NonNull
    public java.lang.String getServicesSystemSharedLibraryPackageName() {
        try {
            return mPM.getServicesSystemSharedLibraryPackageName();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public java.lang.String getSharedSystemSharedLibraryPackageName() {
        try {
            return mPM.getSharedSystemSharedLibraryPackageName();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public android.content.pm.FeatureInfo[] getSystemAvailableFeatures() {
        try {
            android.content.pm.ParceledListSlice<android.content.pm.FeatureInfo> parceledList = mPM.getSystemAvailableFeatures();
            if (parceledList == null) {
                return new android.content.pm.FeatureInfo[0];
            }
            final java.util.List<android.content.pm.FeatureInfo> list = parceledList.getList();
            final android.content.pm.FeatureInfo[] res = new android.content.pm.FeatureInfo[list.size()];
            for (int i = 0; i < res.length; i++) {
                res[i] = list.get(i);
            }
            return res;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public boolean hasSystemFeature(java.lang.String name) {
        return hasSystemFeature(name, 0);
    }

    @java.lang.Override
    public boolean hasSystemFeature(java.lang.String name, int version) {
        try {
            return mPM.hasSystemFeature(name, version);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public int checkPermission(java.lang.String permName, java.lang.String pkgName) {
        try {
            return mPM.checkPermission(permName, pkgName, mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public boolean isPermissionRevokedByPolicy(java.lang.String permName, java.lang.String pkgName) {
        try {
            return mPM.isPermissionRevokedByPolicy(permName, pkgName, mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.lang.String getPermissionControllerPackageName() {
        synchronized(mLock) {
            if (mPermissionsControllerPackageName == null) {
                try {
                    mPermissionsControllerPackageName = mPM.getPermissionControllerPackageName();
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            return mPermissionsControllerPackageName;
        }
    }

    @java.lang.Override
    public boolean addPermission(android.content.pm.PermissionInfo info) {
        try {
            return mPM.addPermission(info);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public boolean addPermissionAsync(android.content.pm.PermissionInfo info) {
        try {
            return mPM.addPermissionAsync(info);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void removePermission(java.lang.String name) {
        try {
            mPM.removePermission(name);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void grantRuntimePermission(java.lang.String packageName, java.lang.String permissionName, android.os.UserHandle user) {
        try {
            mPM.grantRuntimePermission(packageName, permissionName, user.getIdentifier());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void revokeRuntimePermission(java.lang.String packageName, java.lang.String permissionName, android.os.UserHandle user) {
        try {
            mPM.revokeRuntimePermission(packageName, permissionName, user.getIdentifier());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public int getPermissionFlags(java.lang.String permissionName, java.lang.String packageName, android.os.UserHandle user) {
        try {
            return mPM.getPermissionFlags(permissionName, packageName, user.getIdentifier());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void updatePermissionFlags(java.lang.String permissionName, java.lang.String packageName, int flagMask, int flagValues, android.os.UserHandle user) {
        try {
            mPM.updatePermissionFlags(permissionName, packageName, flagMask, flagValues, user.getIdentifier());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public boolean shouldShowRequestPermissionRationale(java.lang.String permission) {
        try {
            return mPM.shouldShowRequestPermissionRationale(permission, mContext.getPackageName(), mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public int checkSignatures(java.lang.String pkg1, java.lang.String pkg2) {
        try {
            return mPM.checkSignatures(pkg1, pkg2);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public int checkSignatures(int uid1, int uid2) {
        try {
            return mPM.checkUidSignatures(uid1, uid2);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public java.lang.String[] getPackagesForUid(int uid) {
        try {
            return mPM.getPackagesForUid(uid);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public java.lang.String getNameForUid(int uid) {
        try {
            return mPM.getNameForUid(uid);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public int getUidForSharedUser(java.lang.String sharedUserName) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            int uid = mPM.getUidForSharedUser(sharedUserName);
            if (uid != (-1)) {
                return uid;
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        throw new android.content.pm.PackageManager.NameNotFoundException("No shared userid for user:" + sharedUserName);
    }

    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public java.util.List<android.content.pm.PackageInfo> getInstalledPackages(int flags) {
        return getInstalledPackagesAsUser(flags, mContext.getUserId());
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public java.util.List<android.content.pm.PackageInfo> getInstalledPackagesAsUser(int flags, int userId) {
        try {
            android.content.pm.ParceledListSlice<android.content.pm.PackageInfo> parceledList = mPM.getInstalledPackages(flags, userId);
            if (parceledList == null) {
                return java.util.Collections.emptyList();
            }
            return parceledList.getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public java.util.List<android.content.pm.PackageInfo> getPackagesHoldingPermissions(java.lang.String[] permissions, int flags) {
        final int userId = mContext.getUserId();
        try {
            android.content.pm.ParceledListSlice<android.content.pm.PackageInfo> parceledList = mPM.getPackagesHoldingPermissions(permissions, flags, userId);
            if (parceledList == null) {
                return java.util.Collections.emptyList();
            }
            return parceledList.getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public java.util.List<android.content.pm.ApplicationInfo> getInstalledApplications(int flags) {
        final int userId = mContext.getUserId();
        try {
            android.content.pm.ParceledListSlice<android.content.pm.ApplicationInfo> parceledList = mPM.getInstalledApplications(flags, userId);
            if (parceledList == null) {
                return java.util.Collections.emptyList();
            }
            return parceledList.getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.SuppressWarnings("unchecked")
    @java.lang.Override
    public java.util.List<android.content.pm.EphemeralApplicationInfo> getEphemeralApplications() {
        try {
            android.content.pm.ParceledListSlice<android.content.pm.EphemeralApplicationInfo> slice = mPM.getEphemeralApplications(mContext.getUserId());
            if (slice != null) {
                return slice.getList();
            }
            return java.util.Collections.emptyList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.graphics.drawable.Drawable getEphemeralApplicationIcon(java.lang.String packageName) {
        try {
            android.graphics.Bitmap bitmap = mPM.getEphemeralApplicationIcon(packageName, mContext.getUserId());
            if (bitmap != null) {
                return new android.graphics.drawable.BitmapDrawable(null, bitmap);
            }
            return null;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public boolean isEphemeralApplication() {
        try {
            return mPM.isEphemeralApplication(mContext.getPackageName(), mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public int getEphemeralCookieMaxSizeBytes() {
        return android.provider.Settings.Global.getInt(mContext.getContentResolver(), android.provider.Settings.Global.EPHEMERAL_COOKIE_MAX_SIZE_BYTES, android.app.ApplicationPackageManager.DEFAULT_EPHEMERAL_COOKIE_MAX_SIZE_BYTES);
    }

    @java.lang.Override
    @android.annotation.NonNull
    public byte[] getEphemeralCookie() {
        try {
            final byte[] cookie = mPM.getEphemeralApplicationCookie(mContext.getPackageName(), mContext.getUserId());
            if (cookie != null) {
                return cookie;
            } else {
                return libcore.util.EmptyArray.BYTE;
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public boolean setEphemeralCookie(@android.annotation.NonNull
    byte[] cookie) {
        try {
            return mPM.setEphemeralApplicationCookie(mContext.getPackageName(), cookie, mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public android.content.pm.ResolveInfo resolveActivity(android.content.Intent intent, int flags) {
        return resolveActivityAsUser(intent, flags, mContext.getUserId());
    }

    @java.lang.Override
    public android.content.pm.ResolveInfo resolveActivityAsUser(android.content.Intent intent, int flags, int userId) {
        try {
            return mPM.resolveIntent(intent, intent.resolveTypeIfNeeded(mContext.getContentResolver()), flags, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public java.util.List<android.content.pm.ResolveInfo> queryIntentActivities(android.content.Intent intent, int flags) {
        return queryIntentActivitiesAsUser(intent, flags, mContext.getUserId());
    }

    /**
     *
     *
     * @unknown Same as above but for a specific user
     */
    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public java.util.List<android.content.pm.ResolveInfo> queryIntentActivitiesAsUser(android.content.Intent intent, int flags, int userId) {
        try {
            android.content.pm.ParceledListSlice<android.content.pm.ResolveInfo> parceledList = mPM.queryIntentActivities(intent, intent.resolveTypeIfNeeded(mContext.getContentResolver()), flags, userId);
            if (parceledList == null) {
                return java.util.Collections.emptyList();
            }
            return parceledList.getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public java.util.List<android.content.pm.ResolveInfo> queryIntentActivityOptions(android.content.ComponentName caller, android.content.Intent[] specifics, android.content.Intent intent, int flags) {
        final android.content.ContentResolver resolver = mContext.getContentResolver();
        java.lang.String[] specificTypes = null;
        if (specifics != null) {
            final int N = specifics.length;
            for (int i = 0; i < N; i++) {
                android.content.Intent sp = specifics[i];
                if (sp != null) {
                    java.lang.String t = sp.resolveTypeIfNeeded(resolver);
                    if (t != null) {
                        if (specificTypes == null) {
                            specificTypes = new java.lang.String[N];
                        }
                        specificTypes[i] = t;
                    }
                }
            }
        }
        try {
            android.content.pm.ParceledListSlice<android.content.pm.ResolveInfo> parceledList = mPM.queryIntentActivityOptions(caller, specifics, specificTypes, intent, intent.resolveTypeIfNeeded(resolver), flags, mContext.getUserId());
            if (parceledList == null) {
                return java.util.Collections.emptyList();
            }
            return parceledList.getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public java.util.List<android.content.pm.ResolveInfo> queryBroadcastReceiversAsUser(android.content.Intent intent, int flags, int userId) {
        try {
            android.content.pm.ParceledListSlice<android.content.pm.ResolveInfo> parceledList = mPM.queryIntentReceivers(intent, intent.resolveTypeIfNeeded(mContext.getContentResolver()), flags, userId);
            if (parceledList == null) {
                return java.util.Collections.emptyList();
            }
            return parceledList.getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public java.util.List<android.content.pm.ResolveInfo> queryBroadcastReceivers(android.content.Intent intent, int flags) {
        return queryBroadcastReceiversAsUser(intent, flags, mContext.getUserId());
    }

    @java.lang.Override
    public android.content.pm.ResolveInfo resolveService(android.content.Intent intent, int flags) {
        try {
            return mPM.resolveService(intent, intent.resolveTypeIfNeeded(mContext.getContentResolver()), flags, mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public java.util.List<android.content.pm.ResolveInfo> queryIntentServicesAsUser(android.content.Intent intent, int flags, int userId) {
        try {
            android.content.pm.ParceledListSlice<android.content.pm.ResolveInfo> parceledList = mPM.queryIntentServices(intent, intent.resolveTypeIfNeeded(mContext.getContentResolver()), flags, userId);
            if (parceledList == null) {
                return java.util.Collections.emptyList();
            }
            return parceledList.getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public java.util.List<android.content.pm.ResolveInfo> queryIntentServices(android.content.Intent intent, int flags) {
        return queryIntentServicesAsUser(intent, flags, mContext.getUserId());
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public java.util.List<android.content.pm.ResolveInfo> queryIntentContentProvidersAsUser(android.content.Intent intent, int flags, int userId) {
        try {
            android.content.pm.ParceledListSlice<android.content.pm.ResolveInfo> parceledList = mPM.queryIntentContentProviders(intent, intent.resolveTypeIfNeeded(mContext.getContentResolver()), flags, userId);
            if (parceledList == null) {
                return java.util.Collections.emptyList();
            }
            return parceledList.getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public java.util.List<android.content.pm.ResolveInfo> queryIntentContentProviders(android.content.Intent intent, int flags) {
        return queryIntentContentProvidersAsUser(intent, flags, mContext.getUserId());
    }

    @java.lang.Override
    public android.content.pm.ProviderInfo resolveContentProvider(java.lang.String name, int flags) {
        return resolveContentProviderAsUser(name, flags, mContext.getUserId());
    }

    /**
     *
     *
     * @unknown *
     */
    @java.lang.Override
    public android.content.pm.ProviderInfo resolveContentProviderAsUser(java.lang.String name, int flags, int userId) {
        try {
            return mPM.resolveContentProvider(name, flags, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public java.util.List<android.content.pm.ProviderInfo> queryContentProviders(java.lang.String processName, int uid, int flags) {
        try {
            android.content.pm.ParceledListSlice<android.content.pm.ProviderInfo> slice = mPM.queryContentProviders(processName, uid, flags);
            return slice != null ? slice.getList() : java.util.Collections.<android.content.pm.ProviderInfo>emptyList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public android.content.pm.InstrumentationInfo getInstrumentationInfo(android.content.ComponentName className, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.InstrumentationInfo ii = mPM.getInstrumentationInfo(className, flags);
            if (ii != null) {
                return ii;
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(className.toString());
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public java.util.List<android.content.pm.InstrumentationInfo> queryInstrumentation(java.lang.String targetPackage, int flags) {
        try {
            android.content.pm.ParceledListSlice<android.content.pm.InstrumentationInfo> parceledList = mPM.queryInstrumentation(targetPackage, flags);
            if (parceledList == null) {
                return java.util.Collections.emptyList();
            }
            return parceledList.getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @android.annotation.Nullable
    @java.lang.Override
    public android.graphics.drawable.Drawable getDrawable(java.lang.String packageName, @android.annotation.DrawableRes
    int resId, @android.annotation.Nullable
    android.content.pm.ApplicationInfo appInfo) {
        final android.app.ApplicationPackageManager.ResourceName name = new android.app.ApplicationPackageManager.ResourceName(packageName, resId);
        final android.graphics.drawable.Drawable cachedIcon = getCachedIcon(name);
        if (cachedIcon != null) {
            return cachedIcon;
        }
        if (appInfo == null) {
            try {
                appInfo = getApplicationInfo(packageName, android.app.ApplicationPackageManager.sDefaultFlags);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                return null;
            }
        }
        if (resId != 0) {
            try {
                final android.content.res.Resources r = getResourcesForApplication(appInfo);
                final android.graphics.drawable.Drawable dr = r.getDrawable(resId, null);
                if (dr != null) {
                    putCachedIcon(name, dr);
                }
                if (false) {
                    java.lang.RuntimeException e = new java.lang.RuntimeException("here");
                    e.fillInStackTrace();
                    android.util.Log.w(android.app.ApplicationPackageManager.TAG, (((((("Getting drawable 0x" + java.lang.Integer.toHexString(resId)) + " from package ") + packageName) + ": app scale=") + r.getCompatibilityInfo().applicationScale) + ", caller scale=") + mContext.getResources().getCompatibilityInfo().applicationScale, e);
                }
                if (android.app.ApplicationPackageManager.DEBUG_ICONS) {
                    android.util.Log.v(android.app.ApplicationPackageManager.TAG, (((("Getting drawable 0x" + java.lang.Integer.toHexString(resId)) + " from ") + r) + ": ") + dr);
                }
                return dr;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Log.w("PackageManager", "Failure retrieving resources for " + appInfo.packageName);
            } catch (android.content.res.Resources.NotFoundException e) {
                android.util.Log.w("PackageManager", (("Failure retrieving resources for " + appInfo.packageName) + ": ") + e.getMessage());
            } catch (java.lang.Exception e) {
                // If an exception was thrown, fall through to return
                // default icon.
                android.util.Log.w("PackageManager", (("Failure retrieving icon 0x" + java.lang.Integer.toHexString(resId)) + " in package ") + packageName, e);
            }
        }
        return null;
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getActivityIcon(android.content.ComponentName activityName) throws android.content.pm.PackageManager.NameNotFoundException {
        return getActivityInfo(activityName, android.app.ApplicationPackageManager.sDefaultFlags).loadIcon(this);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getActivityIcon(android.content.Intent intent) throws android.content.pm.PackageManager.NameNotFoundException {
        if (intent.getComponent() != null) {
            return getActivityIcon(intent.getComponent());
        }
        android.content.pm.ResolveInfo info = resolveActivity(intent, android.content.pm.PackageManager.MATCH_DEFAULT_ONLY);
        if (info != null) {
            return info.activityInfo.loadIcon(this);
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(intent.toUri(0));
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getDefaultActivityIcon() {
        return android.content.res.Resources.getSystem().getDrawable(com.android.internal.R.drawable.sym_def_app_icon);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getApplicationIcon(android.content.pm.ApplicationInfo info) {
        return info.loadIcon(this);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getApplicationIcon(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        return getApplicationIcon(getApplicationInfo(packageName, android.app.ApplicationPackageManager.sDefaultFlags));
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getActivityBanner(android.content.ComponentName activityName) throws android.content.pm.PackageManager.NameNotFoundException {
        return getActivityInfo(activityName, android.app.ApplicationPackageManager.sDefaultFlags).loadBanner(this);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getActivityBanner(android.content.Intent intent) throws android.content.pm.PackageManager.NameNotFoundException {
        if (intent.getComponent() != null) {
            return getActivityBanner(intent.getComponent());
        }
        android.content.pm.ResolveInfo info = resolveActivity(intent, android.content.pm.PackageManager.MATCH_DEFAULT_ONLY);
        if (info != null) {
            return info.activityInfo.loadBanner(this);
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(intent.toUri(0));
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getApplicationBanner(android.content.pm.ApplicationInfo info) {
        return info.loadBanner(this);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getApplicationBanner(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        return getApplicationBanner(getApplicationInfo(packageName, android.app.ApplicationPackageManager.sDefaultFlags));
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getActivityLogo(android.content.ComponentName activityName) throws android.content.pm.PackageManager.NameNotFoundException {
        return getActivityInfo(activityName, android.app.ApplicationPackageManager.sDefaultFlags).loadLogo(this);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getActivityLogo(android.content.Intent intent) throws android.content.pm.PackageManager.NameNotFoundException {
        if (intent.getComponent() != null) {
            return getActivityLogo(intent.getComponent());
        }
        android.content.pm.ResolveInfo info = resolveActivity(intent, android.content.pm.PackageManager.MATCH_DEFAULT_ONLY);
        if (info != null) {
            return info.activityInfo.loadLogo(this);
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(intent.toUri(0));
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getApplicationLogo(android.content.pm.ApplicationInfo info) {
        return info.loadLogo(this);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getApplicationLogo(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        return getApplicationLogo(getApplicationInfo(packageName, android.app.ApplicationPackageManager.sDefaultFlags));
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getManagedUserBadgedDrawable(android.graphics.drawable.Drawable drawable, android.graphics.Rect badgeLocation, int badgeDensity) {
        android.graphics.drawable.Drawable badgeDrawable = getDrawableForDensity(com.android.internal.R.drawable.ic_corp_badge, badgeDensity);
        return getBadgedDrawable(drawable, badgeDrawable, badgeLocation, true);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getUserBadgedIcon(android.graphics.drawable.Drawable icon, android.os.UserHandle user) {
        final int badgeResId = getBadgeResIdForUser(user.getIdentifier());
        if (badgeResId == 0) {
            return icon;
        }
        android.graphics.drawable.Drawable badgeIcon = getDrawable("system", badgeResId, null);
        return getBadgedDrawable(icon, badgeIcon, null, true);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getUserBadgedDrawableForDensity(android.graphics.drawable.Drawable drawable, android.os.UserHandle user, android.graphics.Rect badgeLocation, int badgeDensity) {
        android.graphics.drawable.Drawable badgeDrawable = getUserBadgeForDensity(user, badgeDensity);
        if (badgeDrawable == null) {
            return drawable;
        }
        return getBadgedDrawable(drawable, badgeDrawable, badgeLocation, true);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getUserBadgeForDensity(android.os.UserHandle user, int density) {
        return getManagedProfileIconForDensity(user, com.android.internal.R.drawable.ic_corp_badge, density);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getUserBadgeForDensityNoBackground(android.os.UserHandle user, int density) {
        return getManagedProfileIconForDensity(user, com.android.internal.R.drawable.ic_corp_badge_no_background, density);
    }

    private android.graphics.drawable.Drawable getDrawableForDensity(int drawableId, int density) {
        if (density <= 0) {
            density = mContext.getResources().getDisplayMetrics().densityDpi;
        }
        return android.content.res.Resources.getSystem().getDrawableForDensity(drawableId, density);
    }

    private android.graphics.drawable.Drawable getManagedProfileIconForDensity(android.os.UserHandle user, int drawableId, int density) {
        if (isManagedProfile(user.getIdentifier())) {
            return getDrawableForDensity(drawableId, density);
        }
        return null;
    }

    @java.lang.Override
    public java.lang.CharSequence getUserBadgedLabel(java.lang.CharSequence label, android.os.UserHandle user) {
        if (isManagedProfile(user.getIdentifier())) {
            return android.content.res.Resources.getSystem().getString(com.android.internal.R.string.managed_profile_label_badge, label);
        }
        return label;
    }

    @java.lang.Override
    public android.content.res.Resources getResourcesForActivity(android.content.ComponentName activityName) throws android.content.pm.PackageManager.NameNotFoundException {
        return getResourcesForApplication(getActivityInfo(activityName, android.app.ApplicationPackageManager.sDefaultFlags).applicationInfo);
    }

    @java.lang.Override
    public android.content.res.Resources getResourcesForApplication(@android.annotation.NonNull
    android.content.pm.ApplicationInfo app) throws android.content.pm.PackageManager.NameNotFoundException {
        if (app.packageName.equals("system")) {
            return mContext.mMainThread.getSystemContext().getResources();
        }
        final boolean sameUid = app.uid == android.os.Process.myUid();
        final android.content.res.Resources r = mContext.mMainThread.getTopLevelResources(sameUid ? app.sourceDir : app.publicSourceDir, sameUid ? app.splitSourceDirs : app.splitPublicSourceDirs, app.resourceDirs, app.sharedLibraryFiles, android.view.Display.DEFAULT_DISPLAY, mContext.mPackageInfo);
        if (r != null) {
            return r;
        }
        throw new android.content.pm.PackageManager.NameNotFoundException("Unable to open " + app.publicSourceDir);
    }

    @java.lang.Override
    public android.content.res.Resources getResourcesForApplication(java.lang.String appPackageName) throws android.content.pm.PackageManager.NameNotFoundException {
        return getResourcesForApplication(getApplicationInfo(appPackageName, android.app.ApplicationPackageManager.sDefaultFlags));
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.res.Resources getResourcesForApplicationAsUser(java.lang.String appPackageName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        if (userId < 0) {
            throw new java.lang.IllegalArgumentException("Call does not support special user #" + userId);
        }
        if ("system".equals(appPackageName)) {
            return mContext.mMainThread.getSystemContext().getResources();
        }
        try {
            android.content.pm.ApplicationInfo ai = mPM.getApplicationInfo(appPackageName, android.app.ApplicationPackageManager.sDefaultFlags, userId);
            if (ai != null) {
                return getResourcesForApplication(ai);
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(("Package " + appPackageName) + " doesn't exist");
    }

    volatile int mCachedSafeMode = -1;

    @java.lang.Override
    public boolean isSafeMode() {
        try {
            if (mCachedSafeMode < 0) {
                mCachedSafeMode = (mPM.isSafeMode()) ? 1 : 0;
            }
            return mCachedSafeMode != 0;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void addOnPermissionsChangeListener(android.content.pm.PackageManager.OnPermissionsChangedListener listener) {
        synchronized(mPermissionListeners) {
            if (mPermissionListeners.get(listener) != null) {
                return;
            }
            android.app.ApplicationPackageManager.OnPermissionsChangeListenerDelegate delegate = new android.app.ApplicationPackageManager.OnPermissionsChangeListenerDelegate(listener, android.os.Looper.getMainLooper());
            try {
                mPM.addOnPermissionsChangeListener(delegate);
                mPermissionListeners.put(listener, delegate);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @java.lang.Override
    public void removeOnPermissionsChangeListener(android.content.pm.PackageManager.OnPermissionsChangedListener listener) {
        synchronized(mPermissionListeners) {
            android.content.pm.IOnPermissionsChangeListener delegate = mPermissionListeners.get(listener);
            if (delegate != null) {
                try {
                    mPM.removeOnPermissionsChangeListener(delegate);
                    mPermissionListeners.remove(listener);
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    static void configurationChanged() {
        synchronized(android.app.ApplicationPackageManager.sSync) {
            android.app.ApplicationPackageManager.sIconCache.clear();
            android.app.ApplicationPackageManager.sStringCache.clear();
        }
    }

    ApplicationPackageManager(android.app.ContextImpl context, android.content.pm.IPackageManager pm) {
        mContext = context;
        mPM = pm;
    }

    @android.annotation.Nullable
    private android.graphics.drawable.Drawable getCachedIcon(@android.annotation.NonNull
    android.app.ApplicationPackageManager.ResourceName name) {
        synchronized(android.app.ApplicationPackageManager.sSync) {
            final java.lang.ref.WeakReference<android.graphics.drawable.Drawable.ConstantState> wr = android.app.ApplicationPackageManager.sIconCache.get(name);
            if (android.app.ApplicationPackageManager.DEBUG_ICONS)
                android.util.Log.v(android.app.ApplicationPackageManager.TAG, (("Get cached weak drawable ref for " + name) + ": ") + wr);

            if (wr != null) {
                // we have the activity
                final android.graphics.drawable.Drawable.ConstantState state = wr.get();
                if (state != null) {
                    if (android.app.ApplicationPackageManager.DEBUG_ICONS) {
                        android.util.Log.v(android.app.ApplicationPackageManager.TAG, (("Get cached drawable state for " + name) + ": ") + state);
                    }
                    // Note: It's okay here to not use the newDrawable(Resources) variant
                    // of the API. The ConstantState comes from a drawable that was
                    // originally created by passing the proper app Resources instance
                    // which means the state should already contain the proper
                    // resources specific information (like density.) See
                    // BitmapDrawable.BitmapState for instance.
                    return state.newDrawable();
                }
                // our entry has been purged
                android.app.ApplicationPackageManager.sIconCache.remove(name);
            }
        }
        return null;
    }

    private void putCachedIcon(@android.annotation.NonNull
    android.app.ApplicationPackageManager.ResourceName name, @android.annotation.NonNull
    android.graphics.drawable.Drawable dr) {
        synchronized(android.app.ApplicationPackageManager.sSync) {
            android.app.ApplicationPackageManager.sIconCache.put(name, new java.lang.ref.WeakReference<>(dr.getConstantState()));
            if (android.app.ApplicationPackageManager.DEBUG_ICONS)
                android.util.Log.v(android.app.ApplicationPackageManager.TAG, (("Added cached drawable state for " + name) + ": ") + dr);

        }
    }

    static void handlePackageBroadcast(int cmd, java.lang.String[] pkgList, boolean hasPkgInfo) {
        boolean immediateGc = false;
        if (cmd == android.app.IApplicationThread.EXTERNAL_STORAGE_UNAVAILABLE) {
            immediateGc = true;
        }
        if ((pkgList != null) && (pkgList.length > 0)) {
            boolean needCleanup = false;
            for (java.lang.String ssp : pkgList) {
                synchronized(android.app.ApplicationPackageManager.sSync) {
                    for (int i = android.app.ApplicationPackageManager.sIconCache.size() - 1; i >= 0; i--) {
                        android.app.ApplicationPackageManager.ResourceName nm = android.app.ApplicationPackageManager.sIconCache.keyAt(i);
                        if (nm.packageName.equals(ssp)) {
                            // Log.i(TAG, "Removing cached drawable for " + nm);
                            android.app.ApplicationPackageManager.sIconCache.removeAt(i);
                            needCleanup = true;
                        }
                    }
                    for (int i = android.app.ApplicationPackageManager.sStringCache.size() - 1; i >= 0; i--) {
                        android.app.ApplicationPackageManager.ResourceName nm = android.app.ApplicationPackageManager.sStringCache.keyAt(i);
                        if (nm.packageName.equals(ssp)) {
                            // Log.i(TAG, "Removing cached string for " + nm);
                            android.app.ApplicationPackageManager.sStringCache.removeAt(i);
                            needCleanup = true;
                        }
                    }
                }
            }
            if (needCleanup || hasPkgInfo) {
                if (immediateGc) {
                    // Schedule an immediate gc.
                    java.lang.Runtime.getRuntime().gc();
                } else {
                    android.app.ActivityThread.currentActivityThread().scheduleGcIdler();
                }
            }
        }
    }

    private static final class ResourceName {
        final java.lang.String packageName;

        final int iconId;

        ResourceName(java.lang.String _packageName, int _iconId) {
            packageName = _packageName;
            iconId = _iconId;
        }

        ResourceName(android.content.pm.ApplicationInfo aInfo, int _iconId) {
            this(aInfo.packageName, _iconId);
        }

        ResourceName(android.content.pm.ComponentInfo cInfo, int _iconId) {
            this(cInfo.applicationInfo.packageName, _iconId);
        }

        ResourceName(android.content.pm.ResolveInfo rInfo, int _iconId) {
            this(rInfo.activityInfo.applicationInfo.packageName, _iconId);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            android.app.ApplicationPackageManager.ResourceName that = ((android.app.ApplicationPackageManager.ResourceName) (o));
            if (iconId != that.iconId)
                return false;

            return !(packageName != null ? !packageName.equals(that.packageName) : that.packageName != null);
        }

        @java.lang.Override
        public int hashCode() {
            int result;
            result = packageName.hashCode();
            result = (31 * result) + iconId;
            return result;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((("{ResourceName " + packageName) + " / ") + iconId) + "}";
        }
    }

    private java.lang.CharSequence getCachedString(android.app.ApplicationPackageManager.ResourceName name) {
        synchronized(android.app.ApplicationPackageManager.sSync) {
            java.lang.ref.WeakReference<java.lang.CharSequence> wr = android.app.ApplicationPackageManager.sStringCache.get(name);
            if (wr != null) {
                // we have the activity
                java.lang.CharSequence cs = wr.get();
                if (cs != null) {
                    return cs;
                }
                // our entry has been purged
                android.app.ApplicationPackageManager.sStringCache.remove(name);
            }
        }
        return null;
    }

    private void putCachedString(android.app.ApplicationPackageManager.ResourceName name, java.lang.CharSequence cs) {
        synchronized(android.app.ApplicationPackageManager.sSync) {
            android.app.ApplicationPackageManager.sStringCache.put(name, new java.lang.ref.WeakReference<java.lang.CharSequence>(cs));
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getText(java.lang.String packageName, @android.annotation.StringRes
    int resid, android.content.pm.ApplicationInfo appInfo) {
        android.app.ApplicationPackageManager.ResourceName name = new android.app.ApplicationPackageManager.ResourceName(packageName, resid);
        java.lang.CharSequence text = getCachedString(name);
        if (text != null) {
            return text;
        }
        if (appInfo == null) {
            try {
                appInfo = getApplicationInfo(packageName, android.app.ApplicationPackageManager.sDefaultFlags);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                return null;
            }
        }
        try {
            android.content.res.Resources r = getResourcesForApplication(appInfo);
            text = r.getText(resid);
            putCachedString(name, text);
            return text;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.w("PackageManager", "Failure retrieving resources for " + appInfo.packageName);
        } catch (java.lang.RuntimeException e) {
            // If an exception was thrown, fall through to return
            // default icon.
            android.util.Log.w("PackageManager", (("Failure retrieving text 0x" + java.lang.Integer.toHexString(resid)) + " in package ") + packageName, e);
        }
        return null;
    }

    @java.lang.Override
    public android.content.res.XmlResourceParser getXml(java.lang.String packageName, @android.annotation.XmlRes
    int resid, android.content.pm.ApplicationInfo appInfo) {
        if (appInfo == null) {
            try {
                appInfo = getApplicationInfo(packageName, android.app.ApplicationPackageManager.sDefaultFlags);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                return null;
            }
        }
        try {
            android.content.res.Resources r = getResourcesForApplication(appInfo);
            return r.getXml(resid);
        } catch (java.lang.RuntimeException e) {
            // If an exception was thrown, fall through to return
            // default icon.
            android.util.Log.w("PackageManager", (("Failure retrieving xml 0x" + java.lang.Integer.toHexString(resid)) + " in package ") + packageName, e);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.w("PackageManager", "Failure retrieving resources for " + appInfo.packageName);
        }
        return null;
    }

    @java.lang.Override
    public java.lang.CharSequence getApplicationLabel(android.content.pm.ApplicationInfo info) {
        return info.loadLabel(this);
    }

    @java.lang.Override
    public void installPackage(android.net.Uri packageURI, android.content.pm.IPackageInstallObserver observer, int flags, java.lang.String installerPackageName) {
        installCommon(packageURI, new android.content.pm.PackageManager.LegacyPackageInstallObserver(observer), flags, installerPackageName, mContext.getUserId());
    }

    @java.lang.Override
    public void installPackage(android.net.Uri packageURI, android.app.PackageInstallObserver observer, int flags, java.lang.String installerPackageName) {
        installCommon(packageURI, observer, flags, installerPackageName, mContext.getUserId());
    }

    private void installCommon(android.net.Uri packageURI, android.app.PackageInstallObserver observer, int flags, java.lang.String installerPackageName, int userId) {
        if (!"file".equals(packageURI.getScheme())) {
            throw new java.lang.UnsupportedOperationException("Only file:// URIs are supported");
        }
        final java.lang.String originPath = packageURI.getPath();
        try {
            mPM.installPackageAsUser(originPath, observer.getBinder(), flags, installerPackageName, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public int installExistingPackage(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        return installExistingPackageAsUser(packageName, mContext.getUserId());
    }

    @java.lang.Override
    public int installExistingPackageAsUser(java.lang.String packageName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            int res = mPM.installExistingPackageAsUser(packageName, userId);
            if (res == android.content.pm.PackageManager.INSTALL_FAILED_INVALID_URI) {
                throw new android.content.pm.PackageManager.NameNotFoundException(("Package " + packageName) + " doesn't exist");
            }
            return res;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void verifyPendingInstall(int id, int response) {
        try {
            mPM.verifyPendingInstall(id, response);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void extendVerificationTimeout(int id, int verificationCodeAtTimeout, long millisecondsToDelay) {
        try {
            mPM.extendVerificationTimeout(id, verificationCodeAtTimeout, millisecondsToDelay);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void verifyIntentFilter(int id, int verificationCode, java.util.List<java.lang.String> failedDomains) {
        try {
            mPM.verifyIntentFilter(id, verificationCode, failedDomains);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public int getIntentVerificationStatusAsUser(java.lang.String packageName, int userId) {
        try {
            return mPM.getIntentVerificationStatus(packageName, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public boolean updateIntentVerificationStatusAsUser(java.lang.String packageName, int status, int userId) {
        try {
            return mPM.updateIntentVerificationStatus(packageName, status, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public java.util.List<android.content.pm.IntentFilterVerificationInfo> getIntentFilterVerifications(java.lang.String packageName) {
        try {
            android.content.pm.ParceledListSlice<android.content.pm.IntentFilterVerificationInfo> parceledList = mPM.getIntentFilterVerifications(packageName);
            if (parceledList == null) {
                return java.util.Collections.emptyList();
            }
            return parceledList.getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public java.util.List<android.content.IntentFilter> getAllIntentFilters(java.lang.String packageName) {
        try {
            android.content.pm.ParceledListSlice<android.content.IntentFilter> parceledList = mPM.getAllIntentFilters(packageName);
            if (parceledList == null) {
                return java.util.Collections.emptyList();
            }
            return parceledList.getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public java.lang.String getDefaultBrowserPackageNameAsUser(int userId) {
        try {
            return mPM.getDefaultBrowserPackageName(userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public boolean setDefaultBrowserPackageNameAsUser(java.lang.String packageName, int userId) {
        try {
            return mPM.setDefaultBrowserPackageName(packageName, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void setInstallerPackageName(java.lang.String targetPackage, java.lang.String installerPackageName) {
        try {
            mPM.setInstallerPackageName(targetPackage, installerPackageName);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public java.lang.String getInstallerPackageName(java.lang.String packageName) {
        try {
            return mPM.getInstallerPackageName(packageName);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public int getMoveStatus(int moveId) {
        try {
            return mPM.getMoveStatus(moveId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void registerMoveCallback(android.content.pm.PackageManager.MoveCallback callback, android.os.Handler handler) {
        synchronized(mDelegates) {
            final android.app.ApplicationPackageManager.MoveCallbackDelegate delegate = new android.app.ApplicationPackageManager.MoveCallbackDelegate(callback, handler.getLooper());
            try {
                mPM.registerMoveCallback(delegate);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
            mDelegates.add(delegate);
        }
    }

    @java.lang.Override
    public void unregisterMoveCallback(android.content.pm.PackageManager.MoveCallback callback) {
        synchronized(mDelegates) {
            for (java.util.Iterator<android.app.ApplicationPackageManager.MoveCallbackDelegate> i = mDelegates.iterator(); i.hasNext();) {
                final android.app.ApplicationPackageManager.MoveCallbackDelegate delegate = i.next();
                if (delegate.mCallback == callback) {
                    try {
                        mPM.unregisterMoveCallback(delegate);
                    } catch (android.os.RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                    i.remove();
                }
            }
        }
    }

    @java.lang.Override
    public int movePackage(java.lang.String packageName, android.os.storage.VolumeInfo vol) {
        try {
            final java.lang.String volumeUuid;
            if (android.os.storage.VolumeInfo.ID_PRIVATE_INTERNAL.equals(vol.id)) {
                volumeUuid = android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL;
            } else
                if (vol.isPrimaryPhysical()) {
                    volumeUuid = android.os.storage.StorageManager.UUID_PRIMARY_PHYSICAL;
                } else {
                    volumeUuid = com.android.internal.util.Preconditions.checkNotNull(vol.fsUuid);
                }

            return mPM.movePackage(packageName, volumeUuid);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.os.storage.VolumeInfo getPackageCurrentVolume(android.content.pm.ApplicationInfo app) {
        final android.os.storage.StorageManager storage = mContext.getSystemService(android.os.storage.StorageManager.class);
        if (app.isInternal()) {
            return storage.findVolumeById(android.os.storage.VolumeInfo.ID_PRIVATE_INTERNAL);
        } else
            if (app.isExternalAsec()) {
                return storage.getPrimaryPhysicalVolume();
            } else {
                return storage.findVolumeByUuid(app.volumeUuid);
            }

    }

    @java.lang.Override
    @android.annotation.NonNull
    public java.util.List<android.os.storage.VolumeInfo> getPackageCandidateVolumes(android.content.pm.ApplicationInfo app) {
        final android.os.storage.StorageManager storage = mContext.getSystemService(android.os.storage.StorageManager.class);
        final android.os.storage.VolumeInfo currentVol = getPackageCurrentVolume(app);
        final java.util.List<android.os.storage.VolumeInfo> vols = storage.getVolumes();
        final java.util.List<android.os.storage.VolumeInfo> candidates = new java.util.ArrayList<>();
        for (android.os.storage.VolumeInfo vol : vols) {
            if (java.util.Objects.equals(vol, currentVol) || isPackageCandidateVolume(mContext, app, vol)) {
                candidates.add(vol);
            }
        }
        return candidates;
    }

    private boolean isPackageCandidateVolume(android.app.ContextImpl context, android.content.pm.ApplicationInfo app, android.os.storage.VolumeInfo vol) {
        final boolean forceAllowOnExternal = android.provider.Settings.Global.getInt(context.getContentResolver(), android.provider.Settings.Global.FORCE_ALLOW_ON_EXTERNAL, 0) != 0;
        // Private internal is always an option
        if (android.os.storage.VolumeInfo.ID_PRIVATE_INTERNAL.equals(vol.getId())) {
            return true;
        }
        // System apps and apps demanding internal storage can't be moved
        // anywhere else
        if (app.isSystemApp()) {
            return false;
        }
        if ((!forceAllowOnExternal) && ((app.installLocation == android.content.pm.PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY) || (app.installLocation == android.content.pm.PackageInfo.INSTALL_LOCATION_UNSPECIFIED))) {
            return false;
        }
        // Gotta be able to write there
        if (!vol.isMountedWritable()) {
            return false;
        }
        // Moving into an ASEC on public primary is only option internal
        if (vol.isPrimaryPhysical()) {
            return app.isInternal();
        }
        // Some apps can't be moved. (e.g. device admins)
        try {
            if (mPM.isPackageDeviceAdminOnAnyUser(app.packageName)) {
                return false;
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        // Otherwise we can move to any private volume
        return vol.getType() == android.os.storage.VolumeInfo.TYPE_PRIVATE;
    }

    @java.lang.Override
    public int movePrimaryStorage(android.os.storage.VolumeInfo vol) {
        try {
            final java.lang.String volumeUuid;
            if (android.os.storage.VolumeInfo.ID_PRIVATE_INTERNAL.equals(vol.id)) {
                volumeUuid = android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL;
            } else
                if (vol.isPrimaryPhysical()) {
                    volumeUuid = android.os.storage.StorageManager.UUID_PRIMARY_PHYSICAL;
                } else {
                    volumeUuid = com.android.internal.util.Preconditions.checkNotNull(vol.fsUuid);
                }

            return mPM.movePrimaryStorage(volumeUuid);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.os.storage.VolumeInfo getPrimaryStorageCurrentVolume() {
        final android.os.storage.StorageManager storage = mContext.getSystemService(android.os.storage.StorageManager.class);
        final java.lang.String volumeUuid = storage.getPrimaryStorageUuid();
        return storage.findVolumeByQualifiedUuid(volumeUuid);
    }

    @java.lang.Override
    @android.annotation.NonNull
    public java.util.List<android.os.storage.VolumeInfo> getPrimaryStorageCandidateVolumes() {
        final android.os.storage.StorageManager storage = mContext.getSystemService(android.os.storage.StorageManager.class);
        final android.os.storage.VolumeInfo currentVol = getPrimaryStorageCurrentVolume();
        final java.util.List<android.os.storage.VolumeInfo> vols = storage.getVolumes();
        final java.util.List<android.os.storage.VolumeInfo> candidates = new java.util.ArrayList<>();
        if (java.util.Objects.equals(android.os.storage.StorageManager.UUID_PRIMARY_PHYSICAL, storage.getPrimaryStorageUuid()) && (currentVol != null)) {
            // TODO: support moving primary physical to emulated volume
            candidates.add(currentVol);
        } else {
            for (android.os.storage.VolumeInfo vol : vols) {
                if (java.util.Objects.equals(vol, currentVol) || android.app.ApplicationPackageManager.isPrimaryStorageCandidateVolume(vol)) {
                    candidates.add(vol);
                }
            }
        }
        return candidates;
    }

    private static boolean isPrimaryStorageCandidateVolume(android.os.storage.VolumeInfo vol) {
        // Private internal is always an option
        if (android.os.storage.VolumeInfo.ID_PRIVATE_INTERNAL.equals(vol.getId())) {
            return true;
        }
        // Gotta be able to write there
        if (!vol.isMountedWritable()) {
            return false;
        }
        // We can move to any private volume
        return vol.getType() == android.os.storage.VolumeInfo.TYPE_PRIVATE;
    }

    @java.lang.Override
    public void deletePackage(java.lang.String packageName, android.content.pm.IPackageDeleteObserver observer, int flags) {
        deletePackageAsUser(packageName, observer, flags, mContext.getUserId());
    }

    @java.lang.Override
    public void deletePackageAsUser(java.lang.String packageName, android.content.pm.IPackageDeleteObserver observer, int flags, int userId) {
        try {
            mPM.deletePackageAsUser(packageName, observer, userId, flags);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void clearApplicationUserData(java.lang.String packageName, android.content.pm.IPackageDataObserver observer) {
        try {
            mPM.clearApplicationUserData(packageName, observer, mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void deleteApplicationCacheFiles(java.lang.String packageName, android.content.pm.IPackageDataObserver observer) {
        try {
            mPM.deleteApplicationCacheFiles(packageName, observer);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void deleteApplicationCacheFilesAsUser(java.lang.String packageName, int userId, android.content.pm.IPackageDataObserver observer) {
        try {
            mPM.deleteApplicationCacheFilesAsUser(packageName, userId, observer);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void freeStorageAndNotify(java.lang.String volumeUuid, long idealStorageSize, android.content.pm.IPackageDataObserver observer) {
        try {
            mPM.freeStorageAndNotify(volumeUuid, idealStorageSize, observer);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void freeStorage(java.lang.String volumeUuid, long freeStorageSize, android.content.IntentSender pi) {
        try {
            mPM.freeStorage(volumeUuid, freeStorageSize, pi);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public java.lang.String[] setPackagesSuspendedAsUser(java.lang.String[] packageNames, boolean suspended, int userId) {
        try {
            return mPM.setPackagesSuspendedAsUser(packageNames, suspended, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public boolean isPackageSuspendedForUser(java.lang.String packageName, int userId) {
        try {
            return mPM.isPackageSuspendedForUser(packageName, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void getPackageSizeInfoAsUser(java.lang.String packageName, int userHandle, android.content.pm.IPackageStatsObserver observer) {
        try {
            mPM.getPackageSizeInfo(packageName, userHandle, observer);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void addPackageToPreferred(java.lang.String packageName) {
        android.util.Log.w(android.app.ApplicationPackageManager.TAG, "addPackageToPreferred() is a no-op");
    }

    @java.lang.Override
    public void removePackageFromPreferred(java.lang.String packageName) {
        android.util.Log.w(android.app.ApplicationPackageManager.TAG, "removePackageFromPreferred() is a no-op");
    }

    @java.lang.Override
    public java.util.List<android.content.pm.PackageInfo> getPreferredPackages(int flags) {
        android.util.Log.w(android.app.ApplicationPackageManager.TAG, "getPreferredPackages() is a no-op");
        return java.util.Collections.emptyList();
    }

    @java.lang.Override
    public void addPreferredActivity(android.content.IntentFilter filter, int match, android.content.ComponentName[] set, android.content.ComponentName activity) {
        try {
            mPM.addPreferredActivity(filter, match, set, activity, mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void addPreferredActivityAsUser(android.content.IntentFilter filter, int match, android.content.ComponentName[] set, android.content.ComponentName activity, int userId) {
        try {
            mPM.addPreferredActivity(filter, match, set, activity, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void replacePreferredActivity(android.content.IntentFilter filter, int match, android.content.ComponentName[] set, android.content.ComponentName activity) {
        try {
            mPM.replacePreferredActivity(filter, match, set, activity, mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void replacePreferredActivityAsUser(android.content.IntentFilter filter, int match, android.content.ComponentName[] set, android.content.ComponentName activity, int userId) {
        try {
            mPM.replacePreferredActivity(filter, match, set, activity, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void clearPackagePreferredActivities(java.lang.String packageName) {
        try {
            mPM.clearPackagePreferredActivities(packageName);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public int getPreferredActivities(java.util.List<android.content.IntentFilter> outFilters, java.util.List<android.content.ComponentName> outActivities, java.lang.String packageName) {
        try {
            return mPM.getPreferredActivities(outFilters, outActivities, packageName);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public android.content.ComponentName getHomeActivities(java.util.List<android.content.pm.ResolveInfo> outActivities) {
        try {
            return mPM.getHomeActivities(outActivities);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void setComponentEnabledSetting(android.content.ComponentName componentName, int newState, int flags) {
        try {
            mPM.setComponentEnabledSetting(componentName, newState, flags, mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public int getComponentEnabledSetting(android.content.ComponentName componentName) {
        try {
            return mPM.getComponentEnabledSetting(componentName, mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void setApplicationEnabledSetting(java.lang.String packageName, int newState, int flags) {
        try {
            mPM.setApplicationEnabledSetting(packageName, newState, flags, mContext.getUserId(), mContext.getOpPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public int getApplicationEnabledSetting(java.lang.String packageName) {
        try {
            return mPM.getApplicationEnabledSetting(packageName, mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void flushPackageRestrictionsAsUser(int userId) {
        try {
            mPM.flushPackageRestrictionsAsUser(userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public boolean setApplicationHiddenSettingAsUser(java.lang.String packageName, boolean hidden, android.os.UserHandle user) {
        try {
            return mPM.setApplicationHiddenSettingAsUser(packageName, hidden, user.getIdentifier());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public boolean getApplicationHiddenSettingAsUser(java.lang.String packageName, android.os.UserHandle user) {
        try {
            return mPM.getApplicationHiddenSettingAsUser(packageName, user.getIdentifier());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.pm.KeySet getKeySetByAlias(java.lang.String packageName, java.lang.String alias) {
        com.android.internal.util.Preconditions.checkNotNull(packageName);
        com.android.internal.util.Preconditions.checkNotNull(alias);
        try {
            return mPM.getKeySetByAlias(packageName, alias);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.pm.KeySet getSigningKeySet(java.lang.String packageName) {
        com.android.internal.util.Preconditions.checkNotNull(packageName);
        try {
            return mPM.getSigningKeySet(packageName);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean isSignedBy(java.lang.String packageName, android.content.pm.KeySet ks) {
        com.android.internal.util.Preconditions.checkNotNull(packageName);
        com.android.internal.util.Preconditions.checkNotNull(ks);
        try {
            return mPM.isPackageSignedByKeySet(packageName, ks);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean isSignedByExactly(java.lang.String packageName, android.content.pm.KeySet ks) {
        com.android.internal.util.Preconditions.checkNotNull(packageName);
        com.android.internal.util.Preconditions.checkNotNull(ks);
        try {
            return mPM.isPackageSignedByKeySetExactly(packageName, ks);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.pm.VerifierDeviceIdentity getVerifierDeviceIdentity() {
        try {
            return mPM.getVerifierDeviceIdentity();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean isUpgrade() {
        try {
            return mPM.isUpgrade();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public android.content.pm.PackageInstaller getPackageInstaller() {
        synchronized(mLock) {
            if (mInstaller == null) {
                try {
                    mInstaller = new android.content.pm.PackageInstaller(mContext, this, mPM.getPackageInstaller(), mContext.getPackageName(), mContext.getUserId());
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            return mInstaller;
        }
    }

    @java.lang.Override
    public boolean isPackageAvailable(java.lang.String packageName) {
        try {
            return mPM.isPackageAvailable(packageName, mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void addCrossProfileIntentFilter(android.content.IntentFilter filter, int sourceUserId, int targetUserId, int flags) {
        try {
            mPM.addCrossProfileIntentFilter(filter, mContext.getOpPackageName(), sourceUserId, targetUserId, flags);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void clearCrossProfileIntentFilters(int sourceUserId) {
        try {
            mPM.clearCrossProfileIntentFilters(sourceUserId, mContext.getOpPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public android.graphics.drawable.Drawable loadItemIcon(android.content.pm.PackageItemInfo itemInfo, android.content.pm.ApplicationInfo appInfo) {
        android.graphics.drawable.Drawable dr = loadUnbadgedItemIcon(itemInfo, appInfo);
        if (itemInfo.showUserIcon != android.os.UserHandle.USER_NULL) {
            return dr;
        }
        return getUserBadgedIcon(dr, new android.os.UserHandle(mContext.getUserId()));
    }

    /**
     *
     *
     * @unknown 
     */
    public android.graphics.drawable.Drawable loadUnbadgedItemIcon(android.content.pm.PackageItemInfo itemInfo, android.content.pm.ApplicationInfo appInfo) {
        if (itemInfo.showUserIcon != android.os.UserHandle.USER_NULL) {
            android.graphics.Bitmap bitmap = getUserManager().getUserIcon(itemInfo.showUserIcon);
            if (bitmap == null) {
                return /* light= */
                com.android.internal.util.UserIcons.getDefaultUserIcon(itemInfo.showUserIcon, false);
            }
            return new android.graphics.drawable.BitmapDrawable(bitmap);
        }
        android.graphics.drawable.Drawable dr = null;
        if (itemInfo.packageName != null) {
            dr = getDrawable(itemInfo.packageName, itemInfo.icon, appInfo);
        }
        if (dr == null) {
            dr = itemInfo.loadDefaultIcon(this);
        }
        return dr;
    }

    private android.graphics.drawable.Drawable getBadgedDrawable(android.graphics.drawable.Drawable drawable, android.graphics.drawable.Drawable badgeDrawable, android.graphics.Rect badgeLocation, boolean tryBadgeInPlace) {
        final int badgedWidth = drawable.getIntrinsicWidth();
        final int badgedHeight = drawable.getIntrinsicHeight();
        final boolean canBadgeInPlace = (tryBadgeInPlace && (drawable instanceof android.graphics.drawable.BitmapDrawable)) && ((android.graphics.drawable.BitmapDrawable) (drawable)).getBitmap().isMutable();
        final android.graphics.Bitmap bitmap;
        if (canBadgeInPlace) {
            bitmap = ((android.graphics.drawable.BitmapDrawable) (drawable)).getBitmap();
        } else {
            bitmap = android.graphics.Bitmap.createBitmap(badgedWidth, badgedHeight, android.graphics.Bitmap.Config.ARGB_8888);
        }
        android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
        if (!canBadgeInPlace) {
            drawable.setBounds(0, 0, badgedWidth, badgedHeight);
            drawable.draw(canvas);
        }
        if (badgeLocation != null) {
            if ((((badgeLocation.left < 0) || (badgeLocation.top < 0)) || (badgeLocation.width() > badgedWidth)) || (badgeLocation.height() > badgedHeight)) {
                throw new java.lang.IllegalArgumentException((("Badge location " + badgeLocation) + " not in badged drawable bounds ") + new android.graphics.Rect(0, 0, badgedWidth, badgedHeight));
            }
            badgeDrawable.setBounds(0, 0, badgeLocation.width(), badgeLocation.height());
            canvas.save();
            canvas.translate(badgeLocation.left, badgeLocation.top);
            badgeDrawable.draw(canvas);
            canvas.restore();
        } else {
            badgeDrawable.setBounds(0, 0, badgedWidth, badgedHeight);
            badgeDrawable.draw(canvas);
        }
        if (!canBadgeInPlace) {
            android.graphics.drawable.BitmapDrawable mergedDrawable = new android.graphics.drawable.BitmapDrawable(mContext.getResources(), bitmap);
            if (drawable instanceof android.graphics.drawable.BitmapDrawable) {
                android.graphics.drawable.BitmapDrawable bitmapDrawable = ((android.graphics.drawable.BitmapDrawable) (drawable));
                mergedDrawable.setTargetDensity(bitmapDrawable.getBitmap().getDensity());
            }
            return mergedDrawable;
        }
        return drawable;
    }

    private int getBadgeResIdForUser(int userId) {
        // Return the framework-provided badge.
        if (isManagedProfile(userId)) {
            return com.android.internal.R.drawable.ic_corp_icon_badge;
        }
        return 0;
    }

    private boolean isManagedProfile(int userId) {
        return getUserManager().isManagedProfile(userId);
    }

    /**
     * {@hide }
     */
    private static class MoveCallbackDelegate extends android.content.pm.IPackageMoveObserver.Stub implements android.os.Handler.Callback {
        private static final int MSG_CREATED = 1;

        private static final int MSG_STATUS_CHANGED = 2;

        final android.content.pm.PackageManager.MoveCallback mCallback;

        final android.os.Handler mHandler;

        public MoveCallbackDelegate(android.content.pm.PackageManager.MoveCallback callback, android.os.Looper looper) {
            mCallback = callback;
            mHandler = new android.os.Handler(looper, this);
        }

        @java.lang.Override
        public boolean handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.app.ApplicationPackageManager.MoveCallbackDelegate.MSG_CREATED :
                    {
                        final com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        mCallback.onCreated(args.argi1, ((android.os.Bundle) (args.arg2)));
                        args.recycle();
                        return true;
                    }
                case android.app.ApplicationPackageManager.MoveCallbackDelegate.MSG_STATUS_CHANGED :
                    {
                        final com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        mCallback.onStatusChanged(args.argi1, args.argi2, ((long) (args.arg3)));
                        args.recycle();
                        return true;
                    }
            }
            return false;
        }

        @java.lang.Override
        public void onCreated(int moveId, android.os.Bundle extras) {
            final com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.argi1 = moveId;
            args.arg2 = extras;
            mHandler.obtainMessage(android.app.ApplicationPackageManager.MoveCallbackDelegate.MSG_CREATED, args).sendToTarget();
        }

        @java.lang.Override
        public void onStatusChanged(int moveId, int status, long estMillis) {
            final com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.argi1 = moveId;
            args.argi2 = status;
            args.arg3 = estMillis;
            mHandler.obtainMessage(android.app.ApplicationPackageManager.MoveCallbackDelegate.MSG_STATUS_CHANGED, args).sendToTarget();
        }
    }

    private final android.app.ContextImpl mContext;

    private final android.content.pm.IPackageManager mPM;

    private static final java.lang.Object sSync = new java.lang.Object();

    private static android.util.ArrayMap<android.app.ApplicationPackageManager.ResourceName, java.lang.ref.WeakReference<android.graphics.drawable.Drawable.ConstantState>> sIconCache = new android.util.ArrayMap<android.app.ApplicationPackageManager.ResourceName, java.lang.ref.WeakReference<android.graphics.drawable.Drawable.ConstantState>>();

    private static android.util.ArrayMap<android.app.ApplicationPackageManager.ResourceName, java.lang.ref.WeakReference<java.lang.CharSequence>> sStringCache = new android.util.ArrayMap<android.app.ApplicationPackageManager.ResourceName, java.lang.ref.WeakReference<java.lang.CharSequence>>();

    private final java.util.Map<android.content.pm.PackageManager.OnPermissionsChangedListener, android.content.pm.IOnPermissionsChangeListener> mPermissionListeners = new android.util.ArrayMap<>();

    public class OnPermissionsChangeListenerDelegate extends android.content.pm.IOnPermissionsChangeListener.Stub implements android.os.Handler.Callback {
        private static final int MSG_PERMISSIONS_CHANGED = 1;

        private final android.content.pm.PackageManager.OnPermissionsChangedListener mListener;

        private final android.os.Handler mHandler;

        public OnPermissionsChangeListenerDelegate(android.content.pm.PackageManager.OnPermissionsChangedListener listener, android.os.Looper looper) {
            mListener = listener;
            mHandler = new android.os.Handler(looper, this);
        }

        @java.lang.Override
        public void onPermissionsChanged(int uid) {
            mHandler.obtainMessage(android.app.ApplicationPackageManager.OnPermissionsChangeListenerDelegate.MSG_PERMISSIONS_CHANGED, uid, 0).sendToTarget();
        }

        @java.lang.Override
        public boolean handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.app.ApplicationPackageManager.OnPermissionsChangeListenerDelegate.MSG_PERMISSIONS_CHANGED :
                    {
                        final int uid = msg.arg1;
                        mListener.onPermissionsChanged(uid);
                        return true;
                    }
            }
            return false;
        }
    }
}

