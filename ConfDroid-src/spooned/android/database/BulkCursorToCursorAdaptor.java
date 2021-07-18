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
package android.database;


/**
 * Adapts an {@link IBulkCursor} to a {@link Cursor} for use in the local process.
 *
 * {@hide }
 */
public final class BulkCursorToCursorAdaptor extends android.database.AbstractWindowedCursor {
    private static final java.lang.String TAG = "BulkCursor";

    private android.database.AbstractCursor.SelfContentObserver mObserverBridge = new android.database.AbstractCursor.SelfContentObserver(this);

    private android.database.IBulkCursor mBulkCursor;

    private java.lang.String[] mColumns;

    private boolean mWantsAllOnMoveCalls;

    private int mCount;

    /**
     * Initializes the adaptor.
     * Must be called before first use.
     */
    public void initialize(android.database.BulkCursorDescriptor d) {
        mBulkCursor = d.cursor;
        mColumns = d.columnNames;
        mWantsAllOnMoveCalls = d.wantsAllOnMoveCalls;
        mCount = d.count;
        if (d.window != null) {
            setWindow(d.window);
        }
    }

    /**
     * Gets a SelfDataChangeOberserver that can be sent to a remote
     * process to receive change notifications over IPC.
     *
     * @return A SelfContentObserver hooked up to this Cursor
     */
    public android.database.IContentObserver getObserver() {
        return mObserverBridge.getContentObserver();
    }

    private void throwIfCursorIsClosed() {
        if (mBulkCursor == null) {
            throw new android.database.StaleDataException("Attempted to access a cursor after it has been closed.");
        }
    }

    @java.lang.Override
    public int getCount() {
        throwIfCursorIsClosed();
        return mCount;
    }

    @java.lang.Override
    public boolean onMove(int oldPosition, int newPosition) {
        throwIfCursorIsClosed();
        try {
            // Make sure we have the proper window
            if (((mWindow == null) || (newPosition < mWindow.getStartPosition())) || (newPosition >= (mWindow.getStartPosition() + mWindow.getNumRows()))) {
                setWindow(mBulkCursor.getWindow(newPosition));
            } else
                if (mWantsAllOnMoveCalls) {
                    mBulkCursor.onMove(newPosition);
                }

        } catch (android.os.RemoteException ex) {
            // We tried to get a window and failed
            android.util.Log.e(android.database.BulkCursorToCursorAdaptor.TAG, "Unable to get window because the remote process is dead");
            return false;
        }
        // Couldn't obtain a window, something is wrong
        if (mWindow == null) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public void deactivate() {
        // This will call onInvalidated(), so make sure to do it before calling release,
        // which is what actually makes the data set invalid.
        super.deactivate();
        if (mBulkCursor != null) {
            try {
                mBulkCursor.deactivate();
            } catch (android.os.RemoteException ex) {
                android.util.Log.w(android.database.BulkCursorToCursorAdaptor.TAG, "Remote process exception when deactivating");
            }
        }
    }

    @java.lang.Override
    public void close() {
        super.close();
        if (mBulkCursor != null) {
            try {
                mBulkCursor.close();
            } catch (android.os.RemoteException ex) {
                android.util.Log.w(android.database.BulkCursorToCursorAdaptor.TAG, "Remote process exception when closing");
            } finally {
                mBulkCursor = null;
            }
        }
    }

    @java.lang.Override
    public boolean requery() {
        throwIfCursorIsClosed();
        try {
            mCount = mBulkCursor.requery(getObserver());
            if (mCount != (-1)) {
                mPos = -1;
                closeWindow();
                // super.requery() will call onChanged. Do it here instead of relying on the
                // observer from the far side so that observers can see a correct value for mCount
                // when responding to onChanged.
                super.requery();
                return true;
            } else {
                deactivate();
                return false;
            }
        } catch (java.lang.Exception ex) {
            android.util.Log.e(android.database.BulkCursorToCursorAdaptor.TAG, "Unable to requery because the remote process exception " + ex.getMessage());
            deactivate();
            return false;
        }
    }

    @java.lang.Override
    public java.lang.String[] getColumnNames() {
        throwIfCursorIsClosed();
        return mColumns;
    }

    @java.lang.Override
    public android.os.Bundle getExtras() {
        throwIfCursorIsClosed();
        try {
            return mBulkCursor.getExtras();
        } catch (android.os.RemoteException e) {
            // This should never happen because the system kills processes that are using remote
            // cursors when the provider process is killed.
            throw new java.lang.RuntimeException(e);
        }
    }

    @java.lang.Override
    public android.os.Bundle respond(android.os.Bundle extras) {
        throwIfCursorIsClosed();
        try {
            return mBulkCursor.respond(extras);
        } catch (android.os.RemoteException e) {
            // the system kills processes that are using remote cursors when the provider process
            // is killed, but this can still happen if this is being called from the system process,
            // so, better to log and return an empty bundle.
            android.util.Log.w(android.database.BulkCursorToCursorAdaptor.TAG, "respond() threw RemoteException, returning an empty bundle.", e);
            return android.os.Bundle.EMPTY;
        }
    }
}

