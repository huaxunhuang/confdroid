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
package android.security.keystore;


/**
 * Provides a way to create instances of a KeyPair which will be placed in the
 * Android keystore service usable only by the application that called it. This
 * can be used in conjunction with
 * {@link java.security.KeyStore#getInstance(String)} using the
 * {@code "AndroidKeyStore"} type.
 * <p>
 * This class can not be directly instantiated and must instead be used via the
 * {@link KeyPairGenerator#getInstance(String)
 * KeyPairGenerator.getInstance("AndroidKeyStore")} API.
 *
 * @unknown 
 */
public abstract class AndroidKeyStoreKeyPairGeneratorSpi extends java.security.KeyPairGeneratorSpi {
    public static class RSA extends android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi {
        public RSA() {
            super(android.security.keymaster.KeymasterDefs.KM_ALGORITHM_RSA);
        }
    }

    public static class EC extends android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi {
        public EC() {
            super(android.security.keymaster.KeymasterDefs.KM_ALGORITHM_EC);
        }
    }

    /* These must be kept in sync with system/security/keystore/defaults.h */
    /* EC */
    private static final int EC_DEFAULT_KEY_SIZE = 256;

    /* RSA */
    private static final int RSA_DEFAULT_KEY_SIZE = 2048;

    private static final int RSA_MIN_KEY_SIZE = 512;

    private static final int RSA_MAX_KEY_SIZE = 8192;

    private static final java.util.Map<java.lang.String, java.lang.Integer> SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE = new java.util.HashMap<java.lang.String, java.lang.Integer>();

    private static final java.util.List<java.lang.String> SUPPORTED_EC_NIST_CURVE_NAMES = new java.util.ArrayList<java.lang.String>();

    private static final java.util.List<java.lang.Integer> SUPPORTED_EC_NIST_CURVE_SIZES = new java.util.ArrayList<java.lang.Integer>();

    static {
        // Aliases for NIST P-224
        android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("p-224", 224);
        android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp224r1", 224);
        // Aliases for NIST P-256
        android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("p-256", 256);
        android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp256r1", 256);
        android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("prime256v1", 256);
        // Aliases for NIST P-384
        android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("p-384", 384);
        android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp384r1", 384);
        // Aliases for NIST P-521
        android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("p-521", 521);
        android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp521r1", 521);
        android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_NAMES.addAll(android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.keySet());
        java.util.Collections.sort(android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_NAMES);
        android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_SIZES.addAll(new java.util.HashSet<java.lang.Integer>(android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.values()));
        java.util.Collections.sort(android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_SIZES);
    }

    private final int mOriginalKeymasterAlgorithm;

    private android.security.KeyStore mKeyStore;

    private android.security.keystore.KeyGenParameterSpec mSpec;

    private java.lang.String mEntryAlias;

    private int mEntryUid;

    private boolean mEncryptionAtRestRequired;

    @android.security.keystore.KeyProperties.KeyAlgorithmEnum
    private java.lang.String mJcaKeyAlgorithm;

    private int mKeymasterAlgorithm = -1;

    private int mKeySizeBits;

    private java.security.SecureRandom mRng;

    private int[] mKeymasterPurposes;

    private int[] mKeymasterBlockModes;

    private int[] mKeymasterEncryptionPaddings;

    private int[] mKeymasterSignaturePaddings;

    private int[] mKeymasterDigests;

    private java.math.BigInteger mRSAPublicExponent;

    protected AndroidKeyStoreKeyPairGeneratorSpi(int keymasterAlgorithm) {
        mOriginalKeymasterAlgorithm = keymasterAlgorithm;
    }

    @java.lang.SuppressWarnings("deprecation")
    @java.lang.Override
    public void initialize(int keysize, java.security.SecureRandom random) {
        throw new java.lang.IllegalArgumentException(((android.security.keystore.KeyGenParameterSpec.class.getName() + " or ") + android.security.KeyPairGeneratorSpec.class.getName()) + " required to initialize this KeyPairGenerator");
    }

