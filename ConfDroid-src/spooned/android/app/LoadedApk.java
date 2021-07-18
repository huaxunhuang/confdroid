package android.app;


/**
 * Local state maintained about a currently loaded .apk.
 *
 * @unknown 
 */
public final class LoadedApk {
    private static final java.lang.String TAG = "LoadedApk";

    private final android.app.ActivityThread mActivityThread;

    final java.lang.String mPackageName;

    private android.content.pm.ApplicationInfo mApplicationInfo;

    private java.lang.String mAppDir;

    private java.lang.String mResDir;

    private java.lang.String[] mSplitAppDirs;

    private java.lang.String[] mSplitResDirs;

    private java.lang.String[] mOverlayDirs;

    private java.lang.String[] mSharedLibraries;

    private java.lang.String mDataDir;

    private java.lang.String mLibDir;

    private java.io.File mDataDirFile;

    private java.io.File mDeviceProtectedDataDirFile;

    private java.io.File mCredentialProtectedDataDirFile;

    private final java.lang.ClassLoader mBaseClassLoader;

    private final boolean mSecurityViolation;

    private final boolean mIncludeCode;

    private final boolean mRegisterPackage;

    private final android.view.DisplayAdjustments mDisplayAdjustments = new android.view.DisplayAdjustments();

    /**
     * WARNING: This may change. Don't hold external references to it.
     */
    android.content.res.Resources mResources;

    private java.lang.ClassLoader mClassLoader;

    private android.app.Application mApplication;

    private final android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk.ReceiverDispatcher>> mReceivers = new android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk.ReceiverDispatcher>>();

    private final android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk.ReceiverDispatcher>> mUnregisteredReceivers = new android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk.ReceiverDispatcher>>();

    private final android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk.ServiceDispatcher>> mServices = new android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk.ServiceDispatcher>>();

    private final android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk.ServiceDispatcher>> mUnboundServices = new android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk.ServiceDispatcher>>();

    int mClientCount = 0;

    android.app.Application getApplication() {
        return mApplication;
    }

    /**
     * Create information about a new .apk
     *
     * NOTE: This constructor is called with ActivityThread's lock held,
     * so MUST NOT call back out to the activity manager.
     */
    public LoadedApk(android.app.ActivityThread activityThread, android.content.pm.ApplicationInfo aInfo, android.content.res.CompatibilityInfo compatInfo, java.lang.ClassLoader baseLoader, boolean securityViolation, boolean includeCode, boolean registerPackage) {
        mActivityThread = activityThread;
        setApplicationInfo(aInfo);
        mPackageName = aInfo.packageName;
        mBaseClassLoader = baseLoader;
        mSecurityViolation = securityViolation;
        mIncludeCode = includeCode;
        mRegisterPackage = registerPackage;
        mDisplayAdjustments.setCompatibilityInfo(compatInfo);
    }

