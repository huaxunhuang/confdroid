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
 * Base class for {@link CipherSpi} implementations of Android KeyStore backed ciphers.
 *
 * @unknown 
 */
abstract class AndroidKeyStoreCipherSpiBase extends javax.crypto.CipherSpi implements android.security.keystore.KeyStoreCryptoOperation {
    private final android.security.KeyStore mKeyStore;

    // Fields below are populated by Cipher.init and KeyStore.begin and should be preserved after
    // doFinal finishes.
    private boolean mEncrypting;

    private int mKeymasterPurposeOverride = -1;

    private android.security.keystore.AndroidKeyStoreKey mKey;

    private java.security.SecureRandom mRng;

    /**
     * Token referencing this operation inside keystore service. It is initialized by
     * {@code engineInit} and is invalidated when {@code engineDoFinal} succeeds and on some error
     * conditions in between.
     */
    private android.os.IBinder mOperationToken;

    private long mOperationHandle;

    private android.security.keystore.KeyStoreCryptoOperationStreamer mMainDataStreamer;

    private android.security.keystore.KeyStoreCryptoOperationStreamer mAdditionalAuthenticationDataStreamer;

    private boolean mAdditionalAuthenticationDataStreamerClosed;

    /**
     * Encountered exception which could not be immediately thrown because it was encountered inside
     * a method that does not throw checked exception. This exception will be thrown from
     * {@code engineDoFinal}. Once such an exception is encountered, {@code engineUpdate} and
     * {@code engineDoFinal} start ignoring input data.
     */
    private java.lang.Exception mCachedException;

    AndroidKeyStoreCipherSpiBase() {
        mKeyStore = android.security.KeyStore.getInstance();
    }

    @java.lang.Override
    protected final void engineInit(int opmode, java.security.Key key, java.security.SecureRandom random) throws java.security.InvalidKeyException {
        resetAll();
        boolean success = false;
        try {
            init(opmode, key, random);
            initAlgorithmSpecificParameters();
            try {
                ensureKeystoreOperationInitialized();
            } catch (java.security.InvalidAlgorithmParameterException e) {
                throw new java.security.InvalidKeyException(e);
            }
            success = true;
        } finally {
            if (!success) {
                resetAll();
            }
        }
    }

    @java.lang.Override
    protected final void engineInit(int opmode, java.security.Key key, java.security.AlgorithmParameters params, java.security.SecureRandom random) throws java.security.InvalidAlgorithmParameterException, java.security.InvalidKeyException {
        resetAll();
        boolean success = false;
        try {
            init(opmode, key, random);
            initAlgorithmSpecificParameters(params);
            ensureKeystoreOperationInitialized();
            success = true;
        } finally {
            if (!success) {
                resetAll();
            }
        }
    }

    @java.lang.Override
    protected final void engineInit(int opmode, java.security.Key key, java.security.spec.AlgorithmParameterSpec params, java.security.SecureRandom random) throws java.security.InvalidAlgorithmParameterException, java.security.InvalidKeyException {
        resetAll();
        boolean success = false;
        try {
            init(opmode, key, random);
            initAlgorithmSpecificParameters(params);
            ensureKeystoreOperationInitialized();
            success = true;
        } finally {
            if (!success) {
                resetAll();
            }
        }
    }

    private void init(int opmode, java.security.Key key, java.security.SecureRandom random) throws java.security.InvalidKeyException {
        switch (opmode) {
            case javax.crypto.Cipher.ENCRYPT_MODE :
            case javax.crypto.Cipher.WRAP_MODE :
                mEncrypting = true;
                break;
            case javax.crypto.Cipher.DECRYPT_MODE :
            case javax.crypto.Cipher.UNWRAP_MODE :
                mEncrypting = false;
                break;
            default :
                throw new java.security.InvalidParameterException("Unsupported opmode: " + opmode);
        }
        initKey(opmode, key);
        if (mKey == null) {
            throw new java.security.ProviderException("initKey did not initialize the key");
        }
        mRng = random;
    }

