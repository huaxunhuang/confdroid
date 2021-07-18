/**
 * Copyright (C) 2018 The Android Open Source Project
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
public class TextViewPrecomputedTextPerfTest {
    private static final int WORD_LENGTH = 9;// Random word has 9 characters.


    private static final int WORDS_IN_LINE = 8;// Roughly, 8 words in a line.


    private static final boolean NO_STYLE_TEXT = false;

    private static final boolean STYLE_TEXT = true;

    private static android.text.TextPaint PAINT = new android.text.TextPaint();

    private static final int TEXT_WIDTH = (android.widget.TextViewPrecomputedTextPerfTest.WORDS_IN_LINE * android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH) * ((int) (android.widget.TextViewPrecomputedTextPerfTest.PAINT.getTextSize()));

    public TextViewPrecomputedTextPerfTest() {
    }

    private static class TestableTextView extends android.widget.TextView {
        public TestableTextView(android.content.Context ctx) {
            super(ctx);
        }

        public void onMeasure(int w, int h) {
            super.onMeasure(w, h);
        }

        public void onDraw(android.graphics.Canvas canvas) {
            super.onDraw(canvas);
        }
    }

    @org.junit.Rule
    public android.perftests.utils.PerfStatusReporter mPerfStatusReporter = new android.perftests.utils.PerfStatusReporter();

    private android.text.TextPerfUtils mTextUtil = new android.text.TextPerfUtils();

    @org.junit.Before
    public void setUp() {
        /* seed */
        mTextUtil.resetRandom(0);
    }

    private static android.content.Context getContext() {
        return androidx.test.InstrumentationRegistry.getTargetContext();
    }

    @org.junit.Test
    public void testNewLayout_RandomText() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.text.BoringLayout.Metrics metrics = new android.text.BoringLayout.Metrics();
        while (state.keepRunning()) {
            state.pauseTiming();
            final java.lang.CharSequence text = mTextUtil.nextRandomParagraph(android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH, android.widget.TextViewPrecomputedTextPerfTest.NO_STYLE_TEXT);
            final android.widget.TextView textView = new android.widget.TextView(android.widget.TextViewPrecomputedTextPerfTest.getContext());
            textView.setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED);
            textView.setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL);
            textView.setText(text);
            android.graphics.Canvas.freeTextLayoutCaches();
            state.resumeTiming();
            textView.makeNewLayout(android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, android.widget.TextView.UNKNOWN_BORING, android.widget.TextView.UNKNOWN_BORING, android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, false);
        } 
    }

    @org.junit.Test
    public void testNewLayout_RandomText_Selectable() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.text.BoringLayout.Metrics metrics = new android.text.BoringLayout.Metrics();
        while (state.keepRunning()) {
            state.pauseTiming();
            final java.lang.CharSequence text = mTextUtil.nextRandomParagraph(android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH, android.widget.TextViewPrecomputedTextPerfTest.NO_STYLE_TEXT);
            final android.widget.TextView textView = new android.widget.TextView(android.widget.TextViewPrecomputedTextPerfTest.getContext());
            textView.setTextIsSelectable(true);
            textView.setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED);
            textView.setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL);
            textView.setText(text);
            android.graphics.Canvas.freeTextLayoutCaches();
            state.resumeTiming();
            textView.makeNewLayout(android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, android.widget.TextView.UNKNOWN_BORING, android.widget.TextView.UNKNOWN_BORING, android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, false);
        } 
    }

    @org.junit.Test
    public void testNewLayout_PrecomputedText() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.text.BoringLayout.Metrics metrics = new android.text.BoringLayout.Metrics();
        while (state.keepRunning()) {
            state.pauseTiming();
            final android.text.PrecomputedText.Params params = new android.text.PrecomputedText.Params.Builder(android.widget.TextViewPrecomputedTextPerfTest.PAINT).setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL).setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED).build();
            final java.lang.CharSequence text = android.text.PrecomputedText.create(mTextUtil.nextRandomParagraph(android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH, android.widget.TextViewPrecomputedTextPerfTest.NO_STYLE_TEXT), params);
            final android.widget.TextView textView = new android.widget.TextView(android.widget.TextViewPrecomputedTextPerfTest.getContext());
            textView.setTextMetricsParams(params);
            textView.setText(text);
            android.graphics.Canvas.freeTextLayoutCaches();
            state.resumeTiming();
            textView.makeNewLayout(android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, android.widget.TextView.UNKNOWN_BORING, android.widget.TextView.UNKNOWN_BORING, android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, false);
        } 
    }

    @org.junit.Test
    public void testNewLayout_PrecomputedText_Selectable() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.text.BoringLayout.Metrics metrics = new android.text.BoringLayout.Metrics();
        while (state.keepRunning()) {
            state.pauseTiming();
            final android.text.PrecomputedText.Params params = new android.text.PrecomputedText.Params.Builder(android.widget.TextViewPrecomputedTextPerfTest.PAINT).setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL).setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED).build();
            final java.lang.CharSequence text = android.text.PrecomputedText.create(mTextUtil.nextRandomParagraph(android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH, android.widget.TextViewPrecomputedTextPerfTest.NO_STYLE_TEXT), params);
            final android.widget.TextView textView = new android.widget.TextView(android.widget.TextViewPrecomputedTextPerfTest.getContext());
            textView.setTextIsSelectable(true);
            textView.setTextMetricsParams(params);
            textView.setText(text);
            android.graphics.Canvas.freeTextLayoutCaches();
            state.resumeTiming();
            textView.makeNewLayout(android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, android.widget.TextView.UNKNOWN_BORING, android.widget.TextView.UNKNOWN_BORING, android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, false);
        } 
    }

    @org.junit.Test
    public void testSetText_RandomText() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.text.BoringLayout.Metrics metrics = new android.text.BoringLayout.Metrics();
        while (state.keepRunning()) {
            state.pauseTiming();
            final java.lang.CharSequence text = mTextUtil.nextRandomParagraph(android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH, android.widget.TextViewPrecomputedTextPerfTest.NO_STYLE_TEXT);
            final android.widget.TextView textView = new android.widget.TextView(android.widget.TextViewPrecomputedTextPerfTest.getContext());
            textView.setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED);
            textView.setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL);
            android.graphics.Canvas.freeTextLayoutCaches();
            state.resumeTiming();
            textView.setText(text);
        } 
    }

    @org.junit.Test
    public void testSetText_RandomText_Selectable() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.text.BoringLayout.Metrics metrics = new android.text.BoringLayout.Metrics();
        while (state.keepRunning()) {
            state.pauseTiming();
            final java.lang.CharSequence text = mTextUtil.nextRandomParagraph(android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH, android.widget.TextViewPrecomputedTextPerfTest.NO_STYLE_TEXT);
            final android.widget.TextView textView = new android.widget.TextView(android.widget.TextViewPrecomputedTextPerfTest.getContext());
            textView.setTextIsSelectable(true);
            textView.setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED);
            textView.setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL);
            android.graphics.Canvas.freeTextLayoutCaches();
            state.resumeTiming();
            textView.setText(text);
        } 
    }

    @org.junit.Test
    public void testSetText_PrecomputedText() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.text.BoringLayout.Metrics metrics = new android.text.BoringLayout.Metrics();
        while (state.keepRunning()) {
            state.pauseTiming();
            final android.text.PrecomputedText.Params params = new android.text.PrecomputedText.Params.Builder(android.widget.TextViewPrecomputedTextPerfTest.PAINT).setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL).setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED).build();
            final java.lang.CharSequence text = android.text.PrecomputedText.create(mTextUtil.nextRandomParagraph(android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH, android.widget.TextViewPrecomputedTextPerfTest.NO_STYLE_TEXT), params);
            final android.widget.TextView textView = new android.widget.TextView(android.widget.TextViewPrecomputedTextPerfTest.getContext());
            textView.setTextMetricsParams(params);
            android.graphics.Canvas.freeTextLayoutCaches();
            state.resumeTiming();
            textView.setText(text);
        } 
    }

    @org.junit.Test
    public void testSetText_PrecomputedText_Selectable() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        android.text.BoringLayout.Metrics metrics = new android.text.BoringLayout.Metrics();
        while (state.keepRunning()) {
            state.pauseTiming();
            final android.text.PrecomputedText.Params params = new android.text.PrecomputedText.Params.Builder(android.widget.TextViewPrecomputedTextPerfTest.PAINT).setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL).setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED).build();
            final java.lang.CharSequence text = android.text.PrecomputedText.create(mTextUtil.nextRandomParagraph(android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH, android.widget.TextViewPrecomputedTextPerfTest.NO_STYLE_TEXT), params);
            final android.widget.TextView textView = new android.widget.TextView(android.widget.TextViewPrecomputedTextPerfTest.getContext());
            textView.setTextIsSelectable(true);
            textView.setTextMetricsParams(params);
            android.graphics.Canvas.freeTextLayoutCaches();
            state.resumeTiming();
            textView.setText(text);
        } 
    }

    @org.junit.Test
    public void testOnMeasure_RandomText() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        int width = android.view.View.MeasureSpec.makeMeasureSpec(android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, android.view.View.MeasureSpec.AT_MOST);
        int height = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
        while (state.keepRunning()) {
            state.pauseTiming();
            final java.lang.CharSequence text = mTextUtil.nextRandomParagraph(android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH, android.widget.TextViewPrecomputedTextPerfTest.NO_STYLE_TEXT);
            final android.widget.TextViewPrecomputedTextPerfTest.TestableTextView textView = new android.widget.TextViewPrecomputedTextPerfTest.TestableTextView(android.widget.TextViewPrecomputedTextPerfTest.getContext());
            textView.setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED);
            textView.setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL);
            textView.setText(text);
            textView.nullLayouts();
            android.graphics.Canvas.freeTextLayoutCaches();
            state.resumeTiming();
            textView.onMeasure(width, height);
        } 
    }

    @org.junit.Test
    public void testOnMeasure_RandomText_Selectable() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        int width = android.view.View.MeasureSpec.makeMeasureSpec(android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, android.view.View.MeasureSpec.AT_MOST);
        int height = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
        while (state.keepRunning()) {
            state.pauseTiming();
            final java.lang.CharSequence text = mTextUtil.nextRandomParagraph(android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH, android.widget.TextViewPrecomputedTextPerfTest.NO_STYLE_TEXT);
            final android.widget.TextViewPrecomputedTextPerfTest.TestableTextView textView = new android.widget.TextViewPrecomputedTextPerfTest.TestableTextView(android.widget.TextViewPrecomputedTextPerfTest.getContext());
            textView.setTextIsSelectable(true);
            textView.setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED);
            textView.setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL);
            textView.setText(text);
            textView.nullLayouts();
            android.graphics.Canvas.freeTextLayoutCaches();
            state.resumeTiming();
            textView.onMeasure(width, height);
        } 
    }

    @org.junit.Test
    public void testOnMeasure_PrecomputedText() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        int width = android.view.View.MeasureSpec.makeMeasureSpec(android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, android.view.View.MeasureSpec.AT_MOST);
        int height = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
        while (state.keepRunning()) {
            state.pauseTiming();
            final android.text.PrecomputedText.Params params = new android.text.PrecomputedText.Params.Builder(android.widget.TextViewPrecomputedTextPerfTest.PAINT).setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL).setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED).build();
            final java.lang.CharSequence text = android.text.PrecomputedText.create(mTextUtil.nextRandomParagraph(android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH, android.widget.TextViewPrecomputedTextPerfTest.NO_STYLE_TEXT), params);
            final android.widget.TextViewPrecomputedTextPerfTest.TestableTextView textView = new android.widget.TextViewPrecomputedTextPerfTest.TestableTextView(android.widget.TextViewPrecomputedTextPerfTest.getContext());
            textView.setTextMetricsParams(params);
            textView.setText(text);
            textView.nullLayouts();
            android.graphics.Canvas.freeTextLayoutCaches();
            state.resumeTiming();
            textView.onMeasure(width, height);
        } 
    }

    @org.junit.Test
    public void testOnMeasure_PrecomputedText_Selectable() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        int width = android.view.View.MeasureSpec.makeMeasureSpec(android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, android.view.View.MeasureSpec.AT_MOST);
        int height = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
        while (state.keepRunning()) {
            state.pauseTiming();
            final android.text.PrecomputedText.Params params = new android.text.PrecomputedText.Params.Builder(android.widget.TextViewPrecomputedTextPerfTest.PAINT).setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL).setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED).build();
            final java.lang.CharSequence text = android.text.PrecomputedText.create(mTextUtil.nextRandomParagraph(android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH, android.widget.TextViewPrecomputedTextPerfTest.NO_STYLE_TEXT), params);
            final android.widget.TextViewPrecomputedTextPerfTest.TestableTextView textView = new android.widget.TextViewPrecomputedTextPerfTest.TestableTextView(android.widget.TextViewPrecomputedTextPerfTest.getContext());
            textView.setTextIsSelectable(true);
            textView.setTextMetricsParams(params);
            textView.setText(text);
            textView.nullLayouts();
            android.graphics.Canvas.freeTextLayoutCaches();
            state.resumeTiming();
            textView.onMeasure(width, height);
        } 
    }

    @org.junit.Test
    public void testOnDraw_RandomText() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        int width = android.view.View.MeasureSpec.makeMeasureSpec(android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, android.view.View.MeasureSpec.AT_MOST);
        int height = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
        final android.graphics.RenderNode node = android.graphics.RenderNode.create("benchmark", null);
        while (state.keepRunning()) {
            state.pauseTiming();
            final java.lang.CharSequence text = mTextUtil.nextRandomParagraph(android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH, android.widget.TextViewPrecomputedTextPerfTest.NO_STYLE_TEXT);
            final android.widget.TextViewPrecomputedTextPerfTest.TestableTextView textView = new android.widget.TextViewPrecomputedTextPerfTest.TestableTextView(android.widget.TextViewPrecomputedTextPerfTest.getContext());
            textView.setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED);
            textView.setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL);
            textView.setText(text);
            textView.measure(width, height);
            textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
            final android.graphics.RecordingCanvas c = node.beginRecording(textView.getMeasuredWidth(), textView.getMeasuredHeight());
            textView.nullLayouts();
            android.graphics.Canvas.freeTextLayoutCaches();
            state.resumeTiming();
            textView.onDraw(c);
            node.endRecording();
        } 
    }

    @org.junit.Test
    public void testOnDraw_RandomText_Selectable() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        int width = android.view.View.MeasureSpec.makeMeasureSpec(android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, android.view.View.MeasureSpec.AT_MOST);
        int height = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
        final android.graphics.RenderNode node = android.graphics.RenderNode.create("benchmark", null);
        while (state.keepRunning()) {
            state.pauseTiming();
            final java.lang.CharSequence text = mTextUtil.nextRandomParagraph(android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH, android.widget.TextViewPrecomputedTextPerfTest.NO_STYLE_TEXT);
            final android.widget.TextViewPrecomputedTextPerfTest.TestableTextView textView = new android.widget.TextViewPrecomputedTextPerfTest.TestableTextView(android.widget.TextViewPrecomputedTextPerfTest.getContext());
            textView.setTextIsSelectable(true);
            textView.setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED);
            textView.setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL);
            textView.setText(text);
            textView.measure(width, height);
            textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
            final android.graphics.RecordingCanvas c = node.beginRecording(textView.getMeasuredWidth(), textView.getMeasuredHeight());
            textView.nullLayouts();
            android.graphics.Canvas.freeTextLayoutCaches();
            state.resumeTiming();
            textView.onDraw(c);
            node.endRecording();
        } 
    }

    @org.junit.Test
    public void testOnDraw_PrecomputedText() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        int width = android.view.View.MeasureSpec.makeMeasureSpec(android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH, android.view.View.MeasureSpec.AT_MOST);
        int height = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
        final android.graphics.RenderNode node = android.graphics.RenderNode.create("benchmark", null);
        while (state.keepRunning()) {
            state.pauseTiming();
            final android.text.PrecomputedText.Params params = new android.text.PrecomputedText.Params.Builder(android.widget.TextViewPrecomputedTextPerfTest.PAINT).setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL).setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED).build();
            final java.lang.CharSequence text = android.text.PrecomputedText.create(mTextUtil.nextRandomParagraph(android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH, android.widget.TextViewPrecomputedTextPerfTest.NO_STYLE_TEXT), params);
            final android.widget.TextViewPrecomputedTextPerfTest.TestableTextView textView = new android.widget.TextViewPrecomputedTextPerfTest.TestableTextView(android.widget.TextViewPrecomputedTextPerfTest.getContext());
            textView.setTextMetricsParams(params);
            textView.setText(text);
            textView.measure(width, height);
            textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
            final android.graphics.RecordingCanvas c = node.beginRecording(textView.getMeasuredWidth(), textView.getMeasuredHeight());
            textView.nullLayouts();
            android.graphics.Canvas.freeTextLayoutCaches();
            state.resumeTiming();
            textView.onDraw(c);
            node.endRecording();
        } 
    }

    @org.junit.Test
    public void testOnDraw_PrecomputedText_Selectable() {
        final android.perftests.utils.BenchmarkState state = mPerfStatusReporter.getBenchmarkState();
        int width = android.view.View.MeasureSpec.makeMeasureSpec(android.view.View.MeasureSpec.AT_MOST, android.widget.TextViewPrecomputedTextPerfTest.TEXT_WIDTH);
        int height = android.view.View.MeasureSpec.makeMeasureSpec(android.view.View.MeasureSpec.UNSPECIFIED, 0);
        final android.graphics.RenderNode node = android.graphics.RenderNode.create("benchmark", null);
        while (state.keepRunning()) {
            state.pauseTiming();
            final android.text.PrecomputedText.Params params = new android.text.PrecomputedText.Params.Builder(android.widget.TextViewPrecomputedTextPerfTest.PAINT).setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL).setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED).build();
            final java.lang.CharSequence text = android.text.PrecomputedText.create(mTextUtil.nextRandomParagraph(android.widget.TextViewPrecomputedTextPerfTest.WORD_LENGTH, android.widget.TextViewPrecomputedTextPerfTest.NO_STYLE_TEXT), params);
            final android.widget.TextViewPrecomputedTextPerfTest.TestableTextView textView = new android.widget.TextViewPrecomputedTextPerfTest.TestableTextView(android.widget.TextViewPrecomputedTextPerfTest.getContext());
            textView.setTextIsSelectable(true);
            textView.setTextMetricsParams(params);
            textView.setText(text);
            textView.measure(width, height);
            textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
            final android.graphics.RecordingCanvas c = node.beginRecording(textView.getMeasuredWidth(), textView.getMeasuredHeight());
            textView.nullLayouts();
            android.graphics.Canvas.freeTextLayoutCaches();
            state.resumeTiming();
            textView.onDraw(c);
            node.endRecording();
        } 
    }
}

