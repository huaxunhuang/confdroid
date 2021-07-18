/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v4.text;


/**
 * Some objects that implement TextDirectionHeuristic.
 */
public final class TextDirectionHeuristicsCompat {
    /**
     * Always decides that the direction is left to right.
     */
    public static final android.support.v4.text.TextDirectionHeuristicCompat LTR = /* no algorithm */
    new android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal(null, false);

    /**
     * Always decides that the direction is right to left.
     */
    public static final android.support.v4.text.TextDirectionHeuristicCompat RTL = /* no algorithm */
    new android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal(null, true);

    /**
     * Determines the direction based on the first strong directional character, including bidi
     * format chars, falling back to left to right if it finds none. This is the default behavior
     * of the Unicode Bidirectional Algorithm.
     */
    public static final android.support.v4.text.TextDirectionHeuristicCompat FIRSTSTRONG_LTR = new android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal(android.support.v4.text.TextDirectionHeuristicsCompat.FirstStrong.INSTANCE, false);

    /**
     * Determines the direction based on the first strong directional character, including bidi
     * format chars, falling back to right to left if it finds none. This is similar to the default
     * behavior of the Unicode Bidirectional Algorithm, just with different fallback behavior.
     */
    public static final android.support.v4.text.TextDirectionHeuristicCompat FIRSTSTRONG_RTL = new android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal(android.support.v4.text.TextDirectionHeuristicsCompat.FirstStrong.INSTANCE, true);

    /**
     * If the text contains any strong right to left non-format character, determines that the
     * direction is right to left, falling back to left to right if it finds none.
     */
    public static final android.support.v4.text.TextDirectionHeuristicCompat ANYRTL_LTR = new android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal(android.support.v4.text.TextDirectionHeuristicsCompat.AnyStrong.INSTANCE_RTL, false);

    /**
     * Force the paragraph direction to the Locale direction. Falls back to left to right.
     */
    public static final android.support.v4.text.TextDirectionHeuristicCompat LOCALE = android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionHeuristicLocale.INSTANCE;

    /**
     * State constants for taking care about true / false / unknown
     */
    private static final int STATE_TRUE = 0;

    private static final int STATE_FALSE = 1;

    private static final int STATE_UNKNOWN = 2;

    static int isRtlText(int directionality) {
        switch (directionality) {
            case java.lang.Character.DIRECTIONALITY_LEFT_TO_RIGHT :
                return android.support.v4.text.TextDirectionHeuristicsCompat.STATE_FALSE;
            case java.lang.Character.DIRECTIONALITY_RIGHT_TO_LEFT :
            case java.lang.Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC :
                return android.support.v4.text.TextDirectionHeuristicsCompat.STATE_TRUE;
            default :
                return android.support.v4.text.TextDirectionHeuristicsCompat.STATE_UNKNOWN;
        }
    }

    static int isRtlTextOrFormat(int directionality) {
        switch (directionality) {
            case java.lang.Character.DIRECTIONALITY_LEFT_TO_RIGHT :
            case java.lang.Character.DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING :
            case java.lang.Character.DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE :
                return android.support.v4.text.TextDirectionHeuristicsCompat.STATE_FALSE;
            case java.lang.Character.DIRECTIONALITY_RIGHT_TO_LEFT :
            case java.lang.Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC :
            case java.lang.Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING :
            case java.lang.Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE :
                return android.support.v4.text.TextDirectionHeuristicsCompat.STATE_TRUE;
            default :
                return android.support.v4.text.TextDirectionHeuristicsCompat.STATE_UNKNOWN;
        }
    }

    /**
     * Computes the text direction based on an algorithm.  Subclasses implement
     * {@link #defaultIsRtl} to handle cases where the algorithm cannot determine the
     * direction from the text alone.
     */
    private static abstract class TextDirectionHeuristicImpl implements android.support.v4.text.TextDirectionHeuristicCompat {
        private final android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionAlgorithm mAlgorithm;

