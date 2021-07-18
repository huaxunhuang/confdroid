/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.security.net.config;


/**
 * {@link X509ExtendedTrustManager} based on an {@link ApplicationConfig}.
 *
 * <p>This trust manager delegates to the specific trust manager for the hostname being used for
 * the connection (See {@link ApplicationConfig#getConfigForHostname(String)} and
 * {@link NetworkSecurityTrustManager}).</p>
 *
 * Note that if the {@code ApplicationConfig} has per-domain configurations the hostname aware
 * {@link #checkServerTrusted(X509Certificate[], String String)} must be used instead of the normal
 * non-aware call.
 *
 * @unknown 
 */
public class RootTrustManager extends javax.net.ssl.X509ExtendedTrustManager {
    private final android.security.net.config.ApplicationConfig mConfig;

    public RootTrustManager(android.security.net.config.ApplicationConfig config) {
        if (config == null) {
            throw new java.lang.NullPointerException("config must not be null");
        }
        mConfig = config;
    }

    @java.lang.Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, java.lang.String authType) throws java.security.cert.CertificateException {
        // Use the default configuration for all client authentication. Domain specific configs are
        // only for use in checking server trust not client trust.
        android.security.net.config.NetworkSecurityConfig config = mConfig.getConfigForHostname("");
        config.getTrustManager().checkClientTrusted(chain, authType);
    }

    @java.lang.Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, java.lang.String authType, java.net.Socket socket) throws java.security.cert.CertificateException {
        // Use the default configuration for all client authentication. Domain specific configs are
        // only for use in checking server trust not client trust.
        android.security.net.config.NetworkSecurityConfig config = mConfig.getConfigForHostname("");
        config.getTrustManager().checkClientTrusted(certs, authType, socket);
    }

    @java.lang.Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, java.lang.String authType, javax.net.ssl.SSLEngine engine) throws java.security.cert.CertificateException {
        // Use the default configuration for all client authentication. Domain specific configs are
        // only for use in checking server trust not client trust.
        android.security.net.config.NetworkSecurityConfig config = mConfig.getConfigForHostname("");
        config.getTrustManager().checkClientTrusted(certs, authType, engine);
    }

    @java.lang.Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, java.lang.String authType, java.net.Socket socket) throws java.security.cert.CertificateException {
        if (socket instanceof javax.net.ssl.SSLSocket) {
            javax.net.ssl.SSLSocket sslSocket = ((javax.net.ssl.SSLSocket) (socket));
            javax.net.ssl.SSLSession session = sslSocket.getHandshakeSession();
            if (session == null) {
                throw new java.security.cert.CertificateException("Not in handshake; no session available");
            }
            java.lang.String host = session.getPeerHost();
            android.security.net.config.NetworkSecurityConfig config = mConfig.getConfigForHostname(host);
            config.getTrustManager().checkServerTrusted(certs, authType, socket);
        } else {
            // Not an SSLSocket, use the hostname unaware checkServerTrusted.
            checkServerTrusted(certs, authType);
        }
    }

    @java.lang.Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, java.lang.String authType, javax.net.ssl.SSLEngine engine) throws java.security.cert.CertificateException {
        javax.net.ssl.SSLSession session = engine.getHandshakeSession();
        if (session == null) {
            throw new java.security.cert.CertificateException("Not in handshake; no session available");
        }
        java.lang.String host = session.getPeerHost();
        android.security.net.config.NetworkSecurityConfig config = mConfig.getConfigForHostname(host);
        config.getTrustManager().checkServerTrusted(certs, authType, engine);
    }

    @java.lang.Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, java.lang.String authType) throws java.security.cert.CertificateException {
        if (mConfig.hasPerDomainConfigs()) {
            throw new java.security.cert.CertificateException("Domain specific configurations require that hostname aware" + " checkServerTrusted(X509Certificate[], String, String) is used");
        }
        android.security.net.config.NetworkSecurityConfig config = mConfig.getConfigForHostname("");
        config.getTrustManager().checkServerTrusted(certs, authType);
    }

    /**
     * Hostname aware version of {@link #checkServerTrusted(X509Certificate[], String)}.
     * This interface is used by conscrypt and android.net.http.X509TrustManagerExtensions do not
     * modify without modifying those callers.
     */
    public java.util.List<java.security.cert.X509Certificate> checkServerTrusted(java.security.cert.X509Certificate[] certs, java.lang.String authType, java.lang.String hostname) throws java.security.cert.CertificateException {
        if ((hostname == null) && mConfig.hasPerDomainConfigs()) {
            throw new java.security.cert.CertificateException("Domain specific configurations require that the hostname be provided");
        }
        android.security.net.config.NetworkSecurityConfig config = mConfig.getConfigForHostname(hostname);
        return config.getTrustManager().checkServerTrusted(certs, authType, hostname);
    }

    @java.lang.Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        // getAcceptedIssuers is meant to be used to determine which trust anchors the server will
        // accept when verifying clients. Domain specific configs are only for use in checking
        // server trust not client trust so use the default config.
        android.security.net.config.NetworkSecurityConfig config = mConfig.getConfigForHostname("");
        return config.getTrustManager().getAcceptedIssuers();
    }

    /**
     * Returns {@code true} if this trust manager uses the same trust configuration for the provided
     * hostnames.
     *
     * <p>This is required by android.net.http.X509TrustManagerExtensions.
     */
    public boolean isSameTrustConfiguration(java.lang.String hostname1, java.lang.String hostname2) {
        return mConfig.getConfigForHostname(hostname1).equals(mConfig.getConfigForHostname(hostname2));
    }
}

