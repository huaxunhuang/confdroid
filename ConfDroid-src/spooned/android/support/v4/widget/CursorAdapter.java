/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.support.v4.widget;


/**
 * Static library support version of the framework's {@link android.widget.CursorAdapter}.
 * Used to write apps that run on platforms prior to Android 3.0.  When running
 * on Android 3.0 or above, this implementation is still used; it does not try
 * to switch to the framework's implementation.  See the framework SDK
 * documentation for a class overview.
 */
public abstract class CursorAdapter extends android.widget.BaseAdapter implements android.support.v4.widget.CursorFilter.CursorFilterClient , android.widget.Filterable {
    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide }
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    protected boolean mDataValid;

    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide }
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    protected boolean mAutoRequery;

    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide }
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    protected android.database.Cursor mCursor;

    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide }
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    protected android.content.Context mContext;

    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide }
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    protected int mRowIDColumn;

    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide }
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    protected android.support.v4.widget.CursorAdapter.ChangeObserver mChangeObserver;

    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide }
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    protected android.database.DataSetObserver mDataSetObserver;

    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide }
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    protected android.support.v4.widget.CursorFilter mCursorFilter;

    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide }
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    protected android.widget.FilterQueryProvider mFilterQueryProvider;

    /**
     * If set the adapter will call requery() on the cursor whenever a content change
     * notification is delivered. Implies {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     *
     * @deprecated This option is discouraged, as it results in Cursor queries
    being performed on the application's UI thread and thus can cause poor
    responsiveness or even Application Not Responding errors.  As an alternative,
    use {@link android.app.LoaderManager} with a {@link android.content.CursorLoader}.
     */
    @java.lang.Deprecated
    public static final int FLAG_AUTO_REQUERY = 0x1;

    /**
     * If set the adapter will register a content observer on the cursor and will call
     * {@link #onContentChanged()} when a notification comes in.  Be careful when
     * using this flag: you will need to unset the current Cursor from the adapter
     * to avoid leaks due to its registered observers.  This flag is not needed
     * when using a CursorAdapter with a
     * {@link android.content.CursorLoader}.
     */
    public static final int FLAG_REGISTER_CONTENT_OBSERVER = 0x2;

    /**
     * Constructor that always enables auto-requery.
     *
     * @deprecated This option is discouraged, as it results in Cursor queries
    being performed on the application's UI thread and thus can cause poor
    responsiveness or even Application Not Responding errors.  As an alternative,
    use {@link android.app.LoaderManager} with a {@link android.content.CursorLoader}.
     * @param c
     * 		The cursor from which to get the data.
     * @param context
     * 		The context
     */
    @java.lang.Deprecated
    public CursorAdapter(android.content.Context context, android.database.Cursor c) {
        init(context, c, android.support.v4.widget.CursorAdapter.FLAG_AUTO_REQUERY);
    }

    /**
     * Constructor that allows control over auto-requery.  It is recommended
     * you not use this, but instead {@link #CursorAdapter(Context, Cursor, int)}.
     * When using this constructor, {@link #FLAG_REGISTER_CONTENT_OBSERVER}
     * will always be set.
     *
     * @param c
     * 		The cursor from which to get the data.
     * @param context
     * 		The context
     * @param autoRequery
     * 		If true the adapter will call requery() on the
     * 		cursor whenever it changes so the most recent
     * 		data is always displayed.  Using true here is discouraged.
     */
    public CursorAdapter(android.content.Context context, android.database.Cursor c, boolean autoRequery) {
        init(context, c, autoRequery ? android.support.v4.widget.CursorAdapter.FLAG_AUTO_REQUERY : android.support.v4.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    /**
     * Recommended constructor.
     *
     * @param c
     * 		The cursor from which to get the data.
     * @param context
     * 		The context
     * @param flags
     * 		Flags used to determine the behavior of the adapter; may
     * 		be any combination of {@link #FLAG_AUTO_REQUERY} and
     * 		{@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */
    public CursorAdapter(android.content.Context context, android.database.Cursor c, int flags) {
        init(context, c, flags);
    }

    /**
     *
     *
     * @deprecated Don't use this, use the normal constructor.  This will
    be removed in the future.
     */
    @java.lang.Deprecated
    protected void init(android.content.Context context, android.database.Cursor c, boolean autoRequery) {
        init(context, c, autoRequery ? android.support.v4.widget.CursorAdapter.FLAG_AUTO_REQUERY : android.support.v4.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    void init(android.content.Context context, android.database.Cursor c, int flags) {
        if ((flags & android.support.v4.widget.CursorAdapter.FLAG_AUTO_REQUERY) == android.support.v4.widget.CursorAdapter.FLAG_AUTO_REQUERY) {
            flags |= android.support.v4.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER;
            mAutoRequery = true;
        } else {
            mAutoRequery = false;
        }
        boolean cursorPresent = c != null;
        mCursor = c;
        mDataValid = cursorPresent;
        mContext = context;
        mRowIDColumn = (cursorPresent) ? c.getColumnIndexOrThrow("_id") : -1;
        if ((flags & android.support.v4.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) == android.support.v4.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
            mChangeObserver = new android.support.v4.widget.CursorAdapter.ChangeObserver();
            mDataSetObserver = new android.support.v4.widget.CursorAdapter.MyDataSetObserver();
        } else {
            mChangeObserver = null;
            mDataSetObserver = null;
        }
        if (cursorPresent) {
            if (mChangeObserver != null)
                c.registerContentObserver(mChangeObserver);

            if (mDataSetObserver != null)
                c.registerDataSetObserver(mDataSetObserver);

        }
    }

    /**
     * Returns the cursor.
     *
     * @return the cursor.
     */
    @java.lang.Override
    public android.database.Cursor getCursor() {
        return mCursor;
    }

    /**
     *
     *
     * @see android.widget.ListAdapter#getCount()
     */
    @java.lang.Override
    public int getCount() {
        if (mDataValid && (mCursor != null)) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    /**
     *
     *
     * @see android.widget.ListAdapter#getItem(int)
     */
    @java.lang.Override
    public java.lang.Object getItem(int position) {
        if (mDataValid && (mCursor != null)) {
            mCursor.moveToPosition(position);
            return mCursor;
        } else {
            return null;
        }
    }

    /**
     *
     *
     * @see android.widget.ListAdapter#getItemId(int)
     */
    @java.lang.Override
    public long getItemId(int position) {
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

    @java.lang.Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     *
     *
     * @see android.widget.ListAdapter#getView(int, View, ViewGroup)
     */
    @java.lang.Override
    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        if (!mDataValid) {
            throw new java.lang.IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new java.lang.IllegalStateException("couldn't move cursor to position " + position);
        }
        android.view.View v;
        if (convertView == null) {
            v = newView(mContext, mCursor, parent);
        } else {
            v = convertView;
        }
        bindView(v, mContext, mCursor);
        return v;
    }

    @java.lang.Override
    public android.view.View getDropDownView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        if (mDataValid) {
            mCursor.moveToPosition(position);
            android.view.View v;
            if (convertView == null) {
                v = newDropDownView(mContext, mCursor, parent);
            } else {
                v = convertView;
            }
            bindView(v, mContext, mCursor);
            return v;
        } else {
            return null;
        }
    }

    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context
     * 		Interface to application's global information
     * @param cursor
     * 		The cursor from which to get the data. The cursor is already
     * 		moved to the correct position.
     * @param parent
     * 		The parent to which the new view is attached to
     * @return the newly created view.
     */
    public abstract android.view.View newView(android.content.Context context, android.database.Cursor cursor, android.view.ViewGroup parent);

    /**
     * Makes a new drop down view to hold the data pointed to by cursor.
     *
     * @param context
     * 		Interface to application's global information
     * @param cursor
     * 		The cursor from which to get the data. The cursor is already
     * 		moved to the correct position.
     * @param parent
     * 		The parent to which the new view is attached to
     * @return the newly created view.
     */
    public android.view.View newDropDownView(android.content.Context context, android.database.Cursor cursor, android.view.ViewGroup parent) {
        return newView(context, cursor, parent);
    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view
     * 		Existing view, returned earlier by newView
     * @param context
     * 		Interface to application's global information
     * @param cursor
     * 		The cursor from which to get the data. The cursor is already
     * 		moved to the correct position.
     */
    public abstract void bindView(android.view.View view, android.content.Context context, android.database.Cursor cursor);

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     *
     * @param cursor
     * 		The new cursor to be used
     */
    @java.lang.Override
    public void changeCursor(android.database.Cursor cursor) {
        android.database.Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     *
     * @param newCursor
     * 		The new cursor to be used.
     * @return Returns the previously set Cursor, or null if there was not one.
    If the given new Cursor is the same instance is the previously set
    Cursor, null is also returned.
     */
    public android.database.Cursor swapCursor(android.database.Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        android.database.Cursor oldCursor = mCursor;
        if (oldCursor != null) {
            if (mChangeObserver != null)
                oldCursor.unregisterContentObserver(mChangeObserver);

            if (mDataSetObserver != null)
                oldCursor.unregisterDataSetObserver(mDataSetObserver);

        }
        mCursor = newCursor;
        if (newCursor != null) {
            if (mChangeObserver != null)
                newCursor.registerContentObserver(mChangeObserver);

            if (mDataSetObserver != null)
                newCursor.registerDataSetObserver(mDataSetObserver);

            mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            mRowIDColumn = -1;
            mDataValid = false;
            // notify the observers about the lack of a data set
            notifyDataSetInvalidated();
        }
        return oldCursor;
    }

    /**
     * <p>Converts the cursor into a CharSequence. Subclasses should override this
     * method to convert their results. The default implementation returns an
     * empty String for null values or the default String representation of
     * the value.</p>
     *
     * @param cursor
     * 		the cursor to convert to a CharSequence
     * @return a CharSequence representing the value
     */
    @java.lang.Override
    public java.lang.CharSequence convertToString(android.database.Cursor cursor) {
        return cursor == null ? "" : cursor.toString();
    }

    /**
     * Runs a query with the specified constraint. This query is requested
     * by the filter attached to this adapter.
     *
     * The query is provided by a
     * {@link android.widget.FilterQueryProvider}.
     * If no provider is specified, the current cursor is not filtered and returned.
     *
     * After this method returns the resulting cursor is passed to {@link #changeCursor(Cursor)}
     * and the previous cursor is closed.
     *
     * This method is always executed on a background thread, not on the
     * application's main thread (or UI thread.)
     *
     * Contract: when constraint is null or empty, the original results,
     * prior to any filtering, must be returned.
     *
     * @param constraint
     * 		the constraint with which the query must be filtered
     * @return a Cursor representing the results of the new query
     * @see #getFilter()
     * @see #getFilterQueryProvider()
     * @see #setFilterQueryProvider(android.widget.FilterQueryProvider)
     */
    @java.lang.Override
    public android.database.Cursor runQueryOnBackgroundThread(java.lang.CharSequence constraint) {
        if (mFilterQueryProvider != null) {
            return mFilterQueryProvider.runQuery(constraint);
        }
        return mCursor;
    }

    @java.lang.Override
    public android.widget.Filter getFilter() {
        if (mCursorFilter == null) {
            mCursorFilter = new android.support.v4.widget.CursorFilter(this);
        }
        return mCursorFilter;
    }

    /**
     * Returns the query filter provider used for filtering. When the
     * provider is null, no filtering occurs.
     *
     * @return the current filter query provider or null if it does not exist
     * @see #setFilterQueryProvider(android.widget.FilterQueryProvider)
     * @see #runQueryOnBackgroundThread(CharSequence)
     */
    public android.widget.FilterQueryProvider getFilterQueryProvider() {
        return mFilterQueryProvider;
    }

    /**
     * Sets the query filter provider used to filter the current Cursor.
     * The provider's
     * {@link android.widget.FilterQueryProvider#runQuery(CharSequence)}
     * method is invoked when filtering is requested by a client of
     * this adapter.
     *
     * @param filterQueryProvider
     * 		the filter query provider or null to remove it
     * @see #getFilterQueryProvider()
     * @see #runQueryOnBackgroundThread(CharSequence)
     */
    public void setFilterQueryProvider(android.widget.FilterQueryProvider filterQueryProvider) {
        mFilterQueryProvider = filterQueryProvider;
    }

    /**
     * Called when the {@link ContentObserver} on the cursor receives a change notification.
     * The default implementation provides the auto-requery logic, but may be overridden by
     * sub classes.
     *
     * @see ContentObserver#onChange(boolean)
     */
    protected void onContentChanged() {
        if ((mAutoRequery && (mCursor != null)) && (!mCursor.isClosed())) {
            if (false)
                android.util.Log.v("Cursor", ("Auto requerying " + mCursor) + " due to update");

            mDataValid = mCursor.requery();
        }
    }

    private class ChangeObserver extends android.database.ContentObserver {
        ChangeObserver() {
            super(new android.os.Handler());
        }

        @java.lang.Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @java.lang.Override
        public void onChange(boolean selfChange) {
            onContentChanged();
        }
    }

    private class MyDataSetObserver extends android.database.DataSetObserver {
        MyDataSetObserver() {
        }

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

