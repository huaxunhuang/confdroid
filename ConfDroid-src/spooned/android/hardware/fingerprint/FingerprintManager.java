/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.hardware.fingerprint;


/**
 * A class that coordinates access to the fingerprint hardware.
 * <p>
 * Use {@link android.content.Context#getSystemService(java.lang.String)}
 * with argument {@link android.content.Context#FINGERPRINT_SERVICE} to get
 * an instance of this class.
 */
public class FingerprintManager {
    private static final java.lang.String TAG = "FingerprintManager";

    private static final boolean DEBUG = true;

    private static final int MSG_ENROLL_RESULT = 100;

    private static final int MSG_ACQUIRED = 101;

    private static final int MSG_AUTHENTICATION_SUCCEEDED = 102;

    private static final int MSG_AUTHENTICATION_FAILED = 103;

    private static final int MSG_ERROR = 104;

    private static final int MSG_REMOVED = 105;

    // 
    // Error messages from fingerprint hardware during initilization, enrollment, authentication or
    // removal. Must agree with the list in fingerprint.h
    // 
    /**
     * The hardware is unavailable. Try again later.
     */
    public static final int FINGERPRINT_ERROR_HW_UNAVAILABLE = 1;

    /**
     * Error state returned when the sensor was unable to process the current image.
     */
    public static final int FINGERPRINT_ERROR_UNABLE_TO_PROCESS = 2;

    /**
     * Error state returned when the current request has been running too long. This is intended to
     * prevent programs from waiting for the fingerprint sensor indefinitely. The timeout is
     * platform and sensor-specific, but is generally on the order of 30 seconds.
     */
    public static final int FINGERPRINT_ERROR_TIMEOUT = 3;

    /**
     * Error state returned for operations like enrollment; the operation cannot be completed
     * because there's not enough storage remaining to complete the operation.
     */
    public static final int FINGERPRINT_ERROR_NO_SPACE = 4;

    /**
     * The operation was canceled because the fingerprint sensor is unavailable. For example,
     * this may happen when the user is switched, the device is locked or another pending operation
     * prevents or disables it.
     */
    public static final int FINGERPRINT_ERROR_CANCELED = 5;

    /**
     * The {@link FingerprintManager#remove(Fingerprint, RemovalCallback)} call failed. Typically
     * this will happen when the provided fingerprint id was incorrect.
     *
     * @unknown 
     */
    public static final int FINGERPRINT_ERROR_UNABLE_TO_REMOVE = 6;

    /**
     * The operation was canceled because the API is locked out due to too many attempts.
     */
    public static final int FINGERPRINT_ERROR_LOCKOUT = 7;

    /**
     * Hardware vendors may extend this list if there are conditions that do not fall under one of
     * the above categories. Vendors are responsible for providing error strings for these errors.
     *
     * @unknown 
     */
    public static final int FINGERPRINT_ERROR_VENDOR_BASE = 1000;

    // 
    // Image acquisition messages. Must agree with those in fingerprint.h
    // 
    /**
     * The image acquired was good.
     */
    public static final int FINGERPRINT_ACQUIRED_GOOD = 0;

    /**
     * Only a partial fingerprint image was detected. During enrollment, the user should be
     * informed on what needs to happen to resolve this problem, e.g. "press firmly on sensor."
     */
    public static final int FINGERPRINT_ACQUIRED_PARTIAL = 1;

    /**
     * The fingerprint image was too noisy to process due to a detected condition (i.e. dry skin) or
     * a possibly dirty sensor (See {@link #FINGERPRINT_ACQUIRED_IMAGER_DIRTY}).
     */
    public static final int FINGERPRINT_ACQUIRED_INSUFFICIENT = 2;

    /**
     * The fingerprint image was too noisy due to suspected or detected dirt on the sensor.
     * For example, it's reasonable return this after multiple
     * {@link #FINGERPRINT_ACQUIRED_INSUFFICIENT} or actual detection of dirt on the sensor
     * (stuck pixels, swaths, etc.). The user is expected to take action to clean the sensor
     * when this is returned.
     */
    public static final int FINGERPRINT_ACQUIRED_IMAGER_DIRTY = 3;

    /**
     * The fingerprint image was unreadable due to lack of motion. This is most appropriate for
     * linear array sensors that require a swipe motion.
     */
    public static final int FINGERPRINT_ACQUIRED_TOO_SLOW = 4;

    /**
     * The fingerprint image was incomplete due to quick motion. While mostly appropriate for
     * linear array sensors,  this could also happen if the finger was moved during acquisition.
     * The user should be asked to move the finger slower (linear) or leave the finger on the sensor
     * longer.
     */
    public static final int FINGERPRINT_ACQUIRED_TOO_FAST = 5;

