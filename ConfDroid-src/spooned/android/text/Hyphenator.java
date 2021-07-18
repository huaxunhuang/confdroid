/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * Hyphenator is a wrapper class for a native implementation of automatic hyphenation,
 * in essence finding valid hyphenation opportunities in a word.
 *
 * @unknown 
 */
public class Hyphenator {
    // This class has deliberately simple lifetime management (no finalizer) because in
    // the common case a process will use a very small number of locales.
    private static java.lang.String TAG = "Hyphenator";

    private static final java.lang.Object sLock = new java.lang.Object();

    @com.android.internal.annotations.GuardedBy("sLock")
    static final java.util.HashMap<java.util.Locale, android.text.Hyphenator> sMap = new java.util.HashMap<java.util.Locale, android.text.Hyphenator>();

    static final android.text.Hyphenator sEmptyHyphenator = new android.text.Hyphenator(android.text.StaticLayout.nLoadHyphenator(null, 0), null);

    private final long mNativePtr;

    // We retain a reference to the buffer to keep the memory mapping valid
    @java.lang.SuppressWarnings("unused")
    private final java.nio.ByteBuffer mBuffer;

    private Hyphenator(long nativePtr, java.nio.ByteBuffer b) {
        mNativePtr = nativePtr;
        mBuffer = b;
    }

    public long getNativePtr() {
        return mNativePtr;
    }

    public static android.text.Hyphenator get(@android.annotation.Nullable
    java.util.Locale locale) {
        synchronized(android.text.Hyphenator.sLock) {
            android.text.Hyphenator result = android.text.Hyphenator.sMap.get(locale);
            if (result != null) {
                return result;
            }
            // If there's a variant, fall back to language+variant only, if available
            final java.lang.String variant = locale.getVariant();
            if (!variant.isEmpty()) {
                final java.util.Locale languageAndVariantOnlyLocale = new java.util.Locale(locale.getLanguage(), "", variant);
                result = android.text.Hyphenator.sMap.get(languageAndVariantOnlyLocale);
                if (result != null) {
                    android.text.Hyphenator.sMap.put(locale, result);
                    return result;
                }
            }
            // Fall back to language-only, if available
            final java.util.Locale languageOnlyLocale = new java.util.Locale(locale.getLanguage());
            result = android.text.Hyphenator.sMap.get(languageOnlyLocale);
            if (result != null) {
                android.text.Hyphenator.sMap.put(locale, result);
                return result;
            }
            // Fall back to script-only, if available
            final java.lang.String script = locale.getScript();
            if (!script.equals("")) {
                final java.util.Locale scriptOnlyLocale = new java.util.Locale.Builder().setLanguage("und").setScript(script).build();
                result = android.text.Hyphenator.sMap.get(scriptOnlyLocale);
                if (result != null) {
                    android.text.Hyphenator.sMap.put(locale, result);
                    return result;
                }
            }
            android.text.Hyphenator.sMap.put(locale, android.text.Hyphenator.sEmptyHyphenator);// To remember we found nothing.

        }
        return android.text.Hyphenator.sEmptyHyphenator;
    }

