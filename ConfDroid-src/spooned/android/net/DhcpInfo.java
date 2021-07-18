/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.net;


/**
 * A simple object for retrieving the results of a DHCP request.
 */
public class DhcpInfo implements android.os.Parcelable {
    public int ipAddress;

    public int gateway;

    public int netmask;

    public int dns1;

    public int dns2;

    public int serverAddress;

    public int leaseDuration;

    public DhcpInfo() {
        super();
    }

    /**
     * copy constructor {@hide }
     */
    public DhcpInfo(android.net.DhcpInfo source) {
        if (source != null) {
            ipAddress = source.ipAddress;
            gateway = source.gateway;
            netmask = source.netmask;
            dns1 = source.dns1;
            dns2 = source.dns2;
            serverAddress = source.serverAddress;
            leaseDuration = source.leaseDuration;
        }
    }

    public java.lang.String toString() {
        java.lang.StringBuffer str = new java.lang.StringBuffer();
        str.append("ipaddr ");
        android.net.DhcpInfo.putAddress(str, ipAddress);
        str.append(" gateway ");
        android.net.DhcpInfo.putAddress(str, gateway);
        str.append(" netmask ");
        android.net.DhcpInfo.putAddress(str, netmask);
        str.append(" dns1 ");
        android.net.DhcpInfo.putAddress(str, dns1);
        str.append(" dns2 ");
        android.net.DhcpInfo.putAddress(str, dns2);
        str.append(" DHCP server ");
        android.net.DhcpInfo.putAddress(str, serverAddress);
        str.append(" lease ").append(leaseDuration).append(" seconds");
        return str.toString();
    }

    private static void putAddress(java.lang.StringBuffer buf, int addr) {
        buf.append(android.net.NetworkUtils.intToInetAddress(addr).getHostAddress());
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
        dest.writeInt(ipAddress);
        dest.writeInt(gateway);
        dest.writeInt(netmask);
        dest.writeInt(dns1);
        dest.writeInt(dns2);
        dest.writeInt(serverAddress);
        dest.writeInt(leaseDuration);
    }

    /**
     * Implement the Parcelable interface {@hide }
     */
    public static final android.os.Parcelable.Creator<android.net.DhcpInfo> CREATOR = new android.os.Parcelable.Creator<android.net.DhcpInfo>() {
        public android.net.DhcpInfo createFromParcel(android.os.Parcel in) {
            android.net.DhcpInfo info = new android.net.DhcpInfo();
            info.ipAddress = in.readInt();
            info.gateway = in.readInt();
            info.netmask = in.readInt();
            info.dns1 = in.readInt();
            info.dns2 = in.readInt();
            info.serverAddress = in.readInt();
            info.leaseDuration = in.readInt();
            return info;
        }

        public android.net.DhcpInfo[] newArray(int size) {
            return new android.net.DhcpInfo[size];
        }
    };
}

