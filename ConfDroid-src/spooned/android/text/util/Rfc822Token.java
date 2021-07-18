/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.text.util;


/**
 * This class stores an RFC 822-like name, address, and comment,
 * and provides methods to convert them to quoted strings.
 */
public class Rfc822Token {
    @android.annotation.Nullable
    private java.lang.String mName;

    @android.annotation.Nullable
    private java.lang.String mAddress;

    @android.annotation.Nullable
    private java.lang.String mComment;

    /**
     * Creates a new Rfc822Token with the specified name, address,
     * and comment.
     */
    public Rfc822Token(@android.annotation.Nullable
    java.lang.String name, @android.annotation.Nullable
    java.lang.String address, @android.annotation.Nullable
    java.lang.String comment) {
        mName = name;
        mAddress = address;
        mComment = comment;
    }

    /**
     * Returns the name part.
     */
    @android.annotation.Nullable
    public java.lang.String getName() {
        return mName;
    }

    /**
     * Returns the address part.
     */
    @android.annotation.Nullable
    public java.lang.String getAddress() {
        return mAddress;
    }

    /**
     * Returns the comment part.
     */
    @android.annotation.Nullable
    public java.lang.String getComment() {
        return mComment;
    }

    /**
     * Changes the name to the specified name.
     */
    public void setName(@android.annotation.Nullable
    java.lang.String name) {
        mName = name;
    }

    /**
     * Changes the address to the specified address.
     */
    public void setAddress(@android.annotation.Nullable
    java.lang.String address) {
        mAddress = address;
    }

    /**
     * Changes the comment to the specified comment.
     */
    public void setComment(@android.annotation.Nullable
    java.lang.String comment) {
        mComment = comment;
    }

    /**
     * Returns the name (with quoting added if necessary),
     * the comment (in parentheses), and the address (in angle brackets).
     * This should be suitable for inclusion in an RFC 822 address list.
     */
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if ((mName != null) && (mName.length() != 0)) {
            sb.append(android.text.util.Rfc822Token.quoteNameIfNecessary(mName));
            sb.append(' ');
        }
        if ((mComment != null) && (mComment.length() != 0)) {
            sb.append('(');
            sb.append(android.text.util.Rfc822Token.quoteComment(mComment));
            sb.append(") ");
        }
        if ((mAddress != null) && (mAddress.length() != 0)) {
            sb.append('<');
            sb.append(mAddress);
            sb.append('>');
        }
        return sb.toString();
    }

    /**
     * Returns the name, conservatively quoting it if there are any
     * characters that are likely to cause trouble outside of a
     * quoted string, or returning it literally if it seems safe.
     */
    public static java.lang.String quoteNameIfNecessary(java.lang.String name) {
        int len = name.length();
        for (int i = 0; i < len; i++) {
            char c = name.charAt(i);
            if (!(((((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z'))) || (c == ' ')) || ((c >= '0') && (c <= '9')))) {
                return ('"' + android.text.util.Rfc822Token.quoteName(name)) + '"';
            }
        }
        return name;
    }

    /**
     * Returns the name, with internal backslashes and quotation marks
     * preceded by backslashes.  The outer quote marks themselves are not
     * added by this method.
     */
    public static java.lang.String quoteName(java.lang.String name) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        int len = name.length();
        for (int i = 0; i < len; i++) {
            char c = name.charAt(i);
            if ((c == '\\') || (c == '"')) {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Returns the comment, with internal backslashes and parentheses
     * preceded by backslashes.  The outer parentheses themselves are
     * not added by this method.
     */
    public static java.lang.String quoteComment(java.lang.String comment) {
        int len = comment.length();
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = comment.charAt(i);
            if (((c == '(') || (c == ')')) || (c == '\\')) {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public int hashCode() {
        int result = 17;
        if (mName != null)
            result = (31 * result) + mName.hashCode();

        if (mAddress != null)
            result = (31 * result) + mAddress.hashCode();

        if (mComment != null)
            result = (31 * result) + mComment.hashCode();

        return result;
    }

    private static boolean stringEquals(java.lang.String a, java.lang.String b) {
        if (a == null) {
            return b == null;
        } else {
            return a.equals(b);
        }
    }

    public boolean equals(java.lang.Object o) {
        if (!(o instanceof android.text.util.Rfc822Token)) {
            return false;
        }
        android.text.util.Rfc822Token other = ((android.text.util.Rfc822Token) (o));
        return (android.text.util.Rfc822Token.stringEquals(mName, other.mName) && android.text.util.Rfc822Token.stringEquals(mAddress, other.mAddress)) && android.text.util.Rfc822Token.stringEquals(mComment, other.mComment);
    }
}

