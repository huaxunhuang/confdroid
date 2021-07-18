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
 * This test is made to check that getSelectedView() will return
 * null in touch mode.
 */
public class ListGetSelectedViewTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListGetSelectedView> {
    private android.widget.listview.ListGetSelectedView mActivity;

    private android.widget.ListView mListView;

    public ListGetSelectedViewTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListGetSelectedView.class);
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
        assertEquals(0, mListView.getSelectedItemPosition());
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testGetSelectedView() {
        android.view.View last = mListView.getChildAt(1);
        android.test.TouchUtils.clickView(this, last);
        assertNull(mListView.getSelectedItem());
        assertNull(mListView.getSelectedView());
        assertEquals(-1, mListView.getSelectedItemPosition());
    }
}

