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
 * An interaction behavior plugin for a child view of {@link CoordinatorLayout} to make it work as
 * a bottom sheet.
 */
public class BottomSheetBehavior<V extends android.view.View> extends android.support.design.widget.CoordinatorLayout.Behavior<V> {
    /**
     * Callback for monitoring events about bottom sheets.
     */
    public static abstract class BottomSheetCallback {
        /**
         * Called when the bottom sheet changes its state.
         *
         * @param bottomSheet
         * 		The bottom sheet view.
         * @param newState
         * 		The new state. This will be one of {@link #STATE_DRAGGING},
         * 		{@link #STATE_SETTLING}, {@link #STATE_EXPANDED},
         * 		{@link #STATE_COLLAPSED}, or {@link #STATE_HIDDEN}.
         */
        public abstract void onStateChanged(@android.support.annotation.NonNull
        android.view.View bottomSheet, @android.support.design.widget.BottomSheetBehavior.State
        int newState);

        /**
         * Called when the bottom sheet is being dragged.
         *
         * @param bottomSheet
         * 		The bottom sheet view.
         * @param slideOffset
         * 		The new offset of this bottom sheet within [-1,1] range. Offset
         * 		increases as this bottom sheet is moving upward. From 0 to 1 the sheet
         * 		is between collapsed and expanded states and from -1 to 0 it is
         * 		between hidden and collapsed states.
         */
        public abstract void onSlide(@android.support.annotation.NonNull
        android.view.View bottomSheet, float slideOffset);
    }

    /**
     * The bottom sheet is dragging.
     */
    public static final int STATE_DRAGGING = 1;

    /**
     * The bottom sheet is settling.
     */
    public static final int STATE_SETTLING = 2;

    /**
     * The bottom sheet is expanded.
     */
    public static final int STATE_EXPANDED = 3;

    /**
     * The bottom sheet is collapsed.
     */
    public static final int STATE_COLLAPSED = 4;