    @java.lang.SuppressWarnings("deprecation")
    @java.lang.Override
    public void initialize(java.security.spec.AlgorithmParameterSpec params, java.security.SecureRandom random) throws java.security.InvalidAlgorithmParameterException {
        resetAll();
        boolean success = false;
        try {
            if (params == null) {
                throw new java.security.InvalidAlgorithmParameterException((("Must supply params of type " + android.security.keystore.KeyGenParameterSpec.class.getName()) + " or ") + android.security.KeyPairGeneratorSpec.class.getName());
            }
            android.security.keystore.KeyGenParameterSpec spec;
            boolean encryptionAtRestRequired = false;
            int keymasterAlgorithm = mOriginalKeymasterAlgorithm;
            if (params instanceof android.security.keystore.KeyGenParameterSpec) {
                spec = ((android.security.keystore.KeyGenParameterSpec) (params));
            } else
                if (params instanceof android.security.KeyPairGeneratorSpec) {
                    // Legacy/deprecated spec
                    android.security.KeyPairGeneratorSpec legacySpec = ((android.security.KeyPairGeneratorSpec) (params));
                    try {
                        android.security.keystore.KeyGenParameterSpec.Builder specBuilder;
                        java.lang.String specKeyAlgorithm = legacySpec.getKeyType();
                        if (specKeyAlgorithm != null) {
                            // Spec overrides the generator's default key algorithm
                            try {
                                keymasterAlgorithm = android.security.keystore.KeyProperties.KeyAlgorithm.toKeymasterAsymmetricKeyAlgorithm(specKeyAlgorithm);
                            } catch (java.lang.IllegalArgumentException e) {
                                throw new java.security.InvalidAlgorithmParameterException("Invalid key type in parameters", e);
                            }
                        }
                        switch (keymasterAlgorithm) {
                            case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_EC :
                                specBuilder = new android.security.keystore.KeyGenParameterSpec.Builder(legacySpec.getKeystoreAlias(), android.security.keystore.KeyProperties.PURPOSE_SIGN | android.security.keystore.KeyProperties.PURPOSE_VERIFY);
                                // Authorized to be used with any digest (including no digest).
                                // MD5 was never offered for Android Keystore for ECDSA.
                                specBuilder.setDigests(android.security.keystore.KeyProperties.DIGEST_NONE, android.security.keystore.KeyProperties.DIGEST_SHA1, android.security.keystore.KeyProperties.DIGEST_SHA224, android.security.keystore.KeyProperties.DIGEST_SHA256, android.security.keystore.KeyProperties.DIGEST_SHA384, android.security.keystore.KeyProperties.DIGEST_SHA512);
                                break;
                            case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_RSA :
                                specBuilder = new android.security.keystore.KeyGenParameterSpec.Builder(legacySpec.getKeystoreAlias(), ((android.security.keystore.KeyProperties.PURPOSE_ENCRYPT | android.security.keystore.KeyProperties.PURPOSE_DECRYPT) | android.security.keystore.KeyProperties.PURPOSE_SIGN) | android.security.keystore.KeyProperties.PURPOSE_VERIFY);
                                // Authorized to be used with any digest (including no digest).
                                specBuilder.setDigests(android.security.keystore.KeyProperties.DIGEST_NONE, android.security.keystore.KeyProperties.DIGEST_MD5, android.security.keystore.KeyProperties.DIGEST_SHA1, android.security.keystore.KeyProperties.DIGEST_SHA224, android.security.keystore.KeyProperties.DIGEST_SHA256, android.security.keystore.KeyProperties.DIGEST_SHA384, android.security.keystore.KeyProperties.DIGEST_SHA512);
                                // Authorized to be used with any encryption and signature padding
                                // schemes (including no padding).
                                specBuilder.setEncryptionPaddings(android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE, android.security.keystore.KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1, android.security.keystore.KeyProperties.ENCRYPTION_PADDING_RSA_OAEP);
                                specBuilder.setSignaturePaddings(android.security.keystore.KeyProperties.SIGNATURE_PADDING_RSA_PKCS1, android.security.keystore.KeyProperties.SIGNATURE_PADDING_RSA_PSS);
                                // Disable randomized encryption requirement to support encryption
                                // padding NONE above.
                                specBuilder.setRandomizedEncryptionRequired(false);
                                break;
                            default :
                                throw new java.security.ProviderException("Unsupported algorithm: " + mKeymasterAlgorithm);
                        }
                        if (legacySpec.getKeySize() != (-1)) {
                            specBuilder.setKeySize(legacySpec.getKeySize());
                        }
                        if (legacySpec.getAlgorithmParameterSpec() != null) {
                            specBuilder.setAlgorithmParameterSpec(legacySpec.getAlgorithmParameterSpec());
                        }
                        specBuilder.setCertificateSubject(legacySpec.getSubjectDN());
                        specBuilder.setCertificateSerialNumber(legacySpec.getSerialNumber());
                        specBuilder.setCertificateNotBefore(legacySpec.getStartDate());
                        specBuilder.setCertificateNotAfter(legacySpec.getEndDate());
                        encryptionAtRestRequired = legacySpec.isEncryptionRequired();
                        specBuilder.setUserAuthenticationRequired(false);
                        spec = specBuilder.build();
                    } catch (java.lang.NullPointerException | java.lang.IllegalArgumentException e) {
                        throw new java.security.InvalidAlgorithmParameterException(e);
                    }
                } else {
                    throw new java.security.InvalidAlgorithmParameterException((((("Unsupported params class: " + params.getClass().getName()) + ". Supported: ") + android.security.keystore.KeyGenParameterSpec.class.getName()) + ", ") + android.security.KeyPairGeneratorSpec.class.getName());
                }

            mEntryAlias = spec.getKeystoreAlias();
            mEntryUid = spec.getUid();
            mSpec = spec;
            mKeymasterAlgorithm = keymasterAlgorithm;
            mEncryptionAtRestRequired = encryptionAtRestRequired;
            mKeySizeBits = spec.getKeySize();
            initAlgorithmSpecificParameters();
            if (mKeySizeBits == (-1)) {
                mKeySizeBits = android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.getDefaultKeySize(keymasterAlgorithm);
            }
            android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.checkValidKeySize(keymasterAlgorithm, mKeySizeBits);
            if (spec.getKeystoreAlias() == null) {
                throw new java.security.InvalidAlgorithmParameterException("KeyStore entry alias not provided");
            }
            java.lang.String jcaKeyAlgorithm;
            try {
                jcaKeyAlgorithm = android.security.keystore.KeyProperties.KeyAlgorithm.fromKeymasterAsymmetricKeyAlgorithm(keymasterAlgorithm);
                mKeymasterPurposes = android.security.keystore.KeyProperties.Purpose.allToKeymaster(spec.getPurposes());
                mKeymasterBlockModes = android.security.keystore.KeyProperties.BlockMode.allToKeymaster(spec.getBlockModes());
                mKeymasterEncryptionPaddings = android.security.keystore.KeyProperties.EncryptionPadding.allToKeymaster(spec.getEncryptionPaddings());
                if (((spec.getPurposes() & android.security.keystore.KeyProperties.PURPOSE_ENCRYPT) != 0) && spec.isRandomizedEncryptionRequired()) {
                    for (int keymasterPadding : mKeymasterEncryptionPaddings) {
                        if (!android.security.keystore.KeymasterUtils.isKeymasterPaddingSchemeIndCpaCompatibleWithAsymmetricCrypto(keymasterPadding)) {
                            throw new java.security.InvalidAlgorithmParameterException((((("Randomized encryption (IND-CPA) required but may be violated" + " by padding scheme: ") + android.security.keystore.KeyProperties.EncryptionPadding.fromKeymaster(keymasterPadding)) + ". See ") + android.security.keystore.KeyGenParameterSpec.class.getName()) + " documentation.");
                        }
                    }
                }
                mKeymasterSignaturePaddings = android.security.keystore.KeyProperties.SignaturePadding.allToKeymaster(spec.getSignaturePaddings());
                if (spec.isDigestsSpecified()) {
                    mKeymasterDigests = android.security.keystore.KeyProperties.Digest.allToKeymaster(spec.getDigests());
                } else {
                    mKeymasterDigests = libcore.util.EmptyArray.INT;
                }
                // Check that user authentication related parameters are acceptable. This method
                // will throw an IllegalStateException if there are issues (e.g., secure lock screen
                // not set up).
                android.security.keystore.KeymasterUtils.addUserAuthArgs(new android.security.keymaster.KeymasterArguments(), mSpec.isUserAuthenticationRequired(), mSpec.getUserAuthenticationValidityDurationSeconds(), mSpec.isUserAuthenticationValidWhileOnBody(), mSpec.isInvalidatedByBiometricEnrollment());
            } catch (java.lang.IllegalArgumentException | java.lang.IllegalStateException e) {
                throw new java.security.InvalidAlgorithmParameterException(e);
            }
            mJcaKeyAlgorithm = jcaKeyAlgorithm;
            mRng = random;
            mKeyStore = android.security.KeyStore.getInstance();
            success = true;
        } finally {
            if (!success) {
                resetAll();
            }
        }
    }

