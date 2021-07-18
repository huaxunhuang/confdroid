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
package android.content;


/**
 * Proxying implementation of Context that simply delegates all of its calls to
 * another Context.  Can be subclassed to modify behavior without changing
 * the original Context.
 */
public class ContextWrapper extends android.content.Context {
    @android.annotation.UnsupportedAppUsage
    android.content.Context mBase;

    public ContextWrapper(android.content.Context base) {
        mBase = base;
    }

    /**
     * Set the base context for this ContextWrapper.  All calls will then be
     * delegated to the base context.  Throws
     * IllegalStateException if a base context has already been set.
     *
     * @param base
     * 		The new base context for this wrapper.
     */
    protected void attachBaseContext(android.content.Context base) {
        if (mBase != null) {
            throw new java.lang.IllegalStateException("Base context already set");
        }
        mBase = base;
    }

    /**
     *
     *
     * @return the base context as set by the constructor or setBaseContext
     */
    public android.content.Context getBaseContext() {
        return mBase;
    }

    @java.lang.Override
    public android.content.res.AssetManager getAssets() {
        return mBase.getAssets();
    }

    @java.lang.Override
    public android.content.res.Resources getResources() {
        return mBase.getResources();
    }

    @java.lang.Override
    public android.content.pm.PackageManager getPackageManager() {
        return mBase.getPackageManager();
    }

    @java.lang.Override
    public android.content.ContentResolver getContentResolver() {
        return mBase.getContentResolver();
    }

    @java.lang.Override
    public android.os.Looper getMainLooper() {
        return mBase.getMainLooper();
    }

    @java.lang.Override
    public java.util.concurrent.Executor getMainExecutor() {
        return mBase.getMainExecutor();
    }

    @java.lang.Override
    public android.content.Context getApplicationContext() {
        return mBase.getApplicationContext();
    }

