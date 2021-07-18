/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * Exercises {@link android.view.VelocityTracker} to compute correct velocity.<br>
 * To launch this test, use :<br>
 * <code>./development/testrunner/runtest.py framework -c android.view.VelocityTest</code>
 */
public class VelocityTest extends android.test.InstrumentationTestCase {
    @android.test.suitebuilder.annotation.MediumTest
    public void testInitialCondiditions() {
        android.view.VelocityTracker vt = android.view.VelocityTracker.obtain();
        assertNotNull(vt);
        vt.recycle();
    }

    /**
     * Test that {@link android.view.VelocityTracker}.clear() clears
     * the previous values after a call to computeCurrentVelocity()
     */
    @android.test.suitebuilder.annotation.MediumTest
    public void testClear() {
        long t = java.lang.System.currentTimeMillis();
        android.view.VelocityTracker vt = android.view.VelocityTracker.obtain();
        drag(vt, 100, 200, 100, 200, 10, t, 300);
        vt.computeCurrentVelocity(1);
        assertFalse("Velocity should not be null", vt.getXVelocity() == 0.0F);
        assertFalse("Velocity should not be null", vt.getYVelocity() == 0.0F);
        vt.clear();
        vt.computeCurrentVelocity(1);
        assertEquals(0.0F, vt.getXVelocity());
        assertEquals(0.0F, vt.getYVelocity());
        vt.recycle();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testDragAcceleration() {
        long t = java.lang.System.currentTimeMillis();
        android.view.VelocityTracker vt = android.view.VelocityTracker.obtain();
        drag(vt, 100, 200, 100, 200, 15, t, 400, new android.view.animation.AccelerateInterpolator());
        vt.computeCurrentVelocity(1000);
        assertGreater(250.0F, vt.getXVelocity());
        assertGreater(250.0F, vt.getYVelocity());
        vt.recycle();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testDragDeceleration() {
        long t = java.lang.System.currentTimeMillis();
        android.view.VelocityTracker vt = android.view.VelocityTracker.obtain();
        drag(vt, 100, 200, 100, 200, 15, t, 400, new android.view.animation.DecelerateInterpolator());
        vt.computeCurrentVelocity(1000);
        assertLower(250.0F, vt.getXVelocity());
        assertLower(250.0F, vt.getYVelocity());
        vt.recycle();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testDragLinearHorizontal() {
        long t = java.lang.System.currentTimeMillis();
        android.view.VelocityTracker vt = android.view.VelocityTracker.obtain();
        // 100px in 400ms => 250px/s
        drag(vt, 100, 200, 200, 200, 15, t, 400);
        vt.computeCurrentVelocity(1000);
        assertEquals(0.0F, vt.getYVelocity());
        assertEqualFuzzy(250.0F, vt.getXVelocity(), 4.0F);
        vt.recycle();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testDragLinearVertical() {
        long t = java.lang.System.currentTimeMillis();
        android.view.VelocityTracker vt = android.view.VelocityTracker.obtain();
        // 100px in 400ms => 250px/s
        drag(vt, 200, 200, 100, 200, 15, t, 400);
        vt.computeCurrentVelocity(1000);
        assertEquals(0.0F, vt.getXVelocity());
        assertEqualFuzzy(250.0F, vt.getYVelocity(), 4.0F);
        vt.recycle();
    }

    /**
     * Test dragging with two points only
     * (velocity must be an exact value)
     */
    @android.test.suitebuilder.annotation.MediumTest
    public void testDragWith2Points() {
        long t = java.lang.System.currentTimeMillis();
        android.view.VelocityTracker vt = android.view.VelocityTracker.obtain();
        // 100px, 2 steps, 100ms => 1000px/s
        drag(vt, 100, 200, 100, 200, 2, t, 100);
        vt.computeCurrentVelocity(1000);
        assertEquals(1000.0F, vt.getXVelocity());
        assertEquals(1000.0F, vt.getYVelocity());
        vt.recycle();
    }

    /**
     * Velocity is independent of the number of points used during
     * the same interval
     */
    @android.test.suitebuilder.annotation.MediumTest
    public void testStabilityInNbPoints() {
        long t = java.lang.System.currentTimeMillis();
        android.view.VelocityTracker vt = android.view.VelocityTracker.obtain();
        drag(vt, 100, 200, 100, 200, 10, t, 400);// 10 steps over 400ms

        vt.computeCurrentVelocity(1);
        float firstX = vt.getXVelocity();
        float firstY = vt.getYVelocity();
        vt.clear();
        drag(vt, 100, 200, 100, 200, 20, t, 400);// 20 steps over 400ms

        vt.computeCurrentVelocity(1);
        float secondX = vt.getXVelocity();
        float secondY = vt.getYVelocity();
        assertEqualFuzzy(firstX, secondX, 0.1F);
        assertEqualFuzzy(firstY, secondY, 0.1F);
        vt.recycle();
    }

    /**
     * Velocity is independent of the time when the events occurs,
     * it only depends on delays between the events.
     */
    @android.test.suitebuilder.annotation.MediumTest
    public void testStabilityInTime() {
        long t = java.lang.System.currentTimeMillis();
        android.view.VelocityTracker vt = android.view.VelocityTracker.obtain();
        drag(vt, 100, 200, 100, 200, 10, t, 400);
        vt.computeCurrentVelocity(1);
        float firstX = vt.getXVelocity();
        float firstY = vt.getYVelocity();
        vt.clear();
        drag(vt, 100, 200, 100, 200, 10, t + (3600 * 1000), 400);// on hour later

        vt.computeCurrentVelocity(1);
        float secondX = vt.getXVelocity();
        float secondY = vt.getYVelocity();
        assertEqualFuzzy(firstX, secondX, 0.1F);
        assertEqualFuzzy(firstY, secondY, 0.1F);
        vt.recycle();
    }

    /**
     * Velocity is independent of the position of the events,
     * it only depends on their relative distance.
     */
    @android.test.suitebuilder.annotation.MediumTest
    public void testStabilityInSpace() {
        long t = java.lang.System.currentTimeMillis();
        android.view.VelocityTracker vt = android.view.VelocityTracker.obtain();
        drag(vt, 100, 200, 100, 200, 10, t, 400);
        vt.computeCurrentVelocity(1);
        float firstX = vt.getXVelocity();
        float firstY = vt.getYVelocity();
        vt.clear();
        drag(vt, 200, 300, 200, 300, 10, t, 400);// 100px further

        vt.computeCurrentVelocity(1);
        float secondX = vt.getXVelocity();
        float secondY = vt.getYVelocity();
        assertEqualFuzzy(firstX, secondX, 0.1F);
        assertEqualFuzzy(firstY, secondY, 0.1F);
        vt.recycle();
    }

    /**
     * Test that calls to {@link android.view.VelocityTracker}.computeCurrentVelocity()
     * will output same values when using the same data.
     */
    @android.test.suitebuilder.annotation.MediumTest
    public void testStabilityOfComputation() {
        long t = java.lang.System.currentTimeMillis();
        android.view.VelocityTracker vt = android.view.VelocityTracker.obtain();
        drag(vt, 100, 200, 100, 200, 10, t, 300);
        vt.computeCurrentVelocity(1);
        float firstX = vt.getXVelocity();
        float firstY = vt.getYVelocity();
        vt.computeCurrentVelocity(1);
        float secondX = vt.getXVelocity();
        float secondY = vt.getYVelocity();
        assertEquals(firstX, secondX);
        assertEquals(firstY, secondY);
        vt.recycle();
    }

    /**
     * Test the units parameter of {@link android.view.VelocityTracker}.computeCurrentVelocity()
     */
    @android.test.suitebuilder.annotation.MediumTest
    public void testStabilityOfUnits() {
        long t = java.lang.System.currentTimeMillis();
        android.view.VelocityTracker vt = android.view.VelocityTracker.obtain();
        drag(vt, 100, 200, 100, 200, 10, t, 300);
        vt.computeCurrentVelocity(1);
        float firstX = vt.getXVelocity();
        float firstY = vt.getYVelocity();
        vt.computeCurrentVelocity(1000);
        float secondX = vt.getXVelocity();
        float secondY = vt.getYVelocity();
        assertEqualFuzzy(firstX, secondX / 1000.0F, 0.1F);
        assertEqualFuzzy(firstY, secondY / 1000.0F, 0.1F);
        vt.recycle();
    }

    /**
     * Simulate a drag by giving directly MotionEvents to
     * the VelocityTracker using a linear interpolator
     */
    private void drag(android.view.VelocityTracker vt, int startX, int endX, int startY, int endY, int steps, long startime, int duration) {
        drag(vt, startX, endX, startY, endY, steps, startime, duration, new android.view.animation.LinearInterpolator());
    }

    /**
     * Simulate a drag by giving directly MotionEvents to
     * the VelocityTracker using a given interpolator
     */
    private void drag(android.view.VelocityTracker vt, int startX, int endX, int startY, int endY, int steps, long startime, int duration, android.view.animation.Interpolator interpolator) {
        addMotionEvent(vt, startX, startY, startime, android.view.MotionEvent.ACTION_DOWN);
        float dt = duration / ((float) (steps));
        int distX = endX - startX;
        int distY = endY - startY;
        for (int i = 1; i < (steps - 1); i++) {
            float ii = interpolator.getInterpolation(i / ((float) (steps)));
            int x = ((int) (startX + (distX * ii)));
            int y = ((int) (startY + (distY * ii)));
            long time = startime + ((int) (i * dt));
            addMotionEvent(vt, x, y, time, android.view.MotionEvent.ACTION_MOVE);
        }
        addMotionEvent(vt, endX, endY, startime + duration, android.view.MotionEvent.ACTION_UP);
    }

    private void addMotionEvent(android.view.VelocityTracker vt, int x, int y, long time, int action) {
        android.view.MotionEvent me = android.view.MotionEvent.obtain(time, time, action, x, y, 0);
        vt.addMovement(me);
        me.recycle();
    }

    /**
     * Float imprecision of the average computations and filtering
     * (removing last MotionEvent for N > 3) implies that tests
     *  accepts some approximated values.
     */
    private void assertEqualFuzzy(float expected, float actual, float threshold) {
        boolean fuzzyEqual = (actual >= (expected - threshold)) && (actual <= (expected + threshold));
        junit.framework.Assert.assertTrue(((((("Expected: <" + expected) + "> but was: <") + actual) + "> while accepting a variation of: <") + threshold) + ">", fuzzyEqual);
    }

    private void assertGreater(float minExpected, float actual) {
        junit.framework.Assert.assertTrue(((("Expected: minimum <" + minExpected) + "> but was: <") + actual) + ">", actual > minExpected);
    }

    private void assertLower(float maxExpected, float actual) {
        junit.framework.Assert.assertTrue(((("Expected: maximum <" + maxExpected) + "> but was: <") + actual) + ">", actual < maxExpected);
    }
}

