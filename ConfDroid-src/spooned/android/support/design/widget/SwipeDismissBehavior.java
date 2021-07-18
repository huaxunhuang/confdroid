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
 * An interaction behavior plugin for child views of {@link CoordinatorLayout} to provide support
 * for the 'swipe-to-dismiss' gesture.
 */
public class SwipeDismissBehavior<V extends android.view.View> extends android.support.design.widget.CoordinatorLayout.Behavior<V> {
    /**
     * A view is not currently being dragged or animating as a result of a fling/snap.
     */
    public static final int STATE_IDLE = android.support.v4.widget.ViewDragHelper.STATE_IDLE;

    /**
     * A view is currently being dragged. The position is currently changing as a result
     * of user input or simulated user input.
     */
    public static final int STATE_DRAGGING = android.support.v4.widget.ViewDragHelper.STATE_DRAGGING;

    /**
     * A view is currently settling into place as a result of a fling or
     * predefined non-interactive motion.
     */
    public static final int STATE_SETTLING = android.support.v4.widget.ViewDragHelper.STATE_SETTLING;

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @android.support.annotation.IntDef({ android.support.design.widget.SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END, android.support.design.widget.SwipeDismissBehavior.SWIPE_DIRECTION_END_TO_START, android.support.design.widget.SwipeDismissBehavior.SWIPE_DIRECTION_ANY })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface SwipeDirection {}

    /**
     * Swipe direction that only allows swiping in the direction of start-to-end. That is
     * left-to-right in LTR, or right-to-left in RTL.
     */
    public static final int SWIPE_DIRECTION_START_TO_END = 0;

    /**
     * Swipe direction that only allows swiping in the direction of end-to-start. That is
     * right-to-left in LTR or left-to-right in RTL.
     */
    public static final int SWIPE_DIRECTION_END_TO_START = 1;

    /**
     * Swipe direction which allows swiping in either direction.
     */
    public static final int SWIPE_DIRECTION_ANY = 2;

    private static final float DEFAULT_DRAG_DISMISS_THRESHOLD = 0.5F;

    private static final float DEFAULT_ALPHA_START_DISTANCE = 0.0F;

    private static final float DEFAULT_ALPHA_END_DISTANCE = android.support.design.widget.SwipeDismissBehavior.DEFAULT_DRAG_DISMISS_THRESHOLD;

    android.support.v4.widget.ViewDragHelper mViewDragHelper;

    android.support.design.widget.SwipeDismissBehavior.OnDismissListener mListener;

    private boolean mInterceptingEvents;

    private float mSensitivity = 0.0F;

    private boolean mSensitivitySet;

    int mSwipeDirection = android.support.design.widget.SwipeDismissBehavior.SWIPE_DIRECTION_ANY;

    float mDragDismissThreshold = android.support.design.widget.SwipeDismissBehavior.DEFAULT_DRAG_DISMISS_THRESHOLD;

    float mAlphaStartSwipeDistance = android.support.design.widget.SwipeDismissBehavior.DEFAULT_ALPHA_START_DISTANCE;

    float mAlphaEndSwipeDistance = android.support.design.widget.SwipeDismissBehavior.DEFAULT_ALPHA_END_DISTANCE;

    /**
     * Callback interface used to notify the application that the view has been dismissed.
     */
    public interface OnDismissListener {
        /**
         * Called when {@code view} has been dismissed via swiping.
         */
        public void onDismiss(android.view.View view);

        /**
         * Called when the drag state has changed.
         *
         * @param state
         * 		the new state. One of
         * 		{@link #STATE_IDLE}, {@link #STATE_DRAGGING} or {@link #STATE_SETTLING}.
         */
        public void onDragStateChanged(int state);
    }

    /**
     * Set the listener to be used when a dismiss event occurs.
     *
     * @param listener
     * 		the listener to use.
     */
    public void setListener(android.support.design.widget.SwipeDismissBehavior.OnDismissListener listener) {
        mListener = listener;
    }

    /**
     * Sets the swipe direction for this behavior.
     *
     * @param direction
     * 		one of the {@link #SWIPE_DIRECTION_START_TO_END},
     * 		{@link #SWIPE_DIRECTION_END_TO_START} or {@link #SWIPE_DIRECTION_ANY}
     */
    public void setSwipeDirection(@android.support.design.widget.SwipeDismissBehavior.SwipeDirection
    int direction) {
        mSwipeDirection = direction;
    }

    /**
     * Set the threshold for telling if a view has been dragged enough to be dismissed.
     *
     * @param distance
     * 		a ratio of a view's width, values are clamped to 0 >= x <= 1f;
     */
    public void setDragDismissDistance(float distance) {
        mDragDismissThreshold = android.support.design.widget.SwipeDismissBehavior.clamp(0.0F, distance, 1.0F);
    }

    /**
     * The minimum swipe distance before the view's alpha is modified.
     *
     * @param fraction
     * 		the distance as a fraction of the view's width.
     */
    public void setStartAlphaSwipeDistance(float fraction) {
        mAlphaStartSwipeDistance = android.support.design.widget.SwipeDismissBehavior.clamp(0.0F, fraction, 1.0F);
    }

