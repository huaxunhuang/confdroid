/**
 * Copyright (C) 2010 The Android Open Source Project
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
 * limitations under the License
 */
package android.provider;


/**
 * <p>
 * The contract between the browser provider and applications. Contains the definition
 * for the supported URIS and columns.
 * </p>
 * <h3>Overview</h3>
 * <p>
 * BrowserContract defines an database of browser-related information which are bookmarks,
 * history, images and the mapping between the image and URL.
 * </p>
 *
 * @unknown 
 */
public class BrowserContract {
    /**
     * The authority for the browser provider
     */
    public static final java.lang.String AUTHORITY = "com.android.browser";

    /**
     * A content:// style uri to the authority for the browser provider
     */
    public static final android.net.Uri AUTHORITY_URI = android.net.Uri.parse("content://" + android.provider.BrowserContract.AUTHORITY);

    /**
     * An optional insert, update or delete URI parameter that allows the caller
     * to specify that it is a sync adapter. The default value is false. If true
     * the dirty flag is not automatically set and the "syncToNetwork" parameter
     * is set to false when calling
     * {@link ContentResolver#notifyChange(android.net.Uri, android.database.ContentObserver, boolean)}.
     *
     * @unknown 
     */
    public static final java.lang.String CALLER_IS_SYNCADAPTER = "caller_is_syncadapter";

    /**
     * A parameter for use when querying any table that allows specifying a limit on the number
     * of rows returned.
     *
     * @unknown 
     */
    public static final java.lang.String PARAM_LIMIT = "limit";

    /**
     * Generic columns for use by sync adapters. The specific functions of
     * these columns are private to the sync adapter. Other clients of the API
     * should not attempt to either read or write these columns.
     *
     * @unknown 
     */
    interface BaseSyncColumns {
        /**
         * Generic column for use by sync adapters.
         */
        public static final java.lang.String SYNC1 = "sync1";

        /**
         * Generic column for use by sync adapters.
         */
        public static final java.lang.String SYNC2 = "sync2";

        /**
         * Generic column for use by sync adapters.
         */
        public static final java.lang.String SYNC3 = "sync3";

        /**
         * Generic column for use by sync adapters.
         */
        public static final java.lang.String SYNC4 = "sync4";

        /**
         * Generic column for use by sync adapters.
         */
        public static final java.lang.String SYNC5 = "sync5";
    }

    /**
     * Convenience definitions for use in implementing chrome bookmarks sync in the Bookmarks table.
     *
     * @unknown 
     */
    public static final class ChromeSyncColumns {
        private ChromeSyncColumns() {
        }

        /**
         * The server unique ID for an item
         */
        public static final java.lang.String SERVER_UNIQUE = android.provider.BrowserContract.BaseSyncColumns.SYNC3;

        public static final java.lang.String FOLDER_NAME_ROOT = "google_chrome";

        public static final java.lang.String FOLDER_NAME_BOOKMARKS = "google_chrome_bookmarks";

        public static final java.lang.String FOLDER_NAME_BOOKMARKS_BAR = "bookmark_bar";

        public static final java.lang.String FOLDER_NAME_OTHER_BOOKMARKS = "other_bookmarks";

        /**
         * The client unique ID for an item
         */
        public static final java.lang.String CLIENT_UNIQUE = android.provider.BrowserContract.BaseSyncColumns.SYNC4;
    }

    /**
     * Columns that appear when each row of a table belongs to a specific
     * account, including sync information that an account may need.
     *
     * @unknown 
     */
    interface SyncColumns extends android.provider.BrowserContract.BaseSyncColumns {
        /**
         * The name of the account instance to which this row belongs, which when paired with
         * {@link #ACCOUNT_TYPE} identifies a specific account.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String ACCOUNT_NAME = "account_name";

        /**
         * The type of account to which this row belongs, which when paired with
         * {@link #ACCOUNT_NAME} identifies a specific account.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String ACCOUNT_TYPE = "account_type";

        /**
         * String that uniquely identifies this row to its source account.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String SOURCE_ID = "sourceid";

        /**
         * Version number that is updated whenever this row or its related data
         * changes.
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String VERSION = "version";

        /**
         * Flag indicating that {@link #VERSION} has changed, and this row needs
         * to be synchronized by its owning account.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final java.lang.String DIRTY = "dirty";

        /**
         * The time that this row was last modified by a client (msecs since the epoch).
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String DATE_MODIFIED = "modified";
    }

    interface CommonColumns {
        /**
         * The unique ID for a row.
         * <P>Type: INTEGER (long)</P>
         */
        public static final java.lang.String _ID = "_id";

