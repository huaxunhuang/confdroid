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
package android.security.keystore;


/**
 * Information about a key from the <a href="{@docRoot }training/articles/keystore.html">Android
 * Keystore system</a>. This class describes whether the key material is available in
 * plaintext outside of secure hardware, whether user authentication is required for using the key
 * and whether this requirement is enforced by secure hardware, the key's origin, what uses the key
 * is authorized for (e.g., only in {@code GCM} mode, or signing only), whether the key should be
 * encrypted at rest, the key's and validity start and end dates.
 *
 * <p>Instances of this class are immutable.
 *
 * <p><h3>Example: Symmetric Key</h3>
 * The following example illustrates how to obtain a {@code KeyInfo} describing the provided Android
 * Keystore {@link SecretKey}.
 * <pre>{@code SecretKey key = ...; // Android Keystore key
 *
 * SecretKeyFactory factory = SecretKeyFactory.getInstance(key.getAlgorithm(), "AndroidKeyStore");
 * KeyInfo keyInfo;
 * try {
 *     keyInfo = (KeyInfo) factory.getKeySpec(key, KeyInfo.class);} catch (InvalidKeySpecException e) {
 *     // Not an Android KeyStore key.
 * }}</pre>
 *
 * <p><h3>Example: Private Key</h3>
 * The following example illustrates how to obtain a {@code KeyInfo} describing the provided
 * Android KeyStore {@link PrivateKey}.
 * <pre>{@code PrivateKey key = ...; // Android KeyStore key
 *
 * KeyFactory factory = KeyFactory.getInstance(key.getAlgorithm(), "AndroidKeyStore");
 * KeyInfo keyInfo;
 * try {
 *     keyInfo = factory.getKeySpec(key, KeyInfo.class);} catch (InvalidKeySpecException e) {
 *     // Not an Android KeyStore key.
 * }}</pre>
 */
public class KeyInfo implements java.security.spec.KeySpec {
    private final java.lang.String mKeystoreAlias;

    private final int mKeySize;

    private final boolean mInsideSecureHardware;

    @android.security.keystore.KeyProperties.OriginEnum
    private final int mOrigin;

    private final java.util.Date mKeyValidityStart;

    private final java.util.Date mKeyValidityForOriginationEnd;

    private final java.util.Date mKeyValidityForConsumptionEnd;

    @android.security.keystore.KeyProperties.PurposeEnum
    private final int mPurposes;

    @android.security.keystore.KeyProperties.EncryptionPaddingEnum
    private final java.lang.String[] mEncryptionPaddings;

    @android.security.keystore.KeyProperties.SignaturePaddingEnum
    private final java.lang.String[] mSignaturePaddings;

    @android.security.keystore.KeyProperties.DigestEnum
    private final java.lang.String[] mDigests;

    @android.security.keystore.KeyProperties.BlockModeEnum
    private final java.lang.String[] mBlockModes;

    private final boolean mUserAuthenticationRequired;

    private final int mUserAuthenticationValidityDurationSeconds;

    private final boolean mUserAuthenticationRequirementEnforcedBySecureHardware;

    private final boolean mUserAuthenticationValidWhileOnBody;

    private final boolean mInvalidatedByBiometricEnrollment;

