/**
 * Copyright (C) 2012 The Android Open Source Project
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
 * Changes the {@link Locale} of the text to which the span is attached.
 */
public class LocaleSpan extends android.text.style.MetricAffectingSpan implements android.text.ParcelableSpan {
    @android.annotation.NonNull
    private final android.os.LocaleList mLocales;

    /**
     * Creates a {@link LocaleSpan} from a well-formed {@link Locale}.  Note that only
     * {@link Locale} objects that can be created by {@link Locale#forLanguageTag(String)} are
     * supported.
     *
     * <p><b>Caveat:</b> Do not specify any {@link Locale} object that cannot be created by
     * {@link Locale#forLanguageTag(String)}.  {@code new Locale(" a ", " b c", " d")} is an
     * example of such a malformed {@link Locale} object.</p>
     *
     * @param locale
     * 		The {@link Locale} of the text to which the span is attached.
     * @see #LocaleSpan(LocaleList)
     */
    public LocaleSpan(@android.annotation.Nullable
    java.util.Locale locale) {
        mLocales = (locale == null) ? android.os.LocaleList.getEmptyLocaleList() : new android.os.LocaleList(locale);
    }

    /**
     * Creates a {@link LocaleSpan} from {@link LocaleList}.
     *
     * @param locales
     * 		The {@link LocaleList} of the text to which the span is attached.
     * @throws NullPointerException
     * 		if {@code locales} is null
     */
    public LocaleSpan(@android.annotation.NonNull
    android.os.LocaleList locales) {
        com.android.internal.util.Preconditions.checkNotNull(locales, "locales cannot be null");
        mLocales = locales;
    }

    public LocaleSpan(android.os.Parcel source) {
        mLocales = android.os.LocaleList.CREATOR.createFromParcel(source);
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
        return android.text.TextUtils.LOCALE_SPAN;
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
        mLocales.writeToParcel(dest, flags);
    }

    /**
     *
     *
     * @return The {@link Locale} for this span.  If multiple locales are associated with this
    span, only the first locale is returned.  {@code null} if no {@link Locale} is specified.
     * @see LocaleList#get()
     * @see #getLocales()
     */
    @android.annotation.Nullable
    public java.util.Locale getLocale() {
        return mLocales.get(0);
    }

    /**
     *
     *
     * @return The entire list of locales that are associated with this span.
     */
    @android.annotation.NonNull
    public android.os.LocaleList getLocales() {
        return mLocales;
    }

    @java.lang.Override
    public void updateDrawState(android.text.TextPaint ds) {
        android.text.style.LocaleSpan.apply(ds, mLocales);
    }

    @java.lang.Override
    public void updateMeasureState(android.text.TextPaint paint) {
        android.text.style.LocaleSpan.apply(paint, mLocales);
    }

    private static void apply(@android.annotation.NonNull
    android.graphics.Paint paint, @android.annotation.NonNull
    android.os.LocaleList locales) {
        paint.setTextLocales(locales);
    }
}

