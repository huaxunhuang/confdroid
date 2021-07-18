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


/* This class implements record like data storage for aggregator.
The data is stored in the sqlite database row by row without primary key, all
columns are assume having string value.
Sample usage:
      AggregatorRecordStorage db = new AggregatorRecordStorage(this, "TestTable",
              new String[]{"clusterid", "long", "lat"});
      db.removeAllData();
      HashMap<String, String> row = new HashMap<String, String>();
      row.put("clusterid", "home");
      row.put("long", "110.203");
      row.put("lat", "-13.787");
      db.addData(row);
      row.put("clusterid", "office");
      row.put("long", "1.203");
      row.put("lat", "33.787");
      db.addData(row);
      List<Map<String,String> > allData = db.getAllData();
      Log.i(TAG,"Total data in database: " + allData.size());
 */
class AggregatorRecordStorage extends android.bordeaux.services.AggregatorStorage {
    private static final java.lang.String TAG = "AggregatorRecordStorage";

    private java.lang.String mTableName;

    private java.util.List<java.lang.String> mColumnNames;

    public AggregatorRecordStorage(android.content.Context context, java.lang.String tableName, java.lang.String[] columnNames) {
        if (columnNames.length < 1) {
            throw new java.lang.RuntimeException("No column keys");
        }
        mColumnNames = java.util.Arrays.asList(columnNames);
        mTableName = tableName;
        java.lang.String tableCmd = ((("create table " + tableName) + "( ") + columnNames[0]) + " TEXT";
        for (int i = 1; i < columnNames.length; ++i)
            tableCmd = ((tableCmd + ", ") + columnNames[i]) + " TEXT";

        tableCmd = tableCmd + ");";
        android.util.Log.i(android.bordeaux.services.AggregatorRecordStorage.TAG, tableCmd);
        try {
            mDbHelper = new android.bordeaux.services.AggregatorStorage.DBHelper(context, tableName, tableCmd);
            mDatabase = mDbHelper.getWritableDatabase();
        } catch (android.database.SQLException e) {
            throw new java.lang.RuntimeException("Can't open table: " + tableName);
        }
    }

    // Adding one more row to the table.
    // the data is a map of <column_name, value> pair.
    public boolean addData(java.util.Map<java.lang.String, java.lang.String> data) {
        android.content.ContentValues content = new android.content.ContentValues();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> item : data.entrySet()) {
            content.put(item.getKey(), item.getValue());
        }
        long rowID = mDatabase.insert(mTableName, null, content);
        return rowID >= 0;
    }

    // Return all data as a list of Map.
    // Notice that the column names are repeated for each row.
    public java.util.List<java.util.Map<java.lang.String, java.lang.String>> getAllData() {
        java.util.ArrayList<java.util.Map<java.lang.String, java.lang.String>> allData = new java.util.ArrayList<java.util.Map<java.lang.String, java.lang.String>>();
        android.database.Cursor cursor = mDatabase.rawQuery(("select * from " + mTableName) + ";", null);
        if (cursor.getCount() == 0) {
            return allData;
        }
        cursor.moveToFirst();
        do {
            java.util.HashMap<java.lang.String, java.lang.String> oneRow = new java.util.HashMap<java.lang.String, java.lang.String>();
            for (java.lang.String column : mColumnNames) {
                int columnIndex = cursor.getColumnIndex(column);
                if (!cursor.isNull(columnIndex)) {
                    java.lang.String value = cursor.getString(columnIndex);
                    oneRow.put(column, value);
                }
            }
            allData.add(oneRow);
        } while (cursor.moveToNext() );
        return allData;
    }

    // Empty the storage.
    public int removeAllData() {
        int nDeleteRows = mDatabase.delete(mTableName, "1", null);
        android.util.Log.i(android.bordeaux.services.AggregatorRecordStorage.TAG, "Number of rows in table deleted: " + nDeleteRows);
        return nDeleteRows;
    }
}

