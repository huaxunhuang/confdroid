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
package android.os;


/**
 * LocaleList is an immutable list of Locales, typically used to keep an ordered list of user
 * preferences for locales.
 */
public final class LocaleList implements android.os.Parcelable {
    private final java.util.Locale[] mList;

    // This is a comma-separated list of the locales in the LocaleList created at construction time,
    // basically the result of running each locale's toLanguageTag() method and concatenating them
    // with commas in between.
    @android.annotation.NonNull
    private final java.lang.String mStringRepresentation;

    private static final java.util.Locale[] sEmptyList = new java.util.Locale[0];

    private static final android.os.LocaleList sEmptyLocaleList = new android.os.LocaleList();

    /**
     * Retrieves the {@link Locale} at the specified index.
     *
     * @param index
     * 		The position to retrieve.
     * @return The {@link Locale} in the given index.
     */
    public java.util.Locale get(int index) {
        return (0 <= index) && (index < mList.length) ? mList[index] : null;
    }

    /**
     * Returns whether the {@link LocaleList} contains no {@link Locale} items.
     *
     * @return {@code true} if this {@link LocaleList} has no {@link Locale} items, {@code false}
    otherwise.
     */
    public boolean isEmpty() {
        return mList.length == 0;
    }

    /**
     * Returns the number of {@link Locale} items in this {@link LocaleList}.
     */
    @android.annotation.IntRange(from = 0)
    public int size() {
        return mList.length;
    }

