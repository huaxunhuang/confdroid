/**
 * Copyright (C) 2011 The Android Open Source Project
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
 * Contains information about a package verifier as used by
 * {@code PackageManagerService} during package verification.
 *
 * @unknown 
 */
public class VerifierInfo implements android.os.Parcelable {
    /**
     * Package name of the verifier.
     */
    public final java.lang.String packageName;

    /**
     * Signatures used to sign the package verifier's package.
     */
    public final java.security.PublicKey publicKey;

    /**
     * Creates an object that represents a verifier info object.
     *
     * @param packageName
     * 		the package name in Java-style. Must not be {@code null} or empty.
     * @param publicKey
     * 		the public key for the signer encoded in Base64. Must
     * 		not be {@code null} or empty.
     * @throws IllegalArgumentException
     * 		if either argument is null or empty.
     */
    @android.annotation.UnsupportedAppUsage
    public VerifierInfo(java.lang.String packageName, java.security.PublicKey publicKey) {
        if ((packageName == null) || (packageName.length() == 0)) {
            throw new java.lang.IllegalArgumentException("packageName must not be null or empty");
        } else
            if (publicKey == null) {
                throw new java.lang.IllegalArgumentException("publicKey must not be null");
            }

        this.packageName = packageName;
        this.publicKey = publicKey;
    }

    private VerifierInfo(android.os.Parcel source) {
        packageName = source.readString();
        publicKey = ((java.security.PublicKey) (source.readSerializable()));
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(packageName);
        dest.writeSerializable(publicKey);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.pm.VerifierInfo> CREATOR = new android.os.Parcelable.Creator<android.content.pm.VerifierInfo>() {
        public android.content.pm.VerifierInfo createFromParcel(android.os.Parcel source) {
            return new android.content.pm.VerifierInfo(source);
        }

        public android.content.pm.VerifierInfo[] newArray(int size) {
            return new android.content.pm.VerifierInfo[size];
        }
    };
}

