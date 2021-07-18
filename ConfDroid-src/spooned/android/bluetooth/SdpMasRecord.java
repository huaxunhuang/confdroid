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
public class SdpMasRecord implements android.os.Parcelable {
    private final int mMasInstanceId;

    private final int mL2capPsm;

    private final int mRfcommChannelNumber;

    private final int mProfileVersion;

    private final int mSupportedFeatures;

    private final int mSupportedMessageTypes;

    private final java.lang.String mServiceName;

    public static final class MessageType {
        public static final int EMAIL = 0x1;

        public static final int SMS_GSM = 0x2;

        public static final int SMS_CDMA = 0x4;

        public static final int MMS = 0x8;
    }

    public SdpMasRecord(int mas_instance_id, int l2cap_psm, int rfcomm_channel_number, int profile_version, int supported_features, int supported_message_types, java.lang.String service_name) {
        this.mMasInstanceId = mas_instance_id;
        this.mL2capPsm = l2cap_psm;
        this.mRfcommChannelNumber = rfcomm_channel_number;
        this.mProfileVersion = profile_version;
        this.mSupportedFeatures = supported_features;
        this.mSupportedMessageTypes = supported_message_types;
        this.mServiceName = service_name;
    }

    public SdpMasRecord(android.os.Parcel in) {
        this.mMasInstanceId = in.readInt();
        this.mL2capPsm = in.readInt();
        this.mRfcommChannelNumber = in.readInt();
        this.mProfileVersion = in.readInt();
        this.mSupportedFeatures = in.readInt();
        this.mSupportedMessageTypes = in.readInt();
        this.mServiceName = in.readString();
    }

    @java.lang.Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getMasInstanceId() {
        return mMasInstanceId;
    }

    public int getL2capPsm() {
        return mL2capPsm;
    }

    public int getRfcommCannelNumber() {
        return mRfcommChannelNumber;
    }

    public int getProfileVersion() {
        return mProfileVersion;
    }

    public int getSupportedFeatures() {
        return mSupportedFeatures;
    }

    public int getSupportedMessageTypes() {
        return mSupportedMessageTypes;
    }

    public boolean msgSupported(int msg) {
        return (mSupportedMessageTypes & msg) != 0;
    }

    public java.lang.String getServiceName() {
        return mServiceName;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(this.mMasInstanceId);
        dest.writeInt(this.mL2capPsm);
        dest.writeInt(this.mRfcommChannelNumber);
        dest.writeInt(this.mProfileVersion);
        dest.writeInt(this.mSupportedFeatures);
        dest.writeInt(this.mSupportedMessageTypes);
        dest.writeString(this.mServiceName);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.String ret = "Bluetooth MAS SDP Record:\n";
        if (mMasInstanceId != (-1)) {
            ret += ("Mas Instance Id: " + mMasInstanceId) + "\n";
        }
        if (mRfcommChannelNumber != (-1)) {
            ret += ("RFCOMM Chan Number: " + mRfcommChannelNumber) + "\n";
        }
        if (mL2capPsm != (-1)) {
            ret += ("L2CAP PSM: " + mL2capPsm) + "\n";
        }
        if (mServiceName != null) {
            ret += ("Service Name: " + mServiceName) + "\n";
        }
        if (mProfileVersion != (-1)) {
            ret += ("Profile version: " + mProfileVersion) + "\n";
        }
        if (mSupportedMessageTypes != (-1)) {
            ret += ("Supported msg types: " + mSupportedMessageTypes) + "\n";
        }
        if (mSupportedFeatures != (-1)) {
            ret += ("Supported features: " + mSupportedFeatures) + "\n";
        }
        return ret;
    }

    public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {
        public android.bluetooth.SdpMasRecord createFromParcel(android.os.Parcel in) {
            return new android.bluetooth.SdpMasRecord(in);
        }

        public android.bluetooth.SdpRecord[] newArray(int size) {
            return new android.bluetooth.SdpRecord[size];
        }
    };
}

