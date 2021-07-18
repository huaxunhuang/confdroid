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
 * This is the class for text whose content and markup can both be changed.
 */
public class SpannableStringBuilder implements android.text.Editable , android.text.GetChars , android.text.GraphicsOperations , android.text.Spannable , java.lang.Appendable , java.lang.CharSequence {
    private static final java.lang.String TAG = "SpannableStringBuilder";

    /**
     * Create a new SpannableStringBuilder with empty contents
     */
    public SpannableStringBuilder() {
        this("");
    }

    /**
     * Create a new SpannableStringBuilder containing a copy of the
     * specified text, including its spans if any.
     */
    public SpannableStringBuilder(java.lang.CharSequence text) {
        this(text, 0, text.length());
    }

    /**
     * Create a new SpannableStringBuilder containing a copy of the
     * specified slice of the specified text, including its spans if any.
     */
    public SpannableStringBuilder(java.lang.CharSequence text, int start, int end) {
        int srclen = end - start;
        if (srclen < 0)
            throw new java.lang.StringIndexOutOfBoundsException();

        mText = com.android.internal.util.ArrayUtils.newUnpaddedCharArray(com.android.internal.util.GrowingArrayUtils.growSize(srclen));
        mGapStart = srclen;
        mGapLength = mText.length - srclen;
        android.text.TextUtils.getChars(text, start, end, mText, 0);
        mSpanCount = 0;
        mSpanInsertCount = 0;
        mSpans = libcore.util.EmptyArray.OBJECT;
        mSpanStarts = libcore.util.EmptyArray.INT;
        mSpanEnds = libcore.util.EmptyArray.INT;
        mSpanFlags = libcore.util.EmptyArray.INT;
        mSpanMax = libcore.util.EmptyArray.INT;
        mSpanOrder = libcore.util.EmptyArray.INT;
        mPrioSortBuffer = libcore.util.EmptyArray.INT;
        mOrderSortBuffer = libcore.util.EmptyArray.INT;
        if (text instanceof android.text.Spanned) {
            android.text.Spanned sp = ((android.text.Spanned) (text));
            java.lang.Object[] spans = sp.getSpans(start, end, java.lang.Object.class);
            for (int i = 0; i < spans.length; i++) {
                if (spans[i] instanceof android.text.NoCopySpan) {
                    continue;
                }
                int st = sp.getSpanStart(spans[i]) - start;
                int en = sp.getSpanEnd(spans[i]) - start;
                int fl = sp.getSpanFlags(spans[i]);
                if (st < 0)
                    st = 0;

                if (st > (end - start))
                    st = end - start;

                if (en < 0)
                    en = 0;

                if (en > (end - start))
                    en = end - start;

                setSpan(false, spans[i], st, en, fl);
            }
            restoreInvariants();
        }
    }

    public static android.text.SpannableStringBuilder valueOf(java.lang.CharSequence source) {
        if (source instanceof android.text.SpannableStringBuilder) {
            return ((android.text.SpannableStringBuilder) (source));
        } else {
            return new android.text.SpannableStringBuilder(source);
        }
    }

    /**
     * Return the char at the specified offset within the buffer.
     */
    public char charAt(int where) {
        int len = length();
        if (where < 0) {
            throw new java.lang.IndexOutOfBoundsException(("charAt: " + where) + " < 0");
        } else
            if (where >= len) {
                throw new java.lang.IndexOutOfBoundsException((("charAt: " + where) + " >= length ") + len);
            }

        if (where >= mGapStart)
            return mText[where + mGapLength];
        else
            return mText[where];

    }

    /**
     * Return the number of chars in the buffer.
     */
    public int length() {
        return mText.length - mGapLength;
    }

    private void resizeFor(int size) {
        final int oldLength = mText.length;
        if ((size + 1) <= oldLength) {
            return;
        }
        char[] newText = com.android.internal.util.ArrayUtils.newUnpaddedCharArray(com.android.internal.util.GrowingArrayUtils.growSize(size));
        java.lang.System.arraycopy(mText, 0, newText, 0, mGapStart);
        final int newLength = newText.length;
        final int delta = newLength - oldLength;
        final int after = oldLength - (mGapStart + mGapLength);
        java.lang.System.arraycopy(mText, oldLength - after, newText, newLength - after, after);
        mText = newText;
        mGapLength += delta;
        if (mGapLength < 1)
            new java.lang.Exception("mGapLength < 1").printStackTrace();

        if (mSpanCount != 0) {
            for (int i = 0; i < mSpanCount; i++) {
                if (mSpanStarts[i] > mGapStart)
                    mSpanStarts[i] += delta;

                if (mSpanEnds[i] > mGapStart)
                    mSpanEnds[i] += delta;

            }
            calcMax(treeRoot());
        }
    }

    private void moveGapTo(int where) {
        if (where == mGapStart)
            return;

        boolean atEnd = where == length();
        if (where < mGapStart) {
            int overlap = mGapStart - where;
            java.lang.System.arraycopy(mText, where, mText, (mGapStart + mGapLength) - overlap, overlap);
        } else {
            int overlap = where - mGapStart;
            java.lang.System.arraycopy(mText, (where + mGapLength) - overlap, mText, mGapStart, overlap);
        }
        // TODO: be more clever (although the win really isn't that big)
        if (mSpanCount != 0) {
            for (int i = 0; i < mSpanCount; i++) {
                int start = mSpanStarts[i];
                int end = mSpanEnds[i];
                if (start > mGapStart)
                    start -= mGapLength;

                if (start > where)
                    start += mGapLength;
                else
                    if (start == where) {
                        int flag = (mSpanFlags[i] & android.text.SpannableStringBuilder.START_MASK) >> android.text.SpannableStringBuilder.START_SHIFT;
                        if ((flag == android.text.SpannableStringBuilder.POINT) || (atEnd && (flag == android.text.SpannableStringBuilder.PARAGRAPH)))
                            start += mGapLength;

                    }

                if (end > mGapStart)
                    end -= mGapLength;

                if (end > where)
                    end += mGapLength;
                else
                    if (end == where) {
                        int flag = mSpanFlags[i] & android.text.SpannableStringBuilder.END_MASK;
                        if ((flag == android.text.SpannableStringBuilder.POINT) || (atEnd && (flag == android.text.SpannableStringBuilder.PARAGRAPH)))
                            end += mGapLength;

                    }

                mSpanStarts[i] = start;
                mSpanEnds[i] = end;
            }
            calcMax(treeRoot());
        }
        mGapStart = where;
    }

    // Documentation from interface
    public android.text.SpannableStringBuilder insert(int where, java.lang.CharSequence tb, int start, int end) {
        return replace(where, where, tb, start, end);
    }

    // Documentation from interface
    public android.text.SpannableStringBuilder insert(int where, java.lang.CharSequence tb) {
        return replace(where, where, tb, 0, tb.length());
    }

    // Documentation from interface
    public android.text.SpannableStringBuilder delete(int start, int end) {
        android.text.SpannableStringBuilder ret = replace(start, end, "", 0, 0);
        if (mGapLength > (2 * length()))
            resizeFor(length());

        return ret;// == this

    }

    // Documentation from interface
    public void clear() {
        replace(0, length(), "", 0, 0);
        mSpanInsertCount = 0;
    }

    // Documentation from interface
    public void clearSpans() {
        for (int i = mSpanCount - 1; i >= 0; i--) {
            java.lang.Object what = mSpans[i];
            int ostart = mSpanStarts[i];
            int oend = mSpanEnds[i];
            if (ostart > mGapStart)
                ostart -= mGapLength;

            if (oend > mGapStart)
                oend -= mGapLength;

            mSpanCount = i;
            mSpans[i] = null;
            sendSpanRemoved(what, ostart, oend);
        }
        if (mIndexOfSpan != null) {
            mIndexOfSpan.clear();
        }
        mSpanInsertCount = 0;
    }

    // Documentation from interface
    public android.text.SpannableStringBuilder append(java.lang.CharSequence text) {
        int length = length();
        return replace(length, length, text, 0, text.length());
    }

