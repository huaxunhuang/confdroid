/**
 * Copyright (C) 2008 The Android Open Source Project
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


public class ListEmptyViewTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListWithEmptyView> {
    private android.widget.listview.ListWithEmptyView mActivity;

    private android.widget.ListView mListView;

    public ListEmptyViewTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListWithEmptyView.class);
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
        assertTrue("Empty view not shown", mListView.getVisibility() == android.view.View.GONE);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testZeroToOne() {
        android.app.Instrumentation inst = getInstrumentation();
        inst.invokeMenuActionSync(mActivity, mActivity.MENU_ADD, 0);
        inst.waitForIdleSync();
        assertTrue("Empty view still shown", mActivity.getEmptyView().getVisibility() == android.view.View.GONE);
        assertTrue("List not shown", mActivity.getListView().getVisibility() == android.view.View.VISIBLE);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testZeroToOneForwardBack() {
        android.app.Instrumentation inst = getInstrumentation();
        inst.invokeMenuActionSync(mActivity, mActivity.MENU_ADD, 0);
        inst.waitForIdleSync();
        assertTrue("Empty view still shown", mActivity.getEmptyView().getVisibility() == android.view.View.GONE);
        assertTrue("List not shown", mActivity.getListView().getVisibility() == android.view.View.VISIBLE);
        // Navigate forward
        android.content.Intent intent = new android.content.Intent();
        intent.setClass(mActivity, android.widget.listview.ListWithEmptyView.class);
        mActivity.startActivity(intent);
        // Navigate backward
        inst.sendCharacterSync(android.view.KeyEvent.KEYCODE_BACK);
        inst.waitForIdleSync();
        assertTrue("Empty view still shown", mActivity.getEmptyView().getVisibility() == android.view.View.GONE);
        assertTrue("List not shown", mActivity.getListView().getVisibility() == android.view.View.VISIBLE);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testZeroToManyToZero() {
        android.app.Instrumentation inst = getInstrumentation();
        int i;
        for (i = 0; i < 10; i++) {
            inst.invokeMenuActionSync(mActivity, mActivity.MENU_ADD, 0);
            inst.waitForIdleSync();
            assertTrue("Empty view still shown", mActivity.getEmptyView().getVisibility() == android.view.View.GONE);
            assertTrue("List not shown", mActivity.getListView().getVisibility() == android.view.View.VISIBLE);
        }
        for (i = 0; i < 10; i++) {
            inst.invokeMenuActionSync(mActivity, mActivity.MENU_REMOVE, 0);
            inst.waitForIdleSync();
            if (i < 9) {
                assertTrue("Empty view still shown", mActivity.getEmptyView().getVisibility() == android.view.View.GONE);
                assertTrue("List not shown", mActivity.getListView().getVisibility() == android.view.View.VISIBLE);
            } else {
                assertTrue("Empty view not shown", mActivity.getEmptyView().getVisibility() == android.view.View.VISIBLE);
                assertTrue("List still shown", mActivity.getListView().getVisibility() == android.view.View.GONE);
            }
        }
        // Navigate forward
        android.content.Intent intent = new android.content.Intent();
        intent.setClass(mActivity, android.widget.listview.ListWithEmptyView.class);
        mActivity.startActivity(intent);
        // Navigate backward
        inst.sendCharacterSync(android.view.KeyEvent.KEYCODE_BACK);
        inst.waitForIdleSync();
        assertTrue("Empty view not shown", mActivity.getEmptyView().getVisibility() == android.view.View.VISIBLE);
        assertTrue("List still shown", mActivity.getListView().getVisibility() == android.view.View.GONE);
    }
}