    private static android.content.pm.ApplicationInfo adjustNativeLibraryPaths(android.content.pm.ApplicationInfo info) {
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
                final android.content.pm.ApplicationInfo modified = new android.content.pm.ApplicationInfo(info);
                modified.nativeLibraryDir = modified.secondaryNativeLibraryDir;
                modified.primaryCpuAbi = modified.secondaryCpuAbi;
                return modified;
            }
        }
        return info;
    }

    /**
     * Create information about the system package.
     * Must call {@link #installSystemApplicationInfo} later.
     */
    LoadedApk(android.app.ActivityThread activityThread) {
        mActivityThread = activityThread;
        mApplicationInfo = new android.content.pm.ApplicationInfo();
        mApplicationInfo.packageName = "android";
        mPackageName = "android";
        mAppDir = null;
        mResDir = null;
        mSplitAppDirs = null;
        mSplitResDirs = null;
        mOverlayDirs = null;
        mSharedLibraries = null;
        mDataDir = null;
        mDataDirFile = null;
        mDeviceProtectedDataDirFile = null;
        mCredentialProtectedDataDirFile = null;
        mLibDir = null;
        mBaseClassLoader = null;
        mSecurityViolation = false;
        mIncludeCode = true;
        mRegisterPackage = false;
        mClassLoader = java.lang.ClassLoader.getSystemClassLoader();
        mResources = android.content.res.Resources.getSystem();
    }

    /**
     * Sets application info about the system package.
     */
    void installSystemApplicationInfo(android.content.pm.ApplicationInfo info, java.lang.ClassLoader classLoader) {
        assert info.packageName.equals("android");
        mApplicationInfo = info;
        mClassLoader = classLoader;
    }

    public java.lang.String getPackageName() {
        return mPackageName;
    }

    public android.content.pm.ApplicationInfo getApplicationInfo() {
        return mApplicationInfo;
    }

    public int getTargetSdkVersion() {
        return mApplicationInfo.targetSdkVersion;
    }

    public boolean isSecurityViolation() {
        return mSecurityViolation;
    }

    public android.content.res.CompatibilityInfo getCompatibilityInfo() {
        return mDisplayAdjustments.getCompatibilityInfo();
    }

    public void setCompatibilityInfo(android.content.res.CompatibilityInfo compatInfo) {
        mDisplayAdjustments.setCompatibilityInfo(compatInfo);
    }

    /**
     * Gets the array of shared libraries that are listed as
     * used by the given package.
     *
     * @param packageName
     * 		the name of the package (note: not its
     * 		file name)
     * @return null-ok; the array of shared libraries, each one
    a fully-qualified path
     */
    private static java.lang.String[] getLibrariesFor(java.lang.String packageName) {
        android.content.pm.ApplicationInfo ai = null;
        try {
            ai = android.app.ActivityThread.getPackageManager().getApplicationInfo(packageName, android.content.pm.PackageManager.GET_SHARED_LIBRARY_FILES, android.os.UserHandle.myUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        if (ai == null) {
            return null;
        }
        return ai.sharedLibraryFiles;
    }

    public void updateApplicationInfo(android.content.pm.ApplicationInfo aInfo, java.util.List<java.lang.String> oldPaths) {
        setApplicationInfo(aInfo);
        final java.util.List<java.lang.String> newPaths = new java.util.ArrayList<>();
        /* libPaths */
        android.app.LoadedApk.makePaths(mActivityThread, aInfo, newPaths, null);
        final java.util.List<java.lang.String> addedPaths = new java.util.ArrayList<>(newPaths.size());
        if (oldPaths != null) {
            for (java.lang.String path : newPaths) {
                final java.lang.String apkName = path.substring(path.lastIndexOf(java.io.File.separator));
                boolean match = false;
                for (java.lang.String oldPath : oldPaths) {
                    final java.lang.String oldApkName = oldPath.substring(path.lastIndexOf(java.io.File.separator));
                    if (apkName.equals(oldApkName)) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    addedPaths.add(path);
                }
            }
        } else {
            addedPaths.addAll(newPaths);
        }
        synchronized(this) {
            createOrUpdateClassLoaderLocked(addedPaths);
            if (mResources != null) {
                mResources = mActivityThread.getTopLevelResources(mResDir, mSplitResDirs, mOverlayDirs, mApplicationInfo.sharedLibraryFiles, android.view.Display.DEFAULT_DISPLAY, this);
            }
        }
    }

    private void setApplicationInfo(android.content.pm.ApplicationInfo aInfo) {
        final int myUid = android.os.Process.myUid();
        aInfo = android.app.LoadedApk.adjustNativeLibraryPaths(aInfo);
        mApplicationInfo = aInfo;
        mAppDir = aInfo.sourceDir;
        mResDir = (aInfo.uid == myUid) ? aInfo.sourceDir : aInfo.publicSourceDir;
        mSplitAppDirs = aInfo.splitSourceDirs;
        mSplitResDirs = (aInfo.uid == myUid) ? aInfo.splitSourceDirs : aInfo.splitPublicSourceDirs;
        mOverlayDirs = aInfo.resourceDirs;
        mSharedLibraries = aInfo.sharedLibraryFiles;
        mDataDir = aInfo.dataDir;
        mLibDir = aInfo.nativeLibraryDir;
        mDataDirFile = android.os.FileUtils.newFileOrNull(aInfo.dataDir);
        mDeviceProtectedDataDirFile = android.os.FileUtils.newFileOrNull(aInfo.deviceProtectedDataDir);
        mCredentialProtectedDataDirFile = android.os.FileUtils.newFileOrNull(aInfo.credentialProtectedDataDir);
    }

    public static void makePaths(android.app.ActivityThread activityThread, android.content.pm.ApplicationInfo aInfo, java.util.List<java.lang.String> outZipPaths, java.util.List<java.lang.String> outLibPaths) {
        final java.lang.String appDir = aInfo.sourceDir;
        final java.lang.String[] splitAppDirs = aInfo.splitSourceDirs;
        final java.lang.String libDir = aInfo.nativeLibraryDir;
        final java.lang.String[] sharedLibraries = aInfo.sharedLibraryFiles;
        outZipPaths.clear();
        outZipPaths.add(appDir);
        if (splitAppDirs != null) {
            java.util.Collections.addAll(outZipPaths, splitAppDirs);
        }
        if (outLibPaths != null) {
            outLibPaths.clear();
        }
        /* The following is a bit of a hack to inject
        instrumentation into the system: If the app
        being started matches one of the instrumentation names,
        then we combine both the "instrumentation" and
        "instrumented" app into the path, along with the
        concatenation of both apps' shared library lists.
         */
        java.lang.String instrumentationPackageName = activityThread.mInstrumentationPackageName;
        java.lang.String instrumentationAppDir = activityThread.mInstrumentationAppDir;
        java.lang.String[] instrumentationSplitAppDirs = activityThread.mInstrumentationSplitAppDirs;
        java.lang.String instrumentationLibDir = activityThread.mInstrumentationLibDir;
        java.lang.String instrumentedAppDir = activityThread.mInstrumentedAppDir;
        java.lang.String[] instrumentedSplitAppDirs = activityThread.mInstrumentedSplitAppDirs;
        java.lang.String instrumentedLibDir = activityThread.mInstrumentedLibDir;
        java.lang.String[] instrumentationLibs = null;
        if (appDir.equals(instrumentationAppDir) || appDir.equals(instrumentedAppDir)) {
            outZipPaths.clear();
            outZipPaths.add(instrumentationAppDir);
            if (instrumentationSplitAppDirs != null) {
                java.util.Collections.addAll(outZipPaths, instrumentationSplitAppDirs);
            }
            if (!instrumentationAppDir.equals(instrumentedAppDir)) {
                outZipPaths.add(instrumentedAppDir);
                if (instrumentedSplitAppDirs != null) {
                    java.util.Collections.addAll(outZipPaths, instrumentedSplitAppDirs);
                }
            }
            if (outLibPaths != null) {
                outLibPaths.add(instrumentationLibDir);
                if (!instrumentationLibDir.equals(instrumentedLibDir)) {
                    outLibPaths.add(instrumentedLibDir);
                }
            }
            if (!instrumentedAppDir.equals(instrumentationAppDir)) {
                instrumentationLibs = android.app.LoadedApk.getLibrariesFor(instrumentationPackageName);
            }
        }
        if (outLibPaths != null) {
            if (outLibPaths.isEmpty()) {
                outLibPaths.add(libDir);
            }
            // Add path to libraries in apk for current abi. Do this now because more entries
            // will be added to zipPaths that shouldn't be part of the library path.
            if (aInfo.primaryCpuAbi != null) {
                // Add fake libs into the library search path if we target prior to N.
                if (aInfo.targetSdkVersion <= 23) {
                    outLibPaths.add("/system/fake-libs" + (dalvik.system.VMRuntime.is64BitAbi(aInfo.primaryCpuAbi) ? "64" : ""));
                }
                for (java.lang.String apk : outZipPaths) {
                    outLibPaths.add((apk + "!/lib/") + aInfo.primaryCpuAbi);
                }
            }
            if (aInfo.isSystemApp() && (!aInfo.isUpdatedSystemApp())) {
                // Add path to system libraries to libPaths;
                // Access to system libs should be limited
                // to bundled applications; this is why updated
                // system apps are not included.
                outLibPaths.add(java.lang.System.getProperty("java.library.path"));
            }
        }
        if (sharedLibraries != null) {
            for (java.lang.String lib : sharedLibraries) {
                if (!outZipPaths.contains(lib)) {
                    outZipPaths.add(0, lib);
                }
            }
        }
        if (instrumentationLibs != null) {
            for (java.lang.String lib : instrumentationLibs) {
                if (!outZipPaths.contains(lib)) {
                    outZipPaths.add(0, lib);
                }
            }
        }
    }

    private void createOrUpdateClassLoaderLocked(java.util.List<java.lang.String> addedPaths) {
        if (mPackageName.equals("android")) {
            // Note: This branch is taken for system server and we don't need to setup
            // jit profiling support.
            if (mClassLoader != null) {
                // nothing to update
                return;
            }
            if (mBaseClassLoader != null) {
                mClassLoader = mBaseClassLoader;
            } else {
                mClassLoader = java.lang.ClassLoader.getSystemClassLoader();
            }
            return;
        }
        // Avoid the binder call when the package is the current application package.
        // The activity manager will perform ensure that dexopt is performed before
        // spinning up the process.
        if (!java.util.Objects.equals(mPackageName, android.app.ActivityThread.currentPackageName())) {
            dalvik.system.VMRuntime.getRuntime().vmInstructionSet();
            try {
                android.app.ActivityThread.getPackageManager().notifyPackageUse(mPackageName, android.content.pm.PackageManager.NOTIFY_PACKAGE_USE_CROSS_PACKAGE);
            } catch (android.os.RemoteException re) {
                throw re.rethrowFromSystemServer();
            }
        }
        if (mRegisterPackage) {
            try {
                android.app.ActivityManagerNative.getDefault().addPackageDependency(mPackageName);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        // Lists for the elements of zip/code and native libraries.
        // 
        // Both lists are usually not empty. We expect on average one APK for the zip component,
        // but shared libraries and splits are not uncommon. We expect at least three elements
        // for native libraries (app-based, system, vendor). As such, give both some breathing
        // space and initialize to a small value (instead of incurring growth code).
        final java.util.List<java.lang.String> zipPaths = new java.util.ArrayList<>(10);
        final java.util.List<java.lang.String> libPaths = new java.util.ArrayList<>(10);
        android.app.LoadedApk.makePaths(mActivityThread, mApplicationInfo, zipPaths, libPaths);
        final boolean isBundledApp = mApplicationInfo.isSystemApp() && (!mApplicationInfo.isUpdatedSystemApp());
        java.lang.String libraryPermittedPath = mDataDir;
        if (isBundledApp) {
            // This is necessary to grant bundled apps access to
            // libraries located in subdirectories of /system/lib
            libraryPermittedPath += java.io.File.pathSeparator + java.lang.System.getProperty("java.library.path");
        }
        final java.lang.String librarySearchPath = android.text.TextUtils.join(java.io.File.pathSeparator, libPaths);
        // If we're not asked to include code, we construct a classloader that has
        // no code path included. We still need to set up the library search paths
        // and permitted path because NativeActivity relies on it (it attempts to
        // call System.loadLibrary() on a classloader from a LoadedApk with
        // mIncludeCode == false).
        if (!mIncludeCode) {
            if (mClassLoader == null) {
                android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskReads();
                mClassLoader = /* codePath */
                android.app.ApplicationLoaders.getDefault().getClassLoader("", mApplicationInfo.targetSdkVersion, isBundledApp, librarySearchPath, libraryPermittedPath, mBaseClassLoader);
                android.os.StrictMode.setThreadPolicy(oldPolicy);
            }
            return;
        }
        /* With all the combination done (if necessary, actually create the java class
        loader and set up JIT profiling support if necessary.

        In many cases this is a single APK, so try to avoid the StringBuilder in TextUtils.
         */
        final java.lang.String zip = (zipPaths.size() == 1) ? zipPaths.get(0) : android.text.TextUtils.join(java.io.File.pathSeparator, zipPaths);
        if (android.app.ActivityThread.localLOGV)
            android.util.Slog.v(android.app.ActivityThread.TAG, (("Class path: " + zip) + ", JNI path: ") + librarySearchPath);

        boolean needToSetupJitProfiles = false;
        if (mClassLoader == null) {
            // Temporarily disable logging of disk reads on the Looper thread
            // as this is early and necessary.
            android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskReads();
            mClassLoader = android.app.ApplicationLoaders.getDefault().getClassLoader(zip, mApplicationInfo.targetSdkVersion, isBundledApp, librarySearchPath, libraryPermittedPath, mBaseClassLoader);
            android.os.StrictMode.setThreadPolicy(oldPolicy);
            // Setup the class loader paths for profiling.
            needToSetupJitProfiles = true;
        }
        if ((addedPaths != null) && (addedPaths.size() > 0)) {
            final java.lang.String add = android.text.TextUtils.join(java.io.File.pathSeparator, addedPaths);
            android.app.ApplicationLoaders.getDefault().addPath(mClassLoader, add);
            // Setup the new code paths for profiling.
            needToSetupJitProfiles = true;
        }
        // Setup jit profile support.
        // 
        // It is ok to call this multiple times if the application gets updated with new splits.
        // The runtime only keeps track of unique code paths and can handle re-registration of
        // the same code path. There's no need to pass `addedPaths` since any new code paths
        // are already in `mApplicationInfo`.
        // 
        // It is NOT ok to call this function from the system_server (for any of the packages it
        // loads code from) so we explicitly disallow it there.
        if (needToSetupJitProfiles && (!android.app.ActivityThread.isSystem())) {
            setupJitProfileSupport();
        }
    }

    public java.lang.ClassLoader getClassLoader() {
        synchronized(this) {
            if (mClassLoader == null) {
                /* addedPaths */
                createOrUpdateClassLoaderLocked(null);
            }
            return mClassLoader;
        }
    }

    // Keep in sync with installd (frameworks/native/cmds/installd/commands.cpp).
    private static java.io.File getPrimaryProfileFile(java.lang.String packageName) {
        java.io.File profileDir = android.os.Environment.getDataProfilesDePackageDirectory(android.os.UserHandle.myUserId(), packageName);
        return new java.io.File(profileDir, "primary.prof");
    }

    private void setupJitProfileSupport() {
        if (!android.os.SystemProperties.getBoolean("dalvik.vm.usejitprofiles", false)) {
            return;
        }
        // Only set up profile support if the loaded apk has the same uid as the
        // current process.
        // Currently, we do not support profiling across different apps.
        // (e.g. application's uid might be different when the code is
        // loaded by another app via createApplicationContext)
        if (mApplicationInfo.uid != android.os.Process.myUid()) {
            return;
        }
        final java.util.List<java.lang.String> codePaths = new java.util.ArrayList<>();
        if ((mApplicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_HAS_CODE) != 0) {
            codePaths.add(mApplicationInfo.sourceDir);
        }
        if (mApplicationInfo.splitSourceDirs != null) {
            java.util.Collections.addAll(codePaths, mApplicationInfo.splitSourceDirs);
        }
        if (codePaths.isEmpty()) {
            // If there are no code paths there's no need to setup a profile file and register with
            // the runtime,
            return;
        }
        final java.io.File profileFile = android.app.LoadedApk.getPrimaryProfileFile(mPackageName);
        final java.io.File foreignDexProfilesFile = android.os.Environment.getDataProfilesDeForeignDexDirectory(android.os.UserHandle.myUserId());
        dalvik.system.VMRuntime.registerAppInfo(profileFile.getPath(), mApplicationInfo.dataDir, codePaths.toArray(new java.lang.String[codePaths.size()]), foreignDexProfilesFile.getPath());
    }

    /**
     * Setup value for Thread.getContextClassLoader(). If the
     * package will not run in in a VM with other packages, we set
     * the Java context ClassLoader to the
     * PackageInfo.getClassLoader value. However, if this VM can
     * contain multiple packages, we intead set the Java context
     * ClassLoader to a proxy that will warn about the use of Java
     * context ClassLoaders and then fall through to use the
     * system ClassLoader.
     *
     * <p> Note that this is similar to but not the same as the
     * android.content.Context.getClassLoader(). While both
     * context class loaders are typically set to the
     * PathClassLoader used to load the package archive in the
     * single application per VM case, a single Android process
     * may contain several Contexts executing on one thread with
     * their own logical ClassLoaders while the Java context
     * ClassLoader is a thread local. This is why in the case when
     * we have multiple packages per VM we do not set the Java
     * context ClassLoader to an arbitrary but instead warn the
     * user to set their own if we detect that they are using a
     * Java library that expects it to be set.
     */
    private void initializeJavaContextClassLoader() {
        android.content.pm.IPackageManager pm = android.app.ActivityThread.getPackageManager();
        android.content.pm.PackageInfo pi;
        try {
            pi = pm.getPackageInfo(mPackageName, android.content.pm.PackageManager.MATCH_DEBUG_TRIAGED_MISSING, android.os.UserHandle.myUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        if (pi == null) {
            throw new java.lang.IllegalStateException(("Unable to get package info for " + mPackageName) + "; is package not installed?");
        }
        /* Two possible indications that this package could be
        sharing its virtual machine with other packages:

        1.) the sharedUserId attribute is set in the manifest,
            indicating a request to share a VM with other
            packages with the same sharedUserId.

        2.) the application element of the manifest has an
            attribute specifying a non-default process name,
            indicating the desire to run in another packages VM.
         */
        boolean sharedUserIdSet = pi.sharedUserId != null;
        boolean processNameNotDefault = (pi.applicationInfo != null) && (!mPackageName.equals(pi.applicationInfo.processName));
        boolean sharable = sharedUserIdSet || processNameNotDefault;
        java.lang.ClassLoader contextClassLoader = (sharable) ? new android.app.LoadedApk.WarningContextClassLoader() : mClassLoader;
        java.lang.Thread.currentThread().setContextClassLoader(contextClassLoader);
    }

    private static class WarningContextClassLoader extends java.lang.ClassLoader {
        private static boolean warned = false;

        private void warn(java.lang.String methodName) {
            if (android.app.LoadedApk.WarningContextClassLoader.warned) {
                return;
            }
            android.app.LoadedApk.WarningContextClassLoader.warned = true;
            java.lang.Thread.currentThread().setContextClassLoader(getParent());
            android.util.Slog.w(android.app.ActivityThread.TAG, (((((("ClassLoader." + methodName) + ": ") + "The class loader returned by ") + "Thread.getContextClassLoader() may fail for processes ") + "that host multiple applications. You should explicitly ") + "specify a context class loader. For example: ") + "Thread.setContextClassLoader(getClass().getClassLoader());");
        }

        @java.lang.Override
        public java.net.URL getResource(java.lang.String resName) {
            warn("getResource");
            return getParent().getResource(resName);
        }

        @java.lang.Override
        public java.util.Enumeration<java.net.URL> getResources(java.lang.String resName) throws java.io.IOException {
            warn("getResources");
            return getParent().getResources(resName);
        }

        @java.lang.Override
        public java.io.InputStream getResourceAsStream(java.lang.String resName) {
            warn("getResourceAsStream");
            return getParent().getResourceAsStream(resName);
        }

        @java.lang.Override
        public java.lang.Class<?> loadClass(java.lang.String className) throws java.lang.ClassNotFoundException {
            warn("loadClass");
            return getParent().loadClass(className);
        }

        @java.lang.Override
        public void setClassAssertionStatus(java.lang.String cname, boolean enable) {
            warn("setClassAssertionStatus");
            getParent().setClassAssertionStatus(cname, enable);
        }

        @java.lang.Override
        public void setPackageAssertionStatus(java.lang.String pname, boolean enable) {
            warn("setPackageAssertionStatus");
            getParent().setPackageAssertionStatus(pname, enable);
        }

        @java.lang.Override
        public void setDefaultAssertionStatus(boolean enable) {
            warn("setDefaultAssertionStatus");
            getParent().setDefaultAssertionStatus(enable);
        }

        @java.lang.Override
        public void clearAssertionStatus() {
            warn("clearAssertionStatus");
            getParent().clearAssertionStatus();
        }
    }

    public java.lang.String getAppDir() {
        return mAppDir;
    }

    public java.lang.String getLibDir() {
        return mLibDir;
    }

    public java.lang.String getResDir() {
        return mResDir;
    }

    public java.lang.String[] getSplitAppDirs() {
        return mSplitAppDirs;
    }

    public java.lang.String[] getSplitResDirs() {
        return mSplitResDirs;
    }

    public java.lang.String[] getOverlayDirs() {
        return mOverlayDirs;
    }

    public java.lang.String getDataDir() {
        return mDataDir;
    }

    public java.io.File getDataDirFile() {
        return mDataDirFile;
    }

    public java.io.File getDeviceProtectedDataDirFile() {
        return mDeviceProtectedDataDirFile;
    }

    public java.io.File getCredentialProtectedDataDirFile() {
        return mCredentialProtectedDataDirFile;
    }

    public android.content.res.AssetManager getAssets(android.app.ActivityThread mainThread) {
        return getResources(mainThread).getAssets();
    }

    public android.content.res.Resources getResources(android.app.ActivityThread mainThread) {
        if (mResources == null) {
            mResources = mainThread.getTopLevelResources(mResDir, mSplitResDirs, mOverlayDirs, mApplicationInfo.sharedLibraryFiles, android.view.Display.DEFAULT_DISPLAY, this);
        }
        return mResources;
    }

    public android.app.Application makeApplication(boolean forceDefaultAppClass, android.app.Instrumentation instrumentation) {
        if (mApplication != null) {
            return mApplication;
        }
        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "makeApplication");
        android.app.Application app = null;
        java.lang.String appClass = mApplicationInfo.className;
        if (forceDefaultAppClass || (appClass == null)) {
            appClass = "android.app.Application";
        }
        try {
            java.lang.ClassLoader cl = getClassLoader();
            if (!mPackageName.equals("android")) {
                android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "initializeJavaContextClassLoader");
                initializeJavaContextClassLoader();
                android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
            }
            android.app.ContextImpl appContext = android.app.ContextImpl.createAppContext(mActivityThread, this);
            app = mActivityThread.mInstrumentation.newApplication(cl, appClass, appContext);
            appContext.setOuterContext(app);
        } catch (java.lang.Exception e) {
            if (!mActivityThread.mInstrumentation.onException(app, e)) {
                android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                throw new java.lang.RuntimeException((("Unable to instantiate application " + appClass) + ": ") + e.toString(), e);
            }
        }
        mActivityThread.mAllApplications.add(app);
        mApplication = app;
        if (instrumentation != null) {
            try {
                instrumentation.callApplicationOnCreate(app);
            } catch (java.lang.Exception e) {
                if (!instrumentation.onException(app, e)) {
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    throw new java.lang.RuntimeException((("Unable to create application " + app.getClass().getName()) + ": ") + e.toString(), e);
                }
            }
        }
        // Rewrite the R 'constants' for all library apks.
        android.util.SparseArray<java.lang.String> packageIdentifiers = getAssets(mActivityThread).getAssignedPackageIdentifiers();
        final int N = packageIdentifiers.size();
        for (int i = 0; i < N; i++) {
            final int id = packageIdentifiers.keyAt(i);
            if ((id == 0x1) || (id == 0x7f)) {
                continue;
            }
            rewriteRValues(getClassLoader(), packageIdentifiers.valueAt(i), id);
        }
        android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
        return app;
    }

    private void rewriteRValues(java.lang.ClassLoader cl, java.lang.String packageName, int id) {
        final java.lang.Class<?> rClazz;
        try {
            rClazz = cl.loadClass(packageName + ".R");
        } catch (java.lang.ClassNotFoundException e) {
            // This is not necessarily an error, as some packages do not ship with resources
            // (or they do not need rewriting).
            android.util.Log.i(android.app.LoadedApk.TAG, "No resource references to update in package " + packageName);
            return;
        }
        final java.lang.reflect.Method callback;
        try {
            callback = rClazz.getMethod("onResourcesLoaded", int.class);
        } catch (java.lang.NoSuchMethodException e) {
            // No rewriting to be done.
            return;
        }
        java.lang.Throwable cause;
        try {
            callback.invoke(null, id);
            return;
        } catch (java.lang.IllegalAccessException e) {
            cause = e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            cause = e.getCause();
        }
        throw new java.lang.RuntimeException("Failed to rewrite resource references for " + packageName, cause);
    }

    public void removeContextRegistrations(android.content.Context context, java.lang.String who, java.lang.String what) {
        final boolean reportRegistrationLeaks = android.os.StrictMode.vmRegistrationLeaksEnabled();
        synchronized(mReceivers) {
            android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk.ReceiverDispatcher> rmap = mReceivers.remove(context);
            if (rmap != null) {
                for (int i = 0; i < rmap.size(); i++) {
                    android.app.LoadedApk.ReceiverDispatcher rd = rmap.valueAt(i);
                    android.app.IntentReceiverLeaked leak = new android.app.IntentReceiverLeaked(((((((what + " ") + who) + " has leaked IntentReceiver ") + rd.getIntentReceiver()) + " that was ") + "originally registered here. Are you missing a ") + "call to unregisterReceiver()?");
                    leak.setStackTrace(rd.getLocation().getStackTrace());
                    android.util.Slog.e(android.app.ActivityThread.TAG, leak.getMessage(), leak);
                    if (reportRegistrationLeaks) {
                        android.os.StrictMode.onIntentReceiverLeaked(leak);
                    }
                    try {
                        android.app.ActivityManagerNative.getDefault().unregisterReceiver(rd.getIIntentReceiver());
                    } catch (android.os.RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
            }
            mUnregisteredReceivers.remove(context);
        }
        synchronized(mServices) {
            // Slog.i(TAG, "Receiver registrations: " + mReceivers);
            android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk.ServiceDispatcher> smap = mServices.remove(context);
            if (smap != null) {
                for (int i = 0; i < smap.size(); i++) {
                    android.app.LoadedApk.ServiceDispatcher sd = smap.valueAt(i);
                    android.app.ServiceConnectionLeaked leak = new android.app.ServiceConnectionLeaked(((((what + " ") + who) + " has leaked ServiceConnection ") + sd.getServiceConnection()) + " that was originally bound here");
                    leak.setStackTrace(sd.getLocation().getStackTrace());
                    android.util.Slog.e(android.app.ActivityThread.TAG, leak.getMessage(), leak);
                    if (reportRegistrationLeaks) {
                        android.os.StrictMode.onServiceConnectionLeaked(leak);
                    }
                    try {
                        android.app.ActivityManagerNative.getDefault().unbindService(sd.getIServiceConnection());
                    } catch (android.os.RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                    sd.doForget();
                }
            }
            mUnboundServices.remove(context);
            // Slog.i(TAG, "Service registrations: " + mServices);
        }
    }

    public android.content.IIntentReceiver getReceiverDispatcher(android.content.BroadcastReceiver r, android.content.Context context, android.os.Handler handler, android.app.Instrumentation instrumentation, boolean registered) {
        synchronized(mReceivers) {
            android.app.LoadedApk.ReceiverDispatcher rd = null;
            android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk.ReceiverDispatcher> map = null;
            if (registered) {
                map = mReceivers.get(context);
                if (map != null) {
                    rd = map.get(r);
                }
            }
            if (rd == null) {
                rd = new android.app.LoadedApk.ReceiverDispatcher(r, context, handler, instrumentation, registered);
                if (registered) {
                    if (map == null) {
                        map = new android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk.ReceiverDispatcher>();
                        mReceivers.put(context, map);
                    }
                    map.put(r, rd);
                }
            } else {
                rd.validate(context, handler);
            }
            rd.mForgotten = false;
            return rd.getIIntentReceiver();
        }
    }

    public android.content.IIntentReceiver forgetReceiverDispatcher(android.content.Context context, android.content.BroadcastReceiver r) {
        synchronized(mReceivers) {
            android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk.ReceiverDispatcher> map = mReceivers.get(context);
            android.app.LoadedApk.ReceiverDispatcher rd = null;
            if (map != null) {
                rd = map.get(r);
                if (rd != null) {
                    map.remove(r);
                    if (map.size() == 0) {
                        mReceivers.remove(context);
                    }
                    if (r.getDebugUnregister()) {
                        android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk.ReceiverDispatcher> holder = mUnregisteredReceivers.get(context);
                        if (holder == null) {
                            holder = new android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk.ReceiverDispatcher>();
                            mUnregisteredReceivers.put(context, holder);
                        }
                        java.lang.RuntimeException ex = new java.lang.IllegalArgumentException("Originally unregistered here:");
                        ex.fillInStackTrace();
                        rd.setUnregisterLocation(ex);
                        holder.put(r, rd);
                    }
                    rd.mForgotten = true;
                    return rd.getIIntentReceiver();
                }
            }
            android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk.ReceiverDispatcher> holder = mUnregisteredReceivers.get(context);
            if (holder != null) {
                rd = holder.get(r);
                if (rd != null) {
                    java.lang.RuntimeException ex = rd.getUnregisterLocation();
                    throw new java.lang.IllegalArgumentException(("Unregistering Receiver " + r) + " that was already unregistered", ex);
                }
            }
            if (context == null) {
                throw new java.lang.IllegalStateException((("Unbinding Receiver " + r) + " from Context that is no longer in use: ") + context);
            } else {
                throw new java.lang.IllegalArgumentException("Receiver not registered: " + r);
            }
        }
    }

    static final class ReceiverDispatcher {
        static final class InnerReceiver extends android.content.IIntentReceiver.Stub {
            final java.lang.ref.WeakReference<android.app.LoadedApk.ReceiverDispatcher> mDispatcher;

            final android.app.LoadedApk.ReceiverDispatcher mStrongRef;

            InnerReceiver(android.app.LoadedApk.ReceiverDispatcher rd, boolean strong) {
                mDispatcher = new java.lang.ref.WeakReference<android.app.LoadedApk.ReceiverDispatcher>(rd);
                mStrongRef = (strong) ? rd : null;
            }

            @java.lang.Override
            public void performReceive(android.content.Intent intent, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
                final android.app.LoadedApk.ReceiverDispatcher rd;
                if (intent == null) {
                    android.util.Log.wtf(android.app.LoadedApk.TAG, "Null intent received");
                    rd = null;
                } else {
                    rd = mDispatcher.get();
                }
                if (android.app.ActivityThread.DEBUG_BROADCAST) {
                    int seq = intent.getIntExtra("seq", -1);
                    android.util.Slog.i(android.app.ActivityThread.TAG, (((("Receiving broadcast " + intent.getAction()) + " seq=") + seq) + " to ") + (rd != null ? rd.mReceiver : null));
                }
                if (rd != null) {
                    rd.performReceive(intent, resultCode, data, extras, ordered, sticky, sendingUser);
                } else {
                    // The activity manager dispatched a broadcast to a registered
                    // receiver in this process, but before it could be delivered the
                    // receiver was unregistered.  Acknowledge the broadcast on its
                    // behalf so that the system's broadcast sequence can continue.
                    if (android.app.ActivityThread.DEBUG_BROADCAST)
                        android.util.Slog.i(android.app.ActivityThread.TAG, "Finishing broadcast to unregistered receiver");

                    android.app.IActivityManager mgr = android.app.ActivityManagerNative.getDefault();
                    try {
                        if (extras != null) {
                            extras.setAllowFds(false);
                        }
                        mgr.finishReceiver(this, resultCode, data, extras, false, intent.getFlags());
                    } catch (android.os.RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
            }
        }

        final IIntentReceiver.Stub mIIntentReceiver;

        final android.content.BroadcastReceiver mReceiver;

        final android.content.Context mContext;

        final android.os.Handler mActivityThread;

        final android.app.Instrumentation mInstrumentation;

        final boolean mRegistered;

        final android.app.IntentReceiverLeaked mLocation;

        java.lang.RuntimeException mUnregisterLocation;

        boolean mForgotten;

        final class Args extends android.content.BroadcastReceiver.PendingResult implements java.lang.Runnable {
            private android.content.Intent mCurIntent;

            private final boolean mOrdered;

            private boolean mDispatched;

            public Args(android.content.Intent intent, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras, boolean ordered, boolean sticky, int sendingUser) {
                super(resultCode, resultData, resultExtras, mRegistered ? android.content.BroadcastReceiver.PendingResult.TYPE_REGISTERED : android.content.BroadcastReceiver.PendingResult.TYPE_UNREGISTERED, ordered, sticky, mIIntentReceiver.asBinder(), sendingUser, intent.getFlags());
                mCurIntent = intent;
                mOrdered = ordered;
            }

            public void run() {
                final android.content.BroadcastReceiver receiver = mReceiver;
                final boolean ordered = mOrdered;
                if (android.app.ActivityThread.DEBUG_BROADCAST) {
                    int seq = mCurIntent.getIntExtra("seq", -1);
                    android.util.Slog.i(android.app.ActivityThread.TAG, (((("Dispatching broadcast " + mCurIntent.getAction()) + " seq=") + seq) + " to ") + mReceiver);
                    android.util.Slog.i(android.app.ActivityThread.TAG, (("  mRegistered=" + mRegistered) + " mOrderedHint=") + ordered);
                }
                final android.app.IActivityManager mgr = android.app.ActivityManagerNative.getDefault();
                final android.content.Intent intent = mCurIntent;
                if (intent == null) {
                    android.util.Log.wtf(android.app.LoadedApk.TAG, "Null intent being dispatched, mDispatched=" + mDispatched);
                }
                mCurIntent = null;
                mDispatched = true;
                if (((receiver == null) || (intent == null)) || mForgotten) {
                    if (mRegistered && ordered) {
                        if (android.app.ActivityThread.DEBUG_BROADCAST)
                            android.util.Slog.i(android.app.ActivityThread.TAG, "Finishing null broadcast to " + mReceiver);

                        sendFinished(mgr);
                    }
                    return;
                }
                android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "broadcastReceiveReg");
                try {
                    java.lang.ClassLoader cl = mReceiver.getClass().getClassLoader();
                    intent.setExtrasClassLoader(cl);
                    intent.prepareToEnterProcess();
                    setExtrasClassLoader(cl);
                    receiver.setPendingResult(this);
                    receiver.onReceive(mContext, intent);
                } catch (java.lang.Exception e) {
                    if (mRegistered && ordered) {
                        if (android.app.ActivityThread.DEBUG_BROADCAST)
                            android.util.Slog.i(android.app.ActivityThread.TAG, "Finishing failed broadcast to " + mReceiver);

                        sendFinished(mgr);
                    }
                    if ((mInstrumentation == null) || (!mInstrumentation.onException(mReceiver, e))) {
                        android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                        throw new java.lang.RuntimeException((("Error receiving broadcast " + intent) + " in ") + mReceiver, e);
                    }
                }
                if (receiver.getPendingResult() != null) {
                    finish();
                }
                android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
            }
        }

        ReceiverDispatcher(android.content.BroadcastReceiver receiver, android.content.Context context, android.os.Handler activityThread, android.app.Instrumentation instrumentation, boolean registered) {
            if (activityThread == null) {
                throw new java.lang.NullPointerException("Handler must not be null");
            }
            mIIntentReceiver = new android.app.LoadedApk.ReceiverDispatcher.InnerReceiver(this, !registered);
            mReceiver = receiver;
            mContext = context;
            mActivityThread = activityThread;
            mInstrumentation = instrumentation;
            mRegistered = registered;
            mLocation = new android.app.IntentReceiverLeaked(null);
            mLocation.fillInStackTrace();
        }

        void validate(android.content.Context context, android.os.Handler activityThread) {
            if (mContext != context) {
                throw new java.lang.IllegalStateException(((((("Receiver " + mReceiver) + " registered with differing Context (was ") + mContext) + " now ") + context) + ")");
            }
            if (mActivityThread != activityThread) {
                throw new java.lang.IllegalStateException(((((("Receiver " + mReceiver) + " registered with differing handler (was ") + mActivityThread) + " now ") + activityThread) + ")");
            }
        }

        android.app.IntentReceiverLeaked getLocation() {
            return mLocation;
        }

        android.content.BroadcastReceiver getIntentReceiver() {
            return mReceiver;
        }

        android.content.IIntentReceiver getIIntentReceiver() {
            return mIIntentReceiver;
        }

        void setUnregisterLocation(java.lang.RuntimeException ex) {
            mUnregisterLocation = ex;
        }

        java.lang.RuntimeException getUnregisterLocation() {
            return mUnregisterLocation;
        }

        public void performReceive(android.content.Intent intent, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
            final android.app.LoadedApk.ReceiverDispatcher.Args args = new android.app.LoadedApk.ReceiverDispatcher.Args(intent, resultCode, data, extras, ordered, sticky, sendingUser);
            if (intent == null) {
                android.util.Log.wtf(android.app.LoadedApk.TAG, "Null intent received");
            } else {
                if (android.app.ActivityThread.DEBUG_BROADCAST) {
                    int seq = intent.getIntExtra("seq", -1);
                    android.util.Slog.i(android.app.ActivityThread.TAG, (((("Enqueueing broadcast " + intent.getAction()) + " seq=") + seq) + " to ") + mReceiver);
                }
            }
            if ((intent == null) || (!mActivityThread.post(args))) {
                if (mRegistered && ordered) {
                    android.app.IActivityManager mgr = android.app.ActivityManagerNative.getDefault();
                    if (android.app.ActivityThread.DEBUG_BROADCAST)
                        android.util.Slog.i(android.app.ActivityThread.TAG, "Finishing sync broadcast to " + mReceiver);

                    args.sendFinished(mgr);
                }
            }
        }
    }

    public final android.app.IServiceConnection getServiceDispatcher(android.content.ServiceConnection c, android.content.Context context, android.os.Handler handler, int flags) {
        synchronized(mServices) {
            android.app.LoadedApk.ServiceDispatcher sd = null;
            android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk.ServiceDispatcher> map = mServices.get(context);
            if (map != null) {
                sd = map.get(c);
            }
            if (sd == null) {
                sd = new android.app.LoadedApk.ServiceDispatcher(c, context, handler, flags);
                if (map == null) {
                    map = new android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk.ServiceDispatcher>();
                    mServices.put(context, map);
                }
                map.put(c, sd);
            } else {
                sd.validate(context, handler);
            }
            return sd.getIServiceConnection();
        }
    }

    public final android.app.IServiceConnection forgetServiceDispatcher(android.content.Context context, android.content.ServiceConnection c) {
        synchronized(mServices) {
            android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk.ServiceDispatcher> map = mServices.get(context);
            android.app.LoadedApk.ServiceDispatcher sd = null;
            if (map != null) {
                sd = map.get(c);
                if (sd != null) {
                    map.remove(c);
                    sd.doForget();
                    if (map.size() == 0) {
                        mServices.remove(context);
                    }
                    if ((sd.getFlags() & android.content.Context.BIND_DEBUG_UNBIND) != 0) {
                        android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk.ServiceDispatcher> holder = mUnboundServices.get(context);
                        if (holder == null) {
                            holder = new android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk.ServiceDispatcher>();
                            mUnboundServices.put(context, holder);
                        }
                        java.lang.RuntimeException ex = new java.lang.IllegalArgumentException("Originally unbound here:");
                        ex.fillInStackTrace();
                        sd.setUnbindLocation(ex);
                        holder.put(c, sd);
                    }
                    return sd.getIServiceConnection();
                }
            }
            android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk.ServiceDispatcher> holder = mUnboundServices.get(context);
            if (holder != null) {
                sd = holder.get(c);
                if (sd != null) {
                    java.lang.RuntimeException ex = sd.getUnbindLocation();
                    throw new java.lang.IllegalArgumentException(("Unbinding Service " + c) + " that was already unbound", ex);
                }
            }
            if (context == null) {
                throw new java.lang.IllegalStateException((("Unbinding Service " + c) + " from Context that is no longer in use: ") + context);
            } else {
                throw new java.lang.IllegalArgumentException("Service not registered: " + c);
            }
        }
    }

    static final class ServiceDispatcher {
        private final android.app.LoadedApk.ServiceDispatcher.InnerConnection mIServiceConnection;

        private final android.content.ServiceConnection mConnection;

        private final android.content.Context mContext;

        private final android.os.Handler mActivityThread;

        private final android.app.ServiceConnectionLeaked mLocation;

        private final int mFlags;

        private java.lang.RuntimeException mUnbindLocation;

        private boolean mForgotten;

        private static class ConnectionInfo {
            android.os.IBinder binder;

            android.os.IBinder.DeathRecipient deathMonitor;
        }

        private static class InnerConnection extends android.app.IServiceConnection.Stub {
            final java.lang.ref.WeakReference<android.app.LoadedApk.ServiceDispatcher> mDispatcher;

            InnerConnection(android.app.LoadedApk.ServiceDispatcher sd) {
                mDispatcher = new java.lang.ref.WeakReference<android.app.LoadedApk.ServiceDispatcher>(sd);
            }

            public void connected(android.content.ComponentName name, android.os.IBinder service) throws android.os.RemoteException {
                android.app.LoadedApk.ServiceDispatcher sd = mDispatcher.get();
                if (sd != null) {
                    sd.connected(name, service);
                }
            }
        }

        private final android.util.ArrayMap<android.content.ComponentName, android.app.LoadedApk.ServiceDispatcher.ConnectionInfo> mActiveConnections = new android.util.ArrayMap<android.content.ComponentName, android.app.LoadedApk.ServiceDispatcher.ConnectionInfo>();

        ServiceDispatcher(android.content.ServiceConnection conn, android.content.Context context, android.os.Handler activityThread, int flags) {
            mIServiceConnection = new android.app.LoadedApk.ServiceDispatcher.InnerConnection(this);
            mConnection = conn;
            mContext = context;
            mActivityThread = activityThread;
            mLocation = new android.app.ServiceConnectionLeaked(null);
            mLocation.fillInStackTrace();
            mFlags = flags;
        }

        void validate(android.content.Context context, android.os.Handler activityThread) {
            if (mContext != context) {
                throw new java.lang.RuntimeException(((((("ServiceConnection " + mConnection) + " registered with differing Context (was ") + mContext) + " now ") + context) + ")");
            }
            if (mActivityThread != activityThread) {
                throw new java.lang.RuntimeException(((((("ServiceConnection " + mConnection) + " registered with differing handler (was ") + mActivityThread) + " now ") + activityThread) + ")");
            }
        }

        void doForget() {
            synchronized(this) {
                for (int i = 0; i < mActiveConnections.size(); i++) {
                    android.app.LoadedApk.ServiceDispatcher.ConnectionInfo ci = mActiveConnections.valueAt(i);
                    ci.binder.unlinkToDeath(ci.deathMonitor, 0);
                }
                mActiveConnections.clear();
                mForgotten = true;
            }
        }

        android.app.ServiceConnectionLeaked getLocation() {
            return mLocation;
        }

        android.content.ServiceConnection getServiceConnection() {
            return mConnection;
        }

        android.app.IServiceConnection getIServiceConnection() {
            return mIServiceConnection;
        }

        int getFlags() {
            return mFlags;
        }

        void setUnbindLocation(java.lang.RuntimeException ex) {
            mUnbindLocation = ex;
        }

        java.lang.RuntimeException getUnbindLocation() {
            return mUnbindLocation;
        }

        public void connected(android.content.ComponentName name, android.os.IBinder service) {
            if (mActivityThread != null) {
                mActivityThread.post(new android.app.LoadedApk.ServiceDispatcher.RunConnection(name, service, 0));
            } else {
                doConnected(name, service);
            }
        }

        public void death(android.content.ComponentName name, android.os.IBinder service) {
            if (mActivityThread != null) {
                mActivityThread.post(new android.app.LoadedApk.ServiceDispatcher.RunConnection(name, service, 1));
            } else {
                doDeath(name, service);
            }
        }

        public void doConnected(android.content.ComponentName name, android.os.IBinder service) {
            android.app.LoadedApk.ServiceDispatcher.ConnectionInfo old;
            android.app.LoadedApk.ServiceDispatcher.ConnectionInfo info;
            synchronized(this) {
                if (mForgotten) {
                    // We unbound before receiving the connection; ignore
                    // any connection received.
                    return;
                }
                old = mActiveConnections.get(name);
                if ((old != null) && (old.binder == service)) {
                    // Huh, already have this one.  Oh well!
                    return;
                }
                if (service != null) {
                    // A new service is being connected... set it all up.
                    info = new android.app.LoadedApk.ServiceDispatcher.ConnectionInfo();
                    info.binder = service;
                    info.deathMonitor = new android.app.LoadedApk.ServiceDispatcher.DeathMonitor(name, service);
                    try {
                        service.linkToDeath(info.deathMonitor, 0);
                        mActiveConnections.put(name, info);
                    } catch (android.os.RemoteException e) {
                        // This service was dead before we got it...  just
                        // don't do anything with it.
                        mActiveConnections.remove(name);
                        return;
                    }
                } else {
                    // The named service is being disconnected... clean up.
                    mActiveConnections.remove(name);
                }
                if (old != null) {
                    old.binder.unlinkToDeath(old.deathMonitor, 0);
                }
            }
            // If there was an old service, it is now disconnected.
            if (old != null) {
                mConnection.onServiceDisconnected(name);
            }
            // If there is a new service, it is now connected.
            if (service != null) {
                mConnection.onServiceConnected(name, service);
            }
        }

        public void doDeath(android.content.ComponentName name, android.os.IBinder service) {
            synchronized(this) {
                android.app.LoadedApk.ServiceDispatcher.ConnectionInfo old = mActiveConnections.get(name);
                if ((old == null) || (old.binder != service)) {
                    // Death for someone different than who we last
                    // reported...  just ignore it.
                    return;
                }
                mActiveConnections.remove(name);
                old.binder.unlinkToDeath(old.deathMonitor, 0);
            }
            mConnection.onServiceDisconnected(name);
        }

        private final class RunConnection implements java.lang.Runnable {
            RunConnection(android.content.ComponentName name, android.os.IBinder service, int command) {
                mName = name;
                mService = service;
                mCommand = command;
            }

            public void run() {
                if (mCommand == 0) {
                    doConnected(mName, mService);
                } else
                    if (mCommand == 1) {
                        doDeath(mName, mService);
                    }

            }

            final android.content.ComponentName mName;

            final android.os.IBinder mService;

            final int mCommand;
        }

        private final class DeathMonitor implements android.os.IBinder.DeathRecipient {
            DeathMonitor(android.content.ComponentName name, android.os.IBinder service) {
                mName = name;
                mService = service;
            }

            public void binderDied() {
                death(mName, mService);
            }

            final android.content.ComponentName mName;

            final android.os.IBinder mService;
        }
    }
}

