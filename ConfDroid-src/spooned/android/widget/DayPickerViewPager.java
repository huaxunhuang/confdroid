/**
 * Copyright (C) 2015 The Android Open Source Project
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


/**
 * This displays a list of months in a calendar format with selectable days.
 */
class DayPickerViewPager extends com.android.internal.widget.ViewPager {
    private final java.util.ArrayList<android.view.View> mMatchParentChildren = new java.util.ArrayList<>(1);

    public DayPickerViewPager(android.content.Context context) {
        this(context, null);
    }

    public DayPickerViewPager(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayPickerViewPager(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DayPickerViewPager(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        populate();
        // Everything below is mostly copied from FrameLayout.
        int count = getChildCount();
        final boolean measureMatchParentChildren = (android.widget.MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) || (android.widget.MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY);
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        for (int i = 0; i < count; i++) {
            final android.view.View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                final android.widget.LayoutParams lp = ((android.widget.LayoutParams) (child.getLayoutParams()));
                maxWidth = java.lang.Math.max(maxWidth, child.getMeasuredWidth());
                maxHeight = java.lang.Math.max(maxHeight, child.getMeasuredHeight());
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren) {
                    if ((lp.width == LayoutParams.MATCH_PARENT) || (lp.height == LayoutParams.MATCH_PARENT)) {
                        mMatchParentChildren.add(child);
                    }
                }
            }
        }
        // Account for padding too
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();
        // Check against our minimum height and width
        maxHeight = java.lang.Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = java.lang.Math.max(maxWidth, getSuggestedMinimumWidth());
        // Check against our foreground's minimum height and width
        final android.graphics.drawable.Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight = java.lang.Math.max(maxHeight, drawable.getMinimumHeight());
            maxWidth = java.lang.Math.max(maxWidth, drawable.getMinimumWidth());
        }
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
        count = mMatchParentChildren.size();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                final android.view.View child = mMatchParentChildren.get(i);
                final android.widget.LayoutParams lp = ((android.widget.LayoutParams) (child.getLayoutParams()));
                final int childWidthMeasureSpec;
                final int childHeightMeasureSpec;
                if (lp.width == LayoutParams.MATCH_PARENT) {
                    childWidthMeasureSpec = android.widget.MeasureSpec.makeMeasureSpec((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), lp.width);
                }
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    childHeightMeasureSpec = android.widget.MeasureSpec.makeMeasureSpec((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), lp.height);
                }
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
        mMatchParentChildren.clear();
    }

    @java.lang.Override
    protected <T extends android.view.View> T findViewByPredicateTraversal(java.util.function.Predicate<android.view.View> predicate, android.view.View childToSkip) {
        if (predicate.test(this)) {
            return ((T) (this));
        }
        // Always try the selected view first.
        final android.widget.DayPickerPagerAdapter adapter = ((android.widget.DayPickerPagerAdapter) (getAdapter()));
        final android.widget.SimpleMonthView current = adapter.getView(getCurrent());
        if ((current != childToSkip) && (current != null)) {
            final android.view.View v = current.findViewByPredicate(predicate);
            if (v != null) {
                return ((T) (v));
            }
        }
        final int len = getChildCount();
        for (int i = 0; i < len; i++) {
            final android.view.View child = getChildAt(i);
            if ((child != childToSkip) && (child != current)) {
                final android.view.View v = child.findViewByPredicate(predicate);
                if (v != null) {
                    return ((T) (v));
                }
            }
        }
        return null;
    }
}

