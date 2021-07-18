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
package android.content;


/**
 * The public interface object used to interact with a specific
 * {@link ContentProvider}.
 * <p>
 * Instances can be obtained by calling
 * {@link ContentResolver#acquireContentProviderClient} or
 * {@link ContentResolver#acquireUnstableContentProviderClient}. Instances must
 * be released using {@link #close()} in order to indicate to the system that
 * the underlying {@link ContentProvider} is no longer needed and can be killed
 * to free up resources.
 * <p>
 * Note that you should generally create a new ContentProviderClient instance
 * for each thread that will be performing operations. Unlike
 * {@link ContentResolver}, the methods here such as {@link #query} and
 * {@link #openFile} are not thread safe -- you must not call {@link #close()}
 * on the ContentProviderClient those calls are made from until you are finished
 * with the data they have returned.
 */
public class ContentProviderClient implements android.content.ContentInterface , java.lang.AutoCloseable {
    private static final java.lang.String TAG = "ContentProviderClient";

    @com.android.internal.annotations.GuardedBy("ContentProviderClient.class")
    private static android.os.Handler sAnrHandler;

    private final android.content.ContentResolver mContentResolver;

    @android.annotation.UnsupportedAppUsage
    private final android.content.IContentProvider mContentProvider;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private final java.lang.String mPackageName;

    private final java.lang.String mAuthority;

    private final boolean mStable;

    private final java.util.concurrent.atomic.AtomicBoolean mClosed = new java.util.concurrent.atomic.AtomicBoolean();

    private final dalvik.system.CloseGuard mCloseGuard = dalvik.system.CloseGuard.get();

    private long mAnrTimeout;

    private android.content.ContentProviderClient.NotRespondingRunnable mAnrRunnable;

    /**
     * {@hide }
     */
    @com.android.internal.annotations.VisibleForTesting
    public ContentProviderClient(android.content.ContentResolver contentResolver, android.content.IContentProvider contentProvider, boolean stable) {
        // Only used for testing, so use a fake authority
        this(contentResolver, contentProvider, "unknown", stable);
    }

    /**
     * {@hide }
     */
    public ContentProviderClient(android.content.ContentResolver contentResolver, android.content.IContentProvider contentProvider, java.lang.String authority, boolean stable) {
        mContentResolver = contentResolver;
        mContentProvider = contentProvider;
        mPackageName = contentResolver.mPackageName;
        mAuthority = authority;
        mStable = stable;
        mCloseGuard.open("close");
    }

