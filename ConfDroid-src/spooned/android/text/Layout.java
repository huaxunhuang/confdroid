/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.text;


/**
 * A base class that manages text layout in visual elements on
 * the screen.
 * <p>For text that will be edited, use a {@link DynamicLayout},
 * which will be updated as the text changes.
 * For text that will not change, use a {@link StaticLayout}.
 */
public abstract class Layout {
    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.text.Layout.BREAK_STRATEGY_SIMPLE, android.text.Layout.BREAK_STRATEGY_HIGH_QUALITY, android.text.Layout.BREAK_STRATEGY_BALANCED })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface BreakStrategy {}

    /**
     * Value for break strategy indicating simple line breaking. Automatic hyphens are not added
     * (though soft hyphens are respected), and modifying text generally doesn't affect the layout
     * before it (which yields a more consistent user experience when editing), but layout may not
     * be the highest quality.
     */
    public static final int BREAK_STRATEGY_SIMPLE = 0;

    /**
     * Value for break strategy indicating high quality line breaking, including automatic
     * hyphenation and doing whole-paragraph optimization of line breaks.
     */
    public static final int BREAK_STRATEGY_HIGH_QUALITY = 1;

    /**
     * Value for break strategy indicating balanced line breaking. The breaks are chosen to
     * make all lines as close to the same length as possible, including automatic hyphenation.
     */
    public static final int BREAK_STRATEGY_BALANCED = 2;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.text.Layout.HYPHENATION_FREQUENCY_NORMAL, android.text.Layout.HYPHENATION_FREQUENCY_FULL, android.text.Layout.HYPHENATION_FREQUENCY_NONE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface HyphenationFrequency {}

    /**
     * Value for hyphenation frequency indicating no automatic hyphenation. Useful
     * for backward compatibility, and for cases where the automatic hyphenation algorithm results
     * in incorrect hyphenation. Mid-word breaks may still happen when a word is wider than the
     * layout and there is otherwise no valid break. Soft hyphens are ignored and will not be used
     * as suggestions for potential line breaks.
     */
    public static final int HYPHENATION_FREQUENCY_NONE = 0;

    /**
     * Value for hyphenation frequency indicating a light amount of automatic hyphenation, which
     * is a conservative default. Useful for informal cases, such as short sentences or chat
     * messages.
     */
    public static final int HYPHENATION_FREQUENCY_NORMAL = 1;

    /**
     * Value for hyphenation frequency indicating the full amount of automatic hyphenation, typical
     * in typography. Useful for running text and where it's important to put the maximum amount of
     * text in a screen with limited space.
     */
    public static final int HYPHENATION_FREQUENCY_FULL = 2;

    private static final android.text.style.ParagraphStyle[] NO_PARA_SPANS = com.android.internal.util.ArrayUtils.emptyArray(android.text.style.ParagraphStyle.class);

    /**
     * Return how wide a layout must be in order to display the
     * specified text with one line per paragraph.
     */
    public static float getDesiredWidth(java.lang.CharSequence source, android.text.TextPaint paint) {
        return android.text.Layout.getDesiredWidth(source, 0, source.length(), paint);
    }

    /**
     * Return how wide a layout must be in order to display the
     * specified text slice with one line per paragraph.
     */
    public static float getDesiredWidth(java.lang.CharSequence source, int start, int end, android.text.TextPaint paint) {
        float need = 0;
        int next;
        for (int i = start; i <= end; i = next) {
            next = android.text.TextUtils.indexOf(source, '\n', i, end);
            if (next < 0)
                next = end;

            // note, omits trailing paragraph char
            float w = android.text.Layout.measurePara(paint, source, i, next);
            if (w > need)
                need = w;

            next++;
        }
        return need;
    }

    /**
     * Subclasses of Layout use this constructor to set the display text,
     * width, and other standard properties.
     *
     * @param text
     * 		the text to render
     * @param paint
     * 		the default paint for the layout.  Styles can override
     * 		various attributes of the paint.
     * @param width
     * 		the wrapping width for the text.
     * @param align
     * 		whether to left, right, or center the text.  Styles can
     * 		override the alignment.
     * @param spacingMult
     * 		factor by which to scale the font size to get the
     * 		default line spacing
     * @param spacingAdd
     * 		amount to add to the default line spacing
     */
    protected Layout(java.lang.CharSequence text, android.text.TextPaint paint, int width, android.text.Layout.Alignment align, float spacingMult, float spacingAdd) {
        this(text, paint, width, align, android.text.TextDirectionHeuristics.FIRSTSTRONG_LTR, spacingMult, spacingAdd);
    }

    /**
     * Subclasses of Layout use this constructor to set the display text,
     * width, and other standard properties.
     *
     * @param text
     * 		the text to render
     * @param paint
     * 		the default paint for the layout.  Styles can override
     * 		various attributes of the paint.
     * @param width
     * 		the wrapping width for the text.
     * @param align
     * 		whether to left, right, or center the text.  Styles can
     * 		override the alignment.
     * @param spacingMult
     * 		factor by which to scale the font size to get the
     * 		default line spacing
     * @param spacingAdd
     * 		amount to add to the default line spacing
     * @unknown 
     */
    protected Layout(java.lang.CharSequence text, android.text.TextPaint paint, int width, android.text.Layout.Alignment align, android.text.TextDirectionHeuristic textDir, float spacingMult, float spacingAdd) {
        if (width < 0)
            throw new java.lang.IllegalArgumentException(("Layout: " + width) + " < 0");

        // Ensure paint doesn't have baselineShift set.
        // While normally we don't modify the paint the user passed in,
        // we were already doing this in Styled.drawUniformRun with both
        // baselineShift and bgColor.  We probably should reevaluate bgColor.
        if (paint != null) {
            paint.bgColor = 0;
            paint.baselineShift = 0;
        }
        mText = text;
        mPaint = paint;
        mWidth = width;
        mAlignment = align;
        mSpacingMult = spacingMult;
        mSpacingAdd = spacingAdd;
        mSpannedText = text instanceof android.text.Spanned;
        mTextDir = textDir;
    }

    /**
     * Replace constructor properties of this Layout with new ones.  Be careful.
     */
    /* package */
    void replaceWith(java.lang.CharSequence text, android.text.TextPaint paint, int width, android.text.Layout.Alignment align, float spacingmult, float spacingadd) {
        if (width < 0) {
            throw new java.lang.IllegalArgumentException(("Layout: " + width) + " < 0");
        }
        mText = text;
        mPaint = paint;
        mWidth = width;
        mAlignment = align;
        mSpacingMult = spacingmult;
        mSpacingAdd = spacingadd;
        mSpannedText = text instanceof android.text.Spanned;
    }

    /**
     * Draw this Layout on the specified Canvas.
     */
    public void draw(android.graphics.Canvas c) {
        draw(c, null, null, 0);
    }

    /**
     * Draw this Layout on the specified canvas, with the highlight path drawn
     * between the background and the text.
     *
     * @param canvas
     * 		the canvas
     * @param highlight
     * 		the path of the highlight or cursor; can be null
     * @param highlightPaint
     * 		the paint for the highlight
     * @param cursorOffsetVertical
     * 		the amount to temporarily translate the
     * 		canvas while rendering the highlight
     */
    public void draw(android.graphics.Canvas canvas, android.graphics.Path highlight, android.graphics.Paint highlightPaint, int cursorOffsetVertical) {
        final long lineRange = getLineRangeForDraw(canvas);
        int firstLine = android.text.TextUtils.unpackRangeStartFromLong(lineRange);
        int lastLine = android.text.TextUtils.unpackRangeEndFromLong(lineRange);
        if (lastLine < 0)
            return;

        drawBackground(canvas, highlight, highlightPaint, cursorOffsetVertical, firstLine, lastLine);
        drawText(canvas, firstLine, lastLine);
    }

    /**
     *
     *
     * @unknown 
     */
    public void drawText(android.graphics.Canvas canvas, int firstLine, int lastLine) {
        int previousLineBottom = getLineTop(firstLine);
        int previousLineEnd = getLineStart(firstLine);
        android.text.style.ParagraphStyle[] spans = android.text.Layout.NO_PARA_SPANS;
        int spanEnd = 0;
        android.text.TextPaint paint = mPaint;
        java.lang.CharSequence buf = mText;
        android.text.Layout.Alignment paraAlign = mAlignment;
        android.text.Layout.TabStops tabStops = null;
        boolean tabStopsIsInitialized = false;
        android.text.TextLine tl = android.text.TextLine.obtain();
        // Draw the lines, one at a time.
        // The baseline is the top of the following line minus the current line's descent.
        for (int lineNum = firstLine; lineNum <= lastLine; lineNum++) {
            int start = previousLineEnd;
            previousLineEnd = getLineStart(lineNum + 1);
            int end = getLineVisibleEnd(lineNum, start, previousLineEnd);
            int ltop = previousLineBottom;
            int lbottom = getLineTop(lineNum + 1);
            previousLineBottom = lbottom;
            int lbaseline = lbottom - getLineDescent(lineNum);
            int dir = getParagraphDirection(lineNum);
            int left = 0;
            int right = mWidth;
            if (mSpannedText) {
                android.text.Spanned sp = ((android.text.Spanned) (buf));
                int textLength = buf.length();
                boolean isFirstParaLine = (start == 0) || (buf.charAt(start - 1) == '\n');
                // New batch of paragraph styles, collect into spans array.
                // Compute the alignment, last alignment style wins.
                // Reset tabStops, we'll rebuild if we encounter a line with
                // tabs.
                // We expect paragraph spans to be relatively infrequent, use
                // spanEnd so that we can check less frequently.  Since
                // paragraph styles ought to apply to entire paragraphs, we can
                // just collect the ones present at the start of the paragraph.
                // If spanEnd is before the end of the paragraph, that's not
                // our problem.
                if ((start >= spanEnd) && ((lineNum == firstLine) || isFirstParaLine)) {
                    spanEnd = sp.nextSpanTransition(start, textLength, android.text.style.ParagraphStyle.class);
                    spans = android.text.Layout.getParagraphSpans(sp, start, spanEnd, android.text.style.ParagraphStyle.class);
                    paraAlign = mAlignment;
                    for (int n = spans.length - 1; n >= 0; n--) {
                        if (spans[n] instanceof android.text.style.AlignmentSpan) {
                            paraAlign = ((android.text.style.AlignmentSpan) (spans[n])).getAlignment();
                            break;
                        }
                    }
                    tabStopsIsInitialized = false;
                }
                // Draw all leading margin spans.  Adjust left or right according
                // to the paragraph direction of the line.
                final int length = spans.length;
                boolean useFirstLineMargin = isFirstParaLine;
                for (int n = 0; n < length; n++) {
                    if (spans[n] instanceof android.text.style.LeadingMarginSpan.LeadingMarginSpan2) {
                        int count = ((android.text.style.LeadingMarginSpan.LeadingMarginSpan2) (spans[n])).getLeadingMarginLineCount();
                        int startLine = getLineForOffset(sp.getSpanStart(spans[n]));
                        // if there is more than one LeadingMarginSpan2, use
                        // the count that is greatest
                        if (lineNum < (startLine + count)) {
                            useFirstLineMargin = true;
                            break;
                        }
                    }
                }
                for (int n = 0; n < length; n++) {
                    if (spans[n] instanceof android.text.style.LeadingMarginSpan) {
                        android.text.style.LeadingMarginSpan margin = ((android.text.style.LeadingMarginSpan) (spans[n]));
                        if (dir == android.text.Layout.DIR_RIGHT_TO_LEFT) {
                            margin.drawLeadingMargin(canvas, paint, right, dir, ltop, lbaseline, lbottom, buf, start, end, isFirstParaLine, this);
                            right -= margin.getLeadingMargin(useFirstLineMargin);
                        } else {
                            margin.drawLeadingMargin(canvas, paint, left, dir, ltop, lbaseline, lbottom, buf, start, end, isFirstParaLine, this);
                            left += margin.getLeadingMargin(useFirstLineMargin);
                        }
                    }
                }
            }
            boolean hasTab = getLineContainsTab(lineNum);
            // Can't tell if we have tabs for sure, currently
            if (hasTab && (!tabStopsIsInitialized)) {
                if (tabStops == null) {
                    tabStops = new android.text.Layout.TabStops(android.text.Layout.TAB_INCREMENT, spans);
                } else {
                    tabStops.reset(android.text.Layout.TAB_INCREMENT, spans);
                }
                tabStopsIsInitialized = true;
            }
            // Determine whether the line aligns to normal, opposite, or center.
            android.text.Layout.Alignment align = paraAlign;
            if (align == android.text.Layout.Alignment.ALIGN_LEFT) {
                align = (dir == android.text.Layout.DIR_LEFT_TO_RIGHT) ? android.text.Layout.Alignment.ALIGN_NORMAL : android.text.Layout.Alignment.ALIGN_OPPOSITE;
            } else
                if (align == android.text.Layout.Alignment.ALIGN_RIGHT) {
                    align = (dir == android.text.Layout.DIR_LEFT_TO_RIGHT) ? android.text.Layout.Alignment.ALIGN_OPPOSITE : android.text.Layout.Alignment.ALIGN_NORMAL;
                }

            int x;
            if (align == android.text.Layout.Alignment.ALIGN_NORMAL) {
                if (dir == android.text.Layout.DIR_LEFT_TO_RIGHT) {
                    x = left + getIndentAdjust(lineNum, android.text.Layout.Alignment.ALIGN_LEFT);
                } else {
                    x = right + getIndentAdjust(lineNum, android.text.Layout.Alignment.ALIGN_RIGHT);
                }
            } else {
                int max = ((int) (getLineExtent(lineNum, tabStops, false)));
                if (align == android.text.Layout.Alignment.ALIGN_OPPOSITE) {
                    if (dir == android.text.Layout.DIR_LEFT_TO_RIGHT) {
                        x = (right - max) + getIndentAdjust(lineNum, android.text.Layout.Alignment.ALIGN_RIGHT);
                    } else {
                        x = (left - max) + getIndentAdjust(lineNum, android.text.Layout.Alignment.ALIGN_LEFT);
                    }
                } else {
                    // Alignment.ALIGN_CENTER
                    max = max & (~1);
                    x = (((right + left) - max) >> 1) + getIndentAdjust(lineNum, android.text.Layout.Alignment.ALIGN_CENTER);
                }
            }
            paint.setHyphenEdit(getHyphen(lineNum));
            android.text.Layout.Directions directions = getLineDirections(lineNum);
            if (((directions == android.text.Layout.DIRS_ALL_LEFT_TO_RIGHT) && (!mSpannedText)) && (!hasTab)) {
                // XXX: assumes there's nothing additional to be done
                canvas.drawText(buf, start, end, x, lbaseline, paint);
            } else {
                tl.set(paint, buf, start, end, dir, directions, hasTab, tabStops);
                tl.draw(canvas, x, ltop, lbaseline, lbottom);
            }
            paint.setHyphenEdit(0);
        }
        android.text.TextLine.recycle(tl);
    }

    /**
     *
     *
     * @unknown 
     */
    public void drawBackground(android.graphics.Canvas canvas, android.graphics.Path highlight, android.graphics.Paint highlightPaint, int cursorOffsetVertical, int firstLine, int lastLine) {
        // First, draw LineBackgroundSpans.
        // LineBackgroundSpans know nothing about the alignment, margins, or
        // direction of the layout or line.  XXX: Should they?
        // They are evaluated at each line.
        if (mSpannedText) {
            if (mLineBackgroundSpans == null) {
                mLineBackgroundSpans = new android.text.SpanSet<android.text.style.LineBackgroundSpan>(android.text.style.LineBackgroundSpan.class);
            }
            android.text.Spanned buffer = ((android.text.Spanned) (mText));
            int textLength = buffer.length();
            mLineBackgroundSpans.init(buffer, 0, textLength);
            if (mLineBackgroundSpans.numberOfSpans > 0) {
                int previousLineBottom = getLineTop(firstLine);
                int previousLineEnd = getLineStart(firstLine);
                android.text.style.ParagraphStyle[] spans = android.text.Layout.NO_PARA_SPANS;
                int spansLength = 0;
                android.text.TextPaint paint = mPaint;
                int spanEnd = 0;
                final int width = mWidth;
                for (int i = firstLine; i <= lastLine; i++) {
                    int start = previousLineEnd;
                    int end = getLineStart(i + 1);
                    previousLineEnd = end;
                    int ltop = previousLineBottom;
                    int lbottom = getLineTop(i + 1);
                    previousLineBottom = lbottom;
                    int lbaseline = lbottom - getLineDescent(i);
                    if (start >= spanEnd) {
                        // These should be infrequent, so we'll use this so that
                        // we don't have to check as often.
                        spanEnd = mLineBackgroundSpans.getNextTransition(start, textLength);
                        // All LineBackgroundSpans on a line contribute to its background.
                        spansLength = 0;
                        // Duplication of the logic of getParagraphSpans
                        if ((start != end) || (start == 0)) {
                            // Equivalent to a getSpans(start, end), but filling the 'spans' local
                            // array instead to reduce memory allocation
                            for (int j = 0; j < mLineBackgroundSpans.numberOfSpans; j++) {
                                // equal test is valid since both intervals are not empty by
                                // construction
                                if ((mLineBackgroundSpans.spanStarts[j] >= end) || (mLineBackgroundSpans.spanEnds[j] <= start))
                                    continue;

                                spans = com.android.internal.util.GrowingArrayUtils.append(spans, spansLength, mLineBackgroundSpans.spans[j]);
                                spansLength++;
                            }
                        }
                    }
                    for (int n = 0; n < spansLength; n++) {
                        android.text.style.LineBackgroundSpan lineBackgroundSpan = ((android.text.style.LineBackgroundSpan) (spans[n]));
                        lineBackgroundSpan.drawBackground(canvas, paint, 0, width, ltop, lbaseline, lbottom, buffer, start, end, i);
                    }
                }
            }
            mLineBackgroundSpans.recycle();
        }
        // There can be a highlight even without spans if we are drawing
        // a non-spanned transformation of a spanned editing buffer.
        if (highlight != null) {
            if (cursorOffsetVertical != 0)
                canvas.translate(0, cursorOffsetVertical);

            canvas.drawPath(highlight, highlightPaint);
            if (cursorOffsetVertical != 0)
                canvas.translate(0, -cursorOffsetVertical);

        }
    }

    /**
     *
     *
     * @param canvas
     * 		
     * @return The range of lines that need to be drawn, possibly empty.
     * @unknown 
     */
    public long getLineRangeForDraw(android.graphics.Canvas canvas) {
        int dtop;
        int dbottom;
        synchronized(android.text.Layout.sTempRect) {
            if (!canvas.getClipBounds(android.text.Layout.sTempRect)) {
                // Negative range end used as a special flag
                return android.text.TextUtils.packRangeInLong(0, -1);
            }
            dtop = android.text.Layout.sTempRect.top;
            dbottom = android.text.Layout.sTempRect.bottom;
        }
        final int top = java.lang.Math.max(dtop, 0);
        final int bottom = java.lang.Math.min(getLineTop(getLineCount()), dbottom);
        if (top >= bottom)
            return android.text.TextUtils.packRangeInLong(0, -1);

        return android.text.TextUtils.packRangeInLong(getLineForVertical(top), getLineForVertical(bottom));
    }

    /**
     * Return the start position of the line, given the left and right bounds
     * of the margins.
     *
     * @param line
     * 		the line index
     * @param left
     * 		the left bounds (0, or leading margin if ltr para)
     * @param right
     * 		the right bounds (width, minus leading margin if rtl para)
     * @return the start position of the line (to right of line if rtl para)
     */
    private int getLineStartPos(int line, int left, int right) {
        // Adjust the point at which to start rendering depending on the
        // alignment of the paragraph.
        android.text.Layout.Alignment align = getParagraphAlignment(line);
        int dir = getParagraphDirection(line);
        if (align == android.text.Layout.Alignment.ALIGN_LEFT) {
            align = (dir == android.text.Layout.DIR_LEFT_TO_RIGHT) ? android.text.Layout.Alignment.ALIGN_NORMAL : android.text.Layout.Alignment.ALIGN_OPPOSITE;
        } else
            if (align == android.text.Layout.Alignment.ALIGN_RIGHT) {
                align = (dir == android.text.Layout.DIR_LEFT_TO_RIGHT) ? android.text.Layout.Alignment.ALIGN_OPPOSITE : android.text.Layout.Alignment.ALIGN_NORMAL;
            }

        int x;
        if (align == android.text.Layout.Alignment.ALIGN_NORMAL) {
            if (dir == android.text.Layout.DIR_LEFT_TO_RIGHT) {
                x = left + getIndentAdjust(line, android.text.Layout.Alignment.ALIGN_LEFT);
            } else {
                x = right + getIndentAdjust(line, android.text.Layout.Alignment.ALIGN_RIGHT);
            }
        } else {
            android.text.Layout.TabStops tabStops = null;
            if (mSpannedText && getLineContainsTab(line)) {
                android.text.Spanned spanned = ((android.text.Spanned) (mText));
                int start = getLineStart(line);
                int spanEnd = spanned.nextSpanTransition(start, spanned.length(), android.text.style.TabStopSpan.class);
                android.text.style.TabStopSpan[] tabSpans = android.text.Layout.getParagraphSpans(spanned, start, spanEnd, android.text.style.TabStopSpan.class);
                if (tabSpans.length > 0) {
                    tabStops = new android.text.Layout.TabStops(android.text.Layout.TAB_INCREMENT, tabSpans);
                }
            }
            int max = ((int) (getLineExtent(line, tabStops, false)));
            if (align == android.text.Layout.Alignment.ALIGN_OPPOSITE) {
                if (dir == android.text.Layout.DIR_LEFT_TO_RIGHT) {
                    x = (right - max) + getIndentAdjust(line, android.text.Layout.Alignment.ALIGN_RIGHT);
                } else {
                    // max is negative here
                    x = (left - max) + getIndentAdjust(line, android.text.Layout.Alignment.ALIGN_LEFT);
                }
            } else {
                // Alignment.ALIGN_CENTER
                max = max & (~1);
                x = ((left + right) - max) >> (1 + getIndentAdjust(line, android.text.Layout.Alignment.ALIGN_CENTER));
            }
        }
        return x;
    }

    /**
     * Return the text that is displayed by this Layout.
     */
    public final java.lang.CharSequence getText() {
        return mText;
    }

    /**
     * Return the base Paint properties for this layout.
     * Do NOT change the paint, which may result in funny
     * drawing for this layout.
     */
    public final android.text.TextPaint getPaint() {
        return mPaint;
    }

    /**
     * Return the width of this layout.
     */
    public final int getWidth() {
        return mWidth;
    }

    /**
     * Return the width to which this Layout is ellipsizing, or
     * {@link #getWidth} if it is not doing anything special.
     */
    public int getEllipsizedWidth() {
        return mWidth;
    }

    /**
     * Increase the width of this layout to the specified width.
     * Be careful to use this only when you know it is appropriate&mdash;
     * it does not cause the text to reflow to use the full new width.
     */
    public final void increaseWidthTo(int wid) {
        if (wid < mWidth) {
            throw new java.lang.RuntimeException("attempted to reduce Layout width");
        }
        mWidth = wid;
    }

    /**
     * Return the total height of this layout.
     */
    public int getHeight() {
        return getLineTop(getLineCount());
    }

    /**
     * Return the base alignment of this layout.
     */
    public final android.text.Layout.Alignment getAlignment() {
        return mAlignment;
    }

    /**
     * Return what the text height is multiplied by to get the line height.
     */
    public final float getSpacingMultiplier() {
        return mSpacingMult;
    }

    /**
     * Return the number of units of leading that are added to each line.
     */
    public final float getSpacingAdd() {
        return mSpacingAdd;
    }

    /**
     * Return the heuristic used to determine paragraph text direction.
     *
     * @unknown 
     */
    public final android.text.TextDirectionHeuristic getTextDirectionHeuristic() {
        return mTextDir;
    }

    /**
     * Return the number of lines of text in this layout.
     */
    public abstract int getLineCount();

    /**
     * Return the baseline for the specified line (0&hellip;getLineCount() - 1)
     * If bounds is not null, return the top, left, right, bottom extents
     * of the specified line in it.
     *
     * @param line
     * 		which line to examine (0..getLineCount() - 1)
     * @param bounds
     * 		Optional. If not null, it returns the extent of the line
     * @return the Y-coordinate of the baseline
     */
    public int getLineBounds(int line, android.graphics.Rect bounds) {
        if (bounds != null) {
            bounds.left = 0;// ???

            bounds.top = getLineTop(line);
            bounds.right = mWidth;// ???

            bounds.bottom = getLineTop(line + 1);
        }
        return getLineBaseline(line);
    }

    /**
     * Return the vertical position of the top of the specified line
     * (0&hellip;getLineCount()).
     * If the specified line is equal to the line count, returns the
     * bottom of the last line.
     */
    public abstract int getLineTop(int line);

    /**
     * Return the descent of the specified line(0&hellip;getLineCount() - 1).
     */
    public abstract int getLineDescent(int line);

    /**
     * Return the text offset of the beginning of the specified line (
     * 0&hellip;getLineCount()). If the specified line is equal to the line
     * count, returns the length of the text.
     */
    public abstract int getLineStart(int line);

    /**
     * Returns the primary directionality of the paragraph containing the
     * specified line, either 1 for left-to-right lines, or -1 for right-to-left
     * lines (see {@link #DIR_LEFT_TO_RIGHT}, {@link #DIR_RIGHT_TO_LEFT}).
     */
    public abstract int getParagraphDirection(int line);

    /**
     * Returns whether the specified line contains one or more
     * characters that need to be handled specially, like tabs.
     */
    public abstract boolean getLineContainsTab(int line);

    /**
     * Returns the directional run information for the specified line.
     * The array alternates counts of characters in left-to-right
     * and right-to-left segments of the line.
     *
     * <p>NOTE: this is inadequate to support bidirectional text, and will change.
     */
    public abstract android.text.Layout.Directions getLineDirections(int line);

    /**
     * Returns the (negative) number of extra pixels of ascent padding in the
     * top line of the Layout.
     */
    public abstract int getTopPadding();

    /**
     * Returns the number of extra pixels of descent padding in the
     * bottom line of the Layout.
     */
    public abstract int getBottomPadding();

    /**
     * Returns the hyphen edit for a line.
     *
     * @unknown 
     */
    public int getHyphen(int line) {
        return 0;
    }

    /**
     * Returns the left indent for a line.
     *
     * @unknown 
     */
    public int getIndentAdjust(int line, android.text.Layout.Alignment alignment) {
        return 0;
    }

    /**
     * Returns true if the character at offset and the preceding character
     * are at different run levels (and thus there's a split caret).
     *
     * @param offset
     * 		the offset
     * @return true if at a level boundary
     * @unknown 
     */
    public boolean isLevelBoundary(int offset) {
        int line = getLineForOffset(offset);
        android.text.Layout.Directions dirs = getLineDirections(line);
        if ((dirs == android.text.Layout.DIRS_ALL_LEFT_TO_RIGHT) || (dirs == android.text.Layout.DIRS_ALL_RIGHT_TO_LEFT)) {
            return false;
        }
        int[] runs = dirs.mDirections;
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        if ((offset == lineStart) || (offset == lineEnd)) {
            int paraLevel = (getParagraphDirection(line) == 1) ? 0 : 1;
            int runIndex = (offset == lineStart) ? 0 : runs.length - 2;
            return ((runs[runIndex + 1] >>> android.text.Layout.RUN_LEVEL_SHIFT) & android.text.Layout.RUN_LEVEL_MASK) != paraLevel;
        }
        offset -= lineStart;
        for (int i = 0; i < runs.length; i += 2) {
            if (offset == runs[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the character at offset is right to left (RTL).
     *
     * @param offset
     * 		the offset
     * @return true if the character is RTL, false if it is LTR
     */
    public boolean isRtlCharAt(int offset) {
        int line = getLineForOffset(offset);
        android.text.Layout.Directions dirs = getLineDirections(line);
        if (dirs == android.text.Layout.DIRS_ALL_LEFT_TO_RIGHT) {
            return false;
        }
        if (dirs == android.text.Layout.DIRS_ALL_RIGHT_TO_LEFT) {
            return true;
        }
        int[] runs = dirs.mDirections;
        int lineStart = getLineStart(line);
        for (int i = 0; i < runs.length; i += 2) {
            int start = lineStart + runs[i];
            int limit = start + (runs[i + 1] & android.text.Layout.RUN_LENGTH_MASK);
            if ((offset >= start) && (offset < limit)) {
                int level = (runs[i + 1] >>> android.text.Layout.RUN_LEVEL_SHIFT) & android.text.Layout.RUN_LEVEL_MASK;
                return (level & 1) != 0;
            }
        }
        // Should happen only if the offset is "out of bounds"
        return false;
    }

    /**
     * Returns the range of the run that the character at offset belongs to.
     *
     * @param offset
     * 		the offset
     * @return The range of the run
     * @unknown 
     */
    public long getRunRange(int offset) {
        int line = getLineForOffset(offset);
        android.text.Layout.Directions dirs = getLineDirections(line);
        if ((dirs == android.text.Layout.DIRS_ALL_LEFT_TO_RIGHT) || (dirs == android.text.Layout.DIRS_ALL_RIGHT_TO_LEFT)) {
            return android.text.TextUtils.packRangeInLong(0, getLineEnd(line));
        }
        int[] runs = dirs.mDirections;
        int lineStart = getLineStart(line);
        for (int i = 0; i < runs.length; i += 2) {
            int start = lineStart + runs[i];
            int limit = start + (runs[i + 1] & android.text.Layout.RUN_LENGTH_MASK);
            if ((offset >= start) && (offset < limit)) {
                return android.text.TextUtils.packRangeInLong(start, limit);
            }
        }
        // Should happen only if the offset is "out of bounds"
        return android.text.TextUtils.packRangeInLong(0, getLineEnd(line));
    }

    private boolean primaryIsTrailingPrevious(int offset) {
        int line = getLineForOffset(offset);
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        int[] runs = getLineDirections(line).mDirections;
        int levelAt = -1;
        for (int i = 0; i < runs.length; i += 2) {
            int start = lineStart + runs[i];
            int limit = start + (runs[i + 1] & android.text.Layout.RUN_LENGTH_MASK);
            if (limit > lineEnd) {
                limit = lineEnd;
            }
            if ((offset >= start) && (offset < limit)) {
                if (offset > start) {
                    // Previous character is at same level, so don't use trailing.
                    return false;
                }
                levelAt = (runs[i + 1] >>> android.text.Layout.RUN_LEVEL_SHIFT) & android.text.Layout.RUN_LEVEL_MASK;
                break;
            }
        }
        if (levelAt == (-1)) {
            // Offset was limit of line.
            levelAt = (getParagraphDirection(line) == 1) ? 0 : 1;
        }
        // At level boundary, check previous level.
        int levelBefore = -1;
        if (offset == lineStart) {
            levelBefore = (getParagraphDirection(line) == 1) ? 0 : 1;
        } else {
            offset -= 1;
            for (int i = 0; i < runs.length; i += 2) {
                int start = lineStart + runs[i];
                int limit = start + (runs[i + 1] & android.text.Layout.RUN_LENGTH_MASK);
                if (limit > lineEnd) {
                    limit = lineEnd;
                }
                if ((offset >= start) && (offset < limit)) {
                    levelBefore = (runs[i + 1] >>> android.text.Layout.RUN_LEVEL_SHIFT) & android.text.Layout.RUN_LEVEL_MASK;
                    break;
                }
            }
        }
        return levelBefore < levelAt;
    }

    /**
     * Get the primary horizontal position for the specified text offset.
     * This is the location where a new character would be inserted in
     * the paragraph's primary direction.
     */
    public float getPrimaryHorizontal(int offset) {
        return /* not clamped */
        getPrimaryHorizontal(offset, false);
    }

    /**
     * Get the primary horizontal position for the specified text offset, but
     * optionally clamp it so that it doesn't exceed the width of the layout.
     *
     * @unknown 
     */
    public float getPrimaryHorizontal(int offset, boolean clamped) {
        boolean trailing = primaryIsTrailingPrevious(offset);
        return getHorizontal(offset, trailing, clamped);
    }

    /**
     * Get the secondary horizontal position for the specified text offset.
     * This is the location where a new character would be inserted in
     * the direction other than the paragraph's primary direction.
     */
    public float getSecondaryHorizontal(int offset) {
        return /* not clamped */
        getSecondaryHorizontal(offset, false);
    }

    /**
     * Get the secondary horizontal position for the specified text offset, but
     * optionally clamp it so that it doesn't exceed the width of the layout.
     *
     * @unknown 
     */
    public float getSecondaryHorizontal(int offset, boolean clamped) {
        boolean trailing = primaryIsTrailingPrevious(offset);
        return getHorizontal(offset, !trailing, clamped);
    }

    private float getHorizontal(int offset, boolean primary) {
        return primary ? getPrimaryHorizontal(offset) : getSecondaryHorizontal(offset);
    }

    private float getHorizontal(int offset, boolean trailing, boolean clamped) {
        int line = getLineForOffset(offset);
        return getHorizontal(offset, trailing, line, clamped);
    }

    private float getHorizontal(int offset, boolean trailing, int line, boolean clamped) {
        int start = getLineStart(line);
        int end = getLineEnd(line);
        int dir = getParagraphDirection(line);
        boolean hasTab = getLineContainsTab(line);
        android.text.Layout.Directions directions = getLineDirections(line);
        android.text.Layout.TabStops tabStops = null;
        if (hasTab && (mText instanceof android.text.Spanned)) {
            // Just checking this line should be good enough, tabs should be
            // consistent across all lines in a paragraph.
            android.text.style.TabStopSpan[] tabs = android.text.Layout.getParagraphSpans(((android.text.Spanned) (mText)), start, end, android.text.style.TabStopSpan.class);
            if (tabs.length > 0) {
                tabStops = new android.text.Layout.TabStops(android.text.Layout.TAB_INCREMENT, tabs);// XXX should reuse

            }
        }
        android.text.TextLine tl = android.text.TextLine.obtain();
        tl.set(mPaint, mText, start, end, dir, directions, hasTab, tabStops);
        float wid = tl.measure(offset - start, trailing, null);
        android.text.TextLine.recycle(tl);
        if (clamped && (wid > mWidth)) {
            wid = mWidth;
        }
        int left = getParagraphLeft(line);
        int right = getParagraphRight(line);
        return getLineStartPos(line, left, right) + wid;
    }

    /**
     * Get the leftmost position that should be exposed for horizontal
     * scrolling on the specified line.
     */
    public float getLineLeft(int line) {
        int dir = getParagraphDirection(line);
        android.text.Layout.Alignment align = getParagraphAlignment(line);
        if (align == android.text.Layout.Alignment.ALIGN_LEFT) {
            return 0;
        } else
            if (align == android.text.Layout.Alignment.ALIGN_NORMAL) {
                if (dir == android.text.Layout.DIR_RIGHT_TO_LEFT)
                    return getParagraphRight(line) - getLineMax(line);
                else
                    return 0;

            } else
                if (align == android.text.Layout.Alignment.ALIGN_RIGHT) {
                    return mWidth - getLineMax(line);
                } else
                    if (align == android.text.Layout.Alignment.ALIGN_OPPOSITE) {
                        if (dir == android.text.Layout.DIR_RIGHT_TO_LEFT)
                            return 0;
                        else
                            return mWidth - getLineMax(line);

                    } else {
                        /* align == Alignment.ALIGN_CENTER */
                        int left = getParagraphLeft(line);
                        int right = getParagraphRight(line);
                        int max = ((int) (getLineMax(line))) & (~1);
                        return left + (((right - left) - max) / 2);
                    }



    }

    /**
     * Get the rightmost position that should be exposed for horizontal
     * scrolling on the specified line.
     */
    public float getLineRight(int line) {
        int dir = getParagraphDirection(line);
        android.text.Layout.Alignment align = getParagraphAlignment(line);
        if (align == android.text.Layout.Alignment.ALIGN_LEFT) {
            return getParagraphLeft(line) + getLineMax(line);
        } else
            if (align == android.text.Layout.Alignment.ALIGN_NORMAL) {
                if (dir == android.text.Layout.DIR_RIGHT_TO_LEFT)
                    return mWidth;
                else
                    return getParagraphLeft(line) + getLineMax(line);

            } else
                if (align == android.text.Layout.Alignment.ALIGN_RIGHT) {
                    return mWidth;
                } else
                    if (align == android.text.Layout.Alignment.ALIGN_OPPOSITE) {
                        if (dir == android.text.Layout.DIR_RIGHT_TO_LEFT)
                            return getLineMax(line);
                        else
                            return mWidth;

                    } else {
                        /* align == Alignment.ALIGN_CENTER */
                        int left = getParagraphLeft(line);
                        int right = getParagraphRight(line);
                        int max = ((int) (getLineMax(line))) & (~1);
                        return right - (((right - left) - max) / 2);
                    }



    }

    /**
     * Gets the unsigned horizontal extent of the specified line, including
     * leading margin indent, but excluding trailing whitespace.
     */
    public float getLineMax(int line) {
        float margin = getParagraphLeadingMargin(line);
        float signedExtent = getLineExtent(line, false);
        return margin + (signedExtent >= 0 ? signedExtent : -signedExtent);
    }

    /**
     * Gets the unsigned horizontal extent of the specified line, including
     * leading margin indent and trailing whitespace.
     */
    public float getLineWidth(int line) {
        float margin = getParagraphLeadingMargin(line);
        float signedExtent = getLineExtent(line, true);
        return margin + (signedExtent >= 0 ? signedExtent : -signedExtent);
    }

    /**
     * Like {@link #getLineExtent(int,TabStops,boolean)} but determines the
     * tab stops instead of using the ones passed in.
     *
     * @param line
     * 		the index of the line
     * @param full
     * 		whether to include trailing whitespace
     * @return the extent of the line
     */
    private float getLineExtent(int line, boolean full) {
        int start = getLineStart(line);
        int end = (full) ? getLineEnd(line) : getLineVisibleEnd(line);
        boolean hasTabs = getLineContainsTab(line);
        android.text.Layout.TabStops tabStops = null;
        if (hasTabs && (mText instanceof android.text.Spanned)) {
            // Just checking this line should be good enough, tabs should be
            // consistent across all lines in a paragraph.
            android.text.style.TabStopSpan[] tabs = android.text.Layout.getParagraphSpans(((android.text.Spanned) (mText)), start, end, android.text.style.TabStopSpan.class);
            if (tabs.length > 0) {
                tabStops = new android.text.Layout.TabStops(android.text.Layout.TAB_INCREMENT, tabs);// XXX should reuse

            }
        }
        android.text.Layout.Directions directions = getLineDirections(line);
        // Returned directions can actually be null
        if (directions == null) {
            return 0.0F;
        }
        int dir = getParagraphDirection(line);
        android.text.TextLine tl = android.text.TextLine.obtain();
        tl.set(mPaint, mText, start, end, dir, directions, hasTabs, tabStops);
        float width = tl.metrics(null);
        android.text.TextLine.recycle(tl);
        return width;
    }

    /**
     * Returns the signed horizontal extent of the specified line, excluding
     * leading margin.  If full is false, excludes trailing whitespace.
     *
     * @param line
     * 		the index of the line
     * @param tabStops
     * 		the tab stops, can be null if we know they're not used.
     * @param full
     * 		whether to include trailing whitespace
     * @return the extent of the text on this line
     */
    private float getLineExtent(int line, android.text.Layout.TabStops tabStops, boolean full) {
        int start = getLineStart(line);
        int end = (full) ? getLineEnd(line) : getLineVisibleEnd(line);
        boolean hasTabs = getLineContainsTab(line);
        android.text.Layout.Directions directions = getLineDirections(line);
        int dir = getParagraphDirection(line);
        android.text.TextLine tl = android.text.TextLine.obtain();
        tl.set(mPaint, mText, start, end, dir, directions, hasTabs, tabStops);
        float width = tl.metrics(null);
        android.text.TextLine.recycle(tl);
        return width;
    }

    /**
     * Get the line number corresponding to the specified vertical position.
     * If you ask for a position above 0, you get 0; if you ask for a position
     * below the bottom of the text, you get the last line.
     */
    // FIXME: It may be faster to do a linear search for layouts without many lines.
    public int getLineForVertical(int vertical) {
        int high = getLineCount();
        int low = -1;
        int guess;
        while ((high - low) > 1) {
            guess = (high + low) / 2;
            if (getLineTop(guess) > vertical)
                high = guess;
            else
                low = guess;

        } 
        if (low < 0)
            return 0;
        else
            return low;

    }

    /**
     * Get the line number on which the specified text offset appears.
     * If you ask for a position before 0, you get 0; if you ask for a position
     * beyond the end of the text, you get the last line.
     */
    public int getLineForOffset(int offset) {
        int high = getLineCount();
        int low = -1;
        int guess;
        while ((high - low) > 1) {
            guess = (high + low) / 2;
            if (getLineStart(guess) > offset)
                high = guess;
            else
                low = guess;

        } 
        if (low < 0)
            return 0;
        else
            return low;

    }

    /**
     * Get the character offset on the specified line whose position is
     * closest to the specified horizontal position.
     */
    public int getOffsetForHorizontal(int line, float horiz) {
        return getOffsetForHorizontal(line, horiz, true);
    }

    /**
     * Get the character offset on the specified line whose position is
     * closest to the specified horizontal position.
     *
     * @param line
     * 		the line used to find the closest offset
     * @param horiz
     * 		the horizontal position used to find the closest offset
     * @param primary
     * 		whether to use the primary position or secondary position to find the offset
     * @unknown 
     */
    public int getOffsetForHorizontal(int line, float horiz, boolean primary) {
        // TODO: use Paint.getOffsetForAdvance to avoid binary search
        final int lineEndOffset = getLineEnd(line);
        final int lineStartOffset = getLineStart(line);
        android.text.Layout.Directions dirs = getLineDirections(line);
        android.text.TextLine tl = android.text.TextLine.obtain();
        // XXX: we don't care about tabs as we just use TextLine#getOffsetToLeftRightOf here.
        tl.set(mPaint, mText, lineStartOffset, lineEndOffset, getParagraphDirection(line), dirs, false, null);
        final int max;
        if (line == (getLineCount() - 1)) {
            max = lineEndOffset;
        } else {
            max = tl.getOffsetToLeftRightOf(lineEndOffset - lineStartOffset, !isRtlCharAt(lineEndOffset - 1)) + lineStartOffset;
        }
        int best = lineStartOffset;
        float bestdist = java.lang.Math.abs(getHorizontal(best, primary) - horiz);
        for (int i = 0; i < dirs.mDirections.length; i += 2) {
            int here = lineStartOffset + dirs.mDirections[i];
            int there = here + (dirs.mDirections[i + 1] & android.text.Layout.RUN_LENGTH_MASK);
            boolean isRtl = (dirs.mDirections[i + 1] & android.text.Layout.RUN_RTL_FLAG) != 0;
            int swap = (isRtl) ? -1 : 1;
            if (there > max)
                there = max;

            int high = (there - 1) + 1;
            int low = (here + 1) - 1;
            int guess;
            while ((high - low) > 1) {
                guess = (high + low) / 2;
                int adguess = getOffsetAtStartOf(guess);
                if ((getHorizontal(adguess, primary) * swap) >= (horiz * swap))
                    high = guess;
                else
                    low = guess;

            } 
            if (low < (here + 1))
                low = here + 1;

            if (low < there) {
                int aft = tl.getOffsetToLeftRightOf(low - lineStartOffset, isRtl) + lineStartOffset;
                low = tl.getOffsetToLeftRightOf(aft - lineStartOffset, !isRtl) + lineStartOffset;
                if ((low >= here) && (low < there)) {
                    float dist = java.lang.Math.abs(getHorizontal(low, primary) - horiz);
                    if (aft < there) {
                        float other = java.lang.Math.abs(getHorizontal(aft, primary) - horiz);
                        if (other < dist) {
                            dist = other;
                            low = aft;
                        }
                    }
                    if (dist < bestdist) {
                        bestdist = dist;
                        best = low;
                    }
                }
            }
            float dist = java.lang.Math.abs(getHorizontal(here, primary) - horiz);
            if (dist < bestdist) {
                bestdist = dist;
                best = here;
            }
        }
        float dist = java.lang.Math.abs(getHorizontal(max, primary) - horiz);
        if (dist <= bestdist) {
            bestdist = dist;
            best = max;
        }
        android.text.TextLine.recycle(tl);
        return best;
    }

    /**
     * Return the text offset after the last character on the specified line.
     */
    public final int getLineEnd(int line) {
        return getLineStart(line + 1);
    }

    /**
     * Return the text offset after the last visible character (so whitespace
     * is not counted) on the specified line.
     */
    public int getLineVisibleEnd(int line) {
        return getLineVisibleEnd(line, getLineStart(line), getLineStart(line + 1));
    }

    private int getLineVisibleEnd(int line, int start, int end) {
        java.lang.CharSequence text = mText;
        char ch;
        if (line == (getLineCount() - 1)) {
            return end;
        }
        for (; end > start; end--) {
            ch = text.charAt(end - 1);
            if (ch == '\n') {
                return end - 1;
            }
            // Note: keep this in sync with Minikin LineBreaker::isLineEndSpace()
            if (!((((((ch == ' ') || (ch == '\t')) || (ch == 0x1680)) || (((0x2000 <= ch) && (ch <= 0x200a)) && (ch != 0x2007))) || (ch == 0x205f)) || (ch == 0x3000))) {
                break;
            }
        }
        return end;
    }

    /**
     * Return the vertical position of the bottom of the specified line.
     */
    public final int getLineBottom(int line) {
        return getLineTop(line + 1);
    }

    /**
     * Return the vertical position of the baseline of the specified line.
     */
    public final int getLineBaseline(int line) {
        // getLineTop(line+1) == getLineTop(line)
        return getLineTop(line + 1) - getLineDescent(line);
    }

    /**
     * Get the ascent of the text on the specified line.
     * The return value is negative to match the Paint.ascent() convention.
     */
    public final int getLineAscent(int line) {
        // getLineTop(line+1) - getLineDescent(line) == getLineBaseLine(line)
        return getLineTop(line) - (getLineTop(line + 1) - getLineDescent(line));
    }

    public int getOffsetToLeftOf(int offset) {
        return getOffsetToLeftRightOf(offset, true);
    }

    public int getOffsetToRightOf(int offset) {
        return getOffsetToLeftRightOf(offset, false);
    }

    private int getOffsetToLeftRightOf(int caret, boolean toLeft) {
        int line = getLineForOffset(caret);
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        int lineDir = getParagraphDirection(line);
        boolean lineChanged = false;
        boolean advance = toLeft == (lineDir == android.text.Layout.DIR_RIGHT_TO_LEFT);
        // if walking off line, look at the line we're headed to
        if (advance) {
            if (caret == lineEnd) {
                if (line < (getLineCount() - 1)) {
                    lineChanged = true;
                    ++line;
                } else {
                    return caret;// at very end, don't move

                }
            }
        } else {
            if (caret == lineStart) {
                if (line > 0) {
                    lineChanged = true;
                    --line;
                } else {
                    return caret;// at very start, don't move

                }
            }
        }
        if (lineChanged) {
            lineStart = getLineStart(line);
            lineEnd = getLineEnd(line);
            int newDir = getParagraphDirection(line);
            if (newDir != lineDir) {
                // unusual case.  we want to walk onto the line, but it runs
                // in a different direction than this one, so we fake movement
                // in the opposite direction.
                toLeft = !toLeft;
                lineDir = newDir;
            }
        }
        android.text.Layout.Directions directions = getLineDirections(line);
        android.text.TextLine tl = android.text.TextLine.obtain();
        // XXX: we don't care about tabs
        tl.set(mPaint, mText, lineStart, lineEnd, lineDir, directions, false, null);
        caret = lineStart + tl.getOffsetToLeftRightOf(caret - lineStart, toLeft);
        tl = android.text.TextLine.recycle(tl);
        return caret;
    }

    private int getOffsetAtStartOf(int offset) {
        // XXX this probably should skip local reorderings and
        // zero-width characters, look at callers
        if (offset == 0)
            return 0;

        java.lang.CharSequence text = mText;
        char c = text.charAt(offset);
        if ((c >= '\udc00') && (c <= '\udfff')) {
            char c1 = text.charAt(offset - 1);
            if ((c1 >= '\ud800') && (c1 <= '\udbff'))
                offset -= 1;

        }
        if (mSpannedText) {
            android.text.style.ReplacementSpan[] spans = ((android.text.Spanned) (text)).getSpans(offset, offset, android.text.style.ReplacementSpan.class);
            for (int i = 0; i < spans.length; i++) {
                int start = ((android.text.Spanned) (text)).getSpanStart(spans[i]);
                int end = ((android.text.Spanned) (text)).getSpanEnd(spans[i]);
                if ((start < offset) && (end > offset))
                    offset = start;

            }
        }
        return offset;
    }

    /**
     * Determine whether we should clamp cursor position. Currently it's
     * only robust for left-aligned displays.
     *
     * @unknown 
     */
    public boolean shouldClampCursor(int line) {
        // Only clamp cursor position in left-aligned displays.
        switch (getParagraphAlignment(line)) {
            case ALIGN_LEFT :
                return true;
            case ALIGN_NORMAL :
                return getParagraphDirection(line) > 0;
            default :
                return false;
        }
    }

    /**
     * Fills in the specified Path with a representation of a cursor
     * at the specified offset.  This will often be a vertical line
     * but can be multiple discontinuous lines in text with multiple
     * directionalities.
     */
    public void getCursorPath(int point, android.graphics.Path dest, java.lang.CharSequence editingBuffer) {
        dest.reset();
        int line = getLineForOffset(point);
        int top = getLineTop(line);
        int bottom = getLineTop(line + 1);
        boolean clamped = shouldClampCursor(line);
        float h1 = getPrimaryHorizontal(point, clamped) - 0.5F;
        float h2 = (isLevelBoundary(point)) ? getSecondaryHorizontal(point, clamped) - 0.5F : h1;
        int caps = android.text.method.TextKeyListener.getMetaState(editingBuffer, android.text.method.TextKeyListener.META_SHIFT_ON) | android.text.method.TextKeyListener.getMetaState(editingBuffer, android.text.method.TextKeyListener.META_SELECTING);
        int fn = android.text.method.TextKeyListener.getMetaState(editingBuffer, android.text.method.TextKeyListener.META_ALT_ON);
        int dist = 0;
        if ((caps != 0) || (fn != 0)) {
            dist = (bottom - top) >> 2;
            if (fn != 0)
                top += dist;

            if (caps != 0)
                bottom -= dist;

        }
        if (h1 < 0.5F)
            h1 = 0.5F;

        if (h2 < 0.5F)
            h2 = 0.5F;

        if (java.lang.Float.compare(h1, h2) == 0) {
            dest.moveTo(h1, top);
            dest.lineTo(h1, bottom);
        } else {
            dest.moveTo(h1, top);
            dest.lineTo(h1, (top + bottom) >> 1);
            dest.moveTo(h2, (top + bottom) >> 1);
            dest.lineTo(h2, bottom);
        }
        if (caps == 2) {
            dest.moveTo(h2, bottom);
            dest.lineTo(h2 - dist, bottom + dist);
            dest.lineTo(h2, bottom);
            dest.lineTo(h2 + dist, bottom + dist);
        } else
            if (caps == 1) {
                dest.moveTo(h2, bottom);
                dest.lineTo(h2 - dist, bottom + dist);
                dest.moveTo(h2 - dist, (bottom + dist) - 0.5F);
                dest.lineTo(h2 + dist, (bottom + dist) - 0.5F);
                dest.moveTo(h2 + dist, bottom + dist);
                dest.lineTo(h2, bottom);
            }

        if (fn == 2) {
            dest.moveTo(h1, top);
            dest.lineTo(h1 - dist, top - dist);
            dest.lineTo(h1, top);
            dest.lineTo(h1 + dist, top - dist);
        } else
            if (fn == 1) {
                dest.moveTo(h1, top);
                dest.lineTo(h1 - dist, top - dist);
                dest.moveTo(h1 - dist, (top - dist) + 0.5F);
                dest.lineTo(h1 + dist, (top - dist) + 0.5F);
                dest.moveTo(h1 + dist, top - dist);
                dest.lineTo(h1, top);
            }

    }

    private void addSelection(int line, int start, int end, int top, int bottom, android.graphics.Path dest) {
        int linestart = getLineStart(line);
        int lineend = getLineEnd(line);
        android.text.Layout.Directions dirs = getLineDirections(line);
        if ((lineend > linestart) && (mText.charAt(lineend - 1) == '\n'))
            lineend--;

        for (int i = 0; i < dirs.mDirections.length; i += 2) {
            int here = linestart + dirs.mDirections[i];
            int there = here + (dirs.mDirections[i + 1] & android.text.Layout.RUN_LENGTH_MASK);
            if (there > lineend)
                there = lineend;

            if ((start <= there) && (end >= here)) {
                int st = java.lang.Math.max(start, here);
                int en = java.lang.Math.min(end, there);
                if (st != en) {
                    float h1 = /* not clamped */
                    getHorizontal(st, false, line, false);
                    float h2 = /* not clamped */
                    getHorizontal(en, true, line, false);
                    float left = java.lang.Math.min(h1, h2);
                    float right = java.lang.Math.max(h1, h2);
                    dest.addRect(left, top, right, bottom, android.graphics.Path.Direction.CW);
                }
            }
        }
    }

    /**
     * Fills in the specified Path with a representation of a highlight
     * between the specified offsets.  This will often be a rectangle
     * or a potentially discontinuous set of rectangles.  If the start
     * and end are the same, the returned path is empty.
     */
    public void getSelectionPath(int start, int end, android.graphics.Path dest) {
        dest.reset();
        if (start == end)
            return;

        if (end < start) {
            int temp = end;
            end = start;
            start = temp;
        }
        int startline = getLineForOffset(start);
        int endline = getLineForOffset(end);
        int top = getLineTop(startline);
        int bottom = getLineBottom(endline);
        if (startline == endline) {
            addSelection(startline, start, end, top, bottom, dest);
        } else {
            final float width = mWidth;
            addSelection(startline, start, getLineEnd(startline), top, getLineBottom(startline), dest);
            if (getParagraphDirection(startline) == android.text.Layout.DIR_RIGHT_TO_LEFT)
                dest.addRect(getLineLeft(startline), top, 0, getLineBottom(startline), android.graphics.Path.Direction.CW);
            else
                dest.addRect(getLineRight(startline), top, width, getLineBottom(startline), android.graphics.Path.Direction.CW);

            for (int i = startline + 1; i < endline; i++) {
                top = getLineTop(i);
                bottom = getLineBottom(i);
                dest.addRect(0, top, width, bottom, android.graphics.Path.Direction.CW);
            }
            top = getLineTop(endline);
            bottom = getLineBottom(endline);
            addSelection(endline, getLineStart(endline), end, top, bottom, dest);
            if (getParagraphDirection(endline) == android.text.Layout.DIR_RIGHT_TO_LEFT)
                dest.addRect(width, top, getLineRight(endline), bottom, android.graphics.Path.Direction.CW);
            else
                dest.addRect(0, top, getLineLeft(endline), bottom, android.graphics.Path.Direction.CW);

        }
    }

    /**
     * Get the alignment of the specified paragraph, taking into account
     * markup attached to it.
     */
    public final android.text.Layout.Alignment getParagraphAlignment(int line) {
        android.text.Layout.Alignment align = mAlignment;
        if (mSpannedText) {
            android.text.Spanned sp = ((android.text.Spanned) (mText));
            android.text.style.AlignmentSpan[] spans = android.text.Layout.getParagraphSpans(sp, getLineStart(line), getLineEnd(line), android.text.style.AlignmentSpan.class);
            int spanLength = spans.length;
            if (spanLength > 0) {
                align = spans[spanLength - 1].getAlignment();
            }
        }
        return align;
    }

    /**
     * Get the left edge of the specified paragraph, inset by left margins.
     */
    public final int getParagraphLeft(int line) {
        int left = 0;
        int dir = getParagraphDirection(line);
        if ((dir == android.text.Layout.DIR_RIGHT_TO_LEFT) || (!mSpannedText)) {
            return left;// leading margin has no impact, or no styles

        }
        return getParagraphLeadingMargin(line);
    }

    /**
     * Get the right edge of the specified paragraph, inset by right margins.
     */
    public final int getParagraphRight(int line) {
        int right = mWidth;
        int dir = getParagraphDirection(line);
        if ((dir == android.text.Layout.DIR_LEFT_TO_RIGHT) || (!mSpannedText)) {
            return right;// leading margin has no impact, or no styles

        }
        return right - getParagraphLeadingMargin(line);
    }

    /**
     * Returns the effective leading margin (unsigned) for this line,
     * taking into account LeadingMarginSpan and LeadingMarginSpan2.
     *
     * @param line
     * 		the line index
     * @return the leading margin of this line
     */
    private int getParagraphLeadingMargin(int line) {
        if (!mSpannedText) {
            return 0;
        }
        android.text.Spanned spanned = ((android.text.Spanned) (mText));
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        int spanEnd = spanned.nextSpanTransition(lineStart, lineEnd, android.text.style.LeadingMarginSpan.class);
        android.text.style.LeadingMarginSpan[] spans = android.text.Layout.getParagraphSpans(spanned, lineStart, spanEnd, android.text.style.LeadingMarginSpan.class);
        if (spans.length == 0) {
            return 0;// no leading margin span;

        }
        int margin = 0;
        boolean isFirstParaLine = (lineStart == 0) || (spanned.charAt(lineStart - 1) == '\n');
        boolean useFirstLineMargin = isFirstParaLine;
        for (int i = 0; i < spans.length; i++) {
            if (spans[i] instanceof android.text.style.LeadingMarginSpan.LeadingMarginSpan2) {
                int spStart = spanned.getSpanStart(spans[i]);
                int spanLine = getLineForOffset(spStart);
                int count = ((android.text.style.LeadingMarginSpan.LeadingMarginSpan2) (spans[i])).getLeadingMarginLineCount();
                // if there is more than one LeadingMarginSpan2, use the count that is greatest
                useFirstLineMargin |= line < (spanLine + count);
            }
        }
        for (int i = 0; i < spans.length; i++) {
            android.text.style.LeadingMarginSpan span = spans[i];
            margin += span.getLeadingMargin(useFirstLineMargin);
        }
        return margin;
    }

    /* package */
    static float measurePara(android.text.TextPaint paint, java.lang.CharSequence text, int start, int end) {
        android.text.MeasuredText mt = android.text.MeasuredText.obtain();
        android.text.TextLine tl = android.text.TextLine.obtain();
        try {
            mt.setPara(text, start, end, android.text.TextDirectionHeuristics.LTR, null);
            android.text.Layout.Directions directions;
            int dir;
            if (mt.mEasy) {
                directions = android.text.Layout.DIRS_ALL_LEFT_TO_RIGHT;
                dir = android.text.Layout.DIR_LEFT_TO_RIGHT;
            } else {
                directions = android.text.AndroidBidi.directions(mt.mDir, mt.mLevels, 0, mt.mChars, 0, mt.mLen);
                dir = mt.mDir;
            }
            char[] chars = mt.mChars;
            int len = mt.mLen;
            boolean hasTabs = false;
            android.text.Layout.TabStops tabStops = null;
            // leading margins should be taken into account when measuring a paragraph
            int margin = 0;
            if (text instanceof android.text.Spanned) {
                android.text.Spanned spanned = ((android.text.Spanned) (text));
                android.text.style.LeadingMarginSpan[] spans = android.text.Layout.getParagraphSpans(spanned, start, end, android.text.style.LeadingMarginSpan.class);
                for (android.text.style.LeadingMarginSpan lms : spans) {
                    margin += lms.getLeadingMargin(true);
                }
            }
            for (int i = 0; i < len; ++i) {
                if (chars[i] == '\t') {
                    hasTabs = true;
                    if (text instanceof android.text.Spanned) {
                        android.text.Spanned spanned = ((android.text.Spanned) (text));
                        int spanEnd = spanned.nextSpanTransition(start, end, android.text.style.TabStopSpan.class);
                        android.text.style.TabStopSpan[] spans = android.text.Layout.getParagraphSpans(spanned, start, spanEnd, android.text.style.TabStopSpan.class);
                        if (spans.length > 0) {
                            tabStops = new android.text.Layout.TabStops(android.text.Layout.TAB_INCREMENT, spans);
                        }
                    }
                    break;
                }
            }
            tl.set(paint, text, start, end, dir, directions, hasTabs, tabStops);
            return margin + tl.metrics(null);
        } finally {
            android.text.TextLine.recycle(tl);
            android.text.MeasuredText.recycle(mt);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    /* package */
    static class TabStops {
        private int[] mStops;

        private int mNumStops;

        private int mIncrement;

        TabStops(int increment, java.lang.Object[] spans) {
            reset(increment, spans);
        }

        void reset(int increment, java.lang.Object[] spans) {
            this.mIncrement = increment;
            int ns = 0;
            if (spans != null) {
                int[] stops = this.mStops;
                for (java.lang.Object o : spans) {
                    if (o instanceof android.text.style.TabStopSpan) {
                        if (stops == null) {
                            stops = new int[10];
                        } else
                            if (ns == stops.length) {
                                int[] nstops = new int[ns * 2];
                                for (int i = 0; i < ns; ++i) {
                                    nstops[i] = stops[i];
                                }
                                stops = nstops;
                            }

                        stops[ns++] = ((android.text.style.TabStopSpan) (o)).getTabStop();
                    }
                }
                if (ns > 1) {
                    java.util.Arrays.sort(stops, 0, ns);
                }
                if (stops != this.mStops) {
                    this.mStops = stops;
                }
            }
            this.mNumStops = ns;
        }

        float nextTab(float h) {
            int ns = this.mNumStops;
            if (ns > 0) {
                int[] stops = this.mStops;
                for (int i = 0; i < ns; ++i) {
                    int stop = stops[i];
                    if (stop > h) {
                        return stop;
                    }
                }
            }
            return android.text.Layout.TabStops.nextDefaultStop(h, mIncrement);
        }

        public static float nextDefaultStop(float h, int inc) {
            return ((int) ((h + inc) / inc)) * inc;
        }
    }

    /**
     * Returns the position of the next tab stop after h on the line.
     *
     * @param text
     * 		the text
     * @param start
     * 		start of the line
     * @param end
     * 		limit of the line
     * @param h
     * 		the current horizontal offset
     * @param tabs
     * 		the tabs, can be null.  If it is null, any tabs in effect
     * 		on the line will be used.  If there are no tabs, a default offset
     * 		will be used to compute the tab stop.
     * @return the offset of the next tab stop.
     */
    /* package */
    static float nextTab(java.lang.CharSequence text, int start, int end, float h, java.lang.Object[] tabs) {
        float nh = java.lang.Float.MAX_VALUE;
        boolean alltabs = false;
        if (text instanceof android.text.Spanned) {
            if (tabs == null) {
                tabs = android.text.Layout.getParagraphSpans(((android.text.Spanned) (text)), start, end, android.text.style.TabStopSpan.class);
                alltabs = true;
            }
            for (int i = 0; i < tabs.length; i++) {
                if (!alltabs) {
                    if (!(tabs[i] instanceof android.text.style.TabStopSpan))
                        continue;

                }
                int where = ((android.text.style.TabStopSpan) (tabs[i])).getTabStop();
                if ((where < nh) && (where > h))
                    nh = where;

            }
            if (nh != java.lang.Float.MAX_VALUE)
                return nh;

        }
        return ((int) ((h + android.text.Layout.TAB_INCREMENT) / android.text.Layout.TAB_INCREMENT)) * android.text.Layout.TAB_INCREMENT;
    }

    protected final boolean isSpanned() {
        return mSpannedText;
    }

    /**
     * Returns the same as <code>text.getSpans()</code>, except where
     * <code>start</code> and <code>end</code> are the same and are not
     * at the very beginning of the text, in which case an empty array
     * is returned instead.
     * <p>
     * This is needed because of the special case that <code>getSpans()</code>
     * on an empty range returns the spans adjacent to that range, which is
     * primarily for the sake of <code>TextWatchers</code> so they will get
     * notifications when text goes from empty to non-empty.  But it also
     * has the unfortunate side effect that if the text ends with an empty
     * paragraph, that paragraph accidentally picks up the styles of the
     * preceding paragraph (even though those styles will not be picked up
     * by new text that is inserted into the empty paragraph).
     * <p>
     * The reason it just checks whether <code>start</code> and <code>end</code>
     * is the same is that the only time a line can contain 0 characters
     * is if it is the final paragraph of the Layout; otherwise any line will
     * contain at least one printing or newline character.  The reason for the
     * additional check if <code>start</code> is greater than 0 is that
     * if the empty paragraph is the entire content of the buffer, paragraph
     * styles that are already applied to the buffer will apply to text that
     * is inserted into it.
     */
    /* package */
    static <T> T[] getParagraphSpans(android.text.Spanned text, int start, int end, java.lang.Class<T> type) {
        if ((start == end) && (start > 0)) {
            return com.android.internal.util.ArrayUtils.emptyArray(type);
        }
        if (text instanceof android.text.SpannableStringBuilder) {
            return ((android.text.SpannableStringBuilder) (text)).getSpans(start, end, type, false);
        } else {
            return text.getSpans(start, end, type);
        }
    }

    private char getEllipsisChar(android.text.TextUtils.TruncateAt method) {
        return method == android.text.TextUtils.TruncateAt.END_SMALL ? android.text.TextUtils.ELLIPSIS_TWO_DOTS[0] : android.text.TextUtils.ELLIPSIS_NORMAL[0];
    }

    private void ellipsize(int start, int end, int line, char[] dest, int destoff, android.text.TextUtils.TruncateAt method) {
        int ellipsisCount = getEllipsisCount(line);
        if (ellipsisCount == 0) {
            return;
        }
        int ellipsisStart = getEllipsisStart(line);
        int linestart = getLineStart(line);
        for (int i = ellipsisStart; i < (ellipsisStart + ellipsisCount); i++) {
            char c;
            if (i == ellipsisStart) {
                c = getEllipsisChar(method);// ellipsis

            } else {
                c = '\ufeff';// 0-width space

            }
            int a = i + linestart;
            if ((a >= start) && (a < end)) {
                dest[(destoff + a) - start] = c;
            }
        }
    }

    /**
     * Stores information about bidirectional (left-to-right or right-to-left)
     * text within the layout of a line.
     */
    public static class Directions {
        // Directions represents directional runs within a line of text.
        // Runs are pairs of ints listed in visual order, starting from the
        // leading margin.  The first int of each pair is the offset from
        // the first character of the line to the start of the run.  The
        // second int represents both the length and level of the run.
        // The length is in the lower bits, accessed by masking with
        // DIR_LENGTH_MASK.  The level is in the higher bits, accessed
        // by shifting by DIR_LEVEL_SHIFT and masking by DIR_LEVEL_MASK.
        // To simply test for an RTL direction, test the bit using
        // DIR_RTL_FLAG, if set then the direction is rtl.
        /* package */
        int[] mDirections;

        /* package */
        Directions(int[] dirs) {
            mDirections = dirs;
        }
    }

    /**
     * Return the offset of the first character to be ellipsized away,
     * relative to the start of the line.  (So 0 if the beginning of the
     * line is ellipsized, not getLineStart().)
     */
    public abstract int getEllipsisStart(int line);

    /**
     * Returns the number of characters to be ellipsized away, or 0 if
     * no ellipsis is to take place.
     */
    public abstract int getEllipsisCount(int line);

    /* package */
    static class Ellipsizer implements android.text.GetChars , java.lang.CharSequence {
        /* package */
        java.lang.CharSequence mText;

        /* package */
        android.text.Layout mLayout;

        /* package */
        int mWidth;

        /* package */
        android.text.TextUtils.TruncateAt mMethod;

        public Ellipsizer(java.lang.CharSequence s) {
            mText = s;
        }

        public char charAt(int off) {
            char[] buf = android.text.TextUtils.obtain(1);
            getChars(off, off + 1, buf, 0);
            char ret = buf[0];
            android.text.TextUtils.recycle(buf);
            return ret;
        }

        public void getChars(int start, int end, char[] dest, int destoff) {
            int line1 = mLayout.getLineForOffset(start);
            int line2 = mLayout.getLineForOffset(end);
            android.text.TextUtils.getChars(mText, start, end, dest, destoff);
            for (int i = line1; i <= line2; i++) {
                mLayout.ellipsize(start, end, i, dest, destoff, mMethod);
            }
        }

        public int length() {
            return mText.length();
        }

        public java.lang.CharSequence subSequence(int start, int end) {
            char[] s = new char[end - start];
            getChars(start, end, s, 0);
            return new java.lang.String(s);
        }

        @java.lang.Override
        public java.lang.String toString() {
            char[] s = new char[length()];
            getChars(0, length(), s, 0);
            return new java.lang.String(s);
        }
    }

    /* package */
    static class SpannedEllipsizer extends android.text.Layout.Ellipsizer implements android.text.Spanned {
        private android.text.Spanned mSpanned;

        public SpannedEllipsizer(java.lang.CharSequence display) {
            super(display);
            mSpanned = ((android.text.Spanned) (display));
        }

        public <T> T[] getSpans(int start, int end, java.lang.Class<T> type) {
            return mSpanned.getSpans(start, end, type);
        }

        public int getSpanStart(java.lang.Object tag) {
            return mSpanned.getSpanStart(tag);
        }

        public int getSpanEnd(java.lang.Object tag) {
            return mSpanned.getSpanEnd(tag);
        }

        public int getSpanFlags(java.lang.Object tag) {
            return mSpanned.getSpanFlags(tag);
        }

        @java.lang.SuppressWarnings("rawtypes")
        public int nextSpanTransition(int start, int limit, java.lang.Class type) {
            return mSpanned.nextSpanTransition(start, limit, type);
        }

        @java.lang.Override
        public java.lang.CharSequence subSequence(int start, int end) {
            char[] s = new char[end - start];
            getChars(start, end, s, 0);
            android.text.SpannableString ss = new android.text.SpannableString(new java.lang.String(s));
            android.text.TextUtils.copySpansFrom(mSpanned, start, end, java.lang.Object.class, ss, 0);
            return ss;
        }
    }

    private java.lang.CharSequence mText;

    private android.text.TextPaint mPaint;

    private int mWidth;

    private android.text.Layout.Alignment mAlignment = android.text.Layout.Alignment.ALIGN_NORMAL;

    private float mSpacingMult;

    private float mSpacingAdd;

    private static final android.graphics.Rect sTempRect = new android.graphics.Rect();

    private boolean mSpannedText;

    private android.text.TextDirectionHeuristic mTextDir;

    private android.text.SpanSet<android.text.style.LineBackgroundSpan> mLineBackgroundSpans;

    public static final int DIR_LEFT_TO_RIGHT = 1;

    public static final int DIR_RIGHT_TO_LEFT = -1;

    /* package */
    static final int DIR_REQUEST_LTR = 1;

    /* package */
    static final int DIR_REQUEST_RTL = -1;

    /* package */
    static final int DIR_REQUEST_DEFAULT_LTR = 2;

    /* package */
    static final int DIR_REQUEST_DEFAULT_RTL = -2;

    /* package */
    static final int RUN_LENGTH_MASK = 0x3ffffff;

    /* package */
    static final int RUN_LEVEL_SHIFT = 26;

    /* package */
    static final int RUN_LEVEL_MASK = 0x3f;

    /* package */
    static final int RUN_RTL_FLAG = 1 << android.text.Layout.RUN_LEVEL_SHIFT;

    public enum Alignment {

        ALIGN_NORMAL,
        ALIGN_OPPOSITE,
        ALIGN_CENTER,
        /**
         *
         *
         * @unknown 
         */
        ALIGN_LEFT,
        /**
         *
         *
         * @unknown 
         */
        ALIGN_RIGHT;}

    private static final int TAB_INCREMENT = 20;

    /* package */
    static final android.text.Layout.Directions DIRS_ALL_LEFT_TO_RIGHT = new android.text.Layout.Directions(new int[]{ 0, android.text.Layout.RUN_LENGTH_MASK });

    /* package */
    static final android.text.Layout.Directions DIRS_ALL_RIGHT_TO_LEFT = new android.text.Layout.Directions(new int[]{ 0, android.text.Layout.RUN_LENGTH_MASK | android.text.Layout.RUN_RTL_FLAG });
}

