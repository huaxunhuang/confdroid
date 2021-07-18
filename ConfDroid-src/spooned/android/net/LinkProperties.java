/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * Describes the properties of a network link.
 *
 * A link represents a connection to a network.
 * It may have multiple addresses and multiple gateways,
 * multiple dns servers but only one http proxy and one
 * network interface.
 *
 * Note that this is just a holder of data.  Modifying it
 * does not affect live networks.
 */
public final class LinkProperties implements android.os.Parcelable {
    // The interface described by the network link.
    private java.lang.String mIfaceName;

    private java.util.ArrayList<android.net.LinkAddress> mLinkAddresses = new java.util.ArrayList<android.net.LinkAddress>();

    private java.util.ArrayList<java.net.InetAddress> mDnses = new java.util.ArrayList<java.net.InetAddress>();

    private java.lang.String mDomains;

    private java.util.ArrayList<android.net.RouteInfo> mRoutes = new java.util.ArrayList<android.net.RouteInfo>();

    private android.net.ProxyInfo mHttpProxy;

    private int mMtu;

    // in the format "rmem_min,rmem_def,rmem_max,wmem_min,wmem_def,wmem_max"
    private java.lang.String mTcpBufferSizes;

    private static final int MIN_MTU = 68;

    private static final int MIN_MTU_V6 = 1280;

    private static final int MAX_MTU = 10000;

    // Stores the properties of links that are "stacked" above this link.
    // Indexed by interface name to allow modification and to prevent duplicates being added.
    private java.util.Hashtable<java.lang.String, android.net.LinkProperties> mStackedLinks = new java.util.Hashtable<java.lang.String, android.net.LinkProperties>();

    /**
     *
     *
     * @unknown 
     */
    public static class CompareResult<T> {
        public java.util.List<T> removed = new java.util.ArrayList<T>();

        public java.util.List<T> added = new java.util.ArrayList<T>();

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.String retVal = "removed=[";
            for (T addr : removed)
                retVal += addr.toString() + ",";

            retVal += "] added=[";
            for (T addr : added)
                retVal += addr.toString() + ",";

            retVal += "]";
            return retVal;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public enum ProvisioningChange {

        STILL_NOT_PROVISIONED,
        LOST_PROVISIONING,
        GAINED_PROVISIONING,
        STILL_PROVISIONED;}

    /**
     * Compare the provisioning states of two LinkProperties instances.
     *
     * @unknown 
     */
    public static android.net.LinkProperties.ProvisioningChange compareProvisioning(android.net.LinkProperties before, android.net.LinkProperties after) {
        if (before.isProvisioned() && after.isProvisioned()) {
            // On dualstack networks, DHCPv4 renewals can occasionally fail.
            // When this happens, IPv6-reachable services continue to function
            // normally but IPv4-only services (naturally) fail.
            // 
            // When an application using an IPv4-only service reports a bad
            // network condition to the framework, attempts to re-validate
            // the network succeed (since we support IPv6-only networks) and
            // nothing is changed.
            // 
            // For users, this is confusing and unexpected behaviour, and is
            // not necessarily easy to diagnose.  Therefore, we treat changing
            // from a dualstack network to an IPv6-only network equivalent to
            // a total loss of provisioning.
            // 
            // For one such example of this, see b/18867306.
            // 
            // Additionally, losing IPv6 provisioning can result in TCP
            // connections getting stuck until timeouts fire and other
            // baffling failures. Therefore, loss of either IPv4 or IPv6 on a
            // previously dualstack network is deemed a lost of provisioning.
            if ((before.isIPv4Provisioned() && (!after.isIPv4Provisioned())) || (before.isIPv6Provisioned() && (!after.isIPv6Provisioned()))) {
                return android.net.LinkProperties.ProvisioningChange.LOST_PROVISIONING;
            }
            return android.net.LinkProperties.ProvisioningChange.STILL_PROVISIONED;
        } else
            if (before.isProvisioned() && (!after.isProvisioned())) {
                return android.net.LinkProperties.ProvisioningChange.LOST_PROVISIONING;
            } else
                if ((!before.isProvisioned()) && after.isProvisioned()) {
                    return android.net.LinkProperties.ProvisioningChange.GAINED_PROVISIONING;
                } else {
                    // !before.isProvisioned() && !after.isProvisioned()
                    return android.net.LinkProperties.ProvisioningChange.STILL_NOT_PROVISIONED;
                }


    }

    /**
     *
     *
     * @unknown 
     */
    public LinkProperties() {
    }

    /**
     *
     *
     * @unknown 
     */
    public LinkProperties(android.net.LinkProperties source) {
        if (source != null) {
            mIfaceName = source.getInterfaceName();
            for (android.net.LinkAddress l : source.getLinkAddresses())
                mLinkAddresses.add(l);

            for (java.net.InetAddress i : source.getDnsServers())
                mDnses.add(i);

            mDomains = source.getDomains();
            for (android.net.RouteInfo r : source.getRoutes())
                mRoutes.add(r);

            mHttpProxy = (source.getHttpProxy() == null) ? null : new android.net.ProxyInfo(source.getHttpProxy());
            for (android.net.LinkProperties l : source.mStackedLinks.values()) {
                addStackedLink(l);
            }
            setMtu(source.getMtu());
            mTcpBufferSizes = source.mTcpBufferSizes;
        }
    }

