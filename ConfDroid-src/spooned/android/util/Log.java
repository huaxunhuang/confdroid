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
 * API for sending log output.
 *
 * <p>Generally, use the Log.v() Log.d() Log.i() Log.w() and Log.e()
 * methods.
 *
 * <p>The order in terms of verbosity, from least to most is
 * ERROR, WARN, INFO, DEBUG, VERBOSE.  Verbose should never be compiled
 * into an application except during development.  Debug logs are compiled
 * in but stripped at runtime.  Error, warning and info logs are always kept.
 *
 * <p><b>Tip:</b> A good convention is to declare a <code>TAG</code> constant
 * in your class:
 *
 * <pre>private static final String TAG = "MyActivity";</pre>
 *
 * and use that in subsequent calls to the log methods.
 * </p>
 *
 * <p><b>Tip:</b> Don't forget that when you make a call like
 * <pre>Log.v(TAG, "index=" + i);</pre>
 * that when you're building the string to pass into Log.d, the compiler uses a
 * StringBuilder and at least three allocations occur: the StringBuilder
 * itself, the buffer, and the String object.  Realistically, there is also
 * another buffer allocation and copy, and even more pressure on the gc.
 * That means that if your log message is filtered out, you might be doing
 * significant work and incurring significant overhead.
 */
public final class Log {
    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    /**
     * Exception class used to capture a stack trace in {@link #wtf}.
     */
    private static class TerribleFailure extends java.lang.Exception {
        TerribleFailure(java.lang.String msg, java.lang.Throwable cause) {
            super(msg, cause);
        }
    }

    /**
     * Interface to handle terrible failures from {@link #wtf}.
     *
     * @unknown 
     */
    public interface TerribleFailureHandler {
        void onTerribleFailure(java.lang.String tag, android.util.Log.TerribleFailure what, boolean system);
    }

    private static android.util.Log.TerribleFailureHandler sWtfHandler = new android.util.Log.TerribleFailureHandler() {
        public void onTerribleFailure(java.lang.String tag, android.util.Log.TerribleFailure what, boolean system) {
            com.android.internal.os.RuntimeInit.wtf(tag, what, system);
        }
    };

    private Log() {
    }

