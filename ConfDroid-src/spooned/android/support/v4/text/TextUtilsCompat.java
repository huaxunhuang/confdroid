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


public final class TextUtilsCompat {
    private static class TextUtilsCompatImpl {
        TextUtilsCompatImpl() {
        }

        @android.support.annotation.NonNull
        public java.lang.String htmlEncode(@android.support.annotation.NonNull
        java.lang.String s) {
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
                        // The named character reference &apos; (the apostrophe, U+0027) was
                        // introduced in XML 1.0 but does not appear in HTML. Authors should
                        // therefore use &#39; instead of &apos; to work as expected in HTML 4
                        // user agents.
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

        public int getLayoutDirectionFromLocale(@android.support.annotation.Nullable
        java.util.Locale locale) {
            if ((locale != null) && (!locale.equals(android.support.v4.text.TextUtilsCompat.ROOT))) {
                final java.lang.String scriptSubtag = android.support.v4.text.ICUCompat.maximizeAndGetScript(locale);
                if (scriptSubtag == null)
                    return android.support.v4.text.TextUtilsCompat.TextUtilsCompatImpl.getLayoutDirectionFromFirstChar(locale);

                // This is intentionally limited to Arabic and Hebrew scripts, since older
                // versions of Android platform only considered those scripts to be right-to-left.
                if (scriptSubtag.equalsIgnoreCase(android.support.v4.text.TextUtilsCompat.ARAB_SCRIPT_SUBTAG) || scriptSubtag.equalsIgnoreCase(android.support.v4.text.TextUtilsCompat.HEBR_SCRIPT_SUBTAG)) {
                    return android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL;
                }
            }
            return android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR;
        }

        /**
         * Fallback algorithm to detect the locale direction. Rely on the first char of the
         * localized locale name. This will not work if the localized locale name is in English
         * (this is the case for ICU 4.4 and "Urdu" script)
         *
         * @param locale
         * 		
         * @return the layout direction. This may be one of:
        {@link ViewCompat#LAYOUT_DIRECTION_LTR} or
        {@link ViewCompat#LAYOUT_DIRECTION_RTL}.

        Be careful: this code will need to be updated when vertical scripts will be supported
         */
        private static int getLayoutDirectionFromFirstChar(@android.support.annotation.NonNull
        java.util.Locale locale) {
            switch (java.lang.Character.getDirectionality(locale.getDisplayName(locale).charAt(0))) {
                case java.lang.Character.DIRECTIONALITY_RIGHT_TO_LEFT :
                case java.lang.Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC :
                    return android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL;
                case java.lang.Character.DIRECTIONALITY_LEFT_TO_RIGHT :
                default :
                    return android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR;
            }
        }
    }

    private static class TextUtilsCompatJellybeanMr1Impl extends android.support.v4.text.TextUtilsCompat.TextUtilsCompatImpl {
        TextUtilsCompatJellybeanMr1Impl() {
        }

        @java.lang.Override
        @android.support.annotation.NonNull
        public java.lang.String htmlEncode(@android.support.annotation.NonNull
        java.lang.String s) {
            return android.support.v4.text.TextUtilsCompatJellybeanMr1.htmlEncode(s);
        }

        @java.lang.Override
        public int getLayoutDirectionFromLocale(@android.support.annotation.Nullable
        java.util.Locale locale) {
            return android.support.v4.text.TextUtilsCompatJellybeanMr1.getLayoutDirectionFromLocale(locale);
        }
    }

    private static final android.support.v4.text.TextUtilsCompat.TextUtilsCompatImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 17) {
            // JellyBean MR1
            IMPL = new android.support.v4.text.TextUtilsCompat.TextUtilsCompatJellybeanMr1Impl();
        } else {
            IMPL = new android.support.v4.text.TextUtilsCompat.TextUtilsCompatImpl();
        }
    }

    /**
     * Html-encode the string.
     *
     * @param s
     * 		the string to be encoded
     * @return the encoded string
     */
    @android.support.annotation.NonNull
    public static java.lang.String htmlEncode(@android.support.annotation.NonNull
    java.lang.String s) {
        return android.support.v4.text.TextUtilsCompat.IMPL.htmlEncode(s);
    }

    /**
     * Return the layout direction for a given Locale
     *
     * @param locale
     * 		the Locale for which we want the layout direction. Can be null.
     * @return the layout direction. This may be one of:
    {@link ViewCompat#LAYOUT_DIRECTION_LTR} or
    {@link ViewCompat#LAYOUT_DIRECTION_RTL}.

    Be careful: this code will need to be updated when vertical scripts will be supported
     */
    public static int getLayoutDirectionFromLocale(@android.support.annotation.Nullable
    java.util.Locale locale) {
        return android.support.v4.text.TextUtilsCompat.IMPL.getLayoutDirectionFromLocale(locale);
    }

    public static final java.util.Locale ROOT = new java.util.Locale("", "");

    static java.lang.String ARAB_SCRIPT_SUBTAG = "Arab";

    static java.lang.String HEBR_SCRIPT_SUBTAG = "Hebr";

    private TextUtilsCompat() {
    }
}

