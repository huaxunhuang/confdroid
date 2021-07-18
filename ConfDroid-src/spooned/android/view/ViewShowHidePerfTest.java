/**
 * Copyright (C) 2016 The Android Open Source Project
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


@org.junit.runner.RunWith(org.junit.runners.Parameterized.class)
@androidx.test.filters.LargeTest
public class ViewShowHidePerfTest {
    @org.junit.Rule
    public androidx.test.rule.ActivityTestRule mActivityRule = new androidx.test.rule.ActivityTestRule(android.perftests.utils.StubActivity.class);

    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    public android.content.Context getContext() {
        return androidx.test.InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    static abstract class SubTreeFactory {
        java.lang.String mName;

        SubTreeFactory(java.lang.String name) {
            mName = name;
        }

        abstract android.view.View create(android.content.Context context, int depth);

        @java.lang.Override
        public java.lang.String toString() {
            return mName;
        }
    }

    private static android.view.ViewShowHidePerfTest.SubTreeFactory[] sSubTreeFactories = new android.view.ViewShowHidePerfTest.SubTreeFactory[]{ new android.view.ViewShowHidePerfTest.SubTreeFactory("NestedLinearLayoutTree") {
        private int mColorToggle = 0;

        private void createNestedLinearLayoutTree(android.content.Context context, android.widget.LinearLayout parent, int remainingDepth) {
            if (remainingDepth <= 0) {
                mColorToggle = (mColorToggle + 1) % 4;
                parent.setBackgroundColor(mColorToggle < 2 ? android.graphics.Color.RED : android.graphics.Color.BLUE);
                return;
            }
            boolean vertical = (remainingDepth % 2) == 0;
            parent.setOrientation(vertical ? android.widget.LinearLayout.VERTICAL : android.widget.LinearLayout.HORIZONTAL);
            for (int i = 0; i < 2; i++) {
                android.widget.LinearLayout child = new android.widget.LinearLayout(context);
                // vertical: match parent in x axis, horizontal: y axis.
                parent.addView(child, new android.widget.LinearLayout.LayoutParams(vertical ? android.view.ViewGroup.LayoutParams.MATCH_PARENT : 0, vertical ? 0 : android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1.0F));
                createNestedLinearLayoutTree(context, child, remainingDepth - 1);
            }
        }

        @java.lang.Override
        public android.view.View create(android.content.Context context, int depth) {
            android.widget.LinearLayout root = new android.widget.LinearLayout(context);
            createNestedLinearLayoutTree(context, root, depth - 1);
            return root;
        }
    }, new android.view.ViewShowHidePerfTest.SubTreeFactory("ImageViewList") {
        @java.lang.Override
        public android.view.View create(android.content.Context context, int depth) {
            android.widget.LinearLayout root = new android.widget.LinearLayout(context);
            root.setOrientation(android.widget.LinearLayout.HORIZONTAL);
            int childCount = ((int) (java.lang.Math.pow(2, depth)));
            for (int i = 0; i < childCount; i++) {
                android.widget.ImageView imageView = new android.widget.ImageView(context);
                root.addView(imageView, new android.widget.LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1.0F));
                imageView.setImageDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.RED));
            }
            return root;
        }
    } };

    @org.junit.runners.Parameterized.Parameters(name = "Factory:{0},depth:{1}")
    public static java.lang.Iterable<java.lang.Object[]> params() {
        java.util.List<java.lang.Object[]> params = new java.util.ArrayList<>();
        for (int depth : new int[]{ 6 }) {
            for (android.view.ViewShowHidePerfTest.SubTreeFactory subTreeFactory : android.view.ViewShowHidePerfTest.sSubTreeFactories) {
                params.add(new java.lang.Object[]{ subTreeFactory, depth });
            }
        }
        return params;
    }

    private final android.view.View mChild;

    public ViewShowHidePerfTest(android.view.ViewShowHidePerfTest.SubTreeFactory subTreeFactory, int depth) {
        mChild = subTreeFactory.create(getContext(), depth);
    }

    interface TestCallback {
        void run(android.perftests.utils.BenchmarkState state, int width, int height, android.view.ViewGroup parent, android.view.View child);
    }

    private void testParentWithChild(android.view.ViewShowHidePerfTest.TestCallback callback) throws java.lang.Throwable {
        mActivityRule.runOnUiThread(() -> {
            final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
            android.widget.FrameLayout parent = new android.widget.FrameLayout(getContext());
            mActivityRule.getActivity().setContentView(parent);
            final int width = 1000;
            final int height = 1000;
            layout(width, height, parent);
            callback.run(state, width, height, parent, mChild);
        });
    }

    private void updateAndValidateDisplayList(android.view.View view) {
        boolean hasDisplayList = view.updateDisplayListIfDirty().hasDisplayList();
        org.junit.Assert.assertTrue(hasDisplayList);
    }

    private void layout(int width, int height, android.view.View view) {
        view.measure(android.view.View.MeasureSpec.makeMeasureSpec(width, android.view.View.MeasureSpec.EXACTLY), android.view.View.MeasureSpec.makeMeasureSpec(height, android.view.View.MeasureSpec.EXACTLY));
        view.layout(0, 0, height, width);
    }

    @org.junit.Test
    public void testRemove() throws java.lang.Throwable {
        testParentWithChild(( state, width, height, parent, child) -> {
            while (state.keepRunning()) {
                state.pauseTiming();
                updateAndValidateDisplayList(parent);// Note, done to be safe, likely not needed

                parent.addView(child);
                layout(width, height, child);
                updateAndValidateDisplayList(parent);
                state.resumeTiming();
                parent.removeAllViews();
            } 
        });
    }

    @org.junit.Test
    public void testAdd() throws java.lang.Throwable {
        testParentWithChild(( state, width, height, parent, child) -> {
            while (state.keepRunning()) {
                state.pauseTiming();
                layout(width, height, child);// Note, done to be safe, likely not needed

                updateAndValidateDisplayList(parent);// Note, done to be safe, likely not needed

                parent.removeAllViews();
                updateAndValidateDisplayList(parent);
                state.resumeTiming();
                parent.addView(child);
            } 
        });
    }

    @org.junit.Test
    public void testRecordAfterAdd() throws java.lang.Throwable {
        testParentWithChild(( state, width, height, parent, child) -> {
            while (state.keepRunning()) {
                state.pauseTiming();
                parent.removeAllViews();
                updateAndValidateDisplayList(parent);// Note, done to be safe, likely not needed

                parent.addView(child);
                layout(width, height, child);
                state.resumeTiming();
                updateAndValidateDisplayList(parent);
            } 
        });
    }

    private void testVisibility(int fromVisibility, int toVisibility) throws java.lang.Throwable {
        testParentWithChild(( state, width, height, parent, child) -> {
            parent.addView(child);
            while (state.keepRunning()) {
                state.pauseTiming();
                layout(width, height, parent);
                updateAndValidateDisplayList(parent);
                child.setVisibility(fromVisibility);
                layout(width, height, parent);
                updateAndValidateDisplayList(parent);
                state.resumeTiming();
                child.setVisibility(toVisibility);
            } 
        });
    }

    @org.junit.Test
    public void testInvisibleToVisible() throws java.lang.Throwable {
        testVisibility(android.view.View.INVISIBLE, android.view.View.VISIBLE);
    }

    @org.junit.Test
    public void testVisibleToInvisible() throws java.lang.Throwable {
        testVisibility(android.view.View.VISIBLE, android.view.View.INVISIBLE);
    }

    @org.junit.Test
    public void testGoneToVisible() throws java.lang.Throwable {
        testVisibility(android.view.View.GONE, android.view.View.VISIBLE);
    }

    @org.junit.Test
    public void testVisibleToGone() throws java.lang.Throwable {
        testVisibility(android.view.View.VISIBLE, android.view.View.GONE);
    }
}

