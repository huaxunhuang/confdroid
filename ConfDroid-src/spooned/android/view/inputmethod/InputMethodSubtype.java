/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.view.inputmethod;


/**
 * This class is used to specify meta information of a subtype contained in an input method editor
 * (IME). Subtype can describe locale (e.g. en_US, fr_FR...) and mode (e.g. voice, keyboard...),
 * and is used for IME switch and settings. The input method subtype allows the system to bring up
 * the specified subtype of the designated IME directly.
 *
 * <p>It should be defined in an XML resource file of the input method with the
 * <code>&lt;subtype&gt;</code> element, which resides within an {@code <input-method>} element.
 * For more information, see the guide to
 * <a href="{@docRoot }guide/topics/text/creating-input-method.html">
 * Creating an Input Method</a>.</p>
 *
 * @see InputMethodInfo
 * @unknown ref android.R.styleable#InputMethod_Subtype_label
 * @unknown ref android.R.styleable#InputMethod_Subtype_icon
 * @unknown ref android.R.styleable#InputMethod_Subtype_languageTag
 * @unknown ref android.R.styleable#InputMethod_Subtype_imeSubtypeLocale
 * @unknown ref android.R.styleable#InputMethod_Subtype_imeSubtypeMode
 * @unknown ref android.R.styleable#InputMethod_Subtype_imeSubtypeExtraValue
 * @unknown ref android.R.styleable#InputMethod_Subtype_isAuxiliary
 * @unknown ref android.R.styleable#InputMethod_Subtype_overridesImplicitlyEnabledSubtype
 * @unknown ref android.R.styleable#InputMethod_Subtype_subtypeId
 * @unknown ref android.R.styleable#InputMethod_Subtype_isAsciiCapable
 */
public final class InputMethodSubtype implements android.os.Parcelable {
    private static final java.lang.String TAG = android.view.inputmethod.InputMethodSubtype.class.getSimpleName();

    private static final java.lang.String LANGUAGE_TAG_NONE = "";

    private static final java.lang.String EXTRA_VALUE_PAIR_SEPARATOR = ",";

    private static final java.lang.String EXTRA_VALUE_KEY_VALUE_SEPARATOR = "=";

    // TODO: remove this
    private static final java.lang.String EXTRA_KEY_UNTRANSLATABLE_STRING_IN_SUBTYPE_NAME = "UntranslatableReplacementStringInSubtypeName";

    private static final int SUBTYPE_ID_NONE = 0;

    private final boolean mIsAuxiliary;

    private final boolean mOverridesImplicitlyEnabledSubtype;

    private final boolean mIsAsciiCapable;

    private final int mSubtypeHashCode;

    private final int mSubtypeIconResId;

    private final int mSubtypeNameResId;

    private final int mSubtypeId;

    private final java.lang.String mSubtypeLocale;

    private final java.lang.String mSubtypeLanguageTag;

    private final java.lang.String mSubtypeMode;

    private final java.lang.String mSubtypeExtraValue;

    private final java.lang.Object mLock = new java.lang.Object();

    private volatile java.util.Locale mCachedLocaleObj;

    private volatile java.util.HashMap<java.lang.String, java.lang.String> mExtraValueHashMapCache;

    /**
     * InputMethodSubtypeBuilder is a builder class of InputMethodSubtype.
     * This class is designed to be used with
     * {@link android.view.inputmethod.InputMethodManager#setAdditionalInputMethodSubtypes}.
     * The developer needs to be aware of what each parameter means.
     */
    public static class InputMethodSubtypeBuilder {
        /**
         *
         *
         * @param isAuxiliary
         * 		should true when this subtype is auxiliary, false otherwise.
         * 		An auxiliary subtype has the following differences with a regular subtype:
         * 		- An auxiliary subtype cannot be chosen as the default IME in Settings.
         * 		- The framework will never switch to this subtype through
         * 		{@link android.view.inputmethod.InputMethodManager#switchToLastInputMethod}.
         * 		Note that the subtype will still be available in the IME switcher.
         * 		The intent is to allow for IMEs to specify they are meant to be invoked temporarily
         * 		in a one-shot way, and to return to the previous IME once finished (e.g. voice input).
         */
        public android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder setIsAuxiliary(boolean isAuxiliary) {
            mIsAuxiliary = isAuxiliary;
            return this;
        }

        private boolean mIsAuxiliary = false;

