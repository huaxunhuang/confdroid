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
 * Base class for Android Keystore authenticated AES {@link CipherSpi} implementations.
 *
 * @unknown 
 */
abstract class AndroidKeyStoreAuthenticatedAESCipherSpi extends android.security.keystore.AndroidKeyStoreCipherSpiBase {
    static abstract class GCM extends android.security.keystore.AndroidKeyStoreAuthenticatedAESCipherSpi {
        static final int MIN_SUPPORTED_TAG_LENGTH_BITS = 96;

        private static final int MAX_SUPPORTED_TAG_LENGTH_BITS = 128;

        private static final int DEFAULT_TAG_LENGTH_BITS = 128;

        private static final int IV_LENGTH_BYTES = 12;

        private int mTagLengthBits = android.security.keystore.AndroidKeyStoreAuthenticatedAESCipherSpi.GCM.DEFAULT_TAG_LENGTH_BITS;

        GCM(int keymasterPadding) {
            super(android.security.keymaster.KeymasterDefs.KM_MODE_GCM, keymasterPadding);
        }

        @java.lang.Override
        protected final void resetAll() {
            mTagLengthBits = android.security.keystore.AndroidKeyStoreAuthenticatedAESCipherSpi.GCM.DEFAULT_TAG_LENGTH_BITS;
            super.resetAll();
        }

        @java.lang.Override
        protected final void resetWhilePreservingInitState() {
            super.resetWhilePreservingInitState();
        }

        @java.lang.Override
        protected final void initAlgorithmSpecificParameters() throws java.security.InvalidKeyException {
            if (!isEncrypting()) {
                throw new java.security.InvalidKeyException("IV required when decrypting" + ". Use IvParameterSpec or AlgorithmParameters to provide it.");
            }
        }

        @java.lang.Override
        protected final void initAlgorithmSpecificParameters(java.security.spec.AlgorithmParameterSpec params) throws java.security.InvalidAlgorithmParameterException {
            // IV is used
            if (params == null) {
                if (!isEncrypting()) {
                    // IV must be provided by the caller
                    throw new java.security.InvalidAlgorithmParameterException("GCMParameterSpec must be provided when decrypting");
                }
                return;
            }
            if (!(params instanceof javax.crypto.spec.GCMParameterSpec)) {
                throw new java.security.InvalidAlgorithmParameterException("Only GCMParameterSpec supported");
            }
            javax.crypto.spec.GCMParameterSpec spec = ((javax.crypto.spec.GCMParameterSpec) (params));
            byte[] iv = spec.getIV();
            if (iv == null) {
                throw new java.security.InvalidAlgorithmParameterException("Null IV in GCMParameterSpec");
            } else
                if (iv.length != android.security.keystore.AndroidKeyStoreAuthenticatedAESCipherSpi.GCM.IV_LENGTH_BYTES) {
                    throw new java.security.InvalidAlgorithmParameterException(((("Unsupported IV length: " + iv.length) + " bytes. Only ") + android.security.keystore.AndroidKeyStoreAuthenticatedAESCipherSpi.GCM.IV_LENGTH_BYTES) + " bytes long IV supported");
                }

            int tagLengthBits = spec.getTLen();
            if (((tagLengthBits < android.security.keystore.AndroidKeyStoreAuthenticatedAESCipherSpi.GCM.MIN_SUPPORTED_TAG_LENGTH_BITS) || (tagLengthBits > android.security.keystore.AndroidKeyStoreAuthenticatedAESCipherSpi.GCM.MAX_SUPPORTED_TAG_LENGTH_BITS)) || ((tagLengthBits % 8) != 0)) {
                throw new java.security.InvalidAlgorithmParameterException((("Unsupported tag length: " + tagLengthBits) + " bits") + ". Supported lengths: 96, 104, 112, 120, 128");
            }
            setIv(iv);
            mTagLengthBits = tagLengthBits;
        }

        @java.lang.Override
        protected final void initAlgorithmSpecificParameters(java.security.AlgorithmParameters params) throws java.security.InvalidAlgorithmParameterException {
            if (params == null) {
                if (!isEncrypting()) {
                    // IV must be provided by the caller
                    throw new java.security.InvalidAlgorithmParameterException("IV required when decrypting" + ". Use GCMParameterSpec or GCM AlgorithmParameters to provide it.");
                }
                return;
            }
            if (!"GCM".equalsIgnoreCase(params.getAlgorithm())) {
                throw new java.security.InvalidAlgorithmParameterException(("Unsupported AlgorithmParameters algorithm: " + params.getAlgorithm()) + ". Supported: GCM");
            }
            javax.crypto.spec.GCMParameterSpec spec;
            try {
                spec = params.getParameterSpec(javax.crypto.spec.GCMParameterSpec.class);
            } catch (java.security.spec.InvalidParameterSpecException e) {
                if (!isEncrypting()) {
                    // IV must be provided by the caller
                    throw new java.security.InvalidAlgorithmParameterException(("IV and tag length required when" + " decrypting, but not found in parameters: ") + params, e);
                }
                setIv(null);
                return;
            }
            initAlgorithmSpecificParameters(spec);
        }

