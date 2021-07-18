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
package android.hardware;


/**
 * Information about a camera
 *
 * @unknown 
 */
public class CameraInfo implements android.os.Parcelable {
    // Can't parcel nested classes, so make this a top level class that composes
    // CameraInfo.
    public android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(info.facing);
        out.writeInt(info.orientation);
    }

    public void readFromParcel(android.os.Parcel in) {
        info.facing = in.readInt();
        info.orientation = in.readInt();
    }

    public static final android.os.Parcelable.Creator<android.hardware.CameraInfo> CREATOR = new android.os.Parcelable.Creator<android.hardware.CameraInfo>() {
        @java.lang.Override
        public android.hardware.CameraInfo createFromParcel(android.os.Parcel in) {
            android.hardware.CameraInfo info = new android.hardware.CameraInfo();
            info.readFromParcel(in);
            return info;
        }

        @java.lang.Override
        public android.hardware.CameraInfo[] newArray(int size) {
            return new android.hardware.CameraInfo[size];
        }
    };
}

