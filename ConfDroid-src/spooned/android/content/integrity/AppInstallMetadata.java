/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.content.integrity;


/**
 * The app install metadata.
 *
 * <p>The integrity component retrieves metadata for app installs from package manager, passing it
 * to the rule evaluation engine to evaluate the metadata against the rules.
 *
 * <p>Instances of this class are immutable.
 *
 * @unknown 
 */
public final class AppInstallMetadata {
    private final java.lang.String mPackageName;

    // Raw string encoding for the SHA-256 hash of the certificate of the app.
    private final java.util.List<java.lang.String> mAppCertificates;

    private final java.lang.String mInstallerName;

    // Raw string encoding for the SHA-256 hash of the certificate of the installer.
    private final java.util.List<java.lang.String> mInstallerCertificates;

    private final long mVersionCode;

    private final boolean mIsPreInstalled;

    private final boolean mIsStampPresent;

    private final boolean mIsStampVerified;

    private final boolean mIsStampTrusted;

    // Raw string encoding for the SHA-256 hash of the certificate of the stamp.
    private final java.lang.String mStampCertificateHash;

    private final java.util.Map<java.lang.String, java.lang.String> mAllowedInstallersAndCertificates;

    private AppInstallMetadata(android.content.integrity.AppInstallMetadata.Builder builder) {
        this.mPackageName = builder.mPackageName;
        this.mAppCertificates = builder.mAppCertificates;
        this.mInstallerName = builder.mInstallerName;
        this.mInstallerCertificates = builder.mInstallerCertificates;
        this.mVersionCode = builder.mVersionCode;
        this.mIsPreInstalled = builder.mIsPreInstalled;
        this.mIsStampPresent = builder.mIsStampPresent;
        this.mIsStampVerified = builder.mIsStampVerified;
        this.mIsStampTrusted = builder.mIsStampTrusted;
        this.mStampCertificateHash = builder.mStampCertificateHash;
        this.mAllowedInstallersAndCertificates = builder.mAllowedInstallersAndCertificates;
    }

    @android.annotation.NonNull
    public java.lang.String getPackageName() {
        return mPackageName;
    }

    @android.annotation.NonNull
    public java.util.List<java.lang.String> getAppCertificates() {
        return mAppCertificates;
    }

    @android.annotation.NonNull
    public java.lang.String getInstallerName() {
        return mInstallerName;
    }

    @android.annotation.NonNull
    public java.util.List<java.lang.String> getInstallerCertificates() {
        return mInstallerCertificates;
    }

    /**
     *
     *
     * @see AppInstallMetadata.Builder#setVersionCode(long)
     */
    public long getVersionCode() {
        return mVersionCode;
    }

    /**
     *
     *
     * @see AppInstallMetadata.Builder#setIsPreInstalled(boolean)
     */
    public boolean isPreInstalled() {
        return mIsPreInstalled;
    }

    /**
     *
     *
     * @see AppInstallMetadata.Builder#setIsStampPresent(boolean)
     */
    public boolean isStampPresent() {
        return mIsStampPresent;
    }

    /**
     *
     *
     * @see AppInstallMetadata.Builder#setIsStampVerified(boolean)
     */
    public boolean isStampVerified() {
        return mIsStampVerified;
    }

    /**
     *
     *
     * @see AppInstallMetadata.Builder#setIsStampTrusted(boolean)
     */
    public boolean isStampTrusted() {
        return mIsStampTrusted;
    }

    /**
     *
     *
     * @see AppInstallMetadata.Builder#setStampCertificateHash(String)
     */
    public java.lang.String getStampCertificateHash() {
        return mStampCertificateHash;
    }

    /**
     * Get the allowed installers and their corresponding cert.
     */
    public java.util.Map<java.lang.String, java.lang.String> getAllowedInstallersAndCertificates() {
        return mAllowedInstallersAndCertificates;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("AppInstallMetadata { PackageName = %s, AppCerts = %s, InstallerName = %s," + (" InstallerCerts = %s, VersionCode = %d, PreInstalled = %b, StampPresent =" + " %b, StampVerified = %b, StampTrusted = %b, StampCert = %s }"), mPackageName, mAppCertificates, mInstallerName == null ? "null" : mInstallerName, mInstallerCertificates == null ? "null" : mInstallerCertificates, mVersionCode, mIsPreInstalled, mIsStampPresent, mIsStampVerified, mIsStampTrusted, mStampCertificateHash == null ? "null" : mStampCertificateHash);
    }

    /**
     * Builder class for constructing {@link AppInstallMetadata} objects.
     */
    public static final class Builder {
        private java.lang.String mPackageName;

        private java.util.List<java.lang.String> mAppCertificates;

        private java.lang.String mInstallerName;

        private java.util.List<java.lang.String> mInstallerCertificates;

        private long mVersionCode;

        private boolean mIsPreInstalled;

        private boolean mIsStampPresent;

        private boolean mIsStampVerified;

        private boolean mIsStampTrusted;

        private java.lang.String mStampCertificateHash;

        private java.util.Map<java.lang.String, java.lang.String> mAllowedInstallersAndCertificates;

