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
package android.view.textservice;


/**
 * This class is used to specify meta information of a subtype contained in a spell checker.
 * Subtype can describe locale (e.g. en_US, fr_FR...) used for settings.
 *
 * @see SpellCheckerInfo
 * @unknown ref android.R.styleable#SpellChecker_Subtype_label
 * @unknown ref android.R.styleable#SpellChecker_Subtype_languageTag
 * @unknown ref android.R.styleable#SpellChecker_Subtype_subtypeLocale
 * @unknown ref android.R.styleable#SpellChecker_Subtype_subtypeExtraValue
 * @unknown ref android.R.styleable#SpellChecker_Subtype_subtypeId
 */
public final class SpellCheckerSubtype implements android.os.Parcelable {
    private static final java.lang.String TAG = android.view.textservice.SpellCheckerSubtype.class.getSimpleName();

    private static final java.lang.String EXTRA_VALUE_PAIR_SEPARATOR = ",";

    private static final java.lang.String EXTRA_VALUE_KEY_VALUE_SEPARATOR = "=";

    /**
     *
     *
     * @unknown 
     */
    public static final int SUBTYPE_ID_NONE = 0;

    private static final java.lang.String SUBTYPE_LANGUAGE_TAG_NONE = "";

    private final int mSubtypeId;

    private final int mSubtypeHashCode;

    private final int mSubtypeNameResId;

    private final java.lang.String mSubtypeLocale;

    private final java.lang.String mSubtypeLanguageTag;

    private final java.lang.String mSubtypeExtraValue;

    private java.util.HashMap<java.lang.String, java.lang.String> mExtraValueHashMapCache;

    /**
     * Constructor.
     *
     * <p>There is no public API that requires developers to instantiate custom
     * {@link SpellCheckerSubtype} object.  Hence so far there is no need to make this constructor
     * available in public API.</p>
     *
     * @param nameId
     * 		The name of the subtype
     * @param locale
     * 		The locale supported by the subtype
     * @param languageTag
     * 		The BCP-47 Language Tag associated with this subtype.
     * @param extraValue
     * 		The extra value of the subtype
     * @param subtypeId
     * 		The subtype ID that is supposed to be stable during package update.
     * @unknown 
     */
    public SpellCheckerSubtype(int nameId, java.lang.String locale, java.lang.String languageTag, java.lang.String extraValue, int subtypeId) {
        mSubtypeNameResId = nameId;
        mSubtypeLocale = (locale != null) ? locale : "";
        mSubtypeLanguageTag = (languageTag != null) ? languageTag : android.view.textservice.SpellCheckerSubtype.SUBTYPE_LANGUAGE_TAG_NONE;
        mSubtypeExtraValue = (extraValue != null) ? extraValue : "";
        mSubtypeId = subtypeId;
        mSubtypeHashCode = (mSubtypeId != android.view.textservice.SpellCheckerSubtype.SUBTYPE_ID_NONE) ? mSubtypeId : android.view.textservice.SpellCheckerSubtype.hashCodeInternal(mSubtypeLocale, mSubtypeExtraValue);
    }

    /**
     * Constructor.
     *
     * @param nameId
     * 		The name of the subtype
     * @param locale
     * 		The locale supported by the subtype
     * @param extraValue
     * 		The extra value of the subtype
     * @deprecated There is no public API that requires developers to directly instantiate custom
    {@link SpellCheckerSubtype} objects right now.  Hence only the system is expected to be able
    to instantiate {@link SpellCheckerSubtype} object.
     */
    @java.lang.Deprecated
    public SpellCheckerSubtype(int nameId, java.lang.String locale, java.lang.String extraValue) {
        this(nameId, locale, android.view.textservice.SpellCheckerSubtype.SUBTYPE_LANGUAGE_TAG_NONE, extraValue, android.view.textservice.SpellCheckerSubtype.SUBTYPE_ID_NONE);
    }

    SpellCheckerSubtype(android.os.Parcel source) {
        java.lang.String s;
        mSubtypeNameResId = source.readInt();
        s = source.readString();
        mSubtypeLocale = (s != null) ? s : "";
        s = source.readString();
        mSubtypeLanguageTag = (s != null) ? s : "";
        s = source.readString();
        mSubtypeExtraValue = (s != null) ? s : "";
        mSubtypeId = source.readInt();
        mSubtypeHashCode = (mSubtypeId != android.view.textservice.SpellCheckerSubtype.SUBTYPE_ID_NONE) ? mSubtypeId : android.view.textservice.SpellCheckerSubtype.hashCodeInternal(mSubtypeLocale, mSubtypeExtraValue);
    }

