/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.test.mock;


/**
 * A mock {@link android.content.pm.PackageManager} class.  All methods are non-functional and throw
 * {@link java.lang.UnsupportedOperationException}. Override it to provide the operations that you
 * need.
 *
 * @deprecated Use a mocking framework like <a href="https://github.com/mockito/mockito">Mockito</a>.
New tests should be written using the
<a href="{@docRoot }tools/testing-support-library/index.html">Android Testing Support Library</a>.
 */
@java.lang.Deprecated
public class MockPackageManager extends android.content.pm.PackageManager {
    @java.lang.Override
    public android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.pm.PackageInfo getPackageInfoAsUser(java.lang.String packageName, int flags, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String[] currentToCanonicalPackageNames(java.lang.String[] names) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String[] canonicalToCurrentPackageNames(java.lang.String[] names) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.Intent getLaunchIntentForPackage(java.lang.String packageName) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.Intent getLeanbackLaunchIntentForPackage(java.lang.String packageName) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int[] getPackageGids(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int[] getPackageGids(java.lang.String packageName, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int getPackageUid(java.lang.String packageName, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int getPackageUidAsUser(java.lang.String packageName, int flags, int userHandle) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int getPackageUidAsUser(java.lang.String packageName, int userHandle) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.pm.PermissionInfo getPermissionInfo(java.lang.String name, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.List<android.content.pm.PermissionInfo> queryPermissionsByGroup(java.lang.String group, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.pm.PermissionGroupInfo getPermissionGroupInfo(java.lang.String name, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.List<android.content.pm.PermissionGroupInfo> getAllPermissionGroups(int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String packageName, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.pm.ApplicationInfo getApplicationInfoAsUser(java.lang.String packageName, int flags, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.pm.ActivityInfo getActivityInfo(android.content.ComponentName className, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.pm.ActivityInfo getReceiverInfo(android.content.ComponentName className, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.pm.ServiceInfo getServiceInfo(android.content.ComponentName className, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.pm.ProviderInfo getProviderInfo(android.content.ComponentName className, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.List<android.content.pm.PackageInfo> getInstalledPackages(int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.List<android.content.pm.PackageInfo> getPackagesHoldingPermissions(java.lang.String[] permissions, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.util.List<android.content.pm.PackageInfo> getInstalledPackagesAsUser(int flags, int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int checkPermission(java.lang.String permName, java.lang.String pkgName) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean isPermissionRevokedByPolicy(java.lang.String permName, java.lang.String pkgName) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.lang.String getPermissionControllerPackageName() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean addPermission(android.content.pm.PermissionInfo info) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean addPermissionAsync(android.content.pm.PermissionInfo info) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void removePermission(java.lang.String name) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void grantRuntimePermission(java.lang.String packageName, java.lang.String permissionName, android.os.UserHandle user) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void revokeRuntimePermission(java.lang.String packageName, java.lang.String permissionName, android.os.UserHandle user) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int getPermissionFlags(java.lang.String permissionName, java.lang.String packageName, android.os.UserHandle user) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void updatePermissionFlags(java.lang.String permissionName, java.lang.String packageName, int flagMask, int flagValues, android.os.UserHandle user) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean shouldShowRequestPermissionRationale(java.lang.String permission) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void addOnPermissionsChangeListener(android.content.pm.PackageManager.OnPermissionsChangedListener listener) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void removeOnPermissionsChangeListener(android.content.pm.PackageManager.OnPermissionsChangedListener listener) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int checkSignatures(java.lang.String pkg1, java.lang.String pkg2) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int checkSignatures(int uid1, int uid2) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String[] getPackagesForUid(int uid) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String getNameForUid(int uid) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown - to match hiding in superclass
     */
    @java.lang.Override
    public int getUidForSharedUser(java.lang.String sharedUserName) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.List<android.content.pm.ApplicationInfo> getInstalledApplications(int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.util.List<android.content.pm.EphemeralApplicationInfo> getEphemeralApplications() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.graphics.drawable.Drawable getEphemeralApplicationIcon(java.lang.String packageName) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public byte[] getEphemeralCookie() {
        return new byte[0];
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean isEphemeralApplication() {
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int getEphemeralCookieMaxSizeBytes() {
        return 0;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean setEphemeralCookie(@android.annotation.NonNull
    byte[] cookie) {
        return false;
    }

    @java.lang.Override
    public android.content.pm.ResolveInfo resolveActivity(android.content.Intent intent, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.pm.ResolveInfo resolveActivityAsUser(android.content.Intent intent, int flags, int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.List<android.content.pm.ResolveInfo> queryIntentActivities(android.content.Intent intent, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.util.List<android.content.pm.ResolveInfo> queryIntentActivitiesAsUser(android.content.Intent intent, int flags, int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.List<android.content.pm.ResolveInfo> queryIntentActivityOptions(android.content.ComponentName caller, android.content.Intent[] specifics, android.content.Intent intent, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.List<android.content.pm.ResolveInfo> queryBroadcastReceivers(android.content.Intent intent, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.util.List<android.content.pm.ResolveInfo> queryBroadcastReceiversAsUser(android.content.Intent intent, int flags, int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.pm.ResolveInfo resolveService(android.content.Intent intent, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.List<android.content.pm.ResolveInfo> queryIntentServices(android.content.Intent intent, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.util.List<android.content.pm.ResolveInfo> queryIntentServicesAsUser(android.content.Intent intent, int flags, int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.util.List<android.content.pm.ResolveInfo> queryIntentContentProvidersAsUser(android.content.Intent intent, int flags, int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.List<android.content.pm.ResolveInfo> queryIntentContentProviders(android.content.Intent intent, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.pm.ProviderInfo resolveContentProvider(java.lang.String name, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.pm.ProviderInfo resolveContentProviderAsUser(java.lang.String name, int flags, int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.List<android.content.pm.ProviderInfo> queryContentProviders(java.lang.String processName, int uid, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.pm.InstrumentationInfo getInstrumentationInfo(android.content.ComponentName className, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.List<android.content.pm.InstrumentationInfo> queryInstrumentation(java.lang.String targetPackage, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getDrawable(java.lang.String packageName, int resid, android.content.pm.ApplicationInfo appInfo) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getActivityIcon(android.content.ComponentName activityName) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getActivityIcon(android.content.Intent intent) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getDefaultActivityIcon() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getActivityBanner(android.content.ComponentName activityName) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getActivityBanner(android.content.Intent intent) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getApplicationBanner(android.content.pm.ApplicationInfo info) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getApplicationBanner(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getApplicationIcon(android.content.pm.ApplicationInfo info) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getApplicationIcon(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getActivityLogo(android.content.ComponentName activityName) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getActivityLogo(android.content.Intent intent) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getApplicationLogo(android.content.pm.ApplicationInfo info) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getApplicationLogo(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.graphics.drawable.Drawable getManagedUserBadgedDrawable(android.graphics.drawable.Drawable drawable, android.graphics.Rect badgeLocation, int badgeDensity) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getUserBadgedIcon(android.graphics.drawable.Drawable icon, android.os.UserHandle user) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getUserBadgedDrawableForDensity(android.graphics.drawable.Drawable drawable, android.os.UserHandle user, android.graphics.Rect badgeLocation, int badgeDensity) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.graphics.drawable.Drawable getUserBadgeForDensity(android.os.UserHandle user, int density) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.graphics.drawable.Drawable getUserBadgeForDensityNoBackground(android.os.UserHandle user, int density) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.CharSequence getUserBadgedLabel(java.lang.CharSequence label, android.os.UserHandle user) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.CharSequence getText(java.lang.String packageName, int resid, android.content.pm.ApplicationInfo appInfo) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.res.XmlResourceParser getXml(java.lang.String packageName, int resid, android.content.pm.ApplicationInfo appInfo) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.CharSequence getApplicationLabel(android.content.pm.ApplicationInfo info) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.res.Resources getResourcesForActivity(android.content.ComponentName activityName) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.res.Resources getResourcesForApplication(android.content.pm.ApplicationInfo app) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.res.Resources getResourcesForApplication(java.lang.String appPackageName) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.res.Resources getResourcesForApplicationAsUser(java.lang.String appPackageName, int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.pm.PackageInfo getPackageArchiveInfo(java.lang.String archiveFilePath, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown - to match hiding in superclass
     */
    @java.lang.Override
    public void installPackage(android.net.Uri packageURI, android.content.pm.IPackageInstallObserver observer, int flags, java.lang.String installerPackageName) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void setInstallerPackageName(java.lang.String targetPackage, java.lang.String installerPackageName) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String getInstallerPackageName(java.lang.String packageName) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public int getMoveStatus(int moveId) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public void registerMoveCallback(android.content.pm.PackageManager.MoveCallback callback, android.os.Handler handler) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public void unregisterMoveCallback(android.content.pm.PackageManager.MoveCallback callback) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public int movePackage(java.lang.String packageName, android.os.storage.VolumeInfo vol) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public android.os.storage.VolumeInfo getPackageCurrentVolume(android.content.pm.ApplicationInfo app) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public java.util.List<android.os.storage.VolumeInfo> getPackageCandidateVolumes(android.content.pm.ApplicationInfo app) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public int movePrimaryStorage(android.os.storage.VolumeInfo vol) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public android.os.storage.VolumeInfo getPrimaryStorageCurrentVolume() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public java.util.List<android.os.storage.VolumeInfo> getPrimaryStorageCandidateVolumes() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown - to match hiding in superclass
     */
    @java.lang.Override
    public void clearApplicationUserData(java.lang.String packageName, android.content.pm.IPackageDataObserver observer) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown - to match hiding in superclass
     */
    @java.lang.Override
    public void deleteApplicationCacheFiles(java.lang.String packageName, android.content.pm.IPackageDataObserver observer) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown - to match hiding in superclass
     */
    @java.lang.Override
    public void deleteApplicationCacheFilesAsUser(java.lang.String packageName, int userId, android.content.pm.IPackageDataObserver observer) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public void freeStorageAndNotify(java.lang.String volumeUuid, long idealStorageSize, android.content.pm.IPackageDataObserver observer) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public void freeStorage(java.lang.String volumeUuid, long idealStorageSize, android.content.IntentSender pi) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown - to match hiding in superclass
     */
    @java.lang.Override
    public void deletePackage(java.lang.String packageName, android.content.pm.IPackageDeleteObserver observer, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown - to match hiding in superclass
     */
    @java.lang.Override
    public void deletePackageAsUser(java.lang.String packageName, android.content.pm.IPackageDeleteObserver observer, int flags, int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void addPackageToPreferred(java.lang.String packageName) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void removePackageFromPreferred(java.lang.String packageName) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.List<android.content.pm.PackageInfo> getPreferredPackages(int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void setComponentEnabledSetting(android.content.ComponentName componentName, int newState, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int getComponentEnabledSetting(android.content.ComponentName componentName) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void setApplicationEnabledSetting(java.lang.String packageName, int newState, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int getApplicationEnabledSetting(java.lang.String packageName) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void flushPackageRestrictionsAsUser(int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void addPreferredActivity(android.content.IntentFilter filter, int match, android.content.ComponentName[] set, android.content.ComponentName activity) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown - to match hiding in superclass
     */
    @java.lang.Override
    public void replacePreferredActivity(android.content.IntentFilter filter, int match, android.content.ComponentName[] set, android.content.ComponentName activity) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void clearPackagePreferredActivities(java.lang.String packageName) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown - to match hiding in superclass
     */
    @java.lang.Override
    public void getPackageSizeInfoAsUser(java.lang.String packageName, int userHandle, android.content.pm.IPackageStatsObserver observer) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int getPreferredActivities(java.util.List<android.content.IntentFilter> outFilters, java.util.List<android.content.ComponentName> outActivities, java.lang.String packageName) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown - hidden in superclass
     */
    @java.lang.Override
    public android.content.ComponentName getHomeActivities(java.util.List<android.content.pm.ResolveInfo> outActivities) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String[] getSystemSharedLibraryNames() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.NonNull
    public java.lang.String getServicesSystemSharedLibraryPackageName() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.NonNull
    public java.lang.String getSharedSystemSharedLibraryPackageName() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.pm.FeatureInfo[] getSystemAvailableFeatures() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean hasSystemFeature(java.lang.String name) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean hasSystemFeature(java.lang.String name, int version) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean isSafeMode() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.pm.KeySet getKeySetByAlias(java.lang.String packageName, java.lang.String alias) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.pm.KeySet getSigningKeySet(java.lang.String packageName) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean isSignedBy(java.lang.String packageName, android.content.pm.KeySet ks) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean isSignedByExactly(java.lang.String packageName, android.content.pm.KeySet ks) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.lang.String[] setPackagesSuspendedAsUser(java.lang.String[] packageNames, boolean hidden, int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean isPackageSuspendedForUser(java.lang.String packageName, int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean setApplicationHiddenSettingAsUser(java.lang.String packageName, boolean hidden, android.os.UserHandle user) {
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean getApplicationHiddenSettingAsUser(java.lang.String packageName, android.os.UserHandle user) {
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int installExistingPackage(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int installExistingPackageAsUser(java.lang.String packageName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void verifyPendingInstall(int id, int verificationCode) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void extendVerificationTimeout(int id, int verificationCodeAtTimeout, long millisecondsToDelay) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void verifyIntentFilter(int id, int verificationCode, java.util.List<java.lang.String> outFailedDomains) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int getIntentVerificationStatusAsUser(java.lang.String packageName, int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean updateIntentVerificationStatusAsUser(java.lang.String packageName, int status, int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.util.List<android.content.pm.IntentFilterVerificationInfo> getIntentFilterVerifications(java.lang.String packageName) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.List<android.content.IntentFilter> getAllIntentFilters(java.lang.String packageName) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@removed }
     */
    @java.lang.Deprecated
    public java.lang.String getDefaultBrowserPackageName(int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public java.lang.String getDefaultBrowserPackageNameAsUser(int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@removed }
     */
    @java.lang.Deprecated
    public boolean setDefaultBrowserPackageName(java.lang.String packageName, int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public boolean setDefaultBrowserPackageNameAsUser(java.lang.String packageName, int userId) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.pm.VerifierDeviceIdentity getVerifierDeviceIdentity() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean isUpgrade() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void installPackage(android.net.Uri packageURI, android.app.PackageInstallObserver observer, int flags, java.lang.String installerPackageName) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void addCrossProfileIntentFilter(android.content.IntentFilter filter, int sourceUserId, int targetUserId, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void clearCrossProfileIntentFilters(int sourceUserId) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    public android.content.pm.PackageInstaller getPackageInstaller() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public boolean isPackageAvailable(java.lang.String packageName) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    public android.graphics.drawable.Drawable loadItemIcon(android.content.pm.PackageItemInfo itemInfo, android.content.pm.ApplicationInfo appInfo) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    public android.graphics.drawable.Drawable loadUnbadgedItemIcon(android.content.pm.PackageItemInfo itemInfo, android.content.pm.ApplicationInfo appInfo) {
        throw new java.lang.UnsupportedOperationException();
    }
}