    /**
     * Appends the character sequence {@code text} and spans {@code what} over the appended part.
     * See {@link Spanned} for an explanation of what the flags mean.
     *
     * @param text
     * 		the character sequence to append.
     * @param what
     * 		the object to be spanned over the appended text.
     * @param flags
     * 		see {@link Spanned}.
     * @return this {@code SpannableStringBuilder}.
     */
    public android.text.SpannableStringBuilder append(java.lang.CharSequence text, java.lang.Object what, int flags) {
        int start = length();
        append(text);
        setSpan(what, start, length(), flags);
        return this;
    }

    // Documentation from interface
    public android.text.SpannableStringBuilder append(java.lang.CharSequence text, int start, int end) {
        int length = length();
        return replace(length, length, text, start, end);
    }

    // Documentation from interface
    public android.text.SpannableStringBuilder append(char text) {
        return append(java.lang.String.valueOf(text));
    }

    // Returns true if a node was removed (so we can restart search from root)
    private boolean removeSpansForChange(int start, int end, boolean textIsRemoved, int i) {
        if ((i & 1) != 0) {
            // internal tree node
            if ((resolveGap(mSpanMax[i]) >= start) && removeSpansForChange(start, end, textIsRemoved, android.text.SpannableStringBuilder.leftChild(i))) {
                return true;
            }
        }
        if (i < mSpanCount) {
            if (((((((mSpanFlags[i] & android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) == android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) && (mSpanStarts[i] >= start)) && (mSpanStarts[i] < (mGapStart + mGapLength))) && (mSpanEnds[i] >= start)) && (mSpanEnds[i] < (mGapStart + mGapLength))) && // The following condition indicates that the span would become empty
            ((textIsRemoved || (mSpanStarts[i] > start)) || (mSpanEnds[i] < mGapStart))) {
                mIndexOfSpan.remove(mSpans[i]);
                removeSpan(i);
                return true;
            }
            return ((resolveGap(mSpanStarts[i]) <= end) && ((i & 1) != 0)) && removeSpansForChange(start, end, textIsRemoved, android.text.SpannableStringBuilder.rightChild(i));
        }
        return false;
    }

    private void change(int start, int end, java.lang.CharSequence cs, int csStart, int csEnd) {
        // Can be negative
        final int replacedLength = end - start;
        final int replacementLength = csEnd - csStart;
        final int nbNewChars = replacementLength - replacedLength;
        boolean changed = false;
        for (int i = mSpanCount - 1; i >= 0; i--) {
            int spanStart = mSpanStarts[i];
            if (spanStart > mGapStart)
                spanStart -= mGapLength;

            int spanEnd = mSpanEnds[i];
            if (spanEnd > mGapStart)
                spanEnd -= mGapLength;

            if ((mSpanFlags[i] & android.text.Spanned.SPAN_PARAGRAPH) == android.text.Spanned.SPAN_PARAGRAPH) {
                int ost = spanStart;
                int oen = spanEnd;
                int clen = length();
                if ((spanStart > start) && (spanStart <= end)) {
                    for (spanStart = end; spanStart < clen; spanStart++)
                        if ((spanStart > end) && (charAt(spanStart - 1) == '\n'))
                            break;


                }
                if ((spanEnd > start) && (spanEnd <= end)) {
                    for (spanEnd = end; spanEnd < clen; spanEnd++)
                        if ((spanEnd > end) && (charAt(spanEnd - 1) == '\n'))
                            break;


                }
                if ((spanStart != ost) || (spanEnd != oen)) {
                    setSpan(false, mSpans[i], spanStart, spanEnd, mSpanFlags[i]);
                    changed = true;
                }
            }
            int flags = 0;
            if (spanStart == start)
                flags |= android.text.SpannableStringBuilder.SPAN_START_AT_START;
            else
                if (spanStart == (end + nbNewChars))
                    flags |= android.text.SpannableStringBuilder.SPAN_START_AT_END;


            if (spanEnd == start)
                flags |= android.text.SpannableStringBuilder.SPAN_END_AT_START;
            else
                if (spanEnd == (end + nbNewChars))
                    flags |= android.text.SpannableStringBuilder.SPAN_END_AT_END;


            mSpanFlags[i] |= flags;
        }
        if (changed) {
            restoreInvariants();
        }
        moveGapTo(end);
        if (nbNewChars >= mGapLength) {
            resizeFor((mText.length + nbNewChars) - mGapLength);
        }
        final boolean textIsRemoved = replacementLength == 0;
        // The removal pass needs to be done before the gap is updated in order to broadcast the
        // correct previous positions to the correct intersecting SpanWatchers
        if (replacedLength > 0) {
            // no need for span fixup on pure insertion
            while ((mSpanCount > 0) && removeSpansForChange(start, end, textIsRemoved, treeRoot())) {
                // keep deleting spans as needed, and restart from root after every deletion
                // because deletion can invalidate an index.
            } 
        }
        mGapStart += nbNewChars;
        mGapLength -= nbNewChars;
        if (mGapLength < 1)
            new java.lang.Exception("mGapLength < 1").printStackTrace();

        android.text.TextUtils.getChars(cs, csStart, csEnd, mText, start);
        if (replacedLength > 0) {
            // no need for span fixup on pure insertion
            // TODO potential optimization: only update bounds on intersecting spans
            final boolean atEnd = (mGapStart + mGapLength) == mText.length;
            for (int i = 0; i < mSpanCount; i++) {
                final int startFlag = (mSpanFlags[i] & android.text.SpannableStringBuilder.START_MASK) >> android.text.SpannableStringBuilder.START_SHIFT;
                mSpanStarts[i] = updatedIntervalBound(mSpanStarts[i], start, nbNewChars, startFlag, atEnd, textIsRemoved);
                final int endFlag = mSpanFlags[i] & android.text.SpannableStringBuilder.END_MASK;
                mSpanEnds[i] = updatedIntervalBound(mSpanEnds[i], start, nbNewChars, endFlag, atEnd, textIsRemoved);
            }
            // TODO potential optimization: only fix up invariants when bounds actually changed
            restoreInvariants();
        }
        if (cs instanceof android.text.Spanned) {
            android.text.Spanned sp = ((android.text.Spanned) (cs));
            java.lang.Object[] spans = sp.getSpans(csStart, csEnd, java.lang.Object.class);
            for (int i = 0; i < spans.length; i++) {
                int st = sp.getSpanStart(spans[i]);
                int en = sp.getSpanEnd(spans[i]);
                if (st < csStart)
                    st = csStart;

                if (en > csEnd)
                    en = csEnd;

                // Add span only if this object is not yet used as a span in this string
                if (getSpanStart(spans[i]) < 0) {
                    int copySpanStart = (st - csStart) + start;
                    int copySpanEnd = (en - csStart) + start;
                    int copySpanFlags = sp.getSpanFlags(spans[i]) | android.text.SpannableStringBuilder.SPAN_ADDED;
                    int flagsStart = (copySpanFlags & android.text.SpannableStringBuilder.START_MASK) >> android.text.SpannableStringBuilder.START_SHIFT;
                    int flagsEnd = copySpanFlags & android.text.SpannableStringBuilder.END_MASK;
                    if ((!isInvalidParagraphStart(copySpanStart, flagsStart)) && (!isInvalidParagraphEnd(copySpanEnd, flagsEnd))) {
                        setSpan(false, spans[i], copySpanStart, copySpanEnd, copySpanFlags);
                    }
                }
            }
            restoreInvariants();
        }
    }

