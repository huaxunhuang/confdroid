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
public class ListOfTouchablesTest extends android.test.ActivityInstrumentationTestCase<android.widget.listview.ListOfTouchables> {
    private android.widget.listview.ListOfTouchables mActivity;

    private android.widget.ListView mListView;

    public ListOfTouchablesTest() {
        super("com.android.frameworks.coretests", android.widget.listview.ListOfTouchables.class);
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

    // TODO: needs to be adjusted to pass on non-HVGA displays
    // @LargeTest
    public void testShortScroll() {
        android.view.View firstChild = mListView.getChildAt(0);
        android.view.View lastChild = mListView.getChildAt(mListView.getChildCount() - 1);
        int firstTop = firstChild.getTop();
        android.test.TouchUtils.dragViewBy(this, lastChild, android.view.Gravity.TOP | android.view.Gravity.LEFT, 0, -((android.view.ViewConfiguration.getTouchSlop() + 1) + 10));
        android.view.View newFirstChild = mListView.getChildAt(0);
        assertEquals("View scrolled too early", firstTop, newFirstChild.getTop() + 10);
        assertEquals("Wrong view in first position", 0, newFirstChild.getId());
    }

    // TODO: needs to be adjusted to pass on non-HVGA displays
    // @LargeTest
    public void testLongScroll() {
        android.view.View lastChild = mListView.getChildAt(mListView.getChildCount() - 1);
        int lastTop = lastChild.getTop();
        int distance = android.test.TouchUtils.dragViewToY(this, lastChild, android.view.Gravity.TOP | android.view.Gravity.LEFT, mListView.getTop());
        assertEquals("View scrolled to wrong position", lastTop - ((distance - android.view.ViewConfiguration.getTouchSlop()) - 1), lastChild.getTop());
    }
}

