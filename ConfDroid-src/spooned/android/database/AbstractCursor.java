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
 * This is an abstract cursor class that handles a lot of the common code
 * that all cursors need to deal with and is provided for convenience reasons.
 */
public abstract class AbstractCursor implements android.database.CrossProcessCursor {
    private static final java.lang.String TAG = "Cursor";

    /**
     *
     *
     * @unknown This field should not be used.
     */
    protected java.util.HashMap<java.lang.Long, java.util.Map<java.lang.String, java.lang.Object>> mUpdatedRows;

    /**
     *
     *
     * @unknown This field should not be used.
     */
    protected int mRowIdColumnIndex;

    /**
     *
     *
     * @unknown This field should not be used.
     */
    protected java.lang.Long mCurrentRowID;

    /**
     *
     *
     * @deprecated Use {@link #getPosition()} instead.
     */
    @java.lang.Deprecated
    protected int mPos;

    /**
     *
     *
     * @deprecated Use {@link #isClosed()} instead.
     */
    @java.lang.Deprecated
    protected boolean mClosed;

    /**
     *
     *
     * @deprecated Do not use.
     */
    @java.lang.Deprecated
    protected android.content.ContentResolver mContentResolver;

    private android.net.Uri mNotifyUri;

    private final java.lang.Object mSelfObserverLock = new java.lang.Object();

    private android.database.ContentObserver mSelfObserver;

    private boolean mSelfObserverRegistered;

    private final android.database.DataSetObservable mDataSetObservable = new android.database.DataSetObservable();

    private final android.database.ContentObservable mContentObservable = new android.database.ContentObservable();

    private android.os.Bundle mExtras = android.os.Bundle.EMPTY;

    /* -------------------------------------------------------- */
    /* These need to be implemented by subclasses */
    @java.lang.Override
    public abstract int getCount();

    @java.lang.Override
    public abstract java.lang.String[] getColumnNames();

    @java.lang.Override
    public abstract java.lang.String getString(int column);

    @java.lang.Override
    public abstract short getShort(int column);

    @java.lang.Override
    public abstract int getInt(int column);

    @java.lang.Override
    public abstract long getLong(int column);

    @java.lang.Override
    public abstract float getFloat(int column);

    @java.lang.Override
    public abstract double getDouble(int column);

    @java.lang.Override
    public abstract boolean isNull(int column);

    @java.lang.Override
    public int getType(int column) {
        // Reflects the assumption that all commonly used field types (meaning everything
        // but blobs) are convertible to strings so it should be safe to call
        // getString to retrieve them.
        return android.database.Cursor.FIELD_TYPE_STRING;
    }

    // TODO implement getBlob in all cursor types
    @java.lang.Override
    public byte[] getBlob(int column) {
        throw new java.lang.UnsupportedOperationException("getBlob is not supported");
    }

    /* -------------------------------------------------------- */
    /* Methods that may optionally be implemented by subclasses */
    /**
     * If the cursor is backed by a {@link CursorWindow}, returns a pre-filled
     * window with the contents of the cursor, otherwise null.
     *
     * @return The pre-filled window that backs this cursor, or null if none.
     */
    @java.lang.Override
    public android.database.CursorWindow getWindow() {
        return null;
    }

    @java.lang.Override
    public int getColumnCount() {
        return getColumnNames().length;
    }

    @java.lang.Override
    public void deactivate() {
        onDeactivateOrClose();
    }

    /**
     *
     *
     * @unknown 
     */
    protected void onDeactivateOrClose() {
        if (mSelfObserver != null) {
            mContentResolver.unregisterContentObserver(mSelfObserver);
            mSelfObserverRegistered = false;
        }
        mDataSetObservable.notifyInvalidated();
    }

    @java.lang.Override
    public boolean requery() {
        if ((mSelfObserver != null) && (mSelfObserverRegistered == false)) {
            mContentResolver.registerContentObserver(mNotifyUri, true, mSelfObserver);
            mSelfObserverRegistered = true;
        }
        mDataSetObservable.notifyChanged();
        return true;
    }

    @java.lang.Override
    public boolean isClosed() {
        return mClosed;
    }

    @java.lang.Override
    public void close() {
        mClosed = true;
        mContentObservable.unregisterAll();
        onDeactivateOrClose();
    }