    /**
     * Hardware vendors may extend this list if there are conditions that do not fall under one of
     * the above categories. Vendors are responsible for providing error strings for these errors.
     *
     * @unknown 
     */
    public static final int FINGERPRINT_ACQUIRED_VENDOR_BASE = 1000;

    private android.hardware.fingerprint.IFingerprintService mService;

    private android.content.Context mContext;

    private android.os.IBinder mToken = new android.os.Binder();

    private android.hardware.fingerprint.FingerprintManager.AuthenticationCallback mAuthenticationCallback;

    private android.hardware.fingerprint.FingerprintManager.EnrollmentCallback mEnrollmentCallback;

    private android.hardware.fingerprint.FingerprintManager.RemovalCallback mRemovalCallback;

    private android.hardware.fingerprint.FingerprintManager.CryptoObject mCryptoObject;

    private android.hardware.fingerprint.Fingerprint mRemovalFingerprint;

    private android.os.Handler mHandler;

    private class OnEnrollCancelListener implements android.os.CancellationSignal.OnCancelListener {
        @java.lang.Override
        public void onCancel() {
            cancelEnrollment();
        }
    }

    private class OnAuthenticationCancelListener implements android.os.CancellationSignal.OnCancelListener {
        private android.hardware.fingerprint.FingerprintManager.CryptoObject mCrypto;

        public OnAuthenticationCancelListener(android.hardware.fingerprint.FingerprintManager.CryptoObject crypto) {
            mCrypto = crypto;
        }

        @java.lang.Override
        public void onCancel() {
            cancelAuthentication(mCrypto);
        }
    }

    /**
     * A wrapper class for the crypto objects supported by FingerprintManager. Currently the
     * framework supports {@link Signature}, {@link Cipher} and {@link Mac} objects.
     */
    public static final class CryptoObject {
        public CryptoObject(@android.annotation.NonNull
        java.security.Signature signature) {
            mCrypto = signature;
        }

        public CryptoObject(@android.annotation.NonNull
        javax.crypto.Cipher cipher) {
            mCrypto = cipher;
        }

        public CryptoObject(@android.annotation.NonNull
        javax.crypto.Mac mac) {
            mCrypto = mac;
        }

        /**
         * Get {@link Signature} object.
         *
         * @return {@link Signature} object or null if this doesn't contain one.
         */
        public java.security.Signature getSignature() {
            return mCrypto instanceof java.security.Signature ? ((java.security.Signature) (mCrypto)) : null;
        }

        /**
         * Get {@link Cipher} object.
         *
         * @return {@link Cipher} object or null if this doesn't contain one.
         */
        public javax.crypto.Cipher getCipher() {
            return mCrypto instanceof javax.crypto.Cipher ? ((javax.crypto.Cipher) (mCrypto)) : null;
        }

        /**
         * Get {@link Mac} object.
         *
         * @return {@link Mac} object or null if this doesn't contain one.
         */
        public javax.crypto.Mac getMac() {
            return mCrypto instanceof javax.crypto.Mac ? ((javax.crypto.Mac) (mCrypto)) : null;
        }

        /**
         *
         *
         * @unknown 
         * @return the opId associated with this object or 0 if none
         */
        public long getOpId() {
            return mCrypto != null ? android.security.keystore.AndroidKeyStoreProvider.getKeyStoreOperationHandle(mCrypto) : 0;
        }

        private final java.lang.Object mCrypto;
    }

    /**
     * Container for callback data from {@link FingerprintManager#authenticate(CryptoObject,
     *     CancellationSignal, int, AuthenticationCallback, Handler)}.
     */
    public static class AuthenticationResult {
        private android.hardware.fingerprint.Fingerprint mFingerprint;

        private android.hardware.fingerprint.FingerprintManager.CryptoObject mCryptoObject;

        private int mUserId;

        /**
         * Authentication result
         *
         * @param crypto
         * 		the crypto object
         * @param fingerprint
         * 		the recognized fingerprint data, if allowed.
         * @unknown 
         */
        public AuthenticationResult(android.hardware.fingerprint.FingerprintManager.CryptoObject crypto, android.hardware.fingerprint.Fingerprint fingerprint, int userId) {
            mCryptoObject = crypto;
            mFingerprint = fingerprint;
            mUserId = userId;
        }

