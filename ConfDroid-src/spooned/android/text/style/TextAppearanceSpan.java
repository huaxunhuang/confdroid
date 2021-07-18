/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * Sets the text color, size, style, and typeface to match a TextAppearance
 * resource.
 */
public class TextAppearanceSpan extends android.text.style.MetricAffectingSpan implements android.text.ParcelableSpan {
    private final java.lang.String mTypeface;

    private final int mStyle;

    private final int mTextSize;

    private final android.content.res.ColorStateList mTextColor;

    private final android.content.res.ColorStateList mTextColorLink;

    /**
     * Uses the specified TextAppearance resource to determine the
     * text appearance.  The <code>appearance</code> should be, for example,
     * <code>android.R.style.TextAppearance_Small</code>.
     */
    public TextAppearanceSpan(android.content.Context context, int appearance) {
        this(context, appearance, -1);
    }

    /**
     * Uses the specified TextAppearance resource to determine the
     * text appearance, and the specified text color resource
     * to determine the color.  The <code>appearance</code> should be,
     * for example, <code>android.R.style.TextAppearance_Small</code>,
     * and the <code>colorList</code> should be, for example,
     * <code>android.R.styleable.Theme_textColorPrimary</code>.
     */
    public TextAppearanceSpan(android.content.Context context, int appearance, int colorList) {
        android.content.res.ColorStateList textColor;
        android.content.res.TypedArray a = context.obtainStyledAttributes(appearance, com.android.internal.R.styleable.TextAppearance);
        textColor = a.getColorStateList(com.android.internal.R.styleable.TextAppearance_textColor);
        mTextColorLink = a.getColorStateList(com.android.internal.R.styleable.TextAppearance_textColorLink);
        mTextSize = a.getDimensionPixelSize(com.android.internal.R.styleable.TextAppearance_textSize, -1);
        mStyle = a.getInt(com.android.internal.R.styleable.TextAppearance_textStyle, 0);
        java.lang.String family = a.getString(com.android.internal.R.styleable.TextAppearance_fontFamily);
        if (family != null) {
            mTypeface = family;
        } else {
            int tf = a.getInt(com.android.internal.R.styleable.TextAppearance_typeface, 0);
            switch (tf) {
                case 1 :
                    mTypeface = "sans";
                    break;
                case 2 :
                    mTypeface = "serif";
                    break;
                case 3 :
                    mTypeface = "monospace";
                    break;
                default :
                    mTypeface = null;
                    break;
            }
        }
        a.recycle();
        if (colorList >= 0) {
            a = context.obtainStyledAttributes(com.android.internal.R.style.Theme, com.android.internal.R.styleable.Theme);
            textColor = a.getColorStateList(colorList);
            a.recycle();
        }
        mTextColor = textColor;
    }

    /**
     * Makes text be drawn with the specified typeface, size, style,
     * and colors.
     */
    public TextAppearanceSpan(java.lang.String family, int style, int size, android.content.res.ColorStateList color, android.content.res.ColorStateList linkColor) {
        mTypeface = family;
        mStyle = style;
        mTextSize = size;
        mTextColor = color;
        mTextColorLink = linkColor;
    }

    public TextAppearanceSpan(android.os.Parcel src) {
        mTypeface = src.readString();
        mStyle = src.readInt();
        mTextSize = src.readInt();
        if (src.readInt() != 0) {
            mTextColor = android.content.res.ColorStateList.CREATOR.createFromParcel(src);
        } else {
            mTextColor = null;
        }
        if (src.readInt() != 0) {
            mTextColorLink = android.content.res.ColorStateList.CREATOR.createFromParcel(src);
        } else {
            mTextColorLink = null;
        }
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
        return android.text.TextUtils.TEXT_APPEARANCE_SPAN;
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
        dest.writeString(mTypeface);
        dest.writeInt(mStyle);
        dest.writeInt(mTextSize);
        if (mTextColor != null) {
            dest.writeInt(1);
            mTextColor.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        if (mTextColorLink != null) {
            dest.writeInt(1);
            mTextColorLink.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
    }

    /**
     * Returns the typeface family specified by this span, or <code>null</code>
     * if it does not specify one.
     */
    public java.lang.String getFamily() {
        return mTypeface;
    }

    /**
     * Returns the text color specified by this span, or <code>null</code>
     * if it does not specify one.
     */
    public android.content.res.ColorStateList getTextColor() {
        return mTextColor;
    }

    /**
     * Returns the link color specified by this span, or <code>null</code>
     * if it does not specify one.
     */
    public android.content.res.ColorStateList getLinkTextColor() {
        return mTextColorLink;
    }

    /**
     * Returns the text size specified by this span, or <code>-1</code>
     * if it does not specify one.
     */
    public int getTextSize() {
        return mTextSize;
    }

    /**
     * Returns the text style specified by this span, or <code>0</code>
     * if it does not specify one.
     */
    public int getTextStyle() {
        return mStyle;
    }

    @java.lang.Override
    public void updateDrawState(android.text.TextPaint ds) {
        updateMeasureState(ds);
        if (mTextColor != null) {
            ds.setColor(mTextColor.getColorForState(ds.drawableState, 0));
        }
        if (mTextColorLink != null) {
            ds.linkColor = mTextColorLink.getColorForState(ds.drawableState, 0);
        }
    }

    @java.lang.Override
    public void updateMeasureState(android.text.TextPaint ds) {
        if ((mTypeface != null) || (mStyle != 0)) {
            android.graphics.Typeface tf = ds.getTypeface();
            int style = 0;
            if (tf != null) {
                style = tf.getStyle();
            }
            style |= mStyle;
            if (mTypeface != null) {
                tf = android.graphics.Typeface.create(mTypeface, style);
            } else
                if (tf == null) {
                    tf = android.graphics.Typeface.defaultFromStyle(style);
                } else {
                    tf = android.graphics.Typeface.create(tf, style);
                }

            int fake = style & (~tf.getStyle());
            if ((fake & android.graphics.Typeface.BOLD) != 0) {
                ds.setFakeBoldText(true);
            }
            if ((fake & android.graphics.Typeface.ITALIC) != 0) {
                ds.setTextSkewX(-0.25F);
            }
            ds.setTypeface(tf);
        }
        if (mTextSize > 0) {
            ds.setTextSize(mTextSize);
        }
    }
}

