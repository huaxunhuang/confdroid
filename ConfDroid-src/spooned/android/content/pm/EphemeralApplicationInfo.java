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
package android.content.pm;


/**
 * This class represents the state of an ephemeral app.
 *
 * @unknown 
 */
public final class EphemeralApplicationInfo implements android.os.Parcelable {
    private final android.content.pm.ApplicationInfo mApplicationInfo;

    private final java.lang.String mPackageName;

    private final java.lang.CharSequence mLabelText;

    private final java.lang.String[] mRequestedPermissions;

    private final java.lang.String[] mGrantedPermissions;

    public EphemeralApplicationInfo(android.content.pm.ApplicationInfo appInfo, java.lang.String[] requestedPermissions, java.lang.String[] grantedPermissions) {
        mApplicationInfo = appInfo;
        mPackageName = null;
        mLabelText = null;
        mRequestedPermissions = requestedPermissions;
        mGrantedPermissions = grantedPermissions;
    }

    public EphemeralApplicationInfo(java.lang.String packageName, java.lang.CharSequence label, java.lang.String[] requestedPermissions, java.lang.String[] grantedPermissions) {
        mApplicationInfo = null;
        mPackageName = packageName;
        mLabelText = label;
        mRequestedPermissions = requestedPermissions;
        mGrantedPermissions = grantedPermissions;
    }

    private EphemeralApplicationInfo(android.os.Parcel parcel) {
        mPackageName = parcel.readString();
        mLabelText = parcel.readCharSequence();
        mRequestedPermissions = parcel.readStringArray();
        mGrantedPermissions = parcel.createStringArray();
        mApplicationInfo = parcel.readParcelable(null);
    }

    @android.annotation.NonNull
    public java.lang.String getPackageName() {
        if (mApplicationInfo != null) {
            return mApplicationInfo.packageName;
        }
        return mPackageName;
    }

    @android.annotation.NonNull
    public java.lang.CharSequence loadLabel(@android.annotation.NonNull
    android.content.pm.PackageManager packageManager) {
        if (mApplicationInfo != null) {
            return mApplicationInfo.loadLabel(packageManager);
        }
        return mLabelText;
    }

    @android.annotation.NonNull
    public android.graphics.drawable.Drawable loadIcon(@android.annotation.NonNull
    android.content.pm.PackageManager packageManager) {
        if (mApplicationInfo != null) {
            return mApplicationInfo.loadIcon(packageManager);
        }
        return packageManager.getEphemeralApplicationIcon(mPackageName);
    }

    @android.annotation.Nullable
    public java.lang.String[] getRequestedPermissions() {
        return mRequestedPermissions;
    }

    @android.annotation.Nullable
    public java.lang.String[] getGrantedPermissions() {
        return mGrantedPermissions;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeString(mPackageName);
        parcel.writeCharSequence(mLabelText);
        parcel.writeStringArray(mRequestedPermissions);
        parcel.writeStringArray(mGrantedPermissions);
        parcel.writeParcelable(mApplicationInfo, flags);
    }

    public static final android.os.Parcelable.Creator<android.content.pm.EphemeralApplicationInfo> CREATOR = new android.os.Parcelable.Creator<android.content.pm.EphemeralApplicationInfo>() {
        @java.lang.Override
        public android.content.pm.EphemeralApplicationInfo createFromParcel(android.os.Parcel parcel) {
            return new android.content.pm.EphemeralApplicationInfo(parcel);
        }

        @java.lang.Override
        public android.content.pm.EphemeralApplicationInfo[] newArray(int size) {
            return new android.content.pm.EphemeralApplicationInfo[0];
        }
    };
}

