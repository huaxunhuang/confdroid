/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * A helper class to help make handling asynchronous {@link ContentResolver}
 * queries easier.
 */
public abstract class AsyncQueryHandler extends android.os.Handler {
    private static final java.lang.String TAG = "AsyncQuery";

    private static final boolean localLOGV = false;

    private static final int EVENT_ARG_QUERY = 1;

    private static final int EVENT_ARG_INSERT = 2;

    private static final int EVENT_ARG_UPDATE = 3;

    private static final int EVENT_ARG_DELETE = 4;

    /* package */
    final java.lang.ref.WeakReference<android.content.ContentResolver> mResolver;

    private static android.os.Looper sLooper = null;

    private android.os.Handler mWorkerThreadHandler;

    protected static final class WorkerArgs {
        public android.net.Uri uri;

        public android.os.Handler handler;

        public java.lang.String[] projection;

        public java.lang.String selection;

        public java.lang.String[] selectionArgs;

        public java.lang.String orderBy;

        public java.lang.Object result;

        public java.lang.Object cookie;

        public android.content.ContentValues values;
    }

    protected class WorkerHandler extends android.os.Handler {
        public WorkerHandler(android.os.Looper looper) {
            super(looper);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            final android.content.ContentResolver resolver = mResolver.get();
            if (resolver == null)
                return;

            android.content.AsyncQueryHandler.WorkerArgs args = ((android.content.AsyncQueryHandler.WorkerArgs) (msg.obj));
            int token = msg.what;
            int event = msg.arg1;
            switch (event) {
                case android.content.AsyncQueryHandler.EVENT_ARG_QUERY :
                    android.database.Cursor cursor;
                    try {
                        cursor = resolver.query(args.uri, args.projection, args.selection, args.selectionArgs, args.orderBy);
                        // Calling getCount() causes the cursor window to be filled,
                        // which will make the first access on the main thread a lot faster.
                        if (cursor != null) {
                            cursor.getCount();
                        }
                    } catch (java.lang.Exception e) {
                        android.util.Log.w(android.content.AsyncQueryHandler.TAG, "Exception thrown during handling EVENT_ARG_QUERY", e);
                        cursor = null;
                    }
                    args.result = cursor;
                    break;
                case android.content.AsyncQueryHandler.EVENT_ARG_INSERT :
                    args.result = resolver.insert(args.uri, args.values);
                    break;
                case android.content.AsyncQueryHandler.EVENT_ARG_UPDATE :
                    args.result = resolver.update(args.uri, args.values, args.selection, args.selectionArgs);
                    break;
                case android.content.AsyncQueryHandler.EVENT_ARG_DELETE :
                    args.result = resolver.delete(args.uri, args.selection, args.selectionArgs);
                    break;
            }
            // passing the original token value back to the caller
            // on top of the event values in arg1.
            android.os.Message reply = args.handler.obtainMessage(token);
            reply.obj = args;
            reply.arg1 = msg.arg1;
            if (android.content.AsyncQueryHandler.localLOGV) {
                android.util.Log.d(android.content.AsyncQueryHandler.TAG, (("WorkerHandler.handleMsg: msg.arg1=" + msg.arg1) + ", reply.what=") + reply.what);
            }
            reply.sendToTarget();
        }
    }

    public AsyncQueryHandler(android.content.ContentResolver cr) {
        super();
        mResolver = new java.lang.ref.WeakReference<android.content.ContentResolver>(cr);
        synchronized(android.content.AsyncQueryHandler.class) {
            if (android.content.AsyncQueryHandler.sLooper == null) {
                android.os.HandlerThread thread = new android.os.HandlerThread("AsyncQueryWorker");
                thread.start();
                android.content.AsyncQueryHandler.sLooper = thread.getLooper();
            }
        }
        mWorkerThreadHandler = createHandler(android.content.AsyncQueryHandler.sLooper);
    }

    protected android.os.Handler createHandler(android.os.Looper looper) {
        return new android.content.AsyncQueryHandler.WorkerHandler(looper);
    }