    /**
     *
     *
     * @unknown 
     */
    public KeyInfo(java.lang.String keystoreKeyAlias, boolean insideSecureHardware, @android.security.keystore.KeyProperties.OriginEnum
    int origin, int keySize, java.util.Date keyValidityStart, java.util.Date keyValidityForOriginationEnd, java.util.Date keyValidityForConsumptionEnd, @android.security.keystore.KeyProperties.PurposeEnum
    int purposes, @android.security.keystore.KeyProperties.EncryptionPaddingEnum
    java.lang.String[] encryptionPaddings, @android.security.keystore.KeyProperties.SignaturePaddingEnum
    java.lang.String[] signaturePaddings, @android.security.keystore.KeyProperties.DigestEnum
    java.lang.String[] digests, @android.security.keystore.KeyProperties.BlockModeEnum
    java.lang.String[] blockModes, boolean userAuthenticationRequired, int userAuthenticationValidityDurationSeconds, boolean userAuthenticationRequirementEnforcedBySecureHardware, boolean userAuthenticationValidWhileOnBody, boolean invalidatedByBiometricEnrollment) {
        mKeystoreAlias = keystoreKeyAlias;
        mInsideSecureHardware = insideSecureHardware;
        mOrigin = origin;
        mKeySize = keySize;
        mKeyValidityStart = android.security.keystore.Utils.cloneIfNotNull(keyValidityStart);
        mKeyValidityForOriginationEnd = android.security.keystore.Utils.cloneIfNotNull(keyValidityForOriginationEnd);
        mKeyValidityForConsumptionEnd = android.security.keystore.Utils.cloneIfNotNull(keyValidityForConsumptionEnd);
        mPurposes = purposes;
        mEncryptionPaddings = android.security.keystore.ArrayUtils.cloneIfNotEmpty(android.security.keystore.ArrayUtils.nullToEmpty(encryptionPaddings));
        mSignaturePaddings = android.security.keystore.ArrayUtils.cloneIfNotEmpty(android.security.keystore.ArrayUtils.nullToEmpty(signaturePaddings));
        mDigests = android.security.keystore.ArrayUtils.cloneIfNotEmpty(android.security.keystore.ArrayUtils.nullToEmpty(digests));
        mBlockModes = android.security.keystore.ArrayUtils.cloneIfNotEmpty(android.security.keystore.ArrayUtils.nullToEmpty(blockModes));
        mUserAuthenticationRequired = userAuthenticationRequired;
        mUserAuthenticationValidityDurationSeconds = userAuthenticationValidityDurationSeconds;
        mUserAuthenticationRequirementEnforcedBySecureHardware = userAuthenticationRequirementEnforcedBySecureHardware;
        mUserAuthenticationValidWhileOnBody = userAuthenticationValidWhileOnBody;
        mInvalidatedByBiometricEnrollment = invalidatedByBiometricEnrollment;
    }

    /**
     * Gets the entry alias under which the key is stored in the {@code AndroidKeyStore}.
     */
    public java.lang.String getKeystoreAlias() {
        return mKeystoreAlias;
    }

    /**
     * Returns {@code true} if the key resides inside secure hardware (e.g., Trusted Execution
     * Environment (TEE) or Secure Element (SE)). Key material of such keys is available in
     * plaintext only inside the secure hardware and is not exposed outside of it.
     */
    public boolean isInsideSecureHardware() {
        return mInsideSecureHardware;
    }

    /**
     * Gets the origin of the key. See {@link KeyProperties}.{@code ORIGIN} constants.
     */
    @android.security.keystore.KeyProperties.OriginEnum
    public int getOrigin() {
        return mOrigin;
    }

    /**
     * Gets the size of the key in bits.
     */
    public int getKeySize() {
        return mKeySize;
    }

    /**
     * Gets the time instant before which the key is not yet valid.
     *
     * @return instant or {@code null} if not restricted.
     */
    @android.annotation.Nullable
    public java.util.Date getKeyValidityStart() {
        return android.security.keystore.Utils.cloneIfNotNull(mKeyValidityStart);
    }

    /**
     * Gets the time instant after which the key is no long valid for decryption and verification.
     *
     * @return instant or {@code null} if not restricted.
     */
    @android.annotation.Nullable
    public java.util.Date getKeyValidityForConsumptionEnd() {
        return android.security.keystore.Utils.cloneIfNotNull(mKeyValidityForConsumptionEnd);
    }

    /**
     * Gets the time instant after which the key is no long valid for encryption and signing.
     *
     * @return instant or {@code null} if not restricted.
     */
    @android.annotation.Nullable
    public java.util.Date getKeyValidityForOriginationEnd() {
        return android.security.keystore.Utils.cloneIfNotNull(mKeyValidityForOriginationEnd);
    }

    /**
     * Gets the set of purposes (e.g., encrypt, decrypt, sign) for which the key can be used.
     * Attempts to use the key for any other purpose will be rejected.
     *
     * <p>See {@link KeyProperties}.{@code PURPOSE} flags.
     */
    @android.security.keystore.KeyProperties.PurposeEnum
    public int getPurposes() {
        return mPurposes;
    }