    /**
     *
     *
     * @return the name of the subtype
     */
    public int getNameResId() {
        return mSubtypeNameResId;
    }

    /**
     *
     *
     * @return the locale of the subtype
     * @deprecated Use {@link #getLanguageTag()} instead.
     */
    @java.lang.Deprecated
    @android.annotation.NonNull
    public java.lang.String getLocale() {
        return mSubtypeLocale;
    }

    /**
     *
     *
     * @return the BCP-47 Language Tag of the subtype.  Returns an empty string when no Language Tag
    is specified.
     * @see Locale#forLanguageTag(String)
     */
    @android.annotation.NonNull
    public java.lang.String getLanguageTag() {
        return mSubtypeLanguageTag;
    }

    /**
     *
     *
     * @return the extra value of the subtype
     */
    public java.lang.String getExtraValue() {
        return mSubtypeExtraValue;
    }

    private java.util.HashMap<java.lang.String, java.lang.String> getExtraValueHashMap() {
        if (mExtraValueHashMapCache == null) {
            mExtraValueHashMapCache = new java.util.HashMap<java.lang.String, java.lang.String>();
            final java.lang.String[] pairs = mSubtypeExtraValue.split(android.view.textservice.SpellCheckerSubtype.EXTRA_VALUE_PAIR_SEPARATOR);
            final int N = pairs.length;
            for (int i = 0; i < N; ++i) {
                final java.lang.String[] pair = pairs[i].split(android.view.textservice.SpellCheckerSubtype.EXTRA_VALUE_KEY_VALUE_SEPARATOR);
                if (pair.length == 1) {
                    mExtraValueHashMapCache.put(pair[0], null);
                } else
                    if (pair.length > 1) {
                        if (pair.length > 2) {
                            android.util.Slog.w(android.view.textservice.SpellCheckerSubtype.TAG, "ExtraValue has two or more '='s");
                        }
                        mExtraValueHashMapCache.put(pair[0], pair[1]);
                    }

            }
        }
        return mExtraValueHashMapCache;
    }

    /**
     * The string of ExtraValue in subtype should be defined as follows:
     * example: key0,key1=value1,key2,key3,key4=value4
     *
     * @param key
     * 		the key of extra value
     * @return the subtype contains specified the extra value
     */
    public boolean containsExtraValueKey(java.lang.String key) {
        return getExtraValueHashMap().containsKey(key);
    }

    /**
     * The string of ExtraValue in subtype should be defined as follows:
     * example: key0,key1=value1,key2,key3,key4=value4
     *
     * @param key
     * 		the key of extra value
     * @return the value of the specified key
     */
    public java.lang.String getExtraValueOf(java.lang.String key) {
        return getExtraValueHashMap().get(key);
    }

