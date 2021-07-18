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
 * This is the standard key listener for alphabetic input on 12-key
 * keyboards.  You should generally not need to instantiate this yourself;
 * TextKeyListener will do it for you.
 * <p></p>
 * As for all implementations of {@link KeyListener}, this class is only concerned
 * with hardware keyboards.  Software input methods have no obligation to trigger
 * the methods in this class.
 */
public class MultiTapKeyListener extends android.text.method.BaseKeyListener implements android.text.SpanWatcher {
    private static android.text.method.MultiTapKeyListener[] sInstance = new android.text.method.MultiTapKeyListener[android.text.method.TextKeyListener.Capitalize.values().length * 2];

    private static final android.util.SparseArray<java.lang.String> sRecs = new android.util.SparseArray<java.lang.String>();

    private android.text.method.TextKeyListener.Capitalize mCapitalize;

    private boolean mAutoText;

    static {
        android.text.method.MultiTapKeyListener.sRecs.put(android.view.KeyEvent.KEYCODE_1, ".,1!@#$%^&*:/?'=()");
        android.text.method.MultiTapKeyListener.sRecs.put(android.view.KeyEvent.KEYCODE_2, "abc2ABC");
        android.text.method.MultiTapKeyListener.sRecs.put(android.view.KeyEvent.KEYCODE_3, "def3DEF");
        android.text.method.MultiTapKeyListener.sRecs.put(android.view.KeyEvent.KEYCODE_4, "ghi4GHI");
        android.text.method.MultiTapKeyListener.sRecs.put(android.view.KeyEvent.KEYCODE_5, "jkl5JKL");
        android.text.method.MultiTapKeyListener.sRecs.put(android.view.KeyEvent.KEYCODE_6, "mno6MNO");
        android.text.method.MultiTapKeyListener.sRecs.put(android.view.KeyEvent.KEYCODE_7, "pqrs7PQRS");
        android.text.method.MultiTapKeyListener.sRecs.put(android.view.KeyEvent.KEYCODE_8, "tuv8TUV");
        android.text.method.MultiTapKeyListener.sRecs.put(android.view.KeyEvent.KEYCODE_9, "wxyz9WXYZ");
        android.text.method.MultiTapKeyListener.sRecs.put(android.view.KeyEvent.KEYCODE_0, "0+");
        android.text.method.MultiTapKeyListener.sRecs.put(android.view.KeyEvent.KEYCODE_POUND, " ");
    }

    public MultiTapKeyListener(android.text.method.TextKeyListener.Capitalize cap, boolean autotext) {
        mCapitalize = cap;
        mAutoText = autotext;
    }

    /**
     * Returns a new or existing instance with the specified capitalization
     * and correction properties.
     */
    public static android.text.method.MultiTapKeyListener getInstance(boolean autotext, android.text.method.TextKeyListener.Capitalize cap) {
        int off = (cap.ordinal() * 2) + (autotext ? 1 : 0);
        if (android.text.method.MultiTapKeyListener.sInstance[off] == null) {
            android.text.method.MultiTapKeyListener.sInstance[off] = new android.text.method.MultiTapKeyListener(cap, autotext);
        }
        return android.text.method.MultiTapKeyListener.sInstance[off];
    }

