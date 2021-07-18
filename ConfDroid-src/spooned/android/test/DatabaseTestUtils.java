/**
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
package android.test;


/**
 * A collection of utilities for writing unit tests for database code.
 *
 * @unknown pending API council approval
 */
@java.lang.Deprecated
public class DatabaseTestUtils {
    /**
     * Compares the schema of two databases and asserts that they are equal.
     *
     * @param expectedDb
     * 		the db that is known to have the correct schema
     * @param db
     * 		the db whose schema should be checked
     */
    public static void assertSchemaEquals(android.database.sqlite.SQLiteDatabase expectedDb, android.database.sqlite.SQLiteDatabase db) {
        java.util.Set<java.lang.String> expectedSchema = android.test.DatabaseTestUtils.getSchemaSet(expectedDb);
        java.util.Set<java.lang.String> schema = android.test.DatabaseTestUtils.getSchemaSet(db);
        android.test.MoreAsserts.assertEquals(expectedSchema, schema);
    }

    private static java.util.Set<java.lang.String> getSchemaSet(android.database.sqlite.SQLiteDatabase db) {
        java.util.Set<java.lang.String> schemaSet = com.google.android.collect.Sets.newHashSet();
        android.database.Cursor entityCursor = db.rawQuery("SELECT sql FROM sqlite_master", null);
        try {
            while (entityCursor.moveToNext()) {
                java.lang.String sql = entityCursor.getString(0);
                schemaSet.add(sql);
            } 
        } finally {
            entityCursor.close();
        }
        return schemaSet;
    }
}

