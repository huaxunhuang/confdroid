/**
 * Copyright (C) 2019 The Android Open Source Project
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
 * Instance of {@link ContentInterface} that logs all inputs and outputs while
 * delegating to another {@link ContentInterface}.
 *
 * @unknown 
 */
public class LoggingContentInterface implements android.content.ContentInterface {
    private final java.lang.String tag;

    private final android.content.ContentInterface delegate;

    public LoggingContentInterface(java.lang.String tag, android.content.ContentInterface delegate) {
        this.tag = tag;
        this.delegate = delegate;
    }

    private class Logger implements java.lang.AutoCloseable {
        private final java.lang.StringBuilder sb = new java.lang.StringBuilder();

        public Logger(java.lang.String method, java.lang.Object... args) {
            // First, force-unparcel any bundles so we can log them
            for (java.lang.Object arg : args) {
                if (arg instanceof android.os.Bundle) {
                    ((android.os.Bundle) (arg)).size();
                }
            }
            sb.append("callingUid=").append(android.os.Binder.getCallingUid()).append(' ');
            sb.append(method);
            sb.append('(').append(deepToString(args)).append(')');
        }

        private java.lang.String deepToString(java.lang.Object value) {
            if ((value != null) && value.getClass().isArray()) {
                return java.util.Arrays.deepToString(((java.lang.Object[]) (value)));
            } else {
                return java.lang.String.valueOf(value);
            }
        }

        public <T> T setResult(T res) {
            if (res instanceof android.database.Cursor) {
                sb.append('\n');
                android.database.DatabaseUtils.dumpCursor(((android.database.Cursor) (res)), sb);
            } else {
                sb.append(" = ").append(deepToString(res));
            }
            return res;
        }

