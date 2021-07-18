/**
 * Copyright (C) 2009 The Android Open Source Project
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
 * An abstract implementation of a SyncAdapter that spawns a thread to invoke a sync operation.
 * If a sync operation is already in progress when a sync request is received, an error will be
 * returned to the new request and the existing request will be allowed to continue.
 * However if there is no sync in progress then a thread will be spawned and {@link #onPerformSync}
 * will be invoked on that thread.
 * <p>
 * Syncs can be cancelled at any time by the framework. For example a sync that was not
 * user-initiated and lasts longer than 30 minutes will be considered timed-out and cancelled.
 * Similarly the framework will attempt to determine whether or not an adapter is making progress
 * by monitoring its network activity over the course of a minute. If the network traffic over this
 * window is close enough to zero the sync will be cancelled. You can also request the sync be
 * cancelled via {@link ContentResolver#cancelSync(Account, String)} or
 * {@link ContentResolver#cancelSync(SyncRequest)}.
 * <p>
 * A sync is cancelled by issuing a {@link Thread#interrupt()} on the syncing thread. <strong>Either
 * your code in {@link #onPerformSync(Account, Bundle, String, ContentProviderClient, SyncResult)}
 * must check {@link Thread#interrupted()}, or you you must override one of
 * {@link #onSyncCanceled(Thread)}/{@link #onSyncCanceled()}</strong> (depending on whether or not
 * your adapter supports syncing of multiple accounts in parallel). If your adapter does not
 * respect the cancel issued by the framework you run the risk of your app's entire process being
 * killed.
 * <p>
 * In order to be a sync adapter one must extend this class, provide implementations for the
 * abstract methods and write a service that returns the result of {@link #getSyncAdapterBinder()}
 * in the service's {@link android.app.Service#onBind(android.content.Intent)} when invoked
 * with an intent with action <code>android.content.SyncAdapter</code>. This service
 * must specify the following intent filter and metadata tags in its AndroidManifest.xml file
 * <pre>
 *   &lt;intent-filter&gt;
 *     &lt;action android:name="android.content.SyncAdapter" /&gt;
 *   &lt;/intent-filter&gt;
 *   &lt;meta-data android:name="android.content.SyncAdapter"
 *             android:resource="@xml/syncadapter" /&gt;
 * </pre>
 * The <code>android:resource</code> attribute must point to a resource that looks like:
 * <pre>
 * &lt;sync-adapter xmlns:android="http://schemas.android.com/apk/res/android"
 *    android:contentAuthority="authority"
 *    android:accountType="accountType"
 *    android:userVisible="true|false"
 *    android:supportsUploading="true|false"
 *    android:allowParallelSyncs="true|false"
 *    android:isAlwaysSyncable="true|false"
 *    android:syncAdapterSettingsAction="ACTION_OF_SETTINGS_ACTIVITY"
 * /&gt;
 * </pre>
 * <ul>
 * <li>The <code>android:contentAuthority</code> and <code>android:accountType</code> attributes
 * indicate which content authority and for which account types this sync adapter serves.
 * <li><code>android:userVisible</code> defaults to true and controls whether or not this sync
 * adapter shows up in the Sync Settings screen.
 * <li><code>android:supportsUploading</code> defaults
 * to true and if true an upload-only sync will be requested for all syncadapters associated
 * with an authority whenever that authority's content provider does a
 * {@link ContentResolver#notifyChange(android.net.Uri, android.database.ContentObserver, boolean)}
 * with syncToNetwork set to true.
 * <li><code>android:allowParallelSyncs</code> defaults to false and if true indicates that
 * the sync adapter can handle syncs for multiple accounts at the same time. Otherwise
 * the SyncManager will wait until the sync adapter is not in use before requesting that
 * it sync an account's data.
 * <li><code>android:isAlwaysSyncable</code> defaults to false and if true tells the SyncManager
 * to initialize the isSyncable state to 1 for that sync adapter for each account that is added.
 * <li><code>android:syncAdapterSettingsAction</code> defaults to null and if supplied it
 * specifies an Intent action of an activity that can be used to adjust the sync adapter's
 * sync settings. The activity must live in the same package as the sync adapter.
 * </ul>
 */
