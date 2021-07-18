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
 * The {@link Behavior} for a view that sits vertically above scrolling a view.
 * See {@link HeaderScrollingViewBehavior}.
 */
abstract class HeaderBehavior<V extends android.view.View> extends android.support.design.widget.ViewOffsetBehavior<V> {
    private static final int INVALID_POINTER = -1;

    private java.lang.Runnable mFlingRunnable;

    android.support.v4.widget.ScrollerCompat mScroller;

    private boolean mIsBeingDragged;

    private int mActivePointerId = android.support.design.widget.HeaderBehavior.INVALID_POINTER;

    private int mLastMotionY;

    private int mTouchSlop = -1;

    private android.view.VelocityTracker mVelocityTracker;

    public HeaderBehavior() {
    }

    public HeaderBehavior(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    @java.lang.Override
    public boolean onInterceptTouchEvent(android.support.design.widget.CoordinatorLayout parent, V child, android.view.MotionEvent ev) {
        if (mTouchSlop < 0) {
            mTouchSlop = android.view.ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
        }
        final int action = ev.getAction();
        // Shortcut since we're being dragged
        if ((action == android.view.MotionEvent.ACTION_MOVE) && mIsBeingDragged) {
            return true;
        }
        switch (android.support.v4.view.MotionEventCompat.getActionMasked(ev)) {
            case android.view.MotionEvent.ACTION_DOWN :
                {
                    mIsBeingDragged = false;
                    final int x = ((int) (ev.getX()));
                    final int y = ((int) (ev.getY()));
                    if (canDragView(child) && parent.isPointInChildBounds(child, x, y)) {
                        mLastMotionY = y;
                        mActivePointerId = ev.getPointerId(0);
                        ensureVelocityTracker();
                    }
                    break;
                }
            case android.view.MotionEvent.ACTION_MOVE :
                {
                    final int activePointerId = mActivePointerId;
                    if (activePointerId == android.support.design.widget.HeaderBehavior.INVALID_POINTER) {
                        // If we don't have a valid id, the touch down wasn't on content.
                        break;
                    }
                    final int pointerIndex = ev.findPointerIndex(activePointerId);
                    if (pointerIndex == (-1)) {
                        break;
                    }
                    final int y = ((int) (ev.getY(pointerIndex)));
                    final int yDiff = java.lang.Math.abs(y - mLastMotionY);
                    if (yDiff > mTouchSlop) {
                        mIsBeingDragged = true;
                        mLastMotionY = y;
                    }
                    break;
                }
            case android.view.MotionEvent.ACTION_CANCEL :
            case android.view.MotionEvent.ACTION_UP :
                {
                    mIsBeingDragged = false;
                    mActivePointerId = android.support.design.widget.HeaderBehavior.INVALID_POINTER;
                    if (mVelocityTracker != null) {
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }
                    break;
                }
        }
        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(ev);
        }
        return mIsBeingDragged;
    }

