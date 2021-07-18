/**
 * Copyright (C) 2012 The Android Open Source Project
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
 *
 *
 * @unknown 
 */
public class PackageCleanItem implements android.os.Parcelable {
    public final int userId;

    public final java.lang.String packageName;

    public final boolean andCode;

    public PackageCleanItem(int userId, java.lang.String packageName, boolean andCode) {
        this.userId = userId;
        this.packageName = packageName;
        this.andCode = andCode;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        try {
            if (obj != null) {
                android.content.pm.PackageCleanItem other = ((android.content.pm.PackageCleanItem) (obj));
                return ((userId == other.userId) && packageName.equals(other.packageName)) && (andCode == other.andCode);
            }
        } catch (java.lang.ClassCastException e) {
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        int result = 17;
        result = (31 * result) + userId;
        result = (31 * result) + packageName.hashCode();
        result = (31 * result) + (andCode ? 1 : 0);
        return result;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        dest.writeInt(userId);
        dest.writeString(packageName);
        dest.writeInt(andCode ? 1 : 0);
    }

    public static final android.os.Parcelable.Creator<android.content.pm.PackageCleanItem> CREATOR = new android.os.Parcelable.Creator<android.content.pm.PackageCleanItem>() {
        public android.content.pm.PackageCleanItem createFromParcel(android.os.Parcel source) {
            return new android.content.pm.PackageCleanItem(source);
        }

        public android.content.pm.PackageCleanItem[] newArray(int size) {
            return new android.content.pm.PackageCleanItem[size];
        }
    };

    private PackageCleanItem(android.os.Parcel source) {
        userId = source.readInt();
        packageName = source.readString();
        andCode = source.readInt() != 0;
    }
}

