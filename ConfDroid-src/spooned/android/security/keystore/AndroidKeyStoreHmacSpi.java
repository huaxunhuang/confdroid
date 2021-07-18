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
 * {@link MacSpi} which provides HMAC implementations backed by Android KeyStore.
 *
 * @unknown 
 */
public abstract class AndroidKeyStoreHmacSpi extends javax.crypto.MacSpi implements android.security.keystore.KeyStoreCryptoOperation {
    public static class HmacSHA1 extends android.security.keystore.AndroidKeyStoreHmacSpi {
        public HmacSHA1() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA1);
        }
    }

    public static class HmacSHA224 extends android.security.keystore.AndroidKeyStoreHmacSpi {
        public HmacSHA224() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_224);
        }
    }

    public static class HmacSHA256 extends android.security.keystore.AndroidKeyStoreHmacSpi {
        public HmacSHA256() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_256);
        }
    }

    public static class HmacSHA384 extends android.security.keystore.AndroidKeyStoreHmacSpi {
        public HmacSHA384() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_384);
        }
    }

    public static class HmacSHA512 extends android.security.keystore.AndroidKeyStoreHmacSpi {
        public HmacSHA512() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_512);
        }
    }

    private final android.security.KeyStore mKeyStore = android.security.KeyStore.getInstance();

    private final int mKeymasterDigest;

    private final int mMacSizeBits;

    // Fields below are populated by engineInit and should be preserved after engineDoFinal.
    private android.security.keystore.AndroidKeyStoreSecretKey mKey;

    // Fields below are reset when engineDoFinal succeeds.
    private android.security.keystore.KeyStoreCryptoOperationChunkedStreamer mChunkedStreamer;

    private android.os.IBinder mOperationToken;

    private long mOperationHandle;

    protected AndroidKeyStoreHmacSpi(int keymasterDigest) {
        mKeymasterDigest = keymasterDigest;
        mMacSizeBits = android.security.keystore.KeymasterUtils.getDigestOutputSizeBits(keymasterDigest);
    }

    @java.lang.Override
    protected int engineGetMacLength() {
        return (mMacSizeBits + 7) / 8;
    }

    @java.lang.Override
    protected void engineInit(java.security.Key key, java.security.spec.AlgorithmParameterSpec params) throws java.security.InvalidAlgorithmParameterException, java.security.InvalidKeyException {
        resetAll();
        boolean success = false;
        try {
            init(key, params);
            ensureKeystoreOperationInitialized();
            success = true;
        } finally {
            if (!success) {
                resetAll();
            }
        }
    }

    private void init(java.security.Key key, java.security.spec.AlgorithmParameterSpec params) throws java.security.InvalidAlgorithmParameterException, java.security.InvalidKeyException {
        if (key == null) {
            throw new java.security.InvalidKeyException("key == null");
        } else
            if (!(key instanceof android.security.keystore.AndroidKeyStoreSecretKey)) {
                throw new java.security.InvalidKeyException("Only Android KeyStore secret keys supported. Key: " + key);
            }

        mKey = ((android.security.keystore.AndroidKeyStoreSecretKey) (key));
        if (params != null) {
            throw new java.security.InvalidAlgorithmParameterException("Unsupported algorithm parameters: " + params);
        }
    }

    private void resetAll() {
        mKey = null;
        android.os.IBinder operationToken = mOperationToken;
        if (operationToken != null) {
            mKeyStore.abort(operationToken);
        }
        mOperationToken = null;
        mOperationHandle = 0;
        mChunkedStreamer = null;
    }

    private void resetWhilePreservingInitState() {
        android.os.IBinder operationToken = mOperationToken;
        if (operationToken != null) {
            mKeyStore.abort(operationToken);
        }
        mOperationToken = null;
        mOperationHandle = 0;
        mChunkedStreamer = null;
    }

    @java.lang.Override
    protected void engineReset() {
        resetWhilePreservingInitState();
    }

    private void ensureKeystoreOperationInitialized() throws java.security.InvalidKeyException {
        if (mChunkedStreamer != null) {
            return;
        }
        if (mKey == null) {
            throw new java.lang.IllegalStateException("Not initialized");
        }
        android.security.keymaster.KeymasterArguments keymasterArgs = new android.security.keymaster.KeymasterArguments();
        keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_ALGORITHM, android.security.keymaster.KeymasterDefs.KM_ALGORITHM_HMAC);
        keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_DIGEST, mKeymasterDigest);
        keymasterArgs.addUnsignedInt(android.security.keymaster.KeymasterDefs.KM_TAG_MAC_LENGTH, mMacSizeBits);
        android.security.keymaster.OperationResult opResult = // no additional entropy needed for HMAC because it's deterministic
        mKeyStore.begin(mKey.getAlias(), android.security.keymaster.KeymasterDefs.KM_PURPOSE_SIGN, true, keymasterArgs, null, mKey.getUid());
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
        mChunkedStreamer = new android.security.keystore.KeyStoreCryptoOperationChunkedStreamer(new android.security.keystore.KeyStoreCryptoOperationChunkedStreamer.MainDataStream(mKeyStore, mOperationToken));
    }

    @java.lang.Override
    protected void engineUpdate(byte input) {
        engineUpdate(new byte[]{ input }, 0, 1);
    }

    @java.lang.Override
    protected void engineUpdate(byte[] input, int offset, int len) {
        try {
            ensureKeystoreOperationInitialized();
        } catch (java.security.InvalidKeyException e) {
            throw new java.security.ProviderException("Failed to reinitialize MAC", e);
        }
        byte[] output;
        try {
            output = mChunkedStreamer.update(input, offset, len);
        } catch (android.security.KeyStoreException e) {
            throw new java.security.ProviderException("Keystore operation failed", e);
        }
        if ((output != null) && (output.length != 0)) {
            throw new java.security.ProviderException("Update operation unexpectedly produced output");
        }
    }

    @java.lang.Override
    protected byte[] engineDoFinal() {
        try {
            ensureKeystoreOperationInitialized();
        } catch (java.security.InvalidKeyException e) {
            throw new java.security.ProviderException("Failed to reinitialize MAC", e);
        }
        byte[] result;
        try {
            result = // no signature provided -- this invocation will generate one
            // no additional entropy needed -- HMAC is deterministic
            mChunkedStreamer.doFinal(null, 0, 0, null, null);
        } catch (android.security.KeyStoreException e) {
            throw new java.security.ProviderException("Keystore operation failed", e);
        }
        resetWhilePreservingInitState();
        return result;
    }

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
    public long getOperationHandle() {
        return mOperationHandle;
    }
}

