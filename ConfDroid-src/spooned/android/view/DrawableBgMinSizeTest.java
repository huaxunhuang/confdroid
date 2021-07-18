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
 * {@link DrawableBgMinSize} exercises Views to obey their background drawable's
 * minimum sizes.
 */
public class DrawableBgMinSizeTest extends android.test.ActivityInstrumentationTestCase<android.view.DrawableBgMinSize> {
    private android.widget.Button mChangeBackgroundsButton;

    private android.graphics.drawable.Drawable mBackgroundDrawable;

    private android.graphics.drawable.Drawable mBigBackgroundDrawable;

    private android.widget.TextView mTextView;

    private android.widget.LinearLayout mLinearLayout;

    private android.widget.RelativeLayout mRelativeLayout;

    private android.widget.FrameLayout mFrameLayout;

    private android.widget.AbsoluteLayout mAbsoluteLayout;

    public DrawableBgMinSizeTest() {
        super("com.android.frameworks.coretests", android.view.DrawableBgMinSize.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        final android.view.DrawableBgMinSize a = getActivity();
        mChangeBackgroundsButton = ((android.widget.Button) (a.findViewById(R.id.change_backgrounds)));
        mBackgroundDrawable = a.getResources().getDrawable(R.drawable.drawable_background);
        mBigBackgroundDrawable = a.getResources().getDrawable(R.drawable.big_drawable_background);
        mTextView = ((android.widget.TextView) (a.findViewById(R.id.text_view)));
        mLinearLayout = ((android.widget.LinearLayout) (a.findViewById(R.id.linear_layout)));
        mRelativeLayout = ((android.widget.RelativeLayout) (a.findViewById(R.id.relative_layout)));
        mFrameLayout = ((android.widget.FrameLayout) (a.findViewById(R.id.frame_layout)));
        mAbsoluteLayout = ((android.widget.AbsoluteLayout) (a.findViewById(R.id.absolute_layout)));
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testSetUpConditions() throws java.lang.Exception {
        assertNotNull(mChangeBackgroundsButton);
        assertNotNull(mBackgroundDrawable);
        assertNotNull(mBigBackgroundDrawable);
        assertNotNull(mTextView);
        assertNotNull(mLinearLayout);
        assertNotNull(mRelativeLayout);
        assertNotNull(mFrameLayout);
        assertNotNull(mAbsoluteLayout);
    }

    public void doMinimumSizeTest(android.view.View view) throws java.lang.Exception {
        assertTrue(view.getClass().getSimpleName() + " should respect the background Drawable's minimum width", view.getWidth() >= mBackgroundDrawable.getMinimumWidth());
        assertTrue(view.getClass().getSimpleName() + " should respect the background Drawable's minimum height", view.getHeight() >= mBackgroundDrawable.getMinimumHeight());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testTextViewMinimumSize() throws java.lang.Exception {
        doMinimumSizeTest(mTextView);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testLinearLayoutMinimumSize() throws java.lang.Exception {
        doMinimumSizeTest(mLinearLayout);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testRelativeLayoutMinimumSize() throws java.lang.Exception {
        doMinimumSizeTest(mRelativeLayout);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testAbsoluteLayoutMinimumSize() throws java.lang.Exception {
        doMinimumSizeTest(mAbsoluteLayout);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testFrameLayoutMinimumSize() throws java.lang.Exception {
        doMinimumSizeTest(mFrameLayout);
    }

    public void doDiffBgMinimumSizeTest(final android.view.View view) throws java.lang.Exception {
        // Change to the bigger backgrounds
        android.test.TouchUtils.tapView(this, mChangeBackgroundsButton);
        assertTrue(view.getClass().getSimpleName() + " should respect the different bigger background Drawable's minimum width", view.getWidth() >= mBigBackgroundDrawable.getMinimumWidth());
        assertTrue(view.getClass().getSimpleName() + " should respect the different bigger background Drawable's minimum height", view.getHeight() >= mBigBackgroundDrawable.getMinimumHeight());
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testTextViewDiffBgMinimumSize() throws java.lang.Exception {
        doDiffBgMinimumSizeTest(mTextView);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testLinearLayoutDiffBgMinimumSize() throws java.lang.Exception {
        doDiffBgMinimumSizeTest(mLinearLayout);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testRelativeLayoutDiffBgMinimumSize() throws java.lang.Exception {
        doDiffBgMinimumSizeTest(mRelativeLayout);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testAbsoluteLayoutDiffBgMinimumSize() throws java.lang.Exception {
        doDiffBgMinimumSizeTest(mAbsoluteLayout);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testFrameLayoutDiffBgMinimumSize() throws java.lang.Exception {
        doDiffBgMinimumSizeTest(mFrameLayout);
    }
}