        /**
         *
         *
         * @param overridesImplicitlyEnabledSubtype
         * 		should be true if this subtype should be
         * 		enabled by default if no other subtypes in the IME are enabled explicitly. Note that a
         * 		subtype with this parameter set will not be shown in the list of subtypes in each IME's
         * 		subtype enabler. A canonical use of this would be for an IME to supply an "automatic"
         * 		subtype that adapts to the current system language.
         */
        public android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder setOverridesImplicitlyEnabledSubtype(boolean overridesImplicitlyEnabledSubtype) {
            mOverridesImplicitlyEnabledSubtype = overridesImplicitlyEnabledSubtype;
            return this;
        }

        private boolean mOverridesImplicitlyEnabledSubtype = false;

        /**
         *
         *
         * @param isAsciiCapable
         * 		should be true if this subtype is ASCII capable. If the subtype
         * 		is ASCII capable, it should guarantee that the user can input ASCII characters with
         * 		this subtype. This is important because many password fields only allow
         * 		ASCII-characters.
         */
        public android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder setIsAsciiCapable(boolean isAsciiCapable) {
            mIsAsciiCapable = isAsciiCapable;
            return this;
        }

        private boolean mIsAsciiCapable = false;

        /**
         *
         *
         * @param subtypeIconResId
         * 		is a resource ID of the subtype icon drawable.
         */
        public android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder setSubtypeIconResId(int subtypeIconResId) {
            mSubtypeIconResId = subtypeIconResId;
            return this;
        }

        private int mSubtypeIconResId = 0;

        /**
         *
         *
         * @param subtypeNameResId
         * 		is the resource ID of the subtype name string.
         * 		The string resource may have exactly one %s in it. If present,
         * 		the %s part will be replaced with the locale's display name by
         * 		the formatter. Please refer to {@link #getDisplayName} for details.
         */
        public android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder setSubtypeNameResId(int subtypeNameResId) {
            mSubtypeNameResId = subtypeNameResId;
            return this;
        }

        private int mSubtypeNameResId = 0;

        /**
         *
         *
         * @param subtypeId
         * 		is the unique ID for this subtype. The input method framework keeps
         * 		track of enabled subtypes by ID. When the IME package gets upgraded, enabled IDs will
         * 		stay enabled even if other attributes are different. If the ID is unspecified or 0,
         * 		Arrays.hashCode(new Object[] {locale, mode, extraValue,
         * 		isAuxiliary, overridesImplicitlyEnabledSubtype, isAsciiCapable}) will be used instead.
         */
        public android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder setSubtypeId(int subtypeId) {
            mSubtypeId = subtypeId;
            return this;
        }

        private int mSubtypeId = android.view.inputmethod.InputMethodSubtype.SUBTYPE_ID_NONE;

        /**
         *
         *
         * @param subtypeLocale
         * 		is the locale supported by this subtype.
         */
        public android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder setSubtypeLocale(java.lang.String subtypeLocale) {
            mSubtypeLocale = (subtypeLocale == null) ? "" : subtypeLocale;
            return this;
        }

        private java.lang.String mSubtypeLocale = "";

        /**
         *
         *
         * @param languageTag
         * 		is the BCP-47 Language Tag supported by this subtype.
         */
        public android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder setLanguageTag(java.lang.String languageTag) {
            mSubtypeLanguageTag = (languageTag == null) ? android.view.inputmethod.InputMethodSubtype.LANGUAGE_TAG_NONE : languageTag;
            return this;
        }

        private java.lang.String mSubtypeLanguageTag = android.view.inputmethod.InputMethodSubtype.LANGUAGE_TAG_NONE;

        /**
         *
         *
         * @param subtypeMode
         * 		is the mode supported by this subtype.
         */
        public android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder setSubtypeMode(java.lang.String subtypeMode) {
            mSubtypeMode = (subtypeMode == null) ? "" : subtypeMode;
            return this;
        }

        private java.lang.String mSubtypeMode = "";

        /**
         *
         *
         * @param subtypeExtraValue
         * 		is the extra value of the subtype. This string is free-form,
         * 		but the API supplies tools to deal with a key-value comma-separated list; see
         * 		{@link #containsExtraValueKey} and {@link #getExtraValueOf}.
         */
        public android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder setSubtypeExtraValue(java.lang.String subtypeExtraValue) {
            mSubtypeExtraValue = (subtypeExtraValue == null) ? "" : subtypeExtraValue;
            return this;
        }

