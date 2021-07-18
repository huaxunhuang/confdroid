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
 * This is suppressed because {@link TextView#scrollBy} isn't working.
 */
@android.test.suitebuilder.annotation.Suppress
public class RequestRectangleVisibleWithInternalScrollTest extends android.test.ActivityInstrumentationTestCase<android.widget.scroll.RequestRectangleVisibleWithInternalScroll> {
    private android.widget.TextView mTextBlob;

    private android.widget.Button mScrollToBlob;

    private android.widget.ScrollView mScrollView;

    public RequestRectangleVisibleWithInternalScrollTest() {
        super("com.android.frameworks.coretests", android.widget.scroll.RequestRectangleVisibleWithInternalScroll.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mTextBlob = getActivity().getTextBlob();
        mScrollToBlob = getActivity().getScrollToBlob();
        mScrollView = ((android.widget.ScrollView) (getActivity().findViewById(R.id.scrollView)));
    }

    public void testPreconditions() {
        assertNotNull(mTextBlob);
        assertNotNull(mScrollToBlob);
        assertEquals(getActivity().getScrollYofBlob(), mTextBlob.getScrollY());
    }

    public void testMoveToChildWithScrollYBelow() {
        assertTrue(mScrollToBlob.hasFocus());
        android.test.ViewAsserts.assertOffScreenBelow(mScrollView, mTextBlob);
        // click
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_CENTER);
        getInstrumentation().waitForIdleSync();// wait for scrolling to finish

        // should be on screen, positioned at the bottom (with enough room for
        // fading edge)
        android.test.ViewAsserts.assertOnScreen(mScrollView, mTextBlob);
        android.test.ViewAsserts.assertHasScreenCoordinates(mScrollView, mTextBlob, 0, (mScrollView.getHeight() - mTextBlob.getHeight()) - mScrollView.getVerticalFadingEdgeLength());
    }
}