        /**
         * This column is valid when the row is a URL. The history table's URL
         * can not be updated.
         * <P>Type: TEXT (URL)</P>
         */
        public static final java.lang.String URL = "url";

        /**
         * The user visible title.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String TITLE = "title";

        /**
         * The time that this row was created on its originating client (msecs
         * since the epoch).
         * <P>Type: INTEGER</P>
         *
         * @unknown 
         */
        public static final java.lang.String DATE_CREATED = "created";
    }

    /**
     *
     *
     * @unknown 
     */
    interface ImageColumns {
        /**
         * The favicon of the bookmark, may be NULL.
         * Must decode via {@link BitmapFactory#decodeByteArray}.
         * <p>Type: BLOB (image)</p>
         */
        public static final java.lang.String FAVICON = "favicon";

        /**
         * A thumbnail of the page,may be NULL.
         * Must decode via {@link BitmapFactory#decodeByteArray}.
         * <p>Type: BLOB (image)</p>
         */
        public static final java.lang.String THUMBNAIL = "thumbnail";

        /**
         * The touch icon for the web page, may be NULL.
         * Must decode via {@link BitmapFactory#decodeByteArray}.
         * <p>Type: BLOB (image)</p>
         */
        public static final java.lang.String TOUCH_ICON = "touch_icon";
    }

    interface HistoryColumns {
        /**
         * The date the item was last visited, in milliseconds since the epoch.
         * <p>Type: INTEGER (date in milliseconds since January 1, 1970)</p>
         */
        public static final java.lang.String DATE_LAST_VISITED = "date";

        /**
         * The number of times the item has been visited.
         * <p>Type: INTEGER</p>
         */
        public static final java.lang.String VISITS = "visits";

        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String USER_ENTERED = "user_entered";
    }

    interface ImageMappingColumns {
        /**
         * The ID of the image in Images. One image can map onto the multiple URLs.
         * <P>Type: INTEGER (long)</P>
         */
        public static final java.lang.String IMAGE_ID = "image_id";

        /**
         * The URL. The URL can map onto the different type of images.
         * <P>Type: TEXT (URL)</P>
         */
        public static final java.lang.String URL = "url";
    }

    /**
     * The bookmarks table, which holds the user's browser bookmarks.
     */
    public static final class Bookmarks implements android.provider.BrowserContract.CommonColumns , android.provider.BrowserContract.ImageColumns , android.provider.BrowserContract.SyncColumns {
        /**
         * This utility class cannot be instantiated.
         */
        private Bookmarks() {
        }

        /**
         * The content:// style URI for this table
         */
        public static final android.net.Uri CONTENT_URI = android.net.Uri.withAppendedPath(android.provider.BrowserContract.AUTHORITY_URI, "bookmarks");

        /**
         * Used in {@link Bookmarks#TYPE} column and indicats the row is a bookmark.
         */
        public static final int BOOKMARK_TYPE_BOOKMARK = 1;

        /**
         * Used in {@link Bookmarks#TYPE} column and indicats the row is a folder.
         */
        public static final int BOOKMARK_TYPE_FOLDER = 2;

        /**
         * Used in {@link Bookmarks#TYPE} column and indicats the row is the bookmark bar folder.
         */
        public static final int BOOKMARK_TYPE_BOOKMARK_BAR_FOLDER = 3;

