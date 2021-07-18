/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v7.widget;


/**
 * Provides the contents for the suggestion drop-down list.in {@link SearchView}.
 */
class SuggestionsAdapter extends android.support.v4.widget.ResourceCursorAdapter implements android.view.View.OnClickListener {
    private static final boolean DBG = false;

    private static final java.lang.String LOG_TAG = "SuggestionsAdapter";

    private static final int QUERY_LIMIT = 50;

    static final int REFINE_NONE = 0;

    static final int REFINE_BY_ENTRY = 1;

    static final int REFINE_ALL = 2;

    private final android.app.SearchManager mSearchManager;

    private final android.support.v7.widget.SearchView mSearchView;

    private final android.app.SearchableInfo mSearchable;

    private final android.content.Context mProviderContext;

    private final java.util.WeakHashMap<java.lang.String, android.graphics.drawable.Drawable.ConstantState> mOutsideDrawablesCache;

    private final int mCommitIconResId;

    private boolean mClosed = false;

    private int mQueryRefinement = android.support.v7.widget.SuggestionsAdapter.REFINE_BY_ENTRY;

    // URL color
    private android.content.res.ColorStateList mUrlColor;

    static final int INVALID_INDEX = -1;

    // Cached column indexes, updated when the cursor changes.
    private int mText1Col = android.support.v7.widget.SuggestionsAdapter.INVALID_INDEX;

    private int mText2Col = android.support.v7.widget.SuggestionsAdapter.INVALID_INDEX;

    private int mText2UrlCol = android.support.v7.widget.SuggestionsAdapter.INVALID_INDEX;

    private int mIconName1Col = android.support.v7.widget.SuggestionsAdapter.INVALID_INDEX;

    private int mIconName2Col = android.support.v7.widget.SuggestionsAdapter.INVALID_INDEX;

    private int mFlagsCol = android.support.v7.widget.SuggestionsAdapter.INVALID_INDEX;

    // private final Runnable mStartSpinnerRunnable;
    // private final Runnable mStopSpinnerRunnable;
    public SuggestionsAdapter(android.content.Context context, android.support.v7.widget.SearchView searchView, android.app.SearchableInfo searchable, java.util.WeakHashMap<java.lang.String, android.graphics.drawable.Drawable.ConstantState> outsideDrawablesCache) {
        /* no initial cursor */
        /* auto-requery */
        super(context, searchView.getSuggestionRowLayout(), null, true);
        mSearchManager = ((android.app.SearchManager) (mContext.getSystemService(android.content.Context.SEARCH_SERVICE)));
        mSearchView = searchView;
        mSearchable = searchable;
        mCommitIconResId = searchView.getSuggestionCommitIconResId();
        // set up provider resources (gives us icons, etc.)
        mProviderContext = context;
        mOutsideDrawablesCache = outsideDrawablesCache;
    }

    /**
     * Enables query refinement for all suggestions. This means that an additional icon
     * will be shown for each entry. When clicked, the suggested text on that line will be
     * copied to the query text field.
     * <p>
     *
     * @param refine
     * 		which queries to refine. Possible values are {@link #REFINE_NONE},
     * 		{@link #REFINE_BY_ENTRY}, and {@link #REFINE_ALL}.
     */
    public void setQueryRefinement(int refineWhat) {
        mQueryRefinement = refineWhat;
    }

    /**
     * Returns the current query refinement preference.
     *
     * @return value of query refinement preference
     */
    public int getQueryRefinement() {
        return mQueryRefinement;
    }

