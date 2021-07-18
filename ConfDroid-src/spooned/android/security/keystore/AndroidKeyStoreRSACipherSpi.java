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
 * Base class for {@link CipherSpi} providing Android KeyStore backed RSA encryption/decryption.
 *
 * @unknown 
 */
abstract class AndroidKeyStoreRSACipherSpi extends android.security.keystore.AndroidKeyStoreCipherSpiBase {
    /**
     * Raw RSA cipher without any padding.
     */
    public static final class NoPadding extends android.security.keystore.AndroidKeyStoreRSACipherSpi {
        public NoPadding() {
            super(android.security.keymaster.KeymasterDefs.KM_PAD_NONE);
        }

        @java.lang.Override
        protected boolean adjustConfigForEncryptingWithPrivateKey() {
            // RSA encryption with no padding using private key is a way to implement raw RSA
            // signatures which JCA does not expose via Signature. We thus have to support this.
            setKeymasterPurposeOverride(android.security.keymaster.KeymasterDefs.KM_PURPOSE_SIGN);
            return true;
        }

        @java.lang.Override
        protected void initAlgorithmSpecificParameters() throws java.security.InvalidKeyException {
        }

        @java.lang.Override
        protected void initAlgorithmSpecificParameters(@android.annotation.Nullable
        java.security.spec.AlgorithmParameterSpec params) throws java.security.InvalidAlgorithmParameterException {
            if (params != null) {
                throw new java.security.InvalidAlgorithmParameterException(("Unexpected parameters: " + params) + ". No parameters supported");
            }
        }

        @java.lang.Override
        protected void initAlgorithmSpecificParameters(@android.annotation.Nullable
        java.security.AlgorithmParameters params) throws java.security.InvalidAlgorithmParameterException {
            if (params != null) {
                throw new java.security.InvalidAlgorithmParameterException(("Unexpected parameters: " + params) + ". No parameters supported");
            }
        }

        @java.lang.Override
        protected java.security.AlgorithmParameters engineGetParameters() {
            return null;
        }

        @java.lang.Override
        protected final int getAdditionalEntropyAmountForBegin() {
            return 0;
        }

        @java.lang.Override
        protected final int getAdditionalEntropyAmountForFinish() {
            return 0;
        }
    }

    /**
     * RSA cipher with PKCS#1 v1.5 encryption padding.
     */
    public static final class PKCS1Padding extends android.security.keystore.AndroidKeyStoreRSACipherSpi {
        public PKCS1Padding() {
            super(android.security.keymaster.KeymasterDefs.KM_PAD_RSA_PKCS1_1_5_ENCRYPT);
        }

        @java.lang.Override
        protected boolean adjustConfigForEncryptingWithPrivateKey() {
            // RSA encryption with PCKS#1 padding using private key is a way to implement RSA
            // signatures with PKCS#1 padding. We have to support this for legacy reasons.
            setKeymasterPurposeOverride(android.security.keymaster.KeymasterDefs.KM_PURPOSE_SIGN);
            setKeymasterPaddingOverride(android.security.keymaster.KeymasterDefs.KM_PAD_RSA_PKCS1_1_5_SIGN);
            return true;
        }

        @java.lang.Override
        protected void initAlgorithmSpecificParameters() throws java.security.InvalidKeyException {
        }

        @java.lang.Override
        protected void initAlgorithmSpecificParameters(@android.annotation.Nullable
        java.security.spec.AlgorithmParameterSpec params) throws java.security.InvalidAlgorithmParameterException {
            if (params != null) {
                throw new java.security.InvalidAlgorithmParameterException(("Unexpected parameters: " + params) + ". No parameters supported");
            }
        }

        @java.lang.Override
        protected void initAlgorithmSpecificParameters(@android.annotation.Nullable
        java.security.AlgorithmParameters params) throws java.security.InvalidAlgorithmParameterException {
            if (params != null) {
                throw new java.security.InvalidAlgorithmParameterException(("Unexpected parameters: " + params) + ". No parameters supported");
            }
        }

        @java.lang.Override
        protected java.security.AlgorithmParameters engineGetParameters() {
            return null;
        }

