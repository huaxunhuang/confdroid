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
package android.database.sqlite;


/**
 * Represents a SQLite database connection.
 * Each connection wraps an instance of a native <code>sqlite3</code> object.
 * <p>
 * When database connection pooling is enabled, there can be multiple active
 * connections to the same database.  Otherwise there is typically only one
 * connection per database.
 * </p><p>
 * When the SQLite WAL feature is enabled, multiple readers and one writer
 * can concurrently access the database.  Without WAL, readers and writers
 * are mutually exclusive.
 * </p>
 *
 * <h2>Ownership and concurrency guarantees</h2>
 * <p>
 * Connection objects are not thread-safe.  They are acquired as needed to
 * perform a database operation and are then returned to the pool.  At any
 * given time, a connection is either owned and used by a {@link SQLiteSession}
 * object or the {@link SQLiteConnectionPool}.  Those classes are
 * responsible for serializing operations to guard against concurrent
 * use of a connection.
 * </p><p>
 * The guarantee of having a single owner allows this class to be implemented
 * without locks and greatly simplifies resource management.
 * </p>
 *
 * <h2>Encapsulation guarantees</h2>
 * <p>
 * The connection object object owns *all* of the SQLite related native
 * objects that are associated with the connection.  What's more, there are
 * no other objects in the system that are capable of obtaining handles to
 * those native objects.  Consequently, when the connection is closed, we do
 * not have to worry about what other components might have references to
 * its associated SQLite state -- there are none.
 * </p><p>
 * Encapsulation is what ensures that the connection object's
 * lifecycle does not become a tortured mess of finalizers and reference
 * queues.
 * </p>
 *
 * <h2>Reentrance</h2>
 * <p>
 * This class must tolerate reentrant execution of SQLite operations because
 * triggers may call custom SQLite functions that perform additional queries.
 * </p>
 *
 * @unknown 
 */
public final class SQLiteConnection implements android.os.CancellationSignal.OnCancelListener {
    private static final java.lang.String TAG = "SQLiteConnection";

    private static final boolean DEBUG = false;

    private static final java.lang.String[] EMPTY_STRING_ARRAY = new java.lang.String[0];

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private final dalvik.system.CloseGuard mCloseGuard = dalvik.system.CloseGuard.get();

    private final android.database.sqlite.SQLiteConnectionPool mPool;

    private final android.database.sqlite.SQLiteDatabaseConfiguration mConfiguration;

    private final int mConnectionId;

    private final boolean mIsPrimaryConnection;

    private final boolean mIsReadOnlyConnection;

    private final android.database.sqlite.SQLiteConnection.PreparedStatementCache mPreparedStatementCache;

    private android.database.sqlite.SQLiteConnection.PreparedStatement mPreparedStatementPool;

    // The recent operations log.
    private final android.database.sqlite.SQLiteConnection.OperationLog mRecentOperations = new android.database.sqlite.SQLiteConnection.OperationLog();

    // The native SQLiteConnection pointer.  (FOR INTERNAL USE ONLY)
    private long mConnectionPtr;

    private boolean mOnlyAllowReadOnlyOperations;

    // The number of times attachCancellationSignal has been called.
    // Because SQLite statement execution can be reentrant, we keep track of how many
    // times we have attempted to attach a cancellation signal to the connection so that
    // we can ensure that we detach the signal at the right time.
    private int mCancellationSignalAttachCount;

    private static native long nativeOpen(java.lang.String path, int openFlags, java.lang.String label, boolean enableTrace, boolean enableProfile);

    private static native void nativeClose(long connectionPtr);

    private static native void nativeRegisterCustomFunction(long connectionPtr, android.database.sqlite.SQLiteCustomFunction function);

    private static native void nativeRegisterLocalizedCollators(long connectionPtr, java.lang.String locale);

    private static native long nativePrepareStatement(long connectionPtr, java.lang.String sql);

    private static native void nativeFinalizeStatement(long connectionPtr, long statementPtr);

    private static native int nativeGetParameterCount(long connectionPtr, long statementPtr);

    private static native boolean nativeIsReadOnly(long connectionPtr, long statementPtr);

    private static native int nativeGetColumnCount(long connectionPtr, long statementPtr);

    private static native java.lang.String nativeGetColumnName(long connectionPtr, long statementPtr, int index);

    private static native void nativeBindNull(long connectionPtr, long statementPtr, int index);

    private static native void nativeBindLong(long connectionPtr, long statementPtr, int index, long value);

    private static native void nativeBindDouble(long connectionPtr, long statementPtr, int index, double value);

    private static native void nativeBindString(long connectionPtr, long statementPtr, int index, java.lang.String value);

    private static native void nativeBindBlob(long connectionPtr, long statementPtr, int index, byte[] value);

    private static native void nativeResetStatementAndClearBindings(long connectionPtr, long statementPtr);

    private static native void nativeExecute(long connectionPtr, long statementPtr);

    private static native long nativeExecuteForLong(long connectionPtr, long statementPtr);

    private static native java.lang.String nativeExecuteForString(long connectionPtr, long statementPtr);

    private static native int nativeExecuteForBlobFileDescriptor(long connectionPtr, long statementPtr);

    private static native int nativeExecuteForChangedRowCount(long connectionPtr, long statementPtr);

    private static native long nativeExecuteForLastInsertedRowId(long connectionPtr, long statementPtr);

    private static native long nativeExecuteForCursorWindow(long connectionPtr, long statementPtr, long windowPtr, int startPos, int requiredPos, boolean countAllRows);

    private static native int nativeGetDbLookaside(long connectionPtr);

    private static native void nativeCancel(long connectionPtr);

    private static native void nativeResetCancel(long connectionPtr, boolean cancelable);

    private SQLiteConnection(android.database.sqlite.SQLiteConnectionPool pool, android.database.sqlite.SQLiteDatabaseConfiguration configuration, int connectionId, boolean primaryConnection) {
        mPool = pool;
        mConfiguration = new android.database.sqlite.SQLiteDatabaseConfiguration(configuration);
        mConnectionId = connectionId;
        mIsPrimaryConnection = primaryConnection;
        mIsReadOnlyConnection = (configuration.openFlags & android.database.sqlite.SQLiteDatabase.OPEN_READONLY) != 0;
        mPreparedStatementCache = new android.database.sqlite.SQLiteConnection.PreparedStatementCache(mConfiguration.maxSqlCacheSize);
        mCloseGuard.open("close");
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        try {
            if ((mPool != null) && (mConnectionPtr != 0)) {
                mPool.onConnectionLeaked();
            }
            dispose(true);
        } finally {
            super.finalize();
        }
    }

    // Called by SQLiteConnectionPool only.
    static android.database.sqlite.SQLiteConnection open(android.database.sqlite.SQLiteConnectionPool pool, android.database.sqlite.SQLiteDatabaseConfiguration configuration, int connectionId, boolean primaryConnection) {
        android.database.sqlite.SQLiteConnection connection = new android.database.sqlite.SQLiteConnection(pool, configuration, connectionId, primaryConnection);
        try {
            connection.open();
            return connection;
        } catch (android.database.sqlite.SQLiteException ex) {
            connection.dispose(false);
            throw ex;
        }
    }

    // Called by SQLiteConnectionPool only.
    // Closes the database closes and releases all of its associated resources.
    // Do not call methods on the connection after it is closed.  It will probably crash.
    void close() {
        dispose(false);
    }

