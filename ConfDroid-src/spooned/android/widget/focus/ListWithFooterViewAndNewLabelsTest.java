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
package android.widget.focus;


public class ListWithFooterViewAndNewLabelsTest extends android.test.ActivityInstrumentationTestCase<android.widget.focus.ListWithFooterViewAndNewLabels> {
    private android.widget.Button mButton;

    private android.widget.ListAdapter mAdapter;

    private android.widget.ListView mListView;

    public ListWithFooterViewAndNewLabelsTest() {
        super("com.android.frameworks.coretests", android.widget.focus.ListWithFooterViewAndNewLabels.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        android.widget.focus.ListWithFooterViewAndNewLabels a = getActivity();
        mButton = ((android.widget.Button) (a.findViewById(R.id.button)));
        mAdapter = a.getListAdapter();
        mListView = a.getListView();
    }

    // bug 900885
    public void FAILING_testPreconditions() {
        assertNotNull(mButton);
        assertNotNull(mAdapter);
        assertNotNull(mListView);
        assertTrue(mButton.hasFocus());
        assertEquals("expected list adapter to have 1 item", 1, mAdapter.getCount());
        assertEquals("expected list view to have 2 items (1 in adapter, plus " + "the footer view).", 2, mListView.getCount());
        // fails here!!!
        assertEquals("Expecting the selected index to be 0, the first non footer " + "view item.", 0, mListView.getSelectedItemPosition());
    }
}

