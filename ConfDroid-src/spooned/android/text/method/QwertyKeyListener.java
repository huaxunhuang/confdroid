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
 * This is the standard key listener for alphabetic input on qwerty
 * keyboards.  You should generally not need to instantiate this yourself;
 * TextKeyListener will do it for you.
 * <p></p>
 * As for all implementations of {@link KeyListener}, this class is only concerned
 * with hardware keyboards.  Software input methods have no obligation to trigger
 * the methods in this class.
 */
public class QwertyKeyListener extends android.text.method.BaseKeyListener {
    private static android.text.method.QwertyKeyListener[] sInstance = new android.text.method.QwertyKeyListener[android.text.method.TextKeyListener.Capitalize.values().length * 2];

    private static android.text.method.QwertyKeyListener sFullKeyboardInstance;

    private android.text.method.TextKeyListener.Capitalize mAutoCap;

    private boolean mAutoText;

    private boolean mFullKeyboard;

    private QwertyKeyListener(android.text.method.TextKeyListener.Capitalize cap, boolean autoText, boolean fullKeyboard) {
        mAutoCap = cap;
        mAutoText = autoText;
        mFullKeyboard = fullKeyboard;
    }

    public QwertyKeyListener(android.text.method.TextKeyListener.Capitalize cap, boolean autoText) {
        this(cap, autoText, false);
    }

    /**
     * Returns a new or existing instance with the specified capitalization
     * and correction properties.
     */
    public static android.text.method.QwertyKeyListener getInstance(boolean autoText, android.text.method.TextKeyListener.Capitalize cap) {
        int off = (cap.ordinal() * 2) + (autoText ? 1 : 0);
        if (android.text.method.QwertyKeyListener.sInstance[off] == null) {
            android.text.method.QwertyKeyListener.sInstance[off] = new android.text.method.QwertyKeyListener(cap, autoText);
        }
        return android.text.method.QwertyKeyListener.sInstance[off];
    }

    /**
     * Gets an instance of the listener suitable for use with full keyboards.
     * Disables auto-capitalization, auto-text and long-press initiated on-screen
     * character pickers.
     */
    public static android.text.method.QwertyKeyListener getInstanceForFullKeyboard() {
        if (android.text.method.QwertyKeyListener.sFullKeyboardInstance == null) {
            android.text.method.QwertyKeyListener.sFullKeyboardInstance = new android.text.method.QwertyKeyListener(android.text.method.TextKeyListener.Capitalize.NONE, false, true);
        }
        return android.text.method.QwertyKeyListener.sFullKeyboardInstance;
    }

    public int getInputType() {
        return android.text.method.BaseKeyListener.makeTextContentType(mAutoCap, mAutoText);
    }

