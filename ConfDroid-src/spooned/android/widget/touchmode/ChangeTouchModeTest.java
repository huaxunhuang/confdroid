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
package android.widget.touchmode;


/**
 * Tests that the touch mode changes from various events, and that the state
 * persists across activities.
 */
public class ChangeTouchModeTest extends android.test.ActivityInstrumentationTestCase<android.widget.layout.linear.LLOfButtons1> {
    public ChangeTouchModeTest() {
        super("com.android.frameworks.coretests", android.widget.layout.linear.LLOfButtons1.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() throws java.lang.Exception {
        assertFalse("touch mode", getActivity().isInTouchMode());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testTouchingScreenEntersTouchMode() throws java.lang.Exception {
        android.util.TouchModeFlexibleAsserts.assertInTouchModeAfterTap(this, getActivity().getFirstButton());
        assertTrue("touch mode", getActivity().isInTouchMode());
    }

    // TODO: reenable when more reliable
    public void DISABLE_testDpadDirectionLeavesTouchMode() throws java.lang.Exception {
        android.util.TouchModeFlexibleAsserts.assertInTouchModeAfterClick(this, getActivity().getFirstButton());
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_RIGHT);
        android.util.TouchModeFlexibleAsserts.assertNotInTouchModeAfterKey(this, android.view.KeyEvent.KEYCODE_DPAD_RIGHT, getActivity().getFirstButton());
        assertFalse("touch mode", getActivity().isInTouchMode());
    }

    public void TODO_touchTrackBallMovementLeavesTouchMode() throws java.lang.Exception {
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testTouchModeFalseAcrossActivites() throws java.lang.Exception {
        getInstrumentation().waitForIdleSync();
        android.widget.layout.linear.LLOfButtons2 otherActivity = null;
        try {
            otherActivity = launchActivity("com.android.frameworks.coretests", android.widget.layout.linear.LLOfButtons2.class, null);
            assertNotNull(otherActivity);
            assertFalse(otherActivity.isInTouchMode());
        } finally {
            if (otherActivity != null) {
                otherActivity.finish();
            }
        }
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testTouchModeTrueAcrossActivites() throws java.lang.Exception {
        android.util.TouchModeFlexibleAsserts.assertInTouchModeAfterClick(this, getActivity().getFirstButton());
        android.widget.layout.linear.LLOfButtons2 otherActivity = null;
        try {
            otherActivity = launchActivity("com.android.frameworks.coretests", android.widget.layout.linear.LLOfButtons2.class, null);
            assertNotNull(otherActivity);
            assertTrue(otherActivity.isInTouchMode());
        } finally {
            if (otherActivity != null) {
                otherActivity.finish();
            }
        }
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testTouchModeChangedInOtherActivity() throws java.lang.Exception {
        assertFalse("touch mode", getActivity().isInTouchMode());
        android.widget.layout.linear.LLOfButtons2 otherActivity = null;
        try {
            otherActivity = launchActivity("com.android.frameworks.coretests", android.widget.layout.linear.LLOfButtons2.class, null);
            assertNotNull(otherActivity);
            assertFalse(otherActivity.isInTouchMode());
            android.util.TouchModeFlexibleAsserts.assertInTouchModeAfterClick(this, otherActivity.getFirstButton());
            assertTrue(otherActivity.isInTouchMode());
        } finally {
            if (otherActivity != null) {
                otherActivity.finish();
            }
        }
        // need to wait for async update back to window to occur
        java.lang.Thread.sleep(200);
        assertTrue("touch mode", getActivity().isInTouchMode());
    }
}