    private int updatedIntervalBound(int offset, int start, int nbNewChars, int flag, boolean atEnd, boolean textIsRemoved) {
        if ((offset >= start) && (offset < (mGapStart + mGapLength))) {
            if (flag == android.text.SpannableStringBuilder.POINT) {
                // A POINT located inside the replaced range should be moved to the end of the
                // replaced text.
                // The exception is when the point is at the start of the range and we are doing a
                // text replacement (as opposed to a deletion): the point stays there.
                if (textIsRemoved || (offset > start)) {
                    return mGapStart + mGapLength;
                }
            } else {
                if (flag == android.text.SpannableStringBuilder.PARAGRAPH) {
                    if (atEnd) {
                        return mGapStart + mGapLength;
                    }
                } else {
                    // MARK
                    // MARKs should be moved to the start, with the exception of a mark located at
                    // the end of the range (which will be < mGapStart + mGapLength since mGapLength
                    // is > 0, which should stay 'unchanged' at the end of the replaced text.
                    if (textIsRemoved || (offset < (mGapStart - nbNewChars))) {
                        return start;
                    } else {
                        // Move to the end of replaced text (needed if nbNewChars != 0)
                        return mGapStart;
                    }
                }
            }
        }
        return offset;
    }

    // Note: caller is responsible for removing the mIndexOfSpan entry.
    private void removeSpan(int i) {
        java.lang.Object object = mSpans[i];
        int start = mSpanStarts[i];
        int end = mSpanEnds[i];
        if (start > mGapStart)
            start -= mGapLength;

        if (end > mGapStart)
            end -= mGapLength;

        int count = mSpanCount - (i + 1);
        java.lang.System.arraycopy(mSpans, i + 1, mSpans, i, count);
        java.lang.System.arraycopy(mSpanStarts, i + 1, mSpanStarts, i, count);
        java.lang.System.arraycopy(mSpanEnds, i + 1, mSpanEnds, i, count);
        java.lang.System.arraycopy(mSpanFlags, i + 1, mSpanFlags, i, count);
        java.lang.System.arraycopy(mSpanOrder, i + 1, mSpanOrder, i, count);
        mSpanCount--;
        invalidateIndex(i);
        mSpans[mSpanCount] = null;
        // Invariants must be restored before sending span removed notifications.
        restoreInvariants();
        sendSpanRemoved(object, start, end);
    }

    // Documentation from interface
    public android.text.SpannableStringBuilder replace(int start, int end, java.lang.CharSequence tb) {
        return replace(start, end, tb, 0, tb.length());
    }

    // Documentation from interface
    public android.text.SpannableStringBuilder replace(final int start, final int end, java.lang.CharSequence tb, int tbstart, int tbend) {
        checkRange("replace", start, end);
        int filtercount = mFilters.length;
        for (int i = 0; i < filtercount; i++) {
            java.lang.CharSequence repl = mFilters[i].filter(tb, tbstart, tbend, this, start, end);
            if (repl != null) {
                tb = repl;
                tbstart = 0;
                tbend = repl.length();
            }
        }
        final int origLen = end - start;
        final int newLen = tbend - tbstart;
        if (((origLen == 0) && (newLen == 0)) && (!android.text.SpannableStringBuilder.hasNonExclusiveExclusiveSpanAt(tb, tbstart))) {
            // This is a no-op iif there are no spans in tb that would be added (with a 0-length)
            // Early exit so that the text watchers do not get notified
            return this;
        }
        android.text.TextWatcher[] textWatchers = getSpans(start, start + origLen, android.text.TextWatcher.class);
        sendBeforeTextChanged(textWatchers, start, origLen, newLen);
        // Try to keep the cursor / selection at the same relative position during
        // a text replacement. If replaced or replacement text length is zero, this
        // is already taken care of.
        boolean adjustSelection = (origLen != 0) && (newLen != 0);
        int selectionStart = 0;
        int selectionEnd = 0;
        if (adjustSelection) {
            selectionStart = android.text.Selection.getSelectionStart(this);
            selectionEnd = android.text.Selection.getSelectionEnd(this);
        }
        change(start, end, tb, tbstart, tbend);
        if (adjustSelection) {
            boolean changed = false;
            if ((selectionStart > start) && (selectionStart < end)) {
                final long diff = selectionStart - start;
                final int offset = java.lang.Math.toIntExact((diff * newLen) / origLen);
                selectionStart = start + offset;
                changed = true;
                setSpan(false, android.text.Selection.SELECTION_START, selectionStart, selectionStart, android.text.Spanned.SPAN_POINT_POINT);
            }
            if ((selectionEnd > start) && (selectionEnd < end)) {
                final long diff = selectionEnd - start;
                final int offset = java.lang.Math.toIntExact((diff * newLen) / origLen);
                selectionEnd = start + offset;
                changed = true;
                setSpan(false, android.text.Selection.SELECTION_END, selectionEnd, selectionEnd, android.text.Spanned.SPAN_POINT_POINT);
            }
            if (changed) {
                restoreInvariants();
            }
        }
        sendTextChanged(textWatchers, start, origLen, newLen);
        sendAfterTextChanged(textWatchers);
        // Span watchers need to be called after text watchers, which may update the layout
        sendToSpanWatchers(start, end, newLen - origLen);
        return this;
    }