        private java.lang.String mSubtypeExtraValue = "";

        /**
         *
         *
         * @return InputMethodSubtype using parameters in this InputMethodSubtypeBuilder.
         */
        public android.view.inputmethod.InputMethodSubtype build() {
            return new android.view.inputmethod.InputMethodSubtype(this);
        }
    }

    private static android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder getBuilder(int nameId, int iconId, java.lang.String locale, java.lang.String mode, java.lang.String extraValue, boolean isAuxiliary, boolean overridesImplicitlyEnabledSubtype, int id, boolean isAsciiCapable) {
        final android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder builder = new android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder();
        builder.mSubtypeNameResId = nameId;
        builder.mSubtypeIconResId = iconId;
        builder.mSubtypeLocale = locale;
        builder.mSubtypeMode = mode;
        builder.mSubtypeExtraValue = extraValue;
        builder.mIsAuxiliary = isAuxiliary;
        builder.mOverridesImplicitlyEnabledSubtype = overridesImplicitlyEnabledSubtype;
        builder.mSubtypeId = id;
        builder.mIsAsciiCapable = isAsciiCapable;
        return builder;
    }

    /**
     * Constructor with no subtype ID specified.
     *
     * @deprecated use {@link InputMethodSubtypeBuilder} instead.
    Arguments for this constructor have the same meanings as
    {@link InputMethodSubtype#InputMethodSubtype(int, int, String, String, String, boolean,
    boolean, int)} except "id".
     */
    @java.lang.Deprecated
    public InputMethodSubtype(int nameId, int iconId, java.lang.String locale, java.lang.String mode, java.lang.String extraValue, boolean isAuxiliary, boolean overridesImplicitlyEnabledSubtype) {
        this(nameId, iconId, locale, mode, extraValue, isAuxiliary, overridesImplicitlyEnabledSubtype, 0);
    }

    /**
     * Constructor.
     *
     * @deprecated use {@link InputMethodSubtypeBuilder} instead.
    "isAsciiCapable" is "false" in this constructor.
     * @param nameId
     * 		Resource ID of the subtype name string. The string resource may have exactly
     * 		one %s in it. If there is, the %s part will be replaced with the locale's display name by
     * 		the formatter. Please refer to {@link #getDisplayName} for details.
     * @param iconId
     * 		Resource ID of the subtype icon drawable.
     * @param locale
     * 		The locale supported by the subtype
     * @param mode
     * 		The mode supported by the subtype
     * @param extraValue
     * 		The extra value of the subtype. This string is free-form, but the API
     * 		supplies tools to deal with a key-value comma-separated list; see
     * 		{@link #containsExtraValueKey} and {@link #getExtraValueOf}.
     * @param isAuxiliary
     * 		true when this subtype is auxiliary, false otherwise. An auxiliary
     * 		subtype will not be shown in the list of enabled IMEs for choosing the current IME in
     * 		the Settings even when this subtype is enabled. Please note that this subtype will still
     * 		be shown in the list of IMEs in the IME switcher to allow the user to tentatively switch
     * 		to this subtype while an IME is shown. The framework will never switch the current IME to
     * 		this subtype by {@link android.view.inputmethod.InputMethodManager#switchToLastInputMethod}.
     * 		The intent of having this flag is to allow for IMEs that are invoked in a one-shot way as
     * 		auxiliary input mode, and return to the previous IME once it is finished (e.g. voice input).
     * @param overridesImplicitlyEnabledSubtype
     * 		true when this subtype should be enabled by default
     * 		if no other subtypes in the IME are enabled explicitly. Note that a subtype with this
     * 		parameter being true will not be shown in the list of subtypes in each IME's subtype enabler.
     * 		Having an "automatic" subtype is an example use of this flag.
     * @param id
     * 		The unique ID for the subtype. The input method framework keeps track of enabled
     * 		subtypes by ID. When the IME package gets upgraded, enabled IDs will stay enabled even if
     * 		other attributes are different. If the ID is unspecified or 0,
     * 		Arrays.hashCode(new Object[] {locale, mode, extraValue,
     * 		isAuxiliary, overridesImplicitlyEnabledSubtype, isAsciiCapable}) will be used instead.
     */
    @java.lang.Deprecated
    public InputMethodSubtype(int nameId, int iconId, java.lang.String locale, java.lang.String mode, java.lang.String extraValue, boolean isAuxiliary, boolean overridesImplicitlyEnabledSubtype, int id) {
        this(android.view.inputmethod.InputMethodSubtype.getBuilder(nameId, iconId, locale, mode, extraValue, isAuxiliary, overridesImplicitlyEnabledSubtype, id, false));
    }

