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
package android.os;


/**
 * A simple pattern matcher, which is safe to use on untrusted data: it does
 * not provide full reg-exp support, only simple globbing that can not be
 * used maliciously.
 */
public class PatternMatcher implements android.os.Parcelable {
    /**
     * Pattern type: the given pattern must exactly match the string it is
     * tested against.
     */
    public static final int PATTERN_LITERAL = 0;

    /**
     * Pattern type: the given pattern must match the
     * beginning of the string it is tested against.
     */
    public static final int PATTERN_PREFIX = 1;

    /**
     * Pattern type: the given pattern is interpreted with a
     * simple glob syntax for matching against the string it is tested against.
     * In this syntax, you can use the '*' character to match against zero or
     * more occurrences of the character immediately before.  If the
     * character before it is '.' it will match any character.  The character
     * '\' can be used as an escape.  This essentially provides only the '*'
     * wildcard part of a normal regexp.
     */
    public static final int PATTERN_SIMPLE_GLOB = 2;

    /**
     * Pattern type: the given pattern is interpreted with a regular
     * expression-like syntax for matching against the string it is tested
     * against. Supported tokens include dot ({@code .}) and sets ({@code [...]})
     * with full support for character ranges and the not ({@code ^}) modifier.
     * Supported modifiers include star ({@code *}) for zero-or-more, plus ({@code +})
     * for one-or-more and full range ({@code {...}}) support. This is a simple
     * evaulation implementation in which matching is done against the pattern in
     * realtime with no backtracking support.
     *
     * {@hide } Pending approval for public API
     */
    public static final int PATTERN_ADVANCED_GLOB = 3;

    // token types for advanced matching
    private static final int TOKEN_TYPE_LITERAL = 0;

    private static final int TOKEN_TYPE_ANY = 1;

    private static final int TOKEN_TYPE_SET = 2;

    private static final int TOKEN_TYPE_INVERSE_SET = 3;

    // Return for no match
    private static final int NO_MATCH = -1;

    private static final java.lang.String TAG = "PatternMatcher";

    // Parsed placeholders for advanced patterns
    private static final int PARSED_TOKEN_CHAR_SET_START = -1;

    private static final int PARSED_TOKEN_CHAR_SET_INVERSE_START = -2;

    private static final int PARSED_TOKEN_CHAR_SET_STOP = -3;

    private static final int PARSED_TOKEN_CHAR_ANY = -4;

    private static final int PARSED_MODIFIER_RANGE_START = -5;

    private static final int PARSED_MODIFIER_RANGE_STOP = -6;

    private static final int PARSED_MODIFIER_ZERO_OR_MORE = -7;

    private static final int PARSED_MODIFIER_ONE_OR_MORE = -8;

    private final java.lang.String mPattern;

    private final int mType;

    private final int[] mParsedPattern;

    private static final int MAX_PATTERN_STORAGE = 2048;

    // workspace to use for building a parsed advanced pattern;
    private static final int[] sParsedPatternScratch = new int[android.os.PatternMatcher.MAX_PATTERN_STORAGE];

    public PatternMatcher(java.lang.String pattern, int type) {
        mPattern = pattern;
        mType = type;
        if (mType == android.os.PatternMatcher.PATTERN_ADVANCED_GLOB) {
            mParsedPattern = android.os.PatternMatcher.parseAndVerifyAdvancedPattern(pattern);
        } else {
            mParsedPattern = null;
        }
    }

    public final java.lang.String getPath() {
        return mPattern;
    }

    public final int getType() {
        return mType;
    }

    public boolean match(java.lang.String str) {
        return android.os.PatternMatcher.matchPattern(str, mPattern, mParsedPattern, mType);
    }

    public java.lang.String toString() {
        java.lang.String type = "? ";
        switch (mType) {
            case android.os.PatternMatcher.PATTERN_LITERAL :
                type = "LITERAL: ";
                break;
            case android.os.PatternMatcher.PATTERN_PREFIX :
                type = "PREFIX: ";
                break;
            case android.os.PatternMatcher.PATTERN_SIMPLE_GLOB :
                type = "GLOB: ";
                break;
            case android.os.PatternMatcher.PATTERN_ADVANCED_GLOB :
                type = "ADVANCED: ";
                break;
        }
        return (("PatternMatcher{" + type) + mPattern) + "}";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(mPattern);
        dest.writeInt(mType);
        dest.writeIntArray(mParsedPattern);
    }

