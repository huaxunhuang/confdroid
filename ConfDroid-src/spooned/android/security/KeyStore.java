/**
 * Copyright (C) 2009 The Android Open Source Project
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
 * @unknown This should not be made public in its present form because it
assumes that private and secret key bytes are available and would
preclude the use of hardware crypto.
 */
public class KeyStore {
    private static final java.lang.String TAG = "KeyStore";

    // ResponseCodes
    public static final int NO_ERROR = 1;

    public static final int LOCKED = 2;

    public static final int UNINITIALIZED = 3;

    public static final int SYSTEM_ERROR = 4;

    public static final int PROTOCOL_ERROR = 5;

    public static final int PERMISSION_DENIED = 6;

    public static final int KEY_NOT_FOUND = 7;

    public static final int VALUE_CORRUPTED = 8;

    public static final int UNDEFINED_ACTION = 9;

    public static final int WRONG_PASSWORD = 10;

    /**
     * Per operation authentication is needed before this operation is valid.
     * This is returned from {@link #begin} when begin succeeds but the operation uses
     * per-operation authentication and must authenticate before calling {@link #update} or
     * {@link #finish}.
     */
    public static final int OP_AUTH_NEEDED = 15;

    // Used for UID field to indicate the calling UID.
    public static final int UID_SELF = -1;

    // Flags for "put" "import" and "generate"
    public static final int FLAG_NONE = 0;

    /**
     * Indicates that this key (or key pair) must be encrypted at rest. This will protect the key
     * (or key pair) with the secure lock screen credential (e.g., password, PIN, or pattern).
     *
     * <p>Note that this requires that the secure lock screen (e.g., password, PIN, pattern) is set
     * up, otherwise key (or key pair) generation or import will fail. Moreover, this key (or key
     * pair) will be deleted when the secure lock screen is disabled or reset (e.g., by the user or
     * a Device Administrator). Finally, this key (or key pair) cannot be used until the user
     * unlocks the secure lock screen after boot.
     *
     * @see KeyguardManager#isDeviceSecure()
     */
    public static final int FLAG_ENCRYPTED = 1;

    // States
    public enum State {

        UNLOCKED,
        LOCKED,
        UNINITIALIZED;}

    private int mError = android.security.KeyStore.NO_ERROR;

    private final android.security.IKeystoreService mBinder;

    private final android.content.Context mContext;

    private android.os.IBinder mToken;

    private KeyStore(android.security.IKeystoreService binder) {
        mBinder = binder;
        mContext = android.security.KeyStore.getApplicationContext();
    }

    public static android.content.Context getApplicationContext() {
        android.app.Application application = android.app.ActivityThread.currentApplication();
        if (application == null) {
            throw new java.lang.IllegalStateException("Failed to obtain application Context from ActivityThread");
        }
        return application;
    }

    public static android.security.KeyStore getInstance() {
        android.security.IKeystoreService keystore = IKeystoreService.Stub.asInterface(android.os.ServiceManager.getService("android.security.keystore"));
        return new android.security.KeyStore(keystore);
    }

    private synchronized android.os.IBinder getToken() {
        if (mToken == null) {
            mToken = new android.os.Binder();
        }
        return mToken;
    }

    public android.security.KeyStore.State state(int userId) {
        final int ret;
        try {
            ret = mBinder.getState(userId);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            throw new java.lang.AssertionError(e);
        }
        switch (ret) {
            case android.security.KeyStore.NO_ERROR :
                return android.security.KeyStore.State.UNLOCKED;
            case android.security.KeyStore.LOCKED :
                return android.security.KeyStore.State.LOCKED;
            case android.security.KeyStore.UNINITIALIZED :
                return android.security.KeyStore.State.UNINITIALIZED;
            default :
                throw new java.lang.AssertionError(mError);
        }
    }

    public android.security.KeyStore.State state() {
        return state(android.os.UserHandle.myUserId());
    }

    public boolean isUnlocked() {
        return state() == android.security.KeyStore.State.UNLOCKED;
    }

