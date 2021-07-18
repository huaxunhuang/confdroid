/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * Delegate implementing the native methods of android.graphics.Typeface
 * <p>
 * Through the layoutlib_create tool, the original native methods of Typeface have been replaced by
 * calls to methods of the same name in this delegate class.
 * <p>
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between it
 * and the original Typeface class.
 *
 * @see DelegateManager
 */
public final class Typeface_Delegate {
    public static final java.lang.String SYSTEM_FONTS = "/system/fonts/";

    public static final java.util.Map<java.lang.String, android.graphics.FontFamily_Delegate[]> sGenericNativeFamilies = new java.util.HashMap<>();

    // ---- delegate manager ----
    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.Typeface_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager(android.graphics.Typeface_Delegate.class);

    private static long sFinalizer = -1;

    // ---- delegate data ----
    private static long sDefaultTypeface;

    @android.annotation.NonNull
    private final android.graphics.FontFamily_Delegate[] mFontFamilies;// the reference to FontFamily_Delegate.


    @android.annotation.NonNull
    private final android.graphics.fonts.FontFamily_Builder_Delegate[] mFontFamilyBuilders;// the reference to


    // FontFamily_Builder_Delegate.
    /**
     *
     *
     * @see Font#getStyle()
     */
    private final int mStyle;

    private final int mWeight;

    // ---- Public Helper methods ----
    private Typeface_Delegate(@android.annotation.NonNull
    android.graphics.FontFamily_Delegate[] fontFamilies, @android.annotation.NonNull
    android.graphics.fonts.FontFamily_Builder_Delegate[] fontFamilyBuilders, int style, int weight) {
        mFontFamilies = fontFamilies;
        mFontFamilyBuilders = fontFamilyBuilders;
        mStyle = style;
        mWeight = weight;
    }

    public static android.graphics.Typeface_Delegate getDelegate(long nativeTypeface) {
        return android.graphics.Typeface_Delegate.sManager.getDelegate(nativeTypeface);
    }

