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


/* package */
abstract class SpannableStringInternal {
    /* package */
    SpannableStringInternal(java.lang.CharSequence source, int start, int end) {
        if ((start == 0) && (end == source.length()))
            mText = source.toString();
        else
            mText = source.toString().substring(start, end);

        mSpans = libcore.util.EmptyArray.OBJECT;
        // Invariant: mSpanData.length = mSpans.length * COLUMNS
        mSpanData = libcore.util.EmptyArray.INT;
        if (source instanceof android.text.Spanned) {
            if (source instanceof android.text.SpannableStringInternal) {
                copySpans(((android.text.SpannableStringInternal) (source)), start, end);
            } else {
                copySpans(((android.text.Spanned) (source)), start, end);
            }
        }
    }

    /**
     * Copies another {@link Spanned} object's spans between [start, end] into this object.
     *
     * @param src
     * 		Source object to copy from.
     * @param start
     * 		Start index in the source object.
     * @param end
     * 		End index in the source object.
     */
    private final void copySpans(android.text.Spanned src, int start, int end) {
        java.lang.Object[] spans = src.getSpans(start, end, java.lang.Object.class);
        for (int i = 0; i < spans.length; i++) {
            int st = src.getSpanStart(spans[i]);
            int en = src.getSpanEnd(spans[i]);
            int fl = src.getSpanFlags(spans[i]);
            if (st < start)
                st = start;

            if (en > end)
                en = end;

            setSpan(spans[i], st - start, en - start, fl);
        }
    }

