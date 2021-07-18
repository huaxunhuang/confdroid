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
 * Mock implementation of ContentProvider.  All methods are non-functional and throw
 * {@link java.lang.UnsupportedOperationException}.  Tests can extend this class to
 * implement behavior needed for tests.
 */
public class MockContentProvider extends android.content.ContentProvider {
    /* Note: if you add methods to ContentProvider, you must add similar methods to
          MockContentProvider.
     */
    /**
     * IContentProvider that directs all calls to this MockContentProvider.
     */
    private class InversionIContentProvider implements android.content.IContentProvider {
        @java.lang.Override
        public android.content.ContentProviderResult[] applyBatch(java.lang.String callingPackage, java.util.ArrayList<android.content.ContentProviderOperation> operations) throws android.content.OperationApplicationException, android.os.RemoteException {
            return android.test.mock.MockContentProvider.this.applyBatch(operations);
        }

        @java.lang.Override
        public int bulkInsert(java.lang.String callingPackage, android.net.Uri url, android.content.ContentValues[] initialValues) throws android.os.RemoteException {
            return android.test.mock.MockContentProvider.this.bulkInsert(url, initialValues);
        }

        @java.lang.Override
        public int delete(java.lang.String callingPackage, android.net.Uri url, java.lang.String selection, java.lang.String[] selectionArgs) throws android.os.RemoteException {
            return android.test.mock.MockContentProvider.this.delete(url, selection, selectionArgs);
        }

        @java.lang.Override
        public java.lang.String getType(android.net.Uri url) throws android.os.RemoteException {
            return android.test.mock.MockContentProvider.this.getType(url);
        }

        @java.lang.Override
        public android.net.Uri insert(java.lang.String callingPackage, android.net.Uri url, android.content.ContentValues initialValues) throws android.os.RemoteException {
            return android.test.mock.MockContentProvider.this.insert(url, initialValues);
        }