        public Builder() {
            mAllowedInstallersAndCertificates = new java.util.HashMap<>();
        }

        /**
         * Add allowed installers and cert.
         *
         * @see AppInstallMetadata#getAllowedInstallersAndCertificates()
         */
        @android.annotation.NonNull
        public android.content.integrity.AppInstallMetadata.Builder setAllowedInstallersAndCert(@android.annotation.NonNull
        java.util.Map<java.lang.String, java.lang.String> allowedInstallersAndCertificates) {
            this.mAllowedInstallersAndCertificates = allowedInstallersAndCertificates;
            return this;
        }

        /**
         * Set package name of the app to be installed.
         *
         * @see AppInstallMetadata#getPackageName()
         */
        @android.annotation.NonNull
        public android.content.integrity.AppInstallMetadata.Builder setPackageName(@android.annotation.NonNull
        java.lang.String packageName) {
            this.mPackageName = java.util.Objects.requireNonNull(packageName);
            return this;
        }

        /**
         * Set certificate of the app to be installed.
         *
         * <p>It is represented as the raw string encoding for the SHA-256 hash of the certificate
         * of the app.
         *
         * @see AppInstallMetadata#getAppCertificates()
         */
        @android.annotation.NonNull
        public android.content.integrity.AppInstallMetadata.Builder setAppCertificates(@android.annotation.NonNull
        java.util.List<java.lang.String> appCertificates) {
            this.mAppCertificates = java.util.Objects.requireNonNull(appCertificates);
            return this;
        }

        /**
         * Set name of the installer installing the app.
         *
         * @see AppInstallMetadata#getInstallerName()
         */
        @android.annotation.NonNull
        public android.content.integrity.AppInstallMetadata.Builder setInstallerName(@android.annotation.NonNull
        java.lang.String installerName) {
            this.mInstallerName = java.util.Objects.requireNonNull(installerName);
            return this;
        }

        /**
         * Set certificate of the installer installing the app.
         *
         * <p>It is represented as the raw string encoding for the SHA-256 hash of the certificate
         * of the installer.
         *
         * @see AppInstallMetadata#getInstallerCertificates()
         */
        @android.annotation.NonNull
        public android.content.integrity.AppInstallMetadata.Builder setInstallerCertificates(@android.annotation.NonNull
        java.util.List<java.lang.String> installerCertificates) {
            this.mInstallerCertificates = java.util.Objects.requireNonNull(installerCertificates);
            return this;
        }

        /**
         * Set version code of the app to be installed.
         *
         * @see AppInstallMetadata#getVersionCode()
         */
        @android.annotation.NonNull
        public android.content.integrity.AppInstallMetadata.Builder setVersionCode(long versionCode) {
            this.mVersionCode = versionCode;
            return this;
        }

        /**
         * Set whether the app is pre-installed on the device or not.
         *
         * @see AppInstallMetadata#isPreInstalled()
         */
        @android.annotation.NonNull
        public android.content.integrity.AppInstallMetadata.Builder setIsPreInstalled(boolean isPreInstalled) {
            this.mIsPreInstalled = isPreInstalled;
            return this;
        }

        /**
         * Set whether the stamp embedded in the APK is present or not.
         *
         * @see AppInstallMetadata#isStampPresent()
         */
        @android.annotation.NonNull
        public android.content.integrity.AppInstallMetadata.Builder setIsStampPresent(boolean isStampPresent) {
            this.mIsStampPresent = isStampPresent;
            return this;
        }

        /**
         * Set whether the stamp embedded in the APK is verified or not.
         *
         * @see AppInstallMetadata#isStampVerified()
         */
        @android.annotation.NonNull
        public android.content.integrity.AppInstallMetadata.Builder setIsStampVerified(boolean isStampVerified) {
            this.mIsStampVerified = isStampVerified;
            return this;
        }

        /**
         * Set whether the stamp embedded in the APK is trusted or not.
         *
         * @see AppInstallMetadata#isStampTrusted()
         */
        @android.annotation.NonNull
        public android.content.integrity.AppInstallMetadata.Builder setIsStampTrusted(boolean isStampTrusted) {
            this.mIsStampTrusted = isStampTrusted;
            return this;
        }

        /**
         * Set certificate hash of the stamp embedded in the APK.
         *
         * <p>It is represented as the raw string encoding for the SHA-256 hash of the certificate
         * of the stamp.
         *
         * @see AppInstallMetadata#getStampCertificateHash()
         */
        @android.annotation.NonNull
        public android.content.integrity.AppInstallMetadata.Builder setStampCertificateHash(@android.annotation.NonNull
        java.lang.String stampCertificateHash) {
            this.mStampCertificateHash = java.util.Objects.requireNonNull(stampCertificateHash);
            return this;
        }

        /**
         * Build {@link AppInstallMetadata}.
         *
         * @throws IllegalArgumentException
         * 		if package name or app certificate is null
         */
        @android.annotation.NonNull
        public android.content.integrity.AppInstallMetadata build() {
            java.util.Objects.requireNonNull(mPackageName);
            java.util.Objects.requireNonNull(mAppCertificates);
            return new android.content.integrity.AppInstallMetadata(this);
        }
    }
}

