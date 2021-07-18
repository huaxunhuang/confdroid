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
 * A base class for Cursors that store their data in {@link CursorWindow}s.
 * <p>
 * The cursor owns the cursor window it uses.  When the cursor is closed,
 * its window is also closed.  Likewise, when the window used by the cursor is
 * changed, its old window is closed.  This policy of strict ownership ensures
 * that cursor windows are not leaked.
 * </p><p>
 * Subclasses are responsible for filling the cursor window with data during
 * {@link #onMove(int, int)}, allocating a new cursor window if necessary.
 * During {@link #requery()}, the existing cursor window should be cleared and
 * filled with new data.
 * </p><p>
 * If the contents of the cursor change or become invalid, the old window must be closed
 * (because it is owned by the cursor) and set to null.
 * </p>
 */
public abstract class AbstractWindowedCursor extends android.database.AbstractCursor {
    /**
     * The cursor window owned by this cursor.
     */
    protected android.database.CursorWindow mWindow;

    @java.lang.Override
    public byte[] getBlob(int columnIndex) {
        checkPosition();
        return mWindow.getBlob(mPos, columnIndex);
    }

    @java.lang.Override
    public java.lang.String getString(int columnIndex) {
        checkPosition();
        return mWindow.getString(mPos, columnIndex);
    }

    @java.lang.Override
    public void copyStringToBuffer(int columnIndex, android.database.CharArrayBuffer buffer) {
        checkPosition();
        mWindow.copyStringToBuffer(mPos, columnIndex, buffer);
    }

    @java.lang.Override
    public short getShort(int columnIndex) {
        checkPosition();
        return mWindow.getShort(mPos, columnIndex);
    }

    @java.lang.Override
    public int getInt(int columnIndex) {
        checkPosition();
        return mWindow.getInt(mPos, columnIndex);
    }

    @java.lang.Override
    public long getLong(int columnIndex) {
        checkPosition();
        return mWindow.getLong(mPos, columnIndex);
    }

    @java.lang.Override
    public float getFloat(int columnIndex) {
        checkPosition();
        return mWindow.getFloat(mPos, columnIndex);
    }

    @java.lang.Override
    public double getDouble(int columnIndex) {
        checkPosition();
        return mWindow.getDouble(mPos, columnIndex);
    }

    @java.lang.Override
    public boolean isNull(int columnIndex) {
        checkPosition();
        return mWindow.getType(mPos, columnIndex) == android.database.Cursor.FIELD_TYPE_NULL;
    }

    /**
     *
     *
     * @deprecated Use {@link #getType}
     */
    @java.lang.Deprecated
    public boolean isBlob(int columnIndex) {
        return getType(columnIndex) == android.database.Cursor.FIELD_TYPE_BLOB;
    }

    /**
     *
     *
     * @deprecated Use {@link #getType}
     */
    @java.lang.Deprecated
    public boolean isString(int columnIndex) {
        return getType(columnIndex) == android.database.Cursor.FIELD_TYPE_STRING;
    }

    /**
     *
     *
     * @deprecated Use {@link #getType}
     */
    @java.lang.Deprecated
    public boolean isLong(int columnIndex) {
        return getType(columnIndex) == android.database.Cursor.FIELD_TYPE_INTEGER;
    }

    /**
     *
     *
     * @deprecated Use {@link #getType}
     */
    @java.lang.Deprecated
    public boolean isFloat(int columnIndex) {
        return getType(columnIndex) == android.database.Cursor.FIELD_TYPE_FLOAT;
    }

    @java.lang.Override
    public int getType(int columnIndex) {
        checkPosition();
        return mWindow.getType(mPos, columnIndex);
    }

    @java.lang.Override
    protected void checkPosition() {
        super.checkPosition();
        if (mWindow == null) {
            throw new android.database.StaleDataException("Attempting to access a closed CursorWindow." + "Most probable cause: cursor is deactivated prior to calling this method.");
        }
    }

    @java.lang.Override
    public android.database.CursorWindow getWindow() {
        return mWindow;
    }

    /**
     * Sets a new cursor window for the cursor to use.
     * <p>
     * The cursor takes ownership of the provided cursor window; the cursor window
     * will be closed when the cursor is closed or when the cursor adopts a new
     * cursor window.
     * </p><p>
     * If the cursor previously had a cursor window, then it is closed when the
     * new cursor window is assigned.
     * </p>
     *
     * @param window
     * 		The new cursor window, typically a remote cursor window.
     */
    public void setWindow(android.database.CursorWindow window) {
        if (window != mWindow) {
            closeWindow();
            mWindow = window;
        }
    }

    /**
     * Returns true if the cursor has an associated cursor window.
     *
     * @return True if the cursor has an associated cursor window.
     */
    public boolean hasWindow() {
        return mWindow != null;
    }

    /**
     * Closes the cursor window and sets {@link #mWindow} to null.
     *
     * @unknown 
     */
    protected void closeWindow() {
        if (mWindow != null) {
            mWindow.close();
            mWindow = null;
        }
    }

    /**
     * If there is a window, clear it.
     * Otherwise, creates a new window.
     *
     * @param name
     * 		The window name.
     * @unknown 
     */
    protected void clearOrCreateWindow(java.lang.String name) {
        if (mWindow == null) {
            mWindow = new android.database.CursorWindow(name);
        } else {
            mWindow.clear();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void onDeactivateOrClose() {
        super.onDeactivateOrClose();
        closeWindow();
    }
}