    public PatternMatcher(android.os.Parcel src) {
        mPattern = src.readString();
        mType = src.readInt();
        mParsedPattern = src.createIntArray();
    }

    public static final android.os.Parcelable.Creator<android.os.PatternMatcher> CREATOR = new android.os.Parcelable.Creator<android.os.PatternMatcher>() {
        public android.os.PatternMatcher createFromParcel(android.os.Parcel source) {
            return new android.os.PatternMatcher(source);
        }

        public android.os.PatternMatcher[] newArray(int size) {
            return new android.os.PatternMatcher[size];
        }
    };

    static boolean matchPattern(java.lang.String match, java.lang.String pattern, int[] parsedPattern, int type) {
        if (match == null)
            return false;

        if (type == android.os.PatternMatcher.PATTERN_LITERAL) {
            return pattern.equals(match);
        }
        if (type == android.os.PatternMatcher.PATTERN_PREFIX) {
            return match.startsWith(pattern);
        } else
            if (type == android.os.PatternMatcher.PATTERN_SIMPLE_GLOB) {
                return android.os.PatternMatcher.matchGlobPattern(pattern, match);
            } else
                if (type == android.os.PatternMatcher.PATTERN_ADVANCED_GLOB) {
                    return android.os.PatternMatcher.matchAdvancedPattern(parsedPattern, match);
                }


        return false;
    }

    static boolean matchGlobPattern(java.lang.String pattern, java.lang.String match) {
        final int NP = pattern.length();
        if (NP <= 0) {
            return match.length() <= 0;
        }
        final int NM = match.length();
        int ip = 0;
        int im = 0;
        char nextChar = pattern.charAt(0);
        while ((ip < NP) && (im < NM)) {
            char c = nextChar;
            ip++;
            nextChar = (ip < NP) ? pattern.charAt(ip) : 0;
            final boolean escaped = c == '\\';
            if (escaped) {
                c = nextChar;
                ip++;
                nextChar = (ip < NP) ? pattern.charAt(ip) : 0;
            }
            if (nextChar == '*') {
                if ((!escaped) && (c == '.')) {
                    if (ip >= (NP - 1)) {
                        // at the end with a pattern match, so
                        // all is good without checking!
                        return true;
                    }
                    ip++;
                    nextChar = pattern.charAt(ip);
                    // Consume everything until the next character in the
                    // pattern is found.
                    if (nextChar == '\\') {
                        ip++;
                        nextChar = (ip < NP) ? pattern.charAt(ip) : 0;
                    }
                    do {
                        if (match.charAt(im) == nextChar) {
                            break;
                        }
                        im++;
                    } while (im < NM );
                    if (im == NM) {
                        // Whoops, the next character in the pattern didn't
                        // exist in the match.
                        return false;
                    }
                    ip++;
                    nextChar = (ip < NP) ? pattern.charAt(ip) : 0;
                    im++;
                } else {
                    // Consume only characters matching the one before '*'.
                    do {
                        if (match.charAt(im) != c) {
                            break;
                        }
                        im++;
                    } while (im < NM );
                    ip++;
                    nextChar = (ip < NP) ? pattern.charAt(ip) : 0;
                }
            } else {
                if ((c != '.') && (match.charAt(im) != c))
                    return false;

                im++;
            }
        } 
        if ((ip >= NP) && (im >= NM)) {
            // Reached the end of both strings, all is good!
            return true;
        }
        // One last check: we may have finished the match string, but still
        // have a '.*' at the end of the pattern, which should still count
        // as a match.
        if (((ip == (NP - 2)) && (pattern.charAt(ip) == '.')) && (pattern.charAt(ip + 1) == '*')) {
            return true;
        }
        return false;
    }

