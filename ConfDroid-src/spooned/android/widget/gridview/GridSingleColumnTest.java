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


public class GridSingleColumnTest extends android.test.ActivityInstrumentationTestCase<android.widget.gridview.GridSingleColumn> {
    private android.widget.gridview.GridSingleColumn mActivity;

    private android.widget.GridView mGridView;

    public GridSingleColumnTest() {
        super("com.android.frameworks.coretests", android.widget.gridview.GridSingleColumn.class);
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
}

