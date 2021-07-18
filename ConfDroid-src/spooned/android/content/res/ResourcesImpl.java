/**
 * Copyright 2018 The Android Open Source Project
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


import static value.string.toString;


/**
 * The implementation of Resource access. This class contains the AssetManager and all caches
 * associated with it.
 *
 * {@link Resources} is just a thing wrapper around this class. When a configuration change
 * occurs, clients can retain the same {@link Resources} reference because the underlying
 * {@link ResourcesImpl} object will be updated or re-created.
 *
 * @unknown 
 */
public class ResourcesImpl {
    static final java.lang.String TAG = "Resources";

    private static final boolean DEBUG_LOAD = false;

    private static final boolean DEBUG_CONFIG = false;

    static final java.lang.String TAG_PRELOAD = android.content.res.ResourcesImpl.TAG + ".preload";

    @android.annotation.UnsupportedAppUsage
    private static final boolean TRACE_FOR_PRELOAD = false;// Do we still need it?


    @android.annotation.UnsupportedAppUsage
    private static final boolean TRACE_FOR_MISS_PRELOAD = false;// Do we still need it?


    public static final boolean TRACE_FOR_DETAILED_PRELOAD = android.os.SystemProperties.getBoolean("debug.trace_resource_preload", false);

    /**
     * Used only when TRACE_FOR_DETAILED_PRELOAD is true.
     */
    private static int sPreloadTracingNumLoadedDrawables;

    private long mPreloadTracingPreloadStartTime;

    private long mPreloadTracingStartBitmapSize;

    private long mPreloadTracingStartBitmapCount;

    private static final int ID_OTHER = 0x1000004;

    private static final java.lang.Object sSync = new java.lang.Object();

    private static boolean sPreloaded;

    @android.annotation.UnsupportedAppUsage
    private boolean mPreloading;

    // Information about preloaded resources.  Note that they are not
    // protected by a lock, because while preloading in zygote we are all
    // single-threaded, and after that these are immutable.
    @android.annotation.UnsupportedAppUsage
    private static final android.util.LongSparseArray<android.graphics.drawable.Drawable.ConstantState>[] sPreloadedDrawables;

    @android.annotation.UnsupportedAppUsage
    private static final android.util.LongSparseArray<android.graphics.drawable.Drawable.ConstantState> sPreloadedColorDrawables = new android.util.LongSparseArray();

    @android.annotation.UnsupportedAppUsage
    private static final android.util.LongSparseArray<android.content.res.ConstantState<android.content.res.ComplexColor>> sPreloadedComplexColors = new android.util.LongSparseArray();

    /**
     * Lock object used to protect access to caches and configuration.
     */
    @android.annotation.UnsupportedAppUsage
    private final java.lang.Object mAccessLock = new java.lang.Object();

    // These are protected by mAccessLock.
    private final android.content.res.Configuration mTmpConfig = new android.content.res.Configuration();

    @android.annotation.UnsupportedAppUsage
    private final android.content.res.DrawableCache mDrawableCache = new android.content.res.DrawableCache();

    @android.annotation.UnsupportedAppUsage
    private final android.content.res.DrawableCache mColorDrawableCache = new android.content.res.DrawableCache();

    private final android.content.res.ConfigurationBoundResourceCache<android.content.res.ComplexColor> mComplexColorCache = new android.content.res.ConfigurationBoundResourceCache<>();

    @android.annotation.UnsupportedAppUsage
    private final android.content.res.ConfigurationBoundResourceCache<android.animation.Animator> mAnimatorCache = new android.content.res.ConfigurationBoundResourceCache<>();

    @android.annotation.UnsupportedAppUsage
    private final android.content.res.ConfigurationBoundResourceCache<android.animation.StateListAnimator> mStateListAnimatorCache = new android.content.res.ConfigurationBoundResourceCache<>();

    // A stack of all the resourceIds already referenced when parsing a resource. This is used to
    // detect circular references in the xml.
    // Using a ThreadLocal variable ensures that we have different stacks for multiple parallel
    // calls to ResourcesImpl
    private final java.lang.ThreadLocal<android.content.res.ResourcesImpl.LookupStack> mLookupStack = java.lang.ThreadLocal.withInitial(() -> new android.content.res.ResourcesImpl.LookupStack());

    /**
     * Size of the cyclical cache used to map XML files to blocks.
     */
    private static final int XML_BLOCK_CACHE_SIZE = 4;

    // Cyclical cache used for recently-accessed XML files.
    private int mLastCachedXmlBlockIndex = -1;

    private final int[] mCachedXmlBlockCookies = new int[android.content.res.ResourcesImpl.XML_BLOCK_CACHE_SIZE];

    private final java.lang.String[] mCachedXmlBlockFiles = new java.lang.String[android.content.res.ResourcesImpl.XML_BLOCK_CACHE_SIZE];

    private final android.content.res.XmlBlock[] mCachedXmlBlocks = new android.content.res.XmlBlock[android.content.res.ResourcesImpl.XML_BLOCK_CACHE_SIZE];

    @android.annotation.UnsupportedAppUsage
    final android.content.res.AssetManager mAssets;

    private final android.util.DisplayMetrics mMetrics = new android.util.DisplayMetrics();

    private final android.view.DisplayAdjustments mDisplayAdjustments;

    private android.icu.text.PluralRules mPluralRule;

    @android.annotation.UnsupportedAppUsage
    private final android.content.res.Configuration mConfiguration = new android.content.res.Configuration();

    static {
        sPreloadedDrawables = new android.util.LongSparseArray[2];
        android.content.res.ResourcesImpl.sPreloadedDrawables[0] = new android.util.LongSparseArray();
        android.content.res.ResourcesImpl.sPreloadedDrawables[1] = new android.util.LongSparseArray();
    }

    /**
     * Creates a new ResourcesImpl object with CompatibilityInfo.
     *
     * @param assets
     * 		Previously created AssetManager.
     * @param metrics
     * 		Current display metrics to consider when
     * 		selecting/computing resource values.
     * @param config
     * 		Desired device configuration to consider when
     * 		selecting/computing resource values (optional).
     * @param displayAdjustments
     * 		this resource's Display override and compatibility info.
     * 		Must not be null.
     */
    @android.annotation.UnsupportedAppUsage
    public ResourcesImpl(@android.annotation.NonNull
    android.content.res.AssetManager assets, @android.annotation.Nullable
    android.util.DisplayMetrics metrics, @android.annotation.Nullable
    android.content.res.Configuration config, @android.annotation.NonNull
    android.view.DisplayAdjustments displayAdjustments) {
        mAssets = assets;
        mMetrics.setToDefaults();
        mDisplayAdjustments = displayAdjustments;
        mConfiguration.setToDefaults();
        updateConfiguration(config, metrics, displayAdjustments.getCompatibilityInfo());
    }

    public android.view.DisplayAdjustments getDisplayAdjustments() {
        return mDisplayAdjustments;
    }

    @android.annotation.UnsupportedAppUsage
    public android.content.res.AssetManager getAssets() {
        return mAssets;
    }

    @android.annotation.UnsupportedAppUsage
    android.util.DisplayMetrics getDisplayMetrics() {
        if (android.content.res.ResourcesImpl.DEBUG_CONFIG)
            android.util.Slog.v(android.content.res.ResourcesImpl.TAG, (((("Returning DisplayMetrics: " + mMetrics.widthPixels) + "x") + mMetrics.heightPixels) + " ") + mMetrics.density);

        return mMetrics;
    }

    android.content.res.Configuration getConfiguration() {
        return mConfiguration;
    }

    android.content.res.Configuration[] getSizeConfigurations() {
        return mAssets.getSizeConfigurations();
    }

    android.content.res.CompatibilityInfo getCompatibilityInfo() {
        return mDisplayAdjustments.getCompatibilityInfo();
    }

