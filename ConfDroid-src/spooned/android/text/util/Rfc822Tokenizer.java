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
 * This class works as a Tokenizer for MultiAutoCompleteTextView for
 * address list fields, and also provides a method for converting
 * a string of addresses (such as might be typed into such a field)
 * into a series of Rfc822Tokens.
 */
public class Rfc822Tokenizer implements android.widget.MultiAutoCompleteTextView.Tokenizer {
    /**
     * This constructor will try to take a string like
     * "Foo Bar (something) &lt;foo\@google.com&gt;,
     * blah\@google.com (something)"
     * and convert it into one or more Rfc822Tokens, output into the supplied
     * collection.
     *
     * It does *not* decode MIME encoded-words; charset conversion
     * must already have taken place if necessary.
     * It will try to be tolerant of broken syntax instead of
     * returning an error.
     */
    public static void tokenize(java.lang.CharSequence text, java.util.Collection<android.text.util.Rfc822Token> out) {
        java.lang.StringBuilder name = new java.lang.StringBuilder();
        java.lang.StringBuilder address = new java.lang.StringBuilder();
        java.lang.StringBuilder comment = new java.lang.StringBuilder();
        int i = 0;
        int cursor = text.length();
        while (i < cursor) {
            char c = text.charAt(i);
            if ((c == ',') || (c == ';')) {
                i++;
                while ((i < cursor) && (text.charAt(i) == ' ')) {
                    i++;
                } 
                android.text.util.Rfc822Tokenizer.crunch(name);
                if (address.length() > 0) {
                    out.add(new android.text.util.Rfc822Token(name.toString(), address.toString(), comment.toString()));
                } else
                    if (name.length() > 0) {
                        out.add(new android.text.util.Rfc822Token(null, name.toString(), comment.toString()));
                    }

                name.setLength(0);
                address.setLength(0);
                comment.setLength(0);
            } else
                if (c == '"') {
                    i++;
                    while (i < cursor) {
                        c = text.charAt(i);
                        if (c == '"') {
                            i++;
                            break;
                        } else
                            if (c == '\\') {
                                if ((i + 1) < cursor) {
                                    name.append(text.charAt(i + 1));
                                }
                                i += 2;
                            } else {
                                name.append(c);
                                i++;
                            }

                    } 
                } else
                    if (c == '(') {
                        int level = 1;
                        i++;
                        while ((i < cursor) && (level > 0)) {
                            c = text.charAt(i);
                            if (c == ')') {
                                if (level > 1) {
                                    comment.append(c);
                                }
                                level--;
                                i++;
                            } else
                                if (c == '(') {
                                    comment.append(c);
                                    level++;
                                    i++;
                                } else
                                    if (c == '\\') {
                                        if ((i + 1) < cursor) {
                                            comment.append(text.charAt(i + 1));
                                        }
                                        i += 2;
                                    } else {
                                        comment.append(c);
                                        i++;
                                    }


                        } 
                    } else
                        if (c == '<') {
                            i++;
                            while (i < cursor) {
                                c = text.charAt(i);
                                if (c == '>') {
                                    i++;
                                    break;
                                } else {
                                    address.append(c);
                                    i++;
                                }
                            } 
                        } else
                            if (c == ' ') {
                                name.append('\u0000');
                                i++;
                            } else {
                                name.append(c);
                                i++;
                            }




        } 
        android.text.util.Rfc822Tokenizer.crunch(name);
        if (address.length() > 0) {
            out.add(new android.text.util.Rfc822Token(name.toString(), address.toString(), comment.toString()));
        } else
            if (name.length() > 0) {
                out.add(new android.text.util.Rfc822Token(null, name.toString(), comment.toString()));
            }

    }

    /**
     * This method will try to take a string like
     * "Foo Bar (something) &lt;foo\@google.com&gt;,
     * blah\@google.com (something)"
     * and convert it into one or more Rfc822Tokens.
     * It does *not* decode MIME encoded-words; charset conversion
     * must already have taken place if necessary.
     * It will try to be tolerant of broken syntax instead of
     * returning an error.
     */
    public static android.text.util.Rfc822Token[] tokenize(java.lang.CharSequence text) {
        java.util.ArrayList<android.text.util.Rfc822Token> out = new java.util.ArrayList<android.text.util.Rfc822Token>();
        android.text.util.Rfc822Tokenizer.tokenize(text, out);
        return out.toArray(new android.text.util.Rfc822Token[out.size()]);
    }

    private static void crunch(java.lang.StringBuilder sb) {
        int i = 0;
        int len = sb.length();
        while (i < len) {
            char c = sb.charAt(i);
            if (c == '\u0000') {
                if ((((((i == 0) || (i == (len - 1))) || (sb.charAt(i - 1) == ' ')) || (sb.charAt(i - 1) == '\u0000')) || (sb.charAt(i + 1) == ' ')) || (sb.charAt(i + 1) == '\u0000')) {
                    sb.deleteCharAt(i);
                    len--;
                } else {
                    i++;
                }
            } else {
                i++;
            }
        } 
        for (i = 0; i < len; i++) {
            if (sb.charAt(i) == '\u0000') {
                sb.setCharAt(i, ' ');
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    public int findTokenStart(java.lang.CharSequence text, int cursor) {
        /* It's hard to search backward, so search forward until
        we reach the cursor.
         */
        int best = 0;
        int i = 0;
        while (i < cursor) {
            i = findTokenEnd(text, i);
            if (i < cursor) {
                i++;// Skip terminating punctuation

                while ((i < cursor) && (text.charAt(i) == ' ')) {
                    i++;
                } 
                if (i < cursor) {
                    best = i;
                }
            }
        } 
        return best;
    }

    /**
     * {@inheritDoc }
     */
    public int findTokenEnd(java.lang.CharSequence text, int cursor) {
        int len = text.length();
        int i = cursor;
        while (i < len) {
            char c = text.charAt(i);
            if ((c == ',') || (c == ';')) {
                return i;
            } else
                if (c == '"') {
                    i++;
                    while (i < len) {
                        c = text.charAt(i);
                        if (c == '"') {
                            i++;
                            break;
                        } else
                            if ((c == '\\') && ((i + 1) < len)) {
                                i += 2;
                            } else {
                                i++;
                            }

                    } 
                } else
                    if (c == '(') {
                        int level = 1;
                        i++;
                        while ((i < len) && (level > 0)) {
                            c = text.charAt(i);
                            if (c == ')') {
                                level--;
                                i++;
                            } else
                                if (c == '(') {
                                    level++;
                                    i++;
                                } else
                                    if ((c == '\\') && ((i + 1) < len)) {
                                        i += 2;
                                    } else {
                                        i++;
                                    }


                        } 
                    } else
                        if (c == '<') {
                            i++;
                            while (i < len) {
                                c = text.charAt(i);
                                if (c == '>') {
                                    i++;
                                    break;
                                } else {
                                    i++;
                                }
                            } 
                        } else {
                            i++;
                        }



        } 
        return i;
    }

    /**
     * Terminates the specified address with a comma and space.
     * This assumes that the specified text already has valid syntax.
     * The Adapter subclass's convertToString() method must make that
     * guarantee.
     */
    public java.lang.CharSequence terminateToken(java.lang.CharSequence text) {
        return text + ", ";
    }
}

