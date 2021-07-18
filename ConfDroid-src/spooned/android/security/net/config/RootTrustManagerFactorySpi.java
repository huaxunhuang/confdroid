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
public class RootTrustManagerFactorySpi extends javax.net.ssl.TrustManagerFactorySpi {
    private android.security.net.config.ApplicationConfig mApplicationConfig;

    private android.security.net.config.NetworkSecurityConfig mConfig;

    @java.lang.Override
    public void engineInit(javax.net.ssl.ManagerFactoryParameters spec) throws java.security.InvalidAlgorithmParameterException {
        if (!(spec instanceof android.security.net.config.RootTrustManagerFactorySpi.ApplicationConfigParameters)) {
            throw new java.security.InvalidAlgorithmParameterException(((("Unsupported spec: " + spec) + ". Only ") + android.security.net.config.RootTrustManagerFactorySpi.ApplicationConfigParameters.class.getName()) + " supported");
        }
        mApplicationConfig = ((android.security.net.config.RootTrustManagerFactorySpi.ApplicationConfigParameters) (spec)).config;
    }

    @java.lang.Override
    public void engineInit(java.security.KeyStore ks) throws java.security.KeyStoreException {
        if (ks != null) {
            mApplicationConfig = new android.security.net.config.ApplicationConfig(new android.security.net.config.KeyStoreConfigSource(ks));
        } else {
            mApplicationConfig = android.security.net.config.ApplicationConfig.getDefaultInstance();
        }
    }

    @java.lang.Override
    public javax.net.ssl.TrustManager[] engineGetTrustManagers() {
        if (mApplicationConfig == null) {
            throw new java.lang.IllegalStateException("TrustManagerFactory not initialized");
        }
        return new javax.net.ssl.TrustManager[]{ mApplicationConfig.getTrustManager() };
    }

    @com.android.internal.annotations.VisibleForTesting
    public static final class ApplicationConfigParameters implements javax.net.ssl.ManagerFactoryParameters {
        public final android.security.net.config.ApplicationConfig config;

        public ApplicationConfigParameters(android.security.net.config.ApplicationConfig config) {
            this.config = config;
        }
    }
}