        @android.annotation.Nullable
        @java.lang.Override
        protected final java.security.AlgorithmParameters engineGetParameters() {
            byte[] iv = getIv();
            if ((iv != null) && (iv.length > 0)) {
                try {
                    java.security.AlgorithmParameters params = java.security.AlgorithmParameters.getInstance("GCM");
                    params.init(new javax.crypto.spec.GCMParameterSpec(mTagLengthBits, iv));
                    return params;
                } catch (java.security.NoSuchAlgorithmException e) {
                    throw new java.security.ProviderException("Failed to obtain GCM AlgorithmParameters", e);
                } catch (java.security.spec.InvalidParameterSpecException e) {
                    throw new java.security.ProviderException("Failed to initialize GCM AlgorithmParameters", e);
                }
            }
            return null;
        }

        @android.annotation.NonNull
        @java.lang.Override
        protected android.security.keystore.KeyStoreCryptoOperationStreamer createMainDataStreamer(android.security.KeyStore keyStore, android.os.IBinder operationToken) {
            android.security.keystore.KeyStoreCryptoOperationStreamer streamer = new android.security.keystore.KeyStoreCryptoOperationChunkedStreamer(new android.security.keystore.KeyStoreCryptoOperationChunkedStreamer.MainDataStream(keyStore, operationToken));
            if (isEncrypting()) {
                return streamer;
            } else {
                // When decrypting, to avoid leaking unauthenticated plaintext, do not return any
                // plaintext before ciphertext is authenticated by KeyStore.finish.
                return new android.security.keystore.AndroidKeyStoreAuthenticatedAESCipherSpi.BufferAllOutputUntilDoFinalStreamer(streamer);
            }
        }

        @android.annotation.NonNull
        @java.lang.Override
        protected final android.security.keystore.KeyStoreCryptoOperationStreamer createAdditionalAuthenticationDataStreamer(android.security.KeyStore keyStore, android.os.IBinder operationToken) {
            return new android.security.keystore.KeyStoreCryptoOperationChunkedStreamer(new android.security.keystore.AndroidKeyStoreAuthenticatedAESCipherSpi.AdditionalAuthenticationDataStream(keyStore, operationToken));
        }

        @java.lang.Override
        protected final int getAdditionalEntropyAmountForBegin() {
            if ((getIv() == null) && isEncrypting()) {
                // IV will need to be generated
                return android.security.keystore.AndroidKeyStoreAuthenticatedAESCipherSpi.GCM.IV_LENGTH_BYTES;
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
            super.addAlgorithmSpecificParametersToBegin(keymasterArgs);
            keymasterArgs.addUnsignedInt(android.security.keymaster.KeymasterDefs.KM_TAG_MAC_LENGTH, mTagLengthBits);
        }

        protected final int getTagLengthBits() {
            return mTagLengthBits;
        }

        public static final class NoPadding extends android.security.keystore.AndroidKeyStoreAuthenticatedAESCipherSpi.GCM {
            public NoPadding() {
                super(android.security.keymaster.KeymasterDefs.KM_PAD_NONE);
            }

            @java.lang.Override
            protected final int engineGetOutputSize(int inputLen) {
                int tagLengthBytes = (getTagLengthBits() + 7) / 8;
                long result;
                if (isEncrypting()) {
                    result = ((getConsumedInputSizeBytes() - getProducedOutputSizeBytes()) + inputLen) + tagLengthBytes;
                } else {
                    result = ((getConsumedInputSizeBytes() - getProducedOutputSizeBytes()) + inputLen) - tagLengthBytes;
                }
                if (result < 0) {
                    return 0;
                } else
                    if (result > java.lang.Integer.MAX_VALUE) {
                        return java.lang.Integer.MAX_VALUE;
                    }

                return ((int) (result));
            }
        }
    }

    private static final int BLOCK_SIZE_BYTES = 16;

    private final int mKeymasterBlockMode;

    private final int mKeymasterPadding;

    private byte[] mIv;

    /**
     * Whether the current {@code #mIv} has been used by the underlying crypto operation.
     */
    private boolean mIvHasBeenUsed;

    AndroidKeyStoreAuthenticatedAESCipherSpi(int keymasterBlockMode, int keymasterPadding) {
        mKeymasterBlockMode = keymasterBlockMode;
        mKeymasterPadding = keymasterPadding;
    }

    @java.lang.Override
    protected void resetAll() {
        mIv = null;
        mIvHasBeenUsed = false;
        super.resetAll();
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
    protected void addAlgorithmSpecificParametersToBegin(@android.annotation.NonNull
    android.security.keymaster.KeymasterArguments keymasterArgs) {
        if (isEncrypting() && mIvHasBeenUsed) {
            // IV is being reused for encryption: this violates security best practices.
            throw new java.lang.IllegalStateException("IV has already been used. Reusing IV in encryption mode violates security best" + " practices.");
        }
        keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_ALGORITHM, android.security.keymaster.KeymasterDefs.KM_ALGORITHM_AES);
        keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_BLOCK_MODE, mKeymasterBlockMode);
        keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_PADDING, mKeymasterPadding);
        if (mIv != null) {
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
        if (mIv == null) {
            mIv = returnedIv;
        } else
            if ((returnedIv != null) && (!java.util.Arrays.equals(returnedIv, mIv))) {
                throw new java.security.ProviderException("IV in use differs from provided IV");
            }

    }