    @java.lang.Override
    public int hashCode() {
        return mSubtypeHashCode;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o instanceof android.view.textservice.SpellCheckerSubtype) {
            android.view.textservice.SpellCheckerSubtype subtype = ((android.view.textservice.SpellCheckerSubtype) (o));
            if ((subtype.mSubtypeId != android.view.textservice.SpellCheckerSubtype.SUBTYPE_ID_NONE) || (mSubtypeId != android.view.textservice.SpellCheckerSubtype.SUBTYPE_ID_NONE)) {
                return subtype.hashCode() == hashCode();
            }
            return ((((subtype.hashCode() == hashCode()) && (subtype.getNameResId() == getNameResId())) && subtype.getLocale().equals(getLocale())) && subtype.getLanguageTag().equals(getLanguageTag())) && subtype.getExtraValue().equals(getExtraValue());
        }
        return false;
    }

    /**
     *
     *
     * @return {@link Locale} constructed from {@link #getLanguageTag()}. If the Language Tag is not
    specified, then try to construct from {@link #getLocale()}

    <p>TODO: Consider to make this a public API, or move this to support lib.</p>
     * @unknown 
     */
    @android.annotation.Nullable
    public java.util.Locale getLocaleObject() {
        if (!android.text.TextUtils.isEmpty(mSubtypeLanguageTag)) {
            return java.util.Locale.forLanguageTag(mSubtypeLanguageTag);
        }
        return com.android.internal.inputmethod.SubtypeLocaleUtils.constructLocaleFromString(mSubtypeLocale);
    }

    /**
     *
     *
     * @param context
     * 		Context will be used for getting Locale and PackageManager.
     * @param packageName
     * 		The package name of the spell checker
     * @param appInfo
     * 		The application info of the spell checker
     * @return a display name for this subtype. The string resource of the label (mSubtypeNameResId)
    can have only one %s in it. If there is, the %s part will be replaced with the locale's
    display name by the formatter. If there is not, this method simply returns the string
    specified by mSubtypeNameResId. If mSubtypeNameResId is not specified (== 0), it's up to the
    framework to generate an appropriate display name.
     */
    public java.lang.CharSequence getDisplayName(android.content.Context context, java.lang.String packageName, android.content.pm.ApplicationInfo appInfo) {
        final java.util.Locale locale = getLocaleObject();
        final java.lang.String localeStr = (locale != null) ? locale.getDisplayName() : mSubtypeLocale;
        if (mSubtypeNameResId == 0) {
            return localeStr;
        }
        final java.lang.CharSequence subtypeName = context.getPackageManager().getText(packageName, mSubtypeNameResId, appInfo);
        if (!android.text.TextUtils.isEmpty(subtypeName)) {
            return java.lang.String.format(subtypeName.toString(), localeStr);
        } else {
            return localeStr;
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        dest.writeInt(mSubtypeNameResId);
        dest.writeString(mSubtypeLocale);
        dest.writeString(mSubtypeLanguageTag);
        dest.writeString(mSubtypeExtraValue);
        dest.writeInt(mSubtypeId);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.textservice.SpellCheckerSubtype> CREATOR = new android.os.Parcelable.Creator<android.view.textservice.SpellCheckerSubtype>() {
        @java.lang.Override
        public android.view.textservice.SpellCheckerSubtype createFromParcel(android.os.Parcel source) {
            return new android.view.textservice.SpellCheckerSubtype(source);
        }

        @java.lang.Override
        public android.view.textservice.SpellCheckerSubtype[] newArray(int size) {
            return new android.view.textservice.SpellCheckerSubtype[size];
        }
    };

    private static int hashCodeInternal(java.lang.String locale, java.lang.String extraValue) {
        return java.util.Arrays.hashCode(new java.lang.Object[]{ locale, extraValue });
    }

    /**
     * Sort the list of subtypes
     *
     * @param context
     * 		Context will be used for getting localized strings
     * @param flags
     * 		Flags for the sort order
     * @param sci
     * 		SpellCheckerInfo of which subtypes are subject to be sorted
     * @param subtypeList
     * 		List which will be sorted
     * @return Sorted list of subtypes
     * @unknown 
     */
    public static java.util.List<android.view.textservice.SpellCheckerSubtype> sort(android.content.Context context, int flags, android.view.textservice.SpellCheckerInfo sci, java.util.List<android.view.textservice.SpellCheckerSubtype> subtypeList) {
        if (sci == null)
            return subtypeList;

        final java.util.HashSet<android.view.textservice.SpellCheckerSubtype> subtypesSet = new java.util.HashSet<android.view.textservice.SpellCheckerSubtype>(subtypeList);
        final java.util.ArrayList<android.view.textservice.SpellCheckerSubtype> sortedList = new java.util.ArrayList<android.view.textservice.SpellCheckerSubtype>();
        int N = sci.getSubtypeCount();
        for (int i = 0; i < N; ++i) {
            android.view.textservice.SpellCheckerSubtype subtype = sci.getSubtypeAt(i);
            if (subtypesSet.contains(subtype)) {
                sortedList.add(subtype);
                subtypesSet.remove(subtype);
            }
        }
        // If subtypes in subtypesSet remain, that means these subtypes are not
        // contained in sci, so the remaining subtypes will be appended.
        for (android.view.textservice.SpellCheckerSubtype subtype : subtypesSet) {
            sortedList.add(subtype);
        }
        return sortedList;
    }
}