    /**
     * This method begins an asynchronous query. When the query is done
     * {@link #onQueryComplete} is called.
     *
     * @param token
     * 		A token passed into {@link #onQueryComplete} to identify
     * 		the query.
     * @param cookie
     * 		An object that gets passed into {@link #onQueryComplete}
     * @param uri
     * 		The URI, using the content:// scheme, for the content to
     * 		retrieve.
     * @param projection
     * 		A list of which columns to return. Passing null will
     * 		return all columns, which is discouraged to prevent reading data
     * 		from storage that isn't going to be used.
     * @param selection
     * 		A filter declaring which rows to return, formatted as an
     * 		SQL WHERE clause (excluding the WHERE itself). Passing null will
     * 		return all rows for the given URI.
     * @param selectionArgs
     * 		You may include ?s in selection, which will be
     * 		replaced by the values from selectionArgs, in the order that they
     * 		appear in the selection. The values will be bound as Strings.
     * @param orderBy
     * 		How to order the rows, formatted as an SQL ORDER BY
     * 		clause (excluding the ORDER BY itself). Passing null will use the
     * 		default sort order, which may be unordered.
     */
    public void startQuery(int token, java.lang.Object cookie, android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String orderBy) {
        // Use the token as what so cancelOperations works properly
        android.os.Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = android.content.AsyncQueryHandler.EVENT_ARG_QUERY;
        android.content.AsyncQueryHandler.WorkerArgs args = new android.content.AsyncQueryHandler.WorkerArgs();
        args.handler = this;
        args.uri = uri;
        args.projection = projection;
        args.selection = selection;
        args.selectionArgs = selectionArgs;
        args.orderBy = orderBy;
        args.cookie = cookie;
        msg.obj = args;
        mWorkerThreadHandler.sendMessage(msg);
    }

    /**
     * Attempts to cancel operation that has not already started. Note that
     * there is no guarantee that the operation will be canceled. They still may
     * result in a call to on[Query/Insert/Update/Delete]Complete after this
     * call has completed.
     *
     * @param token
     * 		The token representing the operation to be canceled.
     * 		If multiple operations have the same token they will all be canceled.
     */
    public final void cancelOperation(int token) {
        mWorkerThreadHandler.removeMessages(token);
    }

    /**
     * This method begins an asynchronous insert. When the insert operation is
     * done {@link #onInsertComplete} is called.
     *
     * @param token
     * 		A token passed into {@link #onInsertComplete} to identify
     * 		the insert operation.
     * @param cookie
     * 		An object that gets passed into {@link #onInsertComplete}
     * @param uri
     * 		the Uri passed to the insert operation.
     * @param initialValues
     * 		the ContentValues parameter passed to the insert operation.
     */
    public final void startInsert(int token, java.lang.Object cookie, android.net.Uri uri, android.content.ContentValues initialValues) {
        // Use the token as what so cancelOperations works properly
        android.os.Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = android.content.AsyncQueryHandler.EVENT_ARG_INSERT;
        android.content.AsyncQueryHandler.WorkerArgs args = new android.content.AsyncQueryHandler.WorkerArgs();
        args.handler = this;
        args.uri = uri;
        args.cookie = cookie;
        args.values = initialValues;
        msg.obj = args;
        mWorkerThreadHandler.sendMessage(msg);
    }

    /**
     * This method begins an asynchronous update. When the update operation is
     * done {@link #onUpdateComplete} is called.
     *
     * @param token
     * 		A token passed into {@link #onUpdateComplete} to identify
     * 		the update operation.
     * @param cookie
     * 		An object that gets passed into {@link #onUpdateComplete}
     * @param uri
     * 		the Uri passed to the update operation.
     * @param values
     * 		the ContentValues parameter passed to the update operation.
     */
    public final void startUpdate(int token, java.lang.Object cookie, android.net.Uri uri, android.content.ContentValues values, java.lang.String selection, java.lang.String[] selectionArgs) {
        // Use the token as what so cancelOperations works properly
        android.os.Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = android.content.AsyncQueryHandler.EVENT_ARG_UPDATE;
        android.content.AsyncQueryHandler.WorkerArgs args = new android.content.AsyncQueryHandler.WorkerArgs();
        args.handler = this;
        args.uri = uri;
        args.cookie = cookie;
        args.values = values;
        args.selection = selection;
        args.selectionArgs = selectionArgs;
        msg.obj = args;
        mWorkerThreadHandler.sendMessage(msg);
    }