    private android.icu.text.PluralRules getPluralRule() {
        synchronized(android.content.res.ResourcesImpl.sSync) {
            if (mPluralRule == null) {
                mPluralRule = android.icu.text.PluralRules.forLocale(mConfiguration.getLocales().get(0));
            }
            return mPluralRule;
        }
    }

    @android.annotation.UnsupportedAppUsage
    void getValue(@android.annotation.AnyRes
    int id, android.util.TypedValue outValue, boolean resolveRefs) throws android.content.res.Resources.NotFoundException {
        boolean found = mAssets.getResourceValue(id, 0, outValue, resolveRefs);
        if (found) {
            return;
        }
        throw new android.content.res.Resources.NotFoundException("Resource ID #0x" + java.lang.Integer.toHexString(id));
    }

    void getValueForDensity(@android.annotation.AnyRes
    int id, int density, android.util.TypedValue outValue, boolean resolveRefs) throws android.content.res.Resources.NotFoundException {
        boolean found = mAssets.getResourceValue(id, density, outValue, resolveRefs);
        if (found) {
            return;
        }
        throw new android.content.res.Resources.NotFoundException("Resource ID #0x" + java.lang.Integer.toHexString(id));
    }

    void getValue(java.lang.String name, android.util.TypedValue outValue, boolean resolveRefs) throws android.content.res.Resources.NotFoundException {
        int id = getIdentifier(name, "string", null);
        if (id != 0) {
            getValue(id, outValue, resolveRefs);
            return;
        }
        throw new android.content.res.Resources.NotFoundException("String resource name " + name);
    }

    int getIdentifier(java.lang.String name, java.lang.String defType, java.lang.String defPackage) {
        if (name == null) {
            throw new java.lang.NullPointerException("name is null");
        }
        try {
            return java.lang.Integer.parseInt(name);
        } catch (java.lang.Exception e) {
            // Ignore
        }
        return mAssets.getResourceIdentifier(name, defType, defPackage);
    }

    @android.annotation.NonNull
    java.lang.String getResourceName(@android.annotation.AnyRes
    int resid) throws android.content.res.Resources.NotFoundException {
        java.lang.String str = mAssets.getResourceName(resid);
        if (str != null)
            return str;

        throw new android.content.res.Resources.NotFoundException("Unable to find resource ID #0x" + java.lang.Integer.toHexString(resid));
    }

    @android.annotation.NonNull
    java.lang.String getResourcePackageName(@android.annotation.AnyRes
    int resid) throws android.content.res.Resources.NotFoundException {
        java.lang.String str = mAssets.getResourcePackageName(resid);
        if (str != null)
            return str;

        throw new android.content.res.Resources.NotFoundException("Unable to find resource ID #0x" + java.lang.Integer.toHexString(resid));
    }

    @android.annotation.NonNull
    java.lang.String getResourceTypeName(@android.annotation.AnyRes
    int resid) throws android.content.res.Resources.NotFoundException {
        java.lang.String str = mAssets.getResourceTypeName(resid);
        if (str != null)
            return str;

        throw new android.content.res.Resources.NotFoundException("Unable to find resource ID #0x" + java.lang.Integer.toHexString(resid));
    }

    @android.annotation.NonNull
    java.lang.String getResourceEntryName(@android.annotation.AnyRes
    int resid) throws android.content.res.Resources.NotFoundException {
        java.lang.String str = mAssets.getResourceEntryName(resid);
        if (str != null)
            return str;

        throw new android.content.res.Resources.NotFoundException("Unable to find resource ID #0x" + java.lang.Integer.toHexString(resid));
    }

    @android.annotation.NonNull
    java.lang.String getLastResourceResolution() throws android.content.res.Resources.NotFoundException {
        java.lang.String str = mAssets.getLastResourceResolution();
        if (str != null)
            return str;

        throw new android.content.res.Resources.NotFoundException("Associated AssetManager hasn't resolved a resource");
    }

    @android.annotation.NonNull
    java.lang.CharSequence getQuantityText(@android.annotation.PluralsRes
    int id, int quantity) throws android.content.res.Resources.NotFoundException {
        android.icu.text.PluralRules rule = getPluralRule();
        java.lang.CharSequence res = mAssets.getResourceBagText(id, android.content.res.ResourcesImpl.attrForQuantityCode(rule.select(quantity)));
        if (res != null) {
            return res;
        }
        res = mAssets.getResourceBagText(id, android.content.res.ResourcesImpl.ID_OTHER);
        if (res != null) {
            return res;
        }
        throw new android.content.res.Resources.NotFoundException((((("Plural resource ID #0x" + java.lang.Integer.toHexString(id)) + " quantity=") + quantity) + " item=") + rule.select(quantity));
    }

    private static int attrForQuantityCode(java.lang.String quantityCode) {
        switch (quantityCode) {
            case android.icu.text.PluralRules.KEYWORD_ZERO :
                return 0x1000005;
            case android.icu.text.PluralRules.KEYWORD_ONE :
                return 0x1000006;
            case android.icu.text.PluralRules.KEYWORD_TWO :
                return 0x1000007;
            case android.icu.text.PluralRules.KEYWORD_FEW :
                return 0x1000008;
            case android.icu.text.PluralRules.KEYWORD_MANY :
                return 0x1000009;
            default :
                return android.content.res.ResourcesImpl.ID_OTHER;
        }
    }

    @android.annotation.NonNull
    android.content.res.AssetFileDescriptor openRawResourceFd(@android.annotation.RawRes
    int id, android.util.TypedValue tempValue) throws android.content.res.Resources.NotFoundException {
        getValue(id, tempValue, true);
        try {
            return mAssets.openNonAssetFd(tempValue.assetCookie, tempValue.string.toString());
        } catch (java.lang.Exception e) {
            throw new android.content.res.Resources.NotFoundException(((("File " + tempValue.string.toString()) + " from drawable ") + "resource ID #0x") + java.lang.Integer.toHexString(id), e);
        }
    }

    @android.annotation.NonNull
    java.io.InputStream openRawResource(@android.annotation.RawRes
    int id, android.util.TypedValue value) throws android.content.res.Resources.NotFoundException {
        getValue(id, value, true);
        try {
            return mAssets.openNonAsset(value.assetCookie, value.string.toString(), android.content.res.AssetManager.ACCESS_STREAMING);
        } catch (java.lang.Exception e) {
            // Note: value.string might be null
            android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException((("File " + (value.string == null ? "(null)" : value.string.toString())) + " from drawable resource ID #0x") + java.lang.Integer.toHexString(id));
            rnf.initCause(e);
            throw rnf;
        }
    }

    android.content.res.ConfigurationBoundResourceCache<android.animation.Animator> getAnimatorCache() {
        return mAnimatorCache;
    }

    android.content.res.ConfigurationBoundResourceCache<android.animation.StateListAnimator> getStateListAnimatorCache() {
        return mStateListAnimatorCache;
    }

