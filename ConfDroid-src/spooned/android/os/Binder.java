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
 * Base class for a remotable object, the core part of a lightweight
 * remote procedure call mechanism defined by {@link IBinder}.
 * This class is an implementation of IBinder that provides
 * standard local implementation of such an object.
 *
 * <p>Most developers will not implement this class directly, instead using the
 * <a href="{@docRoot }guide/components/aidl.html">aidl</a> tool to describe the desired
 * interface, having it generate the appropriate Binder subclass.  You can,
 * however, derive directly from Binder to implement your own custom RPC
 * protocol or simply instantiate a raw Binder object directly to use as a
 * token that can be shared across processes.
 *
 * <p>This class is just a basic IPC primitive; it has no impact on an application's
 * lifecycle, and is valid only as long as the process that created it continues to run.
 * To use this correctly, you must be doing so within the context of a top-level
 * application component (a {@link android.app.Service}, {@link android.app.Activity},
 * or {@link android.content.ContentProvider}) that lets the system know your process
 * should remain running.</p>
 *
 * <p>You must keep in mind the situations in which your process
 * could go away, and thus require that you later re-create a new Binder and re-attach
 * it when the process starts again.  For example, if you are using this within an
 * {@link android.app.Activity}, your activity's process may be killed any time the
 * activity is not started; if the activity is later re-created you will need to
 * create a new Binder and hand it back to the correct place again; you need to be
 * aware that your process may be started for another reason (for example to receive
 * a broadcast) that will not involve re-creating the activity and thus run its code
 * to create a new Binder.</p>
 *
 * @see IBinder
 */
public class Binder implements android.os.IBinder {
    /* Set this flag to true to detect anonymous, local or member classes
    that extend this Binder class and that are not static. These kind
    of classes can potentially create leaks.
     */
    private static final boolean FIND_POTENTIAL_LEAKS = false;

    private static final boolean CHECK_PARCEL_SIZE = false;

    static final java.lang.String TAG = "Binder";

    /**
     *
     *
     * @unknown 
     */
    public static boolean LOG_RUNTIME_EXCEPTION = false;// DO NOT SUBMIT WITH TRUE


    /**
     * Control whether dump() calls are allowed.
     */
    private static java.lang.String sDumpDisabled = null;

    /**
     * Global transaction tracker instance for this process.
     */
    private static android.os.TransactionTracker sTransactionTracker = null;

    // Transaction tracking code.
    /**
     * Flag indicating whether we should be tracing transact calls.
     */
    private static boolean sTracingEnabled = false;

    /**
     * Enable Binder IPC tracing.
     *
     * @unknown 
     */
    public static void enableTracing() {
        android.os.Binder.sTracingEnabled = true;
    }

    /**
     * Disable Binder IPC tracing.
     *
     * @unknown 
     */
    public static void disableTracing() {
        android.os.Binder.sTracingEnabled = false;
    }

    /**
     * Check if binder transaction tracing is enabled.
     *
     * @unknown 
     */
    public static boolean isTracingEnabled() {
        return android.os.Binder.sTracingEnabled;
    }

    /**
     * Get the binder transaction tracker for this process.
     *
     * @unknown 
     */
    public static synchronized android.os.TransactionTracker getTransactionTracker() {
        if (android.os.Binder.sTransactionTracker == null)
            android.os.Binder.sTransactionTracker = new android.os.TransactionTracker();

        return android.os.Binder.sTransactionTracker;
    }

    /* mObject is used by native code, do not remove or rename */
    private long mObject;

    private android.os.IInterface mOwner;

    private java.lang.String mDescriptor;

    /**
     * Return the ID of the process that sent you the current transaction
     * that is being processed.  This pid can be used with higher-level
     * system services to determine its identity and check permissions.
     * If the current thread is not currently executing an incoming transaction,
     * then its own pid is returned.
     */
    public static final native int getCallingPid();

    /**
     * Return the Linux uid assigned to the process that sent you the
     * current transaction that is being processed.  This uid can be used with
     * higher-level system services to determine its identity and check
     * permissions.  If the current thread is not currently executing an
     * incoming transaction, then its own uid is returned.
     */
    public static final native int getCallingUid();

    /**
     * Return the UserHandle assigned to the process that sent you the
     * current transaction that is being processed.  This is the user
     * of the caller.  It is distinct from {@link #getCallingUid()} in that a
     * particular user will have multiple distinct apps running under it each
     * with their own uid.  If the current thread is not currently executing an
     * incoming transaction, then its own UserHandle is returned.
     */
    public static final android.os.UserHandle getCallingUserHandle() {
        return android.os.UserHandle.of(android.os.UserHandle.getUserId(android.os.Binder.getCallingUid()));
    }

