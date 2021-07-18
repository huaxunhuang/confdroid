/**
 * Copyright 2014, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.telecom;


/**
 * Manages logging for the entire module.
 *
 * @unknown 
 */
public final class Log {
    // Generic tag for all Telecom Framework logging
    private static final java.lang.String TAG = "TelecomFramework";

    public static final boolean FORCE_LOGGING = false;/* STOP SHIP if true */


    public static final boolean DEBUG = android.telecom.Log.isLoggable(android.util.Log.DEBUG);

    public static final boolean INFO = android.telecom.Log.isLoggable(android.util.Log.INFO);

    public static final boolean VERBOSE = android.telecom.Log.isLoggable(android.util.Log.VERBOSE);

    public static final boolean WARN = android.telecom.Log.isLoggable(android.util.Log.WARN);

    public static final boolean ERROR = android.telecom.Log.isLoggable(android.util.Log.ERROR);

    private static java.security.MessageDigest sMessageDigest;

    private static final java.lang.Object sMessageDigestLock = new java.lang.Object();

    private Log() {
    }

    public static void initMd5Sum() {
        new android.os.AsyncTask<java.lang.Void, java.lang.Void, java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void doInBackground(java.lang.Void... args) {
                java.security.MessageDigest md;
                try {
                    md = java.security.MessageDigest.getInstance("SHA-1");
                } catch (java.security.NoSuchAlgorithmException e) {
                    md = null;
                }
                synchronized(android.telecom.Log.sMessageDigestLock) {
                    android.telecom.Log.sMessageDigest = md;
                }
                return null;
            }
        }.execute();
    }

    public static boolean isLoggable(int level) {
        return android.telecom.Log.FORCE_LOGGING || android.util.Log.isLoggable(android.telecom.Log.TAG, level);
    }

    public static void d(java.lang.String prefix, java.lang.String format, java.lang.Object... args) {
        if (android.telecom.Log.DEBUG) {
            android.util.Log.d(android.telecom.Log.TAG, android.telecom.Log.buildMessage(prefix, format, args));
        }
    }

    public static void d(java.lang.Object objectPrefix, java.lang.String format, java.lang.Object... args) {
        if (android.telecom.Log.DEBUG) {
            android.util.Log.d(android.telecom.Log.TAG, android.telecom.Log.buildMessage(android.telecom.Log.getPrefixFromObject(objectPrefix), format, args));
        }
    }

    public static void i(java.lang.String prefix, java.lang.String format, java.lang.Object... args) {
        if (android.telecom.Log.INFO) {
            android.util.Log.i(android.telecom.Log.TAG, android.telecom.Log.buildMessage(prefix, format, args));
        }
    }

    public static void i(java.lang.Object objectPrefix, java.lang.String format, java.lang.Object... args) {
        if (android.telecom.Log.INFO) {
            android.util.Log.i(android.telecom.Log.TAG, android.telecom.Log.buildMessage(android.telecom.Log.getPrefixFromObject(objectPrefix), format, args));
        }
    }

    public static void v(java.lang.String prefix, java.lang.String format, java.lang.Object... args) {
        if (android.telecom.Log.VERBOSE) {
            android.util.Log.v(android.telecom.Log.TAG, android.telecom.Log.buildMessage(prefix, format, args));
        }
    }

    public static void v(java.lang.Object objectPrefix, java.lang.String format, java.lang.Object... args) {
        if (android.telecom.Log.VERBOSE) {
            android.util.Log.v(android.telecom.Log.TAG, android.telecom.Log.buildMessage(android.telecom.Log.getPrefixFromObject(objectPrefix), format, args));
        }
    }

    public static void w(java.lang.String prefix, java.lang.String format, java.lang.Object... args) {
        if (android.telecom.Log.WARN) {
            android.util.Log.w(android.telecom.Log.TAG, android.telecom.Log.buildMessage(prefix, format, args));
        }
    }

    public static void w(java.lang.Object objectPrefix, java.lang.String format, java.lang.Object... args) {
        if (android.telecom.Log.WARN) {
            android.util.Log.w(android.telecom.Log.TAG, android.telecom.Log.buildMessage(android.telecom.Log.getPrefixFromObject(objectPrefix), format, args));
        }
    }

    public static void e(java.lang.String prefix, java.lang.Throwable tr, java.lang.String format, java.lang.Object... args) {
        if (android.telecom.Log.ERROR) {
            android.util.Log.e(android.telecom.Log.TAG, android.telecom.Log.buildMessage(prefix, format, args), tr);
        }
    }

    public static void e(java.lang.Object objectPrefix, java.lang.Throwable tr, java.lang.String format, java.lang.Object... args) {
        if (android.telecom.Log.ERROR) {
            android.util.Log.e(android.telecom.Log.TAG, android.telecom.Log.buildMessage(android.telecom.Log.getPrefixFromObject(objectPrefix), format, args), tr);
        }
    }

    public static void wtf(java.lang.String prefix, java.lang.Throwable tr, java.lang.String format, java.lang.Object... args) {
        android.util.Log.wtf(android.telecom.Log.TAG, android.telecom.Log.buildMessage(prefix, format, args), tr);
    }

    public static void wtf(java.lang.Object objectPrefix, java.lang.Throwable tr, java.lang.String format, java.lang.Object... args) {
        android.util.Log.wtf(android.telecom.Log.TAG, android.telecom.Log.buildMessage(android.telecom.Log.getPrefixFromObject(objectPrefix), format, args), tr);
    }

    public static void wtf(java.lang.String prefix, java.lang.String format, java.lang.Object... args) {
        java.lang.String msg = android.telecom.Log.buildMessage(prefix, format, args);
        android.util.Log.wtf(android.telecom.Log.TAG, msg, new java.lang.IllegalStateException(msg));
    }

    public static void wtf(java.lang.Object objectPrefix, java.lang.String format, java.lang.Object... args) {
        java.lang.String msg = android.telecom.Log.buildMessage(android.telecom.Log.getPrefixFromObject(objectPrefix), format, args);
        android.util.Log.wtf(android.telecom.Log.TAG, msg, new java.lang.IllegalStateException(msg));
    }

    /**
     * Redact personally identifiable information for production users.
     * If we are running in verbose mode, return the original string, otherwise
     * return a SHA-1 hash of the input string.
     */
    public static java.lang.String pii(java.lang.Object pii) {
        if ((pii == null) || android.telecom.Log.VERBOSE) {
            return java.lang.String.valueOf(pii);
        }
        if (pii instanceof android.net.Uri) {
            return android.telecom.Log.piiUri(((android.net.Uri) (pii)));
        }
        return ("[" + android.telecom.Log.secureHash(java.lang.String.valueOf(pii).getBytes())) + "]";
    }

    private static java.lang.String piiUri(android.net.Uri handle) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.lang.String scheme = handle.getScheme();
        if (!android.text.TextUtils.isEmpty(scheme)) {
            sb.append(scheme).append(":");
        }
        java.lang.String value = handle.getSchemeSpecificPart();
        if (!android.text.TextUtils.isEmpty(value)) {
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                if (android.telephony.PhoneNumberUtils.isStartsPostDial(c)) {
                    sb.append(c);
                } else
                    if (android.telephony.PhoneNumberUtils.isDialable(c)) {
                        sb.append("*");
                    } else
                        if ((('a' <= c) && (c <= 'z')) || (('A' <= c) && (c <= 'Z'))) {
                            sb.append("*");
                        } else {
                            sb.append(c);
                        }


            }
        }
        return sb.toString();
    }

    private static java.lang.String secureHash(byte[] input) {
        synchronized(android.telecom.Log.sMessageDigestLock) {
            if (android.telecom.Log.sMessageDigest != null) {
                android.telecom.Log.sMessageDigest.reset();
                android.telecom.Log.sMessageDigest.update(input);
                byte[] result = android.telecom.Log.sMessageDigest.digest();
                return android.telecom.Log.encodeHex(result);
            } else {
                return "Uninitialized SHA1";
            }
        }
    }

    private static java.lang.String encodeHex(byte[] bytes) {
        java.lang.StringBuffer hex = new java.lang.StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            int byteIntValue = bytes[i] & 0xff;
            if (byteIntValue < 0x10) {
                hex.append("0");
            }
            hex.append(java.lang.Integer.toString(byteIntValue, 16));
        }
        return hex.toString();
    }

    private static java.lang.String getPrefixFromObject(java.lang.Object obj) {
        return obj == null ? "<null>" : obj.getClass().getSimpleName();
    }

    private static java.lang.String buildMessage(java.lang.String prefix, java.lang.String format, java.lang.Object... args) {
        java.lang.String msg;
        try {
            msg = ((args == null) || (args.length == 0)) ? format : java.lang.String.format(java.util.Locale.US, format, args);
        } catch (java.util.IllegalFormatException ife) {
            android.telecom.Log.wtf("Log", ife, "IllegalFormatException: formatString='%s' numArgs=%d", format, args.length);
            msg = format + " (An error occurred while formatting the message.)";
        }
        return java.lang.String.format(java.util.Locale.US, "%s: %s", prefix, msg);
    }
}

