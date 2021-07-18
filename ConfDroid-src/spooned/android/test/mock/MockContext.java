/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * A mock {@link android.content.Context} class.  All methods are non-functional and throw
 * {@link java.lang.UnsupportedOperationException}.  You can use this to inject other dependencies,
 * mocks, or monitors into the classes you are testing.
 */
public class MockContext extends android.content.Context {
    @java.lang.Override
    public android.content.res.AssetManager getAssets() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.res.Resources getResources() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.pm.PackageManager getPackageManager() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.ContentResolver getContentResolver() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.os.Looper getMainLooper() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.Context getApplicationContext() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void setTheme(int resid) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.res.Resources.Theme getTheme() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.ClassLoader getClassLoader() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String getPackageName() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.lang.String getBasePackageName() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.lang.String getOpPackageName() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.pm.ApplicationInfo getApplicationInfo() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String getPackageResourcePath() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String getPackageCodePath() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.SharedPreferences getSharedPreferences(java.lang.String name, int mode) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.SharedPreferences getSharedPreferences(java.io.File file, int mode) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean moveSharedPreferencesFrom(android.content.Context sourceContext, java.lang.String name) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean deleteSharedPreferences(java.lang.String name) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.FileInputStream openFileInput(java.lang.String name) throws java.io.FileNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.FileOutputStream openFileOutput(java.lang.String name, int mode) throws java.io.FileNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean deleteFile(java.lang.String name) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.File getFileStreamPath(java.lang.String name) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.io.File getSharedPreferencesPath(java.lang.String name) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String[] fileList() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.File getDataDir() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.File getFilesDir() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.File getNoBackupFilesDir() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.File getExternalFilesDir(java.lang.String type) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.File getObbDir() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.File getCacheDir() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.File getCodeCacheDir() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.File getExternalCacheDir() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.File getDir(java.lang.String name, int mode) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.database.sqlite.SQLiteDatabase openOrCreateDatabase(java.lang.String file, int mode, android.database.sqlite.SQLiteDatabase.CursorFactory factory) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.database.sqlite.SQLiteDatabase openOrCreateDatabase(java.lang.String file, int mode, android.database.sqlite.SQLiteDatabase.CursorFactory factory, android.database.DatabaseErrorHandler errorHandler) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.File getDatabasePath(java.lang.String name) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String[] databaseList() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean moveDatabaseFrom(android.content.Context sourceContext, java.lang.String name) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean deleteDatabase(java.lang.String name) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getWallpaper() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable peekWallpaper() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int getWallpaperDesiredMinimumWidth() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int getWallpaperDesiredMinimumHeight() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void setWallpaper(android.graphics.Bitmap bitmap) throws java.io.IOException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void setWallpaper(java.io.InputStream data) throws java.io.IOException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void clearWallpaper() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void startActivity(android.content.Intent intent) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void startActivity(android.content.Intent intent, android.os.Bundle options) {
        startActivity(intent);
    }

    @java.lang.Override
    public void startActivities(android.content.Intent[] intents) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void startActivities(android.content.Intent[] intents, android.os.Bundle options) {
        startActivities(intents);
    }

    @java.lang.Override
    public void startIntentSender(android.content.IntentSender intent, android.content.Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws android.content.IntentSender.SendIntentException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void startIntentSender(android.content.IntentSender intent, android.content.Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, android.os.Bundle options) throws android.content.IntentSender.SendIntentException {
        startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags);
    }

