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
 * Touch tests for a list where all of the items fit on the screen.
 */
public class ListTouchTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListTopGravity> {
    private android.widget.listview.ListTopGravity mActivity;

    private android.widget.ListView mListView;

    public ListTouchTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListTopGravity.class);
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
        android.view.View firstChild = mListView.getChildAt(0);
        android.test.TouchUtils.dragViewToBottom(this, firstChild);
        // Nothing should be selected
        assertEquals("Selection still available after touch", -1, mListView.getSelectedItemPosition());
        firstChild = mListView.getChildAt(0);
        assertEquals("Item zero not the first child in the list", 0, firstChild.getId());
        assertEquals("Item zero not at the top of the list", mListView.getListPaddingTop(), firstChild.getTop());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPushUp() {
        android.view.View lastChild = mListView.getChildAt(mListView.getChildCount() - 1);
        android.test.TouchUtils.dragViewToTop(this, lastChild);
        // Nothing should be selected
        assertEquals("Selection still available after touch", -1, mListView.getSelectedItemPosition());
        android.view.View firstChild = mListView.getChildAt(0);
        assertEquals("Item zero not the first child in the list", 0, firstChild.getId());
        assertEquals("Item zero not at the top of the list", mListView.getListPaddingTop(), firstChild.getTop());
    }
}

