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
package android.media.projection;


/**
 *
 *
 * @unknown 
 */
public final class MediaProjectionInfo implements android.os.Parcelable {
    private final java.lang.String mPackageName;

    private final android.os.UserHandle mUserHandle;

    public MediaProjectionInfo(java.lang.String packageName, android.os.UserHandle handle) {
        mPackageName = packageName;
        mUserHandle = handle;
    }

    public MediaProjectionInfo(android.os.Parcel in) {
        mPackageName = in.readString();
        mUserHandle = android.os.UserHandle.readFromParcel(in);
    }

    public java.lang.String getPackageName() {
        return mPackageName;
    }

    public android.os.UserHandle getUserHandle() {
        return mUserHandle;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o instanceof android.media.projection.MediaProjectionInfo) {
            final android.media.projection.MediaProjectionInfo other = ((android.media.projection.MediaProjectionInfo) (o));
            return java.util.Objects.equals(other.mPackageName, mPackageName) && java.util.Objects.equals(other.mUserHandle, mUserHandle);
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mPackageName, mUserHandle);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("MediaProjectionInfo{mPackageName=" + mPackageName) + ", mUserHandle=") + mUserHandle) + "}";
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeString(mPackageName);
        android.os.UserHandle.writeToParcel(mUserHandle, out);
    }

    public static final android.os.Parcelable.Creator<android.media.projection.MediaProjectionInfo> CREATOR = new android.os.Parcelable.Creator<android.media.projection.MediaProjectionInfo>() {
        @java.lang.Override
        public android.media.projection.MediaProjectionInfo createFromParcel(android.os.Parcel in) {
            return new android.media.projection.MediaProjectionInfo(in);
        }

        @java.lang.Override
        public android.media.projection.MediaProjectionInfo[] newArray(int size) {
            return new android.media.projection.MediaProjectionInfo[size];
        }
    };
}