    public int getInputType() {
        return android.text.method.BaseKeyListener.makeTextContentType(mCapitalize, mAutoText);
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
        }
        int activeStart = content.getSpanStart(android.text.method.TextKeyListener.ACTIVE);
        int activeEnd = content.getSpanEnd(android.text.method.TextKeyListener.ACTIVE);
        // now for the multitap cases...
        // Try to increment the character we were working on before
        // if we have one and it's still the same key.
        int rec = (content.getSpanFlags(android.text.method.TextKeyListener.ACTIVE) & android.text.Spannable.SPAN_USER) >>> android.text.Spannable.SPAN_USER_SHIFT;
        if (((((activeStart == selStart) && (activeEnd == selEnd)) && ((selEnd - selStart) == 1)) && (rec >= 0)) && (rec < android.text.method.MultiTapKeyListener.sRecs.size())) {
            if (keyCode == android.view.KeyEvent.KEYCODE_STAR) {
                char current = content.charAt(selStart);
                if (java.lang.Character.isLowerCase(current)) {
                    content.replace(selStart, selEnd, java.lang.String.valueOf(current).toUpperCase());
                    android.text.method.MultiTapKeyListener.removeTimeouts(content);
                    new android.text.method.MultiTapKeyListener.Timeout(content);// for its side effects

                    return true;
                }
                if (java.lang.Character.isUpperCase(current)) {
                    content.replace(selStart, selEnd, java.lang.String.valueOf(current).toLowerCase());
                    android.text.method.MultiTapKeyListener.removeTimeouts(content);
                    new android.text.method.MultiTapKeyListener.Timeout(content);// for its side effects

                    return true;
                }
            }
            if (android.text.method.MultiTapKeyListener.sRecs.indexOfKey(keyCode) == rec) {
                java.lang.String val = android.text.method.MultiTapKeyListener.sRecs.valueAt(rec);
                char ch = content.charAt(selStart);
                int ix = val.indexOf(ch);
                if (ix >= 0) {
                    ix = (ix + 1) % val.length();
                    content.replace(selStart, selEnd, val, ix, ix + 1);
                    android.text.method.MultiTapKeyListener.removeTimeouts(content);
                    new android.text.method.MultiTapKeyListener.Timeout(content);// for its side effects

                    return true;
                }
            }
            // Is this key one we know about at all?  If so, acknowledge
            // that the selection is our fault but the key has changed
            // or the text no longer matches, so move the selection over
            // so that it inserts instead of replaces.
            rec = android.text.method.MultiTapKeyListener.sRecs.indexOfKey(keyCode);
            if (rec >= 0) {
                android.text.Selection.setSelection(content, selEnd, selEnd);
                selStart = selEnd;
            }
        } else {
            rec = android.text.method.MultiTapKeyListener.sRecs.indexOfKey(keyCode);
        }
        if (rec >= 0) {
            // We have a valid key.  Replace the selection or insertion point
            // with the first character for that key, and remember what
            // record it came from for next time.
            java.lang.String val = android.text.method.MultiTapKeyListener.sRecs.valueAt(rec);
            int off = 0;
            if (((pref & android.text.method.TextKeyListener.AUTO_CAP) != 0) && android.text.method.TextKeyListener.shouldCap(mCapitalize, content, selStart)) {
                for (int i = 0; i < val.length(); i++) {
                    if (java.lang.Character.isUpperCase(val.charAt(i))) {
                        off = i;
                        break;
                    }
                }
            }
            if (selStart != selEnd) {
                android.text.Selection.setSelection(content, selEnd);
            }
            content.setSpan(android.text.method.BaseKeyListener.OLD_SEL_START, selStart, selStart, android.text.Spannable.SPAN_MARK_MARK);
            content.replace(selStart, selEnd, val, off, off + 1);
            int oldStart = content.getSpanStart(android.text.method.BaseKeyListener.OLD_SEL_START);
            selEnd = android.text.Selection.getSelectionEnd(content);
            if (selEnd != oldStart) {
                android.text.Selection.setSelection(content, oldStart, selEnd);
                content.setSpan(android.text.method.TextKeyListener.LAST_TYPED, oldStart, selEnd, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                content.setSpan(android.text.method.TextKeyListener.ACTIVE, oldStart, selEnd, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE | (rec << android.text.Spannable.SPAN_USER_SHIFT));
            }
            android.text.method.MultiTapKeyListener.removeTimeouts(content);
            new android.text.method.MultiTapKeyListener.Timeout(content);// for its side effects

            // Set up the callback so we can remove the timeout if the
            // cursor moves.
            if (content.getSpanStart(this) < 0) {
                android.text.method.KeyListener[] methods = content.getSpans(0, content.length(), android.text.method.KeyListener.class);
                for (java.lang.Object method : methods) {
                    content.removeSpan(method);
                }
                content.setSpan(this, 0, content.length(), android.text.Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
            return true;
        }
        return super.onKeyDown(view, content, keyCode, event);
    }

    public void onSpanChanged(android.text.Spannable buf, java.lang.Object what, int s, int e, int start, int stop) {
        if (what == android.text.Selection.SELECTION_END) {
            buf.removeSpan(android.text.method.TextKeyListener.ACTIVE);
            android.text.method.MultiTapKeyListener.removeTimeouts(buf);
        }
    }

    private static void removeTimeouts(android.text.Spannable buf) {
        android.text.method.MultiTapKeyListener.Timeout[] timeout = buf.getSpans(0, buf.length(), android.text.method.MultiTapKeyListener.Timeout.class);
        for (int i = 0; i < timeout.length; i++) {
            android.text.method.MultiTapKeyListener.Timeout t = timeout[i];
            t.removeCallbacks(t);
            t.mBuffer = null;
            buf.removeSpan(t);
        }
    }

    private class Timeout extends android.os.Handler implements java.lang.Runnable {
        public Timeout(android.text.Editable buffer) {
            mBuffer = buffer;
            mBuffer.setSpan(this, 0, mBuffer.length(), android.text.Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            postAtTime(this, android.os.SystemClock.uptimeMillis() + 2000);
        }

        public void run() {
            android.text.Spannable buf = mBuffer;
            if (buf != null) {
                int st = android.text.Selection.getSelectionStart(buf);
                int en = android.text.Selection.getSelectionEnd(buf);
                int start = buf.getSpanStart(android.text.method.TextKeyListener.ACTIVE);
                int end = buf.getSpanEnd(android.text.method.TextKeyListener.ACTIVE);
                if ((st == start) && (en == end)) {
                    android.text.Selection.setSelection(buf, android.text.Selection.getSelectionEnd(buf));
                }
                buf.removeSpan(this);
            }
        }

        private android.text.Editable mBuffer;
    }

    public void onSpanAdded(android.text.Spannable s, java.lang.Object what, int start, int end) {
    }

    public void onSpanRemoved(android.text.Spannable s, java.lang.Object what, int start, int end) {
    }
}