    private static android.text.Hyphenator loadHyphenator(java.lang.String languageTag) {
        java.lang.String patternFilename = ("hyph-" + languageTag.toLowerCase(java.util.Locale.US)) + ".hyb";
        java.io.File patternFile = new java.io.File(android.text.Hyphenator.getSystemHyphenatorLocation(), patternFilename);
        try {
            java.io.RandomAccessFile f = new java.io.RandomAccessFile(patternFile, "r");
            try {
                java.nio.channels.FileChannel fc = f.getChannel();
                java.nio.MappedByteBuffer buf = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fc.size());
                long nativePtr = android.text.StaticLayout.nLoadHyphenator(buf, 0);
                return new android.text.Hyphenator(nativePtr, buf);
            } finally {
                f.close();
            }
        } catch (java.io.IOException e) {
            android.util.Log.e(android.text.Hyphenator.TAG, "error loading hyphenation " + patternFile, e);
            return null;
        }
    }

    private static java.io.File getSystemHyphenatorLocation() {
        return new java.io.File("/system/usr/hyphen-data");
    }

    // This array holds pairs of language tags that are used to prefill the map from locale to
    // hyphenation data: The hyphenation data for the first field will be prefilled from the
    // hyphenation data for the second field.
    // 
    // The aliases that are computable by the get() method above are not included.
    private static final java.lang.String[][] LOCALE_FALLBACK_DATA = new java.lang.String[][]{ // English locales that fall back to en-US. The data is
    // from CLDR. It's all English locales, minus the locales whose
    // parent is en-001 (from supplementalData.xml, under <parentLocales>).
    // TODO: Figure out how to get this from ICU.
    new java.lang.String[]{ "en-AS", "en-US" }// English (American Samoa)
    // English (American Samoa)
    // English (American Samoa)
    , new java.lang.String[]{ "en-GU", "en-US" }// English (Guam)
    // English (Guam)
    // English (Guam)
    , new java.lang.String[]{ "en-MH", "en-US" }// English (Marshall Islands)
    // English (Marshall Islands)
    // English (Marshall Islands)
    , new java.lang.String[]{ "en-MP", "en-US" }// English (Northern Mariana Islands)
    // English (Northern Mariana Islands)
    // English (Northern Mariana Islands)
    , new java.lang.String[]{ "en-PR", "en-US" }// English (Puerto Rico)
    // English (Puerto Rico)
    // English (Puerto Rico)
    , new java.lang.String[]{ "en-UM", "en-US" }// English (United States Minor Outlying Islands)
    // English (United States Minor Outlying Islands)
    // English (United States Minor Outlying Islands)
    , new java.lang.String[]{ "en-VI", "en-US" }// English (Virgin Islands)
    // English (Virgin Islands)
    // English (Virgin Islands)
    , // All English locales other than those falling back to en-US are mapped to en-GB.
    new java.lang.String[]{ "en", "en-GB" }, // For German, we're assuming the 1996 (and later) orthography by default.
    new java.lang.String[]{ "de", "de-1996" }, // Liechtenstein uses the Swiss hyphenation rules for the 1901 orthography.
    new java.lang.String[]{ "de-LI-1901", "de-CH-1901" }, // Norwegian is very probably Norwegian Bokmål.
    new java.lang.String[]{ "no", "nb" }, // Use mn-Cyrl. According to CLDR's likelySubtags.xml, mn is most likely to be mn-Cyrl.
    new java.lang.String[]{ "mn", "mn-Cyrl" }// Mongolian
    // Mongolian
    // Mongolian
    , // Fall back to Ethiopic script for languages likely to be written in Ethiopic.
    // Data is from CLDR's likelySubtags.xml.
    // TODO: Convert this to a mechanism using ICU4J's ULocale#addLikelySubtags().
    new java.lang.String[]{ "am", "und-Ethi" }// Amharic
    // Amharic
    // Amharic
    , new java.lang.String[]{ "byn", "und-Ethi" }// Blin
    // Blin
    // Blin
    , new java.lang.String[]{ "gez", "und-Ethi" }// Geʻez
    // Geʻez
    // Geʻez
    , new java.lang.String[]{ "ti", "und-Ethi" }// Tigrinya
    // Tigrinya
    // Tigrinya
    , new java.lang.String[]{ "wal", "und-Ethi" }// Wolaytta
    // Wolaytta
    // Wolaytta
     };

    /**
     * Load hyphenation patterns at initialization time. We want to have patterns
     * for all locales loaded and ready to use so we don't have to do any file IO
     * on the UI thread when drawing text in different locales.
     *
     * @unknown 
     */
    public static void init() {
        android.text.Hyphenator.sMap.put(null, null);
        // TODO: replace this with a discovery-based method that looks into /system/usr/hyphen-data
        java.lang.String[] availableLanguages = new java.lang.String[]{ "as", "bn", "cy", "da", "de-1901", "de-1996", "de-CH-1901", "en-GB", "en-US", "es", "et", "eu", "fr", "ga", "gu", "hi", "hr", "hu", "hy", "kn", "ml", "mn-Cyrl", "mr", "nb", "nn", "or", "pa", "pt", "sl", "ta", "te", "tk", "und-Ethi" };
        for (int i = 0; i < availableLanguages.length; i++) {
            java.lang.String languageTag = availableLanguages[i];
            android.text.Hyphenator h = android.text.Hyphenator.loadHyphenator(languageTag);
            if (h != null) {
                android.text.Hyphenator.sMap.put(java.util.Locale.forLanguageTag(languageTag), h);
            }
        }
        for (int i = 0; i < android.text.Hyphenator.LOCALE_FALLBACK_DATA.length; i++) {
            java.lang.String language = android.text.Hyphenator.LOCALE_FALLBACK_DATA[i][0];
            java.lang.String fallback = android.text.Hyphenator.LOCALE_FALLBACK_DATA[i][1];
            android.text.Hyphenator.sMap.put(java.util.Locale.forLanguageTag(language), android.text.Hyphenator.sMap.get(java.util.Locale.forLanguageTag(fallback)));
        }
    }
}

