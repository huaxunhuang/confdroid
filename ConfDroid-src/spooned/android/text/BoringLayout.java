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
package android.text;


/**
 * A BoringLayout is a very simple Layout implementation for text that
 * fits on a single line and is all left-to-right characters.
 * You will probably never want to make one of these yourself;
 * if you do, be sure to call {@link #isBoring} first to make sure
 * the text meets the criteria.
 * <p>This class is used by widgets to control text layout. You should not need
 * to use this class directly unless you are implementing your own widget
 * or custom display object, in which case
 * you are encouraged to use a Layout instead of calling
 * {@link android.graphics.Canvas#drawText(java.lang.CharSequence, int, int, float, float, android.graphics.Paint)
 *  Canvas.drawText()} directly.</p>
 */
public class BoringLayout extends android.text.Layout implements android.text.TextUtils.EllipsizeCallback {
    public static android.text.BoringLayout make(java.lang.CharSequence source, android.text.TextPaint paint, int outerwidth, android.text.Layout.Alignment align, float spacingmult, float spacingadd, android.text.BoringLayout.Metrics metrics, boolean includepad) {
        return new android.text.BoringLayout(source, paint, outerwidth, align, spacingmult, spacingadd, metrics, includepad);
    }

    public static android.text.BoringLayout make(java.lang.CharSequence source, android.text.TextPaint paint, int outerwidth, android.text.Layout.Alignment align, float spacingmult, float spacingadd, android.text.BoringLayout.Metrics metrics, boolean includepad, android.text.TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        return new android.text.BoringLayout(source, paint, outerwidth, align, spacingmult, spacingadd, metrics, includepad, ellipsize, ellipsizedWidth);
    }

    /**
     * Returns a BoringLayout for the specified text, potentially reusing
     * this one if it is already suitable.  The caller must make sure that
     * no one is still using this Layout.
     */
    public android.text.BoringLayout replaceOrMake(java.lang.CharSequence source, android.text.TextPaint paint, int outerwidth, android.text.Layout.Alignment align, float spacingmult, float spacingadd, android.text.BoringLayout.Metrics metrics, boolean includepad) {
        replaceWith(source, paint, outerwidth, align, spacingmult, spacingadd);
        mEllipsizedWidth = outerwidth;
        mEllipsizedStart = 0;
        mEllipsizedCount = 0;
        init(source, paint, outerwidth, align, spacingmult, spacingadd, metrics, includepad, true);
        return this;
    }

    /**
     * Returns a BoringLayout for the specified text, potentially reusing
     * this one if it is already suitable.  The caller must make sure that
     * no one is still using this Layout.
     */
    public android.text.BoringLayout replaceOrMake(java.lang.CharSequence source, android.text.TextPaint paint, int outerwidth, android.text.Layout.Alignment align, float spacingmult, float spacingadd, android.text.BoringLayout.Metrics metrics, boolean includepad, android.text.TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        boolean trust;
        if ((ellipsize == null) || (ellipsize == android.text.TextUtils.TruncateAt.MARQUEE)) {
            replaceWith(source, paint, outerwidth, align, spacingmult, spacingadd);
            mEllipsizedWidth = outerwidth;
            mEllipsizedStart = 0;
            mEllipsizedCount = 0;
            trust = true;
        } else {
            replaceWith(android.text.TextUtils.ellipsize(source, paint, ellipsizedWidth, ellipsize, true, this), paint, outerwidth, align, spacingmult, spacingadd);
            mEllipsizedWidth = ellipsizedWidth;
            trust = false;
        }
        init(getText(), paint, outerwidth, align, spacingmult, spacingadd, metrics, includepad, trust);
        return this;
    }

    public BoringLayout(java.lang.CharSequence source, android.text.TextPaint paint, int outerwidth, android.text.Layout.Alignment align, float spacingmult, float spacingadd, android.text.BoringLayout.Metrics metrics, boolean includepad) {
        super(source, paint, outerwidth, align, spacingmult, spacingadd);
        mEllipsizedWidth = outerwidth;
        mEllipsizedStart = 0;
        mEllipsizedCount = 0;
        init(source, paint, outerwidth, align, spacingmult, spacingadd, metrics, includepad, true);
    }

