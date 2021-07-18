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
 * limitations under the License.
 */
package android.graphics.text;


/**
 * Provides automatic line breaking for a <em>single</em> paragraph.
 *
 * <p>
 * <pre>
 * <code>
 * Paint paint = new Paint();
 * Paint bigPaint = new Paint();
 * bigPaint.setTextSize(paint.getTextSize() * 2.0);
 * String text = "Hello, Android.";
 *
 * // Prepare the measured text
 * MeasuredText mt = new MeasuredText.Builder(text.toCharArray())
 *     .appendStyleRun(paint, 7, false)  // Use paint for "Hello, "
 *     .appednStyleRun(bigPaint, 8, false)  // Use bigPaint for "Hello, "
 *     .build();
 *
 * LineBreaker lb = new LineBreaker.Builder()
 *     // Use simple line breaker
 *     .setBreakStrategy(LineBreaker.BREAK_STRATEGY_SIMPLE)
 *     // Do not add hyphenation.
 *     .setHyphenationFrequency(LineBreaker.HYPHENATION_FREQUENCY_NONE)
 *     // Build the LineBreaker
 *     .build();
 *
 * ParagraphConstraints c = new ParagraphConstraints();
 * c.setWidth(240);  // Set the line wieth as 1024px
 *
 * // Do the line breaking
 * Result r = lb.computeLineBreaks(mt, c, 0);
 *
 * // Compute the total height of the text.
 * float totalHeight = 0;
 * for (int i = 0; i < r.getLineCount(); ++i) {  // iterate over the lines
 *    totalHeight += r.getLineDescent(i) - r.getLineAscent(i);
 * }
 *
 * // Draw text to the canvas
 * Bitmap bmp = Bitmap.createBitmap(240, totalHeight, Bitmap.Config.ARGB_8888);
 * Canvas c = new Canvas(bmp);
 * float yOffset = 0f;
 * int prevOffset = 0;
 * for (int i = 0; i < r.getLineCount(); ++i) {  // iterate over the lines
 *     int nextOffset = r.getLineBreakOffset(i);
 *     c.drawText(text, prevOffset, nextOffset, 0f, yOffset, paint);
 *
 *     prevOffset = nextOffset;
 *     yOffset += r.getLineDescent(i) - r.getLineAscent(i);
 * }
 * </code>
 * </pre>
 * </p>
 */
public class LineBreaker {
    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(prefix = { "BREAK_STRATEGY_" }, value = { android.graphics.text.LineBreaker.BREAK_STRATEGY_SIMPLE, android.graphics.text.LineBreaker.BREAK_STRATEGY_HIGH_QUALITY, android.graphics.text.LineBreaker.BREAK_STRATEGY_BALANCED })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface BreakStrategy {}

    /**
     * Value for break strategy indicating simple line breaking.
     *
     * The line breaker puts words to the line as much as possible and breaks line if no more words
     * can fit into the same line. Automatic hyphens are only added when a line has a single word
     * and that word is longer than line width. This is the fastest break strategy and ideal for
     * editor.
     */
    public static final int BREAK_STRATEGY_SIMPLE = 0;

    /**
     * Value for break strategy indicating high quality line breaking.
     *
     * With this option line breaker does whole-paragraph optimization for more readable text, and
     * also applies automatic hyphenation when required.
     */
    public static final int BREAK_STRATEGY_HIGH_QUALITY = 1;

