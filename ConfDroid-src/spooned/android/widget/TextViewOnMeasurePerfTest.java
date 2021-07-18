/**
 * Copyright (C) 2017 The Android Open Source Project
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
@org.junit.runner.RunWith(androidx.test.runner.AndroidJUnit4.class)
public class TextViewOnMeasurePerfTest {
    private static final java.lang.String MULTILINE_TEXT = "Lorem ipsum dolor sit amet, \n" + ((((((("consectetur adipiscing elit, \n" + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \n") + "Ut enim ad minim veniam, \n") + "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n") + "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat ") + "nulla pariatur.\n") + "Excepteur sint occaecat cupidatat non proident, \n") + "sunt in culpa qui officia deserunt mollit anim id est laborum.\n");

    private static final int VIEW_WIDTH = 1000;

    private static final int VIEW_HEIGHT = 1000;

    private static final java.lang.CharSequence COMPLEX_MULTILINE_TEXT;

    static {
        final android.text.SpannableStringBuilder ssb = new android.text.SpannableStringBuilder();
        // To emphasize, append multiline text 10 times.
        for (int i = 0; i < 10; ++i) {
            ssb.append(android.widget.TextViewOnMeasurePerfTest.MULTILINE_TEXT);
        }
        final android.content.res.ColorStateList[] COLORS = new android.content.res.ColorStateList[]{ android.content.res.ColorStateList.valueOf(0xffff0000)// RED
        , android.content.res.ColorStateList.valueOf(0xff00ff00)// GREEN
        , android.content.res.ColorStateList.valueOf(0xff0000ff)// BLUE
         };
        final int[] STYLES = new int[]{ android.graphics.Typeface.NORMAL, android.graphics.Typeface.BOLD, android.graphics.Typeface.ITALIC, android.graphics.Typeface.BOLD_ITALIC };
        final java.lang.String[] FAMILIES = new java.lang.String[]{ "sans-serif", "serif", "monospace" };
        // Append random span to text.
        final java.util.Random random = new java.util.Random(0);
        for (int pos = 0; pos < ssb.length();) {
            final android.text.style.TextAppearanceSpan span = // text size. minimum 24
            new android.text.style.TextAppearanceSpan(FAMILIES[random.nextInt(FAMILIES.length)], STYLES[random.nextInt(STYLES.length)], 24 + random.nextInt(32), COLORS[random.nextInt(COLORS.length)], COLORS[random.nextInt(COLORS.length)]);
            int spanLength = 1 + random.nextInt(9);// Up to 9 span length.

            if ((pos + spanLength) > ssb.length()) {
                spanLength = ssb.length() - pos;
            }
            ssb.setSpan(span, pos, pos + spanLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            pos += spanLength;
        }
        COMPLEX_MULTILINE_TEXT = ssb;
    }

    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.Test
    public void testMeasure_AtMost() throws java.lang.Throwable {
        final android.content.Context context = androidx.test.InstrumentationRegistry.getTargetContext();
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        final android.widget.TextView textView = new android.widget.TextView(context);
        textView.setText(android.widget.TextViewOnMeasurePerfTest.COMPLEX_MULTILINE_TEXT);
        while (state.keepRunning()) {
            // Changing locale to invalidate internal layout.
            textView.setTextLocale(java.util.Locale.UK);
            textView.setTextLocale(java.util.Locale.US);
            textView.measure(android.view.View.MeasureSpec.AT_MOST | android.widget.TextViewOnMeasurePerfTest.VIEW_WIDTH, android.view.View.MeasureSpec.AT_MOST | android.widget.TextViewOnMeasurePerfTest.VIEW_HEIGHT);
        } 
    }

    @org.junit.Test
    public void testMeasure_Exactly() throws java.lang.Throwable {
        final android.content.Context context = androidx.test.InstrumentationRegistry.getTargetContext();
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        final android.widget.TextView textView = new android.widget.TextView(context);
        textView.setText(android.widget.TextViewOnMeasurePerfTest.COMPLEX_MULTILINE_TEXT);
        while (state.keepRunning()) {
            // Changing locale to invalidate internal layout.
            textView.setTextLocale(java.util.Locale.UK);
            textView.setTextLocale(java.util.Locale.US);
            textView.measure(android.view.View.MeasureSpec.EXACTLY | android.widget.TextViewOnMeasurePerfTest.VIEW_WIDTH, android.view.View.MeasureSpec.EXACTLY | android.widget.TextViewOnMeasurePerfTest.VIEW_HEIGHT);
        } 
    }

    @org.junit.Test
    public void testMeasure_Unspecified() throws java.lang.Throwable {
        final android.content.Context context = androidx.test.InstrumentationRegistry.getTargetContext();
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        final android.widget.TextView textView = new android.widget.TextView(context);
        textView.setText(android.widget.TextViewOnMeasurePerfTest.COMPLEX_MULTILINE_TEXT);
        while (state.keepRunning()) {
            // Changing locale to invalidate internal layout.
            textView.setTextLocale(java.util.Locale.UK);
            textView.setTextLocale(java.util.Locale.US);
            textView.measure(android.view.View.MeasureSpec.UNSPECIFIED, android.view.View.MeasureSpec.UNSPECIFIED);
        } 
    }
}