        @java.lang.Override
        public android.content.res.AssetFileDescriptor openAssetFile(java.lang.String callingPackage, android.net.Uri url, java.lang.String mode, android.os.ICancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException {
            return android.test.mock.MockContentProvider.this.openAssetFile(url, mode);
        }

        @java.lang.Override
        public android.os.ParcelFileDescriptor openFile(java.lang.String callingPackage, android.net.Uri url, java.lang.String mode, android.os.ICancellationSignal signal, android.os.IBinder callerToken) throws android.os.RemoteException, java.io.FileNotFoundException {
            return android.test.mock.MockContentProvider.this.openFile(url, mode);
        }

        @java.lang.Override
        public android.database.Cursor query(java.lang.String callingPackage, android.net.Uri url, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder, android.os.ICancellationSignal cancellationSignal) throws android.os.RemoteException {
            return android.test.mock.MockContentProvider.this.query(url, projection, selection, selectionArgs, sortOrder);
        }

        @java.lang.Override
        public int update(java.lang.String callingPackage, android.net.Uri url, android.content.ContentValues values, java.lang.String selection, java.lang.String[] selectionArgs) throws android.os.RemoteException {
            return android.test.mock.MockContentProvider.this.update(url, values, selection, selectionArgs);
        }

        @java.lang.Override
        public android.os.Bundle call(java.lang.String callingPackage, java.lang.String method, java.lang.String request, android.os.Bundle args) throws android.os.RemoteException {
            return android.test.mock.MockContentProvider.this.call(method, request, args);
        }

        @java.lang.Override
        public android.os.IBinder asBinder() {
            throw new java.lang.UnsupportedOperationException();
        }

        @java.lang.Override
        public java.lang.String[] getStreamTypes(android.net.Uri url, java.lang.String mimeTypeFilter) throws android.os.RemoteException {
            return android.test.mock.MockContentProvider.this.getStreamTypes(url, mimeTypeFilter);
        }

        @java.lang.Override
        public android.content.res.AssetFileDescriptor openTypedAssetFile(java.lang.String callingPackage, android.net.Uri url, java.lang.String mimeType, android.os.Bundle opts, android.os.ICancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException {
            return android.test.mock.MockContentProvider.this.openTypedAssetFile(url, mimeType, opts);
        }

        @java.lang.Override
        public android.os.ICancellationSignal createCancellationSignal() throws android.os.RemoteException {
            return null;
        }

        @java.lang.Override
        public android.net.Uri canonicalize(java.lang.String callingPkg, android.net.Uri uri) throws android.os.RemoteException {
            return android.test.mock.MockContentProvider.this.canonicalize(uri);
        }

        @java.lang.Override
        public android.net.Uri uncanonicalize(java.lang.String callingPkg, android.net.Uri uri) throws android.os.RemoteException {
            return android.test.mock.MockContentProvider.this.uncanonicalize(uri);
        }
    }

    private final android.test.mock.MockContentProvider.InversionIContentProvider mIContentProvider = new android.test.mock.MockContentProvider.InversionIContentProvider();

    /**
     * A constructor using {@link MockContext} instance as a Context in it.
     */
    protected MockContentProvider() {
        super(new android.test.mock.MockContext(), "", "", null);
    }

    /**
     * A constructor accepting a Context instance, which is supposed to be the subclasss of
     * {@link MockContext}.
     */
    public MockContentProvider(android.content.Context context) {
        super(context, "", "", null);
    }

    /**
     * A constructor which initialize four member variables which
     * {@link android.content.ContentProvider} have internally.
     *
     * @param context
     * 		A Context object which should be some mock instance (like the
     * 		instance of {@link android.test.mock.MockContext}).
     * @param readPermission
     * 		The read permision you want this instance should have in the
     * 		test, which is available via {@link #getReadPermission()}.
     * @param writePermission
     * 		The write permission you want this instance should have
     * 		in the test, which is available via {@link #getWritePermission()}.
     * @param pathPermissions
     * 		The PathPermissions you want this instance should have
     * 		in the test, which is available via {@link #getPathPermissions()}.
     */
    public MockContentProvider(android.content.Context context, java.lang.String readPermission, java.lang.String writePermission, android.content.pm.PathPermission[] pathPermissions) {
        super(context, readPermission, writePermission, pathPermissions);
    }

    @java.lang.Override
    public int delete(android.net.Uri uri, java.lang.String selection, java.lang.String[] selectionArgs) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public java.lang.String getType(android.net.Uri uri) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public android.net.Uri insert(android.net.Uri uri, android.content.ContentValues values) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public boolean onCreate() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public android.database.Cursor query(android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public int update(android.net.Uri uri, android.content.ContentValues values, java.lang.String selection, java.lang.String[] selectionArgs) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    /**
     * If you're reluctant to implement this manually, please just call super.bulkInsert().
     */
    @java.lang.Override
    public int bulkInsert(android.net.Uri uri, android.content.ContentValues[] values) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public void attachInfo(android.content.Context context, android.content.pm.ProviderInfo info) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public android.content.ContentProviderResult[] applyBatch(java.util.ArrayList<android.content.ContentProviderOperation> operations) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.os.Bundle call(java.lang.String method, java.lang.String request, android.os.Bundle args) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method call");
    }

    public java.lang.String[] getStreamTypes(android.net.Uri url, java.lang.String mimeTypeFilter) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method call");
    }

    public android.content.res.AssetFileDescriptor openTypedAssetFile(android.net.Uri url, java.lang.String mimeType, android.os.Bundle opts) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method call");
    }

    /**
     * Returns IContentProvider which calls back same methods in this class.
     * By overriding this class, we avoid the mechanism hidden behind ContentProvider
     * (IPC, etc.)
     *
     * @unknown 
     */
    @java.lang.Override
    public final android.content.IContentProvider getIContentProvider() {
        return mIContentProvider;
    }
}

