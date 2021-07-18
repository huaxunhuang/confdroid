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
 * Properties of <a href="{@docRoot }training/articles/keystore.html">Android Keystore</a> keys.
 */
public abstract class KeyProperties {
    private KeyProperties() {
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef(flag = true, value = { android.security.keystore.KeyProperties.PURPOSE_ENCRYPT, android.security.keystore.KeyProperties.PURPOSE_DECRYPT, android.security.keystore.KeyProperties.PURPOSE_SIGN, android.security.keystore.KeyProperties.PURPOSE_VERIFY })
    public @interface PurposeEnum {}

    /**
     * Purpose of key: encryption.
     */
    public static final int PURPOSE_ENCRYPT = 1 << 0;

    /**
     * Purpose of key: decryption.
     */
    public static final int PURPOSE_DECRYPT = 1 << 1;

    /**
     * Purpose of key: signing or generating a Message Authentication Code (MAC).
     */
    public static final int PURPOSE_SIGN = 1 << 2;

    /**
     * Purpose of key: signature or Message Authentication Code (MAC) verification.
     */
    public static final int PURPOSE_VERIFY = 1 << 3;

    /**
     *
     *
     * @unknown 
     */
    public static abstract class Purpose {
        private Purpose() {
        }

        public static int toKeymaster(@android.security.keystore.KeyProperties.PurposeEnum
        int purpose) {
            switch (purpose) {
                case android.security.keystore.KeyProperties.PURPOSE_ENCRYPT :
                    return android.security.keymaster.KeymasterDefs.KM_PURPOSE_ENCRYPT;
                case android.security.keystore.KeyProperties.PURPOSE_DECRYPT :
                    return android.security.keymaster.KeymasterDefs.KM_PURPOSE_DECRYPT;
                case android.security.keystore.KeyProperties.PURPOSE_SIGN :
                    return android.security.keymaster.KeymasterDefs.KM_PURPOSE_SIGN;
                case android.security.keystore.KeyProperties.PURPOSE_VERIFY :
                    return android.security.keymaster.KeymasterDefs.KM_PURPOSE_VERIFY;
                default :
                    throw new java.lang.IllegalArgumentException("Unknown purpose: " + purpose);
            }
        }

        @android.security.keystore.KeyProperties.PurposeEnum
        public static int fromKeymaster(int purpose) {
            switch (purpose) {
                case android.security.keymaster.KeymasterDefs.KM_PURPOSE_ENCRYPT :
                    return android.security.keystore.KeyProperties.PURPOSE_ENCRYPT;
                case android.security.keymaster.KeymasterDefs.KM_PURPOSE_DECRYPT :
                    return android.security.keystore.KeyProperties.PURPOSE_DECRYPT;
                case android.security.keymaster.KeymasterDefs.KM_PURPOSE_SIGN :
                    return android.security.keystore.KeyProperties.PURPOSE_SIGN;
                case android.security.keymaster.KeymasterDefs.KM_PURPOSE_VERIFY :
                    return android.security.keystore.KeyProperties.PURPOSE_VERIFY;
                default :
                    throw new java.lang.IllegalArgumentException("Unknown purpose: " + purpose);
            }
        }

        @android.annotation.NonNull
        public static int[] allToKeymaster(@android.security.keystore.KeyProperties.PurposeEnum
        int purposes) {
            int[] result = android.security.keystore.KeyProperties.getSetFlags(purposes);
            for (int i = 0; i < result.length; i++) {
                result[i] = android.security.keystore.KeyProperties.Purpose.toKeymaster(result[i]);
            }
            return result;
        }

        @android.security.keystore.KeyProperties.PurposeEnum
        public static int allFromKeymaster(@android.annotation.NonNull
        java.util.Collection<java.lang.Integer> purposes) {
            @android.security.keystore.KeyProperties.PurposeEnum
            int result = 0;
            for (int keymasterPurpose : purposes) {
                result |= android.security.keystore.KeyProperties.Purpose.fromKeymaster(keymasterPurpose);
            }
            return result;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.StringDef({ android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA, android.security.keystore.KeyProperties.KEY_ALGORITHM_EC, android.security.keystore.KeyProperties.KEY_ALGORITHM_AES, android.security.keystore.KeyProperties.KEY_ALGORITHM_HMAC_SHA1, android.security.keystore.KeyProperties.KEY_ALGORITHM_HMAC_SHA224, android.security.keystore.KeyProperties.KEY_ALGORITHM_HMAC_SHA256, android.security.keystore.KeyProperties.KEY_ALGORITHM_HMAC_SHA384, android.security.keystore.KeyProperties.KEY_ALGORITHM_HMAC_SHA512 })
    public @interface KeyAlgorithmEnum {}

    /**
     * Rivest Shamir Adleman (RSA) key.
     */
    public static final java.lang.String KEY_ALGORITHM_RSA = "RSA";

    /**
     * Elliptic Curve (EC) Cryptography key.
     */
    public static final java.lang.String KEY_ALGORITHM_EC = "EC";

    /**
     * Advanced Encryption Standard (AES) key.
     */
    public static final java.lang.String KEY_ALGORITHM_AES = "AES";

    /**
     * Keyed-Hash Message Authentication Code (HMAC) key using SHA-1 as the hash.
     */
    public static final java.lang.String KEY_ALGORITHM_HMAC_SHA1 = "HmacSHA1";

    /**
     * Keyed-Hash Message Authentication Code (HMAC) key using SHA-224 as the hash.
     */
    public static final java.lang.String KEY_ALGORITHM_HMAC_SHA224 = "HmacSHA224";

    /**
     * Keyed-Hash Message Authentication Code (HMAC) key using SHA-256 as the hash.
     */
    public static final java.lang.String KEY_ALGORITHM_HMAC_SHA256 = "HmacSHA256";

    /**
     * Keyed-Hash Message Authentication Code (HMAC) key using SHA-384 as the hash.
     */
    public static final java.lang.String KEY_ALGORITHM_HMAC_SHA384 = "HmacSHA384";

    /**
     * Keyed-Hash Message Authentication Code (HMAC) key using SHA-512 as the hash.
     */
    public static final java.lang.String KEY_ALGORITHM_HMAC_SHA512 = "HmacSHA512";

    /**
     *
     *
     * @unknown 
     */
    public static abstract class KeyAlgorithm {
        private KeyAlgorithm() {
        }

        public static int toKeymasterAsymmetricKeyAlgorithm(@android.annotation.NonNull
        @android.security.keystore.KeyProperties.KeyAlgorithmEnum
        java.lang.String algorithm) {
            if (android.security.keystore.KeyProperties.KEY_ALGORITHM_EC.equalsIgnoreCase(algorithm)) {
                return android.security.keymaster.KeymasterDefs.KM_ALGORITHM_EC;
            } else
                if (android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA.equalsIgnoreCase(algorithm)) {
                    return android.security.keymaster.KeymasterDefs.KM_ALGORITHM_RSA;
                } else {
                    throw new java.lang.IllegalArgumentException("Unsupported key algorithm: " + algorithm);
                }

        }

        @android.annotation.NonNull
        @android.security.keystore.KeyProperties.KeyAlgorithmEnum
        public static java.lang.String fromKeymasterAsymmetricKeyAlgorithm(int keymasterAlgorithm) {
            switch (keymasterAlgorithm) {
                case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_EC :
                    return android.security.keystore.KeyProperties.KEY_ALGORITHM_EC;
                case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_RSA :
                    return android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA;
                default :
                    throw new java.lang.IllegalArgumentException("Unsupported key algorithm: " + keymasterAlgorithm);
            }
        }

        public static int toKeymasterSecretKeyAlgorithm(@android.annotation.NonNull
        @android.security.keystore.KeyProperties.KeyAlgorithmEnum
        java.lang.String algorithm) {
            if (android.security.keystore.KeyProperties.KEY_ALGORITHM_AES.equalsIgnoreCase(algorithm)) {
                return android.security.keymaster.KeymasterDefs.KM_ALGORITHM_AES;
            } else
                if (algorithm.toUpperCase(java.util.Locale.US).startsWith("HMAC")) {
                    return android.security.keymaster.KeymasterDefs.KM_ALGORITHM_HMAC;
                } else {
                    throw new java.lang.IllegalArgumentException("Unsupported secret key algorithm: " + algorithm);
                }

        }

        @android.annotation.NonNull
        @android.security.keystore.KeyProperties.KeyAlgorithmEnum
        public static java.lang.String fromKeymasterSecretKeyAlgorithm(int keymasterAlgorithm, int keymasterDigest) {
            switch (keymasterAlgorithm) {
                case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_AES :
                    return android.security.keystore.KeyProperties.KEY_ALGORITHM_AES;
                case android.security.keymaster.KeymasterDefs.KM_ALGORITHM_HMAC :
                    switch (keymasterDigest) {
                        case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA1 :
                            return android.security.keystore.KeyProperties.KEY_ALGORITHM_HMAC_SHA1;
                        case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_224 :
                            return android.security.keystore.KeyProperties.KEY_ALGORITHM_HMAC_SHA224;
                        case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_256 :
                            return android.security.keystore.KeyProperties.KEY_ALGORITHM_HMAC_SHA256;
                        case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_384 :
                            return android.security.keystore.KeyProperties.KEY_ALGORITHM_HMAC_SHA384;
                        case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_512 :
                            return android.security.keystore.KeyProperties.KEY_ALGORITHM_HMAC_SHA512;
                        default :
                            throw new java.lang.IllegalArgumentException("Unsupported HMAC digest: " + android.security.keystore.KeyProperties.Digest.fromKeymaster(keymasterDigest));
                    }
                default :
                    throw new java.lang.IllegalArgumentException("Unsupported key algorithm: " + keymasterAlgorithm);
            }
        }

        /**
         *
         *
         * @unknown 
         * @return keymaster digest or {@code -1} if the algorithm does not involve a digest.
         */
        public static int toKeymasterDigest(@android.annotation.NonNull
        @android.security.keystore.KeyProperties.KeyAlgorithmEnum
        java.lang.String algorithm) {
            java.lang.String algorithmUpper = algorithm.toUpperCase(java.util.Locale.US);
            if (algorithmUpper.startsWith("HMAC")) {
                java.lang.String digestUpper = algorithmUpper.substring("HMAC".length());
                switch (digestUpper) {
                    case "SHA1" :
                        return android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA1;
                    case "SHA224" :
                        return android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_224;
                    case "SHA256" :
                        return android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_256;
                    case "SHA384" :
                        return android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_384;
                    case "SHA512" :
                        return android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_512;
                    default :
                        throw new java.lang.IllegalArgumentException("Unsupported HMAC digest: " + digestUpper);
                }
            } else {
                return -1;
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.StringDef({ android.security.keystore.KeyProperties.BLOCK_MODE_ECB, android.security.keystore.KeyProperties.BLOCK_MODE_CBC, android.security.keystore.KeyProperties.BLOCK_MODE_CTR, android.security.keystore.KeyProperties.BLOCK_MODE_GCM })
    public @interface BlockModeEnum {}

    /**
     * Electronic Codebook (ECB) block mode.
     */
    public static final java.lang.String BLOCK_MODE_ECB = "ECB";

    /**
     * Cipher Block Chaining (CBC) block mode.
     */
    public static final java.lang.String BLOCK_MODE_CBC = "CBC";

    /**
     * Counter (CTR) block mode.
     */
    public static final java.lang.String BLOCK_MODE_CTR = "CTR";

    /**
     * Galois/Counter Mode (GCM) block mode.
     */
    public static final java.lang.String BLOCK_MODE_GCM = "GCM";

    /**
     *
     *
     * @unknown 
     */
    public static abstract class BlockMode {
        private BlockMode() {
        }

        public static int toKeymaster(@android.annotation.NonNull
        @android.security.keystore.KeyProperties.BlockModeEnum
        java.lang.String blockMode) {
            if (android.security.keystore.KeyProperties.BLOCK_MODE_ECB.equalsIgnoreCase(blockMode)) {
                return android.security.keymaster.KeymasterDefs.KM_MODE_ECB;
            } else
                if (android.security.keystore.KeyProperties.BLOCK_MODE_CBC.equalsIgnoreCase(blockMode)) {
                    return android.security.keymaster.KeymasterDefs.KM_MODE_CBC;
                } else
                    if (android.security.keystore.KeyProperties.BLOCK_MODE_CTR.equalsIgnoreCase(blockMode)) {
                        return android.security.keymaster.KeymasterDefs.KM_MODE_CTR;
                    } else
                        if (android.security.keystore.KeyProperties.BLOCK_MODE_GCM.equalsIgnoreCase(blockMode)) {
                            return android.security.keymaster.KeymasterDefs.KM_MODE_GCM;
                        } else {
                            throw new java.lang.IllegalArgumentException("Unsupported block mode: " + blockMode);
                        }



        }

        @android.annotation.NonNull
        @android.security.keystore.KeyProperties.BlockModeEnum
        public static java.lang.String fromKeymaster(int blockMode) {
            switch (blockMode) {
                case android.security.keymaster.KeymasterDefs.KM_MODE_ECB :
                    return android.security.keystore.KeyProperties.BLOCK_MODE_ECB;
                case android.security.keymaster.KeymasterDefs.KM_MODE_CBC :
                    return android.security.keystore.KeyProperties.BLOCK_MODE_CBC;
                case android.security.keymaster.KeymasterDefs.KM_MODE_CTR :
                    return android.security.keystore.KeyProperties.BLOCK_MODE_CTR;
                case android.security.keymaster.KeymasterDefs.KM_MODE_GCM :
                    return android.security.keystore.KeyProperties.BLOCK_MODE_GCM;
                default :
                    throw new java.lang.IllegalArgumentException("Unsupported block mode: " + blockMode);
            }
        }

        @android.annotation.NonNull
        @android.security.keystore.KeyProperties.BlockModeEnum
        public static java.lang.String[] allFromKeymaster(@android.annotation.NonNull
        java.util.Collection<java.lang.Integer> blockModes) {
            if ((blockModes == null) || blockModes.isEmpty()) {
                return libcore.util.EmptyArray.STRING;
            }
            @android.security.keystore.KeyProperties.BlockModeEnum
            java.lang.String[] result = new java.lang.String[blockModes.size()];
            int offset = 0;
            for (int blockMode : blockModes) {
                result[offset] = android.security.keystore.KeyProperties.BlockMode.fromKeymaster(blockMode);
                offset++;
            }
            return result;
        }

        public static int[] allToKeymaster(@android.annotation.Nullable
        @android.security.keystore.KeyProperties.BlockModeEnum
        java.lang.String[] blockModes) {
            if ((blockModes == null) || (blockModes.length == 0)) {
                return libcore.util.EmptyArray.INT;
            }
            int[] result = new int[blockModes.length];
            for (int i = 0; i < blockModes.length; i++) {
                result[i] = android.security.keystore.KeyProperties.BlockMode.toKeymaster(blockModes[i]);
            }
            return result;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.StringDef({ android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE, android.security.keystore.KeyProperties.ENCRYPTION_PADDING_PKCS7, android.security.keystore.KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1, android.security.keystore.KeyProperties.ENCRYPTION_PADDING_RSA_OAEP })
    public @interface EncryptionPaddingEnum {}

    /**
     * No encryption padding.
     */
    public static final java.lang.String ENCRYPTION_PADDING_NONE = "NoPadding";

    /**
     * PKCS#7 encryption padding scheme.
     */
    public static final java.lang.String ENCRYPTION_PADDING_PKCS7 = "PKCS7Padding";

    /**
     * RSA PKCS#1 v1.5 padding scheme for encryption.
     */
    public static final java.lang.String ENCRYPTION_PADDING_RSA_PKCS1 = "PKCS1Padding";

    /**
     * RSA Optimal Asymmetric Encryption Padding (OAEP) scheme.
     */
    public static final java.lang.String ENCRYPTION_PADDING_RSA_OAEP = "OAEPPadding";

    /**
     *
     *
     * @unknown 
     */
    public static abstract class EncryptionPadding {
        private EncryptionPadding() {
        }

        public static int toKeymaster(@android.annotation.NonNull
        @android.security.keystore.KeyProperties.EncryptionPaddingEnum
        java.lang.String padding) {
            if (android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE.equalsIgnoreCase(padding)) {
                return android.security.keymaster.KeymasterDefs.KM_PAD_NONE;
            } else
                if (android.security.keystore.KeyProperties.ENCRYPTION_PADDING_PKCS7.equalsIgnoreCase(padding)) {
                    return android.security.keymaster.KeymasterDefs.KM_PAD_PKCS7;
                } else
                    if (android.security.keystore.KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1.equalsIgnoreCase(padding)) {
                        return android.security.keymaster.KeymasterDefs.KM_PAD_RSA_PKCS1_1_5_ENCRYPT;
                    } else
                        if (android.security.keystore.KeyProperties.ENCRYPTION_PADDING_RSA_OAEP.equalsIgnoreCase(padding)) {
                            return android.security.keymaster.KeymasterDefs.KM_PAD_RSA_OAEP;
                        } else {
                            throw new java.lang.IllegalArgumentException("Unsupported encryption padding scheme: " + padding);
                        }



        }

        @android.annotation.NonNull
        @android.security.keystore.KeyProperties.EncryptionPaddingEnum
        public static java.lang.String fromKeymaster(int padding) {
            switch (padding) {
                case android.security.keymaster.KeymasterDefs.KM_PAD_NONE :
                    return android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE;
                case android.security.keymaster.KeymasterDefs.KM_PAD_PKCS7 :
                    return android.security.keystore.KeyProperties.ENCRYPTION_PADDING_PKCS7;
                case android.security.keymaster.KeymasterDefs.KM_PAD_RSA_PKCS1_1_5_ENCRYPT :
                    return android.security.keystore.KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1;
                case android.security.keymaster.KeymasterDefs.KM_PAD_RSA_OAEP :
                    return android.security.keystore.KeyProperties.ENCRYPTION_PADDING_RSA_OAEP;
                default :
                    throw new java.lang.IllegalArgumentException("Unsupported encryption padding: " + padding);
            }
        }

        @android.annotation.NonNull
        public static int[] allToKeymaster(@android.annotation.Nullable
        @android.security.keystore.KeyProperties.EncryptionPaddingEnum
        java.lang.String[] paddings) {
            if ((paddings == null) || (paddings.length == 0)) {
                return libcore.util.EmptyArray.INT;
            }
            int[] result = new int[paddings.length];
            for (int i = 0; i < paddings.length; i++) {
                result[i] = android.security.keystore.KeyProperties.EncryptionPadding.toKeymaster(paddings[i]);
            }
            return result;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.StringDef({ android.security.keystore.KeyProperties.SIGNATURE_PADDING_RSA_PKCS1, android.security.keystore.KeyProperties.SIGNATURE_PADDING_RSA_PSS })
    public @interface SignaturePaddingEnum {}

    /**
     * RSA PKCS#1 v1.5 padding for signatures.
     */
    public static final java.lang.String SIGNATURE_PADDING_RSA_PKCS1 = "PKCS1";

    /**
     * RSA PKCS#1 v2.1 Probabilistic Signature Scheme (PSS) padding.
     */
    public static final java.lang.String SIGNATURE_PADDING_RSA_PSS = "PSS";

    static abstract class SignaturePadding {
        private SignaturePadding() {
        }

        static int toKeymaster(@android.annotation.NonNull
        @android.security.keystore.KeyProperties.SignaturePaddingEnum
        java.lang.String padding) {
            switch (padding.toUpperCase(java.util.Locale.US)) {
                case android.security.keystore.KeyProperties.SIGNATURE_PADDING_RSA_PKCS1 :
                    return android.security.keymaster.KeymasterDefs.KM_PAD_RSA_PKCS1_1_5_SIGN;
                case android.security.keystore.KeyProperties.SIGNATURE_PADDING_RSA_PSS :
                    return android.security.keymaster.KeymasterDefs.KM_PAD_RSA_PSS;
                default :
                    throw new java.lang.IllegalArgumentException("Unsupported signature padding scheme: " + padding);
            }
        }

        @android.annotation.NonNull
        @android.security.keystore.KeyProperties.SignaturePaddingEnum
        static java.lang.String fromKeymaster(int padding) {
            switch (padding) {
                case android.security.keymaster.KeymasterDefs.KM_PAD_RSA_PKCS1_1_5_SIGN :
                    return android.security.keystore.KeyProperties.SIGNATURE_PADDING_RSA_PKCS1;
                case android.security.keymaster.KeymasterDefs.KM_PAD_RSA_PSS :
                    return android.security.keystore.KeyProperties.SIGNATURE_PADDING_RSA_PSS;
                default :
                    throw new java.lang.IllegalArgumentException("Unsupported signature padding: " + padding);
            }
        }

        @android.annotation.NonNull
        static int[] allToKeymaster(@android.annotation.Nullable
        @android.security.keystore.KeyProperties.SignaturePaddingEnum
        java.lang.String[] paddings) {
            if ((paddings == null) || (paddings.length == 0)) {
                return libcore.util.EmptyArray.INT;
            }
            int[] result = new int[paddings.length];
            for (int i = 0; i < paddings.length; i++) {
                result[i] = android.security.keystore.KeyProperties.SignaturePadding.toKeymaster(paddings[i]);
            }
            return result;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.StringDef({ android.security.keystore.KeyProperties.DIGEST_NONE, android.security.keystore.KeyProperties.DIGEST_MD5, android.security.keystore.KeyProperties.DIGEST_SHA1, android.security.keystore.KeyProperties.DIGEST_SHA224, android.security.keystore.KeyProperties.DIGEST_SHA256, android.security.keystore.KeyProperties.DIGEST_SHA384, android.security.keystore.KeyProperties.DIGEST_SHA512 })
    public @interface DigestEnum {}

    /**
     * No digest: sign/authenticate the raw message.
     */
    public static final java.lang.String DIGEST_NONE = "NONE";

    /**
     * MD5 digest.
     */
    public static final java.lang.String DIGEST_MD5 = "MD5";

    /**
     * SHA-1 digest.
     */
    public static final java.lang.String DIGEST_SHA1 = "SHA-1";

    /**
     * SHA-2 224 (aka SHA-224) digest.
     */
    public static final java.lang.String DIGEST_SHA224 = "SHA-224";

    /**
     * SHA-2 256 (aka SHA-256) digest.
     */
    public static final java.lang.String DIGEST_SHA256 = "SHA-256";

    /**
     * SHA-2 384 (aka SHA-384) digest.
     */
    public static final java.lang.String DIGEST_SHA384 = "SHA-384";

    /**
     * SHA-2 512 (aka SHA-512) digest.
     */
    public static final java.lang.String DIGEST_SHA512 = "SHA-512";

    /**
     *
     *
     * @unknown 
     */
    public static abstract class Digest {
        private Digest() {
        }

        public static int toKeymaster(@android.annotation.NonNull
        @android.security.keystore.KeyProperties.DigestEnum
        java.lang.String digest) {
            switch (digest.toUpperCase(java.util.Locale.US)) {
                case android.security.keystore.KeyProperties.DIGEST_SHA1 :
                    return android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA1;
                case android.security.keystore.KeyProperties.DIGEST_SHA224 :
                    return android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_224;
                case android.security.keystore.KeyProperties.DIGEST_SHA256 :
                    return android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_256;
                case android.security.keystore.KeyProperties.DIGEST_SHA384 :
                    return android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_384;
                case android.security.keystore.KeyProperties.DIGEST_SHA512 :
                    return android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_512;
                case android.security.keystore.KeyProperties.DIGEST_NONE :
                    return android.security.keymaster.KeymasterDefs.KM_DIGEST_NONE;
                case android.security.keystore.KeyProperties.DIGEST_MD5 :
                    return android.security.keymaster.KeymasterDefs.KM_DIGEST_MD5;
                default :
                    throw new java.lang.IllegalArgumentException("Unsupported digest algorithm: " + digest);
            }
        }

        @android.annotation.NonNull
        @android.security.keystore.KeyProperties.DigestEnum
        public static java.lang.String fromKeymaster(int digest) {
            switch (digest) {
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_NONE :
                    return android.security.keystore.KeyProperties.DIGEST_NONE;
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_MD5 :
                    return android.security.keystore.KeyProperties.DIGEST_MD5;
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA1 :
                    return android.security.keystore.KeyProperties.DIGEST_SHA1;
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_224 :
                    return android.security.keystore.KeyProperties.DIGEST_SHA224;
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_256 :
                    return android.security.keystore.KeyProperties.DIGEST_SHA256;
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_384 :
                    return android.security.keystore.KeyProperties.DIGEST_SHA384;
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_512 :
                    return android.security.keystore.KeyProperties.DIGEST_SHA512;
                default :
                    throw new java.lang.IllegalArgumentException("Unsupported digest algorithm: " + digest);
            }
        }

        @android.annotation.NonNull
        @android.security.keystore.KeyProperties.DigestEnum
        public static java.lang.String fromKeymasterToSignatureAlgorithmDigest(int digest) {
            switch (digest) {
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_NONE :
                    return "NONE";
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_MD5 :
                    return "MD5";
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA1 :
                    return "SHA1";
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_224 :
                    return "SHA224";
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_256 :
                    return "SHA256";
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_384 :
                    return "SHA384";
                case android.security.keymaster.KeymasterDefs.KM_DIGEST_SHA_2_512 :
                    return "SHA512";
                default :
                    throw new java.lang.IllegalArgumentException("Unsupported digest algorithm: " + digest);
            }
        }

        @android.annotation.NonNull
        @android.security.keystore.KeyProperties.DigestEnum
        public static java.lang.String[] allFromKeymaster(@android.annotation.NonNull
        java.util.Collection<java.lang.Integer> digests) {
            if (digests.isEmpty()) {
                return libcore.util.EmptyArray.STRING;
            }
            java.lang.String[] result = new java.lang.String[digests.size()];
            int offset = 0;
            for (int digest : digests) {
                result[offset] = android.security.keystore.KeyProperties.Digest.fromKeymaster(digest);
                offset++;
            }
            return result;
        }

        @android.annotation.NonNull
        public static int[] allToKeymaster(@android.annotation.Nullable
        @android.security.keystore.KeyProperties.DigestEnum
        java.lang.String[] digests) {
            if ((digests == null) || (digests.length == 0)) {
                return libcore.util.EmptyArray.INT;
            }
            int[] result = new int[digests.length];
            int offset = 0;
            for (@android.security.keystore.KeyProperties.DigestEnum
            java.lang.String digest : digests) {
                result[offset] = android.security.keystore.KeyProperties.Digest.toKeymaster(digest);
                offset++;
            }
            return result;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.security.keystore.KeyProperties.ORIGIN_GENERATED, android.security.keystore.KeyProperties.ORIGIN_IMPORTED, android.security.keystore.KeyProperties.ORIGIN_UNKNOWN })
    public @interface OriginEnum {}

    /**
     * Key was generated inside AndroidKeyStore.
     */
    public static final int ORIGIN_GENERATED = 1 << 0;

    /**
     * Key was imported into AndroidKeyStore.
     */
    public static final int ORIGIN_IMPORTED = 1 << 1;

    /**
     * Origin of the key is unknown. This can occur only for keys backed by an old TEE-backed
     * implementation which does not record origin information.
     */
    public static final int ORIGIN_UNKNOWN = 1 << 2;

    /**
     *
     *
     * @unknown 
     */
    public static abstract class Origin {
        private Origin() {
        }

        @android.security.keystore.KeyProperties.OriginEnum
        public static int fromKeymaster(int origin) {
            switch (origin) {
                case android.security.keymaster.KeymasterDefs.KM_ORIGIN_GENERATED :
                    return android.security.keystore.KeyProperties.ORIGIN_GENERATED;
                case android.security.keymaster.KeymasterDefs.KM_ORIGIN_IMPORTED :
                    return android.security.keystore.KeyProperties.ORIGIN_IMPORTED;
                case android.security.keymaster.KeymasterDefs.KM_ORIGIN_UNKNOWN :
                    return android.security.keystore.KeyProperties.ORIGIN_UNKNOWN;
                default :
                    throw new java.lang.IllegalArgumentException("Unknown origin: " + origin);
            }
        }
    }

    private static int[] getSetFlags(int flags) {
        if (flags == 0) {
            return libcore.util.EmptyArray.INT;
        }
        int[] result = new int[android.security.keystore.KeyProperties.getSetBitCount(flags)];
        int resultOffset = 0;
        int flag = 1;
        while (flags != 0) {
            if ((flags & 1) != 0) {
                result[resultOffset] = flag;
                resultOffset++;
            }
            flags >>>= 1;
            flag <<= 1;
        } 
        return result;
    }

    private static int getSetBitCount(int value) {
        if (value == 0) {
            return 0;
        }
        int result = 0;
        while (value != 0) {
            if ((value & 1) != 0) {
                result++;
            }
            value >>>= 1;
        } 
        return result;
    }
}

