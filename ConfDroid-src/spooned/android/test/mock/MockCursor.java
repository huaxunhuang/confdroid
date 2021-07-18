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
package android.test.mock;


/**
 * A mock {@link android.database.Cursor} class that isolates the test code from real
 * Cursor implementation.
 *
 * <p>
 * All methods including ones related to querying the state of the cursor are
 * are non-functional and throw {@link java.lang.UnsupportedOperationException}.
 *
 * @deprecated Use a mocking framework like <a href="https://github.com/mockito/mockito">Mockito</a>.
New tests should be written using the
<a href="{@docRoot }tools/testing-support-library/index.html">Android Testing Support Library</a>.
 */
@java.lang.Deprecated
public class MockCursor implements android.database.Cursor {
    @java.lang.Override
    public int getColumnCount() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public int getColumnIndex(java.lang.String columnName) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public int getColumnIndexOrThrow(java.lang.String columnName) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public java.lang.String getColumnName(int columnIndex) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public java.lang.String[] getColumnNames() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public int getCount() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public boolean isNull(int columnIndex) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public int getInt(int columnIndex) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public long getLong(int columnIndex) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public short getShort(int columnIndex) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public float getFloat(int columnIndex) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public double getDouble(int columnIndex) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public byte[] getBlob(int columnIndex) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public java.lang.String getString(int columnIndex) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public void setExtras(android.os.Bundle extras) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public android.os.Bundle getExtras() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public int getPosition() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public boolean isAfterLast() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public boolean isBeforeFirst() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public boolean isFirst() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public boolean isLast() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public boolean move(int offset) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public boolean moveToFirst() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public boolean moveToLast() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public boolean moveToNext() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public boolean moveToPrevious() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public boolean moveToPosition(int position) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public void copyStringToBuffer(int columnIndex, android.database.CharArrayBuffer buffer) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    @java.lang.Deprecated
    public void deactivate() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public void close() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public boolean isClosed() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    @java.lang.Deprecated
    public boolean requery() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public void registerContentObserver(android.database.ContentObserver observer) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public void registerDataSetObserver(android.database.DataSetObserver observer) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public android.os.Bundle respond(android.os.Bundle extras) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public boolean getWantsAllOnMoveCalls() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public void setNotificationUri(android.content.ContentResolver cr, android.net.Uri uri) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public android.net.Uri getNotificationUri() {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public void unregisterContentObserver(android.database.ContentObserver observer) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public void unregisterDataSetObserver(android.database.DataSetObserver observer) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }

    @java.lang.Override
    public int getType(int columnIndex) {
        throw new java.lang.UnsupportedOperationException("unimplemented mock method");
    }
}

