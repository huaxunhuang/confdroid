/**
 * Copyright (C) 2011 The Android Open Source Project
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


/**
 * Some objects that implement {@link TextDirectionHeuristic}. Use these with
 * the {@link BidiFormatter#unicodeWrap unicodeWrap()} methods in {@link BidiFormatter}.
 * Also notice that these direction heuristics correspond to the same types of constants
 * provided in the {@link android.view.View} class for {@link android.view.View#setTextDirection
 * setTextDirection()}, such as {@link android.view.View#TEXT_DIRECTION_RTL}.
 * <p>To support versions lower than {@link android.os.Build.VERSION_CODES#JELLY_BEAN_MR2},
 * you can use the support library's {@link android.support.v4.text.TextDirectionHeuristicsCompat}
 * class.
 */
public class TextDirectionHeuristics {
    /**
     * Always decides that the direction is left to right.
     */
    public static final android.text.TextDirectionHeuristic LTR = /* no algorithm */
    new android.text.TextDirectionHeuristics.TextDirectionHeuristicInternal(null, false);

    /**
     * Always decides that the direction is right to left.
     */
    public static final android.text.TextDirectionHeuristic RTL = /* no algorithm */
    new android.text.TextDirectionHeuristics.TextDirectionHeuristicInternal(null, true);

    /**
     * Determines the direction based on the first strong directional character, including bidi
     * format chars, falling back to left to right if it finds none. This is the default behavior
     * of the Unicode Bidirectional Algorithm.
     */
    public static final android.text.TextDirectionHeuristic FIRSTSTRONG_LTR = new android.text.TextDirectionHeuristics.TextDirectionHeuristicInternal(android.text.TextDirectionHeuristics.FirstStrong.INSTANCE, false);

    /**
     * Determines the direction based on the first strong directional character, including bidi
     * format chars, falling back to right to left if it finds none. This is similar to the default
     * behavior of the Unicode Bidirectional Algorithm, just with different fallback behavior.
     */
    public static final android.text.TextDirectionHeuristic FIRSTSTRONG_RTL = new android.text.TextDirectionHeuristics.TextDirectionHeuristicInternal(android.text.TextDirectionHeuristics.FirstStrong.INSTANCE, true);

    /**
     * If the text contains any strong right to left non-format character, determines that the
     * direction is right to left, falling back to left to right if it finds none.
     */
    public static final android.text.TextDirectionHeuristic ANYRTL_LTR = new android.text.TextDirectionHeuristics.TextDirectionHeuristicInternal(android.text.TextDirectionHeuristics.AnyStrong.INSTANCE_RTL, false);

    /**
     * Force the paragraph direction to the Locale direction. Falls back to left to right.
     */
    public static final android.text.TextDirectionHeuristic LOCALE = android.text.TextDirectionHeuristics.TextDirectionHeuristicLocale.INSTANCE;

    /**
     * State constants for taking care about true / false / unknown
     */
    private static final int STATE_TRUE = 0;

    private static final int STATE_FALSE = 1;

    private static final int STATE_UNKNOWN = 2;

