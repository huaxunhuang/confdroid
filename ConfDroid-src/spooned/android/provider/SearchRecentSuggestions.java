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
package android.provider;


/**
 * This is a utility class providing access to
 * {@link android.content.SearchRecentSuggestionsProvider}.
 *
 * <p>Unlike some utility classes, this one must be instantiated and properly initialized, so that
 * it can be configured to operate with the search suggestions provider that you have created.
 *
 * <p>Typically, you will do this in your searchable activity, each time you receive an incoming
 * {@link android.content.Intent#ACTION_SEARCH ACTION_SEARCH} Intent.  The code to record each
 * incoming query is as follows:
 * <pre class="prettyprint">
 *      SearchSuggestions suggestions = new SearchSuggestions(this,
 *              MySuggestionsProvider.AUTHORITY, MySuggestionsProvider.MODE);
 *      suggestions.saveRecentQuery(queryString, null);
 * </pre>
 *
 * <p>For a working example, see SearchSuggestionSampleProvider and SearchQueryResults in
 * samples/ApiDemos/app.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about using search suggestions in your application, read the
 * <a href="{@docRoot }guide/topics/search/adding-recent-query-suggestions.html">Adding Recent Query
 * Suggestions</a> developer guide.</p>
 * </div>
 */
public class SearchRecentSuggestions {
    // debugging support
    private static final java.lang.String LOG_TAG = "SearchSuggestions";

    // This is a superset of all possible column names (need not all be in table)
    private static class SuggestionColumns implements android.provider.BaseColumns {
        public static final java.lang.String DISPLAY1 = "display1";

        public static final java.lang.String DISPLAY2 = "display2";

        public static final java.lang.String QUERY = "query";

        public static final java.lang.String DATE = "date";
    }

    /* if you change column order you must also change indices below */
    /**
     * This is the database projection that can be used to view saved queries, when
     * configured for one-line operation.
     */
    public static final java.lang.String[] QUERIES_PROJECTION_1LINE = new java.lang.String[]{ android.provider.SearchRecentSuggestions.SuggestionColumns._ID, android.provider.SearchRecentSuggestions.SuggestionColumns.DATE, android.provider.SearchRecentSuggestions.SuggestionColumns.QUERY, android.provider.SearchRecentSuggestions.SuggestionColumns.DISPLAY1 };

    /* if you change column order you must also change indices below */
    /**
     * This is the database projection that can be used to view saved queries, when
     * configured for two-line operation.
     */
    public static final java.lang.String[] QUERIES_PROJECTION_2LINE = new java.lang.String[]{ android.provider.SearchRecentSuggestions.SuggestionColumns._ID, android.provider.SearchRecentSuggestions.SuggestionColumns.DATE, android.provider.SearchRecentSuggestions.SuggestionColumns.QUERY, android.provider.SearchRecentSuggestions.SuggestionColumns.DISPLAY1, android.provider.SearchRecentSuggestions.SuggestionColumns.DISPLAY2 };

    /* these indices depend on QUERIES_PROJECTION_xxx */
    /**
     * Index into the provided query projections.  For use with Cursor.update methods.
     */
    public static final int QUERIES_PROJECTION_DATE_INDEX = 1;

    /**
     * Index into the provided query projections.  For use with Cursor.update methods.
     */
    public static final int QUERIES_PROJECTION_QUERY_INDEX = 2;

    /**
     * Index into the provided query projections.  For use with Cursor.update methods.
     */
    public static final int QUERIES_PROJECTION_DISPLAY1_INDEX = 3;

    /**
     * Index into the provided query projections.  For use with Cursor.update methods.
     */
    public static final int QUERIES_PROJECTION_DISPLAY2_INDEX = 4;// only when 2line active


    /* Set a cap on the count of items in the suggestions table, to
    prevent db and layout operations from dragging to a crawl. Revisit this
    cap when/if db/layout performance improvements are made.
     */
    private static final int MAX_HISTORY_COUNT = 250;

    // client-provided configuration values
    private final android.content.Context mContext;

    private final java.lang.String mAuthority;

    private final boolean mTwoLineDisplay;

    private final android.net.Uri mSuggestionsUri;

    /**
     * Released once per completion of async write.  Used for tests.
     */
    private static final java.util.concurrent.Semaphore sWritesInProgress = new java.util.concurrent.Semaphore(0);

    /**
     * Although provider utility classes are typically static, this one must be constructed
     * because it needs to be initialized using the same values that you provided in your
     * {@link android.content.SearchRecentSuggestionsProvider}.
     *
     * @param authority
     * 		This must match the authority that you've declared in your manifest.
     * @param mode
     * 		You can use mode flags here to determine certain functional aspects of your
     * 		database.  Note, this value should not change from run to run, because when it does change,
     * 		your suggestions database may be wiped.
     * @see android.content.SearchRecentSuggestionsProvider
     * @see android.content.SearchRecentSuggestionsProvider#setupSuggestions
     */
    public SearchRecentSuggestions(android.content.Context context, java.lang.String authority, int mode) {
        if (android.text.TextUtils.isEmpty(authority) || ((mode & android.content.SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES) == 0)) {
            throw new java.lang.IllegalArgumentException();
        }
        // unpack mode flags
        mTwoLineDisplay = 0 != (mode & android.content.SearchRecentSuggestionsProvider.DATABASE_MODE_2LINES);
        // saved values
        mContext = context;
        mAuthority = new java.lang.String(authority);
        // derived values
        mSuggestionsUri = android.net.Uri.parse(("content://" + mAuthority) + "/suggestions");
    }

