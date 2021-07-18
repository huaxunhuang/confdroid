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


public class ListWithSeparatorsTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListWithSeparators> {
    private android.widget.listview.ListWithSeparators mActivity;

    private android.widget.ListView mListView;

    public ListWithSeparatorsTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListWithSeparators.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mActivity = getActivity();
        mListView = getActivity().getListView();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertNotNull(mActivity);
        assertNotNull(mListView);
        assertFalse(mListView.getAdapter().areAllItemsEnabled());
        assertFalse(mListView.getAdapter().isEnabled(0));
        assertFalse(mListView.getAdapter().isEnabled(2));
        assertEquals(1, mListView.getSelectedItemPosition());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGoingUpDoesnNotHitUnselectableItem() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        assertEquals("selected position should remain the same", 1, mListView.getSelectedItemPosition());
        assertEquals("seperator should be scrolled flush with top", mListView.getListPaddingTop(), mListView.getChildAt(0).getTop());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGoingDownSkipsOverUnselectable() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertEquals("should have skipped to next selectable ", 3, mListView.getSelectedItemPosition());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGoingUpSkippingOverUnselectable() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        assertEquals(1, mListView.getSelectedItemPosition());
    }
}

