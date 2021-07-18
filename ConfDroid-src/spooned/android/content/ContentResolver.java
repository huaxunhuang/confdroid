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
package android.content;


import android.content.ContentProvider;

import static android.app.UriGrantsManager.getService;
import static android.content.ContentProvider.getUriWithoutUserId;


/**
 * This class provides applications access to the content model.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about using a ContentResolver with content providers, read the
 * <a href="{@docRoot }guide/topics/providers/content-providers.html">Content Providers</a>
 * developer guide.</p>
 * </div>
 */
public abstract class ContentResolver implements android.content.ContentInterface {
    /**
     * Enables logic that supports deprecation of {@code _data} columns,
     * typically by replacing values with fake paths that the OS then offers to
     * redirect to {@link #openFileDescriptor(Uri, String)}, which developers
     * should be using directly.
     *
     * @unknown 
     */
    public static final boolean DEPRECATE_DATA_COLUMNS = android.os.storage.StorageManager.hasIsolatedStorage();

    /**
     * Special filesystem path prefix which indicates that a path should be
     * treated as a {@code content://} {@link Uri} when
     * {@link #DEPRECATE_DATA_COLUMNS} is enabled.
     * <p>
     * The remainder of the path after this prefix is a
     * {@link Uri#getSchemeSpecificPart()} value, which includes authority, path
     * segments, and query parameters.
     *
     * @unknown 
     */
    public static final java.lang.String DEPRECATE_DATA_PREFIX = "/mnt/content/";

    /**
     *
     *
     * @deprecated instead use
    {@link #requestSync(android.accounts.Account, String, android.os.Bundle)}
     */
    @java.lang.Deprecated
    public static final java.lang.String SYNC_EXTRAS_ACCOUNT = "account";

    /**
     * If this extra is set to true, the sync request will be scheduled
     * at the front of the sync request queue and without any delay
     */
    public static final java.lang.String SYNC_EXTRAS_EXPEDITED = "expedited";

    /**
     * If this extra is set to true, the sync request will be scheduled
     * only when the device is plugged in. This is equivalent to calling
     * setRequiresCharging(true) on {@link SyncRequest}.
     */
    public static final java.lang.String SYNC_EXTRAS_REQUIRE_CHARGING = "require_charging";

    /**
     *
     *
     * @deprecated instead use
    {@link #SYNC_EXTRAS_MANUAL}
     */
    @java.lang.Deprecated
    public static final java.lang.String SYNC_EXTRAS_FORCE = "force";

    /**
     * If this extra is set to true then the sync settings (like getSyncAutomatically())
     * are ignored by the sync scheduler.
     */
    public static final java.lang.String SYNC_EXTRAS_IGNORE_SETTINGS = "ignore_settings";

    /**
     * If this extra is set to true then any backoffs for the initial attempt (e.g. due to retries)
     * are ignored by the sync scheduler. If this request fails and gets rescheduled then the
     * retries will still honor the backoff.
     */
    public static final java.lang.String SYNC_EXTRAS_IGNORE_BACKOFF = "ignore_backoff";

    /**
     * If this extra is set to true then the request will not be retried if it fails.
     */
    public static final java.lang.String SYNC_EXTRAS_DO_NOT_RETRY = "do_not_retry";

    /**
     * Setting this extra is the equivalent of setting both {@link #SYNC_EXTRAS_IGNORE_SETTINGS}
     * and {@link #SYNC_EXTRAS_IGNORE_BACKOFF}
     */
    public static final java.lang.String SYNC_EXTRAS_MANUAL = "force";

    /**
     * Indicates that this sync is intended to only upload local changes to the server.
     * For example, this will be set to true if the sync is initiated by a call to
     * {@link ContentResolver#notifyChange(android.net.Uri, android.database.ContentObserver, boolean)}
     */
    public static final java.lang.String SYNC_EXTRAS_UPLOAD = "upload";

    /**
     * Indicates that the sync adapter should proceed with the delete operations,
     * even if it determines that there are too many.
     * See {@link SyncResult#tooManyDeletions}
     */
    public static final java.lang.String SYNC_EXTRAS_OVERRIDE_TOO_MANY_DELETIONS = "deletions_override";

    /**
     * Indicates that the sync adapter should not proceed with the delete operations,
     * if it determines that there are too many.
     * See {@link SyncResult#tooManyDeletions}
     */
    public static final java.lang.String SYNC_EXTRAS_DISCARD_LOCAL_DELETIONS = "discard_deletions";

    /* Extensions to API. TODO: Not clear if we will keep these as public flags. */
    /**
     * {@hide } User-specified flag for expected upload size.
     */
    public static final java.lang.String SYNC_EXTRAS_EXPECTED_UPLOAD = "expected_upload";

    /**
     * {@hide } User-specified flag for expected download size.
     */
    public static final java.lang.String SYNC_EXTRAS_EXPECTED_DOWNLOAD = "expected_download";

    /**
     * {@hide } Priority of this sync with respect to other syncs scheduled for this application.
     */
    public static final java.lang.String SYNC_EXTRAS_PRIORITY = "sync_priority";

    /**
     * {@hide } Flag to allow sync to occur on metered network.
     */
    public static final java.lang.String SYNC_EXTRAS_DISALLOW_METERED = "allow_metered";

    /**
     * {@hide } Integer extra containing a SyncExemption flag.
     *
     * Only the system and the shell user can set it.
     *
     * This extra is "virtual". Once passed to the system server, it'll be removed from the bundle.
     */
    public static final java.lang.String SYNC_VIRTUAL_EXTRAS_EXEMPTION_FLAG = "v_exemption";

    /**
     * Set by the SyncManager to request that the SyncAdapter initialize itself for
     * the given account/authority pair. One required initialization step is to
     * ensure that {@link #setIsSyncable(android.accounts.Account, String, int)} has been
     * called with a >= 0 value. When this flag is set the SyncAdapter does not need to
     * do a full sync, though it is allowed to do so.
     */
    public static final java.lang.String SYNC_EXTRAS_INITIALIZE = "initialize";

    /**
     *
     *
     * @unknown 
     */
    public static final android.content.Intent ACTION_SYNC_CONN_STATUS_CHANGED = new android.content.Intent("com.android.sync.SYNC_CONN_STATUS_CHANGED");

    public static final java.lang.String SCHEME_CONTENT = "content";

    public static final java.lang.String SCHEME_ANDROID_RESOURCE = "android.resource";

    public static final java.lang.String SCHEME_FILE = "file";

    /**
     * An extra {@link Point} describing the optimal size for a requested image
     * resource, in pixels. If a provider has multiple sizes of the image, it
     * should return the image closest to this size.
     *
     * @see #openTypedAssetFileDescriptor(Uri, String, Bundle)
     * @see #openTypedAssetFileDescriptor(Uri, String, Bundle,
    CancellationSignal)
     */
    public static final java.lang.String EXTRA_SIZE = "android.content.extra.SIZE";

    /**
     * An extra boolean describing whether a particular provider supports refresh
     * or not. If a provider supports refresh, it should include this key in its
     * returned Cursor as part of its query call.
     */
    public static final java.lang.String EXTRA_REFRESH_SUPPORTED = "android.content.extra.REFRESH_SUPPORTED";

    /**
     * Key for an SQL style selection string that may be present in the query Bundle argument
     * passed to {@link ContentProvider#query(Uri, String[], Bundle, CancellationSignal)}
     * when called by a legacy client.
     *
     * <p>Clients should never include user supplied values directly in the selection string,
     * as this presents an avenue for SQL injection attacks. In lieu of this, a client
     * should use standard placeholder notation to represent values in a selection string,
     * then supply a corresponding value in {@value #QUERY_ARG_SQL_SELECTION_ARGS}.
     *
     * <p><b>Apps targeting {@link android.os.Build.VERSION_CODES#O} or higher are strongly
     * encourage to use structured query arguments in lieu of opaque SQL query clauses.</b>
     *
     * @see #QUERY_ARG_SORT_COLUMNS
     * @see #QUERY_ARG_SORT_DIRECTION
     * @see #QUERY_ARG_SORT_COLLATION
     */
    public static final java.lang.String QUERY_ARG_SQL_SELECTION = "android:query-arg-sql-selection";

    /**
     * Key for SQL selection string arguments list.
     *
     * <p>Clients should never include user supplied values directly in the selection string,
     * as this presents an avenue for SQL injection attacks. In lieu of this, a client
     * should use standard placeholder notation to represent values in a selection string,
     * then supply a corresponding value in {@value #QUERY_ARG_SQL_SELECTION_ARGS}.
     *
     * <p><b>Apps targeting {@link android.os.Build.VERSION_CODES#O} or higher are strongly
     * encourage to use structured query arguments in lieu of opaque SQL query clauses.</b>
     *
     * @see #QUERY_ARG_SORT_COLUMNS
     * @see #QUERY_ARG_SORT_DIRECTION
     * @see #QUERY_ARG_SORT_COLLATION
     */
    public static final java.lang.String QUERY_ARG_SQL_SELECTION_ARGS = "android:query-arg-sql-selection-args";

    /**
     * Key for an SQL style sort string that may be present in the query Bundle argument
     * passed to {@link ContentProvider#query(Uri, String[], Bundle, CancellationSignal)}
     * when called by a legacy client.
     *
     * <p><b>Apps targeting {@link android.os.Build.VERSION_CODES#O} or higher are strongly
     * encourage to use structured query arguments in lieu of opaque SQL query clauses.</b>
     *
     * @see #QUERY_ARG_SORT_COLUMNS
     * @see #QUERY_ARG_SORT_DIRECTION
     * @see #QUERY_ARG_SORT_COLLATION
     */
    public static final java.lang.String QUERY_ARG_SQL_SORT_ORDER = "android:query-arg-sql-sort-order";

    /**
     * {@hide }
     */
    public static final java.lang.String QUERY_ARG_SQL_GROUP_BY = "android:query-arg-sql-group-by";

    /**
     * {@hide }
     */
    public static final java.lang.String QUERY_ARG_SQL_HAVING = "android:query-arg-sql-having";

    /**
     * {@hide }
     */
    public static final java.lang.String QUERY_ARG_SQL_LIMIT = "android:query-arg-sql-limit";

    /**
     * Specifies the list of columns against which to sort results. When first column values
     * are identical, records are then sorted based on second column values, and so on.
     *
     * <p>Columns present in this list must also be included in the projection
     * supplied to {@link ContentResolver#query(Uri, String[], Bundle, CancellationSignal)}.
     *
     * <p>Apps targeting {@link android.os.Build.VERSION_CODES#O} or higher:
     *
     * <li>{@link ContentProvider} implementations: When preparing data in
     * {@link ContentProvider#query(Uri, String[], Bundle, CancellationSignal)}, if sort columns
     * is reflected in the returned Cursor, it is  strongly recommended that
     * {@link #QUERY_ARG_SORT_COLUMNS} then be included in the array of honored arguments
     * reflected in {@link Cursor} extras {@link Bundle} under {@link #EXTRA_HONORED_ARGS}.
     *
     * <li>When querying a provider, where no QUERY_ARG_SQL* otherwise exists in the
     * arguments {@link Bundle}, the Content framework will attempt to synthesize
     * an QUERY_ARG_SQL* argument using the corresponding QUERY_ARG_SORT* values.
     */
    public static final java.lang.String QUERY_ARG_SORT_COLUMNS = "android:query-arg-sort-columns";

    /**
     * Specifies desired sort order. When unspecified a provider may provide a default
     * sort direction, or choose to return unsorted results.
     *
     * <p>Apps targeting {@link android.os.Build.VERSION_CODES#O} or higher:
     *
     * <li>{@link ContentProvider} implementations: When preparing data in
     * {@link ContentProvider#query(Uri, String[], Bundle, CancellationSignal)}, if sort direction
     * is reflected in the returned Cursor, it is  strongly recommended that
     * {@link #QUERY_ARG_SORT_DIRECTION} then be included in the array of honored arguments
     * reflected in {@link Cursor} extras {@link Bundle} under {@link #EXTRA_HONORED_ARGS}.
     *
     * <li>When querying a provider, where no QUERY_ARG_SQL* otherwise exists in the
     * arguments {@link Bundle}, the Content framework will attempt to synthesize
     * a QUERY_ARG_SQL* argument using the corresponding QUERY_ARG_SORT* values.
     *
     * @see #QUERY_SORT_DIRECTION_ASCENDING
     * @see #QUERY_SORT_DIRECTION_DESCENDING
     */
    public static final java.lang.String QUERY_ARG_SORT_DIRECTION = "android:query-arg-sort-direction";

    /**
     * Allows client to specify a hint to the provider declaring which collation
     * to use when sorting text values.
     *
     * <p>Providers may support custom collators. When specifying a custom collator
     * the value is determined by the Provider.
     *
     * <li>{@link ContentProvider} implementations: When preparing data in
     * {@link ContentProvider#query(Uri, String[], Bundle, CancellationSignal)}, if sort collation
     * is reflected in the returned Cursor, it is  strongly recommended that
     * {@link #QUERY_ARG_SORT_COLLATION} then be included in the array of honored arguments
     * reflected in {@link Cursor} extras {@link Bundle} under {@link #EXTRA_HONORED_ARGS}.
     *
     * <li>When querying a provider, where no QUERY_ARG_SQL* otherwise exists in the
     * arguments {@link Bundle}, the Content framework will attempt to synthesize
     * a QUERY_ARG_SQL* argument using the corresponding QUERY_ARG_SORT* values.
     *
     * @see java.text.Collator#PRIMARY
     * @see java.text.Collator#SECONDARY
     * @see java.text.Collator#TERTIARY
     * @see java.text.Collator#IDENTICAL
     */
    public static final java.lang.String QUERY_ARG_SORT_COLLATION = "android:query-arg-sort-collation";

