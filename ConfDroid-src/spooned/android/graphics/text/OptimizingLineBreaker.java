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


// Based on the native implementation of OptimizingLineBreaker in
// frameworks/base/core/jni/android_text_StaticLayout.cpp revision b808260
/**
 * A more complex version of line breaking where we try to prevent the right edge from being too
 * jagged.
 */
public class OptimizingLineBreaker extends android.graphics.text.BaseLineBreaker {
    public OptimizingLineBreaker(@android.annotation.NonNull
    java.util.List<android.graphics.text.Primitive> primitives, @android.annotation.NonNull
    android.graphics.text.LineWidth lineWidth, @android.annotation.NonNull
    android.graphics.text.TabStops tabStops) {
        super(primitives, lineWidth, tabStops);
    }

    @java.lang.Override
    public android.graphics.text.BaseLineBreaker.Result computeBreaks() {
        android.graphics.text.BaseLineBreaker.Result result = new android.graphics.text.BaseLineBreaker.Result();
        int numBreaks = mPrimitives.size();
        assert numBreaks > 0;
        if (numBreaks == 1) {
            // This can be true only if it's an empty paragraph.
            android.graphics.text.Primitive p = mPrimitives.get(0);
            assert p.type == android.graphics.text.Primitive.PrimitiveType.PENALTY;
            result.mLineBreakOffset.add(0);
            result.mLineWidths.add(p.width);
            result.mLineAscents.add(0.0F);
            result.mLineDescents.add(0.0F);
            result.mLineFlags.add(0);
            return result;
        }
        android.graphics.text.OptimizingLineBreaker.Node[] opt = new android.graphics.text.OptimizingLineBreaker.Node[numBreaks];
        opt[0] = new android.graphics.text.OptimizingLineBreaker.Node(-1, 0, 0, 0, false);
        opt[numBreaks - 1] = new android.graphics.text.OptimizingLineBreaker.Node(-1, 0, 0, 0, false);
        java.util.ArrayList<java.lang.Integer> active = new java.util.ArrayList<java.lang.Integer>();
        active.add(0);
        int lastBreak = 0;
        for (int i = 0; i < numBreaks; i++) {
            android.graphics.text.Primitive p = mPrimitives.get(i);
            if (p.type == android.graphics.text.Primitive.PrimitiveType.PENALTY) {
                boolean finalBreak = (i + 1) == numBreaks;
                android.graphics.text.OptimizingLineBreaker.Node bestBreak = null;
                /* incrementing done in loop */
                for (java.util.ListIterator<java.lang.Integer> it = active.listIterator(); it.hasNext();) {
                    int pos = it.next();
                    int lines = opt[pos].mPrevCount;
                    float maxWidth = mLineWidth.getLineWidth(lines);
                    // we have to compute metrics every time --
                    // we can't really pre-compute this stuff and just deal with breaks
                    // because of the way tab characters work, this makes it computationally
                    // harder, but this way, we can still optimize while treating tab characters
                    // correctly
                    android.graphics.text.OptimizingLineBreaker.LineMetrics lineMetrics = computeMetrics(pos, i);
                    if (lineMetrics.mPrintedWidth <= maxWidth) {
                        float demerits = android.graphics.text.OptimizingLineBreaker.computeDemerits(maxWidth, lineMetrics.mPrintedWidth, finalBreak, p.penalty) + opt[pos].mDemerits;
                        if ((bestBreak == null) || (demerits < bestBreak.mDemerits)) {
                            if (bestBreak == null) {
                                bestBreak = new android.graphics.text.OptimizingLineBreaker.Node(pos, opt[pos].mPrevCount + 1, demerits, lineMetrics.mPrintedWidth, lineMetrics.mHasTabs);
                            } else {
                                bestBreak.mPrev = pos;
                                bestBreak.mPrevCount = opt[pos].mPrevCount + 1;
                                bestBreak.mDemerits = demerits;
                                bestBreak.mWidth = lineMetrics.mPrintedWidth;
                                bestBreak.mHasTabs = lineMetrics.mHasTabs;
                            }
                        }
                    } else {
                        it.remove();
                    }
                }
                if (p.penalty == (-android.graphics.text.Primitive.PrimitiveType.PENALTY_INFINITY)) {
                    active.clear();
                }
                if (bestBreak != null) {
                    opt[i] = bestBreak;
                    active.add(i);
                    lastBreak = i;
                }
                if (active.isEmpty()) {
                    // we can't give up!
                    android.graphics.text.OptimizingLineBreaker.LineMetrics lineMetrics = new android.graphics.text.OptimizingLineBreaker.LineMetrics();
                    int lines = opt[lastBreak].mPrevCount;
                    float maxWidth = mLineWidth.getLineWidth(lines);
                    int breakIndex = desperateBreak(lastBreak, numBreaks, maxWidth, lineMetrics);
                    opt[breakIndex] = /* doesn't matter */
                    new android.graphics.text.OptimizingLineBreaker.Node(lastBreak, lines + 1, 0, lineMetrics.mWidth, lineMetrics.mHasTabs);
                    active.add(breakIndex);
                    lastBreak = breakIndex;
                    i = breakIndex;// incremented by i++

                }
            }
        }
        int idx = numBreaks - 1;
        while (opt[idx].mPrev != (-1)) {
            result.mLineBreakOffset.add(mPrimitives.get(idx).location);
            result.mLineWidths.add(opt[idx].mWidth);
            result.mLineAscents.add(0.0F);
            result.mLineDescents.add(0.0F);
            result.mLineFlags.add(opt[idx].mHasTabs ? android.graphics.text.BaseLineBreaker.TAB_MASK : 0);
            idx = opt[idx].mPrev;
        } 
        java.util.Collections.reverse(result.mLineBreakOffset);
        java.util.Collections.reverse(result.mLineWidths);
        java.util.Collections.reverse(result.mLineAscents);
        java.util.Collections.reverse(result.mLineDescents);
        java.util.Collections.reverse(result.mLineFlags);
        return result;
    }

