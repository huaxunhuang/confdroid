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
 * A class representing a Wi-Fi P2p configuration for setting up a connection
 *
 * {@see WifiP2pManager}
 */
public class WifiP2pConfig implements android.os.Parcelable {
    /**
     * The device MAC address uniquely identifies a Wi-Fi p2p device
     */
    public java.lang.String deviceAddress = "";

    /**
     * Wi-Fi Protected Setup information
     */
    public android.net.wifi.WpsInfo wps;

    /**
     *
     *
     * @unknown 
     */
    public static final int MAX_GROUP_OWNER_INTENT = 15;

    /**
     *
     *
     * @unknown 
     */
    public static final int MIN_GROUP_OWNER_INTENT = 0;

    /**
     * This is an integer value between 0 and 15 where 0 indicates the least
     * inclination to be a group owner and 15 indicates the highest inclination
     * to be a group owner.
     *
     * A value of -1 indicates the system can choose an appropriate value.
     */
    public int groupOwnerIntent = -1;

    /**
     *
     *
     * @unknown 
     */
    public int netId = android.net.wifi.p2p.WifiP2pGroup.PERSISTENT_NET_ID;

    public WifiP2pConfig() {
        // set defaults
        wps = new android.net.wifi.WpsInfo();
        wps.setup = android.net.wifi.WpsInfo.PBC;
    }

    /**
     *
     *
     * @unknown 
     */
    public void invalidate() {
        deviceAddress = "";
    }

    /**
     * P2P-GO-NEG-REQUEST 42:fc:89:a8:96:09 dev_passwd_id=4 {@hide }
     */
    public WifiP2pConfig(java.lang.String supplicantEvent) throws java.lang.IllegalArgumentException {
        java.lang.String[] tokens = supplicantEvent.split(" ");
        if ((tokens.length < 2) || (!tokens[0].equals("P2P-GO-NEG-REQUEST"))) {
            throw new java.lang.IllegalArgumentException("Malformed supplicant event");
        }
        deviceAddress = tokens[1];
        wps = new android.net.wifi.WpsInfo();
        if (tokens.length > 2) {
            java.lang.String[] nameVal = tokens[2].split("=");
            int devPasswdId;
            try {
                devPasswdId = java.lang.Integer.parseInt(nameVal[1]);
            } catch (java.lang.NumberFormatException e) {
                devPasswdId = 0;
            }
            // Based on definitions in wps/wps_defs.h
            switch (devPasswdId) {
                // DEV_PW_USER_SPECIFIED = 0x0001,
                case 0x1 :
                    wps.setup = android.net.wifi.WpsInfo.DISPLAY;
                    break;
                    // DEV_PW_PUSHBUTTON = 0x0004,
                case 0x4 :
                    wps.setup = android.net.wifi.WpsInfo.PBC;
                    break;
                    // DEV_PW_REGISTRAR_SPECIFIED = 0x0005
                case 0x5 :
                    wps.setup = android.net.wifi.WpsInfo.KEYPAD;
                    break;
                default :
                    wps.setup = android.net.wifi.WpsInfo.PBC;
                    break;
            }
        }
    }

    public java.lang.String toString() {
        java.lang.StringBuffer sbuf = new java.lang.StringBuffer();
        sbuf.append("\n address: ").append(deviceAddress);
        sbuf.append("\n wps: ").append(wps);
        sbuf.append("\n groupOwnerIntent: ").append(groupOwnerIntent);
        sbuf.append("\n persist: ").append(netId);
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
    public WifiP2pConfig(android.net.wifi.p2p.WifiP2pConfig source) {
        if (source != null) {
            deviceAddress = source.deviceAddress;
            wps = new android.net.wifi.WpsInfo(source.wps);
            groupOwnerIntent = source.groupOwnerIntent;
            netId = source.netId;
        }
    }

    /**
     * Implement the Parcelable interface
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(deviceAddress);
        dest.writeParcelable(wps, flags);
        dest.writeInt(groupOwnerIntent);
        dest.writeInt(netId);
    }

    /**
     * Implement the Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.net.wifi.p2p.WifiP2pConfig> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.p2p.WifiP2pConfig>() {
        public android.net.wifi.p2p.WifiP2pConfig createFromParcel(android.os.Parcel in) {
            android.net.wifi.p2p.WifiP2pConfig config = new android.net.wifi.p2p.WifiP2pConfig();
            config.deviceAddress = in.readString();
            config.wps = ((android.net.wifi.WpsInfo) (in.readParcelable(null)));
            config.groupOwnerIntent = in.readInt();
            config.netId = in.readInt();
            return config;
        }

        public android.net.wifi.p2p.WifiP2pConfig[] newArray(int size) {
            return new android.net.wifi.p2p.WifiP2pConfig[size];
        }
    };
}