    /**
     * Overridden to always return <code>false</code>, since we cannot be sure that
     * suggestion sources return stable IDs.
     */
    @java.lang.Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Use the search suggestions provider to obtain a live cursor.  This will be called
     * in a worker thread, so it's OK if the query is slow (e.g. round trip for suggestions).
     * The results will be processed in the UI thread and changeCursor() will be called.
     */
    @java.lang.Override
    public android.database.Cursor runQueryOnBackgroundThread(java.lang.CharSequence constraint) {
        if (android.support.v7.widget.SuggestionsAdapter.DBG)
            android.util.Log.d(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, ("runQueryOnBackgroundThread(" + constraint) + ")");

        java.lang.String query = (constraint == null) ? "" : constraint.toString();
        /**
         * for in app search we show the progress spinner until the cursor is returned with
         * the results.
         */
        android.database.Cursor cursor = null;
        if ((mSearchView.getVisibility() != android.view.View.VISIBLE) || (mSearchView.getWindowVisibility() != android.view.View.VISIBLE)) {
            return null;
        }
        try {
            cursor = getSearchManagerSuggestions(mSearchable, query, android.support.v7.widget.SuggestionsAdapter.QUERY_LIMIT);
            // trigger fill window so the spinner stays up until the results are copied over and
            // closer to being ready
            if (cursor != null) {
                cursor.getCount();
                return cursor;
            }
        } catch (java.lang.RuntimeException e) {
            android.util.Log.w(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, "Search suggestions query threw an exception.", e);
        }
        // If cursor is null or an exception was thrown, stop the spinner and return null.
        // changeCursor doesn't get called if cursor is null
        return null;
    }

    public void close() {
        if (android.support.v7.widget.SuggestionsAdapter.DBG)
            android.util.Log.d(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, "close()");

        changeCursor(null);
        mClosed = true;
    }

    @java.lang.Override
    public void notifyDataSetChanged() {
        if (android.support.v7.widget.SuggestionsAdapter.DBG)
            android.util.Log.d(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, "notifyDataSetChanged");

        super.notifyDataSetChanged();
        updateSpinnerState(getCursor());
    }

    @java.lang.Override
    public void notifyDataSetInvalidated() {
        if (android.support.v7.widget.SuggestionsAdapter.DBG)
            android.util.Log.d(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, "notifyDataSetInvalidated");

        super.notifyDataSetInvalidated();
        updateSpinnerState(getCursor());
    }

    private void updateSpinnerState(android.database.Cursor cursor) {
        android.os.Bundle extras = (cursor != null) ? cursor.getExtras() : null;
        if (android.support.v7.widget.SuggestionsAdapter.DBG) {
            android.util.Log.d(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, "updateSpinnerState - extra = " + (extras != null ? extras.getBoolean(android.app.SearchManager.CURSOR_EXTRA_KEY_IN_PROGRESS) : null));
        }
        // Check if the Cursor indicates that the query is not complete and show the spinner
        if ((extras != null) && extras.getBoolean(android.app.SearchManager.CURSOR_EXTRA_KEY_IN_PROGRESS)) {
            return;
        }
        // If cursor is null or is done, stop the spinner
    }

