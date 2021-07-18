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
package android.content;


/**
 * Value type that contains information about a periodic sync.
 */
public class PeriodicSync implements android.os.Parcelable {
    /**
     * The account to be synced. Can be null.
     */
    public final android.accounts.Account account;

    /**
     * The authority of the sync. Can be null.
     */
    public final java.lang.String authority;

    /**
     * Any extras that parameters that are to be passed to the sync adapter.
     */
    public final android.os.Bundle extras;

    /**
     * How frequently the sync should be scheduled, in seconds. Kept around for API purposes.
     */
    public final long period;

    /**
     * How much flexibility can be taken in scheduling the sync, in seconds.
     * {@hide }
     */
    public final long flexTime;

    /**
     * Creates a new PeriodicSync, copying the Bundle. This constructor is no longer used.
     */
    public PeriodicSync(android.accounts.Account account, java.lang.String authority, android.os.Bundle extras, long periodInSeconds) {
        this.account = account;
        this.authority = authority;
        if (extras == null) {
            this.extras = new android.os.Bundle();
        } else {
            this.extras = new android.os.Bundle(extras);
        }
        this.period = periodInSeconds;
        // Old API uses default flex time. No-one should be using this ctor anyway.
        this.flexTime = 0L;
    }

    /**
     * Create a copy of a periodic sync.
     * {@hide }
     */
    public PeriodicSync(android.content.PeriodicSync other) {
        this.account = other.account;
        this.authority = other.authority;
        this.extras = new android.os.Bundle(other.extras);
        this.period = other.period;
        this.flexTime = other.flexTime;
    }

    /**
     * A PeriodicSync for a sync with a specified provider.
     * {@hide }
     */
    public PeriodicSync(android.accounts.Account account, java.lang.String authority, android.os.Bundle extras, long period, long flexTime) {
        this.account = account;
        this.authority = authority;
        this.extras = new android.os.Bundle(extras);
        this.period = period;
        this.flexTime = flexTime;
    }

    private PeriodicSync(android.os.Parcel in) {
        this.account = in.readParcelable(null);
        this.authority = in.readString();
        this.extras = in.readBundle();
        this.period = in.readLong();
        this.flexTime = in.readLong();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeParcelable(account, flags);
        dest.writeString(authority);
        dest.writeBundle(extras);
        dest.writeLong(period);
        dest.writeLong(flexTime);
    }

    @android.annotation.NonNull
    public static final android.content.Creator<android.content.PeriodicSync> CREATOR = new android.content.Creator<android.content.PeriodicSync>() {
        @java.lang.Override
        public android.content.PeriodicSync createFromParcel(android.os.Parcel source) {
            return new android.content.PeriodicSync(source);
        }

        @java.lang.Override
        public android.content.PeriodicSync[] newArray(int size) {
            return new android.content.PeriodicSync[size];
        }
    };

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof android.content.PeriodicSync)) {
            return false;
        }
        final android.content.PeriodicSync other = ((android.content.PeriodicSync) (o));
        return ((account.equals(other.account) && authority.equals(other.authority)) && (period == other.period)) && android.content.PeriodicSync.syncExtrasEquals(extras, other.extras);
    }

    /**
     * Periodic sync extra comparison function.
     * {@hide }
     */
    public static boolean syncExtrasEquals(android.os.Bundle b1, android.os.Bundle b2) {
        if (b1.size() != b2.size()) {
            return false;
        }
        if (b1.isEmpty()) {
            return true;
        }
        for (java.lang.String key : b1.keySet()) {
            if (!b2.containsKey(key)) {
                return false;
            }
            // Null check. According to ContentResolver#validateSyncExtrasBundle null-valued keys
            // are allowed in the bundle.
            if (!java.util.Objects.equals(b1.get(key), b2.get(key))) {
                return false;
            }
        }
        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((("account: " + account) + ", authority: ") + authority) + ". period: ") + period) + "s ") + ", flex: ") + flexTime;
    }
}