    public boolean onKeyDown(android.view.View view, android.text.Editable content, int keyCode, android.view.KeyEvent event) {
        int selStart;
        int selEnd;
        int pref = 0;
        if (view != null) {
            pref = android.text.method.TextKeyListener.getInstance().getPrefs(view.getContext());
        }
        {
            int a = android.text.Selection.getSelectionStart(content);
            int b = android.text.Selection.getSelectionEnd(content);
            selStart = java.lang.Math.min(a, b);
            selEnd = java.lang.Math.max(a, b);
            if ((selStart < 0) || (selEnd < 0)) {
                selStart = selEnd = 0;
                android.text.Selection.setSelection(content, 0, 0);
            }
        }
        int activeStart = content.getSpanStart(android.text.method.TextKeyListener.ACTIVE);
        int activeEnd = content.getSpanEnd(android.text.method.TextKeyListener.ACTIVE);
        // QWERTY keyboard normal case
        int i = event.getUnicodeChar(android.text.method.MetaKeyKeyListener.getMetaState(content, event));
        if (!mFullKeyboard) {
            int count = event.getRepeatCount();
            if (((count > 0) && (selStart == selEnd)) && (selStart > 0)) {
                char c = content.charAt(selStart - 1);
                if (((c == i) || (c == java.lang.Character.toUpperCase(i))) && (view != null)) {
                    if (showCharacterPicker(view, content, c, false, count)) {
                        android.text.method.MetaKeyKeyListener.resetMetaState(content);
                        return true;
                    }
                }
            }
        }
        if (i == android.view.KeyCharacterMap.PICKER_DIALOG_INPUT) {
            if (view != null) {
                showCharacterPicker(view, content, android.view.KeyCharacterMap.PICKER_DIALOG_INPUT, true, 1);
            }
            android.text.method.MetaKeyKeyListener.resetMetaState(content);
            return true;
        }
        if (i == android.view.KeyCharacterMap.HEX_INPUT) {
            int start;
            if (selStart == selEnd) {
                start = selEnd;
                while (((start > 0) && ((selEnd - start) < 4)) && (java.lang.Character.digit(content.charAt(start - 1), 16) >= 0)) {
                    start--;
                } 
            } else {
                start = selStart;
            }
            int ch = -1;
            try {
                java.lang.String hex = android.text.TextUtils.substring(content, start, selEnd);
                ch = java.lang.Integer.parseInt(hex, 16);
            } catch (java.lang.NumberFormatException nfe) {
            }
            if (ch >= 0) {
                selStart = start;
                android.text.Selection.setSelection(content, selStart, selEnd);
                i = ch;
            } else {
                i = 0;
            }
        }
        if (i != 0) {
            boolean dead = false;
            if ((i & android.view.KeyCharacterMap.COMBINING_ACCENT) != 0) {
                dead = true;
                i = i & android.view.KeyCharacterMap.COMBINING_ACCENT_MASK;
            }
            if ((activeStart == selStart) && (activeEnd == selEnd)) {
                boolean replace = false;
                if (((selEnd - selStart) - 1) == 0) {
                    char accent = content.charAt(selStart);
                    int composed = event.getDeadChar(accent, i);
                    if (composed != 0) {
                        i = composed;
                        replace = true;
                        dead = false;
                    }
                }
                if (!replace) {
                    android.text.Selection.setSelection(content, selEnd);
                    content.removeSpan(android.text.method.TextKeyListener.ACTIVE);
                    selStart = selEnd;
                }
            }
            if ((((pref & android.text.method.TextKeyListener.AUTO_CAP) != 0) && java.lang.Character.isLowerCase(i)) && android.text.method.TextKeyListener.shouldCap(mAutoCap, content, selStart)) {
                int where = content.getSpanEnd(android.text.method.TextKeyListener.CAPPED);
                int flags = content.getSpanFlags(android.text.method.TextKeyListener.CAPPED);
                if ((where == selStart) && (((flags >> 16) & 0xffff) == i)) {
                    content.removeSpan(android.text.method.TextKeyListener.CAPPED);
                } else {
                    flags = i << 16;
                    i = java.lang.Character.toUpperCase(i);
                    if (selStart == 0)
                        content.setSpan(android.text.method.TextKeyListener.CAPPED, 0, 0, android.text.Spannable.SPAN_MARK_MARK | flags);
                    else
                        content.setSpan(android.text.method.TextKeyListener.CAPPED, selStart - 1, selStart, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE | flags);

                }
            }
            if (selStart != selEnd) {
                android.text.Selection.setSelection(content, selEnd);
            }
            content.setSpan(android.text.method.BaseKeyListener.OLD_SEL_START, selStart, selStart, android.text.Spannable.SPAN_MARK_MARK);
            content.replace(selStart, selEnd, java.lang.String.valueOf(((char) (i))));
            int oldStart = content.getSpanStart(android.text.method.BaseKeyListener.OLD_SEL_START);
            selEnd = android.text.Selection.getSelectionEnd(content);
            if (oldStart < selEnd) {
                content.setSpan(android.text.method.TextKeyListener.LAST_TYPED, oldStart, selEnd, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (dead) {
                    android.text.Selection.setSelection(content, oldStart, selEnd);
                    content.setSpan(android.text.method.TextKeyListener.ACTIVE, oldStart, selEnd, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            android.text.method.MetaKeyKeyListener.adjustMetaAfterKeypress(content);
            // potentially do autotext replacement if the character
            // that was typed was an autotext terminator
            if (((((pref & android.text.method.TextKeyListener.AUTO_TEXT) != 0) && mAutoText) && (((((((((i == ' ') || (i == '\t')) || (i == '\n')) || (i == ',')) || (i == '.')) || (i == '!')) || (i == '?')) || (i == '"')) || (java.lang.Character.getType(i) == java.lang.Character.END_PUNCTUATION))) && (content.getSpanEnd(android.text.method.TextKeyListener.INHIBIT_REPLACEMENT) != oldStart)) {
                int x;
                for (x = oldStart; x > 0; x--) {
                    char c = content.charAt(x - 1);
                    if ((c != '\'') && (!java.lang.Character.isLetter(c))) {
                        break;
                    }
                }
                java.lang.String rep = getReplacement(content, x, oldStart, view);
                if (rep != null) {
                    android.text.method.QwertyKeyListener.Replaced[] repl = content.getSpans(0, content.length(), android.text.method.QwertyKeyListener.Replaced.class);
                    for (int a = 0; a < repl.length; a++)
                        content.removeSpan(repl[a]);

                    char[] orig = new char[oldStart - x];
                    android.text.TextUtils.getChars(content, x, oldStart, orig, 0);
                    content.setSpan(new android.text.method.QwertyKeyListener.Replaced(orig), x, oldStart, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    content.replace(x, oldStart, rep);
                }
            }
            // Replace two spaces by a period and a space.
            if (((pref & android.text.method.TextKeyListener.AUTO_PERIOD) != 0) && mAutoText) {
                selEnd = android.text.Selection.getSelectionEnd(content);
                if ((selEnd - 3) >= 0) {
                    if ((content.charAt(selEnd - 1) == ' ') && (content.charAt(selEnd - 2) == ' ')) {
                        char c = content.charAt(selEnd - 3);
                        for (int j = selEnd - 3; j > 0; j--) {
                            if ((c == '"') || (java.lang.Character.getType(c) == java.lang.Character.END_PUNCTUATION)) {
                                c = content.charAt(j - 1);
                            } else {
                                break;
                            }
                        }
                        if (java.lang.Character.isLetter(c) || java.lang.Character.isDigit(c)) {
                            content.replace(selEnd - 2, selEnd - 1, ".");
                        }
                    }
                }
            }
            return true;
        } else
            if (((keyCode == android.view.KeyEvent.KEYCODE_DEL) && (event.hasNoModifiers() || event.hasModifiers(android.view.KeyEvent.META_ALT_ON))) && (selStart == selEnd)) {
                // special backspace case for undoing autotext
                int consider = 1;
                // if backspacing over the last typed character,
                // it undoes the autotext prior to that character
                // (unless the character typed was newline, in which
                // case this behavior would be confusing)
                if (content.getSpanEnd(android.text.method.TextKeyListener.LAST_TYPED) == selStart) {
                    if (content.charAt(selStart - 1) != '\n')
                        consider = 2;

                }
                android.text.method.QwertyKeyListener.Replaced[] repl = content.getSpans(selStart - consider, selStart, android.text.method.QwertyKeyListener.Replaced.class);
                if (repl.length > 0) {
                    int st = content.getSpanStart(repl[0]);
                    int en = content.getSpanEnd(repl[0]);
                    java.lang.String old = new java.lang.String(repl[0].mText);
                    content.removeSpan(repl[0]);
                    // only cancel the autocomplete if the cursor is at the end of
                    // the replaced span (or after it, because the user is
                    // backspacing over the space after the word, not the word
                    // itself).
                    if (selStart >= en) {
                        content.setSpan(android.text.method.TextKeyListener.INHIBIT_REPLACEMENT, en, en, android.text.Spannable.SPAN_POINT_POINT);
                        content.replace(st, en, old);
                        en = content.getSpanStart(android.text.method.TextKeyListener.INHIBIT_REPLACEMENT);
                        if ((en - 1) >= 0) {
                            content.setSpan(android.text.method.TextKeyListener.INHIBIT_REPLACEMENT, en - 1, en, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else {
                            content.removeSpan(android.text.method.TextKeyListener.INHIBIT_REPLACEMENT);
                        }
                        android.text.method.MetaKeyKeyListener.adjustMetaAfterKeypress(content);
                    } else {
                        android.text.method.MetaKeyKeyListener.adjustMetaAfterKeypress(content);
                        return super.onKeyDown(view, content, keyCode, event);
                    }
                    return true;
                }
            }

        return super.onKeyDown(view, content, keyCode, event);
    }

    private java.lang.String getReplacement(java.lang.CharSequence src, int start, int end, android.view.View view) {
        int len = end - start;
        boolean changecase = false;
        java.lang.String replacement = android.text.AutoText.get(src, start, end, view);
        if (replacement == null) {
            java.lang.String key = android.text.TextUtils.substring(src, start, end).toLowerCase();
            replacement = android.text.AutoText.get(key, 0, end - start, view);
            changecase = true;
            if (replacement == null)
                return null;

        }
        int caps = 0;
        if (changecase) {
            for (int j = start; j < end; j++) {
                if (java.lang.Character.isUpperCase(src.charAt(j)))
                    caps++;

            }
        }
        java.lang.String out;
        if (caps == 0)
            out = replacement;
        else
            if (caps == 1)
                out = android.text.method.QwertyKeyListener.toTitleCase(replacement);
            else
                if (caps == len)
                    out = replacement.toUpperCase();
                else
                    out = android.text.method.QwertyKeyListener.toTitleCase(replacement);



        if ((out.length() == len) && android.text.TextUtils.regionMatches(src, start, out, 0, len))
            return null;

        return out;
    }

    /**
     * Marks the specified region of <code>content</code> as having
     * contained <code>original</code> prior to AutoText replacement.
     * Call this method when you have done or are about to do an
     * AutoText-style replacement on a region of text and want to let
     * the same mechanism (the user pressing DEL immediately after the
     * change) undo the replacement.
     *
     * @param content
     * 		the Editable text where the replacement was made
     * @param start
     * 		the start of the replaced region
     * @param end
     * 		the end of the replaced region; the location of the cursor
     * @param original
     * 		the text to be restored if the user presses DEL
     */
    public static void markAsReplaced(android.text.Spannable content, int start, int end, java.lang.String original) {
        android.text.method.QwertyKeyListener.Replaced[] repl = content.getSpans(0, content.length(), android.text.method.QwertyKeyListener.Replaced.class);
        for (int a = 0; a < repl.length; a++) {
            content.removeSpan(repl[a]);
        }
        int len = original.length();
        char[] orig = new char[len];
        original.getChars(0, len, orig, 0);
        content.setSpan(new android.text.method.QwertyKeyListener.Replaced(orig), start, end, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static android.util.SparseArray<java.lang.String> PICKER_SETS = new android.util.SparseArray<java.lang.String>();

    static {
        android.text.method.QwertyKeyListener.PICKER_SETS.put('A', "\u00c0\u00c1\u00c2\u00c4\u00c6\u00c3\u00c5\u0104\u0100");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('C', "\u00c7\u0106\u010c");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('D', "\u010e");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('E', "\u00c8\u00c9\u00ca\u00cb\u0118\u011a\u0112");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('G', "\u011e");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('L', "\u0141");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('I', "\u00cc\u00cd\u00ce\u00cf\u012a\u0130");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('N', "\u00d1\u0143\u0147");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('O', "\u00d8\u0152\u00d5\u00d2\u00d3\u00d4\u00d6\u014c");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('R', "\u0158");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('S', "\u015a\u0160\u015e");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('T', "\u0164");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('U', "\u00d9\u00da\u00db\u00dc\u016e\u016a");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('Y', "\u00dd\u0178");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('Z', "\u0179\u017b\u017d");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('a', "\u00e0\u00e1\u00e2\u00e4\u00e6\u00e3\u00e5\u0105\u0101");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('c', "\u00e7\u0107\u010d");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('d', "\u010f");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('e', "\u00e8\u00e9\u00ea\u00eb\u0119\u011b\u0113");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('g', "\u011f");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('i', "\u00ec\u00ed\u00ee\u00ef\u012b\u0131");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('l', "\u0142");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('n', "\u00f1\u0144\u0148");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('o', "\u00f8\u0153\u00f5\u00f2\u00f3\u00f4\u00f6\u014d");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('r', "\u0159");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('s', "\u00a7\u00df\u015b\u0161\u015f");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('t', "\u0165");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('u', "\u00f9\u00fa\u00fb\u00fc\u016f\u016b");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('y', "\u00fd\u00ff");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('z', "\u017a\u017c\u017e");
        android.text.method.QwertyKeyListener.PICKER_SETS.put(android.view.KeyCharacterMap.PICKER_DIALOG_INPUT, "\u2026\u00a5\u2022\u00ae\u00a9\u00b1[]{}\\|");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('/', "\\");
        // From packages/inputmethods/LatinIME/res/xml/kbd_symbols.xml
        android.text.method.QwertyKeyListener.PICKER_SETS.put('1', "\u00b9\u00bd\u2153\u00bc\u215b");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('2', "\u00b2\u2154");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('3', "\u00b3\u00be\u215c");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('4', "\u2074");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('5', "\u215d");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('7', "\u215e");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('0', "\u207f\u2205");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('$', "\u00a2\u00a3\u20ac\u00a5\u20a3\u20a4\u20b1");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('%', "\u2030");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('*', "\u2020\u2021");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('-', "\u2013\u2014");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('+', "\u00b1");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('(', "[{<");
        android.text.method.QwertyKeyListener.PICKER_SETS.put(')', "]}>");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('!', "\u00a1");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('"', "\u201c\u201d\u00ab\u00bb\u02dd");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('?', "\u00bf");
        android.text.method.QwertyKeyListener.PICKER_SETS.put(',', "\u201a\u201e");
        // From packages/inputmethods/LatinIME/res/xml/kbd_symbols_shift.xml
        android.text.method.QwertyKeyListener.PICKER_SETS.put('=', "\u2260\u2248\u221e");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('<', "\u2264\u00ab\u2039");
        android.text.method.QwertyKeyListener.PICKER_SETS.put('>', "\u2265\u00bb\u203a");
    }

    private boolean showCharacterPicker(android.view.View view, android.text.Editable content, char c, boolean insert, int count) {
        java.lang.String set = android.text.method.QwertyKeyListener.PICKER_SETS.get(c);
        if (set == null) {
            return false;
        }
        if (count == 1) {
            new android.text.method.CharacterPickerDialog(view.getContext(), view, content, set, insert).show();
        }
        return true;
    }

    private static java.lang.String toTitleCase(java.lang.String src) {
        return java.lang.Character.toUpperCase(src.charAt(0)) + src.substring(1);
    }

    /* package */
    static class Replaced implements android.text.NoCopySpan {
        public Replaced(char[] text) {
            mText = text;
        }

        private char[] mText;
    }
}

