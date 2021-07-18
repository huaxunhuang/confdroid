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


final class GridLayoutManager extends android.support.v7.widget.RecyclerView.LayoutManager {
    /* LayoutParams for {@link HorizontalGridView} and {@link VerticalGridView}.
    The class currently does two internal jobs:
    - Saves optical bounds insets.
    - Caches focus align view center.
     */
    static final class LayoutParams extends android.support.v7.widget.RecyclerView.LayoutParams {
        // For placement
        int mLeftInset;

        int mTopInset;

        int mRightInset;

        int mBottomInset;

        // For alignment
        private int mAlignX;

        private int mAlignY;

        private int[] mAlignMultiple;

        private android.support.v17.leanback.widget.ItemAlignmentFacet mAlignmentFacet;

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

        public LayoutParams(android.support.v17.leanback.widget.GridLayoutManager.LayoutParams source) {
            super(source);
        }

        int getAlignX() {
            return mAlignX;
        }

        int getAlignY() {
            return mAlignY;
        }

        int getOpticalLeft(android.view.View view) {
            return view.getLeft() + mLeftInset;
        }

        int getOpticalTop(android.view.View view) {
            return view.getTop() + mTopInset;
        }

        int getOpticalRight(android.view.View view) {
            return view.getRight() - mRightInset;
        }

        int getOpticalBottom(android.view.View view) {
            return view.getBottom() - mBottomInset;
        }

        int getOpticalWidth(android.view.View view) {
            return (view.getWidth() - mLeftInset) - mRightInset;
        }

        int getOpticalHeight(android.view.View view) {
            return (view.getHeight() - mTopInset) - mBottomInset;
        }

        int getOpticalLeftInset() {
            return mLeftInset;
        }

        int getOpticalRightInset() {
            return mRightInset;
        }

        int getOpticalTopInset() {
            return mTopInset;
        }

        int getOpticalBottomInset() {
            return mBottomInset;
        }

        void setAlignX(int alignX) {
            mAlignX = alignX;
        }

        void setAlignY(int alignY) {
            mAlignY = alignY;
        }

        void setItemAlignmentFacet(android.support.v17.leanback.widget.ItemAlignmentFacet facet) {
            mAlignmentFacet = facet;
        }

        android.support.v17.leanback.widget.ItemAlignmentFacet getItemAlignmentFacet() {
            return mAlignmentFacet;
        }

        void calculateItemAlignments(int orientation, android.view.View view) {
            android.support.v17.leanback.widget.ItemAlignmentFacet.ItemAlignmentDef[] defs = mAlignmentFacet.getAlignmentDefs();
            if ((mAlignMultiple == null) || (mAlignMultiple.length != defs.length)) {
                mAlignMultiple = new int[defs.length];
            }
            for (int i = 0; i < defs.length; i++) {
                mAlignMultiple[i] = android.support.v17.leanback.widget.ItemAlignmentFacetHelper.getAlignmentPosition(view, defs[i], orientation);
            }
            if (orientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
                mAlignX = mAlignMultiple[0];
            } else {
                mAlignY = mAlignMultiple[0];
            }
        }

        int[] getAlignMultiple() {
            return mAlignMultiple;
        }

        void setOpticalInsets(int leftInset, int topInset, int rightInset, int bottomInset) {
            mLeftInset = leftInset;
            mTopInset = topInset;
            mRightInset = rightInset;
            mBottomInset = bottomInset;
        }
    }

    /**
     * Base class which scrolls to selected view in onStop().
     */
    abstract class GridLinearSmoothScroller extends android.support.v7.widget.LinearSmoothScroller {
        GridLinearSmoothScroller() {
            super(mBaseGridView.getContext());
        }

        @java.lang.Override
        protected void onStop() {
            // onTargetFound() may not be called if we hit the "wall" first or get cancelled.
            android.view.View targetView = findViewByPosition(getTargetPosition());
            if (targetView == null) {
                if (getTargetPosition() >= 0) {
                    // if smooth scroller is stopped without target, immediately jumps
                    // to the target position.
                    scrollToSelection(getTargetPosition(), 0, false, 0);
                }
                super.onStop();
                return;
            }
            if (hasFocus()) {
                mInSelection = true;
                targetView.requestFocus();
                mInSelection = false;
            }
            dispatchChildSelected();
            super.onStop();
        }

        @java.lang.Override
        protected int calculateTimeForScrolling(int dx) {
            int ms = super.calculateTimeForScrolling(dx);
            if (mWindowAlignment.mainAxis().getSize() > 0) {
                float minMs = (((float) (android.support.v17.leanback.widget.GridLayoutManager.MIN_MS_SMOOTH_SCROLL_MAIN_SCREEN)) / mWindowAlignment.mainAxis().getSize()) * dx;
                if (ms < minMs) {
                    ms = ((int) (minMs));
                }
            }
            return ms;
        }

