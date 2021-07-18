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
 * {@link android.widget.layout.table.VerticalGravity} is
 * setup to exercise tables in which cells use vertical gravity.
 */
public class VerticalGravityTest extends android.test.ActivityInstrumentationTestCase<android.widget.layout.table.VerticalGravity> {
    private android.view.View mReference1;

    private android.view.View mReference2;

    private android.view.View mReference3;

    private android.view.View mTop;

    private android.view.View mCenter;

    private android.view.View mBottom;

    public VerticalGravityTest() {
        super("com.android.frameworks.coretests", android.widget.layout.table.VerticalGravity.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        final android.widget.layout.table.VerticalGravity activity = getActivity();
        mReference1 = activity.findViewById(R.id.reference1);
        mReference2 = activity.findViewById(R.id.reference2);
        mReference3 = activity.findViewById(R.id.reference3);
        mTop = activity.findViewById(R.id.cell_top);
        mCenter = activity.findViewById(R.id.cell_center);
        mBottom = activity.findViewById(R.id.cell_bottom);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mReference1);
        assertNotNull(mReference2);
        assertNotNull(mReference3);
        assertNotNull(mTop);
        assertNotNull(mCenter);
        assertNotNull(mBottom);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testTopGravity() throws java.lang.Exception {
        android.test.ViewAsserts.assertTopAligned(mReference1, mTop);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testCenterGravity() throws java.lang.Exception {
        android.test.ViewAsserts.assertVerticalCenterAligned(mReference2, mCenter);
    }

    @android.test.suitebuilder.annotation.Suppress
    @android.test.suitebuilder.annotation.MediumTest
    public void testBottomGravity() throws java.lang.Exception {
        android.test.ViewAsserts.assertBottomAligned(mReference3, mBottom);
    }
}

