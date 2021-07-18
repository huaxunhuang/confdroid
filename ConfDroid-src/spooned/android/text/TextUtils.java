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


public class TextUtils {
    private static final java.lang.String TAG = "TextUtils";

    /* package */
    static final char[] ELLIPSIS_NORMAL = new char[]{ '\u2026' };// this is "..."


    /**
     * {@hide }
     */
    public static final java.lang.String ELLIPSIS_STRING = new java.lang.String(android.text.TextUtils.ELLIPSIS_NORMAL);

    /* package */
    static final char[] ELLIPSIS_TWO_DOTS = new char[]{ '\u2025' };// this is ".."


    private static final java.lang.String ELLIPSIS_TWO_DOTS_STRING = new java.lang.String(android.text.TextUtils.ELLIPSIS_TWO_DOTS);

    private TextUtils() {
        /* cannot be instantiated */
    }

    public static void getChars(java.lang.CharSequence s, int start, int end, char[] dest, int destoff) {
        java.lang.Class<? extends java.lang.CharSequence> c = s.getClass();
        if (c == java.lang.String.class)
            ((java.lang.String) (s)).getChars(start, end, dest, destoff);
        else
            if (c == java.lang.StringBuffer.class)
                ((java.lang.StringBuffer) (s)).getChars(start, end, dest, destoff);
            else
                if (c == java.lang.StringBuilder.class)
                    ((java.lang.StringBuilder) (s)).getChars(start, end, dest, destoff);
                else
                    if (s instanceof android.text.GetChars)
                        ((android.text.GetChars) (s)).getChars(start, end, dest, destoff);
                    else {
                        for (int i = start; i < end; i++)
                            dest[destoff++] = s.charAt(i);

                    }



    }

    public static int indexOf(java.lang.CharSequence s, char ch) {
        return android.text.TextUtils.indexOf(s, ch, 0);
    }

    public static int indexOf(java.lang.CharSequence s, char ch, int start) {
        java.lang.Class<? extends java.lang.CharSequence> c = s.getClass();
        if (c == java.lang.String.class)
            return ((java.lang.String) (s)).indexOf(ch, start);

        return android.text.TextUtils.indexOf(s, ch, start, s.length());
    }

    public static int indexOf(java.lang.CharSequence s, char ch, int start, int end) {
        java.lang.Class<? extends java.lang.CharSequence> c = s.getClass();
        if ((((s instanceof android.text.GetChars) || (c == java.lang.StringBuffer.class)) || (c == java.lang.StringBuilder.class)) || (c == java.lang.String.class)) {
            final int INDEX_INCREMENT = 500;
            char[] temp = android.text.TextUtils.obtain(INDEX_INCREMENT);
            while (start < end) {
                int segend = start + INDEX_INCREMENT;
                if (segend > end)
                    segend = end;

                android.text.TextUtils.getChars(s, start, segend, temp, 0);
                int count = segend - start;
                for (int i = 0; i < count; i++) {
                    if (temp[i] == ch) {
                        android.text.TextUtils.recycle(temp);
                        return i + start;
                    }
                }
                start = segend;
            } 
            android.text.TextUtils.recycle(temp);
            return -1;
        }
        for (int i = start; i < end; i++)
            if (s.charAt(i) == ch)
                return i;


        return -1;
    }

    public static int lastIndexOf(java.lang.CharSequence s, char ch) {
        return android.text.TextUtils.lastIndexOf(s, ch, s.length() - 1);
    }

    public static int lastIndexOf(java.lang.CharSequence s, char ch, int last) {
        java.lang.Class<? extends java.lang.CharSequence> c = s.getClass();
        if (c == java.lang.String.class)
            return ((java.lang.String) (s)).lastIndexOf(ch, last);

        return android.text.TextUtils.lastIndexOf(s, ch, 0, last);
    }

    public static int lastIndexOf(java.lang.CharSequence s, char ch, int start, int last) {
        if (last < 0)
            return -1;

        if (last >= s.length())
            last = s.length() - 1;

        int end = last + 1;
        java.lang.Class<? extends java.lang.CharSequence> c = s.getClass();
        if ((((s instanceof android.text.GetChars) || (c == java.lang.StringBuffer.class)) || (c == java.lang.StringBuilder.class)) || (c == java.lang.String.class)) {
            final int INDEX_INCREMENT = 500;
            char[] temp = android.text.TextUtils.obtain(INDEX_INCREMENT);
            while (start < end) {
                int segstart = end - INDEX_INCREMENT;
                if (segstart < start)
                    segstart = start;

                android.text.TextUtils.getChars(s, segstart, end, temp, 0);
                int count = end - segstart;
                for (int i = count - 1; i >= 0; i--) {
                    if (temp[i] == ch) {
                        android.text.TextUtils.recycle(temp);
                        return i + segstart;
                    }
                }
                end = segstart;
            } 
            android.text.TextUtils.recycle(temp);
            return -1;
        }
        for (int i = end - 1; i >= start; i--)
            if (s.charAt(i) == ch)
                return i;


        return -1;
    }

    public static int indexOf(java.lang.CharSequence s, java.lang.CharSequence needle) {
        return android.text.TextUtils.indexOf(s, needle, 0, s.length());
    }

    public static int indexOf(java.lang.CharSequence s, java.lang.CharSequence needle, int start) {
        return android.text.TextUtils.indexOf(s, needle, start, s.length());
    }

    public static int indexOf(java.lang.CharSequence s, java.lang.CharSequence needle, int start, int end) {
        int nlen = needle.length();
        if (nlen == 0)
            return start;

        char c = needle.charAt(0);
        for (; ;) {
            start = android.text.TextUtils.indexOf(s, c, start);
            if (start > (end - nlen)) {
                break;
            }
            if (start < 0) {
                return -1;
            }
            if (android.text.TextUtils.regionMatches(s, start, needle, 0, nlen)) {
                return start;
            }
            start++;
        }
        return -1;
    }

    public static boolean regionMatches(java.lang.CharSequence one, int toffset, java.lang.CharSequence two, int ooffset, int len) {
        int tempLen = 2 * len;
        if (tempLen < len) {
            // Integer overflow; len is unreasonably large
            throw new java.lang.IndexOutOfBoundsException();
        }
        char[] temp = android.text.TextUtils.obtain(tempLen);
        android.text.TextUtils.getChars(one, toffset, toffset + len, temp, 0);
        android.text.TextUtils.getChars(two, ooffset, ooffset + len, temp, len);
        boolean match = true;
        for (int i = 0; i < len; i++) {
            if (temp[i] != temp[i + len]) {
                match = false;
                break;
            }
        }
        android.text.TextUtils.recycle(temp);
        return match;
    }

