/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.support.v4.widget;


/**
 * SlidingPaneLayout provides a horizontal, multi-pane layout for use at the top level
 * of a UI. A left (or first) pane is treated as a content list or browser, subordinate to a
 * primary detail view for displaying content.
 *
 * <p>Child views may overlap if their combined width exceeds the available width
 * in the SlidingPaneLayout. When this occurs the user may slide the topmost view out of the way
 * by dragging it, or by navigating in the direction of the overlapped view using a keyboard.
 * If the content of the dragged child view is itself horizontally scrollable, the user may
 * grab it by the very edge.</p>
 *
 * <p>Thanks to this sliding behavior, SlidingPaneLayout may be suitable for creating layouts
 * that can smoothly adapt across many different screen sizes, expanding out fully on larger
 * screens and collapsing on smaller screens.</p>
 *
 * <p>SlidingPaneLayout is distinct from a navigation drawer as described in the design
 * guide and should not be used in the same scenarios. SlidingPaneLayout should be thought
 * of only as a way to allow a two-pane layout normally used on larger screens to adapt to smaller
 * screens in a natural way. The interaction patterns expressed by SlidingPaneLayout imply
 * a physicality and direct information hierarchy between panes that does not necessarily exist
 * in a scenario where a navigation drawer should be used instead.</p>
 *
 * <p>Appropriate uses of SlidingPaneLayout include pairings of panes such as a contact list and
 * subordinate interactions with those contacts, or an email thread list with the content pane
 * displaying the contents of the selected thread. Inappropriate uses of SlidingPaneLayout include
 * switching between disparate functions of your app, such as jumping from a social stream view
 * to a view of your personal profile - cases such as this should use the navigation drawer
 * pattern instead. ({@link DrawerLayout DrawerLayout} implements this pattern.)</p>
 *
 * <p>Like {@link android.widget.LinearLayout LinearLayout}, SlidingPaneLayout supports
 * the use of the layout parameter <code>layout_weight</code> on child views to determine
 * how to divide leftover space after measurement is complete. It is only relevant for width.
 * When views do not overlap weight behaves as it does in a LinearLayout.</p>
 *
 * <p>When views do overlap, weight on a slideable pane indicates that the pane should be
 * sized to fill all available space in the closed state. Weight on a pane that becomes covered
 * indicates that the pane should be sized to fill all available space except a small minimum strip
 * that the user may use to grab the slideable view and pull it back over into a closed state.</p>
 */
public class SlidingPaneLayout extends android.view.ViewGroup {
    private static final java.lang.String TAG = "SlidingPaneLayout";

    /**
     * Default size of the overhang for a pane in the open state.
     * At least this much of a sliding pane will remain visible.
     * This indicates that there is more content available and provides
     * a "physical" edge to grab to pull it closed.
     */
    private static final int DEFAULT_OVERHANG_SIZE = 32;// dp;


    /**
     * If no fade color is given by default it will fade to 80% gray.
     */
    private static final int DEFAULT_FADE_COLOR = 0xcccccccc;

    /**
     * The fade color used for the sliding panel. 0 = no fading.
     */
    private int mSliderFadeColor = android.support.v4.widget.SlidingPaneLayout.DEFAULT_FADE_COLOR;

    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400;// dips per second


    /**
     * The fade color used for the panel covered by the slider. 0 = no fading.
     */
    private int mCoveredFadeColor;

    /**
     * Drawable used to draw the shadow between panes by default.
     */
    private android.graphics.drawable.Drawable mShadowDrawableLeft;

    /**
     * Drawable used to draw the shadow between panes to support RTL (right to left language).
     */
    private android.graphics.drawable.Drawable mShadowDrawableRight;

    /**
     * The size of the overhang in pixels.
     * This is the minimum section of the sliding panel that will
     * be visible in the open state to allow for a closing drag.
     */
    private final int mOverhangSize;

    /**
     * True if a panel can slide with the current measurements
     */
    private boolean mCanSlide;

    /**
     * The child view that can slide, if any.
     */
    android.view.View mSlideableView;

    /**
     * How far the panel is offset from its closed position.
     * range [0, 1] where 0 = closed, 1 = open.
     */
    float mSlideOffset;

    /**
     * How far the non-sliding panel is parallaxed from its usual position when open.
     * range [0, 1]
     */
    private float mParallaxOffset;

    /**
     * How far in pixels the slideable panel may move.
     */
    int mSlideRange;

    /**
     * A panel view is locked into internal scrolling or another condition that
     * is preventing a drag.
     */
    boolean mIsUnableToDrag;

    /**
     * Distance in pixels to parallax the fixed pane by when fully closed
     */
    private int mParallaxBy;

    private float mInitialMotionX;

    private float mInitialMotionY;

    private android.support.v4.widget.SlidingPaneLayout.PanelSlideListener mPanelSlideListener;

    final android.support.v4.widget.ViewDragHelper mDragHelper;

    /**
     * Stores whether or not the pane was open the last time it was slideable.
     * If open/close operations are invoked this state is modified. Used by
     * instance state save/restore.
     */
    boolean mPreservedOpenState;

    private boolean mFirstLayout = true;

    private final android.graphics.Rect mTmpRect = new android.graphics.Rect();

    final java.util.ArrayList<android.support.v4.widget.SlidingPaneLayout.DisableLayerRunnable> mPostedRunnables = new java.util.ArrayList<android.support.v4.widget.SlidingPaneLayout.DisableLayerRunnable>();

    static final android.support.v4.widget.SlidingPaneLayout.SlidingPanelLayoutImpl IMPL;

    static {
        final int deviceVersion = android.os.Build.VERSION.SDK_INT;
        if (deviceVersion >= 17) {
            IMPL = new android.support.v4.widget.SlidingPaneLayout.SlidingPanelLayoutImplJBMR1();
        } else
            if (deviceVersion >= 16) {
                IMPL = new android.support.v4.widget.SlidingPaneLayout.SlidingPanelLayoutImplJB();
            } else {
                IMPL = new android.support.v4.widget.SlidingPaneLayout.SlidingPanelLayoutImplBase();
            }

    }

    /**
     * Listener for monitoring events about sliding panes.
     */
    public interface PanelSlideListener {
        /**
         * Called when a sliding pane's position changes.
         *
         * @param panel
         * 		The child view that was moved
         * @param slideOffset
         * 		The new offset of this sliding pane within its range, from 0-1
         */
        void onPanelSlide(android.view.View panel, float slideOffset);

        /**
         * Called when a sliding pane becomes slid completely open. The pane may or may not
         * be interactive at this point depending on how much of the pane is visible.
         *
         * @param panel
         * 		The child view that was slid to an open position, revealing other panes
         */
        void onPanelOpened(android.view.View panel);

        /**
         * Called when a sliding pane becomes slid completely closed. The pane is now guaranteed
         * to be interactive. It may now obscure other views in the layout.
         *
         * @param panel
         * 		The child view that was slid to a closed position
         */
        void onPanelClosed(android.view.View panel);
    }

    /**
     * No-op stubs for {@link PanelSlideListener}. If you only want to implement a subset
     * of the listener methods you can extend this instead of implement the full interface.
     */
    public static class SimplePanelSlideListener implements android.support.v4.widget.SlidingPaneLayout.PanelSlideListener {
        @java.lang.Override
        public void onPanelSlide(android.view.View panel, float slideOffset) {
        }

