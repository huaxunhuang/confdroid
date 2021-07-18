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
package android.support.v7.widget;


/**
 *
 *
 * @unknown 
 */
public class ContentFrameLayout extends android.widget.FrameLayout {
    public interface OnAttachListener {
        void onDetachedFromWindow();

        void onAttachedFromWindow();
    }

    private android.util.TypedValue mMinWidthMajor;

    private android.util.TypedValue mMinWidthMinor;

    private android.util.TypedValue mFixedWidthMajor;

    private android.util.TypedValue mFixedWidthMinor;

    private android.util.TypedValue mFixedHeightMajor;

    private android.util.TypedValue mFixedHeightMinor;

    private final android.graphics.Rect mDecorPadding;

    private android.support.v7.widget.ContentFrameLayout.OnAttachListener mAttachListener;

    public ContentFrameLayout(android.content.Context context) {
        this(context, null);
    }

    public ContentFrameLayout(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContentFrameLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDecorPadding = new android.graphics.Rect();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public void dispatchFitSystemWindows(android.graphics.Rect insets) {
        fitSystemWindows(insets);
    }

    public void setAttachListener(android.support.v7.widget.ContentFrameLayout.OnAttachListener attachListener) {
        mAttachListener = attachListener;
    }

    /**
     * Notify this view of the window decor view's padding. We use these values when working out
     * our size for the window size attributes.
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public void setDecorPadding(int left, int top, int right, int bottom) {
        mDecorPadding.set(left, top, right, bottom);
        if (android.support.v4.view.ViewCompat.isLaidOut(this)) {
            requestLayout();
        }
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final android.util.DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        final boolean isPortrait = metrics.widthPixels < metrics.heightPixels;
        final int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        boolean fixedWidth = false;
        if (widthMode == android.view.View.MeasureSpec.AT_MOST) {
            final android.util.TypedValue tvw = (isPortrait) ? mFixedWidthMinor : mFixedWidthMajor;
            if ((tvw != null) && (tvw.type != android.util.TypedValue.TYPE_NULL)) {
                int w = 0;
                if (tvw.type == android.util.TypedValue.TYPE_DIMENSION) {
                    w = ((int) (tvw.getDimension(metrics)));
                } else
                    if (tvw.type == android.util.TypedValue.TYPE_FRACTION) {
                        w = ((int) (tvw.getFraction(metrics.widthPixels, metrics.widthPixels)));
                    }

                if (w > 0) {
                    w -= mDecorPadding.left + mDecorPadding.right;
                    final int widthSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
                    widthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(java.lang.Math.min(w, widthSize), android.view.View.MeasureSpec.EXACTLY);
                    fixedWidth = true;
                }
            }
        }
        if (heightMode == android.view.View.MeasureSpec.AT_MOST) {
            final android.util.TypedValue tvh = (isPortrait) ? mFixedHeightMajor : mFixedHeightMinor;
            if ((tvh != null) && (tvh.type != android.util.TypedValue.TYPE_NULL)) {
                int h = 0;
                if (tvh.type == android.util.TypedValue.TYPE_DIMENSION) {
                    h = ((int) (tvh.getDimension(metrics)));
                } else
                    if (tvh.type == android.util.TypedValue.TYPE_FRACTION) {
                        h = ((int) (tvh.getFraction(metrics.heightPixels, metrics.heightPixels)));
                    }

                if (h > 0) {
                    h -= mDecorPadding.top + mDecorPadding.bottom;
                    final int heightSize = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
                    heightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(java.lang.Math.min(h, heightSize), android.view.View.MeasureSpec.EXACTLY);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        boolean measure = false;
        widthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(width, android.view.View.MeasureSpec.EXACTLY);
        if ((!fixedWidth) && (widthMode == android.view.View.MeasureSpec.AT_MOST)) {
            final android.util.TypedValue tv = (isPortrait) ? mMinWidthMinor : mMinWidthMajor;
            if ((tv != null) && (tv.type != android.util.TypedValue.TYPE_NULL)) {
                int min = 0;
                if (tv.type == android.util.TypedValue.TYPE_DIMENSION) {
                    min = ((int) (tv.getDimension(metrics)));
                } else
                    if (tv.type == android.util.TypedValue.TYPE_FRACTION) {
                        min = ((int) (tv.getFraction(metrics.widthPixels, metrics.widthPixels)));
                    }

                if (min > 0) {
                    min -= mDecorPadding.left + mDecorPadding.right;
                }
                if (width < min) {
                    widthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(min, android.view.View.MeasureSpec.EXACTLY);
                    measure = true;
                }
            }
        }
        if (measure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public android.util.TypedValue getMinWidthMajor() {
        if (mMinWidthMajor == null)
            mMinWidthMajor = new android.util.TypedValue();

        return mMinWidthMajor;
    }

    public android.util.TypedValue getMinWidthMinor() {
        if (mMinWidthMinor == null)
            mMinWidthMinor = new android.util.TypedValue();

        return mMinWidthMinor;
    }

    public android.util.TypedValue getFixedWidthMajor() {
        if (mFixedWidthMajor == null)
            mFixedWidthMajor = new android.util.TypedValue();

        return mFixedWidthMajor;
    }

    public android.util.TypedValue getFixedWidthMinor() {
        if (mFixedWidthMinor == null)
            mFixedWidthMinor = new android.util.TypedValue();

        return mFixedWidthMinor;
    }

    public android.util.TypedValue getFixedHeightMajor() {
        if (mFixedHeightMajor == null)
            mFixedHeightMajor = new android.util.TypedValue();

        return mFixedHeightMajor;
    }

    public android.util.TypedValue getFixedHeightMinor() {
        if (mFixedHeightMinor == null)
            mFixedHeightMinor = new android.util.TypedValue();

        return mFixedHeightMinor;
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mAttachListener != null) {
            mAttachListener.onAttachedFromWindow();
        }
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttachListener != null) {
            mAttachListener.onDetachedFromWindow();
        }
    }
}

