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
 * Class that describes static IP configuration.
 *
 * This class is different from LinkProperties because it represents
 * configuration intent. The general contract is that if we can represent
 * a configuration here, then we should be able to configure it on a network.
 * The intent is that it closely match the UI we have for configuring networks.
 *
 * In contrast, LinkProperties represents current state. It is much more
 * expressive. For example, it supports multiple IP addresses, multiple routes,
 * stacked interfaces, and so on. Because LinkProperties is so expressive,
 * using it to represent configuration intent as well as current state causes
 * problems. For example, we could unknowingly save a configuration that we are
 * not in fact capable of applying, or we could save a configuration that the
 * UI cannot display, which has the potential for malicious code to hide
 * hostile or unexpected configuration from the user: see, for example,
 * http://b/12663469 and http://b/16893413 .
 *
 * @unknown 
 */
public class StaticIpConfiguration implements android.os.Parcelable {
    public android.net.LinkAddress ipAddress;

    public java.net.InetAddress gateway;

    public final java.util.ArrayList<java.net.InetAddress> dnsServers;

    public java.lang.String domains;

    public StaticIpConfiguration() {
        dnsServers = new java.util.ArrayList<java.net.InetAddress>();
    }

    public StaticIpConfiguration(android.net.StaticIpConfiguration source) {
        this();
        if (source != null) {
            // All of these except dnsServers are immutable, so no need to make copies.
            ipAddress = source.ipAddress;
            gateway = source.gateway;
            dnsServers.addAll(source.dnsServers);
            domains = source.domains;
        }
    }

    public void clear() {
        ipAddress = null;
        gateway = null;
        dnsServers.clear();
        domains = null;
    }

    /**
     * Returns the network routes specified by this object. Will typically include a
     * directly-connected route for the IP address's local subnet and a default route. If the
     * default gateway is not covered by the directly-connected route, it will also contain a host
     * route to the gateway as well. This configuration is arguably invalid, but it used to work
     * in K and earlier, and other OSes appear to accept it.
     */
    public java.util.List<android.net.RouteInfo> getRoutes(java.lang.String iface) {
        java.util.List<android.net.RouteInfo> routes = new java.util.ArrayList<android.net.RouteInfo>(3);
        if (ipAddress != null) {
            android.net.RouteInfo connectedRoute = new android.net.RouteInfo(ipAddress, null, iface);
            routes.add(connectedRoute);
            if ((gateway != null) && (!connectedRoute.matches(gateway))) {
                routes.add(android.net.RouteInfo.makeHostRoute(gateway, iface));
            }
        }
        if (gateway != null) {
            routes.add(new android.net.RouteInfo(((android.net.IpPrefix) (null)), gateway, iface));
        }
        return routes;
    }

    /**
     * Returns a LinkProperties object expressing the data in this object. Note that the information
     * contained in the LinkProperties will not be a complete picture of the link's configuration,
     * because any configuration information that is obtained dynamically by the network (e.g.,
     * IPv6 configuration) will not be included.
     */
    public android.net.LinkProperties toLinkProperties(java.lang.String iface) {
        android.net.LinkProperties lp = new android.net.LinkProperties();
        lp.setInterfaceName(iface);
        if (ipAddress != null) {
            lp.addLinkAddress(ipAddress);
        }
        for (android.net.RouteInfo route : getRoutes(iface)) {
            lp.addRoute(route);
        }
        for (java.net.InetAddress dns : dnsServers) {
            lp.addDnsServer(dns);
        }
        lp.setDomains(domains);
        return lp;
    }

    public java.lang.String toString() {
        java.lang.StringBuffer str = new java.lang.StringBuffer();
        str.append("IP address ");
        if (ipAddress != null)
            str.append(ipAddress).append(" ");

        str.append("Gateway ");
        if (gateway != null)
            str.append(gateway.getHostAddress()).append(" ");

        str.append(" DNS servers: [");
        for (java.net.InetAddress dnsServer : dnsServers) {
            str.append(" ").append(dnsServer.getHostAddress());
        }
        str.append(" ] Domains ");
        if (domains != null)
            str.append(domains);

        return str.toString();
    }

    public int hashCode() {
        int result = 13;
        result = (47 * result) + (ipAddress == null ? 0 : ipAddress.hashCode());
        result = (47 * result) + (gateway == null ? 0 : gateway.hashCode());
        result = (47 * result) + (domains == null ? 0 : domains.hashCode());
        result = (47 * result) + dnsServers.hashCode();
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof android.net.StaticIpConfiguration))
            return false;

        android.net.StaticIpConfiguration other = ((android.net.StaticIpConfiguration) (obj));
        return ((((other != null) && java.util.Objects.equals(ipAddress, other.ipAddress)) && java.util.Objects.equals(gateway, other.gateway)) && dnsServers.equals(other.dnsServers)) && java.util.Objects.equals(domains, other.domains);
    }

    /**
     * Implement the Parcelable interface
     */
    public static android.os.Parcelable.Creator<android.net.StaticIpConfiguration> CREATOR = new android.os.Parcelable.Creator<android.net.StaticIpConfiguration>() {
        public android.net.StaticIpConfiguration createFromParcel(android.os.Parcel in) {
            android.net.StaticIpConfiguration s = new android.net.StaticIpConfiguration();
            android.net.StaticIpConfiguration.readFromParcel(s, in);
            return s;
        }

        public android.net.StaticIpConfiguration[] newArray(int size) {
            return new android.net.StaticIpConfiguration[size];
        }
    };

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
        dest.writeParcelable(ipAddress, flags);
        android.net.NetworkUtils.parcelInetAddress(dest, gateway, flags);
        dest.writeInt(dnsServers.size());
        for (java.net.InetAddress dnsServer : dnsServers) {
            android.net.NetworkUtils.parcelInetAddress(dest, dnsServer, flags);
        }
        dest.writeString(domains);
    }

    protected static void readFromParcel(android.net.StaticIpConfiguration s, android.os.Parcel in) {
        s.ipAddress = in.readParcelable(null);
        s.gateway = android.net.NetworkUtils.unparcelInetAddress(in);
        s.dnsServers.clear();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            s.dnsServers.add(android.net.NetworkUtils.unparcelInetAddress(in));
        }
        s.domains = in.readString();
    }
}