    private static boolean hasNonExclusiveExclusiveSpanAt(java.lang.CharSequence text, int offset) {
        if (text instanceof android.text.Spanned) {
            android.text.Spanned spanned = ((android.text.Spanned) (text));
            java.lang.Object[] spans = spanned.getSpans(offset, offset, java.lang.Object.class);
            final int length = spans.length;
            for (int i = 0; i < length; i++) {
                java.lang.Object span = spans[i];
                int flags = spanned.getSpanFlags(span);
                if (flags != android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    return true;

            }
        }
        return false;
    }

    private void sendToSpanWatchers(int replaceStart, int replaceEnd, int nbNewChars) {
        for (int i = 0; i < mSpanCount; i++) {
            int spanFlags = mSpanFlags[i];
            // This loop handles only modified (not added) spans.
            if ((spanFlags & android.text.SpannableStringBuilder.SPAN_ADDED) != 0)
                continue;

            int spanStart = mSpanStarts[i];
            int spanEnd = mSpanEnds[i];
            if (spanStart > mGapStart)
                spanStart -= mGapLength;

            if (spanEnd > mGapStart)
                spanEnd -= mGapLength;

            int newReplaceEnd = replaceEnd + nbNewChars;
            boolean spanChanged = false;
            int previousSpanStart = spanStart;
            if (spanStart > newReplaceEnd) {
                if (nbNewChars != 0) {
                    previousSpanStart -= nbNewChars;
                    spanChanged = true;
                }
            } else
                if (spanStart >= replaceStart) {
                    // No change if span start was already at replace interval boundaries before replace
                    if (((spanStart != replaceStart) || ((spanFlags & android.text.SpannableStringBuilder.SPAN_START_AT_START) != android.text.SpannableStringBuilder.SPAN_START_AT_START)) && ((spanStart != newReplaceEnd) || ((spanFlags & android.text.SpannableStringBuilder.SPAN_START_AT_END) != android.text.SpannableStringBuilder.SPAN_START_AT_END))) {
                        // TODO A correct previousSpanStart cannot be computed at this point.
                        // It would require to save all the previous spans' positions before the replace
                        // Using an invalid -1 value to convey this would break the broacast range
                        spanChanged = true;
                    }
                }

            int previousSpanEnd = spanEnd;
            if (spanEnd > newReplaceEnd) {
                if (nbNewChars != 0) {
                    previousSpanEnd -= nbNewChars;
                    spanChanged = true;
                }
            } else
                if (spanEnd >= replaceStart) {
                    // No change if span start was already at replace interval boundaries before replace
                    if (((spanEnd != replaceStart) || ((spanFlags & android.text.SpannableStringBuilder.SPAN_END_AT_START) != android.text.SpannableStringBuilder.SPAN_END_AT_START)) && ((spanEnd != newReplaceEnd) || ((spanFlags & android.text.SpannableStringBuilder.SPAN_END_AT_END) != android.text.SpannableStringBuilder.SPAN_END_AT_END))) {
                        // TODO same as above for previousSpanEnd
                        spanChanged = true;
                    }
                }

            if (spanChanged) {
                sendSpanChanged(mSpans[i], previousSpanStart, previousSpanEnd, spanStart, spanEnd);
            }
            mSpanFlags[i] &= ~android.text.SpannableStringBuilder.SPAN_START_END_MASK;
        }
        // Handle added spans
        for (int i = 0; i < mSpanCount; i++) {
            int spanFlags = mSpanFlags[i];
            if ((spanFlags & android.text.SpannableStringBuilder.SPAN_ADDED) != 0) {
                mSpanFlags[i] &= ~android.text.SpannableStringBuilder.SPAN_ADDED;
                int spanStart = mSpanStarts[i];
                int spanEnd = mSpanEnds[i];
                if (spanStart > mGapStart)
                    spanStart -= mGapLength;

                if (spanEnd > mGapStart)
                    spanEnd -= mGapLength;

                sendSpanAdded(mSpans[i], spanStart, spanEnd);
            }
        }
    }

    /**
     * Mark the specified range of text with the specified object.
     * The flags determine how the span will behave when text is
     * inserted at the start or end of the span's range.
     */
    public void setSpan(java.lang.Object what, int start, int end, int flags) {
        setSpan(true, what, start, end, flags);
    }

    // Note: if send is false, then it is the caller's responsibility to restore
    // invariants. If send is false and the span already exists, then this method
    // will not change the index of any spans.
    private void setSpan(boolean send, java.lang.Object what, int start, int end, int flags) {
        checkRange("setSpan", start, end);
        int flagsStart = (flags & android.text.SpannableStringBuilder.START_MASK) >> android.text.SpannableStringBuilder.START_SHIFT;
        if (isInvalidParagraphStart(start, flagsStart)) {
            throw new java.lang.RuntimeException("PARAGRAPH span must start at paragraph boundary");
        }
        int flagsEnd = flags & android.text.SpannableStringBuilder.END_MASK;
        if (isInvalidParagraphEnd(end, flagsEnd)) {
            throw new java.lang.RuntimeException("PARAGRAPH span must end at paragraph boundary");
        }
        // 0-length Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        if (((flagsStart == android.text.SpannableStringBuilder.POINT) && (flagsEnd == android.text.SpannableStringBuilder.MARK)) && (start == end)) {
            if (send) {
                android.util.Log.e(android.text.SpannableStringBuilder.TAG, "SPAN_EXCLUSIVE_EXCLUSIVE spans cannot have a zero length");
            }
            // Silently ignore invalid spans when they are created from this class.
            // This avoids the duplication of the above test code before all the
            // calls to setSpan that are done in this class
            return;
        }
        int nstart = start;
        int nend = end;
        if (start > mGapStart) {
            start += mGapLength;
        } else
            if (start == mGapStart) {
                if ((flagsStart == android.text.SpannableStringBuilder.POINT) || ((flagsStart == android.text.SpannableStringBuilder.PARAGRAPH) && (start == length())))
                    start += mGapLength;

            }

        if (end > mGapStart) {
            end += mGapLength;
        } else
            if (end == mGapStart) {
                if ((flagsEnd == android.text.SpannableStringBuilder.POINT) || ((flagsEnd == android.text.SpannableStringBuilder.PARAGRAPH) && (end == length())))
                    end += mGapLength;

            }

        if (mIndexOfSpan != null) {
            java.lang.Integer index = mIndexOfSpan.get(what);
            if (index != null) {
                int i = index;
                int ostart = mSpanStarts[i];
                int oend = mSpanEnds[i];
                if (ostart > mGapStart)
                    ostart -= mGapLength;

                if (oend > mGapStart)
                    oend -= mGapLength;

                mSpanStarts[i] = start;
                mSpanEnds[i] = end;
                mSpanFlags[i] = flags;
                if (send) {
                    restoreInvariants();
                    sendSpanChanged(what, ostart, oend, nstart, nend);
                }
                return;
            }
        }
        mSpans = com.android.internal.util.GrowingArrayUtils.append(mSpans, mSpanCount, what);
        mSpanStarts = com.android.internal.util.GrowingArrayUtils.append(mSpanStarts, mSpanCount, start);
        mSpanEnds = com.android.internal.util.GrowingArrayUtils.append(mSpanEnds, mSpanCount, end);
        mSpanFlags = com.android.internal.util.GrowingArrayUtils.append(mSpanFlags, mSpanCount, flags);
        mSpanOrder = com.android.internal.util.GrowingArrayUtils.append(mSpanOrder, mSpanCount, mSpanInsertCount);
        invalidateIndex(mSpanCount);
        mSpanCount++;
        mSpanInsertCount++;
        // Make sure there is enough room for empty interior nodes.
        // This magic formula computes the size of the smallest perfect binary
        // tree no smaller than mSpanCount.
        int sizeOfMax = (2 * treeRoot()) + 1;
        if (mSpanMax.length < sizeOfMax) {
            mSpanMax = new int[sizeOfMax];
        }
        if (send) {
            restoreInvariants();
            sendSpanAdded(what, nstart, nend);
        }
    }

    private final boolean isInvalidParagraphStart(int start, int flagsStart) {
        if (flagsStart == android.text.SpannableStringBuilder.PARAGRAPH) {
            if ((start != 0) && (start != length())) {
                char c = charAt(start - 1);
                if (c != '\n')
                    return true;

            }
        }
        return false;
    }

    private final boolean isInvalidParagraphEnd(int end, int flagsEnd) {
        if (flagsEnd == android.text.SpannableStringBuilder.PARAGRAPH) {
            if ((end != 0) && (end != length())) {
                char c = charAt(end - 1);
                if (c != '\n')
                    return true;

            }
        }
        return false;
    }

    /**
     * Remove the specified markup object from the buffer.
     */
    public void removeSpan(java.lang.Object what) {
        if (mIndexOfSpan == null)
            return;

        java.lang.Integer i = mIndexOfSpan.remove(what);
        if (i != null) {
            removeSpan(i.intValue());
        }
    }

    /**
     * Return externally visible offset given offset into gapped buffer.
     */
    private int resolveGap(int i) {
        return i > mGapStart ? i - mGapLength : i;
    }

    /**
     * Return the buffer offset of the beginning of the specified
     * markup object, or -1 if it is not attached to this buffer.
     */
    public int getSpanStart(java.lang.Object what) {
        if (mIndexOfSpan == null)
            return -1;

        java.lang.Integer i = mIndexOfSpan.get(what);
        return i == null ? -1 : resolveGap(mSpanStarts[i]);
    }

    /**
     * Return the buffer offset of the end of the specified
     * markup object, or -1 if it is not attached to this buffer.
     */
    public int getSpanEnd(java.lang.Object what) {
        if (mIndexOfSpan == null)
            return -1;

        java.lang.Integer i = mIndexOfSpan.get(what);
        return i == null ? -1 : resolveGap(mSpanEnds[i]);
    }

    /**
     * Return the flags of the end of the specified
     * markup object, or 0 if it is not attached to this buffer.
     */
    public int getSpanFlags(java.lang.Object what) {
        if (mIndexOfSpan == null)
            return 0;

        java.lang.Integer i = mIndexOfSpan.get(what);
        return i == null ? 0 : mSpanFlags[i];
    }

    /**
     * Return an array of the spans of the specified type that overlap
     * the specified range of the buffer.  The kind may be Object.class to get
     * a list of all the spans regardless of type.
     */
    @java.lang.SuppressWarnings("unchecked")
    public <T> T[] getSpans(int queryStart, int queryEnd, @android.annotation.Nullable
    java.lang.Class<T> kind) {
        return getSpans(queryStart, queryEnd, kind, true);
    }

