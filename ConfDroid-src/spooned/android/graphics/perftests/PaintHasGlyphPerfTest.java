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
package android.graphics.perftests;


@androidx.test.filters.LargeTest
@org.junit.runner.RunWith(org.junit.runners.Parameterized.class)
public class PaintHasGlyphPerfTest {
    @org.junit.runners.Parameterized.Parameters(name = "{0}")
    public static java.util.Collection glyphStrings() {
        return java.util.Arrays.asList(new java.lang.Object[][]{ new java.lang.Object[]{ "Latin", "A" }, new java.lang.Object[]{ "Ligature", "fi" }, new java.lang.Object[]{ "SurrogatePair", "\ud83d\ude00" }// U+1F600
        // U+1F600
        // U+1F600
        , new java.lang.Object[]{ "Flags", "\ud83c\uddfa\ud83c\uddf8" }// US
        // US
        // US
        , new java.lang.Object[]{ "Ideograph_VariationSelector", "\u3402\udb40\udd00" }// U+3402 U+E0100
        // U+3402 U+E0100
        // U+3402 U+E0100
        , new java.lang.Object[]{ "Emoji_VariationSelector", "\u00a9\ufe0f" }, new java.lang.Object[]{ "EmojiSequence", // U+1F468 U+200D U+2764 U+FE0F U+200D U+1F48B U+200D U+1F468
        "\ud83d\udc68\u200d\u2764\ufe0f\u200d\ud83d\udc8b\u200d\ud83d\udc68" } });
    }

    private final java.lang.String mQuery;

    public PaintHasGlyphPerfTest(java.lang.String metricKey, java.lang.String query) {
        mQuery = query;
    }

    @org.junit.Rule
    public androidx.test.rule.ActivityTestRule<android.perftests.utils.StubActivity> mActivityRule = new androidx.test.rule.ActivityTestRule(android.perftests.utils.StubActivity.class);

    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.Test
    public void testHasGlyph() {
        android.graphics.Paint paint = new android.graphics.Paint();
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        while (state.keepRunning()) {
            paint.hasGlyph(mQuery);
        } 
    }
}

