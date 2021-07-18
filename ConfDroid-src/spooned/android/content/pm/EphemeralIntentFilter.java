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
package android.content.pm;


/**
 * Information about an ephemeral application intent filter.
 *
 * @unknown 
 * @unknown 
 */
@java.lang.Deprecated
@android.annotation.SystemApi
public final class EphemeralIntentFilter implements android.os.Parcelable {
    private final android.content.pm.InstantAppIntentFilter mInstantAppIntentFilter;

    public EphemeralIntentFilter(@android.annotation.Nullable
    java.lang.String splitName, @android.annotation.NonNull
    java.util.List<android.content.IntentFilter> filters) {
        mInstantAppIntentFilter = new android.content.pm.InstantAppIntentFilter(splitName, filters);
    }

    EphemeralIntentFilter(@android.annotation.NonNull
    android.content.pm.InstantAppIntentFilter intentFilter) {
        mInstantAppIntentFilter = intentFilter;
    }

    EphemeralIntentFilter(android.os.Parcel in) {
        mInstantAppIntentFilter = /* loader */
        in.readParcelable(null);
    }

    public java.lang.String getSplitName() {
        return mInstantAppIntentFilter.getSplitName();
    }

    public java.util.List<android.content.IntentFilter> getFilters() {
        return mInstantAppIntentFilter.getFilters();
    }

    /**
     *
     *
     * @unknown 
     */
    android.content.pm.InstantAppIntentFilter getInstantAppIntentFilter() {
        return mInstantAppIntentFilter;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeParcelable(mInstantAppIntentFilter, flags);
    }

    public static final android.os.Parcelable.Creator<android.content.pm.EphemeralIntentFilter> CREATOR = new android.os.Parcelable.Creator<android.content.pm.EphemeralIntentFilter>() {
        @java.lang.Override
        public android.content.pm.EphemeralIntentFilter createFromParcel(android.os.Parcel in) {
            return new android.content.pm.EphemeralIntentFilter(in);
        }

        @java.lang.Override
        public android.content.pm.EphemeralIntentFilter[] newArray(int size) {
            return new android.content.pm.EphemeralIntentFilter[size];
        }
    };
}