    /**
     * This method begins an asynchronous delete. When the delete operation is
     * done {@link #onDeleteComplete} is called.
     *
     * @param token
     * 		A token passed into {@link #onDeleteComplete} to identify
     * 		the delete operation.
     * @param cookie
     * 		An object that gets passed into {@link #onDeleteComplete}
     * @param uri
     * 		the Uri passed to the delete operation.
     * @param selection
     * 		the where clause.
     */
    public final void startDelete(int token, java.lang.Object cookie, android.net.Uri uri, java.lang.String selection, java.lang.String[] selectionArgs) {
        // Use the token as what so cancelOperations works properly
        android.os.Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = android.content.AsyncQueryHandler.EVENT_ARG_DELETE;
        android.content.AsyncQueryHandler.WorkerArgs args = new android.content.AsyncQueryHandler.WorkerArgs();
        args.handler = this;
        args.uri = uri;
        args.cookie = cookie;
        args.selection = selection;
        args.selectionArgs = selectionArgs;
        msg.obj = args;
        mWorkerThreadHandler.sendMessage(msg);
    }

    /**
     * Called when an asynchronous query is completed.
     *
     * @param token
     * 		the token to identify the query, passed in from
     * 		{@link #startQuery}.
     * @param cookie
     * 		the cookie object passed in from {@link #startQuery}.
     * @param cursor
     * 		The cursor holding the results from the query.
     */
    protected void onQueryComplete(int token, java.lang.Object cookie, android.database.Cursor cursor) {
        // Empty
    }

    /**
     * Called when an asynchronous insert is completed.
     *
     * @param token
     * 		the token to identify the query, passed in from
     * 		{@link #startInsert}.
     * @param cookie
     * 		the cookie object that's passed in from
     * 		{@link #startInsert}.
     * @param uri
     * 		the uri returned from the insert operation.
     */
    protected void onInsertComplete(int token, java.lang.Object cookie, android.net.Uri uri) {
        // Empty
    }

    /**
     * Called when an asynchronous update is completed.
     *
     * @param token
     * 		the token to identify the query, passed in from
     * 		{@link #startUpdate}.
     * @param cookie
     * 		the cookie object that's passed in from
     * 		{@link #startUpdate}.
     * @param result
     * 		the result returned from the update operation
     */
    protected void onUpdateComplete(int token, java.lang.Object cookie, int result) {
        // Empty
    }

    /**
     * Called when an asynchronous delete is completed.
     *
     * @param token
     * 		the token to identify the query, passed in from
     * 		{@link #startDelete}.
     * @param cookie
     * 		the cookie object that's passed in from
     * 		{@link #startDelete}.
     * @param result
     * 		the result returned from the delete operation
     */
    protected void onDeleteComplete(int token, java.lang.Object cookie, int result) {
        // Empty
    }

    @java.lang.Override
    public void handleMessage(android.os.Message msg) {
        android.content.AsyncQueryHandler.WorkerArgs args = ((android.content.AsyncQueryHandler.WorkerArgs) (msg.obj));
        if (android.content.AsyncQueryHandler.localLOGV) {
            android.util.Log.d(android.content.AsyncQueryHandler.TAG, (("AsyncQueryHandler.handleMessage: msg.what=" + msg.what) + ", msg.arg1=") + msg.arg1);
        }
        int token = msg.what;
        int event = msg.arg1;
        // pass token back to caller on each callback.
        switch (event) {
            case android.content.AsyncQueryHandler.EVENT_ARG_QUERY :
                onQueryComplete(token, args.cookie, ((android.database.Cursor) (args.result)));
                break;
            case android.content.AsyncQueryHandler.EVENT_ARG_INSERT :
                onInsertComplete(token, args.cookie, ((android.net.Uri) (args.result)));
                break;
            case android.content.AsyncQueryHandler.EVENT_ARG_UPDATE :
                onUpdateComplete(token, args.cookie, ((java.lang.Integer) (args.result)));
                break;
            case android.content.AsyncQueryHandler.EVENT_ARG_DELETE :
                onDeleteComplete(token, args.cookie, ((java.lang.Integer) (args.result)));
                break;
        }
    }
}