    /**
     * Send a {@link #VERBOSE} log message.
     *
     * @param tag
     * 		Used to identify the source of a log message.  It usually identifies
     * 		the class or activity where the log call occurs.
     * @param msg
     * 		The message you would like logged.
     */
    public static int v(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_MAIN, android.util.Log.VERBOSE, tag, msg);
    }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     *
     * @param tag
     * 		Used to identify the source of a log message.  It usually identifies
     * 		the class or activity where the log call occurs.
     * @param msg
     * 		The message you would like logged.
     * @param tr
     * 		An exception to log
     */
    public static int v(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.printlns(android.util.Log.LOG_ID_MAIN, android.util.Log.VERBOSE, tag, msg, tr);
    }

    /**
     * Send a {@link #DEBUG} log message.
     *
     * @param tag
     * 		Used to identify the source of a log message.  It usually identifies
     * 		the class or activity where the log call occurs.
     * @param msg
     * 		The message you would like logged.
     */
    public static int d(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_MAIN, android.util.Log.DEBUG, tag, msg);
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     *
     * @param tag
     * 		Used to identify the source of a log message.  It usually identifies
     * 		the class or activity where the log call occurs.
     * @param msg
     * 		The message you would like logged.
     * @param tr
     * 		An exception to log
     */
    public static int d(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.printlns(android.util.Log.LOG_ID_MAIN, android.util.Log.DEBUG, tag, msg, tr);
    }

    /**
     * Send an {@link #INFO} log message.
     *
     * @param tag
     * 		Used to identify the source of a log message.  It usually identifies
     * 		the class or activity where the log call occurs.
     * @param msg
     * 		The message you would like logged.
     */
    public static int i(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_MAIN, android.util.Log.INFO, tag, msg);
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     *
     * @param tag
     * 		Used to identify the source of a log message.  It usually identifies
     * 		the class or activity where the log call occurs.
     * @param msg
     * 		The message you would like logged.
     * @param tr
     * 		An exception to log
     */
    public static int i(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.printlns(android.util.Log.LOG_ID_MAIN, android.util.Log.INFO, tag, msg, tr);
    }

    /**
     * Send a {@link #WARN} log message.
     *
     * @param tag
     * 		Used to identify the source of a log message.  It usually identifies
     * 		the class or activity where the log call occurs.
     * @param msg
     * 		The message you would like logged.
     */
    public static int w(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_MAIN, android.util.Log.WARN, tag, msg);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag
     * 		Used to identify the source of a log message.  It usually identifies
     * 		the class or activity where the log call occurs.
     * @param msg
     * 		The message you would like logged.
     * @param tr
     * 		An exception to log
     */
    public static int w(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.printlns(android.util.Log.LOG_ID_MAIN, android.util.Log.WARN, tag, msg, tr);
    }

    /**
     * Checks to see whether or not a log for the specified tag is loggable at the specified level.
     *
     *  The default level of any tag is set to INFO. This means that any level above and including
     *  INFO will be logged. Before you make any calls to a logging method you should check to see
     *  if your tag should be logged. You can change the default level by setting a system property:
     *      'setprop log.tag.&lt;YOUR_LOG_TAG> &lt;LEVEL>'
     *  Where level is either VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT, or SUPPRESS. SUPPRESS will
     *  turn off all logging for your tag. You can also create a local.prop file that with the
     *  following in it:
     *      'log.tag.&lt;YOUR_LOG_TAG>=&lt;LEVEL>'
     *  and place that in /data/local.prop.
     *
     * @param tag
     * 		The tag to check.
     * @param level
     * 		The level to check.
     * @return Whether or not that this is allowed to be logged.
     * @throws IllegalArgumentException
     * 		is thrown if the tag.length() > 23.
     */
    public static native boolean isLoggable(java.lang.String tag, int level);

    /* Send a {@link #WARN} log message and log the exception.
    @param tag Used to identify the source of a log message.  It usually identifies
           the class or activity where the log call occurs.
    @param tr An exception to log
     */
    public static int w(java.lang.String tag, java.lang.Throwable tr) {
        return android.util.Log.printlns(android.util.Log.LOG_ID_MAIN, android.util.Log.WARN, tag, "", tr);
    }

    /**
     * Send an {@link #ERROR} log message.
     *
     * @param tag
     * 		Used to identify the source of a log message.  It usually identifies
     * 		the class or activity where the log call occurs.
     * @param msg
     * 		The message you would like logged.
     */
    public static int e(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_MAIN, android.util.Log.ERROR, tag, msg);
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     *
     * @param tag
     * 		Used to identify the source of a log message.  It usually identifies
     * 		the class or activity where the log call occurs.
     * @param msg
     * 		The message you would like logged.
     * @param tr
     * 		An exception to log
     */
    public static int e(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.printlns(android.util.Log.LOG_ID_MAIN, android.util.Log.ERROR, tag, msg, tr);
    }

    /**
     * What a Terrible Failure: Report a condition that should never happen.
     * The error will always be logged at level ASSERT with the call stack.
     * Depending on system configuration, a report may be added to the
     * {@link android.os.DropBoxManager} and/or the process may be terminated
     * immediately with an error dialog.
     *
     * @param tag
     * 		Used to identify the source of a log message.
     * @param msg
     * 		The message you would like logged.
     */
    public static int wtf(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.wtf(android.util.Log.LOG_ID_MAIN, tag, msg, null, false, false);
    }

    /**
     * Like {@link #wtf(String, String)}, but also writes to the log the full
     * call stack.
     *
     * @unknown 
     */
    public static int wtfStack(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.wtf(android.util.Log.LOG_ID_MAIN, tag, msg, null, true, false);
    }

    /**
     * What a Terrible Failure: Report an exception that should never happen.
     * Similar to {@link #wtf(String, String)}, with an exception to log.
     *
     * @param tag
     * 		Used to identify the source of a log message.
     * @param tr
     * 		An exception to log.
     */
    public static int wtf(java.lang.String tag, java.lang.Throwable tr) {
        return android.util.Log.wtf(android.util.Log.LOG_ID_MAIN, tag, tr.getMessage(), tr, false, false);
    }

    /**
     * What a Terrible Failure: Report an exception that should never happen.
     * Similar to {@link #wtf(String, Throwable)}, with a message as well.
     *
     * @param tag
     * 		Used to identify the source of a log message.
     * @param msg
     * 		The message you would like logged.
     * @param tr
     * 		An exception to log.  May be null.
     */
    public static int wtf(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.wtf(android.util.Log.LOG_ID_MAIN, tag, msg, tr, false, false);
    }

    static int wtf(int logId, java.lang.String tag, java.lang.String msg, java.lang.Throwable tr, boolean localStack, boolean system) {
        android.util.Log.TerribleFailure what = new android.util.Log.TerribleFailure(msg, tr);
        // Only mark this as ERROR, do not use ASSERT since that should be
        // reserved for cases where the system is guaranteed to abort.
        // The onTerribleFailure call does not always cause a crash.
        int bytes = android.util.Log.printlns(logId, android.util.Log.ERROR, tag, msg, localStack ? what : tr);
        android.util.Log.sWtfHandler.onTerribleFailure(tag, what, system);
        return bytes;
    }

    static void wtfQuiet(int logId, java.lang.String tag, java.lang.String msg, boolean system) {
        android.util.Log.TerribleFailure what = new android.util.Log.TerribleFailure(msg, null);
        android.util.Log.sWtfHandler.onTerribleFailure(tag, what, system);
    }

    /**
     * Sets the terrible failure handler, for testing.
     *
     * @return the old handler
     * @unknown 
     */
    public static android.util.Log.TerribleFailureHandler setWtfHandler(android.util.Log.TerribleFailureHandler handler) {
        if (handler == null) {
            throw new java.lang.NullPointerException("handler == null");
        }
        android.util.Log.TerribleFailureHandler oldHandler = android.util.Log.sWtfHandler;
        android.util.Log.sWtfHandler = handler;
        return oldHandler;
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr
     * 		An exception to log
     */
    public static java.lang.String getStackTraceString(java.lang.Throwable tr) {
        if (tr == null) {
            return "";
        }
        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        java.lang.Throwable t = tr;
        while (t != null) {
            if (t instanceof java.net.UnknownHostException) {
                return "";
            }
            t = t.getCause();
        } 
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(sw, false, 256);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /**
     * Low-level logging call.
     *
     * @param priority
     * 		The priority/type of this log message
     * @param tag
     * 		Used to identify the source of a log message.  It usually identifies
     * 		the class or activity where the log call occurs.
     * @param msg
     * 		The message you would like logged.
     * @return The number of bytes written.
     */
    public static int println(int priority, java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println_native(android.util.Log.LOG_ID_MAIN, priority, tag, msg);
    }

    /**
     *
     *
     * @unknown 
     */
    public static final int LOG_ID_MAIN = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int LOG_ID_RADIO = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int LOG_ID_EVENTS = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int LOG_ID_SYSTEM = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int LOG_ID_CRASH = 4;

    /**
     *
     *
     * @unknown 
     */
    public static native int println_native(int bufID, int priority, java.lang.String tag, java.lang.String msg);

    /**
     * Return the maximum payload the log daemon accepts without truncation.
     *
     * @return LOGGER_ENTRY_MAX_PAYLOAD.
     */
    private static native int logger_entry_max_payload_native();

    /**
     * Helper function for long messages. Uses the LineBreakBufferedWriter to break
     * up long messages and stacktraces along newlines, but tries to write in large
     * chunks. This is to avoid truncation.
     *
     * @unknown 
     */
    public static int printlns(int bufID, int priority, java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        android.util.Log.ImmediateLogWriter logWriter = new android.util.Log.ImmediateLogWriter(bufID, priority, tag);
        // Acceptable buffer size. Get the native buffer size, subtract two zero terminators,
        // and the length of the tag.
        // Note: we implicitly accept possible truncation for Modified-UTF8 differences. It
        // is too expensive to compute that ahead of time.
        int bufferSize = ((android.util.Log.NoPreloadHolder.LOGGER_ENTRY_MAX_PAYLOAD// Base.
         - 2) - // Two terminators.
        (tag != null ? tag.length() : 0))// Tag length.
         - 32;// Some slack.

        // At least assume you can print *some* characters (tag is not too large).
        bufferSize = java.lang.Math.max(bufferSize, 100);
        com.android.internal.util.LineBreakBufferedWriter lbbw = new com.android.internal.util.LineBreakBufferedWriter(logWriter, bufferSize);
        lbbw.println(msg);
        if (tr != null) {
            // This is to reduce the amount of log spew that apps do in the non-error
            // condition of the network being unavailable.
            java.lang.Throwable t = tr;
            while (t != null) {
                if (t instanceof java.net.UnknownHostException) {
                    break;
                }
                if (t instanceof android.os.DeadSystemException) {
                    lbbw.println("DeadSystemException: The system died; " + "earlier logs will point to the root cause");
                    break;
                }
                t = t.getCause();
            } 
            if (t == null) {
                tr.printStackTrace(lbbw);
            }
        }
        lbbw.flush();
        return logWriter.getWritten();
    }

    /**
     * NoPreloadHelper class. Caches the LOGGER_ENTRY_MAX_PAYLOAD value to avoid
     * a JNI call during logging.
     */
    static class NoPreloadHolder {
        public static final int LOGGER_ENTRY_MAX_PAYLOAD = android.util.Log.logger_entry_max_payload_native();
    }

    /**
     * Helper class to write to the logcat. Different from LogWriter, this writes
     * the whole given buffer and does not break along newlines.
     */
    private static class ImmediateLogWriter extends java.io.Writer {
        private int bufID;

        private int priority;

        private java.lang.String tag;

        private int written = 0;

        /**
         * Create a writer that immediately writes to the log, using the given
         * parameters.
         */
        public ImmediateLogWriter(int bufID, int priority, java.lang.String tag) {
            this.bufID = bufID;
            this.priority = priority;
            this.tag = tag;
        }

        public int getWritten() {
            return written;
        }

        @java.lang.Override
        public void write(char[] cbuf, int off, int len) {
            // Note: using String here has a bit of overhead as a Java object is created,
            // but using the char[] directly is not easier, as it needs to be translated
            // to a C char[] for logging.
            written += android.util.Log.println_native(bufID, priority, tag, new java.lang.String(cbuf, off, len));
        }

        @java.lang.Override
        public void flush() {
            // Ignored.
        }

        @java.lang.Override
        public void close() {
            // Ignored.
        }
    }
}

