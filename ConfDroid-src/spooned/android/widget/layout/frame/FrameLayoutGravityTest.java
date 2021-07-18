/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.widget.layout.frame;


public class FrameLayoutGravityTest extends android.test.ActivityInstrumentationTestCase<android.widget.layout.frame.FrameLayoutGravity> {
    private android.view.View mLeftView;

    private android.view.View mRightView;

    private android.view.View mCenterHorizontalView;

    private android.view.View mLeftCenterVerticalView;

    private android.view.View mRighCenterVerticalView;

    private android.view.View mCenterView;

    private android.view.View mLeftBottomView;

    private android.view.View mRightBottomView;

    private android.view.View mCenterHorizontalBottomView;

    private android.view.View mParent;

    public FrameLayoutGravityTest() {
        super("com.android.frameworks.coretests", android.widget.layout.frame.FrameLayoutGravity.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        final android.app.Activity activity = getActivity();
        mParent = activity.findViewById(R.id.parent);
        mLeftView = activity.findViewById(R.id.left);
        mRightView = activity.findViewById(R.id.right);
        mCenterHorizontalView = activity.findViewById(R.id.center_horizontal);
        mLeftCenterVerticalView = activity.findViewById(R.id.left_center_vertical);
        mRighCenterVerticalView = activity.findViewById(R.id.right_center_vertical);
        mCenterView = activity.findViewById(R.id.center);
        mLeftBottomView = activity.findViewById(R.id.left_bottom);
        mRightBottomView = activity.findViewById(R.id.right_bottom);
        mCenterHorizontalBottomView = activity.findViewById(R.id.center_horizontal_bottom);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mParent);
        assertNotNull(mLeftView);
        assertNotNull(mRightView);
        assertNotNull(mCenterHorizontalView);
        assertNotNull(mLeftCenterVerticalView);
        assertNotNull(mRighCenterVerticalView);
        assertNotNull(mCenterView);
        assertNotNull(mLeftBottomView);
        assertNotNull(mRightBottomView);
        assertNotNull(mCenterHorizontalBottomView);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testLeftTopAligned() throws java.lang.Exception {
        android.test.ViewAsserts.assertLeftAligned(mParent, mLeftView);
        android.test.ViewAsserts.assertTopAligned(mParent, mLeftView);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testRightTopAligned() throws java.lang.Exception {
        android.test.ViewAsserts.assertRightAligned(mParent, mRightView);
        android.test.ViewAsserts.assertTopAligned(mParent, mRightView);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testCenterHorizontalTopAligned() throws java.lang.Exception {
        android.test.ViewAsserts.assertHorizontalCenterAligned(mParent, mCenterHorizontalView);
        android.test.ViewAsserts.assertTopAligned(mParent, mCenterHorizontalView);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testLeftCenterVerticalAligned() throws java.lang.Exception {
        android.test.ViewAsserts.assertLeftAligned(mParent, mLeftCenterVerticalView);
        android.test.ViewAsserts.assertVerticalCenterAligned(mParent, mLeftCenterVerticalView);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testRightCenterVerticalAligned() throws java.lang.Exception {
        android.test.ViewAsserts.assertRightAligned(mParent, mRighCenterVerticalView);
        android.test.ViewAsserts.assertVerticalCenterAligned(mParent, mRighCenterVerticalView);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testCenterAligned() throws java.lang.Exception {
        android.test.ViewAsserts.assertHorizontalCenterAligned(mParent, mCenterView);
        android.test.ViewAsserts.assertVerticalCenterAligned(mParent, mCenterView);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testLeftBottomAligned() throws java.lang.Exception {
        android.test.ViewAsserts.assertLeftAligned(mParent, mLeftBottomView);
        android.test.ViewAsserts.assertBottomAligned(mParent, mLeftBottomView);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testRightBottomAligned() throws java.lang.Exception {
        android.test.ViewAsserts.assertRightAligned(mParent, mRightBottomView);
        android.test.ViewAsserts.assertBottomAligned(mParent, mRightBottomView);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testCenterHorizontalBottomAligned() throws java.lang.Exception {
        android.test.ViewAsserts.assertHorizontalCenterAligned(mParent, mCenterHorizontalBottomView);
        android.test.ViewAsserts.assertBottomAligned(mParent, mCenterHorizontalBottomView);
    }
}