    /**
     * Resets this cipher to its pristine pre-init state. This must be equivalent to obtaining a new
     * cipher instance.
     *
     * <p>Subclasses storing additional state should override this method, reset the additional
     * state, and then chain to superclass.
     */
    @android.annotation.CallSuper
    protected void resetAll() {
        android.os.IBinder operationToken = mOperationToken;
        if (operationToken != null) {
            mKeyStore.abort(operationToken);
        }
        mEncrypting = false;
        mKeymasterPurposeOverride = -1;
        mKey = null;
        mRng = null;
        mOperationToken = null;
        mOperationHandle = 0;
        mMainDataStreamer = null;
        mAdditionalAuthenticationDataStreamer = null;
        mAdditionalAuthenticationDataStreamerClosed = false;
        mCachedException = null;
    }

    /**
     * Resets this cipher while preserving the initialized state. This must be equivalent to
     * rolling back the cipher's state to just after the most recent {@code engineInit} completed
     * successfully.
     *
     * <p>Subclasses storing additional post-init state should override this method, reset the
     * additional state, and then chain to superclass.
     */
    @android.annotation.CallSuper
    protected void resetWhilePreservingInitState() {
        android.os.IBinder operationToken = mOperationToken;
        if (operationToken != null) {
            mKeyStore.abort(operationToken);
        }
        mOperationToken = null;
        mOperationHandle = 0;
        mMainDataStreamer = null;
        mAdditionalAuthenticationDataStreamer = null;
        mAdditionalAuthenticationDataStreamerClosed = false;
        mCachedException = null;
    }

    private void ensureKeystoreOperationInitialized() throws java.security.InvalidAlgorithmParameterException, java.security.InvalidKeyException {
        if (mMainDataStreamer != null) {
            return;
        }
        if (mCachedException != null) {
            return;
        }
        if (mKey == null) {
            throw new java.lang.IllegalStateException("Not initialized");
        }
        android.security.keymaster.KeymasterArguments keymasterInputArgs = new android.security.keymaster.KeymasterArguments();
        addAlgorithmSpecificParametersToBegin(keymasterInputArgs);
        byte[] additionalEntropy = android.security.keystore.KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(mRng, getAdditionalEntropyAmountForBegin());
        int purpose;
        if (mKeymasterPurposeOverride != (-1)) {
            purpose = mKeymasterPurposeOverride;
        } else {
            purpose = (mEncrypting) ? android.security.keymaster.KeymasterDefs.KM_PURPOSE_ENCRYPT : android.security.keymaster.KeymasterDefs.KM_PURPOSE_DECRYPT;
        }
        android.security.keymaster.OperationResult opResult = // permit aborting this operation if keystore runs out of resources
        mKeyStore.begin(mKey.getAlias(), purpose, true, keymasterInputArgs, additionalEntropy, mKey.getUid());
        if (opResult == null) {
            throw new android.security.keystore.KeyStoreConnectException();
        }
        // Store operation token and handle regardless of the error code returned by KeyStore to
        // ensure that the operation gets aborted immediately if the code below throws an exception.
        mOperationToken = opResult.token;
        mOperationHandle = opResult.operationHandle;
        // If necessary, throw an exception due to KeyStore operation having failed.
        java.security.GeneralSecurityException e = android.security.keystore.KeyStoreCryptoOperationUtils.getExceptionForCipherInit(mKeyStore, mKey, opResult.resultCode);
        if (e != null) {
            if (e instanceof java.security.InvalidKeyException) {
                throw ((java.security.InvalidKeyException) (e));
            } else
                if (e instanceof java.security.InvalidAlgorithmParameterException) {
                    throw ((java.security.InvalidAlgorithmParameterException) (e));
                } else {
                    throw new java.security.ProviderException("Unexpected exception type", e);
                }

        }
        if (mOperationToken == null) {
            throw new java.security.ProviderException("Keystore returned null operation token");
        }
        if (mOperationHandle == 0) {
            throw new java.security.ProviderException("Keystore returned invalid operation handle");
        }
        loadAlgorithmSpecificParametersFromBeginResult(opResult.outParams);
        mMainDataStreamer = createMainDataStreamer(mKeyStore, opResult.token);
        mAdditionalAuthenticationDataStreamer = createAdditionalAuthenticationDataStreamer(mKeyStore, opResult.token);
        mAdditionalAuthenticationDataStreamerClosed = false;
    }

