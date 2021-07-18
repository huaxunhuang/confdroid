package android.app;


/**
 * Common implementation of Context API, which provides the base
 * context object for Activity and other application components.
 */
class ContextImpl extends android.content.Context {
    private static final java.lang.String TAG = "ContextImpl";

    private static final boolean DEBUG = false;

    /**
     * Map from package name, to preference name, to cached preferences.
     */
    @com.android.internal.annotations.GuardedBy("ContextImpl.class")
    private static android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.io.File, android.app.SharedPreferencesImpl>> sSharedPrefsCache;

    /**
     * Map from preference name to generated path.
     */
    @com.android.internal.annotations.GuardedBy("ContextImpl.class")
    private android.util.ArrayMap<java.lang.String, java.io.File> mSharedPrefsPaths;

    final android.app.ActivityThread mMainThread;

    final android.app.LoadedApk mPackageInfo;

    private final android.os.IBinder mActivityToken;

    private final android.os.UserHandle mUser;

    private final android.app.ContextImpl.ApplicationContentResolver mContentResolver;

    private final java.lang.String mBasePackageName;

    private final java.lang.String mOpPackageName;

    @android.annotation.NonNull
    private final android.app.ResourcesManager mResourcesManager;

    @android.annotation.NonNull
    private final android.content.res.Resources mResources;

    @android.annotation.Nullable
    private android.view.Display mDisplay;// may be null if default display


    private final int mFlags;

    private android.content.Context mOuterContext;

    private int mThemeResource = 0;

    private android.content.res.Resources.Theme mTheme = null;

    private android.content.pm.PackageManager mPackageManager;

    private android.content.Context mReceiverRestrictedContext = null;

    private final java.lang.Object mSync = new java.lang.Object();

    @com.android.internal.annotations.GuardedBy("mSync")
    private java.io.File mDatabasesDir;

    @com.android.internal.annotations.GuardedBy("mSync")
    private java.io.File mPreferencesDir;

    @com.android.internal.annotations.GuardedBy("mSync")
    private java.io.File mFilesDir;

    @com.android.internal.annotations.GuardedBy("mSync")
    private java.io.File mNoBackupFilesDir;

    @com.android.internal.annotations.GuardedBy("mSync")
    private java.io.File mCacheDir;

    @com.android.internal.annotations.GuardedBy("mSync")
    private java.io.File mCodeCacheDir;

    // The system service cache for the system services that are cached per-ContextImpl.
    final java.lang.Object[] mServiceCache = android.app.SystemServiceRegistry.createServiceCache();

    static android.app.ContextImpl getImpl(android.content.Context context) {
        android.content.Context nextContext;
        while ((context instanceof android.content.ContextWrapper) && ((nextContext = ((android.content.ContextWrapper) (context)).getBaseContext()) != null)) {
            context = nextContext;
        } 
        return ((android.app.ContextImpl) (context));
    }

    @java.lang.Override
    public android.content.res.AssetManager getAssets() {
        return getResources().getAssets();
    }

    @java.lang.Override
    public android.content.res.Resources getResources() {
        return mResources;
    }

    @java.lang.Override
    public android.content.pm.PackageManager getPackageManager() {
        if (mPackageManager != null) {
            return mPackageManager;
        }
        android.content.pm.IPackageManager pm = android.app.ActivityThread.getPackageManager();
        if (pm != null) {
            // Doesn't matter if we make more than one instance.
            return mPackageManager = new android.app.ApplicationPackageManager(this, pm);
        }
        return null;
    }

    @java.lang.Override
    public android.content.ContentResolver getContentResolver() {
        return mContentResolver;
    }

    @java.lang.Override
    public android.os.Looper getMainLooper() {
        return mMainThread.getLooper();
    }

    @java.lang.Override
    public android.content.Context getApplicationContext() {
        return mPackageInfo != null ? mPackageInfo.getApplication() : mMainThread.getApplication();
    }

    @java.lang.Override
    public void setTheme(int resId) {
        if (mThemeResource != resId) {
            mThemeResource = resId;
            initializeTheme();
        }
    }

    @java.lang.Override
    public int getThemeResId() {
        return mThemeResource;
    }

    @java.lang.Override
    public android.content.res.Resources.Theme getTheme() {
        if (mTheme != null) {
            return mTheme;
        }
        mThemeResource = android.content.res.Resources.selectDefaultTheme(mThemeResource, getOuterContext().getApplicationInfo().targetSdkVersion);
        initializeTheme();
        return mTheme;
    }

    private void initializeTheme() {
        if (mTheme == null) {
            mTheme = mResources.newTheme();
        }
        mTheme.applyStyle(mThemeResource, true);
    }

    @java.lang.Override
    public java.lang.ClassLoader getClassLoader() {
        return mPackageInfo != null ? mPackageInfo.getClassLoader() : java.lang.ClassLoader.getSystemClassLoader();
    }