    /**
     * This function is called every time the cursor is successfully scrolled
     * to a new position, giving the subclass a chance to update any state it
     * may have. If it returns false the move function will also do so and the
     * cursor will scroll to the beforeFirst position.
     *
     * @param oldPosition
     * 		the position that we're moving from
     * @param newPosition
     * 		the position that we're moving to
     * @return true if the move is successful, false otherwise
     */
    @java.lang.Override
    public boolean onMove(int oldPosition, int newPosition) {
        return true;
    }

    @java.lang.Override
    public void copyStringToBuffer(int columnIndex, android.database.CharArrayBuffer buffer) {
        // Default implementation, uses getString
        java.lang.String result = getString(columnIndex);
        if (result != null) {
            char[] data = buffer.data;
            if ((data == null) || (data.length < result.length())) {
                buffer.data = result.toCharArray();
            } else {
                result.getChars(0, result.length(), data, 0);
            }
            buffer.sizeCopied = result.length();
        } else {
            buffer.sizeCopied = 0;
        }
    }

    /* -------------------------------------------------------- */
    /* Implementation */
    public AbstractCursor() {
        mPos = -1;
    }

    @java.lang.Override
    public final int getPosition() {
        return mPos;
    }

    @java.lang.Override
    public final boolean moveToPosition(int position) {
        // Make sure position isn't past the end of the cursor
        final int count = getCount();
        if (position >= count) {
            mPos = count;
            return false;
        }
        // Make sure position isn't before the beginning of the cursor
        if (position < 0) {
            mPos = -1;
            return false;
        }
        // Check for no-op moves, and skip the rest of the work for them
        if (position == mPos) {
            return true;
        }
        boolean result = onMove(mPos, position);
        if (result == false) {
            mPos = -1;
        } else {
            mPos = position;
        }
        return result;
    }

    @java.lang.Override
    public void fillWindow(int position, android.database.CursorWindow window) {
        android.database.DatabaseUtils.cursorFillWindow(this, position, window);
    }

    @java.lang.Override
    public final boolean move(int offset) {
        return moveToPosition(mPos + offset);
    }

    @java.lang.Override
    public final boolean moveToFirst() {
        return moveToPosition(0);
    }

    @java.lang.Override
    public final boolean moveToLast() {
        return moveToPosition(getCount() - 1);
    }

    @java.lang.Override
    public final boolean moveToNext() {
        return moveToPosition(mPos + 1);
    }

    @java.lang.Override
    public final boolean moveToPrevious() {
        return moveToPosition(mPos - 1);
    }

    @java.lang.Override
    public final boolean isFirst() {
        return (mPos == 0) && (getCount() != 0);
    }

    @java.lang.Override
    public final boolean isLast() {
        int cnt = getCount();
        return (mPos == (cnt - 1)) && (cnt != 0);
    }

    @java.lang.Override
    public final boolean isBeforeFirst() {
        if (getCount() == 0) {
            return true;
        }
        return mPos == (-1);
    }

    @java.lang.Override
    public final boolean isAfterLast() {
        if (getCount() == 0) {
            return true;
        }
        return mPos == getCount();
    }