    @java.lang.Override
    protected final int engineGetBlockSize() {
        return android.security.keystore.AndroidKeyStoreAuthenticatedAESCipherSpi.BLOCK_SIZE_BYTES;
    }

    @java.lang.Override
    protected final byte[] engineGetIV() {
        return android.security.keystore.ArrayUtils.cloneIfNotEmpty(mIv);
    }

    protected void setIv(byte[] iv) {
        mIv = iv;
    }

    protected byte[] getIv() {
        return mIv;
    }

    /**
     * {@link KeyStoreCryptoOperationStreamer} which buffers all output until {@code doFinal} from
     * which it returns all output in one go, provided {@code doFinal} succeeds.
     */
    private static class BufferAllOutputUntilDoFinalStreamer implements android.security.keystore.KeyStoreCryptoOperationStreamer {
        private final android.security.keystore.KeyStoreCryptoOperationStreamer mDelegate;

        private java.io.ByteArrayOutputStream mBufferedOutput = new java.io.ByteArrayOutputStream();

        private long mProducedOutputSizeBytes;

        private BufferAllOutputUntilDoFinalStreamer(android.security.keystore.KeyStoreCryptoOperationStreamer delegate) {
            mDelegate = delegate;
        }

        @java.lang.Override
        public byte[] update(byte[] input, int inputOffset, int inputLength) throws android.security.KeyStoreException {
            byte[] output = mDelegate.update(input, inputOffset, inputLength);
            if (output != null) {
                try {
                    mBufferedOutput.write(output);
                } catch (java.io.IOException e) {
                    throw new java.security.ProviderException("Failed to buffer output", e);
                }
            }
            return libcore.util.EmptyArray.BYTE;
        }

        @java.lang.Override
        public byte[] doFinal(byte[] input, int inputOffset, int inputLength, byte[] signature, byte[] additionalEntropy) throws android.security.KeyStoreException {
            byte[] output = mDelegate.doFinal(input, inputOffset, inputLength, signature, additionalEntropy);
            if (output != null) {
                try {
                    mBufferedOutput.write(output);
                } catch (java.io.IOException e) {
                    throw new java.security.ProviderException("Failed to buffer output", e);
                }
            }
            byte[] result = mBufferedOutput.toByteArray();
            mBufferedOutput.reset();
            mProducedOutputSizeBytes += result.length;
            return result;
        }

        @java.lang.Override
        public long getConsumedInputSizeBytes() {
            return mDelegate.getConsumedInputSizeBytes();
        }

        @java.lang.Override
        public long getProducedOutputSizeBytes() {
            return mProducedOutputSizeBytes;
        }
    }

    /**
     * Additional Authentication Data (AAD) stream via a KeyStore streaming operation. This stream
     * sends AAD into the KeyStore.
     */
    private static class AdditionalAuthenticationDataStream implements android.security.keystore.KeyStoreCryptoOperationChunkedStreamer.Stream {
        private final android.security.KeyStore mKeyStore;

        private final android.os.IBinder mOperationToken;

        private AdditionalAuthenticationDataStream(android.security.KeyStore keyStore, android.os.IBinder operationToken) {
            mKeyStore = keyStore;
            mOperationToken = operationToken;
        }

        @java.lang.Override
        public android.security.keymaster.OperationResult update(byte[] input) {
            android.security.keymaster.KeymasterArguments keymasterArgs = new android.security.keymaster.KeymasterArguments();
            keymasterArgs.addBytes(android.security.keymaster.KeymasterDefs.KM_TAG_ASSOCIATED_DATA, input);
            // KeyStore does not reflect AAD in inputConsumed, but users of Stream rely on this
            // field. We fix this discrepancy here. KeyStore.update contract is that all of AAD
            // has been consumed if the method succeeds.
            android.security.keymaster.OperationResult result = mKeyStore.update(mOperationToken, keymasterArgs, null);
            if (result.resultCode == android.security.KeyStore.NO_ERROR) {
                result = // inputConsumed
                new android.security.keymaster.OperationResult(result.resultCode, result.token, result.operationHandle, input.length, result.output, result.outParams);
            }
            return result;
        }

        @java.lang.Override
        public android.security.keymaster.OperationResult finish(byte[] signature, byte[] additionalEntropy) {
            if ((additionalEntropy != null) && (additionalEntropy.length > 0)) {
                throw new java.security.ProviderException("AAD stream does not support additional entropy");
            }
            return // operation handle -- nobody cares about this being returned from finish
            // inputConsumed
            // output
            // additional params returned by finish
            new android.security.keymaster.OperationResult(android.security.KeyStore.NO_ERROR, mOperationToken, 0, 0, libcore.util.EmptyArray.BYTE, new android.security.keymaster.KeymasterArguments());
        }
    }
}

