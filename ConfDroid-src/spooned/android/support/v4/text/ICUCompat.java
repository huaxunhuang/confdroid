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


public final class ICUCompat {
    interface ICUCompatImpl {
        public java.lang.String maximizeAndGetScript(java.util.Locale locale);
    }

    static class ICUCompatImplBase implements android.support.v4.text.ICUCompat.ICUCompatImpl {
        @java.lang.Override
        public java.lang.String maximizeAndGetScript(java.util.Locale locale) {
            return null;
        }
    }

    static class ICUCompatImplIcs implements android.support.v4.text.ICUCompat.ICUCompatImpl {
        @java.lang.Override
        public java.lang.String maximizeAndGetScript(java.util.Locale locale) {
            return android.support.v4.text.ICUCompatIcs.maximizeAndGetScript(locale);
        }
    }

    static class ICUCompatImplLollipop implements android.support.v4.text.ICUCompat.ICUCompatImpl {
        @java.lang.Override
        public java.lang.String maximizeAndGetScript(java.util.Locale locale) {
            return android.support.v4.text.ICUCompatApi23.maximizeAndGetScript(locale);
        }
    }

    private static final android.support.v4.text.ICUCompat.ICUCompatImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 21) {
            IMPL = new android.support.v4.text.ICUCompat.ICUCompatImplLollipop();
        } else
            if (version >= 14) {
                IMPL = new android.support.v4.text.ICUCompat.ICUCompatImplIcs();
            } else {
                IMPL = new android.support.v4.text.ICUCompat.ICUCompatImplBase();
            }

    }

    /**
     * Returns the script for a given Locale.
     *
     * If the locale isn't already in its maximal form, likely subtags for the provided locale
     * ID are added before we determine the script. For further details, see the following CLDR
     * technical report :
     *
     * http://www.unicode.org/reports/tr35/#Likely_Subtags
     *
     * If locale is already in the maximal form, or there is no data available for maximization,
     * it will be just returned. For example, "und-Zzzz" cannot be maximized, since there is no
     * reasonable maximization.
     *
     * Examples:
     *
     * "en" maximizes to "en_Latn_US"
     * "de" maximizes to "de_Latn_US"
     * "sr" maximizes to "sr_Cyrl_RS"
     * "sh" maximizes to "sr_Latn_RS" (Note this will not reverse.)
     * "zh_Hani" maximizes to "zh_Hans_CN" (Note this will not reverse.)
     *
     * @return 
     */
    public static java.lang.String maximizeAndGetScript(java.util.Locale locale) {
        return android.support.v4.text.ICUCompat.IMPL.maximizeAndGetScript(locale);
    }

    private ICUCompat() {
    }
}