        public TextDirectionHeuristicImpl(android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionAlgorithm algorithm) {
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
                case android.support.v4.text.TextDirectionHeuristicsCompat.STATE_TRUE :
                    return true;
                case android.support.v4.text.TextDirectionHeuristicsCompat.STATE_FALSE :
                    return false;
                default :
                    return defaultIsRtl();
            }
        }
    }

    private static class TextDirectionHeuristicInternal extends android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionHeuristicImpl {
        private final boolean mDefaultIsRtl;

        TextDirectionHeuristicInternal(android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionAlgorithm algorithm, boolean defaultIsRtl) {
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
     * direction. This is the standard Unicode Bidirectional algorithm.
     */
    private static class FirstStrong implements android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionAlgorithm {
        @java.lang.Override
        public int checkRtl(java.lang.CharSequence cs, int start, int count) {
            int result = android.support.v4.text.TextDirectionHeuristicsCompat.STATE_UNKNOWN;
            for (int i = start, e = start + count; (i < e) && (result == android.support.v4.text.TextDirectionHeuristicsCompat.STATE_UNKNOWN); ++i) {
                result = android.support.v4.text.TextDirectionHeuristicsCompat.isRtlTextOrFormat(java.lang.Character.getDirectionality(cs.charAt(i)));
            }
            return result;
        }

        private FirstStrong() {
        }

        public static final android.support.v4.text.TextDirectionHeuristicsCompat.FirstStrong INSTANCE = new android.support.v4.text.TextDirectionHeuristicsCompat.FirstStrong();
    }

    /**
     * Algorithm that uses the presence of any strong directional non-format
     * character (e.g. excludes LRE, LRO, RLE, RLO) to determine the
     * direction of text.
     */
    private static class AnyStrong implements android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionAlgorithm {
        private final boolean mLookForRtl;

        @java.lang.Override
        public int checkRtl(java.lang.CharSequence cs, int start, int count) {
            boolean haveUnlookedFor = false;
            for (int i = start, e = start + count; i < e; ++i) {
                switch (android.support.v4.text.TextDirectionHeuristicsCompat.isRtlText(java.lang.Character.getDirectionality(cs.charAt(i)))) {
                    case android.support.v4.text.TextDirectionHeuristicsCompat.STATE_TRUE :
                        if (mLookForRtl) {
                            return android.support.v4.text.TextDirectionHeuristicsCompat.STATE_TRUE;
                        }
                        haveUnlookedFor = true;
                        break;
                    case android.support.v4.text.TextDirectionHeuristicsCompat.STATE_FALSE :
                        if (!mLookForRtl) {
                            return android.support.v4.text.TextDirectionHeuristicsCompat.STATE_FALSE;
                        }
                        haveUnlookedFor = true;
                        break;
                    default :
                        break;
                }
            }
            if (haveUnlookedFor) {
                return mLookForRtl ? android.support.v4.text.TextDirectionHeuristicsCompat.STATE_FALSE : android.support.v4.text.TextDirectionHeuristicsCompat.STATE_TRUE;
            }
            return android.support.v4.text.TextDirectionHeuristicsCompat.STATE_UNKNOWN;
        }

        private AnyStrong(boolean lookForRtl) {
            this.mLookForRtl = lookForRtl;
        }

        public static final android.support.v4.text.TextDirectionHeuristicsCompat.AnyStrong INSTANCE_RTL = new android.support.v4.text.TextDirectionHeuristicsCompat.AnyStrong(true);

        public static final android.support.v4.text.TextDirectionHeuristicsCompat.AnyStrong INSTANCE_LTR = new android.support.v4.text.TextDirectionHeuristicsCompat.AnyStrong(false);
    }

    /**
     * Algorithm that uses the Locale direction to force the direction of a paragraph.
     */
    private static class TextDirectionHeuristicLocale extends android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionHeuristicImpl {
        public TextDirectionHeuristicLocale() {
            super(null);
        }

        @java.lang.Override
        protected boolean defaultIsRtl() {
            final int dir = android.support.v4.text.TextUtilsCompat.getLayoutDirectionFromLocale(java.util.Locale.getDefault());
            return dir == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL;
        }

        public static final android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionHeuristicLocale INSTANCE = new android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionHeuristicLocale();
    }

    private TextDirectionHeuristicsCompat() {
    }
}