public abstract class AbstractThreadedSyncAdapter {
    private static final java.lang.String TAG = "SyncAdapter";

    /**
     * Kernel event log tag.  Also listed in data/etc/event-log-tags.
     *
     * @deprecated Private constant.  May go away in the next release.
     */
    @java.lang.Deprecated
    public static final int LOG_SYNC_DETAILS = 2743;

    private static final boolean ENABLE_LOG = android.os.Build.IS_DEBUGGABLE && android.util.Log.isLoggable(android.content.AbstractThreadedSyncAdapter.TAG, Log.DEBUG);

    private final android.content.Context mContext;

    private final java.util.concurrent.atomic.AtomicInteger mNumSyncStarts;

    private final android.content.AbstractThreadedSyncAdapter.ISyncAdapterImpl mISyncAdapterImpl;

    // all accesses to this member variable must be synchronized on mSyncThreadLock
    private final java.util.HashMap<android.accounts.Account, android.content.AbstractThreadedSyncAdapter.SyncThread> mSyncThreads = new java.util.HashMap<android.accounts.Account, android.content.AbstractThreadedSyncAdapter.SyncThread>();

    private final java.lang.Object mSyncThreadLock = new java.lang.Object();

    private final boolean mAutoInitialize;

    private boolean mAllowParallelSyncs;

    /**
     * Creates an {@link AbstractThreadedSyncAdapter}.
     *
     * @param context
     * 		the {@link android.content.Context} that this is running within.
     * @param autoInitialize
     * 		if true then sync requests that have
     * 		{@link ContentResolver#SYNC_EXTRAS_INITIALIZE} set will be internally handled by
     * 		{@link AbstractThreadedSyncAdapter} by calling
     * 		{@link ContentResolver#setIsSyncable(android.accounts.Account, String, int)} with 1 if it
     * 		is currently set to <0.
     */
    public AbstractThreadedSyncAdapter(android.content.Context context, boolean autoInitialize) {
        /* allowParallelSyncs */
        this(context, autoInitialize, false);
    }

    /**
     * Creates an {@link AbstractThreadedSyncAdapter}.
     *
     * @param context
     * 		the {@link android.content.Context} that this is running within.
     * @param autoInitialize
     * 		if true then sync requests that have
     * 		{@link ContentResolver#SYNC_EXTRAS_INITIALIZE} set will be internally handled by
     * 		{@link AbstractThreadedSyncAdapter} by calling
     * 		{@link ContentResolver#setIsSyncable(android.accounts.Account, String, int)} with 1 if it
     * 		is currently set to <0.
     * @param allowParallelSyncs
     * 		if true then allow syncs for different accounts to run
     * 		at the same time, each in their own thread. This must be consistent with the setting
     * 		in the SyncAdapter's configuration file.
     */
    public AbstractThreadedSyncAdapter(android.content.Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        mContext = context;
        mISyncAdapterImpl = new android.content.AbstractThreadedSyncAdapter.ISyncAdapterImpl();
        mNumSyncStarts = new java.util.concurrent.atomic.AtomicInteger(0);
        mAutoInitialize = autoInitialize;
        mAllowParallelSyncs = allowParallelSyncs;
    }

    public android.content.Context getContext() {
        return mContext;
    }

    private android.accounts.Account toSyncKey(android.accounts.Account account) {
        if (mAllowParallelSyncs) {
            return account;
        } else {
            return null;
        }
    }

    private class ISyncAdapterImpl extends android.content.ISyncAdapter.Stub {
        @java.lang.Override
        public void onUnsyncableAccount(android.content.ISyncAdapterUnsyncableAccountCallback cb) {
            android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(android.content.AbstractThreadedSyncAdapter::handleOnUnsyncableAccount, android.content.AbstractThreadedSyncAdapter.this, cb));
        }

