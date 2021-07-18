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
package android.content.res;


/**
 * Provides access to an application's raw asset files; see {@link Resources}
 * for the way most applications will want to retrieve their resource data.
 * This class presents a lower-level API that allows you to open and read raw
 * files that have been bundled with the application as a simple stream of
 * bytes.
 */
public final class AssetManager implements java.lang.AutoCloseable {
    private static final java.lang.String TAG = "AssetManager";

    private static final boolean DEBUG_REFS = false;

    private static final boolean FEATURE_FLAG_IDMAP2 = true;

    private static final java.lang.String FRAMEWORK_APK_PATH = "/system/framework/framework-res.apk";

    private static final java.lang.Object sSync = new java.lang.Object();

    private static final android.content.res.ApkAssets[] sEmptyApkAssets = new android.content.res.ApkAssets[0];

    // Not private for LayoutLib's BridgeAssetManager.
    @android.annotation.UnsupportedAppUsage
    @com.android.internal.annotations.GuardedBy("sSync")
    static android.content.res.AssetManager sSystem = null;

    @com.android.internal.annotations.GuardedBy("sSync")
    private static android.content.res.ApkAssets[] sSystemApkAssets = new android.content.res.ApkAssets[0];

    @com.android.internal.annotations.GuardedBy("sSync")
    private static android.util.ArraySet<android.content.res.ApkAssets> sSystemApkAssetsSet;

    /**
     * Mode for {@link #open(String, int)}: no specific information about how
     * data will be accessed.
     */
    public static final int ACCESS_UNKNOWN = 0;

    /**
     * Mode for {@link #open(String, int)}: Read chunks, and seek forward and
     * backward.
     */
    public static final int ACCESS_RANDOM = 1;

    /**
     * Mode for {@link #open(String, int)}: Read sequentially, with an
     * occasional forward seek.
     */
    public static final int ACCESS_STREAMING = 2;

    /**
     * Mode for {@link #open(String, int)}: Attempt to load contents into
     * memory, for fast small reads.
     */
    public static final int ACCESS_BUFFER = 3;

    @com.android.internal.annotations.GuardedBy("this")
    private final android.util.TypedValue mValue = new android.util.TypedValue();

    @com.android.internal.annotations.GuardedBy("this")
    private final long[] mOffsets = new long[2];

    // Pointer to native implementation, stuffed inside a long.
    @android.annotation.UnsupportedAppUsage
    @com.android.internal.annotations.GuardedBy("this")
    private long mObject;

    // The loaded asset paths.
    @com.android.internal.annotations.GuardedBy("this")
    private android.content.res.ApkAssets[] mApkAssets;

    // Debug/reference counting implementation.
    @com.android.internal.annotations.GuardedBy("this")
    private boolean mOpen = true;

    @com.android.internal.annotations.GuardedBy("this")
    private int mNumRefs = 1;

    @com.android.internal.annotations.GuardedBy("this")
    private java.util.HashMap<java.lang.Long, java.lang.RuntimeException> mRefStacks;

    /**
     * A Builder class that helps create an AssetManager with only a single invocation of
     * {@link AssetManager#setApkAssets(ApkAssets[], boolean)}. Without using this builder,
     * AssetManager must ensure there are system ApkAssets loaded at all times, which when combined
     * with the user's call to add additional ApkAssets, results in multiple calls to
     * {@link AssetManager#setApkAssets(ApkAssets[], boolean)}.
     *
     * @unknown 
     */
    public static class Builder {
        private java.util.ArrayList<android.content.res.ApkAssets> mUserApkAssets = new java.util.ArrayList<>();

        public android.content.res.AssetManager.Builder addApkAssets(android.content.res.ApkAssets apkAssets) {
            mUserApkAssets.add(apkAssets);
            return this;
        }

        public android.content.res.AssetManager build() {
            // Retrieving the system ApkAssets forces their creation as well.
            final android.content.res.ApkAssets[] systemApkAssets = android.content.res.AssetManager.getSystem().getApkAssets();
            final int totalApkAssetCount = systemApkAssets.length + mUserApkAssets.size();
            final android.content.res.ApkAssets[] apkAssets = new android.content.res.ApkAssets[totalApkAssetCount];
            java.lang.System.arraycopy(systemApkAssets, 0, apkAssets, 0, systemApkAssets.length);
            final int userApkAssetCount = mUserApkAssets.size();
            for (int i = 0; i < userApkAssetCount; i++) {
                apkAssets[i + systemApkAssets.length] = mUserApkAssets.get(i);
            }
            // Calling this constructor prevents creation of system ApkAssets, which we took care
            // of in this Builder.
            final android.content.res.AssetManager assetManager = /* sentinel */
            new android.content.res.AssetManager(false);
            assetManager.mApkAssets = apkAssets;
            /* invalidateCaches */
            android.content.res.AssetManager.nativeSetApkAssets(assetManager.mObject, apkAssets, false);
            return assetManager;
        }
    }

