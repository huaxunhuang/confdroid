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


/**
 * Testing the ListView getCheckItemIds() method in different situations.
 */
public class ListGetCheckItemIdsTest extends android.test.ActivityInstrumentationTestCase2<android.widget.listview.ListSimple> {
    private android.widget.ListView mListView;

    public ListGetCheckItemIdsTest() {
        super(android.widget.listview.ListSimple.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mListView = getActivity().getListView();
    }

    private void assertChecked(java.lang.String message, long... expectedIds) {
        // Sort the two arrays since we are actually doing a set equality.
        long[] checkItemIds = mListView.getCheckItemIds();
        long[] sortedCheckItemsIds = new long[checkItemIds.length];
        java.lang.System.arraycopy(checkItemIds, 0, sortedCheckItemsIds, 0, checkItemIds.length);
        java.util.Arrays.sort(sortedCheckItemsIds);
        long[] sortedExpectedIds = new long[expectedIds.length];
        java.lang.System.arraycopy(expectedIds, 0, sortedExpectedIds, 0, expectedIds.length);
        java.util.Arrays.sort(sortedExpectedIds);
        assertTrue(message, java.util.Arrays.equals(sortedExpectedIds, sortedCheckItemsIds));
    }

    @android.test.suitebuilder.annotation.MediumTest
    @android.test.UiThreadTest
    public void testNoneCheck() {
        mListView.setChoiceMode(android.widget.ListView.CHOICE_MODE_NONE);
        mListView.setItemChecked(0, true);
        assertChecked("None check choice has item checked");
    }

    @android.test.suitebuilder.annotation.MediumTest
    @android.test.UiThreadTest
    public void testSimpleCheck() {
        mListView.setChoiceMode(android.widget.ListView.CHOICE_MODE_SINGLE);
        assertChecked("Item checked when setting Single mode");
        // Test a check at each position
        int childCount = mListView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            mListView.setItemChecked(i, true);
            assertChecked(("Only element " + i) + " should be checked", i);
        }
        // Check an element and uncheck some others
        for (int i = 0; i < childCount; i++) {
            mListView.setItemChecked(i, true);
            mListView.setItemChecked(((i - 3) + childCount) % childCount, false);
            mListView.setItemChecked((i + 1) % childCount, false);
            assertChecked(("Only element " + i) + " should be checked", i);
        }
    }

    @android.test.suitebuilder.annotation.MediumTest
    @android.test.UiThreadTest
    public void testMultipleCheck() {
        mListView.setChoiceMode(android.widget.ListView.CHOICE_MODE_MULTIPLE);
        assertChecked("Item checked when setting Multiple mode");
        int childCount = mListView.getChildCount();
        assertTrue("Tests requires at least 4 items", childCount >= 4);
        mListView.setItemChecked(1, true);
        assertChecked("First element non checked", 1);
        mListView.setItemChecked(3, true);
        assertChecked("Second element not checked", 1, 3);
        mListView.setItemChecked(0, true);
        assertChecked("Third element not checked", 0, 1, 3);
        mListView.setItemChecked(2, false);
        assertChecked("Unchecked element appears checked", 0, 1, 3);
        mListView.setItemChecked(1, false);
        assertChecked("Unchecked element remains", 0, 3);
        mListView.setItemChecked(2, false);
        assertChecked("Already unchecked element appears", 0, 3);
        mListView.setItemChecked(3, false);
        assertChecked("Unchecked 3 remains", 0);
        mListView.setItemChecked(3, false);
        assertChecked("Twice unchecked 3 remains", 0);
        mListView.setItemChecked(0, false);
        assertChecked("Checked items after last element unchecked");
    }

    @android.test.suitebuilder.annotation.MediumTest
    @android.test.UiThreadTest
    public void testClearChoices() {
        mListView.setChoiceMode(android.widget.ListView.CHOICE_MODE_SINGLE);
        mListView.setItemChecked(0, true);
        mListView.clearChoices();
        assertChecked("Item checked after SINGLE clear choice");
        mListView.setChoiceMode(android.widget.ListView.CHOICE_MODE_MULTIPLE);
        int childCount = mListView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            mListView.setItemChecked(0, (i % 3) == 0);
        }
        mListView.clearChoices();
        assertChecked("Item checked after MULTIPLE clear choice");
    }
}