        @java.lang.Override
        public void startSync(android.content.ISyncContext syncContext, java.lang.String authority, android.accounts.Account account, android.os.Bundle extras) {
            if (android.content.AbstractThreadedSyncAdapter.ENABLE_LOG) {
                if (extras != null) {
                    extras.size();// Unparcel so its toString() will show the contents.

                }
                android.util.Log.d(android.content.AbstractThreadedSyncAdapter.TAG, (((("startSync() start " + authority) + " ") + account) + " ") + extras);
            }
            try {
                final android.content.SyncContext syncContextClient = new android.content.SyncContext(syncContext);
                boolean alreadyInProgress;
                // synchronize to make sure that mSyncThreads doesn't change between when we
                // check it and when we use it
                final android.accounts.Account threadsKey = toSyncKey(account);
                synchronized(mSyncThreadLock) {
                    if (!mSyncThreads.containsKey(threadsKey)) {
                        if ((mAutoInitialize && (extras != null)) && extras.getBoolean(android.content.ContentResolver.SYNC_EXTRAS_INITIALIZE, false)) {
                            try {
                                if (android.content.ContentResolver.getIsSyncable(account, authority) < 0) {
                                    android.content.ContentResolver.setIsSyncable(account, authority, 1);
                                }
                            } finally {
                                syncContextClient.onFinished(new android.content.SyncResult());
                            }
                            return;
                        }
                        android.content.AbstractThreadedSyncAdapter.SyncThread syncThread = new android.content.AbstractThreadedSyncAdapter.SyncThread("SyncAdapterThread-" + mNumSyncStarts.incrementAndGet(), syncContextClient, authority, account, extras);
                        mSyncThreads.put(threadsKey, syncThread);
                        syncThread.start();
                        alreadyInProgress = false;
                    } else {
                        if (android.content.AbstractThreadedSyncAdapter.ENABLE_LOG) {
                            android.util.Log.d(android.content.AbstractThreadedSyncAdapter.TAG, "  alreadyInProgress");
                        }
                        alreadyInProgress = true;
                    }
                }
                // do this outside since we don't want to call back into the syncContext while
                // holding the synchronization lock
                if (alreadyInProgress) {
                    syncContextClient.onFinished(android.content.SyncResult.ALREADY_IN_PROGRESS);
                }
            } catch (java.lang.RuntimeException | java.lang.Error th) {
                if (android.content.AbstractThreadedSyncAdapter.ENABLE_LOG) {
                    android.util.Log.d(android.content.AbstractThreadedSyncAdapter.TAG, "startSync() caught exception", th);
                }
                throw th;
            } finally {
                if (android.content.AbstractThreadedSyncAdapter.ENABLE_LOG) {
                    android.util.Log.d(android.content.AbstractThreadedSyncAdapter.TAG, "startSync() finishing");
                }
            }
        }

