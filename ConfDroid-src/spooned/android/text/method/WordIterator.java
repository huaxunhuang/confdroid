/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.text.method;


/**
 * Walks through cursor positions at word boundaries. Internally uses
 * {@link BreakIterator#getWordInstance()}, and caches {@link CharSequence}
 * for performance reasons.
 *
 * Also provides methods to determine word boundaries.
 * {@hide }
 */
public class WordIterator implements android.text.Selection.PositionIterator {
    // Size of the window for the word iterator, should be greater than the longest word's length
    private static final int WINDOW_WIDTH = 50;

    private java.lang.String mString;

    private int mOffsetShift;

    private android.icu.text.BreakIterator mIterator;

    /**
     * Constructs a WordIterator using the default locale.
     */
    public WordIterator() {
        this(java.util.Locale.getDefault());
    }

    /**
     * Constructs a new WordIterator for the specified locale.
     *
     * @param locale
     * 		The locale to be used when analysing the text.
     */
    public WordIterator(java.util.Locale locale) {
        mIterator = android.icu.text.BreakIterator.getWordInstance(locale);
    }

    public void setCharSequence(java.lang.CharSequence charSequence, int start, int end) {
        mOffsetShift = java.lang.Math.max(0, start - android.text.method.WordIterator.WINDOW_WIDTH);
        final int windowEnd = java.lang.Math.min(charSequence.length(), end + android.text.method.WordIterator.WINDOW_WIDTH);
        if (charSequence instanceof android.text.SpannableStringBuilder) {
            mString = ((android.text.SpannableStringBuilder) (charSequence)).substring(mOffsetShift, windowEnd);
        } else {
            mString = charSequence.subSequence(mOffsetShift, windowEnd).toString();
        }
        mIterator.setText(mString);
    }

    /**
     * {@inheritDoc }
     */
    public int preceding(int offset) {
        int shiftedOffset = offset - mOffsetShift;
        do {
            shiftedOffset = mIterator.preceding(shiftedOffset);
            if (shiftedOffset == android.icu.text.BreakIterator.DONE) {
                return android.icu.text.BreakIterator.DONE;
            }
            if (isOnLetterOrDigit(shiftedOffset)) {
                return shiftedOffset + mOffsetShift;
            }
        } while (true );
    }

    /**
     * {@inheritDoc }
     */
    public int following(int offset) {
        int shiftedOffset = offset - mOffsetShift;
        do {
            shiftedOffset = mIterator.following(shiftedOffset);
            if (shiftedOffset == android.icu.text.BreakIterator.DONE) {
                return android.icu.text.BreakIterator.DONE;
            }
            if (isAfterLetterOrDigit(shiftedOffset)) {
                return shiftedOffset + mOffsetShift;
            }
        } while (true );
    }

    /**
     * {@inheritDoc }
     */
    public boolean isBoundary(int offset) {
        int shiftedOffset = offset - mOffsetShift;
        checkOffsetIsValid(shiftedOffset);
        return mIterator.isBoundary(shiftedOffset);
    }

    /**
     * Returns the position of next boundary after the given offset. Returns
     * {@code DONE} if there is no boundary after the given offset.
     *
     * @param offset
     * 		the given start position to search from.
     * @return the position of the last boundary preceding the given offset.
     */
    public int nextBoundary(int offset) {
        int shiftedOffset = offset - mOffsetShift;
        shiftedOffset = mIterator.following(shiftedOffset);
        if (shiftedOffset == android.icu.text.BreakIterator.DONE) {
            return android.icu.text.BreakIterator.DONE;
        }
        return shiftedOffset + mOffsetShift;
    }

    /**
     * Returns the position of boundary preceding the given offset or
     * {@code DONE} if the given offset specifies the starting position.
     *
     * @param offset
     * 		the given start position to search from.
     * @return the position of the last boundary preceding the given offset.
     */
    public int prevBoundary(int offset) {
        int shiftedOffset = offset - mOffsetShift;
        shiftedOffset = mIterator.preceding(shiftedOffset);
        if (shiftedOffset == android.icu.text.BreakIterator.DONE) {
            return android.icu.text.BreakIterator.DONE;
        }
        return shiftedOffset + mOffsetShift;
    }

    /**
     * If <code>offset</code> is within a word, returns the index of the first character of that
     * word, otherwise returns BreakIterator.DONE.
     *
     * The offsets that are considered to be part of a word are the indexes of its characters,
     * <i>as well as</i> the index of its last character plus one.
     * If offset is the index of a low surrogate character, BreakIterator.DONE will be returned.
     *
     * Valid range for offset is [0..textLength] (note the inclusive upper bound).
     * The returned value is within [0..offset] or BreakIterator.DONE.
     *
     * @throws IllegalArgumentException
     * 		is offset is not valid.
     */
    public int getBeginning(int offset) {
        // TODO: Check if usage of this can be updated to getBeginning(offset, true) if
        // so this method can be removed.
        return getBeginning(offset, false);
    }

