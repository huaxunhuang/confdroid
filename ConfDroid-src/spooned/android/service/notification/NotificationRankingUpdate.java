/**
 * Copyright (C) 2014 The Android Open Source Project
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
 *
 *
 * @unknown 
 */
public class NotificationRankingUpdate implements android.os.Parcelable {
    // TODO: Support incremental updates.
    private final java.lang.String[] mKeys;

    private final java.lang.String[] mInterceptedKeys;

    private final android.os.Bundle mVisibilityOverrides;

    private final android.os.Bundle mSuppressedVisualEffects;

    private final int[] mImportance;

    private final android.os.Bundle mImportanceExplanation;

    private final android.os.Bundle mOverrideGroupKeys;

    public NotificationRankingUpdate(java.lang.String[] keys, java.lang.String[] interceptedKeys, android.os.Bundle visibilityOverrides, android.os.Bundle suppressedVisualEffects, int[] importance, android.os.Bundle explanation, android.os.Bundle overrideGroupKeys) {
        mKeys = keys;
        mInterceptedKeys = interceptedKeys;
        mVisibilityOverrides = visibilityOverrides;
        mSuppressedVisualEffects = suppressedVisualEffects;
        mImportance = importance;
        mImportanceExplanation = explanation;
        mOverrideGroupKeys = overrideGroupKeys;
    }

    public NotificationRankingUpdate(android.os.Parcel in) {
        mKeys = in.readStringArray();
        mInterceptedKeys = in.readStringArray();
        mVisibilityOverrides = in.readBundle();
        mSuppressedVisualEffects = in.readBundle();
        mImportance = new int[mKeys.length];
        in.readIntArray(mImportance);
        mImportanceExplanation = in.readBundle();
        mOverrideGroupKeys = in.readBundle();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeStringArray(mKeys);
        out.writeStringArray(mInterceptedKeys);
        out.writeBundle(mVisibilityOverrides);
        out.writeBundle(mSuppressedVisualEffects);
        out.writeIntArray(mImportance);
        out.writeBundle(mImportanceExplanation);
        out.writeBundle(mOverrideGroupKeys);
    }

    public static final android.os.Parcelable.Creator<android.service.notification.NotificationRankingUpdate> CREATOR = new android.os.Parcelable.Creator<android.service.notification.NotificationRankingUpdate>() {
        public android.service.notification.NotificationRankingUpdate createFromParcel(android.os.Parcel parcel) {
            return new android.service.notification.NotificationRankingUpdate(parcel);
        }

        public android.service.notification.NotificationRankingUpdate[] newArray(int size) {
            return new android.service.notification.NotificationRankingUpdate[size];
        }
    };

    public java.lang.String[] getOrderedKeys() {
        return mKeys;
    }

    public java.lang.String[] getInterceptedKeys() {
        return mInterceptedKeys;
    }

    public android.os.Bundle getVisibilityOverrides() {
        return mVisibilityOverrides;
    }

    public android.os.Bundle getSuppressedVisualEffects() {
        return mSuppressedVisualEffects;
    }

    public int[] getImportance() {
        return mImportance;
    }

    public android.os.Bundle getImportanceExplanation() {
        return mImportanceExplanation;
    }

    public android.os.Bundle getOverrideGroupKeys() {
        return mOverrideGroupKeys;
    }
}

