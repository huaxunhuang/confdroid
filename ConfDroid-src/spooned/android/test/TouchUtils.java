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
package android.test;


/**
 * Reusable methods for generating touch events. These methods can be used with
 * InstrumentationTestCase or ActivityInstrumentationTestCase2 to simulate user interaction with
 * the application through a touch screen.
 *
 * @deprecated Use
<a href="{@docRoot }training/testing/ui-testing/espresso-testing.html">Espresso UI testing
framework</a> instead. New tests should be written using the
<a href="{@docRoot }tools/testing-support-library/index.html">Android Testing Support Library</a>.
 */
@java.lang.Deprecated
public class TouchUtils {
    /**
     * Simulate touching in the center of the screen and dragging one quarter of the way down
     *
     * @param test
     * 		The test case that is being run
     * @deprecated {@link android.test.ActivityInstrumentationTestCase} is deprecated in favor of
    {@link android.test.ActivityInstrumentationTestCase2}, which provides more options for
    configuring the Activity under test
     */
    @java.lang.Deprecated
    public static void dragQuarterScreenDown(android.test.ActivityInstrumentationTestCase test) {
        android.test.TouchUtils.dragQuarterScreenDown(test, test.getActivity());
    }

    /**
     * Simulate touching in the center of the screen and dragging one quarter of the way down
     *
     * @param test
     * 		The test case that is being run
     * @param activity
     * 		The activity that is in the foreground of the test case
     */
    public static void dragQuarterScreenDown(android.test.InstrumentationTestCase test, android.app.Activity activity) {
        android.view.Display display = activity.getWindowManager().getDefaultDisplay();
        final android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        final float x = size.x / 2.0F;
        final float fromY = size.y * 0.5F;
        final float toY = size.y * 0.75F;
        android.test.TouchUtils.drag(test, x, x, fromY, toY, 4);
    }

    /**
     * Simulate touching in the center of the screen and dragging one quarter of the way up
     *
     * @param test
     * 		The test case that is being run
     * @deprecated {@link android.test.ActivityInstrumentationTestCase} is deprecated in favor of
    {@link android.test.ActivityInstrumentationTestCase2}, which provides more options for
    configuring the Activity under test
     */
    @java.lang.Deprecated
    public static void dragQuarterScreenUp(android.test.ActivityInstrumentationTestCase test) {
        android.test.TouchUtils.dragQuarterScreenUp(test, test.getActivity());
    }

    /**
     * Simulate touching in the center of the screen and dragging one quarter of the way up
     *
     * @param test
     * 		The test case that is being run
     * @param activity
     * 		The activity that is in the foreground of the test case
     */
    public static void dragQuarterScreenUp(android.test.InstrumentationTestCase test, android.app.Activity activity) {
        android.view.Display display = activity.getWindowManager().getDefaultDisplay();
        final android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        final float x = size.x / 2.0F;
        final float fromY = size.y * 0.5F;
        final float toY = size.y * 0.25F;
        android.test.TouchUtils.drag(test, x, x, fromY, toY, 4);
    }

    /**
     * Scroll a ViewGroup to the bottom by repeatedly calling
     * {@link #dragQuarterScreenUp(InstrumentationTestCase, Activity)}
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The ViewGroup that should be dragged
     * @deprecated {@link android.test.ActivityInstrumentationTestCase} is deprecated in favor of
    {@link android.test.ActivityInstrumentationTestCase2}, which provides more options for
    configuring the Activity under test
     */
    @java.lang.Deprecated
    public static void scrollToBottom(android.test.ActivityInstrumentationTestCase test, android.view.ViewGroup v) {
        android.test.TouchUtils.scrollToBottom(test, test.getActivity(), v);
    }

    /**
     * Scroll a ViewGroup to the bottom by repeatedly calling
     * {@link #dragQuarterScreenUp(InstrumentationTestCase, Activity)}
     *
     * @param test
     * 		The test case that is being run
     * @param activity
     * 		The activity that is in the foreground of the test case
     * @param v
     * 		The ViewGroup that should be dragged
     */
    public static void scrollToBottom(android.test.InstrumentationTestCase test, android.app.Activity activity, android.view.ViewGroup v) {
        android.test.TouchUtils.ViewStateSnapshot prev;
        android.test.TouchUtils.ViewStateSnapshot next = new android.test.TouchUtils.ViewStateSnapshot(v);
        do {
            prev = next;
            android.test.TouchUtils.dragQuarterScreenUp(test, activity);
            next = new android.test.TouchUtils.ViewStateSnapshot(v);
        } while (!prev.equals(next) );
    }