    /**
     * The bottom sheet is hidden.
     */
    public static final int STATE_HIDDEN = 5;

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.IntDef({ android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED, android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED, android.support.design.widget.BottomSheetBehavior.STATE_DRAGGING, android.support.design.widget.BottomSheetBehavior.STATE_SETTLING, android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface State {}

    /**
     * Peek at the 16:9 ratio keyline of its parent.
     *
     * <p>This can be used as a parameter for {@link #setPeekHeight(int)}.
     * {@link #getPeekHeight()} will return this when the value is set.</p>
     */
    public static final int PEEK_HEIGHT_AUTO = -1;

    private static final float HIDE_THRESHOLD = 0.5F;

    private static final float HIDE_FRICTION = 0.1F;

    private float mMaximumVelocity;

    private int mPeekHeight;

    private boolean mPeekHeightAuto;

    private int mPeekHeightMin;

    int mMinOffset;

    int mMaxOffset;

    boolean mHideable;

    private boolean mSkipCollapsed;

    @android.support.design.widget.BottomSheetBehavior.State
    int mState = android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;

    android.support.v4.widget.ViewDragHelper mViewDragHelper;

    private boolean mIgnoreEvents;

    private int mLastNestedScrollDy;

    private boolean mNestedScrolled;

    int mParentHeight;

    java.lang.ref.WeakReference<V> mViewRef;

    java.lang.ref.WeakReference<android.view.View> mNestedScrollingChildRef;

    private android.support.design.widget.BottomSheetBehavior.BottomSheetCallback mCallback;

    private android.view.VelocityTracker mVelocityTracker;

    int mActivePointerId;

    private int mInitialY;

    boolean mTouchingScrollingChild;

    /**
     * Default constructor for instantiating BottomSheetBehaviors.
     */
    public BottomSheetBehavior() {
    }

    /**
     * Default constructor for inflating BottomSheetBehaviors from layout.
     *
     * @param context
     * 		The {@link Context}.
     * @param attrs
     * 		The {@link AttributeSet}.
     */
    public BottomSheetBehavior(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomSheetBehavior_Layout);
        android.util.TypedValue value = a.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
        if ((value != null) && (value.data == android.support.design.widget.BottomSheetBehavior.PEEK_HEIGHT_AUTO)) {
            setPeekHeight(value.data);
        } else {
            setPeekHeight(a.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, android.support.design.widget.BottomSheetBehavior.PEEK_HEIGHT_AUTO));
        }
        setHideable(a.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
        setSkipCollapsed(a.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
        a.recycle();
        android.view.ViewConfiguration configuration = android.view.ViewConfiguration.get(context);
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState(android.support.design.widget.CoordinatorLayout parent, V child) {
        return new android.support.design.widget.BottomSheetBehavior.SavedState(super.onSaveInstanceState(parent, child), mState);
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.support.design.widget.CoordinatorLayout parent, V child, android.os.Parcelable state) {
        android.support.design.widget.BottomSheetBehavior.SavedState ss = ((android.support.design.widget.BottomSheetBehavior.SavedState) (state));
        super.onRestoreInstanceState(parent, child, ss.getSuperState());
        // Intermediate states are restored as collapsed state
        if ((ss.state == android.support.design.widget.BottomSheetBehavior.STATE_DRAGGING) || (ss.state == android.support.design.widget.BottomSheetBehavior.STATE_SETTLING)) {
            mState = android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
        } else {
            mState = ss.state;
        }
    }

    @java.lang.Override
    public boolean onLayoutChild(android.support.design.widget.CoordinatorLayout parent, V child, int layoutDirection) {
        if (android.support.v4.view.ViewCompat.getFitsSystemWindows(parent) && (!android.support.v4.view.ViewCompat.getFitsSystemWindows(child))) {
            android.support.v4.view.ViewCompat.setFitsSystemWindows(child, true);
        }
        int savedTop = child.getTop();
        // First let the parent lay it out
        parent.onLayoutChild(child, layoutDirection);
        // Offset the bottom sheet
        mParentHeight = parent.getHeight();
        int peekHeight;
        if (mPeekHeightAuto) {
            if (mPeekHeightMin == 0) {
                mPeekHeightMin = parent.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min);
            }
            peekHeight = java.lang.Math.max(mPeekHeightMin, mParentHeight - ((parent.getWidth() * 9) / 16));
        } else {
            peekHeight = mPeekHeight;
        }
        mMinOffset = java.lang.Math.max(0, mParentHeight - child.getHeight());
        mMaxOffset = java.lang.Math.max(mParentHeight - peekHeight, mMinOffset);
        if (mState == android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED) {
            android.support.v4.view.ViewCompat.offsetTopAndBottom(child, mMinOffset);
        } else
            if (mHideable && (mState == android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN)) {
                android.support.v4.view.ViewCompat.offsetTopAndBottom(child, mParentHeight);
            } else
                if (mState == android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED) {
                    android.support.v4.view.ViewCompat.offsetTopAndBottom(child, mMaxOffset);
                } else
                    if ((mState == android.support.design.widget.BottomSheetBehavior.STATE_DRAGGING) || (mState == android.support.design.widget.BottomSheetBehavior.STATE_SETTLING)) {
                        android.support.v4.view.ViewCompat.offsetTopAndBottom(child, savedTop - child.getTop());
                    }



        if (mViewDragHelper == null) {
            mViewDragHelper = android.support.v4.widget.ViewDragHelper.create(parent, mDragCallback);
        }
        mViewRef = new java.lang.ref.WeakReference<>(child);
        mNestedScrollingChildRef = new java.lang.ref.WeakReference<>(findScrollingChild(child));
        return true;
    }

    @java.lang.Override
    public boolean onInterceptTouchEvent(android.support.design.widget.CoordinatorLayout parent, V child, android.view.MotionEvent event) {
        if (!child.isShown()) {
            mIgnoreEvents = true;
            return false;
        }
        int action = android.support.v4.view.MotionEventCompat.getActionMasked(event);
        // Record the velocity
        if (action == android.view.MotionEvent.ACTION_DOWN) {
            reset();
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = android.view.VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (action) {
            case android.view.MotionEvent.ACTION_UP :
            case android.view.MotionEvent.ACTION_CANCEL :
                mTouchingScrollingChild = false;
                mActivePointerId = android.view.MotionEvent.INVALID_POINTER_ID;
                // Reset the ignore flag
                if (mIgnoreEvents) {
                    mIgnoreEvents = false;
                    return false;
                }
                break;
            case android.view.MotionEvent.ACTION_DOWN :
                int initialX = ((int) (event.getX()));
                mInitialY = ((int) (event.getY()));
                android.view.View scroll = mNestedScrollingChildRef.get();
                if ((scroll != null) && parent.isPointInChildBounds(scroll, initialX, mInitialY)) {
                    mActivePointerId = event.getPointerId(event.getActionIndex());
                    mTouchingScrollingChild = true;
                }
                mIgnoreEvents = (mActivePointerId == android.view.MotionEvent.INVALID_POINTER_ID) && (!parent.isPointInChildBounds(child, initialX, mInitialY));
                break;
        }
        if ((!mIgnoreEvents) && mViewDragHelper.shouldInterceptTouchEvent(event)) {
            return true;
        }
        // We have to handle cases that the ViewDragHelper does not capture the bottom sheet because
        // it is not the top most view of its parent. This is not necessary when the touch event is
        // happening over the scrolling content as nested scrolling logic handles that case.
        android.view.View scroll = mNestedScrollingChildRef.get();
        return (((((action == android.view.MotionEvent.ACTION_MOVE) && (scroll != null)) && (!mIgnoreEvents)) && (mState != android.support.design.widget.BottomSheetBehavior.STATE_DRAGGING)) && (!parent.isPointInChildBounds(scroll, ((int) (event.getX())), ((int) (event.getY()))))) && (java.lang.Math.abs(mInitialY - event.getY()) > mViewDragHelper.getTouchSlop());
    }

    @java.lang.Override
    public boolean onTouchEvent(android.support.design.widget.CoordinatorLayout parent, V child, android.view.MotionEvent event) {
        if (!child.isShown()) {
            return false;
        }
        int action = android.support.v4.view.MotionEventCompat.getActionMasked(event);
        if ((mState == android.support.design.widget.BottomSheetBehavior.STATE_DRAGGING) && (action == android.view.MotionEvent.ACTION_DOWN)) {
            return true;
        }
        mViewDragHelper.processTouchEvent(event);
        // Record the velocity
        if (action == android.view.MotionEvent.ACTION_DOWN) {
            reset();
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = android.view.VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        // The ViewDragHelper tries to capture only the top-most View. We have to explicitly tell it
        // to capture the bottom sheet in case it is not captured and the touch slop is passed.
        if ((action == android.view.MotionEvent.ACTION_MOVE) && (!mIgnoreEvents)) {
            if (java.lang.Math.abs(mInitialY - event.getY()) > mViewDragHelper.getTouchSlop()) {
                mViewDragHelper.captureChildView(child, event.getPointerId(event.getActionIndex()));
            }
        }
        return !mIgnoreEvents;
    }

    @java.lang.Override
    public boolean onStartNestedScroll(android.support.design.widget.CoordinatorLayout coordinatorLayout, V child, android.view.View directTargetChild, android.view.View target, int nestedScrollAxes) {
        mLastNestedScrollDy = 0;
        mNestedScrolled = false;
        return (nestedScrollAxes & android.support.v4.view.ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @java.lang.Override
    public void onNestedPreScroll(android.support.design.widget.CoordinatorLayout coordinatorLayout, V child, android.view.View target, int dx, int dy, int[] consumed) {
        android.view.View scrollingChild = mNestedScrollingChildRef.get();
        if (target != scrollingChild) {
            return;
        }
        int currentTop = child.getTop();
        int newTop = currentTop - dy;
        if (dy > 0) {
            // Upward
            if (newTop < mMinOffset) {
                consumed[1] = currentTop - mMinOffset;
                android.support.v4.view.ViewCompat.offsetTopAndBottom(child, -consumed[1]);
                setStateInternal(android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED);
            } else {
                consumed[1] = dy;
                android.support.v4.view.ViewCompat.offsetTopAndBottom(child, -dy);
                setStateInternal(android.support.design.widget.BottomSheetBehavior.STATE_DRAGGING);
            }
        } else
            if (dy < 0) {
                // Downward
                if (!android.support.v4.view.ViewCompat.canScrollVertically(target, -1)) {
                    if ((newTop <= mMaxOffset) || mHideable) {
                        consumed[1] = dy;
                        android.support.v4.view.ViewCompat.offsetTopAndBottom(child, -dy);
                        setStateInternal(android.support.design.widget.BottomSheetBehavior.STATE_DRAGGING);
                    } else {
                        consumed[1] = currentTop - mMaxOffset;
                        android.support.v4.view.ViewCompat.offsetTopAndBottom(child, -consumed[1]);
                        setStateInternal(android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }
            }

        dispatchOnSlide(child.getTop());
        mLastNestedScrollDy = dy;
        mNestedScrolled = true;
    }

    @java.lang.Override
    public void onStopNestedScroll(android.support.design.widget.CoordinatorLayout coordinatorLayout, V child, android.view.View target) {
        if (child.getTop() == mMinOffset) {
            setStateInternal(android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED);
            return;
        }
        if ((target != mNestedScrollingChildRef.get()) || (!mNestedScrolled)) {
            return;
        }
        int top;
        int targetState;
        if (mLastNestedScrollDy > 0) {
            top = mMinOffset;
            targetState = android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
        } else
            if (mHideable && shouldHide(child, getYVelocity())) {
                top = mParentHeight;
                targetState = android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN;
            } else
                if (mLastNestedScrollDy == 0) {
                    int currentTop = child.getTop();
                    if (java.lang.Math.abs(currentTop - mMinOffset) < java.lang.Math.abs(currentTop - mMaxOffset)) {
                        top = mMinOffset;
                        targetState = android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
                    } else {
                        top = mMaxOffset;
                        targetState = android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
                    }
                } else {
                    top = mMaxOffset;
                    targetState = android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
                }


        if (mViewDragHelper.smoothSlideViewTo(child, child.getLeft(), top)) {
            setStateInternal(android.support.design.widget.BottomSheetBehavior.STATE_SETTLING);
            android.support.v4.view.ViewCompat.postOnAnimation(child, new SettleRunnable(child, targetState));
        } else {
            setStateInternal(targetState);
        }
        mNestedScrolled = false;
    }

    @java.lang.Override
    public boolean onNestedPreFling(android.support.design.widget.CoordinatorLayout coordinatorLayout, V child, android.view.View target, float velocityX, float velocityY) {
        return (target == mNestedScrollingChildRef.get()) && ((mState != android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED) || super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY));
    }

    /**
     * Sets the height of the bottom sheet when it is collapsed.
     *
     * @param peekHeight
     * 		The height of the collapsed bottom sheet in pixels, or
     * 		{@link #PEEK_HEIGHT_AUTO} to configure the sheet to peek automatically
     * 		at 16:9 ratio keyline.
     * @unknown ref android.support.design.R.styleable#BottomSheetBehavior_Layout_behavior_peekHeight
     */
    public final void setPeekHeight(int peekHeight) {
        boolean layout = false;
        if (peekHeight == android.support.design.widget.BottomSheetBehavior.PEEK_HEIGHT_AUTO) {
            if (!mPeekHeightAuto) {
                mPeekHeightAuto = true;
                layout = true;
            }
        } else
            if (mPeekHeightAuto || (mPeekHeight != peekHeight)) {
                mPeekHeightAuto = false;
                mPeekHeight = java.lang.Math.max(0, peekHeight);
                mMaxOffset = mParentHeight - peekHeight;
                layout = true;
            }

        if ((layout && (mState == android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED)) && (mViewRef != null)) {
            V view = mViewRef.get();
            if (view != null) {
                view.requestLayout();
            }
        }
    }

    /**
     * Gets the height of the bottom sheet when it is collapsed.
     *
     * @return The height of the collapsed bottom sheet in pixels, or {@link #PEEK_HEIGHT_AUTO}
    if the sheet is configured to peek automatically at 16:9 ratio keyline
     * @unknown ref android.support.design.R.styleable#BottomSheetBehavior_Layout_behavior_peekHeight
     */
    public final int getPeekHeight() {
        return mPeekHeightAuto ? android.support.design.widget.BottomSheetBehavior.PEEK_HEIGHT_AUTO : mPeekHeight;
    }

    /**
     * Sets whether this bottom sheet can hide when it is swiped down.
     *
     * @param hideable
     * 		{@code true} to make this bottom sheet hideable.
     * @unknown ref android.support.design.R.styleable#BottomSheetBehavior_Layout_behavior_hideable
     */
    public void setHideable(boolean hideable) {
        mHideable = hideable;
    }

    /**
     * Gets whether this bottom sheet can hide when it is swiped down.
     *
     * @return {@code true} if this bottom sheet can hide.
     * @unknown ref android.support.design.R.styleable#BottomSheetBehavior_Layout_behavior_hideable
     */
    public boolean isHideable() {
        return mHideable;
    }

    /**
     * Sets whether this bottom sheet should skip the collapsed state when it is being hidden
     * after it is expanded once. Setting this to true has no effect unless the sheet is hideable.
     *
     * @param skipCollapsed
     * 		True if the bottom sheet should skip the collapsed state.
     * @unknown ref android.support.design.R.styleable#BottomSheetBehavior_Layout_behavior_skipCollapsed
     */
    public void setSkipCollapsed(boolean skipCollapsed) {
        mSkipCollapsed = skipCollapsed;
    }

    /**
     * Sets whether this bottom sheet should skip the collapsed state when it is being hidden
     * after it is expanded once.
     *
     * @return Whether the bottom sheet should skip the collapsed state.
     * @unknown ref android.support.design.R.styleable#BottomSheetBehavior_Layout_behavior_skipCollapsed
     */
    public boolean getSkipCollapsed() {
        return mSkipCollapsed;
    }

    /**
     * Sets a callback to be notified of bottom sheet events.
     *
     * @param callback
     * 		The callback to notify when bottom sheet events occur.
     */
    public void setBottomSheetCallback(android.support.design.widget.BottomSheetBehavior.BottomSheetCallback callback) {
        mCallback = callback;
    }

    /**
     * Sets the state of the bottom sheet. The bottom sheet will transition to that state with
     * animation.
     *
     * @param state
     * 		One of {@link #STATE_COLLAPSED}, {@link #STATE_EXPANDED}, or
     * 		{@link #STATE_HIDDEN}.
     */
    public final void setState(@android.support.design.widget.BottomSheetBehavior.State
    final int state) {
        if (state == mState) {
            return;
        }
        if (mViewRef == null) {
            // The view is not laid out yet; modify mState and let onLayoutChild handle it later
            if (((state == android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED) || (state == android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED)) || (mHideable && (state == android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN))) {
                mState = state;
            }
            return;
        }
        final V child = mViewRef.get();
        if (child == null) {
            return;
        }
        // Start the animation; wait until a pending layout if there is one.
        android.view.ViewParent parent = child.getParent();
        if (((parent != null) && parent.isLayoutRequested()) && android.support.v4.view.ViewCompat.isAttachedToWindow(child)) {
            child.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    startSettlingAnimation(child, state);
                }
            });
        } else {
            startSettlingAnimation(child, state);
        }
    }

    /**
     * Gets the current state of the bottom sheet.
     *
     * @return One of {@link #STATE_EXPANDED}, {@link #STATE_COLLAPSED}, {@link #STATE_DRAGGING},
    and {@link #STATE_SETTLING}.
     */
    @android.support.design.widget.BottomSheetBehavior.State
    public final int getState() {
        return mState;
    }

    void setStateInternal(@android.support.design.widget.BottomSheetBehavior.State
    int state) {
        if (mState == state) {
            return;
        }
        mState = state;
        android.view.View bottomSheet = mViewRef.get();
        if ((bottomSheet != null) && (mCallback != null)) {
            mCallback.onStateChanged(bottomSheet, state);
        }
    }

    private void reset() {
        mActivePointerId = android.support.v4.widget.ViewDragHelper.INVALID_POINTER;
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    boolean shouldHide(android.view.View child, float yvel) {
        if (mSkipCollapsed) {
            return true;
        }
        if (child.getTop() < mMaxOffset) {
            // It should not hide, but collapse.
            return false;
        }
        final float newTop = child.getTop() + (yvel * android.support.design.widget.BottomSheetBehavior.HIDE_FRICTION);
        return (java.lang.Math.abs(newTop - mMaxOffset) / ((float) (mPeekHeight))) > android.support.design.widget.BottomSheetBehavior.HIDE_THRESHOLD;
    }

    private android.view.View findScrollingChild(android.view.View view) {
        if (view instanceof android.support.v4.view.NestedScrollingChild) {
            return view;
        }
        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup group = ((android.view.ViewGroup) (view));
            for (int i = 0, count = group.getChildCount(); i < count; i++) {
                android.view.View scrollingChild = findScrollingChild(group.getChildAt(i));
                if (scrollingChild != null) {
                    return scrollingChild;
                }
            }
        }
        return null;
    }

    private float getYVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
        return android.support.v4.view.VelocityTrackerCompat.getYVelocity(mVelocityTracker, mActivePointerId);
    }

    void startSettlingAnimation(android.view.View child, int state) {
        int top;
        if (state == android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED) {
            top = mMaxOffset;
        } else
            if (state == android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED) {
                top = mMinOffset;
            } else
                if (mHideable && (state == android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN)) {
                    top = mParentHeight;
                } else {
                    throw new java.lang.IllegalArgumentException("Illegal state argument: " + state);
                }


        setStateInternal(android.support.design.widget.BottomSheetBehavior.STATE_SETTLING);
        if (mViewDragHelper.smoothSlideViewTo(child, child.getLeft(), top)) {
            android.support.v4.view.ViewCompat.postOnAnimation(child, new SettleRunnable(child, state));
        }
    }

    private final android.support.v4.widget.ViewDragHelper.Callback mDragCallback = new android.support.v4.widget.ViewDragHelper.Callback() {
        @java.lang.Override
        public boolean tryCaptureView(android.view.View child, int pointerId) {
            if (mState == android.support.design.widget.BottomSheetBehavior.STATE_DRAGGING) {
                return false;
            }
            if (mTouchingScrollingChild) {
                return false;
            }
            if ((mState == android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED) && (mActivePointerId == pointerId)) {
                android.view.View scroll = mNestedScrollingChildRef.get();
                if ((scroll != null) && android.support.v4.view.ViewCompat.canScrollVertically(scroll, -1)) {
                    // Let the content scroll up
                    return false;
                }
            }
            return (mViewRef != null) && (mViewRef.get() == child);
        }

        @java.lang.Override
        public void onViewPositionChanged(android.view.View changedView, int left, int top, int dx, int dy) {
            dispatchOnSlide(top);
        }

        @java.lang.Override
        public void onViewDragStateChanged(int state) {
            if (state == android.support.v4.widget.ViewDragHelper.STATE_DRAGGING) {
                setStateInternal(android.support.design.widget.BottomSheetBehavior.STATE_DRAGGING);
            }
        }

        @java.lang.Override
        public void onViewReleased(android.view.View releasedChild, float xvel, float yvel) {
            int top;
            @android.support.design.widget.BottomSheetBehavior.State
            int targetState;
            if (yvel < 0) {
                // Moving up
                top = mMinOffset;
                targetState = android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
            } else
                if (mHideable && shouldHide(releasedChild, yvel)) {
                    top = mParentHeight;
                    targetState = android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN;
                } else
                    if (yvel == 0.0F) {
                        int currentTop = releasedChild.getTop();
                        if (java.lang.Math.abs(currentTop - mMinOffset) < java.lang.Math.abs(currentTop - mMaxOffset)) {
                            top = mMinOffset;
                            targetState = android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
                        } else {
                            top = mMaxOffset;
                            targetState = android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
                        }
                    } else {
                        top = mMaxOffset;
                        targetState = android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
                    }


            if (mViewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top)) {
                setStateInternal(android.support.design.widget.BottomSheetBehavior.STATE_SETTLING);
                android.support.v4.view.ViewCompat.postOnAnimation(releasedChild, new SettleRunnable(releasedChild, targetState));
            } else {
                setStateInternal(targetState);
            }
        }

        @java.lang.Override
        public int clampViewPositionVertical(android.view.View child, int top, int dy) {
            return android.support.design.widget.MathUtils.constrain(top, mMinOffset, mHideable ? mParentHeight : mMaxOffset);
        }

        @java.lang.Override
        public int clampViewPositionHorizontal(android.view.View child, int left, int dx) {
            return child.getLeft();
        }

        @java.lang.Override
        public int getViewVerticalDragRange(android.view.View child) {
            if (mHideable) {
                return mParentHeight - mMinOffset;
            } else {
                return mMaxOffset - mMinOffset;
            }
        }
    };

    void dispatchOnSlide(int top) {
        android.view.View bottomSheet = mViewRef.get();
        if ((bottomSheet != null) && (mCallback != null)) {
            if (top > mMaxOffset) {
                mCallback.onSlide(bottomSheet, ((float) (mMaxOffset - top)) / (mParentHeight - mMaxOffset));
            } else {
                mCallback.onSlide(bottomSheet, ((float) (mMaxOffset - top)) / (mMaxOffset - mMinOffset));
            }
        }
    }

    @android.support.annotation.VisibleForTesting
    int getPeekHeightMin() {
        return mPeekHeightMin;
    }

    private class SettleRunnable implements java.lang.Runnable {
        private final android.view.View mView;

        @android.support.design.widget.BottomSheetBehavior.State
        private final int mTargetState;

        SettleRunnable(android.view.View view, @android.support.design.widget.BottomSheetBehavior.State
        int targetState) {
            mView = view;
            mTargetState = targetState;
        }

        @java.lang.Override
        public void run() {
            if ((mViewDragHelper != null) && mViewDragHelper.continueSettling(true)) {
                android.support.v4.view.ViewCompat.postOnAnimation(mView, this);
            } else {
                setStateInternal(mTargetState);
            }
        }
    }

    protected static class SavedState extends android.support.v4.view.AbsSavedState {
        @android.support.design.widget.BottomSheetBehavior.State
        final int state;

        public SavedState(android.os.Parcel source) {
            this(source, null);
        }

        public SavedState(android.os.Parcel source, java.lang.ClassLoader loader) {
            super(source, loader);
            // noinspection ResourceType
            state = source.readInt();
        }

        public SavedState(android.os.Parcelable superState, @android.support.design.widget.BottomSheetBehavior.State
        int state) {
            super(superState);
            this.state = state;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(state);
        }

        public static final android.os.Parcelable.Creator<android.support.design.widget.BottomSheetBehavior.SavedState> CREATOR = android.support.v4.os.ParcelableCompat.newCreator(new android.support.v4.os.ParcelableCompatCreatorCallbacks<android.support.design.widget.BottomSheetBehavior.SavedState>() {
            @java.lang.Override
            public android.support.design.widget.BottomSheetBehavior.SavedState createFromParcel(android.os.Parcel in, java.lang.ClassLoader loader) {
                return new android.support.design.widget.BottomSheetBehavior.SavedState(in, loader);
            }

            @java.lang.Override
            public android.support.design.widget.BottomSheetBehavior.SavedState[] newArray(int size) {
                return new android.support.design.widget.BottomSheetBehavior.SavedState[size];
            }
        });
    }

    /**
     * A utility function to get the {@link BottomSheetBehavior} associated with the {@code view}.
     *
     * @param view
     * 		The {@link View} with {@link BottomSheetBehavior}.
     * @return The {@link BottomSheetBehavior} associated with the {@code view}.
     */
    @java.lang.SuppressWarnings("unchecked")
    public static <V extends android.view.View> android.support.design.widget.BottomSheetBehavior<V> from(V view) {
        android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof android.support.design.widget.CoordinatorLayout.LayoutParams)) {
            throw new java.lang.IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        android.support.design.widget.CoordinatorLayout.Behavior behavior = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (params)).getBehavior();
        if (!(behavior instanceof android.support.design.widget.BottomSheetBehavior)) {
            throw new java.lang.IllegalArgumentException("The view is not associated with BottomSheetBehavior");
        }
        return ((android.support.design.widget.BottomSheetBehavior<V>) (behavior));
    }
}