    public void updateConfiguration(android.content.res.Configuration config, android.util.DisplayMetrics metrics, android.content.res.CompatibilityInfo compat) {
        android.os.Trace.traceBegin(Trace.TRACE_TAG_RESOURCES, "ResourcesImpl#updateConfiguration");
        try {
            synchronized(mAccessLock) {
                if (false) {
                    android.util.Slog.i(android.content.res.ResourcesImpl.TAG, (((("**** Updating config of " + this) + ": old config is ") + mConfiguration) + " old compat is ") + mDisplayAdjustments.getCompatibilityInfo());
                    android.util.Slog.i(android.content.res.ResourcesImpl.TAG, (((("**** Updating config of " + this) + ": new config is ") + config) + " new compat is ") + compat);
                }
                if (compat != null) {
                    mDisplayAdjustments.setCompatibilityInfo(compat);
                }
                if (metrics != null) {
                    mMetrics.setTo(metrics);
                }
                // NOTE: We should re-arrange this code to create a Display
                // with the CompatibilityInfo that is used everywhere we deal
                // with the display in relation to this app, rather than
                // doing the conversion here.  This impl should be okay because
                // we make sure to return a compatible display in the places
                // where there are public APIs to retrieve the display...  but
                // it would be cleaner and more maintainable to just be
                // consistently dealing with a compatible display everywhere in
                // the framework.
                mDisplayAdjustments.getCompatibilityInfo().applyToDisplayMetrics(mMetrics);
                @android.content.pm.ActivityInfo.Config
                final int configChanges = calcConfigChanges(config);
                // If even after the update there are no Locales set, grab the default locales.
                android.os.LocaleList locales = mConfiguration.getLocales();
                if (locales.isEmpty()) {
                    locales = android.os.LocaleList.getDefault();
                    mConfiguration.setLocales(locales);
                }
                if ((configChanges & android.content.pm.ActivityInfo.CONFIG_LOCALE) != 0) {
                    if (locales.size() > 1) {
                        // The LocaleList has changed. We must query the AssetManager's available
                        // Locales and figure out the best matching Locale in the new LocaleList.
                        java.lang.String[] availableLocales = mAssets.getNonSystemLocales();
                        if (android.os.LocaleList.isPseudoLocalesOnly(availableLocales)) {
                            // No app defined locales, so grab the system locales.
                            availableLocales = mAssets.getLocales();
                            if (android.os.LocaleList.isPseudoLocalesOnly(availableLocales)) {
                                availableLocales = null;
                            }
                        }
                        if (availableLocales != null) {
                            final java.util.Locale bestLocale = locales.getFirstMatchWithEnglishSupported(availableLocales);
                            if ((bestLocale != null) && (bestLocale != locales.get(0))) {
                                mConfiguration.setLocales(new android.os.LocaleList(bestLocale, locales));
                            }
                        }
                    }
                }
                if (mConfiguration.densityDpi != android.content.res.Configuration.DENSITY_DPI_UNDEFINED) {
                    mMetrics.densityDpi = mConfiguration.densityDpi;
                    mMetrics.density = mConfiguration.densityDpi * android.util.DisplayMetrics.DENSITY_DEFAULT_SCALE;
                }
                // Protect against an unset fontScale.
                mMetrics.scaledDensity = mMetrics.density * (mConfiguration.fontScale != 0 ? mConfiguration.fontScale : 1.0F);
                final int width;
                final int height;
                if (mMetrics.widthPixels >= mMetrics.heightPixels) {
                    width = mMetrics.widthPixels;
                    height = mMetrics.heightPixels;
                } else {
                    // noinspection SuspiciousNameCombination
                    width = mMetrics.heightPixels;
                    // noinspection SuspiciousNameCombination
                    height = mMetrics.widthPixels;
                }
                final int keyboardHidden;
                if ((mConfiguration.keyboardHidden == android.content.res.Configuration.KEYBOARDHIDDEN_NO) && (mConfiguration.hardKeyboardHidden == android.content.res.Configuration.HARDKEYBOARDHIDDEN_YES)) {
                    keyboardHidden = android.content.res.Configuration.KEYBOARDHIDDEN_SOFT;
                } else {
                    keyboardHidden = mConfiguration.keyboardHidden;
                }
                mAssets.setConfiguration(mConfiguration.mcc, mConfiguration.mnc, android.content.res.ResourcesImpl.adjustLanguageTag(mConfiguration.getLocales().get(0).toLanguageTag()), mConfiguration.orientation, mConfiguration.touchscreen, mConfiguration.densityDpi, mConfiguration.keyboard, keyboardHidden, mConfiguration.navigation, width, height, mConfiguration.smallestScreenWidthDp, mConfiguration.screenWidthDp, mConfiguration.screenHeightDp, mConfiguration.screenLayout, mConfiguration.uiMode, mConfiguration.colorMode, Build.VERSION.RESOURCES_SDK_INT);
                if (android.content.res.ResourcesImpl.DEBUG_CONFIG) {
                    android.util.Slog.i(android.content.res.ResourcesImpl.TAG, (((("**** Updating config of " + this) + ": final config is ") + mConfiguration) + " final compat is ") + mDisplayAdjustments.getCompatibilityInfo());
                }
                mDrawableCache.onConfigurationChange(configChanges);
                mColorDrawableCache.onConfigurationChange(configChanges);
                mComplexColorCache.onConfigurationChange(configChanges);
                mAnimatorCache.onConfigurationChange(configChanges);
                mStateListAnimatorCache.onConfigurationChange(configChanges);
                flushLayoutCache();
            }
            synchronized(android.content.res.ResourcesImpl.sSync) {
                if (mPluralRule != null) {
                    mPluralRule = android.icu.text.PluralRules.forLocale(mConfiguration.getLocales().get(0));
                }
            }
        } finally {
            android.os.Trace.traceEnd(Trace.TRACE_TAG_RESOURCES);
        }
    }

    /**
     * Applies the new configuration, returning a bitmask of the changes
     * between the old and new configurations.
     *
     * @param config
     * 		the new configuration
     * @return bitmask of config changes
     */
    @android.content.pm.ActivityInfo.Config
    public int calcConfigChanges(@android.annotation.Nullable
    android.content.res.Configuration config) {
        if (config == null) {
            // If there is no configuration, assume all flags have changed.
            return 0xffffffff;
        }
        mTmpConfig.setTo(config);
        int density = config.densityDpi;
        if (density == android.content.res.Configuration.DENSITY_DPI_UNDEFINED) {
            density = mMetrics.noncompatDensityDpi;
        }
        mDisplayAdjustments.getCompatibilityInfo().applyToConfiguration(density, mTmpConfig);
        if (mTmpConfig.getLocales().isEmpty()) {
            mTmpConfig.setLocales(android.os.LocaleList.getDefault());
        }
        return mConfiguration.updateFrom(mTmpConfig);
    }

    /**
     * {@code Locale.toLanguageTag} will transform the obsolete (and deprecated)
     * language codes "in", "ji" and "iw" to "id", "yi" and "he" respectively.
     *
     * All released versions of android prior to "L" used the deprecated language
     * tags, so we will need to support them for backwards compatibility.
     *
     * Note that this conversion needs to take place *after* the call to
     * {@code toLanguageTag} because that will convert all the deprecated codes to
     * the new ones, even if they're set manually.
     */
    private static java.lang.String adjustLanguageTag(java.lang.String languageTag) {
        final int separator = languageTag.indexOf('-');
        final java.lang.String language;
        final java.lang.String remainder;
        if (separator == (-1)) {
            language = languageTag;
            remainder = "";
        } else {
            language = languageTag.substring(0, separator);
            remainder = languageTag.substring(separator);
        }
        return java.util.Locale.adjustLanguageCode(language) + remainder;
    }

    /**
     * Call this to remove all cached loaded layout resources from the
     * Resources object.  Only intended for use with performance testing
     * tools.
     */
    public void flushLayoutCache() {
        synchronized(mCachedXmlBlocks) {
            java.util.Arrays.fill(mCachedXmlBlockCookies, 0);
            java.util.Arrays.fill(mCachedXmlBlockFiles, null);
            final android.content.res.XmlBlock[] cachedXmlBlocks = mCachedXmlBlocks;
            for (int i = 0; i < android.content.res.ResourcesImpl.XML_BLOCK_CACHE_SIZE; i++) {
                final android.content.res.XmlBlock oldBlock = cachedXmlBlocks[i];
                if (oldBlock != null) {
                    oldBlock.close();
                }
            }
            java.util.Arrays.fill(cachedXmlBlocks, null);
        }
    }