    /**
     * Searches this {@link LocaleList} for the specified {@link Locale} and returns the index of
     * the first occurrence.
     *
     * @param locale
     * 		The {@link Locale} to search for.
     * @return The index of the first occurrence of the {@link Locale} or {@code -1} if the item
    wasn't found.
     */
    @android.annotation.IntRange(from = -1)
    public int indexOf(java.util.Locale locale) {
        for (int i = 0; i < mList.length; i++) {
            if (mList[i].equals(locale)) {
                return i;
            }
        }
        return -1;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (other == this)
            return true;

        if (!(other instanceof android.os.LocaleList))
            return false;

        final java.util.Locale[] otherList = ((android.os.LocaleList) (other)).mList;
        if (mList.length != otherList.length)
            return false;

        for (int i = 0; i < mList.length; i++) {
            if (!mList[i].equals(otherList[i]))
                return false;

        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < mList.length; i++) {
            result = (31 * result) + mList[i].hashCode();
        }
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("[");
        for (int i = 0; i < mList.length; i++) {
            sb.append(mList[i]);
            if (i < (mList.length - 1)) {
                sb.append(',');
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        dest.writeString(mStringRepresentation);
    }

    /**
     * Retrieves a String representation of the language tags in this list.
     */
    @android.annotation.NonNull
    public java.lang.String toLanguageTags() {
        return mStringRepresentation;
    }

    /**
     * Creates a new {@link LocaleList}.
     *
     * <p>For empty lists of {@link Locale} items it is better to use {@link #getEmptyLocaleList()},
     * which returns a pre-constructed empty list.</p>
     *
     * @throws NullPointerException
     * 		if any of the input locales is <code>null</code>.
     * @throws IllegalArgumentException
     * 		if any of the input locales repeat.
     */
    public LocaleList(@android.annotation.NonNull
    java.util.Locale... list) {
        if (list.length == 0) {
            mList = android.os.LocaleList.sEmptyList;
            mStringRepresentation = "";
        } else {
            final java.util.Locale[] localeList = new java.util.Locale[list.length];
            final java.util.HashSet<java.util.Locale> seenLocales = new java.util.HashSet<java.util.Locale>();
            final java.lang.StringBuilder sb = new java.lang.StringBuilder();
            for (int i = 0; i < list.length; i++) {
                final java.util.Locale l = list[i];
                if (l == null) {
                    throw new java.lang.NullPointerException(("list[" + i) + "] is null");
                } else
                    if (seenLocales.contains(l)) {
                        throw new java.lang.IllegalArgumentException(("list[" + i) + "] is a repetition");
                    } else {
                        final java.util.Locale localeClone = ((java.util.Locale) (l.clone()));
                        localeList[i] = localeClone;
                        sb.append(localeClone.toLanguageTag());
                        if (i < (list.length - 1)) {
                            sb.append(',');
                        }
                        seenLocales.add(localeClone);
                    }

            }
            mList = localeList;
            mStringRepresentation = sb.toString();
        }
    }

    /**
     * Constructs a locale list, with the topLocale moved to the front if it already is
     * in otherLocales, or added to the front if it isn't.
     *
     * {@hide }
     */
    public LocaleList(@android.annotation.NonNull
    java.util.Locale topLocale, android.os.LocaleList otherLocales) {
        if (topLocale == null) {
            throw new java.lang.NullPointerException("topLocale is null");
        }
        final int inputLength = (otherLocales == null) ? 0 : otherLocales.mList.length;
        int topLocaleIndex = -1;
        for (int i = 0; i < inputLength; i++) {
            if (topLocale.equals(otherLocales.mList[i])) {
                topLocaleIndex = i;
                break;
            }
        }
        final int outputLength = inputLength + (topLocaleIndex == (-1) ? 1 : 0);
        final java.util.Locale[] localeList = new java.util.Locale[outputLength];
        localeList[0] = ((java.util.Locale) (topLocale.clone()));
        if (topLocaleIndex == (-1)) {
            // topLocale was not in otherLocales
            for (int i = 0; i < inputLength; i++) {
                localeList[i + 1] = ((java.util.Locale) (otherLocales.mList[i].clone()));
            }
        } else {
            for (int i = 0; i < topLocaleIndex; i++) {
                localeList[i + 1] = ((java.util.Locale) (otherLocales.mList[i].clone()));
            }
            for (int i = topLocaleIndex + 1; i < inputLength; i++) {
                localeList[i] = ((java.util.Locale) (otherLocales.mList[i].clone()));
            }
        }
        final java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < outputLength; i++) {
            sb.append(localeList[i].toLanguageTag());
            if (i < (outputLength - 1)) {
                sb.append(',');
            }
        }
        mList = localeList;
        mStringRepresentation = sb.toString();
    }

    public static final android.os.Parcelable.Creator<android.os.LocaleList> CREATOR = new android.os.Parcelable.Creator<android.os.LocaleList>() {
        @java.lang.Override
        public android.os.LocaleList createFromParcel(android.os.Parcel source) {
            return android.os.LocaleList.forLanguageTags(source.readString());
        }

        @java.lang.Override
        public android.os.LocaleList[] newArray(int size) {
            return new android.os.LocaleList[size];
        }
    };

    /**
     * Retrieve an empty instance of {@link LocaleList}.
     */
    @android.annotation.NonNull
    public static android.os.LocaleList getEmptyLocaleList() {
        return android.os.LocaleList.sEmptyLocaleList;
    }

    /**
     * Generates a new LocaleList with the given language tags.
     *
     * @param list
     * 		The language tags to be included as a single {@link String} separated by commas.
     * @return A new instance with the {@link Locale} items identified by the given tags.
     */
    @android.annotation.NonNull
    public static android.os.LocaleList forLanguageTags(@android.annotation.Nullable
    java.lang.String list) {
        if ((list == null) || list.equals("")) {
            return android.os.LocaleList.getEmptyLocaleList();
        } else {
            final java.lang.String[] tags = list.split(",");
            final java.util.Locale[] localeArray = new java.util.Locale[tags.length];
            for (int i = 0; i < localeArray.length; i++) {
                localeArray[i] = java.util.Locale.forLanguageTag(tags[i]);
            }
            return new android.os.LocaleList(localeArray);
        }
    }

    private static java.lang.String getLikelyScript(java.util.Locale locale) {
        final java.lang.String script = locale.getScript();
        if (!script.isEmpty()) {
            return script;
        } else {
            // TODO: Cache the results if this proves to be too slow
            return android.icu.util.ULocale.addLikelySubtags(android.icu.util.ULocale.forLocale(locale)).getScript();
        }
    }

    private static final java.lang.String STRING_EN_XA = "en-XA";

    private static final java.lang.String STRING_AR_XB = "ar-XB";

    private static final java.util.Locale LOCALE_EN_XA = new java.util.Locale("en", "XA");

    private static final java.util.Locale LOCALE_AR_XB = new java.util.Locale("ar", "XB");

    private static final int NUM_PSEUDO_LOCALES = 2;

    private static boolean isPseudoLocale(java.lang.String locale) {
        return android.os.LocaleList.STRING_EN_XA.equals(locale) || android.os.LocaleList.STRING_AR_XB.equals(locale);
    }

    private static boolean isPseudoLocale(java.util.Locale locale) {
        return android.os.LocaleList.LOCALE_EN_XA.equals(locale) || android.os.LocaleList.LOCALE_AR_XB.equals(locale);
    }

    @android.annotation.IntRange(from = 0, to = 1)
    private static int matchScore(java.util.Locale supported, java.util.Locale desired) {
        if (supported.equals(desired)) {
            return 1;// return early so we don't do unnecessary computation

        }
        if (!supported.getLanguage().equals(desired.getLanguage())) {
            return 0;
        }
        if (android.os.LocaleList.isPseudoLocale(supported) || android.os.LocaleList.isPseudoLocale(desired)) {
            // The locales are not the same, but the languages are the same, and one of the locales
            // is a pseudo-locale. So this is not a match.
            return 0;
        }
        final java.lang.String supportedScr = android.os.LocaleList.getLikelyScript(supported);
        if (supportedScr.isEmpty()) {
            // If we can't guess a script, we don't know enough about the locales' language to find
            // if the locales match. So we fall back to old behavior of matching, which considered
            // locales with different regions different.
            final java.lang.String supportedRegion = supported.getCountry();
            return supportedRegion.isEmpty() || supportedRegion.equals(desired.getCountry()) ? 1 : 0;
        }
        final java.lang.String desiredScr = android.os.LocaleList.getLikelyScript(desired);
        // There is no match if the two locales use different scripts. This will most imporantly
        // take care of traditional vs simplified Chinese.
        return supportedScr.equals(desiredScr) ? 1 : 0;
    }

    private int findFirstMatchIndex(java.util.Locale supportedLocale) {
        for (int idx = 0; idx < mList.length; idx++) {
            final int score = android.os.LocaleList.matchScore(supportedLocale, mList[idx]);
            if (score > 0) {
                return idx;
            }
        }
        return java.lang.Integer.MAX_VALUE;
    }

    private static final java.util.Locale EN_LATN = java.util.Locale.forLanguageTag("en-Latn");

    private int computeFirstMatchIndex(java.util.Collection<java.lang.String> supportedLocales, boolean assumeEnglishIsSupported) {
        if (mList.length == 1) {
            // just one locale, perhaps the most common scenario
            return 0;
        }
        if (mList.length == 0) {
            // empty locale list
            return -1;
        }
        int bestIndex = java.lang.Integer.MAX_VALUE;
        // Try English first, so we can return early if it's in the LocaleList
        if (assumeEnglishIsSupported) {
            final int idx = findFirstMatchIndex(android.os.LocaleList.EN_LATN);
            if (idx == 0) {
                // We have a match on the first locale, which is good enough
                return 0;
            } else
                if (idx < bestIndex) {
                    bestIndex = idx;
                }

        }
        for (java.lang.String languageTag : supportedLocales) {
            final java.util.Locale supportedLocale = java.util.Locale.forLanguageTag(languageTag);
            // We expect the average length of locale lists used for locale resolution to be
            // smaller than three, so it's OK to do this as an O(mn) algorithm.
            final int idx = findFirstMatchIndex(supportedLocale);
            if (idx == 0) {
                // We have a match on the first locale, which is good enough
                return 0;
            } else
                if (idx < bestIndex) {
                    bestIndex = idx;
                }

        }
        if (bestIndex == java.lang.Integer.MAX_VALUE) {
            // no match was found, so we fall back to the first locale in the locale list
            return 0;
        } else {
            return bestIndex;
        }
    }

    private java.util.Locale computeFirstMatch(java.util.Collection<java.lang.String> supportedLocales, boolean assumeEnglishIsSupported) {
        int bestIndex = computeFirstMatchIndex(supportedLocales, assumeEnglishIsSupported);
        return bestIndex == (-1) ? null : mList[bestIndex];
    }

    /**
     * Returns the first match in the locale list given an unordered array of supported locales
     * in BCP 47 format.
     *
     * @return The first {@link Locale} from this list that appears in the given array, or
    {@code null} if the {@link LocaleList} is empty.
     */
    @android.annotation.Nullable
    public java.util.Locale getFirstMatch(java.lang.String[] supportedLocales) {
        return /* assume English is not supported */
        computeFirstMatch(java.util.Arrays.asList(supportedLocales), false);
    }

    /**
     * {@hide }
     */
    public int getFirstMatchIndex(java.lang.String[] supportedLocales) {
        return /* assume English is not supported */
        computeFirstMatchIndex(java.util.Arrays.asList(supportedLocales), false);
    }

    /**
     * Same as getFirstMatch(), but with English assumed to be supported, even if it's not.
     * {@hide }
     */
    @android.annotation.Nullable
    public java.util.Locale getFirstMatchWithEnglishSupported(java.lang.String[] supportedLocales) {
        return /* assume English is supported */
        computeFirstMatch(java.util.Arrays.asList(supportedLocales), true);
    }

    /**
     * {@hide }
     */
    public int getFirstMatchIndexWithEnglishSupported(java.util.Collection<java.lang.String> supportedLocales) {
        return /* assume English is supported */
        computeFirstMatchIndex(supportedLocales, true);
    }

    /**
     * {@hide }
     */
    public int getFirstMatchIndexWithEnglishSupported(java.lang.String[] supportedLocales) {
        return getFirstMatchIndexWithEnglishSupported(java.util.Arrays.asList(supportedLocales));
    }

    /**
     * Returns true if the collection of locale tags only contains empty locales and pseudolocales.
     * Assumes that there is no repetition in the input.
     * {@hide }
     */
    public static boolean isPseudoLocalesOnly(@android.annotation.Nullable
    java.lang.String[] supportedLocales) {
        if (supportedLocales == null) {
            return true;
        }
        if (supportedLocales.length > (android.os.LocaleList.NUM_PSEUDO_LOCALES + 1)) {
            // This is for optimization. Since there's no repetition in the input, if we have more
            // than the number of pseudo-locales plus one for the empty string, it's guaranteed
            // that we have some meaninful locale in the collection, so the list is not "practically
            // empty".
            return false;
        }
        for (java.lang.String locale : supportedLocales) {
            if ((!locale.isEmpty()) && (!android.os.LocaleList.isPseudoLocale(locale))) {
                return false;
            }
        }
        return true;
    }

    private static final java.lang.Object sLock = new java.lang.Object();

    @com.android.internal.annotations.GuardedBy("sLock")
    private static android.os.LocaleList sLastExplicitlySetLocaleList = null;

    @com.android.internal.annotations.GuardedBy("sLock")
    private static android.os.LocaleList sDefaultLocaleList = null;

    @com.android.internal.annotations.GuardedBy("sLock")
    private static android.os.LocaleList sDefaultAdjustedLocaleList = null;

    @com.android.internal.annotations.GuardedBy("sLock")
    private static java.util.Locale sLastDefaultLocale = null;

    /**
     * The result is guaranteed to include the default Locale returned by Locale.getDefault(), but
     * not necessarily at the top of the list. The default locale not being at the top of the list
     * is an indication that the system has set the default locale to one of the user's other
     * preferred locales, having concluded that the primary preference is not supported but a
     * secondary preference is.
     *
     * <p>Note that the default LocaleList would change if Locale.setDefault() is called. This
     * method takes that into account by always checking the output of Locale.getDefault() and
     * recalculating the default LocaleList if needed.</p>
     */
    @android.annotation.NonNull
    @android.annotation.Size(min = 1)
    public static android.os.LocaleList getDefault() {
        final java.util.Locale defaultLocale = java.util.Locale.getDefault();
        synchronized(android.os.LocaleList.sLock) {
            if (!defaultLocale.equals(android.os.LocaleList.sLastDefaultLocale)) {
                android.os.LocaleList.sLastDefaultLocale = defaultLocale;
                // It's either the first time someone has asked for the default locale list, or
                // someone has called Locale.setDefault() since we last set or adjusted the default
                // locale list. So let's recalculate the locale list.
                if ((android.os.LocaleList.sDefaultLocaleList != null) && defaultLocale.equals(android.os.LocaleList.sDefaultLocaleList.get(0))) {
                    // The default Locale has changed, but it happens to be the first locale in the
                    // default locale list, so we don't need to construct a new locale list.
                    return android.os.LocaleList.sDefaultLocaleList;
                }
                android.os.LocaleList.sDefaultLocaleList = new android.os.LocaleList(defaultLocale, android.os.LocaleList.sLastExplicitlySetLocaleList);
                android.os.LocaleList.sDefaultAdjustedLocaleList = android.os.LocaleList.sDefaultLocaleList;
            }
            // sDefaultLocaleList can't be null, since it can't be set to null by
            // LocaleList.setDefault(), and if getDefault() is called before a call to
            // setDefault(), sLastDefaultLocale would be null and the check above would set
            // sDefaultLocaleList.
            return android.os.LocaleList.sDefaultLocaleList;
        }
    }

    /**
     * Returns the default locale list, adjusted by moving the default locale to its first
     * position.
     */
    @android.annotation.NonNull
    @android.annotation.Size(min = 1)
    public static android.os.LocaleList getAdjustedDefault() {
        android.os.LocaleList.getDefault();// to recalculate the default locale list, if necessary

        synchronized(android.os.LocaleList.sLock) {
            return android.os.LocaleList.sDefaultAdjustedLocaleList;
        }
    }

    /**
     * Also sets the default locale by calling Locale.setDefault() with the first locale in the
     * list.
     *
     * @throws NullPointerException
     * 		if the input is <code>null</code>.
     * @throws IllegalArgumentException
     * 		if the input is empty.
     */
    public static void setDefault(@android.annotation.NonNull
    @android.annotation.Size(min = 1)
    android.os.LocaleList locales) {
        android.os.LocaleList.setDefault(locales, 0);
    }

    /**
     * This may be used directly by system processes to set the default locale list for apps. For
     * such uses, the default locale list would always come from the user preferences, but the
     * default locale may have been chosen to be a locale other than the first locale in the locale
     * list (based on the locales the app supports).
     *
     * {@hide }
     */
    public static void setDefault(@android.annotation.NonNull
    @android.annotation.Size(min = 1)
    android.os.LocaleList locales, int localeIndex) {
        if (locales == null) {
            throw new java.lang.NullPointerException("locales is null");
        }
        if (locales.isEmpty()) {
            throw new java.lang.IllegalArgumentException("locales is empty");
        }
        synchronized(android.os.LocaleList.sLock) {
            android.os.LocaleList.sLastDefaultLocale = locales.get(localeIndex);
            java.util.Locale.setDefault(android.os.LocaleList.sLastDefaultLocale);
            android.os.LocaleList.sLastExplicitlySetLocaleList = locales;
            android.os.LocaleList.sDefaultLocaleList = locales;
            if (localeIndex == 0) {
                android.os.LocaleList.sDefaultAdjustedLocaleList = android.os.LocaleList.sDefaultLocaleList;
            } else {
                android.os.LocaleList.sDefaultAdjustedLocaleList = new android.os.LocaleList(android.os.LocaleList.sLastDefaultLocale, android.os.LocaleList.sDefaultLocaleList);
            }
        }
    }
}