        /**
         * Obtain the crypto object associated with this transaction
         *
         * @return crypto object provided to {@link FingerprintManager#authenticate(CryptoObject,
        CancellationSignal, int, AuthenticationCallback, Handler)}.
         */
        public android.hardware.fingerprint.FingerprintManager.CryptoObject getCryptoObject() {
            return mCryptoObject;
        }

        /**
         * Obtain the Fingerprint associated with this operation. Applications are strongly
         * discouraged from associating specific fingers with specific applications or operations.
         *
         * @unknown 
         */
        public android.hardware.fingerprint.Fingerprint getFingerprint() {
            return mFingerprint;
        }

        /**
         * Obtain the userId for which this fingerprint was authenticated.
         *
         * @unknown 
         */
        public int getUserId() {
            return mUserId;
        }
    }

    /**
     * Callback structure provided to {@link FingerprintManager#authenticate(CryptoObject,
     * CancellationSignal, int, AuthenticationCallback, Handler)}. Users of {@link FingerprintManager#authenticate(CryptoObject, CancellationSignal,
     * int, AuthenticationCallback, Handler)} must provide an implementation of this for listening to
     * fingerprint events.
     */
    public static abstract class AuthenticationCallback {
        /**
         * Called when an unrecoverable error has been encountered and the operation is complete.
         * No further callbacks will be made on this object.
         *
         * @param errorCode
         * 		An integer identifying the error message
         * @param errString
         * 		A human-readable error string that can be shown in UI
         */
        public void onAuthenticationError(int errorCode, java.lang.CharSequence errString) {
        }

        /**
         * Called when a recoverable error has been encountered during authentication. The help
         * string is provided to give the user guidance for what went wrong, such as
         * "Sensor dirty, please clean it."
         *
         * @param helpCode
         * 		An integer identifying the error message
         * @param helpString
         * 		A human-readable string that can be shown in UI
         */
        public void onAuthenticationHelp(int helpCode, java.lang.CharSequence helpString) {
        }

        /**
         * Called when a fingerprint is recognized.
         *
         * @param result
         * 		An object containing authentication-related data
         */
        public void onAuthenticationSucceeded(android.hardware.fingerprint.FingerprintManager.AuthenticationResult result) {
        }

        /**
         * Called when a fingerprint is valid but not recognized.
         */
        public void onAuthenticationFailed() {
        }

        /**
         * Called when a fingerprint image has been acquired, but wasn't processed yet.
         *
         * @param acquireInfo
         * 		one of FINGERPRINT_ACQUIRED_* constants
         * @unknown 
         */
        public void onAuthenticationAcquired(int acquireInfo) {
        }
    }

    /**
     * Callback structure provided to {@link FingerprintManager#enroll(long, EnrollmentCallback,
     * CancellationSignal, int). Users of {@link #FingerprintManager()}
     * must provide an implementation of this to {@link FingerprintManager#enroll(long,
     * CancellationSignal, int, EnrollmentCallback) for listening to fingerprint events.
     *
     * @unknown 
     */
    public static abstract class EnrollmentCallback {
        /**
         * Called when an unrecoverable error has been encountered and the operation is complete.
         * No further callbacks will be made on this object.
         *
         * @param errMsgId
         * 		An integer identifying the error message
         * @param errString
         * 		A human-readable error string that can be shown in UI
         */
        public void onEnrollmentError(int errMsgId, java.lang.CharSequence errString) {
        }

        /**
         * Called when a recoverable error has been encountered during enrollment. The help
         * string is provided to give the user guidance for what went wrong, such as
         * "Sensor dirty, please clean it" or what they need to do next, such as
         * "Touch sensor again."
         *
         * @param helpMsgId
         * 		An integer identifying the error message
         * @param helpString
         * 		A human-readable string that can be shown in UI
         */
        public void onEnrollmentHelp(int helpMsgId, java.lang.CharSequence helpString) {
        }

        /**
         * Called as each enrollment step progresses. Enrollment is considered complete when
         * remaining reaches 0. This function will not be called if enrollment fails. See
         * {@link EnrollmentCallback#onEnrollmentError(int, CharSequence)}
         *
         * @param remaining
         * 		The number of remaining steps
         */
        public void onEnrollmentProgress(int remaining) {
        }
    }

    /**
     * Callback structure provided to {@link FingerprintManager#remove(int). Users of
     * {@link #FingerprintManager()} may optionally provide an implementation of this to
     * {@link FingerprintManager#remove(int, int, RemovalCallback)} for listening to
     * fingerprint template removal events.
     *
     * @unknown 
     */
    public static abstract class RemovalCallback {
        /**
         * Called when the given fingerprint can't be removed.
         *
         * @param fp
         * 		The fingerprint that the call attempted to remove
         * @param errMsgId
         * 		An associated error message id
         * @param errString
         * 		An error message indicating why the fingerprint id can't be removed
         */
        public void onRemovalError(android.hardware.fingerprint.Fingerprint fp, int errMsgId, java.lang.CharSequence errString) {
        }