    public BoringLayout(java.lang.CharSequence source, android.text.TextPaint paint, int outerwidth, android.text.Layout.Alignment align, float spacingmult, float spacingadd, android.text.BoringLayout.Metrics metrics, boolean includepad, android.text.TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        /* It is silly to have to call super() and then replaceWith(),
        but we can't use "this" for the callback until the call to
        super() finishes.
         */
        super(source, paint, outerwidth, align, spacingmult, spacingadd);
        boolean trust;
        if ((ellipsize == null) || (ellipsize == android.text.TextUtils.TruncateAt.MARQUEE)) {
            mEllipsizedWidth = outerwidth;
            mEllipsizedStart = 0;
            mEllipsizedCount = 0;
            trust = true;
        } else {
            replaceWith(android.text.TextUtils.ellipsize(source, paint, ellipsizedWidth, ellipsize, true, this), paint, outerwidth, align, spacingmult, spacingadd);
            mEllipsizedWidth = ellipsizedWidth;
            trust = false;
        }
        init(getText(), paint, outerwidth, align, spacingmult, spacingadd, metrics, includepad, trust);
    }

    /* package */
    void init(java.lang.CharSequence source, android.text.TextPaint paint, int outerwidth, android.text.Layout.Alignment align, float spacingmult, float spacingadd, android.text.BoringLayout.Metrics metrics, boolean includepad, boolean trustWidth) {
        int spacing;
        if ((source instanceof java.lang.String) && (align == android.text.Layout.Alignment.ALIGN_NORMAL)) {
            mDirect = source.toString();
        } else {
            mDirect = null;
        }
        mPaint = paint;
        if (includepad) {
            spacing = metrics.bottom - metrics.top;
            mDesc = metrics.bottom;
        } else {
            spacing = metrics.descent - metrics.ascent;
            mDesc = metrics.descent;
        }
        mBottom = spacing;
        if (trustWidth) {
            mMax = metrics.width;
        } else {
            /* If we have ellipsized, we have to actually calculate the
            width because the width that was passed in was for the
            full text, not the ellipsized form.
             */
            android.text.TextLine line = android.text.TextLine.obtain();
            line.set(paint, source, 0, source.length(), android.text.Layout.DIR_LEFT_TO_RIGHT, android.text.Layout.DIRS_ALL_LEFT_TO_RIGHT, false, null);
            mMax = ((int) (java.lang.Math.ceil(line.metrics(null))));
            android.text.TextLine.recycle(line);
        }
        if (includepad) {
            mTopPadding = metrics.top - metrics.ascent;
            mBottomPadding = metrics.bottom - metrics.descent;
        }
    }

    /**
     * Returns null if not boring; the width, ascent, and descent if boring.
     */
    public static android.text.BoringLayout.Metrics isBoring(java.lang.CharSequence text, android.text.TextPaint paint) {
        return android.text.BoringLayout.isBoring(text, paint, android.text.TextDirectionHeuristics.FIRSTSTRONG_LTR, null);
    }

    /**
     * Returns null if not boring; the width, ascent, and descent if boring.
     *
     * @unknown 
     */
    public static android.text.BoringLayout.Metrics isBoring(java.lang.CharSequence text, android.text.TextPaint paint, android.text.TextDirectionHeuristic textDir) {
        return android.text.BoringLayout.isBoring(text, paint, textDir, null);
    }

    /**
     * Returns null if not boring; the width, ascent, and descent in the
     * provided Metrics object (or a new one if the provided one was null)
     * if boring.
     */
    public static android.text.BoringLayout.Metrics isBoring(java.lang.CharSequence text, android.text.TextPaint paint, android.text.BoringLayout.Metrics metrics) {
        return android.text.BoringLayout.isBoring(text, paint, android.text.TextDirectionHeuristics.FIRSTSTRONG_LTR, metrics);
    }