    /**
     * Sets the interface name for this link.  All {@link RouteInfo} already set for this
     * will have their interface changed to match this new value.
     *
     * @param iface
     * 		The name of the network interface used for this link.
     * @unknown 
     */
    public void setInterfaceName(java.lang.String iface) {
        mIfaceName = iface;
        java.util.ArrayList<android.net.RouteInfo> newRoutes = new java.util.ArrayList<android.net.RouteInfo>(mRoutes.size());
        for (android.net.RouteInfo route : mRoutes) {
            newRoutes.add(routeWithInterface(route));
        }
        mRoutes = newRoutes;
    }

    /**
     * Gets the interface name for this link.  May be {@code null} if not set.
     *
     * @return The interface name set for this link or {@code null}.
     */
    @android.annotation.Nullable
    public java.lang.String getInterfaceName() {
        return mIfaceName;
    }

    /**
     *
     *
     * @unknown 
     */
    public java.util.List<java.lang.String> getAllInterfaceNames() {
        java.util.List<java.lang.String> interfaceNames = new java.util.ArrayList<java.lang.String>(mStackedLinks.size() + 1);
        if (mIfaceName != null)
            interfaceNames.add(new java.lang.String(mIfaceName));

        for (android.net.LinkProperties stacked : mStackedLinks.values()) {
            interfaceNames.addAll(stacked.getAllInterfaceNames());
        }
        return interfaceNames;
    }

    /**
     * Returns all the addresses on this link.  We often think of a link having a single address,
     * however, particularly with Ipv6 several addresses are typical.  Note that the
     * {@code LinkProperties} actually contains {@link LinkAddress} objects which also include
     * prefix lengths for each address.  This is a simplified utility alternative to
     * {@link LinkProperties#getLinkAddresses}.
     *
     * @return An umodifiable {@link List} of {@link InetAddress} for this link.
     * @unknown 
     */
    public java.util.List<java.net.InetAddress> getAddresses() {
        java.util.List<java.net.InetAddress> addresses = new java.util.ArrayList<java.net.InetAddress>();
        for (android.net.LinkAddress linkAddress : mLinkAddresses) {
            addresses.add(linkAddress.getAddress());
        }
        return java.util.Collections.unmodifiableList(addresses);
    }

    /**
     * Returns all the addresses on this link and all the links stacked above it.
     *
     * @unknown 
     */
    public java.util.List<java.net.InetAddress> getAllAddresses() {
        java.util.List<java.net.InetAddress> addresses = new java.util.ArrayList<java.net.InetAddress>();
        for (android.net.LinkAddress linkAddress : mLinkAddresses) {
            addresses.add(linkAddress.getAddress());
        }
        for (android.net.LinkProperties stacked : mStackedLinks.values()) {
            addresses.addAll(stacked.getAllAddresses());
        }
        return addresses;
    }