    /* Returns STATE_TRUE for strong RTL characters, STATE_FALSE for strong LTR characters, and
    STATE_UNKNOWN for everything else.
     */
    private static int isRtlCodePoint(int codePoint) {
        switch (java.lang.Character.getDirectionality(codePoint)) {
            case java.lang.Character.DIRECTIONALITY_LEFT_TO_RIGHT :
                return android.text.TextDirectionHeuristics.STATE_FALSE;
            case java.lang.Character.DIRECTIONALITY_RIGHT_TO_LEFT :
            case java.lang.Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC :
                return android.text.TextDirectionHeuristics.STATE_TRUE;
            case java.lang.Character.DIRECTIONALITY_UNDEFINED :
                // Unassigned characters still have bidi direction, defined at:
                // http://www.unicode.org/Public/UCD/latest/ucd/extracted/DerivedBidiClass.txt
                if (((((((0x590 <= codePoint) && (codePoint <= 0x8ff)) || ((0xfb1d <= codePoint) && (codePoint <= 0xfdcf))) || ((0xfdf0 <= codePoint) && (codePoint <= 0xfdff))) || ((0xfe70 <= codePoint) && (codePoint <= 0xfeff))) || ((0x10800 <= codePoint) && (codePoint <= 0x10fff))) || ((0x1e800 <= codePoint) && (codePoint <= 0x1efff))) {
                    // Unassigned RTL character
                    return android.text.TextDirectionHeuristics.STATE_TRUE;
                } else
                    // Potentially-unassigned Default_Ignorable. Ranges are from unassigned
                    // characters that have Unicode property Other_Default_Ignorable_Code_Point
                    // plus some enlargening to cover bidi isolates and simplify checks.
                    if ((((((((0x2065 <= codePoint) && (codePoint <= 0x2069)) || ((0xfff0 <= codePoint) && (codePoint <= 0xfff8))) || ((0xe0000 <= codePoint) && (codePoint <= 0xe0fff))) || // Non-character
                    ((0xfdd0 <= codePoint) && (codePoint <= 0xfdef))) || ((codePoint & 0xfffe) == 0xfffe)) || // Currency symbol
                    ((0x20a0 <= codePoint) && (codePoint <= 0x20cf))) || // Unpaired surrogate
                    ((0xd800 <= codePoint) && (codePoint <= 0xdfff))) {
                        return android.text.TextDirectionHeuristics.STATE_UNKNOWN;
                    } else {
                        // Unassigned LTR character
                        return android.text.TextDirectionHeuristics.STATE_FALSE;
                    }

            default :
                return android.text.TextDirectionHeuristics.STATE_UNKNOWN;
        }
    }

    /**
     * Computes the text direction based on an algorithm.  Subclasses implement
     * {@link #defaultIsRtl} to handle cases where the algorithm cannot determine the
     * direction from the text alone.
     */
    private static abstract class TextDirectionHeuristicImpl implements android.text.TextDirectionHeuristic {
        private final android.text.TextDirectionHeuristics.TextDirectionAlgorithm mAlgorithm;

        public TextDirectionHeuristicImpl(android.text.TextDirectionHeuristics.TextDirectionAlgorithm algorithm) {
            mAlgorithm = algorithm;
        }

        /**
         * Return true if the default text direction is rtl.
         */
        protected abstract boolean defaultIsRtl();

        @java.lang.Override
        public boolean isRtl(char[] array, int start, int count) {
            return isRtl(java.nio.CharBuffer.wrap(array), start, count);
        }

        @java.lang.Override
        public boolean isRtl(java.lang.CharSequence cs, int start, int count) {
            if ((((cs == null) || (start < 0)) || (count < 0)) || ((cs.length() - count) < start)) {
                throw new java.lang.IllegalArgumentException();
            }
            if (mAlgorithm == null) {
                return defaultIsRtl();
            }
            return doCheck(cs, start, count);
        }

        private boolean doCheck(java.lang.CharSequence cs, int start, int count) {
            switch (mAlgorithm.checkRtl(cs, start, count)) {
                case android.text.TextDirectionHeuristics.STATE_TRUE :
                    return true;
                case android.text.TextDirectionHeuristics.STATE_FALSE :
                    return false;
                default :
                    return defaultIsRtl();
            }
        }
    }

    private static class TextDirectionHeuristicInternal extends android.text.TextDirectionHeuristics.TextDirectionHeuristicImpl {
        private final boolean mDefaultIsRtl;

        private TextDirectionHeuristicInternal(android.text.TextDirectionHeuristics.TextDirectionAlgorithm algorithm, boolean defaultIsRtl) {
            super(algorithm);
            mDefaultIsRtl = defaultIsRtl;
        }

        @java.lang.Override
        protected boolean defaultIsRtl() {
            return mDefaultIsRtl;
        }
    }

    /**
     * Interface for an algorithm to guess the direction of a paragraph of text.
     */
    private static interface TextDirectionAlgorithm {
        /**
         * Returns whether the range of text is RTL according to the algorithm.
         */
        int checkRtl(java.lang.CharSequence cs, int start, int count);
    }

