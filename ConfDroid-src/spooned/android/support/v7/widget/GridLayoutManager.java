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
 * A {@link RecyclerView.LayoutManager} implementations that lays out items in a grid.
 * <p>
 * By default, each item occupies 1 span. You can change it by providing a custom
 * {@link SpanSizeLookup} instance via {@link #setSpanSizeLookup(SpanSizeLookup)}.
 */
public class GridLayoutManager extends android.support.v7.widget.LinearLayoutManager {
    private static final boolean DEBUG = false;

    private static final java.lang.String TAG = "GridLayoutManager";

    public static final int DEFAULT_SPAN_COUNT = -1;

    /**
     * Span size have been changed but we've not done a new layout calculation.
     */
    boolean mPendingSpanCountChange = false;

    int mSpanCount = android.support.v7.widget.GridLayoutManager.DEFAULT_SPAN_COUNT;

    /**
     * Right borders for each span.
     * <p>For <b>i-th</b> item start is {@link #mCachedBorders}[i-1] + 1
     * and end is {@link #mCachedBorders}[i].
     */
    int[] mCachedBorders;

    /**
     * Temporary array to keep views in layoutChunk method
     */
    android.view.View[] mSet;

    final android.util.SparseIntArray mPreLayoutSpanSizeCache = new android.util.SparseIntArray();

    final android.util.SparseIntArray mPreLayoutSpanIndexCache = new android.util.SparseIntArray();

    android.support.v7.widget.GridLayoutManager.SpanSizeLookup mSpanSizeLookup = new android.support.v7.widget.GridLayoutManager.DefaultSpanSizeLookup();

    // re-used variable to acquire decor insets from RecyclerView
    final android.graphics.Rect mDecorInsets = new android.graphics.Rect();

    /**
     * Constructor used when layout manager is set in XML by RecyclerView attribute
     * "layoutManager". If spanCount is not specified in the XML, it defaults to a
     * single column.
     *
     * @unknown ref android.support.v7.recyclerview.R.styleable#RecyclerView_spanCount
     */
    public GridLayoutManager(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        android.support.v7.widget.RecyclerView.LayoutManager.Properties properties = android.support.v7.widget.RecyclerView.LayoutManager.getProperties(context, attrs, defStyleAttr, defStyleRes);
        setSpanCount(properties.spanCount);
    }

    /**
     * Creates a vertical GridLayoutManager
     *
     * @param context
     * 		Current context, will be used to access resources.
     * @param spanCount
     * 		The number of columns in the grid
     */
    public GridLayoutManager(android.content.Context context, int spanCount) {
        super(context);
        setSpanCount(spanCount);
    }

    /**
     *
     *
     * @param context
     * 		Current context, will be used to access resources.
     * @param spanCount
     * 		The number of columns or rows in the grid
     * @param orientation
     * 		Layout orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     * @param reverseLayout
     * 		When set to true, layouts from end to start.
     */
    public GridLayoutManager(android.content.Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        setSpanCount(spanCount);
    }

    /**
     * stackFromEnd is not supported by GridLayoutManager. Consider using
     * {@link #setReverseLayout(boolean)}.
     */
    @java.lang.Override
    public void setStackFromEnd(boolean stackFromEnd) {
        if (stackFromEnd) {
            throw new java.lang.UnsupportedOperationException("GridLayoutManager does not support stack from end." + " Consider using reverse layout");
        }
        super.setStackFromEnd(false);
    }

    @java.lang.Override
    public int getRowCountForAccessibility(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        if (mOrientation == android.support.v7.widget.LinearLayoutManager.HORIZONTAL) {
            return mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        // Row count is one more than the last item's row index.
        return getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }

    @java.lang.Override
    public int getColumnCountForAccessibility(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        if (mOrientation == android.support.v7.widget.LinearLayoutManager.VERTICAL) {
            return mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        // Column count is one more than the last item's column index.
        return getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }

    @java.lang.Override
    public void onInitializeAccessibilityNodeInfoForItem(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, android.view.View host, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
        android.view.ViewGroup.LayoutParams lp = host.getLayoutParams();
        if (!(lp instanceof android.support.v7.widget.GridLayoutManager.LayoutParams)) {
            super.onInitializeAccessibilityNodeInfoForItem(host, info);
            return;
        }
        android.support.v7.widget.GridLayoutManager.LayoutParams glp = ((android.support.v7.widget.GridLayoutManager.LayoutParams) (lp));
        int spanGroupIndex = getSpanGroupIndex(recycler, state, glp.getViewLayoutPosition());
        if (mOrientation == android.support.v7.widget.LinearLayoutManager.HORIZONTAL) {
            info.setCollectionItemInfo(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(glp.getSpanIndex(), glp.getSpanSize(), spanGroupIndex, 1, (mSpanCount > 1) && (glp.getSpanSize() == mSpanCount), false));
        } else {
            // VERTICAL
            info.setCollectionItemInfo(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(spanGroupIndex, 1, glp.getSpanIndex(), glp.getSpanSize(), (mSpanCount > 1) && (glp.getSpanSize() == mSpanCount), false));
        }
    }

    @java.lang.Override
    public void onLayoutChildren(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        if (state.isPreLayout()) {
            cachePreLayoutSpanMapping();
        }
        super.onLayoutChildren(recycler, state);
        if (android.support.v7.widget.GridLayoutManager.DEBUG) {
            validateChildOrder();
        }
        clearPreLayoutSpanMappingCache();
    }

    @java.lang.Override
    public void onLayoutCompleted(android.support.v7.widget.RecyclerView.State state) {
        super.onLayoutCompleted(state);
        mPendingSpanCountChange = false;
    }

    private void clearPreLayoutSpanMappingCache() {
        mPreLayoutSpanSizeCache.clear();
        mPreLayoutSpanIndexCache.clear();
    }

    private void cachePreLayoutSpanMapping() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.support.v7.widget.GridLayoutManager.LayoutParams lp = ((android.support.v7.widget.GridLayoutManager.LayoutParams) (getChildAt(i).getLayoutParams()));
            final int viewPosition = lp.getViewLayoutPosition();
            mPreLayoutSpanSizeCache.put(viewPosition, lp.getSpanSize());
            mPreLayoutSpanIndexCache.put(viewPosition, lp.getSpanIndex());
        }
    }

    @java.lang.Override
    public void onItemsAdded(android.support.v7.widget.RecyclerView recyclerView, int positionStart, int itemCount) {
        mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @java.lang.Override
    public void onItemsChanged(android.support.v7.widget.RecyclerView recyclerView) {
        mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @java.lang.Override
    public void onItemsRemoved(android.support.v7.widget.RecyclerView recyclerView, int positionStart, int itemCount) {
        mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @java.lang.Override
    public void onItemsUpdated(android.support.v7.widget.RecyclerView recyclerView, int positionStart, int itemCount, java.lang.Object payload) {
        mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @java.lang.Override
    public void onItemsMoved(android.support.v7.widget.RecyclerView recyclerView, int from, int to, int itemCount) {
        mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @java.lang.Override
    public android.support.v7.widget.RecyclerView.LayoutParams generateDefaultLayoutParams() {
        if (mOrientation == android.support.v7.widget.LinearLayoutManager.HORIZONTAL) {
            return new android.support.v7.widget.GridLayoutManager.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            return new android.support.v7.widget.GridLayoutManager.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @java.lang.Override
    public android.support.v7.widget.RecyclerView.LayoutParams generateLayoutParams(android.content.Context c, android.util.AttributeSet attrs) {
        return new android.support.v7.widget.GridLayoutManager.LayoutParams(c, attrs);
    }

    @java.lang.Override
    public android.support.v7.widget.RecyclerView.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams lp) {
        if (lp instanceof android.view.ViewGroup.MarginLayoutParams) {
            return new android.support.v7.widget.GridLayoutManager.LayoutParams(((android.view.ViewGroup.MarginLayoutParams) (lp)));
        } else {
            return new android.support.v7.widget.GridLayoutManager.LayoutParams(lp);
        }
    }

    @java.lang.Override
    public boolean checkLayoutParams(android.support.v7.widget.RecyclerView.LayoutParams lp) {
        return lp instanceof android.support.v7.widget.GridLayoutManager.LayoutParams;
    }

    /**
     * Sets the source to get the number of spans occupied by each item in the adapter.
     *
     * @param spanSizeLookup
     * 		{@link SpanSizeLookup} instance to be used to query number of spans
     * 		occupied by each item
     */
    public void setSpanSizeLookup(android.support.v7.widget.GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        mSpanSizeLookup = spanSizeLookup;
    }

    /**
     * Returns the current {@link SpanSizeLookup} used by the GridLayoutManager.
     *
     * @return The current {@link SpanSizeLookup} used by the GridLayoutManager.
     */
    public android.support.v7.widget.GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
        return mSpanSizeLookup;
    }

    private void updateMeasurements() {
        int totalSpace;
        if (getOrientation() == android.support.v7.widget.LinearLayoutManager.VERTICAL) {
            totalSpace = (getWidth() - getPaddingRight()) - getPaddingLeft();
        } else {
            totalSpace = (getHeight() - getPaddingBottom()) - getPaddingTop();
        }
        calculateItemBorders(totalSpace);
    }

    @java.lang.Override
    public void setMeasuredDimension(android.graphics.Rect childrenBounds, int wSpec, int hSpec) {
        if (mCachedBorders == null) {
            super.setMeasuredDimension(childrenBounds, wSpec, hSpec);
        }
        final int width;
        final int height;
        final int horizontalPadding = getPaddingLeft() + getPaddingRight();
        final int verticalPadding = getPaddingTop() + getPaddingBottom();
        if (mOrientation == android.support.v7.widget.LinearLayoutManager.VERTICAL) {
            final int usedHeight = childrenBounds.height() + verticalPadding;
            height = android.support.v7.widget.RecyclerView.LayoutManager.chooseSize(hSpec, usedHeight, getMinimumHeight());
            width = android.support.v7.widget.RecyclerView.LayoutManager.chooseSize(wSpec, mCachedBorders[mCachedBorders.length - 1] + horizontalPadding, getMinimumWidth());
        } else {
            final int usedWidth = childrenBounds.width() + horizontalPadding;
            width = android.support.v7.widget.RecyclerView.LayoutManager.chooseSize(wSpec, usedWidth, getMinimumWidth());
            height = android.support.v7.widget.RecyclerView.LayoutManager.chooseSize(hSpec, mCachedBorders[mCachedBorders.length - 1] + verticalPadding, getMinimumHeight());
        }
        setMeasuredDimension(width, height);
    }

    /**
     *
     *
     * @param totalSpace
     * 		Total available space after padding is removed
     */
    private void calculateItemBorders(int totalSpace) {
        mCachedBorders = android.support.v7.widget.GridLayoutManager.calculateItemBorders(mCachedBorders, mSpanCount, totalSpace);
    }

    /**
     *
     *
     * @param cachedBorders
     * 		The out array
     * @param spanCount
     * 		number of spans
     * @param totalSpace
     * 		total available space after padding is removed
     * @return The updated array. Might be the same instance as the provided array if its size
    has not changed.
     */
    static int[] calculateItemBorders(int[] cachedBorders, int spanCount, int totalSpace) {
        if (((cachedBorders == null) || (cachedBorders.length != (spanCount + 1))) || (cachedBorders[cachedBorders.length - 1] != totalSpace)) {
            cachedBorders = new int[spanCount + 1];
        }
        cachedBorders[0] = 0;
        int sizePerSpan = totalSpace / spanCount;
        int sizePerSpanRemainder = totalSpace % spanCount;
        int consumedPixels = 0;
        int additionalSize = 0;
        for (int i = 1; i <= spanCount; i++) {
            int itemSize = sizePerSpan;
            additionalSize += sizePerSpanRemainder;
            if ((additionalSize > 0) && ((spanCount - additionalSize) < sizePerSpanRemainder)) {
                itemSize += 1;
                additionalSize -= spanCount;
            }
            consumedPixels += itemSize;
            cachedBorders[i] = consumedPixels;
        }
        return cachedBorders;
    }

    int getSpaceForSpanRange(int startSpan, int spanSize) {
        if ((mOrientation == android.support.v7.widget.LinearLayoutManager.VERTICAL) && isLayoutRTL()) {
            return mCachedBorders[mSpanCount - startSpan] - mCachedBorders[(mSpanCount - startSpan) - spanSize];
        } else {
            return mCachedBorders[startSpan + spanSize] - mCachedBorders[startSpan];
        }
    }

    @java.lang.Override
    void onAnchorReady(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, android.support.v7.widget.LinearLayoutManager.AnchorInfo anchorInfo, int itemDirection) {
        super.onAnchorReady(recycler, state, anchorInfo, itemDirection);
        updateMeasurements();
        if ((state.getItemCount() > 0) && (!state.isPreLayout())) {
            ensureAnchorIsInCorrectSpan(recycler, state, anchorInfo, itemDirection);
        }
        ensureViewSet();
    }

    private void ensureViewSet() {
        if ((mSet == null) || (mSet.length != mSpanCount)) {
            mSet = new android.view.View[mSpanCount];
        }
    }

    @java.lang.Override
    public int scrollHorizontallyBy(int dx, android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        updateMeasurements();
        ensureViewSet();
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    @java.lang.Override
    public int scrollVerticallyBy(int dy, android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        updateMeasurements();
        ensureViewSet();
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    private void ensureAnchorIsInCorrectSpan(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, android.support.v7.widget.LinearLayoutManager.AnchorInfo anchorInfo, int itemDirection) {
        final boolean layingOutInPrimaryDirection = itemDirection == android.support.v7.widget.LinearLayoutManager.LayoutState.ITEM_DIRECTION_TAIL;
        int span = getSpanIndex(recycler, state, anchorInfo.mPosition);
        if (layingOutInPrimaryDirection) {
            // choose span 0
            while ((span > 0) && (anchorInfo.mPosition > 0)) {
                anchorInfo.mPosition--;
                span = getSpanIndex(recycler, state, anchorInfo.mPosition);
            } 
        } else {
            // choose the max span we can get. hopefully last one
            final int indexLimit = state.getItemCount() - 1;
            int pos = anchorInfo.mPosition;
            int bestSpan = span;
            while (pos < indexLimit) {
                int next = getSpanIndex(recycler, state, pos + 1);
                if (next > bestSpan) {
                    pos += 1;
                    bestSpan = next;
                } else {
                    break;
                }
            } 
            anchorInfo.mPosition = pos;
        }
    }

    @java.lang.Override
    android.view.View findReferenceChild(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, int start, int end, int itemCount) {
        ensureLayoutState();
        android.view.View invalidMatch = null;
        android.view.View outOfBoundsMatch = null;
        final int boundsStart = mOrientationHelper.getStartAfterPadding();
        final int boundsEnd = mOrientationHelper.getEndAfterPadding();
        final int diff = (end > start) ? 1 : -1;
        for (int i = start; i != end; i += diff) {
            final android.view.View view = getChildAt(i);
            final int position = getPosition(view);
            if ((position >= 0) && (position < itemCount)) {
                final int span = getSpanIndex(recycler, state, position);
                if (span != 0) {
                    continue;
                }
                if (((android.support.v7.widget.RecyclerView.LayoutParams) (view.getLayoutParams())).isItemRemoved()) {
                    if (invalidMatch == null) {
                        invalidMatch = view;// removed item, least preferred

                    }
                } else
                    if ((mOrientationHelper.getDecoratedStart(view) >= boundsEnd) || (mOrientationHelper.getDecoratedEnd(view) < boundsStart)) {
                        if (outOfBoundsMatch == null) {
                            outOfBoundsMatch = view;// item is not visible, less preferred

                        }
                    } else {
                        return view;
                    }

            }
        }
        return outOfBoundsMatch != null ? outOfBoundsMatch : invalidMatch;
    }

    private int getSpanGroupIndex(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, int viewPosition) {
        if (!state.isPreLayout()) {
            return mSpanSizeLookup.getSpanGroupIndex(viewPosition, mSpanCount);
        }
        final int adapterPosition = recycler.convertPreLayoutPositionToPostLayout(viewPosition);
        if (adapterPosition == (-1)) {
            if (android.support.v7.widget.GridLayoutManager.DEBUG) {
                throw new java.lang.RuntimeException("Cannot find span group index for position " + viewPosition);
            }
            android.util.Log.w(android.support.v7.widget.GridLayoutManager.TAG, "Cannot find span size for pre layout position. " + viewPosition);
            return 0;
        }
        return mSpanSizeLookup.getSpanGroupIndex(adapterPosition, mSpanCount);
    }

    private int getSpanIndex(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, int pos) {
        if (!state.isPreLayout()) {
            return mSpanSizeLookup.getCachedSpanIndex(pos, mSpanCount);
        }
        final int cached = mPreLayoutSpanIndexCache.get(pos, -1);
        if (cached != (-1)) {
            return cached;
        }
        final int adapterPosition = recycler.convertPreLayoutPositionToPostLayout(pos);
        if (adapterPosition == (-1)) {
            if (android.support.v7.widget.GridLayoutManager.DEBUG) {
                throw new java.lang.RuntimeException(("Cannot find span index for pre layout position. It is" + " not cached, not in the adapter. Pos:") + pos);
            }
            android.util.Log.w(android.support.v7.widget.GridLayoutManager.TAG, ("Cannot find span size for pre layout position. It is" + " not cached, not in the adapter. Pos:") + pos);
            return 0;
        }
        return mSpanSizeLookup.getCachedSpanIndex(adapterPosition, mSpanCount);
    }

    private int getSpanSize(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, int pos) {
        if (!state.isPreLayout()) {
            return mSpanSizeLookup.getSpanSize(pos);
        }
        final int cached = mPreLayoutSpanSizeCache.get(pos, -1);
        if (cached != (-1)) {
            return cached;
        }
        final int adapterPosition = recycler.convertPreLayoutPositionToPostLayout(pos);
        if (adapterPosition == (-1)) {
            if (android.support.v7.widget.GridLayoutManager.DEBUG) {
                throw new java.lang.RuntimeException(("Cannot find span size for pre layout position. It is" + " not cached, not in the adapter. Pos:") + pos);
            }
            android.util.Log.w(android.support.v7.widget.GridLayoutManager.TAG, ("Cannot find span size for pre layout position. It is" + " not cached, not in the adapter. Pos:") + pos);
            return 1;
        }
        return mSpanSizeLookup.getSpanSize(adapterPosition);
    }

    @java.lang.Override
    int getItemPrefetchCount() {
        return mSpanCount;
    }

    @java.lang.Override
    int gatherPrefetchIndicesForLayoutState(android.support.v7.widget.RecyclerView.State state, android.support.v7.widget.LinearLayoutManager.LayoutState layoutState, int[] outIndices) {
        int remainingSpan = mSpanCount;
        int count = 0;
        while (((count < mSpanCount) && layoutState.hasMore(state)) && (remainingSpan > 0)) {
            final int pos = layoutState.mCurrentPosition;
            outIndices[count] = pos;
            final int spanSize = mSpanSizeLookup.getSpanSize(pos);
            remainingSpan -= spanSize;
            layoutState.mCurrentPosition += layoutState.mItemDirection;
            count++;
        } 
        return count;
    }

    @java.lang.Override
    void layoutChunk(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, android.support.v7.widget.LinearLayoutManager.LayoutState layoutState, android.support.v7.widget.LinearLayoutManager.LayoutChunkResult result) {
        final int otherDirSpecMode = mOrientationHelper.getModeInOther();
        final boolean flexibleInOtherDir = otherDirSpecMode != android.view.View.MeasureSpec.EXACTLY;
        final int currentOtherDirSize = (getChildCount() > 0) ? mCachedBorders[mSpanCount] : 0;
        // if grid layout's dimensions are not specified, let the new row change the measurements
        // This is not perfect since we not covering all rows but still solves an important case
        // where they may have a header row which should be laid out according to children.
        if (flexibleInOtherDir) {
            updateMeasurements();// reset measurements

        }
        final boolean layingOutInPrimaryDirection = layoutState.mItemDirection == android.support.v7.widget.LinearLayoutManager.LayoutState.ITEM_DIRECTION_TAIL;
        int count = 0;
        int consumedSpanCount = 0;
        int remainingSpan = mSpanCount;
        if (!layingOutInPrimaryDirection) {
            int itemSpanIndex = getSpanIndex(recycler, state, layoutState.mCurrentPosition);
            int itemSpanSize = getSpanSize(recycler, state, layoutState.mCurrentPosition);
            remainingSpan = itemSpanIndex + itemSpanSize;
        }
        while (((count < mSpanCount) && layoutState.hasMore(state)) && (remainingSpan > 0)) {
            int pos = layoutState.mCurrentPosition;
            final int spanSize = getSpanSize(recycler, state, pos);
            if (spanSize > mSpanCount) {
                throw new java.lang.IllegalArgumentException(((((("Item at position " + pos) + " requires ") + spanSize) + " spans but GridLayoutManager has only ") + mSpanCount) + " spans.");
            }
            remainingSpan -= spanSize;
            if (remainingSpan < 0) {
                break;// item did not fit into this row or column

            }
            android.view.View view = layoutState.next(recycler);
            if (view == null) {
                break;
            }
            consumedSpanCount += spanSize;
            mSet[count] = view;
            count++;
        } 
        if (count == 0) {
            result.mFinished = true;
            return;
        }
        int maxSize = 0;
        float maxSizeInOther = 0;// use a float to get size per span

        // we should assign spans before item decor offsets are calculated
        assignSpans(recycler, state, count, consumedSpanCount, layingOutInPrimaryDirection);
        for (int i = 0; i < count; i++) {
            android.view.View view = mSet[i];
            if (layoutState.mScrapList == null) {
                if (layingOutInPrimaryDirection) {
                    addView(view);
                } else {
                    addView(view, 0);
                }
            } else {
                if (layingOutInPrimaryDirection) {
                    addDisappearingView(view);
                } else {
                    addDisappearingView(view, 0);
                }
            }
            calculateItemDecorationsForChild(view, mDecorInsets);
            measureChild(view, otherDirSpecMode, false);
            final int size = mOrientationHelper.getDecoratedMeasurement(view);
            if (size > maxSize) {
                maxSize = size;
            }
            final android.support.v7.widget.GridLayoutManager.LayoutParams lp = ((android.support.v7.widget.GridLayoutManager.LayoutParams) (view.getLayoutParams()));
            final float otherSize = (1.0F * mOrientationHelper.getDecoratedMeasurementInOther(view)) / lp.mSpanSize;
            if (otherSize > maxSizeInOther) {
                maxSizeInOther = otherSize;
            }
        }
        if (flexibleInOtherDir) {
            // re-distribute columns
            guessMeasurement(maxSizeInOther, currentOtherDirSize);
            // now we should re-measure any item that was match parent.
            maxSize = 0;
            for (int i = 0; i < count; i++) {
                android.view.View view = mSet[i];
                measureChild(view, android.view.View.MeasureSpec.EXACTLY, true);
                final int size = mOrientationHelper.getDecoratedMeasurement(view);
                if (size > maxSize) {
                    maxSize = size;
                }
            }
        }
        // Views that did not measure the maxSize has to be re-measured
        // We will stop doing this once we introduce Gravity in the GLM layout params
        for (int i = 0; i < count; i++) {
            final android.view.View view = mSet[i];
            if (mOrientationHelper.getDecoratedMeasurement(view) != maxSize) {
                final android.support.v7.widget.GridLayoutManager.LayoutParams lp = ((android.support.v7.widget.GridLayoutManager.LayoutParams) (view.getLayoutParams()));
                final android.graphics.Rect decorInsets = lp.mDecorInsets;
                final int verticalInsets = ((decorInsets.top + decorInsets.bottom) + lp.topMargin) + lp.bottomMargin;
                final int horizontalInsets = ((decorInsets.left + decorInsets.right) + lp.leftMargin) + lp.rightMargin;
                final int totalSpaceInOther = getSpaceForSpanRange(lp.mSpanIndex, lp.mSpanSize);
                final int wSpec;
                final int hSpec;
                if (mOrientation == android.support.v7.widget.LinearLayoutManager.VERTICAL) {
                    wSpec = android.support.v7.widget.RecyclerView.LayoutManager.getChildMeasureSpec(totalSpaceInOther, android.view.View.MeasureSpec.EXACTLY, horizontalInsets, lp.width, false);
                    hSpec = android.view.View.MeasureSpec.makeMeasureSpec(maxSize - verticalInsets, android.view.View.MeasureSpec.EXACTLY);
                } else {
                    wSpec = android.view.View.MeasureSpec.makeMeasureSpec(maxSize - horizontalInsets, android.view.View.MeasureSpec.EXACTLY);
                    hSpec = android.support.v7.widget.RecyclerView.LayoutManager.getChildMeasureSpec(totalSpaceInOther, android.view.View.MeasureSpec.EXACTLY, verticalInsets, lp.height, false);
                }
                measureChildWithDecorationsAndMargin(view, wSpec, hSpec, true);
            }
        }
        result.mConsumed = maxSize;
        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;
        if (mOrientation == android.support.v7.widget.LinearLayoutManager.VERTICAL) {
            if (layoutState.mLayoutDirection == android.support.v7.widget.LinearLayoutManager.LayoutState.LAYOUT_START) {
                bottom = layoutState.mOffset;
                top = bottom - maxSize;
            } else {
                top = layoutState.mOffset;
                bottom = top + maxSize;
            }
        } else {
            if (layoutState.mLayoutDirection == android.support.v7.widget.LinearLayoutManager.LayoutState.LAYOUT_START) {
                right = layoutState.mOffset;
                left = right - maxSize;
            } else {
                left = layoutState.mOffset;
                right = left + maxSize;
            }
        }
        for (int i = 0; i < count; i++) {
            android.view.View view = mSet[i];
            android.support.v7.widget.GridLayoutManager.LayoutParams params = ((android.support.v7.widget.GridLayoutManager.LayoutParams) (view.getLayoutParams()));
            if (mOrientation == android.support.v7.widget.LinearLayoutManager.VERTICAL) {
                if (isLayoutRTL()) {
                    right = getPaddingLeft() + mCachedBorders[mSpanCount - params.mSpanIndex];
                    left = right - mOrientationHelper.getDecoratedMeasurementInOther(view);
                } else {
                    left = getPaddingLeft() + mCachedBorders[params.mSpanIndex];
                    right = left + mOrientationHelper.getDecoratedMeasurementInOther(view);
                }
            } else {
                top = getPaddingTop() + mCachedBorders[params.mSpanIndex];
                bottom = top + mOrientationHelper.getDecoratedMeasurementInOther(view);
            }
            // We calculate everything with View's bounding box (which includes decor and margins)
            // To calculate correct layout position, we subtract margins.
            layoutDecoratedWithMargins(view, left, top, right, bottom);
            if (android.support.v7.widget.GridLayoutManager.DEBUG) {
                android.util.Log.d(android.support.v7.widget.GridLayoutManager.TAG, (((((((((((("laid out child at position " + getPosition(view)) + ", with l:") + (left + params.leftMargin)) + ", t:") + (top + params.topMargin)) + ", r:") + (right - params.rightMargin)) + ", b:") + (bottom - params.bottomMargin)) + ", span:") + params.mSpanIndex) + ", spanSize:") + params.mSpanSize);
            }
            // Consume the available space if the view is not removed OR changed
            if (params.isItemRemoved() || params.isItemChanged()) {
                result.mIgnoreConsumed = true;
            }
            result.mFocusable |= view.isFocusable();
        }
        java.util.Arrays.fill(mSet, null);
    }

    /**
     * Measures a child with currently known information. This is not necessarily the child's final
     * measurement. (see fillChunk for details).
     *
     * @param view
     * 		The child view to be measured
     * @param otherDirParentSpecMode
     * 		The RV measure spec that should be used in the secondary
     * 		orientation
     * @param alreadyMeasured
     * 		True if we've already measured this view once
     */
    private void measureChild(android.view.View view, int otherDirParentSpecMode, boolean alreadyMeasured) {
        final android.support.v7.widget.GridLayoutManager.LayoutParams lp = ((android.support.v7.widget.GridLayoutManager.LayoutParams) (view.getLayoutParams()));
        final android.graphics.Rect decorInsets = lp.mDecorInsets;
        final int verticalInsets = ((decorInsets.top + decorInsets.bottom) + lp.topMargin) + lp.bottomMargin;
        final int horizontalInsets = ((decorInsets.left + decorInsets.right) + lp.leftMargin) + lp.rightMargin;
        final int availableSpaceInOther = getSpaceForSpanRange(lp.mSpanIndex, lp.mSpanSize);
        final int wSpec;
        final int hSpec;
        if (mOrientation == android.support.v7.widget.LinearLayoutManager.VERTICAL) {
            wSpec = android.support.v7.widget.RecyclerView.LayoutManager.getChildMeasureSpec(availableSpaceInOther, otherDirParentSpecMode, horizontalInsets, lp.width, false);
            hSpec = android.support.v7.widget.RecyclerView.LayoutManager.getChildMeasureSpec(mOrientationHelper.getTotalSpace(), getHeightMode(), verticalInsets, lp.height, true);
        } else {
            hSpec = android.support.v7.widget.RecyclerView.LayoutManager.getChildMeasureSpec(availableSpaceInOther, otherDirParentSpecMode, verticalInsets, lp.height, false);
            wSpec = android.support.v7.widget.RecyclerView.LayoutManager.getChildMeasureSpec(mOrientationHelper.getTotalSpace(), getWidthMode(), horizontalInsets, lp.width, true);
        }
        measureChildWithDecorationsAndMargin(view, wSpec, hSpec, alreadyMeasured);
    }

    /**
     * This is called after laying out a row (if vertical) or a column (if horizontal) when the
     * RecyclerView does not have exact measurement specs.
     * <p>
     * Here we try to assign a best guess width or height and re-do the layout to update other
     * views that wanted to MATCH_PARENT in the non-scroll orientation.
     *
     * @param maxSizeInOther
     * 		The maximum size per span ratio from the measurement of the children.
     * @param currentOtherDirSize
     * 		The size before this layout chunk. There is no reason to go below.
     */
    private void guessMeasurement(float maxSizeInOther, int currentOtherDirSize) {
        final int contentSize = java.lang.Math.round(maxSizeInOther * mSpanCount);
        // always re-calculate because borders were stretched during the fill
        calculateItemBorders(java.lang.Math.max(contentSize, currentOtherDirSize));
    }

    private void measureChildWithDecorationsAndMargin(android.view.View child, int widthSpec, int heightSpec, boolean alreadyMeasured) {
        android.support.v7.widget.RecyclerView.LayoutParams lp = ((android.support.v7.widget.RecyclerView.LayoutParams) (child.getLayoutParams()));
        final boolean measure;
        if (alreadyMeasured) {
            measure = shouldReMeasureChild(child, widthSpec, heightSpec, lp);
        } else {
            measure = shouldMeasureChild(child, widthSpec, heightSpec, lp);
        }
        if (measure) {
            child.measure(widthSpec, heightSpec);
        }
    }

    private void assignSpans(android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state, int count, int consumedSpanCount, boolean layingOutInPrimaryDirection) {
        // spans are always assigned from 0 to N no matter if it is RTL or not.
        // RTL is used only when positioning the view.
        int span;
        int start;
        int end;
        int diff;
        // make sure we traverse from min position to max position
        if (layingOutInPrimaryDirection) {
            start = 0;
            end = count;
            diff = 1;
        } else {
            start = count - 1;
            end = -1;
            diff = -1;
        }
        span = 0;
        for (int i = start; i != end; i += diff) {
            android.view.View view = mSet[i];
            android.support.v7.widget.GridLayoutManager.LayoutParams params = ((android.support.v7.widget.GridLayoutManager.LayoutParams) (view.getLayoutParams()));
            params.mSpanSize = getSpanSize(recycler, state, getPosition(view));
            params.mSpanIndex = span;
            span += params.mSpanSize;
        }
    }

    /**
     * Returns the number of spans laid out by this grid.
     *
     * @return The number of spans
     * @see #setSpanCount(int)
     */
    public int getSpanCount() {
        return mSpanCount;
    }

    /**
     * Sets the number of spans to be laid out.
     * <p>
     * If {@link #getOrientation()} is {@link #VERTICAL}, this is the number of columns.
     * If {@link #getOrientation()} is {@link #HORIZONTAL}, this is the number of rows.
     *
     * @param spanCount
     * 		The total number of spans in the grid
     * @see #getSpanCount()
     */
    public void setSpanCount(int spanCount) {
        if (spanCount == mSpanCount) {
            return;
        }
        mPendingSpanCountChange = true;
        if (spanCount < 1) {
            throw new java.lang.IllegalArgumentException("Span count should be at least 1. Provided " + spanCount);
        }
        mSpanCount = spanCount;
        mSpanSizeLookup.invalidateSpanIndexCache();
        requestLayout();
    }

    /**
     * A helper class to provide the number of spans each item occupies.
     * <p>
     * Default implementation sets each item to occupy exactly 1 span.
     *
     * @see GridLayoutManager#setSpanSizeLookup(SpanSizeLookup)
     */
    public static abstract class SpanSizeLookup {
        final android.util.SparseIntArray mSpanIndexCache = new android.util.SparseIntArray();

        private boolean mCacheSpanIndices = false;

        /**
         * Returns the number of span occupied by the item at <code>position</code>.
         *
         * @param position
         * 		The adapter position of the item
         * @return The number of spans occupied by the item at the provided position
         */
        public abstract int getSpanSize(int position);

        /**
         * Sets whether the results of {@link #getSpanIndex(int, int)} method should be cached or
         * not. By default these values are not cached. If you are not overriding
         * {@link #getSpanIndex(int, int)}, you should set this to true for better performance.
         *
         * @param cacheSpanIndices
         * 		Whether results of getSpanIndex should be cached or not.
         */
        public void setSpanIndexCacheEnabled(boolean cacheSpanIndices) {
            mCacheSpanIndices = cacheSpanIndices;
        }

        /**
         * Clears the span index cache. GridLayoutManager automatically calls this method when
         * adapter changes occur.
         */
        public void invalidateSpanIndexCache() {
            mSpanIndexCache.clear();
        }

        /**
         * Returns whether results of {@link #getSpanIndex(int, int)} method are cached or not.
         *
         * @return True if results of {@link #getSpanIndex(int, int)} are cached.
         */
        public boolean isSpanIndexCacheEnabled() {
            return mCacheSpanIndices;
        }

        int getCachedSpanIndex(int position, int spanCount) {
            if (!mCacheSpanIndices) {
                return getSpanIndex(position, spanCount);
            }
            final int existing = mSpanIndexCache.get(position, -1);
            if (existing != (-1)) {
                return existing;
            }
            final int value = getSpanIndex(position, spanCount);
            mSpanIndexCache.put(position, value);
            return value;
        }

        /**
         * Returns the final span index of the provided position.
         * <p>
         * If you have a faster way to calculate span index for your items, you should override
         * this method. Otherwise, you should enable span index cache
         * ({@link #setSpanIndexCacheEnabled(boolean)}) for better performance. When caching is
         * disabled, default implementation traverses all items from 0 to
         * <code>position</code>. When caching is enabled, it calculates from the closest cached
         * value before the <code>position</code>.
         * <p>
         * If you override this method, you need to make sure it is consistent with
         * {@link #getSpanSize(int)}. GridLayoutManager does not call this method for
         * each item. It is called only for the reference item and rest of the items
         * are assigned to spans based on the reference item. For example, you cannot assign a
         * position to span 2 while span 1 is empty.
         * <p>
         * Note that span offsets always start with 0 and are not affected by RTL.
         *
         * @param position
         * 		The position of the item
         * @param spanCount
         * 		The total number of spans in the grid
         * @return The final span position of the item. Should be between 0 (inclusive) and
        <code>spanCount</code>(exclusive)
         */
        public int getSpanIndex(int position, int spanCount) {
            int positionSpanSize = getSpanSize(position);
            if (positionSpanSize == spanCount) {
                return 0;// quick return for full-span items

            }
            int span = 0;
            int startPos = 0;
            // If caching is enabled, try to jump
            if (mCacheSpanIndices && (mSpanIndexCache.size() > 0)) {
                int prevKey = findReferenceIndexFromCache(position);
                if (prevKey >= 0) {
                    span = mSpanIndexCache.get(prevKey) + getSpanSize(prevKey);
                    startPos = prevKey + 1;
                }
            }
            for (int i = startPos; i < position; i++) {
                int size = getSpanSize(i);
                span += size;
                if (span == spanCount) {
                    span = 0;
                } else
                    if (span > spanCount) {
                        // did not fit, moving to next row / column
                        span = size;
                    }

            }
            if ((span + positionSpanSize) <= spanCount) {
                return span;
            }
            return 0;
        }

        int findReferenceIndexFromCache(int position) {
            int lo = 0;
            int hi = mSpanIndexCache.size() - 1;
            while (lo <= hi) {
                final int mid = (lo + hi) >>> 1;
                final int midVal = mSpanIndexCache.keyAt(mid);
                if (midVal < position) {
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            } 
            int index = lo - 1;
            if ((index >= 0) && (index < mSpanIndexCache.size())) {
                return mSpanIndexCache.keyAt(index);
            }
            return -1;
        }

        /**
         * Returns the index of the group this position belongs.
         * <p>
         * For example, if grid has 3 columns and each item occupies 1 span, span group index
         * for item 1 will be 0, item 5 will be 1.
         *
         * @param adapterPosition
         * 		The position in adapter
         * @param spanCount
         * 		The total number of spans in the grid
         * @return The index of the span group including the item at the given adapter position
         */
        public int getSpanGroupIndex(int adapterPosition, int spanCount) {
            int span = 0;
            int group = 0;
            int positionSpanSize = getSpanSize(adapterPosition);
            for (int i = 0; i < adapterPosition; i++) {
                int size = getSpanSize(i);
                span += size;
                if (span == spanCount) {
                    span = 0;
                    group++;
                } else
                    if (span > spanCount) {
                        // did not fit, moving to next row / column
                        span = size;
                        group++;
                    }

            }
            if ((span + positionSpanSize) > spanCount) {
                group++;
            }
            return group;
        }
    }

    @java.lang.Override
    public android.view.View onFocusSearchFailed(android.view.View focused, int focusDirection, android.support.v7.widget.RecyclerView.Recycler recycler, android.support.v7.widget.RecyclerView.State state) {
        android.view.View prevFocusedChild = findContainingItemView(focused);
        if (prevFocusedChild == null) {
            return null;
        }
        android.support.v7.widget.GridLayoutManager.LayoutParams lp = ((android.support.v7.widget.GridLayoutManager.LayoutParams) (prevFocusedChild.getLayoutParams()));
        final int prevSpanStart = lp.mSpanIndex;
        final int prevSpanEnd = lp.mSpanIndex + lp.mSpanSize;
        android.view.View view = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
        if (view == null) {
            return null;
        }
        // LinearLayoutManager finds the last child. What we want is the child which has the same
        // spanIndex.
        final int layoutDir = convertFocusDirectionToLayoutDirection(focusDirection);
        final boolean ascend = (layoutDir == android.support.v7.widget.LinearLayoutManager.LayoutState.LAYOUT_END) != mShouldReverseLayout;
        final int start;
        final int inc;
        final int limit;
        if (ascend) {
            start = getChildCount() - 1;
            inc = -1;
            limit = -1;
        } else {
            start = 0;
            inc = 1;
            limit = getChildCount();
        }
        final boolean preferLastSpan = (mOrientation == android.support.v7.widget.LinearLayoutManager.VERTICAL) && isLayoutRTL();
        android.view.View weakCandidate = null;// somewhat matches but not strong

        int weakCandidateSpanIndex = -1;
        int weakCandidateOverlap = 0;// how many spans overlap

        for (int i = start; i != limit; i += inc) {
            android.view.View candidate = getChildAt(i);
            if (candidate == prevFocusedChild) {
                break;
            }
            if (!candidate.isFocusable()) {
                continue;
            }
            final android.support.v7.widget.GridLayoutManager.LayoutParams candidateLp = ((android.support.v7.widget.GridLayoutManager.LayoutParams) (candidate.getLayoutParams()));
            final int candidateStart = candidateLp.mSpanIndex;
            final int candidateEnd = candidateLp.mSpanIndex + candidateLp.mSpanSize;
            if ((candidateStart == prevSpanStart) && (candidateEnd == prevSpanEnd)) {
                return candidate;// perfect match

            }
            boolean assignAsWeek = false;
            if (weakCandidate == null) {
                assignAsWeek = true;
            } else {
                int maxStart = java.lang.Math.max(candidateStart, prevSpanStart);
                int minEnd = java.lang.Math.min(candidateEnd, prevSpanEnd);
                int overlap = minEnd - maxStart;
                if (overlap > weakCandidateOverlap) {
                    assignAsWeek = true;
                } else
                    if ((overlap == weakCandidateOverlap) && (preferLastSpan == (candidateStart > weakCandidateSpanIndex))) {
                        assignAsWeek = true;
                    }

            }
            if (assignAsWeek) {
                weakCandidate = candidate;
                weakCandidateSpanIndex = candidateLp.mSpanIndex;
                weakCandidateOverlap = java.lang.Math.min(candidateEnd, prevSpanEnd) - java.lang.Math.max(candidateStart, prevSpanStart);
            }
        }
        return weakCandidate;
    }

    @java.lang.Override
    public boolean supportsPredictiveItemAnimations() {
        return (mPendingSavedState == null) && (!mPendingSpanCountChange);
    }

    /**
     * Default implementation for {@link SpanSizeLookup}. Each item occupies 1 span.
     */
    public static final class DefaultSpanSizeLookup extends android.support.v7.widget.GridLayoutManager.SpanSizeLookup {
        @java.lang.Override
        public int getSpanSize(int position) {
            return 1;
        }

        @java.lang.Override
        public int getSpanIndex(int position, int spanCount) {
            return position % spanCount;
        }
    }

    /**
     * LayoutParams used by GridLayoutManager.
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

        int mSpanIndex = android.support.v7.widget.GridLayoutManager.LayoutParams.INVALID_SPAN_ID;

        int mSpanSize = 0;

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
         * Returns the current span index of this View. If the View is not laid out yet, the return
         * value is <code>undefined</code>.
         * <p>
         * Starting with RecyclerView <b>24.2.0</b>, span indices are always indexed from position 0
         * even if the layout is RTL. In a vertical GridLayoutManager, <b>leftmost</b> span is span
         * 0 if the layout is <b>LTR</b> and <b>rightmost</b> span is span 0 if the layout is
         * <b>RTL</b>. Prior to 24.2.0, it was the opposite which was conflicting with
         * {@link SpanSizeLookup#getSpanIndex(int, int)}.
         * <p>
         * If the View occupies multiple spans, span with the minimum index is returned.
         *
         * @return The span index of the View.
         */
        public int getSpanIndex() {
            return mSpanIndex;
        }

        /**
         * Returns the number of spans occupied by this View. If the View not laid out yet, the
         * return value is <code>undefined</code>.
         *
         * @return The number of spans occupied by this View.
         */
        public int getSpanSize() {
            return mSpanSize;
        }
    }
}

