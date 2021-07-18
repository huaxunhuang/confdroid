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
 * SSLSocketFactory implementation with several extra features:
 *
 * <ul>
 * <li>Timeout specification for SSL handshake operations
 * <li>Hostname verification in most cases (see WARNINGs below)
 * <li>Optional SSL session caching with {@link SSLSessionCache}
 * <li>Optionally bypass all SSL certificate checks
 * </ul>
 *
 * The handshake timeout does not apply to actual TCP socket connection.
 * If you want a connection timeout as well, use {@link #createSocket()}
 * and {@link Socket#connect(SocketAddress, int)}, after which you
 * must verify the identity of the server you are connected to.
 *
 * <p class="caution"><b>Most {@link SSLSocketFactory} implementations do not
 * verify the server's identity, allowing man-in-the-middle attacks.</b>
 * This implementation does check the server's certificate hostname, but only
 * for createSocket variants that specify a hostname.  When using methods that
 * use {@link InetAddress} or which return an unconnected socket, you MUST
 * verify the server's identity yourself to ensure a secure connection.</p>
 *
 * <p>One way to verify the server's identity is to use
 * {@link HttpsURLConnection#getDefaultHostnameVerifier()} to get a
 * {@link HostnameVerifier} to verify the certificate hostname.
 *
 * <p>On development devices, "setprop socket.relaxsslcheck yes" bypasses all
 * SSL certificate and hostname checks for testing purposes.  This setting
 * requires root access.
 */
public class SSLCertificateSocketFactory extends javax.net.ssl.SSLSocketFactory {
    private static final java.lang.String TAG = "SSLCertificateSocketFactory";

    private static final javax.net.ssl.TrustManager[] INSECURE_TRUST_MANAGER = new javax.net.ssl.TrustManager[]{ new javax.net.ssl.X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, java.lang.String authType) {
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, java.lang.String authType) {
        }
    } };

    private javax.net.ssl.SSLSocketFactory mInsecureFactory = null;

    private javax.net.ssl.SSLSocketFactory mSecureFactory = null;

    private javax.net.ssl.TrustManager[] mTrustManagers = null;

    private javax.net.ssl.KeyManager[] mKeyManagers = null;

    private byte[] mNpnProtocols = null;

    private byte[] mAlpnProtocols = null;

    private java.security.PrivateKey mChannelIdPrivateKey = null;

    private final int mHandshakeTimeoutMillis;

    private final com.android.org.conscrypt.SSLClientSessionCache mSessionCache;

    private final boolean mSecure;

    /**
     *
     *
     * @deprecated Use {@link #getDefault(int)} instead.
     */
    @java.lang.Deprecated
    public SSLCertificateSocketFactory(int handshakeTimeoutMillis) {
        this(handshakeTimeoutMillis, null, true);
    }

    private SSLCertificateSocketFactory(int handshakeTimeoutMillis, android.net.SSLSessionCache cache, boolean secure) {
        mHandshakeTimeoutMillis = handshakeTimeoutMillis;
        mSessionCache = (cache == null) ? null : cache.mSessionCache;
        mSecure = secure;
    }

    /**
     * Returns a new socket factory instance with an optional handshake timeout.
     *
     * @param handshakeTimeoutMillis
     * 		to use for SSL connection handshake, or 0
     * 		for none.  The socket timeout is reset to 0 after the handshake.
     * @return a new SSLSocketFactory with the specified parameters
     */
    public static javax.net.SocketFactory getDefault(int handshakeTimeoutMillis) {
        return new android.net.SSLCertificateSocketFactory(handshakeTimeoutMillis, null, true);
    }

    /**
     * Returns a new socket factory instance with an optional handshake timeout
     * and SSL session cache.
     *
     * @param handshakeTimeoutMillis
     * 		to use for SSL connection handshake, or 0
     * 		for none.  The socket timeout is reset to 0 after the handshake.
     * @param cache
     * 		The {@link SSLSessionCache} to use, or null for no cache.
     * @return a new SSLSocketFactory with the specified parameters
     */
    public static javax.net.ssl.SSLSocketFactory getDefault(int handshakeTimeoutMillis, android.net.SSLSessionCache cache) {
        return new android.net.SSLCertificateSocketFactory(handshakeTimeoutMillis, cache, true);
    }

    /**
     * Returns a new instance of a socket factory with all SSL security checks
     * disabled, using an optional handshake timeout and SSL session cache.
     *
     * <p class="caution"><b>Warning:</b> Sockets created using this factory
     * are vulnerable to man-in-the-middle attacks!</p>
     *
     * @param handshakeTimeoutMillis
     * 		to use for SSL connection handshake, or 0
     * 		for none.  The socket timeout is reset to 0 after the handshake.
     * @param cache
     * 		The {@link SSLSessionCache} to use, or null for no cache.
     * @return an insecure SSLSocketFactory with the specified parameters
     */
    public static javax.net.ssl.SSLSocketFactory getInsecure(int handshakeTimeoutMillis, android.net.SSLSessionCache cache) {
        return new android.net.SSLCertificateSocketFactory(handshakeTimeoutMillis, cache, false);
    }

    /**
     * Returns a socket factory (also named SSLSocketFactory, but in a different
     * namespace) for use with the Apache HTTP stack.
     *
     * @param handshakeTimeoutMillis
     * 		to use for SSL connection handshake, or 0
     * 		for none.  The socket timeout is reset to 0 after the handshake.
     * @param cache
     * 		The {@link SSLSessionCache} to use, or null for no cache.
     * @return a new SocketFactory with the specified parameters
     * @deprecated Use {@link #getDefault()} along with a {@link javax.net.ssl.HttpsURLConnection}
    instead. The Apache HTTP client is no longer maintained and may be removed in a future
    release. Please visit <a href="http://android-developers.blogspot.com/2011/09/androids-http-clients.html">this webpage</a>
    for further details.
     * @unknown 
     */
    @java.lang.Deprecated
    public static javax.net.ssl.SSLSocketFactory getHttpSocketFactory(int handshakeTimeoutMillis, android.net.SSLSessionCache cache) {
        return new org.apache.http.conn.ssl.SSLSocketFactory(new android.net.SSLCertificateSocketFactory(handshakeTimeoutMillis, cache, true));
    }

    /**
     * Verify the hostname of the certificate used by the other end of a
     * connected socket.  You MUST call this if you did not supply a hostname
     * to {@link #createSocket()}.  It is harmless to call this method
     * redundantly if the hostname has already been verified.
     *
     * <p>Wildcard certificates are allowed to verify any matching hostname,
     * so "foo.bar.example.com" is verified if the peer has a certificate
     * for "*.example.com".
     *
     * @param socket
     * 		An SSL socket which has been connected to a server
     * @param hostname
     * 		The expected hostname of the remote server
     * @throws IOException
     * 		if something goes wrong handshaking with the server
     * @throws SSLPeerUnverifiedException
     * 		if the server cannot prove its identity
     * @unknown 
     */
    public static void verifyHostname(java.net.Socket socket, java.lang.String hostname) throws java.io.IOException {
        if (!(socket instanceof javax.net.ssl.SSLSocket)) {
            throw new java.lang.IllegalArgumentException("Attempt to verify non-SSL socket");
        }
        if (!android.net.SSLCertificateSocketFactory.isSslCheckRelaxed()) {
            // The code at the start of OpenSSLSocketImpl.startHandshake()
            // ensures that the call is idempotent, so we can safely call it.
            javax.net.ssl.SSLSocket ssl = ((javax.net.ssl.SSLSocket) (socket));
            ssl.startHandshake();
            javax.net.ssl.SSLSession session = ssl.getSession();
            if (session == null) {
                throw new javax.net.ssl.SSLException("Cannot verify SSL socket without session");
            }
            if (!javax.net.ssl.HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session)) {
                throw new javax.net.ssl.SSLPeerUnverifiedException("Cannot verify hostname: " + hostname);
            }
        }
    }

    private javax.net.ssl.SSLSocketFactory makeSocketFactory(javax.net.ssl.KeyManager[] keyManagers, javax.net.ssl.TrustManager[] trustManagers) {
        try {
            com.android.org.conscrypt.OpenSSLContextImpl sslContext = com.android.org.conscrypt.OpenSSLContextImpl.getPreferred();
            sslContext.engineInit(keyManagers, trustManagers, null);
            sslContext.engineGetClientSessionContext().setPersistentCache(mSessionCache);
            return sslContext.engineGetSocketFactory();
        } catch (java.security.KeyManagementException e) {
            android.util.Log.wtf(android.net.SSLCertificateSocketFactory.TAG, e);
            return ((javax.net.ssl.SSLSocketFactory) (javax.net.ssl.SSLSocketFactory.getDefault()));// Fallback

        }
    }

    private static boolean isSslCheckRelaxed() {
        return "1".equals(android.os.SystemProperties.get("ro.debuggable")) && "yes".equals(android.os.SystemProperties.get("socket.relaxsslcheck"));
    }

    private synchronized javax.net.ssl.SSLSocketFactory getDelegate() {
        // Relax the SSL check if instructed (for this factory, or systemwide)
        if ((!mSecure) || android.net.SSLCertificateSocketFactory.isSslCheckRelaxed()) {
            if (mInsecureFactory == null) {
                if (mSecure) {
                    android.util.Log.w(android.net.SSLCertificateSocketFactory.TAG, "*** BYPASSING SSL SECURITY CHECKS (socket.relaxsslcheck=yes) ***");
                } else {
                    android.util.Log.w(android.net.SSLCertificateSocketFactory.TAG, "Bypassing SSL security checks at caller's request");
                }
                mInsecureFactory = makeSocketFactory(mKeyManagers, android.net.SSLCertificateSocketFactory.INSECURE_TRUST_MANAGER);
            }
            return mInsecureFactory;
        } else {
            if (mSecureFactory == null) {
                mSecureFactory = makeSocketFactory(mKeyManagers, mTrustManagers);
            }
            return mSecureFactory;
        }
    }

    /**
     * Sets the {@link TrustManager}s to be used for connections made by this factory.
     */
    public void setTrustManagers(javax.net.ssl.TrustManager[] trustManager) {
        mTrustManagers = trustManager;
        // Clear out all cached secure factories since configurations have changed.
        mSecureFactory = null;
        // Note - insecure factories only ever use the INSECURE_TRUST_MANAGER so they need not
        // be cleared out here.
    }

    /**
     * Sets the <a href="http://technotes.googlecode.com/git/nextprotoneg.html">Next
     * Protocol Negotiation (NPN)</a> protocols that this peer is interested in.
     *
     * <p>For servers this is the sequence of protocols to advertise as
     * supported, in order of preference. This list is sent unencrypted to
     * all clients that support NPN.
     *
     * <p>For clients this is a list of supported protocols to match against the
     * server's list. If there is no protocol supported by both client and
     * server then the first protocol in the client's list will be selected.
     * The order of the client's protocols is otherwise insignificant.
     *
     * @param npnProtocols
     * 		a non-empty list of protocol byte arrays. All arrays
     * 		must be non-empty and of length less than 256.
     */
    public void setNpnProtocols(byte[][] npnProtocols) {
        this.mNpnProtocols = android.net.SSLCertificateSocketFactory.toLengthPrefixedList(npnProtocols);
    }

    /**
     * Sets the
     * <a href="http://tools.ietf.org/html/draft-ietf-tls-applayerprotoneg-01">
     * Application Layer Protocol Negotiation (ALPN)</a> protocols that this peer
     * is interested in.
     *
     * <p>For servers this is the sequence of protocols to advertise as
     * supported, in order of preference. This list is sent unencrypted to
     * all clients that support ALPN.
     *
     * <p>For clients this is a list of supported protocols to match against the
     * server's list. If there is no protocol supported by both client and
     * server then the first protocol in the client's list will be selected.
     * The order of the client's protocols is otherwise insignificant.
     *
     * @param protocols
     * 		a non-empty list of protocol byte arrays. All arrays
     * 		must be non-empty and of length less than 256.
     * @unknown 
     */
    public void setAlpnProtocols(byte[][] protocols) {
        this.mAlpnProtocols = android.net.SSLCertificateSocketFactory.toLengthPrefixedList(protocols);
    }

    /**
     * Returns an array containing the concatenation of length-prefixed byte
     * strings.
     */
    static byte[] toLengthPrefixedList(byte[]... items) {
        if (items.length == 0) {
            throw new java.lang.IllegalArgumentException("items.length == 0");
        }
        int totalLength = 0;
        for (byte[] s : items) {
            if ((s.length == 0) || (s.length > 255)) {
                throw new java.lang.IllegalArgumentException("s.length == 0 || s.length > 255: " + s.length);
            }
            totalLength += 1 + s.length;
        }
        byte[] result = new byte[totalLength];
        int pos = 0;
        for (byte[] s : items) {
            result[pos++] = ((byte) (s.length));
            for (byte b : s) {
                result[pos++] = b;
            }
        }
        return result;
    }

    /**
     * Returns the <a href="http://technotes.googlecode.com/git/nextprotoneg.html">Next
     * Protocol Negotiation (NPN)</a> protocol selected by client and server, or
     * null if no protocol was negotiated.
     *
     * @param socket
     * 		a socket created by this factory.
     * @throws IllegalArgumentException
     * 		if the socket was not created by this factory.
     */
    public byte[] getNpnSelectedProtocol(java.net.Socket socket) {
        return android.net.SSLCertificateSocketFactory.castToOpenSSLSocket(socket).getNpnSelectedProtocol();
    }

    /**
     * Returns the
     * <a href="http://tools.ietf.org/html/draft-ietf-tls-applayerprotoneg-01">Application
     * Layer Protocol Negotiation (ALPN)</a> protocol selected by client and server, or null
     * if no protocol was negotiated.
     *
     * @param socket
     * 		a socket created by this factory.
     * @throws IllegalArgumentException
     * 		if the socket was not created by this factory.
     * @unknown 
     */
    public byte[] getAlpnSelectedProtocol(java.net.Socket socket) {
        return android.net.SSLCertificateSocketFactory.castToOpenSSLSocket(socket).getAlpnSelectedProtocol();
    }

    /**
     * Sets the {@link KeyManager}s to be used for connections made by this factory.
     */
    public void setKeyManagers(javax.net.ssl.KeyManager[] keyManagers) {
        mKeyManagers = keyManagers;
        // Clear out any existing cached factories since configurations have changed.
        mSecureFactory = null;
        mInsecureFactory = null;
    }

    /**
     * Sets the private key to be used for TLS Channel ID by connections made by this
     * factory.
     *
     * @param privateKey
     * 		private key (enables TLS Channel ID) or {@code null} for no key (disables
     * 		TLS Channel ID). The private key has to be an Elliptic Curve (EC) key based on the
     * 		NIST P-256 curve (aka SECG secp256r1 or ANSI X9.62 prime256v1).
     * @unknown 
     */
    public void setChannelIdPrivateKey(java.security.PrivateKey privateKey) {
        mChannelIdPrivateKey = privateKey;
    }

    /**
     * Enables <a href="http://tools.ietf.org/html/rfc5077#section-3.2">session ticket</a>
     * support on the given socket.
     *
     * @param socket
     * 		a socket created by this factory
     * @param useSessionTickets
     * 		{@code true} to enable session ticket support on this socket.
     * @throws IllegalArgumentException
     * 		if the socket was not created by this factory.
     */
    public void setUseSessionTickets(java.net.Socket socket, boolean useSessionTickets) {
        android.net.SSLCertificateSocketFactory.castToOpenSSLSocket(socket).setUseSessionTickets(useSessionTickets);
    }

    /**
     * Turns on <a href="http://tools.ietf.org/html/rfc6066#section-3">Server
     * Name Indication (SNI)</a> on a given socket.
     *
     * @param socket
     * 		a socket created by this factory.
     * @param hostName
     * 		the desired SNI hostname, null to disable.
     * @throws IllegalArgumentException
     * 		if the socket was not created by this factory.
     */
    public void setHostname(java.net.Socket socket, java.lang.String hostName) {
        android.net.SSLCertificateSocketFactory.castToOpenSSLSocket(socket).setHostname(hostName);
    }

    /**
     * Sets this socket's SO_SNDTIMEO write timeout in milliseconds.
     * Use 0 for no timeout.
     * To take effect, this option must be set before the blocking method was called.
     *
     * @param socket
     * 		a socket created by this factory.
     * @param timeout
     * 		the desired write timeout in milliseconds.
     * @throws IllegalArgumentException
     * 		if the socket was not created by this factory.
     * @unknown 
     */
    public void setSoWriteTimeout(java.net.Socket socket, int writeTimeoutMilliseconds) throws java.net.SocketException {
        android.net.SSLCertificateSocketFactory.castToOpenSSLSocket(socket).setSoWriteTimeout(writeTimeoutMilliseconds);
    }

    private static com.android.org.conscrypt.OpenSSLSocketImpl castToOpenSSLSocket(java.net.Socket socket) {
        if (!(socket instanceof com.android.org.conscrypt.OpenSSLSocketImpl)) {
            throw new java.lang.IllegalArgumentException("Socket not created by this factory: " + socket);
        }
        return ((com.android.org.conscrypt.OpenSSLSocketImpl) (socket));
    }

    /**
     * {@inheritDoc }
     *
     * <p>This method verifies the peer's certificate hostname after connecting
     * (unless created with {@link #getInsecure(int, SSLSessionCache)}).
     */
    @java.lang.Override
    public java.net.Socket createSocket(java.net.Socket k, java.lang.String host, int port, boolean close) throws java.io.IOException {
        com.android.org.conscrypt.OpenSSLSocketImpl s = ((com.android.org.conscrypt.OpenSSLSocketImpl) (getDelegate().createSocket(k, host, port, close)));
        s.setNpnProtocols(mNpnProtocols);
        s.setAlpnProtocols(mAlpnProtocols);
        s.setHandshakeTimeout(mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(mChannelIdPrivateKey);
        if (mSecure) {
            android.net.SSLCertificateSocketFactory.verifyHostname(s, host);
        }
        return s;
    }

    /**
     * Creates a new socket which is not connected to any remote host.
     * You must use {@link Socket#connect} to connect the socket.
     *
     * <p class="caution"><b>Warning:</b> Hostname verification is not performed
     * with this method.  You MUST verify the server's identity after connecting
     * the socket to avoid man-in-the-middle attacks.</p>
     */
    @java.lang.Override
    public java.net.Socket createSocket() throws java.io.IOException {
        com.android.org.conscrypt.OpenSSLSocketImpl s = ((com.android.org.conscrypt.OpenSSLSocketImpl) (getDelegate().createSocket()));
        s.setNpnProtocols(mNpnProtocols);
        s.setAlpnProtocols(mAlpnProtocols);
        s.setHandshakeTimeout(mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(mChannelIdPrivateKey);
        return s;
    }

    /**
     * {@inheritDoc }
     *
     * <p class="caution"><b>Warning:</b> Hostname verification is not performed
     * with this method.  You MUST verify the server's identity after connecting
     * the socket to avoid man-in-the-middle attacks.</p>
     */
    @java.lang.Override
    public java.net.Socket createSocket(java.net.InetAddress addr, int port, java.net.InetAddress localAddr, int localPort) throws java.io.IOException {
        com.android.org.conscrypt.OpenSSLSocketImpl s = ((com.android.org.conscrypt.OpenSSLSocketImpl) (getDelegate().createSocket(addr, port, localAddr, localPort)));
        s.setNpnProtocols(mNpnProtocols);
        s.setAlpnProtocols(mAlpnProtocols);
        s.setHandshakeTimeout(mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(mChannelIdPrivateKey);
        return s;
    }

    /**
     * {@inheritDoc }
     *
     * <p class="caution"><b>Warning:</b> Hostname verification is not performed
     * with this method.  You MUST verify the server's identity after connecting
     * the socket to avoid man-in-the-middle attacks.</p>
     */
    @java.lang.Override
    public java.net.Socket createSocket(java.net.InetAddress addr, int port) throws java.io.IOException {
        com.android.org.conscrypt.OpenSSLSocketImpl s = ((com.android.org.conscrypt.OpenSSLSocketImpl) (getDelegate().createSocket(addr, port)));
        s.setNpnProtocols(mNpnProtocols);
        s.setAlpnProtocols(mAlpnProtocols);
        s.setHandshakeTimeout(mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(mChannelIdPrivateKey);
        return s;
    }

    /**
     * {@inheritDoc }
     *
     * <p>This method verifies the peer's certificate hostname after connecting
     * (unless created with {@link #getInsecure(int, SSLSessionCache)}).
     */
    @java.lang.Override
    public java.net.Socket createSocket(java.lang.String host, int port, java.net.InetAddress localAddr, int localPort) throws java.io.IOException {
        com.android.org.conscrypt.OpenSSLSocketImpl s = ((com.android.org.conscrypt.OpenSSLSocketImpl) (getDelegate().createSocket(host, port, localAddr, localPort)));
        s.setNpnProtocols(mNpnProtocols);
        s.setAlpnProtocols(mAlpnProtocols);
        s.setHandshakeTimeout(mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(mChannelIdPrivateKey);
        if (mSecure) {
            android.net.SSLCertificateSocketFactory.verifyHostname(s, host);
        }
        return s;
    }

    /**
     * {@inheritDoc }
     *
     * <p>This method verifies the peer's certificate hostname after connecting
     * (unless created with {@link #getInsecure(int, SSLSessionCache)}).
     */
    @java.lang.Override
    public java.net.Socket createSocket(java.lang.String host, int port) throws java.io.IOException {
        com.android.org.conscrypt.OpenSSLSocketImpl s = ((com.android.org.conscrypt.OpenSSLSocketImpl) (getDelegate().createSocket(host, port)));
        s.setNpnProtocols(mNpnProtocols);
        s.setAlpnProtocols(mAlpnProtocols);
        s.setHandshakeTimeout(mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(mChannelIdPrivateKey);
        if (mSecure) {
            android.net.SSLCertificateSocketFactory.verifyHostname(s, host);
        }
        return s;
    }

    @java.lang.Override
    public java.lang.String[] getDefaultCipherSuites() {
        return getDelegate().getDefaultCipherSuites();
    }

    @java.lang.Override
    public java.lang.String[] getSupportedCipherSuites() {
        return getDelegate().getSupportedCipherSuites();
    }
}