    public byte[] get(java.lang.String key, int uid) {
        try {
            return mBinder.get(key, uid);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public byte[] get(java.lang.String key) {
        return get(key, android.security.KeyStore.UID_SELF);
    }

    public boolean put(java.lang.String key, byte[] value, int uid, int flags) {
        return insert(key, value, uid, flags) == android.security.KeyStore.NO_ERROR;
    }

    public int insert(java.lang.String key, byte[] value, int uid, int flags) {
        try {
            return mBinder.insert(key, value, uid, flags);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return android.security.KeyStore.SYSTEM_ERROR;
        }
    }

    public boolean delete(java.lang.String key, int uid) {
        try {
            int ret = mBinder.del(key, uid);
            return (ret == android.security.KeyStore.NO_ERROR) || (ret == android.security.KeyStore.KEY_NOT_FOUND);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean delete(java.lang.String key) {
        return delete(key, android.security.KeyStore.UID_SELF);
    }

    public boolean contains(java.lang.String key, int uid) {
        try {
            return mBinder.exist(key, uid) == android.security.KeyStore.NO_ERROR;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean contains(java.lang.String key) {
        return contains(key, android.security.KeyStore.UID_SELF);
    }

    /**
     * List all entries in the keystore for {@code uid} starting with {@code prefix}.
     */
    public java.lang.String[] list(java.lang.String prefix, int uid) {
        try {
            return mBinder.list(prefix, uid);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public java.lang.String[] list(java.lang.String prefix) {
        return list(prefix, android.security.KeyStore.UID_SELF);
    }

    public boolean reset() {
        try {
            return mBinder.reset() == android.security.KeyStore.NO_ERROR;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    /**
     * Attempt to lock the keystore for {@code user}.
     *
     * @param user
     * 		Android user to lock.
     * @return whether {@code user}'s keystore was locked.
     */
    public boolean lock(int userId) {
        try {
            return mBinder.lock(userId) == android.security.KeyStore.NO_ERROR;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean lock() {
        return lock(android.os.UserHandle.myUserId());
    }

    /**
     * Attempt to unlock the keystore for {@code user} with the password {@code password}.
     * This is required before keystore entries created with FLAG_ENCRYPTED can be accessed or
     * created.
     *
     * @param user
     * 		Android user ID to operate on
     * @param password
     * 		user's keystore password. Should be the most recent value passed to
     * 		{@link #onUserPasswordChanged} for the user.
     * @return whether the keystore was unlocked.
     */
    public boolean unlock(int userId, java.lang.String password) {
        try {
            mError = mBinder.unlock(userId, password);
            return mError == android.security.KeyStore.NO_ERROR;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean unlock(java.lang.String password) {
        return unlock(android.os.UserHandle.getUserId(android.os.Process.myUid()), password);
    }

    /**
     * Check if the keystore for {@code userId} is empty.
     */
    public boolean isEmpty(int userId) {
        try {
            return mBinder.isEmpty(userId) != 0;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean isEmpty() {
        return isEmpty(android.os.UserHandle.myUserId());
    }

    public boolean generate(java.lang.String key, int uid, int keyType, int keySize, int flags, byte[][] args) {
        try {
            return mBinder.generate(key, uid, keyType, keySize, flags, new android.security.KeystoreArguments(args)) == android.security.KeyStore.NO_ERROR;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean importKey(java.lang.String keyName, byte[] key, int uid, int flags) {
        try {
            return mBinder.import_key(keyName, key, uid, flags) == android.security.KeyStore.NO_ERROR;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public byte[] sign(java.lang.String key, byte[] data) {
        try {
            return mBinder.sign(key, data);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public boolean verify(java.lang.String key, byte[] data, byte[] signature) {
        try {
            return mBinder.verify(key, data, signature) == android.security.KeyStore.NO_ERROR;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean grant(java.lang.String key, int uid) {
        try {
            return mBinder.grant(key, uid) == android.security.KeyStore.NO_ERROR;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean ungrant(java.lang.String key, int uid) {
        try {
            return mBinder.ungrant(key, uid) == android.security.KeyStore.NO_ERROR;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    /**
     * Returns the last modification time of the key in milliseconds since the
     * epoch. Will return -1L if the key could not be found or other error.
     */
    public long getmtime(java.lang.String key, int uid) {
        try {
            final long millis = mBinder.getmtime(key, uid);
            if (millis == (-1L)) {
                return -1L;
            }
            return millis * 1000L;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return -1L;
        }
    }

    public long getmtime(java.lang.String key) {
        return getmtime(key, android.security.KeyStore.UID_SELF);
    }

    public boolean duplicate(java.lang.String srcKey, int srcUid, java.lang.String destKey, int destUid) {
        try {
            return mBinder.duplicate(srcKey, srcUid, destKey, destUid) == android.security.KeyStore.NO_ERROR;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    // TODO: remove this when it's removed from Settings
    public boolean isHardwareBacked() {
        return isHardwareBacked("RSA");
    }

    public boolean isHardwareBacked(java.lang.String keyType) {
        try {
            return mBinder.is_hardware_backed(keyType.toUpperCase(java.util.Locale.US)) == android.security.KeyStore.NO_ERROR;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean clearUid(int uid) {
        try {
            return mBinder.clear_uid(uid) == android.security.KeyStore.NO_ERROR;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public int getLastError() {
        return mError;
    }

    public boolean addRngEntropy(byte[] data) {
        try {
            return mBinder.addRngEntropy(data) == android.security.KeyStore.NO_ERROR;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public int generateKey(java.lang.String alias, android.security.keymaster.KeymasterArguments args, byte[] entropy, int uid, int flags, android.security.keymaster.KeyCharacteristics outCharacteristics) {
        try {
            return mBinder.generateKey(alias, args, entropy, uid, flags, outCharacteristics);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return android.security.KeyStore.SYSTEM_ERROR;
        }
    }

    public int generateKey(java.lang.String alias, android.security.keymaster.KeymasterArguments args, byte[] entropy, int flags, android.security.keymaster.KeyCharacteristics outCharacteristics) {
        return generateKey(alias, args, entropy, android.security.KeyStore.UID_SELF, flags, outCharacteristics);
    }

    public int getKeyCharacteristics(java.lang.String alias, android.security.keymaster.KeymasterBlob clientId, android.security.keymaster.KeymasterBlob appId, int uid, android.security.keymaster.KeyCharacteristics outCharacteristics) {
        try {
            return mBinder.getKeyCharacteristics(alias, clientId, appId, uid, outCharacteristics);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return android.security.KeyStore.SYSTEM_ERROR;
        }
    }

    public int getKeyCharacteristics(java.lang.String alias, android.security.keymaster.KeymasterBlob clientId, android.security.keymaster.KeymasterBlob appId, android.security.keymaster.KeyCharacteristics outCharacteristics) {
        return getKeyCharacteristics(alias, clientId, appId, android.security.KeyStore.UID_SELF, outCharacteristics);
    }

    public int importKey(java.lang.String alias, android.security.keymaster.KeymasterArguments args, int format, byte[] keyData, int uid, int flags, android.security.keymaster.KeyCharacteristics outCharacteristics) {
        try {
            return mBinder.importKey(alias, args, format, keyData, uid, flags, outCharacteristics);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return android.security.KeyStore.SYSTEM_ERROR;
        }
    }

    public int importKey(java.lang.String alias, android.security.keymaster.KeymasterArguments args, int format, byte[] keyData, int flags, android.security.keymaster.KeyCharacteristics outCharacteristics) {
        return importKey(alias, args, format, keyData, android.security.KeyStore.UID_SELF, flags, outCharacteristics);
    }

    public android.security.keymaster.ExportResult exportKey(java.lang.String alias, int format, android.security.keymaster.KeymasterBlob clientId, android.security.keymaster.KeymasterBlob appId, int uid) {
        try {
            return mBinder.exportKey(alias, format, clientId, appId, uid);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public android.security.keymaster.ExportResult exportKey(java.lang.String alias, int format, android.security.keymaster.KeymasterBlob clientId, android.security.keymaster.KeymasterBlob appId) {
        return exportKey(alias, format, clientId, appId, android.security.KeyStore.UID_SELF);
    }

    public android.security.keymaster.OperationResult begin(java.lang.String alias, int purpose, boolean pruneable, android.security.keymaster.KeymasterArguments args, byte[] entropy, int uid) {
        try {
            return mBinder.begin(getToken(), alias, purpose, pruneable, args, entropy, uid);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public android.security.keymaster.OperationResult begin(java.lang.String alias, int purpose, boolean pruneable, android.security.keymaster.KeymasterArguments args, byte[] entropy) {
        return begin(alias, purpose, pruneable, args, entropy, android.security.KeyStore.UID_SELF);
    }

    public android.security.keymaster.OperationResult update(android.os.IBinder token, android.security.keymaster.KeymasterArguments arguments, byte[] input) {
        try {
            return mBinder.update(token, arguments, input);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public android.security.keymaster.OperationResult finish(android.os.IBinder token, android.security.keymaster.KeymasterArguments arguments, byte[] signature, byte[] entropy) {
        try {
            return mBinder.finish(token, arguments, signature, entropy);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public android.security.keymaster.OperationResult finish(android.os.IBinder token, android.security.keymaster.KeymasterArguments arguments, byte[] signature) {
        return finish(token, arguments, signature, null);
    }

    public int abort(android.os.IBinder token) {
        try {
            return mBinder.abort(token);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return android.security.KeyStore.SYSTEM_ERROR;
        }
    }

    /**
     * Check if the operation referenced by {@code token} is currently authorized.
     *
     * @param token
     * 		An operation token returned by a call to
     * 		{@link #begin(String, int, boolean, KeymasterArguments, byte[], KeymasterArguments) begin}.
     */
    public boolean isOperationAuthorized(android.os.IBinder token) {
        try {
            return mBinder.isOperationAuthorized(token);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    /**
     * Add an authentication record to the keystore authorization table.
     *
     * @param authToken
     * 		The packed bytes of a hw_auth_token_t to be provided to keymaster.
     * @return {@code KeyStore.NO_ERROR} on success, otherwise an error value corresponding to
    a {@code KeymasterDefs.KM_ERROR_} value or {@code KeyStore} ResponseCode.
     */
    public int addAuthToken(byte[] authToken) {
        try {
            return mBinder.addAuthToken(authToken);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return android.security.KeyStore.SYSTEM_ERROR;
        }
    }

    /**
     * Notify keystore that a user's password has changed.
     *
     * @param userId
     * 		the user whose password changed.
     * @param newPassword
     * 		the new password or "" if the password was removed.
     */
    public boolean onUserPasswordChanged(int userId, java.lang.String newPassword) {
        // Parcel.cpp doesn't support deserializing null strings and treats them as "". Make that
        // explicit here.
        if (newPassword == null) {
            newPassword = "";
        }
        try {
            return mBinder.onUserPasswordChanged(userId, newPassword) == android.security.KeyStore.NO_ERROR;
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    /**
     * Notify keystore that a user was added.
     *
     * @param userId
     * 		the new user.
     * @param parentId
     * 		the parent of the new user, or -1 if the user has no parent. If parentId is
     * 		specified then the new user's keystore will be intialized with the same secure lockscreen
     * 		password as the parent.
     */
    public void onUserAdded(int userId, int parentId) {
        try {
            mBinder.onUserAdded(userId, parentId);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
        }
    }

    /**
     * Notify keystore that a user was added.
     *
     * @param userId
     * 		the new user.
     */
    public void onUserAdded(int userId) {
        onUserAdded(userId, -1);
    }

    /**
     * Notify keystore that a user was removed.
     *
     * @param userId
     * 		the removed user.
     */
    public void onUserRemoved(int userId) {
        try {
            mBinder.onUserRemoved(userId);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
        }
    }

    public boolean onUserPasswordChanged(java.lang.String newPassword) {
        return onUserPasswordChanged(android.os.UserHandle.getUserId(android.os.Process.myUid()), newPassword);
    }

    public int attestKey(java.lang.String alias, android.security.keymaster.KeymasterArguments params, android.security.keymaster.KeymasterCertificateChain outChain) {
        try {
            return mBinder.attestKey(alias, params, outChain);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.security.KeyStore.TAG, "Cannot connect to keystore", e);
            return android.security.KeyStore.SYSTEM_ERROR;
        }
    }

    /**
     * Returns a {@link KeyStoreException} corresponding to the provided keystore/keymaster error
     * code.
     */
    public static android.security.KeyStoreException getKeyStoreException(int errorCode) {
        if (errorCode > 0) {
            // KeyStore layer error
            switch (errorCode) {
                case android.security.KeyStore.NO_ERROR :
                    return new android.security.KeyStoreException(errorCode, "OK");
                case android.security.KeyStore.LOCKED :
                    return new android.security.KeyStoreException(errorCode, "User authentication required");
                case android.security.KeyStore.UNINITIALIZED :
                    return new android.security.KeyStoreException(errorCode, "Keystore not initialized");
                case android.security.KeyStore.SYSTEM_ERROR :
                    return new android.security.KeyStoreException(errorCode, "System error");
                case android.security.KeyStore.PERMISSION_DENIED :
                    return new android.security.KeyStoreException(errorCode, "Permission denied");
                case android.security.KeyStore.KEY_NOT_FOUND :
                    return new android.security.KeyStoreException(errorCode, "Key not found");
                case android.security.KeyStore.VALUE_CORRUPTED :
                    return new android.security.KeyStoreException(errorCode, "Key blob corrupted");
                case android.security.KeyStore.OP_AUTH_NEEDED :
                    return new android.security.KeyStoreException(errorCode, "Operation requires authorization");
                default :
                    return new android.security.KeyStoreException(errorCode, java.lang.String.valueOf(errorCode));
            }
        } else {
            // Keymaster layer error
            switch (errorCode) {
                case android.security.keymaster.KeymasterDefs.KM_ERROR_INVALID_AUTHORIZATION_TIMEOUT :
                    // The name of this parameter significantly differs between Keymaster and
                    // framework APIs. Use the framework wording to make life easier for developers.
                    return new android.security.KeyStoreException(errorCode, "Invalid user authentication validity duration");
                default :
                    return new android.security.KeyStoreException(errorCode, android.security.keymaster.KeymasterDefs.getErrorMessage(errorCode));
            }
        }
    }

    /**
     * Returns an {@link InvalidKeyException} corresponding to the provided
     * {@link KeyStoreException}.
     */
    public java.security.InvalidKeyException getInvalidKeyException(java.lang.String keystoreKeyAlias, int uid, android.security.KeyStoreException e) {
        switch (e.getErrorCode()) {
            case android.security.KeyStore.LOCKED :
                return new android.security.keystore.UserNotAuthenticatedException();
            case android.security.keymaster.KeymasterDefs.KM_ERROR_KEY_EXPIRED :
                return new android.security.keystore.KeyExpiredException();
            case android.security.keymaster.KeymasterDefs.KM_ERROR_KEY_NOT_YET_VALID :
                return new android.security.keystore.KeyNotYetValidException();
            case android.security.keymaster.KeymasterDefs.KM_ERROR_KEY_USER_NOT_AUTHENTICATED :
            case android.security.KeyStore.OP_AUTH_NEEDED :
                {
                    // We now need to determine whether the key/operation can become usable if user
                    // authentication is performed, or whether it can never become usable again.
                    // User authentication requirements are contained in the key's characteristics. We
                    // need to check whether these requirements can be be satisfied by asking the user
                    // to authenticate.
                    android.security.keymaster.KeyCharacteristics keyCharacteristics = new android.security.keymaster.KeyCharacteristics();
                    int getKeyCharacteristicsErrorCode = getKeyCharacteristics(keystoreKeyAlias, null, null, uid, keyCharacteristics);
                    if (getKeyCharacteristicsErrorCode != android.security.KeyStore.NO_ERROR) {
                        return new java.security.InvalidKeyException("Failed to obtained key characteristics", android.security.KeyStore.getKeyStoreException(getKeyCharacteristicsErrorCode));
                    }
                    java.util.List<java.math.BigInteger> keySids = keyCharacteristics.getUnsignedLongs(android.security.keymaster.KeymasterDefs.KM_TAG_USER_SECURE_ID);
                    if (keySids.isEmpty()) {
                        // Key is not bound to any SIDs -- no amount of authentication will help here.
                        return new android.security.keystore.KeyPermanentlyInvalidatedException();
                    }
                    long rootSid = android.security.GateKeeper.getSecureUserId();
                    if ((rootSid != 0) && keySids.contains(android.security.keymaster.KeymasterArguments.toUint64(rootSid))) {
                        // One of the key's SIDs is the current root SID -- user can be authenticated
                        // against that SID.
                        return new android.security.keystore.UserNotAuthenticatedException();
                    }
                    long fingerprintOnlySid = getFingerprintOnlySid();
                    if ((fingerprintOnlySid != 0) && keySids.contains(android.security.keymaster.KeymasterArguments.toUint64(fingerprintOnlySid))) {
                        // One of the key's SIDs is the current fingerprint SID -- user can be
                        // authenticated against that SID.
                        return new android.security.keystore.UserNotAuthenticatedException();
                    }
                    // None of the key's SIDs can ever be authenticated
                    return new android.security.keystore.KeyPermanentlyInvalidatedException();
                }
            default :
                return new java.security.InvalidKeyException("Keystore operation failed", e);
        }
    }

    private long getFingerprintOnlySid() {
        android.hardware.fingerprint.FingerprintManager fingerprintManager = mContext.getSystemService(android.hardware.fingerprint.FingerprintManager.class);
        if (fingerprintManager == null) {
            return 0;
        }
        // TODO: Restore USE_FINGERPRINT permission check in
        // FingerprintManager.getAuthenticatorId once the ID is no longer needed here.
        return fingerprintManager.getAuthenticatorId();
    }

    /**
     * Returns an {@link InvalidKeyException} corresponding to the provided keystore/keymaster error
     * code.
     */
    public java.security.InvalidKeyException getInvalidKeyException(java.lang.String keystoreKeyAlias, int uid, int errorCode) {
        return getInvalidKeyException(keystoreKeyAlias, uid, android.security.KeyStore.getKeyStoreException(errorCode));
    }
}