    @android.annotation.Nullable
    android.graphics.drawable.Drawable loadDrawable(@android.annotation.NonNull
    android.content.res.Resources wrapper, @android.annotation.NonNull
    android.util.TypedValue value, int id, int density, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws android.content.res.Resources.NotFoundException {
        // If the drawable's XML lives in our current density qualifier,
        // it's okay to use a scaled version from the cache. Otherwise, we
        // need to actually load the drawable from XML.
        final boolean useCache = (density == 0) || (value.density == mMetrics.densityDpi);
        // Pretend the requested density is actually the display density. If
        // the drawable returned is not the requested density, then force it
        // to be scaled later by dividing its density by the ratio of
        // requested density to actual device density. Drawables that have
        // undefined density or no density don't need to be handled here.
        if (((density > 0) && (value.density > 0)) && (value.density != android.util.TypedValue.DENSITY_NONE)) {
            if (value.density == density) {
                value.density = mMetrics.densityDpi;
            } else {
                value.density = (value.density * mMetrics.densityDpi) / density;
            }
        }
        try {
            if (android.content.res.ResourcesImpl.TRACE_FOR_PRELOAD) {
                // Log only framework resources
                if ((id >>> 24) == 0x1) {
                    final java.lang.String name = getResourceName(id);
                    if (name != null) {
                        android.util.Log.d("PreloadDrawable", name);
                    }
                }
            }
            final boolean isColorDrawable;
            final android.content.res.DrawableCache caches;
            final long key;
            if ((value.type >= android.util.TypedValue.TYPE_FIRST_COLOR_INT) && (value.type <= android.util.TypedValue.TYPE_LAST_COLOR_INT)) {
                isColorDrawable = true;
                caches = mColorDrawableCache;
                key = value.data;
            } else {
                isColorDrawable = false;
                caches = mDrawableCache;
                key = (((long) (value.assetCookie)) << 32) | value.data;
            }
            // First, check whether we have a cached version of this drawable
            // that was inflated against the specified theme. Skip the cache if
            // we're currently preloading or we're not using the cache.
            if ((!mPreloading) && useCache) {
                final android.graphics.drawable.Drawable cachedDrawable = caches.getInstance(key, wrapper, theme);
                if (cachedDrawable != null) {
                    cachedDrawable.setChangingConfigurations(value.changingConfigurations);
                    return cachedDrawable;
                }
            }
            // Next, check preloaded drawables. Preloaded drawables may contain
            // unresolved theme attributes.
            final android.graphics.drawable.Drawable.ConstantState cs;
            if (isColorDrawable) {
                cs = android.content.res.ResourcesImpl.sPreloadedColorDrawables.get(key);
            } else {
                cs = android.content.res.ResourcesImpl.sPreloadedDrawables[mConfiguration.getLayoutDirection()].get(key);
            }
            android.graphics.drawable.Drawable dr;
            boolean needsNewDrawableAfterCache = false;
            if (cs != null) {
                if (android.content.res.ResourcesImpl.TRACE_FOR_DETAILED_PRELOAD) {
                    // Log only framework resources
                    if (((id >>> 24) == 0x1) && (android.content.res.android.os.Process.myUid() != 0)) {
                        final java.lang.String name = getResourceName(id);
                        if (name != null) {
                            android.util.Log.d(android.content.res.ResourcesImpl.TAG_PRELOAD, (("Hit preloaded FW drawable #" + java.lang.Integer.toHexString(id)) + " ") + name);
                        }
                    }
                }
                dr = cs.newDrawable(wrapper);
            } else
                if (isColorDrawable) {
                    dr = new android.graphics.drawable.ColorDrawable(value.data);
                } else {
                    dr = loadDrawableForCookie(wrapper, value, id, density);
                }

            // DrawableContainer' constant state has drawables instances. In order to leave the
            // constant state intact in the cache, we need to create a new DrawableContainer after
            // added to cache.
            if (dr instanceof android.graphics.drawable.DrawableContainer) {
                needsNewDrawableAfterCache = true;
            }
            // Determine if the drawable has unresolved theme attributes. If it
            // does, we'll need to apply a theme and store it in a theme-specific
            // cache.
            final boolean canApplyTheme = (dr != null) && dr.canApplyTheme();
            if (canApplyTheme && (theme != null)) {
                dr = dr.mutate();
                dr.applyTheme(theme);
                dr.clearMutated();
            }
            // If we were able to obtain a drawable, store it in the appropriate
            // cache: preload, not themed, null theme, or theme-specific. Don't
            // pollute the cache with drawables loaded from a foreign density.
            if (dr != null) {
                dr.setChangingConfigurations(value.changingConfigurations);
                if (useCache) {
                    cacheDrawable(value, isColorDrawable, caches, theme, canApplyTheme, key, dr);
                    if (needsNewDrawableAfterCache) {
                        android.graphics.drawable.Drawable.ConstantState state = dr.getConstantState();
                        if (state != null) {
                            dr = state.newDrawable(wrapper);
                        }
                    }
                }
            }
            return dr;
        } catch (java.lang.Exception e) {
            java.lang.String name;
            try {
                name = getResourceName(id);
            } catch (android.content.res.Resources.NotFoundException e2) {
                name = "(missing name)";
            }
            // The target drawable might fail to load for any number of
            // reasons, but we always want to include the resource name.
            // Since the client already expects this method to throw a
            // NotFoundException, just throw one of those.
            final android.content.res.Resources.NotFoundException nfe = new android.content.res.Resources.NotFoundException((("Drawable " + name) + " with resource ID #0x") + java.lang.Integer.toHexString(id), e);
            nfe.setStackTrace(new java.lang.StackTraceElement[0]);
            throw nfe;
        }
    }

    private void cacheDrawable(android.util.TypedValue value, boolean isColorDrawable, android.content.res.DrawableCache caches, android.content.res.Resources.Theme theme, boolean usesTheme, long key, android.graphics.drawable.Drawable dr) {
        final android.graphics.drawable.Drawable.ConstantState cs = dr.getConstantState();
        if (cs == null) {
            return;
        }
        if (mPreloading) {
            final int changingConfigs = cs.getChangingConfigurations();
            if (isColorDrawable) {
                if (verifyPreloadConfig(changingConfigs, 0, value.resourceId, "drawable")) {
                    android.content.res.ResourcesImpl.sPreloadedColorDrawables.put(key, cs);
                }
            } else {
                if (verifyPreloadConfig(changingConfigs, android.content.pm.ActivityInfo.CONFIG_LAYOUT_DIRECTION, value.resourceId, "drawable")) {
                    if ((changingConfigs & android.content.pm.ActivityInfo.CONFIG_LAYOUT_DIRECTION) == 0) {
                        // If this resource does not vary based on layout direction,
                        // we can put it in all of the preload maps.
                        android.content.res.ResourcesImpl.sPreloadedDrawables[0].put(key, cs);
                        android.content.res.ResourcesImpl.sPreloadedDrawables[1].put(key, cs);
                    } else {
                        // Otherwise, only in the layout dir we loaded it for.
                        android.content.res.ResourcesImpl.sPreloadedDrawables[mConfiguration.getLayoutDirection()].put(key, cs);
                    }
                }
            }
        } else {
            synchronized(mAccessLock) {
                caches.put(key, theme, cs, usesTheme);
            }
        }
    }

    private boolean verifyPreloadConfig(@android.content.pm.ActivityInfo.Config
    int changingConfigurations, @android.content.pm.ActivityInfo.Config
    int allowVarying, @android.annotation.AnyRes
    int resourceId, @android.annotation.Nullable
    java.lang.String name) {
        // We allow preloading of resources even if they vary by font scale (which
        // doesn't impact resource selection) or density (which we handle specially by
        // simply turning off all preloading), as well as any other configs specified
        // by the caller.
        if (((changingConfigurations & (~(android.content.pm.ActivityInfo.CONFIG_FONT_SCALE | android.content.pm.ActivityInfo.CONFIG_DENSITY))) & (~allowVarying)) != 0) {
            java.lang.String resName;
            try {
                resName = getResourceName(resourceId);
            } catch (android.content.res.Resources.NotFoundException e) {
                resName = "?";
            }
            // This should never happen in production, so we should log a
            // warning even if we're not debugging.
            android.util.Log.w(android.content.res.ResourcesImpl.TAG, ((((("Preloaded " + name) + " resource #0x") + java.lang.Integer.toHexString(resourceId)) + " (") + resName) + ") that varies with configuration!!");
            return false;
        }
        if (android.content.res.ResourcesImpl.TRACE_FOR_PRELOAD) {
            java.lang.String resName;
            try {
                resName = getResourceName(resourceId);
            } catch (android.content.res.Resources.NotFoundException e) {
                resName = "?";
            }
            android.util.Log.w(android.content.res.ResourcesImpl.TAG, ((((("Preloading " + name) + " resource #0x") + java.lang.Integer.toHexString(resourceId)) + " (") + resName) + ")");
        }
        return true;
    }

    /**
     * Loads a Drawable from an encoded image stream, or null.
     *
     * This call will handle closing ais.
     */
    @android.annotation.Nullable
    private android.graphics.drawable.Drawable decodeImageDrawable(@android.annotation.NonNull
    android.content.res.AssetManager.AssetInputStream ais, @android.annotation.NonNull
    android.content.res.Resources wrapper, @android.annotation.NonNull
    android.util.TypedValue value) {
        android.graphics.ImageDecoder.Source src = new android.graphics.ImageDecoder.AssetInputStreamSource(ais, wrapper, value);
        try {
            return android.graphics.ImageDecoder.decodeDrawable(src, ( decoder, info, s) -> {
                decoder.setAllocator(android.graphics.ImageDecoder.ALLOCATOR_SOFTWARE);
            });
        } catch (java.io.IOException ioe) {
            // This is okay. This may be something that ImageDecoder does not
            // support, like SVG.
            return null;
        }
    }

    /**
     * Loads a drawable from XML or resources stream.
     *
     * @return Drawable, or null if Drawable cannot be decoded.
     */
    @android.annotation.Nullable
    private android.graphics.drawable.Drawable loadDrawableForCookie(@android.annotation.NonNull
    android.content.res.Resources wrapper, @android.annotation.NonNull
    android.util.TypedValue value, int id, int density) {
        if (value.string == null) {
            throw new android.content.res.Resources.NotFoundException((((("Resource \"" + getResourceName(id)) + "\" (") + java.lang.Integer.toHexString(id)) + ") is not a Drawable (color or path): ") + value);
        }
        final java.lang.String file = toString();
        if (android.content.res.ResourcesImpl.TRACE_FOR_MISS_PRELOAD) {
            // Log only framework resources
            if ((id >>> 24) == 0x1) {
                final java.lang.String name = getResourceName(id);
                if (name != null) {
                    android.util.Log.d(android.content.res.ResourcesImpl.TAG, (((("Loading framework drawable #" + java.lang.Integer.toHexString(id)) + ": ") + name) + " at ") + file);
                }
            }
        }
        // For preload tracing.
        long startTime = 0;
        int startBitmapCount = 0;
        long startBitmapSize = 0;
        int startDrawableCount = 0;
        if (android.content.res.ResourcesImpl.TRACE_FOR_DETAILED_PRELOAD) {
            startTime = java.lang.System.nanoTime();
            startBitmapCount = android.graphics.Bitmap.sPreloadTracingNumInstantiatedBitmaps;
            startBitmapSize = android.graphics.Bitmap.sPreloadTracingTotalBitmapsSize;
            startDrawableCount = android.content.res.ResourcesImpl.sPreloadTracingNumLoadedDrawables;
        }
        if (android.content.res.ResourcesImpl.DEBUG_LOAD) {
            android.util.Log.v(android.content.res.ResourcesImpl.TAG, (("Loading drawable for cookie " + value.assetCookie) + ": ") + file);
        }
        final android.graphics.drawable.Drawable dr;
        android.os.Trace.traceBegin(Trace.TRACE_TAG_RESOURCES, file);
        android.content.res.ResourcesImpl.LookupStack stack = mLookupStack.get();
        try {
            // Perform a linear search to check if we have already referenced this resource before.
            if (stack.contains(id)) {
                throw new java.lang.Exception("Recursive reference in drawable");
            }
            stack.push(id);
            try {
                if (file.endsWith(".xml")) {
                    if (file.startsWith("res/color/")) {
                        dr = loadColorOrXmlDrawable(wrapper, value, id, density, file);
                    } else {
                        dr = loadXmlDrawable(wrapper, value, id, density, file);
                    }
                } else {
                    final java.io.InputStream is = mAssets.openNonAsset(value.assetCookie, file, android.content.res.AssetManager.ACCESS_STREAMING);
                    android.content.res.AssetManager.AssetInputStream ais = ((android.content.res.AssetManager.AssetInputStream) (is));
                    dr = decodeImageDrawable(ais, wrapper, value);
                }
            } finally {
                stack.pop();
            }
        } catch (java.lang.Exception | java.lang.StackOverflowError e) {
            android.os.Trace.traceEnd(Trace.TRACE_TAG_RESOURCES);
            final android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException((("File " + file) + " from drawable resource ID #0x") + java.lang.Integer.toHexString(id));
            rnf.initCause(e);
            throw rnf;
        }
        android.os.Trace.traceEnd(Trace.TRACE_TAG_RESOURCES);
        if (android.content.res.ResourcesImpl.TRACE_FOR_DETAILED_PRELOAD) {
            if ((id >>> 24) == 0x1) {
                final java.lang.String name = getResourceName(id);
                if (name != null) {
                    final long time = java.lang.System.nanoTime() - startTime;
                    final int loadedBitmapCount = android.graphics.Bitmap.sPreloadTracingNumInstantiatedBitmaps - startBitmapCount;
                    final long loadedBitmapSize = android.graphics.Bitmap.sPreloadTracingTotalBitmapsSize - startBitmapSize;
                    final int loadedDrawables = android.content.res.ResourcesImpl.sPreloadTracingNumLoadedDrawables - startDrawableCount;
                    android.content.res.ResourcesImpl.sPreloadTracingNumLoadedDrawables++;
                    final boolean isRoot = android.content.res.android.os.Process.myUid() == 0;
                    android.util.Log.d(android.content.res.ResourcesImpl.TAG_PRELOAD, (((((((((((((((isRoot ? "Preloaded FW drawable #" : "Loaded non-preloaded FW drawable #") + java.lang.Integer.toHexString(id)) + " ") + name) + " ") + file) + " ") + dr.getClass().getCanonicalName()) + " #nested_drawables= ") + loadedDrawables) + " #bitmaps= ") + loadedBitmapCount) + " total_bitmap_size= ") + loadedBitmapSize) + " in[us] ") + (time / 1000));
                }
            }
        }
        return dr;
    }

    private android.graphics.drawable.Drawable loadColorOrXmlDrawable(@android.annotation.NonNull
    android.content.res.Resources wrapper, @android.annotation.NonNull
    android.util.TypedValue value, int id, int density, java.lang.String file) {
        try {
            android.content.res.ColorStateList csl = loadColorStateList(wrapper, value, id, null);
            return new android.graphics.drawable.ColorStateListDrawable(csl);
        } catch (android.content.res.Resources.NotFoundException originalException) {
            // If we fail to load as color, try as normal XML drawable
            try {
                return loadXmlDrawable(wrapper, value, id, density, file);
            } catch (java.lang.Exception ignored) {
                // If fallback also fails, throw the original exception
                throw originalException;
            }
        }
    }

    private android.graphics.drawable.Drawable loadXmlDrawable(@android.annotation.NonNull
    android.content.res.Resources wrapper, @android.annotation.NonNull
    android.util.TypedValue value, int id, int density, java.lang.String file) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        try (android.content.res.XmlResourceParser rp = loadXmlResourceParser(file, id, value.assetCookie, "drawable")) {
            return android.graphics.drawable.Drawable.createFromXmlForDensity(wrapper, rp, density, null);
        }
    }

