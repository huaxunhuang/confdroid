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
package android.net.wifi.p2p.nsd;


/**
 * A class for storing service information that is advertised
 * over a Wi-Fi peer-to-peer setup
 *
 * @see WifiP2pUpnpServiceInfo
 * @see WifiP2pDnsSdServiceInfo
 */
public class WifiP2pServiceInfo implements android.os.Parcelable {
    /**
     * All service protocol types.
     */
    public static final int SERVICE_TYPE_ALL = 0;

    /**
     * DNS based service discovery protocol.
     */
    public static final int SERVICE_TYPE_BONJOUR = 1;

    /**
     * UPnP protocol.
     */
    public static final int SERVICE_TYPE_UPNP = 2;

    /**
     * WS-Discovery protocol
     *
     * @unknown 
     */
    public static final int SERVICE_TYPE_WS_DISCOVERY = 3;

    /**
     * Vendor Specific protocol
     */
    public static final int SERVICE_TYPE_VENDOR_SPECIFIC = 255;

    /**
     * the list of query string for wpa_supplicant
     *
     * e.g)
     * # IP Printing over TCP (PTR) (RDATA=MyPrinter._ipp._tcp.local.)
     * {"bonjour", "045f697070c00c000c01", "094d795072696e746572c027"
     *
     * # IP Printing over TCP (TXT) (RDATA=txtvers=1,pdl=application/postscript)
     * {"bonjour", "096d797072696e746572045f697070c00c001001",
     *  "09747874766572733d311a70646c3d6170706c69636174696f6e2f706f7374736372797074"}
     *
     * [UPnP]
     * # UPnP uuid
     * {"upnp", "10", "uuid:6859dede-8574-59ab-9332-123456789012"}
     *
     * # UPnP rootdevice
     * {"upnp", "10", "uuid:6859dede-8574-59ab-9332-123456789012::upnp:rootdevice"}
     *
     * # UPnP device
     * {"upnp", "10", "uuid:6859dede-8574-59ab-9332-123456789012::urn:schemas-upnp
     * -org:device:InternetGatewayDevice:1"}
     *
     *  # UPnP service
     * {"upnp", "10", "uuid:6859dede-8574-59ab-9322-123456789012::urn:schemas-upnp
     * -org:service:ContentDirectory:2"}
     */
    private java.util.List<java.lang.String> mQueryList;

    /**
     * This is only used in subclass.
     *
     * @param queryList
     * 		query string for wpa_supplicant
     * @unknown 
     */
    protected WifiP2pServiceInfo(java.util.List<java.lang.String> queryList) {
        if (queryList == null) {
            throw new java.lang.IllegalArgumentException("query list cannot be null");
        }
        mQueryList = queryList;
    }

    /**
     * Return the list of the query string for wpa_supplicant.
     *
     * @return the list of the query string for wpa_supplicant.
     * @unknown 
     */
    public java.util.List<java.lang.String> getSupplicantQueryList() {
        return mQueryList;
    }

    /**
     * Converts byte array to hex string.
     *
     * @param data
     * 		
     * @return hex string.
     * @unknown 
     */
    static java.lang.String bin2HexStr(byte[] data) {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        for (byte b : data) {
            java.lang.String s = null;
            try {
                s = java.lang.Integer.toHexString(b & 0xff);
            } catch (java.lang.Exception e) {
                e.printStackTrace();
                return null;
            }
            // add 0 padding
            if (s.length() == 1) {
                sb.append('0');
            }
            sb.append(s);
        }
        return sb.toString();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof android.net.wifi.p2p.nsd.WifiP2pServiceInfo)) {
            return false;
        }
        android.net.wifi.p2p.nsd.WifiP2pServiceInfo servInfo = ((android.net.wifi.p2p.nsd.WifiP2pServiceInfo) (o));
        return mQueryList.equals(servInfo.mQueryList);
    }

    @java.lang.Override
    public int hashCode() {
        int result = 17;
        result = (31 * result) + (mQueryList == null ? 0 : mQueryList.hashCode());
        return result;
    }

    /**
     * Implement the Parcelable interface {@hide }
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Implement the Parcelable interface {@hide }
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeStringList(mQueryList);
    }

    /**
     * Implement the Parcelable interface {@hide }
     */
    public static final android.os.Parcelable.Creator<android.net.wifi.p2p.nsd.WifiP2pServiceInfo> CREATOR = new android.os.Parcelable.Creator<android.net.wifi.p2p.nsd.WifiP2pServiceInfo>() {
        public android.net.wifi.p2p.nsd.WifiP2pServiceInfo createFromParcel(android.os.Parcel in) {
            java.util.List<java.lang.String> data = new java.util.ArrayList<java.lang.String>();
            in.readStringList(data);
            return new android.net.wifi.p2p.nsd.WifiP2pServiceInfo(data);
        }

        public android.net.wifi.p2p.nsd.WifiP2pServiceInfo[] newArray(int size) {
            return new android.net.wifi.p2p.nsd.WifiP2pServiceInfo[size];
        }
    };
}

