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
package android.widget.gridview.touch;


public class GridTouchVerticalSpacingStackFromBottomTest extends android.test.ActivityInstrumentationTestCase<android.widget.gridview.GridVerticalSpacingStackFromBottom> {
    private android.widget.gridview.GridVerticalSpacingStackFromBottom mActivity;

    private android.widget.GridView mGridView;

    private android.view.ViewConfiguration mViewConfig;

    public GridTouchVerticalSpacingStackFromBottomTest() {
        super("com.android.frameworks.coretests", android.widget.gridview.GridVerticalSpacingStackFromBottom.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mActivity = getActivity();
        mGridView = getActivity().getGridView();
        final android.content.Context context = mActivity.getApplicationContext();
        mViewConfig = android.view.ViewConfiguration.get(context);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertNotNull(mActivity);
        assertNotNull(mGridView);
        // Last item should be selected
        assertEquals(mGridView.getAdapter().getCount() - 1, mGridView.getSelectedItemPosition());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testNoScroll() {
        android.view.View firstChild = mGridView.getChildAt(0);
        android.view.View lastChild = mGridView.getChildAt(mGridView.getChildCount() - 1);
        int lastTop = lastChild.getTop();
        android.test.TouchUtils.dragViewBy(this, firstChild, android.view.Gravity.BOTTOM | android.view.Gravity.CENTER_HORIZONTAL, 0, android.view.ViewConfiguration.getTouchSlop());
        android.view.View newLastChild = mGridView.getChildAt(mGridView.getChildCount() - 1);
        assertEquals("View scrolled too early", lastTop, newLastChild.getTop());
        assertEquals("Wrong view in last position", mGridView.getAdapter().getCount() - 1, newLastChild.getId());
    }

    // TODO: needs to be adjusted to pass on non-HVGA displays
    // @LargeTest
    public void testShortScroll() {
        android.view.View firstChild = mGridView.getChildAt(0);
        if (firstChild.getTop() < this.mGridView.getListPaddingTop()) {
            firstChild = mGridView.getChildAt(1);
        }
        android.view.View lastChild = mGridView.getChildAt(mGridView.getChildCount() - 1);
        int lastTop = lastChild.getTop();
        android.test.TouchUtils.dragViewBy(this, firstChild, android.view.Gravity.BOTTOM | android.view.Gravity.CENTER_HORIZONTAL, 0, (mViewConfig.getScaledTouchSlop() + 1) + 10);
        android.view.View newLastChild = mGridView.getChildAt(mGridView.getChildCount() - 1);
        assertEquals("View scrolled to wrong position", lastTop, newLastChild.getTop() - 10);
        assertEquals("Wrong view in last position", mGridView.getAdapter().getCount() - 1, newLastChild.getId());
    }

    // TODO: needs to be adjusted to pass on non-HVGA displays
    // @LargeTest
    public void testLongScroll() {
        android.view.View firstChild = mGridView.getChildAt(0);
        if (firstChild.getTop() < mGridView.getListPaddingTop()) {
            firstChild = mGridView.getChildAt(1);
        }
        int firstTop = firstChild.getTop();
        int distance = android.test.TouchUtils.dragViewBy(this, firstChild, android.view.Gravity.BOTTOM | android.view.Gravity.CENTER_HORIZONTAL, 0, ((int) (mActivity.getWindowManager().getDefaultDisplay().getHeight() * 0.75F)));
        assertEquals("View scrolled to wrong position", firstTop + ((distance - mViewConfig.getScaledTouchSlop()) - 1), firstChild.getTop());
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testManyScrolls() {
        int originalCount = mGridView.getChildCount();
        android.view.View firstChild;
        int firstId = java.lang.Integer.MIN_VALUE;
        int firstTop = java.lang.Integer.MIN_VALUE;
        int prevId;
        int prevTop;
        do {
            prevId = firstId;
            prevTop = firstTop;
            android.test.TouchUtils.dragQuarterScreenDown(this);
            assertTrue(java.lang.String.format("Too many children created: %d expected no more than %d", mGridView.getChildCount(), originalCount + 4), mGridView.getChildCount() <= (originalCount + 4));
            firstChild = mGridView.getChildAt(0);
            firstId = firstChild.getId();
            firstTop = firstChild.getTop();
        } while ((prevId != firstId) || (prevTop != firstTop) );
        firstChild = mGridView.getChildAt(0);
        assertEquals("View scrolled to wrong position", 0, firstChild.getId());
        firstId = java.lang.Integer.MIN_VALUE;
        firstTop = java.lang.Integer.MIN_VALUE;
        do {
            prevId = firstId;
            prevTop = firstTop;
            android.test.TouchUtils.dragQuarterScreenUp(this);
            assertTrue(java.lang.String.format("Too many children created: %d expected no more than %d", mGridView.getChildCount(), originalCount + 4), mGridView.getChildCount() <= (originalCount + 4));
            firstChild = mGridView.getChildAt(0);
            firstId = firstChild.getId();
            firstTop = firstChild.getTop();
        } while ((prevId != firstId) || (prevTop != firstTop) );
        android.view.View lastChild = mGridView.getChildAt(mGridView.getChildCount() - 1);
        assertEquals("Grid is not scrolled to the bottom", mGridView.getAdapter().getCount() - 1, lastChild.getId());
    }
}

