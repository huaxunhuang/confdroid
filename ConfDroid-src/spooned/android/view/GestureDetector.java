/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.view;


/**
 * Detects various gestures and events using the supplied {@link MotionEvent}s.
 * The {@link OnGestureListener} callback will notify users when a particular
 * motion event has occurred. This class should only be used with {@link MotionEvent}s
 * reported via touch (don't use for trackball events).
 *
 * To use this class:
 * <ul>
 *  <li>Create an instance of the {@code GestureDetector} for your {@link View}
 *  <li>In the {@link View#onTouchEvent(MotionEvent)} method ensure you call
 *          {@link #onTouchEvent(MotionEvent)}. The methods defined in your callback
 *          will be executed when the events occur.
 *  <li>If listening for {@link OnContextClickListener#onContextClick(MotionEvent)}
 *          you must call {@link #onGenericMotionEvent(MotionEvent)}
 *          in {@link View#onGenericMotionEvent(MotionEvent)}.
 * </ul>
 */
public class GestureDetector {
    /**
     * The listener that is used to notify when gestures occur.
     * If you want to listen for all the different gestures then implement
     * this interface. If you only want to listen for a subset it might
     * be easier to extend {@link SimpleOnGestureListener}.
     */
    public interface OnGestureListener {
        /**
         * Notified when a tap occurs with the down {@link MotionEvent}
         * that triggered it. This will be triggered immediately for
         * every down event. All other events should be preceded by this.
         *
         * @param e
         * 		The down motion event.
         */
        boolean onDown(android.view.MotionEvent e);

        /**
         * The user has performed a down {@link MotionEvent} and not performed
         * a move or up yet. This event is commonly used to provide visual
         * feedback to the user to let them know that their action has been
         * recognized i.e. highlight an element.
         *
         * @param e
         * 		The down motion event
         */
        void onShowPress(android.view.MotionEvent e);

        /**
         * Notified when a tap occurs with the up {@link MotionEvent}
         * that triggered it.
         *
         * @param e
         * 		The up motion event that completed the first tap
         * @return true if the event is consumed, else false
         */
        boolean onSingleTapUp(android.view.MotionEvent e);

        /**
         * Notified when a scroll occurs with the initial on down {@link MotionEvent} and the
         * current move {@link MotionEvent}. The distance in x and y is also supplied for
         * convenience.
         *
         * @param e1
         * 		The first down motion event that started the scrolling.
         * @param e2
         * 		The move motion event that triggered the current onScroll.
         * @param distanceX
         * 		The distance along the X axis that has been scrolled since the last
         * 		call to onScroll. This is NOT the distance between {@code e1}
         * 		and {@code e2}.
         * @param distanceY
         * 		The distance along the Y axis that has been scrolled since the last
         * 		call to onScroll. This is NOT the distance between {@code e1}
         * 		and {@code e2}.
         * @return true if the event is consumed, else false
         */
        boolean onScroll(android.view.MotionEvent e1, android.view.MotionEvent e2, float distanceX, float distanceY);

        /**
         * Notified when a long press occurs with the initial on down {@link MotionEvent}
         * that trigged it.
         *
         * @param e
         * 		The initial on down motion event that started the longpress.
         */
        void onLongPress(android.view.MotionEvent e);

        /**
         * Notified of a fling event when it occurs with the initial on down {@link MotionEvent}
         * and the matching up {@link MotionEvent}. The calculated velocity is supplied along
         * the x and y axis in pixels per second.
         *
         * @param e1
         * 		The first down motion event that started the fling.
         * @param e2
         * 		The move motion event that triggered the current onFling.
         * @param velocityX
         * 		The velocity of this fling measured in pixels per second
         * 		along the x axis.
         * @param velocityY
         * 		The velocity of this fling measured in pixels per second
         * 		along the y axis.
         * @return true if the event is consumed, else false
         */
        boolean onFling(android.view.MotionEvent e1, android.view.MotionEvent e2, float velocityX, float velocityY);
    }

    /**
     * The listener that is used to notify when a double-tap or a confirmed
     * single-tap occur.
     */
    public interface OnDoubleTapListener {
        /**
         * Notified when a single-tap occurs.
         * <p>
         * Unlike {@link OnGestureListener#onSingleTapUp(MotionEvent)}, this
         * will only be called after the detector is confident that the user's
         * first tap is not followed by a second tap leading to a double-tap
         * gesture.
         *
         * @param e
         * 		The down motion event of the single-tap.
         * @return true if the event is consumed, else false
         */
        boolean onSingleTapConfirmed(android.view.MotionEvent e);

