/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * A view group that allows the view hierarchy placed within it to be scrolled.
 * Scroll view may have only one direct child placed within it.
 * To add multiple views within the scroll view, make
 * the direct child you add a view group, for example {@link LinearLayout}, and
 * place additional views within that LinearLayout.
 *
 * <p>Scroll view supports vertical scrolling only. For horizontal scrolling,
 * use {@link HorizontalScrollView} instead.</p>
 *
 * <p>Never add a {@link android.support.v7.widget.RecyclerView} or {@link ListView} to
 * a scroll view. Doing so results in poor user interface performance and a poor user
 * experience.</p>
 *
 * <p class="note">
 * For vertical scrolling, consider {@link android.support.v4.widget.NestedScrollView}
 * instead of scroll view which offers greater user interface flexibility and
 * support for the material design scrolling patterns.</p>
 *
 * <p>To learn more about material design patterns for handling scrolling, see
 * <a href="https://material.io/guidelines/patterns/scrolling-techniques.html#">
 * Scrolling techniques</a>.</p>
 *
 * @unknown ref android.R.styleable#ScrollView_fillViewport
 */
public class ScrollView extends android.widget.FrameLayout {
    static final int ANIMATED_SCROLL_GAP = 250;

    static final float MAX_SCROLL_FACTOR = 0.5F;

    private static final java.lang.String TAG = "ScrollView";

    @android.annotation.UnsupportedAppUsage
    private long mLastScroll;

    private final android.graphics.Rect mTempRect = new android.graphics.Rect();

    @android.annotation.UnsupportedAppUsage
    private android.widget.OverScroller mScroller;