    @java.lang.Override
    public void setTheme(int resid) {
        mBase.setTheme(resid);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.UnsupportedAppUsage
    public int getThemeResId() {
        return mBase.getThemeResId();
    }

    @java.lang.Override
    public android.content.res.Resources.Theme getTheme() {
        return mBase.getTheme();
    }

    @java.lang.Override
    public java.lang.ClassLoader getClassLoader() {
        return mBase.getClassLoader();
    }

    @java.lang.Override
    public java.lang.String getPackageName() {
        return mBase.getPackageName();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.UnsupportedAppUsage
    public java.lang.String getBasePackageName() {
        return mBase.getBasePackageName();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.lang.String getOpPackageName() {
        return mBase.getOpPackageName();
    }

    @java.lang.Override
    public android.content.pm.ApplicationInfo getApplicationInfo() {
        return mBase.getApplicationInfo();
    }

    @java.lang.Override
    public java.lang.String getPackageResourcePath() {
        return mBase.getPackageResourcePath();
    }

    @java.lang.Override
    public java.lang.String getPackageCodePath() {
        return mBase.getPackageCodePath();
    }

    @java.lang.Override
    public android.content.SharedPreferences getSharedPreferences(java.lang.String name, int mode) {
        return mBase.getSharedPreferences(name, mode);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.SharedPreferences getSharedPreferences(java.io.File file, int mode) {
        return mBase.getSharedPreferences(file, mode);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void reloadSharedPreferences() {
        mBase.reloadSharedPreferences();
    }

    @java.lang.Override
    public boolean moveSharedPreferencesFrom(android.content.Context sourceContext, java.lang.String name) {
        return mBase.moveSharedPreferencesFrom(sourceContext, name);
    }

    @java.lang.Override
    public boolean deleteSharedPreferences(java.lang.String name) {
        return mBase.deleteSharedPreferences(name);
    }

    @java.lang.Override
    public java.io.FileInputStream openFileInput(java.lang.String name) throws java.io.FileNotFoundException {
        return mBase.openFileInput(name);
    }

    @java.lang.Override
    public java.io.FileOutputStream openFileOutput(java.lang.String name, int mode) throws java.io.FileNotFoundException {
        return mBase.openFileOutput(name, mode);
    }

    @java.lang.Override
    public boolean deleteFile(java.lang.String name) {
        return mBase.deleteFile(name);
    }

    @java.lang.Override
    public java.io.File getFileStreamPath(java.lang.String name) {
        return mBase.getFileStreamPath(name);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.io.File getSharedPreferencesPath(java.lang.String name) {
        return mBase.getSharedPreferencesPath(name);
    }

    @java.lang.Override
    public java.lang.String[] fileList() {
        return mBase.fileList();
    }

    @java.lang.Override
    public java.io.File getDataDir() {
        return mBase.getDataDir();
    }

    @java.lang.Override
    public java.io.File getFilesDir() {
        return mBase.getFilesDir();
    }

    @java.lang.Override
    public java.io.File getNoBackupFilesDir() {
        return mBase.getNoBackupFilesDir();
    }

    @java.lang.Override
    public java.io.File getExternalFilesDir(java.lang.String type) {
        return mBase.getExternalFilesDir(type);
    }

    @java.lang.Override
    public java.io.File[] getExternalFilesDirs(java.lang.String type) {
        return mBase.getExternalFilesDirs(type);
    }

    @java.lang.Override
    public java.io.File getObbDir() {
        return mBase.getObbDir();
    }

    @java.lang.Override
    public java.io.File[] getObbDirs() {
        return mBase.getObbDirs();
    }

    @java.lang.Override
    public java.io.File getCacheDir() {
        return mBase.getCacheDir();
    }

    @java.lang.Override
    public java.io.File getCodeCacheDir() {
        return mBase.getCodeCacheDir();
    }

    @java.lang.Override
    public java.io.File getExternalCacheDir() {
        return mBase.getExternalCacheDir();
    }

    @java.lang.Override
    public java.io.File[] getExternalCacheDirs() {
        return mBase.getExternalCacheDirs();
    }

    @java.lang.Override
    public java.io.File[] getExternalMediaDirs() {
        return mBase.getExternalMediaDirs();
    }

    @java.lang.Override
    public java.io.File getDir(java.lang.String name, int mode) {
        return mBase.getDir(name, mode);
    }

    /**
     *
     *
     * @unknown *
     */
    @java.lang.Override
    public java.io.File getPreloadsFileCache() {
        return mBase.getPreloadsFileCache();
    }

    @java.lang.Override
    public android.database.sqlite.SQLiteDatabase openOrCreateDatabase(java.lang.String name, int mode, android.database.sqlite.SQLiteDatabase.CursorFactory factory) {
        return mBase.openOrCreateDatabase(name, mode, factory);
    }

    @java.lang.Override
    public android.database.sqlite.SQLiteDatabase openOrCreateDatabase(java.lang.String name, int mode, android.database.sqlite.SQLiteDatabase.CursorFactory factory, android.database.DatabaseErrorHandler errorHandler) {
        return mBase.openOrCreateDatabase(name, mode, factory, errorHandler);
    }

    @java.lang.Override
    public boolean moveDatabaseFrom(android.content.Context sourceContext, java.lang.String name) {
        return mBase.moveDatabaseFrom(sourceContext, name);
    }

    @java.lang.Override
    public boolean deleteDatabase(java.lang.String name) {
        return mBase.deleteDatabase(name);
    }

    @java.lang.Override
    public java.io.File getDatabasePath(java.lang.String name) {
        return mBase.getDatabasePath(name);
    }

    @java.lang.Override
    public java.lang.String[] databaseList() {
        return mBase.databaseList();
    }

    @java.lang.Override
    @java.lang.Deprecated
    public android.graphics.drawable.Drawable getWallpaper() {
        return mBase.getWallpaper();
    }

    @java.lang.Override
    @java.lang.Deprecated
    public android.graphics.drawable.Drawable peekWallpaper() {
        return mBase.peekWallpaper();
    }

    @java.lang.Override
    @java.lang.Deprecated
    public int getWallpaperDesiredMinimumWidth() {
        return mBase.getWallpaperDesiredMinimumWidth();
    }

    @java.lang.Override
    @java.lang.Deprecated
    public int getWallpaperDesiredMinimumHeight() {
        return mBase.getWallpaperDesiredMinimumHeight();
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void setWallpaper(android.graphics.Bitmap bitmap) throws java.io.IOException {
        mBase.setWallpaper(bitmap);
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void setWallpaper(java.io.InputStream data) throws java.io.IOException {
        mBase.setWallpaper(data);
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void clearWallpaper() throws java.io.IOException {
        mBase.clearWallpaper();
    }

    @java.lang.Override
    public void startActivity(android.content.Intent intent) {
        mBase.startActivity(intent);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void startActivityAsUser(android.content.Intent intent, android.os.UserHandle user) {
        mBase.startActivityAsUser(intent, user);
    }

    /**
     *
     *
     * @unknown *
     */
    public void startActivityForResult(java.lang.String who, android.content.Intent intent, int requestCode, android.os.Bundle options) {
        mBase.startActivityForResult(who, intent, requestCode, options);
    }

    /**
     *
     *
     * @unknown *
     */
    public boolean canStartActivityForResult() {
        return mBase.canStartActivityForResult();
    }

    @java.lang.Override
    public void startActivity(android.content.Intent intent, android.os.Bundle options) {
        mBase.startActivity(intent, options);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void startActivityAsUser(android.content.Intent intent, android.os.Bundle options, android.os.UserHandle user) {
        mBase.startActivityAsUser(intent, options, user);
    }

    @java.lang.Override
    public void startActivities(android.content.Intent[] intents) {
        mBase.startActivities(intents);
    }

    @java.lang.Override
    public void startActivities(android.content.Intent[] intents, android.os.Bundle options) {
        mBase.startActivities(intents, options);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int startActivitiesAsUser(android.content.Intent[] intents, android.os.Bundle options, android.os.UserHandle userHandle) {
        return mBase.startActivitiesAsUser(intents, options, userHandle);
    }

    @java.lang.Override
    public void startIntentSender(android.content.IntentSender intent, android.content.Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws android.content.IntentSender.SendIntentException {
        mBase.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags);
    }

    @java.lang.Override
    public void startIntentSender(android.content.IntentSender intent, android.content.Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, android.os.Bundle options) throws android.content.IntentSender.SendIntentException {
        mBase.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, options);
    }

    @java.lang.Override
    public void sendBroadcast(android.content.Intent intent) {
        mBase.sendBroadcast(intent);
    }

    @java.lang.Override
    public void sendBroadcast(android.content.Intent intent, java.lang.String receiverPermission) {
        mBase.sendBroadcast(intent, receiverPermission);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendBroadcastMultiplePermissions(android.content.Intent intent, java.lang.String[] receiverPermissions) {
        mBase.sendBroadcastMultiplePermissions(intent, receiverPermissions);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendBroadcastAsUserMultiplePermissions(android.content.Intent intent, android.os.UserHandle user, java.lang.String[] receiverPermissions) {
        mBase.sendBroadcastAsUserMultiplePermissions(intent, user, receiverPermissions);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @java.lang.Override
    public void sendBroadcast(android.content.Intent intent, java.lang.String receiverPermission, android.os.Bundle options) {
        mBase.sendBroadcast(intent, receiverPermission, options);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendBroadcast(android.content.Intent intent, java.lang.String receiverPermission, int appOp) {
        mBase.sendBroadcast(intent, receiverPermission, appOp);
    }

    @java.lang.Override
    public void sendOrderedBroadcast(android.content.Intent intent, java.lang.String receiverPermission) {
        mBase.sendOrderedBroadcast(intent, receiverPermission);
    }

    @java.lang.Override
    public void sendOrderedBroadcast(android.content.Intent intent, java.lang.String receiverPermission, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        mBase.sendOrderedBroadcast(intent, receiverPermission, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @java.lang.Override
    public void sendOrderedBroadcast(android.content.Intent intent, java.lang.String receiverPermission, android.os.Bundle options, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        mBase.sendOrderedBroadcast(intent, receiverPermission, options, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendOrderedBroadcast(android.content.Intent intent, java.lang.String receiverPermission, int appOp, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        mBase.sendOrderedBroadcast(intent, receiverPermission, appOp, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    @java.lang.Override
    public void sendBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user) {
        mBase.sendBroadcastAsUser(intent, user);
    }

    @java.lang.Override
    public void sendBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, java.lang.String receiverPermission) {
        mBase.sendBroadcastAsUser(intent, user, receiverPermission);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, java.lang.String receiverPermission, android.os.Bundle options) {
        mBase.sendBroadcastAsUser(intent, user, receiverPermission, options);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, java.lang.String receiverPermission, int appOp) {
        mBase.sendBroadcastAsUser(intent, user, receiverPermission, appOp);
    }

    @java.lang.Override
    public void sendOrderedBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, java.lang.String receiverPermission, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        mBase.sendOrderedBroadcastAsUser(intent, user, receiverPermission, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendOrderedBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, java.lang.String receiverPermission, int appOp, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        mBase.sendOrderedBroadcastAsUser(intent, user, receiverPermission, appOp, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendOrderedBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, java.lang.String receiverPermission, int appOp, android.os.Bundle options, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        mBase.sendOrderedBroadcastAsUser(intent, user, receiverPermission, appOp, options, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void sendStickyBroadcast(android.content.Intent intent) {
        mBase.sendStickyBroadcast(intent);
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void sendStickyOrderedBroadcast(android.content.Intent intent, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        mBase.sendStickyOrderedBroadcast(intent, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void removeStickyBroadcast(android.content.Intent intent) {
        mBase.removeStickyBroadcast(intent);
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void sendStickyBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user) {
        mBase.sendStickyBroadcastAsUser(intent, user);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @java.lang.Deprecated
    public void sendStickyBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, android.os.Bundle options) {
        mBase.sendStickyBroadcastAsUser(intent, user, options);
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void sendStickyOrderedBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        mBase.sendStickyOrderedBroadcastAsUser(intent, user, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void removeStickyBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user) {
        mBase.removeStickyBroadcastAsUser(intent, user);
    }

    @java.lang.Override
    public android.content.Intent registerReceiver(android.content.BroadcastReceiver receiver, android.content.IntentFilter filter) {
        return mBase.registerReceiver(receiver, filter);
    }

    @java.lang.Override
    public android.content.Intent registerReceiver(android.content.BroadcastReceiver receiver, android.content.IntentFilter filter, int flags) {
        return mBase.registerReceiver(receiver, filter, flags);
    }

    @java.lang.Override
    public android.content.Intent registerReceiver(android.content.BroadcastReceiver receiver, android.content.IntentFilter filter, java.lang.String broadcastPermission, android.os.Handler scheduler) {
        return mBase.registerReceiver(receiver, filter, broadcastPermission, scheduler);
    }

    @java.lang.Override
    public android.content.Intent registerReceiver(android.content.BroadcastReceiver receiver, android.content.IntentFilter filter, java.lang.String broadcastPermission, android.os.Handler scheduler, int flags) {
        return mBase.registerReceiver(receiver, filter, broadcastPermission, scheduler, flags);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.UnsupportedAppUsage
    public android.content.Intent registerReceiverAsUser(android.content.BroadcastReceiver receiver, android.os.UserHandle user, android.content.IntentFilter filter, java.lang.String broadcastPermission, android.os.Handler scheduler) {
        return mBase.registerReceiverAsUser(receiver, user, filter, broadcastPermission, scheduler);
    }

    @java.lang.Override
    public void unregisterReceiver(android.content.BroadcastReceiver receiver) {
        mBase.unregisterReceiver(receiver);
    }

    @java.lang.Override
    public android.content.ComponentName startService(android.content.Intent service) {
        return mBase.startService(service);
    }

    @java.lang.Override
    public android.content.ComponentName startForegroundService(android.content.Intent service) {
        return mBase.startForegroundService(service);
    }

    @java.lang.Override
    public boolean stopService(android.content.Intent name) {
        return mBase.stopService(name);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.UnsupportedAppUsage
    public android.content.ComponentName startServiceAsUser(android.content.Intent service, android.os.UserHandle user) {
        return mBase.startServiceAsUser(service, user);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.UnsupportedAppUsage
    public android.content.ComponentName startForegroundServiceAsUser(android.content.Intent service, android.os.UserHandle user) {
        return mBase.startForegroundServiceAsUser(service, user);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean stopServiceAsUser(android.content.Intent name, android.os.UserHandle user) {
        return mBase.stopServiceAsUser(name, user);
    }

    @java.lang.Override
    public boolean bindService(android.content.Intent service, android.content.ServiceConnection conn, int flags) {
        return mBase.bindService(service, conn, flags);
    }

    @java.lang.Override
    public boolean bindService(android.content.Intent service, int flags, java.util.concurrent.Executor executor, android.content.ServiceConnection conn) {
        return mBase.bindService(service, flags, executor, conn);
    }

    @java.lang.Override
    public boolean bindIsolatedService(android.content.Intent service, int flags, java.lang.String instanceName, java.util.concurrent.Executor executor, android.content.ServiceConnection conn) {
        return mBase.bindIsolatedService(service, flags, instanceName, executor, conn);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean bindServiceAsUser(android.content.Intent service, android.content.ServiceConnection conn, int flags, android.os.UserHandle user) {
        return mBase.bindServiceAsUser(service, conn, flags, user);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean bindServiceAsUser(android.content.Intent service, android.content.ServiceConnection conn, int flags, android.os.Handler handler, android.os.UserHandle user) {
        return mBase.bindServiceAsUser(service, conn, flags, handler, user);
    }

    @java.lang.Override
    public void updateServiceGroup(android.content.ServiceConnection conn, int group, int importance) {
        mBase.updateServiceGroup(conn, group, importance);
    }

    @java.lang.Override
    public void unbindService(android.content.ServiceConnection conn) {
        mBase.unbindService(conn);
    }

    @java.lang.Override
    public boolean startInstrumentation(android.content.ComponentName className, java.lang.String profileFile, android.os.Bundle arguments) {
        return mBase.startInstrumentation(className, profileFile, arguments);
    }

    @java.lang.Override
    public java.lang.Object getSystemService(java.lang.String name) {
        return mBase.getSystemService(name);
    }

    @java.lang.Override
    public java.lang.String getSystemServiceName(java.lang.Class<?> serviceClass) {
        return mBase.getSystemServiceName(serviceClass);
    }

    @java.lang.Override
    public int checkPermission(java.lang.String permission, int pid, int uid) {
        return mBase.checkPermission(permission, pid, uid);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int checkPermission(java.lang.String permission, int pid, int uid, android.os.IBinder callerToken) {
        return mBase.checkPermission(permission, pid, uid, callerToken);
    }

    @java.lang.Override
    public int checkCallingPermission(java.lang.String permission) {
        return mBase.checkCallingPermission(permission);
    }

    @java.lang.Override
    public int checkCallingOrSelfPermission(java.lang.String permission) {
        return mBase.checkCallingOrSelfPermission(permission);
    }

    @java.lang.Override
    public int checkSelfPermission(java.lang.String permission) {
        return mBase.checkSelfPermission(permission);
    }

    @java.lang.Override
    public void enforcePermission(java.lang.String permission, int pid, int uid, java.lang.String message) {
        mBase.enforcePermission(permission, pid, uid, message);
    }

    @java.lang.Override
    public void enforceCallingPermission(java.lang.String permission, java.lang.String message) {
        mBase.enforceCallingPermission(permission, message);
    }

    @java.lang.Override
    public void enforceCallingOrSelfPermission(java.lang.String permission, java.lang.String message) {
        mBase.enforceCallingOrSelfPermission(permission, message);
    }

    @java.lang.Override
    public void grantUriPermission(java.lang.String toPackage, android.net.Uri uri, int modeFlags) {
        mBase.grantUriPermission(toPackage, uri, modeFlags);
    }

    @java.lang.Override
    public void revokeUriPermission(android.net.Uri uri, int modeFlags) {
        mBase.revokeUriPermission(uri, modeFlags);
    }

    @java.lang.Override
    public void revokeUriPermission(java.lang.String targetPackage, android.net.Uri uri, int modeFlags) {
        mBase.revokeUriPermission(targetPackage, uri, modeFlags);
    }

    @java.lang.Override
    public int checkUriPermission(android.net.Uri uri, int pid, int uid, int modeFlags) {
        return mBase.checkUriPermission(uri, pid, uid, modeFlags);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int checkUriPermission(android.net.Uri uri, int pid, int uid, int modeFlags, android.os.IBinder callerToken) {
        return mBase.checkUriPermission(uri, pid, uid, modeFlags, callerToken);
    }

    @java.lang.Override
    public int checkCallingUriPermission(android.net.Uri uri, int modeFlags) {
        return mBase.checkCallingUriPermission(uri, modeFlags);
    }

    @java.lang.Override
    public int checkCallingOrSelfUriPermission(android.net.Uri uri, int modeFlags) {
        return mBase.checkCallingOrSelfUriPermission(uri, modeFlags);
    }

    @java.lang.Override
    public int checkUriPermission(android.net.Uri uri, java.lang.String readPermission, java.lang.String writePermission, int pid, int uid, int modeFlags) {
        return mBase.checkUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags);
    }

    @java.lang.Override
    public void enforceUriPermission(android.net.Uri uri, int pid, int uid, int modeFlags, java.lang.String message) {
        mBase.enforceUriPermission(uri, pid, uid, modeFlags, message);
    }

    @java.lang.Override
    public void enforceCallingUriPermission(android.net.Uri uri, int modeFlags, java.lang.String message) {
        mBase.enforceCallingUriPermission(uri, modeFlags, message);
    }

    @java.lang.Override
    public void enforceCallingOrSelfUriPermission(android.net.Uri uri, int modeFlags, java.lang.String message) {
        mBase.enforceCallingOrSelfUriPermission(uri, modeFlags, message);
    }

    @java.lang.Override
    public void enforceUriPermission(android.net.Uri uri, java.lang.String readPermission, java.lang.String writePermission, int pid, int uid, int modeFlags, java.lang.String message) {
        mBase.enforceUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags, message);
    }

    @java.lang.Override
    public android.content.Context createPackageContext(java.lang.String packageName, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        return mBase.createPackageContext(packageName, flags);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.Context createPackageContextAsUser(java.lang.String packageName, int flags, android.os.UserHandle user) throws android.content.pm.PackageManager.NameNotFoundException {
        return mBase.createPackageContextAsUser(packageName, flags, user);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.UnsupportedAppUsage
    public android.content.Context createApplicationContext(android.content.pm.ApplicationInfo application, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        return mBase.createApplicationContext(application, flags);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.Context createContextForSplit(java.lang.String splitName) throws android.content.pm.PackageManager.NameNotFoundException {
        return mBase.createContextForSplit(splitName);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int getUserId() {
        return mBase.getUserId();
    }

    @java.lang.Override
    public android.content.Context createConfigurationContext(android.content.res.Configuration overrideConfiguration) {
        return mBase.createConfigurationContext(overrideConfiguration);
    }

    @java.lang.Override
    public android.content.Context createDisplayContext(android.view.Display display) {
        return mBase.createDisplayContext(display);
    }

    @java.lang.Override
    public boolean isRestricted() {
        return mBase.isRestricted();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.view.DisplayAdjustments getDisplayAdjustments(int displayId) {
        return mBase.getDisplayAdjustments(displayId);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    @java.lang.Override
    public android.view.Display getDisplay() {
        return mBase.getDisplay();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int getDisplayId() {
        return mBase.getDisplayId();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void updateDisplay(int displayId) {
        mBase.updateDisplay(displayId);
    }

    @java.lang.Override
    public android.content.Context createDeviceProtectedStorageContext() {
        return mBase.createDeviceProtectedStorageContext();
    }

    /**
     * {@hide }
     */
    @android.annotation.SystemApi
    @java.lang.Override
    public android.content.Context createCredentialProtectedStorageContext() {
        return mBase.createCredentialProtectedStorageContext();
    }

    @java.lang.Override
    public boolean isDeviceProtectedStorage() {
        return mBase.isDeviceProtectedStorage();
    }

    /**
     * {@hide }
     */
    @android.annotation.SystemApi
    @java.lang.Override
    public boolean isCredentialProtectedStorage() {
        return mBase.isCredentialProtectedStorage();
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public boolean canLoadUnsafeResources() {
        return mBase.canLoadUnsafeResources();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.os.IBinder getActivityToken() {
        return mBase.getActivityToken();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.app.IServiceConnection getServiceDispatcher(android.content.ServiceConnection conn, android.os.Handler handler, int flags) {
        return mBase.getServiceDispatcher(conn, handler, flags);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.app.IApplicationThread getIApplicationThread() {
        return mBase.getIApplicationThread();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.os.Handler getMainThreadHandler() {
        return mBase.getMainThreadHandler();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int getNextAutofillId() {
        return mBase.getNextAutofillId();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.view.autofill.AutofillManager.AutofillClient getAutofillClient() {
        return mBase.getAutofillClient();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void setAutofillClient(android.view.autofill.AutofillManager.AutofillClient client) {
        mBase.setAutofillClient(client);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.AutofillOptions getAutofillOptions() {
        return mBase == null ? null : mBase.getAutofillOptions();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void setAutofillOptions(android.content.AutofillOptions options) {
        if (mBase != null) {
            mBase.setAutofillOptions(options);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.content.ContentCaptureOptions getContentCaptureOptions() {
        return mBase == null ? null : mBase.getContentCaptureOptions();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    @java.lang.Override
    public void setContentCaptureOptions(android.content.ContentCaptureOptions options) {
        if (mBase != null) {
            mBase.setContentCaptureOptions(options);
        }
    }
}

