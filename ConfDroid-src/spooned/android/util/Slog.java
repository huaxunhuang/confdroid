/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.util;


/**
 *
 *
 * @unknown 
 */
public final class Slog {
    private Slog() {
    }

    public static int v(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_SYSTEM, android.util.Log.VERBOSE, tag, msg);
    }

    public static int v(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_SYSTEM, android.util.Log.VERBOSE, tag, (msg + '\n') + android.util.Log.getStackTraceString(tr));
    }

    public static int d(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_SYSTEM, android.util.Log.DEBUG, tag, msg);
    }

    public static int d(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_SYSTEM, android.util.Log.DEBUG, tag, (msg + '\n') + android.util.Log.getStackTraceString(tr));
    }

    public static int i(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_SYSTEM, android.util.Log.INFO, tag, msg);
    }

    public static int i(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_SYSTEM, android.util.Log.INFO, tag, (msg + '\n') + android.util.Log.getStackTraceString(tr));
    }

    public static int w(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_SYSTEM, android.util.Log.WARN, tag, msg);
    }

    public static int w(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_SYSTEM, android.util.Log.WARN, tag, (msg + '\n') + android.util.Log.getStackTraceString(tr));
    }

    public static int w(java.lang.String tag, java.lang.Throwable tr) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_SYSTEM, android.util.Log.WARN, tag, android.util.Log.getStackTraceString(tr));
    }

    public static int e(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_SYSTEM, android.util.Log.ERROR, tag, msg);
    }

    public static int e(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_SYSTEM, android.util.Log.ERROR, tag, (msg + '\n') + android.util.Log.getStackTraceString(tr));
    }

    /**
     * Like {@link Log#wtf(String, String)}, but will never cause the caller to crash, and
     * will always be handled asynchronously.  Primarily for use by coding running within
     * the system process.
     */
    public static int wtf(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.wtf(android.util.Log.LOG_ID_SYSTEM, tag, msg, null, false, true);
    }

    /**
     * Like {@link #wtf(String, String)}, but does not output anything to the log.
     */
    public static void wtfQuiet(java.lang.String tag, java.lang.String msg) {
        android.util.Log.wtfQuiet(android.util.Log.LOG_ID_SYSTEM, tag, msg, true);
    }

    /**
     * Like {@link Log#wtfStack(String, String)}, but will never cause the caller to crash, and
     * will always be handled asynchronously.  Primarily for use by coding running within
     * the system process.
     */
    public static int wtfStack(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.wtf(android.util.Log.LOG_ID_SYSTEM, tag, msg, null, true, true);
    }

    /**
     * Like {@link Log#wtf(String, Throwable)}, but will never cause the caller to crash,
     * and will always be handled asynchronously.  Primarily for use by coding running within
     * the system process.
     */
    public static int wtf(java.lang.String tag, java.lang.Throwable tr) {
        return android.util.Log.wtf(android.util.Log.LOG_ID_SYSTEM, tag, tr.getMessage(), tr, false, true);
    }

    /**
     * Like {@link Log#wtf(String, String, Throwable)}, but will never cause the caller to crash,
     * and will always be handled asynchronously.  Primarily for use by coding running within
     * the system process.
     */
    public static int wtf(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.wtf(android.util.Log.LOG_ID_SYSTEM, tag, msg, tr, false, true);
    }

    public static int println(int priority, java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_SYSTEM, priority, tag, msg);
    }
}

