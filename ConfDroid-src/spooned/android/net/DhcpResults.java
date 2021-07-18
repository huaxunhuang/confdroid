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
package android.net;


/**
 * A simple object for retrieving the results of a DHCP request.
 * Optimized (attempted) for that jni interface
 * TODO - remove when DhcpInfo is deprecated.  Move the remaining api to LinkProperties.
 *
 * @unknown 
 */
public class DhcpResults extends android.net.StaticIpConfiguration {
    private static final java.lang.String TAG = "DhcpResults";

    public java.net.Inet4Address serverAddress;

    /**
     * Vendor specific information (from RFC 2132).
     */
    public java.lang.String vendorInfo;

    public int leaseDuration;

    /**
     * Link MTU option. 0 means unset.
     */
    public int mtu;

    public DhcpResults() {
        super();
    }

    public DhcpResults(android.net.StaticIpConfiguration source) {
        super(source);
    }

    /**
     * copy constructor
     */
    public DhcpResults(android.net.DhcpResults source) {
        super(source);
        if (source != null) {
            // All these are immutable, so no need to make copies.
            serverAddress = source.serverAddress;
            vendorInfo = source.vendorInfo;
            leaseDuration = source.leaseDuration;
            mtu = source.mtu;
        }
    }

    /**
     * Test if this DHCP lease includes vendor hint that network link is
     * metered, and sensitive to heavy data transfers.
     */
    public boolean hasMeteredHint() {
        if (vendorInfo != null) {
            return vendorInfo.contains("ANDROID_METERED");
        } else {
            return false;
        }
    }

    public void clear() {
        super.clear();
        vendorInfo = null;
        leaseDuration = 0;
        mtu = 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuffer str = new java.lang.StringBuffer(super.toString());
        str.append(" DHCP server ").append(serverAddress);
        str.append(" Vendor info ").append(vendorInfo);
        str.append(" lease ").append(leaseDuration).append(" seconds");
        if (mtu != 0)
            str.append(" MTU ").append(mtu);

        return str.toString();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof android.net.DhcpResults))
            return false;

        android.net.DhcpResults target = ((android.net.DhcpResults) (obj));
        return (((super.equals(((android.net.StaticIpConfiguration) (obj))) && java.util.Objects.equals(serverAddress, target.serverAddress)) && java.util.Objects.equals(vendorInfo, target.vendorInfo)) && (leaseDuration == target.leaseDuration)) && (mtu == target.mtu);
    }

    /**
     * Implement the Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.net.DhcpResults> CREATOR = new android.os.Parcelable.Creator<android.net.DhcpResults>() {
        public android.net.DhcpResults createFromParcel(android.os.Parcel in) {
            android.net.DhcpResults dhcpResults = new android.net.DhcpResults();
            android.net.DhcpResults.readFromParcel(dhcpResults, in);
            return dhcpResults;
        }

        public android.net.DhcpResults[] newArray(int size) {
            return new android.net.DhcpResults[size];
        }
    };

    /**
     * Implement the Parcelable interface
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(leaseDuration);
        dest.writeInt(mtu);
        android.net.NetworkUtils.parcelInetAddress(dest, serverAddress, flags);
        dest.writeString(vendorInfo);
    }

    private static void readFromParcel(android.net.DhcpResults dhcpResults, android.os.Parcel in) {
        android.net.StaticIpConfiguration.readFromParcel(dhcpResults, in);
        dhcpResults.leaseDuration = in.readInt();
        dhcpResults.mtu = in.readInt();
        dhcpResults.serverAddress = ((java.net.Inet4Address) (android.net.NetworkUtils.unparcelInetAddress(in)));
        dhcpResults.vendorInfo = in.readString();
    }

    // Utils for jni population - false on success
    // Not part of the superclass because they're only used by the JNI iterface to the DHCP daemon.
    public boolean setIpAddress(java.lang.String addrString, int prefixLength) {
        try {
            java.net.Inet4Address addr = ((java.net.Inet4Address) (android.net.NetworkUtils.numericToInetAddress(addrString)));
            ipAddress = new android.net.LinkAddress(addr, prefixLength);
        } catch (java.lang.IllegalArgumentException | java.lang.ClassCastException e) {
            android.util.Log.e(android.net.DhcpResults.TAG, (("setIpAddress failed with addrString " + addrString) + "/") + prefixLength);
            return true;
        }
        return false;
    }

    public boolean setGateway(java.lang.String addrString) {
        try {
            gateway = android.net.NetworkUtils.numericToInetAddress(addrString);
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Log.e(android.net.DhcpResults.TAG, "setGateway failed with addrString " + addrString);
            return true;
        }
        return false;
    }

    public boolean addDns(java.lang.String addrString) {
        if (android.text.TextUtils.isEmpty(addrString) == false) {
            try {
                dnsServers.add(android.net.NetworkUtils.numericToInetAddress(addrString));
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Log.e(android.net.DhcpResults.TAG, "addDns failed with addrString " + addrString);
                return true;
            }
        }
        return false;
    }

    public boolean setServerAddress(java.lang.String addrString) {
        try {
            serverAddress = ((java.net.Inet4Address) (android.net.NetworkUtils.numericToInetAddress(addrString)));
        } catch (java.lang.IllegalArgumentException | java.lang.ClassCastException e) {
            android.util.Log.e(android.net.DhcpResults.TAG, "setServerAddress failed with addrString " + addrString);
            return true;
        }
        return false;
    }

    public void setLeaseDuration(int duration) {
        leaseDuration = duration;
    }

    public void setVendorInfo(java.lang.String info) {
        vendorInfo = info;
    }

    public void setDomains(java.lang.String newDomains) {
        domains = newDomains;
    }
}

