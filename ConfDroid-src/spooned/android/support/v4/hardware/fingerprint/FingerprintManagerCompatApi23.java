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
 * Actual FingerprintManagerCompat implementation for API level 23 and later.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public final class FingerprintManagerCompatApi23 {
    private static android.hardware.fingerprint.FingerprintManager getFingerprintManager(android.content.Context ctx) {
        return ctx.getSystemService(android.hardware.fingerprint.FingerprintManager.class);
    }

    public static boolean hasEnrolledFingerprints(android.content.Context context) {
        return android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.getFingerprintManager(context).hasEnrolledFingerprints();
    }

    public static boolean isHardwareDetected(android.content.Context context) {
        return android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.getFingerprintManager(context).isHardwareDetected();
    }

    public static void authenticate(android.content.Context context, android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.CryptoObject crypto, int flags, java.lang.Object cancel, android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.AuthenticationCallback callback, android.os.Handler handler) {
        android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.getFingerprintManager(context).authenticate(android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.wrapCryptoObject(crypto), ((android.os.CancellationSignal) (cancel)), flags, android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.wrapCallback(callback), handler);
    }

    private static android.hardware.fingerprint.FingerprintManager.CryptoObject wrapCryptoObject(android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.CryptoObject cryptoObject) {
        if (cryptoObject == null) {
            return null;
        } else
            if (cryptoObject.getCipher() != null) {
                return new android.hardware.fingerprint.FingerprintManager.CryptoObject(cryptoObject.getCipher());
            } else
                if (cryptoObject.getSignature() != null) {
                    return new android.hardware.fingerprint.FingerprintManager.CryptoObject(cryptoObject.getSignature());
                } else
                    if (cryptoObject.getMac() != null) {
                        return new android.hardware.fingerprint.FingerprintManager.CryptoObject(cryptoObject.getMac());
                    } else {
                        return null;
                    }



    }

    private static android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.CryptoObject unwrapCryptoObject(android.hardware.fingerprint.FingerprintManager.CryptoObject cryptoObject) {
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

    private static android.hardware.fingerprint.FingerprintManager.AuthenticationCallback wrapCallback(final android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.AuthenticationCallback callback) {
        return new android.hardware.fingerprint.FingerprintManager.AuthenticationCallback() {
            @java.lang.Override
            public void onAuthenticationError(int errMsgId, java.lang.CharSequence errString) {
                callback.onAuthenticationError(errMsgId, errString);
            }

            @java.lang.Override
            public void onAuthenticationHelp(int helpMsgId, java.lang.CharSequence helpString) {
                callback.onAuthenticationHelp(helpMsgId, helpString);
            }

            @java.lang.Override
            public void onAuthenticationSucceeded(android.hardware.fingerprint.FingerprintManager.AuthenticationResult result) {
                callback.onAuthenticationSucceeded(new android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.AuthenticationResultInternal(android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.unwrapCryptoObject(result.getCryptoObject())));
            }

            @java.lang.Override
            public void onAuthenticationFailed() {
                callback.onAuthenticationFailed();
            }
        };
    }

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

        public java.security.Signature getSignature() {
            return mSignature;
        }

        public javax.crypto.Cipher getCipher() {
            return mCipher;
        }

        public javax.crypto.Mac getMac() {
            return mMac;
        }
    }

    public static final class AuthenticationResultInternal {
        private android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.CryptoObject mCryptoObject;

        public AuthenticationResultInternal(android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.CryptoObject crypto) {
            mCryptoObject = crypto;
        }

        public android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.CryptoObject getCryptoObject() {
            return mCryptoObject;
        }
    }

    public static abstract class AuthenticationCallback {
        public void onAuthenticationError(int errMsgId, java.lang.CharSequence errString) {
        }

        public void onAuthenticationHelp(int helpMsgId, java.lang.CharSequence helpString) {
        }

        public void onAuthenticationSucceeded(android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.AuthenticationResultInternal result) {
        }

        public void onAuthenticationFailed() {
        }
    }
}

