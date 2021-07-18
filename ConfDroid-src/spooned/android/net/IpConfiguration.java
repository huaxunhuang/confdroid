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
package android.net;


/**
 * A class representing a configured network.
 *
 * @unknown 
 */
public class IpConfiguration implements android.os.Parcelable {
    private static final java.lang.String TAG = "IpConfiguration";

    public enum IpAssignment {

        /* Use statically configured IP settings. Configuration can be accessed
        with staticIpConfiguration
         */
        STATIC,
        /* Use dynamically configured IP settigns */
        DHCP,
        /* no IP details are assigned, this is used to indicate
        that any existing IP settings should be retained
         */
        UNASSIGNED;}

    public android.net.IpConfiguration.IpAssignment ipAssignment;

    public android.net.StaticIpConfiguration staticIpConfiguration;

    public enum ProxySettings {

        /* No proxy is to be used. Any existing proxy settings
        should be cleared.
         */
        NONE,
        /* Use statically configured proxy. Configuration can be accessed
        with httpProxy.
         */
        STATIC,
        /* no proxy details are assigned, this is used to indicate
        that any existing proxy settings should be retained
         */
        UNASSIGNED,
        /* Use a Pac based proxy. */
        PAC;}

    public android.net.IpConfiguration.ProxySettings proxySettings;

    public android.net.ProxyInfo httpProxy;

    private void init(android.net.IpConfiguration.IpAssignment ipAssignment, android.net.IpConfiguration.ProxySettings proxySettings, android.net.StaticIpConfiguration staticIpConfiguration, android.net.ProxyInfo httpProxy) {
        this.ipAssignment = ipAssignment;
        this.proxySettings = proxySettings;
        this.staticIpConfiguration = (staticIpConfiguration == null) ? null : new android.net.StaticIpConfiguration(staticIpConfiguration);
        this.httpProxy = (httpProxy == null) ? null : new android.net.ProxyInfo(httpProxy);
    }

    public IpConfiguration() {
        init(android.net.IpConfiguration.IpAssignment.UNASSIGNED, android.net.IpConfiguration.ProxySettings.UNASSIGNED, null, null);
    }

    public IpConfiguration(android.net.IpConfiguration.IpAssignment ipAssignment, android.net.IpConfiguration.ProxySettings proxySettings, android.net.StaticIpConfiguration staticIpConfiguration, android.net.ProxyInfo httpProxy) {
        init(ipAssignment, proxySettings, staticIpConfiguration, httpProxy);
    }

    public IpConfiguration(android.net.IpConfiguration source) {
        this();
        if (source != null) {
            init(source.ipAssignment, source.proxySettings, source.staticIpConfiguration, source.httpProxy);
        }
    }

    public android.net.IpConfiguration.IpAssignment getIpAssignment() {
        return ipAssignment;
    }

    public void setIpAssignment(android.net.IpConfiguration.IpAssignment ipAssignment) {
        this.ipAssignment = ipAssignment;
    }

    public android.net.StaticIpConfiguration getStaticIpConfiguration() {
        return staticIpConfiguration;
    }

    public void setStaticIpConfiguration(android.net.StaticIpConfiguration staticIpConfiguration) {
        this.staticIpConfiguration = staticIpConfiguration;
    }

    public android.net.IpConfiguration.ProxySettings getProxySettings() {
        return proxySettings;
    }

    public void setProxySettings(android.net.IpConfiguration.ProxySettings proxySettings) {
        this.proxySettings = proxySettings;
    }

    public android.net.ProxyInfo getHttpProxy() {
        return httpProxy;
    }

    public void setHttpProxy(android.net.ProxyInfo httpProxy) {
        this.httpProxy = httpProxy;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sbuf = new java.lang.StringBuilder();
        sbuf.append("IP assignment: " + ipAssignment.toString());
        sbuf.append("\n");
        if (staticIpConfiguration != null) {
            sbuf.append("Static configuration: " + staticIpConfiguration.toString());
            sbuf.append("\n");
        }
        sbuf.append("Proxy settings: " + proxySettings.toString());
        sbuf.append("\n");
        if (httpProxy != null) {
            sbuf.append("HTTP proxy: " + httpProxy.toString());
            sbuf.append("\n");
        }
        return sbuf.toString();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof android.net.IpConfiguration)) {
            return false;
        }
        android.net.IpConfiguration other = ((android.net.IpConfiguration) (o));
        return (((this.ipAssignment == other.ipAssignment) && (this.proxySettings == other.proxySettings)) && java.util.Objects.equals(this.staticIpConfiguration, other.staticIpConfiguration)) && java.util.Objects.equals(this.httpProxy, other.httpProxy);
    }

    @java.lang.Override
    public int hashCode() {
        return (((13 + (staticIpConfiguration != null ? staticIpConfiguration.hashCode() : 0)) + (17 * ipAssignment.ordinal())) + (47 * proxySettings.ordinal())) + (83 * httpProxy.hashCode());
    }

    /**
     * Implement the Parcelable interface
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Implement the Parcelable interface
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(ipAssignment.name());
        dest.writeString(proxySettings.name());
        dest.writeParcelable(staticIpConfiguration, flags);
        dest.writeParcelable(httpProxy, flags);
    }

    /**
     * Implement the Parcelable interface
     */
    public static final android.os.Parcelable.Creator<android.net.IpConfiguration> CREATOR = new android.os.Parcelable.Creator<android.net.IpConfiguration>() {
        public android.net.IpConfiguration createFromParcel(android.os.Parcel in) {
            android.net.IpConfiguration config = new android.net.IpConfiguration();
            config.ipAssignment = android.net.IpConfiguration.IpAssignment.valueOf(in.readString());
            config.proxySettings = android.net.IpConfiguration.ProxySettings.valueOf(in.readString());
            config.staticIpConfiguration = in.readParcelable(null);
            config.httpProxy = in.readParcelable(null);
            return config;
        }

        public android.net.IpConfiguration[] newArray(int size) {
            return new android.net.IpConfiguration[size];
        }
    };
}

