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
package android.app;


/**
 *
 *
 * @unknown 
 */
public class ResourcesManager {
    static final java.lang.String TAG = "ResourcesManager";

    private static final boolean DEBUG = false;

    private static android.app.ResourcesManager sResourcesManager;

    /**
     * Predicate that returns true if a WeakReference is gc'ed.
     */
    private static final java.util.function.Predicate<java.lang.ref.WeakReference<android.content.res.Resources>> sEmptyReferencePredicate = new java.util.function.Predicate<java.lang.ref.WeakReference<android.content.res.Resources>>() {
        @java.lang.Override
        public boolean test(java.lang.ref.WeakReference<android.content.res.Resources> weakRef) {
            return (weakRef == null) || (weakRef.get() == null);
        }
    };

    /**
     * The global compatibility settings.
     */
    private android.content.res.CompatibilityInfo mResCompatibilityInfo;

    /**
     * The global configuration upon which all Resources are based. Multi-window Resources
     * apply their overrides to this configuration.
     */
    private final android.content.res.Configuration mResConfiguration = new android.content.res.Configuration();

    /**
     * A mapping of ResourceImpls and their configurations. These are heavy weight objects
     * which should be reused as much as possible.
     */
    private final android.util.ArrayMap<android.content.res.ResourcesKey, java.lang.ref.WeakReference<android.content.res.ResourcesImpl>> mResourceImpls = new android.util.ArrayMap<>();

    /**
     * A list of Resource references that can be reused.
     */
    private final java.util.ArrayList<java.lang.ref.WeakReference<android.content.res.Resources>> mResourceReferences = new java.util.ArrayList<>();

    /**
     * Resources and base configuration override associated with an Activity.
     */
    private static class ActivityResources {
        public final android.content.res.Configuration overrideConfig = new android.content.res.Configuration();

        public final java.util.ArrayList<java.lang.ref.WeakReference<android.content.res.Resources>> activityResources = new java.util.ArrayList<>();
    }

    /**
     * Each Activity may has a base override configuration that is applied to each Resources object,
     * which in turn may have their own override configuration specified.
     */
    private final java.util.WeakHashMap<android.os.IBinder, android.app.ResourcesManager.ActivityResources> mActivityResourceReferences = new java.util.WeakHashMap<>();

    /**
     * A cache of DisplayId to DisplayAdjustments.
     */
    private final android.util.ArrayMap<android.util.Pair<java.lang.Integer, android.view.DisplayAdjustments>, java.lang.ref.WeakReference<android.view.Display>> mDisplays = new android.util.ArrayMap<>();

    public static android.app.ResourcesManager getInstance() {
        synchronized(android.app.ResourcesManager.class) {
            if (android.app.ResourcesManager.sResourcesManager == null) {
                android.app.ResourcesManager.sResourcesManager = new android.app.ResourcesManager();
            }
            return android.app.ResourcesManager.sResourcesManager;
        }
    }

    /**
     * Invalidate and destroy any resources that reference content under the
     * given filesystem path. Typically used when unmounting a storage device to
     * try as hard as possible to release any open FDs.
     */
    public void invalidatePath(java.lang.String path) {
        synchronized(this) {
            int count = 0;
            for (int i = 0; i < mResourceImpls.size();) {
                final android.content.res.ResourcesKey key = mResourceImpls.keyAt(i);
                if (key.isPathReferenced(path)) {
                    final android.content.res.ResourcesImpl res = mResourceImpls.removeAt(i).get();
                    if (res != null) {
                        res.flushLayoutCache();
                    }
                    count++;
                } else {
                    i++;
                }
            }
            android.util.Log.i(android.app.ResourcesManager.TAG, (("Invalidated " + count) + " asset managers that referenced ") + path);
        }
    }

    public android.content.res.Configuration getConfiguration() {
        synchronized(this) {
            return mResConfiguration;
        }
    }

    android.util.DisplayMetrics getDisplayMetrics() {
        return getDisplayMetrics(android.view.Display.DEFAULT_DISPLAY, android.view.DisplayAdjustments.DEFAULT_DISPLAY_ADJUSTMENTS);
    }

    /**
     * Protected so that tests can override and returns something a fixed value.
     */
    @com.android.internal.annotations.VisibleForTesting
    @android.annotation.NonNull
    protected android.util.DisplayMetrics getDisplayMetrics(int displayId, android.view.DisplayAdjustments da) {
        android.util.DisplayMetrics dm = new android.util.DisplayMetrics();
        final android.view.Display display = getAdjustedDisplay(displayId, da);
        if (display != null) {
            display.getMetrics(dm);
        } else {
            dm.setToDefaults();
        }
        return dm;
    }

