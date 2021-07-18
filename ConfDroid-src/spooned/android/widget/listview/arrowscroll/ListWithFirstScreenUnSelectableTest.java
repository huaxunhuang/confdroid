/**
 * Copyright (C) 2008 Google Inc.
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


public class ListWithFirstScreenUnSelectableTest extends android.test.ActivityInstrumentationTestCase2<android.widget.listview.ListWithFirstScreenUnSelectable> {
    private android.widget.ListView mListView;

    public ListWithFirstScreenUnSelectableTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListWithFirstScreenUnSelectable.class);
    }

    protected void setUp() throws java.lang.Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        mListView = getActivity().getListView();
    }

    public void testPreconditions() {
        assertTrue(mListView.isInTouchMode());
        assertEquals(1, mListView.getChildCount());
        assertFalse(mListView.getAdapter().isEnabled(0));
        assertEquals(android.widget.AdapterView.INVALID_POSITION, mListView.getSelectedItemPosition());
    }

    public void testRessurectSelection() {
        sendKeys(android.view.KeyEvent.KEYCODE_SPACE);
        assertEquals(android.widget.AdapterView.INVALID_POSITION, mListView.getSelectedItemPosition());
    }

    public void testScrollUpDoesNothing() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        assertEquals(android.widget.AdapterView.INVALID_POSITION, mListView.getSelectedItemPosition());
        assertEquals(1, mListView.getChildCount());
        assertEquals(0, mListView.getFirstVisiblePosition());
    }

    public void testScrollDownPansNextItemOn() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertEquals(2, mListView.getChildCount());
    }
}

