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


public class AdjacentListsWithAdjacentISVsInsideTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.AdjacentListsWithAdjacentISVsInside> {
    private android.widget.ListView mLeftListView;

    private android.util.InternalSelectionView mLeftIsv;

    private android.util.InternalSelectionView mLeftMiddleIsv;

    private android.widget.ListView mRightListView;

    private android.util.InternalSelectionView mRightMiddleIsv;

    private android.util.InternalSelectionView mRightIsv;

    public AdjacentListsWithAdjacentISVsInsideTest() {
        super("com.android.frameworks.coretests", android.widget.listview.AdjacentListsWithAdjacentISVsInside.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        final android.widget.listview.AdjacentListsWithAdjacentISVsInside a = getActivity();
        mLeftListView = a.getLeftListView();
        mLeftIsv = a.getLeftIsv();
        mLeftMiddleIsv = a.getLeftMiddleIsv();
        mRightListView = a.getRightListView();
        mRightMiddleIsv = a.getRightMiddleIsv();
        mRightIsv = a.getRightIsv();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertTrue(mLeftListView.hasFocus());
        assertTrue(mLeftIsv.isFocused());
        assertEquals(0, mLeftIsv.getSelectedRow());
    }

    /**
     * rockinist test name to date!
     */
    @android.test.suitebuilder.annotation.MediumTest
    public void testFocusedRectAndFocusHintWorkWithinListItemHorizontal() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertEquals(1, mLeftIsv.getSelectedRow());
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_RIGHT);
        assertTrue(mLeftListView.hasFocus());
        assertTrue(mLeftMiddleIsv.isFocused());
        assertEquals("mLeftMiddleIsv.getSelectedRow()", 1, mLeftMiddleIsv.getSelectedRow());
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_LEFT);
        assertTrue(mLeftIsv.isFocused());
        assertEquals("mLeftIsv.getSelectedRow()", 1, mLeftIsv.getSelectedRow());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testFocusTransfersOutsideOfListWhenNoCandidateInsideHorizontal() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN, android.view.KeyEvent.KEYCODE_DPAD_DOWN, android.view.KeyEvent.KEYCODE_DPAD_RIGHT);
        assertTrue(mLeftListView.hasFocus());
        assertTrue(mLeftMiddleIsv.isFocused());
        assertEquals(2, mLeftMiddleIsv.getSelectedRow());
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_RIGHT);
        assertTrue("mRightListView.hasFocus()", mRightListView.hasFocus());
        assertTrue("mRightMiddleIsv.isFocused()", mRightMiddleIsv.isFocused());
        assertEquals("mRightMiddleIsv.getSelectedRow()", 2, mRightMiddleIsv.getSelectedRow());
    }
}

