/**
 * Copyright (C) 2012 The Android Open Source Project
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
 * A provider focused on providing JCA interfaces for the Android KeyStore.
 *
 * @unknown 
 */
public class AndroidKeyStoreProvider extends java.security.Provider {
    public static final java.lang.String PROVIDER_NAME = "AndroidKeyStore";

    // IMPLEMENTATION NOTE: Class names are hard-coded in this provider to avoid loading these
    // classes when this provider is instantiated and installed early on during each app's
    // initialization process.
    // 
    // Crypto operations operating on the AndroidKeyStore keys must not be offered by this provider.
    // Instead, they need to be offered by AndroidKeyStoreBCWorkaroundProvider. See its Javadoc
    // for details.
    private static final java.lang.String PACKAGE_NAME = "android.security.keystore";

    public AndroidKeyStoreProvider() {
        super(android.security.keystore.AndroidKeyStoreProvider.PROVIDER_NAME, 1.0, "Android KeyStore security provider");
        // java.security.KeyStore
        put("KeyStore.AndroidKeyStore", android.security.keystore.AndroidKeyStoreProvider.PACKAGE_NAME + ".AndroidKeyStoreSpi");
        // java.security.KeyPairGenerator
        put("KeyPairGenerator.EC", android.security.keystore.AndroidKeyStoreProvider.PACKAGE_NAME + ".AndroidKeyStoreKeyPairGeneratorSpi$EC");
        put("KeyPairGenerator.RSA", android.security.keystore.AndroidKeyStoreProvider.PACKAGE_NAME + ".AndroidKeyStoreKeyPairGeneratorSpi$RSA");
        // java.security.KeyFactory
        putKeyFactoryImpl("EC");
        putKeyFactoryImpl("RSA");
        // javax.crypto.KeyGenerator
        put("KeyGenerator.AES", android.security.keystore.AndroidKeyStoreProvider.PACKAGE_NAME + ".AndroidKeyStoreKeyGeneratorSpi$AES");
        put("KeyGenerator.HmacSHA1", android.security.keystore.AndroidKeyStoreProvider.PACKAGE_NAME + ".AndroidKeyStoreKeyGeneratorSpi$HmacSHA1");
        put("KeyGenerator.HmacSHA224", android.security.keystore.AndroidKeyStoreProvider.PACKAGE_NAME + ".AndroidKeyStoreKeyGeneratorSpi$HmacSHA224");
        put("KeyGenerator.HmacSHA256", android.security.keystore.AndroidKeyStoreProvider.PACKAGE_NAME + ".AndroidKeyStoreKeyGeneratorSpi$HmacSHA256");
        put("KeyGenerator.HmacSHA384", android.security.keystore.AndroidKeyStoreProvider.PACKAGE_NAME + ".AndroidKeyStoreKeyGeneratorSpi$HmacSHA384");
        put("KeyGenerator.HmacSHA512", android.security.keystore.AndroidKeyStoreProvider.PACKAGE_NAME + ".AndroidKeyStoreKeyGeneratorSpi$HmacSHA512");
        // java.security.SecretKeyFactory
        putSecretKeyFactoryImpl("AES");
        putSecretKeyFactoryImpl("HmacSHA1");
        putSecretKeyFactoryImpl("HmacSHA224");
        putSecretKeyFactoryImpl("HmacSHA256");
        putSecretKeyFactoryImpl("HmacSHA384");
        putSecretKeyFactoryImpl("HmacSHA512");
    }

    /**
     * Installs a new instance of this provider (and the
     * {@link AndroidKeyStoreBCWorkaroundProvider}).
     */
    public static void install() {
        java.security.Provider[] providers = java.security.Security.getProviders();
        int bcProviderIndex = -1;
        for (int i = 0; i < providers.length; i++) {
            java.security.Provider provider = providers[i];
            if ("BC".equals(provider.getName())) {
                bcProviderIndex = i;
                break;
            }
        }
        java.security.Security.addProvider(new android.security.keystore.AndroidKeyStoreProvider());
        java.security.Provider workaroundProvider = new android.security.keystore.AndroidKeyStoreBCWorkaroundProvider();
        if (bcProviderIndex != (-1)) {
            // Bouncy Castle provider found -- install the workaround provider above it.
            // insertProviderAt uses 1-based positions.
            java.security.Security.insertProviderAt(workaroundProvider, bcProviderIndex + 1);
        } else {
            // Bouncy Castle provider not found -- install the workaround provider at lowest
            // priority.
            java.security.Security.addProvider(workaroundProvider);
        }
    }

