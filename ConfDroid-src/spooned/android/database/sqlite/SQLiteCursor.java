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
package android.database.sqlite;


/**
 * A Cursor implementation that exposes results from a query on a
 * {@link SQLiteDatabase}.
 *
 * SQLiteCursor is not internally synchronized so code using a SQLiteCursor from multiple
 * threads should perform its own synchronization when using the SQLiteCursor.
 */
public class SQLiteCursor extends android.database.AbstractWindowedCursor {
    static final java.lang.String TAG = "SQLiteCursor";

    static final int NO_COUNT = -1;

    /**
     * The name of the table to edit
     */
    private final java.lang.String mEditTable;

    /**
     * The names of the columns in the rows
     */
    private final java.lang.String[] mColumns;

    /**
     * The query object for the cursor
     */
    private final android.database.sqlite.SQLiteQuery mQuery;

    /**
     * The compiled query this cursor came from
     */
    private final android.database.sqlite.SQLiteCursorDriver mDriver;

    /**
     * The number of rows in the cursor
     */
    private int mCount = android.database.sqlite.SQLiteCursor.NO_COUNT;

    /**
     * The number of rows that can fit in the cursor window, 0 if unknown
     */
    private int mCursorWindowCapacity;

    /**
     * A mapping of column names to column indices, to speed up lookups
     */
    private java.util.Map<java.lang.String, java.lang.Integer> mColumnNameMap;

    /**
     * Used to find out where a cursor was allocated in case it never got released.
     */
    private final java.lang.Throwable mStackTrace;

    /**
     * Execute a query and provide access to its result set through a Cursor
     * interface. For a query such as: {@code SELECT name, birth, phone FROM
     * myTable WHERE ... LIMIT 1,20 ORDER BY...} the column names (name, birth,
     * phone) would be in the projection argument and everything from
     * {@code FROM} onward would be in the params argument.
     *
     * @param db
     * 		a reference to a Database object that is already constructed
     * 		and opened. This param is not used any longer
     * @param editTable
     * 		the name of the table used for this query
     * @param query
     * 		the rest of the query terms
     * 		cursor is finalized
     * @deprecated use {@link #SQLiteCursor(SQLiteCursorDriver, String, SQLiteQuery)} instead
     */
    @java.lang.Deprecated
    public SQLiteCursor(android.database.sqlite.SQLiteDatabase db, android.database.sqlite.SQLiteCursorDriver driver, java.lang.String editTable, android.database.sqlite.SQLiteQuery query) {
        this(driver, editTable, query);
    }

    /**
     * Execute a query and provide access to its result set through a Cursor
     * interface. For a query such as: {@code SELECT name, birth, phone FROM
     * myTable WHERE ... LIMIT 1,20 ORDER BY...} the column names (name, birth,
     * phone) would be in the projection argument and everything from
     * {@code FROM} onward would be in the params argument.
     *
     * @param editTable
     * 		the name of the table used for this query
     * @param query
     * 		the {@link SQLiteQuery} object associated with this cursor object.
     */
    public SQLiteCursor(android.database.sqlite.SQLiteCursorDriver driver, java.lang.String editTable, android.database.sqlite.SQLiteQuery query) {
        if (query == null) {
            throw new java.lang.IllegalArgumentException("query object cannot be null");
        }
        if (android.os.StrictMode.vmSqliteObjectLeaksEnabled()) {
            mStackTrace = new android.database.sqlite.DatabaseObjectNotClosedException().fillInStackTrace();
        } else {
            mStackTrace = null;
        }
        mDriver = driver;
        mEditTable = editTable;
        mColumnNameMap = null;
        mQuery = query;
        mColumns = query.getColumnNames();
    }

    /**
     * Get the database that this cursor is associated with.
     *
     * @return the SQLiteDatabase that this cursor is associated with.
     */
    public android.database.sqlite.SQLiteDatabase getDatabase() {
        return mQuery.getDatabase();
    }

    @java.lang.Override
    public boolean onMove(int oldPosition, int newPosition) {
        // Make sure the row at newPosition is present in the window
        if (((mWindow == null) || (newPosition < mWindow.getStartPosition())) || (newPosition >= (mWindow.getStartPosition() + mWindow.getNumRows()))) {
            fillWindow(newPosition);
        }
        return true;
    }

    @java.lang.Override
    public int getCount() {
        if (mCount == android.database.sqlite.SQLiteCursor.NO_COUNT) {
            fillWindow(0);
        }
        return mCount;
    }

