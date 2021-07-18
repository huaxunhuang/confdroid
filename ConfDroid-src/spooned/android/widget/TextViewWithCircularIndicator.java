/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.widget;


class TextViewWithCircularIndicator extends android.widget.TextView {
    private static final int SELECTED_CIRCLE_ALPHA = 60;

    private final android.graphics.Paint mCirclePaint = new android.graphics.Paint();

    private final java.lang.String mItemIsSelectedText;

    private int mCircleColor;

    private boolean mDrawIndicator;

    public TextViewWithCircularIndicator(android.content.Context context) {
        this(context, null);
    }

    public TextViewWithCircularIndicator(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewWithCircularIndicator(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TextViewWithCircularIndicator(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);
        // Use Theme attributes if possible
        final android.content.res.TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.DatePicker, defStyleAttr, defStyleRes);
        final int resId = a.getResourceId(R.styleable.DatePicker_yearListItemTextAppearance, -1);
        if (resId != (-1)) {
            setTextAppearance(context, resId);
        }
        final android.content.res.Resources res = context.getResources();
        mItemIsSelectedText = res.getString(R.string.item_is_selected);
        a.recycle();
        init();
    }

    private void init() {
        mCirclePaint.setTypeface(android.graphics.Typeface.create(mCirclePaint.getTypeface(), android.graphics.Typeface.BOLD));
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setTextAlign(android.graphics.Paint.Align.CENTER);
        mCirclePaint.setStyle(android.graphics.Paint.Style.FILL);
    }

    public void setCircleColor(int color) {
        if (color != mCircleColor) {
            mCircleColor = color;
            mCirclePaint.setColor(mCircleColor);
            mCirclePaint.setAlpha(android.widget.TextViewWithCircularIndicator.SELECTED_CIRCLE_ALPHA);
            requestLayout();
        }
    }

    public void setDrawIndicator(boolean drawIndicator) {
        mDrawIndicator = drawIndicator;
    }

    @java.lang.Override
    public void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawIndicator) {
            final int width = getWidth();
            final int height = getHeight();
            int radius = java.lang.Math.min(width, height) / 2;
            canvas.drawCircle(width / 2, height / 2, radius, mCirclePaint);
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getContentDescription() {
        java.lang.CharSequence itemText = getText();
        if (mDrawIndicator) {
            return java.lang.String.format(mItemIsSelectedText, itemText);
        } else {
            return itemText;
        }
    }
}

