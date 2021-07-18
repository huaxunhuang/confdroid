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
 * Base class for {@link SignatureSpi} providing Android KeyStore backed ECDSA signatures.
 *
 * @unknown 
 */
abstract class AndroidKeyStoreECDSASignatureSpi extends android.security.keystore.AndroidKeyStoreSignatureSpiBase {
    public static final class NONE extends android.security.keystore.AndroidKeyStoreECDSASignatureSpi {
        public NONE() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_NONE);
        }

        @java.lang.Override
        protected android.security.keystore.KeyStoreCryptoOperationStreamer createMainDataStreamer(android.security.KeyStore keyStore, android.os.IBinder operationToken) {
            return new android.security.keystore.AndroidKeyStoreECDSASignatureSpi.NONE.TruncateToFieldSizeMessageStreamer(super.createMainDataStreamer(keyStore, operationToken), getGroupSizeBits());
        }

        /**
         * Streamer which buffers all input, then truncates it to field size, and then sends it into
         * KeyStore via the provided delegate streamer.
         */
        private static class TruncateToFieldSizeMessageStreamer implements android.security.keystore.KeyStoreCryptoOperationStreamer {
            private final android.security.keystore.KeyStoreCryptoOperationStreamer mDelegate;

            private final int mGroupSizeBits;

            private final java.io.ByteArrayOutputStream mInputBuffer = new java.io.ByteArrayOutputStream();

            private long mConsumedInputSizeBytes;

            private TruncateToFieldSizeMessageStreamer(android.security.keystore.KeyStoreCryptoOperationStreamer delegate, int groupSizeBits) {
                mDelegate = delegate;
                mGroupSizeBits = groupSizeBits;
            }

            @java.lang.Override
            public byte[] update(byte[] input, int inputOffset, int inputLength) throws android.security.KeyStoreException {
                if (inputLength > 0) {
                    mInputBuffer.write(input, inputOffset, inputLength);
                    mConsumedInputSizeBytes += inputLength;
                }
                return libcore.util.EmptyArray.BYTE;
            }

            @java.lang.Override
            public byte[] doFinal(byte[] input, int inputOffset, int inputLength, byte[] signature, byte[] additionalEntropy) throws android.security.KeyStoreException {
                if (inputLength > 0) {
                    mConsumedInputSizeBytes += inputLength;
                    mInputBuffer.write(input, inputOffset, inputLength);
                }
                byte[] bufferedInput = mInputBuffer.toByteArray();
                mInputBuffer.reset();
                // Truncate input at field size (bytes)
                return mDelegate.doFinal(bufferedInput, 0, java.lang.Math.min(bufferedInput.length, (mGroupSizeBits + 7) / 8), signature, additionalEntropy);
            }

            @java.lang.Override
            public long getConsumedInputSizeBytes() {
                return mConsumedInputSizeBytes;
            }

            @java.lang.Override
            public long getProducedOutputSizeBytes() {
                return mDelegate.getProducedOutputSizeBytes();
            }
        }
    }

    public static final class SHA1 extends android.security.keystore.AndroidKeyStoreECDSASignatureSpi {
        public SHA1() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA1);
        }
    }

    public static final class SHA224 extends android.security.keystore.AndroidKeyStoreECDSASignatureSpi {
        public SHA224() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_224);
        }
    }

    public static final class SHA256 extends android.security.keystore.AndroidKeyStoreECDSASignatureSpi {
        public SHA256() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_256);
        }
    }

    public static final class SHA384 extends android.security.keystore.AndroidKeyStoreECDSASignatureSpi {
        public SHA384() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_384);
        }
    }

    public static final class SHA512 extends android.security.keystore.AndroidKeyStoreECDSASignatureSpi {
        public SHA512() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_512);
        }
    }

    private final int mKeymasterDigest;

    private int mGroupSizeBits = -1;

    AndroidKeyStoreECDSASignatureSpi(int keymasterDigest) {
        mKeymasterDigest = keymasterDigest;
    }

    @java.lang.Override
    protected final void initKey(android.security.keystore.AndroidKeyStoreKey key) throws java.security.InvalidKeyException {
        if (!android.security.keystore.KeyProperties.KEY_ALGORITHM_EC.equalsIgnoreCase(key.getAlgorithm())) {
            throw new java.security.InvalidKeyException(((("Unsupported key algorithm: " + key.getAlgorithm()) + ". Only") + android.security.keystore.KeyProperties.KEY_ALGORITHM_EC) + " supported");
        }
        android.security.keymaster.KeyCharacteristics keyCharacteristics = new android.security.keymaster.KeyCharacteristics();
        int errorCode = getKeyStore().getKeyCharacteristics(key.getAlias(), null, null, key.getUid(), keyCharacteristics);
        if (errorCode != android.security.KeyStore.NO_ERROR) {
            throw getKeyStore().getInvalidKeyException(key.getAlias(), key.getUid(), errorCode);
        }
        long keySizeBits = keyCharacteristics.getUnsignedInt(android.security.keymaster.KeymasterDefs.KM_TAG_KEY_SIZE, -1);
        if (keySizeBits == (-1)) {
            throw new java.security.InvalidKeyException("Size of key not known");
        } else
            if (keySizeBits > java.lang.Integer.MAX_VALUE) {
                throw new java.security.InvalidKeyException(("Key too large: " + keySizeBits) + " bits");
            }

        mGroupSizeBits = ((int) (keySizeBits));
        super.initKey(key);
    }

    @java.lang.Override
    protected final void resetAll() {
        mGroupSizeBits = -1;
        super.resetAll();
    }

    @java.lang.Override
    protected final void resetWhilePreservingInitState() {
        super.resetWhilePreservingInitState();
    }

    @java.lang.Override
    protected final void addAlgorithmSpecificParametersToBegin(@android.annotation.NonNull
    android.security.keymaster.KeymasterArguments keymasterArgs) {
        keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_ALGORITHM, android.security.keymaster.KeymasterDefs.KM_ALGORITHM_EC);
        keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_DIGEST, mKeymasterDigest);
    }

    @java.lang.Override
    protected final int getAdditionalEntropyAmountForSign() {
        return (mGroupSizeBits + 7) / 8;
    }

    protected final int getGroupSizeBits() {
        if (mGroupSizeBits == (-1)) {
            throw new java.lang.IllegalStateException("Not initialized");
        }
        return mGroupSizeBits;
    }
}

