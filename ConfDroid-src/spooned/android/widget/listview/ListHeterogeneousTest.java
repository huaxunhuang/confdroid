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


public class ListHeterogeneousTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListHeterogeneous> {
    private android.widget.listview.ListHeterogeneous mActivity;

    private android.widget.ListView mListView;

    public ListHeterogeneousTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListHeterogeneous.class);
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
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testKeyScrolling() {
        android.app.Instrumentation inst = getInstrumentation();
        int count = mListView.getAdapter().getCount();
        for (int i = 0; i < (count - 1); i++) {
            inst.sendCharacterSync(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        }
        inst.waitForIdleSync();
        int convertMissesBefore = mActivity.getConvertMisses();
        assertEquals("Unexpected convert misses", 0, convertMissesBefore);
        for (int i = 0; i < (count - 1); i++) {
            inst.sendCharacterSync(android.view.KeyEvent.KEYCODE_DPAD_UP);
        }
        inst.waitForIdleSync();
        int convertMissesAfter = mActivity.getConvertMisses();
        assertEquals("Unexpected convert misses", 0, convertMissesAfter);
    }
}