    private static void applyNonDefaultDisplayMetricsToConfiguration(@android.annotation.NonNull
    android.util.DisplayMetrics dm, @android.annotation.NonNull
    android.content.res.Configuration config) {
        config.touchscreen = android.content.res.Configuration.TOUCHSCREEN_NOTOUCH;
        config.densityDpi = dm.densityDpi;
        config.screenWidthDp = ((int) (dm.widthPixels / dm.density));
        config.screenHeightDp = ((int) (dm.heightPixels / dm.density));
        int sl = android.content.res.Configuration.resetScreenLayout(config.screenLayout);
        if (dm.widthPixels > dm.heightPixels) {
            config.orientation = android.content.res.Configuration.ORIENTATION_LANDSCAPE;
            config.screenLayout = android.content.res.Configuration.reduceScreenLayout(sl, config.screenWidthDp, config.screenHeightDp);
        } else {
            config.orientation = android.content.res.Configuration.ORIENTATION_PORTRAIT;
            config.screenLayout = android.content.res.Configuration.reduceScreenLayout(sl, config.screenHeightDp, config.screenWidthDp);
        }
        config.smallestScreenWidthDp = config.screenWidthDp;// assume screen does not rotate

        config.compatScreenWidthDp = config.screenWidthDp;
        config.compatScreenHeightDp = config.screenHeightDp;
        config.compatSmallestScreenWidthDp = config.smallestScreenWidthDp;
    }

    public boolean applyCompatConfigurationLocked(int displayDensity, @android.annotation.NonNull
    android.content.res.Configuration compatConfiguration) {
        if ((mResCompatibilityInfo != null) && (!mResCompatibilityInfo.supportsScreen())) {
            mResCompatibilityInfo.applyToConfiguration(displayDensity, compatConfiguration);
            return true;
        }
        return false;
    }

    /**
     * Returns an adjusted {@link Display} object based on the inputs or null if display isn't
     * available.
     *
     * @param displayId
     * 		display Id.
     * @param displayAdjustments
     * 		display adjustments.
     */
    public android.view.Display getAdjustedDisplay(final int displayId, @android.annotation.Nullable
    android.view.DisplayAdjustments displayAdjustments) {
        final android.view.DisplayAdjustments displayAdjustmentsCopy = (displayAdjustments != null) ? new android.view.DisplayAdjustments(displayAdjustments) : new android.view.DisplayAdjustments();
        final android.util.Pair<java.lang.Integer, android.view.DisplayAdjustments> key = android.util.Pair.create(displayId, displayAdjustmentsCopy);
        synchronized(this) {
            java.lang.ref.WeakReference<android.view.Display> wd = mDisplays.get(key);
            if (wd != null) {
                final android.view.Display display = wd.get();
                if (display != null) {
                    return display;
                }
            }
            final android.hardware.display.DisplayManagerGlobal dm = android.hardware.display.DisplayManagerGlobal.getInstance();
            if (dm == null) {
                // may be null early in system startup
                return null;
            }
            final android.view.Display display = dm.getCompatibleDisplay(displayId, key.second);
            if (display != null) {
                mDisplays.put(key, new java.lang.ref.WeakReference<>(display));
            }
            return display;
        }
    }

    /**
     * Creates an AssetManager from the paths within the ResourcesKey.
     *
     * This can be overridden in tests so as to avoid creating a real AssetManager with
     * real APK paths.
     *
     * @param key
     * 		The key containing the resource paths to add to the AssetManager.
     * @return a new AssetManager.
     */
    @com.android.internal.annotations.VisibleForTesting
    @android.annotation.Nullable
    protected android.content.res.AssetManager createAssetManager(@android.annotation.NonNull
    final android.content.res.ResourcesKey key) {
        android.content.res.AssetManager assets = new android.content.res.AssetManager();
        // resDir can be null if the 'android' package is creating a new Resources object.
        // This is fine, since each AssetManager automatically loads the 'android' package
        // already.
        if (key.mResDir != null) {
            if (assets.addAssetPath(key.mResDir) == 0) {
                android.util.Log.e(android.app.ResourcesManager.TAG, "failed to add asset path " + key.mResDir);
                return null;
            }
        }
        if (key.mSplitResDirs != null) {
            for (final java.lang.String splitResDir : key.mSplitResDirs) {
                if (assets.addAssetPath(splitResDir) == 0) {
                    android.util.Log.e(android.app.ResourcesManager.TAG, "failed to add split asset path " + splitResDir);
                    return null;
                }
            }
        }
        if (key.mOverlayDirs != null) {
            for (final java.lang.String idmapPath : key.mOverlayDirs) {
                assets.addOverlayPath(idmapPath);
            }
        }
        if (key.mLibDirs != null) {
            for (final java.lang.String libDir : key.mLibDirs) {
                if (libDir.endsWith(".apk")) {
                    // Avoid opening files we know do not have resources,
                    // like code-only .jar files.
                    if (assets.addAssetPathAsSharedLibrary(libDir) == 0) {
                        android.util.Log.w(android.app.ResourcesManager.TAG, ("Asset path '" + libDir) + "' does not exist or contains no resources.");
                    }
                }
            }
        }
        return assets;
    }

