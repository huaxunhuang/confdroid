package android.os;


final class BinderProxy implements android.os.IBinder {
    public native boolean pingBinder();

    public native boolean isBinderAlive();

    public android.os.IInterface queryLocalInterface(java.lang.String descriptor) {
        return null;
    }

    public boolean transact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        android.os.Binder.checkParcel(this, code, data, "Unreasonably large binder buffer");
        if (android.os.Binder.isTracingEnabled()) {
            android.os.Binder.getTransactionTracker().addTrace();
        }
        return transactNative(code, data, reply, flags);
    }

    public native java.lang.String getInterfaceDescriptor() throws android.os.RemoteException;

    public native boolean transactNative(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException;

    public native void linkToDeath(android.os.IBinder.DeathRecipient recipient, int flags) throws android.os.RemoteException;

    public native boolean unlinkToDeath(android.os.IBinder.DeathRecipient recipient, int flags);

    public void dump(java.io.FileDescriptor fd, java.lang.String[] args) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeFileDescriptor(fd);
        data.writeStringArray(args);
        try {
            transact(android.os.IBinder.DUMP_TRANSACTION, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void dumpAsync(java.io.FileDescriptor fd, java.lang.String[] args) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeFileDescriptor(fd);
        data.writeStringArray(args);
        try {
            transact(android.os.IBinder.DUMP_TRANSACTION, data, reply, android.os.IBinder.FLAG_ONEWAY);
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void shellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ResultReceiver resultReceiver) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeFileDescriptor(in);
        data.writeFileDescriptor(out);
        data.writeFileDescriptor(err);
        data.writeStringArray(args);
        resultReceiver.writeToParcel(data, 0);
        try {
            transact(android.os.IBinder.SHELL_COMMAND_TRANSACTION, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    BinderProxy() {
        mSelf = new java.lang.ref.WeakReference(this);
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            destroy();
        } finally {
            super.finalize();
        }
    }

    private final native void destroy();

    private static final void sendDeathNotice(android.os.IBinder.DeathRecipient recipient) {
        if (false)
            android.util.Log.v("JavaBinder", "sendDeathNotice to " + recipient);

        try {
            recipient.binderDied();
        } catch (java.lang.RuntimeException exc) {
            android.util.Log.w("BinderNative", "Uncaught exception from death notification", exc);
        }
    }

    private final java.lang.ref.WeakReference mSelf;

    private long mObject;

    private long mOrgue;
}

