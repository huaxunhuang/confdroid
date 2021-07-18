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
 * Identifies a {@code Network}.  This is supplied to applications via
 * {@link ConnectivityManager.NetworkCallback} in response to the active
 * {@link ConnectivityManager#requestNetwork} or passive
 * {@link ConnectivityManager#registerNetworkCallback} calls.
 * It is used to direct traffic to the given {@code Network}, either on a {@link Socket} basis
 * through a targeted {@link SocketFactory} or process-wide via
 * {@link ConnectivityManager#bindProcessToNetwork}.
 */
public class Network implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    public final int netId;

    // Objects used to perform per-network operations such as getSocketFactory
    // and openConnection, and a lock to protect access to them.
    private volatile android.net.Network.NetworkBoundSocketFactory mNetworkBoundSocketFactory = null;

    // mLock should be used to control write access to mConnectionPool and mNetwork.
    // maybeInitHttpClient() must be called prior to reading either variable.
    private volatile com.android.okhttp.ConnectionPool mConnectionPool = null;

    private volatile com.android.okhttp.internal.Network mNetwork = null;

    private final java.lang.Object mLock = new java.lang.Object();

    // Default connection pool values. These are evaluated at startup, just
    // like the OkHttp code. Also like the OkHttp code, we will throw parse
    // exceptions at class loading time if the properties are set but are not
    // valid integers.
    private static final boolean httpKeepAlive = java.lang.Boolean.parseBoolean(java.lang.System.getProperty("http.keepAlive", "true"));

    private static final int httpMaxConnections = (android.net.Network.httpKeepAlive) ? java.lang.Integer.parseInt(java.lang.System.getProperty("http.maxConnections", "5")) : 0;

    private static final long httpKeepAliveDurationMs = java.lang.Long.parseLong(java.lang.System.getProperty("http.keepAliveDuration", "300000"));// 5 minutes.


    /**
     *
     *
     * @unknown 
     */
    public Network(int netId) {
        this.netId = netId;
    }

    /**
     *
     *
     * @unknown 
     */
    public Network(android.net.Network that) {
        this.netId = that.netId;
    }

    /**
     * Operates the same as {@code InetAddress.getAllByName} except that host
     * resolution is done on this network.
     *
     * @param host
     * 		the hostname or literal IP string to be resolved.
     * @return the array of addresses associated with the specified host.
     * @throws UnknownHostException
     * 		if the address lookup fails.
     */
    public java.net.InetAddress[] getAllByName(java.lang.String host) throws java.net.UnknownHostException {
        return java.net.InetAddress.getAllByNameOnNet(host, netId);
    }

    /**
     * Operates the same as {@code InetAddress.getByName} except that host
     * resolution is done on this network.
     *
     * @param host
     * 		the hostName to be resolved to an address or {@code null}.
     * @return the {@code InetAddress} instance representing the host.
     * @throws UnknownHostException
     * 		if the address lookup fails.
     */
    public java.net.InetAddress getByName(java.lang.String host) throws java.net.UnknownHostException {
        return java.net.InetAddress.getByNameOnNet(host, netId);
    }

    /**
     * A {@code SocketFactory} that produces {@code Socket}'s bound to this network.
     */
    private class NetworkBoundSocketFactory extends javax.net.SocketFactory {
        private final int mNetId;

        public NetworkBoundSocketFactory(int netId) {
            super();
            mNetId = netId;
        }

        private java.net.Socket connectToHost(java.lang.String host, int port, java.net.SocketAddress localAddress) throws java.io.IOException {
            // Lookup addresses only on this Network.
            java.net.InetAddress[] hostAddresses = getAllByName(host);
            // Try all addresses.
            for (int i = 0; i < hostAddresses.length; i++) {
                try {
                    java.net.Socket socket = createSocket();
                    if (localAddress != null)
                        socket.bind(localAddress);

                    socket.connect(new java.net.InetSocketAddress(hostAddresses[i], port));
                    return socket;
                } catch (java.io.IOException e) {
                    if (i == (hostAddresses.length - 1))
                        throw e;

                }
            }
            throw new java.net.UnknownHostException(host);
        }

        @java.lang.Override
        public java.net.Socket createSocket(java.lang.String host, int port, java.net.InetAddress localHost, int localPort) throws java.io.IOException {
            return connectToHost(host, port, new java.net.InetSocketAddress(localHost, localPort));
        }

        @java.lang.Override
        public java.net.Socket createSocket(java.net.InetAddress address, int port, java.net.InetAddress localAddress, int localPort) throws java.io.IOException {
            java.net.Socket socket = createSocket();
            socket.bind(new java.net.InetSocketAddress(localAddress, localPort));
            socket.connect(new java.net.InetSocketAddress(address, port));
            return socket;
        }

        @java.lang.Override
        public java.net.Socket createSocket(java.net.InetAddress host, int port) throws java.io.IOException {
            java.net.Socket socket = createSocket();
            socket.connect(new java.net.InetSocketAddress(host, port));
            return socket;
        }

        @java.lang.Override
        public java.net.Socket createSocket(java.lang.String host, int port) throws java.io.IOException {
            return connectToHost(host, port, null);
        }

        @java.lang.Override
        public java.net.Socket createSocket() throws java.io.IOException {
            java.net.Socket socket = new java.net.Socket();
            bindSocket(socket);
            return socket;
        }
    }

    /**
     * Returns a {@link SocketFactory} bound to this network.  Any {@link Socket} created by
     * this factory will have its traffic sent over this {@code Network}.  Note that if this
     * {@code Network} ever disconnects, this factory and any {@link Socket} it produced in the
     * past or future will cease to work.
     *
     * @return a {@link SocketFactory} which produces {@link Socket} instances bound to this
    {@code Network}.
     */
    public javax.net.SocketFactory getSocketFactory() {
        if (mNetworkBoundSocketFactory == null) {
            synchronized(mLock) {
                if (mNetworkBoundSocketFactory == null) {
                    mNetworkBoundSocketFactory = new android.net.Network.NetworkBoundSocketFactory(netId);
                }
            }
        }
        return mNetworkBoundSocketFactory;
    }

    // TODO: This creates a connection pool and host resolver for
    // every Network object, instead of one for every NetId. This is
    // suboptimal, because an app could potentially have more than one
    // Network object for the same NetId, causing increased memory footprint
    // and performance penalties due to lack of connection reuse (connection
    // setup time, congestion window growth time, etc.).
    // 
    // Instead, investigate only having one connection pool and host resolver
    // for every NetId, perhaps by using a static HashMap of NetIds to
    // connection pools and host resolvers. The tricky part is deciding when
    // to remove a map entry; a WeakHashMap shouldn't be used because whether
    // a Network is referenced doesn't correlate with whether a new Network
    // will be instantiated in the near future with the same NetID. A good
    // solution would involve purging empty (or when all connections are timed
    // out) ConnectionPools.
    private void maybeInitHttpClient() {
        synchronized(mLock) {
            if (mNetwork == null) {
                mNetwork = new com.android.okhttp.internal.Network() {
                    @java.lang.Override
                    public java.net.InetAddress[] resolveInetAddresses(java.lang.String host) throws java.net.UnknownHostException {
                        return android.net.Network.this.getAllByName(host);
                    }
                };
            }
            if (mConnectionPool == null) {
                mConnectionPool = new com.android.okhttp.ConnectionPool(android.net.Network.httpMaxConnections, android.net.Network.httpKeepAliveDurationMs);
            }
        }
    }

    /**
     * Opens the specified {@link URL} on this {@code Network}, such that all traffic will be sent
     * on this Network. The URL protocol must be {@code HTTP} or {@code HTTPS}.
     *
     * @return a {@code URLConnection} to the resource referred to by this URL.
     * @throws MalformedURLException
     * 		if the URL protocol is not HTTP or HTTPS.
     * @throws IOException
     * 		if an error occurs while opening the connection.
     * @see java.net.URL#openConnection()
     */
    public java.net.URLConnection openConnection(java.net.URL url) throws java.io.IOException {
        final android.net.ConnectivityManager cm = android.net.ConnectivityManager.getInstanceOrNull();
        if (cm == null) {
            throw new java.io.IOException("No ConnectivityManager yet constructed, please construct one");
        }
        // TODO: Should this be optimized to avoid fetching the global proxy for every request?
        final android.net.ProxyInfo proxyInfo = cm.getProxyForNetwork(this);
        java.net.Proxy proxy = null;
        if (proxyInfo != null) {
            proxy = proxyInfo.makeProxy();
        } else {
            proxy = java.net.Proxy.NO_PROXY;
        }
        return openConnection(url, proxy);
    }

    /**
     * Opens the specified {@link URL} on this {@code Network}, such that all traffic will be sent
     * on this Network. The URL protocol must be {@code HTTP} or {@code HTTPS}.
     *
     * @param proxy
     * 		the proxy through which the connection will be established.
     * @return a {@code URLConnection} to the resource referred to by this URL.
     * @throws MalformedURLException
     * 		if the URL protocol is not HTTP or HTTPS.
     * @throws IllegalArgumentException
     * 		if the argument proxy is null.
     * @throws IOException
     * 		if an error occurs while opening the connection.
     * @see java.net.URL#openConnection()
     */
    public java.net.URLConnection openConnection(java.net.URL url, java.net.Proxy proxy) throws java.io.IOException {
        if (proxy == null)
            throw new java.lang.IllegalArgumentException("proxy is null");

        maybeInitHttpClient();
        java.lang.String protocol = url.getProtocol();
        com.android.okhttp.OkUrlFactory okUrlFactory;
        // TODO: HttpHandler creates OkUrlFactory instances that share the default ResponseCache.
        // Could this cause unexpected behavior?
        if (protocol.equals("http")) {
            okUrlFactory = com.android.okhttp.HttpHandler.createHttpOkUrlFactory(proxy);
        } else
            if (protocol.equals("https")) {
                okUrlFactory = com.android.okhttp.HttpsHandler.createHttpsOkUrlFactory(proxy);
            } else {
                // OkHttp only supports HTTP and HTTPS and returns a null URLStreamHandler if
                // passed another protocol.
                throw new java.net.MalformedURLException("Invalid URL or unrecognized protocol " + protocol);
            }

        com.android.okhttp.OkHttpClient client = okUrlFactory.client();
        client.setSocketFactory(getSocketFactory()).setConnectionPool(mConnectionPool);
        // Use internal APIs to change the Network.
        Internal.instance.setNetwork(client, mNetwork);
        return okUrlFactory.open(url);
    }

    /**
     * Binds the specified {@link DatagramSocket} to this {@code Network}. All data traffic on the
     * socket will be sent on this {@code Network}, irrespective of any process-wide network binding
     * set by {@link ConnectivityManager#bindProcessToNetwork}. The socket must not be
     * connected.
     */
    public void bindSocket(java.net.DatagramSocket socket) throws java.io.IOException {
        // Query a property of the underlying socket to ensure that the socket's file descriptor
        // exists, is available to bind to a network and is not closed.
        socket.getReuseAddress();
        bindSocket(socket.getFileDescriptor$());
    }

    /**
     * Binds the specified {@link Socket} to this {@code Network}. All data traffic on the socket
     * will be sent on this {@code Network}, irrespective of any process-wide network binding set by
     * {@link ConnectivityManager#bindProcessToNetwork}. The socket must not be connected.
     */
    public void bindSocket(java.net.Socket socket) throws java.io.IOException {
        // Query a property of the underlying socket to ensure that the socket's file descriptor
        // exists, is available to bind to a network and is not closed.
        socket.getReuseAddress();
        bindSocket(socket.getFileDescriptor$());
    }

    /**
     * Binds the specified {@link FileDescriptor} to this {@code Network}. All data traffic on the
     * socket represented by this file descriptor will be sent on this {@code Network},
     * irrespective of any process-wide network binding set by
     * {@link ConnectivityManager#bindProcessToNetwork}. The socket must not be connected.
     */
    public void bindSocket(java.io.FileDescriptor fd) throws java.io.IOException {
        try {
            final java.net.SocketAddress peer = android.system.Os.getpeername(fd);
            final java.net.InetAddress inetPeer = ((java.net.InetSocketAddress) (peer)).getAddress();
            if (!inetPeer.isAnyLocalAddress()) {
                // Apparently, the kernel doesn't update a connected UDP socket's
                // routing upon mark changes.
                throw new java.net.SocketException("Socket is connected");
            }
        } catch (android.system.ErrnoException e) {
            // getpeername() failed.
            if (e.errno != android.system.OsConstants.ENOTCONN) {
                throw e.rethrowAsSocketException();
            }
        } catch (java.lang.ClassCastException e) {
            // Wasn't an InetSocketAddress.
            throw new java.net.SocketException("Only AF_INET/AF_INET6 sockets supported");
        }
        final int err = android.net.NetworkUtils.bindSocketToNetwork(fd.getInt$(), netId);
        if (err != 0) {
            // bindSocketToNetwork returns negative errno.
            throw new android.system.ErrnoException("Binding socket to network " + netId, -err).rethrowAsSocketException();
        }
    }

    /**
     * Returns a handle representing this {@code Network}, for use with the NDK API.
     */
    public long getNetworkHandle() {
        // The network handle is explicitly not the same as the netId.
        // 
        // The netId is an implementation detail which might be changed in the
        // future, or which alone (i.e. in the absence of some additional
        // context) might not be sufficient to fully identify a Network.
        // 
        // As such, the intention is to prevent accidental misuse of the API
        // that might result if a developer assumed that handles and netIds
        // were identical and passing a netId to a call expecting a handle
        // "just worked".  Such accidental misuse, if widely deployed, might
        // prevent future changes to the semantics of the netId field or
        // inhibit the expansion of state required for Network objects.
        // 
        // This extra layer of indirection might be seen as paranoia, and might
        // never end up being necessary, but the added complexity is trivial.
        // At some future date it may be desirable to realign the handle with
        // Multiple Provisioning Domains API recommendations, as made by the
        // IETF mif working group.
        // 
        // The HANDLE_MAGIC value MUST be kept in sync with the corresponding
        // value in the native/android/net.c NDK implementation.
        if (netId == 0) {
            return 0L;// make this zero condition obvious for debugging

        }
        final long HANDLE_MAGIC = 0xfacade;
        return (((long) (netId)) << 32) | HANDLE_MAGIC;
    }

    // implement the Parcelable interface
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(netId);
    }

    public static final android.os.Parcelable.Creator<android.net.Network> CREATOR = new android.os.Parcelable.Creator<android.net.Network>() {
        public android.net.Network createFromParcel(android.os.Parcel in) {
            int netId = in.readInt();
            return new android.net.Network(netId);
        }

        public android.net.Network[] newArray(int size) {
            return new android.net.Network[size];
        }
    };

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if ((obj instanceof android.net.Network) == false)
            return false;

        android.net.Network other = ((android.net.Network) (obj));
        return this.netId == other.netId;
    }

    @java.lang.Override
    public int hashCode() {
        return netId * 11;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.Integer.toString(netId);
    }
}

