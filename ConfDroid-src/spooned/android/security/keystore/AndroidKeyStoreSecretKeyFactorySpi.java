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
 * {@link SecretKeyFactorySpi} backed by Android Keystore.
 *
 * @unknown 
 */
public class AndroidKeyStoreSecretKeyFactorySpi extends javax.crypto.SecretKeyFactorySpi {
    private final android.security.KeyStore mKeyStore = android.security.KeyStore.getInstance();

    @java.lang.Override
    protected java.security.spec.KeySpec engineGetKeySpec(javax.crypto.SecretKey key, @java.lang.SuppressWarnings("rawtypes")
    java.lang.Class keySpecClass) throws java.security.spec.InvalidKeySpecException {
        if (keySpecClass == null) {
            throw new java.security.spec.InvalidKeySpecException("keySpecClass == null");
        }
        if (!(key instanceof android.security.keystore.AndroidKeyStoreSecretKey)) {
            throw new java.security.spec.InvalidKeySpecException("Only Android KeyStore secret keys supported: " + (key != null ? key.getClass().getName() : "null"));
        }
        if (javax.crypto.spec.SecretKeySpec.class.isAssignableFrom(keySpecClass)) {
            throw new java.security.spec.InvalidKeySpecException("Key material export of Android KeyStore keys is not supported");
        }
        if (!android.security.keystore.KeyInfo.class.equals(keySpecClass)) {
            throw new java.security.spec.InvalidKeySpecException("Unsupported key spec: " + keySpecClass.getName());
        }
        android.security.keystore.AndroidKeyStoreKey keystoreKey = ((android.security.keystore.AndroidKeyStoreKey) (key));
        java.lang.String keyAliasInKeystore = keystoreKey.getAlias();
        java.lang.String entryAlias;
        if (keyAliasInKeystore.startsWith(android.security.Credentials.USER_SECRET_KEY)) {
            entryAlias = keyAliasInKeystore.substring(android.security.Credentials.USER_SECRET_KEY.length());
        } else {
            throw new java.security.spec.InvalidKeySpecException("Invalid key alias: " + keyAliasInKeystore);
        }
        return android.security.keystore.AndroidKeyStoreSecretKeyFactorySpi.getKeyInfo(mKeyStore, entryAlias, keyAliasInKeystore, keystoreKey.getUid());
    }

