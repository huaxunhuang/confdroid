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


public class BulletSpan implements android.text.ParcelableSpan , android.text.style.LeadingMarginSpan {
    private final int mGapWidth;

    private final boolean mWantColor;

    private final int mColor;

    private static final int BULLET_RADIUS = 3;

    private static android.graphics.Path sBulletPath = null;

    public static final int STANDARD_GAP_WIDTH = 2;

    public BulletSpan() {
        mGapWidth = android.text.style.BulletSpan.STANDARD_GAP_WIDTH;
        mWantColor = false;
        mColor = 0;
    }

    public BulletSpan(int gapWidth) {
        mGapWidth = gapWidth;
        mWantColor = false;
        mColor = 0;
    }

    public BulletSpan(int gapWidth, int color) {
        mGapWidth = gapWidth;
        mWantColor = true;
        mColor = color;
    }

    public BulletSpan(android.os.Parcel src) {
        mGapWidth = src.readInt();
        mWantColor = src.readInt() != 0;
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
        return android.text.TextUtils.BULLET_SPAN;
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
        dest.writeInt(mGapWidth);
        dest.writeInt(mWantColor ? 1 : 0);
        dest.writeInt(mColor);
    }

    public int getLeadingMargin(boolean first) {
        return (2 * android.text.style.BulletSpan.BULLET_RADIUS) + mGapWidth;
    }

    public void drawLeadingMargin(android.graphics.Canvas c, android.graphics.Paint p, int x, int dir, int top, int baseline, int bottom, java.lang.CharSequence text, int start, int end, boolean first, android.text.Layout l) {
        if (((android.text.Spanned) (text)).getSpanStart(this) == start) {
            android.graphics.Paint.Style style = p.getStyle();
            int oldcolor = 0;
            if (mWantColor) {
                oldcolor = p.getColor();
                p.setColor(mColor);
            }
            p.setStyle(android.graphics.Paint.Style.FILL);
            if (c.isHardwareAccelerated()) {
                if (android.text.style.BulletSpan.sBulletPath == null) {
                    android.text.style.BulletSpan.sBulletPath = new android.graphics.Path();
                    // Bullet is slightly better to avoid aliasing artifacts on mdpi devices.
                    android.text.style.BulletSpan.sBulletPath.addCircle(0.0F, 0.0F, 1.2F * android.text.style.BulletSpan.BULLET_RADIUS, android.graphics.Path.Direction.CW);
                }
                c.save();
                c.translate(x + (dir * android.text.style.BulletSpan.BULLET_RADIUS), (top + bottom) / 2.0F);
                c.drawPath(android.text.style.BulletSpan.sBulletPath, p);
                c.restore();
            } else {
                c.drawCircle(x + (dir * android.text.style.BulletSpan.BULLET_RADIUS), (top + bottom) / 2.0F, android.text.style.BulletSpan.BULLET_RADIUS, p);
            }
            if (mWantColor) {
                p.setColor(oldcolor);
            }
            p.setStyle(style);
        }
    }
}