    /**
     * Loads a font from XML or resources stream.
     */
    @android.annotation.Nullable
    public android.graphics.Typeface loadFont(android.content.res.Resources wrapper, android.util.TypedValue value, int id) {
        if (value.string == null) {
            throw new android.content.res.Resources.NotFoundException((((("Resource \"" + getResourceName(id)) + "\" (") + java.lang.Integer.toHexString(id)) + ") is not a Font: ") + value);
        }
        final java.lang.String file = toString();
        if (!file.startsWith("res/")) {
            return null;
        }
        android.graphics.Typeface cached = android.graphics.Typeface.findFromCache(mAssets, file);
        if (cached != null) {
            return cached;
        }
        if (android.content.res.ResourcesImpl.DEBUG_LOAD) {
            android.util.Log.v(android.content.res.ResourcesImpl.TAG, (("Loading font for cookie " + value.assetCookie) + ": ") + file);
        }
        android.os.Trace.traceBegin(Trace.TRACE_TAG_RESOURCES, file);
        try {
            if (file.endsWith("xml")) {
                final android.content.res.XmlResourceParser rp = loadXmlResourceParser(file, id, value.assetCookie, "font");
                final android.content.res.FontResourcesParser.FamilyResourceEntry familyEntry = android.content.res.FontResourcesParser.parse(rp, wrapper);
                if (familyEntry == null) {
                    return null;
                }
                return android.graphics.Typeface.createFromResources(familyEntry, mAssets, file);
            }
            return /* isAsset */
            new android.graphics.Typeface.Builder(mAssets, file, false, value.assetCookie).build();
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.e(android.content.res.ResourcesImpl.TAG, "Failed to parse xml resource " + file, e);
        } catch (java.io.IOException e) {
            android.util.Log.e(android.content.res.ResourcesImpl.TAG, "Failed to read xml resource " + file, e);
        } finally {
            android.os.Trace.traceEnd(Trace.TRACE_TAG_RESOURCES);
        }
        return null;
    }