        @java.lang.Override
        public void cancelSync(android.content.ISyncContext syncContext) {
            try {
                // synchronize to make sure that mSyncThreads doesn't change between when we
                // check it and when we use it
                android.content.AbstractThreadedSyncAdapter.SyncThread info = null;
                synchronized(mSyncThreadLock) {
                    for (android.content.AbstractThreadedSyncAdapter.SyncThread current : mSyncThreads.values()) {
                        if (current.mSyncContext.getSyncContextBinder() == syncContext.asBinder()) {
                            info = current;
                            break;
                        }
                    }
                }
                if (info != null) {
                    if (android.content.AbstractThreadedSyncAdapter.ENABLE_LOG) {
                        android.util.Log.d(android.content.AbstractThreadedSyncAdapter.TAG, (("cancelSync() " + info.mAuthority) + " ") + info.mAccount);
                    }
                    if (mAllowParallelSyncs) {
                        onSyncCanceled(info);
                    } else {
                        onSyncCanceled();
                    }
                } else {
                    if (android.content.AbstractThreadedSyncAdapter.ENABLE_LOG) {
                        android.util.Log.w(android.content.AbstractThreadedSyncAdapter.TAG, "cancelSync() unknown context");
                    }
                }
            } catch (java.lang.RuntimeException | java.lang.Error th) {
                if (android.content.AbstractThreadedSyncAdapter.ENABLE_LOG) {
                    android.util.Log.d(android.content.AbstractThreadedSyncAdapter.TAG, "cancelSync() caught exception", th);
                }
                throw th;
            } finally {
                if (android.content.AbstractThreadedSyncAdapter.ENABLE_LOG) {
                    android.util.Log.d(android.content.AbstractThreadedSyncAdapter.TAG, "cancelSync() finishing");
                }
            }
        }
    }

    /**
     * The thread that invokes {@link AbstractThreadedSyncAdapter#onPerformSync}. It also acquires
     * the provider for this sync before calling onPerformSync and releases it afterwards. Cancel
     * this thread in order to cancel the sync.
     */
    private class SyncThread extends java.lang.Thread {
        private final android.content.SyncContext mSyncContext;

        private final java.lang.String mAuthority;

        private final android.accounts.Account mAccount;

        private final android.os.Bundle mExtras;

        private final android.accounts.Account mThreadsKey;

        private SyncThread(java.lang.String name, android.content.SyncContext syncContext, java.lang.String authority, android.accounts.Account account, android.os.Bundle extras) {
            super(name);
            mSyncContext = syncContext;
            mAuthority = authority;
            mAccount = account;
            mExtras = extras;
            mThreadsKey = toSyncKey(account);
        }

        @java.lang.Override
        public void run() {
            java.lang.Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND);
            if (android.content.AbstractThreadedSyncAdapter.ENABLE_LOG) {
                android.util.Log.d(android.content.AbstractThreadedSyncAdapter.TAG, "Thread started");
            }
            // Trace this sync instance.  Note, conceptually this should be in
            // SyncStorageEngine.insertStartSyncEvent(), but the trace functions require unique
            // threads in order to track overlapping operations, so we'll do it here for now.
            android.os.Trace.traceBegin(Trace.TRACE_TAG_SYNC_MANAGER, mAuthority);
            android.content.SyncResult syncResult = new android.content.SyncResult();
            android.content.ContentProviderClient provider = null;
            try {
                if (isCanceled()) {
                    if (android.content.AbstractThreadedSyncAdapter.ENABLE_LOG) {
                        android.util.Log.d(android.content.AbstractThreadedSyncAdapter.TAG, "Already canceled");
                    }
                    return;
                }
                if (android.content.AbstractThreadedSyncAdapter.ENABLE_LOG) {
                    android.util.Log.d(android.content.AbstractThreadedSyncAdapter.TAG, "Calling onPerformSync...");
                }
                provider = mContext.getContentResolver().acquireContentProviderClient(mAuthority);
                if (provider != null) {
                    android.content.AbstractThreadedSyncAdapter.this.onPerformSync(mAccount, mExtras, mAuthority, provider, syncResult);
                } else {
                    syncResult.databaseError = true;
                }
                if (android.content.AbstractThreadedSyncAdapter.ENABLE_LOG) {
                    android.util.Log.d(android.content.AbstractThreadedSyncAdapter.TAG, "onPerformSync done");
                }
            } catch (java.lang.SecurityException e) {
                if (android.content.AbstractThreadedSyncAdapter.ENABLE_LOG) {
                    android.util.Log.d(android.content.AbstractThreadedSyncAdapter.TAG, "SecurityException", e);
                }
                android.content.AbstractThreadedSyncAdapter.this.onSecurityException(mAccount, mExtras, mAuthority, syncResult);
                syncResult.databaseError = true;
            } catch (java.lang.RuntimeException | java.lang.Error th) {
                if (android.content.AbstractThreadedSyncAdapter.ENABLE_LOG) {
                    android.util.Log.d(android.content.AbstractThreadedSyncAdapter.TAG, "caught exception", th);
                }
                throw th;
            } finally {
                android.os.Trace.traceEnd(Trace.TRACE_TAG_SYNC_MANAGER);
                if (provider != null) {
                    provider.release();
                }
                if (!isCanceled()) {
                    mSyncContext.onFinished(syncResult);
                }
                // synchronize so that the assignment will be seen by other threads
                // that also synchronize accesses to mSyncThreads
                synchronized(mSyncThreadLock) {
                    mSyncThreads.remove(mThreadsKey);
                }
                if (android.content.AbstractThreadedSyncAdapter.ENABLE_LOG) {
                    android.util.Log.d(android.content.AbstractThreadedSyncAdapter.TAG, "Thread finished");
                }
            }
        }

        private boolean isCanceled() {
            return java.lang.Thread.currentThread().isInterrupted();
        }
    }

    /**
     *
     *
     * @return a reference to the IBinder of the SyncAdapter service.
     */
    public final android.os.IBinder getSyncAdapterBinder() {
        return mISyncAdapterImpl.asBinder();
    }

    /**
     * Handle a call of onUnsyncableAccount.
     *
     * @param cb
     * 		The callback to report the return value to
     */
    private void handleOnUnsyncableAccount(@android.annotation.NonNull
    android.content.ISyncAdapterUnsyncableAccountCallback cb) {
        boolean doSync;
        try {
            doSync = onUnsyncableAccount();
        } catch (java.lang.RuntimeException e) {
            android.util.Log.e(android.content.AbstractThreadedSyncAdapter.TAG, "Exception while calling onUnsyncableAccount, assuming 'true'", e);
            doSync = true;
        }
        try {
            cb.onUnsyncableAccountDone(doSync);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.content.AbstractThreadedSyncAdapter.TAG, "Could not report result of onUnsyncableAccount", e);
        }
    }

    /**
     * Allows to defer syncing until all accounts are properly set up.
     *
     * <p>Called when a account / authority pair
     * <ul>
     * <li>that can be handled by this adapter</li>
     * <li>{@link ContentResolver#requestSync(SyncRequest) is synced}</li>
     * <li>and the account/provider {@link ContentResolver#getIsSyncable(Account, String) has
     * unknown state (<0)}.</li>
     * </ul>
     *
     * <p>This might be called on a different service connection as {@link #onPerformSync}.
     *
     * <p>The system expects this method to immediately return. If the call stalls the system
     * behaves as if this method returned {@code true}. If it is required to perform a longer task
     * (such as interacting with the user), return {@code false} and proceed in a difference
     * context, such as an {@link android.app.Activity}, or foreground service. The sync can then be
     * rescheduled once the account becomes syncable.
     *
     * @return If {@code false} syncing is deferred. Returns {@code true} by default, i.e. by
    default syncing starts immediately.
     */
    @android.annotation.MainThread
    public boolean onUnsyncableAccount() {
        return true;
    }

    /**
     * Perform a sync for this account. SyncAdapter-specific parameters may
     * be specified in extras, which is guaranteed to not be null. Invocations
     * of this method are guaranteed to be serialized.
     *
     * @param account
     * 		the account that should be synced
     * @param extras
     * 		SyncAdapter-specific parameters
     * @param authority
     * 		the authority of this sync request
     * @param provider
     * 		a ContentProviderClient that points to the ContentProvider for this
     * 		authority
     * @param syncResult
     * 		SyncAdapter-specific parameters
     */
    public abstract void onPerformSync(android.accounts.Account account, android.os.Bundle extras, java.lang.String authority, android.content.ContentProviderClient provider, android.content.SyncResult syncResult);

    /**
     * Report that there was a security exception when opening the content provider
     * prior to calling {@link #onPerformSync}.  This will be treated as a sync
     * database failure.
     *
     * @param account
     * 		the account that attempted to sync
     * @param extras
     * 		SyncAdapter-specific parameters
     * @param authority
     * 		the authority of the failed sync request
     * @param syncResult
     * 		SyncAdapter-specific parameters
     */
    public void onSecurityException(android.accounts.Account account, android.os.Bundle extras, java.lang.String authority, android.content.SyncResult syncResult) {
    }

    /**
     * Indicates that a sync operation has been canceled. This will be invoked on a separate
     * thread than the sync thread and so you must consider the multi-threaded implications
     * of the work that you do in this method.
     * <p>
     * This will only be invoked when the SyncAdapter indicates that it doesn't support
     * parallel syncs.
     */
    public void onSyncCanceled() {
        final android.content.AbstractThreadedSyncAdapter.SyncThread syncThread;
        synchronized(mSyncThreadLock) {
            syncThread = mSyncThreads.get(null);
        }
        if (syncThread != null) {
            syncThread.interrupt();
        }
    }

    /**
     * Indicates that a sync operation has been canceled. This will be invoked on a separate
     * thread than the sync thread and so you must consider the multi-threaded implications
     * of the work that you do in this method.
     * <p>
     * This will only be invoked when the SyncAdapter indicates that it does support
     * parallel syncs.
     *
     * @param thread
     * 		the Thread of the sync that is to be canceled.
     */
    public void onSyncCanceled(java.lang.Thread thread) {
        thread.interrupt();
    }
}

