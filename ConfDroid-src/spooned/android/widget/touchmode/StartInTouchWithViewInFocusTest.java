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


public class StartInTouchWithViewInFocusTest extends android.test.ActivityInstrumentationTestCase2<android.widget.layout.linear.LLEditTextThenButton> {
    private android.widget.EditText mEditText;

    private android.widget.Button mButton;

    public StartInTouchWithViewInFocusTest() {
        super("com.android.frameworks.coretests", android.widget.layout.linear.LLEditTextThenButton.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        this.setActivityInitialTouchMode(true);
        mEditText = getActivity().getEditText();
        mButton = getActivity().getButton();
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testPreconditions() {
        assertTrue("should start in touch mode", mEditText.isInTouchMode());
        assertTrue("edit text is focusable in touch mode, should have focus", mEditText.isFocused());
    }

    // TODO: reenable when more reliable
    public void DISABLE_testKeyDownLeavesTouchModeAndGoesToNextView() {
        android.util.TouchModeFlexibleAsserts.assertNotInTouchModeAfterKey(this, android.view.KeyEvent.KEYCODE_DPAD_DOWN, mEditText);
        assertFalse("should have left touch mode", mEditText.isInTouchMode());
        assertTrue("should have given focus to next view", mButton.isFocused());
    }

    // TODO: reenable when more reliable
    public void DISABLE_testNonDirectionalKeyExitsTouchMode() {
        android.util.TouchModeFlexibleAsserts.assertNotInTouchModeAfterKey(this, android.view.KeyEvent.KEYCODE_A, mEditText);
        assertFalse("should have left touch mode", mEditText.isInTouchMode());
        assertTrue("edit text should still have focus", mEditText.isFocused());
    }
}