        /**
         * Notified when a double-tap occurs.
         *
         * @param e
         * 		The down motion event of the first tap of the double-tap.
         * @return true if the event is consumed, else false
         */
        boolean onDoubleTap(android.view.MotionEvent e);

        /**
         * Notified when an event within a double-tap gesture occurs, including
         * the down, move, and up events.
         *
         * @param e
         * 		The motion event that occurred during the double-tap gesture.
         * @return true if the event is consumed, else false
         */
        boolean onDoubleTapEvent(android.view.MotionEvent e);
    }

    /**
     * The listener that is used to notify when a context click occurs. When listening for a
     * context click ensure that you call {@link #onGenericMotionEvent(MotionEvent)} in
     * {@link View#onGenericMotionEvent(MotionEvent)}.
     */
    public interface OnContextClickListener {
        /**
         * Notified when a context click occurs.
         *
         * @param e
         * 		The motion event that occurred during the context click.
         * @return true if the event is consumed, else false
         */
        boolean onContextClick(android.view.MotionEvent e);
    }

    /**
     * A convenience class to extend when you only want to listen for a subset
     * of all the gestures. This implements all methods in the
     * {@link OnGestureListener}, {@link OnDoubleTapListener}, and {@link OnContextClickListener}
     * but does nothing and return {@code false} for all applicable methods.
     */
    public static class SimpleOnGestureListener implements android.view.GestureDetector.OnContextClickListener , android.view.GestureDetector.OnDoubleTapListener , android.view.GestureDetector.OnGestureListener {
        public boolean onSingleTapUp(android.view.MotionEvent e) {
            return false;
        }

        public void onLongPress(android.view.MotionEvent e) {
        }

        public boolean onScroll(android.view.MotionEvent e1, android.view.MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        public boolean onFling(android.view.MotionEvent e1, android.view.MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        public void onShowPress(android.view.MotionEvent e) {
        }

        public boolean onDown(android.view.MotionEvent e) {
            return false;
        }

        public boolean onDoubleTap(android.view.MotionEvent e) {
            return false;
        }

        public boolean onDoubleTapEvent(android.view.MotionEvent e) {
            return false;
        }

        public boolean onSingleTapConfirmed(android.view.MotionEvent e) {
            return false;
        }

        public boolean onContextClick(android.view.MotionEvent e) {
            return false;
        }
    }

    @android.annotation.UnsupportedAppUsage
    private int mTouchSlopSquare;

    private int mDoubleTapTouchSlopSquare;

    private int mDoubleTapSlopSquare;

    @android.annotation.UnsupportedAppUsage
    private int mMinimumFlingVelocity;

    private int mMaximumFlingVelocity;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    private static final int LONGPRESS_TIMEOUT = android.view.ViewConfiguration.getLongPressTimeout();

    private static final int TAP_TIMEOUT = android.view.ViewConfiguration.getTapTimeout();

    private static final int DOUBLE_TAP_TIMEOUT = android.view.ViewConfiguration.getDoubleTapTimeout();

    private static final int DOUBLE_TAP_MIN_TIME = android.view.ViewConfiguration.getDoubleTapMinTime();

    // constants for Message.what used by GestureHandler below
    private static final int SHOW_PRESS = 1;

    private static final int LONG_PRESS = 2;

    private static final int TAP = 3;

    private final android.os.Handler mHandler;

    @android.annotation.UnsupportedAppUsage
    private final android.view.GestureDetector.OnGestureListener mListener;

    private android.view.GestureDetector.OnDoubleTapListener mDoubleTapListener;

    private android.view.GestureDetector.OnContextClickListener mContextClickListener;

    private boolean mStillDown;

    private boolean mDeferConfirmSingleTap;

    private boolean mInLongPress;

    private boolean mInContextClick;

    @android.annotation.UnsupportedAppUsage
    private boolean mAlwaysInTapRegion;

    private boolean mAlwaysInBiggerTapRegion;

    private boolean mIgnoreNextUpEvent;

    // Whether a classification has been recorded by statsd for the current event stream. Reset on
    // ACTION_DOWN.
    private boolean mHasRecordedClassification;

    private android.view.MotionEvent mCurrentDownEvent;

    private android.view.MotionEvent mCurrentMotionEvent;

    private android.view.MotionEvent mPreviousUpEvent;

    /**
     * True when the user is still touching for the second tap (down, move, and
     * up events). Can only be true if there is a double tap listener attached.
     */
    private boolean mIsDoubleTapping;

    private float mLastFocusX;

    private float mLastFocusY;

    private float mDownFocusX;

    private float mDownFocusY;

    private boolean mIsLongpressEnabled;

    /**
     * Determines speed during touch scrolling
     */
    private android.view.VelocityTracker mVelocityTracker;

    /**
     * Consistency verifier for debugging purposes.
     */
    private final android.view.InputEventConsistencyVerifier mInputEventConsistencyVerifier = (android.view.InputEventConsistencyVerifier.isInstrumentationEnabled()) ? new android.view.InputEventConsistencyVerifier(this, 0) : null;

    private class GestureHandler extends android.os.Handler {
        GestureHandler() {
            super();
        }

        GestureHandler(android.os.Handler handler) {
            super(handler.getLooper());
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.view.GestureDetector.SHOW_PRESS :
                    mListener.onShowPress(mCurrentDownEvent);
                    break;
                case android.view.GestureDetector.LONG_PRESS :
                    recordGestureClassification(msg.arg1);
                    dispatchLongPress();
                    break;
                case android.view.GestureDetector.TAP :
                    // If the user's finger is still down, do not count it as a tap
                    if (mDoubleTapListener != null) {
                        if (!mStillDown) {
                            recordGestureClassification(android.util.StatsLog.TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__SINGLE_TAP);
                            mDoubleTapListener.onSingleTapConfirmed(mCurrentDownEvent);
                        } else {
                            mDeferConfirmSingleTap = true;
                        }
                    }
                    break;
                default :
                    throw new java.lang.RuntimeException("Unknown message " + msg);// never

            }
        }
    }