    /**
     * Cache columns.
     */
    @java.lang.Override
    public void changeCursor(android.database.Cursor c) {
        if (android.support.v7.widget.SuggestionsAdapter.DBG)
            android.util.Log.d(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, ("changeCursor(" + c) + ")");

        if (mClosed) {
            android.util.Log.w(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, "Tried to change cursor after adapter was closed.");
            if (c != null)
                c.close();

            return;
        }
        try {
            super.changeCursor(c);
            if (c != null) {
                mText1Col = c.getColumnIndex(android.app.SearchManager.SUGGEST_COLUMN_TEXT_1);
                mText2Col = c.getColumnIndex(android.app.SearchManager.SUGGEST_COLUMN_TEXT_2);
                mText2UrlCol = c.getColumnIndex(android.app.SearchManager.SUGGEST_COLUMN_TEXT_2_URL);
                mIconName1Col = c.getColumnIndex(android.app.SearchManager.SUGGEST_COLUMN_ICON_1);
                mIconName2Col = c.getColumnIndex(android.app.SearchManager.SUGGEST_COLUMN_ICON_2);
                mFlagsCol = c.getColumnIndex(android.app.SearchManager.SUGGEST_COLUMN_FLAGS);
            }
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, "error changing cursor and caching columns", e);
        }
    }

    /**
     * Tags the view with cached child view look-ups.
     */
    @java.lang.Override
    public android.view.View newView(android.content.Context context, android.database.Cursor cursor, android.view.ViewGroup parent) {
        final android.view.View v = super.newView(context, cursor, parent);
        v.setTag(new android.support.v7.widget.SuggestionsAdapter.ChildViewCache(v));
        // Set up icon.
        final android.widget.ImageView iconRefine = ((android.widget.ImageView) (v.findViewById(R.id.edit_query)));
        iconRefine.setImageResource(mCommitIconResId);
        return v;
    }

    /**
     * Cache of the child views of drop-drown list items, to avoid looking up the children
     * each time the contents of a list item are changed.
     */
    private static final class ChildViewCache {
        public final android.widget.TextView mText1;

        public final android.widget.TextView mText2;

        public final android.widget.ImageView mIcon1;

        public final android.widget.ImageView mIcon2;

        public final android.widget.ImageView mIconRefine;

        public ChildViewCache(android.view.View v) {
            mText1 = ((android.widget.TextView) (v.findViewById(android.R.id.text1)));
            mText2 = ((android.widget.TextView) (v.findViewById(android.R.id.text2)));
            mIcon1 = ((android.widget.ImageView) (v.findViewById(android.R.id.icon1)));
            mIcon2 = ((android.widget.ImageView) (v.findViewById(android.R.id.icon2)));
            mIconRefine = ((android.widget.ImageView) (v.findViewById(R.id.edit_query)));
        }
    }

    @java.lang.Override
    public void bindView(android.view.View view, android.content.Context context, android.database.Cursor cursor) {
        android.support.v7.widget.SuggestionsAdapter.ChildViewCache views = ((android.support.v7.widget.SuggestionsAdapter.ChildViewCache) (view.getTag()));
        int flags = 0;
        if (mFlagsCol != android.support.v7.widget.SuggestionsAdapter.INVALID_INDEX) {
            flags = cursor.getInt(mFlagsCol);
        }
        if (views.mText1 != null) {
            java.lang.String text1 = android.support.v7.widget.SuggestionsAdapter.getStringOrNull(cursor, mText1Col);
            setViewText(views.mText1, text1);
        }
        if (views.mText2 != null) {
            // First check TEXT_2_URL
            java.lang.CharSequence text2 = android.support.v7.widget.SuggestionsAdapter.getStringOrNull(cursor, mText2UrlCol);
            if (text2 != null) {
                text2 = formatUrl(text2);
            } else {
                text2 = android.support.v7.widget.SuggestionsAdapter.getStringOrNull(cursor, mText2Col);
            }
            // If no second line of text is indicated, allow the first line of text
            // to be up to two lines if it wants to be.
            if (android.text.TextUtils.isEmpty(text2)) {
                if (views.mText1 != null) {
                    views.mText1.setSingleLine(false);
                    views.mText1.setMaxLines(2);
                }
            } else {
                if (views.mText1 != null) {
                    views.mText1.setSingleLine(true);
                    views.mText1.setMaxLines(1);
                }
            }
            setViewText(views.mText2, text2);
        }
        if (views.mIcon1 != null) {
            setViewDrawable(views.mIcon1, getIcon1(cursor), android.view.View.INVISIBLE);
        }
        if (views.mIcon2 != null) {
            setViewDrawable(views.mIcon2, getIcon2(cursor), android.view.View.GONE);
        }
        if ((mQueryRefinement == android.support.v7.widget.SuggestionsAdapter.REFINE_ALL) || ((mQueryRefinement == android.support.v7.widget.SuggestionsAdapter.REFINE_BY_ENTRY) && ((flags & android.app.SearchManager.FLAG_QUERY_REFINEMENT) != 0))) {
            views.mIconRefine.setVisibility(android.view.View.VISIBLE);
            views.mIconRefine.setTag(views.mText1.getText());
            views.mIconRefine.setOnClickListener(this);
        } else {
            views.mIconRefine.setVisibility(android.view.View.GONE);
        }
    }

    @java.lang.Override
    public void onClick(android.view.View v) {
        java.lang.Object tag = v.getTag();
        if (tag instanceof java.lang.CharSequence) {
            mSearchView.onQueryRefine(((java.lang.CharSequence) (tag)));
        }
    }

    private java.lang.CharSequence formatUrl(java.lang.CharSequence url) {
        if (mUrlColor == null) {
            // Lazily get the URL color from the current theme.
            android.util.TypedValue colorValue = new android.util.TypedValue();
            mContext.getTheme().resolveAttribute(R.attr.textColorSearchUrl, colorValue, true);
            mUrlColor = mContext.getResources().getColorStateList(colorValue.resourceId);
        }
        android.text.SpannableString text = new android.text.SpannableString(url);
        text.setSpan(new android.text.style.TextAppearanceSpan(null, 0, 0, mUrlColor, null), 0, url.length(), android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    private void setViewText(android.widget.TextView v, java.lang.CharSequence text) {
        // Set the text even if it's null, since we need to clear any previous text.
        v.setText(text);
        if (android.text.TextUtils.isEmpty(text)) {
            v.setVisibility(android.view.View.GONE);
        } else {
            v.setVisibility(android.view.View.VISIBLE);
        }
    }

    private android.graphics.drawable.Drawable getIcon1(android.database.Cursor cursor) {
        if (mIconName1Col == android.support.v7.widget.SuggestionsAdapter.INVALID_INDEX) {
            return null;
        }
        java.lang.String value = cursor.getString(mIconName1Col);
        android.graphics.drawable.Drawable drawable = getDrawableFromResourceValue(value);
        if (drawable != null) {
            return drawable;
        }
        return getDefaultIcon1(cursor);
    }

    private android.graphics.drawable.Drawable getIcon2(android.database.Cursor cursor) {
        if (mIconName2Col == android.support.v7.widget.SuggestionsAdapter.INVALID_INDEX) {
            return null;
        }
        java.lang.String value = cursor.getString(mIconName2Col);
        return getDrawableFromResourceValue(value);
    }

    /**
     * Sets the drawable in an image view, makes sure the view is only visible if there
     * is a drawable.
     */
    private void setViewDrawable(android.widget.ImageView v, android.graphics.drawable.Drawable drawable, int nullVisibility) {
        // Set the icon even if the drawable is null, since we need to clear any
        // previous icon.
        v.setImageDrawable(drawable);
        if (drawable == null) {
            v.setVisibility(nullVisibility);
        } else {
            v.setVisibility(android.view.View.VISIBLE);
            // This is a hack to get any animated drawables (like a 'working' spinner)
            // to animate. You have to setVisible true on an AnimationDrawable to get
            // it to start animating, but it must first have been false or else the
            // call to setVisible will be ineffective. We need to clear up the story
            // about animated drawables in the future, see http://b/1878430.
            drawable.setVisible(false, false);
            drawable.setVisible(true, false);
        }
    }

    /**
     * Gets the text to show in the query field when a suggestion is selected.
     *
     * @param cursor
     * 		The Cursor to read the suggestion data from. The Cursor should already
     * 		be moved to the suggestion that is to be read from.
     * @return The text to show, or <code>null</code> if the query should not be
    changed when selecting this suggestion.
     */
    @java.lang.Override
    public java.lang.CharSequence convertToString(android.database.Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        java.lang.String query = android.support.v7.widget.SuggestionsAdapter.getColumnString(cursor, android.app.SearchManager.SUGGEST_COLUMN_QUERY);
        if (query != null) {
            return query;
        }
        if (mSearchable.shouldRewriteQueryFromData()) {
            java.lang.String data = android.support.v7.widget.SuggestionsAdapter.getColumnString(cursor, android.app.SearchManager.SUGGEST_COLUMN_INTENT_DATA);
            if (data != null) {
                return data;
            }
        }
        if (mSearchable.shouldRewriteQueryFromText()) {
            java.lang.String text1 = android.support.v7.widget.SuggestionsAdapter.getColumnString(cursor, android.app.SearchManager.SUGGEST_COLUMN_TEXT_1);
            if (text1 != null) {
                return text1;
            }
        }
        return null;
    }

    /**
     * This method is overridden purely to provide a bit of protection against
     * flaky content providers.
     *
     * @see android.widget.ListAdapter#getView(int, View, ViewGroup)
     */
    @java.lang.Override
    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        try {
            return super.getView(position, convertView, parent);
        } catch (java.lang.RuntimeException e) {
            android.util.Log.w(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, "Search suggestions cursor threw exception.", e);
            // Put exception string in item title
            android.view.View v = newView(mContext, mCursor, parent);
            if (v != null) {
                android.support.v7.widget.SuggestionsAdapter.ChildViewCache views = ((android.support.v7.widget.SuggestionsAdapter.ChildViewCache) (v.getTag()));
                android.widget.TextView tv = views.mText1;
                tv.setText(e.toString());
            }
            return v;
        }
    }

    /**
     * Gets a drawable given a value provided by a suggestion provider.
     *
     * This value could be just the string value of a resource id
     * (e.g., "2130837524"), in which case we will try to retrieve a drawable from
     * the provider's resources. If the value is not an integer, it is
     * treated as a Uri and opened with
     * {@link ContentResolver#openOutputStream(android.net.Uri, String)}.
     *
     * All resources and URIs are read using the suggestion provider's context.
     *
     * If the string is not formatted as expected, or no drawable can be found for
     * the provided value, this method returns null.
     *
     * @param drawableId
     * 		a string like "2130837524",
     * 		"android.resource://com.android.alarmclock/2130837524",
     * 		or "content://contacts/photos/253".
     * @return a Drawable, or null if none found
     */
    private android.graphics.drawable.Drawable getDrawableFromResourceValue(java.lang.String drawableId) {
        if (((drawableId == null) || (drawableId.length() == 0)) || "0".equals(drawableId)) {
            return null;
        }
        try {
            // First, see if it's just an integer
            int resourceId = java.lang.Integer.parseInt(drawableId);
            // It's an int, look for it in the cache
            java.lang.String drawableUri = (((android.content.ContentResolver.SCHEME_ANDROID_RESOURCE + "://") + mProviderContext.getPackageName()) + "/") + resourceId;
            // Must use URI as cache key, since ints are app-specific
            android.graphics.drawable.Drawable drawable = checkIconCache(drawableUri);
            if (drawable != null) {
                return drawable;
            }
            // Not cached, find it by resource ID
            drawable = android.support.v4.content.ContextCompat.getDrawable(mProviderContext, resourceId);
            // Stick it in the cache, using the URI as key
            storeInIconCache(drawableUri, drawable);
            return drawable;
        } catch (java.lang.NumberFormatException nfe) {
            // It's not an integer, use it as a URI
            android.graphics.drawable.Drawable drawable = checkIconCache(drawableId);
            if (drawable != null) {
                return drawable;
            }
            android.net.Uri uri = android.net.Uri.parse(drawableId);
            drawable = getDrawable(uri);
            storeInIconCache(drawableId, drawable);
            return drawable;
        } catch (android.content.res.Resources.NotFoundException nfe) {
            // It was an integer, but it couldn't be found, bail out
            android.util.Log.w(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, "Icon resource not found: " + drawableId);
            return null;
        }
    }

    /**
     * Gets a drawable by URI, without using the cache.
     *
     * @return A drawable, or {@code null} if the drawable could not be loaded.
     */
    private android.graphics.drawable.Drawable getDrawable(android.net.Uri uri) {
        try {
            java.lang.String scheme = uri.getScheme();
            if (android.content.ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
                // Load drawables through Resources, to get the source density information
                try {
                    return getDrawableFromResourceUri(uri);
                } catch (android.content.res.Resources.NotFoundException ex) {
                    throw new java.io.FileNotFoundException("Resource does not exist: " + uri);
                }
            } else {
                // Let the ContentResolver handle content and file URIs.
                java.io.InputStream stream = mProviderContext.getContentResolver().openInputStream(uri);
                if (stream == null) {
                    throw new java.io.FileNotFoundException("Failed to open " + uri);
                }
                try {
                    return android.graphics.drawable.Drawable.createFromStream(stream, null);
                } finally {
                    try {
                        stream.close();
                    } catch (java.io.IOException ex) {
                        android.util.Log.e(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, "Error closing icon stream for " + uri, ex);
                    }
                }
            }
        } catch (java.io.FileNotFoundException fnfe) {
            android.util.Log.w(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, (("Icon not found: " + uri) + ", ") + fnfe.getMessage());
            return null;
        }
    }

    private android.graphics.drawable.Drawable checkIconCache(java.lang.String resourceUri) {
        android.graphics.drawable.Drawable.ConstantState cached = mOutsideDrawablesCache.get(resourceUri);
        if (cached == null) {
            return null;
        }
        if (android.support.v7.widget.SuggestionsAdapter.DBG)
            android.util.Log.d(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, "Found icon in cache: " + resourceUri);

        return cached.newDrawable();
    }

    private void storeInIconCache(java.lang.String resourceUri, android.graphics.drawable.Drawable drawable) {
        if (drawable != null) {
            mOutsideDrawablesCache.put(resourceUri, drawable.getConstantState());
        }
    }

    /**
     * Gets the left-hand side icon that will be used for the current suggestion
     * if the suggestion contains an icon column but no icon or a broken icon.
     *
     * @param cursor
     * 		A cursor positioned at the current suggestion.
     * @return A non-null drawable.
     */
    private android.graphics.drawable.Drawable getDefaultIcon1(android.database.Cursor cursor) {
        // Check the component that gave us the suggestion
        android.graphics.drawable.Drawable drawable = getActivityIconWithCache(mSearchable.getSearchActivity());
        if (drawable != null) {
            return drawable;
        }
        // Fall back to a default icon
        return mContext.getPackageManager().getDefaultActivityIcon();
    }

    /**
     * Gets the activity or application icon for an activity.
     * Uses the local icon cache for fast repeated lookups.
     *
     * @param component
     * 		Name of an activity.
     * @return A drawable, or {@code null} if neither the activity nor the application
    has an icon set.
     */
    private android.graphics.drawable.Drawable getActivityIconWithCache(android.content.ComponentName component) {
        // First check the icon cache
        java.lang.String componentIconKey = component.flattenToShortString();
        // Using containsKey() since we also store null values.
        if (mOutsideDrawablesCache.containsKey(componentIconKey)) {
            android.graphics.drawable.Drawable.ConstantState cached = mOutsideDrawablesCache.get(componentIconKey);
            return cached == null ? null : cached.newDrawable(mProviderContext.getResources());
        }
        // Then try the activity or application icon
        android.graphics.drawable.Drawable drawable = getActivityIcon(component);
        // Stick it in the cache so we don't do this lookup again.
        android.graphics.drawable.Drawable.ConstantState toCache = (drawable == null) ? null : drawable.getConstantState();
        mOutsideDrawablesCache.put(componentIconKey, toCache);
        return drawable;
    }

    /**
     * Gets the activity or application icon for an activity.
     *
     * @param component
     * 		Name of an activity.
     * @return A drawable, or {@code null} if neither the activity or the application
    have an icon set.
     */
    private android.graphics.drawable.Drawable getActivityIcon(android.content.ComponentName component) {
        android.content.pm.PackageManager pm = mContext.getPackageManager();
        final android.content.pm.ActivityInfo activityInfo;
        try {
            activityInfo = pm.getActivityInfo(component, android.content.pm.PackageManager.GET_META_DATA);
        } catch (android.content.pm.PackageManager.NameNotFoundException ex) {
            android.util.Log.w(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, ex.toString());
            return null;
        }
        int iconId = activityInfo.getIconResource();
        if (iconId == 0)
            return null;

        java.lang.String pkg = component.getPackageName();
        android.graphics.drawable.Drawable drawable = pm.getDrawable(pkg, iconId, activityInfo.applicationInfo);
        if (drawable == null) {
            android.util.Log.w(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, (("Invalid icon resource " + iconId) + " for ") + component.flattenToShortString());
            return null;
        }
        return drawable;
    }

    /**
     * Gets the value of a string column by name.
     *
     * @param cursor
     * 		Cursor to read the value from.
     * @param columnName
     * 		The name of the column to read.
     * @return The value of the given column, or <code>null</null>
    if the cursor does not contain the given column.
     */
    public static java.lang.String getColumnString(android.database.Cursor cursor, java.lang.String columnName) {
        int col = cursor.getColumnIndex(columnName);
        return android.support.v7.widget.SuggestionsAdapter.getStringOrNull(cursor, col);
    }

    private static java.lang.String getStringOrNull(android.database.Cursor cursor, int col) {
        if (col == android.support.v7.widget.SuggestionsAdapter.INVALID_INDEX) {
            return null;
        }
        try {
            return cursor.getString(col);
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.support.v7.widget.SuggestionsAdapter.LOG_TAG, "unexpected error retrieving valid column from cursor, " + "did the remote process die?", e);
            return null;
        }
    }

    /**
     * Import of hidden method: ContentResolver.getResourceId(Uri).
     * Modified to return a drawable, rather than a hidden type.
     */
    android.graphics.drawable.Drawable getDrawableFromResourceUri(android.net.Uri uri) throws java.io.FileNotFoundException {
        java.lang.String authority = uri.getAuthority();
        android.content.res.Resources r;
        if (android.text.TextUtils.isEmpty(authority)) {
            throw new java.io.FileNotFoundException("No authority: " + uri);
        } else {
            try {
                r = mContext.getPackageManager().getResourcesForApplication(authority);
            } catch (android.content.pm.PackageManager.NameNotFoundException ex) {
                throw new java.io.FileNotFoundException("No package found for authority: " + uri);
            }
        }
        java.util.List<java.lang.String> path = uri.getPathSegments();
        if (path == null) {
            throw new java.io.FileNotFoundException("No path: " + uri);
        }
        int len = path.size();
        int id;
        if (len == 1) {
            try {
                id = java.lang.Integer.parseInt(path.get(0));
            } catch (java.lang.NumberFormatException e) {
                throw new java.io.FileNotFoundException("Single path segment is not a resource ID: " + uri);
            }
        } else
            if (len == 2) {
                id = r.getIdentifier(path.get(1), path.get(0), authority);
            } else {
                throw new java.io.FileNotFoundException("More than two path segments: " + uri);
            }

        if (id == 0) {
            throw new java.io.FileNotFoundException("No resource found for: " + uri);
        }
        return r.getDrawable(id);
    }

    /**
     * Import of hidden method: SearchManager.getSuggestions(SearchableInfo, String, int).
     */
    android.database.Cursor getSearchManagerSuggestions(android.app.SearchableInfo searchable, java.lang.String query, int limit) {
        if (searchable == null) {
            return null;
        }
        java.lang.String authority = searchable.getSuggestAuthority();
        if (authority == null) {
            return null;
        }
        android.net.Uri.Builder uriBuilder = // TODO: Remove, workaround for a bug in Uri.writeToParcel()
        new android.net.Uri.Builder().scheme(android.content.ContentResolver.SCHEME_CONTENT).authority(authority).query("").fragment("");// TODO: Remove, workaround for a bug in Uri.writeToParcel()

        // if content path provided, insert it now
        final java.lang.String contentPath = searchable.getSuggestPath();
        if (contentPath != null) {
            uriBuilder.appendEncodedPath(contentPath);
        }
        // append standard suggestion query path
        uriBuilder.appendPath(android.app.SearchManager.SUGGEST_URI_PATH_QUERY);
        // get the query selection, may be null
        java.lang.String selection = searchable.getSuggestSelection();
        // inject query, either as selection args or inline
        java.lang.String[] selArgs = null;
        if (selection != null) {
            // use selection if provided
            selArgs = new java.lang.String[]{ query };
        } else {
            // no selection, use REST pattern
            uriBuilder.appendPath(query);
        }
        if (limit > 0) {
            uriBuilder.appendQueryParameter("limit", java.lang.String.valueOf(limit));
        }
        android.net.Uri uri = uriBuilder.build();
        // finally, make the query
        return mContext.getContentResolver().query(uri, null, selection, selArgs, null);
    }
}

