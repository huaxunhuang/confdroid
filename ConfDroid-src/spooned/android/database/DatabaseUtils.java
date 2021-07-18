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
 * Static utility methods for dealing with databases and {@link Cursor}s.
 */
public class DatabaseUtils {
    private static final java.lang.String TAG = "DatabaseUtils";

    private static final boolean DEBUG = false;

    /**
     * One of the values returned by {@link #getSqlStatementType(String)}.
     */
    public static final int STATEMENT_SELECT = 1;

    /**
     * One of the values returned by {@link #getSqlStatementType(String)}.
     */
    public static final int STATEMENT_UPDATE = 2;

    /**
     * One of the values returned by {@link #getSqlStatementType(String)}.
     */
    public static final int STATEMENT_ATTACH = 3;

    /**
     * One of the values returned by {@link #getSqlStatementType(String)}.
     */
    public static final int STATEMENT_BEGIN = 4;

    /**
     * One of the values returned by {@link #getSqlStatementType(String)}.
     */
    public static final int STATEMENT_COMMIT = 5;

    /**
     * One of the values returned by {@link #getSqlStatementType(String)}.
     */
    public static final int STATEMENT_ABORT = 6;

    /**
     * One of the values returned by {@link #getSqlStatementType(String)}.
     */
    public static final int STATEMENT_PRAGMA = 7;

    /**
     * One of the values returned by {@link #getSqlStatementType(String)}.
     */
    public static final int STATEMENT_DDL = 8;

    /**
     * One of the values returned by {@link #getSqlStatementType(String)}.
     */
    public static final int STATEMENT_UNPREPARED = 9;

    /**
     * One of the values returned by {@link #getSqlStatementType(String)}.
     */
    public static final int STATEMENT_OTHER = 99;

    /**
     * Special function for writing an exception result at the header of
     * a parcel, to be used when returning an exception from a transaction.
     * exception will be re-thrown by the function in another process
     *
     * @param reply
     * 		Parcel to write to
     * @param e
     * 		The Exception to be written.
     * @see Parcel#writeNoException
     * @see Parcel#writeException
     */
    public static final void writeExceptionToParcel(android.os.Parcel reply, java.lang.Exception e) {
        int code = 0;
        boolean logException = true;
        if (e instanceof java.io.FileNotFoundException) {
            code = 1;
            logException = false;
        } else
            if (e instanceof java.lang.IllegalArgumentException) {
                code = 2;
            } else
                if (e instanceof java.lang.UnsupportedOperationException) {
                    code = 3;
                } else
                    if (e instanceof android.database.sqlite.SQLiteAbortException) {
                        code = 4;
                    } else
                        if (e instanceof android.database.sqlite.SQLiteConstraintException) {
                            code = 5;
                        } else
                            if (e instanceof android.database.sqlite.SQLiteDatabaseCorruptException) {
                                code = 6;
                            } else
                                if (e instanceof android.database.sqlite.SQLiteFullException) {
                                    code = 7;
                                } else
                                    if (e instanceof android.database.sqlite.SQLiteDiskIOException) {
                                        code = 8;
                                    } else
                                        if (e instanceof android.database.sqlite.SQLiteException) {
                                            code = 9;
                                        } else
                                            if (e instanceof android.content.OperationApplicationException) {
                                                code = 10;
                                            } else
                                                if (e instanceof android.os.OperationCanceledException) {
                                                    code = 11;
                                                    logException = false;
                                                } else {
                                                    reply.writeException(e);
                                                    android.util.Log.e(android.database.DatabaseUtils.TAG, "Writing exception to parcel", e);
                                                    return;
                                                }










        reply.writeInt(code);
        reply.writeString(e.getMessage());
        if (logException) {
            android.util.Log.e(android.database.DatabaseUtils.TAG, "Writing exception to parcel", e);
        }
    }

    /**
     * Special function for reading an exception result from the header of
     * a parcel, to be used after receiving the result of a transaction.  This
     * will throw the exception for you if it had been written to the Parcel,
     * otherwise return and let you read the normal result data from the Parcel.
     *
     * @param reply
     * 		Parcel to read from
     * @see Parcel#writeNoException
     * @see Parcel#readException
     */
    public static final void readExceptionFromParcel(android.os.Parcel reply) {
        int code = reply.readExceptionCode();
        if (code == 0)
            return;

        java.lang.String msg = reply.readString();
        android.database.DatabaseUtils.readExceptionFromParcel(reply, msg, code);
    }

    public static void readExceptionWithFileNotFoundExceptionFromParcel(android.os.Parcel reply) throws java.io.FileNotFoundException {
        int code = reply.readExceptionCode();
        if (code == 0)
            return;

        java.lang.String msg = reply.readString();
        if (code == 1) {
            throw new java.io.FileNotFoundException(msg);
        } else {
            android.database.DatabaseUtils.readExceptionFromParcel(reply, msg, code);
        }
    }

    public static void readExceptionWithOperationApplicationExceptionFromParcel(android.os.Parcel reply) throws android.content.OperationApplicationException {
        int code = reply.readExceptionCode();
        if (code == 0)
            return;

        java.lang.String msg = reply.readString();
        if (code == 10) {
            throw new android.content.OperationApplicationException(msg);
        } else {
            android.database.DatabaseUtils.readExceptionFromParcel(reply, msg, code);
        }
    }

    private static final void readExceptionFromParcel(android.os.Parcel reply, java.lang.String msg, int code) {
        switch (code) {
            case 2 :
                throw new java.lang.IllegalArgumentException(msg);
            case 3 :
                throw new java.lang.UnsupportedOperationException(msg);
            case 4 :
                throw new android.database.sqlite.SQLiteAbortException(msg);
            case 5 :
                throw new android.database.sqlite.SQLiteConstraintException(msg);
            case 6 :
                throw new android.database.sqlite.SQLiteDatabaseCorruptException(msg);
            case 7 :
                throw new android.database.sqlite.SQLiteFullException(msg);
            case 8 :
                throw new android.database.sqlite.SQLiteDiskIOException(msg);
            case 9 :
                throw new android.database.sqlite.SQLiteException(msg);
            case 11 :
                throw new android.os.OperationCanceledException(msg);
            default :
                reply.readException(code, msg);
        }
    }