    /**
     * Scroll a ViewGroup to the top by repeatedly calling
     * {@link #dragQuarterScreenDown(InstrumentationTestCase, Activity)}
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The ViewGroup that should be dragged
     * @deprecated {@link android.test.ActivityInstrumentationTestCase} is deprecated in favor of
    {@link android.test.ActivityInstrumentationTestCase2}, which provides more options for
    configuring the Activity under test
     */
    @java.lang.Deprecated
    public static void scrollToTop(android.test.ActivityInstrumentationTestCase test, android.view.ViewGroup v) {
        android.test.TouchUtils.scrollToTop(test, test.getActivity(), v);
    }

    /**
     * Scroll a ViewGroup to the top by repeatedly calling
     * {@link #dragQuarterScreenDown(InstrumentationTestCase, Activity)}
     *
     * @param test
     * 		The test case that is being run
     * @param activity
     * 		The activity that is in the foreground of the test case
     * @param v
     * 		The ViewGroup that should be dragged
     */
    public static void scrollToTop(android.test.InstrumentationTestCase test, android.app.Activity activity, android.view.ViewGroup v) {
        android.test.TouchUtils.ViewStateSnapshot prev;
        android.test.TouchUtils.ViewStateSnapshot next = new android.test.TouchUtils.ViewStateSnapshot(v);
        do {
            prev = next;
            android.test.TouchUtils.dragQuarterScreenDown(test, activity);
            next = new android.test.TouchUtils.ViewStateSnapshot(v);
        } while (!prev.equals(next) );
    }

    /**
     * Simulate touching the center of a view and dragging to the bottom of the screen.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be dragged
     * @deprecated {@link android.test.ActivityInstrumentationTestCase} is deprecated in favor of
    {@link android.test.ActivityInstrumentationTestCase2}, which provides more options for
    configuring the Activity under test
     */
    @java.lang.Deprecated
    public static void dragViewToBottom(android.test.ActivityInstrumentationTestCase test, android.view.View v) {
        android.test.TouchUtils.dragViewToBottom(test, test.getActivity(), v, 4);
    }

    /**
     * Simulate touching the center of a view and dragging to the bottom of the screen.
     *
     * @param test
     * 		The test case that is being run
     * @param activity
     * 		The activity that is in the foreground of the test case
     * @param v
     * 		The view that should be dragged
     */
    public static void dragViewToBottom(android.test.InstrumentationTestCase test, android.app.Activity activity, android.view.View v) {
        android.test.TouchUtils.dragViewToBottom(test, activity, v, 4);
    }

    /**
     * Simulate touching the center of a view and dragging to the bottom of the screen.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be dragged
     * @param stepCount
     * 		How many move steps to include in the drag
     * @deprecated {@link android.test.ActivityInstrumentationTestCase} is deprecated in favor of
    {@link android.test.ActivityInstrumentationTestCase2}, which provides more options for
    configuring the Activity under test
     */
    @java.lang.Deprecated
    public static void dragViewToBottom(android.test.ActivityInstrumentationTestCase test, android.view.View v, int stepCount) {
        android.test.TouchUtils.dragViewToBottom(test, test.getActivity(), v, stepCount);
    }

    /**
     * Simulate touching the center of a view and dragging to the bottom of the screen.
     *
     * @param test
     * 		The test case that is being run
     * @param activity
     * 		The activity that is in the foreground of the test case
     * @param v
     * 		The view that should be dragged
     * @param stepCount
     * 		How many move steps to include in the drag
     */
    public static void dragViewToBottom(android.test.InstrumentationTestCase test, android.app.Activity activity, android.view.View v, int stepCount) {
        int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        int[] xy = new int[2];
        v.getLocationOnScreen(xy);
        final int viewWidth = v.getWidth();
        final int viewHeight = v.getHeight();
        final float x = xy[0] + (viewWidth / 2.0F);
        float fromY = xy[1] + (viewHeight / 2.0F);
        float toY = screenHeight - 1;
        android.test.TouchUtils.drag(test, x, x, fromY, toY, stepCount);
    }

