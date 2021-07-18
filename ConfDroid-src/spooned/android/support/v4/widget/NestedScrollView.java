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
package android.support.v4.widget;


/**
 * NestedScrollView is just like {@link android.widget.ScrollView}, but it supports acting
 * as both a nested scrolling parent and child on both new and old versions of Android.
 * Nested scrolling is enabled by default.
 */
public class NestedScrollView extends android.widget.FrameLayout implements android.support.v4.view.NestedScrollingChild , android.support.v4.view.NestedScrollingParent , android.support.v4.view.ScrollingView {
    static final int ANIMATED_SCROLL_GAP = 250;

    static final float MAX_SCROLL_FACTOR = 0.5F;

    private static final java.lang.String TAG = "NestedScrollView";

    /**
     * Interface definition for a callback to be invoked when the scroll
     * X or Y positions of a view change.
     *
     * <p>This version of the interface works on all versions of Android, back to API v4.</p>
     *
     * @see #setOnScrollChangeListener(OnScrollChangeListener)
     */
    public interface OnScrollChangeListener {
        /**
         * Called when the scroll position of a view changes.
         *
         * @param v
         * 		The view whose scroll position has changed.
         * @param scrollX
         * 		Current horizontal scroll origin.
         * @param scrollY
         * 		Current vertical scroll origin.
         * @param oldScrollX
         * 		Previous horizontal scroll origin.
         * @param oldScrollY
         * 		Previous vertical scroll origin.
         */
        void onScrollChange(android.support.v4.widget.NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

    private long mLastScroll;

    private final android.graphics.Rect mTempRect = new android.graphics.Rect();

    private android.support.v4.widget.ScrollerCompat mScroller;

    private android.support.v4.widget.EdgeEffectCompat mEdgeGlowTop;

    private android.support.v4.widget.EdgeEffectCompat mEdgeGlowBottom;

    /**
     * Position of the last motion event.
     */
    private int mLastMotionY;

    /**
     * True when the layout has changed but the traversal has not come through yet.
     * Ideally the view hierarchy would keep track of this for us.
     */
    private boolean mIsLayoutDirty = true;

    private boolean mIsLaidOut = false;

    /**
     * The child to give focus to in the event that a child has requested focus while the
     * layout is dirty. This prevents the scroll from being wrong if the child has not been
     * laid out before requesting focus.
     */
    private android.view.View mChildToScrollTo = null;

    /**
     * True if the user is currently dragging this ScrollView around. This is
     * not the same as 'is being flinged', which can be checked by
     * mScroller.isFinished() (flinging begins when the user lifts his finger).
     */
    private boolean mIsBeingDragged = false;

    /**
     * Determines speed during touch scrolling
     */
    private android.view.VelocityTracker mVelocityTracker;

    /**
     * When set to true, the scroll view measure its child to make it fill the currently
     * visible area.
     */
    private boolean mFillViewport;

    /**
     * Whether arrow scrolling is animated.
     */
    private boolean mSmoothScrollingEnabled = true;

    private int mTouchSlop;

    private int mMinimumVelocity;

    private int mMaximumVelocity;

    /**
     * ID of the active pointer. This is used to retain consistency during
     * drags/flings if multiple pointers are used.
     */
    private int mActivePointerId = android.support.v4.widget.NestedScrollView.INVALID_POINTER;

    /**
     * Used during scrolling to retrieve the new offset within the window.
     */
    private final int[] mScrollOffset = new int[2];

    private final int[] mScrollConsumed = new int[2];

    private int mNestedYOffset;

    /**
     * Sentinel value for no current active pointer.
     * Used by {@link #mActivePointerId}.
     */
    private static final int INVALID_POINTER = -1;

    private android.support.v4.widget.NestedScrollView.SavedState mSavedState;

    private static final android.support.v4.widget.NestedScrollView.AccessibilityDelegate ACCESSIBILITY_DELEGATE = new android.support.v4.widget.NestedScrollView.AccessibilityDelegate();

    private static final int[] SCROLLVIEW_STYLEABLE = new int[]{ android.R.attr.fillViewport };

    private final android.support.v4.view.NestedScrollingParentHelper mParentHelper;

    private final android.support.v4.view.NestedScrollingChildHelper mChildHelper;

    private float mVerticalScrollFactor;

    private android.support.v4.widget.NestedScrollView.OnScrollChangeListener mOnScrollChangeListener;

    public NestedScrollView(android.content.Context context) {
        this(context, null);
    }

    public NestedScrollView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initScrollView();
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, android.support.v4.widget.NestedScrollView.SCROLLVIEW_STYLEABLE, defStyleAttr, 0);
        setFillViewport(a.getBoolean(0, false));
        a.recycle();
        mParentHelper = new android.support.v4.view.NestedScrollingParentHelper(this);
        mChildHelper = new android.support.v4.view.NestedScrollingChildHelper(this);
        // ...because why else would you be using this widget?
        setNestedScrollingEnabled(true);
        android.support.v4.view.ViewCompat.setAccessibilityDelegate(this, android.support.v4.widget.NestedScrollView.ACCESSIBILITY_DELEGATE);
    }

