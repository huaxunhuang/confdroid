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
 * A java.security.KeyStore interface for the Android KeyStore. An instance of
 * it can be created via the {@link java.security.KeyStore#getInstance(String)
 * KeyStore.getInstance("AndroidKeyStore")} interface. This returns a
 * java.security.KeyStore backed by this "AndroidKeyStore" implementation.
 * <p>
 * This is built on top of Android's keystore daemon. The convention of alias
 * use is:
 * <p>
 * PrivateKeyEntry will have a Credentials.USER_PRIVATE_KEY as the private key,
 * Credentials.USER_CERTIFICATE as the first certificate in the chain (the one
 * that corresponds to the private key), and then a Credentials.CA_CERTIFICATE
 * entry which will have the rest of the chain concatenated in BER format.
 * <p>
 * TrustedCertificateEntry will just have a Credentials.CA_CERTIFICATE entry
 * with a single certificate.
 *
 * @unknown 
 */
public class AndroidKeyStoreSpi extends java.security.KeyStoreSpi {
    public static final java.lang.String NAME = "AndroidKeyStore";

    private android.security.KeyStore mKeyStore;

    private int mUid = android.security.KeyStore.UID_SELF;

    @java.lang.Override
    public java.security.Key engineGetKey(java.lang.String alias, char[] password) throws java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException {
        if (isPrivateKeyEntry(alias)) {
            java.lang.String privateKeyAlias = android.security.Credentials.USER_PRIVATE_KEY + alias;
            return android.security.keystore.AndroidKeyStoreProvider.loadAndroidKeyStorePrivateKeyFromKeystore(mKeyStore, privateKeyAlias, mUid);
        } else
            if (isSecretKeyEntry(alias)) {
                java.lang.String secretKeyAlias = android.security.Credentials.USER_SECRET_KEY + alias;
                return android.security.keystore.AndroidKeyStoreProvider.loadAndroidKeyStoreSecretKeyFromKeystore(mKeyStore, secretKeyAlias, mUid);
            } else {
                // Key not found
                return null;
            }

    }

    @java.lang.Override
    public java.security.cert.Certificate[] engineGetCertificateChain(java.lang.String alias) {
        if (alias == null) {
            throw new java.lang.NullPointerException("alias == null");
        }
        final java.security.cert.X509Certificate leaf = ((java.security.cert.X509Certificate) (engineGetCertificate(alias)));
        if (leaf == null) {
            return null;
        }
        final java.security.cert.Certificate[] caList;
        final byte[] caBytes = mKeyStore.get(android.security.Credentials.CA_CERTIFICATE + alias, mUid);
        if (caBytes != null) {
            final java.util.Collection<java.security.cert.X509Certificate> caChain = android.security.keystore.AndroidKeyStoreSpi.toCertificates(caBytes);
            caList = new java.security.cert.Certificate[caChain.size() + 1];
            final java.util.Iterator<java.security.cert.X509Certificate> it = caChain.iterator();
            int i = 1;
            while (it.hasNext()) {
                caList[i++] = it.next();
            } 
        } else {
            caList = new java.security.cert.Certificate[1];
        }
        caList[0] = leaf;
        return caList;
    }

    @java.lang.Override
    public java.security.cert.Certificate engineGetCertificate(java.lang.String alias) {
        if (alias == null) {
            throw new java.lang.NullPointerException("alias == null");
        }
        byte[] encodedCert = mKeyStore.get(android.security.Credentials.USER_CERTIFICATE + alias, mUid);
        if (encodedCert != null) {
            return getCertificateForPrivateKeyEntry(alias, encodedCert);
        }
        encodedCert = mKeyStore.get(android.security.Credentials.CA_CERTIFICATE + alias, mUid);
        if (encodedCert != null) {
            return getCertificateForTrustedCertificateEntry(encodedCert);
        }
        // This entry/alias does not contain a certificate.
        return null;
    }

    private java.security.cert.Certificate getCertificateForTrustedCertificateEntry(byte[] encodedCert) {
        // For this certificate there shouldn't be a private key in this KeyStore entry. Thus,
        // there's no need to wrap this certificate as opposed to the certificate associated with
        // a private key entry.
        return android.security.keystore.AndroidKeyStoreSpi.toCertificate(encodedCert);
    }

    private java.security.cert.Certificate getCertificateForPrivateKeyEntry(java.lang.String alias, byte[] encodedCert) {
        // All crypto algorithms offered by Android Keystore for its private keys must also
        // be offered for the corresponding public keys stored in the Android Keystore. The
        // complication is that the underlying keystore service operates only on full key pairs,
        // rather than just public keys or private keys. As a result, Android Keystore-backed
        // crypto can only be offered for public keys for which keystore contains the
        // corresponding private key. This is not the case for certificate-only entries (e.g.,
        // trusted certificates).
        // 
        // getCertificate().getPublicKey() is the only way to obtain the public key
        // corresponding to the private key stored in the KeyStore. Thus, we need to make sure
        // that the returned public key points to the underlying key pair / private key
        // when available.
        java.security.cert.X509Certificate cert = android.security.keystore.AndroidKeyStoreSpi.toCertificate(encodedCert);
        if (cert == null) {
            // Failed to parse the certificate.
            return null;
        }
        java.lang.String privateKeyAlias = android.security.Credentials.USER_PRIVATE_KEY + alias;
        if (mKeyStore.contains(privateKeyAlias, mUid)) {
            // As expected, keystore contains the private key corresponding to this public key. Wrap
            // the certificate so that its getPublicKey method returns an Android Keystore
            // PublicKey. This key will delegate crypto operations involving this public key to
            // Android Keystore when higher-priority providers do not offer these crypto
            // operations for this key.
            return android.security.keystore.AndroidKeyStoreSpi.wrapIntoKeyStoreCertificate(privateKeyAlias, mUid, cert);
        } else {
            // This KeyStore entry/alias is supposed to contain the private key corresponding to
            // the public key in this certificate, but it does not for some reason. It's probably a
            // bug. Let other providers handle crypto operations involving the public key returned
            // by this certificate's getPublicKey.
            return cert;
        }
    }

