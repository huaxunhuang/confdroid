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


public class GlobalFocusChangeTest extends android.test.ActivityInstrumentationTestCase<android.view.GlobalFocusChange> {
    private android.view.GlobalFocusChange mActivity;

    private android.view.View mLeft;

    private android.view.View mRight;

    public GlobalFocusChangeTest() {
        super("com.android.frameworks.coretests", android.view.GlobalFocusChange.class);
    }

    @java.lang.Override
    public void setUp() throws java.lang.Exception {
        super.setUp();
        mActivity = getActivity();
        mLeft = mActivity.findViewById(R.id.left);
        mRight = mActivity.findViewById(R.id.right);
    }

    @java.lang.Override
    protected void tearDown() throws java.lang.Exception {
        mActivity.reset();
        super.tearDown();
    }

    @android.test.FlakyTest(tolerance = 4)
    @android.test.suitebuilder.annotation.LargeTest
    public void testFocusChange() throws java.lang.Exception {
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_RIGHT);
        assertFalse(mLeft.isFocused());
        assertTrue(mRight.isFocused());
        assertSame(mLeft, mActivity.mOldFocus);
        assertSame(mRight, mActivity.mNewFocus);
    }

    @android.test.FlakyTest(tolerance = 4)
    @android.test.suitebuilder.annotation.MediumTest
    public void testEnterTouchMode() throws java.lang.Exception {
        assertTrue(mLeft.isFocused());
        android.test.TouchUtils.tapView(this, mLeft);
        assertSame(mLeft, mActivity.mOldFocus);
        assertSame(null, mActivity.mNewFocus);
    }

    @android.test.FlakyTest(tolerance = 4)
    @android.test.suitebuilder.annotation.MediumTest
    public void testLeaveTouchMode() throws java.lang.Exception {
        assertTrue(mLeft.isFocused());
        android.test.TouchUtils.tapView(this, mLeft);
        sendKeys(android.view.KeyEvent.KEYCODE_DPAD_RIGHT);
        assertTrue(mLeft.isFocused());
        assertSame(null, mActivity.mOldFocus);
        assertSame(mLeft, mActivity.mNewFocus);
    }
}

