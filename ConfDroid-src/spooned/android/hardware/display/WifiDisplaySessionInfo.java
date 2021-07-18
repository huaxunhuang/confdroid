/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.hardware.display;


/**
 * This class contains information regarding a wifi display session
 * (such as session id, source ip address, etc.). This is needed for
 * Wifi Display Certification process.
 * <p>
 * This object is immutable.
 * </p>
 *
 * @unknown 
 */
public final class WifiDisplaySessionInfo implements android.os.Parcelable {
    private final boolean mClient;

    private final int mSessionId;

    private final java.lang.String mGroupId;

    private final java.lang.String mPassphrase;

    private final java.lang.String mIP;

    public static final android.os.Parcelable.Creator<android.hardware.display.WifiDisplaySessionInfo> CREATOR = new android.os.Parcelable.Creator<android.hardware.display.WifiDisplaySessionInfo>() {
        @java.lang.Override
        public android.hardware.display.WifiDisplaySessionInfo createFromParcel(android.os.Parcel in) {
            boolean client = in.readInt() != 0;
            int session = in.readInt();
            java.lang.String group = in.readString();
            java.lang.String pp = in.readString();
            java.lang.String ip = in.readString();
            return new android.hardware.display.WifiDisplaySessionInfo(client, session, group, pp, ip);
        }

        @java.lang.Override
        public android.hardware.display.WifiDisplaySessionInfo[] newArray(int size) {
            return new android.hardware.display.WifiDisplaySessionInfo[size];
        }
    };

    public WifiDisplaySessionInfo() {
        this(true, 0, "", "", "");
    }

    public WifiDisplaySessionInfo(boolean client, int session, java.lang.String group, java.lang.String pp, java.lang.String ip) {
        mClient = client;
        mSessionId = session;
        mGroupId = group;
        mPassphrase = pp;
        mIP = ip;
    }

    public boolean isClient() {
        return mClient;
    }

    public int getSessionId() {
        return mSessionId;
    }

    public java.lang.String getGroupId() {
        return mGroupId;
    }

    public java.lang.String getPassphrase() {
        return mPassphrase;
    }

    public java.lang.String getIP() {
        return mIP;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mClient ? 1 : 0);
        dest.writeInt(mSessionId);
        dest.writeString(mGroupId);
        dest.writeString(mPassphrase);
        dest.writeString(mIP);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    // For debugging purposes only.
    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((("WifiDisplaySessionInfo:" + "\n    Client/Owner: ") + (mClient ? "Client" : "Owner")) + "\n    GroupId: ") + mGroupId) + "\n    Passphrase: ") + mPassphrase) + "\n    SessionId: ") + mSessionId) + "\n    IP Address: ") + mIP;
    }
}

