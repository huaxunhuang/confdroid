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
 * {@link CertificateSource} based on certificates contained in an application resource file.
 *
 * @unknown 
 */
public class ResourceCertificateSource implements android.security.net.config.CertificateSource {
    private final java.lang.Object mLock = new java.lang.Object();

    private final int mResourceId;

    private java.util.Set<java.security.cert.X509Certificate> mCertificates;

    private android.content.Context mContext;

    private com.android.org.conscrypt.TrustedCertificateIndex mIndex;

    public ResourceCertificateSource(int resourceId, android.content.Context context) {
        mResourceId = resourceId;
        mContext = context;
    }

    private void ensureInitialized() {
        synchronized(mLock) {
            if (mCertificates != null) {
                return;
            }
            java.util.Set<java.security.cert.X509Certificate> certificates = new android.util.ArraySet<java.security.cert.X509Certificate>();
            java.util.Collection<? extends java.security.cert.Certificate> certs;
            java.io.InputStream in = null;
            try {
                java.security.cert.CertificateFactory factory = java.security.cert.CertificateFactory.getInstance("X.509");
                in = mContext.getResources().openRawResource(mResourceId);
                certs = factory.generateCertificates(in);
            } catch (java.security.cert.CertificateException e) {
                throw new java.lang.RuntimeException("Failed to load trust anchors from id " + mResourceId, e);
            } finally {
                libcore.io.IoUtils.closeQuietly(in);
            }
            com.android.org.conscrypt.TrustedCertificateIndex indexLocal = new com.android.org.conscrypt.TrustedCertificateIndex();
            for (java.security.cert.Certificate cert : certs) {
                certificates.add(((java.security.cert.X509Certificate) (cert)));
                indexLocal.index(((java.security.cert.X509Certificate) (cert)));
            }
            mCertificates = certificates;
            mIndex = indexLocal;
            mContext = null;
        }
    }

    @java.lang.Override
    public java.util.Set<java.security.cert.X509Certificate> getCertificates() {
        ensureInitialized();
        return mCertificates;
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
        // Nothing to do, resource sources never change.
    }
}

