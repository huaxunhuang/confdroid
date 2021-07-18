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
 * {@link X509ExtendedTrustManager} that implements the trust anchor and pinning for a
 * given {@link NetworkSecurityConfig}.
 *
 * @unknown 
 */
public class NetworkSecurityTrustManager extends javax.net.ssl.X509ExtendedTrustManager {
    // TODO: Replace this with a general X509TrustManager and use duck-typing.
    private final com.android.org.conscrypt.TrustManagerImpl mDelegate;

    private final android.security.net.config.NetworkSecurityConfig mNetworkSecurityConfig;

    private final java.lang.Object mIssuersLock = new java.lang.Object();

    private java.security.cert.X509Certificate[] mIssuers;

    public NetworkSecurityTrustManager(android.security.net.config.NetworkSecurityConfig config) {
        if (config == null) {
            throw new java.lang.NullPointerException("config must not be null");
        }
        mNetworkSecurityConfig = config;
        try {
            android.security.net.config.TrustedCertificateStoreAdapter certStore = new android.security.net.config.TrustedCertificateStoreAdapter(config);
            // Provide an empty KeyStore since TrustManagerImpl doesn't support null KeyStores.
            // TrustManagerImpl will use certStore to lookup certificates.
            java.security.KeyStore store = java.security.KeyStore.getInstance(java.security.KeyStore.getDefaultType());
            store.load(null);
            mDelegate = new com.android.org.conscrypt.TrustManagerImpl(store, null, certStore);
        } catch (java.security.GeneralSecurityException | java.io.IOException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    @java.lang.Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, java.lang.String authType) throws java.security.cert.CertificateException {
        mDelegate.checkClientTrusted(chain, authType);
    }

    @java.lang.Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, java.lang.String authType, java.net.Socket socket) throws java.security.cert.CertificateException {
        mDelegate.checkClientTrusted(certs, authType, socket);
    }

    @java.lang.Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, java.lang.String authType, javax.net.ssl.SSLEngine engine) throws java.security.cert.CertificateException {
        mDelegate.checkClientTrusted(certs, authType, engine);
    }

    @java.lang.Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, java.lang.String authType) throws java.security.cert.CertificateException {
        checkServerTrusted(certs, authType, ((java.lang.String) (null)));
    }

    @java.lang.Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, java.lang.String authType, java.net.Socket socket) throws java.security.cert.CertificateException {
        java.util.List<java.security.cert.X509Certificate> trustedChain = mDelegate.getTrustedChainForServer(certs, authType, socket);
        checkPins(trustedChain);
    }

    @java.lang.Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, java.lang.String authType, javax.net.ssl.SSLEngine engine) throws java.security.cert.CertificateException {
        java.util.List<java.security.cert.X509Certificate> trustedChain = mDelegate.getTrustedChainForServer(certs, authType, engine);
        checkPins(trustedChain);
    }

    /**
     * Hostname aware version of {@link #checkServerTrusted(X509Certificate[], String)}.
     * This interface is used by conscrypt and android.net.http.X509TrustManagerExtensions do not
     * modify without modifying those callers.
     */
    public java.util.List<java.security.cert.X509Certificate> checkServerTrusted(java.security.cert.X509Certificate[] certs, java.lang.String authType, java.lang.String host) throws java.security.cert.CertificateException {
        java.util.List<java.security.cert.X509Certificate> trustedChain = mDelegate.checkServerTrusted(certs, authType, host);
        checkPins(trustedChain);
        return trustedChain;
    }

    private void checkPins(java.util.List<java.security.cert.X509Certificate> chain) throws java.security.cert.CertificateException {
        android.security.net.config.PinSet pinSet = mNetworkSecurityConfig.getPins();
        if ((pinSet.pins.isEmpty() || (java.lang.System.currentTimeMillis() > pinSet.expirationTime)) || (!isPinningEnforced(chain))) {
            return;
        }
        java.util.Set<java.lang.String> pinAlgorithms = pinSet.getPinAlgorithms();
        java.util.Map<java.lang.String, java.security.MessageDigest> digestMap = new android.util.ArrayMap<java.lang.String, java.security.MessageDigest>(pinAlgorithms.size());
        for (int i = chain.size() - 1; i >= 0; i--) {
            java.security.cert.X509Certificate cert = chain.get(i);
            byte[] encodedSPKI = cert.getPublicKey().getEncoded();
            for (java.lang.String algorithm : pinAlgorithms) {
                java.security.MessageDigest md = digestMap.get(algorithm);
                if (md == null) {
                    try {
                        md = java.security.MessageDigest.getInstance(algorithm);
                    } catch (java.security.GeneralSecurityException e) {
                        throw new java.lang.RuntimeException(e);
                    }
                    digestMap.put(algorithm, md);
                }
                if (pinSet.pins.contains(new android.security.net.config.Pin(algorithm, md.digest(encodedSPKI)))) {
                    return;
                }
            }
        }
        // TODO: Throw a subclass of CertificateException which indicates a pinning failure.
        throw new java.security.cert.CertificateException("Pin verification failed");
    }

    private boolean isPinningEnforced(java.util.List<java.security.cert.X509Certificate> chain) throws java.security.cert.CertificateException {
        if (chain.isEmpty()) {
            return false;
        }
        java.security.cert.X509Certificate anchorCert = chain.get(chain.size() - 1);
        android.security.net.config.TrustAnchor chainAnchor = mNetworkSecurityConfig.findTrustAnchorBySubjectAndPublicKey(anchorCert);
        if (chainAnchor == null) {
            throw new java.security.cert.CertificateException("Trusted chain does not end in a TrustAnchor");
        }
        return !chainAnchor.overridesPins;
    }

    @java.lang.Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        // TrustManagerImpl only looks at the provided KeyStore and not the TrustedCertificateStore
        // for getAcceptedIssuers, so implement it here instead of delegating.
        synchronized(mIssuersLock) {
            if (mIssuers == null) {
                java.util.Set<android.security.net.config.TrustAnchor> anchors = mNetworkSecurityConfig.getTrustAnchors();
                java.security.cert.X509Certificate[] issuers = new java.security.cert.X509Certificate[anchors.size()];
                int i = 0;
                for (android.security.net.config.TrustAnchor anchor : anchors) {
                    issuers[i++] = anchor.certificate;
                }
                mIssuers = issuers;
            }
            return mIssuers.clone();
        }
    }

    public void handleTrustStorageUpdate() {
        synchronized(mIssuersLock) {
            mIssuers = null;
            mDelegate.handleTrustStorageUpdate();
        }
    }
}

