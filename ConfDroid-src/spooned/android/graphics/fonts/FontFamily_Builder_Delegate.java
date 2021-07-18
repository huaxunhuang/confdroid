/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.graphics.fonts;


/**
 * Delegate implementing the native methods of android.graphics.fonts.FontFamily$Builder
 * <p>
 * Through the layoutlib_create tool, the original native methods of FontFamily$Builder have been
 * replaced by calls to methods of the same name in this delegate class.
 * <p>
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between it
 * and the original FontFamily$Builder class.
 *
 * @see DelegateManager
 */
public class FontFamily_Builder_Delegate {
    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.fonts.FontFamily_Builder_Delegate> sBuilderManager = new com.android.layoutlib.bridge.impl.DelegateManager(android.graphics.fonts.FontFamily_Builder_Delegate.class);

    private static long sFontFamilyFinalizer = -1;

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

    private boolean mIsCustomFallback;

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nInitBuilder() {
        return android.graphics.fonts.FontFamily_Builder_Delegate.sBuilderManager.addNewDelegate(new android.graphics.fonts.FontFamily_Builder_Delegate());
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nAddFont(long builderPtr, long fontPtr) {
        android.graphics.fonts.FontFamily_Builder_Delegate builder = android.graphics.fonts.FontFamily_Builder_Delegate.sBuilderManager.getDelegate(builderPtr);
        android.graphics.fonts.Font_Builder_Delegate font = android.graphics.fonts.Font_Builder_Delegate.sBuilderManager.getDelegate(fontPtr);
        if ((builder != null) && (font != null)) {
            builder.addFont(font.mBuffer, font.mTtcIndex, font.mWeight, font.mItalic);
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nBuild(long builderPtr, java.lang.String langTags, int variant, boolean isCustomFallback) {
        android.graphics.fonts.FontFamily_Builder_Delegate builder = android.graphics.fonts.FontFamily_Builder_Delegate.sBuilderManager.getDelegate(builderPtr);
        if (builder != null) {
            assert variant < 3;
            builder.mVariant = android.graphics.FontFamily_Delegate.FontVariant.values()[variant];
            builder.mIsCustomFallback = isCustomFallback;
        }
        return builderPtr;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nGetReleaseNativeFamily() {
        synchronized(android.graphics.fonts.Font_Builder_Delegate.class) {
            if (android.graphics.fonts.FontFamily_Builder_Delegate.sFontFamilyFinalizer == (-1)) {
                android.graphics.fonts.FontFamily_Builder_Delegate.sFontFamilyFinalizer = libcore.util.NativeAllocationRegistry_Delegate.createFinalizer(android.graphics.fonts.FontFamily_Builder_Delegate.sBuilderManager::removeJavaReferenceFor);
            }
        }
        return android.graphics.fonts.FontFamily_Builder_Delegate.sFontFamilyFinalizer;
    }

    public static android.graphics.fonts.FontFamily_Builder_Delegate getDelegate(long nativeFontFamily) {
        return android.graphics.fonts.FontFamily_Builder_Delegate.sBuilderManager.getDelegate(nativeFontFamily);
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

    // ---- private helper methods ----
    private void addFont(final java.nio.ByteBuffer buffer, int ttcIndex, int weight, boolean italic) {
        addFont(buffer, weight, italic);
    }

    private void addFont(@android.annotation.NonNull
    java.nio.ByteBuffer buffer, int weight, boolean italic) {
        // Set valid to true, even if the font fails to load.
        java.awt.Font font = android.graphics.fonts.FontFamily_Builder_Delegate.loadFont(buffer);
        if (font == null) {
            return;
        }
        android.graphics.FontFamily_Delegate.FontInfo fontInfo = new android.graphics.FontFamily_Delegate.FontInfo();
        fontInfo.mFont = font;
        fontInfo.mWeight = weight;
        fontInfo.mIsItalic = italic;
        addFont(fontInfo);
    }

    private void addFont(@android.annotation.NonNull
    android.graphics.FontFamily_Delegate.FontInfo fontInfo) {
        mFonts.putIfAbsent(fontInfo, fontInfo.mFont);
    }

    private static java.awt.Font loadFont(@android.annotation.NonNull
    java.nio.ByteBuffer buffer) {
        try {
            byte[] byteArray = new byte[buffer.limit()];
            buffer.get(byteArray);
            buffer.rewind();
            return java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new java.io.ByteArrayInputStream(byteArray));
        } catch (java.lang.Exception e) {
            com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_BROKEN, "Unable to load font", e, null);
        }
        return null;
    }
}