        @java.lang.Override
        public void onPanelOpened(android.view.View panel) {
        }

        @java.lang.Override
        public void onPanelClosed(android.view.View panel) {
        }
    }

    public SlidingPaneLayout(android.content.Context context) {
        this(context, null);
    }

    public SlidingPaneLayout(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingPaneLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final float density = context.getResources().getDisplayMetrics().density;
        mOverhangSize = ((int) ((android.support.v4.widget.SlidingPaneLayout.DEFAULT_OVERHANG_SIZE * density) + 0.5F));
        final android.view.ViewConfiguration viewConfig = android.view.ViewConfiguration.get(context);
        setWillNotDraw(false);
        android.support.v4.view.ViewCompat.setAccessibilityDelegate(this, new android.support.v4.widget.SlidingPaneLayout.AccessibilityDelegate());
        android.support.v4.view.ViewCompat.setImportantForAccessibility(this, android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
        mDragHelper = android.support.v4.widget.ViewDragHelper.create(this, 0.5F, new android.support.v4.widget.SlidingPaneLayout.DragHelperCallback());
        mDragHelper.setMinVelocity(android.support.v4.widget.SlidingPaneLayout.MIN_FLING_VELOCITY * density);
    }

    /**
     * Set a distance to parallax the lower pane by when the upper pane is in its
     * fully closed state. The lower pane will scroll between this position and
     * its fully open state.
     *
     * @param parallaxBy
     * 		Distance to parallax by in pixels
     */
    public void setParallaxDistance(int parallaxBy) {
        mParallaxBy = parallaxBy;
        requestLayout();
    }

    /**
     *
     *
     * @return The distance the lower pane will parallax by when the upper pane is fully closed.
     * @see #setParallaxDistance(int)
     */
    public int getParallaxDistance() {
        return mParallaxBy;
    }

    /**
     * Set the color used to fade the sliding pane out when it is slid most of the way offscreen.
     *
     * @param color
     * 		An ARGB-packed color value
     */
    public void setSliderFadeColor(@android.support.annotation.ColorInt
    int color) {
        mSliderFadeColor = color;
    }

    /**
     *
     *
     * @return The ARGB-packed color value used to fade the sliding pane
     */
    @android.support.annotation.ColorInt
    public int getSliderFadeColor() {
        return mSliderFadeColor;
    }

    /**
     * Set the color used to fade the pane covered by the sliding pane out when the pane
     * will become fully covered in the closed state.
     *
     * @param color
     * 		An ARGB-packed color value
     */
    public void setCoveredFadeColor(@android.support.annotation.ColorInt
    int color) {
        mCoveredFadeColor = color;
    }

    /**
     *
     *
     * @return The ARGB-packed color value used to fade the fixed pane
     */
    @android.support.annotation.ColorInt
    public int getCoveredFadeColor() {
        return mCoveredFadeColor;
    }

    public void setPanelSlideListener(android.support.v4.widget.SlidingPaneLayout.PanelSlideListener listener) {
        mPanelSlideListener = listener;
    }

    void dispatchOnPanelSlide(android.view.View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelSlide(panel, mSlideOffset);
        }
    }

