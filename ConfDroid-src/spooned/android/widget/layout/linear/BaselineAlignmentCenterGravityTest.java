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


public class BaselineAlignmentCenterGravityTest extends android.test.ActivityInstrumentationTestCase<android.widget.layout.linear.BaselineAlignmentCenterGravity> {
    private android.widget.Button mButton1;

    private android.widget.Button mButton2;

    private android.widget.Button mButton3;

    public BaselineAlignmentCenterGravityTest() {
        super("com.android.frameworks.coretests", android.widget.layout.linear.BaselineAlignmentCenterGravity.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        final android.app.Activity activity = getActivity();
        mButton1 = ((android.widget.Button) (activity.findViewById(R.id.button1)));
        mButton2 = ((android.widget.Button) (activity.findViewById(R.id.button2)));
        mButton3 = ((android.widget.Button) (activity.findViewById(R.id.button3)));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mButton1);
        assertNotNull(mButton2);
        assertNotNull(mButton3);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testChildrenAligned() throws java.lang.Exception {
        final android.view.View parent = ((android.view.View) (mButton1.getParent()));
        android.test.ViewAsserts.assertTopAligned(mButton1, parent);
        android.test.ViewAsserts.assertTopAligned(mButton2, parent);
        android.test.ViewAsserts.assertTopAligned(mButton3, parent);
        android.test.ViewAsserts.assertBottomAligned(mButton1, parent);
        android.test.ViewAsserts.assertBottomAligned(mButton2, parent);
        android.test.ViewAsserts.assertBottomAligned(mButton3, parent);
        android.test.ViewAsserts.assertTopAligned(mButton1, mButton2);
        android.test.ViewAsserts.assertTopAligned(mButton2, mButton3);
        android.test.ViewAsserts.assertBottomAligned(mButton1, mButton2);
        android.test.ViewAsserts.assertBottomAligned(mButton2, mButton3);
    }
}

