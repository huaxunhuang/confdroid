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
public final class NetworkSecurityConfig {
    /**
     *
     *
     * @unknown 
     */
    public static final boolean DEFAULT_CLEARTEXT_TRAFFIC_PERMITTED = true;

    /**
     *
     *
     * @unknown 
     */
    public static final boolean DEFAULT_HSTS_ENFORCED = false;

    private final boolean mCleartextTrafficPermitted;

    private final boolean mHstsEnforced;

    private final android.security.net.config.PinSet mPins;

    private final java.util.List<android.security.net.config.CertificatesEntryRef> mCertificatesEntryRefs;

    private java.util.Set<android.security.net.config.TrustAnchor> mAnchors;

    private final java.lang.Object mAnchorsLock = new java.lang.Object();

    private android.security.net.config.NetworkSecurityTrustManager mTrustManager;

    private final java.lang.Object mTrustManagerLock = new java.lang.Object();

    private NetworkSecurityConfig(boolean cleartextTrafficPermitted, boolean hstsEnforced, android.security.net.config.PinSet pins, java.util.List<android.security.net.config.CertificatesEntryRef> certificatesEntryRefs) {
        mCleartextTrafficPermitted = cleartextTrafficPermitted;
        mHstsEnforced = hstsEnforced;
        mPins = pins;
        mCertificatesEntryRefs = certificatesEntryRefs;
        // Sort the certificates entry refs so that all entries that override pins come before
        // non-override pin entries. This allows us to handle the case where a certificate is in
        // multiple entry refs by returning the certificate from the first entry ref.
        java.util.Collections.sort(mCertificatesEntryRefs, new java.util.Comparator<android.security.net.config.CertificatesEntryRef>() {
            @java.lang.Override
            public int compare(android.security.net.config.CertificatesEntryRef lhs, android.security.net.config.CertificatesEntryRef rhs) {
                if (lhs.overridesPins()) {
                    return rhs.overridesPins() ? 0 : -1;
                } else {
                    return rhs.overridesPins() ? 1 : 0;
                }
            }
        });
    }

    public java.util.Set<android.security.net.config.TrustAnchor> getTrustAnchors() {
        synchronized(mAnchorsLock) {
            if (mAnchors != null) {
                return mAnchors;
            }
            // Merge trust anchors based on the X509Certificate.
            // If we see the same certificate in two TrustAnchors, one with overridesPins and one
            // without, the one with overridesPins wins.
            // Because mCertificatesEntryRefs is sorted with all overridesPins anchors coming first
            // this can be simplified to just using the first occurrence of a certificate.
            java.util.Map<java.security.cert.X509Certificate, android.security.net.config.TrustAnchor> anchorMap = new android.util.ArrayMap<>();
            for (android.security.net.config.CertificatesEntryRef ref : mCertificatesEntryRefs) {
                java.util.Set<android.security.net.config.TrustAnchor> anchors = ref.getTrustAnchors();
                for (android.security.net.config.TrustAnchor anchor : anchors) {
                    java.security.cert.X509Certificate cert = anchor.certificate;
                    if (!anchorMap.containsKey(cert)) {
                        anchorMap.put(cert, anchor);
                    }
                }
            }
            android.util.ArraySet<android.security.net.config.TrustAnchor> anchors = new android.util.ArraySet<android.security.net.config.TrustAnchor>(anchorMap.size());
            anchors.addAll(anchorMap.values());
            mAnchors = anchors;
            return mAnchors;
        }
    }

    public boolean isCleartextTrafficPermitted() {
        return mCleartextTrafficPermitted;
    }

    public boolean isHstsEnforced() {
        return mHstsEnforced;
    }

    public android.security.net.config.PinSet getPins() {
        return mPins;
    }

