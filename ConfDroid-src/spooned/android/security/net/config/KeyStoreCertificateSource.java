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
 * {@link CertificateSource} which provides certificates from trusted certificate entries of a
 * {@link KeyStore}.
 */
class KeyStoreCertificateSource implements android.security.net.config.CertificateSource {
    private final java.lang.Object mLock = new java.lang.Object();

    private final java.security.KeyStore mKeyStore;

    private com.android.org.conscrypt.TrustedCertificateIndex mIndex;

    private java.util.Set<java.security.cert.X509Certificate> mCertificates;

    public KeyStoreCertificateSource(java.security.KeyStore ks) {
        mKeyStore = ks;
    }

    @java.lang.Override
    public java.util.Set<java.security.cert.X509Certificate> getCertificates() {
        ensureInitialized();
        return mCertificates;
    }

    private void ensureInitialized() {
        synchronized(mLock) {
            if (mCertificates != null) {
                return;
            }
            try {
                com.android.org.conscrypt.TrustedCertificateIndex localIndex = new com.android.org.conscrypt.TrustedCertificateIndex();
                java.util.Set<java.security.cert.X509Certificate> certificates = new android.util.ArraySet<>(mKeyStore.size());
                for (java.util.Enumeration<java.lang.String> en = mKeyStore.aliases(); en.hasMoreElements();) {
                    java.lang.String alias = en.nextElement();
                    java.security.cert.X509Certificate cert = ((java.security.cert.X509Certificate) (mKeyStore.getCertificate(alias)));
                    if (cert != null) {
                        certificates.add(cert);
                        localIndex.index(cert);
                    }
                }
                mIndex = localIndex;
                mCertificates = certificates;
            } catch (java.security.KeyStoreException e) {
                throw new java.lang.RuntimeException("Failed to load certificates from KeyStore", e);
            }
        }
    }

    @java.lang.Override
    public java.security.cert.X509Certificate findBySubjectAndPublicKey(java.security.cert.X509Certificate cert) {
        ensureInitialized();
        java.security.cert.TrustAnchor anchor = mIndex.findBySubjectAndPublicKey(cert);
        if (anchor == null) {
            return null;
        }
        return anchor.getTrustedCert();
    }

    @java.lang.Override
    public java.security.cert.X509Certificate findByIssuerAndSignature(java.security.cert.X509Certificate cert) {
        ensureInitialized();
        java.security.cert.TrustAnchor anchor = mIndex.findByIssuerAndSignature(cert);
        if (anchor == null) {
            return null;
        }
        return anchor.getTrustedCert();
    }

    @java.lang.Override
    public java.util.Set<java.security.cert.X509Certificate> findAllByIssuerAndSignature(java.security.cert.X509Certificate cert) {
        ensureInitialized();
        java.util.Set<java.security.cert.TrustAnchor> anchors = mIndex.findAllByIssuerAndSignature(cert);
        if (anchors.isEmpty()) {
            return java.util.Collections.<java.security.cert.X509Certificate>emptySet();
        }
        java.util.Set<java.security.cert.X509Certificate> certs = new android.util.ArraySet<java.security.cert.X509Certificate>(anchors.size());
        for (java.security.cert.TrustAnchor anchor : anchors) {
            certs.add(anchor.getTrustedCert());
        }
        return certs;
    }

    @java.lang.Override
    public void handleTrustStorageUpdate() {
        // Nothing to do.
    }
}