    /**
     * Creates a streamer which sends plaintext/ciphertext into the provided KeyStore and receives
     * the corresponding ciphertext/plaintext from the KeyStore.
     *
     * <p>This implementation returns a working streamer.
     */
    @android.annotation.NonNull
    protected android.security.keystore.KeyStoreCryptoOperationStreamer createMainDataStreamer(android.security.KeyStore keyStore, android.os.IBinder operationToken) {
        return new android.security.keystore.KeyStoreCryptoOperationChunkedStreamer(new android.security.keystore.KeyStoreCryptoOperationChunkedStreamer.MainDataStream(keyStore, operationToken));
    }

    /**
     * Creates a streamer which sends Additional Authentication Data (AAD) into the KeyStore.
     *
     * <p>This implementation returns {@code null}.
     *
     * @unknown stream or {@code null} if AAD is not supported by this cipher.
     */
    @android.annotation.Nullable
    protected android.security.keystore.KeyStoreCryptoOperationStreamer createAdditionalAuthenticationDataStreamer(@java.lang.SuppressWarnings("unused")
    android.security.KeyStore keyStore, @java.lang.SuppressWarnings("unused")
    android.os.IBinder operationToken) {
        return null;
    }

    @java.lang.Override
    protected final byte[] engineUpdate(byte[] input, int inputOffset, int inputLen) {
        if (mCachedException != null) {
            return null;
        }
        try {
            ensureKeystoreOperationInitialized();
        } catch (java.security.InvalidKeyException | java.security.InvalidAlgorithmParameterException e) {
            mCachedException = e;
            return null;
        }
        if (inputLen == 0) {
            return null;
        }
        byte[] output;
        try {
            flushAAD();
            output = mMainDataStreamer.update(input, inputOffset, inputLen);
        } catch (android.security.KeyStoreException e) {
            mCachedException = e;
            return null;
        }
        if (output.length == 0) {
            return null;
        }
        return output;
    }

    private void flushAAD() throws android.security.KeyStoreException {
        if ((mAdditionalAuthenticationDataStreamer != null) && (!mAdditionalAuthenticationDataStreamerClosed)) {
            byte[] output;
            try {
                output = // no signature
                // no additional entropy needed flushing AAD
                mAdditionalAuthenticationDataStreamer.doFinal(EmptyArray.BYTE, 0, 0, null, null);
            } finally {
                mAdditionalAuthenticationDataStreamerClosed = true;
            }
            if ((output != null) && (output.length > 0)) {
                throw new java.security.ProviderException(("AAD update unexpectedly returned data: " + output.length) + " bytes");
            }
        }
    }

    @java.lang.Override
    protected final int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws javax.crypto.ShortBufferException {
        byte[] outputCopy = engineUpdate(input, inputOffset, inputLen);
        if (outputCopy == null) {
            return 0;
        }
        int outputAvailable = output.length - outputOffset;
        if (outputCopy.length > outputAvailable) {
            throw new javax.crypto.ShortBufferException((("Output buffer too short. Produced: " + outputCopy.length) + ", available: ") + outputAvailable);
        }
        java.lang.System.arraycopy(outputCopy, 0, output, outputOffset, outputCopy.length);
        return outputCopy.length;
    }