    /**
     * Given the value and id, we can get the XML filename as in value.data, based on that, we
     * first try to load CSL from the cache. If not found, try to get from the constant state.
     * Last, parse the XML and generate the CSL.
     */
    @android.annotation.Nullable
    private android.content.res.ComplexColor loadComplexColorFromName(android.content.res.Resources wrapper, android.content.res.Resources.Theme theme, android.util.TypedValue value, int id) {
        final long key = (((long) (value.assetCookie)) << 32) | value.data;
        final android.content.res.ConfigurationBoundResourceCache<android.content.res.ComplexColor> cache = mComplexColorCache;
        android.content.res.ComplexColor complexColor = cache.getInstance(key, wrapper, theme);
        if (complexColor != null) {
            return complexColor;
        }
        final android.content.res.ConstantState<android.content.res.ComplexColor> factory = android.content.res.ResourcesImpl.sPreloadedComplexColors.get(key);
        if (factory != null) {
            complexColor = factory.newInstance(wrapper, theme);
        }
        if (complexColor == null) {
            complexColor = loadComplexColorForCookie(wrapper, value, id, theme);
        }
        if (complexColor != null) {
            complexColor.setBaseChangingConfigurations(value.changingConfigurations);
            if (mPreloading) {
                if (verifyPreloadConfig(complexColor.getChangingConfigurations(), 0, value.resourceId, "color")) {
                    android.content.res.ResourcesImpl.sPreloadedComplexColors.put(key, complexColor.getConstantState());
                }
            } else {
                cache.put(key, theme, complexColor.getConstantState());
            }
        }
        return complexColor;
    }

    @android.annotation.Nullable
    android.content.res.ComplexColor loadComplexColor(android.content.res.Resources wrapper, @android.annotation.NonNull
    android.util.TypedValue value, int id, android.content.res.Resources.Theme theme) {
        if (android.content.res.ResourcesImpl.TRACE_FOR_PRELOAD) {
            // Log only framework resources
            if ((id >>> 24) == 0x1) {
                final java.lang.String name = getResourceName(id);
                if (name != null)
                    android.util.android.util.Log.d("loadComplexColor", name);

            }
        }
        final long key = (((long) (value.assetCookie)) << 32) | value.data;
        // Handle inline color definitions.
        if ((value.type >= android.util.TypedValue.TYPE_FIRST_COLOR_INT) && (value.type <= android.util.TypedValue.TYPE_LAST_COLOR_INT)) {
            return getColorStateListFromInt(value, key);
        }
        final java.lang.String file = toString();
        android.content.res.ComplexColor complexColor;
        if (file.endsWith(".xml")) {
            try {
                complexColor = loadComplexColorFromName(wrapper, theme, value, id);
            } catch (java.lang.Exception e) {
                final android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException((("File " + file) + " from complex color resource ID #0x") + java.lang.Integer.toHexString(id));
                rnf.initCause(e);
                throw rnf;
            }
        } else {
            throw new android.content.res.Resources.NotFoundException(((("File " + file) + " from drawable resource ID #0x") + java.lang.Integer.toHexString(id)) + ": .xml extension required");
        }
        return complexColor;
    }

    @android.annotation.NonNull
    android.content.res.ColorStateList loadColorStateList(android.content.res.Resources wrapper, android.util.TypedValue value, int id, android.content.res.Resources.Theme theme) throws android.content.res.Resources.NotFoundException {
        if (android.content.res.ResourcesImpl.TRACE_FOR_PRELOAD) {
            // Log only framework resources
            if ((id >>> 24) == 0x1) {
                final java.lang.String name = getResourceName(id);
                if (name != null)
                    android.util.android.util.Log.d("PreloadColorStateList", name);

            }
        }
        final long key = (((long) (value.assetCookie)) << 32) | value.data;
        // Handle inline color definitions.
        if ((value.type >= android.util.TypedValue.TYPE_FIRST_COLOR_INT) && (value.type <= android.util.TypedValue.TYPE_LAST_COLOR_INT)) {
            return getColorStateListFromInt(value, key);
        }
        android.content.res.ComplexColor complexColor = loadComplexColorFromName(wrapper, theme, value, id);
        if ((complexColor != null) && (complexColor instanceof android.content.res.ColorStateList)) {
            return ((android.content.res.ColorStateList) (complexColor));
        }
        throw new android.content.res.Resources.NotFoundException("Can't find ColorStateList from drawable resource ID #0x" + java.lang.Integer.toHexString(id));
    }

    @android.annotation.NonNull
    private android.content.res.ColorStateList getColorStateListFromInt(@android.annotation.NonNull
    android.util.TypedValue value, long key) {
        android.content.res.ColorStateList csl;
        final android.content.res.ConstantState<android.content.res.ComplexColor> factory = android.content.res.ResourcesImpl.sPreloadedComplexColors.get(key);
        if (factory != null) {
            return ((android.content.res.ColorStateList) (factory.newInstance()));
        }
        csl = android.content.res.ColorStateList.valueOf(value.data);
        if (mPreloading) {
            if (verifyPreloadConfig(value.changingConfigurations, 0, value.resourceId, "color")) {
                android.content.res.ResourcesImpl.sPreloadedComplexColors.put(key, csl.getConstantState());
            }
        }
        return csl;
    }

