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
 * Base class for {@link SignatureSpi} implementations of Android KeyStore backed ciphers.
 *
 * @unknown 
 */
abstract class AndroidKeyStoreSignatureSpiBase extends java.security.SignatureSpi implements android.security.keystore.KeyStoreCryptoOperation {
    private final android.security.KeyStore mKeyStore;

    // Fields below are populated by SignatureSpi.engineInitSign/engineInitVerify and KeyStore.begin
    // and should be preserved after SignatureSpi.engineSign/engineVerify finishes.
    private boolean mSigning;

    private android.security.keystore.AndroidKeyStoreKey mKey;

    /**
     * Token referencing this operation inside keystore service. It is initialized by
     * {@code engineInitSign}/{@code engineInitVerify} and is invalidated when
     * {@code engineSign}/{@code engineVerify} succeeds and on some error conditions in between.
     */
    private android.os.IBinder mOperationToken;

    private long mOperationHandle;

    private android.security.keystore.KeyStoreCryptoOperationStreamer mMessageStreamer;

    /**
     * Encountered exception which could not be immediately thrown because it was encountered inside
     * a method that does not throw checked exception. This exception will be thrown from
     * {@code engineSign} or {@code engineVerify}. Once such an exception is encountered,
     * {@code engineUpdate} starts ignoring input data.
     */
    private java.lang.Exception mCachedException;

    AndroidKeyStoreSignatureSpiBase() {
        mKeyStore = android.security.KeyStore.getInstance();
    }

    @java.lang.Override
    protected final void engineInitSign(java.security.PrivateKey key) throws java.security.InvalidKeyException {
        engineInitSign(key, null);
    }

    @java.lang.Override
    protected final void engineInitSign(java.security.PrivateKey privateKey, java.security.SecureRandom random) throws java.security.InvalidKeyException {
        resetAll();
        boolean success = false;
        try {
            if (privateKey == null) {
                throw new java.security.InvalidKeyException("Unsupported key: null");
            }
            android.security.keystore.AndroidKeyStoreKey keystoreKey;
            if (privateKey instanceof android.security.keystore.AndroidKeyStorePrivateKey) {
                keystoreKey = ((android.security.keystore.AndroidKeyStoreKey) (privateKey));
            } else {
                throw new java.security.InvalidKeyException("Unsupported private key type: " + privateKey);
            }
            mSigning = true;
            initKey(keystoreKey);
            appRandom = random;
            ensureKeystoreOperationInitialized();
            success = true;
        } finally {
            if (!success) {
                resetAll();
            }
        }
    }

    @java.lang.Override
    protected final void engineInitVerify(java.security.PublicKey publicKey) throws java.security.InvalidKeyException {
        resetAll();
        boolean success = false;
        try {
            if (publicKey == null) {
                throw new java.security.InvalidKeyException("Unsupported key: null");
            }
            android.security.keystore.AndroidKeyStoreKey keystoreKey;
            if (publicKey instanceof android.security.keystore.AndroidKeyStorePublicKey) {
                keystoreKey = ((android.security.keystore.AndroidKeyStorePublicKey) (publicKey));
            } else {
                throw new java.security.InvalidKeyException("Unsupported public key type: " + publicKey);
            }
            mSigning = false;
            initKey(keystoreKey);
            appRandom = null;
            ensureKeystoreOperationInitialized();
            success = true;
        } finally {
            if (!success) {
                resetAll();
            }
        }
    }

