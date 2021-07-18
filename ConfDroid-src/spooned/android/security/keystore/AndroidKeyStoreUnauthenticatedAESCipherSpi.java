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
 * Base class for Android Keystore unauthenticated AES {@link CipherSpi} implementations.
 *
 * @unknown 
 */
class AndroidKeyStoreUnauthenticatedAESCipherSpi extends android.security.keystore.AndroidKeyStoreCipherSpiBase {
    static abstract class ECB extends android.security.keystore.AndroidKeyStoreUnauthenticatedAESCipherSpi {
        protected ECB(int keymasterPadding) {
            super(android.security.keymaster.KeymasterDefs.KM_MODE_ECB, keymasterPadding, false);
        }

        public static class NoPadding extends android.security.keystore.AndroidKeyStoreUnauthenticatedAESCipherSpi.ECB {
            public NoPadding() {
                super(android.security.keymaster.KeymasterDefs.KM_PAD_NONE);
            }
        }

        public static class PKCS7Padding extends android.security.keystore.AndroidKeyStoreUnauthenticatedAESCipherSpi.ECB {
            public PKCS7Padding() {
                super(android.security.keymaster.KeymasterDefs.KM_PAD_PKCS7);
            }
        }
    }

    static abstract class CBC extends android.security.keystore.AndroidKeyStoreUnauthenticatedAESCipherSpi {
        protected CBC(int keymasterPadding) {
            super(android.security.keymaster.KeymasterDefs.KM_MODE_CBC, keymasterPadding, true);
        }

        public static class NoPadding extends android.security.keystore.AndroidKeyStoreUnauthenticatedAESCipherSpi.CBC {
            public NoPadding() {
                super(android.security.keymaster.KeymasterDefs.KM_PAD_NONE);
            }
        }

        public static class PKCS7Padding extends android.security.keystore.AndroidKeyStoreUnauthenticatedAESCipherSpi.CBC {
            public PKCS7Padding() {
                super(android.security.keymaster.KeymasterDefs.KM_PAD_PKCS7);
            }
        }
    }

    static abstract class CTR extends android.security.keystore.AndroidKeyStoreUnauthenticatedAESCipherSpi {
        protected CTR(int keymasterPadding) {
            super(android.security.keymaster.KeymasterDefs.KM_MODE_CTR, keymasterPadding, true);
        }

        public static class NoPadding extends android.security.keystore.AndroidKeyStoreUnauthenticatedAESCipherSpi.CTR {
            public NoPadding() {
                super(android.security.keymaster.KeymasterDefs.KM_PAD_NONE);
            }
        }
    }

    private static final int BLOCK_SIZE_BYTES = 16;

    private final int mKeymasterBlockMode;

    private final int mKeymasterPadding;

    /**
     * Whether this transformation requires an IV.
     */
    private final boolean mIvRequired;

    private byte[] mIv;

    /**
     * Whether the current {@code #mIv} has been used by the underlying crypto operation.
     */
    private boolean mIvHasBeenUsed;

    AndroidKeyStoreUnauthenticatedAESCipherSpi(int keymasterBlockMode, int keymasterPadding, boolean ivRequired) {
        mKeymasterBlockMode = keymasterBlockMode;
        mKeymasterPadding = keymasterPadding;
        mIvRequired = ivRequired;
    }

    @java.lang.Override
    protected final void resetAll() {
        mIv = null;
        mIvHasBeenUsed = false;
        super.resetAll();
    }

    @java.lang.Override
    protected final void resetWhilePreservingInitState() {
        super.resetWhilePreservingInitState();
    }

    @java.lang.Override
    protected final void initKey(int opmode, java.security.Key key) throws java.security.InvalidKeyException {
        if (!(key instanceof android.security.keystore.AndroidKeyStoreSecretKey)) {
            throw new java.security.InvalidKeyException("Unsupported key: " + (key != null ? key.getClass().getName() : "null"));
        }
        if (!android.security.keystore.KeyProperties.KEY_ALGORITHM_AES.equalsIgnoreCase(key.getAlgorithm())) {
            throw new java.security.InvalidKeyException(((("Unsupported key algorithm: " + key.getAlgorithm()) + ". Only ") + android.security.keystore.KeyProperties.KEY_ALGORITHM_AES) + " supported");
        }
        setKey(((android.security.keystore.AndroidKeyStoreSecretKey) (key)));
    }

    @java.lang.Override
    protected final void initAlgorithmSpecificParameters() throws java.security.InvalidKeyException {
        if (!mIvRequired) {
            return;
        }
        // IV is used
        if (!isEncrypting()) {
            throw new java.security.InvalidKeyException("IV required when decrypting" + ". Use IvParameterSpec or AlgorithmParameters to provide it.");
        }
    }

    @java.lang.Override
    protected final void initAlgorithmSpecificParameters(java.security.spec.AlgorithmParameterSpec params) throws java.security.InvalidAlgorithmParameterException {
        if (!mIvRequired) {
            if (params != null) {
                throw new java.security.InvalidAlgorithmParameterException("Unsupported parameters: " + params);
            }
            return;
        }
        // IV is used
        if (params == null) {
            if (!isEncrypting()) {
                // IV must be provided by the caller
                throw new java.security.InvalidAlgorithmParameterException("IvParameterSpec must be provided when decrypting");
            }
            return;
        }
        if (!(params instanceof javax.crypto.spec.IvParameterSpec)) {
            throw new java.security.InvalidAlgorithmParameterException("Only IvParameterSpec supported");
        }
        mIv = ((javax.crypto.spec.IvParameterSpec) (params)).getIV();
        if (mIv == null) {
            throw new java.security.InvalidAlgorithmParameterException("Null IV in IvParameterSpec");
        }
    }

