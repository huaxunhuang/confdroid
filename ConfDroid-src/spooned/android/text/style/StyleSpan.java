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
 * Describes a style in a span.
 * Note that styles are cumulative -- if both bold and italic are set in
 * separate spans, or if the base style is bold and a span calls for italic,
 * you get bold italic.  You can't turn off a style from the base style.
 */
public class StyleSpan extends android.text.style.MetricAffectingSpan implements android.text.ParcelableSpan {
    private final int mStyle;

    /**
     *
     *
     * @param style
     * 		An integer constant describing the style for this span. Examples
     * 		include bold, italic, and normal. Values are constants defined
     * 		in {@link android.graphics.Typeface}.
     */
    public StyleSpan(int style) {
        mStyle = style;
    }

    public StyleSpan(android.os.Parcel src) {
        mStyle = src.readInt();
    }

    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    /**
     *
     *
     * @unknown 
     */
    public int getSpanTypeIdInternal() {
        return android.text.TextUtils.STYLE_SPAN;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    /**
     *
     *
     * @unknown 
     */
    public void writeToParcelInternal(android.os.Parcel dest, int flags) {
        dest.writeInt(mStyle);
    }

    /**
     * Returns the style constant defined in {@link android.graphics.Typeface}.
     */
    public int getStyle() {
        return mStyle;
    }

    @java.lang.Override
    public void updateDrawState(android.text.TextPaint ds) {
        android.text.style.StyleSpan.apply(ds, mStyle);
    }

    @java.lang.Override
    public void updateMeasureState(android.text.TextPaint paint) {
        android.text.style.StyleSpan.apply(paint, mStyle);
    }

    private static void apply(android.graphics.Paint paint, int style) {
        int oldStyle;
        android.graphics.Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }
        int want = oldStyle | style;
        android.graphics.Typeface tf;
        if (old == null) {
            tf = android.graphics.Typeface.defaultFromStyle(want);
        } else {
            tf = android.graphics.Typeface.create(old, want);
        }
        int fake = want & (~tf.getStyle());
        if ((fake & android.graphics.Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }
        if ((fake & android.graphics.Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25F);
        }
        paint.setTypeface(tf);
    }
}