    /**
     * Create a new String object containing the given range of characters
     * from the source string.  This is different than simply calling
     * {@link CharSequence#subSequence(int, int) CharSequence.subSequence}
     * in that it does not preserve any style runs in the source sequence,
     * allowing a more efficient implementation.
     */
    public static java.lang.String substring(java.lang.CharSequence source, int start, int end) {
        if (source instanceof java.lang.String)
            return ((java.lang.String) (source)).substring(start, end);

        if (source instanceof java.lang.StringBuilder)
            return ((java.lang.StringBuilder) (source)).substring(start, end);

        if (source instanceof java.lang.StringBuffer)
            return ((java.lang.StringBuffer) (source)).substring(start, end);

        char[] temp = android.text.TextUtils.obtain(end - start);
        android.text.TextUtils.getChars(source, start, end, temp, 0);
        java.lang.String ret = new java.lang.String(temp, 0, end - start);
        android.text.TextUtils.recycle(temp);
        return ret;
    }

    /**
     * Returns a string containing the tokens joined by delimiters.
     *
     * @param tokens
     * 		an array objects to be joined. Strings will be formed from
     * 		the objects by calling object.toString().
     */
    public static java.lang.String join(java.lang.CharSequence delimiter, java.lang.Object[] tokens) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        boolean firstTime = true;
        for (java.lang.Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    /**
     * Returns a string containing the tokens joined by delimiters.
     *
     * @param tokens
     * 		an array objects to be joined. Strings will be formed from
     * 		the objects by calling object.toString().
     */
    public static java.lang.String join(java.lang.CharSequence delimiter, java.lang.Iterable tokens) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.util.Iterator<?> it = tokens.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(delimiter);
                sb.append(it.next());
            } 
        }
        return sb.toString();
    }

    /**
     * String.split() returns [''] when the string to be split is empty. This returns []. This does
     * not remove any empty strings from the result. For example split("a,", ","  ) returns {"a", ""}.
     *
     * @param text
     * 		the string to split
     * @param expression
     * 		the regular expression to match
     * @return an array of strings. The array will be empty if text is empty
     * @throws NullPointerException
     * 		if expression or text is null
     */
    public static java.lang.String[] split(java.lang.String text, java.lang.String expression) {
        if (text.length() == 0) {
            return android.text.TextUtils.EMPTY_STRING_ARRAY;
        } else {
            return text.split(expression, -1);
        }
    }

    /**
     * Splits a string on a pattern. String.split() returns [''] when the string to be
     * split is empty. This returns []. This does not remove any empty strings from the result.
     *
     * @param text
     * 		the string to split
     * @param pattern
     * 		the regular expression to match
     * @return an array of strings. The array will be empty if text is empty
     * @throws NullPointerException
     * 		if expression or text is null
     */
    public static java.lang.String[] split(java.lang.String text, java.util.regex.Pattern pattern) {
        if (text.length() == 0) {
            return android.text.TextUtils.EMPTY_STRING_ARRAY;
        } else {
            return pattern.split(text, -1);
        }
    }

    /**
     * An interface for splitting strings according to rules that are opaque to the user of this
     * interface. This also has less overhead than split, which uses regular expressions and
     * allocates an array to hold the results.
     *
     * <p>The most efficient way to use this class is:
     *
     * <pre>
     * // Once
     * TextUtils.StringSplitter splitter = new TextUtils.SimpleStringSplitter(delimiter);
     *
     * // Once per string to split
     * splitter.setString(string);
     * for (String s : splitter) {
     *     ...
     * }
     * </pre>
     */
    public interface StringSplitter extends java.lang.Iterable<java.lang.String> {
        public void setString(java.lang.String string);
    }

    /**
     * A simple string splitter.
     *
     * <p>If the final character in the string to split is the delimiter then no empty string will
     * be returned for the empty string after that delimeter. That is, splitting <tt>"a,b,"</tt> on
     * comma will return <tt>"a", "b"</tt>, not <tt>"a", "b", ""</tt>.
     */
    public static class SimpleStringSplitter implements android.text.TextUtils.StringSplitter , java.util.Iterator<java.lang.String> {
        private java.lang.String mString;

        private char mDelimiter;

        private int mPosition;

        private int mLength;

        /**
         * Initializes the splitter. setString may be called later.
         *
         * @param delimiter
         * 		the delimeter on which to split
         */
        public SimpleStringSplitter(char delimiter) {
            mDelimiter = delimiter;
        }

        /**
         * Sets the string to split
         *
         * @param string
         * 		the string to split
         */
        public void setString(java.lang.String string) {
            mString = string;
            mPosition = 0;
            mLength = mString.length();
        }

        public java.util.Iterator<java.lang.String> iterator() {
            return this;
        }

        public boolean hasNext() {
            return mPosition < mLength;
        }

        public java.lang.String next() {
            int end = mString.indexOf(mDelimiter, mPosition);
            if (end == (-1)) {
                end = mLength;
            }
            java.lang.String nextString = mString.substring(mPosition, end);
            mPosition = end + 1;// Skip the delimiter.

            return nextString;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    public static java.lang.CharSequence stringOrSpannedString(java.lang.CharSequence source) {
        if (source == null)
            return null;

        if (source instanceof android.text.SpannedString)
            return source;

        if (source instanceof android.text.Spanned)
            return new android.text.SpannedString(source);

        return source.toString();
    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str
     * 		the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@android.annotation.Nullable
    java.lang.CharSequence str) {
        if ((str == null) || (str.length() == 0))
            return true;
        else
            return false;

    }

    /**
     * {@hide }
     */
    public static java.lang.String nullIfEmpty(@android.annotation.Nullable
    java.lang.String str) {
        return android.text.TextUtils.isEmpty(str) ? null : str;
    }

    /**
     * Returns the length that the specified CharSequence would have if
     * spaces and ASCII control characters were trimmed from the start and end,
     * as by {@link String#trim}.
     */
    public static int getTrimmedLength(java.lang.CharSequence s) {
        int len = s.length();
        int start = 0;
        while ((start < len) && (s.charAt(start) <= ' ')) {
            start++;
        } 
        int end = len;
        while ((end > start) && (s.charAt(end - 1) <= ' ')) {
            end--;
        } 
        return end - start;
    }

    /**
     * Returns true if a and b are equal, including if they are both null.
     * <p><i>Note: In platform versions 1.1 and earlier, this method only worked well if
     * both the arguments were instances of String.</i></p>
     *
     * @param a
     * 		first CharSequence to check
     * @param b
     * 		second CharSequence to check
     * @return true if a and b are equal
     */
    public static boolean equals(java.lang.CharSequence a, java.lang.CharSequence b) {
        if (a == b)
            return true;

        int length;
        if (((a != null) && (b != null)) && ((length = a.length()) == b.length())) {
            if ((a instanceof java.lang.String) && (b instanceof java.lang.String)) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i))
                        return false;

                }
                return true;
            }
        }
        return false;
    }

    /**
     * This function only reverses individual {@code char}s and not their associated
     * spans. It doesn't support surrogate pairs (that correspond to non-BMP code points), combining
     * sequences or conjuncts either.
     *
     * @deprecated Do not use.
     */
    @java.lang.Deprecated
    public static java.lang.CharSequence getReverse(java.lang.CharSequence source, int start, int end) {
        return new android.text.TextUtils.Reverser(source, start, end);
    }

    private static class Reverser implements android.text.GetChars , java.lang.CharSequence {
        public Reverser(java.lang.CharSequence source, int start, int end) {
            mSource = source;
            mStart = start;
            mEnd = end;
        }

        public int length() {
            return mEnd - mStart;
        }

        public java.lang.CharSequence subSequence(int start, int end) {
            char[] buf = new char[end - start];
            getChars(start, end, buf, 0);
            return new java.lang.String(buf);
        }

        @java.lang.Override
        public java.lang.String toString() {
            return subSequence(0, length()).toString();
        }

        public char charAt(int off) {
            return android.text.AndroidCharacter.getMirror(mSource.charAt((mEnd - 1) - off));
        }

        public void getChars(int start, int end, char[] dest, int destoff) {
            android.text.TextUtils.getChars(mSource, start + mStart, end + mStart, dest, destoff);
            android.text.AndroidCharacter.mirror(dest, 0, end - start);
            int len = end - start;
            int n = (end - start) / 2;
            for (int i = 0; i < n; i++) {
                char tmp = dest[destoff + i];
                dest[destoff + i] = dest[((destoff + len) - i) - 1];
                dest[((destoff + len) - i) - 1] = tmp;
            }
        }

        private java.lang.CharSequence mSource;

        private int mStart;

        private int mEnd;
    }

    /**
     *
     *
     * @unknown 
     */
    public static final int ALIGNMENT_SPAN = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int FIRST_SPAN = android.text.TextUtils.ALIGNMENT_SPAN;

    /**
     *
     *
     * @unknown 
     */
    public static final int FOREGROUND_COLOR_SPAN = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int RELATIVE_SIZE_SPAN = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int SCALE_X_SPAN = 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int STRIKETHROUGH_SPAN = 5;

    /**
     *
     *
     * @unknown 
     */
    public static final int UNDERLINE_SPAN = 6;

    /**
     *
     *
     * @unknown 
     */
    public static final int STYLE_SPAN = 7;

    /**
     *
     *
     * @unknown 
     */
    public static final int BULLET_SPAN = 8;

    /**
     *
     *
     * @unknown 
     */
    public static final int QUOTE_SPAN = 9;

    /**
     *
     *
     * @unknown 
     */
    public static final int LEADING_MARGIN_SPAN = 10;

    /**
     *
     *
     * @unknown 
     */
    public static final int URL_SPAN = 11;

    /**
     *
     *
     * @unknown 
     */
    public static final int BACKGROUND_COLOR_SPAN = 12;

    /**
     *
     *
     * @unknown 
     */
    public static final int TYPEFACE_SPAN = 13;

    /**
     *
     *
     * @unknown 
     */
    public static final int SUPERSCRIPT_SPAN = 14;

    /**
     *
     *
     * @unknown 
     */
    public static final int SUBSCRIPT_SPAN = 15;

    /**
     *
     *
     * @unknown 
     */
    public static final int ABSOLUTE_SIZE_SPAN = 16;

    /**
     *
     *
     * @unknown 
     */
    public static final int TEXT_APPEARANCE_SPAN = 17;

    /**
     *
     *
     * @unknown 
     */
    public static final int ANNOTATION = 18;

    /**
     *
     *
     * @unknown 
     */
    public static final int SUGGESTION_SPAN = 19;

    /**
     *
     *
     * @unknown 
     */
    public static final int SPELL_CHECK_SPAN = 20;

    /**
     *
     *
     * @unknown 
     */
    public static final int SUGGESTION_RANGE_SPAN = 21;

    /**
     *
     *
     * @unknown 
     */
    public static final int EASY_EDIT_SPAN = 22;

    /**
     *
     *
     * @unknown 
     */
    public static final int LOCALE_SPAN = 23;

    /**
     *
     *
     * @unknown 
     */
    public static final int TTS_SPAN = 24;

    /**
     *
     *
     * @unknown 
     */
    public static final int LAST_SPAN = android.text.TextUtils.TTS_SPAN;

    /**
     * Flatten a CharSequence and whatever styles can be copied across processes
     * into the parcel.
     */
    public static void writeToParcel(java.lang.CharSequence cs, android.os.Parcel p, int parcelableFlags) {
        if (cs instanceof android.text.Spanned) {
            p.writeInt(0);
            p.writeString(cs.toString());
            android.text.Spanned sp = ((android.text.Spanned) (cs));
            java.lang.Object[] os = sp.getSpans(0, cs.length(), java.lang.Object.class);
            // note to people adding to this: check more specific types
            // before more generic types.  also notice that it uses
            // "if" instead of "else if" where there are interfaces
            // so one object can be several.
            for (int i = 0; i < os.length; i++) {
                java.lang.Object o = os[i];
                java.lang.Object prop = os[i];
                if (prop instanceof android.text.style.CharacterStyle) {
                    prop = ((android.text.style.CharacterStyle) (prop)).getUnderlying();
                }
                if (prop instanceof android.text.ParcelableSpan) {
                    final android.text.ParcelableSpan ps = ((android.text.ParcelableSpan) (prop));
                    final int spanTypeId = ps.getSpanTypeIdInternal();
                    if ((spanTypeId < android.text.TextUtils.FIRST_SPAN) || (spanTypeId > android.text.TextUtils.LAST_SPAN)) {
                        android.util.Log.e(android.text.TextUtils.TAG, (("External class \"" + ps.getClass().getSimpleName()) + "\" is attempting to use the frameworks-only ParcelableSpan") + " interface");
                    } else {
                        p.writeInt(spanTypeId);
                        ps.writeToParcelInternal(p, parcelableFlags);
                        android.text.TextUtils.writeWhere(p, sp, o);
                    }
                }
            }
            p.writeInt(0);
        } else {
            p.writeInt(1);
            if (cs != null) {
                p.writeString(cs.toString());
            } else {
                p.writeString(null);
            }
        }
    }

    private static void writeWhere(android.os.Parcel p, android.text.Spanned sp, java.lang.Object o) {
        p.writeInt(sp.getSpanStart(o));
        p.writeInt(sp.getSpanEnd(o));
        p.writeInt(sp.getSpanFlags(o));
    }

    public static final android.os.Parcelable.Creator<java.lang.CharSequence> CHAR_SEQUENCE_CREATOR = new android.os.Parcelable.Creator<java.lang.CharSequence>() {
        /**
         * Read and return a new CharSequence, possibly with styles,
         * from the parcel.
         */
        public java.lang.CharSequence createFromParcel(android.os.Parcel p) {
            int kind = p.readInt();
            java.lang.String string = p.readString();
            if (string == null) {
                return null;
            }
            if (kind == 1) {
                return string;
            }
            android.text.SpannableString sp = new android.text.SpannableString(string);
            while (true) {
                kind = p.readInt();
                if (kind == 0)
                    break;

                switch (kind) {
                    case android.text.TextUtils.ALIGNMENT_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.AlignmentSpan.Standard(p));
                        break;
                    case android.text.TextUtils.FOREGROUND_COLOR_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.ForegroundColorSpan(p));
                        break;
                    case android.text.TextUtils.RELATIVE_SIZE_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.RelativeSizeSpan(p));
                        break;
                    case android.text.TextUtils.SCALE_X_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.ScaleXSpan(p));
                        break;
                    case android.text.TextUtils.STRIKETHROUGH_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.StrikethroughSpan(p));
                        break;
                    case android.text.TextUtils.UNDERLINE_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.UnderlineSpan(p));
                        break;
                    case android.text.TextUtils.STYLE_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.StyleSpan(p));
                        break;
                    case android.text.TextUtils.BULLET_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.BulletSpan(p));
                        break;
                    case android.text.TextUtils.QUOTE_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.QuoteSpan(p));
                        break;
                    case android.text.TextUtils.LEADING_MARGIN_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.LeadingMarginSpan.Standard(p));
                        break;
                    case android.text.TextUtils.URL_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.URLSpan(p));
                        break;
                    case android.text.TextUtils.BACKGROUND_COLOR_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.BackgroundColorSpan(p));
                        break;
                    case android.text.TextUtils.TYPEFACE_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.TypefaceSpan(p));
                        break;
                    case android.text.TextUtils.SUPERSCRIPT_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.SuperscriptSpan(p));
                        break;
                    case android.text.TextUtils.SUBSCRIPT_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.SubscriptSpan(p));
                        break;
                    case android.text.TextUtils.ABSOLUTE_SIZE_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.AbsoluteSizeSpan(p));
                        break;
                    case android.text.TextUtils.TEXT_APPEARANCE_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.TextAppearanceSpan(p));
                        break;
                    case android.text.TextUtils.ANNOTATION :
                        android.text.TextUtils.readSpan(p, sp, new android.text.Annotation(p));
                        break;
                    case android.text.TextUtils.SUGGESTION_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.SuggestionSpan(p));
                        break;
                    case android.text.TextUtils.SPELL_CHECK_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.SpellCheckSpan(p));
                        break;
                    case android.text.TextUtils.SUGGESTION_RANGE_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.SuggestionRangeSpan(p));
                        break;
                    case android.text.TextUtils.EASY_EDIT_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.EasyEditSpan(p));
                        break;
                    case android.text.TextUtils.LOCALE_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.LocaleSpan(p));
                        break;
                    case android.text.TextUtils.TTS_SPAN :
                        android.text.TextUtils.readSpan(p, sp, new android.text.style.TtsSpan(p));
                        break;
                    default :
                        throw new java.lang.RuntimeException("bogus span encoding " + kind);
                }
            } 
            return sp;
        }

        public java.lang.CharSequence[] newArray(int size) {
            return new java.lang.CharSequence[size];
        }
    };

    /**
     * Debugging tool to print the spans in a CharSequence.  The output will
     * be printed one span per line.  If the CharSequence is not a Spanned,
     * then the entire string will be printed on a single line.
     */
    public static void dumpSpans(java.lang.CharSequence cs, android.util.Printer printer, java.lang.String prefix) {
        if (cs instanceof android.text.Spanned) {
            android.text.Spanned sp = ((android.text.Spanned) (cs));
            java.lang.Object[] os = sp.getSpans(0, cs.length(), java.lang.Object.class);
            for (int i = 0; i < os.length; i++) {
                java.lang.Object o = os[i];
                printer.println(((((((((((prefix + cs.subSequence(sp.getSpanStart(o), sp.getSpanEnd(o))) + ": ") + java.lang.Integer.toHexString(java.lang.System.identityHashCode(o))) + " ") + o.getClass().getCanonicalName()) + " (") + sp.getSpanStart(o)) + "-") + sp.getSpanEnd(o)) + ") fl=#") + sp.getSpanFlags(o));
            }
        } else {
            printer.println((prefix + cs) + ": (no spans)");
        }
    }

    /**
     * Return a new CharSequence in which each of the source strings is
     * replaced by the corresponding element of the destinations.
     */
    public static java.lang.CharSequence replace(java.lang.CharSequence template, java.lang.String[] sources, java.lang.CharSequence[] destinations) {
        android.text.SpannableStringBuilder tb = new android.text.SpannableStringBuilder(template);
        for (int i = 0; i < sources.length; i++) {
            int where = android.text.TextUtils.indexOf(tb, sources[i]);
            if (where >= 0)
                tb.setSpan(sources[i], where, where + sources[i].length(), android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        for (int i = 0; i < sources.length; i++) {
            int start = tb.getSpanStart(sources[i]);
            int end = tb.getSpanEnd(sources[i]);
            if (start >= 0) {
                tb.replace(start, end, destinations[i]);
            }
        }
        return tb;
    }

    /**
     * Replace instances of "^1", "^2", etc. in the
     * <code>template</code> CharSequence with the corresponding
     * <code>values</code>.  "^^" is used to produce a single caret in
     * the output.  Only up to 9 replacement values are supported,
     * "^10" will be produce the first replacement value followed by a
     * '0'.
     *
     * @param template
     * 		the input text containing "^1"-style
     * 		placeholder values.  This object is not modified; a copy is
     * 		returned.
     * @param values
     * 		CharSequences substituted into the template.  The
     * 		first is substituted for "^1", the second for "^2", and so on.
     * @return the new CharSequence produced by doing the replacement
     * @throws IllegalArgumentException
     * 		if the template requests a
     * 		value that was not provided, or if more than 9 values are
     * 		provided.
     */
    public static java.lang.CharSequence expandTemplate(java.lang.CharSequence template, java.lang.CharSequence... values) {
        if (values.length > 9) {
            throw new java.lang.IllegalArgumentException("max of 9 values are supported");
        }
        android.text.SpannableStringBuilder ssb = new android.text.SpannableStringBuilder(template);
        try {
            int i = 0;
            while (i < ssb.length()) {
                if (ssb.charAt(i) == '^') {
                    char next = ssb.charAt(i + 1);
                    if (next == '^') {
                        ssb.delete(i + 1, i + 2);
                        ++i;
                        continue;
                    } else
                        if (java.lang.Character.isDigit(next)) {
                            int which = java.lang.Character.getNumericValue(next) - 1;
                            if (which < 0) {
                                throw new java.lang.IllegalArgumentException("template requests value ^" + (which + 1));
                            }
                            if (which >= values.length) {
                                throw new java.lang.IllegalArgumentException(((("template requests value ^" + (which + 1)) + "; only ") + values.length) + " provided");
                            }
                            ssb.replace(i, i + 2, values[which]);
                            i += values[which].length();
                            continue;
                        }

                }
                ++i;
            } 
        } catch (java.lang.IndexOutOfBoundsException ignore) {
            // happens when ^ is the last character in the string.
        }
        return ssb;
    }

    public static int getOffsetBefore(java.lang.CharSequence text, int offset) {
        if (offset == 0)
            return 0;

        if (offset == 1)
            return 0;

        char c = text.charAt(offset - 1);
        if ((c >= '\udc00') && (c <= '\udfff')) {
            char c1 = text.charAt(offset - 2);
            if ((c1 >= '\ud800') && (c1 <= '\udbff'))
                offset -= 2;
            else
                offset -= 1;

        } else {
            offset -= 1;
        }
        if (text instanceof android.text.Spanned) {
            android.text.style.ReplacementSpan[] spans = ((android.text.Spanned) (text)).getSpans(offset, offset, android.text.style.ReplacementSpan.class);
            for (int i = 0; i < spans.length; i++) {
                int start = ((android.text.Spanned) (text)).getSpanStart(spans[i]);
                int end = ((android.text.Spanned) (text)).getSpanEnd(spans[i]);
                if ((start < offset) && (end > offset))
                    offset = start;

            }
        }
        return offset;
    }

    public static int getOffsetAfter(java.lang.CharSequence text, int offset) {
        int len = text.length();
        if (offset == len)
            return len;

        if (offset == (len - 1))
            return len;

        char c = text.charAt(offset);
        if ((c >= '\ud800') && (c <= '\udbff')) {
            char c1 = text.charAt(offset + 1);
            if ((c1 >= '\udc00') && (c1 <= '\udfff'))
                offset += 2;
            else
                offset += 1;

        } else {
            offset += 1;
        }
        if (text instanceof android.text.Spanned) {
            android.text.style.ReplacementSpan[] spans = ((android.text.Spanned) (text)).getSpans(offset, offset, android.text.style.ReplacementSpan.class);
            for (int i = 0; i < spans.length; i++) {
                int start = ((android.text.Spanned) (text)).getSpanStart(spans[i]);
                int end = ((android.text.Spanned) (text)).getSpanEnd(spans[i]);
                if ((start < offset) && (end > offset))
                    offset = end;

            }
        }
        return offset;
    }

    private static void readSpan(android.os.Parcel p, android.text.Spannable sp, java.lang.Object o) {
        sp.setSpan(o, p.readInt(), p.readInt(), p.readInt());
    }

    /**
     * Copies the spans from the region <code>start...end</code> in
     * <code>source</code> to the region
     * <code>destoff...destoff+end-start</code> in <code>dest</code>.
     * Spans in <code>source</code> that begin before <code>start</code>
     * or end after <code>end</code> but overlap this range are trimmed
     * as if they began at <code>start</code> or ended at <code>end</code>.
     *
     * @throws IndexOutOfBoundsException
     * 		if any of the copied spans
     * 		are out of range in <code>dest</code>.
     */
    public static void copySpansFrom(android.text.Spanned source, int start, int end, java.lang.Class kind, android.text.Spannable dest, int destoff) {
        if (kind == null) {
            kind = java.lang.Object.class;
        }
        java.lang.Object[] spans = source.getSpans(start, end, kind);
        for (int i = 0; i < spans.length; i++) {
            int st = source.getSpanStart(spans[i]);
            int en = source.getSpanEnd(spans[i]);
            int fl = source.getSpanFlags(spans[i]);
            if (st < start)
                st = start;

            if (en > end)
                en = end;

            dest.setSpan(spans[i], (st - start) + destoff, (en - start) + destoff, fl);
        }
    }

    public enum TruncateAt {

        START,
        MIDDLE,
        END,
        MARQUEE,
        /**
         *
         *
         * @unknown 
         */
        END_SMALL;}

    public interface EllipsizeCallback {
        /**
         * This method is called to report that the specified region of
         * text was ellipsized away by a call to {@link #ellipsize}.
         */
        public void ellipsized(int start, int end);
    }

    /**
     * Returns the original text if it fits in the specified width
     * given the properties of the specified Paint,
     * or, if it does not fit, a truncated
     * copy with ellipsis character added at the specified edge or center.
     */
    public static java.lang.CharSequence ellipsize(java.lang.CharSequence text, android.text.TextPaint p, float avail, android.text.TextUtils.TruncateAt where) {
        return android.text.TextUtils.ellipsize(text, p, avail, where, false, null);
    }

    /**
     * Returns the original text if it fits in the specified width
     * given the properties of the specified Paint,
     * or, if it does not fit, a copy with ellipsis character added
     * at the specified edge or center.
     * If <code>preserveLength</code> is specified, the returned copy
     * will be padded with zero-width spaces to preserve the original
     * length and offsets instead of truncating.
     * If <code>callback</code> is non-null, it will be called to
     * report the start and end of the ellipsized range.  TextDirection
     * is determined by the first strong directional character.
     */
    public static java.lang.CharSequence ellipsize(java.lang.CharSequence text, android.text.TextPaint paint, float avail, android.text.TextUtils.TruncateAt where, boolean preserveLength, android.text.TextUtils.EllipsizeCallback callback) {
        return android.text.TextUtils.ellipsize(text, paint, avail, where, preserveLength, callback, android.text.TextDirectionHeuristics.FIRSTSTRONG_LTR, where == android.text.TextUtils.TruncateAt.END_SMALL ? android.text.TextUtils.ELLIPSIS_TWO_DOTS_STRING : android.text.TextUtils.ELLIPSIS_STRING);
    }

    /**
     * Returns the original text if it fits in the specified width
     * given the properties of the specified Paint,
     * or, if it does not fit, a copy with ellipsis character added
     * at the specified edge or center.
     * If <code>preserveLength</code> is specified, the returned copy
     * will be padded with zero-width spaces to preserve the original
     * length and offsets instead of truncating.
     * If <code>callback</code> is non-null, it will be called to
     * report the start and end of the ellipsized range.
     *
     * @unknown 
     */
    public static java.lang.CharSequence ellipsize(java.lang.CharSequence text, android.text.TextPaint paint, float avail, android.text.TextUtils.TruncateAt where, boolean preserveLength, android.text.TextUtils.EllipsizeCallback callback, android.text.TextDirectionHeuristic textDir, java.lang.String ellipsis) {
        int len = text.length();
        android.text.MeasuredText mt = android.text.MeasuredText.obtain();
        try {
            float width = android.text.TextUtils.setPara(mt, paint, text, 0, text.length(), textDir);
            if (width <= avail) {
                if (callback != null) {
                    callback.ellipsized(0, 0);
                }
                return text;
            }
            // XXX assumes ellipsis string does not require shaping and
            // is unaffected by style
            float ellipsiswid = paint.measureText(ellipsis);
            avail -= ellipsiswid;
            int left = 0;
            int right = len;
            if (avail < 0) {
                // it all goes
            } else
                if (where == android.text.TextUtils.TruncateAt.START) {
                    right = len - mt.breakText(len, false, avail);
                } else
                    if ((where == android.text.TextUtils.TruncateAt.END) || (where == android.text.TextUtils.TruncateAt.END_SMALL)) {
                        left = mt.breakText(len, true, avail);
                    } else {
                        right = len - mt.breakText(len, false, avail / 2);
                        avail -= mt.measure(right, len);
                        left = mt.breakText(right, true, avail);
                    }


            if (callback != null) {
                callback.ellipsized(left, right);
            }
            char[] buf = mt.mChars;
            android.text.Spanned sp = (text instanceof android.text.Spanned) ? ((android.text.Spanned) (text)) : null;
            int remaining = len - (right - left);
            if (preserveLength) {
                if (remaining > 0) {
                    // else eliminate the ellipsis too
                    buf[left++] = ellipsis.charAt(0);
                }
                for (int i = left; i < right; i++) {
                    buf[i] = android.text.TextUtils.ZWNBS_CHAR;
                }
                java.lang.String s = new java.lang.String(buf, 0, len);
                if (sp == null) {
                    return s;
                }
                android.text.SpannableString ss = new android.text.SpannableString(s);
                android.text.TextUtils.copySpansFrom(sp, 0, len, java.lang.Object.class, ss, 0);
                return ss;
            }
            if (remaining == 0) {
                return "";
            }
            if (sp == null) {
                java.lang.StringBuilder sb = new java.lang.StringBuilder(remaining + ellipsis.length());
                sb.append(buf, 0, left);
                sb.append(ellipsis);
                sb.append(buf, right, len - right);
                return sb.toString();
            }
            android.text.SpannableStringBuilder ssb = new android.text.SpannableStringBuilder();
            ssb.append(text, 0, left);
            ssb.append(ellipsis);
            ssb.append(text, right, len);
            return ssb;
        } finally {
            android.text.MeasuredText.recycle(mt);
        }
    }

    /**
     * Converts a CharSequence of the comma-separated form "Andy, Bob,
     * Charles, David" that is too wide to fit into the specified width
     * into one like "Andy, Bob, 2 more".
     *
     * @param text
     * 		the text to truncate
     * @param p
     * 		the Paint with which to measure the text
     * @param avail
     * 		the horizontal width available for the text
     * @param oneMore
     * 		the string for "1 more" in the current locale
     * @param more
     * 		the string for "%d more" in the current locale
     */
    public static java.lang.CharSequence commaEllipsize(java.lang.CharSequence text, android.text.TextPaint p, float avail, java.lang.String oneMore, java.lang.String more) {
        return android.text.TextUtils.commaEllipsize(text, p, avail, oneMore, more, android.text.TextDirectionHeuristics.FIRSTSTRONG_LTR);
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.CharSequence commaEllipsize(java.lang.CharSequence text, android.text.TextPaint p, float avail, java.lang.String oneMore, java.lang.String more, android.text.TextDirectionHeuristic textDir) {
        android.text.MeasuredText mt = android.text.MeasuredText.obtain();
        try {
            int len = text.length();
            float width = android.text.TextUtils.setPara(mt, p, text, 0, len, textDir);
            if (width <= avail) {
                return text;
            }
            char[] buf = mt.mChars;
            int commaCount = 0;
            for (int i = 0; i < len; i++) {
                if (buf[i] == ',') {
                    commaCount++;
                }
            }
            int remaining = commaCount + 1;
            int ok = 0;
            java.lang.String okFormat = "";
            int w = 0;
            int count = 0;
            float[] widths = mt.mWidths;
            android.text.MeasuredText tempMt = android.text.MeasuredText.obtain();
            for (int i = 0; i < len; i++) {
                w += widths[i];
                if (buf[i] == ',') {
                    count++;
                    java.lang.String format;
                    // XXX should not insert spaces, should be part of string
                    // XXX should use plural rules and not assume English plurals
                    if ((--remaining) == 1) {
                        format = " " + oneMore;
                    } else {
                        format = " " + java.lang.String.format(more, remaining);
                    }
                    // XXX this is probably ok, but need to look at it more
                    tempMt.setPara(format, 0, format.length(), textDir, null);
                    float moreWid = tempMt.addStyleRun(p, tempMt.mLen, null);
                    if ((w + moreWid) <= avail) {
                        ok = i + 1;
                        okFormat = format;
                    }
                }
            }
            android.text.MeasuredText.recycle(tempMt);
            android.text.SpannableStringBuilder out = new android.text.SpannableStringBuilder(okFormat);
            out.insert(0, text, 0, ok);
            return out;
        } finally {
            android.text.MeasuredText.recycle(mt);
        }
    }

    private static float setPara(android.text.MeasuredText mt, android.text.TextPaint paint, java.lang.CharSequence text, int start, int end, android.text.TextDirectionHeuristic textDir) {
        mt.setPara(text, start, end, textDir, null);
        float width;
        android.text.Spanned sp = (text instanceof android.text.Spanned) ? ((android.text.Spanned) (text)) : null;
        int len = end - start;
        if (sp == null) {
            width = mt.addStyleRun(paint, len, null);
        } else {
            width = 0;
            int spanEnd;
            for (int spanStart = 0; spanStart < len; spanStart = spanEnd) {
                spanEnd = sp.nextSpanTransition(spanStart, len, android.text.style.MetricAffectingSpan.class);
                android.text.style.MetricAffectingSpan[] spans = sp.getSpans(spanStart, spanEnd, android.text.style.MetricAffectingSpan.class);
                spans = android.text.TextUtils.removeEmptySpans(spans, sp, android.text.style.MetricAffectingSpan.class);
                width += mt.addStyleRun(paint, spans, spanEnd - spanStart, null);
            }
        }
        return width;
    }

    private static final char FIRST_RIGHT_TO_LEFT = '\u0590';

    /* package */
    static boolean doesNotNeedBidi(java.lang.CharSequence s, int start, int end) {
        for (int i = start; i < end; i++) {
            if (s.charAt(i) >= android.text.TextUtils.FIRST_RIGHT_TO_LEFT) {
                return false;
            }
        }
        return true;
    }

    /* package */
    static boolean doesNotNeedBidi(char[] text, int start, int len) {
        for (int i = start, e = i + len; i < e; i++) {
            if (text[i] >= android.text.TextUtils.FIRST_RIGHT_TO_LEFT) {
                return false;
            }
        }
        return true;
    }

    /* package */
    static char[] obtain(int len) {
        char[] buf;
        synchronized(android.text.TextUtils.sLock) {
            buf = android.text.TextUtils.sTemp;
            android.text.TextUtils.sTemp = null;
        }
        if ((buf == null) || (buf.length < len))
            buf = com.android.internal.util.ArrayUtils.newUnpaddedCharArray(len);

        return buf;
    }

    /* package */
    static void recycle(char[] temp) {
        if (temp.length > 1000)
            return;

        synchronized(android.text.TextUtils.sLock) {
            android.text.TextUtils.sTemp = temp;
        }
    }

    /**
     * Html-encode the string.
     *
     * @param s
     * 		the string to be encoded
     * @return the encoded string
     */
    public static java.lang.String htmlEncode(java.lang.String s) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        char c;
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            switch (c) {
                case '<' :
                    sb.append("&lt;");// $NON-NLS-1$

                    break;
                case '>' :
                    sb.append("&gt;");// $NON-NLS-1$

                    break;
                case '&' :
                    sb.append("&amp;");// $NON-NLS-1$

                    break;
                case '\'' :
                    // http://www.w3.org/TR/xhtml1
                    // The named character reference &apos; (the apostrophe, U+0027) was introduced in
                    // XML 1.0 but does not appear in HTML. Authors should therefore use &#39; instead
                    // of &apos; to work as expected in HTML 4 user agents.
                    sb.append("&#39;");// $NON-NLS-1$

                    break;
                case '"' :
                    sb.append("&quot;");// $NON-NLS-1$

                    break;
                default :
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Returns a CharSequence concatenating the specified CharSequences,
     * retaining their spans if any.
     */
    public static java.lang.CharSequence concat(java.lang.CharSequence... text) {
        if (text.length == 0) {
            return "";
        }
        if (text.length == 1) {
            return text[0];
        }
        boolean spanned = false;
        for (int i = 0; i < text.length; i++) {
            if (text[i] instanceof android.text.Spanned) {
                spanned = true;
                break;
            }
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < text.length; i++) {
            sb.append(text[i]);
        }
        if (!spanned) {
            return sb.toString();
        }
        android.text.SpannableString ss = new android.text.SpannableString(sb);
        int off = 0;
        for (int i = 0; i < text.length; i++) {
            int len = text[i].length();
            if (text[i] instanceof android.text.Spanned) {
                android.text.TextUtils.copySpansFrom(((android.text.Spanned) (text[i])), 0, len, java.lang.Object.class, ss, off);
            }
            off += len;
        }
        return new android.text.SpannedString(ss);
    }

    /**
     * Returns whether the given CharSequence contains any printable characters.
     */
    public static boolean isGraphic(java.lang.CharSequence str) {
        final int len = str.length();
        for (int cp, i = 0; i < len; i += java.lang.Character.charCount(cp)) {
            cp = java.lang.Character.codePointAt(str, i);
            int gc = java.lang.Character.getType(cp);
            if (((((((gc != java.lang.Character.CONTROL) && (gc != java.lang.Character.FORMAT)) && (gc != java.lang.Character.SURROGATE)) && (gc != java.lang.Character.UNASSIGNED)) && (gc != java.lang.Character.LINE_SEPARATOR)) && (gc != java.lang.Character.PARAGRAPH_SEPARATOR)) && (gc != java.lang.Character.SPACE_SEPARATOR)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether this character is a printable character.
     *
     * This does not support non-BMP characters and should not be used.
     *
     * @deprecated Use {@link #isGraphic(CharSequence)} instead.
     */
    @java.lang.Deprecated
    public static boolean isGraphic(char c) {
        int gc = java.lang.Character.getType(c);
        return ((((((gc != java.lang.Character.CONTROL) && (gc != java.lang.Character.FORMAT)) && (gc != java.lang.Character.SURROGATE)) && (gc != java.lang.Character.UNASSIGNED)) && (gc != java.lang.Character.LINE_SEPARATOR)) && (gc != java.lang.Character.PARAGRAPH_SEPARATOR)) && (gc != java.lang.Character.SPACE_SEPARATOR);
    }

    /**
     * Returns whether the given CharSequence contains only digits.
     */
    public static boolean isDigitsOnly(java.lang.CharSequence str) {
        final int len = str.length();
        for (int cp, i = 0; i < len; i += java.lang.Character.charCount(cp)) {
            cp = java.lang.Character.codePointAt(str, i);
            if (!java.lang.Character.isDigit(cp)) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean isPrintableAscii(final char c) {
        final int asciiFirst = 0x20;
        final int asciiLast = 0x7e;// included

        return (((asciiFirst <= c) && (c <= asciiLast)) || (c == '\r')) || (c == '\n');
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean isPrintableAsciiOnly(final java.lang.CharSequence str) {
        final int len = str.length();
        for (int i = 0; i < len; i++) {
            if (!android.text.TextUtils.isPrintableAscii(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Capitalization mode for {@link #getCapsMode}: capitalize all
     * characters.  This value is explicitly defined to be the same as
     * {@link InputType#TYPE_TEXT_FLAG_CAP_CHARACTERS}.
     */
    public static final int CAP_MODE_CHARACTERS = android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;

    /**
     * Capitalization mode for {@link #getCapsMode}: capitalize the first
     * character of all words.  This value is explicitly defined to be the same as
     * {@link InputType#TYPE_TEXT_FLAG_CAP_WORDS}.
     */
    public static final int CAP_MODE_WORDS = android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS;

    /**
     * Capitalization mode for {@link #getCapsMode}: capitalize the first
     * character of each sentence.  This value is explicitly defined to be the same as
     * {@link InputType#TYPE_TEXT_FLAG_CAP_SENTENCES}.
     */
    public static final int CAP_MODE_SENTENCES = android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;

    /**
     * Determine what caps mode should be in effect at the current offset in
     * the text.  Only the mode bits set in <var>reqModes</var> will be
     * checked.  Note that the caps mode flags here are explicitly defined
     * to match those in {@link InputType}.
     *
     * @param cs
     * 		The text that should be checked for caps modes.
     * @param off
     * 		Location in the text at which to check.
     * @param reqModes
     * 		The modes to be checked: may be any combination of
     * 		{@link #CAP_MODE_CHARACTERS}, {@link #CAP_MODE_WORDS}, and
     * 		{@link #CAP_MODE_SENTENCES}.
     * @return Returns the actual capitalization modes that can be in effect
    at the current position, which is any combination of
    {@link #CAP_MODE_CHARACTERS}, {@link #CAP_MODE_WORDS}, and
    {@link #CAP_MODE_SENTENCES}.
     */
    public static int getCapsMode(java.lang.CharSequence cs, int off, int reqModes) {
        if (off < 0) {
            return 0;
        }
        int i;
        char c;
        int mode = 0;
        if ((reqModes & android.text.TextUtils.CAP_MODE_CHARACTERS) != 0) {
            mode |= android.text.TextUtils.CAP_MODE_CHARACTERS;
        }
        if ((reqModes & (android.text.TextUtils.CAP_MODE_WORDS | android.text.TextUtils.CAP_MODE_SENTENCES)) == 0) {
            return mode;
        }
        // Back over allowed opening punctuation.
        for (i = off; i > 0; i--) {
            c = cs.charAt(i - 1);
            if (((c != '"') && (c != '\'')) && (java.lang.Character.getType(c) != java.lang.Character.START_PUNCTUATION)) {
                break;
            }
        }
        // Start of paragraph, with optional whitespace.
        int j = i;
        while ((j > 0) && (((c = cs.charAt(j - 1)) == ' ') || (c == '\t'))) {
            j--;
        } 
        if ((j == 0) || (cs.charAt(j - 1) == '\n')) {
            return mode | android.text.TextUtils.CAP_MODE_WORDS;
        }
        // Or start of word if we are that style.
        if ((reqModes & android.text.TextUtils.CAP_MODE_SENTENCES) == 0) {
            if (i != j)
                mode |= android.text.TextUtils.CAP_MODE_WORDS;

            return mode;
        }
        // There must be a space if not the start of paragraph.
        if (i == j) {
            return mode;
        }
        // Back over allowed closing punctuation.
        for (; j > 0; j--) {
            c = cs.charAt(j - 1);
            if (((c != '"') && (c != '\'')) && (java.lang.Character.getType(c) != java.lang.Character.END_PUNCTUATION)) {
                break;
            }
        }
        if (j > 0) {
            c = cs.charAt(j - 1);
            if (((c == '.') || (c == '?')) || (c == '!')) {
                // Do not capitalize if the word ends with a period but
                // also contains a period, in which case it is an abbreviation.
                if (c == '.') {
                    for (int k = j - 2; k >= 0; k--) {
                        c = cs.charAt(k);
                        if (c == '.') {
                            return mode;
                        }
                        if (!java.lang.Character.isLetter(c)) {
                            break;
                        }
                    }
                }
                return mode | android.text.TextUtils.CAP_MODE_SENTENCES;
            }
        }
        return mode;
    }

    /**
     * Does a comma-delimited list 'delimitedString' contain a certain item?
     * (without allocating memory)
     *
     * @unknown 
     */
    public static boolean delimitedStringContains(java.lang.String delimitedString, char delimiter, java.lang.String item) {
        if (android.text.TextUtils.isEmpty(delimitedString) || android.text.TextUtils.isEmpty(item)) {
            return false;
        }
        int pos = -1;
        int length = delimitedString.length();
        while ((pos = delimitedString.indexOf(item, pos + 1)) != (-1)) {
            if ((pos > 0) && (delimitedString.charAt(pos - 1) != delimiter)) {
                continue;
            }
            int expectedDelimiterPos = pos + item.length();
            if (expectedDelimiterPos == length) {
                // Match at end of string.
                return true;
            }
            if (delimitedString.charAt(expectedDelimiterPos) == delimiter) {
                return true;
            }
        } 
        return false;
    }

    /**
     * Removes empty spans from the <code>spans</code> array.
     *
     * When parsing a Spanned using {@link Spanned#nextSpanTransition(int, int, Class)}, empty spans
     * will (correctly) create span transitions, and calling getSpans on a slice of text bounded by
     * one of these transitions will (correctly) include the empty overlapping span.
     *
     * However, these empty spans should not be taken into account when layouting or rendering the
     * string and this method provides a way to filter getSpans' results accordingly.
     *
     * @param spans
     * 		A list of spans retrieved using {@link Spanned#getSpans(int, int, Class)} from
     * 		the <code>spanned</code>
     * @param spanned
     * 		The Spanned from which spans were extracted
     * @return A subset of spans where empty spans ({@link Spanned#getSpanStart(Object)}  ==
    {@link Spanned#getSpanEnd(Object)} have been removed. The initial order is preserved
     * @unknown 
     */
    @java.lang.SuppressWarnings("unchecked")
    public static <T> T[] removeEmptySpans(T[] spans, android.text.Spanned spanned, java.lang.Class<T> klass) {
        T[] copy = null;
        int count = 0;
        for (int i = 0; i < spans.length; i++) {
            final T span = spans[i];
            final int start = spanned.getSpanStart(span);
            final int end = spanned.getSpanEnd(span);
            if (start == end) {
                if (copy == null) {
                    copy = ((T[]) (java.lang.reflect.Array.newInstance(klass, spans.length - 1)));
                    java.lang.System.arraycopy(spans, 0, copy, 0, i);
                    count = i;
                }
            } else {
                if (copy != null) {
                    copy[count] = span;
                    count++;
                }
            }
        }
        if (copy != null) {
            T[] result = ((T[]) (java.lang.reflect.Array.newInstance(klass, count)));
            java.lang.System.arraycopy(copy, 0, result, 0, count);
            return result;
        } else {
            return spans;
        }
    }

    /**
     * Pack 2 int values into a long, useful as a return value for a range
     *
     * @see #unpackRangeStartFromLong(long)
     * @see #unpackRangeEndFromLong(long)
     * @unknown 
     */
    public static long packRangeInLong(int start, int end) {
        return (((long) (start)) << 32) | end;
    }

    /**
     * Get the start value from a range packed in a long by {@link #packRangeInLong(int, int)}
     *
     * @see #unpackRangeEndFromLong(long)
     * @see #packRangeInLong(int, int)
     * @unknown 
     */
    public static int unpackRangeStartFromLong(long range) {
        return ((int) (range >>> 32));
    }

    /**
     * Get the end value from a range packed in a long by {@link #packRangeInLong(int, int)}
     *
     * @see #unpackRangeStartFromLong(long)
     * @see #packRangeInLong(int, int)
     * @unknown 
     */
    public static int unpackRangeEndFromLong(long range) {
        return ((int) (range & 0xffffffffL));
    }

    /**
     * Return the layout direction for a given Locale
     *
     * @param locale
     * 		the Locale for which we want the layout direction. Can be null.
     * @return the layout direction. This may be one of:
    {@link android.view.View#LAYOUT_DIRECTION_LTR} or
    {@link android.view.View#LAYOUT_DIRECTION_RTL}.

    Be careful: this code will need to be updated when vertical scripts will be supported
     */
    public static int getLayoutDirectionFromLocale(java.util.Locale locale) {
        return (((locale != null) && (!locale.equals(java.util.Locale.ROOT))) && android.icu.util.ULocale.forLocale(locale).isRightToLeft()) || // If forcing into RTL layout mode, return RTL as default
        android.os.SystemProperties.getBoolean(android.provider.Settings.Global.DEVELOPMENT_FORCE_RTL, false) ? android.view.View.LAYOUT_DIRECTION_RTL : android.view.View.LAYOUT_DIRECTION_LTR;
    }

    /**
     * Return localized string representing the given number of selected items.
     *
     * @unknown 
     */
    public static java.lang.CharSequence formatSelectedCount(int count) {
        return android.content.res.Resources.getSystem().getQuantityString(R.plurals.selected_count, count, count);
    }

    private static java.lang.Object sLock = new java.lang.Object();

    private static char[] sTemp = null;

    private static java.lang.String[] EMPTY_STRING_ARRAY = new java.lang.String[]{  };

    private static final char ZWNBS_CHAR = '\ufeff';
}