    /**
     * Allows provider to report back to client which query keys are honored in a Cursor.
     *
     * <p>Key identifying a {@code String[]} containing all QUERY_ARG_SORT* arguments
     * honored by the provider. Include this in {@link Cursor} extras {@link Bundle}
     * when any QUERY_ARG_SORT* value was honored during the preparation of the
     * results {@link Cursor}.
     *
     * <p>If present, ALL honored arguments are enumerated in this extra’s payload.
     *
     * @see #QUERY_ARG_SORT_COLUMNS
     * @see #QUERY_ARG_SORT_DIRECTION
     * @see #QUERY_ARG_SORT_COLLATION
     */
    public static final java.lang.String EXTRA_HONORED_ARGS = "android.content.extra.HONORED_ARGS";

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = false, prefix = { "QUERY_SORT_DIRECTION_" }, value = { android.content.ContentResolver.QUERY_SORT_DIRECTION_ASCENDING, android.content.ContentResolver.QUERY_SORT_DIRECTION_DESCENDING })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface SortDirection {}

    public static final int QUERY_SORT_DIRECTION_ASCENDING = 0;

    public static final int QUERY_SORT_DIRECTION_DESCENDING = 1;

    /**
     *
     *
     * @see {@link java.text.Collector} for details on respective collation strength.
     * @unknown 
     */
    @android.annotation.IntDef(flag = false, value = { java.text.Collator.PRIMARY, java.text.Collator.SECONDARY, java.text.Collator.TERTIARY, java.text.Collator.IDENTICAL })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface QueryCollator {}

    /**
     * Specifies the offset row index within a Cursor.
     */
    public static final java.lang.String QUERY_ARG_OFFSET = "android:query-arg-offset";

    /**
     * Specifies the max number of rows to include in a Cursor.
     */
    public static final java.lang.String QUERY_ARG_LIMIT = "android:query-arg-limit";

    /**
     * Added to {@link Cursor} extras {@link Bundle} to indicate total row count of
     * recordset when paging is supported. Providers must include this when
     * implementing paging support.
     *
     * <p>A provider may return -1 that row count of the recordset is unknown.
     *
     * <p>Providers having returned -1 in a previous query are recommended to
     * send content change notification once (if) full recordset size becomes
     * known.
     */
    public static final java.lang.String EXTRA_TOTAL_COUNT = "android.content.extra.TOTAL_COUNT";

    /**
     * This is the Android platform's base MIME type for a content: URI
     * containing a Cursor of a single item.  Applications should use this
     * as the base type along with their own sub-type of their content: URIs
     * that represent a particular item.  For example, hypothetical IMAP email
     * client may have a URI
     * <code>content://com.company.provider.imap/inbox/1</code> for a particular
     * message in the inbox, whose MIME type would be reported as
     * <code>CURSOR_ITEM_BASE_TYPE + "/vnd.company.imap-msg"</code>
     *
     * <p>Compare with {@link #CURSOR_DIR_BASE_TYPE}.
     */
    public static final java.lang.String CURSOR_ITEM_BASE_TYPE = "vnd.android.cursor.item";

    /**
     * This is the Android platform's base MIME type for a content: URI
     * containing a Cursor of zero or more items.  Applications should use this
     * as the base type along with their own sub-type of their content: URIs
     * that represent a directory of items.  For example, hypothetical IMAP email
     * client may have a URI
     * <code>content://com.company.provider.imap/inbox</code> for all of the
     * messages in its inbox, whose MIME type would be reported as
     * <code>CURSOR_DIR_BASE_TYPE + "/vnd.company.imap-msg"</code>
     *
     * <p>Note how the base MIME type varies between this and
     * {@link #CURSOR_ITEM_BASE_TYPE} depending on whether there is
     * one single item or multiple items in the data set, while the sub-type
     * remains the same because in either case the data structure contained
     * in the cursor is the same.
     */
    public static final java.lang.String CURSOR_DIR_BASE_TYPE = "vnd.android.cursor.dir";

    /**
     * This is the Android platform's generic MIME type to match any MIME
     * type of the form "{@link #CURSOR_ITEM_BASE_TYPE}/{@code SUB_TYPE}".
     * {@code SUB_TYPE} is the sub-type of the application-dependent
     * content, e.g., "audio", "video", "playlist".
     */
    public static final java.lang.String ANY_CURSOR_ITEM_TYPE = "vnd.android.cursor.item/*";

    /**
     * Default MIME type for files whose type is otherwise unknown.
     *
     * @unknown 
     */
    public static final java.lang.String MIME_TYPE_DEFAULT = "application/octet-stream";

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static final int SYNC_ERROR_SYNC_ALREADY_IN_PROGRESS = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int SYNC_ERROR_AUTHENTICATION = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int SYNC_ERROR_IO = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int SYNC_ERROR_PARSE = 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int SYNC_ERROR_CONFLICT = 5;

    /**
     *
     *
     * @unknown 
     */
    public static final int SYNC_ERROR_TOO_MANY_DELETIONS = 6;

    /**
     *
     *
     * @unknown 
     */
    public static final int SYNC_ERROR_TOO_MANY_RETRIES = 7;

    /**
     *
     *
     * @unknown 
     */
    public static final int SYNC_ERROR_INTERNAL = 8;

    private static final java.lang.String[] SYNC_ERROR_NAMES = new java.lang.String[]{ "already-in-progress", "authentication-error", "io-error", "parse-error", "conflict", "too-many-deletions", "too-many-retries", "internal-error" };

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String syncErrorToString(int error) {
        if ((error < 1) || (error > android.content.ContentResolver.SYNC_ERROR_NAMES.length)) {
            return java.lang.String.valueOf(error);
        }
        return android.content.ContentResolver.SYNC_ERROR_NAMES[error - 1];
    }

    /**
     *
     *
     * @unknown 
     */
    public static int syncErrorStringToInt(java.lang.String error) {
        for (int i = 0, n = android.content.ContentResolver.SYNC_ERROR_NAMES.length; i < n; i++) {
            if (android.content.ContentResolver.SYNC_ERROR_NAMES[i].equals(error)) {
                return i + 1;
            }
        }
        if (error != null) {
            try {
                return java.lang.Integer.parseInt(error);
            } catch (java.lang.NumberFormatException e) {
                android.util.Log.d(android.content.ContentResolver.TAG, "error parsing sync error: " + error);
            }
        }
        return 0;
    }

    public static final int SYNC_OBSERVER_TYPE_SETTINGS = 1 << 0;

    public static final int SYNC_OBSERVER_TYPE_PENDING = 1 << 1;

    public static final int SYNC_OBSERVER_TYPE_ACTIVE = 1 << 2;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static final int SYNC_OBSERVER_TYPE_STATUS = 1 << 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int SYNC_OBSERVER_TYPE_ALL = 0x7fffffff;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = true, prefix = { "NOTIFY_" }, value = { android.content.ContentResolver.NOTIFY_SYNC_TO_NETWORK, android.content.ContentResolver.NOTIFY_SKIP_NOTIFY_FOR_DESCENDANTS })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface NotifyFlags {}

    /**
     * Flag for {@link #notifyChange(Uri, ContentObserver, int)}: attempt to sync the change
     * to the network.
     */
    public static final int NOTIFY_SYNC_TO_NETWORK = 1 << 0;

    /**
     * Flag for {@link #notifyChange(Uri, ContentObserver, int)}: if set, this notification
     * will be skipped if it is being delivered to the root URI of a ContentObserver that is
     * using "notify for descendants."  The purpose of this is to allow the provide to send
     * a general notification of "something under X" changed that observers of that specific
     * URI can receive, while also sending a specific URI under X.  It would use this flag
     * when sending the former, so that observers of "X and descendants" only see the latter.
     */
    public static final int NOTIFY_SKIP_NOTIFY_FOR_DESCENDANTS = 1 << 1;

    /**
     * No exception, throttled by app standby normally.
     *
     * @unknown 
     */
    public static final int SYNC_EXEMPTION_NONE = 0;

    /**
     * Exemption given to a sync request made by a foreground app (including
     * PROCESS_STATE_IMPORTANT_FOREGROUND).
     *
     * At the schedule time, we promote the sync adapter app for a higher bucket:
     * - If the device is not dozing (so the sync will start right away)
     *   promote to ACTIVE for 1 hour.
     * - If the device is dozing (so the sync *won't* start right away),
     * promote to WORKING_SET for 4 hours, so it'll get a higher chance to be started once the
     * device comes out of doze.
     * - When the sync actually starts, we promote the sync adapter app to ACTIVE for 10 minutes,
     * so it can schedule and start more syncs without getting throttled, even when the first
     * operation was canceled and now we're retrying.
     *
     * @unknown 
     */
    public static final int SYNC_EXEMPTION_PROMOTE_BUCKET = 1;

    /**
     * In addition to {@link #SYNC_EXEMPTION_PROMOTE_BUCKET}, we put the sync adapter app in the
     * temp whitelist for 10 minutes, so that even RARE apps can run syncs right away.
     *
     * @unknown 
     */
    public static final int SYNC_EXEMPTION_PROMOTE_BUCKET_WITH_TEMP = 2;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = false, prefix = { "SYNC_EXEMPTION_" }, value = { android.content.ContentResolver.SYNC_EXEMPTION_NONE, android.content.ContentResolver.SYNC_EXEMPTION_PROMOTE_BUCKET, android.content.ContentResolver.SYNC_EXEMPTION_PROMOTE_BUCKET_WITH_TEMP })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface SyncExemption {}

    // Always log queries which take 500ms+; shorter queries are
    // sampled accordingly.
    private static final boolean ENABLE_CONTENT_SAMPLE = false;

    private static final int SLOW_THRESHOLD_MILLIS = 500;

    private final java.util.Random mRandom = new java.util.Random();// guarded by itself


    public ContentResolver(@android.annotation.Nullable
    android.content.Context context) {
        this(context, null);
    }

    /**
     * {@hide }
     */
    public ContentResolver(@android.annotation.Nullable
    android.content.Context context, @android.annotation.Nullable
    android.content.ContentInterface wrapped) {
        mContext = (context != null) ? context : android.app.ActivityThread.currentApplication();
        mPackageName = mContext.getOpPackageName();
        mTargetSdkVersion = mContext.getApplicationInfo().targetSdkVersion;
        mWrapped = wrapped;
    }

    /**
     * {@hide }
     */
    @android.annotation.NonNull
    public static android.content.ContentResolver wrap(@android.annotation.NonNull
    android.content.ContentInterface wrapped) {
        com.android.internal.util.Preconditions.checkNotNull(wrapped);
        return new android.content.ContentResolver(null, wrapped) {
            @java.lang.Override
            public void unstableProviderDied(android.content.IContentProvider icp) {
                throw new java.lang.UnsupportedOperationException();
            }

            @java.lang.Override
            public boolean releaseUnstableProvider(android.content.IContentProvider icp) {
                throw new java.lang.UnsupportedOperationException();
            }

            @java.lang.Override
            public boolean releaseProvider(android.content.IContentProvider icp) {
                throw new java.lang.UnsupportedOperationException();
            }

            @java.lang.Override
            protected android.content.IContentProvider acquireUnstableProvider(android.content.Context c, java.lang.String name) {
                throw new java.lang.UnsupportedOperationException();
            }

            @java.lang.Override
            protected android.content.IContentProvider acquireProvider(android.content.Context c, java.lang.String name) {
                throw new java.lang.UnsupportedOperationException();
            }
        };
    }

    /**
     * Create a {@link ContentResolver} instance that redirects all its methods
     * to the given {@link ContentProvider}.
     */
    @android.annotation.NonNull
    public static android.content.ContentResolver wrap(@android.annotation.NonNull
    ContentProvider wrapped) {
        return android.content.ContentResolver.wrap(((android.content.ContentInterface) (wrapped)));
    }

    /**
     * Create a {@link ContentResolver} instance that redirects all its methods
     * to the given {@link ContentProviderClient}.
     */
    @android.annotation.NonNull
    public static android.content.ContentResolver wrap(@android.annotation.NonNull
    android.content.ContentProviderClient wrapped) {
        return android.content.ContentResolver.wrap(((android.content.ContentInterface) (wrapped)));
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    protected abstract android.content.IContentProvider acquireProvider(android.content.Context c, java.lang.String name);

    /**
     * Providing a default implementation of this, to avoid having to change a
     * lot of other things, but implementations of ContentResolver should
     * implement it.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    protected android.content.IContentProvider acquireExistingProvider(android.content.Context c, java.lang.String name) {
        return acquireProvider(c, name);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public abstract boolean releaseProvider(android.content.IContentProvider icp);

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    protected abstract android.content.IContentProvider acquireUnstableProvider(android.content.Context c, java.lang.String name);

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public abstract boolean releaseUnstableProvider(android.content.IContentProvider icp);

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public abstract void unstableProviderDied(android.content.IContentProvider icp);

    /**
     *
     *
     * @unknown 
     */
    public void appNotRespondingViaProvider(android.content.IContentProvider icp) {
        throw new java.lang.UnsupportedOperationException("appNotRespondingViaProvider");
    }

    /**
     * Return the MIME type of the given content URL.
     *
     * @param url
     * 		A Uri identifying content (either a list or specific type),
     * 		using the content:// scheme.
     * @return A MIME type for the content, or null if the URL is invalid or the type is unknown
     */
    @java.lang.Override
    @android.annotation.Nullable
    public final java.lang.String getType(@android.annotation.NonNull
    android.net.Uri url) {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        try {
            if (mWrapped != null)
                return mWrapped.getType(url);

        } catch (android.os.RemoteException e) {
            return null;
        }
        // XXX would like to have an acquireExistingUnstableProvider for this.
        android.content.IContentProvider provider = acquireExistingProvider(url);
        if (provider != null) {
            try {
                return provider.getType(url);
            } catch (android.os.RemoteException e) {
                // Arbitrary and not worth documenting, as Activity
                // Manager will kill this process shortly anyway.
                return null;
            } catch (java.lang.Exception e) {
                android.util.Log.w(android.content.ContentResolver.TAG, ((("Failed to get type for: " + url) + " (") + e.getMessage()) + ")");
                return null;
            } finally {
                releaseProvider(provider);
            }
        }
        if (!android.content.ContentResolver.SCHEME_CONTENT.equals(url.getScheme())) {
            return null;
        }
        try {
            java.lang.String type = android.app.ActivityManager.getService().getProviderMimeType(getUriWithoutUserId(url), resolveUserId(url));
            return type;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        } catch (java.lang.Exception e) {
            android.util.Log.w(android.content.ContentResolver.TAG, ((("Failed to get type for: " + url) + " (") + e.getMessage()) + ")");
            return null;
        }
    }

    /**
     * Query for the possible MIME types for the representations the given
     * content URL can be returned when opened as as stream with
     * {@link #openTypedAssetFileDescriptor}.  Note that the types here are
     * not necessarily a superset of the type returned by {@link #getType} --
     * many content providers cannot return a raw stream for the structured
     * data that they contain.
     *
     * @param url
     * 		A Uri identifying content (either a list or specific type),
     * 		using the content:// scheme.
     * @param mimeTypeFilter
     * 		The desired MIME type.  This may be a pattern,
     * 		such as *&#47;*, to query for all available MIME types that match the
     * 		pattern.
     * @return Returns an array of MIME type strings for all available
    data streams that match the given mimeTypeFilter.  If there are none,
    null is returned.
     */
    @java.lang.Override
    @android.annotation.Nullable
    public java.lang.String[] getStreamTypes(@android.annotation.NonNull
    android.net.Uri url, @android.annotation.NonNull
    java.lang.String mimeTypeFilter) {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        com.android.internal.util.Preconditions.checkNotNull(mimeTypeFilter, "mimeTypeFilter");
        try {
            if (mWrapped != null)
                return mWrapped.getStreamTypes(url, mimeTypeFilter);

        } catch (android.os.RemoteException e) {
            return null;
        }
        android.content.IContentProvider provider = acquireProvider(url);
        if (provider == null) {
            return null;
        }
        try {
            return provider.getStreamTypes(url, mimeTypeFilter);
        } catch (android.os.RemoteException e) {
            // Arbitrary and not worth documenting, as Activity
            // Manager will kill this process shortly anyway.
            return null;
        } finally {
            releaseProvider(provider);
        }
    }

    /**
     * Query the given URI, returning a {@link Cursor} over the result set.
     * <p>
     * For best performance, the caller should follow these guidelines:
     * <ul>
     * <li>Provide an explicit projection, to prevent
     * reading data from storage that aren't going to be used.</li>
     * <li>Use question mark parameter markers such as 'phone=?' instead of
     * explicit values in the {@code selection} parameter, so that queries
     * that differ only by those values will be recognized as the same
     * for caching purposes.</li>
     * </ul>
     * </p>
     *
     * @param uri
     * 		The URI, using the content:// scheme, for the content to
     * 		retrieve.
     * @param projection
     * 		A list of which columns to return. Passing null will
     * 		return all columns, which is inefficient.
     * @param selection
     * 		A filter declaring which rows to return, formatted as an
     * 		SQL WHERE clause (excluding the WHERE itself). Passing null will
     * 		return all rows for the given URI.
     * @param selectionArgs
     * 		You may include ?s in selection, which will be
     * 		replaced by the values from selectionArgs, in the order that they
     * 		appear in the selection. The values will be bound as Strings.
     * @param sortOrder
     * 		How to order the rows, formatted as an SQL ORDER BY
     * 		clause (excluding the ORDER BY itself). Passing null will use the
     * 		default sort order, which may be unordered.
     * @return A Cursor object, which is positioned before the first entry. May return
    <code>null</code> if the underlying content provider returns <code>null</code>,
    or if it crashes.
     * @see Cursor
     */
    @android.annotation.Nullable
    public final android.database.Cursor query(@android.annotation.RequiresPermission.Read
    @android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    java.lang.String[] projection, @android.annotation.Nullable
    java.lang.String selection, @android.annotation.Nullable
    java.lang.String[] selectionArgs, @android.annotation.Nullable
    java.lang.String sortOrder) {
        return query(uri, projection, selection, selectionArgs, sortOrder, null);
    }

    /**
     * Query the given URI, returning a {@link Cursor} over the result set
     * with optional support for cancellation.
     * <p>
     * For best performance, the caller should follow these guidelines:
     * <ul>
     * <li>Provide an explicit projection, to prevent
     * reading data from storage that aren't going to be used.</li>
     * <li>Use question mark parameter markers such as 'phone=?' instead of
     * explicit values in the {@code selection} parameter, so that queries
     * that differ only by those values will be recognized as the same
     * for caching purposes.</li>
     * </ul>
     * </p>
     *
     * @param uri
     * 		The URI, using the content:// scheme, for the content to
     * 		retrieve.
     * @param projection
     * 		A list of which columns to return. Passing null will
     * 		return all columns, which is inefficient.
     * @param selection
     * 		A filter declaring which rows to return, formatted as an
     * 		SQL WHERE clause (excluding the WHERE itself). Passing null will
     * 		return all rows for the given URI.
     * @param selectionArgs
     * 		You may include ?s in selection, which will be
     * 		replaced by the values from selectionArgs, in the order that they
     * 		appear in the selection. The values will be bound as Strings.
     * @param sortOrder
     * 		How to order the rows, formatted as an SQL ORDER BY
     * 		clause (excluding the ORDER BY itself). Passing null will use the
     * 		default sort order, which may be unordered.
     * @param cancellationSignal
     * 		A signal to cancel the operation in progress, or null if none.
     * 		If the operation is canceled, then {@link OperationCanceledException} will be thrown
     * 		when the query is executed.
     * @return A Cursor object, which is positioned before the first entry. May return
    <code>null</code> if the underlying content provider returns <code>null</code>,
    or if it crashes.
     * @see Cursor
     */
    @android.annotation.Nullable
    public final android.database.Cursor query(@android.annotation.RequiresPermission.Read
    @android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    java.lang.String[] projection, @android.annotation.Nullable
    java.lang.String selection, @android.annotation.Nullable
    java.lang.String[] selectionArgs, @android.annotation.Nullable
    java.lang.String sortOrder, @android.annotation.Nullable
    android.os.CancellationSignal cancellationSignal) {
        android.os.Bundle queryArgs = android.content.ContentResolver.createSqlQueryBundle(selection, selectionArgs, sortOrder);
        return query(uri, projection, queryArgs, cancellationSignal);
    }

    /**
     * Query the given URI, returning a {@link Cursor} over the result set
     * with support for cancellation.
     *
     * <p>For best performance, the caller should follow these guidelines:
     *
     * <li>Provide an explicit projection, to prevent reading data from storage
     * that aren't going to be used.
     *
     * Provider must identify which QUERY_ARG_SORT* arguments were honored during
     * the preparation of the result set by including the respective argument keys
     * in the {@link Cursor} extras {@link Bundle}. See {@link #EXTRA_HONORED_ARGS}
     * for details.
     *
     * @see #QUERY_ARG_SORT_COLUMNS
     * @see #QUERY_ARG_SORT_DIRECTION
     * @see #QUERY_ARG_SORT_COLLATION
     * @param uri
     * 		The URI, using the content:// scheme, for the content to
     * 		retrieve.
     * @param projection
     * 		A list of which columns to return. Passing null will
     * 		return all columns, which is inefficient.
     * @param queryArgs
     * 		A Bundle containing any arguments to the query.
     * @param cancellationSignal
     * 		A signal to cancel the operation in progress, or null if none.
     * 		If the operation is canceled, then {@link OperationCanceledException} will be thrown
     * 		when the query is executed.
     * @return A Cursor object, which is positioned before the first entry. May return
    <code>null</code> if the underlying content provider returns <code>null</code>,
    or if it crashes.
     * @see Cursor
     */
    @java.lang.Override
    @android.annotation.Nullable
    public final android.database.Cursor query(@android.annotation.RequiresPermission.Read
    @android.annotation.NonNull
    final android.net.Uri uri, @android.annotation.Nullable
    java.lang.String[] projection, @android.annotation.Nullable
    android.os.Bundle queryArgs, @android.annotation.Nullable
    android.os.CancellationSignal cancellationSignal) {
        com.android.internal.util.Preconditions.checkNotNull(uri, "uri");
        try {
            if (mWrapped != null) {
                return mWrapped.query(uri, projection, queryArgs, cancellationSignal);
            }
        } catch (android.os.RemoteException e) {
            return null;
        }
        android.content.IContentProvider unstableProvider = acquireUnstableProvider(uri);
        if (unstableProvider == null) {
            return null;
        }
        android.content.IContentProvider stableProvider = null;
        android.database.Cursor qCursor = null;
        try {
            long startTime = android.os.SystemClock.uptimeMillis();
            android.os.ICancellationSignal remoteCancellationSignal = null;
            if (cancellationSignal != null) {
                cancellationSignal.throwIfCanceled();
                remoteCancellationSignal = unstableProvider.createCancellationSignal();
                cancellationSignal.setRemote(remoteCancellationSignal);
            }
            try {
                qCursor = unstableProvider.query(mPackageName, uri, projection, queryArgs, remoteCancellationSignal);
            } catch (android.os.DeadObjectException e) {
                // The remote process has died...  but we only hold an unstable
                // reference though, so we might recover!!!  Let's try!!!!
                // This is exciting!!1!!1!!!!1
                unstableProviderDied(unstableProvider);
                stableProvider = acquireProvider(uri);
                if (stableProvider == null) {
                    return null;
                }
                qCursor = stableProvider.query(mPackageName, uri, projection, queryArgs, remoteCancellationSignal);
            }
            if (qCursor == null) {
                return null;
            }
            // Force query execution.  Might fail and throw a runtime exception here.
            qCursor.getCount();
            long durationMillis = android.os.SystemClock.uptimeMillis() - startTime;
            maybeLogQueryToEventLog(durationMillis, uri, projection, queryArgs);
            // Wrap the cursor object into CursorWrapperInner object.
            final android.content.IContentProvider provider = (stableProvider != null) ? stableProvider : acquireProvider(uri);
            final android.content.ContentResolver.CursorWrapperInner wrapper = new android.content.ContentResolver.CursorWrapperInner(qCursor, provider);
            stableProvider = null;
            qCursor = null;
            return wrapper;
        } catch (android.os.RemoteException e) {
            // Arbitrary and not worth documenting, as Activity
            // Manager will kill this process shortly anyway.
            return null;
        } finally {
            if (qCursor != null) {
                qCursor.close();
            }
            if (cancellationSignal != null) {
                cancellationSignal.setRemote(null);
            }
            if (unstableProvider != null) {
                releaseUnstableProvider(unstableProvider);
            }
            if (stableProvider != null) {
                releaseProvider(stableProvider);
            }
        }
    }

    /**
     * {@hide }
     */
    @android.annotation.NonNull
    public final android.net.Uri canonicalizeOrElse(@android.annotation.NonNull
    android.net.Uri uri) {
        final android.net.Uri res = canonicalize(uri);
        return res != null ? res : uri;
    }

    /**
     * Transform the given <var>url</var> to a canonical representation of
     * its referenced resource, which can be used across devices, persisted,
     * backed up and restored, etc.  The returned Uri is still a fully capable
     * Uri for use with its content provider, allowing you to do all of the
     * same content provider operations as with the original Uri --
     * {@link #query}, {@link #openInputStream(android.net.Uri)}, etc.  The
     * only difference in behavior between the original and new Uris is that
     * the content provider may need to do some additional work at each call
     * using it to resolve it to the correct resource, especially if the
     * canonical Uri has been moved to a different environment.
     *
     * <p>If you are moving a canonical Uri between environments, you should
     * perform another call to {@link #canonicalize} with that original Uri to
     * re-canonicalize it for the current environment.  Alternatively, you may
     * want to use {@link #uncanonicalize} to transform it to a non-canonical
     * Uri that works only in the current environment but potentially more
     * efficiently than the canonical representation.</p>
     *
     * @param url
     * 		The {@link Uri} that is to be transformed to a canonical
     * 		representation.  Like all resolver calls, the input can be either
     * 		a non-canonical or canonical Uri.
     * @return Returns the official canonical representation of <var>url</var>,
    or null if the content provider does not support a canonical representation
    of the given Uri.  Many providers may not support canonicalization of some
    or all of their Uris.
     * @see #uncanonicalize
     */
    @java.lang.Override
    @android.annotation.Nullable
    public final android.net.Uri canonicalize(@android.annotation.NonNull
    android.net.Uri url) {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        try {
            if (mWrapped != null)
                return mWrapped.canonicalize(url);

        } catch (android.os.RemoteException e) {
            return null;
        }
        android.content.IContentProvider provider = acquireProvider(url);
        if (provider == null) {
            return null;
        }
        try {
            return provider.canonicalize(mPackageName, url);
        } catch (android.os.RemoteException e) {
            // Arbitrary and not worth documenting, as Activity
            // Manager will kill this process shortly anyway.
            return null;
        } finally {
            releaseProvider(provider);
        }
    }

    /**
     * Given a canonical Uri previously generated by {@link #canonicalize}, convert
     * it to its local non-canonical form.  This can be useful in some cases where
     * you know that you will only be using the Uri in the current environment and
     * want to avoid any possible overhead when using it with the content
     * provider or want to verify that the referenced data exists at all in the
     * new environment.
     *
     * @param url
     * 		The canonical {@link Uri} that is to be convered back to its
     * 		non-canonical form.
     * @return Returns the non-canonical representation of <var>url</var>.  This will
    return null if data identified by the canonical Uri can not be found in
    the current environment; callers must always check for null and deal with
    that by appropriately falling back to an alternative.
     * @see #canonicalize
     */
    @java.lang.Override
    @android.annotation.Nullable
    public final android.net.Uri uncanonicalize(@android.annotation.NonNull
    android.net.Uri url) {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        try {
            if (mWrapped != null)
                return mWrapped.uncanonicalize(url);

        } catch (android.os.RemoteException e) {
            return null;
        }
        android.content.IContentProvider provider = acquireProvider(url);
        if (provider == null) {
            return null;
        }
        try {
            return provider.uncanonicalize(mPackageName, url);
        } catch (android.os.RemoteException e) {
            // Arbitrary and not worth documenting, as Activity
            // Manager will kill this process shortly anyway.
            return null;
        } finally {
            releaseProvider(provider);
        }
    }

    /**
     * This allows clients to request an explicit refresh of content identified by {@code uri}.
     * <p>
     * Client code should only invoke this method when there is a strong indication (such as a user
     * initiated pull to refresh gesture) that the content is stale.
     * <p>
     *
     * @param url
     * 		The Uri identifying the data to refresh.
     * @param args
     * 		Additional options from the client. The definitions of these are specific to the
     * 		content provider being called.
     * @param cancellationSignal
     * 		A signal to cancel the operation in progress, or {@code null} if
     * 		none. For example, if you called refresh on a particular uri, you should call
     * 		{@link CancellationSignal#throwIfCanceled()} to check whether the client has
     * 		canceled the refresh request.
     * @return true if the provider actually tried refreshing.
     */
    @java.lang.Override
    public final boolean refresh(@android.annotation.NonNull
    android.net.Uri url, @android.annotation.Nullable
    android.os.Bundle args, @android.annotation.Nullable
    android.os.CancellationSignal cancellationSignal) {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        try {
            if (mWrapped != null)
                return mWrapped.refresh(url, args, cancellationSignal);

        } catch (android.os.RemoteException e) {
            return false;
        }
        android.content.IContentProvider provider = acquireProvider(url);
        if (provider == null) {
            return false;
        }
        try {
            android.os.ICancellationSignal remoteCancellationSignal = null;
            if (cancellationSignal != null) {
                cancellationSignal.throwIfCanceled();
                remoteCancellationSignal = provider.createCancellationSignal();
                cancellationSignal.setRemote(remoteCancellationSignal);
            }
            return provider.refresh(mPackageName, url, args, remoteCancellationSignal);
        } catch (android.os.RemoteException e) {
            // Arbitrary and not worth documenting, as Activity
            // Manager will kill this process shortly anyway.
            return false;
        } finally {
            releaseProvider(provider);
        }
    }

    /**
     * Open a stream on to the content associated with a content URI.  If there
     * is no data associated with the URI, FileNotFoundException is thrown.
     *
     * <h5>Accepts the following URI schemes:</h5>
     * <ul>
     * <li>content ({@link #SCHEME_CONTENT})</li>
     * <li>android.resource ({@link #SCHEME_ANDROID_RESOURCE})</li>
     * <li>file ({@link #SCHEME_FILE})</li>
     * </ul>
     *
     * <p>See {@link #openAssetFileDescriptor(Uri, String)} for more information
     * on these schemes.
     *
     * @param uri
     * 		The desired URI.
     * @return InputStream
     * @throws FileNotFoundException
     * 		if the provided URI could not be opened.
     * @see #openAssetFileDescriptor(Uri, String)
     */
    @android.annotation.Nullable
    public final java.io.InputStream openInputStream(@android.annotation.NonNull
    android.net.Uri uri) throws java.io.FileNotFoundException {
        com.android.internal.util.Preconditions.checkNotNull(uri, "uri");
        java.lang.String scheme = uri.getScheme();
        if (android.content.ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
            // Note: left here to avoid breaking compatibility.  May be removed
            // with sufficient testing.
            android.content.ContentResolver.OpenResourceIdResult r = getResourceId(uri);
            try {
                java.io.InputStream stream = r.r.openRawResource(r.id);
                return stream;
            } catch (android.content.res.Resources.NotFoundException ex) {
                throw new java.io.FileNotFoundException("Resource does not exist: " + uri);
            }
        } else
            if (android.content.ContentResolver.SCHEME_FILE.equals(scheme)) {
                // Note: left here to avoid breaking compatibility.  May be removed
                // with sufficient testing.
                return new java.io.FileInputStream(uri.getPath());
            } else {
                android.content.res.AssetFileDescriptor fd = openAssetFileDescriptor(uri, "r", null);
                try {
                    return fd != null ? fd.createInputStream() : null;
                } catch (java.io.IOException e) {
                    throw new java.io.FileNotFoundException("Unable to create stream");
                }
            }

    }

    /**
     * Synonym for {@link #openOutputStream(Uri, String)
     * openOutputStream(uri, "w")}.
     *
     * @throws FileNotFoundException
     * 		if the provided URI could not be opened.
     */
    @android.annotation.Nullable
    public final java.io.OutputStream openOutputStream(@android.annotation.NonNull
    android.net.Uri uri) throws java.io.FileNotFoundException {
        return openOutputStream(uri, "w");
    }

    /**
     * Open a stream on to the content associated with a content URI.  If there
     * is no data associated with the URI, FileNotFoundException is thrown.
     *
     * <h5>Accepts the following URI schemes:</h5>
     * <ul>
     * <li>content ({@link #SCHEME_CONTENT})</li>
     * <li>file ({@link #SCHEME_FILE})</li>
     * </ul>
     *
     * <p>See {@link #openAssetFileDescriptor(Uri, String)} for more information
     * on these schemes.
     *
     * @param uri
     * 		The desired URI.
     * @param mode
     * 		May be "w", "wa", "rw", or "rwt".
     * @return OutputStream
     * @throws FileNotFoundException
     * 		if the provided URI could not be opened.
     * @see #openAssetFileDescriptor(Uri, String)
     */
    @android.annotation.Nullable
    public final java.io.OutputStream openOutputStream(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mode) throws java.io.FileNotFoundException {
        android.content.res.AssetFileDescriptor fd = openAssetFileDescriptor(uri, mode, null);
        try {
            return fd != null ? fd.createOutputStream() : null;
        } catch (java.io.IOException e) {
            throw new java.io.FileNotFoundException("Unable to create stream");
        }
    }

    @java.lang.Override
    @android.annotation.Nullable
    public final android.os.ParcelFileDescriptor openFile(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mode, @android.annotation.Nullable
    android.os.CancellationSignal signal) throws java.io.FileNotFoundException {
        try {
            if (mWrapped != null)
                return mWrapped.openFile(uri, mode, signal);

        } catch (android.os.RemoteException e) {
            return null;
        }
        return openFileDescriptor(uri, mode, signal);
    }

    /**
     * Open a raw file descriptor to access data under a URI.  This
     * is like {@link #openAssetFileDescriptor(Uri, String)}, but uses the
     * underlying {@link ContentProvider#openFile}
     * ContentProvider.openFile()} method, so will <em>not</em> work with
     * providers that return sub-sections of files.  If at all possible,
     * you should use {@link #openAssetFileDescriptor(Uri, String)}.  You
     * will receive a FileNotFoundException exception if the provider returns a
     * sub-section of a file.
     *
     * <h5>Accepts the following URI schemes:</h5>
     * <ul>
     * <li>content ({@link #SCHEME_CONTENT})</li>
     * <li>file ({@link #SCHEME_FILE})</li>
     * </ul>
     *
     * <p>See {@link #openAssetFileDescriptor(Uri, String)} for more information
     * on these schemes.
     * <p>
     * If opening with the exclusive "r" or "w" modes, the returned
     * ParcelFileDescriptor could be a pipe or socket pair to enable streaming
     * of data. Opening with the "rw" mode implies a file on disk that supports
     * seeking. If possible, always use an exclusive mode to give the underlying
     * {@link ContentProvider} the most flexibility.
     * <p>
     * If you are writing a file, and need to communicate an error to the
     * provider, use {@link ParcelFileDescriptor#closeWithError(String)}.
     *
     * @param uri
     * 		The desired URI to open.
     * @param mode
     * 		The file mode to use, as per {@link ContentProvider#openFile
     * 		ContentProvider.openFile}.
     * @return Returns a new ParcelFileDescriptor pointing to the file.  You
    own this descriptor and are responsible for closing it when done.
     * @throws FileNotFoundException
     * 		Throws FileNotFoundException if no
     * 		file exists under the URI or the mode is invalid.
     * @see #openAssetFileDescriptor(Uri, String)
     */
    @android.annotation.Nullable
    public final android.os.ParcelFileDescriptor openFileDescriptor(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mode) throws java.io.FileNotFoundException {
        return openFileDescriptor(uri, mode, null);
    }

    /**
     * Open a raw file descriptor to access data under a URI.  This
     * is like {@link #openAssetFileDescriptor(Uri, String)}, but uses the
     * underlying {@link ContentProvider#openFile}
     * ContentProvider.openFile()} method, so will <em>not</em> work with
     * providers that return sub-sections of files.  If at all possible,
     * you should use {@link #openAssetFileDescriptor(Uri, String)}.  You
     * will receive a FileNotFoundException exception if the provider returns a
     * sub-section of a file.
     *
     * <h5>Accepts the following URI schemes:</h5>
     * <ul>
     * <li>content ({@link #SCHEME_CONTENT})</li>
     * <li>file ({@link #SCHEME_FILE})</li>
     * </ul>
     *
     * <p>See {@link #openAssetFileDescriptor(Uri, String)} for more information
     * on these schemes.
     * <p>
     * If opening with the exclusive "r" or "w" modes, the returned
     * ParcelFileDescriptor could be a pipe or socket pair to enable streaming
     * of data. Opening with the "rw" mode implies a file on disk that supports
     * seeking. If possible, always use an exclusive mode to give the underlying
     * {@link ContentProvider} the most flexibility.
     * <p>
     * If you are writing a file, and need to communicate an error to the
     * provider, use {@link ParcelFileDescriptor#closeWithError(String)}.
     *
     * @param uri
     * 		The desired URI to open.
     * @param mode
     * 		The file mode to use, as per {@link ContentProvider#openFile
     * 		ContentProvider.openFile}.
     * @param cancellationSignal
     * 		A signal to cancel the operation in progress,
     * 		or null if none. If the operation is canceled, then
     * 		{@link OperationCanceledException} will be thrown.
     * @return Returns a new ParcelFileDescriptor pointing to the file.  You
    own this descriptor and are responsible for closing it when done.
     * @throws FileNotFoundException
     * 		Throws FileNotFoundException if no
     * 		file exists under the URI or the mode is invalid.
     * @see #openAssetFileDescriptor(Uri, String)
     */
    @android.annotation.Nullable
    public final android.os.ParcelFileDescriptor openFileDescriptor(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mode, @android.annotation.Nullable
    android.os.CancellationSignal cancellationSignal) throws java.io.FileNotFoundException {
        try {
            if (mWrapped != null)
                return mWrapped.openFile(uri, mode, cancellationSignal);

        } catch (android.os.RemoteException e) {
            return null;
        }
        android.content.res.AssetFileDescriptor afd = openAssetFileDescriptor(uri, mode, cancellationSignal);
        if (afd == null) {
            return null;
        }
        if (afd.getDeclaredLength() < 0) {
            // This is a full file!
            return afd.getParcelFileDescriptor();
        }
        // Client can't handle a sub-section of a file, so close what
        // we got and bail with an exception.
        try {
            afd.close();
        } catch (java.io.IOException e) {
        }
        throw new java.io.FileNotFoundException("Not a whole file");
    }

    @java.lang.Override
    @android.annotation.Nullable
    public final android.content.res.AssetFileDescriptor openAssetFile(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mode, @android.annotation.Nullable
    android.os.CancellationSignal signal) throws java.io.FileNotFoundException {
        try {
            if (mWrapped != null)
                return mWrapped.openAssetFile(uri, mode, signal);

        } catch (android.os.RemoteException e) {
            return null;
        }
        return openAssetFileDescriptor(uri, mode, signal);
    }

    /**
     * Open a raw file descriptor to access data under a URI.  This
     * interacts with the underlying {@link ContentProvider#openAssetFile}
     * method of the provider associated with the given URI, to retrieve any file stored there.
     *
     * <h5>Accepts the following URI schemes:</h5>
     * <ul>
     * <li>content ({@link #SCHEME_CONTENT})</li>
     * <li>android.resource ({@link #SCHEME_ANDROID_RESOURCE})</li>
     * <li>file ({@link #SCHEME_FILE})</li>
     * </ul>
     * <h5>The android.resource ({@link #SCHEME_ANDROID_RESOURCE}) Scheme</h5>
     * <p>
     * A Uri object can be used to reference a resource in an APK file.  The
     * Uri should be one of the following formats:
     * <ul>
     * <li><code>android.resource://package_name/id_number</code><br/>
     * <code>package_name</code> is your package name as listed in your AndroidManifest.xml.
     * For example <code>com.example.myapp</code><br/>
     * <code>id_number</code> is the int form of the ID.<br/>
     * The easiest way to construct this form is
     * <pre>Uri uri = Uri.parse("android.resource://com.example.myapp/" + R.raw.my_resource");</pre>
     * </li>
     * <li><code>android.resource://package_name/type/name</code><br/>
     * <code>package_name</code> is your package name as listed in your AndroidManifest.xml.
     * For example <code>com.example.myapp</code><br/>
     * <code>type</code> is the string form of the resource type.  For example, <code>raw</code>
     * or <code>drawable</code>.
     * <code>name</code> is the string form of the resource name.  That is, whatever the file
     * name was in your res directory, without the type extension.
     * The easiest way to construct this form is
     * <pre>Uri uri = Uri.parse("android.resource://com.example.myapp/raw/my_resource");</pre>
     * </li>
     * </ul>
     *
     * <p>Note that if this function is called for read-only input (mode is "r")
     * on a content: URI, it will instead call {@link #openTypedAssetFileDescriptor}
     * for you with a MIME type of "*&#47;*".  This allows such callers to benefit
     * from any built-in data conversion that a provider implements.
     *
     * @param uri
     * 		The desired URI to open.
     * @param mode
     * 		The file mode to use, as per {@link ContentProvider#openAssetFile
     * 		ContentProvider.openAssetFile}.
     * @return Returns a new ParcelFileDescriptor pointing to the file.  You
    own this descriptor and are responsible for closing it when done.
     * @throws FileNotFoundException
     * 		Throws FileNotFoundException of no
     * 		file exists under the URI or the mode is invalid.
     */
    @android.annotation.Nullable
    public final android.content.res.AssetFileDescriptor openAssetFileDescriptor(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mode) throws java.io.FileNotFoundException {
        return openAssetFileDescriptor(uri, mode, null);
    }

    /**
     * Open a raw file descriptor to access data under a URI.  This
     * interacts with the underlying {@link ContentProvider#openAssetFile}
     * method of the provider associated with the given URI, to retrieve any file stored there.
     *
     * <h5>Accepts the following URI schemes:</h5>
     * <ul>
     * <li>content ({@link #SCHEME_CONTENT})</li>
     * <li>android.resource ({@link #SCHEME_ANDROID_RESOURCE})</li>
     * <li>file ({@link #SCHEME_FILE})</li>
     * </ul>
     * <h5>The android.resource ({@link #SCHEME_ANDROID_RESOURCE}) Scheme</h5>
     * <p>
     * A Uri object can be used to reference a resource in an APK file.  The
     * Uri should be one of the following formats:
     * <ul>
     * <li><code>android.resource://package_name/id_number</code><br/>
     * <code>package_name</code> is your package name as listed in your AndroidManifest.xml.
     * For example <code>com.example.myapp</code><br/>
     * <code>id_number</code> is the int form of the ID.<br/>
     * The easiest way to construct this form is
     * <pre>Uri uri = Uri.parse("android.resource://com.example.myapp/" + R.raw.my_resource");</pre>
     * </li>
     * <li><code>android.resource://package_name/type/name</code><br/>
     * <code>package_name</code> is your package name as listed in your AndroidManifest.xml.
     * For example <code>com.example.myapp</code><br/>
     * <code>type</code> is the string form of the resource type.  For example, <code>raw</code>
     * or <code>drawable</code>.
     * <code>name</code> is the string form of the resource name.  That is, whatever the file
     * name was in your res directory, without the type extension.
     * The easiest way to construct this form is
     * <pre>Uri uri = Uri.parse("android.resource://com.example.myapp/raw/my_resource");</pre>
     * </li>
     * </ul>
     *
     * <p>Note that if this function is called for read-only input (mode is "r")
     * on a content: URI, it will instead call {@link #openTypedAssetFileDescriptor}
     * for you with a MIME type of "*&#47;*".  This allows such callers to benefit
     * from any built-in data conversion that a provider implements.
     *
     * @param uri
     * 		The desired URI to open.
     * @param mode
     * 		The file mode to use, as per {@link ContentProvider#openAssetFile
     * 		ContentProvider.openAssetFile}.
     * @param cancellationSignal
     * 		A signal to cancel the operation in progress, or null if
     * 		none. If the operation is canceled, then
     * 		{@link OperationCanceledException} will be thrown.
     * @return Returns a new ParcelFileDescriptor pointing to the file.  You
    own this descriptor and are responsible for closing it when done.
     * @throws FileNotFoundException
     * 		Throws FileNotFoundException of no
     * 		file exists under the URI or the mode is invalid.
     */
    @android.annotation.Nullable
    public final android.content.res.AssetFileDescriptor openAssetFileDescriptor(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mode, @android.annotation.Nullable
    android.os.CancellationSignal cancellationSignal) throws java.io.FileNotFoundException {
        com.android.internal.util.Preconditions.checkNotNull(uri, "uri");
        com.android.internal.util.Preconditions.checkNotNull(mode, "mode");
        try {
            if (mWrapped != null)
                return mWrapped.openAssetFile(uri, mode, cancellationSignal);

        } catch (android.os.RemoteException e) {
            return null;
        }
        java.lang.String scheme = uri.getScheme();
        if (android.content.ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
            if (!"r".equals(mode)) {
                throw new java.io.FileNotFoundException("Can't write resources: " + uri);
            }
            android.content.ContentResolver.OpenResourceIdResult r = getResourceId(uri);
            try {
                return r.r.openRawResourceFd(r.id);
            } catch (android.content.res.Resources.NotFoundException ex) {
                throw new java.io.FileNotFoundException("Resource does not exist: " + uri);
            }
        } else
            if (android.content.ContentResolver.SCHEME_FILE.equals(scheme)) {
                android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.open(new java.io.File(uri.getPath()), android.os.ParcelFileDescriptor.parseMode(mode));
                return new android.content.res.AssetFileDescriptor(pfd, 0, -1);
            } else {
                if ("r".equals(mode)) {
                    return openTypedAssetFileDescriptor(uri, "*/*", null, cancellationSignal);
                } else {
                    android.content.IContentProvider unstableProvider = acquireUnstableProvider(uri);
                    if (unstableProvider == null) {
                        throw new java.io.FileNotFoundException("No content provider: " + uri);
                    }
                    android.content.IContentProvider stableProvider = null;
                    android.content.res.AssetFileDescriptor fd = null;
                    try {
                        android.os.ICancellationSignal remoteCancellationSignal = null;
                        if (cancellationSignal != null) {
                            cancellationSignal.throwIfCanceled();
                            remoteCancellationSignal = unstableProvider.createCancellationSignal();
                            cancellationSignal.setRemote(remoteCancellationSignal);
                        }
                        try {
                            fd = unstableProvider.openAssetFile(mPackageName, uri, mode, remoteCancellationSignal);
                            if (fd == null) {
                                // The provider will be released by the finally{} clause
                                return null;
                            }
                        } catch (android.os.DeadObjectException e) {
                            // The remote process has died...  but we only hold an unstable
                            // reference though, so we might recover!!!  Let's try!!!!
                            // This is exciting!!1!!1!!!!1
                            unstableProviderDied(unstableProvider);
                            stableProvider = acquireProvider(uri);
                            if (stableProvider == null) {
                                throw new java.io.FileNotFoundException("No content provider: " + uri);
                            }
                            fd = stableProvider.openAssetFile(mPackageName, uri, mode, remoteCancellationSignal);
                            if (fd == null) {
                                // The provider will be released by the finally{} clause
                                return null;
                            }
                        }
                        if (stableProvider == null) {
                            stableProvider = acquireProvider(uri);
                        }
                        releaseUnstableProvider(unstableProvider);
                        unstableProvider = null;
                        android.os.ParcelFileDescriptor pfd = new android.content.ContentResolver.ParcelFileDescriptorInner(fd.getParcelFileDescriptor(), stableProvider);
                        // Success!  Don't release the provider when exiting, let
                        // ParcelFileDescriptorInner do that when it is closed.
                        stableProvider = null;
                        return new android.content.res.AssetFileDescriptor(pfd, fd.getStartOffset(), fd.getDeclaredLength());
                    } catch (android.os.RemoteException e) {
                        // Whatever, whatever, we'll go away.
                        throw new java.io.FileNotFoundException("Failed opening content provider: " + uri);
                    } catch (java.io.FileNotFoundException e) {
                        throw e;
                    } finally {
                        if (cancellationSignal != null) {
                            cancellationSignal.setRemote(null);
                        }
                        if (stableProvider != null) {
                            releaseProvider(stableProvider);
                        }
                        if (unstableProvider != null) {
                            releaseUnstableProvider(unstableProvider);
                        }
                    }
                }
            }

    }

    @java.lang.Override
    @android.annotation.Nullable
    public final android.content.res.AssetFileDescriptor openTypedAssetFile(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mimeTypeFilter, @android.annotation.Nullable
    android.os.Bundle opts, @android.annotation.Nullable
    android.os.CancellationSignal signal) throws java.io.FileNotFoundException {
        try {
            if (mWrapped != null) {
                return mWrapped.openTypedAssetFile(uri, mimeTypeFilter, opts, signal);
            }
        } catch (android.os.RemoteException e) {
            return null;
        }
        return openTypedAssetFileDescriptor(uri, mimeTypeFilter, opts, signal);
    }

    /**
     * Open a raw file descriptor to access (potentially type transformed)
     * data from a "content:" URI.  This interacts with the underlying
     * {@link ContentProvider#openTypedAssetFile} method of the provider
     * associated with the given URI, to retrieve retrieve any appropriate
     * data stream for the data stored there.
     *
     * <p>Unlike {@link #openAssetFileDescriptor}, this function only works
     * with "content:" URIs, because content providers are the only facility
     * with an associated MIME type to ensure that the returned data stream
     * is of the desired type.
     *
     * <p>All text/* streams are encoded in UTF-8.
     *
     * @param uri
     * 		The desired URI to open.
     * @param mimeType
     * 		The desired MIME type of the returned data.  This can
     * 		be a pattern such as *&#47;*, which will allow the content provider to
     * 		select a type, though there is no way for you to determine what type
     * 		it is returning.
     * @param opts
     * 		Additional provider-dependent options.
     * @return Returns a new ParcelFileDescriptor from which you can read the
    data stream from the provider.  Note that this may be a pipe, meaning
    you can't seek in it.  The only seek you should do is if the
    AssetFileDescriptor contains an offset, to move to that offset before
    reading.  You own this descriptor and are responsible for closing it when done.
     * @throws FileNotFoundException
     * 		Throws FileNotFoundException of no
     * 		data of the desired type exists under the URI.
     */
    @android.annotation.Nullable
    public final android.content.res.AssetFileDescriptor openTypedAssetFileDescriptor(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mimeType, @android.annotation.Nullable
    android.os.Bundle opts) throws java.io.FileNotFoundException {
        return openTypedAssetFileDescriptor(uri, mimeType, opts, null);
    }

    /**
     * Open a raw file descriptor to access (potentially type transformed)
     * data from a "content:" URI.  This interacts with the underlying
     * {@link ContentProvider#openTypedAssetFile} method of the provider
     * associated with the given URI, to retrieve retrieve any appropriate
     * data stream for the data stored there.
     *
     * <p>Unlike {@link #openAssetFileDescriptor}, this function only works
     * with "content:" URIs, because content providers are the only facility
     * with an associated MIME type to ensure that the returned data stream
     * is of the desired type.
     *
     * <p>All text/* streams are encoded in UTF-8.
     *
     * @param uri
     * 		The desired URI to open.
     * @param mimeType
     * 		The desired MIME type of the returned data.  This can
     * 		be a pattern such as *&#47;*, which will allow the content provider to
     * 		select a type, though there is no way for you to determine what type
     * 		it is returning.
     * @param opts
     * 		Additional provider-dependent options.
     * @param cancellationSignal
     * 		A signal to cancel the operation in progress,
     * 		or null if none. If the operation is canceled, then
     * 		{@link OperationCanceledException} will be thrown.
     * @return Returns a new ParcelFileDescriptor from which you can read the
    data stream from the provider.  Note that this may be a pipe, meaning
    you can't seek in it.  The only seek you should do is if the
    AssetFileDescriptor contains an offset, to move to that offset before
    reading.  You own this descriptor and are responsible for closing it when done.
     * @throws FileNotFoundException
     * 		Throws FileNotFoundException of no
     * 		data of the desired type exists under the URI.
     */
    @android.annotation.Nullable
    public final android.content.res.AssetFileDescriptor openTypedAssetFileDescriptor(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String mimeType, @android.annotation.Nullable
    android.os.Bundle opts, @android.annotation.Nullable
    android.os.CancellationSignal cancellationSignal) throws java.io.FileNotFoundException {
        com.android.internal.util.Preconditions.checkNotNull(uri, "uri");
        com.android.internal.util.Preconditions.checkNotNull(mimeType, "mimeType");
        try {
            if (mWrapped != null)
                return mWrapped.openTypedAssetFile(uri, mimeType, opts, cancellationSignal);

        } catch (android.os.RemoteException e) {
            return null;
        }
        android.content.IContentProvider unstableProvider = acquireUnstableProvider(uri);
        if (unstableProvider == null) {
            throw new java.io.FileNotFoundException("No content provider: " + uri);
        }
        android.content.IContentProvider stableProvider = null;
        android.content.res.AssetFileDescriptor fd = null;
        try {
            android.os.ICancellationSignal remoteCancellationSignal = null;
            if (cancellationSignal != null) {
                cancellationSignal.throwIfCanceled();
                remoteCancellationSignal = unstableProvider.createCancellationSignal();
                cancellationSignal.setRemote(remoteCancellationSignal);
            }
            try {
                fd = unstableProvider.openTypedAssetFile(mPackageName, uri, mimeType, opts, remoteCancellationSignal);
                if (fd == null) {
                    // The provider will be released by the finally{} clause
                    return null;
                }
            } catch (android.os.DeadObjectException e) {
                // The remote process has died...  but we only hold an unstable
                // reference though, so we might recover!!!  Let's try!!!!
                // This is exciting!!1!!1!!!!1
                unstableProviderDied(unstableProvider);
                stableProvider = acquireProvider(uri);
                if (stableProvider == null) {
                    throw new java.io.FileNotFoundException("No content provider: " + uri);
                }
                fd = stableProvider.openTypedAssetFile(mPackageName, uri, mimeType, opts, remoteCancellationSignal);
                if (fd == null) {
                    // The provider will be released by the finally{} clause
                    return null;
                }
            }
            if (stableProvider == null) {
                stableProvider = acquireProvider(uri);
            }
            releaseUnstableProvider(unstableProvider);
            unstableProvider = null;
            android.os.ParcelFileDescriptor pfd = new android.content.ContentResolver.ParcelFileDescriptorInner(fd.getParcelFileDescriptor(), stableProvider);
            // Success!  Don't release the provider when exiting, let
            // ParcelFileDescriptorInner do that when it is closed.
            stableProvider = null;
            return new android.content.res.AssetFileDescriptor(pfd, fd.getStartOffset(), fd.getDeclaredLength());
        } catch (android.os.RemoteException e) {
            // Whatever, whatever, we'll go away.
            throw new java.io.FileNotFoundException("Failed opening content provider: " + uri);
        } catch (java.io.FileNotFoundException e) {
            throw e;
        } finally {
            if (cancellationSignal != null) {
                cancellationSignal.setRemote(null);
            }
            if (stableProvider != null) {
                releaseProvider(stableProvider);
            }
            if (unstableProvider != null) {
                releaseUnstableProvider(unstableProvider);
            }
        }
    }

    /**
     * A resource identified by the {@link Resources} that contains it, and a resource id.
     *
     * @unknown 
     */
    public class OpenResourceIdResult {
        @android.annotation.UnsupportedAppUsage
        public android.content.res.Resources r;

        @android.annotation.UnsupportedAppUsage
        public int id;
    }

    /**
     * Resolves an android.resource URI to a {@link Resources} and a resource id.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public android.content.ContentResolver.OpenResourceIdResult getResourceId(android.net.Uri uri) throws java.io.FileNotFoundException {
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
        android.content.ContentResolver.OpenResourceIdResult res = new android.content.ContentResolver.OpenResourceIdResult();
        res.r = r;
        res.id = id;
        return res;
    }

    /**
     * Inserts a row into a table at the given URL.
     *
     * If the content provider supports transactions the insertion will be atomic.
     *
     * @param url
     * 		The URL of the table to insert into.
     * @param values
     * 		The initial values for the newly inserted row. The key is the column name for
     * 		the field. Passing an empty ContentValues will create an empty row.
     * @return the URL of the newly created row. May return <code>null</code> if the underlying
    content provider returns <code>null</code>, or if it crashes.
     */
    @java.lang.Override
    @android.annotation.Nullable
    public final android.net.Uri insert(@android.annotation.RequiresPermission.Write
    @android.annotation.NonNull
    android.net.Uri url, @android.annotation.Nullable
    android.content.ContentValues values) {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        try {
            if (mWrapped != null)
                return mWrapped.insert(url, values);

        } catch (android.os.RemoteException e) {
            return null;
        }
        android.content.IContentProvider provider = acquireProvider(url);
        if (provider == null) {
            throw new java.lang.IllegalArgumentException("Unknown URL " + url);
        }
        try {
            long startTime = android.os.SystemClock.uptimeMillis();
            android.net.Uri createdRow = provider.insert(mPackageName, url, values);
            long durationMillis = android.os.SystemClock.uptimeMillis() - startTime;
            /* where */
            maybeLogUpdateToEventLog(durationMillis, url, "insert", null);
            return createdRow;
        } catch (android.os.RemoteException e) {
            // Arbitrary and not worth documenting, as Activity
            // Manager will kill this process shortly anyway.
            return null;
        } finally {
            releaseProvider(provider);
        }
    }

    /**
     * Applies each of the {@link ContentProviderOperation} objects and returns an array
     * of their results. Passes through OperationApplicationException, which may be thrown
     * by the call to {@link ContentProviderOperation#apply}.
     * If all the applications succeed then a {@link ContentProviderResult} array with the
     * same number of elements as the operations will be returned. It is implementation-specific
     * how many, if any, operations will have been successfully applied if a call to
     * apply results in a {@link OperationApplicationException}.
     *
     * @param authority
     * 		the authority of the ContentProvider to which this batch should be applied
     * @param operations
     * 		the operations to apply
     * @return the results of the applications
     * @throws OperationApplicationException
     * 		thrown if an application fails.
     * 		See {@link ContentProviderOperation#apply} for more information.
     * @throws RemoteException
     * 		thrown if a RemoteException is encountered while attempting
     * 		to communicate with a remote provider.
     */
    @java.lang.Override
    @android.annotation.NonNull
    public android.content.ContentProviderResult[] applyBatch(@android.annotation.NonNull
    java.lang.String authority, @android.annotation.NonNull
    java.util.ArrayList<android.content.ContentProviderOperation> operations) throws android.content.OperationApplicationException, android.os.RemoteException {
        com.android.internal.util.Preconditions.checkNotNull(authority, "authority");
        com.android.internal.util.Preconditions.checkNotNull(operations, "operations");
        try {
            if (mWrapped != null)
                return mWrapped.applyBatch(authority, operations);

        } catch (android.os.RemoteException e) {
            return null;
        }
        android.content.ContentProviderClient provider = acquireContentProviderClient(authority);
        if (provider == null) {
            throw new java.lang.IllegalArgumentException("Unknown authority " + authority);
        }
        try {
            return provider.applyBatch(operations);
        } finally {
            provider.release();
        }
    }

    /**
     * Inserts multiple rows into a table at the given URL.
     *
     * This function make no guarantees about the atomicity of the insertions.
     *
     * @param url
     * 		The URL of the table to insert into.
     * @param values
     * 		The initial values for the newly inserted rows. The key is the column name for
     * 		the field. Passing null will create an empty row.
     * @return the number of newly created rows.
     */
    @java.lang.Override
    public final int bulkInsert(@android.annotation.RequiresPermission.Write
    @android.annotation.NonNull
    android.net.Uri url, @android.annotation.NonNull
    android.content.ContentValues[] values) {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        com.android.internal.util.Preconditions.checkNotNull(values, "values");
        try {
            if (mWrapped != null)
                return mWrapped.bulkInsert(url, values);

        } catch (android.os.RemoteException e) {
            return 0;
        }
        android.content.IContentProvider provider = acquireProvider(url);
        if (provider == null) {
            throw new java.lang.IllegalArgumentException("Unknown URL " + url);
        }
        try {
            long startTime = android.os.SystemClock.uptimeMillis();
            int rowsCreated = provider.bulkInsert(mPackageName, url, values);
            long durationMillis = android.os.SystemClock.uptimeMillis() - startTime;
            /* where */
            maybeLogUpdateToEventLog(durationMillis, url, "bulkinsert", null);
            return rowsCreated;
        } catch (android.os.RemoteException e) {
            // Arbitrary and not worth documenting, as Activity
            // Manager will kill this process shortly anyway.
            return 0;
        } finally {
            releaseProvider(provider);
        }
    }

    /**
     * Deletes row(s) specified by a content URI.
     *
     * If the content provider supports transactions, the deletion will be atomic.
     *
     * @param url
     * 		The URL of the row to delete.
     * @param where
     * 		A filter to apply to rows before deleting, formatted as an SQL WHERE clause
     * 		(excluding the WHERE itself).
     * @return The number of rows deleted.
     */
    @java.lang.Override
    public final int delete(@android.annotation.RequiresPermission.Write
    @android.annotation.NonNull
    android.net.Uri url, @android.annotation.Nullable
    java.lang.String where, @android.annotation.Nullable
    java.lang.String[] selectionArgs) {
        com.android.internal.util.Preconditions.checkNotNull(url, "url");
        try {
            if (mWrapped != null)
                return mWrapped.delete(url, where, selectionArgs);

        } catch (android.os.RemoteException e) {
            return 0;
        }
        android.content.IContentProvider provider = acquireProvider(url);
        if (provider == null) {
            throw new java.lang.IllegalArgumentException("Unknown URL " + url);
        }
        try {
            long startTime = android.os.SystemClock.uptimeMillis();
            int rowsDeleted = provider.delete(mPackageName, url, where, selectionArgs);
            long durationMillis = android.os.SystemClock.uptimeMillis() - startTime;
            maybeLogUpdateToEventLog(durationMillis, url, "delete", where);
            return rowsDeleted;
        } catch (android.os.RemoteException e) {
            // Arbitrary and not worth documenting, as Activity
            // Manager will kill this process shortly anyway.
            return -1;
        } finally {
            releaseProvider(provider);
        }
    }

    /**
     * Update row(s) in a content URI.
     *
     * If the content provider supports transactions the update will be atomic.
     *
     * @param uri
     * 		The URI to modify.
     * @param values
     * 		The new field values. The key is the column name for the field.
     * 		A null value will remove an existing field value.
     * @param where
     * 		A filter to apply to rows before updating, formatted as an SQL WHERE clause
     * 		(excluding the WHERE itself).
     * @return the number of rows updated.
     * @throws NullPointerException
     * 		if uri or values are null
     */
    @java.lang.Override
    public final int update(@android.annotation.RequiresPermission.Write
    @android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    android.content.ContentValues values, @android.annotation.Nullable
    java.lang.String where, @android.annotation.Nullable
    java.lang.String[] selectionArgs) {
        com.android.internal.util.Preconditions.checkNotNull(uri, "uri");
        try {
            if (mWrapped != null)
                return mWrapped.update(uri, values, where, selectionArgs);

        } catch (android.os.RemoteException e) {
            return 0;
        }
        android.content.IContentProvider provider = acquireProvider(uri);
        if (provider == null) {
            throw new java.lang.IllegalArgumentException("Unknown URI " + uri);
        }
        try {
            long startTime = android.os.SystemClock.uptimeMillis();
            int rowsUpdated = provider.update(mPackageName, uri, values, where, selectionArgs);
            long durationMillis = android.os.SystemClock.uptimeMillis() - startTime;
            maybeLogUpdateToEventLog(durationMillis, uri, "update", where);
            return rowsUpdated;
        } catch (android.os.RemoteException e) {
            // Arbitrary and not worth documenting, as Activity
            // Manager will kill this process shortly anyway.
            return -1;
        } finally {
            releaseProvider(provider);
        }
    }

    /**
     * Call a provider-defined method.  This can be used to implement
     * read or write interfaces which are cheaper than using a Cursor and/or
     * do not fit into the traditional table model.
     *
     * @param method
     * 		provider-defined method name to call.  Opaque to
     * 		framework, but must be non-null.
     * @param arg
     * 		provider-defined String argument.  May be null.
     * @param extras
     * 		provider-defined Bundle argument.  May be null.
     * @return a result Bundle, possibly null.  Will be null if the ContentProvider
    does not implement call.
     * @throws NullPointerException
     * 		if uri or method is null
     * @throws IllegalArgumentException
     * 		if uri is not known
     */
    @android.annotation.Nullable
    public final android.os.Bundle call(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    java.lang.String method, @android.annotation.Nullable
    java.lang.String arg, @android.annotation.Nullable
    android.os.Bundle extras) {
        return call(uri.getAuthority(), method, arg, extras);
    }

    @java.lang.Override
    @android.annotation.Nullable
    public final android.os.Bundle call(@android.annotation.NonNull
    java.lang.String authority, @android.annotation.NonNull
    java.lang.String method, @android.annotation.Nullable
    java.lang.String arg, @android.annotation.Nullable
    android.os.Bundle extras) {
        com.android.internal.util.Preconditions.checkNotNull(authority, "authority");
        com.android.internal.util.Preconditions.checkNotNull(method, "method");
        try {
            if (mWrapped != null)
                return mWrapped.call(authority, method, arg, extras);

        } catch (android.os.RemoteException e) {
            return null;
        }
        android.content.IContentProvider provider = acquireProvider(authority);
        if (provider == null) {
            throw new java.lang.IllegalArgumentException("Unknown authority " + authority);
        }
        try {
            final android.os.Bundle res = provider.call(mPackageName, authority, method, arg, extras);
            android.os.Bundle.setDefusable(res, true);
            return res;
        } catch (android.os.RemoteException e) {
            // Arbitrary and not worth documenting, as Activity
            // Manager will kill this process shortly anyway.
            return null;
        } finally {
            releaseProvider(provider);
        }
    }

    /**
     * Returns the content provider for the given content URI.
     *
     * @param uri
     * 		The URI to a content provider
     * @return The ContentProvider for the given URI, or null if no content provider is found.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public final android.content.IContentProvider acquireProvider(android.net.Uri uri) {
        if (!android.content.ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            return null;
        }
        final java.lang.String auth = uri.getAuthority();
        if (auth != null) {
            return acquireProvider(mContext, auth);
        }
        return null;
    }

    /**
     * Returns the content provider for the given content URI if the process
     * already has a reference on it.
     *
     * @param uri
     * 		The URI to a content provider
     * @return The ContentProvider for the given URI, or null if no content provider is found.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public final android.content.IContentProvider acquireExistingProvider(android.net.Uri uri) {
        if (!android.content.ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            return null;
        }
        final java.lang.String auth = uri.getAuthority();
        if (auth != null) {
            return acquireExistingProvider(mContext, auth);
        }
        return null;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public final android.content.IContentProvider acquireProvider(java.lang.String name) {
        if (name == null) {
            return null;
        }
        return acquireProvider(mContext, name);
    }

    /**
     * Returns the content provider for the given content URI.
     *
     * @param uri
     * 		The URI to a content provider
     * @return The ContentProvider for the given URI, or null if no content provider is found.
     * @unknown 
     */
    public final android.content.IContentProvider acquireUnstableProvider(android.net.Uri uri) {
        if (!android.content.ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            return null;
        }
        java.lang.String auth = uri.getAuthority();
        if (auth != null) {
            return acquireUnstableProvider(mContext, uri.getAuthority());
        }
        return null;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public final android.content.IContentProvider acquireUnstableProvider(java.lang.String name) {
        if (name == null) {
            return null;
        }
        return acquireUnstableProvider(mContext, name);
    }

    /**
     * Returns a {@link ContentProviderClient} that is associated with the {@link ContentProvider}
     * that services the content at uri, starting the provider if necessary. Returns
     * null if there is no provider associated wih the uri. The caller must indicate that they are
     * done with the provider by calling {@link ContentProviderClient#release} which will allow
     * the system to release the provider if it determines that there is no other reason for
     * keeping it active.
     *
     * @param uri
     * 		specifies which provider should be acquired
     * @return a {@link ContentProviderClient} that is associated with the {@link ContentProvider}
    that services the content at uri or null if there isn't one.
     */
    @android.annotation.Nullable
    public final android.content.ContentProviderClient acquireContentProviderClient(@android.annotation.NonNull
    android.net.Uri uri) {
        com.android.internal.util.Preconditions.checkNotNull(uri, "uri");
        android.content.IContentProvider provider = acquireProvider(uri);
        if (provider != null) {
            return new android.content.ContentProviderClient(this, provider, uri.getAuthority(), true);
        }
        return null;
    }

    /**
     * Returns a {@link ContentProviderClient} that is associated with the {@link ContentProvider}
     * with the authority of name, starting the provider if necessary. Returns
     * null if there is no provider associated wih the uri. The caller must indicate that they are
     * done with the provider by calling {@link ContentProviderClient#release} which will allow
     * the system to release the provider if it determines that there is no other reason for
     * keeping it active.
     *
     * @param name
     * 		specifies which provider should be acquired
     * @return a {@link ContentProviderClient} that is associated with the {@link ContentProvider}
    with the authority of name or null if there isn't one.
     */
    @android.annotation.Nullable
    public final android.content.ContentProviderClient acquireContentProviderClient(@android.annotation.NonNull
    java.lang.String name) {
        com.android.internal.util.Preconditions.checkNotNull(name, "name");
        android.content.IContentProvider provider = acquireProvider(name);
        if (provider != null) {
            return new android.content.ContentProviderClient(this, provider, name, true);
        }
        return null;
    }

    /**
     * Like {@link #acquireContentProviderClient(Uri)}, but for use when you do
     * not trust the stability of the target content provider.  This turns off
     * the mechanism in the platform clean up processes that are dependent on
     * a content provider if that content provider's process goes away.  Normally
     * you can safely assume that once you have acquired a provider, you can freely
     * use it as needed and it won't disappear, even if your process is in the
     * background.  If using this method, you need to take care to deal with any
     * failures when communicating with the provider, and be sure to close it
     * so that it can be re-opened later.  In particular, catching a
     * {@link android.os.DeadObjectException} from the calls there will let you
     * know that the content provider has gone away; at that point the current
     * ContentProviderClient object is invalid, and you should release it.  You
     * can acquire a new one if you would like to try to restart the provider
     * and perform new operations on it.
     */
    @android.annotation.Nullable
    public final android.content.ContentProviderClient acquireUnstableContentProviderClient(@android.annotation.NonNull
    android.net.Uri uri) {
        com.android.internal.util.Preconditions.checkNotNull(uri, "uri");
        android.content.IContentProvider provider = acquireUnstableProvider(uri);
        if (provider != null) {
            return new android.content.ContentProviderClient(this, provider, uri.getAuthority(), false);
        }
        return null;
    }

    /**
     * Like {@link #acquireContentProviderClient(String)}, but for use when you do
     * not trust the stability of the target content provider.  This turns off
     * the mechanism in the platform clean up processes that are dependent on
     * a content provider if that content provider's process goes away.  Normally
     * you can safely assume that once you have acquired a provider, you can freely
     * use it as needed and it won't disappear, even if your process is in the
     * background.  If using this method, you need to take care to deal with any
     * failures when communicating with the provider, and be sure to close it
     * so that it can be re-opened later.  In particular, catching a
     * {@link android.os.DeadObjectException} from the calls there will let you
     * know that the content provider has gone away; at that point the current
     * ContentProviderClient object is invalid, and you should release it.  You
     * can acquire a new one if you would like to try to restart the provider
     * and perform new operations on it.
     */
    @android.annotation.Nullable
    public final android.content.ContentProviderClient acquireUnstableContentProviderClient(@android.annotation.NonNull
    java.lang.String name) {
        com.android.internal.util.Preconditions.checkNotNull(name, "name");
        android.content.IContentProvider provider = acquireUnstableProvider(name);
        if (provider != null) {
            return new android.content.ContentProviderClient(this, provider, name, false);
        }
        return null;
    }

    /**
     * Register an observer class that gets callbacks when data identified by a
     * given content URI changes.
     * <p>
     * Starting in {@link android.os.Build.VERSION_CODES#O}, all content
     * notifications must be backed by a valid {@link ContentProvider}.
     *
     * @param uri
     * 		The URI to watch for changes. This can be a specific row URI,
     * 		or a base URI for a whole class of content.
     * @param notifyForDescendants
     * 		When false, the observer will be notified
     * 		whenever a change occurs to the exact URI specified by
     * 		<code>uri</code> or to one of the URI's ancestors in the path
     * 		hierarchy. When true, the observer will also be notified
     * 		whenever a change occurs to the URI's descendants in the path
     * 		hierarchy.
     * @param observer
     * 		The object that receives callbacks when changes occur.
     * @see #unregisterContentObserver
     */
    public final void registerContentObserver(@android.annotation.NonNull
    android.net.Uri uri, boolean notifyForDescendants, @android.annotation.NonNull
    android.database.ContentObserver observer) {
        com.android.internal.util.Preconditions.checkNotNull(uri, "uri");
        com.android.internal.util.Preconditions.checkNotNull(observer, "observer");
        registerContentObserver(getUriWithoutUserId(uri), notifyForDescendants, observer, ContentProvider.getUserIdFromUri(uri, mContext.getUserId()));
    }

    /**
     *
     *
     * @unknown - designated user version
     */
    @android.annotation.UnsupportedAppUsage
    public final void registerContentObserver(android.net.Uri uri, boolean notifyForDescendents, android.database.ContentObserver observer, @android.annotation.UserIdInt
    int userHandle) {
        try {
            android.content.ContentResolver.getContentService().registerContentObserver(uri, notifyForDescendents, observer.getContentObserver(), userHandle, mTargetSdkVersion);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Unregisters a change observer.
     *
     * @param observer
     * 		The previously registered observer that is no longer needed.
     * @see #registerContentObserver
     */
    public final void unregisterContentObserver(@android.annotation.NonNull
    android.database.ContentObserver observer) {
        com.android.internal.util.Preconditions.checkNotNull(observer, "observer");
        try {
            android.database.IContentObserver contentObserver = observer.releaseContentObserver();
            if (contentObserver != null) {
                android.content.ContentResolver.getContentService().unregisterContentObserver(contentObserver);
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Notify registered observers that a row was updated and attempt to sync
     * changes to the network.
     * <p>
     * To observe events sent through this call, use
     * {@link #registerContentObserver(Uri, boolean, ContentObserver)}.
     * <p>
     * Starting in {@link android.os.Build.VERSION_CODES#O}, all content
     * notifications must be backed by a valid {@link ContentProvider}.
     *
     * @param uri
     * 		The uri of the content that was changed.
     * @param observer
     * 		The observer that originated the change, may be
     * 		<code>null</null>. The observer that originated the change
     * 		will only receive the notification if it has requested to
     * 		receive self-change notifications by implementing
     * 		{@link ContentObserver#deliverSelfNotifications()} to return
     * 		true.
     */
    public void notifyChange(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    android.database.ContentObserver observer) {
        /* sync to network */
        notifyChange(uri, observer, true);
    }

    /**
     * Notify registered observers that a row was updated.
     * <p>
     * To observe events sent through this call, use
     * {@link #registerContentObserver(Uri, boolean, ContentObserver)}.
     * <p>
     * If syncToNetwork is true, this will attempt to schedule a local sync
     * using the sync adapter that's registered for the authority of the
     * provided uri. No account will be passed to the sync adapter, so all
     * matching accounts will be synchronized.
     * <p>
     * Starting in {@link android.os.Build.VERSION_CODES#O}, all content
     * notifications must be backed by a valid {@link ContentProvider}.
     *
     * @param uri
     * 		The uri of the content that was changed.
     * @param observer
     * 		The observer that originated the change, may be
     * 		<code>null</null>. The observer that originated the change
     * 		will only receive the notification if it has requested to
     * 		receive self-change notifications by implementing
     * 		{@link ContentObserver#deliverSelfNotifications()} to return
     * 		true.
     * @param syncToNetwork
     * 		If true, same as {@link #NOTIFY_SYNC_TO_NETWORK}.
     * @see #requestSync(android.accounts.Account, String, android.os.Bundle)
     */
    public void notifyChange(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    android.database.ContentObserver observer, boolean syncToNetwork) {
        com.android.internal.util.Preconditions.checkNotNull(uri, "uri");
        notifyChange(getUriWithoutUserId(uri), observer, syncToNetwork, ContentProvider.getUserIdFromUri(uri, mContext.getUserId()));
    }

    /**
     * Notify registered observers that a row was updated.
     * <p>
     * To observe events sent through this call, use
     * {@link #registerContentObserver(Uri, boolean, ContentObserver)}.
     * <p>
     * If syncToNetwork is true, this will attempt to schedule a local sync
     * using the sync adapter that's registered for the authority of the
     * provided uri. No account will be passed to the sync adapter, so all
     * matching accounts will be synchronized.
     * <p>
     * Starting in {@link android.os.Build.VERSION_CODES#O}, all content
     * notifications must be backed by a valid {@link ContentProvider}.
     *
     * @param uri
     * 		The uri of the content that was changed.
     * @param observer
     * 		The observer that originated the change, may be
     * 		<code>null</null>. The observer that originated the change
     * 		will only receive the notification if it has requested to
     * 		receive self-change notifications by implementing
     * 		{@link ContentObserver#deliverSelfNotifications()} to return
     * 		true.
     * @param flags
     * 		Additional flags: {@link #NOTIFY_SYNC_TO_NETWORK}.
     * @see #requestSync(android.accounts.Account, String, android.os.Bundle)
     */
    public void notifyChange(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    android.database.ContentObserver observer, @android.content.ContentResolver.NotifyFlags
    int flags) {
        com.android.internal.util.Preconditions.checkNotNull(uri, "uri");
        notifyChange(getUriWithoutUserId(uri), observer, flags, ContentProvider.getUserIdFromUri(uri, mContext.getUserId()));
    }

    /**
     * Notify registered observers within the designated user(s) that a row was updated.
     *
     * @unknown 
     */
    public void notifyChange(@android.annotation.NonNull
    android.net.Uri uri, android.database.ContentObserver observer, boolean syncToNetwork, @android.annotation.UserIdInt
    int userHandle) {
        try {
            android.content.ContentResolver.getContentService().notifyChange(uri, observer == null ? null : observer.getContentObserver(), (observer != null) && observer.deliverSelfNotifications(), syncToNetwork ? android.content.ContentResolver.NOTIFY_SYNC_TO_NETWORK : 0, userHandle, mTargetSdkVersion, mContext.getPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Notify registered observers within the designated user(s) that a row was updated.
     *
     * @unknown 
     */
    public void notifyChange(@android.annotation.NonNull
    android.net.Uri uri, android.database.ContentObserver observer, @android.content.ContentResolver.NotifyFlags
    int flags, @android.annotation.UserIdInt
    int userHandle) {
        try {
            android.content.ContentResolver.getContentService().notifyChange(uri, observer == null ? null : observer.getContentObserver(), (observer != null) && observer.deliverSelfNotifications(), flags, userHandle, mTargetSdkVersion, mContext.getPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Take a persistable URI permission grant that has been offered. Once
     * taken, the permission grant will be remembered across device reboots.
     * Only URI permissions granted with
     * {@link Intent#FLAG_GRANT_PERSISTABLE_URI_PERMISSION} can be persisted. If
     * the grant has already been persisted, taking it again will touch
     * {@link UriPermission#getPersistedTime()}.
     *
     * @see #getPersistedUriPermissions()
     */
    public void takePersistableUriPermission(@android.annotation.NonNull
    android.net.Uri uri, @android.content.Intent.AccessUriMode
    int modeFlags) {
        com.android.internal.util.Preconditions.checkNotNull(uri, "uri");
        try {
            /* toPackage= */
            takePersistableUriPermission(getUriWithoutUserId(uri), modeFlags, null, resolveUserId(uri));
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void takePersistableUriPermission(@android.annotation.NonNull
    java.lang.String toPackage, @android.annotation.NonNull
    android.net.Uri uri, @android.content.Intent.AccessUriMode
    int modeFlags) {
        com.android.internal.util.Preconditions.checkNotNull(toPackage, "toPackage");
        com.android.internal.util.Preconditions.checkNotNull(uri, "uri");
        try {
            getService().takePersistableUriPermission(getUriWithoutUserId(uri), modeFlags, toPackage, resolveUserId(uri));
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Relinquish a persisted URI permission grant. The URI must have been
     * previously made persistent with
     * {@link #takePersistableUriPermission(Uri, int)}. Any non-persistent
     * grants to the calling package will remain intact.
     *
     * @see #getPersistedUriPermissions()
     */
    public void releasePersistableUriPermission(@android.annotation.NonNull
    android.net.Uri uri, @android.content.Intent.AccessUriMode
    int modeFlags) {
        com.android.internal.util.Preconditions.checkNotNull(uri, "uri");
        try {
            /* toPackage= */
            releasePersistableUriPermission(getUriWithoutUserId(uri), modeFlags, null, resolveUserId(uri));
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Return list of all URI permission grants that have been persisted by the
     * calling app. That is, the returned permissions have been granted
     * <em>to</em> the calling app. Only persistable grants taken with
     * {@link #takePersistableUriPermission(Uri, int)} are returned.
     * <p>Note: Some of the returned URIs may not be usable until after the user is unlocked.
     *
     * @see #takePersistableUriPermission(Uri, int)
     * @see #releasePersistableUriPermission(Uri, int)
     */
    @android.annotation.NonNull
    public java.util.List<android.content.UriPermission> getPersistedUriPermissions() {
        try {
            return /* incoming */
            /* persistedOnly */
            getService().getUriPermissions(mPackageName, true, true).getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Return list of all persisted URI permission grants that are hosted by the
     * calling app. That is, the returned permissions have been granted
     * <em>from</em> the calling app. Only grants taken with
     * {@link #takePersistableUriPermission(Uri, int)} are returned.
     * <p>Note: Some of the returned URIs may not be usable until after the user is unlocked.
     */
    @android.annotation.NonNull
    public java.util.List<android.content.UriPermission> getOutgoingPersistedUriPermissions() {
        try {
            return /* incoming */
            /* persistedOnly */
            getService().getUriPermissions(mPackageName, false, true).getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public java.util.List<android.content.UriPermission> getOutgoingUriPermissions() {
        try {
            return /* incoming */
            /* persistedOnly */
            getService().getUriPermissions(mPackageName, false, false).getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Start an asynchronous sync operation. If you want to monitor the progress
     * of the sync you may register a SyncObserver. Only values of the following
     * types may be used in the extras bundle:
     * <ul>
     * <li>Integer</li>
     * <li>Long</li>
     * <li>Boolean</li>
     * <li>Float</li>
     * <li>Double</li>
     * <li>String</li>
     * <li>Account</li>
     * <li>null</li>
     * </ul>
     *
     * @param uri
     * 		the uri of the provider to sync or null to sync all providers.
     * @param extras
     * 		any extras to pass to the SyncAdapter.
     * @deprecated instead use
    {@link #requestSync(android.accounts.Account, String, android.os.Bundle)}
     */
    @java.lang.Deprecated
    public void startSync(android.net.Uri uri, android.os.Bundle extras) {
        android.accounts.Account account = null;
        if (extras != null) {
            java.lang.String accountName = extras.getString(android.content.ContentResolver.SYNC_EXTRAS_ACCOUNT);
            if (!android.text.TextUtils.isEmpty(accountName)) {
                // TODO: No references to Google in AOSP
                account = new android.accounts.Account(accountName, "com.google");
            }
            extras.remove(android.content.ContentResolver.SYNC_EXTRAS_ACCOUNT);
        }
        android.content.ContentResolver.requestSync(account, uri != null ? uri.getAuthority() : null, extras);
    }

    /**
     * Start an asynchronous sync operation. If you want to monitor the progress
     * of the sync you may register a SyncObserver. Only values of the following
     * types may be used in the extras bundle:
     * <ul>
     * <li>Integer</li>
     * <li>Long</li>
     * <li>Boolean</li>
     * <li>Float</li>
     * <li>Double</li>
     * <li>String</li>
     * <li>Account</li>
     * <li>null</li>
     * </ul>
     *
     * @param account
     * 		which account should be synced
     * @param authority
     * 		which authority should be synced
     * @param extras
     * 		any extras to pass to the SyncAdapter.
     */
    public static void requestSync(android.accounts.Account account, java.lang.String authority, android.os.Bundle extras) {
        android.content.ContentResolver.requestSyncAsUser(account, authority, android.os.UserHandle.myUserId(), extras);
    }

    /**
     *
     *
     * @see #requestSync(Account, String, Bundle)
     * @unknown 
     */
    public static void requestSyncAsUser(android.accounts.Account account, java.lang.String authority, @android.annotation.UserIdInt
    int userId, android.os.Bundle extras) {
        if (extras == null) {
            throw new java.lang.IllegalArgumentException("Must specify extras.");
        }
        android.content.SyncRequest request = // Immediate sync.
        new android.content.SyncRequest.Builder().setSyncAdapter(account, authority).setExtras(extras).syncOnce().build();
        try {
            // Note ActivityThread.currentPackageName() may not be accurate in a shared process
            // case, but it's only for debugging.
            android.content.ContentResolver.getContentService().syncAsUser(request, userId, android.app.ActivityThread.currentPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Register a sync with the SyncManager. These requests are built using the
     * {@link SyncRequest.Builder}.
     */
    public static void requestSync(android.content.SyncRequest request) {
        try {
            // Note ActivityThread.currentPackageName() may not be accurate in a shared process
            // case, but it's only for debugging.
            android.content.ContentResolver.getContentService().sync(request, android.app.ActivityThread.currentPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Check that only values of the following types are in the Bundle:
     * <ul>
     * <li>Integer</li>
     * <li>Long</li>
     * <li>Boolean</li>
     * <li>Float</li>
     * <li>Double</li>
     * <li>String</li>
     * <li>Account</li>
     * <li>null</li>
     * </ul>
     *
     * @param extras
     * 		the Bundle to check
     */
    public static void validateSyncExtrasBundle(android.os.Bundle extras) {
        try {
            for (java.lang.String key : extras.keySet()) {
                java.lang.Object value = extras.get(key);
                if (value == null)
                    continue;

                if (value instanceof java.lang.Long)
                    continue;

                if (value instanceof java.lang.Integer)
                    continue;

                if (value instanceof java.lang.Boolean)
                    continue;

                if (value instanceof java.lang.Float)
                    continue;

                if (value instanceof java.lang.Double)
                    continue;

                if (value instanceof java.lang.String)
                    continue;

                if (value instanceof android.accounts.Account)
                    continue;

                throw new java.lang.IllegalArgumentException("unexpected value type: " + value.getClass().getName());
            }
        } catch (java.lang.IllegalArgumentException e) {
            throw e;
        } catch (java.lang.RuntimeException exc) {
            throw new java.lang.IllegalArgumentException("error unparceling Bundle", exc);
        }
    }

    /**
     * Cancel any active or pending syncs that match the Uri. If the uri is null then
     * all syncs will be canceled.
     *
     * @param uri
     * 		the uri of the provider to sync or null to sync all providers.
     * @deprecated instead use {@link #cancelSync(android.accounts.Account, String)}
     */
    @java.lang.Deprecated
    public void cancelSync(android.net.Uri uri) {
        /* all accounts */
        android.content.ContentResolver.cancelSync(null, uri != null ? uri.getAuthority() : null);
    }

    /**
     * Cancel any active or pending syncs that match account and authority. The account and
     * authority can each independently be set to null, which means that syncs with any account
     * or authority, respectively, will match.
     *
     * @param account
     * 		filters the syncs that match by this account
     * @param authority
     * 		filters the syncs that match by this authority
     */
    public static void cancelSync(android.accounts.Account account, java.lang.String authority) {
        try {
            android.content.ContentResolver.getContentService().cancelSync(account, authority, null);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @see #cancelSync(Account, String)
     * @unknown 
     */
    public static void cancelSyncAsUser(android.accounts.Account account, java.lang.String authority, @android.annotation.UserIdInt
    int userId) {
        try {
            android.content.ContentResolver.getContentService().cancelSyncAsUser(account, authority, null, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Get information about the SyncAdapters that are known to the system.
     *
     * @return an array of SyncAdapters that have registered with the system
     */
    public static android.content.SyncAdapterType[] getSyncAdapterTypes() {
        try {
            return android.content.ContentResolver.getContentService().getSyncAdapterTypes();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @see #getSyncAdapterTypes()
     * @unknown 
     */
    public static android.content.SyncAdapterType[] getSyncAdapterTypesAsUser(@android.annotation.UserIdInt
    int userId) {
        try {
            return android.content.ContentResolver.getContentService().getSyncAdapterTypesAsUser(userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown Returns the package names of syncadapters that match a given user and authority.
     */
    @android.annotation.TestApi
    public static java.lang.String[] getSyncAdapterPackagesForAuthorityAsUser(java.lang.String authority, @android.annotation.UserIdInt
    int userId) {
        try {
            return android.content.ContentResolver.getContentService().getSyncAdapterPackagesForAuthorityAsUser(authority, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Check if the provider should be synced when a network tickle is received
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#READ_SYNC_SETTINGS}.
     *
     * @param account
     * 		the account whose setting we are querying
     * @param authority
     * 		the provider whose setting we are querying
     * @return true if the provider should be synced when a network tickle is received
     */
    public static boolean getSyncAutomatically(android.accounts.Account account, java.lang.String authority) {
        try {
            return android.content.ContentResolver.getContentService().getSyncAutomatically(account, authority);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @see #getSyncAutomatically(Account, String)
     * @unknown 
     */
    public static boolean getSyncAutomaticallyAsUser(android.accounts.Account account, java.lang.String authority, @android.annotation.UserIdInt
    int userId) {
        try {
            return android.content.ContentResolver.getContentService().getSyncAutomaticallyAsUser(account, authority, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Set whether or not the provider is synced when it receives a network tickle.
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#WRITE_SYNC_SETTINGS}.
     *
     * @param account
     * 		the account whose setting we are querying
     * @param authority
     * 		the provider whose behavior is being controlled
     * @param sync
     * 		true if the provider should be synced when tickles are received for it
     */
    public static void setSyncAutomatically(android.accounts.Account account, java.lang.String authority, boolean sync) {
        android.content.ContentResolver.setSyncAutomaticallyAsUser(account, authority, sync, android.os.UserHandle.myUserId());
    }

    /**
     *
     *
     * @see #setSyncAutomatically(Account, String, boolean)
     * @unknown 
     */
    public static void setSyncAutomaticallyAsUser(android.accounts.Account account, java.lang.String authority, boolean sync, @android.annotation.UserIdInt
    int userId) {
        try {
            android.content.ContentResolver.getContentService().setSyncAutomaticallyAsUser(account, authority, sync, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Specifies that a sync should be requested with the specified the account, authority,
     * and extras at the given frequency. If there is already another periodic sync scheduled
     * with the account, authority and extras then a new periodic sync won't be added, instead
     * the frequency of the previous one will be updated.
     * <p>
     * These periodic syncs honor the "syncAutomatically" and "masterSyncAutomatically" settings.
     * Although these sync are scheduled at the specified frequency, it may take longer for it to
     * actually be started if other syncs are ahead of it in the sync operation queue. This means
     * that the actual start time may drift.
     * <p>
     * Periodic syncs are not allowed to have any of {@link #SYNC_EXTRAS_DO_NOT_RETRY},
     * {@link #SYNC_EXTRAS_IGNORE_BACKOFF}, {@link #SYNC_EXTRAS_IGNORE_SETTINGS},
     * {@link #SYNC_EXTRAS_INITIALIZE}, {@link #SYNC_EXTRAS_FORCE},
     * {@link #SYNC_EXTRAS_EXPEDITED}, {@link #SYNC_EXTRAS_MANUAL} set to true.
     * If any are supplied then an {@link IllegalArgumentException} will be thrown.
     *
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#WRITE_SYNC_SETTINGS}.
     * <p>The bundle for a periodic sync can be queried by applications with the correct
     * permissions using
     * {@link ContentResolver#getPeriodicSyncs(Account account, String provider)}, so no
     * sensitive data should be transferred here.
     *
     * @param account
     * 		the account to specify in the sync
     * @param authority
     * 		the provider to specify in the sync request
     * @param extras
     * 		extra parameters to go along with the sync request
     * @param pollFrequency
     * 		how frequently the sync should be performed, in seconds.
     * 		On Android API level 24 and above, a minmam interval of 15 minutes is enforced.
     * 		On previous versions, the minimum interval is 1 hour.
     * @throws IllegalArgumentException
     * 		if an illegal extra was set or if any of the parameters
     * 		are null.
     */
    public static void addPeriodicSync(android.accounts.Account account, java.lang.String authority, android.os.Bundle extras, long pollFrequency) {
        android.content.ContentResolver.validateSyncExtrasBundle(extras);
        if (android.content.ContentResolver.invalidPeriodicExtras(extras)) {
            throw new java.lang.IllegalArgumentException("illegal extras were set");
        }
        try {
            android.content.ContentResolver.getContentService().addPeriodicSync(account, authority, extras, pollFrequency);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     * Helper function to throw an <code>IllegalArgumentException</code> if any illegal
     * extras were set for a periodic sync.
     *
     * @param extras
     * 		bundle to validate.
     */
    public static boolean invalidPeriodicExtras(android.os.Bundle extras) {
        if ((((((extras.getBoolean(android.content.ContentResolver.SYNC_EXTRAS_MANUAL, false) || extras.getBoolean(android.content.ContentResolver.SYNC_EXTRAS_DO_NOT_RETRY, false)) || extras.getBoolean(android.content.ContentResolver.SYNC_EXTRAS_IGNORE_BACKOFF, false)) || extras.getBoolean(android.content.ContentResolver.SYNC_EXTRAS_IGNORE_SETTINGS, false)) || extras.getBoolean(android.content.ContentResolver.SYNC_EXTRAS_INITIALIZE, false)) || extras.getBoolean(android.content.ContentResolver.SYNC_EXTRAS_FORCE, false)) || extras.getBoolean(android.content.ContentResolver.SYNC_EXTRAS_EXPEDITED, false)) {
            return true;
        }
        return false;
    }

    /**
     * Remove a periodic sync. Has no affect if account, authority and extras don't match
     * an existing periodic sync.
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#WRITE_SYNC_SETTINGS}.
     *
     * @param account
     * 		the account of the periodic sync to remove
     * @param authority
     * 		the provider of the periodic sync to remove
     * @param extras
     * 		the extras of the periodic sync to remove
     */
    public static void removePeriodicSync(android.accounts.Account account, java.lang.String authority, android.os.Bundle extras) {
        android.content.ContentResolver.validateSyncExtrasBundle(extras);
        try {
            android.content.ContentResolver.getContentService().removePeriodicSync(account, authority, extras);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Remove the specified sync. This will cancel any pending or active syncs. If the request is
     * for a periodic sync, this call will remove any future occurrences.
     * <p>
     *     If a periodic sync is specified, the caller must hold the permission
     *     {@link android.Manifest.permission#WRITE_SYNC_SETTINGS}.
     * </p>
     * It is possible to cancel a sync using a SyncRequest object that is not the same object
     * with which you requested the sync. Do so by building a SyncRequest with the same
     * adapter, frequency, <b>and</b> extras bundle.
     *
     * @param request
     * 		SyncRequest object containing information about sync to cancel.
     */
    public static void cancelSync(android.content.SyncRequest request) {
        if (request == null) {
            throw new java.lang.IllegalArgumentException("request cannot be null");
        }
        try {
            android.content.ContentResolver.getContentService().cancelRequest(request);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Get the list of information about the periodic syncs for the given account and authority.
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#READ_SYNC_SETTINGS}.
     *
     * @param account
     * 		the account whose periodic syncs we are querying
     * @param authority
     * 		the provider whose periodic syncs we are querying
     * @return a list of PeriodicSync objects. This list may be empty but will never be null.
     */
    public static java.util.List<android.content.PeriodicSync> getPeriodicSyncs(android.accounts.Account account, java.lang.String authority) {
        try {
            return android.content.ContentResolver.getContentService().getPeriodicSyncs(account, authority, null);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Check if this account/provider is syncable.
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#READ_SYNC_SETTINGS}.
     *
     * @return >0 if it is syncable, 0 if not, and <0 if the state isn't known yet.
     */
    public static int getIsSyncable(android.accounts.Account account, java.lang.String authority) {
        try {
            return android.content.ContentResolver.getContentService().getIsSyncable(account, authority);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @see #getIsSyncable(Account, String)
     * @unknown 
     */
    public static int getIsSyncableAsUser(android.accounts.Account account, java.lang.String authority, @android.annotation.UserIdInt
    int userId) {
        try {
            return android.content.ContentResolver.getContentService().getIsSyncableAsUser(account, authority, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Set whether this account/provider is syncable.
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#WRITE_SYNC_SETTINGS}.
     *
     * @param syncable
     * 		>0 denotes syncable, 0 means not syncable, <0 means unknown
     */
    public static void setIsSyncable(android.accounts.Account account, java.lang.String authority, int syncable) {
        try {
            android.content.ContentResolver.getContentService().setIsSyncable(account, authority, syncable);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @see #setIsSyncable(Account, String, int)
     * @unknown 
     */
    public static void setIsSyncableAsUser(android.accounts.Account account, java.lang.String authority, int syncable, int userId) {
        try {
            android.content.ContentResolver.getContentService().setIsSyncableAsUser(account, authority, syncable, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Gets the master auto-sync setting that applies to all the providers and accounts.
     * If this is false then the per-provider auto-sync setting is ignored.
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#READ_SYNC_SETTINGS}.
     *
     * @return the master auto-sync setting that applies to all the providers and accounts
     */
    public static boolean getMasterSyncAutomatically() {
        try {
            return android.content.ContentResolver.getContentService().getMasterSyncAutomatically();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @see #getMasterSyncAutomatically()
     * @unknown 
     */
    public static boolean getMasterSyncAutomaticallyAsUser(@android.annotation.UserIdInt
    int userId) {
        try {
            return android.content.ContentResolver.getContentService().getMasterSyncAutomaticallyAsUser(userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Sets the master auto-sync setting that applies to all the providers and accounts.
     * If this is false then the per-provider auto-sync setting is ignored.
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#WRITE_SYNC_SETTINGS}.
     *
     * @param sync
     * 		the master auto-sync setting that applies to all the providers and accounts
     */
    public static void setMasterSyncAutomatically(boolean sync) {
        android.content.ContentResolver.setMasterSyncAutomaticallyAsUser(sync, android.os.UserHandle.myUserId());
    }

    /**
     *
     *
     * @see #setMasterSyncAutomatically(boolean)
     * @unknown 
     */
    public static void setMasterSyncAutomaticallyAsUser(boolean sync, @android.annotation.UserIdInt
    int userId) {
        try {
            android.content.ContentResolver.getContentService().setMasterSyncAutomaticallyAsUser(sync, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns true if there is currently a sync operation for the given account or authority
     * actively being processed.
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#READ_SYNC_STATS}.
     *
     * @param account
     * 		the account whose setting we are querying
     * @param authority
     * 		the provider whose behavior is being queried
     * @return true if a sync is active for the given account or authority.
     */
    public static boolean isSyncActive(android.accounts.Account account, java.lang.String authority) {
        if (account == null) {
            throw new java.lang.IllegalArgumentException("account must not be null");
        }
        if (authority == null) {
            throw new java.lang.IllegalArgumentException("authority must not be null");
        }
        try {
            return android.content.ContentResolver.getContentService().isSyncActive(account, authority, null);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * If a sync is active returns the information about it, otherwise returns null.
     * <p>
     * This method requires the caller to hold the permission
     * {@link android.Manifest.permission#READ_SYNC_STATS}.
     * <p>
     *
     * @return the SyncInfo for the currently active sync or null if one is not active.
     * @deprecated Since multiple concurrent syncs are now supported you should use
    {@link #getCurrentSyncs()} to get the accurate list of current syncs.
    This method returns the first item from the list of current syncs
    or null if there are none.
     */
    @java.lang.Deprecated
    public static android.content.SyncInfo getCurrentSync() {
        try {
            final java.util.List<android.content.SyncInfo> syncs = android.content.ContentResolver.getContentService().getCurrentSyncs();
            if (syncs.isEmpty()) {
                return null;
            }
            return syncs.get(0);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns a list with information about all the active syncs. This list will be empty
     * if there are no active syncs.
     * <p>
     * This method requires the caller to hold the permission
     * {@link android.Manifest.permission#READ_SYNC_STATS}.
     * <p>
     *
     * @return a List of SyncInfo objects for the currently active syncs.
     */
    public static java.util.List<android.content.SyncInfo> getCurrentSyncs() {
        try {
            return android.content.ContentResolver.getContentService().getCurrentSyncs();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @see #getCurrentSyncs()
     * @unknown 
     */
    public static java.util.List<android.content.SyncInfo> getCurrentSyncsAsUser(@android.annotation.UserIdInt
    int userId) {
        try {
            return android.content.ContentResolver.getContentService().getCurrentSyncsAsUser(userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns the status that matches the authority.
     *
     * @param account
     * 		the account whose setting we are querying
     * @param authority
     * 		the provider whose behavior is being queried
     * @return the SyncStatusInfo for the authority, or null if none exists
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static android.content.SyncStatusInfo getSyncStatus(android.accounts.Account account, java.lang.String authority) {
        try {
            return android.content.ContentResolver.getContentService().getSyncStatus(account, authority, null);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @see #getSyncStatus(Account, String)
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static android.content.SyncStatusInfo getSyncStatusAsUser(android.accounts.Account account, java.lang.String authority, @android.annotation.UserIdInt
    int userId) {
        try {
            return android.content.ContentResolver.getContentService().getSyncStatusAsUser(account, authority, null, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Return true if the pending status is true of any matching authorities.
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#READ_SYNC_STATS}.
     *
     * @param account
     * 		the account whose setting we are querying
     * @param authority
     * 		the provider whose behavior is being queried
     * @return true if there is a pending sync with the matching account and authority
     */
    public static boolean isSyncPending(android.accounts.Account account, java.lang.String authority) {
        return android.content.ContentResolver.isSyncPendingAsUser(account, authority, android.os.UserHandle.myUserId());
    }

    /**
     *
     *
     * @see #requestSync(Account, String, Bundle)
     * @unknown 
     */
    public static boolean isSyncPendingAsUser(android.accounts.Account account, java.lang.String authority, @android.annotation.UserIdInt
    int userId) {
        try {
            return android.content.ContentResolver.getContentService().isSyncPendingAsUser(account, authority, null, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Request notifications when the different aspects of the SyncManager change. The
     * different items that can be requested are:
     * <ul>
     * <li> {@link #SYNC_OBSERVER_TYPE_PENDING}
     * <li> {@link #SYNC_OBSERVER_TYPE_ACTIVE}
     * <li> {@link #SYNC_OBSERVER_TYPE_SETTINGS}
     * </ul>
     * The caller can set one or more of the status types in the mask for any
     * given listener registration.
     *
     * @param mask
     * 		the status change types that will cause the callback to be invoked
     * @param callback
     * 		observer to be invoked when the status changes
     * @return a handle that can be used to remove the listener at a later time
     */
    public static java.lang.Object addStatusChangeListener(int mask, final android.content.SyncStatusObserver callback) {
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("you passed in a null callback");
        }
        try {
            android.content.ISyncStatusObserver.Stub observer = new android.content.ISyncStatusObserver.Stub() {
                @java.lang.Override
                public void onStatusChanged(int which) throws android.os.RemoteException {
                    callback.onStatusChanged(which);
                }
            };
            android.content.ContentResolver.getContentService().addStatusChangeListener(mask, observer);
            return observer;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Remove a previously registered status change listener.
     *
     * @param handle
     * 		the handle that was returned by {@link #addStatusChangeListener}
     */
    public static void removeStatusChangeListener(java.lang.Object handle) {
        if (handle == null) {
            throw new java.lang.IllegalArgumentException("you passed in a null handle");
        }
        try {
            android.content.ContentResolver.getContentService().removeStatusChangeListener(((android.content.ISyncStatusObserver.Stub) (handle)));
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Store the given {@link Bundle} as a long-lived cached object within the
     * system. This can be useful to avoid expensive re-parsing when apps are
     * restarted multiple times on low-RAM devices.
     * <p>
     * The {@link Bundle} is automatically invalidated when a
     * {@link #notifyChange(Uri, ContentObserver)} event applies to the key.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.RequiresPermission(android.content.android.Manifest.permission.CACHE_CONTENT.class)
    public void putCache(@android.annotation.NonNull
    android.net.Uri key, @android.annotation.Nullable
    android.os.Bundle value) {
        try {
            android.content.ContentResolver.getContentService().putCache(mContext.getPackageName(), key, value, mContext.getUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Retrieve the last {@link Bundle} stored as a long-lived cached object
     * within the system.
     *
     * @return {@code null} if no cached object has been stored, or if the
    stored object has been invalidated due to a
    {@link #notifyChange(Uri, ContentObserver)} event.
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.RequiresPermission(android.content.android.Manifest.permission.CACHE_CONTENT.class)
    @android.annotation.Nullable
    public android.os.Bundle getCache(@android.annotation.NonNull
    android.net.Uri key) {
        try {
            final android.os.Bundle bundle = android.content.ContentResolver.getContentService().getCache(mContext.getPackageName(), key, mContext.getUserId());
            if (bundle != null)
                bundle.setClassLoader(mContext.getClassLoader());

            return bundle;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public int getTargetSdkVersion() {
        return mTargetSdkVersion;
    }

    /**
     * Returns sampling percentage for a given duration.
     *
     * Always returns at least 1%.
     */
    private int samplePercentForDuration(long durationMillis) {
        if (durationMillis >= android.content.ContentResolver.SLOW_THRESHOLD_MILLIS) {
            return 100;
        }
        return ((int) ((100 * durationMillis) / android.content.ContentResolver.SLOW_THRESHOLD_MILLIS)) + 1;
    }

    private void maybeLogQueryToEventLog(long durationMillis, android.net.Uri uri, java.lang.String[] projection, @android.annotation.Nullable
    android.os.Bundle queryArgs) {
        if (!android.content.ContentResolver.ENABLE_CONTENT_SAMPLE)
            return;

        int samplePercent = samplePercentForDuration(durationMillis);
        if (samplePercent < 100) {
            synchronized(mRandom) {
                if (mRandom.nextInt(100) >= samplePercent) {
                    return;
                }
            }
        }
        // Ensure a non-null bundle.
        queryArgs = (queryArgs != null) ? queryArgs : android.os.Bundle.EMPTY;
        java.lang.StringBuilder projectionBuffer = new java.lang.StringBuilder(100);
        if (projection != null) {
            for (int i = 0; i < projection.length; ++i) {
                // Note: not using a comma delimiter here, as the
                // multiple arguments to EventLog.writeEvent later
                // stringify with a comma delimiter, which would make
                // parsing uglier later.
                if (i != 0)
                    projectionBuffer.append('/');

                projectionBuffer.append(projection[i]);
            }
        }
        // ActivityThread.currentPackageName() only returns non-null if the
        // current thread is an application main thread.  This parameter tells
        // us whether an event loop is blocked, and if so, which app it is.
        java.lang.String blockingPackage = android.app.AppGlobals.getInitialPackage();
        android.util.EventLog.writeEvent(EventLogTags.CONTENT_QUERY_SAMPLE, uri.toString(), projectionBuffer.toString(), queryArgs.getString(android.content.ContentResolver.QUERY_ARG_SQL_SELECTION, ""), queryArgs.getString(android.content.ContentResolver.QUERY_ARG_SQL_SORT_ORDER, ""), durationMillis, blockingPackage != null ? blockingPackage : "", samplePercent);
    }

    private void maybeLogUpdateToEventLog(long durationMillis, android.net.Uri uri, java.lang.String operation, java.lang.String selection) {
        if (!android.content.ContentResolver.ENABLE_CONTENT_SAMPLE)
            return;

        int samplePercent = samplePercentForDuration(durationMillis);
        if (samplePercent < 100) {
            synchronized(mRandom) {
                if (mRandom.nextInt(100) >= samplePercent) {
                    return;
                }
            }
        }
        java.lang.String blockingPackage = android.app.AppGlobals.getInitialPackage();
        android.util.EventLog.writeEvent(EventLogTags.CONTENT_UPDATE_SAMPLE, uri.toString(), operation, selection != null ? selection : "", durationMillis, blockingPackage != null ? blockingPackage : "", samplePercent);
    }

    private final class CursorWrapperInner extends android.database.CrossProcessCursorWrapper {
        private final android.content.IContentProvider mContentProvider;

        private final java.util.concurrent.atomic.AtomicBoolean mProviderReleased = new java.util.concurrent.atomic.AtomicBoolean();

        private final dalvik.system.CloseGuard mCloseGuard = dalvik.system.CloseGuard.get();

        CursorWrapperInner(android.database.Cursor cursor, android.content.IContentProvider contentProvider) {
            super(cursor);
            mContentProvider = contentProvider;
            mCloseGuard.open("close");
        }

        @java.lang.Override
        public void close() {
            mCloseGuard.close();
            close();
            if (mProviderReleased.compareAndSet(false, true)) {
                android.content.ContentResolver.this.releaseProvider(mContentProvider);
            }
        }

        @java.lang.Override
        protected void finalize() throws java.lang.Throwable {
            try {
                if (mCloseGuard != null) {
                    mCloseGuard.warnIfOpen();
                }
                close();
            } finally {
                super.finalize();
            }
        }
    }

    private final class ParcelFileDescriptorInner extends android.os.ParcelFileDescriptor {
        private final android.content.IContentProvider mContentProvider;

        private final java.util.concurrent.atomic.AtomicBoolean mProviderReleased = new java.util.concurrent.atomic.AtomicBoolean();

        ParcelFileDescriptorInner(android.os.ParcelFileDescriptor pfd, android.content.IContentProvider icp) {
            super(pfd);
            mContentProvider = icp;
        }

        @java.lang.Override
        public void releaseResources() {
            if (mProviderReleased.compareAndSet(false, true)) {
                android.content.ContentResolver.this.releaseProvider(mContentProvider);
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String CONTENT_SERVICE_NAME = "content";

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static android.content.IContentService getContentService() {
        if (android.content.ContentResolver.sContentService != null) {
            return android.content.ContentResolver.sContentService;
        }
        android.os.IBinder b = android.os.ServiceManager.getService(android.content.ContentResolver.CONTENT_SERVICE_NAME);
        android.content.ContentResolver.sContentService = IContentService.Stub.asInterface(b);
        return android.content.ContentResolver.sContentService;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public java.lang.String getPackageName() {
        return mPackageName;
    }

    @android.annotation.UnsupportedAppUsage
    private static volatile android.content.IContentService sContentService;

    @android.annotation.UnsupportedAppUsage
    private final android.content.Context mContext;

    @android.annotation.UnsupportedAppUsage
    final java.lang.String mPackageName;

    final int mTargetSdkVersion;

    final android.content.ContentInterface mWrapped;

    private static final java.lang.String TAG = "ContentResolver";

    /**
     *
     *
     * @unknown 
     */
    public int resolveUserId(android.net.Uri uri) {
        return ContentProvider.getUserIdFromUri(uri, mContext.getUserId());
    }

    /**
     *
     *
     * @unknown 
     */
    public int getUserId() {
        return mContext.getUserId();
    }

    /**
     * {@hide }
     */
    @java.lang.Deprecated
    public android.graphics.drawable.Drawable getTypeDrawable(java.lang.String mimeType) {
        return getTypeInfo(mimeType).getIcon().loadDrawable(mContext);
    }

    /**
     * Return a detailed description of the given MIME type, including an icon
     * and label that describe the type.
     *
     * @param mimeType
     * 		Valid, concrete MIME type.
     */
    @android.annotation.NonNull
    public final android.content.ContentResolver.MimeTypeInfo getTypeInfo(@android.annotation.NonNull
    java.lang.String mimeType) {
        java.util.Objects.requireNonNull(mimeType);
        return com.android.internal.util.MimeIconUtils.getTypeInfo(mimeType);
    }

    /**
     * Detailed description of a specific MIME type, including an icon and label
     * that describe the type.
     */
    public static final class MimeTypeInfo {
        private final android.graphics.drawable.Icon mIcon;

        private final java.lang.CharSequence mLabel;

        private final java.lang.CharSequence mContentDescription;

        /**
         * {@hide }
         */
        public MimeTypeInfo(@android.annotation.NonNull
        android.graphics.drawable.Icon icon, @android.annotation.NonNull
        java.lang.CharSequence label, @android.annotation.NonNull
        java.lang.CharSequence contentDescription) {
            mIcon = java.util.Objects.requireNonNull(icon);
            mLabel = java.util.Objects.requireNonNull(label);
            mContentDescription = java.util.Objects.requireNonNull(contentDescription);
        }

        /**
         * Return a visual representation of this MIME type. This can be styled
         * using {@link Icon#setTint(int)} to match surrounding UI.
         *
         * @see Icon#loadDrawable(Context)
         * @see android.widget.ImageView#setImageDrawable(Drawable)
         */
        @android.annotation.NonNull
        public android.graphics.drawable.Icon getIcon() {
            return mIcon;
        }

        /**
         * Return a textual representation of this MIME type.
         *
         * @see android.widget.TextView#setText(CharSequence)
         */
        @android.annotation.NonNull
        public java.lang.CharSequence getLabel() {
            return mLabel;
        }

        /**
         * Return a content description for this MIME type.
         *
         * @see android.view.View#setContentDescription(CharSequence)
         */
        @android.annotation.NonNull
        public java.lang.CharSequence getContentDescription() {
            return mContentDescription;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.Nullable
    public static android.os.Bundle createSqlQueryBundle(@android.annotation.Nullable
    java.lang.String selection, @android.annotation.Nullable
    java.lang.String[] selectionArgs, @android.annotation.Nullable
    java.lang.String sortOrder) {
        if (((selection == null) && (selectionArgs == null)) && (sortOrder == null)) {
            return null;
        }
        android.os.Bundle queryArgs = new android.os.Bundle();
        if (selection != null) {
            queryArgs.putString(android.content.ContentResolver.QUERY_ARG_SQL_SELECTION, selection);
        }
        if (selectionArgs != null) {
            queryArgs.putStringArray(android.content.ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs);
        }
        if (sortOrder != null) {
            queryArgs.putString(android.content.ContentResolver.QUERY_ARG_SQL_SORT_ORDER, sortOrder);
        }
        return queryArgs;
    }

    /**
     * Returns structured sort args formatted as an SQL sort clause.
     *
     * NOTE: Collator clauses are suitable for use with non text fields. We might
     * choose to omit any collation clause since we don't know the underlying
     * type of data to be collated. Imperical testing shows that sqlite3 doesn't
     * appear to care much about the presence of collate clauses in queries
     * when ordering by numeric fields. For this reason we include collate
     * clause unilaterally when {@link #QUERY_ARG_SORT_COLLATION} is present
     * in query args bundle.
     *
     * TODO: Would be nice to explicitly validate that colums referenced in
     * {@link #QUERY_ARG_SORT_COLUMNS} are present in the associated projection.
     *
     * @unknown 
     */
    public static java.lang.String createSqlSortClause(android.os.Bundle queryArgs) {
        java.lang.String[] columns = queryArgs.getStringArray(android.content.ContentResolver.QUERY_ARG_SORT_COLUMNS);
        if ((columns == null) || (columns.length == 0)) {
            throw new java.lang.IllegalArgumentException("Can't create sort clause without columns.");
        }
        java.lang.String query = android.text.TextUtils.join(", ", columns);
        // Interpret PRIMARY and SECONDARY collation strength as no-case collation based
        // on their javadoc descriptions.
        int collation = queryArgs.getInt(android.content.ContentResolver.QUERY_ARG_SORT_COLLATION, java.text.Collator.IDENTICAL);
        if ((collation == java.text.Collator.PRIMARY) || (collation == java.text.Collator.SECONDARY)) {
            query += " COLLATE NOCASE";
        }
        int sortDir = queryArgs.getInt(android.content.ContentResolver.QUERY_ARG_SORT_DIRECTION, java.lang.Integer.MIN_VALUE);
        if (sortDir != java.lang.Integer.MIN_VALUE) {
            switch (sortDir) {
                case android.content.ContentResolver.QUERY_SORT_DIRECTION_ASCENDING :
                    query += " ASC";
                    break;
                case android.content.ContentResolver.QUERY_SORT_DIRECTION_DESCENDING :
                    query += " DESC";
                    break;
                default :
                    throw new java.lang.IllegalArgumentException("Unsupported sort direction value." + " See ContentResolver documentation for details.");
            }
        }
        return query;
    }

    /**
     * Convenience method that efficiently loads a visual thumbnail for the
     * given {@link Uri}. Internally calls
     * {@link ContentProvider#openTypedAssetFile} on the remote provider, but
     * also defensively resizes any returned content to match the requested
     * target size.
     *
     * @param uri
     * 		The item that should be visualized as a thumbnail.
     * @param size
     * 		The target area on the screen where this thumbnail will be
     * 		shown. This is passed to the provider as {@link #EXTRA_SIZE}
     * 		to help it avoid downloading or generating heavy resources.
     * @param signal
     * 		A signal to cancel the operation in progress.
     * @return Valid {@link Bitmap} which is a visual thumbnail.
     * @throws IOException
     * 		If any trouble was encountered while generating or
     * 		loading the thumbnail, or if
     * 		{@link CancellationSignal#cancel()} was invoked.
     */
    @android.annotation.NonNull
    public android.graphics.Bitmap loadThumbnail(@android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    android.util.Size size, @android.annotation.Nullable
    android.os.CancellationSignal signal) throws java.io.IOException {
        return android.content.ContentResolver.loadThumbnail(this, uri, size, signal, android.graphics.ImageDecoder.ALLOCATOR_SOFTWARE);
    }

    /**
     * {@hide }
     */
    public static android.graphics.Bitmap loadThumbnail(@android.annotation.NonNull
    android.content.ContentInterface content, @android.annotation.NonNull
    android.net.Uri uri, @android.annotation.NonNull
    android.util.Size size, @android.annotation.Nullable
    android.os.CancellationSignal signal, int allocator) throws java.io.IOException {
        java.util.Objects.requireNonNull(content);
        java.util.Objects.requireNonNull(uri);
        java.util.Objects.requireNonNull(size);
        // Convert to Point, since that's what the API is defined as
        final android.os.Bundle opts = new android.os.Bundle();
        opts.putParcelable(android.content.ContentResolver.EXTRA_SIZE, android.graphics.Point.convert(size));
        final android.system.Int32Ref orientation = new android.system.Int32Ref(0);
        android.graphics.Bitmap bitmap = android.graphics.ImageDecoder.decodeBitmap(android.graphics.ImageDecoder.createSource(() -> {
            final android.content.res.AssetFileDescriptor afd = content.openTypedAssetFile(uri, "image/*", opts, signal);
            final android.os.Bundle extras = afd.getExtras();
            orientation.value = (extras != null) ? extras.getInt(EXTRA_ORIENTATION, 0) : 0;
            return afd;
        }), (android.graphics.ImageDecoder decoder,android.graphics.ImageDecoder.ImageInfo info,android.graphics.ImageDecoder.Source source) -> {
            decoder.setAllocator(allocator);
            // One last-ditch check to see if we've been canceled.
            if (signal != null)
                signal.throwIfCanceled();

            // We requested a rough thumbnail size, but the remote size may have
            // returned something giant, so defensively scale down as needed.
            final int widthSample = info.getSize().getWidth() / size.getWidth();
            final int heightSample = info.getSize().getHeight() / size.getHeight();
            final int sample = java.lang.Math.min(widthSample, heightSample);
            if (sample > 1) {
                decoder.setTargetSampleSize(sample);
            }
        });
        // Transform the bitmap if requested. We use a side-channel to
        // communicate the orientation, since EXIF thumbnails don't contain
        // the rotation flags of the original image.
        if (orientation.value != 0) {
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();
            final android.graphics.Matrix m = new android.graphics.Matrix();
            m.setRotate(orientation.value, width / 2, height / 2);
            bitmap = android.graphics.Bitmap.createBitmap(bitmap, 0, 0, width, height, m, false);
        }
        return bitmap;
    }

    /**
     * {@hide }
     */
    public static void onDbCorruption(java.lang.String tag, java.lang.String message, java.lang.Throwable stacktrace) {
        try {
            android.content.ContentResolver.getContentService().onDbCorruption(tag, message, android.util.Log.getStackTraceString(stacktrace));
        } catch (android.os.RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public static android.net.Uri translateDeprecatedDataPath(java.lang.String path) {
        final java.lang.String ssp = "//" + path.substring(android.content.ContentResolver.DEPRECATE_DATA_PREFIX.length());
        return android.net.Uri.parse(new android.net.Uri.Builder().scheme(android.content.ContentResolver.SCHEME_CONTENT).encodedOpaquePart(ssp).build().toString());
    }

    /**
     * {@hide }
     */
    public static java.lang.String translateDeprecatedDataPath(android.net.Uri uri) {
        return android.content.ContentResolver.DEPRECATE_DATA_PREFIX + uri.getEncodedSchemeSpecificPart().substring(2);
    }
}

