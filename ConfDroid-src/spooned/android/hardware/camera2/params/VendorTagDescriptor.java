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
package android.hardware.camera2.params;


/**
 * A class for describing the vendor tags declared by a camera HAL module.
 * Generally only used by the native side of
 * android.hardware.camera2.impl.CameraMetadataNative
 *
 * @unknown 
 */
public final class VendorTagDescriptor implements android.os.Parcelable {
    private VendorTagDescriptor(android.os.Parcel source) {
    }

    public static final android.os.Parcelable.Creator<android.hardware.camera2.params.VendorTagDescriptor> CREATOR = new android.os.Parcelable.Creator<android.hardware.camera2.params.VendorTagDescriptor>() {
        @java.lang.Override
        public android.hardware.camera2.params.VendorTagDescriptor createFromParcel(android.os.Parcel source) {
            try {
                android.hardware.camera2.params.VendorTagDescriptor vendorDescriptor = new android.hardware.camera2.params.VendorTagDescriptor(source);
                return vendorDescriptor;
            } catch (java.lang.Exception e) {
                android.util.Log.e(android.hardware.camera2.params.VendorTagDescriptor.TAG, "Exception creating VendorTagDescriptor from parcel", e);
                return null;
            }
        }

        @java.lang.Override
        public android.hardware.camera2.params.VendorTagDescriptor[] newArray(int size) {
            return new android.hardware.camera2.params.VendorTagDescriptor[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (dest == null) {
            throw new java.lang.IllegalArgumentException("dest must not be null");
        }
    }

    private static final java.lang.String TAG = "VendorTagDescriptor";
}

