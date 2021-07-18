package android.database;


final class BulkCursorProxy implements android.database.IBulkCursor {
    private android.os.IBinder mRemote;

    private android.os.Bundle mExtras;

    public BulkCursorProxy(android.os.IBinder remote) {
        mRemote = remote;
        mExtras = null;
    }

    public android.os.IBinder asBinder() {
        return mRemote;
    }

    public android.database.CursorWindow getWindow(int position) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.database.IBulkCursor.descriptor);
            data.writeInt(position);
            mRemote.transact(android.database.IBulkCursor.GET_CURSOR_WINDOW_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
            android.database.CursorWindow window = null;
            if (reply.readInt() == 1) {
                window = android.database.CursorWindow.newFromParcel(reply);
            }
            return window;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void onMove(int position) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.database.IBulkCursor.descriptor);
            data.writeInt(position);
            mRemote.transact(android.database.IBulkCursor.ON_MOVE_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void deactivate() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.database.IBulkCursor.descriptor);
            mRemote.transact(android.database.IBulkCursor.DEACTIVATE_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void close() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.database.IBulkCursor.descriptor);
            mRemote.transact(android.database.IBulkCursor.CLOSE_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public int requery(android.database.IContentObserver observer) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.database.IBulkCursor.descriptor);
            data.writeStrongInterface(observer);
            boolean result = mRemote.transact(android.database.IBulkCursor.REQUERY_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
            int count;
            if (!result) {
                count = -1;
            } else {
                count = reply.readInt();
                mExtras = reply.readBundle();
            }
            return count;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public android.os.Bundle getExtras() throws android.os.RemoteException {
        if (mExtras == null) {
            android.os.Parcel data = android.os.Parcel.obtain();
            android.os.Parcel reply = android.os.Parcel.obtain();
            try {
                data.writeInterfaceToken(android.database.IBulkCursor.descriptor);
                mRemote.transact(android.database.IBulkCursor.GET_EXTRAS_TRANSACTION, data, reply, 0);
                android.database.DatabaseUtils.readExceptionFromParcel(reply);
                mExtras = reply.readBundle();
            } finally {
                data.recycle();
                reply.recycle();
            }
        }
        return mExtras;
    }

    public android.os.Bundle respond(android.os.Bundle extras) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.database.IBulkCursor.descriptor);
            data.writeBundle(extras);
            mRemote.transact(android.database.IBulkCursor.RESPOND_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
            android.os.Bundle returnExtras = reply.readBundle();
            return returnExtras;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }
}

