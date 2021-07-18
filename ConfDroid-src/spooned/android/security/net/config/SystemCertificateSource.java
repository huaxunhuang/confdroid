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
 * {@link CertificateSource} based on the system trusted CA store.
 *
 * @unknown 
 */
public final class SystemCertificateSource extends android.security.net.config.DirectoryCertificateSource {
    private static class NoPreloadHolder {
        private static final android.security.net.config.SystemCertificateSource INSTANCE = new android.security.net.config.SystemCertificateSource();
    }

    private final java.io.File mUserRemovedCaDir;

    private SystemCertificateSource() {
        super(new java.io.File(java.lang.System.getenv("ANDROID_ROOT") + "/etc/security/cacerts"));
        java.io.File configDir = android.os.Environment.getUserConfigDirectory(android.os.UserHandle.myUserId());
        mUserRemovedCaDir = new java.io.File(configDir, "cacerts-removed");
    }

    public static android.security.net.config.SystemCertificateSource getInstance() {
        return android.security.net.config.SystemCertificateSource.NoPreloadHolder.INSTANCE;
    }

    @java.lang.Override
    protected boolean isCertMarkedAsRemoved(java.lang.String caFile) {
        return new java.io.File(mUserRemovedCaDir, caFile).exists();
    }
}