        @java.lang.Override
        public void close() {
            android.util.Log.v(tag, sb.toString());
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.database.Cursor query(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    java.lang.String[] projection, @android.annotation.Nullable
    android.os.Bundle queryArgs, @android.annotation.Nullable
    android.os.CancellationSignal cancellationSignal) throws android.os.RemoteException {
        try (android.content.LoggingContentInterface.Logger l = new android.content.LoggingContentInterface.Logger("query", uri, projection, queryArgs, cancellationSignal)) {
            try {
                return l.setResult(delegate.query(uri, projection, queryArgs, cancellationSignal));
            } catch (java.lang.Exception res) {
                l.setResult(res);
                throw res;
            }
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public java.lang.String getType(@android.annotation.NonNull
    android.net.Uri uri) throws android.os.RemoteException {
        try (android.content.LoggingContentInterface.Logger l = new android.content.LoggingContentInterface.Logger("getType", uri)) {
            try {
                return l.setResult(delegate.getType(uri));
            } catch (java.lang.Exception res) {
                l.setResult(res);
                throw res;
            }
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public java.lang.String[] getStreamTypes(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mimeTypeFilter) throws android.os.RemoteException {
        try (android.content.LoggingContentInterface.Logger l = new android.content.LoggingContentInterface.Logger("getStreamTypes", uri, mimeTypeFilter)) {
            try {
                return l.setResult(delegate.getStreamTypes(uri, mimeTypeFilter));
            } catch (java.lang.Exception res) {
                l.setResult(res);
                throw res;
            }
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.net.Uri canonicalize(@android.annotation.NonNull
    android.net.Uri uri) throws android.os.RemoteException {
        try (android.content.LoggingContentInterface.Logger l = new android.content.LoggingContentInterface.Logger("canonicalize", uri)) {
            try {
                return l.setResult(delegate.canonicalize(uri));
            } catch (java.lang.Exception res) {
                l.setResult(res);
                throw res;
            }
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.net.Uri uncanonicalize(@android.annotation.NonNull
    android.net.Uri uri) throws android.os.RemoteException {
        try (android.content.LoggingContentInterface.Logger l = new android.content.LoggingContentInterface.Logger("uncanonicalize", uri)) {
            try {
                return l.setResult(delegate.uncanonicalize(uri));
            } catch (java.lang.Exception res) {
                l.setResult(res);
                throw res;
            }
        }
    }

    @java.lang.Override
    public boolean refresh(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    android.os.Bundle args, @android.annotation.Nullable
    android.os.CancellationSignal cancellationSignal) throws android.os.RemoteException {
        try (android.content.LoggingContentInterface.Logger l = new android.content.LoggingContentInterface.Logger("refresh", uri, args, cancellationSignal)) {
            try {
                return l.setResult(delegate.refresh(uri, args, cancellationSignal));
            } catch (java.lang.Exception res) {
                l.setResult(res);
                throw res;
            }
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.net.Uri insert(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    android.content.ContentValues initialValues) throws android.os.RemoteException {
        try (android.content.LoggingContentInterface.Logger l = new android.content.LoggingContentInterface.Logger("insert", uri, initialValues)) {
            try {
                return l.setResult(delegate.insert(uri, initialValues));
            } catch (java.lang.Exception res) {
                l.setResult(res);
                throw res;
            }
        }
    }

    @java.lang.Override
    public int bulkInsert(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    android.content.ContentValues[] initialValues) throws android.os.RemoteException {
        try (android.content.LoggingContentInterface.Logger l = new android.content.LoggingContentInterface.Logger("bulkInsert", uri, initialValues)) {
            try {
                return l.setResult(delegate.bulkInsert(uri, initialValues));
            } catch (java.lang.Exception res) {
                l.setResult(res);
                throw res;
            }
        }
    }

    @java.lang.Override
    public int delete(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    java.lang.String selection, @android.annotation.Nullable
    java.lang.String[] selectionArgs) throws android.os.RemoteException {
        try (android.content.LoggingContentInterface.Logger l = new android.content.LoggingContentInterface.Logger("delete", uri, selection, selectionArgs)) {
            try {
                return l.setResult(delegate.delete(uri, selection, selectionArgs));
            } catch (java.lang.Exception res) {
                l.setResult(res);
                throw res;
            }
        }
    }

    @java.lang.Override
    public int update(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    android.content.ContentValues values, @android.annotation.Nullable
    java.lang.String selection, @android.annotation.Nullable
    java.lang.String[] selectionArgs) throws android.os.RemoteException {
        try (android.content.LoggingContentInterface.Logger l = new android.content.LoggingContentInterface.Logger("update", uri, values, selection, selectionArgs)) {
            try {
                return l.setResult(delegate.update(uri, values, selection, selectionArgs));
            } catch (java.lang.Exception res) {
                l.setResult(res);
                throw res;
            }
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.os.ParcelFileDescriptor openFile(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mode, @android.annotation.Nullable
    android.os.CancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException {
        try (android.content.LoggingContentInterface.Logger l = new android.content.LoggingContentInterface.Logger("openFile", uri, mode, signal)) {
            try {
                return l.setResult(delegate.openFile(uri, mode, signal));
            } catch (java.lang.Exception res) {
                l.setResult(res);
                throw res;
            }
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.content.res.AssetFileDescriptor openAssetFile(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mode, @android.annotation.Nullable
    android.os.CancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException {
        try (android.content.LoggingContentInterface.Logger l = new android.content.LoggingContentInterface.Logger("openAssetFile", uri, mode, signal)) {
            try {
                return l.setResult(delegate.openAssetFile(uri, mode, signal));
            } catch (java.lang.Exception res) {
                l.setResult(res);
                throw res;
            }
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.content.res.AssetFileDescriptor openTypedAssetFile(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mimeTypeFilter, @android.annotation.Nullable
    android.os.Bundle opts, @android.annotation.Nullable
    android.os.CancellationSignal signal) throws android.os.RemoteException, java.io.FileNotFoundException {
        try (android.content.LoggingContentInterface.Logger l = new android.content.LoggingContentInterface.Logger("openTypedAssetFile", uri, mimeTypeFilter, opts, signal)) {
            try {
                return l.setResult(delegate.openTypedAssetFile(uri, mimeTypeFilter, opts, signal));
            } catch (java.lang.Exception res) {
                l.setResult(res);
                throw res;
            }
        }
    }

    @java.lang.Override
    @android.annotation.NonNull
    public android.content.ContentProviderResult[] applyBatch(@android.annotation.NonNull
    java.lang.String authority, @android.annotation.NonNull
    java.util.ArrayList<android.content.ContentProviderOperation> operations) throws android.content.OperationApplicationException, android.os.RemoteException {
        try (android.content.LoggingContentInterface.Logger l = new android.content.LoggingContentInterface.Logger("applyBatch", authority, operations)) {
            try {
                return l.setResult(delegate.applyBatch(authority, operations));
            } catch (java.lang.Exception res) {
                l.setResult(res);
                throw res;
            }
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public android.os.Bundle call(@android.annotation.NonNull
    java.lang.String authority, @android.annotation.NonNull
    java.lang.String method, @android.annotation.Nullable
    java.lang.String arg, @android.annotation.Nullable
    android.os.Bundle extras) throws android.os.RemoteException {
        try (android.content.LoggingContentInterface.Logger l = new android.content.LoggingContentInterface.Logger("call", authority, method, arg, extras)) {
            try {
                return l.setResult(delegate.call(authority, method, arg, extras));
            } catch (java.lang.Exception res) {
                l.setResult(res);
                throw res;
            }
        }
    }
}

