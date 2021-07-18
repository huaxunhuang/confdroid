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
package android.widget.listview.touch;


/**
 * Touch tests for a list where all of the items do not fit on the screen.
 */
public class ListTouchManyTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListTopGravityMany> {
    private android.widget.listview.ListTopGravityMany mActivity;

    private android.widget.ListView mListView;

    public ListTouchManyTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListTopGravityMany.class);
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
        // First item should be selected
        assertEquals(0, mListView.getSelectedItemPosition());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPullDown() {
        android.test.TouchUtils.scrollToTop(this, mListView);
        // Nothing should be selected
        assertEquals("Selection still available after touch", -1, mListView.getSelectedItemPosition());
        android.view.View firstChild = mListView.getChildAt(0);
        assertEquals("Item zero not the first child in the list", 0, firstChild.getId());
        assertEquals("Item zero not at the top of the list", mListView.getListPaddingTop(), firstChild.getTop());
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testPushUp() {
        int originalCount = mListView.getChildCount();
        android.test.TouchUtils.scrollToBottom(this, mListView);
        // Nothing should be selected
        assertEquals("Selection still available after touch", -1, mListView.getSelectedItemPosition());
        android.view.View lastChild = mListView.getChildAt(mListView.getChildCount() - 1);
        assertEquals("List is not scrolled to the bottom", mListView.getAdapter().getCount() - 1, lastChild.getId());
        assertEquals("Last item is not touching the bottom edge", mListView.getHeight() - mListView.getListPaddingBottom(), lastChild.getBottom());
        assertTrue(java.lang.String.format("Too many children created: %d expected no more than %d", mListView.getChildCount(), originalCount + 1), mListView.getChildCount() <= (originalCount + 1));
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testPress() {
        int i;
        int count = mListView.getChildCount();
        mActivity.setClickedPosition(-1);
        mActivity.setLongClickedPosition(-1);
        for (i = 0; i < count; i++) {
            android.view.View child = mListView.getChildAt(i);
            if ((child.getTop() >= mListView.getListPaddingTop()) && (child.getBottom() <= (mListView.getHeight() - mListView.getListPaddingBottom()))) {
                android.test.TouchUtils.clickView(this, child);
                assertEquals("Incorrect view position reported being clicked", i, mActivity.getClickedPosition());
                assertEquals("View falsely reported being long clicked", -1, mActivity.getLongClickedPosition());
                try {
                    java.lang.Thread.sleep(((long) (android.view.ViewConfiguration.getLongPressTimeout() * 1.25F)));
                } catch (java.lang.InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testLongPress() {
        int i;
        int count = mListView.getChildCount();
        mActivity.enableLongPress();
        mActivity.setClickedPosition(-1);
        mActivity.setLongClickedPosition(-1);
        for (i = 0; i < count; i++) {
            android.view.View child = mListView.getChildAt(i);
            if ((child.getTop() >= mListView.getListPaddingTop()) && (child.getBottom() <= (mListView.getHeight() - mListView.getListPaddingBottom()))) {
                android.test.TouchUtils.longClickView(this, child);
                assertEquals("Incorrect view position reported being long clicked", i, mActivity.getLongClickedPosition());
                assertEquals("View falsely reported being clicked", -1, mActivity.getClickedPosition());
            }
        }
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testNoScroll() {
        android.view.View firstChild = mListView.getChildAt(0);
        android.view.View lastChild = mListView.getChildAt(mListView.getChildCount() - 1);
        int firstTop = firstChild.getTop();
        android.test.TouchUtils.dragViewBy(this, lastChild, android.view.Gravity.TOP | android.view.Gravity.LEFT, 0, -android.view.ViewConfiguration.getTouchSlop());
        android.view.View newFirstChild = mListView.getChildAt(0);
        assertEquals("View scrolled too early", firstTop, newFirstChild.getTop());
        assertEquals("Wrong view in first position", 0, newFirstChild.getId());
    }

    // TODO: needs to be adjusted to pass on non-HVGA displays
    // @LargeTest
    public void testShortScroll() {
        android.view.View firstChild = mListView.getChildAt(0);
        android.view.View lastChild = mListView.getChildAt(mListView.getChildCount() - 1);
        int firstTop = firstChild.getTop();
        android.test.TouchUtils.dragViewBy(this, lastChild, android.view.Gravity.TOP | android.view.Gravity.LEFT, 0, -((android.view.ViewConfiguration.getTouchSlop() + 1) + 10));
        android.view.View newFirstChild = mListView.getChildAt(0);
        assertEquals("View scrolled too early", firstTop, newFirstChild.getTop() + 10);
        assertEquals("Wrong view in first position", 0, newFirstChild.getId());
    }

    // TODO: needs to be adjusted to pass on non-HVGA displays
    // @LargeTest
    public void testLongScroll() {
        android.view.View lastChild = mListView.getChildAt(mListView.getChildCount() - 1);
        int lastTop = lastChild.getTop();
        int distance = android.test.TouchUtils.dragViewToY(this, lastChild, android.view.Gravity.TOP | android.view.Gravity.LEFT, mListView.getTop());
        assertEquals("View scrolled to wrong position", lastTop - ((distance - android.view.ViewConfiguration.getTouchSlop()) - 1), lastChild.getTop());
    }
}