    /**
     * Return an array of the spans of the specified type that overlap
     * the specified range of the buffer.  The kind may be Object.class to get
     * a list of all the spans regardless of type.
     *
     * @param queryStart
     * 		Start index.
     * @param queryEnd
     * 		End index.
     * @param kind
     * 		Class type to search for.
     * @param sort
     * 		If true the results are sorted by the insertion order.
     * @param <T>
     * 		
     * @return Array of the spans. Empty array if no results are found.
     * @unknown 
     */
    public <T> T[] getSpans(int queryStart, int queryEnd, @android.annotation.Nullable
    java.lang.Class<T> kind, boolean sort) {
        if (kind == null)
            return ((T[]) (com.android.internal.util.ArrayUtils.emptyArray(java.lang.Object.class)));

        if (mSpanCount == 0)
            return com.android.internal.util.ArrayUtils.emptyArray(kind);

        int count = countSpans(queryStart, queryEnd, kind, treeRoot());
        if (count == 0) {
            return com.android.internal.util.ArrayUtils.emptyArray(kind);
        }
        // Safe conversion, but requires a suppressWarning
        T[] ret = ((T[]) (java.lang.reflect.Array.newInstance(kind, count)));
        if (sort) {
            mPrioSortBuffer = checkSortBuffer(mPrioSortBuffer, count);
            mOrderSortBuffer = checkSortBuffer(mOrderSortBuffer, count);
        }
        getSpansRec(queryStart, queryEnd, kind, treeRoot(), ret, mPrioSortBuffer, mOrderSortBuffer, 0, sort);
        if (sort)
            sort(ret, mPrioSortBuffer, mOrderSortBuffer);

        return ret;
    }

    private int countSpans(int queryStart, int queryEnd, java.lang.Class kind, int i) {
        int count = 0;
        if ((i & 1) != 0) {
            // internal tree node
            int left = android.text.SpannableStringBuilder.leftChild(i);
            int spanMax = mSpanMax[left];
            if (spanMax > mGapStart) {
                spanMax -= mGapLength;
            }
            if (spanMax >= queryStart) {
                count = countSpans(queryStart, queryEnd, kind, left);
            }
        }
        if (i < mSpanCount) {
            int spanStart = mSpanStarts[i];
            if (spanStart > mGapStart) {
                spanStart -= mGapLength;
            }
            if (spanStart <= queryEnd) {
                int spanEnd = mSpanEnds[i];
                if (spanEnd > mGapStart) {
                    spanEnd -= mGapLength;
                }
                if (((spanEnd >= queryStart) && (((spanStart == spanEnd) || (queryStart == queryEnd)) || ((spanStart != queryEnd) && (spanEnd != queryStart)))) && ((java.lang.Object.class == kind) || kind.isInstance(mSpans[i]))) {
                    count++;
                }
                if ((i & 1) != 0) {
                    count += countSpans(queryStart, queryEnd, kind, android.text.SpannableStringBuilder.rightChild(i));
                }
            }
        }
        return count;
    }

    /**
     * Fills the result array with the spans found under the current interval tree node.
     *
     * @param queryStart
     * 		Start index for the interval query.
     * @param queryEnd
     * 		End index for the interval query.
     * @param kind
     * 		Class type to search for.
     * @param i
     * 		Index of the current tree node.
     * @param ret
     * 		Array to be filled with results.
     * @param priority
     * 		Buffer to keep record of the priorities of spans found.
     * @param insertionOrder
     * 		Buffer to keep record of the insertion orders of spans found.
     * @param count
     * 		The number of found spans.
     * @param sort
     * 		Flag to fill the priority and insertion order buffers. If false then
     * 		the spans with priority flag will be sorted in the result array.
     * @param <T>
     * 		
     * @return The total number of spans found.
     */
    @java.lang.SuppressWarnings("unchecked")
    private <T> int getSpansRec(int queryStart, int queryEnd, java.lang.Class<T> kind, int i, T[] ret, int[] priority, int[] insertionOrder, int count, boolean sort) {
        if ((i & 1) != 0) {
            // internal tree node
            int left = android.text.SpannableStringBuilder.leftChild(i);
            int spanMax = mSpanMax[left];
            if (spanMax > mGapStart) {
                spanMax -= mGapLength;
            }
            if (spanMax >= queryStart) {
                count = getSpansRec(queryStart, queryEnd, kind, left, ret, priority, insertionOrder, count, sort);
            }
        }
        if (i >= mSpanCount)
            return count;

        int spanStart = mSpanStarts[i];
        if (spanStart > mGapStart) {
            spanStart -= mGapLength;
        }
        if (spanStart <= queryEnd) {
            int spanEnd = mSpanEnds[i];
            if (spanEnd > mGapStart) {
                spanEnd -= mGapLength;
            }
            if (((spanEnd >= queryStart) && (((spanStart == spanEnd) || (queryStart == queryEnd)) || ((spanStart != queryEnd) && (spanEnd != queryStart)))) && ((java.lang.Object.class == kind) || kind.isInstance(mSpans[i]))) {
                int spanPriority = mSpanFlags[i] & android.text.Spanned.SPAN_PRIORITY;
                int target = count;
                if (sort) {
                    priority[target] = spanPriority;
                    insertionOrder[target] = mSpanOrder[i];
                } else
                    if (spanPriority != 0) {
                        // insertion sort for elements with priority
                        int j = 0;
                        for (; j < count; j++) {
                            int p = getSpanFlags(ret[j]) & android.text.Spanned.SPAN_PRIORITY;
                            if (spanPriority > p)
                                break;

                        }
                        java.lang.System.arraycopy(ret, j, ret, j + 1, count - j);
                        target = j;
                    }

                ret[target] = ((T) (mSpans[i]));
                count++;
            }
            if ((count < ret.length) && ((i & 1) != 0)) {
                count = getSpansRec(queryStart, queryEnd, kind, android.text.SpannableStringBuilder.rightChild(i), ret, priority, insertionOrder, count, sort);
            }
        }
        return count;
    }

    /**
     * Check the size of the buffer and grow if required.
     *
     * @param buffer
     * 		Buffer to be checked.
     * @param size
     * 		Required size.
     * @return Same buffer instance if the current size is greater than required size. Otherwise a
    new instance is created and returned.
     */
    private final int[] checkSortBuffer(int[] buffer, int size) {
        if (size > buffer.length) {
            return com.android.internal.util.ArrayUtils.newUnpaddedIntArray(com.android.internal.util.GrowingArrayUtils.growSize(size));
        }
        return buffer;
    }

    /**
     * An iterative heap sort implementation. It will sort the spans using first their priority
     * then insertion order. A span with higher priority will be before a span with lower
     * priority. If priorities are the same, the spans will be sorted with insertion order. A
     * span with a lower insertion order will be before a span with a higher insertion order.
     *
     * @param array
     * 		Span array to be sorted.
     * @param priority
     * 		Priorities of the spans
     * @param insertionOrder
     * 		Insertion orders of the spans
     * @param <T>
     * 		Span object type.
     * @param <T>
     * 		
     */
    private final <T> void sort(T[] array, int[] priority, int[] insertionOrder) {
        int size = array.length;
        for (int i = (size / 2) - 1; i >= 0; i--) {
            siftDown(i, array, size, priority, insertionOrder);
        }
        for (int i = size - 1; i > 0; i--) {
            T v = array[0];
            int prio = priority[0];
            int insertOrder = insertionOrder[0];
            array[0] = array[i];
            priority[0] = priority[i];
            insertionOrder[0] = insertionOrder[i];
            siftDown(0, array, i, priority, insertionOrder);
            array[i] = v;
            priority[i] = prio;
            insertionOrder[i] = insertOrder;
        }
    }

    /**
     * Helper function for heap sort.
     *
     * @param index
     * 		Index of the element to sift down.
     * @param array
     * 		Span array to be sorted.
     * @param size
     * 		Current heap size.
     * @param priority
     * 		Priorities of the spans
     * @param insertionOrder
     * 		Insertion orders of the spans
     * @param <T>
     * 		Span object type.
     */
    private final <T> void siftDown(int index, T[] array, int size, int[] priority, int[] insertionOrder) {
        T v = array[index];
        int prio = priority[index];
        int insertOrder = insertionOrder[index];
        int left = (2 * index) + 1;
        while (left < size) {
            if ((left < (size - 1)) && (compareSpans(left, left + 1, priority, insertionOrder) < 0)) {
                left++;
            }
            if (compareSpans(index, left, priority, insertionOrder) >= 0) {
                break;
            }
            array[index] = array[left];
            priority[index] = priority[left];
            insertionOrder[index] = insertionOrder[left];
            index = left;
            left = (2 * index) + 1;
        } 
        array[index] = v;
        priority[index] = prio;
        insertionOrder[index] = insertOrder;
    }

