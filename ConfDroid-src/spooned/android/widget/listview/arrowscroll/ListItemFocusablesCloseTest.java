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
package android.widget.listview.arrowscroll;


public class ListItemFocusablesCloseTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListItemFocusablesClose> {
    private android.widget.ListView mListView;

    private int mListTop;

    private int mListBottom;

    public ListItemFocusablesCloseTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListItemFocusablesClose.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mListView = getActivity().getListView();
        mListTop = mListView.getListPaddingTop();
        mListBottom = mListView.getHeight() - mListView.getListPaddingBottom();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertNotNull(mListView);
        assertTrue(mListView.getAdapter().areAllItemsEnabled());
        assertTrue(mListView.getItemsCanFocus());
        assertEquals(0, mListView.getSelectedItemPosition());
        final android.widget.LinearLayout first = ((android.widget.LinearLayout) (mListView.getSelectedView()));
        getInstrumentation().waitForIdleSync();
        assertEquals("first item should be at top of screen", mListView.getListPaddingTop(), first.getTop());
        assertTrue("first button of first list item should have focus", first.getChildAt(0).isFocused());
        assertTrue("item should be shorter than list for this test to make sense", first.getHeight() < mListView.getHeight());
        assertEquals("two items should be on screen", 2, mListView.getChildCount());
        assertTrue("first button of second item should be on screen", getActivity().getChildOfItem(1, 0).getBottom() < mListBottom);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testChangeFocusWithinItem() {
        final android.widget.LinearLayout first = ((android.widget.LinearLayout) (mListView.getSelectedView()));
        final int topOfFirstItemBefore = first.getTop();
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertTrue("focus should have moved to second button of first item", first.getChildAt(2).isFocused());
        assertEquals("selection should not have changed", 0, mListView.getSelectedItemPosition());
        assertEquals("list item should not have been shifted", topOfFirstItemBefore, first.getTop());
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        assertTrue("focus should have moved back to first button of first item", first.getChildAt(0).isFocused());
        assertEquals("list item should not have been shifted", topOfFirstItemBefore, first.getTop());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testMoveDownToButtonInDifferentSelection() {
        final android.widget.LinearLayout first = ((android.widget.LinearLayout) (mListView.getSelectedView()));
        final int topOfFirstItemBefore = first.getTop();
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertEquals("selection should have moved to second item", 1, mListView.getSelectedItemPosition());
        final android.widget.LinearLayout selectedItem = ((android.widget.LinearLayout) (mListView.getSelectedView()));
        assertTrue("first button of second item should have focus", selectedItem.getChildAt(0).isFocused());
        assertEquals("list item should not have been shifted", topOfFirstItemBefore, first.getTop());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testMoveUpToButtonInDifferentSelection() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertEquals(1, mListView.getSelectedItemPosition());
        assertTrue("first button of second item should have focus", getActivity().getChildOfItem(1, 0).hasFocus());
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        assertEquals("first list item should have selection", 0, mListView.getSelectedItemPosition());
        assertTrue("second button of first item should have focus", getActivity().getChildOfItem(0, 2).hasFocus());
    }
}

