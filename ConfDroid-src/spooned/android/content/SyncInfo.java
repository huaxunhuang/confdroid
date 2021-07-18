/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.content;


/**
 * Information about the sync operation that is currently underway.
 */
public class SyncInfo implements android.os.Parcelable {
    /**
     * Used when the caller receiving this object doesn't have permission to access the accounts
     * on device.
     *
     * @unknown Manifest.permission.GET_ACCOUNTS
     */
    private static final android.accounts.Account REDACTED_ACCOUNT = new android.accounts.Account("*****", "*****");

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public final int authorityId;

    /**
     * The {@link Account} that is currently being synced.
     */
    public final android.accounts.Account account;

    /**
     * The authority of the provider that is currently being synced.
     */
    public final java.lang.String authority;

    /**
     * The start time of the current sync operation in milliseconds since boot.
     * This is represented in elapsed real time.
     * See {@link android.os.SystemClock#elapsedRealtime()}.
     */
    public final long startTime;

    /**
     * Creates a SyncInfo object with an unusable Account. Used when the caller receiving this
     * object doesn't have access to the accounts on the device.
     *
     * @unknown Manifest.permission.GET_ACCOUNTS
     * @unknown 
     */
    public static android.content.SyncInfo createAccountRedacted(int authorityId, java.lang.String authority, long startTime) {
        return new android.content.SyncInfo(authorityId, android.content.SyncInfo.REDACTED_ACCOUNT, authority, startTime);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public SyncInfo(int authorityId, android.accounts.Account account, java.lang.String authority, long startTime) {
        this.authorityId = authorityId;
        this.account = account;
        this.authority = authority;
        this.startTime = startTime;
    }

    /**
     *
     *
     * @unknown 
     */
    public SyncInfo(android.content.SyncInfo other) {
        this.authorityId = other.authorityId;
        this.account = new android.accounts.Account(other.account.name, other.account.type);
        this.authority = other.authority;
        this.startTime = other.startTime;
    }

    /**
     *
     *
     * @unknown 
     */
    public int describeContents() {
        return 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeInt(authorityId);
        parcel.writeParcelable(account, flags);
        parcel.writeString(authority);
        parcel.writeLong(startTime);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    SyncInfo(android.os.Parcel parcel) {
        authorityId = parcel.readInt();
        account = parcel.readParcelable(android.accounts.Account.class.getClassLoader());
        authority = parcel.readString();
        startTime = parcel.readLong();
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    @android.annotation.NonNull
    public static final android.content.Creator<android.content.SyncInfo> CREATOR = new android.content.Creator<android.content.SyncInfo>() {
        public android.content.SyncInfo createFromParcel(android.os.Parcel in) {
            return new android.content.SyncInfo(in);
        }

        public android.content.SyncInfo[] newArray(int size) {
            return new android.content.SyncInfo[size];
        }
    };
}

