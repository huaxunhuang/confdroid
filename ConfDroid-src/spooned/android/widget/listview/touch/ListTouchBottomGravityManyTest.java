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
 * Touch tests for a list where all of the items do not fit on the screen, and the list
 * stacks from the bottom.
 */
public class ListTouchBottomGravityManyTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListBottomGravityMany> {
    private android.widget.listview.ListBottomGravityMany mActivity;

    private android.widget.ListView mListView;

    public ListTouchBottomGravityManyTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListBottomGravityMany.class);
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
        // Last item should be selected
        assertEquals(mListView.getAdapter().getCount() - 1, mListView.getSelectedItemPosition());
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testPullDown() {
        int originalCount = mListView.getChildCount();
        android.test.TouchUtils.scrollToTop(this, mListView);
        // Nothing should be selected
        assertEquals("Selection still available after touch", -1, mListView.getSelectedItemPosition());
        android.view.View firstChild = mListView.getChildAt(0);
        assertEquals("Item zero not the first child in the list", 0, firstChild.getId());
        assertEquals("Item zero not at the top of the list", mListView.getListPaddingTop(), firstChild.getTop());
        assertTrue(java.lang.String.format("Too many children created: %d expected no more than %d", mListView.getChildCount(), originalCount + 1), mListView.getChildCount() <= (originalCount + 1));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPushUp() {
        android.test.TouchUtils.scrollToBottom(this, mListView);
        // Nothing should be selected
        assertEquals("Selection still available after touch", -1, mListView.getSelectedItemPosition());
        android.view.View lastChild = mListView.getChildAt(mListView.getChildCount() - 1);
        assertEquals("List is not scrolled to the bottom", mListView.getAdapter().getCount() - 1, lastChild.getId());
        assertEquals("Last item is not touching the bottom edge", mListView.getHeight() - mListView.getListPaddingBottom(), lastChild.getBottom());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testNoScroll() {
        android.view.View firstChild = mListView.getChildAt(0);
        android.view.View lastChild = mListView.getChildAt(mListView.getChildCount() - 1);
        int lastTop = lastChild.getTop();
        android.test.TouchUtils.dragViewBy(this, firstChild, android.view.Gravity.BOTTOM | android.view.Gravity.CENTER_HORIZONTAL, 0, android.view.ViewConfiguration.getTouchSlop());
        android.view.View newLastChild = mListView.getChildAt(mListView.getChildCount() - 1);
        assertEquals("View scrolled too early", lastTop, newLastChild.getTop());
        assertEquals("Wrong view in last position", mListView.getAdapter().getCount() - 1, newLastChild.getId());
    }

    // TODO: needs to be adjusted to pass on non-HVGA displays
    // @LargeTest
    public void testShortScroll() {
        android.view.View firstChild = mListView.getChildAt(0);
        if (firstChild.getTop() < this.mListView.getListPaddingTop()) {
            firstChild = mListView.getChildAt(1);
        }
        android.view.View lastChild = mListView.getChildAt(mListView.getChildCount() - 1);
        int lastTop = lastChild.getTop();
        android.test.TouchUtils.dragViewBy(this, firstChild, android.view.Gravity.BOTTOM | android.view.Gravity.CENTER_HORIZONTAL, 0, (android.view.ViewConfiguration.getTouchSlop() + 1) + 10);
        android.view.View newLastChild = mListView.getChildAt(mListView.getChildCount() - 1);
        assertEquals("View scrolled to wrong position", lastTop, newLastChild.getTop() - 10);
        assertEquals("Wrong view in last position", mListView.getAdapter().getCount() - 1, newLastChild.getId());
    }

    // TODO: needs to be adjusted to pass on non-HVGA displays
    // @LargeTest
    public void testLongScroll() {
        android.view.View firstChild = mListView.getChildAt(0);
        if (firstChild.getTop() < mListView.getListPaddingTop()) {
            firstChild = mListView.getChildAt(1);
        }
        int firstTop = firstChild.getTop();
        int distance = android.test.TouchUtils.dragViewBy(this, firstChild, android.view.Gravity.BOTTOM | android.view.Gravity.CENTER_HORIZONTAL, 0, ((int) (mActivity.getWindowManager().getDefaultDisplay().getHeight() * 0.75F)));
        assertEquals("View scrolled to wrong position", firstTop + ((distance - android.view.ViewConfiguration.getTouchSlop()) - 1), firstChild.getTop());
    }
}

