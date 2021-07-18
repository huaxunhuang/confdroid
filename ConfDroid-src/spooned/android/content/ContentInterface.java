/**
 * Copyright (C) 2018 The Android Open Source Project
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
 * Interface representing calls that can be made to {@link ContentProvider}
 * instances.
 * <p>
 * These methods have been extracted into a general interface so that APIs can
 * be flexible in accepting either a {@link ContentProvider}, a
 * {@link ContentResolver}, or a {@link ContentProviderClient}.
 *
 * @unknown 
 */
public interface ContentInterface {
    @android.annotation.Nullable
    public android.database.Cursor query(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    java.lang.String[] projection, @android.annotation.Nullable
    android.os.Bundle queryArgs, @android.annotation.Nullable
    android.os.CancellationSignal cancellationSignal) throws android.os.RemoteException;

    @android.annotation.Nullable
    public java.lang.String getType(@android.annotation.NonNull
    android.net.Uri uri) throws android.os.RemoteException;

    @android.annotation.Nullable
    public java.lang.String[] getStreamTypes(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mimeTypeFilter) throws android.os.RemoteException;

    @android.annotation.Nullable
    public android.net.Uri canonicalize(@android.annotation.NonNull
    android.net.Uri uri) throws android.os.RemoteException;

    @android.annotation.Nullable
    public android.net.Uri uncanonicalize(@android.annotation.NonNull
    android.net.Uri uri) throws android.os.RemoteException;

    public boolean refresh(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    android.os.Bundle args, @android.annotation.Nullable
    android.os.CancellationSignal cancellationSignal) throws android.os.RemoteException;

    @android.annotation.Nullable
    public android.net.Uri insert(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    android.content.ContentValues initialValues) throws android.os.RemoteException;

    public int bulkInsert(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    android.content.ContentValues[] initialValues) throws android.os.RemoteException;

    public int delete(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    java.lang.String selection, @android.annotation.Nullable
    java.lang.String[] selectionArgs) throws android.os.RemoteException;

    public int update(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    android.content.ContentValues values, @android.annotation.Nullable
    java.lang.String selection, @android.annotation.Nullable
    java.lang.String[] selectionArgs) throws android.os.RemoteException;

    @android.annotation.Nullable
    public android.os.ParcelFileDescriptor openFile(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mode, @android.annotation.Nullable
    android.os.CancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException;

    @android.annotation.Nullable
    public android.content.res.AssetFileDescriptor openAssetFile(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mode, @android.annotation.Nullable
    android.os.CancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException;

    @android.annotation.Nullable
    public android.content.res.AssetFileDescriptor openTypedAssetFile(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mimeTypeFilter, @android.annotation.Nullable
    android.os.Bundle opts, @android.annotation.Nullable
    android.os.CancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException;

    @android.annotation.NonNull
    public android.content.ContentProviderResult[] applyBatch(@android.annotation.NonNull
    java.lang.String authority, @android.annotation.NonNull
    java.util.ArrayList<android.content.ContentProviderOperation> operations) throws android.content.OperationApplicationException, android.os.RemoteException;

    @android.annotation.Nullable
    public android.os.Bundle call(@android.annotation.NonNull
    java.lang.String authority, @android.annotation.NonNull
    java.lang.String method, @android.annotation.Nullable
    java.lang.String arg, @android.annotation.Nullable
    android.os.Bundle extras) throws android.os.RemoteException;
}

