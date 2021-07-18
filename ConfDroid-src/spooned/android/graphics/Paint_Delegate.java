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
 * Delegate implementing the native methods of android.graphics.Paint
 *
 * Through the layoutlib_create tool, the original native methods of Paint have been replaced
 * by calls to methods of the same name in this delegate class.
 *
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between
 * it and the original Paint class.
 *
 * @see DelegateManager
 */
public class Paint_Delegate {
    private static final float DEFAULT_TEXT_SIZE = 20.0F;

    private static final float DEFAULT_TEXT_SCALE_X = 1.0F;

    private static final float DEFAULT_TEXT_SKEW_X = 0.0F;

    /**
     * Class associating a {@link Font} and its {@link java.awt.FontMetrics}.
     */
    /* package */
    static final class FontInfo {
        final java.awt.Font mFont;

        final java.awt.FontMetrics mMetrics;

        FontInfo(@android.annotation.NonNull
        java.awt.Font font, @android.annotation.NonNull
        java.awt.FontMetrics fontMetrics) {
            this.mFont = font;
            this.mMetrics = fontMetrics;
        }
    }

    // ---- delegate manager ----
    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.Paint_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager(android.graphics.Paint_Delegate.class);

    private static long sFinalizer = -1;

    // ---- delegate helper data ----
    // This list can contain null elements.
    @android.annotation.Nullable
    private java.util.List<android.graphics.Paint_Delegate.FontInfo> mFonts;

    // ---- delegate data ----
    private int mFlags;

    private int mColor;

    private int mStyle;

    private int mCap;

    private int mJoin;

    private int mTextAlign;

    private android.graphics.Typeface_Delegate mTypeface;

    private float mStrokeWidth;

    private float mStrokeMiter;

    private float mTextSize;

    private float mTextScaleX;

    private float mTextSkewX;

    private int mHintingMode = android.graphics.Paint.HINTING_ON;

    private int mStartHyphenEdit;

    private int mEndHyphenEdit;

    private float mLetterSpacing;// not used in actual text rendering.


    private float mWordSpacing;// not used in actual text rendering.


    // Variant of the font. A paint's variant can only be compact or elegant.
    private android.graphics.FontFamily_Delegate.FontVariant mFontVariant = android.graphics.FontFamily_Delegate.FontVariant.COMPACT;

    private int mPorterDuffMode = android.graphics.Xfermode.DEFAULT;

    private android.graphics.ColorFilter_Delegate mColorFilter;

    private android.graphics.Shader_Delegate mShader;

    private android.graphics.PathEffect_Delegate mPathEffect;

    private android.graphics.MaskFilter_Delegate mMaskFilter;

    // Used to store the locale for future use
    @java.lang.SuppressWarnings("FieldCanBeLocal")
    private java.util.Locale mLocale = java.util.Locale.getDefault();

    // ---- Public Helper methods ----
    @android.annotation.Nullable
    public static android.graphics.Paint_Delegate getDelegate(long native_paint) {
        return android.graphics.Paint_Delegate.sManager.getDelegate(native_paint);
    }

    /**
     * Returns the list of {@link Font} objects.
     */
    @android.annotation.NonNull
    public java.util.List<android.graphics.Paint_Delegate.FontInfo> getFonts() {
        android.graphics.Typeface_Delegate typeface = mTypeface;
        if (typeface == null) {
            if (android.graphics.Typeface.sDefaultTypeface == null) {
                return java.util.Collections.emptyList();
            }
            typeface = android.graphics.Typeface_Delegate.getDelegate(android.graphics.Typeface.sDefaultTypeface.native_instance);
        }
        if (mFonts != null) {
            return mFonts;
        }
        // Apply an optional transformation for skew and scale
        java.awt.geom.AffineTransform affineTransform = ((mTextScaleX != 1.0) || (mTextSkewX != 0)) ? new java.awt.geom.AffineTransform(mTextScaleX, mTextSkewX, 0, 1, 0, 0) : null;
        java.util.List<android.graphics.Paint_Delegate.FontInfo> infoList = java.util.stream.StreamSupport.stream(typeface.getFonts(mFontVariant).spliterator(), false).filter(java.util.Objects::nonNull).map(( font) -> android.graphics.Paint_Delegate.getFontInfo(font, mTextSize, affineTransform)).collect(java.util.stream.Collectors.toList());
        mFonts = java.util.Collections.unmodifiableList(infoList);
        return mFonts;
    }

