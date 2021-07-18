/**
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.view.textservice;


/**
 * This class contains a metadata of the input of TextService
 */
public final class TextInfo implements android.os.Parcelable {
    private final java.lang.CharSequence mCharSequence;

    private final int mCookie;

    private final int mSequenceNumber;

    private static final int DEFAULT_COOKIE = 0;

    private static final int DEFAULT_SEQUENCE_NUMBER = 0;

    /**
     * Constructor.
     *
     * @param text
     * 		the text which will be input to TextService
     */
    public TextInfo(java.lang.String text) {
        this(text, 0, android.view.textservice.TextInfo.getStringLengthOrZero(text), android.view.textservice.TextInfo.DEFAULT_COOKIE, android.view.textservice.TextInfo.DEFAULT_SEQUENCE_NUMBER);
    }

    /**
     * Constructor.
     *
     * @param text
     * 		the text which will be input to TextService
     * @param cookie
     * 		the cookie for this TextInfo
     * @param sequenceNumber
     * 		the sequence number for this TextInfo
     */
    public TextInfo(java.lang.String text, int cookie, int sequenceNumber) {
        this(text, 0, android.view.textservice.TextInfo.getStringLengthOrZero(text), cookie, sequenceNumber);
    }

    private static int getStringLengthOrZero(final java.lang.String text) {
        return android.text.TextUtils.isEmpty(text) ? 0 : text.length();
    }

    /**
     * Constructor.
     *
     * @param charSequence
     * 		the text which will be input to TextService. Attached spans that
     * 		implement {@link ParcelableSpan} will also be marshaled alongside with the text.
     * @param start
     * 		the beginning of the range of text (inclusive).
     * @param end
     * 		the end of the range of text (exclusive).
     * @param cookie
     * 		the cookie for this TextInfo
     * @param sequenceNumber
     * 		the sequence number for this TextInfo
     */
    public TextInfo(java.lang.CharSequence charSequence, int start, int end, int cookie, int sequenceNumber) {
        if (android.text.TextUtils.isEmpty(charSequence)) {
            throw new java.lang.IllegalArgumentException("charSequence is empty");
        }
        // Create a snapshot of the text including spans in case they are updated outside later.
        final android.text.SpannableStringBuilder spannableString = new android.text.SpannableStringBuilder(charSequence, start, end);
        // SpellCheckSpan is for internal use. We do not want to marshal this for TextService.
        final android.text.style.SpellCheckSpan[] spans = spannableString.getSpans(0, spannableString.length(), android.text.style.SpellCheckSpan.class);
        for (int i = 0; i < spans.length; ++i) {
            spannableString.removeSpan(spans[i]);
        }
        mCharSequence = spannableString;
        mCookie = cookie;
        mSequenceNumber = sequenceNumber;
    }

    public TextInfo(android.os.Parcel source) {
        mCharSequence = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
        mCookie = source.readInt();
        mSequenceNumber = source.readInt();
    }

    /**
     * Used to package this object into a {@link Parcel}.
     *
     * @param dest
     * 		The {@link Parcel} to be written.
     * @param flags
     * 		The flags used for parceling.
     */
    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        android.text.TextUtils.writeToParcel(mCharSequence, dest, flags);
        dest.writeInt(mCookie);
        dest.writeInt(mSequenceNumber);
    }

    /**
     *
     *
     * @return the text which is an input of a text service
     */
    public java.lang.String getText() {
        if (mCharSequence == null) {
            return null;
        }
        return mCharSequence.toString();
    }

    /**
     *
     *
     * @return the charSequence which is an input of a text service. This may have some parcelable
    spans.
     */
    public java.lang.CharSequence getCharSequence() {
        return mCharSequence;
    }

    /**
     *
     *
     * @return the cookie of TextInfo
     */
    public int getCookie() {
        return mCookie;
    }

    /**
     *
     *
     * @return the sequence of TextInfo
     */
    public int getSequence() {
        return mSequenceNumber;
    }

    /**
     * Used to make this class parcelable.
     */
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.textservice.TextInfo> CREATOR = new android.os.Parcelable.Creator<android.view.textservice.TextInfo>() {
        @java.lang.Override
        public android.view.textservice.TextInfo createFromParcel(android.os.Parcel source) {
            return new android.view.textservice.TextInfo(source);
        }

        @java.lang.Override
        public android.view.textservice.TextInfo[] newArray(int size) {
            return new android.view.textservice.TextInfo[size];
        }
    };

    /**
     * Used to make this class parcelable.
     */
    @java.lang.Override
    public int describeContents() {
        return 0;
    }
}