    /**
     * Creates a GestureDetector with the supplied listener.
     * This variant of the constructor should be used from a non-UI thread
     * (as it allows specifying the Handler).
     *
     * @param listener
     * 		the listener invoked for all the callbacks, this must
     * 		not be null.
     * @param handler
     * 		the handler to use
     * @throws NullPointerException
     * 		if either {@code listener} or
     * 		{@code handler} is null.
     * @deprecated Use {@link #GestureDetector(android.content.Context,
    android.view.GestureDetector.OnGestureListener, android.os.Handler)} instead.
     */
    @java.lang.Deprecated
    public GestureDetector(android.view.GestureDetector.OnGestureListener listener, android.os.Handler handler) {
        this(null, listener, handler);
    }

    /**
     * Creates a GestureDetector with the supplied listener.
     * You may only use this constructor from a UI thread (this is the usual situation).
     *
     * @see android.os.Handler#Handler()
     * @param listener
     * 		the listener invoked for all the callbacks, this must
     * 		not be null.
     * @throws NullPointerException
     * 		if {@code listener} is null.
     * @deprecated Use {@link #GestureDetector(android.content.Context,
    android.view.GestureDetector.OnGestureListener)} instead.
     */
    @java.lang.Deprecated
    public GestureDetector(android.view.GestureDetector.OnGestureListener listener) {
        this(null, listener, null);
    }

    /**
     * Creates a GestureDetector with the supplied listener.
     * You may only use this constructor from a {@link android.os.Looper} thread.
     *
     * @see android.os.Handler#Handler()
     * @param context
     * 		the application's context
     * @param listener
     * 		the listener invoked for all the callbacks, this must
     * 		not be null.
     * @throws NullPointerException
     * 		if {@code listener} is null.
     */
    public GestureDetector(android.content.Context context, android.view.GestureDetector.OnGestureListener listener) {
        this(context, listener, null);
    }