    @java.lang.Override
    protected final void initAlgorithmSpecificParameters(java.security.AlgorithmParameters params) throws java.security.InvalidAlgorithmParameterException {
        if (!mIvRequired) {
            if (params != null) {
                throw new java.security.InvalidAlgorithmParameterException("Unsupported parameters: " + params);
            }
            return;
        }
        // IV is used
        if (params == null) {
            if (!isEncrypting()) {
                // IV must be provided by the caller
                throw new java.security.InvalidAlgorithmParameterException("IV required when decrypting" + ". Use IvParameterSpec or AlgorithmParameters to provide it.");
            }
            return;
        }
        if (!"AES".equalsIgnoreCase(params.getAlgorithm())) {
            throw new java.security.InvalidAlgorithmParameterException(("Unsupported AlgorithmParameters algorithm: " + params.getAlgorithm()) + ". Supported: AES");
        }
        javax.crypto.spec.IvParameterSpec ivSpec;
        try {
            ivSpec = params.getParameterSpec(javax.crypto.spec.IvParameterSpec.class);
        } catch (java.security.spec.InvalidParameterSpecException e) {
            if (!isEncrypting()) {
                // IV must be provided by the caller
                throw new java.security.InvalidAlgorithmParameterException(("IV required when decrypting" + ", but not found in parameters: ") + params, e);
            }
            mIv = null;
            return;
        }
        mIv = ivSpec.getIV();
        if (mIv == null) {
            throw new java.security.InvalidAlgorithmParameterException("Null IV in AlgorithmParameters");
        }
    }

    @java.lang.Override
    protected final int getAdditionalEntropyAmountForBegin() {
        if ((mIvRequired && (mIv == null)) && isEncrypting()) {
            // IV will need to be generated
            return android.security.keystore.AndroidKeyStoreUnauthenticatedAESCipherSpi.BLOCK_SIZE_BYTES;
        }
        return 0;
    }

    @java.lang.Override
    protected final int getAdditionalEntropyAmountForFinish() {
        return 0;
    }

    @java.lang.Override
    protected final void addAlgorithmSpecificParametersToBegin(@android.annotation.NonNull
    android.security.keymaster.KeymasterArguments keymasterArgs) {
        if ((isEncrypting() && mIvRequired) && mIvHasBeenUsed) {
            // IV is being reused for encryption: this violates security best practices.
            throw new java.lang.IllegalStateException("IV has already been used. Reusing IV in encryption mode violates security best" + " practices.");
        }
        keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_ALGORITHM, android.security.keymaster.KeymasterDefs.KM_ALGORITHM_AES);
        keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_BLOCK_MODE, mKeymasterBlockMode);
        keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_PADDING, mKeymasterPadding);
        if (mIvRequired && (mIv != null)) {
            keymasterArgs.addBytes(android.security.keymaster.KeymasterDefs.KM_TAG_NONCE, mIv);
        }
    }

    @java.lang.Override
    protected final void loadAlgorithmSpecificParametersFromBeginResult(@android.annotation.NonNull
    android.security.keymaster.KeymasterArguments keymasterArgs) {
        mIvHasBeenUsed = true;
        // NOTE: Keymaster doesn't always return an IV, even if it's used.
        byte[] returnedIv = keymasterArgs.getBytes(android.security.keymaster.KeymasterDefs.KM_TAG_NONCE, null);
        if ((returnedIv != null) && (returnedIv.length == 0)) {
            returnedIv = null;
        }
        if (mIvRequired) {
            if (mIv == null) {
                mIv = returnedIv;
            } else
                if ((returnedIv != null) && (!java.util.Arrays.equals(returnedIv, mIv))) {
                    throw new java.security.ProviderException("IV in use differs from provided IV");
                }

        } else {
            if (returnedIv != null) {
                throw new java.security.ProviderException("IV in use despite IV not being used by this transformation");
            }
        }
    }

    @java.lang.Override
    protected final int engineGetBlockSize() {
        return android.security.keystore.AndroidKeyStoreUnauthenticatedAESCipherSpi.BLOCK_SIZE_BYTES;
    }

    @java.lang.Override
    protected final int engineGetOutputSize(int inputLen) {
        return inputLen + (3 * android.security.keystore.AndroidKeyStoreUnauthenticatedAESCipherSpi.BLOCK_SIZE_BYTES);
    }

    @java.lang.Override
    protected final byte[] engineGetIV() {
        return android.security.keystore.ArrayUtils.cloneIfNotEmpty(mIv);
    }

    @android.annotation.Nullable
    @java.lang.Override
    protected final java.security.AlgorithmParameters engineGetParameters() {
        if (!mIvRequired) {
            return null;
        }
        if ((mIv != null) && (mIv.length > 0)) {
            try {
                java.security.AlgorithmParameters params = java.security.AlgorithmParameters.getInstance("AES");
                params.init(new javax.crypto.spec.IvParameterSpec(mIv));
                return params;
            } catch (java.security.NoSuchAlgorithmException e) {
                throw new java.security.ProviderException("Failed to obtain AES AlgorithmParameters", e);
            } catch (java.security.spec.InvalidParameterSpecException e) {
                throw new java.security.ProviderException("Failed to initialize AES AlgorithmParameters with an IV", e);
            }
        }
        return null;
    }
}

