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
package android.content;


/**
 * This superclass can be used to create a simple search suggestions provider for your application.
 * It creates suggestions (as the user types) based on recent queries and/or recent views.
 *
 * <p>In order to use this class, you must do the following.
 *
 * <ul>
 * <li>Implement and test query search, as described in {@link android.app.SearchManager}.  (This
 * provider will send any suggested queries via the standard
 * {@link android.content.Intent#ACTION_SEARCH ACTION_SEARCH} Intent, which you'll already
 * support once you have implemented and tested basic searchability.)</li>
 * <li>Create a Content Provider within your application by extending
 * {@link android.content.SearchRecentSuggestionsProvider}.  The class you create will be
 * very simple - typically, it will have only a constructor.  But the constructor has a very
 * important responsibility:  When it calls {@link #setupSuggestions(String, int)}, it
 * <i>configures</i> the provider to match the requirements of your searchable activity.</li>
 * <li>Create a manifest entry describing your provider.  Typically this would be as simple
 * as adding the following lines:
 * <pre class="prettyprint">
 *     &lt;!-- Content provider for search suggestions --&gt;
 *     &lt;provider android:name="YourSuggestionProviderClass"
 *               android:authorities="your.suggestion.authority" /&gt;</pre>
 * </li>
 * <li>Please note that you <i>do not</i> instantiate this content provider directly from within
 * your code.  This is done automatically by the system Content Resolver, when the search dialog
 * looks for suggestions.</li>
 * <li>In order for the Content Resolver to do this, you must update your searchable activity's
 * XML configuration file with information about your content provider.  The following additions
 * are usually sufficient:
 * <pre class="prettyprint">
 *     android:searchSuggestAuthority="your.suggestion.authority"
 *     android:searchSuggestSelection=" ? "</pre>
 * </li>
 * <li>In your searchable activities, capture any user-generated queries and record them
 * for future searches by calling {@link android.provider.SearchRecentSuggestions#saveRecentQuery
 * SearchRecentSuggestions.saveRecentQuery()}.</li>
 * </ul>
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about using search suggestions in your application, read the
 * <a href="{@docRoot }guide/topics/search/index.html">Search</a> developer guide.</p>
 * </div>
 *
 * @see android.provider.SearchRecentSuggestions
 */
public class SearchRecentSuggestionsProvider extends android.content.ContentProvider {
    // debugging support
    private static final java.lang.String TAG = "SuggestionsProvider";

    // client-provided configuration values
    private java.lang.String mAuthority;

    private int mMode;

    private boolean mTwoLineDisplay;

    // general database configuration and tables
    private android.database.sqlite.SQLiteOpenHelper mOpenHelper;

    private static final java.lang.String sDatabaseName = "suggestions.db";

    private static final java.lang.String sSuggestions = "suggestions";

    private static final java.lang.String ORDER_BY = "date DESC";

    private static final java.lang.String NULL_COLUMN = "query";

    // Table of database versions.  Don't forget to update!
    // NOTE:  These version values are shifted left 8 bits (x 256) in order to create space for
    // a small set of mode bitflags in the version int.
    // 
    // 1      original implementation with queries, and 1 or 2 display columns
    // 1->2   added UNIQUE constraint to display1 column
    private static final int DATABASE_VERSION = 2 * 256;

    /**
     * This mode bit configures the database to record recent queries.  <i>required</i>
     *
     * @see #setupSuggestions(String, int)
     */
    public static final int DATABASE_MODE_QUERIES = 1;

    /**
     * This mode bit configures the database to include a 2nd annotation line with each entry.
     * <i>optional</i>
     *
     * @see #setupSuggestions(String, int)
     */
    public static final int DATABASE_MODE_2LINES = 2;

    // Uri and query support
    private static final int URI_MATCH_SUGGEST = 1;

    private android.net.Uri mSuggestionsUri;

    private android.content.UriMatcher mUriMatcher;

    private java.lang.String mSuggestSuggestionClause;

    @android.annotation.UnsupportedAppUsage
    private java.lang.String[] mSuggestionProjection;

    /**
     * Builds the database.  This version has extra support for using the version field
     * as a mode flags field, and configures the database columns depending on the mode bits
     * (features) requested by the extending class.
     *
     * @unknown 
     */
    private static class DatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {
        private int mNewVersion;

        public DatabaseHelper(android.content.Context context, int newVersion) {
            super(context, android.content.SearchRecentSuggestionsProvider.sDatabaseName, null, newVersion);
            mNewVersion = newVersion;
        }

        @java.lang.Override
        public void onCreate(android.database.sqlite.SQLiteDatabase db) {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            builder.append("CREATE TABLE suggestions (" + ("_id INTEGER PRIMARY KEY" + ",display1 TEXT UNIQUE ON CONFLICT REPLACE"));
            if (0 != (mNewVersion & android.content.SearchRecentSuggestionsProvider.DATABASE_MODE_2LINES)) {
                builder.append(",display2 TEXT");
            }
            builder.append(",query TEXT" + (",date LONG" + ");"));
            db.execSQL(builder.toString());
        }

