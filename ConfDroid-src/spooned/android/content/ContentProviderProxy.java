package android.content;


final class ContentProviderProxy implements android.content.IContentProvider {
    public ContentProviderProxy(android.os.IBinder remote) {
        mRemote = remote;
    }

    @java.lang.Override
    public android.os.IBinder asBinder() {
        return mRemote;
    }

    @java.lang.Override
    public android.database.Cursor query(java.lang.String callingPkg, android.net.Uri url, @android.annotation.Nullable
    java.lang.String[] projection, @android.annotation.Nullable
    android.os.Bundle queryArgs, @android.annotation.Nullable
    android.os.ICancellationSignal cancellationSignal) throws android.os.RemoteException {
        android.database.BulkCursorToCursorAdaptor adaptor = new android.database.BulkCursorToCursorAdaptor();
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.content.IContentProvider.descriptor);
            data.writeString(callingPkg);
            url.writeToParcel(data, 0);
            int length = 0;
            if (projection != null) {
                length = projection.length;
            }
            data.writeInt(length);
            for (int i = 0; i < length; i++) {
                data.writeString(projection[i]);
            }
            data.writeBundle(queryArgs);
            data.writeStrongBinder(asBinder());
            data.writeStrongBinder(cancellationSignal != null ? cancellationSignal.asBinder() : null);
            mRemote.transact(android.content.IContentProvider.QUERY_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
            if (reply.readInt() != 0) {
                android.database.BulkCursorDescriptor d = BulkCursorDescriptor.CREATOR.createFromParcel(reply);
                android.os.Binder.copyAllowBlocking(mRemote, d.cursor != null ? d.cursor.asBinder() : null);
                adaptor.initialize(d);
            } else {
                adaptor.close();
                adaptor = null;
            }
            return adaptor;
        } catch (android.os.RemoteException ex) {
            adaptor.close();
            throw ex;
        } catch (java.lang.RuntimeException ex) {
            adaptor.close();
            throw ex;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @java.lang.Override
    public java.lang.String getType(android.net.Uri url) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.content.IContentProvider.descriptor);
            url.writeToParcel(data, 0);
            mRemote.transact(android.content.IContentProvider.GET_TYPE_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
            java.lang.String out = reply.readString();
            return out;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @java.lang.Override
    public android.net.Uri insert(java.lang.String callingPkg, android.net.Uri url, android.content.ContentValues values) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.content.IContentProvider.descriptor);
            data.writeString(callingPkg);
            url.writeToParcel(data, 0);
            values.writeToParcel(data, 0);
            mRemote.transact(android.content.IContentProvider.INSERT_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
            android.net.Uri out = Uri.CREATOR.createFromParcel(reply);
            return out;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @java.lang.Override
    public int bulkInsert(java.lang.String callingPkg, android.net.Uri url, android.content.ContentValues[] values) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.content.IContentProvider.descriptor);
            data.writeString(callingPkg);
            url.writeToParcel(data, 0);
            data.writeTypedArray(values, 0);
            mRemote.transact(android.content.IContentProvider.BULK_INSERT_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
            int count = reply.readInt();
            return count;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @java.lang.Override
    public android.content.ContentProviderResult[] applyBatch(java.lang.String callingPkg, java.lang.String authority, java.util.ArrayList<android.content.ContentProviderOperation> operations) throws android.content.OperationApplicationException, android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.content.IContentProvider.descriptor);
            data.writeString(callingPkg);
            data.writeString(authority);
            data.writeInt(operations.size());
            for (android.content.ContentProviderOperation operation : operations) {
                operation.writeToParcel(data, 0);
            }
            mRemote.transact(android.content.IContentProvider.APPLY_BATCH_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionWithOperationApplicationExceptionFromParcel(reply);
            final android.content.ContentProviderResult[] results = reply.createTypedArray(android.content.ContentProviderResult.CREATOR);
            return results;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @java.lang.Override
    public int delete(java.lang.String callingPkg, android.net.Uri url, java.lang.String selection, java.lang.String[] selectionArgs) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.content.IContentProvider.descriptor);
            data.writeString(callingPkg);
            url.writeToParcel(data, 0);
            data.writeString(selection);
            data.writeStringArray(selectionArgs);
            mRemote.transact(android.content.IContentProvider.DELETE_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
            int count = reply.readInt();
            return count;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @java.lang.Override
    public int update(java.lang.String callingPkg, android.net.Uri url, android.content.ContentValues values, java.lang.String selection, java.lang.String[] selectionArgs) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.content.IContentProvider.descriptor);
            data.writeString(callingPkg);
            url.writeToParcel(data, 0);
            values.writeToParcel(data, 0);
            data.writeString(selection);
            data.writeStringArray(selectionArgs);
            mRemote.transact(android.content.IContentProvider.UPDATE_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
            int count = reply.readInt();
            return count;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @java.lang.Override
    public android.os.ParcelFileDescriptor openFile(java.lang.String callingPkg, android.net.Uri url, java.lang.String mode, android.os.ICancellationSignal signal, android.os.IBinder token) throws android.os.RemoteException, java.io.FileNotFoundException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.content.IContentProvider.descriptor);
            data.writeString(callingPkg);
            url.writeToParcel(data, 0);
            data.writeString(mode);
            data.writeStrongBinder(signal != null ? signal.asBinder() : null);
            data.writeStrongBinder(token);
            mRemote.transact(android.content.IContentProvider.OPEN_FILE_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionWithFileNotFoundExceptionFromParcel(reply);
            int has = reply.readInt();
            android.os.ParcelFileDescriptor fd = (has != 0) ? ParcelFileDescriptor.CREATOR.createFromParcel(reply) : null;
            return fd;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @java.lang.Override
    public android.content.res.AssetFileDescriptor openAssetFile(java.lang.String callingPkg, android.net.Uri url, java.lang.String mode, android.os.ICancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.content.IContentProvider.descriptor);
            data.writeString(callingPkg);
            url.writeToParcel(data, 0);
            data.writeString(mode);
            data.writeStrongBinder(signal != null ? signal.asBinder() : null);
            mRemote.transact(android.content.IContentProvider.OPEN_ASSET_FILE_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionWithFileNotFoundExceptionFromParcel(reply);
            int has = reply.readInt();
            android.content.res.AssetFileDescriptor fd = (has != 0) ? this.CREATOR.createFromParcel(reply) : null;
            return fd;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @java.lang.Override
    public android.os.Bundle call(java.lang.String callingPkg, java.lang.String authority, java.lang.String method, java.lang.String request, android.os.Bundle args) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.content.IContentProvider.descriptor);
            data.writeString(callingPkg);
            data.writeString(authority);
            data.writeString(method);
            data.writeString(request);
            data.writeBundle(args);
            mRemote.transact(android.content.IContentProvider.CALL_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
            android.os.Bundle bundle = reply.readBundle();
            return bundle;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @java.lang.Override
    public java.lang.String[] getStreamTypes(android.net.Uri url, java.lang.String mimeTypeFilter) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.content.IContentProvider.descriptor);
            url.writeToParcel(data, 0);
            data.writeString(mimeTypeFilter);
            mRemote.transact(android.content.IContentProvider.GET_STREAM_TYPES_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
            java.lang.String[] out = reply.createStringArray();
            return out;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @java.lang.Override
    public android.content.res.AssetFileDescriptor openTypedAssetFile(java.lang.String callingPkg, android.net.Uri url, java.lang.String mimeType, android.os.Bundle opts, android.os.ICancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.content.IContentProvider.descriptor);
            data.writeString(callingPkg);
            url.writeToParcel(data, 0);
            data.writeString(mimeType);
            data.writeBundle(opts);
            data.writeStrongBinder(signal != null ? signal.asBinder() : null);
            mRemote.transact(android.content.IContentProvider.OPEN_TYPED_ASSET_FILE_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionWithFileNotFoundExceptionFromParcel(reply);
            int has = reply.readInt();
            android.content.res.AssetFileDescriptor fd = (has != 0) ? this.CREATOR.createFromParcel(reply) : null;
            return fd;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @java.lang.Override
    public android.os.ICancellationSignal createCancellationSignal() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.content.IContentProvider.descriptor);
            mRemote.transact(android.content.IContentProvider.CREATE_CANCELATION_SIGNAL_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
            android.os.ICancellationSignal cancellationSignal = ICancellationSignal.Stub.asInterface(reply.readStrongBinder());
            return cancellationSignal;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @java.lang.Override
    public android.net.Uri canonicalize(java.lang.String callingPkg, android.net.Uri url) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.content.IContentProvider.descriptor);
            data.writeString(callingPkg);
            url.writeToParcel(data, 0);
            mRemote.transact(android.content.IContentProvider.CANONICALIZE_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
            android.net.Uri out = Uri.CREATOR.createFromParcel(reply);
            return out;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @java.lang.Override
    public android.net.Uri uncanonicalize(java.lang.String callingPkg, android.net.Uri url) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.content.IContentProvider.descriptor);
            data.writeString(callingPkg);
            url.writeToParcel(data, 0);
            mRemote.transact(android.content.IContentProvider.UNCANONICALIZE_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
            android.net.Uri out = Uri.CREATOR.createFromParcel(reply);
            return out;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @java.lang.Override
    public boolean refresh(java.lang.String callingPkg, android.net.Uri url, android.os.Bundle args, android.os.ICancellationSignal signal) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(android.content.IContentProvider.descriptor);
            data.writeString(callingPkg);
            url.writeToParcel(data, 0);
            data.writeBundle(args);
            data.writeStrongBinder(signal != null ? signal.asBinder() : null);
            mRemote.transact(android.content.IContentProvider.REFRESH_TRANSACTION, data, reply, 0);
            android.database.DatabaseUtils.readExceptionFromParcel(reply);
            int success = reply.readInt();
            return success == 0;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @android.annotation.UnsupportedAppUsage
    private android.os.IBinder mRemote;
}

