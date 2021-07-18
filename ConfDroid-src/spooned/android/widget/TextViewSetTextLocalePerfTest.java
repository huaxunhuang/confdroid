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
public class TextViewSetTextLocalePerfTest {
    @org.junit.runners.Parameterized.Parameters(name = "{0}")
    public static java.util.Collection locales() {
        return java.util.Arrays.asList(new java.lang.Object[][]{ new java.lang.Object[]{ "SameLocale", "en-US", "en-US" }, new java.lang.Object[]{ "DifferentLocale", "en-US", "ja-JP" } });
    }

    private java.lang.String mMetricKey;

    private java.util.Locale mFirstLocale;

    private java.util.Locale mSecondLocale;

    public TextViewSetTextLocalePerfTest(java.lang.String metricKey, java.lang.String firstLocale, java.lang.String secondLocale) {
        mMetricKey = metricKey;
        mFirstLocale = java.util.Locale.forLanguageTag(firstLocale);
        mSecondLocale = java.util.Locale.forLanguageTag(secondLocale);
    }

    @org.junit.Rule
    public androidx.test.rule.ActivityTestRule<android.perftests.utils.StubActivity> mActivityRule = new androidx.test.rule.ActivityTestRule(android.perftests.utils.StubActivity.class);

    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.Test
    public void testSetTextLocale() {
        android.widget.TextView textView = new android.widget.TextView(mActivityRule.getActivity());
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        while (state.keepRunning()) {
            textView.setTextLocale(mFirstLocale);
            textView.setTextLocale(mSecondLocale);
        } 
    }
}

