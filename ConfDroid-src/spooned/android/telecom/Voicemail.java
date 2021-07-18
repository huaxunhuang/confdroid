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
package android.telecom;


/**
 * Represents a single voicemail stored in the voicemail content provider.
 *
 * @unknown 
 */
public class Voicemail implements android.os.Parcelable {
    private final java.lang.Long mTimestamp;

    private final java.lang.String mNumber;

    private final android.telecom.PhoneAccountHandle mPhoneAccount;

    private final java.lang.Long mId;

    private final java.lang.Long mDuration;

    private final java.lang.String mSource;

    private final java.lang.String mProviderData;

    private final android.net.Uri mUri;

    private final java.lang.Boolean mIsRead;

    private final java.lang.Boolean mHasContent;

    private final java.lang.String mTranscription;

    private Voicemail(java.lang.Long timestamp, java.lang.String number, android.telecom.PhoneAccountHandle phoneAccountHandle, java.lang.Long id, java.lang.Long duration, java.lang.String source, java.lang.String providerData, android.net.Uri uri, java.lang.Boolean isRead, java.lang.Boolean hasContent, java.lang.String transcription) {
        mTimestamp = timestamp;
        mNumber = number;
        mPhoneAccount = phoneAccountHandle;
        mId = id;
        mDuration = duration;
        mSource = source;
        mProviderData = providerData;
        mUri = uri;
        mIsRead = isRead;
        mHasContent = hasContent;
        mTranscription = transcription;
    }

    /**
     * Create a {@link Builder} for a new {@link Voicemail} to be inserted.
     * <p>
     * The number and the timestamp are mandatory for insertion.
     */
    public static android.telecom.Voicemail.Builder createForInsertion(long timestamp, java.lang.String number) {
        return new android.telecom.Voicemail.Builder().setNumber(number).setTimestamp(timestamp);
    }

    /**
     * Create a {@link Builder} for a {@link Voicemail} to be updated (or deleted).
     * <p>
     * The id and source data fields are mandatory for update - id is necessary for updating the
     * database and source data is necessary for updating the server.
     */
    public static android.telecom.Voicemail.Builder createForUpdate(long id, java.lang.String sourceData) {
        return new android.telecom.Voicemail.Builder().setId(id).setSourceData(sourceData);
    }

    /**
     * Builder pattern for creating a {@link Voicemail}. The builder must be created with the
     * {@link #createForInsertion(long, String)} method.
     * <p>
     * This class is <b>not thread safe</b>
     */
    public static class Builder {
        private java.lang.Long mBuilderTimestamp;

        private java.lang.String mBuilderNumber;

        private android.telecom.PhoneAccountHandle mBuilderPhoneAccount;

        private java.lang.Long mBuilderId;

        private java.lang.Long mBuilderDuration;

        private java.lang.String mBuilderSourcePackage;

        private java.lang.String mBuilderSourceData;

        private android.net.Uri mBuilderUri;

        private java.lang.Boolean mBuilderIsRead;

        private boolean mBuilderHasContent;

        private java.lang.String mBuilderTranscription;

        /**
         * You should use the correct factory method to construct a builder.
         */
        private Builder() {
        }

        public android.telecom.Voicemail.Builder setNumber(java.lang.String number) {
            mBuilderNumber = number;
            return this;
        }

        public android.telecom.Voicemail.Builder setTimestamp(long timestamp) {
            mBuilderTimestamp = timestamp;
            return this;
        }

        public android.telecom.Voicemail.Builder setPhoneAccount(android.telecom.PhoneAccountHandle phoneAccount) {
            mBuilderPhoneAccount = phoneAccount;
            return this;
        }

        public android.telecom.Voicemail.Builder setId(long id) {
            mBuilderId = id;
            return this;
        }

        public android.telecom.Voicemail.Builder setDuration(long duration) {
            mBuilderDuration = duration;
            return this;
        }

        public android.telecom.Voicemail.Builder setSourcePackage(java.lang.String sourcePackage) {
            mBuilderSourcePackage = sourcePackage;
            return this;
        }

        public android.telecom.Voicemail.Builder setSourceData(java.lang.String sourceData) {
            mBuilderSourceData = sourceData;
            return this;
        }

        public android.telecom.Voicemail.Builder setUri(android.net.Uri uri) {
            mBuilderUri = uri;
            return this;
        }

        public android.telecom.Voicemail.Builder setIsRead(boolean isRead) {
            mBuilderIsRead = isRead;
            return this;
        }

        public android.telecom.Voicemail.Builder setHasContent(boolean hasContent) {
            mBuilderHasContent = hasContent;
            return this;
        }

        public android.telecom.Voicemail.Builder setTranscription(java.lang.String transcription) {
            mBuilderTranscription = transcription;
            return this;
        }

