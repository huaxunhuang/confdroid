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
 * AppBarLayout is a vertical {@link LinearLayout} which implements many of the features of
 * material designs app bar concept, namely scrolling gestures.
 * <p>
 * Children should provide their desired scrolling behavior through
 * {@link LayoutParams#setScrollFlags(int)} and the associated layout xml attribute:
 * {@code app:layout_scrollFlags}.
 *
 * <p>
 * This view depends heavily on being used as a direct child within a {@link CoordinatorLayout}.
 * If you use AppBarLayout within a different {@link ViewGroup}, most of it's functionality will
 * not work.
 * <p>
 * AppBarLayout also requires a separate scrolling sibling in order to know when to scroll.
 * The binding is done through the {@link ScrollingViewBehavior} behavior class, meaning that you
 * should set your scrolling view's behavior to be an instance of {@link ScrollingViewBehavior}.
 * A string resource containing the full class name is available.
 *
 * <pre>
 * &lt;android.support.design.widget.CoordinatorLayout
 *         xmlns:android=&quot;http://schemas.android.com/apk/res/android&quot;
 *         xmlns:app=&quot;http://schemas.android.com/apk/res-auto&quot;
 *         android:layout_width=&quot;match_parent&quot;
 *         android:layout_height=&quot;match_parent&quot;&gt;
 *
 *     &lt;android.support.v4.widget.NestedScrollView
 *             android:layout_width=&quot;match_parent&quot;
 *             android:layout_height=&quot;match_parent&quot;
 *             app:layout_behavior=&quot;@string/appbar_scrolling_view_behavior&quot;&gt;
 *
 *         &lt;!-- Your scrolling content --&gt;
 *
 *     &lt;/android.support.v4.widget.NestedScrollView&gt;
 *
 *     &lt;android.support.design.widget.AppBarLayout
 *             android:layout_height=&quot;wrap_content&quot;
 *             android:layout_width=&quot;match_parent&quot;&gt;
 *
 *         &lt;android.support.v7.widget.Toolbar
 *                 ...
 *                 app:layout_scrollFlags=&quot;scroll|enterAlways&quot;/&gt;
 *
 *         &lt;android.support.design.widget.TabLayout
 *                 ...
 *                 app:layout_scrollFlags=&quot;scroll|enterAlways&quot;/&gt;
 *
 *     &lt;/android.support.design.widget.AppBarLayout&gt;
 *
 * &lt;/android.support.design.widget.CoordinatorLayout&gt;
 * </pre>
 *
 * @see <a href="http://www.google.com/design/spec/layout/structure.html#structure-app-bar">
http://www.google.com/design/spec/layout/structure.html#structure-app-bar</a>
 */
@android.support.design.widget.CoordinatorLayout.DefaultBehavior(android.support.design.widget.AppBarLayout.Behavior.class)
public class AppBarLayout extends android.widget.LinearLayout {
    static final int PENDING_ACTION_NONE = 0x0;

    static final int PENDING_ACTION_EXPANDED = 0x1;

    static final int PENDING_ACTION_COLLAPSED = 0x2;

    static final int PENDING_ACTION_ANIMATE_ENABLED = 0x4;

    /**
     * Interface definition for a callback to be invoked when an {@link AppBarLayout}'s vertical
     * offset changes.
     */
    public interface OnOffsetChangedListener {
        /**
         * Called when the {@link AppBarLayout}'s layout offset has been changed. This allows
         * child views to implement custom behavior based on the offset (for instance pinning a
         * view at a certain y value).
         *
         * @param appBarLayout
         * 		the {@link AppBarLayout} which offset has changed
         * @param verticalOffset
         * 		the vertical offset for the parent {@link AppBarLayout}, in px
         */
        void onOffsetChanged(android.support.design.widget.AppBarLayout appBarLayout, int verticalOffset);
    }

    private static final int INVALID_SCROLL_RANGE = -1;

    private int mTotalScrollRange = android.support.design.widget.AppBarLayout.INVALID_SCROLL_RANGE;

    private int mDownPreScrollRange = android.support.design.widget.AppBarLayout.INVALID_SCROLL_RANGE;

    private int mDownScrollRange = android.support.design.widget.AppBarLayout.INVALID_SCROLL_RANGE;

    private boolean mHaveChildWithInterpolator;

    private int mPendingAction = android.support.design.widget.AppBarLayout.PENDING_ACTION_NONE;

    private android.support.v4.view.WindowInsetsCompat mLastInsets;

    private java.util.List<android.support.design.widget.AppBarLayout.OnOffsetChangedListener> mListeners;

    private boolean mCollapsible;

    private boolean mCollapsed;

    private final int[] mTmpStatesArray = new int[2];

    public AppBarLayout(android.content.Context context) {
        this(context, null);
    }

    public AppBarLayout(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        setOrientation(android.widget.LinearLayout.VERTICAL);
        android.support.design.widget.ThemeUtils.checkAppCompatTheme(context);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            // Use the bounds view outline provider so that we cast a shadow, even without a
            // background
            android.support.design.widget.ViewUtilsLollipop.setBoundsViewOutlineProvider(this);
            // If we're running on API 21+, we should reset any state list animator from our
            // default style
            android.support.design.widget.ViewUtilsLollipop.setStateListAnimatorFromAttrs(this, attrs, 0, R.style.Widget_Design_AppBarLayout);
        }
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AppBarLayout, 0, R.style.Widget_Design_AppBarLayout);
        android.support.v4.view.ViewCompat.setBackground(this, a.getDrawable(R.styleable.AppBarLayout_android_background));
        if (a.hasValue(R.styleable.AppBarLayout_expanded)) {
            setExpanded(a.getBoolean(R.styleable.AppBarLayout_expanded, false));
        }
        if ((android.os.Build.VERSION.SDK_INT >= 21) && a.hasValue(R.styleable.AppBarLayout_elevation)) {
            android.support.design.widget.ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(this, a.getDimensionPixelSize(R.styleable.AppBarLayout_elevation, 0));
        }
        a.recycle();
        android.support.v4.view.ViewCompat.setOnApplyWindowInsetsListener(this, new android.support.v4.view.OnApplyWindowInsetsListener() {
            @java.lang.Override
            public android.support.v4.view.WindowInsetsCompat onApplyWindowInsets(android.view.View v, android.support.v4.view.WindowInsetsCompat insets) {
                return onWindowInsetChanged(insets);
            }
        });
    }

    /**
     * Add a listener that will be called when the offset of this {@link AppBarLayout} changes.
     *
     * @param listener
     * 		The listener that will be called when the offset changes.]
     * @see #removeOnOffsetChangedListener(OnOffsetChangedListener)
     */
    public void addOnOffsetChangedListener(android.support.design.widget.AppBarLayout.OnOffsetChangedListener listener) {
        if (mListeners == null) {
            mListeners = new java.util.ArrayList<>();
        }
        if ((listener != null) && (!mListeners.contains(listener))) {
            mListeners.add(listener);
        }
    }

    /**
     * Remove the previously added {@link OnOffsetChangedListener}.
     *
     * @param listener
     * 		the listener to remove.
     */
    public void removeOnOffsetChangedListener(android.support.design.widget.AppBarLayout.OnOffsetChangedListener listener) {
        if ((mListeners != null) && (listener != null)) {
            mListeners.remove(listener);
        }
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        invalidateScrollRanges();
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        invalidateScrollRanges();
        mHaveChildWithInterpolator = false;
        for (int i = 0, z = getChildCount(); i < z; i++) {
            final android.view.View child = getChildAt(i);
            final android.support.design.widget.AppBarLayout.LayoutParams childLp = ((android.support.design.widget.AppBarLayout.LayoutParams) (child.getLayoutParams()));
            final android.view.animation.Interpolator interpolator = childLp.getScrollInterpolator();
            if (interpolator != null) {
                mHaveChildWithInterpolator = true;
                break;
            }
        }
        updateCollapsible();
    }

    private void updateCollapsible() {
        boolean haveCollapsibleChild = false;
        for (int i = 0, z = getChildCount(); i < z; i++) {
            if (((android.support.design.widget.AppBarLayout.LayoutParams) (getChildAt(i).getLayoutParams())).isCollapsible()) {
                haveCollapsibleChild = true;
                break;
            }
        }
        setCollapsibleState(haveCollapsibleChild);
    }

    private void invalidateScrollRanges() {
        // Invalidate the scroll ranges
        mTotalScrollRange = android.support.design.widget.AppBarLayout.INVALID_SCROLL_RANGE;
        mDownPreScrollRange = android.support.design.widget.AppBarLayout.INVALID_SCROLL_RANGE;
        mDownScrollRange = android.support.design.widget.AppBarLayout.INVALID_SCROLL_RANGE;
    }

    @java.lang.Override
    public void setOrientation(int orientation) {
        if (orientation != android.widget.LinearLayout.VERTICAL) {
            throw new java.lang.IllegalArgumentException("AppBarLayout is always vertical and does" + " not support horizontal orientation");
        }
        super.setOrientation(orientation);
    }

    /**
     * Sets whether this {@link AppBarLayout} is expanded or not, animating if it has already
     * been laid out.
     *
     * <p>As with {@link AppBarLayout}'s scrolling, this method relies on this layout being a
     * direct child of a {@link CoordinatorLayout}.</p>
     *
     * @param expanded
     * 		true if the layout should be fully expanded, false if it should
     * 		be fully collapsed
     * @unknown ref android.support.design.R.styleable#AppBarLayout_expanded
     */
    public void setExpanded(boolean expanded) {
        setExpanded(expanded, android.support.v4.view.ViewCompat.isLaidOut(this));
    }

    /**
     * Sets whether this {@link AppBarLayout} is expanded or not.
     *
     * <p>As with {@link AppBarLayout}'s scrolling, this method relies on this layout being a
     * direct child of a {@link CoordinatorLayout}.</p>
     *
     * @param expanded
     * 		true if the layout should be fully expanded, false if it should
     * 		be fully collapsed
     * @param animate
     * 		Whether to animate to the new state
     * @unknown ref android.support.design.R.styleable#AppBarLayout_expanded
     */
    public void setExpanded(boolean expanded, boolean animate) {
        mPendingAction = (expanded ? android.support.design.widget.AppBarLayout.PENDING_ACTION_EXPANDED : android.support.design.widget.AppBarLayout.PENDING_ACTION_COLLAPSED) | (animate ? android.support.design.widget.AppBarLayout.PENDING_ACTION_ANIMATE_ENABLED : 0);
        requestLayout();
    }

    @java.lang.Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof android.support.design.widget.AppBarLayout.LayoutParams;
    }

    @java.lang.Override
    protected android.support.design.widget.AppBarLayout.LayoutParams generateDefaultLayoutParams() {
        return new android.support.design.widget.AppBarLayout.LayoutParams(android.support.design.widget.AppBarLayout.LayoutParams.MATCH_PARENT, android.support.design.widget.AppBarLayout.LayoutParams.WRAP_CONTENT);
    }

    @java.lang.Override
    public android.support.design.widget.AppBarLayout.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.support.design.widget.AppBarLayout.LayoutParams(getContext(), attrs);
    }

    @java.lang.Override
    protected android.support.design.widget.AppBarLayout.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        if (p instanceof android.widget.LinearLayout.LayoutParams) {
            return new android.support.design.widget.AppBarLayout.LayoutParams(((android.widget.LinearLayout.LayoutParams) (p)));
        } else
            if (p instanceof android.view.ViewGroup.MarginLayoutParams) {
                return new android.support.design.widget.AppBarLayout.LayoutParams(((android.view.ViewGroup.MarginLayoutParams) (p)));
            }

        return new android.support.design.widget.AppBarLayout.LayoutParams(p);
    }

    boolean hasChildWithInterpolator() {
        return mHaveChildWithInterpolator;
    }

    /**
     * Returns the scroll range of all children.
     *
     * @return the scroll range in px
     */
    public final int getTotalScrollRange() {
        if (mTotalScrollRange != android.support.design.widget.AppBarLayout.INVALID_SCROLL_RANGE) {
            return mTotalScrollRange;
        }
        int range = 0;
        for (int i = 0, z = getChildCount(); i < z; i++) {
            final android.view.View child = getChildAt(i);
            final android.support.design.widget.AppBarLayout.LayoutParams lp = ((android.support.design.widget.AppBarLayout.LayoutParams) (child.getLayoutParams()));
            final int childHeight = child.getMeasuredHeight();
            final int flags = lp.mScrollFlags;
            if ((flags & android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL) != 0) {
                // We're set to scroll so add the child's height
                range += (childHeight + lp.topMargin) + lp.bottomMargin;
                if ((flags & android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
                    // For a collapsing scroll, we to take the collapsed height into account.
                    // We also break straight away since later views can't scroll beneath
                    // us
                    range -= android.support.v4.view.ViewCompat.getMinimumHeight(child);
                    break;
                }
            } else {
                // As soon as a view doesn't have the scroll flag, we end the range calculation.
                // This is because views below can not scroll under a fixed view.
                break;
            }
        }
        return mTotalScrollRange = java.lang.Math.max(0, range - getTopInset());
    }

    boolean hasScrollableChildren() {
        return getTotalScrollRange() != 0;
    }

    /**
     * Return the scroll range when scrolling up from a nested pre-scroll.
     */
    int getUpNestedPreScrollRange() {
        return getTotalScrollRange();
    }

    /**
     * Return the scroll range when scrolling down from a nested pre-scroll.
     */
    int getDownNestedPreScrollRange() {
        if (mDownPreScrollRange != android.support.design.widget.AppBarLayout.INVALID_SCROLL_RANGE) {
            // If we already have a valid value, return it
            return mDownPreScrollRange;
        }
        int range = 0;
        for (int i = getChildCount() - 1; i >= 0; i--) {
            final android.view.View child = getChildAt(i);
            final android.support.design.widget.AppBarLayout.LayoutParams lp = ((android.support.design.widget.AppBarLayout.LayoutParams) (child.getLayoutParams()));
            final int childHeight = child.getMeasuredHeight();
            final int flags = lp.mScrollFlags;
            if ((flags & android.support.design.widget.AppBarLayout.LayoutParams.FLAG_QUICK_RETURN) == android.support.design.widget.AppBarLayout.LayoutParams.FLAG_QUICK_RETURN) {
                // First take the margin into account
                range += lp.topMargin + lp.bottomMargin;
                // The view has the quick return flag combination...
                if ((flags & android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED) != 0) {
                    // If they're set to enter collapsed, use the minimum height
                    range += android.support.v4.view.ViewCompat.getMinimumHeight(child);
                } else
                    if ((flags & android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
                        // Only enter by the amount of the collapsed height
                        range += childHeight - android.support.v4.view.ViewCompat.getMinimumHeight(child);
                    } else {
                        // Else use the full height
                        range += childHeight;
                    }

            } else
                if (range > 0) {
                    // If we've hit an non-quick return scrollable view, and we've already hit a
                    // quick return view, return now
                    break;
                }

        }
        return mDownPreScrollRange = java.lang.Math.max(0, range);
    }

    /**
     * Return the scroll range when scrolling down from a nested scroll.
     */
    int getDownNestedScrollRange() {
        if (mDownScrollRange != android.support.design.widget.AppBarLayout.INVALID_SCROLL_RANGE) {
            // If we already have a valid value, return it
            return mDownScrollRange;
        }
        int range = 0;
        for (int i = 0, z = getChildCount(); i < z; i++) {
            final android.view.View child = getChildAt(i);
            final android.support.design.widget.AppBarLayout.LayoutParams lp = ((android.support.design.widget.AppBarLayout.LayoutParams) (child.getLayoutParams()));
            int childHeight = child.getMeasuredHeight();
            childHeight += lp.topMargin + lp.bottomMargin;
            final int flags = lp.mScrollFlags;
            if ((flags & android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL) != 0) {
                // We're set to scroll so add the child's height
                range += childHeight;
                if ((flags & android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
                    // For a collapsing exit scroll, we to take the collapsed height into account.
                    // We also break the range straight away since later views can't scroll
                    // beneath us
                    range -= android.support.v4.view.ViewCompat.getMinimumHeight(child) + getTopInset();
                    break;
                }
            } else {
                // As soon as a view doesn't have the scroll flag, we end the range calculation.
                // This is because views below can not scroll under a fixed view.
                break;
            }
        }
        return mDownScrollRange = java.lang.Math.max(0, range);
    }

    void dispatchOffsetUpdates(int offset) {
        // Iterate backwards through the list so that most recently added listeners
        // get the first chance to decide
        if (mListeners != null) {
            for (int i = 0, z = mListeners.size(); i < z; i++) {
                final android.support.design.widget.AppBarLayout.OnOffsetChangedListener listener = mListeners.get(i);
                if (listener != null) {
                    listener.onOffsetChanged(this, offset);
                }
            }
        }
    }

    final int getMinimumHeightForVisibleOverlappingContent() {
        final int topInset = getTopInset();
        final int minHeight = android.support.v4.view.ViewCompat.getMinimumHeight(this);
        if (minHeight != 0) {
            // If this layout has a min height, use it (doubled)
            return (minHeight * 2) + topInset;
        }
        // Otherwise, we'll use twice the min height of our last child
        final int childCount = getChildCount();
        final int lastChildMinHeight = (childCount >= 1) ? android.support.v4.view.ViewCompat.getMinimumHeight(getChildAt(childCount - 1)) : 0;
        if (lastChildMinHeight != 0) {
            return (lastChildMinHeight * 2) + topInset;
        }
        // If we reach here then we don't have a min height explicitly set. Instead we'll take a
        // guess at 1/3 of our height being visible
        return getHeight() / 3;
    }

    @java.lang.Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] extraStates = mTmpStatesArray;
        final int[] states = super.onCreateDrawableState(extraSpace + extraStates.length);
        extraStates[0] = (mCollapsible) ? R.attr.state_collapsible : -R.attr.state_collapsible;
        extraStates[1] = (mCollapsible && mCollapsed) ? R.attr.state_collapsed : -R.attr.state_collapsed;
        return android.view.View.mergeDrawableStates(states, extraStates);
    }

    /**
     * Sets whether the AppBarLayout has collapsible children or not.
     *
     * @return true if the collapsible state changed
     */
    private boolean setCollapsibleState(boolean collapsible) {
        if (mCollapsible != collapsible) {
            mCollapsible = collapsible;
            refreshDrawableState();
            return true;
        }
        return false;
    }

    /**
     * Sets whether the AppBarLayout is in a collapsed state or not.
     *
     * @return true if the collapsed state changed
     */
    boolean setCollapsedState(boolean collapsed) {
        if (mCollapsed != collapsed) {
            mCollapsed = collapsed;
            refreshDrawableState();
            return true;
        }
        return false;
    }

    /**
     *
     *
     * @deprecated target elevation is now deprecated. AppBarLayout's elevation is now
    controlled via a {@link android.animation.StateListAnimator}. If a target
    elevation is set, either by this method or the {@code app:elevation} attribute,
    a new state list animator is created which uses the given {@code elevation} value.
     * @unknown ref android.support.design.R.styleable#AppBarLayout_elevation
     */
    @java.lang.Deprecated
    public void setTargetElevation(float elevation) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            android.support.design.widget.ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(this, elevation);
        }
    }

    /**
     *
     *
     * @deprecated target elevation is now deprecated. AppBarLayout's elevation is now
    controlled via a {@link android.animation.StateListAnimator}. This method now
    always returns 0.
     */
    @java.lang.Deprecated
    public float getTargetElevation() {
        return 0;
    }

    int getPendingAction() {
        return mPendingAction;
    }

    void resetPendingAction() {
        mPendingAction = android.support.design.widget.AppBarLayout.PENDING_ACTION_NONE;
    }

    @android.support.annotation.VisibleForTesting
    final int getTopInset() {
        return mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0;
    }

    android.support.v4.view.WindowInsetsCompat onWindowInsetChanged(final android.support.v4.view.WindowInsetsCompat insets) {
        android.support.v4.view.WindowInsetsCompat newInsets = null;
        if (android.support.v4.view.ViewCompat.getFitsSystemWindows(this)) {
            // If we're set to fit system windows, keep the insets
            newInsets = insets;
        }
        // If our insets have changed, keep them and invalidate the scroll ranges...
        if (!android.support.design.widget.ViewUtils.objectEquals(mLastInsets, newInsets)) {
            mLastInsets = newInsets;
            invalidateScrollRanges();
        }
        return insets;
    }

    public static class LayoutParams extends android.widget.LinearLayout.LayoutParams {
        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        @android.support.annotation.IntDef(flag = true, value = { android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL, android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED, android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS, android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED, android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP })
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface ScrollFlags {}

        /**
         * The view will be scroll in direct relation to scroll events. This flag needs to be
         * set for any of the other flags to take effect. If any sibling views
         * before this one do not have this flag, then this value has no effect.
         */
        public static final int SCROLL_FLAG_SCROLL = 0x1;

        /**
         * When exiting (scrolling off screen) the view will be scrolled until it is
         * 'collapsed'. The collapsed height is defined by the view's minimum height.
         *
         * @see ViewCompat#getMinimumHeight(View)
         * @see View#setMinimumHeight(int)
         */
        public static final int SCROLL_FLAG_EXIT_UNTIL_COLLAPSED = 0x2;

        /**
         * When entering (scrolling on screen) the view will scroll on any downwards
         * scroll event, regardless of whether the scrolling view is also scrolling. This
         * is commonly referred to as the 'quick return' pattern.
         */
        public static final int SCROLL_FLAG_ENTER_ALWAYS = 0x4;

        /**
         * An additional flag for 'enterAlways' which modifies the returning view to
         * only initially scroll back to it's collapsed height. Once the scrolling view has
         * reached the end of it's scroll range, the remainder of this view will be scrolled
         * into view. The collapsed height is defined by the view's minimum height.
         *
         * @see ViewCompat#getMinimumHeight(View)
         * @see View#setMinimumHeight(int)
         */
        public static final int SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED = 0x8;

        /**
         * Upon a scroll ending, if the view is only partially visible then it will be snapped
         * and scrolled to it's closest edge. For example, if the view only has it's bottom 25%
         * displayed, it will be scrolled off screen completely. Conversely, if it's bottom 75%
         * is visible then it will be scrolled fully into view.
         */
        public static final int SCROLL_FLAG_SNAP = 0x10;

        /**
         * Internal flags which allows quick checking features
         */
        static final int FLAG_QUICK_RETURN = android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS;

        static final int FLAG_SNAP = android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP;

        static final int COLLAPSIBLE_FLAGS = android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED;

        int mScrollFlags = android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;

        android.view.animation.Interpolator mScrollInterpolator;

        public LayoutParams(android.content.Context c, android.util.AttributeSet attrs) {
            super(c, attrs);
            android.content.res.TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.AppBarLayout_Layout);
            mScrollFlags = a.getInt(R.styleable.AppBarLayout_Layout_layout_scrollFlags, 0);
            if (a.hasValue(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator)) {
                int resId = a.getResourceId(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator, 0);
                mScrollInterpolator = android.view.animation.AnimationUtils.loadInterpolator(c, resId);
            }
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, float weight) {
            super(width, height, weight);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(android.view.ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(android.widget.LinearLayout.LayoutParams source) {
            super(source);
        }

        public LayoutParams(android.support.design.widget.AppBarLayout.LayoutParams source) {
            super(source);
            mScrollFlags = source.mScrollFlags;
            mScrollInterpolator = source.mScrollInterpolator;
        }

        /**
         * Set the scrolling flags.
         *
         * @param flags
         * 		bitwise int of {@link #SCROLL_FLAG_SCROLL},
         * 		{@link #SCROLL_FLAG_EXIT_UNTIL_COLLAPSED}, {@link #SCROLL_FLAG_ENTER_ALWAYS},
         * 		{@link #SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED} and {@link #SCROLL_FLAG_SNAP}.
         * @see #getScrollFlags()
         * @unknown ref android.support.design.R.styleable#AppBarLayout_Layout_layout_scrollFlags
         */
        public void setScrollFlags(@android.support.design.widget.AppBarLayout.LayoutParams.ScrollFlags
        int flags) {
            mScrollFlags = flags;
        }

        /**
         * Returns the scrolling flags.
         *
         * @see #setScrollFlags(int)
         * @unknown ref android.support.design.R.styleable#AppBarLayout_Layout_layout_scrollFlags
         */
        @android.support.design.widget.AppBarLayout.LayoutParams.ScrollFlags
        public int getScrollFlags() {
            return mScrollFlags;
        }

        /**
         * Set the interpolator to when scrolling the view associated with this
         * {@link LayoutParams}.
         *
         * @param interpolator
         * 		the interpolator to use, or null to use normal 1-to-1 scrolling.
         * @unknown ref android.support.design.R.styleable#AppBarLayout_Layout_layout_scrollInterpolator
         * @see #getScrollInterpolator()
         */
        public void setScrollInterpolator(android.view.animation.Interpolator interpolator) {
            mScrollInterpolator = interpolator;
        }

        /**
         * Returns the {@link Interpolator} being used for scrolling the view associated with this
         * {@link LayoutParams}. Null indicates 'normal' 1-to-1 scrolling.
         *
         * @unknown ref android.support.design.R.styleable#AppBarLayout_Layout_layout_scrollInterpolator
         * @see #setScrollInterpolator(Interpolator)
         */
        public android.view.animation.Interpolator getScrollInterpolator() {
            return mScrollInterpolator;
        }

        /**
         * Returns true if the scroll flags are compatible for 'collapsing'
         */
        boolean isCollapsible() {
            return ((mScrollFlags & android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL) == android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL) && ((mScrollFlags & android.support.design.widget.AppBarLayout.LayoutParams.COLLAPSIBLE_FLAGS) != 0);
        }
    }

    /**
     * The default {@link Behavior} for {@link AppBarLayout}. Implements the necessary nested
     * scroll handling with offsetting.
     */
    public static class Behavior extends android.support.design.widget.HeaderBehavior<android.support.design.widget.AppBarLayout> {
        private static final int MAX_OFFSET_ANIMATION_DURATION = 600;// ms


        private static final int INVALID_POSITION = -1;

        /**
         * Callback to allow control over any {@link AppBarLayout} dragging.
         */
        public static abstract class DragCallback {
            /**
             * Allows control over whether the given {@link AppBarLayout} can be dragged or not.
             *
             * <p>Dragging is defined as a direct touch on the AppBarLayout with movement. This
             * call does not affect any nested scrolling.</p>
             *
             * @return true if we are in a position to scroll the AppBarLayout via a drag, false
            if not.
             */
            public abstract boolean canDrag(@android.support.annotation.NonNull
            android.support.design.widget.AppBarLayout appBarLayout);
        }

        private int mOffsetDelta;

        private boolean mSkipNestedPreScroll;

        private boolean mWasNestedFlung;

        private android.support.design.widget.ValueAnimatorCompat mOffsetAnimator;

        private int mOffsetToChildIndexOnLayout = android.support.design.widget.AppBarLayout.Behavior.INVALID_POSITION;

        private boolean mOffsetToChildIndexOnLayoutIsMinHeight;

        private float mOffsetToChildIndexOnLayoutPerc;

        private java.lang.ref.WeakReference<android.view.View> mLastNestedScrollingChildRef;

        private android.support.design.widget.AppBarLayout.Behavior.DragCallback mOnDragCallback;

        public Behavior() {
        }

        public Behavior(android.content.Context context, android.util.AttributeSet attrs) {
            super(context, attrs);
        }

        @java.lang.Override
        public boolean onStartNestedScroll(android.support.design.widget.CoordinatorLayout parent, android.support.design.widget.AppBarLayout child, android.view.View directTargetChild, android.view.View target, int nestedScrollAxes) {
            // Return true if we're nested scrolling vertically, and we have scrollable children
            // and the scrolling view is big enough to scroll
            final boolean started = (((nestedScrollAxes & android.support.v4.view.ViewCompat.SCROLL_AXIS_VERTICAL) != 0) && child.hasScrollableChildren()) && ((parent.getHeight() - directTargetChild.getHeight()) <= child.getHeight());
            if (started && (mOffsetAnimator != null)) {
                // Cancel any offset animation
                mOffsetAnimator.cancel();
            }
            // A new nested scroll has started so clear out the previous ref
            mLastNestedScrollingChildRef = null;
            return started;
        }

        @java.lang.Override
        public void onNestedPreScroll(android.support.design.widget.CoordinatorLayout coordinatorLayout, android.support.design.widget.AppBarLayout child, android.view.View target, int dx, int dy, int[] consumed) {
            if ((dy != 0) && (!mSkipNestedPreScroll)) {
                int min;
                int max;
                if (dy < 0) {
                    // We're scrolling down
                    min = -child.getTotalScrollRange();
                    max = min + child.getDownNestedPreScrollRange();
                } else {
                    // We're scrolling up
                    min = -child.getUpNestedPreScrollRange();
                    max = 0;
                }
                consumed[1] = scroll(coordinatorLayout, child, dy, min, max);
            }
        }

        @java.lang.Override
        public void onNestedScroll(android.support.design.widget.CoordinatorLayout coordinatorLayout, android.support.design.widget.AppBarLayout child, android.view.View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            if (dyUnconsumed < 0) {
                // If the scrolling view is scrolling down but not consuming, it's probably be at
                // the top of it's content
                scroll(coordinatorLayout, child, dyUnconsumed, -child.getDownNestedScrollRange(), 0);
                // Set the expanding flag so that onNestedPreScroll doesn't handle any events
                mSkipNestedPreScroll = true;
            } else {
                // As we're no longer handling nested scrolls, reset the skip flag
                mSkipNestedPreScroll = false;
            }
        }

        @java.lang.Override
        public void onStopNestedScroll(android.support.design.widget.CoordinatorLayout coordinatorLayout, android.support.design.widget.AppBarLayout abl, android.view.View target) {
            if (!mWasNestedFlung) {
                // If we haven't been flung then let's see if the current view has been set to snap
                snapToChildIfNeeded(coordinatorLayout, abl);
            }
            // Reset the flags
            mSkipNestedPreScroll = false;
            mWasNestedFlung = false;
            // Keep a reference to the previous nested scrolling child
            mLastNestedScrollingChildRef = new java.lang.ref.WeakReference<>(target);
        }

        @java.lang.Override
        public boolean onNestedFling(final android.support.design.widget.CoordinatorLayout coordinatorLayout, final android.support.design.widget.AppBarLayout child, android.view.View target, float velocityX, float velocityY, boolean consumed) {
            boolean flung = false;
            if (!consumed) {
                // It has been consumed so let's fling ourselves
                flung = fling(coordinatorLayout, child, -child.getTotalScrollRange(), 0, -velocityY);
            } else {
                // If we're scrolling up and the child also consumed the fling. We'll fake scroll
                // up to our 'collapsed' offset
                if (velocityY < 0) {
                    // We're scrolling down
                    final int targetScroll = (-child.getTotalScrollRange()) + child.getDownNestedPreScrollRange();
                    if (getTopBottomOffsetForScrollingSibling() < targetScroll) {
                        // If we're currently not expanded more than the target scroll, we'll
                        // animate a fling
                        animateOffsetTo(coordinatorLayout, child, targetScroll, velocityY);
                        flung = true;
                    }
                } else {
                    // We're scrolling up
                    final int targetScroll = -child.getUpNestedPreScrollRange();
                    if (getTopBottomOffsetForScrollingSibling() > targetScroll) {
                        // If we're currently not expanded less than the target scroll, we'll
                        // animate a fling
                        animateOffsetTo(coordinatorLayout, child, targetScroll, velocityY);
                        flung = true;
                    }
                }
            }
            mWasNestedFlung = flung;
            return flung;
        }

        /**
         * Set a callback to control any {@link AppBarLayout} dragging.
         *
         * @param callback
         * 		the callback to use, or {@code null} to use the default behavior.
         */
        public void setDragCallback(@android.support.annotation.Nullable
        android.support.design.widget.AppBarLayout.Behavior.DragCallback callback) {
            mOnDragCallback = callback;
        }

        private void animateOffsetTo(final android.support.design.widget.CoordinatorLayout coordinatorLayout, final android.support.design.widget.AppBarLayout child, final int offset, float velocity) {
            final int distance = java.lang.Math.abs(getTopBottomOffsetForScrollingSibling() - offset);
            final int duration;
            velocity = java.lang.Math.abs(velocity);
            if (velocity > 0) {
                duration = 3 * java.lang.Math.round(1000 * (distance / velocity));
            } else {
                final float distanceRatio = ((float) (distance)) / child.getHeight();
                duration = ((int) ((distanceRatio + 1) * 150));
            }
            animateOffsetWithDuration(coordinatorLayout, child, offset, duration);
        }

        private void animateOffsetWithDuration(final android.support.design.widget.CoordinatorLayout coordinatorLayout, final android.support.design.widget.AppBarLayout child, final int offset, final int duration) {
            final int currentOffset = getTopBottomOffsetForScrollingSibling();
            if (currentOffset == offset) {
                if ((mOffsetAnimator != null) && mOffsetAnimator.isRunning()) {
                    mOffsetAnimator.cancel();
                }
                return;
            }
            if (mOffsetAnimator == null) {
                mOffsetAnimator = android.support.design.widget.ViewUtils.createAnimator();
                mOffsetAnimator.setInterpolator(android.support.design.widget.AnimationUtils.DECELERATE_INTERPOLATOR);
                mOffsetAnimator.addUpdateListener(new android.support.design.widget.ValueAnimatorCompat.AnimatorUpdateListener() {
                    @java.lang.Override
                    public void onAnimationUpdate(android.support.design.widget.ValueAnimatorCompat animator) {
                        setHeaderTopBottomOffset(coordinatorLayout, child, animator.getAnimatedIntValue());
                    }
                });
            } else {
                mOffsetAnimator.cancel();
            }
            mOffsetAnimator.setDuration(java.lang.Math.min(duration, android.support.design.widget.AppBarLayout.Behavior.MAX_OFFSET_ANIMATION_DURATION));
            mOffsetAnimator.setIntValues(currentOffset, offset);
            mOffsetAnimator.start();
        }

        private int getChildIndexOnOffset(android.support.design.widget.AppBarLayout abl, final int offset) {
            for (int i = 0, count = abl.getChildCount(); i < count; i++) {
                android.view.View child = abl.getChildAt(i);
                if ((child.getTop() <= (-offset)) && (child.getBottom() >= (-offset))) {
                    return i;
                }
            }
            return -1;
        }

        private void snapToChildIfNeeded(android.support.design.widget.CoordinatorLayout coordinatorLayout, android.support.design.widget.AppBarLayout abl) {
            final int offset = getTopBottomOffsetForScrollingSibling();
            final int offsetChildIndex = getChildIndexOnOffset(abl, offset);
            if (offsetChildIndex >= 0) {
                final android.view.View offsetChild = abl.getChildAt(offsetChildIndex);
                final android.support.design.widget.AppBarLayout.LayoutParams lp = ((android.support.design.widget.AppBarLayout.LayoutParams) (offsetChild.getLayoutParams()));
                final int flags = lp.getScrollFlags();
                if ((flags & android.support.design.widget.AppBarLayout.LayoutParams.FLAG_SNAP) == android.support.design.widget.AppBarLayout.LayoutParams.FLAG_SNAP) {
                    // We're set the snap, so animate the offset to the nearest edge
                    int snapTop = -offsetChild.getTop();
                    int snapBottom = -offsetChild.getBottom();
                    if (offsetChildIndex == (abl.getChildCount() - 1)) {
                        // If this is the last child, we need to take the top inset into account
                        snapBottom += abl.getTopInset();
                    }
                    if (android.support.design.widget.AppBarLayout.Behavior.checkFlag(flags, android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED)) {
                        // If the view is set only exit until it is collapsed, we'll abide by that
                        snapBottom += android.support.v4.view.ViewCompat.getMinimumHeight(offsetChild);
                    } else
                        if (android.support.design.widget.AppBarLayout.Behavior.checkFlag(flags, android.support.design.widget.AppBarLayout.LayoutParams.FLAG_QUICK_RETURN | android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS)) {
                            // If it's set to always enter collapsed, it actually has two states. We
                            // select the state and then snap within the state
                            final int seam = snapBottom + android.support.v4.view.ViewCompat.getMinimumHeight(offsetChild);
                            if (offset < seam) {
                                snapTop = seam;
                            } else {
                                snapBottom = seam;
                            }
                        }

                    final int newOffset = (offset < ((snapBottom + snapTop) / 2)) ? snapBottom : snapTop;
                    animateOffsetTo(coordinatorLayout, abl, android.support.design.widget.MathUtils.constrain(newOffset, -abl.getTotalScrollRange(), 0), 0);
                }
            }
        }

        private static boolean checkFlag(final int flags, final int check) {
            return (flags & check) == check;
        }

        @java.lang.Override
        public boolean onMeasureChild(android.support.design.widget.CoordinatorLayout parent, android.support.design.widget.AppBarLayout child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
            if (lp.height == android.support.design.widget.CoordinatorLayout.LayoutParams.WRAP_CONTENT) {
                // If the view is set to wrap on it's height, CoordinatorLayout by default will
                // cap the view at the CoL's height. Since the AppBarLayout can scroll, this isn't
                // what we actually want, so we measure it ourselves with an unspecified spec to
                // allow the child to be larger than it's parent
                parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED), heightUsed);
                return true;
            }
            // Let the parent handle it as normal
            return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
        }

        @java.lang.Override
        public boolean onLayoutChild(android.support.design.widget.CoordinatorLayout parent, android.support.design.widget.AppBarLayout abl, int layoutDirection) {
            boolean handled = super.onLayoutChild(parent, abl, layoutDirection);
            final int pendingAction = abl.getPendingAction();
            if (pendingAction != android.support.design.widget.AppBarLayout.PENDING_ACTION_NONE) {
                final boolean animate = (pendingAction & android.support.design.widget.AppBarLayout.PENDING_ACTION_ANIMATE_ENABLED) != 0;
                if ((pendingAction & android.support.design.widget.AppBarLayout.PENDING_ACTION_COLLAPSED) != 0) {
                    final int offset = -abl.getUpNestedPreScrollRange();
                    if (animate) {
                        animateOffsetTo(parent, abl, offset, 0);
                    } else {
                        setHeaderTopBottomOffset(parent, abl, offset);
                    }
                } else
                    if ((pendingAction & android.support.design.widget.AppBarLayout.PENDING_ACTION_EXPANDED) != 0) {
                        if (animate) {
                            animateOffsetTo(parent, abl, 0, 0);
                        } else {
                            setHeaderTopBottomOffset(parent, abl, 0);
                        }
                    }

            } else
                if (mOffsetToChildIndexOnLayout >= 0) {
                    android.view.View child = abl.getChildAt(mOffsetToChildIndexOnLayout);
                    int offset = -child.getBottom();
                    if (mOffsetToChildIndexOnLayoutIsMinHeight) {
                        offset += android.support.v4.view.ViewCompat.getMinimumHeight(child);
                    } else {
                        offset += java.lang.Math.round(child.getHeight() * mOffsetToChildIndexOnLayoutPerc);
                    }
                    setTopAndBottomOffset(offset);
                }

            // Finally reset any pending states
            abl.resetPendingAction();
            mOffsetToChildIndexOnLayout = android.support.design.widget.AppBarLayout.Behavior.INVALID_POSITION;
            // We may have changed size, so let's constrain the top and bottom offset correctly,
            // just in case we're out of the bounds
            setTopAndBottomOffset(android.support.design.widget.MathUtils.constrain(getTopAndBottomOffset(), -abl.getTotalScrollRange(), 0));
            // Make sure we dispatch the offset update
            abl.dispatchOffsetUpdates(getTopAndBottomOffset());
            return handled;
        }

        @java.lang.Override
        boolean canDragView(android.support.design.widget.AppBarLayout view) {
            if (mOnDragCallback != null) {
                // If there is a drag callback set, it's in control
                return mOnDragCallback.canDrag(view);
            }
            // Else we'll use the default behaviour of seeing if it can scroll down
            if (mLastNestedScrollingChildRef != null) {
                // If we have a reference to a scrolling view, check it
                final android.view.View scrollingView = mLastNestedScrollingChildRef.get();
                return ((scrollingView != null) && scrollingView.isShown()) && (!android.support.v4.view.ViewCompat.canScrollVertically(scrollingView, -1));
            } else {
                // Otherwise we assume that the scrolling view hasn't been scrolled and can drag.
                return true;
            }
        }

        @java.lang.Override
        void onFlingFinished(android.support.design.widget.CoordinatorLayout parent, android.support.design.widget.AppBarLayout layout) {
            // At the end of a manual fling, check to see if we need to snap to the edge-child
            snapToChildIfNeeded(parent, layout);
        }

        @java.lang.Override
        int getMaxDragOffset(android.support.design.widget.AppBarLayout view) {
            return -view.getDownNestedScrollRange();
        }

        @java.lang.Override
        int getScrollRangeForDragFling(android.support.design.widget.AppBarLayout view) {
            return view.getTotalScrollRange();
        }

        @java.lang.Override
        int setHeaderTopBottomOffset(android.support.design.widget.CoordinatorLayout coordinatorLayout, android.support.design.widget.AppBarLayout appBarLayout, int newOffset, int minOffset, int maxOffset) {
            final int curOffset = getTopBottomOffsetForScrollingSibling();
            int consumed = 0;
            if (((minOffset != 0) && (curOffset >= minOffset)) && (curOffset <= maxOffset)) {
                // If we have some scrolling range, and we're currently within the min and max
                // offsets, calculate a new offset
                newOffset = android.support.design.widget.MathUtils.constrain(newOffset, minOffset, maxOffset);
                if (curOffset != newOffset) {
                    final int interpolatedOffset = (appBarLayout.hasChildWithInterpolator()) ? interpolateOffset(appBarLayout, newOffset) : newOffset;
                    final boolean offsetChanged = setTopAndBottomOffset(interpolatedOffset);
                    // Update how much dy we have consumed
                    consumed = curOffset - newOffset;
                    // Update the stored sibling offset
                    mOffsetDelta = newOffset - interpolatedOffset;
                    if ((!offsetChanged) && appBarLayout.hasChildWithInterpolator()) {
                        // If the offset hasn't changed and we're using an interpolated scroll
                        // then we need to keep any dependent views updated. CoL will do this for
                        // us when we move, but we need to do it manually when we don't (as an
                        // interpolated scroll may finish early).
                        coordinatorLayout.dispatchDependentViewsChanged(appBarLayout);
                    }
                    // Dispatch the updates to any listeners
                    appBarLayout.dispatchOffsetUpdates(getTopAndBottomOffset());
                    // Update the AppBarLayout's drawable state (for any elevation changes)
                    updateAppBarLayoutDrawableState(coordinatorLayout, appBarLayout, newOffset, newOffset < curOffset ? -1 : 1);
                }
            } else {
                // Reset the offset delta
                mOffsetDelta = 0;
            }
            return consumed;
        }

        @android.support.annotation.VisibleForTesting
        boolean isOffsetAnimatorRunning() {
            return (mOffsetAnimator != null) && mOffsetAnimator.isRunning();
        }

        private int interpolateOffset(android.support.design.widget.AppBarLayout layout, final int offset) {
            final int absOffset = java.lang.Math.abs(offset);
            for (int i = 0, z = layout.getChildCount(); i < z; i++) {
                final android.view.View child = layout.getChildAt(i);
                final android.support.design.widget.AppBarLayout.LayoutParams childLp = ((android.support.design.widget.AppBarLayout.LayoutParams) (child.getLayoutParams()));
                final android.view.animation.Interpolator interpolator = childLp.getScrollInterpolator();
                if ((absOffset >= child.getTop()) && (absOffset <= child.getBottom())) {
                    if (interpolator != null) {
                        int childScrollableHeight = 0;
                        final int flags = childLp.getScrollFlags();
                        if ((flags & android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL) != 0) {
                            // We're set to scroll so add the child's height plus margin
                            childScrollableHeight += (child.getHeight() + childLp.topMargin) + childLp.bottomMargin;
                            if ((flags & android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
                                // For a collapsing scroll, we to take the collapsed height
                                // into account.
                                childScrollableHeight -= android.support.v4.view.ViewCompat.getMinimumHeight(child);
                            }
                        }
                        if (android.support.v4.view.ViewCompat.getFitsSystemWindows(child)) {
                            childScrollableHeight -= layout.getTopInset();
                        }
                        if (childScrollableHeight > 0) {
                            final int offsetForView = absOffset - child.getTop();
                            final int interpolatedDiff = java.lang.Math.round(childScrollableHeight * interpolator.getInterpolation(offsetForView / ((float) (childScrollableHeight))));
                            return java.lang.Integer.signum(offset) * (child.getTop() + interpolatedDiff);
                        }
                    }
                    // If we get to here then the view on the offset isn't suitable for interpolated
                    // scrolling. So break out of the loop
                    break;
                }
            }
            return offset;
        }

        private void updateAppBarLayoutDrawableState(final android.support.design.widget.CoordinatorLayout parent, final android.support.design.widget.AppBarLayout layout, final int offset, final int direction) {
            final android.view.View child = android.support.design.widget.AppBarLayout.Behavior.getAppBarChildOnOffset(layout, offset);
            if (child != null) {
                final android.support.design.widget.AppBarLayout.LayoutParams childLp = ((android.support.design.widget.AppBarLayout.LayoutParams) (child.getLayoutParams()));
                final int flags = childLp.getScrollFlags();
                boolean collapsed = false;
                if ((flags & android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL) != 0) {
                    final int minHeight = android.support.v4.view.ViewCompat.getMinimumHeight(child);
                    if ((direction > 0) && ((flags & (android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED)) != 0)) {
                        // We're set to enter always collapsed so we are only collapsed when
                        // being scrolled down, and in a collapsed offset
                        collapsed = (-offset) >= ((child.getBottom() - minHeight) - layout.getTopInset());
                    } else
                        if ((flags & android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
                            // We're set to exit until collapsed, so any offset which results in
                            // the minimum height (or less) being shown is collapsed
                            collapsed = (-offset) >= ((child.getBottom() - minHeight) - layout.getTopInset());
                        }

                }
                final boolean changed = layout.setCollapsedState(collapsed);
                if ((changed && (android.os.Build.VERSION.SDK_INT >= 11)) && shouldJumpElevationState(parent, layout)) {
                    // If the collapsed state changed, we may need to
                    // jump to the current state if we have an overlapping view
                    layout.jumpDrawablesToCurrentState();
                }
            }
        }

        private boolean shouldJumpElevationState(android.support.design.widget.CoordinatorLayout parent, android.support.design.widget.AppBarLayout layout) {
            // We should jump the elevated state if we have a dependent scrolling view which has
            // an overlapping top (i.e. overlaps us)
            final java.util.List<android.view.View> dependencies = parent.getDependents(layout);
            for (int i = 0, size = dependencies.size(); i < size; i++) {
                final android.view.View dependency = dependencies.get(i);
                final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (dependency.getLayoutParams()));
                final android.support.design.widget.CoordinatorLayout.Behavior behavior = lp.getBehavior();
                if (behavior instanceof android.support.design.widget.AppBarLayout.ScrollingViewBehavior) {
                    return ((android.support.design.widget.AppBarLayout.ScrollingViewBehavior) (behavior)).getOverlayTop() != 0;
                }
            }
            return false;
        }

        private static android.view.View getAppBarChildOnOffset(final android.support.design.widget.AppBarLayout layout, final int offset) {
            final int absOffset = java.lang.Math.abs(offset);
            for (int i = 0, z = layout.getChildCount(); i < z; i++) {
                final android.view.View child = layout.getChildAt(i);
                if ((absOffset >= child.getTop()) && (absOffset <= child.getBottom())) {
                    return child;
                }
            }
            return null;
        }

        @java.lang.Override
        int getTopBottomOffsetForScrollingSibling() {
            return getTopAndBottomOffset() + mOffsetDelta;
        }

        @java.lang.Override
        public android.os.Parcelable onSaveInstanceState(android.support.design.widget.CoordinatorLayout parent, android.support.design.widget.AppBarLayout abl) {
            final android.os.Parcelable superState = super.onSaveInstanceState(parent, abl);
            final int offset = getTopAndBottomOffset();
            // Try and find the first visible child...
            for (int i = 0, count = abl.getChildCount(); i < count; i++) {
                android.view.View child = abl.getChildAt(i);
                final int visBottom = child.getBottom() + offset;
                if (((child.getTop() + offset) <= 0) && (visBottom >= 0)) {
                    final android.support.design.widget.AppBarLayout.Behavior.SavedState ss = new android.support.design.widget.AppBarLayout.Behavior.SavedState(superState);
                    ss.firstVisibleChildIndex = i;
                    ss.firstVisibleChildAtMinimumHeight = visBottom == (android.support.v4.view.ViewCompat.getMinimumHeight(child) + abl.getTopInset());
                    ss.firstVisibleChildPercentageShown = visBottom / ((float) (child.getHeight()));
                    return ss;
                }
            }
            // Else we'll just return the super state
            return superState;
        }

        @java.lang.Override
        public void onRestoreInstanceState(android.support.design.widget.CoordinatorLayout parent, android.support.design.widget.AppBarLayout appBarLayout, android.os.Parcelable state) {
            if (state instanceof android.support.design.widget.AppBarLayout.Behavior.SavedState) {
                final android.support.design.widget.AppBarLayout.Behavior.SavedState ss = ((android.support.design.widget.AppBarLayout.Behavior.SavedState) (state));
                super.onRestoreInstanceState(parent, appBarLayout, ss.getSuperState());
                mOffsetToChildIndexOnLayout = ss.firstVisibleChildIndex;
                mOffsetToChildIndexOnLayoutPerc = ss.firstVisibleChildPercentageShown;
                mOffsetToChildIndexOnLayoutIsMinHeight = ss.firstVisibleChildAtMinimumHeight;
            } else {
                super.onRestoreInstanceState(parent, appBarLayout, state);
                mOffsetToChildIndexOnLayout = android.support.design.widget.AppBarLayout.Behavior.INVALID_POSITION;
            }
        }

        protected static class SavedState extends android.support.v4.view.AbsSavedState {
            int firstVisibleChildIndex;

            float firstVisibleChildPercentageShown;

            boolean firstVisibleChildAtMinimumHeight;

            public SavedState(android.os.Parcel source, java.lang.ClassLoader loader) {
                super(source, loader);
                firstVisibleChildIndex = source.readInt();
                firstVisibleChildPercentageShown = source.readFloat();
                firstVisibleChildAtMinimumHeight = source.readByte() != 0;
            }

            public SavedState(android.os.Parcelable superState) {
                super(superState);
            }

            @java.lang.Override
            public void writeToParcel(android.os.Parcel dest, int flags) {
                super.writeToParcel(dest, flags);
                dest.writeInt(firstVisibleChildIndex);
                dest.writeFloat(firstVisibleChildPercentageShown);
                dest.writeByte(((byte) (firstVisibleChildAtMinimumHeight ? 1 : 0)));
            }

            public static final android.os.Parcelable.Creator<android.support.design.widget.AppBarLayout.Behavior.SavedState> CREATOR = android.support.v4.os.ParcelableCompat.newCreator(new android.support.v4.os.ParcelableCompatCreatorCallbacks<android.support.design.widget.AppBarLayout.Behavior.SavedState>() {
                @java.lang.Override
                public android.support.design.widget.AppBarLayout.Behavior.SavedState createFromParcel(android.os.Parcel source, java.lang.ClassLoader loader) {
                    return new android.support.design.widget.AppBarLayout.Behavior.SavedState(source, loader);
                }

                @java.lang.Override
                public android.support.design.widget.AppBarLayout.Behavior.SavedState[] newArray(int size) {
                    return new android.support.design.widget.AppBarLayout.Behavior.SavedState[size];
                }
            });
        }
    }

    /**
     * Behavior which should be used by {@link View}s which can scroll vertically and support
     * nested scrolling to automatically scroll any {@link AppBarLayout} siblings.
     */
    public static class ScrollingViewBehavior extends android.support.design.widget.HeaderScrollingViewBehavior {
        public ScrollingViewBehavior() {
        }

        public ScrollingViewBehavior(android.content.Context context, android.util.AttributeSet attrs) {
            super(context, attrs);
            final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollingViewBehavior_Layout);
            setOverlayTop(a.getDimensionPixelSize(R.styleable.ScrollingViewBehavior_Layout_behavior_overlapTop, 0));
            a.recycle();
        }

        @java.lang.Override
        public boolean layoutDependsOn(android.support.design.widget.CoordinatorLayout parent, android.view.View child, android.view.View dependency) {
            // We depend on any AppBarLayouts
            return dependency instanceof android.support.design.widget.AppBarLayout;
        }

        @java.lang.Override
        public boolean onDependentViewChanged(android.support.design.widget.CoordinatorLayout parent, android.view.View child, android.view.View dependency) {
            offsetChildAsNeeded(parent, child, dependency);
            return false;
        }

        @java.lang.Override
        public boolean onRequestChildRectangleOnScreen(android.support.design.widget.CoordinatorLayout parent, android.view.View child, android.graphics.Rect rectangle, boolean immediate) {
            final android.support.design.widget.AppBarLayout header = findFirstDependency(parent.getDependencies(child));
            if (header != null) {
                // Offset the rect by the child's left/top
                rectangle.offset(child.getLeft(), child.getTop());
                final android.graphics.Rect parentRect = mTempRect1;
                parentRect.set(0, 0, parent.getWidth(), parent.getHeight());
                if (!parentRect.contains(rectangle)) {
                    // If the rectangle can not be fully seen the visible bounds, collapse
                    // the AppBarLayout
                    header.setExpanded(false, !immediate);
                    return true;
                }
            }
            return false;
        }

        private void offsetChildAsNeeded(android.support.design.widget.CoordinatorLayout parent, android.view.View child, android.view.View dependency) {
            final android.support.design.widget.CoordinatorLayout.Behavior behavior = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (dependency.getLayoutParams())).getBehavior();
            if (behavior instanceof android.support.design.widget.AppBarLayout.Behavior) {
                // Offset the child, pinning it to the bottom the header-dependency, maintaining
                // any vertical gap and overlap
                final android.support.design.widget.AppBarLayout.Behavior ablBehavior = ((android.support.design.widget.AppBarLayout.Behavior) (behavior));
                android.support.v4.view.ViewCompat.offsetTopAndBottom(child, (((dependency.getBottom() - child.getTop()) + ablBehavior.mOffsetDelta) + getVerticalLayoutGap()) - getOverlapPixelsForOffset(dependency));
            }
        }

        @java.lang.Override
        float getOverlapRatioForOffset(final android.view.View header) {
            if (header instanceof android.support.design.widget.AppBarLayout) {
                final android.support.design.widget.AppBarLayout abl = ((android.support.design.widget.AppBarLayout) (header));
                final int totalScrollRange = abl.getTotalScrollRange();
                final int preScrollDown = abl.getDownNestedPreScrollRange();
                final int offset = android.support.design.widget.AppBarLayout.ScrollingViewBehavior.getAppBarLayoutOffset(abl);
                if ((preScrollDown != 0) && ((totalScrollRange + offset) <= preScrollDown)) {
                    // If we're in a pre-scroll down. Don't use the offset at all.
                    return 0;
                } else {
                    final int availScrollRange = totalScrollRange - preScrollDown;
                    if (availScrollRange != 0) {
                        // Else we'll use a interpolated ratio of the overlap, depending on offset
                        return 1.0F + (offset / ((float) (availScrollRange)));
                    }
                }
            }
            return 0.0F;
        }

        private static int getAppBarLayoutOffset(android.support.design.widget.AppBarLayout abl) {
            final android.support.design.widget.CoordinatorLayout.Behavior behavior = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (abl.getLayoutParams())).getBehavior();
            if (behavior instanceof android.support.design.widget.AppBarLayout.Behavior) {
                return ((android.support.design.widget.AppBarLayout.Behavior) (behavior)).getTopBottomOffsetForScrollingSibling();
            }
            return 0;
        }

        @java.lang.Override
        android.support.design.widget.AppBarLayout findFirstDependency(java.util.List<android.view.View> views) {
            for (int i = 0, z = views.size(); i < z; i++) {
                android.view.View view = views.get(i);
                if (view instanceof android.support.design.widget.AppBarLayout) {
                    return ((android.support.design.widget.AppBarLayout) (view));
                }
            }
            return null;
        }

        @java.lang.Override
        int getScrollRange(android.view.View v) {
            if (v instanceof android.support.design.widget.AppBarLayout) {
                return ((android.support.design.widget.AppBarLayout) (v)).getTotalScrollRange();
            } else {
                return super.getScrollRange(v);
            }
        }
    }
}

