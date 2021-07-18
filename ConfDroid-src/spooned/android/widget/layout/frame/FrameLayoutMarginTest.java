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


public class FrameLayoutMarginTest extends android.test.ActivityInstrumentationTestCase<android.widget.layout.frame.FrameLayoutMargin> {
    private android.view.View mLeftView;

    private android.view.View mRightView;

    private android.view.View mTopView;

    private android.view.View mBottomView;

    private android.view.View mParent;

    public FrameLayoutMarginTest() {
        super("com.android.frameworks.coretests", android.widget.layout.frame.FrameLayoutMargin.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        final android.app.Activity activity = getActivity();
        mParent = activity.findViewById(R.id.parent);
        mLeftView = activity.findViewById(R.id.left);
        mRightView = activity.findViewById(R.id.right);
        mTopView = activity.findViewById(R.id.top);
        mBottomView = activity.findViewById(R.id.bottom);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mParent);
        assertNotNull(mLeftView);
        assertNotNull(mRightView);
        assertNotNull(mTopView);
        assertNotNull(mBottomView);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testLeftMarginAligned() throws java.lang.Exception {
        android.test.ViewAsserts.assertLeftAligned(mParent, mLeftView, ((android.view.ViewGroup.MarginLayoutParams) (mLeftView.getLayoutParams())).leftMargin);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testRightMarginAligned() throws java.lang.Exception {
        android.test.ViewAsserts.assertRightAligned(mParent, mRightView, ((android.view.ViewGroup.MarginLayoutParams) (mRightView.getLayoutParams())).rightMargin);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testTopMarginAligned() throws java.lang.Exception {
        android.test.ViewAsserts.assertTopAligned(mParent, mTopView, ((android.view.ViewGroup.MarginLayoutParams) (mTopView.getLayoutParams())).topMargin);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testBottomMarginAligned() throws java.lang.Exception {
        android.test.ViewAsserts.assertBottomAligned(mParent, mBottomView, ((android.view.ViewGroup.MarginLayoutParams) (mBottomView.getLayoutParams())).bottomMargin);
    }
}