    /**
     * Constructor.
     *
     * @param builder
     * 		Builder for InputMethodSubtype
     */
    private InputMethodSubtype(android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder builder) {
        mSubtypeNameResId = builder.mSubtypeNameResId;
        mSubtypeIconResId = builder.mSubtypeIconResId;
        mSubtypeLocale = builder.mSubtypeLocale;
        mSubtypeLanguageTag = builder.mSubtypeLanguageTag;
        mSubtypeMode = builder.mSubtypeMode;
        mSubtypeExtraValue = builder.mSubtypeExtraValue;
        mIsAuxiliary = builder.mIsAuxiliary;
        mOverridesImplicitlyEnabledSubtype = builder.mOverridesImplicitlyEnabledSubtype;
        mSubtypeId = builder.mSubtypeId;
        mIsAsciiCapable = builder.mIsAsciiCapable;
        // If hashCode() of this subtype is 0 and you want to specify it as an id of this subtype,
        // just specify 0 as this subtype's id. Then, this subtype's id is treated as 0.
        if (mSubtypeId != android.view.inputmethod.InputMethodSubtype.SUBTYPE_ID_NONE) {
            mSubtypeHashCode = mSubtypeId;
        } else {
            mSubtypeHashCode = android.view.inputmethod.InputMethodSubtype.hashCodeInternal(mSubtypeLocale, mSubtypeMode, mSubtypeExtraValue, mIsAuxiliary, mOverridesImplicitlyEnabledSubtype, mIsAsciiCapable);
        }
    }

    InputMethodSubtype(android.os.Parcel source) {
        java.lang.String s;
        mSubtypeNameResId = source.readInt();
        mSubtypeIconResId = source.readInt();
        s = source.readString();
        mSubtypeLocale = (s != null) ? s : "";
        s = source.readString();
        mSubtypeLanguageTag = (s != null) ? s : android.view.inputmethod.InputMethodSubtype.LANGUAGE_TAG_NONE;
        s = source.readString();
        mSubtypeMode = (s != null) ? s : "";
        s = source.readString();
        mSubtypeExtraValue = (s != null) ? s : "";
        mIsAuxiliary = source.readInt() == 1;
        mOverridesImplicitlyEnabledSubtype = source.readInt() == 1;
        mSubtypeHashCode = source.readInt();
        mSubtypeId = source.readInt();
        mIsAsciiCapable = source.readInt() == 1;
    }

    /**
     *
     *
     * @return Resource ID of the subtype name string.
     */
    public int getNameResId() {
        return mSubtypeNameResId;
    }

    /**
     *
     *
     * @return Resource ID of the subtype icon drawable.
     */
    public int getIconResId() {
        return mSubtypeIconResId;
    }

    /**
     *
     *
     * @return The locale of the subtype. This method returns the "locale" string parameter passed
    to the constructor.
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
     * @return {@link Locale} constructed from {@link #getLanguageTag()}. If the Language Tag is not
    specified, then try to construct from {@link #getLocale()}

    <p>TODO: Consider to make this a public API, or move this to support lib.</p>
     * @unknown 
     */
    @android.annotation.Nullable
    public java.util.Locale getLocaleObject() {
        if (mCachedLocaleObj != null) {
            return mCachedLocaleObj;
        }
        synchronized(mLock) {
            if (mCachedLocaleObj != null) {
                return mCachedLocaleObj;
            }
            if (!android.text.TextUtils.isEmpty(mSubtypeLanguageTag)) {
                mCachedLocaleObj = java.util.Locale.forLanguageTag(mSubtypeLanguageTag);
            } else {
                mCachedLocaleObj = com.android.internal.inputmethod.SubtypeLocaleUtils.constructLocaleFromString(mSubtypeLocale);
            }
            return mCachedLocaleObj;
        }
    }

    /**
     *
     *
     * @return The mode of the subtype.
     */
    public java.lang.String getMode() {
        return mSubtypeMode;
    }

    /**
     *
     *
     * @return The extra value of the subtype.
     */
    public java.lang.String getExtraValue() {
        return mSubtypeExtraValue;
    }

