/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget;


/**
 * Subclass of FrameLayout that support scale layout area size for children.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class ScaleFrameLayout extends android.widget.FrameLayout {
    private static final int DEFAULT_CHILD_GRAVITY = android.view.Gravity.TOP | android.view.Gravity.START;

    private float mLayoutScaleX = 1.0F;

    private float mLayoutScaleY = 1.0F;

    private float mChildScale = 1.0F;

    public ScaleFrameLayout(android.content.Context context) {
        this(context, null);
    }

    public ScaleFrameLayout(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleFrameLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLayoutScaleX(float scaleX) {
        if (scaleX != mLayoutScaleX) {
            mLayoutScaleX = scaleX;
            requestLayout();
        }
    }

    public void setLayoutScaleY(float scaleY) {
        if (scaleY != mLayoutScaleY) {
            mLayoutScaleY = scaleY;
            requestLayout();
        }
    }

    public void setChildScale(float scale) {
        if (mChildScale != scale) {
            mChildScale = scale;
            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).setScaleX(scale);
                getChildAt(i).setScaleY(scale);
            }
        }
    }

    @java.lang.Override
    public void addView(android.view.View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        child.setScaleX(mChildScale);
        child.setScaleY(mChildScale);
    }

    @java.lang.Override
    protected boolean addViewInLayout(android.view.View child, int index, android.view.ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        boolean ret = super.addViewInLayout(child, index, params, preventRequestLayout);
        if (ret) {
            child.setScaleX(mChildScale);
            child.setScaleY(mChildScale);
        }
        return ret;
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        final int parentLeft;
        final int parentRight;
        final int layoutDirection = getLayoutDirection();
        final float pivotX = (layoutDirection == android.view.View.LAYOUT_DIRECTION_RTL) ? getWidth() - getPivotX() : getPivotX();
        if (mLayoutScaleX != 1.0F) {
            parentLeft = getPaddingLeft() + ((int) ((pivotX - (pivotX / mLayoutScaleX)) + 0.5F));
            parentRight = ((int) ((pivotX + (((right - left) - pivotX) / mLayoutScaleX)) + 0.5F)) - getPaddingRight();
        } else {
            parentLeft = getPaddingLeft();
            parentRight = (right - left) - getPaddingRight();
        }
        final int parentTop;
        final int parentBottom;
        final float pivotY = getPivotY();
        if (mLayoutScaleY != 1.0F) {
            parentTop = getPaddingTop() + ((int) ((pivotY - (pivotY / mLayoutScaleY)) + 0.5F));
            parentBottom = ((int) ((pivotY + (((bottom - top) - pivotY) / mLayoutScaleY)) + 0.5F)) - getPaddingBottom();
        } else {
            parentTop = getPaddingTop();
            parentBottom = (bottom - top) - getPaddingBottom();
        }
        for (int i = 0; i < count; i++) {
            final android.view.View child = getChildAt(i);
            if (child.getVisibility() != android.view.View.GONE) {
                final android.widget.FrameLayout.LayoutParams lp = ((android.widget.FrameLayout.LayoutParams) (child.getLayoutParams()));
                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();
                int childLeft;
                int childTop;
                int gravity = lp.gravity;
                if (gravity == (-1)) {
                    gravity = android.support.v17.leanback.widget.ScaleFrameLayout.DEFAULT_CHILD_GRAVITY;
                }
                final int absoluteGravity = android.view.Gravity.getAbsoluteGravity(gravity, layoutDirection);
                final int verticalGravity = gravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
                switch (absoluteGravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case android.view.Gravity.CENTER_HORIZONTAL :
                        childLeft = ((parentLeft + (((parentRight - parentLeft) - width) / 2)) + lp.leftMargin) - lp.rightMargin;
                        break;
                    case android.view.Gravity.RIGHT :
                        childLeft = (parentRight - width) - lp.rightMargin;
                        break;
                    case android.view.Gravity.LEFT :
                    default :
                        childLeft = parentLeft + lp.leftMargin;
                }
                switch (verticalGravity) {
                    case android.view.Gravity.TOP :
                        childTop = parentTop + lp.topMargin;
                        break;
                    case android.view.Gravity.CENTER_VERTICAL :
                        childTop = ((parentTop + (((parentBottom - parentTop) - height) / 2)) + lp.topMargin) - lp.bottomMargin;
                        break;
                    case android.view.Gravity.BOTTOM :
                        childTop = (parentBottom - height) - lp.bottomMargin;
                        break;
                    default :
                        childTop = parentTop + lp.topMargin;
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
                // synchronize child pivot to be same as ScaleFrameLayout's pivot
                child.setPivotX(pivotX - childLeft);
                child.setPivotY(pivotY - childTop);
            }
        }
    }

    private static int getScaledMeasureSpec(int measureSpec, float scale) {
        return scale == 1.0F ? measureSpec : android.view.View.MeasureSpec.makeMeasureSpec(((int) ((android.view.View.MeasureSpec.getSize(measureSpec) / scale) + 0.5F)), android.view.View.MeasureSpec.getMode(measureSpec));
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if ((mLayoutScaleX != 1.0F) || (mLayoutScaleY != 1.0F)) {
            final int scaledWidthMeasureSpec = android.support.v17.leanback.widget.ScaleFrameLayout.getScaledMeasureSpec(widthMeasureSpec, mLayoutScaleX);
            final int scaledHeightMeasureSpec = android.support.v17.leanback.widget.ScaleFrameLayout.getScaledMeasureSpec(heightMeasureSpec, mLayoutScaleY);
            super.onMeasure(scaledWidthMeasureSpec, scaledHeightMeasureSpec);
            setMeasuredDimension(((int) ((getMeasuredWidth() * mLayoutScaleX) + 0.5F)), ((int) ((getMeasuredHeight() * mLayoutScaleY) + 0.5F)));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * setForeground() is not supported,  throws UnsupportedOperationException() when called.
     */
    @java.lang.Override
    public void setForeground(android.graphics.drawable.Drawable d) {
        throw new java.lang.UnsupportedOperationException();
    }
}