    /**
     * Creates a GestureDetector with the supplied listener that runs deferred events on the
     * thread associated with the supplied {@link android.os.Handler}.
     *
     * @see android.os.Handler#Handler()
     * @param context
     * 		the application's context
     * @param listener
     * 		the listener invoked for all the callbacks, this must
     * 		not be null.
     * @param handler
     * 		the handler to use for running deferred listener events.
     * @throws NullPointerException
     * 		if {@code listener} is null.
     */
    public GestureDetector(android.content.Context context, android.view.GestureDetector.OnGestureListener listener, android.os.Handler handler) {
        if (handler != null) {
            mHandler = new android.view.GestureDetector.GestureHandler(handler);
        } else {
            mHandler = new android.view.GestureDetector.GestureHandler();
        }
        mListener = listener;
        if (listener instanceof android.view.GestureDetector.OnDoubleTapListener) {
            setOnDoubleTapListener(((android.view.GestureDetector.OnDoubleTapListener) (listener)));
        }
        if (listener instanceof android.view.GestureDetector.OnContextClickListener) {
            setContextClickListener(((android.view.GestureDetector.OnContextClickListener) (listener)));
        }
        init(context);
    }

    /**
     * Creates a GestureDetector with the supplied listener that runs deferred events on the
     * thread associated with the supplied {@link android.os.Handler}.
     *
     * @see android.os.Handler#Handler()
     * @param context
     * 		the application's context
     * @param listener
     * 		the listener invoked for all the callbacks, this must
     * 		not be null.
     * @param handler
     * 		the handler to use for running deferred listener events.
     * @param unused
     * 		currently not used.
     * @throws NullPointerException
     * 		if {@code listener} is null.
     */
    public GestureDetector(android.content.Context context, android.view.GestureDetector.OnGestureListener listener, android.os.Handler handler, boolean unused) {
        this(context, listener, handler);
    }

    private void init(android.content.Context context) {
        if (mListener == null) {
            throw new java.lang.NullPointerException("OnGestureListener must not be null");
        }
        mIsLongpressEnabled = true;
        // Fallback to support pre-donuts releases
        int touchSlop;
        int doubleTapSlop;
        int doubleTapTouchSlop;
        if (context == null) {
            // noinspection deprecation
            touchSlop = android.view.ViewConfiguration.getTouchSlop();
            doubleTapTouchSlop = touchSlop;// Hack rather than adding a hidden method for this

            doubleTapSlop = android.view.ViewConfiguration.getDoubleTapSlop();
            // noinspection deprecation
            mMinimumFlingVelocity = android.view.ViewConfiguration.getMinimumFlingVelocity();
            mMaximumFlingVelocity = android.view.ViewConfiguration.getMaximumFlingVelocity();
        } else {
            final android.view.ViewConfiguration configuration = android.view.ViewConfiguration.get(context);
            touchSlop = configuration.getScaledTouchSlop();
            doubleTapTouchSlop = configuration.getScaledDoubleTapTouchSlop();
            doubleTapSlop = configuration.getScaledDoubleTapSlop();
            mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
            mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
        }
        mTouchSlopSquare = touchSlop * touchSlop;
        mDoubleTapTouchSlopSquare = doubleTapTouchSlop * doubleTapTouchSlop;
        mDoubleTapSlopSquare = doubleTapSlop * doubleTapSlop;
    }

    /**
     * Sets the listener which will be called for double-tap and related
     * gestures.
     *
     * @param onDoubleTapListener
     * 		the listener invoked for all the callbacks, or
     * 		null to stop listening for double-tap gestures.
     */
    public void setOnDoubleTapListener(android.view.GestureDetector.OnDoubleTapListener onDoubleTapListener) {
        mDoubleTapListener = onDoubleTapListener;
    }

    /**
     * Sets the listener which will be called for context clicks.
     *
     * @param onContextClickListener
     * 		the listener invoked for all the callbacks, or null to stop
     * 		listening for context clicks.
     */
    public void setContextClickListener(android.view.GestureDetector.OnContextClickListener onContextClickListener) {
        mContextClickListener = onContextClickListener;
    }

    /**
     * Set whether longpress is enabled, if this is enabled when a user
     * presses and holds down you get a longpress event and nothing further.
     * If it's disabled the user can press and hold down and then later
     * moved their finger and you will get scroll events. By default
     * longpress is enabled.
     *
     * @param isLongpressEnabled
     * 		whether longpress should be enabled.
     */
    public void setIsLongpressEnabled(boolean isLongpressEnabled) {
        mIsLongpressEnabled = isLongpressEnabled;
    }