    @java.lang.Override
    public int getColumnIndex(java.lang.String columnName) {
        // Hack according to bug 903852
        final int periodIndex = columnName.lastIndexOf('.');
        if (periodIndex != (-1)) {
            java.lang.Exception e = new java.lang.Exception();
            android.util.Log.e(android.database.AbstractCursor.TAG, "requesting column name with table name -- " + columnName, e);
            columnName = columnName.substring(periodIndex + 1);
        }
        java.lang.String[] columnNames = getColumnNames();
        int length = columnNames.length;
        for (int i = 0; i < length; i++) {
            if (columnNames[i].equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        if (false) {
            if (getCount() > 0) {
                android.util.Log.w("AbstractCursor", "Unknown column " + columnName);
            }
        }
        return -1;
    }

    @java.lang.Override
    public int getColumnIndexOrThrow(java.lang.String columnName) {
        final int index = getColumnIndex(columnName);
        if (index < 0) {
            throw new java.lang.IllegalArgumentException(("column '" + columnName) + "' does not exist");
        }
        return index;
    }

    @java.lang.Override
    public java.lang.String getColumnName(int columnIndex) {
        return getColumnNames()[columnIndex];
    }

    @java.lang.Override
    public void registerContentObserver(android.database.ContentObserver observer) {
        mContentObservable.registerObserver(observer);
    }

    @java.lang.Override
    public void unregisterContentObserver(android.database.ContentObserver observer) {
        // cursor will unregister all observers when it close
        if (!mClosed) {
            mContentObservable.unregisterObserver(observer);
        }
    }

    @java.lang.Override
    public void registerDataSetObserver(android.database.DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @java.lang.Override
    public void unregisterDataSetObserver(android.database.DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    /**
     * Subclasses must call this method when they finish committing updates to notify all
     * observers.
     *
     * @param selfChange
     * 		
     */
    protected void onChange(boolean selfChange) {
        synchronized(mSelfObserverLock) {
            mContentObservable.dispatchChange(selfChange, null);
            if ((mNotifyUri != null) && selfChange) {
                mContentResolver.notifyChange(mNotifyUri, mSelfObserver);
            }
        }
    }

    /**
     * Specifies a content URI to watch for changes.
     *
     * @param cr
     * 		The content resolver from the caller's context.
     * @param notifyUri
     * 		The URI to watch for changes. This can be a
     * 		specific row URI, or a base URI for a whole class of content.
     */
    @java.lang.Override
    public void setNotificationUri(android.content.ContentResolver cr, android.net.Uri notifyUri) {
        setNotificationUri(cr, notifyUri, android.os.UserHandle.myUserId());
    }

    /**
     *
     *
     * @unknown - set the notification uri but with an observer for a particular user's view
     */
    public void setNotificationUri(android.content.ContentResolver cr, android.net.Uri notifyUri, int userHandle) {
        synchronized(mSelfObserverLock) {
            mNotifyUri = notifyUri;
            mContentResolver = cr;
            if (mSelfObserver != null) {
                mContentResolver.unregisterContentObserver(mSelfObserver);
            }
            mSelfObserver = new android.database.AbstractCursor.SelfContentObserver(this);
            mContentResolver.registerContentObserver(mNotifyUri, true, mSelfObserver, userHandle);
            mSelfObserverRegistered = true;
        }
    }

    @java.lang.Override
    public android.net.Uri getNotificationUri() {
        synchronized(mSelfObserverLock) {
            return mNotifyUri;
        }
    }

    @java.lang.Override
    public boolean getWantsAllOnMoveCalls() {
        return false;
    }

    @java.lang.Override
    public void setExtras(android.os.Bundle extras) {
        mExtras = (extras == null) ? android.os.Bundle.EMPTY : extras;
    }

    @java.lang.Override
    public android.os.Bundle getExtras() {
        return mExtras;
    }

    @java.lang.Override
    public android.os.Bundle respond(android.os.Bundle extras) {
        return android.os.Bundle.EMPTY;
    }

    /**
     *
     *
     * @deprecated Always returns false since Cursors do not support updating rows
     */
    @java.lang.Deprecated
    protected boolean isFieldUpdated(int columnIndex) {
        return false;
    }

    /**
     *
     *
     * @deprecated Always returns null since Cursors do not support updating rows
     */
    @java.lang.Deprecated
    protected java.lang.Object getUpdatedField(int columnIndex) {
        return null;
    }

    /**
     * This function throws CursorIndexOutOfBoundsException if
     * the cursor position is out of bounds. Subclass implementations of
     * the get functions should call this before attempting
     * to retrieve data.
     *
     * @throws CursorIndexOutOfBoundsException
     * 		
     */
    protected void checkPosition() {
        if (((-1) == mPos) || (getCount() == mPos)) {
            throw new android.database.CursorIndexOutOfBoundsException(mPos, getCount());
        }
    }

    @java.lang.Override
    protected void finalize() {
        if ((mSelfObserver != null) && (mSelfObserverRegistered == true)) {
            mContentResolver.unregisterContentObserver(mSelfObserver);
        }
        try {
            if (!mClosed)
                close();

        } catch (java.lang.Exception e) {
        }
    }

    /**
     * Cursors use this class to track changes others make to their URI.
     */
    protected static class SelfContentObserver extends android.database.ContentObserver {
        java.lang.ref.WeakReference<android.database.AbstractCursor> mCursor;

        public SelfContentObserver(android.database.AbstractCursor cursor) {
            super(null);
            mCursor = new java.lang.ref.WeakReference<android.database.AbstractCursor>(cursor);
        }

        @java.lang.Override
        public boolean deliverSelfNotifications() {
            return false;
        }

        @java.lang.Override
        public void onChange(boolean selfChange) {
            android.database.AbstractCursor cursor = mCursor.get();
            if (cursor != null) {
                cursor.onChange(false);
            }
        }
    }
}

