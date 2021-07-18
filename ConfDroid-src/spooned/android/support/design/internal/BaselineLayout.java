/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.design.internal;


/**
 * A simple ViewGroup that aligns all the views inside on a baseline.
 *
 * @unknown 
 */
public class BaselineLayout extends android.view.ViewGroup {
    private int mBaseline = -1;

    public BaselineLayout(android.content.Context context) {
        super(context, null, 0);
    }

    public BaselineLayout(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public BaselineLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int count = getChildCount();
        int maxWidth = 0;
        int maxHeight = 0;
        int maxChildBaseline = -1;
        int maxChildDescent = -1;
        int childState = 0;
        for (int i = 0; i < count; i++) {
            final android.view.View child = getChildAt(i);
            if (child.getVisibility() == android.view.View.GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            final int baseline = child.getBaseline();
            if (baseline != (-1)) {
                maxChildBaseline = java.lang.Math.max(maxChildBaseline, baseline);
                maxChildDescent = java.lang.Math.max(maxChildDescent, child.getMeasuredHeight() - baseline);
            }
            maxWidth = java.lang.Math.max(maxWidth, child.getMeasuredWidth());
            maxHeight = java.lang.Math.max(maxHeight, child.getMeasuredHeight());
            childState = android.support.v7.widget.ViewUtils.combineMeasuredStates(childState, android.support.v4.view.ViewCompat.getMeasuredState(child));
        }
        if (maxChildBaseline != (-1)) {
            maxHeight = java.lang.Math.max(maxHeight, maxChildBaseline + maxChildDescent);
            mBaseline = maxChildBaseline;
        }
        maxHeight = java.lang.Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = java.lang.Math.max(maxWidth, getSuggestedMinimumWidth());
        setMeasuredDimension(android.support.v4.view.ViewCompat.resolveSizeAndState(maxWidth, widthMeasureSpec, childState), android.support.v4.view.ViewCompat.resolveSizeAndState(maxHeight, heightMeasureSpec, childState << android.view.View.MEASURED_HEIGHT_STATE_SHIFT));
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        final int parentLeft = getPaddingLeft();
        final int parentRight = (right - left) - getPaddingRight();
        final int parentContentWidth = parentRight - parentLeft;
        final int parentTop = getPaddingTop();
        for (int i = 0; i < count; i++) {
            final android.view.View child = getChildAt(i);
            if (child.getVisibility() == android.view.View.GONE) {
                continue;
            }
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();
            final int childLeft = parentLeft + ((parentContentWidth - width) / 2);
            final int childTop;
            if ((mBaseline != (-1)) && (child.getBaseline() != (-1))) {
                childTop = (parentTop + mBaseline) - child.getBaseline();
            } else {
                childTop = parentTop;
            }
            child.layout(childLeft, childTop, childLeft + width, childTop + height);
        }
    }

    @java.lang.Override
    public int getBaseline() {
        return mBaseline;
    }
}