    private void open() {
        mConnectionPtr = android.database.sqlite.SQLiteConnection.nativeOpen(mConfiguration.path, mConfiguration.openFlags, mConfiguration.label, android.database.sqlite.SQLiteDebug.DEBUG_SQL_STATEMENTS, android.database.sqlite.SQLiteDebug.DEBUG_SQL_TIME);
        setPageSize();
        setForeignKeyModeFromConfiguration();
        setWalModeFromConfiguration();
        setJournalSizeLimit();
        setAutoCheckpointInterval();
        setLocaleFromConfiguration();
        // Register custom functions.
        final int functionCount = mConfiguration.customFunctions.size();
        for (int i = 0; i < functionCount; i++) {
            android.database.sqlite.SQLiteCustomFunction function = mConfiguration.customFunctions.get(i);
            android.database.sqlite.SQLiteConnection.nativeRegisterCustomFunction(mConnectionPtr, function);
        }
    }

    private void dispose(boolean finalized) {
        if (mCloseGuard != null) {
            if (finalized) {
                mCloseGuard.warnIfOpen();
            }
            mCloseGuard.close();
        }
        if (mConnectionPtr != 0) {
            final int cookie = mRecentOperations.beginOperation("close", null, null);
            try {
                mPreparedStatementCache.evictAll();
                android.database.sqlite.SQLiteConnection.nativeClose(mConnectionPtr);
                mConnectionPtr = 0;
            } finally {
                mRecentOperations.endOperation(cookie);
            }
        }
    }

    private void setPageSize() {
        if ((!mConfiguration.isInMemoryDb()) && (!mIsReadOnlyConnection)) {
            final long newValue = android.database.sqlite.SQLiteGlobal.getDefaultPageSize();
            long value = executeForLong("PRAGMA page_size", null, null);
            if (value != newValue) {
                execute("PRAGMA page_size=" + newValue, null, null);
            }
        }
    }

    private void setAutoCheckpointInterval() {
        if ((!mConfiguration.isInMemoryDb()) && (!mIsReadOnlyConnection)) {
            final long newValue = android.database.sqlite.SQLiteGlobal.getWALAutoCheckpoint();
            long value = executeForLong("PRAGMA wal_autocheckpoint", null, null);
            if (value != newValue) {
                executeForLong("PRAGMA wal_autocheckpoint=" + newValue, null, null);
            }
        }
    }

    private void setJournalSizeLimit() {
        if ((!mConfiguration.isInMemoryDb()) && (!mIsReadOnlyConnection)) {
            final long newValue = android.database.sqlite.SQLiteGlobal.getJournalSizeLimit();
            long value = executeForLong("PRAGMA journal_size_limit", null, null);
            if (value != newValue) {
                executeForLong("PRAGMA journal_size_limit=" + newValue, null, null);
            }
        }
    }

    private void setForeignKeyModeFromConfiguration() {
        if (!mIsReadOnlyConnection) {
            final long newValue = (mConfiguration.foreignKeyConstraintsEnabled) ? 1 : 0;
            long value = executeForLong("PRAGMA foreign_keys", null, null);
            if (value != newValue) {
                execute("PRAGMA foreign_keys=" + newValue, null, null);
            }
        }
    }

    private void setWalModeFromConfiguration() {
        if ((!mConfiguration.isInMemoryDb()) && (!mIsReadOnlyConnection)) {
            if ((mConfiguration.openFlags & android.database.sqlite.SQLiteDatabase.ENABLE_WRITE_AHEAD_LOGGING) != 0) {
                setJournalMode("WAL");
                setSyncMode(android.database.sqlite.SQLiteGlobal.getWALSyncMode());
            } else {
                setJournalMode(android.database.sqlite.SQLiteGlobal.getDefaultJournalMode());
                setSyncMode(android.database.sqlite.SQLiteGlobal.getDefaultSyncMode());
            }
        }
    }

    private void setSyncMode(java.lang.String newValue) {
        java.lang.String value = executeForString("PRAGMA synchronous", null, null);
        if (!android.database.sqlite.SQLiteConnection.canonicalizeSyncMode(value).equalsIgnoreCase(android.database.sqlite.SQLiteConnection.canonicalizeSyncMode(newValue))) {
            execute("PRAGMA synchronous=" + newValue, null, null);
        }
    }

    private static java.lang.String canonicalizeSyncMode(java.lang.String value) {
        if (value.equals("0")) {
            return "OFF";
        } else
            if (value.equals("1")) {
                return "NORMAL";
            } else
                if (value.equals("2")) {
                    return "FULL";
                }


        return value;
    }

    private void setJournalMode(java.lang.String newValue) {
        java.lang.String value = executeForString("PRAGMA journal_mode", null, null);
        if (!value.equalsIgnoreCase(newValue)) {
            try {
                java.lang.String result = executeForString("PRAGMA journal_mode=" + newValue, null, null);
                if (result.equalsIgnoreCase(newValue)) {
                    return;
                }
                // PRAGMA journal_mode silently fails and returns the original journal
                // mode in some cases if the journal mode could not be changed.
            } catch (android.database.sqlite.SQLiteDatabaseLockedException ex) {
                // This error (SQLITE_BUSY) occurs if one connection has the database
                // open in WAL mode and another tries to change it to non-WAL.
            }
            // Because we always disable WAL mode when a database is first opened
            // (even if we intend to re-enable it), we can encounter problems if
            // there is another open connection to the database somewhere.
            // This can happen for a variety of reasons such as an application opening
            // the same database in multiple processes at the same time or if there is a
            // crashing content provider service that the ActivityManager has
            // removed from its registry but whose process hasn't quite died yet
            // by the time it is restarted in a new process.
            // 
            // If we don't change the journal mode, nothing really bad happens.
            // In the worst case, an application that enables WAL might not actually
            // get it, although it can still use connection pooling.
            android.util.Log.w(android.database.sqlite.SQLiteConnection.TAG, (((((((("Could not change the database journal mode of '" + mConfiguration.label) + "' from '") + value) + "' to '") + newValue) + "' because the database is locked.  This usually means that ") + "there are other open connections to the database which prevents ") + "the database from enabling or disabling write-ahead logging mode.  ") + "Proceeding without changing the journal mode.");
        }
    }

    private void setLocaleFromConfiguration() {
        if ((mConfiguration.openFlags & android.database.sqlite.SQLiteDatabase.NO_LOCALIZED_COLLATORS) != 0) {
            return;
        }
        // Register the localized collators.
        final java.lang.String newLocale = mConfiguration.locale.toString();
        android.database.sqlite.SQLiteConnection.nativeRegisterLocalizedCollators(mConnectionPtr, newLocale);
        // If the database is read-only, we cannot modify the android metadata table
        // or existing indexes.
        if (mIsReadOnlyConnection) {
            return;
        }
        try {
            // Ensure the android metadata table exists.
            execute("CREATE TABLE IF NOT EXISTS android_metadata (locale TEXT)", null, null);
            // Check whether the locale was actually changed.
            final java.lang.String oldLocale = executeForString("SELECT locale FROM android_metadata " + "UNION SELECT NULL ORDER BY locale DESC LIMIT 1", null, null);
            if ((oldLocale != null) && oldLocale.equals(newLocale)) {
                return;
            }
            // Go ahead and update the indexes using the new locale.
            execute("BEGIN", null, null);
            boolean success = false;
            try {
                execute("DELETE FROM android_metadata", null, null);
                execute("INSERT INTO android_metadata (locale) VALUES(?)", new java.lang.Object[]{ newLocale }, null);
                execute("REINDEX LOCALIZED", null, null);
                success = true;
            } finally {
                execute(success ? "COMMIT" : "ROLLBACK", null, null);
            }
        } catch (java.lang.RuntimeException ex) {
            throw new android.database.sqlite.SQLiteException(((("Failed to change locale for db '" + mConfiguration.label) + "' to '") + newLocale) + "'.", ex);
        }
    }

