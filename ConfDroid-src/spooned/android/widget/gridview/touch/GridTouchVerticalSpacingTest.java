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


public class GridTouchVerticalSpacingTest extends android.test.ActivityInstrumentationTestCase<android.widget.gridview.GridVerticalSpacing> {
    private android.widget.gridview.GridVerticalSpacing mActivity;

    private android.widget.GridView mGridView;

    public GridTouchVerticalSpacingTest() {
        super("com.android.frameworks.coretests", android.widget.gridview.GridVerticalSpacing.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mActivity = getActivity();
        mGridView = getActivity().getGridView();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertNotNull(mActivity);
        assertNotNull(mGridView);
        assertEquals(0, mGridView.getSelectedItemPosition());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testNoScroll() {
        android.view.View firstChild = mGridView.getChildAt(0);
        android.view.View lastChild = mGridView.getChildAt(mGridView.getChildCount() - 1);
        int firstTop = firstChild.getTop();
        android.test.TouchUtils.dragViewBy(this, lastChild, android.view.Gravity.TOP | android.view.Gravity.LEFT, 0, -android.view.ViewConfiguration.getTouchSlop());
        android.view.View newFirstChild = mGridView.getChildAt(0);
        assertEquals("View scrolled too early", firstTop, newFirstChild.getTop());
        assertEquals("Wrong view in first position", 0, newFirstChild.getId());
    }

    // TODO: needs to be adjusted to pass on non-HVGA displays
    // @LargeTest
    public void testShortScroll() {
        android.view.View firstChild = mGridView.getChildAt(0);
        android.view.View lastChild = mGridView.getChildAt(mGridView.getChildCount() - 1);
        int firstTop = firstChild.getTop();
        android.test.TouchUtils.dragViewBy(this, lastChild, android.view.Gravity.TOP | android.view.Gravity.LEFT, 0, -((android.view.ViewConfiguration.getTouchSlop() + 1) + 10));
        android.view.View newFirstChild = mGridView.getChildAt(0);
        assertEquals("View scrolled to wrong position", firstTop, newFirstChild.getTop() + 10);
        assertEquals("Wrong view in first position", 0, newFirstChild.getId());
    }

    // TODO: needs to be adjusted to pass on non-HVGA displays
    // @LargeTest
    public void testLongScroll() {
        android.view.View lastChild = mGridView.getChildAt(mGridView.getChildCount() - 1);
        int lastTop = lastChild.getTop();
        int distance = android.test.TouchUtils.dragViewToY(this, lastChild, android.view.Gravity.TOP | android.view.Gravity.LEFT, mGridView.getTop());
        assertEquals("View scrolled to wrong position", lastTop - ((distance - android.view.ViewConfiguration.getTouchSlop()) - 1), lastChild.getTop());
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
            android.test.TouchUtils.dragQuarterScreenUp(this);
            assertTrue(java.lang.String.format("Too many children created: %d expected no more than %d", mGridView.getChildCount(), originalCount + 4), mGridView.getChildCount() <= (originalCount + 4));
            firstChild = mGridView.getChildAt(0);
            firstId = firstChild.getId();
            firstTop = firstChild.getTop();
        } while ((prevId != firstId) || (prevTop != firstTop) );
        android.view.View lastChild = mGridView.getChildAt(mGridView.getChildCount() - 1);
        assertEquals("Grid is not scrolled to the bottom", mGridView.getAdapter().getCount() - 1, lastChild.getId());
        firstId = java.lang.Integer.MIN_VALUE;
        firstTop = java.lang.Integer.MIN_VALUE;
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
        assertEquals("Grid is not scrolled to the top", 0, firstChild.getId());
    }
}