    /**
     * Returns null if not boring; the width, ascent, and descent in the
     * provided Metrics object (or a new one if the provided one was null)
     * if boring.
     *
     * @unknown 
     */
    public static android.text.BoringLayout.Metrics isBoring(java.lang.CharSequence text, android.text.TextPaint paint, android.text.TextDirectionHeuristic textDir, android.text.BoringLayout.Metrics metrics) {
        final int MAX_BUF_LEN = 500;
        final char[] buffer = android.text.TextUtils.obtain(MAX_BUF_LEN);
        final int textLength = text.length();
        boolean boring = true;
        outer : for (int start = 0; start < textLength; start += MAX_BUF_LEN) {
            final int end = java.lang.Math.min(start + MAX_BUF_LEN, textLength);
            // No need to worry about getting half codepoints, since we reject surrogate code units
            // as non-boring as soon we see one.
            android.text.TextUtils.getChars(text, start, end, buffer, 0);
            final int len = end - start;
            for (int i = 0; i < len; i++) {
                final char c = buffer[i];
                // Arabic presentation forms
                if (((((((((c == '\n') || (c == '\t')) || ((c >= 0x590) && (c <= 0x8ff)))// RTL scripts
                 || (c == 0x200f))// Bidi format character
                 || ((c >= 0x202a) && (c <= 0x202e)))// Bidi format characters
                 || ((c >= 0x2066) && (c <= 0x2069)))// Bidi format characters
                 || ((c >= 0xd800) && (c <= 0xdfff)))// surrogate pairs
                 || ((c >= 0xfb1d) && (c <= 0xfdff)))// Hebrew and Arabic presentation forms
                 || ((c >= 0xfe70) && (c <= 0xfefe))) {
                    boring = false;
                    break outer;
                }
            }
            // TODO: This looks a little suspicious, and in some cases can result in O(n^2)
            // run time. Consider moving outside the loop.
            if ((textDir != null) && textDir.isRtl(buffer, 0, len)) {
                boring = false;
                break outer;
            }
        }
        android.text.TextUtils.recycle(buffer);
        if (boring && (text instanceof android.text.Spanned)) {
            android.text.Spanned sp = ((android.text.Spanned) (text));
            java.lang.Object[] styles = sp.getSpans(0, textLength, android.text.style.ParagraphStyle.class);
            if (styles.length > 0) {
                boring = false;
            }
        }
        if (boring) {
            android.text.BoringLayout.Metrics fm = metrics;
            if (fm == null) {
                fm = new android.text.BoringLayout.Metrics();
            } else {
                fm.reset();
            }
            android.text.TextLine line = android.text.TextLine.obtain();
            line.set(paint, text, 0, textLength, android.text.Layout.DIR_LEFT_TO_RIGHT, android.text.Layout.DIRS_ALL_LEFT_TO_RIGHT, false, null);
            fm.width = ((int) (java.lang.Math.ceil(line.metrics(fm))));
            android.text.TextLine.recycle(line);
            return fm;
        } else {
            return null;
        }
    }

    @java.lang.Override
    public int getHeight() {
        return mBottom;
    }

    @java.lang.Override
    public int getLineCount() {
        return 1;
    }

    @java.lang.Override
    public int getLineTop(int line) {
        if (line == 0)
            return 0;
        else
            return mBottom;

    }

    @java.lang.Override
    public int getLineDescent(int line) {
        return mDesc;
    }

    @java.lang.Override
    public int getLineStart(int line) {
        if (line == 0)
            return 0;
        else
            return getText().length();

    }

    @java.lang.Override
    public int getParagraphDirection(int line) {
        return android.text.Layout.DIR_LEFT_TO_RIGHT;
    }

    @java.lang.Override
    public boolean getLineContainsTab(int line) {
        return false;
    }

    @java.lang.Override
    public float getLineMax(int line) {
        return mMax;
    }

    @java.lang.Override
    public float getLineWidth(int line) {
        return line == 0 ? mMax : 0;
    }

    @java.lang.Override
    public final android.text.Layout.Directions getLineDirections(int line) {
        return android.text.Layout.DIRS_ALL_LEFT_TO_RIGHT;
    }

    @java.lang.Override
    public int getTopPadding() {
        return mTopPadding;
    }

    @java.lang.Override
    public int getBottomPadding() {
        return mBottomPadding;
    }

    @java.lang.Override
    public int getEllipsisCount(int line) {
        return mEllipsizedCount;
    }

    @java.lang.Override
    public int getEllipsisStart(int line) {
        return mEllipsizedStart;
    }

    @java.lang.Override
    public int getEllipsizedWidth() {
        return mEllipsizedWidth;
    }

    // Override draw so it will be faster.
    @java.lang.Override
    public void draw(android.graphics.Canvas c, android.graphics.Path highlight, android.graphics.Paint highlightpaint, int cursorOffset) {
        if ((mDirect != null) && (highlight == null)) {
            c.drawText(mDirect, 0, mBottom - mDesc, mPaint);
        } else {
            super.draw(c, highlight, highlightpaint, cursorOffset);
        }
    }

    /**
     * Callback for the ellipsizer to report what region it ellipsized.
     */
    public void ellipsized(int start, int end) {
        mEllipsizedStart = start;
        mEllipsizedCount = end - start;
    }

    private java.lang.String mDirect;

    private android.graphics.Paint mPaint;

    int mBottom;

    /* package */
    int mDesc;// for Direct


    private int mTopPadding;

    private int mBottomPadding;

    private float mMax;

    private int mEllipsizedWidth;

    private int mEllipsizedStart;

    private int mEllipsizedCount;

    public static class Metrics extends android.graphics.Paint.FontMetricsInt {
        public int width;

        @java.lang.Override
        public java.lang.String toString() {
            return (super.toString() + " width=") + width;
        }

        private void reset() {
            top = 0;
            bottom = 0;
            ascent = 0;
            descent = 0;
            width = 0;
            leading = 0;
        }
    }
}