        @java.lang.Override
        protected void onTargetFound(android.view.View targetView, android.support.v7.widget.RecyclerView.State state, android.support.v7.widget.RecyclerView.SmoothScroller.Action action) {
            if (getScrollPosition(targetView, null, android.support.v17.leanback.widget.GridLayoutManager.sTwoInts)) {
                int dx;
                int dy;
                if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
                    dx = android.support.v17.leanback.widget.GridLayoutManager.sTwoInts[0];
                    dy = android.support.v17.leanback.widget.GridLayoutManager.sTwoInts[1];
                } else {
                    dx = android.support.v17.leanback.widget.GridLayoutManager.sTwoInts[1];
                    dy = android.support.v17.leanback.widget.GridLayoutManager.sTwoInts[0];
                }
                final int distance = ((int) (java.lang.Math.sqrt((dx * dx) + (dy * dy))));
                final int time = calculateTimeForDeceleration(distance);
                action.update(dx, dy, time, mDecelerateInterpolator);
            }
        }
    }

    /**
     * The SmoothScroller that remembers pending DPAD keys and consume pending keys
     * during scroll.
     */
    final class PendingMoveSmoothScroller extends android.support.v17.leanback.widget.GridLayoutManager.GridLinearSmoothScroller {
        // -2 is a target position that LinearSmoothScroller can never find until
        // consumePendingMovesXXX() sets real targetPosition.
        static final int TARGET_UNDEFINED = -2;

        // whether the grid is staggered.
        private final boolean mStaggeredGrid;

        // Number of pending movements on primary direction, negative if PREV_ITEM.
        private int mPendingMoves;

        PendingMoveSmoothScroller(int initialPendingMoves, boolean staggeredGrid) {
            mPendingMoves = initialPendingMoves;
            mStaggeredGrid = staggeredGrid;
            setTargetPosition(android.support.v17.leanback.widget.GridLayoutManager.PendingMoveSmoothScroller.TARGET_UNDEFINED);
        }

        void increasePendingMoves() {
            if (mPendingMoves < android.support.v17.leanback.widget.GridLayoutManager.MAX_PENDING_MOVES) {
                mPendingMoves++;
            }
        }

        void decreasePendingMoves() {
            if (mPendingMoves > (-android.support.v17.leanback.widget.GridLayoutManager.MAX_PENDING_MOVES)) {
                mPendingMoves--;
            }
        }

        /**
         * Called before laid out an item when non-staggered grid can handle pending movements
         * by skipping "mNumRows" per movement;  staggered grid will have to wait the item
         * has been laid out in consumePendingMovesAfterLayout().
         */
        void consumePendingMovesBeforeLayout() {
            if (mStaggeredGrid || (mPendingMoves == 0)) {
                return;
            }
            android.view.View newSelected = null;
            int startPos = (mPendingMoves > 0) ? mFocusPosition + mNumRows : mFocusPosition - mNumRows;
            for (int pos = startPos; mPendingMoves != 0; pos = (mPendingMoves > 0) ? pos + mNumRows : pos - mNumRows) {
                android.view.View v = findViewByPosition(pos);
                if (v == null) {
                    break;
                }
                if (!canScrollTo(v)) {
                    continue;
                }
                newSelected = v;
                mFocusPosition = pos;
                mSubFocusPosition = 0;
                if (mPendingMoves > 0) {
                    mPendingMoves--;
                } else {
                    mPendingMoves++;
                }
            }
            if ((newSelected != null) && hasFocus()) {
                mInSelection = true;
                newSelected.requestFocus();
                mInSelection = false;
            }
        }

        /**
         * Called after laid out an item.  Staggered grid should find view on same
         * Row and consume pending movements.
         */
        void consumePendingMovesAfterLayout() {
            if (mStaggeredGrid && (mPendingMoves != 0)) {
                // consume pending moves, focus to item on the same row.
                mPendingMoves = processSelectionMoves(true, mPendingMoves);
            }
            if (((mPendingMoves == 0) || ((mPendingMoves > 0) && hasCreatedLastItem())) || ((mPendingMoves < 0) && hasCreatedFirstItem())) {
                setTargetPosition(mFocusPosition);
                stop();
            }
        }

        @java.lang.Override
        protected void updateActionForInterimTarget(android.support.v7.widget.RecyclerView.SmoothScroller.Action action) {
            if (mPendingMoves == 0) {
                return;
            }
            super.updateActionForInterimTarget(action);
        }

        @java.lang.Override
        public android.graphics.PointF computeScrollVectorForPosition(int targetPosition) {
            if (mPendingMoves == 0) {
                return null;
            }
            int direction = (mReverseFlowPrimary ? mPendingMoves > 0 : mPendingMoves < 0) ? -1 : 1;
            if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
                return new android.graphics.PointF(direction, 0);
            } else {
                return new android.graphics.PointF(0, direction);
            }
        }

        @java.lang.Override
        protected void onStop() {
            super.onStop();
            // if we hit wall,  need clear the remaining pending moves.
            mPendingMoves = 0;
            mPendingMoveSmoothScroller = null;
            android.view.View v = findViewByPosition(getTargetPosition());
            if (v != null)
                scrollToView(v, true);

        }
    }

    private static final java.lang.String TAG = "GridLayoutManager";

    static final boolean DEBUG = false;

    static final boolean TRACE = false;

    // maximum pending movement in one direction.
    static final int MAX_PENDING_MOVES = 10;

    // minimal milliseconds to scroll window size in major direction,  we put a cap to prevent the
    // effect smooth scrolling too over to bind an item view then drag the item view back.
    static final int MIN_MS_SMOOTH_SCROLL_MAIN_SCREEN = 30;

    java.lang.String getTag() {
        return (android.support.v17.leanback.widget.GridLayoutManager.TAG + ":") + mBaseGridView.getId();
    }

    final android.support.v17.leanback.widget.BaseGridView mBaseGridView;

    /**
     * Note on conventions in the presence of RTL layout directions:
     * Many properties and method names reference entities related to the
     * beginnings and ends of things.  In the presence of RTL flows,
     * it may not be clear whether this is intended to reference a
     * quantity that changes direction in RTL cases, or a quantity that
     * does not.  Here are the conventions in use:
     *
     * start/end: coordinate quantities - do reverse
     * (optical) left/right: coordinate quantities - do not reverse
     * low/high: coordinate quantities - do not reverse
     * min/max: coordinate quantities - do not reverse
     * scroll offset - coordinate quantities - do not reverse
     * first/last: positional indices - do not reverse
     * front/end: positional indices - do not reverse
     * prepend/append: related to positional indices - do not reverse
     *
     * Note that although quantities do not reverse in RTL flows, their
     * relationship does.  In LTR flows, the first positional index is
     * leftmost; in RTL flows, it is rightmost.  Thus, anywhere that
     * positional quantities are mapped onto coordinate quantities,
     * the flow must be checked and the logic reversed.
     */
    /**
     * The orientation of a "row".
     */
    int mOrientation = android.support.v7.widget.RecyclerView.HORIZONTAL;

    private android.support.v7.widget.OrientationHelper mOrientationHelper = android.support.v7.widget.OrientationHelper.createHorizontalHelper(this);

    android.support.v7.widget.RecyclerView.State mState;

    android.support.v7.widget.RecyclerView.Recycler mRecycler;

    private static final android.graphics.Rect sTempRect = new android.graphics.Rect();

    boolean mInLayout;

    private boolean mInScroll;

    boolean mInFastRelayout;

    /**
     * During full layout pass, when GridView had focus: onLayoutChildren will
     * skip non-focusable child and adjust mFocusPosition.
     */
    boolean mInLayoutSearchFocus;

    boolean mInSelection = false;

    private android.support.v17.leanback.widget.OnChildSelectedListener mChildSelectedListener = null;

    private java.util.ArrayList<android.support.v17.leanback.widget.OnChildViewHolderSelectedListener> mChildViewHolderSelectedListeners = null;

    android.support.v17.leanback.widget.OnChildLaidOutListener mChildLaidOutListener = null;

    /**
     * The focused position, it's not the currently visually aligned position
     * but it is the final position that we intend to focus on. If there are
     * multiple setSelection() called, mFocusPosition saves last value.
     */
    int mFocusPosition = android.support.v7.widget.RecyclerView.NO_POSITION;

    /**
     * A view can have multiple alignment position,  this is the index of which
     * alignment is used,  by default is 0.
     */
    int mSubFocusPosition = 0;

    /**
     * LinearSmoothScroller that consume pending DPAD movements.
     */
    android.support.v17.leanback.widget.GridLayoutManager.PendingMoveSmoothScroller mPendingMoveSmoothScroller;

    /**
     * The offset to be applied to mFocusPosition, due to adapter change, on the next
     * layout.  Set to Integer.MIN_VALUE means we should stop adding delta to mFocusPosition
     * until next layout cycler.
     * TODO:  This is somewhat duplication of RecyclerView getOldPosition() which is
     * unfortunately cleared after prelayout.
     */
    private int mFocusPositionOffset = 0;

    /**
     * Extra pixels applied on primary direction.
     */
    private int mPrimaryScrollExtra;

    /**
     * Force a full layout under certain situations.  E.g. Rows change, jump to invisible child.
     */
    private boolean mForceFullLayout;

    /**
     * True if layout is enabled.
     */
    private boolean mLayoutEnabled = true;

    /**
     * override child visibility
     */
    int mChildVisibility = -1;

    /**
     * The scroll offsets of the viewport relative to the entire view.
     */
    private int mScrollOffsetPrimary;

    int mScrollOffsetSecondary;

    /**
     * User-specified row height/column width.  Can be WRAP_CONTENT.
     */
    private int mRowSizeSecondaryRequested;

    /**
     * The fixed size of each grid item in the secondary direction. This corresponds to
     * the row height, equal for all rows. Grid items may have variable length
     * in the primary direction.
     */
    private int mFixedRowSizeSecondary;

    /**
     * Tracks the secondary size of each row.
     */
    private int[] mRowSizeSecondary;

    /**
     * Flag controlling whether the current/next layout should
     * be updating the secondary size of rows.
     */
    private boolean mRowSecondarySizeRefresh;

    /**
     * The maximum measured size of the view.
     */
    private int mMaxSizeSecondary;

    /**
     * Margin between items.
     */
    private int mHorizontalMargin;

    /**
     * Margin between items vertically.
     */
    private int mVerticalMargin;

    /**
     * Margin in main direction.
     */
    private int mMarginPrimary;

    /**
     * Margin in second direction.
     */
    private int mMarginSecondary;

    /**
     * How to position child in secondary direction.
     */
    private int mGravity = android.view.Gravity.START | android.view.Gravity.TOP;

    /**
     * The number of rows in the grid.
     */
    int mNumRows;

    /**
     * Number of rows requested, can be 0 to be determined by parent size and
     * rowHeight.
     */
    private int mNumRowsRequested = 1;

    /**
     * Saves grid information of each view.
     */
    android.support.v17.leanback.widget.Grid mGrid;

    /**
     * Focus Scroll strategy.
     */
    private int mFocusScrollStrategy = android.support.v17.leanback.widget.BaseGridView.FOCUS_SCROLL_ALIGNED;

    /**
     * Defines how item view is aligned in the window.
     */
    final android.support.v17.leanback.widget.WindowAlignment mWindowAlignment = new android.support.v17.leanback.widget.WindowAlignment();

    /**
     * Defines how item view is aligned.
     */
    private final android.support.v17.leanback.widget.ItemAlignment mItemAlignment = new android.support.v17.leanback.widget.ItemAlignment();

    /**
     * Dimensions of the view, width or height depending on orientation.
     */
    private int mSizePrimary;

    /**
     * Pixels of extra space for layout item (outside the widget)
     */
    private int mExtraLayoutSpace;

    /**
     * Allow DPAD key to navigate out at the front of the View (where position = 0),
     *  default is false.
     */
    private boolean mFocusOutFront;

    /**
     * Allow DPAD key to navigate out at the end of the view, default is false.
     */
    private boolean mFocusOutEnd;

    /**
     * Allow DPAD key to navigate out of second axis.
     *  default is true.
     */
    private boolean mFocusOutSideStart = true;

    /**
     * Allow DPAD key to navigate out of second axis.
     */
    private boolean mFocusOutSideEnd = true;

    /**
     * True if focus search is disabled.
     */
    private boolean mFocusSearchDisabled;

    /**
     * True if prune child,  might be disabled during transition.
     */
    private boolean mPruneChild = true;

    /**
     * True if scroll content,  might be disabled during transition.
     */
    private boolean mScrollEnabled = true;

    /**
     * Temporary variable: an int array of length=2.
     */
    static int[] sTwoInts = new int[2];

    /**
     * Set to true for RTL layout in horizontal orientation
     */
    boolean mReverseFlowPrimary = false;

    /**
     * Set to true for RTL layout in vertical orientation
     */
    private boolean mReverseFlowSecondary = false;

    /**
     * Temporaries used for measuring.
     */
    private int[] mMeasuredDimension = new int[2];

    final android.support.v17.leanback.widget.ViewsStateBundle mChildrenStates = new android.support.v17.leanback.widget.ViewsStateBundle();

    /**
     * Optional interface implemented by Adapter.
     */
    private android.support.v17.leanback.widget.FacetProviderAdapter mFacetProviderAdapter;

    public GridLayoutManager(android.support.v17.leanback.widget.BaseGridView baseGridView) {
        mBaseGridView = baseGridView;
    }

    public void setOrientation(int orientation) {
        if ((orientation != android.support.v7.widget.RecyclerView.HORIZONTAL) && (orientation != android.support.v7.widget.RecyclerView.VERTICAL)) {
            if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
                android.util.Log.v(getTag(), "invalid orientation: " + orientation);

            return;
        }
        mOrientation = orientation;
        mOrientationHelper = android.support.v7.widget.OrientationHelper.createOrientationHelper(this, mOrientation);
        mWindowAlignment.setOrientation(orientation);
        mItemAlignment.setOrientation(orientation);
        mForceFullLayout = true;
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
            mReverseFlowPrimary = layoutDirection == android.view.View.LAYOUT_DIRECTION_RTL;
            mReverseFlowSecondary = false;
        } else {
            mReverseFlowSecondary = layoutDirection == android.view.View.LAYOUT_DIRECTION_RTL;
            mReverseFlowPrimary = false;
        }
        mWindowAlignment.horizontal.setReversedFlow(layoutDirection == android.view.View.LAYOUT_DIRECTION_RTL);
    }

    public int getFocusScrollStrategy() {
        return mFocusScrollStrategy;
    }

    public void setFocusScrollStrategy(int focusScrollStrategy) {
        mFocusScrollStrategy = focusScrollStrategy;
    }

    public void setWindowAlignment(int windowAlignment) {
        mWindowAlignment.mainAxis().setWindowAlignment(windowAlignment);
    }

    public int getWindowAlignment() {
        return mWindowAlignment.mainAxis().getWindowAlignment();
    }

    public void setWindowAlignmentOffset(int alignmentOffset) {
        mWindowAlignment.mainAxis().setWindowAlignmentOffset(alignmentOffset);
    }

    public int getWindowAlignmentOffset() {
        return mWindowAlignment.mainAxis().getWindowAlignmentOffset();
    }

    public void setWindowAlignmentOffsetPercent(float offsetPercent) {
        mWindowAlignment.mainAxis().setWindowAlignmentOffsetPercent(offsetPercent);
    }

    public float getWindowAlignmentOffsetPercent() {
        return mWindowAlignment.mainAxis().getWindowAlignmentOffsetPercent();
    }

    public void setItemAlignmentOffset(int alignmentOffset) {
        mItemAlignment.mainAxis().setItemAlignmentOffset(alignmentOffset);
        updateChildAlignments();
    }

    public int getItemAlignmentOffset() {
        return mItemAlignment.mainAxis().getItemAlignmentOffset();
    }

    public void setItemAlignmentOffsetWithPadding(boolean withPadding) {
        mItemAlignment.mainAxis().setItemAlignmentOffsetWithPadding(withPadding);
        updateChildAlignments();
    }

    public boolean isItemAlignmentOffsetWithPadding() {
        return mItemAlignment.mainAxis().isItemAlignmentOffsetWithPadding();
    }

    public void setItemAlignmentOffsetPercent(float offsetPercent) {
        mItemAlignment.mainAxis().setItemAlignmentOffsetPercent(offsetPercent);
        updateChildAlignments();
    }

    public float getItemAlignmentOffsetPercent() {
        return mItemAlignment.mainAxis().getItemAlignmentOffsetPercent();
    }

    public void setItemAlignmentViewId(int viewId) {
        mItemAlignment.mainAxis().setItemAlignmentViewId(viewId);
        updateChildAlignments();
    }

    public int getItemAlignmentViewId() {
        return mItemAlignment.mainAxis().getItemAlignmentViewId();
    }

    public void setFocusOutAllowed(boolean throughFront, boolean throughEnd) {
        mFocusOutFront = throughFront;
        mFocusOutEnd = throughEnd;
    }

    public void setFocusOutSideAllowed(boolean throughStart, boolean throughEnd) {
        mFocusOutSideStart = throughStart;
        mFocusOutSideEnd = throughEnd;
    }

    public void setNumRows(int numRows) {
        if (numRows < 0)
            throw new java.lang.IllegalArgumentException();

        mNumRowsRequested = numRows;
    }

    /**
     * Set the row height. May be WRAP_CONTENT, or a size in pixels.
     */
    public void setRowHeight(int height) {
        if ((height >= 0) || (height == android.view.ViewGroup.LayoutParams.WRAP_CONTENT)) {
            mRowSizeSecondaryRequested = height;
        } else {
            throw new java.lang.IllegalArgumentException("Invalid row height: " + height);
        }
    }

    public void setItemMargin(int margin) {
        mVerticalMargin = mHorizontalMargin = margin;
        mMarginPrimary = mMarginSecondary = margin;
    }

    public void setVerticalMargin(int margin) {
        if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
            mMarginSecondary = mVerticalMargin = margin;
        } else {
            mMarginPrimary = mVerticalMargin = margin;
        }
    }

    public void setHorizontalMargin(int margin) {
        if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
            mMarginPrimary = mHorizontalMargin = margin;
        } else {
            mMarginSecondary = mHorizontalMargin = margin;
        }
    }

    public int getVerticalMargin() {
        return mVerticalMargin;
    }

    public int getHorizontalMargin() {
        return mHorizontalMargin;
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    protected boolean hasDoneFirstLayout() {
        return mGrid != null;
    }

    public void setOnChildSelectedListener(android.support.v17.leanback.widget.OnChildSelectedListener listener) {
        mChildSelectedListener = listener;
    }

    public void setOnChildViewHolderSelectedListener(android.support.v17.leanback.widget.OnChildViewHolderSelectedListener listener) {
        if (listener == null) {
            mChildViewHolderSelectedListeners = null;
            return;
        }
        if (mChildViewHolderSelectedListeners == null) {
            mChildViewHolderSelectedListeners = new java.util.ArrayList<android.support.v17.leanback.widget.OnChildViewHolderSelectedListener>();
        } else {
            mChildViewHolderSelectedListeners.clear();
        }
        mChildViewHolderSelectedListeners.add(listener);
    }

    public void addOnChildViewHolderSelectedListener(android.support.v17.leanback.widget.OnChildViewHolderSelectedListener listener) {
        if (mChildViewHolderSelectedListeners == null) {
            mChildViewHolderSelectedListeners = new java.util.ArrayList<android.support.v17.leanback.widget.OnChildViewHolderSelectedListener>();
        }
        mChildViewHolderSelectedListeners.add(listener);
    }

    public void removeOnChildViewHolderSelectedListener(android.support.v17.leanback.widget.OnChildViewHolderSelectedListener listener) {
        if (mChildViewHolderSelectedListeners != null) {
            mChildViewHolderSelectedListeners.remove(listener);
        }
    }

    boolean hasOnChildViewHolderSelectedListener() {
        return (mChildViewHolderSelectedListeners != null) && (mChildViewHolderSelectedListeners.size() > 0);
    }

    void fireOnChildViewHolderSelected(android.support.v7.widget.RecyclerView parent, android.support.v7.widget.RecyclerView.ViewHolder child, int position, int subposition) {
        if (mChildViewHolderSelectedListeners == null) {
            return;
        }
        for (int i = mChildViewHolderSelectedListeners.size() - 1; i >= 0; i--) {
            mChildViewHolderSelectedListeners.get(i).onChildViewHolderSelected(parent, child, position, subposition);
        }
    }

    void setOnChildLaidOutListener(android.support.v17.leanback.widget.OnChildLaidOutListener listener) {
        mChildLaidOutListener = listener;
    }

    private int getPositionByView(android.view.View view) {
        if (view == null) {
            return android.support.v7.widget.RecyclerView.NO_POSITION;
        }
        android.support.v17.leanback.widget.GridLayoutManager.LayoutParams params = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (view.getLayoutParams()));
        if ((params == null) || params.isItemRemoved()) {
            // when item is removed, the position value can be any value.
            return android.support.v7.widget.RecyclerView.NO_POSITION;
        }
        return params.getViewPosition();
    }

    int getSubPositionByView(android.view.View view, android.view.View childView) {
        if ((view == null) || (childView == null)) {
            return 0;
        }
        final android.support.v17.leanback.widget.GridLayoutManager.LayoutParams lp = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (view.getLayoutParams()));
        final android.support.v17.leanback.widget.ItemAlignmentFacet facet = lp.getItemAlignmentFacet();
        if (facet != null) {
            final android.support.v17.leanback.widget.ItemAlignmentFacet.ItemAlignmentDef[] defs = facet.getAlignmentDefs();
            if (defs.length > 1) {
                while (childView != view) {
                    int id = childView.getId();
                    if (id != android.view.View.NO_ID) {
                        for (int i = 1; i < defs.length; i++) {
                            if (defs[i].getItemAlignmentFocusViewId() == id) {
                                return i;
                            }
                        }
                    }
                    childView = ((android.view.View) (childView.getParent()));
                } 
            }
        }
        return 0;
    }

    private int getPositionByIndex(int index) {
        return getPositionByView(getChildAt(index));
    }

    void dispatchChildSelected() {
        if ((mChildSelectedListener == null) && (!hasOnChildViewHolderSelectedListener())) {
            return;
        }
        if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
            android.support.v17.leanback.os.TraceHelper.beginSection("onChildSelected");

        android.view.View view = (mFocusPosition == android.support.v7.widget.RecyclerView.NO_POSITION) ? null : findViewByPosition(mFocusPosition);
        if (view != null) {
            android.support.v7.widget.RecyclerView.ViewHolder vh = mBaseGridView.getChildViewHolder(view);
            if (mChildSelectedListener != null) {
                mChildSelectedListener.onChildSelected(mBaseGridView, view, mFocusPosition, vh == null ? android.support.v7.widget.RecyclerView.NO_ID : vh.getItemId());
            }
            fireOnChildViewHolderSelected(mBaseGridView, vh, mFocusPosition, mSubFocusPosition);
        } else {
            if (mChildSelectedListener != null) {
                mChildSelectedListener.onChildSelected(mBaseGridView, null, android.support.v7.widget.RecyclerView.NO_POSITION, android.support.v7.widget.RecyclerView.NO_ID);
            }
            fireOnChildViewHolderSelected(mBaseGridView, null, android.support.v7.widget.RecyclerView.NO_POSITION, 0);
        }
        if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
            android.support.v17.leanback.os.TraceHelper.endSection();

        // Children may request layout when a child selection event occurs (such as a change of
        // padding on the current and previously selected rows).
        // If in layout, a child requesting layout may have been laid out before the selection
        // callback.
        // If it was not, the child will be laid out after the selection callback.
        // If so, the layout request will be honoured though the view system will emit a double-
        // layout warning.
        // If not in layout, we may be scrolling in which case the child layout request will be
        // eaten by recyclerview.  Post a requestLayout.
        if ((!mInLayout) && (!mBaseGridView.isLayoutRequested())) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (getChildAt(i).isLayoutRequested()) {
                    forceRequestLayout();
                    break;
                }
            }
        }
    }

    @java.lang.Override
    public boolean canScrollHorizontally() {
        // We can scroll horizontally if we have horizontal orientation, or if
        // we are vertical and have more than one column.
        return (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) || (mNumRows > 1);
    }

    @java.lang.Override
    public boolean canScrollVertically() {
        // We can scroll vertically if we have vertical orientation, or if we
        // are horizontal and have more than one row.
        return (mOrientation == android.support.v7.widget.RecyclerView.VERTICAL) || (mNumRows > 1);
    }

    @java.lang.Override
    public android.support.v7.widget.RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new android.support.v17.leanback.widget.GridLayoutManager.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @java.lang.Override
    public android.support.v7.widget.RecyclerView.LayoutParams generateLayoutParams(android.content.Context context, android.util.AttributeSet attrs) {
        return new android.support.v17.leanback.widget.GridLayoutManager.LayoutParams(context, attrs);
    }

    @java.lang.Override
    public android.support.v7.widget.RecyclerView.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams lp) {
        if (lp instanceof android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) {
            return new android.support.v17.leanback.widget.GridLayoutManager.LayoutParams(((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (lp)));
        } else
            if (lp instanceof android.support.v7.widget.RecyclerView.LayoutParams) {
                return new android.support.v17.leanback.widget.GridLayoutManager.LayoutParams(((android.support.v7.widget.RecyclerView.LayoutParams) (lp)));
            } else
                if (lp instanceof android.view.ViewGroup.MarginLayoutParams) {
                    return new android.support.v17.leanback.widget.GridLayoutManager.LayoutParams(((android.view.ViewGroup.MarginLayoutParams) (lp)));
                } else {
                    return new android.support.v17.leanback.widget.GridLayoutManager.LayoutParams(lp);
                }


    }

    protected android.view.View getViewForPosition(int position) {
        return mRecycler.getViewForPosition(position);
    }

    final int getOpticalLeft(android.view.View v) {
        return ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (v.getLayoutParams())).getOpticalLeft(v);
    }

    final int getOpticalRight(android.view.View v) {
        return ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (v.getLayoutParams())).getOpticalRight(v);
    }

    final int getOpticalTop(android.view.View v) {
        return ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (v.getLayoutParams())).getOpticalTop(v);
    }

    final int getOpticalBottom(android.view.View v) {
        return ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (v.getLayoutParams())).getOpticalBottom(v);
    }

    @java.lang.Override
    public int getDecoratedLeft(android.view.View child) {
        return super.getDecoratedLeft(child) + ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (child.getLayoutParams())).mLeftInset;
    }

    @java.lang.Override
    public int getDecoratedTop(android.view.View child) {
        return super.getDecoratedTop(child) + ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (child.getLayoutParams())).mTopInset;
    }

    @java.lang.Override
    public int getDecoratedRight(android.view.View child) {
        return super.getDecoratedRight(child) - ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (child.getLayoutParams())).mRightInset;
    }

    @java.lang.Override
    public int getDecoratedBottom(android.view.View child) {
        return super.getDecoratedBottom(child) - ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (child.getLayoutParams())).mBottomInset;
    }

    @java.lang.Override
    public void getDecoratedBoundsWithMargins(android.view.View view, android.graphics.Rect outBounds) {
        super.getDecoratedBoundsWithMargins(view, outBounds);
        android.support.v17.leanback.widget.GridLayoutManager.LayoutParams params = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (view.getLayoutParams()));
        outBounds.left += params.mLeftInset;
        outBounds.top += params.mTopInset;
        outBounds.right -= params.mRightInset;
        outBounds.bottom -= params.mBottomInset;
    }

    int getViewMin(android.view.View v) {
        return mOrientationHelper.getDecoratedStart(v);
    }

    int getViewMax(android.view.View v) {
        return mOrientationHelper.getDecoratedEnd(v);
    }

    int getViewPrimarySize(android.view.View view) {
        getDecoratedBoundsWithMargins(view, android.support.v17.leanback.widget.GridLayoutManager.sTempRect);
        return mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL ? android.support.v17.leanback.widget.GridLayoutManager.sTempRect.width() : android.support.v17.leanback.widget.GridLayoutManager.sTempRect.height();
    }

    private int getViewCenter(android.view.View view) {
        return mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL ? getViewCenterX(view) : getViewCenterY(view);
    }

    private int getViewCenterSecondary(android.view.View view) {
        return mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL ? getViewCenterY(view) : getViewCenterX(view);
    }

    private int getViewCenterX(android.view.View v) {
        android.support.v17.leanback.widget.GridLayoutManager.LayoutParams p = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (v.getLayoutParams()));
        return p.getOpticalLeft(v) + p.getAlignX();
    }

    private int getViewCenterY(android.view.View v) {
        android.support.v17.leanback.widget.GridLayoutManager.LayoutParams p = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (v.getLayoutParams()));
        return p.getOpticalTop(v) + p.getAlignY();
    }

    /**
     * Save Recycler and State for convenience.  Must be paired with leaveContext().
     */
    private void saveContext(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        if ((mRecycler != null) || (mState != null)) {
            android.util.Log.e(android.support.v17.leanback.widget.GridLayoutManager.TAG, "Recycler information was not released, bug!");
        }
        mRecycler = recycler;
        mState = state;
    }

    /**
     * Discard saved Recycler and State.
     */
    private void leaveContext() {
        mRecycler = null;
        mState = null;
    }

    /**
     * Re-initialize data structures for a data change or handling invisible
     * selection. The method tries its best to preserve position information so
     * that staggered grid looks same before and after re-initialize.
     *
     * @return true if can fastRelayout()
     */
    private boolean layoutInit() {
        boolean focusViewWasInTree = (((mGrid != null) && (mFocusPosition >= 0)) && (mFocusPosition >= mGrid.getFirstVisibleIndex())) && (mFocusPosition <= mGrid.getLastVisibleIndex());
        final int newItemCount = mState.getItemCount();
        if (newItemCount == 0) {
            mFocusPosition = android.support.v7.widget.RecyclerView.NO_POSITION;
            mSubFocusPosition = 0;
        } else
            if (mFocusPosition >= newItemCount) {
                mFocusPosition = newItemCount - 1;
                mSubFocusPosition = 0;
            } else
                if ((mFocusPosition == android.support.v7.widget.RecyclerView.NO_POSITION) && (newItemCount > 0)) {
                    // if focus position is never set before,  initialize it to 0
                    mFocusPosition = 0;
                    mSubFocusPosition = 0;
                }


        if (((((!mState.didStructureChange()) && (mGrid.getFirstVisibleIndex() >= 0)) && (!mForceFullLayout)) && (mGrid != null)) && (mGrid.getNumRows() == mNumRows)) {
            updateScrollController();
            updateScrollSecondAxis();
            mGrid.setMargin(mMarginPrimary);
            if ((!focusViewWasInTree) && (mFocusPosition != android.support.v7.widget.RecyclerView.NO_POSITION)) {
                mGrid.setStart(mFocusPosition);
            }
            return true;
        } else {
            mForceFullLayout = false;
            int firstVisibleIndex = (focusViewWasInTree) ? mGrid.getFirstVisibleIndex() : 0;
            if (((mGrid == null) || (mNumRows != mGrid.getNumRows())) || (mReverseFlowPrimary != mGrid.isReversedFlow())) {
                mGrid = android.support.v17.leanback.widget.Grid.createGrid(mNumRows);
                mGrid.setProvider(mGridProvider);
                mGrid.setReversedFlow(mReverseFlowPrimary);
            }
            initScrollController();
            updateScrollSecondAxis();
            mGrid.setMargin(mMarginPrimary);
            detachAndScrapAttachedViews(mRecycler);
            mGrid.resetVisibleIndex();
            if (mFocusPosition == android.support.v7.widget.RecyclerView.NO_POSITION) {
                mBaseGridView.clearFocus();
            }
            mWindowAlignment.mainAxis().invalidateScrollMin();
            mWindowAlignment.mainAxis().invalidateScrollMax();
            if (focusViewWasInTree && (firstVisibleIndex <= mFocusPosition)) {
                // if focusView was in tree, we will add item from first visible item
                mGrid.setStart(firstVisibleIndex);
            } else {
                // if focusView was not in tree, it's probably because focus position jumped
                // far away from visible range,  so use mFocusPosition as start
                mGrid.setStart(mFocusPosition);
            }
            return false;
        }
    }

    private int getRowSizeSecondary(int rowIndex) {
        if (mFixedRowSizeSecondary != 0) {
            return mFixedRowSizeSecondary;
        }
        if (mRowSizeSecondary == null) {
            return 0;
        }
        return mRowSizeSecondary[rowIndex];
    }

    int getRowStartSecondary(int rowIndex) {
        int start = 0;
        // Iterate from left to right, which is a different index traversal
        // in RTL flow
        if (mReverseFlowSecondary) {
            for (int i = mNumRows - 1; i > rowIndex; i--) {
                start += getRowSizeSecondary(i) + mMarginSecondary;
            }
        } else {
            for (int i = 0; i < rowIndex; i++) {
                start += getRowSizeSecondary(i) + mMarginSecondary;
            }
        }
        return start;
    }

    private int getSizeSecondary() {
        int rightmostIndex = (mReverseFlowSecondary) ? 0 : mNumRows - 1;
        return getRowStartSecondary(rightmostIndex) + getRowSizeSecondary(rightmostIndex);
    }

    int getDecoratedMeasuredWidthWithMargin(android.view.View v) {
        final android.support.v17.leanback.widget.GridLayoutManager.LayoutParams lp = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (v.getLayoutParams()));
        return (getDecoratedMeasuredWidth(v) + lp.leftMargin) + lp.rightMargin;
    }

    int getDecoratedMeasuredHeightWithMargin(android.view.View v) {
        final android.support.v17.leanback.widget.GridLayoutManager.LayoutParams lp = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (v.getLayoutParams()));
        return (getDecoratedMeasuredHeight(v) + lp.topMargin) + lp.bottomMargin;
    }

    private void measureScrapChild(int position, int widthSpec, int heightSpec, int[] measuredDimension) {
        android.view.View view = mRecycler.getViewForPosition(position);
        if (view != null) {
            final android.support.v17.leanback.widget.GridLayoutManager.LayoutParams p = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (view.getLayoutParams()));
            calculateItemDecorationsForChild(view, android.support.v17.leanback.widget.GridLayoutManager.sTempRect);
            int widthUsed = ((p.leftMargin + p.rightMargin) + android.support.v17.leanback.widget.GridLayoutManager.sTempRect.left) + android.support.v17.leanback.widget.GridLayoutManager.sTempRect.right;
            int heightUsed = ((p.topMargin + p.bottomMargin) + android.support.v17.leanback.widget.GridLayoutManager.sTempRect.top) + android.support.v17.leanback.widget.GridLayoutManager.sTempRect.bottom;
            int childWidthSpec = android.view.ViewGroup.getChildMeasureSpec(widthSpec, (getPaddingLeft() + getPaddingRight()) + widthUsed, p.width);
            int childHeightSpec = android.view.ViewGroup.getChildMeasureSpec(heightSpec, (getPaddingTop() + getPaddingBottom()) + heightUsed, p.height);
            view.measure(childWidthSpec, childHeightSpec);
            measuredDimension[0] = getDecoratedMeasuredWidthWithMargin(view);
            measuredDimension[1] = getDecoratedMeasuredHeightWithMargin(view);
            mRecycler.recycleView(view);
        }
    }

    private boolean processRowSizeSecondary(boolean measure) {
        if ((mFixedRowSizeSecondary != 0) || (mRowSizeSecondary == null)) {
            return false;
        }
        if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
            android.support.v17.leanback.os.TraceHelper.beginSection("processRowSizeSecondary");

        android.support.v4.util.CircularIntArray[] rows = (mGrid == null) ? null : mGrid.getItemPositionsInRows();
        boolean changed = false;
        int scrapChildWidth = -1;
        int scrapChildHeight = -1;
        for (int rowIndex = 0; rowIndex < mNumRows; rowIndex++) {
            android.support.v4.util.CircularIntArray row = (rows == null) ? null : rows[rowIndex];
            final int rowItemsPairCount = (row == null) ? 0 : row.size();
            int rowSize = -1;
            for (int rowItemPairIndex = 0; rowItemPairIndex < rowItemsPairCount; rowItemPairIndex += 2) {
                final int rowIndexStart = row.get(rowItemPairIndex);
                final int rowIndexEnd = row.get(rowItemPairIndex + 1);
                for (int i = rowIndexStart; i <= rowIndexEnd; i++) {
                    final android.view.View view = findViewByPosition(i);
                    if (view == null) {
                        continue;
                    }
                    if (measure) {
                        measureChild(view);
                    }
                    final int secondarySize = (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) ? getDecoratedMeasuredHeightWithMargin(view) : getDecoratedMeasuredWidthWithMargin(view);
                    if (secondarySize > rowSize) {
                        rowSize = secondarySize;
                    }
                }
            }
            final int itemCount = mState.getItemCount();
            if ((((!mBaseGridView.hasFixedSize()) && measure) && (rowSize < 0)) && (itemCount > 0)) {
                if ((scrapChildWidth < 0) && (scrapChildHeight < 0)) {
                    int position;
                    if (mFocusPosition == android.support.v7.widget.RecyclerView.NO_POSITION) {
                        position = 0;
                    } else
                        if (mFocusPosition >= itemCount) {
                            position = itemCount - 1;
                        } else {
                            position = mFocusPosition;
                        }

                    measureScrapChild(position, android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED), android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED), mMeasuredDimension);
                    scrapChildWidth = mMeasuredDimension[0];
                    scrapChildHeight = mMeasuredDimension[1];
                    if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
                        android.util.Log.v(android.support.v17.leanback.widget.GridLayoutManager.TAG, (("measured scrap child: " + scrapChildWidth) + " ") + scrapChildHeight);

                }
                rowSize = (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) ? scrapChildHeight : scrapChildWidth;
            }
            if (rowSize < 0) {
                rowSize = 0;
            }
            if (mRowSizeSecondary[rowIndex] != rowSize) {
                if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
                    android.util.Log.v(getTag(), (("row size secondary changed: " + mRowSizeSecondary[rowIndex]) + ", ") + rowSize);

                mRowSizeSecondary[rowIndex] = rowSize;
                changed = true;
            }
        }
        if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
            android.support.v17.leanback.os.TraceHelper.endSection();

        return changed;
    }

    /**
     * Checks if we need to update row secondary sizes.
     */
    private void updateRowSecondarySizeRefresh() {
        mRowSecondarySizeRefresh = processRowSizeSecondary(false);
        if (mRowSecondarySizeRefresh) {
            if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
                android.util.Log.v(getTag(), "mRowSecondarySizeRefresh now set");

            forceRequestLayout();
        }
    }

    private void forceRequestLayout() {
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), "forceRequestLayout");

        // RecyclerView prevents us from requesting layout in many cases
        // (during layout, during scroll, etc.)
        // For secondary row size wrap_content support we currently need a
        // second layout pass to update the measured size after having measured
        // and added child views in layoutChildren.
        // Force the second layout by posting a delayed runnable.
        // TODO: investigate allowing a second layout pass,
        // or move child add/measure logic to the measure phase.
        android.support.v4.view.ViewCompat.postOnAnimation(mBaseGridView, mRequestLayoutRunnable);
    }

    private final java.lang.Runnable mRequestLayoutRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
                android.util.Log.v(getTag(), "request Layout from runnable");

            requestLayout();
        }
    };

    @java.lang.Override
    public void onMeasure(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, int widthSpec, int heightSpec) {
        saveContext(recycler, state);
        int sizePrimary;
        int sizeSecondary;
        int modeSecondary;
        int paddingSecondary;
        int measuredSizeSecondary;
        if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
            sizePrimary = android.view.View.MeasureSpec.getSize(widthSpec);
            sizeSecondary = android.view.View.MeasureSpec.getSize(heightSpec);
            modeSecondary = android.view.View.MeasureSpec.getMode(heightSpec);
            paddingSecondary = getPaddingTop() + getPaddingBottom();
        } else {
            sizeSecondary = android.view.View.MeasureSpec.getSize(widthSpec);
            sizePrimary = android.view.View.MeasureSpec.getSize(heightSpec);
            modeSecondary = android.view.View.MeasureSpec.getMode(widthSpec);
            paddingSecondary = getPaddingLeft() + getPaddingRight();
        }
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), (((((((("onMeasure widthSpec " + java.lang.Integer.toHexString(widthSpec)) + " heightSpec ") + java.lang.Integer.toHexString(heightSpec)) + " modeSecondary ") + java.lang.Integer.toHexString(modeSecondary)) + " sizeSecondary ") + sizeSecondary) + " ") + this);

        mMaxSizeSecondary = sizeSecondary;
        if (mRowSizeSecondaryRequested == android.view.ViewGroup.LayoutParams.WRAP_CONTENT) {
            mNumRows = (mNumRowsRequested == 0) ? 1 : mNumRowsRequested;
            mFixedRowSizeSecondary = 0;
            if ((mRowSizeSecondary == null) || (mRowSizeSecondary.length != mNumRows)) {
                mRowSizeSecondary = new int[mNumRows];
            }
            // Measure all current children and update cached row heights
            processRowSizeSecondary(true);
            switch (modeSecondary) {
                case android.view.View.MeasureSpec.UNSPECIFIED :
                    measuredSizeSecondary = getSizeSecondary() + paddingSecondary;
                    break;
                case android.view.View.MeasureSpec.AT_MOST :
                    measuredSizeSecondary = java.lang.Math.min(getSizeSecondary() + paddingSecondary, mMaxSizeSecondary);
                    break;
                case android.view.View.MeasureSpec.EXACTLY :
                    measuredSizeSecondary = mMaxSizeSecondary;
                    break;
                default :
                    throw new java.lang.IllegalStateException("wrong spec");
            }
        } else {
            switch (modeSecondary) {
                case android.view.View.MeasureSpec.UNSPECIFIED :
                    mFixedRowSizeSecondary = (mRowSizeSecondaryRequested == 0) ? sizeSecondary - paddingSecondary : mRowSizeSecondaryRequested;
                    mNumRows = (mNumRowsRequested == 0) ? 1 : mNumRowsRequested;
                    measuredSizeSecondary = ((mFixedRowSizeSecondary * mNumRows) + (mMarginSecondary * (mNumRows - 1))) + paddingSecondary;
                    break;
                case android.view.View.MeasureSpec.AT_MOST :
                case android.view.View.MeasureSpec.EXACTLY :
                    if ((mNumRowsRequested == 0) && (mRowSizeSecondaryRequested == 0)) {
                        mNumRows = 1;
                        mFixedRowSizeSecondary = sizeSecondary - paddingSecondary;
                    } else
                        if (mNumRowsRequested == 0) {
                            mFixedRowSizeSecondary = mRowSizeSecondaryRequested;
                            mNumRows = (sizeSecondary + mMarginSecondary) / (mRowSizeSecondaryRequested + mMarginSecondary);
                        } else
                            if (mRowSizeSecondaryRequested == 0) {
                                mNumRows = mNumRowsRequested;
                                mFixedRowSizeSecondary = ((sizeSecondary - paddingSecondary) - (mMarginSecondary * (mNumRows - 1))) / mNumRows;
                            } else {
                                mNumRows = mNumRowsRequested;
                                mFixedRowSizeSecondary = mRowSizeSecondaryRequested;
                            }


                    measuredSizeSecondary = sizeSecondary;
                    if (modeSecondary == android.view.View.MeasureSpec.AT_MOST) {
                        int childrenSize = ((mFixedRowSizeSecondary * mNumRows) + (mMarginSecondary * (mNumRows - 1))) + paddingSecondary;
                        if (childrenSize < measuredSizeSecondary) {
                            measuredSizeSecondary = childrenSize;
                        }
                    }
                    break;
                default :
                    throw new java.lang.IllegalStateException("wrong spec");
            }
        }
        if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
            setMeasuredDimension(sizePrimary, measuredSizeSecondary);
        } else {
            setMeasuredDimension(measuredSizeSecondary, sizePrimary);
        }
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG) {
            android.util.Log.v(getTag(), (((((("onMeasure sizePrimary " + sizePrimary) + " measuredSizeSecondary ") + measuredSizeSecondary) + " mFixedRowSizeSecondary ") + mFixedRowSizeSecondary) + " mNumRows ") + mNumRows);
        }
        leaveContext();
    }

    void measureChild(android.view.View child) {
        if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
            android.support.v17.leanback.os.TraceHelper.beginSection("measureChild");

        final android.support.v17.leanback.widget.GridLayoutManager.LayoutParams lp = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (child.getLayoutParams()));
        calculateItemDecorationsForChild(child, android.support.v17.leanback.widget.GridLayoutManager.sTempRect);
        int widthUsed = ((lp.leftMargin + lp.rightMargin) + android.support.v17.leanback.widget.GridLayoutManager.sTempRect.left) + android.support.v17.leanback.widget.GridLayoutManager.sTempRect.right;
        int heightUsed = ((lp.topMargin + lp.bottomMargin) + android.support.v17.leanback.widget.GridLayoutManager.sTempRect.top) + android.support.v17.leanback.widget.GridLayoutManager.sTempRect.bottom;
        final int secondarySpec = (mRowSizeSecondaryRequested == android.view.ViewGroup.LayoutParams.WRAP_CONTENT) ? android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED) : android.view.View.MeasureSpec.makeMeasureSpec(mFixedRowSizeSecondary, android.view.View.MeasureSpec.EXACTLY);
        int widthSpec;
        int heightSpec;
        if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
            widthSpec = android.view.ViewGroup.getChildMeasureSpec(android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED), widthUsed, lp.width);
            heightSpec = android.view.ViewGroup.getChildMeasureSpec(secondarySpec, heightUsed, lp.height);
        } else {
            heightSpec = android.view.ViewGroup.getChildMeasureSpec(android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED), heightUsed, lp.height);
            widthSpec = android.view.ViewGroup.getChildMeasureSpec(secondarySpec, widthUsed, lp.width);
        }
        child.measure(widthSpec, heightSpec);
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), (((((((("measureChild secondarySpec " + java.lang.Integer.toHexString(secondarySpec)) + " widthSpec ") + java.lang.Integer.toHexString(widthSpec)) + " heightSpec ") + java.lang.Integer.toHexString(heightSpec)) + " measuredWidth ") + child.getMeasuredWidth()) + " measuredHeight ") + child.getMeasuredHeight());

        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), (("child lp width " + lp.width) + " height ") + lp.height);

        if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
            android.support.v17.leanback.os.TraceHelper.endSection();

    }

    /**
     * Get facet from the ViewHolder or the viewType.
     */
    <E> E getFacet(android.support.v7.widget.RecyclerView.ViewHolder vh, java.lang.Class<? extends E> facetClass) {
        E facet = null;
        if (vh instanceof android.support.v17.leanback.widget.FacetProvider) {
            facet = ((E) (((android.support.v17.leanback.widget.FacetProvider) (vh)).getFacet(facetClass)));
        }
        if ((facet == null) && (mFacetProviderAdapter != null)) {
            android.support.v17.leanback.widget.FacetProvider p = mFacetProviderAdapter.getFacetProvider(vh.getItemViewType());
            if (p != null) {
                facet = ((E) (p.getFacet(facetClass)));
            }
        }
        return facet;
    }

    private android.support.v17.leanback.widget.Grid.Provider mGridProvider = new android.support.v17.leanback.widget.Grid.Provider() {
        @java.lang.Override
        public int getCount() {
            return mState.getItemCount();
        }

        @java.lang.Override
        public int createItem(int index, boolean append, java.lang.Object[] item) {
            if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
                android.support.v17.leanback.os.TraceHelper.beginSection("createItem");

            if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
                android.support.v17.leanback.os.TraceHelper.beginSection("getview");

            android.view.View v = getViewForPosition(index);
            if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
                android.support.v17.leanback.os.TraceHelper.endSection();

            android.support.v17.leanback.widget.GridLayoutManager.LayoutParams lp = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (v.getLayoutParams()));
            android.support.v7.widget.RecyclerView.ViewHolder vh = mBaseGridView.getChildViewHolder(v);
            lp.setItemAlignmentFacet(((android.support.v17.leanback.widget.ItemAlignmentFacet) (getFacet(vh, android.support.v17.leanback.widget.ItemAlignmentFacet.class))));
            // See recyclerView docs:  we don't need re-add scraped view if it was removed.
            if (!lp.isItemRemoved()) {
                if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
                    android.support.v17.leanback.os.TraceHelper.beginSection("addView");

                if (append) {
                    addView(v);
                } else {
                    addView(v, 0);
                }
                if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
                    android.support.v17.leanback.os.TraceHelper.endSection();

                if (mChildVisibility != (-1)) {
                    v.setVisibility(mChildVisibility);
                }
                if (mPendingMoveSmoothScroller != null) {
                    mPendingMoveSmoothScroller.consumePendingMovesBeforeLayout();
                }
                int subindex = getSubPositionByView(v, v.findFocus());
                if (!mInLayout) {
                    // when we are appending item during scroll pass and the item's position
                    // matches the mFocusPosition,  we should signal a childSelected event.
                    // However if we are still running PendingMoveSmoothScroller,  we defer and
                    // signal the event in PendingMoveSmoothScroller.onStop().  This can
                    // avoid lots of childSelected events during a long smooth scrolling and
                    // increase performance.
                    if (((index == mFocusPosition) && (subindex == mSubFocusPosition)) && (mPendingMoveSmoothScroller == null)) {
                        dispatchChildSelected();
                    }
                } else
                    if (!mInFastRelayout) {
                        // fastRelayout will dispatch event at end of onLayoutChildren().
                        // For full layout, two situations here:
                        // 1. mInLayoutSearchFocus is false, dispatchChildSelected() at mFocusPosition.
                        // 2. mInLayoutSearchFocus is true:  dispatchChildSelected() on first child
                        // equal to or after mFocusPosition that can take focus.
                        if (((!mInLayoutSearchFocus) && (index == mFocusPosition)) && (subindex == mSubFocusPosition)) {
                            dispatchChildSelected();
                        } else
                            if ((mInLayoutSearchFocus && (index >= mFocusPosition)) && v.hasFocusable()) {
                                mFocusPosition = index;
                                mSubFocusPosition = subindex;
                                mInLayoutSearchFocus = false;
                                dispatchChildSelected();
                            }

                    }

                measureChild(v);
            }
            item[0] = v;
            return mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL ? getDecoratedMeasuredWidthWithMargin(v) : getDecoratedMeasuredHeightWithMargin(v);
        }

        @java.lang.Override
        public void addItem(java.lang.Object item, int index, int length, int rowIndex, int edge) {
            android.view.View v = ((android.view.View) (item));
            int start;
            int end;
            if ((edge == java.lang.Integer.MIN_VALUE) || (edge == java.lang.Integer.MAX_VALUE)) {
                edge = (!mGrid.isReversedFlow()) ? mWindowAlignment.mainAxis().getPaddingLow() : mWindowAlignment.mainAxis().getSize() - mWindowAlignment.mainAxis().getPaddingHigh();
            }
            boolean edgeIsMin = !mGrid.isReversedFlow();
            if (edgeIsMin) {
                start = edge;
                end = edge + length;
            } else {
                start = edge - length;
                end = edge;
            }
            int startSecondary = getRowStartSecondary(rowIndex) - mScrollOffsetSecondary;
            mChildrenStates.loadView(v, index);
            layoutChild(rowIndex, v, start, end, startSecondary);
            if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG) {
                android.util.Log.d(getTag(), (("addView " + index) + " ") + v);
            }
            if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
                android.support.v17.leanback.os.TraceHelper.endSection();

            if (index == mGrid.getFirstVisibleIndex()) {
                if (!mGrid.isReversedFlow()) {
                    updateScrollMin();
                } else {
                    updateScrollMax();
                }
            }
            if (index == mGrid.getLastVisibleIndex()) {
                if (!mGrid.isReversedFlow()) {
                    updateScrollMax();
                } else {
                    updateScrollMin();
                }
            }
            if ((!mInLayout) && (mPendingMoveSmoothScroller != null)) {
                mPendingMoveSmoothScroller.consumePendingMovesAfterLayout();
            }
            if (mChildLaidOutListener != null) {
                android.support.v7.widget.RecyclerView.ViewHolder vh = mBaseGridView.getChildViewHolder(v);
                mChildLaidOutListener.onChildLaidOut(mBaseGridView, v, index, vh == null ? android.support.v7.widget.RecyclerView.NO_ID : vh.getItemId());
            }
        }

        @java.lang.Override
        public void removeItem(int index) {
            if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
                android.support.v17.leanback.os.TraceHelper.beginSection("removeItem");

            android.view.View v = findViewByPosition(index);
            if (mInLayout) {
                detachAndScrapView(v, mRecycler);
            } else {
                removeAndRecycleView(v, mRecycler);
            }
            if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
                android.support.v17.leanback.os.TraceHelper.endSection();

        }

        @java.lang.Override
        public int getEdge(int index) {
            if (mReverseFlowPrimary) {
                return getViewMax(findViewByPosition(index));
            } else {
                return getViewMin(findViewByPosition(index));
            }
        }

        @java.lang.Override
        public int getSize(int index) {
            return getViewPrimarySize(findViewByPosition(index));
        }
    };

    void layoutChild(int rowIndex, android.view.View v, int start, int end, int startSecondary) {
        if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
            android.support.v17.leanback.os.TraceHelper.beginSection("layoutChild");

        int sizeSecondary = (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) ? getDecoratedMeasuredHeightWithMargin(v) : getDecoratedMeasuredWidthWithMargin(v);
        if (mFixedRowSizeSecondary > 0) {
            sizeSecondary = java.lang.Math.min(sizeSecondary, mFixedRowSizeSecondary);
        }
        final int verticalGravity = mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
        final int horizontalGravity = (mReverseFlowPrimary || mReverseFlowSecondary) ? android.view.Gravity.getAbsoluteGravity(mGravity & android.view.Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK, android.view.View.LAYOUT_DIRECTION_RTL) : mGravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK;
        if (((mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) && (verticalGravity == android.view.Gravity.TOP)) || ((mOrientation == android.support.v7.widget.RecyclerView.VERTICAL) && (horizontalGravity == android.view.Gravity.LEFT))) {
            // do nothing
        } else
            if (((mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) && (verticalGravity == android.view.Gravity.BOTTOM)) || ((mOrientation == android.support.v7.widget.RecyclerView.VERTICAL) && (horizontalGravity == android.view.Gravity.RIGHT))) {
                startSecondary += getRowSizeSecondary(rowIndex) - sizeSecondary;
            } else
                if (((mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) && (verticalGravity == android.view.Gravity.CENTER_VERTICAL)) || ((mOrientation == android.support.v7.widget.RecyclerView.VERTICAL) && (horizontalGravity == android.view.Gravity.CENTER_HORIZONTAL))) {
                    startSecondary += (getRowSizeSecondary(rowIndex) - sizeSecondary) / 2;
                }


        int left;
        int top;
        int right;
        int bottom;
        if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
            left = start;
            top = startSecondary;
            right = end;
            bottom = startSecondary + sizeSecondary;
        } else {
            top = start;
            left = startSecondary;
            bottom = end;
            right = startSecondary + sizeSecondary;
        }
        android.support.v17.leanback.widget.GridLayoutManager.LayoutParams params = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (v.getLayoutParams()));
        layoutDecoratedWithMargins(v, left, top, right, bottom);
        // Now super.getDecoratedBoundsWithMargins() includes the extra space for optical bounds,
        // subtracting it from value passed in layoutDecoratedWithMargins(), we can get the optical
        // bounds insets.
        super.getDecoratedBoundsWithMargins(v, android.support.v17.leanback.widget.GridLayoutManager.sTempRect);
        params.setOpticalInsets(left - android.support.v17.leanback.widget.GridLayoutManager.sTempRect.left, top - android.support.v17.leanback.widget.GridLayoutManager.sTempRect.top, android.support.v17.leanback.widget.GridLayoutManager.sTempRect.right - right, android.support.v17.leanback.widget.GridLayoutManager.sTempRect.bottom - bottom);
        updateChildAlignments(v);
        if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
            android.support.v17.leanback.os.TraceHelper.endSection();

    }

    private void updateChildAlignments(android.view.View v) {
        final android.support.v17.leanback.widget.GridLayoutManager.LayoutParams p = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (v.getLayoutParams()));
        if (p.getItemAlignmentFacet() == null) {
            // Fallback to global settings on grid view
            p.setAlignX(mItemAlignment.horizontal.getAlignmentPosition(v));
            p.setAlignY(mItemAlignment.vertical.getAlignmentPosition(v));
        } else {
            // Use ItemAlignmentFacet defined on specific ViewHolder
            p.calculateItemAlignments(mOrientation, v);
            if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
                p.setAlignY(mItemAlignment.vertical.getAlignmentPosition(v));
            } else {
                p.setAlignX(mItemAlignment.horizontal.getAlignmentPosition(v));
            }
        }
    }

    private void updateChildAlignments() {
        for (int i = 0, c = getChildCount(); i < c; i++) {
            updateChildAlignments(getChildAt(i));
        }
    }

    void setExtraLayoutSpace(int extraLayoutSpace) {
        if (mExtraLayoutSpace == extraLayoutSpace) {
            return;
        } else
            if (mExtraLayoutSpace < 0) {
                throw new java.lang.IllegalArgumentException("ExtraLayoutSpace must >= 0");
            }

        mExtraLayoutSpace = extraLayoutSpace;
        requestLayout();
    }

    int getExtraLayoutSpace() {
        return mExtraLayoutSpace;
    }

    private void removeInvisibleViewsAtEnd() {
        if (mPruneChild) {
            mGrid.removeInvisibleItemsAtEnd(mFocusPosition, mReverseFlowPrimary ? -mExtraLayoutSpace : mSizePrimary + mExtraLayoutSpace);
        }
    }

    private void removeInvisibleViewsAtFront() {
        if (mPruneChild) {
            mGrid.removeInvisibleItemsAtFront(mFocusPosition, mReverseFlowPrimary ? mSizePrimary + mExtraLayoutSpace : -mExtraLayoutSpace);
        }
    }

    private boolean appendOneColumnVisibleItems() {
        return mGrid.appendOneColumnVisibleItems();
    }

    private boolean prependOneColumnVisibleItems() {
        return mGrid.prependOneColumnVisibleItems();
    }

    private void appendVisibleItems() {
        mGrid.appendVisibleItems(mReverseFlowPrimary ? -mExtraLayoutSpace : mSizePrimary + mExtraLayoutSpace);
    }

    private void prependVisibleItems() {
        mGrid.prependVisibleItems(mReverseFlowPrimary ? mSizePrimary + mExtraLayoutSpace : -mExtraLayoutSpace);
    }

    /**
     * Fast layout when there is no structure change, adapter change, etc.
     * It will layout all views was layout requested or updated, until hit a view
     * with different size,  then it break and detachAndScrap all views after that.
     */
    private void fastRelayout() {
        boolean invalidateAfter = false;
        final int childCount = getChildCount();
        int position = -1;
        for (int index = 0; index < childCount; index++) {
            android.view.View view = getChildAt(index);
            position = getPositionByIndex(index);
            android.support.v17.leanback.widget.Grid.Location location = mGrid.getLocation(position);
            if (location == null) {
                if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
                    android.util.Log.w(getTag(), "fastRelayout(): no Location at " + position);

                invalidateAfter = true;
                break;
            }
            int startSecondary = getRowStartSecondary(location.row) - mScrollOffsetSecondary;
            int primarySize;
            int end;
            int start = getViewMin(view);
            int oldPrimarySize = getViewPrimarySize(view);
            android.support.v17.leanback.widget.GridLayoutManager.LayoutParams lp = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (view.getLayoutParams()));
            if (lp.viewNeedsUpdate()) {
                int viewIndex = mBaseGridView.indexOfChild(view);
                detachAndScrapView(view, mRecycler);
                view = getViewForPosition(position);
                addView(view, viewIndex);
            }
            measureChild(view);
            if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
                primarySize = getDecoratedMeasuredWidthWithMargin(view);
                end = start + primarySize;
            } else {
                primarySize = getDecoratedMeasuredHeightWithMargin(view);
                end = start + primarySize;
            }
            layoutChild(location.row, view, start, end, startSecondary);
            if (oldPrimarySize != primarySize) {
                // size changed invalidate remaining Locations
                if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
                    android.util.Log.d(getTag(), "fastRelayout: view size changed at " + position);

                invalidateAfter = true;
                break;
            }
        }
        if (invalidateAfter) {
            final int savedLastPos = mGrid.getLastVisibleIndex();
            mGrid.invalidateItemsAfter(position);
            if (mPruneChild) {
                // in regular prune child mode, we just append items up to edge limit
                appendVisibleItems();
                if ((mFocusPosition >= 0) && (mFocusPosition <= savedLastPos)) {
                    // make sure add focus view back:  the view might be outside edge limit
                    // when there is delta in onLayoutChildren().
                    while (mGrid.getLastVisibleIndex() < mFocusPosition) {
                        mGrid.appendOneColumnVisibleItems();
                    } 
                }
            } else {
                // prune disabled(e.g. in RowsFragment transition): append all removed items
                while (mGrid.appendOneColumnVisibleItems() && (mGrid.getLastVisibleIndex() < savedLastPos));
            }
        }
        updateScrollMin();
        updateScrollMax();
        updateScrollSecondAxis();
    }

    @java.lang.Override
    public void removeAndRecycleAllViews(android.support.v7.widget.RecyclerView.Recycler recycler) {
        if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
            android.support.v17.leanback.os.TraceHelper.beginSection("removeAndRecycleAllViews");

        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(android.support.v17.leanback.widget.GridLayoutManager.TAG, "removeAndRecycleAllViews " + getChildCount());

        for (int i = getChildCount() - 1; i >= 0; i--) {
            removeAndRecycleViewAt(i, recycler);
        }
        if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
            android.support.v17.leanback.os.TraceHelper.endSection();

    }

    // called by onLayoutChildren, either focus to FocusPosition or declare focusViewAvailable
    // and scroll to the view if framework focus on it.
    private void scrollToFocusViewInLayout(boolean hadFocus, boolean alignToView) {
        android.view.View focusView = findViewByPosition(mFocusPosition);
        if ((focusView != null) && alignToView) {
            scrollToView(focusView, false);
        }
        if (((focusView != null) && hadFocus) && (!focusView.hasFocus())) {
            focusView.requestFocus();
        } else
            if ((!hadFocus) && (!mBaseGridView.hasFocus())) {
                if ((focusView != null) && focusView.hasFocusable()) {
                    mBaseGridView.focusableViewAvailable(focusView);
                } else {
                    for (int i = 0, count = getChildCount(); i < count; i++) {
                        focusView = getChildAt(i);
                        if ((focusView != null) && focusView.hasFocusable()) {
                            mBaseGridView.focusableViewAvailable(focusView);
                            break;
                        }
                    }
                    // focusViewAvailable() might focus to the view, scroll to it if that is the case.
                    if ((alignToView && (focusView != null)) && focusView.hasFocus()) {
                        scrollToView(focusView, false);
                    }
                }
            }

    }

    // Lays out items based on the current scroll position
    @java.lang.Override
    public void onLayoutChildren(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG) {
            android.util.Log.v(getTag(), (((((((((("layoutChildren start numRows " + mNumRows) + " mScrollOffsetSecondary ") + mScrollOffsetSecondary) + " mScrollOffsetPrimary ") + mScrollOffsetPrimary) + " inPreLayout ") + state.isPreLayout()) + " didStructureChange ") + state.didStructureChange()) + " mForceFullLayout ") + mForceFullLayout);
            android.util.Log.v(getTag(), (("width " + getWidth()) + " height ") + getHeight());
        }
        if (mNumRows == 0) {
            // haven't done measure yet
            return;
        }
        final int itemCount = state.getItemCount();
        if (itemCount < 0) {
            return;
        }
        if (!mLayoutEnabled) {
            discardLayoutInfo();
            removeAndRecycleAllViews(recycler);
            return;
        }
        mInLayout = true;
        if (state.didStructureChange()) {
            // didStructureChange() == true means attached item has been removed/added.
            // scroll animation: we are unable to continue a scroll animation,
            // kill the scroll animation,  and let ItemAnimation move the item to new position.
            // position smooth scroller: kill the animation and stop at final position.
            // pending smooth scroller: stop and scroll to current focus position.
            mBaseGridView.stopScroll();
        }
        final boolean scrollToFocus = (!isSmoothScrolling()) && (mFocusScrollStrategy == android.support.v17.leanback.widget.BaseGridView.FOCUS_SCROLL_ALIGNED);
        if ((mFocusPosition != android.support.v7.widget.RecyclerView.NO_POSITION) && (mFocusPositionOffset != java.lang.Integer.MIN_VALUE)) {
            mFocusPosition = mFocusPosition + mFocusPositionOffset;
            mSubFocusPosition = 0;
        }
        mFocusPositionOffset = 0;
        saveContext(recycler, state);
        android.view.View savedFocusView = findViewByPosition(mFocusPosition);
        int savedFocusPos = mFocusPosition;
        int savedSubFocusPos = mSubFocusPosition;
        boolean hadFocus = mBaseGridView.hasFocus();
        // Track the old focus view so we can adjust our system scroll position
        // so that any scroll animations happening now will remain valid.
        // We must use same delta in Pre Layout (if prelayout exists) and second layout.
        // So we cache the deltas in PreLayout and use it in second layout.
        int delta = 0;
        int deltaSecondary = 0;
        if (((mFocusPosition != android.support.v7.widget.RecyclerView.NO_POSITION) && scrollToFocus) && (mBaseGridView.getScrollState() != android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE)) {
            // FIXME: we should get the remaining scroll animation offset from RecyclerView
            if (savedFocusView != null) {
                if (getScrollPosition(savedFocusView, savedFocusView.findFocus(), android.support.v17.leanback.widget.GridLayoutManager.sTwoInts)) {
                    delta = android.support.v17.leanback.widget.GridLayoutManager.sTwoInts[0];
                    deltaSecondary = android.support.v17.leanback.widget.GridLayoutManager.sTwoInts[1];
                }
            }
        }
        if (mInFastRelayout = layoutInit()) {
            fastRelayout();
            // appends items till focus position.
            if (mFocusPosition != android.support.v7.widget.RecyclerView.NO_POSITION) {
                scrollToFocusViewInLayout(hadFocus, scrollToFocus);
            }
        } else {
            mInLayoutSearchFocus = hadFocus;
            if (mFocusPosition != android.support.v7.widget.RecyclerView.NO_POSITION) {
                // appends items till focus position.
                while (appendOneColumnVisibleItems() && (findViewByPosition(mFocusPosition) == null));
            }
            // multiple rounds: scrollToView of first round may drag first/last child into
            // "visible window" and we update scrollMin/scrollMax then run second scrollToView
            int oldFirstVisible;
            int oldLastVisible;
            do {
                updateScrollMin();
                updateScrollMax();
                oldFirstVisible = mGrid.getFirstVisibleIndex();
                oldLastVisible = mGrid.getLastVisibleIndex();
                scrollToFocusViewInLayout(hadFocus, true);
                appendVisibleItems();
                prependVisibleItems();
                removeInvisibleViewsAtFront();
                removeInvisibleViewsAtEnd();
            } while ((mGrid.getFirstVisibleIndex() != oldFirstVisible) || (mGrid.getLastVisibleIndex() != oldLastVisible) );
        }
        if (scrollToFocus) {
            scrollDirectionPrimary(-delta);
            scrollDirectionSecondary(-deltaSecondary);
        }
        appendVisibleItems();
        prependVisibleItems();
        removeInvisibleViewsAtFront();
        removeInvisibleViewsAtEnd();
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            mGrid.debugPrint(pw);
            android.util.Log.d(getTag(), sw.toString());
        }
        if (mRowSecondarySizeRefresh) {
            mRowSecondarySizeRefresh = false;
        } else {
            updateRowSecondarySizeRefresh();
        }
        // For fastRelayout, only dispatch event when focus position changes.
        if (mInFastRelayout && (((mFocusPosition != savedFocusPos) || (mSubFocusPosition != savedSubFocusPos)) || (findViewByPosition(mFocusPosition) != savedFocusView))) {
            dispatchChildSelected();
        } else
            if ((!mInFastRelayout) && mInLayoutSearchFocus) {
                // For full layout we dispatchChildSelected() in createItem() unless searched all
                // children and found none is focusable then dispatchChildSelected() here.
                dispatchChildSelected();
            }

        mInLayout = false;
        leaveContext();
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), "layoutChildren end");

    }

    private void offsetChildrenSecondary(int increment) {
        final int childCount = getChildCount();
        if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).offsetTopAndBottom(increment);
            }
        } else {
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).offsetLeftAndRight(increment);
            }
        }
    }

    private void offsetChildrenPrimary(int increment) {
        final int childCount = getChildCount();
        if (mOrientation == android.support.v7.widget.RecyclerView.VERTICAL) {
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).offsetTopAndBottom(increment);
            }
        } else {
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).offsetLeftAndRight(increment);
            }
        }
    }

    @java.lang.Override
    public int scrollHorizontallyBy(int dx, android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), "scrollHorizontallyBy " + dx);

        if ((!mLayoutEnabled) || (!hasDoneFirstLayout())) {
            return 0;
        }
        saveContext(recycler, state);
        mInScroll = true;
        int result;
        if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
            result = scrollDirectionPrimary(dx);
        } else {
            result = scrollDirectionSecondary(dx);
        }
        leaveContext();
        mInScroll = false;
        return result;
    }

    @java.lang.Override
    public int scrollVerticallyBy(int dy, android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), "scrollVerticallyBy " + dy);

        if ((!mLayoutEnabled) || (!hasDoneFirstLayout())) {
            return 0;
        }
        mInScroll = true;
        saveContext(recycler, state);
        int result;
        if (mOrientation == android.support.v7.widget.RecyclerView.VERTICAL) {
            result = scrollDirectionPrimary(dy);
        } else {
            result = scrollDirectionSecondary(dy);
        }
        leaveContext();
        mInScroll = false;
        return result;
    }

    // scroll in main direction may add/prune views
    private int scrollDirectionPrimary(int da) {
        if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
            android.support.v17.leanback.os.TraceHelper.beginSection("scrollPrimary");

        boolean isMaxUnknown = false;
        boolean isMinUnknown = false;
        int minScroll = 0;
        int maxScroll = 0;
        if (da > 0) {
            isMaxUnknown = mWindowAlignment.mainAxis().isMaxUnknown();
            if (!isMaxUnknown) {
                maxScroll = mWindowAlignment.mainAxis().getMaxScroll();
                if ((mScrollOffsetPrimary + da) > maxScroll) {
                    da = maxScroll - mScrollOffsetPrimary;
                }
            }
        } else
            if (da < 0) {
                isMinUnknown = mWindowAlignment.mainAxis().isMinUnknown();
                if (!isMinUnknown) {
                    minScroll = mWindowAlignment.mainAxis().getMinScroll();
                    if ((mScrollOffsetPrimary + da) < minScroll) {
                        da = minScroll - mScrollOffsetPrimary;
                    }
                }
            }

        if (da == 0) {
            if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
                android.support.v17.leanback.os.TraceHelper.endSection();

            return 0;
        }
        offsetChildrenPrimary(-da);
        mScrollOffsetPrimary += da;
        if (mInLayout) {
            if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
                android.support.v17.leanback.os.TraceHelper.endSection();

            return da;
        }
        int childCount = getChildCount();
        boolean updated;
        if (mReverseFlowPrimary ? da > 0 : da < 0) {
            prependVisibleItems();
        } else {
            appendVisibleItems();
        }
        updated = getChildCount() > childCount;
        childCount = getChildCount();
        if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
            android.support.v17.leanback.os.TraceHelper.beginSection("remove");

        if (mReverseFlowPrimary ? da > 0 : da < 0) {
            removeInvisibleViewsAtEnd();
        } else {
            removeInvisibleViewsAtFront();
        }
        if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
            android.support.v17.leanback.os.TraceHelper.endSection();

        updated |= getChildCount() < childCount;
        if (updated) {
            updateRowSecondarySizeRefresh();
        }
        mBaseGridView.invalidate();
        if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
            android.support.v17.leanback.os.TraceHelper.endSection();

        return da;
    }

    // scroll in second direction will not add/prune views
    private int scrollDirectionSecondary(int dy) {
        if (dy == 0) {
            return 0;
        }
        offsetChildrenSecondary(-dy);
        mScrollOffsetSecondary += dy;
        mBaseGridView.invalidate();
        return dy;
    }

    void updateScrollMax() {
        int highVisiblePos = (!mReverseFlowPrimary) ? mGrid.getLastVisibleIndex() : mGrid.getFirstVisibleIndex();
        int highMaxPos = (!mReverseFlowPrimary) ? mState.getItemCount() - 1 : 0;
        if (highVisiblePos < 0) {
            return;
        }
        final boolean highAvailable = highVisiblePos == highMaxPos;
        final boolean maxUnknown = mWindowAlignment.mainAxis().isMaxUnknown();
        if ((!highAvailable) && maxUnknown) {
            return;
        }
        int maxEdge = mGrid.findRowMax(true, android.support.v17.leanback.widget.GridLayoutManager.sTwoInts) + mScrollOffsetPrimary;
        int rowIndex = android.support.v17.leanback.widget.GridLayoutManager.sTwoInts[0];
        int pos = android.support.v17.leanback.widget.GridLayoutManager.sTwoInts[1];
        int savedMaxEdge = mWindowAlignment.mainAxis().getMaxEdge();
        mWindowAlignment.mainAxis().setMaxEdge(maxEdge);
        int maxScroll = getPrimarySystemScrollPositionOfChildMax(findViewByPosition(pos));
        mWindowAlignment.mainAxis().setMaxEdge(savedMaxEdge);
        if (highAvailable) {
            mWindowAlignment.mainAxis().setMaxEdge(maxEdge);
            mWindowAlignment.mainAxis().setMaxScroll(maxScroll);
            if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
                android.util.Log.v(getTag(), (("updating scroll maxEdge to " + maxEdge) + " scrollMax to ") + maxScroll);

        } else {
            mWindowAlignment.mainAxis().invalidateScrollMax();
            if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
                android.util.Log.v(getTag(), ("Invalidate scrollMax since it should be " + "greater than ") + maxScroll);

        }
    }

    void updateScrollMin() {
        int lowVisiblePos = (!mReverseFlowPrimary) ? mGrid.getFirstVisibleIndex() : mGrid.getLastVisibleIndex();
        int lowMinPos = (!mReverseFlowPrimary) ? 0 : mState.getItemCount() - 1;
        if (lowVisiblePos < 0) {
            return;
        }
        final boolean lowAvailable = lowVisiblePos == lowMinPos;
        final boolean minUnknown = mWindowAlignment.mainAxis().isMinUnknown();
        if ((!lowAvailable) && minUnknown) {
            return;
        }
        int minEdge = mGrid.findRowMin(false, android.support.v17.leanback.widget.GridLayoutManager.sTwoInts) + mScrollOffsetPrimary;
        int rowIndex = android.support.v17.leanback.widget.GridLayoutManager.sTwoInts[0];
        int pos = android.support.v17.leanback.widget.GridLayoutManager.sTwoInts[1];
        int savedMinEdge = mWindowAlignment.mainAxis().getMinEdge();
        mWindowAlignment.mainAxis().setMinEdge(minEdge);
        int minScroll = getPrimarySystemScrollPosition(findViewByPosition(pos));
        mWindowAlignment.mainAxis().setMinEdge(savedMinEdge);
        if (lowAvailable) {
            mWindowAlignment.mainAxis().setMinEdge(minEdge);
            mWindowAlignment.mainAxis().setMinScroll(minScroll);
            if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
                android.util.Log.v(getTag(), (("updating scroll minEdge to " + minEdge) + " scrollMin to ") + minScroll);

        } else {
            mWindowAlignment.mainAxis().invalidateScrollMin();
            if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
                android.util.Log.v(getTag(), ("Invalidate scrollMin, since it should be " + "less than ") + minScroll);

        }
    }

    private void updateScrollSecondAxis() {
        mWindowAlignment.secondAxis().setMinEdge(0);
        mWindowAlignment.secondAxis().setMaxEdge(getSizeSecondary());
    }

    private void initScrollController() {
        mWindowAlignment.reset();
        mWindowAlignment.horizontal.setSize(getWidth());
        mWindowAlignment.vertical.setSize(getHeight());
        mWindowAlignment.horizontal.setPadding(getPaddingLeft(), getPaddingRight());
        mWindowAlignment.vertical.setPadding(getPaddingTop(), getPaddingBottom());
        mSizePrimary = mWindowAlignment.mainAxis().getSize();
        mScrollOffsetPrimary = -mWindowAlignment.mainAxis().getPaddingLow();
        mScrollOffsetSecondary = -mWindowAlignment.secondAxis().getPaddingLow();
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG) {
            android.util.Log.v(getTag(), (((("initScrollController mSizePrimary " + mSizePrimary) + " mWindowAlignment ") + mWindowAlignment) + " mScrollOffsetPrimary ") + mScrollOffsetPrimary);
        }
    }

    private void updateScrollController() {
        // mScrollOffsetPrimary and mScrollOffsetSecondary includes the padding.
        // e.g. when topPadding is 16 for horizontal grid view,  the initial
        // mScrollOffsetSecondary is -16.  fastRelayout() put views based on offsets(not padding),
        // when padding changes to 20,  we also need update mScrollOffsetSecondary to -20 before
        // fastRelayout() is performed
        int paddingPrimaryDiff;
        int paddingSecondaryDiff;
        if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
            paddingPrimaryDiff = getPaddingLeft() - mWindowAlignment.horizontal.getPaddingLow();
            paddingSecondaryDiff = getPaddingTop() - mWindowAlignment.vertical.getPaddingLow();
        } else {
            paddingPrimaryDiff = getPaddingTop() - mWindowAlignment.vertical.getPaddingLow();
            paddingSecondaryDiff = getPaddingLeft() - mWindowAlignment.horizontal.getPaddingLow();
        }
        mScrollOffsetPrimary -= paddingPrimaryDiff;
        mScrollOffsetSecondary -= paddingSecondaryDiff;
        mWindowAlignment.horizontal.setSize(getWidth());
        mWindowAlignment.vertical.setSize(getHeight());
        mWindowAlignment.horizontal.setPadding(getPaddingLeft(), getPaddingRight());
        mWindowAlignment.vertical.setPadding(getPaddingTop(), getPaddingBottom());
        mSizePrimary = mWindowAlignment.mainAxis().getSize();
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG) {
            android.util.Log.v(getTag(), (((("updateScrollController mSizePrimary " + mSizePrimary) + " mWindowAlignment ") + mWindowAlignment) + " mScrollOffsetPrimary ") + mScrollOffsetPrimary);
        }
    }

    @java.lang.Override
    public void scrollToPosition(int position) {
        setSelection(position, 0, false, 0);
    }

    public void setSelection(int position, int primaryScrollExtra) {
        setSelection(position, 0, false, primaryScrollExtra);
    }

    public void setSelectionSmooth(int position) {
        setSelection(position, 0, true, 0);
    }

    public void setSelectionWithSub(int position, int subposition, int primaryScrollExtra) {
        setSelection(position, subposition, false, primaryScrollExtra);
    }

    public void setSelectionSmoothWithSub(int position, int subposition) {
        setSelection(position, subposition, true, 0);
    }

    public int getSelection() {
        return mFocusPosition;
    }

    public int getSubSelection() {
        return mSubFocusPosition;
    }

    public void setSelection(int position, int subposition, boolean smooth, int primaryScrollExtra) {
        if ((((mFocusPosition != position) && (position != android.support.v7.widget.RecyclerView.NO_POSITION)) || (subposition != mSubFocusPosition)) || (primaryScrollExtra != mPrimaryScrollExtra)) {
            scrollToSelection(position, subposition, smooth, primaryScrollExtra);
        }
    }

    void scrollToSelection(int position, int subposition, boolean smooth, int primaryScrollExtra) {
        if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
            android.support.v17.leanback.os.TraceHelper.beginSection("scrollToSelection");

        mPrimaryScrollExtra = primaryScrollExtra;
        android.view.View view = findViewByPosition(position);
        if (view != null) {
            mInSelection = true;
            scrollToView(view, smooth);
            mInSelection = false;
        } else {
            mFocusPosition = position;
            mSubFocusPosition = subposition;
            mFocusPositionOffset = java.lang.Integer.MIN_VALUE;
            if (!mLayoutEnabled) {
                return;
            }
            if (smooth) {
                if (!hasDoneFirstLayout()) {
                    android.util.Log.w(getTag(), "setSelectionSmooth should " + "not be called before first layout pass");
                    return;
                }
                startPositionSmoothScroller(position);
            } else {
                mForceFullLayout = true;
                requestLayout();
            }
        }
        if (android.support.v17.leanback.widget.GridLayoutManager.TRACE)
            android.support.v17.leanback.os.TraceHelper.endSection();

    }

    void startPositionSmoothScroller(int position) {
        android.support.v7.widget.LinearSmoothScroller linearSmoothScroller = new android.support.v17.leanback.widget.GridLayoutManager.GridLinearSmoothScroller() {
            @java.lang.Override
            public android.graphics.PointF computeScrollVectorForPosition(int targetPosition) {
                if (getChildCount() == 0) {
                    return null;
                }
                final int firstChildPos = getPosition(getChildAt(0));
                // TODO We should be able to deduce direction from bounds of current and target
                // focus, rather than making assumptions about positions and directionality
                final boolean isStart = (mReverseFlowPrimary) ? targetPosition > firstChildPos : targetPosition < firstChildPos;
                final int direction = (isStart) ? -1 : 1;
                if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
                    return new android.graphics.PointF(direction, 0);
                } else {
                    return new android.graphics.PointF(0, direction);
                }
            }
        };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    private void processPendingMovement(boolean forward) {
        if (forward ? hasCreatedLastItem() : hasCreatedFirstItem()) {
            return;
        }
        if (mPendingMoveSmoothScroller == null) {
            // Stop existing scroller and create a new PendingMoveSmoothScroller.
            mBaseGridView.stopScroll();
            android.support.v17.leanback.widget.GridLayoutManager.PendingMoveSmoothScroller linearSmoothScroller = new android.support.v17.leanback.widget.GridLayoutManager.PendingMoveSmoothScroller(forward ? 1 : -1, mNumRows > 1);
            mFocusPositionOffset = 0;
            startSmoothScroll(linearSmoothScroller);
            if (linearSmoothScroller.isRunning()) {
                mPendingMoveSmoothScroller = linearSmoothScroller;
            }
        } else {
            if (forward) {
                mPendingMoveSmoothScroller.increasePendingMoves();
            } else {
                mPendingMoveSmoothScroller.decreasePendingMoves();
            }
        }
    }

    @java.lang.Override
    public void onItemsAdded(android.support.v7.widget.RecyclerView recyclerView, int positionStart, int itemCount) {
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), (("onItemsAdded positionStart " + positionStart) + " itemCount ") + itemCount);

        if ((((mFocusPosition != android.support.v7.widget.RecyclerView.NO_POSITION) && (mGrid != null)) && (mGrid.getFirstVisibleIndex() >= 0)) && (mFocusPositionOffset != java.lang.Integer.MIN_VALUE)) {
            int pos = mFocusPosition + mFocusPositionOffset;
            if (positionStart <= pos) {
                mFocusPositionOffset += itemCount;
            }
        }
        mChildrenStates.clear();
    }

    @java.lang.Override
    public void onItemsChanged(android.support.v7.widget.RecyclerView recyclerView) {
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), "onItemsChanged");

        mFocusPositionOffset = 0;
        mChildrenStates.clear();
    }

    @java.lang.Override
    public void onItemsRemoved(android.support.v7.widget.RecyclerView recyclerView, int positionStart, int itemCount) {
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), (("onItemsRemoved positionStart " + positionStart) + " itemCount ") + itemCount);

        if ((((mFocusPosition != android.support.v7.widget.RecyclerView.NO_POSITION) && (mGrid != null)) && (mGrid.getFirstVisibleIndex() >= 0)) && (mFocusPositionOffset != java.lang.Integer.MIN_VALUE)) {
            int pos = mFocusPosition + mFocusPositionOffset;
            if (positionStart <= pos) {
                if ((positionStart + itemCount) > pos) {
                    // stop updating offset after the focus item was removed
                    mFocusPositionOffset = java.lang.Integer.MIN_VALUE;
                } else {
                    mFocusPositionOffset -= itemCount;
                }
            }
        }
        mChildrenStates.clear();
    }

    @java.lang.Override
    public void onItemsMoved(android.support.v7.widget.RecyclerView recyclerView, int fromPosition, int toPosition, int itemCount) {
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), (("onItemsMoved fromPosition " + fromPosition) + " toPosition ") + toPosition);

        if ((mFocusPosition != android.support.v7.widget.RecyclerView.NO_POSITION) && (mFocusPositionOffset != java.lang.Integer.MIN_VALUE)) {
            int pos = mFocusPosition + mFocusPositionOffset;
            if ((fromPosition <= pos) && (pos < (fromPosition + itemCount))) {
                // moved items include focused position
                mFocusPositionOffset += toPosition - fromPosition;
            } else
                if ((fromPosition < pos) && (toPosition > (pos - itemCount))) {
                    // move items before focus position to after focused position
                    mFocusPositionOffset -= itemCount;
                } else
                    if ((fromPosition > pos) && (toPosition < pos)) {
                        // move items after focus position to before focused position
                        mFocusPositionOffset += itemCount;
                    }


        }
        mChildrenStates.clear();
    }

    @java.lang.Override
    public void onItemsUpdated(android.support.v7.widget.RecyclerView recyclerView, int positionStart, int itemCount) {
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), (("onItemsUpdated positionStart " + positionStart) + " itemCount ") + itemCount);

        for (int i = positionStart, end = positionStart + itemCount; i < end; i++) {
            mChildrenStates.remove(i);
        }
    }

    @java.lang.Override
    public boolean onRequestChildFocus(android.support.v7.widget.RecyclerView parent, android.view.View child, android.view.View focused) {
        if (mFocusSearchDisabled) {
            return true;
        }
        if (getPositionByView(child) == android.support.v7.widget.RecyclerView.NO_POSITION) {
            // This shouldn't happen, but in case it does be sure not to attempt a
            // scroll to a view whose item has been removed.
            return true;
        }
        if (((!mInLayout) && (!mInSelection)) && (!mInScroll)) {
            scrollToView(child, focused, true);
        }
        return true;
    }

    @java.lang.Override
    public boolean requestChildRectangleOnScreen(android.support.v7.widget.RecyclerView parent, android.view.View view, android.graphics.Rect rect, boolean immediate) {
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), (("requestChildRectangleOnScreen " + view) + " ") + rect);

        return false;
    }

    int getScrollOffsetX() {
        return mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL ? mScrollOffsetPrimary : mScrollOffsetSecondary;
    }

    int getScrollOffsetY() {
        return mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL ? mScrollOffsetSecondary : mScrollOffsetPrimary;
    }

    public void getViewSelectedOffsets(android.view.View view, int[] offsets) {
        if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
            offsets[0] = getPrimarySystemScrollPosition(view) - mScrollOffsetPrimary;
            offsets[1] = getSecondarySystemScrollPosition(view) - mScrollOffsetSecondary;
        } else {
            offsets[1] = getPrimarySystemScrollPosition(view) - mScrollOffsetPrimary;
            offsets[0] = getSecondarySystemScrollPosition(view) - mScrollOffsetSecondary;
        }
    }

    private int getPrimarySystemScrollPosition(android.view.View view) {
        final int viewCenterPrimary = mScrollOffsetPrimary + getViewCenter(view);
        final int viewMin = getViewMin(view);
        final int viewMax = getViewMax(view);
        // TODO: change to use State object in onRequestChildFocus()
        boolean isMin;
        boolean isMax;
        if (!mReverseFlowPrimary) {
            isMin = mGrid.getFirstVisibleIndex() == 0;
            isMax = mGrid.getLastVisibleIndex() == ((mState == null ? getItemCount() : mState.getItemCount()) - 1);
        } else {
            isMax = mGrid.getFirstVisibleIndex() == 0;
            isMin = mGrid.getLastVisibleIndex() == ((mState == null ? getItemCount() : mState.getItemCount()) - 1);
        }
        for (int i = getChildCount() - 1; (isMin || isMax) && (i >= 0); i--) {
            android.view.View v = getChildAt(i);
            if ((v == view) || (v == null)) {
                continue;
            }
            if (isMin && (getViewMin(v) < viewMin)) {
                isMin = false;
            }
            if (isMax && (getViewMax(v) > viewMax)) {
                isMax = false;
            }
        }
        return mWindowAlignment.mainAxis().getSystemScrollPos(viewCenterPrimary, isMin, isMax);
    }

    private int getPrimarySystemScrollPositionOfChildMax(android.view.View view) {
        int scrollPosition = getPrimarySystemScrollPosition(view);
        final android.support.v17.leanback.widget.GridLayoutManager.LayoutParams lp = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (view.getLayoutParams()));
        int[] multipleAligns = lp.getAlignMultiple();
        if ((multipleAligns != null) && (multipleAligns.length > 0)) {
            scrollPosition += multipleAligns[multipleAligns.length - 1] - multipleAligns[0];
        }
        return scrollPosition;
    }

    /**
     * Get adjusted primary position for a given childView (if there is multiple ItemAlignment defined
     * on the view).
     */
    private int getAdjustedPrimaryScrollPosition(int scrollPrimary, android.view.View view, android.view.View childView) {
        int subindex = getSubPositionByView(view, childView);
        if (subindex != 0) {
            final android.support.v17.leanback.widget.GridLayoutManager.LayoutParams lp = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (view.getLayoutParams()));
            scrollPrimary += lp.getAlignMultiple()[subindex] - lp.getAlignMultiple()[0];
        }
        return scrollPrimary;
    }

    private int getSecondarySystemScrollPosition(android.view.View view) {
        int viewCenterSecondary = mScrollOffsetSecondary + getViewCenterSecondary(view);
        int pos = getPositionByView(view);
        android.support.v17.leanback.widget.Grid.Location location = mGrid.getLocation(pos);
        final int row = location.row;
        final boolean isMin;
        final boolean isMax;
        if (!mReverseFlowSecondary) {
            isMin = row == 0;
            isMax = row == (mGrid.getNumRows() - 1);
        } else {
            isMax = row == 0;
            isMin = row == (mGrid.getNumRows() - 1);
        }
        return mWindowAlignment.secondAxis().getSystemScrollPos(viewCenterSecondary, isMin, isMax);
    }

    /**
     * Scroll to a given child view and change mFocusPosition.
     */
    void scrollToView(android.view.View view, boolean smooth) {
        scrollToView(view, view == null ? null : view.findFocus(), smooth);
    }

    /**
     * Scroll to a given child view and change mFocusPosition.
     */
    private void scrollToView(android.view.View view, android.view.View childView, boolean smooth) {
        int newFocusPosition = getPositionByView(view);
        int newSubFocusPosition = getSubPositionByView(view, childView);
        if ((newFocusPosition != mFocusPosition) || (newSubFocusPosition != mSubFocusPosition)) {
            mFocusPosition = newFocusPosition;
            mSubFocusPosition = newSubFocusPosition;
            mFocusPositionOffset = 0;
            if (!mInLayout) {
                dispatchChildSelected();
            }
            if (mBaseGridView.isChildrenDrawingOrderEnabledInternal()) {
                mBaseGridView.invalidate();
            }
        }
        if (view == null) {
            return;
        }
        if ((!view.hasFocus()) && mBaseGridView.hasFocus()) {
            // transfer focus to the child if it does not have focus yet (e.g. triggered
            // by setSelection())
            view.requestFocus();
        }
        if ((!mScrollEnabled) && smooth) {
            return;
        }
        if (getScrollPosition(view, childView, android.support.v17.leanback.widget.GridLayoutManager.sTwoInts)) {
            scrollGrid(android.support.v17.leanback.widget.GridLayoutManager.sTwoInts[0], android.support.v17.leanback.widget.GridLayoutManager.sTwoInts[1], smooth);
        }
    }

    boolean getScrollPosition(android.view.View view, android.view.View childView, int[] deltas) {
        switch (mFocusScrollStrategy) {
            case android.support.v17.leanback.widget.BaseGridView.FOCUS_SCROLL_ALIGNED :
            default :
                return getAlignedPosition(view, childView, deltas);
            case android.support.v17.leanback.widget.BaseGridView.FOCUS_SCROLL_ITEM :
            case android.support.v17.leanback.widget.BaseGridView.FOCUS_SCROLL_PAGE :
                return getNoneAlignedPosition(view, deltas);
        }
    }

    private boolean getNoneAlignedPosition(android.view.View view, int[] deltas) {
        int pos = getPositionByView(view);
        int viewMin = getViewMin(view);
        int viewMax = getViewMax(view);
        // we either align "firstView" to left/top padding edge
        // or align "lastView" to right/bottom padding edge
        android.view.View firstView = null;
        android.view.View lastView = null;
        int paddingLow = mWindowAlignment.mainAxis().getPaddingLow();
        int clientSize = mWindowAlignment.mainAxis().getClientSize();
        final int row = mGrid.getRowIndex(pos);
        if (viewMin < paddingLow) {
            // view enters low padding area:
            firstView = view;
            if (mFocusScrollStrategy == android.support.v17.leanback.widget.BaseGridView.FOCUS_SCROLL_PAGE) {
                // scroll one "page" left/top,
                // align first visible item of the "page" at the low padding edge.
                while (prependOneColumnVisibleItems()) {
                    android.support.v4.util.CircularIntArray positions = mGrid.getItemPositionsInRows(mGrid.getFirstVisibleIndex(), pos)[row];
                    firstView = findViewByPosition(positions.get(0));
                    if ((viewMax - getViewMin(firstView)) > clientSize) {
                        if (positions.size() > 2) {
                            firstView = findViewByPosition(positions.get(2));
                        }
                        break;
                    }
                } 
            }
        } else
            if (viewMax > (clientSize + paddingLow)) {
                // view enters high padding area:
                if (mFocusScrollStrategy == android.support.v17.leanback.widget.BaseGridView.FOCUS_SCROLL_PAGE) {
                    // scroll whole one page right/bottom, align view at the low padding edge.
                    firstView = view;
                    do {
                        android.support.v4.util.CircularIntArray positions = mGrid.getItemPositionsInRows(pos, mGrid.getLastVisibleIndex())[row];
                        lastView = findViewByPosition(positions.get(positions.size() - 1));
                        if ((getViewMax(lastView) - viewMin) > clientSize) {
                            lastView = null;
                            break;
                        }
                    } while (appendOneColumnVisibleItems() );
                    if (lastView != null) {
                        // however if we reached end,  we should align last view.
                        firstView = null;
                    }
                } else {
                    lastView = view;
                }
            }

        int scrollPrimary = 0;
        int scrollSecondary = 0;
        if (firstView != null) {
            scrollPrimary = getViewMin(firstView) - paddingLow;
        } else
            if (lastView != null) {
                scrollPrimary = getViewMax(lastView) - (paddingLow + clientSize);
            }

        android.view.View secondaryAlignedView;
        if (firstView != null) {
            secondaryAlignedView = firstView;
        } else
            if (lastView != null) {
                secondaryAlignedView = lastView;
            } else {
                secondaryAlignedView = view;
            }

        scrollSecondary = getSecondarySystemScrollPosition(secondaryAlignedView);
        scrollSecondary -= mScrollOffsetSecondary;
        if ((scrollPrimary != 0) || (scrollSecondary != 0)) {
            deltas[0] = scrollPrimary;
            deltas[1] = scrollSecondary;
            return true;
        }
        return false;
    }

    private boolean getAlignedPosition(android.view.View view, android.view.View childView, int[] deltas) {
        int scrollPrimary = getPrimarySystemScrollPosition(view);
        if (childView != null) {
            scrollPrimary = getAdjustedPrimaryScrollPosition(scrollPrimary, view, childView);
        }
        int scrollSecondary = getSecondarySystemScrollPosition(view);
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG) {
            android.util.Log.v(getTag(), (((((("getAlignedPosition " + scrollPrimary) + " ") + scrollSecondary) + " ") + mPrimaryScrollExtra) + " ") + mWindowAlignment);
            android.util.Log.v(getTag(), (("getAlignedPosition " + mScrollOffsetPrimary) + " ") + mScrollOffsetSecondary);
        }
        scrollPrimary -= mScrollOffsetPrimary;
        scrollSecondary -= mScrollOffsetSecondary;
        scrollPrimary += mPrimaryScrollExtra;
        if ((scrollPrimary != 0) || (scrollSecondary != 0)) {
            deltas[0] = scrollPrimary;
            deltas[1] = scrollSecondary;
            return true;
        }
        return false;
    }

    private void scrollGrid(int scrollPrimary, int scrollSecondary, boolean smooth) {
        if (mInLayout) {
            scrollDirectionPrimary(scrollPrimary);
            scrollDirectionSecondary(scrollSecondary);
        } else {
            int scrollX;
            int scrollY;
            if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
                scrollX = scrollPrimary;
                scrollY = scrollSecondary;
            } else {
                scrollX = scrollSecondary;
                scrollY = scrollPrimary;
            }
            if (smooth) {
                mBaseGridView.smoothScrollBy(scrollX, scrollY);
            } else {
                mBaseGridView.scrollBy(scrollX, scrollY);
            }
        }
    }

    public void setPruneChild(boolean pruneChild) {
        if (mPruneChild != pruneChild) {
            mPruneChild = pruneChild;
            if (mPruneChild) {
                requestLayout();
            }
        }
    }

    public boolean getPruneChild() {
        return mPruneChild;
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        if (mScrollEnabled != scrollEnabled) {
            mScrollEnabled = scrollEnabled;
            if ((mScrollEnabled && (mFocusScrollStrategy == android.support.v17.leanback.widget.BaseGridView.FOCUS_SCROLL_ALIGNED)) && (mFocusPosition != android.support.v7.widget.RecyclerView.NO_POSITION)) {
                scrollToSelection(mFocusPosition, mSubFocusPosition, true, mPrimaryScrollExtra);
            }
        }
    }

    public boolean isScrollEnabled() {
        return mScrollEnabled;
    }

    private int findImmediateChildIndex(android.view.View view) {
        if ((mBaseGridView != null) && (view != mBaseGridView)) {
            view = findContainingItemView(view);
            if (view != null) {
                for (int i = 0, count = getChildCount(); i < count; i++) {
                    if (getChildAt(i) == view) {
                        return i;
                    }
                }
            }
        }
        return android.support.v7.widget.RecyclerView.NO_POSITION;
    }

    void onFocusChanged(boolean gainFocus, int direction, android.graphics.Rect previouslyFocusedRect) {
        if (gainFocus) {
            // if gridview.requestFocus() is called, select first focusable child.
            for (int i = mFocusPosition; ; i++) {
                android.view.View view = findViewByPosition(i);
                if (view == null) {
                    break;
                }
                if ((view.getVisibility() == android.view.View.VISIBLE) && view.hasFocusable()) {
                    view.requestFocus();
                    break;
                }
            }
        }
    }

    void setFocusSearchDisabled(boolean disabled) {
        mFocusSearchDisabled = disabled;
    }

    boolean isFocusSearchDisabled() {
        return mFocusSearchDisabled;
    }

    @java.lang.Override
    public android.view.View onInterceptFocusSearch(android.view.View focused, int direction) {
        if (mFocusSearchDisabled) {
            return focused;
        }
        final android.view.FocusFinder ff = android.view.FocusFinder.getInstance();
        android.view.View result = null;
        if ((direction == android.view.View.FOCUS_FORWARD) || (direction == android.view.View.FOCUS_BACKWARD)) {
            // convert direction to absolute direction and see if we have a view there and if not
            // tell LayoutManager to add if it can.
            if (canScrollVertically()) {
                final int absDir = (direction == android.view.View.FOCUS_FORWARD) ? android.view.View.FOCUS_DOWN : android.view.View.FOCUS_UP;
                result = ff.findNextFocus(mBaseGridView, focused, absDir);
            }
            if (canScrollHorizontally()) {
                boolean rtl = getLayoutDirection() == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL;
                final int absDir = ((direction == android.view.View.FOCUS_FORWARD) ^ rtl) ? android.view.View.FOCUS_RIGHT : android.view.View.FOCUS_LEFT;
                result = ff.findNextFocus(mBaseGridView, focused, absDir);
            }
        } else {
            result = ff.findNextFocus(mBaseGridView, focused, direction);
        }
        if (result != null) {
            return result;
        }
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), "regular focusSearch failed direction " + direction);

        int movement = getMovement(direction);
        final boolean isScroll = mBaseGridView.getScrollState() != android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
        if (movement == android.support.v17.leanback.widget.GridLayoutManager.NEXT_ITEM) {
            if (isScroll || (!mFocusOutEnd)) {
                result = focused;
            }
            if (mScrollEnabled && (!hasCreatedLastItem())) {
                processPendingMovement(true);
                result = focused;
            }
        } else
            if (movement == android.support.v17.leanback.widget.GridLayoutManager.PREV_ITEM) {
                if (isScroll || (!mFocusOutFront)) {
                    result = focused;
                }
                if (mScrollEnabled && (!hasCreatedFirstItem())) {
                    processPendingMovement(false);
                    result = focused;
                }
            } else
                if (movement == android.support.v17.leanback.widget.GridLayoutManager.NEXT_ROW) {
                    if (isScroll || (!mFocusOutSideEnd)) {
                        result = focused;
                    }
                } else
                    if (movement == android.support.v17.leanback.widget.GridLayoutManager.PREV_ROW) {
                        if (isScroll || (!mFocusOutSideStart)) {
                            result = focused;
                        }
                    }



        if (result != null) {
            return result;
        }
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), "now focusSearch in parent");

        result = mBaseGridView.getParent().focusSearch(focused, direction);
        if (result != null) {
            return result;
        }
        return focused != null ? focused : mBaseGridView;
    }

    boolean hasPreviousViewInSameRow(int pos) {
        if (((mGrid == null) || (pos == android.support.v7.widget.RecyclerView.NO_POSITION)) || (mGrid.getFirstVisibleIndex() < 0)) {
            return false;
        }
        if (mGrid.getFirstVisibleIndex() > 0) {
            return true;
        }
        final int focusedRow = mGrid.getLocation(pos).row;
        for (int i = getChildCount() - 1; i >= 0; i--) {
            int position = getPositionByIndex(i);
            android.support.v17.leanback.widget.Grid.Location loc = mGrid.getLocation(position);
            if ((loc != null) && (loc.row == focusedRow)) {
                if (position < pos) {
                    return true;
                }
            }
        }
        return false;
    }

    @java.lang.Override
    public boolean onAddFocusables(android.support.v7.widget.RecyclerView recyclerView, java.util.ArrayList<android.view.View> views, int direction, int focusableMode) {
        if (mFocusSearchDisabled) {
            return true;
        }
        // If this viewgroup or one of its children currently has focus then we
        // consider our children for focus searching in main direction on the same row.
        // If this viewgroup has no focus and using focus align, we want the system
        // to ignore our children and pass focus to the viewgroup, which will pass
        // focus on to its children appropriately.
        // If this viewgroup has no focus and not using focus align, we want to
        // consider the child that does not overlap with padding area.
        if (recyclerView.hasFocus()) {
            if (mPendingMoveSmoothScroller != null) {
                // don't find next focusable if has pending movement.
                return true;
            }
            final int movement = getMovement(direction);
            final android.view.View focused = recyclerView.findFocus();
            final int focusedIndex = findImmediateChildIndex(focused);
            final int focusedPos = getPositionByIndex(focusedIndex);
            // Add focusables of focused item.
            if (focusedPos != android.support.v7.widget.RecyclerView.NO_POSITION) {
                findViewByPosition(focusedPos).addFocusables(views, direction, focusableMode);
            }
            if ((mGrid == null) || (getChildCount() == 0)) {
                // no grid information, or no child, bail out.
                return true;
            }
            if (((movement == android.support.v17.leanback.widget.GridLayoutManager.NEXT_ROW) || (movement == android.support.v17.leanback.widget.GridLayoutManager.PREV_ROW)) && (mGrid.getNumRows() <= 1)) {
                // For single row, cannot navigate to previous/next row.
                return true;
            }
            // Add focusables of neighbor depending on the focus search direction.
            final int focusedRow = ((mGrid != null) && (focusedPos != android.support.v7.widget.RecyclerView.NO_POSITION)) ? mGrid.getLocation(focusedPos).row : android.support.v7.widget.RecyclerView.NO_POSITION;
            final int focusableCount = views.size();
            int inc = ((movement == android.support.v17.leanback.widget.GridLayoutManager.NEXT_ITEM) || (movement == android.support.v17.leanback.widget.GridLayoutManager.NEXT_ROW)) ? 1 : -1;
            int loop_end = (inc > 0) ? getChildCount() - 1 : 0;
            int loop_start;
            if (focusedIndex == android.support.v7.widget.RecyclerView.NO_POSITION) {
                loop_start = (inc > 0) ? 0 : getChildCount() - 1;
            } else {
                loop_start = focusedIndex + inc;
            }
            for (int i = loop_start; inc > 0 ? i <= loop_end : i >= loop_end; i += inc) {
                final android.view.View child = getChildAt(i);
                if ((child.getVisibility() != android.view.View.VISIBLE) || (!child.hasFocusable())) {
                    continue;
                }
                // if there wasn't any focusing item,  add the very first focusable
                // items and stop.
                if (focusedPos == android.support.v7.widget.RecyclerView.NO_POSITION) {
                    child.addFocusables(views, direction, focusableMode);
                    if (views.size() > focusableCount) {
                        break;
                    }
                    continue;
                }
                int position = getPositionByIndex(i);
                android.support.v17.leanback.widget.Grid.Location loc = mGrid.getLocation(position);
                if (loc == null) {
                    continue;
                }
                if (movement == android.support.v17.leanback.widget.GridLayoutManager.NEXT_ITEM) {
                    // Add first focusable item on the same row
                    if ((loc.row == focusedRow) && (position > focusedPos)) {
                        child.addFocusables(views, direction, focusableMode);
                        if (views.size() > focusableCount) {
                            break;
                        }
                    }
                } else
                    if (movement == android.support.v17.leanback.widget.GridLayoutManager.PREV_ITEM) {
                        // Add first focusable item on the same row
                        if ((loc.row == focusedRow) && (position < focusedPos)) {
                            child.addFocusables(views, direction, focusableMode);
                            if (views.size() > focusableCount) {
                                break;
                            }
                        }
                    } else
                        if (movement == android.support.v17.leanback.widget.GridLayoutManager.NEXT_ROW) {
                            // Add all focusable items after this item whose row index is bigger
                            if (loc.row == focusedRow) {
                                continue;
                            } else
                                if (loc.row < focusedRow) {
                                    break;
                                }

                            child.addFocusables(views, direction, focusableMode);
                        } else
                            if (movement == android.support.v17.leanback.widget.GridLayoutManager.PREV_ROW) {
                                // Add all focusable items before this item whose row index is smaller
                                if (loc.row == focusedRow) {
                                    continue;
                                } else
                                    if (loc.row > focusedRow) {
                                        break;
                                    }

                                child.addFocusables(views, direction, focusableMode);
                            }



            }
        } else {
            int focusableCount = views.size();
            if (mFocusScrollStrategy != android.support.v17.leanback.widget.BaseGridView.FOCUS_SCROLL_ALIGNED) {
                // adding views not overlapping padding area to avoid scrolling in gaining focus
                int left = mWindowAlignment.mainAxis().getPaddingLow();
                int right = mWindowAlignment.mainAxis().getClientSize() + left;
                for (int i = 0, count = getChildCount(); i < count; i++) {
                    android.view.View child = getChildAt(i);
                    if (child.getVisibility() == android.view.View.VISIBLE) {
                        if ((getViewMin(child) >= left) && (getViewMax(child) <= right)) {
                            child.addFocusables(views, direction, focusableMode);
                        }
                    }
                }
                // if we cannot find any, then just add all children.
                if (views.size() == focusableCount) {
                    for (int i = 0, count = getChildCount(); i < count; i++) {
                        android.view.View child = getChildAt(i);
                        if (child.getVisibility() == android.view.View.VISIBLE) {
                            child.addFocusables(views, direction, focusableMode);
                        }
                    }
                }
            } else {
                android.view.View view = findViewByPosition(mFocusPosition);
                if (view != null) {
                    view.addFocusables(views, direction, focusableMode);
                }
            }
            // if still cannot find any, fall through and add itself
            if (views.size() != focusableCount) {
                return true;
            }
            if (recyclerView.isFocusable()) {
                views.add(recyclerView);
            }
        }
        return true;
    }

    boolean hasCreatedLastItem() {
        int count = getItemCount();
        return (count == 0) || (mBaseGridView.findViewHolderForAdapterPosition(count - 1) != null);
    }

    boolean hasCreatedFirstItem() {
        int count = getItemCount();
        return (count == 0) || (mBaseGridView.findViewHolderForAdapterPosition(0) != null);
    }

    boolean canScrollTo(android.view.View view) {
        return (view.getVisibility() == android.view.View.VISIBLE) && ((!hasFocus()) || view.hasFocusable());
    }

    boolean gridOnRequestFocusInDescendants(android.support.v7.widget.RecyclerView recyclerView, int direction, android.graphics.Rect previouslyFocusedRect) {
        switch (mFocusScrollStrategy) {
            case android.support.v17.leanback.widget.BaseGridView.FOCUS_SCROLL_ALIGNED :
            default :
                return gridOnRequestFocusInDescendantsAligned(recyclerView, direction, previouslyFocusedRect);
            case android.support.v17.leanback.widget.BaseGridView.FOCUS_SCROLL_PAGE :
            case android.support.v17.leanback.widget.BaseGridView.FOCUS_SCROLL_ITEM :
                return gridOnRequestFocusInDescendantsUnaligned(recyclerView, direction, previouslyFocusedRect);
        }
    }

    private boolean gridOnRequestFocusInDescendantsAligned(android.support.v7.widget.RecyclerView recyclerView, int direction, android.graphics.Rect previouslyFocusedRect) {
        android.view.View view = findViewByPosition(mFocusPosition);
        if (view != null) {
            boolean result = view.requestFocus(direction, previouslyFocusedRect);
            if ((!result) && android.support.v17.leanback.widget.GridLayoutManager.DEBUG) {
                android.util.Log.w(getTag(), "failed to request focus on " + view);
            }
            return result;
        }
        return false;
    }

    private boolean gridOnRequestFocusInDescendantsUnaligned(android.support.v7.widget.RecyclerView recyclerView, int direction, android.graphics.Rect previouslyFocusedRect) {
        // focus to view not overlapping padding area to avoid scrolling in gaining focus
        int index;
        int increment;
        int end;
        int count = getChildCount();
        if ((direction & android.view.View.FOCUS_FORWARD) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }
        int left = mWindowAlignment.mainAxis().getPaddingLow();
        int right = mWindowAlignment.mainAxis().getClientSize() + left;
        for (int i = index; i != end; i += increment) {
            android.view.View child = getChildAt(i);
            if (child.getVisibility() == android.view.View.VISIBLE) {
                if ((getViewMin(child) >= left) && (getViewMax(child) <= right)) {
                    if (child.requestFocus(direction, previouslyFocusedRect)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static final int PREV_ITEM = 0;

    private static final int NEXT_ITEM = 1;

    private static final int PREV_ROW = 2;

    private static final int NEXT_ROW = 3;

    private int getMovement(int direction) {
        int movement = android.view.View.FOCUS_LEFT;
        if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
            switch (direction) {
                case android.view.View.FOCUS_LEFT :
                    movement = (!mReverseFlowPrimary) ? android.support.v17.leanback.widget.GridLayoutManager.PREV_ITEM : android.support.v17.leanback.widget.GridLayoutManager.NEXT_ITEM;
                    break;
                case android.view.View.FOCUS_RIGHT :
                    movement = (!mReverseFlowPrimary) ? android.support.v17.leanback.widget.GridLayoutManager.NEXT_ITEM : android.support.v17.leanback.widget.GridLayoutManager.PREV_ITEM;
                    break;
                case android.view.View.FOCUS_UP :
                    movement = android.support.v17.leanback.widget.GridLayoutManager.PREV_ROW;
                    break;
                case android.view.View.FOCUS_DOWN :
                    movement = android.support.v17.leanback.widget.GridLayoutManager.NEXT_ROW;
                    break;
            }
        } else
            if (mOrientation == android.support.v7.widget.RecyclerView.VERTICAL) {
                switch (direction) {
                    case android.view.View.FOCUS_LEFT :
                        movement = (!mReverseFlowSecondary) ? android.support.v17.leanback.widget.GridLayoutManager.PREV_ROW : android.support.v17.leanback.widget.GridLayoutManager.NEXT_ROW;
                        break;
                    case android.view.View.FOCUS_RIGHT :
                        movement = (!mReverseFlowSecondary) ? android.support.v17.leanback.widget.GridLayoutManager.NEXT_ROW : android.support.v17.leanback.widget.GridLayoutManager.PREV_ROW;
                        break;
                    case android.view.View.FOCUS_UP :
                        movement = android.support.v17.leanback.widget.GridLayoutManager.PREV_ITEM;
                        break;
                    case android.view.View.FOCUS_DOWN :
                        movement = android.support.v17.leanback.widget.GridLayoutManager.NEXT_ITEM;
                        break;
                }
            }

        return movement;
    }

    int getChildDrawingOrder(android.support.v7.widget.RecyclerView recyclerView, int childCount, int i) {
        android.view.View view = findViewByPosition(mFocusPosition);
        if (view == null) {
            return i;
        }
        int focusIndex = recyclerView.indexOfChild(view);
        // supposely 0 1 2 3 4 5 6 7 8 9, 4 is the center item
        // drawing order is 0 1 2 3 9 8 7 6 5 4
        if (i < focusIndex) {
            return i;
        } else
            if (i < (childCount - 1)) {
                return ((focusIndex + childCount) - 1) - i;
            } else {
                return focusIndex;
            }

    }

    @java.lang.Override
    public void onAdapterChanged(android.support.v7.widget.RecyclerView.Adapter oldAdapter, android.support.v7.widget.RecyclerView.Adapter newAdapter) {
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), "onAdapterChanged to " + newAdapter);

        if (oldAdapter != null) {
            discardLayoutInfo();
            mFocusPosition = android.support.v7.widget.RecyclerView.NO_POSITION;
            mFocusPositionOffset = 0;
            mChildrenStates.clear();
        }
        if (newAdapter instanceof android.support.v17.leanback.widget.FacetProviderAdapter) {
            mFacetProviderAdapter = ((android.support.v17.leanback.widget.FacetProviderAdapter) (newAdapter));
        } else {
            mFacetProviderAdapter = null;
        }
        super.onAdapterChanged(oldAdapter, newAdapter);
    }

    private void discardLayoutInfo() {
        mGrid = null;
        mRowSizeSecondary = null;
        mRowSecondarySizeRefresh = false;
    }

    public void setLayoutEnabled(boolean layoutEnabled) {
        if (mLayoutEnabled != layoutEnabled) {
            mLayoutEnabled = layoutEnabled;
            requestLayout();
        }
    }

    void setChildrenVisibility(int visibility) {
        mChildVisibility = visibility;
        if (mChildVisibility != (-1)) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                getChildAt(i).setVisibility(mChildVisibility);
            }
        }
    }

    static final class SavedState implements android.os.Parcelable {
        int index;// index inside adapter of the current view


        android.os.Bundle childStates = android.os.Bundle.EMPTY;

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            out.writeInt(index);
            out.writeBundle(childStates);
        }

        @java.lang.SuppressWarnings("hiding")
        public static final android.os.Parcelable.Creator<android.support.v17.leanback.widget.GridLayoutManager.SavedState> CREATOR = new android.os.Parcelable.Creator<android.support.v17.leanback.widget.GridLayoutManager.SavedState>() {
            @java.lang.Override
            public android.support.v17.leanback.widget.GridLayoutManager.SavedState createFromParcel(android.os.Parcel in) {
                return new android.support.v17.leanback.widget.GridLayoutManager.SavedState(in);
            }

            @java.lang.Override
            public android.support.v17.leanback.widget.GridLayoutManager.SavedState[] newArray(int size) {
                return new android.support.v17.leanback.widget.GridLayoutManager.SavedState[size];
            }
        };

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        SavedState(android.os.Parcel in) {
            index = in.readInt();
            childStates = in.readBundle(android.support.v17.leanback.widget.GridLayoutManager.class.getClassLoader());
        }

        SavedState() {
        }
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState() {
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), "onSaveInstanceState getSelection() " + getSelection());

        android.support.v17.leanback.widget.GridLayoutManager.SavedState ss = new android.support.v17.leanback.widget.GridLayoutManager.SavedState();
        // save selected index
        ss.index = getSelection();
        // save offscreen child (state when they are recycled)
        android.os.Bundle bundle = mChildrenStates.saveAsBundle();
        // save views currently is on screen (TODO save cached views)
        for (int i = 0, count = getChildCount(); i < count; i++) {
            android.view.View view = getChildAt(i);
            int position = getPositionByView(view);
            if (position != android.support.v7.widget.RecyclerView.NO_POSITION) {
                bundle = mChildrenStates.saveOnScreenView(bundle, view, position);
            }
        }
        ss.childStates = bundle;
        return ss;
    }

    void onChildRecycled(android.support.v7.widget.RecyclerView.ViewHolder holder) {
        final int position = holder.getAdapterPosition();
        if (position != android.support.v7.widget.RecyclerView.NO_POSITION) {
            mChildrenStates.saveOffscreenView(holder.itemView, position);
        }
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
        if (!(state instanceof android.support.v17.leanback.widget.GridLayoutManager.SavedState)) {
            return;
        }
        android.support.v17.leanback.widget.GridLayoutManager.SavedState loadingState = ((android.support.v17.leanback.widget.GridLayoutManager.SavedState) (state));
        mFocusPosition = loadingState.index;
        mFocusPositionOffset = 0;
        mChildrenStates.loadFromBundle(loadingState.childStates);
        mForceFullLayout = true;
        requestLayout();
        if (android.support.v17.leanback.widget.GridLayoutManager.DEBUG)
            android.util.Log.v(getTag(), "onRestoreInstanceState mFocusPosition " + mFocusPosition);

    }

    @java.lang.Override
    public int getRowCountForAccessibility(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        if ((mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) && (mGrid != null)) {
            return mGrid.getNumRows();
        }
        return super.getRowCountForAccessibility(recycler, state);
    }

    @java.lang.Override
    public int getColumnCountForAccessibility(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        if ((mOrientation == android.support.v7.widget.RecyclerView.VERTICAL) && (mGrid != null)) {
            return mGrid.getNumRows();
        }
        return super.getColumnCountForAccessibility(recycler, state);
    }

    @java.lang.Override
    public void onInitializeAccessibilityNodeInfoForItem(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, android.view.View host, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
        android.view.ViewGroup.LayoutParams lp = host.getLayoutParams();
        if ((mGrid == null) || (!(lp instanceof android.support.v17.leanback.widget.GridLayoutManager.LayoutParams))) {
            super.onInitializeAccessibilityNodeInfoForItem(recycler, state, host, info);
            return;
        }
        android.support.v17.leanback.widget.GridLayoutManager.LayoutParams glp = ((android.support.v17.leanback.widget.GridLayoutManager.LayoutParams) (lp));
        int position = glp.getViewLayoutPosition();
        int rowIndex = mGrid.getRowIndex(position);
        int guessSpanIndex = position / mGrid.getNumRows();
        if (mOrientation == android.support.v7.widget.RecyclerView.HORIZONTAL) {
            info.setCollectionItemInfo(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(rowIndex, 1, guessSpanIndex, 1, false, false));
        } else {
            info.setCollectionItemInfo(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(guessSpanIndex, 1, rowIndex, 1, false, false));
        }
    }

    /* Leanback widget is different than the default implementation because the "scroll" is driven
    by selection change.
     */
    @java.lang.Override
    public boolean performAccessibilityAction(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, int action, android.os.Bundle args) {
        saveContext(recycler, state);
        switch (action) {
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD :
                // try to focus all the way to the last visible item on the same row.
                processSelectionMoves(false, -mState.getItemCount());
                break;
            case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD :
                processSelectionMoves(false, mState.getItemCount());
                break;
        }
        leaveContext();
        return true;
    }

    /* Move mFocusPosition multiple steps on the same row in main direction.
    Stops when moves are all consumed or reach first/last visible item.
    Returning remaining moves.
     */
    int processSelectionMoves(boolean preventScroll, int moves) {
        if (mGrid == null) {
            return moves;
        }
        int focusPosition = mFocusPosition;
        int focusedRow = (focusPosition != android.support.v7.widget.RecyclerView.NO_POSITION) ? mGrid.getRowIndex(focusPosition) : android.support.v7.widget.RecyclerView.NO_POSITION;
        android.view.View newSelected = null;
        for (int i = 0, count = getChildCount(); (i < count) && (moves != 0); i++) {
            int index = (moves > 0) ? i : (count - 1) - i;
            final android.view.View child = getChildAt(index);
            if (!canScrollTo(child)) {
                continue;
            }
            int position = getPositionByIndex(index);
            int rowIndex = mGrid.getRowIndex(position);
            if (focusedRow == android.support.v7.widget.RecyclerView.NO_POSITION) {
                focusPosition = position;
                newSelected = child;
                focusedRow = rowIndex;
            } else
                if (rowIndex == focusedRow) {
                    if (((moves > 0) && (position > focusPosition)) || ((moves < 0) && (position < focusPosition))) {
                        focusPosition = position;
                        newSelected = child;
                        if (moves > 0) {
                            moves--;
                        } else {
                            moves++;
                        }
                    }
                }

        }
        if (newSelected != null) {
            if (preventScroll) {
                if (hasFocus()) {
                    mInSelection = true;
                    newSelected.requestFocus();
                    mInSelection = false;
                }
                mFocusPosition = focusPosition;
                mSubFocusPosition = 0;
            } else {
                scrollToView(newSelected, true);
            }
        }
        return moves;
    }

    @java.lang.Override
    public void onInitializeAccessibilityNodeInfo(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
        saveContext(recycler, state);
        if (mScrollEnabled && (!hasCreatedFirstItem())) {
            info.addAction(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
            info.setScrollable(true);
        }
        if (mScrollEnabled && (!hasCreatedLastItem())) {
            info.addAction(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
            info.setScrollable(true);
        }
        final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionInfoCompat collectionInfo = android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(getRowCountForAccessibility(recycler, state), getColumnCountForAccessibility(recycler, state), isLayoutHierarchical(recycler, state), getSelectionModeForAccessibility(recycler, state));
        info.setCollectionInfo(collectionInfo);
        leaveContext();
    }
}

