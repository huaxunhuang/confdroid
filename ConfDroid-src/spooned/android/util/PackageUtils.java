/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.util;


/**
 * Helper functions applicable to packages.
 *
 * @unknown 
 */
public final class PackageUtils {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private PackageUtils() {
        /* hide constructor */
    }

    /**
     * Computes the SHA256 digest of the signing cert for a package.
     *
     * @param packageManager
     * 		The package manager.
     * @param packageName
     * 		The package for which to generate the digest.
     * @param userId
     * 		The user for which to generate the digest.
     * @return The digest or null if the package does not exist for this user.
     */
    @android.annotation.Nullable
    public static java.lang.String computePackageCertSha256Digest(@android.annotation.NonNull
    android.content.pm.PackageManager packageManager, @android.annotation.NonNull
    java.lang.String packageName, int userId) {
        final android.content.pm.PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfoAsUser(packageName, android.content.pm.PackageManager.GET_SIGNATURES, userId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
        return android.util.PackageUtils.computeCertSha256Digest(packageInfo.signatures[0]);
    }

    /**
     * Computes the SHA256 digest of a cert.
     *
     * @param signature
     * 		The signature.
     * @return The digest or null if an error occurs.
     */
    @android.annotation.Nullable
    public static java.lang.String computeCertSha256Digest(@android.annotation.NonNull
    android.content.pm.Signature signature) {
        return android.util.PackageUtils.computeSha256Digest(signature.toByteArray());
    }

    /**
     * Computes the SHA256 digest of some data.
     *
     * @param data
     * 		The data.
     * @return The digest or null if an error occurs.
     */
    @android.annotation.Nullable
    public static java.lang.String computeSha256Digest(@android.annotation.NonNull
    byte[] data) {
        java.security.MessageDigest messageDigest;
        try {
            messageDigest = java.security.MessageDigest.getInstance("SHA256");
        } catch (java.security.NoSuchAlgorithmException e) {
            /* can't happen */
            return null;
        }
        messageDigest.update(data);
        final byte[] digest = messageDigest.digest();
        final int digestLength = digest.length;
        final int charCount = 2 * digestLength;
        final char[] chars = new char[charCount];
        for (int i = 0; i < digestLength; i++) {
            final int byteHex = digest[i] & 0xff;
            chars[i * 2] = android.util.PackageUtils.HEX_ARRAY[byteHex >>> 4];
            chars[(i * 2) + 1] = android.util.PackageUtils.HEX_ARRAY[byteHex & 0xf];
        }
        return new java.lang.String(chars);
    }
}

