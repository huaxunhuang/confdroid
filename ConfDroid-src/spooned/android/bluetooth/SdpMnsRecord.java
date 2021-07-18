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
public class SdpMnsRecord implements android.os.Parcelable {
    private final int mL2capPsm;

    private final int mRfcommChannelNumber;

    private final int mSupportedFeatures;

    private final int mProfileVersion;

    private final java.lang.String mServiceName;

    public SdpMnsRecord(int l2cap_psm, int rfcomm_channel_number, int profile_version, int supported_features, java.lang.String service_name) {
        this.mL2capPsm = l2cap_psm;
        this.mRfcommChannelNumber = rfcomm_channel_number;
        this.mSupportedFeatures = supported_features;
        this.mServiceName = service_name;
        this.mProfileVersion = profile_version;
    }

    public SdpMnsRecord(android.os.Parcel in) {
        this.mRfcommChannelNumber = in.readInt();
        this.mL2capPsm = in.readInt();
        this.mServiceName = in.readString();
        this.mSupportedFeatures = in.readInt();
        this.mProfileVersion = in.readInt();
    }

    @java.lang.Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getL2capPsm() {
        return mL2capPsm;
    }

    public int getRfcommChannelNumber() {
        return mRfcommChannelNumber;
    }

    public int getSupportedFeatures() {
        return mSupportedFeatures;
    }

    public java.lang.String getServiceName() {
        return mServiceName;
    }

    public int getProfileVersion() {
        return mProfileVersion;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mRfcommChannelNumber);
        dest.writeInt(mL2capPsm);
        dest.writeString(mServiceName);
        dest.writeInt(mSupportedFeatures);
        dest.writeInt(mProfileVersion);
    }

    public java.lang.String toString() {
        java.lang.String ret = "Bluetooth MNS SDP Record:\n";
        if (mRfcommChannelNumber != (-1)) {
            ret += ("RFCOMM Chan Number: " + mRfcommChannelNumber) + "\n";
        }
        if (mL2capPsm != (-1)) {
            ret += ("L2CAP PSM: " + mL2capPsm) + "\n";
        }
        if (mServiceName != null) {
            ret += ("Service Name: " + mServiceName) + "\n";
        }
        if (mSupportedFeatures != (-1)) {
            ret += ("Supported features: " + mSupportedFeatures) + "\n";
        }
        if (mProfileVersion != (-1)) {
            ret += ("Profile_version: " + mProfileVersion) + "\n";
        }
        return ret;
    }

    public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {
        public android.bluetooth.SdpMnsRecord createFromParcel(android.os.Parcel in) {
            return new android.bluetooth.SdpMnsRecord(in);
        }

        public android.bluetooth.SdpMnsRecord[] newArray(int size) {
            return new android.bluetooth.SdpMnsRecord[size];
        }
    };
}

