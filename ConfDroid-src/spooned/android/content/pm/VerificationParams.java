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
package android.content.pm;


/**
 * Represents verification parameters used to verify packages to be installed.
 *
 * @deprecated callers should migrate to {@link PackageInstaller}.
 * @unknown 
 */
@java.lang.Deprecated
public class VerificationParams implements android.os.Parcelable {
    /**
     * A constant used to indicate that a uid value is not present.
     */
    public static final int NO_UID = -1;

    /**
     * What we print out first when toString() is called.
     */
    private static final java.lang.String TO_STRING_PREFIX = "VerificationParams{";

    /**
     * The location of the supplementary verification file.
     */
    private final android.net.Uri mVerificationURI;

    /**
     * URI referencing where the package was downloaded from.
     */
    private final android.net.Uri mOriginatingURI;

    /**
     * HTTP referrer URI associated with the originatingURI.
     */
    private final android.net.Uri mReferrer;

    /**
     * UID of the application that the install request originated from.
     */
    private final int mOriginatingUid;

    /**
     * UID of application requesting the install
     */
    private int mInstallerUid;

    /**
     * Creates verification specifications for installing with application verification.
     *
     * @param verificationURI
     * 		The location of the supplementary verification
     * 		file. This can be a 'file:' or a 'content:' URI. May be {@code null}.
     * @param originatingURI
     * 		URI referencing where the package was downloaded
     * 		from. May be {@code null}.
     * @param referrer
     * 		HTTP referrer URI associated with the originatingURI.
     * 		May be {@code null}.
     * @param originatingUid
     * 		UID of the application that the install request originated
     * 		from, or NO_UID if not present
     */
    public VerificationParams(android.net.Uri verificationURI, android.net.Uri originatingURI, android.net.Uri referrer, int originatingUid) {
        mVerificationURI = verificationURI;
        mOriginatingURI = originatingURI;
        mReferrer = referrer;
        mOriginatingUid = originatingUid;
        mInstallerUid = android.content.pm.VerificationParams.NO_UID;
    }

    public android.net.Uri getVerificationURI() {
        return mVerificationURI;
    }

    public android.net.Uri getOriginatingURI() {
        return mOriginatingURI;
    }

    public android.net.Uri getReferrer() {
        return mReferrer;
    }

    /**
     * return NO_UID if not available
     */
    public int getOriginatingUid() {
        return mOriginatingUid;
    }

    /**
     *
     *
     * @return NO_UID when not set
     */
    public int getInstallerUid() {
        return mInstallerUid;
    }

    public void setInstallerUid(int uid) {
        mInstallerUid = uid;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof android.content.pm.VerificationParams)) {
            return false;
        }
        final android.content.pm.VerificationParams other = ((android.content.pm.VerificationParams) (o));
        if (mVerificationURI == null) {
            if (other.mVerificationURI != null) {
                return false;
            }
        } else
            if (!mVerificationURI.equals(other.mVerificationURI)) {
                return false;
            }

        if (mOriginatingURI == null) {
            if (other.mOriginatingURI != null) {
                return false;
            }
        } else
            if (!mOriginatingURI.equals(other.mOriginatingURI)) {
                return false;
            }

        if (mReferrer == null) {
            if (other.mReferrer != null) {
                return false;
            }
        } else
            if (!mReferrer.equals(other.mReferrer)) {
                return false;
            }

        if (mOriginatingUid != other.mOriginatingUid) {
            return false;
        }
        if (mInstallerUid != other.mInstallerUid) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int hash = 3;
        hash += 5 * (mVerificationURI == null ? 1 : mVerificationURI.hashCode());
        hash += 7 * (mOriginatingURI == null ? 1 : mOriginatingURI.hashCode());
        hash += 11 * (mReferrer == null ? 1 : mReferrer.hashCode());
        hash += 13 * mOriginatingUid;
        hash += 17 * mInstallerUid;
        return hash;
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.StringBuilder sb = new java.lang.StringBuilder(android.content.pm.VerificationParams.TO_STRING_PREFIX);
        sb.append("mVerificationURI=");
        sb.append(mVerificationURI.toString());
        sb.append(",mOriginatingURI=");
        sb.append(mOriginatingURI.toString());
        sb.append(",mReferrer=");
        sb.append(mReferrer.toString());
        sb.append(",mOriginatingUid=");
        sb.append(mOriginatingUid);
        sb.append(",mInstallerUid=");
        sb.append(mInstallerUid);
        sb.append('}');
        return sb.toString();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeParcelable(mVerificationURI, 0);
        dest.writeParcelable(mOriginatingURI, 0);
        dest.writeParcelable(mReferrer, 0);
        dest.writeInt(mOriginatingUid);
        dest.writeInt(mInstallerUid);
    }

    private VerificationParams(android.os.Parcel source) {
        mVerificationURI = source.readParcelable(android.net.Uri.class.getClassLoader());
        mOriginatingURI = source.readParcelable(android.net.Uri.class.getClassLoader());
        mReferrer = source.readParcelable(android.net.Uri.class.getClassLoader());
        mOriginatingUid = source.readInt();
        mInstallerUid = source.readInt();
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.pm.VerificationParams> CREATOR = new android.os.Parcelable.Creator<android.content.pm.VerificationParams>() {
        public android.content.pm.VerificationParams createFromParcel(android.os.Parcel source) {
            return new android.content.pm.VerificationParams(source);
        }

        public android.content.pm.VerificationParams[] newArray(int size) {
            return new android.content.pm.VerificationParams[size];
        }
    };
}