    /**
     * Copies a {@link SpannableStringInternal} object's spans between [start, end] into this
     * object.
     *
     * @param src
     * 		Source object to copy from.
     * @param start
     * 		Start index in the source object.
     * @param end
     * 		End index in the source object.
     */
    private final void copySpans(android.text.SpannableStringInternal src, int start, int end) {
        if ((start == 0) && (end == src.length())) {
            mSpans = com.android.internal.util.ArrayUtils.newUnpaddedObjectArray(src.mSpans.length);
            mSpanData = new int[src.mSpanData.length];
            mSpanCount = src.mSpanCount;
            java.lang.System.arraycopy(src.mSpans, 0, mSpans, 0, src.mSpans.length);
            java.lang.System.arraycopy(src.mSpanData, 0, mSpanData, 0, mSpanData.length);
        } else {
            int count = 0;
            int[] srcData = src.mSpanData;
            int limit = src.mSpanCount;
            for (int i = 0; i < limit; i++) {
                int spanStart = srcData[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.START];
                int spanEnd = srcData[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.END];
                if (isOutOfCopyRange(start, end, spanStart, spanEnd))
                    continue;

                count++;
            }
            if (count == 0)
                return;

            java.lang.Object[] srcSpans = src.mSpans;
            mSpanCount = count;
            mSpans = com.android.internal.util.ArrayUtils.newUnpaddedObjectArray(mSpanCount);
            mSpanData = new int[mSpans.length * android.text.SpannableStringInternal.COLUMNS];
            for (int i = 0, j = 0; i < limit; i++) {
                int spanStart = srcData[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.START];
                int spanEnd = srcData[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.END];
                if (isOutOfCopyRange(start, end, spanStart, spanEnd))
                    continue;

                if (spanStart < start)
                    spanStart = start;

                if (spanEnd > end)
                    spanEnd = end;

                mSpans[j] = srcSpans[i];
                mSpanData[(j * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.START] = spanStart - start;
                mSpanData[(j * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.END] = spanEnd - start;
                mSpanData[(j * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.FLAGS] = srcData[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.FLAGS];
                j++;
            }
        }
    }

    /**
     * Checks if [spanStart, spanEnd] interval is excluded from [start, end].
     *
     * @return True if excluded, false if included.
     */
    private final boolean isOutOfCopyRange(int start, int end, int spanStart, int spanEnd) {
        if ((spanStart > end) || (spanEnd < start))
            return true;

        if ((spanStart != spanEnd) && (start != end)) {
            if ((spanStart == end) || (spanEnd == start))
                return true;

        }
        return false;
    }

    public final int length() {
        return mText.length();
    }

    public final char charAt(int i) {
        return mText.charAt(i);
    }

    public final java.lang.String toString() {
        return mText;
    }

    /* subclasses must do subSequence() to preserve type */
    public final void getChars(int start, int end, char[] dest, int off) {
        mText.getChars(start, end, dest, off);
    }

    /* package */
    void setSpan(java.lang.Object what, int start, int end, int flags) {
        int nstart = start;
        int nend = end;
        checkRange("setSpan", start, end);
        if ((flags & android.text.Spannable.SPAN_PARAGRAPH) == android.text.Spannable.SPAN_PARAGRAPH) {
            if ((start != 0) && (start != length())) {
                char c = charAt(start - 1);
                if (c != '\n')
                    throw new java.lang.RuntimeException((((("PARAGRAPH span must start at paragraph boundary" + " (") + start) + " follows ") + c) + ")");

            }
            if ((end != 0) && (end != length())) {
                char c = charAt(end - 1);
                if (c != '\n')
                    throw new java.lang.RuntimeException((((("PARAGRAPH span must end at paragraph boundary" + " (") + end) + " follows ") + c) + ")");

            }
        }
        int count = mSpanCount;
        java.lang.Object[] spans = mSpans;
        int[] data = mSpanData;
        for (int i = 0; i < count; i++) {
            if (spans[i] == what) {
                int ostart = data[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.START];
                int oend = data[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.END];
                data[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.START] = start;
                data[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.END] = end;
                data[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.FLAGS] = flags;
                sendSpanChanged(what, ostart, oend, nstart, nend);
                return;
            }
        }
        if ((mSpanCount + 1) >= mSpans.length) {
            java.lang.Object[] newtags = com.android.internal.util.ArrayUtils.newUnpaddedObjectArray(com.android.internal.util.GrowingArrayUtils.growSize(mSpanCount));
            int[] newdata = new int[newtags.length * 3];
            java.lang.System.arraycopy(mSpans, 0, newtags, 0, mSpanCount);
            java.lang.System.arraycopy(mSpanData, 0, newdata, 0, mSpanCount * 3);
            mSpans = newtags;
            mSpanData = newdata;
        }
        mSpans[mSpanCount] = what;
        mSpanData[(mSpanCount * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.START] = start;
        mSpanData[(mSpanCount * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.END] = end;
        mSpanData[(mSpanCount * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.FLAGS] = flags;
        mSpanCount++;
        if (this instanceof android.text.Spannable)
            sendSpanAdded(what, nstart, nend);

    }

    /* package */
    void removeSpan(java.lang.Object what) {
        int count = mSpanCount;
        java.lang.Object[] spans = mSpans;
        int[] data = mSpanData;
        for (int i = count - 1; i >= 0; i--) {
            if (spans[i] == what) {
                int ostart = data[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.START];
                int oend = data[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.END];
                int c = count - (i + 1);
                java.lang.System.arraycopy(spans, i + 1, spans, i, c);
                java.lang.System.arraycopy(data, (i + 1) * android.text.SpannableStringInternal.COLUMNS, data, i * android.text.SpannableStringInternal.COLUMNS, c * android.text.SpannableStringInternal.COLUMNS);
                mSpanCount--;
                sendSpanRemoved(what, ostart, oend);
                return;
            }
        }
    }

    public int getSpanStart(java.lang.Object what) {
        int count = mSpanCount;
        java.lang.Object[] spans = mSpans;
        int[] data = mSpanData;
        for (int i = count - 1; i >= 0; i--) {
            if (spans[i] == what) {
                return data[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.START];
            }
        }
        return -1;
    }

    public int getSpanEnd(java.lang.Object what) {
        int count = mSpanCount;
        java.lang.Object[] spans = mSpans;
        int[] data = mSpanData;
        for (int i = count - 1; i >= 0; i--) {
            if (spans[i] == what) {
                return data[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.END];
            }
        }
        return -1;
    }

    public int getSpanFlags(java.lang.Object what) {
        int count = mSpanCount;
        java.lang.Object[] spans = mSpans;
        int[] data = mSpanData;
        for (int i = count - 1; i >= 0; i--) {
            if (spans[i] == what) {
                return data[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.FLAGS];
            }
        }
        return 0;
    }

    public <T> T[] getSpans(int queryStart, int queryEnd, java.lang.Class<T> kind) {
        int count = 0;
        int spanCount = mSpanCount;
        java.lang.Object[] spans = mSpans;
        int[] data = mSpanData;
        java.lang.Object[] ret = null;
        java.lang.Object ret1 = null;
        for (int i = 0; i < spanCount; i++) {
            int spanStart = data[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.START];
            int spanEnd = data[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.END];
            if (spanStart > queryEnd) {
                continue;
            }
            if (spanEnd < queryStart) {
                continue;
            }
            if ((spanStart != spanEnd) && (queryStart != queryEnd)) {
                if (spanStart == queryEnd) {
                    continue;
                }
                if (spanEnd == queryStart) {
                    continue;
                }
            }
            // verify span class as late as possible, since it is expensive
            if (((kind != null) && (kind != java.lang.Object.class)) && (!kind.isInstance(spans[i]))) {
                continue;
            }
            if (count == 0) {
                ret1 = spans[i];
                count++;
            } else {
                if (count == 1) {
                    ret = ((java.lang.Object[]) (java.lang.reflect.Array.newInstance(kind, (spanCount - i) + 1)));
                    ret[0] = ret1;
                }
                int prio = data[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.FLAGS] & android.text.Spanned.SPAN_PRIORITY;
                if (prio != 0) {
                    int j;
                    for (j = 0; j < count; j++) {
                        int p = getSpanFlags(ret[j]) & android.text.Spanned.SPAN_PRIORITY;
                        if (prio > p) {
                            break;
                        }
                    }
                    java.lang.System.arraycopy(ret, j, ret, j + 1, count - j);
                    ret[j] = spans[i];
                    count++;
                } else {
                    ret[count++] = spans[i];
                }
            }
        }
        if (count == 0) {
            return ((T[]) (com.android.internal.util.ArrayUtils.emptyArray(kind)));
        }
        if (count == 1) {
            ret = ((java.lang.Object[]) (java.lang.reflect.Array.newInstance(kind, 1)));
            ret[0] = ret1;
            return ((T[]) (ret));
        }
        if (count == ret.length) {
            return ((T[]) (ret));
        }
        java.lang.Object[] nret = ((java.lang.Object[]) (java.lang.reflect.Array.newInstance(kind, count)));
        java.lang.System.arraycopy(ret, 0, nret, 0, count);
        return ((T[]) (nret));
    }

    public int nextSpanTransition(int start, int limit, java.lang.Class kind) {
        int count = mSpanCount;
        java.lang.Object[] spans = mSpans;
        int[] data = mSpanData;
        if (kind == null) {
            kind = java.lang.Object.class;
        }
        for (int i = 0; i < count; i++) {
            int st = data[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.START];
            int en = data[(i * android.text.SpannableStringInternal.COLUMNS) + android.text.SpannableStringInternal.END];
            if (((st > start) && (st < limit)) && kind.isInstance(spans[i]))
                limit = st;

            if (((en > start) && (en < limit)) && kind.isInstance(spans[i]))
                limit = en;

        }
        return limit;
    }

    private void sendSpanAdded(java.lang.Object what, int start, int end) {
        android.text.SpanWatcher[] recip = getSpans(start, end, android.text.SpanWatcher.class);
        int n = recip.length;
        for (int i = 0; i < n; i++) {
            recip[i].onSpanAdded(((android.text.Spannable) (this)), what, start, end);
        }
    }

    private void sendSpanRemoved(java.lang.Object what, int start, int end) {
        android.text.SpanWatcher[] recip = getSpans(start, end, android.text.SpanWatcher.class);
        int n = recip.length;
        for (int i = 0; i < n; i++) {
            recip[i].onSpanRemoved(((android.text.Spannable) (this)), what, start, end);
        }
    }

    private void sendSpanChanged(java.lang.Object what, int s, int e, int st, int en) {
        android.text.SpanWatcher[] recip = getSpans(java.lang.Math.min(s, st), java.lang.Math.max(e, en), android.text.SpanWatcher.class);
        int n = recip.length;
        for (int i = 0; i < n; i++) {
            recip[i].onSpanChanged(((android.text.Spannable) (this)), what, s, e, st, en);
        }
    }

    private static java.lang.String region(int start, int end) {
        return ((("(" + start) + " ... ") + end) + ")";
    }

    private void checkRange(final java.lang.String operation, int start, int end) {
        if (end < start) {
            throw new java.lang.IndexOutOfBoundsException(((operation + " ") + android.text.SpannableStringInternal.region(start, end)) + " has end before start");
        }
        int len = length();
        if ((start > len) || (end > len)) {
            throw new java.lang.IndexOutOfBoundsException((((operation + " ") + android.text.SpannableStringInternal.region(start, end)) + " ends beyond length ") + len);
        }
        if ((start < 0) || (end < 0)) {
            throw new java.lang.IndexOutOfBoundsException(((operation + " ") + android.text.SpannableStringInternal.region(start, end)) + " starts before 0");
        }
    }

    // Same as SpannableStringBuilder
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

    // Same as SpannableStringBuilder
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

    private java.lang.String mText;

    private java.lang.Object[] mSpans;

    private int[] mSpanData;

    private int mSpanCount;

    /* package */
    static final java.lang.Object[] EMPTY = new java.lang.Object[0];

    private static final int START = 0;

    private static final int END = 1;

    private static final int FLAGS = 2;

    private static final int COLUMNS = 3;
}