    /**
     * Compare two span elements in an array. Comparison is based first on the priority flag of
     * the span, and then the insertion order of the span.
     *
     * @param left
     * 		Index of the element to compare.
     * @param right
     * 		Index of the other element to compare.
     * @param priority
     * 		Priorities of the spans
     * @param insertionOrder
     * 		Insertion orders of the spans
     * @return 
     */
    private final int compareSpans(int left, int right, int[] priority, int[] insertionOrder) {
        int priority1 = priority[left];
        int priority2 = priority[right];
        if (priority1 == priority2) {
            return java.lang.Integer.compare(insertionOrder[left], insertionOrder[right]);
        }
        // since high priority has to be before a lower priority, the arguments to compare are
        // opposite of the insertion order check.
        return java.lang.Integer.compare(priority2, priority1);
    }

    /**
     * Return the next offset after <code>start</code> but less than or
     * equal to <code>limit</code> where a span of the specified type
     * begins or ends.
     */
    public int nextSpanTransition(int start, int limit, java.lang.Class kind) {
        if (mSpanCount == 0)
            return limit;

        if (kind == null) {
            kind = java.lang.Object.class;
        }
        return nextSpanTransitionRec(start, limit, kind, treeRoot());
    }

    private int nextSpanTransitionRec(int start, int limit, java.lang.Class kind, int i) {
        if ((i & 1) != 0) {
            // internal tree node
            int left = android.text.SpannableStringBuilder.leftChild(i);
            if (resolveGap(mSpanMax[left]) > start) {
                limit = nextSpanTransitionRec(start, limit, kind, left);
            }
        }
        if (i < mSpanCount) {
            int st = resolveGap(mSpanStarts[i]);
            int en = resolveGap(mSpanEnds[i]);
            if (((st > start) && (st < limit)) && kind.isInstance(mSpans[i]))
                limit = st;

            if (((en > start) && (en < limit)) && kind.isInstance(mSpans[i]))
                limit = en;

            if ((st < limit) && ((i & 1) != 0)) {
                limit = nextSpanTransitionRec(start, limit, kind, android.text.SpannableStringBuilder.rightChild(i));
            }
        }
        return limit;
    }

    /**
     * Return a new CharSequence containing a copy of the specified
     * range of this buffer, including the overlapping spans.
     */
    public java.lang.CharSequence subSequence(int start, int end) {
        return new android.text.SpannableStringBuilder(this, start, end);
    }

    /**
     * Copy the specified range of chars from this buffer into the
     * specified array, beginning at the specified offset.
     */
    public void getChars(int start, int end, char[] dest, int destoff) {
        checkRange("getChars", start, end);
        if (end <= mGapStart) {
            java.lang.System.arraycopy(mText, start, dest, destoff, end - start);
        } else
            if (start >= mGapStart) {
                java.lang.System.arraycopy(mText, start + mGapLength, dest, destoff, end - start);
            } else {
                java.lang.System.arraycopy(mText, start, dest, destoff, mGapStart - start);
                java.lang.System.arraycopy(mText, mGapStart + mGapLength, dest, destoff + (mGapStart - start), end - mGapStart);
            }

    }

    /**
     * Return a String containing a copy of the chars in this buffer.
     */
    @java.lang.Override
    public java.lang.String toString() {
        int len = length();
        char[] buf = new char[len];
        getChars(0, len, buf, 0);
        return new java.lang.String(buf);
    }

    /**
     * Return a String containing a copy of the chars in this buffer, limited to the
     * [start, end[ range.
     *
     * @unknown 
     */
    public java.lang.String substring(int start, int end) {
        char[] buf = new char[end - start];
        getChars(start, end, buf, 0);
        return new java.lang.String(buf);
    }

    /**
     * Returns the depth of TextWatcher callbacks. Returns 0 when the object is not handling
     * TextWatchers. A return value greater than 1 implies that a TextWatcher caused a change that
     * recursively triggered a TextWatcher.
     */
    public int getTextWatcherDepth() {
        return mTextWatcherDepth;
    }

    private void sendBeforeTextChanged(android.text.TextWatcher[] watchers, int start, int before, int after) {
        int n = watchers.length;
        mTextWatcherDepth++;
        for (int i = 0; i < n; i++) {
            watchers[i].beforeTextChanged(this, start, before, after);
        }
        mTextWatcherDepth--;
    }

    private void sendTextChanged(android.text.TextWatcher[] watchers, int start, int before, int after) {
        int n = watchers.length;
        mTextWatcherDepth++;
        for (int i = 0; i < n; i++) {
            watchers[i].onTextChanged(this, start, before, after);
        }
        mTextWatcherDepth--;
    }

    private void sendAfterTextChanged(android.text.TextWatcher[] watchers) {
        int n = watchers.length;
        mTextWatcherDepth++;
        for (int i = 0; i < n; i++) {
            watchers[i].afterTextChanged(this);
        }
        mTextWatcherDepth--;
    }

    private void sendSpanAdded(java.lang.Object what, int start, int end) {
        android.text.SpanWatcher[] recip = getSpans(start, end, android.text.SpanWatcher.class);
        int n = recip.length;
        for (int i = 0; i < n; i++) {
            recip[i].onSpanAdded(this, what, start, end);
        }
    }

    private void sendSpanRemoved(java.lang.Object what, int start, int end) {
        android.text.SpanWatcher[] recip = getSpans(start, end, android.text.SpanWatcher.class);
        int n = recip.length;
        for (int i = 0; i < n; i++) {
            recip[i].onSpanRemoved(this, what, start, end);
        }
    }

    private void sendSpanChanged(java.lang.Object what, int oldStart, int oldEnd, int start, int end) {
        // The bounds of a possible SpanWatcher are guaranteed to be set before this method is
        // called, so that the order of the span does not affect this broadcast.
        android.text.SpanWatcher[] spanWatchers = getSpans(java.lang.Math.min(oldStart, start), java.lang.Math.min(java.lang.Math.max(oldEnd, end), length()), android.text.SpanWatcher.class);
        int n = spanWatchers.length;
        for (int i = 0; i < n; i++) {
            spanWatchers[i].onSpanChanged(this, what, oldStart, oldEnd, start, end);
        }
    }

    private static java.lang.String region(int start, int end) {
        return ((("(" + start) + " ... ") + end) + ")";
    }

    private void checkRange(final java.lang.String operation, int start, int end) {
        if (end < start) {
            throw new java.lang.IndexOutOfBoundsException(((operation + " ") + android.text.SpannableStringBuilder.region(start, end)) + " has end before start");
        }
        int len = length();
        if ((start > len) || (end > len)) {
            throw new java.lang.IndexOutOfBoundsException((((operation + " ") + android.text.SpannableStringBuilder.region(start, end)) + " ends beyond length ") + len);
        }
        if ((start < 0) || (end < 0)) {
            throw new java.lang.IndexOutOfBoundsException(((operation + " ") + android.text.SpannableStringBuilder.region(start, end)) + " starts before 0");
        }
    }

