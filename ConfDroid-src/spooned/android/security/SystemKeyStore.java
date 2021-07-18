/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.security;


/**
 *
 *
 * @unknown 
 */
public class SystemKeyStore {
    private static final java.lang.String SYSTEM_KEYSTORE_DIRECTORY = "misc/systemkeys";

    private static final java.lang.String KEY_FILE_EXTENSION = ".sks";

    private static android.security.SystemKeyStore mInstance = new android.security.SystemKeyStore();

    private SystemKeyStore() {
    }

    public static android.security.SystemKeyStore getInstance() {
        return android.security.SystemKeyStore.mInstance;
    }

    public static java.lang.String toHexString(byte[] keyData) {
        if (keyData == null) {
            return null;
        }
        int keyLen = keyData.length;
        int expectedStringLen = keyData.length * 2;
        java.lang.StringBuilder sb = new java.lang.StringBuilder(expectedStringLen);
        for (int i = 0; i < keyData.length; i++) {
            java.lang.String hexStr = java.lang.Integer.toString(keyData[i] & 0xff, 16);
            if (hexStr.length() == 1) {
                hexStr = "0" + hexStr;
            }
            sb.append(hexStr);
        }
        return sb.toString();
    }

    public java.lang.String generateNewKeyHexString(int numBits, java.lang.String algName, java.lang.String keyName) throws java.security.NoSuchAlgorithmException {
        return android.security.SystemKeyStore.toHexString(generateNewKey(numBits, algName, keyName));
    }

    public byte[] generateNewKey(int numBits, java.lang.String algName, java.lang.String keyName) throws java.security.NoSuchAlgorithmException {
        // Check if key with similar name exists. If so, return null.
        java.io.File keyFile = getKeyFile(keyName);
        if (keyFile.exists()) {
            throw new java.lang.IllegalArgumentException();
        }
        javax.crypto.KeyGenerator skg = javax.crypto.KeyGenerator.getInstance(algName);
        java.security.SecureRandom srng = java.security.SecureRandom.getInstance("SHA1PRNG");
        skg.init(numBits, srng);
        javax.crypto.SecretKey sk = skg.generateKey();
        byte[] retKey = sk.getEncoded();
        try {
            // Store the key
            if (!keyFile.createNewFile()) {
                throw new java.lang.IllegalArgumentException();
            }
            java.io.FileOutputStream fos = new java.io.FileOutputStream(keyFile);
            fos.write(retKey);
            fos.flush();
            android.os.FileUtils.sync(fos);
            fos.close();
            android.os.FileUtils.setPermissions(keyFile.getName(), android.os.FileUtils.S_IRUSR | android.os.FileUtils.S_IWUSR, -1, -1);
        } catch (java.io.IOException ioe) {
            return null;
        }
        return retKey;
    }

    private java.io.File getKeyFile(java.lang.String keyName) {
        java.io.File sysKeystoreDir = new java.io.File(android.os.Environment.getDataDirectory(), android.security.SystemKeyStore.SYSTEM_KEYSTORE_DIRECTORY);
        java.io.File keyFile = new java.io.File(sysKeystoreDir, keyName + android.security.SystemKeyStore.KEY_FILE_EXTENSION);
        return keyFile;
    }

    public java.lang.String retrieveKeyHexString(java.lang.String keyName) throws java.io.IOException {
        return android.security.SystemKeyStore.toHexString(retrieveKey(keyName));
    }

    public byte[] retrieveKey(java.lang.String keyName) throws java.io.IOException {
        java.io.File keyFile = getKeyFile(keyName);
        if (!keyFile.exists()) {
            return null;
        }
        return libcore.io.IoUtils.readFileAsByteArray(keyFile.toString());
    }

    public void deleteKey(java.lang.String keyName) {
        // Get the file first.
        java.io.File keyFile = getKeyFile(keyName);
        if (!keyFile.exists()) {
            throw new java.lang.IllegalArgumentException();
        }
        keyFile.delete();
    }
}