        /**
         * Called when a given fingerprint is successfully removed.
         *
         * @param fingerprint
         * 		the fingerprint template that was removed.
         */
        public void onRemovalSucceeded(android.hardware.fingerprint.Fingerprint fingerprint) {
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static abstract class LockoutResetCallback {
        /**
         * Called when lockout period expired and clients are allowed to listen for fingerprint
         * again.
         */
        public void onLockoutReset() {
        }
    }

    /**
     * Request authentication of a crypto object. This call warms up the fingerprint hardware
     * and starts scanning for a fingerprint. It terminates when
     * {@link AuthenticationCallback#onAuthenticationError(int, CharSequence)} or
     * {@link AuthenticationCallback#onAuthenticationSucceeded(AuthenticationResult)} is called, at
     * which point the object is no longer valid. The operation can be canceled by using the
     * provided cancel object.
     *
     * @param crypto
     * 		object associated with the call or null if none required.
     * @param cancel
     * 		an object that can be used to cancel authentication
     * @param flags
     * 		optional flags; should be 0
     * @param callback
     * 		an object to receive authentication events
     * @param handler
     * 		an optional handler to handle callback events
     * @throws IllegalArgumentException
     * 		if the crypto operation is not supported or is not backed
     * 		by <a href="{@docRoot }training/articles/keystore.html">Android Keystore
     * 		facility</a>.
     * @throws IllegalStateException
     * 		if the crypto primitive is not initialized.
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.USE_FINGERPRINT)
    public void authenticate(@android.annotation.Nullable
    android.hardware.fingerprint.FingerprintManager.CryptoObject crypto, @android.annotation.Nullable
    android.os.CancellationSignal cancel, int flags, @android.annotation.NonNull
    android.hardware.fingerprint.FingerprintManager.AuthenticationCallback callback, @android.annotation.Nullable
    android.os.Handler handler) {
        authenticate(crypto, cancel, flags, callback, handler, android.os.UserHandle.myUserId());
    }

    /**
     * Use the provided handler thread for events.
     *
     * @param handler
     * 		
     */
    private void useHandler(android.os.Handler handler) {
        if (handler != null) {
            mHandler = new android.hardware.fingerprint.FingerprintManager.MyHandler(handler.getLooper());
        } else
            if (mHandler.getLooper() != mContext.getMainLooper()) {
                mHandler = new android.hardware.fingerprint.FingerprintManager.MyHandler(mContext.getMainLooper());
            }

    }

    /**
     * Per-user version
     *
     * @unknown 
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.USE_FINGERPRINT)
    public void authenticate(@android.annotation.Nullable
    android.hardware.fingerprint.FingerprintManager.CryptoObject crypto, @android.annotation.Nullable
    android.os.CancellationSignal cancel, int flags, @android.annotation.NonNull
    android.hardware.fingerprint.FingerprintManager.AuthenticationCallback callback, android.os.Handler handler, int userId) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("Must supply an authentication callback");
        }
        if (cancel != null) {
            if (cancel.isCanceled()) {
                android.util.Log.w(android.hardware.fingerprint.FingerprintManager.TAG, "authentication already canceled");
                return;
            } else {
                cancel.setOnCancelListener(new android.hardware.fingerprint.FingerprintManager.OnAuthenticationCancelListener(crypto));
            }
        }
        if (mService != null)
            try {
                useHandler(handler);
                mAuthenticationCallback = callback;
                mCryptoObject = crypto;
                long sessionId = (crypto != null) ? crypto.getOpId() : 0;
                mService.authenticate(mToken, sessionId, userId, mServiceReceiver, flags, mContext.getOpPackageName());
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.hardware.fingerprint.FingerprintManager.TAG, "Remote exception while authenticating: ", e);
                if (callback != null) {
                    // Though this may not be a hardware issue, it will cause apps to give up or try
                    // again later.
                    callback.onAuthenticationError(android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE, getErrorString(android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE));
                }
            }

    }

    /**
     * Request fingerprint enrollment. This call warms up the fingerprint hardware
     * and starts scanning for fingerprints. Progress will be indicated by callbacks to the
     * {@link EnrollmentCallback} object. It terminates when
     * {@link EnrollmentCallback#onEnrollmentError(int, CharSequence)} or
     * {@link EnrollmentCallback#onEnrollmentProgress(int) is called with remaining == 0, at
     * which point the object is no longer valid. The operation can be canceled by using the
     * provided cancel object.
     *
     * @param token
     * 		a unique token provided by a recent creation or verification of device
     * 		credentials (e.g. pin, pattern or password).
     * @param cancel
     * 		an object that can be used to cancel enrollment
     * @param flags
     * 		optional flags
     * @param userId
     * 		the user to whom this fingerprint will belong to
     * @param callback
     * 		an object to receive enrollment events
     * @unknown 
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.MANAGE_FINGERPRINT)
    public void enroll(byte[] token, android.os.CancellationSignal cancel, int flags, int userId, android.hardware.fingerprint.FingerprintManager.EnrollmentCallback callback) {
        if (userId == android.os.UserHandle.USER_CURRENT) {
            userId = getCurrentUserId();
        }
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("Must supply an enrollment callback");
        }
        if (cancel != null) {
            if (cancel.isCanceled()) {
                android.util.Log.w(android.hardware.fingerprint.FingerprintManager.TAG, "enrollment already canceled");
                return;
            } else {
                cancel.setOnCancelListener(new android.hardware.fingerprint.FingerprintManager.OnEnrollCancelListener());
            }
        }
        if (mService != null)
            try {
                mEnrollmentCallback = callback;
                mService.enroll(mToken, token, userId, mServiceReceiver, flags, mContext.getOpPackageName());
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.hardware.fingerprint.FingerprintManager.TAG, "Remote exception in enroll: ", e);
                if (callback != null) {
                    // Though this may not be a hardware issue, it will cause apps to give up or try
                    // again later.
                    callback.onEnrollmentError(android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE, getErrorString(android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE));
                }
            }

    }

    /**
     * Requests a pre-enrollment auth token to tie enrollment to the confirmation of
     * existing device credentials (e.g. pin/pattern/password).
     *
     * @unknown 
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.MANAGE_FINGERPRINT)
    public long preEnroll() {
        long result = 0;
        if (mService != null)
            try {
                result = mService.preEnroll(mToken);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }

        return result;
    }

    /**
     * Finishes enrollment and cancels the current auth token.
     *
     * @unknown 
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.MANAGE_FINGERPRINT)
    public int postEnroll() {
        int result = 0;
        if (mService != null)
            try {
                result = mService.postEnroll(mToken);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }

        return result;
    }

    /**
     * Sets the active user. This is meant to be used to select the current profile for enrollment
     * to allow separate enrolled fingers for a work profile
     *
     * @param userId
     * 		
     * @unknown 
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.MANAGE_FINGERPRINT)
    public void setActiveUser(int userId) {
        if (mService != null)
            try {
                mService.setActiveUser(userId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }

    }

    /**
     * Remove given fingerprint template from fingerprint hardware and/or protected storage.
     *
     * @param fp
     * 		the fingerprint item to remove
     * @param userId
     * 		the user who this fingerprint belongs to
     * @param callback
     * 		an optional callback to verify that fingerprint templates have been
     * 		successfully removed. May be null of no callback is required.
     * @unknown 
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.MANAGE_FINGERPRINT)
    public void remove(android.hardware.fingerprint.Fingerprint fp, int userId, android.hardware.fingerprint.FingerprintManager.RemovalCallback callback) {
        if (mService != null)
            try {
                mRemovalCallback = callback;
                mRemovalFingerprint = fp;
                mService.remove(mToken, fp.getFingerId(), fp.getGroupId(), userId, mServiceReceiver);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.hardware.fingerprint.FingerprintManager.TAG, "Remote exception in remove: ", e);
                if (callback != null) {
                    callback.onRemovalError(fp, android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE, getErrorString(android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE));
                }
            }

    }

    /**
     * Renames the given fingerprint template
     *
     * @param fpId
     * 		the fingerprint id
     * @param userId
     * 		the user who this fingerprint belongs to
     * @param newName
     * 		the new name
     * @unknown 
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.MANAGE_FINGERPRINT)
    public void rename(int fpId, int userId, java.lang.String newName) {
        // Renames the given fpId
        if (mService != null) {
            try {
                mService.rename(fpId, userId, newName);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            android.util.Log.w(android.hardware.fingerprint.FingerprintManager.TAG, "rename(): Service not connected!");
        }
    }

    /**
     * Obtain the list of enrolled fingerprints templates.
     *
     * @return list of current fingerprint items
     * @unknown 
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.USE_FINGERPRINT)
    public java.util.List<android.hardware.fingerprint.Fingerprint> getEnrolledFingerprints(int userId) {
        if (mService != null)
            try {
                return mService.getEnrolledFingerprints(userId, mContext.getOpPackageName());
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }

        return null;
    }

    /**
     * Obtain the list of enrolled fingerprints templates.
     *
     * @return list of current fingerprint items
     * @unknown 
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.USE_FINGERPRINT)
    public java.util.List<android.hardware.fingerprint.Fingerprint> getEnrolledFingerprints() {
        return getEnrolledFingerprints(android.os.UserHandle.myUserId());
    }

    /**
     * Determine if there is at least one fingerprint enrolled.
     *
     * @return true if at least one fingerprint is enrolled, false otherwise
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.USE_FINGERPRINT)
    public boolean hasEnrolledFingerprints() {
        if (mService != null)
            try {
                return mService.hasEnrolledFingerprints(android.os.UserHandle.myUserId(), mContext.getOpPackageName());
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }

        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.RequiresPermission(allOf = { android.Manifest.permission.USE_FINGERPRINT, android.Manifest.permission.INTERACT_ACROSS_USERS })
    public boolean hasEnrolledFingerprints(int userId) {
        if (mService != null)
            try {
                return mService.hasEnrolledFingerprints(userId, mContext.getOpPackageName());
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }

        return false;
    }

    /**
     * Determine if fingerprint hardware is present and functional.
     *
     * @return true if hardware is present and functional, false otherwise.
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.USE_FINGERPRINT)
    public boolean isHardwareDetected() {
        if (mService != null) {
            try {
                long deviceId = 0;/* TODO: plumb hardware id to FPMS */

                return mService.isHardwareDetected(deviceId, mContext.getOpPackageName());
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            android.util.Log.w(android.hardware.fingerprint.FingerprintManager.TAG, "isFingerprintHardwareDetected(): Service not connected!");
        }
        return false;
    }