    @java.lang.Override
    protected final int engineUpdate(java.nio.ByteBuffer input, java.nio.ByteBuffer output) throws javax.crypto.ShortBufferException {
        if (input == null) {
            throw new java.lang.NullPointerException("input == null");
        }
        if (output == null) {
            throw new java.lang.NullPointerException("output == null");
        }
        int inputSize = input.remaining();
        byte[] outputArray;
        if (input.hasArray()) {
            outputArray = engineUpdate(input.array(), input.arrayOffset() + input.position(), inputSize);
            input.position(input.position() + inputSize);
        } else {
            byte[] inputArray = new byte[inputSize];
            input.get(inputArray);
            outputArray = engineUpdate(inputArray, 0, inputSize);
        }
        int outputSize = (outputArray != null) ? outputArray.length : 0;
        if (outputSize > 0) {
            int outputBufferAvailable = output.remaining();
            try {
                output.put(outputArray);
            } catch (java.nio.BufferOverflowException e) {
                throw new javax.crypto.ShortBufferException((("Output buffer too small. Produced: " + outputSize) + ", available: ") + outputBufferAvailable);
            }
        }
        return outputSize;
    }

    @java.lang.Override
    protected final void engineUpdateAAD(byte[] input, int inputOffset, int inputLen) {
        if (mCachedException != null) {
            return;
        }
        try {
            ensureKeystoreOperationInitialized();
        } catch (java.security.InvalidKeyException | java.security.InvalidAlgorithmParameterException e) {
            mCachedException = e;
            return;
        }
        if (mAdditionalAuthenticationDataStreamerClosed) {
            throw new java.lang.IllegalStateException("AAD can only be provided before Cipher.update is invoked");
        }
        if (mAdditionalAuthenticationDataStreamer == null) {
            throw new java.lang.IllegalStateException("This cipher does not support AAD");
        }
        byte[] output;
        try {
            output = mAdditionalAuthenticationDataStreamer.update(input, inputOffset, inputLen);
        } catch (android.security.KeyStoreException e) {
            mCachedException = e;
            return;
        }
        if ((output != null) && (output.length > 0)) {
            throw new java.security.ProviderException(("AAD update unexpectedly produced output: " + output.length) + " bytes");
        }
    }

    @java.lang.Override
    protected final void engineUpdateAAD(java.nio.ByteBuffer src) {
        if (src == null) {
            throw new java.lang.IllegalArgumentException("src == null");
        }
        if (!src.hasRemaining()) {
            return;
        }
        byte[] input;
        int inputOffset;
        int inputLen;
        if (src.hasArray()) {
            input = src.array();
            inputOffset = src.arrayOffset() + src.position();
            inputLen = src.remaining();
            src.position(src.limit());
        } else {
            input = new byte[src.remaining()];
            inputOffset = 0;
            inputLen = input.length;
            src.get(input);
        }
        engineUpdateAAD(input, inputOffset, inputLen);
    }

