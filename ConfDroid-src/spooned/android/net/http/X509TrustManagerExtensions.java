/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.net.http;


/**
 * X509TrustManager wrapper exposing Android-added features.
 * <p>
 * The checkServerTrusted method allows callers to perform additional
 * verification of certificate chains after they have been successfully verified
 * by the platform.
 * </p>
 */
public class X509TrustManagerExtensions {
    private final com.android.org.conscrypt.TrustManagerImpl mDelegate;

    // Methods to use when mDelegate is not a TrustManagerImpl and duck typing is being used.
    private final javax.net.ssl.X509TrustManager mTrustManager;

    private final java.lang.reflect.Method mCheckServerTrusted;

    private final java.lang.reflect.Method mIsSameTrustConfiguration;

    /**
     * Constructs a new X509TrustManagerExtensions wrapper.
     *
     * @param tm
     * 		A {@link X509TrustManager} as returned by TrustManagerFactory.getInstance();
     * @throws IllegalArgumentException
     * 		If tm is an unsupported TrustManager type.
     */
    public X509TrustManagerExtensions(javax.net.ssl.X509TrustManager tm) throws java.lang.IllegalArgumentException {
        if (tm instanceof com.android.org.conscrypt.TrustManagerImpl) {
            mDelegate = ((com.android.org.conscrypt.TrustManagerImpl) (tm));
            mTrustManager = null;
            mCheckServerTrusted = null;
            mIsSameTrustConfiguration = null;
            return;
        }
        // Use duck typing if possible.
        mDelegate = null;
        mTrustManager = tm;
        // Check that the hostname aware checkServerTrusted is present.
        try {
            mCheckServerTrusted = tm.getClass().getMethod("checkServerTrusted", java.security.cert.X509Certificate[].class, java.lang.String.class, java.lang.String.class);
        } catch (java.lang.NoSuchMethodException e) {
            throw new java.lang.IllegalArgumentException("Required method" + " checkServerTrusted(X509Certificate[], String, String, String) missing");
        }
        // Get the option isSameTrustConfiguration method.
        java.lang.reflect.Method isSameTrustConfiguration = null;
        try {
            isSameTrustConfiguration = tm.getClass().getMethod("isSameTrustConfiguration", java.lang.String.class, java.lang.String.class);
        } catch (java.lang.ReflectiveOperationException ignored) {
        }
        mIsSameTrustConfiguration = isSameTrustConfiguration;
    }

    /**
     * Verifies the given certificate chain.
     *
     * <p>See {@link X509TrustManager#checkServerTrusted(X509Certificate[], String)} for a
     * description of the chain and authType parameters. The final parameter, host, should be the
     * hostname of the server.</p>
     *
     * @throws CertificateException
     * 		if the chain does not verify correctly.
     * @return the properly ordered chain used for verification as a list of X509Certificates.
     */
    public java.util.List<java.security.cert.X509Certificate> checkServerTrusted(java.security.cert.X509Certificate[] chain, java.lang.String authType, java.lang.String host) throws java.security.cert.CertificateException {
        if (mDelegate != null) {
            return mDelegate.checkServerTrusted(chain, authType, host);
        } else {
            try {
                return ((java.util.List<java.security.cert.X509Certificate>) (mCheckServerTrusted.invoke(mTrustManager, chain, authType, host)));
            } catch (java.lang.IllegalAccessException e) {
                throw new java.security.cert.CertificateException("Failed to call checkServerTrusted", e);
            } catch (java.lang.reflect.InvocationTargetException e) {
                if (e.getCause() instanceof java.security.cert.CertificateException) {
                    throw ((java.security.cert.CertificateException) (e.getCause()));
                }
                if (e.getCause() instanceof java.lang.RuntimeException) {
                    throw ((java.lang.RuntimeException) (e.getCause()));
                }
                throw new java.security.cert.CertificateException("checkServerTrusted failed", e.getCause());
            }
        }
    }

    /**
     * Checks whether a CA certificate is added by an user.
     *
     * <p>Since {@link X509TrustManager#checkServerTrusted} may allow its parameter {@code chain} to
     * chain up to user-added CA certificates, this method can be used to perform additional
     * policies for user-added CA certificates.
     *
     * @return {@code true} to indicate that the certificate authority exists in the user added
    certificate store, {@code false} otherwise.
     */
    public boolean isUserAddedCertificate(java.security.cert.X509Certificate cert) {
        return android.security.net.config.UserCertificateSource.getInstance().findBySubjectAndPublicKey(cert) != null;
    }

    /**
     * Returns {@code true} if the TrustManager uses the same trust configuration for the provided
     * hostnames.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public boolean isSameTrustConfiguration(java.lang.String hostname1, java.lang.String hostname2) {
        if (mIsSameTrustConfiguration == null) {
            return true;
        }
        try {
            return ((java.lang.Boolean) (mIsSameTrustConfiguration.invoke(mTrustManager, hostname1, hostname2)));
        } catch (java.lang.IllegalAccessException e) {
            throw new java.lang.RuntimeException("Failed to call isSameTrustConfiguration", e);
        } catch (java.lang.reflect.InvocationTargetException e) {
            if (e.getCause() instanceof java.lang.RuntimeException) {
                throw ((java.lang.RuntimeException) (e.getCause()));
            } else {
                throw new java.lang.RuntimeException("isSameTrustConfiguration failed", e.getCause());
            }
        }
    }
}

