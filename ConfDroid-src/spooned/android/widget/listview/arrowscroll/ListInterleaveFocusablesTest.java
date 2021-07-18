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


public class ListInterleaveFocusablesTest extends android.test.ActivityInstrumentationTestCase2<android.widget.listview.ListInterleaveFocusables> {
    private android.widget.ListView mListView;

    private android.util.ListUtil mListUtil;

    public ListInterleaveFocusablesTest() {
        super(android.widget.listview.ListInterleaveFocusables.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mListView = getActivity().getListView();
        mListUtil = new android.util.ListUtil(mListView, getInstrumentation());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertEquals(7, mListView.getChildCount());
        assertTrue(mListView.getChildAt(1).isFocusable());
        assertTrue(mListView.getChildAt(3).isFocusable());
        assertTrue(mListView.getChildAt(6).isFocusable());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGoingFromUnFocusableSelectedToFocusable() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertEquals("selected item position", 1, mListView.getSelectedItemPosition());
        assertSelectedViewFocus(true);
    }

    // go down from an item that isn't focusable, make sure it finds the focusable
    // below (instead of above).  this exposes a (now fixed) bug where the focus search
    // was not starting from the right spot
    @android.test.suitebuilder.annotation.MediumTest
    public void testGoingDownFromUnFocusableSelectedToFocusableWithOtherFocusableAbove() {
        mListUtil.setSelectedPosition(2);
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertEquals("selected item position", 3, mListView.getSelectedItemPosition());
        assertSelectedViewFocus(true);
    }

    // same, but going up
    @android.test.suitebuilder.annotation.MediumTest
    public void testGoingUpFromUnFocusableSelectedToFocusableWithOtherFocusableAbove() {
        mListUtil.setSelectedPosition(2);
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        assertEquals("selected item position", 1, mListView.getSelectedItemPosition());
        assertSelectedViewFocus(true);
    }

    /**
     * Go down from a focusable when there is a focusable below, but it is more than
     * one item away; make sure it won't give that item focus because it is too far away.
     */
    @android.test.suitebuilder.annotation.MediumTest
    public void testGoingDownFromFocusableToUnfocusableWhenFocusableIsBelow() {
        mListUtil.setSelectedPosition(3);
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertEquals("selected item position", 4, mListView.getSelectedItemPosition());
        assertSelectedViewFocus(false);
    }

    // same but going up
    @android.test.suitebuilder.annotation.MediumTest
    public void testGoingUpFromFocusableToUnfocusableWhenFocusableIsBelow() {
        mListUtil.setSelectedPosition(6);
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        assertEquals("selected item position", 5, mListView.getSelectedItemPosition());
        assertSelectedViewFocus(false);
    }

    public void assertSelectedViewFocus(boolean isFocused) {
        final android.view.View view = mListView.getSelectedView();
        assertEquals("selected view focused", isFocused, view.isFocused());
        assertEquals("selected position's isSelected should be the inverse " + "of it having focus", !isFocused, view.isSelected());
    }
}