    private void putSecretKeyFactoryImpl(java.lang.String algorithm) {
        put("SecretKeyFactory." + algorithm, android.security.keystore.AndroidKeyStoreProvider.PACKAGE_NAME + ".AndroidKeyStoreSecretKeyFactorySpi");
    }

    private void putKeyFactoryImpl(java.lang.String algorithm) {
        put("KeyFactory." + algorithm, android.security.keystore.AndroidKeyStoreProvider.PACKAGE_NAME + ".AndroidKeyStoreKeyFactorySpi");
    }

    /**
     * Gets the {@link KeyStore} operation handle corresponding to the provided JCA crypto
     * primitive.
     *
     * <p>The following primitives are supported: {@link Cipher} and {@link Mac}.
     *
     * @return KeyStore operation handle or {@code 0} if the provided primitive's KeyStore operation
    is not in progress.
     * @throws IllegalArgumentException
     * 		if the provided primitive is not supported or is not backed
     * 		by AndroidKeyStore provider.
     * @throws IllegalStateException
     * 		if the provided primitive is not initialized.
     */
    public static long getKeyStoreOperationHandle(java.lang.Object cryptoPrimitive) {
        if (cryptoPrimitive == null) {
            throw new java.lang.NullPointerException();
        }
        java.lang.Object spi;
        if (cryptoPrimitive instanceof java.security.Signature) {
            spi = ((java.security.Signature) (cryptoPrimitive)).getCurrentSpi();
        } else
            if (cryptoPrimitive instanceof javax.crypto.Mac) {
                spi = ((javax.crypto.Mac) (cryptoPrimitive)).getCurrentSpi();
            } else
                if (cryptoPrimitive instanceof javax.crypto.Cipher) {
                    spi = ((javax.crypto.Cipher) (cryptoPrimitive)).getCurrentSpi();
                } else {
                    throw new java.lang.IllegalArgumentException(("Unsupported crypto primitive: " + cryptoPrimitive) + ". Supported: Signature, Mac, Cipher");
                }


        if (spi == null) {
            throw new java.lang.IllegalStateException("Crypto primitive not initialized");
        } else
            if (!(spi instanceof android.security.keystore.KeyStoreCryptoOperation)) {
                throw new java.lang.IllegalArgumentException((("Crypto primitive not backed by AndroidKeyStore provider: " + cryptoPrimitive) + ", spi: ") + spi);
            }

        return ((android.security.keystore.KeyStoreCryptoOperation) (spi)).getOperationHandle();
    }

