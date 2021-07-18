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


// Base Helper class for aggregator storage database
class AggregatorStorage {
    private static final java.lang.String TAG = "AggregatorStorage";

    private static final java.lang.String DATABASE_NAME = "aggregator";

    private static final int DATABASE_VERSION = 1;

    protected android.bordeaux.services.AggregatorStorage.DBHelper mDbHelper;

    protected android.database.sqlite.SQLiteDatabase mDatabase;

    class DBHelper extends android.database.sqlite.SQLiteOpenHelper {
        private java.lang.String mTableCmd;

        private java.lang.String mTableName;

        DBHelper(android.content.Context context, java.lang.String tableName, java.lang.String tableCmd) {
            super(context, android.bordeaux.services.AggregatorStorage.DATABASE_NAME, null, android.bordeaux.services.AggregatorStorage.DATABASE_VERSION);
            mTableName = tableName;
            mTableCmd = tableCmd;
        }

        @java.lang.Override
        public void onCreate(android.database.sqlite.SQLiteDatabase db) {
            db.execSQL(mTableCmd);
        }

        @java.lang.Override
        public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
            android.util.Log.w(android.bordeaux.services.AggregatorStorage.TAG, ((("Upgrading database from version " + oldVersion) + " to ") + newVersion) + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + mTableName);
            onCreate(db);
        }
    }
}

