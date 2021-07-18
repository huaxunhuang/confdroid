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


public class ListWithEditTextHeaderTest extends android.test.ActivityInstrumentationTestCase2<android.widget.listview.ListWithEditTextHeader> {
    private android.widget.ListView mListView;

    public ListWithEditTextHeaderTest() {
        super(android.widget.listview.ListWithEditTextHeader.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mListView = getActivity().getListView();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertTrue("listview.getItemsCanFocus()", mListView.getItemsCanFocus());
        assertFalse("out of touch-mode", mListView.isInTouchMode());
        assertEquals("header view count", 1, mListView.getHeaderViewsCount());
        assertTrue("header does not have focus", mListView.getChildAt(0).isFocused());
    }

    @android.test.FlakyTest(tolerance = 2)
    @android.test.suitebuilder.annotation.LargeTest
    public void testClickingHeaderKeepsFocus() {
        android.test.TouchUtils.clickView(this, mListView.getChildAt(0));
        assertTrue("header does not have focus", mListView.getChildAt(0).isFocused());
        assertEquals("something is selected", android.widget.AbsListView.INVALID_POSITION, mListView.getSelectedItemPosition());
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testClickingHeaderWhenOtherItemHasFocusGivesHeaderFocus() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertEquals("selected position", 1, mListView.getSelectedItemPosition());
        android.test.TouchUtils.clickView(this, mListView.getChildAt(0));
        assertTrue("header does not have focus", mListView.getChildAt(0).isFocused());
        assertEquals("something is selected", android.widget.AbsListView.INVALID_POSITION, mListView.getSelectedItemPosition());
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testScrollingDoesNotDetachHeaderViewFromWindow() {
        android.view.View header = mListView.getChildAt(0);
        assertNotNull("header is not attached to a window (?!)", header.getWindowToken());
        // Scroll header off the screen and back onto the screen
        int numItemsOnScreen = mListView.getChildCount();
        for (int i = 0; i < numItemsOnScreen; i++) {
            sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        }
        for (int i = 0; i < numItemsOnScreen; i++) {
            sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        }
        // Make sure the header was not accidentally left detached from its window
        assertNotNull("header has lost its window", header.getWindowToken());
    }
}

