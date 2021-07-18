/**
 * Copyright (C) 2010 The Android Open Source Project
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


/**
 * TextViewPatchTest tests {@link TextView}'s definition of word. Finds and
 * verifies word limits to be in strings containing different kinds of
 * characters.
 */
public class TextViewWordLimitsTest extends android.test.AndroidTestCase {
    android.widget.TextView mTv = null;

    java.lang.reflect.Method mGetWordLimits;

    java.lang.reflect.Method mSelectCurrentWord;

    java.lang.reflect.Field mContextMenuTriggeredByKey;

    java.lang.reflect.Field mSelectionControllerEnabled;

    /**
     * Sets up common fields used in all test cases.
     *
     * @throws NoSuchFieldException
     * 		
     * @throws SecurityException
     * 		
     */
    @java.lang.Override
    protected void setUp() throws java.lang.NoSuchFieldException, java.lang.NoSuchMethodException, java.lang.SecurityException {
        mTv = new android.widget.TextView(getContext());
        mTv.setInputType(InputType.TYPE_CLASS_TEXT);
        mGetWordLimits = mTv.getClass().getDeclaredMethod("getWordLimitsAt", new java.lang.Class[]{ int.class });
        mGetWordLimits.setAccessible(true);
        mSelectCurrentWord = mTv.getClass().getDeclaredMethod("selectCurrentWord", new java.lang.Class[]{  });
        mSelectCurrentWord.setAccessible(true);
        mContextMenuTriggeredByKey = mTv.getClass().getDeclaredField("mContextMenuTriggeredByKey");
        mContextMenuTriggeredByKey.setAccessible(true);
        mSelectionControllerEnabled = mTv.getClass().getDeclaredField("mSelectionControllerEnabled");
        mSelectionControllerEnabled.setAccessible(true);
    }

