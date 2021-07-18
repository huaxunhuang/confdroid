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
 * Tests that focus searching works on a vertical linear layout of buttons of
 * various widths and horizontal placements.
 */
// Suppress until bug http://b/issue?id=1416545 is fixed
@android.test.suitebuilder.annotation.Suppress
public class VerticalFocusSearchTest extends android.test.ActivityInstrumentationTestCase<android.widget.focus.VerticalFocusSearch> {
    private android.widget.LinearLayout mLayout;

    private android.widget.Button mTopWide;

    private android.widget.Button mMidSkinny1Left;

    private android.widget.Button mMidSkinny2Right;

    private android.widget.Button mBottomWide;

    private android.widget.focus.VerticalFocusSearchTest.FocusSearchAlg mFocusFinder;

    // helps test old and new impls when figuring out why something that used
    interface FocusSearchAlg {
        // to work doesn't anymore (or verifying that new works as well as old).
        android.view.View findNextFocus(android.view.ViewGroup root, android.view.View focused, int direction);
    }

    // calls new impl
    static class NewFocusSearchAlg implements android.widget.focus.VerticalFocusSearchTest.FocusSearchAlg {
        public android.view.View findNextFocus(android.view.ViewGroup root, android.view.View focused, int direction) {
            return android.view.FocusFinder.getInstance().findNextFocus(root, focused, direction);
        }
    }

    public VerticalFocusSearchTest() {
        super("com.android.frameworks.coretests", android.widget.focus.VerticalFocusSearch.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mLayout = getActivity().getLayout();
        mTopWide = getActivity().getTopWide();
        mMidSkinny1Left = getActivity().getMidSkinny1Left();
        mMidSkinny2Right = getActivity().getMidSkinny2Right();
        mBottomWide = getActivity().getBottomWide();
        mFocusFinder = new android.widget.focus.VerticalFocusSearchTest.NewFocusSearchAlg();
    }

    public void testPreconditions() {
        assertNotNull(mLayout);
        assertNotNull(mTopWide);
        assertNotNull(mMidSkinny1Left);
        assertNotNull(mMidSkinny2Right);
        assertNotNull(mBottomWide);
    }

    public void testSearchFromTopButton() {
        assertNull("going up from mTopWide.", mFocusFinder.findNextFocus(mLayout, mTopWide, android.view.View.FOCUS_UP));
        assertNull("going left from mTopWide.", mFocusFinder.findNextFocus(mLayout, mTopWide, android.view.View.FOCUS_LEFT));
        assertNull("going right from mTopWide.", mFocusFinder.findNextFocus(mLayout, mTopWide, android.view.View.FOCUS_RIGHT));
        assertEquals("going down from mTopWide.", mMidSkinny1Left, mFocusFinder.findNextFocus(mLayout, mTopWide, android.view.View.FOCUS_DOWN));
    }

    public void testSearchFromMidLeft() {
        assertNull("going left should have no next focus", mFocusFinder.findNextFocus(mLayout, mMidSkinny1Left, android.view.View.FOCUS_LEFT));
        assertEquals("going right from mMidSkinny1Left should go to mMidSkinny2Right", mMidSkinny2Right, mFocusFinder.findNextFocus(mLayout, mMidSkinny1Left, android.view.View.FOCUS_RIGHT));
        assertEquals("going up from mMidSkinny1Left should go to mTopWide", mTopWide, mFocusFinder.findNextFocus(mLayout, mMidSkinny1Left, android.view.View.FOCUS_UP));
        assertEquals("going down from mMidSkinny1Left should go to mMidSkinny2Right", mMidSkinny2Right, mFocusFinder.findNextFocus(mLayout, mMidSkinny1Left, android.view.View.FOCUS_DOWN));
    }

    public void testSearchFromMidRight() {
        assertEquals("going left from mMidSkinny2Right should go to mMidSkinny1Left", mMidSkinny1Left, mFocusFinder.findNextFocus(mLayout, mMidSkinny2Right, android.view.View.FOCUS_LEFT));
        assertNull("going right should have no next focus", mFocusFinder.findNextFocus(mLayout, mMidSkinny2Right, android.view.View.FOCUS_RIGHT));
        assertEquals("going up from mMidSkinny2Right should go to mMidSkinny1Left", mMidSkinny1Left, mFocusFinder.findNextFocus(mLayout, mMidSkinny2Right, android.view.View.FOCUS_UP));
        assertEquals("going down from mMidSkinny2Right should go to mBottomWide", mBottomWide, mFocusFinder.findNextFocus(mLayout, mMidSkinny2Right, android.view.View.FOCUS_DOWN));
    }

    public void testSearchFromFromBottom() {
        assertNull("going down from bottom button should have no next focus.", mFocusFinder.findNextFocus(mLayout, mBottomWide, android.view.View.FOCUS_DOWN));
        assertNull("going left from bottom button should have no next focus.", mFocusFinder.findNextFocus(mLayout, mBottomWide, android.view.View.FOCUS_LEFT));
        assertNull("going right from bottom button should have no next focus.", mFocusFinder.findNextFocus(mLayout, mBottomWide, android.view.View.FOCUS_RIGHT));
        assertEquals("going up from bottom button should go to mMidSkinny2Right.", mMidSkinny2Right, mFocusFinder.findNextFocus(mLayout, mBottomWide, android.view.View.FOCUS_UP));
    }
}

