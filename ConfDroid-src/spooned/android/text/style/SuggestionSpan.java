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
package android.text.style;


/**
 * Holds suggestion candidates for the text enclosed in this span.
 *
 * When such a span is edited in an EditText, double tapping on the text enclosed in this span will
 * display a popup dialog listing suggestion replacement for that text. The user can then replace
 * the original text by one of the suggestions.
 *
 * These spans should typically be created by the input method to provide correction and alternates
 * for the text.
 *
 * @see TextView#isSuggestionsEnabled()
 */
public class SuggestionSpan extends android.text.style.CharacterStyle implements android.text.ParcelableSpan {
    private static final java.lang.String TAG = "SuggestionSpan";

    /**
     * Sets this flag if the suggestions should be easily accessible with few interactions.
     * This flag should be set for every suggestions that the user is likely to use.
     */
    public static final int FLAG_EASY_CORRECT = 0x1;

    /**
     * Sets this flag if the suggestions apply to a misspelled word/text. This type of suggestion is
     * rendered differently to highlight the error.
     */
    public static final int FLAG_MISSPELLED = 0x2;

    /**
     * Sets this flag if the auto correction is about to be applied to a word/text
     * that the user is typing/composing. This type of suggestion is rendered differently
     * to indicate the auto correction is happening.
     */
    public static final int FLAG_AUTO_CORRECTION = 0x4;

    public static final java.lang.String ACTION_SUGGESTION_PICKED = "android.text.style.SUGGESTION_PICKED";

    public static final java.lang.String SUGGESTION_SPAN_PICKED_AFTER = "after";

    public static final java.lang.String SUGGESTION_SPAN_PICKED_BEFORE = "before";

    public static final java.lang.String SUGGESTION_SPAN_PICKED_HASHCODE = "hashcode";

    public static final int SUGGESTIONS_MAX_SIZE = 5;

    /* TODO: Needs to check the validity and add a feature that TextView will change
    the current IME to the other IME which is specified in SuggestionSpan.
    An IME needs to set the span by specifying the target IME and Subtype of SuggestionSpan.
    And the current IME might want to specify any IME as the target IME including other IMEs.
     */
    private int mFlags;

    private final java.lang.String[] mSuggestions;

    /**
     * Kept for compatibility for apps that rely on invalid locale strings e.g.
     * {@code new Locale(" an ", " i n v a l i d ", "data")}, which cannot be handled by
     * {@link #mLanguageTag}.
     */
    @android.annotation.NonNull
    private final java.lang.String mLocaleStringForCompatibility;

    @android.annotation.NonNull
    private final java.lang.String mLanguageTag;

    private final java.lang.String mNotificationTargetClassName;

    private final java.lang.String mNotificationTargetPackageName;

    private final int mHashCode;

    private float mEasyCorrectUnderlineThickness;

    private int mEasyCorrectUnderlineColor;

    private float mMisspelledUnderlineThickness;

    private int mMisspelledUnderlineColor;

    private float mAutoCorrectionUnderlineThickness;

    private int mAutoCorrectionUnderlineColor;

    /**
     *
     *
     * @param context
     * 		Context for the application
     * @param suggestions
     * 		Suggestions for the string under the span
     * @param flags
     * 		Additional flags indicating how this span is handled in TextView
     */
    public SuggestionSpan(android.content.Context context, java.lang.String[] suggestions, int flags) {
        this(context, null, suggestions, flags, null);
    }

    /**
     *
     *
     * @param locale
     * 		Locale of the suggestions
     * @param suggestions
     * 		Suggestions for the string under the span
     * @param flags
     * 		Additional flags indicating how this span is handled in TextView
     */
    public SuggestionSpan(java.util.Locale locale, java.lang.String[] suggestions, int flags) {
        this(null, locale, suggestions, flags, null);
    }

