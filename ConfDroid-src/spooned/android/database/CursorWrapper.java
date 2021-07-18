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
 * Wrapper class for Cursor that delegates all calls to the actual cursor object.  The primary
 * use for this class is to extend a cursor while overriding only a subset of its methods.
 */
public class CursorWrapper implements android.database.Cursor {
    /**
     *
     *
     * @unknown 
     */
    protected final android.database.Cursor mCursor;

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor
     * 		The underlying cursor to wrap.
     */
    public CursorWrapper(android.database.Cursor cursor) {
        mCursor = cursor;
    }

    /**
     * Gets the underlying cursor that is wrapped by this instance.
     *
     * @return The wrapped cursor.
     */
    public android.database.Cursor getWrappedCursor() {
        return mCursor;
    }

    @java.lang.Override
    public void close() {
        mCursor.close();
    }

    @java.lang.Override
    public boolean isClosed() {
        return mCursor.isClosed();
    }

    @java.lang.Override
    public int getCount() {
        return mCursor.getCount();
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void deactivate() {
        mCursor.deactivate();
    }

    @java.lang.Override
    public boolean moveToFirst() {
        return mCursor.moveToFirst();
    }

    @java.lang.Override
    public int getColumnCount() {
        return mCursor.getColumnCount();
    }

    @java.lang.Override
    public int getColumnIndex(java.lang.String columnName) {
        return mCursor.getColumnIndex(columnName);
    }

    @java.lang.Override
    public int getColumnIndexOrThrow(java.lang.String columnName) throws java.lang.IllegalArgumentException {
        return mCursor.getColumnIndexOrThrow(columnName);
    }

    @java.lang.Override
    public java.lang.String getColumnName(int columnIndex) {
        return mCursor.getColumnName(columnIndex);
    }

    @java.lang.Override
    public java.lang.String[] getColumnNames() {
        return mCursor.getColumnNames();
    }

    @java.lang.Override
    public double getDouble(int columnIndex) {
        return mCursor.getDouble(columnIndex);
    }

    @java.lang.Override
    public void setExtras(android.os.Bundle extras) {
        mCursor.setExtras(extras);
    }

    @java.lang.Override
    public android.os.Bundle getExtras() {
        return mCursor.getExtras();
    }

    @java.lang.Override
    public float getFloat(int columnIndex) {
        return mCursor.getFloat(columnIndex);
    }

    @java.lang.Override
    public int getInt(int columnIndex) {
        return mCursor.getInt(columnIndex);
    }

    @java.lang.Override
    public long getLong(int columnIndex) {
        return mCursor.getLong(columnIndex);
    }

    @java.lang.Override
    public short getShort(int columnIndex) {
        return mCursor.getShort(columnIndex);
    }

    @java.lang.Override
    public java.lang.String getString(int columnIndex) {
        return mCursor.getString(columnIndex);
    }

    @java.lang.Override
    public void copyStringToBuffer(int columnIndex, android.database.CharArrayBuffer buffer) {
        mCursor.copyStringToBuffer(columnIndex, buffer);
    }

    @java.lang.Override
    public byte[] getBlob(int columnIndex) {
        return mCursor.getBlob(columnIndex);
    }

    @java.lang.Override
    public boolean getWantsAllOnMoveCalls() {
        return mCursor.getWantsAllOnMoveCalls();
    }

    @java.lang.Override
    public boolean isAfterLast() {
        return mCursor.isAfterLast();
    }

    @java.lang.Override
    public boolean isBeforeFirst() {
        return mCursor.isBeforeFirst();
    }

    @java.lang.Override
    public boolean isFirst() {
        return mCursor.isFirst();
    }

    @java.lang.Override
    public boolean isLast() {
        return mCursor.isLast();
    }

    @java.lang.Override
    public int getType(int columnIndex) {
        return mCursor.getType(columnIndex);
    }

    @java.lang.Override
    public boolean isNull(int columnIndex) {
        return mCursor.isNull(columnIndex);
    }

    @java.lang.Override
    public boolean moveToLast() {
        return mCursor.moveToLast();
    }

    @java.lang.Override
    public boolean move(int offset) {
        return mCursor.move(offset);
    }

    @java.lang.Override
    public boolean moveToPosition(int position) {
        return mCursor.moveToPosition(position);
    }

    @java.lang.Override
    public boolean moveToNext() {
        return mCursor.moveToNext();
    }

    @java.lang.Override
    public int getPosition() {
        return mCursor.getPosition();
    }

    @java.lang.Override
    public boolean moveToPrevious() {
        return mCursor.moveToPrevious();
    }

    @java.lang.Override
    public void registerContentObserver(android.database.ContentObserver observer) {
        mCursor.registerContentObserver(observer);
    }

    @java.lang.Override
    public void registerDataSetObserver(android.database.DataSetObserver observer) {
        mCursor.registerDataSetObserver(observer);
    }

    @java.lang.Override
    @java.lang.Deprecated
    public boolean requery() {
        return mCursor.requery();
    }

    @java.lang.Override
    public android.os.Bundle respond(android.os.Bundle extras) {
        return mCursor.respond(extras);
    }

    @java.lang.Override
    public void setNotificationUri(android.content.ContentResolver cr, android.net.Uri uri) {
        mCursor.setNotificationUri(cr, uri);
    }

    @java.lang.Override
    public android.net.Uri getNotificationUri() {
        return mCursor.getNotificationUri();
    }

    @java.lang.Override
    public void unregisterContentObserver(android.database.ContentObserver observer) {
        mCursor.unregisterContentObserver(observer);
    }

    @java.lang.Override
    public void unregisterDataSetObserver(android.database.DataSetObserver observer) {
        mCursor.unregisterDataSetObserver(observer);
    }
}

