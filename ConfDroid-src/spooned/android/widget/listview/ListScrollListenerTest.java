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


public class ListScrollListenerTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListScrollListener> implements android.widget.AbsListView.OnScrollListener {
    private android.widget.listview.ListScrollListener mActivity;

    private android.widget.ListView mListView;

    private int mFirstVisibleItem = -1;

    private int mVisibleItemCount = -1;

    private int mTotalItemCount = -1;

    public ListScrollListenerTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListScrollListener.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mActivity = getActivity();
        mListView = getActivity().getListView();
        mListView.setOnScrollListener(this);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertNotNull(mActivity);
        assertNotNull(mListView);
        assertEquals(0, mFirstVisibleItem);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testKeyScrolling() {
        android.app.Instrumentation inst = getInstrumentation();
        int firstVisibleItem = mFirstVisibleItem;
        for (int i = 0; i < (mVisibleItemCount * 2); i++) {
            inst.sendCharacterSync(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        }
        inst.waitForIdleSync();
        assertTrue("Arrow scroll did not happen", mFirstVisibleItem > firstVisibleItem);
        firstVisibleItem = mFirstVisibleItem;
        inst.sendCharacterSync(android.view.KeyEvent.KEYCODE_SPACE);
        inst.waitForIdleSync();
        assertTrue("Page scroll did not happen", mFirstVisibleItem > firstVisibleItem);
        firstVisibleItem = mFirstVisibleItem;
        android.view.KeyEvent down = new android.view.KeyEvent(0, 0, android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_DPAD_DOWN, 0, android.view.KeyEvent.META_ALT_ON);
        android.view.KeyEvent up = new android.view.KeyEvent(0, 0, android.view.KeyEvent.ACTION_UP, android.view.KeyEvent.KEYCODE_DPAD_DOWN, 0, android.view.KeyEvent.META_ALT_ON);
        inst.sendKeySync(down);
        inst.sendKeySync(up);
        inst.waitForIdleSync();
        assertTrue("Full scroll did not happen", mFirstVisibleItem > firstVisibleItem);
        assertEquals("Full scroll did not happen", mTotalItemCount, mFirstVisibleItem + mVisibleItemCount);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testTouchScrolling() {
        int firstVisibleItem = mFirstVisibleItem;
        android.test.TouchUtils.dragQuarterScreenUp(this);
        android.test.TouchUtils.dragQuarterScreenUp(this);
        assertTrue("Touch scroll did not happen", mFirstVisibleItem > firstVisibleItem);
    }

    public void onScroll(android.widget.AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        mTotalItemCount = totalItemCount;
    }

    public void onScrollStateChanged(android.widget.AbsListView view, int scrollState) {
    }
}

