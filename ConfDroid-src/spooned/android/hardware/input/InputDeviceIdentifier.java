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
package android.hardware.input;


/**
 * Wrapper for passing identifying information for input devices.
 *
 * @unknown 
 */
public final class InputDeviceIdentifier implements android.os.Parcelable {
    private final java.lang.String mDescriptor;

    private final int mVendorId;

    private final int mProductId;

    public InputDeviceIdentifier(java.lang.String descriptor, int vendorId, int productId) {
        this.mDescriptor = descriptor;
        this.mVendorId = vendorId;
        this.mProductId = productId;
    }

    private InputDeviceIdentifier(android.os.Parcel src) {
        mDescriptor = src.readString();
        mVendorId = src.readInt();
        mProductId = src.readInt();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(mDescriptor);
        dest.writeInt(mVendorId);
        dest.writeInt(mProductId);
    }

    public java.lang.String getDescriptor() {
        return mDescriptor;
    }

    public int getVendorId() {
        return mVendorId;
    }

    public int getProductId() {
        return mProductId;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (!(o instanceof android.hardware.input.InputDeviceIdentifier)))
            return false;

        final android.hardware.input.InputDeviceIdentifier that = ((android.hardware.input.InputDeviceIdentifier) (o));
        return ((mVendorId == that.mVendorId) && (mProductId == that.mProductId)) && android.text.TextUtils.equals(mDescriptor, that.mDescriptor);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mDescriptor, mVendorId, mProductId);
    }

    public static final android.os.Parcelable.Creator<android.hardware.input.InputDeviceIdentifier> CREATOR = new android.os.Parcelable.Creator<android.hardware.input.InputDeviceIdentifier>() {
        @java.lang.Override
        public android.hardware.input.InputDeviceIdentifier createFromParcel(android.os.Parcel source) {
            return new android.hardware.input.InputDeviceIdentifier(source);
        }

        @java.lang.Override
        public android.hardware.input.InputDeviceIdentifier[] newArray(int size) {
            return new android.hardware.input.InputDeviceIdentifier[size];
        }
    };
}