    /**
     * Calculate and verify word limits. Depends on the TextView implementation.
     * Uses a private method and internal data representation.
     *
     * @param text
     * 		Text to select a word from
     * @param pos
     * 		Position to expand word around
     * @param correctStart
     * 		Correct start position for the word
     * @param correctEnd
     * 		Correct end position for the word
     * @throws InvocationTargetException
     * 		
     * @throws IllegalAccessException
     * 		
     * @throws IllegalArgumentException
     * 		
     * @throws InvocationTargetException
     * 		
     * @throws IllegalAccessException
     * 		
     */
    private void verifyWordLimits(java.lang.String text, int pos, int correctStart, int correctEnd) throws java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        mTv.setText(text, android.widget.TextView.BufferType.SPANNABLE);
        long limits = ((java.lang.Long) (mGetWordLimits.invoke(mTv, new java.lang.Object[]{ new java.lang.Integer(pos) })));
        int actualStart = ((int) (limits >>> 32));
        int actualEnd = ((int) (limits & 0xffffffffL));
        assertEquals(correctStart, actualStart);
        assertEquals(correctEnd, actualEnd);
    }

    private void verifySelectCurrentWord(android.text.Spannable text, int selectionStart, int selectionEnd, int correctStart, int correctEnd) throws java.lang.IllegalAccessException, java.lang.reflect.InvocationTargetException {
        mTv.setText(text, android.widget.TextView.BufferType.SPANNABLE);
        android.text.Selection.setSelection(((android.text.Spannable) (mTv.getText())), selectionStart, selectionEnd);
        mContextMenuTriggeredByKey.setBoolean(mTv, true);
        mSelectionControllerEnabled.setBoolean(mTv, true);
        mSelectCurrentWord.invoke(mTv);
        assertEquals(correctStart, mTv.getSelectionStart());
        assertEquals(correctEnd, mTv.getSelectionEnd());
    }

    /**
     * Corner cases for string length.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testLengths() throws java.lang.Exception {
        final java.lang.String ONE_TWO = "one two";
        final java.lang.String EMPTY = "";
        final java.lang.String TOOLONG = "ThisWordIsTooLongToBeDefinedAsAWordInTheSenseUsedInTextView";
        // Select first word
        verifyWordLimits(ONE_TWO, 0, 0, 3);
        verifyWordLimits(ONE_TWO, 3, 0, 3);
        // Select last word
        verifyWordLimits(ONE_TWO, 4, 4, 7);
        verifyWordLimits(ONE_TWO, 7, 4, 7);
        // Empty string
        verifyWordLimits(EMPTY, 0, -1, -1);
        // Too long word
        verifyWordLimits(TOOLONG, 0, -1, -1);
    }

    /**
     * Unicode classes included.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testIncludedClasses() throws java.lang.Exception {
        final java.lang.String LOWER = "njlj";
        final java.lang.String UPPER = "NJLJ";
        final java.lang.String TITLECASE = "\u01c8\u01cb\u01f2";// Lj Nj Dz

        final java.lang.String OTHER = "\u3042\u3044\u3046";// Hiragana AIU

        final java.lang.String MODIFIER = "\u02c6\u02ca\u02cb";// Circumflex Acute Grave

        // Each string contains a single valid word
        verifyWordLimits(LOWER, 1, 0, 4);
        verifyWordLimits(UPPER, 1, 0, 4);
        verifyWordLimits(TITLECASE, 1, 0, 3);
        verifyWordLimits(OTHER, 1, 0, 3);
        verifyWordLimits(MODIFIER, 1, 0, 3);
    }

    /**
     * Unicode classes included if combined with a letter.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testPartlyIncluded() throws java.lang.Exception {
        final java.lang.String NUMBER = "123";
        final java.lang.String NUMBER_LOWER = "1st";
        final java.lang.String APOSTROPHE = "''";
        final java.lang.String APOSTROPHE_LOWER = "'Android's'";
        // Pure decimal number is ignored
        verifyWordLimits(NUMBER, 1, -1, -1);
        // Number with letter is valid
        verifyWordLimits(NUMBER_LOWER, 1, 0, 3);
        // Stand apostrophes are ignore
        verifyWordLimits(APOSTROPHE, 1, -1, -1);
        // Apostrophes are accepted if they are a part of a word
        verifyWordLimits(APOSTROPHE_LOWER, 1, 0, 11);
    }

    /**
     * Unicode classes included if combined with a letter.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testFinalSeparator() throws java.lang.Exception {
        final java.lang.String PUNCTUATION = "abc, def.";
        // Starting from the comma
        verifyWordLimits(PUNCTUATION, 3, 0, 3);
        verifyWordLimits(PUNCTUATION, 4, 0, 4);
        // Starting from the final period
        verifyWordLimits(PUNCTUATION, 8, 5, 8);
        verifyWordLimits(PUNCTUATION, 9, 5, 9);
    }

    /**
     * Unicode classes other than listed in testIncludedClasses and
     * testPartlyIncluded act as word separators.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testNotIncluded() throws java.lang.Exception {
        // Selection of character classes excluded
        final java.lang.String MARK_NONSPACING = "a\u030a";// a Combining ring above

        final java.lang.String PUNCTUATION_OPEN_CLOSE = "(c)";
        // Parenthesis
        final java.lang.String PUNCTUATION_DASH = "non-fiction";// Hyphen

        final java.lang.String PUNCTUATION_OTHER = "b&b";
        // Ampersand
        final java.lang.String SYMBOL_OTHER = "Android\u00ae";// Registered

        final java.lang.String SEPARATOR_SPACE = "one two";// Space

        // "a"
        verifyWordLimits(MARK_NONSPACING, 1, 0, 1);
        // "c"
        verifyWordLimits(PUNCTUATION_OPEN_CLOSE, 1, 1, 2);
        // "non-"
        verifyWordLimits(PUNCTUATION_DASH, 3, 0, 3);
        verifyWordLimits(PUNCTUATION_DASH, 4, 4, 11);
        // "b"
        verifyWordLimits(PUNCTUATION_OTHER, 0, 0, 1);
        verifyWordLimits(PUNCTUATION_OTHER, 1, 0, 1);
        verifyWordLimits(PUNCTUATION_OTHER, 2, 0, 3);// & is considered a punctuation sign.

        verifyWordLimits(PUNCTUATION_OTHER, 3, 2, 3);
        // "Android"
        verifyWordLimits(SYMBOL_OTHER, 7, 0, 7);
        verifyWordLimits(SYMBOL_OTHER, 8, -1, -1);
        // "one"
        verifyWordLimits(SEPARATOR_SPACE, 1, 0, 3);
    }

    /**
     * Surrogate characters are treated as their code points.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testSurrogate() throws java.lang.Exception {
        final java.lang.String SURROGATE_LETTER = "\ud800\udc00\ud800\udc01\ud800\udc02";// Linear B AEI

        final java.lang.String SURROGATE_SYMBOL = "\ud83d\ude01\ud83d\ude02\ud83d\ude03";// Three smileys

        // Letter Other is included even when coded as surrogate pairs
        verifyWordLimits(SURROGATE_LETTER, 0, 0, 6);
        verifyWordLimits(SURROGATE_LETTER, 1, 0, 6);
        verifyWordLimits(SURROGATE_LETTER, 2, 0, 6);
        verifyWordLimits(SURROGATE_LETTER, 3, 0, 6);
        verifyWordLimits(SURROGATE_LETTER, 4, 0, 6);
        verifyWordLimits(SURROGATE_LETTER, 5, 0, 6);
        verifyWordLimits(SURROGATE_LETTER, 6, 0, 6);
        // Not included classes are ignored even when coded as surrogate pairs
        verifyWordLimits(SURROGATE_SYMBOL, 0, -1, -1);
        verifyWordLimits(SURROGATE_SYMBOL, 1, -1, -1);
        verifyWordLimits(SURROGATE_SYMBOL, 2, -1, -1);
        verifyWordLimits(SURROGATE_SYMBOL, 3, -1, -1);
        verifyWordLimits(SURROGATE_SYMBOL, 4, -1, -1);
        verifyWordLimits(SURROGATE_SYMBOL, 5, -1, -1);
        verifyWordLimits(SURROGATE_SYMBOL, 6, -1, -1);
    }

    /**
     * Selection is used if present and valid word.
     */
    @android.test.suitebuilder.annotation.LargeTest
    public void testSelectCurrentWord() throws java.lang.Exception {
        android.text.SpannableString textLower = new android.text.SpannableString("first second");
        android.text.SpannableString textOther = new android.text.SpannableString("\u3042\u00c44\u00c46");// Hiragana AIU

        android.text.SpannableString textDash = new android.text.SpannableString("non-fiction");// Hyphen

        android.text.SpannableString textPunctOther = new android.text.SpannableString("b&b");
        // Ampersand
        android.text.SpannableString textSymbolOther = new android.text.SpannableString("Android\u00ae");// Registered

        // Valid selection - Letter, Lower
        verifySelectCurrentWord(textLower, 2, 5, 0, 5);
        // Adding the space spreads to the second word
        verifySelectCurrentWord(textLower, 2, 6, 0, 12);
        // Valid selection -- Letter, Other
        verifySelectCurrentWord(textOther, 1, 2, 0, 5);
        // Zero-width selection is interpreted as a cursor and the selection is ignored
        verifySelectCurrentWord(textLower, 2, 2, 0, 5);
        // Hyphen is part of selection
        verifySelectCurrentWord(textDash, 2, 5, 0, 11);
        // Ampersand part of selection or not
        verifySelectCurrentWord(textPunctOther, 1, 2, 0, 3);
        verifySelectCurrentWord(textPunctOther, 1, 3, 0, 3);
        // (R) part of the selection
        verifySelectCurrentWord(textSymbolOther, 2, 7, 0, 7);
        verifySelectCurrentWord(textSymbolOther, 2, 8, 0, 8);
    }
}