    /**
     * Wraps the provided cerificate into {@link KeyStoreX509Certificate} so that the public key
     * returned by the certificate contains information about the alias of the private key in
     * keystore. This is needed so that Android Keystore crypto operations using public keys can
     * find out which key alias to use. These operations cannot work without an alias.
     */
    private static android.security.keystore.AndroidKeyStoreSpi.KeyStoreX509Certificate wrapIntoKeyStoreCertificate(java.lang.String privateKeyAlias, int uid, java.security.cert.X509Certificate certificate) {
        return certificate != null ? new android.security.keystore.AndroidKeyStoreSpi.KeyStoreX509Certificate(privateKeyAlias, uid, certificate) : null;
    }

    private static java.security.cert.X509Certificate toCertificate(byte[] bytes) {
        try {
            final java.security.cert.CertificateFactory certFactory = java.security.cert.CertificateFactory.getInstance("X.509");
            return ((java.security.cert.X509Certificate) (certFactory.generateCertificate(new java.io.ByteArrayInputStream(bytes))));
        } catch (java.security.cert.CertificateException e) {
            android.util.Log.w(android.security.keystore.AndroidKeyStoreSpi.NAME, "Couldn't parse certificate in keystore", e);
            return null;
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    private static java.util.Collection<java.security.cert.X509Certificate> toCertificates(byte[] bytes) {
        try {
            final java.security.cert.CertificateFactory certFactory = java.security.cert.CertificateFactory.getInstance("X.509");
            return ((java.util.Collection<java.security.cert.X509Certificate>) (certFactory.generateCertificates(new java.io.ByteArrayInputStream(bytes))));
        } catch (java.security.cert.CertificateException e) {
            android.util.Log.w(android.security.keystore.AndroidKeyStoreSpi.NAME, "Couldn't parse certificates in keystore", e);
            return new java.util.ArrayList<java.security.cert.X509Certificate>();
        }
    }

    private java.util.Date getModificationDate(java.lang.String alias) {
        final long epochMillis = mKeyStore.getmtime(alias, mUid);
        if (epochMillis == (-1L)) {
            return null;
        }
        return new java.util.Date(epochMillis);
    }

    @java.lang.Override
    public java.util.Date engineGetCreationDate(java.lang.String alias) {
        if (alias == null) {
            throw new java.lang.NullPointerException("alias == null");
        }
        java.util.Date d = getModificationDate(android.security.Credentials.USER_PRIVATE_KEY + alias);
        if (d != null) {
            return d;
        }
        d = getModificationDate(android.security.Credentials.USER_SECRET_KEY + alias);
        if (d != null) {
            return d;
        }
        d = getModificationDate(android.security.Credentials.USER_CERTIFICATE + alias);
        if (d != null) {
            return d;
        }
        return getModificationDate(android.security.Credentials.CA_CERTIFICATE + alias);
    }

    @java.lang.Override
    public void engineSetKeyEntry(java.lang.String alias, java.security.Key key, char[] password, java.security.cert.Certificate[] chain) throws java.security.KeyStoreException {
        if ((password != null) && (password.length > 0)) {
            throw new java.security.KeyStoreException("entries cannot be protected with passwords");
        }
        if (key instanceof java.security.PrivateKey) {
            setPrivateKeyEntry(alias, ((java.security.PrivateKey) (key)), chain, null);
        } else
            if (key instanceof javax.crypto.SecretKey) {
                setSecretKeyEntry(alias, ((javax.crypto.SecretKey) (key)), null);
            } else {
                throw new java.security.KeyStoreException("Only PrivateKey and SecretKey are supported");
            }

    }

    private static android.security.keystore.KeyProtection getLegacyKeyProtectionParameter(java.security.PrivateKey key) throws java.security.KeyStoreException {
        java.lang.String keyAlgorithm = key.getAlgorithm();
        android.security.keystore.KeyProtection.Builder specBuilder;
        if (android.security.keystore.KeyProperties.KEY_ALGORITHM_EC.equalsIgnoreCase(keyAlgorithm)) {
            specBuilder = new android.security.keystore.KeyProtection.Builder(android.security.keystore.KeyProperties.PURPOSE_SIGN | android.security.keystore.KeyProperties.PURPOSE_VERIFY);
            // Authorized to be used with any digest (including no digest).
            // MD5 was never offered for Android Keystore for ECDSA.
            specBuilder.setDigests(android.security.keystore.KeyProperties.DIGEST_NONE, android.security.keystore.KeyProperties.DIGEST_SHA1, android.security.keystore.KeyProperties.DIGEST_SHA224, android.security.keystore.KeyProperties.DIGEST_SHA256, android.security.keystore.KeyProperties.DIGEST_SHA384, android.security.keystore.KeyProperties.DIGEST_SHA512);
        } else
            if (android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA.equalsIgnoreCase(keyAlgorithm)) {
                specBuilder = new android.security.keystore.KeyProtection.Builder(((android.security.keystore.KeyProperties.PURPOSE_ENCRYPT | android.security.keystore.KeyProperties.PURPOSE_DECRYPT) | android.security.keystore.KeyProperties.PURPOSE_SIGN) | android.security.keystore.KeyProperties.PURPOSE_VERIFY);
                // Authorized to be used with any digest (including no digest).
                specBuilder.setDigests(android.security.keystore.KeyProperties.DIGEST_NONE, android.security.keystore.KeyProperties.DIGEST_MD5, android.security.keystore.KeyProperties.DIGEST_SHA1, android.security.keystore.KeyProperties.DIGEST_SHA224, android.security.keystore.KeyProperties.DIGEST_SHA256, android.security.keystore.KeyProperties.DIGEST_SHA384, android.security.keystore.KeyProperties.DIGEST_SHA512);
                // Authorized to be used with any encryption and signature padding
                // schemes (including no padding).
                specBuilder.setEncryptionPaddings(android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE, android.security.keystore.KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1, android.security.keystore.KeyProperties.ENCRYPTION_PADDING_RSA_OAEP);
                specBuilder.setSignaturePaddings(android.security.keystore.KeyProperties.SIGNATURE_PADDING_RSA_PKCS1, android.security.keystore.KeyProperties.SIGNATURE_PADDING_RSA_PSS);
                // Disable randomized encryption requirement to support encryption
                // padding NONE above.
                specBuilder.setRandomizedEncryptionRequired(false);
            } else {
                throw new java.security.KeyStoreException("Unsupported key algorithm: " + keyAlgorithm);
            }

        specBuilder.setUserAuthenticationRequired(false);
        return specBuilder.build();
    }

    private void setPrivateKeyEntry(java.lang.String alias, java.security.PrivateKey key, java.security.cert.Certificate[] chain, java.security.KeyStore.ProtectionParameter param) throws java.security.KeyStoreException {
        int flags = 0;
        android.security.keystore.KeyProtection spec;
        if (param == null) {
            spec = android.security.keystore.AndroidKeyStoreSpi.getLegacyKeyProtectionParameter(key);
        } else
            if (param instanceof android.security.KeyStoreParameter) {
                spec = android.security.keystore.AndroidKeyStoreSpi.getLegacyKeyProtectionParameter(key);
                android.security.KeyStoreParameter legacySpec = ((android.security.KeyStoreParameter) (param));
                if (legacySpec.isEncryptionRequired()) {
                    flags = android.security.KeyStore.FLAG_ENCRYPTED;
                }
            } else
                if (param instanceof android.security.keystore.KeyProtection) {
                    spec = ((android.security.keystore.KeyProtection) (param));
                } else {
                    throw new java.security.KeyStoreException((((("Unsupported protection parameter class:" + param.getClass().getName()) + ". Supported: ") + android.security.keystore.KeyProtection.class.getName()) + ", ") + android.security.KeyStoreParameter.class.getName());
                }


        // Make sure the chain exists since this is a PrivateKey
        if ((chain == null) || (chain.length == 0)) {
            throw new java.security.KeyStoreException("Must supply at least one Certificate with PrivateKey");
        }
        // Do chain type checking.
        java.security.cert.X509Certificate[] x509chain = new java.security.cert.X509Certificate[chain.length];
        for (int i = 0; i < chain.length; i++) {
            if (!"X.509".equals(chain[i].getType())) {
                throw new java.security.KeyStoreException("Certificates must be in X.509 format: invalid cert #" + i);
            }
            if (!(chain[i] instanceof java.security.cert.X509Certificate)) {
                throw new java.security.KeyStoreException("Certificates must be in X.509 format: invalid cert #" + i);
            }
            x509chain[i] = ((java.security.cert.X509Certificate) (chain[i]));
        }
        final byte[] userCertBytes;
        try {
            userCertBytes = x509chain[0].getEncoded();
        } catch (java.security.cert.CertificateEncodingException e) {
            throw new java.security.KeyStoreException("Failed to encode certificate #0", e);
        }
        /* If we have a chain, store it in the CA certificate slot for this
        alias as concatenated DER-encoded certificates. These can be
        deserialized by {@link CertificateFactory#generateCertificates}.
         */
        final byte[] chainBytes;
        if (chain.length > 1) {
            /* The chain is passed in as {user_cert, ca_cert_1, ca_cert_2, ...}
            so we only need the certificates starting at index 1.
             */
            final byte[][] certsBytes = new byte[x509chain.length - 1][];
            int totalCertLength = 0;
            for (int i = 0; i < certsBytes.length; i++) {
                try {
                    certsBytes[i] = x509chain[i + 1].getEncoded();
                    totalCertLength += certsBytes[i].length;
                } catch (java.security.cert.CertificateEncodingException e) {
                    throw new java.security.KeyStoreException("Failed to encode certificate #" + i, e);
                }
            }
            /* Serialize this into one byte array so we can later call
            CertificateFactory#generateCertificates to recover them.
             */
            chainBytes = new byte[totalCertLength];
            int outputOffset = 0;
            for (int i = 0; i < certsBytes.length; i++) {
                final int certLength = certsBytes[i].length;
                java.lang.System.arraycopy(certsBytes[i], 0, chainBytes, outputOffset, certLength);
                outputOffset += certLength;
                certsBytes[i] = null;
            }
        } else {
            chainBytes = null;
        }
        final java.lang.String pkeyAlias;
        if (key instanceof android.security.keystore.AndroidKeyStorePrivateKey) {
            pkeyAlias = ((android.security.keystore.AndroidKeyStoreKey) (key)).getAlias();
        } else {
            pkeyAlias = null;
        }
        byte[] pkcs8EncodedPrivateKeyBytes;
        android.security.keymaster.KeymasterArguments importArgs;
        final boolean shouldReplacePrivateKey;
        if ((pkeyAlias != null) && pkeyAlias.startsWith(android.security.Credentials.USER_PRIVATE_KEY)) {
            final java.lang.String keySubalias = pkeyAlias.substring(android.security.Credentials.USER_PRIVATE_KEY.length());
            if (!alias.equals(keySubalias)) {
                throw new java.security.KeyStoreException((("Can only replace keys with same alias: " + alias) + " != ") + keySubalias);
            }
            shouldReplacePrivateKey = false;
            importArgs = null;
            pkcs8EncodedPrivateKeyBytes = null;
        } else {
            shouldReplacePrivateKey = true;
            // Make sure the PrivateKey format is the one we support.
            final java.lang.String keyFormat = key.getFormat();
            if ((keyFormat == null) || (!"PKCS#8".equals(keyFormat))) {
                throw new java.security.KeyStoreException((("Unsupported private key export format: " + keyFormat) + ". Only private keys which export their key material in PKCS#8 format are") + " supported.");
            }
            // Make sure we can actually encode the key.
            pkcs8EncodedPrivateKeyBytes = key.getEncoded();
            if (pkcs8EncodedPrivateKeyBytes == null) {
                throw new java.security.KeyStoreException("Private key did not export any key material");
            }
            importArgs = new android.security.keymaster.KeymasterArguments();
            try {
                importArgs.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_ALGORITHM, android.security.keystore.KeyProperties.KeyAlgorithm.toKeymasterAsymmetricKeyAlgorithm(key.getAlgorithm()));
                @android.security.keystore.KeyProperties.PurposeEnum
                int purposes = spec.getPurposes();
                importArgs.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_PURPOSE, android.security.keystore.KeyProperties.Purpose.allToKeymaster(purposes));
                if (spec.isDigestsSpecified()) {
                    importArgs.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_DIGEST, android.security.keystore.KeyProperties.Digest.allToKeymaster(spec.getDigests()));
                }
                importArgs.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_BLOCK_MODE, android.security.keystore.KeyProperties.BlockMode.allToKeymaster(spec.getBlockModes()));
                int[] keymasterEncryptionPaddings = android.security.keystore.KeyProperties.EncryptionPadding.allToKeymaster(spec.getEncryptionPaddings());
                if (((purposes & android.security.keystore.KeyProperties.PURPOSE_ENCRYPT) != 0) && spec.isRandomizedEncryptionRequired()) {
                    for (int keymasterPadding : keymasterEncryptionPaddings) {
                        if (!android.security.keystore.KeymasterUtils.isKeymasterPaddingSchemeIndCpaCompatibleWithAsymmetricCrypto(keymasterPadding)) {
                            throw new java.security.KeyStoreException((("Randomized encryption (IND-CPA) required but is violated by" + " encryption padding mode: ") + android.security.keystore.KeyProperties.EncryptionPadding.fromKeymaster(keymasterPadding)) + ". See KeyProtection documentation.");
                        }
                    }
                }
                importArgs.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_PADDING, keymasterEncryptionPaddings);
                importArgs.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_PADDING, android.security.keystore.KeyProperties.SignaturePadding.allToKeymaster(spec.getSignaturePaddings()));
                android.security.keystore.KeymasterUtils.addUserAuthArgs(importArgs, spec.isUserAuthenticationRequired(), spec.getUserAuthenticationValidityDurationSeconds(), spec.isUserAuthenticationValidWhileOnBody(), spec.isInvalidatedByBiometricEnrollment());
                importArgs.addDateIfNotNull(android.security.keymaster.KeymasterDefs.KM_TAG_ACTIVE_DATETIME, spec.getKeyValidityStart());
                importArgs.addDateIfNotNull(android.security.keymaster.KeymasterDefs.KM_TAG_ORIGINATION_EXPIRE_DATETIME, spec.getKeyValidityForOriginationEnd());
                importArgs.addDateIfNotNull(android.security.keymaster.KeymasterDefs.KM_TAG_USAGE_EXPIRE_DATETIME, spec.getKeyValidityForConsumptionEnd());
            } catch (java.lang.IllegalArgumentException | java.lang.IllegalStateException e) {
                throw new java.security.KeyStoreException(e);
            }
        }
        boolean success = false;
        try {
            // Store the private key, if necessary
            if (shouldReplacePrivateKey) {
                // Delete the stored private key and any related entries before importing the
                // provided key
                android.security.Credentials.deleteAllTypesForAlias(mKeyStore, alias, mUid);
                android.security.keymaster.KeyCharacteristics resultingKeyCharacteristics = new android.security.keymaster.KeyCharacteristics();
                int errorCode = mKeyStore.importKey(android.security.Credentials.USER_PRIVATE_KEY + alias, importArgs, android.security.keymaster.KeymasterDefs.KM_KEY_FORMAT_PKCS8, pkcs8EncodedPrivateKeyBytes, mUid, flags, resultingKeyCharacteristics);
                if (errorCode != android.security.KeyStore.NO_ERROR) {
                    throw new java.security.KeyStoreException("Failed to store private key", android.security.KeyStore.getKeyStoreException(errorCode));
                }
            } else {
                // Keep the stored private key around -- delete all other entry types
                android.security.Credentials.deleteCertificateTypesForAlias(mKeyStore, alias, mUid);
                android.security.Credentials.deleteSecretKeyTypeForAlias(mKeyStore, alias, mUid);
            }
            // Store the leaf certificate
            int errorCode = mKeyStore.insert(android.security.Credentials.USER_CERTIFICATE + alias, userCertBytes, mUid, flags);
            if (errorCode != android.security.KeyStore.NO_ERROR) {
                throw new java.security.KeyStoreException("Failed to store certificate #0", android.security.KeyStore.getKeyStoreException(errorCode));
            }
            // Store the certificate chain
            errorCode = mKeyStore.insert(android.security.Credentials.CA_CERTIFICATE + alias, chainBytes, mUid, flags);
            if (errorCode != android.security.KeyStore.NO_ERROR) {
                throw new java.security.KeyStoreException("Failed to store certificate chain", android.security.KeyStore.getKeyStoreException(errorCode));
            }
            success = true;
        } finally {
            if (!success) {
                if (shouldReplacePrivateKey) {
                    android.security.Credentials.deleteAllTypesForAlias(mKeyStore, alias, mUid);
                } else {
                    android.security.Credentials.deleteCertificateTypesForAlias(mKeyStore, alias, mUid);
                    android.security.Credentials.deleteSecretKeyTypeForAlias(mKeyStore, alias, mUid);
                }
            }
        }
    }

    private void setSecretKeyEntry(java.lang.String entryAlias, javax.crypto.SecretKey key, java.security.KeyStore.ProtectionParameter param) throws java.security.KeyStoreException {
        if ((param != null) && (!(param instanceof android.security.keystore.KeyProtection))) {
            throw new java.security.KeyStoreException((("Unsupported protection parameter class: " + param.getClass().getName()) + ". Supported: ") + android.security.keystore.KeyProtection.class.getName());
        }
        android.security.keystore.KeyProtection params = ((android.security.keystore.KeyProtection) (param));
        if (key instanceof android.security.keystore.AndroidKeyStoreSecretKey) {
            // KeyStore-backed secret key. It cannot be duplicated into another entry and cannot
            // overwrite its own entry.
            java.lang.String keyAliasInKeystore = ((android.security.keystore.AndroidKeyStoreSecretKey) (key)).getAlias();
            if (keyAliasInKeystore == null) {
                throw new java.security.KeyStoreException("KeyStore-backed secret key does not have an alias");
            }
            if (!keyAliasInKeystore.startsWith(android.security.Credentials.USER_SECRET_KEY)) {
                throw new java.security.KeyStoreException("KeyStore-backed secret key has invalid alias: " + keyAliasInKeystore);
            }
            java.lang.String keyEntryAlias = keyAliasInKeystore.substring(android.security.Credentials.USER_SECRET_KEY.length());
            if (!entryAlias.equals(keyEntryAlias)) {
                throw new java.security.KeyStoreException(((("Can only replace KeyStore-backed keys with same" + " alias: ") + entryAlias) + " != ") + keyEntryAlias);
            }
            // This is the entry where this key is already stored. No need to do anything.
            if (params != null) {
                throw new java.security.KeyStoreException("Modifying KeyStore-backed key using protection" + " parameters not supported");
            }
            return;
        }
        if (params == null) {
            throw new java.security.KeyStoreException("Protection parameters must be specified when importing a symmetric key");
        }
        // Not a KeyStore-backed secret key -- import its key material into keystore.
        java.lang.String keyExportFormat = key.getFormat();
        if (keyExportFormat == null) {
            throw new java.security.KeyStoreException("Only secret keys that export their key material are supported");
        } else
            if (!"RAW".equals(keyExportFormat)) {
                throw new java.security.KeyStoreException("Unsupported secret key material export format: " + keyExportFormat);
            }

        byte[] keyMaterial = key.getEncoded();
        if (keyMaterial == null) {
            throw new java.security.KeyStoreException("Key did not export its key material despite supporting" + " RAW format export");
        }
        android.security.keymaster.KeymasterArguments args = new android.security.keymaster.KeymasterArguments();
        try {
            int keymasterAlgorithm = android.security.keystore.KeyProperties.KeyAlgorithm.toKeymasterSecretKeyAlgorithm(key.getAlgorithm());
            args.addEnum(android.security.keymaster.KeymasterDefs.KM_TAG_ALGORITHM, keymasterAlgorithm);
            int[] keymasterDigests;
            if (keymasterAlgorithm == android.security.keymaster.KeymasterDefs.KM_ALGORITHM_HMAC) {
                // JCA HMAC key algorithm implies a digest (e.g., HmacSHA256 key algorithm
                // implies SHA-256 digest). Because keymaster HMAC key is authorized only for one
                // digest, we don't let import parameters override the digest implied by the key.
                // If the parameters specify digests at all, they must specify only one digest, the
                // only implied by key algorithm.
                int keymasterImpliedDigest = android.security.keystore.KeyProperties.KeyAlgorithm.toKeymasterDigest(key.getAlgorithm());
                if (keymasterImpliedDigest == (-1)) {
                    throw new java.security.ProviderException("HMAC key algorithm digest unknown for key algorithm " + key.getAlgorithm());
                }
                keymasterDigests = new int[]{ keymasterImpliedDigest };
                if (params.isDigestsSpecified()) {
                    // Digest(s) explicitly specified in params -- check that the list consists of
                    // exactly one digest, the one implied by key algorithm.
                    int[] keymasterDigestsFromParams = android.security.keystore.KeyProperties.Digest.allToKeymaster(params.getDigests());
                    if ((keymasterDigestsFromParams.length != 1) || (keymasterDigestsFromParams[0] != keymasterImpliedDigest)) {
                        throw new java.security.KeyStoreException((((("Unsupported digests specification: " + java.util.Arrays.asList(params.getDigests())) + ". Only ") + android.security.keystore.KeyProperties.Digest.fromKeymaster(keymasterImpliedDigest)) + " supported for HMAC key algorithm ") + key.getAlgorithm());
                    }
                }
            } else {
                // Key algorithm does not imply a digest.
                if (params.isDigestsSpecified()) {
                    keymasterDigests = android.security.keystore.KeyProperties.Digest.allToKeymaster(params.getDigests());
                } else {
                    keymasterDigests = libcore.util.EmptyArray.INT;
                }
            }
            args.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_DIGEST, keymasterDigests);
            @android.security.keystore.KeyProperties.PurposeEnum
            int purposes = params.getPurposes();
            int[] keymasterBlockModes = android.security.keystore.KeyProperties.BlockMode.allToKeymaster(params.getBlockModes());
            if (((purposes & android.security.keystore.KeyProperties.PURPOSE_ENCRYPT) != 0) && params.isRandomizedEncryptionRequired()) {
                for (int keymasterBlockMode : keymasterBlockModes) {
                    if (!android.security.keystore.KeymasterUtils.isKeymasterBlockModeIndCpaCompatibleWithSymmetricCrypto(keymasterBlockMode)) {
                        throw new java.security.KeyStoreException((("Randomized encryption (IND-CPA) required but may be violated by" + " block mode: ") + android.security.keystore.KeyProperties.BlockMode.fromKeymaster(keymasterBlockMode)) + ". See KeyProtection documentation.");
                    }
                }
            }
            args.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_PURPOSE, android.security.keystore.KeyProperties.Purpose.allToKeymaster(purposes));
            args.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_BLOCK_MODE, keymasterBlockModes);
            if (params.getSignaturePaddings().length > 0) {
                throw new java.security.KeyStoreException("Signature paddings not supported for symmetric keys");
            }
            int[] keymasterPaddings = android.security.keystore.KeyProperties.EncryptionPadding.allToKeymaster(params.getEncryptionPaddings());
            args.addEnums(android.security.keymaster.KeymasterDefs.KM_TAG_PADDING, keymasterPaddings);
            android.security.keystore.KeymasterUtils.addUserAuthArgs(args, params.isUserAuthenticationRequired(), params.getUserAuthenticationValidityDurationSeconds(), params.isUserAuthenticationValidWhileOnBody(), params.isInvalidatedByBiometricEnrollment());
            android.security.keystore.KeymasterUtils.addMinMacLengthAuthorizationIfNecessary(args, keymasterAlgorithm, keymasterBlockModes, keymasterDigests);
            args.addDateIfNotNull(android.security.keymaster.KeymasterDefs.KM_TAG_ACTIVE_DATETIME, params.getKeyValidityStart());
            args.addDateIfNotNull(android.security.keymaster.KeymasterDefs.KM_TAG_ORIGINATION_EXPIRE_DATETIME, params.getKeyValidityForOriginationEnd());
            args.addDateIfNotNull(android.security.keymaster.KeymasterDefs.KM_TAG_USAGE_EXPIRE_DATETIME, params.getKeyValidityForConsumptionEnd());
            if (((purposes & android.security.keystore.KeyProperties.PURPOSE_ENCRYPT) != 0) && (!params.isRandomizedEncryptionRequired())) {
                // Permit caller-provided IV when encrypting with this key
                args.addBoolean(android.security.keymaster.KeymasterDefs.KM_TAG_CALLER_NONCE);
            }
        } catch (java.lang.IllegalArgumentException | java.lang.IllegalStateException e) {
            throw new java.security.KeyStoreException(e);
        }
        android.security.Credentials.deleteAllTypesForAlias(mKeyStore, entryAlias, mUid);
        java.lang.String keyAliasInKeystore = android.security.Credentials.USER_SECRET_KEY + entryAlias;
        int errorCode = // flags
        mKeyStore.importKey(keyAliasInKeystore, args, android.security.keymaster.KeymasterDefs.KM_KEY_FORMAT_RAW, keyMaterial, mUid, 0, new android.security.keymaster.KeyCharacteristics());
        if (errorCode != android.security.KeyStore.NO_ERROR) {
            throw new java.security.KeyStoreException("Failed to import secret key. Keystore error code: " + errorCode);
        }
    }

    @java.lang.Override
    public void engineSetKeyEntry(java.lang.String alias, byte[] userKey, java.security.cert.Certificate[] chain) throws java.security.KeyStoreException {
        throw new java.security.KeyStoreException("Operation not supported because key encoding is unknown");
    }

    @java.lang.Override
    public void engineSetCertificateEntry(java.lang.String alias, java.security.cert.Certificate cert) throws java.security.KeyStoreException {
        if (isKeyEntry(alias)) {
            throw new java.security.KeyStoreException("Entry exists and is not a trusted certificate");
        }
        // We can't set something to null.
        if (cert == null) {
            throw new java.lang.NullPointerException("cert == null");
        }
        final byte[] encoded;
        try {
            encoded = cert.getEncoded();
        } catch (java.security.cert.CertificateEncodingException e) {
            throw new java.security.KeyStoreException(e);
        }
        if (!mKeyStore.put(android.security.Credentials.CA_CERTIFICATE + alias, encoded, mUid, android.security.KeyStore.FLAG_NONE)) {
            throw new java.security.KeyStoreException("Couldn't insert certificate; is KeyStore initialized?");
        }
    }

    @java.lang.Override
    public void engineDeleteEntry(java.lang.String alias) throws java.security.KeyStoreException {
        if (!android.security.Credentials.deleteAllTypesForAlias(mKeyStore, alias, mUid)) {
            throw new java.security.KeyStoreException("Failed to delete entry: " + alias);
        }
    }

    private java.util.Set<java.lang.String> getUniqueAliases() {
        final java.lang.String[] rawAliases = mKeyStore.list("", mUid);
        if (rawAliases == null) {
            return new java.util.HashSet<java.lang.String>();
        }
        final java.util.Set<java.lang.String> aliases = new java.util.HashSet<java.lang.String>(rawAliases.length);
        for (java.lang.String alias : rawAliases) {
            final int idx = alias.indexOf('_');
            if ((idx == (-1)) || (alias.length() <= idx)) {
                android.util.Log.e(android.security.keystore.AndroidKeyStoreSpi.NAME, "invalid alias: " + alias);
                continue;
            }
            aliases.add(new java.lang.String(alias.substring(idx + 1)));
        }
        return aliases;
    }

    @java.lang.Override
    public java.util.Enumeration<java.lang.String> engineAliases() {
        return java.util.Collections.enumeration(getUniqueAliases());
    }

    @java.lang.Override
    public boolean engineContainsAlias(java.lang.String alias) {
        if (alias == null) {
            throw new java.lang.NullPointerException("alias == null");
        }
        return ((mKeyStore.contains(android.security.Credentials.USER_PRIVATE_KEY + alias, mUid) || mKeyStore.contains(android.security.Credentials.USER_SECRET_KEY + alias, mUid)) || mKeyStore.contains(android.security.Credentials.USER_CERTIFICATE + alias, mUid)) || mKeyStore.contains(android.security.Credentials.CA_CERTIFICATE + alias, mUid);
    }

    @java.lang.Override
    public int engineSize() {
        return getUniqueAliases().size();
    }

    @java.lang.Override
    public boolean engineIsKeyEntry(java.lang.String alias) {
        return isKeyEntry(alias);
    }

    private boolean isKeyEntry(java.lang.String alias) {
        return isPrivateKeyEntry(alias) || isSecretKeyEntry(alias);
    }

    private boolean isPrivateKeyEntry(java.lang.String alias) {
        if (alias == null) {
            throw new java.lang.NullPointerException("alias == null");
        }
        return mKeyStore.contains(android.security.Credentials.USER_PRIVATE_KEY + alias, mUid);
    }

    private boolean isSecretKeyEntry(java.lang.String alias) {
        if (alias == null) {
            throw new java.lang.NullPointerException("alias == null");
        }
        return mKeyStore.contains(android.security.Credentials.USER_SECRET_KEY + alias, mUid);
    }

    private boolean isCertificateEntry(java.lang.String alias) {
        if (alias == null) {
            throw new java.lang.NullPointerException("alias == null");
        }
        return mKeyStore.contains(android.security.Credentials.CA_CERTIFICATE + alias, mUid);
    }

    @java.lang.Override
    public boolean engineIsCertificateEntry(java.lang.String alias) {
        return (!isKeyEntry(alias)) && isCertificateEntry(alias);
    }

    @java.lang.Override
    public java.lang.String engineGetCertificateAlias(java.security.cert.Certificate cert) {
        if (cert == null) {
            return null;
        }
        if (!"X.509".equalsIgnoreCase(cert.getType())) {
            // Only X.509 certificates supported
            return null;
        }
        byte[] targetCertBytes;
        try {
            targetCertBytes = cert.getEncoded();
        } catch (java.security.cert.CertificateEncodingException e) {
            return null;
        }
        if (targetCertBytes == null) {
            return null;
        }
        final java.util.Set<java.lang.String> nonCaEntries = new java.util.HashSet<java.lang.String>();
        /* First scan the PrivateKeyEntry types. The KeyStoreSpi documentation
        says to only compare the first certificate in the chain which is
        equivalent to the USER_CERTIFICATE prefix for the Android keystore
        convention.
         */
        final java.lang.String[] certAliases = mKeyStore.list(android.security.Credentials.USER_CERTIFICATE, mUid);
        if (certAliases != null) {
            for (java.lang.String alias : certAliases) {
                final byte[] certBytes = mKeyStore.get(android.security.Credentials.USER_CERTIFICATE + alias, mUid);
                if (certBytes == null) {
                    continue;
                }
                nonCaEntries.add(alias);
                if (java.util.Arrays.equals(certBytes, targetCertBytes)) {
                    return alias;
                }
            }
        }
        /* Look at all the TrustedCertificateEntry types. Skip all the
        PrivateKeyEntry we looked at above.
         */
        final java.lang.String[] caAliases = mKeyStore.list(android.security.Credentials.CA_CERTIFICATE, mUid);
        if (certAliases != null) {
            for (java.lang.String alias : caAliases) {
                if (nonCaEntries.contains(alias)) {
                    continue;
                }
                final byte[] certBytes = mKeyStore.get(android.security.Credentials.CA_CERTIFICATE + alias, mUid);
                if (certBytes == null) {
                    continue;
                }
                if (java.util.Arrays.equals(certBytes, targetCertBytes)) {
                    return alias;
                }
            }
        }
        return null;
    }

    @java.lang.Override
    public void engineStore(java.io.OutputStream stream, char[] password) throws java.io.IOException, java.security.NoSuchAlgorithmException, java.security.cert.CertificateException {
        throw new java.lang.UnsupportedOperationException("Can not serialize AndroidKeyStore to OutputStream");
    }

    @java.lang.Override
    public void engineLoad(java.io.InputStream stream, char[] password) throws java.io.IOException, java.security.NoSuchAlgorithmException, java.security.cert.CertificateException {
        if (stream != null) {
            throw new java.lang.IllegalArgumentException("InputStream not supported");
        }
        if (password != null) {
            throw new java.lang.IllegalArgumentException("password not supported");
        }
        // Unfortunate name collision.
        mKeyStore = android.security.KeyStore.getInstance();
        mUid = android.security.KeyStore.UID_SELF;
    }

    @java.lang.Override
    public void engineLoad(java.security.KeyStore.LoadStoreParameter param) throws java.io.IOException, java.security.NoSuchAlgorithmException, java.security.cert.CertificateException {
        int uid = android.security.KeyStore.UID_SELF;
        if (param != null) {
            if (param instanceof android.security.keystore.AndroidKeyStoreLoadStoreParameter) {
                uid = ((android.security.keystore.AndroidKeyStoreLoadStoreParameter) (param)).getUid();
            } else {
                throw new java.lang.IllegalArgumentException("Unsupported param type: " + param.getClass());
            }
        }
        mKeyStore = android.security.KeyStore.getInstance();
        mUid = uid;
    }

    @java.lang.Override
    public void engineSetEntry(java.lang.String alias, java.security.KeyStore.Entry entry, java.security.KeyStore.ProtectionParameter param) throws java.security.KeyStoreException {
        if (entry == null) {
            throw new java.security.KeyStoreException("entry == null");
        }
        android.security.Credentials.deleteAllTypesForAlias(mKeyStore, alias, mUid);
        if (entry instanceof java.security.KeyStore.TrustedCertificateEntry) {
            java.security.KeyStore.TrustedCertificateEntry trE = ((java.security.KeyStore.TrustedCertificateEntry) (entry));
            engineSetCertificateEntry(alias, trE.getTrustedCertificate());
            return;
        }
        if (entry instanceof java.security.KeyStore.PrivateKeyEntry) {
            java.security.KeyStore.PrivateKeyEntry prE = ((java.security.KeyStore.PrivateKeyEntry) (entry));
            setPrivateKeyEntry(alias, prE.getPrivateKey(), prE.getCertificateChain(), param);
        } else
            if (entry instanceof java.security.KeyStore.SecretKeyEntry) {
                java.security.KeyStore.SecretKeyEntry secE = ((java.security.KeyStore.SecretKeyEntry) (entry));
                setSecretKeyEntry(alias, secE.getSecretKey(), param);
            } else {
                throw new java.security.KeyStoreException(("Entry must be a PrivateKeyEntry, SecretKeyEntry or TrustedCertificateEntry" + "; was ") + entry);
            }

    }

    /**
     * {@link X509Certificate} which returns {@link AndroidKeyStorePublicKey} from
     * {@link #getPublicKey()}. This is so that crypto operations on these public keys contain
     * can find out which keystore private key entry to use. This is needed so that Android Keystore
     * crypto operations using public keys can find out which key alias to use. These operations
     * require an alias.
     */
    static class KeyStoreX509Certificate extends android.security.keystore.DelegatingX509Certificate {
        private final java.lang.String mPrivateKeyAlias;

        private final int mPrivateKeyUid;

        KeyStoreX509Certificate(java.lang.String privateKeyAlias, int privateKeyUid, java.security.cert.X509Certificate delegate) {
            super(delegate);
            mPrivateKeyAlias = privateKeyAlias;
            mPrivateKeyUid = privateKeyUid;
        }

        @java.lang.Override
        public java.security.PublicKey getPublicKey() {
            java.security.PublicKey original = super.getPublicKey();
            return android.security.keystore.AndroidKeyStoreProvider.getAndroidKeyStorePublicKey(mPrivateKeyAlias, mPrivateKeyUid, original.getAlgorithm(), original.getEncoded());
        }
    }
}

