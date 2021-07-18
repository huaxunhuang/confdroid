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
 * Simple test provider that runs in the local process.
 */
public class MemoryFileProvider extends android.content.ContentProvider {
    private static final java.lang.String TAG = "MemoryFileProvider";

    private static final java.lang.String DATA_FILE = "data.bin";

    // some random data
    public static final byte[] TEST_BLOB = new byte[]{ -12, 127, 0, 3, 1, 2, 3, 4, 5, 6, 1, -128, -1, -54, -65, 35, -53, -96, -74, -74, -55, -43, -69, 3, 52, -58, -121, 127, 87, -73, 16, -13, -103, -65, -128, -36, 107, 24, 118, -17, 97, 97, -88, 19, -94, -54, 53, 43, 44, -27, -124, 28, -74, 26, 35, -36, 16, -124, -31, -31, -128, -79, 108, 116, 43, -17 };

    private android.database.sqlite.SQLiteOpenHelper mOpenHelper;

    private static final int DATA_ID_BLOB = 1;

    private static final int HUGE = 2;

    private static final int FILE = 3;

    private static final android.content.UriMatcher sURLMatcher = new android.content.UriMatcher(android.content.UriMatcher.NO_MATCH);

    static {
        android.content.MemoryFileProvider.sURLMatcher.addURI("*", "data/#/blob", android.content.MemoryFileProvider.DATA_ID_BLOB);
        android.content.MemoryFileProvider.sURLMatcher.addURI("*", "huge", android.content.MemoryFileProvider.HUGE);
        android.content.MemoryFileProvider.sURLMatcher.addURI("*", "file", android.content.MemoryFileProvider.FILE);
    }

    private static class DatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {
        private static final java.lang.String DATABASE_NAME = "local.db";

        private static final int DATABASE_VERSION = 1;

        public DatabaseHelper(android.content.Context context) {
            super(context, android.content.MemoryFileProvider.DatabaseHelper.DATABASE_NAME, null, android.content.MemoryFileProvider.DatabaseHelper.DATABASE_VERSION);
        }

        @java.lang.Override
        public void onCreate(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL("CREATE TABLE data (" + (("_id INTEGER PRIMARY KEY," + "_blob TEXT, ") + "integer INTEGER);"));
            // insert alarms
            android.content.ContentValues values = new android.content.ContentValues();
            values.put("_id", 1);
            values.put("_blob", android.content.MemoryFileProvider.TEST_BLOB);
            values.put("integer", 100);
            db.insert("data", null, values);
        }

        @java.lang.Override
        public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int currentVersion) {
            android.util.Log.w(android.content.MemoryFileProvider.TAG, ((("Upgrading test database from version " + oldVersion) + " to ") + currentVersion) + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS data");
            onCreate(db);
        }
    }

    public MemoryFileProvider() {
    }

    @java.lang.Override
    public boolean onCreate() {
        mOpenHelper = new android.content.MemoryFileProvider.DatabaseHelper(getContext());
        try {
            java.io.OutputStream out = getContext().openFileOutput(android.content.MemoryFileProvider.DATA_FILE, android.content.Context.MODE_PRIVATE);
            out.write(android.content.MemoryFileProvider.TEST_BLOB);
            out.close();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    @java.lang.Override
    public android.database.Cursor query(android.net.Uri url, java.lang.String[] projectionIn, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sort) {
        throw new java.lang.UnsupportedOperationException("query not supported");
    }

    @java.lang.Override
    public java.lang.String getType(android.net.Uri url) {
        int match = android.content.MemoryFileProvider.sURLMatcher.match(url);
        switch (match) {
            case android.content.MemoryFileProvider.DATA_ID_BLOB :
                return "application/octet-stream";
            case android.content.MemoryFileProvider.FILE :
                return "application/octet-stream";
            default :
                throw new java.lang.IllegalArgumentException("Unknown URL");
        }
    }

    @java.lang.Override
    public android.os.ParcelFileDescriptor openFile(android.net.Uri url, java.lang.String mode) throws java.io.FileNotFoundException {
        int match = android.content.MemoryFileProvider.sURLMatcher.match(url);
        switch (match) {
            case android.content.MemoryFileProvider.DATA_ID_BLOB :
                java.lang.String sql = "SELECT _blob FROM data WHERE _id=" + url.getPathSegments().get(1);
                return getBlobColumnAsFile(url, mode, sql);
            case android.content.MemoryFileProvider.HUGE :
                try {
                    return android.os.ParcelFileDescriptor.fromData(android.content.MemoryFileProvider.TEST_BLOB, null);
                } catch (java.io.IOException ex) {
                    throw new java.io.FileNotFoundException((("Error reading " + url) + ":") + ex.toString());
                }
            case android.content.MemoryFileProvider.FILE :
                java.io.File file = getContext().getFileStreamPath(android.content.MemoryFileProvider.DATA_FILE);
                return android.os.ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            default :
                throw new java.io.FileNotFoundException("No files supported by provider at " + url);
        }
    }

    private android.os.ParcelFileDescriptor getBlobColumnAsFile(android.net.Uri url, java.lang.String mode, java.lang.String sql) throws java.io.FileNotFoundException {
        if (!"r".equals(mode)) {
            throw new java.io.FileNotFoundException((("Mode " + mode) + " not supported for ") + url);
        }
        android.database.sqlite.SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        return android.database.DatabaseUtils.blobFileDescriptorForQuery(db, sql, null);
    }

    @java.lang.Override
    public int update(android.net.Uri url, android.content.ContentValues values, java.lang.String where, java.lang.String[] whereArgs) {
        throw new java.lang.UnsupportedOperationException("update not supported");
    }

    @java.lang.Override
    public android.net.Uri insert(android.net.Uri url, android.content.ContentValues initialValues) {
        throw new java.lang.UnsupportedOperationException("insert not supported");
    }

    @java.lang.Override
    public int delete(android.net.Uri url, java.lang.String where, java.lang.String[] whereArgs) {
        throw new java.lang.UnsupportedOperationException("delete not supported");
    }
}