    // Called by SQLiteConnectionPool only.
    void reconfigure(android.database.sqlite.SQLiteDatabaseConfiguration configuration) {
        mOnlyAllowReadOnlyOperations = false;
        // Register custom functions.
        final int functionCount = configuration.customFunctions.size();
        for (int i = 0; i < functionCount; i++) {
            android.database.sqlite.SQLiteCustomFunction function = configuration.customFunctions.get(i);
            if (!mConfiguration.customFunctions.contains(function)) {
                android.database.sqlite.SQLiteConnection.nativeRegisterCustomFunction(mConnectionPtr, function);
            }
        }
        // Remember what changed.
        boolean foreignKeyModeChanged = configuration.foreignKeyConstraintsEnabled != mConfiguration.foreignKeyConstraintsEnabled;
        boolean walModeChanged = ((configuration.openFlags ^ mConfiguration.openFlags) & android.database.sqlite.SQLiteDatabase.ENABLE_WRITE_AHEAD_LOGGING) != 0;
        boolean localeChanged = !configuration.locale.equals(mConfiguration.locale);
        // Update configuration parameters.
        mConfiguration.updateParametersFrom(configuration);
        // Update prepared statement cache size.
        mPreparedStatementCache.resize(configuration.maxSqlCacheSize);
        // Update foreign key mode.
        if (foreignKeyModeChanged) {
            setForeignKeyModeFromConfiguration();
        }
        // Update WAL.
        if (walModeChanged) {
            setWalModeFromConfiguration();
        }
        // Update locale.
        if (localeChanged) {
            setLocaleFromConfiguration();
        }
    }

    // Called by SQLiteConnectionPool only.
    // When set to true, executing write operations will throw SQLiteException.
    // Preparing statements that might write is ok, just don't execute them.
    void setOnlyAllowReadOnlyOperations(boolean readOnly) {
        mOnlyAllowReadOnlyOperations = readOnly;
    }

    // Called by SQLiteConnectionPool only.
    // Returns true if the prepared statement cache contains the specified SQL.
    boolean isPreparedStatementInCache(java.lang.String sql) {
        return mPreparedStatementCache.get(sql) != null;
    }

    /**
     * Gets the unique id of this connection.
     *
     * @return The connection id.
     */
    public int getConnectionId() {
        return mConnectionId;
    }

    /**
     * Returns true if this is the primary database connection.
     *
     * @return True if this is the primary database connection.
     */
    public boolean isPrimaryConnection() {
        return mIsPrimaryConnection;
    }

    /**
     * Prepares a statement for execution but does not bind its parameters or execute it.
     * <p>
     * This method can be used to check for syntax errors during compilation
     * prior to execution of the statement.  If the {@code outStatementInfo} argument
     * is not null, the provided {@link SQLiteStatementInfo} object is populated
     * with information about the statement.
     * </p><p>
     * A prepared statement makes no reference to the arguments that may eventually
     * be bound to it, consequently it it possible to cache certain prepared statements
     * such as SELECT or INSERT/UPDATE statements.  If the statement is cacheable,
     * then it will be stored in the cache for later.
     * </p><p>
     * To take advantage of this behavior as an optimization, the connection pool
     * provides a method to acquire a connection that already has a given SQL statement
     * in its prepared statement cache so that it is ready for execution.
     * </p>
     *
     * @param sql
     * 		The SQL statement to prepare.
     * @param outStatementInfo
     * 		The {@link SQLiteStatementInfo} object to populate
     * 		with information about the statement, or null if none.
     * @throws SQLiteException
     * 		if an error occurs, such as a syntax error.
     */
    public void prepare(java.lang.String sql, android.database.sqlite.SQLiteStatementInfo outStatementInfo) {
        if (sql == null) {
            throw new java.lang.IllegalArgumentException("sql must not be null.");
        }
        final int cookie = mRecentOperations.beginOperation("prepare", sql, null);
        try {
            final android.database.sqlite.SQLiteConnection.PreparedStatement statement = acquirePreparedStatement(sql);
            try {
                if (outStatementInfo != null) {
                    outStatementInfo.numParameters = statement.mNumParameters;
                    outStatementInfo.readOnly = statement.mReadOnly;
                    final int columnCount = android.database.sqlite.SQLiteConnection.nativeGetColumnCount(mConnectionPtr, statement.mStatementPtr);
                    if (columnCount == 0) {
                        outStatementInfo.columnNames = android.database.sqlite.SQLiteConnection.EMPTY_STRING_ARRAY;
                    } else {
                        outStatementInfo.columnNames = new java.lang.String[columnCount];
                        for (int i = 0; i < columnCount; i++) {
                            outStatementInfo.columnNames[i] = android.database.sqlite.SQLiteConnection.nativeGetColumnName(mConnectionPtr, statement.mStatementPtr, i);
                        }
                    }
                }
            } finally {
                releasePreparedStatement(statement);
            }
        } catch (java.lang.RuntimeException ex) {
            mRecentOperations.failOperation(cookie, ex);
            throw ex;
        } finally {
            mRecentOperations.endOperation(cookie);
        }
    }

    /**
     * Executes a statement that does not return a result.
     *
     * @param sql
     * 		The SQL statement to execute.
     * @param bindArgs
     * 		The arguments to bind, or null if none.
     * @param cancellationSignal
     * 		A signal to cancel the operation in progress, or null if none.
     * @throws SQLiteException
     * 		if an error occurs, such as a syntax error
     * 		or invalid number of bind arguments.
     * @throws OperationCanceledException
     * 		if the operation was canceled.
     */
    public void execute(java.lang.String sql, java.lang.Object[] bindArgs, android.os.CancellationSignal cancellationSignal) {
        if (sql == null) {
            throw new java.lang.IllegalArgumentException("sql must not be null.");
        }
        final int cookie = mRecentOperations.beginOperation("execute", sql, bindArgs);
        try {
            final android.database.sqlite.SQLiteConnection.PreparedStatement statement = acquirePreparedStatement(sql);
            try {
                throwIfStatementForbidden(statement);
                bindArguments(statement, bindArgs);
                applyBlockGuardPolicy(statement);
                attachCancellationSignal(cancellationSignal);
                try {
                    android.database.sqlite.SQLiteConnection.nativeExecute(mConnectionPtr, statement.mStatementPtr);
                } finally {
                    detachCancellationSignal(cancellationSignal);
                }
            } finally {
                releasePreparedStatement(statement);
            }
        } catch (java.lang.RuntimeException ex) {
            mRecentOperations.failOperation(cookie, ex);
            throw ex;
        } finally {
            mRecentOperations.endOperation(cookie);
        }
    }

    /**
     * Executes a statement that returns a single <code>long</code> result.
     *
     * @param sql
     * 		The SQL statement to execute.
     * @param bindArgs
     * 		The arguments to bind, or null if none.
     * @param cancellationSignal
     * 		A signal to cancel the operation in progress, or null if none.
     * @return The value of the first column in the first row of the result set
    as a <code>long</code>, or zero if none.
     * @throws SQLiteException
     * 		if an error occurs, such as a syntax error
     * 		or invalid number of bind arguments.
     * @throws OperationCanceledException
     * 		if the operation was canceled.
     */
    public long executeForLong(java.lang.String sql, java.lang.Object[] bindArgs, android.os.CancellationSignal cancellationSignal) {
        if (sql == null) {
            throw new java.lang.IllegalArgumentException("sql must not be null.");
        }
        final int cookie = mRecentOperations.beginOperation("executeForLong", sql, bindArgs);
        try {
            final android.database.sqlite.SQLiteConnection.PreparedStatement statement = acquirePreparedStatement(sql);
            try {
                throwIfStatementForbidden(statement);
                bindArguments(statement, bindArgs);
                applyBlockGuardPolicy(statement);
                attachCancellationSignal(cancellationSignal);
                try {
                    return android.database.sqlite.SQLiteConnection.nativeExecuteForLong(mConnectionPtr, statement.mStatementPtr);
                } finally {
                    detachCancellationSignal(cancellationSignal);
                }
            } finally {
                releasePreparedStatement(statement);
            }
        } catch (java.lang.RuntimeException ex) {
            mRecentOperations.failOperation(cookie, ex);
            throw ex;
        } finally {
            mRecentOperations.endOperation(cookie);
        }
    }