    public boolean isAntiAliased() {
        return (mFlags & android.graphics.Paint.ANTI_ALIAS_FLAG) != 0;
    }

    public boolean isFilterBitmap() {
        return (mFlags & android.graphics.Paint.FILTER_BITMAP_FLAG) != 0;
    }

    public int getStyle() {
        return mStyle;
    }

    public int getColor() {
        return mColor;
    }

    public int getAlpha() {
        return mColor >>> 24;
    }

    public void setAlpha(int alpha) {
        mColor = (alpha << 24) | (mColor & 0xffffff);
    }

    public int getTextAlign() {
        return mTextAlign;
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    /**
     * returns the value of stroke miter needed by the java api.
     */
    public float getJavaStrokeMiter() {
        return mStrokeMiter;
    }

    public int getJavaCap() {
        switch (android.graphics.Paint.sCapArray[mCap]) {
            case BUTT :
                return java.awt.BasicStroke.CAP_BUTT;
            case ROUND :
                return java.awt.BasicStroke.CAP_ROUND;
            default :
            case SQUARE :
                return java.awt.BasicStroke.CAP_SQUARE;
        }
    }

    public int getJavaJoin() {
        switch (android.graphics.Paint.sJoinArray[mJoin]) {
            default :
            case MITER :
                return java.awt.BasicStroke.JOIN_MITER;
            case ROUND :
                return java.awt.BasicStroke.JOIN_ROUND;
            case BEVEL :
                return java.awt.BasicStroke.JOIN_BEVEL;
        }
    }

    public java.awt.Stroke getJavaStroke() {
        if (mPathEffect != null) {
            if (mPathEffect.isSupported()) {
                java.awt.Stroke stroke = mPathEffect.getStroke(this);
                assert stroke != null;
                // noinspection ConstantConditions
                if (stroke != null) {
                    return stroke;
                }
            } else {
                /* data */
                com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_PATHEFFECT, mPathEffect.getSupportMessage(), null, null);
            }
        }
        // if no custom stroke as been set, set the default one.
        return new java.awt.BasicStroke(getStrokeWidth(), getJavaCap(), getJavaJoin(), getJavaStrokeMiter());
    }

    /**
     * Returns the {@link PorterDuff.Mode} as an int
     */
    public int getPorterDuffMode() {
        return mPorterDuffMode;
    }

    /**
     * Returns the {@link ColorFilter} delegate or null if none have been set
     *
     * @return the delegate or null.
     */
    public android.graphics.ColorFilter_Delegate getColorFilter() {
        return mColorFilter;
    }

    public void setColorFilter(long colorFilterPtr) {
        mColorFilter = android.graphics.ColorFilter_Delegate.getDelegate(colorFilterPtr);
    }

    public void setShader(long shaderPtr) {
        mShader = android.graphics.Shader_Delegate.getDelegate(shaderPtr);
    }

    /**
     * Returns the {@link Shader} delegate or null if none have been set
     *
     * @return the delegate or null.
     */
    public android.graphics.Shader_Delegate getShader() {
        return mShader;
    }

    /**
     * Returns the {@link MaskFilter} delegate or null if none have been set
     *
     * @return the delegate or null.
     */
    public android.graphics.MaskFilter_Delegate getMaskFilter() {
        return mMaskFilter;
    }