    /**
     * Retrieves the authenticator token for binding keys to the lifecycle
     * of the current set of fingerprints. Used only by internal clients.
     *
     * @unknown 
     */
    public long getAuthenticatorId() {
        if (mService != null) {
            try {
                return mService.getAuthenticatorId(mContext.getOpPackageName());
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            android.util.Log.w(android.hardware.fingerprint.FingerprintManager.TAG, "getAuthenticatorId(): Service not connected!");
        }
        return 0;
    }

    /**
     * Reset the lockout timer when asked to do so by keyguard.
     *
     * @param token
     * 		an opaque token returned by password confirmation.
     * @unknown 
     */
    public void resetTimeout(byte[] token) {
        if (mService != null) {
            try {
                mService.resetTimeout(token);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            android.util.Log.w(android.hardware.fingerprint.FingerprintManager.TAG, "resetTimeout(): Service not connected!");
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void addLockoutResetCallback(final android.hardware.fingerprint.FingerprintManager.LockoutResetCallback callback) {
        if (mService != null) {
            try {
                final android.os.PowerManager powerManager = mContext.getSystemService(android.os.PowerManager.class);
                mService.addLockoutResetCallback(new android.hardware.fingerprint.IFingerprintServiceLockoutResetCallback.Stub() {
                    @java.lang.Override
                    public void onLockoutReset(long deviceId) throws android.os.RemoteException {
                        final android.os.PowerManager.WakeLock wakeLock = powerManager.newWakeLock(android.os.PowerManager.PARTIAL_WAKE_LOCK, "lockoutResetCallback");
                        wakeLock.acquire();
                        mHandler.post(new java.lang.Runnable() {
                            @java.lang.Override
                            public void run() {
                                try {
                                    callback.onLockoutReset();
                                } finally {
                                    wakeLock.release();
                                }
                            }
                        });
                    }
                });
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            android.util.Log.w(android.hardware.fingerprint.FingerprintManager.TAG, "addLockoutResetCallback(): Service not connected!");
        }
    }

    private class MyHandler extends android.os.Handler {
        private MyHandler(android.content.Context context) {
            super(context.getMainLooper());
        }

        private MyHandler(android.os.Looper looper) {
            super(looper);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.hardware.fingerprint.FingerprintManager.MSG_ENROLL_RESULT :
                    /* remaining */
                    sendEnrollResult(((android.hardware.fingerprint.Fingerprint) (msg.obj)), msg.arg1);
                    break;
                case android.hardware.fingerprint.FingerprintManager.MSG_ACQUIRED :
                    /* deviceId */
                    /* acquire info */
                    sendAcquiredResult(((java.lang.Long) (msg.obj)), msg.arg1);
                    break;
                case android.hardware.fingerprint.FingerprintManager.MSG_AUTHENTICATION_SUCCEEDED :
                    /* userId */
                    sendAuthenticatedSucceeded(((android.hardware.fingerprint.Fingerprint) (msg.obj)), msg.arg1);
                    break;
                case android.hardware.fingerprint.FingerprintManager.MSG_AUTHENTICATION_FAILED :
                    sendAuthenticatedFailed();
                    break;
                case android.hardware.fingerprint.FingerprintManager.MSG_ERROR :
                    /* deviceId */
                    /* errMsgId */
                    sendErrorResult(((java.lang.Long) (msg.obj)), msg.arg1);
                    break;
                case android.hardware.fingerprint.FingerprintManager.MSG_REMOVED :
                    /* deviceId */
                    /* fingerId */
                    /* groupId */
                    sendRemovedResult(((java.lang.Long) (msg.obj)), msg.arg1, msg.arg2);
            }
        }

        private void sendRemovedResult(long deviceId, int fingerId, int groupId) {
            if (mRemovalCallback != null) {
                int reqFingerId = mRemovalFingerprint.getFingerId();
                int reqGroupId = mRemovalFingerprint.getGroupId();
                if (((reqFingerId != 0) && (fingerId != 0)) && (fingerId != reqFingerId)) {
                    android.util.Log.w(android.hardware.fingerprint.FingerprintManager.TAG, (("Finger id didn't match: " + fingerId) + " != ") + reqFingerId);
                    return;
                }
                if (groupId != reqGroupId) {
                    android.util.Log.w(android.hardware.fingerprint.FingerprintManager.TAG, (("Group id didn't match: " + groupId) + " != ") + reqGroupId);
                    return;
                }
                mRemovalCallback.onRemovalSucceeded(new android.hardware.fingerprint.Fingerprint(null, groupId, fingerId, deviceId));
            }
        }

        private void sendErrorResult(long deviceId, int errMsgId) {
            if (mEnrollmentCallback != null) {
                mEnrollmentCallback.onEnrollmentError(errMsgId, getErrorString(errMsgId));
            } else
                if (mAuthenticationCallback != null) {
                    mAuthenticationCallback.onAuthenticationError(errMsgId, getErrorString(errMsgId));
                } else
                    if (mRemovalCallback != null) {
                        mRemovalCallback.onRemovalError(mRemovalFingerprint, errMsgId, getErrorString(errMsgId));
                    }


        }

        private void sendEnrollResult(android.hardware.fingerprint.Fingerprint fp, int remaining) {
            if (mEnrollmentCallback != null) {
                mEnrollmentCallback.onEnrollmentProgress(remaining);
            }
        }

        private void sendAuthenticatedSucceeded(android.hardware.fingerprint.Fingerprint fp, int userId) {
            if (mAuthenticationCallback != null) {
                final android.hardware.fingerprint.FingerprintManager.AuthenticationResult result = new android.hardware.fingerprint.FingerprintManager.AuthenticationResult(mCryptoObject, fp, userId);
                mAuthenticationCallback.onAuthenticationSucceeded(result);
            }
        }

        private void sendAuthenticatedFailed() {
            if (mAuthenticationCallback != null) {
                mAuthenticationCallback.onAuthenticationFailed();
            }
        }

        private void sendAcquiredResult(long deviceId, int acquireInfo) {
            if (mAuthenticationCallback != null) {
                mAuthenticationCallback.onAuthenticationAcquired(acquireInfo);
            }
            final java.lang.String msg = getAcquiredString(acquireInfo);
            if (msg == null) {
                return;
            }
            if (mEnrollmentCallback != null) {
                mEnrollmentCallback.onEnrollmentHelp(acquireInfo, msg);
            } else
                if (mAuthenticationCallback != null) {
                    mAuthenticationCallback.onAuthenticationHelp(acquireInfo, msg);
                }

        }
    }

    /**
     *
     *
     * @unknown 
     */
    public FingerprintManager(android.content.Context context, android.hardware.fingerprint.IFingerprintService service) {
        mContext = context;
        mService = service;
        if (mService == null) {
            android.util.Slog.v(android.hardware.fingerprint.FingerprintManager.TAG, "FingerprintManagerService was null");
        }
        mHandler = new android.hardware.fingerprint.FingerprintManager.MyHandler(context);
    }

    private int getCurrentUserId() {
        try {
            return android.app.ActivityManagerNative.getDefault().getCurrentUser().id;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void cancelEnrollment() {
        if (mService != null)
            try {
                mService.cancelEnrollment(mToken);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }

    }

    private void cancelAuthentication(android.hardware.fingerprint.FingerprintManager.CryptoObject cryptoObject) {
        if (mService != null)
            try {
                mService.cancelAuthentication(mToken, mContext.getOpPackageName());
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }

    }

    private java.lang.String getErrorString(int errMsg) {
        switch (errMsg) {
            case android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS :
                return mContext.getString(com.android.internal.R.string.fingerprint_error_unable_to_process);
            case android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE :
                return mContext.getString(com.android.internal.R.string.fingerprint_error_hw_not_available);
            case android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_NO_SPACE :
                return mContext.getString(com.android.internal.R.string.fingerprint_error_no_space);
            case android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_TIMEOUT :
                return mContext.getString(com.android.internal.R.string.fingerprint_error_timeout);
            case android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_CANCELED :
                return mContext.getString(com.android.internal.R.string.fingerprint_error_canceled);
            case android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_LOCKOUT :
                return mContext.getString(com.android.internal.R.string.fingerprint_error_lockout);
            default :
                if (errMsg >= android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_VENDOR_BASE) {
                    int msgNumber = errMsg - android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_VENDOR_BASE;
                    java.lang.String[] msgArray = mContext.getResources().getStringArray(com.android.internal.R.array.fingerprint_error_vendor);
                    if (msgNumber < msgArray.length) {
                        return msgArray[msgNumber];
                    }
                }
                return null;
        }
    }

    private java.lang.String getAcquiredString(int acquireInfo) {
        switch (acquireInfo) {
            case android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ACQUIRED_GOOD :
                return null;
            case android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL :
                return mContext.getString(com.android.internal.R.string.fingerprint_acquired_partial);
            case android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT :
                return mContext.getString(com.android.internal.R.string.fingerprint_acquired_insufficient);
            case android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY :
                return mContext.getString(com.android.internal.R.string.fingerprint_acquired_imager_dirty);
            case android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW :
                return mContext.getString(com.android.internal.R.string.fingerprint_acquired_too_slow);
            case android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST :
                return mContext.getString(com.android.internal.R.string.fingerprint_acquired_too_fast);
            default :
                if (acquireInfo >= android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ACQUIRED_VENDOR_BASE) {
                    int msgNumber = acquireInfo - android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ACQUIRED_VENDOR_BASE;
                    java.lang.String[] msgArray = mContext.getResources().getStringArray(com.android.internal.R.array.fingerprint_acquired_vendor);
                    if (msgNumber < msgArray.length) {
                        return msgArray[msgNumber];
                    }
                }
                return null;
        }
    }

    private android.hardware.fingerprint.IFingerprintServiceReceiver mServiceReceiver = new android.hardware.fingerprint.IFingerprintServiceReceiver.Stub() {
        // binder call
        @java.lang.Override
        public void onEnrollResult(long deviceId, int fingerId, int groupId, int remaining) {
            mHandler.obtainMessage(android.hardware.fingerprint.FingerprintManager.MSG_ENROLL_RESULT, remaining, 0, new android.hardware.fingerprint.Fingerprint(null, groupId, fingerId, deviceId)).sendToTarget();
        }

        // binder call
        @java.lang.Override
        public void onAcquired(long deviceId, int acquireInfo) {
            mHandler.obtainMessage(android.hardware.fingerprint.FingerprintManager.MSG_ACQUIRED, acquireInfo, 0, deviceId).sendToTarget();
        }

        // binder call
        @java.lang.Override
        public void onAuthenticationSucceeded(long deviceId, android.hardware.fingerprint.Fingerprint fp, int userId) {
            mHandler.obtainMessage(android.hardware.fingerprint.FingerprintManager.MSG_AUTHENTICATION_SUCCEEDED, userId, 0, fp).sendToTarget();
        }

        // binder call
        @java.lang.Override
        public void onAuthenticationFailed(long deviceId) {
            mHandler.obtainMessage(android.hardware.fingerprint.FingerprintManager.MSG_AUTHENTICATION_FAILED).sendToTarget();
        }

        // binder call
        @java.lang.Override
        public void onError(long deviceId, int error) {
            mHandler.obtainMessage(android.hardware.fingerprint.FingerprintManager.MSG_ERROR, error, 0, deviceId).sendToTarget();
        }

        // binder call
        @java.lang.Override
        public void onRemoved(long deviceId, int fingerId, int groupId) {
            mHandler.obtainMessage(android.hardware.fingerprint.FingerprintManager.MSG_REMOVED, fingerId, groupId, deviceId).sendToTarget();
        }
    };
}