        @java.lang.Override
        public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
            android.util.Log.w(android.content.SearchRecentSuggestionsProvider.TAG, ((("Upgrading database from version " + oldVersion) + " to ") + newVersion) + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS suggestions");
            onCreate(db);
        }
    }

    /**
     * In order to use this class, you must extend it, and call this setup function from your
     * constructor.  In your application or activities, you must provide the same values when
     * you create the {@link android.provider.SearchRecentSuggestions} helper.
     *
     * @param authority
     * 		This must match the authority that you've declared in your manifest.
     * @param mode
     * 		You can use mode flags here to determine certain functional aspects of your
     * 		database.  Note, this value should not change from run to run, because when it does change,
     * 		your suggestions database may be wiped.
     * @see #DATABASE_MODE_QUERIES
     * @see #DATABASE_MODE_2LINES
     */
    protected void setupSuggestions(java.lang.String authority, int mode) {
        if (android.text.TextUtils.isEmpty(authority) || ((mode & android.content.SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES) == 0)) {
            throw new java.lang.IllegalArgumentException();
        }
        // unpack mode flags
        mTwoLineDisplay = 0 != (mode & android.content.SearchRecentSuggestionsProvider.DATABASE_MODE_2LINES);
        // saved values
        mAuthority = new java.lang.String(authority);
        mMode = mode;
        // derived values
        mSuggestionsUri = android.net.Uri.parse(("content://" + mAuthority) + "/suggestions");
        mUriMatcher = new android.content.UriMatcher(android.content.UriMatcher.NO_MATCH);
        mUriMatcher.addURI(mAuthority, SearchManager.SUGGEST_URI_PATH_QUERY, android.content.SearchRecentSuggestionsProvider.URI_MATCH_SUGGEST);
        if (mTwoLineDisplay) {
            mSuggestSuggestionClause = "display1 LIKE ? OR display2 LIKE ?";
            mSuggestionProjection = new java.lang.String[]{ "0 AS " + android.app.SearchManager.SUGGEST_COLUMN_FORMAT, (("'android.resource://system/" + com.android.internal.R.drawable.ic_menu_recent_history) + "' AS ") + android.app.SearchManager.SUGGEST_COLUMN_ICON_1, "display1 AS " + android.app.SearchManager.SUGGEST_COLUMN_TEXT_1, "display2 AS " + android.app.SearchManager.SUGGEST_COLUMN_TEXT_2, "query AS " + android.app.SearchManager.SUGGEST_COLUMN_QUERY, "_id" };
        } else {
            mSuggestSuggestionClause = "display1 LIKE ?";
            mSuggestionProjection = new java.lang.String[]{ "0 AS " + android.app.SearchManager.SUGGEST_COLUMN_FORMAT, (("'android.resource://system/" + com.android.internal.R.drawable.ic_menu_recent_history) + "' AS ") + android.app.SearchManager.SUGGEST_COLUMN_ICON_1, "display1 AS " + android.app.SearchManager.SUGGEST_COLUMN_TEXT_1, "query AS " + android.app.SearchManager.SUGGEST_COLUMN_QUERY, "_id" };
        }
    }

