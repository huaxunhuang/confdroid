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
 * Represents a query that reads the resulting rows into a {@link SQLiteQuery}.
 * This class is used by {@link SQLiteCursor} and isn't useful itself.
 * <p>
 * This class is not thread-safe.
 * </p>
 */
public final class SQLiteQuery extends android.database.sqlite.SQLiteProgram {
    private static final java.lang.String TAG = "SQLiteQuery";

    private final android.os.CancellationSignal mCancellationSignal;

    SQLiteQuery(android.database.sqlite.SQLiteDatabase db, java.lang.String query, android.os.CancellationSignal cancellationSignal) {
        super(db, query, null, cancellationSignal);
        mCancellationSignal = cancellationSignal;
    }

    /**
     * Reads rows into a buffer.
     *
     * @param window
     * 		The window to fill into
     * @param startPos
     * 		The start position for filling the window.
     * @param requiredPos
     * 		The position of a row that MUST be in the window.
     * 		If it won't fit, then the query should discard part of what it filled.
     * @param countAllRows
     * 		True to count all rows that the query would
     * 		return regardless of whether they fit in the window.
     * @return Number of rows that were enumerated.  Might not be all rows
    unless countAllRows is true.
     * @throws SQLiteException
     * 		if an error occurs.
     * @throws OperationCanceledException
     * 		if the operation was canceled.
     */
    int fillWindow(android.database.CursorWindow window, int startPos, int requiredPos, boolean countAllRows) {
        acquireReference();
        try {
            window.acquireReference();
            try {
                int numRows = getSession().executeForCursorWindow(getSql(), getBindArgs(), window, startPos, requiredPos, countAllRows, getConnectionFlags(), mCancellationSignal);
                return numRows;
            } catch (android.database.sqlite.SQLiteDatabaseCorruptException ex) {
                onCorruption();
                throw ex;
            } catch (android.database.sqlite.SQLiteException ex) {
                android.util.Log.e(android.database.sqlite.SQLiteQuery.TAG, (("exception: " + ex.getMessage()) + "; query: ") + getSql());
                throw ex;
            } finally {
                window.releaseReference();
            }
        } finally {
            releaseReference();
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "SQLiteQuery: " + getSql();
    }
}

