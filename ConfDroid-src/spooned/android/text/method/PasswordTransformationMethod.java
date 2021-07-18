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


public class PasswordTransformationMethod implements android.text.TextWatcher , android.text.method.TransformationMethod {
    public java.lang.CharSequence getTransformation(java.lang.CharSequence source, android.view.View view) {
        if (source instanceof android.text.Spannable) {
            android.text.Spannable sp = ((android.text.Spannable) (source));
            /* Remove any references to other views that may still be
            attached.  This will happen when you flip the screen
            while a password field is showing; there will still
            be references to the old EditText in the text.
             */
            android.text.method.PasswordTransformationMethod.ViewReference[] vr = sp.getSpans(0, sp.length(), android.text.method.PasswordTransformationMethod.ViewReference.class);
            for (int i = 0; i < vr.length; i++) {
                sp.removeSpan(vr[i]);
            }
            android.text.method.PasswordTransformationMethod.removeVisibleSpans(sp);
            sp.setSpan(new android.text.method.PasswordTransformationMethod.ViewReference(view), 0, 0, android.text.Spannable.SPAN_POINT_POINT);
        }
        return new android.text.method.PasswordTransformationMethod.PasswordCharSequence(source);
    }

    public static android.text.method.PasswordTransformationMethod getInstance() {
        if (android.text.method.PasswordTransformationMethod.sInstance != null)
            return android.text.method.PasswordTransformationMethod.sInstance;

        android.text.method.PasswordTransformationMethod.sInstance = new android.text.method.PasswordTransformationMethod();
        return android.text.method.PasswordTransformationMethod.sInstance;
    }

    public void beforeTextChanged(java.lang.CharSequence s, int start, int count, int after) {
        // This callback isn't used.
    }

