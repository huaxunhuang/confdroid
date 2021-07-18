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


public class ListViewHeightTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListViewHeight> {
    private android.widget.listview.ListViewHeight mActivity;

    public ListViewHeightTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListViewHeight.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mActivity = getActivity();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertNotNull(mActivity);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testButtons() {
        android.app.Instrumentation inst = getInstrumentation();
        final android.widget.Button button1 = ((android.widget.Button) (mActivity.findViewById(R.id.button1)));
        final android.widget.Button button2 = ((android.widget.Button) (mActivity.findViewById(R.id.button2)));
        final android.widget.Button button3 = ((android.widget.Button) (mActivity.findViewById(R.id.button3)));
        android.widget.ListView list = ((android.widget.ListView) (mActivity.findViewById(R.id.inner_list)));
        assertEquals("Unexpected items in adapter", 0, list.getCount());
        assertEquals("Unexpected children in list view", 0, list.getChildCount());
        mActivity.runOnUiThread(new java.lang.Runnable() {
            public void run() {
                button1.performClick();
            }
        });
        inst.waitForIdleSync();
        assertTrue("List not be visible after clicking button1", list.isShown());
        assertTrue("List incorrect height", list.getHeight() == 200);
        mActivity.runOnUiThread(new java.lang.Runnable() {
            public void run() {
                button2.performClick();
            }
        });
        inst.waitForIdleSync();
        assertTrue("List not be visible after clicking button2", list.isShown());
        mActivity.runOnUiThread(new java.lang.Runnable() {
            public void run() {
                button3.performClick();
            }
        });
        inst.waitForIdleSync();
        assertFalse("List should not be visible clicking button3", list.isShown());
    }
}

