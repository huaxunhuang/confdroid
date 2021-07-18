/**
 * Copyright (C) 2020 The Android Open Source Project
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
 * Equivalent to List<ProviderInfo>, but it "squashes" the ApplicationInfo in the elements.
 *
 * @unknown 
 */
@android.annotation.TestApi
public final class ProviderInfoList implements android.os.Parcelable {
    private final java.util.List<android.content.pm.ProviderInfo> mList;

    private ProviderInfoList(android.os.Parcel source) {
        final java.util.ArrayList<android.content.pm.ProviderInfo> list = new java.util.ArrayList<>();
        source.readTypedList(list, this.CREATOR);
        mList = list;
    }

    private ProviderInfoList(java.util.List<android.content.pm.ProviderInfo> list) {
        mList = list;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(@android.annotation.NonNull
    android.os.Parcel dest, int flags) {
        // Allow ApplicationInfo to be squashed.
        final boolean prevAllowSquashing = dest.allowSquashing();
        dest.writeTypedList(mList, flags);
        dest.restoreAllowSquashing(prevAllowSquashing);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.pm.ProviderInfoList> CREATOR = new android.os.Parcelable.Creator<android.content.pm.ProviderInfoList>() {
        @java.lang.Override
        public android.content.pm.ProviderInfoList createFromParcel(@android.annotation.NonNull
        android.os.Parcel source) {
            return new android.content.pm.ProviderInfoList(source);
        }

        @java.lang.Override
        public android.content.pm.ProviderInfoList[] newArray(int size) {
            return new android.content.pm.ProviderInfoList[size];
        }
    };

    /**
     * Return the stored list.
     */
    @android.annotation.NonNull
    public java.util.List<android.content.pm.ProviderInfo> getList() {
        return mList;
    }

    /**
     * Create a new instance with a {@code list}. The passed list will be shared with the new
     * instance, so the caller shouldn't modify it.
     */
    @android.annotation.NonNull
    public static android.content.pm.ProviderInfoList fromList(@android.annotation.NonNull
    java.util.List<android.content.pm.ProviderInfo> list) {
        return new android.content.pm.ProviderInfoList(list);
    }
}

