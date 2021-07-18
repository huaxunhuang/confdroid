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
package android.widget.layout.table;


/**
 * {@link android.widget.layout.table.HorizontalGravity} is
 * setup to exercise tables in which cells use horizontal gravity.
 */
public class HorizontalGravityTest extends android.test.ActivityInstrumentationTestCase<android.widget.layout.table.HorizontalGravity> {
    private android.view.View mReference;

    private android.view.View mCenter;

    private android.view.View mBottomRight;

    private android.view.View mLeft;

    public HorizontalGravityTest() {
        super("com.android.frameworks.coretests", android.widget.layout.table.HorizontalGravity.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        final android.widget.layout.table.HorizontalGravity activity = getActivity();
        mReference = activity.findViewById(R.id.reference);
        mCenter = activity.findViewById(R.id.center);
        mBottomRight = activity.findViewById(R.id.bottomRight);
        mLeft = activity.findViewById(R.id.left);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mReference);
        assertNotNull(mCenter);
        assertNotNull(mBottomRight);
        assertNotNull(mLeft);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testCenterGravity() throws java.lang.Exception {
        android.test.ViewAsserts.assertHorizontalCenterAligned(mReference, mCenter);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testLeftGravity() throws java.lang.Exception {
        android.test.ViewAsserts.assertLeftAligned(mReference, mLeft);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testRightGravity() throws java.lang.Exception {
        android.test.ViewAsserts.assertRightAligned(mReference, mBottomRight);
    }
}