    /**
     *
     *
     * @param context
     * 		Context for the application
     * @param locale
     * 		locale Locale of the suggestions
     * @param suggestions
     * 		Suggestions for the string under the span. Only the first up to
     * 		{@link SuggestionSpan#SUGGESTIONS_MAX_SIZE} will be considered. Null values not permitted.
     * @param flags
     * 		Additional flags indicating how this span is handled in TextView
     * @param notificationTargetClass
     * 		if not null, this class will get notified when the user
     * 		selects one of the suggestions.
     */
    public SuggestionSpan(android.content.Context context, java.util.Locale locale, java.lang.String[] suggestions, int flags, java.lang.Class<?> notificationTargetClass) {
        final int N = java.lang.Math.min(android.text.style.SuggestionSpan.SUGGESTIONS_MAX_SIZE, suggestions.length);
        mSuggestions = java.util.Arrays.copyOf(suggestions, N);
        mFlags = flags;
        final java.util.Locale sourceLocale;
        if (locale != null) {
            sourceLocale = locale;
        } else
            if (context != null) {
                // TODO: Consider to context.getResources().getResolvedLocale() instead.
                sourceLocale = context.getResources().getConfiguration().locale;
            } else {
                android.util.Log.e("SuggestionSpan", "No locale or context specified in SuggestionSpan constructor");
                sourceLocale = null;
            }

        mLocaleStringForCompatibility = (sourceLocale == null) ? "" : sourceLocale.toString();
        mLanguageTag = (sourceLocale == null) ? "" : sourceLocale.toLanguageTag();
        if (context != null) {
            mNotificationTargetPackageName = context.getPackageName();
        } else {
            mNotificationTargetPackageName = null;
        }
        if (notificationTargetClass != null) {
            mNotificationTargetClassName = notificationTargetClass.getCanonicalName();
        } else {
            mNotificationTargetClassName = "";
        }
        mHashCode = android.text.style.SuggestionSpan.hashCodeInternal(mSuggestions, mLanguageTag, mLocaleStringForCompatibility, mNotificationTargetClassName);
        initStyle(context);
    }

    private void initStyle(android.content.Context context) {
        if (context == null) {
            mMisspelledUnderlineThickness = 0;
            mEasyCorrectUnderlineThickness = 0;
            mAutoCorrectionUnderlineThickness = 0;
            mMisspelledUnderlineColor = android.graphics.Color.BLACK;
            mEasyCorrectUnderlineColor = android.graphics.Color.BLACK;
            mAutoCorrectionUnderlineColor = android.graphics.Color.BLACK;
            return;
        }
        int defStyleAttr = com.android.internal.R.attr.textAppearanceMisspelledSuggestion;
        android.content.res.TypedArray typedArray = context.obtainStyledAttributes(null, com.android.internal.R.styleable.SuggestionSpan, defStyleAttr, 0);
        mMisspelledUnderlineThickness = typedArray.getDimension(com.android.internal.R.styleable.SuggestionSpan_textUnderlineThickness, 0);
        mMisspelledUnderlineColor = typedArray.getColor(com.android.internal.R.styleable.SuggestionSpan_textUnderlineColor, android.graphics.Color.BLACK);
        defStyleAttr = com.android.internal.R.attr.textAppearanceEasyCorrectSuggestion;
        typedArray = context.obtainStyledAttributes(null, com.android.internal.R.styleable.SuggestionSpan, defStyleAttr, 0);
        mEasyCorrectUnderlineThickness = typedArray.getDimension(com.android.internal.R.styleable.SuggestionSpan_textUnderlineThickness, 0);
        mEasyCorrectUnderlineColor = typedArray.getColor(com.android.internal.R.styleable.SuggestionSpan_textUnderlineColor, android.graphics.Color.BLACK);
        defStyleAttr = com.android.internal.R.attr.textAppearanceAutoCorrectionSuggestion;
        typedArray = context.obtainStyledAttributes(null, com.android.internal.R.styleable.SuggestionSpan, defStyleAttr, 0);
        mAutoCorrectionUnderlineThickness = typedArray.getDimension(com.android.internal.R.styleable.SuggestionSpan_textUnderlineThickness, 0);
        mAutoCorrectionUnderlineColor = typedArray.getColor(com.android.internal.R.styleable.SuggestionSpan_textUnderlineColor, android.graphics.Color.BLACK);
    }