        @java.lang.Override
        protected final int getAdditionalEntropyAmountForBegin() {
            return 0;
        }

        @java.lang.Override
        protected final int getAdditionalEntropyAmountForFinish() {
            return isEncrypting() ? getModulusSizeBytes() : 0;
        }
    }

    /**
     * RSA cipher with OAEP encryption padding. Only SHA-1 based MGF1 is supported as MGF.
     */
    static abstract class OAEPWithMGF1Padding extends android.security.keystore.AndroidKeyStoreRSACipherSpi {
        private static final java.lang.String MGF_ALGORITGM_MGF1 = "MGF1";

        private int mKeymasterDigest = -1;

        private int mDigestOutputSizeBytes;

        OAEPWithMGF1Padding(int keymasterDigest) {
            super(android.security.keymaster.KeymasterDefs.KM_PAD_RSA_OAEP);
            mKeymasterDigest = keymasterDigest;
            mDigestOutputSizeBytes = (android.security.keystore.KeymasterUtils.getDigestOutputSizeBits(keymasterDigest) + 7) / 8;
        }

        @java.lang.Override
        protected final void initAlgorithmSpecificParameters() throws java.security.InvalidKeyException {
        }

        @java.lang.Override
        protected final void initAlgorithmSpecificParameters(@android.annotation.Nullable
        java.security.spec.AlgorithmParameterSpec params) throws java.security.InvalidAlgorithmParameterException {
            if (params == null) {
                return;
            }
            if (!(params instanceof javax.crypto.spec.OAEPParameterSpec)) {
                throw new java.security.InvalidAlgorithmParameterException(("Unsupported parameter spec: " + params) + ". Only OAEPParameterSpec supported");
            }
            javax.crypto.spec.OAEPParameterSpec spec = ((javax.crypto.spec.OAEPParameterSpec) (params));
            if (!android.security.keystore.AndroidKeyStoreRSACipherSpi.OAEPWithMGF1Padding.MGF_ALGORITGM_MGF1.equalsIgnoreCase(spec.getMGFAlgorithm())) {
                throw new java.security.InvalidAlgorithmParameterException(((("Unsupported MGF: " + spec.getMGFAlgorithm()) + ". Only ") + android.security.keystore.AndroidKeyStoreRSACipherSpi.OAEPWithMGF1Padding.MGF_ALGORITGM_MGF1) + " supported");
            }
            java.lang.String jcaDigest = spec.getDigestAlgorithm();
            int keymasterDigest;
            try {
                keymasterDigest = android.security.keystore.KeyProperties.Digest.toKeymaster(jcaDigest);
            } catch (java.lang.IllegalArgumentException e) {
                throw new java.security.InvalidAlgorithmParameterException("Unsupported digest: " + jcaDigest, e);
            }
            switch (keymasterDigest) {
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA1 :
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_224 :
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_256 :
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_384 :
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_512 :
                    // Permitted.
                    break;
                default :
                    throw new java.security.InvalidAlgorithmParameterException("Unsupported digest: " + jcaDigest);
            }
            java.security.spec.AlgorithmParameterSpec mgfParams = spec.getMGFParameters();
            if (mgfParams == null) {
                throw new java.security.InvalidAlgorithmParameterException("MGF parameters must be provided");
            }
            // Check whether MGF parameters match the OAEPParameterSpec
            if (!(mgfParams instanceof java.security.spec.MGF1ParameterSpec)) {
                throw new java.security.InvalidAlgorithmParameterException((("Unsupported MGF parameters" + ": ") + mgfParams) + ". Only MGF1ParameterSpec supported");
            }
            java.security.spec.MGF1ParameterSpec mgfSpec = ((java.security.spec.MGF1ParameterSpec) (mgfParams));
            java.lang.String mgf1JcaDigest = mgfSpec.getDigestAlgorithm();
            if (!android.security.keystore.KeyProperties.DIGEST_SHA1.equalsIgnoreCase(mgf1JcaDigest)) {
                throw new java.security.InvalidAlgorithmParameterException(((("Unsupported MGF1 digest: " + mgf1JcaDigest) + ". Only ") + android.security.keystore.KeyProperties.DIGEST_SHA1) + " supported");
            }
            javax.crypto.spec.PSource pSource = spec.getPSource();
            if (!(pSource instanceof javax.crypto.spec.PSource.PSpecified)) {
                throw new java.security.InvalidAlgorithmParameterException(("Unsupported source of encoding input P: " + pSource) + ". Only pSpecifiedEmpty (PSource.PSpecified.DEFAULT) supported");
            }
            javax.crypto.spec.PSource.PSpecified pSourceSpecified = ((javax.crypto.spec.PSource.PSpecified) (pSource));
            byte[] pSourceValue = pSourceSpecified.getValue();
            if ((pSourceValue != null) && (pSourceValue.length > 0)) {
                throw new java.security.InvalidAlgorithmParameterException(("Unsupported source of encoding input P: " + pSource) + ". Only pSpecifiedEmpty (PSource.PSpecified.DEFAULT) supported");
            }
            mKeymasterDigest = keymasterDigest;
            mDigestOutputSizeBytes = (android.security.keystore.KeymasterUtils.getDigestOutputSizeBits(keymasterDigest) + 7) / 8;
        }