    // ---- native methods ----
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetFlags(long nativePaint) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return 0;
        }
        return delegate.mFlags;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetFlags(long nativePaint, int flags) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return;
        }
        delegate.mFlags = flags;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetFilterBitmap(long nativePaint, boolean filter) {
        android.graphics.Paint_Delegate.setFlag(nativePaint, android.graphics.Paint.FILTER_BITMAP_FLAG, filter);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetHinting(long nativePaint) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return android.graphics.Paint.HINTING_ON;
        }
        return delegate.mHintingMode;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetHinting(long nativePaint, int mode) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return;
        }
        delegate.mHintingMode = mode;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetAntiAlias(long nativePaint, boolean aa) {
        android.graphics.Paint_Delegate.setFlag(nativePaint, android.graphics.Paint.ANTI_ALIAS_FLAG, aa);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetSubpixelText(long nativePaint, boolean subpixelText) {
        android.graphics.Paint_Delegate.setFlag(nativePaint, android.graphics.Paint.SUBPIXEL_TEXT_FLAG, subpixelText);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetUnderlineText(long nativePaint, boolean underlineText) {
        android.graphics.Paint_Delegate.setFlag(nativePaint, android.graphics.Paint.UNDERLINE_TEXT_FLAG, underlineText);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetStrikeThruText(long nativePaint, boolean strikeThruText) {
        android.graphics.Paint_Delegate.setFlag(nativePaint, android.graphics.Paint.STRIKE_THRU_TEXT_FLAG, strikeThruText);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetFakeBoldText(long nativePaint, boolean fakeBoldText) {
        android.graphics.Paint_Delegate.setFlag(nativePaint, android.graphics.Paint.FAKE_BOLD_TEXT_FLAG, fakeBoldText);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetDither(long nativePaint, boolean dither) {
        android.graphics.Paint_Delegate.setFlag(nativePaint, android.graphics.Paint.DITHER_FLAG, dither);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetLinearText(long nativePaint, boolean linearText) {
        android.graphics.Paint_Delegate.setFlag(nativePaint, android.graphics.Paint.LINEAR_TEXT_FLAG, linearText);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetColor(long paintPtr, long colorSpaceHandle, long color) {
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(paintPtr);
        if (delegate == null) {
            return;
        }
        delegate.mColor = android.graphics.Color.toArgb(color);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetColor(long paintPtr, int color) {
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(paintPtr);
        if (delegate == null) {
            return;
        }
        delegate.mColor = color;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetAlpha(long nativePaint, int a) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return;
        }
        delegate.setAlpha(a);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetStrokeWidth(long nativePaint) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return 1.0F;
        }
        return delegate.mStrokeWidth;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetStrokeWidth(long nativePaint, float width) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return;
        }
        delegate.mStrokeWidth = width;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetStrokeMiter(long nativePaint) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return 1.0F;
        }
        return delegate.mStrokeMiter;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetStrokeMiter(long nativePaint, float miter) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return;
        }
        delegate.mStrokeMiter = miter;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetShadowLayer(long paintPtr, float radius, float dx, float dy, long colorSpaceHandle, long shadowColor) {
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Paint.setShadowLayer is not supported.", null, null);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nHasShadowLayer(long paint) {
        // FIXME
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Paint.hasShadowLayer is not supported.", null, null);
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nIsElegantTextHeight(long nativePaint) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        return (delegate != null) && (delegate.mFontVariant == android.graphics.FontFamily_Delegate.FontVariant.ELEGANT);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetElegantTextHeight(long nativePaint, boolean elegant) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return;
        }
        delegate.mFontVariant = (elegant) ? android.graphics.FontFamily_Delegate.FontVariant.ELEGANT : android.graphics.FontFamily_Delegate.FontVariant.COMPACT;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetTextSize(long nativePaint) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return 1.0F;
        }
        return delegate.mTextSize;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetTextSize(long nativePaint, float textSize) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return;
        }
        if (delegate.mTextSize != textSize) {
            delegate.mTextSize = textSize;
            delegate.invalidateFonts();
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetTextScaleX(long nativePaint) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return 1.0F;
        }
        return delegate.mTextScaleX;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetTextScaleX(long nativePaint, float scaleX) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return;
        }
        if (delegate.mTextScaleX != scaleX) {
            delegate.mTextScaleX = scaleX;
            delegate.invalidateFonts();
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetTextSkewX(long nativePaint) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return 1.0F;
        }
        return delegate.mTextSkewX;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetTextSkewX(long nativePaint, float skewX) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return;
        }
        if (delegate.mTextSkewX != skewX) {
            delegate.mTextSkewX = skewX;
            delegate.invalidateFonts();
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nAscent(long nativePaint) {
        // get the delegate
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return 0;
        }
        java.util.List<android.graphics.Paint_Delegate.FontInfo> fonts = delegate.getFonts();
        if (fonts.size() > 0) {
            java.awt.FontMetrics javaMetrics = fonts.get(0).mMetrics;
            // Android expects negative ascent so we invert the value from Java.
            return -javaMetrics.getAscent();
        }
        return 0;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nDescent(long nativePaint) {
        // get the delegate
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return 0;
        }
        java.util.List<android.graphics.Paint_Delegate.FontInfo> fonts = delegate.getFonts();
        if (fonts.size() > 0) {
            java.awt.FontMetrics javaMetrics = fonts.get(0).mMetrics;
            return javaMetrics.getDescent();
        }
        return 0;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetFontMetrics(long nativePaint, android.graphics.Paint.FontMetrics metrics) {
        // get the delegate
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return 0;
        }
        return delegate.getFontMetrics(metrics);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetFontMetricsInt(long nativePaint, android.graphics.Paint.FontMetricsInt fmi) {
        // get the delegate
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return 0;
        }
        java.util.List<android.graphics.Paint_Delegate.FontInfo> fonts = delegate.getFonts();
        if (fonts.size() > 0) {
            java.awt.FontMetrics javaMetrics = fonts.get(0).mMetrics;
            if (fmi != null) {
                // Android expects negative ascent so we invert the value from Java.
                fmi.top = ((int) ((-javaMetrics.getMaxAscent()) * 1.15));
                fmi.ascent = -javaMetrics.getAscent();
                fmi.descent = javaMetrics.getDescent();
                fmi.bottom = ((int) (javaMetrics.getMaxDescent() * 1.15));
                fmi.leading = javaMetrics.getLeading();
            }
            return javaMetrics.getHeight();
        }
        return 0;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nBreakText(long nativePaint, char[] text, int index, int count, float maxWidth, int bidiFlags, float[] measuredWidth) {
        // get the delegate
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return 0;
        }
        int inc = (count > 0) ? 1 : -1;
        int measureIndex = 0;
        for (int i = index; i != (index + count); i += inc , measureIndex++) {
            int start;
            int end;
            if (i < index) {
                start = i;
                end = index;
            } else {
                start = index;
                end = i;
            }
            // measure from start to end
            android.graphics.RectF bounds = delegate.measureText(text, start, (end - start) + 1, null, 0, bidiFlags);
            float res = bounds.right - bounds.left;
            if (measuredWidth != null) {
                measuredWidth[measureIndex] = res;
            }
            if (res > maxWidth) {
                // we should not return this char index, but since it's 0-based
                // and we need to return a count, we simply return measureIndex;
                return measureIndex;
            }
        }
        return measureIndex;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nBreakText(long nativePaint, java.lang.String text, boolean measureForwards, float maxWidth, int bidiFlags, float[] measuredWidth) {
        return android.graphics.Paint_Delegate.nBreakText(nativePaint, text.toCharArray(), 0, text.length(), maxWidth, bidiFlags, measuredWidth);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nInit() {
        android.graphics.Paint_Delegate newDelegate = new android.graphics.Paint_Delegate();
        return android.graphics.Paint_Delegate.sManager.addNewDelegate(newDelegate);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nInitWithPaint(long paint) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(paint);
        if (delegate == null) {
            return 0;
        }
        android.graphics.Paint_Delegate newDelegate = new android.graphics.Paint_Delegate(delegate);
        return android.graphics.Paint_Delegate.sManager.addNewDelegate(newDelegate);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nReset(long native_object) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return;
        }
        delegate.reset();
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSet(long native_dst, long native_src) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate_dst = android.graphics.Paint_Delegate.sManager.getDelegate(native_dst);
        if (delegate_dst == null) {
            return;
        }
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate_src = android.graphics.Paint_Delegate.sManager.getDelegate(native_src);
        if (delegate_src == null) {
            return;
        }
        delegate_dst.set(delegate_src);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetStyle(long native_object) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return 0;
        }
        return delegate.mStyle;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetStyle(long native_object, int style) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return;
        }
        delegate.mStyle = style;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetStrokeCap(long native_object) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return 0;
        }
        return delegate.mCap;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetStrokeCap(long native_object, int cap) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return;
        }
        delegate.mCap = cap;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetStrokeJoin(long native_object) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return 0;
        }
        return delegate.mJoin;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetStrokeJoin(long native_object, int join) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return;
        }
        delegate.mJoin = join;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nGetFillPath(long native_object, long src, long dst) {
        android.graphics.Paint_Delegate paint = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (paint == null) {
            return false;
        }
        android.graphics.Path_Delegate srcPath = android.graphics.Path_Delegate.getDelegate(src);
        if (srcPath == null) {
            return true;
        }
        android.graphics.Path_Delegate dstPath = android.graphics.Path_Delegate.getDelegate(dst);
        if (dstPath == null) {
            return true;
        }
        java.awt.Stroke stroke = paint.getJavaStroke();
        java.awt.Shape strokeShape = stroke.createStrokedShape(srcPath.getJavaShape());
        dstPath.setJavaShape(strokeShape);
        // FIXME figure out the return value?
        return true;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nSetShader(long native_object, long shader) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return shader;
        }
        delegate.mShader = android.graphics.Shader_Delegate.getDelegate(shader);
        return shader;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nSetColorFilter(long native_object, long filter) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return filter;
        }
        delegate.mColorFilter = android.graphics.ColorFilter_Delegate.getDelegate(filter);
        // Log warning if it's not supported.
        if ((delegate.mColorFilter != null) && (!delegate.mColorFilter.isSupported())) {
            /* data */
            com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_COLORFILTER, delegate.mColorFilter.getSupportMessage(), null, null);
        }
        return filter;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetXfermode(long native_object, int xfermode) {
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return;
        }
        delegate.mPorterDuffMode = xfermode;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nSetPathEffect(long native_object, long effect) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return effect;
        }
        delegate.mPathEffect = android.graphics.PathEffect_Delegate.getDelegate(effect);
        return effect;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nSetMaskFilter(long native_object, long maskfilter) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return maskfilter;
        }
        delegate.mMaskFilter = android.graphics.MaskFilter_Delegate.getDelegate(maskfilter);
        // since none of those are supported, display a fidelity warning right away
        if ((delegate.mMaskFilter != null) && (!delegate.mMaskFilter.isSupported())) {
            /* data */
            com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_MASKFILTER, delegate.mMaskFilter.getSupportMessage(), null, null);
        }
        return maskfilter;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetTypeface(long native_object, long typeface) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return;
        }
        android.graphics.Typeface_Delegate typefaceDelegate = android.graphics.Typeface_Delegate.getDelegate(typeface);
        if (delegate.mTypeface != typefaceDelegate) {
            delegate.mTypeface = typefaceDelegate;
            delegate.invalidateFonts();
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetTextAlign(long native_object) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return 0;
        }
        return delegate.mTextAlign;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetTextAlign(long native_object, int align) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return;
        }
        delegate.mTextAlign = align;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nSetTextLocales(long native_object, java.lang.String locale) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return 0;
        }
        delegate.setTextLocale(locale);
        return 0;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetTextLocalesByMinikinLocaleListId(long paintPtr, int mMinikinLangListId) {
        // FIXME
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetTextAdvances(long native_object, char[] text, int index, int count, int contextIndex, int contextCount, int bidiFlags, float[] advances, int advancesIndex) {
        if (advances != null)
            for (int i = advancesIndex; i < (advancesIndex + count); i++)
                advances[i] = 0;


        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(native_object);
        if (delegate == null) {
            return 0.0F;
        }
        android.graphics.RectF bounds = delegate.measureText(text, index, count, advances, advancesIndex, bidiFlags);
        return bounds.right - bounds.left;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetTextAdvances(long native_object, java.lang.String text, int start, int end, int contextStart, int contextEnd, int bidiFlags, float[] advances, int advancesIndex) {
        // FIXME: support contextStart and contextEnd
        int count = end - start;
        char[] buffer = android.graphics.TemporaryBuffer.obtain(count);
        android.text.TextUtils.getChars(text, start, end, buffer, 0);
        return android.graphics.Paint_Delegate.nGetTextAdvances(native_object, buffer, 0, count, contextStart, contextEnd - contextStart, bidiFlags, advances, advancesIndex);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetTextRunCursor(android.graphics.Paint paint, long native_object, char[] text, int contextStart, int contextLength, int flags, int offset, int cursorOpt) {
        // FIXME
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Paint.getTextRunCursor is not supported.", null, null);
        return 0;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetTextRunCursor(android.graphics.Paint paint, long native_object, java.lang.String text, int contextStart, int contextEnd, int flags, int offset, int cursorOpt) {
        // FIXME
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Paint.getTextRunCursor is not supported.", null, null);
        return 0;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nGetTextPath(long native_object, int bidiFlags, char[] text, int index, int count, float x, float y, long path) {
        // FIXME
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Paint.getTextPath is not supported.", null, null);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nGetTextPath(long native_object, int bidiFlags, java.lang.String text, int start, int end, float x, float y, long path) {
        // FIXME
        /* data */
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_UNSUPPORTED, "Paint.getTextPath is not supported.", null, null);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nGetStringBounds(long nativePaint, java.lang.String text, int start, int end, int bidiFlags, android.graphics.Rect bounds) {
        android.graphics.Paint_Delegate.nGetCharArrayBounds(nativePaint, text.toCharArray(), start, end - start, bidiFlags, bounds);
    }

    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    public static void nGetCharArrayBounds(long nativePaint, char[] text, int index, int count, int bidiFlags, android.graphics.Rect bounds) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return;
        }
        delegate.measureText(text, index, count, null, 0, bidiFlags).roundOut(bounds);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nGetNativeFinalizer() {
        synchronized(android.graphics.Paint_Delegate.class) {
            if (android.graphics.Paint_Delegate.sFinalizer == (-1)) {
                android.graphics.Paint_Delegate.sFinalizer = libcore.util.NativeAllocationRegistry_Delegate.createFinalizer(android.graphics.Paint_Delegate.sManager::removeJavaReferenceFor);
            }
        }
        return android.graphics.Paint_Delegate.sFinalizer;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetLetterSpacing(long nativePaint) {
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return 0;
        }
        return delegate.mLetterSpacing;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetLetterSpacing(long nativePaint, float letterSpacing) {
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_TEXT_RENDERING, "Paint.setLetterSpacing() not supported.", null, null);
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return;
        }
        delegate.mLetterSpacing = letterSpacing;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetWordSpacing(long nativePaint) {
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return 0;
        }
        return delegate.mWordSpacing;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetWordSpacing(long nativePaint, float wordSpacing) {
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return;
        }
        delegate.mWordSpacing = wordSpacing;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetFontFeatureSettings(long nativePaint, java.lang.String settings) {
        com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_TEXT_RENDERING, "Paint.setFontFeatureSettings() not supported.", null, null);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetStartHyphenEdit(long nativePaint) {
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return 0;
        }
        return delegate.mStartHyphenEdit;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetStartHyphenEdit(long nativePaint, int hyphen) {
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return;
        }
        delegate.mStartHyphenEdit = hyphen;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetEndHyphenEdit(long nativePaint) {
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return 0;
        }
        return delegate.mEndHyphenEdit;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void nSetEndHyphenEdit(long nativePaint, int hyphen) {
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return;
        }
        delegate.mEndHyphenEdit = hyphen;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nHasGlyph(long nativePaint, int bidiFlags, java.lang.String string) {
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return false;
        }
        if (string.length() == 0) {
            return false;
        }
        if (string.length() > 1) {
            com.android.layoutlib.bridge.Bridge.getLog().fidelityWarning(LayoutLog.TAG_TEXT_RENDERING, "Paint.hasGlyph() is not supported for ligatures.", null, null);
            return false;
        }
        char c = string.charAt(0);
        for (java.awt.Font font : delegate.mTypeface.getFonts(delegate.mFontVariant)) {
            if (font.canDisplay(c)) {
                return true;
            }
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetRunAdvance(long nativePaint, @android.annotation.NonNull
    char[] text, int start, int end, int contextStart, int contextEnd, boolean isRtl, int offset) {
        int count = end - start;
        float[] advances = new float[count];
        int bidiFlags = (isRtl) ? android.graphics.Paint.BIDI_FORCE_RTL : android.graphics.Paint.BIDI_FORCE_LTR;
        android.graphics.Paint_Delegate.nGetTextAdvances(nativePaint, text, start, count, contextStart, contextEnd - contextStart, bidiFlags, advances, 0);
        int startOffset = offset - start;// offset from start.

        float sum = 0;
        for (int i = 0; i < startOffset; i++) {
            sum += advances[i];
        }
        return sum;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetOffsetForAdvance(long nativePaint, char[] text, int start, int end, int contextStart, int contextEnd, boolean isRtl, float advance) {
        int count = end - start;
        float[] advances = new float[count];
        int bidiFlags = (isRtl) ? android.graphics.Paint.BIDI_FORCE_RTL : android.graphics.Paint.BIDI_FORCE_LTR;
        android.graphics.Paint_Delegate.nGetTextAdvances(nativePaint, text, start, count, contextStart, contextEnd - contextStart, bidiFlags, advances, 0);
        float sum = 0;
        int i;
        for (i = 0; (i < count) && (sum < advance); i++) {
            sum += advances[i];
        }
        float distanceToI = sum - advance;
        float distanceToIMinus1 = advance - (sum - advances[i]);
        return distanceToI > distanceToIMinus1 ? i : i - 1;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetUnderlinePosition(long paintPtr) {
        return (1.0F / 9.0F) * android.graphics.Paint_Delegate.nGetTextSize(paintPtr);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetUnderlineThickness(long paintPtr) {
        return (1.0F / 18.0F) * android.graphics.Paint_Delegate.nGetTextSize(paintPtr);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetStrikeThruPosition(long paintPtr) {
        return ((-79.0F) / 252.0F) * android.graphics.Paint_Delegate.nGetTextSize(paintPtr);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetStrikeThruThickness(long paintPtr) {
        return (1.0F / 18.0F) * android.graphics.Paint_Delegate.nGetTextSize(paintPtr);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nEqualsForTextMeasurement(long leftPaintPtr, long rightPaintPtr) {
        return leftPaintPtr == rightPaintPtr;
    }

    // ---- Private delegate/helper methods ----
    /* package */
    Paint_Delegate() {
        reset();
    }

    private Paint_Delegate(android.graphics.Paint_Delegate paint) {
        set(paint);
    }

    private void set(android.graphics.Paint_Delegate paint) {
        mFlags = paint.mFlags;
        mColor = paint.mColor;
        mStyle = paint.mStyle;
        mCap = paint.mCap;
        mJoin = paint.mJoin;
        mTextAlign = paint.mTextAlign;
        if (mTypeface != paint.mTypeface) {
            mTypeface = paint.mTypeface;
            invalidateFonts();
        }
        if (mTextSize != paint.mTextSize) {
            mTextSize = paint.mTextSize;
            invalidateFonts();
        }
        if (mTextScaleX != paint.mTextScaleX) {
            mTextScaleX = paint.mTextScaleX;
            invalidateFonts();
        }
        if (mTextSkewX != paint.mTextSkewX) {
            mTextSkewX = paint.mTextSkewX;
            invalidateFonts();
        }
        mStrokeWidth = paint.mStrokeWidth;
        mStrokeMiter = paint.mStrokeMiter;
        mPorterDuffMode = paint.mPorterDuffMode;
        mColorFilter = paint.mColorFilter;
        mShader = paint.mShader;
        mPathEffect = paint.mPathEffect;
        mMaskFilter = paint.mMaskFilter;
        mHintingMode = paint.mHintingMode;
    }

    private void reset() {
        android.graphics.Typeface_Delegate defaultTypeface = android.graphics.Typeface_Delegate.getDelegate(android.graphics.Typeface.sDefaults[0].native_instance);
        mFlags = android.graphics.Paint.HIDDEN_DEFAULT_PAINT_FLAGS;
        mColor = 0xff000000;
        mStyle = android.graphics.Paint.Style.FILL.nativeInt;
        mCap = android.graphics.Paint.Cap.BUTT.nativeInt;
        mJoin = android.graphics.Paint.Join.MITER.nativeInt;
        mTextAlign = 0;
        if (mTypeface != defaultTypeface) {
            mTypeface = defaultTypeface;
            invalidateFonts();
        }
        mStrokeWidth = 1.0F;
        mStrokeMiter = 4.0F;
        if (mTextSize != android.graphics.Paint_Delegate.DEFAULT_TEXT_SIZE) {
            mTextSize = android.graphics.Paint_Delegate.DEFAULT_TEXT_SIZE;
            invalidateFonts();
        }
        if (mTextScaleX != android.graphics.Paint_Delegate.DEFAULT_TEXT_SCALE_X) {
            mTextScaleX = android.graphics.Paint_Delegate.DEFAULT_TEXT_SCALE_X;
            invalidateFonts();
        }
        if (mTextSkewX != android.graphics.Paint_Delegate.DEFAULT_TEXT_SKEW_X) {
            mTextSkewX = android.graphics.Paint_Delegate.DEFAULT_TEXT_SKEW_X;
            invalidateFonts();
        }
        mPorterDuffMode = android.graphics.Xfermode.DEFAULT;
        mColorFilter = null;
        mShader = null;
        mPathEffect = null;
        mMaskFilter = null;
        mHintingMode = android.graphics.Paint.HINTING_ON;
    }

    private void invalidateFonts() {
        mFonts = null;
    }

    @android.annotation.Nullable
    private static android.graphics.Paint_Delegate.FontInfo getFontInfo(@android.annotation.Nullable
    java.awt.Font font, float textSize, @android.annotation.Nullable
    java.awt.geom.AffineTransform transform) {
        if (font == null) {
            return null;
        }
        java.awt.Font transformedFont = font.deriveFont(textSize);
        if (transform != null) {
            // TODO: support skew
            transformedFont = transformedFont.deriveFont(transform);
        }
        // The metrics here don't have anti-aliasing set.
        return new android.graphics.Paint_Delegate.FontInfo(transformedFont, java.awt.Toolkit.getDefaultToolkit().getFontMetrics(transformedFont));
    }

    /* package */
    android.graphics.RectF measureText(char[] text, int index, int count, float[] advances, int advancesIndex, int bidiFlags) {
        return new android.graphics.BidiRenderer(null, this, text).renderText(index, index + count, bidiFlags, advances, advancesIndex, false);
    }

    /* package */
    android.graphics.RectF measureText(char[] text, int index, int count, float[] advances, int advancesIndex, boolean isRtl) {
        return new android.graphics.BidiRenderer(null, this, text).renderText(index, index + count, isRtl, advances, advancesIndex, false);
    }

    private float getFontMetrics(android.graphics.Paint.FontMetrics metrics) {
        java.util.List<android.graphics.Paint_Delegate.FontInfo> fonts = getFonts();
        if (fonts.size() > 0) {
            java.awt.FontMetrics javaMetrics = fonts.get(0).mMetrics;
            if (metrics != null) {
                // Android expects negative ascent so we invert the value from Java.
                metrics.top = -javaMetrics.getMaxAscent();
                metrics.ascent = -javaMetrics.getAscent();
                metrics.descent = javaMetrics.getDescent();
                metrics.bottom = javaMetrics.getMaxDescent();
                metrics.leading = javaMetrics.getLeading();
            }
            return javaMetrics.getHeight();
        }
        return 0;
    }

    private void setTextLocale(java.lang.String locale) {
        mLocale = new java.util.Locale(locale);
    }

    private static void setFlag(long nativePaint, int flagMask, boolean flagValue) {
        // get the delegate from the native int.
        android.graphics.Paint_Delegate delegate = android.graphics.Paint_Delegate.sManager.getDelegate(nativePaint);
        if (delegate == null) {
            return;
        }
        if (flagValue) {
            delegate.mFlags |= flagMask;
        } else {
            delegate.mFlags &= ~flagMask;
        }
    }
}

