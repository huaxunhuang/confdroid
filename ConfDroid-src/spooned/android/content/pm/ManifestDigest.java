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
 * Represents the manifest digest for a package. This is suitable for comparison
 * of two packages to know whether the manifests are identical.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class ManifestDigest implements android.os.Parcelable {
    private static final java.lang.String TAG = "ManifestDigest";

    /**
     * The digest of the manifest in our preferred order.
     */
    private final byte[] mDigest;

    /**
     * What we print out first when toString() is called.
     */
    private static final java.lang.String TO_STRING_PREFIX = "ManifestDigest {mDigest=";

    /**
     * Digest algorithm to use.
     */
    private static final java.lang.String DIGEST_ALGORITHM = "SHA-256";

    ManifestDigest(byte[] digest) {
        mDigest = digest;
    }

    private ManifestDigest(android.os.Parcel source) {
        mDigest = source.createByteArray();
    }

    static android.content.pm.ManifestDigest fromInputStream(java.io.InputStream fileIs) {
        if (fileIs == null) {
            return null;
        }
        final java.security.MessageDigest md;
        try {
            md = java.security.MessageDigest.getInstance(android.content.pm.ManifestDigest.DIGEST_ALGORITHM);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new java.lang.RuntimeException(android.content.pm.ManifestDigest.DIGEST_ALGORITHM + " must be available", e);
        }
        final java.security.DigestInputStream dis = new java.security.DigestInputStream(new java.io.BufferedInputStream(fileIs), md);
        try {
            byte[] readBuffer = new byte[8192];
            while (dis.read(readBuffer, 0, readBuffer.length) != (-1)) {
                // not using
            } 
        } catch (java.io.IOException e) {
            android.util.Slog.w(android.content.pm.ManifestDigest.TAG, "Could not read manifest");
            return null;
        } finally {
            libcore.io.IoUtils.closeQuietly(dis);
        }
        final byte[] digest = md.digest();
        return new android.content.pm.ManifestDigest(digest);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (!(o instanceof android.content.pm.ManifestDigest)) {
            return false;
        }
        final android.content.pm.ManifestDigest other = ((android.content.pm.ManifestDigest) (o));
        return (this == other) || java.util.Arrays.equals(mDigest, other.mDigest);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Arrays.hashCode(mDigest);
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.StringBuilder sb = new java.lang.StringBuilder((android.content.pm.ManifestDigest.TO_STRING_PREFIX.length() + (mDigest.length * 3)) + 1);
        sb.append(android.content.pm.ManifestDigest.TO_STRING_PREFIX);
        final int N = mDigest.length;
        for (int i = 0; i < N; i++) {
            final byte b = mDigest[i];
            android.content.pm.IntegralToString.appendByteAsHex(sb, b, false);
            sb.append(',');
        }
        sb.append('}');
        return sb.toString();
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeByteArray(mDigest);
    }

    public static final android.os.Parcelable.Creator<android.content.pm.ManifestDigest> CREATOR = new android.os.Parcelable.Creator<android.content.pm.ManifestDigest>() {
        public android.content.pm.ManifestDigest createFromParcel(android.os.Parcel source) {
            return new android.content.pm.ManifestDigest(source);
        }

        public android.content.pm.ManifestDigest[] newArray(int size) {
            return new android.content.pm.ManifestDigest[size];
        }
    };
}