    private android.content.res.Configuration generateConfig(@android.annotation.NonNull
    android.content.res.ResourcesKey key, @android.annotation.NonNull
    android.util.DisplayMetrics dm) {
        android.content.res.Configuration config;
        final boolean isDefaultDisplay = key.mDisplayId == android.view.Display.DEFAULT_DISPLAY;
        final boolean hasOverrideConfig = key.hasOverrideConfiguration();
        if ((!isDefaultDisplay) || hasOverrideConfig) {
            config = new android.content.res.Configuration(getConfiguration());
            if (!isDefaultDisplay) {
                android.app.ResourcesManager.applyNonDefaultDisplayMetricsToConfiguration(dm, config);
            }
            if (hasOverrideConfig) {
                config.updateFrom(key.mOverrideConfiguration);
                if (android.app.ResourcesManager.DEBUG)
                    android.util.Slog.v(android.app.ResourcesManager.TAG, "Applied overrideConfig=" + key.mOverrideConfiguration);

            }
        } else {
            config = getConfiguration();
        }
        return config;
    }

    @android.annotation.Nullable
    private android.content.res.ResourcesImpl createResourcesImpl(@android.annotation.NonNull
    android.content.res.ResourcesKey key) {
        final android.view.DisplayAdjustments daj = new android.view.DisplayAdjustments(key.mOverrideConfiguration);
        daj.setCompatibilityInfo(key.mCompatInfo);
        final android.content.res.AssetManager assets = createAssetManager(key);
        if (assets == null) {
            return null;
        }
        final android.util.DisplayMetrics dm = getDisplayMetrics(key.mDisplayId, daj);
        final android.content.res.Configuration config = generateConfig(key, dm);
        final android.content.res.ResourcesImpl impl = new android.content.res.ResourcesImpl(assets, dm, config, daj);
        if (android.app.ResourcesManager.DEBUG) {
            android.util.Slog.d(android.app.ResourcesManager.TAG, (("- creating impl=" + impl) + " with key: ") + key);
        }
        return impl;
    }

    /**
     * Finds a cached ResourcesImpl object that matches the given ResourcesKey.
     *
     * @param key
     * 		The key to match.
     * @return a ResourcesImpl if the key matches a cache entry, null otherwise.
     */
    @android.annotation.Nullable
    private android.content.res.ResourcesImpl findResourcesImplForKeyLocked(@android.annotation.NonNull
    android.content.res.ResourcesKey key) {
        java.lang.ref.WeakReference<android.content.res.ResourcesImpl> weakImplRef = mResourceImpls.get(key);
        android.content.res.ResourcesImpl impl = (weakImplRef != null) ? weakImplRef.get() : null;
        if ((impl != null) && impl.getAssets().isUpToDate()) {
            return impl;
        }
        return null;
    }

    /**
     * Finds a cached ResourcesImpl object that matches the given ResourcesKey, or
     * creates a new one and caches it for future use.
     *
     * @param key
     * 		The key to match.
     * @return a ResourcesImpl object matching the key.
     */
    @android.annotation.Nullable
    private android.content.res.ResourcesImpl findOrCreateResourcesImplForKeyLocked(@android.annotation.NonNull
    android.content.res.ResourcesKey key) {
        android.content.res.ResourcesImpl impl = findResourcesImplForKeyLocked(key);
        if (impl == null) {
            impl = createResourcesImpl(key);
            if (impl != null) {
                mResourceImpls.put(key, new java.lang.ref.WeakReference<>(impl));
            }
        }
        return impl;
    }

    /**
     * Find the ResourcesKey that this ResourcesImpl object is associated with.
     *
     * @return the ResourcesKey or null if none was found.
     */
    @android.annotation.Nullable
    private android.content.res.ResourcesKey findKeyForResourceImplLocked(@android.annotation.NonNull
    android.content.res.ResourcesImpl resourceImpl) {
        final int refCount = mResourceImpls.size();
        for (int i = 0; i < refCount; i++) {
            java.lang.ref.WeakReference<android.content.res.ResourcesImpl> weakImplRef = mResourceImpls.valueAt(i);
            android.content.res.ResourcesImpl impl = (weakImplRef != null) ? weakImplRef.get() : null;
            if ((impl != null) && (resourceImpl == impl)) {
                return mResourceImpls.keyAt(i);
            }
        }
        return null;
    }

    /**
     * Check if activity resources have same override config as the provided on.
     *
     * @param activityToken
     * 		The Activity that resources should be associated with.
     * @param overrideConfig
     * 		The override configuration to be checked for equality with.
     * @return true if activity resources override config matches the provided one or they are both
    null, false otherwise.
     */
    boolean isSameResourcesOverrideConfig(@android.annotation.Nullable
    android.os.IBinder activityToken, @android.annotation.Nullable
    android.content.res.Configuration overrideConfig) {
        synchronized(this) {
            final android.app.ResourcesManager.ActivityResources activityResources = (activityToken != null) ? mActivityResourceReferences.get(activityToken) : null;
            if (activityResources == null) {
                return overrideConfig == null;
            } else {
                return java.util.Objects.equals(activityResources.overrideConfig, overrideConfig);
            }
        }
    }

    private android.app.ResourcesManager.ActivityResources getOrCreateActivityResourcesStructLocked(@android.annotation.NonNull
    android.os.IBinder activityToken) {
        android.app.ResourcesManager.ActivityResources activityResources = mActivityResourceReferences.get(activityToken);
        if (activityResources == null) {
            activityResources = new android.app.ResourcesManager.ActivityResources();
            mActivityResourceReferences.put(activityToken, activityResources);
        }
        return activityResources;
    }

