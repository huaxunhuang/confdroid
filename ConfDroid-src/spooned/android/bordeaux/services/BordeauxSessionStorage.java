/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.bordeaux.services;


// This class manages the database for storing the session data.
// 
class BordeauxSessionStorage {
    private static final java.lang.String TAG = "BordeauxSessionStorage";

    // unique key for the session
    public static final java.lang.String COLUMN_KEY = "key";

    // name of the learning class
    public static final java.lang.String COLUMN_CLASS = "class";

    // data of the learning model
    public static final java.lang.String COLUMN_MODEL = "model";

    // last update time
    public static final java.lang.String COLUMN_TIME = "time";

    private static final java.lang.String DATABASE_NAME = "bordeaux";

    private static final java.lang.String SESSION_TABLE = "sessions";

    private static final int DATABASE_VERSION = 1;

    private static final java.lang.String DATABASE_CREATE = ((((((((("create table " + android.bordeaux.services.BordeauxSessionStorage.SESSION_TABLE) + "( ") + android.bordeaux.services.BordeauxSessionStorage.COLUMN_KEY) + " TEXT primary key, ") + android.bordeaux.services.BordeauxSessionStorage.COLUMN_CLASS) + " TEXT, ") + android.bordeaux.services.BordeauxSessionStorage.COLUMN_MODEL) + " BLOB, ") + android.bordeaux.services.BordeauxSessionStorage.COLUMN_TIME) + " INTEGER);";

    private android.bordeaux.services.BordeauxSessionStorage.SessionDBHelper mDbHelper;

    private android.database.sqlite.SQLiteDatabase mDbSessions;

    BordeauxSessionStorage(final android.content.Context context) {
        try {
            mDbHelper = new android.bordeaux.services.BordeauxSessionStorage.SessionDBHelper(context);
            mDbSessions = mDbHelper.getWritableDatabase();
        } catch (android.database.SQLException e) {
            throw new java.lang.RuntimeException("Can't open session database");
        }
    }

    private class SessionDBHelper extends android.database.sqlite.SQLiteOpenHelper {
        SessionDBHelper(android.content.Context context) {
            super(context, android.bordeaux.services.BordeauxSessionStorage.DATABASE_NAME, null, android.bordeaux.services.BordeauxSessionStorage.DATABASE_VERSION);
        }

        @java.lang.Override
        public void onCreate(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL(android.bordeaux.services.BordeauxSessionStorage.DATABASE_CREATE);
        }

        @java.lang.Override
        public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
            android.util.Log.w(android.bordeaux.services.BordeauxSessionStorage.TAG, ((("Upgrading database from version " + oldVersion) + " to ") + newVersion) + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + android.bordeaux.services.BordeauxSessionStorage.SESSION_TABLE);
            onCreate(db);
        }
    }

    private android.content.ContentValues createSessionEntry(java.lang.String key, java.lang.Class learner, byte[] model) {
        android.content.ContentValues entry = new android.content.ContentValues();
        entry.put(android.bordeaux.services.BordeauxSessionStorage.COLUMN_KEY, key);
        entry.put(android.bordeaux.services.BordeauxSessionStorage.COLUMN_TIME, java.lang.System.currentTimeMillis());
        entry.put(android.bordeaux.services.BordeauxSessionStorage.COLUMN_MODEL, model);
        entry.put(android.bordeaux.services.BordeauxSessionStorage.COLUMN_CLASS, learner.getName());
        return entry;
    }

    boolean saveSession(java.lang.String key, java.lang.Class learner, byte[] model) {
        android.content.ContentValues content = createSessionEntry(key, learner, model);
        long rowID = mDbSessions.insertWithOnConflict(android.bordeaux.services.BordeauxSessionStorage.SESSION_TABLE, null, content, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE);
        return rowID >= 0;
    }

    private android.bordeaux.services.BordeauxSessionManager.Session getSessionFromCursor(android.database.Cursor cursor) {
        android.bordeaux.services.BordeauxSessionManager.Session session = new android.bordeaux.services.BordeauxSessionManager.Session();
        java.lang.String className = cursor.getString(cursor.getColumnIndex(android.bordeaux.services.BordeauxSessionStorage.COLUMN_CLASS));
        try {
            session.learnerClass = java.lang.Class.forName(className);
            session.learner = ((android.bordeaux.services.IBordeauxLearner) (session.learnerClass.getConstructor().newInstance()));
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException("Can't instantiate class: " + className);
        }
        byte[] model = cursor.getBlob(cursor.getColumnIndex(android.bordeaux.services.BordeauxSessionStorage.COLUMN_MODEL));
        session.learner.setModel(model);
        return session;
    }

    android.bordeaux.services.BordeauxSessionManager.Session getSession(java.lang.String key) {
        android.database.Cursor cursor = mDbSessions.query(true, android.bordeaux.services.BordeauxSessionStorage.SESSION_TABLE, new java.lang.String[]{ android.bordeaux.services.BordeauxSessionStorage.COLUMN_KEY, android.bordeaux.services.BordeauxSessionStorage.COLUMN_CLASS, android.bordeaux.services.BordeauxSessionStorage.COLUMN_MODEL, android.bordeaux.services.BordeauxSessionStorage.COLUMN_TIME }, ((android.bordeaux.services.BordeauxSessionStorage.COLUMN_KEY + "=\"") + key) + "\"", null, null, null, null, null);
        if ((cursor == null) | (cursor.getCount() == 0)) {
            cursor.close();
            return null;
        }
        if (cursor.getCount() > 1) {
            cursor.close();
            throw new java.lang.RuntimeException("Unexpected duplication in session table for key:" + key);
        }
        cursor.moveToFirst();
        android.bordeaux.services.BordeauxSessionManager.Session s = getSessionFromCursor(cursor);
        cursor.close();
        return s;
    }

    void getAllSessions(java.util.concurrent.ConcurrentHashMap<java.lang.String, android.bordeaux.services.BordeauxSessionManager.Session> sessions) {
        android.database.Cursor cursor = mDbSessions.rawQuery("select * from ?;", new java.lang.String[]{ android.bordeaux.services.BordeauxSessionStorage.SESSION_TABLE });
        if (cursor == null)
            return;

        cursor.moveToFirst();
        do {
            java.lang.String key = cursor.getString(cursor.getColumnIndex(android.bordeaux.services.BordeauxSessionStorage.COLUMN_KEY));
            android.bordeaux.services.BordeauxSessionManager.Session session = getSessionFromCursor(cursor);
            sessions.put(key, session);
        } while (cursor.moveToNext() );
    }

    // remove all sessions that have the key that matches the given sql regular
    // expression.
    int removeSessions(java.lang.String reKey) {
        int nDeleteRows = mDbSessions.delete(android.bordeaux.services.BordeauxSessionStorage.SESSION_TABLE, "? like \"?\"", new java.lang.String[]{ android.bordeaux.services.BordeauxSessionStorage.COLUMN_KEY, reKey });
        android.util.Log.i(android.bordeaux.services.BordeauxSessionStorage.TAG, "Number of rows in session table deleted: " + nDeleteRows);
        return nDeleteRows;
    }
}