    /* private boolean isprint(char c) { // XXX
    if (c >= ' ' && c <= '~')
    return true;
    else
    return false;
    }

    private static final int startFlag(int flag) {
    return (flag >> 4) & 0x0F;
    }

    private static final int endFlag(int flag) {
    return flag & 0x0F;
    }

    public void dump() { // XXX
    for (int i = 0; i < mGapStart; i++) {
    System.out.print('|');
    System.out.print(' ');
    System.out.print(isprint(mText[i]) ? mText[i] : '.');
    System.out.print(' ');
    }

    for (int i = mGapStart; i < mGapStart + mGapLength; i++) {
    System.out.print('|');
    System.out.print('(');
    System.out.print(isprint(mText[i]) ? mText[i] : '.');
    System.out.print(')');
    }

    for (int i = mGapStart + mGapLength; i < mText.length; i++) {
    System.out.print('|');
    System.out.print(' ');
    System.out.print(isprint(mText[i]) ? mText[i] : '.');
    System.out.print(' ');
    }

    System.out.print('\n');

    for (int i = 0; i < mText.length + 1; i++) {
    int found = 0;
    int wfound = 0;

    for (int j = 0; j < mSpanCount; j++) {
    if (mSpanStarts[j] == i) {
    found = 1;
    wfound = j;
    break;
    }

    if (mSpanEnds[j] == i) {
    found = 2;
    wfound = j;
    break;
    }
    }

    if (found == 1) {
    if (startFlag(mSpanFlags[wfound]) == MARK)
    System.out.print("(   ");
    if (startFlag(mSpanFlags[wfound]) == PARAGRAPH)
    System.out.print("<   ");
    else
    System.out.print("[   ");
    } else if (found == 2) {
    if (endFlag(mSpanFlags[wfound]) == POINT)
    System.out.print(")   ");
    if (endFlag(mSpanFlags[wfound]) == PARAGRAPH)
    System.out.print(">   ");
    else
    System.out.print("]   ");
    } else {
    System.out.print("    ");
    }
    }

    System.out.print("\n");
    }
     */
    /**
     * Don't call this yourself -- exists for Canvas to use internally.
     * {@hide }
     */
    public void drawText(android.graphics.Canvas c, int start, int end, float x, float y, android.graphics.Paint p) {
        checkRange("drawText", start, end);
        if (end <= mGapStart) {
            c.drawText(mText, start, end - start, x, y, p);
        } else
            if (start >= mGapStart) {
                c.drawText(mText, start + mGapLength, end - start, x, y, p);
            } else {
                char[] buf = android.text.TextUtils.obtain(end - start);
                getChars(start, end, buf, 0);
                c.drawText(buf, 0, end - start, x, y, p);
                android.text.TextUtils.recycle(buf);
            }

    }

    /**
     * Don't call this yourself -- exists for Canvas to use internally.
     * {@hide }
     */
    public void drawTextRun(android.graphics.Canvas c, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, android.graphics.Paint p) {
        checkRange("drawTextRun", start, end);
        int contextLen = contextEnd - contextStart;
        int len = end - start;
        if (contextEnd <= mGapStart) {
            c.drawTextRun(mText, start, len, contextStart, contextLen, x, y, isRtl, p);
        } else
            if (contextStart >= mGapStart) {
                c.drawTextRun(mText, start + mGapLength, len, contextStart + mGapLength, contextLen, x, y, isRtl, p);
            } else {
                char[] buf = android.text.TextUtils.obtain(contextLen);
                getChars(contextStart, contextEnd, buf, 0);
                c.drawTextRun(buf, start - contextStart, len, 0, contextLen, x, y, isRtl, p);
                android.text.TextUtils.recycle(buf);
            }

    }

    /**
     * Don't call this yourself -- exists for Paint to use internally.
     * {@hide }
     */
    public float measureText(int start, int end, android.graphics.Paint p) {
        checkRange("measureText", start, end);
        float ret;
        if (end <= mGapStart) {
            ret = p.measureText(mText, start, end - start);
        } else
            if (start >= mGapStart) {
                ret = p.measureText(mText, start + mGapLength, end - start);
            } else {
                char[] buf = android.text.TextUtils.obtain(end - start);
                getChars(start, end, buf, 0);
                ret = p.measureText(buf, 0, end - start);
                android.text.TextUtils.recycle(buf);
            }

        return ret;
    }

    /**
     * Don't call this yourself -- exists for Paint to use internally.
     * {@hide }
     */
    public int getTextWidths(int start, int end, float[] widths, android.graphics.Paint p) {
        checkRange("getTextWidths", start, end);
        int ret;
        if (end <= mGapStart) {
            ret = p.getTextWidths(mText, start, end - start, widths);
        } else
            if (start >= mGapStart) {
                ret = p.getTextWidths(mText, start + mGapLength, end - start, widths);
            } else {
                char[] buf = android.text.TextUtils.obtain(end - start);
                getChars(start, end, buf, 0);
                ret = p.getTextWidths(buf, 0, end - start, widths);
                android.text.TextUtils.recycle(buf);
            }

        return ret;
    }

    /**
     * Don't call this yourself -- exists for Paint to use internally.
     * {@hide }
     */
    public float getTextRunAdvances(int start, int end, int contextStart, int contextEnd, boolean isRtl, float[] advances, int advancesPos, android.graphics.Paint p) {
        float ret;
        int contextLen = contextEnd - contextStart;
        int len = end - start;
        if (end <= mGapStart) {
            ret = p.getTextRunAdvances(mText, start, len, contextStart, contextLen, isRtl, advances, advancesPos);
        } else
            if (start >= mGapStart) {
                ret = p.getTextRunAdvances(mText, start + mGapLength, len, contextStart + mGapLength, contextLen, isRtl, advances, advancesPos);
            } else {
                char[] buf = android.text.TextUtils.obtain(contextLen);
                getChars(contextStart, contextEnd, buf, 0);
                ret = p.getTextRunAdvances(buf, start - contextStart, len, 0, contextLen, isRtl, advances, advancesPos);
                android.text.TextUtils.recycle(buf);
            }

        return ret;
    }

    /**
     * Returns the next cursor position in the run.  This avoids placing the cursor between
     * surrogates, between characters that form conjuncts, between base characters and combining
     * marks, or within a reordering cluster.
     *
     * <p>The context is the shaping context for cursor movement, generally the bounds of the metric
     * span enclosing the cursor in the direction of movement.
     * <code>contextStart</code>, <code>contextEnd</code> and <code>offset</code> are relative to
     * the start of the string.</p>
     *
     * <p>If cursorOpt is CURSOR_AT and the offset is not a valid cursor position,
     * this returns -1.  Otherwise this will never return a value before contextStart or after
     * contextEnd.</p>
     *
     * @param contextStart
     * 		the start index of the context
     * @param contextEnd
     * 		the (non-inclusive) end index of the context
     * @param dir
     * 		either DIRECTION_RTL or DIRECTION_LTR
     * @param offset
     * 		the cursor position to move from
     * @param cursorOpt
     * 		how to move the cursor, one of CURSOR_AFTER,
     * 		CURSOR_AT_OR_AFTER, CURSOR_BEFORE,
     * 		CURSOR_AT_OR_BEFORE, or CURSOR_AT
     * @param p
     * 		the Paint object that is requesting this information
     * @return the offset of the next position, or -1
     * @deprecated This is an internal method, refrain from using it in your code
     */
    @java.lang.Deprecated
    public int getTextRunCursor(int contextStart, int contextEnd, int dir, int offset, int cursorOpt, android.graphics.Paint p) {
        int ret;
        int contextLen = contextEnd - contextStart;
        if (contextEnd <= mGapStart) {
            ret = p.getTextRunCursor(mText, contextStart, contextLen, dir, offset, cursorOpt);
        } else
            if (contextStart >= mGapStart) {
                ret = p.getTextRunCursor(mText, contextStart + mGapLength, contextLen, dir, offset + mGapLength, cursorOpt) - mGapLength;
            } else {
                char[] buf = android.text.TextUtils.obtain(contextLen);
                getChars(contextStart, contextEnd, buf, 0);
                ret = p.getTextRunCursor(buf, 0, contextLen, dir, offset - contextStart, cursorOpt) + contextStart;
                android.text.TextUtils.recycle(buf);
            }

        return ret;
    }

    // Documentation from interface
    public void setFilters(android.text.InputFilter[] filters) {
        if (filters == null) {
            throw new java.lang.IllegalArgumentException();
        }
        mFilters = filters;
    }

    // Documentation from interface
    public android.text.InputFilter[] getFilters() {
        return mFilters;
    }

