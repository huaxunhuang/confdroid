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
package android.widget.listview;


public class ListFocusableTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListTopGravity> {
    private android.widget.listview.ListTopGravity mActivity;

    private android.widget.ListView mListView;

    public ListFocusableTest() {
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
    public void testAdapterFull() {
        setFullAdapter();
        assertTrue(mListView.isFocusable());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testAdapterEmpty() {
        setEmptyAdapter();
        assertFalse(mListView.isFocusable());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testAdapterNull() {
        setNullAdapter();
        assertFalse(mListView.isFocusable());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testAdapterFullSetFocusable() {
        assertTrue(mListView.isFocusable());
        setFocusable();
        assertTrue(mListView.isFocusable());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testAdapterFullSetNonFocusable() {
        assertTrue(mListView.isFocusable());
        setNonFocusable();
        assertFalse(mListView.isFocusable());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testAdapterEmptySetFocusable() {
        setEmptyAdapter();
        assertFalse(mListView.isFocusable());
        setFocusable();
        assertFalse(mListView.isFocusable());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testAdapterEmptySetNonFocusable() {
        setEmptyAdapter();
        assertFalse(mListView.isFocusable());
        setNonFocusable();
        assertFalse(mListView.isFocusable());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testAdapterNullSetFocusable() {
        setNullAdapter();
        assertFalse(mListView.isFocusable());
        setFocusable();
        assertFalse(mListView.isFocusable());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testAdapterNullSetNonFocusable() {
        setNullAdapter();
        assertFalse(mListView.isFocusable());
        setNonFocusable();
        assertFalse(mListView.isFocusable());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testFocusableSetAdapterFull() {
        assertTrue(mListView.isFocusable());
        setFullAdapter();
        assertTrue(mListView.isFocusable());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testNonFocusableSetAdapterFull() {
        assertTrue(mListView.isFocusable());
        setNonFocusable();
        assertFalse(mListView.isFocusable());
        setFullAdapter();
        assertFalse(mListView.isFocusable());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testFocusableSetAdapterEmpty() {
        assertTrue(mListView.isFocusable());
        setEmptyAdapter();
        assertFalse(mListView.isFocusable());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testNonFocusableSetAdapterEmpty() {
        assertTrue(mListView.isFocusable());
        setNonFocusable();
        assertFalse(mListView.isFocusable());
        setEmptyAdapter();
        assertFalse(mListView.isFocusable());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testFocusableSetAdapterNull() {
        assertTrue(mListView.isFocusable());
        setNullAdapter();
        assertFalse(mListView.isFocusable());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testNonFocusableSetAdapterNull() {
        assertTrue(mListView.isFocusable());
        setNonFocusable();
        assertFalse(mListView.isFocusable());
        setNullAdapter();
        assertFalse(mListView.isFocusable());
    }

    private android.widget.ListAdapter createFullAdapter() {
        return new android.widget.ArrayAdapter<java.lang.String>(mActivity, android.R.layout.simple_list_item_1, new java.lang.String[]{ "Android", "Robot" });
    }

    private android.widget.ListAdapter createEmptyAdapter() {
        return new android.widget.ArrayAdapter<java.lang.String>(mActivity, android.R.layout.simple_list_item_1, new java.lang.String[]{  });
    }

    private void setFullAdapter() {
        mActivity.runOnUiThread(new java.lang.Runnable() {
            public void run() {
                mListView.setAdapter(createFullAdapter());
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    private void setEmptyAdapter() {
        mActivity.runOnUiThread(new java.lang.Runnable() {
            public void run() {
                mListView.setAdapter(createEmptyAdapter());
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    private void setNullAdapter() {
        mActivity.runOnUiThread(new java.lang.Runnable() {
            public void run() {
                mListView.setAdapter(null);
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    private void setFocusable() {
        mActivity.runOnUiThread(new java.lang.Runnable() {
            public void run() {
                mListView.setFocusable(true);
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    private void setNonFocusable() {
        mActivity.runOnUiThread(new java.lang.Runnable() {
            public void run() {
                mListView.setFocusable(false);
            }
        });
        getInstrumentation().waitForIdleSync();
    }
}