    @java.lang.Override
    public java.lang.String getPackageName() {
        if (mPackageInfo != null) {
            return mPackageInfo.getPackageName();
        }
        // No mPackageInfo means this is a Context for the system itself,
        // and this here is its name.
        return "android";
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.lang.String getBasePackageName() {
        return mBasePackageName != null ? mBasePackageName : getPackageName();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.lang.String getOpPackageName() {
        return mOpPackageName != null ? mOpPackageName : getBasePackageName();
    }

    @java.lang.Override
    public android.content.pm.ApplicationInfo getApplicationInfo() {
        if (mPackageInfo != null) {
            return mPackageInfo.getApplicationInfo();
        }
        throw new java.lang.RuntimeException("Not supported in system context");
    }

    @java.lang.Override
    public java.lang.String getPackageResourcePath() {
        if (mPackageInfo != null) {
            return mPackageInfo.getResDir();
        }
        throw new java.lang.RuntimeException("Not supported in system context");
    }

    @java.lang.Override
    public java.lang.String getPackageCodePath() {
        if (mPackageInfo != null) {
            return mPackageInfo.getAppDir();
        }
        throw new java.lang.RuntimeException("Not supported in system context");
    }

    @java.lang.Override
    public android.content.SharedPreferences getSharedPreferences(java.lang.String name, int mode) {
        // At least one application in the world actually passes in a null
        // name.  This happened to work because when we generated the file name
        // we would stringify it to "null.xml".  Nice.
        if (mPackageInfo.getApplicationInfo().targetSdkVersion < android.os.Build.VERSION_CODES.KITKAT) {
            if (name == null) {
                name = "null";
            }
        }
        java.io.File file;
        synchronized(android.app.ContextImpl.class) {
            if (mSharedPrefsPaths == null) {
                mSharedPrefsPaths = new android.util.ArrayMap<>();
            }
            file = mSharedPrefsPaths.get(name);
            if (file == null) {
                file = getSharedPreferencesPath(name);
                mSharedPrefsPaths.put(name, file);
            }
        }
        return getSharedPreferences(file, mode);
    }

    @java.lang.Override
    public android.content.SharedPreferences getSharedPreferences(java.io.File file, int mode) {
        checkMode(mode);
        android.app.SharedPreferencesImpl sp;
        synchronized(android.app.ContextImpl.class) {
            final android.util.ArrayMap<java.io.File, android.app.SharedPreferencesImpl> cache = getSharedPreferencesCacheLocked();
            sp = cache.get(file);
            if (sp == null) {
                sp = new android.app.SharedPreferencesImpl(file, mode);
                cache.put(file, sp);
                return sp;
            }
        }
        if (((mode & android.content.Context.MODE_MULTI_PROCESS) != 0) || (getApplicationInfo().targetSdkVersion < android.os.Build.VERSION_CODES.HONEYCOMB)) {
            // If somebody else (some other process) changed the prefs
            // file behind our back, we reload it.  This has been the
            // historical (if undocumented) behavior.
            sp.startReloadIfChangedUnexpectedly();
        }
        return sp;
    }

    private android.util.ArrayMap<java.io.File, android.app.SharedPreferencesImpl> getSharedPreferencesCacheLocked() {
        if (android.app.ContextImpl.sSharedPrefsCache == null) {
            android.app.ContextImpl.sSharedPrefsCache = new android.util.ArrayMap<>();
        }
        final java.lang.String packageName = getPackageName();
        android.util.ArrayMap<java.io.File, android.app.SharedPreferencesImpl> packagePrefs = android.app.ContextImpl.sSharedPrefsCache.get(packageName);
        if (packagePrefs == null) {
            packagePrefs = new android.util.ArrayMap<>();
            android.app.ContextImpl.sSharedPrefsCache.put(packageName, packagePrefs);
        }
        return packagePrefs;
    }

    /**
     * Try our best to migrate all files from source to target that match
     * requested prefix.
     *
     * @return the number of files moved, or -1 if there was trouble.
     */
    private static int moveFiles(java.io.File sourceDir, java.io.File targetDir, final java.lang.String prefix) {
        final java.io.File[] sourceFiles = android.os.FileUtils.listFilesOrEmpty(sourceDir, new java.io.FilenameFilter() {
            @java.lang.Override
            public boolean accept(java.io.File dir, java.lang.String name) {
                return name.startsWith(prefix);
            }
        });
        int res = 0;
        for (java.io.File sourceFile : sourceFiles) {
            final java.io.File targetFile = new java.io.File(targetDir, sourceFile.getName());
            android.util.Log.d(android.app.ContextImpl.TAG, (("Migrating " + sourceFile) + " to ") + targetFile);
            try {
                android.os.FileUtils.copyFileOrThrow(sourceFile, targetFile);
                android.os.FileUtils.copyPermissions(sourceFile, targetFile);
                if (!sourceFile.delete()) {
                    throw new java.io.IOException("Failed to clean up " + sourceFile);
                }
                if (res != (-1)) {
                    res++;
                }
            } catch (java.io.IOException e) {
                android.util.Log.w(android.app.ContextImpl.TAG, (("Failed to migrate " + sourceFile) + ": ") + e);
                res = -1;
            }
        }
        return res;
    }

    @java.lang.Override
    public boolean moveSharedPreferencesFrom(android.content.Context sourceContext, java.lang.String name) {
        synchronized(android.app.ContextImpl.class) {
            final java.io.File source = sourceContext.getSharedPreferencesPath(name);
            final java.io.File target = getSharedPreferencesPath(name);
            final int res = android.app.ContextImpl.moveFiles(source.getParentFile(), target.getParentFile(), source.getName());
            if (res > 0) {
                // We moved at least one file, so evict any in-memory caches for
                // either location
                final android.util.ArrayMap<java.io.File, android.app.SharedPreferencesImpl> cache = getSharedPreferencesCacheLocked();
                cache.remove(source);
                cache.remove(target);
            }
            return res != (-1);
        }
    }

    @java.lang.Override
    public boolean deleteSharedPreferences(java.lang.String name) {
        synchronized(android.app.ContextImpl.class) {
            final java.io.File prefs = getSharedPreferencesPath(name);
            final java.io.File prefsBackup = android.app.SharedPreferencesImpl.makeBackupFile(prefs);
            // Evict any in-memory caches
            final android.util.ArrayMap<java.io.File, android.app.SharedPreferencesImpl> cache = getSharedPreferencesCacheLocked();
            cache.remove(prefs);
            prefs.delete();
            prefsBackup.delete();
            // We failed if files are still lingering
            return !(prefs.exists() || prefsBackup.exists());
        }
    }

    private java.io.File getPreferencesDir() {
        synchronized(mSync) {
            if (mPreferencesDir == null) {
                mPreferencesDir = new java.io.File(getDataDir(), "shared_prefs");
            }
            return android.app.ContextImpl.ensurePrivateDirExists(mPreferencesDir);
        }
    }

    @java.lang.Override
    public java.io.FileInputStream openFileInput(java.lang.String name) throws java.io.FileNotFoundException {
        java.io.File f = makeFilename(getFilesDir(), name);
        return new java.io.FileInputStream(f);
    }

    @java.lang.Override
    public java.io.FileOutputStream openFileOutput(java.lang.String name, int mode) throws java.io.FileNotFoundException {
        checkMode(mode);
        final boolean append = (mode & android.content.Context.MODE_APPEND) != 0;
        java.io.File f = makeFilename(getFilesDir(), name);
        try {
            java.io.FileOutputStream fos = new java.io.FileOutputStream(f, append);
            android.app.ContextImpl.setFilePermissionsFromMode(f.getPath(), mode, 0);
            return fos;
        } catch (java.io.FileNotFoundException e) {
        }
        java.io.File parent = f.getParentFile();
        parent.mkdir();
        android.os.FileUtils.setPermissions(parent.getPath(), (android.os.FileUtils.S_IRWXU | android.os.FileUtils.S_IRWXG) | android.os.FileUtils.S_IXOTH, -1, -1);
        java.io.FileOutputStream fos = new java.io.FileOutputStream(f, append);
        android.app.ContextImpl.setFilePermissionsFromMode(f.getPath(), mode, 0);
        return fos;
    }

    @java.lang.Override
    public boolean deleteFile(java.lang.String name) {
        java.io.File f = makeFilename(getFilesDir(), name);
        return f.delete();
    }

    /**
     * Common-path handling of app data dir creation
     */
    private static java.io.File ensurePrivateDirExists(java.io.File file) {
        if (!file.exists()) {
            try {
                android.system.Os.mkdir(file.getAbsolutePath(), 0771);
                android.system.Os.chmod(file.getAbsolutePath(), 0771);
            } catch (android.system.ErrnoException e) {
                if (e.errno == android.system.OsConstants.EEXIST) {
                    // We must have raced with someone; that's okay
                } else {
                    android.util.Log.w(android.app.ContextImpl.TAG, (("Failed to ensure " + file) + ": ") + e.getMessage());
                }
            }
        }
        return file;
    }

    @java.lang.Override
    public java.io.File getFilesDir() {
        synchronized(mSync) {
            if (mFilesDir == null) {
                mFilesDir = new java.io.File(getDataDir(), "files");
            }
            return android.app.ContextImpl.ensurePrivateDirExists(mFilesDir);
        }
    }

    @java.lang.Override
    public java.io.File getNoBackupFilesDir() {
        synchronized(mSync) {
            if (mNoBackupFilesDir == null) {
                mNoBackupFilesDir = new java.io.File(getDataDir(), "no_backup");
            }
            return android.app.ContextImpl.ensurePrivateDirExists(mNoBackupFilesDir);
        }
    }

    @java.lang.Override
    public java.io.File getExternalFilesDir(java.lang.String type) {
        // Operates on primary external storage
        return getExternalFilesDirs(type)[0];
    }

    @java.lang.Override
    public java.io.File[] getExternalFilesDirs(java.lang.String type) {
        synchronized(mSync) {
            java.io.File[] dirs = android.os.Environment.buildExternalStorageAppFilesDirs(getPackageName());
            if (type != null) {
                dirs = android.os.Environment.buildPaths(dirs, type);
            }
            return ensureExternalDirsExistOrFilter(dirs);
        }
    }

    @java.lang.Override
    public java.io.File getObbDir() {
        // Operates on primary external storage
        return getObbDirs()[0];
    }

    @java.lang.Override
    public java.io.File[] getObbDirs() {
        synchronized(mSync) {
            java.io.File[] dirs = android.os.Environment.buildExternalStorageAppObbDirs(getPackageName());
            return ensureExternalDirsExistOrFilter(dirs);
        }
    }

    @java.lang.Override
    public java.io.File getCacheDir() {
        synchronized(mSync) {
            if (mCacheDir == null) {
                mCacheDir = new java.io.File(getDataDir(), "cache");
            }
            return android.app.ContextImpl.ensurePrivateDirExists(mCacheDir);
        }
    }

    @java.lang.Override
    public java.io.File getCodeCacheDir() {
        synchronized(mSync) {
            if (mCodeCacheDir == null) {
                mCodeCacheDir = new java.io.File(getDataDir(), "code_cache");
            }
            return android.app.ContextImpl.ensurePrivateDirExists(mCodeCacheDir);
        }
    }

    @java.lang.Override
    public java.io.File getExternalCacheDir() {
        // Operates on primary external storage
        return getExternalCacheDirs()[0];
    }

    @java.lang.Override
    public java.io.File[] getExternalCacheDirs() {
        synchronized(mSync) {
            java.io.File[] dirs = android.os.Environment.buildExternalStorageAppCacheDirs(getPackageName());
            return ensureExternalDirsExistOrFilter(dirs);
        }
    }

    @java.lang.Override
    public java.io.File[] getExternalMediaDirs() {
        synchronized(mSync) {
            java.io.File[] dirs = android.os.Environment.buildExternalStorageAppMediaDirs(getPackageName());
            return ensureExternalDirsExistOrFilter(dirs);
        }
    }

    @java.lang.Override
    public java.io.File getFileStreamPath(java.lang.String name) {
        return makeFilename(getFilesDir(), name);
    }

    @java.lang.Override
    public java.io.File getSharedPreferencesPath(java.lang.String name) {
        return makeFilename(getPreferencesDir(), name + ".xml");
    }

    @java.lang.Override
    public java.lang.String[] fileList() {
        return android.os.FileUtils.listOrEmpty(getFilesDir());
    }

    @java.lang.Override
    public android.database.sqlite.SQLiteDatabase openOrCreateDatabase(java.lang.String name, int mode, android.database.sqlite.SQLiteDatabase.CursorFactory factory) {
        return openOrCreateDatabase(name, mode, factory, null);
    }

    @java.lang.Override
    public android.database.sqlite.SQLiteDatabase openOrCreateDatabase(java.lang.String name, int mode, android.database.sqlite.SQLiteDatabase.CursorFactory factory, android.database.DatabaseErrorHandler errorHandler) {
        checkMode(mode);
        java.io.File f = getDatabasePath(name);
        int flags = android.database.sqlite.SQLiteDatabase.CREATE_IF_NECESSARY;
        if ((mode & android.content.Context.MODE_ENABLE_WRITE_AHEAD_LOGGING) != 0) {
            flags |= android.database.sqlite.SQLiteDatabase.ENABLE_WRITE_AHEAD_LOGGING;
        }
        if ((mode & android.content.Context.MODE_NO_LOCALIZED_COLLATORS) != 0) {
            flags |= android.database.sqlite.SQLiteDatabase.NO_LOCALIZED_COLLATORS;
        }
        android.database.sqlite.SQLiteDatabase db = android.database.sqlite.SQLiteDatabase.openDatabase(f.getPath(), factory, flags, errorHandler);
        android.app.ContextImpl.setFilePermissionsFromMode(f.getPath(), mode, 0);
        return db;
    }

    @java.lang.Override
    public boolean moveDatabaseFrom(android.content.Context sourceContext, java.lang.String name) {
        synchronized(android.app.ContextImpl.class) {
            final java.io.File source = sourceContext.getDatabasePath(name);
            final java.io.File target = getDatabasePath(name);
            return android.app.ContextImpl.moveFiles(source.getParentFile(), target.getParentFile(), source.getName()) != (-1);
        }
    }

    @java.lang.Override
    public boolean deleteDatabase(java.lang.String name) {
        try {
            java.io.File f = getDatabasePath(name);
            return android.database.sqlite.SQLiteDatabase.deleteDatabase(f);
        } catch (java.lang.Exception e) {
        }
        return false;
    }

    @java.lang.Override
    public java.io.File getDatabasePath(java.lang.String name) {
        java.io.File dir;
        java.io.File f;
        if (name.charAt(0) == java.io.File.separatorChar) {
            java.lang.String dirPath = name.substring(0, name.lastIndexOf(java.io.File.separatorChar));
            dir = new java.io.File(dirPath);
            name = name.substring(name.lastIndexOf(java.io.File.separatorChar));
            f = new java.io.File(dir, name);
            if ((!dir.isDirectory()) && dir.mkdir()) {
                android.os.FileUtils.setPermissions(dir.getPath(), (android.os.FileUtils.S_IRWXU | android.os.FileUtils.S_IRWXG) | android.os.FileUtils.S_IXOTH, -1, -1);
            }
        } else {
            dir = getDatabasesDir();
            f = makeFilename(dir, name);
        }
        return f;
    }

    @java.lang.Override
    public java.lang.String[] databaseList() {
        return android.os.FileUtils.listOrEmpty(getDatabasesDir());
    }

    private java.io.File getDatabasesDir() {
        synchronized(mSync) {
            if (mDatabasesDir == null) {
                if ("android".equals(getPackageName())) {
                    mDatabasesDir = new java.io.File("/data/system");
                } else {
                    mDatabasesDir = new java.io.File(getDataDir(), "databases");
                }
            }
            return android.app.ContextImpl.ensurePrivateDirExists(mDatabasesDir);
        }
    }

    @java.lang.Override
    @java.lang.Deprecated
    public android.graphics.drawable.Drawable getWallpaper() {
        return getWallpaperManager().getDrawable();
    }

    @java.lang.Override
    @java.lang.Deprecated
    public android.graphics.drawable.Drawable peekWallpaper() {
        return getWallpaperManager().peekDrawable();
    }

    @java.lang.Override
    @java.lang.Deprecated
    public int getWallpaperDesiredMinimumWidth() {
        return getWallpaperManager().getDesiredMinimumWidth();
    }

    @java.lang.Override
    @java.lang.Deprecated
    public int getWallpaperDesiredMinimumHeight() {
        return getWallpaperManager().getDesiredMinimumHeight();
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void setWallpaper(android.graphics.Bitmap bitmap) throws java.io.IOException {
        getWallpaperManager().setBitmap(bitmap);
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void setWallpaper(java.io.InputStream data) throws java.io.IOException {
        getWallpaperManager().setStream(data);
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void clearWallpaper() throws java.io.IOException {
        getWallpaperManager().clear();
    }

    private android.app.WallpaperManager getWallpaperManager() {
        return getSystemService(android.app.WallpaperManager.class);
    }

    @java.lang.Override
    public void startActivity(android.content.Intent intent) {
        warnIfCallingFromSystemProcess();
        startActivity(intent, null);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void startActivityAsUser(android.content.Intent intent, android.os.UserHandle user) {
        startActivityAsUser(intent, null, user);
    }

    @java.lang.Override
    public void startActivity(android.content.Intent intent, android.os.Bundle options) {
        warnIfCallingFromSystemProcess();
        // Calling start activity from outside an activity without FLAG_ACTIVITY_NEW_TASK is
        // generally not allowed, except if the caller specifies the task id the activity should
        // be launched in.
        if ((((intent.getFlags() & android.content.Intent.FLAG_ACTIVITY_NEW_TASK) == 0) && (options != null)) && (android.app.ActivityOptions.fromBundle(options).getLaunchTaskId() == (-1))) {
            throw new android.util.AndroidRuntimeException("Calling startActivity() from outside of an Activity " + (" context requires the FLAG_ACTIVITY_NEW_TASK flag." + " Is this really what you want?"));
        }
        mMainThread.getInstrumentation().execStartActivity(getOuterContext(), mMainThread.getApplicationThread(), null, ((android.app.Activity) (null)), intent, -1, options);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void startActivityAsUser(android.content.Intent intent, android.os.Bundle options, android.os.UserHandle user) {
        try {
            android.app.ActivityManagerNative.getDefault().startActivityAsUser(mMainThread.getApplicationThread(), getBasePackageName(), intent, intent.resolveTypeIfNeeded(getContentResolver()), null, null, 0, android.content.Intent.FLAG_ACTIVITY_NEW_TASK, null, options, user.getIdentifier());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void startActivities(android.content.Intent[] intents) {
        warnIfCallingFromSystemProcess();
        startActivities(intents, null);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void startActivitiesAsUser(android.content.Intent[] intents, android.os.Bundle options, android.os.UserHandle userHandle) {
        if ((intents[0].getFlags() & android.content.Intent.FLAG_ACTIVITY_NEW_TASK) == 0) {
            throw new android.util.AndroidRuntimeException("Calling startActivities() from outside of an Activity " + (" context requires the FLAG_ACTIVITY_NEW_TASK flag on first Intent." + " Is this really what you want?"));
        }
        mMainThread.getInstrumentation().execStartActivitiesAsUser(getOuterContext(), mMainThread.getApplicationThread(), null, ((android.app.Activity) (null)), intents, options, userHandle.getIdentifier());
    }

    @java.lang.Override
    public void startActivities(android.content.Intent[] intents, android.os.Bundle options) {
        warnIfCallingFromSystemProcess();
        if ((intents[0].getFlags() & android.content.Intent.FLAG_ACTIVITY_NEW_TASK) == 0) {
            throw new android.util.AndroidRuntimeException("Calling startActivities() from outside of an Activity " + (" context requires the FLAG_ACTIVITY_NEW_TASK flag on first Intent." + " Is this really what you want?"));
        }
        mMainThread.getInstrumentation().execStartActivities(getOuterContext(), mMainThread.getApplicationThread(), null, ((android.app.Activity) (null)), intents, options);
    }

    @java.lang.Override
    public void startIntentSender(android.content.IntentSender intent, android.content.Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws android.content.IntentSender.SendIntentException {
        startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, null);
    }

    @java.lang.Override
    public void startIntentSender(android.content.IntentSender intent, android.content.Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, android.os.Bundle options) throws android.content.IntentSender.SendIntentException {
        try {
            java.lang.String resolvedType = null;
            if (fillInIntent != null) {
                fillInIntent.migrateExtraStreamToClipData();
                fillInIntent.prepareToLeaveProcess(this);
                resolvedType = fillInIntent.resolveTypeIfNeeded(getContentResolver());
            }
            int result = android.app.ActivityManagerNative.getDefault().startActivityIntentSender(mMainThread.getApplicationThread(), intent, fillInIntent, resolvedType, null, null, 0, flagsMask, flagsValues, options);
            if (result == android.app.ActivityManager.START_CANCELED) {
                throw new android.content.IntentSender.SendIntentException();
            }
            android.app.Instrumentation.checkStartActivityResult(result, null);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void sendBroadcast(android.content.Intent intent) {
        warnIfCallingFromSystemProcess();
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().broadcastIntent(mMainThread.getApplicationThread(), intent, resolvedType, null, android.app.Activity.RESULT_OK, null, null, null, android.app.AppOpsManager.OP_NONE, null, false, false, getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void sendBroadcast(android.content.Intent intent, java.lang.String receiverPermission) {
        warnIfCallingFromSystemProcess();
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        java.lang.String[] receiverPermissions = (receiverPermission == null) ? null : new java.lang.String[]{ receiverPermission };
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().broadcastIntent(mMainThread.getApplicationThread(), intent, resolvedType, null, android.app.Activity.RESULT_OK, null, null, receiverPermissions, android.app.AppOpsManager.OP_NONE, null, false, false, getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void sendBroadcastMultiplePermissions(android.content.Intent intent, java.lang.String[] receiverPermissions) {
        warnIfCallingFromSystemProcess();
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().broadcastIntent(mMainThread.getApplicationThread(), intent, resolvedType, null, android.app.Activity.RESULT_OK, null, null, receiverPermissions, android.app.AppOpsManager.OP_NONE, null, false, false, getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void sendBroadcast(android.content.Intent intent, java.lang.String receiverPermission, android.os.Bundle options) {
        warnIfCallingFromSystemProcess();
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        java.lang.String[] receiverPermissions = (receiverPermission == null) ? null : new java.lang.String[]{ receiverPermission };
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().broadcastIntent(mMainThread.getApplicationThread(), intent, resolvedType, null, android.app.Activity.RESULT_OK, null, null, receiverPermissions, android.app.AppOpsManager.OP_NONE, options, false, false, getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void sendBroadcast(android.content.Intent intent, java.lang.String receiverPermission, int appOp) {
        warnIfCallingFromSystemProcess();
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        java.lang.String[] receiverPermissions = (receiverPermission == null) ? null : new java.lang.String[]{ receiverPermission };
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().broadcastIntent(mMainThread.getApplicationThread(), intent, resolvedType, null, android.app.Activity.RESULT_OK, null, null, receiverPermissions, appOp, null, false, false, getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void sendOrderedBroadcast(android.content.Intent intent, java.lang.String receiverPermission) {
        warnIfCallingFromSystemProcess();
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        java.lang.String[] receiverPermissions = (receiverPermission == null) ? null : new java.lang.String[]{ receiverPermission };
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().broadcastIntent(mMainThread.getApplicationThread(), intent, resolvedType, null, android.app.Activity.RESULT_OK, null, null, receiverPermissions, android.app.AppOpsManager.OP_NONE, null, true, false, getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void sendOrderedBroadcast(android.content.Intent intent, java.lang.String receiverPermission, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        sendOrderedBroadcast(intent, receiverPermission, android.app.AppOpsManager.OP_NONE, resultReceiver, scheduler, initialCode, initialData, initialExtras, null);
    }

    @java.lang.Override
    public void sendOrderedBroadcast(android.content.Intent intent, java.lang.String receiverPermission, android.os.Bundle options, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        sendOrderedBroadcast(intent, receiverPermission, android.app.AppOpsManager.OP_NONE, resultReceiver, scheduler, initialCode, initialData, initialExtras, options);
    }

    @java.lang.Override
    public void sendOrderedBroadcast(android.content.Intent intent, java.lang.String receiverPermission, int appOp, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        sendOrderedBroadcast(intent, receiverPermission, appOp, resultReceiver, scheduler, initialCode, initialData, initialExtras, null);
    }

    void sendOrderedBroadcast(android.content.Intent intent, java.lang.String receiverPermission, int appOp, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras, android.os.Bundle options) {
        warnIfCallingFromSystemProcess();
        android.content.IIntentReceiver rd = null;
        if (resultReceiver != null) {
            if (mPackageInfo != null) {
                if (scheduler == null) {
                    scheduler = mMainThread.getHandler();
                }
                rd = mPackageInfo.getReceiverDispatcher(resultReceiver, getOuterContext(), scheduler, mMainThread.getInstrumentation(), false);
            } else {
                if (scheduler == null) {
                    scheduler = mMainThread.getHandler();
                }
                rd = new android.app.LoadedApk.ReceiverDispatcher(resultReceiver, getOuterContext(), scheduler, null, false).getIIntentReceiver();
            }
        }
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        java.lang.String[] receiverPermissions = (receiverPermission == null) ? null : new java.lang.String[]{ receiverPermission };
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().broadcastIntent(mMainThread.getApplicationThread(), intent, resolvedType, rd, initialCode, initialData, initialExtras, receiverPermissions, appOp, options, true, false, getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void sendBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user) {
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().broadcastIntent(mMainThread.getApplicationThread(), intent, resolvedType, null, android.app.Activity.RESULT_OK, null, null, null, android.app.AppOpsManager.OP_NONE, null, false, false, user.getIdentifier());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void sendBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, java.lang.String receiverPermission) {
        sendBroadcastAsUser(intent, user, receiverPermission, android.app.AppOpsManager.OP_NONE);
    }

    @java.lang.Override
    public void sendBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, java.lang.String receiverPermission, int appOp) {
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        java.lang.String[] receiverPermissions = (receiverPermission == null) ? null : new java.lang.String[]{ receiverPermission };
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().broadcastIntent(mMainThread.getApplicationThread(), intent, resolvedType, null, android.app.Activity.RESULT_OK, null, null, receiverPermissions, appOp, null, false, false, user.getIdentifier());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void sendOrderedBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, java.lang.String receiverPermission, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        sendOrderedBroadcastAsUser(intent, user, receiverPermission, android.app.AppOpsManager.OP_NONE, null, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    @java.lang.Override
    public void sendOrderedBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, java.lang.String receiverPermission, int appOp, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        sendOrderedBroadcastAsUser(intent, user, receiverPermission, appOp, null, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    @java.lang.Override
    public void sendOrderedBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, java.lang.String receiverPermission, int appOp, android.os.Bundle options, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        android.content.IIntentReceiver rd = null;
        if (resultReceiver != null) {
            if (mPackageInfo != null) {
                if (scheduler == null) {
                    scheduler = mMainThread.getHandler();
                }
                rd = mPackageInfo.getReceiverDispatcher(resultReceiver, getOuterContext(), scheduler, mMainThread.getInstrumentation(), false);
            } else {
                if (scheduler == null) {
                    scheduler = mMainThread.getHandler();
                }
                rd = new android.app.LoadedApk.ReceiverDispatcher(resultReceiver, getOuterContext(), scheduler, null, false).getIIntentReceiver();
            }
        }
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        java.lang.String[] receiverPermissions = (receiverPermission == null) ? null : new java.lang.String[]{ receiverPermission };
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().broadcastIntent(mMainThread.getApplicationThread(), intent, resolvedType, rd, initialCode, initialData, initialExtras, receiverPermissions, appOp, options, true, false, user.getIdentifier());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void sendStickyBroadcast(android.content.Intent intent) {
        warnIfCallingFromSystemProcess();
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().broadcastIntent(mMainThread.getApplicationThread(), intent, resolvedType, null, android.app.Activity.RESULT_OK, null, null, null, android.app.AppOpsManager.OP_NONE, null, false, true, getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void sendStickyOrderedBroadcast(android.content.Intent intent, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        warnIfCallingFromSystemProcess();
        android.content.IIntentReceiver rd = null;
        if (resultReceiver != null) {
            if (mPackageInfo != null) {
                if (scheduler == null) {
                    scheduler = mMainThread.getHandler();
                }
                rd = mPackageInfo.getReceiverDispatcher(resultReceiver, getOuterContext(), scheduler, mMainThread.getInstrumentation(), false);
            } else {
                if (scheduler == null) {
                    scheduler = mMainThread.getHandler();
                }
                rd = new android.app.LoadedApk.ReceiverDispatcher(resultReceiver, getOuterContext(), scheduler, null, false).getIIntentReceiver();
            }
        }
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().broadcastIntent(mMainThread.getApplicationThread(), intent, resolvedType, rd, initialCode, initialData, initialExtras, null, android.app.AppOpsManager.OP_NONE, null, true, true, getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void removeStickyBroadcast(android.content.Intent intent) {
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        if (resolvedType != null) {
            intent = new android.content.Intent(intent);
            intent.setDataAndType(intent.getData(), resolvedType);
        }
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().unbroadcastIntent(mMainThread.getApplicationThread(), intent, getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void sendStickyBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user) {
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().broadcastIntent(mMainThread.getApplicationThread(), intent, resolvedType, null, android.app.Activity.RESULT_OK, null, null, null, android.app.AppOpsManager.OP_NONE, null, false, true, user.getIdentifier());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void sendStickyBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, android.os.Bundle options) {
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().broadcastIntent(mMainThread.getApplicationThread(), intent, resolvedType, null, android.app.Activity.RESULT_OK, null, null, null, android.app.AppOpsManager.OP_NONE, options, false, true, user.getIdentifier());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void sendStickyOrderedBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user, android.content.BroadcastReceiver resultReceiver, android.os.Handler scheduler, int initialCode, java.lang.String initialData, android.os.Bundle initialExtras) {
        android.content.IIntentReceiver rd = null;
        if (resultReceiver != null) {
            if (mPackageInfo != null) {
                if (scheduler == null) {
                    scheduler = mMainThread.getHandler();
                }
                rd = mPackageInfo.getReceiverDispatcher(resultReceiver, getOuterContext(), scheduler, mMainThread.getInstrumentation(), false);
            } else {
                if (scheduler == null) {
                    scheduler = mMainThread.getHandler();
                }
                rd = new android.app.LoadedApk.ReceiverDispatcher(resultReceiver, getOuterContext(), scheduler, null, false).getIIntentReceiver();
            }
        }
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().broadcastIntent(mMainThread.getApplicationThread(), intent, resolvedType, rd, initialCode, initialData, initialExtras, null, android.app.AppOpsManager.OP_NONE, null, true, true, user.getIdentifier());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void removeStickyBroadcastAsUser(android.content.Intent intent, android.os.UserHandle user) {
        java.lang.String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        if (resolvedType != null) {
            intent = new android.content.Intent(intent);
            intent.setDataAndType(intent.getData(), resolvedType);
        }
        try {
            intent.prepareToLeaveProcess(this);
            android.app.ActivityManagerNative.getDefault().unbroadcastIntent(mMainThread.getApplicationThread(), intent, user.getIdentifier());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public android.content.Intent registerReceiver(android.content.BroadcastReceiver receiver, android.content.IntentFilter filter) {
        return registerReceiver(receiver, filter, null, null);
    }

    @java.lang.Override
    public android.content.Intent registerReceiver(android.content.BroadcastReceiver receiver, android.content.IntentFilter filter, java.lang.String broadcastPermission, android.os.Handler scheduler) {
        return registerReceiverInternal(receiver, getUserId(), filter, broadcastPermission, scheduler, getOuterContext());
    }

    @java.lang.Override
    public android.content.Intent registerReceiverAsUser(android.content.BroadcastReceiver receiver, android.os.UserHandle user, android.content.IntentFilter filter, java.lang.String broadcastPermission, android.os.Handler scheduler) {
        return registerReceiverInternal(receiver, user.getIdentifier(), filter, broadcastPermission, scheduler, getOuterContext());
    }

    private android.content.Intent registerReceiverInternal(android.content.BroadcastReceiver receiver, int userId, android.content.IntentFilter filter, java.lang.String broadcastPermission, android.os.Handler scheduler, android.content.Context context) {
        android.content.IIntentReceiver rd = null;
        if (receiver != null) {
            if ((mPackageInfo != null) && (context != null)) {
                if (scheduler == null) {
                    scheduler = mMainThread.getHandler();
                }
                rd = mPackageInfo.getReceiverDispatcher(receiver, context, scheduler, mMainThread.getInstrumentation(), true);
            } else {
                if (scheduler == null) {
                    scheduler = mMainThread.getHandler();
                }
                rd = new android.app.LoadedApk.ReceiverDispatcher(receiver, context, scheduler, null, true).getIIntentReceiver();
            }
        }
        try {
            final android.content.Intent intent = android.app.ActivityManagerNative.getDefault().registerReceiver(mMainThread.getApplicationThread(), mBasePackageName, rd, filter, broadcastPermission, userId);
            if (intent != null) {
                intent.setExtrasClassLoader(getClassLoader());
                intent.prepareToEnterProcess();
            }
            return intent;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void unregisterReceiver(android.content.BroadcastReceiver receiver) {
        if (mPackageInfo != null) {
            android.content.IIntentReceiver rd = mPackageInfo.forgetReceiverDispatcher(getOuterContext(), receiver);
            try {
                android.app.ActivityManagerNative.getDefault().unregisterReceiver(rd);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new java.lang.RuntimeException("Not supported in system context");
        }
    }

    private void validateServiceIntent(android.content.Intent service) {
        if ((service.getComponent() == null) && (service.getPackage() == null)) {
            if (getApplicationInfo().targetSdkVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                java.lang.IllegalArgumentException ex = new java.lang.IllegalArgumentException("Service Intent must be explicit: " + service);
                throw ex;
            } else {
                android.util.Log.w(android.app.ContextImpl.TAG, (("Implicit intents with startService are not safe: " + service) + " ") + android.os.Debug.getCallers(2, 3));
            }
        }
    }

    @java.lang.Override
    public android.content.ComponentName startService(android.content.Intent service) {
        warnIfCallingFromSystemProcess();
        return startServiceCommon(service, mUser);
    }

    @java.lang.Override
    public boolean stopService(android.content.Intent service) {
        warnIfCallingFromSystemProcess();
        return stopServiceCommon(service, mUser);
    }

    @java.lang.Override
    public android.content.ComponentName startServiceAsUser(android.content.Intent service, android.os.UserHandle user) {
        return startServiceCommon(service, user);
    }

    private android.content.ComponentName startServiceCommon(android.content.Intent service, android.os.UserHandle user) {
        try {
            validateServiceIntent(service);
            service.prepareToLeaveProcess(this);
            android.content.ComponentName cn = android.app.ActivityManagerNative.getDefault().startService(mMainThread.getApplicationThread(), service, service.resolveTypeIfNeeded(getContentResolver()), getOpPackageName(), user.getIdentifier());
            if (cn != null) {
                if (cn.getPackageName().equals("!")) {
                    throw new java.lang.SecurityException((("Not allowed to start service " + service) + " without permission ") + cn.getClassName());
                } else
                    if (cn.getPackageName().equals("!!")) {
                        throw new java.lang.SecurityException((("Unable to start service " + service) + ": ") + cn.getClassName());
                    }

            }
            return cn;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public boolean stopServiceAsUser(android.content.Intent service, android.os.UserHandle user) {
        return stopServiceCommon(service, user);
    }

    private boolean stopServiceCommon(android.content.Intent service, android.os.UserHandle user) {
        try {
            validateServiceIntent(service);
            service.prepareToLeaveProcess(this);
            int res = android.app.ActivityManagerNative.getDefault().stopService(mMainThread.getApplicationThread(), service, service.resolveTypeIfNeeded(getContentResolver()), user.getIdentifier());
            if (res < 0) {
                throw new java.lang.SecurityException("Not allowed to stop service " + service);
            }
            return res != 0;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public boolean bindService(android.content.Intent service, android.content.ServiceConnection conn, int flags) {
        warnIfCallingFromSystemProcess();
        return bindServiceCommon(service, conn, flags, mMainThread.getHandler(), android.os.Process.myUserHandle());
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean bindServiceAsUser(android.content.Intent service, android.content.ServiceConnection conn, int flags, android.os.UserHandle user) {
        return bindServiceCommon(service, conn, flags, mMainThread.getHandler(), user);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean bindServiceAsUser(android.content.Intent service, android.content.ServiceConnection conn, int flags, android.os.Handler handler, android.os.UserHandle user) {
        if (handler == null) {
            throw new java.lang.IllegalArgumentException("handler must not be null.");
        }
        return bindServiceCommon(service, conn, flags, handler, user);
    }

    private boolean bindServiceCommon(android.content.Intent service, android.content.ServiceConnection conn, int flags, android.os.Handler handler, android.os.UserHandle user) {
        android.app.IServiceConnection sd;
        if (conn == null) {
            throw new java.lang.IllegalArgumentException("connection is null");
        }
        if (mPackageInfo != null) {
            sd = mPackageInfo.getServiceDispatcher(conn, getOuterContext(), handler, flags);
        } else {
            throw new java.lang.RuntimeException("Not supported in system context");
        }
        validateServiceIntent(service);
        try {
            android.os.IBinder token = getActivityToken();
            if ((((token == null) && ((flags & android.content.Context.BIND_AUTO_CREATE) == 0)) && (mPackageInfo != null)) && (mPackageInfo.getApplicationInfo().targetSdkVersion < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)) {
                flags |= android.content.Context.BIND_WAIVE_PRIORITY;
            }
            service.prepareToLeaveProcess(this);
            int res = android.app.ActivityManagerNative.getDefault().bindService(mMainThread.getApplicationThread(), getActivityToken(), service, service.resolveTypeIfNeeded(getContentResolver()), sd, flags, getOpPackageName(), user.getIdentifier());
            if (res < 0) {
                throw new java.lang.SecurityException("Not allowed to bind to service " + service);
            }
            return res != 0;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void unbindService(android.content.ServiceConnection conn) {
        if (conn == null) {
            throw new java.lang.IllegalArgumentException("connection is null");
        }
        if (mPackageInfo != null) {
            android.app.IServiceConnection sd = mPackageInfo.forgetServiceDispatcher(getOuterContext(), conn);
            try {
                android.app.ActivityManagerNative.getDefault().unbindService(sd);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new java.lang.RuntimeException("Not supported in system context");
        }
    }

    @java.lang.Override
    public boolean startInstrumentation(android.content.ComponentName className, java.lang.String profileFile, android.os.Bundle arguments) {
        try {
            if (arguments != null) {
                arguments.setAllowFds(false);
            }
            return /* ABI override */
            android.app.ActivityManagerNative.getDefault().startInstrumentation(className, profileFile, 0, arguments, null, null, getUserId(), null);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public java.lang.Object getSystemService(java.lang.String name) {
        return android.app.SystemServiceRegistry.getSystemService(this, name);
    }

    @java.lang.Override
    public java.lang.String getSystemServiceName(java.lang.Class<?> serviceClass) {
        return android.app.SystemServiceRegistry.getSystemServiceName(serviceClass);
    }

    @java.lang.Override
    public int checkPermission(java.lang.String permission, int pid, int uid) {
        if (permission == null) {
            throw new java.lang.IllegalArgumentException("permission is null");
        }
        try {
            return android.app.ActivityManagerNative.getDefault().checkPermission(permission, pid, uid);
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
    public int checkPermission(java.lang.String permission, int pid, int uid, android.os.IBinder callerToken) {
        if (permission == null) {
            throw new java.lang.IllegalArgumentException("permission is null");
        }
        try {
            return android.app.ActivityManagerNative.getDefault().checkPermissionWithToken(permission, pid, uid, callerToken);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public int checkCallingPermission(java.lang.String permission) {
        if (permission == null) {
            throw new java.lang.IllegalArgumentException("permission is null");
        }
        int pid = android.os.Binder.getCallingPid();
        if (pid != android.os.Process.myPid()) {
            return checkPermission(permission, pid, android.os.Binder.getCallingUid());
        }
        return android.content.pm.PackageManager.PERMISSION_DENIED;
    }

    @java.lang.Override
    public int checkCallingOrSelfPermission(java.lang.String permission) {
        if (permission == null) {
            throw new java.lang.IllegalArgumentException("permission is null");
        }
        return checkPermission(permission, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
    }

    @java.lang.Override
    public int checkSelfPermission(java.lang.String permission) {
        if (permission == null) {
            throw new java.lang.IllegalArgumentException("permission is null");
        }
        return checkPermission(permission, android.os.Process.myPid(), android.os.Process.myUid());
    }

    private void enforce(java.lang.String permission, int resultOfCheck, boolean selfToo, int uid, java.lang.String message) {
        if (resultOfCheck != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            throw new java.lang.SecurityException((((message != null ? message + ": " : "") + (selfToo ? ("Neither user " + uid) + " nor current process has " : ("uid " + uid) + " does not have ")) + permission) + ".");
        }
    }

    @java.lang.Override
    public void enforcePermission(java.lang.String permission, int pid, int uid, java.lang.String message) {
        enforce(permission, checkPermission(permission, pid, uid), false, uid, message);
    }

    @java.lang.Override
    public void enforceCallingPermission(java.lang.String permission, java.lang.String message) {
        enforce(permission, checkCallingPermission(permission), false, android.os.Binder.getCallingUid(), message);
    }

    @java.lang.Override
    public void enforceCallingOrSelfPermission(java.lang.String permission, java.lang.String message) {
        enforce(permission, checkCallingOrSelfPermission(permission), true, android.os.Binder.getCallingUid(), message);
    }

    @java.lang.Override
    public void grantUriPermission(java.lang.String toPackage, android.net.Uri uri, int modeFlags) {
        try {
            android.app.ActivityManagerNative.getDefault().grantUriPermission(mMainThread.getApplicationThread(), toPackage, android.content.ContentProvider.getUriWithoutUserId(uri), modeFlags, resolveUserId(uri));
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public void revokeUriPermission(android.net.Uri uri, int modeFlags) {
        try {
            android.app.ActivityManagerNative.getDefault().revokeUriPermission(mMainThread.getApplicationThread(), android.content.ContentProvider.getUriWithoutUserId(uri), modeFlags, resolveUserId(uri));
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @java.lang.Override
    public int checkUriPermission(android.net.Uri uri, int pid, int uid, int modeFlags) {
        try {
            return android.app.ActivityManagerNative.getDefault().checkUriPermission(android.content.ContentProvider.getUriWithoutUserId(uri), pid, uid, modeFlags, resolveUserId(uri), null);
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
    public int checkUriPermission(android.net.Uri uri, int pid, int uid, int modeFlags, android.os.IBinder callerToken) {
        try {
            return android.app.ActivityManagerNative.getDefault().checkUriPermission(android.content.ContentProvider.getUriWithoutUserId(uri), pid, uid, modeFlags, resolveUserId(uri), callerToken);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private int resolveUserId(android.net.Uri uri) {
        return android.content.ContentProvider.getUserIdFromUri(uri, getUserId());
    }

    @java.lang.Override
    public int checkCallingUriPermission(android.net.Uri uri, int modeFlags) {
        int pid = android.os.Binder.getCallingPid();
        if (pid != android.os.Process.myPid()) {
            return checkUriPermission(uri, pid, android.os.Binder.getCallingUid(), modeFlags);
        }
        return android.content.pm.PackageManager.PERMISSION_DENIED;
    }

    @java.lang.Override
    public int checkCallingOrSelfUriPermission(android.net.Uri uri, int modeFlags) {
        return checkUriPermission(uri, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), modeFlags);
    }

    @java.lang.Override
    public int checkUriPermission(android.net.Uri uri, java.lang.String readPermission, java.lang.String writePermission, int pid, int uid, int modeFlags) {
        if (android.app.ContextImpl.DEBUG) {
            android.util.Log.i("foo", (((((((((("checkUriPermission: uri=" + uri) + "readPermission=") + readPermission) + " writePermission=") + writePermission) + " pid=") + pid) + " uid=") + uid) + " mode") + modeFlags);
        }
        if ((modeFlags & android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION) != 0) {
            if ((readPermission == null) || (checkPermission(readPermission, pid, uid) == android.content.pm.PackageManager.PERMISSION_GRANTED)) {
                return android.content.pm.PackageManager.PERMISSION_GRANTED;
            }
        }
        if ((modeFlags & android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION) != 0) {
            if ((writePermission == null) || (checkPermission(writePermission, pid, uid) == android.content.pm.PackageManager.PERMISSION_GRANTED)) {
                return android.content.pm.PackageManager.PERMISSION_GRANTED;
            }
        }
        return uri != null ? checkUriPermission(uri, pid, uid, modeFlags) : android.content.pm.PackageManager.PERMISSION_DENIED;
    }

    private java.lang.String uriModeFlagToString(int uriModeFlags) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        if ((uriModeFlags & android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION) != 0) {
            builder.append("read and ");
        }
        if ((uriModeFlags & android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION) != 0) {
            builder.append("write and ");
        }
        if ((uriModeFlags & android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION) != 0) {
            builder.append("persistable and ");
        }
        if ((uriModeFlags & android.content.Intent.FLAG_GRANT_PREFIX_URI_PERMISSION) != 0) {
            builder.append("prefix and ");
        }
        if (builder.length() > 5) {
            builder.setLength(builder.length() - 5);
            return builder.toString();
        } else {
            throw new java.lang.IllegalArgumentException("Unknown permission mode flags: " + uriModeFlags);
        }
    }

    private void enforceForUri(int modeFlags, int resultOfCheck, boolean selfToo, int uid, android.net.Uri uri, java.lang.String message) {
        if (resultOfCheck != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            throw new java.lang.SecurityException((((((message != null ? message + ": " : "") + (selfToo ? ("Neither user " + uid) + " nor current process has " : ("User " + uid) + " does not have ")) + uriModeFlagToString(modeFlags)) + " permission on ") + uri) + ".");
        }
    }

    @java.lang.Override
    public void enforceUriPermission(android.net.Uri uri, int pid, int uid, int modeFlags, java.lang.String message) {
        enforceForUri(modeFlags, checkUriPermission(uri, pid, uid, modeFlags), false, uid, uri, message);
    }

    @java.lang.Override
    public void enforceCallingUriPermission(android.net.Uri uri, int modeFlags, java.lang.String message) {
        enforceForUri(modeFlags, checkCallingUriPermission(uri, modeFlags), false, android.os.Binder.getCallingUid(), uri, message);
    }

    @java.lang.Override
    public void enforceCallingOrSelfUriPermission(android.net.Uri uri, int modeFlags, java.lang.String message) {
        enforceForUri(modeFlags, checkCallingOrSelfUriPermission(uri, modeFlags), true, android.os.Binder.getCallingUid(), uri, message);
    }

    @java.lang.Override
    public void enforceUriPermission(android.net.Uri uri, java.lang.String readPermission, java.lang.String writePermission, int pid, int uid, int modeFlags, java.lang.String message) {
        enforceForUri(modeFlags, checkUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags), false, uid, uri, message);
    }

    /**
     * Logs a warning if the system process directly called a method such as
     * {@link #startService(Intent)} instead of {@link #startServiceAsUser(Intent, UserHandle)}.
     * The "AsUser" variants allow us to properly enforce the user's restrictions.
     */
    private void warnIfCallingFromSystemProcess() {
        if (android.os.Process.myUid() == android.os.Process.SYSTEM_UID) {
            android.util.Slog.w(android.app.ContextImpl.TAG, "Calling a method in the system process without a qualified user: " + android.os.Debug.getCallers(5));
        }
    }

    @java.lang.Override
    public android.content.Context createApplicationContext(android.content.pm.ApplicationInfo application, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        android.app.LoadedApk pi = mMainThread.getPackageInfo(application, mResources.getCompatibilityInfo(), flags | android.content.Context.CONTEXT_REGISTER_PACKAGE);
        if (pi != null) {
            android.app.ContextImpl c = new android.app.ContextImpl(this, mMainThread, pi, mActivityToken, new android.os.UserHandle(android.os.UserHandle.getUserId(application.uid)), flags, mDisplay, null, android.view.Display.INVALID_DISPLAY);
            if (c.mResources != null) {
                return c;
            }
        }
        throw new android.content.pm.PackageManager.NameNotFoundException(("Application package " + application.packageName) + " not found");
    }

    @java.lang.Override
    public android.content.Context createPackageContext(java.lang.String packageName, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        return createPackageContextAsUser(packageName, flags, mUser != null ? mUser : android.os.Process.myUserHandle());
    }

    @java.lang.Override
    public android.content.Context createPackageContextAsUser(java.lang.String packageName, int flags, android.os.UserHandle user) throws android.content.pm.PackageManager.NameNotFoundException {
        if (packageName.equals("system") || packageName.equals("android")) {
            return new android.app.ContextImpl(this, mMainThread, mPackageInfo, mActivityToken, user, flags, mDisplay, null, android.view.Display.INVALID_DISPLAY);
        }
        android.app.LoadedApk pi = mMainThread.getPackageInfo(packageName, mResources.getCompatibilityInfo(), flags | android.content.Context.CONTEXT_REGISTER_PACKAGE, user.getIdentifier());
        if (pi != null) {
            android.app.ContextImpl c = new android.app.ContextImpl(this, mMainThread, pi, mActivityToken, user, flags, mDisplay, null, android.view.Display.INVALID_DISPLAY);
            if (c.mResources != null) {
                return c;
            }
        }
        // Should be a better exception.
        throw new android.content.pm.PackageManager.NameNotFoundException(("Application package " + packageName) + " not found");
    }

    @java.lang.Override
    public android.content.Context createConfigurationContext(android.content.res.Configuration overrideConfiguration) {
        if (overrideConfiguration == null) {
            throw new java.lang.IllegalArgumentException("overrideConfiguration must not be null");
        }
        return new android.app.ContextImpl(this, mMainThread, mPackageInfo, mActivityToken, mUser, mFlags, mDisplay, overrideConfiguration, android.view.Display.INVALID_DISPLAY);
    }

    @java.lang.Override
    public android.content.Context createDisplayContext(android.view.Display display) {
        if (display == null) {
            throw new java.lang.IllegalArgumentException("display must not be null");
        }
        return new android.app.ContextImpl(this, mMainThread, mPackageInfo, mActivityToken, mUser, mFlags, display, null, android.view.Display.INVALID_DISPLAY);
    }

    @java.lang.Override
    public android.content.Context createDeviceProtectedStorageContext() {
        final int flags = (mFlags & (~android.content.Context.CONTEXT_CREDENTIAL_PROTECTED_STORAGE)) | android.content.Context.CONTEXT_DEVICE_PROTECTED_STORAGE;
        return new android.app.ContextImpl(this, mMainThread, mPackageInfo, mActivityToken, mUser, flags, mDisplay, null, android.view.Display.INVALID_DISPLAY);
    }

    @java.lang.Override
    public android.content.Context createCredentialProtectedStorageContext() {
        final int flags = (mFlags & (~android.content.Context.CONTEXT_DEVICE_PROTECTED_STORAGE)) | android.content.Context.CONTEXT_CREDENTIAL_PROTECTED_STORAGE;
        return new android.app.ContextImpl(this, mMainThread, mPackageInfo, mActivityToken, mUser, flags, mDisplay, null, android.view.Display.INVALID_DISPLAY);
    }

    @java.lang.Override
    public boolean isRestricted() {
        return (mFlags & android.content.Context.CONTEXT_RESTRICTED) != 0;
    }

    @java.lang.Override
    public boolean isDeviceProtectedStorage() {
        return (mFlags & android.content.Context.CONTEXT_DEVICE_PROTECTED_STORAGE) != 0;
    }

    @java.lang.Override
    public boolean isCredentialProtectedStorage() {
        return (mFlags & android.content.Context.CONTEXT_CREDENTIAL_PROTECTED_STORAGE) != 0;
    }

    @java.lang.Override
    public android.view.Display getDisplay() {
        final android.view.DisplayAdjustments displayAdjustments = mResources.getDisplayAdjustments();
        if (mDisplay == null) {
            return mResourcesManager.getAdjustedDisplay(android.view.Display.DEFAULT_DISPLAY, displayAdjustments);
        }
        if (!mDisplay.getDisplayAdjustments().equals(displayAdjustments)) {
            mDisplay = mResourcesManager.getAdjustedDisplay(mDisplay.getDisplayId(), displayAdjustments);
        }
        return mDisplay;
    }

    @java.lang.Override
    public android.view.DisplayAdjustments getDisplayAdjustments(int displayId) {
        return mResources.getDisplayAdjustments();
    }

    @java.lang.Override
    public java.io.File getDataDir() {
        if (mPackageInfo != null) {
            java.io.File res = null;
            if (isCredentialProtectedStorage()) {
                res = mPackageInfo.getCredentialProtectedDataDirFile();
            } else
                if (isDeviceProtectedStorage()) {
                    res = mPackageInfo.getDeviceProtectedDataDirFile();
                } else {
                    res = mPackageInfo.getDataDirFile();
                }

            if (res != null) {
                if ((!res.exists()) && (android.os.Process.myUid() == android.os.Process.SYSTEM_UID)) {
                    android.util.Log.wtf(android.app.ContextImpl.TAG, "Data directory doesn't exist for package " + getPackageName(), new java.lang.Throwable());
                }
                return res;
            } else {
                throw new java.lang.RuntimeException("No data directory found for package " + getPackageName());
            }
        } else {
            throw new java.lang.RuntimeException("No package details found for package " + getPackageName());
        }
    }

    @java.lang.Override
    public java.io.File getDir(java.lang.String name, int mode) {
        checkMode(mode);
        name = "app_" + name;
        java.io.File file = makeFilename(getDataDir(), name);
        if (!file.exists()) {
            file.mkdir();
            android.app.ContextImpl.setFilePermissionsFromMode(file.getPath(), mode, (android.os.FileUtils.S_IRWXU | android.os.FileUtils.S_IRWXG) | android.os.FileUtils.S_IXOTH);
        }
        return file;
    }

    /**
     * {@hide }
     */
    @java.lang.Override
    public int getUserId() {
        return mUser.getIdentifier();
    }

    static android.app.ContextImpl createSystemContext(android.app.ActivityThread mainThread) {
        android.app.LoadedApk packageInfo = new android.app.LoadedApk(mainThread);
        android.app.ContextImpl context = new android.app.ContextImpl(null, mainThread, packageInfo, null, null, 0, null, null, android.view.Display.INVALID_DISPLAY);
        context.mResources.updateConfiguration(context.mResourcesManager.getConfiguration(), context.mResourcesManager.getDisplayMetrics());
        return context;
    }

    static android.app.ContextImpl createAppContext(android.app.ActivityThread mainThread, android.app.LoadedApk packageInfo) {
        if (packageInfo == null)
            throw new java.lang.IllegalArgumentException("packageInfo");

        return new android.app.ContextImpl(null, mainThread, packageInfo, null, null, 0, null, null, android.view.Display.INVALID_DISPLAY);
    }

    static android.app.ContextImpl createActivityContext(android.app.ActivityThread mainThread, android.app.LoadedApk packageInfo, android.os.IBinder activityToken, int displayId, android.content.res.Configuration overrideConfiguration) {
        if (packageInfo == null)
            throw new java.lang.IllegalArgumentException("packageInfo");

        return new android.app.ContextImpl(null, mainThread, packageInfo, activityToken, null, 0, null, overrideConfiguration, displayId);
    }

    private ContextImpl(android.app.ContextImpl container, android.app.ActivityThread mainThread, android.app.LoadedApk packageInfo, android.os.IBinder activityToken, android.os.UserHandle user, int flags, android.view.Display display, android.content.res.Configuration overrideConfiguration, int createDisplayWithId) {
        mOuterContext = this;
        // If creator didn't specify which storage to use, use the default
        // location for application.
        if ((flags & (android.content.Context.CONTEXT_CREDENTIAL_PROTECTED_STORAGE | android.content.Context.CONTEXT_DEVICE_PROTECTED_STORAGE)) == 0) {
            final java.io.File dataDir = packageInfo.getDataDirFile();
            if (java.util.Objects.equals(dataDir, packageInfo.getCredentialProtectedDataDirFile())) {
                flags |= android.content.Context.CONTEXT_CREDENTIAL_PROTECTED_STORAGE;
            } else
                if (java.util.Objects.equals(dataDir, packageInfo.getDeviceProtectedDataDirFile())) {
                    flags |= android.content.Context.CONTEXT_DEVICE_PROTECTED_STORAGE;
                }

        }
        mMainThread = mainThread;
        mActivityToken = activityToken;
        mFlags = flags;
        if (user == null) {
            user = android.os.Process.myUserHandle();
        }
        mUser = user;
        mPackageInfo = packageInfo;
        mResourcesManager = android.app.ResourcesManager.getInstance();
        final int displayId = (createDisplayWithId != android.view.Display.INVALID_DISPLAY) ? createDisplayWithId : display != null ? display.getDisplayId() : android.view.Display.DEFAULT_DISPLAY;
        android.content.res.CompatibilityInfo compatInfo = null;
        if (container != null) {
            compatInfo = container.getDisplayAdjustments(displayId).getCompatibilityInfo();
        }
        if (compatInfo == null) {
            compatInfo = (displayId == android.view.Display.DEFAULT_DISPLAY) ? packageInfo.getCompatibilityInfo() : android.content.res.CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
        }
        android.content.res.Resources resources = packageInfo.getResources(mainThread);
        if (resources != null) {
            if (((displayId != android.view.Display.DEFAULT_DISPLAY) || (overrideConfiguration != null)) || ((compatInfo != null) && (compatInfo.applicationScale != resources.getCompatibilityInfo().applicationScale))) {
                if (container != null) {
                    // This is a nested Context, so it can't be a base Activity context.
                    // Just create a regular Resources object associated with the Activity.
                    resources = mResourcesManager.getResources(activityToken, packageInfo.getResDir(), packageInfo.getSplitResDirs(), packageInfo.getOverlayDirs(), packageInfo.getApplicationInfo().sharedLibraryFiles, displayId, overrideConfiguration, compatInfo, packageInfo.getClassLoader());
                } else {
                    // This is not a nested Context, so it must be the root Activity context.
                    // All other nested Contexts will inherit the configuration set here.
                    resources = mResourcesManager.createBaseActivityResources(activityToken, packageInfo.getResDir(), packageInfo.getSplitResDirs(), packageInfo.getOverlayDirs(), packageInfo.getApplicationInfo().sharedLibraryFiles, displayId, overrideConfiguration, compatInfo, packageInfo.getClassLoader());
                }
            }
        }
        mResources = resources;
        mDisplay = (createDisplayWithId == android.view.Display.INVALID_DISPLAY) ? display : mResourcesManager.getAdjustedDisplay(displayId, mResources.getDisplayAdjustments());
        if (container != null) {
            mBasePackageName = container.mBasePackageName;
            mOpPackageName = container.mOpPackageName;
        } else {
            mBasePackageName = packageInfo.mPackageName;
            android.content.pm.ApplicationInfo ainfo = packageInfo.getApplicationInfo();
            if ((ainfo.uid == android.os.Process.SYSTEM_UID) && (ainfo.uid != android.os.Process.myUid())) {
                // Special case: system components allow themselves to be loaded in to other
                // processes.  For purposes of app ops, we must then consider the context as
                // belonging to the package of this process, not the system itself, otherwise
                // the package+uid verifications in app ops will fail.
                mOpPackageName = android.app.ActivityThread.currentPackageName();
            } else {
                mOpPackageName = mBasePackageName;
            }
        }
        mContentResolver = new android.app.ContextImpl.ApplicationContentResolver(this, mainThread, user);
    }

    void installSystemApplicationInfo(android.content.pm.ApplicationInfo info, java.lang.ClassLoader classLoader) {
        mPackageInfo.installSystemApplicationInfo(info, classLoader);
    }

    final void scheduleFinalCleanup(java.lang.String who, java.lang.String what) {
        mMainThread.scheduleContextCleanup(this, who, what);
    }

    final void performFinalCleanup(java.lang.String who, java.lang.String what) {
        // Log.i(TAG, "Cleanup up context: " + this);
        mPackageInfo.removeContextRegistrations(getOuterContext(), who, what);
    }

    final android.content.Context getReceiverRestrictedContext() {
        if (mReceiverRestrictedContext != null) {
            return mReceiverRestrictedContext;
        }
        return mReceiverRestrictedContext = new android.app.ReceiverRestrictedContext(getOuterContext());
    }

    final void setOuterContext(android.content.Context context) {
        mOuterContext = context;
    }

    final android.content.Context getOuterContext() {
        return mOuterContext;
    }

    final android.os.IBinder getActivityToken() {
        return mActivityToken;
    }

    private void checkMode(int mode) {
        if (getApplicationInfo().targetSdkVersion >= android.os.Build.VERSION_CODES.N) {
            if ((mode & android.content.Context.MODE_WORLD_READABLE) != 0) {
                throw new java.lang.SecurityException("MODE_WORLD_READABLE no longer supported");
            }
            if ((mode & android.content.Context.MODE_WORLD_WRITEABLE) != 0) {
                throw new java.lang.SecurityException("MODE_WORLD_WRITEABLE no longer supported");
            }
        }
    }

    @java.lang.SuppressWarnings("deprecation")
    static void setFilePermissionsFromMode(java.lang.String name, int mode, int extraPermissions) {
        int perms = (((android.os.FileUtils.S_IRUSR | android.os.FileUtils.S_IWUSR) | android.os.FileUtils.S_IRGRP) | android.os.FileUtils.S_IWGRP) | extraPermissions;
        if ((mode & android.content.Context.MODE_WORLD_READABLE) != 0) {
            perms |= android.os.FileUtils.S_IROTH;
        }
        if ((mode & android.content.Context.MODE_WORLD_WRITEABLE) != 0) {
            perms |= android.os.FileUtils.S_IWOTH;
        }
        if (android.app.ContextImpl.DEBUG) {
            android.util.Log.i(android.app.ContextImpl.TAG, (((("File " + name) + ": mode=0x") + java.lang.Integer.toHexString(mode)) + ", perms=0x") + java.lang.Integer.toHexString(perms));
        }
        android.os.FileUtils.setPermissions(name, perms, -1, -1);
    }

    private java.io.File makeFilename(java.io.File base, java.lang.String name) {
        if (name.indexOf(java.io.File.separatorChar) < 0) {
            return new java.io.File(base, name);
        }
        throw new java.lang.IllegalArgumentException(("File " + name) + " contains a path separator");
    }

    /**
     * Ensure that given directories exist, trying to create them if missing. If
     * unable to create, they are filtered by replacing with {@code null}.
     */
    private java.io.File[] ensureExternalDirsExistOrFilter(java.io.File[] dirs) {
        java.io.File[] result = new java.io.File[dirs.length];
        for (int i = 0; i < dirs.length; i++) {
            java.io.File dir = dirs[i];
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    // recheck existence in case of cross-process race
                    if (!dir.exists()) {
                        // Failing to mkdir() may be okay, since we might not have
                        // enough permissions; ask vold to create on our behalf.
                        final android.os.storage.IMountService mount = android.os.storage.IMountService.Stub.asInterface(android.os.ServiceManager.getService("mount"));
                        try {
                            final int res = mount.mkdirs(getPackageName(), dir.getAbsolutePath());
                            if (res != 0) {
                                android.util.Log.w(android.app.ContextImpl.TAG, (("Failed to ensure " + dir) + ": ") + res);
                                dir = null;
                            }
                        } catch (java.lang.Exception e) {
                            android.util.Log.w(android.app.ContextImpl.TAG, (("Failed to ensure " + dir) + ": ") + e);
                            dir = null;
                        }
                    }
                }
            }
            result[i] = dir;
        }
        return result;
    }

    // ----------------------------------------------------------------------
    // ----------------------------------------------------------------------
    // ----------------------------------------------------------------------
    private static final class ApplicationContentResolver extends android.content.ContentResolver {
        private final android.app.ActivityThread mMainThread;

        private final android.os.UserHandle mUser;

        public ApplicationContentResolver(android.content.Context context, android.app.ActivityThread mainThread, android.os.UserHandle user) {
            super(context);
            mMainThread = com.android.internal.util.Preconditions.checkNotNull(mainThread);
            mUser = com.android.internal.util.Preconditions.checkNotNull(user);
        }

        @java.lang.Override
        protected android.content.IContentProvider acquireProvider(android.content.Context context, java.lang.String auth) {
            return mMainThread.acquireProvider(context, android.content.ContentProvider.getAuthorityWithoutUserId(auth), resolveUserIdFromAuthority(auth), true);
        }

        @java.lang.Override
        protected android.content.IContentProvider acquireExistingProvider(android.content.Context context, java.lang.String auth) {
            return mMainThread.acquireExistingProvider(context, android.content.ContentProvider.getAuthorityWithoutUserId(auth), resolveUserIdFromAuthority(auth), true);
        }

        @java.lang.Override
        public boolean releaseProvider(android.content.IContentProvider provider) {
            return mMainThread.releaseProvider(provider, true);
        }

        @java.lang.Override
        protected android.content.IContentProvider acquireUnstableProvider(android.content.Context c, java.lang.String auth) {
            return mMainThread.acquireProvider(c, android.content.ContentProvider.getAuthorityWithoutUserId(auth), resolveUserIdFromAuthority(auth), false);
        }

        @java.lang.Override
        public boolean releaseUnstableProvider(android.content.IContentProvider icp) {
            return mMainThread.releaseProvider(icp, false);
        }

        @java.lang.Override
        public void unstableProviderDied(android.content.IContentProvider icp) {
            mMainThread.handleUnstableProviderDied(icp.asBinder(), true);
        }

        @java.lang.Override
        public void appNotRespondingViaProvider(android.content.IContentProvider icp) {
            mMainThread.appNotRespondingViaProvider(icp.asBinder());
        }

        /**
         *
         *
         * @unknown 
         */
        protected int resolveUserIdFromAuthority(java.lang.String auth) {
            return android.content.ContentProvider.getUserIdFromAuthority(auth, mUser.getIdentifier());
        }
    }
}

