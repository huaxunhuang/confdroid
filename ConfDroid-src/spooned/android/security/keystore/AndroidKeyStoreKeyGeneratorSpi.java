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
 * {@link KeyGeneratorSpi} backed by Android KeyStore.
 *
 * @unknown 
 */
public abstract class AndroidKeyStoreKeyGeneratorSpi extends javax.crypto.KeyGeneratorSpi {
    public static class AES extends android.security.keystore.AndroidKeyStoreKeyGeneratorSpi {
        public AES() {
            super(android.security.keymaster.KeymasterDefs.KM_ALGORITHM_AES, 128);
        }

        @java.lang.Override
        protected void engineInit(java.security.spec.AlgorithmParameterSpec params, java.security.SecureRandom random) throws java.security.InvalidAlgorithmParameterException {
            super.engineInit(params, random);
            if (((mKeySizeBits != 128) && (mKeySizeBits != 192)) && (mKeySizeBits != 256)) {
                throw new java.security.InvalidAlgorithmParameterException(("Unsupported key size: " + mKeySizeBits) + ". Supported: 128, 192, 256.");
            }
        }
    }

    protected static abstract class HmacBase extends android.security.keystore.AndroidKeyStoreKeyGeneratorSpi {
        protected HmacBase(int keymasterDigest) {
            super(android.security.keymaster.KeymasterDefs.KM_ALGORITHM_HMAC, keymasterDigest, android.security.keystore.KeymasterUtils.getDigestOutputSizeBits(keymasterDigest));
        }
    }

