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
package android.text.method;


/**
 * For digits-only text entry
 * <p></p>
 * As for all implementations of {@link KeyListener}, this class is only concerned
 * with hardware keyboards.  Software input methods have no obligation to trigger
 * the methods in this class.
 */
public class DigitsKeyListener extends android.text.method.NumberKeyListener {
    private char[] mAccepted;

    private boolean mSign;

    private boolean mDecimal;

    private static final int SIGN = 1;

    private static final int DECIMAL = 2;

    @java.lang.Override
    protected char[] getAcceptedChars() {
        return mAccepted;
    }

    /**
     * The characters that are used.
     *
     * @see KeyEvent#getMatch
     * @see #getAcceptedChars
     */
    private static final char[][] CHARACTERS = new char[][]{ new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' }, new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '+' }, new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' }, new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '+', '.' } };

    private static boolean isSignChar(final char c) {
        return (c == '-') || (c == '+');
    }

    // TODO: Needs internationalization
    private static boolean isDecimalPointChar(final char c) {
        return c == '.';
    }

    /**
     * Allocates a DigitsKeyListener that accepts the digits 0 through 9.
     */
    public DigitsKeyListener() {
        this(false, false);
    }

    /**
     * Allocates a DigitsKeyListener that accepts the digits 0 through 9,
     * plus the minus sign (only at the beginning) and/or decimal point
     * (only one per field) if specified.
     */
    public DigitsKeyListener(boolean sign, boolean decimal) {
        mSign = sign;
        mDecimal = decimal;
        int kind = (sign ? android.text.method.DigitsKeyListener.SIGN : 0) | (decimal ? android.text.method.DigitsKeyListener.DECIMAL : 0);
        mAccepted = android.text.method.DigitsKeyListener.CHARACTERS[kind];
    }

    /**
     * Returns a DigitsKeyListener that accepts the digits 0 through 9.
     */
    public static android.text.method.DigitsKeyListener getInstance() {
        return android.text.method.DigitsKeyListener.getInstance(false, false);
    }

    /**
     * Returns a DigitsKeyListener that accepts the digits 0 through 9,
     * plus the minus sign (only at the beginning) and/or decimal point
     * (only one per field) if specified.
     */
    public static android.text.method.DigitsKeyListener getInstance(boolean sign, boolean decimal) {
        int kind = (sign ? android.text.method.DigitsKeyListener.SIGN : 0) | (decimal ? android.text.method.DigitsKeyListener.DECIMAL : 0);
        if (android.text.method.DigitsKeyListener.sInstance[kind] != null)
            return android.text.method.DigitsKeyListener.sInstance[kind];

        android.text.method.DigitsKeyListener.sInstance[kind] = new android.text.method.DigitsKeyListener(sign, decimal);
        return android.text.method.DigitsKeyListener.sInstance[kind];
    }

    /**
     * Returns a DigitsKeyListener that accepts only the characters
     * that appear in the specified String.  Note that not all characters
     * may be available on every keyboard.
     */
    public static android.text.method.DigitsKeyListener getInstance(java.lang.String accepted) {
        // TODO: do we need a cache of these to avoid allocating?
        android.text.method.DigitsKeyListener dim = new android.text.method.DigitsKeyListener();
        dim.mAccepted = new char[accepted.length()];
        accepted.getChars(0, accepted.length(), dim.mAccepted, 0);
        return dim;
    }

    public int getInputType() {
        int contentType = android.text.InputType.TYPE_CLASS_NUMBER;
        if (mSign) {
            contentType |= android.text.InputType.TYPE_NUMBER_FLAG_SIGNED;
        }
        if (mDecimal) {
            contentType |= android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;
        }
        return contentType;
    }

    @java.lang.Override
    public java.lang.CharSequence filter(java.lang.CharSequence source, int start, int end, android.text.Spanned dest, int dstart, int dend) {
        java.lang.CharSequence out = super.filter(source, start, end, dest, dstart, dend);
        if ((mSign == false) && (mDecimal == false)) {
            return out;
        }
        if (out != null) {
            source = out;
            start = 0;
            end = out.length();
        }
        int sign = -1;
        int decimal = -1;
        int dlen = dest.length();
        /* Find out if the existing text has a sign or decimal point characters. */
        for (int i = 0; i < dstart; i++) {
            char c = dest.charAt(i);
            if (android.text.method.DigitsKeyListener.isSignChar(c)) {
                sign = i;
            } else
                if (android.text.method.DigitsKeyListener.isDecimalPointChar(c)) {
                    decimal = i;
                }

        }
        for (int i = dend; i < dlen; i++) {
            char c = dest.charAt(i);
            if (android.text.method.DigitsKeyListener.isSignChar(c)) {
                return "";// Nothing can be inserted in front of a sign character.

            } else
                if (android.text.method.DigitsKeyListener.isDecimalPointChar(c)) {
                    decimal = i;
                }

        }
        /* If it does, we must strip them out from the source.
        In addition, a sign character must be the very first character,
        and nothing can be inserted before an existing sign character.
        Go in reverse order so the offsets are stable.
         */
        android.text.SpannableStringBuilder stripped = null;
        for (int i = end - 1; i >= start; i--) {
            char c = source.charAt(i);
            boolean strip = false;
            if (android.text.method.DigitsKeyListener.isSignChar(c)) {
                if ((i != start) || (dstart != 0)) {
                    strip = true;
                } else
                    if (sign >= 0) {
                        strip = true;
                    } else {
                        sign = i;
                    }

            } else
                if (android.text.method.DigitsKeyListener.isDecimalPointChar(c)) {
                    if (decimal >= 0) {
                        strip = true;
                    } else {
                        decimal = i;
                    }
                }

            if (strip) {
                if (end == (start + 1)) {
                    return "";// Only one character, and it was stripped.

                }
                if (stripped == null) {
                    stripped = new android.text.SpannableStringBuilder(source, start, end);
                }
                stripped.delete(i - start, (i + 1) - start);
            }
        }
        if (stripped != null) {
            return stripped;
        } else
            if (out != null) {
                return out;
            } else {
                return null;
            }

    }

    private static android.text.method.DigitsKeyListener[] sInstance = new android.text.method.DigitsKeyListener[4];
}