        /**
         * Used in {@link Bookmarks#TYPE} column and indicats the row is other folder and
         */
        public static final int BOOKMARK_TYPE_OTHER_FOLDER = 4;

        /**
         * Used in {@link Bookmarks#TYPE} column and indicats the row is other folder, .
         */
        public static final int BOOKMARK_TYPE_MOBILE_FOLDER = 5;

        /**
         * The type of the item.
         * <P>Type: INTEGER</P>
         * <p>Allowed values are:</p>
         * <p>
         * <ul>
         * <li>{@link #BOOKMARK_TYPE_BOOKMARK}</li>
         * <li>{@link #BOOKMARK_TYPE_FOLDER}</li>
         * <li>{@link #BOOKMARK_TYPE_BOOKMARK_BAR_FOLDER}</li>
         * <li>{@link #BOOKMARK_TYPE_OTHER_FOLDER}</li>
         * <li>{@link #BOOKMARK_TYPE_MOBILE_FOLDER}</li>
         * </ul>
         * </p>
         * <p> The TYPE_BOOKMARK_BAR_FOLDER, TYPE_OTHER_FOLDER and TYPE_MOBILE_FOLDER
         * can not be updated or deleted.</p>
         */
        public static final java.lang.String TYPE = "type";

        /**
         * The content:// style URI for the default folder
         *
         * @unknown 
         */
        public static final android.net.Uri CONTENT_URI_DEFAULT_FOLDER = android.net.Uri.withAppendedPath(android.provider.BrowserContract.Bookmarks.CONTENT_URI, "folder");

        /**
         * Query parameter used to specify an account name
         *
         * @unknown 
         */
        public static final java.lang.String PARAM_ACCOUNT_NAME = "acct_name";

        /**
         * Query parameter used to specify an account type
         *
         * @unknown 
         */
        public static final java.lang.String PARAM_ACCOUNT_TYPE = "acct_type";

        /**
         * Builds a URI that points to a specific folder.
         *
         * @param folderId
         * 		the ID of the folder to point to
         * @unknown 
         */
        public static final android.net.Uri buildFolderUri(long folderId) {
            return android.content.ContentUris.withAppendedId(android.provider.BrowserContract.Bookmarks.CONTENT_URI_DEFAULT_FOLDER, folderId);
        }

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of bookmarks.
         */
        public static final java.lang.String CONTENT_TYPE = "vnd.android.cursor.dir/bookmark";

        /**
         * The MIME type of a {@link #CONTENT_URI} of a single bookmark.
         */
        public static final java.lang.String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/bookmark";

        /**
         * Query parameter to use if you want to see deleted bookmarks that are still
         * around on the device and haven't been synced yet.
         *
         * @see #IS_DELETED
         * @unknown 
         */
        public static final java.lang.String QUERY_PARAMETER_SHOW_DELETED = "show_deleted";

        /**
         * Flag indicating if an item is a folder or bookmark. Non-zero values indicate
         * a folder and zero indicates a bookmark.
         * <P>Type: INTEGER (boolean)</P>
         *
         * @unknown 
         */
        public static final java.lang.String IS_FOLDER = "folder";

        /**
         * The ID of the parent folder. ID 0 is the root folder.
         * <P>Type: INTEGER (reference to item in the same table)</P>
         */
        public static final java.lang.String PARENT = "parent";

        /**
         * The source ID for an item's parent. Read-only.
         *
         * @see #PARENT
         * @unknown 
         */
        public static final java.lang.String PARENT_SOURCE_ID = "parent_source";

        /**
         * The position of the bookmark in relation to it's siblings that share the same
         * {@link #PARENT}. May be negative.
         * <P>Type: INTEGER</P>
         *
         * @unknown 
         */
        public static final java.lang.String POSITION = "position";

        /**
         * The item that the bookmark should be inserted after.
         * May be negative.
         * <P>Type: INTEGER</P>
         *
         * @unknown 
         */
        public static final java.lang.String INSERT_AFTER = "insert_after";