    /**
     * Binds the given Object to the given SQLiteProgram using the proper
     * typing. For example, bind numbers as longs/doubles, and everything else
     * as a string by call toString() on it.
     *
     * @param prog
     * 		the program to bind the object to
     * @param index
     * 		the 1-based index to bind at
     * @param value
     * 		the value to bind
     */
    public static void bindObjectToProgram(android.database.sqlite.SQLiteProgram prog, int index, java.lang.Object value) {
        if (value == null) {
            prog.bindNull(index);
        } else
            if ((value instanceof java.lang.Double) || (value instanceof java.lang.Float)) {
                prog.bindDouble(index, ((java.lang.Number) (value)).doubleValue());
            } else
                if (value instanceof java.lang.Number) {
                    prog.bindLong(index, ((java.lang.Number) (value)).longValue());
                } else
                    if (value instanceof java.lang.Boolean) {
                        java.lang.Boolean bool = ((java.lang.Boolean) (value));
                        if (bool) {
                            prog.bindLong(index, 1);
                        } else {
                            prog.bindLong(index, 0);
                        }
                    } else
                        if (value instanceof byte[]) {
                            prog.bindBlob(index, ((byte[]) (value)));
                        } else {
                            prog.bindString(index, value.toString());
                        }




    }

    /**
     * Returns data type of the given object's value.
     * <p>
     * Returned values are
     * <ul>
     *   <li>{@link Cursor#FIELD_TYPE_NULL}</li>
     *   <li>{@link Cursor#FIELD_TYPE_INTEGER}</li>
     *   <li>{@link Cursor#FIELD_TYPE_FLOAT}</li>
     *   <li>{@link Cursor#FIELD_TYPE_STRING}</li>
     *   <li>{@link Cursor#FIELD_TYPE_BLOB}</li>
     * </ul>
     * </p>
     *
     * @param obj
     * 		the object whose value type is to be returned
     * @return object value type
     * @unknown 
     */
    public static int getTypeOfObject(java.lang.Object obj) {
        if (obj == null) {
            return android.database.Cursor.FIELD_TYPE_NULL;
        } else
            if (obj instanceof byte[]) {
                return android.database.Cursor.FIELD_TYPE_BLOB;
            } else
                if ((obj instanceof java.lang.Float) || (obj instanceof java.lang.Double)) {
                    return android.database.Cursor.FIELD_TYPE_FLOAT;
                } else
                    if ((((obj instanceof java.lang.Long) || (obj instanceof java.lang.Integer)) || (obj instanceof java.lang.Short)) || (obj instanceof java.lang.Byte)) {
                        return android.database.Cursor.FIELD_TYPE_INTEGER;
                    } else {
                        return android.database.Cursor.FIELD_TYPE_STRING;
                    }



    }

    /**
     * Fills the specified cursor window by iterating over the contents of the cursor.
     * The window is filled until the cursor is exhausted or the window runs out
     * of space.
     *
     * The original position of the cursor is left unchanged by this operation.
     *
     * @param cursor
     * 		The cursor that contains the data to put in the window.
     * @param position
     * 		The start position for filling the window.
     * @param window
     * 		The window to fill.
     * @unknown 
     */
    public static void cursorFillWindow(final android.database.Cursor cursor, int position, final android.database.CursorWindow window) {
        if ((position < 0) || (position >= cursor.getCount())) {
            return;
        }
        final int oldPos = cursor.getPosition();
        final int numColumns = cursor.getColumnCount();
        window.clear();
        window.setStartPosition(position);
        window.setNumColumns(numColumns);
        if (cursor.moveToPosition(position)) {
            rowloop : do {
                if (!window.allocRow()) {
                    break;
                }
                for (int i = 0; i < numColumns; i++) {
                    final int type = cursor.getType(i);
                    final boolean success;
                    switch (type) {
                        case android.database.Cursor.FIELD_TYPE_NULL :
                            success = window.putNull(position, i);
                            break;
                        case android.database.Cursor.FIELD_TYPE_INTEGER :
                            success = window.putLong(cursor.getLong(i), position, i);
                            break;
                        case android.database.Cursor.FIELD_TYPE_FLOAT :
                            success = window.putDouble(cursor.getDouble(i), position, i);
                            break;
                        case android.database.Cursor.FIELD_TYPE_BLOB :
                            {
                                final byte[] value = cursor.getBlob(i);
                                success = (value != null) ? window.putBlob(value, position, i) : window.putNull(position, i);
                                break;
                            }
                        default :
                            // assume value is convertible to String
                        case android.database.Cursor.FIELD_TYPE_STRING :
                            {
                                final java.lang.String value = cursor.getString(i);
                                success = (value != null) ? window.putString(value, position, i) : window.putNull(position, i);
                                break;
                            }
                    }
                    if (!success) {
                        window.freeLastRow();
                        break rowloop;
                    }
                }
                position += 1;
            } while (cursor.moveToNext() );
        }
        cursor.moveToPosition(oldPos);
    }

    /**
     * Appends an SQL string to the given StringBuilder, including the opening
     * and closing single quotes. Any single quotes internal to sqlString will
     * be escaped.
     *
     * This method is deprecated because we want to encourage everyone
     * to use the "?" binding form.  However, when implementing a
     * ContentProvider, one may want to add WHERE clauses that were
     * not provided by the caller.  Since "?" is a positional form,
     * using it in this case could break the caller because the
     * indexes would be shifted to accomodate the ContentProvider's
     * internal bindings.  In that case, it may be necessary to
     * construct a WHERE clause manually.  This method is useful for
     * those cases.
     *
     * @param sb
     * 		the StringBuilder that the SQL string will be appended to
     * @param sqlString
     * 		the raw string to be appended, which may contain single
     * 		quotes
     */
    public static void appendEscapedSQLString(java.lang.StringBuilder sb, java.lang.String sqlString) {
        sb.append('\'');
        if (sqlString.indexOf('\'') != (-1)) {
            int length = sqlString.length();
            for (int i = 0; i < length; i++) {
                char c = sqlString.charAt(i);
                if (c == '\'') {
                    sb.append('\'');
                }
                sb.append(c);
            }
        } else
            sb.append(sqlString);

        sb.append('\'');
    }

    /**
     * SQL-escape a string.
     */
    public static java.lang.String sqlEscapeString(java.lang.String value) {
        java.lang.StringBuilder escaper = new java.lang.StringBuilder();
        android.database.DatabaseUtils.appendEscapedSQLString(escaper, value);
        return escaper.toString();
    }

    /**
     * Appends an Object to an SQL string with the proper escaping, etc.
     */
    public static final void appendValueToSql(java.lang.StringBuilder sql, java.lang.Object value) {
        if (value == null) {
            sql.append("NULL");
        } else
            if (value instanceof java.lang.Boolean) {
                java.lang.Boolean bool = ((java.lang.Boolean) (value));
                if (bool) {
                    sql.append('1');
                } else {
                    sql.append('0');
                }
            } else {
                android.database.DatabaseUtils.appendEscapedSQLString(sql, value.toString());
            }

    }

