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
 * Make sure focus isn't kept by buttons when entering touch mode.
 *
 * When in touch mode and hitting the d-pad, we should leave touch mode and the
 * top most focusable gets focus.
 */
public class TouchModeFocusChangeTest extends android.test.ActivityInstrumentationTestCase<android.widget.layout.linear.LLOfButtons1> {
    private android.widget.layout.linear.LLOfButtons1 mActivity;

    private android.widget.Button mFirstButton;

    public TouchModeFocusChangeTest() {
        super("com.android.frameworks.coretests", android.widget.layout.linear.LLOfButtons1.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mActivity = getActivity();
        mFirstButton = mActivity.getFirstButton();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertFalse("we should not be in touch mode", mActivity.isInTouchMode());
        assertTrue("top button should have focus", mFirstButton.isFocused());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testTouchButtonNotTakeFocus() {
        android.util.TouchModeFlexibleAsserts.assertInTouchModeAfterTap(this, mFirstButton);
        assertTrue("should be in touch mode", mActivity.isInTouchMode());
        assertFalse("button.isFocused", mFirstButton.isFocused());
        assertFalse("button.hasFocus", mFirstButton.hasFocus());
        assertNull("activity shouldn't have focus", mActivity.getCurrentFocus());
        assertFalse("linear layout should not have focus", mActivity.getLayout().hasFocus());
        assertTrue("button's onClickListener should have fired", mActivity.buttonClickListenerFired());
    }

    // TODO: reenable when more reliable
    public void DISABLE_testLeaveTouchModeWithDpadEvent() {
        android.util.TouchModeFlexibleAsserts.assertInTouchModeAfterClick(this, mFirstButton);
        assertTrue("should be in touch mode", mActivity.isInTouchMode());
        assertFalse("button should not have focus when touched", mFirstButton.isFocused());
        android.util.TouchModeFlexibleAsserts.assertNotInTouchModeAfterKey(this, android.view.KeyEvent.KEYCODE_DPAD_RIGHT, mFirstButton);
        assertFalse("should be out of touch mode", mActivity.isInTouchMode());
        assertTrue("first button (the top most focusable) should have gained focus", mFirstButton.isFocused());
    }
}