    /**
     * Parses the advanced pattern and returns an integer array representation of it. The integer
     * array treats each field as a character if positive and a unique token placeholder if
     * negative. This method will throw on any pattern structure violations.
     */
    static synchronized int[] parseAndVerifyAdvancedPattern(java.lang.String pattern) {
        int ip = 0;
        final int LP = pattern.length();
        int it = 0;
        boolean inSet = false;
        boolean inRange = false;
        boolean inCharClass = false;
        boolean addToParsedPattern;
        while (ip < LP) {
            if (it > (android.os.PatternMatcher.MAX_PATTERN_STORAGE - 3)) {
                throw new java.lang.IllegalArgumentException("Pattern is too large!");
            }
            char c = pattern.charAt(ip);
            addToParsedPattern = false;
            switch (c) {
                case '[' :
                    if (inSet) {
                        addToParsedPattern = true;// treat as literal or char class in set

                    } else {
                        if (pattern.charAt(ip + 1) == '^') {
                            android.os.PatternMatcher.sParsedPatternScratch[it++] = android.os.PatternMatcher.PARSED_TOKEN_CHAR_SET_INVERSE_START;
                            ip++;// skip over the '^'

                        } else {
                            android.os.PatternMatcher.sParsedPatternScratch[it++] = android.os.PatternMatcher.PARSED_TOKEN_CHAR_SET_START;
                        }
                        ip++;// move to the next pattern char

                        inSet = true;
                        continue;
                    }
                    break;
                case ']' :
                    if (!inSet) {
                        addToParsedPattern = true;// treat as literal outside of set

                    } else {
                        int parsedToken = android.os.PatternMatcher.sParsedPatternScratch[it - 1];
                        if ((parsedToken == android.os.PatternMatcher.PARSED_TOKEN_CHAR_SET_START) || (parsedToken == android.os.PatternMatcher.PARSED_TOKEN_CHAR_SET_INVERSE_START)) {
                            throw new java.lang.IllegalArgumentException("You must define characters in a set.");
                        }
                        android.os.PatternMatcher.sParsedPatternScratch[it++] = android.os.PatternMatcher.PARSED_TOKEN_CHAR_SET_STOP;
                        inSet = false;
                        inCharClass = false;
                    }
                    break;
                case '{' :
                    if (!inSet) {
                        if ((it == 0) || android.os.PatternMatcher.isParsedModifier(android.os.PatternMatcher.sParsedPatternScratch[it - 1])) {
                            throw new java.lang.IllegalArgumentException("Modifier must follow a token.");
                        }
                        android.os.PatternMatcher.sParsedPatternScratch[it++] = android.os.PatternMatcher.PARSED_MODIFIER_RANGE_START;
                        ip++;
                        inRange = true;
                    }
                    break;
                case '}' :
                    if (inRange) {
                        // only terminate the range if we're currently in one
                        android.os.PatternMatcher.sParsedPatternScratch[it++] = android.os.PatternMatcher.PARSED_MODIFIER_RANGE_STOP;
                        inRange = false;
                    }
                    break;
                case '*' :
                    if (!inSet) {
                        if ((it == 0) || android.os.PatternMatcher.isParsedModifier(android.os.PatternMatcher.sParsedPatternScratch[it - 1])) {
                            throw new java.lang.IllegalArgumentException("Modifier must follow a token.");
                        }
                        android.os.PatternMatcher.sParsedPatternScratch[it++] = android.os.PatternMatcher.PARSED_MODIFIER_ZERO_OR_MORE;
                    }
                    break;
                case '+' :
                    if (!inSet) {
                        if ((it == 0) || android.os.PatternMatcher.isParsedModifier(android.os.PatternMatcher.sParsedPatternScratch[it - 1])) {
                            throw new java.lang.IllegalArgumentException("Modifier must follow a token.");
                        }
                        android.os.PatternMatcher.sParsedPatternScratch[it++] = android.os.PatternMatcher.PARSED_MODIFIER_ONE_OR_MORE;
                    }
                    break;
                case '.' :
                    if (!inSet) {
                        android.os.PatternMatcher.sParsedPatternScratch[it++] = android.os.PatternMatcher.PARSED_TOKEN_CHAR_ANY;
                    }
                    break;
                case '\\' :
                    // escape
                    if ((ip + 1) >= LP) {
                        throw new java.lang.IllegalArgumentException("Escape found at end of pattern!");
                    }
                    c = pattern.charAt(++ip);
                    addToParsedPattern = true;
                    break;
                default :
                    addToParsedPattern = true;
                    break;
            }
            if (inSet) {
                if (inCharClass) {
                    android.os.PatternMatcher.sParsedPatternScratch[it++] = c;
                    inCharClass = false;
                } else {
                    // look forward for character class
                    if ((((ip + 2) < LP) && (pattern.charAt(ip + 1) == '-')) && (pattern.charAt(ip + 2) != ']')) {
                        inCharClass = true;
                        android.os.PatternMatcher.sParsedPatternScratch[it++] = c;// set first token as lower end of range

                        ip++;// advance past dash

                    } else {
                        // literal
                        android.os.PatternMatcher.sParsedPatternScratch[it++] = c;// set first token as literal

                        android.os.PatternMatcher.sParsedPatternScratch[it++] = c;// set second set as literal

                    }
                }
            } else
                if (inRange) {
                    int endOfSet = pattern.indexOf('}', ip);
                    if (endOfSet < 0) {
                        throw new java.lang.IllegalArgumentException("Range not ended with '}'");
                    }
                    java.lang.String rangeString = pattern.substring(ip, endOfSet);
                    int commaIndex = rangeString.indexOf(',');
                    try {
                        final int rangeMin;
                        final int rangeMax;
                        if (commaIndex < 0) {
                            int parsedRange = java.lang.Integer.parseInt(rangeString);
                            rangeMin = rangeMax = parsedRange;
                        } else {
                            rangeMin = java.lang.Integer.parseInt(rangeString.substring(0, commaIndex));
                            if (commaIndex == (rangeString.length() - 1)) {
                                // e.g. {n,} (n or more)
                                rangeMax = java.lang.Integer.MAX_VALUE;
                            } else {
                                rangeMax = java.lang.Integer.parseInt(rangeString.substring(commaIndex + 1));
                            }
                        }
                        if (rangeMin > rangeMax) {
                            throw new java.lang.IllegalArgumentException("Range quantifier minimum is greater than maximum");
                        }
                        android.os.PatternMatcher.sParsedPatternScratch[it++] = rangeMin;
                        android.os.PatternMatcher.sParsedPatternScratch[it++] = rangeMax;
                    } catch (java.lang.NumberFormatException e) {
                        throw new java.lang.IllegalArgumentException("Range number format incorrect", e);
                    }
                    ip = endOfSet;
                    continue;// don't increment ip

                } else
                    if (addToParsedPattern) {
                        android.os.PatternMatcher.sParsedPatternScratch[it++] = c;
                    }


            ip++;
        } 
        if (inSet) {
            throw new java.lang.IllegalArgumentException("Set was not terminated!");
        }
        return java.util.Arrays.copyOf(android.os.PatternMatcher.sParsedPatternScratch, it);
    }

