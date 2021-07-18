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
package android.widget.scroll;


/**
 * {@link RequestRectangleVisible} is set up to exercise the cases of moving a
 * rectangle that is either off screen or not entirely on the screen onto the screen.
 */
public class RequestRectangleVisibleTest extends android.test.ActivityInstrumentationTestCase<android.widget.scroll.RequestRectangleVisible> {
    private android.widget.ScrollView mScrollView;

    private android.widget.Button mClickToScrollFromAbove;

    private android.widget.Button mClickToScrollToUpperBlob;

    private android.widget.TextView mTopBlob;

    private android.view.View mChildToScrollTo;

    private android.widget.TextView mBottomBlob;

    private android.widget.Button mClickToScrollToBlobLowerBlob;

    private android.widget.Button mClickToScrollFromBelow;

    public RequestRectangleVisibleTest() {
        super("com.android.frameworks.coretests", android.widget.scroll.RequestRectangleVisible.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        android.widget.scroll.RequestRectangleVisible a = getActivity();
        mScrollView = ((android.widget.ScrollView) (a.findViewById(R.id.scrollView)));
        mClickToScrollFromAbove = ((android.widget.Button) (a.findViewById(R.id.scrollToRectFromTop)));
        mClickToScrollToUpperBlob = ((android.widget.Button) (a.findViewById(R.id.scrollToRectFromTop2)));
        mTopBlob = ((android.widget.TextView) (a.findViewById(R.id.topBlob)));
        mChildToScrollTo = a.findViewById(R.id.childToMakeVisible);
        mBottomBlob = ((android.widget.TextView) (a.findViewById(R.id.bottomBlob)));
        mClickToScrollToBlobLowerBlob = ((android.widget.Button) (a.findViewById(R.id.scrollToRectFromBottom2)));
        mClickToScrollFromBelow = ((android.widget.Button) (a.findViewById(R.id.scrollToRectFromBottom)));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertNotNull(mScrollView);
        assertNotNull(mClickToScrollFromAbove);
        assertNotNull(mClickToScrollToUpperBlob);
        assertNotNull(mTopBlob);
        assertNotNull(mChildToScrollTo);
        assertNotNull(mBottomBlob);
        assertNotNull(mClickToScrollToBlobLowerBlob);
        assertNotNull(mClickToScrollFromBelow);
        assertTrue("top blob needs to be taller than the screen for many of the " + "tests below to work.", mTopBlob.getHeight() > mScrollView.getHeight());
        assertTrue("bottom blob needs to be taller than the screen for many of the " + "tests below to work.", mBottomBlob.getHeight() > mScrollView.getHeight());
        assertTrue("top blob needs to be lower than the fading edge region", mTopBlob.getTop() > mScrollView.getVerticalFadingEdgeLength());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testScrollToOffScreenRectangleFromTop() {
        // view is off screen
        assertTrue(mClickToScrollFromAbove.hasFocus());
        android.test.ViewAsserts.assertOffScreenBelow(mScrollView, mChildToScrollTo);
        // click
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_CENTER);
        getInstrumentation().waitForIdleSync();// wait for scrolling to finish

        // should be on screen, positioned at the bottom (with room for
        // fading edge)
        android.test.ViewAsserts.assertOnScreen(mScrollView, mChildToScrollTo);
        android.test.ViewAsserts.assertHasScreenCoordinates(mScrollView, mChildToScrollTo, 0, (mScrollView.getHeight() - mChildToScrollTo.getHeight()) - mScrollView.getVerticalFadingEdgeLength());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testScrollToPartiallyOffScreenRectFromTop() {
        pressDownUntilViewInFocus(mClickToScrollToUpperBlob, 4);
        // make sure the blob is indeed partially on screen below
        android.widget.scroll.RequestRectangleVisibleTest.assertOnBottomEdgeOfScreen(mScrollView, mTopBlob);
        // click
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_CENTER);
        getInstrumentation().waitForIdleSync();// wait for scrolling to finish

        // blob should have moved so top of it is at top of screen (with
        // room for the vertical fading edge
        android.test.ViewAsserts.assertHasScreenCoordinates(mScrollView, mTopBlob, 0, mScrollView.getVerticalFadingEdgeLength());
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testScrollToOffScreenRectangleFromBottom() {
        // go to bottom button
        pressDownUntilViewInFocus(mClickToScrollFromBelow, 10);
        // view is off screen above
        assertTrue(mClickToScrollFromBelow.hasFocus());
        android.test.ViewAsserts.assertOffScreenAbove(mScrollView, mChildToScrollTo);
        // click
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_CENTER);
        getInstrumentation().waitForIdleSync();// wait for scrolling to finish

        // on screen, positioned at top (with room for fading edge)
        android.test.ViewAsserts.assertOnScreen(mScrollView, mChildToScrollTo);
        android.test.ViewAsserts.assertHasScreenCoordinates(mScrollView, mChildToScrollTo, 0, mScrollView.getVerticalFadingEdgeLength());
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testScrollToPartiallyOffScreenRectFromBottom() {
        pressDownUntilViewInFocus(mClickToScrollToBlobLowerBlob, 10);
        // make sure the blob is indeed partially on screen above
        android.widget.scroll.RequestRectangleVisibleTest.assertOnTopEdgeOfScreen(mScrollView, mBottomBlob);
        // click
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_CENTER);
        getInstrumentation().waitForIdleSync();// wait for scrolling to finish

        // blob should have moved so bottom of it is at bottom of screen
        // with room for vertical fading edge
        android.test.ViewAsserts.assertHasScreenCoordinates(mScrollView, mBottomBlob, 0, (mScrollView.getHeight() - mBottomBlob.getHeight()) - mScrollView.getVerticalFadingEdgeLength());
    }

    /**
     * Press the down key until a particular view is in focus
     *
     * @param view
     * 		The view to get in focus.
     * @param maxKeyPress
     * 		The maximum times to press down before failing.
     */
    private void pressDownUntilViewInFocus(android.view.View view, int maxKeyPress) {
        int count = 0;
        while (!view.hasFocus()) {
            sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
            getInstrumentation().waitForIdleSync();
            // just in case...
            if ((++count) > maxKeyPress) {
                fail(("couldn't move down to bottom button within " + maxKeyPress) + " key presses.");
            }
        } 
    }

    /**
     * Assert that view overlaps the bottom edge of the screen
     *
     * @param origin
     * 		The root view of the screen.
     * @param view
     * 		The view
     */
    public static void assertOnBottomEdgeOfScreen(android.view.View origin, android.view.View view) {
        int[] xy = new int[2];
        view.getLocationOnScreen(xy);
        int[] xyRoot = new int[2];
        origin.getLocationOnScreen(xyRoot);
        int bottom = xy[1] + view.getHeight();
        int bottomOfRoot = xyRoot[1] + origin.getHeight();
        assertTrue(bottom > bottomOfRoot);
        assertTrue(xy[1] < bottomOfRoot);
        assertTrue(bottom > bottomOfRoot);
    }

    /**
     * Assert that view overlaps the bottom edge of the screen
     *
     * @param origin
     * 		The root view of the screen.
     * @param view
     * 		The view
     */
    public static void assertOnTopEdgeOfScreen(android.view.View origin, android.view.View view) {
        int[] xy = new int[2];
        view.getLocationOnScreen(xy);
        int[] xyRoot = new int[2];
        origin.getLocationOnScreen(xyRoot);
        int bottom = xy[1] + view.getHeight();
        int bottomOfRoot = xyRoot[1] + origin.getHeight();
        assertTrue(bottom < bottomOfRoot);
        assertTrue(bottom > xyRoot[1]);
        assertTrue(xy[1] < xyRoot[1]);
    }
}