    // NestedScrollingChild
    @java.lang.Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @java.lang.Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @java.lang.Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @java.lang.Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @java.lang.Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @java.lang.Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @java.lang.Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @java.lang.Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @java.lang.Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    // NestedScrollingParent
    @java.lang.Override
    public boolean onStartNestedScroll(android.view.View child, android.view.View target, int nestedScrollAxes) {
        return (nestedScrollAxes & android.support.v4.view.ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @java.lang.Override
    public void onNestedScrollAccepted(android.view.View child, android.view.View target, int nestedScrollAxes) {
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
        startNestedScroll(android.support.v4.view.ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    @java.lang.Override
    public void onStopNestedScroll(android.view.View target) {
        mParentHelper.onStopNestedScroll(target);
        stopNestedScroll();
    }

    @java.lang.Override
    public void onNestedScroll(android.view.View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        final int oldScrollY = getScrollY();
        scrollBy(0, dyUnconsumed);
        final int myConsumed = getScrollY() - oldScrollY;
        final int myUnconsumed = dyUnconsumed - myConsumed;
        dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null);
    }

    @java.lang.Override
    public void onNestedPreScroll(android.view.View target, int dx, int dy, int[] consumed) {
        dispatchNestedPreScroll(dx, dy, consumed, null);
    }

    @java.lang.Override
    public boolean onNestedFling(android.view.View target, float velocityX, float velocityY, boolean consumed) {
        if (!consumed) {
            flingWithNestedDispatch(((int) (velocityY)));
            return true;
        }
        return false;
    }

    @java.lang.Override
    public boolean onNestedPreFling(android.view.View target, float velocityX, float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @java.lang.Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }

    // ScrollView import
    public boolean shouldDelayChildPressedState() {
        return true;
    }

    @java.lang.Override
    protected float getTopFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0F;
        }
        final int length = getVerticalFadingEdgeLength();
        final int scrollY = getScrollY();
        if (scrollY < length) {
            return scrollY / ((float) (length));
        }
        return 1.0F;
    }

    @java.lang.Override
    protected float getBottomFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0F;
        }
        final int length = getVerticalFadingEdgeLength();
        final int bottomEdge = getHeight() - getPaddingBottom();
        final int span = (getChildAt(0).getBottom() - getScrollY()) - bottomEdge;
        if (span < length) {
            return span / ((float) (length));
        }
        return 1.0F;
    }

    /**
     *
     *
     * @return The maximum amount this scroll view will scroll in response to
    an arrow event.
     */
    public int getMaxScrollAmount() {
        return ((int) (android.support.v4.widget.NestedScrollView.MAX_SCROLL_FACTOR * getHeight()));
    }

    private void initScrollView() {
        mScroller = android.support.v4.widget.ScrollerCompat.create(getContext(), null);
        setFocusable(true);
        setDescendantFocusability(android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);
        final android.view.ViewConfiguration configuration = android.view.ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @java.lang.Override
    public void addView(android.view.View child) {
        if (getChildCount() > 0) {
            throw new java.lang.IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(child);
    }

    @java.lang.Override
    public void addView(android.view.View child, int index) {
        if (getChildCount() > 0) {
            throw new java.lang.IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(child, index);
    }

    @java.lang.Override
    public void addView(android.view.View child, android.view.ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new java.lang.IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(child, params);
    }

    @java.lang.Override
    public void addView(android.view.View child, int index, android.view.ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new java.lang.IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(child, index, params);
    }

    /**
     * Register a callback to be invoked when the scroll X or Y positions of
     * this view change.
     * <p>This version of the method works on all versions of Android, back to API v4.</p>
     *
     * @param l
     * 		The listener to notify when the scroll X or Y position changes.
     * @see android.view.View#getScrollX()
     * @see android.view.View#getScrollY()
     */
    public void setOnScrollChangeListener(android.support.v4.widget.NestedScrollView.OnScrollChangeListener l) {
        mOnScrollChangeListener = l;
    }

    /**
     *
     *
     * @return Returns true this ScrollView can be scrolled
     */
    private boolean canScroll() {
        android.view.View child = getChildAt(0);
        if (child != null) {
            int childHeight = child.getHeight();
            return getHeight() < ((childHeight + getPaddingTop()) + getPaddingBottom());
        }
        return false;
    }

    /**
     * Indicates whether this ScrollView's content is stretched to fill the viewport.
     *
     * @return True if the content fills the viewport, false otherwise.
     * @unknown name android:fillViewport
     */
    public boolean isFillViewport() {
        return mFillViewport;
    }

    /**
     * Set whether this ScrollView should stretch its content height to fill the viewport or not.
     *
     * @param fillViewport
     * 		True to stretch the content's height to the viewport's
     * 		boundaries, false otherwise.
     * @unknown name android:fillViewport
     */
    public void setFillViewport(boolean fillViewport) {
        if (fillViewport != mFillViewport) {
            mFillViewport = fillViewport;
            requestLayout();
        }
    }

    /**
     *
     *
     * @return Whether arrow scrolling will animate its transition.
     */
    public boolean isSmoothScrollingEnabled() {
        return mSmoothScrollingEnabled;
    }

    /**
     * Set whether arrow scrolling will animate its transition.
     *
     * @param smoothScrollingEnabled
     * 		whether arrow scrolling will animate its transition
     */
    public void setSmoothScrollingEnabled(boolean smoothScrollingEnabled) {
        mSmoothScrollingEnabled = smoothScrollingEnabled;
    }

    @java.lang.Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mFillViewport) {
            return;
        }
        final int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == android.view.View.MeasureSpec.UNSPECIFIED) {
            return;
        }
        if (getChildCount() > 0) {
            final android.view.View child = getChildAt(0);
            int height = getMeasuredHeight();
            if (child.getMeasuredHeight() < height) {
                final android.widget.FrameLayout.LayoutParams lp = ((android.widget.FrameLayout.LayoutParams) (child.getLayoutParams()));
                int childWidthMeasureSpec = android.view.ViewGroup.getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), lp.width);
                height -= getPaddingTop();
                height -= getPaddingBottom();
                int childHeightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(height, android.view.View.MeasureSpec.EXACTLY);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    @java.lang.Override
    public boolean dispatchKeyEvent(android.view.KeyEvent event) {
        // Let the focused view and/or our descendants get the key first
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    /**
     * You can call this function yourself to have the scroll view perform
     * scrolling from a key event, just as if the event had been dispatched to
     * it by the view hierarchy.
     *
     * @param event
     * 		The key event to execute.
     * @return Return true if the event was handled, else false.
     */
    public boolean executeKeyEvent(android.view.KeyEvent event) {
        mTempRect.setEmpty();
        if (!canScroll()) {
            if (isFocused() && (event.getKeyCode() != android.view.KeyEvent.KEYCODE_BACK)) {
                android.view.View currentFocused = findFocus();
                if (currentFocused == this)
                    currentFocused = null;

                android.view.View nextFocused = android.view.FocusFinder.getInstance().findNextFocus(this, currentFocused, android.view.View.FOCUS_DOWN);
                return ((nextFocused != null) && (nextFocused != this)) && nextFocused.requestFocus(android.view.View.FOCUS_DOWN);
            }
            return false;
        }
        boolean handled = false;
        if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case android.view.KeyEvent.KEYCODE_DPAD_UP :
                    if (!event.isAltPressed()) {
                        handled = arrowScroll(android.view.View.FOCUS_UP);
                    } else {
                        handled = fullScroll(android.view.View.FOCUS_UP);
                    }
                    break;
                case android.view.KeyEvent.KEYCODE_DPAD_DOWN :
                    if (!event.isAltPressed()) {
                        handled = arrowScroll(android.view.View.FOCUS_DOWN);
                    } else {
                        handled = fullScroll(android.view.View.FOCUS_DOWN);
                    }
                    break;
                case android.view.KeyEvent.KEYCODE_SPACE :
                    pageScroll(event.isShiftPressed() ? android.view.View.FOCUS_UP : android.view.View.FOCUS_DOWN);
                    break;
            }
        }
        return handled;
    }

    private boolean inChild(int x, int y) {
        if (getChildCount() > 0) {
            final int scrollY = getScrollY();
            final android.view.View child = getChildAt(0);
            return !((((y < (child.getTop() - scrollY)) || (y >= (child.getBottom() - scrollY))) || (x < child.getLeft())) || (x >= child.getRight()));
        }
        return false;
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = android.view.VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = android.view.VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @java.lang.Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept) {
            recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @java.lang.Override
    public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
        /* This method JUST determines whether we want to intercept the motion.
        If we return true, onMotionEvent will be called and we do the actual
        scrolling there.
         */
        /* Shortcut the most recurring case: the user is in the dragging
        state and he is moving his finger.  We want to intercept this
        motion.
         */
        final int action = ev.getAction();
        if ((action == android.view.MotionEvent.ACTION_MOVE) && mIsBeingDragged) {
            return true;
        }
        switch (action & android.support.v4.view.MotionEventCompat.ACTION_MASK) {
            case android.view.MotionEvent.ACTION_MOVE :
                {
                    /* mIsBeingDragged == false, otherwise the shortcut would have caught it. Check
                    whether the user has moved far enough from his original down touch.
                     */
                    /* Locally do absolute value. mLastMotionY is set to the y value
                    of the down event.
                     */
                    final int activePointerId = mActivePointerId;
                    if (activePointerId == android.support.v4.widget.NestedScrollView.INVALID_POINTER) {
                        // If we don't have a valid id, the touch down wasn't on content.
                        break;
                    }
                    final int pointerIndex = ev.findPointerIndex(activePointerId);
                    if (pointerIndex == (-1)) {
                        android.util.Log.e(android.support.v4.widget.NestedScrollView.TAG, ("Invalid pointerId=" + activePointerId) + " in onInterceptTouchEvent");
                        break;
                    }
                    final int y = ((int) (ev.getY(pointerIndex)));
                    final int yDiff = java.lang.Math.abs(y - mLastMotionY);
                    if ((yDiff > mTouchSlop) && ((getNestedScrollAxes() & android.support.v4.view.ViewCompat.SCROLL_AXIS_VERTICAL) == 0)) {
                        mIsBeingDragged = true;
                        mLastMotionY = y;
                        initVelocityTrackerIfNotExists();
                        mVelocityTracker.addMovement(ev);
                        mNestedYOffset = 0;
                        final android.view.ViewParent parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                    break;
                }
            case android.view.MotionEvent.ACTION_DOWN :
                {
                    final int y = ((int) (ev.getY()));
                    if (!inChild(((int) (ev.getX())), ((int) (y)))) {
                        mIsBeingDragged = false;
                        recycleVelocityTracker();
                        break;
                    }
                    /* Remember location of down touch.
                    ACTION_DOWN always refers to pointer index 0.
                     */
                    mLastMotionY = y;
                    mActivePointerId = ev.getPointerId(0);
                    initOrResetVelocityTracker();
                    mVelocityTracker.addMovement(ev);
                    /* If being flinged and user touches the screen, initiate drag;
                    otherwise don't. mScroller.isFinished should be false when
                    being flinged. We need to call computeScrollOffset() first so that
                    isFinished() is correct.
                     */
                    mScroller.computeScrollOffset();
                    mIsBeingDragged = !mScroller.isFinished();
                    startNestedScroll(android.support.v4.view.ViewCompat.SCROLL_AXIS_VERTICAL);
                    break;
                }
            case android.view.MotionEvent.ACTION_CANCEL :
            case android.view.MotionEvent.ACTION_UP :
                /* Release the drag */
                mIsBeingDragged = false;
                mActivePointerId = android.support.v4.widget.NestedScrollView.INVALID_POINTER;
                recycleVelocityTracker();
                if (mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                    android.support.v4.view.ViewCompat.postInvalidateOnAnimation(this);
                }
                stopNestedScroll();
                break;
            case android.support.v4.view.MotionEventCompat.ACTION_POINTER_UP :
                onSecondaryPointerUp(ev);
                break;
        }
        /* The only time we want to intercept motion events is if we are in the
        drag mode.
         */
        return mIsBeingDragged;
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        initVelocityTrackerIfNotExists();
        android.view.MotionEvent vtev = android.view.MotionEvent.obtain(ev);
        final int actionMasked = android.support.v4.view.MotionEventCompat.getActionMasked(ev);
        if (actionMasked == android.view.MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0;
        }
        vtev.offsetLocation(0, mNestedYOffset);
        switch (actionMasked) {
            case android.view.MotionEvent.ACTION_DOWN :
                {
                    if (getChildCount() == 0) {
                        return false;
                    }
                    if (mIsBeingDragged = !mScroller.isFinished()) {
                        final android.view.ViewParent parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                    /* If being flinged and user touches, stop the fling. isFinished
                    will be false if being flinged.
                     */
                    if (!mScroller.isFinished()) {
                        mScroller.abortAnimation();
                    }
                    // Remember where the motion event started
                    mLastMotionY = ((int) (ev.getY()));
                    mActivePointerId = ev.getPointerId(0);
                    startNestedScroll(android.support.v4.view.ViewCompat.SCROLL_AXIS_VERTICAL);
                    break;
                }
            case android.view.MotionEvent.ACTION_MOVE :
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == (-1)) {
                    android.util.Log.e(android.support.v4.widget.NestedScrollView.TAG, ("Invalid pointerId=" + mActivePointerId) + " in onTouchEvent");
                    break;
                }
                final int y = ((int) (ev.getY(activePointerIndex)));
                int deltaY = mLastMotionY - y;
                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                    deltaY -= mScrollConsumed[1];
                    vtev.offsetLocation(0, mScrollOffset[1]);
                    mNestedYOffset += mScrollOffset[1];
                }
                if ((!mIsBeingDragged) && (java.lang.Math.abs(deltaY) > mTouchSlop)) {
                    final android.view.ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                }
                if (mIsBeingDragged) {
                    // Scroll to follow the motion event
                    mLastMotionY = y - mScrollOffset[1];
                    final int oldY = getScrollY();
                    final int range = getScrollRange();
                    final int overscrollMode = getOverScrollMode();
                    boolean canOverscroll = (overscrollMode == android.view.View.OVER_SCROLL_ALWAYS) || ((overscrollMode == android.view.View.OVER_SCROLL_IF_CONTENT_SCROLLS) && (range > 0));
                    // Calling overScrollByCompat will call onOverScrolled, which
                    // calls onScrollChanged if applicable.
                    if (overScrollByCompat(0, deltaY, 0, getScrollY(), 0, range, 0, 0, true) && (!hasNestedScrollingParent())) {
                        // Break our velocity if we hit a scroll barrier.
                        mVelocityTracker.clear();
                    }
                    final int scrolledDeltaY = getScrollY() - oldY;
                    final int unconsumedY = deltaY - scrolledDeltaY;
                    if (dispatchNestedScroll(0, scrolledDeltaY, 0, unconsumedY, mScrollOffset)) {
                        mLastMotionY -= mScrollOffset[1];
                        vtev.offsetLocation(0, mScrollOffset[1]);
                        mNestedYOffset += mScrollOffset[1];
                    } else
                        if (canOverscroll) {
                            ensureGlows();
                            final int pulledToY = oldY + deltaY;
                            if (pulledToY < 0) {
                                mEdgeGlowTop.onPull(((float) (deltaY)) / getHeight(), ev.getX(activePointerIndex) / getWidth());
                                if (!mEdgeGlowBottom.isFinished()) {
                                    mEdgeGlowBottom.onRelease();
                                }
                            } else
                                if (pulledToY > range) {
                                    mEdgeGlowBottom.onPull(((float) (deltaY)) / getHeight(), 1.0F - (ev.getX(activePointerIndex) / getWidth()));
                                    if (!mEdgeGlowTop.isFinished()) {
                                        mEdgeGlowTop.onRelease();
                                    }
                                }

                            if ((mEdgeGlowTop != null) && ((!mEdgeGlowTop.isFinished()) || (!mEdgeGlowBottom.isFinished()))) {
                                android.support.v4.view.ViewCompat.postInvalidateOnAnimation(this);
                            }
                        }

                }
                break;
            case android.view.MotionEvent.ACTION_UP :
                if (mIsBeingDragged) {
                    final android.view.VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity = ((int) (android.support.v4.view.VelocityTrackerCompat.getYVelocity(velocityTracker, mActivePointerId)));
                    if (java.lang.Math.abs(initialVelocity) > mMinimumVelocity) {
                        flingWithNestedDispatch(-initialVelocity);
                    } else
                        if (mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                            android.support.v4.view.ViewCompat.postInvalidateOnAnimation(this);
                        }

                }
                mActivePointerId = android.support.v4.widget.NestedScrollView.INVALID_POINTER;
                endDrag();
                break;
            case android.view.MotionEvent.ACTION_CANCEL :
                if (mIsBeingDragged && (getChildCount() > 0)) {
                    if (mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                        android.support.v4.view.ViewCompat.postInvalidateOnAnimation(this);
                    }
                }
                mActivePointerId = android.support.v4.widget.NestedScrollView.INVALID_POINTER;
                endDrag();
                break;
            case android.support.v4.view.MotionEventCompat.ACTION_POINTER_DOWN :
                {
                    final int index = android.support.v4.view.MotionEventCompat.getActionIndex(ev);
                    mLastMotionY = ((int) (ev.getY(index)));
                    mActivePointerId = ev.getPointerId(index);
                    break;
                }
            case android.support.v4.view.MotionEventCompat.ACTION_POINTER_UP :
                onSecondaryPointerUp(ev);
                mLastMotionY = ((int) (ev.getY(ev.findPointerIndex(mActivePointerId))));
                break;
        }
        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
    }

    private void onSecondaryPointerUp(android.view.MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & android.support.v4.view.MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> android.support.v4.view.MotionEventCompat.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            // TODO: Make this decision more intelligent.
            final int newPointerIndex = (pointerIndex == 0) ? 1 : 0;
            mLastMotionY = ((int) (ev.getY(newPointerIndex)));
            mActivePointerId = ev.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    public boolean onGenericMotionEvent(android.view.MotionEvent event) {
        if ((event.getSource() & android.support.v4.view.InputDeviceCompat.SOURCE_CLASS_POINTER) != 0) {
            switch (event.getAction()) {
                case android.support.v4.view.MotionEventCompat.ACTION_SCROLL :
                    {
                        if (!mIsBeingDragged) {
                            final float vscroll = android.support.v4.view.MotionEventCompat.getAxisValue(event, android.support.v4.view.MotionEventCompat.AXIS_VSCROLL);
                            if (vscroll != 0) {
                                final int delta = ((int) (vscroll * getVerticalScrollFactorCompat()));
                                final int range = getScrollRange();
                                int oldScrollY = getScrollY();
                                int newScrollY = oldScrollY - delta;
                                if (newScrollY < 0) {
                                    newScrollY = 0;
                                } else
                                    if (newScrollY > range) {
                                        newScrollY = range;
                                    }

                                if (newScrollY != oldScrollY) {
                                    super.scrollTo(getScrollX(), newScrollY);
                                    return true;
                                }
                            }
                        }
                    }
            }
        }
        return false;
    }

    private float getVerticalScrollFactorCompat() {
        if (mVerticalScrollFactor == 0) {
            android.util.TypedValue outValue = new android.util.TypedValue();
            final android.content.Context context = getContext();
            if (!context.getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight, outValue, true)) {
                throw new java.lang.IllegalStateException("Expected theme to define listPreferredItemHeight.");
            }
            mVerticalScrollFactor = outValue.getDimension(context.getResources().getDisplayMetrics());
        }
        return mVerticalScrollFactor;
    }

    @java.lang.Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.scrollTo(scrollX, scrollY);
    }

    boolean overScrollByCompat(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        final int overScrollMode = getOverScrollMode();
        final boolean canScrollHorizontal = computeHorizontalScrollRange() > computeHorizontalScrollExtent();
        final boolean canScrollVertical = computeVerticalScrollRange() > computeVerticalScrollExtent();
        final boolean overScrollHorizontal = (overScrollMode == android.view.View.OVER_SCROLL_ALWAYS) || ((overScrollMode == android.view.View.OVER_SCROLL_IF_CONTENT_SCROLLS) && canScrollHorizontal);
        final boolean overScrollVertical = (overScrollMode == android.view.View.OVER_SCROLL_ALWAYS) || ((overScrollMode == android.view.View.OVER_SCROLL_IF_CONTENT_SCROLLS) && canScrollVertical);
        int newScrollX = scrollX + deltaX;
        if (!overScrollHorizontal) {
            maxOverScrollX = 0;
        }
        int newScrollY = scrollY + deltaY;
        if (!overScrollVertical) {
            maxOverScrollY = 0;
        }
        // Clamp values if at the limits and record
        final int left = -maxOverScrollX;
        final int right = maxOverScrollX + scrollRangeX;
        final int top = -maxOverScrollY;
        final int bottom = maxOverScrollY + scrollRangeY;
        boolean clampedX = false;
        if (newScrollX > right) {
            newScrollX = right;
            clampedX = true;
        } else
            if (newScrollX < left) {
                newScrollX = left;
                clampedX = true;
            }

        boolean clampedY = false;
        if (newScrollY > bottom) {
            newScrollY = bottom;
            clampedY = true;
        } else
            if (newScrollY < top) {
                newScrollY = top;
                clampedY = true;
            }

        if (clampedY) {
            mScroller.springBack(newScrollX, newScrollY, 0, 0, 0, getScrollRange());
        }
        onOverScrolled(newScrollX, newScrollY, clampedX, clampedY);
        return clampedX || clampedY;
    }

    int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            android.view.View child = getChildAt(0);
            scrollRange = java.lang.Math.max(0, child.getHeight() - ((getHeight() - getPaddingBottom()) - getPaddingTop()));
        }
        return scrollRange;
    }

    /**
     * <p>
     * Finds the next focusable component that fits in the specified bounds.
     * </p>
     *
     * @param topFocus
     * 		look for a candidate is the one at the top of the bounds
     * 		if topFocus is true, or at the bottom of the bounds if topFocus is
     * 		false
     * @param top
     * 		the top offset of the bounds in which a focusable must be
     * 		found
     * @param bottom
     * 		the bottom offset of the bounds in which a focusable must
     * 		be found
     * @return the next focusable component in the bounds or null if none can
    be found
     */
    private android.view.View findFocusableViewInBounds(boolean topFocus, int top, int bottom) {
        java.util.List<android.view.View> focusables = getFocusables(android.view.View.FOCUS_FORWARD);
        android.view.View focusCandidate = null;
        /* A fully contained focusable is one where its top is below the bound's
        top, and its bottom is above the bound's bottom. A partially
        contained focusable is one where some part of it is within the
        bounds, but it also has some part that is not within bounds.  A fully contained
        focusable is preferred to a partially contained focusable.
         */
        boolean foundFullyContainedFocusable = false;
        int count = focusables.size();
        for (int i = 0; i < count; i++) {
            android.view.View view = focusables.get(i);
            int viewTop = view.getTop();
            int viewBottom = view.getBottom();
            if ((top < viewBottom) && (viewTop < bottom)) {
                /* the focusable is in the target area, it is a candidate for
                focusing
                 */
                final boolean viewIsFullyContained = (top < viewTop) && (viewBottom < bottom);
                if (focusCandidate == null) {
                    /* No candidate, take this one */
                    focusCandidate = view;
                    foundFullyContainedFocusable = viewIsFullyContained;
                } else {
                    final boolean viewIsCloserToBoundary = (topFocus && (viewTop < focusCandidate.getTop())) || ((!topFocus) && (viewBottom > focusCandidate.getBottom()));
                    if (foundFullyContainedFocusable) {
                        if (viewIsFullyContained && viewIsCloserToBoundary) {
                            /* We're dealing with only fully contained views, so
                            it has to be closer to the boundary to beat our
                            candidate
                             */
                            focusCandidate = view;
                        }
                    } else {
                        if (viewIsFullyContained) {
                            /* Any fully contained view beats a partially contained view */
                            focusCandidate = view;
                            foundFullyContainedFocusable = true;
                        } else
                            if (viewIsCloserToBoundary) {
                                /* Partially contained view beats another partially
                                contained view if it's closer
                                 */
                                focusCandidate = view;
                            }

                    }
                }
            }
        }
        return focusCandidate;
    }

    /**
     * <p>Handles scrolling in response to a "page up/down" shortcut press. This
     * method will scroll the view by one page up or down and give the focus
     * to the topmost/bottommost component in the new visible area. If no
     * component is a good candidate for focus, this scrollview reclaims the
     * focus.</p>
     *
     * @param direction
     * 		the scroll direction: {@link android.view.View#FOCUS_UP}
     * 		to go one page up or
     * 		{@link android.view.View#FOCUS_DOWN} to go one page down
     * @return true if the key event is consumed by this method, false otherwise
     */
    public boolean pageScroll(int direction) {
        boolean down = direction == android.view.View.FOCUS_DOWN;
        int height = getHeight();
        if (down) {
            mTempRect.top = getScrollY() + height;
            int count = getChildCount();
            if (count > 0) {
                android.view.View view = getChildAt(count - 1);
                if ((mTempRect.top + height) > view.getBottom()) {
                    mTempRect.top = view.getBottom() - height;
                }
            }
        } else {
            mTempRect.top = getScrollY() - height;
            if (mTempRect.top < 0) {
                mTempRect.top = 0;
            }
        }
        mTempRect.bottom = mTempRect.top + height;
        return scrollAndFocus(direction, mTempRect.top, mTempRect.bottom);
    }

    /**
     * <p>Handles scrolling in response to a "home/end" shortcut press. This
     * method will scroll the view to the top or bottom and give the focus
     * to the topmost/bottommost component in the new visible area. If no
     * component is a good candidate for focus, this scrollview reclaims the
     * focus.</p>
     *
     * @param direction
     * 		the scroll direction: {@link android.view.View#FOCUS_UP}
     * 		to go the top of the view or
     * 		{@link android.view.View#FOCUS_DOWN} to go the bottom
     * @return true if the key event is consumed by this method, false otherwise
     */
    public boolean fullScroll(int direction) {
        boolean down = direction == android.view.View.FOCUS_DOWN;
        int height = getHeight();
        mTempRect.top = 0;
        mTempRect.bottom = height;
        if (down) {
            int count = getChildCount();
            if (count > 0) {
                android.view.View view = getChildAt(count - 1);
                mTempRect.bottom = view.getBottom() + getPaddingBottom();
                mTempRect.top = mTempRect.bottom - height;
            }
        }
        return scrollAndFocus(direction, mTempRect.top, mTempRect.bottom);
    }

    /**
     * <p>Scrolls the view to make the area defined by <code>top</code> and
     * <code>bottom</code> visible. This method attempts to give the focus
     * to a component visible in this area. If no component can be focused in
     * the new visible area, the focus is reclaimed by this ScrollView.</p>
     *
     * @param direction
     * 		the scroll direction: {@link android.view.View#FOCUS_UP}
     * 		to go upward, {@link android.view.View#FOCUS_DOWN} to downward
     * @param top
     * 		the top offset of the new area to be made visible
     * @param bottom
     * 		the bottom offset of the new area to be made visible
     * @return true if the key event is consumed by this method, false otherwise
     */
    private boolean scrollAndFocus(int direction, int top, int bottom) {
        boolean handled = true;
        int height = getHeight();
        int containerTop = getScrollY();
        int containerBottom = containerTop + height;
        boolean up = direction == android.view.View.FOCUS_UP;
        android.view.View newFocused = findFocusableViewInBounds(up, top, bottom);
        if (newFocused == null) {
            newFocused = this;
        }
        if ((top >= containerTop) && (bottom <= containerBottom)) {
            handled = false;
        } else {
            int delta = (up) ? top - containerTop : bottom - containerBottom;
            doScrollY(delta);
        }
        if (newFocused != findFocus())
            newFocused.requestFocus(direction);

        return handled;
    }

    /**
     * Handle scrolling in response to an up or down arrow click.
     *
     * @param direction
     * 		The direction corresponding to the arrow key that was
     * 		pressed
     * @return True if we consumed the event, false otherwise
     */
    public boolean arrowScroll(int direction) {
        android.view.View currentFocused = findFocus();
        if (currentFocused == this)
            currentFocused = null;

        android.view.View nextFocused = android.view.FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        final int maxJump = getMaxScrollAmount();
        if ((nextFocused != null) && isWithinDeltaOfScreen(nextFocused, maxJump, getHeight())) {
            nextFocused.getDrawingRect(mTempRect);
            offsetDescendantRectToMyCoords(nextFocused, mTempRect);
            int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);
            doScrollY(scrollDelta);
            nextFocused.requestFocus(direction);
        } else {
            // no new focus
            int scrollDelta = maxJump;
            if ((direction == android.view.View.FOCUS_UP) && (getScrollY() < scrollDelta)) {
                scrollDelta = getScrollY();
            } else
                if (direction == android.view.View.FOCUS_DOWN) {
                    if (getChildCount() > 0) {
                        int daBottom = getChildAt(0).getBottom();
                        int screenBottom = (getScrollY() + getHeight()) - getPaddingBottom();
                        if ((daBottom - screenBottom) < maxJump) {
                            scrollDelta = daBottom - screenBottom;
                        }
                    }
                }

            if (scrollDelta == 0) {
                return false;
            }
            doScrollY(direction == android.view.View.FOCUS_DOWN ? scrollDelta : -scrollDelta);
        }
        if (((currentFocused != null) && currentFocused.isFocused()) && isOffScreen(currentFocused)) {
            // previously focused item still has focus and is off screen, give
            // it up (take it back to ourselves)
            // (also, need to temporarily force FOCUS_BEFORE_DESCENDANTS so we are
            // sure to
            // get it)
            final int descendantFocusability = getDescendantFocusability();// save

            setDescendantFocusability(android.view.ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            requestFocus();
            setDescendantFocusability(descendantFocusability);// restore

        }
        return true;
    }

    /**
     *
     *
     * @return whether the descendant of this scroll view is scrolled off
    screen.
     */
    private boolean isOffScreen(android.view.View descendant) {
        return !isWithinDeltaOfScreen(descendant, 0, getHeight());
    }

    /**
     *
     *
     * @return whether the descendant of this scroll view is within delta
    pixels of being on the screen.
     */
    private boolean isWithinDeltaOfScreen(android.view.View descendant, int delta, int height) {
        descendant.getDrawingRect(mTempRect);
        offsetDescendantRectToMyCoords(descendant, mTempRect);
        return ((mTempRect.bottom + delta) >= getScrollY()) && ((mTempRect.top - delta) <= (getScrollY() + height));
    }

    /**
     * Smooth scroll by a Y delta
     *
     * @param delta
     * 		the number of pixels to scroll by on the Y axis
     */
    private void doScrollY(int delta) {
        if (delta != 0) {
            if (mSmoothScrollingEnabled) {
                smoothScrollBy(0, delta);
            } else {
                scrollBy(0, delta);
            }
        }
    }

    /**
     * Like {@link View#scrollBy}, but scroll smoothly instead of immediately.
     *
     * @param dx
     * 		the number of pixels to scroll by on the X axis
     * @param dy
     * 		the number of pixels to scroll by on the Y axis
     */
    public final void smoothScrollBy(int dx, int dy) {
        if (getChildCount() == 0) {
            // Nothing to do.
            return;
        }
        long duration = android.view.animation.AnimationUtils.currentAnimationTimeMillis() - mLastScroll;
        if (duration > android.support.v4.widget.NestedScrollView.ANIMATED_SCROLL_GAP) {
            final int height = (getHeight() - getPaddingBottom()) - getPaddingTop();
            final int bottom = getChildAt(0).getHeight();
            final int maxY = java.lang.Math.max(0, bottom - height);
            final int scrollY = getScrollY();
            dy = java.lang.Math.max(0, java.lang.Math.min(scrollY + dy, maxY)) - scrollY;
            mScroller.startScroll(getScrollX(), scrollY, 0, dy);
            android.support.v4.view.ViewCompat.postInvalidateOnAnimation(this);
        } else {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            scrollBy(dx, dy);
        }
        mLastScroll = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
    }

    /**
     * Like {@link #scrollTo}, but scroll smoothly instead of immediately.
     *
     * @param x
     * 		the position where to scroll on the X axis
     * @param y
     * 		the position where to scroll on the Y axis
     */
    public final void smoothScrollTo(int x, int y) {
        smoothScrollBy(x - getScrollX(), y - getScrollY());
    }

    /**
     * <p>The scroll range of a scroll view is the overall height of all of its
     * children.</p>
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public int computeVerticalScrollRange() {
        final int count = getChildCount();
        final int contentHeight = (getHeight() - getPaddingBottom()) - getPaddingTop();
        if (count == 0) {
            return contentHeight;
        }
        int scrollRange = getChildAt(0).getBottom();
        final int scrollY = getScrollY();
        final int overscrollBottom = java.lang.Math.max(0, scrollRange - contentHeight);
        if (scrollY < 0) {
            scrollRange -= scrollY;
        } else
            if (scrollY > overscrollBottom) {
                scrollRange += scrollY - overscrollBottom;
            }

        return scrollRange;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public int computeVerticalScrollOffset() {
        return java.lang.Math.max(0, super.computeVerticalScrollOffset());
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public int computeHorizontalScrollRange() {
        return super.computeHorizontalScrollRange();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public int computeHorizontalScrollOffset() {
        return super.computeHorizontalScrollOffset();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.Override
    public int computeHorizontalScrollExtent() {
        return super.computeHorizontalScrollExtent();
    }

    @java.lang.Override
    protected void measureChild(android.view.View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        android.view.ViewGroup.LayoutParams lp = child.getLayoutParams();
        int childWidthMeasureSpec;
        int childHeightMeasureSpec;
        childWidthMeasureSpec = android.view.ViewGroup.getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight(), lp.width);
        childHeightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @java.lang.Override
    protected void measureChildWithMargins(android.view.View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        final android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (child.getLayoutParams()));
        final int childWidthMeasureSpec = android.view.ViewGroup.getChildMeasureSpec(parentWidthMeasureSpec, (((getPaddingLeft() + getPaddingRight()) + lp.leftMargin) + lp.rightMargin) + widthUsed, lp.width);
        final int childHeightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(lp.topMargin + lp.bottomMargin, android.view.View.MeasureSpec.UNSPECIFIED);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @java.lang.Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            if ((oldX != x) || (oldY != y)) {
                final int range = getScrollRange();
                final int overscrollMode = getOverScrollMode();
                final boolean canOverscroll = (overscrollMode == android.view.View.OVER_SCROLL_ALWAYS) || ((overscrollMode == android.view.View.OVER_SCROLL_IF_CONTENT_SCROLLS) && (range > 0));
                overScrollByCompat(x - oldX, y - oldY, oldX, oldY, 0, range, 0, 0, false);
                if (canOverscroll) {
                    ensureGlows();
                    if ((y <= 0) && (oldY > 0)) {
                        mEdgeGlowTop.onAbsorb(((int) (mScroller.getCurrVelocity())));
                    } else
                        if ((y >= range) && (oldY < range)) {
                            mEdgeGlowBottom.onAbsorb(((int) (mScroller.getCurrVelocity())));
                        }

                }
            }
        }
    }

    /**
     * Scrolls the view to the given child.
     *
     * @param child
     * 		the View to scroll to
     */
    private void scrollToChild(android.view.View child) {
        child.getDrawingRect(mTempRect);
        /* Offset from child's local coordinates to ScrollView coordinates */
        offsetDescendantRectToMyCoords(child, mTempRect);
        int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);
        if (scrollDelta != 0) {
            scrollBy(0, scrollDelta);
        }
    }

    /**
     * If rect is off screen, scroll just enough to get it (or at least the
     * first screen size chunk of it) on screen.
     *
     * @param rect
     * 		The rectangle.
     * @param immediate
     * 		True to scroll immediately without animation
     * @return true if scrolling was performed
     */
    private boolean scrollToChildRect(android.graphics.Rect rect, boolean immediate) {
        final int delta = computeScrollDeltaToGetChildRectOnScreen(rect);
        final boolean scroll = delta != 0;
        if (scroll) {
            if (immediate) {
                scrollBy(0, delta);
            } else {
                smoothScrollBy(0, delta);
            }
        }
        return scroll;
    }

    /**
     * Compute the amount to scroll in the Y direction in order to get
     * a rectangle completely on the screen (or, if taller than the screen,
     * at least the first screen size chunk of it).
     *
     * @param rect
     * 		The rect.
     * @return The scroll delta.
     */
    protected int computeScrollDeltaToGetChildRectOnScreen(android.graphics.Rect rect) {
        if (getChildCount() == 0)
            return 0;

        int height = getHeight();
        int screenTop = getScrollY();
        int screenBottom = screenTop + height;
        int fadingEdge = getVerticalFadingEdgeLength();
        // leave room for top fading edge as long as rect isn't at very top
        if (rect.top > 0) {
            screenTop += fadingEdge;
        }
        // leave room for bottom fading edge as long as rect isn't at very bottom
        if (rect.bottom < getChildAt(0).getHeight()) {
            screenBottom -= fadingEdge;
        }
        int scrollYDelta = 0;
        if ((rect.bottom > screenBottom) && (rect.top > screenTop)) {
            // need to move down to get it in view: move down just enough so
            // that the entire rectangle is in view (or at least the first
            // screen size chunk).
            if (rect.height() > height) {
                // just enough to get screen size chunk on
                scrollYDelta += rect.top - screenTop;
            } else {
                // get entire rect at bottom of screen
                scrollYDelta += rect.bottom - screenBottom;
            }
            // make sure we aren't scrolling beyond the end of our content
            int bottom = getChildAt(0).getBottom();
            int distanceToBottom = bottom - screenBottom;
            scrollYDelta = java.lang.Math.min(scrollYDelta, distanceToBottom);
        } else
            if ((rect.top < screenTop) && (rect.bottom < screenBottom)) {
                // need to move up to get it in view: move up just enough so that
                // entire rectangle is in view (or at least the first screen
                // size chunk of it).
                if (rect.height() > height) {
                    // screen size chunk
                    scrollYDelta -= screenBottom - rect.bottom;
                } else {
                    // entire rect at top
                    scrollYDelta -= screenTop - rect.top;
                }
                // make sure we aren't scrolling any further than the top our content
                scrollYDelta = java.lang.Math.max(scrollYDelta, -getScrollY());
            }

        return scrollYDelta;
    }

    @java.lang.Override
    public void requestChildFocus(android.view.View child, android.view.View focused) {
        if (!mIsLayoutDirty) {
            scrollToChild(focused);
        } else {
            // The child may not be laid out yet, we can't compute the scroll yet
            mChildToScrollTo = focused;
        }
        super.requestChildFocus(child, focused);
    }

    /**
     * When looking for focus in children of a scroll view, need to be a little
     * more careful not to give focus to something that is scrolled off screen.
     *
     * This is more expensive than the default {@link android.view.ViewGroup}
     * implementation, otherwise this behavior might have been made the default.
     */
    @java.lang.Override
    protected boolean onRequestFocusInDescendants(int direction, android.graphics.Rect previouslyFocusedRect) {
        // convert from forward / backward notation to up / down / left / right
        // (ugh).
        if (direction == android.view.View.FOCUS_FORWARD) {
            direction = android.view.View.FOCUS_DOWN;
        } else
            if (direction == android.view.View.FOCUS_BACKWARD) {
                direction = android.view.View.FOCUS_UP;
            }

        final android.view.View nextFocus = (previouslyFocusedRect == null) ? android.view.FocusFinder.getInstance().findNextFocus(this, null, direction) : android.view.FocusFinder.getInstance().findNextFocusFromRect(this, previouslyFocusedRect, direction);
        if (nextFocus == null) {
            return false;
        }
        if (isOffScreen(nextFocus)) {
            return false;
        }
        return nextFocus.requestFocus(direction, previouslyFocusedRect);
    }

    @java.lang.Override
    public boolean requestChildRectangleOnScreen(android.view.View child, android.graphics.Rect rectangle, boolean immediate) {
        // offset into coordinate space of this scroll view
        rectangle.offset(child.getLeft() - child.getScrollX(), child.getTop() - child.getScrollY());
        return scrollToChildRect(rectangle, immediate);
    }

    @java.lang.Override
    public void requestLayout() {
        mIsLayoutDirty = true;
        super.requestLayout();
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mIsLayoutDirty = false;
        // Give a child focus if it needs it
        if ((mChildToScrollTo != null) && android.support.v4.widget.NestedScrollView.isViewDescendantOf(mChildToScrollTo, this)) {
            scrollToChild(mChildToScrollTo);
        }
        mChildToScrollTo = null;
        if (!mIsLaidOut) {
            if (mSavedState != null) {
                scrollTo(getScrollX(), mSavedState.scrollPosition);
                mSavedState = null;
            }// mScrollY default value is "0"

            final int childHeight = (getChildCount() > 0) ? getChildAt(0).getMeasuredHeight() : 0;
            final int scrollRange = java.lang.Math.max(0, childHeight - (((b - t) - getPaddingBottom()) - getPaddingTop()));
            // Don't forget to clamp
            if (getScrollY() > scrollRange) {
                scrollTo(getScrollX(), scrollRange);
            } else
                if (getScrollY() < 0) {
                    scrollTo(getScrollX(), 0);
                }

        }
        // Calling this with the present values causes it to re-claim them
        scrollTo(getScrollX(), getScrollY());
        mIsLaidOut = true;
    }

    @java.lang.Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mIsLaidOut = false;
    }

    @java.lang.Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        android.view.View currentFocused = findFocus();
        if ((null == currentFocused) || (this == currentFocused)) {
            return;
        }
        // If the currently-focused view was visible on the screen when the
        // screen was at the old height, then scroll the screen to make that
        // view visible with the new screen height.
        if (isWithinDeltaOfScreen(currentFocused, 0, oldh)) {
            currentFocused.getDrawingRect(mTempRect);
            offsetDescendantRectToMyCoords(currentFocused, mTempRect);
            int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);
            doScrollY(scrollDelta);
        }
    }

    /**
     * Return true if child is a descendant of parent, (or equal to the parent).
     */
    private static boolean isViewDescendantOf(android.view.View child, android.view.View parent) {
        if (child == parent) {
            return true;
        }
        final android.view.ViewParent theParent = child.getParent();
        return (theParent instanceof android.view.ViewGroup) && android.support.v4.widget.NestedScrollView.isViewDescendantOf(((android.view.View) (theParent)), parent);
    }

    /**
     * Fling the scroll view
     *
     * @param velocityY
     * 		The initial velocity in the Y direction. Positive
     * 		numbers mean that the finger/cursor is moving down the screen,
     * 		which means we want to scroll towards the top.
     */
    public void fling(int velocityY) {
        if (getChildCount() > 0) {
            int height = (getHeight() - getPaddingBottom()) - getPaddingTop();
            int bottom = getChildAt(0).getHeight();
            mScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, 0, java.lang.Math.max(0, bottom - height), 0, height / 2);
            android.support.v4.view.ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void flingWithNestedDispatch(int velocityY) {
        final int scrollY = getScrollY();
        final boolean canFling = ((scrollY > 0) || (velocityY > 0)) && ((scrollY < getScrollRange()) || (velocityY < 0));
        if (!dispatchNestedPreFling(0, velocityY)) {
            dispatchNestedFling(0, velocityY, canFling);
            if (canFling) {
                fling(velocityY);
            }
        }
    }

    private void endDrag() {
        mIsBeingDragged = false;
        recycleVelocityTracker();
        stopNestedScroll();
        if (mEdgeGlowTop != null) {
            mEdgeGlowTop.onRelease();
            mEdgeGlowBottom.onRelease();
        }
    }

    /**
     * {@inheritDoc }
     *
     * <p>This version also clamps the scrolling to the bounds of our child.
     */
    @java.lang.Override
    public void scrollTo(int x, int y) {
        // we rely on the fact the View.scrollBy calls scrollTo.
        if (getChildCount() > 0) {
            android.view.View child = getChildAt(0);
            x = android.support.v4.widget.NestedScrollView.clamp(x, (getWidth() - getPaddingRight()) - getPaddingLeft(), child.getWidth());
            y = android.support.v4.widget.NestedScrollView.clamp(y, (getHeight() - getPaddingBottom()) - getPaddingTop(), child.getHeight());
            if ((x != getScrollX()) || (y != getScrollY())) {
                super.scrollTo(x, y);
            }
        }
    }

    private void ensureGlows() {
        if (getOverScrollMode() != android.view.View.OVER_SCROLL_NEVER) {
            if (mEdgeGlowTop == null) {
                android.content.Context context = getContext();
                mEdgeGlowTop = new android.support.v4.widget.EdgeEffectCompat(context);
                mEdgeGlowBottom = new android.support.v4.widget.EdgeEffectCompat(context);
            }
        } else {
            mEdgeGlowTop = null;
            mEdgeGlowBottom = null;
        }
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        super.draw(canvas);
        if (mEdgeGlowTop != null) {
            final int scrollY = getScrollY();
            if (!mEdgeGlowTop.isFinished()) {
                final int restoreCount = canvas.save();
                final int width = (getWidth() - getPaddingLeft()) - getPaddingRight();
                canvas.translate(getPaddingLeft(), java.lang.Math.min(0, scrollY));
                mEdgeGlowTop.setSize(width, getHeight());
                if (mEdgeGlowTop.draw(canvas)) {
                    android.support.v4.view.ViewCompat.postInvalidateOnAnimation(this);
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!mEdgeGlowBottom.isFinished()) {
                final int restoreCount = canvas.save();
                final int width = (getWidth() - getPaddingLeft()) - getPaddingRight();
                final int height = getHeight();
                canvas.translate((-width) + getPaddingLeft(), java.lang.Math.max(getScrollRange(), scrollY) + height);
                canvas.rotate(180, width, 0);
                mEdgeGlowBottom.setSize(width, height);
                if (mEdgeGlowBottom.draw(canvas)) {
                    android.support.v4.view.ViewCompat.postInvalidateOnAnimation(this);
                }
                canvas.restoreToCount(restoreCount);
            }
        }
    }

    private static int clamp(int n, int my, int child) {
        if ((my >= child) || (n < 0)) {
            /* my >= child is this case:
                               |--------------- me ---------------|
                |------ child ------|
            or
                |--------------- me ---------------|
                       |------ child ------|
            or
                |--------------- me ---------------|
                                             |------ child ------|

            n < 0 is this case:
                |------ me ------|
                               |-------- child --------|
                |-- mScrollX --|
             */
            return 0;
        }
        if ((my + n) > child) {
            /* this case:
                               |------ me ------|
                |------ child ------|
                |-- mScrollX --|
             */
            return child - my;
        }
        return n;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if (!(state instanceof android.support.v4.widget.NestedScrollView.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        android.support.v4.widget.NestedScrollView.SavedState ss = ((android.support.v4.widget.NestedScrollView.SavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        mSavedState = ss;
        requestLayout();
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        android.os.Parcelable superState = super.onSaveInstanceState();
        android.support.v4.widget.NestedScrollView.SavedState ss = new android.support.v4.widget.NestedScrollView.SavedState(superState);
        ss.scrollPosition = getScrollY();
        return ss;
    }

    static class SavedState extends android.view.View.BaseSavedState {
        public int scrollPosition;

        SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        SavedState(android.os.Parcel source) {
            super(source);
            scrollPosition = source.readInt();
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(scrollPosition);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((("HorizontalScrollView.SavedState{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " scrollPosition=") + scrollPosition) + "}";
        }

        public static final android.os.Parcelable.Creator<android.support.v4.widget.NestedScrollView.SavedState> CREATOR = new android.os.Parcelable.Creator<android.support.v4.widget.NestedScrollView.SavedState>() {
            @java.lang.Override
            public android.support.v4.widget.NestedScrollView.SavedState createFromParcel(android.os.Parcel in) {
                return new android.support.v4.widget.NestedScrollView.SavedState(in);
            }

            @java.lang.Override
            public android.support.v4.widget.NestedScrollView.SavedState[] newArray(int size) {
                return new android.support.v4.widget.NestedScrollView.SavedState[size];
            }
        };
    }

    static class AccessibilityDelegate extends android.support.v4.view.AccessibilityDelegateCompat {
        @java.lang.Override
        public boolean performAccessibilityAction(android.view.View host, int action, android.os.Bundle arguments) {
            if (super.performAccessibilityAction(host, action, arguments)) {
                return true;
            }
            final android.support.v4.widget.NestedScrollView nsvHost = ((android.support.v4.widget.NestedScrollView) (host));
            if (!nsvHost.isEnabled()) {
                return false;
            }
            switch (action) {
                case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD :
                    {
                        final int viewportHeight = (nsvHost.getHeight() - nsvHost.getPaddingBottom()) - nsvHost.getPaddingTop();
                        final int targetScrollY = java.lang.Math.min(nsvHost.getScrollY() + viewportHeight, nsvHost.getScrollRange());
                        if (targetScrollY != nsvHost.getScrollY()) {
                            nsvHost.smoothScrollTo(0, targetScrollY);
                            return true;
                        }
                    }
                    return false;
                case android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD :
                    {
                        final int viewportHeight = (nsvHost.getHeight() - nsvHost.getPaddingBottom()) - nsvHost.getPaddingTop();
                        final int targetScrollY = java.lang.Math.max(nsvHost.getScrollY() - viewportHeight, 0);
                        if (targetScrollY != nsvHost.getScrollY()) {
                            nsvHost.smoothScrollTo(0, targetScrollY);
                            return true;
                        }
                    }
                    return false;
            }
            return false;
        }

        @java.lang.Override
        public void onInitializeAccessibilityNodeInfo(android.view.View host, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            final android.support.v4.widget.NestedScrollView nsvHost = ((android.support.v4.widget.NestedScrollView) (host));
            info.setClassName(android.widget.ScrollView.class.getName());
            if (nsvHost.isEnabled()) {
                final int scrollRange = nsvHost.getScrollRange();
                if (scrollRange > 0) {
                    info.setScrollable(true);
                    if (nsvHost.getScrollY() > 0) {
                        info.addAction(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
                    }
                    if (nsvHost.getScrollY() < scrollRange) {
                        info.addAction(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
                    }
                }
            }
        }

        @java.lang.Override
        public void onInitializeAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            final android.support.v4.widget.NestedScrollView nsvHost = ((android.support.v4.widget.NestedScrollView) (host));
            event.setClassName(android.widget.ScrollView.class.getName());
            final android.support.v4.view.accessibility.AccessibilityRecordCompat record = android.support.v4.view.accessibility.AccessibilityEventCompat.asRecord(event);
            final boolean scrollable = nsvHost.getScrollRange() > 0;
            record.setScrollable(scrollable);
            record.setScrollX(nsvHost.getScrollX());
            record.setScrollY(nsvHost.getScrollY());
            record.setMaxScrollX(nsvHost.getScrollX());
            record.setMaxScrollY(nsvHost.getScrollRange());
        }
    }
}