    /**
     * Reset the identity of the incoming IPC on the current thread.  This can
     * be useful if, while handling an incoming call, you will be calling
     * on interfaces of other objects that may be local to your process and
     * need to do permission checks on the calls coming into them (so they
     * will check the permission of your own local process, and not whatever
     * process originally called you).
     *
     * @return Returns an opaque token that can be used to restore the
    original calling identity by passing it to
    {@link #restoreCallingIdentity(long)}.
     * @see #getCallingPid()
     * @see #getCallingUid()
     * @see #restoreCallingIdentity(long)
     */
    public static final native long clearCallingIdentity();

    /**
     * Restore the identity of the incoming IPC on the current thread
     * back to a previously identity that was returned by {@link #clearCallingIdentity}.
     *
     * @param token
     * 		The opaque token that was previously returned by
     * 		{@link #clearCallingIdentity}.
     * @see #clearCallingIdentity
     */
    public static final native void restoreCallingIdentity(long token);

    /**
     * Sets the native thread-local StrictMode policy mask.
     *
     * <p>The StrictMode settings are kept in two places: a Java-level
     * threadlocal for libcore/Dalvik, and a native threadlocal (set
     * here) for propagation via Binder calls.  This is a little
     * unfortunate, but necessary to break otherwise more unfortunate
     * dependencies either of Dalvik on Android, or Android
     * native-only code on Dalvik.
     *
     * @see StrictMode
     * @unknown 
     */
    public static final native void setThreadStrictModePolicy(int policyMask);

    /**
     * Gets the current native thread-local StrictMode policy mask.
     *
     * @see #setThreadStrictModePolicy
     * @unknown 
     */
    public static final native int getThreadStrictModePolicy();

    /**
     * Flush any Binder commands pending in the current thread to the kernel
     * driver.  This can be
     * useful to call before performing an operation that may block for a long
     * time, to ensure that any pending object references have been released
     * in order to prevent the process from holding on to objects longer than
     * it needs to.
     */
    public static final native void flushPendingCommands();

    /**
     * Add the calling thread to the IPC thread pool.  This function does
     * not return until the current process is exiting.
     */
    public static final native void joinThreadPool();

    /**
     * Returns true if the specified interface is a proxy.
     *
     * @unknown 
     */
    public static final boolean isProxy(android.os.IInterface iface) {
        return iface.asBinder() != iface;
    }

    /**
     * Call blocks until the number of executing binder threads is less
     * than the maximum number of binder threads allowed for this process.
     *
     * @unknown 
     */
    public static final native void blockUntilThreadAvailable();

    /**
     * Default constructor initializes the object.
     */
    public Binder() {
        init();
        if (android.os.Binder.FIND_POTENTIAL_LEAKS) {
            final java.lang.Class<? extends android.os.Binder> klass = getClass();
            if (((klass.isAnonymousClass() || klass.isMemberClass()) || klass.isLocalClass()) && ((klass.getModifiers() & java.lang.reflect.Modifier.STATIC) == 0)) {
                android.util.Log.w(android.os.Binder.TAG, "The following Binder class should be static or leaks might occur: " + klass.getCanonicalName());
            }
        }
    }

    /**
     * Convenience method for associating a specific interface with the Binder.
     * After calling, queryLocalInterface() will be implemented for you
     * to return the given owner IInterface when the corresponding
     * descriptor is requested.
     */
    public void attachInterface(android.os.IInterface owner, java.lang.String descriptor) {
        mOwner = owner;
        mDescriptor = descriptor;
    }

    /**
     * Default implementation returns an empty interface name.
     */
    public java.lang.String getInterfaceDescriptor() {
        return mDescriptor;
    }

    /**
     * Default implementation always returns true -- if you got here,
     * the object is alive.
     */
    public boolean pingBinder() {
        return true;
    }

    /**
     * {@inheritDoc }
     *
     * Note that if you're calling on a local binder, this always returns true
     * because your process is alive if you're calling it.
     */
    public boolean isBinderAlive() {
        return true;
    }

    /**
     * Use information supplied to attachInterface() to return the
     * associated IInterface if it matches the requested
     * descriptor.
     */
    public android.os.IInterface queryLocalInterface(java.lang.String descriptor) {
        if (mDescriptor.equals(descriptor)) {
            return mOwner;
        }
        return null;
    }

    /**
     * Control disabling of dump calls in this process.  This is used by the system
     * process watchdog to disable incoming dump calls while it has detecting the system
     * is hung and is reporting that back to the activity controller.  This is to
     * prevent the controller from getting hung up on bug reports at this point.
     *
     * @unknown 
     * @param msg
     * 		The message to show instead of the dump; if null, dumps are
     * 		re-enabled.
     */
    public static void setDumpDisabled(java.lang.String msg) {
        synchronized(android.os.Binder.class) {
            android.os.Binder.sDumpDisabled = msg;
        }
    }

