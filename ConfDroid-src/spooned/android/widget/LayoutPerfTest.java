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
package android.widget;


@androidx.test.filters.LargeTest
@org.junit.runner.RunWith(org.junit.runners.Parameterized.class)
public class LayoutPerfTest {
    @org.junit.runners.Parameterized.Parameters(name = "{0}")
    public static java.util.Collection measureSpecs() {
        return java.util.Arrays.asList(new java.lang.Object[][]{ new java.lang.Object[]{ "relative", R.layout.test_relative_layout, R.id.relative_layout_root }, new java.lang.Object[]{ "linear", R.layout.test_linear_layout, R.id.linear_layout_root }, new java.lang.Object[]{ "linear_weighted", R.layout.test_linear_layout_weighted, R.id.linear_layout_weighted_root } });
    }

    private int[] mMeasureSpecs = new int[]{ android.view.View.MeasureSpec.EXACTLY, android.view.View.MeasureSpec.AT_MOST, android.view.View.MeasureSpec.UNSPECIFIED };

    private int mLayoutId;

    private int mViewId;

    public LayoutPerfTest(java.lang.String key, int layoutId, int viewId) {
        // key is used in the final report automatically.
        mLayoutId = layoutId;
        mViewId = viewId;
    }

    @org.junit.Rule
    public androidx.test.rule.ActivityTestRule<android.perftests.utils.StubActivity> mActivityRule = new androidx.test.rule.ActivityTestRule(android.perftests.utils.StubActivity.class);

    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.Test
    @androidx.test.annotation.UiThreadTest
    public void testLayoutPerf() throws java.lang.Throwable {
        mActivityRule.runOnUiThread(() -> {
            assertTrue("We should be running on the main thread", android.os.Looper.getMainLooper().getThread() == java.lang.Thread.currentThread());
            assertTrue("We should be running on the main thread", android.os.Looper.myLooper() == android.os.Looper.getMainLooper());
            android.app.Activity activity = mActivityRule.getActivity();
            activity.setContentView(mLayoutId);
            android.view.ViewGroup viewGroup = ((android.view.ViewGroup) (activity.findViewById(mViewId)));
            List<android.view.View> allNodes = gatherViewTree(viewGroup);
            android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
            int length = mMeasureSpecs.length;
            while (state.keepRunning()) {
                for (int i = 0; i < length; i++) {
                    // The overhead of this call is ignorable, like within 1% difference.
                    requestLayoutForAllNodes(allNodes);
                    viewGroup.measure(mMeasureSpecs[i % length], mMeasureSpecs[i % length]);
                    viewGroup.layout(0, 0, viewGroup.getMeasuredWidth(), viewGroup.getMeasuredHeight());
                }
            } 
        });
    }
}

