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
package android.widget.layout.linear;


public class BaselineAlignmentZeroWidthAndWeightTest extends android.test.ActivityInstrumentationTestCase<android.widget.layout.linear.BaselineAlignmentZeroWidthAndWeight> {
    private android.widget.Button mShowButton;

    public BaselineAlignmentZeroWidthAndWeightTest() {
        super("com.android.frameworks.coretests", android.widget.layout.linear.BaselineAlignmentZeroWidthAndWeight.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        final android.app.Activity activity = getActivity();
        mShowButton = ((android.widget.Button) (activity.findViewById(R.id.show)));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mShowButton);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testComputeTexViewWithoutIllegalArgumentException() throws java.lang.Exception {
        assertTrue(mShowButton.hasFocus());
        // Pressing the button will show an ExceptionTextView that might set a failed bit if
        // the test fails.
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_CENTER);
        getInstrumentation().waitForIdleSync();
        final android.widget.layout.linear.ExceptionTextView etv = ((android.widget.layout.linear.ExceptionTextView) (getActivity().findViewById(R.id.routeToField)));
        assertFalse("exception test view should not fail", etv.isFailed());
    }
}

