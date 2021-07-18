/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.os;


/**
 * Parcelable containing a raw Parcel of data.
 *
 * @unknown 
 */
public class ParcelableParcel implements android.os.Parcelable {
    final android.os.Parcel mParcel;

    final java.lang.ClassLoader mClassLoader;

    public ParcelableParcel(java.lang.ClassLoader loader) {
        mParcel = android.os.Parcel.obtain();
        mClassLoader = loader;
    }

    public ParcelableParcel(android.os.Parcel src, java.lang.ClassLoader loader) {
        mParcel = android.os.Parcel.obtain();
        mClassLoader = loader;
        int size = src.readInt();
        if (size < 0) {
            throw new java.lang.IllegalArgumentException("Negative size read from parcel");
        }
        int pos = src.dataPosition();
        src.setDataPosition(android.util.MathUtils.addOrThrow(pos, size));
        mParcel.appendFrom(src, pos, size);
    }

    public android.os.Parcel getParcel() {
        mParcel.setDataPosition(0);
        return mParcel;
    }

    public java.lang.ClassLoader getClassLoader() {
        return mClassLoader;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mParcel.dataSize());
        dest.appendFrom(mParcel, 0, mParcel.dataSize());
    }

    public static final android.os.Parcelable.ClassLoaderCreator<android.os.ParcelableParcel> CREATOR = new android.os.Parcelable.ClassLoaderCreator<android.os.ParcelableParcel>() {
        public android.os.ParcelableParcel createFromParcel(android.os.Parcel in) {
            return new android.os.ParcelableParcel(in, null);
        }

        public android.os.ParcelableParcel createFromParcel(android.os.Parcel in, java.lang.ClassLoader loader) {
            return new android.os.ParcelableParcel(in, loader);
        }

        public android.os.ParcelableParcel[] newArray(int size) {
            return new android.os.ParcelableParcel[size];
        }
    };
}

