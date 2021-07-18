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
package android.bluetooth;


/**
 *
 *
 * @unknown 
 */
public class SdpSapsRecord implements android.os.Parcelable {
    private final int mRfcommChannelNumber;

    private final int mProfileVersion;

    private final java.lang.String mServiceName;

    public SdpSapsRecord(int rfcomm_channel_number, int profile_version, java.lang.String service_name) {
        this.mRfcommChannelNumber = rfcomm_channel_number;
        this.mProfileVersion = profile_version;
        this.mServiceName = service_name;
    }

    public SdpSapsRecord(android.os.Parcel in) {
        this.mRfcommChannelNumber = in.readInt();
        this.mProfileVersion = in.readInt();
        this.mServiceName = in.readString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    public int getRfcommCannelNumber() {
        return mRfcommChannelNumber;
    }

    public int getProfileVersion() {
        return mProfileVersion;
    }

    public java.lang.String getServiceName() {
        return mServiceName;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(this.mRfcommChannelNumber);
        dest.writeInt(this.mProfileVersion);
        dest.writeString(this.mServiceName);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.String ret = "Bluetooth MAS SDP Record:\n";
        if (mRfcommChannelNumber != (-1)) {
            ret += ("RFCOMM Chan Number: " + mRfcommChannelNumber) + "\n";
        }
        if (mServiceName != null) {
            ret += ("Service Name: " + mServiceName) + "\n";
        }
        if (mProfileVersion != (-1)) {
            ret += ("Profile version: " + mProfileVersion) + "\n";
        }
        return ret;
    }

    public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {
        public android.bluetooth.SdpSapsRecord createFromParcel(android.os.Parcel in) {
            return new android.bluetooth.SdpSapsRecord(in);
        }

        public android.bluetooth.SdpRecord[] newArray(int size) {
            return new android.bluetooth.SdpRecord[size];
        }
    };
}