    /**
     * This method is provided for use by the ContentResolver.  Do not override, or directly
     * call from your own code.
     */
    @java.lang.Override
    public int delete(android.net.Uri uri, java.lang.String selection, java.lang.String[] selectionArgs) {
        android.database.sqlite.SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int length = uri.getPathSegments().size();
        if (length != 1) {
            throw new java.lang.IllegalArgumentException("Unknown Uri");
        }
        final java.lang.String base = uri.getPathSegments().get(0);
        int count = 0;
        if (base.equals(android.content.SearchRecentSuggestionsProvider.sSuggestions)) {
            count = db.delete(android.content.SearchRecentSuggestionsProvider.sSuggestions, selection, selectionArgs);
        } else {
            throw new java.lang.IllegalArgumentException("Unknown Uri");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /**
     * This method is provided for use by the ContentResolver.  Do not override, or directly
     * call from your own code.
     */
    @java.lang.Override
    public java.lang.String getType(android.net.Uri uri) {
        if (mUriMatcher.match(uri) == android.content.SearchRecentSuggestionsProvider.URI_MATCH_SUGGEST) {
            return android.app.SearchManager.SUGGEST_MIME_TYPE;
        }
        int length = uri.getPathSegments().size();
        if (length >= 1) {
            java.lang.String base = uri.getPathSegments().get(0);
            if (base.equals(android.content.SearchRecentSuggestionsProvider.sSuggestions)) {
                if (length == 1) {
                    return "vnd.android.cursor.dir/suggestion";
                } else
                    if (length == 2) {
                        return "vnd.android.cursor.item/suggestion";
                    }

            }
        }
        throw new java.lang.IllegalArgumentException("Unknown Uri");
    }

    /**
     * This method is provided for use by the ContentResolver.  Do not override, or directly
     * call from your own code.
     */
    @java.lang.Override
    public android.net.Uri insert(android.net.Uri uri, android.content.ContentValues values) {
        android.database.sqlite.SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int length = uri.getPathSegments().size();
        if (length < 1) {
            throw new java.lang.IllegalArgumentException("Unknown Uri");
        }
        // Note:  This table has on-conflict-replace semantics, so insert() may actually replace()
        long rowID = -1;
        java.lang.String base = uri.getPathSegments().get(0);
        android.net.Uri newUri = null;
        if (base.equals(android.content.SearchRecentSuggestionsProvider.sSuggestions)) {
            if (length == 1) {
                rowID = db.insert(android.content.SearchRecentSuggestionsProvider.sSuggestions, android.content.SearchRecentSuggestionsProvider.NULL_COLUMN, values);
                if (rowID > 0) {
                    newUri = android.net.Uri.withAppendedPath(mSuggestionsUri, java.lang.String.valueOf(rowID));
                }
            }
        }
        if (rowID < 0) {
            throw new java.lang.IllegalArgumentException("Unknown Uri");
        }
        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    /**
     * This method is provided for use by the ContentResolver.  Do not override, or directly
     * call from your own code.
     */
    @java.lang.Override
    public boolean onCreate() {
        if ((mAuthority == null) || (mMode == 0)) {
            throw new java.lang.IllegalArgumentException("Provider not configured");
        }
        int mWorkingDbVersion = android.content.SearchRecentSuggestionsProvider.DATABASE_VERSION + mMode;
        mOpenHelper = new android.content.SearchRecentSuggestionsProvider.DatabaseHelper(getContext(), mWorkingDbVersion);
        return true;
    }

    /**
     * This method is provided for use by the ContentResolver.  Do not override, or directly
     * call from your own code.
     */
    // TODO: Confirm no injection attacks here, or rewrite.
    @java.lang.Override
    public android.database.Cursor query(android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder) {
        android.database.sqlite.SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        // special case for actual suggestions (from search manager)
        if (mUriMatcher.match(uri) == android.content.SearchRecentSuggestionsProvider.URI_MATCH_SUGGEST) {
            java.lang.String suggestSelection;
            java.lang.String[] myArgs;
            if (android.text.TextUtils.isEmpty(selectionArgs[0])) {
                suggestSelection = null;
                myArgs = null;
            } else {
                java.lang.String like = ("%" + selectionArgs[0]) + "%";
                if (mTwoLineDisplay) {
                    myArgs = new java.lang.String[]{ like, like };
                } else {
                    myArgs = new java.lang.String[]{ like };
                }
                suggestSelection = mSuggestSuggestionClause;
            }
            // Suggestions are always performed with the default sort order
            android.database.Cursor c = db.query(android.content.SearchRecentSuggestionsProvider.sSuggestions, mSuggestionProjection, suggestSelection, myArgs, null, null, android.content.SearchRecentSuggestionsProvider.ORDER_BY, null);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        }
        // otherwise process arguments and perform a standard query
        int length = uri.getPathSegments().size();
        if ((length != 1) && (length != 2)) {
            throw new java.lang.IllegalArgumentException("Unknown Uri");
        }
        java.lang.String base = uri.getPathSegments().get(0);
        if (!base.equals(android.content.SearchRecentSuggestionsProvider.sSuggestions)) {
            throw new java.lang.IllegalArgumentException("Unknown Uri");
        }
        java.lang.String[] useProjection = null;
        if ((projection != null) && (projection.length > 0)) {
            useProjection = new java.lang.String[projection.length + 1];
            java.lang.System.arraycopy(projection, 0, useProjection, 0, projection.length);
            useProjection[projection.length] = "_id AS _id";
        }
        java.lang.StringBuilder whereClause = new java.lang.StringBuilder(256);
        if (length == 2) {
            whereClause.append("(_id = ").append(uri.getPathSegments().get(1)).append(")");
        }
        // Tack on the user's selection, if present
        if ((selection != null) && (selection.length() > 0)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append('(');
            whereClause.append(selection);
            whereClause.append(')');
        }
        // And perform the generic query as requested
        android.database.Cursor c = db.query(base, useProjection, whereClause.toString(), selectionArgs, null, null, sortOrder, null);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    /**
     * This method is provided for use by the ContentResolver.  Do not override, or directly
     * call from your own code.
     */
    @java.lang.Override
    public int update(android.net.Uri uri, android.content.ContentValues values, java.lang.String selection, java.lang.String[] selectionArgs) {
        throw new java.lang.UnsupportedOperationException("Not implemented");
    }
}