    /**
     * Executes a statement that returns a single {@link String} result.
     *
     * @param sql
     * 		The SQL statement to execute.
     * @param bindArgs
     * 		The arguments to bind, or null if none.
     * @param cancellationSignal
     * 		A signal to cancel the operation in progress, or null if none.
     * @return The value of the first column in the first row of the result set
    as a <code>String</code>, or null if none.
     * @throws SQLiteException
     * 		if an error occurs, such as a syntax error
     * 		or invalid number of bind arguments.
     * @throws OperationCanceledException
     * 		if the operation was canceled.
     */
    public java.lang.String executeForString(java.lang.String sql, java.lang.Object[] bindArgs, android.os.CancellationSignal cancellationSignal) {
        if (sql == null) {
            throw new java.lang.IllegalArgumentException("sql must not be null.");
        }
        final int cookie = mRecentOperations.beginOperation("executeForString", sql, bindArgs);
        try {
            final android.database.sqlite.SQLiteConnection.PreparedStatement statement = acquirePreparedStatement(sql);
            try {
                throwIfStatementForbidden(statement);
                bindArguments(statement, bindArgs);
                applyBlockGuardPolicy(statement);
                attachCancellationSignal(cancellationSignal);
                try {
                    return android.database.sqlite.SQLiteConnection.nativeExecuteForString(mConnectionPtr, statement.mStatementPtr);
                } finally {
                    detachCancellationSignal(cancellationSignal);
                }
            } finally {
                releasePreparedStatement(statement);
            }
        } catch (java.lang.RuntimeException ex) {
            mRecentOperations.failOperation(cookie, ex);
            throw ex;
        } finally {
            mRecentOperations.endOperation(cookie);
        }
    }

    /**
     * Executes a statement that returns a single BLOB result as a
     * file descriptor to a shared memory region.
     *
     * @param sql
     * 		The SQL statement to execute.
     * @param bindArgs
     * 		The arguments to bind, or null if none.
     * @param cancellationSignal
     * 		A signal to cancel the operation in progress, or null if none.
     * @return The file descriptor for a shared memory region that contains
    the value of the first column in the first row of the result set as a BLOB,
    or null if none.
     * @throws SQLiteException
     * 		if an error occurs, such as a syntax error
     * 		or invalid number of bind arguments.
     * @throws OperationCanceledException
     * 		if the operation was canceled.
     */
    public android.os.ParcelFileDescriptor executeForBlobFileDescriptor(java.lang.String sql, java.lang.Object[] bindArgs, android.os.CancellationSignal cancellationSignal) {
        if (sql == null) {
            throw new java.lang.IllegalArgumentException("sql must not be null.");
        }
        final int cookie = mRecentOperations.beginOperation("executeForBlobFileDescriptor", sql, bindArgs);
        try {
            final android.database.sqlite.SQLiteConnection.PreparedStatement statement = acquirePreparedStatement(sql);
            try {
                throwIfStatementForbidden(statement);
                bindArguments(statement, bindArgs);
                applyBlockGuardPolicy(statement);
                attachCancellationSignal(cancellationSignal);
                try {
                    int fd = android.database.sqlite.SQLiteConnection.nativeExecuteForBlobFileDescriptor(mConnectionPtr, statement.mStatementPtr);
                    return fd >= 0 ? android.os.ParcelFileDescriptor.adoptFd(fd) : null;
                } finally {
                    detachCancellationSignal(cancellationSignal);
                }
            } finally {
                releasePreparedStatement(statement);
            }
        } catch (java.lang.RuntimeException ex) {
            mRecentOperations.failOperation(cookie, ex);
            throw ex;
        } finally {
            mRecentOperations.endOperation(cookie);
        }
    }

    /**
     * Executes a statement that returns a count of the number of rows
     * that were changed.  Use for UPDATE or DELETE SQL statements.
     *
     * @param sql
     * 		The SQL statement to execute.
     * @param bindArgs
     * 		The arguments to bind, or null if none.
     * @param cancellationSignal
     * 		A signal to cancel the operation in progress, or null if none.
     * @return The number of rows that were changed.
     * @throws SQLiteException
     * 		if an error occurs, such as a syntax error
     * 		or invalid number of bind arguments.
     * @throws OperationCanceledException
     * 		if the operation was canceled.
     */
    public int executeForChangedRowCount(java.lang.String sql, java.lang.Object[] bindArgs, android.os.CancellationSignal cancellationSignal) {
        if (sql == null) {
            throw new java.lang.IllegalArgumentException("sql must not be null.");
        }
        int changedRows = 0;
        final int cookie = mRecentOperations.beginOperation("executeForChangedRowCount", sql, bindArgs);
        try {
            final android.database.sqlite.SQLiteConnection.PreparedStatement statement = acquirePreparedStatement(sql);
            try {
                throwIfStatementForbidden(statement);
                bindArguments(statement, bindArgs);
                applyBlockGuardPolicy(statement);
                attachCancellationSignal(cancellationSignal);
                try {
                    changedRows = android.database.sqlite.SQLiteConnection.nativeExecuteForChangedRowCount(mConnectionPtr, statement.mStatementPtr);
                    return changedRows;
                } finally {
                    detachCancellationSignal(cancellationSignal);
                }
            } finally {
                releasePreparedStatement(statement);
            }
        } catch (java.lang.RuntimeException ex) {
            mRecentOperations.failOperation(cookie, ex);
            throw ex;
        } finally {
            if (mRecentOperations.endOperationDeferLog(cookie)) {
                mRecentOperations.logOperation(cookie, "changedRows=" + changedRows);
            }
        }
    }

    /**
     * Executes a statement that returns the row id of the last row inserted
     * by the statement.  Use for INSERT SQL statements.
     *
     * @param sql
     * 		The SQL statement to execute.
     * @param bindArgs
     * 		The arguments to bind, or null if none.
     * @param cancellationSignal
     * 		A signal to cancel the operation in progress, or null if none.
     * @return The row id of the last row that was inserted, or 0 if none.
     * @throws SQLiteException
     * 		if an error occurs, such as a syntax error
     * 		or invalid number of bind arguments.
     * @throws OperationCanceledException
     * 		if the operation was canceled.
     */
    public long executeForLastInsertedRowId(java.lang.String sql, java.lang.Object[] bindArgs, android.os.CancellationSignal cancellationSignal) {
        if (sql == null) {
            throw new java.lang.IllegalArgumentException("sql must not be null.");
        }
        final int cookie = mRecentOperations.beginOperation("executeForLastInsertedRowId", sql, bindArgs);
        try {
            final android.database.sqlite.SQLiteConnection.PreparedStatement statement = acquirePreparedStatement(sql);
            try {
                throwIfStatementForbidden(statement);
                bindArguments(statement, bindArgs);
                applyBlockGuardPolicy(statement);
                attachCancellationSignal(cancellationSignal);
                try {
                    return android.database.sqlite.SQLiteConnection.nativeExecuteForLastInsertedRowId(mConnectionPtr, statement.mStatementPtr);
                } finally {
                    detachCancellationSignal(cancellationSignal);
                }
            } finally {
                releasePreparedStatement(statement);
            }
        } catch (java.lang.RuntimeException ex) {
            mRecentOperations.failOperation(cookie, ex);
            throw ex;
        } finally {
            mRecentOperations.endOperation(cookie);
        }
    }