    /**
     * Concatenates two SQL WHERE clauses, handling empty or null values.
     */
    public static java.lang.String concatenateWhere(java.lang.String a, java.lang.String b) {
        if (android.text.TextUtils.isEmpty(a)) {
            return b;
        }
        if (android.text.TextUtils.isEmpty(b)) {
            return a;
        }
        return ((("(" + a) + ") AND (") + b) + ")";
    }

    /**
     * return the collation key
     *
     * @param name
     * 		
     * @return the collation key
     */
    public static java.lang.String getCollationKey(java.lang.String name) {
        byte[] arr = android.database.DatabaseUtils.getCollationKeyInBytes(name);
        try {
            return new java.lang.String(arr, 0, android.database.DatabaseUtils.getKeyLen(arr), "ISO8859_1");
        } catch (java.lang.Exception ex) {
            return "";
        }
    }

    /**
     * return the collation key in hex format
     *
     * @param name
     * 		
     * @return the collation key in hex format
     */
    public static java.lang.String getHexCollationKey(java.lang.String name) {
        byte[] arr = android.database.DatabaseUtils.getCollationKeyInBytes(name);
        char[] keys = android.database.DatabaseUtils.encodeHex(arr);
        return new java.lang.String(keys, 0, android.database.DatabaseUtils.getKeyLen(arr) * 2);
    }