    public void onTextChanged(java.lang.CharSequence s, int start, int before, int count) {
        if (s instanceof android.text.Spannable) {
            android.text.Spannable sp = ((android.text.Spannable) (s));
            android.text.method.PasswordTransformationMethod.ViewReference[] vr = sp.getSpans(0, s.length(), android.text.method.PasswordTransformationMethod.ViewReference.class);
            if (vr.length == 0) {
                return;
            }
            /* There should generally only be one ViewReference in the text,
            but make sure to look through all of them if necessary in case
            something strange is going on.  (We might still end up with
            multiple ViewReferences if someone moves text from one password
            field to another.)
             */
            android.view.View v = null;
            for (int i = 0; (v == null) && (i < vr.length); i++) {
                v = vr[i].get();
            }
            if (v == null) {
                return;
            }
            int pref = android.text.method.TextKeyListener.getInstance().getPrefs(v.getContext());
            if ((pref & android.text.method.TextKeyListener.SHOW_PASSWORD) != 0) {
                if (count > 0) {
                    android.text.method.PasswordTransformationMethod.removeVisibleSpans(sp);
                    if (count == 1) {
                        sp.setSpan(new android.text.method.PasswordTransformationMethod.Visible(sp, this), start, start + count, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
    }

    public void afterTextChanged(android.text.Editable s) {
        // This callback isn't used.
    }

    public void onFocusChanged(android.view.View view, java.lang.CharSequence sourceText, boolean focused, int direction, android.graphics.Rect previouslyFocusedRect) {
        if (!focused) {
            if (sourceText instanceof android.text.Spannable) {
                android.text.Spannable sp = ((android.text.Spannable) (sourceText));
                android.text.method.PasswordTransformationMethod.removeVisibleSpans(sp);
            }
        }
    }

    private static void removeVisibleSpans(android.text.Spannable sp) {
        android.text.method.PasswordTransformationMethod.Visible[] old = sp.getSpans(0, sp.length(), android.text.method.PasswordTransformationMethod.Visible.class);
        for (int i = 0; i < old.length; i++) {
            sp.removeSpan(old[i]);
        }
    }

    private static class PasswordCharSequence implements android.text.GetChars , java.lang.CharSequence {
        public PasswordCharSequence(java.lang.CharSequence source) {
            mSource = source;
        }

        public int length() {
            return mSource.length();
        }

        public char charAt(int i) {
            if (mSource instanceof android.text.Spanned) {
                android.text.Spanned sp = ((android.text.Spanned) (mSource));
                int st = sp.getSpanStart(android.text.method.TextKeyListener.ACTIVE);
                int en = sp.getSpanEnd(android.text.method.TextKeyListener.ACTIVE);
                if ((i >= st) && (i < en)) {
                    return mSource.charAt(i);
                }
                android.text.method.PasswordTransformationMethod.Visible[] visible = sp.getSpans(0, sp.length(), android.text.method.PasswordTransformationMethod.Visible.class);
                for (int a = 0; a < visible.length; a++) {
                    if (sp.getSpanStart(visible[a].mTransformer) >= 0) {
                        st = sp.getSpanStart(visible[a]);
                        en = sp.getSpanEnd(visible[a]);
                        if ((i >= st) && (i < en)) {
                            return mSource.charAt(i);
                        }
                    }
                }
            }
            return android.text.method.PasswordTransformationMethod.DOT;
        }

        public java.lang.CharSequence subSequence(int start, int end) {
            char[] buf = new char[end - start];
            getChars(start, end, buf, 0);
            return new java.lang.String(buf);
        }

        public java.lang.String toString() {
            return subSequence(0, length()).toString();
        }

        public void getChars(int start, int end, char[] dest, int off) {
            android.text.TextUtils.getChars(mSource, start, end, dest, off);
            int st = -1;
            int en = -1;
            int nvisible = 0;
            int[] starts = null;
            int[] ends = null;
            if (mSource instanceof android.text.Spanned) {
                android.text.Spanned sp = ((android.text.Spanned) (mSource));
                st = sp.getSpanStart(android.text.method.TextKeyListener.ACTIVE);
                en = sp.getSpanEnd(android.text.method.TextKeyListener.ACTIVE);
                android.text.method.PasswordTransformationMethod.Visible[] visible = sp.getSpans(0, sp.length(), android.text.method.PasswordTransformationMethod.Visible.class);
                nvisible = visible.length;
                starts = new int[nvisible];
                ends = new int[nvisible];
                for (int i = 0; i < nvisible; i++) {
                    if (sp.getSpanStart(visible[i].mTransformer) >= 0) {
                        starts[i] = sp.getSpanStart(visible[i]);
                        ends[i] = sp.getSpanEnd(visible[i]);
                    }
                }
            }
            for (int i = start; i < end; i++) {
                if (!((i >= st) && (i < en))) {
                    boolean visible = false;
                    for (int a = 0; a < nvisible; a++) {
                        if ((i >= starts[a]) && (i < ends[a])) {
                            visible = true;
                            break;
                        }
                    }
                    if (!visible) {
                        dest[(i - start) + off] = android.text.method.PasswordTransformationMethod.DOT;
                    }
                }
            }
        }

        private java.lang.CharSequence mSource;
    }

    private static class Visible extends android.os.Handler implements android.text.style.UpdateLayout , java.lang.Runnable {
        public Visible(android.text.Spannable sp, android.text.method.PasswordTransformationMethod ptm) {
            mText = sp;
            mTransformer = ptm;
            postAtTime(this, android.os.SystemClock.uptimeMillis() + 1500);
        }

        public void run() {
            mText.removeSpan(this);
        }

        private android.text.Spannable mText;

        private android.text.method.PasswordTransformationMethod mTransformer;
    }

    /**
     * Used to stash a reference back to the View in the Editable so we
     * can use it to check the settings.
     */
    private static class ViewReference extends java.lang.ref.WeakReference<android.view.View> implements android.text.NoCopySpan {
        public ViewReference(android.view.View v) {
            super(v);
        }
    }

    private static android.text.method.PasswordTransformationMethod sInstance;

    private static char DOT = '\u2022';
}