    // ---- native methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static synchronized long nativeCreateFromTypeface(long native_instance, int style) {
        android.graphics.Typeface_Delegate delegate = android.graphics.Typeface_Delegate.sManager.getDelegate(native_instance);
        if (delegate == null) {
            delegate = android.graphics.Typeface_Delegate.sManager.getDelegate(android.graphics.Typeface_Delegate.sDefaultTypeface);
        }
        if (delegate == null) {
            return 0;
        }
        return android.graphics.Typeface_Delegate.sManager.addNewDelegate(new android.graphics.Typeface_Delegate(delegate.mFontFamilies, delegate.mFontFamilyBuilders, style, delegate.mWeight));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nativeCreateFromTypefaceWithExactStyle(long native_instance, int weight, boolean italic) {
        android.graphics.Typeface_Delegate delegate = android.graphics.Typeface_Delegate.sManager.getDelegate(native_instance);
        if (delegate == null) {
            delegate = android.graphics.Typeface_Delegate.sManager.getDelegate(android.graphics.Typeface_Delegate.sDefaultTypeface);
        }
        if (delegate == null) {
            return 0;
        }
        int style = (weight >= 600) ? italic ? android.graphics.Typeface.BOLD_ITALIC : android.graphics.Typeface.BOLD : italic ? android.graphics.Typeface.ITALIC : android.graphics.Typeface.NORMAL;
        return android.graphics.Typeface_Delegate.sManager.addNewDelegate(new android.graphics.Typeface_Delegate(delegate.mFontFamilies, delegate.mFontFamilyBuilders, style, weight));
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static synchronized long nativeCreateFromTypefaceWithVariation(long native_instance, java.util.List<android.graphics.fonts.FontVariationAxis> axes) {
        long newInstance = android.graphics.Typeface_Delegate.nativeCreateFromTypeface(native_instance, 0);
        if (newInstance != 0) {
            com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "nativeCreateFromTypefaceWithVariation is not supported", null, null);
        }
        return newInstance;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static synchronized int[] nativeGetSupportedAxes(long native_instance) {
        // nativeCreateFromTypefaceWithVariation is not supported so we do not keep the axes
        return null;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nativeCreateWeightAlias(long native_instance, int weight) {
        android.graphics.Typeface_Delegate delegate = android.graphics.Typeface_Delegate.sManager.getDelegate(native_instance);
        if (delegate == null) {
            delegate = android.graphics.Typeface_Delegate.sManager.getDelegate(android.graphics.Typeface_Delegate.sDefaultTypeface);
        }
        if (delegate == null) {
            return 0;
        }
        android.graphics.Typeface_Delegate weightAlias = new android.graphics.Typeface_Delegate(delegate.mFontFamilies, delegate.mFontFamilyBuilders, delegate.mStyle, weight);
        return android.graphics.Typeface_Delegate.sManager.addNewDelegate(weightAlias);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static synchronized long nativeCreateFromArray(long[] familyArray, int weight, int italic) {
        java.util.List<android.graphics.FontFamily_Delegate> fontFamilies = new java.util.ArrayList<>();
        java.util.List<android.graphics.fonts.FontFamily_Builder_Delegate> fontFamilyBuilders = new java.util.ArrayList<>();
        for (long aFamilyArray : familyArray) {
            try {
                fontFamilies.add(android.graphics.FontFamily_Delegate.getDelegate(aFamilyArray));
            } catch (java.lang.ClassCastException e) {
                fontFamilyBuilders.add(android.graphics.fonts.FontFamily_Builder_Delegate.getDelegate(aFamilyArray));
            }
        }
        if (weight == android.graphics.Typeface.RESOLVE_BY_FONT_TABLE) {
            weight = 400;
        }
        if (italic == android.graphics.Typeface.RESOLVE_BY_FONT_TABLE) {
            italic = 0;
        }
        int style = (weight >= 600) ? italic == 1 ? android.graphics.Typeface.BOLD_ITALIC : android.graphics.Typeface.BOLD : italic == 1 ? android.graphics.Typeface.ITALIC : android.graphics.Typeface.NORMAL;
        android.graphics.Typeface_Delegate delegate = new android.graphics.Typeface_Delegate(fontFamilies.toArray(new android.graphics.FontFamily_Delegate[0]), fontFamilyBuilders.toArray(new android.graphics.fonts.FontFamily_Builder_Delegate[0]), style, weight);
        return android.graphics.Typeface_Delegate.sManager.addNewDelegate(delegate);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nativeGetReleaseFunc() {
        synchronized(android.graphics.Typeface_Delegate.class) {
            if (android.graphics.Typeface_Delegate.sFinalizer == (-1)) {
                android.graphics.Typeface_Delegate.sFinalizer = libcore.util.NativeAllocationRegistry_Delegate.createFinalizer(android.graphics.Typeface_Delegate.sManager::removeJavaReferenceFor);
            }
        }
        return android.graphics.Typeface_Delegate.sFinalizer;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nativeGetStyle(long native_instance) {
        android.graphics.Typeface_Delegate delegate = android.graphics.Typeface_Delegate.sManager.getDelegate(native_instance);
        if (delegate == null) {
            return 0;
        }
        return delegate.mStyle;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeSetDefault(long native_instance) {
        android.graphics.Typeface_Delegate.sDefaultTypeface = native_instance;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nativeGetWeight(long native_instance) {
        android.graphics.Typeface_Delegate delegate = android.graphics.Typeface_Delegate.sManager.getDelegate(native_instance);
        if (delegate == null) {
            return 0;
        }
        return delegate.mWeight;
    }

    /**
     * Loads a single font or font family from disk
     */
    @android.annotation.Nullable
    public static android.graphics.Typeface createFromDisk(@android.annotation.NonNull
    com.android.layoutlib.bridge.android.BridgeContext context, @android.annotation.NonNull
    java.lang.String path, boolean isFramework) {
        // Check if this is an asset that we've already loaded dynamically
        android.graphics.Typeface typeface = android.graphics.Typeface.findFromCache(context.getAssets(), path);
        if (typeface != null) {
            return typeface;
        }
        java.lang.String lowerCaseValue = path.toLowerCase();
        if (lowerCaseValue.endsWith(SdkConstants.DOT_XML)) {
            // create a block parser for the file
            java.lang.Boolean psiParserSupport = context.getLayoutlibCallback().getFlag(RenderParamsFlags.FLAG_KEY_XML_FILE_PARSER_SUPPORT);
            org.xmlpull.v1.XmlPullParser parser;
            if ((psiParserSupport != null) && psiParserSupport) {
                parser = context.getLayoutlibCallback().createXmlParserForPsiFile(path);
            } else {
                parser = context.getLayoutlibCallback().createXmlParserForFile(path);
            }
            if (parser != null) {
                // TODO(namespaces): The aapt namespace should not matter for parsing font files?
                com.android.layoutlib.bridge.android.BridgeXmlBlockParser blockParser = new com.android.layoutlib.bridge.android.BridgeXmlBlockParser(parser, context, com.android.ide.common.rendering.api.ResourceNamespace.fromBoolean(isFramework));
                try {
                    android.content.res.FontResourcesParser.FamilyResourceEntry entry = android.content.res.FontResourcesParser.parse(blockParser, context.getResources());
                    typeface = android.graphics.Typeface.createFromResources(entry, context.getAssets(), path);
                } catch (org.xmlpull.v1.XmlPullParserException | java.io.IOException e) {
                    /* data */
                    com.android.layoutlib.bridge.Bridge.getLog().error(null, "Failed to parse file " + path, e, null);
                } finally {
                    blockParser.ensurePopped();
                }
            } else {
                /* data */
                com.android.layoutlib.bridge.Bridge.getLog().error(LayoutLog.TAG_BROKEN, java.lang.String.format("File %s does not exist (or is not a file)", path), null);
            }
        } else {
            typeface = new android.graphics.Typeface.Builder(context.getAssets(), path).build();
        }
        return typeface;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Typeface create(java.lang.String familyName, int style) {
        if ((familyName != null) && java.nio.file.Files.exists(java.nio.file.Paths.get(familyName))) {
            // Workaround for b/64137851
            // Support lib will call this method after failing to create the TypefaceCompat.
            return android.graphics.Typeface_Delegate.createFromDisk(com.android.layoutlib.bridge.impl.RenderAction.getCurrentContext(), familyName, false);
        }
        return android.graphics.Typeface.create_Original(familyName, style);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Typeface create(android.graphics.Typeface family, int style) {
        return android.graphics.Typeface.create_Original(family, style);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static android.graphics.Typeface create(android.graphics.Typeface family, int style, boolean isItalic) {
        return android.graphics.Typeface.create_Original(family, style, isItalic);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nativeRegisterGenericFamily(java.lang.String str, long nativePtr) {
        android.graphics.Typeface_Delegate delegate = android.graphics.Typeface_Delegate.sManager.getDelegate(nativePtr);
        if (delegate == null) {
            return;
        }
        android.graphics.Typeface_Delegate.sGenericNativeFamilies.put(str, delegate.mFontFamilies);
    }

    // ---- Private delegate/helper methods ----
    /**
     * Return an Iterable of fonts that match the style and variant. The list is ordered
     * according to preference of fonts.
     * <p>
     * The Iterator may contain null when the font failed to load. If null is reached when trying to
     * render with this list of fonts, then a warning should be logged letting the user know that
     * some font failed to load.
     *
     * @param variant
     * 		The variant preferred. Can only be {@link FontVariant#COMPACT} or {@link FontVariant#ELEGANT}
     */
    @android.annotation.NonNull
    public java.lang.Iterable<java.awt.Font> getFonts(final android.graphics.FontFamily_Delegate.FontVariant variant) {
        assert variant != android.graphics.FontFamily_Delegate.FontVariant.NONE;
        return new android.graphics.Typeface_Delegate.FontsIterator(mFontFamilies, mFontFamilyBuilders, variant, mWeight, mStyle);
    }

    private static class FontsIterator implements java.lang.Iterable<java.awt.Font> , java.util.Iterator<java.awt.Font> {
        private final android.graphics.FontFamily_Delegate[] fontFamilies;

        private final android.graphics.fonts.FontFamily_Builder_Delegate[] fontFamilyBuilders;

        private final int weight;

        private final boolean isItalic;

        private final android.graphics.FontFamily_Delegate.FontVariant variant;

        private int index = 0;

        private FontsIterator(@android.annotation.NonNull
        android.graphics.FontFamily_Delegate[] fontFamilies, @android.annotation.NonNull
        android.graphics.fonts.FontFamily_Builder_Delegate[] fontFamilyBuilders, @android.annotation.NonNull
        android.graphics.FontFamily_Delegate.FontVariant variant, int weight, int style) {
            // Calculate the required weight based on style and weight of this typeface.
            int boldExtraWeight = ((style & java.awt.Font.BOLD) == 0) ? 0 : android.graphics.FontFamily_Delegate.BOLD_FONT_WEIGHT_DELTA;
            this.weight = java.lang.Math.min(java.lang.Math.max(100, (weight + 50) + boldExtraWeight), 1000);
            this.isItalic = (style & java.awt.Font.ITALIC) != 0;
            this.fontFamilies = fontFamilies;
            this.fontFamilyBuilders = fontFamilyBuilders;
            this.variant = variant;
        }

        @java.lang.Override
        public boolean hasNext() {
            return index < (fontFamilies.length + fontFamilyBuilders.length);
        }

        @java.lang.Override
        @android.annotation.Nullable
        public java.awt.Font next() {
            java.awt.Font font;
            android.graphics.FontFamily_Delegate.FontVariant ffdVariant;
            if (index < fontFamilies.length) {
                android.graphics.FontFamily_Delegate ffd = fontFamilies[index++];
                if ((ffd == null) || (!ffd.isValid())) {
                    return null;
                }
                font = ffd.getFont(weight, isItalic);
                ffdVariant = ffd.getVariant();
            } else {
                android.graphics.fonts.FontFamily_Builder_Delegate ffd = fontFamilyBuilders[(index++) - fontFamilies.length];
                if (ffd == null) {
                    return null;
                }
                font = ffd.getFont(weight, isItalic);
                ffdVariant = ffd.getVariant();
            }
            if (font == null) {
                // The FontFamily is valid but doesn't contain any matching font. This means
                // that the font failed to load. We add null to the list of fonts. Don't throw
                // the warning just yet. If this is a non-english font, we don't want to warn
                // users who are trying to render only english text.
                return null;
            }
            if ((ffdVariant == android.graphics.FontFamily_Delegate.FontVariant.NONE) || (ffdVariant == variant)) {
                return font;
            }
            // We cannot open each font and get locales supported, etc to match the fonts.
            // As a workaround, we hardcode certain assumptions like Elegant and Compact
            // always appear in pairs.
            if (index < fontFamilies.length) {
                assert index < (fontFamilies.length - 1);
                android.graphics.FontFamily_Delegate ffd2 = fontFamilies[index++];
                assert ffd2 != null;
                return ffd2.getFont(weight, isItalic);
            } else {
                assert index < ((fontFamilies.length + fontFamilyBuilders.length) - 1);
                android.graphics.fonts.FontFamily_Builder_Delegate ffd2 = fontFamilyBuilders[(index++) - fontFamilies.length];
                assert ffd2 != null;
                return ffd2.getFont(weight, isItalic);
            }
        }

        @android.annotation.NonNull
        @java.lang.Override
        public java.util.Iterator<java.awt.Font> iterator() {
            return this;
        }

        @java.lang.Override
        public java.util.Spliterator<java.awt.Font> spliterator() {
            return java.util.Spliterators.spliterator(iterator(), fontFamilies.length + fontFamilyBuilders.length, java.util.Spliterator.IMMUTABLE | java.util.Spliterator.SIZED);
        }
    }
}

