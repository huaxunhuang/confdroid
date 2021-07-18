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
public class EditTextLongTextPerfTest {
    @org.junit.runners.Parameterized.Parameters(name = "{0}")
    public static java.util.Collection cases() {
        return java.util.Arrays.asList(new java.lang.Object[][]{ new java.lang.Object[]{ "10x30K", 10, 30000 }, new java.lang.Object[]{ "300x1K", 300, 1000 } });
    }

    private final java.lang.String mMetricKey;

    private final int mChars;

    private final int mLines;

    public EditTextLongTextPerfTest(java.lang.String metricKey, int chars, int lines) {
        mMetricKey = metricKey;
        mChars = chars;
        mLines = lines;
    }

    @org.junit.Rule
    public androidx.test.rule.ActivityTestRule<android.perftests.utils.StubActivity> mActivityRule = new androidx.test.rule.ActivityTestRule(android.perftests.utils.StubActivity.class);

    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    private android.widget.EditText setupEditText() {
        final android.widget.EditText editText = new android.widget.EditText(mActivityRule.getActivity());
        java.lang.String alphabet = "abcdefghijklmnopqrstuvwxyz";
        final long seed = 1234567890;
        java.util.Random r = new java.util.Random(seed);
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < mLines; i++) {
            for (int j = 0; j < mChars; j++) {
                char c = alphabet.charAt(r.nextInt(alphabet.length()));
                sb.append(c);
            }
            sb.append('\n');
        }
        final int height = 1000;
        final int width = 1000;
        editText.setHeight(height);
        editText.setWidth(width);
        editText.setLayoutParams(new android.view.ViewGroup.LayoutParams(width, height));
        android.app.Activity activity = mActivityRule.getActivity();
        activity.setContentView(editText);
        editText.setText(sb.toString(), android.widget.TextView.BufferType.EDITABLE);
        editText.invalidate();
        editText.measure(android.view.View.MeasureSpec.makeMeasureSpec(width, android.view.View.MeasureSpec.EXACTLY), android.view.View.MeasureSpec.makeMeasureSpec(height, android.view.View.MeasureSpec.EXACTLY));
        editText.layout(0, 0, height, width);
        return editText;
    }

    @org.junit.Test
    public void testEditText() throws java.lang.Throwable {
        mActivityRule.runOnUiThread(() -> {
            android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
            final android.widget.EditText editText = setupEditText();
            final android.view.KeyEvent keyEvent = new android.view.KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER);
            final int steps = 100;
            while (state.keepRunning()) {
                for (int i = 0; i < steps; i++) {
                    int offset = (editText.getText().length() * i) / steps;
                    editText.setSelection(offset);
                    editText.bringPointIntoView(offset);
                    editText.onKeyDown(keyEvent.getKeyCode(), keyEvent);
                    editText.updateDisplayListIfDirty();
                }
            } 
        });
    }
}

