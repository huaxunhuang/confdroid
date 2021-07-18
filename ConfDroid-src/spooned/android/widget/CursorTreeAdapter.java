/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.widget;


/**
 * An adapter that exposes data from a series of {@link Cursor}s to an
 * {@link ExpandableListView} widget. The top-level {@link Cursor} (that is
 * given in the constructor) exposes the groups, while subsequent {@link Cursor}s
 * returned from {@link #getChildrenCursor(Cursor)} expose children within a
 * particular group. The Cursors must include a column named "_id" or this class
 * will not work.
 */
public abstract class CursorTreeAdapter extends android.widget.BaseExpandableListAdapter implements android.widget.CursorFilter.CursorFilterClient , android.widget.Filterable {
    private android.content.Context mContext;

    private android.os.Handler mHandler;

    private boolean mAutoRequery;

    /**
     * The cursor helper that is used to get the groups
     */
    android.widget.CursorTreeAdapter.MyCursorHelper mGroupCursorHelper;

    /**
     * The map of a group position to the group's children cursor helper (the
     * cursor helper that is used to get the children for that group)
     */
    android.util.SparseArray<android.widget.CursorTreeAdapter.MyCursorHelper> mChildrenCursorHelpers;

    // Filter related
    android.widget.CursorFilter mCursorFilter;

    android.widget.FilterQueryProvider mFilterQueryProvider;

    /**
     * Constructor. The adapter will call {@link Cursor#requery()} on the cursor whenever
     * it changes so that the most recent data is always displayed.
     *
     * @param cursor
     * 		The cursor from which to get the data for the groups.
     */
    public CursorTreeAdapter(android.database.Cursor cursor, android.content.Context context) {
        init(cursor, context, true);
    }

    /**
     * Constructor.
     *
     * @param cursor
     * 		The cursor from which to get the data for the groups.
     * @param context
     * 		The context
     * @param autoRequery
     * 		If true the adapter will call {@link Cursor#requery()}
     * 		on the cursor whenever it changes so the most recent data is
     * 		always displayed.
     */
    public CursorTreeAdapter(android.database.Cursor cursor, android.content.Context context, boolean autoRequery) {
        init(cursor, context, autoRequery);
    }

    private void init(android.database.Cursor cursor, android.content.Context context, boolean autoRequery) {
        mContext = context;
        mHandler = new android.os.Handler();
        mAutoRequery = autoRequery;
        mGroupCursorHelper = new android.widget.CursorTreeAdapter.MyCursorHelper(cursor);
        mChildrenCursorHelpers = new android.util.SparseArray<android.widget.CursorTreeAdapter.MyCursorHelper>();
    }

    /**
     * Gets the cursor helper for the children in the given group.
     *
     * @param groupPosition
     * 		The group whose children will be returned
     * @param requestCursor
     * 		Whether to request a Cursor via
     * 		{@link #getChildrenCursor(Cursor)} (true), or to assume a call
     * 		to {@link #setChildrenCursor(int, Cursor)} will happen shortly
     * 		(false).
     * @return The cursor helper for the children of the given group
     */
    synchronized android.widget.CursorTreeAdapter.MyCursorHelper getChildrenCursorHelper(int groupPosition, boolean requestCursor) {
        android.widget.CursorTreeAdapter.MyCursorHelper cursorHelper = mChildrenCursorHelpers.get(groupPosition);
        if (cursorHelper == null) {
            if (mGroupCursorHelper.moveTo(groupPosition) == null)
                return null;

            final android.database.Cursor cursor = getChildrenCursor(mGroupCursorHelper.getCursor());
            cursorHelper = new android.widget.CursorTreeAdapter.MyCursorHelper(cursor);
            mChildrenCursorHelpers.put(groupPosition, cursorHelper);
        }
        return cursorHelper;
    }

    /**
     * Gets the Cursor for the children at the given group. Subclasses must
     * implement this method to return the children data for a particular group.
     * <p>
     * If you want to asynchronously query a provider to prevent blocking the
     * UI, it is possible to return null and at a later time call
     * {@link #setChildrenCursor(int, Cursor)}.
     * <p>
     * It is your responsibility to manage this Cursor through the Activity
     * lifecycle. It is a good idea to use {@link Activity#managedQuery} which
     * will handle this for you. In some situations, the adapter will deactivate
     * the Cursor on its own, but this will not always be the case, so please
     * ensure the Cursor is properly managed.
     *
     * @param groupCursor
     * 		The cursor pointing to the group whose children cursor
     * 		should be returned
     * @return The cursor for the children of a particular group, or null.
     */
    protected abstract android.database.Cursor getChildrenCursor(android.database.Cursor groupCursor);

