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
package android.accounts;


/**
 * Value type that represents an Account in the {@link AccountManager}. This object is
 * {@link Parcelable} and also overrides {@link #equals} and {@link #hashCode}, making it
 * suitable for use as the key of a {@link java.util.Map}
 */
public class Account implements android.os.Parcelable {
    private static final java.lang.String TAG = "Account";

    @com.android.internal.annotations.GuardedBy("sAccessedAccounts")
    private static final java.util.Set<android.accounts.Account> sAccessedAccounts = new android.util.ArraySet<>();

    public final java.lang.String name;

    public final java.lang.String type;

    @android.annotation.Nullable
    private final java.lang.String accessId;

    public boolean equals(java.lang.Object o) {
        if (o == this)
            return true;

        if (!(o instanceof android.accounts.Account))
            return false;

        final android.accounts.Account other = ((android.accounts.Account) (o));
        return name.equals(other.name) && type.equals(other.type);
    }

    public int hashCode() {
        int result = 17;
        result = (31 * result) + name.hashCode();
        result = (31 * result) + type.hashCode();
        return result;
    }

    public Account(java.lang.String name, java.lang.String type) {
        this(name, type, null);
    }

    /**
     *
     *
     * @unknown 
     */
    public Account(@android.annotation.NonNull
    android.accounts.Account other, @android.annotation.NonNull
    java.lang.String accessId) {
        this(other.name, other.type, accessId);
    }

    /**
     *
     *
     * @unknown 
     */
    public Account(java.lang.String name, java.lang.String type, java.lang.String accessId) {
        if (android.text.TextUtils.isEmpty(name)) {
            throw new java.lang.IllegalArgumentException("the name must not be empty: " + name);
        }
        if (android.text.TextUtils.isEmpty(type)) {
            throw new java.lang.IllegalArgumentException("the type must not be empty: " + type);
        }
        this.name = name;
        this.type = type;
        this.accessId = accessId;
    }

    public Account(android.os.Parcel in) {
        this.name = in.readString();
        this.type = in.readString();
        this.accessId = in.readString();
        if (accessId != null) {
            synchronized(android.accounts.Account.sAccessedAccounts) {
                if (android.accounts.Account.sAccessedAccounts.add(this)) {
                    try {
                        android.accounts.IAccountManager accountManager = IAccountManager.Stub.asInterface(android.os.ServiceManager.getService(android.content.Context.ACCOUNT_SERVICE));
                        accountManager.onAccountAccessed(accessId);
                    } catch (android.os.RemoteException e) {
                        android.util.Log.e(android.accounts.Account.TAG, "Error noting account access", e);
                    }
                }
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public java.lang.String getAccessId() {
        return accessId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(accessId);
    }

    public static final android.os.Parcelable.Creator<android.accounts.Account> CREATOR = new android.os.Parcelable.Creator<android.accounts.Account>() {
        public android.accounts.Account createFromParcel(android.os.Parcel source) {
            return new android.accounts.Account(source);
        }

        public android.accounts.Account[] newArray(int size) {
            return new android.accounts.Account[size];
        }
    };

    public java.lang.String toString() {
        return ((("Account {name=" + name) + ", type=") + type) + "}";
    }
}

