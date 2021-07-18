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
 * Exercises {@link android.view.View}'s longpress plumbing by testing the
 * disabled case.
 */
public class DisabledLongpressTest extends android.test.ActivityInstrumentationTestCase<android.view.Longpress> {
    private android.view.View mSimpleView;

    private boolean mLongClicked;

    public DisabledLongpressTest() {
        super("com.android.frameworks.coretests", android.view.Longpress.class);
    }

    @java.lang.Override
    public void setUp() throws java.lang.Exception {
        super.setUp();
        final android.view.Longpress a = getActivity();
        mSimpleView = a.findViewById(R.id.simple_view);
        mSimpleView.setOnLongClickListener(new android.view.View.OnLongClickListener() {
            public boolean onLongClick(android.view.View v) {
                mLongClicked = true;
                return true;
            }
        });
        // The View#setOnLongClickListener will ensure the View is long
        // clickable, we reverse that here
        mSimpleView.setLongClickable(false);
    }

    @java.lang.Override
    protected void tearDown() throws java.lang.Exception {
        super.tearDown();
        mLongClicked = false;
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mSimpleView);
        assertTrue(mSimpleView.hasFocus());
        assertFalse(mLongClicked);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testKeypadLongClick() throws java.lang.Exception {
        mSimpleView.requestFocus();
        getInstrumentation().waitForIdleSync();
        android.util.KeyUtils.longClick(this);
        getInstrumentation().waitForIdleSync();
        assertFalse(mLongClicked);
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testTouchLongClick() throws java.lang.Exception {
        android.test.TouchUtils.longClickView(this, mSimpleView);
        getInstrumentation().waitForIdleSync();
        assertFalse(mLongClicked);
    }
}

