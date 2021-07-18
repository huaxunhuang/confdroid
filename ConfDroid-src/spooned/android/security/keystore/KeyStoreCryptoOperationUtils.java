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
 * Assorted utility methods for implementing crypto operations on top of KeyStore.
 *
 * @unknown 
 */
abstract class KeyStoreCryptoOperationUtils {
    private static volatile java.security.SecureRandom sRng;

    private KeyStoreCryptoOperationUtils() {
    }

    /**
     * Returns the {@link InvalidKeyException} to be thrown by the {@code init} method of
     * the crypto operation in response to {@code KeyStore.begin} operation or {@code null} if
     * the {@code init} method should succeed.
     */
    static java.security.InvalidKeyException getInvalidKeyExceptionForInit(android.security.KeyStore keyStore, android.security.keystore.AndroidKeyStoreKey key, int beginOpResultCode) {
        if (beginOpResultCode == android.security.KeyStore.NO_ERROR) {
            return null;
        }
        // An error occured. However, some errors should not lead to init throwing an exception.
        // See below.
        java.security.InvalidKeyException e = keyStore.getInvalidKeyException(key.getAlias(), key.getUid(), beginOpResultCode);
        switch (beginOpResultCode) {
            case android.security.KeyStore.OP_AUTH_NEEDED :
                // Operation needs to be authorized by authenticating the user. Don't throw an
                // exception is such authentication is possible for this key
                // (UserNotAuthenticatedException). An example of when it's not possible is where
                // the key is permanently invalidated (KeyPermanentlyInvalidatedException).
                if (e instanceof android.security.keystore.UserNotAuthenticatedException) {
                    return null;
                }
                break;
        }
        return e;
    }

    /**
     * Returns the exception to be thrown by the {@code Cipher.init} method of the crypto operation
     * in response to {@code KeyStore.begin} operation or {@code null} if the {@code init} method
     * should succeed.
     */
    public static java.security.GeneralSecurityException getExceptionForCipherInit(android.security.KeyStore keyStore, android.security.keystore.AndroidKeyStoreKey key, int beginOpResultCode) {
        if (beginOpResultCode == android.security.KeyStore.NO_ERROR) {
            return null;
        }
        // Cipher-specific cases
        switch (beginOpResultCode) {
            case android.security.keymaster.KeymasterDefs.KM_ERROR_INVALID_NONCE :
                return new java.security.InvalidAlgorithmParameterException("Invalid IV");
            case android.security.keymaster.KeymasterDefs.KM_ERROR_CALLER_NONCE_PROHIBITED :
                return new java.security.InvalidAlgorithmParameterException("Caller-provided IV not permitted");
        }
        // General cases
        return android.security.keystore.KeyStoreCryptoOperationUtils.getInvalidKeyExceptionForInit(keyStore, key, beginOpResultCode);
    }

    /**
     * Returns the requested number of random bytes to mix into keystore/keymaster RNG.
     *
     * @param rng
     * 		RNG from which to obtain the random bytes or {@code null} for the platform-default
     * 		RNG.
     */
    static byte[] getRandomBytesToMixIntoKeystoreRng(java.security.SecureRandom rng, int sizeBytes) {
        if (sizeBytes <= 0) {
            return libcore.util.EmptyArray.BYTE;
        }
        if (rng == null) {
            rng = android.security.keystore.KeyStoreCryptoOperationUtils.getRng();
        }
        byte[] result = new byte[sizeBytes];
        rng.nextBytes(result);
        return result;
    }

    private static java.security.SecureRandom getRng() {
        // IMPLEMENTATION NOTE: It's OK to share a SecureRandom instance because SecureRandom is
        // required to be thread-safe.
        if (android.security.keystore.KeyStoreCryptoOperationUtils.sRng == null) {
            android.security.keystore.KeyStoreCryptoOperationUtils.sRng = new java.security.SecureRandom();
        }
        return android.security.keystore.KeyStoreCryptoOperationUtils.sRng;
    }
}

