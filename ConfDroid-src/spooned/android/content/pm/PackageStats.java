/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * implementation of PackageStats associated with a application package.
 *
 * @deprecated this class is an orphan that could never be obtained from a valid
public API. If you need package storage statistics use the new
{@link StorageStatsManager} APIs.
 */
@java.lang.Deprecated
public class PackageStats implements android.os.Parcelable {
    /**
     * Name of the package to which this stats applies.
     */
    public java.lang.String packageName;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    public int userHandle;

    /**
     * Size of the code (e.g., APK)
     */
    public long codeSize;

    /**
     * Size of the internal data size for the application. (e.g.,
     * /data/data/<app>)
     */
    public long dataSize;

    /**
     * Size of cache used by the application. (e.g., /data/data/<app>/cache)
     */
    public long cacheSize;

    /**
     * Size of the secure container on external storage holding the
     * application's code.
     */
    public long externalCodeSize;

    /**
     * Size of the external data used by the application (e.g.,
     * <sdcard>/Android/data/<app>)
     */
    public long externalDataSize;

    /**
     * Size of the external cache used by the application (i.e., on the SD
     * card). If this is a subdirectory of the data directory, this size will be
     * subtracted out of the external data size.
     */
    public long externalCacheSize;

    /**
     * Size of the external media size used by the application.
     */
    public long externalMediaSize;

    /**
     * Size of the package's OBBs placed on external media.
     */
    public long externalObbSize;

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.pm.PackageStats> CREATOR = new android.os.Parcelable.Creator<android.content.pm.PackageStats>() {
        public android.content.pm.PackageStats createFromParcel(android.os.Parcel in) {
            return new android.content.pm.PackageStats(in);
        }

        public android.content.pm.PackageStats[] newArray(int size) {
            return new android.content.pm.PackageStats[size];
        }
    };

    public java.lang.String toString() {
        final java.lang.StringBuilder sb = new java.lang.StringBuilder("PackageStats{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(" ");
        sb.append(packageName);
        if (codeSize != 0) {
            sb.append(" code=");
            sb.append(codeSize);
        }
        if (dataSize != 0) {
            sb.append(" data=");
            sb.append(dataSize);
        }
        if (cacheSize != 0) {
            sb.append(" cache=");
            sb.append(cacheSize);
        }
        if (externalCodeSize != 0) {
            sb.append(" extCode=");
            sb.append(externalCodeSize);
        }
        if (externalDataSize != 0) {
            sb.append(" extData=");
            sb.append(externalDataSize);
        }
        if (externalCacheSize != 0) {
            sb.append(" extCache=");
            sb.append(externalCacheSize);
        }
        if (externalMediaSize != 0) {
            sb.append(" media=");
            sb.append(externalMediaSize);
        }
        if (externalObbSize != 0) {
            sb.append(" obb=");
            sb.append(externalObbSize);
        }
        sb.append("}");
        return sb.toString();
    }

    public PackageStats(java.lang.String pkgName) {
        packageName = pkgName;
        userHandle = android.os.UserHandle.myUserId();
    }

    /**
     *
     *
     * @unknown 
     */
    public PackageStats(java.lang.String pkgName, int userHandle) {
        this.packageName = pkgName;
        this.userHandle = userHandle;
    }

    public PackageStats(android.os.Parcel source) {
        packageName = source.readString();
        userHandle = source.readInt();
        codeSize = source.readLong();
        dataSize = source.readLong();
        cacheSize = source.readLong();
        externalCodeSize = source.readLong();
        externalDataSize = source.readLong();
        externalCacheSize = source.readLong();
        externalMediaSize = source.readLong();
        externalObbSize = source.readLong();
    }

    public PackageStats(android.content.pm.PackageStats pStats) {
        packageName = pStats.packageName;
        userHandle = pStats.userHandle;
        codeSize = pStats.codeSize;
        dataSize = pStats.dataSize;
        cacheSize = pStats.cacheSize;
        externalCodeSize = pStats.externalCodeSize;
        externalDataSize = pStats.externalDataSize;
        externalCacheSize = pStats.externalCacheSize;
        externalMediaSize = pStats.externalMediaSize;
        externalObbSize = pStats.externalObbSize;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        dest.writeString(packageName);
        dest.writeInt(userHandle);
        dest.writeLong(codeSize);
        dest.writeLong(dataSize);
        dest.writeLong(cacheSize);
        dest.writeLong(externalCodeSize);
        dest.writeLong(externalDataSize);
        dest.writeLong(externalCacheSize);
        dest.writeLong(externalMediaSize);
        dest.writeLong(externalObbSize);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof android.content.pm.PackageStats)) {
            return false;
        }
        final android.content.pm.PackageStats otherStats = ((android.content.pm.PackageStats) (obj));
        return ((((((((android.text.TextUtils.equals(packageName, otherStats.packageName) && (userHandle == otherStats.userHandle)) && (codeSize == otherStats.codeSize)) && (dataSize == otherStats.dataSize)) && (cacheSize == otherStats.cacheSize)) && (externalCodeSize == otherStats.externalCodeSize)) && (externalDataSize == otherStats.externalDataSize)) && (externalCacheSize == otherStats.externalCacheSize)) && (externalMediaSize == otherStats.externalMediaSize)) && (externalObbSize == otherStats.externalObbSize);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(packageName, userHandle, codeSize, dataSize, cacheSize, externalCodeSize, externalDataSize, externalCacheSize, externalMediaSize, externalObbSize);
    }
}

