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
 * For numeric text entry
 * <p></p>
 * As for all implementations of {@link KeyListener}, this class is only concerned
 * with hardware keyboards.  Software input methods have no obligation to trigger
 * the methods in this class.
 */
public abstract class NumberKeyListener extends android.text.method.BaseKeyListener implements android.text.InputFilter {
    /**
     * You can say which characters you can accept.
     */
    protected abstract char[] getAcceptedChars();

    protected int lookup(android.view.KeyEvent event, android.text.Spannable content) {
        return event.getMatch(getAcceptedChars(), android.text.method.MetaKeyKeyListener.getMetaState(content, event));
    }

    public java.lang.CharSequence filter(java.lang.CharSequence source, int start, int end, android.text.Spanned dest, int dstart, int dend) {
        char[] accept = getAcceptedChars();
        boolean filter = false;
        int i;
        for (i = start; i < end; i++) {
            if (!android.text.method.NumberKeyListener.ok(accept, source.charAt(i))) {
                break;
            }
        }
        if (i == end) {
            // It was all OK.
            return null;
        }
        if ((end - start) == 1) {
            // It was not OK, and there is only one char, so nothing remains.
            return "";
        }
        android.text.SpannableStringBuilder filtered = new android.text.SpannableStringBuilder(source, start, end);
        i -= start;
        end -= start;
        int len = end - start;
        // Only count down to i because the chars before that were all OK.
        for (int j = end - 1; j >= i; j--) {
            if (!android.text.method.NumberKeyListener.ok(accept, source.charAt(j))) {
                filtered.delete(j, j + 1);
            }
        }
        return filtered;
    }

    protected static boolean ok(char[] accept, char c) {
        for (int i = accept.length - 1; i >= 0; i--) {
            if (accept[i] == c) {
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public boolean onKeyDown(android.view.View view, android.text.Editable content, int keyCode, android.view.KeyEvent event) {
        int selStart;
        int selEnd;
        {
            int a = android.text.Selection.getSelectionStart(content);
            int b = android.text.Selection.getSelectionEnd(content);
            selStart = java.lang.Math.min(a, b);
            selEnd = java.lang.Math.max(a, b);
        }
        if ((selStart < 0) || (selEnd < 0)) {
            selStart = selEnd = 0;
            android.text.Selection.setSelection(content, 0);
        }
        int i = (event != null) ? lookup(event, content) : 0;
        int repeatCount = (event != null) ? event.getRepeatCount() : 0;
        if (repeatCount == 0) {
            if (i != 0) {
                if (selStart != selEnd) {
                    android.text.Selection.setSelection(content, selEnd);
                }
                content.replace(selStart, selEnd, java.lang.String.valueOf(((char) (i))));
                android.text.method.MetaKeyKeyListener.adjustMetaAfterKeypress(content);
                return true;
            }
        } else
            if ((i == '0') && (repeatCount == 1)) {
                // Pretty hackish, it replaces the 0 with the +
                if (((selStart == selEnd) && (selEnd > 0)) && (content.charAt(selStart - 1) == '0')) {
                    content.replace(selStart - 1, selEnd, java.lang.String.valueOf('+'));
                    android.text.method.MetaKeyKeyListener.adjustMetaAfterKeypress(content);
                    return true;
                }
            }

        android.text.method.MetaKeyKeyListener.adjustMetaAfterKeypress(content);
        return super.onKeyDown(view, content, keyCode, event);
    }
}