    public SuggestionSpan(android.os.Parcel src) {
        mSuggestions = src.readStringArray();
        mFlags = src.readInt();
        mLocaleStringForCompatibility = src.readString();
        mLanguageTag = src.readString();
        mNotificationTargetClassName = src.readString();
        mNotificationTargetPackageName = src.readString();
        mHashCode = src.readInt();
        mEasyCorrectUnderlineColor = src.readInt();
        mEasyCorrectUnderlineThickness = src.readFloat();
        mMisspelledUnderlineColor = src.readInt();
        mMisspelledUnderlineThickness = src.readFloat();
        mAutoCorrectionUnderlineColor = src.readInt();
        mAutoCorrectionUnderlineThickness = src.readFloat();
    }

    /**
     *
     *
     * @return an array of suggestion texts for this span
     */
    public java.lang.String[] getSuggestions() {
        return mSuggestions;
    }

    /**
     *
     *
     * @deprecated use {@link #getLocaleObject()} instead.
     * @return the locale of the suggestions. An empty string is returned if no locale is specified.
     */
    @android.annotation.NonNull
    @java.lang.Deprecated
    public java.lang.String getLocale() {
        return mLocaleStringForCompatibility;
    }

    /**
     * Returns a well-formed BCP 47 language tag representation of the suggestions, as a
     * {@link Locale} object.
     *
     * <p><b>Caveat</b>: The returned object is guaranteed to be a  a well-formed BCP 47 language tag
     * representation.  For example, this method can return an empty locale rather than returning a
     * malformed data when this object is initialized with an malformed {@link Locale} object, e.g.
     * {@code new Locale(" a ", " b c d ", " "}.</p>
     *
     * @return the locale of the suggestions. {@code null} is returned if no locale is specified.
     */
    @android.annotation.Nullable
    public java.util.Locale getLocaleObject() {
        return mLanguageTag.isEmpty() ? null : java.util.Locale.forLanguageTag(mLanguageTag);
    }

    /**
     *
     *
     * @return The name of the class to notify. The class of the original IME package will receive
    a notification when the user selects one of the suggestions. The notification will include
    the original string, the suggested replacement string as well as the hashCode of this span.
    The class will get notified by an intent that has those information.
    This is an internal API because only the framework should know the class name.
     * @unknown 
     */
    public java.lang.String getNotificationTargetClassName() {
        return mNotificationTargetClassName;
    }

    public int getFlags() {
        return mFlags;
    }