    @android.annotation.NonNull
    private android.graphics.text.OptimizingLineBreaker.LineMetrics computeMetrics(int start, int end) {
        boolean f = false;
        float w = 0;
        float pw = 0;
        for (int i = start; i < end; i++) {
            android.graphics.text.Primitive p = mPrimitives.get(i);
            if ((p.type == android.graphics.text.Primitive.PrimitiveType.BOX) || (p.type == android.graphics.text.Primitive.PrimitiveType.GLUE)) {
                w += p.width;
                if (p.type == android.graphics.text.Primitive.PrimitiveType.BOX) {
                    pw = w;
                }
            } else
                if (p.type == android.graphics.text.Primitive.PrimitiveType.VARIABLE) {
                    w = mTabStops.width(w);
                    f = true;
                }

        }
        return new android.graphics.text.OptimizingLineBreaker.LineMetrics(w, pw, f);
    }

    private static float computeDemerits(float maxWidth, float width, boolean finalBreak, float penalty) {
        float deviation = (finalBreak) ? 0 : maxWidth - width;
        return (deviation * deviation) + penalty;
    }

    /**
     *
     *
     * @return the last break position or -1 if failed.
     */
    // method too complex to be analyzed.
    @java.lang.SuppressWarnings("ConstantConditions")
    private int desperateBreak(int start, int limit, float maxWidth, @android.annotation.NonNull
    android.graphics.text.OptimizingLineBreaker.LineMetrics lineMetrics) {
        float w = 0;
        float pw = 0;
        boolean breakFound = false;
        int breakIndex = 0;
        int firstTabIndex = java.lang.Integer.MAX_VALUE;
        for (int i = start; i < limit; i++) {
            android.graphics.text.Primitive p = mPrimitives.get(i);
            if ((p.type == android.graphics.text.Primitive.PrimitiveType.BOX) || (p.type == android.graphics.text.Primitive.PrimitiveType.GLUE)) {
                w += p.width;
                if (p.type == android.graphics.text.Primitive.PrimitiveType.BOX) {
                    pw = w;
                }
            } else
                if (p.type == android.graphics.text.Primitive.PrimitiveType.VARIABLE) {
                    w = mTabStops.width(w);
                    firstTabIndex = java.lang.Math.min(firstTabIndex, i);
                }

            if ((pw > maxWidth) && breakFound) {
                break;
            }
            // must make progress
            if ((i > start) && ((p.type == android.graphics.text.Primitive.PrimitiveType.PENALTY) || (p.type == android.graphics.text.Primitive.PrimitiveType.WORD_BREAK))) {
                breakFound = true;
                breakIndex = i;
            }
        }
        if (breakFound) {
            lineMetrics.mWidth = w;
            lineMetrics.mPrintedWidth = pw;
            lineMetrics.mHasTabs = (start <= firstTabIndex) && (firstTabIndex < breakIndex);
            return breakIndex;
        } else {
            return -1;
        }
    }

    private static class LineMetrics {
        /**
         * Actual width of the line.
         */
        float mWidth;

        /**
         * Width of the line minus trailing whitespace.
         */
        float mPrintedWidth;

        boolean mHasTabs;

        public LineMetrics() {
        }

        public LineMetrics(float width, float printedWidth, boolean hasTabs) {
            mWidth = width;
            mPrintedWidth = printedWidth;
            mHasTabs = hasTabs;
        }
    }

    /**
     * A struct to store the info about a break.
     */
    // For the word struct.
    @java.lang.SuppressWarnings("SpellCheckingInspection")
    private static class Node {
        // -1 for the first node.
        int mPrev;

        // number of breaks so far.
        int mPrevCount;

        float mDemerits;

        float mWidth;

        boolean mHasTabs;

        public Node(int prev, int prevCount, float demerits, float width, boolean hasTabs) {
            mPrev = prev;
            mPrevCount = prevCount;
            mDemerits = demerits;
            mWidth = width;
            mHasTabs = hasTabs;
        }
    }
}

