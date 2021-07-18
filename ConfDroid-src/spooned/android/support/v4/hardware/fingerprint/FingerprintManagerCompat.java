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
 * limitations under the License
 */
package android.support.v4.hardware.fingerprint;


/**
 * A class that coordinates access to the fingerprint hardware.
 * <p>
 * On platforms before {@link android.os.Build.VERSION_CODES#M}, this class behaves as there would
 * be no fingerprint hardware available.
 */
public final class FingerprintManagerCompat {
    private android.content.Context mContext;

    /**
     * Get a {@link FingerprintManagerCompat} instance for a provided context.
     */
    public static android.support.v4.hardware.fingerprint.FingerprintManagerCompat from(android.content.Context context) {
        return new android.support.v4.hardware.fingerprint.FingerprintManagerCompat(context);
    }

    private FingerprintManagerCompat(android.content.Context context) {
        mContext = context;
    }

    static final android.support.v4.hardware.fingerprint.FingerprintManagerCompat.FingerprintManagerCompatImpl IMPL;

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 23) {
            IMPL = new android.support.v4.hardware.fingerprint.FingerprintManagerCompat.Api23FingerprintManagerCompatImpl();
        } else {
            IMPL = new android.support.v4.hardware.fingerprint.FingerprintManagerCompat.LegacyFingerprintManagerCompatImpl();
        }
    }

    /**
     * Determine if there is at least one fingerprint enrolled.
     *
     * @return true if at least one fingerprint is enrolled, false otherwise
     */
    public boolean hasEnrolledFingerprints() {
        return android.support.v4.hardware.fingerprint.FingerprintManagerCompat.IMPL.hasEnrolledFingerprints(mContext);
    }

    /**
     * Determine if fingerprint hardware is present and functional.
     *
     * @return true if hardware is present and functional, false otherwise.
     */
    public boolean isHardwareDetected() {
        return android.support.v4.hardware.fingerprint.FingerprintManagerCompat.IMPL.isHardwareDetected(mContext);
    }

    /**
     * Request authentication of a crypto object. This call warms up the fingerprint hardware
     * and starts scanning for a fingerprint. It terminates when
     * {@link AuthenticationCallback#onAuthenticationError(int, CharSequence)} or
     * {@link AuthenticationCallback#onAuthenticationSucceeded(AuthenticationResult) is called, at
     * which point the object is no longer valid. The operation can be canceled by using the
     * provided cancel object.
     *
     * @param crypto
     * 		object associated with the call or null if none required.
     * @param flags
     * 		optional flags; should be 0
     * @param cancel
     * 		an object that can be used to cancel authentication
     * @param callback
     * 		an object to receive authentication events
     * @param handler
     * 		an optional handler for events
     */
    public void authenticate(@android.support.annotation.Nullable
    android.support.v4.hardware.fingerprint.FingerprintManagerCompat.CryptoObject crypto, int flags, @android.support.annotation.Nullable
    android.support.v4.os.CancellationSignal cancel, @android.support.annotation.NonNull
    android.support.v4.hardware.fingerprint.FingerprintManagerCompat.AuthenticationCallback callback, @android.support.annotation.Nullable
    android.os.Handler handler) {
        android.support.v4.hardware.fingerprint.FingerprintManagerCompat.IMPL.authenticate(mContext, crypto, flags, cancel, callback, handler);
    }

    /**
     * A wrapper class for the crypto objects supported by FingerprintManager. Currently the
     * framework supports {@link Signature} and {@link Cipher} objects.
     */
    public static class CryptoObject {
        private final java.security.Signature mSignature;

        private final javax.crypto.Cipher mCipher;

        private final javax.crypto.Mac mMac;

        public CryptoObject(java.security.Signature signature) {
            mSignature = signature;
            mCipher = null;
            mMac = null;
        }

        public CryptoObject(javax.crypto.Cipher cipher) {
            mCipher = cipher;
            mSignature = null;
            mMac = null;
        }

        public CryptoObject(javax.crypto.Mac mac) {
            mMac = mac;
            mCipher = null;
            mSignature = null;
        }

        /**
         * Get {@link Signature} object.
         *
         * @return {@link Signature} object or null if this doesn't contain one.
         */
        public java.security.Signature getSignature() {
            return mSignature;
        }

        /**
         * Get {@link Cipher} object.
         *
         * @return {@link Cipher} object or null if this doesn't contain one.
         */
        public javax.crypto.Cipher getCipher() {
            return mCipher;
        }

        /**
         * Get {@link Mac} object.
         *
         * @return {@link Mac} object or null if this doesn't contain one.
         */
        public javax.crypto.Mac getMac() {
            return mMac;
        }
    }

    /**
     * Container for callback data from {@link FingerprintManagerCompat#authenticate(CryptoObject,
     *     int, CancellationSignal, AuthenticationCallback, Handler)}.
     */
    public static final class AuthenticationResult {
        private android.support.v4.hardware.fingerprint.FingerprintManagerCompat.CryptoObject mCryptoObject;

        public AuthenticationResult(android.support.v4.hardware.fingerprint.FingerprintManagerCompat.CryptoObject crypto) {
            mCryptoObject = crypto;
        }

        /**
         * Obtain the crypto object associated with this transaction
         *
         * @return crypto object provided to {@link FingerprintManagerCompat#authenticate(
        CryptoObject, int, CancellationSignal, AuthenticationCallback, Handler)}.
         */
        public android.support.v4.hardware.fingerprint.FingerprintManagerCompat.CryptoObject getCryptoObject() {
            return mCryptoObject;
        }
    }

    /**
     * Callback structure provided to {@link FingerprintManagerCompat#authenticate(CryptoObject,
     * int, CancellationSignal, AuthenticationCallback, Handler)}. Users of {@link FingerprintManagerCompat#authenticate(CryptoObject, int, CancellationSignal,
     * AuthenticationCallback, Handler)} must provide an implementation of this for listening to
     * fingerprint events.
     */
    public static abstract class AuthenticationCallback {
        /**
         * Called when an unrecoverable error has been encountered and the operation is complete.
         * No further callbacks will be made on this object.
         *
         * @param errMsgId
         * 		An integer identifying the error message
         * @param errString
         * 		A human-readable error string that can be shown in UI
         */
        public void onAuthenticationError(int errMsgId, java.lang.CharSequence errString) {
        }

        /**
         * Called when a recoverable error has been encountered during authentication. The help
         * string is provided to give the user guidance for what went wrong, such as
         * "Sensor dirty, please clean it."
         *
         * @param helpMsgId
         * 		An integer identifying the error message
         * @param helpString
         * 		A human-readable string that can be shown in UI
         */
        public void onAuthenticationHelp(int helpMsgId, java.lang.CharSequence helpString) {
        }

        /**
         * Called when a fingerprint is recognized.
         *
         * @param result
         * 		An object containing authentication-related data
         */
        public void onAuthenticationSucceeded(android.support.v4.hardware.fingerprint.FingerprintManagerCompat.AuthenticationResult result) {
        }

        /**
         * Called when a fingerprint is valid but not recognized.
         */
        public void onAuthenticationFailed() {
        }
    }

    private interface FingerprintManagerCompatImpl {
        boolean hasEnrolledFingerprints(android.content.Context context);

        boolean isHardwareDetected(android.content.Context context);

        void authenticate(android.content.Context context, android.support.v4.hardware.fingerprint.FingerprintManagerCompat.CryptoObject crypto, int flags, android.support.v4.os.CancellationSignal cancel, android.support.v4.hardware.fingerprint.FingerprintManagerCompat.AuthenticationCallback callback, android.os.Handler handler);
    }

    private static class LegacyFingerprintManagerCompatImpl implements android.support.v4.hardware.fingerprint.FingerprintManagerCompat.FingerprintManagerCompatImpl {
        public LegacyFingerprintManagerCompatImpl() {
        }

        @java.lang.Override
        public boolean hasEnrolledFingerprints(android.content.Context context) {
            return false;
        }

        @java.lang.Override
        public boolean isHardwareDetected(android.content.Context context) {
            return false;
        }

        @java.lang.Override
        public void authenticate(android.content.Context context, android.support.v4.hardware.fingerprint.FingerprintManagerCompat.CryptoObject crypto, int flags, android.support.v4.os.CancellationSignal cancel, android.support.v4.hardware.fingerprint.FingerprintManagerCompat.AuthenticationCallback callback, android.os.Handler handler) {
            // TODO: Figure out behavior when there is no fingerprint hardware available
        }
    }

    private static class Api23FingerprintManagerCompatImpl implements android.support.v4.hardware.fingerprint.FingerprintManagerCompat.FingerprintManagerCompatImpl {
        public Api23FingerprintManagerCompatImpl() {
        }

        @java.lang.Override
        public boolean hasEnrolledFingerprints(android.content.Context context) {
            return android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.hasEnrolledFingerprints(context);
        }

        @java.lang.Override
        public boolean isHardwareDetected(android.content.Context context) {
            return android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.isHardwareDetected(context);
        }

        @java.lang.Override
        public void authenticate(android.content.Context context, android.support.v4.hardware.fingerprint.FingerprintManagerCompat.CryptoObject crypto, int flags, android.support.v4.os.CancellationSignal cancel, android.support.v4.hardware.fingerprint.FingerprintManagerCompat.AuthenticationCallback callback, android.os.Handler handler) {
            android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.authenticate(context, android.support.v4.hardware.fingerprint.FingerprintManagerCompat.Api23FingerprintManagerCompatImpl.wrapCryptoObject(crypto), flags, cancel != null ? cancel.getCancellationSignalObject() : null, android.support.v4.hardware.fingerprint.FingerprintManagerCompat.Api23FingerprintManagerCompatImpl.wrapCallback(callback), handler);
        }

        private static android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.CryptoObject wrapCryptoObject(android.support.v4.hardware.fingerprint.FingerprintManagerCompat.CryptoObject cryptoObject) {
            if (cryptoObject == null) {
                return null;
            } else
                if (cryptoObject.getCipher() != null) {
                    return new android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.CryptoObject(cryptoObject.getCipher());
                } else
                    if (cryptoObject.getSignature() != null) {
                        return new android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.CryptoObject(cryptoObject.getSignature());
                    } else
                        if (cryptoObject.getMac() != null) {
                            return new android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.CryptoObject(cryptoObject.getMac());
                        } else {
                            return null;
                        }



        }

        static android.support.v4.hardware.fingerprint.FingerprintManagerCompat.CryptoObject unwrapCryptoObject(android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.CryptoObject cryptoObject) {
            if (cryptoObject == null) {
                return null;
            } else
                if (cryptoObject.getCipher() != null) {
                    return new android.support.v4.hardware.fingerprint.FingerprintManagerCompat.CryptoObject(cryptoObject.getCipher());
                } else
                    if (cryptoObject.getSignature() != null) {
                        return new android.support.v4.hardware.fingerprint.FingerprintManagerCompat.CryptoObject(cryptoObject.getSignature());
                    } else
                        if (cryptoObject.getMac() != null) {
                            return new android.support.v4.hardware.fingerprint.FingerprintManagerCompat.CryptoObject(cryptoObject.getMac());
                        } else {
                            return null;
                        }



        }

        private static android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.AuthenticationCallback wrapCallback(final android.support.v4.hardware.fingerprint.FingerprintManagerCompat.AuthenticationCallback callback) {
            return new android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.AuthenticationCallback() {
                @java.lang.Override
                public void onAuthenticationError(int errMsgId, java.lang.CharSequence errString) {
                    callback.onAuthenticationError(errMsgId, errString);
                }

                @java.lang.Override
                public void onAuthenticationHelp(int helpMsgId, java.lang.CharSequence helpString) {
                    callback.onAuthenticationHelp(helpMsgId, helpString);
                }

                @java.lang.Override
                public void onAuthenticationSucceeded(android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.AuthenticationResultInternal result) {
                    callback.onAuthenticationSucceeded(new android.support.v4.hardware.fingerprint.FingerprintManagerCompat.AuthenticationResult(android.support.v4.hardware.fingerprint.FingerprintManagerCompat.Api23FingerprintManagerCompatImpl.unwrapCryptoObject(result.getCryptoObject())));
                }

                @java.lang.Override
                public void onAuthenticationFailed() {
                    callback.onAuthenticationFailed();
                }
            };
        }
    }
}

