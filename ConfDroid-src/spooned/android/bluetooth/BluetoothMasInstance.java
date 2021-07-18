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
package android.bluetooth;


/**
 *
 *
 * @unknown 
 */
public final class BluetoothMasInstance implements android.os.Parcelable {
    private final int mId;

    private final java.lang.String mName;

    private final int mChannel;

    private final int mMsgTypes;

    public BluetoothMasInstance(int id, java.lang.String name, int channel, int msgTypes) {
        mId = id;
        mName = name;
        mChannel = channel;
        mMsgTypes = msgTypes;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o instanceof android.bluetooth.BluetoothMasInstance) {
            return mId == ((android.bluetooth.BluetoothMasInstance) (o)).mId;
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        return (mId + (mChannel << 8)) + (mMsgTypes << 16);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((java.lang.Integer.toString(mId) + ":") + mName) + ":") + mChannel) + ":") + java.lang.Integer.toHexString(mMsgTypes);
    }

    public int describeContents() {
        return 0;
    }

    public static final android.os.Parcelable.Creator<android.bluetooth.BluetoothMasInstance> CREATOR = new android.os.Parcelable.Creator<android.bluetooth.BluetoothMasInstance>() {
        public android.bluetooth.BluetoothMasInstance createFromParcel(android.os.Parcel in) {
            return new android.bluetooth.BluetoothMasInstance(in.readInt(), in.readString(), in.readInt(), in.readInt());
        }

        public android.bluetooth.BluetoothMasInstance[] newArray(int size) {
            return new android.bluetooth.BluetoothMasInstance[size];
        }
    };

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(mId);
        out.writeString(mName);
        out.writeInt(mChannel);
        out.writeInt(mMsgTypes);
    }

    public static final class MessageType {
        public static final int EMAIL = 0x1;

        public static final int SMS_GSM = 0x2;

        public static final int SMS_CDMA = 0x4;

        public static final int MMS = 0x8;
    }

    public int getId() {
        return mId;
    }

    public java.lang.String getName() {
        return mName;
    }

    public int getChannel() {
        return mChannel;
    }

    public int getMsgTypes() {
        return mMsgTypes;
    }

    public boolean msgSupported(int msg) {
        return (mMsgTypes & msg) != 0;
    }
}

