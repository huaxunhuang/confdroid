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
public class PaintMeasureTextTest {
    private static final int USE_CACHE = 0;

    private static final int DONT_USE_CACHE = 1;

    @org.junit.runners.Parameterized.Parameters(name = "{0}")
    public static java.util.Collection measureSpecs() {
        return java.util.Arrays.asList(new java.lang.Object[][]{ new java.lang.Object[]{ "alphabet_cached", android.graphics.perftests.PaintMeasureTextTest.USE_CACHE, "a" }, new java.lang.Object[]{ "alphabet_not_cached", android.graphics.perftests.PaintMeasureTextTest.DONT_USE_CACHE, "a" }, // U+4E80 is an ideograph.
        new java.lang.Object[]{ "ideograph_cached", android.graphics.perftests.PaintMeasureTextTest.USE_CACHE, "\u4e80" }, new java.lang.Object[]{ "ideograph_not_cached", android.graphics.perftests.PaintMeasureTextTest.DONT_USE_CACHE, "\u4e80" }, // U+20B9F(\uD842\uDF9F) is an ideograph.
        new java.lang.Object[]{ "surrogate_pairs_cached", android.graphics.perftests.PaintMeasureTextTest.USE_CACHE, "\ud842\udf9f" }, new java.lang.Object[]{ "surrogate_pairs_not_cached", android.graphics.perftests.PaintMeasureTextTest.DONT_USE_CACHE, "\ud842\udf9f" }, // U+303D is PART ALTERNATION MARK
        new java.lang.Object[]{ "emoji_cached", android.graphics.perftests.PaintMeasureTextTest.USE_CACHE, "\u231a" }, new java.lang.Object[]{ "emoji_not_cached", android.graphics.perftests.PaintMeasureTextTest.DONT_USE_CACHE, "\u231a" }, // U+1F368(\uD83C\uDF68) is ICE CREAM
        new java.lang.Object[]{ "emoji_surrogate_pairs_cached", android.graphics.perftests.PaintMeasureTextTest.USE_CACHE, "\ud83c\udf68" }, new java.lang.Object[]{ "emoji_surrogate_pairs_not_cached", android.graphics.perftests.PaintMeasureTextTest.DONT_USE_CACHE, "\ud83c\udf68" } });
    }

    private final java.lang.String mText;

    private final int mCacheMode;

    public PaintMeasureTextTest(java.lang.String key, int cacheMode, java.lang.String text) {
        mText = text;
        mCacheMode = cacheMode;
    }

    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    @org.junit.Test
    public void testMeasureTextPerf() {
        android.text.TextPaint paint = new android.text.TextPaint();
        android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        if (mCacheMode == android.graphics.perftests.PaintMeasureTextTest.USE_CACHE) {
            paint.measureText(mText);
        } else {
            android.graphics.Canvas.freeTextLayoutCaches();
        }
        while (state.keepRunning()) {
            if (mCacheMode == android.graphics.perftests.PaintMeasureTextTest.DONT_USE_CACHE) {
                state.pauseTiming();
                android.graphics.Canvas.freeTextLayoutCaches();
                state.resumeTiming();
            }
            paint.measureText(mText);
        } 
    }
}

