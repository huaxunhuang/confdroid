/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.net.wifi.p2p;


/**
 * A class representing connection information about a Wi-Fi p2p group
 *
 * {@see WifiP2pManager}
 */
public class WifiP2pInfo implements android.os.Parcelable {
    /**
     * Indicates if a p2p group has been successfully formed
     */
    public boolean groupFormed;

    /**
     * Indicates if the current device is the group owner
     */
    public boolean isGroupOwner;

    /**
     * Group owner address
     */
    public java.net.InetAddress groupOwnerAddress;

    public WifiP2pInfo() {
    }

    public java.lang.String toString() {
        java.lang.StringBuffer sbuf = new java.lang.StringBuffer();
        sbuf.append("groupFormed: ").append(groupFormed).append(" isGroupOwner: ").append(isGroupOwner).append(" groupOwnerAddress: ").append(groupOwnerAddress);
        return sbuf.toString();
    }

    /**
     * Implement the Parcelable interface
     */
    public int describeContents() {
        return 0;
    }

    /**
     * copy constructor
     */
    public WifiP2pInfo(android.net.wifi.p2p.WifiP2pInfo source) {
        if (source != null) {
            groupFormed = source.groupFormed;
            isGroupOwner = source.isGroupOwner;
            groupOwnerAddress = source.groupOwnerAddress;
        }
    }

    /**
     * Implement the Parcelable interface
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeByte(groupFormed ? ((byte) (1)) : ((byte) (0)));
        dest.writeByte(isGroupOwner ? ((byte) (1)) : ((byte) (0)));
        if (groupOwnerAddress != null) {
            dest.writeByte(((byte) (1)));
            dest.writeByteArray(groupOwnerAddress.getAddress());
        } else {
            dest.writeByte(((byte) (0)));
        }
    }

    /**
     * Implement the Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.net.wifi.p2p.WifiP2pInfo> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.p2p.WifiP2pInfo>() {
        public android.net.wifi.p2p.WifiP2pInfo createFromParcel(android.os.Parcel in) {
            android.net.wifi.p2p.WifiP2pInfo info = new android.net.wifi.p2p.WifiP2pInfo();
            info.groupFormed = in.readByte() == 1;
            info.isGroupOwner = in.readByte() == 1;
            if (in.readByte() == 1) {
                try {
                    info.groupOwnerAddress = java.net.InetAddress.getByAddress(in.createByteArray());
                } catch (java.net.UnknownHostException e) {
                }
            }
            return info;
        }

        public android.net.wifi.p2p.WifiP2pInfo[] newArray(int size) {
            return new android.net.wifi.p2p.WifiP2pInfo[size];
        }
    };
}

