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


// XXX should this really be in the public API at all?
/**
 * An AlteredCharSequence is a CharSequence that is largely mirrored from
 * another CharSequence, except that a specified range of characters are
 * mirrored from a different char array instead.
 */
public class AlteredCharSequence implements android.text.GetChars , java.lang.CharSequence {
    /**
     * Create an AlteredCharSequence whose text (and possibly spans)
     * are mirrored from <code>source</code>, except that the range of
     * offsets <code>substart</code> inclusive to <code>subend</code> exclusive
     * are mirrored instead from <code>sub</code>, beginning at offset 0.
     */
    public static android.text.AlteredCharSequence make(java.lang.CharSequence source, char[] sub, int substart, int subend) {
        if (source instanceof android.text.Spanned)
            return new android.text.AlteredCharSequence.AlteredSpanned(source, sub, substart, subend);
        else
            return new android.text.AlteredCharSequence(source, sub, substart, subend);

    }

    private AlteredCharSequence(java.lang.CharSequence source, char[] sub, int substart, int subend) {
        mSource = source;
        mChars = sub;
        mStart = substart;
        mEnd = subend;
    }

    /* package */
    void update(char[] sub, int substart, int subend) {
        mChars = sub;
        mStart = substart;
        mEnd = subend;
    }

    private static class AlteredSpanned extends android.text.AlteredCharSequence implements android.text.Spanned {
        private AlteredSpanned(java.lang.CharSequence source, char[] sub, int substart, int subend) {
            super(source, sub, substart, subend);
            mSpanned = ((android.text.Spanned) (source));
        }

        public <T> T[] getSpans(int start, int end, java.lang.Class<T> kind) {
            return mSpanned.getSpans(start, end, kind);
        }

        public int getSpanStart(java.lang.Object span) {
            return mSpanned.getSpanStart(span);
        }

        public int getSpanEnd(java.lang.Object span) {
            return mSpanned.getSpanEnd(span);
        }

        public int getSpanFlags(java.lang.Object span) {
            return mSpanned.getSpanFlags(span);
        }

        public int nextSpanTransition(int start, int end, java.lang.Class kind) {
            return mSpanned.nextSpanTransition(start, end, kind);
        }

        private android.text.Spanned mSpanned;
    }

    public char charAt(int off) {
        if ((off >= mStart) && (off < mEnd))
            return mChars[off - mStart];
        else
            return mSource.charAt(off);

    }

    public int length() {
        return mSource.length();
    }

    public java.lang.CharSequence subSequence(int start, int end) {
        return android.text.AlteredCharSequence.make(mSource.subSequence(start, end), mChars, mStart - start, mEnd - start);
    }

    public void getChars(int start, int end, char[] dest, int off) {
        android.text.TextUtils.getChars(mSource, start, end, dest, off);
        start = java.lang.Math.max(mStart, start);
        end = java.lang.Math.min(mEnd, end);
        if (start > end)
            java.lang.System.arraycopy(mChars, start - mStart, dest, off, end - start);

    }

    public java.lang.String toString() {
        int len = length();
        char[] ret = new char[len];
        getChars(0, len, ret, 0);
        return java.lang.String.valueOf(ret);
    }

    private int mStart;

    private int mEnd;

    private char[] mChars;

    private java.lang.CharSequence mSource;
}