    /**
     * Configure this client to automatically detect and kill the remote
     * provider when an "application not responding" event is detected.
     *
     * @param timeoutMillis
     * 		the duration for which a pending call is allowed
     * 		block before the remote provider is considered to be
     * 		unresponsive. Set to {@code 0} to allow pending calls to block
     * 		indefinitely with no action taken.
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.TestApi
    @android.annotation.RequiresPermission(android.content.android.Manifest.permission.REMOVE_TASKS.class)
    public void setDetectNotResponding(@android.annotation.DurationMillisLong
    long timeoutMillis) {
        synchronized(android.content.ContentProviderClient.class) {
            mAnrTimeout = timeoutMillis;
            if (timeoutMillis > 0) {
                if (mAnrRunnable == null) {
                    mAnrRunnable = new android.content.ContentProviderClient.NotRespondingRunnable();
                }
                if (android.content.ContentProviderClient.sAnrHandler == null) {
                    android.content.ContentProviderClient.sAnrHandler = /* async */
                    new android.os.Handler(android.os.Looper.getMainLooper(), null, true);
                }
                // If the remote process hangs, we're going to kill it, so we're
                // technically okay doing blocking calls.
                android.os.Binder.allowBlocking(mContentProvider.asBinder());
            } else {
                mAnrRunnable = null;
                // If we're no longer watching for hangs, revert back to default
                // blocking behavior.
                android.os.Binder.defaultBlocking(mContentProvider.asBinder());
            }
        }
    }

    private void beforeRemote() {
        if (mAnrRunnable != null) {
            android.content.ContentProviderClient.sAnrHandler.postDelayed(mAnrRunnable, mAnrTimeout);
        }
    }

    private void afterRemote() {
        if (mAnrRunnable != null) {
            android.content.ContentProviderClient.sAnrHandler.removeCallbacks(mAnrRunnable);
        }
    }

    /**
     * See {@link ContentProvider#query ContentProvider.query}
     */
    @android.annotation.Nullable
    public android.database.Cursor query(@android.annotation.NonNull
    android.net.Uri url, @android.annotation.Nullable
    java.lang.String[] projection, @android.annotation.Nullable
    java.lang.String selection, @android.annotation.Nullable
    java.lang.String[] selectionArgs, @android.annotation.Nullable
    java.lang.String sortOrder) throws android.os.RemoteException {
        return query(url, projection, selection, selectionArgs, sortOrder, null);
    }

    /**
     * See {@link ContentProvider#query ContentProvider.query}
     */
    @android.annotation.Nullable
    public android.database.Cursor query(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    java.lang.String[] projection, @android.annotation.Nullable
    java.lang.String selection, @android.annotation.Nullable
    java.lang.String[] selectionArgs, @android.annotation.Nullable
    java.lang.String sortOrder, @android.annotation.Nullable
    android.os.CancellationSignal cancellationSignal) throws android.os.RemoteException {
        android.os.Bundle queryArgs = android.content.ContentResolver.createSqlQueryBundle(selection, selectionArgs, sortOrder);
        return query(uri, projection, queryArgs, cancellationSignal);
    }

    /**
     * See {@link ContentProvider#query ContentProvider.query}
     */
    @java.lang.Override
    @android.annotation.Nullable
    public android.database.Cursor query(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    java.lang.String[] projection, android.os.Bundle queryArgs, @android.annotation.Nullable
    android.os.CancellationSignal cancellationSignal) throws android.os.RemoteException {
        com.android.internal.util.Preconditions.checkNotNull(uri, "url");
        beforeRemote();
        try {
            android.os.ICancellationSignal remoteCancellationSignal = null;
            if (cancellationSignal != null) {
                cancellationSignal.throwIfCanceled();
                remoteCancellationSignal = mContentProvider.createCancellationSignal();
                cancellationSignal.setRemote(remoteCancellationSignal);
            }
            final android.database.Cursor cursor = mContentProvider.query(mPackageName, uri, projection, queryArgs, remoteCancellationSignal);
            if (cursor == null) {
                return null;
            }
            return new android.content.ContentProviderClient.CursorWrapperInner(cursor);
        } catch (android.os.DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        } finally {
            afterRemote();
        }
    }

    /**
     * See {@link ContentProvider#getType ContentProvider.getType}
     */
    @java.lang.Override
    @android.annotation.Nullable
    public java.lang.String getType(@android.annotation.NonNull
    android.net.Uri url) throws android.os.RemoteException {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        beforeRemote();
        try {
            return mContentProvider.getType(url);
        } catch (android.os.DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        } finally {
            afterRemote();
        }
    }

    /**
     * See {@link ContentProvider#getStreamTypes ContentProvider.getStreamTypes}
     */
    @java.lang.Override
    @android.annotation.Nullable
    public java.lang.String[] getStreamTypes(@android.annotation.NonNull
    android.net.Uri url, @android.annotation.NonNull
    java.lang.String mimeTypeFilter) throws android.os.RemoteException {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        com.android.internal.util.Preconditions.checkNotNull(mimeTypeFilter, "mimeTypeFilter");
        beforeRemote();
        try {
            return mContentProvider.getStreamTypes(url, mimeTypeFilter);
        } catch (android.os.DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        } finally {
            afterRemote();
        }
    }

    /**
     * See {@link ContentProvider#canonicalize}
     */
    @java.lang.Override
    @android.annotation.Nullable
    public final android.net.Uri canonicalize(@android.annotation.NonNull
    android.net.Uri url) throws android.os.RemoteException {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        beforeRemote();
        try {
            return mContentProvider.canonicalize(mPackageName, url);
        } catch (android.os.DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        } finally {
            afterRemote();
        }
    }

    /**
     * See {@link ContentProvider#uncanonicalize}
     */
    @java.lang.Override
    @android.annotation.Nullable
    public final android.net.Uri uncanonicalize(@android.annotation.NonNull
    android.net.Uri url) throws android.os.RemoteException {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        beforeRemote();
        try {
            return mContentProvider.uncanonicalize(mPackageName, url);
        } catch (android.os.DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        } finally {
            afterRemote();
        }
    }

    /**
     * See {@link ContentProvider#refresh}
     */
    @java.lang.Override
    public boolean refresh(android.net.Uri url, @android.annotation.Nullable
    android.os.Bundle args, @android.annotation.Nullable
    android.os.CancellationSignal cancellationSignal) throws android.os.RemoteException {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        beforeRemote();
        try {
            android.os.ICancellationSignal remoteCancellationSignal = null;
            if (cancellationSignal != null) {
                cancellationSignal.throwIfCanceled();
                remoteCancellationSignal = mContentProvider.createCancellationSignal();
                cancellationSignal.setRemote(remoteCancellationSignal);
            }
            return mContentProvider.refresh(mPackageName, url, args, remoteCancellationSignal);
        } catch (android.os.DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        } finally {
            afterRemote();
        }
    }

    /**
     * See {@link ContentProvider#insert ContentProvider.insert}
     */
    @java.lang.Override
    @android.annotation.Nullable
    public android.net.Uri insert(@android.annotation.NonNull
    android.net.Uri url, @android.annotation.Nullable
    android.content.ContentValues initialValues) throws android.os.RemoteException {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        beforeRemote();
        try {
            return mContentProvider.insert(mPackageName, url, initialValues);
        } catch (android.os.DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        } finally {
            afterRemote();
        }
    }

    /**
     * See {@link ContentProvider#bulkInsert ContentProvider.bulkInsert}
     */
    @java.lang.Override
    public int bulkInsert(@android.annotation.NonNull
    android.net.Uri url, @android.annotation.NonNull
    android.content.ContentValues[] initialValues) throws android.os.RemoteException {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        com.android.internal.util.Preconditions.checkNotNull(initialValues, "initialValues");
        beforeRemote();
        try {
            return mContentProvider.bulkInsert(mPackageName, url, initialValues);
        } catch (android.os.DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        } finally {
            afterRemote();
        }
    }

    /**
     * See {@link ContentProvider#delete ContentProvider.delete}
     */
    @java.lang.Override
    public int delete(@android.annotation.NonNull
    android.net.Uri url, @android.annotation.Nullable
    java.lang.String selection, @android.annotation.Nullable
    java.lang.String[] selectionArgs) throws android.os.RemoteException {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        beforeRemote();
        try {
            return mContentProvider.delete(mPackageName, url, selection, selectionArgs);
        } catch (android.os.DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        } finally {
            afterRemote();
        }
    }

    /**
     * See {@link ContentProvider#update ContentProvider.update}
     */
    @java.lang.Override
    public int update(@android.annotation.NonNull
    android.net.Uri url, @android.annotation.Nullable
    android.content.ContentValues values, @android.annotation.Nullable
    java.lang.String selection, @android.annotation.Nullable
    java.lang.String[] selectionArgs) throws android.os.RemoteException {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        beforeRemote();
        try {
            return mContentProvider.update(mPackageName, url, values, selection, selectionArgs);
        } catch (android.os.DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        } finally {
            afterRemote();
        }
    }

    /**
     * See {@link ContentProvider#openFile ContentProvider.openFile}.  Note that
     * this <em>does not</em>
     * take care of non-content: URIs such as file:.  It is strongly recommended
     * you use the {@link ContentResolver#openFileDescriptor
     * ContentResolver.openFileDescriptor} API instead.
     */
    @android.annotation.Nullable
    public android.os.ParcelFileDescriptor openFile(@android.annotation.NonNull
    android.net.Uri url, @android.annotation.NonNull
    java.lang.String mode) throws android.os.RemoteException, java.io.FileNotFoundException {
        return openFile(url, mode, null);
    }

    /**
     * See {@link ContentProvider#openFile ContentProvider.openFile}.  Note that
     * this <em>does not</em>
     * take care of non-content: URIs such as file:.  It is strongly recommended
     * you use the {@link ContentResolver#openFileDescriptor
     * ContentResolver.openFileDescriptor} API instead.
     */
    @java.lang.Override
    @android.annotation.Nullable
    public android.os.ParcelFileDescriptor openFile(@android.annotation.NonNull
    android.net.Uri url, @android.annotation.NonNull
    java.lang.String mode, @android.annotation.Nullable
    android.os.CancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        com.android.internal.util.Preconditions.checkNotNull(mode, "mode");
        beforeRemote();
        try {
            android.os.ICancellationSignal remoteSignal = null;
            if (signal != null) {
                signal.throwIfCanceled();
                remoteSignal = mContentProvider.createCancellationSignal();
                signal.setRemote(remoteSignal);
            }
            return mContentProvider.openFile(mPackageName, url, mode, remoteSignal, null);
        } catch (android.os.DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        } finally {
            afterRemote();
        }
    }

    /**
     * See {@link ContentProvider#openAssetFile ContentProvider.openAssetFile}.
     * Note that this <em>does not</em>
     * take care of non-content: URIs such as file:.  It is strongly recommended
     * you use the {@link ContentResolver#openAssetFileDescriptor
     * ContentResolver.openAssetFileDescriptor} API instead.
     */
    @android.annotation.Nullable
    public android.content.res.AssetFileDescriptor openAssetFile(@android.annotation.NonNull
    android.net.Uri url, @android.annotation.NonNull
    java.lang.String mode) throws android.os.RemoteException, java.io.FileNotFoundException {
        return openAssetFile(url, mode, null);
    }

    /**
     * See {@link ContentProvider#openAssetFile ContentProvider.openAssetFile}.
     * Note that this <em>does not</em>
     * take care of non-content: URIs such as file:.  It is strongly recommended
     * you use the {@link ContentResolver#openAssetFileDescriptor
     * ContentResolver.openAssetFileDescriptor} API instead.
     */
    @java.lang.Override
    @android.annotation.Nullable
    public android.content.res.AssetFileDescriptor openAssetFile(@android.annotation.NonNull
    android.net.Uri url, @android.annotation.NonNull
    java.lang.String mode, @android.annotation.Nullable
    android.os.CancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        com.android.internal.util.Preconditions.checkNotNull(mode, "mode");
        beforeRemote();
        try {
            android.os.ICancellationSignal remoteSignal = null;
            if (signal != null) {
                signal.throwIfCanceled();
                remoteSignal = mContentProvider.createCancellationSignal();
                signal.setRemote(remoteSignal);
            }
            return mContentProvider.openAssetFile(mPackageName, url, mode, remoteSignal);
        } catch (android.os.DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        } finally {
            afterRemote();
        }
    }

    /**
     * See {@link ContentProvider#openTypedAssetFile ContentProvider.openTypedAssetFile}
     */
    @android.annotation.Nullable
    public final android.content.res.AssetFileDescriptor openTypedAssetFileDescriptor(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mimeType, @android.annotation.Nullable
    android.os.Bundle opts) throws android.os.RemoteException, java.io.FileNotFoundException {
        return openTypedAssetFileDescriptor(uri, mimeType, opts, null);
    }

    /**
     * See {@link ContentProvider#openTypedAssetFile ContentProvider.openTypedAssetFile}
     */
    @android.annotation.Nullable
    public final android.content.res.AssetFileDescriptor openTypedAssetFileDescriptor(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mimeType, @android.annotation.Nullable
    android.os.Bundle opts, @android.annotation.Nullable
    android.os.CancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException {
        return openTypedAssetFile(uri, mimeType, opts, signal);
    }

    @java.lang.Override
    @android.annotation.Nullable
    public final android.content.res.AssetFileDescriptor openTypedAssetFile(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mimeTypeFilter, @android.annotation.Nullable
    android.os.Bundle opts, @android.annotation.Nullable
    android.os.CancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException {
        com.android.internal.util.Preconditions.checkNotNull(uri, "uri");
        com.android.internal.util.Preconditions.checkNotNull(mimeTypeFilter, "mimeTypeFilter");
        beforeRemote();
        try {
            android.os.ICancellationSignal remoteSignal = null;
            if (signal != null) {
                signal.throwIfCanceled();
                remoteSignal = mContentProvider.createCancellationSignal();
                signal.setRemote(remoteSignal);
            }
            return mContentProvider.openTypedAssetFile(mPackageName, uri, mimeTypeFilter, opts, remoteSignal);
        } catch (android.os.DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        } finally {
            afterRemote();
        }
    }

    /**
     * See {@link ContentProvider#applyBatch ContentProvider.applyBatch}
     */
    @android.annotation.NonNull
    public android.content.ContentProviderResult[] applyBatch(@android.annotation.NonNull
    java.util.ArrayList<android.content.ContentProviderOperation> operations) throws android.content.OperationApplicationException, android.os.RemoteException {
        return applyBatch(mAuthority, operations);
    }

    /**
     * See {@link ContentProvider#applyBatch ContentProvider.applyBatch}
     */
    @java.lang.Override
    @android.annotation.NonNull
    public android.content.ContentProviderResult[] applyBatch(@android.annotation.NonNull
    java.lang.String authority, @android.annotation.NonNull
    java.util.ArrayList<android.content.ContentProviderOperation> operations) throws android.content.OperationApplicationException, android.os.RemoteException {
        com.android.internal.util.Preconditions.checkNotNull(operations, "operations");
        beforeRemote();
        try {
            return mContentProvider.applyBatch(mPackageName, authority, operations);
        } catch (android.os.DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        } finally {
            afterRemote();
        }
    }

    /**
     * See {@link ContentProvider#call(String, String, Bundle)}
     */
    @android.annotation.Nullable
    public android.os.Bundle call(@android.annotation.NonNull
    java.lang.String method, @android.annotation.Nullable
    java.lang.String arg, @android.annotation.Nullable
    android.os.Bundle extras) throws android.os.RemoteException {
        return call(mAuthority, method, arg, extras);
    }

    /**
     * See {@link ContentProvider#call(String, String, Bundle)}
     */
    @java.lang.Override
    @android.annotation.Nullable
    public android.os.Bundle call(@android.annotation.NonNull
    java.lang.String authority, @android.annotation.NonNull
    java.lang.String method, @android.annotation.Nullable
    java.lang.String arg, @android.annotation.Nullable
    android.os.Bundle extras) throws android.os.RemoteException {
        com.android.internal.util.Preconditions.checkNotNull(authority, "authority");
        com.android.internal.util.Preconditions.checkNotNull(method, "method");
        beforeRemote();
        try {
            return mContentProvider.call(mPackageName, authority, method, arg, extras);
        } catch (android.os.DeadObjectException e) {
            if (!mStable) {
                mContentResolver.unstableProviderDied(mContentProvider);
            }
            throw e;
        } finally {
            afterRemote();
        }
    }

    /**
     * Closes this client connection, indicating to the system that the
     * underlying {@link ContentProvider} is no longer needed.
     */
    @java.lang.Override
    public void close() {
        closeInternal();
    }

    /**
     *
     *
     * @deprecated replaced by {@link #close()}.
     */
    @java.lang.Deprecated
    public boolean release() {
        return closeInternal();
    }

    private boolean closeInternal() {
        mCloseGuard.close();
        if (mClosed.compareAndSet(false, true)) {
            // We can't do ANR checks after we cease to exist! Reset any
            // blocking behavior changes we might have made.
            setDetectNotResponding(0);
            if (mStable) {
                return mContentResolver.releaseProvider(mContentProvider);
            } else {
                return mContentResolver.releaseUnstableProvider(mContentProvider);
            }
        } else {
            return false;
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            if (mCloseGuard != null) {
                mCloseGuard.warnIfOpen();
            }
            close();
        } finally {
            super.finalize();
        }
    }

    /**
     * Get a reference to the {@link ContentProvider} that is associated with this
     * client. If the {@link ContentProvider} is running in a different process then
     * null will be returned. This can be used if you know you are running in the same
     * process as a provider, and want to get direct access to its implementation details.
     *
     * @return If the associated {@link ContentProvider} is local, returns it.
    Otherwise returns null.
     */
    @android.annotation.Nullable
    public android.content.ContentProvider getLocalContentProvider() {
        return android.content.ContentProvider.coerceToLocalContentProvider(mContentProvider);
    }

    /**
     * {@hide }
     */
    @java.lang.Deprecated
    public static void closeQuietly(android.content.ContentProviderClient client) {
        libcore.io.IoUtils.closeQuietly(client);
    }

    /**
     * {@hide }
     */
    @java.lang.Deprecated
    public static void releaseQuietly(android.content.ContentProviderClient client) {
        libcore.io.IoUtils.closeQuietly(client);
    }

    private class NotRespondingRunnable implements java.lang.Runnable {
        @java.lang.Override
        public void run() {
            android.util.Log.w(android.content.ContentProviderClient.TAG, "Detected provider not responding: " + mContentProvider);
            mContentResolver.appNotRespondingViaProvider(mContentProvider);
        }
    }

    private final class CursorWrapperInner extends android.database.CrossProcessCursorWrapper {
        private final dalvik.system.CloseGuard mCloseGuard = dalvik.system.CloseGuard.get();

        CursorWrapperInner(android.database.Cursor cursor) {
            super(cursor);
            mCloseGuard.open("close");
        }

        @java.lang.Override
        public void close() {
            mCloseGuard.close();
            close();
        }

        @java.lang.Override
        protected void finalize() throws java.lang.Throwable {
            try {
                if (mCloseGuard != null) {
                    mCloseGuard.warnIfOpen();
                }
                close();
            } finally {
                super.finalize();
            }
        }
    }
}

