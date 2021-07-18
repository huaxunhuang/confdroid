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


public class QuoteSpan implements android.text.ParcelableSpan , android.text.style.LeadingMarginSpan {
    private static final int STRIPE_WIDTH = 2;

    private static final int GAP_WIDTH = 2;

    private final int mColor;

    public QuoteSpan() {
        super();
        mColor = 0xff0000ff;
    }

    public QuoteSpan(@android.annotation.ColorInt
    int color) {
        super();
        mColor = color;
    }

    public QuoteSpan(android.os.Parcel src) {
        mColor = src.readInt();
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
        return android.text.TextUtils.QUOTE_SPAN;
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
        dest.writeInt(mColor);
    }

    @android.annotation.ColorInt
    public int getColor() {
        return mColor;
    }

    public int getLeadingMargin(boolean first) {
        return android.text.style.QuoteSpan.STRIPE_WIDTH + android.text.style.QuoteSpan.GAP_WIDTH;
    }

    public void drawLeadingMargin(android.graphics.Canvas c, android.graphics.Paint p, int x, int dir, int top, int baseline, int bottom, java.lang.CharSequence text, int start, int end, boolean first, android.text.Layout layout) {
        android.graphics.Paint.Style style = p.getStyle();
        int color = p.getColor();
        p.setStyle(android.graphics.Paint.Style.FILL);
        p.setColor(mColor);
        c.drawRect(x, top, x + (dir * android.text.style.QuoteSpan.STRIPE_WIDTH), bottom, p);
        p.setStyle(style);
        p.setColor(color);
    }
}