        /**
         * The source ID for the item that the bookmark should be inserted after. Read-only.
         * May be negative.
         * <P>Type: INTEGER</P>
         *
         * @see #INSERT_AFTER
         * @unknown 
         */
        public static final java.lang.String INSERT_AFTER_SOURCE_ID = "insert_after_source";

        /**
         * A flag to indicate if an item has been deleted. Queries will not return deleted
         * entries unless you add the {@link #QUERY_PARAMETER_SHOW_DELETED} query paramter
         * to the URI when performing your query.
         * <p>Type: INTEGER (non-zero if the item has been deleted, zero if it hasn't)
         *
         * @see #QUERY_PARAMETER_SHOW_DELETED
         * @unknown 
         */
        public static final java.lang.String IS_DELETED = "deleted";
    }

    /**
     * Read-only table that lists all the accounts that are used to provide bookmarks.
     *
     * @unknown 
     */
    public static final class Accounts {
        /**
         * Directory under {@link Bookmarks#CONTENT_URI}
         */
        public static final android.net.Uri CONTENT_URI = android.provider.BrowserContract.AUTHORITY_URI.buildUpon().appendPath("accounts").build();

        /**
         * The name of the account instance to which this row belongs, which when paired with
         * {@link #ACCOUNT_TYPE} identifies a specific account.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String ACCOUNT_NAME = "account_name";

        /**
         * The type of account to which this row belongs, which when paired with
         * {@link #ACCOUNT_NAME} identifies a specific account.
         * <P>Type: TEXT</P>
         */
        public static final java.lang.String ACCOUNT_TYPE = "account_type";

        /**
         * The ID of the account's root folder. This will be the ID of the folder
         * returned when querying {@link Bookmarks#CONTENT_URI_DEFAULT_FOLDER}.
         * <P>Type: INTEGER</P>
         */
        public static final java.lang.String ROOT_ID = "root_id";
    }

    /**
     * The history table, which holds the browsing history.
     */
    public static final class History implements android.provider.BrowserContract.CommonColumns , android.provider.BrowserContract.HistoryColumns , android.provider.BrowserContract.ImageColumns {
        /**
         * This utility class cannot be instantiated.
         */
        private History() {
        }

        /**
         * The content:// style URI for this table
         */
        public static final android.net.Uri CONTENT_URI = android.net.Uri.withAppendedPath(android.provider.BrowserContract.AUTHORITY_URI, "history");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of browser history items.
         */
        public static final java.lang.String CONTENT_TYPE = "vnd.android.cursor.dir/browser-history";

        /**
         * The MIME type of a {@link #CONTENT_URI} of a single browser history item.
         */
        public static final java.lang.String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/browser-history";
    }

    /**
     * The search history table.
     *
     * @unknown 
     */
    public static final class Searches {
        private Searches() {
        }

        /**
         * The content:// style URI for this table
         */
        public static final android.net.Uri CONTENT_URI = android.net.Uri.withAppendedPath(android.provider.BrowserContract.AUTHORITY_URI, "searches");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of browser search items.
         */
        public static final java.lang.String CONTENT_TYPE = "vnd.android.cursor.dir/searches";

        /**
         * The MIME type of a {@link #CONTENT_URI} of a single browser search item.
         */
        public static final java.lang.String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/searches";

        /**
         * The unique ID for a row.
         * <P>Type: INTEGER (long)</P>
         */
        public static final java.lang.String _ID = "_id";

        /**
         * The user entered search term.
         */
        public static final java.lang.String SEARCH = "search";

        /**
         * The date the search was performed, in milliseconds since the epoch.
         * <p>Type: NUMBER (date in milliseconds since January 1, 1970)</p>
         */
        public static final java.lang.String DATE = "date";
    }

    /**
     * A table provided for sync adapters to use for storing private sync state data.
     *
     * @see SyncStateContract
     * @unknown 
     */
    public static final class SyncState implements android.provider.SyncStateContract.Columns {
        /**
         * This utility class cannot be instantiated
         */
        private SyncState() {
        }

        public static final java.lang.String CONTENT_DIRECTORY = android.provider.SyncStateContract.Constants.CONTENT_DIRECTORY;

