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
 * {@link ConfigSource} with a single default config based on a {@link KeyStore} and no per domain
 * configs.
 */
class KeyStoreConfigSource implements android.security.net.config.ConfigSource {
    private final android.security.net.config.NetworkSecurityConfig mConfig;

    public KeyStoreConfigSource(java.security.KeyStore ks) {
        mConfig = // Use the KeyStore and do not override pins (of which there are none).
        new android.security.net.config.NetworkSecurityConfig.Builder().addCertificatesEntryRef(new android.security.net.config.CertificatesEntryRef(new android.security.net.config.KeyStoreCertificateSource(ks), false)).build();
    }

    @java.lang.Override
    public java.util.Set<android.util.Pair<android.security.net.config.Domain, android.security.net.config.NetworkSecurityConfig>> getPerDomainConfigs() {
        return null;
    }

    @java.lang.Override
    public android.security.net.config.NetworkSecurityConfig getDefaultConfig() {
        return mConfig;
    }
}

