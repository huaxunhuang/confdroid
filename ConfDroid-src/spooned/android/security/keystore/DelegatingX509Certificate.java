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
package android.security.keystore;


class DelegatingX509Certificate extends java.security.cert.X509Certificate {
    private final java.security.cert.X509Certificate mDelegate;

    DelegatingX509Certificate(java.security.cert.X509Certificate delegate) {
        mDelegate = delegate;
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getCriticalExtensionOIDs() {
        return mDelegate.getCriticalExtensionOIDs();
    }

    @java.lang.Override
    public byte[] getExtensionValue(java.lang.String oid) {
        return mDelegate.getExtensionValue(oid);
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getNonCriticalExtensionOIDs() {
        return mDelegate.getNonCriticalExtensionOIDs();
    }

    @java.lang.Override
    public boolean hasUnsupportedCriticalExtension() {
        return mDelegate.hasUnsupportedCriticalExtension();
    }

    @java.lang.Override
    public void checkValidity() throws java.security.cert.CertificateExpiredException, java.security.cert.CertificateNotYetValidException {
        mDelegate.checkValidity();
    }

    @java.lang.Override
    public void checkValidity(java.util.Date date) throws java.security.cert.CertificateExpiredException, java.security.cert.CertificateNotYetValidException {
        mDelegate.checkValidity(date);
    }

    @java.lang.Override
    public int getBasicConstraints() {
        return mDelegate.getBasicConstraints();
    }

    @java.lang.Override
    public java.security.Principal getIssuerDN() {
        return mDelegate.getIssuerDN();
    }

    @java.lang.Override
    public boolean[] getIssuerUniqueID() {
        return mDelegate.getIssuerUniqueID();
    }

    @java.lang.Override
    public boolean[] getKeyUsage() {
        return mDelegate.getKeyUsage();
    }

    @java.lang.Override
    public java.util.Date getNotAfter() {
        return mDelegate.getNotAfter();
    }

    @java.lang.Override
    public java.util.Date getNotBefore() {
        return mDelegate.getNotBefore();
    }

    @java.lang.Override
    public java.math.BigInteger getSerialNumber() {
        return mDelegate.getSerialNumber();
    }

    @java.lang.Override
    public java.lang.String getSigAlgName() {
        return mDelegate.getSigAlgName();
    }

    @java.lang.Override
    public java.lang.String getSigAlgOID() {
        return mDelegate.getSigAlgOID();
    }

    @java.lang.Override
    public byte[] getSigAlgParams() {
        return mDelegate.getSigAlgParams();
    }

    @java.lang.Override
    public byte[] getSignature() {
        return mDelegate.getSignature();
    }

    @java.lang.Override
    public java.security.Principal getSubjectDN() {
        return mDelegate.getSubjectDN();
    }

    @java.lang.Override
    public boolean[] getSubjectUniqueID() {
        return mDelegate.getSubjectUniqueID();
    }

    @java.lang.Override
    public byte[] getTBSCertificate() throws java.security.cert.CertificateEncodingException {
        return mDelegate.getTBSCertificate();
    }

    @java.lang.Override
    public int getVersion() {
        return mDelegate.getVersion();
    }

    @java.lang.Override
    public byte[] getEncoded() throws java.security.cert.CertificateEncodingException {
        return mDelegate.getEncoded();
    }

    @java.lang.Override
    public java.security.PublicKey getPublicKey() {
        return mDelegate.getPublicKey();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return mDelegate.toString();
    }

    @java.lang.Override
    public void verify(java.security.PublicKey key) throws java.security.InvalidKeyException, java.security.NoSuchAlgorithmException, java.security.NoSuchProviderException, java.security.SignatureException, java.security.cert.CertificateException {
        mDelegate.verify(key);
    }

    @java.lang.Override
    public void verify(java.security.PublicKey key, java.lang.String sigProvider) throws java.security.InvalidKeyException, java.security.NoSuchAlgorithmException, java.security.NoSuchProviderException, java.security.SignatureException, java.security.cert.CertificateException {
        mDelegate.verify(key, sigProvider);
    }

    @java.lang.Override
    public java.util.List<java.lang.String> getExtendedKeyUsage() throws java.security.cert.CertificateParsingException {
        return mDelegate.getExtendedKeyUsage();
    }

    @java.lang.Override
    public java.util.Collection<java.util.List<?>> getIssuerAlternativeNames() throws java.security.cert.CertificateParsingException {
        return mDelegate.getIssuerAlternativeNames();
    }

    @java.lang.Override
    public javax.security.auth.x500.X500Principal getIssuerX500Principal() {
        return mDelegate.getIssuerX500Principal();
    }

    @java.lang.Override
    public java.util.Collection<java.util.List<?>> getSubjectAlternativeNames() throws java.security.cert.CertificateParsingException {
        return mDelegate.getSubjectAlternativeNames();
    }

    @java.lang.Override
    public javax.security.auth.x500.X500Principal getSubjectX500Principal() {
        return mDelegate.getSubjectX500Principal();
    }
}

