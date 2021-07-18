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


public class ListLastItemPartiallyVisibleTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListLastItemPartiallyVisible> {
    private android.widget.ListView mListView;

    private int mListBottom;

    public ListLastItemPartiallyVisibleTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListLastItemPartiallyVisible.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mListView = getActivity().getListView();
        mListBottom = mListView.getHeight() - mListView.getPaddingBottom();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertEquals("number of elements visible should be the same as number of items " + "in adapter", mListView.getCount(), mListView.getChildCount());
        final android.view.View lastChild = mListView.getChildAt(mListView.getChildCount() - 1);
        assertTrue("last item should be partially off screen", lastChild.getBottom() > mListBottom);
        assertEquals("selected position", 0, mListView.getSelectedItemPosition());
    }

    // reproduce bug 998094
    @android.test.suitebuilder.annotation.MediumTest
    public void testMovingDownToFullyVisibleNoScroll() {
        final android.view.View firstChild = mListView.getChildAt(0);
        final int firstBottom = firstChild.getBottom();
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertEquals("shouldn't have scrolled: bottom of first child changed.", firstBottom, firstChild.getBottom());
    }

    // reproduce bug 998094
    @android.test.suitebuilder.annotation.MediumTest
    public void testMovingUpToFullyVisibleNoScroll() {
        int numMovesToLast = mListView.getCount() - 1;
        for (int i = 0; i < numMovesToLast; i++) {
            sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        }
        assertEquals("should have moved to last position", mListView.getChildCount() - 1, mListView.getSelectedItemPosition());
        final android.view.View lastChild = mListView.getSelectedView();
        final int lastTop = lastChild.getTop();
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        assertEquals("shouldn't have scrolled: top of last child changed.", lastTop, lastChild.getTop());
    }
}

