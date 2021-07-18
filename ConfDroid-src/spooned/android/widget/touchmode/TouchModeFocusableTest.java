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
 * Some views, like edit texts, can keep and gain focus even when in touch mode.
 */
public class TouchModeFocusableTest extends android.test.ActivityInstrumentationTestCase<android.widget.layout.linear.LLEditTextThenButton> {
    private android.widget.EditText mEditText;

    private android.widget.Button mButton;

    public TouchModeFocusableTest() {
        super("com.android.frameworks.coretests", android.widget.layout.linear.LLEditTextThenButton.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mEditText = getActivity().getEditText();
        mButton = getActivity().getButton();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertFalse("should not be in touch mode to start off", mButton.isInTouchMode());
        assertTrue("edit text should have focus", mEditText.isFocused());
        assertTrue("edit text should be focusable in touch mode", mEditText.isFocusableInTouchMode());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testClickButtonEditTextKeepsFocus() {
        android.util.TouchModeFlexibleAsserts.assertInTouchModeAfterTap(this, mButton);
        assertTrue("should be in touch mode", mButton.isInTouchMode());
        assertTrue("edit text should still have focus", mEditText.isFocused());
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testClickEditTextGivesItFocus() {
        // go down to button
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertTrue("button should have focus", mButton.isFocused());
        android.util.TouchModeFlexibleAsserts.assertInTouchModeAfterClick(this, mEditText);
        assertTrue("clicking edit text should have entered touch mode", mButton.isInTouchMode());
        assertTrue("clicking edit text should have given it focus", mEditText.isFocused());
    }

    // entering touch mode takes focus away from the currently focused item if it
    // isn't focusable in touch mode.
    @android.test.suitebuilder.annotation.LargeTest
    public void testEnterTouchModeGivesFocusBackToFocusableInTouchMode() {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
        assertTrue("button should have focus", mButton.isFocused());
        android.util.TouchModeFlexibleAsserts.assertInTouchModeAfterClick(this, mButton);
        assertTrue("should be in touch mode", mButton.isInTouchMode());
        assertNull("nothing should have focus", getActivity().getCurrentFocus());
        assertFalse("layout should not have focus", getActivity().getLayout().hasFocus());
    }
}

