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
package android.content.pm.permission;


/**
 * This class contains information about how a runtime permission
 * is to be presented in the UI. A single runtime permission
 * presented to the user may correspond to multiple platform defined
 * permissions, e.g. the location permission may control both the
 * coarse and fine platform permissions.
 *
 * @unknown 
 * @deprecated Not used anymore. Use {@link android.permission.RuntimePermissionPresentationInfo}
instead
 */
@java.lang.Deprecated
@android.annotation.SystemApi
public final class RuntimePermissionPresentationInfo implements android.os.Parcelable {
    private static final int FLAG_GRANTED = 1 << 0;

    private static final int FLAG_STANDARD = 1 << 1;

    private final java.lang.CharSequence mLabel;

    private final int mFlags;

    /**
     * Creates a new instance.
     *
     * @param label
     * 		The permission label.
     * @param granted
     * 		Whether the permission is granted.
     * @param standard
     * 		Whether this is a platform-defined permission.
     */
    public RuntimePermissionPresentationInfo(java.lang.CharSequence label, boolean granted, boolean standard) {
        mLabel = label;
        int flags = 0;
        if (granted) {
            flags |= android.content.pm.permission.RuntimePermissionPresentationInfo.FLAG_GRANTED;
        }
        if (standard) {
            flags |= android.content.pm.permission.RuntimePermissionPresentationInfo.FLAG_STANDARD;
        }
        mFlags = flags;
    }

    private RuntimePermissionPresentationInfo(android.os.Parcel parcel) {
        mLabel = parcel.readCharSequence();
        mFlags = parcel.readInt();
    }

    /**
     *
     *
     * @return Whether the permission is granted.
     */
    public boolean isGranted() {
        return (mFlags & android.content.pm.permission.RuntimePermissionPresentationInfo.FLAG_GRANTED) != 0;
    }

    /**
     *
     *
     * @return Whether the permission is platform-defined.
     */
    public boolean isStandard() {
        return (mFlags & android.content.pm.permission.RuntimePermissionPresentationInfo.FLAG_STANDARD) != 0;
    }

    /**
     * Gets the permission label.
     *
     * @return The label.
     */
    @android.annotation.NonNull
    public java.lang.CharSequence getLabel() {
        return mLabel;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeCharSequence(mLabel);
        parcel.writeInt(mFlags);
    }

    @android.annotation.NonNull
    public static final android.content.pm.permission.Creator<android.content.pm.permission.RuntimePermissionPresentationInfo> CREATOR = new android.content.pm.permission.Creator<android.content.pm.permission.RuntimePermissionPresentationInfo>() {
        public android.content.pm.permission.RuntimePermissionPresentationInfo createFromParcel(android.os.Parcel source) {
            return new android.content.pm.permission.RuntimePermissionPresentationInfo(source);
        }

        public android.content.pm.permission.RuntimePermissionPresentationInfo[] newArray(int size) {
            return new android.content.pm.permission.RuntimePermissionPresentationInfo[size];
        }
    };
}

