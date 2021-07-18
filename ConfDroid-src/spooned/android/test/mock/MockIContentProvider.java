/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.test.mock;


/**
 * Mock implementation of IContentProvider.  All methods are non-functional and throw
 * {@link java.lang.UnsupportedOperationException}.  Tests can extend this class to
 * implement behavior needed for tests.
 *
 * @unknown - @hide because this exposes bulkQuery() and call(), which must also be hidden.
 */
public class MockIContentProvider implements android.content.IContentProvider {
    public int bulkInsert(java.lang.String callingPackage, android.net.Uri url, android.content.ContentValues[] initialValues) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.SuppressWarnings("unused")
    public int delete(java.lang.String callingPackage, android.net.Uri url, java.lang.String selection, java.lang.String[] selectionArgs) throws android.os.RemoteException {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    public java.lang.String getType(android.net.Uri url) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.SuppressWarnings("unused")
    public android.net.Uri insert(java.lang.String callingPackage, android.net.Uri url, android.content.ContentValues initialValues) throws android.os.RemoteException {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    public android.os.ParcelFileDescriptor openFile(java.lang.String callingPackage, android.net.Uri url, java.lang.String mode, android.os.ICancellationSignal signal, android.os.IBinder callerToken) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    public android.content.res.AssetFileDescriptor openAssetFile(java.lang.String callingPackage, android.net.Uri uri, java.lang.String mode, android.os.ICancellationSignal signal) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    public android.content.ContentProviderResult[] applyBatch(java.lang.String callingPackage, java.util.ArrayList<android.content.ContentProviderOperation> operations) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    public android.database.Cursor query(java.lang.String callingPackage, android.net.Uri url, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder, android.os.ICancellationSignal cancellationSignal) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    public android.content.EntityIterator queryEntities(android.net.Uri url, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    public int update(java.lang.String callingPackage, android.net.Uri url, android.content.ContentValues values, java.lang.String selection, java.lang.String[] selectionArgs) throws android.os.RemoteException {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    public android.os.Bundle call(java.lang.String callingPackage, java.lang.String method, java.lang.String request, android.os.Bundle args) throws android.os.RemoteException {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    public android.os.IBinder asBinder() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    public java.lang.String[] getStreamTypes(android.net.Uri url, java.lang.String mimeTypeFilter) throws android.os.RemoteException {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    public android.content.res.AssetFileDescriptor openTypedAssetFile(java.lang.String callingPackage, android.net.Uri url, java.lang.String mimeType, android.os.Bundle opts, android.os.ICancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public android.os.ICancellationSignal createCancellationSignal() throws android.os.RemoteException {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public android.net.Uri canonicalize(java.lang.String callingPkg, android.net.Uri uri) throws android.os.RemoteException {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public android.net.Uri uncanonicalize(java.lang.String callingPkg, android.net.Uri uri) throws android.os.RemoteException {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }
}

