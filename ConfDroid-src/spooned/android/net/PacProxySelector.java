/**
 * Copyright (C) 2013 The Android Open Source Project
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
 *
 *
 * @unknown 
 */
public class PacProxySelector extends java.net.ProxySelector {
    private static final java.lang.String TAG = "PacProxySelector";

    public static final java.lang.String PROXY_SERVICE = "com.android.net.IProxyService";

    private static final java.lang.String SOCKS = "SOCKS ";

    private static final java.lang.String PROXY = "PROXY ";

    private com.android.net.IProxyService mProxyService;

    private final java.util.List<java.net.Proxy> mDefaultList;

    public PacProxySelector() {
        mProxyService = IProxyService.Stub.asInterface(android.os.ServiceManager.getService(android.net.PacProxySelector.PROXY_SERVICE));
        if (mProxyService == null) {
            // Added because of b10267814 where mako is restarting.
            android.util.Log.e(android.net.PacProxySelector.TAG, "PacManager: no proxy service");
        }
        mDefaultList = com.google.android.collect.Lists.newArrayList(java.net.Proxy.NO_PROXY);
    }

    @java.lang.Override
    public java.util.List<java.net.Proxy> select(java.net.URI uri) {
        if (mProxyService == null) {
            mProxyService = IProxyService.Stub.asInterface(android.os.ServiceManager.getService(android.net.PacProxySelector.PROXY_SERVICE));
        }
        if (mProxyService == null) {
            android.util.Log.e(android.net.PacProxySelector.TAG, "select: no proxy service return NO_PROXY");
            return com.google.android.collect.Lists.newArrayList(java.net.Proxy.NO_PROXY);
        }
        java.lang.String response = null;
        java.lang.String urlString;
        try {
            // Strip path and username/password from URI so it's not visible to PAC script. The
            // path often contains credentials the app does not want exposed to a potentially
            // malicious PAC script.
            if (!"http".equalsIgnoreCase(uri.getScheme())) {
                uri = new java.net.URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), "/", null, null);
            }
            urlString = uri.toURL().toString();
        } catch (java.net.URISyntaxException e) {
            urlString = uri.getHost();
        } catch (java.net.MalformedURLException e) {
            urlString = uri.getHost();
        }
        try {
            response = mProxyService.resolvePacFile(uri.getHost(), urlString);
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.net.PacProxySelector.TAG, "Error resolving PAC File", e);
        }
        if (response == null) {
            return mDefaultList;
        }
        return android.net.PacProxySelector.parseResponse(response);
    }

    private static java.util.List<java.net.Proxy> parseResponse(java.lang.String response) {
        java.lang.String[] split = response.split(";");
        java.util.List<java.net.Proxy> ret = com.google.android.collect.Lists.newArrayList();
        for (java.lang.String s : split) {
            java.lang.String trimmed = s.trim();
            if (trimmed.equals("DIRECT")) {
                ret.add(java.net.Proxy.NO_PROXY);
            } else
                if (trimmed.startsWith(android.net.PacProxySelector.PROXY)) {
                    java.net.Proxy proxy = android.net.PacProxySelector.proxyFromHostPort(java.net.Proxy.Type.HTTP, trimmed.substring(android.net.PacProxySelector.PROXY.length()));
                    if (proxy != null) {
                        ret.add(proxy);
                    }
                } else
                    if (trimmed.startsWith(android.net.PacProxySelector.SOCKS)) {
                        java.net.Proxy proxy = android.net.PacProxySelector.proxyFromHostPort(java.net.Proxy.Type.SOCKS, trimmed.substring(android.net.PacProxySelector.SOCKS.length()));
                        if (proxy != null) {
                            ret.add(proxy);
                        }
                    }


        }
        if (ret.size() == 0) {
            ret.add(java.net.Proxy.NO_PROXY);
        }
        return ret;
    }

    private static java.net.Proxy proxyFromHostPort(java.net.Proxy.Type type, java.lang.String hostPortString) {
        try {
            java.lang.String[] hostPort = hostPortString.split(":");
            java.lang.String host = hostPort[0];
            int port = java.lang.Integer.parseInt(hostPort[1]);
            return new java.net.Proxy(type, java.net.InetSocketAddress.createUnresolved(host, port));
        } catch (java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException e) {
            android.util.Log.d(android.net.PacProxySelector.TAG, (("Unable to parse proxy " + hostPortString) + " ") + e);
            return null;
        }
    }

    @java.lang.Override
    public void connectFailed(java.net.URI uri, java.net.SocketAddress address, java.io.IOException failure) {
    }
}