    /**
     * Default implementation is a stub that returns false.  You will want
     * to override this to do the appropriate unmarshalling of transactions.
     *
     * <p>If you want to call this, call transact().
     */
    protected boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        if (code == android.os.IBinder.INTERFACE_TRANSACTION) {
            reply.writeString(getInterfaceDescriptor());
            return true;
        } else
            if (code == android.os.IBinder.DUMP_TRANSACTION) {
                android.os.ParcelFileDescriptor fd = data.readFileDescriptor();
                java.lang.String[] args = data.readStringArray();
                if (fd != null) {
                    try {
                        dump(fd.getFileDescriptor(), args);
                    } finally {
                        libcore.io.IoUtils.closeQuietly(fd);
                    }
                }
                // Write the StrictMode header.
                if (reply != null) {
                    reply.writeNoException();
                } else {
                    android.os.StrictMode.clearGatheredViolations();
                }
                return true;
            } else
                if (code == android.os.IBinder.SHELL_COMMAND_TRANSACTION) {
                    android.os.ParcelFileDescriptor in = data.readFileDescriptor();
                    android.os.ParcelFileDescriptor out = data.readFileDescriptor();
                    android.os.ParcelFileDescriptor err = data.readFileDescriptor();
                    java.lang.String[] args = data.readStringArray();
                    android.os.ResultReceiver resultReceiver = android.os.ResultReceiver.CREATOR.createFromParcel(data);
                    try {
                        if (out != null) {
                            shellCommand(in != null ? in.getFileDescriptor() : null, out.getFileDescriptor(), err != null ? err.getFileDescriptor() : out.getFileDescriptor(), args, resultReceiver);
                        }
                    } finally {
                        libcore.io.IoUtils.closeQuietly(in);
                        libcore.io.IoUtils.closeQuietly(out);
                        libcore.io.IoUtils.closeQuietly(err);
                        // Write the StrictMode header.
                        if (reply != null) {
                            reply.writeNoException();
                        } else {
                            android.os.StrictMode.clearGatheredViolations();
                        }
                    }
                    return true;
                }


