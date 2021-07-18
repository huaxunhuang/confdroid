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


/**
 * Tests that focus works as expected when navigating into and out of
 * a {@link ListView} that has buttons in it.
 */
public class ListOfButtonsTest extends android.test.ActivityInstrumentationTestCase2<android.widget.focus.ListOfButtons> {
    private android.widget.ListAdapter mListAdapter;

    private android.widget.Button mButtonAtTop;

    private android.widget.ListView mListView;

    public ListOfButtonsTest() {
        super(android.widget.focus.ListOfButtons.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        android.widget.focus.ListOfButtons a = getActivity();
        getInstrumentation().waitForIdleSync();
        mListAdapter = a.getListAdapter();
        mButtonAtTop = ((android.widget.Button) (a.findViewById(R.id.button)));
        mListView = a.getListView();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertNotNull(mListAdapter);
        assertNotNull(mButtonAtTop);
        assertNotNull(mListView);
        assertFalse(mButtonAtTop.hasFocus());
        assertTrue(mListView.hasFocus());
        assertEquals("expecting 0 index to be selected", 0, mListView.getSelectedItemPosition());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testNavigateToButtonAbove() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        assertTrue(mButtonAtTop.hasFocus());
        assertFalse(mListView.hasFocus());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testNavigateToSecondItem() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertTrue(mListView.hasFocus());
        android.view.View childOne = mListView.getChildAt(1);
        assertNotNull(childOne);
        assertEquals(childOne, mListView.getFocusedChild());
        assertTrue(childOne.hasFocus());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testNavigateUpAboveAndBackOut() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertFalse("button at top should have focus back", mButtonAtTop.hasFocus());
        assertTrue(mListView.hasFocus());
    }

    // TODO: this reproduces bug 981791
    public void TODO_testNavigateThroughAllButtonsAndBack() {
        java.lang.String[] labels = getActivity().getLabels();
        for (int i = 0; i < labels.length; i++) {
            java.lang.String label = labels[i];
            sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
            getInstrumentation().waitForIdleSync();
            java.lang.String indexInfo = (("index: " + i) + ", label: ") + label;
            assertTrue(indexInfo, mListView.hasFocus());
            android.widget.Button button = ((android.widget.Button) (mListView.getSelectedView()));
            assertNotNull(indexInfo, button);
            assertEquals(indexInfo, label, button.getText().toString());
            assertTrue(indexInfo, button.hasFocus());
        }
        // pressing down again shouldn't matter; make sure last item keeps focus
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        for (int i = labels.length - 1; i >= 0; i--) {
            java.lang.String label = labels[i];
            java.lang.String indexInfo = (("index: " + i) + ", label: ") + label;
            assertTrue(indexInfo, mListView.hasFocus());
            android.widget.Button button = ((android.widget.Button) (mListView.getSelectedView()));
            assertNotNull(indexInfo, button);
            assertEquals(indexInfo, label, button.getText().toString());
            assertTrue(indexInfo, button.hasFocus());
            sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
            getInstrumentation().waitForIdleSync();
        }
        assertTrue("button at top should have focus back", mButtonAtTop.hasFocus());
        assertFalse(mListView.hasFocus());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testGoInAndOutOfListWithItemsFocusable() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        assertTrue(mButtonAtTop.hasFocus());
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        final java.lang.String firstButtonLabel = getActivity().getLabels()[0];
        final android.widget.Button firstButton = ((android.widget.Button) (mListView.getSelectedView()));
        assertTrue(firstButton.isFocused());
        assertEquals(firstButtonLabel, firstButton.getText());
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        assertTrue(mButtonAtTop.isFocused());
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertTrue(firstButton.isFocused());
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_UP);
        assertTrue(mButtonAtTop.isFocused());
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertTrue(firstButton.isFocused());
    }
}

