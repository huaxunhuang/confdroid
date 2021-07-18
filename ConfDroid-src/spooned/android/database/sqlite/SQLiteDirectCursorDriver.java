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
package android.database.sqlite;


/**
 * A cursor driver that uses the given query directly.
 *
 * @unknown 
 */
public final class SQLiteDirectCursorDriver implements android.database.sqlite.SQLiteCursorDriver {
    private final android.database.sqlite.SQLiteDatabase mDatabase;

    private final java.lang.String mEditTable;

    private final java.lang.String mSql;

    private final android.os.CancellationSignal mCancellationSignal;

    private android.database.sqlite.SQLiteQuery mQuery;

    public SQLiteDirectCursorDriver(android.database.sqlite.SQLiteDatabase db, java.lang.String sql, java.lang.String editTable, android.os.CancellationSignal cancellationSignal) {
        mDatabase = db;
        mEditTable = editTable;
        mSql = sql;
        mCancellationSignal = cancellationSignal;
    }

    public android.database.Cursor query(android.database.sqlite.SQLiteDatabase.CursorFactory factory, java.lang.String[] selectionArgs) {
        final android.database.sqlite.SQLiteQuery query = new android.database.sqlite.SQLiteQuery(mDatabase, mSql, mCancellationSignal);
        final android.database.Cursor cursor;
        try {
            query.bindAllArgsAsStrings(selectionArgs);
            if (factory == null) {
                cursor = new android.database.sqlite.SQLiteCursor(this, mEditTable, query);
            } else {
                cursor = factory.newCursor(mDatabase, this, mEditTable, query);
            }
        } catch (java.lang.RuntimeException ex) {
            query.close();
            throw ex;
        }
        mQuery = query;
        return cursor;
    }

    public void cursorClosed() {
        // Do nothing
    }

    public void setBindArguments(java.lang.String[] bindArgs) {
        mQuery.bindAllArgsAsStrings(bindArgs);
    }

    public void cursorDeactivated() {
        // Do nothing
    }

    public void cursorRequeried(android.database.Cursor cursor) {
        // Do nothing
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "SQLiteDirectCursorDriver: " + mSql;
    }
}