        /**
         * The content:// style URI for this table
         */
        public static final android.net.Uri CONTENT_URI = android.net.Uri.withAppendedPath(android.provider.BrowserContract.AUTHORITY_URI, android.provider.BrowserContract.SyncState.CONTENT_DIRECTORY);

        /**
         *
         *
         * @see android.provider.SyncStateContract.Helpers#get
         */
        public static byte[] get(android.content.ContentProviderClient provider, android.accounts.Account account) throws android.os.RemoteException {
            return android.provider.SyncStateContract.Helpers.get(provider, android.provider.BrowserContract.SyncState.CONTENT_URI, account);
        }

        /**
         *
         *
         * @see android.provider.SyncStateContract.Helpers#get
         */
        public static android.util.Pair<android.net.Uri, byte[]> getWithUri(android.content.ContentProviderClient provider, android.accounts.Account account) throws android.os.RemoteException {
            return android.provider.SyncStateContract.Helpers.getWithUri(provider, android.provider.BrowserContract.SyncState.CONTENT_URI, account);
        }

        /**
         *
         *
         * @see android.provider.SyncStateContract.Helpers#set
         */
        public static void set(android.content.ContentProviderClient provider, android.accounts.Account account, byte[] data) throws android.os.RemoteException {
            android.provider.SyncStateContract.Helpers.set(provider, android.provider.BrowserContract.SyncState.CONTENT_URI, account, data);
        }

        /**
         *
         *
         * @see android.provider.SyncStateContract.Helpers#newSetOperation
         */
        public static android.content.ContentProviderOperation newSetOperation(android.accounts.Account account, byte[] data) {
            return android.provider.SyncStateContract.Helpers.newSetOperation(android.provider.BrowserContract.SyncState.CONTENT_URI, account, data);
        }
    }

    /**
     * <p>
     * Stores images for URLs.
     * </p>
     * <p>
     * The rows in this table can not be updated since there might have multiple URLs mapping onto
     * the same image. If you want to update a URL's image, you need to add the new image in this
     * table, then update the mapping onto the added image.
     * </p>
     * <p>
     * Every image should be at least associated with one URL, otherwise it will be removed after a
     * while.
     * </p>
     */
    public static final class Images implements android.provider.BrowserContract.ImageColumns {
        /**
         * This utility class cannot be instantiated
         */
        private Images() {
        }

        /**
         * The content:// style URI for this table
         */
        public static final android.net.Uri CONTENT_URI = android.net.Uri.withAppendedPath(android.provider.BrowserContract.AUTHORITY_URI, "images");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of images.
         */
        public static final java.lang.String CONTENT_TYPE = "vnd.android.cursor.dir/images";

        /**
         * The MIME type of a {@link #CONTENT_URI} of a single image.
         */
        public static final java.lang.String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/images";

        /**
         * Used in {@link Images#TYPE} column and indicats the row is a favicon.
         */
        public static final int IMAGE_TYPE_FAVICON = 1;

        /**
         * Used in {@link Images#TYPE} column and indicats the row is a precomposed touch icon.
         */
        public static final int IMAGE_TYPE_PRECOMPOSED_TOUCH_ICON = 2;

        /**
         * Used in {@link Images#TYPE} column and indicats the row is a touch icon.
         */
        public static final int IMAGE_TYPE_TOUCH_ICON = 4;

        /**
         * The type of item in the table.
         * <P>Type: INTEGER</P>
         * <p>Allowed values are:</p>
         * <p>
         * <ul>
         * <li>{@link #IMAGE_TYPE_FAVICON}</li>
         * <li>{@link #IMAGE_TYPE_PRECOMPOSED_TOUCH_ICON}</li>
         * <li>{@link #IMAGE_TYPE_TOUCH_ICON}</li>
         * </ul>
         * </p>
         */
        public static final java.lang.String TYPE = "type";

        /**
         * The image data.
         * <p>Type: BLOB (image)</p>
         */
        public static final java.lang.String DATA = "data";