    /**
     * Load a ComplexColor based on the XML file content. The result can be a GradientColor or
     * ColorStateList. Note that pure color will be wrapped into a ColorStateList.
     *
     * We deferred the parser creation to this function b/c we need to differentiate b/t gradient
     * and selector tag.
     *
     * @return a ComplexColor (GradientColor or ColorStateList) based on the XML file content, or
    {@code null} if the XML file is neither.
     */
    @android.annotation.NonNull
    private android.content.res.ComplexColor loadComplexColorForCookie(android.content.res.Resources wrapper, android.util.TypedValue value, int id, android.content.res.Resources.Theme theme) {
        if (value.string == null) {
            throw new java.lang.UnsupportedOperationException("Can't convert to ComplexColor: type=0x" + value.type);
        }
        final java.lang.String file = toString();
        if (android.content.res.ResourcesImpl.TRACE_FOR_MISS_PRELOAD) {
            // Log only framework resources
            if ((id >>> 24) == 0x1) {
                final java.lang.String name = getResourceName(id);
                if (name != null) {
                    android.util.Log.d(android.content.res.ResourcesImpl.TAG, (((("Loading framework ComplexColor #" + java.lang.Integer.toHexString(id)) + ": ") + name) + " at ") + file);
                }
            }
        }
        if (android.content.res.ResourcesImpl.DEBUG_LOAD) {
            android.util.Log.v(android.content.res.ResourcesImpl.TAG, (("Loading ComplexColor for cookie " + value.assetCookie) + ": ") + file);
        }
        android.content.res.ComplexColor complexColor = null;
        android.os.Trace.traceBegin(Trace.TRACE_TAG_RESOURCES, file);
        if (file.endsWith(".xml")) {
            try {
                final android.content.res.XmlResourceParser parser = loadXmlResourceParser(file, id, value.assetCookie, "ComplexColor");
                final android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
                int type;
                while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.START_TAG) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
                    // Seek parser to start tag.
                } 
                if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                    throw new org.xmlpull.v1.XmlPullParserException("No start tag found");
                }
                final java.lang.String name = parser.getName();
                if (name.equals("gradient")) {
                    complexColor = android.content.res.GradientColor.createFromXmlInner(wrapper, parser, attrs, theme);
                } else
                    if (name.equals("selector")) {
                        complexColor = android.content.res.ColorStateList.createFromXmlInner(wrapper, parser, attrs, theme);
                    }

