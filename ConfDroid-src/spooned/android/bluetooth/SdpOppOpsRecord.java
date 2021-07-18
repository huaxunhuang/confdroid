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
 * Data representation of a Object Push Profile Server side SDP record.
 */
/**
 *
 *
 * @unknown 
 */
public class SdpOppOpsRecord implements android.os.Parcelable {
    private final java.lang.String mServiceName;

    private final int mRfcommChannel;

    private final int mL2capPsm;

    private final int mProfileVersion;

    private final byte[] mFormatsList;

    public SdpOppOpsRecord(java.lang.String serviceName, int rfcommChannel, int l2capPsm, int version, byte[] formatsList) {
        super();
        this.mServiceName = serviceName;
        this.mRfcommChannel = rfcommChannel;
        this.mL2capPsm = l2capPsm;
        this.mProfileVersion = version;
        this.mFormatsList = formatsList;
    }

    public java.lang.String getServiceName() {
        return mServiceName;
    }

    public int getRfcommChannel() {
        return mRfcommChannel;
    }

    public int getL2capPsm() {
        return mL2capPsm;
    }

    public int getProfileVersion() {
        return mProfileVersion;
    }

    public byte[] getFormatsList() {
        return mFormatsList;
    }

    @java.lang.Override
    public int describeContents() {
        /* No special objects */
        return 0;
    }

    public SdpOppOpsRecord(android.os.Parcel in) {
        this.mRfcommChannel = in.readInt();
        this.mL2capPsm = in.readInt();
        this.mProfileVersion = in.readInt();
        this.mServiceName = in.readString();
        int arrayLength = in.readInt();
        if (arrayLength > 0) {
            byte[] bytes = new byte[arrayLength];
            in.readByteArray(bytes);
            this.mFormatsList = bytes;
        } else {
            this.mFormatsList = null;
        }
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mRfcommChannel);
        dest.writeInt(mL2capPsm);
        dest.writeInt(mProfileVersion);
        dest.writeString(mServiceName);
        if ((mFormatsList != null) && (mFormatsList.length > 0)) {
            dest.writeInt(mFormatsList.length);
            dest.writeByteArray(mFormatsList);
        } else {
            dest.writeInt(0);
        }
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("Bluetooth OPP Server SDP Record:\n");
        sb.append("  RFCOMM Chan Number: ").append(mRfcommChannel);
        sb.append("\n  L2CAP PSM: ").append(mL2capPsm);
        sb.append("\n  Profile version: ").append(mProfileVersion);
        sb.append("\n  Service Name: ").append(mServiceName);
        sb.append("\n  Formats List: ").append(java.util.Arrays.toString(mFormatsList));
        return sb.toString();
    }

    public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {
        public android.bluetooth.SdpOppOpsRecord createFromParcel(android.os.Parcel in) {
            return new android.bluetooth.SdpOppOpsRecord(in);
        }

        public android.bluetooth.SdpOppOpsRecord[] newArray(int size) {
            return new android.bluetooth.SdpOppOpsRecord[size];
        }
    };
}

