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


public class ListOfShortTallShortTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListOfShortTallShort> {
    private android.widget.ListView mListView;

    public ListOfShortTallShortTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListOfShortTallShort.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mListView = getActivity().getListView();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertTrue("second item should be taller than screen", mListView.getChildAt(1).getHeight() > mListView.getHeight());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGoDownFromShortToTall() {
        int topBeforeMove = mListView.getChildAt(1).getTop();
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertEquals("selection should have moved to tall item below", 1, mListView.getSelectedItemPosition());
        assertEquals("should not have scrolled; top should be the same.", topBeforeMove, mListView.getSelectedView().getTop());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGoUpFromShortToTall() {
        int maxMoves = 8;
        while ((mListView.getSelectedItemPosition() != 2) && (maxMoves > 0)) {
            sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        } 
        assertEquals("couldn't get to 3rd item", 2, mListView.getSelectedItemPosition());
        assertEquals("should only be two items on screen", 2, mListView.getChildCount());
        assertEquals("selected item should be last item on screen", mListView.getChildAt(1), mListView.getSelectedView());
        final int bottomBeforeMove = mListView.getChildAt(0).getBottom();
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        assertEquals("should have moved selection to tall item above", 1, mListView.getSelectedItemPosition());
        assertEquals("should not have scrolled, top should be the same", bottomBeforeMove, mListView.getChildAt(0).getBottom());
    }
}

