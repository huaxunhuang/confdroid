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
package android.content;


/**
 * {@hide }
 */
public abstract class ContentProviderNative extends android.os.Binder implements android.content.IContentProvider {
    public ContentProviderNative() {
        attachInterface(this, android.content.IContentProvider.descriptor);
    }

    /**
     * Cast a Binder object into a content resolver interface, generating
     * a proxy if needed.
     */
    @android.annotation.UnsupportedAppUsage
    public static android.content.IContentProvider asInterface(android.os.IBinder obj) {
        if (obj == null) {
            return null;
        }
        android.content.IContentProvider in = ((android.content.IContentProvider) (obj.queryLocalInterface(android.content.IContentProvider.descriptor)));
        if (in != null) {
            return in;
        }
        return new android.content.ContentProviderProxy(obj);
    }

    /**
     * Gets the name of the content provider.
     * Should probably be part of the {@link IContentProvider} interface.
     *
     * @return The content provider name.
     */
    public abstract java.lang.String getProviderName();

    @java.lang.Override
    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        try {
            switch (code) {
                case android.content.IContentProvider.QUERY_TRANSACTION :
                    {
                        data.enforceInterface(android.content.IContentProvider.descriptor);
                        java.lang.String callingPkg = data.readString();
                        android.net.Uri url = Uri.CREATOR.createFromParcel(data);
                        // String[] projection
                        int num = data.readInt();
                        java.lang.String[] projection = null;
                        if (num > 0) {
                            projection = new java.lang.String[num];
                            for (int i = 0; i < num; i++) {
                                projection[i] = data.readString();
                            }
                        }
                        android.os.Bundle queryArgs = data.readBundle();
                        android.database.IContentObserver observer = IContentObserver.Stub.asInterface(data.readStrongBinder());
                        android.os.ICancellationSignal cancellationSignal = ICancellationSignal.Stub.asInterface(data.readStrongBinder());
                        android.database.Cursor cursor = query(callingPkg, url, projection, queryArgs, cancellationSignal);
                        if (cursor != null) {
                            android.database.CursorToBulkCursorAdaptor adaptor = null;
                            try {
                                adaptor = new android.database.CursorToBulkCursorAdaptor(cursor, observer, getProviderName());
                                cursor = null;
                                android.database.BulkCursorDescriptor d = adaptor.getBulkCursorDescriptor();
                                adaptor = null;
                                reply.writeNoException();
                                reply.writeInt(1);
                                d.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                            } finally {
                                // Close cursor if an exception was thrown while constructing the adaptor.
                                if (adaptor != null) {
                                    adaptor.close();
                                }
                                if (cursor != null) {
                                    cursor.close();
                                }
                            }
                        } else {
                            reply.writeNoException();
                            reply.writeInt(0);
                        }
                        return true;
                    }
                case android.content.IContentProvider.GET_TYPE_TRANSACTION :
                    {
                        data.enforceInterface(android.content.IContentProvider.descriptor);
                        android.net.Uri url = Uri.CREATOR.createFromParcel(data);
                        java.lang.String type = getType(url);
                        reply.writeNoException();
                        reply.writeString(type);
                        return true;
                    }
                case android.content.IContentProvider.INSERT_TRANSACTION :
                    {
                        data.enforceInterface(android.content.IContentProvider.descriptor);
                        java.lang.String callingPkg = data.readString();
                        android.net.Uri url = Uri.CREATOR.createFromParcel(data);
                        android.content.ContentValues values = this.CREATOR.createFromParcel(data);
                        android.net.Uri out = insert(callingPkg, url, values);
                        reply.writeNoException();
                        android.net.Uri.writeToParcel(reply, out);
                        return true;
                    }
                case android.content.IContentProvider.BULK_INSERT_TRANSACTION :
                    {
                        data.enforceInterface(android.content.IContentProvider.descriptor);
                        java.lang.String callingPkg = data.readString();
                        android.net.Uri url = Uri.CREATOR.createFromParcel(data);
                        android.content.ContentValues[] values = data.createTypedArray(this.CREATOR);
                        int count = bulkInsert(callingPkg, url, values);
                        reply.writeNoException();
                        reply.writeInt(count);
                        return true;
                    }
                case android.content.IContentProvider.APPLY_BATCH_TRANSACTION :
                    {
                        data.enforceInterface(android.content.IContentProvider.descriptor);
                        java.lang.String callingPkg = data.readString();
                        java.lang.String authority = data.readString();
                        final int numOperations = data.readInt();
                        final java.util.ArrayList<android.content.ContentProviderOperation> operations = new java.util.ArrayList<>(numOperations);
                        for (int i = 0; i < numOperations; i++) {
                            operations.add(i, android.content.ContentProviderOperation.CREATOR.createFromParcel(data));
                        }
                        final android.content.ContentProviderResult[] results = applyBatch(callingPkg, authority, operations);
                        reply.writeNoException();
                        reply.writeTypedArray(results, 0);
                        return true;
                    }
                case android.content.IContentProvider.DELETE_TRANSACTION :
                    {
                        data.enforceInterface(android.content.IContentProvider.descriptor);
                        java.lang.String callingPkg = data.readString();
                        android.net.Uri url = Uri.CREATOR.createFromParcel(data);
                        java.lang.String selection = data.readString();
                        java.lang.String[] selectionArgs = data.readStringArray();
                        int count = delete(callingPkg, url, selection, selectionArgs);
                        reply.writeNoException();
                        reply.writeInt(count);
                        return true;
                    }
                case android.content.IContentProvider.UPDATE_TRANSACTION :
                    {
                        data.enforceInterface(android.content.IContentProvider.descriptor);
                        java.lang.String callingPkg = data.readString();
                        android.net.Uri url = Uri.CREATOR.createFromParcel(data);
                        android.content.ContentValues values = this.CREATOR.createFromParcel(data);
                        java.lang.String selection = data.readString();
                        java.lang.String[] selectionArgs = data.readStringArray();
                        int count = update(callingPkg, url, values, selection, selectionArgs);
                        reply.writeNoException();
                        reply.writeInt(count);
                        return true;
                    }
                case android.content.IContentProvider.OPEN_FILE_TRANSACTION :
                    {
                        data.enforceInterface(android.content.IContentProvider.descriptor);
                        java.lang.String callingPkg = data.readString();
                        android.net.Uri url = Uri.CREATOR.createFromParcel(data);
                        java.lang.String mode = data.readString();
                        android.os.ICancellationSignal signal = ICancellationSignal.Stub.asInterface(data.readStrongBinder());
                        android.os.IBinder callerToken = data.readStrongBinder();
                        android.os.ParcelFileDescriptor fd;
                        fd = openFile(callingPkg, url, mode, signal, callerToken);
                        reply.writeNoException();
                        if (fd != null) {
                            reply.writeInt(1);
                            fd.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    }
                case android.content.IContentProvider.OPEN_ASSET_FILE_TRANSACTION :
                    {
                        data.enforceInterface(android.content.IContentProvider.descriptor);
                        java.lang.String callingPkg = data.readString();
                        android.net.Uri url = Uri.CREATOR.createFromParcel(data);
                        java.lang.String mode = data.readString();
                        android.os.ICancellationSignal signal = ICancellationSignal.Stub.asInterface(data.readStrongBinder());
                        android.content.res.AssetFileDescriptor fd;
                        fd = openAssetFile(callingPkg, url, mode, signal);
                        reply.writeNoException();
                        if (fd != null) {
                            reply.writeInt(1);
                            fd.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    }
                case android.content.IContentProvider.CALL_TRANSACTION :
                    {
                        data.enforceInterface(android.content.IContentProvider.descriptor);
                        java.lang.String callingPkg = data.readString();
                        java.lang.String authority = data.readString();
                        java.lang.String method = data.readString();
                        java.lang.String stringArg = data.readString();
                        android.os.Bundle args = data.readBundle();
                        android.os.Bundle responseBundle = call(callingPkg, authority, method, stringArg, args);
                        reply.writeNoException();
                        reply.writeBundle(responseBundle);
                        return true;
                    }
                case android.content.IContentProvider.GET_STREAM_TYPES_TRANSACTION :
                    {
                        data.enforceInterface(android.content.IContentProvider.descriptor);
                        android.net.Uri url = Uri.CREATOR.createFromParcel(data);
                        java.lang.String mimeTypeFilter = data.readString();
                        java.lang.String[] types = getStreamTypes(url, mimeTypeFilter);
                        reply.writeNoException();
                        reply.writeStringArray(types);
                        return true;
                    }
                case android.content.IContentProvider.OPEN_TYPED_ASSET_FILE_TRANSACTION :
                    {
                        data.enforceInterface(android.content.IContentProvider.descriptor);
                        java.lang.String callingPkg = data.readString();
                        android.net.Uri url = Uri.CREATOR.createFromParcel(data);
                        java.lang.String mimeType = data.readString();
                        android.os.Bundle opts = data.readBundle();
                        android.os.ICancellationSignal signal = ICancellationSignal.Stub.asInterface(data.readStrongBinder());
                        android.content.res.AssetFileDescriptor fd;
                        fd = openTypedAssetFile(callingPkg, url, mimeType, opts, signal);
                        reply.writeNoException();
                        if (fd != null) {
                            reply.writeInt(1);
                            fd.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    }
                case android.content.IContentProvider.CREATE_CANCELATION_SIGNAL_TRANSACTION :
                    {
                        data.enforceInterface(android.content.IContentProvider.descriptor);
                        android.os.ICancellationSignal cancellationSignal = createCancellationSignal();
                        reply.writeNoException();
                        reply.writeStrongBinder(cancellationSignal.asBinder());
                        return true;
                    }
                case android.content.IContentProvider.CANONICALIZE_TRANSACTION :
                    {
                        data.enforceInterface(android.content.IContentProvider.descriptor);
                        java.lang.String callingPkg = data.readString();
                        android.net.Uri url = Uri.CREATOR.createFromParcel(data);
                        android.net.Uri out = canonicalize(callingPkg, url);
                        reply.writeNoException();
                        android.net.Uri.writeToParcel(reply, out);
                        return true;
                    }
                case android.content.IContentProvider.UNCANONICALIZE_TRANSACTION :
                    {
                        data.enforceInterface(android.content.IContentProvider.descriptor);
                        java.lang.String callingPkg = data.readString();
                        android.net.Uri url = Uri.CREATOR.createFromParcel(data);
                        android.net.Uri out = uncanonicalize(callingPkg, url);
                        reply.writeNoException();
                        android.net.Uri.writeToParcel(reply, out);
                        return true;
                    }
                case android.content.IContentProvider.REFRESH_TRANSACTION :
                    {
                        data.enforceInterface(android.content.IContentProvider.descriptor);
                        java.lang.String callingPkg = data.readString();
                        android.net.Uri url = Uri.CREATOR.createFromParcel(data);
                        android.os.Bundle args = data.readBundle();
                        android.os.ICancellationSignal signal = ICancellationSignal.Stub.asInterface(data.readStrongBinder());
                        boolean out = refresh(callingPkg, url, args, signal);
                        reply.writeNoException();
                        reply.writeInt(out ? 0 : -1);
                        return true;
                    }
            }
        } catch (java.lang.Exception e) {
            android.database.DatabaseUtils.writeExceptionToParcel(reply, e);
            return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    @java.lang.Override
    public android.os.IBinder asBinder() {
        return this;
    }
}