        return false;
    }

    /**
     * Implemented to call the more convenient version
     * {@link #dump(FileDescriptor, PrintWriter, String[])}.
     */
    public void dump(java.io.FileDescriptor fd, java.lang.String[] args) {
        java.io.FileOutputStream fout = new java.io.FileOutputStream(fd);
        java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(fout);
        try {
            doDump(fd, pw, args);
        } finally {
            pw.flush();
        }
    }

    void doDump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        final java.lang.String disabled;
        synchronized(android.os.Binder.class) {
            disabled = android.os.Binder.sDumpDisabled;
        }
        if (disabled == null) {
            try {
                dump(fd, pw, args);
            } catch (java.lang.SecurityException e) {
                pw.println("Security exception: " + e.getMessage());
                throw e;
            } catch (java.lang.Throwable e) {
                // Unlike usual calls, in this case if an exception gets thrown
                // back to us we want to print it back in to the dump data, since
                // that is where the caller expects all interesting information to
                // go.
                pw.println();
                pw.println("Exception occurred while dumping:");
                e.printStackTrace(pw);
            }
        } else {
            pw.println(android.os.Binder.sDumpDisabled);
        }
    }

    /**
     * Like {@link #dump(FileDescriptor, String[])}, but ensures the target
     * executes asynchronously.
     */
    public void dumpAsync(final java.io.FileDescriptor fd, final java.lang.String[] args) {
        final java.io.FileOutputStream fout = new java.io.FileOutputStream(fd);
        final java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(fout);
        java.lang.Thread thr = new java.lang.Thread("Binder.dumpAsync") {
            public void run() {
                try {
                    dump(fd, pw, args);
                } finally {
                    pw.flush();
                }
            }
        };
        thr.start();
    }

    /**
     * Print the object's state into the given stream.
     *
     * @param fd
     * 		The raw file descriptor that the dump is being sent to.
     * @param fout
     * 		The file to which you should dump your state.  This will be
     * 		closed for you after you return.
     * @param args
     * 		additional arguments to the dump request.
     */
    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter fout, java.lang.String[] args) {
    }

    /**
     *
     *
     * @param in
     * 		The raw file descriptor that an input data stream can be read from.
     * @param out
     * 		The raw file descriptor that normal command messages should be written to.
     * @param err
     * 		The raw file descriptor that command error messages should be written to.
     * @param args
     * 		Command-line arguments.
     * @param resultReceiver
     * 		Called when the command has finished executing, with the result code.
     * @throws RemoteException
     * 		
     * @unknown 
     */
    public void shellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ResultReceiver resultReceiver) throws android.os.RemoteException {
        onShellCommand(in, out, err, args, resultReceiver);
    }

    /**
     * Handle a call to {@link #shellCommand}.  The default implementation simply prints
     * an error message.  Override and replace with your own.
     * <p class="caution">Note: no permission checking is done before calling this method; you must
     * apply any security checks as appropriate for the command being executed.
     * Consider using {@link ShellCommand} to help in the implementation.</p>
     *
     * @unknown 
     */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ResultReceiver resultReceiver) throws android.os.RemoteException {
        java.io.FileOutputStream fout = new java.io.FileOutputStream(err != null ? err : out);
        java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(fout);
        pw.println("No shell command implementation.");
        pw.flush();
        resultReceiver.send(0, null);
    }

    /**
     * Default implementation rewinds the parcels and calls onTransact.  On
     * the remote side, transact calls into the binder to do the IPC.
     */
    public final boolean transact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        if (false)
            android.util.Log.v("Binder", (("Transact: " + code) + " to ") + this);

        if (data != null) {
            data.setDataPosition(0);
        }
        boolean r = onTransact(code, data, reply, flags);
        if (reply != null) {
            reply.setDataPosition(0);
        }
        return r;
    }

    /**
     * Local implementation is a no-op.
     */
    public void linkToDeath(android.os.IBinder.DeathRecipient recipient, int flags) {
    }

    /**
     * Local implementation is a no-op.
     */
    public boolean unlinkToDeath(android.os.IBinder.DeathRecipient recipient, int flags) {
        return true;
    }

    protected void finalize() throws java.lang.Throwable {
        try {
            destroy();
        } finally {
            super.finalize();
        }
    }

    static void checkParcel(android.os.IBinder obj, int code, android.os.Parcel parcel, java.lang.String msg) {
        if (android.os.Binder.CHECK_PARCEL_SIZE && (parcel.dataSize() >= (800 * 1024))) {
            // Trying to send > 800k, this is way too much
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(msg);
            sb.append(": on ");
            sb.append(obj);
            sb.append(" calling ");
            sb.append(code);
            sb.append(" size ");
            sb.append(parcel.dataSize());
            sb.append(" (data: ");
            parcel.setDataPosition(0);
            sb.append(parcel.readInt());
            sb.append(", ");
            sb.append(parcel.readInt());
            sb.append(", ");
            sb.append(parcel.readInt());
            sb.append(")");
            android.util.Slog.wtfStack(android.os.Binder.TAG, sb.toString());
        }
    }

    private final native void init();

    private final native void destroy();

    // Entry point from android_util_Binder.cpp's onTransact
    private boolean execTransact(int code, long dataObj, long replyObj, int flags) {
        android.os.Parcel data = android.os.Parcel.obtain(dataObj);
        android.os.Parcel reply = android.os.Parcel.obtain(replyObj);
        // theoretically, we should call transact, which will call onTransact,
        // but all that does is rewind it, and we just got these from an IPC,
        // so we'll just call it directly.
        boolean res;
        // Log any exceptions as warnings, don't silently suppress them.
        // If the call was FLAG_ONEWAY then these exceptions disappear into the ether.
        try {
            res = onTransact(code, data, reply, flags);
        } catch (android.os.RemoteException | java.lang.RuntimeException e) {
            if (android.os.Binder.LOG_RUNTIME_EXCEPTION) {
                android.util.Log.w(android.os.Binder.TAG, "Caught a RuntimeException from the binder stub implementation.", e);
            }
            if ((flags & android.os.IBinder.FLAG_ONEWAY) != 0) {
                if (e instanceof android.os.RemoteException) {
                    android.util.Log.w(android.os.Binder.TAG, "Binder call failed.", e);
                } else {
                    android.util.Log.w(android.os.Binder.TAG, "Caught a RuntimeException from the binder stub implementation.", e);
                }
            } else {
                reply.setDataPosition(0);
                reply.writeException(e);
            }
            res = true;
        } catch (java.lang.OutOfMemoryError e) {
            // Unconditionally log this, since this is generally unrecoverable.
            android.util.Log.e(android.os.Binder.TAG, "Caught an OutOfMemoryError from the binder stub implementation.", e);
            java.lang.RuntimeException re = new java.lang.RuntimeException("Out of memory", e);
            reply.setDataPosition(0);
            reply.writeException(re);
            res = true;
        }
        android.os.Binder.checkParcel(this, code, reply, "Unreasonably large binder reply buffer");
        reply.recycle();
        data.recycle();
        // Just in case -- we are done with the IPC, so there should be no more strict
        // mode violations that have gathered for this thread.  Either they have been
        // parceled and are now in transport off to the caller, or we are returning back
        // to the main transaction loop to wait for another incoming transaction.  Either
        // way, strict mode begone!
        android.os.StrictMode.clearGatheredViolations();
        return res;
    }
}