    /**
     * The maximum swipe distance for the view's alpha is modified.
     *
     * @param fraction
     * 		the distance as a fraction of the view's width.
     */
    public void setEndAlphaSwipeDistance(float fraction) {
        mAlphaEndSwipeDistance = android.support.design.widget.SwipeDismissBehavior.clamp(0.0F, fraction, 1.0F);
    }

    /**
     * Set the sensitivity used for detecting the start of a swipe. This only takes effect if
     * no touch handling has occured yet.
     *
     * @param sensitivity
     * 		Multiplier for how sensitive we should be about detecting
     * 		the start of a drag. Larger values are more sensitive. 1.0f is normal.
     */
    public void setSensitivity(float sensitivity) {
        mSensitivity = sensitivity;
        mSensitivitySet = true;
    }

    @java.lang.Override
    public boolean onInterceptTouchEvent(android.support.design.widget.CoordinatorLayout parent, V child, android.view.MotionEvent event) {
        boolean dispatchEventToHelper = mInterceptingEvents;
        switch (android.support.v4.view.MotionEventCompat.getActionMasked(event)) {
            case android.view.MotionEvent.ACTION_DOWN :
                mInterceptingEvents = parent.isPointInChildBounds(child, ((int) (event.getX())), ((int) (event.getY())));
                dispatchEventToHelper = mInterceptingEvents;
                break;
            case android.view.MotionEvent.ACTION_UP :
            case android.view.MotionEvent.ACTION_CANCEL :
                // Reset the ignore flag for next time
                mInterceptingEvents = false;
                break;
        }
        if (dispatchEventToHelper) {
            ensureViewDragHelper(parent);
            return mViewDragHelper.shouldInterceptTouchEvent(event);
        }
        return false;
    }

    @java.lang.Override
    public boolean onTouchEvent(android.support.design.widget.CoordinatorLayout parent, V child, android.view.MotionEvent event) {
        if (mViewDragHelper != null) {
            mViewDragHelper.processTouchEvent(event);
            return true;
        }
        return false;
    }

    /**
     * Called when the user's input indicates that they want to swipe the given view.
     *
     * @param view
     * 		View the user is attempting to swipe
     * @return true if the view can be dismissed via swiping, false otherwise
     */
    public boolean canSwipeDismissView(@android.support.annotation.NonNull
    android.view.View view) {
        return true;
    }

