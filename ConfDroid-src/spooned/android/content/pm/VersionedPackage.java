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
 * Encapsulates a package and its version code.
 */
public final class VersionedPackage implements android.os.Parcelable {
    private final java.lang.String mPackageName;

    private final long mVersionCode;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntRange(from = android.content.pm.PackageManager.VERSION_CODE_HIGHEST)
    public @interface VersionCode {}

    /**
     * Creates a new instance. Use {@link PackageManager#VERSION_CODE_HIGHEST}
     * to refer to the highest version code of this package.
     *
     * @param packageName
     * 		The package name.
     * @param versionCode
     * 		The version code.
     */
    public VersionedPackage(@android.annotation.NonNull
    java.lang.String packageName, @android.content.pm.VersionedPackage.VersionCode
    int versionCode) {
        mPackageName = packageName;
        mVersionCode = versionCode;
    }

    /**
     * Creates a new instance. Use {@link PackageManager#VERSION_CODE_HIGHEST}
     * to refer to the highest version code of this package.
     *
     * @param packageName
     * 		The package name.
     * @param versionCode
     * 		The version code.
     */
    public VersionedPackage(@android.annotation.NonNull
    java.lang.String packageName, @android.content.pm.VersionedPackage.VersionCode
    long versionCode) {
        mPackageName = packageName;
        mVersionCode = versionCode;
    }

    private VersionedPackage(android.os.Parcel parcel) {
        mPackageName = parcel.readString();
        mVersionCode = parcel.readLong();
    }

    /**
     * Gets the package name.
     *
     * @return The package name.
     */
    @android.annotation.NonNull
    public java.lang.String getPackageName() {
        return mPackageName;
    }

    /**
     *
     *
     * @deprecated use {@link #getLongVersionCode()} instead.
     */
    @java.lang.Deprecated
    @android.content.pm.VersionedPackage.VersionCode
    public int getVersionCode() {
        return ((int) (mVersionCode & 0x7fffffff));
    }

    /**
     * Gets the version code.
     *
     * @return The version code.
     */
    @android.content.pm.VersionedPackage.VersionCode
    public long getLongVersionCode() {
        return mVersionCode;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("VersionedPackage[" + mPackageName) + "/") + mVersionCode) + "]";
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeString(mPackageName);
        parcel.writeLong(mVersionCode);
    }

    @android.annotation.NonNull
    public static final android.content.pm.Creator<android.content.pm.VersionedPackage> CREATOR = new android.content.pm.Creator<android.content.pm.VersionedPackage>() {
        @java.lang.Override
        public android.content.pm.VersionedPackage createFromParcel(android.os.Parcel source) {
            return new android.content.pm.VersionedPackage(source);
        }

        @java.lang.Override
        public android.content.pm.VersionedPackage[] newArray(int size) {
            return new android.content.pm.VersionedPackage[size];
        }
    };
}

