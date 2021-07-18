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
 * {@link CertificateSource} based on a directory where certificates are stored as individual files
 * named after a hash of their SubjectName for more efficient lookups.
 *
 * @unknown 
 */
abstract class DirectoryCertificateSource implements android.security.net.config.CertificateSource {
    private static final java.lang.String LOG_TAG = "DirectoryCertificateSrc";

    private final java.io.File mDir;

    private final java.lang.Object mLock = new java.lang.Object();

    private final java.security.cert.CertificateFactory mCertFactory;

    private java.util.Set<java.security.cert.X509Certificate> mCertificates;

    protected DirectoryCertificateSource(java.io.File caDir) {
        mDir = caDir;
        try {
            mCertFactory = java.security.cert.CertificateFactory.getInstance("X.509");
        } catch (java.security.cert.CertificateException e) {
            throw new java.lang.RuntimeException("Failed to obtain X.509 CertificateFactory", e);
        }
    }

    protected abstract boolean isCertMarkedAsRemoved(java.lang.String caFile);

    @java.lang.Override
    public java.util.Set<java.security.cert.X509Certificate> getCertificates() {
        // TODO: loading all of these is wasteful, we should instead use a keystore style API.
        synchronized(mLock) {
            if (mCertificates != null) {
                return mCertificates;
            }
            java.util.Set<java.security.cert.X509Certificate> certs = new android.util.ArraySet<java.security.cert.X509Certificate>();
            if (mDir.isDirectory()) {
                for (java.lang.String caFile : mDir.list()) {
                    if (isCertMarkedAsRemoved(caFile)) {
                        continue;
                    }
                    java.security.cert.X509Certificate cert = readCertificate(caFile);
                    if (cert != null) {
                        certs.add(cert);
                    }
                }
            }
            mCertificates = certs;
            return mCertificates;
        }
    }

    @java.lang.Override
    public java.security.cert.X509Certificate findBySubjectAndPublicKey(final java.security.cert.X509Certificate cert) {
        return findCert(cert.getSubjectX500Principal(), new android.security.net.config.DirectoryCertificateSource.CertSelector() {
            @java.lang.Override
            public boolean match(java.security.cert.X509Certificate ca) {
                return ca.getPublicKey().equals(cert.getPublicKey());
            }
        });
    }

    @java.lang.Override
    public java.security.cert.X509Certificate findByIssuerAndSignature(final java.security.cert.X509Certificate cert) {
        return findCert(cert.getIssuerX500Principal(), new android.security.net.config.DirectoryCertificateSource.CertSelector() {
            @java.lang.Override
            public boolean match(java.security.cert.X509Certificate ca) {
                try {
                    cert.verify(ca.getPublicKey());
                    return true;
                } catch (java.lang.Exception e) {
                    return false;
                }
            }
        });
    }

    @java.lang.Override
    public java.util.Set<java.security.cert.X509Certificate> findAllByIssuerAndSignature(final java.security.cert.X509Certificate cert) {
        return findCerts(cert.getIssuerX500Principal(), new android.security.net.config.DirectoryCertificateSource.CertSelector() {
            @java.lang.Override
            public boolean match(java.security.cert.X509Certificate ca) {
                try {
                    cert.verify(ca.getPublicKey());
                    return true;
                } catch (java.lang.Exception e) {
                    return false;
                }
            }
        });
    }

    @java.lang.Override
    public void handleTrustStorageUpdate() {
        synchronized(mLock) {
            mCertificates = null;
        }
    }

    private static interface CertSelector {
        boolean match(java.security.cert.X509Certificate cert);
    }

    private java.util.Set<java.security.cert.X509Certificate> findCerts(javax.security.auth.x500.X500Principal subj, android.security.net.config.DirectoryCertificateSource.CertSelector selector) {
        java.lang.String hash = getHash(subj);
        java.util.Set<java.security.cert.X509Certificate> certs = null;
        for (int index = 0; index >= 0; index++) {
            java.lang.String fileName = (hash + ".") + index;
            if (!new java.io.File(mDir, fileName).exists()) {
                break;
            }
            if (isCertMarkedAsRemoved(fileName)) {
                continue;
            }
            java.security.cert.X509Certificate cert = readCertificate(fileName);
            if (cert == null) {
                continue;
            }
            if (!subj.equals(cert.getSubjectX500Principal())) {
                continue;
            }
            if (selector.match(cert)) {
                if (certs == null) {
                    certs = new android.util.ArraySet<java.security.cert.X509Certificate>();
                }
                certs.add(cert);
            }
        }
        return certs != null ? certs : java.util.Collections.<java.security.cert.X509Certificate>emptySet();
    }

    private java.security.cert.X509Certificate findCert(javax.security.auth.x500.X500Principal subj, android.security.net.config.DirectoryCertificateSource.CertSelector selector) {
        java.lang.String hash = getHash(subj);
        for (int index = 0; index >= 0; index++) {
            java.lang.String fileName = (hash + ".") + index;
            if (!new java.io.File(mDir, fileName).exists()) {
                break;
            }
            if (isCertMarkedAsRemoved(fileName)) {
                continue;
            }
            java.security.cert.X509Certificate cert = readCertificate(fileName);
            if (cert == null) {
                continue;
            }
            if (!subj.equals(cert.getSubjectX500Principal())) {
                continue;
            }
            if (selector.match(cert)) {
                return cert;
            }
        }
        return null;
    }

    private java.lang.String getHash(javax.security.auth.x500.X500Principal name) {
        int hash = com.android.org.conscrypt.NativeCrypto.X509_NAME_hash_old(name);
        return com.android.org.conscrypt.Hex.intToHexString(hash, 8);
    }

    private java.security.cert.X509Certificate readCertificate(java.lang.String file) {
        java.io.InputStream is = null;
        try {
            is = new java.io.BufferedInputStream(new java.io.FileInputStream(new java.io.File(mDir, file)));
            return ((java.security.cert.X509Certificate) (mCertFactory.generateCertificate(is)));
        } catch (java.security.cert.CertificateException | java.io.IOException e) {
            android.util.Log.e(android.security.net.config.DirectoryCertificateSource.LOG_TAG, "Failed to read certificate from " + file, e);
            return null;
        } finally {
            libcore.io.IoUtils.closeQuietly(is);
        }
    }
}

