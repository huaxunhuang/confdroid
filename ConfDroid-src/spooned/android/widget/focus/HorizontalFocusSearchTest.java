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
package android.widget.focus;


/**
 * Tests that focus searching works on a horizontal linear layout of buttons of
 * various widths and vertical placements.
 */
// Suppress until bug http://b/issue?id=1416545 is fixed.
@android.test.suitebuilder.annotation.Suppress
public class HorizontalFocusSearchTest extends android.test.ActivityInstrumentationTestCase<android.widget.focus.HorizontalFocusSearch> {
    private android.widget.focus.VerticalFocusSearchTest.FocusSearchAlg mFocusFinder;

    private android.widget.LinearLayout mLayout;

    private android.widget.Button mLeftTall;

    private android.widget.Button mMidShort1Top;

    private android.widget.Button mMidShort2Bottom;

    private android.widget.Button mRightTall;

    public HorizontalFocusSearchTest() {
        super("com.android.frameworks.coretests", android.widget.focus.HorizontalFocusSearch.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mFocusFinder = new android.widget.focus.VerticalFocusSearchTest.NewFocusSearchAlg();
        mLayout = getActivity().getLayout();
        mLeftTall = getActivity().getLeftTall();
        mMidShort1Top = getActivity().getMidShort1Top();
        mMidShort2Bottom = getActivity().getMidShort2Bottom();
        mRightTall = getActivity().getRightTall();
    }

    public void testPreconditions() {
        assertNotNull(mLayout);
        assertNotNull(mLeftTall);
        assertNotNull(mMidShort1Top);
        assertNotNull(mMidShort2Bottom);
        assertNotNull(mRightTall);
    }

    public void testSearchFromLeftButton() {
        assertNull("going up from mLeftTall", mFocusFinder.findNextFocus(mLayout, mLeftTall, android.view.View.FOCUS_UP));
        assertNull("going down from mLeftTall", mFocusFinder.findNextFocus(mLayout, mLeftTall, android.view.View.FOCUS_DOWN));
        assertNull("going left from mLeftTall", mFocusFinder.findNextFocus(mLayout, mLeftTall, android.view.View.FOCUS_LEFT));
        assertEquals("going right from mLeftTall", mMidShort1Top, mFocusFinder.findNextFocus(mLayout, mLeftTall, android.view.View.FOCUS_RIGHT));
    }

    public void TODO_testSearchFromMiddleLeftButton() {
        assertNull("going up from mMidShort1Top", mFocusFinder.findNextFocus(mLayout, mMidShort1Top, android.view.View.FOCUS_UP));
        assertEquals("going down from mMidShort1Top", mMidShort2Bottom, mFocusFinder.findNextFocus(mLayout, mMidShort1Top, android.view.View.FOCUS_DOWN));
        assertEquals("going left from mMidShort1Top", mLeftTall, mFocusFinder.findNextFocus(mLayout, mMidShort1Top, android.view.View.FOCUS_LEFT));
        assertEquals("going right from mMidShort1Top", mMidShort2Bottom, mFocusFinder.findNextFocus(mLayout, mMidShort1Top, android.view.View.FOCUS_RIGHT));
    }

    public void TODO_testSearchFromMiddleRightButton() {
        assertEquals("going up from mMidShort2Bottom", mMidShort1Top, mFocusFinder.findNextFocus(mLayout, mMidShort2Bottom, android.view.View.FOCUS_UP));
        assertNull("going down from mMidShort2Bottom", mFocusFinder.findNextFocus(mLayout, mMidShort2Bottom, android.view.View.FOCUS_DOWN));
        assertEquals("going left from mMidShort2Bottom", mMidShort1Top, mFocusFinder.findNextFocus(mLayout, mMidShort2Bottom, android.view.View.FOCUS_LEFT));
        assertEquals("goind right from mMidShort2Bottom", mRightTall, mFocusFinder.findNextFocus(mLayout, mMidShort2Bottom, android.view.View.FOCUS_RIGHT));
    }

    public void testSearchFromRightButton() {
        assertNull("going up from mRightTall", mFocusFinder.findNextFocus(mLayout, mRightTall, android.view.View.FOCUS_UP));
        assertNull("going down from mRightTall", mFocusFinder.findNextFocus(mLayout, mRightTall, android.view.View.FOCUS_DOWN));
        assertEquals("going left from mRightTall", mMidShort2Bottom, mFocusFinder.findNextFocus(mLayout, mRightTall, android.view.View.FOCUS_LEFT));
        assertNull("going right from mRightTall", mFocusFinder.findNextFocus(mLayout, mRightTall, android.view.View.FOCUS_RIGHT));
    }
}