        public android.telecom.Voicemail build() {
            mBuilderId = (mBuilderId == null) ? -1 : mBuilderId;
            mBuilderTimestamp = (mBuilderTimestamp == null) ? 0 : mBuilderTimestamp;
            mBuilderDuration = (mBuilderDuration == null) ? 0 : mBuilderDuration;
            mBuilderIsRead = (mBuilderIsRead == null) ? false : mBuilderIsRead;
            return new android.telecom.Voicemail(mBuilderTimestamp, mBuilderNumber, mBuilderPhoneAccount, mBuilderId, mBuilderDuration, mBuilderSourcePackage, mBuilderSourceData, mBuilderUri, mBuilderIsRead, mBuilderHasContent, mBuilderTranscription);
        }
    }

    /**
     * The identifier of the voicemail in the content provider.
     * <p>
     * This may be missing in the case of a new {@link Voicemail} that we plan to insert into the
     * content provider, since until it has been inserted we don't know what id it should have. If
     * none is specified, we return -1.
     */
    public long getId() {
        return mId;
    }

    /**
     * The number of the person leaving the voicemail, empty string if unknown, null if not set.
     */
    public java.lang.String getNumber() {
        return mNumber;
    }

    /**
     * The phone account associated with the voicemail, null if not set.
     */
    public android.telecom.PhoneAccountHandle getPhoneAccount() {
        return mPhoneAccount;
    }

    /**
     * The timestamp the voicemail was received, in millis since the epoch, zero if not set.
     */
    public long getTimestampMillis() {
        return mTimestamp;
    }

    /**
     * Gets the duration of the voicemail in millis, or zero if the field is not set.
     */
    public long getDuration() {
        return mDuration;
    }

    /**
     * Returns the package name of the source that added this voicemail, or null if this field is
     * not set.
     */
    public java.lang.String getSourcePackage() {
        return mSource;
    }

    /**
     * Returns the application-specific data type stored with the voicemail, or null if this field
     * is not set.
     * <p>
     * Source data is typically used as an identifier to uniquely identify the voicemail against
     * the voicemail server. This is likely to be something like the IMAP UID, or some other
     * server-generated identifying string.
     */
    public java.lang.String getSourceData() {
        return mProviderData;
    }

    /**
     * Gets the Uri that can be used to refer to this voicemail, and to make it play.
     * <p>
     * Returns null if we don't know the Uri.
     */
    public android.net.Uri getUri() {
        return mUri;
    }

    /**
     * Tells us if the voicemail message has been marked as read.
     * <p>
     * Always returns false if this field has not been set, i.e. if hasRead() returns false.
     */
    public boolean isRead() {
        return mIsRead;
    }

    /**
     * Tells us if there is content stored at the Uri.
     */
    public boolean hasContent() {
        return mHasContent;
    }

    /**
     * Returns the text transcription of this voicemail, or null if this field is not set.
     */
    public java.lang.String getTranscription() {
        return mTranscription;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeLong(mTimestamp);
        dest.writeCharSequence(mNumber);
        if (mPhoneAccount == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            mPhoneAccount.writeToParcel(dest, flags);
        }
        dest.writeLong(mId);
        dest.writeLong(mDuration);
        dest.writeCharSequence(mSource);
        dest.writeCharSequence(mProviderData);
        if (mUri == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            mUri.writeToParcel(dest, flags);
        }
        if (mIsRead) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        if (mHasContent) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        dest.writeCharSequence(mTranscription);
    }

    public static final android.os.Parcelable.Creator<android.telecom.Voicemail> CREATOR = new android.os.Parcelable.Creator<android.telecom.Voicemail>() {
        @java.lang.Override
        public android.telecom.Voicemail createFromParcel(android.os.Parcel in) {
            return new android.telecom.Voicemail(in);
        }

        @java.lang.Override
        public android.telecom.Voicemail[] newArray(int size) {
            return new android.telecom.Voicemail[size];
        }
    };

    private Voicemail(android.os.Parcel in) {
        mTimestamp = in.readLong();
        mNumber = ((java.lang.String) (in.readCharSequence()));
        if (in.readInt() > 0) {
            mPhoneAccount = android.telecom.PhoneAccountHandle.CREATOR.createFromParcel(in);
        } else {
            mPhoneAccount = null;
        }
        mId = in.readLong();
        mDuration = in.readLong();
        mSource = ((java.lang.String) (in.readCharSequence()));
        mProviderData = ((java.lang.String) (in.readCharSequence()));
        if (in.readInt() > 0) {
            mUri = android.net.Uri.CREATOR.createFromParcel(in);
        } else {
            mUri = null;
        }
        mIsRead = (in.readInt() > 0) ? true : false;
        mHasContent = (in.readInt() > 0) ? true : false;
        mTranscription = ((java.lang.String) (in.readCharSequence()));
    }
}

