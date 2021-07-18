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
package android.support.design.internal;


/**
 *
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class ParcelableSparseArray extends android.util.SparseArray<android.os.Parcelable> implements android.os.Parcelable {
    public ParcelableSparseArray() {
        super();
    }

    public ParcelableSparseArray(android.os.Parcel source, java.lang.ClassLoader loader) {
        super();
        int size = source.readInt();
        int[] keys = new int[size];
        source.readIntArray(keys);
        android.os.Parcelable[] values = source.readParcelableArray(loader);
        for (int i = 0; i < size; ++i) {
            put(keys[i], values[i]);
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        int size = size();
        int[] keys = new int[size];
        android.os.Parcelable[] values = new android.os.Parcelable[size];
        for (int i = 0; i < size; ++i) {
            keys[i] = keyAt(i);
            values[i] = valueAt(i);
        }
        parcel.writeInt(size);
        parcel.writeIntArray(keys);
        parcel.writeParcelableArray(values, flags);
    }

    public static final android.os.Parcelable.Creator<android.support.design.internal.ParcelableSparseArray> CREATOR = android.support.v4.os.ParcelableCompat.newCreator(new android.support.v4.os.ParcelableCompatCreatorCallbacks<android.support.design.internal.ParcelableSparseArray>() {
        @java.lang.Override
        public android.support.design.internal.ParcelableSparseArray createFromParcel(android.os.Parcel source, java.lang.ClassLoader loader) {
            return new android.support.design.internal.ParcelableSparseArray(source, loader);
        }

        @java.lang.Override
        public android.support.design.internal.ParcelableSparseArray[] newArray(int size) {
            return new android.support.design.internal.ParcelableSparseArray[size];
        }
    });
}

