/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * A convenience class for accessing the user and default proxy
 * settings.
 */
public final class Proxy {
    private static final java.lang.String TAG = "Proxy";

    private static final java.net.ProxySelector sDefaultProxySelector;

    /**
     * Used to notify an app that's caching the proxy that either the default
     * connection has changed or any connection's proxy has changed. The new
     * proxy should be queried using {@link ConnectivityManager#getDefaultProxy()}.
     *
     * <p class="note">This is a protected intent that can only be sent by the system
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final java.lang.String PROXY_CHANGE_ACTION = "android.intent.action.PROXY_CHANGE";

    /**
     * Intent extra included with {@link #PROXY_CHANGE_ACTION} intents.
     * It describes the new proxy being used (as a {@link ProxyInfo} object).
     *
     * @deprecated Because {@code PROXY_CHANGE_ACTION} is sent whenever the proxy
    for any network on the system changes, applications should always use
    {@link ConnectivityManager#getDefaultProxy()} or
    {@link ConnectivityManager#getLinkProperties(Network)}.{@link LinkProperties#getHttpProxy()}
    to get the proxy for the Network(s) they are using.
     */
    public static final java.lang.String EXTRA_PROXY_INFO = "android.intent.extra.PROXY_INFO";

    /**
     *
     *
     * @unknown 
     */
    public static final int PROXY_VALID = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROXY_HOSTNAME_EMPTY = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROXY_HOSTNAME_INVALID = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROXY_PORT_EMPTY = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROXY_PORT_INVALID = 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROXY_EXCLLIST_INVALID = 5;

    private static android.net.ConnectivityManager sConnectivityManager = null;

    // Hostname / IP REGEX validation
    // Matches blank input, ips, and domain names
    private static final java.lang.String NAME_IP_REGEX = "[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*(\\.[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)*)*";

    private static final java.lang.String HOSTNAME_REGEXP = ("^$|^" + android.net.Proxy.NAME_IP_REGEX) + "$";

    private static final java.util.regex.Pattern HOSTNAME_PATTERN;

    private static final java.lang.String EXCL_REGEX = "[a-zA-Z0-9*]+(\\-[a-zA-Z0-9*]+)*(\\.[a-zA-Z0-9*]+(\\-[a-zA-Z0-9*]+)*)*";

    private static final java.lang.String EXCLLIST_REGEXP = ((("^$|^" + android.net.Proxy.EXCL_REGEX) + "(,") + android.net.Proxy.EXCL_REGEX) + ")*$";

    private static final java.util.regex.Pattern EXCLLIST_PATTERN;

    static {
        HOSTNAME_PATTERN = java.util.regex.Pattern.compile(android.net.Proxy.HOSTNAME_REGEXP);
        EXCLLIST_PATTERN = java.util.regex.Pattern.compile(android.net.Proxy.EXCLLIST_REGEXP);
        sDefaultProxySelector = java.net.ProxySelector.getDefault();
    }

    /**
     * Return the proxy object to be used for the URL given as parameter.
     *
     * @param ctx
     * 		A Context used to get the settings for the proxy host.
     * @param url
     * 		A URL to be accessed. Used to evaluate exclusion list.
     * @return Proxy (java.net) object containing the host name. If the
    user did not set a hostname it returns the default host.
    A null value means that no host is to be used.
    {@hide }
     */
    public static final java.net.Proxy getProxy(android.content.Context ctx, java.lang.String url) {
        java.lang.String host = "";
        if ((url != null) && (!android.net.Proxy.isLocalHost(host))) {
            java.net.URI uri = java.net.URI.create(url);
            java.net.ProxySelector proxySelector = java.net.ProxySelector.getDefault();
            java.util.List<java.net.Proxy> proxyList = proxySelector.select(uri);
            if (proxyList.size() > 0) {
                return proxyList.get(0);
            }
        }
        return java.net.Proxy.NO_PROXY;
    }

    /**
     * Return the proxy host set by the user.
     *
     * @param ctx
     * 		A Context used to get the settings for the proxy host.
     * @return String containing the host name. If the user did not set a host
    name it returns the default host. A null value means that no
    host is to be used.
     * @deprecated Use standard java vm proxy values to find the host, port
    and exclusion list.  This call ignores the exclusion list.
     */
    public static final java.lang.String getHost(android.content.Context ctx) {
        java.net.Proxy proxy = android.net.Proxy.getProxy(ctx, null);
        if (proxy == java.net.Proxy.NO_PROXY)
            return null;

        try {
            return ((java.net.InetSocketAddress) (proxy.address())).getHostName();
        } catch (java.lang.Exception e) {
            return null;
        }
    }

    /**
     * Return the proxy port set by the user.
     *
     * @param ctx
     * 		A Context used to get the settings for the proxy port.
     * @return The port number to use or -1 if no proxy is to be used.
     * @deprecated Use standard java vm proxy values to find the host, port
    and exclusion list.  This call ignores the exclusion list.
     */
    public static final int getPort(android.content.Context ctx) {
        java.net.Proxy proxy = android.net.Proxy.getProxy(ctx, null);
        if (proxy == java.net.Proxy.NO_PROXY)
            return -1;

        try {
            return ((java.net.InetSocketAddress) (proxy.address())).getPort();
        } catch (java.lang.Exception e) {
            return -1;
        }
    }