    /**
     *
     *
     * @return true if this subtype is auxiliary, false otherwise. An auxiliary subtype will not be
    shown in the list of enabled IMEs for choosing the current IME in the Settings even when this
    subtype is enabled. Please note that this subtype will still be shown in the list of IMEs in
    the IME switcher to allow the user to tentatively switch to this subtype while an IME is
    shown. The framework will never switch the current IME to this subtype by
    {@link android.view.inputmethod.InputMethodManager#switchToLastInputMethod}.
    The intent of having this flag is to allow for IMEs that are invoked in a one-shot way as
    auxiliary input mode, and return to the previous IME once it is finished (e.g. voice input).
     */
    public boolean isAuxiliary() {
        return mIsAuxiliary;
    }

    /**
     *
     *
     * @return true when this subtype will be enabled by default if no other subtypes in the IME
    are enabled explicitly, false otherwise. Note that a subtype with this method returning true
    will not be shown in the list of subtypes in each IME's subtype enabler. Having an
    "automatic" subtype is an example use of this flag.
     */
    public boolean overridesImplicitlyEnabledSubtype() {
        return mOverridesImplicitlyEnabledSubtype;
    }

    /**
     *
     *
     * @return true if this subtype is Ascii capable, false otherwise. If the subtype is ASCII
    capable, it should guarantee that the user can input ASCII characters with this subtype.
    This is important because many password fields only allow ASCII-characters.
     */
    public boolean isAsciiCapable() {
        return mIsAsciiCapable;
    }

    /**
     * Returns a display name for this subtype.
     *
     * <p>If {@code subtypeNameResId} is specified (!= 0) text generated from that resource will
     * be returned. The localized string resource of the label should be capitalized for inclusion
     * in UI lists. The string resource may contain at most one {@code %s}. If present, the
     * {@code %s} will be replaced with the display name of the subtype locale in the user's locale.
     *
     * <p>If {@code subtypeNameResId} is not specified (== 0) the framework returns the display name
     * of the subtype locale, as capitalized for use in UI lists, in the user's locale.
     *
     * @param context
     * 		{@link Context} will be used for getting {@link Locale} and
     * 		{@link android.content.pm.PackageManager}.
     * @param packageName
     * 		The package name of the input method.
     * @param appInfo
     * 		The {@link ApplicationInfo} of the input method.
     * @return a display name for this subtype.
     */
    @android.annotation.NonNull
    public java.lang.CharSequence getDisplayName(android.content.Context context, java.lang.String packageName, android.content.pm.ApplicationInfo appInfo) {
        if (mSubtypeNameResId == 0) {
            return android.view.inputmethod.InputMethodSubtype.getLocaleDisplayName(android.view.inputmethod.InputMethodSubtype.getLocaleFromContext(context), getLocaleObject(), DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU);
        }
        final java.lang.CharSequence subtypeName = context.getPackageManager().getText(packageName, mSubtypeNameResId, appInfo);
        if (android.text.TextUtils.isEmpty(subtypeName)) {
            return "";
        }
        final java.lang.String subtypeNameString = subtypeName.toString();
        java.lang.String replacementString;
        if (containsExtraValueKey(android.view.inputmethod.InputMethodSubtype.EXTRA_KEY_UNTRANSLATABLE_STRING_IN_SUBTYPE_NAME)) {
            replacementString = getExtraValueOf(android.view.inputmethod.InputMethodSubtype.EXTRA_KEY_UNTRANSLATABLE_STRING_IN_SUBTYPE_NAME);
        } else {
            final android.icu.text.DisplayContext displayContext;
            if (android.text.TextUtils.equals(subtypeNameString, "%s")) {
                displayContext = android.icu.text.DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU;
            } else
                if (subtypeNameString.startsWith("%s")) {
                    displayContext = android.icu.text.DisplayContext.CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE;
                } else {
                    displayContext = android.icu.text.DisplayContext.CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE;
                }

            replacementString = android.view.inputmethod.InputMethodSubtype.getLocaleDisplayName(android.view.inputmethod.InputMethodSubtype.getLocaleFromContext(context), getLocaleObject(), displayContext);
        }
        if (replacementString == null) {
            replacementString = "";
        }
        try {
            return java.lang.String.format(subtypeNameString, replacementString);
        } catch (java.util.IllegalFormatException e) {
            android.util.Slog.w(android.view.inputmethod.InputMethodSubtype.TAG, (("Found illegal format in subtype name(" + subtypeName) + "): ") + e);
            return "";
        }
    }

