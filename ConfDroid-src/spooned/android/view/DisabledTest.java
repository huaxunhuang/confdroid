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
package android.view;


/**
 * Exercises {@link android.view.View}'s disabled property.
 */
public class DisabledTest extends android.test.ActivityInstrumentationTestCase<android.view.Disabled> {
    private android.widget.Button mDisabled;

    private android.view.View mDisabledParent;

    private boolean mClicked;

    private boolean mParentClicked;

    public DisabledTest() {
        super("com.android.frameworks.coretests", android.view.Disabled.class);
    }

    @java.lang.Override
    public void setUp() throws java.lang.Exception {
        super.setUp();
        final android.view.Disabled a = getActivity();
        mDisabled = ((android.widget.Button) (a.findViewById(R.id.disabledButton)));
        mDisabled.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                mClicked = true;
            }
        });
        mDisabledParent = a.findViewById(R.id.clickableParent);
        mDisabledParent.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                mParentClicked = true;
            }
        });
    }

    @java.lang.Override
    protected void tearDown() throws java.lang.Exception {
        super.tearDown();
        mClicked = false;
        mParentClicked = false;
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mDisabled);
        assertNotNull(mDisabledParent);
        assertFalse(mDisabled.isEnabled());
        assertTrue(mDisabledParent.isEnabled());
        assertTrue(mDisabled.hasFocus());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testKeypadClick() throws java.lang.Exception {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_CENTER);
        getInstrumentation().waitForIdleSync();
        assertFalse(mClicked);
        assertFalse(mParentClicked);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testTouchClick() throws java.lang.Exception {
        android.test.TouchUtils.clickView(this, mDisabled);
        getInstrumentation().waitForIdleSync();
        assertFalse(mClicked);
        assertFalse(mParentClicked);
    }
}