    /**
     * Executes a statement and populates the specified {@link CursorWindow}
     * with a range of results.  Returns the number of rows that were counted
     * during query execution.
     *
     * @param sql
     * 		The SQL statement to execute.
     * @param bindArgs
     * 		The arguments to bind, or null if none.
     * @param window
     * 		The cursor window to clear and fill.
     * @param startPos
     * 		The start position for filling the window.
     * @param requiredPos
     * 		The position of a row that MUST be in the window.
     * 		If it won't fit, then the query should discard part of what it filled
     * 		so that it does.  Must be greater than or equal to <code>startPos</code>.
     * @param countAllRows
     * 		True to count all rows that the query would return
     * 		regagless of whether they fit in the window.
     * @param cancellationSignal
     * 		A signal to cancel the operation in progress, or null if none.
     * @return The number of rows that were counted during query execution.  Might
    not be all rows in the result set unless <code>countAllRows</code> is true.
     * @throws SQLiteException
     * 		if an error occurs, such as a syntax error
     * 		or invalid number of bind arguments.
     * @throws OperationCanceledException
     * 		if the operation was canceled.
     */
    public int executeForCursorWindow(java.lang.String sql, java.lang.Object[] bindArgs, android.database.CursorWindow window, int startPos, int requiredPos, boolean countAllRows, android.os.CancellationSignal cancellationSignal) {
        if (sql == null) {
            throw new java.lang.IllegalArgumentException("sql must not be null.");
        }
        if (window == null) {
            throw new java.lang.IllegalArgumentException("window must not be null.");
        }
        window.acquireReference();
        try {
            int actualPos = -1;
            int countedRows = -1;
            int filledRows = -1;
            final int cookie = mRecentOperations.beginOperation("executeForCursorWindow", sql, bindArgs);
            try {
                final android.database.sqlite.SQLiteConnection.PreparedStatement statement = acquirePreparedStatement(sql);
                try {
                    throwIfStatementForbidden(statement);
                    bindArguments(statement, bindArgs);
                    applyBlockGuardPolicy(statement);
                    attachCancellationSignal(cancellationSignal);
                    try {
                        final long result = android.database.sqlite.SQLiteConnection.nativeExecuteForCursorWindow(mConnectionPtr, statement.mStatementPtr, window.mWindowPtr, startPos, requiredPos, countAllRows);
                        actualPos = ((int) (result >> 32));
                        countedRows = ((int) (result));
                        filledRows = window.getNumRows();
                        window.setStartPosition(actualPos);
                        return countedRows;
                    } finally {
                        detachCancellationSignal(cancellationSignal);
                    }
                } finally {
                    releasePreparedStatement(statement);
                }
            } catch (java.lang.RuntimeException ex) {
                mRecentOperations.failOperation(cookie, ex);
                throw ex;
            } finally {
                if (mRecentOperations.endOperationDeferLog(cookie)) {
                    mRecentOperations.logOperation(cookie, (((((((("window='" + window) + "', startPos=") + startPos) + ", actualPos=") + actualPos) + ", filledRows=") + filledRows) + ", countedRows=") + countedRows);
                }
            }
        } finally {
            window.releaseReference();
        }
    }

    private android.database.sqlite.SQLiteConnection.PreparedStatement acquirePreparedStatement(java.lang.String sql) {
        android.database.sqlite.SQLiteConnection.PreparedStatement statement = mPreparedStatementCache.get(sql);
        boolean skipCache = false;
        if (statement != null) {
            if (!statement.mInUse) {
                return statement;
            }
            // The statement is already in the cache but is in use (this statement appears
            // to be not only re-entrant but recursive!).  So prepare a new copy of the
            // statement but do not cache it.
            skipCache = true;
        }
        final long statementPtr = android.database.sqlite.SQLiteConnection.nativePrepareStatement(mConnectionPtr, sql);
        try {
            final int numParameters = android.database.sqlite.SQLiteConnection.nativeGetParameterCount(mConnectionPtr, statementPtr);
            final int type = android.database.DatabaseUtils.getSqlStatementType(sql);
            final boolean readOnly = android.database.sqlite.SQLiteConnection.nativeIsReadOnly(mConnectionPtr, statementPtr);
            statement = obtainPreparedStatement(sql, statementPtr, numParameters, type, readOnly);
            if ((!skipCache) && android.database.sqlite.SQLiteConnection.isCacheable(type)) {
                mPreparedStatementCache.put(sql, statement);
                statement.mInCache = true;
            }
        } catch (java.lang.RuntimeException ex) {
            // Finalize the statement if an exception occurred and we did not add
            // it to the cache.  If it is already in the cache, then leave it there.
            if ((statement == null) || (!statement.mInCache)) {
                android.database.sqlite.SQLiteConnection.nativeFinalizeStatement(mConnectionPtr, statementPtr);
            }
            throw ex;
        }
        statement.mInUse = true;
        return statement;
    }

    private void releasePreparedStatement(android.database.sqlite.SQLiteConnection.PreparedStatement statement) {
        statement.mInUse = false;
        if (statement.mInCache) {
            try {
                android.database.sqlite.SQLiteConnection.nativeResetStatementAndClearBindings(mConnectionPtr, statement.mStatementPtr);
            } catch (android.database.sqlite.SQLiteException ex) {
                // The statement could not be reset due to an error.  Remove it from the cache.
                // When remove() is called, the cache will invoke its entryRemoved() callback,
                // which will in turn call finalizePreparedStatement() to finalize and
                // recycle the statement.
                if (android.database.sqlite.SQLiteConnection.DEBUG) {
                    android.util.Log.d(android.database.sqlite.SQLiteConnection.TAG, ("Could not reset prepared statement due to an exception.  " + "Removing it from the cache.  SQL: ") + android.database.sqlite.SQLiteConnection.trimSqlForDisplay(statement.mSql), ex);
                }
                mPreparedStatementCache.remove(statement.mSql);
            }
        } else {
            finalizePreparedStatement(statement);
        }
    }

    private void finalizePreparedStatement(android.database.sqlite.SQLiteConnection.PreparedStatement statement) {
        android.database.sqlite.SQLiteConnection.nativeFinalizeStatement(mConnectionPtr, statement.mStatementPtr);
        recyclePreparedStatement(statement);
    }

    private void attachCancellationSignal(android.os.CancellationSignal cancellationSignal) {
        if (cancellationSignal != null) {
            cancellationSignal.throwIfCanceled();
            mCancellationSignalAttachCount += 1;
            if (mCancellationSignalAttachCount == 1) {
                // Reset cancellation flag before executing the statement.
                /* cancelable */
                android.database.sqlite.SQLiteConnection.nativeResetCancel(mConnectionPtr, true);
                // After this point, onCancel() may be called concurrently.
                cancellationSignal.setOnCancelListener(this);
            }
        }
    }

    private void detachCancellationSignal(android.os.CancellationSignal cancellationSignal) {
        if (cancellationSignal != null) {
            assert mCancellationSignalAttachCount > 0;
            mCancellationSignalAttachCount -= 1;
            if (mCancellationSignalAttachCount == 0) {
                // After this point, onCancel() cannot be called concurrently.
                cancellationSignal.setOnCancelListener(null);
                // Reset cancellation flag after executing the statement.
                /* cancelable */
                android.database.sqlite.SQLiteConnection.nativeResetCancel(mConnectionPtr, false);
            }
        }
    }