    private final android.support.v4.widget.ViewDragHelper.Callback mDragCallback = new android.support.v4.widget.ViewDragHelper.Callback() {
        private static final int INVALID_POINTER_ID = -1;

        private int mOriginalCapturedViewLeft;

        private int mActivePointerId = INVALID_POINTER_ID;

        @java.lang.Override
        public boolean tryCaptureView(android.view.View child, int pointerId) {
            // Only capture if we don't already have an active pointer id
            return (mActivePointerId == INVALID_POINTER_ID) && canSwipeDismissView(child);
        }

        @java.lang.Override
        public void onViewCaptured(android.view.View capturedChild, int activePointerId) {
            mActivePointerId = activePointerId;
            mOriginalCapturedViewLeft = capturedChild.getLeft();
            // The view has been captured, and thus a drag is about to start so stop any parents
            // intercepting
            final android.view.ViewParent parent = capturedChild.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }

        @java.lang.Override
        public void onViewDragStateChanged(int state) {
            if (mListener != null) {
                mListener.onDragStateChanged(state);
            }
        }

        @java.lang.Override
        public void onViewReleased(android.view.View child, float xvel, float yvel) {
            // Reset the active pointer ID
            mActivePointerId = INVALID_POINTER_ID;
            final int childWidth = child.getWidth();
            int targetLeft;
            boolean dismiss = false;
            if (shouldDismiss(child, xvel)) {
                targetLeft = (child.getLeft() < mOriginalCapturedViewLeft) ? mOriginalCapturedViewLeft - childWidth : mOriginalCapturedViewLeft + childWidth;
                dismiss = true;
            } else {
                // Else, reset back to the original left
                targetLeft = mOriginalCapturedViewLeft;
            }
            if (mViewDragHelper.settleCapturedViewAt(targetLeft, child.getTop())) {
                android.support.v4.view.ViewCompat.postOnAnimation(child, new SettleRunnable(child, dismiss));
            } else
                if (dismiss && (mListener != null)) {
                    mListener.onDismiss(child);
                }

        }

        private boolean shouldDismiss(android.view.View child, float xvel) {
            if (xvel != 0.0F) {
                final boolean isRtl = android.support.v4.view.ViewCompat.getLayoutDirection(child) == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL;
                if (mSwipeDirection == android.support.design.widget.SwipeDismissBehavior.SWIPE_DIRECTION_ANY) {
                    // We don't care about the direction so return true
                    return true;
                } else
                    if (mSwipeDirection == android.support.design.widget.SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END) {
                        // We only allow start-to-end swiping, so the fling needs to be in the
                        // correct direction
                        return isRtl ? xvel < 0.0F : xvel > 0.0F;
                    } else
                        if (mSwipeDirection == android.support.design.widget.SwipeDismissBehavior.SWIPE_DIRECTION_END_TO_START) {
                            // We only allow end-to-start swiping, so the fling needs to be in the
                            // correct direction
                            return isRtl ? xvel > 0.0F : xvel < 0.0F;
                        }


            } else {
                final int distance = child.getLeft() - mOriginalCapturedViewLeft;
                final int thresholdDistance = java.lang.Math.round(child.getWidth() * mDragDismissThreshold);
                return java.lang.Math.abs(distance) >= thresholdDistance;
            }
            return false;
        }

        @java.lang.Override
        public int getViewHorizontalDragRange(android.view.View child) {
            return child.getWidth();
        }

        @java.lang.Override
        public int clampViewPositionHorizontal(android.view.View child, int left, int dx) {
            final boolean isRtl = android.support.v4.view.ViewCompat.getLayoutDirection(child) == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL;
            int min;
            int max;
            if (mSwipeDirection == android.support.design.widget.SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END) {
                if (isRtl) {
                    min = mOriginalCapturedViewLeft - child.getWidth();
                    max = mOriginalCapturedViewLeft;
                } else {
                    min = mOriginalCapturedViewLeft;
                    max = mOriginalCapturedViewLeft + child.getWidth();
                }
            } else
                if (mSwipeDirection == android.support.design.widget.SwipeDismissBehavior.SWIPE_DIRECTION_END_TO_START) {
                    if (isRtl) {
                        min = mOriginalCapturedViewLeft;
                        max = mOriginalCapturedViewLeft + child.getWidth();
                    } else {
                        min = mOriginalCapturedViewLeft - child.getWidth();
                        max = mOriginalCapturedViewLeft;
                    }
                } else {
                    min = mOriginalCapturedViewLeft - child.getWidth();
                    max = mOriginalCapturedViewLeft + child.getWidth();
                }

            return android.support.design.widget.SwipeDismissBehavior.clamp(min, left, max);
        }

        @java.lang.Override
        public int clampViewPositionVertical(android.view.View child, int top, int dy) {
            return child.getTop();
        }

        @java.lang.Override
        public void onViewPositionChanged(android.view.View child, int left, int top, int dx, int dy) {
            final float startAlphaDistance = mOriginalCapturedViewLeft + (child.getWidth() * mAlphaStartSwipeDistance);
            final float endAlphaDistance = mOriginalCapturedViewLeft + (child.getWidth() * mAlphaEndSwipeDistance);
            if (left <= startAlphaDistance) {
                android.support.v4.view.ViewCompat.setAlpha(child, 1.0F);
            } else
                if (left >= endAlphaDistance) {
                    android.support.v4.view.ViewCompat.setAlpha(child, 0.0F);
                } else {
                    // We're between the start and end distances
                    final float distance = android.support.design.widget.SwipeDismissBehavior.fraction(startAlphaDistance, endAlphaDistance, left);
                    android.support.v4.view.ViewCompat.setAlpha(child, android.support.design.widget.SwipeDismissBehavior.clamp(0.0F, 1.0F - distance, 1.0F));
                }

        }
    };

    private void ensureViewDragHelper(android.view.ViewGroup parent) {
        if (mViewDragHelper == null) {
            mViewDragHelper = (mSensitivitySet) ? android.support.v4.widget.ViewDragHelper.create(parent, mSensitivity, mDragCallback) : android.support.v4.widget.ViewDragHelper.create(parent, mDragCallback);
        }
    }

    private class SettleRunnable implements java.lang.Runnable {
        private final android.view.View mView;

        private final boolean mDismiss;

        SettleRunnable(android.view.View view, boolean dismiss) {
            mView = view;
            mDismiss = dismiss;
        }

        @java.lang.Override
        public void run() {
            if ((mViewDragHelper != null) && mViewDragHelper.continueSettling(true)) {
                android.support.v4.view.ViewCompat.postOnAnimation(mView, this);
            } else {
                if (mDismiss && (mListener != null)) {
                    mListener.onDismiss(mView);
                }
            }
        }
    }

    static float clamp(float min, float value, float max) {
        return java.lang.Math.min(java.lang.Math.max(min, value), max);
    }

    static int clamp(int min, int value, int max) {
        return java.lang.Math.min(java.lang.Math.max(min, value), max);
    }

    /**
     * Retrieve the current drag state of this behavior. This will return one of
     * {@link #STATE_IDLE}, {@link #STATE_DRAGGING} or {@link #STATE_SETTLING}.
     *
     * @return The current drag state
     */
    public int getDragState() {
        return mViewDragHelper != null ? mViewDragHelper.getViewDragState() : android.support.design.widget.SwipeDismissBehavior.STATE_IDLE;
    }

    /**
     * The fraction that {@code value} is between {@code startValue} and {@code endValue}.
     */
    static float fraction(float startValue, float endValue, float value) {
        return (value - startValue) / (endValue - startValue);
    }
}