    public static class HmacSHA1 extends android.security.keystore.AndroidKeyStoreKeyGeneratorSpi.HmacBase {
        public HmacSHA1() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA1);
        }
    }

    public static class HmacSHA224 extends android.security.keystore.AndroidKeyStoreKeyGeneratorSpi.HmacBase {
        public HmacSHA224() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_224);
        }
    }

    public static class HmacSHA256 extends android.security.keystore.AndroidKeyStoreKeyGeneratorSpi.HmacBase {
        public HmacSHA256() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_256);
        }
    }

    public static class HmacSHA384 extends android.security.keystore.AndroidKeyStoreKeyGeneratorSpi.HmacBase {
        public HmacSHA384() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_384);
        }
    }

    public static class HmacSHA512 extends android.security.keystore.AndroidKeyStoreKeyGeneratorSpi.HmacBase {
        public HmacSHA512() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_512);
        }
    }

    private final android.security.KeyStore mKeyStore = android.security.KeyStore.getInstance();

    private final int mKeymasterAlgorithm;

    private final int mKeymasterDigest;

    private final int mDefaultKeySizeBits;

    private android.security.keystore.KeyGenParameterSpec mSpec;

    private java.security.SecureRandom mRng;

    protected int mKeySizeBits;

    private int[] mKeymasterPurposes;

    private int[] mKeymasterBlockModes;

    private int[] mKeymasterPaddings;

    private int[] mKeymasterDigests;

    protected AndroidKeyStoreKeyGeneratorSpi(int keymasterAlgorithm, int defaultKeySizeBits) {
        this(keymasterAlgorithm, -1, defaultKeySizeBits);
    }

    protected AndroidKeyStoreKeyGeneratorSpi(int keymasterAlgorithm, int keymasterDigest, int defaultKeySizeBits) {
        mKeymasterAlgorithm = keymasterAlgorithm;
        mKeymasterDigest = keymasterDigest;
        mDefaultKeySizeBits = defaultKeySizeBits;
        if (mDefaultKeySizeBits <= 0) {
            throw new java.lang.IllegalArgumentException("Default key size must be positive");
        }
        if ((mKeymasterAlgorithm == android.security.keymaster.KeymasterDefs.KM_ALGORITHM_HMAC) && (mKeymasterDigest == (-1))) {
            throw new java.lang.IllegalArgumentException("Digest algorithm must be specified for HMAC key");
        }
    }

    @java.lang.Override
    protected void engineInit(java.security.SecureRandom random) {
        throw new java.lang.UnsupportedOperationException(("Cannot initialize without a " + android.security.keystore.KeyGenParameterSpec.class.getName()) + " parameter");
    }

    @java.lang.Override
    protected void engineInit(int keySize, java.security.SecureRandom random) {
        throw new java.lang.UnsupportedOperationException(("Cannot initialize without a " + android.security.keystore.KeyGenParameterSpec.class.getName()) + " parameter");
    }

    @java.lang.Override
    protected void engineInit(java.security.spec.AlgorithmParameterSpec params, java.security.SecureRandom random) throws java.security.InvalidAlgorithmParameterException {
        resetAll();
        boolean success = false;
        try {
            if ((params == null) || (!(params instanceof android.security.keystore.KeyGenParameterSpec))) {
                throw new java.security.InvalidAlgorithmParameterException(("Cannot initialize without a " + android.security.keystore.KeyGenParameterSpec.class.getName()) + " parameter");
            }
            android.security.keystore.KeyGenParameterSpec spec = ((android.security.keystore.KeyGenParameterSpec) (params));
            if (spec.getKeystoreAlias() == null) {
                throw new java.security.InvalidAlgorithmParameterException("KeyStore entry alias not provided");
            }
            mRng = random;
            mSpec = spec;
            mKeySizeBits = (spec.getKeySize() != (-1)) ? spec.getKeySize() : mDefaultKeySizeBits;
            if (mKeySizeBits <= 0) {
                throw new java.security.InvalidAlgorithmParameterException("Key size must be positive: " + mKeySizeBits);
            } else
                if ((mKeySizeBits % 8) != 0) {
                    throw new java.security.InvalidAlgorithmParameterException("Key size must be a multiple of 8: " + mKeySizeBits);
                }

            try {
                mKeymasterPurposes = android.security.keystore.KeyProperties.Purpose.allToKeymaster(spec.getPurposes());
                mKeymasterPaddings = android.security.keystore.KeyProperties.EncryptionPadding.allToKeymaster(spec.getEncryptionPaddings());
                if (spec.getSignaturePaddings().length > 0) {
                    throw new java.security.InvalidAlgorithmParameterException("Signature paddings not supported for symmetric key algorithms");
                }
                mKeymasterBlockModes = android.security.keystore.KeyProperties.BlockMode.allToKeymaster(spec.getBlockModes());
                if (((spec.getPurposes() & android.security.keystore.KeyProperties.PURPOSE_ENCRYPT) != 0) && spec.isRandomizedEncryptionRequired()) {
                    for (int keymasterBlockMode : mKeymasterBlockModes) {
                        if (!android.security.keystore.KeymasterUtils.isKeymasterBlockModeIndCpaCompatibleWithSymmetricCrypto(keymasterBlockMode)) {
                            throw new java.security.InvalidAlgorithmParameterException((((("Randomized encryption (IND-CPA) required but may be violated" + " by block mode: ") + android.security.keystore.KeyProperties.BlockMode.fromKeymaster(keymasterBlockMode)) + ". See ") + android.security.keystore.KeyGenParameterSpec.class.getName()) + " documentation.");
                        }
                    }
                }
                if (mKeymasterAlgorithm == android.security.keymaster.KeymasterDefs.KM_ALGORITHM_HMAC) {
                    // JCA HMAC key algorithm implies a digest (e.g., HmacSHA256 key algorithm
                    // implies SHA-256 digest). Because keymaster HMAC key is authorized only for
                    // one digest, we don't let algorithm parameter spec override the digest implied
                    // by the key. If the spec specifies digests at all, it must specify only one
                    // digest, the only implied by key algorithm.
                    mKeymasterDigests = new int[]{ mKeymasterDigest };
                    if (spec.isDigestsSpecified()) {
                        // Digest(s) explicitly specified in the spec. Check that the list
                        // consists of exactly one digest, the one implied by key algorithm.
                        int[] keymasterDigestsFromSpec = android.security.keystore.KeyProperties.Digest.allToKeymaster(spec.getDigests());
                        if ((keymasterDigestsFromSpec.length != 1) || (keymasterDigestsFromSpec[0] != mKeymasterDigest)) {
                            throw new java.security.InvalidAlgorithmParameterException(((("Unsupported digests specification: " + java.util.Arrays.asList(spec.getDigests())) + ". Only ") + android.security.keystore.KeyProperties.Digest.fromKeymaster(mKeymasterDigest)) + " supported for this HMAC key algorithm");
                        }
                    }
                } else {
                    // Key algorithm does not imply a digest.
                    if (spec.isDigestsSpecified()) {
                        mKeymasterDigests = android.security.keystore.KeyProperties.Digest.allToKeymaster(spec.getDigests());
                    } else {
                        mKeymasterDigests = libcore.util.EmptyArray.INT;
                    }
                }
                // Check that user authentication related parameters are acceptable. This method
                // will throw an IllegalStateException if there are issues (e.g., secure lock screen
                // not set up).
                android.security.keystore.KeymasterUtils.addUserAuthArgs(new android.security.keymaster.KeymasterArguments(), spec.isUserAuthenticationRequired(), spec.getUserAuthenticationValidityDurationSeconds(), spec.isUserAuthenticationValidWhileOnBody(), spec.isInvalidatedByBiometricEnrollment());
            } catch (java.lang.IllegalStateException | java.lang.IllegalArgumentException e) {
                throw new java.security.InvalidAlgorithmParameterException(e);
            }
            success = true;
        } finally {
            if (!success) {
                resetAll();
            }
        }
    }

    private void resetAll() {
        mSpec = null;
        mRng = null;
        mKeySizeBits = -1;
        mKeymasterPurposes = null;
        mKeymasterPaddings = null;
        mKeymasterBlockModes = null;
    }

    @java.lang.Override
    protected javax.crypto.SecretKey engineGenerateKey() {
        android.security.keystore.KeyGenParameterSpec spec = mSpec;
        if (spec == null) {
            throw new java.lang.IllegalStateException("Not initialized");
        }
        android.security.keymaster.KeymasterArguments args = new android.security.keymaster.KeymasterArguments();
        args.addUnsignedInt(android.security.keymaster.KeymasterDefs.KM_TAG_KEY_SIZE, mKeySizeBits);
        args.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_ALGORITHM, mKeymasterAlgorithm);
        args.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_PURPOSE, mKeymasterPurposes);
        args.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_BLOCK_MODE, mKeymasterBlockModes);
        args.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_PADDING, mKeymasterPaddings);
        args.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_DIGEST, mKeymasterDigests);
        android.security.keystore.KeymasterUtils.addUserAuthArgs(args, spec.isUserAuthenticationRequired(), spec.getUserAuthenticationValidityDurationSeconds(), spec.isUserAuthenticationValidWhileOnBody(), spec.isInvalidatedByBiometricEnrollment());
        android.security.keystore.KeymasterUtils.addMinMacLengthAuthorizationIfNecessary(args, mKeymasterAlgorithm, mKeymasterBlockModes, mKeymasterDigests);
        args.addDateIfNotNull(android.security.keymaster.KeymasterDefs.KM_TAG_ACTIVE_DATETIME, spec.getKeyValidityStart());
        args.addDateIfNotNull(android.security.keymaster.KeymasterDefs.KM_TAG_ORIGINATION_EXPIRE_DATETIME, spec.getKeyValidityForOriginationEnd());
        args.addDateIfNotNull(android.security.keymaster.KeymasterDefs.KM_TAG_USAGE_EXPIRE_DATETIME, spec.getKeyValidityForConsumptionEnd());
        if (((spec.getPurposes() & android.security.keystore.KeyProperties.PURPOSE_ENCRYPT) != 0) && (!spec.isRandomizedEncryptionRequired())) {
            // Permit caller-provided IV when encrypting with this key
            args.addBoolean(android.security.keymaster.KeymasterDefs.KM_TAG_CALLER_NONCE);
        }
        byte[] additionalEntropy = android.security.keystore.KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(mRng, (mKeySizeBits + 7) / 8);
        int flags = 0;
        java.lang.String keyAliasInKeystore = android.security.Credentials.USER_SECRET_KEY + spec.getKeystoreAlias();
        android.security.keymaster.KeyCharacteristics resultingKeyCharacteristics = new android.security.keymaster.KeyCharacteristics();
        boolean success = false;
        try {
            android.security.Credentials.deleteAllTypesForAlias(mKeyStore, spec.getKeystoreAlias(), spec.getUid());
            int errorCode = mKeyStore.generateKey(keyAliasInKeystore, args, additionalEntropy, spec.getUid(), flags, resultingKeyCharacteristics);
            if (errorCode != android.security.KeyStore.NO_ERROR) {
                throw new java.security.ProviderException("Keystore operation failed", android.security.KeyStore.getKeyStoreException(errorCode));
            }
            @android.security.keystore.KeyProperties.KeyAlgorithmEnum
            java.lang.String keyAlgorithmJCA;
            try {
                keyAlgorithmJCA = android.security.keystore.KeyProperties.KeyAlgorithm.fromKeymasterSecretKeyAlgorithm(mKeymasterAlgorithm, mKeymasterDigest);
            } catch (java.lang.IllegalArgumentException e) {
                throw new java.security.ProviderException("Failed to obtain JCA secret key algorithm name", e);
            }
            javax.crypto.SecretKey result = new android.security.keystore.AndroidKeyStoreSecretKey(keyAliasInKeystore, spec.getUid(), keyAlgorithmJCA);
            success = true;
            return result;
        } finally {
            if (!success) {
                android.security.Credentials.deleteAllTypesForAlias(mKeyStore, spec.getKeystoreAlias(), spec.getUid());
            }
        }
    }
}