    /**
     * Add a query to the recent queries list.  Returns immediately, performing the save
     * in the background.
     *
     * @param queryString
     * 		The string as typed by the user.  This string will be displayed as
     * 		the suggestion, and if the user clicks on the suggestion, this string will be sent to your
     * 		searchable activity (as a new search query).
     * @param line2
     * 		If you have configured your recent suggestions provider with
     * 		{@link android.content.SearchRecentSuggestionsProvider#DATABASE_MODE_2LINES}, you can
     * 		pass a second line of text here.  It will be shown in a smaller font, below the primary
     * 		suggestion.  When typing, matches in either line of text will be displayed in the list.
     * 		If you did not configure two-line mode, or if a given suggestion does not have any
     * 		additional text to display, you can pass null here.
     */
    public void saveRecentQuery(final java.lang.String queryString, final java.lang.String line2) {
        if (android.text.TextUtils.isEmpty(queryString)) {
            return;
        }
        if ((!mTwoLineDisplay) && (!android.text.TextUtils.isEmpty(line2))) {
            throw new java.lang.IllegalArgumentException();
        }
        new java.lang.Thread("saveRecentQuery") {
            @java.lang.Override
            public void run() {
                saveRecentQueryBlocking(queryString, line2);
                android.provider.SearchRecentSuggestions.sWritesInProgress.release();
            }
        }.start();
    }

    // Visible for testing.
    void waitForSave() {
        // Acquire writes semaphore until there is nothing available.
        // This is to clean up after any previous callers to saveRecentQuery
        // who did not also call waitForSave().
        do {
            android.provider.SearchRecentSuggestions.sWritesInProgress.acquireUninterruptibly();
        } while (android.provider.SearchRecentSuggestions.sWritesInProgress.availablePermits() > 0 );
    }

    private void saveRecentQueryBlocking(java.lang.String queryString, java.lang.String line2) {
        android.content.ContentResolver cr = mContext.getContentResolver();
        long now = java.lang.System.currentTimeMillis();
        // Use content resolver (not cursor) to insert/update this query
        try {
            android.content.ContentValues values = new android.content.ContentValues();
            values.put(android.provider.SearchRecentSuggestions.SuggestionColumns.DISPLAY1, queryString);
            if (mTwoLineDisplay) {
                values.put(android.provider.SearchRecentSuggestions.SuggestionColumns.DISPLAY2, line2);
            }
            values.put(android.provider.SearchRecentSuggestions.SuggestionColumns.QUERY, queryString);
            values.put(android.provider.SearchRecentSuggestions.SuggestionColumns.DATE, now);
            cr.insert(mSuggestionsUri, values);
        } catch (java.lang.RuntimeException e) {
            android.util.Log.e(android.provider.SearchRecentSuggestions.LOG_TAG, "saveRecentQuery", e);
        }
        // Shorten the list (if it has become too long)
        truncateHistory(cr, android.provider.SearchRecentSuggestions.MAX_HISTORY_COUNT);
    }

    /**
     * Completely delete the history.  Use this call to implement a "clear history" UI.
     *
     * Any application that implements search suggestions based on previous actions (such as
     * recent queries, page/items viewed, etc.) should provide a way for the user to clear the
     * history.  This gives the user a measure of privacy, if they do not wish for their recent
     * searches to be replayed by other users of the device (via suggestions).
     */
    public void clearHistory() {
        android.content.ContentResolver cr = mContext.getContentResolver();
        truncateHistory(cr, 0);
    }

    /**
     * Reduces the length of the history table, to prevent it from growing too large.
     *
     * @param cr
     * 		Convenience copy of the content resolver.
     * @param maxEntries
     * 		Max entries to leave in the table. 0 means remove all entries.
     */
    protected void truncateHistory(android.content.ContentResolver cr, int maxEntries) {
        if (maxEntries < 0) {
            throw new java.lang.IllegalArgumentException();
        }
        try {
            // null means "delete all".  otherwise "delete but leave n newest"
            java.lang.String selection = null;
            if (maxEntries > 0) {
                selection = ((((("_id IN " + ("(SELECT _id FROM suggestions" + " ORDER BY ")) + android.provider.SearchRecentSuggestions.SuggestionColumns.DATE) + " DESC") + " LIMIT -1 OFFSET ") + java.lang.String.valueOf(maxEntries)) + ")";
            }
            cr.delete(mSuggestionsUri, selection, null);
        } catch (java.lang.RuntimeException e) {
            android.util.Log.e(android.provider.SearchRecentSuggestions.LOG_TAG, "truncateHistory", e);
        }
    }
}