    public void setFlags(int flags) {
        mFlags = flags;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    /**
     *
     *
     * @unknown 
     */
    public void writeToParcelInternal(android.os.Parcel dest, int flags) {
        dest.writeStringArray(mSuggestions);
        dest.writeInt(mFlags);
        dest.writeString(mLocaleStringForCompatibility);
        dest.writeString(mLanguageTag);
        dest.writeString(mNotificationTargetClassName);
        dest.writeString(mNotificationTargetPackageName);
        dest.writeInt(mHashCode);
        dest.writeInt(mEasyCorrectUnderlineColor);
        dest.writeFloat(mEasyCorrectUnderlineThickness);
        dest.writeInt(mMisspelledUnderlineColor);
        dest.writeFloat(mMisspelledUnderlineThickness);
        dest.writeInt(mAutoCorrectionUnderlineColor);
        dest.writeFloat(mAutoCorrectionUnderlineThickness);
    }

    @java.lang.Override
    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    /**
     *
     *
     * @unknown 
     */
    public int getSpanTypeIdInternal() {
        return android.text.TextUtils.SUGGESTION_SPAN;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o instanceof android.text.style.SuggestionSpan) {
            return ((android.text.style.SuggestionSpan) (o)).hashCode() == mHashCode;
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        return mHashCode;
    }

    private static int hashCodeInternal(java.lang.String[] suggestions, @android.annotation.NonNull
    java.lang.String languageTag, @android.annotation.NonNull
    java.lang.String localeStringForCompatibility, java.lang.String notificationTargetClassName) {
        return java.util.Arrays.hashCode(new java.lang.Object[]{ java.lang.Long.valueOf(android.os.SystemClock.uptimeMillis()), suggestions, languageTag, localeStringForCompatibility, notificationTargetClassName });
    }

    public static final android.os.Parcelable.Creator<android.text.style.SuggestionSpan> CREATOR = new android.os.Parcelable.Creator<android.text.style.SuggestionSpan>() {
        @java.lang.Override
        public android.text.style.SuggestionSpan createFromParcel(android.os.Parcel source) {
            return new android.text.style.SuggestionSpan(source);
        }

        @java.lang.Override
        public android.text.style.SuggestionSpan[] newArray(int size) {
            return new android.text.style.SuggestionSpan[size];
        }
    };

    @java.lang.Override
    public void updateDrawState(android.text.TextPaint tp) {
        final boolean misspelled = (mFlags & android.text.style.SuggestionSpan.FLAG_MISSPELLED) != 0;
        final boolean easy = (mFlags & android.text.style.SuggestionSpan.FLAG_EASY_CORRECT) != 0;
        final boolean autoCorrection = (mFlags & android.text.style.SuggestionSpan.FLAG_AUTO_CORRECTION) != 0;
        if (easy) {
            if (!misspelled) {
                tp.setUnderlineText(mEasyCorrectUnderlineColor, mEasyCorrectUnderlineThickness);
            } else
                if (tp.underlineColor == 0) {
                    // Spans are rendered in an arbitrary order. Since misspelled is less prioritary
                    // than just easy, do not apply misspelled if an easy (or a mispelled) has been set
                    tp.setUnderlineText(mMisspelledUnderlineColor, mMisspelledUnderlineThickness);
                }

        } else
            if (autoCorrection) {
                tp.setUnderlineText(mAutoCorrectionUnderlineColor, mAutoCorrectionUnderlineThickness);
            }

    }

    /**
     *
     *
     * @return The color of the underline for that span, or 0 if there is no underline
     * @unknown 
     */
    public int getUnderlineColor() {
        // The order here should match what is used in updateDrawState
        final boolean misspelled = (mFlags & android.text.style.SuggestionSpan.FLAG_MISSPELLED) != 0;
        final boolean easy = (mFlags & android.text.style.SuggestionSpan.FLAG_EASY_CORRECT) != 0;
        final boolean autoCorrection = (mFlags & android.text.style.SuggestionSpan.FLAG_AUTO_CORRECTION) != 0;
        if (easy) {
            if (!misspelled) {
                return mEasyCorrectUnderlineColor;
            } else {
                return mMisspelledUnderlineColor;
            }
        } else
            if (autoCorrection) {
                return mAutoCorrectionUnderlineColor;
            }

        return 0;
    }

    /**
     * Notifies a suggestion selection.
     *
     * @unknown 
     */
    public void notifySelection(android.content.Context context, java.lang.String original, int index) {
        final android.content.Intent intent = new android.content.Intent();
        if ((context == null) || (mNotificationTargetClassName == null)) {
            return;
        }
        // Ensures that only a class in the original IME package will receive the
        // notification.
        if (((mSuggestions == null) || (index < 0)) || (index >= mSuggestions.length)) {
            android.util.Log.w(android.text.style.SuggestionSpan.TAG, (("Unable to notify the suggestion as the index is out of range index=" + index) + " length=") + mSuggestions.length);
            return;
        }
        // The package name is not mandatory (legacy from JB), and if the package name
        // is missing, we try to notify the suggestion through the input method manager.
        if (mNotificationTargetPackageName != null) {
            intent.setClassName(mNotificationTargetPackageName, mNotificationTargetClassName);
            intent.setAction(android.text.style.SuggestionSpan.ACTION_SUGGESTION_PICKED);
            intent.putExtra(android.text.style.SuggestionSpan.SUGGESTION_SPAN_PICKED_BEFORE, original);
            intent.putExtra(android.text.style.SuggestionSpan.SUGGESTION_SPAN_PICKED_AFTER, mSuggestions[index]);
            intent.putExtra(android.text.style.SuggestionSpan.SUGGESTION_SPAN_PICKED_HASHCODE, hashCode());
            context.sendBroadcast(intent);
        } else {
            android.view.inputmethod.InputMethodManager imm = android.view.inputmethod.InputMethodManager.peekInstance();
            if (imm != null) {
                imm.notifySuggestionPicked(this, original, index);
            }
        }
    }
}