    static android.security.keystore.KeyInfo getKeyInfo(android.security.KeyStore keyStore, java.lang.String entryAlias, java.lang.String keyAliasInKeystore, int keyUid) {
        android.security.keymaster.KeyCharacteristics keyCharacteristics = new android.security.keymaster.KeyCharacteristics();
        int errorCode = keyStore.getKeyCharacteristics(keyAliasInKeystore, null, null, keyUid, keyCharacteristics);
        if (errorCode != android.security.KeyStore.NO_ERROR) {
            throw new java.security.ProviderException(("Failed to obtain information about key." + " Keystore error: ") + errorCode);
        }
        boolean insideSecureHardware;
        @android.security.keystore.KeyProperties.OriginEnum
        int origin;
        int keySize;
        @android.security.keystore.KeyProperties.PurposeEnum
        int purposes;
        java.lang.String[] encryptionPaddings;
        java.lang.String[] signaturePaddings;
        @android.security.keystore.KeyProperties.DigestEnum
        java.lang.String[] digests;
        @android.security.keystore.KeyProperties.BlockModeEnum
        java.lang.String[] blockModes;
        int keymasterSwEnforcedUserAuthenticators;
        int keymasterHwEnforcedUserAuthenticators;
        java.util.List<java.math.BigInteger> keymasterSecureUserIds;
        try {
            if (keyCharacteristics.hwEnforced.containsTag(android.security.keymaster.KeymasterDefs.KM_TAG_ORIGIN)) {
                insideSecureHardware = true;
                origin = android.security.keystore.KeyProperties.Origin.fromKeymaster(keyCharacteristics.hwEnforced.getEnum(android.security.keymaster.KeymasterDefs.KM_TAG_ORIGIN, -1));
            } else
                if (keyCharacteristics.swEnforced.containsTag(android.security.keymaster.KeymasterDefs.KM_TAG_ORIGIN)) {
                    insideSecureHardware = false;
                    origin = android.security.keystore.KeyProperties.Origin.fromKeymaster(keyCharacteristics.swEnforced.getEnum(android.security.keymaster.KeymasterDefs.KM_TAG_ORIGIN, -1));
                } else {
                    throw new java.security.ProviderException("Key origin not available");
                }

            long keySizeUnsigned = keyCharacteristics.getUnsignedInt(android.security.keymaster.KeymasterDefs.KM_TAG_KEY_SIZE, -1);
            if (keySizeUnsigned == (-1)) {
                throw new java.security.ProviderException("Key size not available");
            } else
                if (keySizeUnsigned > java.lang.Integer.MAX_VALUE) {
                    throw new java.security.ProviderException(("Key too large: " + keySizeUnsigned) + " bits");
                }

            keySize = ((int) (keySizeUnsigned));
            purposes = android.security.keystore.KeyProperties.Purpose.allFromKeymaster(keyCharacteristics.getEnums(android.security.keymaster.KeymasterDefs.KM_TAG_PURPOSE));
            java.util.List<java.lang.String> encryptionPaddingsList = new java.util.ArrayList<java.lang.String>();
            java.util.List<java.lang.String> signaturePaddingsList = new java.util.ArrayList<java.lang.String>();
            // Keymaster stores both types of paddings in the same array -- we split it into two.
            for (int keymasterPadding : keyCharacteristics.getEnums(android.security.keymaster.KeymasterDefs.KM_TAG_PADDING)) {
                try {
                    @android.security.keystore.KeyProperties.EncryptionPaddingEnum
                    java.lang.String jcaPadding = android.security.keystore.KeyProperties.EncryptionPadding.fromKeymaster(keymasterPadding);
                    encryptionPaddingsList.add(jcaPadding);
                } catch (java.lang.IllegalArgumentException e) {
                    try {
                        @android.security.keystore.KeyProperties.SignaturePaddingEnum
                        java.lang.String padding = android.security.keystore.KeyProperties.SignaturePadding.fromKeymaster(keymasterPadding);
                        signaturePaddingsList.add(padding);
                    } catch (java.lang.IllegalArgumentException e2) {
                        throw new java.security.ProviderException("Unsupported encryption padding: " + keymasterPadding);
                    }
                }
            }
            encryptionPaddings = encryptionPaddingsList.toArray(new java.lang.String[encryptionPaddingsList.size()]);
            signaturePaddings = signaturePaddingsList.toArray(new java.lang.String[signaturePaddingsList.size()]);
            digests = android.security.keystore.KeyProperties.Digest.allFromKeymaster(keyCharacteristics.getEnums(android.security.keymaster.KeymasterDefs.KM_TAG_DIGEST));
            blockModes = android.security.keystore.KeyProperties.BlockMode.allFromKeymaster(keyCharacteristics.getEnums(android.security.keymaster.KeymasterDefs.KM_TAG_BLOCK_MODE));
            keymasterSwEnforcedUserAuthenticators = keyCharacteristics.swEnforced.getEnum(android.security.keymaster.KeymasterDefs.KM_TAG_USER_AUTH_TYPE, 0);
            keymasterHwEnforcedUserAuthenticators = keyCharacteristics.hwEnforced.getEnum(android.security.keymaster.KeymasterDefs.KM_TAG_USER_AUTH_TYPE, 0);
            keymasterSecureUserIds = keyCharacteristics.getUnsignedLongs(android.security.keymaster.KeymasterDefs.KM_TAG_USER_SECURE_ID);
        } catch (java.lang.IllegalArgumentException e) {
            throw new java.security.ProviderException("Unsupported key characteristic", e);
        }
        java.util.Date keyValidityStart = keyCharacteristics.getDate(android.security.keymaster.KeymasterDefs.KM_TAG_ACTIVE_DATETIME);
        java.util.Date keyValidityForOriginationEnd = keyCharacteristics.getDate(android.security.keymaster.KeymasterDefs.KM_TAG_ORIGINATION_EXPIRE_DATETIME);
        java.util.Date keyValidityForConsumptionEnd = keyCharacteristics.getDate(android.security.keymaster.KeymasterDefs.KM_TAG_USAGE_EXPIRE_DATETIME);
        boolean userAuthenticationRequired = !keyCharacteristics.getBoolean(android.security.keymaster.KeymasterDefs.KM_TAG_NO_AUTH_REQUIRED);
        long userAuthenticationValidityDurationSeconds = keyCharacteristics.getUnsignedInt(android.security.keymaster.KeymasterDefs.KM_TAG_AUTH_TIMEOUT, -1);
        if (userAuthenticationValidityDurationSeconds > java.lang.Integer.MAX_VALUE) {
            throw new java.security.ProviderException(("User authentication timeout validity too long: " + userAuthenticationValidityDurationSeconds) + " seconds");
        }
        boolean userAuthenticationRequirementEnforcedBySecureHardware = (userAuthenticationRequired && (keymasterHwEnforcedUserAuthenticators != 0)) && (keymasterSwEnforcedUserAuthenticators == 0);
        boolean userAuthenticationValidWhileOnBody = keyCharacteristics.hwEnforced.getBoolean(android.security.keymaster.KeymasterDefs.KM_TAG_ALLOW_WHILE_ON_BODY);
        boolean invalidatedByBiometricEnrollment = false;
        if ((keymasterSwEnforcedUserAuthenticators == android.security.keymaster.KeymasterDefs.HW_AUTH_FINGERPRINT) || (keymasterHwEnforcedUserAuthenticators == android.security.keymaster.KeymasterDefs.HW_AUTH_FINGERPRINT)) {
            // Fingerprint-only key; will be invalidated if the root SID isn't in the SID list.
            invalidatedByBiometricEnrollment = ((keymasterSecureUserIds != null) && (!keymasterSecureUserIds.isEmpty())) && (!keymasterSecureUserIds.contains(android.security.keystore.AndroidKeyStoreSecretKeyFactorySpi.getGateKeeperSecureUserId()));
        }
        return new android.security.keystore.KeyInfo(entryAlias, insideSecureHardware, origin, keySize, keyValidityStart, keyValidityForOriginationEnd, keyValidityForConsumptionEnd, purposes, encryptionPaddings, signaturePaddings, digests, blockModes, userAuthenticationRequired, ((int) (userAuthenticationValidityDurationSeconds)), userAuthenticationRequirementEnforcedBySecureHardware, userAuthenticationValidWhileOnBody, invalidatedByBiometricEnrollment);
    }

    private static java.math.BigInteger getGateKeeperSecureUserId() throws java.security.ProviderException {
        try {
            return java.math.BigInteger.valueOf(android.security.GateKeeper.getSecureUserId());
        } catch (java.lang.IllegalStateException e) {
            throw new java.security.ProviderException("Failed to get GateKeeper secure user ID", e);
        }
    }

    @java.lang.Override
    protected javax.crypto.SecretKey engineGenerateSecret(java.security.spec.KeySpec keySpec) throws java.security.spec.InvalidKeySpecException {
        throw new java.security.spec.InvalidKeySpecException("To generate secret key in Android Keystore, use KeyGenerator initialized with " + android.security.keystore.KeyGenParameterSpec.class.getName());
    }

    @java.lang.Override
    protected javax.crypto.SecretKey engineTranslateKey(javax.crypto.SecretKey key) throws java.security.InvalidKeyException {
        if (key == null) {
            throw new java.security.InvalidKeyException("key == null");
        } else
            if (!(key instanceof android.security.keystore.AndroidKeyStoreSecretKey)) {
                throw new java.security.InvalidKeyException("To import a secret key into Android Keystore, use KeyStore.setEntry");
            }

        return key;
    }
}

