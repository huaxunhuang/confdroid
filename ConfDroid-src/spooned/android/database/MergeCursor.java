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
 * A convience class that lets you present an array of Cursors as a single linear Cursor.
 * The schema of the cursors presented is entirely up to the creator of the MergeCursor, and
 * may be different if that is desired. Calls to getColumns, getColumnIndex, etc will return the
 * value for the row that the MergeCursor is currently pointing at.
 */
public class MergeCursor extends android.database.AbstractCursor {
    private android.database.DataSetObserver mObserver = new android.database.DataSetObserver() {
        @java.lang.Override
        public void onChanged() {
            // Reset our position so the optimizations in move-related code
            // don't screw us over
            mPos = -1;
        }

        @java.lang.Override
        public void onInvalidated() {
            mPos = -1;
        }
    };

    public MergeCursor(android.database.Cursor[] cursors) {
        mCursors = cursors;
        mCursor = cursors[0];
        for (int i = 0; i < mCursors.length; i++) {
            if (mCursors[i] == null)
                continue;

            mCursors[i].registerDataSetObserver(mObserver);
        }
    }

    @java.lang.Override
    public int getCount() {
        int count = 0;
        int length = mCursors.length;
        for (int i = 0; i < length; i++) {
            if (mCursors[i] != null) {
                count += mCursors[i].getCount();
            }
        }
        return count;
    }

    @java.lang.Override
    public boolean onMove(int oldPosition, int newPosition) {
        /* Find the right cursor */
        mCursor = null;
        int cursorStartPos = 0;
        int length = mCursors.length;
        for (int i = 0; i < length; i++) {
            if (mCursors[i] == null) {
                continue;
            }
            if (newPosition < (cursorStartPos + mCursors[i].getCount())) {
                mCursor = mCursors[i];
                break;
            }
            cursorStartPos += mCursors[i].getCount();
        }
        /* Move it to the right position */
        if (mCursor != null) {
            boolean ret = mCursor.moveToPosition(newPosition - cursorStartPos);
            return ret;
        }
        return false;
    }

    @java.lang.Override
    public java.lang.String getString(int column) {
        return mCursor.getString(column);
    }

    @java.lang.Override
    public short getShort(int column) {
        return mCursor.getShort(column);
    }

    @java.lang.Override
    public int getInt(int column) {
        return mCursor.getInt(column);
    }

    @java.lang.Override
    public long getLong(int column) {
        return mCursor.getLong(column);
    }

    @java.lang.Override
    public float getFloat(int column) {
        return mCursor.getFloat(column);
    }

    @java.lang.Override
    public double getDouble(int column) {
        return mCursor.getDouble(column);
    }

    @java.lang.Override
    public int getType(int column) {
        return mCursor.getType(column);
    }

    @java.lang.Override
    public boolean isNull(int column) {
        return mCursor.isNull(column);
    }

    @java.lang.Override
    public byte[] getBlob(int column) {
        return mCursor.getBlob(column);
    }

    @java.lang.Override
    public java.lang.String[] getColumnNames() {
        if (mCursor != null) {
            return mCursor.getColumnNames();
        } else {
            return new java.lang.String[0];
        }
    }

    @java.lang.Override
    public void deactivate() {
        int length = mCursors.length;
        for (int i = 0; i < length; i++) {
            if (mCursors[i] != null) {
                mCursors[i].deactivate();
            }
        }
        super.deactivate();
    }

    @java.lang.Override
    public void close() {
        int length = mCursors.length;
        for (int i = 0; i < length; i++) {
            if (mCursors[i] == null)
                continue;

            mCursors[i].close();
        }
        super.close();
    }

    @java.lang.Override
    public void registerContentObserver(android.database.ContentObserver observer) {
        int length = mCursors.length;
        for (int i = 0; i < length; i++) {
            if (mCursors[i] != null) {
                mCursors[i].registerContentObserver(observer);
            }
        }
    }

    @java.lang.Override
    public void unregisterContentObserver(android.database.ContentObserver observer) {
        int length = mCursors.length;
        for (int i = 0; i < length; i++) {
            if (mCursors[i] != null) {
                mCursors[i].unregisterContentObserver(observer);
            }
        }
    }

    @java.lang.Override
    public void registerDataSetObserver(android.database.DataSetObserver observer) {
        int length = mCursors.length;
        for (int i = 0; i < length; i++) {
            if (mCursors[i] != null) {
                mCursors[i].registerDataSetObserver(observer);
            }
        }
    }

    @java.lang.Override
    public void unregisterDataSetObserver(android.database.DataSetObserver observer) {
        int length = mCursors.length;
        for (int i = 0; i < length; i++) {
            if (mCursors[i] != null) {
                mCursors[i].unregisterDataSetObserver(observer);
            }
        }
    }

    @java.lang.Override
    public boolean requery() {
        int length = mCursors.length;
        for (int i = 0; i < length; i++) {
            if (mCursors[i] == null) {
                continue;
            }
            if (mCursors[i].requery() == false) {
                return false;
            }
        }
        return true;
    }

    private android.database.Cursor mCursor;// updated in onMove


    private android.database.Cursor[] mCursors;
}

