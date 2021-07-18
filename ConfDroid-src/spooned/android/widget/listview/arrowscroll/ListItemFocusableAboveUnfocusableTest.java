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


public class ListItemFocusableAboveUnfocusableTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListItemFocusableAboveUnfocusable> {
    private android.widget.ListView mListView;

    public ListItemFocusableAboveUnfocusableTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListItemFocusableAboveUnfocusable.class);
    }

    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mListView = getActivity().getListView();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertEquals("selected position", 0, mListView.getSelectedItemPosition());
        assertTrue(mListView.getChildAt(0).isFocused());
        assertFalse(mListView.getChildAt(1).isFocusable());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testMovingToUnFocusableTakesFocusAway() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertFalse("focused item should have lost focus", mListView.getChildAt(0).isFocused());
        assertEquals("selected position", 1, mListView.getSelectedItemPosition());
    }
}

