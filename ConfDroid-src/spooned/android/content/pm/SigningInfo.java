/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.content.pm;


/**
 * Information pertaining to the signing certificates used to sign a package.
 */
public final class SigningInfo implements android.os.Parcelable {
    @android.annotation.NonNull
    private final android.content.pm.PackageParser.SigningDetails mSigningDetails;

    public SigningInfo() {
        mSigningDetails = android.content.pm.PackageParser.SigningDetails.UNKNOWN;
    }

    /**
     *
     *
     * @unknown only packagemanager should be populating this
     */
    public SigningInfo(android.content.pm.PackageParser.SigningDetails signingDetails) {
        mSigningDetails = new android.content.pm.PackageParser.SigningDetails(signingDetails);
    }

    public SigningInfo(android.content.pm.SigningInfo orig) {
        mSigningDetails = new android.content.pm.PackageParser.SigningDetails(orig.mSigningDetails);
    }

    private SigningInfo(android.os.Parcel source) {
        mSigningDetails = android.content.pm.PackageParser.SigningDetails.CREATOR.createFromParcel(source);
    }

    /**
     * Although relatively uncommon, packages may be signed by more than one signer, in which case
     * their identity is viewed as being the set of all signers, not just any one.
     */
    public boolean hasMultipleSigners() {
        return (mSigningDetails.signatures != null) && (mSigningDetails.signatures.length > 1);
    }

    /**
     * APK Signature Scheme v3 enables packages to provide a proof-of-rotation record that the
     * platform verifies, and uses, to allow the use of new signing certificates.  This is only
     * available to packages that are not signed by multiple signers.  In the event of a change to a
     * new signing certificate, the package's past signing certificates are presented as well.  Any
     * check of a package's signing certificate should also include a search through its entire
     * signing history, since it could change to a new signing certificate at any time.
     */
    public boolean hasPastSigningCertificates() {
        return (mSigningDetails.signatures != null) && (mSigningDetails.pastSigningCertificates != null);
    }

    /**
     * Returns the signing certificates this package has proven it is authorized to use. This
     * includes both the signing certificate associated with the signer of the package and the past
     * signing certificates it included as its proof of signing certificate rotation.  This method
     * is the preferred replacement for the {@code GET_SIGNATURES} flag used with {@link PackageManager#getPackageInfo(String, int)}.  When determining if a package is signed by a
     * desired certificate, the returned array should be checked to determine if it is one of the
     * entries.
     *
     * <note>
     *     This method returns null if the package is signed by multiple signing certificates, as
     *     opposed to being signed by one current signer and also providing the history of past
     *     signing certificates.  {@link #hasMultipleSigners()} may be used to determine if this
     *     package is signed by multiple signers.  Packages which are signed by multiple signers
     *     cannot change their signing certificates and their {@code Signature} array should be
     *     checked to make sure that every entry matches the looked-for signing certificates.
     * </note>
     */
    public android.content.pm.Signature[] getSigningCertificateHistory() {
        if (hasMultipleSigners()) {
            return null;
        } else
            if (!hasPastSigningCertificates()) {
                // this package is only signed by one signer with no history, return it
                return mSigningDetails.signatures;
            } else {
                // this package has provided proof of past signing certificates, include them
                return mSigningDetails.pastSigningCertificates;
            }

    }

    /**
     * Returns the signing certificates used to sign the APK contents of this application.  Not
     * including any past signing certificates the package proved it is authorized to use.
     * <note>
     *     This method should not be used unless {@link #hasMultipleSigners()} returns true,
     *     indicating that {@link #getSigningCertificateHistory()} cannot be used, otherwise {@link #getSigningCertificateHistory()} should be preferred.
     * </note>
     */
    public android.content.pm.Signature[] getApkContentsSigners() {
        return mSigningDetails.signatures;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        mSigningDetails.writeToParcel(dest, parcelableFlags);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.pm.SigningInfo> CREATOR = new android.os.Parcelable.Creator<android.content.pm.SigningInfo>() {
        @java.lang.Override
        public android.content.pm.SigningInfo createFromParcel(android.os.Parcel source) {
            return new android.content.pm.SigningInfo(source);
        }

        @java.lang.Override
        public android.content.pm.SigningInfo[] newArray(int size) {
            return new android.content.pm.SigningInfo[size];
        }
    };
}

