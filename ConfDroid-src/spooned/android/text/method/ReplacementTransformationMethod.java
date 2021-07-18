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
 * This transformation method causes the characters in the {@link #getOriginal}
 * array to be replaced by the corresponding characters in the
 * {@link #getReplacement} array.
 */
public abstract class ReplacementTransformationMethod implements android.text.method.TransformationMethod {
    /**
     * Returns the list of characters that are to be replaced by other
     * characters when displayed.
     */
    protected abstract char[] getOriginal();

    /**
     * Returns a parallel array of replacement characters for the ones
     * that are to be replaced.
     */
    protected abstract char[] getReplacement();

    /**
     * Returns a CharSequence that will mirror the contents of the
     * source CharSequence but with the characters in {@link #getOriginal}
     * replaced by ones from {@link #getReplacement}.
     */
    public java.lang.CharSequence getTransformation(java.lang.CharSequence source, android.view.View v) {
        char[] original = getOriginal();
        char[] replacement = getReplacement();
        /* Short circuit for faster display if the text will never change. */
        if (!(source instanceof android.text.Editable)) {
            /* Check whether the text does not contain any of the
            source characters so can be used unchanged.
             */
            boolean doNothing = true;
            int n = original.length;
            for (int i = 0; i < n; i++) {
                if (android.text.TextUtils.indexOf(source, original[i]) >= 0) {
                    doNothing = false;
                    break;
                }
            }
            if (doNothing) {
                return source;
            }
            if (!(source instanceof android.text.Spannable)) {
                /* The text contains some of the source characters,
                but they can be flattened out now instead of
                at display time.
                 */
                if (source instanceof android.text.Spanned) {
                    return new android.text.SpannedString(new android.text.method.ReplacementTransformationMethod.SpannedReplacementCharSequence(((android.text.Spanned) (source)), original, replacement));
                } else {
                    return new android.text.method.ReplacementTransformationMethod.ReplacementCharSequence(source, original, replacement).toString();
                }
            }
        }
        if (source instanceof android.text.Spanned) {
            return new android.text.method.ReplacementTransformationMethod.SpannedReplacementCharSequence(((android.text.Spanned) (source)), original, replacement);
        } else {
            return new android.text.method.ReplacementTransformationMethod.ReplacementCharSequence(source, original, replacement);
        }
    }

    public void onFocusChanged(android.view.View view, java.lang.CharSequence sourceText, boolean focused, int direction, android.graphics.Rect previouslyFocusedRect) {
        // This callback isn't used.
    }

    private static class ReplacementCharSequence implements android.text.GetChars , java.lang.CharSequence {
        private char[] mOriginal;

        private char[] mReplacement;

        public ReplacementCharSequence(java.lang.CharSequence source, char[] original, char[] replacement) {
            mSource = source;
            mOriginal = original;
            mReplacement = replacement;
        }

        public int length() {
            return mSource.length();
        }

        public char charAt(int i) {
            char c = mSource.charAt(i);
            int n = mOriginal.length;
            for (int j = 0; j < n; j++) {
                if (c == mOriginal[j]) {
                    c = mReplacement[j];
                }
            }
            return c;
        }

        public java.lang.CharSequence subSequence(int start, int end) {
            char[] c = new char[end - start];
            getChars(start, end, c, 0);
            return new java.lang.String(c);
        }

        public java.lang.String toString() {
            char[] c = new char[length()];
            getChars(0, length(), c, 0);
            return new java.lang.String(c);
        }

        public void getChars(int start, int end, char[] dest, int off) {
            android.text.TextUtils.getChars(mSource, start, end, dest, off);
            int offend = (end - start) + off;
            int n = mOriginal.length;
            for (int i = off; i < offend; i++) {
                char c = dest[i];
                for (int j = 0; j < n; j++) {
                    if (c == mOriginal[j]) {
                        dest[i] = mReplacement[j];
                    }
                }
            }
        }

        private java.lang.CharSequence mSource;
    }

    private static class SpannedReplacementCharSequence extends android.text.method.ReplacementTransformationMethod.ReplacementCharSequence implements android.text.Spanned {
        public SpannedReplacementCharSequence(android.text.Spanned source, char[] original, char[] replacement) {
            super(source, original, replacement);
            mSpanned = source;
        }

        public java.lang.CharSequence subSequence(int start, int end) {
            return new android.text.SpannedString(this).subSequence(start, end);
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

        public int nextSpanTransition(int start, int end, java.lang.Class type) {
            return mSpanned.nextSpanTransition(start, end, type);
        }

        private android.text.Spanned mSpanned;
    }
}

