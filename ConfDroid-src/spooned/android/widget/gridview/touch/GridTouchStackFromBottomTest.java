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


public class GridTouchStackFromBottomTest extends android.test.ActivityInstrumentationTestCase<android.widget.gridview.GridStackFromBottom> {
    private android.widget.gridview.GridStackFromBottom mActivity;

    private android.widget.GridView mGridView;

    public GridTouchStackFromBottomTest() {
        super("com.android.frameworks.coretests", android.widget.gridview.GridStackFromBottom.class);
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
        // First item should be selected
        assertEquals(mGridView.getAdapter().getCount() - 1, mGridView.getSelectedItemPosition());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPushUp() {
        android.test.TouchUtils.scrollToBottom(this, mGridView);
        // Nothing should be selected
        assertEquals("Selection still available after touch", -1, mGridView.getSelectedItemPosition());
        android.view.View lastChild = mGridView.getChildAt(mGridView.getChildCount() - 1);
        assertEquals("Last item not the last child in the grid", mGridView.getAdapter().getCount() - 1, lastChild.getId());
        assertEquals("Last item not at the bottom of the grid", mGridView.getHeight() - mGridView.getListPaddingBottom(), lastChild.getBottom());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPullDown() {
        android.test.TouchUtils.scrollToTop(this, mGridView);
        // Nothing should be selected
        assertEquals("Selection still available after touch", -1, mGridView.getSelectedItemPosition());
        android.view.View lastChild = mGridView.getChildAt(mGridView.getChildCount() - 1);
        assertEquals("Last item not the last child in the grid", mGridView.getAdapter().getCount() - 1, lastChild.getId());
        assertEquals("Last item not at the bottom of the grid", mGridView.getHeight() - mGridView.getListPaddingBottom(), lastChild.getBottom());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPushUpFast() {
        android.test.TouchUtils.dragViewToTop(this, mGridView.getChildAt(mGridView.getChildCount() - 1), 2);
        // Nothing should be selected
        assertEquals("Selection still available after touch", -1, mGridView.getSelectedItemPosition());
        android.view.View lastChild = mGridView.getChildAt(mGridView.getChildCount() - 1);
        assertEquals("Last item not the last child in the grid", mGridView.getAdapter().getCount() - 1, lastChild.getId());
        assertEquals("Last item not at the bottom of the grid", mGridView.getHeight() - mGridView.getListPaddingBottom(), lastChild.getBottom());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPullDownFast() {
        android.test.TouchUtils.dragViewToBottom(this, mGridView.getChildAt(0), 2);
        // Nothing should be selected
        assertEquals("Selection still available after touch", -1, mGridView.getSelectedItemPosition());
        android.view.View lastChild = mGridView.getChildAt(mGridView.getChildCount() - 1);
        assertEquals("Last item not the last child in the grid", mGridView.getAdapter().getCount() - 1, lastChild.getId());
        assertEquals("Last item not at the bottom of the grid", mGridView.getHeight() - mGridView.getListPaddingBottom(), lastChild.getBottom());
    }
}