        /**
         * The URL the images came from.
         * <P>Type: TEXT (URL)</P>
         *
         * @unknown 
         */
        public static final java.lang.String URL = "url_key";
    }

    /**
     * <p>
     * A table that stores the mappings between the image and the URL.
     * </p>
     * <p>
     * Deleting or Updating a mapping might also deletes the mapped image if there is no other URL
     * maps onto it.
     * </p>
     */
    public static final class ImageMappings implements android.provider.BrowserContract.ImageMappingColumns {
        /**
         * This utility class cannot be instantiated
         */
        private ImageMappings() {
        }

        /**
         * The content:// style URI for this table
         */
        public static final android.net.Uri CONTENT_URI = android.net.Uri.withAppendedPath(android.provider.BrowserContract.AUTHORITY_URI, "image_mappings");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of image mappings.
         */
        public static final java.lang.String CONTENT_TYPE = "vnd.android.cursor.dir/image_mappings";

        /**
         * The MIME type of a {@link #CONTENT_URI} of a single image mapping.
         */
        public static final java.lang.String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/image_mappings";
    }

    /**
     * A combined view of bookmarks and history. All bookmarks in all folders are included and
     * no folders are included.
     *
     * @unknown 
     */
    public static final class Combined implements android.provider.BrowserContract.CommonColumns , android.provider.BrowserContract.HistoryColumns , android.provider.BrowserContract.ImageColumns {
        /**
         * This utility class cannot be instantiated
         */
        private Combined() {
        }

        /**
         * The content:// style URI for this table
         */
        public static final android.net.Uri CONTENT_URI = android.net.Uri.withAppendedPath(android.provider.BrowserContract.AUTHORITY_URI, "combined");

        /**
         * Flag indicating that an item is a bookmark. A value of 1 indicates a bookmark, a value
         * of 0 indicates a history item.
         * <p>Type: INTEGER (boolean)</p>
         */
        public static final java.lang.String IS_BOOKMARK = "bookmark";
    }

    /**
     * A table that stores settings specific to the browser. Only support query and insert.
     *
     * @unknown 
     */
    public static final class Settings {
        /**
         * This utility class cannot be instantiated
         */
        private Settings() {
        }

        /**
         * The content:// style URI for this table
         */
        public static final android.net.Uri CONTENT_URI = android.net.Uri.withAppendedPath(android.provider.BrowserContract.AUTHORITY_URI, "settings");

        /**
         * Key for a setting value.
         */
        public static final java.lang.String KEY = "key";

        /**
         * Value for a setting.
         */
        public static final java.lang.String VALUE = "value";

        /**
         * If set to non-0 the user has opted into bookmark sync.
         */
        public static final java.lang.String KEY_SYNC_ENABLED = "sync_enabled";

        /**
         * Returns true if bookmark sync is enabled
         */
        public static boolean isSyncEnabled(android.content.Context context) {
            android.database.Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(android.provider.BrowserContract.Settings.CONTENT_URI, new java.lang.String[]{ android.provider.BrowserContract.Settings.VALUE }, android.provider.BrowserContract.Settings.KEY + "=?", new java.lang.String[]{ android.provider.BrowserContract.Settings.KEY_SYNC_ENABLED }, null);
                if ((cursor == null) || (!cursor.moveToFirst())) {
                    return false;
                }
                return cursor.getInt(0) != 0;
            } finally {
                if (cursor != null)
                    cursor.close();

            }
        }

        /**
         * Sets the bookmark sync enabled setting.
         */
        public static void setSyncEnabled(android.content.Context context, boolean enabled) {
            android.content.ContentValues values = new android.content.ContentValues();
            values.put(android.provider.BrowserContract.Settings.KEY, android.provider.BrowserContract.Settings.KEY_SYNC_ENABLED);
            values.put(android.provider.BrowserContract.Settings.VALUE, enabled ? 1 : 0);
            context.getContentResolver().insert(android.provider.BrowserContract.Settings.CONTENT_URI, values);
        }
    }
}