    /**
     * Configures this signature instance to use the provided key.
     *
     * @throws InvalidKeyException
     * 		if the {@code key} is not suitable.
     */
    @android.annotation.CallSuper
    protected void initKey(android.security.keystore.AndroidKeyStoreKey key) throws java.security.InvalidKeyException {
        mKey = key;
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
            mOperationToken = null;
            mKeyStore.abort(operationToken);
        }
        mSigning = false;
        mKey = null;
        appRandom = null;
        mOperationToken = null;
        mOperationHandle = 0;
        mMessageStreamer = null;
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
            mOperationToken = null;
            mKeyStore.abort(operationToken);
        }
        mOperationHandle = 0;
        mMessageStreamer = null;
        mCachedException = null;
    }

    private void ensureKeystoreOperationInitialized() throws java.security.InvalidKeyException {
        if (mMessageStreamer != null) {
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
        android.security.keymaster.OperationResult opResult = // permit aborting this operation if keystore runs out of resources
        // no additional entropy for begin -- only finish might need some
        mKeyStore.begin(mKey.getAlias(), mSigning ? android.security.keymaster.KeymasterDefs.KM_PURPOSE_SIGN : android.security.keymaster.KeymasterDefs.KM_PURPOSE_VERIFY, true, keymasterInputArgs, null, mKey.getUid());
        if (opResult == null) {
            throw new android.security.keystore.KeyStoreConnectException();
        }
        // Store operation token and handle regardless of the error code returned by KeyStore to
        // ensure that the operation gets aborted immediately if the code below throws an exception.
        mOperationToken = opResult.token;
        mOperationHandle = opResult.operationHandle;
        // If necessary, throw an exception due to KeyStore operation having failed.
        java.security.InvalidKeyException e = android.security.keystore.KeyStoreCryptoOperationUtils.getInvalidKeyExceptionForInit(mKeyStore, mKey, opResult.resultCode);
        if (e != null) {
            throw e;
        }
        if (mOperationToken == null) {
            throw new java.security.ProviderException("Keystore returned null operation token");
        }
        if (mOperationHandle == 0) {
            throw new java.security.ProviderException("Keystore returned invalid operation handle");
        }
        mMessageStreamer = createMainDataStreamer(mKeyStore, opResult.token);
    }

    /**
     * Creates a streamer which sends the message to be signed/verified into the provided KeyStore
     *
     * <p>This implementation returns a working streamer.
     */
    @android.annotation.NonNull
    protected android.security.keystore.KeyStoreCryptoOperationStreamer createMainDataStreamer(android.security.KeyStore keyStore, android.os.IBinder operationToken) {
        return new android.security.keystore.KeyStoreCryptoOperationChunkedStreamer(new android.security.keystore.KeyStoreCryptoOperationChunkedStreamer.MainDataStream(keyStore, operationToken));
    }

    @java.lang.Override
    public final long getOperationHandle() {
        return mOperationHandle;
    }

    @java.lang.Override
    protected final void engineUpdate(byte[] b, int off, int len) throws java.security.SignatureException {
        if (mCachedException != null) {
            throw new java.security.SignatureException(mCachedException);
        }
        try {
            ensureKeystoreOperationInitialized();
        } catch (java.security.InvalidKeyException e) {
            throw new java.security.SignatureException(e);
        }
        if (len == 0) {
            return;
        }
        byte[] output;
        try {
            output = mMessageStreamer.update(b, off, len);
        } catch (android.security.KeyStoreException e) {
            throw new java.security.SignatureException(e);
        }
        if (output.length != 0) {
            throw new java.security.ProviderException(("Update operation unexpectedly produced output: " + output.length) + " bytes");
        }
    }

    @java.lang.Override
    protected final void engineUpdate(byte b) throws java.security.SignatureException {
        engineUpdate(new byte[]{ b }, 0, 1);
    }

    @java.lang.Override
    protected final void engineUpdate(java.nio.ByteBuffer input) {
        byte[] b;
        int off;
        int len = input.remaining();
        if (input.hasArray()) {
            b = input.array();
            off = input.arrayOffset() + input.position();
            input.position(input.limit());
        } else {
            b = new byte[len];
            off = 0;
            input.get(b);
        }
        try {
            engineUpdate(b, off, len);
        } catch (java.security.SignatureException e) {
            mCachedException = e;
        }
    }

    @java.lang.Override
    protected final int engineSign(byte[] out, int outOffset, int outLen) throws java.security.SignatureException {
        return super.engineSign(out, outOffset, outLen);
    }

    @java.lang.Override
    protected final byte[] engineSign() throws java.security.SignatureException {
        if (mCachedException != null) {
            throw new java.security.SignatureException(mCachedException);
        }
        byte[] signature;
        try {
            ensureKeystoreOperationInitialized();
            byte[] additionalEntropy = android.security.keystore.KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(appRandom, getAdditionalEntropyAmountForSign());
            signature = // no signature provided -- it'll be generated by this invocation
            mMessageStreamer.doFinal(EmptyArray.BYTE, 0, 0, null, additionalEntropy);
        } catch (java.security.InvalidKeyException | android.security.KeyStoreException e) {
            throw new java.security.SignatureException(e);
        }
        resetWhilePreservingInitState();
        return signature;
    }

    @java.lang.Override
    protected final boolean engineVerify(byte[] signature) throws java.security.SignatureException {
        if (mCachedException != null) {
            throw new java.security.SignatureException(mCachedException);
        }
        try {
            ensureKeystoreOperationInitialized();
        } catch (java.security.InvalidKeyException e) {
            throw new java.security.SignatureException(e);
        }
        boolean verified;
        try {
            byte[] output = // no additional entropy needed -- verification is deterministic
            mMessageStreamer.doFinal(EmptyArray.BYTE, 0, 0, signature, null);
            if (output.length != 0) {
                throw new java.security.ProviderException(("Signature verification unexpected produced output: " + output.length) + " bytes");
            }
            verified = true;
        } catch (android.security.KeyStoreException e) {
            switch (e.getErrorCode()) {
                case android.security.keymaster.KeymasterDefs.KM_ERROR_VERIFICATION_FAILED :
                    verified = false;
                    break;
                default :
                    throw new java.security.SignatureException(e);
            }
        }
        resetWhilePreservingInitState();
        return verified;
    }

    @java.lang.Override
    protected final boolean engineVerify(byte[] sigBytes, int offset, int len) throws java.security.SignatureException {
        return engineVerify(android.security.keystore.ArrayUtils.subarray(sigBytes, offset, len));
    }

    @java.lang.Deprecated
    @java.lang.Override
    protected final java.lang.Object engineGetParameter(java.lang.String param) throws java.security.InvalidParameterException {
        throw new java.security.InvalidParameterException();
    }

    @java.lang.Deprecated
    @java.lang.Override
    protected final void engineSetParameter(java.lang.String param, java.lang.Object value) throws java.security.InvalidParameterException {
        throw new java.security.InvalidParameterException();
    }

    protected final android.security.KeyStore getKeyStore() {
        return mKeyStore;
    }

    /**
     * Returns {@code true} if this signature is initialized for signing, {@code false} if this
     * signature is initialized for verification.
     */
    protected final boolean isSigning() {
        return mSigning;
    }

    // The methods below need to be implemented by subclasses.
    /**
     * Returns the amount of additional entropy (in bytes) to be provided to the KeyStore's
     * {@code finish} operation when generating a signature.
     *
     * <p>This value should match (or exceed) the amount of Shannon entropy of the produced
     * signature assuming the key and the message are known. For example, for ECDSA signature this
     * should be the size of {@code R}, whereas for the RSA signature with PKCS#1 padding this
     * should be {@code 0}.
     */
    protected abstract int getAdditionalEntropyAmountForSign();

    /**
     * Invoked to add algorithm-specific parameters for the KeyStore's {@code begin} operation.
     *
     * @param keymasterArgs
     * 		keystore/keymaster arguments to be populated with algorithm-specific
     * 		parameters.
     */
    protected abstract void addAlgorithmSpecificParametersToBegin(@android.annotation.NonNull
    android.security.keymaster.KeymasterArguments keymasterArgs);
}