    /**
     *
     *
     * @return true if longpress is enabled, else false.
     */
    public boolean isLongpressEnabled() {
        return mIsLongpressEnabled;
    }

    /**
     * Analyzes the given motion event and if applicable triggers the
     * appropriate callbacks on the {@link OnGestureListener} supplied.
     *
     * @param ev
     * 		The current motion event.
     * @return true if the {@link OnGestureListener} consumed the event,
    else false.
     */
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        if (mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onTouchEvent(ev, 0);
        }
        final int action = ev.getAction();
        if (mCurrentMotionEvent != null) {
            mCurrentMotionEvent.recycle();
        }
        mCurrentMotionEvent = android.view.MotionEvent.obtain(ev);
        if (mVelocityTracker == null) {
            mVelocityTracker = android.view.VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        final boolean pointerUp = (action & android.view.MotionEvent.ACTION_MASK) == android.view.MotionEvent.ACTION_POINTER_UP;
        final int skipIndex = (pointerUp) ? ev.getActionIndex() : -1;
        final boolean isGeneratedGesture = (ev.getFlags() & android.view.MotionEvent.FLAG_IS_GENERATED_GESTURE) != 0;
        // Determine focal point
        float sumX = 0;
        float sumY = 0;
        final int count = ev.getPointerCount();
        for (int i = 0; i < count; i++) {
            if (skipIndex == i)
                continue;

            sumX += ev.getX(i);
            sumY += ev.getY(i);
        }
        final int div = (pointerUp) ? count - 1 : count;
        final float focusX = sumX / div;
        final float focusY = sumY / div;
        boolean handled = false;
        switch (action & android.view.MotionEvent.ACTION_MASK) {
            case android.view.MotionEvent.ACTION_POINTER_DOWN :
                mDownFocusX = mLastFocusX = focusX;
                mDownFocusY = mLastFocusY = focusY;
                // Cancel long press and taps
                cancelTaps();
                break;
            case android.view.MotionEvent.ACTION_POINTER_UP :
                mDownFocusX = mLastFocusX = focusX;
                mDownFocusY = mLastFocusY = focusY;
                // Check the dot product of current velocities.
                // If the pointer that left was opposing another velocity vector, clear.
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                final int upIndex = ev.getActionIndex();
                final int id1 = ev.getPointerId(upIndex);
                final float x1 = mVelocityTracker.getXVelocity(id1);
                final float y1 = mVelocityTracker.getYVelocity(id1);
                for (int i = 0; i < count; i++) {
                    if (i == upIndex)
                        continue;

                    final int id2 = ev.getPointerId(i);
                    final float x = x1 * mVelocityTracker.getXVelocity(id2);
                    final float y = y1 * mVelocityTracker.getYVelocity(id2);
                    final float dot = x + y;
                    if (dot < 0) {
                        mVelocityTracker.clear();
                        break;
                    }
                }
                break;
            case android.view.MotionEvent.ACTION_DOWN :
                if (mDoubleTapListener != null) {
                    boolean hadTapMessage = mHandler.hasMessages(android.view.GestureDetector.TAP);
                    if (hadTapMessage)
                        mHandler.removeMessages(android.view.GestureDetector.TAP);

                    if ((((mCurrentDownEvent != null) && (mPreviousUpEvent != null)) && hadTapMessage) && isConsideredDoubleTap(mCurrentDownEvent, mPreviousUpEvent, ev)) {
                        // This is a second tap
                        mIsDoubleTapping = true;
                        recordGestureClassification(android.util.StatsLog.TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__DOUBLE_TAP);
                        // Give a callback with the first tap of the double-tap
                        handled |= mDoubleTapListener.onDoubleTap(mCurrentDownEvent);
                        // Give a callback with down event of the double-tap
                        handled |= mDoubleTapListener.onDoubleTapEvent(ev);
                    } else {
                        // This is a first tap
                        mHandler.sendEmptyMessageDelayed(android.view.GestureDetector.TAP, android.view.GestureDetector.DOUBLE_TAP_TIMEOUT);
                    }
                }
                mDownFocusX = mLastFocusX = focusX;
                mDownFocusY = mLastFocusY = focusY;
                if (mCurrentDownEvent != null) {
                    mCurrentDownEvent.recycle();
                }
                mCurrentDownEvent = android.view.MotionEvent.obtain(ev);
                mAlwaysInTapRegion = true;
                mAlwaysInBiggerTapRegion = true;
                mStillDown = true;
                mInLongPress = false;
                mDeferConfirmSingleTap = false;
                mHasRecordedClassification = false;
                if (mIsLongpressEnabled) {
                    mHandler.removeMessages(android.view.GestureDetector.LONG_PRESS);
                    mHandler.sendMessageAtTime(/* arg2 */
                    mHandler.obtainMessage(android.view.GestureDetector.LONG_PRESS, android.util.StatsLog.TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__LONG_PRESS, 0), mCurrentDownEvent.getDownTime() + android.view.ViewConfiguration.getLongPressTimeout());
                }
                mHandler.sendEmptyMessageAtTime(android.view.GestureDetector.SHOW_PRESS, mCurrentDownEvent.getDownTime() + android.view.GestureDetector.TAP_TIMEOUT);
                handled |= mListener.onDown(ev);
                break;
            case android.view.MotionEvent.ACTION_MOVE :
                if (mInLongPress || mInContextClick) {
                    break;
                }
                final int motionClassification = ev.getClassification();
                final boolean hasPendingLongPress = mHandler.hasMessages(android.view.GestureDetector.LONG_PRESS);
                final float scrollX = mLastFocusX - focusX;
                final float scrollY = mLastFocusY - focusY;
                if (mIsDoubleTapping) {
                    // Give the move events of the double-tap
                    recordGestureClassification(android.util.StatsLog.TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__DOUBLE_TAP);
                    handled |= mDoubleTapListener.onDoubleTapEvent(ev);
                } else
                    if (mAlwaysInTapRegion) {
                        final int deltaX = ((int) (focusX - mDownFocusX));
                        final int deltaY = ((int) (focusY - mDownFocusY));
                        int distance = (deltaX * deltaX) + (deltaY * deltaY);
                        int slopSquare = (isGeneratedGesture) ? 0 : mTouchSlopSquare;
                        final boolean ambiguousGesture = motionClassification == android.view.MotionEvent.CLASSIFICATION_AMBIGUOUS_GESTURE;
                        final boolean shouldInhibitDefaultAction = hasPendingLongPress && ambiguousGesture;
                        if (shouldInhibitDefaultAction) {
                            // Inhibit default long press
                            final float multiplier = android.view.ViewConfiguration.getAmbiguousGestureMultiplier();
                            if (distance > slopSquare) {
                                // The default action here is to remove long press. But if the touch
                                // slop below gets increased, and we never exceed the modified touch
                                // slop while still receiving AMBIGUOUS_GESTURE, we risk that *nothing*
                                // will happen in response to user input. To prevent this,
                                // reschedule long press with a modified timeout.
                                mHandler.removeMessages(android.view.GestureDetector.LONG_PRESS);
                                final long longPressTimeout = android.view.ViewConfiguration.getLongPressTimeout();
                                mHandler.sendMessageAtTime(/* arg2 */
                                mHandler.obtainMessage(android.view.GestureDetector.LONG_PRESS, android.util.StatsLog.TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__LONG_PRESS, 0), ev.getDownTime() + ((long) (longPressTimeout * multiplier)));
                            }
                            // Inhibit default scroll. If a gesture is ambiguous, we prevent scroll
                            // until the gesture is resolved.
                            // However, for safety, simply increase the touch slop in case the
                            // classification is erroneous. Since the value is squared, multiply twice.
                            slopSquare *= multiplier * multiplier;
                        }
                        if (distance > slopSquare) {
                            recordGestureClassification(android.util.StatsLog.TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__SCROLL);
                            handled = mListener.onScroll(mCurrentDownEvent, ev, scrollX, scrollY);
                            mLastFocusX = focusX;
                            mLastFocusY = focusY;
                            mAlwaysInTapRegion = false;
                            mHandler.removeMessages(android.view.GestureDetector.TAP);
                            mHandler.removeMessages(android.view.GestureDetector.SHOW_PRESS);
                            mHandler.removeMessages(android.view.GestureDetector.LONG_PRESS);
                        }
                        int doubleTapSlopSquare = (isGeneratedGesture) ? 0 : mDoubleTapTouchSlopSquare;
                        if (distance > doubleTapSlopSquare) {
                            mAlwaysInBiggerTapRegion = false;
                        }
                    } else
                        if ((java.lang.Math.abs(scrollX) >= 1) || (java.lang.Math.abs(scrollY) >= 1)) {
                            recordGestureClassification(android.util.StatsLog.TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__SCROLL);
                            handled = mListener.onScroll(mCurrentDownEvent, ev, scrollX, scrollY);
                            mLastFocusX = focusX;
                            mLastFocusY = focusY;
                        }


                final boolean deepPress = motionClassification == android.view.MotionEvent.CLASSIFICATION_DEEP_PRESS;
                if (deepPress && hasPendingLongPress) {
                    mHandler.removeMessages(android.view.GestureDetector.LONG_PRESS);
                    mHandler.sendMessage(/* arg2 */
                    mHandler.obtainMessage(android.view.GestureDetector.LONG_PRESS, android.util.StatsLog.TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__DEEP_PRESS, 0));
                }
                break;
            case android.view.MotionEvent.ACTION_UP :
                mStillDown = false;
                android.view.MotionEvent currentUpEvent = android.view.MotionEvent.obtain(ev);
                if (mIsDoubleTapping) {
                    // Finally, give the up event of the double-tap
                    recordGestureClassification(android.util.StatsLog.TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__DOUBLE_TAP);
                    handled |= mDoubleTapListener.onDoubleTapEvent(ev);
                } else
                    if (mInLongPress) {
                        mHandler.removeMessages(android.view.GestureDetector.TAP);
                        mInLongPress = false;
                    } else
                        if (mAlwaysInTapRegion && (!mIgnoreNextUpEvent)) {
                            recordGestureClassification(android.util.StatsLog.TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__SINGLE_TAP);
                            handled = mListener.onSingleTapUp(ev);
                            if (mDeferConfirmSingleTap && (mDoubleTapListener != null)) {
                                mDoubleTapListener.onSingleTapConfirmed(ev);
                            }
                        } else
                            if (!mIgnoreNextUpEvent) {
                                // A fling must travel the minimum tap distance
                                final android.view.VelocityTracker velocityTracker = mVelocityTracker;
                                final int pointerId = ev.getPointerId(0);
                                velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                                final float velocityY = velocityTracker.getYVelocity(pointerId);
                                final float velocityX = velocityTracker.getXVelocity(pointerId);
                                if ((java.lang.Math.abs(velocityY) > mMinimumFlingVelocity) || (java.lang.Math.abs(velocityX) > mMinimumFlingVelocity)) {
                                    handled = mListener.onFling(mCurrentDownEvent, ev, velocityX, velocityY);
                                }
                            }



                if (mPreviousUpEvent != null) {
                    mPreviousUpEvent.recycle();
                }
                // Hold the event we obtained above - listeners may have changed the original.
                mPreviousUpEvent = currentUpEvent;
                if (mVelocityTracker != null) {
                    // This may have been cleared when we called out to the
                    // application above.
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                mIsDoubleTapping = false;
                mDeferConfirmSingleTap = false;
                mIgnoreNextUpEvent = false;
                mHandler.removeMessages(android.view.GestureDetector.SHOW_PRESS);
                mHandler.removeMessages(android.view.GestureDetector.LONG_PRESS);
                break;
            case android.view.MotionEvent.ACTION_CANCEL :
                cancel();
                break;
        }
        if ((!handled) && (mInputEventConsistencyVerifier != null)) {
            mInputEventConsistencyVerifier.onUnhandledEvent(ev, 0);
        }
        return handled;
    }

    /**
     * Analyzes the given generic motion event and if applicable triggers the
     * appropriate callbacks on the {@link OnGestureListener} supplied.
     *
     * @param ev
     * 		The current motion event.
     * @return true if the {@link OnGestureListener} consumed the event,
    else false.
     */
    public boolean onGenericMotionEvent(android.view.MotionEvent ev) {
        if (mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onGenericMotionEvent(ev, 0);
        }
        final int actionButton = ev.getActionButton();
        switch (ev.getActionMasked()) {
            case android.view.MotionEvent.ACTION_BUTTON_PRESS :
                if ((((mContextClickListener != null) && (!mInContextClick)) && (!mInLongPress)) && ((actionButton == android.view.MotionEvent.BUTTON_STYLUS_PRIMARY) || (actionButton == android.view.MotionEvent.BUTTON_SECONDARY))) {
                    if (mContextClickListener.onContextClick(ev)) {
                        mInContextClick = true;
                        mHandler.removeMessages(android.view.GestureDetector.LONG_PRESS);
                        mHandler.removeMessages(android.view.GestureDetector.TAP);
                        return true;
                    }
                }
                break;
            case android.view.MotionEvent.ACTION_BUTTON_RELEASE :
                if (mInContextClick && ((actionButton == android.view.MotionEvent.BUTTON_STYLUS_PRIMARY) || (actionButton == android.view.MotionEvent.BUTTON_SECONDARY))) {
                    mInContextClick = false;
                    mIgnoreNextUpEvent = true;
                }
                break;
        }
        return false;
    }

    private void cancel() {
        mHandler.removeMessages(android.view.GestureDetector.SHOW_PRESS);
        mHandler.removeMessages(android.view.GestureDetector.LONG_PRESS);
        mHandler.removeMessages(android.view.GestureDetector.TAP);
        mVelocityTracker.recycle();
        mVelocityTracker = null;
        mIsDoubleTapping = false;
        mStillDown = false;
        mAlwaysInTapRegion = false;
        mAlwaysInBiggerTapRegion = false;
        mDeferConfirmSingleTap = false;
        mInLongPress = false;
        mInContextClick = false;
        mIgnoreNextUpEvent = false;
    }

    private void cancelTaps() {
        mHandler.removeMessages(android.view.GestureDetector.SHOW_PRESS);
        mHandler.removeMessages(android.view.GestureDetector.LONG_PRESS);
        mHandler.removeMessages(android.view.GestureDetector.TAP);
        mIsDoubleTapping = false;
        mAlwaysInTapRegion = false;
        mAlwaysInBiggerTapRegion = false;
        mDeferConfirmSingleTap = false;
        mInLongPress = false;
        mInContextClick = false;
        mIgnoreNextUpEvent = false;
    }

    private boolean isConsideredDoubleTap(android.view.MotionEvent firstDown, android.view.MotionEvent firstUp, android.view.MotionEvent secondDown) {
        if (!mAlwaysInBiggerTapRegion) {
            return false;
        }
        final long deltaTime = secondDown.getEventTime() - firstUp.getEventTime();
        if ((deltaTime > android.view.GestureDetector.DOUBLE_TAP_TIMEOUT) || (deltaTime < android.view.GestureDetector.DOUBLE_TAP_MIN_TIME)) {
            return false;
        }
        int deltaX = ((int) (firstDown.getX())) - ((int) (secondDown.getX()));
        int deltaY = ((int) (firstDown.getY())) - ((int) (secondDown.getY()));
        final boolean isGeneratedGesture = (firstDown.getFlags() & android.view.MotionEvent.FLAG_IS_GENERATED_GESTURE) != 0;
        int slopSquare = (isGeneratedGesture) ? 0 : mDoubleTapSlopSquare;
        return ((deltaX * deltaX) + (deltaY * deltaY)) < slopSquare;
    }

    private void dispatchLongPress() {
        mHandler.removeMessages(android.view.GestureDetector.TAP);
        mDeferConfirmSingleTap = false;
        mInLongPress = true;
        mListener.onLongPress(mCurrentDownEvent);
    }

    private void recordGestureClassification(int classification) {
        if (mHasRecordedClassification || (classification == android.util.StatsLog.TOUCH_GESTURE_CLASSIFIED__CLASSIFICATION__UNKNOWN_CLASSIFICATION)) {
            // Only record the first classification for an event stream.
            return;
        }
        if ((mCurrentDownEvent == null) || (mCurrentMotionEvent == null)) {
            // If the complete event stream wasn't seen, don't record anything.
            mHasRecordedClassification = true;
            return;
        }
        android.util.StatsLog.write(StatsLog.TOUCH_GESTURE_CLASSIFIED, getClass().getName(), classification, ((int) (android.os.SystemClock.uptimeMillis() - mCurrentMotionEvent.getDownTime())), ((float) (java.lang.Math.hypot(mCurrentMotionEvent.getRawX() - mCurrentDownEvent.getRawX(), mCurrentMotionEvent.getRawY() - mCurrentDownEvent.getRawY()))));
        mHasRecordedClassification = true;
    }
}

