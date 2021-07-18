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
package android.support.design.widget;


/**
 * The {@link Behavior} for a scrolling view that is positioned vertically below another view.
 * See {@link HeaderBehavior}.
 */
abstract class HeaderScrollingViewBehavior extends android.support.design.widget.ViewOffsetBehavior<android.view.View> {
    final android.graphics.Rect mTempRect1 = new android.graphics.Rect();

    final android.graphics.Rect mTempRect2 = new android.graphics.Rect();

    private int mVerticalLayoutGap = 0;

    private int mOverlayTop;

    public HeaderScrollingViewBehavior() {
    }

    public HeaderScrollingViewBehavior(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    @java.lang.Override
    public boolean onMeasureChild(android.support.design.widget.CoordinatorLayout parent, android.view.View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        final int childLpHeight = child.getLayoutParams().height;
        if ((childLpHeight == android.view.ViewGroup.LayoutParams.MATCH_PARENT) || (childLpHeight == android.view.ViewGroup.LayoutParams.WRAP_CONTENT)) {
            // If the menu's height is set to match_parent/wrap_content then measure it
            // with the maximum visible height
            final java.util.List<android.view.View> dependencies = parent.getDependencies(child);
            final android.view.View header = findFirstDependency(dependencies);
            if (header != null) {
                if (android.support.v4.view.ViewCompat.getFitsSystemWindows(header) && (!android.support.v4.view.ViewCompat.getFitsSystemWindows(child))) {
                    // If the header is fitting system windows then we need to also,
                    // otherwise we'll get CoL's compatible measuring
                    android.support.v4.view.ViewCompat.setFitsSystemWindows(child, true);
                    if (android.support.v4.view.ViewCompat.getFitsSystemWindows(child)) {
                        // If the set succeeded, trigger a new layout and return true
                        child.requestLayout();
                        return true;
                    }
                }
                int availableHeight = android.view.View.MeasureSpec.getSize(parentHeightMeasureSpec);
                if (availableHeight == 0) {
                    // If the measure spec doesn't specify a size, use the current height
                    availableHeight = parent.getHeight();
                }
                final int height = (availableHeight - header.getMeasuredHeight()) + getScrollRange(header);
                final int heightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(height, childLpHeight == android.view.ViewGroup.LayoutParams.MATCH_PARENT ? android.view.View.MeasureSpec.EXACTLY : android.view.View.MeasureSpec.AT_MOST);
                // Now measure the scrolling view with the correct height
                parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    protected void layoutChild(final android.support.design.widget.CoordinatorLayout parent, final android.view.View child, final int layoutDirection) {
        final java.util.List<android.view.View> dependencies = parent.getDependencies(child);
        final android.view.View header = findFirstDependency(dependencies);
        if (header != null) {
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
            final android.graphics.Rect available = mTempRect1;
            available.set(parent.getPaddingLeft() + lp.leftMargin, header.getBottom() + lp.topMargin, (parent.getWidth() - parent.getPaddingRight()) - lp.rightMargin, ((parent.getHeight() + header.getBottom()) - parent.getPaddingBottom()) - lp.bottomMargin);
            final android.support.v4.view.WindowInsetsCompat parentInsets = parent.getLastWindowInsets();
            if (((parentInsets != null) && android.support.v4.view.ViewCompat.getFitsSystemWindows(parent)) && (!android.support.v4.view.ViewCompat.getFitsSystemWindows(child))) {
                // If we're set to handle insets but this child isn't, then it has been measured as
                // if there are no insets. We need to lay it out to match horizontally.
                // Top and bottom and already handled in the logic above
                available.left += parentInsets.getSystemWindowInsetLeft();
                available.right -= parentInsets.getSystemWindowInsetRight();
            }
            final android.graphics.Rect out = mTempRect2;
            android.support.v4.view.GravityCompat.apply(android.support.design.widget.HeaderScrollingViewBehavior.resolveGravity(lp.gravity), child.getMeasuredWidth(), child.getMeasuredHeight(), available, out, layoutDirection);
            final int overlap = getOverlapPixelsForOffset(header);
            child.layout(out.left, out.top - overlap, out.right, out.bottom - overlap);
            mVerticalLayoutGap = out.top - header.getBottom();
        } else {
            // If we don't have a dependency, let super handle it
            super.layoutChild(parent, child, layoutDirection);
            mVerticalLayoutGap = 0;
        }
    }

    float getOverlapRatioForOffset(final android.view.View header) {
        return 1.0F;
    }

    final int getOverlapPixelsForOffset(final android.view.View header) {
        return mOverlayTop == 0 ? 0 : android.support.design.widget.MathUtils.constrain(((int) (getOverlapRatioForOffset(header) * mOverlayTop)), 0, mOverlayTop);
    }

    private static int resolveGravity(int gravity) {
        return gravity == android.view.Gravity.NO_GRAVITY ? android.support.v4.view.GravityCompat.START | android.view.Gravity.TOP : gravity;
    }

    abstract android.view.View findFirstDependency(java.util.List<android.view.View> views);

    int getScrollRange(android.view.View v) {
        return v.getMeasuredHeight();
    }

    /**
     * The gap between the top of the scrolling view and the bottom of the header layout in pixels.
     */
    final int getVerticalLayoutGap() {
        return mVerticalLayoutGap;
    }

    /**
     * Set the distance that this view should overlap any {@link AppBarLayout}.
     *
     * @param overlayTop
     * 		the distance in px
     */
    public final void setOverlayTop(int overlayTop) {
        mOverlayTop = overlayTop;
    }

    /**
     * Returns the distance that this view should overlap any {@link AppBarLayout}.
     */
    public final int getOverlayTop() {
        return mOverlayTop;
    }
}

