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
package android.telephony;


/**
 * A class to log strings to the RADIO LOG.
 *
 * @unknown 
 */
public final class Rlog {
    private Rlog() {
    }

    public static int v(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_RADIO, android.util.Log.VERBOSE, tag, msg);
    }

    public static int v(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_RADIO, android.util.Log.VERBOSE, tag, (msg + '\n') + android.util.Log.getStackTraceString(tr));
    }

    public static int d(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_RADIO, android.util.Log.DEBUG, tag, msg);
    }

    public static int d(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_RADIO, android.util.Log.DEBUG, tag, (msg + '\n') + android.util.Log.getStackTraceString(tr));
    }

    public static int i(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_RADIO, android.util.Log.INFO, tag, msg);
    }

    public static int i(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_RADIO, android.util.Log.INFO, tag, (msg + '\n') + android.util.Log.getStackTraceString(tr));
    }

    public static int w(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_RADIO, android.util.Log.WARN, tag, msg);
    }

    public static int w(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_RADIO, android.util.Log.WARN, tag, (msg + '\n') + android.util.Log.getStackTraceString(tr));
    }

    public static int w(java.lang.String tag, java.lang.Throwable tr) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_RADIO, android.util.Log.WARN, tag, android.util.Log.getStackTraceString(tr));
    }

    public static int e(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_RADIO, android.util.Log.ERROR, tag, msg);
    }

    public static int e(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_RADIO, android.util.Log.ERROR, tag, (msg + '\n') + android.util.Log.getStackTraceString(tr));
    }

    public static int println(int priority, java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_RADIO, priority, tag, msg);
    }

    public static boolean isLoggable(java.lang.String tag, int level) {
        return android.util.Log.isLoggable(tag, level);
    }

    /**
     * Redact personally identifiable information for production users.
     *
     * @param tag
     * 		used to identify the source of a log message
     * @param pii
     * 		the personally identifiable information we want to apply secure hash on.
     * @return If tag is loggable in verbose mode or pii is null, return the original input.
    otherwise return a secure Hash of input pii
     */
    public static java.lang.String pii(java.lang.String tag, java.lang.Object pii) {
        java.lang.String val = java.lang.String.valueOf(pii);
        if (((pii == null) || android.text.TextUtils.isEmpty(val)) || android.telephony.Rlog.isLoggable(tag, android.util.Log.VERBOSE)) {
            return val;
        }
        return ("[" + android.telephony.Rlog.secureHash(val.getBytes())) + "]";
    }

    /**
     * Redact personally identifiable information for production users.
     *
     * @param enablePiiLogging
     * 		set when caller explicitly want to enable sensitive logging.
     * @param pii
     * 		the personally identifiable information we want to apply secure hash on.
     * @return If enablePiiLogging is set to true or pii is null, return the original input.
    otherwise return a secure Hash of input pii
     */
    public static java.lang.String pii(boolean enablePiiLogging, java.lang.Object pii) {
        java.lang.String val = java.lang.String.valueOf(pii);
        if (((pii == null) || android.text.TextUtils.isEmpty(val)) || enablePiiLogging) {
            return val;
        }
        return ("[" + android.telephony.Rlog.secureHash(val.getBytes())) + "]";
    }

    /**
     * Returns a secure hash (using the SHA1 algorithm) of the provided input.
     *
     * @return the hash
     * @param input
     * 		the bytes for which the secure hash should be computed.
     */
    private static java.lang.String secureHash(byte[] input) {
        java.security.MessageDigest messageDigest;
        try {
            messageDigest = java.security.MessageDigest.getInstance("SHA-1");
        } catch (java.security.NoSuchAlgorithmException e) {
            return "####";
        }
        byte[] result = messageDigest.digest(input);
        return android.util.Base64.encodeToString(result, (android.util.Base64.URL_SAFE | android.util.Base64.NO_PADDING) | android.util.Base64.NO_WRAP);
    }
}