    @java.lang.Override
    public void sendBroadcast(android.content.Intent intent) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void sendBroadcast(android.content.Intent intent, java.lang.String receiverPermission) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendBroadcastMultiplePermissions(android.content.Intent intent, java.lang.String[] receiverPermissions) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @java.lang.Override
    public void sendBroadcast(android.content.Intent intent, java.lang.String receiverPermission, android.os.Bundle options) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendBroadcast(android.content.Intent intent, java.lang.String receiverPermission, int appOp) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void sendOrderedBroadcast(android.content.Intent intent, java.lang.String receiverPermission) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void sendOrderedBroadcast(android.content.Intent intent, java.lang.String receiverPermission, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @java.lang.Override
    public void sendOrderedBroadcast(android.content.Intent intent, java.lang.String receiverPermission, android.os.Bundle options, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendOrderedBroadcast(android.content.Intent intent, java.lang.String receiverPermission, int appOp, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void sendBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void sendBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, java.lang.String receiverPermission) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, java.lang.String receiverPermission, int appOp) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void sendOrderedBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, java.lang.String receiverPermission, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendOrderedBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, java.lang.String receiverPermission, int appOp, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendOrderedBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, java.lang.String receiverPermission, int appOp, android.os.Bundle options, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void sendStickyBroadcast(android.content.Intent intent) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void sendStickyOrderedBroadcast(android.content.Intent intent, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void removeStickyBroadcast(android.content.Intent intent) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void sendStickyBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendStickyBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, android.os.Bundle options) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void sendStickyOrderedBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void removeStickyBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.Intent registerReceiver(android.content.BroadcastReceiver receiver, android.content.IntentFilter filter) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.Intent registerReceiver(android.content.BroadcastReceiver receiver, android.content.IntentFilter filter, java.lang.String broadcastPermission, android.os.Handler scheduler) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.Intent registerReceiverAsUser(android.content.BroadcastReceiver receiver, android.os.UserHandle user, android.content.IntentFilter filter, java.lang.String broadcastPermission, android.os.Handler scheduler) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void unregisterReceiver(android.content.BroadcastReceiver receiver) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.ComponentName startService(android.content.Intent service) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean stopService(android.content.Intent service) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.ComponentName startServiceAsUser(android.content.Intent service, android.os.UserHandle user) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean stopServiceAsUser(android.content.Intent service, android.os.UserHandle user) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean bindService(android.content.Intent service, android.content.ServiceConnection conn, int flags) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean bindServiceAsUser(android.content.Intent service, android.content.ServiceConnection conn, int flags, android.os.UserHandle user) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void unbindService(android.content.ServiceConnection conn) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean startInstrumentation(android.content.ComponentName className, java.lang.String profileFile, android.os.Bundle arguments) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.Object getSystemService(java.lang.String name) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String getSystemServiceName(java.lang.Class<?> serviceClass) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int checkPermission(java.lang.String permission, int pid, int uid) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int checkPermission(java.lang.String permission, int pid, int uid, android.os.IBinder callerToken) {
        return checkPermission(permission, pid, uid);
    }

    @java.lang.Override
    public int checkCallingPermission(java.lang.String permission) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int checkCallingOrSelfPermission(java.lang.String permission) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int checkSelfPermission(java.lang.String permission) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void enforcePermission(java.lang.String permission, int pid, int uid, java.lang.String message) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void enforceCallingPermission(java.lang.String permission, java.lang.String message) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void enforceCallingOrSelfPermission(java.lang.String permission, java.lang.String message) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void grantUriPermission(java.lang.String toPackage, android.net.Uri uri, int modeFlags) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void revokeUriPermission(android.net.Uri uri, int modeFlags) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int checkUriPermission(android.net.Uri uri, int pid, int uid, int modeFlags) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int checkUriPermission(android.net.Uri uri, int pid, int uid, int modeFlags, android.os.IBinder callerToken) {
        return checkUriPermission(uri, pid, uid, modeFlags);
    }

    @java.lang.Override
    public int checkCallingUriPermission(android.net.Uri uri, int modeFlags) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int checkCallingOrSelfUriPermission(android.net.Uri uri, int modeFlags) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public int checkUriPermission(android.net.Uri uri, java.lang.String readPermission, java.lang.String writePermission, int pid, int uid, int modeFlags) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void enforceUriPermission(android.net.Uri uri, int pid, int uid, int modeFlags, java.lang.String message) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void enforceCallingUriPermission(android.net.Uri uri, int modeFlags, java.lang.String message) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public void enforceCallingOrSelfUriPermission(android.net.Uri uri, int modeFlags, java.lang.String message) {
        throw new java.lang.UnsupportedOperationException();
    }

    public void enforceUriPermission(android.net.Uri uri, java.lang.String readPermission, java.lang.String writePermission, int pid, int uid, int modeFlags, java.lang.String message) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.Context createPackageContext(java.lang.String packageName, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public android.content.Context createApplicationContext(android.content.pm.ApplicationInfo application, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        return null;
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public android.content.Context createPackageContextAsUser(java.lang.String packageName, int flags, android.os.UserHandle user) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public int getUserId() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.Context createConfigurationContext(android.content.res.Configuration overrideConfiguration) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.Context createDisplayContext(android.view.Display display) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean isRestricted() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.view.DisplayAdjustments getDisplayAdjustments(int displayId) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.view.Display getDisplay() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.File[] getExternalFilesDirs(java.lang.String type) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.File[] getObbDirs() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.File[] getExternalCacheDirs() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.io.File[] getExternalMediaDirs() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public android.content.Context createDeviceProtectedStorageContext() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @android.annotation.SystemApi
    @java.lang.Override
    public android.content.Context createCredentialProtectedStorageContext() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean isDeviceProtectedStorage() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * {@hide }
     */
    @android.annotation.SystemApi
    @java.lang.Override
    public boolean isCredentialProtectedStorage() {
        throw new java.lang.UnsupportedOperationException();
    }
}