    @java.lang.Override
    public boolean onTouchEvent(android.support.design.widget.CoordinatorLayout parent, V child, android.view.MotionEvent ev) {
        if (mTouchSlop < 0) {
            mTouchSlop = android.view.ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
        }
        switch (android.support.v4.view.MotionEventCompat.getActionMasked(ev)) {
            case android.view.MotionEvent.ACTION_DOWN :
                {
                    final int x = ((int) (ev.getX()));
                    final int y = ((int) (ev.getY()));
                    if (parent.isPointInChildBounds(child, x, y) && canDragView(child)) {
                        mLastMotionY = y;
                        mActivePointerId = ev.getPointerId(0);
                        ensureVelocityTracker();
                    } else {
                        return false;
                    }
                    break;
                }
            case android.view.MotionEvent.ACTION_MOVE :
                {
                    final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                    if (activePointerIndex == (-1)) {
                        return false;
                    }
                    final int y = ((int) (ev.getY(activePointerIndex)));
                    int dy = mLastMotionY - y;
                    if ((!mIsBeingDragged) && (java.lang.Math.abs(dy) > mTouchSlop)) {
                        mIsBeingDragged = true;
                        if (dy > 0) {
                            dy -= mTouchSlop;
                        } else {
                            dy += mTouchSlop;
                        }
                    }
                    if (mIsBeingDragged) {
                        mLastMotionY = y;
                        // We're being dragged so scroll the ABL
                        scroll(parent, child, dy, getMaxDragOffset(child), 0);
                    }
                    break;
                }
            case android.view.MotionEvent.ACTION_UP :
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(ev);
                    mVelocityTracker.computeCurrentVelocity(1000);
                    float yvel = android.support.v4.view.VelocityTrackerCompat.getYVelocity(mVelocityTracker, mActivePointerId);
                    fling(parent, child, -getScrollRangeForDragFling(child), 0, yvel);
                }
                // $FALLTHROUGH
            case android.view.MotionEvent.ACTION_CANCEL :
                {
                    mIsBeingDragged = false;
                    mActivePointerId = android.support.design.widget.HeaderBehavior.INVALID_POINTER;
                    if (mVelocityTracker != null) {
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }
                    break;
                }
        }
        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(ev);
        }
        return true;
    }

    int setHeaderTopBottomOffset(android.support.design.widget.CoordinatorLayout parent, V header, int newOffset) {
        return setHeaderTopBottomOffset(parent, header, newOffset, java.lang.Integer.MIN_VALUE, java.lang.Integer.MAX_VALUE);
    }

    int setHeaderTopBottomOffset(android.support.design.widget.CoordinatorLayout parent, V header, int newOffset, int minOffset, int maxOffset) {
        final int curOffset = getTopAndBottomOffset();
        int consumed = 0;
        if (((minOffset != 0) && (curOffset >= minOffset)) && (curOffset <= maxOffset)) {
            // If we have some scrolling range, and we're currently within the min and max
            // offsets, calculate a new offset
            newOffset = android.support.design.widget.MathUtils.constrain(newOffset, minOffset, maxOffset);
            if (curOffset != newOffset) {
                setTopAndBottomOffset(newOffset);
                // Update how much dy we have consumed
                consumed = curOffset - newOffset;
            }
        }
        return consumed;
    }

    int getTopBottomOffsetForScrollingSibling() {
        return getTopAndBottomOffset();
    }

    final int scroll(android.support.design.widget.CoordinatorLayout coordinatorLayout, V header, int dy, int minOffset, int maxOffset) {
        return setHeaderTopBottomOffset(coordinatorLayout, header, getTopBottomOffsetForScrollingSibling() - dy, minOffset, maxOffset);
    }

    final boolean fling(android.support.design.widget.CoordinatorLayout coordinatorLayout, V layout, int minOffset, int maxOffset, float velocityY) {
        if (mFlingRunnable != null) {
            layout.removeCallbacks(mFlingRunnable);
            mFlingRunnable = null;
        }
        if (mScroller == null) {
            mScroller = android.support.v4.widget.ScrollerCompat.create(layout.getContext());
        }
        // curr
        // velocity.
        // x
        mScroller.fling(0, getTopAndBottomOffset(), 0, java.lang.Math.round(velocityY), 0, 0, minOffset, maxOffset);// y

        if (mScroller.computeScrollOffset()) {
            mFlingRunnable = new FlingRunnable(coordinatorLayout, layout);
            android.support.v4.view.ViewCompat.postOnAnimation(layout, mFlingRunnable);
            return true;
        } else {
            onFlingFinished(coordinatorLayout, layout);
            return false;
        }
    }

    /**
     * Called when a fling has finished, or the fling was initiated but there wasn't enough
     * velocity to start it.
     */
    void onFlingFinished(android.support.design.widget.CoordinatorLayout parent, V layout) {
        // no-op
    }

    /**
     * Return true if the view can be dragged.
     */
    boolean canDragView(V view) {
        return false;
    }

    /**
     * Returns the maximum px offset when {@code view} is being dragged.
     */
    int getMaxDragOffset(V view) {
        return -view.getHeight();
    }

    int getScrollRangeForDragFling(V view) {
        return view.getHeight();
    }

    private void ensureVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = android.view.VelocityTracker.obtain();
        }
    }

    private class FlingRunnable implements java.lang.Runnable {
        private final android.support.design.widget.CoordinatorLayout mParent;

        private final V mLayout;

        FlingRunnable(android.support.design.widget.CoordinatorLayout parent, V layout) {
            mParent = parent;
            mLayout = layout;
        }

        @java.lang.Override
        public void run() {
            if ((mLayout != null) && (mScroller != null)) {
                if (mScroller.computeScrollOffset()) {
                    setHeaderTopBottomOffset(mParent, mLayout, mScroller.getCurrY());
                    // Post ourselves so that we run on the next animation
                    android.support.v4.view.ViewCompat.postOnAnimation(mLayout, this);
                } else {
                    onFlingFinished(mParent, mLayout);
                }
            }
        }
    }
}

