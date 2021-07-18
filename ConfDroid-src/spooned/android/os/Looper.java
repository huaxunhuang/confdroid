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
package android.os;


/**
 * Class used to run a message loop for a thread.  Threads by default do
 * not have a message loop associated with them; to create one, call
 * {@link #prepare} in the thread that is to run the loop, and then
 * {@link #loop} to have it process messages until the loop is stopped.
 *
 * <p>Most interaction with a message loop is through the
 * {@link Handler} class.
 *
 * <p>This is a typical example of the implementation of a Looper thread,
 * using the separation of {@link #prepare} and {@link #loop} to create an
 * initial Handler to communicate with the Looper.
 *
 * <pre>
 *  class LooperThread extends Thread {
 *      public Handler mHandler;
 *
 *      public void run() {
 *          Looper.prepare();
 *
 *          mHandler = new Handler() {
 *              public void handleMessage(Message msg) {
 *                  // process incoming messages here
 *              }
 *          };
 *
 *          Looper.loop();
 *      }
 *  }</pre>
 */
public final class Looper {
    /* API Implementation Note:

    This class contains the code required to set up and manage an event loop
    based on MessageQueue.  APIs that affect the state of the queue should be
    defined on MessageQueue or Handler rather than on Looper itself.  For example,
    idle handlers and sync barriers are defined on the queue whereas preparing the
    thread, looping, and quitting are defined on the looper.
     */
    private static final java.lang.String TAG = "Looper";

    // sThreadLocal.get() will return null unless you've called prepare().
    static final java.lang.ThreadLocal<android.os.Looper> sThreadLocal = new java.lang.ThreadLocal<android.os.Looper>();

    private static android.os.Looper sMainLooper;// guarded by Looper.class


    final android.os.MessageQueue mQueue;

    final java.lang.Thread mThread;

    private android.util.Printer mLogging;

    private long mTraceTag;

    /**
     * Initialize the current thread as a looper.
     * This gives you a chance to create handlers that then reference
     * this looper, before actually starting the loop. Be sure to call
     * {@link #loop()} after calling this method, and end it by calling
     * {@link #quit()}.
     */
    public static void prepare() {
        android.os.Looper.prepare(true);
    }

    private static void prepare(boolean quitAllowed) {
        if (android.os.Looper.sThreadLocal.get() != null) {
            throw new java.lang.RuntimeException("Only one Looper may be created per thread");
        }
        android.os.Looper.sThreadLocal.set(new android.os.Looper(quitAllowed));
    }

    /**
     * Initialize the current thread as a looper, marking it as an
     * application's main looper. The main looper for your application
     * is created by the Android environment, so you should never need
     * to call this function yourself.  See also: {@link #prepare()}
     */
    public static void prepareMainLooper() {
        android.os.Looper.prepare(false);
        synchronized(android.os.Looper.class) {
            if (android.os.Looper.sMainLooper != null) {
                throw new java.lang.IllegalStateException("The main Looper has already been prepared.");
            }
            android.os.Looper.sMainLooper = android.os.Looper.myLooper();
        }
    }

    /**
     * Returns the application's main looper, which lives in the main thread of the application.
     */
    public static android.os.Looper getMainLooper() {
        synchronized(android.os.Looper.class) {
            return android.os.Looper.sMainLooper;
        }
    }

