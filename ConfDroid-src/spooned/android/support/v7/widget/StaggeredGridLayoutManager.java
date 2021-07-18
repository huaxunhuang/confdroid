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
 * A LayoutManager that lays out children in a staggered grid formation.
 * It supports horizontal & vertical layout as well as an ability to layout children in reverse.
 * <p>
 * Staggered grids are likely to have gaps at the edges of the layout. To avoid these gaps,
 * StaggeredGridLayoutManager can offset spans independently or move items between spans. You can
 * control this behavior via {@link #setGapStrategy(int)}.
 */
public class StaggeredGridLayoutManager extends android.support.v7.widget.RecyclerView.LayoutManager implements android.support.v7.widget.RecyclerView.SmoothScroller.ScrollVectorProvider {
    private static final java.lang.String TAG = "StaggeredGridLayoutManager";

    static final boolean DEBUG = false;

    public static final int HORIZONTAL = android.support.v7.widget.OrientationHelper.HORIZONTAL;

    public static final int VERTICAL = android.support.v7.widget.OrientationHelper.VERTICAL;

    /**
     * Does not do anything to hide gaps.
     */
    public static final int GAP_HANDLING_NONE = 0;

    /**
     *
     *
     * @deprecated No longer supported.
     */
    @java.lang.SuppressWarnings("unused")
    @java.lang.Deprecated
    public static final int GAP_HANDLING_LAZY = 1;

    /**
     * When scroll state is changed to {@link RecyclerView#SCROLL_STATE_IDLE}, StaggeredGrid will
     * check if there are gaps in the because of full span items. If it finds, it will re-layout
     * and move items to correct positions with animations.
     * <p>
     * For example, if LayoutManager ends up with the following layout due to adapter changes:
     * <pre>
     * AAA
     * _BC
     * DDD
     * </pre>
     * <p>
     * It will animate to the following state:
     * <pre>
     * AAA
     * BC_
     * DDD
     * </pre>
     */
    public static final int GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS = 2;

    static final int INVALID_OFFSET = java.lang.Integer.MIN_VALUE;

    /**
     * While trying to find next view to focus, LayoutManager will not try to scroll more
     * than this factor times the total space of the list. If layout is vertical, total space is the
     * height minus padding, if layout is horizontal, total space is the width minus padding.
     */
    private static final float MAX_SCROLL_FACTOR = 1 / 3.0F;

    /**
     * Number of spans
     */
    private int mSpanCount = -1;

    android.support.v7.widget.StaggeredGridLayoutManager.Span[] mSpans;

    /**
     * Primary orientation is the layout's orientation, secondary orientation is the orientation
     * for spans. Having both makes code much cleaner for calculations.
     */
    @android.support.annotation.NonNull
    android.support.v7.widget.OrientationHelper mPrimaryOrientation;

    @android.support.annotation.NonNull
    android.support.v7.widget.OrientationHelper mSecondaryOrientation;

    private int mOrientation;

    /**
     * The width or height per span, depending on the orientation.
     */
    private int mSizePerSpan;

    @android.support.annotation.NonNull
    private final android.support.v7.widget.LayoutState mLayoutState;

    boolean mReverseLayout = false;

    /**
     * Aggregated reverse layout value that takes RTL into account.
     */
    boolean mShouldReverseLayout = false;

    /**
     * Temporary variable used during fill method to check which spans needs to be filled.
     */
    private java.util.BitSet mRemainingSpans;

    /**
     * When LayoutManager needs to scroll to a position, it sets this variable and requests a
     * layout which will check this variable and re-layout accordingly.
     */
    int mPendingScrollPosition = android.support.v7.widget.RecyclerView.NO_POSITION;

    /**
     * Used to keep the offset value when {@link #scrollToPositionWithOffset(int, int)} is
     * called.
     */
    int mPendingScrollPositionOffset = android.support.v7.widget.StaggeredGridLayoutManager.INVALID_OFFSET;

    /**
     * Keeps the mapping between the adapter positions and spans. This is necessary to provide
     * a consistent experience when user scrolls the list.
     */
    android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup mLazySpanLookup = new android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup();

    /**
     * how we handle gaps in UI.
     */
    private int mGapStrategy = android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS;

    /**
     * Saved state needs this information to properly layout on restore.
     */
    private boolean mLastLayoutFromEnd;

    /**
     * Saved state and onLayout needs this information to re-layout properly
     */
    private boolean mLastLayoutRTL;

    /**
     * SavedState is not handled until a layout happens. This is where we keep it until next
     * layout.
     */
    private android.support.v7.widget.StaggeredGridLayoutManager.SavedState mPendingSavedState;

    /**
     * Re-used measurement specs. updated by onLayout.
     */
    private int mFullSizeSpec;

    /**
     * Re-used rectangle to get child decor offsets.
     */
    private final android.graphics.Rect mTmpRect = new android.graphics.Rect();

    /**
     * Re-used anchor info.
     */
    private final android.support.v7.widget.StaggeredGridLayoutManager.AnchorInfo mAnchorInfo = new android.support.v7.widget.StaggeredGridLayoutManager.AnchorInfo();

    /**
     * If a full span item is invalid / or created in reverse direction; it may create gaps in
     * the UI. While laying out, if such case is detected, we set this flag.
     * <p>
     * After scrolling stops, we check this flag and if it is set, re-layout.
     */
    private boolean mLaidOutInvalidFullSpan = false;

    /**
     * Works the same way as {@link android.widget.AbsListView#setSmoothScrollbarEnabled(boolean)}.
     * see {@link android.widget.AbsListView#setSmoothScrollbarEnabled(boolean)}
     */
    private boolean mSmoothScrollbarEnabled = true;

    private final java.lang.Runnable mCheckForGapsRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            checkForGaps();
        }
    };

    /**
     * Constructor used when layout manager is set in XML by RecyclerView attribute
     * "layoutManager". Defaults to single column and vertical.
     */
    @java.lang.SuppressWarnings("unused")
    public StaggeredGridLayoutManager(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        android.support.v7.widget.RecyclerView.LayoutManager.Properties properties = android.support.v7.widget.RecyclerView.LayoutManager.getProperties(context, attrs, defStyleAttr, defStyleRes);
        setOrientation(properties.orientation);
        setSpanCount(properties.spanCount);
        setReverseLayout(properties.reverseLayout);
        setAutoMeasureEnabled(mGapStrategy != android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mLayoutState = new android.support.v7.widget.LayoutState();
        createOrientationHelpers();
    }

    /**
     * Creates a StaggeredGridLayoutManager with given parameters.
     *
     * @param spanCount
     * 		If orientation is vertical, spanCount is number of columns. If
     * 		orientation is horizontal, spanCount is number of rows.
     * @param orientation
     * 		{@link #VERTICAL} or {@link #HORIZONTAL}
     */
    public StaggeredGridLayoutManager(int spanCount, int orientation) {
        mOrientation = orientation;
        setSpanCount(spanCount);
        setAutoMeasureEnabled(mGapStrategy != android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mLayoutState = new android.support.v7.widget.LayoutState();
        createOrientationHelpers();
    }

    private void createOrientationHelpers() {
        mPrimaryOrientation = android.support.v7.widget.OrientationHelper.createOrientationHelper(this, mOrientation);
        mSecondaryOrientation = android.support.v7.widget.OrientationHelper.createOrientationHelper(this, 1 - mOrientation);
    }

    /**
     * Checks for gaps in the UI that may be caused by adapter changes.
     * <p>
     * When a full span item is laid out in reverse direction, it sets a flag which we check when
     * scroll is stopped (or re-layout happens) and re-layout after first valid item.
     */
    boolean checkForGaps() {
        if (((getChildCount() == 0) || (mGapStrategy == android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE)) || (!isAttachedToWindow())) {
            return false;
        }
        final int minPos;
        final int maxPos;
        if (mShouldReverseLayout) {
            minPos = getLastChildPosition();
            maxPos = getFirstChildPosition();
        } else {
            minPos = getFirstChildPosition();
            maxPos = getLastChildPosition();
        }
        if (minPos == 0) {
            android.view.View gapView = hasGapsToFix();
            if (gapView != null) {
                mLazySpanLookup.clear();
                requestSimpleAnimationsInNextLayout();
                requestLayout();
                return true;
            }
        }
        if (!mLaidOutInvalidFullSpan) {
            return false;
        }
        int invalidGapDir = (mShouldReverseLayout) ? android.support.v7.widget.LayoutState.LAYOUT_START : android.support.v7.widget.LayoutState.LAYOUT_END;
        final android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem invalidFsi = mLazySpanLookup.getFirstFullSpanItemInRange(minPos, maxPos + 1, invalidGapDir, true);
        if (invalidFsi == null) {
            mLaidOutInvalidFullSpan = false;
            mLazySpanLookup.forceInvalidateAfter(maxPos + 1);
            return false;
        }
        final android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem validFsi = mLazySpanLookup.getFirstFullSpanItemInRange(minPos, invalidFsi.mPosition, invalidGapDir * (-1), true);
        if (validFsi == null) {
            mLazySpanLookup.forceInvalidateAfter(invalidFsi.mPosition);
        } else {
            mLazySpanLookup.forceInvalidateAfter(validFsi.mPosition + 1);
        }
        requestSimpleAnimationsInNextLayout();
        requestLayout();
        return true;
    }

    @java.lang.Override
    public void onScrollStateChanged(int state) {
        if (state == android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE) {
            checkForGaps();
        }
    }

    @java.lang.Override
    public void onDetachedFromWindow(android.support.v7.widget.RecyclerView view, android.support.v7.widget.RecyclerView.Recycler recycler) {
        removeCallbacks(mCheckForGapsRunnable);
        for (int i = 0; i < mSpanCount; i++) {
            mSpans[i].clear();
        }
        // SGLM will require fresh layout call to recover state after detach
        view.requestLayout();
    }

    /**
     * Checks for gaps if we've reached to the top of the list.
     * <p>
     * Intermediate gaps created by full span items are tracked via mLaidOutInvalidFullSpan field.
     */
    android.view.View hasGapsToFix() {
        int startChildIndex = 0;
        int endChildIndex = getChildCount() - 1;
        java.util.BitSet mSpansToCheck = new java.util.BitSet(mSpanCount);
        mSpansToCheck.set(0, mSpanCount, true);
        final int firstChildIndex;
        final int childLimit;
        final int preferredSpanDir = ((mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL) && isLayoutRTL()) ? 1 : -1;
        if (mShouldReverseLayout) {
            firstChildIndex = endChildIndex;
            childLimit = startChildIndex - 1;
        } else {
            firstChildIndex = startChildIndex;
            childLimit = endChildIndex + 1;
        }
        final int nextChildDiff = (firstChildIndex < childLimit) ? 1 : -1;
        for (int i = firstChildIndex; i != childLimit; i += nextChildDiff) {
            android.view.View child = getChildAt(i);
            android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams lp = ((android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) (child.getLayoutParams()));
            if (mSpansToCheck.get(lp.mSpan.mIndex)) {
                if (checkSpanForGap(lp.mSpan)) {
                    return child;
                }
                mSpansToCheck.clear(lp.mSpan.mIndex);
            }
            if (lp.mFullSpan) {
                continue;// quick reject

            }
            if ((i + nextChildDiff) != childLimit) {
                android.view.View nextChild = getChildAt(i + nextChildDiff);
                boolean compareSpans = false;
                if (mShouldReverseLayout) {
                    // ensure child's end is below nextChild's end
                    int myEnd = mPrimaryOrientation.getDecoratedEnd(child);
                    int nextEnd = mPrimaryOrientation.getDecoratedEnd(nextChild);
                    if (myEnd < nextEnd) {
                        return child;// i should have a better position

                    } else
                        if (myEnd == nextEnd) {
                            compareSpans = true;
                        }

                } else {
                    int myStart = mPrimaryOrientation.getDecoratedStart(child);
                    int nextStart = mPrimaryOrientation.getDecoratedStart(nextChild);
                    if (myStart > nextStart) {
                        return child;// i should have a better position

                    } else
                        if (myStart == nextStart) {
                            compareSpans = true;
                        }

                }
                if (compareSpans) {
                    // equal, check span indices.
                    android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams nextLp = ((android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) (nextChild.getLayoutParams()));
                    if (((lp.mSpan.mIndex - nextLp.mSpan.mIndex) < 0) != (preferredSpanDir < 0)) {
                        return child;
                    }
                }
            }
        }
        // everything looks good
        return null;
    }

    private boolean checkSpanForGap(android.support.v7.widget.StaggeredGridLayoutManager.Span span) {
        if (mShouldReverseLayout) {
            if (span.getEndLine() < mPrimaryOrientation.getEndAfterPadding()) {
                // if it is full span, it is OK
                final android.view.View endView = span.mViews.get(span.mViews.size() - 1);
                final android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams lp = span.getLayoutParams(endView);
                return !lp.mFullSpan;
            }
        } else
            if (span.getStartLine() > mPrimaryOrientation.getStartAfterPadding()) {
                // if it is full span, it is OK
                final android.view.View startView = span.mViews.get(0);
                final android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams lp = span.getLayoutParams(startView);
                return !lp.mFullSpan;
            }

        return false;
    }

    /**
     * Sets the number of spans for the layout. This will invalidate all of the span assignments
     * for Views.
     * <p>
     * Calling this method will automatically result in a new layout request unless the spanCount
     * parameter is equal to current span count.
     *
     * @param spanCount
     * 		Number of spans to layout
     */
    public void setSpanCount(int spanCount) {
        assertNotInLayoutOrScroll(null);
        if (spanCount != mSpanCount) {
            invalidateSpanAssignments();
            mSpanCount = spanCount;
            mRemainingSpans = new java.util.BitSet(mSpanCount);
            mSpans = new android.support.v7.widget.StaggeredGridLayoutManager.Span[mSpanCount];
            for (int i = 0; i < mSpanCount; i++) {
                mSpans[i] = new android.support.v7.widget.StaggeredGridLayoutManager.Span(i);
            }
            requestLayout();
        }
    }

    /**
     * Sets the orientation of the layout. StaggeredGridLayoutManager will do its best to keep
     * scroll position if this method is called after views are laid out.
     *
     * @param orientation
     * 		{@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setOrientation(int orientation) {
        if ((orientation != android.support.v7.widget.StaggeredGridLayoutManager.HORIZONTAL) && (orientation != android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL)) {
            throw new java.lang.IllegalArgumentException("invalid orientation.");
        }
        assertNotInLayoutOrScroll(null);
        if (orientation == mOrientation) {
            return;
        }
        mOrientation = orientation;
        android.support.v7.widget.OrientationHelper tmp = mPrimaryOrientation;
        mPrimaryOrientation = mSecondaryOrientation;
        mSecondaryOrientation = tmp;
        requestLayout();
    }

    /**
     * Sets whether LayoutManager should start laying out items from the end of the UI. The order
     * items are traversed is not affected by this call.
     * <p>
     * For vertical layout, if it is set to <code>true</code>, first item will be at the bottom of
     * the list.
     * <p>
     * For horizontal layouts, it depends on the layout direction.
     * When set to true, If {@link RecyclerView} is LTR, than it will layout from RTL, if
     * {@link RecyclerView}} is RTL, it will layout from LTR.
     *
     * @param reverseLayout
     * 		Whether layout should be in reverse or not
     */
    public void setReverseLayout(boolean reverseLayout) {
        assertNotInLayoutOrScroll(null);
        if ((mPendingSavedState != null) && (mPendingSavedState.mReverseLayout != reverseLayout)) {
            mPendingSavedState.mReverseLayout = reverseLayout;
        }
        mReverseLayout = reverseLayout;
        requestLayout();
    }

    /**
     * Returns the current gap handling strategy for StaggeredGridLayoutManager.
     * <p>
     * Staggered grid may have gaps in the layout due to changes in the adapter. To avoid gaps,
     * StaggeredGridLayoutManager provides 2 options. Check {@link #GAP_HANDLING_NONE} and
     * {@link #GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS} for details.
     * <p>
     * By default, StaggeredGridLayoutManager uses {@link #GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS}.
     *
     * @return Current gap handling strategy.
     * @see #setGapStrategy(int)
     * @see #GAP_HANDLING_NONE
     * @see #GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
     */
    public int getGapStrategy() {
        return mGapStrategy;
    }

    /**
     * Sets the gap handling strategy for StaggeredGridLayoutManager. If the gapStrategy parameter
     * is different than the current strategy, calling this method will trigger a layout request.
     *
     * @param gapStrategy
     * 		The new gap handling strategy. Should be
     * 		{@link #GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS} or {@link #GAP_HANDLING_NONE}.
     * @see #getGapStrategy()
     */
    public void setGapStrategy(int gapStrategy) {
        assertNotInLayoutOrScroll(null);
        if (gapStrategy == mGapStrategy) {
            return;
        }
        if ((gapStrategy != android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE) && (gapStrategy != android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS)) {
            throw new java.lang.IllegalArgumentException("invalid gap strategy. Must be GAP_HANDLING_NONE " + "or GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS");
        }
        mGapStrategy = gapStrategy;
        setAutoMeasureEnabled(mGapStrategy != android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        requestLayout();
    }

    @java.lang.Override
    public void assertNotInLayoutOrScroll(java.lang.String message) {
        if (mPendingSavedState == null) {
            super.assertNotInLayoutOrScroll(message);
        }
    }

    /**
     * Returns the number of spans laid out by StaggeredGridLayoutManager.
     *
     * @return Number of spans in the layout
     */
    public int getSpanCount() {
        return mSpanCount;
    }

    /**
     * For consistency, StaggeredGridLayoutManager keeps a mapping between spans and items.
     * <p>
     * If you need to cancel current assignments, you can call this method which will clear all
     * assignments and request a new layout.
     */
    public void invalidateSpanAssignments() {
        mLazySpanLookup.clear();
        requestLayout();
    }

    /**
     * Calculates the views' layout order. (e.g. from end to start or start to end)
     * RTL layout support is applied automatically. So if layout is RTL and
     * {@link #getReverseLayout()} is {@code true}, elements will be laid out starting from left.
     */
    private void resolveShouldLayoutReverse() {
        // A == B is the same result, but we rather keep it readable
        if ((mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL) || (!isLayoutRTL())) {
            mShouldReverseLayout = mReverseLayout;
        } else {
            mShouldReverseLayout = !mReverseLayout;
        }
    }

    boolean isLayoutRTL() {
        return getLayoutDirection() == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    /**
     * Returns whether views are laid out in reverse order or not.
     * <p>
     * Not that this value is not affected by RecyclerView's layout direction.
     *
     * @return True if layout is reversed, false otherwise
     * @see #setReverseLayout(boolean)
     */
    public boolean getReverseLayout() {
        return mReverseLayout;
    }

    @java.lang.Override
    public void setMeasuredDimension(android.graphics.Rect childrenBounds, int wSpec, int hSpec) {
        final int width;
        // we don't like it to wrap content in our non-scroll direction.
        final int height;
        final int horizontalPadding = getPaddingLeft() + getPaddingRight();
        final int verticalPadding = getPaddingTop() + getPaddingBottom();
        if (mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL) {
            final int usedHeight = childrenBounds.height() + verticalPadding;
            height = android.support.v7.widget.RecyclerView.LayoutManager.chooseSize(hSpec, usedHeight, getMinimumHeight());
            width = android.support.v7.widget.RecyclerView.LayoutManager.chooseSize(wSpec, (mSizePerSpan * mSpanCount) + horizontalPadding, getMinimumWidth());
        } else {
            final int usedWidth = childrenBounds.width() + horizontalPadding;
            width = android.support.v7.widget.RecyclerView.LayoutManager.chooseSize(wSpec, usedWidth, getMinimumWidth());
            height = android.support.v7.widget.RecyclerView.LayoutManager.chooseSize(hSpec, (mSizePerSpan * mSpanCount) + verticalPadding, getMinimumHeight());
        }
        setMeasuredDimension(width, height);
    }

    @java.lang.Override
    public void onLayoutChildren(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        onLayoutChildren(recycler, state, true);
    }

    private void onLayoutChildren(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, boolean shouldCheckForGaps) {
        final android.support.v7.widget.StaggeredGridLayoutManager.AnchorInfo anchorInfo = mAnchorInfo;
        if ((mPendingSavedState != null) || (mPendingScrollPosition != android.support.v7.widget.RecyclerView.NO_POSITION)) {
            if (state.getItemCount() == 0) {
                removeAndRecycleAllViews(recycler);
                anchorInfo.reset();
                return;
            }
        }
        boolean recalculateAnchor = ((!anchorInfo.mValid) || (mPendingScrollPosition != android.support.v7.widget.RecyclerView.NO_POSITION)) || (mPendingSavedState != null);
        if (recalculateAnchor) {
            anchorInfo.reset();
            if (mPendingSavedState != null) {
                applyPendingSavedState(anchorInfo);
            } else {
                resolveShouldLayoutReverse();
                anchorInfo.mLayoutFromEnd = mShouldReverseLayout;
            }
            updateAnchorInfoForLayout(state, anchorInfo);
            anchorInfo.mValid = true;
        }
        if ((mPendingSavedState == null) && (mPendingScrollPosition == android.support.v7.widget.RecyclerView.NO_POSITION)) {
            if ((anchorInfo.mLayoutFromEnd != mLastLayoutFromEnd) || (isLayoutRTL() != mLastLayoutRTL)) {
                mLazySpanLookup.clear();
                anchorInfo.mInvalidateOffsets = true;
            }
        }
        if ((getChildCount() > 0) && ((mPendingSavedState == null) || (mPendingSavedState.mSpanOffsetsSize < 1))) {
            if (anchorInfo.mInvalidateOffsets) {
                for (int i = 0; i < mSpanCount; i++) {
                    // Scroll to position is set, clear.
                    mSpans[i].clear();
                    if (anchorInfo.mOffset != android.support.v7.widget.StaggeredGridLayoutManager.INVALID_OFFSET) {
                        mSpans[i].setLine(anchorInfo.mOffset);
                    }
                }
            } else {
                if (recalculateAnchor || (mAnchorInfo.mSpanReferenceLines == null)) {
                    for (int i = 0; i < mSpanCount; i++) {
                        mSpans[i].cacheReferenceLineAndClear(mShouldReverseLayout, anchorInfo.mOffset);
                    }
                    mAnchorInfo.saveSpanReferenceLines(mSpans);
                } else {
                    for (int i = 0; i < mSpanCount; i++) {
                        final android.support.v7.widget.StaggeredGridLayoutManager.Span span = mSpans[i];
                        span.clear();
                        span.setLine(mAnchorInfo.mSpanReferenceLines[i]);
                    }
                }
            }
        }
        detachAndScrapAttachedViews(recycler);
        mLayoutState.mRecycle = false;
        mLaidOutInvalidFullSpan = false;
        updateMeasureSpecs(mSecondaryOrientation.getTotalSpace());
        updateLayoutState(anchorInfo.mPosition, state);
        if (anchorInfo.mLayoutFromEnd) {
            // Layout start.
            setLayoutStateDirection(android.support.v7.widget.LayoutState.LAYOUT_START);
            fill(recycler, mLayoutState, state);
            // Layout end.
            setLayoutStateDirection(android.support.v7.widget.LayoutState.LAYOUT_END);
            mLayoutState.mCurrentPosition = anchorInfo.mPosition + mLayoutState.mItemDirection;
            fill(recycler, mLayoutState, state);
        } else {
            // Layout end.
            setLayoutStateDirection(android.support.v7.widget.LayoutState.LAYOUT_END);
            fill(recycler, mLayoutState, state);
            // Layout start.
            setLayoutStateDirection(android.support.v7.widget.LayoutState.LAYOUT_START);
            mLayoutState.mCurrentPosition = anchorInfo.mPosition + mLayoutState.mItemDirection;
            fill(recycler, mLayoutState, state);
        }
        repositionToWrapContentIfNecessary();
        if (getChildCount() > 0) {
            if (mShouldReverseLayout) {
                fixEndGap(recycler, state, true);
                fixStartGap(recycler, state, false);
            } else {
                fixStartGap(recycler, state, true);
                fixEndGap(recycler, state, false);
            }
        }
        boolean hasGaps = false;
        if (shouldCheckForGaps && (!state.isPreLayout())) {
            final boolean needToCheckForGaps = ((mGapStrategy != android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE) && (getChildCount() > 0)) && (mLaidOutInvalidFullSpan || (hasGapsToFix() != null));
            if (needToCheckForGaps) {
                removeCallbacks(mCheckForGapsRunnable);
                if (checkForGaps()) {
                    hasGaps = true;
                }
            }
        }
        if (state.isPreLayout()) {
            mAnchorInfo.reset();
        }
        mLastLayoutFromEnd = anchorInfo.mLayoutFromEnd;
        mLastLayoutRTL = isLayoutRTL();
        if (hasGaps) {
            mAnchorInfo.reset();
            onLayoutChildren(recycler, state, false);
        }
    }

    @java.lang.Override
    public void onLayoutCompleted(android.support.v7.widget.RecyclerView.State state) {
        super.onLayoutCompleted(state);
        mPendingScrollPosition = android.support.v7.widget.RecyclerView.NO_POSITION;
        mPendingScrollPositionOffset = android.support.v7.widget.StaggeredGridLayoutManager.INVALID_OFFSET;
        mPendingSavedState = null;// we don't need this anymore

        mAnchorInfo.reset();
    }

    private void repositionToWrapContentIfNecessary() {
        if (mSecondaryOrientation.getMode() == android.view.View.MeasureSpec.EXACTLY) {
            return;// nothing to do

        }
        float maxSize = 0;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            android.view.View child = getChildAt(i);
            float size = mSecondaryOrientation.getDecoratedMeasurement(child);
            if (size < maxSize) {
                continue;
            }
            android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams layoutParams = ((android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) (child.getLayoutParams()));
            if (layoutParams.isFullSpan()) {
                size = (1.0F * size) / mSpanCount;
            }
            maxSize = java.lang.Math.max(maxSize, size);
        }
        int before = mSizePerSpan;
        int desired = java.lang.Math.round(maxSize * mSpanCount);
        if (mSecondaryOrientation.getMode() == android.view.View.MeasureSpec.AT_MOST) {
            desired = java.lang.Math.min(desired, mSecondaryOrientation.getTotalSpace());
        }
        updateMeasureSpecs(desired);
        if (mSizePerSpan == before) {
            return;// nothing has changed

        }
        for (int i = 0; i < childCount; i++) {
            android.view.View child = getChildAt(i);
            final android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams lp = ((android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) (child.getLayoutParams()));
            if (lp.mFullSpan) {
                continue;
            }
            if (isLayoutRTL() && (mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL)) {
                int newOffset = (-((mSpanCount - 1) - lp.mSpan.mIndex)) * mSizePerSpan;
                int prevOffset = (-((mSpanCount - 1) - lp.mSpan.mIndex)) * before;
                child.offsetLeftAndRight(newOffset - prevOffset);
            } else {
                int newOffset = lp.mSpan.mIndex * mSizePerSpan;
                int prevOffset = lp.mSpan.mIndex * before;
                if (mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL) {
                    child.offsetLeftAndRight(newOffset - prevOffset);
                } else {
                    child.offsetTopAndBottom(newOffset - prevOffset);
                }
            }
        }
    }

    private void applyPendingSavedState(android.support.v7.widget.StaggeredGridLayoutManager.AnchorInfo anchorInfo) {
        if (android.support.v7.widget.StaggeredGridLayoutManager.DEBUG) {
            android.util.Log.d(android.support.v7.widget.StaggeredGridLayoutManager.TAG, "found saved state: " + mPendingSavedState);
        }
        if (mPendingSavedState.mSpanOffsetsSize > 0) {
            if (mPendingSavedState.mSpanOffsetsSize == mSpanCount) {
                for (int i = 0; i < mSpanCount; i++) {
                    mSpans[i].clear();
                    int line = mPendingSavedState.mSpanOffsets[i];
                    if (line != android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE) {
                        if (mPendingSavedState.mAnchorLayoutFromEnd) {
                            line += mPrimaryOrientation.getEndAfterPadding();
                        } else {
                            line += mPrimaryOrientation.getStartAfterPadding();
                        }
                    }
                    mSpans[i].setLine(line);
                }
            } else {
                mPendingSavedState.invalidateSpanInfo();
                mPendingSavedState.mAnchorPosition = mPendingSavedState.mVisibleAnchorPosition;
            }
        }
        mLastLayoutRTL = mPendingSavedState.mLastLayoutRTL;
        setReverseLayout(mPendingSavedState.mReverseLayout);
        resolveShouldLayoutReverse();
        if (mPendingSavedState.mAnchorPosition != android.support.v7.widget.RecyclerView.NO_POSITION) {
            mPendingScrollPosition = mPendingSavedState.mAnchorPosition;
            anchorInfo.mLayoutFromEnd = mPendingSavedState.mAnchorLayoutFromEnd;
        } else {
            anchorInfo.mLayoutFromEnd = mShouldReverseLayout;
        }
        if (mPendingSavedState.mSpanLookupSize > 1) {
            mLazySpanLookup.mData = mPendingSavedState.mSpanLookup;
            mLazySpanLookup.mFullSpanItems = mPendingSavedState.mFullSpanItems;
        }
    }

    void updateAnchorInfoForLayout(android.support.v7.widget.RecyclerView.State state, android.support.v7.widget.StaggeredGridLayoutManager.AnchorInfo anchorInfo) {
        if (updateAnchorFromPendingData(state, anchorInfo)) {
            return;
        }
        if (updateAnchorFromChildren(state, anchorInfo)) {
            return;
        }
        if (android.support.v7.widget.StaggeredGridLayoutManager.DEBUG) {
            android.util.Log.d(android.support.v7.widget.StaggeredGridLayoutManager.TAG, "Deciding anchor info from fresh state");
        }
        anchorInfo.assignCoordinateFromPadding();
        anchorInfo.mPosition = 0;
    }

    private boolean updateAnchorFromChildren(android.support.v7.widget.RecyclerView.State state, android.support.v7.widget.StaggeredGridLayoutManager.AnchorInfo anchorInfo) {
        // We don't recycle views out of adapter order. This way, we can rely on the first or
        // last child as the anchor position.
        // Layout direction may change but we should select the child depending on the latest
        // layout direction. Otherwise, we'll choose the wrong child.
        anchorInfo.mPosition = (mLastLayoutFromEnd) ? findLastReferenceChildPosition(state.getItemCount()) : findFirstReferenceChildPosition(state.getItemCount());
        anchorInfo.mOffset = android.support.v7.widget.StaggeredGridLayoutManager.INVALID_OFFSET;
        return true;
    }

    boolean updateAnchorFromPendingData(android.support.v7.widget.RecyclerView.State state, android.support.v7.widget.StaggeredGridLayoutManager.AnchorInfo anchorInfo) {
        // Validate scroll position if exists.
        if (state.isPreLayout() || (mPendingScrollPosition == android.support.v7.widget.RecyclerView.NO_POSITION)) {
            return false;
        }
        // Validate it.
        if ((mPendingScrollPosition < 0) || (mPendingScrollPosition >= state.getItemCount())) {
            mPendingScrollPosition = android.support.v7.widget.RecyclerView.NO_POSITION;
            mPendingScrollPositionOffset = android.support.v7.widget.StaggeredGridLayoutManager.INVALID_OFFSET;
            return false;
        }
        if (((mPendingSavedState == null) || (mPendingSavedState.mAnchorPosition == android.support.v7.widget.RecyclerView.NO_POSITION)) || (mPendingSavedState.mSpanOffsetsSize < 1)) {
            // If item is visible, make it fully visible.
            final android.view.View child = findViewByPosition(mPendingScrollPosition);
            if (child != null) {
                // Use regular anchor position, offset according to pending offset and target
                // child
                anchorInfo.mPosition = (mShouldReverseLayout) ? getLastChildPosition() : getFirstChildPosition();
                if (mPendingScrollPositionOffset != android.support.v7.widget.StaggeredGridLayoutManager.INVALID_OFFSET) {
                    if (anchorInfo.mLayoutFromEnd) {
                        final int target = mPrimaryOrientation.getEndAfterPadding() - mPendingScrollPositionOffset;
                        anchorInfo.mOffset = target - mPrimaryOrientation.getDecoratedEnd(child);
                    } else {
                        final int target = mPrimaryOrientation.getStartAfterPadding() + mPendingScrollPositionOffset;
                        anchorInfo.mOffset = target - mPrimaryOrientation.getDecoratedStart(child);
                    }
                    return true;
                }
                // no offset provided. Decide according to the child location
                final int childSize = mPrimaryOrientation.getDecoratedMeasurement(child);
                if (childSize > mPrimaryOrientation.getTotalSpace()) {
                    // Item does not fit. Fix depending on layout direction.
                    anchorInfo.mOffset = (anchorInfo.mLayoutFromEnd) ? mPrimaryOrientation.getEndAfterPadding() : mPrimaryOrientation.getStartAfterPadding();
                    return true;
                }
                final int startGap = mPrimaryOrientation.getDecoratedStart(child) - mPrimaryOrientation.getStartAfterPadding();
                if (startGap < 0) {
                    anchorInfo.mOffset = -startGap;
                    return true;
                }
                final int endGap = mPrimaryOrientation.getEndAfterPadding() - mPrimaryOrientation.getDecoratedEnd(child);
                if (endGap < 0) {
                    anchorInfo.mOffset = endGap;
                    return true;
                }
                // child already visible. just layout as usual
                anchorInfo.mOffset = android.support.v7.widget.StaggeredGridLayoutManager.INVALID_OFFSET;
            } else {
                // Child is not visible. Set anchor coordinate depending on in which direction
                // child will be visible.
                anchorInfo.mPosition = mPendingScrollPosition;
                if (mPendingScrollPositionOffset == android.support.v7.widget.StaggeredGridLayoutManager.INVALID_OFFSET) {
                    final int position = calculateScrollDirectionForPosition(anchorInfo.mPosition);
                    anchorInfo.mLayoutFromEnd = position == android.support.v7.widget.LayoutState.LAYOUT_END;
                    anchorInfo.assignCoordinateFromPadding();
                } else {
                    anchorInfo.assignCoordinateFromPadding(mPendingScrollPositionOffset);
                }
                anchorInfo.mInvalidateOffsets = true;
            }
        } else {
            anchorInfo.mOffset = android.support.v7.widget.StaggeredGridLayoutManager.INVALID_OFFSET;
            anchorInfo.mPosition = mPendingScrollPosition;
        }
        return true;
    }

    void updateMeasureSpecs(int totalSpace) {
        mSizePerSpan = totalSpace / mSpanCount;
        // noinspection ResourceType
        mFullSizeSpec = android.view.View.MeasureSpec.makeMeasureSpec(totalSpace, mSecondaryOrientation.getMode());
    }

    @java.lang.Override
    public boolean supportsPredictiveItemAnimations() {
        return mPendingSavedState == null;
    }

    /**
     * Returns the adapter position of the first visible view for each span.
     * <p>
     * Note that, this value is not affected by layout orientation or item order traversal.
     * ({@link #setReverseLayout(boolean)}). Views are sorted by their positions in the adapter,
     * not in the layout.
     * <p>
     * If RecyclerView has item decorators, they will be considered in calculations as well.
     * <p>
     * StaggeredGridLayoutManager may pre-cache some views that are not necessarily visible. Those
     * views are ignored in this method.
     *
     * @param into
     * 		An array to put the results into. If you don't provide any, LayoutManager will
     * 		create a new one.
     * @return The adapter position of the first visible item in each span. If a span does not have
    any items, {@link RecyclerView#NO_POSITION} is returned for that span.
     * @see #findFirstCompletelyVisibleItemPositions(int[])
     * @see #findLastVisibleItemPositions(int[])
     */
    public int[] findFirstVisibleItemPositions(int[] into) {
        if (into == null) {
            into = new int[mSpanCount];
        } else
            if (into.length < mSpanCount) {
                throw new java.lang.IllegalArgumentException(((("Provided int[]'s size must be more than or equal" + " to span count. Expected:") + mSpanCount) + ", array size:") + into.length);
            }

        for (int i = 0; i < mSpanCount; i++) {
            into[i] = mSpans[i].findFirstVisibleItemPosition();
        }
        return into;
    }

    /**
     * Returns the adapter position of the first completely visible view for each span.
     * <p>
     * Note that, this value is not affected by layout orientation or item order traversal.
     * ({@link #setReverseLayout(boolean)}). Views are sorted by their positions in the adapter,
     * not in the layout.
     * <p>
     * If RecyclerView has item decorators, they will be considered in calculations as well.
     * <p>
     * StaggeredGridLayoutManager may pre-cache some views that are not necessarily visible. Those
     * views are ignored in this method.
     *
     * @param into
     * 		An array to put the results into. If you don't provide any, LayoutManager will
     * 		create a new one.
     * @return The adapter position of the first fully visible item in each span. If a span does
    not have any items, {@link RecyclerView#NO_POSITION} is returned for that span.
     * @see #findFirstVisibleItemPositions(int[])
     * @see #findLastCompletelyVisibleItemPositions(int[])
     */
    public int[] findFirstCompletelyVisibleItemPositions(int[] into) {
        if (into == null) {
            into = new int[mSpanCount];
        } else
            if (into.length < mSpanCount) {
                throw new java.lang.IllegalArgumentException(((("Provided int[]'s size must be more than or equal" + " to span count. Expected:") + mSpanCount) + ", array size:") + into.length);
            }

        for (int i = 0; i < mSpanCount; i++) {
            into[i] = mSpans[i].findFirstCompletelyVisibleItemPosition();
        }
        return into;
    }

    /**
     * Returns the adapter position of the last visible view for each span.
     * <p>
     * Note that, this value is not affected by layout orientation or item order traversal.
     * ({@link #setReverseLayout(boolean)}). Views are sorted by their positions in the adapter,
     * not in the layout.
     * <p>
     * If RecyclerView has item decorators, they will be considered in calculations as well.
     * <p>
     * StaggeredGridLayoutManager may pre-cache some views that are not necessarily visible. Those
     * views are ignored in this method.
     *
     * @param into
     * 		An array to put the results into. If you don't provide any, LayoutManager will
     * 		create a new one.
     * @return The adapter position of the last visible item in each span. If a span does not have
    any items, {@link RecyclerView#NO_POSITION} is returned for that span.
     * @see #findLastCompletelyVisibleItemPositions(int[])
     * @see #findFirstVisibleItemPositions(int[])
     */
    public int[] findLastVisibleItemPositions(int[] into) {
        if (into == null) {
            into = new int[mSpanCount];
        } else
            if (into.length < mSpanCount) {
                throw new java.lang.IllegalArgumentException(((("Provided int[]'s size must be more than or equal" + " to span count. Expected:") + mSpanCount) + ", array size:") + into.length);
            }

        for (int i = 0; i < mSpanCount; i++) {
            into[i] = mSpans[i].findLastVisibleItemPosition();
        }
        return into;
    }

    /**
     * Returns the adapter position of the last completely visible view for each span.
     * <p>
     * Note that, this value is not affected by layout orientation or item order traversal.
     * ({@link #setReverseLayout(boolean)}). Views are sorted by their positions in the adapter,
     * not in the layout.
     * <p>
     * If RecyclerView has item decorators, they will be considered in calculations as well.
     * <p>
     * StaggeredGridLayoutManager may pre-cache some views that are not necessarily visible. Those
     * views are ignored in this method.
     *
     * @param into
     * 		An array to put the results into. If you don't provide any, LayoutManager will
     * 		create a new one.
     * @return The adapter position of the last fully visible item in each span. If a span does not
    have any items, {@link RecyclerView#NO_POSITION} is returned for that span.
     * @see #findFirstCompletelyVisibleItemPositions(int[])
     * @see #findLastVisibleItemPositions(int[])
     */
    public int[] findLastCompletelyVisibleItemPositions(int[] into) {
        if (into == null) {
            into = new int[mSpanCount];
        } else
            if (into.length < mSpanCount) {
                throw new java.lang.IllegalArgumentException(((("Provided int[]'s size must be more than or equal" + " to span count. Expected:") + mSpanCount) + ", array size:") + into.length);
            }

        for (int i = 0; i < mSpanCount; i++) {
            into[i] = mSpans[i].findLastCompletelyVisibleItemPosition();
        }
        return into;
    }

    @java.lang.Override
    public int computeHorizontalScrollOffset(android.support.v7.widget.RecyclerView.State state) {
        return computeScrollOffset(state);
    }

    private int computeScrollOffset(android.support.v7.widget.RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        return android.support.v7.widget.ScrollbarHelper.computeScrollOffset(state, mPrimaryOrientation, findFirstVisibleItemClosestToStart(!mSmoothScrollbarEnabled), findFirstVisibleItemClosestToEnd(!mSmoothScrollbarEnabled), this, mSmoothScrollbarEnabled, mShouldReverseLayout);
    }

    @java.lang.Override
    public int computeVerticalScrollOffset(android.support.v7.widget.RecyclerView.State state) {
        return computeScrollOffset(state);
    }

    @java.lang.Override
    public int computeHorizontalScrollExtent(android.support.v7.widget.RecyclerView.State state) {
        return computeScrollExtent(state);
    }

    private int computeScrollExtent(android.support.v7.widget.RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        return android.support.v7.widget.ScrollbarHelper.computeScrollExtent(state, mPrimaryOrientation, findFirstVisibleItemClosestToStart(!mSmoothScrollbarEnabled), findFirstVisibleItemClosestToEnd(!mSmoothScrollbarEnabled), this, mSmoothScrollbarEnabled);
    }

    @java.lang.Override
    public int computeVerticalScrollExtent(android.support.v7.widget.RecyclerView.State state) {
        return computeScrollExtent(state);
    }

    @java.lang.Override
    public int computeHorizontalScrollRange(android.support.v7.widget.RecyclerView.State state) {
        return computeScrollRange(state);
    }

    private int computeScrollRange(android.support.v7.widget.RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        return android.support.v7.widget.ScrollbarHelper.computeScrollRange(state, mPrimaryOrientation, findFirstVisibleItemClosestToStart(!mSmoothScrollbarEnabled), findFirstVisibleItemClosestToEnd(!mSmoothScrollbarEnabled), this, mSmoothScrollbarEnabled);
    }

    @java.lang.Override
    public int computeVerticalScrollRange(android.support.v7.widget.RecyclerView.State state) {
        return computeScrollRange(state);
    }

    private void measureChildWithDecorationsAndMargin(android.view.View child, android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams lp, boolean alreadyMeasured) {
        if (lp.mFullSpan) {
            if (mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL) {
                measureChildWithDecorationsAndMargin(child, mFullSizeSpec, android.support.v7.widget.RecyclerView.LayoutManager.getChildMeasureSpec(getHeight(), getHeightMode(), 0, lp.height, true), alreadyMeasured);
            } else {
                measureChildWithDecorationsAndMargin(child, android.support.v7.widget.RecyclerView.LayoutManager.getChildMeasureSpec(getWidth(), getWidthMode(), 0, lp.width, true), mFullSizeSpec, alreadyMeasured);
            }
        } else {
            if (mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL) {
                measureChildWithDecorationsAndMargin(child, android.support.v7.widget.RecyclerView.LayoutManager.getChildMeasureSpec(mSizePerSpan, getWidthMode(), 0, lp.width, false), android.support.v7.widget.RecyclerView.LayoutManager.getChildMeasureSpec(getHeight(), getHeightMode(), 0, lp.height, true), alreadyMeasured);
            } else {
                measureChildWithDecorationsAndMargin(child, android.support.v7.widget.RecyclerView.LayoutManager.getChildMeasureSpec(getWidth(), getWidthMode(), 0, lp.width, true), android.support.v7.widget.RecyclerView.LayoutManager.getChildMeasureSpec(mSizePerSpan, getHeightMode(), 0, lp.height, false), alreadyMeasured);
            }
        }
    }

    private void measureChildWithDecorationsAndMargin(android.view.View child, int widthSpec, int heightSpec, boolean alreadyMeasured) {
        calculateItemDecorationsForChild(child, mTmpRect);
        android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams lp = ((android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) (child.getLayoutParams()));
        widthSpec = updateSpecWithExtra(widthSpec, lp.leftMargin + mTmpRect.left, lp.rightMargin + mTmpRect.right);
        heightSpec = updateSpecWithExtra(heightSpec, lp.topMargin + mTmpRect.top, lp.bottomMargin + mTmpRect.bottom);
        final boolean measure = (alreadyMeasured) ? shouldReMeasureChild(child, widthSpec, heightSpec, lp) : shouldMeasureChild(child, widthSpec, heightSpec, lp);
        if (measure) {
            child.measure(widthSpec, heightSpec);
        }
    }

    private int updateSpecWithExtra(int spec, int startInset, int endInset) {
        if ((startInset == 0) && (endInset == 0)) {
            return spec;
        }
        final int mode = android.view.View.MeasureSpec.getMode(spec);
        if ((mode == android.view.View.MeasureSpec.AT_MOST) || (mode == android.view.View.MeasureSpec.EXACTLY)) {
            return android.view.View.MeasureSpec.makeMeasureSpec(java.lang.Math.max(0, (android.view.View.MeasureSpec.getSize(spec) - startInset) - endInset), mode);
        }
        return spec;
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
        if (state instanceof android.support.v7.widget.StaggeredGridLayoutManager.SavedState) {
            mPendingSavedState = ((android.support.v7.widget.StaggeredGridLayoutManager.SavedState) (state));
            requestLayout();
        } else
            if (android.support.v7.widget.StaggeredGridLayoutManager.DEBUG) {
                android.util.Log.d(android.support.v7.widget.StaggeredGridLayoutManager.TAG, "invalid saved state class");
            }

    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState() {
        if (mPendingSavedState != null) {
            return new android.support.v7.widget.StaggeredGridLayoutManager.SavedState(mPendingSavedState);
        }
        android.support.v7.widget.StaggeredGridLayoutManager.SavedState state = new android.support.v7.widget.StaggeredGridLayoutManager.SavedState();
        state.mReverseLayout = mReverseLayout;
        state.mAnchorLayoutFromEnd = mLastLayoutFromEnd;
        state.mLastLayoutRTL = mLastLayoutRTL;
        if ((mLazySpanLookup != null) && (mLazySpanLookup.mData != null)) {
            state.mSpanLookup = mLazySpanLookup.mData;
            state.mSpanLookupSize = state.mSpanLookup.length;
            state.mFullSpanItems = mLazySpanLookup.mFullSpanItems;
        } else {
            state.mSpanLookupSize = 0;
        }
        if (getChildCount() > 0) {
            state.mAnchorPosition = (mLastLayoutFromEnd) ? getLastChildPosition() : getFirstChildPosition();
            state.mVisibleAnchorPosition = findFirstVisibleItemPositionInt();
            state.mSpanOffsetsSize = mSpanCount;
            state.mSpanOffsets = new int[mSpanCount];
            for (int i = 0; i < mSpanCount; i++) {
                int line;
                if (mLastLayoutFromEnd) {
                    line = mSpans[i].getEndLine(android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE);
                    if (line != android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE) {
                        line -= mPrimaryOrientation.getEndAfterPadding();
                    }
                } else {
                    line = mSpans[i].getStartLine(android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE);
                    if (line != android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE) {
                        line -= mPrimaryOrientation.getStartAfterPadding();
                    }
                }
                state.mSpanOffsets[i] = line;
            }
        } else {
            state.mAnchorPosition = android.support.v7.widget.RecyclerView.NO_POSITION;
            state.mVisibleAnchorPosition = android.support.v7.widget.RecyclerView.NO_POSITION;
            state.mSpanOffsetsSize = 0;
        }
        if (android.support.v7.widget.StaggeredGridLayoutManager.DEBUG) {
            android.util.Log.d(android.support.v7.widget.StaggeredGridLayoutManager.TAG, "saved state:\n" + state);
        }
        return state;
    }

    @java.lang.Override
    public void onInitializeAccessibilityNodeInfoForItem(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, android.view.View host, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
        android.view.ViewGroup.LayoutParams lp = host.getLayoutParams();
        if (!(lp instanceof android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams)) {
            super.onInitializeAccessibilityNodeInfoForItem(host, info);
            return;
        }
        android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams sglp = ((android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) (lp));
        if (mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.HORIZONTAL) {
            info.setCollectionItemInfo(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(sglp.getSpanIndex(), sglp.mFullSpan ? mSpanCount : 1, -1, -1, sglp.mFullSpan, false));
        } else {
            // VERTICAL
            info.setCollectionItemInfo(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(-1, -1, sglp.getSpanIndex(), sglp.mFullSpan ? mSpanCount : 1, sglp.mFullSpan, false));
        }
    }

    @java.lang.Override
    public void onInitializeAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        if (getChildCount() > 0) {
            final android.support.v4.view.accessibility.AccessibilityRecordCompat record = android.support.v4.view.accessibility.AccessibilityEventCompat.asRecord(event);
            final android.view.View start = findFirstVisibleItemClosestToStart(false);
            final android.view.View end = findFirstVisibleItemClosestToEnd(false);
            if ((start == null) || (end == null)) {
                return;
            }
            final int startPos = getPosition(start);
            final int endPos = getPosition(end);
            if (startPos < endPos) {
                record.setFromIndex(startPos);
                record.setToIndex(endPos);
            } else {
                record.setFromIndex(endPos);
                record.setToIndex(startPos);
            }
        }
    }

    /**
     * Finds the first fully visible child to be used as an anchor child if span count changes when
     * state is restored. If no children is fully visible, returns a partially visible child instead
     * of returning null.
     */
    int findFirstVisibleItemPositionInt() {
        final android.view.View first = (mShouldReverseLayout) ? findFirstVisibleItemClosestToEnd(true) : findFirstVisibleItemClosestToStart(true);
        return first == null ? android.support.v7.widget.RecyclerView.NO_POSITION : getPosition(first);
    }

    @java.lang.Override
    public int getRowCountForAccessibility(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        if (mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.HORIZONTAL) {
            return mSpanCount;
        }
        return super.getRowCountForAccessibility(recycler, state);
    }

    @java.lang.Override
    public int getColumnCountForAccessibility(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        if (mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL) {
            return mSpanCount;
        }
        return super.getColumnCountForAccessibility(recycler, state);
    }

    /**
     * This is for internal use. Not necessarily the child closest to start but the first child
     * we find that matches the criteria.
     * This method does not do any sorting based on child's start coordinate, instead, it uses
     * children order.
     */
    android.view.View findFirstVisibleItemClosestToStart(boolean fullyVisible) {
        final int boundsStart = mPrimaryOrientation.getStartAfterPadding();
        final int boundsEnd = mPrimaryOrientation.getEndAfterPadding();
        final int limit = getChildCount();
        android.view.View partiallyVisible = null;
        for (int i = 0; i < limit; i++) {
            final android.view.View child = getChildAt(i);
            final int childStart = mPrimaryOrientation.getDecoratedStart(child);
            final int childEnd = mPrimaryOrientation.getDecoratedEnd(child);
            if ((childEnd <= boundsStart) || (childStart >= boundsEnd)) {
                continue;// not visible at all

            }
            if ((childStart >= boundsStart) || (!fullyVisible)) {
                // when checking for start, it is enough even if part of the child's top is visible
                // as long as fully visible is not requested.
                return child;
            }
            if (partiallyVisible == null) {
                partiallyVisible = child;
            }
        }
        return partiallyVisible;
    }

    /**
     * This is for internal use. Not necessarily the child closest to bottom but the first child
     * we find that matches the criteria.
     * This method does not do any sorting based on child's end coordinate, instead, it uses
     * children order.
     */
    android.view.View findFirstVisibleItemClosestToEnd(boolean fullyVisible) {
        final int boundsStart = mPrimaryOrientation.getStartAfterPadding();
        final int boundsEnd = mPrimaryOrientation.getEndAfterPadding();
        android.view.View partiallyVisible = null;
        for (int i = getChildCount() - 1; i >= 0; i--) {
            final android.view.View child = getChildAt(i);
            final int childStart = mPrimaryOrientation.getDecoratedStart(child);
            final int childEnd = mPrimaryOrientation.getDecoratedEnd(child);
            if ((childEnd <= boundsStart) || (childStart >= boundsEnd)) {
                continue;// not visible at all

            }
            if ((childEnd <= boundsEnd) || (!fullyVisible)) {
                // when checking for end, it is enough even if part of the child's bottom is visible
                // as long as fully visible is not requested.
                return child;
            }
            if (partiallyVisible == null) {
                partiallyVisible = child;
            }
        }
        return partiallyVisible;
    }

    private void fixEndGap(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, boolean canOffsetChildren) {
        final int maxEndLine = getMaxEnd(java.lang.Integer.MIN_VALUE);
        if (maxEndLine == java.lang.Integer.MIN_VALUE) {
            return;
        }
        int gap = mPrimaryOrientation.getEndAfterPadding() - maxEndLine;
        int fixOffset;
        if (gap > 0) {
            fixOffset = -scrollBy(-gap, recycler, state);
        } else {
            return;// nothing to fix

        }
        gap -= fixOffset;
        if (canOffsetChildren && (gap > 0)) {
            mPrimaryOrientation.offsetChildren(gap);
        }
    }

    private void fixStartGap(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, boolean canOffsetChildren) {
        final int minStartLine = getMinStart(java.lang.Integer.MAX_VALUE);
        if (minStartLine == java.lang.Integer.MAX_VALUE) {
            return;
        }
        int gap = minStartLine - mPrimaryOrientation.getStartAfterPadding();
        int fixOffset;
        if (gap > 0) {
            fixOffset = scrollBy(gap, recycler, state);
        } else {
            return;// nothing to fix

        }
        gap -= fixOffset;
        if (canOffsetChildren && (gap > 0)) {
            mPrimaryOrientation.offsetChildren(-gap);
        }
    }

    private void updateLayoutState(int anchorPosition, android.support.v7.widget.RecyclerView.State state) {
        mLayoutState.mAvailable = 0;
        mLayoutState.mCurrentPosition = anchorPosition;
        int startExtra = 0;
        int endExtra = 0;
        if (isSmoothScrolling()) {
            final int targetPos = state.getTargetScrollPosition();
            if (targetPos != android.support.v7.widget.RecyclerView.NO_POSITION) {
                if (mShouldReverseLayout == (targetPos < anchorPosition)) {
                    endExtra = mPrimaryOrientation.getTotalSpace();
                } else {
                    startExtra = mPrimaryOrientation.getTotalSpace();
                }
            }
        }
        // Line of the furthest row.
        final boolean clipToPadding = getClipToPadding();
        if (clipToPadding) {
            mLayoutState.mStartLine = mPrimaryOrientation.getStartAfterPadding() - startExtra;
            mLayoutState.mEndLine = mPrimaryOrientation.getEndAfterPadding() + endExtra;
        } else {
            mLayoutState.mEndLine = mPrimaryOrientation.getEnd() + endExtra;
            mLayoutState.mStartLine = -startExtra;
        }
        mLayoutState.mStopInFocusable = false;
        mLayoutState.mRecycle = true;
        mLayoutState.mInfinite = (mPrimaryOrientation.getMode() == android.view.View.MeasureSpec.UNSPECIFIED) && (mPrimaryOrientation.getEnd() == 0);
    }

    private void setLayoutStateDirection(int direction) {
        mLayoutState.mLayoutDirection = direction;
        mLayoutState.mItemDirection = (mShouldReverseLayout == (direction == android.support.v7.widget.LayoutState.LAYOUT_START)) ? android.support.v7.widget.LayoutState.ITEM_DIRECTION_TAIL : android.support.v7.widget.LayoutState.ITEM_DIRECTION_HEAD;
    }

    @java.lang.Override
    public void offsetChildrenHorizontal(int dx) {
        super.offsetChildrenHorizontal(dx);
        for (int i = 0; i < mSpanCount; i++) {
            mSpans[i].onOffset(dx);
        }
    }

    @java.lang.Override
    public void offsetChildrenVertical(int dy) {
        super.offsetChildrenVertical(dy);
        for (int i = 0; i < mSpanCount; i++) {
            mSpans[i].onOffset(dy);
        }
    }

    @java.lang.Override
    public void onItemsRemoved(android.support.v7.widget.RecyclerView recyclerView, int positionStart, int itemCount) {
        handleUpdate(positionStart, itemCount, android.support.v7.widget.AdapterHelper.UpdateOp.REMOVE);
    }

    @java.lang.Override
    public void onItemsAdded(android.support.v7.widget.RecyclerView recyclerView, int positionStart, int itemCount) {
        handleUpdate(positionStart, itemCount, android.support.v7.widget.AdapterHelper.UpdateOp.ADD);
    }

    @java.lang.Override
    public void onItemsChanged(android.support.v7.widget.RecyclerView recyclerView) {
        mLazySpanLookup.clear();
        requestLayout();
    }

    @java.lang.Override
    public void onItemsMoved(android.support.v7.widget.RecyclerView recyclerView, int from, int to, int itemCount) {
        handleUpdate(from, to, android.support.v7.widget.AdapterHelper.UpdateOp.MOVE);
    }

    @java.lang.Override
    public void onItemsUpdated(android.support.v7.widget.RecyclerView recyclerView, int positionStart, int itemCount, java.lang.Object payload) {
        handleUpdate(positionStart, itemCount, android.support.v7.widget.AdapterHelper.UpdateOp.UPDATE);
    }

    /**
     * Checks whether it should invalidate span assignments in response to an adapter change.
     */
    private void handleUpdate(int positionStart, int itemCountOrToPosition, int cmd) {
        int minPosition = (mShouldReverseLayout) ? getLastChildPosition() : getFirstChildPosition();
        final int affectedRangeEnd;// exclusive

        final int affectedRangeStart;// inclusive

        if (cmd == android.support.v7.widget.AdapterHelper.UpdateOp.MOVE) {
            if (positionStart < itemCountOrToPosition) {
                affectedRangeEnd = itemCountOrToPosition + 1;
                affectedRangeStart = positionStart;
            } else {
                affectedRangeEnd = positionStart + 1;
                affectedRangeStart = itemCountOrToPosition;
            }
        } else {
            affectedRangeStart = positionStart;
            affectedRangeEnd = positionStart + itemCountOrToPosition;
        }
        mLazySpanLookup.invalidateAfter(affectedRangeStart);
        switch (cmd) {
            case android.support.v7.widget.AdapterHelper.UpdateOp.ADD :
                mLazySpanLookup.offsetForAddition(positionStart, itemCountOrToPosition);
                break;
            case android.support.v7.widget.AdapterHelper.UpdateOp.REMOVE :
                mLazySpanLookup.offsetForRemoval(positionStart, itemCountOrToPosition);
                break;
            case android.support.v7.widget.AdapterHelper.UpdateOp.MOVE :
                // TODO optimize
                mLazySpanLookup.offsetForRemoval(positionStart, 1);
                mLazySpanLookup.offsetForAddition(itemCountOrToPosition, 1);
                break;
        }
        if (affectedRangeEnd <= minPosition) {
            return;
        }
        int maxPosition = (mShouldReverseLayout) ? getFirstChildPosition() : getLastChildPosition();
        if (affectedRangeStart <= maxPosition) {
            requestLayout();
        }
    }

    private int fill(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.LayoutState layoutState, android.support.v7.widget.RecyclerView.State state) {
        mRemainingSpans.set(0, mSpanCount, true);
        // The target position we are trying to reach.
        final int targetLine;
        // Line of the furthest row.
        if (mLayoutState.mInfinite) {
            if (layoutState.mLayoutDirection == android.support.v7.widget.LayoutState.LAYOUT_END) {
                targetLine = java.lang.Integer.MAX_VALUE;
            } else {
                // LAYOUT_START
                targetLine = java.lang.Integer.MIN_VALUE;
            }
        } else {
            if (layoutState.mLayoutDirection == android.support.v7.widget.LayoutState.LAYOUT_END) {
                targetLine = layoutState.mEndLine + layoutState.mAvailable;
            } else {
                // LAYOUT_START
                targetLine = layoutState.mStartLine - layoutState.mAvailable;
            }
        }
        updateAllRemainingSpans(layoutState.mLayoutDirection, targetLine);
        if (android.support.v7.widget.StaggeredGridLayoutManager.DEBUG) {
            android.util.Log.d(android.support.v7.widget.StaggeredGridLayoutManager.TAG, ((((("FILLING targetLine: " + targetLine) + ",") + "remaining spans:") + mRemainingSpans) + ", state: ") + layoutState);
        }
        // the default coordinate to add new view.
        final int defaultNewViewLine = (mShouldReverseLayout) ? mPrimaryOrientation.getEndAfterPadding() : mPrimaryOrientation.getStartAfterPadding();
        boolean added = false;
        while (layoutState.hasMore(state) && (mLayoutState.mInfinite || (!mRemainingSpans.isEmpty()))) {
            android.view.View view = layoutState.next(recycler);
            android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams lp = ((android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) (view.getLayoutParams()));
            final int position = lp.getViewLayoutPosition();
            final int spanIndex = mLazySpanLookup.getSpan(position);
            android.support.v7.widget.StaggeredGridLayoutManager.Span currentSpan;
            final boolean assignSpan = spanIndex == android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams.INVALID_SPAN_ID;
            if (assignSpan) {
                currentSpan = (lp.mFullSpan) ? mSpans[0] : getNextSpan(layoutState);
                mLazySpanLookup.setSpan(position, currentSpan);
                if (android.support.v7.widget.StaggeredGridLayoutManager.DEBUG) {
                    android.util.Log.d(android.support.v7.widget.StaggeredGridLayoutManager.TAG, (("assigned " + currentSpan.mIndex) + " for ") + position);
                }
            } else {
                if (android.support.v7.widget.StaggeredGridLayoutManager.DEBUG) {
                    android.util.Log.d(android.support.v7.widget.StaggeredGridLayoutManager.TAG, (("using " + spanIndex) + " for pos ") + position);
                }
                currentSpan = mSpans[spanIndex];
            }
            // assign span before measuring so that item decorators can get updated span index
            lp.mSpan = currentSpan;
            if (layoutState.mLayoutDirection == android.support.v7.widget.LayoutState.LAYOUT_END) {
                addView(view);
            } else {
                addView(view, 0);
            }
            measureChildWithDecorationsAndMargin(view, lp, false);
            final int start;
            final int end;
            if (layoutState.mLayoutDirection == android.support.v7.widget.LayoutState.LAYOUT_END) {
                start = (lp.mFullSpan) ? getMaxEnd(defaultNewViewLine) : currentSpan.getEndLine(defaultNewViewLine);
                end = start + mPrimaryOrientation.getDecoratedMeasurement(view);
                if (assignSpan && lp.mFullSpan) {
                    android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fullSpanItem;
                    fullSpanItem = createFullSpanItemFromEnd(start);
                    fullSpanItem.mGapDir = android.support.v7.widget.LayoutState.LAYOUT_START;
                    fullSpanItem.mPosition = position;
                    mLazySpanLookup.addFullSpanItem(fullSpanItem);
                }
            } else {
                end = (lp.mFullSpan) ? getMinStart(defaultNewViewLine) : currentSpan.getStartLine(defaultNewViewLine);
                start = end - mPrimaryOrientation.getDecoratedMeasurement(view);
                if (assignSpan && lp.mFullSpan) {
                    android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fullSpanItem;
                    fullSpanItem = createFullSpanItemFromStart(end);
                    fullSpanItem.mGapDir = android.support.v7.widget.LayoutState.LAYOUT_END;
                    fullSpanItem.mPosition = position;
                    mLazySpanLookup.addFullSpanItem(fullSpanItem);
                }
            }
            // check if this item may create gaps in the future
            if (lp.mFullSpan && (layoutState.mItemDirection == android.support.v7.widget.LayoutState.ITEM_DIRECTION_HEAD)) {
                if (assignSpan) {
                    mLaidOutInvalidFullSpan = true;
                } else {
                    final boolean hasInvalidGap;
                    if (layoutState.mLayoutDirection == android.support.v7.widget.LayoutState.LAYOUT_END) {
                        hasInvalidGap = !areAllEndsEqual();
                    } else {
                        // layoutState.mLayoutDirection == LAYOUT_START
                        hasInvalidGap = !areAllStartsEqual();
                    }
                    if (hasInvalidGap) {
                        final android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fullSpanItem = mLazySpanLookup.getFullSpanItem(position);
                        if (fullSpanItem != null) {
                            fullSpanItem.mHasUnwantedGapAfter = true;
                        }
                        mLaidOutInvalidFullSpan = true;
                    }
                }
            }
            attachViewToSpans(view, lp, layoutState);
            final int otherStart;
            final int otherEnd;
            if (isLayoutRTL() && (mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL)) {
                otherEnd = (lp.mFullSpan) ? mSecondaryOrientation.getEndAfterPadding() : mSecondaryOrientation.getEndAfterPadding() - (((mSpanCount - 1) - currentSpan.mIndex) * mSizePerSpan);
                otherStart = otherEnd - mSecondaryOrientation.getDecoratedMeasurement(view);
            } else {
                otherStart = (lp.mFullSpan) ? mSecondaryOrientation.getStartAfterPadding() : (currentSpan.mIndex * mSizePerSpan) + mSecondaryOrientation.getStartAfterPadding();
                otherEnd = otherStart + mSecondaryOrientation.getDecoratedMeasurement(view);
            }
            if (mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL) {
                layoutDecoratedWithMargins(view, otherStart, start, otherEnd, end);
            } else {
                layoutDecoratedWithMargins(view, start, otherStart, end, otherEnd);
            }
            if (lp.mFullSpan) {
                updateAllRemainingSpans(mLayoutState.mLayoutDirection, targetLine);
            } else {
                updateRemainingSpans(currentSpan, mLayoutState.mLayoutDirection, targetLine);
            }
            recycle(recycler, mLayoutState);
            if (mLayoutState.mStopInFocusable && view.isFocusable()) {
                if (lp.mFullSpan) {
                    mRemainingSpans.clear();
                } else {
                    mRemainingSpans.set(currentSpan.mIndex, false);
                }
            }
            added = true;
        } 
        if (!added) {
            recycle(recycler, mLayoutState);
        }
        final int diff;
        if (mLayoutState.mLayoutDirection == android.support.v7.widget.LayoutState.LAYOUT_START) {
            final int minStart = getMinStart(mPrimaryOrientation.getStartAfterPadding());
            diff = mPrimaryOrientation.getStartAfterPadding() - minStart;
        } else {
            final int maxEnd = getMaxEnd(mPrimaryOrientation.getEndAfterPadding());
            diff = maxEnd - mPrimaryOrientation.getEndAfterPadding();
        }
        return diff > 0 ? java.lang.Math.min(layoutState.mAvailable, diff) : 0;
    }

    private android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFullSpanItemFromEnd(int newItemTop) {
        android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fsi = new android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem();
        fsi.mGapPerSpan = new int[mSpanCount];
        for (int i = 0; i < mSpanCount; i++) {
            fsi.mGapPerSpan[i] = newItemTop - mSpans[i].getEndLine(newItemTop);
        }
        return fsi;
    }

    private android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFullSpanItemFromStart(int newItemBottom) {
        android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fsi = new android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem();
        fsi.mGapPerSpan = new int[mSpanCount];
        for (int i = 0; i < mSpanCount; i++) {
            fsi.mGapPerSpan[i] = mSpans[i].getStartLine(newItemBottom) - newItemBottom;
        }
        return fsi;
    }

    private void attachViewToSpans(android.view.View view, android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams lp, android.support.v7.widget.LayoutState layoutState) {
        if (layoutState.mLayoutDirection == android.support.v7.widget.LayoutState.LAYOUT_END) {
            if (lp.mFullSpan) {
                appendViewToAllSpans(view);
            } else {
                lp.mSpan.appendToSpan(view);
            }
        } else {
            if (lp.mFullSpan) {
                prependViewToAllSpans(view);
            } else {
                lp.mSpan.prependToSpan(view);
            }
        }
    }

    private void recycle(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.LayoutState layoutState) {
        if ((!layoutState.mRecycle) || layoutState.mInfinite) {
            return;
        }
        if (layoutState.mAvailable == 0) {
            // easy, recycle line is still valid
            if (layoutState.mLayoutDirection == android.support.v7.widget.LayoutState.LAYOUT_START) {
                recycleFromEnd(recycler, layoutState.mEndLine);
            } else {
                recycleFromStart(recycler, layoutState.mStartLine);
            }
        } else {
            // scrolling case, recycle line can be shifted by how much space we could cover
            // by adding new views
            if (layoutState.mLayoutDirection == android.support.v7.widget.LayoutState.LAYOUT_START) {
                // calculate recycle line
                int scrolled = layoutState.mStartLine - getMaxStart(layoutState.mStartLine);
                final int line;
                if (scrolled < 0) {
                    line = layoutState.mEndLine;
                } else {
                    line = layoutState.mEndLine - java.lang.Math.min(scrolled, layoutState.mAvailable);
                }
                recycleFromEnd(recycler, line);
            } else {
                // calculate recycle line
                int scrolled = getMinEnd(layoutState.mEndLine) - layoutState.mEndLine;
                final int line;
                if (scrolled < 0) {
                    line = layoutState.mStartLine;
                } else {
                    line = layoutState.mStartLine + java.lang.Math.min(scrolled, layoutState.mAvailable);
                }
                recycleFromStart(recycler, line);
            }
        }
    }

    private void appendViewToAllSpans(android.view.View view) {
        // traverse in reverse so that we end up assigning full span items to 0
        for (int i = mSpanCount - 1; i >= 0; i--) {
            mSpans[i].appendToSpan(view);
        }
    }

    private void prependViewToAllSpans(android.view.View view) {
        // traverse in reverse so that we end up assigning full span items to 0
        for (int i = mSpanCount - 1; i >= 0; i--) {
            mSpans[i].prependToSpan(view);
        }
    }

    private void updateAllRemainingSpans(int layoutDir, int targetLine) {
        for (int i = 0; i < mSpanCount; i++) {
            if (mSpans[i].mViews.isEmpty()) {
                continue;
            }
            updateRemainingSpans(mSpans[i], layoutDir, targetLine);
        }
    }

    private void updateRemainingSpans(android.support.v7.widget.StaggeredGridLayoutManager.Span span, int layoutDir, int targetLine) {
        final int deletedSize = span.getDeletedSize();
        if (layoutDir == android.support.v7.widget.LayoutState.LAYOUT_START) {
            final int line = span.getStartLine();
            if ((line + deletedSize) <= targetLine) {
                mRemainingSpans.set(span.mIndex, false);
            }
        } else {
            final int line = span.getEndLine();
            if ((line - deletedSize) >= targetLine) {
                mRemainingSpans.set(span.mIndex, false);
            }
        }
    }

    private int getMaxStart(int def) {
        int maxStart = mSpans[0].getStartLine(def);
        for (int i = 1; i < mSpanCount; i++) {
            final int spanStart = mSpans[i].getStartLine(def);
            if (spanStart > maxStart) {
                maxStart = spanStart;
            }
        }
        return maxStart;
    }

    private int getMinStart(int def) {
        int minStart = mSpans[0].getStartLine(def);
        for (int i = 1; i < mSpanCount; i++) {
            final int spanStart = mSpans[i].getStartLine(def);
            if (spanStart < minStart) {
                minStart = spanStart;
            }
        }
        return minStart;
    }

    boolean areAllEndsEqual() {
        int end = mSpans[0].getEndLine(android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE);
        for (int i = 1; i < mSpanCount; i++) {
            if (mSpans[i].getEndLine(android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE) != end) {
                return false;
            }
        }
        return true;
    }

    boolean areAllStartsEqual() {
        int start = mSpans[0].getStartLine(android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE);
        for (int i = 1; i < mSpanCount; i++) {
            if (mSpans[i].getStartLine(android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE) != start) {
                return false;
            }
        }
        return true;
    }

    private int getMaxEnd(int def) {
        int maxEnd = mSpans[0].getEndLine(def);
        for (int i = 1; i < mSpanCount; i++) {
            final int spanEnd = mSpans[i].getEndLine(def);
            if (spanEnd > maxEnd) {
                maxEnd = spanEnd;
            }
        }
        return maxEnd;
    }

    private int getMinEnd(int def) {
        int minEnd = mSpans[0].getEndLine(def);
        for (int i = 1; i < mSpanCount; i++) {
            final int spanEnd = mSpans[i].getEndLine(def);
            if (spanEnd < minEnd) {
                minEnd = spanEnd;
            }
        }
        return minEnd;
    }

    private void recycleFromStart(android.support.v7.widget.RecyclerView.Recycler recycler, int line) {
        while (getChildCount() > 0) {
            android.view.View child = getChildAt(0);
            if ((mPrimaryOrientation.getDecoratedEnd(child) <= line) && (mPrimaryOrientation.getTransformedEndWithDecoration(child) <= line)) {
                android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams lp = ((android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) (child.getLayoutParams()));
                // Don't recycle the last View in a span not to lose span's start/end lines
                if (lp.mFullSpan) {
                    for (int j = 0; j < mSpanCount; j++) {
                        if (mSpans[j].mViews.size() == 1) {
                            return;
                        }
                    }
                    for (int j = 0; j < mSpanCount; j++) {
                        mSpans[j].popStart();
                    }
                } else {
                    if (lp.mSpan.mViews.size() == 1) {
                        return;
                    }
                    lp.mSpan.popStart();
                }
                removeAndRecycleView(child, recycler);
            } else {
                return;// done

            }
        } 
    }

    private void recycleFromEnd(android.support.v7.widget.RecyclerView.Recycler recycler, int line) {
        final int childCount = getChildCount();
        int i;
        for (i = childCount - 1; i >= 0; i--) {
            android.view.View child = getChildAt(i);
            if ((mPrimaryOrientation.getDecoratedStart(child) >= line) && (mPrimaryOrientation.getTransformedStartWithDecoration(child) >= line)) {
                android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams lp = ((android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) (child.getLayoutParams()));
                // Don't recycle the last View in a span not to lose span's start/end lines
                if (lp.mFullSpan) {
                    for (int j = 0; j < mSpanCount; j++) {
                        if (mSpans[j].mViews.size() == 1) {
                            return;
                        }
                    }
                    for (int j = 0; j < mSpanCount; j++) {
                        mSpans[j].popEnd();
                    }
                } else {
                    if (lp.mSpan.mViews.size() == 1) {
                        return;
                    }
                    lp.mSpan.popEnd();
                }
                removeAndRecycleView(child, recycler);
            } else {
                return;// done

            }
        }
    }

    /**
     *
     *
     * @return True if last span is the first one we want to fill
     */
    private boolean preferLastSpan(int layoutDir) {
        if (mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.HORIZONTAL) {
            return (layoutDir == android.support.v7.widget.LayoutState.LAYOUT_START) != mShouldReverseLayout;
        }
        return ((layoutDir == android.support.v7.widget.LayoutState.LAYOUT_START) == mShouldReverseLayout) == isLayoutRTL();
    }

    /**
     * Finds the span for the next view.
     */
    private android.support.v7.widget.StaggeredGridLayoutManager.Span getNextSpan(android.support.v7.widget.LayoutState layoutState) {
        final boolean preferLastSpan = preferLastSpan(layoutState.mLayoutDirection);
        final int startIndex;
        final int endIndex;
        final int diff;
        if (preferLastSpan) {
            startIndex = mSpanCount - 1;
            endIndex = -1;
            diff = -1;
        } else {
            startIndex = 0;
            endIndex = mSpanCount;
            diff = 1;
        }
        if (layoutState.mLayoutDirection == android.support.v7.widget.LayoutState.LAYOUT_END) {
            android.support.v7.widget.StaggeredGridLayoutManager.Span min = null;
            int minLine = java.lang.Integer.MAX_VALUE;
            final int defaultLine = mPrimaryOrientation.getStartAfterPadding();
            for (int i = startIndex; i != endIndex; i += diff) {
                final android.support.v7.widget.StaggeredGridLayoutManager.Span other = mSpans[i];
                int otherLine = other.getEndLine(defaultLine);
                if (otherLine < minLine) {
                    min = other;
                    minLine = otherLine;
                }
            }
            return min;
        } else {
            android.support.v7.widget.StaggeredGridLayoutManager.Span max = null;
            int maxLine = java.lang.Integer.MIN_VALUE;
            final int defaultLine = mPrimaryOrientation.getEndAfterPadding();
            for (int i = startIndex; i != endIndex; i += diff) {
                final android.support.v7.widget.StaggeredGridLayoutManager.Span other = mSpans[i];
                int otherLine = other.getStartLine(defaultLine);
                if (otherLine > maxLine) {
                    max = other;
                    maxLine = otherLine;
                }
            }
            return max;
        }
    }

    @java.lang.Override
    public boolean canScrollVertically() {
        return mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
    }

    @java.lang.Override
    public boolean canScrollHorizontally() {
        return mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.HORIZONTAL;
    }

    @java.lang.Override
    public int scrollHorizontallyBy(int dx, android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        return scrollBy(dx, recycler, state);
    }

    @java.lang.Override
    public int scrollVerticallyBy(int dy, android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        return scrollBy(dy, recycler, state);
    }

    private int calculateScrollDirectionForPosition(int position) {
        if (getChildCount() == 0) {
            return mShouldReverseLayout ? android.support.v7.widget.LayoutState.LAYOUT_END : android.support.v7.widget.LayoutState.LAYOUT_START;
        }
        final int firstChildPos = getFirstChildPosition();
        return (position < firstChildPos) != mShouldReverseLayout ? android.support.v7.widget.LayoutState.LAYOUT_START : android.support.v7.widget.LayoutState.LAYOUT_END;
    }

    @java.lang.Override
    public android.graphics.PointF computeScrollVectorForPosition(int targetPosition) {
        final int direction = calculateScrollDirectionForPosition(targetPosition);
        android.graphics.PointF outVector = new android.graphics.PointF();
        if (direction == 0) {
            return null;
        }
        if (mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.HORIZONTAL) {
            outVector.x = direction;
            outVector.y = 0;
        } else {
            outVector.x = 0;
            outVector.y = direction;
        }
        return outVector;
    }

    @java.lang.Override
    public void smoothScrollToPosition(android.support.v7.widget.RecyclerView recyclerView, android.support.v7.widget.RecyclerView.State state, int position) {
        android.support.v7.widget.LinearSmoothScroller scroller = new android.support.v7.widget.LinearSmoothScroller(recyclerView.getContext());
        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);
    }

    @java.lang.Override
    public void scrollToPosition(int position) {
        if ((mPendingSavedState != null) && (mPendingSavedState.mAnchorPosition != position)) {
            mPendingSavedState.invalidateAnchorPositionInfo();
        }
        mPendingScrollPosition = position;
        mPendingScrollPositionOffset = android.support.v7.widget.StaggeredGridLayoutManager.INVALID_OFFSET;
        requestLayout();
    }

    /**
     * Scroll to the specified adapter position with the given offset from layout start.
     * <p>
     * Note that scroll position change will not be reflected until the next layout call.
     * <p>
     * If you are just trying to make a position visible, use {@link #scrollToPosition(int)}.
     *
     * @param position
     * 		Index (starting at 0) of the reference item.
     * @param offset
     * 		The distance (in pixels) between the start edge of the item view and
     * 		start edge of the RecyclerView.
     * @see #setReverseLayout(boolean)
     * @see #scrollToPosition(int)
     */
    public void scrollToPositionWithOffset(int position, int offset) {
        if (mPendingSavedState != null) {
            mPendingSavedState.invalidateAnchorPositionInfo();
        }
        mPendingScrollPosition = position;
        mPendingScrollPositionOffset = offset;
        requestLayout();
    }

    @java.lang.Override
    int getItemPrefetchCount() {
        return mSpanCount;
    }

    @java.lang.Override
    int gatherPrefetchIndices(int dx, int dy, android.support.v7.widget.RecyclerView.State state, int[] outIndices) {
        int delta = (mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.HORIZONTAL) ? dx : dy;
        if ((getChildCount() == 0) || (delta == 0)) {
            // can't support this scroll, so don't bother prefetching
            return 0;
        }
        prepareLayoutStateForDelta(delta, state);
        int remainingSpan = mSpanCount;
        int count = 0;
        while (((count < mSpanCount) && mLayoutState.hasMore(state)) && (remainingSpan > 0)) {
            final int pos = mLayoutState.mCurrentPosition;
            outIndices[count] = pos;
            remainingSpan--;
            mLayoutState.mCurrentPosition += mLayoutState.mItemDirection;
            count++;
        } 
        return count;
    }

    void prepareLayoutStateForDelta(int delta, android.support.v7.widget.RecyclerView.State state) {
        final int referenceChildPosition;
        final int layoutDir;
        if (delta > 0) {
            // layout towards end
            layoutDir = android.support.v7.widget.LayoutState.LAYOUT_END;
            referenceChildPosition = getLastChildPosition();
        } else {
            layoutDir = android.support.v7.widget.LayoutState.LAYOUT_START;
            referenceChildPosition = getFirstChildPosition();
        }
        mLayoutState.mRecycle = true;
        updateLayoutState(referenceChildPosition, state);
        setLayoutStateDirection(layoutDir);
        mLayoutState.mCurrentPosition = referenceChildPosition + mLayoutState.mItemDirection;
        final int absDt = java.lang.Math.abs(delta);
        mLayoutState.mAvailable = absDt;
    }

    int scrollBy(int dt, android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        if ((getChildCount() == 0) || (dt == 0)) {
            return 0;
        }
        prepareLayoutStateForDelta(dt, state);
        int consumed = fill(recycler, mLayoutState, state);
        final int available = mLayoutState.mAvailable;
        final int totalScroll;
        if (available < consumed) {
            totalScroll = dt;
        } else
            if (dt < 0) {
                totalScroll = -consumed;
            } else {
                // dt > 0
                totalScroll = consumed;
            }

        if (android.support.v7.widget.StaggeredGridLayoutManager.DEBUG) {
            android.util.Log.d(android.support.v7.widget.StaggeredGridLayoutManager.TAG, (("asked " + dt) + " scrolled") + totalScroll);
        }
        mPrimaryOrientation.offsetChildren(-totalScroll);
        // always reset this if we scroll for a proper save instance state
        mLastLayoutFromEnd = mShouldReverseLayout;
        mLayoutState.mAvailable = 0;
        recycle(recycler, mLayoutState);
        return totalScroll;
    }

    private int getLastChildPosition() {
        final int childCount = getChildCount();
        return childCount == 0 ? 0 : getPosition(getChildAt(childCount - 1));
    }

    private int getFirstChildPosition() {
        final int childCount = getChildCount();
        return childCount == 0 ? 0 : getPosition(getChildAt(0));
    }

    /**
     * Finds the first View that can be used as an anchor View.
     *
     * @return Position of the View or 0 if it cannot find any such View.
     */
    private int findFirstReferenceChildPosition(int itemCount) {
        final int limit = getChildCount();
        for (int i = 0; i < limit; i++) {
            final android.view.View view = getChildAt(i);
            final int position = getPosition(view);
            if ((position >= 0) && (position < itemCount)) {
                return position;
            }
        }
        return 0;
    }

    /**
     * Finds the last View that can be used as an anchor View.
     *
     * @return Position of the View or 0 if it cannot find any such View.
     */
    private int findLastReferenceChildPosition(int itemCount) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            final android.view.View view = getChildAt(i);
            final int position = getPosition(view);
            if ((position >= 0) && (position < itemCount)) {
                return position;
            }
        }
        return 0;
    }

    @java.lang.SuppressWarnings("deprecation")
    @java.lang.Override
    public android.support.v7.widget.RecyclerView.LayoutParams generateDefaultLayoutParams() {
        if (mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.HORIZONTAL) {
            return new android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            return new android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @java.lang.Override
    public android.support.v7.widget.RecyclerView.LayoutParams generateLayoutParams(android.content.Context c, android.util.AttributeSet attrs) {
        return new android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams(c, attrs);
    }

    @java.lang.Override
    public android.support.v7.widget.RecyclerView.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams lp) {
        if (lp instanceof android.view.ViewGroup.MarginLayoutParams) {
            return new android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams(((android.view.ViewGroup.MarginLayoutParams) (lp)));
        } else {
            return new android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams(lp);
        }
    }

    @java.lang.Override
    public boolean checkLayoutParams(android.support.v7.widget.RecyclerView.LayoutParams lp) {
        return lp instanceof android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams;
    }

    public int getOrientation() {
        return mOrientation;
    }

    @android.support.annotation.Nullable
    @java.lang.Override
    public android.view.View onFocusSearchFailed(android.view.View focused, int direction, android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        if (getChildCount() == 0) {
            return null;
        }
        final android.view.View directChild = findContainingItemView(focused);
        if (directChild == null) {
            return null;
        }
        resolveShouldLayoutReverse();
        final int layoutDir = convertFocusDirectionToLayoutDirection(direction);
        if (layoutDir == android.support.v7.widget.LayoutState.INVALID_LAYOUT) {
            return null;
        }
        android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams prevFocusLayoutParams = ((android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) (directChild.getLayoutParams()));
        boolean prevFocusFullSpan = prevFocusLayoutParams.mFullSpan;
        final android.support.v7.widget.StaggeredGridLayoutManager.Span prevFocusSpan = prevFocusLayoutParams.mSpan;
        final int referenceChildPosition;
        if (layoutDir == android.support.v7.widget.LayoutState.LAYOUT_END) {
            // layout towards end
            referenceChildPosition = getLastChildPosition();
        } else {
            referenceChildPosition = getFirstChildPosition();
        }
        updateLayoutState(referenceChildPosition, state);
        setLayoutStateDirection(layoutDir);
        mLayoutState.mCurrentPosition = referenceChildPosition + mLayoutState.mItemDirection;
        mLayoutState.mAvailable = ((int) (android.support.v7.widget.StaggeredGridLayoutManager.MAX_SCROLL_FACTOR * mPrimaryOrientation.getTotalSpace()));
        mLayoutState.mStopInFocusable = true;
        mLayoutState.mRecycle = false;
        fill(recycler, mLayoutState, state);
        mLastLayoutFromEnd = mShouldReverseLayout;
        if (!prevFocusFullSpan) {
            android.view.View view = prevFocusSpan.getFocusableViewAfter(referenceChildPosition, layoutDir);
            if ((view != null) && (view != directChild)) {
                return view;
            }
        }
        // either could not find from the desired span or prev view is full span.
        // traverse all spans
        if (preferLastSpan(layoutDir)) {
            for (int i = mSpanCount - 1; i >= 0; i--) {
                android.view.View view = mSpans[i].getFocusableViewAfter(referenceChildPosition, layoutDir);
                if ((view != null) && (view != directChild)) {
                    return view;
                }
            }
        } else {
            for (int i = 0; i < mSpanCount; i++) {
                android.view.View view = mSpans[i].getFocusableViewAfter(referenceChildPosition, layoutDir);
                if ((view != null) && (view != directChild)) {
                    return view;
                }
            }
        }
        return null;
    }

    /**
     * Converts a focusDirection to orientation.
     *
     * @param focusDirection
     * 		One of {@link View#FOCUS_UP}, {@link View#FOCUS_DOWN},
     * 		{@link View#FOCUS_LEFT}, {@link View#FOCUS_RIGHT},
     * 		{@link View#FOCUS_BACKWARD}, {@link View#FOCUS_FORWARD}
     * 		or 0 for not applicable
     * @return {@link LayoutState#LAYOUT_START} or {@link LayoutState#LAYOUT_END} if focus direction
    is applicable to current state, {@link LayoutState#INVALID_LAYOUT} otherwise.
     */
    private int convertFocusDirectionToLayoutDirection(int focusDirection) {
        switch (focusDirection) {
            case android.view.View.FOCUS_BACKWARD :
                if (mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL) {
                    return android.support.v7.widget.LayoutState.LAYOUT_START;
                } else
                    if (isLayoutRTL()) {
                        return android.support.v7.widget.LayoutState.LAYOUT_END;
                    } else {
                        return android.support.v7.widget.LayoutState.LAYOUT_START;
                    }

            case android.view.View.FOCUS_FORWARD :
                if (mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL) {
                    return android.support.v7.widget.LayoutState.LAYOUT_END;
                } else
                    if (isLayoutRTL()) {
                        return android.support.v7.widget.LayoutState.LAYOUT_START;
                    } else {
                        return android.support.v7.widget.LayoutState.LAYOUT_END;
                    }

            case android.view.View.FOCUS_UP :
                return mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL ? android.support.v7.widget.LayoutState.LAYOUT_START : android.support.v7.widget.LayoutState.INVALID_LAYOUT;
            case android.view.View.FOCUS_DOWN :
                return mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL ? android.support.v7.widget.LayoutState.LAYOUT_END : android.support.v7.widget.LayoutState.INVALID_LAYOUT;
            case android.view.View.FOCUS_LEFT :
                return mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.HORIZONTAL ? android.support.v7.widget.LayoutState.LAYOUT_START : android.support.v7.widget.LayoutState.INVALID_LAYOUT;
            case android.view.View.FOCUS_RIGHT :
                return mOrientation == android.support.v7.widget.StaggeredGridLayoutManager.HORIZONTAL ? android.support.v7.widget.LayoutState.LAYOUT_END : android.support.v7.widget.LayoutState.INVALID_LAYOUT;
            default :
                if (android.support.v7.widget.StaggeredGridLayoutManager.DEBUG) {
                    android.util.Log.d(android.support.v7.widget.StaggeredGridLayoutManager.TAG, "Unknown focus request:" + focusDirection);
                }
                return android.support.v7.widget.LayoutState.INVALID_LAYOUT;
        }
    }

    /**
     * LayoutParams used by StaggeredGridLayoutManager.
     * <p>
     * Note that if the orientation is {@link #VERTICAL}, the width parameter is ignored and if the
     * orientation is {@link #HORIZONTAL} the height parameter is ignored because child view is
     * expected to fill all of the space given to it.
     */
    public static class LayoutParams extends android.support.v7.widget.RecyclerView.LayoutParams {
        /**
         * Span Id for Views that are not laid out yet.
         */
        public static final int INVALID_SPAN_ID = -1;

        // Package scope to be able to access from tests.
        android.support.v7.widget.StaggeredGridLayoutManager.Span mSpan;

        boolean mFullSpan;

        public LayoutParams(android.content.Context c, android.util.AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(android.view.ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(android.support.v7.widget.RecyclerView.LayoutParams source) {
            super(source);
        }

        /**
         * When set to true, the item will layout using all span area. That means, if orientation
         * is vertical, the view will have full width; if orientation is horizontal, the view will
         * have full height.
         *
         * @param fullSpan
         * 		True if this item should traverse all spans.
         * @see #isFullSpan()
         */
        public void setFullSpan(boolean fullSpan) {
            mFullSpan = fullSpan;
        }

        /**
         * Returns whether this View occupies all available spans or just one.
         *
         * @return True if the View occupies all spans or false otherwise.
         * @see #setFullSpan(boolean)
         */
        public boolean isFullSpan() {
            return mFullSpan;
        }

        /**
         * Returns the Span index to which this View is assigned.
         *
         * @return The Span index of the View. If View is not yet assigned to any span, returns
        {@link #INVALID_SPAN_ID}.
         */
        public final int getSpanIndex() {
            if (mSpan == null) {
                return android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams.INVALID_SPAN_ID;
            }
            return mSpan.mIndex;
        }
    }

    // Package scoped to access from tests.
    class Span {
        static final int INVALID_LINE = java.lang.Integer.MIN_VALUE;

        java.util.ArrayList<android.view.View> mViews = new java.util.ArrayList<>();

        int mCachedStart = android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE;

        int mCachedEnd = android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE;

        int mDeletedSize = 0;

        final int mIndex;

        Span(int index) {
            mIndex = index;
        }

        int getStartLine(int def) {
            if (mCachedStart != android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE) {
                return mCachedStart;
            }
            if (mViews.size() == 0) {
                return def;
            }
            calculateCachedStart();
            return mCachedStart;
        }

        void calculateCachedStart() {
            final android.view.View startView = mViews.get(0);
            final android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams lp = getLayoutParams(startView);
            mCachedStart = mPrimaryOrientation.getDecoratedStart(startView);
            if (lp.mFullSpan) {
                android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fsi = mLazySpanLookup.getFullSpanItem(lp.getViewLayoutPosition());
                if ((fsi != null) && (fsi.mGapDir == android.support.v7.widget.LayoutState.LAYOUT_START)) {
                    mCachedStart -= fsi.getGapForSpan(mIndex);
                }
            }
        }

        // Use this one when default value does not make sense and not having a value means a bug.
        int getStartLine() {
            if (mCachedStart != android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE) {
                return mCachedStart;
            }
            calculateCachedStart();
            return mCachedStart;
        }

        int getEndLine(int def) {
            if (mCachedEnd != android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE) {
                return mCachedEnd;
            }
            final int size = mViews.size();
            if (size == 0) {
                return def;
            }
            calculateCachedEnd();
            return mCachedEnd;
        }

        void calculateCachedEnd() {
            final android.view.View endView = mViews.get(mViews.size() - 1);
            final android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams lp = getLayoutParams(endView);
            mCachedEnd = mPrimaryOrientation.getDecoratedEnd(endView);
            if (lp.mFullSpan) {
                android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fsi = mLazySpanLookup.getFullSpanItem(lp.getViewLayoutPosition());
                if ((fsi != null) && (fsi.mGapDir == android.support.v7.widget.LayoutState.LAYOUT_END)) {
                    mCachedEnd += fsi.getGapForSpan(mIndex);
                }
            }
        }

        // Use this one when default value does not make sense and not having a value means a bug.
        int getEndLine() {
            if (mCachedEnd != android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE) {
                return mCachedEnd;
            }
            calculateCachedEnd();
            return mCachedEnd;
        }

        void prependToSpan(android.view.View view) {
            android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams lp = getLayoutParams(view);
            lp.mSpan = this;
            mViews.add(0, view);
            mCachedStart = android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE;
            if (mViews.size() == 1) {
                mCachedEnd = android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE;
            }
            if (lp.isItemRemoved() || lp.isItemChanged()) {
                mDeletedSize += mPrimaryOrientation.getDecoratedMeasurement(view);
            }
        }

        void appendToSpan(android.view.View view) {
            android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams lp = getLayoutParams(view);
            lp.mSpan = this;
            mViews.add(view);
            mCachedEnd = android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE;
            if (mViews.size() == 1) {
                mCachedStart = android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE;
            }
            if (lp.isItemRemoved() || lp.isItemChanged()) {
                mDeletedSize += mPrimaryOrientation.getDecoratedMeasurement(view);
            }
        }

        // Useful method to preserve positions on a re-layout.
        void cacheReferenceLineAndClear(boolean reverseLayout, int offset) {
            int reference;
            if (reverseLayout) {
                reference = getEndLine(android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE);
            } else {
                reference = getStartLine(android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE);
            }
            clear();
            if (reference == android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE) {
                return;
            }
            if ((reverseLayout && (reference < mPrimaryOrientation.getEndAfterPadding())) || ((!reverseLayout) && (reference > mPrimaryOrientation.getStartAfterPadding()))) {
                return;
            }
            if (offset != android.support.v7.widget.StaggeredGridLayoutManager.INVALID_OFFSET) {
                reference += offset;
            }
            mCachedStart = mCachedEnd = reference;
        }

        void clear() {
            mViews.clear();
            invalidateCache();
            mDeletedSize = 0;
        }

        void invalidateCache() {
            mCachedStart = android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE;
            mCachedEnd = android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE;
        }

        void setLine(int line) {
            mCachedEnd = mCachedStart = line;
        }

        void popEnd() {
            final int size = mViews.size();
            android.view.View end = mViews.remove(size - 1);
            final android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams lp = getLayoutParams(end);
            lp.mSpan = null;
            if (lp.isItemRemoved() || lp.isItemChanged()) {
                mDeletedSize -= mPrimaryOrientation.getDecoratedMeasurement(end);
            }
            if (size == 1) {
                mCachedStart = android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE;
            }
            mCachedEnd = android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE;
        }

        void popStart() {
            android.view.View start = mViews.remove(0);
            final android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams lp = getLayoutParams(start);
            lp.mSpan = null;
            if (mViews.size() == 0) {
                mCachedEnd = android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE;
            }
            if (lp.isItemRemoved() || lp.isItemChanged()) {
                mDeletedSize -= mPrimaryOrientation.getDecoratedMeasurement(start);
            }
            mCachedStart = android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE;
        }

        public int getDeletedSize() {
            return mDeletedSize;
        }

        android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams getLayoutParams(android.view.View view) {
            return ((android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) (view.getLayoutParams()));
        }

        void onOffset(int dt) {
            if (mCachedStart != android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE) {
                mCachedStart += dt;
            }
            if (mCachedEnd != android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE) {
                mCachedEnd += dt;
            }
        }

        public int findFirstVisibleItemPosition() {
            return mReverseLayout ? findOneVisibleChild(mViews.size() - 1, -1, false) : findOneVisibleChild(0, mViews.size(), false);
        }

        public int findFirstCompletelyVisibleItemPosition() {
            return mReverseLayout ? findOneVisibleChild(mViews.size() - 1, -1, true) : findOneVisibleChild(0, mViews.size(), true);
        }

        public int findLastVisibleItemPosition() {
            return mReverseLayout ? findOneVisibleChild(0, mViews.size(), false) : findOneVisibleChild(mViews.size() - 1, -1, false);
        }

        public int findLastCompletelyVisibleItemPosition() {
            return mReverseLayout ? findOneVisibleChild(0, mViews.size(), true) : findOneVisibleChild(mViews.size() - 1, -1, true);
        }

        int findOneVisibleChild(int fromIndex, int toIndex, boolean completelyVisible) {
            final int start = mPrimaryOrientation.getStartAfterPadding();
            final int end = mPrimaryOrientation.getEndAfterPadding();
            final int next = (toIndex > fromIndex) ? 1 : -1;
            for (int i = fromIndex; i != toIndex; i += next) {
                final android.view.View child = mViews.get(i);
                final int childStart = mPrimaryOrientation.getDecoratedStart(child);
                final int childEnd = mPrimaryOrientation.getDecoratedEnd(child);
                if ((childStart < end) && (childEnd > start)) {
                    if (completelyVisible) {
                        if ((childStart >= start) && (childEnd <= end)) {
                            return getPosition(child);
                        }
                    } else {
                        return getPosition(child);
                    }
                }
            }
            return android.support.v7.widget.RecyclerView.NO_POSITION;
        }

        /**
         * Depending on the layout direction, returns the View that is after the given position.
         */
        public android.view.View getFocusableViewAfter(int referenceChildPosition, int layoutDir) {
            android.view.View candidate = null;
            if (layoutDir == android.support.v7.widget.LayoutState.LAYOUT_START) {
                final int limit = mViews.size();
                for (int i = 0; i < limit; i++) {
                    final android.view.View view = mViews.get(i);
                    if (view.isFocusable() && ((getPosition(view) > referenceChildPosition) == mReverseLayout)) {
                        candidate = view;
                    } else {
                        break;
                    }
                }
            } else {
                for (int i = mViews.size() - 1; i >= 0; i--) {
                    final android.view.View view = mViews.get(i);
                    if (view.isFocusable() && ((getPosition(view) > referenceChildPosition) == (!mReverseLayout))) {
                        candidate = view;
                    } else {
                        break;
                    }
                }
            }
            return candidate;
        }
    }

    /**
     * An array of mappings from adapter position to span.
     * This only grows when a write happens and it grows up to the size of the adapter.
     */
    static class LazySpanLookup {
        private static final int MIN_SIZE = 10;

        int[] mData;

        java.util.List<android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem> mFullSpanItems;

        /**
         * Invalidates everything after this position, including full span information
         */
        int forceInvalidateAfter(int position) {
            if (mFullSpanItems != null) {
                for (int i = mFullSpanItems.size() - 1; i >= 0; i--) {
                    android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fsi = mFullSpanItems.get(i);
                    if (fsi.mPosition >= position) {
                        mFullSpanItems.remove(i);
                    }
                }
            }
            return invalidateAfter(position);
        }

        /**
         * returns end position for invalidation.
         */
        int invalidateAfter(int position) {
            if (mData == null) {
                return android.support.v7.widget.RecyclerView.NO_POSITION;
            }
            if (position >= mData.length) {
                return android.support.v7.widget.RecyclerView.NO_POSITION;
            }
            int endPosition = invalidateFullSpansAfter(position);
            if (endPosition == android.support.v7.widget.RecyclerView.NO_POSITION) {
                java.util.Arrays.fill(mData, position, mData.length, android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams.INVALID_SPAN_ID);
                return mData.length;
            } else {
                // just invalidate items in between
                java.util.Arrays.fill(mData, position, endPosition + 1, android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams.INVALID_SPAN_ID);
                return endPosition + 1;
            }
        }

        int getSpan(int position) {
            if ((mData == null) || (position >= mData.length)) {
                return android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams.INVALID_SPAN_ID;
            } else {
                return mData[position];
            }
        }

        void setSpan(int position, android.support.v7.widget.StaggeredGridLayoutManager.Span span) {
            ensureSize(position);
            mData[position] = span.mIndex;
        }

        int sizeForPosition(int position) {
            int len = mData.length;
            while (len <= position) {
                len *= 2;
            } 
            return len;
        }

        void ensureSize(int position) {
            if (mData == null) {
                mData = new int[java.lang.Math.max(position, android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.MIN_SIZE) + 1];
                java.util.Arrays.fill(mData, android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams.INVALID_SPAN_ID);
            } else
                if (position >= mData.length) {
                    int[] old = mData;
                    mData = new int[sizeForPosition(position)];
                    java.lang.System.arraycopy(old, 0, mData, 0, old.length);
                    java.util.Arrays.fill(mData, old.length, mData.length, android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams.INVALID_SPAN_ID);
                }

        }

        void clear() {
            if (mData != null) {
                java.util.Arrays.fill(mData, android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams.INVALID_SPAN_ID);
            }
            mFullSpanItems = null;
        }

        void offsetForRemoval(int positionStart, int itemCount) {
            if ((mData == null) || (positionStart >= mData.length)) {
                return;
            }
            ensureSize(positionStart + itemCount);
            java.lang.System.arraycopy(mData, positionStart + itemCount, mData, positionStart, (mData.length - positionStart) - itemCount);
            java.util.Arrays.fill(mData, mData.length - itemCount, mData.length, android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams.INVALID_SPAN_ID);
            offsetFullSpansForRemoval(positionStart, itemCount);
        }

        private void offsetFullSpansForRemoval(int positionStart, int itemCount) {
            if (mFullSpanItems == null) {
                return;
            }
            final int end = positionStart + itemCount;
            for (int i = mFullSpanItems.size() - 1; i >= 0; i--) {
                android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fsi = mFullSpanItems.get(i);
                if (fsi.mPosition < positionStart) {
                    continue;
                }
                if (fsi.mPosition < end) {
                    mFullSpanItems.remove(i);
                } else {
                    fsi.mPosition -= itemCount;
                }
            }
        }

        void offsetForAddition(int positionStart, int itemCount) {
            if ((mData == null) || (positionStart >= mData.length)) {
                return;
            }
            ensureSize(positionStart + itemCount);
            java.lang.System.arraycopy(mData, positionStart, mData, positionStart + itemCount, (mData.length - positionStart) - itemCount);
            java.util.Arrays.fill(mData, positionStart, positionStart + itemCount, android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams.INVALID_SPAN_ID);
            offsetFullSpansForAddition(positionStart, itemCount);
        }

        private void offsetFullSpansForAddition(int positionStart, int itemCount) {
            if (mFullSpanItems == null) {
                return;
            }
            for (int i = mFullSpanItems.size() - 1; i >= 0; i--) {
                android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fsi = mFullSpanItems.get(i);
                if (fsi.mPosition < positionStart) {
                    continue;
                }
                fsi.mPosition += itemCount;
            }
        }

        /**
         * Returns when invalidation should end. e.g. hitting a full span position.
         * Returned position SHOULD BE invalidated.
         */
        private int invalidateFullSpansAfter(int position) {
            if (mFullSpanItems == null) {
                return android.support.v7.widget.RecyclerView.NO_POSITION;
            }
            final android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem item = getFullSpanItem(position);
            // if there is an fsi at this position, get rid of it.
            if (item != null) {
                mFullSpanItems.remove(item);
            }
            int nextFsiIndex = -1;
            final int count = mFullSpanItems.size();
            for (int i = 0; i < count; i++) {
                android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fsi = mFullSpanItems.get(i);
                if (fsi.mPosition >= position) {
                    nextFsiIndex = i;
                    break;
                }
            }
            if (nextFsiIndex != (-1)) {
                android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fsi = mFullSpanItems.get(nextFsiIndex);
                mFullSpanItems.remove(nextFsiIndex);
                return fsi.mPosition;
            }
            return android.support.v7.widget.RecyclerView.NO_POSITION;
        }

        public void addFullSpanItem(android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fullSpanItem) {
            if (mFullSpanItems == null) {
                mFullSpanItems = new java.util.ArrayList<>();
            }
            final int size = mFullSpanItems.size();
            for (int i = 0; i < size; i++) {
                android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem other = mFullSpanItems.get(i);
                if (other.mPosition == fullSpanItem.mPosition) {
                    if (android.support.v7.widget.StaggeredGridLayoutManager.DEBUG) {
                        throw new java.lang.IllegalStateException("two fsis for same position");
                    } else {
                        mFullSpanItems.remove(i);
                    }
                }
                if (other.mPosition >= fullSpanItem.mPosition) {
                    mFullSpanItems.add(i, fullSpanItem);
                    return;
                }
            }
            // if it is not added to a position.
            mFullSpanItems.add(fullSpanItem);
        }

        public android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem getFullSpanItem(int position) {
            if (mFullSpanItems == null) {
                return null;
            }
            for (int i = mFullSpanItems.size() - 1; i >= 0; i--) {
                final android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fsi = mFullSpanItems.get(i);
                if (fsi.mPosition == position) {
                    return fsi;
                }
            }
            return null;
        }

        /**
         *
         *
         * @param minPos
         * 		inclusive
         * @param maxPos
         * 		exclusive
         * @param gapDir
         * 		if not 0, returns FSIs on in that direction
         * @param hasUnwantedGapAfter
         * 		If true, when full span item has unwanted gaps, it will be
         * 		returned even if its gap direction does not match.
         */
        public android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem getFirstFullSpanItemInRange(int minPos, int maxPos, int gapDir, boolean hasUnwantedGapAfter) {
            if (mFullSpanItems == null) {
                return null;
            }
            final int limit = mFullSpanItems.size();
            for (int i = 0; i < limit; i++) {
                android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem fsi = mFullSpanItems.get(i);
                if (fsi.mPosition >= maxPos) {
                    return null;
                }
                if ((fsi.mPosition >= minPos) && (((gapDir == 0) || (fsi.mGapDir == gapDir)) || (hasUnwantedGapAfter && fsi.mHasUnwantedGapAfter))) {
                    return fsi;
                }
            }
            return null;
        }

        /**
         * We keep information about full span items because they may create gaps in the UI.
         */
        static class FullSpanItem implements android.os.Parcelable {
            int mPosition;

            int mGapDir;

            int[] mGapPerSpan;

            // A full span may be laid out in primary direction but may have gaps due to
            // invalidation of views after it. This is recorded during a reverse scroll and if
            // view is still on the screen after scroll stops, we have to recalculate layout
            boolean mHasUnwantedGapAfter;

            public FullSpanItem(android.os.Parcel in) {
                mPosition = in.readInt();
                mGapDir = in.readInt();
                mHasUnwantedGapAfter = in.readInt() == 1;
                int spanCount = in.readInt();
                if (spanCount > 0) {
                    mGapPerSpan = new int[spanCount];
                    in.readIntArray(mGapPerSpan);
                }
            }

            public FullSpanItem() {
            }

            int getGapForSpan(int spanIndex) {
                return mGapPerSpan == null ? 0 : mGapPerSpan[spanIndex];
            }

            @java.lang.Override
            public int describeContents() {
                return 0;
            }

            @java.lang.Override
            public void writeToParcel(android.os.Parcel dest, int flags) {
                dest.writeInt(mPosition);
                dest.writeInt(mGapDir);
                dest.writeInt(mHasUnwantedGapAfter ? 1 : 0);
                if ((mGapPerSpan != null) && (mGapPerSpan.length > 0)) {
                    dest.writeInt(mGapPerSpan.length);
                    dest.writeIntArray(mGapPerSpan);
                } else {
                    dest.writeInt(0);
                }
            }

            @java.lang.Override
            public java.lang.String toString() {
                return (((((((("FullSpanItem{" + "mPosition=") + mPosition) + ", mGapDir=") + mGapDir) + ", mHasUnwantedGapAfter=") + mHasUnwantedGapAfter) + ", mGapPerSpan=") + java.util.Arrays.toString(mGapPerSpan)) + '}';
            }

            public static final android.os.Parcelable.Creator<android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem> CREATOR = new android.os.Parcelable.Creator<android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem>() {
                @java.lang.Override
                public android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFromParcel(android.os.Parcel in) {
                    return new android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem(in);
                }

                @java.lang.Override
                public android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[] newArray(int size) {
                    return new android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[size];
                }
            };
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    public static class SavedState implements android.os.Parcelable {
        int mAnchorPosition;

        int mVisibleAnchorPosition;// Replacement for span info when spans are invalidated


        int mSpanOffsetsSize;

        int[] mSpanOffsets;

        int mSpanLookupSize;

        int[] mSpanLookup;

        java.util.List<android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem> mFullSpanItems;

        boolean mReverseLayout;

        boolean mAnchorLayoutFromEnd;

        boolean mLastLayoutRTL;

        public SavedState() {
        }

        SavedState(android.os.Parcel in) {
            mAnchorPosition = in.readInt();
            mVisibleAnchorPosition = in.readInt();
            mSpanOffsetsSize = in.readInt();
            if (mSpanOffsetsSize > 0) {
                mSpanOffsets = new int[mSpanOffsetsSize];
                in.readIntArray(mSpanOffsets);
            }
            mSpanLookupSize = in.readInt();
            if (mSpanLookupSize > 0) {
                mSpanLookup = new int[mSpanLookupSize];
                in.readIntArray(mSpanLookup);
            }
            mReverseLayout = in.readInt() == 1;
            mAnchorLayoutFromEnd = in.readInt() == 1;
            mLastLayoutRTL = in.readInt() == 1;
            // noinspection unchecked
            mFullSpanItems = in.readArrayList(android.support.v7.widget.StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem.class.getClassLoader());
        }

        public SavedState(android.support.v7.widget.StaggeredGridLayoutManager.SavedState other) {
            mSpanOffsetsSize = other.mSpanOffsetsSize;
            mAnchorPosition = other.mAnchorPosition;
            mVisibleAnchorPosition = other.mVisibleAnchorPosition;
            mSpanOffsets = other.mSpanOffsets;
            mSpanLookupSize = other.mSpanLookupSize;
            mSpanLookup = other.mSpanLookup;
            mReverseLayout = other.mReverseLayout;
            mAnchorLayoutFromEnd = other.mAnchorLayoutFromEnd;
            mLastLayoutRTL = other.mLastLayoutRTL;
            mFullSpanItems = other.mFullSpanItems;
        }

        void invalidateSpanInfo() {
            mSpanOffsets = null;
            mSpanOffsetsSize = 0;
            mSpanLookupSize = 0;
            mSpanLookup = null;
            mFullSpanItems = null;
        }

        void invalidateAnchorPositionInfo() {
            mSpanOffsets = null;
            mSpanOffsetsSize = 0;
            mAnchorPosition = android.support.v7.widget.RecyclerView.NO_POSITION;
            mVisibleAnchorPosition = android.support.v7.widget.RecyclerView.NO_POSITION;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(mAnchorPosition);
            dest.writeInt(mVisibleAnchorPosition);
            dest.writeInt(mSpanOffsetsSize);
            if (mSpanOffsetsSize > 0) {
                dest.writeIntArray(mSpanOffsets);
            }
            dest.writeInt(mSpanLookupSize);
            if (mSpanLookupSize > 0) {
                dest.writeIntArray(mSpanLookup);
            }
            dest.writeInt(mReverseLayout ? 1 : 0);
            dest.writeInt(mAnchorLayoutFromEnd ? 1 : 0);
            dest.writeInt(mLastLayoutRTL ? 1 : 0);
            dest.writeList(mFullSpanItems);
        }

        public static final android.os.Parcelable.Creator<android.support.v7.widget.StaggeredGridLayoutManager.SavedState> CREATOR = new android.os.Parcelable.Creator<android.support.v7.widget.StaggeredGridLayoutManager.SavedState>() {
            @java.lang.Override
            public android.support.v7.widget.StaggeredGridLayoutManager.SavedState createFromParcel(android.os.Parcel in) {
                return new android.support.v7.widget.StaggeredGridLayoutManager.SavedState(in);
            }

            @java.lang.Override
            public android.support.v7.widget.StaggeredGridLayoutManager.SavedState[] newArray(int size) {
                return new android.support.v7.widget.StaggeredGridLayoutManager.SavedState[size];
            }
        };
    }

    /**
     * Data class to hold the information about an anchor position which is used in onLayout call.
     */
    class AnchorInfo {
        int mPosition;

        int mOffset;

        boolean mLayoutFromEnd;

        boolean mInvalidateOffsets;

        boolean mValid;

        // this is where we save span reference lines in case we need to re-use them for multi-pass
        // measure steps
        int[] mSpanReferenceLines;

        public AnchorInfo() {
            reset();
        }

        void reset() {
            mPosition = android.support.v7.widget.RecyclerView.NO_POSITION;
            mOffset = android.support.v7.widget.StaggeredGridLayoutManager.INVALID_OFFSET;
            mLayoutFromEnd = false;
            mInvalidateOffsets = false;
            mValid = false;
            if (mSpanReferenceLines != null) {
                java.util.Arrays.fill(mSpanReferenceLines, -1);
            }
        }

        void saveSpanReferenceLines(android.support.v7.widget.StaggeredGridLayoutManager.Span[] spans) {
            int spanCount = spans.length;
            if ((mSpanReferenceLines == null) || (mSpanReferenceLines.length < spanCount)) {
                mSpanReferenceLines = new int[mSpans.length];
            }
            for (int i = 0; i < spanCount; i++) {
                // does not matter start or end since this is only recorded when span is reset
                mSpanReferenceLines[i] = spans[i].getStartLine(android.support.v7.widget.StaggeredGridLayoutManager.Span.INVALID_LINE);
            }
        }

        void assignCoordinateFromPadding() {
            mOffset = (mLayoutFromEnd) ? mPrimaryOrientation.getEndAfterPadding() : mPrimaryOrientation.getStartAfterPadding();
        }

        void assignCoordinateFromPadding(int addedDistance) {
            if (mLayoutFromEnd) {
                mOffset = mPrimaryOrientation.getEndAfterPadding() - addedDistance;
            } else {
                mOffset = mPrimaryOrientation.getStartAfterPadding() + addedDistance;
            }
        }
    }
}

