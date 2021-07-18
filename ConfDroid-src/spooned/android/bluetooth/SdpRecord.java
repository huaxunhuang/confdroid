/**
 * Copyright (C) 2015 Samsung System LSI
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
package android.bluetooth;


/**
 *
 *
 * @unknown 
 */
public class SdpRecord implements android.os.Parcelable {
    private final byte[] mRawData;

    private final int mRawSize;

    @java.lang.Override
    public java.lang.String toString() {
        return ((("BluetoothSdpRecord [rawData=" + java.util.Arrays.toString(mRawData)) + ", rawSize=") + mRawSize) + "]";
    }

    public SdpRecord(int size_record, byte[] record) {
        this.mRawData = record;
        this.mRawSize = size_record;
    }

    public SdpRecord(android.os.Parcel in) {
        this.mRawSize = in.readInt();
        this.mRawData = new byte[mRawSize];
        in.readByteArray(this.mRawData);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(this.mRawSize);
        dest.writeByteArray(this.mRawData);
    }

    public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {
        public android.bluetooth.SdpRecord createFromParcel(android.os.Parcel in) {
            return new android.bluetooth.SdpRecord(in);
        }

        public android.bluetooth.SdpRecord[] newArray(int size) {
            return new android.bluetooth.SdpRecord[size];
        }
    };

    public byte[] getRawData() {
        return mRawData;
    }

    public int getRawSize() {
        return mRawSize;
    }
}