    private void resetAll() {
        mEntryAlias = null;
        mEntryUid = android.security.KeyStore.UID_SELF;
        mJcaKeyAlgorithm = null;
        mKeymasterAlgorithm = -1;
        mKeymasterPurposes = null;
        mKeymasterBlockModes = null;
        mKeymasterEncryptionPaddings = null;
        mKeymasterSignaturePaddings = null;
        mKeymasterDigests = null;
        mKeySizeBits = 0;
        mSpec = null;
        mRSAPublicExponent = null;
        mEncryptionAtRestRequired = false;
        mRng = null;
        mKeyStore = null;
    }

    private void initAlgorithmSpecificParameters() throws java.security.InvalidAlgorithmParameterException {
        java.security.spec.AlgorithmParameterSpec algSpecificSpec = mSpec.getAlgorithmParameterSpec();
        switch (mKeymasterAlgorithm) {
            case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_RSA :
                {
                    java.math.BigInteger publicExponent = null;
                    if (algSpecificSpec instanceof java.security.spec.RSAKeyGenParameterSpec) {
                        java.security.spec.RSAKeyGenParameterSpec rsaSpec = ((java.security.spec.RSAKeyGenParameterSpec) (algSpecificSpec));
                        if (mKeySizeBits == (-1)) {
                            mKeySizeBits = rsaSpec.getKeysize();
                        } else
                            if (mKeySizeBits != rsaSpec.getKeysize()) {
                                throw new java.security.InvalidAlgorithmParameterException(((((((("RSA key size must match " + " between ") + mSpec) + " and ") + algSpecificSpec) + ": ") + mKeySizeBits) + " vs ") + rsaSpec.getKeysize());
                            }

                        publicExponent = rsaSpec.getPublicExponent();
                    } else
                        if (algSpecificSpec != null) {
                            throw new java.security.InvalidAlgorithmParameterException("RSA may only use RSAKeyGenParameterSpec");
                        }

                    if (publicExponent == null) {
                        publicExponent = java.security.spec.RSAKeyGenParameterSpec.F4;
                    }
                    if (publicExponent.compareTo(java.math.BigInteger.ZERO) < 1) {
                        throw new java.security.InvalidAlgorithmParameterException("RSA public exponent must be positive: " + publicExponent);
                    }
                    if (publicExponent.compareTo(android.security.keymaster.KeymasterArguments.UINT64_MAX_VALUE) > 0) {
                        throw new java.security.InvalidAlgorithmParameterException((("Unsupported RSA public exponent: " + publicExponent) + ". Maximum supported value: ") + android.security.keymaster.KeymasterArguments.UINT64_MAX_VALUE);
                    }
                    mRSAPublicExponent = publicExponent;
                    break;
                }
            case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_EC :
                if (algSpecificSpec instanceof java.security.spec.ECGenParameterSpec) {
                    java.security.spec.ECGenParameterSpec ecSpec = ((java.security.spec.ECGenParameterSpec) (algSpecificSpec));
                    java.lang.String curveName = ecSpec.getName();
                    java.lang.Integer ecSpecKeySizeBits = android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.get(curveName.toLowerCase(java.util.Locale.US));
                    if (ecSpecKeySizeBits == null) {
                        throw new java.security.InvalidAlgorithmParameterException((("Unsupported EC curve name: " + curveName) + ". Supported: ") + android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_NAMES);
                    }
                    if (mKeySizeBits == (-1)) {
                        mKeySizeBits = ecSpecKeySizeBits;
                    } else
                        if (mKeySizeBits != ecSpecKeySizeBits) {
                            throw new java.security.InvalidAlgorithmParameterException(((((((("EC key size must match " + " between ") + mSpec) + " and ") + algSpecificSpec) + ": ") + mKeySizeBits) + " vs ") + ecSpecKeySizeBits);
                        }

                } else
                    if (algSpecificSpec != null) {
                        throw new java.security.InvalidAlgorithmParameterException("EC may only use ECGenParameterSpec");
                    }

                break;
            default :
                throw new java.security.ProviderException("Unsupported algorithm: " + mKeymasterAlgorithm);
        }
    }

