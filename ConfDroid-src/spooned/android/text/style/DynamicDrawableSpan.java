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
package android.text.style;


/**
 *
 */
public abstract class DynamicDrawableSpan extends android.text.style.ReplacementSpan {
    private static final java.lang.String TAG = "DynamicDrawableSpan";

    /**
     * A constant indicating that the bottom of this span should be aligned
     * with the bottom of the surrounding text, i.e., at the same level as the
     * lowest descender in the text.
     */
    public static final int ALIGN_BOTTOM = 0;

    /**
     * A constant indicating that the bottom of this span should be aligned
     * with the baseline of the surrounding text.
     */
    public static final int ALIGN_BASELINE = 1;

    protected final int mVerticalAlignment;

    public DynamicDrawableSpan() {
        mVerticalAlignment = android.text.style.DynamicDrawableSpan.ALIGN_BOTTOM;
    }

    /**
     *
     *
     * @param verticalAlignment
     * 		one of {@link #ALIGN_BOTTOM} or {@link #ALIGN_BASELINE}.
     */
    protected DynamicDrawableSpan(int verticalAlignment) {
        mVerticalAlignment = verticalAlignment;
    }

    /**
     * Returns the vertical alignment of this span, one of {@link #ALIGN_BOTTOM} or
     * {@link #ALIGN_BASELINE}.
     */
    public int getVerticalAlignment() {
        return mVerticalAlignment;
    }

    /**
     * Your subclass must implement this method to provide the bitmap
     * to be drawn.  The dimensions of the bitmap must be the same
     * from each call to the next.
     */
    public abstract android.graphics.drawable.Drawable getDrawable();

    @java.lang.Override
    public int getSize(android.graphics.Paint paint, java.lang.CharSequence text, int start, int end, android.graphics.Paint.FontMetricsInt fm) {
        android.graphics.drawable.Drawable d = getCachedDrawable();
        android.graphics.Rect rect = d.getBounds();
        if (fm != null) {
            fm.ascent = -rect.bottom;
            fm.descent = 0;
            fm.top = fm.ascent;
            fm.bottom = 0;
        }
        return rect.right;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas, java.lang.CharSequence text, int start, int end, float x, int top, int y, int bottom, android.graphics.Paint paint) {
        android.graphics.drawable.Drawable b = getCachedDrawable();
        canvas.save();
        int transY = bottom - b.getBounds().bottom;
        if (mVerticalAlignment == android.text.style.DynamicDrawableSpan.ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        }
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }

    private android.graphics.drawable.Drawable getCachedDrawable() {
        java.lang.ref.WeakReference<android.graphics.drawable.Drawable> wr = mDrawableRef;
        android.graphics.drawable.Drawable d = null;
        if (wr != null)
            d = wr.get();

        if (d == null) {
            d = getDrawable();
            mDrawableRef = new java.lang.ref.WeakReference<android.graphics.drawable.Drawable>(d);
        }
        return d;
    }

    private java.lang.ref.WeakReference<android.graphics.drawable.Drawable> mDrawableRef;
}

