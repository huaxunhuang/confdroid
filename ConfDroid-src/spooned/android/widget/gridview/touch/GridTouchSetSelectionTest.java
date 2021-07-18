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


/**
 * Tests setting the selection in touch mode
 */
public class GridTouchSetSelectionTest extends android.test.ActivityInstrumentationTestCase<android.widget.gridview.GridSimple> {
    private android.widget.gridview.GridSimple mActivity;

    private android.widget.GridView mGridView;

    public GridTouchSetSelectionTest() {
        super("com.android.frameworks.coretests", android.widget.gridview.GridSimple.class);
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
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testSetSelection() {
        android.test.TouchUtils.dragQuarterScreenDown(this);
        android.test.TouchUtils.dragQuarterScreenUp(this);
        // Nothing should be selected
        assertEquals("Selection still available after touch", -1, mGridView.getSelectedItemPosition());
        final int targetPosition = mGridView.getAdapter().getCount() / 2;
        mActivity.runOnUiThread(new java.lang.Runnable() {
            public void run() {
                mGridView.setSelection(targetPosition);
            }
        });
        getInstrumentation().waitForIdleSync();
        boolean found = false;
        int childCount = mGridView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            android.view.View child = mGridView.getChildAt(i);
            if (child.getId() == targetPosition) {
                found = true;
                break;
            }
        }
        assertTrue("Selected item not visible in list", found);
    }
}