    @android.annotation.NonNull
    public static android.security.keystore.AndroidKeyStorePublicKey getAndroidKeyStorePublicKey(@android.annotation.NonNull
    java.lang.String alias, int uid, @android.annotation.NonNull
    @android.security.keystore.KeyProperties.KeyAlgorithmEnum
    java.lang.String keyAlgorithm, @android.annotation.NonNull
    byte[] x509EncodedForm) {
        java.security.PublicKey publicKey;
        try {
            java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance(keyAlgorithm);
            publicKey = keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(x509EncodedForm));
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new java.security.ProviderException(("Failed to obtain " + keyAlgorithm) + " KeyFactory", e);
        } catch (java.security.spec.InvalidKeySpecException e) {
            throw new java.security.ProviderException("Invalid X.509 encoding of public key", e);
        }
        if (android.security.keystore.KeyProperties.KEY_ALGORITHM_EC.equalsIgnoreCase(keyAlgorithm)) {
            return new android.security.keystore.AndroidKeyStoreECPublicKey(alias, uid, ((java.security.interfaces.ECPublicKey) (publicKey)));
        } else
            if (android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA.equalsIgnoreCase(keyAlgorithm)) {
                return new android.security.keystore.AndroidKeyStoreRSAPublicKey(alias, uid, ((java.security.interfaces.RSAPublicKey) (publicKey)));
            } else {
                throw new java.security.ProviderException("Unsupported Android Keystore public key algorithm: " + keyAlgorithm);
            }

    }

    @android.annotation.NonNull
    public static android.security.keystore.AndroidKeyStorePrivateKey getAndroidKeyStorePrivateKey(@android.annotation.NonNull
    android.security.keystore.AndroidKeyStorePublicKey publicKey) {
        java.lang.String keyAlgorithm = publicKey.getAlgorithm();
        if (android.security.keystore.KeyProperties.KEY_ALGORITHM_EC.equalsIgnoreCase(keyAlgorithm)) {
            return new android.security.keystore.AndroidKeyStoreECPrivateKey(publicKey.getAlias(), publicKey.getUid(), ((java.security.interfaces.ECKey) (publicKey)).getParams());
        } else
            if (android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA.equalsIgnoreCase(keyAlgorithm)) {
                return new android.security.keystore.AndroidKeyStoreRSAPrivateKey(publicKey.getAlias(), publicKey.getUid(), ((java.security.interfaces.RSAKey) (publicKey)).getModulus());
            } else {
                throw new java.security.ProviderException("Unsupported Android Keystore public key algorithm: " + keyAlgorithm);
            }

    }

    @android.annotation.NonNull
    public static android.security.keystore.AndroidKeyStorePublicKey loadAndroidKeyStorePublicKeyFromKeystore(@android.annotation.NonNull
    android.security.KeyStore keyStore, @android.annotation.NonNull
    java.lang.String privateKeyAlias, int uid) throws java.security.UnrecoverableKeyException {
        android.security.keymaster.KeyCharacteristics keyCharacteristics = new android.security.keymaster.KeyCharacteristics();
        int errorCode = keyStore.getKeyCharacteristics(privateKeyAlias, null, null, uid, keyCharacteristics);
        if (errorCode != android.security.KeyStore.NO_ERROR) {
            throw ((java.security.UnrecoverableKeyException) (new java.security.UnrecoverableKeyException("Failed to obtain information about private key").initCause(android.security.KeyStore.getKeyStoreException(errorCode))));
        }
        android.security.keymaster.ExportResult exportResult = keyStore.exportKey(privateKeyAlias, android.security.keymaster.KeymasterDefs.KM_KEY_FORMAT_X509, null, null, uid);
        if (exportResult.resultCode != android.security.KeyStore.NO_ERROR) {
            throw ((java.security.UnrecoverableKeyException) (new java.security.UnrecoverableKeyException("Failed to obtain X.509 form of public key").initCause(android.security.KeyStore.getKeyStoreException(exportResult.resultCode))));
        }
        final byte[] x509EncodedPublicKey = exportResult.exportData;
        java.lang.Integer keymasterAlgorithm = keyCharacteristics.getEnum(android.security.keymaster.KeymasterDefs.KM_TAG_ALGORITHM);
        if (keymasterAlgorithm == null) {
            throw new java.security.UnrecoverableKeyException("Key algorithm unknown");
        }
        java.lang.String jcaKeyAlgorithm;
        try {
            jcaKeyAlgorithm = android.security.keystore.KeyProperties.KeyAlgorithm.fromKeymasterAsymmetricKeyAlgorithm(keymasterAlgorithm);
        } catch (java.lang.IllegalArgumentException e) {
            throw ((java.security.UnrecoverableKeyException) (new java.security.UnrecoverableKeyException("Failed to load private key").initCause(e)));
        }
        return android.security.keystore.AndroidKeyStoreProvider.getAndroidKeyStorePublicKey(privateKeyAlias, uid, jcaKeyAlgorithm, x509EncodedPublicKey);
    }

    @android.annotation.NonNull
    public static java.security.KeyPair loadAndroidKeyStoreKeyPairFromKeystore(@android.annotation.NonNull
    android.security.KeyStore keyStore, @android.annotation.NonNull
    java.lang.String privateKeyAlias, int uid) throws java.security.UnrecoverableKeyException {
        android.security.keystore.AndroidKeyStorePublicKey publicKey = android.security.keystore.AndroidKeyStoreProvider.loadAndroidKeyStorePublicKeyFromKeystore(keyStore, privateKeyAlias, uid);
        android.security.keystore.AndroidKeyStorePrivateKey privateKey = android.security.keystore.AndroidKeyStoreProvider.getAndroidKeyStorePrivateKey(publicKey);
        return new java.security.KeyPair(publicKey, privateKey);
    }

    @android.annotation.NonNull
    public static android.security.keystore.AndroidKeyStorePrivateKey loadAndroidKeyStorePrivateKeyFromKeystore(@android.annotation.NonNull
    android.security.KeyStore keyStore, @android.annotation.NonNull
    java.lang.String privateKeyAlias, int uid) throws java.security.UnrecoverableKeyException {
        java.security.KeyPair keyPair = android.security.keystore.AndroidKeyStoreProvider.loadAndroidKeyStoreKeyPairFromKeystore(keyStore, privateKeyAlias, uid);
        return ((android.security.keystore.AndroidKeyStorePrivateKey) (keyPair.getPrivate()));
    }

    @android.annotation.NonNull
    public static android.security.keystore.AndroidKeyStoreSecretKey loadAndroidKeyStoreSecretKeyFromKeystore(@android.annotation.NonNull
    android.security.KeyStore keyStore, @android.annotation.NonNull
    java.lang.String secretKeyAlias, int uid) throws java.security.UnrecoverableKeyException {
        android.security.keymaster.KeyCharacteristics keyCharacteristics = new android.security.keymaster.KeyCharacteristics();
        int errorCode = keyStore.getKeyCharacteristics(secretKeyAlias, null, null, uid, keyCharacteristics);
        if (errorCode != android.security.KeyStore.NO_ERROR) {
            throw ((java.security.UnrecoverableKeyException) (new java.security.UnrecoverableKeyException("Failed to obtain information about key").initCause(android.security.KeyStore.getKeyStoreException(errorCode))));
        }
        java.lang.Integer keymasterAlgorithm = keyCharacteristics.getEnum(android.security.keymaster.KeymasterDefs.KM_TAG_ALGORITHM);
        if (keymasterAlgorithm == null) {
            throw new java.security.UnrecoverableKeyException("Key algorithm unknown");
        }
        java.util.List<java.lang.Integer> keymasterDigests = keyCharacteristics.getEnums(android.security.keymaster.KeymasterDefs.KM_TAG_DIGEST);
        int keymasterDigest;
        if (keymasterDigests.isEmpty()) {
            keymasterDigest = -1;
        } else {
            // More than one digest can be permitted for this key. Use the first one to form the
            // JCA key algorithm name.
            keymasterDigest = keymasterDigests.get(0);
        }
        @android.security.keystore.KeyProperties.KeyAlgorithmEnum
        java.lang.String keyAlgorithmString;
        try {
            keyAlgorithmString = android.security.keystore.KeyProperties.KeyAlgorithm.fromKeymasterSecretKeyAlgorithm(keymasterAlgorithm, keymasterDigest);
        } catch (java.lang.IllegalArgumentException e) {
            throw ((java.security.UnrecoverableKeyException) (new java.security.UnrecoverableKeyException("Unsupported secret key type").initCause(e)));
        }
        return new android.security.keystore.AndroidKeyStoreSecretKey(secretKeyAlias, uid, keyAlgorithmString);
    }

    /**
     * Returns an {@code AndroidKeyStore} {@link java.security.KeyStore}} of the specified UID.
     * The {@code KeyStore} contains keys and certificates owned by that UID. Such cross-UID
     * access is permitted to a few system UIDs and only to a few other UIDs (e.g., Wi-Fi, VPN)
     * all of which are system.
     *
     * <p>Note: the returned {@code KeyStore} is already initialized/loaded. Thus, there is
     * no need to invoke {@code load} on it.
     */
    @android.annotation.NonNull
    public static java.security.KeyStore getKeyStoreForUid(int uid) throws java.security.KeyStoreException, java.security.NoSuchProviderException {
        java.security.KeyStore result = java.security.KeyStore.getInstance("AndroidKeyStore", android.security.keystore.AndroidKeyStoreProvider.PROVIDER_NAME);
        try {
            result.load(new android.security.keystore.AndroidKeyStoreLoadStoreParameter(uid));
        } catch (java.security.NoSuchAlgorithmException | java.security.cert.CertificateException | java.io.IOException e) {
            throw new java.security.KeyStoreException("Failed to load AndroidKeyStore KeyStore for UID " + uid, e);
        }
        return result;
    }
}