    /**
     * If <code>offset</code> is within a word, returns the index of the last character of that
     * word plus one, otherwise returns BreakIterator.DONE.
     *
     * The offsets that are considered to be part of a word are the indexes of its characters,
     * <i>as well as</i> the index of its last character plus one.
     * If offset is the index of a low surrogate character, BreakIterator.DONE will be returned.
     *
     * Valid range for offset is [0..textLength] (note the inclusive upper bound).
     * The returned value is within [offset..textLength] or BreakIterator.DONE.
     *
     * @throws IllegalArgumentException
     * 		is offset is not valid.
     */
    public int getEnd(int offset) {
        // TODO: Check if usage of this can be updated to getEnd(offset, true), if
        // so this method can be removed.
        return getEnd(offset, false);
    }

    /**
     * If the <code>offset</code> is within a word or on a word boundary that can only be
     * considered the start of a word (e.g. _word where "_" is any character that would not
     * be considered part of the word) then this returns the index of the first character of
     * that word.
     *
     * If the offset is on a word boundary that can be considered the start and end of a
     * word, e.g. AABB (where AA and BB are both words) and the offset is the boundary
     * between AA and BB, this would return the start of the previous word, AA.
     *
     * Returns BreakIterator.DONE if there is no previous boundary.
     *
     * @throws IllegalArgumentException
     * 		is offset is not valid.
     */
    public int getPrevWordBeginningOnTwoWordsBoundary(int offset) {
        return getBeginning(offset, true);
    }

    /**
     * If the <code>offset</code> is within a word or on a word boundary that can only be
     * considered the end of a word (e.g. word_ where "_" is any character that would not
     * be considered part of the word) then this returns the index of the last character
     * plus one of that word.
     *
     * If the offset is on a word boundary that can be considered the start and end of a
     * word, e.g. AABB (where AA and BB are both words) and the offset is the boundary
     * between AA and BB, this would return the end of the next word, BB.
     *
     * Returns BreakIterator.DONE if there is no next boundary.
     *
     * @throws IllegalArgumentException
     * 		is offset is not valid.
     */
    public int getNextWordEndOnTwoWordBoundary(int offset) {
        return getEnd(offset, true);
    }

    /**
     * If the <code>offset</code> is within a word or on a word boundary that can only be
     * considered the start of a word (e.g. _word where "_" is any character that would not
     * be considered part of the word) then this returns the index of the first character of
     * that word.
     *
     * If the offset is on a word boundary that can be considered the start and end of a
     * word, e.g. AABB (where AA and BB are both words) and the offset is the boundary
     * between AA and BB, and getPrevWordBeginningOnTwoWordsBoundary is true then this would
     * return the start of the previous word, AA. Otherwise it would return the current offset,
     * the start of BB.
     *
     * Returns BreakIterator.DONE if there is no previous boundary.
     *
     * @throws IllegalArgumentException
     * 		is offset is not valid.
     */
    private int getBeginning(int offset, boolean getPrevWordBeginningOnTwoWordsBoundary) {
        final int shiftedOffset = offset - mOffsetShift;
        checkOffsetIsValid(shiftedOffset);
        if (isOnLetterOrDigit(shiftedOffset)) {
            if (mIterator.isBoundary(shiftedOffset) && ((!isAfterLetterOrDigit(shiftedOffset)) || (!getPrevWordBeginningOnTwoWordsBoundary))) {
                return shiftedOffset + mOffsetShift;
            } else {
                return mIterator.preceding(shiftedOffset) + mOffsetShift;
            }
        } else {
            if (isAfterLetterOrDigit(shiftedOffset)) {
                return mIterator.preceding(shiftedOffset) + mOffsetShift;
            }
        }
        return android.icu.text.BreakIterator.DONE;
    }

    /**
     * If the <code>offset</code> is within a word or on a word boundary that can only be
     * considered the end of a word (e.g. word_ where "_" is any character that would not be
     * considered part of the word) then this returns the index of the last character plus one
     * of that word.
     *
     * If the offset is on a word boundary that can be considered the start and end of a
     * word, e.g. AABB (where AA and BB are both words) and the offset is the boundary
     * between AA and BB, and getNextWordEndOnTwoWordBoundary is true then this would return
     * the end of the next word, BB. Otherwise it would return the current offset, the end
     * of AA.
     *
     * Returns BreakIterator.DONE if there is no next boundary.
     *
     * @throws IllegalArgumentException
     * 		is offset is not valid.
     */
    private int getEnd(int offset, boolean getNextWordEndOnTwoWordBoundary) {
        final int shiftedOffset = offset - mOffsetShift;
        checkOffsetIsValid(shiftedOffset);
        if (isAfterLetterOrDigit(shiftedOffset)) {
            if (mIterator.isBoundary(shiftedOffset) && ((!isOnLetterOrDigit(shiftedOffset)) || (!getNextWordEndOnTwoWordBoundary))) {
                return shiftedOffset + mOffsetShift;
            } else {
                return mIterator.following(shiftedOffset) + mOffsetShift;
            }
        } else {
            if (isOnLetterOrDigit(shiftedOffset)) {
                return mIterator.following(shiftedOffset) + mOffsetShift;
            }
        }
        return android.icu.text.BreakIterator.DONE;
    }