        @java.lang.Override
        protected final void initAlgorithmSpecificParameters(@android.annotation.Nullable
        java.security.AlgorithmParameters params) throws java.security.InvalidAlgorithmParameterException {
            if (params == null) {
                return;
            }
            javax.crypto.spec.OAEPParameterSpec spec;
            try {
                spec = params.getParameterSpec(javax.crypto.spec.OAEPParameterSpec.class);
            } catch (java.security.spec.InvalidParameterSpecException e) {
                throw new java.security.InvalidAlgorithmParameterException(("OAEP parameters required" + ", but not found in parameters: ") + params, e);
            }
            if (spec == null) {
                throw new java.security.InvalidAlgorithmParameterException(("OAEP parameters required" + ", but not provided in parameters: ") + params);
            }
            initAlgorithmSpecificParameters(spec);
        }

        @java.lang.Override
        protected final java.security.AlgorithmParameters engineGetParameters() {
            javax.crypto.spec.OAEPParameterSpec spec = new javax.crypto.spec.OAEPParameterSpec(android.security.keystore.KeyProperties.Digest.fromKeymaster(mKeymasterDigest), android.security.keystore.AndroidKeyStoreRSACipherSpi.OAEPWithMGF1Padding.MGF_ALGORITGM_MGF1, java.security.spec.MGF1ParameterSpec.SHA1, javax.crypto.spec.PSource.PSpecified.DEFAULT);
            try {
                java.security.AlgorithmParameters params = java.security.AlgorithmParameters.getInstance("OAEP");
                params.init(spec);
                return params;
            } catch (java.security.NoSuchAlgorithmException e) {
                throw new java.security.ProviderException("Failed to obtain OAEP AlgorithmParameters", e);
            } catch (java.security.spec.InvalidParameterSpecException e) {
                throw new java.security.ProviderException("Failed to initialize OAEP AlgorithmParameters with an IV", e);
            }
        }

        @java.lang.Override
        protected final void addAlgorithmSpecificParametersToBegin(android.security.keymaster.KeymasterArguments keymasterArgs) {
            super.addAlgorithmSpecificParametersToBegin(keymasterArgs);
            keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_DIGEST, mKeymasterDigest);
        }

        @java.lang.Override
        protected final void loadAlgorithmSpecificParametersFromBeginResult(@android.annotation.NonNull
        android.security.keymaster.KeymasterArguments keymasterArgs) {
            super.loadAlgorithmSpecificParametersFromBeginResult(keymasterArgs);
        }

        @java.lang.Override
        protected final int getAdditionalEntropyAmountForBegin() {
            return 0;
        }