    /**
     * Sets the group Cursor.
     *
     * @param cursor
     * 		The Cursor to set for the group. If there is an existing cursor
     * 		it will be closed.
     */
    public void setGroupCursor(android.database.Cursor cursor) {
        mGroupCursorHelper.changeCursor(cursor, false);
    }

    /**
     * Sets the children Cursor for a particular group. If there is an existing cursor
     * it will be closed.
     * <p>
     * This is useful when asynchronously querying to prevent blocking the UI.
     *
     * @param groupPosition
     * 		The group whose children are being set via this Cursor.
     * @param childrenCursor
     * 		The Cursor that contains the children of the group.
     */
    public void setChildrenCursor(int groupPosition, android.database.Cursor childrenCursor) {
        /* Don't request a cursor from the subclass, instead we will be setting
        the cursor ourselves.
         */
        android.widget.CursorTreeAdapter.MyCursorHelper childrenCursorHelper = getChildrenCursorHelper(groupPosition, false);
        /* Don't release any cursor since we know exactly what data is changing
        (this cursor, which is still valid).
         */
        childrenCursorHelper.changeCursor(childrenCursor, false);
    }

    public android.database.Cursor getChild(int groupPosition, int childPosition) {
        // Return this group's children Cursor pointing to the particular child
        return getChildrenCursorHelper(groupPosition, true).moveTo(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return getChildrenCursorHelper(groupPosition, true).getId(childPosition);
    }

    public int getChildrenCount(int groupPosition) {
        android.widget.CursorTreeAdapter.MyCursorHelper helper = getChildrenCursorHelper(groupPosition, true);
        return mGroupCursorHelper.isValid() && (helper != null) ? helper.getCount() : 0;
    }

    public android.database.Cursor getGroup(int groupPosition) {
        // Return the group Cursor pointing to the given group
        return mGroupCursorHelper.moveTo(groupPosition);
    }

    public int getGroupCount() {
        return mGroupCursorHelper.getCount();
    }

    public long getGroupId(int groupPosition) {
        return mGroupCursorHelper.getId(groupPosition);
    }

    public android.view.View getGroupView(int groupPosition, boolean isExpanded, android.view.View convertView, android.view.ViewGroup parent) {
        android.database.Cursor cursor = mGroupCursorHelper.moveTo(groupPosition);
        if (cursor == null) {
            throw new java.lang.IllegalStateException("this should only be called when the cursor is valid");
        }
        android.view.View v;
        if (convertView == null) {
            v = newGroupView(mContext, cursor, isExpanded, parent);
        } else {
            v = convertView;
        }
        bindGroupView(v, mContext, cursor, isExpanded);
        return v;
    }

    /**
     * Makes a new group view to hold the group data pointed to by cursor.
     *
     * @param context
     * 		Interface to application's global information
     * @param cursor
     * 		The group cursor from which to get the data. The cursor is
     * 		already moved to the correct position.
     * @param isExpanded
     * 		Whether the group is expanded.
     * @param parent
     * 		The parent to which the new view is attached to
     * @return The newly created view.
     */
    protected abstract android.view.View newGroupView(android.content.Context context, android.database.Cursor cursor, boolean isExpanded, android.view.ViewGroup parent);

    /**
     * Bind an existing view to the group data pointed to by cursor.
     *
     * @param view
     * 		Existing view, returned earlier by newGroupView.
     * @param context
     * 		Interface to application's global information
     * @param cursor
     * 		The cursor from which to get the data. The cursor is
     * 		already moved to the correct position.
     * @param isExpanded
     * 		Whether the group is expanded.
     */
    protected abstract void bindGroupView(android.view.View view, android.content.Context context, android.database.Cursor cursor, boolean isExpanded);

    public android.view.View getChildView(int groupPosition, int childPosition, boolean isLastChild, android.view.View convertView, android.view.ViewGroup parent) {
        android.widget.CursorTreeAdapter.MyCursorHelper cursorHelper = getChildrenCursorHelper(groupPosition, true);
        android.database.Cursor cursor = cursorHelper.moveTo(childPosition);
        if (cursor == null) {
            throw new java.lang.IllegalStateException("this should only be called when the cursor is valid");
        }
        android.view.View v;
        if (convertView == null) {
            v = newChildView(mContext, cursor, isLastChild, parent);
        } else {
            v = convertView;
        }
        bindChildView(v, mContext, cursor, isLastChild);
        return v;
    }

    /**
     * Makes a new child view to hold the data pointed to by cursor.
     *
     * @param context
     * 		Interface to application's global information
     * @param cursor
     * 		The cursor from which to get the data. The cursor is
     * 		already moved to the correct position.
     * @param isLastChild
     * 		Whether the child is the last child within its group.
     * @param parent
     * 		The parent to which the new view is attached to
     * @return the newly created view.
     */
    protected abstract android.view.View newChildView(android.content.Context context, android.database.Cursor cursor, boolean isLastChild, android.view.ViewGroup parent);

    /**
     * Bind an existing view to the child data pointed to by cursor
     *
     * @param view
     * 		Existing view, returned earlier by newChildView
     * @param context
     * 		Interface to application's global information
     * @param cursor
     * 		The cursor from which to get the data. The cursor is
     * 		already moved to the correct position.
     * @param isLastChild
     * 		Whether the child is the last child within its group.
     */
    protected abstract void bindChildView(android.view.View view, android.content.Context context, android.database.Cursor cursor, boolean isLastChild);

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

    private synchronized void releaseCursorHelpers() {
        for (int pos = mChildrenCursorHelpers.size() - 1; pos >= 0; pos--) {
            mChildrenCursorHelpers.valueAt(pos).deactivate();
        }
        mChildrenCursorHelpers.clear();
    }

    @java.lang.Override
    public void notifyDataSetChanged() {
        notifyDataSetChanged(true);
    }

    /**
     * Notifies a data set change, but with the option of not releasing any
     * cached cursors.
     *
     * @param releaseCursors
     * 		Whether to release and deactivate any cached
     * 		cursors.
     */
    public void notifyDataSetChanged(boolean releaseCursors) {
        if (releaseCursors) {
            releaseCursorHelpers();
        }
        super.notifyDataSetChanged();
    }

    @java.lang.Override
    public void notifyDataSetInvalidated() {
        releaseCursorHelpers();
        super.notifyDataSetInvalidated();
    }

    @java.lang.Override
    public void onGroupCollapsed(int groupPosition) {
        deactivateChildrenCursorHelper(groupPosition);
    }

    /**
     * Deactivates the Cursor and removes the helper from cache.
     *
     * @param groupPosition
     * 		The group whose children Cursor and helper should be
     * 		deactivated.
     */
    synchronized void deactivateChildrenCursorHelper(int groupPosition) {
        android.widget.CursorTreeAdapter.MyCursorHelper cursorHelper = getChildrenCursorHelper(groupPosition, true);
        mChildrenCursorHelpers.remove(groupPosition);
        cursorHelper.deactivate();
    }

    /**
     *
     *
     * @see CursorAdapter#convertToString(Cursor)
     */
    public java.lang.String convertToString(android.database.Cursor cursor) {
        return cursor == null ? "" : cursor.toString();
    }

    /**
     *
     *
     * @see CursorAdapter#runQueryOnBackgroundThread(CharSequence)
     */
    public android.database.Cursor runQueryOnBackgroundThread(java.lang.CharSequence constraint) {
        if (mFilterQueryProvider != null) {
            return mFilterQueryProvider.runQuery(constraint);
        }
        return mGroupCursorHelper.getCursor();
    }

    public android.widget.Filter getFilter() {
        if (mCursorFilter == null) {
            mCursorFilter = new android.widget.CursorFilter(this);
        }
        return mCursorFilter;
    }

    /**
     *
     *
     * @see CursorAdapter#getFilterQueryProvider()
     */
    public android.widget.FilterQueryProvider getFilterQueryProvider() {
        return mFilterQueryProvider;
    }

    /**
     *
     *
     * @see CursorAdapter#setFilterQueryProvider(FilterQueryProvider)
     */
    public void setFilterQueryProvider(android.widget.FilterQueryProvider filterQueryProvider) {
        mFilterQueryProvider = filterQueryProvider;
    }

    /**
     *
     *
     * @see CursorAdapter#changeCursor(Cursor)
     */
    public void changeCursor(android.database.Cursor cursor) {
        mGroupCursorHelper.changeCursor(cursor, true);
    }

    /**
     *
     *
     * @see CursorAdapter#getCursor()
     */
    public android.database.Cursor getCursor() {
        return mGroupCursorHelper.getCursor();
    }

    /**
     * Helper class for Cursor management:
     * <li> Data validity
     * <li> Funneling the content and data set observers from a Cursor to a
     *      single data set observer for widgets
     * <li> ID from the Cursor for use in adapter IDs
     * <li> Swapping cursors but maintaining other metadata
     */
    class MyCursorHelper {
        private android.database.Cursor mCursor;

        private boolean mDataValid;

        private int mRowIDColumn;

        private android.widget.CursorTreeAdapter.MyCursorHelper.MyContentObserver mContentObserver;

        private android.widget.CursorTreeAdapter.MyCursorHelper.MyDataSetObserver mDataSetObserver;

        MyCursorHelper(android.database.Cursor cursor) {
            final boolean cursorPresent = cursor != null;
            mCursor = cursor;
            mDataValid = cursorPresent;
            mRowIDColumn = (cursorPresent) ? cursor.getColumnIndex("_id") : -1;
            mContentObserver = new android.widget.CursorTreeAdapter.MyCursorHelper.MyContentObserver();
            mDataSetObserver = new android.widget.CursorTreeAdapter.MyCursorHelper.MyDataSetObserver();
            if (cursorPresent) {
                cursor.registerContentObserver(mContentObserver);
                cursor.registerDataSetObserver(mDataSetObserver);
            }
        }

        android.database.Cursor getCursor() {
            return mCursor;
        }

        int getCount() {
            if (mDataValid && (mCursor != null)) {
                return mCursor.getCount();
            } else {
                return 0;
            }
        }

        long getId(int position) {
            if (mDataValid && (mCursor != null)) {
                if (mCursor.moveToPosition(position)) {
                    return mCursor.getLong(mRowIDColumn);
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        }

        android.database.Cursor moveTo(int position) {
            if ((mDataValid && (mCursor != null)) && mCursor.moveToPosition(position)) {
                return mCursor;
            } else {
                return null;
            }
        }

        void changeCursor(android.database.Cursor cursor, boolean releaseCursors) {
            if (cursor == mCursor)
                return;

            deactivate();
            mCursor = cursor;
            if (cursor != null) {
                cursor.registerContentObserver(mContentObserver);
                cursor.registerDataSetObserver(mDataSetObserver);
                mRowIDColumn = cursor.getColumnIndex("_id");
                mDataValid = true;
                // notify the observers about the new cursor
                notifyDataSetChanged(releaseCursors);
            } else {
                mRowIDColumn = -1;
                mDataValid = false;
                // notify the observers about the lack of a data set
                notifyDataSetInvalidated();
            }
        }

        void deactivate() {
            if (mCursor == null) {
                return;
            }
            mCursor.unregisterContentObserver(mContentObserver);
            mCursor.unregisterDataSetObserver(mDataSetObserver);
            mCursor.close();
            mCursor = null;
        }

        boolean isValid() {
            return mDataValid && (mCursor != null);
        }

        private class MyContentObserver extends android.database.ContentObserver {
            public MyContentObserver() {
                super(mHandler);
            }

            @java.lang.Override
            public boolean deliverSelfNotifications() {
                return true;
            }

            @java.lang.Override
            public void onChange(boolean selfChange) {
                if ((mAutoRequery && (mCursor != null)) && (!mCursor.isClosed())) {
                    if (false)
                        android.util.Log.v("Cursor", ("Auto requerying " + mCursor) + " due to update");

                    mDataValid = mCursor.requery();
                }
            }
        }

        private class MyDataSetObserver extends android.database.DataSetObserver {
            @java.lang.Override
            public void onChanged() {
                mDataValid = true;
                notifyDataSetChanged();
            }

            @java.lang.Override
            public void onInvalidated() {
                mDataValid = false;
                notifyDataSetInvalidated();
            }
        }
    }
}

