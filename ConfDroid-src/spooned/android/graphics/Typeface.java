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
package android.graphics;


/**
 * The Typeface class specifies the typeface and intrinsic style of a font.
 * This is used in the paint, along with optionally Paint settings like
 * textSize, textSkewX, textScaleX to specify
 * how text appears when drawn (and measured).
 */
public class Typeface {
    private static java.lang.String TAG = "Typeface";

    private static final libcore.util.NativeAllocationRegistry sRegistry = libcore.util.NativeAllocationRegistry.createMalloced(android.graphics.Typeface.class.getClassLoader(), android.graphics.Typeface.nativeGetReleaseFunc());

    /**
     * The default NORMAL typeface object
     */
    public static final android.graphics.Typeface DEFAULT;

    /**
     * The default BOLD typeface object. Note: this may be not actually be
     * bold, depending on what fonts are installed. Call getStyle() to know
     * for sure.
     */
    public static final android.graphics.Typeface DEFAULT_BOLD;

    /**
     * The NORMAL style of the default sans serif typeface.
     */
    public static final android.graphics.Typeface SANS_SERIF;

    /**
     * The NORMAL style of the default serif typeface.
     */
    public static final android.graphics.Typeface SERIF;

    /**
     * The NORMAL style of the default monospace typeface.
     */
    public static final android.graphics.Typeface MONOSPACE;

    /**
     * The default {@link Typeface}s for different text styles.
     * Call {@link #defaultFromStyle(int)} to get the default typeface for the given text style.
     * It shouldn't be changed for app wide typeface settings. Please use theme and font XML for
     * the same purpose.
     */
    @android.annotation.UnsupportedAppUsage(trackingBug = 123769446)
    static android.graphics.Typeface[] sDefaults;

    /**
     * Cache for Typeface objects for style variant. Currently max size is 3.
     */
    @com.android.internal.annotations.GuardedBy("sStyledCacheLock")
    private static final android.util.LongSparseArray<android.util.SparseArray<android.graphics.Typeface>> sStyledTypefaceCache = new android.util.LongSparseArray(3);

    private static final java.lang.Object sStyledCacheLock = new java.lang.Object();

    /**
     * Cache for Typeface objects for weight variant. Currently max size is 3.
     */
    @com.android.internal.annotations.GuardedBy("sWeightCacheLock")
    private static final android.util.LongSparseArray<android.util.SparseArray<android.graphics.Typeface>> sWeightTypefaceCache = new android.util.LongSparseArray(3);

    private static final java.lang.Object sWeightCacheLock = new java.lang.Object();

    /**
     * Cache for Typeface objects dynamically loaded from assets. Currently max size is 16.
     */
    @com.android.internal.annotations.GuardedBy("sDynamicCacheLock")
    private static final android.util.LruCache<java.lang.String, android.graphics.Typeface> sDynamicTypefaceCache = new android.util.LruCache(16);

    private static final java.lang.Object sDynamicCacheLock = new java.lang.Object();

    static android.graphics.Typeface sDefaultTypeface;

    // Following two fields are not used but left for hiddenapi private list
    /**
     * sSystemFontMap is read only and unmodifiable.
     * Use public API {@link #create(String, int)} to get the typeface for given familyName.
     */
    @android.annotation.UnsupportedAppUsage(trackingBug = 123769347)
    static final java.util.Map<java.lang.String, android.graphics.Typeface> sSystemFontMap;

