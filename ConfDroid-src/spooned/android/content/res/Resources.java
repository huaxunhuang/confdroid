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
 * Class for accessing an application's resources.  This sits on top of the
 * asset manager of the application (accessible through {@link #getAssets}) and
 * provides a high-level API for getting typed data from the assets.
 *
 * <p>The Android resource system keeps track of all non-code assets associated with an
 * application. You can use this class to access your application's resources. You can generally
 * acquire the {@link android.content.res.Resources} instance associated with your application
 * with {@link android.content.Context#getResources getResources()}.</p>
 *
 * <p>The Android SDK tools compile your application's resources into the application binary
 * at build time.  To use a resource, you must install it correctly in the source tree (inside
 * your project's {@code res/} directory) and build your application.  As part of the build
 * process, the SDK tools generate symbols for each resource, which you can use in your application
 * code to access the resources.</p>
 *
 * <p>Using application resources makes it easy to update various characteristics of your
 * application without modifying code, and&mdash;by providing sets of alternative
 * resources&mdash;enables you to optimize your application for a variety of device configurations
 * (such as for different languages and screen sizes). This is an important aspect of developing
 * Android applications that are compatible on different types of devices.</p>
 *
 * <p>For more information about using resources, see the documentation about <a
 * href="{@docRoot }guide/topics/resources/index.html">Application Resources</a>.</p>
 */
public class Resources {
    /**
     * The {@code null} resource ID. This denotes an invalid resource ID that is returned by the
     * system when a resource is not found or the value is set to {@code @null} in XML.
     */
    @android.annotation.AnyRes
    public static final int ID_NULL = 0;

    static final java.lang.String TAG = "Resources";

    private static final java.lang.Object sSync = new java.lang.Object();

    // Used by BridgeResources in layoutlib
    @android.annotation.UnsupportedAppUsage
    static android.content.res.Resources mSystem = null;

    @android.annotation.UnsupportedAppUsage
    private android.content.res.ResourcesImpl mResourcesImpl;

    // Pool of TypedArrays targeted to this Resources object.
    @android.annotation.UnsupportedAppUsage
    final android.util.Pools.SynchronizedPool<android.content.res.TypedArray> mTypedArrayPool = new android.util.Pools.SynchronizedPool(5);

    /**
     * Used to inflate drawable objects from XML.
     */
    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.DrawableInflater mDrawableInflater;

    /**
     * Lock object used to protect access to {@link #mTmpValue}.
     */
    private final java.lang.Object mTmpValueLock = new java.lang.Object();

    /**
     * Single-item pool used to minimize TypedValue allocations.
     */
    @android.annotation.UnsupportedAppUsage
    private android.util.TypedValue mTmpValue = new android.util.TypedValue();

    @android.annotation.UnsupportedAppUsage
    final java.lang.ClassLoader mClassLoader;

    /**
     * WeakReferences to Themes that were constructed from this Resources object.
     * We keep track of these in case our underlying implementation is changed, in which case
     * the Themes must also get updated ThemeImpls.
     */
    private final java.util.ArrayList<java.lang.ref.WeakReference<android.content.res.Resources.Theme>> mThemeRefs = new java.util.ArrayList<>();

    /**
     * To avoid leaking WeakReferences to garbage collected Themes on the
     * mThemeRefs list, we flush the list of stale references any time the
     * mThemeRefNextFlushSize is reached.
     */
    private static final int MIN_THEME_REFS_FLUSH_SIZE = 32;

    private int mThemeRefsNextFlushSize = android.content.res.Resources.MIN_THEME_REFS_FLUSH_SIZE;

    /**
     * Returns the most appropriate default theme for the specified target SDK version.
     * <ul>
     * <li>Below API 11: Gingerbread
     * <li>APIs 12 thru 14: Holo
     * <li>APIs 15 thru 23: Device default dark
     * <li>APIs 24 and above: Device default light with dark action bar
     * </ul>
     *
     * @param curTheme
     * 		The current theme, or 0 if not specified.
     * @param targetSdkVersion
     * 		The target SDK version.
     * @return A theme resource identifier
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static int selectDefaultTheme(int curTheme, int targetSdkVersion) {
        return android.content.res.Resources.selectSystemTheme(curTheme, targetSdkVersion, com.android.internal.R.style.Theme, com.android.internal.R.style.Theme_Holo, com.android.internal.R.style.Theme_DeviceDefault, com.android.internal.R.style.Theme_DeviceDefault_Light_DarkActionBar);
    }

    /**
     *
     *
     * @unknown 
     */
    public static int selectSystemTheme(int curTheme, int targetSdkVersion, int orig, int holo, int dark, int deviceDefault) {
        if (curTheme != android.content.res.Resources.ID_NULL) {
            return curTheme;
        }
        if (targetSdkVersion < Build.VERSION_CODES.HONEYCOMB) {
            return orig;
        }
        if (targetSdkVersion < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return holo;
        }
        if (targetSdkVersion < Build.VERSION_CODES.N) {
            return dark;
        }
        return deviceDefault;
    }

    /**
     * Return a global shared Resources object that provides access to only
     * system resources (no application resources), is not configured for the
     * current screen (can not use dimension units, does not change based on
     * orientation, etc), and is not affected by Runtime Resource Overlay.
     */
    public static android.content.res.Resources getSystem() {
        synchronized(android.content.res.Resources.sSync) {
            android.content.res.Resources ret = android.content.res.Resources.mSystem;
            if (ret == null) {
                ret = new android.content.res.Resources();
                android.content.res.Resources.mSystem = ret;
            }
            return ret;
        }
    }

    /**
     * This exception is thrown by the resource APIs when a requested resource
     * can not be found.
     */
    public static class NotFoundException extends java.lang.RuntimeException {
        public NotFoundException() {
        }

        public NotFoundException(java.lang.String name) {
            super(name);
        }

        public NotFoundException(java.lang.String name, java.lang.Exception cause) {
            super(name, cause);
        }
    }

    /**
     * Create a new Resources object on top of an existing set of assets in an
     * AssetManager.
     *
     * @deprecated Resources should not be constructed by apps.
    See {@link android.content.Context#createConfigurationContext(Configuration)}.
     * @param assets
     * 		Previously created AssetManager.
     * @param metrics
     * 		Current display metrics to consider when
     * 		selecting/computing resource values.
     * @param config
     * 		Desired device configuration to consider when
     * 		selecting/computing resource values (optional).
     */
    @java.lang.Deprecated
    public Resources(android.content.res.AssetManager assets, android.util.DisplayMetrics metrics, android.content.res.Configuration config) {
        this(null);
        mResourcesImpl = new android.content.res.ResourcesImpl(assets, metrics, config, new android.view.DisplayAdjustments());
    }

    /**
     * Creates a new Resources object with CompatibilityInfo.
     *
     * @param classLoader
     * 		class loader for the package used to load custom
     * 		resource classes, may be {@code null} to use system
     * 		class loader
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public Resources(@android.annotation.Nullable
    java.lang.ClassLoader classLoader) {
        mClassLoader = (classLoader == null) ? java.lang.ClassLoader.getSystemClassLoader() : classLoader;
    }

    /**
     * Only for creating the System resources.
     */
    @android.annotation.UnsupportedAppUsage
    private Resources() {
        this(null);
        final android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
        metrics.setToDefaults();
        final android.content.res.Configuration config = new android.content.res.Configuration();
        config.setToDefaults();
        mResourcesImpl = new android.content.res.ResourcesImpl(android.content.res.AssetManager.getSystem(), metrics, config, new android.view.DisplayAdjustments());
    }

    /**
     * Set the underlying implementation (containing all the resources and caches)
     * and updates all Theme references to new implementations as well.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void setImpl(android.content.res.ResourcesImpl impl) {
        if (impl == mResourcesImpl) {
            return;
        }
        mResourcesImpl = impl;
        // Create new ThemeImpls that are identical to the ones we have.
        synchronized(mThemeRefs) {
            final int count = mThemeRefs.size();
            for (int i = 0; i < count; i++) {
                java.lang.ref.WeakReference<android.content.res.Resources.Theme> weakThemeRef = mThemeRefs.get(i);
                android.content.res.Resources.Theme theme = (weakThemeRef != null) ? weakThemeRef.get() : null;
                if (theme != null) {
                    theme.setImpl(mResourcesImpl.newThemeImpl(theme.getKey()));
                }
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public android.content.res.ResourcesImpl getImpl() {
        return mResourcesImpl;
    }

    /**
     *
     *
     * @unknown 
     */
    public java.lang.ClassLoader getClassLoader() {
        return mClassLoader;
    }

    /**
     *
     *
     * @return the inflater used to create drawable objects
     * @unknown Pending API finalization.
     */
    @android.annotation.UnsupportedAppUsage
    public final android.graphics.drawable.DrawableInflater getDrawableInflater() {
        if (mDrawableInflater == null) {
            mDrawableInflater = new android.graphics.drawable.DrawableInflater(this, mClassLoader);
        }
        return mDrawableInflater;
    }

    /**
     * Used by AnimatorInflater.
     *
     * @unknown 
     */
    public android.content.res.ConfigurationBoundResourceCache<android.animation.Animator> getAnimatorCache() {
        return mResourcesImpl.getAnimatorCache();
    }

    /**
     * Used by AnimatorInflater.
     *
     * @unknown 
     */
    public android.content.res.ConfigurationBoundResourceCache<android.animation.StateListAnimator> getStateListAnimatorCache() {
        return mResourcesImpl.getStateListAnimatorCache();
    }

    /**
     * Return the string value associated with a particular resource ID.  The
     * returned object will be a String if this is a plain string; it will be
     * some other type of CharSequence if it is styled.
     * {@more }
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @return CharSequence The string data associated with the resource, plus
    possibly styled text information.
     */
    @android.annotation.NonNull
    public java.lang.CharSequence getText(@android.annotation.StringRes
    int id) throws android.content.res.Resources.NotFoundException {
        java.lang.CharSequence res = mResourcesImpl.getAssets().getResourceText(id);
        if (res != null) {
            return res;
        }
        throw new android.content.res.Resources.NotFoundException("String resource ID #0x" + java.lang.Integer.toHexString(id));
    }

    /**
     * Return the Typeface value associated with a particular resource ID.
     * {@more }
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @return Typeface The Typeface data associated with the resource.
     */
    @android.annotation.NonNull
    public android.graphics.Typeface getFont(@android.annotation.FontRes
    int id) throws android.content.res.Resources.NotFoundException {
        final android.util.TypedValue value = obtainTempTypedValue();
        try {
            final android.content.res.ResourcesImpl impl = mResourcesImpl;
            impl.getValue(id, value, true);
            android.graphics.Typeface typeface = impl.loadFont(this, value, id);
            if (typeface != null) {
                return typeface;
            }
        } finally {
            releaseTempTypedValue(value);
        }
        throw new android.content.res.Resources.NotFoundException("Font resource ID #0x" + java.lang.Integer.toHexString(id));
    }

    @android.annotation.NonNull
    android.graphics.Typeface getFont(@android.annotation.NonNull
    android.util.TypedValue value, @android.annotation.FontRes
    int id) throws android.content.res.Resources.NotFoundException {
        return mResourcesImpl.loadFont(this, value, id);
    }

    /**
     *
     *
     * @unknown 
     */
    public void preloadFonts(@android.annotation.ArrayRes
    int id) {
        final android.content.res.TypedArray array = obtainTypedArray(id);
        try {
            final int size = array.length();
            for (int i = 0; i < size; i++) {
                array.getFont(i);
            }
        } finally {
            array.recycle();
        }
    }

    /**
     * Returns the character sequence necessary for grammatically correct pluralization
     * of the given resource ID for the given quantity.
     * Note that the character sequence is selected based solely on grammatical necessity,
     * and that such rules differ between languages. Do not assume you know which string
     * will be returned for a given quantity. See
     * <a href="{@docRoot }guide/topics/resources/string-resource.html#Plurals">String Resources</a>
     * for more detail.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @param quantity
     * 		The number used to get the correct string for the current language's
     * 		plural rules.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @return CharSequence The string data associated with the resource, plus
    possibly styled text information.
     */
    @android.annotation.NonNull
    public java.lang.CharSequence getQuantityText(@android.annotation.PluralsRes
    int id, int quantity) throws android.content.res.Resources.NotFoundException {
        return mResourcesImpl.getQuantityText(id, quantity);
    }

    /**
     * Return the string value associated with a particular resource ID.  It
     * will be stripped of any styled text information.
     * {@more }
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @return String The string data associated with the resource,
    stripped of styled text information.
     */
    @android.annotation.NonNull
    public java.lang.String getString(@android.annotation.StringRes
    int id) throws android.content.res.Resources.NotFoundException {
        return getText(id).toString();
    }

    /**
     * Return the string value associated with a particular resource ID,
     * substituting the format arguments as defined in {@link java.util.Formatter}
     * and {@link java.lang.String#format}. It will be stripped of any styled text
     * information.
     * {@more }
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @param formatArgs
     * 		The format arguments that will be used for substitution.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @return String The string data associated with the resource,
    stripped of styled text information.
     */
    @android.annotation.NonNull
    public java.lang.String getString(@android.annotation.StringRes
    int id, java.lang.Object... formatArgs) throws android.content.res.Resources.NotFoundException {
        final java.lang.String raw = getString(id);
        return java.lang.String.format(mResourcesImpl.getConfiguration().getLocales().get(0), raw, formatArgs);
    }

    /**
     * Formats the string necessary for grammatically correct pluralization
     * of the given resource ID for the given quantity, using the given arguments.
     * Note that the string is selected based solely on grammatical necessity,
     * and that such rules differ between languages. Do not assume you know which string
     * will be returned for a given quantity. See
     * <a href="{@docRoot }guide/topics/resources/string-resource.html#Plurals">String Resources</a>
     * for more detail.
     *
     * <p>Substitution of format arguments works as if using
     * {@link java.util.Formatter} and {@link java.lang.String#format}.
     * The resulting string will be stripped of any styled text information.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @param quantity
     * 		The number used to get the correct string for the current language's
     * 		plural rules.
     * @param formatArgs
     * 		The format arguments that will be used for substitution.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @return String The string data associated with the resource,
    stripped of styled text information.
     */
    @android.annotation.NonNull
    public java.lang.String getQuantityString(@android.annotation.PluralsRes
    int id, int quantity, java.lang.Object... formatArgs) throws android.content.res.Resources.NotFoundException {
        java.lang.String raw = getQuantityText(id, quantity).toString();
        return java.lang.String.format(mResourcesImpl.getConfiguration().getLocales().get(0), raw, formatArgs);
    }

    /**
     * Returns the string necessary for grammatically correct pluralization
     * of the given resource ID for the given quantity.
     * Note that the string is selected based solely on grammatical necessity,
     * and that such rules differ between languages. Do not assume you know which string
     * will be returned for a given quantity. See
     * <a href="{@docRoot }guide/topics/resources/string-resource.html#Plurals">String Resources</a>
     * for more detail.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @param quantity
     * 		The number used to get the correct string for the current language's
     * 		plural rules.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @return String The string data associated with the resource,
    stripped of styled text information.
     */
    @android.annotation.NonNull
    public java.lang.String getQuantityString(@android.annotation.PluralsRes
    int id, int quantity) throws android.content.res.Resources.NotFoundException {
        return getQuantityText(id, quantity).toString();
    }

    /**
     * Return the string value associated with a particular resource ID.  The
     * returned object will be a String if this is a plain string; it will be
     * some other type of CharSequence if it is styled.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @param def
     * 		The default CharSequence to return.
     * @return CharSequence The string data associated with the resource, plus
    possibly styled text information, or def if id is 0 or not found.
     */
    public java.lang.CharSequence getText(@android.annotation.StringRes
    int id, java.lang.CharSequence def) {
        java.lang.CharSequence res = (id != 0) ? mResourcesImpl.getAssets().getResourceText(id) : null;
        return res != null ? res : def;
    }

    /**
     * Return the styled text array associated with a particular resource ID.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @return The styled text array associated with the resource.
     */
    @android.annotation.NonNull
    public java.lang.CharSequence[] getTextArray(@android.annotation.ArrayRes
    int id) throws android.content.res.Resources.NotFoundException {
        java.lang.CharSequence[] res = mResourcesImpl.getAssets().getResourceTextArray(id);
        if (res != null) {
            return res;
        }
        throw new android.content.res.Resources.NotFoundException("Text array resource ID #0x" + java.lang.Integer.toHexString(id));
    }

    /**
     * Return the string array associated with a particular resource ID.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @return The string array associated with the resource.
     */
    @android.annotation.NonNull
    public java.lang.String[] getStringArray(@android.annotation.ArrayRes
    int id) throws android.content.res.Resources.NotFoundException {
        java.lang.String[] res = mResourcesImpl.getAssets().getResourceStringArray(id);
        if (res != null) {
            return res;
        }
        throw new android.content.res.Resources.NotFoundException("String array resource ID #0x" + java.lang.Integer.toHexString(id));
    }

    /**
     * Return the int array associated with a particular resource ID.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @return The int array associated with the resource.
     */
    @android.annotation.NonNull
    public int[] getIntArray(@android.annotation.ArrayRes
    int id) throws android.content.res.Resources.NotFoundException {
        int[] res = mResourcesImpl.getAssets().getResourceIntArray(id);
        if (res != null) {
            return res;
        }
        throw new android.content.res.Resources.NotFoundException("Int array resource ID #0x" + java.lang.Integer.toHexString(id));
    }

    /**
     * Return an array of heterogeneous values.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @return Returns a TypedArray holding an array of the array values.
    Be sure to call {@link TypedArray#recycle() TypedArray.recycle()}
    when done with it.
     */
    @android.annotation.NonNull
    public android.content.res.TypedArray obtainTypedArray(@android.annotation.ArrayRes
    int id) throws android.content.res.Resources.NotFoundException {
        final android.content.res.ResourcesImpl impl = mResourcesImpl;
        int len = impl.getAssets().getResourceArraySize(id);
        if (len < 0) {
            throw new android.content.res.Resources.NotFoundException("Array resource ID #0x" + java.lang.Integer.toHexString(id));
        }
        android.content.res.TypedArray array = android.content.res.TypedArray.obtain(this, len);
        array.mLength = impl.getAssets().getResourceArray(id, array.mData);
        array.mIndices[0] = 0;
        return array;
    }

    /**
     * Retrieve a dimensional for a particular resource ID.  Unit
     * conversions are based on the current {@link DisplayMetrics} associated
     * with the resources.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @return Resource dimension value multiplied by the appropriate metric to convert to pixels.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @see #getDimensionPixelOffset
     * @see #getDimensionPixelSize
     */
    public float getDimension(@android.annotation.DimenRes
    int id) throws android.content.res.Resources.NotFoundException {
        final android.util.TypedValue value = obtainTempTypedValue();
        try {
            final android.content.res.ResourcesImpl impl = mResourcesImpl;
            impl.getValue(id, value, true);
            if (value.type == android.util.TypedValue.TYPE_DIMENSION) {
                return android.util.TypedValue.complexToDimension(value.data, impl.getDisplayMetrics());
            }
            throw new android.content.res.Resources.NotFoundException(((("Resource ID #0x" + java.lang.Integer.toHexString(id)) + " type #0x") + java.lang.Integer.toHexString(value.type)) + " is not valid");
        } finally {
            releaseTempTypedValue(value);
        }
    }

    /**
     * Retrieve a dimensional for a particular resource ID for use
     * as an offset in raw pixels.  This is the same as
     * {@link #getDimension}, except the returned value is converted to
     * integer pixels for you.  An offset conversion involves simply
     * truncating the base value to an integer.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @return Resource dimension value multiplied by the appropriate
    metric and truncated to integer pixels.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @see #getDimension
     * @see #getDimensionPixelSize
     */
    public int getDimensionPixelOffset(@android.annotation.DimenRes
    int id) throws android.content.res.Resources.NotFoundException {
        final android.util.TypedValue value = obtainTempTypedValue();
        try {
            final android.content.res.ResourcesImpl impl = mResourcesImpl;
            impl.getValue(id, value, true);
            if (value.type == android.util.TypedValue.TYPE_DIMENSION) {
                return android.util.TypedValue.complexToDimensionPixelOffset(value.data, impl.getDisplayMetrics());
            }
            throw new android.content.res.Resources.NotFoundException(((("Resource ID #0x" + java.lang.Integer.toHexString(id)) + " type #0x") + java.lang.Integer.toHexString(value.type)) + " is not valid");
        } finally {
            releaseTempTypedValue(value);
        }
    }

    /**
     * Retrieve a dimensional for a particular resource ID for use
     * as a size in raw pixels.  This is the same as
     * {@link #getDimension}, except the returned value is converted to
     * integer pixels for use as a size.  A size conversion involves
     * rounding the base value, and ensuring that a non-zero base value
     * is at least one pixel in size.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @return Resource dimension value multiplied by the appropriate
    metric and truncated to integer pixels.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @see #getDimension
     * @see #getDimensionPixelOffset
     */
    public int getDimensionPixelSize(@android.annotation.DimenRes
    int id) throws android.content.res.Resources.NotFoundException {
        final android.util.TypedValue value = obtainTempTypedValue();
        try {
            final android.content.res.ResourcesImpl impl = mResourcesImpl;
            impl.getValue(id, value, true);
            if (value.type == android.util.TypedValue.TYPE_DIMENSION) {
                return android.util.TypedValue.complexToDimensionPixelSize(value.data, impl.getDisplayMetrics());
            }
            throw new android.content.res.Resources.NotFoundException(((("Resource ID #0x" + java.lang.Integer.toHexString(id)) + " type #0x") + java.lang.Integer.toHexString(value.type)) + " is not valid");
        } finally {
            releaseTempTypedValue(value);
        }
    }

    /**
     * Retrieve a fractional unit for a particular resource ID.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @param base
     * 		The base value of this fraction.  In other words, a
     * 		standard fraction is multiplied by this value.
     * @param pbase
     * 		The parent base value of this fraction.  In other
     * 		words, a parent fraction (nn%p) is multiplied by this
     * 		value.
     * @return Attribute fractional value multiplied by the appropriate
    base value.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     */
    public float getFraction(@android.annotation.FractionRes
    int id, int base, int pbase) {
        final android.util.TypedValue value = obtainTempTypedValue();
        try {
            mResourcesImpl.getValue(id, value, true);
            if (value.type == android.util.TypedValue.TYPE_FRACTION) {
                return android.util.TypedValue.complexToFraction(value.data, base, pbase);
            }
            throw new android.content.res.Resources.NotFoundException(((("Resource ID #0x" + java.lang.Integer.toHexString(id)) + " type #0x") + java.lang.Integer.toHexString(value.type)) + " is not valid");
        } finally {
            releaseTempTypedValue(value);
        }
    }

    /**
     * Return a drawable object associated with a particular resource ID.
     * Various types of objects will be returned depending on the underlying
     * resource -- for example, a solid color, PNG image, scalable image, etc.
     * The Drawable API hides these implementation details.
     *
     * <p class="note"><strong>Note:</strong> Prior to
     * {@link android.os.Build.VERSION_CODES#JELLY_BEAN}, this function
     * would not correctly retrieve the final configuration density when
     * the resource ID passed here is an alias to another Drawable resource.
     * This means that if the density configuration of the alias resource
     * is different than the actual resource, the density of the returned
     * Drawable would be incorrect, resulting in bad scaling. To work
     * around this, you can instead manually resolve the aliased reference
     * by using {@link #getValue(int, TypedValue, boolean)} and passing
     * {@code true} for {@code resolveRefs}. The resulting
     * {@link TypedValue#resourceId} value may be passed to this method.</p>
     *
     * <p class="note"><strong>Note:</strong> To obtain a themed drawable, use
     * {@link android.content.Context#getDrawable(int) Context.getDrawable(int)}
     * or {@link #getDrawable(int, Theme)} passing the desired theme.</p>
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @return Drawable An object that can be used to draw this resource.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does
     * 		not exist.
     * @see #getDrawable(int, Theme)
     * @deprecated Use {@link #getDrawable(int, Theme)} instead.
     */
    @java.lang.Deprecated
    public android.graphics.drawable.Drawable getDrawable(@android.annotation.DrawableRes
    int id) throws android.content.res.Resources.NotFoundException {
        final android.graphics.drawable.Drawable d = getDrawable(id, null);
        if ((d != null) && d.canApplyTheme()) {
            android.util.Log.w(android.content.res.Resources.TAG, ((("Drawable " + getResourceName(id)) + " has unresolved theme ") + "attributes! Consider using Resources.getDrawable(int, Theme) or ") + "Context.getDrawable(int).", new java.lang.RuntimeException());
        }
        return d;
    }

    /**
     * Return a drawable object associated with a particular resource ID and
     * styled for the specified theme. Various types of objects will be
     * returned depending on the underlying resource -- for example, a solid
     * color, PNG image, scalable image, etc.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @param theme
     * 		The theme used to style the drawable attributes, may be {@code null}.
     * @return Drawable An object that can be used to draw this resource.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does
     * 		not exist.
     */
    public android.graphics.drawable.Drawable getDrawable(@android.annotation.DrawableRes
    int id, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws android.content.res.Resources.NotFoundException {
        return getDrawableForDensity(id, 0, theme);
    }

    /**
     * Return a drawable object associated with a particular resource ID for the
     * given screen density in DPI. This will set the drawable's density to be
     * the device's density multiplied by the ratio of actual drawable density
     * to requested density. This allows the drawable to be scaled up to the
     * correct size if needed. Various types of objects will be returned
     * depending on the underlying resource -- for example, a solid color, PNG
     * image, scalable image, etc. The Drawable API hides these implementation
     * details.
     *
     * <p class="note"><strong>Note:</strong> To obtain a themed drawable, use
     * {@link android.content.Context#getDrawable(int) Context.getDrawable(int)}
     * or {@link #getDrawableForDensity(int, int, Theme)} passing the desired
     * theme.</p>
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt tool.
     * 		This integer encodes the package, type, and resource entry.
     * 		The value 0 is an invalid identifier.
     * @param density
     * 		the desired screen density indicated by the resource as
     * 		found in {@link DisplayMetrics}. A value of 0 means to use the
     * 		density returned from {@link #getConfiguration()}.
     * 		This is equivalent to calling {@link #getDrawable(int)}.
     * @return Drawable An object that can be used to draw this resource.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does
     * 		not exist.
     * @see #getDrawableForDensity(int, int, Theme)
     * @deprecated Use {@link #getDrawableForDensity(int, int, Theme)} instead.
     */
    @android.annotation.Nullable
    @java.lang.Deprecated
    public android.graphics.drawable.Drawable getDrawableForDensity(@android.annotation.DrawableRes
    int id, int density) throws android.content.res.Resources.NotFoundException {
        return getDrawableForDensity(id, density, null);
    }

    /**
     * Return a drawable object associated with a particular resource ID for the
     * given screen density in DPI and styled for the specified theme.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt tool.
     * 		This integer encodes the package, type, and resource entry.
     * 		The value 0 is an invalid identifier.
     * @param density
     * 		The desired screen density indicated by the resource as
     * 		found in {@link DisplayMetrics}. A value of 0 means to use the
     * 		density returned from {@link #getConfiguration()}.
     * 		This is equivalent to calling {@link #getDrawable(int, Theme)}.
     * @param theme
     * 		The theme used to style the drawable attributes, may be {@code null} if the
     * 		drawable cannot be decoded.
     * @return Drawable An object that can be used to draw this resource.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does
     * 		not exist.
     */
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getDrawableForDensity(@android.annotation.DrawableRes
    int id, int density, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) {
        final android.util.TypedValue value = obtainTempTypedValue();
        try {
            final android.content.res.ResourcesImpl impl = mResourcesImpl;
            impl.getValueForDensity(id, density, value, true);
            return impl.loadDrawable(this, value, id, density, theme);
        } finally {
            releaseTempTypedValue(value);
        }
    }

    @android.annotation.NonNull
    @android.annotation.UnsupportedAppUsage
    android.graphics.drawable.Drawable loadDrawable(@android.annotation.NonNull
    android.util.TypedValue value, int id, int density, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws android.content.res.Resources.NotFoundException {
        return mResourcesImpl.loadDrawable(this, value, id, density, theme);
    }

    /**
     * Return a movie object associated with the particular resource ID.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @deprecated Prefer {@link android.graphics.drawable.AnimatedImageDrawable}.
     */
    @java.lang.Deprecated
    public android.graphics.Movie getMovie(@android.annotation.RawRes
    int id) throws android.content.res.Resources.NotFoundException {
        final java.io.InputStream is = openRawResource(id);
        final android.graphics.Movie movie = android.graphics.Movie.decodeStream(is);
        try {
            is.close();
        } catch (java.io.IOException e) {
            // No one cares.
        }
        return movie;
    }

    /**
     * Returns a color integer associated with a particular resource ID. If the
     * resource holds a complex {@link ColorStateList}, then the default color
     * from the set is returned.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does
     * 		not exist.
     * @return A single color value in the form 0xAARRGGBB.
     * @deprecated Use {@link #getColor(int, Theme)} instead.
     */
    @android.annotation.ColorInt
    @java.lang.Deprecated
    public int getColor(@android.annotation.ColorRes
    int id) throws android.content.res.Resources.NotFoundException {
        return getColor(id, null);
    }

    /**
     * Returns a themed color integer associated with a particular resource ID.
     * If the resource holds a complex {@link ColorStateList}, then the default
     * color from the set is returned.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @param theme
     * 		The theme used to style the color attributes, may be
     * 		{@code null}.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does
     * 		not exist.
     * @return A single color value in the form 0xAARRGGBB.
     */
    @android.annotation.ColorInt
    public int getColor(@android.annotation.ColorRes
    int id, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws android.content.res.Resources.NotFoundException {
        final android.util.TypedValue value = obtainTempTypedValue();
        try {
            final android.content.res.ResourcesImpl impl = mResourcesImpl;
            impl.getValue(id, value, true);
            if ((value.type >= android.util.TypedValue.TYPE_FIRST_INT) && (value.type <= android.util.TypedValue.TYPE_LAST_INT)) {
                return value.data;
            } else
                if (value.type != android.util.TypedValue.TYPE_STRING) {
                    throw new android.content.res.Resources.NotFoundException(((("Resource ID #0x" + java.lang.Integer.toHexString(id)) + " type #0x") + java.lang.Integer.toHexString(value.type)) + " is not valid");
                }

            final android.content.res.ColorStateList csl = impl.loadColorStateList(this, value, id, theme);
            return csl.getDefaultColor();
        } finally {
            releaseTempTypedValue(value);
        }
    }

    /**
     * Returns a color state list associated with a particular resource ID. The
     * resource may contain either a single raw color value or a complex
     * {@link ColorStateList} holding multiple possible colors.
     *
     * @param id
     * 		The desired resource identifier of a {@link ColorStateList},
     * 		as generated by the aapt tool. This integer encodes the
     * 		package, type, and resource entry. The value 0 is an invalid
     * 		identifier.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does
     * 		not exist.
     * @return A ColorStateList object containing either a single solid color
    or multiple colors that can be selected based on a state.
     * @deprecated Use {@link #getColorStateList(int, Theme)} instead.
     */
    @android.annotation.NonNull
    @java.lang.Deprecated
    public android.content.res.ColorStateList getColorStateList(@android.annotation.ColorRes
    int id) throws android.content.res.Resources.NotFoundException {
        final android.content.res.ColorStateList csl = getColorStateList(id, null);
        if ((csl != null) && csl.canApplyTheme()) {
            android.util.Log.w(android.content.res.Resources.TAG, (((("ColorStateList " + getResourceName(id)) + " has ") + "unresolved theme attributes! Consider using ") + "Resources.getColorStateList(int, Theme) or ") + "Context.getColorStateList(int).", new java.lang.RuntimeException());
        }
        return csl;
    }

    /**
     * Returns a themed color state list associated with a particular resource
     * ID. The resource may contain either a single raw color value or a
     * complex {@link ColorStateList} holding multiple possible colors.
     *
     * @param id
     * 		The desired resource identifier of a {@link ColorStateList},
     * 		as generated by the aapt tool. This integer encodes the
     * 		package, type, and resource entry. The value 0 is an invalid
     * 		identifier.
     * @param theme
     * 		The theme used to style the color attributes, may be
     * 		{@code null}.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does
     * 		not exist.
     * @return A themed ColorStateList object containing either a single solid
    color or multiple colors that can be selected based on a state.
     */
    @android.annotation.NonNull
    public android.content.res.ColorStateList getColorStateList(@android.annotation.ColorRes
    int id, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws android.content.res.Resources.NotFoundException {
        final android.util.TypedValue value = obtainTempTypedValue();
        try {
            final android.content.res.ResourcesImpl impl = mResourcesImpl;
            impl.getValue(id, value, true);
            return impl.loadColorStateList(this, value, id, theme);
        } finally {
            releaseTempTypedValue(value);
        }
    }

    @android.annotation.NonNull
    android.content.res.ColorStateList loadColorStateList(@android.annotation.NonNull
    android.util.TypedValue value, int id, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws android.content.res.Resources.NotFoundException {
        return mResourcesImpl.loadColorStateList(this, value, id, theme);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public android.content.res.ComplexColor loadComplexColor(@android.annotation.NonNull
    android.util.TypedValue value, int id, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) {
        return mResourcesImpl.loadComplexColor(this, value, id, theme);
    }

    /**
     * Return a boolean associated with a particular resource ID.  This can be
     * used with any integral resource value, and will return true if it is
     * non-zero.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @return Returns the boolean value contained in the resource.
     */
    public boolean getBoolean(@android.annotation.BoolRes
    int id) throws android.content.res.Resources.NotFoundException {
        final android.util.TypedValue value = obtainTempTypedValue();
        try {
            mResourcesImpl.getValue(id, value, true);
            if ((value.type >= android.util.TypedValue.TYPE_FIRST_INT) && (value.type <= android.util.TypedValue.TYPE_LAST_INT)) {
                return value.data != 0;
            }
            throw new android.content.res.Resources.NotFoundException(((("Resource ID #0x" + java.lang.Integer.toHexString(id)) + " type #0x") + java.lang.Integer.toHexString(value.type)) + " is not valid");
        } finally {
            releaseTempTypedValue(value);
        }
    }

    /**
     * Return an integer associated with a particular resource ID.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @return Returns the integer value contained in the resource.
     */
    public int getInteger(@android.annotation.IntegerRes
    int id) throws android.content.res.Resources.NotFoundException {
        final android.util.TypedValue value = obtainTempTypedValue();
        try {
            mResourcesImpl.getValue(id, value, true);
            if ((value.type >= android.util.TypedValue.TYPE_FIRST_INT) && (value.type <= android.util.TypedValue.TYPE_LAST_INT)) {
                return value.data;
            }
            throw new android.content.res.Resources.NotFoundException(((("Resource ID #0x" + java.lang.Integer.toHexString(id)) + " type #0x") + java.lang.Integer.toHexString(value.type)) + " is not valid");
        } finally {
            releaseTempTypedValue(value);
        }
    }

    /**
     * Retrieve a floating-point value for a particular resource ID.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @return Returns the floating-point value contained in the resource.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does
     * 		not exist or is not a floating-point value.
     */
    public float getFloat(@android.annotation.DimenRes
    int id) {
        final android.util.TypedValue value = obtainTempTypedValue();
        try {
            mResourcesImpl.getValue(id, value, true);
            if (value.type == android.util.TypedValue.TYPE_FLOAT) {
                return value.getFloat();
            }
            throw new android.content.res.Resources.NotFoundException(((("Resource ID #0x" + java.lang.Integer.toHexString(id)) + " type #0x") + java.lang.Integer.toHexString(value.type)) + " is not valid");
        } finally {
            releaseTempTypedValue(value);
        }
    }

    /**
     * Return an XmlResourceParser through which you can read a view layout
     * description for the given resource ID.  This parser has limited
     * functionality -- in particular, you can't change its input, and only
     * the high-level events are available.
     *
     * <p>This function is really a simple wrapper for calling
     * {@link #getXml} with a layout resource.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @return A new parser object through which you can read
    the XML data.
     * @see #getXml
     */
    @android.annotation.NonNull
    public android.content.res.XmlResourceParser getLayout(@android.annotation.LayoutRes
    int id) throws android.content.res.Resources.NotFoundException {
        return loadXmlResourceParser(id, "layout");
    }

    /**
     * Return an XmlResourceParser through which you can read an animation
     * description for the given resource ID.  This parser has limited
     * functionality -- in particular, you can't change its input, and only
     * the high-level events are available.
     *
     * <p>This function is really a simple wrapper for calling
     * {@link #getXml} with an animation resource.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @return A new parser object through which you can read
    the XML data.
     * @see #getXml
     */
    @android.annotation.NonNull
    public android.content.res.XmlResourceParser getAnimation(@android.annotation.AnimatorRes
    @android.annotation.AnimRes
    int id) throws android.content.res.Resources.NotFoundException {
        return loadXmlResourceParser(id, "anim");
    }

    /**
     * Return an XmlResourceParser through which you can read a generic XML
     * resource for the given resource ID.
     *
     * <p>The XmlPullParser implementation returned here has some limited
     * functionality.  In particular, you can't change its input, and only
     * high-level parsing events are available (since the document was
     * pre-parsed for you at build time, which involved merging text and
     * stripping comments).
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @return A new parser object through which you can read
    the XML data.
     * @see android.util.AttributeSet
     */
    @android.annotation.NonNull
    public android.content.res.XmlResourceParser getXml(@android.annotation.XmlRes
    int id) throws android.content.res.Resources.NotFoundException {
        return loadXmlResourceParser(id, "xml");
    }

    /**
     * Open a data stream for reading a raw resource.  This can only be used
     * with resources whose value is the name of an asset files -- that is, it can be
     * used to open drawable, sound, and raw resources; it will fail on string
     * and color resources.
     *
     * @param id
     * 		The resource identifier to open, as generated by the aapt tool.
     * @return InputStream Access to the resource data.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     */
    @android.annotation.NonNull
    public java.io.InputStream openRawResource(@android.annotation.RawRes
    int id) throws android.content.res.Resources.NotFoundException {
        final android.util.TypedValue value = obtainTempTypedValue();
        try {
            return openRawResource(id, value);
        } finally {
            releaseTempTypedValue(value);
        }
    }

    /**
     * Returns a TypedValue suitable for temporary use. The obtained TypedValue
     * should be released using {@link #releaseTempTypedValue(TypedValue)}.
     *
     * @return a typed value suitable for temporary use
     */
    private android.util.TypedValue obtainTempTypedValue() {
        android.util.TypedValue tmpValue = null;
        synchronized(mTmpValueLock) {
            if (mTmpValue != null) {
                tmpValue = mTmpValue;
                mTmpValue = null;
            }
        }
        if (tmpValue == null) {
            return new android.util.TypedValue();
        }
        return tmpValue;
    }

    /**
     * Returns a TypedValue to the pool. After calling this method, the
     * specified TypedValue should no longer be accessed.
     *
     * @param value
     * 		the typed value to return to the pool
     */
    private void releaseTempTypedValue(android.util.TypedValue value) {
        synchronized(mTmpValueLock) {
            if (mTmpValue == null) {
                mTmpValue = value;
            }
        }
    }

    /**
     * Open a data stream for reading a raw resource.  This can only be used
     * with resources whose value is the name of an asset file -- that is, it can be
     * used to open drawable, sound, and raw resources; it will fail on string
     * and color resources.
     *
     * @param id
     * 		The resource identifier to open, as generated by the aapt tool.
     * @param value
     * 		The TypedValue object to hold the resource information.
     * @return InputStream Access to the resource data.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     */
    @android.annotation.NonNull
    public java.io.InputStream openRawResource(@android.annotation.RawRes
    int id, android.util.TypedValue value) throws android.content.res.Resources.NotFoundException {
        return mResourcesImpl.openRawResource(id, value);
    }

    /**
     * Open a file descriptor for reading a raw resource.  This can only be used
     * with resources whose value is the name of an asset files -- that is, it can be
     * used to open drawable, sound, and raw resources; it will fail on string
     * and color resources.
     *
     * <p>This function only works for resources that are stored in the package
     * as uncompressed data, which typically includes things like mp3 files
     * and png images.
     *
     * @param id
     * 		The resource identifier to open, as generated by the aapt tool.
     * @return AssetFileDescriptor A new file descriptor you can use to read
    the resource.  This includes the file descriptor itself, as well as the
    offset and length of data where the resource appears in the file.  A
    null is returned if the file exists but is compressed.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     */
    public android.content.res.AssetFileDescriptor openRawResourceFd(@android.annotation.RawRes
    int id) throws android.content.res.Resources.NotFoundException {
        final android.util.TypedValue value = obtainTempTypedValue();
        try {
            return mResourcesImpl.openRawResourceFd(id, value);
        } finally {
            releaseTempTypedValue(value);
        }
    }

    /**
     * Return the raw data associated with a particular resource ID.
     *
     * @param id
     * 		The desired resource identifier, as generated by the aapt
     * 		tool. This integer encodes the package, type, and resource
     * 		entry. The value 0 is an invalid identifier.
     * @param outValue
     * 		Object in which to place the resource data.
     * @param resolveRefs
     * 		If true, a resource that is a reference to another
     * 		resource will be followed so that you receive the
     * 		actual final resource data.  If false, the TypedValue
     * 		will be filled in with the reference itself.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     */
    public void getValue(@android.annotation.AnyRes
    int id, android.util.TypedValue outValue, boolean resolveRefs) throws android.content.res.Resources.NotFoundException {
        mResourcesImpl.getValue(id, outValue, resolveRefs);
    }

    /**
     * Get the raw value associated with a resource with associated density.
     *
     * @param id
     * 		resource identifier
     * @param density
     * 		density in DPI
     * @param resolveRefs
     * 		If true, a resource that is a reference to another
     * 		resource will be followed so that you receive the actual final
     * 		resource data. If false, the TypedValue will be filled in with
     * 		the reference itself.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does
     * 		not exist.
     * @see #getValue(String, TypedValue, boolean)
     */
    public void getValueForDensity(@android.annotation.AnyRes
    int id, int density, android.util.TypedValue outValue, boolean resolveRefs) throws android.content.res.Resources.NotFoundException {
        mResourcesImpl.getValueForDensity(id, density, outValue, resolveRefs);
    }

    /**
     * Return the raw data associated with a particular resource ID.
     * See getIdentifier() for information on how names are mapped to resource
     * IDs, and getString(int) for information on how string resources are
     * retrieved.
     *
     * <p>Note: use of this function is discouraged.  It is much more
     * efficient to retrieve resources by identifier than by name.
     *
     * @param name
     * 		The name of the desired resource.  This is passed to
     * 		getIdentifier() with a default type of "string".
     * @param outValue
     * 		Object in which to place the resource data.
     * @param resolveRefs
     * 		If true, a resource that is a reference to another
     * 		resource will be followed so that you receive the
     * 		actual final resource data.  If false, the TypedValue
     * 		will be filled in with the reference itself.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     */
    public void getValue(java.lang.String name, android.util.TypedValue outValue, boolean resolveRefs) throws android.content.res.Resources.NotFoundException {
        mResourcesImpl.getValue(name, outValue, resolveRefs);
    }

    /**
     * Returns the resource ID of the resource that was used to create this AttributeSet.
     *
     * @param set
     * 		AttributeSet for which we want to find the source.
     * @return The resource ID for the source that is backing the given AttributeSet or
    {@link Resources#ID_NULL} if the AttributeSet is {@code null}.
     */
    @android.annotation.AnyRes
    public static int getAttributeSetSourceResId(@android.annotation.Nullable
    android.util.AttributeSet set) {
        return android.content.res.ResourcesImpl.getAttributeSetSourceResId(set);
    }

    /**
     * This class holds the current attribute values for a particular theme.
     * In other words, a Theme is a set of values for resource attributes;
     * these are used in conjunction with {@link TypedArray}
     * to resolve the final value for an attribute.
     *
     * <p>The Theme's attributes come into play in two ways: (1) a styled
     * attribute can explicit reference a value in the theme through the
     * "?themeAttribute" syntax; (2) if no value has been defined for a
     * particular styled attribute, as a last resort we will try to find that
     * attribute's value in the Theme.
     *
     * <p>You will normally use the {@link #obtainStyledAttributes} APIs to
     * retrieve XML attributes with style and theme information applied.
     */
    public final class Theme {
        @android.annotation.UnsupportedAppUsage
        private android.content.res.ResourcesImpl.ThemeImpl mThemeImpl;

        private Theme() {
        }

        void setImpl(android.content.res.ResourcesImpl.ThemeImpl impl) {
            mThemeImpl = impl;
        }

        /**
         * Place new attribute values into the theme.  The style resource
         * specified by <var>resid</var> will be retrieved from this Theme's
         * resources, its values placed into the Theme object.
         *
         * <p>The semantics of this function depends on the <var>force</var>
         * argument:  If false, only values that are not already defined in
         * the theme will be copied from the system resource; otherwise, if
         * any of the style's attributes are already defined in the theme, the
         * current values in the theme will be overwritten.
         *
         * @param resId
         * 		The resource ID of a style resource from which to
         * 		obtain attribute values.
         * @param force
         * 		If true, values in the style resource will always be
         * 		used in the theme; otherwise, they will only be used
         * 		if not already defined in the theme.
         */
        public void applyStyle(int resId, boolean force) {
            mThemeImpl.applyStyle(resId, force);
        }

        /**
         * Set this theme to hold the same contents as the theme
         * <var>other</var>.  If both of these themes are from the same
         * Resources object, they will be identical after this function
         * returns.  If they are from different Resources, only the resources
         * they have in common will be set in this theme.
         *
         * @param other
         * 		The existing Theme to copy from.
         */
        public void setTo(android.content.res.Resources.Theme other) {
            mThemeImpl.setTo(other.mThemeImpl);
        }

        /**
         * Return a TypedArray holding the values defined by
         * <var>Theme</var> which are listed in <var>attrs</var>.
         *
         * <p>Be sure to call {@link TypedArray#recycle() TypedArray.recycle()} when you are done
         * with the array.
         *
         * @param attrs
         * 		The desired attributes. These attribute IDs must be sorted in ascending
         * 		order.
         * @throws NotFoundException
         * 		Throws NotFoundException if the given ID does not exist.
         * @return Returns a TypedArray holding an array of the attribute values.
        Be sure to call {@link TypedArray#recycle() TypedArray.recycle()}
        when done with it.
         * @see Resources#obtainAttributes
         * @see #obtainStyledAttributes(int, int[])
         * @see #obtainStyledAttributes(AttributeSet, int[], int, int)
         */
        @android.annotation.NonNull
        public android.content.res.TypedArray obtainStyledAttributes(@android.annotation.NonNull
        @android.annotation.StyleableRes
        int[] attrs) {
            return mThemeImpl.obtainStyledAttributes(this, null, attrs, 0, 0);
        }

        /**
         * Return a TypedArray holding the values defined by the style
         * resource <var>resid</var> which are listed in <var>attrs</var>.
         *
         * <p>Be sure to call {@link TypedArray#recycle() TypedArray.recycle()} when you are done
         * with the array.
         *
         * @param resId
         * 		The desired style resource.
         * @param attrs
         * 		The desired attributes in the style. These attribute IDs must be sorted in
         * 		ascending order.
         * @throws NotFoundException
         * 		Throws NotFoundException if the given ID does not exist.
         * @return Returns a TypedArray holding an array of the attribute values.
        Be sure to call {@link TypedArray#recycle() TypedArray.recycle()}
        when done with it.
         * @see Resources#obtainAttributes
         * @see #obtainStyledAttributes(int[])
         * @see #obtainStyledAttributes(AttributeSet, int[], int, int)
         */
        @android.annotation.NonNull
        public android.content.res.TypedArray obtainStyledAttributes(@android.annotation.StyleRes
        int resId, @android.annotation.NonNull
        @android.annotation.StyleableRes
        int[] attrs) throws android.content.res.Resources.NotFoundException {
            return mThemeImpl.obtainStyledAttributes(this, null, attrs, 0, resId);
        }

        /**
         * Return a TypedArray holding the attribute values in
         * <var>set</var>
         * that are listed in <var>attrs</var>.  In addition, if the given
         * AttributeSet specifies a style class (through the "style" attribute),
         * that style will be applied on top of the base attributes it defines.
         *
         * <p>Be sure to call {@link TypedArray#recycle() TypedArray.recycle()} when you are done
         * with the array.
         *
         * <p>When determining the final value of a particular attribute, there
         * are four inputs that come into play:</p>
         *
         * <ol>
         *     <li> Any attribute values in the given AttributeSet.
         *     <li> The style resource specified in the AttributeSet (named
         *     "style").
         *     <li> The default style specified by <var>defStyleAttr</var> and
         *     <var>defStyleRes</var>
         *     <li> The base values in this theme.
         * </ol>
         *
         * <p>Each of these inputs is considered in-order, with the first listed
         * taking precedence over the following ones.  In other words, if in the
         * AttributeSet you have supplied <code>&lt;Button
         * textColor="#ff000000"&gt;</code>, then the button's text will
         * <em>always</em> be black, regardless of what is specified in any of
         * the styles.
         *
         * @param set
         * 		The base set of attribute values.  May be null.
         * @param attrs
         * 		The desired attributes to be retrieved. These attribute IDs must be sorted
         * 		in ascending order.
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
         * @return Returns a TypedArray holding an array of the attribute values.
        Be sure to call {@link TypedArray#recycle() TypedArray.recycle()}
        when done with it.
         * @see Resources#obtainAttributes
         * @see #obtainStyledAttributes(int[])
         * @see #obtainStyledAttributes(int, int[])
         */
        @android.annotation.NonNull
        public android.content.res.TypedArray obtainStyledAttributes(@android.annotation.Nullable
        android.util.AttributeSet set, @android.annotation.NonNull
        @android.annotation.StyleableRes
        int[] attrs, @android.annotation.AttrRes
        int defStyleAttr, @android.annotation.StyleRes
        int defStyleRes) {
            return mThemeImpl.obtainStyledAttributes(this, set, attrs, defStyleAttr, defStyleRes);
        }

        /**
         * Retrieve the values for a set of attributes in the Theme. The
         * contents of the typed array are ultimately filled in by
         * {@link Resources#getValue}.
         *
         * @param values
         * 		The base set of attribute values, must be equal in
         * 		length to {@code attrs}. All values must be of type
         * 		{@link TypedValue#TYPE_ATTRIBUTE}.
         * @param attrs
         * 		The desired attributes to be retrieved. These attribute IDs must be sorted
         * 		in ascending order.
         * @return Returns a TypedArray holding an array of the attribute
        values. Be sure to call {@link TypedArray#recycle()}
        when done with it.
         * @unknown 
         */
        @android.annotation.NonNull
        @android.annotation.UnsupportedAppUsage
        public android.content.res.TypedArray resolveAttributes(@android.annotation.NonNull
        int[] values, @android.annotation.NonNull
        int[] attrs) {
            return mThemeImpl.resolveAttributes(this, values, attrs);
        }

        /**
         * Retrieve the value of an attribute in the Theme.  The contents of
         * <var>outValue</var> are ultimately filled in by
         * {@link Resources#getValue}.
         *
         * @param resid
         * 		The resource identifier of the desired theme
         * 		attribute.
         * @param outValue
         * 		Filled in with the ultimate resource value supplied
         * 		by the attribute.
         * @param resolveRefs
         * 		If true, resource references will be walked; if
         * 		false, <var>outValue</var> may be a
         * 		TYPE_REFERENCE.  In either case, it will never
         * 		be a TYPE_ATTRIBUTE.
         * @return boolean Returns true if the attribute was found and
        <var>outValue</var> is valid, else false.
         */
        public boolean resolveAttribute(int resid, android.util.TypedValue outValue, boolean resolveRefs) {
            return mThemeImpl.resolveAttribute(resid, outValue, resolveRefs);
        }

        /**
         * Gets all of the attribute ids associated with this {@link Theme}. For debugging only.
         *
         * @return The int array containing attribute ids associated with this {@link Theme}.
         * @unknown 
         */
        public int[] getAllAttributes() {
            return mThemeImpl.getAllAttributes();
        }

        /**
         * Returns the resources to which this theme belongs.
         *
         * @return Resources to which this theme belongs.
         */
        public android.content.res.Resources getResources() {
            return android.content.res.Resources.this;
        }

        /**
         * Return a drawable object associated with a particular resource ID
         * and styled for the Theme.
         *
         * @param id
         * 		The desired resource identifier, as generated by the aapt
         * 		tool. This integer encodes the package, type, and resource
         * 		entry. The value 0 is an invalid identifier.
         * @return Drawable An object that can be used to draw this resource.
         * @throws NotFoundException
         * 		Throws NotFoundException if the given ID
         * 		does not exist.
         */
        public android.graphics.drawable.Drawable getDrawable(@android.annotation.DrawableRes
        int id) throws android.content.res.Resources.NotFoundException {
            return android.content.res.Resources.this.getDrawable(id, this);
        }

        /**
         * Returns a bit mask of configuration changes that will impact this
         * theme (and thus require completely reloading it).
         *
         * @return a bit mask of configuration changes, as defined by
        {@link ActivityInfo}
         * @see ActivityInfo
         */
        @android.content.pm.ActivityInfo.Config
        public int getChangingConfigurations() {
            return mThemeImpl.getChangingConfigurations();
        }

        /**
         * Print contents of this theme out to the log.  For debugging only.
         *
         * @param priority
         * 		The log priority to use.
         * @param tag
         * 		The log tag to use.
         * @param prefix
         * 		Text to prefix each line printed.
         */
        public void dump(int priority, java.lang.String tag, java.lang.String prefix) {
            mThemeImpl.dump(priority, tag, prefix);
        }

        // Needed by layoutlib.
        /* package */
        long getNativeTheme() {
            return mThemeImpl.getNativeTheme();
        }

        /* package */
        int getAppliedStyleResId() {
            return mThemeImpl.getAppliedStyleResId();
        }

        /**
         *
         *
         * @unknown 
         */
        public android.content.res.Resources.ThemeKey getKey() {
            return mThemeImpl.getKey();
        }

        private java.lang.String getResourceNameFromHexString(java.lang.String hexString) {
            return getResourceName(java.lang.Integer.parseInt(hexString, 16));
        }

        /**
         * Parses {@link #getKey()} and returns a String array that holds pairs of
         * adjacent Theme data: resource name followed by whether or not it was
         * forced, as specified by {@link #applyStyle(int, boolean)}.
         *
         * @unknown 
         */
        @android.view.ViewDebug.ExportedProperty(category = "theme", hasAdjacentMapping = true)
        public java.lang.String[] getTheme() {
            return mThemeImpl.getTheme();
        }

        /**
         *
         *
         * @unknown 
         */
        public void encode(@android.annotation.NonNull
        android.view.ViewHierarchyEncoder encoder) {
            encoder.beginObject(this);
            final java.lang.String[] properties = getTheme();
            for (int i = 0; i < properties.length; i += 2) {
                encoder.addProperty(properties[i], properties[i + 1]);
            }
            encoder.endObject();
        }

        /**
         * Rebases the theme against the parent Resource object's current
         * configuration by re-applying the styles passed to
         * {@link #applyStyle(int, boolean)}.
         */
        public void rebase() {
            mThemeImpl.rebase();
        }

        /**
         * Returns the resource ID for the style specified using {@code style="..."} in the
         * {@link AttributeSet}'s backing XML element or {@link Resources#ID_NULL} otherwise if not
         * specified or otherwise not applicable.
         * <p>
         * Each {@link android.view.View} can have an explicit style specified in the layout file.
         * This style is used first during the {@link android.view.View} attribute resolution, then
         * if an attribute is not defined there the resource system looks at default style and theme
         * as fallbacks.
         *
         * @param set
         * 		The base set of attribute values.
         * @return The resource ID for the style specified using {@code style="..."} in the
        {@link AttributeSet}'s backing XML element or {@link Resources#ID_NULL} otherwise
        if not specified or otherwise not applicable.
         */
        @android.annotation.StyleRes
        public int getExplicitStyle(@android.annotation.Nullable
        android.util.AttributeSet set) {
            if (set == null) {
                return android.content.res.Resources.ID_NULL;
            }
            int styleAttr = set.getStyleAttribute();
            if (styleAttr == android.content.res.Resources.ID_NULL) {
                return android.content.res.Resources.ID_NULL;
            }
            java.lang.String styleAttrType = getResources().getResourceTypeName(styleAttr);
            if ("attr".equals(styleAttrType)) {
                android.util.TypedValue explicitStyle = new android.util.TypedValue();
                boolean resolved = resolveAttribute(styleAttr, explicitStyle, true);
                if (resolved) {
                    return explicitStyle.resourceId;
                }
            } else
                if ("style".equals(styleAttrType)) {
                    return styleAttr;
                }

            return android.content.res.Resources.ID_NULL;
        }

        /**
         * Returns the ordered list of resource ID that are considered when resolving attribute
         * values when making an equivalent call to
         * {@link #obtainStyledAttributes(AttributeSet, int[], int, int)} . The list will include
         * a set of explicit styles ({@code explicitStyleRes} and it will include the default styles
         * ({@code defStyleAttr} and {@code defStyleRes}).
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
        @android.annotation.NonNull
        public int[] getAttributeResolutionStack(@android.annotation.AttrRes
        int defStyleAttr, @android.annotation.StyleRes
        int defStyleRes, @android.annotation.StyleRes
        int explicitStyleRes) {
            int[] stack = mThemeImpl.getAttributeResolutionStack(defStyleAttr, defStyleRes, explicitStyleRes);
            if (stack == null) {
                return new int[0];
            } else {
                return stack;
            }
        }
    }

    static class ThemeKey implements java.lang.Cloneable {
        int[] mResId;

        boolean[] mForce;

        int mCount;

        private int mHashCode = 0;

        public void append(int resId, boolean force) {
            if (mResId == null) {
                mResId = new int[4];
            }
            if (mForce == null) {
                mForce = new boolean[4];
            }
            mResId = com.android.internal.util.GrowingArrayUtils.append(mResId, mCount, resId);
            mForce = com.android.internal.util.GrowingArrayUtils.append(mForce, mCount, force);
            mCount++;
            mHashCode = (31 * ((31 * mHashCode) + resId)) + (force ? 1 : 0);
        }

        /**
         * Sets up this key as a deep copy of another key.
         *
         * @param other
         * 		the key to deep copy into this key
         */
        public void setTo(android.content.res.Resources.ThemeKey other) {
            mResId = (other.mResId == null) ? null : other.mResId.clone();
            mForce = (other.mForce == null) ? null : other.mForce.clone();
            mCount = other.mCount;
        }

        @java.lang.Override
        public int hashCode() {
            return mHashCode;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (((o == null) || (getClass() != o.getClass())) || (hashCode() != o.hashCode())) {
                return false;
            }
            final android.content.res.Resources.ThemeKey t = ((android.content.res.Resources.ThemeKey) (o));
            if (mCount != t.mCount) {
                return false;
            }
            final int N = mCount;
            for (int i = 0; i < N; i++) {
                if ((mResId[i] != t.mResId[i]) || (mForce[i] != t.mForce[i])) {
                    return false;
                }
            }
            return true;
        }

        /**
         *
         *
         * @return a shallow copy of this key
         */
        @java.lang.Override
        public android.content.res.Resources.ThemeKey clone() {
            final android.content.res.Resources.ThemeKey other = new android.content.res.Resources.ThemeKey();
            other.mResId = mResId;
            other.mForce = mForce;
            other.mCount = mCount;
            other.mHashCode = mHashCode;
            return other;
        }
    }

    /**
     * Generate a new Theme object for this set of Resources.  It initially
     * starts out empty.
     *
     * @return Theme The newly created Theme container.
     */
    public final android.content.res.Resources.Theme newTheme() {
        android.content.res.Resources.Theme theme = new android.content.res.Resources.Theme();
        theme.setImpl(mResourcesImpl.newThemeImpl());
        synchronized(mThemeRefs) {
            mThemeRefs.add(new java.lang.ref.WeakReference<>(theme));
            // Clean up references to garbage collected themes
            if (mThemeRefs.size() > mThemeRefsNextFlushSize) {
                mThemeRefs.removeIf(( ref) -> ref.get() == null);
                mThemeRefsNextFlushSize = java.lang.Math.max(android.content.res.Resources.MIN_THEME_REFS_FLUSH_SIZE, 2 * mThemeRefs.size());
            }
        }
        return theme;
    }

    /**
     * Retrieve a set of basic attribute values from an AttributeSet, not
     * performing styling of them using a theme and/or style resources.
     *
     * @param set
     * 		The current attribute values to retrieve.
     * @param attrs
     * 		The specific attributes to be retrieved. These attribute IDs must be sorted in
     * 		ascending order.
     * @return Returns a TypedArray holding an array of the attribute values.
    Be sure to call {@link TypedArray#recycle() TypedArray.recycle()}
    when done with it.
     * @see Theme#obtainStyledAttributes(AttributeSet, int[], int, int)
     */
    public android.content.res.TypedArray obtainAttributes(android.util.AttributeSet set, @android.annotation.StyleableRes
    int[] attrs) {
        int len = attrs.length;
        android.content.res.TypedArray array = android.content.res.TypedArray.obtain(this, len);
        // XXX note that for now we only work with compiled XML files.
        // To support generic XML files we will need to manually parse
        // out the attributes from the XML file (applying type information
        // contained in the resources and such).
        android.content.res.XmlBlock.Parser parser = ((android.content.res.XmlBlock.Parser) (set));
        mResourcesImpl.getAssets().retrieveAttributes(parser, attrs, array.mData, array.mIndices);
        array.mXml = parser;
        return array;
    }

    /**
     * Store the newly updated configuration.
     *
     * @deprecated See {@link android.content.Context#createConfigurationContext(Configuration)}.
     */
    @java.lang.Deprecated
    public void updateConfiguration(android.content.res.Configuration config, android.util.DisplayMetrics metrics) {
        updateConfiguration(config, metrics, null);
    }

    /**
     *
     *
     * @unknown 
     */
    public void updateConfiguration(android.content.res.Configuration config, android.util.DisplayMetrics metrics, android.content.res.CompatibilityInfo compat) {
        mResourcesImpl.updateConfiguration(config, metrics, compat);
    }

    /**
     * Update the system resources configuration if they have previously
     * been initialized.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static void updateSystemConfiguration(android.content.res.Configuration config, android.util.DisplayMetrics metrics, android.content.res.CompatibilityInfo compat) {
        if (android.content.res.Resources.mSystem != null) {
            android.content.res.Resources.mSystem.updateConfiguration(config, metrics, compat);
            // Log.i(TAG, "Updated system resources " + mSystem
            // + ": " + mSystem.getConfiguration());
        }
    }

    /**
     * Return the current display metrics that are in effect for this resource
     * object.  The returned object should be treated as read-only.
     *
     * @return The resource's current display metrics.
     */
    public android.util.DisplayMetrics getDisplayMetrics() {
        return mResourcesImpl.getDisplayMetrics();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public android.view.DisplayAdjustments getDisplayAdjustments() {
        return mResourcesImpl.getDisplayAdjustments();
    }

    /**
     * Return the current configuration that is in effect for this resource
     * object.  The returned object should be treated as read-only.
     *
     * @return The resource's current configuration.
     */
    public android.content.res.Configuration getConfiguration() {
        return mResourcesImpl.getConfiguration();
    }

    /**
     *
     *
     * @unknown 
     */
    public android.content.res.Configuration[] getSizeConfigurations() {
        return mResourcesImpl.getSizeConfigurations();
    }

    /**
     * Return the compatibility mode information for the application.
     * The returned object should be treated as read-only.
     *
     * @return compatibility info.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public android.content.res.CompatibilityInfo getCompatibilityInfo() {
        return mResourcesImpl.getCompatibilityInfo();
    }

    /**
     * This is just for testing.
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    @android.annotation.UnsupportedAppUsage
    public void setCompatibilityInfo(android.content.res.CompatibilityInfo ci) {
        if (ci != null) {
            mResourcesImpl.updateConfiguration(null, null, ci);
        }
    }

    /**
     * Return a resource identifier for the given resource name.  A fully
     * qualified resource name is of the form "package:type/entry".  The first
     * two components (package and type) are optional if defType and
     * defPackage, respectively, are specified here.
     *
     * <p>Note: use of this function is discouraged.  It is much more
     * efficient to retrieve resources by identifier than by name.
     *
     * @param name
     * 		The name of the desired resource.
     * @param defType
     * 		Optional default resource type to find, if "type/" is
     * 		not included in the name.  Can be null to require an
     * 		explicit type.
     * @param defPackage
     * 		Optional default package to find, if "package:" is
     * 		not included in the name.  Can be null to require an
     * 		explicit package.
     * @return int The associated resource identifier.  Returns 0 if no such
    resource was found.  (0 is not a valid resource ID.)
     */
    public int getIdentifier(java.lang.String name, java.lang.String defType, java.lang.String defPackage) {
        return mResourcesImpl.getIdentifier(name, defType, defPackage);
    }

    /**
     * Return true if given resource identifier includes a package.
     *
     * @unknown 
     */
    public static boolean resourceHasPackage(@android.annotation.AnyRes
    int resid) {
        return (resid >>> 24) != 0;
    }

    /**
     * Return the full name for a given resource identifier.  This name is
     * a single string of the form "package:type/entry".
     *
     * @param resid
     * 		The resource identifier whose name is to be retrieved.
     * @return A string holding the name of the resource.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @see #getResourcePackageName
     * @see #getResourceTypeName
     * @see #getResourceEntryName
     */
    public java.lang.String getResourceName(@android.annotation.AnyRes
    int resid) throws android.content.res.Resources.NotFoundException {
        return mResourcesImpl.getResourceName(resid);
    }

    /**
     * Return the package name for a given resource identifier.
     *
     * @param resid
     * 		The resource identifier whose package name is to be
     * 		retrieved.
     * @return A string holding the package name of the resource.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @see #getResourceName
     */
    public java.lang.String getResourcePackageName(@android.annotation.AnyRes
    int resid) throws android.content.res.Resources.NotFoundException {
        return mResourcesImpl.getResourcePackageName(resid);
    }

    /**
     * Return the type name for a given resource identifier.
     *
     * @param resid
     * 		The resource identifier whose type name is to be
     * 		retrieved.
     * @return A string holding the type name of the resource.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @see #getResourceName
     */
    public java.lang.String getResourceTypeName(@android.annotation.AnyRes
    int resid) throws android.content.res.Resources.NotFoundException {
        return mResourcesImpl.getResourceTypeName(resid);
    }

    /**
     * Return the entry name for a given resource identifier.
     *
     * @param resid
     * 		The resource identifier whose entry name is to be
     * 		retrieved.
     * @return A string holding the entry name of the resource.
     * @throws NotFoundException
     * 		Throws NotFoundException if the given ID does not exist.
     * @see #getResourceName
     */
    public java.lang.String getResourceEntryName(@android.annotation.AnyRes
    int resid) throws android.content.res.Resources.NotFoundException {
        return mResourcesImpl.getResourceEntryName(resid);
    }

    /**
     * Return formatted log of the last retrieved resource's resolution path.
     *
     * @return A string holding a formatted log of the steps taken to resolve the last resource.
     * @throws NotFoundException
     * 		Throws NotFoundException if there hasn't been a resource
     * 		resolved yet.
     * @unknown 
     */
    public java.lang.String getLastResourceResolution() throws android.content.res.Resources.NotFoundException {
        return mResourcesImpl.getLastResourceResolution();
    }

    /**
     * Parse a series of {@link android.R.styleable#Extra &lt;extra&gt;} tags from
     * an XML file.  You call this when you are at the parent tag of the
     * extra tags, and it will return once all of the child tags have been parsed.
     * This will call {@link #parseBundleExtra} for each extra tag encountered.
     *
     * @param parser
     * 		The parser from which to retrieve the extras.
     * @param outBundle
     * 		A Bundle in which to place all parsed extras.
     * @throws XmlPullParserException
     * 		
     * @throws IOException
     * 		
     */
    public void parseBundleExtras(android.content.res.XmlResourceParser parser, android.os.Bundle outBundle) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int outerDepth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            java.lang.String nodeName = parser.getName();
            if (nodeName.equals("extra")) {
                parseBundleExtra("extra", parser, outBundle);
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            } else {
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            }
        } 
    }

    /**
     * Parse a name/value pair out of an XML tag holding that data.  The
     * AttributeSet must be holding the data defined by
     * {@link android.R.styleable#Extra}.  The following value types are supported:
     * <ul>
     * <li> {@link TypedValue#TYPE_STRING}:
     * {@link Bundle#putCharSequence Bundle.putCharSequence()}
     * <li> {@link TypedValue#TYPE_INT_BOOLEAN}:
     * {@link Bundle#putCharSequence Bundle.putBoolean()}
     * <li> {@link TypedValue#TYPE_FIRST_INT}-{@link TypedValue#TYPE_LAST_INT}:
     * {@link Bundle#putCharSequence Bundle.putBoolean()}
     * <li> {@link TypedValue#TYPE_FLOAT}:
     * {@link Bundle#putCharSequence Bundle.putFloat()}
     * </ul>
     *
     * @param tagName
     * 		The name of the tag these attributes come from; this is
     * 		only used for reporting error messages.
     * @param attrs
     * 		The attributes from which to retrieve the name/value pair.
     * @param outBundle
     * 		The Bundle in which to place the parsed value.
     * @throws XmlPullParserException
     * 		If the attributes are not valid.
     */
    public void parseBundleExtra(java.lang.String tagName, android.util.AttributeSet attrs, android.os.Bundle outBundle) throws org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = obtainAttributes(attrs, com.android.internal.R.styleable.Extra);
        java.lang.String name = sa.getString(com.android.internal.R.styleable.Extra_name);
        if (name == null) {
            sa.recycle();
            throw new org.xmlpull.v1.XmlPullParserException((("<" + tagName) + "> requires an android:name attribute at ") + attrs.getPositionDescription());
        }
        android.util.TypedValue v = sa.peekValue(com.android.internal.R.styleable.Extra_value);
        if (v != null) {
            if (v.type == android.util.TypedValue.TYPE_STRING) {
                java.lang.CharSequence cs = v.coerceToString();
                outBundle.putCharSequence(name, cs);
            } else
                if (v.type == android.util.TypedValue.TYPE_INT_BOOLEAN) {
                    outBundle.putBoolean(name, v.data != 0);
                } else
                    if ((v.type >= android.util.TypedValue.TYPE_FIRST_INT) && (v.type <= android.util.TypedValue.TYPE_LAST_INT)) {
                        outBundle.putInt(name, v.data);
                    } else
                        if (v.type == android.util.TypedValue.TYPE_FLOAT) {
                            outBundle.putFloat(name, v.getFloat());
                        } else {
                            sa.recycle();
                            throw new org.xmlpull.v1.XmlPullParserException((("<" + tagName) + "> only supports string, integer, float, color, and boolean at ") + attrs.getPositionDescription());
                        }



        } else {
            sa.recycle();
            throw new org.xmlpull.v1.XmlPullParserException((("<" + tagName) + "> requires an android:value or android:resource attribute at ") + attrs.getPositionDescription());
        }
        sa.recycle();
    }

    /**
     * Retrieve underlying AssetManager storage for these resources.
     */
    public final android.content.res.AssetManager getAssets() {
        return mResourcesImpl.getAssets();
    }

    /**
     * Call this to remove all cached loaded layout resources from the
     * Resources object.  Only intended for use with performance testing
     * tools.
     */
    public final void flushLayoutCache() {
        mResourcesImpl.flushLayoutCache();
    }

    /**
     * Start preloading of resource data using this Resources object.  Only
     * for use by the zygote process for loading common system resources.
     * {@hide }
     */
    public final void startPreloading() {
        mResourcesImpl.startPreloading();
    }

    /**
     * Called by zygote when it is done preloading resources, to change back
     * to normal Resources operation.
     */
    public final void finishPreloading() {
        mResourcesImpl.finishPreloading();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public android.util.LongSparseArray<android.graphics.drawable.Drawable.ConstantState> getPreloadedDrawables() {
        return mResourcesImpl.getPreloadedDrawables();
    }

    /**
     * Loads an XML parser for the specified file.
     *
     * @param id
     * 		the resource identifier for the file
     * @param type
     * 		the type of resource (used for logging)
     * @return a parser for the specified XML file
     * @throws NotFoundException
     * 		if the file could not be loaded
     */
    @android.annotation.NonNull
    @android.annotation.UnsupportedAppUsage
    android.content.res.XmlResourceParser loadXmlResourceParser(@android.annotation.AnyRes
    int id, @android.annotation.NonNull
    java.lang.String type) throws android.content.res.Resources.NotFoundException {
        final android.util.TypedValue value = obtainTempTypedValue();
        try {
            final android.content.res.ResourcesImpl impl = mResourcesImpl;
            impl.getValue(id, value, true);
            if (value.type == android.util.TypedValue.TYPE_STRING) {
                return impl.loadXmlResourceParser(value.string.toString(), id, value.assetCookie, type);
            }
            throw new android.content.res.Resources.NotFoundException(((("Resource ID #0x" + java.lang.Integer.toHexString(id)) + " type #0x") + java.lang.Integer.toHexString(value.type)) + " is not valid");
        } finally {
            releaseTempTypedValue(value);
        }
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
    @android.annotation.UnsupportedAppUsage
    android.content.res.XmlResourceParser loadXmlResourceParser(java.lang.String file, int id, int assetCookie, java.lang.String type) throws android.content.res.Resources.NotFoundException {
        return mResourcesImpl.loadXmlResourceParser(file, id, assetCookie, type);
    }

    /**
     * Called by ConfigurationBoundResourceCacheTest.
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public int calcConfigChanges(android.content.res.Configuration config) {
        return mResourcesImpl.calcConfigChanges(config);
    }

    /**
     * Obtains styled attributes from the theme, if available, or unstyled
     * resources if the theme is null.
     *
     * @unknown 
     */
    public static android.content.res.TypedArray obtainAttributes(android.content.res.Resources res, android.content.res.Resources.Theme theme, android.util.AttributeSet set, int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }
}