                parser.close();
            } catch (java.lang.Exception e) {
                android.os.Trace.traceEnd(Trace.TRACE_TAG_RESOURCES);
                final android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException((("File " + file) + " from ComplexColor resource ID #0x") + java.lang.Integer.toHexString(id));
                rnf.initCause(e);
                throw rnf;
            }
        } else {
            android.os.Trace.traceEnd(Trace.TRACE_TAG_RESOURCES);
            throw new android.content.res.Resources.NotFoundException(((("File " + file) + " from drawable resource ID #0x") + java.lang.Integer.toHexString(id)) + ": .xml extension required");
        }
        android.os.Trace.traceEnd(Trace.TRACE_TAG_RESOURCES);
        return complexColor;
    }

    /**
     * Loads an XML parser for the specified file.
     *
     * @param file
     * 		the path for the XML file to parse
     * @param id
     * 		the resource identifier for the file
     * @param assetCookie
     * 		the asset cookie for the file
     * @param type
     * 		the type of resource (used for logging)
     * @return a parser for the specified XML file
     * @throws NotFoundException
     * 		if the file could not be loaded
     */
    @android.annotation.NonNull
    android.content.res.XmlResourceParser loadXmlResourceParser(@android.annotation.NonNull
    java.lang.String file, @android.annotation.AnyRes
    int id, int assetCookie, @android.annotation.NonNull
    java.lang.String type) throws android.content.res.Resources.NotFoundException {
        if (id != 0) {
            try {
                synchronized(mCachedXmlBlocks) {
                    final int[] cachedXmlBlockCookies = mCachedXmlBlockCookies;
                    final java.lang.String[] cachedXmlBlockFiles = mCachedXmlBlockFiles;
                    final android.content.res.XmlBlock[] cachedXmlBlocks = mCachedXmlBlocks;
                    // First see if this block is in our cache.
                    final int num = cachedXmlBlockFiles.length;
                    for (int i = 0; i < num; i++) {
                        if (((cachedXmlBlockCookies[i] == assetCookie) && (cachedXmlBlockFiles[i] != null)) && cachedXmlBlockFiles[i].equals(file)) {
                            return cachedXmlBlocks[i].newParser(id);
                        }
                    }
                    // Not in the cache, create a new block and put it at
                    // the next slot in the cache.
                    final android.content.res.XmlBlock block = mAssets.openXmlBlockAsset(assetCookie, file);
                    if (block != null) {
                        final int pos = (mLastCachedXmlBlockIndex + 1) % num;
                        mLastCachedXmlBlockIndex = pos;
                        final android.content.res.XmlBlock oldBlock = cachedXmlBlocks[pos];
                        if (oldBlock != null) {
                            oldBlock.close();
                        }
                        cachedXmlBlockCookies[pos] = assetCookie;
                        cachedXmlBlockFiles[pos] = file;
                        cachedXmlBlocks[pos] = block;
                        return block.newParser(id);
                    }
                }
            } catch (java.lang.Exception e) {
                final android.content.res.Resources.NotFoundException rnf = new android.content.res.Resources.NotFoundException((((("File " + file) + " from xml type ") + type) + " resource ID #0x") + java.lang.Integer.toHexString(id));
                rnf.initCause(e);
                throw rnf;
            }
        }
        throw new android.content.res.Resources.NotFoundException((((("File " + file) + " from xml type ") + type) + " resource ID #0x") + java.lang.Integer.toHexString(id));
    }

    /**
     * Start preloading of resource data using this Resources object.  Only
     * for use by the zygote process for loading common system resources.
     * {@hide }
     */
    public final void startPreloading() {
        synchronized(android.content.res.ResourcesImpl.sSync) {
            if (android.content.res.ResourcesImpl.sPreloaded) {
                throw new java.lang.IllegalStateException("Resources already preloaded");
            }
            android.content.res.ResourcesImpl.sPreloaded = true;
            mPreloading = true;
            mConfiguration.densityDpi = android.util.DisplayMetrics.DENSITY_DEVICE;
            updateConfiguration(null, null, null);
            if (android.content.res.ResourcesImpl.TRACE_FOR_DETAILED_PRELOAD) {
                mPreloadTracingPreloadStartTime = android.os.SystemClock.uptimeMillis();
                mPreloadTracingStartBitmapSize = android.graphics.Bitmap.sPreloadTracingTotalBitmapsSize;
                mPreloadTracingStartBitmapCount = android.graphics.Bitmap.sPreloadTracingNumInstantiatedBitmaps;
                android.util.Log.d(android.content.res.ResourcesImpl.TAG_PRELOAD, "Preload starting");
            }
        }
    }

    /**
     * Called by zygote when it is done preloading resources, to change back
     * to normal Resources operation.
     */
    void finishPreloading() {
        if (mPreloading) {
            if (android.content.res.ResourcesImpl.TRACE_FOR_DETAILED_PRELOAD) {
                final long time = android.os.SystemClock.uptimeMillis() - mPreloadTracingPreloadStartTime;
                final long size = android.graphics.Bitmap.sPreloadTracingTotalBitmapsSize - mPreloadTracingStartBitmapSize;
                final long count = android.graphics.Bitmap.sPreloadTracingNumInstantiatedBitmaps - mPreloadTracingStartBitmapCount;
                android.util.Log.d(android.content.res.ResourcesImpl.TAG_PRELOAD, ((((("Preload finished, " + count) + " bitmaps of ") + size) + " bytes in ") + time) + " ms");
            }
            mPreloading = false;
            flushLayoutCache();
        }
    }

    @android.annotation.AnyRes
    static int getAttributeSetSourceResId(@android.annotation.Nullable
    android.util.AttributeSet set) {
        if ((set == null) || (!(set instanceof android.content.res.XmlBlock.Parser))) {
            return android.content.res.Resources.ID_NULL;
        }
        return ((android.content.res.XmlBlock.Parser) (set)).getSourceResId();
    }

    android.util.LongSparseArray<android.graphics.drawable.Drawable.ConstantState> getPreloadedDrawables() {
        return android.content.res.ResourcesImpl.sPreloadedDrawables[0];
    }

    android.content.res.ResourcesImpl.ThemeImpl newThemeImpl() {
        return new android.content.res.ResourcesImpl.ThemeImpl();
    }

    /**
     * Creates a new ThemeImpl which is already set to the given Resources.ThemeKey.
     */
    android.content.res.ResourcesImpl.ThemeImpl newThemeImpl(android.content.res.Resources.ThemeKey key) {
        android.content.res.ResourcesImpl.ThemeImpl impl = new android.content.res.ResourcesImpl.ThemeImpl();
        impl.mKey.setTo(key);
        impl.rebase();
        return impl;
    }

    public class ThemeImpl {
        /**
         * Unique key for the series of styles applied to this theme.
         */
        private final android.content.res.Resources.ThemeKey mKey = new android.content.res.Resources.ThemeKey();

        @java.lang.SuppressWarnings("hiding")
        private final android.content.res.AssetManager mAssets;

        private final long mTheme;

        /**
         * Resource identifier for the theme.
         */
        private int mThemeResId = 0;

        /* package */
        ThemeImpl() {
            mAssets = android.content.res.ResourcesImpl.this.mAssets;
            mTheme = mAssets.createTheme();
        }

        @java.lang.Override
        protected void finalize() throws java.lang.Throwable {
            super.finalize();
            mAssets.releaseTheme(mTheme);
        }

        /* package */
        android.content.res.Resources.ThemeKey getKey() {
            return mKey;
        }

        /* package */
        long getNativeTheme() {
            return mTheme;
        }

        /* package */
        int getAppliedStyleResId() {
            return mThemeResId;
        }

        void applyStyle(int resId, boolean force) {
            synchronized(mKey) {
                mAssets.applyStyleToTheme(mTheme, resId, force);
                mThemeResId = resId;
                mKey.append(resId, force);
            }
        }

        void setTo(android.content.res.ResourcesImpl.ThemeImpl other) {
            synchronized(mKey) {
                synchronized(other.mKey) {
                    mAssets.setThemeTo(mTheme, other.mAssets, other.mTheme);
                    mThemeResId = other.mThemeResId;
                    mKey.setTo(other.getKey());
                }
            }
        }

        @android.annotation.NonNull
        android.content.res.TypedArray obtainStyledAttributes(@android.annotation.NonNull
        android.content.res.Resources.Theme wrapper, android.util.AttributeSet set, @android.annotation.StyleableRes
        int[] attrs, @android.annotation.AttrRes
        int defStyleAttr, @android.annotation.StyleRes
        int defStyleRes) {
            synchronized(mKey) {
                final int len = attrs.length;
                final android.content.res.TypedArray array = android.content.res.TypedArray.obtain(wrapper.getResources(), len);
                // XXX note that for now we only work with compiled XML files.
                // To support generic XML files we will need to manually parse
                // out the attributes from the XML file (applying type information
                // contained in the resources and such).
                final android.content.res.XmlBlock.Parser parser = ((android.content.res.XmlBlock.Parser) (set));
                mAssets.applyStyle(mTheme, defStyleAttr, defStyleRes, parser, attrs, array.mDataAddress, array.mIndicesAddress);
                array.mTheme = wrapper;
                array.mXml = parser;
                return array;
            }
        }

        @android.annotation.NonNull
        android.content.res.TypedArray resolveAttributes(@android.annotation.NonNull
        android.content.res.Resources.Theme wrapper, @android.annotation.NonNull
        int[] values, @android.annotation.NonNull
        int[] attrs) {
            synchronized(mKey) {
                final int len = attrs.length;
                if ((values == null) || (len != values.length)) {
                    throw new java.lang.IllegalArgumentException("Base attribute values must the same length as attrs");
                }
                final android.content.res.TypedArray array = android.content.res.TypedArray.obtain(wrapper.getResources(), len);
                mAssets.resolveAttrs(mTheme, 0, 0, values, attrs, array.mData, array.mIndices);
                array.mTheme = wrapper;
                array.mXml = null;
                return array;
            }
        }

        boolean resolveAttribute(int resid, android.util.TypedValue outValue, boolean resolveRefs) {
            synchronized(mKey) {
                return mAssets.getThemeValue(mTheme, resid, outValue, resolveRefs);
            }
        }

        int[] getAllAttributes() {
            return mAssets.getStyleAttributes(getAppliedStyleResId());
        }

        @android.content.pm.ActivityInfo.Config
        int getChangingConfigurations() {
            synchronized(mKey) {
                @android.content.res.Configuration.NativeConfig
                final int nativeChangingConfig = android.content.res.AssetManager.nativeThemeGetChangingConfigurations(mTheme);
                return android.content.pm.ActivityInfo.activityInfoConfigNativeToJava(nativeChangingConfig);
            }
        }

        public void dump(int priority, java.lang.String tag, java.lang.String prefix) {
            synchronized(mKey) {
                mAssets.dumpTheme(mTheme, priority, tag, prefix);
            }
        }

        java.lang.String[] getTheme() {
            synchronized(mKey) {
                final int N = mKey.mCount;
                final java.lang.String[] themes = new java.lang.String[N * 2];
                for (int i = 0, j = N - 1; i < themes.length; i += 2 , --j) {
                    final int resId = mKey.mResId[j];
                    final boolean forced = mKey.mForce[j];
                    try {
                        themes[i] = getResourceName(resId);
                    } catch (android.content.res.Resources.NotFoundException e) {
                        themes[i] = java.lang.Integer.toHexString(i);
                    }
                    themes[i + 1] = (forced) ? "forced" : "not forced";
                }
                return themes;
            }
        }

        /**
         * Rebases the theme against the parent Resource object's current
         * configuration by re-applying the styles passed to
         * {@link #applyStyle(int, boolean)}.
         */
        void rebase() {
            synchronized(mKey) {
                android.content.res.AssetManager.nativeThemeClear(mTheme);
                // Reapply the same styles in the same order.
                for (int i = 0; i < mKey.mCount; i++) {
                    final int resId = mKey.mResId[i];
                    final boolean force = mKey.mForce[i];
                    mAssets.applyStyleToTheme(mTheme, resId, force);
                }
            }
        }

        /**
         * Returns the ordered list of resource ID that are considered when resolving attribute
         * values when making an equivalent call to
         * {@link #obtainStyledAttributes(Resources.Theme, AttributeSet, int[], int, int)}. The list
         * will include a set of explicit styles ({@code explicitStyleRes} and it will include the
         * default styles ({@code defStyleAttr} and {@code defStyleRes}).
         *
         * @param defStyleAttr
         * 		An attribute in the current theme that contains a
         * 		reference to a style resource that supplies
         * 		defaults values for the TypedArray.  Can be
         * 		0 to not look for defaults.
         * @param defStyleRes
         * 		A resource identifier of a style resource that
         * 		supplies default values for the TypedArray,
         * 		used only if defStyleAttr is 0 or can not be found
         * 		in the theme.  Can be 0 to not look for defaults.
         * @param explicitStyleRes
         * 		A resource identifier of an explicit style resource.
         * @return ordered list of resource ID that are considered when resolving attribute values.
         */
        @android.annotation.Nullable
        public int[] getAttributeResolutionStack(@android.annotation.AttrRes
        int defStyleAttr, @android.annotation.StyleRes
        int defStyleRes, @android.annotation.StyleRes
        int explicitStyleRes) {
            synchronized(mKey) {
                return mAssets.getAttributeResolutionStack(mTheme, defStyleAttr, defStyleRes, explicitStyleRes);
            }
        }
    }

    private static class LookupStack {
        // Pick a reasonable default size for the array, it is grown as needed.
        private int[] mIds = new int[4];

        private int mSize = 0;

        public void push(int id) {
            mIds = com.android.internal.util.GrowingArrayUtils.append(mIds, mSize, id);
            mSize++;
        }

        public boolean contains(int id) {
            for (int i = 0; i < mSize; i++) {
                if (mIds[i] == id) {
                    return true;
                }
            }
            return false;
        }

        public void pop() {
            mSize--;
        }
    }
}