        @java.lang.Override
        protected final int getAdditionalEntropyAmountForFinish() {
            return isEncrypting() ? mDigestOutputSizeBytes : 0;
        }
    }

    public static class OAEPWithSHA1AndMGF1Padding extends android.security.keystore.AndroidKeyStoreRSACipherSpi.OAEPWithMGF1Padding {
        public OAEPWithSHA1AndMGF1Padding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA1);
        }
    }

    public static class OAEPWithSHA224AndMGF1Padding extends android.security.keystore.AndroidKeyStoreRSACipherSpi.OAEPWithMGF1Padding {
        public OAEPWithSHA224AndMGF1Padding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_224);
        }
    }

    public static class OAEPWithSHA256AndMGF1Padding extends android.security.keystore.AndroidKeyStoreRSACipherSpi.OAEPWithMGF1Padding {
        public OAEPWithSHA256AndMGF1Padding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_256);
        }
    }

    public static class OAEPWithSHA384AndMGF1Padding extends android.security.keystore.AndroidKeyStoreRSACipherSpi.OAEPWithMGF1Padding {
        public OAEPWithSHA384AndMGF1Padding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_384);
        }
    }

    public static class OAEPWithSHA512AndMGF1Padding extends android.security.keystore.AndroidKeyStoreRSACipherSpi.OAEPWithMGF1Padding {
        public OAEPWithSHA512AndMGF1Padding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_512);
        }
    }

    private final int mKeymasterPadding;

    private int mKeymasterPaddingOverride;

    private int mModulusSizeBytes = -1;

    AndroidKeyStoreRSACipherSpi(int keymasterPadding) {
        mKeymasterPadding = keymasterPadding;
    }

    @java.lang.Override
    protected final void initKey(int opmode, java.security.Key key) throws java.security.InvalidKeyException {
        if (key == null) {
            throw new java.security.InvalidKeyException("Unsupported key: null");
        }
        if (!android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA.equalsIgnoreCase(key.getAlgorithm())) {
            throw new java.security.InvalidKeyException(((("Unsupported key algorithm: " + key.getAlgorithm()) + ". Only ") + android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA) + " supported");
        }
        android.security.keystore.AndroidKeyStoreKey keystoreKey;
        if (key instanceof android.security.keystore.AndroidKeyStorePrivateKey) {
            keystoreKey = ((android.security.keystore.AndroidKeyStoreKey) (key));
        } else
            if (key instanceof android.security.keystore.AndroidKeyStorePublicKey) {
                keystoreKey = ((android.security.keystore.AndroidKeyStoreKey) (key));
            } else {
                throw new java.security.InvalidKeyException("Unsupported key type: " + key);
            }

        if (keystoreKey instanceof java.security.PrivateKey) {
            // Private key
            switch (opmode) {
                case javax.crypto.Cipher.DECRYPT_MODE :
                case javax.crypto.Cipher.UNWRAP_MODE :
                    // Permitted
                    break;
                case javax.crypto.Cipher.ENCRYPT_MODE :
                case javax.crypto.Cipher.WRAP_MODE :
                    if (!adjustConfigForEncryptingWithPrivateKey()) {
                        throw new java.security.InvalidKeyException(((("RSA private keys cannot be used with " + android.security.keystore.AndroidKeyStoreCipherSpiBase.opmodeToString(opmode)) + " and padding ") + android.security.keystore.KeyProperties.EncryptionPadding.fromKeymaster(mKeymasterPadding)) + ". Only RSA public keys supported for this mode");
                    }
                    break;
                default :
                    throw new java.security.InvalidKeyException("RSA private keys cannot be used with opmode: " + opmode);
            }
        } else {
            // Public key
            switch (opmode) {
                case javax.crypto.Cipher.ENCRYPT_MODE :
                case javax.crypto.Cipher.WRAP_MODE :
                    // Permitted
                    break;
                case javax.crypto.Cipher.DECRYPT_MODE :
                case javax.crypto.Cipher.UNWRAP_MODE :
                    throw new java.security.InvalidKeyException(((("RSA public keys cannot be used with " + android.security.keystore.AndroidKeyStoreCipherSpiBase.opmodeToString(opmode)) + " and padding ") + android.security.keystore.KeyProperties.EncryptionPadding.fromKeymaster(mKeymasterPadding)) + ". Only RSA private keys supported for this opmode.");
                    // break;
                default :
                    throw new java.security.InvalidKeyException("RSA public keys cannot be used with " + android.security.keystore.AndroidKeyStoreCipherSpiBase.opmodeToString(opmode));
            }
        }
        android.security.keymaster.KeyCharacteristics keyCharacteristics = new android.security.keymaster.KeyCharacteristics();
        int errorCode = getKeyStore().getKeyCharacteristics(keystoreKey.getAlias(), null, null, keystoreKey.getUid(), keyCharacteristics);
        if (errorCode != android.security.KeyStore.NO_ERROR) {
            throw getKeyStore().getInvalidKeyException(keystoreKey.getAlias(), keystoreKey.getUid(), errorCode);
        }
        long keySizeBits = keyCharacteristics.getUnsignedInt(android.security.keymaster.KeymasterDefs.KM_TAG_KEY_SIZE, -1);
        if (keySizeBits == (-1)) {
            throw new java.security.InvalidKeyException("Size of key not known");
        } else
            if (keySizeBits > java.lang.Integer.MAX_VALUE) {
                throw new java.security.InvalidKeyException(("Key too large: " + keySizeBits) + " bits");
            }

        mModulusSizeBytes = ((int) ((keySizeBits + 7) / 8));
        setKey(keystoreKey);
    }

    /**
     * Adjusts the configuration of this cipher for encrypting using the private key.
     *
     * <p>The default implementation does nothing and refuses to adjust the configuration.
     *
     * @return {@code true} if the configuration has been adjusted, {@code false} if encrypting
    using private key is not permitted for this cipher.
     */
    protected boolean adjustConfigForEncryptingWithPrivateKey() {
        return false;
    }

    @java.lang.Override
    protected final void resetAll() {
        mModulusSizeBytes = -1;
        mKeymasterPaddingOverride = -1;
        super.resetAll();
    }

    @java.lang.Override
    protected final void resetWhilePreservingInitState() {
        super.resetWhilePreservingInitState();
    }

    @java.lang.Override
    protected void addAlgorithmSpecificParametersToBegin(@android.annotation.NonNull
    android.security.keymaster.KeymasterArguments keymasterArgs) {
        keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_ALGORITHM, android.security.keymaster.KeymasterDefs.KM_ALGORITHM_RSA);
        int keymasterPadding = getKeymasterPaddingOverride();
        if (keymasterPadding == (-1)) {
            keymasterPadding = mKeymasterPadding;
        }
        keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_PADDING, keymasterPadding);
        int purposeOverride = getKeymasterPurposeOverride();
        if ((purposeOverride != (-1)) && ((purposeOverride == android.security.keymaster.KeymasterDefs.KM_PURPOSE_SIGN) || (purposeOverride == android.security.keymaster.KeymasterDefs.KM_PURPOSE_VERIFY))) {
            // Keymaster sign/verify requires digest to be specified. For raw sign/verify it's NONE.
            keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_DIGEST, android.security.keymaster.KeymasterDefs.KM_DIGEST_NONE);
        }
    }

    @java.lang.Override
    protected void loadAlgorithmSpecificParametersFromBeginResult(@android.annotation.NonNull
    android.security.keymaster.KeymasterArguments keymasterArgs) {
    }

    @java.lang.Override
    protected final int engineGetBlockSize() {
        // Not a block cipher, according to the RI
        return 0;
    }

    @java.lang.Override
    protected final byte[] engineGetIV() {
        // IV never used
        return null;
    }

    @java.lang.Override
    protected final int engineGetOutputSize(int inputLen) {
        return getModulusSizeBytes();
    }

    protected final int getModulusSizeBytes() {
        if (mModulusSizeBytes == (-1)) {
            throw new java.lang.IllegalStateException("Not initialized");
        }
        return mModulusSizeBytes;
    }

    /**
     * Overrides the default padding of the crypto operation.
     */
    protected final void setKeymasterPaddingOverride(int keymasterPadding) {
        mKeymasterPaddingOverride = keymasterPadding;
    }

    protected final int getKeymasterPaddingOverride() {
        return mKeymasterPaddingOverride;
    }
}