    // Same as SpannableStringInternal
    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if ((o instanceof android.text.Spanned) && toString().equals(o.toString())) {
            android.text.Spanned other = ((android.text.Spanned) (o));
            // Check span data
            java.lang.Object[] otherSpans = other.getSpans(0, other.length(), java.lang.Object.class);
            if (mSpanCount == otherSpans.length) {
                for (int i = 0; i < mSpanCount; ++i) {
                    java.lang.Object thisSpan = mSpans[i];
                    java.lang.Object otherSpan = otherSpans[i];
                    if (thisSpan == this) {
                        if ((((other != otherSpan) || (getSpanStart(thisSpan) != other.getSpanStart(otherSpan))) || (getSpanEnd(thisSpan) != other.getSpanEnd(otherSpan))) || (getSpanFlags(thisSpan) != other.getSpanFlags(otherSpan))) {
                            return false;
                        }
                    } else
                        if ((((!thisSpan.equals(otherSpan)) || (getSpanStart(thisSpan) != other.getSpanStart(otherSpan))) || (getSpanEnd(thisSpan) != other.getSpanEnd(otherSpan))) || (getSpanFlags(thisSpan) != other.getSpanFlags(otherSpan))) {
                            return false;
                        }

                }
                return true;
            }
        }
        return false;
    }

    // Same as SpannableStringInternal
    @java.lang.Override
    public int hashCode() {
        int hash = toString().hashCode();
        hash = (hash * 31) + mSpanCount;
        for (int i = 0; i < mSpanCount; ++i) {
            java.lang.Object span = mSpans[i];
            if (span != this) {
                hash = (hash * 31) + span.hashCode();
            }
            hash = (hash * 31) + getSpanStart(span);
            hash = (hash * 31) + getSpanEnd(span);
            hash = (hash * 31) + getSpanFlags(span);
        }
        return hash;
    }

    // Primitives for treating span list as binary tree
    // The spans (along with start and end offsets and flags) are stored in linear arrays sorted
    // by start offset. For fast searching, there is a binary search structure imposed over these
    // arrays. This structure is inorder traversal of a perfect binary tree, a slightly unusual
    // but advantageous approach.
    // The value-containing nodes are indexed 0 <= i < n (where n = mSpanCount), thus preserving
    // logic that accesses the values as a contiguous array. Other balanced binary tree approaches
    // (such as a complete binary tree) would require some shuffling of node indices.
    // Basic properties of this structure: For a perfect binary tree of height m:
    // The tree has 2^(m+1) - 1 total nodes.
    // The root of the tree has index 2^m - 1.
    // All leaf nodes have even index, all interior nodes odd.
    // The height of a node of index i is the number of trailing ones in i's binary representation.
    // The left child of a node i of height h is i - 2^(h - 1).
    // The right child of a node i of height h is i + 2^(h - 1).
    // Note that for arbitrary n, interior nodes of this tree may be >= n. Thus, the general
    // structure of a recursive traversal of node i is:
    // * traverse left child if i is an interior node
    // * process i if i < n
    // * traverse right child if i is an interior node and i < n
    private int treeRoot() {
        return java.lang.Integer.highestOneBit(mSpanCount) - 1;
    }

    // (i+1) & ~i is equal to 2^(the number of trailing ones in i)
    private static int leftChild(int i) {
        return i - (((i + 1) & (~i)) >> 1);
    }

    private static int rightChild(int i) {
        return i + (((i + 1) & (~i)) >> 1);
    }

    // The span arrays are also augmented by an mSpanMax[] array that represents an interval tree
    // over the binary tree structure described above. For each node, the mSpanMax[] array contains
    // the maximum value of mSpanEnds of that node and its descendants. Thus, traversals can
    // easily reject subtrees that contain no spans overlapping the area of interest.
    // Note that mSpanMax[] also has a valid valuefor interior nodes of index >= n, but which have
    // descendants of index < n. In these cases, it simply represents the maximum span end of its
    // descendants. This is a consequence of the perfect binary tree structure.
    private int calcMax(int i) {
        int max = 0;
        if ((i & 1) != 0) {
            // internal tree node
            max = calcMax(android.text.SpannableStringBuilder.leftChild(i));
        }
        if (i < mSpanCount) {
            max = java.lang.Math.max(max, mSpanEnds[i]);
            if ((i & 1) != 0) {
                max = java.lang.Math.max(max, calcMax(android.text.SpannableStringBuilder.rightChild(i)));
            }
        }
        mSpanMax[i] = max;
        return max;
    }

    // restores binary interval tree invariants after any mutation of span structure
    private void restoreInvariants() {
        if (mSpanCount == 0)
            return;

        // invariant 1: span starts are nondecreasing
        // This is a simple insertion sort because we expect it to be mostly sorted.
        for (int i = 1; i < mSpanCount; i++) {
            if (mSpanStarts[i] < mSpanStarts[i - 1]) {
                java.lang.Object span = mSpans[i];
                int start = mSpanStarts[i];
                int end = mSpanEnds[i];
                int flags = mSpanFlags[i];
                int insertionOrder = mSpanOrder[i];
                int j = i;
                do {
                    mSpans[j] = mSpans[j - 1];
                    mSpanStarts[j] = mSpanStarts[j - 1];
                    mSpanEnds[j] = mSpanEnds[j - 1];
                    mSpanFlags[j] = mSpanFlags[j - 1];
                    mSpanOrder[j] = mSpanOrder[j - 1];
                    j--;
                } while ((j > 0) && (start < mSpanStarts[j - 1]) );
                mSpans[j] = span;
                mSpanStarts[j] = start;
                mSpanEnds[j] = end;
                mSpanFlags[j] = flags;
                mSpanOrder[j] = insertionOrder;
                invalidateIndex(j);
            }
        }
        // invariant 2: max is max span end for each node and its descendants
        calcMax(treeRoot());
        // invariant 3: mIndexOfSpan maps spans back to indices
        if (mIndexOfSpan == null) {
            mIndexOfSpan = new java.util.IdentityHashMap<java.lang.Object, java.lang.Integer>();
        }
        for (int i = mLowWaterMark; i < mSpanCount; i++) {
            java.lang.Integer existing = mIndexOfSpan.get(mSpans[i]);
            if ((existing == null) || (existing != i)) {
                mIndexOfSpan.put(mSpans[i], i);
            }
        }
        mLowWaterMark = java.lang.Integer.MAX_VALUE;
    }

    // Call this on any update to mSpans[], so that mIndexOfSpan can be updated
    private void invalidateIndex(int i) {
        mLowWaterMark = java.lang.Math.min(i, mLowWaterMark);
    }

    private static final android.text.InputFilter[] NO_FILTERS = new android.text.InputFilter[0];

    private android.text.InputFilter[] mFilters = android.text.SpannableStringBuilder.NO_FILTERS;

    private char[] mText;

    private int mGapStart;

    private int mGapLength;

    private java.lang.Object[] mSpans;

    private int[] mSpanStarts;

    private int[] mSpanEnds;

    private int[] mSpanMax;// see calcMax() for an explanation of what this array stores


    private int[] mSpanFlags;

    private int[] mSpanOrder;// store the order of span insertion


    private int mSpanInsertCount;// counter for the span insertion


    private int[] mPrioSortBuffer;// buffer used to sort getSpans result


    private int[] mOrderSortBuffer;// buffer used to sort getSpans result


    private int mSpanCount;

    private java.util.IdentityHashMap<java.lang.Object, java.lang.Integer> mIndexOfSpan;

    private int mLowWaterMark;// indices below this have not been touched


    // TextWatcher callbacks may trigger changes that trigger more callbacks. This keeps track of
    // how deep the callbacks go.
    private int mTextWatcherDepth;

    // TODO These value are tightly related to the public SPAN_MARK/POINT values in {@link Spanned}
    private static final int MARK = 1;

    private static final int POINT = 2;

    private static final int PARAGRAPH = 3;

    private static final int START_MASK = 0xf0;

    private static final int END_MASK = 0xf;

    private static final int START_SHIFT = 4;

    // These bits are not (currently) used by SPANNED flags
    private static final int SPAN_ADDED = 0x800;

    private static final int SPAN_START_AT_START = 0x1000;

    private static final int SPAN_START_AT_END = 0x2000;

    private static final int SPAN_END_AT_START = 0x4000;

    private static final int SPAN_END_AT_END = 0x8000;

    private static final int SPAN_START_END_MASK = 0xf000;
}

