/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * A view that shows items in a center-locked, horizontally scrolling list.
 * <p>
 * The default values for the Gallery assume you will be using
 * {@link android.R.styleable#Theme_galleryItemBackground} as the background for
 * each View given to the Gallery from the Adapter. If you are not doing this,
 * you may need to adjust some Gallery properties, such as the spacing.
 * <p>
 * Views given to the Gallery should use {@link Gallery.LayoutParams} as their
 * layout parameters type.
 *
 * @unknown ref android.R.styleable#Gallery_animationDuration
 * @unknown ref android.R.styleable#Gallery_spacing
 * @unknown ref android.R.styleable#Gallery_gravity
 * @deprecated This widget is no longer supported. Other horizontally scrolling
widgets include {@link HorizontalScrollView} and {@link android.support.v4.view.ViewPager}
from the support library.
 */
@java.lang.Deprecated
@android.annotation.Widget
public class Gallery extends android.widget.AbsSpinner implements android.view.GestureDetector.OnGestureListener {
    private static final java.lang.String TAG = "Gallery";

    private static final boolean localLOGV = false;

    /**
     * Duration in milliseconds from the start of a scroll during which we're
     * unsure whether the user is scrolling or flinging.
     */
    private static final int SCROLL_TO_FLING_UNCERTAINTY_TIMEOUT = 250;

    /**
     * Horizontal spacing between items.
     */
    @android.annotation.UnsupportedAppUsage
    private int mSpacing = 0;

    /**
     * How long the transition animation should run when a child view changes
     * position, measured in milliseconds.
     */
    private int mAnimationDuration = 400;

    /**
     * The alpha of items that are not selected.
     */
    private float mUnselectedAlpha;

    /**
     * Left most edge of a child seen so far during layout.
     */
    private int mLeftMost;

    /**
     * Right most edge of a child seen so far during layout.
     */
    private int mRightMost;

    private int mGravity;

    /**
     * Helper for detecting touch gestures.
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private android.view.GestureDetector mGestureDetector;

    /**
     * The position of the item that received the user's down touch.
     */
    @android.annotation.UnsupportedAppUsage
    private int mDownTouchPosition;

    /**
     * The view of the item that received the user's down touch.
     */
    @android.annotation.UnsupportedAppUsage
    private android.view.View mDownTouchView;

    /**
     * Executes the delta scrolls from a fling or scroll movement.
     */
    @android.annotation.UnsupportedAppUsage
    private android.widget.Gallery.FlingRunnable mFlingRunnable = new android.widget.Gallery.FlingRunnable();

    /**
     * Sets mSuppressSelectionChanged = false. This is used to set it to false
     * in the future. It will also trigger a selection changed.
     */
    private java.lang.Runnable mDisableSuppressSelectionChangedRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            mSuppressSelectionChanged = false;
            selectionChanged();
        }
    };

    /**
     * When fling runnable runs, it resets this to false. Any method along the
     * path until the end of its run() can set this to true to abort any
     * remaining fling. For example, if we've reached either the leftmost or
     * rightmost item, we will set this to true.
     */
    private boolean mShouldStopFling;

    /**
     * The currently selected item's child.
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private android.view.View mSelectedChild;

    /**
     * Whether to continuously callback on the item selected listener during a
     * fling.
     */
    private boolean mShouldCallbackDuringFling = true;

    /**
     * Whether to callback when an item that is not selected is clicked.
     */
    private boolean mShouldCallbackOnUnselectedItemClick = true;

    /**
     * If true, do not callback to item selected listener.
     */
    private boolean mSuppressSelectionChanged;

    /**
     * If true, we have received the "invoke" (center or enter buttons) key
     * down. This is checked before we action on the "invoke" key up, and is
     * subsequently cleared.
     */
    private boolean mReceivedInvokeKeyDown;

    private android.widget.AdapterView.AdapterContextMenuInfo mContextMenuInfo;

    /**
     * If true, this onScroll is the first for this user's drag (remember, a
     * drag sends many onScrolls).
     */
    private boolean mIsFirstScroll;

    /**
     * If true, mFirstPosition is the position of the rightmost child, and
     * the children are ordered right to left.
     */
    private boolean mIsRtl = true;

    /**
     * Offset between the center of the selected child view and the center of the Gallery.
     * Used to reset position correctly during layout.
     */
    private int mSelectedCenterOffset;

    public Gallery(android.content.Context context) {
        this(context, null);
    }

    public Gallery(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.galleryStyle);
    }

    public Gallery(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Gallery(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, android.widget.com.android.internal.R.styleable.Gallery, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, android.widget.com.android.internal.R.styleable.Gallery, attrs, a, defStyleAttr, defStyleRes);
        int index = a.getInt(android.widget.com.android.internal.R.styleable.Gallery_gravity, -1);
        if (index >= 0) {
            setGravity(index);
        }
        int animationDuration = a.getInt(android.widget.com.android.internal.R.styleable.Gallery_animationDuration, -1);
        if (animationDuration > 0) {
            setAnimationDuration(animationDuration);
        }
        int spacing = a.getDimensionPixelOffset(android.widget.com.android.internal.R.styleable.Gallery_spacing, 0);
        setSpacing(spacing);
        float unselectedAlpha = a.getFloat(android.widget.com.android.internal.R.styleable.Gallery_unselectedAlpha, 0.5F);
        setUnselectedAlpha(unselectedAlpha);
        a.recycle();
        // We draw the selected item last (because otherwise the item to the
        // right overlaps it)
        mGroupFlags |= android.view.ViewGroup.FLAG_USE_CHILD_DRAWING_ORDER;
        mGroupFlags |= android.view.ViewGroup.FLAG_SUPPORT_STATIC_TRANSFORMATIONS;
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mGestureDetector == null) {
            mGestureDetector = new android.view.GestureDetector(getContext(), this);
            mGestureDetector.setIsLongpressEnabled(true);
        }
    }

    /**
     * Whether or not to callback on any {@link #getOnItemSelectedListener()}
     * while the items are being flinged. If false, only the final selected item
     * will cause the callback. If true, all items between the first and the
     * final will cause callbacks.
     *
     * @param shouldCallback
     * 		Whether or not to callback on the listener while
     * 		the items are being flinged.
     */
    public void setCallbackDuringFling(boolean shouldCallback) {
        mShouldCallbackDuringFling = shouldCallback;
    }

    /**
     * Whether or not to callback when an item that is not selected is clicked.
     * If false, the item will become selected (and re-centered). If true, the
     * {@link #getOnItemClickListener()} will get the callback.
     *
     * @param shouldCallback
     * 		Whether or not to callback on the listener when a
     * 		item that is not selected is clicked.
     * @unknown 
     */
    public void setCallbackOnUnselectedItemClick(boolean shouldCallback) {
        mShouldCallbackOnUnselectedItemClick = shouldCallback;
    }

    /**
     * Sets how long the transition animation should run when a child view
     * changes position. Only relevant if animation is turned on.
     *
     * @param animationDurationMillis
     * 		The duration of the transition, in
     * 		milliseconds.
     * @unknown ref android.R.styleable#Gallery_animationDuration
     */
    public void setAnimationDuration(int animationDurationMillis) {
        mAnimationDuration = animationDurationMillis;
    }

    /**
     * Sets the spacing between items in a Gallery
     *
     * @param spacing
     * 		The spacing in pixels between items in the Gallery
     * @unknown ref android.R.styleable#Gallery_spacing
     */
    public void setSpacing(int spacing) {
        mSpacing = spacing;
    }

    /**
     * Sets the alpha of items that are not selected in the Gallery.
     *
     * @param unselectedAlpha
     * 		the alpha for the items that are not selected.
     * @unknown ref android.R.styleable#Gallery_unselectedAlpha
     */
    public void setUnselectedAlpha(float unselectedAlpha) {
        mUnselectedAlpha = unselectedAlpha;
    }

    @java.lang.Override
    protected boolean getChildStaticTransformation(android.view.View child, android.view.animation.Transformation t) {
        t.clear();
        t.setAlpha(child == mSelectedChild ? 1.0F : mUnselectedAlpha);
        return true;
    }

    @java.lang.Override
    protected int computeHorizontalScrollExtent() {
        // Only 1 item is considered to be selected
        return 1;
    }

    @java.lang.Override
    protected int computeHorizontalScrollOffset() {
        // Current scroll position is the same as the selected position
        return mSelectedPosition;
    }

    @java.lang.Override
    protected int computeHorizontalScrollRange() {
        // Scroll range is the same as the item count
        return mItemCount;
    }

    @java.lang.Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof android.widget.Gallery.LayoutParams;
    }

    @java.lang.Override
    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new android.widget.Gallery.LayoutParams(p);
    }

    @java.lang.Override
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.widget.Gallery.LayoutParams(getContext(), attrs);
    }

    @java.lang.Override
    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        /* Gallery expects Gallery.LayoutParams. */
        return new android.widget.Gallery.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        /* Remember that we are in layout to prevent more layout request from
        being generated.
         */
        mInLayout = true;
        layout(0, false);
        mInLayout = false;
    }

    @java.lang.Override
    int getChildHeight(android.view.View child) {
        return child.getMeasuredHeight();
    }

    /**
     * Tracks a motion scroll. In reality, this is used to do just about any
     * movement to items (touch scroll, arrow-key scroll, set an item as selected).
     *
     * @param deltaX
     * 		Change in X from the previous event.
     */
    @android.annotation.UnsupportedAppUsage
    void trackMotionScroll(int deltaX) {
        if (getChildCount() == 0) {
            return;
        }
        boolean toLeft = deltaX < 0;
        int limitedDeltaX = getLimitedMotionScrollAmount(toLeft, deltaX);
        if (limitedDeltaX != deltaX) {
            // The above call returned a limited amount, so stop any scrolls/flings
            mFlingRunnable.endFling(false);
            onFinishedMovement();
        }
        offsetChildrenLeftAndRight(limitedDeltaX);
        detachOffScreenChildren(toLeft);
        if (toLeft) {
            // If moved left, there will be empty space on the right
            fillToGalleryRight();
        } else {
            // Similarly, empty space on the left
            fillToGalleryLeft();
        }
        // Clear unused views
        mRecycler.clear();
        setSelectionToCenterChild();
        final android.view.View selChild = mSelectedChild;
        if (selChild != null) {
            final int childLeft = selChild.getLeft();
            final int childCenter = selChild.getWidth() / 2;
            final int galleryCenter = getWidth() / 2;
            mSelectedCenterOffset = (childLeft + childCenter) - galleryCenter;
        }
        onScrollChanged(0, 0, 0, 0);// dummy values, View's implementation does not use these.

        invalidate();
    }

    int getLimitedMotionScrollAmount(boolean motionToLeft, int deltaX) {
        int extremeItemPosition = (motionToLeft != mIsRtl) ? mItemCount - 1 : 0;
        android.view.View extremeChild = getChildAt(extremeItemPosition - mFirstPosition);
        if (extremeChild == null) {
            return deltaX;
        }
        int extremeChildCenter = android.widget.Gallery.getCenterOfView(extremeChild);
        int galleryCenter = getCenterOfGallery();
        if (motionToLeft) {
            if (extremeChildCenter <= galleryCenter) {
                // The extreme child is past his boundary point!
                return 0;
            }
        } else {
            if (extremeChildCenter >= galleryCenter) {
                // The extreme child is past his boundary point!
                return 0;
            }
        }
        int centerDifference = galleryCenter - extremeChildCenter;
        return motionToLeft ? java.lang.Math.max(centerDifference, deltaX) : java.lang.Math.min(centerDifference, deltaX);
    }

    /**
     * Offset the horizontal location of all children of this view by the
     * specified number of pixels.
     *
     * @param offset
     * 		the number of pixels to offset
     */
    private void offsetChildrenLeftAndRight(int offset) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            getChildAt(i).offsetLeftAndRight(offset);
        }
    }

    /**
     *
     *
     * @return The center of this Gallery.
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private int getCenterOfGallery() {
        return (((getWidth() - mPaddingLeft) - mPaddingRight) / 2) + mPaddingLeft;
    }

    /**
     *
     *
     * @return The center of the given view.
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private static int getCenterOfView(android.view.View view) {
        return view.getLeft() + (view.getWidth() / 2);
    }

    /**
     * Detaches children that are off the screen (i.e.: Gallery bounds).
     *
     * @param toLeft
     * 		Whether to detach children to the left of the Gallery, or
     * 		to the right.
     */
    private void detachOffScreenChildren(boolean toLeft) {
        int numChildren = getChildCount();
        int firstPosition = mFirstPosition;
        int start = 0;
        int count = 0;
        if (toLeft) {
            final int galleryLeft = mPaddingLeft;
            for (int i = 0; i < numChildren; i++) {
                int n = (mIsRtl) ? (numChildren - 1) - i : i;
                final android.view.View child = getChildAt(n);
                if (child.getRight() >= galleryLeft) {
                    break;
                } else {
                    start = n;
                    count++;
                    mRecycler.put(firstPosition + n, child);
                }
            }
            if (!mIsRtl) {
                start = 0;
            }
        } else {
            final int galleryRight = getWidth() - mPaddingRight;
            for (int i = numChildren - 1; i >= 0; i--) {
                int n = (mIsRtl) ? (numChildren - 1) - i : i;
                final android.view.View child = getChildAt(n);
                if (child.getLeft() <= galleryRight) {
                    break;
                } else {
                    start = n;
                    count++;
                    mRecycler.put(firstPosition + n, child);
                }
            }
            if (mIsRtl) {
                start = 0;
            }
        }
        detachViewsFromParent(start, count);
        if (toLeft != mIsRtl) {
            mFirstPosition += count;
        }
    }

    /**
     * Scrolls the items so that the selected item is in its 'slot' (its center
     * is the gallery's center).
     */
    private void scrollIntoSlots() {
        if ((getChildCount() == 0) || (mSelectedChild == null))
            return;

        int selectedCenter = android.widget.Gallery.getCenterOfView(mSelectedChild);
        int targetCenter = getCenterOfGallery();
        int scrollAmount = targetCenter - selectedCenter;
        if (scrollAmount != 0) {
            mFlingRunnable.startUsingDistance(scrollAmount);
        } else {
            onFinishedMovement();
        }
    }

    private void onFinishedMovement() {
        if (mSuppressSelectionChanged) {
            mSuppressSelectionChanged = false;
            // We haven't been callbacking during the fling, so do it now
            super.selectionChanged();
        }
        mSelectedCenterOffset = 0;
        invalidate();
    }

    @java.lang.Override
    void selectionChanged() {
        if (!mSuppressSelectionChanged) {
            super.selectionChanged();
        }
    }

    /**
     * Looks for the child that is closest to the center and sets it as the
     * selected child.
     */
    private void setSelectionToCenterChild() {
        android.view.View selView = mSelectedChild;
        if (mSelectedChild == null)
            return;

        int galleryCenter = getCenterOfGallery();
        // Common case where the current selected position is correct
        if ((selView.getLeft() <= galleryCenter) && (selView.getRight() >= galleryCenter)) {
            return;
        }
        // TODO better search
        int closestEdgeDistance = java.lang.Integer.MAX_VALUE;
        int newSelectedChildIndex = 0;
        for (int i = getChildCount() - 1; i >= 0; i--) {
            android.view.View child = getChildAt(i);
            if ((child.getLeft() <= galleryCenter) && (child.getRight() >= galleryCenter)) {
                // This child is in the center
                newSelectedChildIndex = i;
                break;
            }
            int childClosestEdgeDistance = java.lang.Math.min(java.lang.Math.abs(child.getLeft() - galleryCenter), java.lang.Math.abs(child.getRight() - galleryCenter));
            if (childClosestEdgeDistance < closestEdgeDistance) {
                closestEdgeDistance = childClosestEdgeDistance;
                newSelectedChildIndex = i;
            }
        }
        int newPos = mFirstPosition + newSelectedChildIndex;
        if (newPos != mSelectedPosition) {
            setSelectedPositionInt(newPos);
            setNextSelectedPositionInt(newPos);
            checkSelectionChanged();
        }
    }

    /**
     * Creates and positions all views for this Gallery.
     * <p>
     * We layout rarely, most of the time {@link #trackMotionScroll(int)} takes
     * care of repositioning, adding, and removing children.
     *
     * @param delta
     * 		Change in the selected position. +1 means the selection is
     * 		moving to the right, so views are scrolling to the left. -1
     * 		means the selection is moving to the left.
     */
    @java.lang.Override
    void layout(int delta, boolean animate) {
        mIsRtl = isLayoutRtl();
        int childrenLeft = mSpinnerPadding.left;
        int childrenWidth = ((mRight - mLeft) - mSpinnerPadding.left) - mSpinnerPadding.right;
        if (mDataChanged) {
            handleDataChanged();
        }
        // Handle an empty gallery by removing all views.
        if (mItemCount == 0) {
            resetList();
            return;
        }
        // Update to the new selected position.
        if (mNextSelectedPosition >= 0) {
            setSelectedPositionInt(mNextSelectedPosition);
        }
        // All views go in recycler while we are in layout
        recycleAllViews();
        // Clear out old views
        // removeAllViewsInLayout();
        detachAllViewsFromParent();
        /* These will be used to give initial positions to views entering the
        gallery as we scroll
         */
        mRightMost = 0;
        mLeftMost = 0;
        // Make selected view and center it
        /* mFirstPosition will be decreased as we add views to the left later
        on. The 0 for x will be offset in a couple lines down.
         */
        mFirstPosition = mSelectedPosition;
        android.view.View sel = makeAndAddView(mSelectedPosition, 0, 0, true);
        // Put the selected child in the center
        int selectedOffset = ((childrenLeft + (childrenWidth / 2)) - (sel.getWidth() / 2)) + mSelectedCenterOffset;
        sel.offsetLeftAndRight(selectedOffset);
        fillToGalleryRight();
        fillToGalleryLeft();
        // Flush any cached views that did not get reused above
        mRecycler.clear();
        invalidate();
        checkSelectionChanged();
        mDataChanged = false;
        mNeedSync = false;
        setNextSelectedPositionInt(mSelectedPosition);
        updateSelectedItemMetadata();
    }

    @android.annotation.UnsupportedAppUsage
    private void fillToGalleryLeft() {
        if (mIsRtl) {
            fillToGalleryLeftRtl();
        } else {
            fillToGalleryLeftLtr();
        }
    }

    private void fillToGalleryLeftRtl() {
        int itemSpacing = mSpacing;
        int galleryLeft = mPaddingLeft;
        int numChildren = getChildCount();
        int numItems = mItemCount;
        // Set state for initial iteration
        android.view.View prevIterationView = getChildAt(numChildren - 1);
        int curPosition;
        int curRightEdge;
        if (prevIterationView != null) {
            curPosition = mFirstPosition + numChildren;
            curRightEdge = prevIterationView.getLeft() - itemSpacing;
        } else {
            // No children available!
            mFirstPosition = curPosition = mItemCount - 1;
            curRightEdge = (mRight - mLeft) - mPaddingRight;
            mShouldStopFling = true;
        }
        while ((curRightEdge > galleryLeft) && (curPosition < mItemCount)) {
            prevIterationView = makeAndAddView(curPosition, curPosition - mSelectedPosition, curRightEdge, false);
            // Set state for next iteration
            curRightEdge = prevIterationView.getLeft() - itemSpacing;
            curPosition++;
        } 
    }

    private void fillToGalleryLeftLtr() {
        int itemSpacing = mSpacing;
        int galleryLeft = mPaddingLeft;
        // Set state for initial iteration
        android.view.View prevIterationView = getChildAt(0);
        int curPosition;
        int curRightEdge;
        if (prevIterationView != null) {
            curPosition = mFirstPosition - 1;
            curRightEdge = prevIterationView.getLeft() - itemSpacing;
        } else {
            // No children available!
            curPosition = 0;
            curRightEdge = (mRight - mLeft) - mPaddingRight;
            mShouldStopFling = true;
        }
        while ((curRightEdge > galleryLeft) && (curPosition >= 0)) {
            prevIterationView = makeAndAddView(curPosition, curPosition - mSelectedPosition, curRightEdge, false);
            // Remember some state
            mFirstPosition = curPosition;
            // Set state for next iteration
            curRightEdge = prevIterationView.getLeft() - itemSpacing;
            curPosition--;
        } 
    }

    @android.annotation.UnsupportedAppUsage
    private void fillToGalleryRight() {
        if (mIsRtl) {
            fillToGalleryRightRtl();
        } else {
            fillToGalleryRightLtr();
        }
    }

    private void fillToGalleryRightRtl() {
        int itemSpacing = mSpacing;
        int galleryRight = (mRight - mLeft) - mPaddingRight;
        // Set state for initial iteration
        android.view.View prevIterationView = getChildAt(0);
        int curPosition;
        int curLeftEdge;
        if (prevIterationView != null) {
            curPosition = mFirstPosition - 1;
            curLeftEdge = prevIterationView.getRight() + itemSpacing;
        } else {
            curPosition = 0;
            curLeftEdge = mPaddingLeft;
            mShouldStopFling = true;
        }
        while ((curLeftEdge < galleryRight) && (curPosition >= 0)) {
            prevIterationView = makeAndAddView(curPosition, curPosition - mSelectedPosition, curLeftEdge, true);
            // Remember some state
            mFirstPosition = curPosition;
            // Set state for next iteration
            curLeftEdge = prevIterationView.getRight() + itemSpacing;
            curPosition--;
        } 
    }

    private void fillToGalleryRightLtr() {
        int itemSpacing = mSpacing;
        int galleryRight = (mRight - mLeft) - mPaddingRight;
        int numChildren = getChildCount();
        int numItems = mItemCount;
        // Set state for initial iteration
        android.view.View prevIterationView = getChildAt(numChildren - 1);
        int curPosition;
        int curLeftEdge;
        if (prevIterationView != null) {
            curPosition = mFirstPosition + numChildren;
            curLeftEdge = prevIterationView.getRight() + itemSpacing;
        } else {
            mFirstPosition = curPosition = mItemCount - 1;
            curLeftEdge = mPaddingLeft;
            mShouldStopFling = true;
        }
        while ((curLeftEdge < galleryRight) && (curPosition < numItems)) {
            prevIterationView = makeAndAddView(curPosition, curPosition - mSelectedPosition, curLeftEdge, true);
            // Set state for next iteration
            curLeftEdge = prevIterationView.getRight() + itemSpacing;
            curPosition++;
        } 
    }

    /**
     * Obtain a view, either by pulling an existing view from the recycler or by
     * getting a new one from the adapter. If we are animating, make sure there
     * is enough information in the view's layout parameters to animate from the
     * old to new positions.
     *
     * @param position
     * 		Position in the gallery for the view to obtain
     * @param offset
     * 		Offset from the selected position
     * @param x
     * 		X-coordinate indicating where this view should be placed. This
     * 		will either be the left or right edge of the view, depending on
     * 		the fromLeft parameter
     * @param fromLeft
     * 		Are we positioning views based on the left edge? (i.e.,
     * 		building from left to right)?
     * @return A view that has been added to the gallery
     */
    @android.annotation.UnsupportedAppUsage
    private android.view.View makeAndAddView(int position, int offset, int x, boolean fromLeft) {
        android.view.View child;
        if (!mDataChanged) {
            child = mRecycler.get(position);
            if (child != null) {
                // Can reuse an existing view
                int childLeft = child.getLeft();
                // Remember left and right edges of where views have been placed
                mRightMost = java.lang.Math.max(mRightMost, childLeft + child.getMeasuredWidth());
                mLeftMost = java.lang.Math.min(mLeftMost, childLeft);
                // Position the view
                setUpChild(child, offset, x, fromLeft);
                return child;
            }
        }
        // Nothing found in the recycler -- ask the adapter for a view
        child = mAdapter.getView(position, null, this);
        // Position the view
        setUpChild(child, offset, x, fromLeft);
        return child;
    }

    /**
     * Helper for makeAndAddView to set the position of a view and fill out its
     * layout parameters.
     *
     * @param child
     * 		The view to position
     * @param offset
     * 		Offset from the selected position
     * @param x
     * 		X-coordinate indicating where this view should be placed. This
     * 		will either be the left or right edge of the view, depending on
     * 		the fromLeft parameter
     * @param fromLeft
     * 		Are we positioning views based on the left edge? (i.e.,
     * 		building from left to right)?
     */
    private void setUpChild(android.view.View child, int offset, int x, boolean fromLeft) {
        // Respect layout params that are already in the view. Otherwise
        // make some up...
        android.widget.Gallery.LayoutParams lp = ((android.widget.Gallery.LayoutParams) (child.getLayoutParams()));
        if (lp == null) {
            lp = ((android.widget.Gallery.LayoutParams) (generateDefaultLayoutParams()));
        }
        addViewInLayout(child, fromLeft != mIsRtl ? -1 : 0, lp, true);
        child.setSelected(offset == 0);
        // Get measure specs
        int childHeightSpec = android.view.ViewGroup.getChildMeasureSpec(mHeightMeasureSpec, mSpinnerPadding.top + mSpinnerPadding.bottom, lp.height);
        int childWidthSpec = android.view.ViewGroup.getChildMeasureSpec(mWidthMeasureSpec, mSpinnerPadding.left + mSpinnerPadding.right, lp.width);
        // Measure child
        child.measure(childWidthSpec, childHeightSpec);
        int childLeft;
        int childRight;
        // Position vertically based on gravity setting
        int childTop = calculateTop(child, true);
        int childBottom = childTop + child.getMeasuredHeight();
        int width = child.getMeasuredWidth();
        if (fromLeft) {
            childLeft = x;
            childRight = childLeft + width;
        } else {
            childLeft = x - width;
            childRight = x;
        }
        child.layout(childLeft, childTop, childRight, childBottom);
    }

    /**
     * Figure out vertical placement based on mGravity
     *
     * @param child
     * 		Child to place
     * @return Where the top of the child should be
     */
    private int calculateTop(android.view.View child, boolean duringLayout) {
        int myHeight = (duringLayout) ? getMeasuredHeight() : getHeight();
        int childHeight = (duringLayout) ? child.getMeasuredHeight() : child.getHeight();
        int childTop = 0;
        switch (mGravity) {
            case android.view.Gravity.TOP :
                childTop = mSpinnerPadding.top;
                break;
            case android.view.Gravity.CENTER_VERTICAL :
                int availableSpace = ((myHeight - mSpinnerPadding.bottom) - mSpinnerPadding.top) - childHeight;
                childTop = mSpinnerPadding.top + (availableSpace / 2);
                break;
            case android.view.Gravity.BOTTOM :
                childTop = (myHeight - mSpinnerPadding.bottom) - childHeight;
                break;
        }
        return childTop;
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        // Give everything to the gesture detector
        boolean retValue = mGestureDetector.onTouchEvent(event);
        int action = event.getAction();
        if (action == android.view.MotionEvent.ACTION_UP) {
            // Helper method for lifted finger
            onUp();
        } else
            if (action == android.view.MotionEvent.ACTION_CANCEL) {
                onCancel();
            }

        return retValue;
    }

    @java.lang.Override
    public boolean onSingleTapUp(android.view.MotionEvent e) {
        if (mDownTouchPosition >= 0) {
            // An item tap should make it selected, so scroll to this child.
            scrollToChild(mDownTouchPosition - mFirstPosition);
            // Also pass the click so the client knows, if it wants to.
            if (mShouldCallbackOnUnselectedItemClick || (mDownTouchPosition == mSelectedPosition)) {
                performItemClick(mDownTouchView, mDownTouchPosition, mAdapter.getItemId(mDownTouchPosition));
            }
            return true;
        }
        return false;
    }

    @java.lang.Override
    public boolean onFling(android.view.MotionEvent e1, android.view.MotionEvent e2, float velocityX, float velocityY) {
        if (!mShouldCallbackDuringFling) {
            // We want to suppress selection changes
            // Remove any future code to set mSuppressSelectionChanged = false
            removeCallbacks(mDisableSuppressSelectionChangedRunnable);
            // This will get reset once we scroll into slots
            if (!mSuppressSelectionChanged)
                mSuppressSelectionChanged = true;

        }
        // Fling the gallery!
        mFlingRunnable.startUsingVelocity(((int) (-velocityX)));
        return true;
    }

    @java.lang.Override
    public boolean onScroll(android.view.MotionEvent e1, android.view.MotionEvent e2, float distanceX, float distanceY) {
        if (android.widget.Gallery.localLOGV)
            android.util.Log.v(android.widget.Gallery.TAG, java.lang.String.valueOf(e2.getX() - e1.getX()));

        /* Now's a good time to tell our parent to stop intercepting our events!
        The user has moved more than the slop amount, since GestureDetector
        ensures this before calling this method. Also, if a parent is more
        interested in this touch's events than we are, it would have
        intercepted them by now (for example, we can assume when a Gallery is
        in the ListView, a vertical scroll would not end up in this method
        since a ListView would have intercepted it by now).
         */
        mParent.requestDisallowInterceptTouchEvent(true);
        // As the user scrolls, we want to callback selection changes so related-
        // info on the screen is up-to-date with the gallery's selection
        if (!mShouldCallbackDuringFling) {
            if (mIsFirstScroll) {
                /* We're not notifying the client of selection changes during
                the fling, and this scroll could possibly be a fling. Don't
                do selection changes until we're sure it is not a fling.
                 */
                if (!mSuppressSelectionChanged)
                    mSuppressSelectionChanged = true;

                postDelayed(mDisableSuppressSelectionChangedRunnable, android.widget.Gallery.SCROLL_TO_FLING_UNCERTAINTY_TIMEOUT);
            }
        } else {
            if (mSuppressSelectionChanged)
                mSuppressSelectionChanged = false;

        }
        // Track the motion
        trackMotionScroll((-1) * ((int) (distanceX)));
        mIsFirstScroll = false;
        return true;
    }

    @java.lang.Override
    public boolean onDown(android.view.MotionEvent e) {
        // Kill any existing fling/scroll
        mFlingRunnable.stop(false);
        // Get the item's view that was touched
        mDownTouchPosition = pointToPosition(((int) (e.getX())), ((int) (e.getY())));
        if (mDownTouchPosition >= 0) {
            mDownTouchView = getChildAt(mDownTouchPosition - mFirstPosition);
            mDownTouchView.setPressed(true);
        }
        // Reset the multiple-scroll tracking state
        mIsFirstScroll = true;
        // Must return true to get matching events for this down event.
        return true;
    }

    /**
     * Called when a touch event's action is MotionEvent.ACTION_UP.
     */
    void onUp() {
        if (mFlingRunnable.mScroller.isFinished()) {
            scrollIntoSlots();
        }
        dispatchUnpress();
    }

    /**
     * Called when a touch event's action is MotionEvent.ACTION_CANCEL.
     */
    void onCancel() {
        onUp();
    }

    @java.lang.Override
    public void onLongPress(@android.annotation.NonNull
    android.view.MotionEvent e) {
        if (mDownTouchPosition < 0) {
            return;
        }
        performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS);
        final long id = getItemIdAtPosition(mDownTouchPosition);
        dispatchLongPress(mDownTouchView, mDownTouchPosition, id, e.getX(), e.getY(), true);
    }

    // Unused methods from GestureDetector.OnGestureListener below
    @java.lang.Override
    public void onShowPress(android.view.MotionEvent e) {
    }

    // Unused methods from GestureDetector.OnGestureListener above
    private void dispatchPress(android.view.View child) {
        if (child != null) {
            child.setPressed(true);
        }
        setPressed(true);
    }

    private void dispatchUnpress() {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            getChildAt(i).setPressed(false);
        }
        setPressed(false);
    }

    @java.lang.Override
    public void dispatchSetSelected(boolean selected) {
        /* We don't want to pass the selected state given from its parent to its
        children since this widget itself has a selected state to give to its
        children.
         */
    }

    @java.lang.Override
    protected void dispatchSetPressed(boolean pressed) {
        // Show the pressed state on the selected child
        if (mSelectedChild != null) {
            mSelectedChild.setPressed(pressed);
        }
    }

    @java.lang.Override
    protected android.view.ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return mContextMenuInfo;
    }

    @java.lang.Override
    public boolean showContextMenuForChild(android.view.View originalView) {
        if (isShowingContextMenuWithCoords()) {
            return false;
        }
        return showContextMenuForChildInternal(originalView, 0, 0, false);
    }

    @java.lang.Override
    public boolean showContextMenuForChild(android.view.View originalView, float x, float y) {
        return showContextMenuForChildInternal(originalView, x, y, true);
    }

    private boolean showContextMenuForChildInternal(android.view.View originalView, float x, float y, boolean useOffsets) {
        final int longPressPosition = getPositionForView(originalView);
        if (longPressPosition < 0) {
            return false;
        }
        final long longPressId = mAdapter.getItemId(longPressPosition);
        return dispatchLongPress(originalView, longPressPosition, longPressId, x, y, useOffsets);
    }

    @java.lang.Override
    public boolean showContextMenu() {
        return showContextMenuInternal(0, 0, false);
    }

    @java.lang.Override
    public boolean showContextMenu(float x, float y) {
        return showContextMenuInternal(x, y, true);
    }

    private boolean showContextMenuInternal(float x, float y, boolean useOffsets) {
        if (isPressed() && (mSelectedPosition >= 0)) {
            final int index = mSelectedPosition - mFirstPosition;
            final android.view.View v = getChildAt(index);
            return dispatchLongPress(v, mSelectedPosition, mSelectedRowId, x, y, useOffsets);
        }
        return false;
    }

    private boolean dispatchLongPress(android.view.View view, int position, long id, float x, float y, boolean useOffsets) {
        boolean handled = false;
        if (mOnItemLongClickListener != null) {
            handled = mOnItemLongClickListener.onItemLongClick(this, mDownTouchView, mDownTouchPosition, id);
        }
        if (!handled) {
            mContextMenuInfo = new android.widget.AdapterView.AdapterContextMenuInfo(view, position, id);
            if (useOffsets) {
                handled = super.showContextMenuForChild(view, x, y);
            } else {
                handled = super.showContextMenuForChild(this);
            }
        }
        if (handled) {
            performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS);
        }
        return handled;
    }

    @java.lang.Override
    public boolean dispatchKeyEvent(android.view.KeyEvent event) {
        // Gallery steals all key events
        return event.dispatch(this, null, null);
    }

    /**
     * Handles left, right, and clicking
     *
     * @see android.view.View#onKeyDown
     */
    @java.lang.Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_DPAD_LEFT :
                if (moveDirection(-1)) {
                    playSoundEffect(android.view.SoundEffectConstants.NAVIGATION_LEFT);
                    return true;
                }
                break;
            case android.view.KeyEvent.KEYCODE_DPAD_RIGHT :
                if (moveDirection(1)) {
                    playSoundEffect(android.view.SoundEffectConstants.NAVIGATION_RIGHT);
                    return true;
                }
                break;
            case android.view.KeyEvent.KEYCODE_DPAD_CENTER :
            case android.view.KeyEvent.KEYCODE_ENTER :
                mReceivedInvokeKeyDown = true;
                // fallthrough to default handling
        }
        return super.onKeyDown(keyCode, event);
    }

    @java.lang.Override
    public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (android.view.KeyEvent.isConfirmKey(keyCode)) {
            if (mReceivedInvokeKeyDown) {
                if (mItemCount > 0) {
                    dispatchPress(mSelectedChild);
                    postDelayed(new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            dispatchUnpress();
                        }
                    }, android.view.ViewConfiguration.getPressedStateDuration());
                    int selectedIndex = mSelectedPosition - mFirstPosition;
                    performItemClick(getChildAt(selectedIndex), mSelectedPosition, mAdapter.getItemId(mSelectedPosition));
                }
            }
            // Clear the flag
            mReceivedInvokeKeyDown = false;
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @android.annotation.UnsupportedAppUsage
    boolean moveDirection(int direction) {
        direction = (isLayoutRtl()) ? -direction : direction;
        int targetPosition = mSelectedPosition + direction;
        if (((mItemCount > 0) && (targetPosition >= 0)) && (targetPosition < mItemCount)) {
            scrollToChild(targetPosition - mFirstPosition);
            return true;
        } else {
            return false;
        }
    }

    private boolean scrollToChild(int childPosition) {
        android.view.View child = getChildAt(childPosition);
        if (child != null) {
            int distance = getCenterOfGallery() - android.widget.Gallery.getCenterOfView(child);
            mFlingRunnable.startUsingDistance(distance);
            return true;
        }
        return false;
    }

    @java.lang.Override
    void setSelectedPositionInt(int position) {
        super.setSelectedPositionInt(position);
        // Updates any metadata we keep about the selected item.
        updateSelectedItemMetadata();
    }

    private void updateSelectedItemMetadata() {
        android.view.View oldSelectedChild = mSelectedChild;
        android.view.View child = mSelectedChild = getChildAt(mSelectedPosition - mFirstPosition);
        if (child == null) {
            return;
        }
        child.setSelected(true);
        child.setFocusable(true);
        if (hasFocus()) {
            child.requestFocus();
        }
        // We unfocus the old child down here so the above hasFocus check
        // returns true
        if ((oldSelectedChild != null) && (oldSelectedChild != child)) {
            // Make sure its drawable state doesn't contain 'selected'
            oldSelectedChild.setSelected(false);
            // Make sure it is not focusable anymore, since otherwise arrow keys
            // can make this one be focused
            oldSelectedChild.setFocusable(false);
        }
    }

    /**
     * Describes how the child views are aligned.
     *
     * @param gravity
     * 		
     * @unknown ref android.R.styleable#Gallery_gravity
     */
    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            mGravity = gravity;
            requestLayout();
        }
    }

    @java.lang.Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int selectedIndex = mSelectedPosition - mFirstPosition;
        // Just to be safe
        if (selectedIndex < 0)
            return i;

        if (i == (childCount - 1)) {
            // Draw the selected child last
            return selectedIndex;
        } else
            if (i >= selectedIndex) {
                // Move the children after the selected child earlier one
                return i + 1;
            } else {
                // Keep the children before the selected child the same
                return i;
            }

    }

    @java.lang.Override
    protected void onFocusChanged(boolean gainFocus, int direction, android.graphics.Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        /* The gallery shows focus by focusing the selected item. So, give
        focus to our selected item instead. We steal keys from our
        selected item elsewhere.
         */
        if (gainFocus && (mSelectedChild != null)) {
            mSelectedChild.requestFocus(direction);
            mSelectedChild.setSelected(true);
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.Gallery.class.getName();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onInitializeAccessibilityNodeInfoInternal(android.view.accessibility.AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        info.setScrollable(mItemCount > 1);
        if (isEnabled()) {
            if ((mItemCount > 0) && (mSelectedPosition < (mItemCount - 1))) {
                info.addAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }
            if ((isEnabled() && (mItemCount > 0)) && (mSelectedPosition > 0)) {
                info.addAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean performAccessibilityActionInternal(int action, android.os.Bundle arguments) {
        if (super.performAccessibilityActionInternal(action, arguments)) {
            return true;
        }
        switch (action) {
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_FORWARD :
                {
                    if ((isEnabled() && (mItemCount > 0)) && (mSelectedPosition < (mItemCount - 1))) {
                        final int currentChildIndex = mSelectedPosition - mFirstPosition;
                        return scrollToChild(currentChildIndex + 1);
                    }
                }
                return false;
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD :
                {
                    if ((isEnabled() && (mItemCount > 0)) && (mSelectedPosition > 0)) {
                        final int currentChildIndex = mSelectedPosition - mFirstPosition;
                        return scrollToChild(currentChildIndex - 1);
                    }
                }
                return false;
        }
        return false;
    }

    /**
     * Responsible for fling behavior. Use {@link #startUsingVelocity(int)} to
     * initiate a fling. Each frame of the fling is handled in {@link #run()}.
     * A FlingRunnable will keep re-posting itself until the fling is done.
     */
    private class FlingRunnable implements java.lang.Runnable {
        /**
         * Tracks the decay of a fling scroll
         */
        private android.widget.Scroller mScroller;

        /**
         * X value reported by mScroller on the previous fling
         */
        private int mLastFlingX;

        public FlingRunnable() {
            mScroller = new android.widget.Scroller(getContext());
        }

        private void startCommon() {
            // Remove any pending flings
            removeCallbacks(this);
        }

        @android.annotation.UnsupportedAppUsage
        public void startUsingVelocity(int initialVelocity) {
            if (initialVelocity == 0)
                return;

            startCommon();
            int initialX = (initialVelocity < 0) ? java.lang.Integer.MAX_VALUE : 0;
            mLastFlingX = initialX;
            mScroller.fling(initialX, 0, initialVelocity, 0, 0, java.lang.Integer.MAX_VALUE, 0, java.lang.Integer.MAX_VALUE);
            post(this);
        }

        public void startUsingDistance(int distance) {
            if (distance == 0)
                return;

            startCommon();
            mLastFlingX = 0;
            mScroller.startScroll(0, 0, -distance, 0, mAnimationDuration);
            post(this);
        }

        public void stop(boolean scrollIntoSlots) {
            removeCallbacks(this);
            endFling(scrollIntoSlots);
        }

        private void endFling(boolean scrollIntoSlots) {
            /* Force the scroller's status to finished (without setting its
            position to the end)
             */
            mScroller.forceFinished(true);
            if (scrollIntoSlots)
                scrollIntoSlots();

        }

        @java.lang.Override
        public void run() {
            if (mItemCount == 0) {
                endFling(true);
                return;
            }
            mShouldStopFling = false;
            final android.widget.Scroller scroller = mScroller;
            boolean more = scroller.computeScrollOffset();
            final int x = scroller.getCurrX();
            // Flip sign to convert finger direction to list items direction
            // (e.g. finger moving down means list is moving towards the top)
            int delta = mLastFlingX - x;
            // Pretend that each frame of a fling scroll is a touch scroll
            if (delta > 0) {
                // Moving towards the left. Use leftmost view as mDownTouchPosition
                mDownTouchPosition = (mIsRtl) ? (mFirstPosition + getChildCount()) - 1 : mFirstPosition;
                // Don't fling more than 1 screen
                delta = java.lang.Math.min(((getWidth() - mPaddingLeft) - mPaddingRight) - 1, delta);
            } else {
                // Moving towards the right. Use rightmost view as mDownTouchPosition
                int offsetToLast = getChildCount() - 1;
                mDownTouchPosition = (mIsRtl) ? mFirstPosition : (mFirstPosition + getChildCount()) - 1;
                // Don't fling more than 1 screen
                delta = java.lang.Math.max(-(((getWidth() - mPaddingRight) - mPaddingLeft) - 1), delta);
            }
            trackMotionScroll(delta);
            if (more && (!mShouldStopFling)) {
                mLastFlingX = x;
                post(this);
            } else {
                endFling(true);
            }
        }
    }

    /**
     * Gallery extends LayoutParams to provide a place to hold current
     * Transformation information along with previous position/transformation
     * info.
     */
    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        public LayoutParams(android.content.Context c, android.util.AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}

