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
public class EditTextCursorMovementPerfTest {
    private static final java.lang.String BOY = "\ud83d\udc66";// U+1F466


    private static final java.lang.String US_FLAG = "\ud83c\uddfa\ud83c\uddf8";// U+1F1FA U+1F1F8


    // U+1F469 U+200D U+1F469 U+200D U+1F467 U+200D U+1F467
    private static final java.lang.String FAMILY = "\ud83d\udc69\u200d\ud83d\udc69\u200d\ud83d\udc67\u200d\ud83d\udc67";

    @org.junit.runners.Parameterized.Parameters(name = "{0}")
    public static java.util.Collection cases() {
        return java.util.Arrays.asList(new java.lang.Object[][]{ new java.lang.Object[]{ "Latin", "aaa", 1 }, new java.lang.Object[]{ "Emoji", (android.widget.EditTextCursorMovementPerfTest.BOY + android.widget.EditTextCursorMovementPerfTest.BOY) + android.widget.EditTextCursorMovementPerfTest.BOY, 2 }, new java.lang.Object[]{ "Flags", (android.widget.EditTextCursorMovementPerfTest.US_FLAG + android.widget.EditTextCursorMovementPerfTest.US_FLAG) + android.widget.EditTextCursorMovementPerfTest.US_FLAG, 4 }, new java.lang.Object[]{ "ZwjSequence", (android.widget.EditTextCursorMovementPerfTest.FAMILY + android.widget.EditTextCursorMovementPerfTest.FAMILY) + android.widget.EditTextCursorMovementPerfTest.FAMILY, 11 } });
    }

    private final java.lang.String mMetricKey;

    private final java.lang.String mText;

    private final int mCursorPos;

    private static final android.view.KeyEvent LEFT_ARROW_KEY_EVENT = new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_DPAD_LEFT);

    private static final android.view.KeyEvent RIGHT_ARROW_KEY_EVENT = new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_DPAD_RIGHT);

    public EditTextCursorMovementPerfTest(java.lang.String metricKey, java.lang.String text, int cursorPos) {
        mMetricKey = metricKey;
        mText = text;
        mCursorPos = cursorPos;
    }

    @org.junit.Rule
    public androidx.test.rule.ActivityTestRule<android.perftests.utils.StubActivity> mActivityRule = new androidx.test.rule.ActivityTestRule(android.perftests.utils.StubActivity.class);

    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.Test
    public void testCursorMovement() {
        androidx.test.InstrumentationRegistry.getInstrumentation().runOnMainSync(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                android.widget.EditText editText = new android.widget.EditText(mActivityRule.getActivity());
                editText.setText(mText, android.widget.TextView.BufferType.EDITABLE);
                android.text.Selection.setSelection(editText.getText(), 0, 0);
                // Layout it here since the cursor movement requires layout information but it
                // happens asynchronously even if the view is attached to an Activity.
                editText.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
                editText.invalidate();
                editText.measure(android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED), android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED));
                editText.layout(0, 0, 1024, 768);
                // mText contains three grapheme clusters. Move the cursor to the 2nd grapheme
                // cluster by forwarding right arrow key event.
                editText.onKeyDown(android.widget.EditTextCursorMovementPerfTest.RIGHT_ARROW_KEY_EVENT.getKeyCode(), android.widget.EditTextCursorMovementPerfTest.RIGHT_ARROW_KEY_EVENT);
                org.junit.Assert.assertEquals(mCursorPos, android.text.Selection.getSelectionStart(editText.getText()));
                android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
                while (state.keepRunning()) {
                    editText.onKeyDown(android.widget.EditTextCursorMovementPerfTest.RIGHT_ARROW_KEY_EVENT.getKeyCode(), android.widget.EditTextCursorMovementPerfTest.RIGHT_ARROW_KEY_EVENT);
                    editText.onKeyDown(android.widget.EditTextCursorMovementPerfTest.LEFT_ARROW_KEY_EVENT.getKeyCode(), android.widget.EditTextCursorMovementPerfTest.LEFT_ARROW_KEY_EVENT);
                } 
            }
        });
    }
}

