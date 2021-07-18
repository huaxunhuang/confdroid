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
 * Value type that represents a SyncAdapterType. This object overrides {@link #equals} and
 * {@link #hashCode}, making it suitable for use as the key of a {@link java.util.Map}
 */
public class SyncAdapterType implements android.os.Parcelable {
    public final java.lang.String authority;

    public final java.lang.String accountType;

    public final boolean isKey;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private final boolean userVisible;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private final boolean supportsUploading;

    @android.annotation.UnsupportedAppUsage
    private final boolean isAlwaysSyncable;

    @android.annotation.UnsupportedAppUsage
    private final boolean allowParallelSyncs;

    @android.annotation.UnsupportedAppUsage
    private final java.lang.String settingsActivity;

    private final java.lang.String packageName;

    public SyncAdapterType(java.lang.String authority, java.lang.String accountType, boolean userVisible, boolean supportsUploading) {
        if (android.text.TextUtils.isEmpty(authority)) {
            throw new java.lang.IllegalArgumentException("the authority must not be empty: " + authority);
        }
        if (android.text.TextUtils.isEmpty(accountType)) {
            throw new java.lang.IllegalArgumentException("the accountType must not be empty: " + accountType);
        }
        this.authority = authority;
        this.accountType = accountType;
        this.userVisible = userVisible;
        this.supportsUploading = supportsUploading;
        this.isAlwaysSyncable = false;
        this.allowParallelSyncs = false;
        this.settingsActivity = null;
        this.isKey = false;
        this.packageName = null;
    }

    /**
     *
     *
     * @unknown 
     */
    public SyncAdapterType(java.lang.String authority, java.lang.String accountType, boolean userVisible, boolean supportsUploading, boolean isAlwaysSyncable, boolean allowParallelSyncs, java.lang.String settingsActivity, java.lang.String packageName) {
        if (android.text.TextUtils.isEmpty(authority)) {
            throw new java.lang.IllegalArgumentException("the authority must not be empty: " + authority);
        }
        if (android.text.TextUtils.isEmpty(accountType)) {
            throw new java.lang.IllegalArgumentException("the accountType must not be empty: " + accountType);
        }
        this.authority = authority;
        this.accountType = accountType;
        this.userVisible = userVisible;
        this.supportsUploading = supportsUploading;
        this.isAlwaysSyncable = isAlwaysSyncable;
        this.allowParallelSyncs = allowParallelSyncs;
        this.settingsActivity = settingsActivity;
        this.isKey = false;
        this.packageName = packageName;
    }

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private SyncAdapterType(java.lang.String authority, java.lang.String accountType) {
        if (android.text.TextUtils.isEmpty(authority)) {
            throw new java.lang.IllegalArgumentException("the authority must not be empty: " + authority);
        }
        if (android.text.TextUtils.isEmpty(accountType)) {
            throw new java.lang.IllegalArgumentException("the accountType must not be empty: " + accountType);
        }
        this.authority = authority;
        this.accountType = accountType;
        this.userVisible = true;
        this.supportsUploading = true;
        this.isAlwaysSyncable = false;
        this.allowParallelSyncs = false;
        this.settingsActivity = null;
        this.isKey = true;
        this.packageName = null;
    }

    public boolean supportsUploading() {
        if (isKey) {
            throw new java.lang.IllegalStateException("this method is not allowed to be called when this is a key");
        }
        return supportsUploading;
    }

    public boolean isUserVisible() {
        if (isKey) {
            throw new java.lang.IllegalStateException("this method is not allowed to be called when this is a key");
        }
        return userVisible;
    }

    /**
     *
     *
     * @return True if this SyncAdapter supports syncing multiple accounts simultaneously.
    If false then the SyncManager will take care to only start one sync at a time
    using this SyncAdapter.
     */
    public boolean allowParallelSyncs() {
        if (isKey) {
            throw new java.lang.IllegalStateException("this method is not allowed to be called when this is a key");
        }
        return allowParallelSyncs;
    }

    /**
     * If true then the SyncManager will never issue an initialization sync to the SyncAdapter
     * and will instead automatically call
     * {@link ContentResolver#setIsSyncable(android.accounts.Account, String, int)} with a
     * value of 1 for each account and provider that this sync adapter supports.
     *
     * @return true if the SyncAdapter does not require initialization and if it is ok for the
    SyncAdapter to treat it as syncable automatically.
     */
    public boolean isAlwaysSyncable() {
        if (isKey) {
            throw new java.lang.IllegalStateException("this method is not allowed to be called when this is a key");
        }
        return isAlwaysSyncable;
    }

    /**
     *
     *
     * @return The activity to use to invoke this SyncAdapter's settings activity.
    May be null.
     */
    public java.lang.String getSettingsActivity() {
        if (isKey) {
            throw new java.lang.IllegalStateException("this method is not allowed to be called when this is a key");
        }
        return settingsActivity;
    }

    /**
     * The package hosting the sync adapter.
     *
     * @return The package name.
     * @unknown 
     */
    @android.annotation.Nullable
    public java.lang.String getPackageName() {
        return packageName;
    }

    public static android.content.SyncAdapterType newKey(java.lang.String authority, java.lang.String accountType) {
        return new android.content.SyncAdapterType(authority, accountType);
    }

    public boolean equals(java.lang.Object o) {
        if (o == this)
            return true;

        if (!(o instanceof android.content.SyncAdapterType))
            return false;

        final android.content.SyncAdapterType other = ((android.content.SyncAdapterType) (o));
        // don't include userVisible or supportsUploading in the equality check
        return authority.equals(other.authority) && accountType.equals(other.accountType);
    }

    public int hashCode() {
        int result = 17;
        result = (31 * result) + authority.hashCode();
        result = (31 * result) + accountType.hashCode();
        // don't include userVisible or supportsUploading  the hash
        return result;
    }

    public java.lang.String toString() {
        if (isKey) {
            return ((("SyncAdapterType Key {name=" + authority) + ", type=") + accountType) + "}";
        } else {
            return ((((((((((((((("SyncAdapterType {name=" + authority) + ", type=") + accountType) + ", userVisible=") + userVisible) + ", supportsUploading=") + supportsUploading) + ", isAlwaysSyncable=") + isAlwaysSyncable) + ", allowParallelSyncs=") + allowParallelSyncs) + ", settingsActivity=") + settingsActivity) + ", packageName=") + packageName) + "}";
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (isKey) {
            throw new java.lang.IllegalStateException("keys aren't parcelable");
        }
        dest.writeString(authority);
        dest.writeString(accountType);
        dest.writeInt(userVisible ? 1 : 0);
        dest.writeInt(supportsUploading ? 1 : 0);
        dest.writeInt(isAlwaysSyncable ? 1 : 0);
        dest.writeInt(allowParallelSyncs ? 1 : 0);
        dest.writeString(settingsActivity);
        dest.writeString(packageName);
    }

    public SyncAdapterType(android.os.Parcel source) {
        this(source.readString(), source.readString(), source.readInt() != 0, source.readInt() != 0, source.readInt() != 0, source.readInt() != 0, source.readString(), source.readString());
    }

    @android.annotation.NonNull
    public static final android.content.Creator<android.content.SyncAdapterType> CREATOR = new android.content.Creator<android.content.SyncAdapterType>() {
        public android.content.SyncAdapterType createFromParcel(android.os.Parcel source) {
            return new android.content.SyncAdapterType(source);
        }

        public android.content.SyncAdapterType[] newArray(int size) {
            return new android.content.SyncAdapterType[size];
        }
    };
}

