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
 * limitations under the License
 */
package android.widget;


@androidx.test.filters.LargeTest
@org.junit.runner.RunWith(org.junit.runners.Parameterized.class)
public class TextViewAutoSizeLayoutPerfTest {
    @org.junit.runners.Parameterized.Parameters(name = "{0}")
    public static java.util.Collection layouts() {
        return java.util.Arrays.asList(new java.lang.Object[][]{ new java.lang.Object[]{ "Basic TextView - no autosize", R.layout.test_basic_textview_layout }, new java.lang.Object[]{ "Autosize TextView 5 sizes", R.layout.test_autosize_textview_5 }, new java.lang.Object[]{ "Autosize TextView 10 sizes", R.layout.test_autosize_textview_10 }, new java.lang.Object[]{ "Autosize TextView 50 sizes", R.layout.test_autosize_textview_50 }, new java.lang.Object[]{ "Autosize TextView 100 sizes", R.layout.test_autosize_textview_100 }, new java.lang.Object[]{ "Autosize TextView 300 sizes", R.layout.test_autosize_textview_300 }, new java.lang.Object[]{ "Autosize TextView 500 sizes", R.layout.test_autosize_textview_500 }, new java.lang.Object[]{ "Autosize TextView 1000 sizes", R.layout.test_autosize_textview_1000 }, new java.lang.Object[]{ "Autosize TextView 10000 sizes", R.layout.test_autosize_textview_10000 }, new java.lang.Object[]{ "Autosize TextView 100000 sizes", R.layout.test_autosize_textview_100000 } });
    }

    private int mLayoutId;

    public TextViewAutoSizeLayoutPerfTest(java.lang.String key, int layoutId) {
        mLayoutId = layoutId;
    }

    @org.junit.Rule
    public androidx.test.rule.ActivityTestRule<android.perftests.utils.StubActivity> mActivityRule = new androidx.test.rule.ActivityTestRule(android.perftests.utils.StubActivity.class);

    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.Test
    public void testConstruction() throws java.lang.Throwable {
        mActivityRule.runOnUiThread(() -> {
            assertTrue("We should be running on the main thread", android.os.Looper.getMainLooper().getThread() == java.lang.Thread.currentThread());
            assertTrue("We should be running on the main thread", android.os.Looper.myLooper() == android.os.Looper.getMainLooper());
            android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
            android.app.Activity activity = mActivityRule.getActivity();
            activity.setContentView(mLayoutId);
            while (state.keepRunning()) {
                android.widget.TextView textView = new android.widget.TextView(activity);
                // TextView#onLayout() gets called, which triggers TextView#autoSizeText()
                // which is the method we want to benchmark.
                textView.requestLayout();
            } 
        });
    }
}