    private static boolean isParsedModifier(int parsedChar) {
        return (((parsedChar == android.os.PatternMatcher.PARSED_MODIFIER_ONE_OR_MORE) || (parsedChar == android.os.PatternMatcher.PARSED_MODIFIER_ZERO_OR_MORE)) || (parsedChar == android.os.PatternMatcher.PARSED_MODIFIER_RANGE_STOP)) || (parsedChar == android.os.PatternMatcher.PARSED_MODIFIER_RANGE_START);
    }

    static boolean matchAdvancedPattern(int[] parsedPattern, java.lang.String match) {
        // create indexes
        int ip = 0;
        int im = 0;
        final int LP = parsedPattern.length;
        // one-time length check
        final int LM = match.length();
        // The current character being analyzed in the pattern
        int patternChar;
        int tokenType;
        int charSetStart = 0;
        int charSetEnd = 0;
        while (ip < LP) {
            // we still have content in the pattern
            patternChar = parsedPattern[ip];
            // get the match type of the next verb
            switch (patternChar) {
                case android.os.PatternMatcher.PARSED_TOKEN_CHAR_ANY :
                    tokenType = android.os.PatternMatcher.TOKEN_TYPE_ANY;
                    ip++;
                    break;
                case android.os.PatternMatcher.PARSED_TOKEN_CHAR_SET_START :
                case android.os.PatternMatcher.PARSED_TOKEN_CHAR_SET_INVERSE_START :
                    tokenType = (patternChar == android.os.PatternMatcher.PARSED_TOKEN_CHAR_SET_START) ? android.os.PatternMatcher.TOKEN_TYPE_SET : android.os.PatternMatcher.TOKEN_TYPE_INVERSE_SET;
                    charSetStart = ip + 1;// start from the char after the set start

                    while (((++ip) < LP) && (parsedPattern[ip] != android.os.PatternMatcher.PARSED_TOKEN_CHAR_SET_STOP));
                    charSetEnd = ip - 1;// we're on the set stop, end is the previous

                    ip++;// move the pointer to the next pattern entry

                    break;
                default :
                    charSetStart = ip;
                    tokenType = android.os.PatternMatcher.TOKEN_TYPE_LITERAL;
                    ip++;
                    break;
            }
            final int minRepetition;
            final int maxRepetition;
            // look for a match length modifier
            if (ip >= LP) {
                minRepetition = maxRepetition = 1;
            } else {
                patternChar = parsedPattern[ip];
                switch (patternChar) {
                    case android.os.PatternMatcher.PARSED_MODIFIER_ZERO_OR_MORE :
                        minRepetition = 0;
                        maxRepetition = java.lang.Integer.MAX_VALUE;
                        ip++;
                        break;
                    case android.os.PatternMatcher.PARSED_MODIFIER_ONE_OR_MORE :
                        minRepetition = 1;
                        maxRepetition = java.lang.Integer.MAX_VALUE;
                        ip++;
                        break;
                    case android.os.PatternMatcher.PARSED_MODIFIER_RANGE_START :
                        minRepetition = parsedPattern[++ip];
                        maxRepetition = parsedPattern[++ip];
                        ip += 2;// step over PARSED_MODIFIER_RANGE_STOP and on to the next token

                        break;
                    default :
                        minRepetition = maxRepetition = 1;// implied literal

                        break;
                }
            }
            if (minRepetition > maxRepetition) {
                return false;
            }
            // attempt to match as many characters as possible
            int matched = android.os.PatternMatcher.matchChars(match, im, LM, tokenType, minRepetition, maxRepetition, parsedPattern, charSetStart, charSetEnd);
            // if we found a conflict, return false immediately
            if (matched == android.os.PatternMatcher.NO_MATCH) {
                return false;
            }
            // move the match pointer the number of characters matched
            im += matched;
        } 
        return (ip >= LP) && (im >= LM);// have parsed entire string and regex

    }

