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


/**
 * Tests restoring the scroll position in a list with a managed cursor.
 */
public class ListManagedCursorTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListManagedCursor> {
    private android.widget.listview.ListManagedCursor mActivity;

    private android.widget.ListView mListView;

    public ListManagedCursorTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListManagedCursor.class);
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
        assertEquals(0, mListView.getFirstVisiblePosition());
    }

    /**
     * Scroll the list using arrows, launch new activity, hit back, make sure we're still scrolled.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testKeyScrolling() {
        android.app.Instrumentation inst = getInstrumentation();
        int firstVisiblePosition = arrowScroll(inst);
        inst.sendCharacterSync(android.view.KeyEvent.KEYCODE_BACK);
        inst.waitForIdleSync();
        assertTrue("List changed to touch mode", !mListView.isInTouchMode());
        assertTrue("List did not preserve scroll position", firstVisiblePosition == mListView.getFirstVisiblePosition());
    }

    /**
     * Scroll the list using touch, launch new activity, hit back, make sure we're still scrolled.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testTouchScrolling() {
        android.app.Instrumentation inst = getInstrumentation();
        int firstVisiblePosition = touchScroll(inst);
        inst.sendCharacterSync(android.view.KeyEvent.KEYCODE_BACK);
        inst.waitForIdleSync();
        assertTrue("List not in touch mode", mListView.isInTouchMode());
        assertTrue("List did not preserve scroll position", firstVisiblePosition == mListView.getFirstVisiblePosition());
    }

    /**
     * Scroll the list using arrows, launch new activity, change to touch mode, hit back, make sure
     * we're still scrolled.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testKeyScrollingToTouchMode() {
        android.app.Instrumentation inst = getInstrumentation();
        int firstVisiblePosition = arrowScroll(inst);
        android.test.TouchUtils.dragQuarterScreenUp(this);
        inst.sendCharacterSync(android.view.KeyEvent.KEYCODE_BACK);
        inst.waitForIdleSync();
        assertTrue("List did not change to touch mode", mListView.isInTouchMode());
        assertTrue("List did not preserve scroll position", firstVisiblePosition == mListView.getFirstVisiblePosition());
    }

    /**
     * Scroll the list using touch, launch new activity, change to trackball mode, hit back, make
     * sure we're still scrolled.
     */
    @android.test.FlakyTest(tolerance = 3)
    @android.test.suitebuilder.annotation.LargeTest
    public void testTouchScrollingToTrackballMode() {
        android.app.Instrumentation inst = getInstrumentation();
        int firstVisiblePosition = touchScroll(inst);
        inst.sendCharacterSync(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        inst.waitForIdleSync();
        inst.sendCharacterSync(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        inst.waitForIdleSync();
        inst.sendCharacterSync(android.view.KeyEvent.KEYCODE_BACK);
        inst.waitForIdleSync();
        assertTrue("List not in trackball mode", !mListView.isInTouchMode());
        assertTrue("List did not preserve scroll position", firstVisiblePosition == mListView.getFirstVisiblePosition());
    }

    public int arrowScroll(android.app.Instrumentation inst) {
        int count = mListView.getChildCount();
        for (int i = 0; i < (count * 2); i++) {
            inst.sendCharacterSync(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        }
        inst.waitForIdleSync();
        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        assertTrue("Arrow scroll did not happen", firstVisiblePosition > 0);
        assertTrue("List still in touch mode", !mListView.isInTouchMode());
        inst.sendCharacterSync(android.view.KeyEvent.KEYCODE_DPAD_CENTER);
        inst.waitForIdleSync();
        try {
            java.lang.Thread.sleep(3000);
        } catch (java.lang.InterruptedException e) {
            e.printStackTrace();
        }
        return firstVisiblePosition;
    }

    public int touchScroll(android.app.Instrumentation inst) {
        android.test.TouchUtils.dragQuarterScreenUp(this);
        inst.waitForIdleSync();
        android.test.TouchUtils.dragQuarterScreenUp(this);
        inst.waitForIdleSync();
        android.test.TouchUtils.dragQuarterScreenUp(this);
        inst.waitForIdleSync();
        android.test.TouchUtils.dragQuarterScreenUp(this);
        inst.waitForIdleSync();
        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        assertTrue("Touch scroll did not happen", firstVisiblePosition > 0);
        assertTrue("List not in touch mode", mListView.isInTouchMode());
        android.test.TouchUtils.clickView(this, mListView.getChildAt(mListView.getChildCount() - 1));
        inst.waitForIdleSync();
        try {
            java.lang.Thread.sleep(3000);
        } catch (java.lang.InterruptedException e) {
            e.printStackTrace();
        }
        return firstVisiblePosition;
    }
}