    /**
     * If <code>offset</code> is within a group of punctuation as defined
     * by {@link #isPunctuation(int)}, returns the index of the first character
     * of that group, otherwise returns BreakIterator.DONE.
     *
     * @param offset
     * 		the offset to search from.
     */
    public int getPunctuationBeginning(int offset) {
        while ((offset != android.icu.text.BreakIterator.DONE) && (!isPunctuationStartBoundary(offset))) {
            offset = prevBoundary(offset);
        } 
        // No need to shift offset, prevBoundary handles that.
        return offset;
    }

    /**
     * If <code>offset</code> is within a group of punctuation as defined
     * by {@link #isPunctuation(int)}, returns the index of the last character
     * of that group plus one, otherwise returns BreakIterator.DONE.
     *
     * @param offset
     * 		the offset to search from.
     */
    public int getPunctuationEnd(int offset) {
        while ((offset != android.icu.text.BreakIterator.DONE) && (!isPunctuationEndBoundary(offset))) {
            offset = nextBoundary(offset);
        } 
        // No need to shift offset, nextBoundary handles that.
        return offset;
    }

    /**
     * Indicates if the provided offset is after a punctuation character
     * as defined by {@link #isPunctuation(int)}.
     *
     * @param offset
     * 		the offset to check from.
     * @return Whether the offset is after a punctuation character.
     */
    public boolean isAfterPunctuation(int offset) {
        final int shiftedOffset = offset - mOffsetShift;
        if ((shiftedOffset >= 1) && (shiftedOffset <= mString.length())) {
            final int codePoint = mString.codePointBefore(shiftedOffset);
            return isPunctuation(codePoint);
        }
        return false;
    }

    /**
     * Indicates if the provided offset is at a punctuation character
     * as defined by {@link #isPunctuation(int)}.
     *
     * @param offset
     * 		the offset to check from.
     * @return Whether the offset is at a punctuation character.
     */
    public boolean isOnPunctuation(int offset) {
        final int shiftedOffset = offset - mOffsetShift;
        if ((shiftedOffset >= 0) && (shiftedOffset < mString.length())) {
            final int codePoint = mString.codePointAt(shiftedOffset);
            return isPunctuation(codePoint);
        }
        return false;
    }

    private boolean isPunctuationStartBoundary(int offset) {
        return isOnPunctuation(offset) && (!isAfterPunctuation(offset));
    }

    private boolean isPunctuationEndBoundary(int offset) {
        return (!isOnPunctuation(offset)) && isAfterPunctuation(offset);
    }

    private boolean isPunctuation(int cp) {
        int type = java.lang.Character.getType(cp);
        return ((((((type == java.lang.Character.CONNECTOR_PUNCTUATION) || (type == java.lang.Character.DASH_PUNCTUATION)) || (type == java.lang.Character.END_PUNCTUATION)) || (type == java.lang.Character.FINAL_QUOTE_PUNCTUATION)) || (type == java.lang.Character.INITIAL_QUOTE_PUNCTUATION)) || (type == java.lang.Character.OTHER_PUNCTUATION)) || (type == java.lang.Character.START_PUNCTUATION);
    }

    private boolean isAfterLetterOrDigit(int shiftedOffset) {
        if ((shiftedOffset >= 1) && (shiftedOffset <= mString.length())) {
            final int codePoint = mString.codePointBefore(shiftedOffset);
            if (java.lang.Character.isLetterOrDigit(codePoint))
                return true;

        }
        return false;
    }

    private boolean isOnLetterOrDigit(int shiftedOffset) {
        if ((shiftedOffset >= 0) && (shiftedOffset < mString.length())) {
            final int codePoint = mString.codePointAt(shiftedOffset);
            if (java.lang.Character.isLetterOrDigit(codePoint))
                return true;

        }
        return false;
    }

    private void checkOffsetIsValid(int shiftedOffset) {
        if ((shiftedOffset < 0) || (shiftedOffset > mString.length())) {
            throw new java.lang.IllegalArgumentException(((((("Invalid offset: " + (shiftedOffset + mOffsetShift)) + ". Valid range is [") + mOffsetShift) + ", ") + (mString.length() + mOffsetShift)) + "]");
        }
    }
}

