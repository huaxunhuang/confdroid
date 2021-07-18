/**
 * Copyright (C) 2014 The Android Open Source Project
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
 * Delegate implementing the native methods of android.graphics.FontFamily
 *
 * Through the layoutlib_create tool, the original native methods of FontFamily have been replaced
 * by calls to methods of the same name in this delegate class.
 *
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between
 * it and the original FontFamily class.
 *
 * @see DelegateManager
 */
public class FontFamily_Delegate {
    public static final int DEFAULT_FONT_WEIGHT = 400;

    public static final int BOLD_FONT_WEIGHT_DELTA = 300;

    public static final int BOLD_FONT_WEIGHT = 700;

    private static final java.lang.String FONT_SUFFIX_ITALIC = "Italic.ttf";

    private static final java.lang.String FN_ALL_FONTS_LIST = "fontsInSdk.txt";

    private static final java.lang.String EXTENSION_OTF = ".otf";

    private static final int CACHE_SIZE = 10;

    // The cache has a drawback that if the font file changed after the font object was created,
    // we will not update it.
    private static final java.util.Map<java.lang.String, android.graphics.FontFamily_Delegate.FontInfo> sCache = new java.util.LinkedHashMap<java.lang.String, android.graphics.FontFamily_Delegate.FontInfo>(android.graphics.FontFamily_Delegate.CACHE_SIZE) {
        @java.lang.Override
        protected boolean removeEldestEntry(java.util.Map.Entry<java.lang.String, android.graphics.FontFamily_Delegate.FontInfo> eldest) {
            return size() > android.graphics.FontFamily_Delegate.CACHE_SIZE;
        }

        @java.lang.Override
        public android.graphics.FontFamily_Delegate.FontInfo put(java.lang.String key, android.graphics.FontFamily_Delegate.FontInfo value) {
            // renew this entry.
            android.graphics.FontFamily_Delegate.FontInfo removed = remove(key);
            super.put(key, value);
            return removed;
        }
    };

    /**
     * A class associating {@link Font} with its metadata.
     */
    public static final class FontInfo {
        @android.annotation.Nullable
        public java.awt.Font mFont;

        public int mWeight;

