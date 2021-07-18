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
public class ManifestConfigSource implements android.security.net.config.ConfigSource {
    private static final boolean DBG = true;

    private static final java.lang.String LOG_TAG = "NetworkSecurityConfig";

    private final java.lang.Object mLock = new java.lang.Object();

    private final android.content.Context mContext;

    private final int mApplicationInfoFlags;

    private final int mTargetSdkVersion;

    private final int mConfigResourceId;

    private android.security.net.config.ConfigSource mConfigSource;

    public ManifestConfigSource(android.content.Context context) {
        mContext = context;
        // Cache values because ApplicationInfo is mutable and apps do modify it :(
        android.content.pm.ApplicationInfo info = context.getApplicationInfo();
        mApplicationInfoFlags = info.flags;
        mTargetSdkVersion = info.targetSdkVersion;
        mConfigResourceId = info.networkSecurityConfigRes;
    }

    @java.lang.Override
    public java.util.Set<android.util.Pair<android.security.net.config.Domain, android.security.net.config.NetworkSecurityConfig>> getPerDomainConfigs() {
        return getConfigSource().getPerDomainConfigs();
    }

    @java.lang.Override
    public android.security.net.config.NetworkSecurityConfig getDefaultConfig() {
        return getConfigSource().getDefaultConfig();
    }

    private android.security.net.config.ConfigSource getConfigSource() {
        synchronized(mLock) {
            if (mConfigSource != null) {
                return mConfigSource;
            }
            android.security.net.config.ConfigSource source;
            if (mConfigResourceId != 0) {
                boolean debugBuild = (mApplicationInfoFlags & android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0;
                if (android.security.net.config.ManifestConfigSource.DBG) {
                    android.util.Log.d(android.security.net.config.ManifestConfigSource.LOG_TAG, (("Using Network Security Config from resource " + mContext.getResources().getResourceEntryName(mConfigResourceId)) + " debugBuild: ") + debugBuild);
                }
                source = new android.security.net.config.XmlConfigSource(mContext, mConfigResourceId, debugBuild, mTargetSdkVersion);
            } else {
                if (android.security.net.config.ManifestConfigSource.DBG) {
                    android.util.Log.d(android.security.net.config.ManifestConfigSource.LOG_TAG, "No Network Security Config specified, using platform default");
                }
                boolean usesCleartextTraffic = (mApplicationInfoFlags & android.content.pm.ApplicationInfo.FLAG_USES_CLEARTEXT_TRAFFIC) != 0;
                source = new android.security.net.config.ManifestConfigSource.DefaultConfigSource(usesCleartextTraffic, mTargetSdkVersion);
            }
            mConfigSource = source;
            return mConfigSource;
        }
    }

    private static final class DefaultConfigSource implements android.security.net.config.ConfigSource {
        private final android.security.net.config.NetworkSecurityConfig mDefaultConfig;

        public DefaultConfigSource(boolean usesCleartextTraffic, int targetSdkVersion) {
            mDefaultConfig = android.security.net.config.NetworkSecurityConfig.getDefaultBuilder(targetSdkVersion).setCleartextTrafficPermitted(usesCleartextTraffic).build();
        }

        @java.lang.Override
        public android.security.net.config.NetworkSecurityConfig getDefaultConfig() {
            return mDefaultConfig;
        }

        @java.lang.Override
        public java.util.Set<android.util.Pair<android.security.net.config.Domain, android.security.net.config.NetworkSecurityConfig>> getPerDomainConfigs() {
            return null;
        }
    }
}