    /**
     * Gets the set of block modes (e.g., {@code GCM}, {@code CBC}) with which the key can be used
     * when encrypting/decrypting. Attempts to use the key with any other block modes will be
     * rejected.
     *
     * <p>See {@link KeyProperties}.{@code BLOCK_MODE} constants.
     */
    @android.annotation.NonNull
    @android.security.keystore.KeyProperties.BlockModeEnum
    public java.lang.String[] getBlockModes() {
        return android.security.keystore.ArrayUtils.cloneIfNotEmpty(mBlockModes);
    }

    /**
     * Gets the set of padding schemes (e.g., {@code PKCS7Padding}, {@code PKCS1Padding},
     * {@code NoPadding}) with which the key can be used when encrypting/decrypting. Attempts to use
     * the key with any other padding scheme will be rejected.
     *
     * <p>See {@link KeyProperties}.{@code ENCRYPTION_PADDING} constants.
     */
    @android.annotation.NonNull
    @android.security.keystore.KeyProperties.EncryptionPaddingEnum
    public java.lang.String[] getEncryptionPaddings() {
        return android.security.keystore.ArrayUtils.cloneIfNotEmpty(mEncryptionPaddings);
    }

    /**
     * Gets the set of padding schemes (e.g., {@code PSS}, {@code PKCS#1}) with which the key
     * can be used when signing/verifying. Attempts to use the key with any other padding scheme
     * will be rejected.
     *
     * <p>See {@link KeyProperties}.{@code SIGNATURE_PADDING} constants.
     */
    @android.annotation.NonNull
    @android.security.keystore.KeyProperties.SignaturePaddingEnum
    public java.lang.String[] getSignaturePaddings() {
        return android.security.keystore.ArrayUtils.cloneIfNotEmpty(mSignaturePaddings);
    }

    /**
     * Gets the set of digest algorithms (e.g., {@code SHA-256}, {@code SHA-384}) with which the key
     * can be used.
     *
     * <p>See {@link KeyProperties}.{@code DIGEST} constants.
     */
    @android.annotation.NonNull
    @android.security.keystore.KeyProperties.DigestEnum
    public java.lang.String[] getDigests() {
        return android.security.keystore.ArrayUtils.cloneIfNotEmpty(mDigests);
    }

    /**
     * Returns {@code true} if the key is authorized to be used only if the user has been
     * authenticated.
     *
     * <p>This authorization applies only to secret key and private key operations. Public key
     * operations are not restricted.
     *
     * @see #getUserAuthenticationValidityDurationSeconds()
     * @see KeyGenParameterSpec.Builder#setUserAuthenticationRequired(boolean)
     * @see KeyProtection.Builder#setUserAuthenticationRequired(boolean)
     */
    public boolean isUserAuthenticationRequired() {
        return mUserAuthenticationRequired;
    }

    /**
     * Gets the duration of time (seconds) for which this key is authorized to be used after the
     * user is successfully authenticated. This has effect only if user authentication is required
     * (see {@link #isUserAuthenticationRequired()}).
     *
     * <p>This authorization applies only to secret key and private key operations. Public key
     * operations are not restricted.
     *
     * @return duration in seconds or {@code -1} if authentication is required for every use of the
    key.
     * @see #isUserAuthenticationRequired()
     */
    public int getUserAuthenticationValidityDurationSeconds() {
        return mUserAuthenticationValidityDurationSeconds;
    }

    /**
     * Returns {@code true} if the requirement that this key can only be used if the user has been
     * authenticated is enforced by secure hardware (e.g., Trusted Execution Environment (TEE) or
     * Secure Element (SE)).
     *
     * @see #isUserAuthenticationRequired()
     */
    public boolean isUserAuthenticationRequirementEnforcedBySecureHardware() {
        return mUserAuthenticationRequirementEnforcedBySecureHardware;
    }

    /**
     * Returns {@code true} if this key will become unusable when the device is removed from the
     * user's body.  This is possible only for keys with a specified validity duration, and only on
     * devices with an on-body sensor.  Always returns {@code false} on devices that lack an on-body
     * sensor.
     */
    public boolean isUserAuthenticationValidWhileOnBody() {
        return mUserAuthenticationValidWhileOnBody;
    }

    /**
     * Returns {@code true} if the key will be invalidated by enrollment of a new fingerprint or
     * removal of all fingerprints.
     */
    public boolean isInvalidatedByBiometricEnrollment() {
        return mInvalidatedByBiometricEnrollment;
    }
}

