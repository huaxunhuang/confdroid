/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.support.v4.content;


/**
 * Static library support version of the framework's {@link android.content.CursorLoader}.
 * Used to write apps that run on platforms prior to Android 3.0.  When running
 * on Android 3.0 or above, this implementation is still used; it does not try
 * to switch to the framework's implementation.  See the framework SDK
 * documentation for a class overview.
 */
public class CursorLoader extends android.support.v4.content.AsyncTaskLoader<android.database.Cursor> {
    final android.support.v4.content.Loader<android.database.Cursor>.ForceLoadContentObserver mObserver;

    android.net.Uri mUri;

    java.lang.String[] mProjection;

    java.lang.String mSelection;

    java.lang.String[] mSelectionArgs;

    java.lang.String mSortOrder;

    android.database.Cursor mCursor;

    android.support.v4.os.CancellationSignal mCancellationSignal;

    /* Runs on a worker thread */
    @java.lang.Override
    public android.database.Cursor loadInBackground() {
        synchronized(this) {
            if (isLoadInBackgroundCanceled()) {
                throw new android.support.v4.os.OperationCanceledException();
            }
            mCancellationSignal = new android.support.v4.os.CancellationSignal();
        }
        try {
            android.database.Cursor cursor = android.support.v4.content.ContentResolverCompat.query(getContext().getContentResolver(), mUri, mProjection, mSelection, mSelectionArgs, mSortOrder, mCancellationSignal);
            if (cursor != null) {
                try {
                    // Ensure the cursor window is filled.
                    cursor.getCount();
                    cursor.registerContentObserver(mObserver);
                } catch (java.lang.RuntimeException ex) {
                    cursor.close();
                    throw ex;
                }
            }
            return cursor;
        } finally {
            synchronized(this) {
                mCancellationSignal = null;
            }
        }
    }

    @java.lang.Override
    public void cancelLoadInBackground() {
        super.cancelLoadInBackground();
        synchronized(this) {
            if (mCancellationSignal != null) {
                mCancellationSignal.cancel();
            }
        }
    }

    /* Runs on the UI thread */
    @java.lang.Override
    public void deliverResult(android.database.Cursor cursor) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        android.database.Cursor oldCursor = mCursor;
        mCursor = cursor;
        if (isStarted()) {
            super.deliverResult(cursor);
        }
        if (((oldCursor != null) && (oldCursor != cursor)) && (!oldCursor.isClosed())) {
            oldCursor.close();
        }
    }

    /**
     * Creates an empty unspecified CursorLoader.  You must follow this with
     * calls to {@link #setUri(Uri)}, {@link #setSelection(String)}, etc
     * to specify the query to perform.
     */
    public CursorLoader(android.content.Context context) {
        super(context);
        mObserver = new ForceLoadContentObserver();
    }

    /**
     * Creates a fully-specified CursorLoader.  See
     * {@link ContentResolver#query(Uri, String[], String, String[], String)
     * ContentResolver.query()} for documentation on the meaning of the
     * parameters.  These will be passed as-is to that call.
     */
    public CursorLoader(android.content.Context context, android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder) {
        super(context);
        mObserver = new ForceLoadContentObserver();
        mUri = uri;
        mProjection = projection;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mSortOrder = sortOrder;
    }

    /**
     * Starts an asynchronous load of the contacts list data. When the result is ready the callbacks
     * will be called on the UI thread. If a previous load has been completed and is still valid
     * the result may be passed to the callbacks immediately.
     *
     * Must be called from the UI thread
     */
    @java.lang.Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || (mCursor == null)) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread
     */
    @java.lang.Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @java.lang.Override
    public void onCanceled(android.database.Cursor cursor) {
        if ((cursor != null) && (!cursor.isClosed())) {
            cursor.close();
        }
    }

    @java.lang.Override
    protected void onReset() {
        super.onReset();
        // Ensure the loader is stopped
        onStopLoading();
        if ((mCursor != null) && (!mCursor.isClosed())) {
            mCursor.close();
        }
        mCursor = null;
    }

    public android.net.Uri getUri() {
        return mUri;
    }

    public void setUri(android.net.Uri uri) {
        mUri = uri;
    }

    public java.lang.String[] getProjection() {
        return mProjection;
    }

    public void setProjection(java.lang.String[] projection) {
        mProjection = projection;
    }

    public java.lang.String getSelection() {
        return mSelection;
    }

    public void setSelection(java.lang.String selection) {
        mSelection = selection;
    }

    public java.lang.String[] getSelectionArgs() {
        return mSelectionArgs;
    }

    public void setSelectionArgs(java.lang.String[] selectionArgs) {
        mSelectionArgs = selectionArgs;
    }

    public java.lang.String getSortOrder() {
        return mSortOrder;
    }

    public void setSortOrder(java.lang.String sortOrder) {
        mSortOrder = sortOrder;
    }

    @java.lang.Override
    public void dump(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        super.dump(prefix, fd, writer, args);
        writer.print(prefix);
        writer.print("mUri=");
        writer.println(mUri);
        writer.print(prefix);
        writer.print("mProjection=");
        writer.println(java.util.Arrays.toString(mProjection));
        writer.print(prefix);
        writer.print("mSelection=");
        writer.println(mSelection);
        writer.print(prefix);
        writer.print("mSelectionArgs=");
        writer.println(java.util.Arrays.toString(mSelectionArgs));
        writer.print(prefix);
        writer.print("mSortOrder=");
        writer.println(mSortOrder);
        writer.print(prefix);
        writer.print("mCursor=");
        writer.println(mCursor);
        writer.print(prefix);
        writer.print("mContentChanged=");
        writer.println(mContentChanged);
    }
}

