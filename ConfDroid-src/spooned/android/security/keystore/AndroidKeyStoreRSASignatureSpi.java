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
 * Base class for {@link SignatureSpi} providing Android KeyStore backed RSA signatures.
 *
 * @unknown 
 */
abstract class AndroidKeyStoreRSASignatureSpi extends android.security.keystore.AndroidKeyStoreSignatureSpiBase {
    static abstract class PKCS1Padding extends android.security.keystore.AndroidKeyStoreRSASignatureSpi {
        PKCS1Padding(int keymasterDigest) {
            super(keymasterDigest, android.security.keymaster.KeymasterDefs.KM_PAD_RSA_PKCS1_1_5_SIGN);
        }

        @java.lang.Override
        protected final int getAdditionalEntropyAmountForSign() {
            // No entropy required for this deterministic signature scheme.
            return 0;
        }
    }

    public static final class NONEWithPKCS1Padding extends android.security.keystore.AndroidKeyStoreRSASignatureSpi.PKCS1Padding {
        public NONEWithPKCS1Padding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_NONE);
        }
    }

    public static final class MD5WithPKCS1Padding extends android.security.keystore.AndroidKeyStoreRSASignatureSpi.PKCS1Padding {
        public MD5WithPKCS1Padding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_MD5);
        }
    }

    public static final class SHA1WithPKCS1Padding extends android.security.keystore.AndroidKeyStoreRSASignatureSpi.PKCS1Padding {
        public SHA1WithPKCS1Padding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA1);
        }
    }

    public static final class SHA224WithPKCS1Padding extends android.security.keystore.AndroidKeyStoreRSASignatureSpi.PKCS1Padding {
        public SHA224WithPKCS1Padding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_224);
        }
    }

    public static final class SHA256WithPKCS1Padding extends android.security.keystore.AndroidKeyStoreRSASignatureSpi.PKCS1Padding {
        public SHA256WithPKCS1Padding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_256);
        }
    }

    public static final class SHA384WithPKCS1Padding extends android.security.keystore.AndroidKeyStoreRSASignatureSpi.PKCS1Padding {
        public SHA384WithPKCS1Padding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_384);
        }
    }

    public static final class SHA512WithPKCS1Padding extends android.security.keystore.AndroidKeyStoreRSASignatureSpi.PKCS1Padding {
        public SHA512WithPKCS1Padding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_512);
        }
    }

    static abstract class PSSPadding extends android.security.keystore.AndroidKeyStoreRSASignatureSpi {
        private static final int SALT_LENGTH_BYTES = 20;

        PSSPadding(int keymasterDigest) {
            super(keymasterDigest, android.security.keymaster.KeymasterDefs.KM_PAD_RSA_PSS);
        }

        @java.lang.Override
        protected final int getAdditionalEntropyAmountForSign() {
            return android.security.keystore.AndroidKeyStoreRSASignatureSpi.PSSPadding.SALT_LENGTH_BYTES;
        }
    }

    public static final class SHA1WithPSSPadding extends android.security.keystore.AndroidKeyStoreRSASignatureSpi.PSSPadding {
        public SHA1WithPSSPadding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA1);
        }
    }

    public static final class SHA224WithPSSPadding extends android.security.keystore.AndroidKeyStoreRSASignatureSpi.PSSPadding {
        public SHA224WithPSSPadding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_224);
        }
    }

    public static final class SHA256WithPSSPadding extends android.security.keystore.AndroidKeyStoreRSASignatureSpi.PSSPadding {
        public SHA256WithPSSPadding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_256);
        }
    }

    public static final class SHA384WithPSSPadding extends android.security.keystore.AndroidKeyStoreRSASignatureSpi.PSSPadding {
        public SHA384WithPSSPadding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_384);
        }
    }

    public static final class SHA512WithPSSPadding extends android.security.keystore.AndroidKeyStoreRSASignatureSpi.PSSPadding {
        public SHA512WithPSSPadding() {
            super(android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_512);
        }
    }

    private final int mKeymasterDigest;

    private final int mKeymasterPadding;

    AndroidKeyStoreRSASignatureSpi(int keymasterDigest, int keymasterPadding) {
        mKeymasterDigest = keymasterDigest;
        mKeymasterPadding = keymasterPadding;
    }

    @java.lang.Override
    protected final void initKey(android.security.keystore.AndroidKeyStoreKey key) throws java.security.InvalidKeyException {
        if (!android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA.equalsIgnoreCase(key.getAlgorithm())) {
            throw new java.security.InvalidKeyException(((("Unsupported key algorithm: " + key.getAlgorithm()) + ". Only") + android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA) + " supported");
        }
        super.initKey(key);
    }

    @java.lang.Override
    protected final void resetAll() {
        super.resetAll();
    }

    @java.lang.Override
    protected final void resetWhilePreservingInitState() {
        super.resetWhilePreservingInitState();
    }

    @java.lang.Override
    protected final void addAlgorithmSpecificParametersToBegin(@android.annotation.NonNull
    android.security.keymaster.KeymasterArguments keymasterArgs) {
        keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_ALGORITHM, android.security.keymaster.KeymasterDefs.KM_ALGORITHM_RSA);
        keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_DIGEST, mKeymasterDigest);
        keymasterArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_PADDING, mKeymasterPadding);
    }
}