    private static int matchChars(java.lang.String match, int im, final int lm, int tokenType, int minRepetition, int maxRepetition, int[] parsedPattern, int tokenStart, int tokenEnd) {
        int matched = 0;
        while ((matched < maxRepetition) && android.os.PatternMatcher.matchChar(match, im + matched, lm, tokenType, parsedPattern, tokenStart, tokenEnd)) {
            matched++;
        } 
        return matched < minRepetition ? android.os.PatternMatcher.NO_MATCH : matched;
    }

    private static boolean matchChar(java.lang.String match, int im, final int lm, int tokenType, int[] parsedPattern, int tokenStart, int tokenEnd) {
        if (im >= lm) {
            // we've overrun the string, no match
            return false;
        }
        switch (tokenType) {
            case android.os.PatternMatcher.TOKEN_TYPE_ANY :
                return true;
            case android.os.PatternMatcher.TOKEN_TYPE_SET :
                for (int i = tokenStart; i < tokenEnd; i += 2) {
                    char matchChar = match.charAt(im);
                    if ((matchChar >= parsedPattern[i]) && (matchChar <= parsedPattern[i + 1])) {
                        return true;
                    }
                }
                return false;
            case android.os.PatternMatcher.TOKEN_TYPE_INVERSE_SET :
                for (int i = tokenStart; i < tokenEnd; i += 2) {
                    char matchChar = match.charAt(im);
                    if ((matchChar >= parsedPattern[i]) && (matchChar <= parsedPattern[i + 1])) {
                        return false;
                    }
                }
                return true;
            case android.os.PatternMatcher.TOKEN_TYPE_LITERAL :
                return match.charAt(im) == parsedPattern[tokenStart];
            default :
                return false;
        }
    }
}