    /**
     * Tracks the state of the top edge glow.
     *
     * Even though this field is practically final, we cannot make it final because there are apps
     * setting it via reflection and they need to keep working until they target Q.
     */
    @android.annotation.NonNull
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P, trackingBug = 123768600)
    private android.widget.EdgeEffect mEdgeGlowTop = new android.widget.EdgeEffect(getContext());

    /**
     * Tracks the state of the bottom edge glow.
     *
     * Even though this field is practically final, we cannot make it final because there are apps
     * setting it via reflection and they need to keep working until they target Q.
     */
    @android.annotation.NonNull
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P, trackingBug = 123769386)
    private android.widget.EdgeEffect mEdgeGlowBottom = new android.widget.EdgeEffect(getContext());

    /**
     * Position of the last motion event.
     */
    @android.annotation.UnsupportedAppUsage
    private int mLastMotionY;

    /**
     * True when the layout has changed but the traversal has not come through yet.
     * Ideally the view hierarchy would keep track of this for us.
     */
    private boolean mIsLayoutDirty = true;

    /**
     * The child to give focus to in the event that a child has requested focus while the
     * layout is dirty. This prevents the scroll from being wrong if the child has not been
     * laid out before requesting focus.
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P, trackingBug = 123769715)
    private android.view.View mChildToScrollTo = null;

    /**
     * True if the user is currently dragging this ScrollView around. This is
     * not the same as 'is being flinged', which can be checked by
     * mScroller.isFinished() (flinging begins when the user lifts his finger).
     */
    @android.annotation.UnsupportedAppUsage
    private boolean mIsBeingDragged = false;

    /**
     * Determines speed during touch scrolling
     */
    @android.annotation.UnsupportedAppUsage
    private android.view.VelocityTracker mVelocityTracker;

    /**
     * When set to true, the scroll view measure its child to make it fill the currently
     * visible area.
     */
    @android.view.ViewDebug.ExportedProperty(category = "layout")
    private boolean mFillViewport;

    /**
     * Whether arrow scrolling is animated.
     */
    private boolean mSmoothScrollingEnabled = true;

    private int mTouchSlop;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P, trackingBug = 124051125)
    private int mMinimumVelocity;

    private int mMaximumVelocity;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P, trackingBug = 124050903)
    private int mOverscrollDistance;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P, trackingBug = 124050903)
    private int mOverflingDistance;

    private float mVerticalScrollFactor;

    /**
     * ID of the active pointer. This is used to retain consistency during
     * drags/flings if multiple pointers are used.
     */
    private int mActivePointerId = android.widget.ScrollView.INVALID_POINTER;

    /**
     * Used during scrolling to retrieve the new offset within the window.
     */
    private final int[] mScrollOffset = new int[2];

    private final int[] mScrollConsumed = new int[2];

    private int mNestedYOffset;

    /**
     * The StrictMode "critical time span" objects to catch animation
     * stutters.  Non-null when a time-sensitive animation is
     * in-flight.  Must call finish() on them when done animating.
     * These are no-ops on user builds.
     */
    private StrictMode.Span mScrollStrictSpan = null;// aka "drag"


    @android.annotation.UnsupportedAppUsage
    private StrictMode.Span mFlingStrictSpan = null;

    /**
     * Sentinel value for no current active pointer.
     * Used by {@link #mActivePointerId}.
     */
    private static final int INVALID_POINTER = -1;

    private android.widget.ScrollView.SavedState mSavedState;

    public ScrollView(android.content.Context context) {
        this(context, null);
    }

    public ScrollView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.scrollViewStyle);
    }

    public ScrollView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ScrollView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initScrollView();
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, android.widget.com.android.internal.R.styleable.ScrollView, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, android.widget.com.android.internal.R.styleable.ScrollView, attrs, a, defStyleAttr, defStyleRes);
        setFillViewport(a.getBoolean(R.styleable.ScrollView_fillViewport, false));
        a.recycle();
        if (context.getResources().getConfiguration().uiMode == android.content.res.Configuration.UI_MODE_TYPE_WATCH) {
            setRevealOnFocusHint(false);
        }
    }

    @java.lang.Override
    public boolean shouldDelayChildPressedState() {
        return true;
    }

    @java.lang.Override
    protected float getTopFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0F;
        }
        final int length = getVerticalFadingEdgeLength();
        if (mScrollY < length) {
            return mScrollY / ((float) (length));
        }
        return 1.0F;
    }

    @java.lang.Override
    protected float getBottomFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0F;
        }
        final int length = getVerticalFadingEdgeLength();
        final int bottomEdge = getHeight() - mPaddingBottom;
        final int span = (getChildAt(0).getBottom() - mScrollY) - bottomEdge;
        if (span < length) {
            return span / ((float) (length));
        }
        return 1.0F;
    }

    /**
     * Sets the edge effect color for both top and bottom edge effects.
     *
     * @param color
     * 		The color for the edge effects.
     * @see #setTopEdgeEffectColor(int)
     * @see #setBottomEdgeEffectColor(int)
     * @see #getTopEdgeEffectColor()
     * @see #getBottomEdgeEffectColor()
     */
    public void setEdgeEffectColor(@android.annotation.ColorInt
    int color) {
        setTopEdgeEffectColor(color);
        setBottomEdgeEffectColor(color);
    }

    /**
     * Sets the bottom edge effect color.
     *
     * @param color
     * 		The color for the bottom edge effect.
     * @see #setTopEdgeEffectColor(int)
     * @see #setEdgeEffectColor(int)
     * @see #getTopEdgeEffectColor()
     * @see #getBottomEdgeEffectColor()
     */
    public void setBottomEdgeEffectColor(@android.annotation.ColorInt
    int color) {
        mEdgeGlowBottom.setColor(color);
    }

    /**
     * Sets the top edge effect color.
     *
     * @param color
     * 		The color for the top edge effect.
     * @see #setBottomEdgeEffectColor(int)
     * @see #setEdgeEffectColor(int)
     * @see #getTopEdgeEffectColor()
     * @see #getBottomEdgeEffectColor()
     */
    public void setTopEdgeEffectColor(@android.annotation.ColorInt
    int color) {
        mEdgeGlowTop.setColor(color);
    }

    /**
     * Returns the top edge effect color.
     *
     * @return The top edge effect color.
     * @see #setEdgeEffectColor(int)
     * @see #setTopEdgeEffectColor(int)
     * @see #setBottomEdgeEffectColor(int)
     * @see #getBottomEdgeEffectColor()
     */
    @android.annotation.ColorInt
    public int getTopEdgeEffectColor() {
        return mEdgeGlowTop.getColor();
    }

    /**
     * Returns the bottom edge effect color.
     *
     * @return The bottom edge effect color.
     * @see #setEdgeEffectColor(int)
     * @see #setTopEdgeEffectColor(int)
     * @see #setBottomEdgeEffectColor(int)
     * @see #getTopEdgeEffectColor()
     */
    @android.annotation.ColorInt
    public int getBottomEdgeEffectColor() {
        return mEdgeGlowBottom.getColor();
    }

    /**
     *
     *
     * @return The maximum amount this scroll view will scroll in response to
    an arrow event.
     */
    public int getMaxScrollAmount() {
        return ((int) (android.widget.ScrollView.MAX_SCROLL_FACTOR * (mBottom - mTop)));
    }

    private void initScrollView() {
        mScroller = new android.widget.OverScroller(getContext());
        setFocusable(true);
        setDescendantFocusability(android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);
        final android.view.ViewConfiguration configuration = android.view.ViewConfiguration.get(mContext);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mOverscrollDistance = configuration.getScaledOverscrollDistance();
        mOverflingDistance = configuration.getScaledOverflingDistance();
        mVerticalScrollFactor = configuration.getScaledVerticalScrollFactor();
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
     *
     *
     * @return Returns true this ScrollView can be scrolled
     */
    @android.annotation.UnsupportedAppUsage
    private boolean canScroll() {
        android.view.View child = getChildAt(0);
        if (child != null) {
            int childHeight = child.getHeight();
            return getHeight() < ((childHeight + mPaddingTop) + mPaddingBottom);
        }
        return false;
    }

    /**
     * Indicates whether this ScrollView's content is stretched to fill the viewport.
     *
     * @return True if the content fills the viewport, false otherwise.
     * @unknown ref android.R.styleable#ScrollView_fillViewport
     */
    @android.view.inspector.InspectableProperty
    public boolean isFillViewport() {
        return mFillViewport;
    }

    /**
     * Indicates this ScrollView whether it should stretch its content height to fill
     * the viewport or not.
     *
     * @param fillViewport
     * 		True to stretch the content's height to the viewport's
     * 		boundaries, false otherwise.
     * @unknown ref android.R.styleable#ScrollView_fillViewport
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
            final int widthPadding;
            final int heightPadding;
            final int targetSdkVersion = getContext().getApplicationInfo().targetSdkVersion;
            final android.widget.FrameLayout.LayoutParams lp = ((android.widget.FrameLayout.LayoutParams) (child.getLayoutParams()));
            if (targetSdkVersion >= android.os.Build.VERSION_CODES.M) {
                widthPadding = ((mPaddingLeft + mPaddingRight) + lp.leftMargin) + lp.rightMargin;
                heightPadding = ((mPaddingTop + mPaddingBottom) + lp.topMargin) + lp.bottomMargin;
            } else {
                widthPadding = mPaddingLeft + mPaddingRight;
                heightPadding = mPaddingTop + mPaddingBottom;
            }
            final int desiredHeight = getMeasuredHeight() - heightPadding;
            if (child.getMeasuredHeight() < desiredHeight) {
                final int childWidthMeasureSpec = android.view.ViewGroup.getChildMeasureSpec(widthMeasureSpec, widthPadding, lp.width);
                final int childHeightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(desiredHeight, android.view.View.MeasureSpec.EXACTLY);
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
            final int scrollY = mScrollY;
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
        if (super.onInterceptTouchEvent(ev)) {
            return true;
        }
        /* Don't try to intercept touch if we can't scroll anyway. */
        if ((getScrollY() == 0) && (!canScrollVertically(1))) {
            return false;
        }
        switch (action & android.view.MotionEvent.ACTION_MASK) {
            case android.view.MotionEvent.ACTION_MOVE :
                {
                    /* mIsBeingDragged == false, otherwise the shortcut would have caught it. Check
                    whether the user has moved far enough from his original down touch.
                     */
                    /* Locally do absolute value. mLastMotionY is set to the y value
                    of the down event.
                     */
                    final int activePointerId = mActivePointerId;
                    if (activePointerId == android.widget.ScrollView.INVALID_POINTER) {
                        // If we don't have a valid id, the touch down wasn't on content.
                        break;
                    }
                    final int pointerIndex = ev.findPointerIndex(activePointerId);
                    if (pointerIndex == (-1)) {
                        android.util.Log.e(android.widget.ScrollView.TAG, ("Invalid pointerId=" + activePointerId) + " in onInterceptTouchEvent");
                        break;
                    }
                    final int y = ((int) (ev.getY(pointerIndex)));
                    final int yDiff = java.lang.Math.abs(y - mLastMotionY);
                    if ((yDiff > mTouchSlop) && ((getNestedScrollAxes() & android.view.View.SCROLL_AXIS_VERTICAL) == 0)) {
                        mIsBeingDragged = true;
                        mLastMotionY = y;
                        initVelocityTrackerIfNotExists();
                        mVelocityTracker.addMovement(ev);
                        mNestedYOffset = 0;
                        if (mScrollStrictSpan == null) {
                            mScrollStrictSpan = android.os.StrictMode.enterCriticalSpan("ScrollView-scroll");
                        }
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
                    if (mIsBeingDragged && (mScrollStrictSpan == null)) {
                        mScrollStrictSpan = android.os.StrictMode.enterCriticalSpan("ScrollView-scroll");
                    }
                    startNestedScroll(android.view.View.SCROLL_AXIS_VERTICAL);
                    break;
                }
            case android.view.MotionEvent.ACTION_CANCEL :
            case android.view.MotionEvent.ACTION_UP :
                /* Release the drag */
                mIsBeingDragged = false;
                mActivePointerId = android.widget.ScrollView.INVALID_POINTER;
                recycleVelocityTracker();
                if (mScroller.springBack(mScrollX, mScrollY, 0, 0, 0, getScrollRange())) {
                    postInvalidateOnAnimation();
                }
                stopNestedScroll();
                break;
            case android.view.MotionEvent.ACTION_POINTER_UP :
                onSecondaryPointerUp(ev);
                break;
        }
        /* The only time we want to intercept motion events is if we are in the
        drag mode.
         */
        return mIsBeingDragged;
    }

    private boolean shouldDisplayEdgeEffects() {
        return getOverScrollMode() != android.view.View.OVER_SCROLL_NEVER;
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        initVelocityTrackerIfNotExists();
        android.view.MotionEvent vtev = android.view.MotionEvent.obtain(ev);
        final int actionMasked = ev.getActionMasked();
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
                        if (mFlingStrictSpan != null) {
                            mFlingStrictSpan.finish();
                            mFlingStrictSpan = null;
                        }
                    }
                    // Remember where the motion event started
                    mLastMotionY = ((int) (ev.getY()));
                    mActivePointerId = ev.getPointerId(0);
                    startNestedScroll(android.view.View.SCROLL_AXIS_VERTICAL);
                    break;
                }
            case android.view.MotionEvent.ACTION_MOVE :
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == (-1)) {
                    android.util.Log.e(android.widget.ScrollView.TAG, ("Invalid pointerId=" + mActivePointerId) + " in onTouchEvent");
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
                    final int oldY = mScrollY;
                    final int range = getScrollRange();
                    final int overscrollMode = getOverScrollMode();
                    boolean canOverscroll = (overscrollMode == android.view.View.OVER_SCROLL_ALWAYS) || ((overscrollMode == android.view.View.OVER_SCROLL_IF_CONTENT_SCROLLS) && (range > 0));
                    // Calling overScrollBy will call onOverScrolled, which
                    // calls onScrollChanged if applicable.
                    if (overScrollBy(0, deltaY, 0, mScrollY, 0, range, 0, mOverscrollDistance, true) && (!hasNestedScrollingParent())) {
                        // Break our velocity if we hit a scroll barrier.
                        mVelocityTracker.clear();
                    }
                    final int scrolledDeltaY = mScrollY - oldY;
                    final int unconsumedY = deltaY - scrolledDeltaY;
                    if (dispatchNestedScroll(0, scrolledDeltaY, 0, unconsumedY, mScrollOffset)) {
                        mLastMotionY -= mScrollOffset[1];
                        vtev.offsetLocation(0, mScrollOffset[1]);
                        mNestedYOffset += mScrollOffset[1];
                    } else
                        if (canOverscroll) {
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

                            if (shouldDisplayEdgeEffects() && ((!mEdgeGlowTop.isFinished()) || (!mEdgeGlowBottom.isFinished()))) {
                                postInvalidateOnAnimation();
                            }
                        }

                }
                break;
            case android.view.MotionEvent.ACTION_UP :
                if (mIsBeingDragged) {
                    final android.view.VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity = ((int) (velocityTracker.getYVelocity(mActivePointerId)));
                    if (java.lang.Math.abs(initialVelocity) > mMinimumVelocity) {
                        flingWithNestedDispatch(-initialVelocity);
                    } else
                        if (mScroller.springBack(mScrollX, mScrollY, 0, 0, 0, getScrollRange())) {
                            postInvalidateOnAnimation();
                        }

                    mActivePointerId = android.widget.ScrollView.INVALID_POINTER;
                    endDrag();
                }
                break;
            case android.view.MotionEvent.ACTION_CANCEL :
                if (mIsBeingDragged && (getChildCount() > 0)) {
                    if (mScroller.springBack(mScrollX, mScrollY, 0, 0, 0, getScrollRange())) {
                        postInvalidateOnAnimation();
                    }
                    mActivePointerId = android.widget.ScrollView.INVALID_POINTER;
                    endDrag();
                }
                break;
            case android.view.MotionEvent.ACTION_POINTER_DOWN :
                {
                    final int index = ev.getActionIndex();
                    mLastMotionY = ((int) (ev.getY(index)));
                    mActivePointerId = ev.getPointerId(index);
                    break;
                }
            case android.view.MotionEvent.ACTION_POINTER_UP :
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
        final int pointerIndex = (ev.getAction() & android.view.MotionEvent.ACTION_POINTER_INDEX_MASK) >> android.view.MotionEvent.ACTION_POINTER_INDEX_SHIFT;
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

    @java.lang.Override
    public boolean onGenericMotionEvent(android.view.MotionEvent event) {
        switch (event.getAction()) {
            case android.view.MotionEvent.ACTION_SCROLL :
                final float axisValue;
                if (event.isFromSource(android.view.InputDevice.SOURCE_CLASS_POINTER)) {
                    axisValue = event.getAxisValue(android.view.MotionEvent.AXIS_VSCROLL);
                } else
                    if (event.isFromSource(android.view.InputDevice.SOURCE_ROTARY_ENCODER)) {
                        axisValue = event.getAxisValue(android.view.MotionEvent.AXIS_SCROLL);
                    } else {
                        axisValue = 0;
                    }

                final int delta = java.lang.Math.round(axisValue * mVerticalScrollFactor);
                if (delta != 0) {
                    final int range = getScrollRange();
                    int oldScrollY = mScrollY;
                    int newScrollY = oldScrollY - delta;
                    if (newScrollY < 0) {
                        newScrollY = 0;
                    } else
                        if (newScrollY > range) {
                            newScrollY = range;
                        }

                    if (newScrollY != oldScrollY) {
                        super.scrollTo(mScrollX, newScrollY);
                        return true;
                    }
                }
                break;
        }
        return super.onGenericMotionEvent(event);
    }

    @java.lang.Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        // Treat animating scrolls differently; see #computeScroll() for why.
        if (!mScroller.isFinished()) {
            final int oldX = mScrollX;
            final int oldY = mScrollY;
            mScrollX = scrollX;
            mScrollY = scrollY;
            invalidateParentIfNeeded();
            onScrollChanged(mScrollX, mScrollY, oldX, oldY);
            if (clampedY) {
                mScroller.springBack(mScrollX, mScrollY, 0, 0, 0, getScrollRange());
            }
        } else {
            super.scrollTo(scrollX, scrollY);
        }
        awakenScrollBars();
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
        if (!isEnabled()) {
            return false;
        }
        switch (action) {
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_FORWARD :
            case R.id.accessibilityActionScrollDown :
                {
                    final int viewportHeight = (getHeight() - mPaddingBottom) - mPaddingTop;
                    final int targetScrollY = java.lang.Math.min(mScrollY + viewportHeight, getScrollRange());
                    if (targetScrollY != mScrollY) {
                        smoothScrollTo(0, targetScrollY);
                        return true;
                    }
                }
                return false;
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD :
            case R.id.accessibilityActionScrollUp :
                {
                    final int viewportHeight = (getHeight() - mPaddingBottom) - mPaddingTop;
                    final int targetScrollY = java.lang.Math.max(mScrollY - viewportHeight, 0);
                    if (targetScrollY != mScrollY) {
                        smoothScrollTo(0, targetScrollY);
                        return true;
                    }
                }
                return false;
        }
        return false;
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.ScrollView.class.getName();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onInitializeAccessibilityNodeInfoInternal(android.view.accessibility.AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (isEnabled()) {
            final int scrollRange = getScrollRange();
            if (scrollRange > 0) {
                info.setScrollable(true);
                if (mScrollY > 0) {
                    info.addAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);
                    info.addAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP);
                }
                if (mScrollY < scrollRange) {
                    info.addAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
                    info.addAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN);
                }
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onInitializeAccessibilityEventInternal(android.view.accessibility.AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        final boolean scrollable = getScrollRange() > 0;
        event.setScrollable(scrollable);
        event.setScrollX(mScrollX);
        event.setScrollY(mScrollY);
        event.setMaxScrollX(mScrollX);
        event.setMaxScrollY(getScrollRange());
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            android.view.View child = getChildAt(0);
            scrollRange = java.lang.Math.max(0, child.getHeight() - ((getHeight() - mPaddingBottom) - mPaddingTop));
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
                mTempRect.bottom = view.getBottom() + mPaddingBottom;
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
                        int screenBottom = (getScrollY() + getHeight()) - mPaddingBottom;
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
        if (duration > android.widget.ScrollView.ANIMATED_SCROLL_GAP) {
            final int height = (getHeight() - mPaddingBottom) - mPaddingTop;
            final int bottom = getChildAt(0).getHeight();
            final int maxY = java.lang.Math.max(0, bottom - height);
            final int scrollY = mScrollY;
            dy = java.lang.Math.max(0, java.lang.Math.min(scrollY + dy, maxY)) - scrollY;
            mScroller.startScroll(mScrollX, scrollY, 0, dy);
            postInvalidateOnAnimation();
        } else {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
                if (mFlingStrictSpan != null) {
                    mFlingStrictSpan.finish();
                    mFlingStrictSpan = null;
                }
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
        smoothScrollBy(x - mScrollX, y - mScrollY);
    }

    /**
     * <p>The scroll range of a scroll view is the overall height of all of its
     * children.</p>
     */
    @java.lang.Override
    protected int computeVerticalScrollRange() {
        final int count = getChildCount();
        final int contentHeight = (getHeight() - mPaddingBottom) - mPaddingTop;
        if (count == 0) {
            return contentHeight;
        }
        int scrollRange = getChildAt(0).getBottom();
        final int scrollY = mScrollY;
        final int overscrollBottom = java.lang.Math.max(0, scrollRange - contentHeight);
        if (scrollY < 0) {
            scrollRange -= scrollY;
        } else
            if (scrollY > overscrollBottom) {
                scrollRange += scrollY - overscrollBottom;
            }

        return scrollRange;
    }

    @java.lang.Override
    protected int computeVerticalScrollOffset() {
        return java.lang.Math.max(0, super.computeVerticalScrollOffset());
    }

    @java.lang.Override
    protected void measureChild(android.view.View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        android.view.ViewGroup.LayoutParams lp = child.getLayoutParams();
        int childWidthMeasureSpec;
        int childHeightMeasureSpec;
        childWidthMeasureSpec = android.view.ViewGroup.getChildMeasureSpec(parentWidthMeasureSpec, mPaddingLeft + mPaddingRight, lp.width);
        final int verticalPadding = mPaddingTop + mPaddingBottom;
        childHeightMeasureSpec = android.view.View.MeasureSpec.makeSafeMeasureSpec(java.lang.Math.max(0, android.view.View.MeasureSpec.getSize(parentHeightMeasureSpec) - verticalPadding), android.view.View.MeasureSpec.UNSPECIFIED);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @java.lang.Override
    protected void measureChildWithMargins(android.view.View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        final android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (child.getLayoutParams()));
        final int childWidthMeasureSpec = android.view.ViewGroup.getChildMeasureSpec(parentWidthMeasureSpec, (((mPaddingLeft + mPaddingRight) + lp.leftMargin) + lp.rightMargin) + widthUsed, lp.width);
        final int usedTotal = (((mPaddingTop + mPaddingBottom) + lp.topMargin) + lp.bottomMargin) + heightUsed;
        final int childHeightMeasureSpec = android.view.View.MeasureSpec.makeSafeMeasureSpec(java.lang.Math.max(0, android.view.View.MeasureSpec.getSize(parentHeightMeasureSpec) - usedTotal), android.view.View.MeasureSpec.UNSPECIFIED);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @java.lang.Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            // This is called at drawing time by ViewGroup.  We don't want to
            // re-show the scrollbars at this point, which scrollTo will do,
            // so we replicate most of scrollTo here.
            // 
            // It's a little odd to call onScrollChanged from inside the drawing.
            // 
            // It is, except when you remember that computeScroll() is used to
            // animate scrolling. So unless we want to defer the onScrollChanged()
            // until the end of the animated scrolling, we don't really have a
            // choice here.
            // 
            // I agree.  The alternative, which I think would be worse, is to post
            // something and tell the subclasses later.  This is bad because there
            // will be a window where mScrollX/Y is different from what the app
            // thinks it is.
            // 
            int oldX = mScrollX;
            int oldY = mScrollY;
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            if ((oldX != x) || (oldY != y)) {
                final int range = getScrollRange();
                final int overscrollMode = getOverScrollMode();
                final boolean canOverscroll = (overscrollMode == android.view.View.OVER_SCROLL_ALWAYS) || ((overscrollMode == android.view.View.OVER_SCROLL_IF_CONTENT_SCROLLS) && (range > 0));
                overScrollBy(x - oldX, y - oldY, oldX, oldY, 0, range, 0, mOverflingDistance, false);
                onScrollChanged(mScrollX, mScrollY, oldX, oldY);
                if (canOverscroll) {
                    if ((y < 0) && (oldY >= 0)) {
                        mEdgeGlowTop.onAbsorb(((int) (mScroller.getCurrVelocity())));
                    } else
                        if ((y > range) && (oldY <= range)) {
                            mEdgeGlowBottom.onAbsorb(((int) (mScroller.getCurrVelocity())));
                        }

                }
            }
            if (!awakenScrollBars()) {
                // Keep on drawing until the animation has finished.
                postInvalidateOnAnimation();
            }
        } else {
            if (mFlingStrictSpan != null) {
                mFlingStrictSpan.finish();
                mFlingStrictSpan = null;
            }
        }
    }

    /**
     * Scrolls the view to the given child.
     *
     * @param child
     * 		the View to scroll to
     */
    public void scrollToDescendant(@android.annotation.NonNull
    android.view.View child) {
        if (!mIsLayoutDirty) {
            child.getDrawingRect(mTempRect);
            /* Offset from child's local coordinates to ScrollView coordinates */
            offsetDescendantRectToMyCoords(child, mTempRect);
            int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);
            if (scrollDelta != 0) {
                scrollBy(0, scrollDelta);
            }
        } else {
            mChildToScrollTo = child;
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
        if ((focused != null) && focused.getRevealOnFocusHint()) {
            if (!mIsLayoutDirty) {
                scrollToDescendant(focused);
            } else {
                // The child may not be laid out yet, we can't compute the scroll yet
                mChildToScrollTo = focused;
            }
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
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mScrollStrictSpan != null) {
            mScrollStrictSpan.finish();
            mScrollStrictSpan = null;
        }
        if (mFlingStrictSpan != null) {
            mFlingStrictSpan.finish();
            mFlingStrictSpan = null;
        }
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mIsLayoutDirty = false;
        // Give a child focus if it needs it
        if ((mChildToScrollTo != null) && android.widget.ScrollView.isViewDescendantOf(mChildToScrollTo, this)) {
            scrollToDescendant(mChildToScrollTo);
        }
        mChildToScrollTo = null;
        if (!isLaidOut()) {
            if (mSavedState != null) {
                mScrollY = mSavedState.scrollPosition;
                mSavedState = null;
            }// mScrollY default value is "0"

            final int childHeight = (getChildCount() > 0) ? getChildAt(0).getMeasuredHeight() : 0;
            final int scrollRange = java.lang.Math.max(0, childHeight - (((b - t) - mPaddingBottom) - mPaddingTop));
            // Don't forget to clamp
            if (mScrollY > scrollRange) {
                mScrollY = scrollRange;
            } else
                if (mScrollY < 0) {
                    mScrollY = 0;
                }

        }
        // Calling this with the present values causes it to re-claim them
        scrollTo(mScrollX, mScrollY);
    }

    @java.lang.Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        android.view.View currentFocused = findFocus();
        if ((null == currentFocused) || (this == currentFocused))
            return;

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
        return (theParent instanceof android.view.ViewGroup) && android.widget.ScrollView.isViewDescendantOf(((android.view.View) (theParent)), parent);
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
            int height = (getHeight() - mPaddingBottom) - mPaddingTop;
            int bottom = getChildAt(0).getHeight();
            mScroller.fling(mScrollX, mScrollY, 0, velocityY, 0, 0, 0, java.lang.Math.max(0, bottom - height), 0, height / 2);
            if (mFlingStrictSpan == null) {
                mFlingStrictSpan = android.os.StrictMode.enterCriticalSpan("ScrollView-fling");
            }
            postInvalidateOnAnimation();
        }
    }

    private void flingWithNestedDispatch(int velocityY) {
        final boolean canFling = ((mScrollY > 0) || (velocityY > 0)) && ((mScrollY < getScrollRange()) || (velocityY < 0));
        if (!dispatchNestedPreFling(0, velocityY)) {
            dispatchNestedFling(0, velocityY, canFling);
            if (canFling) {
                fling(velocityY);
            }
        }
    }

    @android.annotation.UnsupportedAppUsage
    private void endDrag() {
        mIsBeingDragged = false;
        recycleVelocityTracker();
        if (shouldDisplayEdgeEffects()) {
            mEdgeGlowTop.onRelease();
            mEdgeGlowBottom.onRelease();
        }
        if (mScrollStrictSpan != null) {
            mScrollStrictSpan.finish();
            mScrollStrictSpan = null;
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
            x = android.widget.ScrollView.clamp(x, (getWidth() - mPaddingRight) - mPaddingLeft, child.getWidth());
            y = android.widget.ScrollView.clamp(y, (getHeight() - mPaddingBottom) - mPaddingTop, child.getHeight());
            if ((x != mScrollX) || (y != mScrollY)) {
                super.scrollTo(x, y);
            }
        }
    }

    @java.lang.Override
    public boolean onStartNestedScroll(android.view.View child, android.view.View target, int nestedScrollAxes) {
        return (nestedScrollAxes & android.view.View.SCROLL_AXIS_VERTICAL) != 0;
    }

    @java.lang.Override
    public void onNestedScrollAccepted(android.view.View child, android.view.View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(android.view.View.SCROLL_AXIS_VERTICAL);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onStopNestedScroll(android.view.View target) {
        super.onStopNestedScroll(target);
    }

    @java.lang.Override
    public void onNestedScroll(android.view.View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        final int oldScrollY = mScrollY;
        scrollBy(0, dyUnconsumed);
        final int myConsumed = mScrollY - oldScrollY;
        final int myUnconsumed = dyUnconsumed - myConsumed;
        dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean onNestedFling(android.view.View target, float velocityX, float velocityY, boolean consumed) {
        if (!consumed) {
            flingWithNestedDispatch(((int) (velocityY)));
            return true;
        }
        return false;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        super.draw(canvas);
        if (shouldDisplayEdgeEffects()) {
            final int scrollY = mScrollY;
            final boolean clipToPadding = getClipToPadding();
            if (!mEdgeGlowTop.isFinished()) {
                final int restoreCount = canvas.save();
                final int width;
                final int height;
                final float translateX;
                final float translateY;
                if (clipToPadding) {
                    width = (getWidth() - mPaddingLeft) - mPaddingRight;
                    height = (getHeight() - mPaddingTop) - mPaddingBottom;
                    translateX = mPaddingLeft;
                    translateY = mPaddingTop;
                } else {
                    width = getWidth();
                    height = getHeight();
                    translateX = 0;
                    translateY = 0;
                }
                canvas.translate(translateX, java.lang.Math.min(0, scrollY) + translateY);
                mEdgeGlowTop.setSize(width, height);
                if (mEdgeGlowTop.draw(canvas)) {
                    postInvalidateOnAnimation();
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!mEdgeGlowBottom.isFinished()) {
                final int restoreCount = canvas.save();
                final int width;
                final int height;
                final float translateX;
                final float translateY;
                if (clipToPadding) {
                    width = (getWidth() - mPaddingLeft) - mPaddingRight;
                    height = (getHeight() - mPaddingTop) - mPaddingBottom;
                    translateX = mPaddingLeft;
                    translateY = mPaddingTop;
                } else {
                    width = getWidth();
                    height = getHeight();
                    translateX = 0;
                    translateY = 0;
                }
                canvas.translate((-width) + translateX, (java.lang.Math.max(getScrollRange(), scrollY) + height) + translateY);
                canvas.rotate(180, width, 0);
                mEdgeGlowBottom.setSize(width, height);
                if (mEdgeGlowBottom.draw(canvas)) {
                    postInvalidateOnAnimation();
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
        if (mContext.getApplicationInfo().targetSdkVersion <= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // Some old apps reused IDs in ways they shouldn't have.
            // Don't break them, but they don't get scroll state restoration.
            super.onRestoreInstanceState(state);
            return;
        }
        android.widget.ScrollView.SavedState ss = ((android.widget.ScrollView.SavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        mSavedState = ss;
        requestLayout();
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        if (mContext.getApplicationInfo().targetSdkVersion <= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // Some old apps reused IDs in ways they shouldn't have.
            // Don't break them, but they don't get scroll state restoration.
            return super.onSaveInstanceState();
        }
        android.os.Parcelable superState = super.onSaveInstanceState();
        android.widget.ScrollView.SavedState ss = new android.widget.ScrollView.SavedState(superState);
        ss.scrollPosition = mScrollY;
        return ss;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void encodeProperties(@android.annotation.NonNull
    android.view.ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("fillViewport", mFillViewport);
    }

    static class SavedState extends android.view.View.BaseSavedState {
        public int scrollPosition;

        SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        public SavedState(android.os.Parcel source) {
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
            return ((("ScrollView.SavedState{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " scrollPosition=") + scrollPosition) + "}";
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.widget.ScrollView.SavedState> CREATOR = new android.os.Parcelable.Creator<android.widget.ScrollView.SavedState>() {
            public android.widget.SavedState createFromParcel(android.os.Parcel in) {
                return new android.widget.SavedState(in);
            }

            public android.widget.SavedState[] newArray(int size) {
                return new android.widget.SavedState[size];
            }
        };
    }
}