    /**
     * Used building output as Hex
     */
    private static final char[] DIGITS = new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private static char[] encodeHex(byte[] input) {
        int l = input.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = android.database.DatabaseUtils.DIGITS[(0xf0 & input[i]) >>> 4];
            out[j++] = android.database.DatabaseUtils.DIGITS[0xf & input[i]];
        }
        return out;
    }

    private static int getKeyLen(byte[] arr) {
        if (arr[arr.length - 1] != 0) {
            return arr.length;
        } else {
            // remove zero "termination"
            return arr.length - 1;
        }
    }

    private static byte[] getCollationKeyInBytes(java.lang.String name) {
        if (android.database.DatabaseUtils.mColl == null) {
            android.database.DatabaseUtils.mColl = java.text.Collator.getInstance();
            android.database.DatabaseUtils.mColl.setStrength(java.text.Collator.PRIMARY);
        }
        return android.database.DatabaseUtils.mColl.getCollationKey(name).toByteArray();
    }

    private static java.text.Collator mColl = null;

    /**
     * Prints the contents of a Cursor to System.out. The position is restored
     * after printing.
     *
     * @param cursor
     * 		the cursor to print
     */
    public static void dumpCursor(android.database.Cursor cursor) {
        android.database.DatabaseUtils.dumpCursor(cursor, java.lang.System.out);
    }

    /**
     * Prints the contents of a Cursor to a PrintSteam. The position is restored
     * after printing.
     *
     * @param cursor
     * 		the cursor to print
     * @param stream
     * 		the stream to print to
     */
    public static void dumpCursor(android.database.Cursor cursor, java.io.PrintStream stream) {
        stream.println(">>>>> Dumping cursor " + cursor);
        if (cursor != null) {
            int startPos = cursor.getPosition();
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()) {
                android.database.DatabaseUtils.dumpCurrentRow(cursor, stream);
            } 
            cursor.moveToPosition(startPos);
        }
        stream.println("<<<<<");
    }

    /**
     * Prints the contents of a Cursor to a StringBuilder. The position
     * is restored after printing.
     *
     * @param cursor
     * 		the cursor to print
     * @param sb
     * 		the StringBuilder to print to
     */
    public static void dumpCursor(android.database.Cursor cursor, java.lang.StringBuilder sb) {
        sb.append((">>>>> Dumping cursor " + cursor) + "\n");
        if (cursor != null) {
            int startPos = cursor.getPosition();
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()) {
                android.database.DatabaseUtils.dumpCurrentRow(cursor, sb);
            } 
            cursor.moveToPosition(startPos);
        }
        sb.append("<<<<<\n");
    }

    /**
     * Prints the contents of a Cursor to a String. The position is restored
     * after printing.
     *
     * @param cursor
     * 		the cursor to print
     * @return a String that contains the dumped cursor
     */
    public static java.lang.String dumpCursorToString(android.database.Cursor cursor) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        android.database.DatabaseUtils.dumpCursor(cursor, sb);
        return sb.toString();
    }

    /**
     * Prints the contents of a Cursor's current row to System.out.
     *
     * @param cursor
     * 		the cursor to print from
     */
    public static void dumpCurrentRow(android.database.Cursor cursor) {
        android.database.DatabaseUtils.dumpCurrentRow(cursor, java.lang.System.out);
    }

    /**
     * Prints the contents of a Cursor's current row to a PrintSteam.
     *
     * @param cursor
     * 		the cursor to print
     * @param stream
     * 		the stream to print to
     */
    public static void dumpCurrentRow(android.database.Cursor cursor, java.io.PrintStream stream) {
        java.lang.String[] cols = cursor.getColumnNames();
        stream.println(("" + cursor.getPosition()) + " {");
        int length = cols.length;
        for (int i = 0; i < length; i++) {
            java.lang.String value;
            try {
                value = cursor.getString(i);
            } catch (android.database.sqlite.SQLiteException e) {
                // assume that if the getString threw this exception then the column is not
                // representable by a string, e.g. it is a BLOB.
                value = "<unprintable>";
            }
            stream.println((("   " + cols[i]) + '=') + value);
        }
        stream.println("}");
    }

    /**
     * Prints the contents of a Cursor's current row to a StringBuilder.
     *
     * @param cursor
     * 		the cursor to print
     * @param sb
     * 		the StringBuilder to print to
     */
    public static void dumpCurrentRow(android.database.Cursor cursor, java.lang.StringBuilder sb) {
        java.lang.String[] cols = cursor.getColumnNames();
        sb.append(("" + cursor.getPosition()) + " {\n");
        int length = cols.length;
        for (int i = 0; i < length; i++) {
            java.lang.String value;
            try {
                value = cursor.getString(i);
            } catch (android.database.sqlite.SQLiteException e) {
                // assume that if the getString threw this exception then the column is not
                // representable by a string, e.g. it is a BLOB.
                value = "<unprintable>";
            }
            sb.append(((("   " + cols[i]) + '=') + value) + "\n");
        }
        sb.append("}\n");
    }

    /**
     * Dump the contents of a Cursor's current row to a String.
     *
     * @param cursor
     * 		the cursor to print
     * @return a String that contains the dumped cursor row
     */
    public static java.lang.String dumpCurrentRowToString(android.database.Cursor cursor) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        android.database.DatabaseUtils.dumpCurrentRow(cursor, sb);
        return sb.toString();
    }

    /**
     * Reads a String out of a field in a Cursor and writes it to a Map.
     *
     * @param cursor
     * 		The cursor to read from
     * @param field
     * 		The TEXT field to read
     * @param values
     * 		The {@link ContentValues} to put the value into, with the field as the key
     */
    public static void cursorStringToContentValues(android.database.Cursor cursor, java.lang.String field, android.content.ContentValues values) {
        android.database.DatabaseUtils.cursorStringToContentValues(cursor, field, values, field);
    }

    /**
     * Reads a String out of a field in a Cursor and writes it to an InsertHelper.
     *
     * @param cursor
     * 		The cursor to read from
     * @param field
     * 		The TEXT field to read
     * @param inserter
     * 		The InsertHelper to bind into
     * @param index
     * 		the index of the bind entry in the InsertHelper
     */
    public static void cursorStringToInsertHelper(android.database.Cursor cursor, java.lang.String field, android.database.DatabaseUtils.InsertHelper inserter, int index) {
        inserter.bind(index, cursor.getString(cursor.getColumnIndexOrThrow(field)));
    }

    /**
     * Reads a String out of a field in a Cursor and writes it to a Map.
     *
     * @param cursor
     * 		The cursor to read from
     * @param field
     * 		The TEXT field to read
     * @param values
     * 		The {@link ContentValues} to put the value into, with the field as the key
     * @param key
     * 		The key to store the value with in the map
     */
    public static void cursorStringToContentValues(android.database.Cursor cursor, java.lang.String field, android.content.ContentValues values, java.lang.String key) {
        values.put(key, cursor.getString(cursor.getColumnIndexOrThrow(field)));
    }

    /**
     * Reads an Integer out of a field in a Cursor and writes it to a Map.
     *
     * @param cursor
     * 		The cursor to read from
     * @param field
     * 		The INTEGER field to read
     * @param values
     * 		The {@link ContentValues} to put the value into, with the field as the key
     */
    public static void cursorIntToContentValues(android.database.Cursor cursor, java.lang.String field, android.content.ContentValues values) {
        android.database.DatabaseUtils.cursorIntToContentValues(cursor, field, values, field);
    }

    /**
     * Reads a Integer out of a field in a Cursor and writes it to a Map.
     *
     * @param cursor
     * 		The cursor to read from
     * @param field
     * 		The INTEGER field to read
     * @param values
     * 		The {@link ContentValues} to put the value into, with the field as the key
     * @param key
     * 		The key to store the value with in the map
     */
    public static void cursorIntToContentValues(android.database.Cursor cursor, java.lang.String field, android.content.ContentValues values, java.lang.String key) {
        int colIndex = cursor.getColumnIndex(field);
        if (!cursor.isNull(colIndex)) {
            values.put(key, cursor.getInt(colIndex));
        } else {
            values.put(key, ((java.lang.Integer) (null)));
        }
    }

    /**
     * Reads a Long out of a field in a Cursor and writes it to a Map.
     *
     * @param cursor
     * 		The cursor to read from
     * @param field
     * 		The INTEGER field to read
     * @param values
     * 		The {@link ContentValues} to put the value into, with the field as the key
     */
    public static void cursorLongToContentValues(android.database.Cursor cursor, java.lang.String field, android.content.ContentValues values) {
        android.database.DatabaseUtils.cursorLongToContentValues(cursor, field, values, field);
    }

    /**
     * Reads a Long out of a field in a Cursor and writes it to a Map.
     *
     * @param cursor
     * 		The cursor to read from
     * @param field
     * 		The INTEGER field to read
     * @param values
     * 		The {@link ContentValues} to put the value into
     * @param key
     * 		The key to store the value with in the map
     */
    public static void cursorLongToContentValues(android.database.Cursor cursor, java.lang.String field, android.content.ContentValues values, java.lang.String key) {
        int colIndex = cursor.getColumnIndex(field);
        if (!cursor.isNull(colIndex)) {
            java.lang.Long value = java.lang.Long.valueOf(cursor.getLong(colIndex));
            values.put(key, value);
        } else {
            values.put(key, ((java.lang.Long) (null)));
        }
    }

    /**
     * Reads a Double out of a field in a Cursor and writes it to a Map.
     *
     * @param cursor
     * 		The cursor to read from
     * @param field
     * 		The REAL field to read
     * @param values
     * 		The {@link ContentValues} to put the value into
     */
    public static void cursorDoubleToCursorValues(android.database.Cursor cursor, java.lang.String field, android.content.ContentValues values) {
        android.database.DatabaseUtils.cursorDoubleToContentValues(cursor, field, values, field);
    }

    /**
     * Reads a Double out of a field in a Cursor and writes it to a Map.
     *
     * @param cursor
     * 		The cursor to read from
     * @param field
     * 		The REAL field to read
     * @param values
     * 		The {@link ContentValues} to put the value into
     * @param key
     * 		The key to store the value with in the map
     */
    public static void cursorDoubleToContentValues(android.database.Cursor cursor, java.lang.String field, android.content.ContentValues values, java.lang.String key) {
        int colIndex = cursor.getColumnIndex(field);
        if (!cursor.isNull(colIndex)) {
            values.put(key, cursor.getDouble(colIndex));
        } else {
            values.put(key, ((java.lang.Double) (null)));
        }
    }

    /**
     * Read the entire contents of a cursor row and store them in a ContentValues.
     *
     * @param cursor
     * 		the cursor to read from.
     * @param values
     * 		the {@link ContentValues} to put the row into.
     */
    public static void cursorRowToContentValues(android.database.Cursor cursor, android.content.ContentValues values) {
        android.database.AbstractWindowedCursor awc = (cursor instanceof android.database.AbstractWindowedCursor) ? ((android.database.AbstractWindowedCursor) (cursor)) : null;
        java.lang.String[] columns = cursor.getColumnNames();
        int length = columns.length;
        for (int i = 0; i < length; i++) {
            if ((awc != null) && awc.isBlob(i)) {
                values.put(columns[i], cursor.getBlob(i));
            } else {
                values.put(columns[i], cursor.getString(i));
            }
        }
    }

    /**
     * Picks a start position for {@link Cursor#fillWindow} such that the
     * window will contain the requested row and a useful range of rows
     * around it.
     *
     * When the data set is too large to fit in a cursor window, seeking the
     * cursor can become a very expensive operation since we have to run the
     * query again when we move outside the bounds of the current window.
     *
     * We try to choose a start position for the cursor window such that
     * 1/3 of the window's capacity is used to hold rows before the requested
     * position and 2/3 of the window's capacity is used to hold rows after the
     * requested position.
     *
     * @param cursorPosition
     * 		The row index of the row we want to get.
     * @param cursorWindowCapacity
     * 		The estimated number of rows that can fit in
     * 		a cursor window, or 0 if unknown.
     * @return The recommended start position, always less than or equal to
    the requested row.
     * @unknown 
     */
    public static int cursorPickFillWindowStartPosition(int cursorPosition, int cursorWindowCapacity) {
        return java.lang.Math.max(cursorPosition - (cursorWindowCapacity / 3), 0);
    }

    /**
     * Query the table for the number of rows in the table.
     *
     * @param db
     * 		the database the table is in
     * @param table
     * 		the name of the table to query
     * @return the number of rows in the table
     */
    public static long queryNumEntries(android.database.sqlite.SQLiteDatabase db, java.lang.String table) {
        return android.database.DatabaseUtils.queryNumEntries(db, table, null, null);
    }

    /**
     * Query the table for the number of rows in the table.
     *
     * @param db
     * 		the database the table is in
     * @param table
     * 		the name of the table to query
     * @param selection
     * 		A filter declaring which rows to return,
     * 		formatted as an SQL WHERE clause (excluding the WHERE itself).
     * 		Passing null will count all rows for the given table
     * @return the number of rows in the table filtered by the selection
     */
    public static long queryNumEntries(android.database.sqlite.SQLiteDatabase db, java.lang.String table, java.lang.String selection) {
        return android.database.DatabaseUtils.queryNumEntries(db, table, selection, null);
    }

    /**
     * Query the table for the number of rows in the table.
     *
     * @param db
     * 		the database the table is in
     * @param table
     * 		the name of the table to query
     * @param selection
     * 		A filter declaring which rows to return,
     * 		formatted as an SQL WHERE clause (excluding the WHERE itself).
     * 		Passing null will count all rows for the given table
     * @param selectionArgs
     * 		You may include ?s in selection,
     * 		which will be replaced by the values from selectionArgs,
     * 		in order that they appear in the selection.
     * 		The values will be bound as Strings.
     * @return the number of rows in the table filtered by the selection
     */
    public static long queryNumEntries(android.database.sqlite.SQLiteDatabase db, java.lang.String table, java.lang.String selection, java.lang.String[] selectionArgs) {
        java.lang.String s = (!android.text.TextUtils.isEmpty(selection)) ? " where " + selection : "";
        return android.database.DatabaseUtils.longForQuery(db, ("select count(*) from " + table) + s, selectionArgs);
    }

    /**
     * Query the table to check whether a table is empty or not
     *
     * @param db
     * 		the database the table is in
     * @param table
     * 		the name of the table to query
     * @return True if the table is empty
     * @unknown 
     */
    public static boolean queryIsEmpty(android.database.sqlite.SQLiteDatabase db, java.lang.String table) {
        long isEmpty = android.database.DatabaseUtils.longForQuery(db, ("select exists(select 1 from " + table) + ")", null);
        return isEmpty == 0;
    }

    /**
     * Utility method to run the query on the db and return the value in the
     * first column of the first row.
     */
    public static long longForQuery(android.database.sqlite.SQLiteDatabase db, java.lang.String query, java.lang.String[] selectionArgs) {
        android.database.sqlite.SQLiteStatement prog = db.compileStatement(query);
        try {
            return android.database.DatabaseUtils.longForQuery(prog, selectionArgs);
        } finally {
            prog.close();
        }
    }

    /**
     * Utility method to run the pre-compiled query and return the value in the
     * first column of the first row.
     */
    public static long longForQuery(android.database.sqlite.SQLiteStatement prog, java.lang.String[] selectionArgs) {
        prog.bindAllArgsAsStrings(selectionArgs);
        return prog.simpleQueryForLong();
    }

    /**
     * Utility method to run the query on the db and return the value in the
     * first column of the first row.
     */
    public static java.lang.String stringForQuery(android.database.sqlite.SQLiteDatabase db, java.lang.String query, java.lang.String[] selectionArgs) {
        android.database.sqlite.SQLiteStatement prog = db.compileStatement(query);
        try {
            return android.database.DatabaseUtils.stringForQuery(prog, selectionArgs);
        } finally {
            prog.close();
        }
    }

    /**
     * Utility method to run the pre-compiled query and return the value in the
     * first column of the first row.
     */
    public static java.lang.String stringForQuery(android.database.sqlite.SQLiteStatement prog, java.lang.String[] selectionArgs) {
        prog.bindAllArgsAsStrings(selectionArgs);
        return prog.simpleQueryForString();
    }

    /**
     * Utility method to run the query on the db and return the blob value in the
     * first column of the first row.
     *
     * @return A read-only file descriptor for a copy of the blob value.
     */
    public static android.os.ParcelFileDescriptor blobFileDescriptorForQuery(android.database.sqlite.SQLiteDatabase db, java.lang.String query, java.lang.String[] selectionArgs) {
        android.database.sqlite.SQLiteStatement prog = db.compileStatement(query);
        try {
            return android.database.DatabaseUtils.blobFileDescriptorForQuery(prog, selectionArgs);
        } finally {
            prog.close();
        }
    }

    /**
     * Utility method to run the pre-compiled query and return the blob value in the
     * first column of the first row.
     *
     * @return A read-only file descriptor for a copy of the blob value.
     */
    public static android.os.ParcelFileDescriptor blobFileDescriptorForQuery(android.database.sqlite.SQLiteStatement prog, java.lang.String[] selectionArgs) {
        prog.bindAllArgsAsStrings(selectionArgs);
        return prog.simpleQueryForBlobFileDescriptor();
    }

    /**
     * Reads a String out of a column in a Cursor and writes it to a ContentValues.
     * Adds nothing to the ContentValues if the column isn't present or if its value is null.
     *
     * @param cursor
     * 		The cursor to read from
     * @param column
     * 		The column to read
     * @param values
     * 		The {@link ContentValues} to put the value into
     */
    public static void cursorStringToContentValuesIfPresent(android.database.Cursor cursor, android.content.ContentValues values, java.lang.String column) {
        final int index = cursor.getColumnIndex(column);
        if ((index != (-1)) && (!cursor.isNull(index))) {
            values.put(column, cursor.getString(index));
        }
    }

    /**
     * Reads a Long out of a column in a Cursor and writes it to a ContentValues.
     * Adds nothing to the ContentValues if the column isn't present or if its value is null.
     *
     * @param cursor
     * 		The cursor to read from
     * @param column
     * 		The column to read
     * @param values
     * 		The {@link ContentValues} to put the value into
     */
    public static void cursorLongToContentValuesIfPresent(android.database.Cursor cursor, android.content.ContentValues values, java.lang.String column) {
        final int index = cursor.getColumnIndex(column);
        if ((index != (-1)) && (!cursor.isNull(index))) {
            values.put(column, cursor.getLong(index));
        }
    }

    /**
     * Reads a Short out of a column in a Cursor and writes it to a ContentValues.
     * Adds nothing to the ContentValues if the column isn't present or if its value is null.
     *
     * @param cursor
     * 		The cursor to read from
     * @param column
     * 		The column to read
     * @param values
     * 		The {@link ContentValues} to put the value into
     */
    public static void cursorShortToContentValuesIfPresent(android.database.Cursor cursor, android.content.ContentValues values, java.lang.String column) {
        final int index = cursor.getColumnIndex(column);
        if ((index != (-1)) && (!cursor.isNull(index))) {
            values.put(column, cursor.getShort(index));
        }
    }

    /**
     * Reads a Integer out of a column in a Cursor and writes it to a ContentValues.
     * Adds nothing to the ContentValues if the column isn't present or if its value is null.
     *
     * @param cursor
     * 		The cursor to read from
     * @param column
     * 		The column to read
     * @param values
     * 		The {@link ContentValues} to put the value into
     */
    public static void cursorIntToContentValuesIfPresent(android.database.Cursor cursor, android.content.ContentValues values, java.lang.String column) {
        final int index = cursor.getColumnIndex(column);
        if ((index != (-1)) && (!cursor.isNull(index))) {
            values.put(column, cursor.getInt(index));
        }
    }

    /**
     * Reads a Float out of a column in a Cursor and writes it to a ContentValues.
     * Adds nothing to the ContentValues if the column isn't present or if its value is null.
     *
     * @param cursor
     * 		The cursor to read from
     * @param column
     * 		The column to read
     * @param values
     * 		The {@link ContentValues} to put the value into
     */
    public static void cursorFloatToContentValuesIfPresent(android.database.Cursor cursor, android.content.ContentValues values, java.lang.String column) {
        final int index = cursor.getColumnIndex(column);
        if ((index != (-1)) && (!cursor.isNull(index))) {
            values.put(column, cursor.getFloat(index));
        }
    }

    /**
     * Reads a Double out of a column in a Cursor and writes it to a ContentValues.
     * Adds nothing to the ContentValues if the column isn't present or if its value is null.
     *
     * @param cursor
     * 		The cursor to read from
     * @param column
     * 		The column to read
     * @param values
     * 		The {@link ContentValues} to put the value into
     */
    public static void cursorDoubleToContentValuesIfPresent(android.database.Cursor cursor, android.content.ContentValues values, java.lang.String column) {
        final int index = cursor.getColumnIndex(column);
        if ((index != (-1)) && (!cursor.isNull(index))) {
            values.put(column, cursor.getDouble(index));
        }
    }

    /**
     * This class allows users to do multiple inserts into a table using
     * the same statement.
     * <p>
     * This class is not thread-safe.
     * </p>
     *
     * @deprecated Use {@link SQLiteStatement} instead.
     */
    @java.lang.Deprecated
    public static class InsertHelper {
        private final android.database.sqlite.SQLiteDatabase mDb;

        private final java.lang.String mTableName;

        private java.util.HashMap<java.lang.String, java.lang.Integer> mColumns;

        private java.lang.String mInsertSQL = null;

        private android.database.sqlite.SQLiteStatement mInsertStatement = null;

        private android.database.sqlite.SQLiteStatement mReplaceStatement = null;

        private android.database.sqlite.SQLiteStatement mPreparedStatement = null;

        /**
         * {@hide }
         *
         * These are the columns returned by sqlite's "PRAGMA
         * table_info(...)" command that we depend on.
         */
        public static final int TABLE_INFO_PRAGMA_COLUMNNAME_INDEX = 1;

        /**
         * This field was accidentally exposed in earlier versions of the platform
         * so we can hide it but we can't remove it.
         *
         * @unknown 
         */
        public static final int TABLE_INFO_PRAGMA_DEFAULT_INDEX = 4;

        /**
         *
         *
         * @param db
         * 		the SQLiteDatabase to insert into
         * @param tableName
         * 		the name of the table to insert into
         */
        public InsertHelper(android.database.sqlite.SQLiteDatabase db, java.lang.String tableName) {
            mDb = db;
            mTableName = tableName;
        }

        private void buildSQL() throws android.database.SQLException {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("INSERT INTO ");
            sb.append(mTableName);
            sb.append(" (");
            java.lang.StringBuilder sbv = new java.lang.StringBuilder(128);
            sbv.append("VALUES (");
            int i = 1;
            android.database.Cursor cur = null;
            try {
                cur = mDb.rawQuery(("PRAGMA table_info(" + mTableName) + ")", null);
                mColumns = new java.util.HashMap<java.lang.String, java.lang.Integer>(cur.getCount());
                while (cur.moveToNext()) {
                    java.lang.String columnName = cur.getString(android.database.DatabaseUtils.InsertHelper.TABLE_INFO_PRAGMA_COLUMNNAME_INDEX);
                    java.lang.String defaultValue = cur.getString(android.database.DatabaseUtils.InsertHelper.TABLE_INFO_PRAGMA_DEFAULT_INDEX);
                    mColumns.put(columnName, i);
                    sb.append("'");
                    sb.append(columnName);
                    sb.append("'");
                    if (defaultValue == null) {
                        sbv.append("?");
                    } else {
                        sbv.append("COALESCE(?, ");
                        sbv.append(defaultValue);
                        sbv.append(")");
                    }
                    sb.append(i == cur.getCount() ? ") " : ", ");
                    sbv.append(i == cur.getCount() ? ");" : ", ");
                    ++i;
                } 
            } finally {
                if (cur != null)
                    cur.close();

            }
            sb.append(sbv);
            mInsertSQL = sb.toString();
            if (android.database.DatabaseUtils.DEBUG)
                android.util.Log.v(android.database.DatabaseUtils.TAG, "insert statement is " + mInsertSQL);

        }

        private android.database.sqlite.SQLiteStatement getStatement(boolean allowReplace) throws android.database.SQLException {
            if (allowReplace) {
                if (mReplaceStatement == null) {
                    if (mInsertSQL == null)
                        buildSQL();

                    // chop "INSERT" off the front and prepend "INSERT OR REPLACE" instead.
                    java.lang.String replaceSQL = "INSERT OR REPLACE" + mInsertSQL.substring(6);
                    mReplaceStatement = mDb.compileStatement(replaceSQL);
                }
                return mReplaceStatement;
            } else {
                if (mInsertStatement == null) {
                    if (mInsertSQL == null)
                        buildSQL();

                    mInsertStatement = mDb.compileStatement(mInsertSQL);
                }
                return mInsertStatement;
            }
        }

        /**
         * Performs an insert, adding a new row with the given values.
         *
         * @param values
         * 		the set of values with which  to populate the
         * 		new row
         * @param allowReplace
         * 		if true, the statement does "INSERT OR
         * 		REPLACE" instead of "INSERT", silently deleting any
         * 		previously existing rows that would cause a conflict
         * @return the row ID of the newly inserted row, or -1 if an
        error occurred
         */
        private long insertInternal(android.content.ContentValues values, boolean allowReplace) {
            // Start a transaction even though we don't really need one.
            // This is to help maintain compatibility with applications that
            // access InsertHelper from multiple threads even though they never should have.
            // The original code used to lock the InsertHelper itself which was prone
            // to deadlocks.  Starting a transaction achieves the same mutual exclusion
            // effect as grabbing a lock but without the potential for deadlocks.
            mDb.beginTransactionNonExclusive();
            try {
                android.database.sqlite.SQLiteStatement stmt = getStatement(allowReplace);
                stmt.clearBindings();
                if (android.database.DatabaseUtils.DEBUG)
                    android.util.Log.v(android.database.DatabaseUtils.TAG, "--- inserting in table " + mTableName);

                for (java.util.Map.Entry<java.lang.String, java.lang.Object> e : values.valueSet()) {
                    final java.lang.String key = e.getKey();
                    int i = getColumnIndex(key);
                    android.database.DatabaseUtils.bindObjectToProgram(stmt, i, e.getValue());
                    if (android.database.DatabaseUtils.DEBUG) {
                        android.util.Log.v(android.database.DatabaseUtils.TAG, ((((("binding " + e.getValue()) + " to column ") + i) + " (") + key) + ")");
                    }
                }
                long result = stmt.executeInsert();
                mDb.setTransactionSuccessful();
                return result;
            } catch (android.database.SQLException e) {
                android.util.Log.e(android.database.DatabaseUtils.TAG, (("Error inserting " + values) + " into table  ") + mTableName, e);
                return -1;
            } finally {
                mDb.endTransaction();
            }
        }

        /**
         * Returns the index of the specified column. This is index is suitagble for use
         * in calls to bind().
         *
         * @param key
         * 		the column name
         * @return the index of the column
         */
        public int getColumnIndex(java.lang.String key) {
            getStatement(false);
            final java.lang.Integer index = mColumns.get(key);
            if (index == null) {
                throw new java.lang.IllegalArgumentException(("column '" + key) + "' is invalid");
            }
            return index;
        }

        /**
         * Bind the value to an index. A prepareForInsert() or prepareForReplace()
         * without a matching execute() must have already have been called.
         *
         * @param index
         * 		the index of the slot to which to bind
         * @param value
         * 		the value to bind
         */
        public void bind(int index, double value) {
            mPreparedStatement.bindDouble(index, value);
        }

        /**
         * Bind the value to an index. A prepareForInsert() or prepareForReplace()
         * without a matching execute() must have already have been called.
         *
         * @param index
         * 		the index of the slot to which to bind
         * @param value
         * 		the value to bind
         */
        public void bind(int index, float value) {
            mPreparedStatement.bindDouble(index, value);
        }

        /**
         * Bind the value to an index. A prepareForInsert() or prepareForReplace()
         * without a matching execute() must have already have been called.
         *
         * @param index
         * 		the index of the slot to which to bind
         * @param value
         * 		the value to bind
         */
        public void bind(int index, long value) {
            mPreparedStatement.bindLong(index, value);
        }

        /**
         * Bind the value to an index. A prepareForInsert() or prepareForReplace()
         * without a matching execute() must have already have been called.
         *
         * @param index
         * 		the index of the slot to which to bind
         * @param value
         * 		the value to bind
         */
        public void bind(int index, int value) {
            mPreparedStatement.bindLong(index, value);
        }

        /**
         * Bind the value to an index. A prepareForInsert() or prepareForReplace()
         * without a matching execute() must have already have been called.
         *
         * @param index
         * 		the index of the slot to which to bind
         * @param value
         * 		the value to bind
         */
        public void bind(int index, boolean value) {
            mPreparedStatement.bindLong(index, value ? 1 : 0);
        }

        /**
         * Bind null to an index. A prepareForInsert() or prepareForReplace()
         * without a matching execute() must have already have been called.
         *
         * @param index
         * 		the index of the slot to which to bind
         */
        public void bindNull(int index) {
            mPreparedStatement.bindNull(index);
        }

        /**
         * Bind the value to an index. A prepareForInsert() or prepareForReplace()
         * without a matching execute() must have already have been called.
         *
         * @param index
         * 		the index of the slot to which to bind
         * @param value
         * 		the value to bind
         */
        public void bind(int index, byte[] value) {
            if (value == null) {
                mPreparedStatement.bindNull(index);
            } else {
                mPreparedStatement.bindBlob(index, value);
            }
        }

        /**
         * Bind the value to an index. A prepareForInsert() or prepareForReplace()
         * without a matching execute() must have already have been called.
         *
         * @param index
         * 		the index of the slot to which to bind
         * @param value
         * 		the value to bind
         */
        public void bind(int index, java.lang.String value) {
            if (value == null) {
                mPreparedStatement.bindNull(index);
            } else {
                mPreparedStatement.bindString(index, value);
            }
        }

        /**
         * Performs an insert, adding a new row with the given values.
         * If the table contains conflicting rows, an error is
         * returned.
         *
         * @param values
         * 		the set of values with which to populate the
         * 		new row
         * @return the row ID of the newly inserted row, or -1 if an
        error occurred
         */
        public long insert(android.content.ContentValues values) {
            return insertInternal(values, false);
        }

        /**
         * Execute the previously prepared insert or replace using the bound values
         * since the last call to prepareForInsert or prepareForReplace.
         *
         * <p>Note that calling bind() and then execute() is not thread-safe. The only thread-safe
         * way to use this class is to call insert() or replace().
         *
         * @return the row ID of the newly inserted row, or -1 if an
        error occurred
         */
        public long execute() {
            if (mPreparedStatement == null) {
                throw new java.lang.IllegalStateException("you must prepare this inserter before calling " + "execute");
            }
            try {
                if (android.database.DatabaseUtils.DEBUG)
                    android.util.Log.v(android.database.DatabaseUtils.TAG, "--- doing insert or replace in table " + mTableName);

                return mPreparedStatement.executeInsert();
            } catch (android.database.SQLException e) {
                android.util.Log.e(android.database.DatabaseUtils.TAG, "Error executing InsertHelper with table " + mTableName, e);
                return -1;
            } finally {
                // you can only call this once per prepare
                mPreparedStatement = null;
            }
        }

        /**
         * Prepare the InsertHelper for an insert. The pattern for this is:
         * <ul>
         * <li>prepareForInsert()
         * <li>bind(index, value);
         * <li>bind(index, value);
         * <li>...
         * <li>bind(index, value);
         * <li>execute();
         * </ul>
         */
        public void prepareForInsert() {
            mPreparedStatement = getStatement(false);
            mPreparedStatement.clearBindings();
        }

        /**
         * Prepare the InsertHelper for a replace. The pattern for this is:
         * <ul>
         * <li>prepareForReplace()
         * <li>bind(index, value);
         * <li>bind(index, value);
         * <li>...
         * <li>bind(index, value);
         * <li>execute();
         * </ul>
         */
        public void prepareForReplace() {
            mPreparedStatement = getStatement(true);
            mPreparedStatement.clearBindings();
        }

        /**
         * Performs an insert, adding a new row with the given values.
         * If the table contains conflicting rows, they are deleted
         * and replaced with the new row.
         *
         * @param values
         * 		the set of values with which to populate the
         * 		new row
         * @return the row ID of the newly inserted row, or -1 if an
        error occurred
         */
        public long replace(android.content.ContentValues values) {
            return insertInternal(values, true);
        }

        /**
         * Close this object and release any resources associated with
         * it.  The behavior of calling <code>insert()</code> after
         * calling this method is undefined.
         */
        public void close() {
            if (mInsertStatement != null) {
                mInsertStatement.close();
                mInsertStatement = null;
            }
            if (mReplaceStatement != null) {
                mReplaceStatement.close();
                mReplaceStatement = null;
            }
            mInsertSQL = null;
            mColumns = null;
        }
    }

    /**
     * Creates a db and populates it with the sql statements in sqlStatements.
     *
     * @param context
     * 		the context to use to create the db
     * @param dbName
     * 		the name of the db to create
     * @param dbVersion
     * 		the version to set on the db
     * @param sqlStatements
     * 		the statements to use to populate the db. This should be a single string
     * 		of the form returned by sqlite3's <tt>.dump</tt> command (statements separated by
     * 		semicolons)
     */
    public static void createDbFromSqlStatements(android.content.Context context, java.lang.String dbName, int dbVersion, java.lang.String sqlStatements) {
        android.database.sqlite.SQLiteDatabase db = context.openOrCreateDatabase(dbName, 0, null);
        // TODO: this is not quite safe since it assumes that all semicolons at the end of a line
        // terminate statements. It is possible that a text field contains ;\n. We will have to fix
        // this if that turns out to be a problem.
        java.lang.String[] statements = android.text.TextUtils.split(sqlStatements, ";\n");
        for (java.lang.String statement : statements) {
            if (android.text.TextUtils.isEmpty(statement))
                continue;

            db.execSQL(statement);
        }
        db.setVersion(dbVersion);
        db.close();
    }

    /**
     * Returns one of the following which represent the type of the given SQL statement.
     * <ol>
     *   <li>{@link #STATEMENT_SELECT}</li>
     *   <li>{@link #STATEMENT_UPDATE}</li>
     *   <li>{@link #STATEMENT_ATTACH}</li>
     *   <li>{@link #STATEMENT_BEGIN}</li>
     *   <li>{@link #STATEMENT_COMMIT}</li>
     *   <li>{@link #STATEMENT_ABORT}</li>
     *   <li>{@link #STATEMENT_OTHER}</li>
     * </ol>
     *
     * @param sql
     * 		the SQL statement whose type is returned by this method
     * @return one of the values listed above
     */
    public static int getSqlStatementType(java.lang.String sql) {
        sql = sql.trim();
        if (sql.length() < 3) {
            return android.database.DatabaseUtils.STATEMENT_OTHER;
        }
        java.lang.String prefixSql = sql.substring(0, 3).toUpperCase(java.util.Locale.ROOT);
        if (prefixSql.equals("SEL")) {
            return android.database.DatabaseUtils.STATEMENT_SELECT;
        } else
            if (((prefixSql.equals("INS") || prefixSql.equals("UPD")) || prefixSql.equals("REP")) || prefixSql.equals("DEL")) {
                return android.database.DatabaseUtils.STATEMENT_UPDATE;
            } else
                if (prefixSql.equals("ATT")) {
                    return android.database.DatabaseUtils.STATEMENT_ATTACH;
                } else
                    if (prefixSql.equals("COM")) {
                        return android.database.DatabaseUtils.STATEMENT_COMMIT;
                    } else
                        if (prefixSql.equals("END")) {
                            return android.database.DatabaseUtils.STATEMENT_COMMIT;
                        } else
                            if (prefixSql.equals("ROL")) {
                                return android.database.DatabaseUtils.STATEMENT_ABORT;
                            } else
                                if (prefixSql.equals("BEG")) {
                                    return android.database.DatabaseUtils.STATEMENT_BEGIN;
                                } else
                                    if (prefixSql.equals("PRA")) {
                                        return android.database.DatabaseUtils.STATEMENT_PRAGMA;
                                    } else
                                        if ((prefixSql.equals("CRE") || prefixSql.equals("DRO")) || prefixSql.equals("ALT")) {
                                            return android.database.DatabaseUtils.STATEMENT_DDL;
                                        } else
                                            if (prefixSql.equals("ANA") || prefixSql.equals("DET")) {
                                                return android.database.DatabaseUtils.STATEMENT_UNPREPARED;
                                            }









        return android.database.DatabaseUtils.STATEMENT_OTHER;
    }

    /**
     * Appends one set of selection args to another. This is useful when adding a selection
     * argument to a user provided set.
     */
    public static java.lang.String[] appendSelectionArgs(java.lang.String[] originalValues, java.lang.String[] newValues) {
        if ((originalValues == null) || (originalValues.length == 0)) {
            return newValues;
        }
        java.lang.String[] result = new java.lang.String[originalValues.length + newValues.length];
        java.lang.System.arraycopy(originalValues, 0, result, 0, originalValues.length);
        java.lang.System.arraycopy(newValues, 0, result, originalValues.length, newValues.length);
        return result;
    }

    /**
     * Returns column index of "_id" column, or -1 if not found.
     *
     * @unknown 
     */
    public static int findRowIdColumnIndex(java.lang.String[] columnNames) {
        int length = columnNames.length;
        for (int i = 0; i < length; i++) {
            if (columnNames[i].equals("_id")) {
                return i;
            }
        }
        return -1;
    }
}