        public boolean mIsItalic;

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            android.graphics.FontFamily_Delegate.FontInfo fontInfo = ((android.graphics.FontFamily_Delegate.FontInfo) (o));
            return (mWeight == fontInfo.mWeight) && (mIsItalic == fontInfo.mIsItalic);
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(mWeight, mIsItalic);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((("FontInfo{" + "mWeight=") + mWeight) + ", mIsItalic=") + mIsItalic) + '}';
        }
    }

    // ---- delegate manager ----
    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.FontFamily_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.FontFamily_Delegate>(android.graphics.FontFamily_Delegate.class);

    private static long sFamilyFinalizer = -1;

    // ---- delegate helper data ----
    private static java.lang.String sFontLocation;

    private static final java.util.List<android.graphics.FontFamily_Delegate> sPostInitDelegate = new java.util.ArrayList<android.graphics.FontFamily_Delegate>();

    private static java.util.Set<java.lang.String> SDK_FONTS;

    // ---- delegate data ----
    // Order does not really matter but we use a LinkedHashMap to get reproducible results across
    // render calls
    private java.util.Map<android.graphics.FontFamily_Delegate.FontInfo, java.awt.Font> mFonts = new java.util.LinkedHashMap<>();

    /**
     * The variant of the Font Family - compact or elegant.
     * <p/>
     * 0 is unspecified, 1 is compact and 2 is elegant. This needs to be kept in sync with values in
     * android.graphics.FontFamily
     *
     * @see Paint#setElegantTextHeight(boolean)
     */
    private android.graphics.FontFamily_Delegate.FontVariant mVariant;

    // List of runnables to process fonts after sFontLoader is initialized.
    private java.util.List<java.lang.Runnable> mPostInitRunnables = new java.util.ArrayList<java.lang.Runnable>();

    /**
     *
     *
     * @see #isValid()
     */
    private boolean mValid = false;

    // ---- Public helper class ----
    public enum FontVariant {

        // The order needs to be kept in sync with android.graphics.FontFamily.
        NONE,
        COMPACT,
        ELEGANT;}

    // ---- Public Helper methods ----
    public static android.graphics.FontFamily_Delegate getDelegate(long nativeFontFamily) {
        return android.graphics.FontFamily_Delegate.sManager.getDelegate(nativeFontFamily);
    }

    public static synchronized void setFontLocation(java.lang.String fontLocation) {
        android.graphics.FontFamily_Delegate.sFontLocation = fontLocation;
        // init list of bundled fonts.
        java.io.File allFonts = new java.io.File(fontLocation, android.graphics.FontFamily_Delegate.FN_ALL_FONTS_LIST);
        // Current number of fonts is 103. Use the next round number to leave scope for more fonts
        // in the future.
        java.util.Set<java.lang.String> allFontsList = new java.util.HashSet<>(128);
        java.util.Scanner scanner = null;
        try {
            scanner = new java.util.Scanner(allFonts);
            while (scanner.hasNext()) {
                java.lang.String name = scanner.next();
                // Skip font configuration files.
                if (!name.endsWith(".xml")) {
                    allFontsList.add(name);
                }
            } 
        } catch (java.io.FileNotFoundException e) {
            com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "Unable to load the list of fonts. Try re-installing the SDK Platform from the SDK Manager.", e, null);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        android.graphics.FontFamily_Delegate.SDK_FONTS = java.util.Collections.unmodifiableSet(allFontsList);
        for (android.graphics.FontFamily_Delegate fontFamily : android.graphics.FontFamily_Delegate.sPostInitDelegate) {
            fontFamily.init();
        }
        android.graphics.FontFamily_Delegate.sPostInitDelegate.clear();
    }

    @android.annotation.Nullable
    public java.awt.Font getFont(int desiredWeight, boolean isItalic) {
        android.graphics.FontFamily_Delegate.FontInfo desiredStyle = new android.graphics.FontFamily_Delegate.FontInfo();
        desiredStyle.mWeight = desiredWeight;
        desiredStyle.mIsItalic = isItalic;
        java.awt.Font cachedFont = mFonts.get(desiredStyle);
        if (cachedFont != null) {
            return cachedFont;
        }
        android.graphics.FontFamily_Delegate.FontInfo bestFont = null;
        if (mFonts.size() == 1) {
            // No need to compute the match since we only have one candidate
            bestFont = mFonts.keySet().iterator().next();
        } else {
            int bestMatch = java.lang.Integer.MAX_VALUE;
            for (android.graphics.FontFamily_Delegate.FontInfo font : mFonts.keySet()) {
                int match = android.graphics.FontFamily_Delegate.computeMatch(font, desiredStyle);
                if (match < bestMatch) {
                    bestMatch = match;
                    bestFont = font;
                    if (bestMatch == 0) {
                        break;
                    }
                }
            }
        }
        if (bestFont == null) {
            return null;
        }
        // Derive the font as required and add it to the list of Fonts.
        android.graphics.FontFamily_Delegate.deriveFont(bestFont, desiredStyle);
        addFont(desiredStyle);
        return desiredStyle.mFont;
    }

    public android.graphics.FontFamily_Delegate.FontVariant getVariant() {
        return mVariant;
    }

    /**
     * Returns if the FontFamily should contain any fonts. If this returns true and
     * {@link #getFont(int, boolean)} returns an empty list, it means that an error occurred while
     * loading the fonts. However, some fonts are deliberately skipped, for example they are not
     * bundled with the SDK. In such a case, this method returns false.
     */
    public boolean isValid() {
        return mValid;
    }

    private static java.awt.Font loadFont(java.lang.String path) {
        if (path.startsWith(android.graphics.Typeface_Delegate.SYSTEM_FONTS)) {
            java.lang.String relativePath = path.substring(android.graphics.Typeface_Delegate.SYSTEM_FONTS.length());
            java.io.File f = new java.io.File(android.graphics.FontFamily_Delegate.sFontLocation, relativePath);
            try {
                return java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, f);
            } catch (java.lang.Exception e) {
                if (path.endsWith(android.graphics.FontFamily_Delegate.EXTENSION_OTF) && (e instanceof java.awt.FontFormatException)) {
                    // If we aren't able to load an Open Type font, don't log a warning just yet.
                    // We wait for a case where font is being used. Only then we try to log the
                    // warning.
                    return null;
                }
                com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_BROKEN, java.lang.String.format("Unable to load font %1$s", relativePath), e, null);
            }
        } else {
            com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, ("Only platform fonts located in " + android.graphics.Typeface_Delegate.SYSTEM_FONTS) + "can be loaded.", null, null);
        }
        return null;
    }

    @android.annotation.Nullable
    public static java.lang.String getFontLocation() {
        return android.graphics.FontFamily_Delegate.sFontLocation;
    }

    // ---- delegate methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean addFont(android.graphics.FontFamily thisFontFamily, java.lang.String path, int ttcIndex, android.graphics.fonts.FontVariationAxis[] axes, int weight, int italic) {
        if (thisFontFamily.mBuilderPtr == 0) {
            assert false : "Unable to call addFont after freezing.";
            return false;
        }
        final android.graphics.FontFamily_Delegate delegate = android.graphics.FontFamily_Delegate.getDelegate(thisFontFamily.mBuilderPtr);
        return (delegate != null) && delegate.addFont(path, ttcIndex, weight, italic);
    }

    // ---- native methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nInitBuilder(java.lang.String lang, int variant) {
        // TODO: support lang. This is required for japanese locale.
        android.graphics.FontFamily_Delegate delegate = new android.graphics.FontFamily_Delegate();
        // variant can be 0, 1 or 2.
        assert variant < 3;
        delegate.mVariant = android.graphics.FontFamily_Delegate.FontVariant.values()[variant];
        if (android.graphics.FontFamily_Delegate.sFontLocation != null) {
            delegate.init();
        } else {
            android.graphics.FontFamily_Delegate.sPostInitDelegate.add(delegate);
        }
        return android.graphics.FontFamily_Delegate.sManager.addNewDelegate(delegate);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreateFamily(long builderPtr) {
        return builderPtr;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nGetFamilyReleaseFunc() {
        synchronized(android.graphics.FontFamily_Delegate.class) {
            if (android.graphics.FontFamily_Delegate.sFamilyFinalizer == (-1)) {
                android.graphics.FontFamily_Delegate.sFamilyFinalizer = libcore.util.NativeAllocationRegistry_Delegate.createFinalizer(android.graphics.FontFamily_Delegate.sManager::removeJavaReferenceFor);
            }
        }
        return android.graphics.FontFamily_Delegate.sFamilyFinalizer;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nAddFont(long builderPtr, java.nio.ByteBuffer font, int ttcIndex, int weight, int isItalic) {
        assert false : "The only client of this method has been overridden.";
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nAddFontWeightStyle(long builderPtr, java.nio.ByteBuffer font, int ttcIndex, int weight, int isItalic) {
        assert false : "The only client of this method has been overridden.";
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nAddAxisValue(long builderPtr, int tag, float value) {
        assert false : "The only client of this method has been overridden.";
    }

    static boolean addFont(long builderPtr, final java.lang.String path, final int weight, final boolean isItalic) {
        final android.graphics.FontFamily_Delegate delegate = android.graphics.FontFamily_Delegate.getDelegate(builderPtr);
        int italic = (isItalic) ? 1 : 0;
        if (delegate != null) {
            if (android.graphics.FontFamily_Delegate.sFontLocation == null) {
                delegate.mPostInitRunnables.add(() -> delegate.addFont(path, weight, italic));
                return true;
            }
            return delegate.addFont(path, weight, italic);
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nAddFontFromAssetManager(long builderPtr, android.content.res.AssetManager mgr, java.lang.String path, int cookie, boolean isAsset, int ttcIndex, int weight, int isItalic) {
        android.graphics.FontFamily_Delegate ffd = android.graphics.FontFamily_Delegate.sManager.getDelegate(builderPtr);
        if (ffd == null) {
            return false;
        }
        ffd.mValid = true;
        if (mgr == null) {
            return false;
        }
        if (mgr instanceof android.content.res.BridgeAssetManager) {
            java.io.InputStream fontStream = null;
            try {
                com.android.ide.common.rendering.api.AssetRepository assetRepository = ((android.content.res.BridgeAssetManager) (mgr)).getAssetRepository();
                if (assetRepository == null) {
                    com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_MISSING_ASSET, "Asset not found: " + path, null);
                    return false;
                }
                if (!assetRepository.isSupported()) {
                    // Don't log any warnings on unsupported IDEs.
                    return false;
                }
                // Check cache
                android.graphics.FontFamily_Delegate.FontInfo fontInfo = android.graphics.FontFamily_Delegate.sCache.get(path);
                if (fontInfo != null) {
                    // renew the font's lease.
                    android.graphics.FontFamily_Delegate.sCache.put(path, fontInfo);
                    ffd.addFont(fontInfo);
                    return true;
                }
                fontStream = (isAsset) ? assetRepository.openAsset(path, android.content.res.AssetManager.ACCESS_STREAMING) : assetRepository.openNonAsset(cookie, path, android.content.res.AssetManager.ACCESS_STREAMING);
                if (fontStream == null) {
                    com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_MISSING_ASSET, "Asset not found: " + path, path);
                    return false;
                }
                java.awt.Font font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, fontStream);
                fontInfo = new android.graphics.FontFamily_Delegate.FontInfo();
                fontInfo.mFont = font;
                if (weight == android.graphics.Typeface.RESOLVE_BY_FONT_TABLE) {
                    fontInfo.mWeight = sun.font.FontUtilities.getFont2D(font).getWeight();
                } else {
                    fontInfo.mWeight = weight;
                }
                if (isItalic == android.graphics.Typeface.RESOLVE_BY_FONT_TABLE) {
                    fontInfo.mIsItalic = (sun.font.FontUtilities.getFont2D(font).getStyle() & java.awt.Font.ITALIC) != 0;
                } else {
                    fontInfo.mIsItalic = isItalic == 1;
                }
                ffd.addFont(fontInfo);
                return true;
            } catch (java.io.IOException e) {
                com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_MISSING_ASSET, "Unable to load font " + path, e, path);
            } catch (java.awt.FontFormatException e) {
                if (path.endsWith(android.graphics.FontFamily_Delegate.EXTENSION_OTF)) {
                    // otf fonts are not supported on the user's config (JRE version + OS)
                    com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "OpenType fonts are not supported yet: " + path, null, path);
                } else {
                    com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "Unable to load font " + path, e, path);
                }
            } finally {
                if (fontStream != null) {
                    try {
                        fontStream.close();
                    } catch (java.io.IOException ignored) {
                    }
                }
            }
            return false;
        }
        // This should never happen. AssetManager is a final class (from user's perspective), and
        // we've replaced every creation of AssetManager with our implementation. We create an
        // exception and log it, but continue with rest of the rendering, without loading this font.
        com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, "You have found a bug in the rendering library. Please file a bug at b.android.com.", new java.lang.RuntimeException("Asset Manager is not an instance of BridgeAssetManager"), null);
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nGetBuilderReleaseFunc() {
        // Layoutlib uses the same reference for the builder and the font family,
        // so it should not release that reference at the builder stage.
        return -1;
    }

    // ---- private helper methods ----
    private void init() {
        for (java.lang.Runnable postInitRunnable : mPostInitRunnables) {
            postInitRunnable.run();
        }
        mPostInitRunnables = null;
    }

    private boolean addFont(final java.lang.String path, int ttcIndex, int weight, int italic) {
        // FIXME: support ttc fonts. Hack JRE??
        if (android.graphics.FontFamily_Delegate.sFontLocation == null) {
            mPostInitRunnables.add(() -> addFont(path, weight, italic));
            return true;
        }
        return addFont(path, weight, italic);
    }

    private boolean addFont(@android.annotation.NonNull
    java.lang.String path) {
        return addFont(path, android.graphics.FontFamily_Delegate.DEFAULT_FONT_WEIGHT, path.endsWith(android.graphics.FontFamily_Delegate.FONT_SUFFIX_ITALIC) ? 1 : android.graphics.Typeface.RESOLVE_BY_FONT_TABLE);
    }

    private boolean addFont(@android.annotation.NonNull
    java.lang.String path, int weight, int italic) {
        if (path.startsWith(android.graphics.Typeface_Delegate.SYSTEM_FONTS) && (!android.graphics.FontFamily_Delegate.SDK_FONTS.contains(path.substring(android.graphics.Typeface_Delegate.SYSTEM_FONTS.length())))) {
            java.util.logging.Logger.getLogger(android.graphics.FontFamily_Delegate.class.getSimpleName()).warning("Unable to load font " + path);
            return mValid = false;
        }
        // Set valid to true, even if the font fails to load.
        mValid = true;
        java.awt.Font font = android.graphics.FontFamily_Delegate.loadFont(path);
        if (font == null) {
            return false;
        }
        android.graphics.FontFamily_Delegate.FontInfo fontInfo = new android.graphics.FontFamily_Delegate.FontInfo();
        fontInfo.mFont = font;
        fontInfo.mWeight = weight;
        fontInfo.mIsItalic = (italic == android.graphics.Typeface.RESOLVE_BY_FONT_TABLE) ? font.isItalic() : italic == 1;
        addFont(fontInfo);
        return true;
    }

    private boolean addFont(@android.annotation.NonNull
    android.graphics.FontFamily_Delegate.FontInfo fontInfo) {
        return mFonts.putIfAbsent(fontInfo, fontInfo.mFont) == null;
    }

    /**
     * Compute matching metric between two styles - 0 is an exact match.
     */
    public static int computeMatch(@android.annotation.NonNull
    android.graphics.FontFamily_Delegate.FontInfo font1, @android.annotation.NonNull
    android.graphics.FontFamily_Delegate.FontInfo font2) {
        int score = java.lang.Math.abs((font1.mWeight / 100) - (font2.mWeight / 100));
        if (font1.mIsItalic != font2.mIsItalic) {
            score += 2;
        }
        return score;
    }

    /**
     * Try to derive a font from {@code srcFont} for the style in {@code outFont}.
     * <p/>
     * {@code outFont} is updated to reflect the style of the derived font.
     *
     * @param srcFont
     * 		the source font
     * @param outFont
     * 		contains the desired font style. Updated to contain the derived font and
     * 		its style
     */
    public static void deriveFont(@android.annotation.NonNull
    android.graphics.FontFamily_Delegate.FontInfo srcFont, @android.annotation.NonNull
    android.graphics.FontFamily_Delegate.FontInfo outFont) {
        int desiredWeight = outFont.mWeight;
        int srcWeight = srcFont.mWeight;
        assert srcFont.mFont != null;
        java.awt.Font derivedFont = srcFont.mFont;
        int derivedStyle = 0;
        // Embolden the font if required.
        if ((desiredWeight >= android.graphics.FontFamily_Delegate.BOLD_FONT_WEIGHT) && ((desiredWeight - srcWeight) > (android.graphics.FontFamily_Delegate.BOLD_FONT_WEIGHT_DELTA / 2))) {
            derivedStyle |= java.awt.Font.BOLD;
            srcWeight += android.graphics.FontFamily_Delegate.BOLD_FONT_WEIGHT_DELTA;
        }
        // Italicize the font if required.
        if (outFont.mIsItalic && (!srcFont.mIsItalic)) {
            derivedStyle |= java.awt.Font.ITALIC;
        } else
            if (outFont.mIsItalic != srcFont.mIsItalic) {
                // The desired font is plain, but the src font is italics. We can't convert it back. So
                // we update the value to reflect the true style of the font we're deriving.
                outFont.mIsItalic = srcFont.mIsItalic;
            }

        if (derivedStyle != 0) {
            derivedFont = derivedFont.deriveFont(derivedStyle);
        }
        outFont.mFont = derivedFont;
        outFont.mWeight = srcWeight;
        // No need to update mIsItalics, as it's already been handled above.
    }
}

