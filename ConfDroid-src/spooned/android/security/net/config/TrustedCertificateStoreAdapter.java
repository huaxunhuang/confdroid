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
public class TrustedCertificateStoreAdapter extends com.android.org.conscrypt.TrustedCertificateStore {
    private final android.security.net.config.NetworkSecurityConfig mConfig;

    public TrustedCertificateStoreAdapter(android.security.net.config.NetworkSecurityConfig config) {
        mConfig = config;
    }

    @java.lang.Override
    public java.security.cert.X509Certificate findIssuer(java.security.cert.X509Certificate cert) {
        android.security.net.config.TrustAnchor anchor = mConfig.findTrustAnchorByIssuerAndSignature(cert);
        if (anchor == null) {
            return null;
        }
        return anchor.certificate;
    }

    @java.lang.Override
    public java.util.Set<java.security.cert.X509Certificate> findAllIssuers(java.security.cert.X509Certificate cert) {
        return mConfig.findAllCertificatesByIssuerAndSignature(cert);
    }

    @java.lang.Override
    public java.security.cert.X509Certificate getTrustAnchor(java.security.cert.X509Certificate cert) {
        android.security.net.config.TrustAnchor anchor = mConfig.findTrustAnchorBySubjectAndPublicKey(cert);
        if (anchor == null) {
            return null;
        }
        return anchor.certificate;
    }

    @java.lang.Override
    public boolean isUserAddedCertificate(java.security.cert.X509Certificate cert) {
        // isUserAddedCertificate is used only for pinning overrides, so use overridesPins here.
        android.security.net.config.TrustAnchor anchor = mConfig.findTrustAnchorBySubjectAndPublicKey(cert);
        if (anchor == null) {
            return false;
        }
        return anchor.overridesPins;
    }

    @java.lang.Override
    public java.io.File getCertificateFile(java.io.File dir, java.security.cert.X509Certificate x) {
        // getCertificateFile is only used for tests, do not support it here.
        throw new java.lang.UnsupportedOperationException();
    }

    // The methods below are exposed in TrustedCertificateStore but not used by conscrypt, do not
    // support them.
    @java.lang.Override
    public java.security.cert.Certificate getCertificate(java.lang.String alias) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.security.cert.Certificate getCertificate(java.lang.String alias, boolean includeDeletedSystem) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.Date getCreationDate(java.lang.String alias) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> aliases() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> userAliases() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> allSystemAliases() {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public boolean containsAlias(java.lang.String alias) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String getCertificateAlias(java.security.cert.Certificate c) {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.lang.String getCertificateAlias(java.security.cert.Certificate c, boolean includeDeletedSystem) {
        throw new java.lang.UnsupportedOperationException();
    }
}

