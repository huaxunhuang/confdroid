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
 * Delegate that provides implementation for native methods in {@link android.text.StaticLayout}
 * <p/>
 * Through the layoutlib_create tool, selected methods of StaticLayout have been replaced
 * by calls to methods of the same name in this delegate class.
 */
public class LineBreaker_Delegate {
    private static final char CHAR_SPACE = 0x20;

    private static final char CHAR_TAB = 0x9;

    private static final char CHAR_NEWLINE = 0xa;

    private static final char CHAR_ZWSP = 0x200b;// Zero width space.


    // ---- Builder delegate manager ----
    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.text.LineBreaker_Delegate.Builder> sBuilderManager = new com.android.layoutlib.bridge.impl.DelegateManager(android.graphics.text.LineBreaker_Delegate.Builder.class);

    private static long sFinalizer = -1;

    // ---- Result delegate manager ----
    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.graphics.text.LineBreaker_Delegate.Result> sResultManager = new com.android.layoutlib.bridge.impl.DelegateManager(android.graphics.text.LineBreaker_Delegate.Result.class);

    private static long sResultFinalizer = -1;

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nInit(@android.text.Layout.BreakStrategy
    int breakStrategy, @android.text.Layout.HyphenationFrequency
    int hyphenationFrequency, boolean isJustified, @android.annotation.Nullable
    int[] indents) {
        android.graphics.text.LineBreaker_Delegate.Builder builder = new android.graphics.text.LineBreaker_Delegate.Builder();
        builder.mBreakStrategy = breakStrategy;
        return android.graphics.text.LineBreaker_Delegate.sBuilderManager.addNewDelegate(builder);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nGetReleaseFunc() {
        synchronized(android.graphics.text.MeasuredText_Delegate.class) {
            if (android.graphics.text.LineBreaker_Delegate.sFinalizer == (-1)) {
                android.graphics.text.LineBreaker_Delegate.sFinalizer = libcore.util.NativeAllocationRegistry_Delegate.createFinalizer(android.graphics.text.LineBreaker_Delegate.sBuilderManager::removeJavaReferenceFor);
            }
        }
        return android.graphics.text.LineBreaker_Delegate.sFinalizer;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nComputeLineBreaks(/* non zero */
    long nativePtr, // Inputs
    @android.annotation.NonNull
    char[] text, long measuredTextPtr, int length, float firstWidth, int firstWidthLineCount, float restWidth, @android.annotation.Nullable
    float[] variableTabStops, float defaultTabStop, int indentsOffset) {
        android.graphics.text.LineBreaker_Delegate.Builder builder = android.graphics.text.LineBreaker_Delegate.sBuilderManager.getDelegate(nativePtr);
        if (builder == null) {
            return 0;
        }
        builder.mText = text;
        builder.mWidths = new float[length];
        builder.mLineWidth = new android.graphics.text.LineWidth(firstWidth, firstWidthLineCount, restWidth);
        builder.mTabStopCalculator = new android.graphics.text.TabStops(variableTabStops, defaultTabStop);
        android.graphics.text.MeasuredText_Delegate.computeRuns(measuredTextPtr, builder);
        // compute all possible breakpoints.
        android.icu.text.BreakIterator it = android.icu.text.BreakIterator.getLineInstance();
        it.setText(((java.text.CharacterIterator) (new javax.swing.text.Segment(builder.mText, 0, length))));
        // average word length in english is 5. So, initialize the possible breaks with a guess.
        java.util.List<java.lang.Integer> breaks = new java.util.ArrayList<java.lang.Integer>(((int) (java.lang.Math.ceil(length / 5.0))));
        int loc;
        it.first();
        while ((loc = it.next()) != android.icu.text.BreakIterator.DONE) {
            breaks.add(loc);
        } 
        java.util.List<android.graphics.text.Primitive> primitives = android.graphics.text.LineBreaker_Delegate.computePrimitives(builder.mText, builder.mWidths, length, breaks);
        switch (builder.mBreakStrategy) {
            case android.text.Layout.BREAK_STRATEGY_SIMPLE :
                builder.mLineBreaker = new android.graphics.text.GreedyLineBreaker(primitives, builder.mLineWidth, builder.mTabStopCalculator);
                break;
            case android.text.Layout.BREAK_STRATEGY_HIGH_QUALITY :
                // TODO
                // break;
            case android.text.Layout.BREAK_STRATEGY_BALANCED :
                builder.mLineBreaker = new android.graphics.text.OptimizingLineBreaker(primitives, builder.mLineWidth, builder.mTabStopCalculator);
                break;
            default :
                assert false : "Unknown break strategy: " + builder.mBreakStrategy;
                builder.mLineBreaker = new android.graphics.text.GreedyLineBreaker(primitives, builder.mLineWidth, builder.mTabStopCalculator);
        }
        android.graphics.text.LineBreaker_Delegate.Result result = new android.graphics.text.LineBreaker_Delegate.Result(builder.mLineBreaker.computeBreaks());
        return android.graphics.text.LineBreaker_Delegate.sResultManager.addNewDelegate(result);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetLineCount(long ptr) {
        android.graphics.text.LineBreaker_Delegate.Result result = android.graphics.text.LineBreaker_Delegate.sResultManager.getDelegate(ptr);
        return result.mResult.mLineBreakOffset.size();
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetLineBreakOffset(long ptr, int idx) {
        android.graphics.text.LineBreaker_Delegate.Result result = android.graphics.text.LineBreaker_Delegate.sResultManager.getDelegate(ptr);
        return result.mResult.mLineBreakOffset.get(idx);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetLineWidth(long ptr, int idx) {
        android.graphics.text.LineBreaker_Delegate.Result result = android.graphics.text.LineBreaker_Delegate.sResultManager.getDelegate(ptr);
        return result.mResult.mLineWidths.get(idx);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetLineAscent(long ptr, int idx) {
        android.graphics.text.LineBreaker_Delegate.Result result = android.graphics.text.LineBreaker_Delegate.sResultManager.getDelegate(ptr);
        return result.mResult.mLineAscents.get(idx);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetLineDescent(long ptr, int idx) {
        android.graphics.text.LineBreaker_Delegate.Result result = android.graphics.text.LineBreaker_Delegate.sResultManager.getDelegate(ptr);
        return result.mResult.mLineDescents.get(idx);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static int nGetLineFlag(long ptr, int idx) {
        android.graphics.text.LineBreaker_Delegate.Result result = android.graphics.text.LineBreaker_Delegate.sResultManager.getDelegate(ptr);
        return result.mResult.mLineFlags.get(idx);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nGetReleaseResultFunc() {
        synchronized(android.graphics.text.MeasuredText_Delegate.class) {
            if (android.graphics.text.LineBreaker_Delegate.sResultFinalizer == (-1)) {
                android.graphics.text.LineBreaker_Delegate.sResultFinalizer = libcore.util.NativeAllocationRegistry_Delegate.createFinalizer(android.graphics.text.LineBreaker_Delegate.sBuilderManager::removeJavaReferenceFor);
            }
        }
        return android.graphics.text.LineBreaker_Delegate.sResultFinalizer;
    }

    /**
     * Compute metadata each character - things which help in deciding if it's possible to break
     * at a point or not.
     */
    @android.annotation.NonNull
    private static java.util.List<android.graphics.text.Primitive> computePrimitives(@android.annotation.NonNull
    char[] text, @android.annotation.NonNull
    float[] widths, int length, @android.annotation.NonNull
    java.util.List<java.lang.Integer> breaks) {
        // Initialize the list with a guess of the number of primitives:
        // 2 Primitives per non-whitespace char and approx 5 chars per word (i.e. 83% chars)
        java.util.List<android.graphics.text.Primitive> primitives = new java.util.ArrayList<android.graphics.text.Primitive>(((int) (java.lang.Math.ceil(length * 1.833))));
        int breaksSize = breaks.size();
        int breakIndex = 0;
        for (int i = 0; i < length; i++) {
            char c = text[i];
            if ((c == android.graphics.text.LineBreaker_Delegate.CHAR_SPACE) || (c == android.graphics.text.LineBreaker_Delegate.CHAR_ZWSP)) {
                primitives.add(android.graphics.text.Primitive.PrimitiveType.GLUE.getNewPrimitive(i, widths[i]));
            } else
                if (c == android.graphics.text.LineBreaker_Delegate.CHAR_TAB) {
                    primitives.add(android.graphics.text.Primitive.PrimitiveType.VARIABLE.getNewPrimitive(i));
                } else
                    if (c != android.graphics.text.LineBreaker_Delegate.CHAR_NEWLINE) {
                        while ((breakIndex < breaksSize) && (breaks.get(breakIndex) < i)) {
                            breakIndex++;
                        } 
                        android.graphics.text.Primitive p;
                        if (widths[i] != 0) {
                            if ((breakIndex < breaksSize) && (breaks.get(breakIndex) == i)) {
                                p = android.graphics.text.Primitive.PrimitiveType.PENALTY.getNewPrimitive(i, 0, 0);
                            } else {
                                p = android.graphics.text.Primitive.PrimitiveType.WORD_BREAK.getNewPrimitive(i, 0);
                            }
                            primitives.add(p);
                        }
                        primitives.add(android.graphics.text.Primitive.PrimitiveType.BOX.getNewPrimitive(i, widths[i]));
                    }


        }
        // final break at end of everything
        primitives.add(android.graphics.text.Primitive.PrimitiveType.PENALTY.getNewPrimitive(length, 0, -android.graphics.text.Primitive.PrimitiveType.PENALTY_INFINITY));
        return primitives;
    }

    // TODO: Rename to LineBreakerRef and move everything other than LineBreaker to LineBreaker.
    /**
     * Java representation of the native Builder class.
     */
    public static class Builder {
        char[] mText;

        float[] mWidths;

        private android.graphics.text.BaseLineBreaker mLineBreaker;

        private int mBreakStrategy;

        private android.graphics.text.LineWidth mLineWidth;

        private android.graphics.text.TabStops mTabStopCalculator;
    }

    public static abstract class Run {
        int mStart;

        int mEnd;

        Run(int start, int end) {
            mStart = start;
            mEnd = end;
        }

        abstract void addTo(android.graphics.text.LineBreaker_Delegate.Builder builder);
    }

    public static class Result {
        final android.graphics.text.BaseLineBreaker.Result mResult;

        public Result(android.graphics.text.BaseLineBreaker.Result result) {
            mResult = result;
        }
    }
}