    @android.annotation.Nullable
    private static java.util.Locale getLocaleFromContext(@android.annotation.Nullable
    final android.content.Context context) {
        if (context == null) {
            return null;
        }
        if (context.getResources() == null) {
            return null;
        }
        final android.content.res.Configuration configuration = context.getResources().getConfiguration();
        if (configuration == null) {
            return null;
        }
        return configuration.getLocales().get(0);
    }

    /**
     *
     *
     * @param displayLocale
     * 		{@link Locale} to be used to display {@code localeToDisplay}
     * @param localeToDisplay
     * 		{@link Locale} to be displayed in {@code displayLocale}
     * @param displayContext
     * 		context parameter to be used to display {@code localeToDisplay} in
     * 		{@code displayLocale}
     * @return Returns the name of the {@code localeToDisplay} in the user's current locale.
     */
    @android.annotation.NonNull
    private static java.lang.String getLocaleDisplayName(@android.annotation.Nullable
    java.util.Locale displayLocale, @android.annotation.Nullable
    java.util.Locale localeToDisplay, final android.icu.text.DisplayContext displayContext) {
        if (localeToDisplay == null) {
            return "";
        }
        final java.util.Locale nonNullDisplayLocale = (displayLocale != null) ? displayLocale : java.util.Locale.getDefault();
        return android.icu.text.LocaleDisplayNames.getInstance(nonNullDisplayLocale, displayContext).localeDisplayName(localeToDisplay);
    }

    private java.util.HashMap<java.lang.String, java.lang.String> getExtraValueHashMap() {
        synchronized(this) {
            java.util.HashMap<java.lang.String, java.lang.String> extraValueMap = mExtraValueHashMapCache;
            if (extraValueMap != null) {
                return extraValueMap;
            }
            extraValueMap = new java.util.HashMap<>();
            final java.lang.String[] pairs = mSubtypeExtraValue.split(android.view.inputmethod.InputMethodSubtype.EXTRA_VALUE_PAIR_SEPARATOR);
            for (int i = 0; i < pairs.length; ++i) {
                final java.lang.String[] pair = pairs[i].split(android.view.inputmethod.InputMethodSubtype.EXTRA_VALUE_KEY_VALUE_SEPARATOR);
                if (pair.length == 1) {
                    extraValueMap.put(pair[0], null);
                } else
                    if (pair.length > 1) {
                        if (pair.length > 2) {
                            android.util.Slog.w(android.view.inputmethod.InputMethodSubtype.TAG, "ExtraValue has two or more '='s");
                        }
                        extraValueMap.put(pair[0], pair[1]);
                    }

            }
            mExtraValueHashMapCache = extraValueMap;
            return extraValueMap;
        }
    }

    /**
     * The string of ExtraValue in subtype should be defined as follows:
     * example: key0,key1=value1,key2,key3,key4=value4
     *
     * @param key
     * 		The key of extra value
     * @return The subtype contains specified the extra value
     */
    public boolean containsExtraValueKey(java.lang.String key) {
        return getExtraValueHashMap().containsKey(key);
    }

    /**
     * The string of ExtraValue in subtype should be defined as follows:
     * example: key0,key1=value1,key2,key3,key4=value4
     *
     * @param key
     * 		The key of extra value
     * @return The value of the specified key
     */
    public java.lang.String getExtraValueOf(java.lang.String key) {
        return getExtraValueHashMap().get(key);
    }

    @java.lang.Override
    public int hashCode() {
        return mSubtypeHashCode;
    }

    /**
     *
     *
     * @unknown 
     * @return {@code true} if a valid subtype ID exists.
     */
    public final boolean hasSubtypeId() {
        return mSubtypeId != android.view.inputmethod.InputMethodSubtype.SUBTYPE_ID_NONE;
    }

