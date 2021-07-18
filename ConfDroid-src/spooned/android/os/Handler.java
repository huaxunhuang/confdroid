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
 * A Handler allows you to send and process {@link Message} and Runnable
 * objects associated with a thread's {@link MessageQueue}.  Each Handler
 * instance is associated with a single thread and that thread's message
 * queue.  When you create a new Handler, it is bound to the thread /
 * message queue of the thread that is creating it -- from that point on,
 * it will deliver messages and runnables to that message queue and execute
 * them as they come out of the message queue.
 *
 * <p>There are two main uses for a Handler: (1) to schedule messages and
 * runnables to be executed as some point in the future; and (2) to enqueue
 * an action to be performed on a different thread than your own.
 *
 * <p>Scheduling messages is accomplished with the
 * {@link #post}, {@link #postAtTime(Runnable, long)},
 * {@link #postDelayed}, {@link #sendEmptyMessage},
 * {@link #sendMessage}, {@link #sendMessageAtTime}, and
 * {@link #sendMessageDelayed} methods.  The <em>post</em> versions allow
 * you to enqueue Runnable objects to be called by the message queue when
 * they are received; the <em>sendMessage</em> versions allow you to enqueue
 * a {@link Message} object containing a bundle of data that will be
 * processed by the Handler's {@link #handleMessage} method (requiring that
 * you implement a subclass of Handler).
 *
 * <p>When posting or sending to a Handler, you can either
 * allow the item to be processed as soon as the message queue is ready
 * to do so, or specify a delay before it gets processed or absolute time for
 * it to be processed.  The latter two allow you to implement timeouts,
 * ticks, and other timing-based behavior.
 *
 * <p>When a
 * process is created for your application, its main thread is dedicated to
 * running a message queue that takes care of managing the top-level
 * application objects (activities, broadcast receivers, etc) and any windows
 * they create.  You can create your own threads, and communicate back with
 * the main application thread through a Handler.  This is done by calling
 * the same <em>post</em> or <em>sendMessage</em> methods as before, but from
 * your new thread.  The given Runnable or Message will then be scheduled
 * in the Handler's message queue and processed when appropriate.
 */
public class Handler {
    /* Set this flag to true to detect anonymous, local or member classes
    that extend this Handler class and that are not static. These kind
    of classes can potentially create leaks.
     */
    private static final boolean FIND_POTENTIAL_LEAKS = false;

    private static final java.lang.String TAG = "Handler";

    /**
     * Callback interface you can use when instantiating a Handler to avoid
     * having to implement your own subclass of Handler.
     *
     * @param msg
     * 		A {@link android.os.Message Message} object
     * @return True if no further handling is desired
     */
    public interface Callback {
        public boolean handleMessage(android.os.Message msg);
    }

    /**
     * Subclasses must implement this to receive messages.
     */
    public void handleMessage(android.os.Message msg) {
    }

    /**
     * Handle system messages here.
     */
    public void dispatchMessage(android.os.Message msg) {
        if (msg.callback != null) {
            android.os.Handler.handleCallback(msg);
        } else {
            if (mCallback != null) {
                if (mCallback.handleMessage(msg)) {
                    return;
                }
            }
            handleMessage(msg);
        }
    }

    /**
     * Default constructor associates this handler with the {@link Looper} for the
     * current thread.
     *
     * If this thread does not have a looper, this handler won't be able to receive messages
     * so an exception is thrown.
     */
    public Handler() {
        this(null, false);
    }

    /**
     * Constructor associates this handler with the {@link Looper} for the
     * current thread and takes a callback interface in which you can handle
     * messages.
     *
     * If this thread does not have a looper, this handler won't be able to receive messages
     * so an exception is thrown.
     *
     * @param callback
     * 		The callback interface in which to handle messages, or null.
     */
    public Handler(android.os.Handler.Callback callback) {
        this(callback, false);
    }

    /**
     * Use the provided {@link Looper} instead of the default one.
     *
     * @param looper
     * 		The looper, must not be null.
     */
    public Handler(android.os.Looper looper) {
        this(looper, null, false);
    }

    /**
     * Use the provided {@link Looper} instead of the default one and take a callback
     * interface in which to handle messages.
     *
     * @param looper
     * 		The looper, must not be null.
     * @param callback
     * 		The callback interface in which to handle messages, or null.
     */
    public Handler(android.os.Looper looper, android.os.Handler.Callback callback) {
        this(looper, callback, false);
    }

    /**
     * Use the {@link Looper} for the current thread
     * and set whether the handler should be asynchronous.
     *
     * Handlers are synchronous by default unless this constructor is used to make
     * one that is strictly asynchronous.
     *
     * Asynchronous messages represent interrupts or events that do not require global ordering
     * with respect to synchronous messages.  Asynchronous messages are not subject to
     * the synchronization barriers introduced by {@link MessageQueue#enqueueSyncBarrier(long)}.
     *
     * @param async
     * 		If true, the handler calls {@link Message#setAsynchronous(boolean)} for
     * 		each {@link Message} that is sent to it or {@link Runnable} that is posted to it.
     * @unknown 
     */
    public Handler(boolean async) {
        this(null, async);
    }

    /**
     * Use the {@link Looper} for the current thread with the specified callback interface
     * and set whether the handler should be asynchronous.
     *
     * Handlers are synchronous by default unless this constructor is used to make
     * one that is strictly asynchronous.
     *
     * Asynchronous messages represent interrupts or events that do not require global ordering
     * with respect to synchronous messages.  Asynchronous messages are not subject to
     * the synchronization barriers introduced by {@link MessageQueue#enqueueSyncBarrier(long)}.
     *
     * @param callback
     * 		The callback interface in which to handle messages, or null.
     * @param async
     * 		If true, the handler calls {@link Message#setAsynchronous(boolean)} for
     * 		each {@link Message} that is sent to it or {@link Runnable} that is posted to it.
     * @unknown 
     */
    public Handler(android.os.Handler.Callback callback, boolean async) {
        if (android.os.Handler.FIND_POTENTIAL_LEAKS) {
            final java.lang.Class<? extends android.os.Handler> klass = getClass();
            if (((klass.isAnonymousClass() || klass.isMemberClass()) || klass.isLocalClass()) && ((klass.getModifiers() & java.lang.reflect.Modifier.STATIC) == 0)) {
                android.util.Log.w(android.os.Handler.TAG, "The following Handler class should be static or leaks might occur: " + klass.getCanonicalName());
            }
        }
        mLooper = android.os.Looper.myLooper();
        if (mLooper == null) {
            throw new java.lang.RuntimeException("Can't create handler inside thread that has not called Looper.prepare()");
        }
        mQueue = mLooper.mQueue;
        mCallback = callback;
        mAsynchronous = async;
    }

    /**
     * Use the provided {@link Looper} instead of the default one and take a callback
     * interface in which to handle messages.  Also set whether the handler
     * should be asynchronous.
     *
     * Handlers are synchronous by default unless this constructor is used to make
     * one that is strictly asynchronous.
     *
     * Asynchronous messages represent interrupts or events that do not require global ordering
     * with respect to synchronous messages.  Asynchronous messages are not subject to
     * the synchronization barriers introduced by {@link MessageQueue#enqueueSyncBarrier(long)}.
     *
     * @param looper
     * 		The looper, must not be null.
     * @param callback
     * 		The callback interface in which to handle messages, or null.
     * @param async
     * 		If true, the handler calls {@link Message#setAsynchronous(boolean)} for
     * 		each {@link Message} that is sent to it or {@link Runnable} that is posted to it.
     * @unknown 
     */
    public Handler(android.os.Looper looper, android.os.Handler.Callback callback, boolean async) {
        mLooper = looper;
        mQueue = looper.mQueue;
        mCallback = callback;
        mAsynchronous = async;
    }

    /**
     * {@hide }
     */
    public java.lang.String getTraceName(android.os.Message message) {
        final java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append(getClass().getName()).append(": ");
        if (message.callback != null) {
            sb.append(message.callback.getClass().getName());
        } else {
            sb.append("#").append(message.what);
        }
        return sb.toString();
    }

    /**
     * Returns a string representing the name of the specified message.
     * The default implementation will either return the class name of the
     * message callback if any, or the hexadecimal representation of the
     * message "what" field.
     *
     * @param message
     * 		The message whose name is being queried
     */
    public java.lang.String getMessageName(android.os.Message message) {
        if (message.callback != null) {
            return message.callback.getClass().getName();
        }
        return "0x" + java.lang.Integer.toHexString(message.what);
    }

    /**
     * Returns a new {@link android.os.Message Message} from the global message pool. More efficient than
     * creating and allocating new instances. The retrieved message has its handler set to this instance (Message.target == this).
     *  If you don't want that facility, just call Message.obtain() instead.
     */
    public final android.os.Message obtainMessage() {
        return android.os.Message.obtain(this);
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what member of the returned Message.
     *
     * @param what
     * 		Value to assign to the returned Message.what field.
     * @return A Message from the global message pool.
     */
    public final android.os.Message obtainMessage(int what) {
        return android.os.Message.obtain(this, what);
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what and obj members
     * of the returned Message.
     *
     * @param what
     * 		Value to assign to the returned Message.what field.
     * @param obj
     * 		Value to assign to the returned Message.obj field.
     * @return A Message from the global message pool.
     */
    public final android.os.Message obtainMessage(int what, java.lang.Object obj) {
        return android.os.Message.obtain(this, what, obj);
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what, arg1 and arg2 members of the returned
     * Message.
     *
     * @param what
     * 		Value to assign to the returned Message.what field.
     * @param arg1
     * 		Value to assign to the returned Message.arg1 field.
     * @param arg2
     * 		Value to assign to the returned Message.arg2 field.
     * @return A Message from the global message pool.
     */
    public final android.os.Message obtainMessage(int what, int arg1, int arg2) {
        return android.os.Message.obtain(this, what, arg1, arg2);
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what, obj, arg1,and arg2 values on the
     * returned Message.
     *
     * @param what
     * 		Value to assign to the returned Message.what field.
     * @param arg1
     * 		Value to assign to the returned Message.arg1 field.
     * @param arg2
     * 		Value to assign to the returned Message.arg2 field.
     * @param obj
     * 		Value to assign to the returned Message.obj field.
     * @return A Message from the global message pool.
     */
    public final android.os.Message obtainMessage(int what, int arg1, int arg2, java.lang.Object obj) {
        return android.os.Message.obtain(this, what, arg1, arg2, obj);
    }

    /**
     * Causes the Runnable r to be added to the message queue.
     * The runnable will be run on the thread to which this handler is
     * attached.
     *
     * @param r
     * 		The Runnable that will be executed.
     * @return Returns true if the Runnable was successfully placed in to the
    message queue.  Returns false on failure, usually because the
    looper processing the message queue is exiting.
     */
    public final boolean post(java.lang.Runnable r) {
        return sendMessageDelayed(android.os.Handler.getPostMessage(r), 0);
    }

    /**
     * Causes the Runnable r to be added to the message queue, to be run
     * at a specific time given by <var>uptimeMillis</var>.
     * <b>The time-base is {@link android.os.SystemClock#uptimeMillis}.</b>
     * Time spent in deep sleep will add an additional delay to execution.
     * The runnable will be run on the thread to which this handler is attached.
     *
     * @param r
     * 		The Runnable that will be executed.
     * @param uptimeMillis
     * 		The absolute time at which the callback should run,
     * 		using the {@link android.os.SystemClock#uptimeMillis} time-base.
     * @return Returns true if the Runnable was successfully placed in to the
    message queue.  Returns false on failure, usually because the
    looper processing the message queue is exiting.  Note that a
    result of true does not mean the Runnable will be processed -- if
    the looper is quit before the delivery time of the message
    occurs then the message will be dropped.
     */
    public final boolean postAtTime(java.lang.Runnable r, long uptimeMillis) {
        return sendMessageAtTime(android.os.Handler.getPostMessage(r), uptimeMillis);
    }

    /**
     * Causes the Runnable r to be added to the message queue, to be run
     * at a specific time given by <var>uptimeMillis</var>.
     * <b>The time-base is {@link android.os.SystemClock#uptimeMillis}.</b>
     * Time spent in deep sleep will add an additional delay to execution.
     * The runnable will be run on the thread to which this handler is attached.
     *
     * @param r
     * 		The Runnable that will be executed.
     * @param uptimeMillis
     * 		The absolute time at which the callback should run,
     * 		using the {@link android.os.SystemClock#uptimeMillis} time-base.
     * @return Returns true if the Runnable was successfully placed in to the
    message queue.  Returns false on failure, usually because the
    looper processing the message queue is exiting.  Note that a
    result of true does not mean the Runnable will be processed -- if
    the looper is quit before the delivery time of the message
    occurs then the message will be dropped.
     * @see android.os.SystemClock#uptimeMillis
     */
    public final boolean postAtTime(java.lang.Runnable r, java.lang.Object token, long uptimeMillis) {
        return sendMessageAtTime(android.os.Handler.getPostMessage(r, token), uptimeMillis);
    }

    /**
     * Causes the Runnable r to be added to the message queue, to be run
     * after the specified amount of time elapses.
     * The runnable will be run on the thread to which this handler
     * is attached.
     * <b>The time-base is {@link android.os.SystemClock#uptimeMillis}.</b>
     * Time spent in deep sleep will add an additional delay to execution.
     *
     * @param r
     * 		The Runnable that will be executed.
     * @param delayMillis
     * 		The delay (in milliseconds) until the Runnable
     * 		will be executed.
     * @return Returns true if the Runnable was successfully placed in to the
    message queue.  Returns false on failure, usually because the
    looper processing the message queue is exiting.  Note that a
    result of true does not mean the Runnable will be processed --
    if the looper is quit before the delivery time of the message
    occurs then the message will be dropped.
     */
    public final boolean postDelayed(java.lang.Runnable r, long delayMillis) {
        return sendMessageDelayed(android.os.Handler.getPostMessage(r), delayMillis);
    }

    /**
     * Posts a message to an object that implements Runnable.
     * Causes the Runnable r to executed on the next iteration through the
     * message queue. The runnable will be run on the thread to which this
     * handler is attached.
     * <b>This method is only for use in very special circumstances -- it
     * can easily starve the message queue, cause ordering problems, or have
     * other unexpected side-effects.</b>
     *
     * @param r
     * 		The Runnable that will be executed.
     * @return Returns true if the message was successfully placed in to the
    message queue.  Returns false on failure, usually because the
    looper processing the message queue is exiting.
     */
    public final boolean postAtFrontOfQueue(java.lang.Runnable r) {
        return sendMessageAtFrontOfQueue(android.os.Handler.getPostMessage(r));
    }

    /**
     * Runs the specified task synchronously.
     * <p>
     * If the current thread is the same as the handler thread, then the runnable
     * runs immediately without being enqueued.  Otherwise, posts the runnable
     * to the handler and waits for it to complete before returning.
     * </p><p>
     * This method is dangerous!  Improper use can result in deadlocks.
     * Never call this method while any locks are held or use it in a
     * possibly re-entrant manner.
     * </p><p>
     * This method is occasionally useful in situations where a background thread
     * must synchronously await completion of a task that must run on the
     * handler's thread.  However, this problem is often a symptom of bad design.
     * Consider improving the design (if possible) before resorting to this method.
     * </p><p>
     * One example of where you might want to use this method is when you just
     * set up a Handler thread and need to perform some initialization steps on
     * it before continuing execution.
     * </p><p>
     * If timeout occurs then this method returns <code>false</code> but the runnable
     * will remain posted on the handler and may already be in progress or
     * complete at a later time.
     * </p><p>
     * When using this method, be sure to use {@link Looper#quitSafely} when
     * quitting the looper.  Otherwise {@link #runWithScissors} may hang indefinitely.
     * (TODO: We should fix this by making MessageQueue aware of blocking runnables.)
     * </p>
     *
     * @param r
     * 		The Runnable that will be executed synchronously.
     * @param timeout
     * 		The timeout in milliseconds, or 0 to wait indefinitely.
     * @return Returns true if the Runnable was successfully executed.
    Returns false on failure, usually because the
    looper processing the message queue is exiting.
     * @unknown This method is prone to abuse and should probably not be in the API.
    If we ever do make it part of the API, we might want to rename it to something
    less funny like runUnsafe().
     */
    public final boolean runWithScissors(final java.lang.Runnable r, long timeout) {
        if (r == null) {
            throw new java.lang.IllegalArgumentException("runnable must not be null");
        }
        if (timeout < 0) {
            throw new java.lang.IllegalArgumentException("timeout must be non-negative");
        }
        if (android.os.Looper.myLooper() == mLooper) {
            r.run();
            return true;
        }
        android.os.Handler.BlockingRunnable br = new android.os.Handler.BlockingRunnable(r);
        return br.postAndWait(this, timeout);
    }

    /**
     * Remove any pending posts of Runnable r that are in the message queue.
     */
    public final void removeCallbacks(java.lang.Runnable r) {
        mQueue.removeMessages(this, r, null);
    }

    /**
     * Remove any pending posts of Runnable <var>r</var> with Object
     * <var>token</var> that are in the message queue.  If <var>token</var> is null,
     * all callbacks will be removed.
     */
    public final void removeCallbacks(java.lang.Runnable r, java.lang.Object token) {
        mQueue.removeMessages(this, r, token);
    }

    /**
     * Pushes a message onto the end of the message queue after all pending messages
     * before the current time. It will be received in {@link #handleMessage},
     * in the thread attached to this handler.
     *
     * @return Returns true if the message was successfully placed in to the
    message queue.  Returns false on failure, usually because the
    looper processing the message queue is exiting.
     */
    public final boolean sendMessage(android.os.Message msg) {
        return sendMessageDelayed(msg, 0);
    }

    /**
     * Sends a Message containing only the what value.
     *
     * @return Returns true if the message was successfully placed in to the
    message queue.  Returns false on failure, usually because the
    looper processing the message queue is exiting.
     */
    public final boolean sendEmptyMessage(int what) {
        return sendEmptyMessageDelayed(what, 0);
    }

    /**
     * Sends a Message containing only the what value, to be delivered
     * after the specified amount of time elapses.
     *
     * @see #sendMessageDelayed(android.os.Message, long)
     * @return Returns true if the message was successfully placed in to the
    message queue.  Returns false on failure, usually because the
    looper processing the message queue is exiting.
     */
    public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        android.os.Message msg = android.os.Message.obtain();
        msg.what = what;
        return sendMessageDelayed(msg, delayMillis);
    }

    /**
     * Sends a Message containing only the what value, to be delivered
     * at a specific time.
     *
     * @see #sendMessageAtTime(android.os.Message, long)
     * @return Returns true if the message was successfully placed in to the
    message queue.  Returns false on failure, usually because the
    looper processing the message queue is exiting.
     */
    public final boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        android.os.Message msg = android.os.Message.obtain();
        msg.what = what;
        return sendMessageAtTime(msg, uptimeMillis);
    }

    /**
     * Enqueue a message into the message queue after all pending messages
     * before (current time + delayMillis). You will receive it in
     * {@link #handleMessage}, in the thread attached to this handler.
     *
     * @return Returns true if the message was successfully placed in to the
    message queue.  Returns false on failure, usually because the
    looper processing the message queue is exiting.  Note that a
    result of true does not mean the message will be processed -- if
    the looper is quit before the delivery time of the message
    occurs then the message will be dropped.
     */
    public final boolean sendMessageDelayed(android.os.Message msg, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return sendMessageAtTime(msg, android.os.SystemClock.uptimeMillis() + delayMillis);
    }

    /**
     * Enqueue a message into the message queue after all pending messages
     * before the absolute time (in milliseconds) <var>uptimeMillis</var>.
     * <b>The time-base is {@link android.os.SystemClock#uptimeMillis}.</b>
     * Time spent in deep sleep will add an additional delay to execution.
     * You will receive it in {@link #handleMessage}, in the thread attached
     * to this handler.
     *
     * @param uptimeMillis
     * 		The absolute time at which the message should be
     * 		delivered, using the
     * 		{@link android.os.SystemClock#uptimeMillis} time-base.
     * @return Returns true if the message was successfully placed in to the
    message queue.  Returns false on failure, usually because the
    looper processing the message queue is exiting.  Note that a
    result of true does not mean the message will be processed -- if
    the looper is quit before the delivery time of the message
    occurs then the message will be dropped.
     */
    public boolean sendMessageAtTime(android.os.Message msg, long uptimeMillis) {
        android.os.MessageQueue queue = mQueue;
        if (queue == null) {
            java.lang.RuntimeException e = new java.lang.RuntimeException(this + " sendMessageAtTime() called with no mQueue");
            android.util.Log.w("Looper", e.getMessage(), e);
            return false;
        }
        return enqueueMessage(queue, msg, uptimeMillis);
    }

    /**
     * Enqueue a message at the front of the message queue, to be processed on
     * the next iteration of the message loop.  You will receive it in
     * {@link #handleMessage}, in the thread attached to this handler.
     * <b>This method is only for use in very special circumstances -- it
     * can easily starve the message queue, cause ordering problems, or have
     * other unexpected side-effects.</b>
     *
     * @return Returns true if the message was successfully placed in to the
    message queue.  Returns false on failure, usually because the
    looper processing the message queue is exiting.
     */
    public final boolean sendMessageAtFrontOfQueue(android.os.Message msg) {
        android.os.MessageQueue queue = mQueue;
        if (queue == null) {
            java.lang.RuntimeException e = new java.lang.RuntimeException(this + " sendMessageAtTime() called with no mQueue");
            android.util.Log.w("Looper", e.getMessage(), e);
            return false;
        }
        return enqueueMessage(queue, msg, 0);
    }

    private boolean enqueueMessage(android.os.MessageQueue queue, android.os.Message msg, long uptimeMillis) {
        msg.target = this;
        if (mAsynchronous) {
            msg.setAsynchronous(true);
        }
        return queue.enqueueMessage(msg, uptimeMillis);
    }

    /**
     * Remove any pending posts of messages with code 'what' that are in the
     * message queue.
     */
    public final void removeMessages(int what) {
        mQueue.removeMessages(this, what, null);
    }

    /**
     * Remove any pending posts of messages with code 'what' and whose obj is
     * 'object' that are in the message queue.  If <var>object</var> is null,
     * all messages will be removed.
     */
    public final void removeMessages(int what, java.lang.Object object) {
        mQueue.removeMessages(this, what, object);
    }

    /**
     * Remove any pending posts of callbacks and sent messages whose
     * <var>obj</var> is <var>token</var>.  If <var>token</var> is null,
     * all callbacks and messages will be removed.
     */
    public final void removeCallbacksAndMessages(java.lang.Object token) {
        mQueue.removeCallbacksAndMessages(this, token);
    }

    /**
     * Check if there are any pending posts of messages with code 'what' in
     * the message queue.
     */
    public final boolean hasMessages(int what) {
        return mQueue.hasMessages(this, what, null);
    }

    /**
     * Check if there are any pending posts of messages with code 'what' and
     * whose obj is 'object' in the message queue.
     */
    public final boolean hasMessages(int what, java.lang.Object object) {
        return mQueue.hasMessages(this, what, object);
    }

    /**
     * Check if there are any pending posts of messages with callback r in
     * the message queue.
     *
     * @unknown 
     */
    public final boolean hasCallbacks(java.lang.Runnable r) {
        return mQueue.hasMessages(this, r, null);
    }

    // if we can get rid of this method, the handler need not remember its loop
    // we could instead export a getMessageQueue() method...
    public final android.os.Looper getLooper() {
        return mLooper;
    }

    public final void dump(android.util.Printer pw, java.lang.String prefix) {
        pw.println(((prefix + this) + " @ ") + android.os.SystemClock.uptimeMillis());
        if (mLooper == null) {
            pw.println(prefix + "looper uninitialized");
        } else {
            mLooper.dump(pw, prefix + "  ");
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("Handler (" + getClass().getName()) + ") {") + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + "}";
    }

    final android.os.IMessenger getIMessenger() {
        synchronized(mQueue) {
            if (mMessenger != null) {
                return mMessenger;
            }
            mMessenger = new android.os.Handler.MessengerImpl();
            return mMessenger;
        }
    }

    private final class MessengerImpl extends android.os.IMessenger.Stub {
        public void send(android.os.Message msg) {
            msg.sendingUid = android.os.Binder.getCallingUid();
            android.os.Handler.this.sendMessage(msg);
        }
    }

    private static android.os.Message getPostMessage(java.lang.Runnable r) {
        android.os.Message m = android.os.Message.obtain();
        m.callback = r;
        return m;
    }

    private static android.os.Message getPostMessage(java.lang.Runnable r, java.lang.Object token) {
        android.os.Message m = android.os.Message.obtain();
        m.obj = token;
        m.callback = r;
        return m;
    }

    private static void handleCallback(android.os.Message message) {
        message.callback.run();
    }

    final android.os.Looper mLooper;

    final android.os.MessageQueue mQueue;

    final android.os.Handler.Callback mCallback;

    final boolean mAsynchronous;

    android.os.IMessenger mMessenger;

    private static final class BlockingRunnable implements java.lang.Runnable {
        private final java.lang.Runnable mTask;

        private boolean mDone;

        public BlockingRunnable(java.lang.Runnable task) {
            mTask = task;
        }

        @java.lang.Override
        public void run() {
            try {
                mTask.run();
            } finally {
                synchronized(this) {
                    mDone = true;
                    notifyAll();
                }
            }
        }

        public boolean postAndWait(android.os.Handler handler, long timeout) {
            if (!handler.post(this)) {
                return false;
            }
            synchronized(this) {
                if (timeout > 0) {
                    final long expirationTime = android.os.SystemClock.uptimeMillis() + timeout;
                    while (!mDone) {
                        long delay = expirationTime - android.os.SystemClock.uptimeMillis();
                        if (delay <= 0) {
                            return false;// timeout

                        }
                        try {
                            wait(delay);
                        } catch (java.lang.InterruptedException ex) {
                        }
                    } 
                } else {
                    while (!mDone) {
                        try {
                            wait();
                        } catch (java.lang.InterruptedException ex) {
                        }
                    } 
                }
            }
            return true;
        }
    }
}