    // CancellationSignal.OnCancelListener callback.
    // This method may be called on a different thread than the executing statement.
    // However, it will only be called between calls to attachCancellationSignal and
    // detachCancellationSignal, while a statement is executing.  We can safely assume
    // that the SQLite connection is still alive.
    @java.lang.Override
    public void onCancel() {
        android.database.sqlite.SQLiteConnection.nativeCancel(mConnectionPtr);
    }

    private void bindArguments(android.database.sqlite.SQLiteConnection.PreparedStatement statement, java.lang.Object[] bindArgs) {
        final int count = (bindArgs != null) ? bindArgs.length : 0;
        if (count != statement.mNumParameters) {
            throw new android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException(((("Expected " + statement.mNumParameters) + " bind arguments but ") + count) + " were provided.");
        }
        if (count == 0) {
            return;
        }
        final long statementPtr = statement.mStatementPtr;
        for (int i = 0; i < count; i++) {
            final java.lang.Object arg = bindArgs[i];
            switch (android.database.DatabaseUtils.getTypeOfObject(arg)) {
                case android.database.Cursor.FIELD_TYPE_NULL :
                    android.database.sqlite.SQLiteConnection.nativeBindNull(mConnectionPtr, statementPtr, i + 1);
                    break;
                case android.database.Cursor.FIELD_TYPE_INTEGER :
                    android.database.sqlite.SQLiteConnection.nativeBindLong(mConnectionPtr, statementPtr, i + 1, ((java.lang.Number) (arg)).longValue());
                    break;
                case android.database.Cursor.FIELD_TYPE_FLOAT :
                    android.database.sqlite.SQLiteConnection.nativeBindDouble(mConnectionPtr, statementPtr, i + 1, ((java.lang.Number) (arg)).doubleValue());
                    break;
                case android.database.Cursor.FIELD_TYPE_BLOB :
                    android.database.sqlite.SQLiteConnection.nativeBindBlob(mConnectionPtr, statementPtr, i + 1, ((byte[]) (arg)));
                    break;
                case android.database.Cursor.FIELD_TYPE_STRING :
                default :
                    if (arg instanceof java.lang.Boolean) {
                        // Provide compatibility with legacy applications which may pass
                        // Boolean values in bind args.
                        android.database.sqlite.SQLiteConnection.nativeBindLong(mConnectionPtr, statementPtr, i + 1, ((java.lang.Boolean) (arg)).booleanValue() ? 1 : 0);
                    } else {
                        android.database.sqlite.SQLiteConnection.nativeBindString(mConnectionPtr, statementPtr, i + 1, arg.toString());
                    }
                    break;
            }
        }
    }

    private void throwIfStatementForbidden(android.database.sqlite.SQLiteConnection.PreparedStatement statement) {
        if (mOnlyAllowReadOnlyOperations && (!statement.mReadOnly)) {
            throw new android.database.sqlite.SQLiteException("Cannot execute this statement because it " + "might modify the database but the connection is read-only.");
        }
    }

    private static boolean isCacheable(int statementType) {
        if ((statementType == android.database.DatabaseUtils.STATEMENT_UPDATE) || (statementType == android.database.DatabaseUtils.STATEMENT_SELECT)) {
            return true;
        }
        return false;
    }

    private void applyBlockGuardPolicy(android.database.sqlite.SQLiteConnection.PreparedStatement statement) {
        if (!mConfiguration.isInMemoryDb()) {
            if (statement.mReadOnly) {
                dalvik.system.BlockGuard.getThreadPolicy().onReadFromDisk();
            } else {
                dalvik.system.BlockGuard.getThreadPolicy().onWriteToDisk();
            }
        }
    }

    /**
     * Dumps debugging information about this connection.
     *
     * @param printer
     * 		The printer to receive the dump, not null.
     * @param verbose
     * 		True to dump more verbose information.
     */
    public void dump(android.util.Printer printer, boolean verbose) {
        dumpUnsafe(printer, verbose);
    }

    /**
     * Dumps debugging information about this connection, in the case where the
     * caller might not actually own the connection.
     *
     * This function is written so that it may be called by a thread that does not
     * own the connection.  We need to be very careful because the connection state is
     * not synchronized.
     *
     * At worst, the method may return stale or slightly wrong data, however
     * it should not crash.  This is ok as it is only used for diagnostic purposes.
     *
     * @param printer
     * 		The printer to receive the dump, not null.
     * @param verbose
     * 		True to dump more verbose information.
     */
    void dumpUnsafe(android.util.Printer printer, boolean verbose) {
        printer.println(("Connection #" + mConnectionId) + ":");
        if (verbose) {
            printer.println("  connectionPtr: 0x" + java.lang.Long.toHexString(mConnectionPtr));
        }
        printer.println("  isPrimaryConnection: " + mIsPrimaryConnection);
        printer.println("  onlyAllowReadOnlyOperations: " + mOnlyAllowReadOnlyOperations);
        mRecentOperations.dump(printer, verbose);
        if (verbose) {
            mPreparedStatementCache.dump(printer);
        }
    }

    /**
     * Describes the currently executing operation, in the case where the
     * caller might not actually own the connection.
     *
     * This function is written so that it may be called by a thread that does not
     * own the connection.  We need to be very careful because the connection state is
     * not synchronized.
     *
     * At worst, the method may return stale or slightly wrong data, however
     * it should not crash.  This is ok as it is only used for diagnostic purposes.
     *
     * @return A description of the current operation including how long it has been running,
    or null if none.
     */
    java.lang.String describeCurrentOperationUnsafe() {
        return mRecentOperations.describeCurrentOperation();
    }

    /**
     * Collects statistics about database connection memory usage.
     *
     * @param dbStatsList
     * 		The list to populate.
     */
    void collectDbStats(java.util.ArrayList<android.database.sqlite.SQLiteDebug.DbStats> dbStatsList) {
        // Get information about the main database.
        int lookaside = android.database.sqlite.SQLiteConnection.nativeGetDbLookaside(mConnectionPtr);
        long pageCount = 0;
        long pageSize = 0;
        try {
            pageCount = executeForLong("PRAGMA page_count;", null, null);
            pageSize = executeForLong("PRAGMA page_size;", null, null);
        } catch (android.database.sqlite.SQLiteException ex) {
            // Ignore.
        }
        dbStatsList.add(getMainDbStatsUnsafe(lookaside, pageCount, pageSize));
        // Get information about attached databases.
        // We ignore the first row in the database list because it corresponds to
        // the main database which we have already described.
        android.database.CursorWindow window = new android.database.CursorWindow("collectDbStats");
        try {
            executeForCursorWindow("PRAGMA database_list;", null, window, 0, 0, false, null);
            for (int i = 1; i < window.getNumRows(); i++) {
                java.lang.String name = window.getString(i, 1);
                java.lang.String path = window.getString(i, 2);
                pageCount = 0;
                pageSize = 0;
                try {
                    pageCount = executeForLong(("PRAGMA " + name) + ".page_count;", null, null);
                    pageSize = executeForLong(("PRAGMA " + name) + ".page_size;", null, null);
                } catch (android.database.sqlite.SQLiteException ex) {
                    // Ignore.
                }
                java.lang.String label = "  (attached) " + name;
                if (!path.isEmpty()) {
                    label += ": " + path;
                }
                dbStatsList.add(new android.database.sqlite.SQLiteDebug.DbStats(label, pageCount, pageSize, 0, 0, 0, 0));
            }
        } catch (android.database.sqlite.SQLiteException ex) {
            // Ignore.
        } finally {
            window.close();
        }
    }