    // We cannot support sSystemFallbackMap since we will migrate to public FontFamily API.
    /**
     *
     *
     * @deprecated Use {@link android.graphics.fonts.FontFamily} instead.
     */
    @android.annotation.UnsupportedAppUsage(trackingBug = 123768928)
    @java.lang.Deprecated
    static final java.util.Map<java.lang.String, android.graphics.FontFamily[]> sSystemFallbackMap = java.util.Collections.emptyMap();

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public long native_instance;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.graphics.Typeface.NORMAL, android.graphics.Typeface.BOLD, android.graphics.Typeface.ITALIC, android.graphics.Typeface.BOLD_ITALIC })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Style {}

    // Style
    public static final int NORMAL = 0;

    public static final int BOLD = 1;

    public static final int ITALIC = 2;

    public static final int BOLD_ITALIC = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int STYLE_MASK = 0x3;

    @android.annotation.UnsupportedAppUsage
    @android.graphics.Typeface.Style
    private int mStyle = 0;

    @android.annotation.IntRange(from = 0, to = android.graphics.fonts.FontStyle.FONT_WEIGHT_MAX)
    private int mWeight = 0;

    // Value for weight and italic. Indicates the value is resolved by font metadata.
    // Must be the same as the C++ constant in core/jni/android/graphics/FontFamily.cpp
    /**
     *
     *
     * @unknown 
     */
    public static final int RESOLVE_BY_FONT_TABLE = -1;

    private static final java.lang.String DEFAULT_FAMILY = "sans-serif";

    // Style value for building typeface.
    private static final int STYLE_NORMAL = 0;

    private static final int STYLE_ITALIC = 1;

    private int[] mSupportedAxes;

    private static final int[] EMPTY_AXES = new int[]{  };

    /**
     * Please use font in xml and also your application global theme to change the default Typeface.
     * android:textViewStyle and its attribute android:textAppearance can be used in order to change
     * typeface and other text related properties.
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    private static void setDefault(android.graphics.Typeface t) {
        android.graphics.Typeface.sDefaultTypeface = t;
        android.graphics.Typeface.nativeSetDefault(t.native_instance);
    }

    /**
     * Returns the typeface's weight value
     */
    @android.annotation.IntRange(from = 0, to = 1000)
    public int getWeight() {
        return mWeight;
    }

    /**
     * Returns the typeface's intrinsic style attributes
     */
    @android.graphics.Typeface.Style
    public int getStyle() {
        return mStyle;
    }

    /**
     * Returns true if getStyle() has the BOLD bit set.
     */
    public final boolean isBold() {
        return (mStyle & android.graphics.Typeface.BOLD) != 0;
    }

    /**
     * Returns true if getStyle() has the ITALIC bit set.
     */
    public final boolean isItalic() {
        return (mStyle & android.graphics.Typeface.ITALIC) != 0;
    }

    /**
     *
     *
     * @unknown Used by Resources to load a font resource of type xml.
     */
    @android.annotation.Nullable
    public static android.graphics.Typeface createFromResources(android.content.res.FontResourcesParser.FamilyResourceEntry entry, android.content.res.AssetManager mgr, java.lang.String path) {
        if (entry instanceof android.content.res.FontResourcesParser.ProviderResourceEntry) {
            final android.content.res.FontResourcesParser.ProviderResourceEntry providerEntry = ((android.content.res.FontResourcesParser.ProviderResourceEntry) (entry));
            // Downloadable font
            java.util.List<java.util.List<java.lang.String>> givenCerts = providerEntry.getCerts();
            java.util.List<java.util.List<byte[]>> certs = new java.util.ArrayList<>();
            if (givenCerts != null) {
                for (int i = 0; i < givenCerts.size(); i++) {
                    java.util.List<java.lang.String> certSet = givenCerts.get(i);
                    java.util.List<byte[]> byteArraySet = new java.util.ArrayList<>();
                    for (int j = 0; j < certSet.size(); j++) {
                        byteArraySet.add(android.util.Base64.decode(certSet.get(j), Base64.DEFAULT));
                    }
                    certs.add(byteArraySet);
                }
            }
            // Downloaded font and it wasn't cached, request it again and return a
            // default font instead (nothing we can do now).
            android.provider.FontRequest request = new android.provider.FontRequest(providerEntry.getAuthority(), providerEntry.getPackage(), providerEntry.getQuery(), certs);
            android.graphics.Typeface typeface = android.provider.FontsContract.getFontSync(request);
            return typeface == null ? android.graphics.Typeface.DEFAULT : typeface;
        }
        android.graphics.Typeface typeface = android.graphics.Typeface.findFromCache(mgr, path);
        if (typeface != null)
            return typeface;

        // family is FontFamilyFilesResourceEntry
        final android.content.res.FontResourcesParser.FontFamilyFilesResourceEntry filesEntry = ((android.content.res.FontResourcesParser.FontFamilyFilesResourceEntry) (entry));
        try {
            android.graphics.fonts.FontFamily.Builder familyBuilder = null;
            for (final android.content.res.FontResourcesParser.FontFileResourceEntry fontFile : filesEntry.getEntries()) {
                final android.graphics.fonts.Font.Builder fontBuilder = /* isAsset */
                /* cookie */
                new android.graphics.fonts.Font.Builder(mgr, fontFile.getFileName(), false, 0).setTtcIndex(fontFile.getTtcIndex()).setFontVariationSettings(fontFile.getVariationSettings());
                if (fontFile.getWeight() != android.graphics.Typeface.RESOLVE_BY_FONT_TABLE) {
                    fontBuilder.setWeight(fontFile.getWeight());
                }
                if (fontFile.getItalic() != android.graphics.Typeface.RESOLVE_BY_FONT_TABLE) {
                    fontBuilder.setSlant(fontFile.getItalic() == android.content.res.FontResourcesParser.FontFileResourceEntry.ITALIC ? android.graphics.fonts.FontStyle.FONT_SLANT_ITALIC : android.graphics.fonts.FontStyle.FONT_SLANT_UPRIGHT);
                }
                if (familyBuilder == null) {
                    familyBuilder = new android.graphics.fonts.FontFamily.Builder(fontBuilder.build());
                } else {
                    familyBuilder.addFont(fontBuilder.build());
                }
            }
            if (familyBuilder == null) {
                return android.graphics.Typeface.DEFAULT;
            }
            final android.graphics.fonts.FontFamily family = familyBuilder.build();
            final android.graphics.fonts.FontStyle normal = new android.graphics.fonts.FontStyle(android.graphics.fonts.FontStyle.FONT_WEIGHT_NORMAL, android.graphics.fonts.FontStyle.FONT_SLANT_UPRIGHT);
            android.graphics.fonts.Font bestFont = family.getFont(0);
            int bestScore = normal.getMatchScore(bestFont.getStyle());
            for (int i = 1; i < family.getSize(); ++i) {
                final android.graphics.fonts.Font candidate = family.getFont(i);
                final int score = normal.getMatchScore(candidate.getStyle());
                if (score < bestScore) {
                    bestFont = candidate;
                    bestScore = score;
                }
            }
            typeface = new android.graphics.Typeface.CustomFallbackBuilder(family).setStyle(bestFont.getStyle()).build();
        } catch (java.lang.IllegalArgumentException e) {
            // To be a compatible behavior with API28 or before, catch IllegalArgumentExcetpion
            // thrown by native code and returns null.
            return null;
        } catch (java.io.IOException e) {
            typeface = android.graphics.Typeface.DEFAULT;
        }
        synchronized(android.graphics.Typeface.sDynamicCacheLock) {
            final java.lang.String key = /* ttcIndex */
            /* axes */
            /* weight */
            /* italic */
            android.graphics.Typeface.Builder.createAssetUid(mgr, path, 0, null, android.graphics.Typeface.RESOLVE_BY_FONT_TABLE, android.graphics.Typeface.RESOLVE_BY_FONT_TABLE, android.graphics.Typeface.DEFAULT_FAMILY);
            android.graphics.Typeface.sDynamicTypefaceCache.put(key, typeface);
        }
        return typeface;
    }

    /**
     * Used by resources for cached loading if the font is available.
     *
     * @unknown 
     */
    public static android.graphics.Typeface findFromCache(android.content.res.AssetManager mgr, java.lang.String path) {
        synchronized(android.graphics.Typeface.sDynamicCacheLock) {
            final java.lang.String key = /* ttcIndex */
            /* axes */
            /* weight */
            /* italic */
            android.graphics.Typeface.Builder.createAssetUid(mgr, path, 0, null, android.graphics.Typeface.RESOLVE_BY_FONT_TABLE, android.graphics.Typeface.RESOLVE_BY_FONT_TABLE, android.graphics.Typeface.DEFAULT_FAMILY);
            android.graphics.Typeface typeface = android.graphics.Typeface.sDynamicTypefaceCache.get(key);
            if (typeface != null) {
                return typeface;
            }
        }
        return null;
    }

    /**
     * A builder class for creating new Typeface instance.
     *
     * <p>
     * Examples,
     * 1) Create Typeface from ttf file.
     * <pre>
     * <code>
     * Typeface.Builder buidler = new Typeface.Builder("your_font_file.ttf");
     * Typeface typeface = builder.build();
     * </code>
     * </pre>
     *
     * 2) Create Typeface from ttc file in assets directory.
     * <pre>
     * <code>
     * Typeface.Builder buidler = new Typeface.Builder(getAssets(), "your_font_file.ttc");
     * builder.setTtcIndex(2);  // Set index of font collection.
     * Typeface typeface = builder.build();
     * </code>
     * </pre>
     *
     * 3) Create Typeface with variation settings.
     * <pre>
     * <code>
     * Typeface.Builder buidler = new Typeface.Builder("your_font_file.ttf");
     * builder.setFontVariationSettings("'wght' 700, 'slnt' 20, 'ital' 1");
     * builder.setWeight(700);  // Tell the system that this is a bold font.
     * builder.setItalic(true);  // Tell the system that this is an italic style font.
     * Typeface typeface = builder.build();
     * </code>
     * </pre>
     * </p>
     */
    public static final class Builder {
        /**
         *
         *
         * @unknown 
         */
        public static final int NORMAL_WEIGHT = 400;

        /**
         *
         *
         * @unknown 
         */
        public static final int BOLD_WEIGHT = 700;

        // Kept for generating asset cache key.
        private final android.content.res.AssetManager mAssetManager;

        private final java.lang.String mPath;

        @android.annotation.Nullable
        private final android.graphics.fonts.Font.Builder mFontBuilder;

        private java.lang.String mFallbackFamilyName;

        private int mWeight = android.graphics.Typeface.RESOLVE_BY_FONT_TABLE;

        private int mItalic = android.graphics.Typeface.RESOLVE_BY_FONT_TABLE;

        /**
         * Constructs a builder with a file path.
         *
         * @param path
         * 		The file object refers to the font file.
         */
        public Builder(@android.annotation.NonNull
        java.io.File path) {
            mFontBuilder = new android.graphics.fonts.Font.Builder(path);
            mAssetManager = null;
            mPath = null;
        }

        /**
         * Constructs a builder with a file descriptor.
         *
         * Caller is responsible for closing the passed file descriptor after {@link #build} is
         * called.
         *
         * @param fd
         * 		The file descriptor. The passed fd must be mmap-able.
         */
        public Builder(@android.annotation.NonNull
        java.io.FileDescriptor fd) {
            android.graphics.fonts.Font.Builder builder;
            try {
                builder = new android.graphics.fonts.Font.Builder(android.os.ParcelFileDescriptor.dup(fd));
            } catch (java.io.IOException e) {
                // We cannot tell the error to developer at this moment since we cannot change the
                // public API signature. Instead, silently fallbacks to system fallback in the build
                // method as the same as other error cases.
                builder = null;
            }
            mFontBuilder = builder;
            mAssetManager = null;
            mPath = null;
        }

        /**
         * Constructs a builder with a file path.
         *
         * @param path
         * 		The full path to the font file.
         */
        public Builder(@android.annotation.NonNull
        java.lang.String path) {
            mFontBuilder = new android.graphics.fonts.Font.Builder(new java.io.File(path));
            mAssetManager = null;
            mPath = null;
        }

        /**
         * Constructs a builder from an asset manager and a file path in an asset directory.
         *
         * @param assetManager
         * 		The application's asset manager
         * @param path
         * 		The file name of the font data in the asset directory
         */
        public Builder(@android.annotation.NonNull
        android.content.res.AssetManager assetManager, @android.annotation.NonNull
        java.lang.String path) {
            /* is asset */
            /* cookie */
            this(assetManager, path, true, 0);
        }

        /**
         * Constructs a builder from an asset manager and a file path in an asset directory.
         *
         * @param assetManager
         * 		The application's asset manager
         * @param path
         * 		The file name of the font data in the asset directory
         * @param cookie
         * 		a cookie for the asset
         * @unknown 
         */
        public Builder(@android.annotation.NonNull
        android.content.res.AssetManager assetManager, @android.annotation.NonNull
        java.lang.String path, boolean isAsset, int cookie) {
            mFontBuilder = new android.graphics.fonts.Font.Builder(assetManager, path, isAsset, cookie);
            mAssetManager = assetManager;
            mPath = path;
        }

        /**
         * Sets weight of the font.
         *
         * Tells the system the weight of the given font. If not provided, the system will resolve
         * the weight value by reading font tables.
         *
         * @param weight
         * 		a weight value.
         */
        public android.graphics.Typeface.Builder setWeight(@android.annotation.IntRange(from = 1, to = 1000)
        int weight) {
            mWeight = weight;
            mFontBuilder.setWeight(weight);
            return this;
        }

        /**
         * Sets italic information of the font.
         *
         * Tells the system the style of the given font. If not provided, the system will resolve
         * the style by reading font tables.
         *
         * @param italic
         * 		{@code true} if the font is italic. Otherwise {@code false}.
         */
        public android.graphics.Typeface.Builder setItalic(boolean italic) {
            mItalic = (italic) ? android.graphics.fonts.FontStyle.FONT_SLANT_ITALIC : android.graphics.fonts.FontStyle.FONT_SLANT_UPRIGHT;
            mFontBuilder.setSlant(mItalic);
            return this;
        }

        /**
         * Sets an index of the font collection. See {@link android.R.attr#ttcIndex}.
         *
         * Can not be used for Typeface source. build() method will return null for invalid index.
         *
         * @param ttcIndex
         * 		An index of the font collection. If the font source is not font
         * 		collection, do not call this method or specify 0.
         */
        public android.graphics.Typeface.Builder setTtcIndex(@android.annotation.IntRange(from = 0)
        int ttcIndex) {
            mFontBuilder.setTtcIndex(ttcIndex);
            return this;
        }

        /**
         * Sets a font variation settings.
         *
         * @param variationSettings
         * 		See {@link android.widget.TextView#setFontVariationSettings}.
         * @throws IllegalArgumentException
         * 		If given string is not a valid font variation settings
         * 		format.
         */
        public android.graphics.Typeface.Builder setFontVariationSettings(@android.annotation.Nullable
        java.lang.String variationSettings) {
            mFontBuilder.setFontVariationSettings(variationSettings);
            return this;
        }

        /**
         * Sets a font variation settings.
         *
         * @param axes
         * 		An array of font variation axis tag-value pairs.
         */
        public android.graphics.Typeface.Builder setFontVariationSettings(@android.annotation.Nullable
        android.graphics.fonts.FontVariationAxis[] axes) {
            mFontBuilder.setFontVariationSettings(axes);
            return this;
        }

        /**
         * Sets a fallback family name.
         *
         * By specifying a fallback family name, a fallback Typeface will be returned if the
         * {@link #build} method fails to create a Typeface from the provided font. The fallback
         * family will be resolved with the provided weight and italic information specified by
         * {@link #setWeight} and {@link #setItalic}.
         *
         * If {@link #setWeight} is not called, the fallback family keeps the default weight.
         * Similary, if {@link #setItalic} is not called, the fallback family keeps the default
         * italic information. For example, calling {@code builder.setFallback("sans-serif-light")}
         * is equivalent to calling {@code builder.setFallback("sans-serif").setWeight(300)} in
         * terms of fallback. The default weight and italic information are overridden by calling
         * {@link #setWeight} and {@link #setItalic}. For example, if a Typeface is constructed
         * using {@code builder.setFallback("sans-serif-light").setWeight(700)}, the fallback text
         * will render as sans serif bold.
         *
         * @param familyName
         * 		A family name to be used for fallback if the provided font can not be
         * 		used. By passing {@code null}, build() returns {@code null}.
         * 		If {@link #setFallback} is not called on the builder, {@code null}
         * 		is assumed.
         */
        public android.graphics.Typeface.Builder setFallback(@android.annotation.Nullable
        java.lang.String familyName) {
            mFallbackFamilyName = familyName;
            return this;
        }

        /**
         * Creates a unique id for a given AssetManager and asset path.
         *
         * @param mgr
         * 		AssetManager instance
         * @param path
         * 		The path for the asset.
         * @param ttcIndex
         * 		The TTC index for the font.
         * @param axes
         * 		The font variation settings.
         * @return Unique id for a given AssetManager and asset path.
         */
        private static java.lang.String createAssetUid(final android.content.res.AssetManager mgr, java.lang.String path, int ttcIndex, @android.annotation.Nullable
        android.graphics.fonts.FontVariationAxis[] axes, int weight, int italic, java.lang.String fallback) {
            final android.util.SparseArray<java.lang.String> pkgs = mgr.getAssignedPackageIdentifiers();
            final java.lang.StringBuilder builder = new java.lang.StringBuilder();
            final int size = pkgs.size();
            for (int i = 0; i < size; i++) {
                builder.append(pkgs.valueAt(i));
                builder.append("-");
            }
            builder.append(path);
            builder.append("-");
            builder.append(java.lang.Integer.toString(ttcIndex));
            builder.append("-");
            builder.append(java.lang.Integer.toString(weight));
            builder.append("-");
            builder.append(java.lang.Integer.toString(italic));
            // Family name may contain hyphen. Use double hyphen for avoiding key conflicts before
            // and after appending falblack name.
            builder.append("--");
            builder.append(fallback);
            builder.append("--");
            if (axes != null) {
                for (android.graphics.fonts.FontVariationAxis axis : axes) {
                    builder.append(axis.getTag());
                    builder.append("-");
                    builder.append(java.lang.Float.toString(axis.getStyleValue()));
                }
            }
            return builder.toString();
        }

        private android.graphics.Typeface resolveFallbackTypeface() {
            if (mFallbackFamilyName == null) {
                return null;
            }
            final android.graphics.Typeface base = android.graphics.Typeface.getSystemDefaultTypeface(mFallbackFamilyName);
            if ((mWeight == android.graphics.Typeface.RESOLVE_BY_FONT_TABLE) && (mItalic == android.graphics.Typeface.RESOLVE_BY_FONT_TABLE)) {
                return base;
            }
            final int weight = (mWeight == android.graphics.Typeface.RESOLVE_BY_FONT_TABLE) ? base.mWeight : mWeight;
            final boolean italic = (mItalic == android.graphics.Typeface.RESOLVE_BY_FONT_TABLE) ? (base.mStyle & android.graphics.Typeface.ITALIC) != 0 : mItalic == 1;
            return android.graphics.Typeface.createWeightStyle(base, weight, italic);
        }

        /**
         * Generates new Typeface from specified configuration.
         *
         * @return Newly created Typeface. May return null if some parameters are invalid.
         */
        public android.graphics.Typeface build() {
            if (mFontBuilder == null) {
                return resolveFallbackTypeface();
            }
            try {
                final android.graphics.fonts.Font font = mFontBuilder.build();
                final java.lang.String key = (mAssetManager == null) ? null : android.graphics.Typeface.Builder.createAssetUid(mAssetManager, mPath, font.getTtcIndex(), font.getAxes(), mWeight, mItalic, mFallbackFamilyName == null ? android.graphics.Typeface.DEFAULT_FAMILY : mFallbackFamilyName);
                if (key != null) {
                    // Dynamic cache lookup is only for assets.
                    synchronized(android.graphics.Typeface.sDynamicCacheLock) {
                        final android.graphics.Typeface typeface = android.graphics.Typeface.sDynamicTypefaceCache.get(key);
                        if (typeface != null) {
                            return typeface;
                        }
                    }
                }
                final android.graphics.fonts.FontFamily family = new android.graphics.fonts.FontFamily.Builder(font).build();
                final int weight = (mWeight == android.graphics.Typeface.RESOLVE_BY_FONT_TABLE) ? font.getStyle().getWeight() : mWeight;
                final int slant = (mItalic == android.graphics.Typeface.RESOLVE_BY_FONT_TABLE) ? font.getStyle().getSlant() : mItalic;
                final android.graphics.Typeface.CustomFallbackBuilder builder = new android.graphics.Typeface.CustomFallbackBuilder(family).setStyle(new android.graphics.fonts.FontStyle(weight, slant));
                if (mFallbackFamilyName != null) {
                    builder.setSystemFallback(mFallbackFamilyName);
                }
                final android.graphics.Typeface typeface = builder.build();
                if (key != null) {
                    synchronized(android.graphics.Typeface.sDynamicCacheLock) {
                        android.graphics.Typeface.sDynamicTypefaceCache.put(key, typeface);
                    }
                }
                return typeface;
            } catch (java.io.IOException | java.lang.IllegalArgumentException e) {
                return resolveFallbackTypeface();
            }
        }
    }

    /**
     * A builder class for creating new Typeface instance.
     *
     * There are two font fallback mechanisms, custom font fallback and system font fallback.
     * The custom font fallback is a simple ordered list. The text renderer tries to see if it can
     * render a character with the first font and if that font does not support the character, try
     * next one and so on. It will keep trying until end of the custom fallback chain. The maximum
     * length of the custom fallback chain is 64.
     * The system font fallback is a system pre-defined fallback chain. The system fallback is
     * processed only when no matching font is found in the custom font fallback.
     *
     * <p>
     * Examples,
     * 1) Create Typeface from single ttf file.
     * <pre>
     * <code>
     * Font font = new Font.Builder("your_font_file.ttf").build();
     * FontFamily family = new FontFamily.Builder(font).build();
     * Typeface typeface = new Typeface.CustomFallbackBuilder(family).build();
     * </code>
     * </pre>
     *
     * 2) Create Typeface from multiple font files and select bold style by default.
     * <pre>
     * <code>
     * Font regularFont = new Font.Builder("regular.ttf").build();
     * Font boldFont = new Font.Builder("bold.ttf").build();
     * FontFamily family = new FontFamily.Builder(regularFont)
     *     .addFont(boldFont).build();
     * Typeface typeface = new Typeface.CustomFallbackBuilder(family)
     *     .setWeight(Font.FONT_WEIGHT_BOLD)  // Set bold style as the default style.
     *                                        // If the font family doesn't have bold style font,
     *                                        // system will select the closest font.
     *     .build();
     * </code>
     * </pre>
     *
     * 3) Create Typeface from single ttf file and if that font does not have glyph for the
     * characters, use "serif" font family instead.
     * <pre>
     * <code>
     * Font font = new Font.Builder("your_font_file.ttf").build();
     * FontFamily family = new FontFamily.Builder(font).build();
     * Typeface typeface = new Typeface.CustomFallbackBuilder(family)
     *     .setSystemFallback("serif")  // Set serif font family as the fallback.
     *     .build();
     * </code>
     * </pre>
     * 4) Create Typeface from single ttf file and set another ttf file for the fallback.
     * <pre>
     * <code>
     * Font font = new Font.Builder("English.ttf").build();
     * FontFamily family = new FontFamily.Builder(font).build();
     *
     * Font fallbackFont = new Font.Builder("Arabic.ttf").build();
     * FontFamily fallbackFamily = new FontFamily.Builder(fallbackFont).build();
     * Typeface typeface = new Typeface.CustomFallbackBuilder(family)
     *     .addCustomFallback(fallbackFamily)  // Specify fallback family.
     *     .setSystemFallback("serif")  // Set serif font family as the fallback.
     *     .build();
     * </code>
     * </pre>
     * </p>
     */
    public static final class CustomFallbackBuilder {
        private static final int MAX_CUSTOM_FALLBACK = 64;

        private final java.util.ArrayList<android.graphics.fonts.FontFamily> mFamilies = new java.util.ArrayList<>();

        private java.lang.String mFallbackName = null;

        @android.annotation.Nullable
        private android.graphics.fonts.FontStyle mStyle;

        /**
         * Returns the maximum capacity of custom fallback families.
         *
         * This includes the the first font family passed to the constructor.
         * It is guaranteed that the value will be greater than or equal to 64.
         *
         * @return the maximum number of font families for the custom fallback
         */
        @android.annotation.IntRange(from = 64)
        public static int getMaxCustomFallbackCount() {
            return android.graphics.Typeface.CustomFallbackBuilder.MAX_CUSTOM_FALLBACK;
        }

        /**
         * Constructs a builder with a font family.
         *
         * @param family
         * 		a family object
         */
        public CustomFallbackBuilder(@android.annotation.NonNull
        android.graphics.fonts.FontFamily family) {
            com.android.internal.util.Preconditions.checkNotNull(family);
            mFamilies.add(family);
        }

        /**
         * Sets a system fallback by name.
         *
         * You can specify generic font familiy names or OEM specific family names. If the system
         * don't have a specified fallback, the default fallback is used instead.
         * For more information about generic font families, see <a
         * href="https://www.w3.org/TR/css-fonts-4/#generic-font-families">CSS specification</a>
         *
         * For more information about fallback, see class description.
         *
         * @param familyName
         * 		a family name to be used for fallback if the provided fonts can not be
         * 		used
         */
        @android.annotation.NonNull
        public android.graphics.Typeface.CustomFallbackBuilder setSystemFallback(@android.annotation.NonNull
        java.lang.String familyName) {
            com.android.internal.util.Preconditions.checkNotNull(familyName);
            mFallbackName = familyName;
            return this;
        }

        /**
         * Sets a font style of the Typeface.
         *
         * If the font family doesn't have a font of given style, system will select the closest
         * font from font family. For example, if a font family has fonts of 300 weight and 700
         * weight then setWeight(400) is called, system will select the font of 300 weight.
         *
         * @param style
         * 		a font style
         */
        @android.annotation.NonNull
        public android.graphics.Typeface.CustomFallbackBuilder setStyle(@android.annotation.NonNull
        android.graphics.fonts.FontStyle style) {
            mStyle = style;
            return this;
        }

        /**
         * Append a font family to the end of the custom font fallback.
         *
         * You can set up to 64 custom fallback families including the first font family you passed
         * to the constructor.
         * For more information about fallback, see class description.
         *
         * @param family
         * 		a fallback family
         * @throws IllegalArgumentException
         * 		if you give more than 64 custom fallback families
         */
        @android.annotation.NonNull
        public android.graphics.Typeface.CustomFallbackBuilder addCustomFallback(@android.annotation.NonNull
        android.graphics.fonts.FontFamily family) {
            com.android.internal.util.Preconditions.checkNotNull(family);
            com.android.internal.util.Preconditions.checkArgument(mFamilies.size() < android.graphics.Typeface.CustomFallbackBuilder.getMaxCustomFallbackCount(), ("Custom fallback limit exceeded(" + android.graphics.Typeface.CustomFallbackBuilder.getMaxCustomFallbackCount()) + ")");
            mFamilies.add(family);
            return this;
        }

        /**
         * Create the Typeface based on the configured values.
         *
         * @return the Typeface object
         */
        @android.annotation.NonNull
        public android.graphics.Typeface build() {
            final int userFallbackSize = mFamilies.size();
            final android.graphics.fonts.FontFamily[] fallback = android.graphics.fonts.SystemFonts.getSystemFallback(mFallbackName);
            final long[] ptrArray = new long[fallback.length + userFallbackSize];
            for (int i = 0; i < userFallbackSize; ++i) {
                ptrArray[i] = mFamilies.get(i).getNativePtr();
            }
            for (int i = 0; i < fallback.length; ++i) {
                ptrArray[i + userFallbackSize] = fallback[i].getNativePtr();
            }
            final int weight = (mStyle == null) ? 400 : mStyle.getWeight();
            final int italic = ((mStyle == null) || (mStyle.getSlant() == android.graphics.fonts.FontStyle.FONT_SLANT_UPRIGHT)) ? 0 : 1;
            return new android.graphics.Typeface(android.graphics.Typeface.nativeCreateFromArray(ptrArray, weight, italic));
        }
    }

    /**
     * Create a typeface object given a family name, and option style information.
     * If null is passed for the name, then the "default" font will be chosen.
     * The resulting typeface object can be queried (getStyle()) to discover what
     * its "real" style characteristics are.
     *
     * @param familyName
     * 		May be null. The name of the font family.
     * @param style
     * 		The style (normal, bold, italic) of the typeface.
     * 		e.g. NORMAL, BOLD, ITALIC, BOLD_ITALIC
     * @return The best matching typeface.
     */
    public static android.graphics.Typeface create(java.lang.String familyName, @android.graphics.Typeface.Style
    int style) {
        return android.graphics.Typeface.create(android.graphics.Typeface.getSystemDefaultTypeface(familyName), style);
    }

    /**
     * Create a typeface object that best matches the specified existing
     * typeface and the specified Style. Use this call if you want to pick a new
     * style from the same family of an existing typeface object. If family is
     * null, this selects from the default font's family.
     *
     * <p>
     * This method is not thread safe on API 27 or before.
     * This method is thread safe on API 28 or after.
     * </p>
     *
     * @param family
     * 		An existing {@link Typeface} object. In case of {@code null}, the default
     * 		typeface is used instead.
     * @param style
     * 		The style (normal, bold, italic) of the typeface.
     * 		e.g. NORMAL, BOLD, ITALIC, BOLD_ITALIC
     * @return The best matching typeface.
     */
    public static android.graphics.Typeface create(android.graphics.Typeface family, @android.graphics.Typeface.Style
    int style) {
        if ((style & (~android.graphics.Typeface.STYLE_MASK)) != 0) {
            style = android.graphics.Typeface.NORMAL;
        }
        if (family == null) {
            family = android.graphics.Typeface.sDefaultTypeface;
        }
        // Return early if we're asked for the same face/style
        if (family.mStyle == style) {
            return family;
        }
        final long ni = family.native_instance;
        android.graphics.Typeface typeface;
        synchronized(android.graphics.Typeface.sStyledCacheLock) {
            android.util.SparseArray<android.graphics.Typeface> styles = android.graphics.Typeface.sStyledTypefaceCache.get(ni);
            if (styles == null) {
                styles = new android.util.SparseArray<android.graphics.Typeface>(4);
                android.graphics.Typeface.sStyledTypefaceCache.put(ni, styles);
            } else {
                typeface = styles.get(style);
                if (typeface != null) {
                    return typeface;
                }
            }
            typeface = new android.graphics.Typeface(android.graphics.Typeface.nativeCreateFromTypeface(ni, style));
            styles.put(style, typeface);
        }
        return typeface;
    }

    /**
     * Creates a typeface object that best matches the specified existing typeface and the specified
     * weight and italic style
     * <p>Below are numerical values and corresponding common weight names.</p>
     * <table>
     * <thead>
     * <tr><th>Value</th><th>Common weight name</th></tr>
     * </thead>
     * <tbody>
     * <tr><td>100</td><td>Thin</td></tr>
     * <tr><td>200</td><td>Extra Light</td></tr>
     * <tr><td>300</td><td>Light</td></tr>
     * <tr><td>400</td><td>Normal</td></tr>
     * <tr><td>500</td><td>Medium</td></tr>
     * <tr><td>600</td><td>Semi Bold</td></tr>
     * <tr><td>700</td><td>Bold</td></tr>
     * <tr><td>800</td><td>Extra Bold</td></tr>
     * <tr><td>900</td><td>Black</td></tr>
     * </tbody>
     * </table>
     *
     * <p>
     * This method is thread safe.
     * </p>
     *
     * @param family
     * 		An existing {@link Typeface} object. In case of {@code null}, the default
     * 		typeface is used instead.
     * @param weight
     * 		The desired weight to be drawn.
     * @param italic
     * 		{@code true} if italic style is desired to be drawn. Otherwise, {@code false}
     * @return A {@link Typeface} object for drawing specified weight and italic style. Never
    returns {@code null}
     * @see #getWeight()
     * @see #isItalic()
     */
    @android.annotation.NonNull
    public static android.graphics.Typeface create(@android.annotation.Nullable
    android.graphics.Typeface family, @android.annotation.IntRange(from = 1, to = 1000)
    int weight, boolean italic) {
        com.android.internal.util.Preconditions.checkArgumentInRange(weight, 0, 1000, "weight");
        if (family == null) {
            family = android.graphics.Typeface.sDefaultTypeface;
        }
        return android.graphics.Typeface.createWeightStyle(family, weight, italic);
    }

    @android.annotation.NonNull
    private static android.graphics.Typeface createWeightStyle(@android.annotation.NonNull
    android.graphics.Typeface base, @android.annotation.IntRange(from = 1, to = 1000)
    int weight, boolean italic) {
        final int key = (weight << 1) | (italic ? 1 : 0);
        android.graphics.Typeface typeface;
        synchronized(android.graphics.Typeface.sWeightCacheLock) {
            android.util.SparseArray<android.graphics.Typeface> innerCache = android.graphics.Typeface.sWeightTypefaceCache.get(base.native_instance);
            if (innerCache == null) {
                innerCache = new android.util.SparseArray(4);
                android.graphics.Typeface.sWeightTypefaceCache.put(base.native_instance, innerCache);
            } else {
                typeface = innerCache.get(key);
                if (typeface != null) {
                    return typeface;
                }
            }
            typeface = new android.graphics.Typeface(android.graphics.Typeface.nativeCreateFromTypefaceWithExactStyle(base.native_instance, weight, italic));
            innerCache.put(key, typeface);
        }
        return typeface;
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.graphics.Typeface createFromTypefaceWithVariation(@android.annotation.Nullable
    android.graphics.Typeface family, @android.annotation.NonNull
    java.util.List<android.graphics.fonts.FontVariationAxis> axes) {
        final android.graphics.Typeface base = (family == null) ? android.graphics.Typeface.DEFAULT : family;
        return new android.graphics.Typeface(android.graphics.Typeface.nativeCreateFromTypefaceWithVariation(base.native_instance, axes));
    }

    /**
     * Returns one of the default typeface objects, based on the specified style
     *
     * @return the default typeface that corresponds to the style
     */
    public static android.graphics.Typeface defaultFromStyle(@android.graphics.Typeface.Style
    int style) {
        return android.graphics.Typeface.sDefaults[style];
    }

    /**
     * Create a new typeface from the specified font data.
     *
     * @param mgr
     * 		The application's asset manager
     * @param path
     * 		The file name of the font data in the assets directory
     * @return The new typeface.
     */
    public static android.graphics.Typeface createFromAsset(android.content.res.AssetManager mgr, java.lang.String path) {
        com.android.internal.util.Preconditions.checkNotNull(path);// for backward compatibility

        com.android.internal.util.Preconditions.checkNotNull(mgr);
        android.graphics.Typeface typeface = new android.graphics.Typeface.Builder(mgr, path).build();
        if (typeface != null)
            return typeface;

        // check if the file exists, and throw an exception for backward compatibility
        try (java.io.InputStream inputStream = mgr.open(path)) {
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException("Font asset not found " + path);
        }
        return android.graphics.Typeface.DEFAULT;
    }

    /**
     * Creates a unique id for a given font provider and query.
     */
    private static java.lang.String createProviderUid(java.lang.String authority, java.lang.String query) {
        final java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("provider:");
        builder.append(authority);
        builder.append("-");
        builder.append(query);
        return builder.toString();
    }

    /**
     * Create a new typeface from the specified font file.
     *
     * @param file
     * 		The path to the font data.
     * @return The new typeface.
     */
    public static android.graphics.Typeface createFromFile(@android.annotation.Nullable
    java.io.File file) {
        // For the compatibility reasons, leaving possible NPE here.
        // See android.graphics.cts.TypefaceTest#testCreateFromFileByFileReferenceNull
        android.graphics.Typeface typeface = new android.graphics.Typeface.Builder(file).build();
        if (typeface != null)
            return typeface;

        // check if the file exists, and throw an exception for backward compatibility
        if (!file.exists()) {
            throw new java.lang.RuntimeException("Font asset not found " + file.getAbsolutePath());
        }
        return android.graphics.Typeface.DEFAULT;
    }

    /**
     * Create a new typeface from the specified font file.
     *
     * @param path
     * 		The full path to the font data.
     * @return The new typeface.
     */
    public static android.graphics.Typeface createFromFile(@android.annotation.Nullable
    java.lang.String path) {
        com.android.internal.util.Preconditions.checkNotNull(path);// for backward compatibility

        return android.graphics.Typeface.createFromFile(new java.io.File(path));
    }

    /**
     * Create a new typeface from an array of font families.
     *
     * @param families
     * 		array of font families
     * @deprecated 
     */
    @java.lang.Deprecated
    @android.annotation.UnsupportedAppUsage(trackingBug = 123768928)
    private static android.graphics.Typeface createFromFamilies(android.graphics.FontFamily[] families) {
        long[] ptrArray = new long[families.length];
        for (int i = 0; i < families.length; i++) {
            ptrArray[i] = families[i].mNativePtr;
        }
        return new android.graphics.Typeface(android.graphics.Typeface.nativeCreateFromArray(ptrArray, android.graphics.Typeface.RESOLVE_BY_FONT_TABLE, android.graphics.Typeface.RESOLVE_BY_FONT_TABLE));
    }

    /**
     * Create a new typeface from an array of android.graphics.fonts.FontFamily.
     *
     * @param families
     * 		array of font families
     */
    private static android.graphics.Typeface createFromFamilies(@android.annotation.Nullable
    android.graphics.fonts.FontFamily[] families) {
        final long[] ptrArray = new long[families.length];
        for (int i = 0; i < families.length; ++i) {
            ptrArray[i] = families[i].getNativePtr();
        }
        return new android.graphics.Typeface(android.graphics.Typeface.nativeCreateFromArray(ptrArray, android.graphics.Typeface.RESOLVE_BY_FONT_TABLE, android.graphics.Typeface.RESOLVE_BY_FONT_TABLE));
    }

    /**
     * This method is used by supportlib-v27.
     *
     * @deprecated Use {@link android.graphics.fonts.FontFamily} instead.
     */
    @android.annotation.UnsupportedAppUsage(trackingBug = 123768395)
    @java.lang.Deprecated
    private static android.graphics.Typeface createFromFamiliesWithDefault(android.graphics.FontFamily[] families, int weight, int italic) {
        return android.graphics.Typeface.createFromFamiliesWithDefault(families, android.graphics.Typeface.DEFAULT_FAMILY, weight, italic);
    }

    /**
     * Create a new typeface from an array of font families, including
     * also the font families in the fallback list.
     *
     * @param fallbackName
     * 		the family name. If given families don't support characters, the
     * 		characters will be rendered with this family.
     * @param weight
     * 		the weight for this family. In that case, the table information in the first
     * 		family's font is used. If the first family has multiple fonts, the closest to
     * 		the regular weight and upright font is used.
     * @param italic
     * 		the italic information for this family. In that case, the table information in
     * 		the first family's font is used. If the first family has multiple fonts, the
     * 		closest to the regular weight and upright font is used.
     * @param families
     * 		array of font families
     * @deprecated Use {@link android.graphics.fonts.FontFamily} instead.
     */
    @android.annotation.UnsupportedAppUsage(trackingBug = 123768928)
    @java.lang.Deprecated
    private static android.graphics.Typeface createFromFamiliesWithDefault(android.graphics.FontFamily[] families, java.lang.String fallbackName, int weight, int italic) {
        android.graphics.fonts.FontFamily[] fallback = android.graphics.fonts.SystemFonts.getSystemFallback(fallbackName);
        long[] ptrArray = new long[families.length + fallback.length];
        for (int i = 0; i < families.length; i++) {
            ptrArray[i] = families[i].mNativePtr;
        }
        for (int i = 0; i < fallback.length; i++) {
            ptrArray[i + families.length] = fallback[i].getNativePtr();
        }
        return new android.graphics.Typeface(android.graphics.Typeface.nativeCreateFromArray(ptrArray, weight, italic));
    }

    // don't allow clients to call this directly
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private Typeface(long ni) {
        if (ni == 0) {
            throw new java.lang.RuntimeException("native typeface cannot be made");
        }
        native_instance = ni;
        android.graphics.Typeface.sRegistry.registerNativeAllocation(this, native_instance);
        mStyle = android.graphics.Typeface.nativeGetStyle(ni);
        mWeight = android.graphics.Typeface.nativeGetWeight(ni);
    }

    private static android.graphics.Typeface getSystemDefaultTypeface(@android.annotation.NonNull
    java.lang.String familyName) {
        android.graphics.Typeface tf = android.graphics.Typeface.sSystemFontMap.get(familyName);
        return tf == null ? android.graphics.Typeface.DEFAULT : tf;
    }

    /**
     *
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public static void initSystemDefaultTypefaces(java.util.Map<java.lang.String, android.graphics.Typeface> systemFontMap, java.util.Map<java.lang.String, android.graphics.fonts.FontFamily[]> fallbacks, android.text.FontConfig[] aliases) {
        for (java.util.Map.Entry<java.lang.String, android.graphics.fonts.FontFamily[]> entry : fallbacks.entrySet()) {
            systemFontMap.put(entry.getKey(), android.graphics.Typeface.createFromFamilies(entry.getValue()));
        }
        for (android.text.FontConfig.Alias alias : aliases) {
            if (systemFontMap.containsKey(alias.getName())) {
                continue;// If alias and named family are conflict, use named family.

            }
            final android.graphics.Typeface base = systemFontMap.get(alias.getToName());
            if (base == null) {
                // The missing target is a valid thing, some configuration don't have font files,
                // e.g. wear devices. Just skip this alias.
                continue;
            }
            final int weight = alias.getWeight();
            final android.graphics.Typeface newFace = (weight == 400) ? base : new android.graphics.Typeface(android.graphics.Typeface.nativeCreateWeightAlias(base.native_instance, weight));
            systemFontMap.put(alias.getName(), newFace);
        }
    }

    private static void registerGenericFamilyNative(@android.annotation.NonNull
    java.lang.String familyName, @android.annotation.Nullable
    android.graphics.Typeface typeface) {
        if (typeface != null) {
            android.graphics.Typeface.nativeRegisterGenericFamily(familyName, typeface.native_instance);
        }
    }

    static {
        final java.util.HashMap<java.lang.String, android.graphics.Typeface> systemFontMap = new java.util.HashMap<>();
        android.graphics.Typeface.initSystemDefaultTypefaces(systemFontMap, android.graphics.fonts.SystemFonts.getRawSystemFallbackMap(), android.graphics.fonts.SystemFonts.getAliases());
        sSystemFontMap = java.util.Collections.unmodifiableMap(systemFontMap);
        // We can't assume DEFAULT_FAMILY available on Roboletric.
        if (android.graphics.Typeface.sSystemFontMap.containsKey(android.graphics.Typeface.DEFAULT_FAMILY)) {
            android.graphics.Typeface.setDefault(android.graphics.Typeface.sSystemFontMap.get(android.graphics.Typeface.DEFAULT_FAMILY));
        }
        // Set up defaults and typefaces exposed in public API
        DEFAULT = android.graphics.Typeface.create(((java.lang.String) (null)), 0);
        DEFAULT_BOLD = android.graphics.Typeface.create(((java.lang.String) (null)), android.graphics.Typeface.BOLD);
        SANS_SERIF = android.graphics.Typeface.create("sans-serif", 0);
        SERIF = android.graphics.Typeface.create("serif", 0);
        MONOSPACE = android.graphics.Typeface.create("monospace", 0);
        android.graphics.Typeface.sDefaults = new android.graphics.Typeface[]{ android.graphics.Typeface.DEFAULT, android.graphics.Typeface.DEFAULT_BOLD, android.graphics.Typeface.create(((java.lang.String) (null)), android.graphics.Typeface.ITALIC), android.graphics.Typeface.create(((java.lang.String) (null)), android.graphics.Typeface.BOLD_ITALIC) };
        // A list of generic families to be registered in native.
        // https://www.w3.org/TR/css-fonts-4/#generic-font-families
        java.lang.String[] genericFamilies = new java.lang.String[]{ "serif", "sans-serif", "cursive", "fantasy", "monospace", "system-ui" };
        for (java.lang.String genericFamily : genericFamilies) {
            android.graphics.Typeface.registerGenericFamilyNative(genericFamily, systemFontMap.get(genericFamily));
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        android.graphics.Typeface typeface = ((android.graphics.Typeface) (o));
        return (mStyle == typeface.mStyle) && (native_instance == typeface.native_instance);
    }

    @java.lang.Override
    public int hashCode() {
        /* Modified method for hashCode with long native_instance derived from
        http://developer.android.com/reference/java/lang/Object.html
         */
        int result = 17;
        result = (31 * result) + ((int) (native_instance ^ (native_instance >>> 32)));
        result = (31 * result) + mStyle;
        return result;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isSupportedAxes(int axis) {
        if (mSupportedAxes == null) {
            synchronized(this) {
                if (mSupportedAxes == null) {
                    mSupportedAxes = android.graphics.Typeface.nativeGetSupportedAxes(native_instance);
                    if (mSupportedAxes == null) {
                        mSupportedAxes = android.graphics.Typeface.EMPTY_AXES;
                    }
                }
            }
        }
        return java.util.Arrays.binarySearch(mSupportedAxes, axis) >= 0;
    }

    private static native long nativeCreateFromTypeface(long native_instance, int style);

    private static native long nativeCreateFromTypefaceWithExactStyle(long native_instance, int weight, boolean italic);

    // TODO: clean up: change List<FontVariationAxis> to FontVariationAxis[]
    private static native long nativeCreateFromTypefaceWithVariation(long native_instance, java.util.List<android.graphics.fonts.FontVariationAxis> axes);

    @android.annotation.UnsupportedAppUsage
    private static native long nativeCreateWeightAlias(long native_instance, int weight);

    @android.annotation.UnsupportedAppUsage
    private static native long nativeCreateFromArray(long[] familyArray, int weight, int italic);

    private static native int[] nativeGetSupportedAxes(long native_instance);

    @dalvik.annotation.optimization.CriticalNative
    private static native void nativeSetDefault(long nativePtr);

    @dalvik.annotation.optimization.CriticalNative
    private static native int nativeGetStyle(long nativePtr);

    @dalvik.annotation.optimization.CriticalNative
    private static native int nativeGetWeight(long nativePtr);

    @dalvik.annotation.optimization.CriticalNative
    private static native long nativeGetReleaseFunc();

    private static native void nativeRegisterGenericFamily(java.lang.String str, long nativePtr);
}