    /**
     * Algorithm that uses the first strong directional character to determine the paragraph
     * direction. This is the standard Unicode Bidirectional Algorithm (steps P2 and P3), with the
     * exception that if no strong character is found, UNKNOWN is returned.
     */
    private static class FirstStrong implements android.text.TextDirectionHeuristics.TextDirectionAlgorithm {
        @java.lang.Override
        public int checkRtl(java.lang.CharSequence cs, int start, int count) {
            int result = android.text.TextDirectionHeuristics.STATE_UNKNOWN;
            int openIsolateCount = 0;
            for (int cp, i = start, end = start + count; (i < end) && (result == android.text.TextDirectionHeuristics.STATE_UNKNOWN); i += java.lang.Character.charCount(cp)) {
                cp = java.lang.Character.codePointAt(cs, i);
                if ((0x2066 <= cp) && (cp <= 0x2068)) {
                    // Opening isolates
                    openIsolateCount += 1;
                } else
                    if (cp == 0x2069) {
                        // POP DIRECTIONAL ISOLATE (PDI)
                        if (openIsolateCount > 0)
                            openIsolateCount -= 1;

                    } else
                        if (openIsolateCount == 0) {
                            // Only consider the characters outside isolate pairs
                            result = android.text.TextDirectionHeuristics.isRtlCodePoint(cp);
                        }


            }
            return result;
        }

        private FirstStrong() {
        }

        public static final android.text.TextDirectionHeuristics.FirstStrong INSTANCE = new android.text.TextDirectionHeuristics.FirstStrong();
    }

    /**
     * Algorithm that uses the presence of any strong directional character of the type indicated
     * in the constructor parameter to determine the direction of text.
     *
     * Characters inside isolate pairs are skipped.
     */
    private static class AnyStrong implements android.text.TextDirectionHeuristics.TextDirectionAlgorithm {
        private final boolean mLookForRtl;

        @java.lang.Override
        public int checkRtl(java.lang.CharSequence cs, int start, int count) {
            boolean haveUnlookedFor = false;
            int openIsolateCount = 0;
            for (int cp, i = start, end = start + count; i < end; i += java.lang.Character.charCount(cp)) {
                cp = java.lang.Character.codePointAt(cs, i);
                if ((0x2066 <= cp) && (cp <= 0x2068)) {
                    // Opening isolates
                    openIsolateCount += 1;
                } else
                    if (cp == 0x2069) {
                        // POP DIRECTIONAL ISOLATE (PDI)
                        if (openIsolateCount > 0)
                            openIsolateCount -= 1;

                    } else
                        if (openIsolateCount == 0) {
                            // Only consider the characters outside isolate pairs
                            switch (android.text.TextDirectionHeuristics.isRtlCodePoint(cp)) {
                                case android.text.TextDirectionHeuristics.STATE_TRUE :
                                    if (mLookForRtl) {
                                        return android.text.TextDirectionHeuristics.STATE_TRUE;
                                    }
                                    haveUnlookedFor = true;
                                    break;
                                case android.text.TextDirectionHeuristics.STATE_FALSE :
                                    if (!mLookForRtl) {
                                        return android.text.TextDirectionHeuristics.STATE_FALSE;
                                    }
                                    haveUnlookedFor = true;
                                    break;
                                default :
                                    break;
                            }
                        }


            }
            if (haveUnlookedFor) {
                return mLookForRtl ? android.text.TextDirectionHeuristics.STATE_FALSE : android.text.TextDirectionHeuristics.STATE_TRUE;
            }
            return android.text.TextDirectionHeuristics.STATE_UNKNOWN;
        }

        private AnyStrong(boolean lookForRtl) {
            this.mLookForRtl = lookForRtl;
        }

        public static final android.text.TextDirectionHeuristics.AnyStrong INSTANCE_RTL = new android.text.TextDirectionHeuristics.AnyStrong(true);

        public static final android.text.TextDirectionHeuristics.AnyStrong INSTANCE_LTR = new android.text.TextDirectionHeuristics.AnyStrong(false);
    }

    /**
     * Algorithm that uses the Locale direction to force the direction of a paragraph.
     */
    private static class TextDirectionHeuristicLocale extends android.text.TextDirectionHeuristics.TextDirectionHeuristicImpl {
        public TextDirectionHeuristicLocale() {
            super(null);
        }

        @java.lang.Override
        protected boolean defaultIsRtl() {
            final int dir = android.text.TextUtils.getLayoutDirectionFromLocale(java.util.Locale.getDefault());
            return dir == android.view.View.LAYOUT_DIRECTION_RTL;
        }

        public static final android.text.TextDirectionHeuristics.TextDirectionHeuristicLocale INSTANCE = new android.text.TextDirectionHeuristics.TextDirectionHeuristicLocale();
    }
}

