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
 * Helper class for LayoutManagers to abstract measurements depending on the View's orientation.
 * <p>
 * It is developed to easily support vertical and horizontal orientations in a LayoutManager but
 * can also be used to abstract calls around view bounds and child measurements with margins and
 * decorations.
 *
 * @see #createHorizontalHelper(RecyclerView.LayoutManager)
 * @see #createVerticalHelper(RecyclerView.LayoutManager)
 */
public abstract class OrientationHelper {
    private static final int INVALID_SIZE = java.lang.Integer.MIN_VALUE;

    protected final android.support.v7.widget.RecyclerView.LayoutManager mLayoutManager;

    public static final int HORIZONTAL = android.widget.LinearLayout.HORIZONTAL;

    public static final int VERTICAL = android.widget.LinearLayout.VERTICAL;

    private int mLastTotalSpace = android.support.v7.widget.OrientationHelper.INVALID_SIZE;

    final android.graphics.Rect mTmpRect = new android.graphics.Rect();

    private OrientationHelper(android.support.v7.widget.RecyclerView.LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    /**
     * Call this method after onLayout method is complete if state is NOT pre-layout.
     * This method records information like layout bounds that might be useful in the next layout
     * calculations.
     */
    public void onLayoutComplete() {
        mLastTotalSpace = getTotalSpace();
    }

    /**
     * Returns the layout space change between the previous layout pass and current layout pass.
     * <p>
     * Make sure you call {@link #onLayoutComplete()} at the end of your LayoutManager's
     * {@link RecyclerView.LayoutManager#onLayoutChildren(RecyclerView.Recycler,
     * RecyclerView.State)} method.
     *
     * @return The difference between the current total space and previous layout's total space.
     * @see #onLayoutComplete()
     */
    public int getTotalSpaceChange() {
        return android.support.v7.widget.OrientationHelper.INVALID_SIZE == mLastTotalSpace ? 0 : getTotalSpace() - mLastTotalSpace;
    }

    /**
     * Returns the start of the view including its decoration and margin.
     * <p>
     * For example, for the horizontal helper, if a View's left is at pixel 20, has 2px left
     * decoration and 3px left margin, returned value will be 15px.
     *
     * @param view
     * 		The view element to check
     * @return The first pixel of the element
     * @see #getDecoratedEnd(android.view.View)
     */
    public abstract int getDecoratedStart(android.view.View view);

    /**
     * Returns the end of the view including its decoration and margin.
     * <p>
     * For example, for the horizontal helper, if a View's right is at pixel 200, has 2px right
     * decoration and 3px right margin, returned value will be 205.
     *
     * @param view
     * 		The view element to check
     * @return The last pixel of the element
     * @see #getDecoratedStart(android.view.View)
     */
    public abstract int getDecoratedEnd(android.view.View view);

    /**
     * Returns the end of the View after its matrix transformations are applied to its layout
     * position.
     * <p>
     * This method is useful when trying to detect the visible edge of a View.
     * <p>
     * It includes the decorations but does not include the margins.
     *
     * @param view
     * 		The view whose transformed end will be returned
     * @return The end of the View after its decor insets and transformation matrix is applied to
    its position
     * @see RecyclerView.LayoutManager#getTransformedBoundingBox(View, boolean, Rect)
     */
    public abstract int getTransformedEndWithDecoration(android.view.View view);

    /**
     * Returns the start of the View after its matrix transformations are applied to its layout
     * position.
     * <p>
     * This method is useful when trying to detect the visible edge of a View.
     * <p>
     * It includes the decorations but does not include the margins.
     *
     * @param view
     * 		The view whose transformed start will be returned
     * @return The start of the View after its decor insets and transformation matrix is applied to
    its position
     * @see RecyclerView.LayoutManager#getTransformedBoundingBox(View, boolean, Rect)
     */
    public abstract int getTransformedStartWithDecoration(android.view.View view);

    /**
     * Returns the space occupied by this View in the current orientation including decorations and
     * margins.
     *
     * @param view
     * 		The view element to check
     * @return Total space occupied by this view
     * @see #getDecoratedMeasurementInOther(View)
     */
    public abstract int getDecoratedMeasurement(android.view.View view);

    /**
     * Returns the space occupied by this View in the perpendicular orientation including
     * decorations and margins.
     *
     * @param view
     * 		The view element to check
     * @return Total space occupied by this view in the perpendicular orientation to current one
     * @see #getDecoratedMeasurement(View)
     */
    public abstract int getDecoratedMeasurementInOther(android.view.View view);

    /**
     * Returns the start position of the layout after the start padding is added.
     *
     * @return The very first pixel we can draw.
     */
    public abstract int getStartAfterPadding();

    /**
     * Returns the end position of the layout after the end padding is removed.
     *
     * @return The end boundary for this layout.
     */
    public abstract int getEndAfterPadding();

    /**
     * Returns the end position of the layout without taking padding into account.
     *
     * @return The end boundary for this layout without considering padding.
     */
    public abstract int getEnd();

    /**
     * Offsets all children's positions by the given amount.
     *
     * @param amount
     * 		Value to add to each child's layout parameters
     */
    public abstract void offsetChildren(int amount);

    /**
     * Returns the total space to layout. This number is the difference between
     * {@link #getEndAfterPadding()} and {@link #getStartAfterPadding()}.
     *
     * @return Total space to layout children
     */
    public abstract int getTotalSpace();

    /**
     * Offsets the child in this orientation.
     *
     * @param view
     * 		View to offset
     * @param offset
     * 		offset amount
     */
    public abstract void offsetChild(android.view.View view, int offset);

    /**
     * Returns the padding at the end of the layout. For horizontal helper, this is the right
     * padding and for vertical helper, this is the bottom padding. This method does not check
     * whether the layout is RTL or not.
     *
     * @return The padding at the end of the layout.
     */
    public abstract int getEndPadding();

    /**
     * Returns the MeasureSpec mode for the current orientation from the LayoutManager.
     *
     * @return The current measure spec mode.
     * @see View.MeasureSpec
     * @see RecyclerView.LayoutManager#getWidthMode()
     * @see RecyclerView.LayoutManager#getHeightMode()
     */
    public abstract int getMode();

    /**
     * Returns the MeasureSpec mode for the perpendicular orientation from the LayoutManager.
     *
     * @return The current measure spec mode.
     * @see View.MeasureSpec
     * @see RecyclerView.LayoutManager#getWidthMode()
     * @see RecyclerView.LayoutManager#getHeightMode()
     */
    public abstract int getModeInOther();

    /**
     * Creates an OrientationHelper for the given LayoutManager and orientation.
     *
     * @param layoutManager
     * 		LayoutManager to attach to
     * @param orientation
     * 		Desired orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}
     * @return A new OrientationHelper
     */
    public static android.support.v7.widget.OrientationHelper createOrientationHelper(android.support.v7.widget.RecyclerView.LayoutManager layoutManager, int orientation) {
        switch (orientation) {
            case android.support.v7.widget.OrientationHelper.HORIZONTAL :
                return android.support.v7.widget.OrientationHelper.createHorizontalHelper(layoutManager);
            case android.support.v7.widget.OrientationHelper.VERTICAL :
                return android.support.v7.widget.OrientationHelper.createVerticalHelper(layoutManager);
        }
        throw new java.lang.IllegalArgumentException("invalid orientation");
    }

    /**
     * Creates a horizontal OrientationHelper for the given LayoutManager.
     *
     * @param layoutManager
     * 		The LayoutManager to attach to.
     * @return A new OrientationHelper
     */
    public static android.support.v7.widget.OrientationHelper createHorizontalHelper(android.support.v7.widget.RecyclerView.LayoutManager layoutManager) {
        return new android.support.v7.widget.OrientationHelper(layoutManager) {
            @java.lang.Override
            public int getEndAfterPadding() {
                return mLayoutManager.getWidth() - mLayoutManager.getPaddingRight();
            }

            @java.lang.Override
            public int getEnd() {
                return mLayoutManager.getWidth();
            }

            @java.lang.Override
            public void offsetChildren(int amount) {
                mLayoutManager.offsetChildrenHorizontal(amount);
            }

            @java.lang.Override
            public int getStartAfterPadding() {
                return mLayoutManager.getPaddingLeft();
            }

            @java.lang.Override
            public int getDecoratedMeasurement(android.view.View view) {
                final android.support.v7.widget.RecyclerView.LayoutParams params = ((android.support.v7.widget.RecyclerView.LayoutParams) (view.getLayoutParams()));
                return (mLayoutManager.getDecoratedMeasuredWidth(view) + params.leftMargin) + params.rightMargin;
            }

            @java.lang.Override
            public int getDecoratedMeasurementInOther(android.view.View view) {
                final android.support.v7.widget.RecyclerView.LayoutParams params = ((android.support.v7.widget.RecyclerView.LayoutParams) (view.getLayoutParams()));
                return (mLayoutManager.getDecoratedMeasuredHeight(view) + params.topMargin) + params.bottomMargin;
            }

            @java.lang.Override
            public int getDecoratedEnd(android.view.View view) {
                final android.support.v7.widget.RecyclerView.LayoutParams params = ((android.support.v7.widget.RecyclerView.LayoutParams) (view.getLayoutParams()));
                return mLayoutManager.getDecoratedRight(view) + params.rightMargin;
            }

            @java.lang.Override
            public int getDecoratedStart(android.view.View view) {
                final android.support.v7.widget.RecyclerView.LayoutParams params = ((android.support.v7.widget.RecyclerView.LayoutParams) (view.getLayoutParams()));
                return mLayoutManager.getDecoratedLeft(view) - params.leftMargin;
            }

            @java.lang.Override
            public int getTransformedEndWithDecoration(android.view.View view) {
                mLayoutManager.getTransformedBoundingBox(view, true, mTmpRect);
                return mTmpRect.right;
            }

            @java.lang.Override
            public int getTransformedStartWithDecoration(android.view.View view) {
                mLayoutManager.getTransformedBoundingBox(view, true, mTmpRect);
                return mTmpRect.left;
            }

            @java.lang.Override
            public int getTotalSpace() {
                return (mLayoutManager.getWidth() - mLayoutManager.getPaddingLeft()) - mLayoutManager.getPaddingRight();
            }

            @java.lang.Override
            public void offsetChild(android.view.View view, int offset) {
                view.offsetLeftAndRight(offset);
            }

            @java.lang.Override
            public int getEndPadding() {
                return mLayoutManager.getPaddingRight();
            }

            @java.lang.Override
            public int getMode() {
                return mLayoutManager.getWidthMode();
            }

            @java.lang.Override
            public int getModeInOther() {
                return mLayoutManager.getHeightMode();
            }
        };
    }

    /**
     * Creates a vertical OrientationHelper for the given LayoutManager.
     *
     * @param layoutManager
     * 		The LayoutManager to attach to.
     * @return A new OrientationHelper
     */
    public static android.support.v7.widget.OrientationHelper createVerticalHelper(android.support.v7.widget.RecyclerView.LayoutManager layoutManager) {
        return new android.support.v7.widget.OrientationHelper(layoutManager) {
            @java.lang.Override
            public int getEndAfterPadding() {
                return mLayoutManager.getHeight() - mLayoutManager.getPaddingBottom();
            }

            @java.lang.Override
            public int getEnd() {
                return mLayoutManager.getHeight();
            }

            @java.lang.Override
            public void offsetChildren(int amount) {
                mLayoutManager.offsetChildrenVertical(amount);
            }

            @java.lang.Override
            public int getStartAfterPadding() {
                return mLayoutManager.getPaddingTop();
            }

            @java.lang.Override
            public int getDecoratedMeasurement(android.view.View view) {
                final android.support.v7.widget.RecyclerView.LayoutParams params = ((android.support.v7.widget.RecyclerView.LayoutParams) (view.getLayoutParams()));
                return (mLayoutManager.getDecoratedMeasuredHeight(view) + params.topMargin) + params.bottomMargin;
            }

            @java.lang.Override
            public int getDecoratedMeasurementInOther(android.view.View view) {
                final android.support.v7.widget.RecyclerView.LayoutParams params = ((android.support.v7.widget.RecyclerView.LayoutParams) (view.getLayoutParams()));
                return (mLayoutManager.getDecoratedMeasuredWidth(view) + params.leftMargin) + params.rightMargin;
            }

            @java.lang.Override
            public int getDecoratedEnd(android.view.View view) {
                final android.support.v7.widget.RecyclerView.LayoutParams params = ((android.support.v7.widget.RecyclerView.LayoutParams) (view.getLayoutParams()));
                return mLayoutManager.getDecoratedBottom(view) + params.bottomMargin;
            }

            @java.lang.Override
            public int getDecoratedStart(android.view.View view) {
                final android.support.v7.widget.RecyclerView.LayoutParams params = ((android.support.v7.widget.RecyclerView.LayoutParams) (view.getLayoutParams()));
                return mLayoutManager.getDecoratedTop(view) - params.topMargin;
            }

            @java.lang.Override
            public int getTransformedEndWithDecoration(android.view.View view) {
                mLayoutManager.getTransformedBoundingBox(view, true, mTmpRect);
                return mTmpRect.bottom;
            }

            @java.lang.Override
            public int getTransformedStartWithDecoration(android.view.View view) {
                mLayoutManager.getTransformedBoundingBox(view, true, mTmpRect);
                return mTmpRect.top;
            }

            @java.lang.Override
            public int getTotalSpace() {
                return (mLayoutManager.getHeight() - mLayoutManager.getPaddingTop()) - mLayoutManager.getPaddingBottom();
            }

            @java.lang.Override
            public void offsetChild(android.view.View view, int offset) {
                view.offsetTopAndBottom(offset);
            }

            @java.lang.Override
            public int getEndPadding() {
                return mLayoutManager.getPaddingBottom();
            }

            @java.lang.Override
            public int getMode() {
                return mLayoutManager.getHeightMode();
            }

            @java.lang.Override
            public int getModeInOther() {
                return mLayoutManager.getWidthMode();
            }
        };
    }
}