    /**
     *
     *
     * @unknown 
     * @return subtype ID. {@code 0} means that not subtype ID is specified.
     */
    public final int getSubtypeId() {
        return mSubtypeId;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o instanceof android.view.inputmethod.InputMethodSubtype) {
            android.view.inputmethod.InputMethodSubtype subtype = ((android.view.inputmethod.InputMethodSubtype) (o));
            if ((subtype.mSubtypeId != 0) || (mSubtypeId != 0)) {
                return subtype.hashCode() == hashCode();
            }
            return (((((((subtype.hashCode() == hashCode()) && subtype.getLocale().equals(getLocale())) && subtype.getLanguageTag().equals(getLanguageTag())) && subtype.getMode().equals(getMode())) && subtype.getExtraValue().equals(getExtraValue())) && (subtype.isAuxiliary() == isAuxiliary())) && (subtype.overridesImplicitlyEnabledSubtype() == overridesImplicitlyEnabledSubtype())) && (subtype.isAsciiCapable() == isAsciiCapable());
        }
        return false;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        dest.writeInt(mSubtypeNameResId);
        dest.writeInt(mSubtypeIconResId);
        dest.writeString(mSubtypeLocale);
        dest.writeString(mSubtypeLanguageTag);
        dest.writeString(mSubtypeMode);
        dest.writeString(mSubtypeExtraValue);
        dest.writeInt(mIsAuxiliary ? 1 : 0);
        dest.writeInt(mOverridesImplicitlyEnabledSubtype ? 1 : 0);
        dest.writeInt(mSubtypeHashCode);
        dest.writeInt(mSubtypeId);
        dest.writeInt(mIsAsciiCapable ? 1 : 0);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.inputmethod.InputMethodSubtype> CREATOR = new android.os.Parcelable.Creator<android.view.inputmethod.InputMethodSubtype>() {
        @java.lang.Override
        public android.view.inputmethod.InputMethodSubtype createFromParcel(android.os.Parcel source) {
            return new android.view.inputmethod.InputMethodSubtype(source);
        }

        @java.lang.Override
        public android.view.inputmethod.InputMethodSubtype[] newArray(int size) {
            return new android.view.inputmethod.InputMethodSubtype[size];
        }
    };

    private static int hashCodeInternal(java.lang.String locale, java.lang.String mode, java.lang.String extraValue, boolean isAuxiliary, boolean overridesImplicitlyEnabledSubtype, boolean isAsciiCapable) {
        // CAVEAT: Must revisit how to compute needsToCalculateCompatibleHashCode when a new
        // attribute is added in order to avoid enabled subtypes being unexpectedly disabled.
        final boolean needsToCalculateCompatibleHashCode = !isAsciiCapable;
        if (needsToCalculateCompatibleHashCode) {
            return java.util.Arrays.hashCode(new java.lang.Object[]{ locale, mode, extraValue, isAuxiliary, overridesImplicitlyEnabledSubtype });
        }
        return java.util.Arrays.hashCode(new java.lang.Object[]{ locale, mode, extraValue, isAuxiliary, overridesImplicitlyEnabledSubtype, isAsciiCapable });
    }

    /**
     * Sort the list of InputMethodSubtype
     *
     * @param context
     * 		Context will be used for getting localized strings from IME
     * @param flags
     * 		Flags for the sort order
     * @param imi
     * 		InputMethodInfo of which subtypes are subject to be sorted
     * @param subtypeList
     * 		List of InputMethodSubtype which will be sorted
     * @return Sorted list of subtypes
     * @unknown 
     */
    public static java.util.List<android.view.inputmethod.InputMethodSubtype> sort(android.content.Context context, int flags, android.view.inputmethod.InputMethodInfo imi, java.util.List<android.view.inputmethod.InputMethodSubtype> subtypeList) {
        if (imi == null)
            return subtypeList;

        final java.util.HashSet<android.view.inputmethod.InputMethodSubtype> inputSubtypesSet = new java.util.HashSet<android.view.inputmethod.InputMethodSubtype>(subtypeList);
        final java.util.ArrayList<android.view.inputmethod.InputMethodSubtype> sortedList = new java.util.ArrayList<android.view.inputmethod.InputMethodSubtype>();
        int N = imi.getSubtypeCount();
        for (int i = 0; i < N; ++i) {
            android.view.inputmethod.InputMethodSubtype subtype = imi.getSubtypeAt(i);
            if (inputSubtypesSet.contains(subtype)) {
                sortedList.add(subtype);
                inputSubtypesSet.remove(subtype);
            }
        }
        // If subtypes in inputSubtypesSet remain, that means these subtypes are not
        // contained in imi, so the remaining subtypes will be appended.
        for (android.view.inputmethod.InputMethodSubtype subtype : inputSubtypesSet) {
            sortedList.add(subtype);
        }
        return sortedList;
    }
}