    /**
     * Run the message queue in this thread. Be sure to call
     * {@link #quit()} to end the loop.
     */
    public static void loop() {
        final android.os.Looper me = android.os.Looper.myLooper();
        if (me == null) {
            throw new java.lang.RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        final android.os.MessageQueue queue = me.mQueue;
        // Make sure the identity of this thread is that of the local process,
        // and keep track of what that identity token actually is.
        android.os.Binder.clearCallingIdentity();
        final long ident = android.os.Binder.clearCallingIdentity();
        for (; ;) {
            android.os.Message msg = queue.next();// might block

            if (msg == null) {
                // No message indicates that the message queue is quitting.
                return;
            }
            // This must be in a local variable, in case a UI event sets the logger
            final android.util.Printer logging = me.mLogging;
            if (logging != null) {
                logging.println(((((">>>>> Dispatching to " + msg.target) + " ") + msg.callback) + ": ") + msg.what);
            }
            final long traceTag = me.mTraceTag;
            if ((traceTag != 0) && android.os.Trace.isTagEnabled(traceTag)) {
                android.os.Trace.traceBegin(traceTag, msg.target.getTraceName(msg));
            }
            try {
                msg.target.dispatchMessage(msg);
            } finally {
                if (traceTag != 0) {
                    android.os.Trace.traceEnd(traceTag);
                }
            }
            if (logging != null) {
                logging.println((("<<<<< Finished to " + msg.target) + " ") + msg.callback);
            }
            // Make sure that during the course of dispatching the
            // identity of the thread wasn't corrupted.
            final long newIdent = android.os.Binder.clearCallingIdentity();
            if (ident != newIdent) {
                android.util.Log.wtf(android.os.Looper.TAG, (((((((("Thread identity changed from 0x" + java.lang.Long.toHexString(ident)) + " to 0x") + java.lang.Long.toHexString(newIdent)) + " while dispatching to ") + msg.target.getClass().getName()) + " ") + msg.callback) + " what=") + msg.what);
            }
            msg.recycleUnchecked();
        }
    }

    /**
     * Return the Looper object associated with the current thread.  Returns
     * null if the calling thread is not associated with a Looper.
     */
    @android.annotation.Nullable
    public static android.os.Looper myLooper() {
        return android.os.Looper.sThreadLocal.get();
    }

    /**
     * Return the {@link MessageQueue} object associated with the current
     * thread.  This must be called from a thread running a Looper, or a
     * NullPointerException will be thrown.
     */
    @android.annotation.NonNull
    public static android.os.MessageQueue myQueue() {
        return android.os.Looper.myLooper().mQueue;
    }

    private Looper(boolean quitAllowed) {
        mQueue = new android.os.MessageQueue(quitAllowed);
        mThread = java.lang.Thread.currentThread();
    }

    /**
     * Returns true if the current thread is this looper's thread.
     */
    public boolean isCurrentThread() {
        return java.lang.Thread.currentThread() == mThread;
    }

    /**
     * Control logging of messages as they are processed by this Looper.  If
     * enabled, a log message will be written to <var>printer</var>
     * at the beginning and ending of each message dispatch, identifying the
     * target Handler and message contents.
     *
     * @param printer
     * 		A Printer object that will receive log messages, or
     * 		null to disable message logging.
     */
    public void setMessageLogging(@android.annotation.Nullable
    android.util.Printer printer) {
        mLogging = printer;
    }

    /**
     * {@hide }
     */
    public void setTraceTag(long traceTag) {
        mTraceTag = traceTag;
    }

    /**
     * Quits the looper.
     * <p>
     * Causes the {@link #loop} method to terminate without processing any
     * more messages in the message queue.
     * </p><p>
     * Any attempt to post messages to the queue after the looper is asked to quit will fail.
     * For example, the {@link Handler#sendMessage(Message)} method will return false.
     * </p><p class="note">
     * Using this method may be unsafe because some messages may not be delivered
     * before the looper terminates.  Consider using {@link #quitSafely} instead to ensure
     * that all pending work is completed in an orderly manner.
     * </p>
     *
     * @see #quitSafely
     */
    public void quit() {
        mQueue.quit(false);
    }

    /**
     * Quits the looper safely.
     * <p>
     * Causes the {@link #loop} method to terminate as soon as all remaining messages
     * in the message queue that are already due to be delivered have been handled.
     * However pending delayed messages with due times in the future will not be
     * delivered before the loop terminates.
     * </p><p>
     * Any attempt to post messages to the queue after the looper is asked to quit will fail.
     * For example, the {@link Handler#sendMessage(Message)} method will return false.
     * </p>
     */
    public void quitSafely() {
        mQueue.quit(true);
    }

    /**
     * Gets the Thread associated with this Looper.
     *
     * @return The looper's thread.
     */
    @android.annotation.NonNull
    public java.lang.Thread getThread() {
        return mThread;
    }

    /**
     * Gets this looper's message queue.
     *
     * @return The looper's message queue.
     */
    @android.annotation.NonNull
    public android.os.MessageQueue getQueue() {
        return mQueue;
    }

    /**
     * Dumps the state of the looper for debugging purposes.
     *
     * @param pw
     * 		A printer to receive the contents of the dump.
     * @param prefix
     * 		A prefix to prepend to each line which is printed.
     */
    public void dump(@android.annotation.NonNull
    android.util.Printer pw, @android.annotation.NonNull
    java.lang.String prefix) {
        pw.println(prefix + toString());
        mQueue.dump(pw, prefix + "  ");
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("Looper (" + mThread.getName()) + ", tid ") + mThread.getId()) + ") {") + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + "}";
    }
}

