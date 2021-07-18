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
 * An atomic formula that evaluates to true if the installer of the current install is specified in
 * the "allowed installer" field in the android manifest. Note that an empty "allowed installer" by
 * default means containing all possible installers.
 *
 * @unknown 
 */
public class InstallerAllowedByManifestFormula extends android.content.integrity.IntegrityFormula implements android.os.Parcelable {
    public static final java.lang.String INSTALLER_CERTIFICATE_NOT_EVALUATED = "";

    public InstallerAllowedByManifestFormula() {
    }

    private InstallerAllowedByManifestFormula(android.os.Parcel in) {
    }

    @android.annotation.NonNull
    public static final android.content.integrity.Creator<android.content.integrity.InstallerAllowedByManifestFormula> CREATOR = new android.content.integrity.Creator<android.content.integrity.InstallerAllowedByManifestFormula>() {
        @java.lang.Override
        public android.content.integrity.InstallerAllowedByManifestFormula createFromParcel(android.os.Parcel in) {
            return new android.content.integrity.InstallerAllowedByManifestFormula(in);
        }

        @java.lang.Override
        public android.content.integrity.InstallerAllowedByManifestFormula[] newArray(int size) {
            return new android.content.integrity.InstallerAllowedByManifestFormula[size];
        }
    };

    @java.lang.Override
    public int getTag() {
        return android.content.integrity.IntegrityFormula.INSTALLER_ALLOWED_BY_MANIFEST_FORMULA_TAG;
    }

    @java.lang.Override
    public boolean matches(android.content.integrity.AppInstallMetadata appInstallMetadata) {
        java.util.Map<java.lang.String, java.lang.String> allowedInstallersAndCertificates = appInstallMetadata.getAllowedInstallersAndCertificates();
        return allowedInstallersAndCertificates.isEmpty() || android.content.integrity.InstallerAllowedByManifestFormula.installerInAllowedInstallersFromManifest(appInstallMetadata, allowedInstallersAndCertificates);
    }

    @java.lang.Override
    public boolean isAppCertificateFormula() {
        return false;
    }

    @java.lang.Override
    public boolean isInstallerFormula() {
        return true;
    }

    private static boolean installerInAllowedInstallersFromManifest(android.content.integrity.AppInstallMetadata appInstallMetadata, java.util.Map<java.lang.String, java.lang.String> allowedInstallersAndCertificates) {
        java.lang.String installerPackage = appInstallMetadata.getInstallerName();
        if (!allowedInstallersAndCertificates.containsKey(installerPackage)) {
            return false;
        }
        // If certificate is not specified in the manifest, we do not check it.
        if (!allowedInstallersAndCertificates.get(installerPackage).equals(android.content.integrity.InstallerAllowedByManifestFormula.INSTALLER_CERTIFICATE_NOT_EVALUATED)) {
            return appInstallMetadata.getInstallerCertificates().contains(allowedInstallersAndCertificates.get(appInstallMetadata.getInstallerName()));
        }
        return true;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
    }
}