    /**
     * Collects statistics about database connection memory usage, in the case where the
     * caller might not actually own the connection.
     *
     * @return The statistics object, never null.
     */
    void collectDbStatsUnsafe(java.util.ArrayList<android.database.sqlite.SQLiteDebug.DbStats> dbStatsList) {
        dbStatsList.add(getMainDbStatsUnsafe(0, 0, 0));
    }

    private android.database.sqlite.SQLiteDebug.DbStats getMainDbStatsUnsafe(int lookaside, long pageCount, long pageSize) {
        // The prepared statement cache is thread-safe so we can access its statistics
        // even if we do not own the database connection.
        java.lang.String label = mConfiguration.path;
        if (!mIsPrimaryConnection) {
            label += (" (" + mConnectionId) + ")";
        }
        return new android.database.sqlite.SQLiteDebug.DbStats(label, pageCount, pageSize, lookaside, mPreparedStatementCache.hitCount(), mPreparedStatementCache.missCount(), mPreparedStatementCache.size());
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("SQLiteConnection: " + mConfiguration.path) + " (") + mConnectionId) + ")";
    }

    private android.database.sqlite.SQLiteConnection.PreparedStatement obtainPreparedStatement(java.lang.String sql, long statementPtr, int numParameters, int type, boolean readOnly) {
        android.database.sqlite.SQLiteConnection.PreparedStatement statement = mPreparedStatementPool;
        if (statement != null) {
            mPreparedStatementPool = statement.mPoolNext;
            statement.mPoolNext = null;
            statement.mInCache = false;
        } else {
            statement = new android.database.sqlite.SQLiteConnection.PreparedStatement();
        }
        statement.mSql = sql;
        statement.mStatementPtr = statementPtr;
        statement.mNumParameters = numParameters;
        statement.mType = type;
        statement.mReadOnly = readOnly;
        return statement;
    }

    private void recyclePreparedStatement(android.database.sqlite.SQLiteConnection.PreparedStatement statement) {
        statement.mSql = null;
        statement.mPoolNext = mPreparedStatementPool;
        mPreparedStatementPool = statement;
    }

    private static java.lang.String trimSqlForDisplay(java.lang.String sql) {
        // Note: Creating and caching a regular expression is expensive at preload-time
        // and stops compile-time initialization. This pattern is only used when
        // dumping the connection, which is a rare (mainly error) case. So:
        // DO NOT CACHE.
        return sql.replaceAll("[\\s]*\\n+[\\s]*", " ");
    }

    /**
     * Holder type for a prepared statement.
     *
     * Although this object holds a pointer to a native statement object, it
     * does not have a finalizer.  This is deliberate.  The {@link SQLiteConnection}
     * owns the statement object and will take care of freeing it when needed.
     * In particular, closing the connection requires a guarantee of deterministic
     * resource disposal because all native statement objects must be freed before
     * the native database object can be closed.  So no finalizers here.
     */
    private static final class PreparedStatement {
        // Next item in pool.
        public android.database.sqlite.SQLiteConnection.PreparedStatement mPoolNext;

        // The SQL from which the statement was prepared.
        public java.lang.String mSql;

        // The native sqlite3_stmt object pointer.
        // Lifetime is managed explicitly by the connection.
        public long mStatementPtr;

        // The number of parameters that the prepared statement has.
        public int mNumParameters;

        // The statement type.
        public int mType;

        // True if the statement is read-only.
        public boolean mReadOnly;

        // True if the statement is in the cache.
        public boolean mInCache;

        // True if the statement is in use (currently executing).
        // We need this flag because due to the use of custom functions in triggers, it's
        // possible for SQLite calls to be re-entrant.  Consequently we need to prevent
        // in use statements from being finalized until they are no longer in use.
        public boolean mInUse;
    }

    private final class PreparedStatementCache extends android.util.LruCache<java.lang.String, android.database.sqlite.SQLiteConnection.PreparedStatement> {
        public PreparedStatementCache(int size) {
            super(size);
        }

        @java.lang.Override
        protected void entryRemoved(boolean evicted, java.lang.String key, android.database.sqlite.SQLiteConnection.PreparedStatement oldValue, android.database.sqlite.SQLiteConnection.PreparedStatement newValue) {
            oldValue.mInCache = false;
            if (!oldValue.mInUse) {
                finalizePreparedStatement(oldValue);
            }
        }

        public void dump(android.util.Printer printer) {
            printer.println("  Prepared statement cache:");
            java.util.Map<java.lang.String, android.database.sqlite.SQLiteConnection.PreparedStatement> cache = snapshot();
            if (!cache.isEmpty()) {
                int i = 0;
                for (java.util.Map.Entry<java.lang.String, android.database.sqlite.SQLiteConnection.PreparedStatement> entry : cache.entrySet()) {
                    android.database.sqlite.SQLiteConnection.PreparedStatement statement = entry.getValue();
                    if (statement.mInCache) {
                        // might be false due to a race with entryRemoved
                        java.lang.String sql = entry.getKey();
                        printer.println(((((((((((("    " + i) + ": statementPtr=0x") + java.lang.Long.toHexString(statement.mStatementPtr)) + ", numParameters=") + statement.mNumParameters) + ", type=") + statement.mType) + ", readOnly=") + statement.mReadOnly) + ", sql=\"") + android.database.sqlite.SQLiteConnection.trimSqlForDisplay(sql)) + "\"");
                    }
                    i += 1;
                }
            } else {
                printer.println("    <none>");
            }
        }
    }

    private static final class OperationLog {
        private static final int MAX_RECENT_OPERATIONS = 20;

        private static final int COOKIE_GENERATION_SHIFT = 8;

        private static final int COOKIE_INDEX_MASK = 0xff;

        private final android.database.sqlite.SQLiteConnection.Operation[] mOperations = new android.database.sqlite.SQLiteConnection.Operation[android.database.sqlite.SQLiteConnection.OperationLog.MAX_RECENT_OPERATIONS];

        private int mIndex;

        private int mGeneration;

        public int beginOperation(java.lang.String kind, java.lang.String sql, java.lang.Object[] bindArgs) {
            synchronized(mOperations) {
                final int index = (mIndex + 1) % android.database.sqlite.SQLiteConnection.OperationLog.MAX_RECENT_OPERATIONS;
                android.database.sqlite.SQLiteConnection.Operation operation = mOperations[index];
                if (operation == null) {
                    operation = new android.database.sqlite.SQLiteConnection.Operation();
                    mOperations[index] = operation;
                } else {
                    operation.mFinished = false;
                    operation.mException = null;
                    if (operation.mBindArgs != null) {
                        operation.mBindArgs.clear();
                    }
                }
                operation.mStartWallTime = java.lang.System.currentTimeMillis();
                operation.mStartTime = android.os.SystemClock.uptimeMillis();
                operation.mKind = kind;
                operation.mSql = sql;
                if (bindArgs != null) {
                    if (operation.mBindArgs == null) {
                        operation.mBindArgs = new java.util.ArrayList<java.lang.Object>();
                    } else {
                        operation.mBindArgs.clear();
                    }
                    for (int i = 0; i < bindArgs.length; i++) {
                        final java.lang.Object arg = bindArgs[i];
                        if ((arg != null) && (arg instanceof byte[])) {
                            // Don't hold onto the real byte array longer than necessary.
                            operation.mBindArgs.add(android.database.sqlite.SQLiteConnection.EMPTY_BYTE_ARRAY);
                        } else {
                            operation.mBindArgs.add(arg);
                        }
                    }
                }
                operation.mCookie = newOperationCookieLocked(index);
                if (android.os.Trace.isTagEnabled(android.os.Trace.TRACE_TAG_DATABASE)) {
                    android.os.Trace.asyncTraceBegin(android.os.Trace.TRACE_TAG_DATABASE, operation.getTraceMethodName(), operation.mCookie);
                }
                mIndex = index;
                return operation.mCookie;
            }
        }

        public void failOperation(int cookie, java.lang.Exception ex) {
            synchronized(mOperations) {
                final android.database.sqlite.SQLiteConnection.Operation operation = getOperationLocked(cookie);
                if (operation != null) {
                    operation.mException = ex;
                }
            }
        }

        public void endOperation(int cookie) {
            synchronized(mOperations) {
                if (endOperationDeferLogLocked(cookie)) {
                    logOperationLocked(cookie, null);
                }
            }
        }

        public boolean endOperationDeferLog(int cookie) {
            synchronized(mOperations) {
                return endOperationDeferLogLocked(cookie);
            }
        }

        public void logOperation(int cookie, java.lang.String detail) {
            synchronized(mOperations) {
                logOperationLocked(cookie, detail);
            }
        }

        private boolean endOperationDeferLogLocked(int cookie) {
            final android.database.sqlite.SQLiteConnection.Operation operation = getOperationLocked(cookie);
            if (operation != null) {
                if (android.os.Trace.isTagEnabled(android.os.Trace.TRACE_TAG_DATABASE)) {
                    android.os.Trace.asyncTraceEnd(android.os.Trace.TRACE_TAG_DATABASE, operation.getTraceMethodName(), operation.mCookie);
                }
                operation.mEndTime = android.os.SystemClock.uptimeMillis();
                operation.mFinished = true;
                return android.database.sqlite.SQLiteDebug.DEBUG_LOG_SLOW_QUERIES && android.database.sqlite.SQLiteDebug.shouldLogSlowQuery(operation.mEndTime - operation.mStartTime);
            }
            return false;
        }

        private void logOperationLocked(int cookie, java.lang.String detail) {
            final android.database.sqlite.SQLiteConnection.Operation operation = getOperationLocked(cookie);
            java.lang.StringBuilder msg = new java.lang.StringBuilder();
            operation.describe(msg, false);
            if (detail != null) {
                msg.append(", ").append(detail);
            }
            android.util.Log.d(android.database.sqlite.SQLiteConnection.TAG, msg.toString());
        }

        private int newOperationCookieLocked(int index) {
            final int generation = mGeneration++;
            return (generation << android.database.sqlite.SQLiteConnection.OperationLog.COOKIE_GENERATION_SHIFT) | index;
        }

        private android.database.sqlite.SQLiteConnection.Operation getOperationLocked(int cookie) {
            final int index = cookie & android.database.sqlite.SQLiteConnection.OperationLog.COOKIE_INDEX_MASK;
            final android.database.sqlite.SQLiteConnection.Operation operation = mOperations[index];
            return operation.mCookie == cookie ? operation : null;
        }

        public java.lang.String describeCurrentOperation() {
            synchronized(mOperations) {
                final android.database.sqlite.SQLiteConnection.Operation operation = mOperations[mIndex];
                if ((operation != null) && (!operation.mFinished)) {
                    java.lang.StringBuilder msg = new java.lang.StringBuilder();
                    operation.describe(msg, false);
                    return msg.toString();
                }
                return null;
            }
        }

        public void dump(android.util.Printer printer, boolean verbose) {
            synchronized(mOperations) {
                printer.println("  Most recently executed operations:");
                int index = mIndex;
                android.database.sqlite.SQLiteConnection.Operation operation = mOperations[index];
                if (operation != null) {
                    int n = 0;
                    do {
                        java.lang.StringBuilder msg = new java.lang.StringBuilder();
                        msg.append("    ").append(n).append(": [");
                        msg.append(operation.getFormattedStartTime());
                        msg.append("] ");
                        operation.describe(msg, verbose);
                        printer.println(msg.toString());
                        if (index > 0) {
                            index -= 1;
                        } else {
                            index = android.database.sqlite.SQLiteConnection.OperationLog.MAX_RECENT_OPERATIONS - 1;
                        }
                        n += 1;
                        operation = mOperations[index];
                    } while ((operation != null) && (n < android.database.sqlite.SQLiteConnection.OperationLog.MAX_RECENT_OPERATIONS) );
                } else {
                    printer.println("    <none>");
                }
            }
        }
    }

    private static final class Operation {
        // Trim all SQL statements to 256 characters inside the trace marker.
        // This limit gives plenty of context while leaving space for other
        // entries in the trace buffer (and ensures atrace doesn't truncate the
        // marker for us, potentially losing metadata in the process).
        private static final int MAX_TRACE_METHOD_NAME_LEN = 256;

        public long mStartWallTime;// in System.currentTimeMillis()


        public long mStartTime;// in SystemClock.uptimeMillis();


        public long mEndTime;// in SystemClock.uptimeMillis();


        public java.lang.String mKind;

        public java.lang.String mSql;

        public java.util.ArrayList<java.lang.Object> mBindArgs;

        public boolean mFinished;

        public java.lang.Exception mException;

        public int mCookie;

        public void describe(java.lang.StringBuilder msg, boolean verbose) {
            msg.append(mKind);
            if (mFinished) {
                msg.append(" took ").append(mEndTime - mStartTime).append("ms");
            } else {
                msg.append(" started ").append(java.lang.System.currentTimeMillis() - mStartWallTime).append("ms ago");
            }
            msg.append(" - ").append(getStatus());
            if (mSql != null) {
                msg.append(", sql=\"").append(android.database.sqlite.SQLiteConnection.trimSqlForDisplay(mSql)).append("\"");
            }
            if ((verbose && (mBindArgs != null)) && (mBindArgs.size() != 0)) {
                msg.append(", bindArgs=[");
                final int count = mBindArgs.size();
                for (int i = 0; i < count; i++) {
                    final java.lang.Object arg = mBindArgs.get(i);
                    if (i != 0) {
                        msg.append(", ");
                    }
                    if (arg == null) {
                        msg.append("null");
                    } else
                        if (arg instanceof byte[]) {
                            msg.append("<byte[]>");
                        } else
                            if (arg instanceof java.lang.String) {
                                msg.append("\"").append(((java.lang.String) (arg))).append("\"");
                            } else {
                                msg.append(arg);
                            }


                }
                msg.append("]");
            }
            if (mException != null) {
                msg.append(", exception=\"").append(mException.getMessage()).append("\"");
            }
        }

        private java.lang.String getStatus() {
            if (!mFinished) {
                return "running";
            }
            return mException != null ? "failed" : "succeeded";
        }

        private java.lang.String getTraceMethodName() {
            java.lang.String methodName = (mKind + " ") + mSql;
            if (methodName.length() > android.database.sqlite.SQLiteConnection.Operation.MAX_TRACE_METHOD_NAME_LEN)
                return methodName.substring(0, android.database.sqlite.SQLiteConnection.Operation.MAX_TRACE_METHOD_NAME_LEN);

            return methodName;
        }

        private java.lang.String getFormattedStartTime() {
            // Note: SimpleDateFormat is not thread-safe, cannot be compile-time created, and is
            // relatively expensive to create during preloading. This method is only used
            // when dumping a connection, which is a rare (mainly error) case. So:
            // DO NOT CACHE.
            return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new java.util.Date(mStartWallTime));
        }
    }
}