    /**
     * Return the default proxy host specified by the carrier.
     *
     * @return String containing the host name or null if there is no proxy for
    this carrier.
     * @deprecated Use standard java vm proxy values to find the host, port and
    exclusion list.  This call ignores the exclusion list and no
    longer reports only mobile-data apn-based proxy values.
     */
    public static final java.lang.String getDefaultHost() {
        java.lang.String host = java.lang.System.getProperty("http.proxyHost");
        if (android.text.TextUtils.isEmpty(host))
            return null;

        return host;
    }

    /**
     * Return the default proxy port specified by the carrier.
     *
     * @return The port number to be used with the proxy host or -1 if there is
    no proxy for this carrier.
     * @deprecated Use standard java vm proxy values to find the host, port and
    exclusion list.  This call ignores the exclusion list and no
    longer reports only mobile-data apn-based proxy values.
     */
    public static final int getDefaultPort() {
        if (android.net.Proxy.getDefaultHost() == null)
            return -1;

        try {
            return java.lang.Integer.parseInt(java.lang.System.getProperty("http.proxyPort"));
        } catch (java.lang.NumberFormatException e) {
            return -1;
        }
    }

    private static final boolean isLocalHost(java.lang.String host) {
        if (host == null) {
            return false;
        }
        try {
            if (host != null) {
                if (host.equalsIgnoreCase("localhost")) {
                    return true;
                }
                if (android.net.NetworkUtils.numericToInetAddress(host).isLoopbackAddress()) {
                    return true;
                }
            }
        } catch (java.lang.IllegalArgumentException iex) {
        }
        return false;
    }

    /**
     * Validate syntax of hostname, port and exclusion list entries
     * {@hide }
     */
    public static int validate(java.lang.String hostname, java.lang.String port, java.lang.String exclList) {
        java.util.regex.Matcher match = android.net.Proxy.HOSTNAME_PATTERN.matcher(hostname);
        java.util.regex.Matcher listMatch = android.net.Proxy.EXCLLIST_PATTERN.matcher(exclList);
        if (!match.matches())
            return android.net.Proxy.PROXY_HOSTNAME_INVALID;

        if (!listMatch.matches())
            return android.net.Proxy.PROXY_EXCLLIST_INVALID;

        if ((hostname.length() > 0) && (port.length() == 0))
            return android.net.Proxy.PROXY_PORT_EMPTY;

        if (port.length() > 0) {
            if (hostname.length() == 0)
                return android.net.Proxy.PROXY_HOSTNAME_EMPTY;

            int portVal = -1;
            try {
                portVal = java.lang.Integer.parseInt(port);
            } catch (java.lang.NumberFormatException ex) {
                return android.net.Proxy.PROXY_PORT_INVALID;
            }
            if ((portVal <= 0) || (portVal > 0xffff))
                return android.net.Proxy.PROXY_PORT_INVALID;

        }
        return android.net.Proxy.PROXY_VALID;
    }

    /**
     *
     *
     * @unknown 
     */
    public static final void setHttpProxySystemProperty(android.net.ProxyInfo p) {
        java.lang.String host = null;
        java.lang.String port = null;
        java.lang.String exclList = null;
        android.net.Uri pacFileUrl = android.net.Uri.EMPTY;
        if (p != null) {
            host = p.getHost();
            port = java.lang.Integer.toString(p.getPort());
            exclList = p.getExclusionListAsString();
            pacFileUrl = p.getPacFileUrl();
        }
        android.net.Proxy.setHttpProxySystemProperty(host, port, exclList, pacFileUrl);
    }

    /**
     *
     *
     * @unknown 
     */
    public static final void setHttpProxySystemProperty(java.lang.String host, java.lang.String port, java.lang.String exclList, android.net.Uri pacFileUrl) {
        if (exclList != null)
            exclList = exclList.replace(",", "|");

        if (false)
            android.util.Log.d(android.net.Proxy.TAG, (((("setHttpProxySystemProperty :" + host) + ":") + port) + " - ") + exclList);

        if (host != null) {
            java.lang.System.setProperty("http.proxyHost", host);
            java.lang.System.setProperty("https.proxyHost", host);
        } else {
            java.lang.System.clearProperty("http.proxyHost");
            java.lang.System.clearProperty("https.proxyHost");
        }
        if (port != null) {
            java.lang.System.setProperty("http.proxyPort", port);
            java.lang.System.setProperty("https.proxyPort", port);
        } else {
            java.lang.System.clearProperty("http.proxyPort");
            java.lang.System.clearProperty("https.proxyPort");
        }
        if (exclList != null) {
            java.lang.System.setProperty("http.nonProxyHosts", exclList);
            java.lang.System.setProperty("https.nonProxyHosts", exclList);
        } else {
            java.lang.System.clearProperty("http.nonProxyHosts");
            java.lang.System.clearProperty("https.nonProxyHosts");
        }
        if (!android.net.Uri.EMPTY.equals(pacFileUrl)) {
            java.net.ProxySelector.setDefault(new android.net.PacProxySelector());
        } else {
            java.net.ProxySelector.setDefault(android.net.Proxy.sDefaultProxySelector);
        }
    }
}