    private void fillWindow(int requiredPos) {
        clearOrCreateWindow(getDatabase().getPath());
        try {
            if (mCount == android.database.sqlite.SQLiteCursor.NO_COUNT) {
                int startPos = android.database.DatabaseUtils.cursorPickFillWindowStartPosition(requiredPos, 0);
                mCount = mQuery.fillWindow(mWindow, startPos, requiredPos, true);
                mCursorWindowCapacity = mWindow.getNumRows();
                if (android.util.Log.isLoggable(android.database.sqlite.SQLiteCursor.TAG, android.util.Log.DEBUG)) {
                    android.util.Log.d(android.database.sqlite.SQLiteCursor.TAG, "received count(*) from native_fill_window: " + mCount);
                }
            } else {
                int startPos = android.database.DatabaseUtils.cursorPickFillWindowStartPosition(requiredPos, mCursorWindowCapacity);
                mQuery.fillWindow(mWindow, startPos, requiredPos, false);
            }
        } catch (java.lang.RuntimeException ex) {
            // Close the cursor window if the query failed and therefore will
            // not produce any results.  This helps to avoid accidentally leaking
            // the cursor window if the client does not correctly handle exceptions
            // and fails to close the cursor.
            closeWindow();
            throw ex;
        }
    }

    @java.lang.Override
    public int getColumnIndex(java.lang.String columnName) {
        // Create mColumnNameMap on demand
        if (mColumnNameMap == null) {
            java.lang.String[] columns = mColumns;
            int columnCount = columns.length;
            java.util.HashMap<java.lang.String, java.lang.Integer> map = new java.util.HashMap<java.lang.String, java.lang.Integer>(columnCount, 1);
            for (int i = 0; i < columnCount; i++) {
                map.put(columns[i], i);
            }
            mColumnNameMap = map;
        }
        // Hack according to bug 903852
        final int periodIndex = columnName.lastIndexOf('.');
        if (periodIndex != (-1)) {
            java.lang.Exception e = new java.lang.Exception();
            android.util.Log.e(android.database.sqlite.SQLiteCursor.TAG, "requesting column name with table name -- " + columnName, e);
            columnName = columnName.substring(periodIndex + 1);
        }
        java.lang.Integer i = mColumnNameMap.get(columnName);
        if (i != null) {
            return i.intValue();
        } else {
            return -1;
        }
    }

    @java.lang.Override
    public java.lang.String[] getColumnNames() {
        return mColumns;
    }

    @java.lang.Override
    public void deactivate() {
        super.deactivate();
        mDriver.cursorDeactivated();
    }

    @java.lang.Override
    public void close() {
        super.close();
        synchronized(this) {
            mQuery.close();
            mDriver.cursorClosed();
        }
    }

    @java.lang.Override
    public boolean requery() {
        if (isClosed()) {
            return false;
        }
        synchronized(this) {
            if (!mQuery.getDatabase().isOpen()) {
                return false;
            }
            if (mWindow != null) {
                mWindow.clear();
            }
            mPos = -1;
            mCount = android.database.sqlite.SQLiteCursor.NO_COUNT;
            mDriver.cursorRequeried(this);
        }
        try {
            return super.requery();
        } catch (java.lang.IllegalStateException e) {
            // for backwards compatibility, just return false
            android.util.Log.w(android.database.sqlite.SQLiteCursor.TAG, "requery() failed " + e.getMessage(), e);
            return false;
        }
    }

    @java.lang.Override
    public void setWindow(android.database.CursorWindow window) {
        super.setWindow(window);
        mCount = android.database.sqlite.SQLiteCursor.NO_COUNT;
    }

    /**
     * Changes the selection arguments. The new values take effect after a call to requery().
     */
    public void setSelectionArguments(java.lang.String[] selectionArgs) {
        mDriver.setBindArguments(selectionArgs);
    }

    /**
     * Release the native resources, if they haven't been released yet.
     */
    @java.lang.Override
    protected void finalize() {
        try {
            // if the cursor hasn't been closed yet, close it first
            if (mWindow != null) {
                if (mStackTrace != null) {
                    java.lang.String sql = mQuery.getSql();
                    int len = sql.length();
                    android.os.StrictMode.onSqliteObjectLeaked(((((("Finalizing a Cursor that has not been deactivated or closed. " + "database = ") + mQuery.getDatabase().getLabel()) + ", table = ") + mEditTable) + ", query = ") + sql.substring(0, len > 1000 ? 1000 : len), mStackTrace);
                }
                close();
            }
        } finally {
            super.finalize();
        }
    }
}