    /**
     * Simulate touching the center of a view and releasing quickly (before the tap timeout).
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be clicked
     */
    public static void tapView(android.test.InstrumentationTestCase test, android.view.View v) {
        int[] xy = new int[2];
        v.getLocationOnScreen(xy);
        final int viewWidth = v.getWidth();
        final int viewHeight = v.getHeight();
        final float x = xy[0] + (viewWidth / 2.0F);
        float y = xy[1] + (viewHeight / 2.0F);
        android.app.Instrumentation inst = test.getInstrumentation();
        long downTime = android.os.SystemClock.uptimeMillis();
        long eventTime = android.os.SystemClock.uptimeMillis();
        android.view.MotionEvent event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_DOWN, x, y, 0);
        inst.sendPointerSync(event);
        inst.waitForIdleSync();
        eventTime = android.os.SystemClock.uptimeMillis();
        final int touchSlop = android.view.ViewConfiguration.get(v.getContext()).getScaledTouchSlop();
        event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_MOVE, x + (touchSlop / 2.0F), y + (touchSlop / 2.0F), 0);
        inst.sendPointerSync(event);
        inst.waitForIdleSync();
        eventTime = android.os.SystemClock.uptimeMillis();
        event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_UP, x, y, 0);
        inst.sendPointerSync(event);
        inst.waitForIdleSync();
    }

    /**
     * Simulate touching the center of a view and cancelling (so no onClick should
     * fire, etc).
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be clicked
     */
    public static void touchAndCancelView(android.test.InstrumentationTestCase test, android.view.View v) {
        int[] xy = new int[2];
        v.getLocationOnScreen(xy);
        final int viewWidth = v.getWidth();
        final int viewHeight = v.getHeight();
        final float x = xy[0] + (viewWidth / 2.0F);
        float y = xy[1] + (viewHeight / 2.0F);
        android.app.Instrumentation inst = test.getInstrumentation();
        long downTime = android.os.SystemClock.uptimeMillis();
        long eventTime = android.os.SystemClock.uptimeMillis();
        android.view.MotionEvent event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_DOWN, x, y, 0);
        inst.sendPointerSync(event);
        inst.waitForIdleSync();
        eventTime = android.os.SystemClock.uptimeMillis();
        final int touchSlop = android.view.ViewConfiguration.get(v.getContext()).getScaledTouchSlop();
        event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_CANCEL, x + (touchSlop / 2.0F), y + (touchSlop / 2.0F), 0);
        inst.sendPointerSync(event);
        inst.waitForIdleSync();
    }

    /**
     * Simulate touching the center of a view and releasing.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be clicked
     */
    public static void clickView(android.test.InstrumentationTestCase test, android.view.View v) {
        int[] xy = new int[2];
        v.getLocationOnScreen(xy);
        final int viewWidth = v.getWidth();
        final int viewHeight = v.getHeight();
        final float x = xy[0] + (viewWidth / 2.0F);
        float y = xy[1] + (viewHeight / 2.0F);
        android.app.Instrumentation inst = test.getInstrumentation();
        long downTime = android.os.SystemClock.uptimeMillis();
        long eventTime = android.os.SystemClock.uptimeMillis();
        android.view.MotionEvent event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_DOWN, x, y, 0);
        inst.sendPointerSync(event);
        inst.waitForIdleSync();
        eventTime = android.os.SystemClock.uptimeMillis();
        final int touchSlop = android.view.ViewConfiguration.get(v.getContext()).getScaledTouchSlop();
        event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_MOVE, x + (touchSlop / 2.0F), y + (touchSlop / 2.0F), 0);
        inst.sendPointerSync(event);
        inst.waitForIdleSync();
        eventTime = android.os.SystemClock.uptimeMillis();
        event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_UP, x, y, 0);
        inst.sendPointerSync(event);
        inst.waitForIdleSync();
        try {
            java.lang.Thread.sleep(1000);
        } catch (java.lang.InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Simulate touching the center of a view, holding until it is a long press, and then releasing.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be clicked
     * @deprecated {@link android.test.ActivityInstrumentationTestCase} is deprecated in favor of
    {@link android.test.ActivityInstrumentationTestCase2}, which provides more options for
    configuring the Activity under test
     */
    @java.lang.Deprecated
    public static void longClickView(android.test.ActivityInstrumentationTestCase test, android.view.View v) {
        android.test.TouchUtils.longClickView(((android.test.InstrumentationTestCase) (test)), v);
    }

    /**
     * Simulate touching the center of a view, holding until it is a long press, and then releasing.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be clicked
     */
    public static void longClickView(android.test.InstrumentationTestCase test, android.view.View v) {
        int[] xy = new int[2];
        v.getLocationOnScreen(xy);
        final int viewWidth = v.getWidth();
        final int viewHeight = v.getHeight();
        final float x = xy[0] + (viewWidth / 2.0F);
        float y = xy[1] + (viewHeight / 2.0F);
        android.app.Instrumentation inst = test.getInstrumentation();
        long downTime = android.os.SystemClock.uptimeMillis();
        long eventTime = android.os.SystemClock.uptimeMillis();
        android.view.MotionEvent event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_DOWN, x, y, 0);
        inst.sendPointerSync(event);
        inst.waitForIdleSync();
        eventTime = android.os.SystemClock.uptimeMillis();
        final int touchSlop = android.view.ViewConfiguration.get(v.getContext()).getScaledTouchSlop();
        event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_MOVE, x + (touchSlop / 2), y + (touchSlop / 2), 0);
        inst.sendPointerSync(event);
        inst.waitForIdleSync();
        try {
            java.lang.Thread.sleep(((long) (android.view.ViewConfiguration.getLongPressTimeout() * 1.5F)));
        } catch (java.lang.InterruptedException e) {
            e.printStackTrace();
        }
        eventTime = android.os.SystemClock.uptimeMillis();
        event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_UP, x, y, 0);
        inst.sendPointerSync(event);
        inst.waitForIdleSync();
    }

    /**
     * Simulate touching the center of a view and dragging to the top of the screen.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be dragged
     * @deprecated {@link android.test.ActivityInstrumentationTestCase} is deprecated in favor of
    {@link android.test.ActivityInstrumentationTestCase2}, which provides more options for
    configuring the Activity under test
     */
    @java.lang.Deprecated
    public static void dragViewToTop(android.test.ActivityInstrumentationTestCase test, android.view.View v) {
        android.test.TouchUtils.dragViewToTop(((android.test.InstrumentationTestCase) (test)), v, 4);
    }

    /**
     * Simulate touching the center of a view and dragging to the top of the screen.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be dragged
     * @param stepCount
     * 		How many move steps to include in the drag
     * @deprecated {@link android.test.ActivityInstrumentationTestCase} is deprecated in favor of
    {@link android.test.ActivityInstrumentationTestCase2}, which provides more options for
    configuring the Activity under test
     */
    @java.lang.Deprecated
    public static void dragViewToTop(android.test.ActivityInstrumentationTestCase test, android.view.View v, int stepCount) {
        android.test.TouchUtils.dragViewToTop(((android.test.InstrumentationTestCase) (test)), v, stepCount);
    }

    /**
     * Simulate touching the center of a view and dragging to the top of the screen.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be dragged
     */
    public static void dragViewToTop(android.test.InstrumentationTestCase test, android.view.View v) {
        android.test.TouchUtils.dragViewToTop(test, v, 4);
    }

    /**
     * Simulate touching the center of a view and dragging to the top of the screen.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be dragged
     * @param stepCount
     * 		How many move steps to include in the drag
     */
    public static void dragViewToTop(android.test.InstrumentationTestCase test, android.view.View v, int stepCount) {
        int[] xy = new int[2];
        v.getLocationOnScreen(xy);
        final int viewWidth = v.getWidth();
        final int viewHeight = v.getHeight();
        final float x = xy[0] + (viewWidth / 2.0F);
        float fromY = xy[1] + (viewHeight / 2.0F);
        float toY = 0;
        android.test.TouchUtils.drag(test, x, x, fromY, toY, stepCount);
    }

    /**
     * Get the location of a view. Use the gravity param to specify which part of the view to
     * return.
     *
     * @param v
     * 		View to find
     * @param gravity
     * 		A combination of (TOP, CENTER_VERTICAL, BOTTOM) and (LEFT, CENTER_HORIZONTAL,
     * 		RIGHT)
     * @param xy
     * 		Result
     */
    private static void getStartLocation(android.view.View v, int gravity, int[] xy) {
        v.getLocationOnScreen(xy);
        final int viewWidth = v.getWidth();
        final int viewHeight = v.getHeight();
        switch (gravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) {
            case android.view.Gravity.TOP :
                break;
            case android.view.Gravity.CENTER_VERTICAL :
                xy[1] += viewHeight / 2;
                break;
            case android.view.Gravity.BOTTOM :
                xy[1] += viewHeight - 1;
                break;
            default :
                // Same as top -- do nothing
        }
        switch (gravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK) {
            case android.view.Gravity.LEFT :
                break;
            case android.view.Gravity.CENTER_HORIZONTAL :
                xy[0] += viewWidth / 2;
                break;
            case android.view.Gravity.RIGHT :
                xy[0] += viewWidth - 1;
                break;
            default :
                // Same as left -- do nothing
        }
    }

    /**
     * Simulate touching a view and dragging it by the specified amount.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be dragged
     * @param gravity
     * 		Which part of the view to use for the initial down event. A combination of
     * 		(TOP, CENTER_VERTICAL, BOTTOM) and (LEFT, CENTER_HORIZONTAL, RIGHT)
     * @param deltaX
     * 		Amount to drag horizontally in pixels
     * @param deltaY
     * 		Amount to drag vertically in pixels
     * @return distance in pixels covered by the drag
     * @deprecated {@link android.test.ActivityInstrumentationTestCase} is deprecated in favor of
    {@link android.test.ActivityInstrumentationTestCase2}, which provides more options for
    configuring the Activity under test
     */
    @java.lang.Deprecated
    public static int dragViewBy(android.test.ActivityInstrumentationTestCase test, android.view.View v, int gravity, int deltaX, int deltaY) {
        return android.test.TouchUtils.dragViewBy(((android.test.InstrumentationTestCase) (test)), v, gravity, deltaX, deltaY);
    }

    /**
     * Simulate touching a view and dragging it by the specified amount.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be dragged
     * @param gravity
     * 		Which part of the view to use for the initial down event. A combination of
     * 		(TOP, CENTER_VERTICAL, BOTTOM) and (LEFT, CENTER_HORIZONTAL, RIGHT)
     * @param deltaX
     * 		Amount to drag horizontally in pixels
     * @param deltaY
     * 		Amount to drag vertically in pixels
     * @return distance in pixels covered by the drag
     * @deprecated {@link android.test.ActivityInstrumentationTestCase} is deprecated in favor of
    {@link android.test.ActivityInstrumentationTestCase2}, which provides more options for
    configuring the Activity under test
     */
    @java.lang.Deprecated
    public static int dragViewBy(android.test.InstrumentationTestCase test, android.view.View v, int gravity, int deltaX, int deltaY) {
        int[] xy = new int[2];
        android.test.TouchUtils.getStartLocation(v, gravity, xy);
        final int fromX = xy[0];
        final int fromY = xy[1];
        int distance = ((int) (java.lang.Math.hypot(deltaX, deltaY)));
        android.test.TouchUtils.drag(test, fromX, fromX + deltaX, fromY, fromY + deltaY, distance);
        return distance;
    }

    /**
     * Simulate touching a view and dragging it to a specified location.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be dragged
     * @param gravity
     * 		Which part of the view to use for the initial down event. A combination of
     * 		(TOP, CENTER_VERTICAL, BOTTOM) and (LEFT, CENTER_HORIZONTAL, RIGHT)
     * @param toX
     * 		Final location of the view after dragging
     * @param toY
     * 		Final location of the view after dragging
     * @return distance in pixels covered by the drag
     * @deprecated {@link android.test.ActivityInstrumentationTestCase} is deprecated in favor of
    {@link android.test.ActivityInstrumentationTestCase2}, which provides more options for
    configuring the Activity under test
     */
    @java.lang.Deprecated
    public static int dragViewTo(android.test.ActivityInstrumentationTestCase test, android.view.View v, int gravity, int toX, int toY) {
        return android.test.TouchUtils.dragViewTo(((android.test.InstrumentationTestCase) (test)), v, gravity, toX, toY);
    }

    /**
     * Simulate touching a view and dragging it to a specified location.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be dragged
     * @param gravity
     * 		Which part of the view to use for the initial down event. A combination of
     * 		(TOP, CENTER_VERTICAL, BOTTOM) and (LEFT, CENTER_HORIZONTAL, RIGHT)
     * @param toX
     * 		Final location of the view after dragging
     * @param toY
     * 		Final location of the view after dragging
     * @return distance in pixels covered by the drag
     */
    public static int dragViewTo(android.test.InstrumentationTestCase test, android.view.View v, int gravity, int toX, int toY) {
        int[] xy = new int[2];
        android.test.TouchUtils.getStartLocation(v, gravity, xy);
        final int fromX = xy[0];
        final int fromY = xy[1];
        int deltaX = fromX - toX;
        int deltaY = fromY - toY;
        int distance = ((int) (java.lang.Math.hypot(deltaX, deltaY)));
        android.test.TouchUtils.drag(test, fromX, toX, fromY, toY, distance);
        return distance;
    }

    /**
     * Simulate touching a view and dragging it to a specified location. Only moves horizontally.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be dragged
     * @param gravity
     * 		Which part of the view to use for the initial down event. A combination of
     * 		(TOP, CENTER_VERTICAL, BOTTOM) and (LEFT, CENTER_HORIZONTAL, RIGHT)
     * @param toX
     * 		Final location of the view after dragging
     * @return distance in pixels covered by the drag
     * @deprecated {@link android.test.ActivityInstrumentationTestCase} is deprecated in favor of
    {@link android.test.ActivityInstrumentationTestCase2}, which provides more options for
    configuring the Activity under test
     */
    @java.lang.Deprecated
    public static int dragViewToX(android.test.ActivityInstrumentationTestCase test, android.view.View v, int gravity, int toX) {
        return android.test.TouchUtils.dragViewToX(((android.test.InstrumentationTestCase) (test)), v, gravity, toX);
    }

    /**
     * Simulate touching a view and dragging it to a specified location. Only moves horizontally.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be dragged
     * @param gravity
     * 		Which part of the view to use for the initial down event. A combination of
     * 		(TOP, CENTER_VERTICAL, BOTTOM) and (LEFT, CENTER_HORIZONTAL, RIGHT)
     * @param toX
     * 		Final location of the view after dragging
     * @return distance in pixels covered by the drag
     */
    public static int dragViewToX(android.test.InstrumentationTestCase test, android.view.View v, int gravity, int toX) {
        int[] xy = new int[2];
        android.test.TouchUtils.getStartLocation(v, gravity, xy);
        final int fromX = xy[0];
        final int fromY = xy[1];
        int deltaX = fromX - toX;
        android.test.TouchUtils.drag(test, fromX, toX, fromY, fromY, deltaX);
        return deltaX;
    }

    /**
     * Simulate touching a view and dragging it to a specified location. Only moves vertically.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be dragged
     * @param gravity
     * 		Which part of the view to use for the initial down event. A combination of
     * 		(TOP, CENTER_VERTICAL, BOTTOM) and (LEFT, CENTER_HORIZONTAL, RIGHT)
     * @param toY
     * 		Final location of the view after dragging
     * @return distance in pixels covered by the drag
     * @deprecated {@link android.test.ActivityInstrumentationTestCase} is deprecated in favor of
    {@link android.test.ActivityInstrumentationTestCase2}, which provides more options for
    configuring the Activity under test
     */
    @java.lang.Deprecated
    public static int dragViewToY(android.test.ActivityInstrumentationTestCase test, android.view.View v, int gravity, int toY) {
        return android.test.TouchUtils.dragViewToY(((android.test.InstrumentationTestCase) (test)), v, gravity, toY);
    }

    /**
     * Simulate touching a view and dragging it to a specified location. Only moves vertically.
     *
     * @param test
     * 		The test case that is being run
     * @param v
     * 		The view that should be dragged
     * @param gravity
     * 		Which part of the view to use for the initial down event. A combination of
     * 		(TOP, CENTER_VERTICAL, BOTTOM) and (LEFT, CENTER_HORIZONTAL, RIGHT)
     * @param toY
     * 		Final location of the view after dragging
     * @return distance in pixels covered by the drag
     */
    public static int dragViewToY(android.test.InstrumentationTestCase test, android.view.View v, int gravity, int toY) {
        int[] xy = new int[2];
        android.test.TouchUtils.getStartLocation(v, gravity, xy);
        final int fromX = xy[0];
        final int fromY = xy[1];
        int deltaY = fromY - toY;
        android.test.TouchUtils.drag(test, fromX, fromX, fromY, toY, deltaY);
        return deltaY;
    }

    /**
     * Simulate touching a specific location and dragging to a new location.
     *
     * @param test
     * 		The test case that is being run
     * @param fromX
     * 		X coordinate of the initial touch, in screen coordinates
     * @param toX
     * 		Xcoordinate of the drag destination, in screen coordinates
     * @param fromY
     * 		X coordinate of the initial touch, in screen coordinates
     * @param toY
     * 		Y coordinate of the drag destination, in screen coordinates
     * @param stepCount
     * 		How many move steps to include in the drag
     * @deprecated {@link android.test.ActivityInstrumentationTestCase} is deprecated in favor of
    {@link android.test.ActivityInstrumentationTestCase2}, which provides more options for
    configuring the Activity under test
     */
    @java.lang.Deprecated
    public static void drag(android.test.ActivityInstrumentationTestCase test, float fromX, float toX, float fromY, float toY, int stepCount) {
        android.test.TouchUtils.drag(((android.test.InstrumentationTestCase) (test)), fromX, toX, fromY, toY, stepCount);
    }

    /**
     * Simulate touching a specific location and dragging to a new location.
     *
     * @param test
     * 		The test case that is being run
     * @param fromX
     * 		X coordinate of the initial touch, in screen coordinates
     * @param toX
     * 		Xcoordinate of the drag destination, in screen coordinates
     * @param fromY
     * 		X coordinate of the initial touch, in screen coordinates
     * @param toY
     * 		Y coordinate of the drag destination, in screen coordinates
     * @param stepCount
     * 		How many move steps to include in the drag
     */
    public static void drag(android.test.InstrumentationTestCase test, float fromX, float toX, float fromY, float toY, int stepCount) {
        android.app.Instrumentation inst = test.getInstrumentation();
        long downTime = android.os.SystemClock.uptimeMillis();
        long eventTime = android.os.SystemClock.uptimeMillis();
        float y = fromY;
        float x = fromX;
        float yStep = (toY - fromY) / stepCount;
        float xStep = (toX - fromX) / stepCount;
        android.view.MotionEvent event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_DOWN, x, y, 0);
        inst.sendPointerSync(event);
        for (int i = 0; i < stepCount; ++i) {
            y += yStep;
            x += xStep;
            eventTime = android.os.SystemClock.uptimeMillis();
            event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_MOVE, x, y, 0);
            inst.sendPointerSync(event);
        }
        eventTime = android.os.SystemClock.uptimeMillis();
        event = android.view.MotionEvent.obtain(downTime, eventTime, android.view.MotionEvent.ACTION_UP, x, y, 0);
        inst.sendPointerSync(event);
        inst.waitForIdleSync();
    }

    private static class ViewStateSnapshot {
        final android.view.View mFirst;

        final android.view.View mLast;

        final int mFirstTop;

        final int mLastBottom;

        final int mChildCount;

        private ViewStateSnapshot(android.view.ViewGroup viewGroup) {
            mChildCount = viewGroup.getChildCount();
            if (mChildCount == 0) {
                mFirst = mLast = null;
                mFirstTop = mLastBottom = java.lang.Integer.MIN_VALUE;
            } else {
                mFirst = viewGroup.getChildAt(0);
                mLast = viewGroup.getChildAt(mChildCount - 1);
                mFirstTop = mFirst.getTop();
                mLastBottom = mLast.getBottom();
            }
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            final android.test.TouchUtils.ViewStateSnapshot that = ((android.test.TouchUtils.ViewStateSnapshot) (o));
            return ((((mFirstTop == that.mFirstTop) && (mLastBottom == that.mLastBottom)) && (mFirst == that.mFirst)) && (mLast == that.mLast)) && (mChildCount == that.mChildCount);
        }

        @java.lang.Override
        public int hashCode() {
            int result = (mFirst != null) ? mFirst.hashCode() : 0;
            result = (31 * result) + (mLast != null ? mLast.hashCode() : 0);
            result = (31 * result) + mFirstTop;
            result = (31 * result) + mLastBottom;
            result = (31 * result) + mChildCount;
            return result;
        }
    }
}