    /**
     * Create a new AssetManager containing only the basic system assets.
     * Applications will not generally use this method, instead retrieving the
     * appropriate asset manager with {@link Resources#getAssets}.    Not for
     * use by applications.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public AssetManager() {
        final android.content.res.ApkAssets[] assets;
        synchronized(android.content.res.AssetManager.sSync) {
            android.content.res.AssetManager.createSystemAssetsInZygoteLocked();
            assets = android.content.res.AssetManager.sSystemApkAssets;
        }
        mObject = android.content.res.AssetManager.nativeCreate();
        if (android.content.res.AssetManager.DEBUG_REFS) {
            mNumRefs = 0;
            incRefsLocked(hashCode());
        }
        // Always set the framework resources.
        /* invalidateCaches */
        setApkAssets(assets, false);
    }

    /**
     * Private constructor that doesn't call ensureSystemAssets.
     * Used for the creation of system assets.
     */
    @java.lang.SuppressWarnings("unused")
    private AssetManager(boolean sentinel) {
        mObject = android.content.res.AssetManager.nativeCreate();
        if (android.content.res.AssetManager.DEBUG_REFS) {
            mNumRefs = 0;
            incRefsLocked(hashCode());
        }
    }

    /**
     * This must be called from Zygote so that system assets are shared by all applications.
     */
    @com.android.internal.annotations.GuardedBy("sSync")
    private static void createSystemAssetsInZygoteLocked() {
        if (android.content.res.AssetManager.sSystem != null) {
            return;
        }
        try {
            final java.util.ArrayList<android.content.res.ApkAssets> apkAssets = new java.util.ArrayList<>();
            apkAssets.add(/* system */
            android.content.res.ApkAssets.loadFromPath(android.content.res.AssetManager.FRAMEWORK_APK_PATH, true));
            if (android.content.res.AssetManager.FEATURE_FLAG_IDMAP2) {
                final java.lang.String[] systemIdmapPaths = android.content.res.AssetManager.nativeCreateIdmapsForStaticOverlaysTargetingAndroid();
                if (systemIdmapPaths != null) {
                    for (java.lang.String idmapPath : systemIdmapPaths) {
                        apkAssets.add(/* system */
                        android.content.res.ApkAssets.loadOverlayFromPath(idmapPath, true));
                    }
                } else {
                    android.util.Log.w(android.content.res.AssetManager.TAG, "\'idmap2 --scan\' failed: no static=\"true\" overlays targeting " + "\"android\" will be loaded");
                }
            } else {
                android.content.res.AssetManager.nativeVerifySystemIdmaps();
                android.content.res.AssetManager.loadStaticRuntimeOverlays(apkAssets);
            }
            android.content.res.AssetManager.sSystemApkAssetsSet = new android.util.ArraySet(apkAssets);
            android.content.res.AssetManager.sSystemApkAssets = apkAssets.toArray(new android.content.res.ApkAssets[apkAssets.size()]);
            android.content.res.AssetManager.sSystem = /* sentinel */
            new android.content.res.AssetManager(true);
            /* invalidateCaches */
            android.content.res.AssetManager.sSystem.setApkAssets(android.content.res.AssetManager.sSystemApkAssets, false);
        } catch (java.io.IOException e) {
            throw new java.lang.IllegalStateException("Failed to create system AssetManager", e);
        }
    }

    /**
     * Loads the static runtime overlays declared in /data/resource-cache/overlays.list.
     * Throws an exception if the file is corrupt or if loading the APKs referenced by the file
     * fails. Returns quietly if the overlays.list file doesn't exist.
     *
     * @param outApkAssets
     * 		The list to fill with the loaded ApkAssets.
     */
    private static void loadStaticRuntimeOverlays(java.util.ArrayList<android.content.res.ApkAssets> outApkAssets) throws java.io.IOException {
        final java.io.FileInputStream fis;
        try {
            fis = new java.io.FileInputStream("/data/resource-cache/overlays.list");
        } catch (java.io.FileNotFoundException e) {
            // We might not have any overlays, this is fine. We catch here since ApkAssets
            // loading can also fail with the same exception, which we would want to propagate.
            android.util.Log.i(android.content.res.AssetManager.TAG, "no overlays.list file found");
            return;
        }
        try {
            // Acquire a lock so that any idmap scanning doesn't impact the current set.
            // The order of this try-with-resources block matters. We must release the lock, and
            // then close the file streams when exiting the block.
            try (final java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(fis));final java.nio.channels.FileLock flock = /* shared */
            fis.getChannel().lock(0, java.lang.Long.MAX_VALUE, true)) {
                for (java.lang.String line; (line = br.readLine()) != null;) {
                    final java.lang.String idmapPath = line.split(" ")[1];
                    outApkAssets.add(/* system */
                    android.content.res.ApkAssets.loadOverlayFromPath(idmapPath, true));
                }
            }
        } finally {
            // When BufferedReader is closed above, FileInputStream is closed as well. But let's be
            // paranoid.
            libcore.io.IoUtils.closeQuietly(fis);
        }
    }

    /**
     * Return a global shared asset manager that provides access to only
     * system assets (no application assets).
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static android.content.res.AssetManager getSystem() {
        synchronized(android.content.res.AssetManager.sSync) {
            android.content.res.AssetManager.createSystemAssetsInZygoteLocked();
            return android.content.res.AssetManager.sSystem;
        }
    }

    /**
     * Close this asset manager.
     */
    @java.lang.Override
    public void close() {
        synchronized(this) {
            if (!mOpen) {
                return;
            }
            mOpen = false;
            decRefsLocked(hashCode());
        }
    }

    /**
     * Changes the asset paths in this AssetManager. This replaces the {@link #addAssetPath(String)}
     * family of methods.
     *
     * @param apkAssets
     * 		The new set of paths.
     * @param invalidateCaches
     * 		Whether to invalidate any caches. This should almost always be true.
     * 		Set this to false if you are appending new resources
     * 		(not new configurations).
     * @unknown 
     */
    public void setApkAssets(@android.annotation.NonNull
    android.content.res.ApkAssets[] apkAssets, boolean invalidateCaches) {
        com.android.internal.util.Preconditions.checkNotNull(apkAssets, "apkAssets");
        android.content.res.ApkAssets[] newApkAssets = new android.content.res.ApkAssets[android.content.res.AssetManager.sSystemApkAssets.length + apkAssets.length];
        // Copy the system assets first.
        java.lang.System.arraycopy(android.content.res.AssetManager.sSystemApkAssets, 0, newApkAssets, 0, android.content.res.AssetManager.sSystemApkAssets.length);
        // Copy the given ApkAssets if they are not already in the system list.
        int newLength = android.content.res.AssetManager.sSystemApkAssets.length;
        for (android.content.res.ApkAssets apkAsset : apkAssets) {
            if (!android.content.res.AssetManager.sSystemApkAssetsSet.contains(apkAsset)) {
                newApkAssets[newLength++] = apkAsset;
            }
        }
        // Truncate if necessary.
        if (newLength != newApkAssets.length) {
            newApkAssets = java.util.Arrays.copyOf(newApkAssets, newLength);
        }
        synchronized(this) {
            ensureOpenLocked();
            mApkAssets = newApkAssets;
            android.content.res.AssetManager.nativeSetApkAssets(mObject, mApkAssets, invalidateCaches);
            if (invalidateCaches) {
                // Invalidate all caches.
                invalidateCachesLocked(-1);
            }
        }
    }

    /**
     * Invalidates the caches in this AssetManager according to the bitmask `diff`.
     *
     * @param diff
     * 		The bitmask of changes generated by {@link Configuration#diff(Configuration)}.
     * @see ActivityInfo.Config
     */
    private void invalidateCachesLocked(int diff) {
        // TODO(adamlesinski): Currently there are no caches to invalidate in Java code.
    }

    /**
     * Returns the set of ApkAssets loaded by this AssetManager. If the AssetManager is closed, this
     * returns a 0-length array.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    @android.annotation.NonNull
    public android.content.res.ApkAssets[] getApkAssets() {
        synchronized(this) {
            if (mOpen) {
                return mApkAssets;
            }
        }
        return android.content.res.AssetManager.sEmptyApkAssets;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    @android.annotation.NonNull
    public java.lang.String[] getApkPaths() {
        synchronized(this) {
            if (mOpen) {
                java.lang.String[] paths = new java.lang.String[mApkAssets.length];
                final int count = mApkAssets.length;
                for (int i = 0; i < count; i++) {
                    paths[i] = mApkAssets[i].getAssetPath();
                }
                return paths;
            }
        }
        return new java.lang.String[0];
    }

    /**
     * Returns a cookie for use with the other APIs of AssetManager.
     *
     * @return 0 if the path was not found, otherwise a positive integer cookie representing
    this path in the AssetManager.
     * @unknown 
     */
    public int findCookieForPath(@android.annotation.NonNull
    java.lang.String path) {
        com.android.internal.util.Preconditions.checkNotNull(path, "path");
        synchronized(this) {
            ensureValidLocked();
            final int count = mApkAssets.length;
            for (int i = 0; i < count; i++) {
                if (path.equals(mApkAssets[i].getAssetPath())) {
                    return i + 1;
                }
            }
        }
        return 0;
    }

    /**
     *
     *
     * @deprecated Use {@link #setApkAssets(ApkAssets[], boolean)}
     * @unknown 
     */
    @java.lang.Deprecated
    @android.annotation.UnsupportedAppUsage
    public int addAssetPath(java.lang.String path) {
        return /* overlay */
        /* appAsLib */
        addAssetPathInternal(path, false, false);
    }

    /**
     *
     *
     * @deprecated Use {@link #setApkAssets(ApkAssets[], boolean)}
     * @unknown 
     */
    @java.lang.Deprecated
    @android.annotation.UnsupportedAppUsage
    public int addAssetPathAsSharedLibrary(java.lang.String path) {
        return /* overlay */
        /* appAsLib */
        addAssetPathInternal(path, false, true);
    }

    /**
     *
     *
     * @deprecated Use {@link #setApkAssets(ApkAssets[], boolean)}
     * @unknown 
     */
    @java.lang.Deprecated
    @android.annotation.UnsupportedAppUsage
    public int addOverlayPath(java.lang.String path) {
        return /* overlay */
        /* appAsLib */
        addAssetPathInternal(path, true, false);
    }

    private int addAssetPathInternal(java.lang.String path, boolean overlay, boolean appAsLib) {
        com.android.internal.util.Preconditions.checkNotNull(path, "path");
        synchronized(this) {
            ensureOpenLocked();
            final int count = mApkAssets.length;
            // See if we already have it loaded.
            for (int i = 0; i < count; i++) {
                if (mApkAssets[i].getAssetPath().equals(path)) {
                    return i + 1;
                }
            }
            final android.content.res.ApkAssets assets;
            try {
                if (overlay) {
                    // TODO(b/70343104): This hardcoded path will be removed once
                    // addAssetPathInternal is deleted.
                    final java.lang.String idmapPath = ("/data/resource-cache/" + path.substring(1).replace('/', '@')) + "@idmap";
                    assets = /* system */
                    android.content.res.ApkAssets.loadOverlayFromPath(idmapPath, false);
                } else {
                    assets = /* system */
                    android.content.res.ApkAssets.loadFromPath(path, false, appAsLib);
                }
            } catch (java.io.IOException e) {
                return 0;
            }
            mApkAssets = java.util.Arrays.copyOf(mApkAssets, count + 1);
            mApkAssets[count] = assets;
            android.content.res.AssetManager.nativeSetApkAssets(mObject, mApkAssets, true);
            invalidateCachesLocked(-1);
            return count + 1;
        }
    }

    /**
     * Ensures that the native implementation has not been destroyed.
     * The AssetManager may have been closed, but references to it still exist
     * and therefore the native implementation is not destroyed.
     */
    @com.android.internal.annotations.GuardedBy("this")
    private void ensureValidLocked() {
        if (mObject == 0) {
            throw new java.lang.RuntimeException("AssetManager has been destroyed");
        }
    }

    /**
     * Ensures that the AssetManager has not been explicitly closed. If this method passes,
     * then this implies that ensureValidLocked() also passes.
     */
    @com.android.internal.annotations.GuardedBy("this")
    private void ensureOpenLocked() {
        // If mOpen is true, this implies that mObject != 0.
        if (!mOpen) {
            throw new java.lang.RuntimeException("AssetManager has been closed");
        }
    }

    /**
     * Populates {@code outValue} with the data associated a particular
     * resource identifier for the current configuration.
     *
     * @param resId
     * 		the resource identifier to load
     * @param densityDpi
     * 		the density bucket for which to load the resource
     * @param outValue
     * 		the typed value in which to put the data
     * @param resolveRefs
     * 		{@code true} to resolve references, {@code false}
     * 		to leave them unresolved
     * @return {@code true} if the data was loaded into {@code outValue},
    {@code false} otherwise
     */
    @android.annotation.UnsupportedAppUsage
    boolean getResourceValue(@android.annotation.AnyRes
    int resId, int densityDpi, @android.annotation.NonNull
    android.util.TypedValue outValue, boolean resolveRefs) {
        com.android.internal.util.Preconditions.checkNotNull(outValue, "outValue");
        synchronized(this) {
            ensureValidLocked();
            final int cookie = android.content.res.AssetManager.nativeGetResourceValue(mObject, resId, ((short) (densityDpi)), outValue, resolveRefs);
            if (cookie <= 0) {
                return false;
            }
            // Convert the changing configurations flags populated by native code.
            outValue.changingConfigurations = android.content.pm.ActivityInfo.activityInfoConfigNativeToJava(outValue.changingConfigurations);
            if (outValue.type == android.util.TypedValue.TYPE_STRING) {
                outValue.string = mApkAssets[cookie - 1].getStringFromPool(outValue.data);
            }
            return true;
        }
    }

    /**
     * Retrieves the string value associated with a particular resource
     * identifier for the current configuration.
     *
     * @param resId
     * 		the resource identifier to load
     * @return the string value, or {@code null}
     */
    @android.annotation.UnsupportedAppUsage
    @android.annotation.Nullable
    java.lang.CharSequence getResourceText(@android.annotation.StringRes
    int resId) {
        synchronized(this) {
            final android.util.TypedValue outValue = mValue;
            if (getResourceValue(resId, 0, outValue, true)) {
                return outValue.coerceToString();
            }
            return null;
        }
    }

    /**
     * Retrieves the string value associated with a particular resource
     * identifier for the current configuration.
     *
     * @param resId
     * 		the resource identifier to load
     * @param bagEntryId
     * 		the index into the bag to load
     * @return the string value, or {@code null}
     */
    @android.annotation.UnsupportedAppUsage
    @android.annotation.Nullable
    java.lang.CharSequence getResourceBagText(@android.annotation.StringRes
    int resId, int bagEntryId) {
        synchronized(this) {
            ensureValidLocked();
            final android.util.TypedValue outValue = mValue;
            final int cookie = android.content.res.AssetManager.nativeGetResourceBagValue(mObject, resId, bagEntryId, outValue);
            if (cookie <= 0) {
                return null;
            }
            // Convert the changing configurations flags populated by native code.
            outValue.changingConfigurations = android.content.pm.ActivityInfo.activityInfoConfigNativeToJava(outValue.changingConfigurations);
            if (outValue.type == android.util.TypedValue.TYPE_STRING) {
                return mApkAssets[cookie - 1].getStringFromPool(outValue.data);
            }
            return outValue.coerceToString();
        }
    }

    int getResourceArraySize(@android.annotation.ArrayRes
    int resId) {
        synchronized(this) {
            ensureValidLocked();
            return android.content.res.AssetManager.nativeGetResourceArraySize(mObject, resId);
        }
    }

    /**
     * Populates `outData` with array elements of `resId`. `outData` is normally
     * used with
     * {@link TypedArray}.
     *
     * Each logical element in `outData` is {@link TypedArray#STYLE_NUM_ENTRIES}
     * long,
     * with the indices of the data representing the type, value, asset cookie,
     * resource ID,
     * configuration change mask, and density of the element.
     *
     * @param resId
     * 		The resource ID of an array resource.
     * @param outData
     * 		The array to populate with data.
     * @return The length of the array.
     * @see TypedArray#STYLE_TYPE
     * @see TypedArray#STYLE_DATA
     * @see TypedArray#STYLE_ASSET_COOKIE
     * @see TypedArray#STYLE_RESOURCE_ID
     * @see TypedArray#STYLE_CHANGING_CONFIGURATIONS
     * @see TypedArray#STYLE_DENSITY
     */
    int getResourceArray(@android.annotation.ArrayRes
    int resId, @android.annotation.NonNull
    int[] outData) {
        com.android.internal.util.Preconditions.checkNotNull(outData, "outData");
        synchronized(this) {
            ensureValidLocked();
            return android.content.res.AssetManager.nativeGetResourceArray(mObject, resId, outData);
        }
    }

    /**
     * Retrieves the string array associated with a particular resource
     * identifier for the current configuration.
     *
     * @param resId
     * 		the resource identifier of the string array
     * @return the string array, or {@code null}
     */
    @android.annotation.Nullable
    java.lang.String[] getResourceStringArray(@android.annotation.ArrayRes
    int resId) {
        synchronized(this) {
            ensureValidLocked();
            return android.content.res.AssetManager.nativeGetResourceStringArray(mObject, resId);
        }
    }

    /**
     * Retrieve the text array associated with a particular resource
     * identifier.
     *
     * @param resId
     * 		the resource id of the string array
     */
    @android.annotation.Nullable
    java.lang.CharSequence[] getResourceTextArray(@android.annotation.ArrayRes
    int resId) {
        synchronized(this) {
            ensureValidLocked();
            final int[] rawInfoArray = android.content.res.AssetManager.nativeGetResourceStringArrayInfo(mObject, resId);
            if (rawInfoArray == null) {
                return null;
            }
            final int rawInfoArrayLen = rawInfoArray.length;
            final int infoArrayLen = rawInfoArrayLen / 2;
            final java.lang.CharSequence[] retArray = new java.lang.CharSequence[infoArrayLen];
            for (int i = 0, j = 0; i < rawInfoArrayLen; i = i + 2 , j++) {
                int cookie = rawInfoArray[i];
                int index = rawInfoArray[i + 1];
                retArray[j] = ((index >= 0) && (cookie > 0)) ? mApkAssets[cookie - 1].getStringFromPool(index) : null;
            }
            return retArray;
        }
    }

    @android.annotation.Nullable
    int[] getResourceIntArray(@android.annotation.ArrayRes
    int resId) {
        synchronized(this) {
            ensureValidLocked();
            return android.content.res.AssetManager.nativeGetResourceIntArray(mObject, resId);
        }
    }

    /**
     * Get the attributes for a style resource. These are the &lt;item&gt;
     * elements in
     * a &lt;style&gt; resource.
     *
     * @param resId
     * 		The resource ID of the style
     * @return An array of attribute IDs.
     */
    @android.annotation.AttrRes
    int[] getStyleAttributes(@android.annotation.StyleRes
    int resId) {
        synchronized(this) {
            ensureValidLocked();
            return android.content.res.AssetManager.nativeGetStyleAttributes(mObject, resId);
        }
    }

    /**
     * Populates {@code outValue} with the data associated with a particular
     * resource identifier for the current configuration. Resolves theme
     * attributes against the specified theme.
     *
     * @param theme
     * 		the native pointer of the theme
     * @param resId
     * 		the resource identifier to load
     * @param outValue
     * 		the typed value in which to put the data
     * @param resolveRefs
     * 		{@code true} to resolve references, {@code false}
     * 		to leave them unresolved
     * @return {@code true} if the data was loaded into {@code outValue},
    {@code false} otherwise
     */
    boolean getThemeValue(long theme, @android.annotation.AnyRes
    int resId, @android.annotation.NonNull
    android.util.TypedValue outValue, boolean resolveRefs) {
        com.android.internal.util.Preconditions.checkNotNull(outValue, "outValue");
        synchronized(this) {
            ensureValidLocked();
            final int cookie = android.content.res.AssetManager.nativeThemeGetAttributeValue(mObject, theme, resId, outValue, resolveRefs);
            if (cookie <= 0) {
                return false;
            }
            // Convert the changing configurations flags populated by native code.
            outValue.changingConfigurations = android.content.pm.ActivityInfo.activityInfoConfigNativeToJava(outValue.changingConfigurations);
            if (outValue.type == android.util.TypedValue.TYPE_STRING) {
                outValue.string = mApkAssets[cookie - 1].getStringFromPool(outValue.data);
            }
            return true;
        }
    }

    void dumpTheme(long theme, int priority, java.lang.String tag, java.lang.String prefix) {
        synchronized(this) {
            ensureValidLocked();
            android.content.res.AssetManager.nativeThemeDump(mObject, theme, priority, tag, prefix);
        }
    }

    @android.annotation.UnsupportedAppUsage
    @android.annotation.Nullable
    java.lang.String getResourceName(@android.annotation.AnyRes
    int resId) {
        synchronized(this) {
            ensureValidLocked();
            return android.content.res.AssetManager.nativeGetResourceName(mObject, resId);
        }
    }

    @android.annotation.UnsupportedAppUsage
    @android.annotation.Nullable
    java.lang.String getResourcePackageName(@android.annotation.AnyRes
    int resId) {
        synchronized(this) {
            ensureValidLocked();
            return android.content.res.AssetManager.nativeGetResourcePackageName(mObject, resId);
        }
    }

    @android.annotation.UnsupportedAppUsage
    @android.annotation.Nullable
    java.lang.String getResourceTypeName(@android.annotation.AnyRes
    int resId) {
        synchronized(this) {
            ensureValidLocked();
            return android.content.res.AssetManager.nativeGetResourceTypeName(mObject, resId);
        }
    }

    @android.annotation.UnsupportedAppUsage
    @android.annotation.Nullable
    java.lang.String getResourceEntryName(@android.annotation.AnyRes
    int resId) {
        synchronized(this) {
            ensureValidLocked();
            return android.content.res.AssetManager.nativeGetResourceEntryName(mObject, resId);
        }
    }

    @android.annotation.UnsupportedAppUsage
    @android.annotation.AnyRes
    int getResourceIdentifier(@android.annotation.NonNull
    java.lang.String name, @android.annotation.Nullable
    java.lang.String defType, @android.annotation.Nullable
    java.lang.String defPackage) {
        synchronized(this) {
            ensureValidLocked();
            // name is checked in JNI.
            return android.content.res.AssetManager.nativeGetResourceIdentifier(mObject, name, defType, defPackage);
        }
    }

    /**
     * Enable resource resolution logging to track the steps taken to resolve the last resource
     * entry retrieved. Stores the configuration and package names for each step.
     *
     * Default disabled.
     *
     * @param enabled
     * 		Boolean indicating whether to enable or disable logging.
     * @unknown 
     */
    public void setResourceResolutionLoggingEnabled(boolean enabled) {
        synchronized(this) {
            ensureValidLocked();
            android.content.res.AssetManager.nativeSetResourceResolutionLoggingEnabled(mObject, enabled);
        }
    }

    /**
     * Retrieve the last resource resolution path logged.
     *
     * @return Formatted string containing last resource ID/name and steps taken to resolve final
    entry, including configuration and package names.
     * @unknown 
     */
    @android.annotation.Nullable
    public java.lang.String getLastResourceResolution() {
        synchronized(this) {
            ensureValidLocked();
            return android.content.res.AssetManager.nativeGetLastResourceResolution(mObject);
        }
    }

    java.lang.CharSequence getPooledStringForCookie(int cookie, int id) {
        // Cookies map to ApkAssets starting at 1.
        return getApkAssets()[cookie - 1].getStringFromPool(id);
    }

    /**
     * Open an asset using ACCESS_STREAMING mode.  This provides access to
     * files that have been bundled with an application as assets -- that is,
     * files placed in to the "assets" directory.
     *
     * @param fileName
     * 		The name of the asset to open.  This name can be hierarchical.
     * @see #open(String, int)
     * @see #list
     */
    @android.annotation.NonNull
    public java.io.InputStream open(@android.annotation.NonNull
    java.lang.String fileName) throws java.io.IOException {
        return open(fileName, android.content.res.AssetManager.ACCESS_STREAMING);
    }

    /**
     * Open an asset using an explicit access mode, returning an InputStream to
     * read its contents.  This provides access to files that have been bundled
     * with an application as assets -- that is, files placed in to the
     * "assets" directory.
     *
     * @param fileName
     * 		The name of the asset to open.  This name can be hierarchical.
     * @param accessMode
     * 		Desired access mode for retrieving the data.
     * @see #ACCESS_UNKNOWN
     * @see #ACCESS_STREAMING
     * @see #ACCESS_RANDOM
     * @see #ACCESS_BUFFER
     * @see #open(String)
     * @see #list
     */
    @android.annotation.NonNull
    public java.io.InputStream open(@android.annotation.NonNull
    java.lang.String fileName, int accessMode) throws java.io.IOException {
        com.android.internal.util.Preconditions.checkNotNull(fileName, "fileName");
        synchronized(this) {
            ensureOpenLocked();
            final long asset = android.content.res.AssetManager.nativeOpenAsset(mObject, fileName, accessMode);
            if (asset == 0) {
                throw new java.io.FileNotFoundException("Asset file: " + fileName);
            }
            final android.content.res.AssetManager.AssetInputStream assetInputStream = new android.content.res.AssetManager.AssetInputStream(asset);
            incRefsLocked(assetInputStream.hashCode());
            return assetInputStream;
        }
    }

    /**
     * Open an uncompressed asset by mmapping it and returning an {@link AssetFileDescriptor}.
     * This provides access to files that have been bundled with an application as assets -- that
     * is, files placed in to the "assets" directory.
     *
     * The asset must be uncompressed, or an exception will be thrown.
     *
     * @param fileName
     * 		The name of the asset to open.  This name can be hierarchical.
     * @return An open AssetFileDescriptor.
     */
    @android.annotation.NonNull
    public android.content.res.AssetFileDescriptor openFd(@android.annotation.NonNull
    java.lang.String fileName) throws java.io.IOException {
        com.android.internal.util.Preconditions.checkNotNull(fileName, "fileName");
        synchronized(this) {
            ensureOpenLocked();
            final android.os.ParcelFileDescriptor pfd = android.content.res.AssetManager.nativeOpenAssetFd(mObject, fileName, mOffsets);
            if (pfd == null) {
                throw new java.io.FileNotFoundException("Asset file: " + fileName);
            }
            return new android.content.res.AssetFileDescriptor(pfd, mOffsets[0], mOffsets[1]);
        }
    }

    /**
     * Return a String array of all the assets at the given path.
     *
     * @param path
     * 		A relative path within the assets, i.e., "docs/home.html".
     * @return String[] Array of strings, one for each asset.  These file
    names are relative to 'path'.  You can open the file by
    concatenating 'path' and a name in the returned string (via
    File) and passing that to open().
     * @see #open
     */
    @android.annotation.Nullable
    public java.lang.String[] list(@android.annotation.NonNull
    java.lang.String path) throws java.io.IOException {
        com.android.internal.util.Preconditions.checkNotNull(path, "path");
        synchronized(this) {
            ensureValidLocked();
            return android.content.res.AssetManager.nativeList(mObject, path);
        }
    }

    /**
     * Open a non-asset file as an asset using ACCESS_STREAMING mode.  This
     * provides direct access to all of the files included in an application
     * package (not only its assets).  Applications should not normally use
     * this.
     *
     * @param fileName
     * 		Name of the asset to retrieve.
     * @see #open(String)
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    @android.annotation.NonNull
    public java.io.InputStream openNonAsset(@android.annotation.NonNull
    java.lang.String fileName) throws java.io.IOException {
        return openNonAsset(0, fileName, android.content.res.AssetManager.ACCESS_STREAMING);
    }

    /**
     * Open a non-asset file as an asset using a specific access mode.  This
     * provides direct access to all of the files included in an application
     * package (not only its assets).  Applications should not normally use
     * this.
     *
     * @param fileName
     * 		Name of the asset to retrieve.
     * @param accessMode
     * 		Desired access mode for retrieving the data.
     * @see #ACCESS_UNKNOWN
     * @see #ACCESS_STREAMING
     * @see #ACCESS_RANDOM
     * @see #ACCESS_BUFFER
     * @see #open(String, int)
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    @android.annotation.NonNull
    public java.io.InputStream openNonAsset(@android.annotation.NonNull
    java.lang.String fileName, int accessMode) throws java.io.IOException {
        return openNonAsset(0, fileName, accessMode);
    }

    /**
     * Open a non-asset in a specified package.  Not for use by applications.
     *
     * @param cookie
     * 		Identifier of the package to be opened.
     * @param fileName
     * 		Name of the asset to retrieve.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    @android.annotation.NonNull
    public java.io.InputStream openNonAsset(int cookie, @android.annotation.NonNull
    java.lang.String fileName) throws java.io.IOException {
        return openNonAsset(cookie, fileName, android.content.res.AssetManager.ACCESS_STREAMING);
    }

    /**
     * Open a non-asset in a specified package.  Not for use by applications.
     *
     * @param cookie
     * 		Identifier of the package to be opened.
     * @param fileName
     * 		Name of the asset to retrieve.
     * @param accessMode
     * 		Desired access mode for retrieving the data.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    @android.annotation.NonNull
    public java.io.InputStream openNonAsset(int cookie, @android.annotation.NonNull
    java.lang.String fileName, int accessMode) throws java.io.IOException {
        com.android.internal.util.Preconditions.checkNotNull(fileName, "fileName");
        synchronized(this) {
            ensureOpenLocked();
            final long asset = android.content.res.AssetManager.nativeOpenNonAsset(mObject, cookie, fileName, accessMode);
            if (asset == 0) {
                throw new java.io.FileNotFoundException("Asset absolute file: " + fileName);
            }
            final android.content.res.AssetManager.AssetInputStream assetInputStream = new android.content.res.AssetManager.AssetInputStream(asset);
            incRefsLocked(assetInputStream.hashCode());
            return assetInputStream;
        }
    }

    /**
     * Open a non-asset as an asset by mmapping it and returning an {@link AssetFileDescriptor}.
     * This provides direct access to all of the files included in an application
     * package (not only its assets).  Applications should not normally use this.
     *
     * The asset must not be compressed, or an exception will be thrown.
     *
     * @param fileName
     * 		Name of the asset to retrieve.
     */
    @android.annotation.NonNull
    public android.content.res.AssetFileDescriptor openNonAssetFd(@android.annotation.NonNull
    java.lang.String fileName) throws java.io.IOException {
        return openNonAssetFd(0, fileName);
    }

    /**
     * Open a non-asset as an asset by mmapping it and returning an {@link AssetFileDescriptor}.
     * This provides direct access to all of the files included in an application
     * package (not only its assets).  Applications should not normally use this.
     *
     * The asset must not be compressed, or an exception will be thrown.
     *
     * @param cookie
     * 		Identifier of the package to be opened.
     * @param fileName
     * 		Name of the asset to retrieve.
     */
    @android.annotation.NonNull
    public android.content.res.AssetFileDescriptor openNonAssetFd(int cookie, @android.annotation.NonNull
    java.lang.String fileName) throws java.io.IOException {
        com.android.internal.util.Preconditions.checkNotNull(fileName, "fileName");
        synchronized(this) {
            ensureOpenLocked();
            final android.os.ParcelFileDescriptor pfd = android.content.res.AssetManager.nativeOpenNonAssetFd(mObject, cookie, fileName, mOffsets);
            if (pfd == null) {
                throw new java.io.FileNotFoundException("Asset absolute file: " + fileName);
            }
            return new android.content.res.AssetFileDescriptor(pfd, mOffsets[0], mOffsets[1]);
        }
    }

    /**
     * Retrieve a parser for a compiled XML file.
     *
     * @param fileName
     * 		The name of the file to retrieve.
     */
    @android.annotation.NonNull
    public android.content.res.XmlResourceParser openXmlResourceParser(@android.annotation.NonNull
    java.lang.String fileName) throws java.io.IOException {
        return openXmlResourceParser(0, fileName);
    }

    /**
     * Retrieve a parser for a compiled XML file.
     *
     * @param cookie
     * 		Identifier of the package to be opened.
     * @param fileName
     * 		The name of the file to retrieve.
     */
    @android.annotation.NonNull
    public android.content.res.XmlResourceParser openXmlResourceParser(int cookie, @android.annotation.NonNull
    java.lang.String fileName) throws java.io.IOException {
        try (android.content.res.XmlBlock block = openXmlBlockAsset(cookie, fileName)) {
            android.content.res.XmlResourceParser parser = block.newParser();
            // If openXmlBlockAsset doesn't throw, it will always return an XmlBlock object with
            // a valid native pointer, which makes newParser always return non-null. But let's
            // be paranoid.
            if (parser == null) {
                throw new java.lang.AssertionError("block.newParser() returned a null parser");
            }
            return parser;
        }
    }

    /**
     * Retrieve a non-asset as a compiled XML file.  Not for use by applications.
     *
     * @param fileName
     * 		The name of the file to retrieve.
     * @unknown 
     */
    @android.annotation.NonNull
    android.content.res.XmlBlock openXmlBlockAsset(@android.annotation.NonNull
    java.lang.String fileName) throws java.io.IOException {
        return openXmlBlockAsset(0, fileName);
    }

    /**
     * Retrieve a non-asset as a compiled XML file.  Not for use by
     * applications.
     *
     * @param cookie
     * 		Identifier of the package to be opened.
     * @param fileName
     * 		Name of the asset to retrieve.
     * @unknown 
     */
    @android.annotation.NonNull
    android.content.res.XmlBlock openXmlBlockAsset(int cookie, @android.annotation.NonNull
    java.lang.String fileName) throws java.io.IOException {
        com.android.internal.util.Preconditions.checkNotNull(fileName, "fileName");
        synchronized(this) {
            ensureOpenLocked();
            final long xmlBlock = android.content.res.AssetManager.nativeOpenXmlAsset(mObject, cookie, fileName);
            if (xmlBlock == 0) {
                throw new java.io.FileNotFoundException("Asset XML file: " + fileName);
            }
            final android.content.res.XmlBlock block = new android.content.res.XmlBlock(this, xmlBlock);
            incRefsLocked(block.hashCode());
            return block;
        }
    }

    void xmlBlockGone(int id) {
        synchronized(this) {
            decRefsLocked(id);
        }
    }

    @android.annotation.UnsupportedAppUsage
    void applyStyle(long themePtr, @android.annotation.AttrRes
    int defStyleAttr, @android.annotation.StyleRes
    int defStyleRes, @android.annotation.Nullable
    android.content.res.XmlBlock.Parser parser, @android.annotation.NonNull
    int[] inAttrs, long outValuesAddress, long outIndicesAddress) {
        com.android.internal.util.Preconditions.checkNotNull(inAttrs, "inAttrs");
        synchronized(this) {
            // Need to synchronize on AssetManager because we will be accessing
            // the native implementation of AssetManager.
            ensureValidLocked();
            android.content.res.AssetManager.nativeApplyStyle(mObject, themePtr, defStyleAttr, defStyleRes, parser != null ? parser.mParseState : 0, inAttrs, outValuesAddress, outIndicesAddress);
        }
    }

    int[] getAttributeResolutionStack(long themePtr, @android.annotation.AttrRes
    int defStyleAttr, @android.annotation.StyleRes
    int defStyleRes, @android.annotation.StyleRes
    int xmlStyle) {
        synchronized(this) {
            return android.content.res.AssetManager.nativeAttributeResolutionStack(mObject, themePtr, xmlStyle, defStyleAttr, defStyleRes);
        }
    }

    @android.annotation.UnsupportedAppUsage
    boolean resolveAttrs(long themePtr, @android.annotation.AttrRes
    int defStyleAttr, @android.annotation.StyleRes
    int defStyleRes, @android.annotation.Nullable
    int[] inValues, @android.annotation.NonNull
    int[] inAttrs, @android.annotation.NonNull
    int[] outValues, @android.annotation.NonNull
    int[] outIndices) {
        com.android.internal.util.Preconditions.checkNotNull(inAttrs, "inAttrs");
        com.android.internal.util.Preconditions.checkNotNull(outValues, "outValues");
        com.android.internal.util.Preconditions.checkNotNull(outIndices, "outIndices");
        synchronized(this) {
            // Need to synchronize on AssetManager because we will be accessing
            // the native implementation of AssetManager.
            ensureValidLocked();
            return android.content.res.AssetManager.nativeResolveAttrs(mObject, themePtr, defStyleAttr, defStyleRes, inValues, inAttrs, outValues, outIndices);
        }
    }

    @android.annotation.UnsupportedAppUsage
    boolean retrieveAttributes(@android.annotation.NonNull
    android.content.res.XmlBlock.Parser parser, @android.annotation.NonNull
    int[] inAttrs, @android.annotation.NonNull
    int[] outValues, @android.annotation.NonNull
    int[] outIndices) {
        com.android.internal.util.Preconditions.checkNotNull(parser, "parser");
        com.android.internal.util.Preconditions.checkNotNull(inAttrs, "inAttrs");
        com.android.internal.util.Preconditions.checkNotNull(outValues, "outValues");
        com.android.internal.util.Preconditions.checkNotNull(outIndices, "outIndices");
        synchronized(this) {
            // Need to synchronize on AssetManager because we will be accessing
            // the native implementation of AssetManager.
            ensureValidLocked();
            return android.content.res.AssetManager.nativeRetrieveAttributes(mObject, parser.mParseState, inAttrs, outValues, outIndices);
        }
    }

    @android.annotation.UnsupportedAppUsage
    long createTheme() {
        synchronized(this) {
            ensureValidLocked();
            long themePtr = android.content.res.AssetManager.nativeThemeCreate(mObject);
            incRefsLocked(themePtr);
            return themePtr;
        }
    }

    void releaseTheme(long themePtr) {
        synchronized(this) {
            android.content.res.AssetManager.nativeThemeDestroy(themePtr);
            decRefsLocked(themePtr);
        }
    }

    void applyStyleToTheme(long themePtr, @android.annotation.StyleRes
    int resId, boolean force) {
        synchronized(this) {
            // Need to synchronize on AssetManager because we will be accessing
            // the native implementation of AssetManager.
            ensureValidLocked();
            android.content.res.AssetManager.nativeThemeApplyStyle(mObject, themePtr, resId, force);
        }
    }

    @android.annotation.UnsupportedAppUsage
    void setThemeTo(long dstThemePtr, @android.annotation.NonNull
    android.content.res.AssetManager srcAssetManager, long srcThemePtr) {
        synchronized(this) {
            ensureValidLocked();
            synchronized(srcAssetManager) {
                srcAssetManager.ensureValidLocked();
                android.content.res.AssetManager.nativeThemeCopy(mObject, dstThemePtr, srcAssetManager.mObject, srcThemePtr);
            }
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        if (android.content.res.AssetManager.DEBUG_REFS && (mNumRefs != 0)) {
            android.util.Log.w(android.content.res.AssetManager.TAG, (("AssetManager " + this) + " finalized with non-zero refs: ") + mNumRefs);
            if (mRefStacks != null) {
                for (java.lang.RuntimeException e : mRefStacks.values()) {
                    android.util.Log.w(android.content.res.AssetManager.TAG, "Reference from here", e);
                }
            }
        }
        if (mObject != 0) {
            android.content.res.AssetManager.nativeDestroy(mObject);
        }
    }

    /* No Locking is needed for AssetInputStream because an AssetInputStream is not-thread
    safe and it does not rely on AssetManager once it has been created. It completely owns the
    underlying Asset.
     */
    public final class AssetInputStream extends java.io.InputStream {
        private long mAssetNativePtr;

        private long mLength;

        private long mMarkPos;

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public final int getAssetInt() {
            throw new java.lang.UnsupportedOperationException();
        }

        /**
         *
         *
         * @unknown 
         */
        @android.annotation.UnsupportedAppUsage
        public final long getNativeAsset() {
            return mAssetNativePtr;
        }

        private AssetInputStream(long assetNativePtr) {
            mAssetNativePtr = assetNativePtr;
            mLength = android.content.res.AssetManager.nativeAssetGetLength(assetNativePtr);
        }

        @java.lang.Override
        public final int read() throws java.io.IOException {
            ensureOpen();
            return android.content.res.AssetManager.nativeAssetReadChar(mAssetNativePtr);
        }

        @java.lang.Override
        public final int read(@android.annotation.NonNull
        byte[] b) throws java.io.IOException {
            ensureOpen();
            com.android.internal.util.Preconditions.checkNotNull(b, "b");
            return android.content.res.AssetManager.nativeAssetRead(mAssetNativePtr, b, 0, b.length);
        }

        @java.lang.Override
        public final int read(@android.annotation.NonNull
        byte[] b, int off, int len) throws java.io.IOException {
            ensureOpen();
            com.android.internal.util.Preconditions.checkNotNull(b, "b");
            return android.content.res.AssetManager.nativeAssetRead(mAssetNativePtr, b, off, len);
        }

        @java.lang.Override
        public final long skip(long n) throws java.io.IOException {
            ensureOpen();
            long pos = android.content.res.AssetManager.nativeAssetSeek(mAssetNativePtr, 0, 0);
            if ((pos + n) > mLength) {
                n = mLength - pos;
            }
            if (n > 0) {
                android.content.res.AssetManager.nativeAssetSeek(mAssetNativePtr, n, 0);
            }
            return n;
        }

        @java.lang.Override
        public final int available() throws java.io.IOException {
            ensureOpen();
            final long len = android.content.res.AssetManager.nativeAssetGetRemainingLength(mAssetNativePtr);
            return len > java.lang.Integer.MAX_VALUE ? java.lang.Integer.MAX_VALUE : ((int) (len));
        }

        @java.lang.Override
        public final boolean markSupported() {
            return true;
        }

        @java.lang.Override
        public final void mark(int readlimit) {
            ensureOpen();
            mMarkPos = android.content.res.AssetManager.nativeAssetSeek(mAssetNativePtr, 0, 0);
        }

        @java.lang.Override
        public final void reset() throws java.io.IOException {
            ensureOpen();
            android.content.res.AssetManager.nativeAssetSeek(mAssetNativePtr, mMarkPos, -1);
        }

        @java.lang.Override
        public final void close() throws java.io.IOException {
            if (mAssetNativePtr != 0) {
                android.content.res.AssetManager.nativeAssetDestroy(mAssetNativePtr);
                mAssetNativePtr = 0;
                synchronized(android.content.res.AssetManager.this) {
                    decRefsLocked(hashCode());
                }
            }
        }

        @java.lang.Override
        protected void finalize() throws java.lang.Throwable {
            close();
        }

        private void ensureOpen() {
            if (mAssetNativePtr == 0) {
                throw new java.lang.IllegalStateException("AssetInputStream is closed");
            }
        }
    }

    /**
     * Determine whether the state in this asset manager is up-to-date with
     * the files on the filesystem.  If false is returned, you need to
     * instantiate a new AssetManager class to see the new data.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public boolean isUpToDate() {
        synchronized(this) {
            if (!mOpen) {
                return false;
            }
            for (android.content.res.ApkAssets apkAssets : mApkAssets) {
                if (!apkAssets.isUpToDate()) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Get the locales that this asset manager contains data for.
     *
     * <p>On SDK 21 (Android 5.0: Lollipop) and above, Locale strings are valid
     * <a href="https://tools.ietf.org/html/bcp47">BCP-47</a> language tags and can be
     * parsed using {@link java.util.Locale#forLanguageTag(String)}.
     *
     * <p>On SDK 20 (Android 4.4W: Kitkat for watches) and below, locale strings
     * are of the form {@code ll_CC} where {@code ll} is a two letter language code,
     * and {@code CC} is a two letter country code.
     */
    public java.lang.String[] getLocales() {
        synchronized(this) {
            ensureValidLocked();
            return /* excludeSystem */
            android.content.res.AssetManager.nativeGetLocales(mObject, false);
        }
    }

    /**
     * Same as getLocales(), except that locales that are only provided by the system (i.e. those
     * present in framework-res.apk or its overlays) will not be listed.
     *
     * For example, if the "system" assets support English, French, and German, and the additional
     * assets support Cherokee and French, getLocales() would return
     * [Cherokee, English, French, German], while getNonSystemLocales() would return
     * [Cherokee, French].
     *
     * @unknown 
     */
    public java.lang.String[] getNonSystemLocales() {
        synchronized(this) {
            ensureValidLocked();
            return /* excludeSystem */
            android.content.res.AssetManager.nativeGetLocales(mObject, true);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    android.content.res.Configuration[] getSizeConfigurations() {
        synchronized(this) {
            ensureValidLocked();
            return android.content.res.AssetManager.nativeGetSizeConfigurations(mObject);
        }
    }

    /**
     * Change the configuration used when retrieving resources.  Not for use by
     * applications.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void setConfiguration(int mcc, int mnc, @android.annotation.Nullable
    java.lang.String locale, int orientation, int touchscreen, int density, int keyboard, int keyboardHidden, int navigation, int screenWidth, int screenHeight, int smallestScreenWidthDp, int screenWidthDp, int screenHeightDp, int screenLayout, int uiMode, int colorMode, int majorVersion) {
        synchronized(this) {
            ensureValidLocked();
            android.content.res.AssetManager.nativeSetConfiguration(mObject, mcc, mnc, locale, orientation, touchscreen, density, keyboard, keyboardHidden, navigation, screenWidth, screenHeight, smallestScreenWidthDp, screenWidthDp, screenHeightDp, screenLayout, uiMode, colorMode, majorVersion);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public android.util.SparseArray<java.lang.String> getAssignedPackageIdentifiers() {
        synchronized(this) {
            ensureValidLocked();
            return android.content.res.AssetManager.nativeGetAssignedPackageIdentifiers(mObject);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    @com.android.internal.annotations.GuardedBy("this")
    @android.annotation.Nullable
    public java.util.Map<java.lang.String, java.lang.String> getOverlayableMap(java.lang.String packageName) {
        synchronized(this) {
            ensureValidLocked();
            return android.content.res.AssetManager.nativeGetOverlayableMap(mObject, packageName);
        }
    }

    @com.android.internal.annotations.GuardedBy("this")
    private void incRefsLocked(long id) {
        if (android.content.res.AssetManager.DEBUG_REFS) {
            if (mRefStacks == null) {
                mRefStacks = new java.util.HashMap<>();
            }
            java.lang.RuntimeException ex = new java.lang.RuntimeException();
            ex.fillInStackTrace();
            mRefStacks.put(id, ex);
        }
        mNumRefs++;
    }

    @com.android.internal.annotations.GuardedBy("this")
    private void decRefsLocked(long id) {
        if (android.content.res.AssetManager.DEBUG_REFS && (mRefStacks != null)) {
            mRefStacks.remove(id);
        }
        mNumRefs--;
        if ((mNumRefs == 0) && (mObject != 0)) {
            android.content.res.AssetManager.nativeDestroy(mObject);
            mObject = 0;
            mApkAssets = android.content.res.AssetManager.sEmptyApkAssets;
        }
    }

    // AssetManager setup native methods.
    private static native long nativeCreate();

    private static native void nativeDestroy(long ptr);

    private static native void nativeSetApkAssets(long ptr, @android.annotation.NonNull
    android.content.res.ApkAssets[] apkAssets, boolean invalidateCaches);

    private static native void nativeSetConfiguration(long ptr, int mcc, int mnc, @android.annotation.Nullable
    java.lang.String locale, int orientation, int touchscreen, int density, int keyboard, int keyboardHidden, int navigation, int screenWidth, int screenHeight, int smallestScreenWidthDp, int screenWidthDp, int screenHeightDp, int screenLayout, int uiMode, int colorMode, int majorVersion);

    @android.annotation.NonNull
    private static native android.util.SparseArray<java.lang.String> nativeGetAssignedPackageIdentifiers(long ptr);

    // File native methods.
    @android.annotation.Nullable
    private static native java.lang.String[] nativeList(long ptr, @android.annotation.NonNull
    java.lang.String path) throws java.io.IOException;

    private static native long nativeOpenAsset(long ptr, @android.annotation.NonNull
    java.lang.String fileName, int accessMode);

    @android.annotation.Nullable
    private static native android.os.ParcelFileDescriptor nativeOpenAssetFd(long ptr, @android.annotation.NonNull
    java.lang.String fileName, long[] outOffsets) throws java.io.IOException;

    private static native long nativeOpenNonAsset(long ptr, int cookie, @android.annotation.NonNull
    java.lang.String fileName, int accessMode);

    @android.annotation.Nullable
    private static native android.os.ParcelFileDescriptor nativeOpenNonAssetFd(long ptr, int cookie, @android.annotation.NonNull
    java.lang.String fileName, @android.annotation.NonNull
    long[] outOffsets) throws java.io.IOException;

    private static native long nativeOpenXmlAsset(long ptr, int cookie, @android.annotation.NonNull
    java.lang.String fileName);

    // Primitive resource native methods.
    private static native int nativeGetResourceValue(long ptr, @android.annotation.AnyRes
    int resId, short density, @android.annotation.NonNull
    android.util.TypedValue outValue, boolean resolveReferences);

    private static native int nativeGetResourceBagValue(long ptr, @android.annotation.AnyRes
    int resId, int bagEntryId, @android.annotation.NonNull
    android.util.TypedValue outValue);

    @android.annotation.Nullable
    @android.annotation.AttrRes
    private static native int[] nativeGetStyleAttributes(long ptr, @android.annotation.StyleRes
    int resId);

    @android.annotation.Nullable
    private static native java.lang.String[] nativeGetResourceStringArray(long ptr, @android.annotation.ArrayRes
    int resId);

    @android.annotation.Nullable
    private static native int[] nativeGetResourceStringArrayInfo(long ptr, @android.annotation.ArrayRes
    int resId);

    @android.annotation.Nullable
    private static native int[] nativeGetResourceIntArray(long ptr, @android.annotation.ArrayRes
    int resId);

    private static native int nativeGetResourceArraySize(long ptr, @android.annotation.ArrayRes
    int resId);

    private static native int nativeGetResourceArray(long ptr, @android.annotation.ArrayRes
    int resId, @android.annotation.NonNull
    int[] outValues);

    // Resource name/ID native methods.
    @android.annotation.AnyRes
    private static native int nativeGetResourceIdentifier(long ptr, @android.annotation.NonNull
    java.lang.String name, @android.annotation.Nullable
    java.lang.String defType, @android.annotation.Nullable
    java.lang.String defPackage);

    @android.annotation.Nullable
    private static native java.lang.String nativeGetResourceName(long ptr, @android.annotation.AnyRes
    int resid);

    @android.annotation.Nullable
    private static native java.lang.String nativeGetResourcePackageName(long ptr, @android.annotation.AnyRes
    int resid);

    @android.annotation.Nullable
    private static native java.lang.String nativeGetResourceTypeName(long ptr, @android.annotation.AnyRes
    int resid);

    @android.annotation.Nullable
    private static native java.lang.String nativeGetResourceEntryName(long ptr, @android.annotation.AnyRes
    int resid);

    @android.annotation.Nullable
    private static native java.lang.String[] nativeGetLocales(long ptr, boolean excludeSystem);

    @android.annotation.Nullable
    private static native android.content.res.Configuration[] nativeGetSizeConfigurations(long ptr);

    private static native void nativeSetResourceResolutionLoggingEnabled(long ptr, boolean enabled);

    @android.annotation.Nullable
    private static native java.lang.String nativeGetLastResourceResolution(long ptr);

    // Style attribute retrieval native methods.
    private static native int[] nativeAttributeResolutionStack(long ptr, long themePtr, @android.annotation.StyleRes
    int xmlStyleRes, @android.annotation.AttrRes
    int defStyleAttr, @android.annotation.StyleRes
    int defStyleRes);

    private static native void nativeApplyStyle(long ptr, long themePtr, @android.annotation.AttrRes
    int defStyleAttr, @android.annotation.StyleRes
    int defStyleRes, long xmlParserPtr, @android.annotation.NonNull
    int[] inAttrs, long outValuesAddress, long outIndicesAddress);

    private static native boolean nativeResolveAttrs(long ptr, long themePtr, @android.annotation.AttrRes
    int defStyleAttr, @android.annotation.StyleRes
    int defStyleRes, @android.annotation.Nullable
    int[] inValues, @android.annotation.NonNull
    int[] inAttrs, @android.annotation.NonNull
    int[] outValues, @android.annotation.NonNull
    int[] outIndices);

    private static native boolean nativeRetrieveAttributes(long ptr, long xmlParserPtr, @android.annotation.NonNull
    int[] inAttrs, @android.annotation.NonNull
    int[] outValues, @android.annotation.NonNull
    int[] outIndices);

    // Theme related native methods
    private static native long nativeThemeCreate(long ptr);

    private static native void nativeThemeDestroy(long themePtr);

    private static native void nativeThemeApplyStyle(long ptr, long themePtr, @android.annotation.StyleRes
    int resId, boolean force);

    private static native void nativeThemeCopy(long dstAssetManagerPtr, long dstThemePtr, long srcAssetManagerPtr, long srcThemePtr);

    static native void nativeThemeClear(long themePtr);

    private static native int nativeThemeGetAttributeValue(long ptr, long themePtr, @android.annotation.AttrRes
    int resId, @android.annotation.NonNull
    android.util.TypedValue outValue, boolean resolve);

    private static native void nativeThemeDump(long ptr, long themePtr, int priority, java.lang.String tag, java.lang.String prefix);

    @android.content.res.Configuration.NativeConfig
    static native int nativeThemeGetChangingConfigurations(long themePtr);

    // AssetInputStream related native methods.
    private static native void nativeAssetDestroy(long assetPtr);

    private static native int nativeAssetReadChar(long assetPtr);

    private static native int nativeAssetRead(long assetPtr, byte[] b, int off, int len);

    private static native long nativeAssetSeek(long assetPtr, long offset, int whence);

    private static native long nativeAssetGetLength(long assetPtr);

    private static native long nativeAssetGetRemainingLength(long assetPtr);

    private static native void nativeVerifySystemIdmaps();

    private static native java.lang.String[] nativeCreateIdmapsForStaticOverlaysTargetingAndroid();

    @android.annotation.Nullable
    private static native java.util.Map nativeGetOverlayableMap(long ptr, @android.annotation.NonNull
    java.lang.String packageName);

    // Global debug native methods.
    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static native int getGlobalAssetCount();

    /**
     *
     *
     * @unknown 
     */
    public static native java.lang.String getAssetAllocations();

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static native int getGlobalAssetManagerCount();
}

