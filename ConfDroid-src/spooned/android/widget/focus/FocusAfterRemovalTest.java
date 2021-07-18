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
 * {@link FocusAfterRemoval} is set up to exercise cases where the views that
 * have focus become invisible or GONE.
 */
public class FocusAfterRemovalTest extends android.test.ActivityInstrumentationTestCase<android.widget.focus.FocusAfterRemoval> {
    private android.widget.LinearLayout mLeftLayout;

    private android.widget.Button mTopLeftButton;

    private android.widget.Button mBottomLeftButton;

    private android.widget.Button mTopRightButton;

    private android.widget.Button mBottomRightButton;

    public FocusAfterRemovalTest() {
        super("com.android.frameworks.coretests", android.widget.focus.FocusAfterRemoval.class);
    }

    @java.lang.Override
    public void setUp() throws java.lang.Exception {
        super.setUp();
        final android.widget.focus.FocusAfterRemoval a = getActivity();
        mLeftLayout = ((android.widget.LinearLayout) (a.findViewById(R.id.leftLayout)));
        mTopLeftButton = ((android.widget.Button) (a.findViewById(R.id.topLeftButton)));
        mBottomLeftButton = ((android.widget.Button) (a.findViewById(R.id.bottomLeftButton)));
        mTopRightButton = ((android.widget.Button) (a.findViewById(R.id.topRightButton)));
        mBottomRightButton = ((android.widget.Button) (a.findViewById(R.id.bottomRightButton)));
    }

    // Test that setUp did what we expect it to do.  These asserts
    // can't go in SetUp, or the test will hang.
    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mLeftLayout);
        assertNotNull(mTopLeftButton);
        assertNotNull(mTopRightButton);
        assertNotNull(mBottomLeftButton);
        assertNotNull(mBottomRightButton);
        assertTrue(mTopLeftButton.hasFocus());
    }

    // if a parent layout becomes GONE when one of its children has focus,
    // make sure the focus moves to something visible (bug 827087)
    @android.test.suitebuilder.annotation.MediumTest
    public void testFocusLeavesWhenParentLayoutIsGone() throws java.lang.Exception {
        // clicking on this button makes its parent linear layout GONE
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_CENTER);
        assertEquals(android.view.View.GONE, mLeftLayout.getVisibility());
        assertTrue("focus should jump to visible button", mTopRightButton.hasFocus());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testFocusLeavesWhenParentLayoutInvisible() throws java.lang.Exception {
        // move down to bottom left button
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertTrue(mBottomLeftButton.hasFocus());
        // clicking on this button makes its parent linear layout INVISIBLE
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_CENTER);
        assertEquals(android.view.View.INVISIBLE, getActivity().findViewById(R.id.leftLayout).getVisibility());
        assertTrue("focus should jump to visible button", mTopRightButton.hasFocus());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testFocusLeavesWhenFocusedViewBecomesGone() throws java.lang.Exception {
        // move to top right
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_RIGHT);
        assertTrue(mTopRightButton.hasFocus());
        // click making it GONE
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_CENTER);
        assertEquals(android.view.View.GONE, mTopRightButton.getVisibility());
        assertTrue("focus should jump to visible button", mTopLeftButton.hasFocus());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testFocusLeavesWhenFocusedViewBecomesInvisible() throws java.lang.Exception {
        // move to bottom right
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_RIGHT);
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertTrue(mBottomRightButton.hasFocus());
        // click making it INVISIBLE
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_CENTER);
        assertEquals(android.view.View.INVISIBLE, mBottomRightButton.getVisibility());
        assertTrue("focus should jump to visible button", mTopLeftButton.hasFocus());
    }
}