    /**
     * Value for break strategy indicating balanced line breaking.
     *
     * The line breaker does whole-paragraph optimization for making all lines similar length, and
     * also applies automatic hyphenation when required. This break strategy is good for small
     * screen devices such as watch screens.
     */
    public static final int BREAK_STRATEGY_BALANCED = 2;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(prefix = { "HYPHENATION_FREQUENCY_" }, value = { android.graphics.text.LineBreaker.HYPHENATION_FREQUENCY_NORMAL, android.graphics.text.LineBreaker.HYPHENATION_FREQUENCY_FULL, android.graphics.text.LineBreaker.HYPHENATION_FREQUENCY_NONE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface HyphenationFrequency {}

    /**
     * Value for hyphenation frequency indicating no automatic hyphenation.
     *
     * Using this option disables auto hyphenation which results in better text layout performance.
     * A word may be broken without hyphens when a line has a single word and that word is longer
     * than line width. Soft hyphens are ignored and will not be used as suggestions for potential
     * line breaks.
     */
    public static final int HYPHENATION_FREQUENCY_NONE = 0;

    /**
     * Value for hyphenation frequency indicating a light amount of automatic hyphenation.
     *
     * This hyphenation frequency is useful for informal cases, such as short sentences or chat
     * messages.
     */
    public static final int HYPHENATION_FREQUENCY_NORMAL = 1;

    /**
     * Value for hyphenation frequency indicating the full amount of automatic hyphenation.
     *
     * This hyphenation frequency is useful for running text and where it's important to put the
     * maximum amount of text in a screen with limited space.
     */
    public static final int HYPHENATION_FREQUENCY_FULL = 2;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(prefix = { "JUSTIFICATION_MODE_" }, value = { android.graphics.text.LineBreaker.JUSTIFICATION_MODE_NONE, android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface JustificationMode {}

    /**
     * Value for justification mode indicating no justification.
     */
    public static final int JUSTIFICATION_MODE_NONE = 0;

    /**
     * Value for justification mode indicating the text is justified by stretching word spacing.
     */
    public static final int JUSTIFICATION_MODE_INTER_WORD = 1;

    /**
     * Helper class for creating a {@link LineBreaker}.
     */
    public static final class Builder {
        @android.graphics.text.LineBreaker.BreakStrategy
        private int mBreakStrategy = android.graphics.text.LineBreaker.BREAK_STRATEGY_SIMPLE;

        @android.graphics.text.LineBreaker.HyphenationFrequency
        private int mHyphenationFrequency = android.graphics.text.LineBreaker.HYPHENATION_FREQUENCY_NONE;

        @android.graphics.text.LineBreaker.JustificationMode
        private int mJustificationMode = android.graphics.text.LineBreaker.JUSTIFICATION_MODE_NONE;

        @android.annotation.Nullable
        private int[] mIndents = null;

        /**
         * Set break strategy.
         *
         * You can change the line breaking behavior by setting break strategy. The default value is
         * {@link #BREAK_STRATEGY_SIMPLE}.
         */
        @android.annotation.NonNull
        public android.graphics.text.LineBreaker.Builder setBreakStrategy(@android.graphics.text.LineBreaker.BreakStrategy
        int breakStrategy) {
            mBreakStrategy = breakStrategy;
            return this;
        }

        /**
         * Set hyphenation frequency.
         *
         * You can change the amount of automatic hyphenation used. The default value is
         * {@link #HYPHENATION_FREQUENCY_NONE}.
         */
        @android.annotation.NonNull
        public android.graphics.text.LineBreaker.Builder setHyphenationFrequency(@android.graphics.text.LineBreaker.HyphenationFrequency
        int hyphenationFrequency) {
            mHyphenationFrequency = hyphenationFrequency;
            return this;
        }

        /**
         * Set whether the text is justified.
         *
         * By setting {@link #JUSTIFICATION_MODE_INTER_WORD}, the line breaker will change the
         * internal parameters for justification.
         * The default value is {@link #JUSTIFICATION_MODE_NONE}
         */
        @android.annotation.NonNull
        public android.graphics.text.LineBreaker.Builder setJustificationMode(@android.graphics.text.LineBreaker.JustificationMode
        int justificationMode) {
            mJustificationMode = justificationMode;
            return this;
        }

        /**
         * Set indents.
         *
         * The supplied array provides the total amount of indentation per line, in pixel. This
         * amount is the sum of both left and right indentations. For lines past the last element in
         * the array, the indentation amount of the last element is used.
         */
        @android.annotation.NonNull
        public android.graphics.text.LineBreaker.Builder setIndents(@android.annotation.Nullable
        int[] indents) {
            mIndents = indents;
            return this;
        }

        /**
         * Build a new LineBreaker with given parameters.
         *
         * You can reuse the Builder instance even after calling this method.
         */
        @android.annotation.NonNull
        public android.graphics.text.LineBreaker build() {
            return new android.graphics.text.LineBreaker(mBreakStrategy, mHyphenationFrequency, mJustificationMode, mIndents);
        }
    }

    /**
     * Line breaking constraints for single paragraph.
     */
    public static class ParagraphConstraints {
        @android.annotation.FloatRange(from = 0.0F)
        private float mWidth = 0;

        @android.annotation.FloatRange(from = 0.0F)
        private float mFirstWidth = 0;

        @android.annotation.IntRange(from = 0)
        private int mFirstWidthLineCount = 0;

        @android.annotation.Nullable
        private float[] mVariableTabStops = null;

        @android.annotation.FloatRange(from = 0)
        private float mDefaultTabStop = 0;

        public ParagraphConstraints() {
        }

        /**
         * Set width for this paragraph.
         *
         * @see #getWidth()
         */
        public void setWidth(@android.annotation.Px
        @android.annotation.FloatRange(from = 0.0F)
        float width) {
            mWidth = width;
        }

        /**
         * Set indent for this paragraph.
         *
         * @param firstWidth
         * 		the line width of the starting of the paragraph
         * @param firstWidthLineCount
         * 		the number of lines that applies the firstWidth
         * @see #getFirstWidth()
         * @see #getFirstWidthLineCount()
         */
        public void setIndent(@android.annotation.Px
        @android.annotation.FloatRange(from = 0.0F)
        float firstWidth, @android.annotation.Px
        @android.annotation.IntRange(from = 0)
        int firstWidthLineCount) {
            mFirstWidth = firstWidth;
            mFirstWidthLineCount = firstWidthLineCount;
        }

        /**
         * Set tab stops for this paragraph.
         *
         * @param tabStops
         * 		the array of pixels of tap stopping position
         * @param defaultTabStop
         * 		pixels of the default tab stopping position
         * @see #getTabStops()
         * @see #getDefaultTabStop()
         */
        public void setTabStops(@android.annotation.Nullable
        float[] tabStops, @android.annotation.Px
        @android.annotation.FloatRange(from = 0)
        float defaultTabStop) {
            mVariableTabStops = tabStops;
            mDefaultTabStop = defaultTabStop;
        }

        /**
         * Return the width for this paragraph in pixels.
         *
         * @see #setWidth(float)
         */
        @android.annotation.Px
        @android.annotation.FloatRange(from = 0.0F)
        public float getWidth() {
            return mWidth;
        }

        /**
         * Return the first line's width for this paragraph in pixel.
         *
         * @see #setIndent(float, int)
         */
        @android.annotation.Px
        @android.annotation.FloatRange(from = 0.0F)
        public float getFirstWidth() {
            return mFirstWidth;
        }

        /**
         * Return the number of lines to apply the first line's width.
         *
         * @see #setIndent(float, int)
         */
        @android.annotation.Px
        @android.annotation.IntRange(from = 0)
        public int getFirstWidthLineCount() {
            return mFirstWidthLineCount;
        }

        /**
         * Returns the array of tab stops in pixels.
         *
         * @see #setTabStops(float[], int)
         */
        @android.annotation.Nullable
        public float[] getTabStops() {
            return mVariableTabStops;
        }

        /**
         * Returns the default tab stops in pixels.
         *
         * @see #setTabStop(float[], int)
         */
        @android.annotation.Px
        @android.annotation.FloatRange(from = 0)
        public float getDefaultTabStop() {
            return mDefaultTabStop;
        }
    }

    /**
     * Holds the result of the {@link LineBreaker#computeLineBreaks line breaking algorithm}.
     *
     * @see LineBreaker#computeLineBreaks
     */
    public static class Result {
        // Following two contstant must be synced with minikin's line breaker.
        // TODO(nona): Remove these constatns by introducing native methods.
        private static final int TAB_MASK = 0x20000000;

        private static final int HYPHEN_MASK = 0xff;

        private static final int START_HYPHEN_MASK = 0x18;// 0b11000


        private static final int END_HYPHEN_MASK = 0x7;// 0b00111


        private static final int START_HYPHEN_BITS_SHIFT = 3;

        private static final libcore.util.NativeAllocationRegistry sRegistry = libcore.util.NativeAllocationRegistry.createMalloced(android.graphics.text.LineBreaker.Result.class.getClassLoader(), android.graphics.text.LineBreaker.nGetReleaseResultFunc());

        private final long mPtr;

        private Result(long ptr) {
            mPtr = ptr;
            android.graphics.text.LineBreaker.Result.sRegistry.registerNativeAllocation(this, mPtr);
        }

        /**
         * Returns the number of lines in the paragraph.
         *
         * @return number of lines
         */
        @android.annotation.IntRange(from = 0)
        public int getLineCount() {
            return android.graphics.text.LineBreaker.nGetLineCount(mPtr);
        }

        /**
         * Returns character offset of the break for a given line.
         *
         * @param lineIndex
         * 		an index of the line.
         * @return the break offset.
         */
        @android.annotation.IntRange(from = 0)
        public int getLineBreakOffset(@android.annotation.IntRange(from = 0)
        int lineIndex) {
            return android.graphics.text.LineBreaker.nGetLineBreakOffset(mPtr, lineIndex);
        }

        /**
         * Returns width of a given line in pixels.
         *
         * @param lineIndex
         * 		an index of the line.
         * @return width of the line in pixels
         */
        @android.annotation.Px
        public float getLineWidth(@android.annotation.IntRange(from = 0)
        int lineIndex) {
            return android.graphics.text.LineBreaker.nGetLineWidth(mPtr, lineIndex);
        }

        /**
         * Returns font ascent of the line in pixels.
         *
         * @param lineIndex
         * 		an index of the line.
         * @return an entier font ascent of the line in pixels.
         */
        @android.annotation.Px
        public float getLineAscent(@android.annotation.IntRange(from = 0)
        int lineIndex) {
            return android.graphics.text.LineBreaker.nGetLineAscent(mPtr, lineIndex);
        }

        /**
         * Returns font descent of the line in pixels.
         *
         * @param lineIndex
         * 		an index of the line.
         * @return an entier font descent of the line in pixels.
         */
        @android.annotation.Px
        public float getLineDescent(@android.annotation.IntRange(from = 0)
        int lineIndex) {
            return android.graphics.text.LineBreaker.nGetLineDescent(mPtr, lineIndex);
        }

        /**
         * Returns true if the line has a TAB character.
         *
         * @param lineIndex
         * 		an index of the line.
         * @return true if the line has a TAB character
         */
        public boolean hasLineTab(int lineIndex) {
            return (android.graphics.text.LineBreaker.nGetLineFlag(mPtr, lineIndex) & android.graphics.text.LineBreaker.Result.TAB_MASK) != 0;
        }

        /**
         * Returns a start hyphen edit for the line.
         *
         * @param lineIndex
         * 		an index of the line.
         * @return a start hyphen edit for the line.
         * @see android.graphics.Paint#setStartHyphenEdit
         * @see android.graphics.Paint#getStartHyphenEdit
         */
        public int getStartLineHyphenEdit(int lineIndex) {
            return (android.graphics.text.LineBreaker.nGetLineFlag(mPtr, lineIndex) & android.graphics.text.LineBreaker.Result.START_HYPHEN_MASK) >> android.graphics.text.LineBreaker.Result.START_HYPHEN_BITS_SHIFT;
        }

        /**
         * Returns an end hyphen edit for the line.
         *
         * @param lineIndex
         * 		an index of the line.
         * @return an end hyphen edit for the line.
         * @see android.graphics.Paint#setEndHyphenEdit
         * @see android.graphics.Paint#getEndHyphenEdit
         */
        public int getEndLineHyphenEdit(int lineIndex) {
            return android.graphics.text.LineBreaker.nGetLineFlag(mPtr, lineIndex) & android.graphics.text.LineBreaker.Result.END_HYPHEN_MASK;
        }
    }

    private static final libcore.util.NativeAllocationRegistry sRegistry = libcore.util.NativeAllocationRegistry.createMalloced(android.graphics.text.LineBreaker.class.getClassLoader(), android.graphics.text.LineBreaker.nGetReleaseFunc());

    private final long mNativePtr;

    /**
     * Use Builder instead.
     */
    private LineBreaker(@android.graphics.text.LineBreaker.BreakStrategy
    int breakStrategy, @android.graphics.text.LineBreaker.HyphenationFrequency
    int hyphenationFrequency, @android.graphics.text.LineBreaker.JustificationMode
    int justify, @android.annotation.Nullable
    int[] indents) {
        mNativePtr = android.graphics.text.LineBreaker.nInit(breakStrategy, hyphenationFrequency, justify == android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD, indents);
        android.graphics.text.LineBreaker.sRegistry.registerNativeAllocation(this, mNativePtr);
    }

    /**
     * Break paragraph into lines.
     *
     * The result is filled to out param.
     *
     * @param measuredPara
     * 		a result of the text measurement
     * @param constraints
     * 		for a single paragraph
     * @param lineNumber
     * 		a line number of this paragraph
     */
    @android.annotation.NonNull
    public android.graphics.text.LineBreaker.Result computeLineBreaks(@android.annotation.NonNull
    android.graphics.text.MeasuredText measuredPara, @android.annotation.NonNull
    android.graphics.text.LineBreaker.ParagraphConstraints constraints, @android.annotation.IntRange(from = 0)
    int lineNumber) {
        return new android.graphics.text.LineBreaker.Result(// Inputs
        android.graphics.text.LineBreaker.nComputeLineBreaks(mNativePtr, measuredPara.getChars(), measuredPara.getNativePtr(), measuredPara.getChars().length, constraints.mFirstWidth, constraints.mFirstWidthLineCount, constraints.mWidth, constraints.mVariableTabStops, constraints.mDefaultTabStop, lineNumber));
    }

    @dalvik.annotation.optimization.FastNative
    private static native long nInit(@android.graphics.text.LineBreaker.BreakStrategy
    int breakStrategy, @android.graphics.text.LineBreaker.HyphenationFrequency
    int hyphenationFrequency, boolean isJustified, @android.annotation.Nullable
    int[] indents);

    @dalvik.annotation.optimization.CriticalNative
    private static native long nGetReleaseFunc();

    // populates LineBreaks and returns the number of breaks found
    // 
    // the arrays inside the LineBreaks objects are passed in as well
    // to reduce the number of JNI calls in the common case where the
    // arrays do not have to be resized
    // The individual character widths will be returned in charWidths. The length of
    // charWidths must be at least the length of the text.
    private static native long nComputeLineBreaks(/* non zero */
    long nativePtr, // Inputs
    @android.annotation.NonNull
    char[] text, /* Non Zero */
    long measuredTextPtr, @android.annotation.IntRange(from = 0)
    int length, @android.annotation.FloatRange(from = 0.0F)
    float firstWidth, @android.annotation.IntRange(from = 0)
    int firstWidthLineCount, @android.annotation.FloatRange(from = 0.0F)
    float restWidth, @android.annotation.Nullable
    float[] variableTabStops, float defaultTabStop, @android.annotation.IntRange(from = 0)
    int indentsOffset);

    // Result accessors
    @dalvik.annotation.optimization.CriticalNative
    private static native int nGetLineCount(long ptr);

    @dalvik.annotation.optimization.CriticalNative
    private static native int nGetLineBreakOffset(long ptr, int idx);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nGetLineWidth(long ptr, int idx);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nGetLineAscent(long ptr, int idx);

    @dalvik.annotation.optimization.CriticalNative
    private static native float nGetLineDescent(long ptr, int idx);

    @dalvik.annotation.optimization.CriticalNative
    private static native int nGetLineFlag(long ptr, int idx);

    @dalvik.annotation.optimization.CriticalNative
    private static native long nGetReleaseResultFunc();
}

