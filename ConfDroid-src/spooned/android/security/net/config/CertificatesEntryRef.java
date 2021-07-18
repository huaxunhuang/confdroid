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
 *
 *
 * @unknown 
 */
public final class CertificatesEntryRef {
    private final android.security.net.config.CertificateSource mSource;

    private final boolean mOverridesPins;

    public CertificatesEntryRef(android.security.net.config.CertificateSource source, boolean overridesPins) {
        mSource = source;
        mOverridesPins = overridesPins;
    }

    boolean overridesPins() {
        return mOverridesPins;
    }

    public java.util.Set<android.security.net.config.TrustAnchor> getTrustAnchors() {
        // TODO: cache this [but handle mutable sources]
        java.util.Set<android.security.net.config.TrustAnchor> anchors = new android.util.ArraySet<android.security.net.config.TrustAnchor>();
        for (java.security.cert.X509Certificate cert : mSource.getCertificates()) {
            anchors.add(new android.security.net.config.TrustAnchor(cert, mOverridesPins));
        }
        return anchors;
    }

    public android.security.net.config.TrustAnchor findBySubjectAndPublicKey(java.security.cert.X509Certificate cert) {
        java.security.cert.X509Certificate foundCert = mSource.findBySubjectAndPublicKey(cert);
        if (foundCert == null) {
            return null;
        }
        return new android.security.net.config.TrustAnchor(foundCert, mOverridesPins);
    }

    public android.security.net.config.TrustAnchor findByIssuerAndSignature(java.security.cert.X509Certificate cert) {
        java.security.cert.X509Certificate foundCert = mSource.findByIssuerAndSignature(cert);
        if (foundCert == null) {
            return null;
        }
        return new android.security.net.config.TrustAnchor(foundCert, mOverridesPins);
    }

    public java.util.Set<java.security.cert.X509Certificate> findAllCertificatesByIssuerAndSignature(java.security.cert.X509Certificate cert) {
        return mSource.findAllByIssuerAndSignature(cert);
    }

    public void handleTrustStorageUpdate() {
        mSource.handleTrustStorageUpdate();
    }
}

