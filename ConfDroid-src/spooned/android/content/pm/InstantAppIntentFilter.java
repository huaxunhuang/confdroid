/**
 * Copyright (C) 2017 The Android Open Source Project
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
package android.content.pm;


/**
 * Information about an instant application intent filter.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class InstantAppIntentFilter implements android.os.Parcelable {
    private final java.lang.String mSplitName;

    /**
     * The filters used to match domain
     */
    private final java.util.List<android.content.IntentFilter> mFilters = new java.util.ArrayList<android.content.IntentFilter>();

    public InstantAppIntentFilter(@android.annotation.Nullable
    java.lang.String splitName, @android.annotation.NonNull
    java.util.List<android.content.IntentFilter> filters) {
        if ((filters == null) || (filters.size() == 0)) {
            throw new java.lang.IllegalArgumentException();
        }
        mSplitName = splitName;
        mFilters.addAll(filters);
    }

    InstantAppIntentFilter(android.os.Parcel in) {
        mSplitName = in.readString();
        /* loader */
        in.readList(mFilters, null);
    }

    public java.lang.String getSplitName() {
        return mSplitName;
    }

    public java.util.List<android.content.IntentFilter> getFilters() {
        return mFilters;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeString(mSplitName);
        out.writeList(mFilters);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.pm.InstantAppIntentFilter> CREATOR = new android.os.Parcelable.Creator<android.content.pm.InstantAppIntentFilter>() {
        @java.lang.Override
        public android.content.pm.InstantAppIntentFilter createFromParcel(android.os.Parcel in) {
            return new android.content.pm.InstantAppIntentFilter(in);
        }

        @java.lang.Override
        public android.content.pm.InstantAppIntentFilter[] newArray(int size) {
            return new android.content.pm.InstantAppIntentFilter[size];
        }
    };
}