    @java.lang.Override
    protected final byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen) throws javax.crypto.BadPaddingException, javax.crypto.IllegalBlockSizeException {
        if (mCachedException != null) {
            throw ((javax.crypto.IllegalBlockSizeException) (new javax.crypto.IllegalBlockSizeException().initCause(mCachedException)));
        }
        try {
            ensureKeystoreOperationInitialized();
        } catch (java.security.InvalidKeyException | java.security.InvalidAlgorithmParameterException e) {
            throw ((javax.crypto.IllegalBlockSizeException) (new javax.crypto.IllegalBlockSizeException().initCause(e)));
        }
        byte[] output;
        try {
            flushAAD();
            byte[] additionalEntropy = android.security.keystore.KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(mRng, getAdditionalEntropyAmountForFinish());
            output = // no signature involved
            mMainDataStreamer.doFinal(input, inputOffset, inputLen, null, additionalEntropy);
        } catch (android.security.KeyStoreException e) {
            switch (e.getErrorCode()) {
                case android.security.keymaster.KeymasterDefs.KM_ERROR_INVALID_INPUT_LENGTH :
                    throw ((javax.crypto.IllegalBlockSizeException) (new javax.crypto.IllegalBlockSizeException().initCause(e)));
                case android.security.keymaster.KeymasterDefs.KM_ERROR_INVALID_ARGUMENT :
                    throw ((javax.crypto.BadPaddingException) (new javax.crypto.BadPaddingException().initCause(e)));
                case android.security.keymaster.KeymasterDefs.KM_ERROR_VERIFICATION_FAILED :
                    throw ((javax.crypto.AEADBadTagException) (new javax.crypto.AEADBadTagException().initCause(e)));
                default :
                    throw ((javax.crypto.IllegalBlockSizeException) (new javax.crypto.IllegalBlockSizeException().initCause(e)));
            }
        }
        resetWhilePreservingInitState();
        return output;
    }

    @java.lang.Override
    protected final int engineDoFinal(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws javax.crypto.BadPaddingException, javax.crypto.IllegalBlockSizeException, javax.crypto.ShortBufferException {
        byte[] outputCopy = engineDoFinal(input, inputOffset, inputLen);
        if (outputCopy == null) {
            return 0;
        }
        int outputAvailable = output.length - outputOffset;
        if (outputCopy.length > outputAvailable) {
            throw new javax.crypto.ShortBufferException((("Output buffer too short. Produced: " + outputCopy.length) + ", available: ") + outputAvailable);
        }
        java.lang.System.arraycopy(outputCopy, 0, output, outputOffset, outputCopy.length);
        return outputCopy.length;
    }

    @java.lang.Override
    protected final int engineDoFinal(java.nio.ByteBuffer input, java.nio.ByteBuffer output) throws javax.crypto.BadPaddingException, javax.crypto.IllegalBlockSizeException, javax.crypto.ShortBufferException {
        if (input == null) {
            throw new java.lang.NullPointerException("input == null");
        }
        if (output == null) {
            throw new java.lang.NullPointerException("output == null");
        }
        int inputSize = input.remaining();
        byte[] outputArray;
        if (input.hasArray()) {
            outputArray = engineDoFinal(input.array(), input.arrayOffset() + input.position(), inputSize);
            input.position(input.position() + inputSize);
        } else {
            byte[] inputArray = new byte[inputSize];
            input.get(inputArray);
            outputArray = engineDoFinal(inputArray, 0, inputSize);
        }
        int outputSize = (outputArray != null) ? outputArray.length : 0;
        if (outputSize > 0) {
            int outputBufferAvailable = output.remaining();
            try {
                output.put(outputArray);
            } catch (java.nio.BufferOverflowException e) {
                throw new javax.crypto.ShortBufferException((("Output buffer too small. Produced: " + outputSize) + ", available: ") + outputBufferAvailable);
            }
        }
        return outputSize;
    }

    @java.lang.Override
    protected final byte[] engineWrap(java.security.Key key) throws java.security.InvalidKeyException, javax.crypto.IllegalBlockSizeException {
        if (mKey == null) {
            throw new java.lang.IllegalStateException("Not initilized");
        }
        if (!isEncrypting()) {
            throw new java.lang.IllegalStateException("Cipher must be initialized in Cipher.WRAP_MODE to wrap keys");
        }
        if (key == null) {
            throw new java.lang.NullPointerException("key == null");
        }
        byte[] encoded = null;
        if (key instanceof javax.crypto.SecretKey) {
            if ("RAW".equalsIgnoreCase(key.getFormat())) {
                encoded = key.getEncoded();
            }
            if (encoded == null) {
                try {
                    javax.crypto.SecretKeyFactory keyFactory = javax.crypto.SecretKeyFactory.getInstance(key.getAlgorithm());
                    javax.crypto.spec.SecretKeySpec spec = ((javax.crypto.spec.SecretKeySpec) (keyFactory.getKeySpec(((javax.crypto.SecretKey) (key)), javax.crypto.spec.SecretKeySpec.class)));
                    encoded = spec.getEncoded();
                } catch (java.security.NoSuchAlgorithmException | java.security.spec.InvalidKeySpecException e) {
                    throw new java.security.InvalidKeyException("Failed to wrap key because it does not export its key material", e);
                }
            }
        } else
            if (key instanceof java.security.PrivateKey) {
                if ("PKCS8".equalsIgnoreCase(key.getFormat())) {
                    encoded = key.getEncoded();
                }
                if (encoded == null) {
                    try {
                        java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance(key.getAlgorithm());
                        java.security.spec.PKCS8EncodedKeySpec spec = keyFactory.getKeySpec(key, java.security.spec.PKCS8EncodedKeySpec.class);
                        encoded = spec.getEncoded();
                    } catch (java.security.NoSuchAlgorithmException | java.security.spec.InvalidKeySpecException e) {
                        throw new java.security.InvalidKeyException("Failed to wrap key because it does not export its key material", e);
                    }
                }
            } else
                if (key instanceof java.security.PublicKey) {
                    if ("X.509".equalsIgnoreCase(key.getFormat())) {
                        encoded = key.getEncoded();
                    }
                    if (encoded == null) {
                        try {
                            java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance(key.getAlgorithm());
                            java.security.spec.X509EncodedKeySpec spec = keyFactory.getKeySpec(key, java.security.spec.X509EncodedKeySpec.class);
                            encoded = spec.getEncoded();
                        } catch (java.security.NoSuchAlgorithmException | java.security.spec.InvalidKeySpecException e) {
                            throw new java.security.InvalidKeyException("Failed to wrap key because it does not export its key material", e);
                        }
                    }
                } else {
                    throw new java.security.InvalidKeyException("Unsupported key type: " + key.getClass().getName());
                }


        if (encoded == null) {
            throw new java.security.InvalidKeyException("Failed to wrap key because it does not export its key material");
        }
        try {
            return engineDoFinal(encoded, 0, encoded.length);
        } catch (javax.crypto.BadPaddingException e) {
            throw ((javax.crypto.IllegalBlockSizeException) (new javax.crypto.IllegalBlockSizeException().initCause(e)));
        }
    }

    @java.lang.Override
    protected final java.security.Key engineUnwrap(byte[] wrappedKey, java.lang.String wrappedKeyAlgorithm, int wrappedKeyType) throws java.security.InvalidKeyException, java.security.NoSuchAlgorithmException {
        if (mKey == null) {
            throw new java.lang.IllegalStateException("Not initilized");
        }
        if (isEncrypting()) {
            throw new java.lang.IllegalStateException("Cipher must be initialized in Cipher.WRAP_MODE to wrap keys");
        }
        if (wrappedKey == null) {
            throw new java.lang.NullPointerException("wrappedKey == null");
        }
        byte[] encoded;
        try {
            encoded = engineDoFinal(wrappedKey, 0, wrappedKey.length);
        } catch (javax.crypto.IllegalBlockSizeException | javax.crypto.BadPaddingException e) {
            throw new java.security.InvalidKeyException("Failed to unwrap key", e);
        }
        switch (wrappedKeyType) {
            case javax.crypto.Cipher.SECRET_KEY :
                {
                    return new javax.crypto.spec.SecretKeySpec(encoded, wrappedKeyAlgorithm);
                    // break;
                }
            case javax.crypto.Cipher.PRIVATE_KEY :
                {
                    java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance(wrappedKeyAlgorithm);
                    try {
                        return keyFactory.generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(encoded));
                    } catch (java.security.spec.InvalidKeySpecException e) {
                        throw new java.security.InvalidKeyException("Failed to create private key from its PKCS#8 encoded form", e);
                    }
                    // break;
                }
            case javax.crypto.Cipher.PUBLIC_KEY :
                {
                    java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance(wrappedKeyAlgorithm);
                    try {
                        return keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(encoded));
                    } catch (java.security.spec.InvalidKeySpecException e) {
                        throw new java.security.InvalidKeyException("Failed to create public key from its X.509 encoded form", e);
                    }
                    // break;
                }
            default :
                throw new java.security.InvalidParameterException("Unsupported wrappedKeyType: " + wrappedKeyType);
        }
    }

    @java.lang.Override
    protected final void engineSetMode(java.lang.String mode) throws java.security.NoSuchAlgorithmException {
        // This should never be invoked because all algorithms registered with the AndroidKeyStore
        // provide explicitly specify block mode.
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    protected final void engineSetPadding(java.lang.String arg0) throws javax.crypto.NoSuchPaddingException {
        // This should never be invoked because all algorithms registered with the AndroidKeyStore
        // provide explicitly specify padding mode.
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    protected final int engineGetKeySize(java.security.Key key) throws java.security.InvalidKeyException {
        throw new java.lang.UnsupportedOperationException();
    }

    @android.annotation.CallSuper
    @java.lang.Override
    public void finalize() throws java.lang.Throwable {
        try {
            android.os.IBinder operationToken = mOperationToken;
            if (operationToken != null) {
                mKeyStore.abort(operationToken);
            }
        } finally {
            super.finalize();
        }
    }

    @java.lang.Override
    public final long getOperationHandle() {
        return mOperationHandle;
    }

    protected final void setKey(@android.annotation.NonNull
    android.security.keystore.AndroidKeyStoreKey key) {
        mKey = key;
    }

    /**
     * Overrides the default purpose/type of the crypto operation.
     */
    protected final void setKeymasterPurposeOverride(int keymasterPurpose) {
        mKeymasterPurposeOverride = keymasterPurpose;
    }

    protected final int getKeymasterPurposeOverride() {
        return mKeymasterPurposeOverride;
    }

    /**
     * Returns {@code true} if this cipher is initialized for encryption, {@code false} if this
     * cipher is initialized for decryption.
     */
    protected final boolean isEncrypting() {
        return mEncrypting;
    }

    @android.annotation.NonNull
    protected final android.security.KeyStore getKeyStore() {
        return mKeyStore;
    }

    protected final long getConsumedInputSizeBytes() {
        if (mMainDataStreamer == null) {
            throw new java.lang.IllegalStateException("Not initialized");
        }
        return mMainDataStreamer.getConsumedInputSizeBytes();
    }

    protected final long getProducedOutputSizeBytes() {
        if (mMainDataStreamer == null) {
            throw new java.lang.IllegalStateException("Not initialized");
        }
        return mMainDataStreamer.getProducedOutputSizeBytes();
    }

    static java.lang.String opmodeToString(int opmode) {
        switch (opmode) {
            case javax.crypto.Cipher.ENCRYPT_MODE :
                return "ENCRYPT_MODE";
            case javax.crypto.Cipher.DECRYPT_MODE :
                return "DECRYPT_MODE";
            case javax.crypto.Cipher.WRAP_MODE :
                return "WRAP_MODE";
            case javax.crypto.Cipher.UNWRAP_MODE :
                return "UNWRAP_MODE";
            default :
                return java.lang.String.valueOf(opmode);
        }
    }

    // The methods below need to be implemented by subclasses.
    /**
     * Initializes this cipher with the provided key.
     *
     * @throws InvalidKeyException
     * 		if the {@code key} is not suitable for this cipher in the
     * 		specified {@code opmode}.
     * @see #setKey(AndroidKeyStoreKey)
     */
    protected abstract void initKey(int opmode, @android.annotation.Nullable
    java.security.Key key) throws java.security.InvalidKeyException;

    /**
     * Returns algorithm-specific parameters used by this cipher or {@code null} if no
     * algorithm-specific parameters are used.
     */
    @android.annotation.Nullable
    @java.lang.Override
    protected abstract java.security.AlgorithmParameters engineGetParameters();

    /**
     * Invoked by {@code engineInit} to initialize algorithm-specific parameters when no additional
     * initialization parameters were provided.
     *
     * @throws InvalidKeyException
     * 		if this cipher cannot be configured based purely on the provided
     * 		key and needs additional parameters to be provided to {@code Cipher.init}.
     */
    protected abstract void initAlgorithmSpecificParameters() throws java.security.InvalidKeyException;

    /**
     * Invoked by {@code engineInit} to initialize algorithm-specific parameters when additional
     * parameters were provided.
     *
     * @param params
     * 		additional algorithm parameters or {@code null} if not specified.
     * @throws InvalidAlgorithmParameterException
     * 		if there is insufficient information to configure
     * 		this cipher or if the provided parameters are not suitable for this cipher.
     */
    protected abstract void initAlgorithmSpecificParameters(@android.annotation.Nullable
    java.security.spec.AlgorithmParameterSpec params) throws java.security.InvalidAlgorithmParameterException;

    /**
     * Invoked by {@code engineInit} to initialize algorithm-specific parameters when additional
     * parameters were provided.
     *
     * @param params
     * 		additional algorithm parameters or {@code null} if not specified.
     * @throws InvalidAlgorithmParameterException
     * 		if there is insufficient information to configure
     * 		this cipher or if the provided parameters are not suitable for this cipher.
     */
    protected abstract void initAlgorithmSpecificParameters(@android.annotation.Nullable
    java.security.AlgorithmParameters params) throws java.security.InvalidAlgorithmParameterException;

    /**
     * Returns the amount of additional entropy (in bytes) to be provided to the KeyStore's
     * {@code begin} operation. This amount of entropy is typically what's consumed to generate
     * random parameters, such as IV.
     *
     * <p>For decryption, the return value should be {@code 0} because decryption should not be
     * consuming any entropy. For encryption, the value combined with
     * {@link #getAdditionalEntropyAmountForFinish()} should match (or exceed) the amount of Shannon
     * entropy of the ciphertext produced by this cipher assuming the key, the plaintext, and all
     * explicitly provided parameters to {@code Cipher.init} are known. For example, for AES CBC
     * encryption with an explicitly provided IV the return value should be {@code 0}, whereas for
     * the case where IV is generated by the KeyStore's {@code begin} operation it should be
     * {@code 16}.
     */
    protected abstract int getAdditionalEntropyAmountForBegin();

    /**
     * Returns the amount of additional entropy (in bytes) to be provided to the KeyStore's
     * {@code finish} operation. This amount of entropy is typically what's consumed by encryption
     * padding scheme.
     *
     * <p>For decryption, the return value should be {@code 0} because decryption should not be
     * consuming any entropy. For encryption, the value combined with
     * {@link #getAdditionalEntropyAmountForBegin()} should match (or exceed) the amount of Shannon
     * entropy of the ciphertext produced by this cipher assuming the key, the plaintext, and all
     * explicitly provided parameters to {@code Cipher.init} are known. For example, for RSA with
     * OAEP the return value should be the size of the OAEP hash output. For RSA with PKCS#1 padding
     * the return value should be the size of the padding string or could be raised (for simplicity)
     * to the size of the modulus.
     */
    protected abstract int getAdditionalEntropyAmountForFinish();

    /**
     * Invoked to add algorithm-specific parameters for the KeyStore's {@code begin} operation.
     *
     * @param keymasterArgs
     * 		keystore/keymaster arguments to be populated with algorithm-specific
     * 		parameters.
     */
    protected abstract void addAlgorithmSpecificParametersToBegin(@android.annotation.NonNull
    android.security.keymaster.KeymasterArguments keymasterArgs);

    /**
     * Invoked to obtain algorithm-specific parameters from the result of the KeyStore's
     * {@code begin} operation.
     *
     * <p>Some parameters, such as IV, are not required to be provided to {@code Cipher.init}. Such
     * parameters, if not provided, must be generated by KeyStore and returned to the user of
     * {@code Cipher} and potentially reused after {@code doFinal}.
     *
     * @param keymasterArgs
     * 		keystore/keymaster arguments returned by KeyStore {@code begin}
     * 		operation.
     */
    protected abstract void loadAlgorithmSpecificParametersFromBeginResult(@android.annotation.NonNull
    android.security.keymaster.KeymasterArguments keymasterArgs);
}

