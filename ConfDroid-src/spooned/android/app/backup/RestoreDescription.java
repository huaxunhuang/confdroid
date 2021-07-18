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
package android.app.backup;


/**
 * Description of the available restore data for a given package.  Returned by a
 * BackupTransport in response to a request about the next available restorable
 * package.
 *
 * @see BackupTransport#nextRestorePackage()
 * @unknown 
 */
@android.annotation.SystemApi
public class RestoreDescription implements android.os.Parcelable {
    private final java.lang.String mPackageName;

    private final int mDataType;

    private static final java.lang.String NO_MORE_PACKAGES_SENTINEL = "";

    /**
     * Return this constant RestoreDescription from BackupTransport.nextRestorePackage()
     * to indicate that no more package data is available in the current restore operation.
     */
    public static final android.app.backup.RestoreDescription NO_MORE_PACKAGES = new android.app.backup.RestoreDescription(android.app.backup.RestoreDescription.NO_MORE_PACKAGES_SENTINEL, 0);

    // ---------------------------------------
    // Data type identifiers
    /**
     * This package's restore data is an original-style key/value dataset
     */
    public static final int TYPE_KEY_VALUE = 1;

    /**
     * This package's restore data is a tarball-type full data stream
     */
    public static final int TYPE_FULL_STREAM = 2;

    @java.lang.Override
    public java.lang.String toString() {
        return ((("RestoreDescription{" + mPackageName) + " : ") + (mDataType == android.app.backup.RestoreDescription.TYPE_KEY_VALUE ? "KEY_VALUE" : "STREAM")) + '}';
    }

    // ---------------------------------------
    // API
    public RestoreDescription(java.lang.String packageName, int dataType) {
        mPackageName = packageName;
        mDataType = dataType;
    }

    public java.lang.String getPackageName() {
        return mPackageName;
    }

    public int getDataType() {
        return mDataType;
    }

    // ---------------------------------------
    // Parcelable implementation - not used by transport
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeString(mPackageName);
        out.writeInt(mDataType);
    }

    public static final android.os.Parcelable.Creator<android.app.backup.RestoreDescription> CREATOR = new android.os.Parcelable.Creator<android.app.backup.RestoreDescription>() {
        public android.app.backup.RestoreDescription createFromParcel(android.os.Parcel in) {
            final android.app.backup.RestoreDescription unparceled = new android.app.backup.RestoreDescription(in);
            return android.app.backup.RestoreDescription.NO_MORE_PACKAGES_SENTINEL.equals(unparceled.mPackageName) ? android.app.backup.RestoreDescription.NO_MORE_PACKAGES : unparceled;
        }

        public android.app.backup.RestoreDescription[] newArray(int size) {
            return new android.app.backup.RestoreDescription[size];
        }
    };

    private RestoreDescription(android.os.Parcel in) {
        mPackageName = in.readString();
        mDataType = in.readInt();
    }
}

