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
 * {@link KeyFactorySpi} backed by Android KeyStore.
 *
 * @unknown 
 */
public class AndroidKeyStoreKeyFactorySpi extends java.security.KeyFactorySpi {
    private final android.security.KeyStore mKeyStore = android.security.KeyStore.getInstance();

    @java.lang.Override
    protected <T extends java.security.spec.KeySpec> T engineGetKeySpec(java.security.Key key, java.lang.Class<T> keySpecClass) throws java.security.spec.InvalidKeySpecException {
        if (key == null) {
            throw new java.security.spec.InvalidKeySpecException("key == null");
        } else
            if ((!(key instanceof android.security.keystore.AndroidKeyStorePrivateKey)) && (!(key instanceof android.security.keystore.AndroidKeyStorePublicKey))) {
                throw new java.security.spec.InvalidKeySpecException(("Unsupported key type: " + key.getClass().getName()) + ". This KeyFactory supports only Android Keystore asymmetric keys");
            }

        // key is an Android Keystore private or public key
        if (keySpecClass == null) {
            throw new java.security.spec.InvalidKeySpecException("keySpecClass == null");
        } else
            if (android.security.keystore.KeyInfo.class.equals(keySpecClass)) {
                if (!(key instanceof android.security.keystore.AndroidKeyStorePrivateKey)) {
                    throw new java.security.spec.InvalidKeySpecException(("Unsupported key type: " + key.getClass().getName()) + ". KeyInfo can be obtained only for Android Keystore private keys");
                }
                android.security.keystore.AndroidKeyStorePrivateKey keystorePrivateKey = ((android.security.keystore.AndroidKeyStorePrivateKey) (key));
                java.lang.String keyAliasInKeystore = keystorePrivateKey.getAlias();
                java.lang.String entryAlias;
                if (keyAliasInKeystore.startsWith(android.security.Credentials.USER_PRIVATE_KEY)) {
                    entryAlias = keyAliasInKeystore.substring(android.security.Credentials.USER_PRIVATE_KEY.length());
                } else {
                    throw new java.security.spec.InvalidKeySpecException("Invalid key alias: " + keyAliasInKeystore);
                }
                @java.lang.SuppressWarnings("unchecked")
                T result = ((T) (android.security.keystore.AndroidKeyStoreSecretKeyFactorySpi.getKeyInfo(mKeyStore, entryAlias, keyAliasInKeystore, keystorePrivateKey.getUid())));
                return result;
            } else
                if (java.security.spec.X509EncodedKeySpec.class.equals(keySpecClass)) {
                    if (!(key instanceof android.security.keystore.AndroidKeyStorePublicKey)) {
                        throw new java.security.spec.InvalidKeySpecException((("Unsupported key type: " + key.getClass().getName()) + ". X509EncodedKeySpec can be obtained only for Android Keystore public") + " keys");
                    }
                    @java.lang.SuppressWarnings("unchecked")
                    T result = ((T) (new java.security.spec.X509EncodedKeySpec(((android.security.keystore.AndroidKeyStorePublicKey) (key)).getEncoded())));
                    return result;
                } else
                    if (java.security.spec.PKCS8EncodedKeySpec.class.equals(keySpecClass)) {
                        if (key instanceof android.security.keystore.AndroidKeyStorePrivateKey) {
                            throw new java.security.spec.InvalidKeySpecException("Key material export of Android Keystore private keys is not supported");
                        } else {
                            throw new java.security.spec.InvalidKeySpecException("Cannot export key material of public key in PKCS#8 format." + " Only X.509 format (X509EncodedKeySpec) supported for public keys.");
                        }
                    } else
                        if (java.security.spec.RSAPublicKeySpec.class.equals(keySpecClass)) {
                            if (key instanceof android.security.keystore.AndroidKeyStoreRSAPublicKey) {
                                android.security.keystore.AndroidKeyStoreRSAPublicKey rsaKey = ((android.security.keystore.AndroidKeyStoreRSAPublicKey) (key));
                                @java.lang.SuppressWarnings("unchecked")
                                T result = ((T) (new java.security.spec.RSAPublicKeySpec(rsaKey.getModulus(), rsaKey.getPublicExponent())));
                                return result;
                            } else {
                                throw new java.security.spec.InvalidKeySpecException(((("Obtaining RSAPublicKeySpec not supported for " + key.getAlgorithm()) + " ") + (key instanceof android.security.keystore.AndroidKeyStorePrivateKey ? "private" : "public")) + " key");
                            }
                        } else
                            if (java.security.spec.ECPublicKeySpec.class.equals(keySpecClass)) {
                                if (key instanceof android.security.keystore.AndroidKeyStoreECPublicKey) {
                                    android.security.keystore.AndroidKeyStoreECPublicKey ecKey = ((android.security.keystore.AndroidKeyStoreECPublicKey) (key));
                                    @java.lang.SuppressWarnings("unchecked")
                                    T result = ((T) (new java.security.spec.ECPublicKeySpec(ecKey.getW(), ecKey.getParams())));
                                    return result;
                                } else {
                                    throw new java.security.spec.InvalidKeySpecException(((("Obtaining ECPublicKeySpec not supported for " + key.getAlgorithm()) + " ") + (key instanceof android.security.keystore.AndroidKeyStorePrivateKey ? "private" : "public")) + " key");
                                }
                            } else {
                                throw new java.security.spec.InvalidKeySpecException("Unsupported key spec: " + keySpecClass.getName());
                            }





    }

    @java.lang.Override
    protected java.security.PrivateKey engineGeneratePrivate(java.security.spec.KeySpec spec) throws java.security.spec.InvalidKeySpecException {
        throw new java.security.spec.InvalidKeySpecException(("To generate a key pair in Android Keystore, use KeyPairGenerator initialized with" + " ") + android.security.keystore.KeyGenParameterSpec.class.getName());
    }

    @java.lang.Override
    protected java.security.PublicKey engineGeneratePublic(java.security.spec.KeySpec spec) throws java.security.spec.InvalidKeySpecException {
        throw new java.security.spec.InvalidKeySpecException(("To generate a key pair in Android Keystore, use KeyPairGenerator initialized with" + " ") + android.security.keystore.KeyGenParameterSpec.class.getName());
    }

    @java.lang.Override
    protected java.security.Key engineTranslateKey(java.security.Key key) throws java.security.InvalidKeyException {
        if (key == null) {
            throw new java.security.InvalidKeyException("key == null");
        } else
            if ((!(key instanceof android.security.keystore.AndroidKeyStorePrivateKey)) && (!(key instanceof android.security.keystore.AndroidKeyStorePublicKey))) {
                throw new java.security.InvalidKeyException("To import a key into Android Keystore, use KeyStore.setEntry");
            }

        return key;
    }
}