    /**
     * Gets an existing Resources object tied to this Activity, or creates one if it doesn't exist
     * or the class loader is different.
     */
    @android.annotation.NonNull
    private android.content.res.Resources getOrCreateResourcesForActivityLocked(@android.annotation.NonNull
    android.os.IBinder activityToken, @android.annotation.NonNull
    java.lang.ClassLoader classLoader, @android.annotation.NonNull
    android.content.res.ResourcesImpl impl) {
        final android.app.ResourcesManager.ActivityResources activityResources = getOrCreateActivityResourcesStructLocked(activityToken);
        final int refCount = activityResources.activityResources.size();
        for (int i = 0; i < refCount; i++) {
            java.lang.ref.WeakReference<android.content.res.Resources> weakResourceRef = activityResources.activityResources.get(i);
            android.content.res.Resources resources = weakResourceRef.get();
            if (((resources != null) && java.util.Objects.equals(resources.getClassLoader(), classLoader)) && (resources.getImpl() == impl)) {
                if (android.app.ResourcesManager.DEBUG) {
                    android.util.Slog.d(android.app.ResourcesManager.TAG, "- using existing ref=" + resources);
                }
                return resources;
            }
        }
        android.content.res.Resources resources = new android.content.res.Resources(classLoader);
        resources.setImpl(impl);
        activityResources.activityResources.add(new java.lang.ref.WeakReference<>(resources));
        if (android.app.ResourcesManager.DEBUG) {
            android.util.Slog.d(android.app.ResourcesManager.TAG, "- creating new ref=" + resources);
            android.util.Slog.d(android.app.ResourcesManager.TAG, (("- setting ref=" + resources) + " with impl=") + impl);
        }
        return resources;
    }

    /**
     * Gets an existing Resources object if the class loader and ResourcesImpl are the same,
     * otherwise creates a new Resources object.
     */
    @android.annotation.NonNull
    private android.content.res.Resources getOrCreateResourcesLocked(@android.annotation.NonNull
    java.lang.ClassLoader classLoader, @android.annotation.NonNull
    android.content.res.ResourcesImpl impl) {
        // Find an existing Resources that has this ResourcesImpl set.
        final int refCount = mResourceReferences.size();
        for (int i = 0; i < refCount; i++) {
            java.lang.ref.WeakReference<android.content.res.Resources> weakResourceRef = mResourceReferences.get(i);
            android.content.res.Resources resources = weakResourceRef.get();
            if (((resources != null) && java.util.Objects.equals(resources.getClassLoader(), classLoader)) && (resources.getImpl() == impl)) {
                if (android.app.ResourcesManager.DEBUG) {
                    android.util.Slog.d(android.app.ResourcesManager.TAG, "- using existing ref=" + resources);
                }
                return resources;
            }
        }
        // Create a new Resources reference and use the existing ResourcesImpl object.
        android.content.res.Resources resources = new android.content.res.Resources(classLoader);
        resources.setImpl(impl);
        mResourceReferences.add(new java.lang.ref.WeakReference<>(resources));
        if (android.app.ResourcesManager.DEBUG) {
            android.util.Slog.d(android.app.ResourcesManager.TAG, "- creating new ref=" + resources);
            android.util.Slog.d(android.app.ResourcesManager.TAG, (("- setting ref=" + resources) + " with impl=") + impl);
        }
        return resources;
    }