    void dispatchOnPanelOpened(android.view.View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelOpened(panel);
        }
        sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }

    void dispatchOnPanelClosed(android.view.View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelClosed(panel);
        }
        sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }

    void updateObscuredViewsVisibility(android.view.View panel) {
        final boolean isLayoutRtl = isLayoutRtlSupport();
        final int startBound = (isLayoutRtl) ? getWidth() - getPaddingRight() : getPaddingLeft();
        final int endBound = (isLayoutRtl) ? getPaddingLeft() : getWidth() - getPaddingRight();
        final int topBound = getPaddingTop();
        final int bottomBound = getHeight() - getPaddingBottom();
        final int left;
        final int right;
        final int top;
        final int bottom;
        if ((panel != null) && android.support.v4.widget.SlidingPaneLayout.viewIsOpaque(panel)) {
            left = panel.getLeft();
            right = panel.getRight();
            top = panel.getTop();
            bottom = panel.getBottom();
        } else {
            left = right = top = bottom = 0;
        }
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            if (child == panel) {
                // There are still more children above the panel but they won't be affected.
                break;
            } else
                if (child.getVisibility() == android.view.View.GONE) {
                    continue;
                }

            final int clampedChildLeft = java.lang.Math.max(isLayoutRtl ? endBound : startBound, child.getLeft());
            final int clampedChildTop = java.lang.Math.max(topBound, child.getTop());
            final int clampedChildRight = java.lang.Math.min(isLayoutRtl ? startBound : endBound, child.getRight());
            final int clampedChildBottom = java.lang.Math.min(bottomBound, child.getBottom());
            final int vis;
            if ((((clampedChildLeft >= left) && (clampedChildTop >= top)) && (clampedChildRight <= right)) && (clampedChildBottom <= bottom)) {
                vis = android.view.View.INVISIBLE;
            } else {
                vis = android.view.View.VISIBLE;
            }
            child.setVisibility(vis);
        }
    }

    void setAllChildrenVisible() {
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            if (child.getVisibility() == android.view.View.INVISIBLE) {
                child.setVisibility(android.view.View.VISIBLE);
            }
        }
    }

    private static boolean viewIsOpaque(android.view.View v) {
        if (v.isOpaque()) {
            return true;
        }
        // View#isOpaque didn't take all valid opaque scrollbar modes into account
        // before API 18 (JB-MR2). On newer devices rely solely on isOpaque above and return false
        // here. On older devices, check the view's background drawable directly as a fallback.
        if (android.os.Build.VERSION.SDK_INT >= 18) {
            return false;
        }
        final android.graphics.drawable.Drawable bg = v.getBackground();
        if (bg != null) {
            return bg.getOpacity() == android.graphics.PixelFormat.OPAQUE;
        }
        return false;
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mFirstLayout = true;
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFirstLayout = true;
        for (int i = 0, count = mPostedRunnables.size(); i < count; i++) {
            final android.support.v4.widget.SlidingPaneLayout.DisableLayerRunnable dlr = mPostedRunnables.get(i);
            dlr.run();
        }
        mPostedRunnables.clear();
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode != android.view.View.MeasureSpec.EXACTLY) {
            if (isInEditMode()) {
                // Don't crash the layout editor. Consume all of the space if specified
                // or pick a magic number from thin air otherwise.
                // TODO Better communication with tools of this bogus state.
                // It will crash on a real device.
                if (widthMode == android.view.View.MeasureSpec.AT_MOST) {
                    widthMode = android.view.View.MeasureSpec.EXACTLY;
                } else
                    if (widthMode == android.view.View.MeasureSpec.UNSPECIFIED) {
                        widthMode = android.view.View.MeasureSpec.EXACTLY;
                        widthSize = 300;
                    }

            } else {
                throw new java.lang.IllegalStateException("Width must have an exact value or MATCH_PARENT");
            }
        } else
            if (heightMode == android.view.View.MeasureSpec.UNSPECIFIED) {
                if (isInEditMode()) {
                    // Don't crash the layout editor. Pick a magic number from thin air instead.
                    // TODO Better communication with tools of this bogus state.
                    // It will crash on a real device.
                    if (heightMode == android.view.View.MeasureSpec.UNSPECIFIED) {
                        heightMode = android.view.View.MeasureSpec.AT_MOST;
                        heightSize = 300;
                    }
                } else {
                    throw new java.lang.IllegalStateException("Height must not be UNSPECIFIED");
                }
            }

        int layoutHeight = 0;
        int maxLayoutHeight = -1;
        switch (heightMode) {
            case android.view.View.MeasureSpec.EXACTLY :
                layoutHeight = maxLayoutHeight = (heightSize - getPaddingTop()) - getPaddingBottom();
                break;
            case android.view.View.MeasureSpec.AT_MOST :
                maxLayoutHeight = (heightSize - getPaddingTop()) - getPaddingBottom();
                break;
        }
        float weightSum = 0;
        boolean canSlide = false;
        final int widthAvailable = (widthSize - getPaddingLeft()) - getPaddingRight();
        int widthRemaining = widthAvailable;
        final int childCount = getChildCount();
        if (childCount > 2) {
            android.util.Log.e(android.support.v4.widget.SlidingPaneLayout.TAG, "onMeasure: More than two child views are not supported.");
        }
        // We'll find the current one below.
        mSlideableView = null;
        // First pass. Measure based on child LayoutParams width/height.
        // Weight will incur a second pass.
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            final android.support.v4.widget.SlidingPaneLayout.LayoutParams lp = ((android.support.v4.widget.SlidingPaneLayout.LayoutParams) (child.getLayoutParams()));
            if (child.getVisibility() == android.view.View.GONE) {
                lp.dimWhenOffset = false;
                continue;
            }
            if (lp.weight > 0) {
                weightSum += lp.weight;
                // If we have no width, weight is the only contributor to the final size.
                // Measure this view on the weight pass only.
                if (lp.width == 0)
                    continue;

            }
            int childWidthSpec;
            final int horizontalMargin = lp.leftMargin + lp.rightMargin;
            if (lp.width == android.support.v4.widget.SlidingPaneLayout.LayoutParams.WRAP_CONTENT) {
                childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(widthAvailable - horizontalMargin, android.view.View.MeasureSpec.AT_MOST);
            } else
                if (lp.width == android.support.v4.widget.SlidingPaneLayout.LayoutParams.MATCH_PARENT) {
                    childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(widthAvailable - horizontalMargin, android.view.View.MeasureSpec.EXACTLY);
                } else {
                    childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(lp.width, android.view.View.MeasureSpec.EXACTLY);
                }

            int childHeightSpec;
            if (lp.height == android.support.v4.widget.SlidingPaneLayout.LayoutParams.WRAP_CONTENT) {
                childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(maxLayoutHeight, android.view.View.MeasureSpec.AT_MOST);
            } else
                if (lp.height == android.support.v4.widget.SlidingPaneLayout.LayoutParams.MATCH_PARENT) {
                    childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(maxLayoutHeight, android.view.View.MeasureSpec.EXACTLY);
                } else {
                    childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(lp.height, android.view.View.MeasureSpec.EXACTLY);
                }

            child.measure(childWidthSpec, childHeightSpec);
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();
            if ((heightMode == android.view.View.MeasureSpec.AT_MOST) && (childHeight > layoutHeight)) {
                layoutHeight = java.lang.Math.min(childHeight, maxLayoutHeight);
            }
            widthRemaining -= childWidth;
            canSlide |= lp.slideable = widthRemaining < 0;
            if (lp.slideable) {
                mSlideableView = child;
            }
        }
        // Resolve weight and make sure non-sliding panels are smaller than the full screen.
        if (canSlide || (weightSum > 0)) {
            final int fixedPanelWidthLimit = widthAvailable - mOverhangSize;
            for (int i = 0; i < childCount; i++) {
                final android.view.View child = getChildAt(i);
                if (child.getVisibility() == android.view.View.GONE) {
                    continue;
                }
                final android.support.v4.widget.SlidingPaneLayout.LayoutParams lp = ((android.support.v4.widget.SlidingPaneLayout.LayoutParams) (child.getLayoutParams()));
                if (child.getVisibility() == android.view.View.GONE) {
                    continue;
                }
                final boolean skippedFirstPass = (lp.width == 0) && (lp.weight > 0);
                final int measuredWidth = (skippedFirstPass) ? 0 : child.getMeasuredWidth();
                if (canSlide && (child != mSlideableView)) {
                    if ((lp.width < 0) && ((measuredWidth > fixedPanelWidthLimit) || (lp.weight > 0))) {
                        // Fixed panels in a sliding configuration should
                        // be clamped to the fixed panel limit.
                        final int childHeightSpec;
                        if (skippedFirstPass) {
                            // Do initial height measurement if we skipped measuring this view
                            // the first time around.
                            if (lp.height == android.support.v4.widget.SlidingPaneLayout.LayoutParams.WRAP_CONTENT) {
                                childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(maxLayoutHeight, android.view.View.MeasureSpec.AT_MOST);
                            } else
                                if (lp.height == android.support.v4.widget.SlidingPaneLayout.LayoutParams.MATCH_PARENT) {
                                    childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(maxLayoutHeight, android.view.View.MeasureSpec.EXACTLY);
                                } else {
                                    childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(lp.height, android.view.View.MeasureSpec.EXACTLY);
                                }

                        } else {
                            childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), android.view.View.MeasureSpec.EXACTLY);
                        }
                        final int childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(fixedPanelWidthLimit, android.view.View.MeasureSpec.EXACTLY);
                        child.measure(childWidthSpec, childHeightSpec);
                    }
                } else
                    if (lp.weight > 0) {
                        int childHeightSpec;
                        if (lp.width == 0) {
                            // This was skipped the first time; figure out a real height spec.
                            if (lp.height == android.support.v4.widget.SlidingPaneLayout.LayoutParams.WRAP_CONTENT) {
                                childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(maxLayoutHeight, android.view.View.MeasureSpec.AT_MOST);
                            } else
                                if (lp.height == android.support.v4.widget.SlidingPaneLayout.LayoutParams.MATCH_PARENT) {
                                    childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(maxLayoutHeight, android.view.View.MeasureSpec.EXACTLY);
                                } else {
                                    childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(lp.height, android.view.View.MeasureSpec.EXACTLY);
                                }

                        } else {
                            childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), android.view.View.MeasureSpec.EXACTLY);
                        }
                        if (canSlide) {
                            // Consume available space
                            final int horizontalMargin = lp.leftMargin + lp.rightMargin;
                            final int newWidth = widthAvailable - horizontalMargin;
                            final int childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(newWidth, android.view.View.MeasureSpec.EXACTLY);
                            if (measuredWidth != newWidth) {
                                child.measure(childWidthSpec, childHeightSpec);
                            }
                        } else {
                            // Distribute the extra width proportionally similar to LinearLayout
                            final int widthToDistribute = java.lang.Math.max(0, widthRemaining);
                            final int addedWidth = ((int) ((lp.weight * widthToDistribute) / weightSum));
                            final int childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(measuredWidth + addedWidth, android.view.View.MeasureSpec.EXACTLY);
                            child.measure(childWidthSpec, childHeightSpec);
                        }
                    }

            }
        }
        final int measuredWidth = widthSize;
        final int measuredHeight = (layoutHeight + getPaddingTop()) + getPaddingBottom();
        setMeasuredDimension(measuredWidth, measuredHeight);
        mCanSlide = canSlide;
        if ((mDragHelper.getViewDragState() != android.support.v4.widget.ViewDragHelper.STATE_IDLE) && (!canSlide)) {
            // Cancel scrolling in progress, it's no longer relevant.
            mDragHelper.abort();
        }
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final boolean isLayoutRtl = isLayoutRtlSupport();
        if (isLayoutRtl) {
            mDragHelper.setEdgeTrackingEnabled(android.support.v4.widget.ViewDragHelper.EDGE_RIGHT);
        } else {
            mDragHelper.setEdgeTrackingEnabled(android.support.v4.widget.ViewDragHelper.EDGE_LEFT);
        }
        final int width = r - l;
        final int paddingStart = (isLayoutRtl) ? getPaddingRight() : getPaddingLeft();
        final int paddingEnd = (isLayoutRtl) ? getPaddingLeft() : getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int childCount = getChildCount();
        int xStart = paddingStart;
        int nextXStart = xStart;
        if (mFirstLayout) {
            mSlideOffset = (mCanSlide && mPreservedOpenState) ? 1.0F : 0.0F;
        }
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            if (child.getVisibility() == android.view.View.GONE) {
                continue;
            }
            final android.support.v4.widget.SlidingPaneLayout.LayoutParams lp = ((android.support.v4.widget.SlidingPaneLayout.LayoutParams) (child.getLayoutParams()));
            final int childWidth = child.getMeasuredWidth();
            int offset = 0;
            if (lp.slideable) {
                final int margin = lp.leftMargin + lp.rightMargin;
                final int range = (java.lang.Math.min(nextXStart, (width - paddingEnd) - mOverhangSize) - xStart) - margin;
                mSlideRange = range;
                final int lpMargin = (isLayoutRtl) ? lp.rightMargin : lp.leftMargin;
                lp.dimWhenOffset = (((xStart + lpMargin) + range) + (childWidth / 2)) > (width - paddingEnd);
                final int pos = ((int) (range * mSlideOffset));
                xStart += pos + lpMargin;
                mSlideOffset = ((float) (pos)) / mSlideRange;
            } else
                if (mCanSlide && (mParallaxBy != 0)) {
                    offset = ((int) ((1 - mSlideOffset) * mParallaxBy));
                    xStart = nextXStart;
                } else {
                    xStart = nextXStart;
                }

            final int childRight;
            final int childLeft;
            if (isLayoutRtl) {
                childRight = (width - xStart) + offset;
                childLeft = childRight - childWidth;
            } else {
                childLeft = xStart - offset;
                childRight = childLeft + childWidth;
            }
            final int childTop = paddingTop;
            final int childBottom = childTop + child.getMeasuredHeight();
            child.layout(childLeft, paddingTop, childRight, childBottom);
            nextXStart += child.getWidth();
        }
        if (mFirstLayout) {
            if (mCanSlide) {
                if (mParallaxBy != 0) {
                    parallaxOtherViews(mSlideOffset);
                }
                if (((android.support.v4.widget.SlidingPaneLayout.LayoutParams) (mSlideableView.getLayoutParams())).dimWhenOffset) {
                    dimChildView(mSlideableView, mSlideOffset, mSliderFadeColor);
                }
            } else {
                // Reset the dim level of all children; it's irrelevant when nothing moves.
                for (int i = 0; i < childCount; i++) {
                    dimChildView(getChildAt(i), 0, mSliderFadeColor);
                }
            }
            updateObscuredViewsVisibility(mSlideableView);
        }
        mFirstLayout = false;
    }

    @java.lang.Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Recalculate sliding panes and their details
        if (w != oldw) {
            mFirstLayout = true;
        }
    }

    @java.lang.Override
    public void requestChildFocus(android.view.View child, android.view.View focused) {
        super.requestChildFocus(child, focused);
        if ((!isInTouchMode()) && (!mCanSlide)) {
            mPreservedOpenState = child == mSlideableView;
        }
    }

    @java.lang.Override
    public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
        final int action = android.support.v4.view.MotionEventCompat.getActionMasked(ev);
        // Preserve the open state based on the last view that was touched.
        if (((!mCanSlide) && (action == android.view.MotionEvent.ACTION_DOWN)) && (getChildCount() > 1)) {
            // After the first things will be slideable.
            final android.view.View secondChild = getChildAt(1);
            if (secondChild != null) {
                mPreservedOpenState = !mDragHelper.isViewUnder(secondChild, ((int) (ev.getX())), ((int) (ev.getY())));
            }
        }
        if ((!mCanSlide) || (mIsUnableToDrag && (action != android.view.MotionEvent.ACTION_DOWN))) {
            mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }
        if ((action == android.view.MotionEvent.ACTION_CANCEL) || (action == android.view.MotionEvent.ACTION_UP)) {
            mDragHelper.cancel();
            return false;
        }
        boolean interceptTap = false;
        switch (action) {
            case android.view.MotionEvent.ACTION_DOWN :
                {
                    mIsUnableToDrag = false;
                    final float x = ev.getX();
                    final float y = ev.getY();
                    mInitialMotionX = x;
                    mInitialMotionY = y;
                    if (mDragHelper.isViewUnder(mSlideableView, ((int) (x)), ((int) (y))) && isDimmed(mSlideableView)) {
                        interceptTap = true;
                    }
                    break;
                }
            case android.view.MotionEvent.ACTION_MOVE :
                {
                    final float x = ev.getX();
                    final float y = ev.getY();
                    final float adx = java.lang.Math.abs(x - mInitialMotionX);
                    final float ady = java.lang.Math.abs(y - mInitialMotionY);
                    final int slop = mDragHelper.getTouchSlop();
                    if ((adx > slop) && (ady > adx)) {
                        mDragHelper.cancel();
                        mIsUnableToDrag = true;
                        return false;
                    }
                }
        }
        final boolean interceptForDrag = mDragHelper.shouldInterceptTouchEvent(ev);
        return interceptForDrag || interceptTap;
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        if (!mCanSlide) {
            return super.onTouchEvent(ev);
        }
        mDragHelper.processTouchEvent(ev);
        final int action = ev.getAction();
        boolean wantTouchEvents = true;
        switch (action & android.support.v4.view.MotionEventCompat.ACTION_MASK) {
            case android.view.MotionEvent.ACTION_DOWN :
                {
                    final float x = ev.getX();
                    final float y = ev.getY();
                    mInitialMotionX = x;
                    mInitialMotionY = y;
                    break;
                }
            case android.view.MotionEvent.ACTION_UP :
                {
                    if (isDimmed(mSlideableView)) {
                        final float x = ev.getX();
                        final float y = ev.getY();
                        final float dx = x - mInitialMotionX;
                        final float dy = y - mInitialMotionY;
                        final int slop = mDragHelper.getTouchSlop();
                        if ((((dx * dx) + (dy * dy)) < (slop * slop)) && mDragHelper.isViewUnder(mSlideableView, ((int) (x)), ((int) (y)))) {
                            // Taps close a dimmed open pane.
                            closePane(mSlideableView, 0);
                            break;
                        }
                    }
                    break;
                }
        }
        return wantTouchEvents;
    }

    private boolean closePane(android.view.View pane, int initialVelocity) {
        if (mFirstLayout || smoothSlideTo(0.0F, initialVelocity)) {
            mPreservedOpenState = false;
            return true;
        }
        return false;
    }

    private boolean openPane(android.view.View pane, int initialVelocity) {
        if (mFirstLayout || smoothSlideTo(1.0F, initialVelocity)) {
            mPreservedOpenState = true;
            return true;
        }
        return false;
    }

    /**
     *
     *
     * @deprecated Renamed to {@link #openPane()} - this method is going away soon!
     */
    @java.lang.Deprecated
    public void smoothSlideOpen() {
        openPane();
    }

    /**
     * Open the sliding pane if it is currently slideable. If first layout
     * has already completed this will animate.
     *
     * @return true if the pane was slideable and is now open/in the process of opening
     */
    public boolean openPane() {
        return openPane(mSlideableView, 0);
    }

    /**
     *
     *
     * @deprecated Renamed to {@link #closePane()} - this method is going away soon!
     */
    @java.lang.Deprecated
    public void smoothSlideClosed() {
        closePane();
    }

    /**
     * Close the sliding pane if it is currently slideable. If first layout
     * has already completed this will animate.
     *
     * @return true if the pane was slideable and is now closed/in the process of closing
     */
    public boolean closePane() {
        return closePane(mSlideableView, 0);
    }

    /**
     * Check if the layout is completely open. It can be open either because the slider
     * itself is open revealing the left pane, or if all content fits without sliding.
     *
     * @return true if sliding panels are completely open
     */
    public boolean isOpen() {
        return (!mCanSlide) || (mSlideOffset == 1);
    }

    /**
     *
     *
     * @return true if content in this layout can be slid open and closed
     * @deprecated Renamed to {@link #isSlideable()} - this method is going away soon!
     */
    @java.lang.Deprecated
    public boolean canSlide() {
        return mCanSlide;
    }

    /**
     * Check if the content in this layout cannot fully fit side by side and therefore
     * the content pane can be slid back and forth.
     *
     * @return true if content in this layout can be slid open and closed
     */
    public boolean isSlideable() {
        return mCanSlide;
    }

    void onPanelDragged(int newLeft) {
        if (mSlideableView == null) {
            // This can happen if we're aborting motion during layout because everything now fits.
            mSlideOffset = 0;
            return;
        }
        final boolean isLayoutRtl = isLayoutRtlSupport();
        final android.support.v4.widget.SlidingPaneLayout.LayoutParams lp = ((android.support.v4.widget.SlidingPaneLayout.LayoutParams) (mSlideableView.getLayoutParams()));
        int childWidth = mSlideableView.getWidth();
        final int newStart = (isLayoutRtl) ? (getWidth() - newLeft) - childWidth : newLeft;
        final int paddingStart = (isLayoutRtl) ? getPaddingRight() : getPaddingLeft();
        final int lpMargin = (isLayoutRtl) ? lp.rightMargin : lp.leftMargin;
        final int startBound = paddingStart + lpMargin;
        mSlideOffset = ((float) (newStart - startBound)) / mSlideRange;
        if (mParallaxBy != 0) {
            parallaxOtherViews(mSlideOffset);
        }
        if (lp.dimWhenOffset) {
            dimChildView(mSlideableView, mSlideOffset, mSliderFadeColor);
        }
        dispatchOnPanelSlide(mSlideableView);
    }

    private void dimChildView(android.view.View v, float mag, int fadeColor) {
        final android.support.v4.widget.SlidingPaneLayout.LayoutParams lp = ((android.support.v4.widget.SlidingPaneLayout.LayoutParams) (v.getLayoutParams()));
        if ((mag > 0) && (fadeColor != 0)) {
            final int baseAlpha = (fadeColor & 0xff000000) >>> 24;
            int imag = ((int) (baseAlpha * mag));
            int color = (imag << 24) | (fadeColor & 0xffffff);
            if (lp.dimPaint == null) {
                lp.dimPaint = new android.graphics.Paint();
            }
            lp.dimPaint.setColorFilter(new android.graphics.PorterDuffColorFilter(color, android.graphics.PorterDuff.Mode.SRC_OVER));
            if (android.support.v4.view.ViewCompat.getLayerType(v) != android.support.v4.view.ViewCompat.LAYER_TYPE_HARDWARE) {
                android.support.v4.view.ViewCompat.setLayerType(v, android.support.v4.view.ViewCompat.LAYER_TYPE_HARDWARE, lp.dimPaint);
            }
            invalidateChildRegion(v);
        } else
            if (android.support.v4.view.ViewCompat.getLayerType(v) != android.support.v4.view.ViewCompat.LAYER_TYPE_NONE) {
                if (lp.dimPaint != null) {
                    lp.dimPaint.setColorFilter(null);
                }
                final android.support.v4.widget.SlidingPaneLayout.DisableLayerRunnable dlr = new android.support.v4.widget.SlidingPaneLayout.DisableLayerRunnable(v);
                mPostedRunnables.add(dlr);
                android.support.v4.view.ViewCompat.postOnAnimation(this, dlr);
            }

    }

    @java.lang.Override
    protected boolean drawChild(android.graphics.Canvas canvas, android.view.View child, long drawingTime) {
        final android.support.v4.widget.SlidingPaneLayout.LayoutParams lp = ((android.support.v4.widget.SlidingPaneLayout.LayoutParams) (child.getLayoutParams()));
        boolean result;
        final int save = canvas.save(android.graphics.Canvas.CLIP_SAVE_FLAG);
        if ((mCanSlide && (!lp.slideable)) && (mSlideableView != null)) {
            // Clip against the slider; no sense drawing what will immediately be covered.
            canvas.getClipBounds(mTmpRect);
            if (isLayoutRtlSupport()) {
                mTmpRect.left = java.lang.Math.max(mTmpRect.left, mSlideableView.getRight());
            } else {
                mTmpRect.right = java.lang.Math.min(mTmpRect.right, mSlideableView.getLeft());
            }
            canvas.clipRect(mTmpRect);
        }
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            // HC
            result = super.drawChild(canvas, child, drawingTime);
        } else {
            if (lp.dimWhenOffset && (mSlideOffset > 0)) {
                if (!child.isDrawingCacheEnabled()) {
                    child.setDrawingCacheEnabled(true);
                }
                final android.graphics.Bitmap cache = child.getDrawingCache();
                if (cache != null) {
                    canvas.drawBitmap(cache, child.getLeft(), child.getTop(), lp.dimPaint);
                    result = false;
                } else {
                    android.util.Log.e(android.support.v4.widget.SlidingPaneLayout.TAG, ("drawChild: child view " + child) + " returned null drawing cache");
                    result = super.drawChild(canvas, child, drawingTime);
                }
            } else {
                if (child.isDrawingCacheEnabled()) {
                    child.setDrawingCacheEnabled(false);
                }
                result = super.drawChild(canvas, child, drawingTime);
            }
        }
        canvas.restoreToCount(save);
        return result;
    }

    void invalidateChildRegion(android.view.View v) {
        android.support.v4.widget.SlidingPaneLayout.IMPL.invalidateChildRegion(this, v);
    }

    /**
     * Smoothly animate mDraggingPane to the target X position within its range.
     *
     * @param slideOffset
     * 		position to animate to
     * @param velocity
     * 		initial velocity in case of fling, or 0.
     */
    boolean smoothSlideTo(float slideOffset, int velocity) {
        if (!mCanSlide) {
            // Nothing to do.
            return false;
        }
        final boolean isLayoutRtl = isLayoutRtlSupport();
        final android.support.v4.widget.SlidingPaneLayout.LayoutParams lp = ((android.support.v4.widget.SlidingPaneLayout.LayoutParams) (mSlideableView.getLayoutParams()));
        int x;
        if (isLayoutRtl) {
            int startBound = getPaddingRight() + lp.rightMargin;
            int childWidth = mSlideableView.getWidth();
            x = ((int) (getWidth() - ((startBound + (slideOffset * mSlideRange)) + childWidth)));
        } else {
            int startBound = getPaddingLeft() + lp.leftMargin;
            x = ((int) (startBound + (slideOffset * mSlideRange)));
        }
        if (mDragHelper.smoothSlideViewTo(mSlideableView, x, mSlideableView.getTop())) {
            setAllChildrenVisible();
            android.support.v4.view.ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    @java.lang.Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            if (!mCanSlide) {
                mDragHelper.abort();
                return;
            }
            android.support.v4.view.ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     *
     *
     * @deprecated Renamed to {@link #setShadowDrawableLeft(Drawable d)} to support LTR (left to
    right language) and {@link #setShadowDrawableRight(Drawable d)} to support RTL (right to left
    language) during opening/closing.
     * @param d
     * 		drawable to use as a shadow
     */
    @java.lang.Deprecated
    public void setShadowDrawable(android.graphics.drawable.Drawable d) {
        setShadowDrawableLeft(d);
    }

    /**
     * Set a drawable to use as a shadow cast by the right pane onto the left pane
     * during opening/closing.
     *
     * @param d
     * 		drawable to use as a shadow
     */
    public void setShadowDrawableLeft(android.graphics.drawable.Drawable d) {
        mShadowDrawableLeft = d;
    }

    /**
     * Set a drawable to use as a shadow cast by the left pane onto the right pane
     * during opening/closing to support right to left language.
     *
     * @param d
     * 		drawable to use as a shadow
     */
    public void setShadowDrawableRight(android.graphics.drawable.Drawable d) {
        mShadowDrawableRight = d;
    }

    /**
     * Set a drawable to use as a shadow cast by the right pane onto the left pane
     * during opening/closing.
     *
     * @param resId
     * 		Resource ID of a drawable to use
     * @deprecated Renamed to {@link #setShadowResourceLeft(int)} to support LTR (left to
    right language) and {@link #setShadowResourceRight(int)} to support RTL (right to left
    language) during opening/closing.
     */
    @java.lang.Deprecated
    public void setShadowResource(@android.support.annotation.DrawableRes
    int resId) {
        setShadowDrawable(getResources().getDrawable(resId));
    }

    /**
     * Set a drawable to use as a shadow cast by the right pane onto the left pane
     * during opening/closing.
     *
     * @param resId
     * 		Resource ID of a drawable to use
     */
    public void setShadowResourceLeft(int resId) {
        setShadowDrawableLeft(android.support.v4.content.ContextCompat.getDrawable(getContext(), resId));
    }

    /**
     * Set a drawable to use as a shadow cast by the left pane onto the right pane
     * during opening/closing to support right to left language.
     *
     * @param resId
     * 		Resource ID of a drawable to use
     */
    public void setShadowResourceRight(int resId) {
        setShadowDrawableRight(android.support.v4.content.ContextCompat.getDrawable(getContext(), resId));
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas c) {
        super.draw(c);
        final boolean isLayoutRtl = isLayoutRtlSupport();
        android.graphics.drawable.Drawable shadowDrawable;
        if (isLayoutRtl) {
            shadowDrawable = mShadowDrawableRight;
        } else {
            shadowDrawable = mShadowDrawableLeft;
        }
        final android.view.View shadowView = (getChildCount() > 1) ? getChildAt(1) : null;
        if ((shadowView == null) || (shadowDrawable == null)) {
            // No need to draw a shadow if we don't have one.
            return;
        }
        final int top = shadowView.getTop();
        final int bottom = shadowView.getBottom();
        final int shadowWidth = shadowDrawable.getIntrinsicWidth();
        final int left;
        final int right;
        if (isLayoutRtlSupport()) {
            left = shadowView.getRight();
            right = left + shadowWidth;
        } else {
            right = shadowView.getLeft();
            left = right - shadowWidth;
        }
        shadowDrawable.setBounds(left, top, right, bottom);
        shadowDrawable.draw(c);
    }

    private void parallaxOtherViews(float slideOffset) {
        final boolean isLayoutRtl = isLayoutRtlSupport();
        final android.support.v4.widget.SlidingPaneLayout.LayoutParams slideLp = ((android.support.v4.widget.SlidingPaneLayout.LayoutParams) (mSlideableView.getLayoutParams()));
        final boolean dimViews = slideLp.dimWhenOffset && ((isLayoutRtl ? slideLp.rightMargin : slideLp.leftMargin) <= 0);
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View v = getChildAt(i);
            if (v == mSlideableView)
                continue;

            final int oldOffset = ((int) ((1 - mParallaxOffset) * mParallaxBy));
            mParallaxOffset = slideOffset;
            final int newOffset = ((int) ((1 - slideOffset) * mParallaxBy));
            final int dx = oldOffset - newOffset;
            v.offsetLeftAndRight(isLayoutRtl ? -dx : dx);
            if (dimViews) {
                dimChildView(v, isLayoutRtl ? mParallaxOffset - 1 : 1 - mParallaxOffset, mCoveredFadeColor);
            }
        }
    }

    /**
     * Tests scrollability within child views of v given a delta of dx.
     *
     * @param v
     * 		View to test for horizontal scrollability
     * @param checkV
     * 		Whether the view v passed should itself be checked for scrollability (true),
     * 		or just its children (false).
     * @param dx
     * 		Delta scrolled in pixels
     * @param x
     * 		X coordinate of the active touch point
     * @param y
     * 		Y coordinate of the active touch point
     * @return true if child views of v can be scrolled by delta of dx.
     */
    protected boolean canScroll(android.view.View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof android.view.ViewGroup) {
            final android.view.ViewGroup group = ((android.view.ViewGroup) (v));
            final int scrollX = v.getScrollX();
            final int scrollY = v.getScrollY();
            final int count = group.getChildCount();
            // Count backwards - let topmost views consume scroll distance first.
            for (int i = count - 1; i >= 0; i--) {
                // TODO: Add versioned support here for transformed views.
                // This will not work for transformed views in Honeycomb+
                final android.view.View child = group.getChildAt(i);
                if ((((((x + scrollX) >= child.getLeft()) && ((x + scrollX) < child.getRight())) && ((y + scrollY) >= child.getTop())) && ((y + scrollY) < child.getBottom())) && canScroll(child, true, dx, (x + scrollX) - child.getLeft(), (y + scrollY) - child.getTop())) {
                    return true;
                }
            }
        }
        return checkV && android.support.v4.view.ViewCompat.canScrollHorizontally(v, isLayoutRtlSupport() ? dx : -dx);
    }

    boolean isDimmed(android.view.View child) {
        if (child == null) {
            return false;
        }
        final android.support.v4.widget.SlidingPaneLayout.LayoutParams lp = ((android.support.v4.widget.SlidingPaneLayout.LayoutParams) (child.getLayoutParams()));
        return (mCanSlide && lp.dimWhenOffset) && (mSlideOffset > 0);
    }

    @java.lang.Override
    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new android.support.v4.widget.SlidingPaneLayout.LayoutParams();
    }

    @java.lang.Override
    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof android.view.ViewGroup.MarginLayoutParams ? new android.support.v4.widget.SlidingPaneLayout.LayoutParams(((android.view.ViewGroup.MarginLayoutParams) (p))) : new android.support.v4.widget.SlidingPaneLayout.LayoutParams(p);
    }

    @java.lang.Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return (p instanceof android.support.v4.widget.SlidingPaneLayout.LayoutParams) && super.checkLayoutParams(p);
    }

    @java.lang.Override
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.support.v4.widget.SlidingPaneLayout.LayoutParams(getContext(), attrs);
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        android.os.Parcelable superState = super.onSaveInstanceState();
        android.support.v4.widget.SlidingPaneLayout.SavedState ss = new android.support.v4.widget.SlidingPaneLayout.SavedState(superState);
        ss.isOpen = (isSlideable()) ? isOpen() : mPreservedOpenState;
        return ss;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if (!(state instanceof android.support.v4.widget.SlidingPaneLayout.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        android.support.v4.widget.SlidingPaneLayout.SavedState ss = ((android.support.v4.widget.SlidingPaneLayout.SavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.isOpen) {
            openPane();
        } else {
            closePane();
        }
        mPreservedOpenState = ss.isOpen;
    }

    private class DragHelperCallback extends android.support.v4.widget.ViewDragHelper.Callback {
        DragHelperCallback() {
        }

        @java.lang.Override
        public boolean tryCaptureView(android.view.View child, int pointerId) {
            if (mIsUnableToDrag) {
                return false;
            }
            return ((android.support.v4.widget.SlidingPaneLayout.LayoutParams) (child.getLayoutParams())).slideable;
        }

        @java.lang.Override
        public void onViewDragStateChanged(int state) {
            if (mDragHelper.getViewDragState() == android.support.v4.widget.ViewDragHelper.STATE_IDLE) {
                if (mSlideOffset == 0) {
                    updateObscuredViewsVisibility(mSlideableView);
                    dispatchOnPanelClosed(mSlideableView);
                    mPreservedOpenState = false;
                } else {
                    dispatchOnPanelOpened(mSlideableView);
                    mPreservedOpenState = true;
                }
            }
        }

        @java.lang.Override
        public void onViewCaptured(android.view.View capturedChild, int activePointerId) {
            // Make all child views visible in preparation for sliding things around
            setAllChildrenVisible();
        }

        @java.lang.Override
        public void onViewPositionChanged(android.view.View changedView, int left, int top, int dx, int dy) {
            onPanelDragged(left);
            invalidate();
        }

        @java.lang.Override
        public void onViewReleased(android.view.View releasedChild, float xvel, float yvel) {
            final android.support.v4.widget.SlidingPaneLayout.LayoutParams lp = ((android.support.v4.widget.SlidingPaneLayout.LayoutParams) (releasedChild.getLayoutParams()));
            int left;
            if (isLayoutRtlSupport()) {
                int startToRight = getPaddingRight() + lp.rightMargin;
                if ((xvel < 0) || ((xvel == 0) && (mSlideOffset > 0.5F))) {
                    startToRight += mSlideRange;
                }
                int childWidth = mSlideableView.getWidth();
                left = (getWidth() - startToRight) - childWidth;
            } else {
                left = getPaddingLeft() + lp.leftMargin;
                if ((xvel > 0) || ((xvel == 0) && (mSlideOffset > 0.5F))) {
                    left += mSlideRange;
                }
            }
            mDragHelper.settleCapturedViewAt(left, releasedChild.getTop());
            invalidate();
        }

        @java.lang.Override
        public int getViewHorizontalDragRange(android.view.View child) {
            return mSlideRange;
        }

        @java.lang.Override
        public int clampViewPositionHorizontal(android.view.View child, int left, int dx) {
            final android.support.v4.widget.SlidingPaneLayout.LayoutParams lp = ((android.support.v4.widget.SlidingPaneLayout.LayoutParams) (mSlideableView.getLayoutParams()));
            final int newLeft;
            if (isLayoutRtlSupport()) {
                int startBound = getWidth() - ((getPaddingRight() + lp.rightMargin) + mSlideableView.getWidth());
                int endBound = startBound - mSlideRange;
                newLeft = java.lang.Math.max(java.lang.Math.min(left, startBound), endBound);
            } else {
                int startBound = getPaddingLeft() + lp.leftMargin;
                int endBound = startBound + mSlideRange;
                newLeft = java.lang.Math.min(java.lang.Math.max(left, startBound), endBound);
            }
            return newLeft;
        }

        @java.lang.Override
        public int clampViewPositionVertical(android.view.View child, int top, int dy) {
            // Make sure we never move views vertically.
            // This could happen if the child has less height than its parent.
            return child.getTop();
        }

        @java.lang.Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            mDragHelper.captureChildView(mSlideableView, pointerId);
        }
    }

    public static class LayoutParams extends android.view.ViewGroup.MarginLayoutParams {
        private static final int[] ATTRS = new int[]{ android.R.attr.layout_weight };

        /**
         * The weighted proportion of how much of the leftover space
         * this child should consume after measurement.
         */
        public float weight = 0;

        /**
         * True if this pane is the slideable pane in the layout.
         */
        boolean slideable;

        /**
         * True if this view should be drawn dimmed
         * when it's been offset from its default position.
         */
        boolean dimWhenOffset;

        android.graphics.Paint dimPaint;

        public LayoutParams() {
            super(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(android.view.ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(android.support.v4.widget.SlidingPaneLayout.LayoutParams source) {
            super(source);
            this.weight = source.weight;
        }

        public LayoutParams(android.content.Context c, android.util.AttributeSet attrs) {
            super(c, attrs);
            final android.content.res.TypedArray a = c.obtainStyledAttributes(attrs, android.support.v4.widget.SlidingPaneLayout.LayoutParams.ATTRS);
            this.weight = a.getFloat(0, 0);
            a.recycle();
        }
    }

    static class SavedState extends android.support.v4.view.AbsSavedState {
        boolean isOpen;

        SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        SavedState(android.os.Parcel in, java.lang.ClassLoader loader) {
            super(in, loader);
            isOpen = in.readInt() != 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(isOpen ? 1 : 0);
        }

        public static final android.os.Parcelable.Creator<android.support.v4.widget.SlidingPaneLayout.SavedState> CREATOR = android.support.v4.os.ParcelableCompat.newCreator(new android.support.v4.os.ParcelableCompatCreatorCallbacks<android.support.v4.widget.SlidingPaneLayout.SavedState>() {
            @java.lang.Override
            public android.support.v4.widget.SlidingPaneLayout.SavedState createFromParcel(android.os.Parcel in, java.lang.ClassLoader loader) {
                return new android.support.v4.widget.SlidingPaneLayout.SavedState(in, loader);
            }

            @java.lang.Override
            public android.support.v4.widget.SlidingPaneLayout.SavedState[] newArray(int size) {
                return new android.support.v4.widget.SlidingPaneLayout.SavedState[size];
            }
        });
    }

    interface SlidingPanelLayoutImpl {
        void invalidateChildRegion(android.support.v4.widget.SlidingPaneLayout parent, android.view.View child);
    }

    static class SlidingPanelLayoutImplBase implements android.support.v4.widget.SlidingPaneLayout.SlidingPanelLayoutImpl {
        @java.lang.Override
        public void invalidateChildRegion(android.support.v4.widget.SlidingPaneLayout parent, android.view.View child) {
            android.support.v4.view.ViewCompat.postInvalidateOnAnimation(parent, child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
        }
    }

    static class SlidingPanelLayoutImplJB extends android.support.v4.widget.SlidingPaneLayout.SlidingPanelLayoutImplBase {
        /* Private API hacks! Nasty! Bad!

        In Jellybean, some optimizations in the hardware UI renderer
        prevent a changed Paint on a View using a hardware layer from having
        the intended effect. This twiddles some internal bits on the view to force
        it to recreate the display list.
         */
        private java.lang.reflect.Method mGetDisplayList;

        private java.lang.reflect.Field mRecreateDisplayList;

        SlidingPanelLayoutImplJB() {
            try {
                mGetDisplayList = android.view.View.class.getDeclaredMethod("getDisplayList", ((java.lang.Class[]) (null)));
            } catch (java.lang.NoSuchMethodException e) {
                android.util.Log.e(android.support.v4.widget.SlidingPaneLayout.TAG, "Couldn't fetch getDisplayList method; dimming won't work right.", e);
            }
            try {
                mRecreateDisplayList = android.view.View.class.getDeclaredField("mRecreateDisplayList");
                mRecreateDisplayList.setAccessible(true);
            } catch (java.lang.NoSuchFieldException e) {
                android.util.Log.e(android.support.v4.widget.SlidingPaneLayout.TAG, "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", e);
            }
        }

        @java.lang.Override
        public void invalidateChildRegion(android.support.v4.widget.SlidingPaneLayout parent, android.view.View child) {
            if ((mGetDisplayList != null) && (mRecreateDisplayList != null)) {
                try {
                    mRecreateDisplayList.setBoolean(child, true);
                    mGetDisplayList.invoke(child, ((java.lang.Object[]) (null)));
                } catch (java.lang.Exception e) {
                    android.util.Log.e(android.support.v4.widget.SlidingPaneLayout.TAG, "Error refreshing display list state", e);
                }
            } else {
                // Slow path. REALLY slow path. Let's hope we don't get here.
                child.invalidate();
                return;
            }
            super.invalidateChildRegion(parent, child);
        }
    }

    static class SlidingPanelLayoutImplJBMR1 extends android.support.v4.widget.SlidingPaneLayout.SlidingPanelLayoutImplBase {
        @java.lang.Override
        public void invalidateChildRegion(android.support.v4.widget.SlidingPaneLayout parent, android.view.View child) {
            android.support.v4.view.ViewCompat.setLayerPaint(child, ((android.support.v4.widget.SlidingPaneLayout.LayoutParams) (child.getLayoutParams())).dimPaint);
        }
    }

    class AccessibilityDelegate extends android.support.v4.view.AccessibilityDelegateCompat {
        private final android.graphics.Rect mTmpRect = new android.graphics.Rect();

        @java.lang.Override
        public void onInitializeAccessibilityNodeInfo(android.view.View host, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
            final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat superNode = android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.obtain(info);
            super.onInitializeAccessibilityNodeInfo(host, superNode);
            copyNodeInfoNoChildren(info, superNode);
            superNode.recycle();
            info.setClassName(android.support.v4.widget.SlidingPaneLayout.class.getName());
            info.setSource(host);
            final android.view.ViewParent parent = android.support.v4.view.ViewCompat.getParentForAccessibility(host);
            if (parent instanceof android.view.View) {
                info.setParent(((android.view.View) (parent)));
            }
            // This is a best-approximation of addChildrenForAccessibility()
            // that accounts for filtering.
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final android.view.View child = getChildAt(i);
                if ((!filter(child)) && (child.getVisibility() == android.view.View.VISIBLE)) {
                    // Force importance to "yes" since we can't read the value.
                    android.support.v4.view.ViewCompat.setImportantForAccessibility(child, android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
                    info.addChild(child);
                }
            }
        }

        @java.lang.Override
        public void onInitializeAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(android.support.v4.widget.SlidingPaneLayout.class.getName());
        }

        @java.lang.Override
        public boolean onRequestSendAccessibilityEvent(android.view.ViewGroup host, android.view.View child, android.view.accessibility.AccessibilityEvent event) {
            if (!filter(child)) {
                return super.onRequestSendAccessibilityEvent(host, child, event);
            }
            return false;
        }

        public boolean filter(android.view.View child) {
            return isDimmed(child);
        }

        /**
         * This should really be in AccessibilityNodeInfoCompat, but there unfortunately
         * seem to be a few elements that are not easily cloneable using the underlying API.
         * Leave it private here as it's not general-purpose useful.
         */
        private void copyNodeInfoNoChildren(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat dest, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat src) {
            final android.graphics.Rect rect = mTmpRect;
            src.getBoundsInParent(rect);
            dest.setBoundsInParent(rect);
            src.getBoundsInScreen(rect);
            dest.setBoundsInScreen(rect);
            dest.setVisibleToUser(src.isVisibleToUser());
            dest.setPackageName(src.getPackageName());
            dest.setClassName(src.getClassName());
            dest.setContentDescription(src.getContentDescription());
            dest.setEnabled(src.isEnabled());
            dest.setClickable(src.isClickable());
            dest.setFocusable(src.isFocusable());
            dest.setFocused(src.isFocused());
            dest.setAccessibilityFocused(src.isAccessibilityFocused());
            dest.setSelected(src.isSelected());
            dest.setLongClickable(src.isLongClickable());
            dest.addAction(src.getActions());
            dest.setMovementGranularities(src.getMovementGranularities());
        }
    }

    private class DisableLayerRunnable implements java.lang.Runnable {
        final android.view.View mChildView;

        DisableLayerRunnable(android.view.View childView) {
            mChildView = childView;
        }

        @java.lang.Override
        public void run() {
            if (mChildView.getParent() == android.support.v4.widget.SlidingPaneLayout.this) {
                android.support.v4.view.ViewCompat.setLayerType(mChildView, android.support.v4.view.ViewCompat.LAYER_TYPE_NONE, null);
                invalidateChildRegion(mChildView);
            }
            mPostedRunnables.remove(this);
        }
    }

    boolean isLayoutRtlSupport() {
        return android.support.v4.view.ViewCompat.getLayoutDirection(this) == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL;
    }
}

