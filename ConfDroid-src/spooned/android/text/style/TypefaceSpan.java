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
 * Changes the typeface family of the text to which the span is attached.
 */
public class TypefaceSpan extends android.text.style.MetricAffectingSpan implements android.text.ParcelableSpan {
    private final java.lang.String mFamily;

    /**
     *
     *
     * @param family
     * 		The font family for this typeface.  Examples include
     * 		"monospace", "serif", and "sans-serif".
     */
    public TypefaceSpan(java.lang.String family) {
        mFamily = family;
    }

    public TypefaceSpan(android.os.Parcel src) {
        mFamily = src.readString();
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
        return android.text.TextUtils.TYPEFACE_SPAN;
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
        dest.writeString(mFamily);
    }

    /**
     * Returns the font family name.
     */
    public java.lang.String getFamily() {
        return mFamily;
    }

    @java.lang.Override
    public void updateDrawState(android.text.TextPaint ds) {
        android.text.style.TypefaceSpan.apply(ds, mFamily);
    }

    @java.lang.Override
    public void updateMeasureState(android.text.TextPaint paint) {
        android.text.style.TypefaceSpan.apply(paint, mFamily);
    }

    private static void apply(android.graphics.Paint paint, java.lang.String family) {
        int oldStyle;
        android.graphics.Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }
        android.graphics.Typeface tf = android.graphics.Typeface.create(family, oldStyle);
        int fake = oldStyle & (~tf.getStyle());
        if ((fake & android.graphics.Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }
        if ((fake & android.graphics.Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25F);
        }
        paint.setTypeface(tf);
    }
}

