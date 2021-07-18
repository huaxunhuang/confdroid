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
 * The ipc interface to talk to a content provider.
 *
 * @unknown 
 */
public interface IContentProvider extends android.os.IInterface {
    public android.database.Cursor query(java.lang.String callingPkg, android.net.Uri url, @android.annotation.Nullable
    java.lang.String[] projection, @android.annotation.Nullable
    android.os.Bundle queryArgs, @android.annotation.Nullable
    android.os.ICancellationSignal cancellationSignal) throws android.os.RemoteException;

    public java.lang.String getType(android.net.Uri url) throws android.os.RemoteException;

    @android.annotation.UnsupportedAppUsage
    public android.net.Uri insert(java.lang.String callingPkg, android.net.Uri url, android.content.ContentValues initialValues) throws android.os.RemoteException;

    @android.annotation.UnsupportedAppUsage
    public int bulkInsert(java.lang.String callingPkg, android.net.Uri url, android.content.ContentValues[] initialValues) throws android.os.RemoteException;

    @android.annotation.UnsupportedAppUsage
    public int delete(java.lang.String callingPkg, android.net.Uri url, java.lang.String selection, java.lang.String[] selectionArgs) throws android.os.RemoteException;

    @android.annotation.UnsupportedAppUsage
    public int update(java.lang.String callingPkg, android.net.Uri url, android.content.ContentValues values, java.lang.String selection, java.lang.String[] selectionArgs) throws android.os.RemoteException;

    public android.os.ParcelFileDescriptor openFile(java.lang.String callingPkg, android.net.Uri url, java.lang.String mode, android.os.ICancellationSignal signal, android.os.IBinder callerToken) throws android.os.RemoteException, java.io.FileNotFoundException;

    public android.content.res.AssetFileDescriptor openAssetFile(java.lang.String callingPkg, android.net.Uri url, java.lang.String mode, android.os.ICancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException;

    @java.lang.Deprecated
    public default android.content.ContentProviderResult[] applyBatch(java.lang.String callingPkg, java.util.ArrayList<android.content.ContentProviderOperation> operations) throws android.content.OperationApplicationException, android.os.RemoteException {
        return applyBatch(callingPkg, "unknown", operations);
    }

    public android.content.ContentProviderResult[] applyBatch(java.lang.String callingPkg, java.lang.String authority, java.util.ArrayList<android.content.ContentProviderOperation> operations) throws android.content.OperationApplicationException, android.os.RemoteException;

    @java.lang.Deprecated
    @android.annotation.UnsupportedAppUsage
    public default android.os.Bundle call(java.lang.String callingPkg, java.lang.String method, @android.annotation.Nullable
    java.lang.String arg, @android.annotation.Nullable
    android.os.Bundle extras) throws android.os.RemoteException {
        return call(callingPkg, "unknown", method, arg, extras);
    }

    public android.os.Bundle call(java.lang.String callingPkg, java.lang.String authority, java.lang.String method, @android.annotation.Nullable
    java.lang.String arg, @android.annotation.Nullable
    android.os.Bundle extras) throws android.os.RemoteException;

    public android.os.ICancellationSignal createCancellationSignal() throws android.os.RemoteException;

    public android.net.Uri canonicalize(java.lang.String callingPkg, android.net.Uri uri) throws android.os.RemoteException;

    public android.net.Uri uncanonicalize(java.lang.String callingPkg, android.net.Uri uri) throws android.os.RemoteException;

    public boolean refresh(java.lang.String callingPkg, android.net.Uri url, @android.annotation.Nullable
    android.os.Bundle args, android.os.ICancellationSignal cancellationSignal) throws android.os.RemoteException;

    // Data interchange.
    public java.lang.String[] getStreamTypes(android.net.Uri url, java.lang.String mimeTypeFilter) throws android.os.RemoteException;

    public android.content.res.AssetFileDescriptor openTypedAssetFile(java.lang.String callingPkg, android.net.Uri url, java.lang.String mimeType, android.os.Bundle opts, android.os.ICancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException;

    /* IPC constants */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    static final java.lang.String descriptor = "android.content.IContentProvider";

    @android.annotation.UnsupportedAppUsage
    static final int QUERY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION;

    static final int GET_TYPE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 1;

    static final int INSERT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 2;

    static final int DELETE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 3;

    static final int UPDATE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 9;

    static final int BULK_INSERT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 12;

    static final int OPEN_FILE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 13;

    static final int OPEN_ASSET_FILE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 14;

    static final int APPLY_BATCH_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 19;

    static final int CALL_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 20;

    static final int GET_STREAM_TYPES_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 21;

    static final int OPEN_TYPED_ASSET_FILE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 22;

    static final int CREATE_CANCELATION_SIGNAL_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 23;

    static final int CANONICALIZE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 24;

    static final int UNCANONICALIZE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 25;

    static final int REFRESH_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 26;
}

