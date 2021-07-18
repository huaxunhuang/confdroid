/**
 * Copyright (C) 2008 Esmertec AG.
 * Copyright (C) 2008 The Android Open Source Project
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
 *
 *
 * @unknown 
 */
public final class SqliteWrapper {
    private static final java.lang.String TAG = "SqliteWrapper";

    private static final java.lang.String SQLITE_EXCEPTION_DETAIL_MESSAGE = "unable to open database file";

    private SqliteWrapper() {
        // Forbidden being instantiated.
    }

    // FIXME: need to optimize this method.
    private static boolean isLowMemory(android.database.sqlite.SQLiteException e) {
        return e.getMessage().equals(android.database.sqlite.SqliteWrapper.SQLITE_EXCEPTION_DETAIL_MESSAGE);
    }

    public static void checkSQLiteException(android.content.Context context, android.database.sqlite.SQLiteException e) {
        if (android.database.sqlite.SqliteWrapper.isLowMemory(e)) {
            android.widget.Toast.makeText(context, com.android.internal.R.string.low_memory, android.widget.Toast.LENGTH_SHORT).show();
        } else {
            throw e;
        }
    }

    public static android.database.Cursor query(android.content.Context context, android.content.ContentResolver resolver, android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder) {
        try {
            return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (android.database.sqlite.SQLiteException e) {
            android.util.Log.e(android.database.sqlite.SqliteWrapper.TAG, "Catch a SQLiteException when query: ", e);
            android.database.sqlite.SqliteWrapper.checkSQLiteException(context, e);
            return null;
        }
    }

    public static boolean requery(android.content.Context context, android.database.Cursor cursor) {
        try {
            return cursor.requery();
        } catch (android.database.sqlite.SQLiteException e) {
            android.util.Log.e(android.database.sqlite.SqliteWrapper.TAG, "Catch a SQLiteException when requery: ", e);
            android.database.sqlite.SqliteWrapper.checkSQLiteException(context, e);
            return false;
        }
    }

    public static int update(android.content.Context context, android.content.ContentResolver resolver, android.net.Uri uri, android.content.ContentValues values, java.lang.String where, java.lang.String[] selectionArgs) {
        try {
            return resolver.update(uri, values, where, selectionArgs);
        } catch (android.database.sqlite.SQLiteException e) {
            android.util.Log.e(android.database.sqlite.SqliteWrapper.TAG, "Catch a SQLiteException when update: ", e);
            android.database.sqlite.SqliteWrapper.checkSQLiteException(context, e);
            return -1;
        }
    }

    public static int delete(android.content.Context context, android.content.ContentResolver resolver, android.net.Uri uri, java.lang.String where, java.lang.String[] selectionArgs) {
        try {
            return resolver.delete(uri, where, selectionArgs);
        } catch (android.database.sqlite.SQLiteException e) {
            android.util.Log.e(android.database.sqlite.SqliteWrapper.TAG, "Catch a SQLiteException when delete: ", e);
            android.database.sqlite.SqliteWrapper.checkSQLiteException(context, e);
            return -1;
        }
    }

    public static android.net.Uri insert(android.content.Context context, android.content.ContentResolver resolver, android.net.Uri uri, android.content.ContentValues values) {
        try {
            return resolver.insert(uri, values);
        } catch (android.database.sqlite.SQLiteException e) {
            android.util.Log.e(android.database.sqlite.SqliteWrapper.TAG, "Catch a SQLiteException when insert: ", e);
            android.database.sqlite.SqliteWrapper.checkSQLiteException(context, e);
            return null;
        }
    }
}