    private int findLinkAddressIndex(android.net.LinkAddress address) {
        for (int i = 0; i < mLinkAddresses.size(); i++) {
            if (mLinkAddresses.get(i).isSameAddressAs(address)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Adds a {@link LinkAddress} to this {@code LinkProperties} if a {@link LinkAddress} of the
     * same address/prefix does not already exist.  If it does exist it is replaced.
     *
     * @param address
     * 		The {@code LinkAddress} to add.
     * @return true if {@code address} was added or updated, false otherwise.
     * @unknown 
     */
    public boolean addLinkAddress(android.net.LinkAddress address) {
        if (address == null) {
            return false;
        }
        int i = findLinkAddressIndex(address);
        if (i < 0) {
            // Address was not present. Add it.
            mLinkAddresses.add(address);
            return true;
        } else
            if (mLinkAddresses.get(i).equals(address)) {
                // Address was present and has same properties. Do nothing.
                return false;
            } else {
                // Address was present and has different properties. Update it.
                mLinkAddresses.set(i, address);
                return true;
            }

    }

    /**
     * Removes a {@link LinkAddress} from this {@code LinkProperties}.  Specifically, matches
     * and {@link LinkAddress} with the same address and prefix.
     *
     * @param toRemove
     * 		A {@link LinkAddress} specifying the address to remove.
     * @return true if the address was removed, false if it did not exist.
     * @unknown 
     */
    public boolean removeLinkAddress(android.net.LinkAddress toRemove) {
        int i = findLinkAddressIndex(toRemove);
        if (i >= 0) {
            mLinkAddresses.remove(i);
            return true;
        }
        return false;
    }

    /**
     * Returns all the {@link LinkAddress} on this link.  Typically a link will have
     * one IPv4 address and one or more IPv6 addresses.
     *
     * @return An unmodifiable {@link List} of {@link LinkAddress} for this link.
     */
    public java.util.List<android.net.LinkAddress> getLinkAddresses() {
        return java.util.Collections.unmodifiableList(mLinkAddresses);
    }

    /**
     * Returns all the addresses on this link and all the links stacked above it.
     *
     * @unknown 
     */
    public java.util.List<android.net.LinkAddress> getAllLinkAddresses() {
        java.util.List<android.net.LinkAddress> addresses = new java.util.ArrayList<android.net.LinkAddress>();
        addresses.addAll(mLinkAddresses);
        for (android.net.LinkProperties stacked : mStackedLinks.values()) {
            addresses.addAll(stacked.getAllLinkAddresses());
        }
        return addresses;
    }

    /**
     * Replaces the {@link LinkAddress} in this {@code LinkProperties} with
     * the given {@link Collection} of {@link LinkAddress}.
     *
     * @param addresses
     * 		The {@link Collection} of {@link LinkAddress} to set in this
     * 		object.
     * @unknown 
     */
    public void setLinkAddresses(java.util.Collection<android.net.LinkAddress> addresses) {
        mLinkAddresses.clear();
        for (android.net.LinkAddress address : addresses) {
            addLinkAddress(address);
        }
    }

    /**
     * Adds the given {@link InetAddress} to the list of DNS servers, if not present.
     *
     * @param dnsServer
     * 		The {@link InetAddress} to add to the list of DNS servers.
     * @return true if the DNS server was added, false if it was already present.
     * @unknown 
     */
    public boolean addDnsServer(java.net.InetAddress dnsServer) {
        if ((dnsServer != null) && (!mDnses.contains(dnsServer))) {
            mDnses.add(dnsServer);
            return true;
        }
        return false;
    }

    /**
     * Removes the given {@link InetAddress} from the list of DNS servers.
     *
     * @param dnsServer
     * 		The {@link InetAddress} to remove from the list of DNS servers.
     * @return true if the DNS server was removed, false if it did not exist.
     * @unknown 
     */
    public boolean removeDnsServer(java.net.InetAddress dnsServer) {
        if (dnsServer != null) {
            return mDnses.remove(dnsServer);
        }
        return false;
    }

    /**
     * Replaces the DNS servers in this {@code LinkProperties} with
     * the given {@link Collection} of {@link InetAddress} objects.
     *
     * @param addresses
     * 		The {@link Collection} of DNS servers to set in this object.
     * @unknown 
     */
    public void setDnsServers(java.util.Collection<java.net.InetAddress> dnsServers) {
        mDnses.clear();
        for (java.net.InetAddress dnsServer : dnsServers) {
            addDnsServer(dnsServer);
        }
    }

    /**
     * Returns all the {@link InetAddress} for DNS servers on this link.
     *
     * @return An umodifiable {@link List} of {@link InetAddress} for DNS servers on
    this link.
     */
    public java.util.List<java.net.InetAddress> getDnsServers() {
        return java.util.Collections.unmodifiableList(mDnses);
    }

    /**
     * Sets the DNS domain search path used on this link.
     *
     * @param domains
     * 		A {@link String} listing in priority order the comma separated
     * 		domains to search when resolving host names on this link.
     * @unknown 
     */
    public void setDomains(java.lang.String domains) {
        mDomains = domains;
    }

    /**
     * Get the DNS domains search path set for this link.
     *
     * @return A {@link String} containing the comma separated domains to search when resolving
    host names on this link.
     */
    public java.lang.String getDomains() {
        return mDomains;
    }

    /**
     * Sets the Maximum Transmission Unit size to use on this link.  This should not be used
     * unless the system default (1500) is incorrect.  Values less than 68 or greater than
     * 10000 will be ignored.
     *
     * @param mtu
     * 		The MTU to use for this link.
     * @unknown 
     */
    public void setMtu(int mtu) {
        mMtu = mtu;
    }

    /**
     * Gets any non-default MTU size set for this link.  Note that if the default is being used
     * this will return 0.
     *
     * @return The mtu value set for this link.
     * @unknown 
     */
    public int getMtu() {
        return mMtu;
    }

    /**
     * Sets the tcp buffers sizes to be used when this link is the system default.
     * Should be of the form "rmem_min,rmem_def,rmem_max,wmem_min,wmem_def,wmem_max".
     *
     * @param tcpBufferSizes
     * 		The tcp buffers sizes to use.
     * @unknown 
     */
    public void setTcpBufferSizes(java.lang.String tcpBufferSizes) {
        mTcpBufferSizes = tcpBufferSizes;
    }

    /**
     * Gets the tcp buffer sizes.
     *
     * @return the tcp buffer sizes to use when this link is the system default.
     * @unknown 
     */
    public java.lang.String getTcpBufferSizes() {
        return mTcpBufferSizes;
    }

    private android.net.RouteInfo routeWithInterface(android.net.RouteInfo route) {
        return new android.net.RouteInfo(route.getDestination(), route.getGateway(), mIfaceName, route.getType());
    }

    /**
     * Adds a {@link RouteInfo} to this {@code LinkProperties}, if not present. If the
     * {@link RouteInfo} had an interface name set and that differs from the interface set for this
     * {@code LinkProperties} an {@link IllegalArgumentException} will be thrown.  The proper
     * course is to add either un-named or properly named {@link RouteInfo}.
     *
     * @param route
     * 		A {@link RouteInfo} to add to this object.
     * @return {@code false} if the route was already present, {@code true} if it was added.
     * @unknown 
     */
    public boolean addRoute(android.net.RouteInfo route) {
        if (route != null) {
            java.lang.String routeIface = route.getInterface();
            if ((routeIface != null) && (!routeIface.equals(mIfaceName))) {
                throw new java.lang.IllegalArgumentException((("Route added with non-matching interface: " + routeIface) + " vs. ") + mIfaceName);
            }
            route = routeWithInterface(route);
            if (!mRoutes.contains(route)) {
                mRoutes.add(route);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a {@link RouteInfo} from this {@code LinkProperties}, if present. The route must
     * specify an interface and the interface must match the interface of this
     * {@code LinkProperties}, or it will not be removed.
     *
     * @return {@code true} if the route was removed, {@code false} if it was not present.
     * @unknown 
     */
    public boolean removeRoute(android.net.RouteInfo route) {
        return ((route != null) && java.util.Objects.equals(mIfaceName, route.getInterface())) && mRoutes.remove(route);
    }

    /**
     * Returns all the {@link RouteInfo} set on this link.
     *
     * @return An unmodifiable {@link List} of {@link RouteInfo} for this link.
     */
    public java.util.List<android.net.RouteInfo> getRoutes() {
        return java.util.Collections.unmodifiableList(mRoutes);
    }

    /**
     * Returns all the routes on this link and all the links stacked above it.
     *
     * @unknown 
     */
    public java.util.List<android.net.RouteInfo> getAllRoutes() {
        java.util.List<android.net.RouteInfo> routes = new java.util.ArrayList();
        routes.addAll(mRoutes);
        for (android.net.LinkProperties stacked : mStackedLinks.values()) {
            routes.addAll(stacked.getAllRoutes());
        }
        return routes;
    }

    /**
     * Sets the recommended {@link ProxyInfo} to use on this link, or {@code null} for none.
     * Note that Http Proxies are only a hint - the system recommends their use, but it does
     * not enforce it and applications may ignore them.
     *
     * @param proxy
     * 		A {@link ProxyInfo} defining the HTTP Proxy to use on this link.
     * @unknown 
     */
    public void setHttpProxy(android.net.ProxyInfo proxy) {
        mHttpProxy = proxy;
    }

    /**
     * Gets the recommended {@link ProxyInfo} (or {@code null}) set on this link.
     *
     * @return The {@link ProxyInfo} set on this link
     */
    public android.net.ProxyInfo getHttpProxy() {
        return mHttpProxy;
    }

    /**
     * Adds a stacked link.
     *
     * If there is already a stacked link with the same interfacename as link,
     * that link is replaced with link. Otherwise, link is added to the list
     * of stacked links. If link is null, nothing changes.
     *
     * @param link
     * 		The link to add.
     * @return true if the link was stacked, false otherwise.
     * @unknown 
     */
    public boolean addStackedLink(android.net.LinkProperties link) {
        if ((link != null) && (link.getInterfaceName() != null)) {
            mStackedLinks.put(link.getInterfaceName(), link);
            return true;
        }
        return false;
    }

    /**
     * Removes a stacked link.
     *
     * If there is a stacked link with the given interface name, it is
     * removed. Otherwise, nothing changes.
     *
     * @param iface
     * 		The interface name of the link to remove.
     * @return true if the link was removed, false otherwise.
     * @unknown 
     */
    public boolean removeStackedLink(java.lang.String iface) {
        if (iface != null) {
            android.net.LinkProperties removed = mStackedLinks.remove(iface);
            return removed != null;
        }
        return false;
    }

    /**
     * Returns all the links stacked on top of this link.
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public java.util.List<android.net.LinkProperties> getStackedLinks() {
        if (mStackedLinks.isEmpty()) {
            return java.util.Collections.EMPTY_LIST;
        }
        java.util.List<android.net.LinkProperties> stacked = new java.util.ArrayList<android.net.LinkProperties>();
        for (android.net.LinkProperties link : mStackedLinks.values()) {
            stacked.add(new android.net.LinkProperties(link));
        }
        return java.util.Collections.unmodifiableList(stacked);
    }

    /**
     * Clears this object to its initial state.
     *
     * @unknown 
     */
    public void clear() {
        mIfaceName = null;
        mLinkAddresses.clear();
        mDnses.clear();
        mDomains = null;
        mRoutes.clear();
        mHttpProxy = null;
        mStackedLinks.clear();
        mMtu = 0;
        mTcpBufferSizes = null;
    }

    /**
     * Implement the Parcelable interface
     */
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.String ifaceName = (mIfaceName == null) ? "" : ("InterfaceName: " + mIfaceName) + " ";
        java.lang.String linkAddresses = "LinkAddresses: [";
        for (android.net.LinkAddress addr : mLinkAddresses)
            linkAddresses += addr.toString() + ",";

        linkAddresses += "] ";
        java.lang.String dns = "DnsAddresses: [";
        for (java.net.InetAddress addr : mDnses)
            dns += addr.getHostAddress() + ",";

        dns += "] ";
        java.lang.String domainName = "Domains: " + mDomains;
        java.lang.String mtu = " MTU: " + mMtu;
        java.lang.String tcpBuffSizes = "";
        if (mTcpBufferSizes != null) {
            tcpBuffSizes = " TcpBufferSizes: " + mTcpBufferSizes;
        }
        java.lang.String routes = " Routes: [";
        for (android.net.RouteInfo route : mRoutes)
            routes += route.toString() + ",";

        routes += "] ";
        java.lang.String proxy = (mHttpProxy == null) ? "" : (" HttpProxy: " + mHttpProxy.toString()) + " ";
        java.lang.String stacked = "";
        if (mStackedLinks.values().size() > 0) {
            stacked += " Stacked: [";
            for (android.net.LinkProperties link : mStackedLinks.values()) {
                stacked += (" [" + link.toString()) + " ],";
            }
            stacked += "] ";
        }
        return ((((((((("{" + ifaceName) + linkAddresses) + routes) + dns) + domainName) + mtu) + tcpBuffSizes) + proxy) + stacked) + "}";
    }

    /**
     * Returns true if this link has an IPv4 address.
     *
     * @return {@code true} if there is an IPv4 address, {@code false} otherwise.
     * @unknown 
     */
    public boolean hasIPv4Address() {
        for (android.net.LinkAddress address : mLinkAddresses) {
            if (address.getAddress() instanceof java.net.Inet4Address) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this link or any of its stacked interfaces has an IPv4 address.
     *
     * @return {@code true} if there is an IPv4 address, {@code false} otherwise.
     */
    private boolean hasIPv4AddressOnInterface(java.lang.String iface) {
        // mIfaceName can be null.
        return (java.util.Objects.equals(iface, mIfaceName) && hasIPv4Address()) || (((iface != null) && mStackedLinks.containsKey(iface)) && mStackedLinks.get(iface).hasIPv4Address());
    }

    /**
     * Returns true if this link has a global preferred IPv6 address.
     *
     * @return {@code true} if there is a global preferred IPv6 address, {@code false} otherwise.
     * @unknown 
     */
    public boolean hasGlobalIPv6Address() {
        for (android.net.LinkAddress address : mLinkAddresses) {
            if ((address.getAddress() instanceof java.net.Inet6Address) && address.isGlobalPreferred()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this link has an IPv4 default route.
     *
     * @return {@code true} if there is an IPv4 default route, {@code false} otherwise.
     * @unknown 
     */
    public boolean hasIPv4DefaultRoute() {
        for (android.net.RouteInfo r : mRoutes) {
            if (r.isIPv4Default()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this link has an IPv6 default route.
     *
     * @return {@code true} if there is an IPv6 default route, {@code false} otherwise.
     * @unknown 
     */
    public boolean hasIPv6DefaultRoute() {
        for (android.net.RouteInfo r : mRoutes) {
            if (r.isIPv6Default()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this link has an IPv4 DNS server.
     *
     * @return {@code true} if there is an IPv4 DNS server, {@code false} otherwise.
     * @unknown 
     */
    public boolean hasIPv4DnsServer() {
        for (java.net.InetAddress ia : mDnses) {
            if (ia instanceof java.net.Inet4Address) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this link has an IPv6 DNS server.
     *
     * @return {@code true} if there is an IPv6 DNS server, {@code false} otherwise.
     * @unknown 
     */
    public boolean hasIPv6DnsServer() {
        for (java.net.InetAddress ia : mDnses) {
            if (ia instanceof java.net.Inet6Address) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this link is provisioned for global IPv4 connectivity.
     * This requires an IP address, default route, and DNS server.
     *
     * @return {@code true} if the link is provisioned, {@code false} otherwise.
     * @unknown 
     */
    public boolean isIPv4Provisioned() {
        return (hasIPv4Address() && hasIPv4DefaultRoute()) && hasIPv4DnsServer();
    }

    /**
     * Returns true if this link is provisioned for global IPv6 connectivity.
     * This requires an IP address, default route, and DNS server.
     *
     * @return {@code true} if the link is provisioned, {@code false} otherwise.
     * @unknown 
     */
    public boolean isIPv6Provisioned() {
        return (hasGlobalIPv6Address() && hasIPv6DefaultRoute()) && hasIPv6DnsServer();
    }

    /**
     * Returns true if this link is provisioned for global connectivity,
     * for at least one Internet Protocol family.
     *
     * @return {@code true} if the link is provisioned, {@code false} otherwise.
     * @unknown 
     */
    public boolean isProvisioned() {
        return isIPv4Provisioned() || isIPv6Provisioned();
    }

    /**
     * Evaluate whether the {@link InetAddress} is considered reachable.
     *
     * @return {@code true} if the given {@link InetAddress} is considered reachable,
    {@code false} otherwise.
     * @unknown 
     */
    public boolean isReachable(java.net.InetAddress ip) {
        final java.util.List<android.net.RouteInfo> allRoutes = getAllRoutes();
        // If we don't have a route to this IP address, it's not reachable.
        final android.net.RouteInfo bestRoute = android.net.RouteInfo.selectBestRoute(allRoutes, ip);
        if (bestRoute == null) {
            return false;
        }
        // TODO: better source address evaluation for destination addresses.
        if (ip instanceof java.net.Inet4Address) {
            // For IPv4, it suffices for now to simply have any address.
            return hasIPv4AddressOnInterface(bestRoute.getInterface());
        } else
            if (ip instanceof java.net.Inet6Address) {
                if (ip.isLinkLocalAddress()) {
                    // For now, just make sure link-local destinations have
                    // scopedIds set, since transmits will generally fail otherwise.
                    // TODO: verify it matches the ifindex of one of the interfaces.
                    return ((java.net.Inet6Address) (ip)).getScopeId() != 0;
                } else {
                    // For non-link-local destinations check that either the best route
                    // is directly connected or that some global preferred address exists.
                    // TODO: reconsider all cases (disconnected ULA networks, ...).
                    return (!bestRoute.hasGateway()) || hasGlobalIPv6Address();
                }
            }

        return false;
    }

    /**
     * Compares this {@code LinkProperties} interface name against the target
     *
     * @param target
     * 		LinkProperties to compare.
     * @return {@code true} if both are identical, {@code false} otherwise.
     * @unknown 
     */
    public boolean isIdenticalInterfaceName(android.net.LinkProperties target) {
        return android.text.TextUtils.equals(getInterfaceName(), target.getInterfaceName());
    }

    /**
     * Compares this {@code LinkProperties} interface addresses against the target
     *
     * @param target
     * 		LinkProperties to compare.
     * @return {@code true} if both are identical, {@code false} otherwise.
     * @unknown 
     */
    public boolean isIdenticalAddresses(android.net.LinkProperties target) {
        java.util.Collection<java.net.InetAddress> targetAddresses = target.getAddresses();
        java.util.Collection<java.net.InetAddress> sourceAddresses = getAddresses();
        return sourceAddresses.size() == targetAddresses.size() ? sourceAddresses.containsAll(targetAddresses) : false;
    }

    /**
     * Compares this {@code LinkProperties} DNS addresses against the target
     *
     * @param target
     * 		LinkProperties to compare.
     * @return {@code true} if both are identical, {@code false} otherwise.
     * @unknown 
     */
    public boolean isIdenticalDnses(android.net.LinkProperties target) {
        java.util.Collection<java.net.InetAddress> targetDnses = target.getDnsServers();
        java.lang.String targetDomains = target.getDomains();
        if (mDomains == null) {
            if (targetDomains != null)
                return false;

        } else {
            if (mDomains.equals(targetDomains) == false)
                return false;

        }
        return mDnses.size() == targetDnses.size() ? mDnses.containsAll(targetDnses) : false;
    }

    /**
     * Compares this {@code LinkProperties} Routes against the target
     *
     * @param target
     * 		LinkProperties to compare.
     * @return {@code true} if both are identical, {@code false} otherwise.
     * @unknown 
     */
    public boolean isIdenticalRoutes(android.net.LinkProperties target) {
        java.util.Collection<android.net.RouteInfo> targetRoutes = target.getRoutes();
        return mRoutes.size() == targetRoutes.size() ? mRoutes.containsAll(targetRoutes) : false;
    }

    /**
     * Compares this {@code LinkProperties} HttpProxy against the target
     *
     * @param target
     * 		LinkProperties to compare.
     * @return {@code true} if both are identical, {@code false} otherwise.
     * @unknown 
     */
    public boolean isIdenticalHttpProxy(android.net.LinkProperties target) {
        return getHttpProxy() == null ? target.getHttpProxy() == null : getHttpProxy().equals(target.getHttpProxy());
    }

    /**
     * Compares this {@code LinkProperties} stacked links against the target
     *
     * @param target
     * 		LinkProperties to compare.
     * @return {@code true} if both are identical, {@code false} otherwise.
     * @unknown 
     */
    public boolean isIdenticalStackedLinks(android.net.LinkProperties target) {
        if (!mStackedLinks.keySet().equals(target.mStackedLinks.keySet())) {
            return false;
        }
        for (android.net.LinkProperties stacked : mStackedLinks.values()) {
            // Hashtable values can never be null.
            java.lang.String iface = stacked.getInterfaceName();
            if (!stacked.equals(target.mStackedLinks.get(iface))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares this {@code LinkProperties} MTU against the target
     *
     * @param target
     * 		LinkProperties to compare.
     * @return {@code true} if both are identical, {@code false} otherwise.
     * @unknown 
     */
    public boolean isIdenticalMtu(android.net.LinkProperties target) {
        return getMtu() == target.getMtu();
    }

    /**
     * Compares this {@code LinkProperties} Tcp buffer sizes against the target.
     *
     * @param target
     * 		LinkProperties to compare.
     * @return {@code true} if both are identical, {@code false} otherwise.
     * @unknown 
     */
    public boolean isIdenticalTcpBufferSizes(android.net.LinkProperties target) {
        return java.util.Objects.equals(mTcpBufferSizes, target.mTcpBufferSizes);
    }

    /**
     * Compares this {@code LinkProperties} instance against the target
     * LinkProperties in {@code obj}. Two LinkPropertieses are equal if
     * all their fields are equal in values.
     *
     * For collection fields, such as mDnses, containsAll() is used to check
     * if two collections contains the same elements, independent of order.
     * There are two thoughts regarding containsAll()
     * 1. Duplicated elements. eg, (A, B, B) and (A, A, B) are equal.
     * 2. Worst case performance is O(n^2).
     *
     * @param obj
     * 		the object to be tested for equality.
     * @return {@code true} if both objects are equal, {@code false} otherwise.
     */
    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof android.net.LinkProperties))
            return false;

        android.net.LinkProperties target = ((android.net.LinkProperties) (obj));
        /**
         * This method does not check that stacked interfaces are equal, because
         * stacked interfaces are not so much a property of the link as a
         * description of connections between links.
         */
        return ((((((isIdenticalInterfaceName(target) && isIdenticalAddresses(target)) && isIdenticalDnses(target)) && isIdenticalRoutes(target)) && isIdenticalHttpProxy(target)) && isIdenticalStackedLinks(target)) && isIdenticalMtu(target)) && isIdenticalTcpBufferSizes(target);
    }

    /**
     * Compares the addresses in this LinkProperties with another
     * LinkProperties, examining only addresses on the base link.
     *
     * @param target
     * 		a LinkProperties with the new list of addresses
     * @return the differences between the addresses.
     * @unknown 
     */
    public android.net.LinkProperties.CompareResult<android.net.LinkAddress> compareAddresses(android.net.LinkProperties target) {
        /* Duplicate the LinkAddresses into removed, we will be removing
        address which are common between mLinkAddresses and target
        leaving the addresses that are different. And address which
        are in target but not in mLinkAddresses are placed in the
        addedAddresses.
         */
        android.net.LinkProperties.CompareResult<android.net.LinkAddress> result = new android.net.LinkProperties.CompareResult<android.net.LinkAddress>();
        result.removed = new java.util.ArrayList<android.net.LinkAddress>(mLinkAddresses);
        result.added.clear();
        if (target != null) {
            for (android.net.LinkAddress newAddress : target.getLinkAddresses()) {
                if (!result.removed.remove(newAddress)) {
                    result.added.add(newAddress);
                }
            }
        }
        return result;
    }

    /**
     * Compares the DNS addresses in this LinkProperties with another
     * LinkProperties, examining only DNS addresses on the base link.
     *
     * @param target
     * 		a LinkProperties with the new list of dns addresses
     * @return the differences between the DNS addresses.
     * @unknown 
     */
    public android.net.LinkProperties.CompareResult<java.net.InetAddress> compareDnses(android.net.LinkProperties target) {
        /* Duplicate the InetAddresses into removed, we will be removing
        dns address which are common between mDnses and target
        leaving the addresses that are different. And dns address which
        are in target but not in mDnses are placed in the
        addedAddresses.
         */
        android.net.LinkProperties.CompareResult<java.net.InetAddress> result = new android.net.LinkProperties.CompareResult<java.net.InetAddress>();
        result.removed = new java.util.ArrayList<java.net.InetAddress>(mDnses);
        result.added.clear();
        if (target != null) {
            for (java.net.InetAddress newAddress : target.getDnsServers()) {
                if (!result.removed.remove(newAddress)) {
                    result.added.add(newAddress);
                }
            }
        }
        return result;
    }

    /**
     * Compares all routes in this LinkProperties with another LinkProperties,
     * examining both the the base link and all stacked links.
     *
     * @param target
     * 		a LinkProperties with the new list of routes
     * @return the differences between the routes.
     * @unknown 
     */
    public android.net.LinkProperties.CompareResult<android.net.RouteInfo> compareAllRoutes(android.net.LinkProperties target) {
        /* Duplicate the RouteInfos into removed, we will be removing
        routes which are common between mRoutes and target
        leaving the routes that are different. And route address which
        are in target but not in mRoutes are placed in added.
         */
        android.net.LinkProperties.CompareResult<android.net.RouteInfo> result = new android.net.LinkProperties.CompareResult<android.net.RouteInfo>();
        result.removed = getAllRoutes();
        result.added.clear();
        if (target != null) {
            for (android.net.RouteInfo r : target.getAllRoutes()) {
                if (!result.removed.remove(r)) {
                    result.added.add(r);
                }
            }
        }
        return result;
    }

    /**
     * Compares all interface names in this LinkProperties with another
     * LinkProperties, examining both the the base link and all stacked links.
     *
     * @param target
     * 		a LinkProperties with the new list of interface names
     * @return the differences between the interface names.
     * @unknown 
     */
    public android.net.LinkProperties.CompareResult<java.lang.String> compareAllInterfaceNames(android.net.LinkProperties target) {
        /* Duplicate the interface names into removed, we will be removing
        interface names which are common between this and target
        leaving the interface names that are different. And interface names which
        are in target but not in this are placed in added.
         */
        android.net.LinkProperties.CompareResult<java.lang.String> result = new android.net.LinkProperties.CompareResult<java.lang.String>();
        result.removed = getAllInterfaceNames();
        result.added.clear();
        if (target != null) {
            for (java.lang.String r : target.getAllInterfaceNames()) {
                if (!result.removed.remove(r)) {
                    result.added.add(r);
                }
            }
        }
        return result;
    }

    /**
     * generate hashcode based on significant fields
     * Equal objects must produce the same hash code, while unequal objects
     * may have the same hash codes.
     */
    @java.lang.Override
    public int hashCode() {
        return ((null == mIfaceName ? 0 : (((((mIfaceName.hashCode() + (mLinkAddresses.size() * 31)) + (mDnses.size() * 37)) + (null == mDomains ? 0 : mDomains.hashCode())) + (mRoutes.size() * 41)) + (null == mHttpProxy ? 0 : mHttpProxy.hashCode())) + (mStackedLinks.hashCode() * 47)) + (mMtu * 51)) + (null == mTcpBufferSizes ? 0 : mTcpBufferSizes.hashCode());
    }

    /**
     * Implement the Parcelable interface.
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(getInterfaceName());
        dest.writeInt(mLinkAddresses.size());
        for (android.net.LinkAddress linkAddress : mLinkAddresses) {
            dest.writeParcelable(linkAddress, flags);
        }
        dest.writeInt(mDnses.size());
        for (java.net.InetAddress d : mDnses) {
            dest.writeByteArray(d.getAddress());
        }
        dest.writeString(mDomains);
        dest.writeInt(mMtu);
        dest.writeString(mTcpBufferSizes);
        dest.writeInt(mRoutes.size());
        for (android.net.RouteInfo route : mRoutes) {
            dest.writeParcelable(route, flags);
        }
        if (mHttpProxy != null) {
            dest.writeByte(((byte) (1)));
            dest.writeParcelable(mHttpProxy, flags);
        } else {
            dest.writeByte(((byte) (0)));
        }
        java.util.ArrayList<android.net.LinkProperties> stackedLinks = new java.util.ArrayList(mStackedLinks.values());
        dest.writeList(stackedLinks);
    }

    /**
     * Implement the Parcelable interface.
     */
    public static final android.os.Parcelable.Creator<android.net.LinkProperties> CREATOR = new android.os.Parcelable.Creator<android.net.LinkProperties>() {
        public android.net.LinkProperties createFromParcel(android.os.Parcel in) {
            android.net.LinkProperties netProp = new android.net.LinkProperties();
            java.lang.String iface = in.readString();
            if (iface != null) {
                netProp.setInterfaceName(iface);
            }
            int addressCount = in.readInt();
            for (int i = 0; i < addressCount; i++) {
                netProp.addLinkAddress(((android.net.LinkAddress) (in.readParcelable(null))));
            }
            addressCount = in.readInt();
            for (int i = 0; i < addressCount; i++) {
                try {
                    netProp.addDnsServer(java.net.InetAddress.getByAddress(in.createByteArray()));
                } catch (java.net.UnknownHostException e) {
                }
            }
            netProp.setDomains(in.readString());
            netProp.setMtu(in.readInt());
            netProp.setTcpBufferSizes(in.readString());
            addressCount = in.readInt();
            for (int i = 0; i < addressCount; i++) {
                netProp.addRoute(((android.net.RouteInfo) (in.readParcelable(null))));
            }
            if (in.readByte() == 1) {
                netProp.setHttpProxy(((android.net.ProxyInfo) (in.readParcelable(null))));
            }
            java.util.ArrayList<android.net.LinkProperties> stackedLinks = new java.util.ArrayList<android.net.LinkProperties>();
            in.readList(stackedLinks, android.net.LinkProperties.class.getClassLoader());
            for (android.net.LinkProperties stackedLink : stackedLinks) {
                netProp.addStackedLink(stackedLink);
            }
            return netProp;
        }

        public android.net.LinkProperties[] newArray(int size) {
            return new android.net.LinkProperties[size];
        }
    };

    /**
     * Check the valid MTU range based on IPv4 or IPv6.
     *
     * @unknown 
     */
    public static boolean isValidMtu(int mtu, boolean ipv6) {
        if (ipv6) {
            if ((mtu >= android.net.LinkProperties.MIN_MTU_V6) && (mtu <= android.net.LinkProperties.MAX_MTU))
                return true;

        } else {
            if ((mtu >= android.net.LinkProperties.MIN_MTU) && (mtu <= android.net.LinkProperties.MAX_MTU))
                return true;

        }
        return false;
    }
}

