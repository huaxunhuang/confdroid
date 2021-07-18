/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.service.notification;


/**
 * Ranking updates from the Ranker.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class Adjustment implements android.os.Parcelable {
    private final java.lang.String mPackage;

    private final java.lang.String mKey;

    private final int mImportance;

    private final java.lang.CharSequence mExplanation;

    private final android.net.Uri mReference;

    private final android.os.Bundle mSignals;

    private final int mUser;

    public static final java.lang.String GROUP_KEY_OVERRIDE_KEY = "group_key_override";

    public static final java.lang.String NEEDS_AUTOGROUPING_KEY = "autogroup_needed";

    /**
     * Create a notification adjustment.
     *
     * @param pkg
     * 		The package of the notification.
     * @param key
     * 		The notification key.
     * @param importance
     * 		The recommended importance of the notification.
     * @param signals
     * 		A bundle of signals that should inform notification grouping and ordering.
     * @param explanation
     * 		A human-readable justification for the adjustment.
     * @param reference
     * 		A reference to an external object that augments the
     * 		explanation, such as a
     * 		{@link android.provider.ContactsContract.Contacts#CONTENT_LOOKUP_URI},
     * 		or null.
     */
    public Adjustment(java.lang.String pkg, java.lang.String key, int importance, android.os.Bundle signals, java.lang.CharSequence explanation, android.net.Uri reference, int user) {
        mPackage = pkg;
        mKey = key;
        mImportance = importance;
        mSignals = signals;
        mExplanation = explanation;
        mReference = reference;
        mUser = user;
    }

    protected Adjustment(android.os.Parcel in) {
        if (in.readInt() == 1) {
            mPackage = in.readString();
        } else {
            mPackage = null;
        }
        if (in.readInt() == 1) {
            mKey = in.readString();
        } else {
            mKey = null;
        }
        mImportance = in.readInt();
        if (in.readInt() == 1) {
            mExplanation = in.readCharSequence();
        } else {
            mExplanation = null;
        }
        mReference = in.readParcelable(android.net.Uri.class.getClassLoader());
        mSignals = in.readBundle();
        mUser = in.readInt();
    }

    public static final android.os.Parcelable.Creator<android.service.notification.Adjustment> CREATOR = new android.os.Parcelable.Creator<android.service.notification.Adjustment>() {
        @java.lang.Override
        public android.service.notification.Adjustment createFromParcel(android.os.Parcel in) {
            return new android.service.notification.Adjustment(in);
        }

        @java.lang.Override
        public android.service.notification.Adjustment[] newArray(int size) {
            return new android.service.notification.Adjustment[size];
        }
    };

    public java.lang.String getPackage() {
        return mPackage;
    }

    public java.lang.String getKey() {
        return mKey;
    }

    public int getImportance() {
        return mImportance;
    }

    public java.lang.CharSequence getExplanation() {
        return mExplanation;
    }

    public android.net.Uri getReference() {
        return mReference;
    }

    public android.os.Bundle getSignals() {
        return mSignals;
    }

    public int getUser() {
        return mUser;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (mPackage != null) {
            dest.writeInt(1);
            dest.writeString(mPackage);
        } else {
            dest.writeInt(0);
        }
        if (mKey != null) {
            dest.writeInt(1);
            dest.writeString(mKey);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(mImportance);
        if (mExplanation != null) {
            dest.writeInt(1);
            dest.writeCharSequence(mExplanation);
        } else {
            dest.writeInt(0);
        }
        dest.writeParcelable(mReference, flags);
        dest.writeBundle(mSignals);
        dest.writeInt(mUser);
    }
}