    public android.security.net.config.NetworkSecurityTrustManager getTrustManager() {
        synchronized(mTrustManagerLock) {
            if (mTrustManager == null) {
                mTrustManager = new android.security.net.config.NetworkSecurityTrustManager(this);
            }
            return mTrustManager;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public android.security.net.config.TrustAnchor findTrustAnchorBySubjectAndPublicKey(java.security.cert.X509Certificate cert) {
        for (android.security.net.config.CertificatesEntryRef ref : mCertificatesEntryRefs) {
            android.security.net.config.TrustAnchor anchor = ref.findBySubjectAndPublicKey(cert);
            if (anchor != null) {
                return anchor;
            }
        }
        return null;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.security.net.config.TrustAnchor findTrustAnchorByIssuerAndSignature(java.security.cert.X509Certificate cert) {
        for (android.security.net.config.CertificatesEntryRef ref : mCertificatesEntryRefs) {
            android.security.net.config.TrustAnchor anchor = ref.findByIssuerAndSignature(cert);
            if (anchor != null) {
                return anchor;
            }
        }
        return null;
    }

    /**
     *
     *
     * @unknown 
     */
    public java.util.Set<java.security.cert.X509Certificate> findAllCertificatesByIssuerAndSignature(java.security.cert.X509Certificate cert) {
        java.util.Set<java.security.cert.X509Certificate> certs = new android.util.ArraySet<java.security.cert.X509Certificate>();
        for (android.security.net.config.CertificatesEntryRef ref : mCertificatesEntryRefs) {
            certs.addAll(ref.findAllCertificatesByIssuerAndSignature(cert));
        }
        return certs;
    }

    public void handleTrustStorageUpdate() {
        synchronized(mAnchorsLock) {
            mAnchors = null;
            for (android.security.net.config.CertificatesEntryRef ref : mCertificatesEntryRefs) {
                ref.handleTrustStorageUpdate();
            }
        }
        getTrustManager().handleTrustStorageUpdate();
    }

    /**
     * Return a {@link Builder} for the default {@code NetworkSecurityConfig}.
     *
     * <p>
     * The default configuration has the following properties:
     * <ol>
     * <li>Cleartext traffic is permitted.</li>
     * <li>HSTS is not enforced.</li>
     * <li>No certificate pinning is used.</li>
     * <li>The system certificate store is trusted for connections.</li>
     * <li>If the application targets API level 23 (Android M) or lower then the user certificate
     * store is trusted by default as well.</li>
     * </ol>
     *
     * @unknown 
     */
    public static final android.security.net.config.NetworkSecurityConfig.Builder getDefaultBuilder(int targetSdkVersion) {
        android.security.net.config.NetworkSecurityConfig.Builder builder = // System certificate store, does not bypass static pins.
        new android.security.net.config.NetworkSecurityConfig.Builder().setCleartextTrafficPermitted(android.security.net.config.NetworkSecurityConfig.DEFAULT_CLEARTEXT_TRAFFIC_PERMITTED).setHstsEnforced(android.security.net.config.NetworkSecurityConfig.DEFAULT_HSTS_ENFORCED).addCertificatesEntryRef(new android.security.net.config.CertificatesEntryRef(android.security.net.config.SystemCertificateSource.getInstance(), false));
        // Applications targeting N and above must opt in into trusting the user added certificate
        // store.
        if (targetSdkVersion <= android.os.Build.VERSION_CODES.M) {
            // User certificate store, does not bypass static pins.
            builder.addCertificatesEntryRef(new android.security.net.config.CertificatesEntryRef(android.security.net.config.UserCertificateSource.getInstance(), false));
        }
        return builder;
    }

    /**
     * Builder for creating {@code NetworkSecurityConfig} objects.
     *
     * @unknown 
     */
    public static final class Builder {
        private java.util.List<android.security.net.config.CertificatesEntryRef> mCertificatesEntryRefs;

        private android.security.net.config.PinSet mPinSet;

        private boolean mCleartextTrafficPermitted = android.security.net.config.NetworkSecurityConfig.DEFAULT_CLEARTEXT_TRAFFIC_PERMITTED;

        private boolean mHstsEnforced = android.security.net.config.NetworkSecurityConfig.DEFAULT_HSTS_ENFORCED;

        private boolean mCleartextTrafficPermittedSet = false;

        private boolean mHstsEnforcedSet = false;

        private android.security.net.config.NetworkSecurityConfig.Builder mParentBuilder;

        /**
         * Sets the parent {@code Builder} for this {@code Builder}.
         * The parent will be used to determine values not configured in this {@code Builder}
         * in {@link Builder#build()}, recursively if needed.
         */
        public android.security.net.config.NetworkSecurityConfig.Builder setParent(android.security.net.config.NetworkSecurityConfig.Builder parent) {
            // Sanity check to avoid adding loops.
            android.security.net.config.NetworkSecurityConfig.Builder current = parent;
            while (current != null) {
                if (current == this) {
                    throw new java.lang.IllegalArgumentException("Loops are not allowed in Builder parents");
                }
                current = current.getParent();
            } 
            mParentBuilder = parent;
            return this;
        }

        public android.security.net.config.NetworkSecurityConfig.Builder getParent() {
            return mParentBuilder;
        }

        public android.security.net.config.NetworkSecurityConfig.Builder setPinSet(android.security.net.config.PinSet pinSet) {
            mPinSet = pinSet;
            return this;
        }

        private android.security.net.config.PinSet getEffectivePinSet() {
            if (mPinSet != null) {
                return mPinSet;
            }
            if (mParentBuilder != null) {
                return mParentBuilder.getEffectivePinSet();
            }
            return android.security.net.config.PinSet.EMPTY_PINSET;
        }

        public android.security.net.config.NetworkSecurityConfig.Builder setCleartextTrafficPermitted(boolean cleartextTrafficPermitted) {
            mCleartextTrafficPermitted = cleartextTrafficPermitted;
            mCleartextTrafficPermittedSet = true;
            return this;
        }

        private boolean getEffectiveCleartextTrafficPermitted() {
            if (mCleartextTrafficPermittedSet) {
                return mCleartextTrafficPermitted;
            }
            if (mParentBuilder != null) {
                return mParentBuilder.getEffectiveCleartextTrafficPermitted();
            }
            return android.security.net.config.NetworkSecurityConfig.DEFAULT_CLEARTEXT_TRAFFIC_PERMITTED;
        }

        public android.security.net.config.NetworkSecurityConfig.Builder setHstsEnforced(boolean hstsEnforced) {
            mHstsEnforced = hstsEnforced;
            mHstsEnforcedSet = true;
            return this;
        }

        private boolean getEffectiveHstsEnforced() {
            if (mHstsEnforcedSet) {
                return mHstsEnforced;
            }
            if (mParentBuilder != null) {
                return mParentBuilder.getEffectiveHstsEnforced();
            }
            return android.security.net.config.NetworkSecurityConfig.DEFAULT_HSTS_ENFORCED;
        }

        public android.security.net.config.NetworkSecurityConfig.Builder addCertificatesEntryRef(android.security.net.config.CertificatesEntryRef ref) {
            if (mCertificatesEntryRefs == null) {
                mCertificatesEntryRefs = new java.util.ArrayList<android.security.net.config.CertificatesEntryRef>();
            }
            mCertificatesEntryRefs.add(ref);
            return this;
        }

        public android.security.net.config.NetworkSecurityConfig.Builder addCertificatesEntryRefs(java.util.Collection<? extends android.security.net.config.CertificatesEntryRef> refs) {
            if (mCertificatesEntryRefs == null) {
                mCertificatesEntryRefs = new java.util.ArrayList<android.security.net.config.CertificatesEntryRef>();
            }
            mCertificatesEntryRefs.addAll(refs);
            return this;
        }

        private java.util.List<android.security.net.config.CertificatesEntryRef> getEffectiveCertificatesEntryRefs() {
            if (mCertificatesEntryRefs != null) {
                return mCertificatesEntryRefs;
            }
            if (mParentBuilder != null) {
                return mParentBuilder.getEffectiveCertificatesEntryRefs();
            }
            return java.util.Collections.<android.security.net.config.CertificatesEntryRef>emptyList();
        }

        public boolean hasCertificatesEntryRefs() {
            return mCertificatesEntryRefs != null;
        }

        java.util.List<android.security.net.config.CertificatesEntryRef> getCertificatesEntryRefs() {
            return mCertificatesEntryRefs;
        }

        public android.security.net.config.NetworkSecurityConfig build() {
            boolean cleartextPermitted = getEffectiveCleartextTrafficPermitted();
            boolean hstsEnforced = getEffectiveHstsEnforced();
            android.security.net.config.PinSet pinSet = getEffectivePinSet();
            java.util.List<android.security.net.config.CertificatesEntryRef> entryRefs = getEffectiveCertificatesEntryRefs();
            return new android.security.net.config.NetworkSecurityConfig(cleartextPermitted, hstsEnforced, pinSet, entryRefs);
        }
    }
}