    @java.lang.Override
    public java.security.KeyPair generateKeyPair() {
        if ((mKeyStore == null) || (mSpec == null)) {
            throw new java.lang.IllegalStateException("Not initialized");
        }
        final int flags = (mEncryptionAtRestRequired) ? android.security.KeyStore.FLAG_ENCRYPTED : 0;
        if (((flags & android.security.KeyStore.FLAG_ENCRYPTED) != 0) && (mKeyStore.state() != android.security.KeyStore.State.UNLOCKED)) {
            throw new java.lang.IllegalStateException("Encryption at rest using secure lock screen credential requested for key pair" + ", but the user has not yet entered the credential");
        }
        byte[] additionalEntropy = android.security.keystore.KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(mRng, (mKeySizeBits + 7) / 8);
        android.security.Credentials.deleteAllTypesForAlias(mKeyStore, mEntryAlias, mEntryUid);
        final java.lang.String privateKeyAlias = android.security.Credentials.USER_PRIVATE_KEY + mEntryAlias;
        boolean success = false;
        try {
            generateKeystoreKeyPair(privateKeyAlias, constructKeyGenerationArguments(), additionalEntropy, flags);
            java.security.KeyPair keyPair = loadKeystoreKeyPair(privateKeyAlias);
            storeCertificateChain(flags, createCertificateChain(privateKeyAlias, keyPair));
            success = true;
            return keyPair;
        } finally {
            if (!success) {
                android.security.Credentials.deleteAllTypesForAlias(mKeyStore, mEntryAlias, mEntryUid);
            }
        }
    }

    private java.lang.Iterable<byte[]> createCertificateChain(final java.lang.String privateKeyAlias, java.security.KeyPair keyPair) throws java.security.ProviderException {
        byte[] challenge = mSpec.getAttestationChallenge();
        if (challenge != null) {
            android.security.keymaster.KeymasterArguments args = new android.security.keymaster.KeymasterArguments();
            args.addBytes(android.security.keymaster.KeymasterDefs.KM_TAG_ATTESTATION_CHALLENGE, challenge);
            return getAttestationChain(privateKeyAlias, keyPair, args);
        }
        // Very short certificate chain in the non-attestation case.
        return java.util.Collections.singleton(generateSelfSignedCertificateBytes(keyPair));
    }

    private void generateKeystoreKeyPair(final java.lang.String privateKeyAlias, android.security.keymaster.KeymasterArguments args, byte[] additionalEntropy, final int flags) throws java.security.ProviderException {
        android.security.keymaster.KeyCharacteristics resultingKeyCharacteristics = new android.security.keymaster.KeyCharacteristics();
        int errorCode = mKeyStore.generateKey(privateKeyAlias, args, additionalEntropy, mEntryUid, flags, resultingKeyCharacteristics);
        if (errorCode != android.security.KeyStore.NO_ERROR) {
            throw new java.security.ProviderException("Failed to generate key pair", android.security.KeyStore.getKeyStoreException(errorCode));
        }
    }