    /**
     * Creates base resources for an Activity. Calls to
     * {@link #getResources(IBinder, String, String[], String[], String[], int, Configuration,
     * CompatibilityInfo, ClassLoader)} with the same activityToken will have their override
     * configurations merged with the one specified here.
     *
     * @param activityToken
     * 		Represents an Activity.
     * @param resDir
     * 		The base resource path. Can be null (only framework resources will be loaded).
     * @param splitResDirs
     * 		An array of split resource paths. Can be null.
     * @param overlayDirs
     * 		An array of overlay paths. Can be null.
     * @param libDirs
     * 		An array of resource library paths. Can be null.
     * @param displayId
     * 		The ID of the display for which to create the resources.
     * @param overrideConfig
     * 		The configuration to apply on top of the base configuration. Can be
     * 		null. This provides the base override for this Activity.
     * @param compatInfo
     * 		The compatibility settings to use. Cannot be null. A default to use is
     * 		{@link CompatibilityInfo#DEFAULT_COMPATIBILITY_INFO}.
     * @param classLoader
     * 		The class loader to use when inflating Resources. If null, the
     * 		{@link ClassLoader#getSystemClassLoader()} is used.
     * @return a Resources object from which to access resources.
     */
    @android.annotation.Nullable
    public android.content.res.Resources createBaseActivityResources(@android.annotation.NonNull
    android.os.IBinder activityToken, @android.annotation.Nullable
    java.lang.String resDir, @android.annotation.Nullable
    java.lang.String[] splitResDirs, @android.annotation.Nullable
    java.lang.String[] overlayDirs, @android.annotation.Nullable
    java.lang.String[] libDirs, int displayId, @android.annotation.Nullable
    android.content.res.Configuration overrideConfig, @android.annotation.NonNull
    android.content.res.CompatibilityInfo compatInfo, @android.annotation.Nullable
    java.lang.ClassLoader classLoader) {
        try {
            android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_RESOURCES, "ResourcesManager#createBaseActivityResources");
            final android.content.res.ResourcesKey key = // Copy
            new android.content.res.ResourcesKey(resDir, splitResDirs, overlayDirs, libDirs, displayId, overrideConfig != null ? new android.content.res.Configuration(overrideConfig) : null, compatInfo);
            classLoader = (classLoader != null) ? classLoader : java.lang.ClassLoader.getSystemClassLoader();
            if (android.app.ResourcesManager.DEBUG) {
                android.util.Slog.d(android.app.ResourcesManager.TAG, (("createBaseActivityResources activity=" + activityToken) + " with key=") + key);
            }
            synchronized(this) {
                // Force the creation of an ActivityResourcesStruct.
                getOrCreateActivityResourcesStructLocked(activityToken);
            }
            // Update any existing Activity Resources references.
            updateResourcesForActivity(activityToken, overrideConfig);
            // Now request an actual Resources object.
            return getOrCreateResources(activityToken, key, classLoader);
        } finally {
            android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_RESOURCES);
        }
    }

    /**
     * Gets an existing Resources object set with a ResourcesImpl object matching the given key,
     * or creates one if it doesn't exist.
     *
     * @param activityToken
     * 		The Activity this Resources object should be associated with.
     * @param key
     * 		The key describing the parameters of the ResourcesImpl object.
     * @param classLoader
     * 		The classloader to use for the Resources object.
     * 		If null, {@link ClassLoader#getSystemClassLoader()} is used.
     * @return A Resources object that gets updated when
    {@link #applyConfigurationToResourcesLocked(Configuration, CompatibilityInfo)}
    is called.
     */
    @android.annotation.Nullable
    private android.content.res.Resources getOrCreateResources(@android.annotation.Nullable
    android.os.IBinder activityToken, @android.annotation.NonNull
    android.content.res.ResourcesKey key, @android.annotation.NonNull
    java.lang.ClassLoader classLoader) {
        synchronized(this) {
            if (android.app.ResourcesManager.DEBUG) {
                java.lang.Throwable here = new java.lang.Throwable();
                here.fillInStackTrace();
                android.util.Slog.w(android.app.ResourcesManager.TAG, (("!! Get resources for activity=" + activityToken) + " key=") + key, here);
            }
            if (activityToken != null) {
                final android.app.ResourcesManager.ActivityResources activityResources = getOrCreateActivityResourcesStructLocked(activityToken);
                // Clean up any dead references so they don't pile up.
                com.android.internal.util.ArrayUtils.unstableRemoveIf(activityResources.activityResources, android.app.ResourcesManager.sEmptyReferencePredicate);
                // Rebase the key's override config on top of the Activity's base override.
                if (key.hasOverrideConfiguration() && (!activityResources.overrideConfig.equals(android.content.res.Configuration.EMPTY))) {
                    final android.content.res.Configuration temp = new android.content.res.Configuration(activityResources.overrideConfig);
                    temp.updateFrom(key.mOverrideConfiguration);
                    key.mOverrideConfiguration.setTo(temp);
                }
                android.content.res.ResourcesImpl resourcesImpl = findResourcesImplForKeyLocked(key);
                if (resourcesImpl != null) {
                    if (android.app.ResourcesManager.DEBUG) {
                        android.util.Slog.d(android.app.ResourcesManager.TAG, "- using existing impl=" + resourcesImpl);
                    }
                    return getOrCreateResourcesForActivityLocked(activityToken, classLoader, resourcesImpl);
                }
                // We will create the ResourcesImpl object outside of holding this lock.
            } else {
                // Clean up any dead references so they don't pile up.
                com.android.internal.util.ArrayUtils.unstableRemoveIf(mResourceReferences, android.app.ResourcesManager.sEmptyReferencePredicate);
                // Not tied to an Activity, find a shared Resources that has the right ResourcesImpl
                android.content.res.ResourcesImpl resourcesImpl = findResourcesImplForKeyLocked(key);
                if (resourcesImpl != null) {
                    if (android.app.ResourcesManager.DEBUG) {
                        android.util.Slog.d(android.app.ResourcesManager.TAG, "- using existing impl=" + resourcesImpl);
                    }
                    return getOrCreateResourcesLocked(classLoader, resourcesImpl);
                }
                // We will create the ResourcesImpl object outside of holding this lock.
            }
        }
        // If we're here, we didn't find a suitable ResourcesImpl to use, so create one now.
        android.content.res.ResourcesImpl resourcesImpl = createResourcesImpl(key);
        if (resourcesImpl == null) {
            return null;
        }
        synchronized(this) {
            android.content.res.ResourcesImpl existingResourcesImpl = findResourcesImplForKeyLocked(key);
            if (existingResourcesImpl != null) {
                if (android.app.ResourcesManager.DEBUG) {
                    android.util.Slog.d(android.app.ResourcesManager.TAG, (("- got beat! existing impl=" + existingResourcesImpl) + " new impl=") + resourcesImpl);
                }
                resourcesImpl.getAssets().close();
                resourcesImpl = existingResourcesImpl;
            } else {
                // Add this ResourcesImpl to the cache.
                mResourceImpls.put(key, new java.lang.ref.WeakReference<>(resourcesImpl));
            }
            final android.content.res.Resources resources;
            if (activityToken != null) {
                resources = getOrCreateResourcesForActivityLocked(activityToken, classLoader, resourcesImpl);
            } else {
                resources = getOrCreateResourcesLocked(classLoader, resourcesImpl);
            }
            return resources;
        }
    }

    /**
     * Gets or creates a new Resources object associated with the IBinder token. References returned
     * by this method live as long as the Activity, meaning they can be cached and used by the
     * Activity even after a configuration change. If any other parameter is changed
     * (resDir, splitResDirs, overrideConfig) for a given Activity, the same Resources object
     * is updated and handed back to the caller. However, changing the class loader will result in a
     * new Resources object.
     * <p/>
     * If activityToken is null, a cached Resources object will be returned if it matches the
     * input parameters. Otherwise a new Resources object that satisfies these parameters is
     * returned.
     *
     * @param activityToken
     * 		Represents an Activity. If null, global resources are assumed.
     * @param resDir
     * 		The base resource path. Can be null (only framework resources will be loaded).
     * @param splitResDirs
     * 		An array of split resource paths. Can be null.
     * @param overlayDirs
     * 		An array of overlay paths. Can be null.
     * @param libDirs
     * 		An array of resource library paths. Can be null.
     * @param displayId
     * 		The ID of the display for which to create the resources.
     * @param overrideConfig
     * 		The configuration to apply on top of the base configuration. Can be
     * 		null. Mostly used with Activities that are in multi-window which may override width and
     * 		height properties from the base config.
     * @param compatInfo
     * 		The compatibility settings to use. Cannot be null. A default to use is
     * 		{@link CompatibilityInfo#DEFAULT_COMPATIBILITY_INFO}.
     * @param classLoader
     * 		The class loader to use when inflating Resources. If null, the
     * 		{@link ClassLoader#getSystemClassLoader()} is used.
     * @return a Resources object from which to access resources.
     */
    @android.annotation.Nullable
    public android.content.res.Resources getResources(@android.annotation.Nullable
    android.os.IBinder activityToken, @android.annotation.Nullable
    java.lang.String resDir, @android.annotation.Nullable
    java.lang.String[] splitResDirs, @android.annotation.Nullable
    java.lang.String[] overlayDirs, @android.annotation.Nullable
    java.lang.String[] libDirs, int displayId, @android.annotation.Nullable
    android.content.res.Configuration overrideConfig, @android.annotation.NonNull
    android.content.res.CompatibilityInfo compatInfo, @android.annotation.Nullable
    java.lang.ClassLoader classLoader) {
        try {
            android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_RESOURCES, "ResourcesManager#getResources");
            final android.content.res.ResourcesKey key = // Copy
            new android.content.res.ResourcesKey(resDir, splitResDirs, overlayDirs, libDirs, displayId, overrideConfig != null ? new android.content.res.Configuration(overrideConfig) : null, compatInfo);
            classLoader = (classLoader != null) ? classLoader : java.lang.ClassLoader.getSystemClassLoader();
            return getOrCreateResources(activityToken, key, classLoader);
        } finally {
            android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_RESOURCES);
        }
    }

    /**
     * Updates an Activity's Resources object with overrideConfig. The Resources object
     * that was previously returned by
     * {@link #getResources(IBinder, String, String[], String[], String[], int, Configuration,
     * CompatibilityInfo, ClassLoader)} is
     * still valid and will have the updated configuration.
     *
     * @param activityToken
     * 		The Activity token.
     * @param overrideConfig
     * 		The configuration override to update.
     */
    public void updateResourcesForActivity(@android.annotation.NonNull
    android.os.IBinder activityToken, @android.annotation.Nullable
    android.content.res.Configuration overrideConfig) {
        try {
            android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_RESOURCES, "ResourcesManager#updateResourcesForActivity");
            synchronized(this) {
                final android.app.ResourcesManager.ActivityResources activityResources = getOrCreateActivityResourcesStructLocked(activityToken);
                if (java.util.Objects.equals(activityResources.overrideConfig, overrideConfig)) {
                    // They are the same, no work to do.
                    return;
                }
                // Grab a copy of the old configuration so we can create the delta's of each
                // Resources object associated with this Activity.
                final android.content.res.Configuration oldConfig = new android.content.res.Configuration(activityResources.overrideConfig);
                // Update the Activity's base override.
                if (overrideConfig != null) {
                    activityResources.overrideConfig.setTo(overrideConfig);
                } else {
                    activityResources.overrideConfig.setToDefaults();
                }
                if (android.app.ResourcesManager.DEBUG) {
                    java.lang.Throwable here = new java.lang.Throwable();
                    here.fillInStackTrace();
                    android.util.Slog.d(android.app.ResourcesManager.TAG, (((("updating resources override for activity=" + activityToken) + " from oldConfig=") + android.content.res.Configuration.resourceQualifierString(oldConfig)) + " to newConfig=") + android.content.res.Configuration.resourceQualifierString(activityResources.overrideConfig), here);
                }
                final boolean activityHasOverrideConfig = !activityResources.overrideConfig.equals(android.content.res.Configuration.EMPTY);
                // Rebase each Resources associated with this Activity.
                final int refCount = activityResources.activityResources.size();
                for (int i = 0; i < refCount; i++) {
                    java.lang.ref.WeakReference<android.content.res.Resources> weakResRef = activityResources.activityResources.get(i);
                    android.content.res.Resources resources = weakResRef.get();
                    if (resources == null) {
                        continue;
                    }
                    // Extract the ResourcesKey that was last used to create the Resources for this
                    // activity.
                    final android.content.res.ResourcesKey oldKey = findKeyForResourceImplLocked(resources.getImpl());
                    if (oldKey == null) {
                        android.util.Slog.e(android.app.ResourcesManager.TAG, "can't find ResourcesKey for resources impl=" + resources.getImpl());
                        continue;
                    }
                    // Build the new override configuration for this ResourcesKey.
                    final android.content.res.Configuration rebasedOverrideConfig = new android.content.res.Configuration();
                    if (overrideConfig != null) {
                        rebasedOverrideConfig.setTo(overrideConfig);
                    }
                    if (activityHasOverrideConfig && oldKey.hasOverrideConfiguration()) {
                        // Generate a delta between the old base Activity override configuration and
                        // the actual final override configuration that was used to figure out the
                        // real delta this Resources object wanted.
                        android.content.res.Configuration overrideOverrideConfig = android.content.res.Configuration.generateDelta(oldConfig, oldKey.mOverrideConfiguration);
                        rebasedOverrideConfig.updateFrom(overrideOverrideConfig);
                    }
                    // Create the new ResourcesKey with the rebased override config.
                    final android.content.res.ResourcesKey newKey = new android.content.res.ResourcesKey(oldKey.mResDir, oldKey.mSplitResDirs, oldKey.mOverlayDirs, oldKey.mLibDirs, oldKey.mDisplayId, rebasedOverrideConfig, oldKey.mCompatInfo);
                    if (android.app.ResourcesManager.DEBUG) {
                        android.util.Slog.d(android.app.ResourcesManager.TAG, (((("rebasing ref=" + resources) + " from oldKey=") + oldKey) + " to newKey=") + newKey);
                    }
                    android.content.res.ResourcesImpl resourcesImpl = findResourcesImplForKeyLocked(newKey);
                    if (resourcesImpl == null) {
                        resourcesImpl = createResourcesImpl(newKey);
                        if (resourcesImpl != null) {
                            mResourceImpls.put(newKey, new java.lang.ref.WeakReference<>(resourcesImpl));
                        }
                    }
                    if ((resourcesImpl != null) && (resourcesImpl != resources.getImpl())) {
                        // Set the ResourcesImpl, updating it for all users of this Resources
                        // object.
                        resources.setImpl(resourcesImpl);
                    }
                }
            }
        } finally {
            android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_RESOURCES);
        }
    }

    public final boolean applyConfigurationToResourcesLocked(@android.annotation.NonNull
    android.content.res.Configuration config, @android.annotation.Nullable
    android.content.res.CompatibilityInfo compat) {
        try {
            android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_RESOURCES, "ResourcesManager#applyConfigurationToResourcesLocked");
            if ((!mResConfiguration.isOtherSeqNewer(config)) && (compat == null)) {
                if (android.app.ResourcesManager.DEBUG || android.app.ActivityThread.DEBUG_CONFIGURATION)
                    android.util.Slog.v(android.app.ResourcesManager.TAG, (("Skipping new config: curSeq=" + mResConfiguration.seq) + ", newSeq=") + config.seq);

                return false;
            }
            int changes = mResConfiguration.updateFrom(config);
            // Things might have changed in display manager, so clear the cached displays.
            mDisplays.clear();
            android.util.DisplayMetrics defaultDisplayMetrics = getDisplayMetrics();
            if ((compat != null) && ((mResCompatibilityInfo == null) || (!mResCompatibilityInfo.equals(compat)))) {
                mResCompatibilityInfo = compat;
                changes |= (android.content.pm.ActivityInfo.CONFIG_SCREEN_LAYOUT | android.content.pm.ActivityInfo.CONFIG_SCREEN_SIZE) | android.content.pm.ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE;
            }
            android.content.res.Resources.updateSystemConfiguration(config, defaultDisplayMetrics, compat);
            android.app.ApplicationPackageManager.configurationChanged();
            // Slog.i(TAG, "Configuration changed in " + currentPackageName());
            android.content.res.Configuration tmpConfig = null;
            for (int i = mResourceImpls.size() - 1; i >= 0; i--) {
                android.content.res.ResourcesKey key = mResourceImpls.keyAt(i);
                android.content.res.ResourcesImpl r = mResourceImpls.valueAt(i).get();
                if (r != null) {
                    if (android.app.ResourcesManager.DEBUG || android.app.ActivityThread.DEBUG_CONFIGURATION)
                        android.util.Slog.v(android.app.ResourcesManager.TAG, (("Changing resources " + r) + " config to: ") + config);

                    int displayId = key.mDisplayId;
                    boolean isDefaultDisplay = displayId == android.view.Display.DEFAULT_DISPLAY;
                    android.util.DisplayMetrics dm = defaultDisplayMetrics;
                    final boolean hasOverrideConfiguration = key.hasOverrideConfiguration();
                    if ((!isDefaultDisplay) || hasOverrideConfiguration) {
                        if (tmpConfig == null) {
                            tmpConfig = new android.content.res.Configuration();
                        }
                        tmpConfig.setTo(config);
                        // Get new DisplayMetrics based on the DisplayAdjustments given
                        // to the ResourcesImpl. Update a copy if the CompatibilityInfo
                        // changed, because the ResourcesImpl object will handle the
                        // update internally.
                        android.view.DisplayAdjustments daj = r.getDisplayAdjustments();
                        if (compat != null) {
                            daj = new android.view.DisplayAdjustments(daj);
                            daj.setCompatibilityInfo(compat);
                        }
                        dm = getDisplayMetrics(displayId, daj);
                        if (!isDefaultDisplay) {
                            android.app.ResourcesManager.applyNonDefaultDisplayMetricsToConfiguration(dm, tmpConfig);
                        }
                        if (hasOverrideConfiguration) {
                            tmpConfig.updateFrom(key.mOverrideConfiguration);
                        }
                        r.updateConfiguration(tmpConfig, dm, compat);
                    } else {
                        r.updateConfiguration(config, dm, compat);
                    }
                    // Slog.i(TAG, "Updated app resources " + v.getKey()
                    // + " " + r + ": " + r.getConfiguration());
                } else {
                    // Slog.i(TAG, "Removing old resources " + v.getKey());
                    mResourceImpls.removeAt(i);
                }
            }
            return changes != 0;
        } finally {
            android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_RESOURCES);
        }
    }

    /**
     * Appends the library asset path to any ResourcesImpl object that contains the main
     * assetPath.
     *
     * @param assetPath
     * 		The main asset path for which to add the library asset path.
     * @param libAsset
     * 		The library asset path to add.
     */
    public void appendLibAssetForMainAssetPath(java.lang.String assetPath, java.lang.String libAsset) {
        synchronized(this) {
            // Record which ResourcesImpl need updating
            // (and what ResourcesKey they should update to).
            final android.util.ArrayMap<android.content.res.ResourcesImpl, android.content.res.ResourcesKey> updatedResourceKeys = new android.util.ArrayMap<>();
            final int implCount = mResourceImpls.size();
            for (int i = 0; i < implCount; i++) {
                final android.content.res.ResourcesImpl impl = mResourceImpls.valueAt(i).get();
                final android.content.res.ResourcesKey key = mResourceImpls.keyAt(i);
                if ((impl != null) && key.mResDir.equals(assetPath)) {
                    if (!com.android.internal.util.ArrayUtils.contains(key.mLibDirs, libAsset)) {
                        final int newLibAssetCount = 1 + (key.mLibDirs != null ? key.mLibDirs.length : 0);
                        final java.lang.String[] newLibAssets = new java.lang.String[newLibAssetCount];
                        if (key.mLibDirs != null) {
                            java.lang.System.arraycopy(key.mLibDirs, 0, newLibAssets, 0, key.mLibDirs.length);
                        }
                        newLibAssets[newLibAssetCount - 1] = libAsset;
                        updatedResourceKeys.put(impl, new android.content.res.ResourcesKey(key.mResDir, key.mSplitResDirs, key.mOverlayDirs, newLibAssets, key.mDisplayId, key.mOverrideConfiguration, key.mCompatInfo));
                    }
                }
            }
            // Bail early if there is no work to do.
            if (updatedResourceKeys.isEmpty()) {
                return;
            }
            // Update any references to ResourcesImpl that require reloading.
            final int resourcesCount = mResourceReferences.size();
            for (int i = 0; i < resourcesCount; i++) {
                final android.content.res.Resources r = mResourceReferences.get(i).get();
                if (r != null) {
                    final android.content.res.ResourcesKey key = updatedResourceKeys.get(r.getImpl());
                    if (key != null) {
                        final android.content.res.ResourcesImpl impl = findOrCreateResourcesImplForKeyLocked(key);
                        if (impl == null) {
                            throw new android.content.res.Resources.NotFoundException("failed to load " + libAsset);
                        }
                        r.setImpl(impl);
                    }
                }
            }
            // Update any references to ResourcesImpl that require reloading for each Activity.
            for (android.app.ResourcesManager.ActivityResources activityResources : mActivityResourceReferences.values()) {
                final int resCount = activityResources.activityResources.size();
                for (int i = 0; i < resCount; i++) {
                    final android.content.res.Resources r = activityResources.activityResources.get(i).get();
                    if (r != null) {
                        final android.content.res.ResourcesKey key = updatedResourceKeys.get(r.getImpl());
                        if (key != null) {
                            final android.content.res.ResourcesImpl impl = findOrCreateResourcesImplForKeyLocked(key);
                            if (impl == null) {
                                throw new android.content.res.Resources.NotFoundException("failed to load " + libAsset);
                            }
                            r.setImpl(impl);
                        }
                    }
                }
            }
        }
    }
}

