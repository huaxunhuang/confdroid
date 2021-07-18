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
package android.widget.gridview;


public class GridSetSelectionBaseTest<T extends android.util.GridScenario> extends android.test.ActivityInstrumentationTestCase<T> {
    private T mActivity;

    private android.widget.GridView mGridView;

    protected GridSetSelectionBaseTest(java.lang.Class<T> klass) {
        super("com.android.frameworks.coretests", klass);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mActivity = android.widget.gridview.GridSetSelectionBaseTest.getActivity();
        mGridView = android.widget.gridview.GridSetSelectionBaseTest.getActivity().getGridView();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        android.widget.gridview.GridSetSelectionBaseTest.assertNotNull(mActivity);
        android.widget.gridview.GridSetSelectionBaseTest.assertNotNull(mGridView);
        // First item should be selected
        if (mGridView.isStackFromBottom()) {
            android.widget.gridview.GridSetSelectionBaseTest.assertEquals(mGridView.getAdapter().getCount() - 1, mGridView.getSelectedItemPosition());
        } else {
            android.widget.gridview.GridSetSelectionBaseTest.assertEquals(0, mGridView.getSelectedItemPosition());
        }
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetSelectionToTheEnd() {
        final int target = mGridView.getAdapter().getCount() - 1;
        mActivity.runOnUiThread(new java.lang.Runnable() {
            public void run() {
                mGridView.setSelection(target);
            }
        });
        android.widget.gridview.GridSetSelectionBaseTest.getInstrumentation().waitForIdleSync();
        android.widget.gridview.GridSetSelectionBaseTest.assertEquals(mGridView.getSelectedItemPosition(), target);
        android.widget.gridview.GridSetSelectionBaseTest.assertNotNull(mGridView.getSelectedView());
        android.test.ViewAsserts.assertOnScreen(mGridView, mGridView.getSelectedView());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetSelectionToMiddle() {
        final int target = mGridView.getAdapter().getCount() / 2;
        mActivity.runOnUiThread(new java.lang.Runnable() {
            public void run() {
                mGridView.setSelection(target);
            }
        });
        android.widget.gridview.GridSetSelectionBaseTest.getInstrumentation().waitForIdleSync();
        android.widget.gridview.GridSetSelectionBaseTest.assertEquals(mGridView.getSelectedItemPosition(), target);
        android.widget.gridview.GridSetSelectionBaseTest.assertNotNull(mGridView.getSelectedView());
        android.test.ViewAsserts.assertOnScreen(mGridView, mGridView.getSelectedView());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetSelectionToTheTop() {
        mActivity.runOnUiThread(new java.lang.Runnable() {
            public void run() {
                mGridView.setSelection(0);
            }
        });
        android.widget.gridview.GridSetSelectionBaseTest.getInstrumentation().waitForIdleSync();
        android.widget.gridview.GridSetSelectionBaseTest.assertEquals(mGridView.getSelectedItemPosition(), 0);
        android.widget.gridview.GridSetSelectionBaseTest.assertNotNull(mGridView.getSelectedView());
        android.test.ViewAsserts.assertOnScreen(mGridView, mGridView.getSelectedView());
    }
}