    private java.security.KeyPair loadKeystoreKeyPair(final java.lang.String privateKeyAlias) throws java.security.ProviderException {
        try {
            java.security.KeyPair result = android.security.keystore.AndroidKeyStoreProvider.loadAndroidKeyStoreKeyPairFromKeystore(mKeyStore, privateKeyAlias, mEntryUid);
            if (!mJcaKeyAlgorithm.equalsIgnoreCase(result.getPrivate().getAlgorithm())) {
                throw new java.security.ProviderException((("Generated key pair algorithm does not match requested algorithm: " + result.getPrivate().getAlgorithm()) + " vs ") + mJcaKeyAlgorithm);
            }
            return result;
        } catch (java.security.UnrecoverableKeyException e) {
            throw new java.security.ProviderException("Failed to load generated key pair from keystore", e);
        }
    }

    private android.security.keymaster.KeymasterArguments constructKeyGenerationArguments() {
        android.security.keymaster.KeymasterArguments args = new android.security.keymaster.KeymasterArguments();
        args.addUnsignedInt(android.security.keymaster.KeymasterDefs.KM_TAG_KEY_SIZE, mKeySizeBits);
        args.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_ALGORITHM, mKeymasterAlgorithm);
        args.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_PURPOSE, mKeymasterPurposes);
        args.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_BLOCK_MODE, mKeymasterBlockModes);
        args.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_PADDING, mKeymasterEncryptionPaddings);
        args.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_PADDING, mKeymasterSignaturePaddings);
        args.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_DIGEST, mKeymasterDigests);
        android.security.keystore.KeymasterUtils.addUserAuthArgs(args, mSpec.isUserAuthenticationRequired(), mSpec.getUserAuthenticationValidityDurationSeconds(), mSpec.isUserAuthenticationValidWhileOnBody(), mSpec.isInvalidatedByBiometricEnrollment());
        args.addDateIfNotNull(android.security.keymaster.KeymasterDefs.KM_TAG_ACTIVE_DATETIME, mSpec.getKeyValidityStart());
        args.addDateIfNotNull(android.security.keymaster.KeymasterDefs.KM_TAG_ORIGINATION_EXPIRE_DATETIME, mSpec.getKeyValidityForOriginationEnd());
        args.addDateIfNotNull(android.security.keymaster.KeymasterDefs.KM_TAG_USAGE_EXPIRE_DATETIME, mSpec.getKeyValidityForConsumptionEnd());
        addAlgorithmSpecificParameters(args);
        if (mSpec.isUniqueIdIncluded())
            args.addBoolean(android.security.keymaster.KeymasterDefs.KM_TAG_INCLUDE_UNIQUE_ID);

        return args;
    }

    private void storeCertificateChain(final int flags, java.lang.Iterable<byte[]> iterable) throws java.security.ProviderException {
        java.util.Iterator<byte[]> iter = iterable.iterator();
        storeCertificate(android.security.Credentials.USER_CERTIFICATE, iter.next(), flags, "Failed to store certificate");
        if (!iter.hasNext()) {
            return;
        }
        java.io.ByteArrayOutputStream certificateConcatenationStream = new java.io.ByteArrayOutputStream();
        while (iter.hasNext()) {
            byte[] data = iter.next();
            certificateConcatenationStream.write(data, 0, data.length);
        } 
        storeCertificate(android.security.Credentials.CA_CERTIFICATE, certificateConcatenationStream.toByteArray(), flags, "Failed to store attestation CA certificate");
    }

    private void storeCertificate(java.lang.String prefix, byte[] certificateBytes, final int flags, java.lang.String failureMessage) throws java.security.ProviderException {
        int insertErrorCode = mKeyStore.insert(prefix + mEntryAlias, certificateBytes, mEntryUid, flags);
        if (insertErrorCode != android.security.KeyStore.NO_ERROR) {
            throw new java.security.ProviderException(failureMessage, android.security.KeyStore.getKeyStoreException(insertErrorCode));
        }
    }

    private byte[] generateSelfSignedCertificateBytes(java.security.KeyPair keyPair) throws java.security.ProviderException {
        try {
            return generateSelfSignedCertificate(keyPair.getPrivate(), keyPair.getPublic()).getEncoded();
        } catch (java.io.IOException | java.security.cert.CertificateParsingException e) {
            throw new java.security.ProviderException("Failed to generate self-signed certificate", e);
        } catch (java.security.cert.CertificateEncodingException e) {
            throw new java.security.ProviderException("Failed to obtain encoded form of self-signed certificate", e);
        }
    }

    private java.lang.Iterable<byte[]> getAttestationChain(java.lang.String privateKeyAlias, java.security.KeyPair keyPair, android.security.keymaster.KeymasterArguments args) throws java.security.ProviderException {
        android.security.keymaster.KeymasterCertificateChain outChain = new android.security.keymaster.KeymasterCertificateChain();
        int errorCode = mKeyStore.attestKey(privateKeyAlias, args, outChain);
        if (errorCode != android.security.KeyStore.NO_ERROR) {
            throw new java.security.ProviderException("Failed to generate attestation certificate chain", android.security.KeyStore.getKeyStoreException(errorCode));
        }
        java.util.Collection<byte[]> chain = outChain.getCertificates();
        if (chain.size() < 2) {
            throw new java.security.ProviderException(("Attestation certificate chain contained " + chain.size()) + " entries. At least two are required.");
        }
        return chain;
    }

    private void addAlgorithmSpecificParameters(android.security.keymaster.KeymasterArguments keymasterArgs) {
        switch (mKeymasterAlgorithm) {
            case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_RSA :
                keymasterArgs.addUnsignedLong(android.security.keymaster.KeymasterDefs.KM_TAG_RSA_PUBLIC_EXPONENT, mRSAPublicExponent);
                break;
            case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_EC :
                break;
            default :
                throw new java.security.ProviderException("Unsupported algorithm: " + mKeymasterAlgorithm);
        }
    }

    private java.security.cert.X509Certificate generateSelfSignedCertificate(java.security.PrivateKey privateKey, java.security.PublicKey publicKey) throws java.io.IOException, java.security.cert.CertificateParsingException {
        java.lang.String signatureAlgorithm = android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.getCertificateSignatureAlgorithm(mKeymasterAlgorithm, mKeySizeBits, mSpec);
        if (signatureAlgorithm == null) {
            // Key cannot be used to sign a certificate
            return generateSelfSignedCertificateWithFakeSignature(publicKey);
        } else {
            // Key can be used to sign a certificate
            try {
                return generateSelfSignedCertificateWithValidSignature(privateKey, publicKey, signatureAlgorithm);
            } catch (java.lang.Exception e) {
                // Failed to generate the self-signed certificate with valid signature. Fall back
                // to generating a self-signed certificate with a fake signature. This is done for
                // all exception types because we prefer key pair generation to succeed and end up
                // producing a self-signed certificate with an invalid signature to key pair
                // generation failing.
                return generateSelfSignedCertificateWithFakeSignature(publicKey);
            }
        }
    }

    @java.lang.SuppressWarnings("deprecation")
    private java.security.cert.X509Certificate generateSelfSignedCertificateWithValidSignature(java.security.PrivateKey privateKey, java.security.PublicKey publicKey, java.lang.String signatureAlgorithm) throws java.lang.Exception {
        final com.android.org.bouncycastle.x509.X509V3CertificateGenerator certGen = new com.android.org.bouncycastle.x509.X509V3CertificateGenerator();
        certGen.setPublicKey(publicKey);
        certGen.setSerialNumber(mSpec.getCertificateSerialNumber());
        certGen.setSubjectDN(mSpec.getCertificateSubject());
        certGen.setIssuerDN(mSpec.getCertificateSubject());
        certGen.setNotBefore(mSpec.getCertificateNotBefore());
        certGen.setNotAfter(mSpec.getCertificateNotAfter());
        certGen.setSignatureAlgorithm(signatureAlgorithm);
        return certGen.generate(privateKey);
    }

    @java.lang.SuppressWarnings("deprecation")
    private java.security.cert.X509Certificate generateSelfSignedCertificateWithFakeSignature(java.security.PublicKey publicKey) throws java.io.IOException, java.security.cert.CertificateParsingException {
        com.android.org.bouncycastle.asn1.x509.V3TBSCertificateGenerator tbsGenerator = new com.android.org.bouncycastle.asn1.x509.V3TBSCertificateGenerator();
        com.android.org.bouncycastle.asn1.ASN1ObjectIdentifier sigAlgOid;
        com.android.org.bouncycastle.asn1.x509.AlgorithmIdentifier sigAlgId;
        byte[] signature;
        switch (mKeymasterAlgorithm) {
            case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_EC :
                sigAlgOid = com.android.org.bouncycastle.asn1.x9.X9ObjectIdentifiers.ecdsa_with_SHA256;
                sigAlgId = new com.android.org.bouncycastle.asn1.x509.AlgorithmIdentifier(sigAlgOid);
                com.android.org.bouncycastle.asn1.ASN1EncodableVector v = new com.android.org.bouncycastle.asn1.ASN1EncodableVector();
                v.add(new com.android.org.bouncycastle.asn1.DERInteger(0));
                v.add(new com.android.org.bouncycastle.asn1.DERInteger(0));
                signature = new com.android.org.bouncycastle.asn1.DERSequence().getEncoded();
                break;
            case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_RSA :
                sigAlgOid = com.android.org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers.sha256WithRSAEncryption;
                sigAlgId = new com.android.org.bouncycastle.asn1.x509.AlgorithmIdentifier(sigAlgOid, com.android.org.bouncycastle.asn1.DERNull.INSTANCE);
                signature = new byte[1];
                break;
            default :
                throw new java.security.ProviderException("Unsupported key algorithm: " + mKeymasterAlgorithm);
        }
        try (com.android.org.bouncycastle.asn1.ASN1InputStream publicKeyInfoIn = new com.android.org.bouncycastle.asn1.ASN1InputStream(publicKey.getEncoded())) {
            tbsGenerator.setSubjectPublicKeyInfo(com.android.org.bouncycastle.asn1.x509.SubjectPublicKeyInfo.getInstance(publicKeyInfoIn.readObject()));
        }
        tbsGenerator.setSerialNumber(new com.android.org.bouncycastle.asn1.ASN1Integer(mSpec.getCertificateSerialNumber()));
        com.android.org.bouncycastle.jce.X509Principal subject = new com.android.org.bouncycastle.jce.X509Principal(mSpec.getCertificateSubject().getEncoded());
        tbsGenerator.setSubject(subject);
        tbsGenerator.setIssuer(subject);
        tbsGenerator.setStartDate(new com.android.org.bouncycastle.asn1.x509.Time(mSpec.getCertificateNotBefore()));
        tbsGenerator.setEndDate(new com.android.org.bouncycastle.asn1.x509.Time(mSpec.getCertificateNotAfter()));
        tbsGenerator.setSignature(sigAlgId);
        com.android.org.bouncycastle.asn1.x509.TBSCertificate tbsCertificate = tbsGenerator.generateTBSCertificate();
        com.android.org.bouncycastle.asn1.ASN1EncodableVector result = new com.android.org.bouncycastle.asn1.ASN1EncodableVector();
        result.add(tbsCertificate);
        result.add(sigAlgId);
        result.add(new com.android.org.bouncycastle.asn1.DERBitString(signature));
        return new com.android.org.bouncycastle.jce.provider.X509CertificateObject(com.android.org.bouncycastle.asn1.x509.Certificate.getInstance(new com.android.org.bouncycastle.asn1.DERSequence(result)));
    }

    private static int getDefaultKeySize(int keymasterAlgorithm) {
        switch (keymasterAlgorithm) {
            case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_EC :
                return android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.EC_DEFAULT_KEY_SIZE;
            case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_RSA :
                return android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.RSA_DEFAULT_KEY_SIZE;
            default :
                throw new java.security.ProviderException("Unsupported algorithm: " + keymasterAlgorithm);
        }
    }

    private static void checkValidKeySize(int keymasterAlgorithm, int keySize) throws java.security.InvalidAlgorithmParameterException {
        switch (keymasterAlgorithm) {
            case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_EC :
                if (!android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_SIZES.contains(keySize)) {
                    throw new java.security.InvalidAlgorithmParameterException((("Unsupported EC key size: " + keySize) + " bits. Supported: ") + android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.SUPPORTED_EC_NIST_CURVE_SIZES);
                }
                break;
            case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_RSA :
                if ((keySize < android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.RSA_MIN_KEY_SIZE) || (keySize > android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.RSA_MAX_KEY_SIZE)) {
                    throw new java.security.InvalidAlgorithmParameterException((("RSA key size must be >= " + android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.RSA_MIN_KEY_SIZE) + " and <= ") + android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.RSA_MAX_KEY_SIZE);
                }
                break;
            default :
                throw new java.security.ProviderException("Unsupported algorithm: " + keymasterAlgorithm);
        }
    }

    /**
     * Returns the {@code Signature} algorithm to be used for signing a certificate using the
     * specified key or {@code null} if the key cannot be used for signing a certificate.
     */
    @android.annotation.Nullable
    private static java.lang.String getCertificateSignatureAlgorithm(int keymasterAlgorithm, int keySizeBits, android.security.keystore.KeyGenParameterSpec spec) {
        // Constraints:
        // 1. Key must be authorized for signing without user authentication.
        // 2. Signature digest must be one of key's authorized digests.
        // 3. For RSA keys, the digest output size must not exceed modulus size minus space overhead
        // of RSA PKCS#1 signature padding scheme (about 30 bytes).
        // 4. For EC keys, the there is no point in using a digest whose output size is longer than
        // key/field size because the digest will be truncated to that size.
        if ((spec.getPurposes() & android.security.keystore.KeyProperties.PURPOSE_SIGN) == 0) {
            // Key not authorized for signing
            return null;
        }
        if (spec.isUserAuthenticationRequired()) {
            // Key not authorized for use without user authentication
            return null;
        }
        if (!spec.isDigestsSpecified()) {
            // Key not authorized for any digests -- can't sign
            return null;
        }
        switch (keymasterAlgorithm) {
            case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_EC :
                {
                    java.util.Set<java.lang.Integer> availableKeymasterDigests = android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.getAvailableKeymasterSignatureDigests(spec.getDigests(), android.security.keystore.AndroidKeyStoreBCWorkaroundProvider.getSupportedEcdsaSignatureDigests());
                    int bestKeymasterDigest = -1;
                    int bestDigestOutputSizeBits = -1;
                    for (int keymasterDigest : availableKeymasterDigests) {
                        int outputSizeBits = android.security.keystore.KeymasterUtils.getDigestOutputSizeBits(keymasterDigest);
                        if (outputSizeBits == keySizeBits) {
                            // Perfect match -- use this digest
                            bestKeymasterDigest = keymasterDigest;
                            bestDigestOutputSizeBits = outputSizeBits;
                            break;
                        }
                        // Not a perfect match -- check against the best digest so far
                        if (bestKeymasterDigest == (-1)) {
                            // First digest tested -- definitely the best so far
                            bestKeymasterDigest = keymasterDigest;
                            bestDigestOutputSizeBits = outputSizeBits;
                        } else {
                            // Prefer output size to be as close to key size as possible, with output
                            // sizes larger than key size preferred to those smaller than key size.
                            if (bestDigestOutputSizeBits < keySizeBits) {
                                // Output size of the best digest so far is smaller than key size.
                                // Anything larger is a win.
                                if (outputSizeBits > bestDigestOutputSizeBits) {
                                    bestKeymasterDigest = keymasterDigest;
                                    bestDigestOutputSizeBits = outputSizeBits;
                                }
                            } else {
                                // Output size of the best digest so far is larger than key size.
                                // Anything smaller is a win, as long as it's not smaller than key size.
                                if ((outputSizeBits < bestDigestOutputSizeBits) && (outputSizeBits >= keySizeBits)) {
                                    bestKeymasterDigest = keymasterDigest;
                                    bestDigestOutputSizeBits = outputSizeBits;
                                }
                            }
                        }
                    }
                    if (bestKeymasterDigest == (-1)) {
                        return null;
                    }
                    return android.security.keystore.KeyProperties.Digest.fromKeymasterToSignatureAlgorithmDigest(bestKeymasterDigest) + "WithECDSA";
                }
            case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_RSA :
                {
                    // Check whether this key is authorized for PKCS#1 signature padding.
                    // We use Bouncy Castle to generate self-signed RSA certificates. Bouncy Castle
                    // only supports RSA certificates signed using PKCS#1 padding scheme. The key needs
                    // to be authorized for PKCS#1 padding or padding NONE which means any padding.
                    boolean pkcs1SignaturePaddingSupported = android.security.keystore.com.android.internal.util.ArrayUtils.contains(android.security.keystore.KeyProperties.SignaturePadding.allToKeymaster(spec.getSignaturePaddings()), android.security.keymaster.KeymasterDefs.KM_PAD_RSA_PKCS1_1_5_SIGN);
                    if (!pkcs1SignaturePaddingSupported) {
                        // Key not authorized for PKCS#1 signature padding -- can't sign
                        return null;
                    }
                    java.util.Set<java.lang.Integer> availableKeymasterDigests = android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi.getAvailableKeymasterSignatureDigests(spec.getDigests(), android.security.keystore.AndroidKeyStoreBCWorkaroundProvider.getSupportedEcdsaSignatureDigests());
                    // The amount of space available for the digest is less than modulus size by about
                    // 30 bytes because padding must be at least 11 bytes long (00 || 01 || PS || 00,
                    // where PS must be at least 8 bytes long), and then there's also the 15--19 bytes
                    // overhead (depending the on chosen digest) for encoding digest OID and digest
                    // value in DER.
                    int maxDigestOutputSizeBits = keySizeBits - (30 * 8);
                    int bestKeymasterDigest = -1;
                    int bestDigestOutputSizeBits = -1;
                    for (int keymasterDigest : availableKeymasterDigests) {
                        int outputSizeBits = android.security.keystore.KeymasterUtils.getDigestOutputSizeBits(keymasterDigest);
                        if (outputSizeBits > maxDigestOutputSizeBits) {
                            // Digest too long (signature generation will fail) -- skip
                            continue;
                        }
                        if (bestKeymasterDigest == (-1)) {
                            // First digest tested -- definitely the best so far
                            bestKeymasterDigest = keymasterDigest;
                            bestDigestOutputSizeBits = outputSizeBits;
                        } else {
                            // The longer the better
                            if (outputSizeBits > bestDigestOutputSizeBits) {
                                bestKeymasterDigest = keymasterDigest;
                                bestDigestOutputSizeBits = outputSizeBits;
                            }
                        }
                    }
                    if (bestKeymasterDigest == (-1)) {
                        return null;
                    }
                    return android.security.keystore.KeyProperties.Digest.fromKeymasterToSignatureAlgorithmDigest(bestKeymasterDigest) + "WithRSA";
                }
            default :
                throw new java.security.ProviderException("Unsupported algorithm: " + keymasterAlgorithm);
        }
    }

    private static java.util.Set<java.lang.Integer> getAvailableKeymasterSignatureDigests(@android.security.keystore.KeyProperties.DigestEnum
    java.lang.String[] authorizedKeyDigests, @android.security.keystore.KeyProperties.DigestEnum
    java.lang.String[] supportedSignatureDigests) {
        java.util.Set<java.lang.Integer> authorizedKeymasterKeyDigests = new java.util.HashSet<java.lang.Integer>();
        for (int keymasterDigest : android.security.keystore.KeyProperties.Digest.allToKeymaster(authorizedKeyDigests)) {
            authorizedKeymasterKeyDigests.add(keymasterDigest);
        }
        java.util.Set<java.lang.Integer> supportedKeymasterSignatureDigests = new java.util.HashSet<java.lang.Integer>();
        for (int keymasterDigest : android.security.keystore.KeyProperties.Digest.allToKeymaster(supportedSignatureDigests)) {
            supportedKeymasterSignatureDigests.add(keymasterDigest);
        }
        java.util.Set<java.lang.Integer> result = new java.util.HashSet<java.lang.Integer>(supportedKeymasterSignatureDigests);
        result.retainAll(authorizedKeymasterKeyDigests);
        return result;
    }
}

