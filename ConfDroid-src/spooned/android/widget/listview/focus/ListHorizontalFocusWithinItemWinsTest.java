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
package android.widget.listview.focus;


public class ListHorizontalFocusWithinItemWinsTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListHorizontalFocusWithinItemWins> {
    private android.widget.ListView mListView;

    private android.widget.Button mTopLeftButton;

    private android.widget.Button mTopRightButton;

    private android.widget.Button mBottomMiddleButton;

    public ListHorizontalFocusWithinItemWinsTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListHorizontalFocusWithinItemWins.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mListView = getActivity().getListView();
        mTopLeftButton = getActivity().getTopLeftButton();
        mTopRightButton = getActivity().getTopRightButton();
        mBottomMiddleButton = getActivity().getBottomMiddleButton();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertEquals("list position", 0, mListView.getSelectedItemPosition());
        assertTrue("mTopLeftButton.isFocused()", mTopLeftButton.isFocused());
        assertEquals("global focus search to right from top left is bottom middle", mBottomMiddleButton, android.view.FocusFinder.getInstance().findNextFocus(mListView, mTopLeftButton, android.view.View.FOCUS_RIGHT));
        assertEquals("global focus search to left from top right is bottom middle", mBottomMiddleButton, android.view.FocusFinder.getInstance().findNextFocus(mListView, mTopRightButton, android.view.View.FOCUS_LEFT));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testOptionWithinItemTrumpsGlobal() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_RIGHT);
        assertEquals("list position", 0, mListView.getSelectedItemPosition());
        assertTrue("mTopRightButton.isFocused()", mTopRightButton.isFocused());
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_LEFT);
        assertEquals("list position", 0, mListView.getSelectedItemPosition());
        assertTrue("mTopLeftButton.isFocused()", mTopLeftButton.isFocused());
    }
}

